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
    public static void main(String[] args) throws Exception {
        
       
        int worldSize = 10;
        int N = 5;
        Particle[] parts = new Particle[N];
        Point[] landmarks = new Point[] {new Point(0, 0), new Point(100,100)};
        for(int i=0;i<N;i++) {
            parts[i] = new Particle(landmarks, worldSize);
            parts[i].setNoise(.00001f, .000001f, .000001f);            
        }
        
        for(Particle p : parts) {
            System.out.println(p.toString());
        }
        
        for(int i=0;i<N;i++) {
            parts[i].move(0f, 5f);
        }
        System.out.println("-------------------------");
        
        for(Particle p : parts) {
            System.out.println(p.toString());
        }
        
        
    }
}
