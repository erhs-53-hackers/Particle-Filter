package particlefilter;

import java.util.Random;

/**
 *
 * @author nick
 */
public class Robot extends Particle {

    public Robot(Point[] landmarks, int width, int height) {
        super(landmarks, width, height);
    }
 
    public float[] sense() {
        float[] measurements = new float[landmarks.length];
        
        for (int i = 0; i < landmarks.length; i++) {
            double dist = Robot.distance(x, y, landmarks[i].x, landmarks[i].y);
            dist += gen.nextGaussian();
            measurements[i] = (float) dist;
        }
        
        return measurements;
    } 
    
}
