/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
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
    String type;
    int data;
    String echo = "";
    ArrayList<String> msgs2send;

    public LegSerialPort(String portName, MainFrame pt) {
        super(portName);
        parent = pt;
        type = "";
        data = -1;
        msgs2send = new ArrayList<>();

        try {
            this.openPort();
            this.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            Thread.sleep(2000);
            this.writeString("S");
            //                           this.addEventListener(new PortWriter(this),
            //                                   SerialPort.MASK_TXEMPTY);
            this.addEventListener(new PortReader(this));
        } catch (SerialPortException | InterruptedException ex) {
            Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stopSerial() throws SerialPortException {
        this.closePort();
        System.out.println("Port closed");
    }

    public void sendData(String c, int value) {
        msgs2send.add(value + c);
    }

    private static class PortReader implements SerialPortEventListener {

        LegSerialPort serialPort;
        String buffer = "";

        private PortReader(LegSerialPort aThis) {
            serialPort = aThis;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort.
                            readString(event.getEventValue());

                    System.out.println(receivedData);
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
                    }
                } catch (SerialPortException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (event.isTXEMPTY()) {
                System.out.println("where my resources at" + event.isTXEMPTY());
                if (serialPort.msgs2send.size() > 0) {
                    System.out.println("now sending: "
                            + serialPort.msgs2send.get(0));
                }

                try {
                    serialPort.writeString(serialPort.msgs2send.get(0));
                } catch (SerialPortException ex) {
                    Logger.getLogger(LegSerialPort.class.getName()).log(Level.SEVERE, null, ex);
                }

                serialPort.msgs2send.remove(0);

            }

        }
    }

}
