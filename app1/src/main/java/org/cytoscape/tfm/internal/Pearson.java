package org.cytoscape.tfm.internal;

public class Pearson {
    public static Double pearson(Double[] uno, Double[] dos){
        //Double res = 0.0;
        int n = uno.length;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sumProductos = 0.0;
        double sumP1 = 0.0;
        double sumP2 = 0.0;
        for(int i=0;i<uno.length;i++){
            //Sumas simples
            sum1 = sum1+uno[i];
            sum2 = sum2+dos[i];
            //Sumas de producto
            sumProductos = sumProductos + (uno[i]*dos[i]);
            //Sumas de potencias
            sumP1 = sumP1 + Math.pow(uno[i],2.0);
            sumP2 = sumP2 + Math.pow(dos[i],2.0);
        }
        //Numerador
        double numerador = sumProductos - (sum1*sum2/n);
        //Denominador
        double denominador = Math.sqrt((sumP1-Math.pow(sum1,2.0)/n)*(sumP2-Math.pow(sum2,2.0)/n));
        if(denominador==0){
            return 0.0;
        }
        return numerador/denominador;
    }
}
