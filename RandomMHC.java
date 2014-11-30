/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Karen P.
 */
public class RandomMHC {
    //Generador de no. aleatorios
    Random semilla;
    //No. de enteros
    int enteros;
    //No. de decimales
    int decimales;
    //Numero de función a evaluar
    int funcion;
    // Longitud del individuo
    int length;
    //No. de iteraciones
    int G;
    //no. de variables
    int variables;
    
    //Auxiliares
    int[] best;
    double bestFit;
    
    //Inicializacion de objeto Random
    public RandomMHC(int ent, int deci, int func, int ge, int var){
        //No. de enteros
        enteros = ent;
        //No. de decimales
        decimales = deci;
        //No. de variables
        variables = var;
        //No. de iteraciones
        G = ge;
        //Funcion a evaluar. 1-4
        funcion = func;
        //Inicializa el gen de num aleatorios.
        semilla = new Random(); 
    }
    
    public double applyRMHC(){
        double fitAux;
        //Calculo de la longitud total del individuo.
        length = (enteros + decimales + 1)*variables;
        
        //Se genera un individuo aleatoriamente
        best = new int[length];
        int[] ind = generateInd();
        
        //Se convierte el individuo a doubles.
        double [] mejorIndiv = getDouble(ind,enteros,decimales,variables);
        
        best = ind;
        bestFit = 9999999;
        //Para minimizar, empieza iteraciones
        for (int i = 0; i < G; i++) {
            //Se calcula el fitness del individuo de acuerdo a la funcion evaluada
            fitAux = getFitness(ind, funcion);
            //Si fitAux es menor que bestFit entonces fitAux es el nuevo bestFit
            // Y best es el individuo que lo produjo
            if (fitAux < bestFit) {
                best = ind;
                bestFit = fitAux;
                //Se guarda el mejor individuo en variables decimales
                mejorIndiv = getDouble(ind, enteros, decimales, variables);
            }
            //Mutation
            //Se produce un numero aleatorio entre 0 y el tamaño del individuo.
            int mut = semilla.nextInt(length);
            //Si el bit en la posicion mut es 0 se vuelve 1 y al reves.
            if (ind[mut] == 0) {
                ind[mut] = 1;
            } else {
                ind[mut] = 0;
            }
        }

        //Imprime los valores de las variables representadas por el individuo.
        //Imprime en ValoresCorrida.txt
        try{
            FileWriter write = new FileWriter("ValoresCorrida.txt", true);
            BufferedWriter out = new BufferedWriter(write); 
            out.write("Funcion" + funcion + "  ");
            for(int i = 0; i < variables; i++){
                out.write("var["+i+"] " + mejorIndiv[i] + "  ");
            }
            out.write("fit: " + bestFit);
            out.newLine();
            out.close();
        } catch (Exception e){
           System.out.println(e.getMessage());
        }
        //Regresa bestFit que sería el valor que salió de la evaluacion del individuo en la función de fitness
        return bestFit;
    }   
    
    //Funcion para generar el primer individuo
    public int[] generateInd(){
        //Inicializa un individuo de longitud length
        int[] res = new int[length];
        double ran; 
        //Por cada bit de res genera un numero aleatorio entre 0 y 1
        //Si ran<0.5 se pone un 0 y si es mayor un 1.
        for(int i = 0; i < length; i++){
            ran = semilla.nextDouble();
            if(ran < 0.5){ 
                res[i] = 0;
            }else{
                res[i] = 1;
            }
        }
    return res;
    }
    
