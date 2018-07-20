/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

import static dvsimulator.DvSimulator.ORDENES;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author David
 */
public class Orden {

    public int idTicket, open,close, take, stop, finaltake, finalstop,minAlcanzado,maxAlcanzado;
    double vol, spreead, beneficio;
    boolean error, sell, buy, iniciada, finalizada, verificada;
    public String fechaClose, fechaInicio, id, observacion, MensajeError, tipoFinOperacion;
    public long timeClose, timeInicio, tiempoOperacion;
    public Register rg;
    public Util util;

    public Orden(int idTicket, String fecha, int open, int take, int stop,
            double vol, double spreead, boolean sell, boolean buy,
            String observacion)
            throws Exception {
        rg= new Register();
        util=new Util();
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy H:mm");
        Date fechaDate = formatFecha.parse(fecha);
        this.idTicket = idTicket;
        this.id = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + idTicket;
        this.open = open;
        this.close = 0;
        this.take = take;
        this.stop = stop;
        this.finaltake = take;
        this.finalstop = stop;
        this.tiempoOperacion = 0;
        this.vol = vol;
        this.spreead = spreead;
        this.beneficio = 0;
        this.error = false;
        this.iniciada = false;
        this.finalizada = false;
        this.verificada = false;
        this.sell = sell;
        this.buy = buy;
        this.fechaInicio = fecha;
        this.timeInicio = fechaDate.getTime() / 1000;
        this.fechaClose = "";
        this.timeClose = 0;
        this.observacion = observacion;
        this.tipoFinOperacion = "";
        this.MensajeError = "";
        this.minAlcanzado=0;
        this.maxAlcanzado=0;
    }

    public boolean verificarOrden() {
        try {              
        this.error=false;        
        if (buy) {
            if ((take<=open || Util.pipDif(open,take)<=spreead)&&take!=0) {
                 this.error=true;
                 this.MensajeError+="El take no esta bien ubicado para operacion de compra.";
            }
            if ((stop>=open || Util.pipDif(open,stop)<=spreead)&&stop!=0) {
                 this.error=true;
                 this.MensajeError+="El Stop no esta bien ubicado para operacion de compra.";
            }
        }else if(sell){
            if ((take>=open || Util.pipDif(open,take)<=spreead)&&take!=0) {
                 this.error=true;
                 this.MensajeError+="El take no esta bien ubicado para operacion de venta.";
            }
            if ((stop<=open || Util.pipDif(open,stop)<=spreead)&&stop!=0) {
                 this.error=true;
                 this.MensajeError+="El Stop no esta bien ubicado para operacion de venta.";
            }            
        }else{
            this.error=true;
            this.MensajeError+="No se marco Orden ni venta ni compra";
        }
        if (this.vol <=0) {
            this.error=true;
            this.MensajeError+="El volumen es incorrecto.";
        }
        if (this.spreead <0) {
            this.error=true;
            this.MensajeError+="El spreead es incorrecto.";
        }
        if (util.verificarOrdenDuplicada(this.idTicket,this.id)) {
           this.error=true;
           this.MensajeError+="Ya hay una orden activa con el mismo ID.";     
        }
        
        //Verifica si ocurrio un error en las validaciones anteriores
        if (this.error) {
            saveLogOrden("verificacion Error");
            ORDENES.add(this);
            return false;
        }else{
            this.MensajeError="Validacion OK";
            this.verificada=true;
            this.iniciada=true;
            ORDENES.add(this);
            saveLogOrden("verificacion Ok");
            return true;
        }        
        } catch (Exception e) {
            this.error=true;
            this.MensajeError="Ocurrio un error al hacer la verificación, puede ser tipos de datos incorrectos.";
            saveLogOrden("Error verificación y Abriendo");
            rg.exception("Error verificando y Abriendo Orden", e.getMessage());
            return false;
        }
        
    }
    
    public boolean cerrarOrden(Tick tick){
        try {
            
            if (this.buy) {            
                if (this.stop!=0 && (tick.getMax()<=this.stop || tick.getMin()<=this.stop)) {                
                    this.observacion+=" Stop Alcanzado";
                    this.tipoFinOperacion="Stop";
                    this.close=this.stop;
                }
                else if (this.take!=0 && (tick.getMax()>=this.take || tick.getMin()>=this.take)) {                
                    this.observacion+=" Take Alcanzado";
                    this.tipoFinOperacion="Take";
                    this.close=this.take;
                }else{
                    this.observacion+=" Cerrado Manual";
                    this.tipoFinOperacion="Manual";
                    this.close=tick.close;
                }
            }else if (this.sell){
                if (this.stop!=0 && (tick.getMax()>=this.stop || tick.getMin()>=this.stop)) {                
                    this.observacion+=" Stop Alcanzado";
                    this.tipoFinOperacion="Stop";
                    this.close=this.stop;
                }
                else if (this.take!=0 && (tick.getMax()<=this.take || tick.getMin()<=this.take)) {                
                    this.observacion+=" Take Alcanzado";
                    this.tipoFinOperacion="Take";
                    this.close=this.take;
                }else{
                    this.observacion+=" Cerrado Manual";
                    this.tipoFinOperacion="Manual";
                    this.close=tick.close;
                }
            }
            this.timeClose=tick.getTime();
            this.fechaClose=tick.getSfecha(); 
            this.idTicket=tick.getId();
            this.tiempoOperacion=this.timeClose-this.timeInicio;
            this.finalizada=true;
            if (this.buy) {
                this.beneficio=this.close-this.open;
            }else{
                this.beneficio=this.open-this.close;
            }            
            saveLogOrden("Orden Cerrada");
            return true;
        } catch (Exception e) {
            this.finalizada=false;
            this.error=true;
            this.observacion+=" Se intento cerrar operacion pero se genero excepcion";
            this.MensajeError="No se pudo cerrar orden: "+e.getMessage();
            saveLogOrden("Error Cerrando Orden");
            rg.exception("Error cerrando Orden", e.getMessage());
            return false;
        }
    }
    
