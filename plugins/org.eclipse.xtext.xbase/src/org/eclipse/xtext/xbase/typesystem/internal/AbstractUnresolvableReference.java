/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.typesystem.internal;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.diagnostics.AbstractDiagnostic;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.linking.ILinkingDiagnosticMessageProvider;
import org.eclipse.xtext.linking.impl.XtextLinkingDiagnostic;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.typesystem.computation.ILinkingCandidate;
import org.eclipse.xtext.xbase.typesystem.computation.ITypeExpectation;
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceHint;
import org.eclipse.xtext.xbase.typesystem.references.AnyTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.OwnedConverter;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@NonNullByDefault
public abstract class AbstractUnresolvableReference implements ILinkingCandidate, ILinkingDiagnosticMessageProvider.ILinkingDiagnosticContext {
	private XExpression expression;
	private ExpressionTypeComputationState state;
	private String text;
	private INode node;

	public AbstractUnresolvableReference(XExpression expression, INode node, String text, ExpressionTypeComputationState state) {
		this.expression = expression;
		this.node = node;
		this.text = text;
		this.state = state;
	}

	protected XExpression getExpression() {
		return expression;
	}

	public void apply() {
		state.getResolvedTypes().acceptLinkingInformation(expression, this);
		computeArgumentTypes();
		for (ITypeExpectation expectation : state.getExpectations()) {
			LightweightTypeReference expectedType = expectation.getExpectedType();
			if (expectedType != null) {
				expectation.acceptActualType(expectedType, ConformanceHint.CHECKED);
			} else {
				expectation.acceptActualType(new AnyTypeReference(expectation.getReferenceOwner()),
						ConformanceHint.CHECKED);
			}
		}
		getResolvedTypes().mergeIntoParent();
	}

	protected StackedResolvedTypes getResolvedTypes() {
		return state.getStackedResolvedTypes();
	}
	
	public boolean validate(IAcceptor<? super AbstractDiagnostic> result) {
		// nothing to do
		return true;
	}

	protected void computeArgumentTypes() {
		List<XExpression> arguments = getArguments();
		for (XExpression argument : arguments) {
			AbstractTypeComputationState argumentState = state.withNonVoidExpectation();
			argumentState.computeTypes(argument);
		}
	}

	protected abstract List<XExpression> getArguments();

	public void resolveLinkingProxy() {
		Resource resource = expression.eResource();
		if (resource instanceof LazyLinkingResource) {
			LazyLinkingResource lazyLinkingResource = (LazyLinkingResource) resource;
			DiagnosticMessage message = lazyLinkingResource.getDiagnosticMessageProvider().getUnresolvedProxyMessage(this);
			if (message != null) {
				List<Resource.Diagnostic> diagnostics = getDiagnosticList(lazyLinkingResource, message);
				Diagnostic diagnostic = createDiagnostic(message);
				diagnostics.add(diagnostic);
			}
			EObject referenced = (InternalEObject) getExpression().eGet(getReference(), false);
			lazyLinkingResource.markUnresolvable(referenced);
		}
	}
	
	protected Resource.Diagnostic createDiagnostic(DiagnosticMessage message) {
		Diagnostic diagnostic = new XtextLinkingDiagnostic(
				node, 
				message.getMessage(),
				message.getIssueCode(), message.getIssueData());
		return diagnostic;
	}
	
	protected List<Diagnostic> getDiagnosticList(LazyLinkingResource resource, @Nullable DiagnosticMessage message) throws AssertionError {
		if (message != null) {
			switch (message.getSeverity()) {
				case ERROR:
					return resource.getErrors();
				case WARNING:
					return resource.getWarnings();
				default:
					throw new AssertionError("Unexpected severity: " + message.getSeverity());
			}
		}
		return Collections.emptyList();
	}
	
	protected INode getNode() {
		return node;
	}

	public ILinkingCandidate getPreferredCandidate(ILinkingCandidate other) {
		return other;
	}

	@Nullable
	public JvmIdentifiableElement getFeature() {
		return null;
	}
	
	public EObject getContext() {
		return expression;
	}
	
	public String getLinkText() {
		return text;
	}
	
	protected OwnedConverter getConverter() {
		return state.getConverter();
	}

}
