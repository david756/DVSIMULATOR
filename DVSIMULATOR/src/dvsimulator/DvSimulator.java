/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class DvSimulator {

    public static String RUTA_EXCEPTION;
    public static String RUTA_LOG;
    public static String RUTA_ARCHIVO;
    public static String RUTA_ORDENES;
    public static Double VOLUMEN;
    public static Double SPREAD;
    public static ArrayList<Orden> ORDENES;
    public static ArrayList<Tick> TICKS;
    
    
    public static void main(String[] args) {        
        // Llamado a While Controller
        WhileController wc = new  WhileController();
        Register rg = new Register();
        VOLUMEN=0.01;
        SPREAD=20.0;
        ORDENES= new ArrayList<Orden>();
        TICKS= new ArrayList<Tick>();
        try {
            wc.start();
        } catch (Exception e) {            
            rg.exception("Nivel Main.",e.getMessage());
        }
    }
    
}