    //Funcion evaluadora de fitness
    //Contiene las 4 funciones que se quieren minimizar
    //Recibe el individuo y la funcion con la que se va a evaluar
    public double getFitness(int[] individuo, int laFunc) {
        //Se inicializan los valores auxiliares
        double fit = 0;
        int max = 9000000;
        int max_r; // max/r
        int r=0;
        //Se obtienen las variables en decimal que representa el individuo
        double[] valores = getDouble(individuo,enteros, decimales, variables);
        //Se busca que funcion se evaluara
        switch (laFunc) {
            case 1: //De Jong s Function n=4
                //Como son 4 variables se divide el fitness maximo entre 4
                max_r = max/4; 
                for(int i=0;i <4;i++){
                    //Se calcula el fitness de acuerdo a la funcion
                    fit = fit + Math.pow(valores[i], 2);
                    //Se evalua si las variables complen con la restriccion de dominio
                    //Si no la cumplen r aumenta en 1
                    if(valores[i]<-5.12 || valores[i]>5.12){
                        r++;
                    }
                }
                //El valor de fitness es fit mas la multiplicacion de r por max_r
                fit = fit +(r*max_r);
                break;
            case 2: //Rosenbrock s valley function n=2
                max_r = max/2;
                double a = valores[1]-(Math.pow(valores[0],2));
                double b = Math.pow(1-valores[0],2);
                fit = (100*(Math.pow(a, 2)))+ b;
                for(int i=0; i<2;i++){
                    if(valores[i]<-2.048 || valores[i]>2.048){
                        r++;
                    }
                }
                fit = fit + (r*max_r);
                break;
            case 3: //Schwefel s function n=3
                max_r = max/3;
                double c;
                for(int i=0;i <3;i++){
                    c = Math.sqrt(Math.abs(valores[i]));
                    fit = fit + ((-1*valores[i])*Math.sin(c));
                    if(valores[i]<-500 || valores[i]>500){
                        r++;
                    }
                }
                fit = fit + (r*max_r);
                break;
            case 4: //Ackley s function n =3
                double d = 20, e=0.2, f = 2*Math.PI;
                max_r = max/3;
                double g, h,k,l =0, m=0;
                for(int i = 0; i<3; i++){
                    l = l+ Math.pow(valores[i],2);
                }
                k = Math.sqrt((1/3)*l);
                g = Math.exp((-1*e)*k);
                for(int i = 0; i<3; i++){
                    m = m+ Math.cos(f*valores[i]);
                }
                h = Math.exp((1/3)*m);
                fit = (d*(g))-h+d+Math.E;
                for(int i=0; i<3;i++){
                    if(valores[i]<-32.768 || valores[i]>32.768){
                        r++;
                    }
                }
                fit = fit + (r*max_r);
                break;
            default:
                fit = 9999999;
                break;
        }
        return fit;
    }
    
    //Funcion para convertir a variables decimales al individuo
    public double[] getDouble(int[] ind, int enter, int decim, int var){
        //Inicializacion de auxiliares
        double[] res = new double[var];
        double signo = 1; //Valor del signo de la variable
        double entero; //Numero de bits de enteros
        double  decimal; //Numero de bits de decimales
        //Calculo de la longitud de una variable
        int longitud = enter+decim+1;
        //Arreglo para guardar el valor de las variables
        ArrayList<int[]> alvalores = new ArrayList<int[]>();
        //Auxiliar para traducir una variable a base decimal
        int[]aux = new int[longitud];
        int l = 0;
        //Cada una de las variables se agregan al arreglo alvalores
        for(int i = 1; i <= var;i++){
            for(int j = 1; j <= longitud; j++){
                aux[j-1]= ind[(l+j) -1];
            }
            l += longitud;
            alvalores.add(i -1, aux);
            aux = new int[longitud];
        }
        //Por cada variable en alvalores se calcula su parte entera, decimal y su signo
        for(int k = 0; k < alvalores.size(); k++){
            int[] elAuxi = alvalores.get(k);
            entero = 0; decimal = 0; signo = 1;
            //Valor del signo, si hay un 1 entonces es negativo
            if(elAuxi[0] == 1){
                signo = -1;
            }
            int powAux = 0;
            //Para calcular la parte entera se hacen potencias de 2
            for(int m = enter ; m > 0; m--){
                entero = entero + (double) Math.pow(elAuxi[m]*2, powAux);
                powAux++;
            }
            //Porque 0^2 es 1, se debe restar el ese caso
            if(elAuxi[enter]==0){
                entero = entero -1;
            }
            //Para sacar la parte decimal son potencias de 2 de la forma 1/(2^n) o 2^(-n)
            for(int n = enter+1; n  < longitud; n++){
                if(elAuxi[n] != 0){
                    decimal = decimal + (double) Math.pow(elAuxi[n]*2, (enter - n));
                }
             }
            //Al final se suma la parte entera con la decimal y se multiplica por el signo
            res[k] = (entero + decimal)* signo;
            
            /*String outi = "";
            for(int i = 0; i<elAuxi.length;i++){
                outi = outi + elAuxi[i];
            }
            System.out.println("El binario: " + outi + "  Decimal: " + res[k]);*/
        }
        return res;
    }
}
