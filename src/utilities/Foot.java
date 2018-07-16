/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author Fede
 */
public class Foot {

    double x0, y0, x1, y1, phi, xM, yM;
    final double length, ankleToeProportion;


    public Foot(double[] anklePointXY, double length, double angle,
            double atProp) {
        this.length = length;
        xM = anklePointXY[0];
        yM = anklePointXY[1];
        x0 = xM - (1-atProp)*length*Math.sin(angle);
        x0 = yM - (1-atProp)*length*Math.cos(angle);
        phi = angle;
        x1 = xM + length * atProp * Math.sin(phi);
        y1 = yM + length * atProp * Math.cos(phi);
        ankleToeProportion = atProp;
    }

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }
    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
        x0 = xM + (ankleToeProportion - 1) * length * Math.sin(phi);
        y0 = yM + (ankleToeProportion - 1) * length * Math.cos(phi);
        x1 = xM + length * ankleToeProportion * Math.sin(phi);
        y1 = yM + length * ankleToeProportion * Math.cos(phi);
        
    }

    public double getLength() {
        return length;
    }

    public double[] getEndXY() {
        return new double[]{x1, y1};
    }

    public void setAnkleXY(double[] startXY) {
        xM = startXY[0];
        yM = startXY[1];
    }


}
