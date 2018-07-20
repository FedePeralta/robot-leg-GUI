/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import visual.MainFrame;

/**
 *
 * @author Fede
 */
public class LegSerialPort extends SerialPort {

    MainFrame parent;
    public String type;

    public LegSerialPort(String portName, MainFrame pt) {
        super(portName);
        parent = pt;
        type = "";
        try {
            this.openPort();
            this.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            Thread.sleep(1500);
            this.writeString("S");
            this.addEventListener(new PortReaderWriter(this));
        } catch (SerialPortException | InterruptedException ex) {
            Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopSerial() throws SerialPortException {
        this.closePort();
        System.out.println("Port closed");
    }

    public void sendData(String c, int value) {
        if (type.equals("")) {
            try {
                System.out.println("sending data " + value + c);
                type = c;
                this.writeString(value + c);
            } catch (SerialPortException ex) {
                Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                do {
                    Thread.sleep(100);
                    System.out.println("System busy, please wait");
                } while (type.equals(""));
                Thread.sleep(100);
                System.out.println("sending data " + value + c);
                type = c;
                this.writeString(value + c);
            } catch (InterruptedException | SerialPortException ex) {
                Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static class PortReaderWriter implements SerialPortEventListener {

        LegSerialPort serialPort;
        String buffer = "";

        private PortReaderWriter(LegSerialPort aThis) {
            serialPort = aThis;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort.
                            readString(event.getEventValue());
                    if (receivedData.contains("S")) {
                        serialPort.parent.messageFromSerial(
                                "conStatus", "S");
                    } else if (buffer.equals("") && receivedData.indexOf(13) < 0
                            && receivedData.getBytes()[0] != 10) {
                        buffer += receivedData;
                    } else if (receivedData.indexOf(13) >= 0) {
                        buffer += receivedData.substring(0, receivedData.indexOf(13));
                        serialPort.parent.messageFromSerial(
                                serialPort.type, buffer);
                        buffer = "";
                        
                        serialPort.type = "";
                    }
                } catch (SerialPortException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
