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
public class ParticleFilter {

    /**
     * @param args the command line arguments
     */
    int numOfParticles = 0;
    Random gen = new Random();
    Particle[] particles;

    public ParticleFilter(int numOfParticles, Point[] landmarks, int width, int height) {
        this.numOfParticles = numOfParticles;

        particles = new Particle[numOfParticles];
        for (int i = 0; i < numOfParticles; i++) {
            particles[i] = new Particle(landmarks, width, height);
        }
    }

    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        for (int i = 0; i < numOfParticles; i++) {
            particles[i].setNoise(Fnoise, Tnoise, Snoise);
        }
    }

    public void move(float turn, float forward) throws Exception {
        for (int i = 0; i < numOfParticles; i++) {
            particles[i].move(turn, forward);
        }
    }

    public Particle getBestParticle() {
        Particle particle = particles[0];
        for (int i = 0; i < numOfParticles; i++) {
            if (particles[i].probability > particle.probability) {
                particle = particles[i];
            }
        }
        return particle;
    }

    public void resample(float[] measurement) throws Exception {
        Particle[] new_particles = new Particle[numOfParticles];

        for (int i = 0; i < numOfParticles; i++) {
            particles[i].measurementProb(measurement);
        }
        float B = 0f;
        Particle best = getBestParticle();
        int index = (int) gen.nextFloat() * numOfParticles;
        for (int i = 0; i < numOfParticles; i++) {
            B += gen.nextFloat() * 2f * best.probability;
            while (B > particles[index].probability) {
                B -= particles[index].probability;
                index = circle(index + 1, numOfParticles);
            }
            new_particles[i] = new Particle(particles[index].landmarks, particles[index].width, particles[index].height);
            new_particles[i].set(particles[index].x, particles[index].y, particles[index].orientation, particles[index].probability);
            new_particles[i].setNoise(particles[index].forwardNoise, particles[index].turnNoise, particles[index].senseNoise);
        }

        particles = new_particles;        
    }

    private int circle(int num, int length) {
        while (num > length - 1) {
            num -= length;
        }
        while (num < 0) {
            num += length;
        }
        return num;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < numOfParticles; i++) {
            res += particles[i].toString() + "\n";
        }
        return res;
    }

    public static double Gaussian(double mu, double sigma, double x) {
        return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
    }
    

    public static void main(String[] args) throws Exception {
        

        Point[] landmarks = new Point[]{new Point(20f, 20f), new Point(80f, 80f), new Point(20f, 80f), new Point(80f, 20f)};
        
      
        float[] Z = new float[]{0, 84.8528f, 60, 60};
        System.out.println("Starting");
        ParticleFilter filter = new ParticleFilter(1000, landmarks, 100, 100);
        filter.setNoise(0.05f,  0.05f, 5f);
        
        //filter.move(.1f, 5f);
        
        System.out.println("sample...");
        filter.resample(Z);
        System.out.println(filter.toString());
        filter.setNoise(0.05f,  0.05f, 5f);
        filter.move(0f, 5f);
        
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println(filter.toString());
        
        System.out.println("done!");

        

        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("best: "
                + filter.getBestParticle().toString());
               
                
        












    }
}
