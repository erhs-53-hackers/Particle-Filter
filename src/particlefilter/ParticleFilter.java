package particlefilter;

import java.math.MathContext;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class ParticleFilter {

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
    
    public Particle[] getBestParticles() {
        Particle[] bestParticles = new Particle[numOfParticles];
        for(int i = 0; i < numOfParticles; i++) {
            
        }
        
        return bestParticles;
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
    
    public Particle getAverageParticle() {
        Particle p = new Particle(particles[0].landmarks, particles[0].width, particles[0].height);
        float x = 0, y = 0, orient = 0, prob = 0;
        for(int i=0;i<numOfParticles;i++) {
            x += particles[i].x;
            y += particles[i].y;
            orient += particles[i].orientation;
            prob += particles[i].probability;
        }
        x /= numOfParticles;
        y /= numOfParticles;
        orient /= numOfParticles;
        prob /= numOfParticles;
        try {
            p.set(x, y, orient, prob);
        } catch (Exception ex) {
            Logger.getLogger(ParticleFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        p.setNoise(particles[0].forwardNoise, particles[0].turnNoise, particles[0].senseNoise);
        
        return p;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < numOfParticles; i++) {
            res += particles[i].toString() + "\n";
        }
        return res;
    }
   
}
