/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.typesystem.conformance;

import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 * TODO JavaDoc
 */
public class TypeConformanceComputationArgument {

	protected final boolean rawType;
	protected final boolean asTypeArgument;
	protected final boolean allowPrimitiveConversion;
	
	public TypeConformanceComputationArgument(boolean rawType, boolean asTypeArgument,
			boolean allowPrimitiveConversion) {
		this.rawType = rawType;
		this.asTypeArgument = asTypeArgument;
		this.allowPrimitiveConversion = allowPrimitiveConversion;
	}
	
	public boolean isAllowPrimitiveConversion() {
		return allowPrimitiveConversion;
	}
	
	public boolean isAsTypeArgument() {
		return asTypeArgument;
	}
	
	public boolean isRawType() {
		return rawType;
	}

	@Override
	public String toString() {
		return "TypeConformanceComputationArgument [rawType=" + rawType + ", asTypeArgument=" + asTypeArgument
				+ ", allowPrimitiveConversion=" + allowPrimitiveConversion + "]";
	}

	protected static class Internal<T extends LightweightTypeReference> extends TypeConformanceComputationArgument {
		protected final T reference;
		
		protected static <T extends LightweightTypeReference> Internal<T> create(T reference, boolean rawType, boolean asTypeArgument, boolean allowPrimitiveConversion) {
			return new Internal<T>(reference, rawType, asTypeArgument, allowPrimitiveConversion);
		}
		
		protected Internal(T reference, boolean rawType, boolean asTypeArgument, boolean allowPrimitiveConversion) {
			super(rawType, asTypeArgument, allowPrimitiveConversion);
			this.reference = reference;
		}

		@Override
		public String toString() {
			return "TypeConformanceComputationArgument.Internal [reference=" + reference + ", rawType=" + rawType + ", asTypeArgument=" + asTypeArgument
					+ ", allowPrimitiveConversion=" + allowPrimitiveConversion + "]";
		}
	}
	
}