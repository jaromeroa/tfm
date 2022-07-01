package org.cytoscape.tfm.internal;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;

class Graph {
    class Edge implements Comparable<Edge>
    {
        int src, dest;
        Double weight;
        //Comparator para ordenar por tamaño de ejes
        public int compareTo(Edge compareEdge)
        {
            //return this.weight - compareEdge.weight;
            return this.weight.compareTo(compareEdge.weight);
        }
    };
    class SuperEdge implements Comparable<SuperEdge>
    {
        String src, dest;
        Double weight;
        public int compareTo(SuperEdge compareEdge)
        {
            //return this.weight - compareEdge.weight;
            return this.weight.compareTo(compareEdge.weight);
        }
    };

    //Clase para representar subconjuntos de funciones
    class subset
    {
        int parent, rank;
    };
    class superSubset
    {
        String parent;
        int rank;
    };

    int V, E; //Numero de vertices y ejes
    Edge edge[]; //Coleccion de ejes
    SuperEdge superEdge[]; //Coleccion de ejes

    //Crear grafo
    Graph(int v, int e)
    {
        V = v;
        E = e;
        edge = new Edge[E];
        superEdge = new SuperEdge[E];
        for (int i = 0; i < e; i++) {
            edge[i] = new Edge();
            superEdge[i] = new SuperEdge();
        }

    }
    /*void SuperGraph(int v, int e)
    {
        V = v;
        E = e;
        superEdge = new SuperEdge[E];
        for (int i = 0; i < e; ++i)
            superEdge[i] = new SuperEdge();
    }*/


    //Funcion para encontrar conjuntos de elementos i
    int find(subset subsets[], int i)
    {
        if (subsets[i].parent != i)
            subsets[i].parent
                    = find(subsets, subsets[i].parent);

        return subsets[i].parent;
    }

    //Funcion que hace la union de 2 conjuntos
    void Union(subset subsets[], int x, int y)
    {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        //Arbol mas rapido
        if (subsets[xroot].rank
                < subsets[yroot].rank)
            subsets[xroot].parent = yroot;
        else if (subsets[xroot].rank
                > subsets[yroot].rank)
            subsets[yroot].parent = xroot;

            //Si los rankins son iguales lo dejamos como raiz y lo incrementamos
        else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    //Funcion principal para construir MST usando Kruskal
    List<ArrayList<String>> KruskalMST()
    {

        //List<ArrayList<String>> importantes = new ArrayList<ArrayList<String>>(500000);
        //Guardar resultado
        List<ArrayList<String>> salida = new ArrayList<ArrayList<String>>();
        Edge result[] = new Edge[V];
        SuperEdge superResult[] = new SuperEdge[V];

        //Variable indice para el resultado
        int e = 0;

        //Variable indice para ordenar ejes
        int i = 0;
        for (i = 0; i < V; i++){
            result[i] = new Edge();
            superResult[i] = new SuperEdge();
        }


        //Paso 1: ordenar ejes por pesos
        Arrays.sort(edge);
        Arrays.sort(superEdge);
        //Alojar en memoria V subconjuntos
        subset subsets[] = new subset[V];
        for (i = 0; i < V; i++)
            subsets[i] = new subset();

        //Crear V subconjuntos con un solo elemento
        for (int v = 0; v < V; v++)
        {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0; //Indice para pillar siguiente elemento

        //Numero de ejes a coger es igual a V-1
        //JOptionPane.showMessageDialog(null,"V: "+ V, "InfoBox: " + "???", JOptionPane.INFORMATION_MESSAGE);
        while (e < V - 1)
        {

            //Paso 2: Pillar eje mas chico. Incrementar indice para siguiente iteracion
            Edge next_edge = edge[i];
            SuperEdge super_next_edge = superEdge[i];
            i++;

            int x = find(subsets, next_edge.src);
            int y = find(subsets, next_edge.dest);

            //Si incluyendo este eje no hace un bucle,
            //meterlo en el resultado y aumentar indice del resultado al siguiente eje


            if (x != y) {
                result[e] = next_edge;
                //superResult[e] = super_next_edge;
                /*if(e==0){
                    JOptionPane.showMessageDialog(null, result[e].weight, "InfoBox: " + "???", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, superResult[e].weight, "InfoBox: " + "???", JOptionPane.INFORMATION_MESSAGE);
                }*/
                ArrayList<String> grupo = new ArrayList<String>();
                grupo.add(super_next_edge.src);
                grupo.add(super_next_edge.dest);
                //Double pesoAbs =  Math.abs(super_next_edge.weight);
                String pesoCadena = super_next_edge.weight.toString();
                grupo.add(pesoCadena);
                salida.add(grupo);
                Union(subsets, x, y);
            }
            e++;
            //Sino descarte
        }

        /*for (i = 0; i < e; i++) {
            ArrayList<String> grupo = new ArrayList<String>();
            grupo.add(superResult[i].src);
            grupo.add(superResult[i].dest);
            Double pesoAbs =  Math.abs(superResult[i].weight);
            String pesoCadena = pesoAbs.toString();
            grupo.add(pesoCadena);
            salida.add(grupo);
        }*/
        return salida;
    }

    /*void renombre(Map<Integer,String> nombres){
        for(int i = 0;i< nombres.size();i++){
            edge[i] = nombres.get(i);
        }
    }*/


    //Salva nodos
    List<ArrayList<String>> salvaNodos(List<ArrayList<String>> salida){
        List<String> importantes = new ArrayList<String>();
        List<ArrayList<String>> salvados = new ArrayList<ArrayList<String>>();
        //Map<Integer,ArrayList<String>> mapeo = new HashMap<>();
        int it = 0;
        for(ArrayList<String> parejas:salida){
            //ArrayList<String> map = new ArrayList<String>();
            String origen = parejas.get(0);
            //map.add(origen);
            String destino = parejas.get(1);
            //map.add(destino);
            String relacion = parejas.get(2);
            //map.add(relacion);
            //mapeo.put(it,map);
            //it++;
            importantes.add(origen);
            importantes.add(destino);
        }
        HashSet<String> sinRepe = new HashSet<>();
        sinRepe.addAll(importantes);
        String[] toaccess = sinRepe.toArray(new String[sinRepe.size()]);
        Map<String, Integer> nodosXrep = new HashMap<>();
        double total = 0.0;
        for(int i=0;i<sinRepe.size();i++){
            int occurrences = Collections.frequency(importantes, toaccess[i]);
            total = total + occurrences;
            nodosXrep.put(toaccess[i],occurrences);
        }
        double media = 0.0;
        if(sinRepe.size()==0){
            media = total;
        }else{
            media = total/sinRepe.size();
        }
        //JOptionPane.showMessageDialog(null, "Media: "+media, "InfoBox: " + "???", JOptionPane.INFORMATION_MESSAGE);

        /*for(int i=0;i<salida.size();i++){
            salida.get(i);
            int occurrences = Collections.frequency(importantes, toaccess[i]);
            total = total + occurrences;
            nodosXrep.put(toaccess[i],occurrences);
        }*/
        for(ArrayList<String> parejas:salida){
            if(nodosXrep.get(parejas.get(0))>=media || nodosXrep.get(parejas.get(1))>=media){
                ArrayList<String> grupoNodo = new ArrayList<String>();
                String origen = parejas.get(0);
                grupoNodo.add(origen);
                String destino = parejas.get(1);
                grupoNodo.add(destino);
                String relacion = parejas.get(2);
                grupoNodo.add(relacion);
                salvados.add(grupoNodo);
            }
        }

        return salvados;
    }
}