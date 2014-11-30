/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Karen L. Poblete 116452
 */
public class PruebaChi {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        //Semilla para el generador de numeros aleatorios
        long valS = System.currentTimeMillis();
        int raiz = (int) valS; 
        //Numero de valores normales
        int valoresN = 6000;
        //Numero de nciles
        int nciles = 10;
        //Valor de la media para los aleatorios normales
        double mu = 0;
        //Valor de la desviacion estandar para los aleatorios normales
        double sigma = 1;

        //Aqui se hacen las llamadas a las funciones en orden
        //Se generan valoresN aleatorios normales con media mu y desviacion sigma
        double normales[] = generaNormales(raiz, valoresN, mu, sigma);
        //Se ordenan los numeros normales aleatorios
        double normOrden[] = ordena(normales);
        //Se calculan los limites de los nciles
        double ncilesVal[] = getNcilesValue(normOrden, nciles);
        double muestraCuan[] = new double[nciles];

        //Producir valores aleatorios de una distribución en específico
        //NOTA: El usuario debe introducir la funcion normalizada y esta debe de integrar a 1.
        //Valores de las constantes de la función
        //Vareable para controlar el ciclo
        boolean xtrue = false;
        //Numro de valores para la funcion
        int valores = 50;
        //Numero de muestras
        int muestras = 1000;
        //Chi deseada
        double chi = 4;
                
        String resultado = "";

        while (xtrue == false && muestras < 15000) {
            //La función de probabilidad
            double medias[] = getMediasMuestras(valores, muestras, raiz);

            //La prueba CHI-Cuadrada
            //Usando las var anteriores: nciles, ncilesVal[], muestras, medias[]
            double mediasOrden[] = ordena(medias);
            //Calculando mu y sigma del conjunto de muestras.
            double muMues = 0.0;
            double sigmaMues = 0.0;
            for (int k = 0; k < mediasOrden.length; k++) {
                muMues = muMues + mediasOrden[k];
            }
            muMues = muMues / mediasOrden.length;
            for (int k = 0; k < mediasOrden.length; k++) {
                sigmaMues = sigmaMues + Math.pow(mediasOrden[k] - muMues, 2);
            }
            sigmaMues = Math.sqrt(sigmaMues / (mediasOrden.length - 1));

            //Calculando las frecuencias
            double muestraCuantil[] = getValoresDeCuantil(nciles, ncilesVal, mediasOrden, muMues, sigmaMues);
            double ncilesNormalMues[] = getValoresNormalPorCuantil(nciles, muestras);
            //Calculando el valor de X^2
            double chi2 = 0.0;
            for (int i = 0; i < muestraCuantil.length; i++) {
                chi2 = chi2 + (Math.pow(muestraCuantil[i] - ncilesNormalMues[i], 2) / ncilesNormalMues[i]);
                System.out.println(Double.toString(muestraCuantil[i]) + " " + ncilesNormalMues[i]);
            }
            //Rechazo de hipotesis
            if(chi2 <= chi){
                xtrue = true;
                resultado = "Se cumple Chi: " + Double.toString(chi2);
                System.out.println(resultado + " " + Double.toString(chi2));
                muestraCuan = muestraCuantil;
            } else {
                muestras = muestras + 1;
                resultado = "Iteración: " + muestras + "\n";
                System.out.println(resultado + " " + Double.toString(chi2));
            }
        }

