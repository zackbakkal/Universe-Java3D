package galaxy;



import com.sun.j3d.utils.geometry.GeometryInfo;
import javax.media.j3d.BranchGroup;
import java.applet.Applet;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/*
 * Course: COMP 382
 * Assignment: 1
 * Student ID: 3240240
 * 
 * Class: Grid3x3.java
 * Author: Zakaria Bakkal
 * Date: Mars 10, 2018
 * Version: 7.0
*/
public class Grid3x3 extends Applet{
    
    private final Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);
    private final Color3f BLUE_1 = new Color3f(0.0f, 0.3f, 1.0f);
    private final Color3f BLUE_2 = new Color3f(0.3f, 0.0f, 1.0f);
    private final Color3f CYAN = new Color3f(0.0f, 1.0f, 1.0f);
    private final Color3f WHITE = new Color3f(1.0f, 0.9f, 0.9f);
            
    // the surface of the room coordinates
    private final Point3f[] wall = new Point3f[4];
    
    /**
     * returns a branch group object representing a box. The box dimensions are:
     * 2 meters on the x axis, from -1 to 1
     * 2 meters on the y axis, from -1 to 1
     * 2 meters on the z axis, from -5 to -3
     * @return BranchGroup
     */
    public BranchGroup createRoom() {
        BranchGroup objRoot = new BranchGroup();
        
        // holds the coordinates of a surface, the order of the culled face is
        // counter clockwise
        Point3f[] wallCoor = new Point3f[4];
        
        // coordinates of the first wall the order is counter clockwise
        Point3f first = new Point3f(-1.0f, -1.0f, -3.0f);
        Point3f second = new Point3f(1.0f, -1.0f, -3.0f);
        Point3f third = new Point3f(1.0f, -1.0f, -5.0f);
        Point3f fourth = new Point3f(-1.0f, -1.0f, -5.0f);
        
        wallCoor[0] = first;
        wallCoor[1] = second;
        wallCoor[2] = third;
        wallCoor[3] = fourth;
        
        // create a shape3d object by calling the get3x3Grids, passing a color
        // for the surface and the coordinates
        Shape3D[] floor = this.get3x3Grids(WHITE, wallCoor);
        // add each grid to the objRoot object
        for(Shape3D s: floor) {
            objRoot.addChild(s);
        }
        
        // same thing goes for each surface of the box
        first = new Point3f(1.0f, -1.0f, -5.0f);
        second = new Point3f(1.0f, -1.0f, -3.0f);
        third = new Point3f(1.0f, 1.0f, -3.0f);
        fourth = new Point3f(1.0f, 1.0f, -5.0f);
        
        wallCoor[0] = first;
        wallCoor[1] = second;
        wallCoor[2] = third;
        wallCoor[3] = fourth;
        
        Shape3D[] right = this.get3x3Grids(BLUE_1, wallCoor);
        for(Shape3D s: right) {
            objRoot.addChild(s);
        }
        
        first = new Point3f(-1.0f, -1.0f, -5.0f);
        second = new Point3f(1.0f, -1.0f, -5.0f);
        third = new Point3f(1.0f, 1.0f, -5.0f);
        fourth = new Point3f(-1.0f, 1.0f, -5.0f);
        
        wallCoor[0] = first;
        wallCoor[1] = second;
        wallCoor[2] = third;
        wallCoor[3] = fourth;
        
        Shape3D[] back = this.get3x3Grids(BLUE, wallCoor);
        for(Shape3D s: back) {
            objRoot.addChild(s);
        }
        
        first = new Point3f(-1.0f, -1.0f, -3.0f);
        second = new Point3f(-1.0f, -1.0f, -5.0f);
        third = new Point3f(-1.0f, 1.0f, -5.0f);
        fourth = new Point3f(-1.0f, 1.0f, -3.0f);
        
        wallCoor[0] = first;
        wallCoor[1] = second;
        wallCoor[2] = third;
        wallCoor[3] = fourth;
        
        Shape3D[] left = this.get3x3Grids(BLUE_2, wallCoor);
        for(Shape3D s: left) {
            objRoot.addChild(s);
        }
        
        first = new Point3f(-1.0f, 1.0f, -5.0f);
        second = new Point3f(1.0f, 1.0f, -5.0f);
        third = new Point3f(1.0f, 1.0f, -3.0f);
        fourth = new Point3f(-1.0f, 1.0f, -3.0f);
        
        wallCoor[0] = first;
        wallCoor[1] = second;
        wallCoor[2] = third;
        wallCoor[3] = fourth;
        
        Shape3D[] ceiling = this.get3x3Grids(CYAN, wallCoor);
        for(Shape3D s: ceiling) {
            objRoot.addChild(s);
        }
        
        first = new Point3f(1.0f, -1.0f, -3.0f);
        second = new Point3f(-1.0f, -1.0f, -3.0f);
        third = new Point3f(-1.0f, 1.0f, -3.0f);
        fourth = new Point3f(1.0f, 1.0f, -3.0f);
        
        wallCoor[0] = first;
        wallCoor[1] = second;
        wallCoor[2] = third;
        wallCoor[3] = fourth;
        
        Shape3D[] front = this.get3x3Grids(BLUE, wallCoor);
        for(Shape3D s: front) {
            objRoot.addChild(s);
        }
        
        
        return objRoot;
    }
    
    /**
     * returns a shape3d array, each element of the array is a small surface.
     * a big surface comprises of 9 little surfaces representing a 3x3 grids.
     * the coordinates must be given in order as counter clock wise, starting 
     * from the bottom left corner. ending at the top left corner.
     * 
     * @param color3f specifies the color of the surface
     * @param points specifies the coordinates of the big surface
     * 
     * @return Shape3D[]
     */
    public Shape3D[] get3x3Grids(Color3f color3f, Point3f... points) {
        
        // check if the number of corners provided is not 4, return null
        if(points.length != 4) {
            return null; 
        }
        
        // initialize the instancce variable wall to the values of the 
        // arguments passed
        for(int i = 0; i < points.length; i++) {
            this.wall[i] = points[i];
        }
        
        // create an array of geometry info that will hold the 9 grids of a
        // surface
        GeometryInfo[] grids = new GeometryInfo[9];
        // the object that we need to create
        Shape3D[] shapes = new Shape3D[9];
        // the index of the current grid to construct
        int gridIndex = 0;

        // we gonna use these values to make a decisison depending on which
        // surface are we constructing, left, back, right, top, bottm or front
        // surface.
        float x = this.wall[0].x;
        float y = this.wall[0].y;
        float z = this.wall[0].z;
        
        // the side length of a grid, which is two third the actual surface
        float gridLength = 2.0f / 3.0f;
        // holds the coordinates of each point in the surface that will create
        // the 3x3 grids.
        // 12 13  14  15
        //  8  9  10  11
        //  4  5   6   7
        //  0  1   2   3
        Point3f[] pointsCoordinates = new Point3f[16];
        
        // now, since the coordinate are giving in conter clockwise, we check to 
        // see which surface are we to construct. it could be a left side, back
        // front or any other side of the box.
        // depending on the coordinates giving for the surface, we start
        // by the first point (x, y, z) and keep going adding or subtracting
        // the value of the gridLength variable to an axis.
        if(this.wall[0].x < this.wall[2].x 
                && this.wall[0].z > this.wall[2].z
                && this.wall[0].y == this.wall[2].y) {
            pointsCoordinates[0] = new Point3f(x, y, z);
            pointsCoordinates[4] = new Point3f(x, y, z - gridLength);
            pointsCoordinates[8] = new Point3f(x, y, z - 2*gridLength);
            pointsCoordinates[12] = new Point3f(x, y, z - 3*gridLength);
            
            pointsCoordinates[1] = new Point3f(x + gridLength, y, z);
            pointsCoordinates[2] = new Point3f(x + 2*gridLength, y, z);
            pointsCoordinates[3] = new Point3f(x + 3*gridLength, y, z);
            
            pointsCoordinates[5] = new Point3f(x + gridLength, y, z - gridLength);
            pointsCoordinates[6] = new Point3f(x + 2*gridLength, y, z - gridLength);
            pointsCoordinates[7] = new Point3f(x + 3*gridLength, y, z - gridLength);
            
            pointsCoordinates[9] = new Point3f(x + gridLength, y, z - 2*gridLength);
            pointsCoordinates[10] = new Point3f(x + 2*gridLength, y, z - 2*gridLength);
            pointsCoordinates[11] = new Point3f(x + 3*gridLength, y, z - 2*gridLength);
            
            pointsCoordinates[13] = new Point3f(x + gridLength, y, z - 3*gridLength);
            pointsCoordinates[14] = new Point3f(x + 2*gridLength, y, z - 3*gridLength);
            pointsCoordinates[15] = new Point3f(x + 3*gridLength, y, z - 3*gridLength);
        } else { 
            if(this.wall[0].x < this.wall[2].x 
                && this.wall[0].z < this.wall[2].z
                && this.wall[0].y == this.wall[2].y){
                pointsCoordinates[0] = new Point3f(x, y, z);
                pointsCoordinates[4] = new Point3f(x, y, z + gridLength);
                pointsCoordinates[8] = new Point3f(x, y, z + 2*gridLength);
                pointsCoordinates[12] = new Point3f(x, y, z + 3*gridLength);

                pointsCoordinates[1] = new Point3f(x + gridLength, y, z);
                pointsCoordinates[2] = new Point3f(x + 2*gridLength, y, z);
                pointsCoordinates[3] = new Point3f(x + 3*gridLength, y, z);

                pointsCoordinates[5] = new Point3f(x + gridLength, y, z + gridLength);
                pointsCoordinates[6] = new Point3f(x + 2*gridLength, y, z + gridLength);
                pointsCoordinates[7] = new Point3f(x + 3*gridLength, y, z + gridLength);

                pointsCoordinates[9] = new Point3f(x + gridLength, y, z + 2*gridLength);
                pointsCoordinates[10] = new Point3f(x + 2*gridLength, y, z + 2*gridLength);
                pointsCoordinates[11] = new Point3f(x + 3*gridLength, y, z + 2*gridLength);

                pointsCoordinates[13] = new Point3f(x + gridLength, y, z + 3*gridLength);
                pointsCoordinates[14] = new Point3f(x + 2*gridLength, y, z + 3*gridLength);
                pointsCoordinates[15] = new Point3f(x + 3*gridLength, y, z + 3*gridLength);
            } else {
                if(this.wall[0].x < this.wall[2].x
                        && this.wall[0].y < this.wall[2].y
                        && this.wall[0].z == this.wall[2].z) {
                    pointsCoordinates[0] = new Point3f(x, y, z);
                    pointsCoordinates[4] = new Point3f(x, y + gridLength, z);
                    pointsCoordinates[8] = new Point3f(x, y + 2*gridLength, z);
                    pointsCoordinates[12] = new Point3f(x, y + 3*gridLength, z);

                    pointsCoordinates[1] = new Point3f(x + gridLength, y, z);
                    pointsCoordinates[2] = new Point3f(x + 2*gridLength, y, z);
                    pointsCoordinates[3] = new Point3f(x + 3*gridLength, y, z);

                    pointsCoordinates[5] = new Point3f(x + gridLength, y + gridLength, z);
                    pointsCoordinates[6] = new Point3f(x + 2*gridLength, y + gridLength, z);
                    pointsCoordinates[7] = new Point3f(x + 3*gridLength, y + gridLength, z);

                    pointsCoordinates[9] = new Point3f(x + gridLength, y + 2*gridLength, z);
                    pointsCoordinates[10] = new Point3f(x + 2*gridLength, y + 2*gridLength, z);
                    pointsCoordinates[11] = new Point3f(x + 3*gridLength, y + 2*gridLength, z);

                    pointsCoordinates[13] = new Point3f(x + gridLength, y + 3*gridLength, z);
                    pointsCoordinates[14] = new Point3f(x + 2*gridLength, y + 3*gridLength, z);
                    pointsCoordinates[15] = new Point3f(x + 3*gridLength, y + 3*gridLength, z); 
                } else {
                    if(this.wall[0].z < this.wall[2].z
                        && this.wall[0].y < this.wall[2].y
                        && this.wall[0].x == this.wall[2].x) {
                        pointsCoordinates[0] = new Point3f(x, y, z);
                        pointsCoordinates[4] = new Point3f(x, y + gridLength, z);
                        pointsCoordinates[8] = new Point3f(x, y + 2*gridLength, z);
                        pointsCoordinates[12] = new Point3f(x, y + 3*gridLength, z);

                        pointsCoordinates[1] = new Point3f(x, y, z + gridLength);
                        pointsCoordinates[2] = new Point3f(x, y, z + 2*gridLength);
                        pointsCoordinates[3] = new Point3f(x, y, z + 3*gridLength);

                        pointsCoordinates[5] = new Point3f(x, y + gridLength, z + gridLength);
                        pointsCoordinates[6] = new Point3f(x, y + gridLength, z + 2*gridLength);
                        pointsCoordinates[7] = new Point3f(x, y + gridLength, z + 3*gridLength);

                        pointsCoordinates[9] = new Point3f(x, y + 2*gridLength, z + gridLength);
                        pointsCoordinates[10] = new Point3f(x, y + 2*gridLength, z + 2*gridLength);
                        pointsCoordinates[11] = new Point3f(x, y + 2*gridLength, z + 3*gridLength);

                        pointsCoordinates[13] = new Point3f(x, y + 3*gridLength, z + gridLength);
                        pointsCoordinates[14] = new Point3f(x, y + 3*gridLength, z + 2*gridLength);
                        pointsCoordinates[15] = new Point3f(x, y + 3*gridLength, z + 3*gridLength);
                    } else {
                        if(this.wall[0].z > this.wall[2].z
                            && this.wall[0].y < this.wall[2].y
                            && this.wall[0].x == this.wall[2].x) {
                            pointsCoordinates[0] = new Point3f(x, y, z);
                            pointsCoordinates[4] = new Point3f(x, y + gridLength, z);
                            pointsCoordinates[8] = new Point3f(x, y + 2*gridLength, z);
                            pointsCoordinates[12] = new Point3f(x, y + 3*gridLength, z);

                            pointsCoordinates[1] = new Point3f(x, y, z - gridLength);
                            pointsCoordinates[2] = new Point3f(x, y, z - 2*gridLength);
                            pointsCoordinates[3] = new Point3f(x, y, z - 3*gridLength);

                            pointsCoordinates[5] = new Point3f(x, y + gridLength, z - gridLength);
                            pointsCoordinates[6] = new Point3f(x, y + gridLength, z - 2*gridLength);
                            pointsCoordinates[7] = new Point3f(x, y + gridLength, z - 3*gridLength);

                            pointsCoordinates[9] = new Point3f(x, y + 2*gridLength, z - gridLength);
                            pointsCoordinates[10] = new Point3f(x, y + 2*gridLength, z - 2*gridLength);
                            pointsCoordinates[11] = new Point3f(x, y + 2*gridLength, z - 3*gridLength);

                            pointsCoordinates[13] = new Point3f(x, y + 3*gridLength, z - gridLength);
                            pointsCoordinates[14] = new Point3f(x, y + 3*gridLength, z - 2*gridLength);
                            pointsCoordinates[15] = new Point3f(x, y + 3*gridLength, z - 3*gridLength);
                        } else {
                            pointsCoordinates[0] = new Point3f(x, y, z);
                            pointsCoordinates[4] = new Point3f(x, y + gridLength, z);
                            pointsCoordinates[8] = new Point3f(x, y + 2*gridLength, z);
                            pointsCoordinates[12] = new Point3f(x, y + 3*gridLength, z);

                            pointsCoordinates[1] = new Point3f(x - gridLength, y, z);
                            pointsCoordinates[2] = new Point3f(x - 2*gridLength, y, z);
                            pointsCoordinates[3] = new Point3f(x - 3*gridLength, y, z);

                            pointsCoordinates[5] = new Point3f(x - gridLength, y + gridLength, z);
                            pointsCoordinates[6] = new Point3f(x - 2*gridLength, y + gridLength, z);
                            pointsCoordinates[7] = new Point3f(x - 3*gridLength, y + gridLength, z);

                            pointsCoordinates[9] = new Point3f(x - gridLength, y + 2*gridLength, z);
                            pointsCoordinates[10] = new Point3f(x - 2*gridLength, y + 2*gridLength, z);
                            pointsCoordinates[11] = new Point3f(x - 3*gridLength, y + 2*gridLength, z);

                            pointsCoordinates[13] = new Point3f(x - gridLength, y + 3*gridLength, z);
                            pointsCoordinates[14] = new Point3f(x - 2*gridLength, y + 3*gridLength, z);
                            pointsCoordinates[15] = new Point3f(x - 3*gridLength, y + 3*gridLength, z);
                        }
                    }
                }
                
            }
        }
        
        // this is a control variable to check if we are at grid 2 or grid 6
        int j = 0;
        // now we construct the 9 grids from the coordinates that we
        // calculated earlier
        for(int i = 0; i < 9; i ++) {
            
            if(j == 3 || j == 7) {
                j++;
            }
            
            // the coordinates of each grid
            Point3f[] gridPoints = new Point3f[4];
            gridPoints[0] = pointsCoordinates[j];
            gridPoints[1] = pointsCoordinates[j+1];
            gridPoints[2] = pointsCoordinates[j+5];
            gridPoints[3] = pointsCoordinates[j+4];
            
            shapes[gridIndex++] = new Surface().createSurface(color3f, gridPoints);
            
            j++;
        }
        
        return shapes;
    }
    
}
