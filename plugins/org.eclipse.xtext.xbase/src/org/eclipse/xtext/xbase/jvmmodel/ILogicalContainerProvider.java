/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.jvmmodel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.xbase.XExpression;

import com.google.inject.ImplementedBy;

/**
 * @author Sven Efftinge - Initial contribution and API
 */
@ImplementedBy(JvmModelAssociator.class)
public interface ILogicalContainerProvider {

	/**
	 * @return the JvmIdentifiableElement that contains the given expression and therefore defines its scope. <code>null</code> if this expression doesn't live in such a context.
	 */
	public JvmIdentifiableElement getLogicalContainer(EObject expr);
	
	/**
	 * @return the XExpression associated with the given {@link JvmIdentifiableElement}
	 */
	public XExpression getAssociatedExpression(JvmIdentifiableElement element);
	
}