        if(muestras >=15000){
            resultado = "No pasa la prueba";
        }
        //DEBUG Imprimir algunos valores
        try {
            File file = new File("normales.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            String text = "Deciles normales \n";
            for (int i = 0; i < ncilesVal.length; i++) {
                text = text + Double.toString(ncilesVal[i]) + ", ";
            }
            String text2 = "Frecuencias experimentales \n";
            for (int i = 0; i < muestraCuan.length; i++) {
                text2 = text2 + Double.toString(muestraCuan[i]) + ", ";
            }
            String text3 = "Valor de chi-cuadrado: " + resultado;

            output.write(text);
            output.write(text2);
            output.write(text3);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Funcion que calcula la frecuencia de las variables en el decil correcto
    public static double[] getValoresDeCuantil(int nciles, double ncilesValNorm[], double medias[], double muMues, double sigmaMues) {
        double valoresRes[] = new double[nciles];
        //sumar las apariciones por cuantil, limites de cuantil
        for (int i = 0; i < medias.length; i++) {
            int j = 0;
            while (j < ncilesValNorm.length - 1) {
                double aux = (medias[i] - muMues) / sigmaMues;
                if (ncilesValNorm[j] < aux && ncilesValNorm[j + 1] > aux) {
                    valoresRes[j] = valoresRes[j] + 1;
                    j = ncilesValNorm.length;
                }
                if (aux < ncilesValNorm[0]) {
                    valoresRes[0] = valoresRes[0] + 1;
                    j = ncilesValNorm.length;
                }
                if (aux > ncilesValNorm[ncilesValNorm.length-1]) {
                    valoresRes[nciles-1] = valoresRes[nciles-1] + 1;
                    j = ncilesValNorm.length;
                }
                j++;
            }
        }
        return valoresRes;
    }

    public static double[] getValoresNormalPorCuantil(int nciles, int muestras) {
        double ncilesRes[] = new double[nciles];
        //Normaliza los valores y busca en la tabla
        for (int i = 0; i < nciles; i++) {
            ncilesRes[i] = muestras / nciles;
        }
        return ncilesRes;
    }

    public static double[] getMediasMuestras(int valores, int muestras, int semilla) {
        Random aleatorio = new Random(semilla);
        //Limites de la funcion
        double A = 0;
        double B = 2;
        //Inicialización de X. 
        double x, y, sum;
        double funcVal[] = new double[valores];
        double medias[] = new double[muestras];
        //La función es: x/2 [0,2]
        //Discretizar la función
        int tam = 2000;
        double[] probDiscreta = discretizarFunc(tam, A, B);
        for (int i = 0; i < muestras; i++) {
            //Creando las variables aleatorios
            for (int j = 0; j < valores; j++) {
                //Devuelve un número entre [0,1)
                x = aleatorio.nextDouble();
                //Hacer transformación para obtener valor de función discretizada
                y = getVariableFun(x, probDiscreta, A, B, tam);
                funcVal[j] = y;
                //System.out.print(funcVal[j] + ", ");
            }
            //sumar y sacar promedio
            sum = 0.0;
            for (int k = 0; k < valores; k++) {
                sum = sum + funcVal[k];
            }
            medias[i] = sum / valores;
            //System.out.println(medias[i]);
        }
        return medias;
    }

    public static double[] discretizarFunc(int tam, double ini, double fin) {
        double[] val = new double[tam];
        double aux = ini;
        //Calcular pasos
        double paso = (fin - ini) / tam;
        //La función de prob acum es: (x^2)/4 [0,2]
        //Dividimos en particiones de 0.001 para saber la probabilidad de esos valores
        for (int i = 0; i < tam; i++) {
            val[i] = Math.pow(aux, 2) / 4;
            aux = aux + paso;
        }
        return val;
    }

    public static double getVariableFun(double x, double[] val, double ini, double fin, int tam) {
        double res = 0.0;
        //Buscamos entre que valores se encuentra x
        for (int i = 0; i < val.length - 1; i++) {
            if (val[i] < x && val[i + 1] > x) {
                res = (i) * ((fin - ini) / tam);
            }
        }
        return res;
    }

    public static double[] getNcilesValue(double[] vals, int nciles) {
        double[] valores = new double[nciles + 1];
        valores[0] = vals[0];
        for (int i = 1; i < nciles; i++) {
            double indexD = (vals.length * (double) i) / nciles;
            int index = new Integer((int) indexD);
            valores[i] = vals[index];
        }
        valores[nciles] = vals[vals.length - 1];
        return valores;
    }

    public static double[] ordena(double[] vals) {
        double aux;
        for (int i = 1; i < vals.length; i++) {
            for (int j = 0; j < vals.length - i; j++) {
                if (vals[j] > vals[j + 1]) {
                    aux = vals[j];
                    vals[j] = vals[j + 1];
                    vals[j + 1] = aux;
                }
            }
        }
        return vals;
    }

    public static double[] generaNormales(int raiz, int num, double mu, double sigma) {
        double[] valores = new double[num];
        Random aleatorio = new Random(raiz);
        double y;
        for (int j = 0; j < num; j++) {
            y = 0.0;
            for (int i = 0; i < 12; i++) {
                y = y + aleatorio.nextDouble();
            }
            valores[j] = sigma * (y - 6) + mu;
        }
        return valores;
    }
}
