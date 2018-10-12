/*******************************************************************************
 * Copyright (c) 2018 Belfort-Montbeliard University of Technology (http://www.utbm.fr) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.tests.typesystem;

import static org.junit.Assert.assertTrue;

import com.google.inject.Inject;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.tests.AbstractXbaseTestCase;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;
import org.eclipse.xtext.xbase.XExpression;
import org.junit.Test;

/**
 * @author Stephane Galland - Initial contribution and API
 */
public class PrimitiveBooleanComparison extends AbstractXbaseTestCase {

	private static final String SNIPSET1 = "{ true }";

	@Inject
	private TypeReferences typeReferences;

	@Inject
	private CommonTypeComputationServices services;

	@Test
	public void isSubtypeOf_00() throws Exception {
		XExpression code0 = expression(SNIPSET1, false);
		XExpression code1 = expression(SNIPSET1, false);

		JvmTypeReference reference0 = this.typeReferences.getTypeForName("boolean", code0);
		
		StandardTypeReferenceOwner owner = new StandardTypeReferenceOwner(this.services, code1);
		LightweightTypeReference reference1 = owner.newParameterizedTypeReference(this.typeReferences.findDeclaredType("boolean", code1));
		
		assertTrue(reference1.isSubtypeOf(reference0.getType()));
	}

	@Test
	public void isAssignableFrom_00() throws Exception {
		XExpression code0 = expression(SNIPSET1, false);
		XExpression code1 = expression(SNIPSET1, false);

		JvmTypeReference reference0 = this.typeReferences.getTypeForName("boolean", code0);
		
		StandardTypeReferenceOwner owner = new StandardTypeReferenceOwner(this.services, code1);
		LightweightTypeReference reference1 = owner.newParameterizedTypeReference(this.typeReferences.findDeclaredType("boolean", code1));
		
		assertTrue(reference1.isAssignableFrom(reference0.getType()));
	}

	@Test
	public void isSubtypeOf_01() throws Exception {
		XExpression code0 = expression(SNIPSET1, true);
		XExpression code1 = expression(SNIPSET1, true);

		JvmTypeReference reference0 = this.typeReferences.getTypeForName("boolean", code0);
		
		StandardTypeReferenceOwner owner = new StandardTypeReferenceOwner(this.services, code1);
		LightweightTypeReference reference1 = owner.newParameterizedTypeReference(this.typeReferences.findDeclaredType("boolean", code1));
		
		assertTrue(reference1.isSubtypeOf(reference0.getType()));
	}

	@Test
	public void isAssignableFrom_01() throws Exception {
		XExpression code0 = expression(SNIPSET1, true);
		XExpression code1 = expression(SNIPSET1, true);

		JvmTypeReference reference0 = this.typeReferences.getTypeForName("boolean", code0);
		
		StandardTypeReferenceOwner owner = new StandardTypeReferenceOwner(this.services, code1);
		LightweightTypeReference reference1 = owner.newParameterizedTypeReference(this.typeReferences.findDeclaredType("boolean", code1));
		
		assertTrue(reference1.isAssignableFrom(reference0.getType()));
	}

}
