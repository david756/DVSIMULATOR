/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dvsimulator;

import java.io.BufferedReader;
import static dvsimulator.DvSimulator.RUTA_EXCEPTION;
import static dvsimulator.DvSimulator.RUTA_LOG;
import static dvsimulator.DvSimulator.RUTA_ARCHIVO;
import static dvsimulator.DvSimulator.RUTA_ORDENES;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author David
 */
public class WhileController {
    
    
    void start() throws Exception {
        iniciarRutas();        
        double contador=0;
        Util util = new Util();
        Time time = new Time();
        BufferedReader br = null; 
        String line = "";        
        br = util.leerCsv();        
        String cvsSplitBy = ";";
        Tick tick=null;
        
        while ((line = br.readLine()) != null) {
            //evitar encabezado
            
            if (contador!=0) {
                String[] datos = line.split(cvsSplitBy);          
                tick = new Tick(datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8]);
                tick.printTick("Commentario");
                time.loadTick(tick);  
            }
            contador++;            
        }
        util.cerrarTodo(tick);
    }

    private void iniciarRutas() {
    
        String date=new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date()); 
        String rutaSimulador="C:\\DVSIMULATOR";
        String rutaGeneral=rutaSimulador+"\\"+date;
        String rutaLog=rutaGeneral+"\\log";
        File directorio=new File(rutaGeneral); 
        directorio.mkdir(); 
        directorio=new File(rutaLog); 
        directorio.mkdir();          
        RUTA_ARCHIVO=rutaSimulador+"\\archivo.csv";
        RUTA_EXCEPTION=rutaLog+"\\Exception.csv";
        RUTA_LOG=rutaLog+"\\log.csv";    
        RUTA_ORDENES=rutaLog+"\\Ordenes.csv";
        
        encabezadoOrdenes();        
    }
    public void encabezadoOrdenes(){
        Register rg=new Register();
        String lineOrden = "Fecha PC; Id Ticket ; Id Operacion ; Open ;Close ;Take; Stop ; Final Take;"
                + "Final Stop; Tiempo Operacion; Volumen; Spread; Beneficio; Error; Iniciada; Finalizada; "
                + "Verificada; Sell; Buy ; Fecha Open; fecha Close;Time Open; Time Close; Minimo ; Maximo ; Observacion; Mensaje Error; Tipo Fin Operacion; Auxiliar";
        rg.orden(lineOrden);
    }
    
    
}