    public void verificarCierre(Tick tick){
        if (checkActiva()) {   
            if (this.buy) {
                if (this.stop!=0 && (tick.getMax()<=this.stop || tick.getMin()<=this.stop)) {                
                        cerrarOrden(tick);
                    }
                else if (this.take!=0 && (tick.getMax()>=this.take || tick.getMin()>=this.take)) {                
                    cerrarOrden(tick);
                }
            }else if(this.sell){
                if (this.stop!=0 && (tick.getMax()>=this.stop || tick.getMin()>=this.stop)) {                
                        cerrarOrden(tick);
                    }
                    else if (this.take!=0 && (tick.getMax()<=this.take || tick.getMin()<=this.take)) {                
                        cerrarOrden(tick);
                    }
            }
        }
    }

    public boolean checkActiva() {
        if (!error && iniciada && !finalizada && verificada) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCompleta() {
        if (!error && iniciada && finalizada && verificada) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPendiente() {
        if (!error && !iniciada && !finalizada && verificada) {
            return true;
        } else {
            return false;
        }
    }

    public void printOrden() {
        String lineOrden = new SimpleDateFormat("dd/MM/yyyy H:mm:ss").format(new Date()) + ";"
                + idTicket + id + ";" + open + ";" +close + ";" + take + ";" + stop + ";" + finaltake + ";"
                + finalstop + ";" + tiempoOperacion + ";" + vol + ";" + spreead + ";"
                + beneficio + ";" + error + ";" + iniciada + ";" + finalizada + ";"
                + verificada + ";" + sell + ";" + buy + ";" + fechaInicio + ";"
                + fechaClose + ";" + timeInicio +";" + timeClose + ";" + observacion
                + ";" + MensajeError + ";" + tipoFinOperacion;
        System.out.println(lineOrden);
    }

    public void saveLogOrden(String aux) {
        String lineOrden = new SimpleDateFormat("dd/MM/yyyy H:mm:ss").format(new Date()) + ";"
                + idTicket + ";" + id + ";" + open + ";" + close + ";" + take + ";" + stop + ";" + finaltake + ";"
                + finalstop + ";" + tiempoOperacion + ";" + vol + ";" + spreead + ";"
                + beneficio + ";" + error + ";" + iniciada + ";" + finalizada + ";"
                + verificada + ";" + sell + ";" + buy + ";" + fechaInicio + ";"
                + fechaClose +";" + timeInicio+ ";" + timeClose + ";" + minAlcanzado +";" + maxAlcanzado + ";"  + observacion
                + ";" + MensajeError + ";" + tipoFinOperacion + ";" + aux;
        rg.orden(lineOrden);
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }
    
    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int getFinaltake() {
        return finaltake;
    }

    public void setFinaltake(int finaltake) {
        this.finaltake = finaltake;
    }

    public int getFinalstop() {
        return finalstop;
    }

    public void setFinalstop(int finalstop) {
        this.finalstop = finalstop;
    }

    public long getTiempoOperacion() {
        return tiempoOperacion;
    }

    public void setTiempoOperacion(int tiempoOperacion) {
        this.tiempoOperacion = tiempoOperacion;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getSpreead() {
        return spreead;
    }

    public void setSpreead(double spreead) {
        this.spreead = spreead;
    }

    public double getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(double beneficio) {
        this.beneficio = beneficio;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSell() {
        return sell;
    }

    public void setSell(boolean sell) {
        this.sell = sell;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public boolean isIniciada() {
        return iniciada;
    }

    public void setIniciada(boolean iniciada) {
        this.iniciada = iniciada;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public boolean isVerificada() {
        return verificada;
    }

    public void setVerificada(boolean verificada) {
        this.verificada = verificada;
    }

    public String getFechaClose() {
        return fechaClose;
    }

    public void setFechaClose(String fechaClose) {
        this.fechaClose = fechaClose;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public long getTimeStop() {
        return timeClose;
    }

    public void setTimeStop(long timeStop) {
        this.timeClose = timeStop;
    }

    public long getTimeInicio() {
        return timeInicio;
    }

    public void setTimeInicio(long timeInicio) {
        this.timeInicio = timeInicio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getMensajeError() {
        return MensajeError;
    }

    public void setMensajeError(String MensajeError) {
        this.MensajeError = MensajeError;
    }

    public String getTipoFinOperacion() {
        return tipoFinOperacion;
    }

    public void setTipoFinOperacion(String tipoFinOperacion) {
        this.tipoFinOperacion = tipoFinOperacion;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getMinAlcanzado() {
        return minAlcanzado;
    }

    public void setMinAlcanzado(int minAlcanzado) {
        this.minAlcanzado = minAlcanzado;
    }

    public int getMaxAlcanzado() {
        return maxAlcanzado;
    }

    public void setMaxAlcanzado(int maxAlcanzado) {
        this.maxAlcanzado = maxAlcanzado;
    }

    public long getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(long timeClose) {
        this.timeClose = timeClose;
    }
    
}
