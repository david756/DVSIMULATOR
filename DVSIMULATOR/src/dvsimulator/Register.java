/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

import static dvsimulator.DvSimulator.RUTA_EXCEPTION;
import static dvsimulator.DvSimulator.RUTA_LOG;
import static dvsimulator.DvSimulator.RUTA_ORDENES;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class Register {
    Util util;

    public Register() {
        this.util = new Util();
    }    
    public void log(String valor){
        String Ruta=RUTA_LOG;
        BufferedWriter bw= util.escribirCsv(Ruta); 
        try {
            bw.write(valor);
            bw.newLine();
            bw.close(); 
        } catch (Exception ex) {
            if (ex!=null) {
                 exception("No se pudo acceder al archivo: "+Ruta,ex.getMessage());
            }
        }              
    }
    public void exception(String aux,String e){
        String Ruta=RUTA_EXCEPTION;
        BufferedWriter bw= util.escribirCsv(Ruta);
        String date=new SimpleDateFormat("dd/MM/yyyy H:mm:ss").format(new Date()); 
        try {
            bw.write(date+" -  "+aux + "   Exception:  "+e);
            bw.newLine();
            bw.close();
        } catch (Exception ex) {
            System.out.print("Error al dejar registro de log: "+ex.getMessage());
        }
    }

    void orden(String valor) {
        String Ruta=RUTA_ORDENES;
        BufferedWriter bw= util.escribirCsv(Ruta); 
        try {
            bw.write(valor);
            bw.newLine();
            bw.close(); 
        } catch (Exception ex) {
            if (ex!=null) {
                 exception("No se pudo acceder al archivo: "+Ruta,ex.getMessage());
            }
        }  
    }
}
