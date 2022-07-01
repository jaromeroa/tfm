package org.cytoscape.tfm.internal;

public class Kendall {
    public static Double kendall(Double[] uno, Double[] dos){
        Double res = 0.0;
        int paresConcordantes = 0;
        int paresDiscordantes = 0;
        int tam = uno.length;
        for(int i=0;i<tam;i++){
            for(int j=0;j<tam;j++){
                if(j>i){
                    if((uno[i]>uno[j] && dos[i]>dos[j]) || (uno[i]<uno[j] && dos[i]<dos[j])){
                        paresConcordantes++;
                    }else{
                        paresDiscordantes++;
                    }
                }
            }
        }
        return 2.0*(paresConcordantes-paresDiscordantes)/(tam*(tam-1));
    }
}
