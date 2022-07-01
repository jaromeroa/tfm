package org.cytoscape.tfm.internal;

import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import java.util.Properties;

public class CyActivator extends AbstractCyActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {

		/*HelloWorldTaskFactory helloWorldTaskFactory = new HelloWorldTaskFactory();
        Properties createNetworkViewTaskFactoryProps = new Properties();
        createNetworkViewTaskFactoryProps.setProperty(ServiceProperties.PREFERRED_MENU,"Apps.Test");
        createNetworkViewTaskFactoryProps.setProperty(ServiceProperties.TITLE,"TFM");
		registerService(bundleContext,helloWorldTaskFactory, TaskFactory.class, createNetworkViewTaskFactoryProps);*/
		CyNetworkNaming cyNetworkNamingServiceRef = getService(bundleContext,CyNetworkNaming.class);

		CyNetworkFactory cyNetworkFactoryServiceRef = getService(bundleContext,CyNetworkFactory.class);
		CyNetworkManager cyNetworkManagerServiceRef = getService(bundleContext,CyNetworkManager.class);

		CyNetworkViewFactory cyNetworkViewFactoryServiceRef = getService(bundleContext,CyNetworkViewFactory.class);
		CyNetworkViewManager cyNetworkViewManagerServiceRef = getService(bundleContext,CyNetworkViewManager.class);

		TfmTaskFactory createNetworkViewTaskFactory = new TfmTaskFactory(cyNetworkNamingServiceRef, cyNetworkFactoryServiceRef,cyNetworkManagerServiceRef, cyNetworkViewFactoryServiceRef,cyNetworkViewManagerServiceRef);

		Properties createNetworkViewTaskFactoryProps = new Properties();
		createNetworkViewTaskFactoryProps.setProperty("preferredMenu","Apps.MyApps");
		createNetworkViewTaskFactoryProps.setProperty("title","TFM");
		registerService(bundleContext,createNetworkViewTaskFactory,TaskFactory.class, createNetworkViewTaskFactoryProps);
	}

	/*private static Properties ezProps(String... vals) {
		final Properties props = new Properties();
		for (int i = 0; i < vals.length; i += 2)
			props.put(vals[i], vals[i + 1]);
		return props;
	}*/
}

