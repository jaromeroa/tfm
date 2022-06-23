package org.cytoscape.tfm.internal;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task {
    //Ejecutores
    public static List<ArrayList<String>> ejecutores(List<ArrayList<String>> lista, List<ArrayList<String>> nuevaLista){
        int size = nuevaLista.size();

        final List<ArrayList<String>> listab1 = new ArrayList<ArrayList<String>>(1500000);
        List<ArrayList<String>> ln1 = new ArrayList<ArrayList<String>>(nuevaLista.subList(0, (size)/10));  //-> 1/4 de 1/10=1/40=0,025
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        executor1.submit(new Runnable() {
            @Override
            public void run() {
                task(ln1,nuevaLista,listab1);
                executor1.shutdown();
                lista.addAll(listab1);
            }
        });
        final List<ArrayList<String>> listab2 = new ArrayList<ArrayList<String>>(1500000);
        List<ArrayList<String>> ln2 = new ArrayList<ArrayList<String>>(nuevaLista.subList((size)/10, (2*size)/10)); //-> =2/40=0,050
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        executor2.submit(new Runnable() {
            @Override
            public void run() {
                task(ln2,nuevaLista,listab2);
                executor2.shutdown();
                lista.addAll(listab2);
            }
        });
        final List<ArrayList<String>> listab3 = new ArrayList<ArrayList<String>>(1500000);
        List<ArrayList<String>> ln3 = new ArrayList<ArrayList<String>>(nuevaLista.subList((2*size)/10, (3*size)/10)); //=3/40=0,075
        ExecutorService executor3 = Executors.newSingleThreadExecutor();
        executor3.submit(new Runnable() {
            @Override
            public void run() {
                task(ln3,nuevaLista,listab3);
                executor3.shutdown();
                lista.addAll(listab3);
            }
        });
        final List<ArrayList<String>> listab4 = new ArrayList<ArrayList<String>>(1000000);
        List<ArrayList<String>> ln4 = new ArrayList<ArrayList<String>>(nuevaLista.subList((3*size)/10, (4*size)/10));  //=4/40=1/10=0,1
        ExecutorService executor4 = Executors.newSingleThreadExecutor();
        executor4.submit(new Runnable() {
            @Override
            public void run() {
                task(ln4,nuevaLista,listab4);
                executor4.shutdown();
                lista.addAll(listab4);
            }
        });
        final List<ArrayList<String>> listab5 = new ArrayList<ArrayList<String>>(1000000);
        List<ArrayList<String>> ln5 = new ArrayList<ArrayList<String>>(nuevaLista.subList((4*size)/10, (5*size)/10));  //=15/10=1,5
        ExecutorService executor5 = Executors.newSingleThreadExecutor();
        executor5.submit(new Runnable() {
            @Override
            public void run() {
                task(ln5,nuevaLista,listab5);
                executor5.shutdown();
                lista.addAll(listab5);
            }
        });
        final List<ArrayList<String>> listab6 = new ArrayList<ArrayList<String>>(500000);
        List<ArrayList<String>> ln6 = new ArrayList<ArrayList<String>>(nuevaLista.subList((5*size)/10, (6*size)/10));  //=2/10=0,2
        ExecutorService executor6 = Executors.newSingleThreadExecutor();
        executor6.submit(new Runnable() {
            @Override
            public void run() {
                task(ln6,nuevaLista,listab6);
                executor6.shutdown();
                lista.addAll(listab6);
            }
        });
        final List<ArrayList<String>> listab7 = new ArrayList<ArrayList<String>>(500000);
        List<ArrayList<String>> ln7 = new ArrayList<ArrayList<String>>(nuevaLista.subList((6*size)/10, (7*size)/10));   //=3/10=0,3
        ExecutorService executor7 = Executors.newSingleThreadExecutor();
        executor7.submit(new Runnable() {
            @Override
            public void run() {
                task(ln7,nuevaLista,listab7);
                executor7.shutdown();
                lista.addAll(listab7);
            }
        });
        final List<ArrayList<String>> listab8 = new ArrayList<ArrayList<String>>(500000);
        List<ArrayList<String>> ln8 = new ArrayList<ArrayList<String>>(nuevaLista.subList((7*size)/10, (8*size)/10));   //=4/10=0,4
        ExecutorService executor8 = Executors.newSingleThreadExecutor();
        executor8.submit(new Runnable() {
            @Override
            public void run() {
                task(ln8,nuevaLista,listab8);
                executor8.shutdown();
                lista.addAll(listab8);
            }
        });
        final List<ArrayList<String>> listab9 = new ArrayList<ArrayList<String>>(500000);
        List<ArrayList<String>> ln9 = new ArrayList<ArrayList<String>>(nuevaLista.subList((8*size)/10, (9*size)/10));    //=5/10=0,5
        ExecutorService executor9 = Executors.newSingleThreadExecutor();
        executor9.submit(new Runnable() {
            @Override
            public void run() {
                task(ln9,nuevaLista,listab9);
                executor9.shutdown();
                lista.addAll(listab9);
            }
        });
        final List<ArrayList<String>> listab10 = new ArrayList<ArrayList<String>>(500000);
        List<ArrayList<String>> ln10 = new ArrayList<ArrayList<String>>(nuevaLista.subList((9*size)/10, size));       //=10/10=1
        ExecutorService executor10 = Executors.newSingleThreadExecutor();
        executor10.submit(new Runnable() {
            @Override
            public void run() {
                task(ln10,nuevaLista,listab10);
                executor10.shutdown();
                lista.addAll(listab10);
            }
        });
        while (!executor1.isTerminated() || !executor2.isTerminated() || !executor3.isTerminated() || !executor4.isTerminated() || !executor5.isTerminated()
                || !executor6.isTerminated() || !executor7.isTerminated() || !executor8.isTerminated() || !executor9.isTerminated() || !executor10.isTerminated()) {
            // infinite loop
        }
        //Limpiar memoria que no se usa
        listab1.clear();listab2.clear();listab3.clear();listab4.clear();listab5.clear();
        listab6.clear();listab7.clear();listab8.clear();listab9.clear();listab10.clear();
        ln1.clear();ln2.clear();ln3.clear();ln4.clear();ln5.clear();
        ln6.clear();ln7.clear();ln8.clear();ln9.clear();ln10.clear();
        //Devolver el resultado
        return lista;
    }
    //Tareas
    public static List<ArrayList<String>> task(List<ArrayList<String>> ln, List<ArrayList<String>> nuevaLista, List<ArrayList<String>> listab){
        for(ArrayList<String> i: ln){
            for (ArrayList<String> j: nuevaLista){
                if(nuevaLista.indexOf(j)>nuevaLista.indexOf(i)){
                    ArrayList<String> sub = new ArrayList<String>();
                    sub.addAll(i);
                    sub.addAll(j);
                    listab.add(sub);
                }
            }
        }
        return listab;
    }
}
