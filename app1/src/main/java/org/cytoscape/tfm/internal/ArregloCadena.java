package org.cytoscape.tfm.internal;

import javax.swing.*;
import java.util.ArrayList;

public class ArregloCadena {
    public static ArrayList<String> arregloCadena(String line){
        StringBuilder nueva = new StringBuilder();
        String nombre = "";
        String num = "";
        String[] arr = line.split("");
        ArrayList<String> res = new ArrayList<String>();
        boolean first = true;
        for(int i=0;i<arr.length;i++){
            String a = arr[i];
            if(first){
                if(arr[i+8].equals("A") || arr[i+8].equals("B")){
                    nombre = arr[i]+arr[i+1]+arr[i+2]+arr[i+3]
                            +arr[i+4]+arr[i+5]+arr[i+6]+arr[i+7]+arr[i+8];
                    //i=i+8;
                }else{
                    nombre = arr[i]+arr[i+1]+arr[i+2]+arr[i+3]
                            +arr[i+4]+arr[i+5]+arr[i+6];
                    //i=i+6;
                }
                first=false;
                res.add(nombre);
                //nueva.append(nombre);
            }

            if(a.equals(".")){
                if(arr[i-2]!=null && arr[i-2].equals("-")){
                    num = arr[i-2]+arr[i-1]+arr[i]
                            +arr[i+1]+arr[i+2];
                }else{
                    num = arr[i-1]+arr[i]
                            +arr[i+1]+arr[i+2];
                }
                res.add(num);
                //nueva.append(",").append(num);
            }
        }
        return res;
    }
}
