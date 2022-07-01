package org.cytoscape.tfm.internal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Umbral extends JFrame implements ActionListener {
    private JTextField textfield1;
    private JLabel label1;
    private JLabel info;
    private JButton boton1;
    public static Double cad;
    public static Double[] cadEnGNet = new Double[3];
    private String entrada;
    public Umbral() {
        entrada = "otros";
        setLayout(null);
        label1=new JLabel("Umbral:");
        label1.setBounds(10,10,100,30);
        add(label1);
        textfield1=new JTextField();
        textfield1.setBounds(120,10,150,20);
        add(textfield1);
        boton1=new JButton("Aceptar");
        boton1.setBounds(10,80,100,30);
        add(boton1);
        boton1.addActionListener(this);

    }
    public Umbral(String algoritmo) {
        entrada = algoritmo;
        setLayout(null);
        label1=new JLabel("Umbrales:");
        label1.setBounds(10,10,100,30);
        add(label1);
        info=new JLabel("Debe usar el formato: x.x-x.x-x.x");
        info.setBounds(25,250,100,30);
        add(info);
        textfield1=new JTextField();
        textfield1.setBounds(120,10,150,20);
        add(textfield1);
        boton1=new JButton("Aceptar");
        boton1.setBounds(10,80,100,30);
        add(boton1);
        boton1.addActionListener(this);

    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==boton1) {
            try{
                //Arrays.fill(cad, 0.0);
                if(entrada.equals("EnGNet")){
                    String[] trozos = textfield1.getText().split("-");
                    Double dbl1 = Double.parseDouble(trozos[0]);
                    Double dbl2 = Double.parseDouble(trozos[1]);
                    Double dbl3 = Double.parseDouble(trozos[2]);
                    cadEnGNet[0]=dbl1;
                    cadEnGNet[1]=dbl2;
                    cadEnGNet[2]=dbl3;
                    }else{
                    cad=Double.parseDouble(textfield1.getText());
                }
            }catch (IllegalArgumentException er){
                JOptionPane.showMessageDialog(null, "Introduce un número", "InfoBox: " + "Error", JOptionPane.ERROR_MESSAGE);
                er.printStackTrace();
            }
        }
        this.dispose();
    }
    /*public static Double devolverUmbral(){
        return cad;
    }*/
}
