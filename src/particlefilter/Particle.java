package particlefilter;

import java.util.Random;

/**
 *
 * @author Michael
 */
public class Particle {
    
    public float orientation, forwardNoise, turnNoise, senseNoise;
    public float x, y;
    public int width;
    public int height;
    public int worldSize;
    public double probability = 0;
    public Point[] landmarks;
    Random gen;
    
    public Particle(Point[] landmarks, int width, int height) {
        gen = new Random();
        this.landmarks = landmarks;
        this.width = width;
        this.height = height;
        this.worldSize = width * height;
        
        x = gen.nextFloat() * width;
        y = gen.nextFloat() * height;        
        orientation = gen.nextFloat() * 2f * ((float)Math.PI);
        forwardNoise = 0f;
        turnNoise = 0f;
        senseNoise = 0f;        
    }
    
    public void set(float x, float y, float orientation, double prob) throws Exception {
        if(x < 0 || x >= width) {
            throw new Exception("X coordinate out of bounds");
        }
        if(y < 0 || y >= height) {
            throw new Exception("Y coordinate out of bounds");
        }
        if(orientation < 0 || orientation >= 2 * Math.PI) {
            throw new Exception("X coordinate out of bounds");
        }
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.probability = prob;
    }
    
    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        this.forwardNoise = Fnoise;
        this.turnNoise = Tnoise;
        this.senseNoise = Snoise;
    }
    
    public float[] sense() {
        float[] ret = new float[landmarks.length];
        
        for(int i=0;i<landmarks.length;i++){
            float dist = (float) distance(x, y, landmarks[i].x, landmarks[i].y);
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
        
        x += Math.cos(orientation) * dist;
        y += Math.sin(orientation) * dist;
        x = circle(x, width);
        y = circle(y, height);
        
    }
    
    private double Gaussian(double mu, double sigma, double x) {       
        return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
    }
    
    public static double distance(float x1, float y1, float x2, float y2) { 
        
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    private float circle(float num, float length) {         
        while(num > length - 1) num -= length;
        while(num < 0) num += length;
        return num;       
    }
    
    public double measurementProb(float[] measurement) {
        double prob = 1.0;
        for(int i=0;i<landmarks.length;i++) {
            float dist = (float) distance(x, y, landmarks[i].x, landmarks[i].y);            
            prob *= Gaussian(dist, senseNoise, measurement[i]);            
        }      
        
        probability = prob;
        
        return prob;
    }

    @Override
    public String toString() {
        return "[x=" + x + " y=" + y + " orient=" + Math.toDegrees(orientation) + " prob=" +probability +  "]";
    }
    
}
