/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

import static dvsimulator.DvSimulator.TICKS;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author David
 */
public class Tick implements Cloneable{
    public int id,open,max,min,close,vol;
    public long time;
    public Date dia,hora,fecha;
    public String sdia,shora,sfecha;
    public Register rg;

    public Tick(String id,String dia, String hora, String fecha, String open, String max, String min, String close, String vol) throws Exception {
        SimpleDateFormat formatDia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("H:mm");
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy H:mm");
        this.id = Integer.parseInt(id);
        this.open = Integer.parseInt(open);
        this.max = Integer.parseInt(max);
        this.min = Integer.parseInt(min);
        this.close = Integer.parseInt(close);
        this.vol = Integer.parseInt(vol);
        this.dia = formatDia.parse(dia);
        this.hora = formatHora.parse(hora);
        this.fecha = formatFecha.parse(fecha);
        this.time = this.fecha.getTime()/1000;
        this.sdia = dia;
        this.shora = hora;
        this.sfecha = fecha;
        TICKS.add(this);
        rg= new Register();
    }
    
    void printTick(String commentario) {
        System.out.println(commentario+" / "+this.id+" "+this.sfecha+" "+this.time+" "+this.open+" "+this.max+" "+this.min+" "+this.close+" "+this.vol);
    }
    public void guardarLogTick(String commentario){
        String text=commentario+";"+this.id+";"+this.sfecha+";"+this.time+";"+this.open+";"+this.max+";"+this.min+";"+this.close+";"+this.vol;
        rg.log(text);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSdia() {
        return sdia;
    }

    public void setSdia(String sdia) {
        this.sdia = sdia;
    }

    public String getShora() {
        return shora;
    }

    public void setShora(String shora) {
        this.shora = shora;
    }

    public String getSfecha() {
        return sfecha;
    }

    public void setSfecha(String sfecha) {
        this.sfecha = sfecha;
    }   

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
    public Tick clone() throws CloneNotSupportedException{
         Tick clonmalefico = (Tick) super.clone();
         return clonmalefico;
    }   
    
    
        
}
