/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.typesystem.references;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.xbase.typesystem.conformance.SuperTypeAcceptor;
import org.eclipse.xtext.xbase.typesystem.conformance.TypeConformanceComputationArgument;
import org.eclipse.xtext.xbase.typesystem.conformance.TypeConformanceComputer;
import org.eclipse.xtext.xbase.typesystem.conformance.TypeConformanceResult;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 * TODO document the purpose of this class
 *  - get rid of containment constraints
 *  - easier copying
 *  - isResolved
 *  - getSynonyms
 *  - getSuperTypes (resolved)
 *  - isAssignableFrom
 *  - collectSuperTypes
 *  - getAllSuperTypes
 * ?? - getFeatures (returns feature handles?)
 *  - getFeatureByName
 */
@NonNullByDefault
public abstract class LightweightTypeReference {
	
	protected static class IdentifierFunction implements Function<LightweightTypeReference, String> {
		public String apply(@Nullable LightweightTypeReference reference) {
			if (reference == null)
				throw new NullPointerException("reference");
			return reference.getIdentifier();
		}
	}
	
	protected static class SimpleNameFunction implements Function<LightweightTypeReference, String> {
		public String apply(@Nullable LightweightTypeReference reference) {
			if (reference == null)
				throw new NullPointerException("reference");
			return reference.getSimpleName();
		}
	}
	
	private TypeReferenceOwner owner;
	
	protected LightweightTypeReference(TypeReferenceOwner owner) {
		this.owner = Preconditions.checkNotNull(owner, "owner");
	}

	public boolean isResolved() {
		return true;
	}
	
	public TypeReferenceOwner getOwner() {
		return owner;
	}
	
	protected TypesFactory getTypesFactory() {
		return getOwner().getServices().getTypesFactory();
	}
	
	protected CommonTypeComputationServices getServices() {
		return getOwner().getServices();
	}
	
	public boolean isOwnedBy(TypeReferenceOwner owner) {
		if (isResolved()) {
			return true;
		}
		return owner == getOwner();
	}
	
	protected <T> List<T> expose(@Nullable List<T> list) {
		if (list == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(list);
	}
	
	public abstract JvmTypeReference toTypeReference();
	
	@Nullable
	public abstract JvmType getType();
	
	public LightweightTypeReference getWrapperTypeIfPrimitive() {
		return this;
	}
	
	public LightweightTypeReference getPrimitiveIfWrapperType() {
		return this;
	}
	
	public List<JvmType> getRawTypes() {
		return getServices().getRawTypeHelper().getAllRawTypes(this, getOwner().getContextResourceSet());
	}
	
	/*
	 * Replaced wildcards and type parameters by their respective
	 * constraints. Returns the JvmTypes without arguments.
	 * TODO implement me
	 */
	public LightweightTypeReference getRawTypeReference() {
		return getServices().getRawTypeHelper().getRawTypeReference(this, getOwner().getContextResourceSet());
	}
	
	public boolean isRawType() {
		return false;
	}
	
	public boolean isArray() {
		return false;
	}
	
	public abstract List<LightweightTypeReference> getSuperTypes();
	
	public void collectSuperTypes(SuperTypeAcceptor acceptor) {
		throw new UnsupportedOperationException("Implement me");
	}
	
//	public abstract List<LightweightTypeReference> getAllSuperTypes();
	
	protected List<LightweightTypeReference> getSuperTypes(@Nullable List<LightweightTypeReference> references) {
		if (references == null || references.isEmpty())
			return Collections.emptyList();
		List<LightweightTypeReference> result = Lists.newArrayListWithCapacity(references.size());
		for(LightweightTypeReference reference: references) {
			result.addAll(reference.getSuperTypes());
		}
		return result;
	}
	
	public boolean isPrimitive() {
		return false;
	}
	
	public boolean isPrimitiveVoid() {
		return isType(Void.TYPE);
	}
	
	public boolean isAssignableFrom(LightweightTypeReference reference) {
		TypeConformanceComputationArgument argument = new TypeConformanceComputationArgument(false, false, true);
		return isAssignableFrom(reference, argument);
	}
	
	public boolean isAssignableFrom(LightweightTypeReference reference, TypeConformanceComputationArgument argument) {
		TypeConformanceComputer conformanceCompouter = getOwner().getServices().getTypeConformanceComputer();
		TypeConformanceResult result = conformanceCompouter.isConformant(this, reference, argument);
		return result.isConformant();
	}
	
	public LightweightTypeReference copyInto(TypeReferenceOwner owner) {
		if (isResolved()) {
			return this;
		}
		return doCopyInto(owner);
	}

	protected abstract LightweightTypeReference doCopyInto(TypeReferenceOwner owner);
	
	@Override
	public final String toString() {
		return getSimpleName();
	}
	
	public abstract String getSimpleName();
	
	public abstract String getIdentifier();
	
	protected JvmType findType(Class<?> type) {
		return getServices().getTypeReferences().findDeclaredType(type, getOwner().getContextResourceSet());
	}

	public abstract boolean isType(Class<?> clazz);
	
	public void accept(TypeReferenceVisitor visitor) {
		visitor.doVisitTypeReference(this);
	}
	
	public <Param> void accept(TypeReferenceVisitorWithParameter<Param> visitor, Param param) {
		visitor.doVisitTypeReference(this, param);
	}
	
	@Nullable
	public <Result> Result accept(TypeReferenceVisitorWithResult<Result> visitor) {
		return visitor.doVisitTypeReference(this);
	}
	
	@Nullable
	public <Param, Result> Result accept(TypeReferenceVisitorWithParameterAndResult<Param, Result> visitor, Param param) {
		return visitor.doVisitTypeReference(this, param);
	}
	
	public <Result> Result accept(TypeReferenceVisitorWithNonNullResult<Result> visitor) {
		Result result = accept((TypeReferenceVisitorWithResult<Result>)visitor);
		if (result == null)
			throw new IllegalStateException("result may not be null");
		return result;
	}
	
	public <Param, Result> Result accept(TypeReferenceVisitorWithParameterAndNonNullResult<Param, Result> visitor, Param param) {
		Result result = accept((TypeReferenceVisitorWithParameterAndResult<Param, Result>)visitor, param);
		if (result == null)
			throw new IllegalStateException("result may not be null");
		return result;
	}

	// TODO move to utility / factory
	public CompoundTypeReference toMultiType(LightweightTypeReference reference) {
		if (reference == null) {
			throw new NullPointerException("reference may not be null");
		}
		CompoundTypeReference result = new CompoundTypeReference(getOwner(), false);
		result.addComponent(this);
		result.addComponent(reference);
		return result;
	}

}
