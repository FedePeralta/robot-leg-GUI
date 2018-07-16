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
public class Leg {
    
    double x0, y0, x1, y1, phi;
    final double length;

    public Leg(double startX, double startY, double length, double angle) {
        this.length  = length;
        x0 = startX;
        y0 = startY;
        phi = angle;
        x1 = x0 + length*Math.sin(phi);
        y1 = y0 + length*Math.cos(phi);
    }

    public Leg(double[] startXY, double length, double angle) {
        this.length  = length;
        x0 = startXY[0];
        y0 = startXY[1];
        phi = angle;
        x1 = x0 + length*Math.sin(phi);
        y1 = y0 + length*Math.cos(phi);
    }

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
        x1 = x0 + length*Math.sin(phi);
        y1 = y0 + length*Math.cos(phi);
    }
    
    public double getLength(){
        return length;
    }
    
    public double [] getEndXY(){
        return new double [] {x1,y1};
    }

    public void setXY(double[] startXY) {
        x0 = startXY[0];
        y0 = startXY[1];
    }
    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    @Override
    public String toString() {
        return "Leg{" + "x0=" + x0 + ", y0=" + y0 + ", x1=" + x1 + ", y1=" + y1 + ", phi=" + phi + ", length=" + length + '}';
    }
}
