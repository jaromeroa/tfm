package org.cytoscape.tfm.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import org.cytoscape.event.CyEventHelper;
import org.cytoscape.event.CyPayloadEvent;
import org.cytoscape.model.*;
import org.cytoscape.*;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TfmTask extends AbstractTask {

	private final CyNetworkFactory cnf;
	private final CyNetworkViewFactory cnvf;
	private final CyNetworkViewManager networkViewManager;
	private final CyNetworkManager networkManager;
	private final CyNetworkNaming cyNetworkNaming;

	public TfmTask(CyNetworkNaming cyNetworkNaming, CyNetworkFactory cnf, CyNetworkManager networkManager,
				   CyNetworkViewFactory cnvf, final CyNetworkViewManager networkViewManager) {
		this.cnf = cnf;
		this.cnvf = cnvf;
		this.networkViewManager = networkViewManager;
		this.networkManager = networkManager;
		this.cyNetworkNaming = cyNetworkNaming;
	}

	@Override
	public void run(TaskMonitor taskMonitor) {

		final int[] chivato = {0};
		//GUI
		final List<ArrayList<String>> lista = new ArrayList<ArrayList<String>>(16000000);
		//final List<ArrayList<String>> res = new ArrayList<ArrayList<String>>(16000000);  //Por si hace falta crear la matriz
		final List<ArrayList<String>> pasanFiltro = new ArrayList<ArrayList<String>>(5000000);
		final List<ArrayList<String>> salidas = new ArrayList<ArrayList<String>>(500000);

		//Marco
		JFrame frame = new JFrame("TFM");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		//frame.setBackground(Color.lightGray);
		try {
			Image img = new ImageIcon("org/cytoscape/tfm/internal/icon.png").getImage();
			//ImageIcon
			frame.setIconImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setVisible(true);
		frame.setSize(400, 200);
		//frame.setResizable(false);

		//MenuBar y componentes
		JMenuBar mb = new JMenuBar();
		JMenu menuArchivo = new JMenu("Archivo");
		//JMenu menuAyuda = new JMenu("Ayuda");
		JMenuItem menuAyuda = new JMenuItem("Ayuda");
		mb.add(menuArchivo);
		mb.add(menuAyuda);
		//
		JMenu submenuAlgoritmos = new JMenu("Seleccionar algoritmo");
		submenuAlgoritmos.setMnemonic(KeyEvent.VK_A);
		//Selección de algoritmo
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem rbMenuItem;
		rbMenuItem = new JRadioButtonMenuItem("Pearson");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_P);
		group.add(rbMenuItem);
		submenuAlgoritmos.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Spearman");
		rbMenuItem.setMnemonic(KeyEvent.VK_S);
		group.add(rbMenuItem);
		submenuAlgoritmos.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Kendal");
		rbMenuItem.setMnemonic(KeyEvent.VK_K);
		group.add(rbMenuItem);
		submenuAlgoritmos.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("EnGNet");
		rbMenuItem.setMnemonic(KeyEvent.VK_K);
		group.add(rbMenuItem);
		submenuAlgoritmos.add(rbMenuItem);
		menuArchivo.add(submenuAlgoritmos);
		// Área de texto en el centro
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setBackground(Color.cyan);

		menuAyuda.setMnemonic(KeyEvent.VK_U);
		menuAyuda.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ta.setText(null);
				frame.setSize(1400, 200);
				ta.append("Utilice el botón 'Archivo' para desplegar las diferentes opciones que ofrece la herramienta.");
				ta.append("\r\n");
				ta.append("Utilice el botón 'Seleccionar algoritmo' para elegir entre los algoritmos de correlación: Pearson, Spearman, Kendal o EnGNet");
				ta.append("\r\n");
				ta.append("Utilice el botón 'Seleccionar umbral' para introducir el umbral del algoritmo de correlación.");
				ta.append("\r\n");
				ta.append("Nota: en caso de haber seleccionado 'EnGNet' debe introducir 3 umbrales, el primero para Pearson, el segundo para Spearman y el tercero para Kendal separados por espacios.");
				ta.append("\r\n");
				ta.append("Finalmente, al seleccionar 'Abrir datos de entrada' y buscar en su maquina local los datos de entrada, la herramienta comenzará");
				ta.append("\r\n");
			}
		});
		//Umbral
		final Double[] umbral = {0.0};
		final Double[] umbralEnGNet = {0.0,0.0,0.0};
		JMenuItem seleccionarUmbral = new JMenuItem("Seleccionar umbral");
		seleccionarUmbral.setMnemonic(KeyEvent.VK_U);
		seleccionarUmbral.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getSelectedButtonText(group).equals("EnGNet")){
					Umbral umbrales = new Umbral("EnGNet");
					umbrales.setBounds(0,0,350,170);
					umbrales.setVisible(true);
					umbrales.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}else{
					Umbral umb = new Umbral();
					umb.setBounds(0,0,350,170);
					umb.setVisible(true);
					umb.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}
				}
		});

		menuArchivo.add(seleccionarUmbral);

		JMenuItem menuDatos = new JMenuItem("Abrir datos de entrada");
		menuDatos.setMnemonic(KeyEvent.VK_D);
		menuDatos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ta.setText(null);
				ta.setEditable(false);
				frame.setSize(400, 200);

				JFileChooser chooser = new JFileChooser();
				int status = chooser.showOpenDialog(null);
				if (status == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (file == null) {
						return;
					}

					try {
						long timeStart;
						timeStart = System.currentTimeMillis();
						int salto=0;
						int genes=0;
						lista.clear();
						pasanFiltro.clear();
						salidas.clear();
						List<ArrayList<String>> nuevaLista = new ArrayList<ArrayList<String>>();
						BufferedReader br = new BufferedReader(new FileReader(file));
						try {
							String line;
							while ((line = br.readLine()) != null) {
								if(salto==0){
									salto=1;
								}else{
									nuevaLista.add(ArregloCadena.arregloCadena(line));
									genes++;
								}
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						} finally {
							br.close();
						}

						String selected = getSelectedButtonText(group);
						if(selected.equals("EnGNet")){
							umbralEnGNet[0] = Umbral.cadEnGNet[0];
							umbralEnGNet[1] = Umbral.cadEnGNet[1];
							umbralEnGNet[2] = Umbral.cadEnGNet[2];
							if(umbralEnGNet[0]==null){
								umbralEnGNet[0]=0.0;
							}
							if(umbralEnGNet[1]==null){
								umbralEnGNet[1]=0.0;
							}
							if(umbralEnGNet[2]==null){
								umbralEnGNet[2]=0.0;
							}
						}else{
							umbral[0] = Umbral.cad;
							if(umbral[0]==null){
								umbral[0]=0.0;
							}
						}



						ta.append("Algoritmo seleccionado: "+selected);
						ta.append("\r\n");
						if(selected.equals("EnGNet")){
							//JOptionPane.showMessageDialog(null, umbralEnGNet[0]+" - "
							//		+umbralEnGNet[1]+" - "+umbralEnGNet[2], "InfoBox: " + "Umbrales:", JOptionPane.INFORMATION_MESSAGE);

							ta.append("Umbrales seleccionados: "+umbralEnGNet[0]+" - "
									+umbralEnGNet[1]+" - "+umbralEnGNet[2]);
						}else{
							//JOptionPane.showMessageDialog(null, umbral[0], "InfoBox: " + "Umbrales:", JOptionPane.INFORMATION_MESSAGE);
							ta.append("Umbral seleccionado: "+umbral[0]);
						}

						//JOptionPane.showMessageDialog(null, genes, "InfoBox: " + "Numero de genes", JOptionPane.INFORMATION_MESSAGE);

						ta.append("\r\n");
						ta.append("Numero de genes: "+genes);

						//Paralelización
						Task.ejecutores(lista,nuevaLista);
						//Limpio memoria
						nuevaLista.clear();
						//JOptionPane.showMessageDialog(null, lista.size(), "InfoBox: " + "Combinaciones", JOptionPane.INFORMATION_MESSAGE);
						ta.append("\r\n");
						ta.append("Combinaciones: "+lista.size());

						//Preparador
						for(ArrayList<String> elem:lista){
							Double[] uno = new Double[24];
							Double[] dos = new Double[24];
							String nameG1 = elem.get(0);
							String nameG2 = elem.get(25);
							for(int i=0;i<24;i++){
								uno[i] = Double.parseDouble(elem.get(i+1));
								dos[i] = Double.parseDouble(elem.get(i+26));
							}
							Double algoritmo = 0.0;
							Double algoritmoPearson = 0.0;
							Double algoritmoSpearman = 0.0;
							Double algoritmoKendall = 0.0;
							switch (selected) {
								case "Pearson":
									algoritmo = Pearson.pearson(uno, dos);
									break;
								case "Spearman":
									algoritmo = Spearman.spearman(uno, dos);
									break;
								case "Kendall":
									algoritmo = Kendall.kendall(uno,dos);
									break;
								case "EnGNet":
									algoritmoPearson = Pearson.pearson(uno, dos);
									algoritmoSpearman = Spearman.spearman(uno, dos);
									algoritmoKendall = Kendall.kendall(uno,dos);
									break;
								default:
									JOptionPane.showMessageDialog(null, "Ningún algoritmo seleccionado", "InfoBox: " + "Error", JOptionPane.ERROR_MESSAGE);
									break;
							}
							if(selected.equals("EnGNet")){
								ArrayList<String> listaP = new ArrayList<String>();
								ArrayList<String> listaS = new ArrayList<String>();
								ArrayList<String> listaK = new ArrayList<String>();
								ArrayList<String> listaENGNet = new ArrayList<String>();
								listaP.add(nameG1);
								listaP.add(nameG2);
								listaS.add(nameG1);
								listaS.add(nameG2);
								listaK.add(nameG1);
								listaK.add(nameG2);
								listaENGNet.add(nameG1);
								listaENGNet.add(nameG2);
								listaP.add(algoritmoPearson.toString());
								listaS.add(algoritmoSpearman.toString());
								listaK.add(algoritmoKendall.toString());
								Double mediaEjes = 0.0;
								Integer votos = 0;
								if(Double.compare(Math.abs(algoritmoPearson),Math.abs(umbralEnGNet[0]))>=0){
									mediaEjes+=algoritmoPearson;
									votos++;
								}
								if(Double.compare(Math.abs(algoritmoSpearman),Math.abs(umbralEnGNet[1]))>=0){
									mediaEjes+=algoritmoSpearman;
									votos++;
								}
								if(Double.compare(Math.abs(algoritmoKendall),Math.abs(umbralEnGNet[2]))>=0){
									mediaEjes+=algoritmoKendall;
									votos++;
								}
								if(votos>=2){
									try{
										Double res=mediaEjes/votos;
										listaENGNet.add(res.toString());
									}catch (NullPointerException en){
										en.printStackTrace();
									}
									pasanFiltro.add(listaENGNet);
								}
							}else{
								ArrayList<String> lista2 = new ArrayList<String>();
								lista2.add(nameG1);
								lista2.add(nameG2);
								lista2.add(algoritmo.toString());
								//res.add(lista2);
								//Mirar si pasa el umbral
								if(Double.compare(Math.abs(algoritmo),Math.abs(umbral[0]))>=0){
									pasanFiltro.add(lista2);
								}
							}

						}
						//liberamos la memoria de las listas que ya no vamos a usar
						//LIMPIAR LISTAS
						lista.clear();
						ta.append("\r\n");
						ta.append("Combinaciones que pasan el umbral: "+pasanFiltro.size());
						//JOptionPane.showMessageDialog(null, pasanFiltro.size(), "InfoBox: " + "Combinaciones que pasan el umbral", JOptionPane.INFORMATION_MESSAGE);


						//Guardo la lista que pasa el filtro
						try {
							FileWriter myObj = new FileWriter("C:\\Pruebas\\Salidas\\pasanFiltro.txt");
							myObj.flush();
							for(ArrayList<String> pares:pasanFiltro){
								myObj.write(pares.toString().replace("[","").replace("]",""));
								myObj.append("\r\n");
							}
							myObj.close();
							ta.append("\r\n");
							ta.append("Se ha copiado el fichero");
							//JOptionPane.showMessageDialog(null, "***", "InfoBox: " + "Fichero guardado", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException ouch) {
							System.out.println("An error occurred.");
							ouch.printStackTrace();
						}

						if(pasanFiltro.size()>0){
							grafo(pasanFiltro,salidas,timeStart);
							//frame.repaint();
						}

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		menuArchivo.add(menuDatos);
		//JMenuItem m22 = new JMenuItem("Seleccionar umbral");


		// Agregar componentes al marco.
		frame.getContentPane().add(BorderLayout.NORTH, mb);
		frame.getContentPane().add(BorderLayout.CENTER, ta);
		frame.setVisible(true);

	}

	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}

		return null;
	}

	public void grafo(List<ArrayList<String>> pasanFiltro, List<ArrayList<String>> salidas, long timeStart){

		List<ArrayList<String>> salvados = new ArrayList<ArrayList<String>>();
		//List<ArrayList<String>> salidasGrafo = new ArrayList<ArrayList<String>>();
		//NUEVO-GENERAR RED
		// Create an empty network
		CyNetwork myNet = this.cnf.createNetwork();

		List<String> nodos = new LinkedList<String>();
		List<ArrayList<String>> paresEjes = new ArrayList<ArrayList<String>>();
		List<CyNode> nds = new LinkedList<CyNode>();
		//List<CyEdge> edg = new LinkedList<CyEdge>();

		//----NUEVO
		int V = 0; //Numero de vertices
		int E = 0; //Numero de ejes


		for(ArrayList<String> parejas:pasanFiltro){
			String origen = parejas.get(0);
			String destino = parejas.get(1);
			String peso = parejas.get(2);
			if(!nodos.contains(origen)){
				nodos.add(origen);
				V+=1;
			}
			if(!nodos.contains(destino)){
				nodos.add(destino);
				V+=1;
			}
			E+=1;
		}

		Graph graph = new Graph(V, E);


		nodos.clear();
		Map<String,Integer> nombres = new HashMap<>();
		int o = 0;
		int recorre = 0;
		for(ArrayList<String> parejas:pasanFiltro){
			String origen = parejas.get(0);
			String destino = parejas.get(1);
			Double peso = Double.parseDouble(parejas.get(2));
			if(!nodos.contains(origen)){
				nodos.add(origen);
				nombres.put(origen,recorre);
				graph.edge[o].src = recorre;
				graph.superEdge[o].src = origen;
				recorre++;
			}else{
				graph.edge[o].src = nombres.get(origen);
				graph.superEdge[o].src = origen;
			}
			if(!nodos.contains(destino)){
				nodos.add(destino);
				nombres.put(destino,recorre);
				graph.edge[o].dest = recorre;
				graph.superEdge[o].dest = destino;
				recorre++;
			}else{
				graph.edge[o].dest = nombres.get(destino);
				graph.superEdge[o].dest = destino;
			}
			/*if(o==0){
				JOptionPane.showMessageDialog(null, "Peso: "+peso, "InfoBox: " + "Peso", JOptionPane.INFORMATION_MESSAGE);
			}*/

			graph.edge[o].weight = peso;
			graph.superEdge[o].weight = peso;
			o++;
		}

		salidas.clear();
		salvados.clear();
		salvados = graph.salvaNodos(pasanFiltro);
		//JOptionPane.showMessageDialog(null, "Salva nodos OK", "InfoBox: " + "Grafo", JOptionPane.INFORMATION_MESSAGE);
		salidas = graph.KruskalMST();
		//JOptionPane.showMessageDialog(null, "Kruskal OK", "InfoBox: " + "Grafo", JOptionPane.INFORMATION_MESSAGE);
		salidas.addAll(salvados);
		//JOptionPane.showMessageDialog(null, "ADD OK", "InfoBox: " + "Grafo", JOptionPane.INFORMATION_MESSAGE);
		nodos.clear();
		for(ArrayList<String> parejas:salidas){
			//nds=myNet.getNodeList();
			String origen = parejas.get(0);
			String destino = parejas.get(1);
			String eje = parejas.get(2);

			CyNode node1;
			CyNode node2;
			CyEdge edge;
			if(!nodos.contains(origen)){
				nodos.add(origen);
				node1 = myNet.addNode();
				nds.add(node1);
			}else{
				node1 = nds.get(nodos.indexOf(origen));
			}
			if(!nodos.contains(destino)){
				nodos.add(destino);
				node2 = myNet.addNode();
				nds.add(node2);
			}else{
				node2 = nds.get(nodos.indexOf(destino));
			}
			ArrayList<String> pareja = new ArrayList<String>();
			pareja.add(origen);
			pareja.add(destino);
			pareja.add(eje);
			if(!paresEjes.contains(pareja)){
				paresEjes.add(pareja);
				edge = myNet.addEdge(node1,node2,true);
				myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", origen);
				myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", destino);
				myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", eje);
				//myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name",origen+" (interacts with) "+destino);
				//myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("interaction","interacts with");
			}

			/*if(!ejes.contains(eje)){
				ejes.add(eje);
				node2 = myNet.addNode();
				nds.add(node2);
			}else{
				node2 = nds.get(nodos.indexOf(destino));
			}*/

			// add a node to the network
			//CyNode node1 = myNet.addNode();
			//CyNode node2 = myNet.addNode();
			//edge = myNet.addEdge(node1,node2,true);
			// set name for the new node
		}
		long timeEnd;
		timeEnd = System.currentTimeMillis();
		long totalTime = timeEnd-timeStart;
		String timeRes = String.format("%02d min, %02d sec",
				TimeUnit.MILLISECONDS.toMinutes(totalTime),
				TimeUnit.MILLISECONDS.toSeconds(totalTime) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
		);
		JOptionPane.showMessageDialog(null, timeRes, "Tiempo: de ejecución", JOptionPane.INFORMATION_MESSAGE);
		//---Termina NUEVO
		/*for(ArrayList<String> parejas:pasanFiltro){
			//nds=myNet.getNodeList();
			String origen = parejas.get(0);
			String destino = parejas.get(1);

			CyNode node1;
			CyNode node2;
			if(!nodos.contains(origen)){
				nodos.add(origen);
				V+=1;
				node1 = myNet.addNode();
				nds.add(node1);
			}else{
				node1 = nds.get(nodos.indexOf(origen));
			}
			if(!nodos.contains(destino)){
				nodos.add(destino);
				V+=1;
				node2 = myNet.addNode();
				nds.add(node2);
			}else{
				node2 = nds.get(nodos.indexOf(destino));
			}

			// add a node to the network
			//CyNode node1 = myNet.addNode();
			//CyNode node2 = myNet.addNode();
			CyEdge edge = myNet.addEdge(node1,node2,true);
			E+=1;
			// set name for the new node
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", origen);
			myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", destino);
			myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name",origen+" (interacts with) "+destino);
			myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("interaction","interacts with");
		}*/

		myNet.getDefaultNetworkTable().getRow(myNet.getSUID())
				.set("name", cyNetworkNaming.getSuggestedNetworkTitle("My Network"));

		if (myNet == null)
			return;
		this.networkManager.addNetwork(myNet);
		final Collection<CyNetworkView> views = networkViewManager.getNetworkViews(myNet);


		CyNetworkView myView = null;
		if(views.size() != 0)
			myView = views.iterator().next();

		if (myView == null) {
			// create a new view for my network
			myView = cnvf.createNetworkView(myNet);
			networkViewManager.addNetworkView(myView);
		} else {
			System.out.println("networkView already existed.");
		}
		// Set the variable destroyView to true, the following snippet of code
		// will destroy a view
		boolean destroyView = false;
		if (destroyView) {
			networkViewManager.destroyNetworkView(myView);
		}
		//*****
	}

}
