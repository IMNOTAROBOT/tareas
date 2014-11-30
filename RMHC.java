/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Karen P.
 */
public class RMHC {

    public static void main(String[] args)  {
       
        RandomMHC rmh; // RandomMH(boolean maximiza, int enteros, int decimales, 
        //  int funciones, int generaciones, int variables)
        //función1
        double res;
        int repeticiones = 10;
        int G = 1000000;
        double[] valores = new double[repeticiones];
        
        //Funcion 1
        for (int i = 0; i < repeticiones; i++) {
            rmh = new RandomMHC(3, 7, 1, 4*G, 4);
            res = rmh.applyRMHC();
            valores[i] = res;
            System.out.println("Ronda " + (i+1)+ " Funcion 1");
        }
        imprimeValores(valores,1);
        
        //Funcion 2
        for (int i = 0; i < repeticiones; i++) {
            rmh = new RandomMHC(2, 7, 2, G, 2);
            res = rmh.applyRMHC();
            valores[i] = res;
            System.out.println("Ronda " + (i+1) + " Funcion 2");
        }
        imprimeValores(valores,2);
        
        //Funcion 3
        for (int i = 0; i < repeticiones; i++) {
            rmh = new RandomMHC(9, 7, 3, G*2, 3);
            res = rmh.applyRMHC();
            valores[i] = res;
            System.out.println("Ronda " + (i+1) + " Funcion 3");
        }
        imprimeValores(valores, 3);
        
        //Funcion 4
        for (int i = 0; i < repeticiones; i++) {
            rmh = new RandomMHC(6, 7, 4, G, 3);
            res = rmh.applyRMHC();
            valores[i] = res;
            System.out.println("Ronda " + (i+1) + " Funcion 4");
        }
        imprimeValores(valores,4);
            
    }
    public static void imprimeValores(double[] vals, int funcion){
        try {
            FileWriter write = new FileWriter("Resultados.txt", true);
            BufferedWriter out = new BufferedWriter(write);
            out.newLine();
            for(int i = 0; i < vals.length; i++){
                out.write("Funcion: "+ funcion+"  "+"var["+i+"]: " + vals[i]);
                out.newLine();
            }
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
