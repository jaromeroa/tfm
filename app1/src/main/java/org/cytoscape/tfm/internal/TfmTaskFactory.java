package org.cytoscape.tfm.internal;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class TfmTaskFactory extends AbstractTaskFactory {

	//@Override
	/*public TaskIterator createTaskIterator() {
		return new TaskIterator(new HelloWorldTask());
	}*/
	private final CyNetworkFactory cnf;
	private final CyNetworkViewFactory cnvf;
	private final CyNetworkViewManager networkViewManager;
	private final CyNetworkManager networkManager;
	private final CyNetworkNaming cyNetworkNaming;

	public TfmTaskFactory(CyNetworkNaming cyNetworkNaming, CyNetworkFactory cnf, CyNetworkManager networkManager, CyNetworkViewFactory cnvf,
						  final CyNetworkViewManager networkViewManager){

		this.cnf = cnf;
		this.cnvf = cnvf;
		this.networkViewManager = networkViewManager;
		this.networkManager = networkManager;
		this.cyNetworkNaming = cyNetworkNaming;

	}

	public TaskIterator createTaskIterator(){
		return new TaskIterator(new TfmTask(cyNetworkNaming, cnf,networkManager, cnvf, networkViewManager));
	}
}
