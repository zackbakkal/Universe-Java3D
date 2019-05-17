package galaxy;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/*
 * Course: COMP 382
 * Assignment: 1
 * Student ID: 3240240
 * 
 * Class: UniverseModified.java
 * Author: Zakaria Bakkal, some methods are taken from the examples provided
 *          to us in this course.
 * Date: Mars 25, 2018
 * Version: 3.0
*/
public class Universe extends Applet {

    private SimpleUniverse simpleU;
    private BoundingSphere bounds;
    private BranchGroup scene;
    private TransformGroup floor;
    private TransformGroup sun;
    private TransformGroup earth;
    private TransformGroup moon;
    private TransformGroup backGround;
    private TransformGroup xLabels;
    private TransformGroup zLabels;
    private TransformGroup axisLabels;
    private ViewingPlatform vp;

    // colors
    private final Point3d CENTRE = new Point3d(0, 0, 0);
    private final Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);
    private final Color3f GREEN = new Color3f(0.5f, 1.0f, 0.5f);
    private final Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);
    private final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);
    private final Color3f RED = new Color3f(1.0f, 0.5f, 0.3f);

    // used for the viewing platform for the starting user position and
    // and th epoint the user will be looking at
    private final Point3d POINT_TO = new Point3d(0, 3.0d, -7.5d);
    private final Point3d USERPOS = new Point3d(0.0d, 20.0d, 5.0d);

    /**
     * 
     */
    public Universe() {

        this.setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        // SimpleUniverse is a Convenience Utility class
        simpleU = new SimpleUniverse(canvas3D);

        bounds = new BoundingSphere(CENTRE, 10000000.0);
        this.scene = createSceneGraph();

        // initialize the user view point
        this.initUserPosition();

        this.orbitControls(canvas3D);

        simpleU.addBranchGraph(scene);
    }

    /**
     * creates the scene graph
     * 
     * @return BranchGroup
     */
    public BranchGroup createSceneGraph() {
        this.scene = new BranchGroup();

        // create each object of the scene
        TransformGroup backGround = this.addBackground();
        TransformGroup floor = this.createFloor();
        TransformGroup sun = this.sun(new Point3f(0.0f, 3.0f, -5.15f));
        TransformGroup earthMoon = this.earthMoon(new Point3f(0.0f, 3.0f, -9.0f));
        TransformGroup sunLight = this.sunLight();
        TransformGroup axis = this.axisLabels();

        // add the objects to the scene
        this.scene.addChild(backGround);
        this.scene.addChild(floor);
        this.scene.addChild(sun);
        // adds earth and moon transformgroups to one transformgroup
        this.scene.addChild(earthMoon);
        this.scene.addChild(sunLight);
        this.scene.addChild(axis);

        return this.scene;
    }

    /**
     * creates the floor of this scene.
     * 
     * @return TransformGroup
     */
    public TransformGroup createFloor() {

        this.floor = new TransformGroup();

        // represent a tile in the surface
        Surface tile = new Surface();

        // starting point coordinates
        float x = -40.0f;
        float y = 0.0f;
        float z = 20.0f;

        // the length of each tile
        float offset = 4.0f;

        // these point3f objects hold the coordinates of each corner of the tile
        Point3f first;
        Point3f second;
        Point3f third;
        Point3f fourth;

        // control variable to alternate between blue and green colors
        int j = 1;

        // construct 400 tiles making the floor
        for (int i = 1; i <= 800; i++) {

            // initialize the coordinates of each corner of the tile
            first = new Point3f(x, y, z);
            second = new Point3f(x + offset, y, z);
            third = new Point3f(x + offset, y, z - offset);
            fourth = new Point3f(x, y, z - offset);

            // create the tiles of the floor
            Point3f[] points = { first, second, third, fourth };
            if (j % 2 == 0) {
                if (i % 2 == 0) {
                    floor.addChild(tile.createSurface(BLUE, points));
                } else {
                    floor.addChild(tile.createSurface(GREEN, points));
                }
            } else {
                if (i % 2 == 0) {
                    floor.addChild(tile.createSurface(GREEN, points));
                } else {
                    floor.addChild(tile.createSurface(BLUE, points));
                }
            }

            // each time we reach 20 tiles in a row we add a second row
            if (i % 20 == 0) {
                // reset the coordinates of the first corner of the second row
                z -= offset;
                x = -40.0f;
                j++;
            } else {
                // we are still in the same row so we just add the length of
                // the tile to the x coordinate
                x += offset;
            }

        }

        // set the bounds of the floor
        this.floor.setBounds(bounds);
        // add the marker of the origin, its a red tile
        this.addOriginMarker();

        return this.floor;

    }

    /**
     * returns a transform group object that represents the axis labels
     * 
     * @return TransformGroup
     */
    public TransformGroup axisLabels() {

        Transform3D xTranslate; // transform object for x axis
        Transform3D zTranslate; // transform object for z axis
        // labels for the x axis
        this.xLabels = new TransformGroup();
        // labels for the z axis
        this.zLabels = new TransformGroup();
        // labels for both axises
        this.axisLabels = new TransformGroup();

        for (int i = -10; i < 20; i++) {
            // represents the label coordinates x and z
            int j = i * 2;
            // each text2d abject is made for each label
            Text2D xAxis = new Text2D(String.valueOf(i), BLACK, "Helvetica", 20, Font.PLAIN);
            Text2D zAxis = new Text2D(String.valueOf(i), BLACK, "Helvetica", 20, Font.PLAIN);

            // the label is translated to its corresponding position
            Vector3f x = new Vector3f(j, 0.01f, 0);
            Vector3f z = new Vector3f(0, 0.01f, j);
            xTranslate = new Transform3D();
            zTranslate = new Transform3D();

            xTranslate.setTranslation(x);
            zTranslate.setTranslation(z);

            // create transform groups from each label. each transform group is
            // translated according the translate object created above
            TransformGroup xL = new TransformGroup(xTranslate);
            xL.addChild(xAxis);
            TransformGroup zL = new TransformGroup(zTranslate);
            zL.addChild(zAxis);

            // add the childs to the transform groups xLabels and zLabels
            this.xLabels.addChild(xL);
            this.zLabels.addChild(zL);

        }
        // add the axis labels object to the transform group axis labels
        this.axisLabels.addChild(xLabels);
        this.axisLabels.addChild(zLabels);
        // set the bounds for the axis labels object
        this.axisLabels.setBounds(bounds);

        return this.axisLabels;
    }

    /**
     * returns a transform group object that represent the sun that is translated to
     * the coordinates of the point3f object in the argument
     * 
     * @param position where the sun is positioned in the scene
     * @return TransformGroup
     */
    public TransformGroup sun(Point3f position) {

        // the vector used to translate the sun using the argument position
        Vector3f vector = new Vector3f(position);

        // this object is used to set the translation vector for the
        // transform group
        Transform3D translate = new Transform3D();
        translate.setTranslation(vector);

        // instantiate the instance variable sun to the a new transform group
        // using the translation created above
        this.sun = new TransformGroup(translate);

        // texture file name
        String fileName = "sun.jpg";

        // texture object used to give the sun a texture
        Texture tex = new Texture() {
        };
        try {
            // load the texture file
            tex = new TextureLoader(ImageIO.read(getClass().getResource(fileName))).getTexture();
            // set the boundry mode for the S and T coordinates
            // in this texture object to WRAP, which repeats the texture
            // by wrapping texture coordinates that are outside the range [0,1]
            tex.setBoundaryModeS(Texture.WRAP);
            tex.setBoundaryModeT(Texture.WRAP);
        } catch (IOException ex) {
            System.out.println("Could not load file:\n " + fileName);
            System.out.println("Error: " + ex);
        }

        // use a texture attribute object to define how the object and texture
        // colors are blended by setting its texture mode. I used mode COMBINE
        // so that the sun has a texture and colors such as emissive color,
        // diffuse color...
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);

        // use this material object to set the sun's colors. I made the sun
        // the source of the light
        Material mat = new Material();
        mat.setEmissiveColor(WHITE);
        mat.setDiffuseColor(WHITE);
        mat.setSpecularColor(WHITE);
        mat.setShininess(20);
        mat.setLightingEnable(true);

        // here we use the appearance to pass it the texture, texture attributes,
        // material objects made above and apply it to the sun object
        Appearance sunApp = new Appearance();
        sunApp.setMaterial(mat);
        sunApp.setTexture(tex);
        sunApp.setTextureAttributes(texAttr);

        // here we initialize the flags used to generate normals for the
        // sphere that will represent the physical object in the scene
        int flags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        // this divisions variable represents how many divisions makes the sun.
        // this actually makes the sphere looks smoother
        int divisions = 1000;

        // instantiating a sphere object that represents the sun with the
        // arguments made above
        Sphere sun = new Sphere(1.5f, flags, divisions, sunApp);
        this.sun.addChild(sun);

        // set the bounds for the sun transform group
        this.sun.setBounds(bounds);
        return this.sun;
    }

    /**
     * returns a transform group object that represent the earth that is translated
     * to the coordinates of the point3f object in the argument
     * 
     * @param position where the earth is positioned in the scene
     * @return TransformGroup
     */
    public TransformGroup earth(Point3f position) {

        // same logic used for the sun goes here
        Vector3f vector = new Vector3f(position);

        Transform3D translate = new Transform3D();
        translate.setTranslation(vector);

        this.earth = new TransformGroup(translate);

        // add read and write capabilities to the earth object
        earth.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        earth.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // this object is used to spin the earth around its axis
        TransformGroup earthSpin = new TransformGroup();
        earthSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        earthSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        this.earth.addChild(earthSpin);

        // use the yaxis as the spin axis for the earth
        Transform3D yAxis = new Transform3D();
        // use an alpha object with -1 for unfinite apining and
        // each spin takes 300 ms, which represents one day
        Alpha oneDay = new Alpha(-1, 300);

        // now the rotation interpolator will take the alpha, yaxis, earth, objects
        // and a rotation angle of 2 pi.
        RotationInterpolator earthRotator = new RotationInterpolator(oneDay, earthSpin, yAxis, 0.0f,
                (float) Math.PI * 2.0f);

        // set the scheduling bounds and add the interpolator to the earthSpin
        // transform group
        earthRotator.setSchedulingBounds(bounds);
        earthSpin.addChild(earthRotator);

        // texture file name
        String fileName = "earth.jpg";

        // apply texture to the earth
        Texture tex = new Texture() {
        };
        try {
            tex = new TextureLoader(ImageIO.read(getClass().getResource(fileName))).getTexture();
            tex.setBoundaryModeS(Texture.WRAP);
            tex.setBoundaryModeT(Texture.WRAP);
        } catch (IOException ex) {
            System.out.println("Could not load file:\n " + fileName);
            System.out.println("Error: " + ex);
        }

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);

        Material mat = new Material();
        mat.setLightingEnable(true);

        Appearance earthApp = new Appearance();
        earthApp.setTexture(tex);
        earthApp.setTextureAttributes(texAttr);
        earthApp.setMaterial(mat);

        int flags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        int divisions = 100;

        Sphere earth = new Sphere(0.5f, flags, divisions, earthApp);
        earthSpin.addChild(earth);

        this.earth.setBounds(bounds);

        return this.earth;
    }

    /**
     * returns a transform group object that represent the moon that is translated
     * to the coordinates of the point3f object in the argument
     * 
     * @param position where the moon is positioned in the scene
     * @return TransformGroup
     */
    public TransformGroup moon(Point3f position) {

        // same logic used for the earth goes here
        Vector3f vector = new Vector3f(position);

        Transform3D translate = new Transform3D();
        translate.setTranslation(vector);

        this.moon = new TransformGroup(translate);

        this.moon.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.moon.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // texture file name
        String fileName = "moon.jpg";

        Texture tex = new Texture() {
        };
        try {
            tex = new TextureLoader(ImageIO.read(getClass().getResource(fileName))).getTexture();
            tex.setBoundaryModeS(Texture.WRAP);
            tex.setBoundaryModeT(Texture.WRAP);
        } catch (IOException ex) {
            System.out.println("Could not load file:\n " + fileName);
            System.out.println("Error: " + ex);
        }

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.COMBINE);

        Material mat = new Material();

        mat.setLightingEnable(true);

        Appearance moonApp = new Appearance();
        moonApp.setTexture(tex);
        moonApp.setTextureAttributes(texAttr);
        moonApp.setMaterial(mat);

        int flags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        int divisions = 100;

        Sphere moon = new Sphere(0.25f, flags, divisions, moonApp);
        this.moon.addChild(moon);

        // the moon orbit is the path the moon takes around the sun
        MoonOrbitBehavior moonOrbit = new MoonOrbitBehavior(this.moon);
        moonOrbit.setSchedulingBounds(bounds);
        this.scene.addChild(moonOrbit);

        this.moon.setBounds(bounds);

        return this.moon;
    }

    /**
     *
     * @param position the starting position of the earth and moon
     * @return TransformGroup representing the earth and moon
     */
    public TransformGroup earthMoon(Point3f position) {

        TransformGroup earthMoon = new TransformGroup();
        earthMoon.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        earthMoon.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // earth position is the same as the one in the argument
        TransformGroup earth = this.earth(position);

        // the moon position is off by 1 unit
        float z = position.getZ();
        z += -1.0f;
        position.z = z;
        TransformGroup moon = this.moon(position);

        // add the transformgroups to one parent
        earthMoon.addChild(earth);
        earthMoon.addChild(moon);
        earthMoon.setBounds(bounds);

        // the earth orbit is applied to both the earth and moon
        // while the moon is performing its own orbit
        EarthOrbitBehavior earthOrbit = new EarthOrbitBehavior(earthMoon);
        earthOrbit.setSchedulingBounds(bounds);
        this.scene.addChild(earthOrbit);

        return earthMoon;
    }

    /**
     * initializes the user position to the value of USERPOS instance variable and
     * points to the POINT_TO instance variable value
     * 
     */
    public void initUserPosition() {

        // retrieve the viewing platform
        this.vp = simpleU.getViewingPlatform();
        // get the transform of the viewing platform so that we can
        // modify it
        TransformGroup camera = this.vp.getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        // set the transform3D object to the value of the camera transform
        camera.getTransform(t3d);
        // position the camera at USERPOS, pointing at POINT_TO which makes
        // a 45 degree down.
        t3d.lookAt(USERPOS, POINT_TO, new Vector3d(0.0f, 1.0f, 0.0f));
        t3d.invert();
        camera.setTransform(t3d);

    }

    /**
     * returns a transform group representing the sunlight
     * 
     * @return TransformGroup
     */
    public TransformGroup sunLight() {

        TransformGroup tg = new TransformGroup();
        // used a spot light to represent the sun light
        SpotLight sunLight = new SpotLight();
        // set the bounds of the light
        sunLight.setBounds(bounds);
        // set the influencing bounds of the light
        sunLight.setInfluencingBounds(bounds);
        // position the light in the centre of the sun
        sunLight.setPosition(new Point3f(0.0f, 5.0f, 0.0f));
        // the angle of the sun light is 360 degrees
        sunLight.setSpreadAngle(360.0f);
        // the concentration of the light is 10 units
        sunLight.setConcentration(10);
        // enable the light
        sunLight.setEnable(true);

        tg.addChild(sunLight);

        return tg;
    }

    /*
     * OrbitBehaviour allows the user to rotate around the scene, and to zoom in and
     * out.
     */
    private void orbitControls(Canvas3D c) {
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = this.simpleU.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    /*
     * A red square centered at (0,0,0), of length 0.5 points created
     * counter-clockwise, a bit above the floor
     */
    private void addOriginMarker() {
        Point3f[] coordinates = new Point3f[4];
        Point3f p1 = new Point3f(-0.25f, 0.01f, 0.25f);
        Point3f p2 = new Point3f(0.25f, 0.01f, 0.25f);
        Point3f p3 = new Point3f(0.25f, 0.01f, -0.25f);
        Point3f p4 = new Point3f(-0.25f, 0.01f, -0.25f);

        coordinates[0] = p1;
        coordinates[1] = p2;
        coordinates[2] = p3;
        coordinates[3] = p4;

        Surface centreTile = new Surface();
        this.scene.addChild(centreTile.createSurface(RED, coordinates));
    }

    /*
     * returns a transform group representing the stars in the sky
     *
     * @return Transform group
     */
    private TransformGroup addBackground() {
        // the background used in this scene is a sphere with a texture taken
        // from a file called stars.jpg
        // the sphere is rendered inwards
        this.backGround = new TransformGroup();

        // texture file name
        String fileName = "stars.jpg";
        // load the texture file and set it to the texture object
        Texture tex = new Texture() {
        };
        try {
            tex = new TextureLoader(ImageIO.read(getClass().getResource(fileName))).getTexture();
            tex.setBoundaryModeS(Texture.WRAP);
            tex.setBoundaryModeT(Texture.WRAP);
        } catch (IOException ex) {
            System.out.println("Could not load file:\n " + fileName);
            System.out.println("Error: " + ex);
        }

        // set the texture mode to replace, so we are using only the texture
        // taken from the file
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.REPLACE);

        // the appearance of the stars transform group will have the texture
        // created above with the texture attributes too
        Appearance starsApp = new Appearance();
        starsApp.setTexture(tex);
        starsApp.setTextureAttributes(texAttr);

        // flags for the normals generated, texture coordinates and inward
        // rendering of the sphere
        int flags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS + Primitive.GENERATE_NORMALS_INWARD;
        // makes the sphere look smoother
        int divisions = 1000;

        // create the sphere with the flags cretaed above and appearance
        Sphere stars = new Sphere(30f, flags, divisions, starsApp);
        this.backGround.addChild(stars);

        this.backGround.setBounds(bounds);

        return this.backGround;
    } // end of addBackground()

    public static void main(String[] args) {
        Frame frame = new MainFrame(new Universe(), 1000, 1500);
    }

}
