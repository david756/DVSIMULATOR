/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

import static dvsimulator.DvSimulator.ORDENES;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import static dvsimulator.DvSimulator.RUTA_ARCHIVO;
import static dvsimulator.DvSimulator.SPREAD;
import static dvsimulator.DvSimulator.TICKS;
import static dvsimulator.DvSimulator.VOLUMEN;
/**
 *
 * @author David
 */
public class Util {

    public BufferedReader leerCsv(){
        String csvFile = RUTA_ARCHIVO;
        BufferedReader br = null;   
        Register rg = new Register();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            return br;
        } catch (Exception e) {
            rg.exception("Leer csv.",e.getMessage());
            return null;
        } 
    }
    public BufferedWriter escribirCsv(String ruta){
       try
        {
            File file = new File(ruta);
            file.createNewFile();
            BufferedWriter bw=null;
            bw = new BufferedWriter(new FileWriter(file,true));
            return bw;
        }
        catch(Exception e)
        {
            Register rg = new Register();
            rg.exception("No se pudo abrir archivo: "+ruta, e.getMessage());
            return null;
        }
        
    }    
   public long getTime(Date fecha){
       return fecha.getTime()/1000;
   }
   static double pipDif(int a, int b) {
        return Math.abs(a-b);
   }
   static int precioConDistancia(int a, int b) {
        return Math.abs(a+b);
   }
   
   
   /*
   * Abrir compra simple sin take ni stop
   */
   static boolean abrirCombra1(Tick tick,String observacion){
        try {
            Orden orden = new Orden(tick.id, tick.getSfecha(),tick.getOpen(), 0, 0, VOLUMEN, SPREAD, false, true, observacion);
            return orden.verificarOrden();
        } catch (Exception ex) {
            Register rg = new Register();
            rg.exception("Error al intetar abrir una operacion de compra ", ex.getMessage());
            return false;
        }
   }
   /*
   * Abrir Venta simple sin take ni stop
   */
   static boolean abrirVenta1(Tick tick,String observacion){
        try {
            Orden orden = new Orden(tick.id, tick.getSfecha(),tick.getOpen(), 0, 0, VOLUMEN, SPREAD, true, false, observacion);
            return orden.verificarOrden();
        } catch (Exception ex) {
            Register rg = new Register();
            rg.exception("Error al intetar abrir una operacion de venta ", ex.getMessage());
            return false;
        }
   }
   
   /*
   * Abrir compra con Stop y Take en distancia.
   */
   static boolean abrirCompra2(Tick tick,int DisTake, int DisStop, String observacion){
        try {
            int  stop=0;
            int take=0;
            if (DisStop!=0) {
              stop= precioConDistancia(tick.getOpen(),-DisStop);   
            }
            if (DisTake!=0) {
               take= precioConDistancia(tick.getOpen(),DisTake);
            }            
            Orden orden = new Orden(tick.id, tick.getSfecha(),tick.getOpen(), take, stop, VOLUMEN, SPREAD, false, true, observacion);
            return orden.verificarOrden();
        } catch (Exception ex) {
            Register rg = new Register();
            rg.exception("Error al intetar abrir una operacion de compra ", ex.getMessage());
            return false;
        }
   }
   
   /*
   * Abrir compra con Stop y Take en distancia.
   */
   static boolean abrirVenta2(Tick tick,int DisTake, int DisStop, String observacion){
        try {            
            int  stop=0;
            int take=0;
            if (DisStop!=0) {
               stop= precioConDistancia(tick.getOpen(),DisStop);            
            }
            if (DisTake!=0) {
               take= precioConDistancia(tick.getOpen(),-DisTake);  
            }
            
            Orden orden = new Orden(tick.id, tick.getSfecha(),tick.getOpen(), take, stop, VOLUMEN, SPREAD, true, false, observacion);
            return orden.verificarOrden();
        } catch (Exception ex) {
            Register rg = new Register();
            rg.exception("Error al intetar abrir una operacion de compra ", ex.getMessage());
            return false;
        }
   }
   
   /*
   * Abrir compra con Stop y Take fijos.
   */
   static boolean abrirCompra3(Tick tick,int take, int stop, String observacion){
        try {            
            Orden orden = new Orden(tick.id, tick.getSfecha(),tick.getOpen(), take, stop, VOLUMEN, SPREAD, false, true, observacion);
            return orden.verificarOrden();
        } catch (Exception ex) {
            Register rg = new Register();
            rg.exception("Error al intetar abrir una operacion de compra ", ex.getMessage());
            return false;
        }
   }
   
    /*
   * Abrir venta con Stop y Take fijos.
   */
   static boolean abrirVenta3(Tick tick,int take, int stop, String observacion){
        try {            
            Orden orden = new Orden(tick.id, tick.getSfecha(),tick.getOpen(), take, stop, VOLUMEN, SPREAD, true, false, observacion);
            return orden.verificarOrden();
        } catch (Exception ex) {
            Register rg = new Register();
            rg.exception("Error al intetar abrir una operacion de compra ", ex.getMessage());
            return false;
        }
   }

    void verificarCierresAuto(Tick tick) {
        for(int x=0;x<ORDENES.size();x++) {
            ORDENES.get(x).verificarCierre(tick);
        }
    }

    boolean verificarOrdenDuplicada(int idTick,String idOrden) {
        boolean status=false;
        for(int x=0;x<ORDENES.size();x++) {
            if (ORDENES.get(x).getIdTicket()==idTick || ORDENES.get(x).getId().equals(idOrden)) {
                if (ORDENES.get(x).checkActiva()) {
                    status=true;
                    break;
                }
            }
        }
        return status;
    }

    void MinMaxOrdenes(Tick tick) {
        
        for(int x=0;x<ORDENES.size();x++) {
            if (ORDENES.get(x).checkActiva()) {             
                if (ORDENES.get(x).getMinAlcanzado() > tick.getMin() || ORDENES.get(x).getMinAlcanzado()==0) {
                    ORDENES.get(x).setMinAlcanzado(tick.getMin());
                }
                if (ORDENES.get(x).getMaxAlcanzado() < tick.getMax() || ORDENES.get(x).getMaxAlcanzado()==0) {
                    ORDENES.get(x).setMaxAlcanzado(tick.getMax());
                }
            }
        }
    }

    void cerrarTodo(Tick tick) {
        for(int x=0;x<ORDENES.size();x++) {
            if (ORDENES.get(x).checkActiva()) {             
                ORDENES.get(x).cerrarOrden(tick);
            }
        }
    }
    
    public Tick Vela(int periodo, String tiempo,int cantidadAtras, Tick tick){
        long tiempoActual= tick.getTime();
        long tiempoBuscado=0;
        long tiempoBuscado2=0;
        
        if (tiempo.equals("m")) {
           tiempoBuscado=(tiempoActual-periodo*60*cantidadAtras); 
           tiempoBuscado2=(tiempoActual-periodo*60*(cantidadAtras+1));  
        }
        if (tiempo.equals("h")) {
            tiempoBuscado=(tiempoActual-periodo*60*60*cantidadAtras); 
            tiempoBuscado2=(tiempoActual-periodo*60*60*(cantidadAtras+1));
        }
        if (tiempo.equals("d")) {
            tiempoBuscado=(tiempoActual-periodo*60*60*24*cantidadAtras); 
            tiempoBuscado2=(tiempoActual-periodo*60*60*24*(cantidadAtras+1)); 
            
        }
        
        Tick tick2 = null;
        int y=0;
        for(int x=TICKS.size()-1;x>=0;x--) {
            if (TICKS.get(x).getTime()<=tiempoBuscado) {   
                try {
                    y=x;
                    tick2=TICKS.get(x).clone();
                    break;
                } catch (Exception ex) {
                     Register rg = new Register();
                     rg.exception("Clonaciion de Tick", ex.getMessage());
                    return null;
                }
            }
        }
        
        for (int i = y; i >=0; i--) { 
                    tick2.setOpen(TICKS.get(i).getOpen());
            if (TICKS.get(i).getMin() < tick2.getMin()) {
                    tick2.setMin(TICKS.get(i).getMin());
            }
            if (TICKS.get(i).getMax() > tick2.getMax()) {
                    tick2.setMax(TICKS.get(i).getMax());
            }
            
            if (TICKS.get(i).getTime()<=tiempoBuscado2) {   
                return tick2;
            }            
        }
        
        return tick2;
    }
}
