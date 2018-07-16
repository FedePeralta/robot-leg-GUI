/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import static java.lang.Thread.yield;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;
import visual.MainFrame;

/**
 *
 * @author Fede
 */
public class LegSerialPort extends SerialPort implements Runnable {

    byte state;
    MainFrame parent;
    String type;
    int data;
    String echo;
    public LegSerialPort(String portName) {
        super(portName);
    }

    @Override
    public void run() {
        try {
            do {
                switch (state) {
                    // Inicializar
                    case 0: {
                        try {
                            this.openPort();
                            this.setParams(SerialPort.BAUDRATE_9600,
                                    SerialPort.DATABITS_8,
                                    SerialPort.STOPBITS_1,
                                    SerialPort.PARITY_NONE);
                            state = 1;
                        } catch (SerialPortException ex) {
                            parent.messageFromSerial("error", ex);
                            state = 0;
                        }
                        break;
                    }
                    // Chequear Conexion
                    case 1: {
                        try {
                            this.writeString("S");
                            echo = this.readString();
                            
                            if (echo.equals("S")){
                                parent.messageFromSerial("conStatus", echo);
                                state = 2;
                            }
                            else state = 0;
                        } catch (SerialPortException ex) {
                            parent.messageFromSerial("error", ex);
                            state = 0;
                        }
                        break;
                    }
                    
                    case 2: {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return; 
                        }
                        break;
                    }
                    case 3: {
                        String auxtype = type;
                        int auxdata = data;
                        this.writeString(auxtype);
                        this.writeInt(auxdata);
                        echo = this.readString();
                        parent.messageFromSerial(auxtype, echo);
                        state = 2;
                        break;
                    }
                    default: {
                        state = 0;
                    }
                }
            } while (state != 0);
            if (this.isOpened()) this.closePort();
            Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null,
                    "Thread de conexion finalizo");
        } catch (SerialPortException ex) {
            Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialize(MainFrame pt) {
        parent = pt;
        state = 0;
        type = "";
        data = -1;
    }

    public void stopSerial() {
        state = -1;
    }

    public void sendData(String c, int value) {
        state = 3;
        type = c;
        data = value;
    }

}
