package galaxy;



import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/*
 * Course: COMP 382
 * Assignment: 1
 * Student ID: 3240240
 * 
 * Class: Surface.java
 * Author: Zakaria Bakkal
 * Date: Mars 12, 2018
 * Version: 5.0
*/
public class Surface extends Shape3D{
    
    /**
     * creates a surface from the points provided in the parameters, with
     * the color provided as the first parameter. if the number of points is
     * not equal to 4 the method returns null.
     * 
     * @param color3f
     * @param points
     * @return Shape3D
     */
    public Shape3D createSurface(Color3f color3f, Point3f... points) {
        
        // return null when the provided points number
        // is not 4
        if(points.length != 4) {
            return null;
        }
        
        // represents the coordinatipons of the four corners of the surface
        Point3f[] wall1Coor = new Point3f[4];
        
        // initilize the points with the value of the parameters
        for(int i = 0; i < points.length; i++) {
            wall1Coor[i] = points[i];
        }
        
        // instantiate an object of type GeometryInfo with polygon array
        GeometryInfo wallGeo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        // pass the coordinates tot he Geometry object
        wallGeo.setCoordinates(wall1Coor);
        // represents the strip count in the Geometry object
        int[] stripCount = new int[1];
        // number of strips is 4
        stripCount[0] = 4;
        // set the strip count of the GeometryInfo object to 4
        wallGeo.setStripCounts(stripCount);
        
        // generate normal for the wall
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(wallGeo);
        
        // stripify
        Stripifier st = new Stripifier();
        st.stripify(wallGeo);
        
        // give the wall appearance color
        Appearance wallApp = new Appearance();
        ColoringAttributes color = new ColoringAttributes();
        color.setColor(color3f);
        wallApp.setColoringAttributes(color);
        
        // create a polygoneattribute aboject to cull the front of the wall
        PolygonAttributes polyAtt = new PolygonAttributes();
        polyAtt.setPolygonMode(PolygonAttributes.CULL_FRONT);
        wallApp.setPolygonAttributes(polyAtt);
        
        // the final wall is a Shape3D object that has the appearance wallapp
        // and the geometry of the GeometryInfo object
        Shape3D wall = new Shape3D();
        wall.setAppearance(wallApp);
        wall.setGeometry(wallGeo.getGeometryArray());
        
        return wall;
    }
    
    
}

