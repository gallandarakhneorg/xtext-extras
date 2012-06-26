/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.typesystem.conformance;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.xtext.xbase.typesystem.references.AnyTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

/**
 * Only the any type can be assigned to the any type.
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@NonNullByDefault
public class AnyTypeConformanceStrategy extends TypeConformanceStrategy<AnyTypeReference> {
	
	protected AnyTypeConformanceStrategy(TypeConformanceComputer conformanceComputer) {
		super(conformanceComputer);
	}

	@Override
	public TypeConformanceResult doVisitAnyTypeReference(AnyTypeReference left, AnyTypeReference right, TypeConformanceComputationArgument.Internal<AnyTypeReference> param) {
		return TypeConformanceResult.SUCCESS;
	}

	@Override
	public TypeConformanceResult doVisitTypeReference(AnyTypeReference left, LightweightTypeReference right, TypeConformanceComputationArgument.Internal<AnyTypeReference> param) {
		return TypeConformanceResult.FAILED;
	}
}