/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

/**
 *
 * @author David
 */
public class Time {
    Operation Operation;
    Register rg;
    Util util;
    Tick tick2;
    long ultMin;
    long ultHor;
    long ultDia;
    
    int openUltMin;
    int maxUltMin;
    int minUltMin;
    int openUltHor;
    int maxUltHor;
    int minUltHor;
    int openUltDia;
    int maxUltDia;
    int minUltDia;

    public Time() {
         Operation= new Operation();
         rg= new Register();
         tick2=null;
         util= new Util();
         ultMin=0;
         ultHor=0;
         ultDia=0;
         
         openUltMin=0;
         maxUltMin=0;
         minUltMin=0;
         
         openUltHor=0;
         maxUltHor=0;
         minUltHor=0;
         
         openUltDia=0;
         maxUltDia=0;
         minUltDia=0;
         
    }
    
    public void loadTick(Tick tick) throws Exception {
        util.MinMaxOrdenes(tick);
        util.verificarCierresAuto(tick);
        verificarMinMaxTick(tick);
        timeController(tick);
        util.MinMaxOrdenes(tick);
        util.verificarCierresAuto(tick);        
    }
    private void timeController(Tick tick) {
        try {
            OperarDias(2,tick);
            OperarHoras(4,tick);
            OperarMinutos(5,tick);
            operarTick(tick);
        } catch (Exception ex) {            
            rg.exception("Error al obtener Vela historica", ex.getMessage());
        }
    }
    private void operarTick(Tick tick) throws Exception{
        Operation.operarTick(tick);
    }
    private void OperarDias(int cantidad,Tick tick) throws Exception{
         if ((tick.getTime()-ultDia)>=cantidad*60*60*24) {
            tick2=tick.clone();
            tick2.setOpen(openUltDia);
            tick2.setMax(maxUltDia);
            tick2.setMin(minUltDia);
             if (openUltDia!=0) {
                 Operation.OperarDias(tick2,tick);
             }            
            ultDia=tick.getTime();
            maxUltDia=0;
            minUltDia=0;
            openUltDia=tick.getOpen();   
        }
    }
    private void OperarHoras(int cantidad,Tick tick) throws Exception {
        if ((tick.getTime()-ultHor)>=cantidad*60*60) {
            tick2=tick.clone();
            tick2.setOpen(openUltHor);
            tick2.setMax(maxUltHor);
            tick2.setMin(minUltHor);
             if (openUltHor!=0) {
                 Operation.OperarHoras(tick2,tick);
             }            
            ultHor=tick.getTime();
            maxUltHor=0;
            minUltHor=0;
            openUltHor=tick.getOpen();   
        }        
    }
    private void OperarMinutos(int cantidad,Tick tick) throws Exception{
         if ((tick.getTime()-ultMin)>=cantidad*60) {
            tick2=tick.clone();
            tick2.setOpen(openUltMin);
            tick2.setMax(maxUltMin);
            tick2.setMin(minUltMin);
             if (openUltMin!=0) {
                 Operation.OperarMinutos(tick2,tick);
             }            
            ultMin=tick.getTime();
            maxUltMin=0;
            minUltMin=0;
            openUltMin=tick.getOpen(); 
        }   
    }

    private void verificarMinMaxTick(Tick tick) {
        if (tick.getMin()<minUltMin || minUltMin==0) {
            minUltMin=tick.getMin();
        }
        if (tick.getMin()<minUltHor || minUltHor==0) {
            minUltHor=tick.getMin();
        }
        if (tick.getMin()<minUltDia || minUltDia==0) {
            minUltDia=tick.getMin();
        }
        
        if (tick.getMax()>maxUltMin || maxUltMin==0) {
            maxUltMin=tick.getMax();
        }
        if (tick.getMax()>maxUltHor || maxUltHor==0) {
            maxUltHor=tick.getMax();
        }
        if (tick.getMax()>maxUltDia || maxUltDia==0) {
            maxUltDia=tick.getMax();
        }
    }
    
}
