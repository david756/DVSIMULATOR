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
public class Operation {
    
    Register rg= new Register();
    Util u = new Util();
    Boolean estado=true;

    void operarTick(Tick tick) {
        //rg.log(" dejando log");
        if (tick.id==3) {
            //u.abrirVenta2(tick,0,0,"PRUEBA");
        }
    }

    void OperarHoras(Tick tickHora,Tick tick) {
        //tickHora.guardarLogTick("Registro de 4 Horas");
    }

    void OperarMinutos(Tick tickMinuto,Tick tick) {
        if (tick.id>=50000 && estado) {
            //estado=false;
            //Tick t=u.Vela(2, "d", 3, tick);
            //tick.guardarLogTick("Vela inicial");
            //t.guardarLogTick("velas de 2 d , 3 periodos atras");
        }
        
    }

    void OperarDias(Tick tickDia,Tick tick) {
    
    }
}
