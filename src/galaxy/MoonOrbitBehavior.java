package galaxy;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Matrix4d;

/*
 * Course: COMP 382
 * Assignment: 2
 * Student ID: 3240240
 * 
 * Class: MoonOrbitBehavior.java
 * Author: Zakaria Bakkal, some methods are taken from the examples provided
 *          to us in this course.
 * Date: May 5, 2018
 * Version: 3.0
*/
public class MoonOrbitBehavior extends Behavior{

    private TransformGroup target;
    private WakeupOnElapsedTime AWTEventCondition;
    private Transform3D t3d;
    private double x;
    private double z;
    private final double MAX_Z = -8.0d;
    private final double MIN_Z = -10.0d;
    private final double MAX_X = 1.0d;
    private final double MIN_X = -1.0d;
    private final double CENTRE = -9.0d;
    private final double RADIUS = 1.0f ;
    private final double Z_OFF_SET = 0.11116d;
    private Matrix4d matrix;
    
    /**
     * initialize instance variables
     * 
     * @param target
     */
    public MoonOrbitBehavior(TransformGroup target) {
        // the behaviour is woken up after 100 ms. The elapsed time is used
        // as the triger for the bahavior
        this.AWTEventCondition = new WakeupOnElapsedTime(100L);
        this.target = target;
        this.t3d = new Transform3D();
        // initialize the coordinates to the original position of the moon
        this.x = 0.0d;
        this.z = MIN_Z;
    }
    
    @Override
    public void initialize() {
        this.wakeupOn(this.AWTEventCondition);
    }

    /**
     * calculate the positions of the moon around the earth
     * @param enmrtn
     */
    @Override
    public void processStimulus(Enumeration enmrtn) {
        
        if(z >= MIN_Z && z < CENTRE && x <= 0 && x > MIN_X) {
            z += Z_OFF_SET;
            if(z > CENTRE) { z = CENTRE; }
            x = -calculateX(z);
        } else if(z >= CENTRE && z < MAX_Z && x >= MIN_X && x < 0) {
            z += Z_OFF_SET;
            if(z > MAX_Z) { z = MAX_Z; }
            x = -calculateX(z);
        } else if(z <= MAX_Z && z > CENTRE && x >= 0 && x < MAX_X) {
            z -= Z_OFF_SET;
            if(z < CENTRE) { z = CENTRE; }
            x = calculateX(z);
        } else if(z <= CENTRE && z > MIN_Z && x <= MAX_X && x > 0) {
            z -= Z_OFF_SET;
            if(z < MIN_Z) { z = MIN_Z; }
            x = calculateX(z);
        }
        
        // create a matrix from the values calculated above. this matrix represents
        // the translation performed on the moon.
        matrix = new Matrix4d(1, 0, 0, x, 0, 1, 0, 3, 0, 0, 1, z, 0, 0, 0, 1);
        
        // retrieve the object's current position
        this.target.getTransform(t3d);
        // careate a transform3D with the created matrix above
        Transform3D temp = new Transform3D(matrix);

        // set the new translation to the object
        this.target.setTransform(temp);
        
        this.wakeupOn(AWTEventCondition);
    }
    
     /*
     * returns a double representing the x coordinates of on a circle.
    
     * @param double z represent the coordinates z on the circle
    */
    private double calculateX(double z) {
        x = (double) Math.sqrt(Math.pow(RADIUS, 2) - Math.pow(z + 9, 2));
        return x;
    }
    
}
