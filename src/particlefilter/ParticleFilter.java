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
    public static void main(String[] args) {
        Random gen = new Random();
        for(int i=0;i<200;i++) {
        System.out.println(gen.nextGaussian() * .5);
        }
        
        // TODO code application logic here
    }
}
