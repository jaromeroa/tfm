package org.cytoscape.tfm.internal;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class Spearman {
    public static Double spearman(Double[] uno, Double[] dos){
        //Tamaño
        int n = uno.length;
        //Orden
        List<Double> rankX = getRanks(uno,n);
        List<Double> rankY = getRanks(dos,n);
        //Formula
        Double numerador = 0.0;
        for (int i = 0; i < n; i++) {
            numerador = numerador + Math.pow((rankX.get(i) - rankY.get(i)), 2);
        }
        numerador = numerador * 6;
        Double denominador = 1.0*n * ((n * n) - 1);
        return 1-(numerador/denominador);
    }

    //En caso de mismo valor: sumar posiciones y dividir entre las veces que aparezcan
    public static List<Double> getRanks(Double[] array, int n) {
        HashMap<Integer, Double> mapa = new HashMap<Integer, Double>(n);
        for (int i = 0; i < n; i++) {
            mapa.put(i, array[i]);
        }

        Map<Integer, Double> ordenado = sortByValue(mapa);

        //Crear y devolver ranks[]
        List<Double> ranks = new LinkedList<Double>();
        Collection<Double> coleccionValores = ordenado.values();
        Set<Double> valores = new HashSet<Double>(ordenado.values());
        for(Integer clave: ordenado.keySet()){
            int sumPos = 0;
            int repeticiones = 0;
            double media = 0.0;
            Double valor = ordenado.get(clave);
            //for(Double v: valores){
            repeticiones = Collections.frequency(coleccionValores,valor);
            sumPos = getKeysByValue(ordenado,valor);
            //sumPos=sumPos+clave;

            //}
            if(repeticiones>1){
                media = 1.0*sumPos/repeticiones;
            }
            if(repeticiones==1){
                media = 1.0*sumPos/repeticiones;
            }
            ranks.add(media);
            //ranks[sumPos]=media;
            }
        return ranks;
    }

    public static HashMap<Integer,Double> sortByValue(HashMap<Integer, Double> hm){
        List<Map.Entry<Integer, Double> > list =
                new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >(){
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2){
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        int i = 0;
        for (Map.Entry<Integer, Double> aa : list){
            temp.put(i, aa.getValue());
            i++;
        }
        return temp;
    }

    public static int getKeysByValue(Map<Integer, Double> map, Double value){
        int sumatorio = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet()){
            if (value.equals(entry.getValue())){
                sumatorio = sumatorio + entry.getKey();
            }
        }
        return sumatorio;
    }
}
