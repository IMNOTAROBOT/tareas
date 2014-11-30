/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;

/**
 *
 * @author karen poblete
 */
public class UTM {

    public static String NewTape(String TT, String Cinta, int numTrans, int posCab) {
        String res = "";
        String aux = "";
        int productividad = 0; //Num. de 1s
		//auxiliares
        char C, S, ES;
        int edoActDec = 0;
        int posEdoSig;
        int j, i, k;
        String CintaIni, CintaFin;

        //Lecturas de archivo para moverse en la cinta
        RandomAccessFile NuevaTT;
        //Parametros de edo de la operacion. Si se acaba la cinta o llegas a HALT
        boolean bandTape = false;
        boolean bandHalt = false;

        try {
            //Inicialización de Documento auxiliar
            NuevaTT = new RandomAccessFile(new File("NuevaTT.txt"), "rw");
            NuevaTT.writeBytes(TT);
            //Aqui se hace todo el proceso
            for (i = 0; i < numTrans; i++) {
				//Condiciónn para HALT
                if (edoActDec == 63) {
                    bandHalt = true;
                    break;
                }
				//Condición para desbordamiento de cinta
                if (posCab > Cinta.length() || posCab < 0) {
                    bandTape = true;
                    break;
                }
                try {
                    C = Cinta.charAt(posCab);
                    if (C == '0') { //Cuando ves un cero
                        //Modificacion la cinta, se escribe lo que se tiene en 16(edoActDec)+1
                        j = 16 * edoActDec;
                        NuevaTT.seek(j);
                        S = (char) NuevaTT.readByte();
                        if (S == '1') {
                            productividad++;
                            CintaFin = "" + Cinta.substring(posCab + 1);
                            CintaIni = "" + Cinta.substring(0, posCab);
                            Cinta = "";
                            Cinta = Cinta + CintaIni + S + CintaFin;
                        }
                        //Movemos la posicion de la cabeza a la izquierda o la derecha
                        j = 16 * edoActDec + 1;
                        NuevaTT.seek(j);
                        ES = (char) NuevaTT.readByte();
                        //System.out.println("Char ES "+ES);
                        if (ES == '0') {
                            posCab = posCab + 1;
                        } else {
                            posCab = posCab - 1;
                        }
                        //Encontramos el nuevo estado 16(edoActDec)+2+1 y lo guardamos
                        posEdoSig = 16 * (edoActDec) + 2;

                        for (k = 0; k < 6; k++) {
                            NuevaTT.seek(posEdoSig);
                            C = (char) NuevaTT.readByte();
                            aux = aux + C;
                            posEdoSig++;
                        }

                    } 
                    else { //Se ve un 1
                        //Se modifica la cinta, se escribe lo que se tenga en 16(edoActDec)+9
                        j = 16 * edoActDec + 8;
                        NuevaTT.seek(j);
                        S = (char) NuevaTT.readByte();
                        if (S == '0') {
                            productividad--;
                            CintaFin = "" + Cinta.substring(posCab + 1);
                            CintaIni = "" + Cinta.substring(0, posCab);
                            Cinta = "";
                            Cinta = Cinta + CintaIni + S + CintaFin;
                        }
                        //Movemos la posicion de la cabeza a la izquierda o la derecha
                        j = 16 * edoActDec + 9;
                        NuevaTT.seek(j);
                        ES = (char) NuevaTT.readByte();
                        //System.out.println("Char ES "+ES);
                        if (ES == '0') {
                            posCab = posCab + 1;
                        } else {
                            posCab = posCab - 1;
                        }
                        //Se encuentra el nuevo estado 16(edoActDec)+2+1 y se guarda
                        posEdoSig = 16 * (edoActDec) + 10;

                        for (k = 0; k < 6; k++) {
                            NuevaTT.seek(posEdoSig);
                            C = (char) NuevaTT.readByte();
                            aux = aux + C;
                            posEdoSig++;
                        }
                    }
                    //Se convierte el estado que se tiene guardado a decimal
                    //Se borra lo que hay en edoActBin
                    edoActDec = Integer.parseInt(aux, 2);
                    aux = "";
                    posEdoSig = 0;
                } catch (Exception e) {
                    bandTape = true;
                }
            }

            if (bandHalt || i == numTrans - 1) {
                if(bandHalt){
                    System.out.println("Alcance Halt");
                }
                System.out.println("Productividad: " + productividad);
                System.out.println("Numero de transiciones: " + i);
                res = Cinta;
            } else {
                if (bandTape) {
                    res = "";
                    if (posCab < 0) {
                        System.out.println("UnderFlow");
                    } else {
                        System.out.println("OverFlow");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Algo salio mal:" + e.getMessage());
        }
        return res;
    }

}
