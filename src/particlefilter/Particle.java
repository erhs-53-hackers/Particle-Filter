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
public class Particle {
    public float orientation, forwardNoise, turnNoise, senseNoise;
    public Point location;
    public int worldSize;
    public Point[] landmarks;
    Random gen;

    public Particle(Point[] landmarks, int worldSize) {
        gen = new Random();
        this.landmarks = landmarks;
        this.worldSize = worldSize; 
        location = new Point();
        location.x = gen.nextFloat() * this.worldSize;
        location.y = gen.nextFloat() * this.worldSize;        
        orientation = gen.nextFloat() * 2f * ((float)Math.PI);
        forwardNoise = 0f;
        turnNoise = 0f;
        senseNoise = 0f;        
    }
    
    public void set(float x, float y, float orientation) throws Exception {
        if(x < 0 || x >= worldSize) {
            throw new Exception("X coordinate out of bounds");
        }
        if(y < 0 || y >= worldSize) {
            throw new Exception("Y coordinate out of bounds");
        }
        if(orientation < 0 || orientation >= 2 * Math.PI) {
            throw new Exception("X coordinate out of bounds");
        }
        location.x = x;
        location.y = y;
        this.orientation = orientation;
    }
    
    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        this.forwardNoise = Fnoise;
        this.turnNoise = Tnoise;
        this.senseNoise = Snoise;
    }
    
    public float[] sense() {
        float[] ret = new float[landmarks.length];
        
        for(int i=0;i<landmarks.length;i++){
            float dist = (float) distance(location.x, landmarks[i].x, location.y, landmarks[i].y);
            ret[i] = dist + (float)gen.nextGaussian() * senseNoise;
        }       
        return ret;
    }    
    
    public void move(float turn, float forward) throws Exception {
        
        if(forward < 0) {
            throw new Exception("Robot cannot move backwards");
        }
        orientation = orientation + turn + (float)gen.nextGaussian() * turnNoise;
        orientation = circle(orientation, 2f * (float)Math.PI);
        
        double dist = forward + gen.nextGaussian() * forwardNoise;
        location.x += Math.cos(orientation) * dist;
        location.y += Math.sin(orientation) * dist;
        location.x = circle(location.x, worldSize);
        location.y = circle(location.y, worldSize);      
    }
    
    private double Gaussian(double mu, double sigma, double x) {       
        return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
    }
    
    private double distance(float x1, float x2, float y1, float y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y2 - y2, 2));
    }
    
    private float circle(float num, float length) {         
        while(num > length - 1) num -= length;
        while(num < 0) num += length;
        return num;       
    }
    
    public double measurementProb(double[] measurement) {
        double prob = 1.0;
        for(int i=0;i<landmarks.length;i++) {
            float dist = (float) distance(location.x, landmarks[i].x, location.y, landmarks[i].y);
            prob *= Gaussian(dist, senseNoise, measurement[i]);
        }
        return prob;
    }

    @Override
    public String toString() {
        return "[x=" + location.x + " y=" + location.y + " orient=" + orientation;
    }
    
    
    
    
    
    
    
}
