package galaxy;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Matrix4d;

/*
 * Course: COMP 382
 * Assignment: 1
 * Student ID: 3240240
 * 
 * Class: EarthorbitBehavior.java
 * Author: Zakaria Bakkal, some methods are taken from the examples provided
 *          to us in this course.
 * Date: May 3, 2018
 * Version: 7.0
*/
public class EarthOrbitBehavior extends Behavior {

    private TransformGroup target;
    private WakeupOnElapsedTime AWTEventCondition;
    private Transform3D t3d;

    private double x;
    private double z;

    private final double MAX_Z = 9.0d;
    private final double RADIUS = MAX_Z;
    private final double Z_OFF_SET = 0.099d;
    private Matrix4d matrix;

    /**
     * initialize instance variables
     * 
     * @param target
     */
    public EarthOrbitBehavior(TransformGroup target) {
        // the behaviour is woken up after 100 ms. The elapsed time is used
        // as the triger for the bahavior
        this.AWTEventCondition = new WakeupOnElapsedTime(100L);
        this.target = target;
        this.t3d = new Transform3D();
        // initialize the coordinates to the original position of the earth
        this.x = 0;
        this.z = -RADIUS;
    }

    @Override
    public void initialize() {
        this.wakeupOn(this.AWTEventCondition);
    }

    /**
     * calculate the positions of the earth around the sun
     * 
     * @param enmrtn
     */
    @Override
    public void processStimulus(Enumeration enmrtn) {

        // depending on which quadrant where the earth is located, the value of
        // the z coordinate is increased by an offset. And, also the value
        // of the coordinate x is calculated by calling the method calculateX(z).
        // the value of x is dependent on the value of z. The order of the
        // quadrants is counter clockwise.
        if (z >= -RADIUS && z < 0 && x <= 0 && x > -RADIUS) {
            z += Z_OFF_SET;
            if (z > 0) {
                z = 0;
            }
            x = -calculateX(z);
        } else if (z >= 0 && z < RADIUS && x >= -RADIUS && x < 0) {
            z += Z_OFF_SET;
            if (z > RADIUS) {
                z = RADIUS;
            }
            x = -calculateX(z);
        } else if (z <= RADIUS && z > 0 && x >= 0 && x < RADIUS) {
            z -= Z_OFF_SET;
            if (z < 0) {
                z = 0;
            }
            x = calculateX(z);
        } else if (z <= 0 && z > -RADIUS && x <= RADIUS && x > 0) {
            z -= Z_OFF_SET;
            if (z < -RADIUS) {
                z = -RADIUS;
            }
            x = calculateX(z);
        }

        // create a matrix from the values calculated above. this matrix represents
        // the translation performed on the earth.
        matrix = new Matrix4d(1, 0, 0, x, 0, 1, 0, 3, 0, 0, 1, z, 0, 0, 0, 1);

        // retrieve the object's current position
        this.target.getTransform(t3d);
        // careate a transform3D with the created matrix above
        Transform3D temp = new Transform3D(matrix);

        // set the new translation to the object
        this.target.setTransform(temp);

        this.wakeupOn(this.AWTEventCondition);
    }

    /*
     * returns a double representing the x coordinates of on a circle.
     * 
     * @param double z represent the coordinates z on the circle
     */
    private double calculateX(double z) {
        x = (double) Math.sqrt(Math.pow(RADIUS, 2) - Math.pow(z, 2));
        return x;
    }
}
