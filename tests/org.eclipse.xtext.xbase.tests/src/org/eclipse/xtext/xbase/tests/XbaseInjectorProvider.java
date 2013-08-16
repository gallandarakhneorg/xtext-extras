/*
* generated by Xtext
*/
package org.eclipse.xtext.xbase.tests;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.access.CachingClasspathTypeProviderFactory;
import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;
import org.eclipse.xtext.junit4.GlobalRegistries;
import org.eclipse.xtext.junit4.GlobalRegistries.GlobalStateMemento;
import org.eclipse.xtext.junit4.IInjectorProvider;
import org.eclipse.xtext.junit4.IRegistryConfigurator;
import org.eclipse.xtext.preferences.IPreferenceValuesProvider;
import org.eclipse.xtext.preferences.IPreferenceValuesProvider.SingletonPreferenceValuesProvider;
import org.eclipse.xtext.resource.SynchronizedXtextResourceSet;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.xbase.XbaseRuntimeModule;
import org.eclipse.xtext.xbase.XbaseStandaloneSetup;
import org.eclipse.xtext.xbase.junit.SynchronizedXtextResourceSetProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * An injector provider for plain Xbase tests.
 * 
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@SuppressWarnings("deprecation")
public class XbaseInjectorProvider implements IInjectorProvider, IRegistryConfigurator {

	protected GlobalStateMemento stateBeforeInjectorCreation;
	protected GlobalStateMemento stateAfterInjectorCreation;
	protected Injector injector;

	static {
		GlobalRegistries.initializeDefaults();
	}

	public Injector getInjector() {
		if (injector == null) {
			stateBeforeInjectorCreation = GlobalRegistries.makeCopyOfGlobalState();
			this.injector = internalCreateInjector();
			stateAfterInjectorCreation = GlobalRegistries.makeCopyOfGlobalState();
		}
		return injector;
	}

	protected Injector internalCreateInjector() {
		return new XbaseTestStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	public void restoreRegistry() {
		stateBeforeInjectorCreation.restoreGlobalState();
	}

	public void setupRegistry() {
		getInjector();
		stateAfterInjectorCreation.restoreGlobalState();
	}

	public static class XbaseTestStandaloneSetup extends XbaseStandaloneSetup {

		@Override
		public Injector createInjector() {
			return Guice.createInjector(new XbaseTestRuntimeModule());
		}
	}

	public static class XbaseTestRuntimeModule extends XbaseRuntimeModule {
		@Override
		public ClassLoader bindClassLoaderToInstance() {
			return XbaseTestRuntimeModule.class.getClassLoader();
		}
		
		public Class<? extends ClasspathTypeProviderFactory> bindClasspathTypeProviderFactory() {
			return CachingClasspathTypeProviderFactory.class;
		}

		public Class<? extends Provider<SynchronizedXtextResourceSet>> provideSynchronizedResourceSet() {
			return SynchronizedXtextResourceSetProvider.class;
		}
		
		public Class<? extends IPreferenceValuesProvider> bindIPreferenceValuesProvider() {
			return SingletonPreferenceValuesProvider.class;
		}
		
		@Override
		public Class<? extends IScopeProvider> bindIScopeProvider() {
			return DisabledXbaseScopeProvider.class;
		}
	}
	
	public static class DisabledXbaseScopeProvider extends org.eclipse.xtext.xbase.scoping.XbaseScopeProvider {
		@Deprecated
		@Override
		public IScope getScope(EObject context, EReference reference) {
			throw new UnsupportedOperationException();
		}
	}

}