/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlefilter;

import java.util.Random;

/**
 *
 * @author Michael
 */
public class Robot {
    public double x, y, orientation, forwardNoise, turnNoise, senseNoise;
    public int worldSize;

    public Robot(int worldSize) {
        this.worldSize = worldSize;
        Random gen = new Random();
        x = gen.nextDouble() * worldSize;
        y = gen.nextDouble() * worldSize;
        orientation = gen.nextDouble() * 2.0 * Math.PI;
        forwardNoise = 0.0;
        turnNoise = 0.0;
        senseNoise = 0.0;        
    }
    
    public void set(double x, double y, double orientation) throws Exception {
        if(x < 0 || x >= worldSize) {
            throw new Exception("X coordinate out of bounds");
        }
        if(y < 0 || y >= worldSize) {
            throw new Exception("Y coordinate out of bounds");
        }
        if(orientation < 0 || orientation >= 2 * Math.PI) {
            throw new Exception("X coordinate out of bounds");
        }
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }
    
    public void setNoise(double Fnoise, double Tnoise, double Snoise) {
        this.forwardNoise = Fnoise;
        this.turnNoise = Tnoise;
        this.senseNoise = Snoise;
    }
    
    public double[] sense(double[] distances) {
        double[] ret = new double[distances.length];
        Random gen = new Random();
        for(int i=0;i<distances.length;i++){
            ret[i] = distances[i] + gen.nextGaussian() * senseNoise;
        }      
        
        return ret;
    }
    
    public void move(double turn, double forward) throws Exception {
        Random gen = new Random();
        if(forward < 0) {
            throw new Exception("Robot cannot move backwards");
        }
        orientation = orientation + turn + gen.nextGaussian() * turnNoise;
        orientation = circle(orientation, 2 * Math.PI);
        
        double dist = forward + gen.nextGaussian() * forwardNoise;
        x += Math.cos(orientation) * dist;
        y += Math.sin(orientation) * dist;
        x = circle(x, worldSize);
        y = circle(y, worldSize);      
    }
    
    private double Gaussian(double mu, double sigma, double x) {       
        return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
    }    
    
    private double circle(double num, double length) {         
        while(num > length - 1) num -= length;
        while(num < 0) num += length;
        return num;       
    }
    
    public double measurementProb(double[] measurement, double[] distances) {
        double prob = 1.0;
        for(int i=0;i<distances.length;i++) {
            prob *= Gaussian(distances[i], senseNoise, measurement[i]);
        }
        return prob;
    }
    
    
    
    
    
}
