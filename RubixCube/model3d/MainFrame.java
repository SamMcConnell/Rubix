package RubixCube.model3d;

import RubixCube.model3d.exceptions.NullMainFrameException;
import RubixCube.module.RubeCube;
import RubixCube.module.exceptions.NonPositiveSizeException;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static RubixCube.model3d.Dimension.*;

/**
 *  The class that contains the 3D model of the cube
 */
public class MainFrame {
    private RubeCube cube;
    private List<List<List<Tile>>> cube3D;
    private static MainFrame mainFrame;
    private BranchGroup group;

    private MainFrame(RubeCube cube) {
        cube3D = new ArrayList<List<List<Tile>>>();
        group = new BranchGroup();
        this.cube = cube;
        //generateCube();

        Tile tile = new Tile(Z, new Point3f(0f, 0f, 0f));
        group.addChild(tile);

        Sphere sphere = new Sphere(.5f);
        group.addChild(sphere);

        Color3f light1Color = new Color3f(1.0f, 0.5f, 1.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        group.addChild(light1);


        SimpleUniverse universe = new SimpleUniverse();
        universe.addBranchGraph(group);
        universe.getViewingPlatform().setNominalViewingTransform();
        // Get rotatable cube on screen
    }

    public static MainFrame getInstance() throws NullMainFrameException {
        if (mainFrame == null) {
            throw new NullMainFrameException();
        }
        return mainFrame;
    }

    public static MainFrame getInstance(RubeCube aCube) {
        if (mainFrame == null) {
            mainFrame = new MainFrame(aCube);
        }
        return mainFrame;
    }

    /**
     * Generate QuadArrays (funky name for planes) required for each face centered at the origin
     */
    private void generateCube() {
        int size = cube.getSize();
        cube3D.add(generateDim(size, Z, true));
        cube3D.add(generateDim(size, Y, true));
        cube3D.add(generateDim(size, Z, false));
        cube3D.add(generateDim(size, Y, false));
        cube3D.add(generateDim(size, X, false));
        cube3D.add(generateDim(size, X, true));
    }

    /**
     * Component keys: X = 0; Y = 1; Z = 2
     * keys[0] - the coordinate component key for the axis of interest
     * keys[1] - the coordinate component key for one of the other two axes
     * keys[2] - the last coordinate component key for the remaining axis
     * @param axis - the dimension that we would like to generate keys for
     */

    // TODO: Update keys[1] and keys[2] so that they always point to rows and columns respectively
    private int[] getKeys(Dimension axis) {
        int[] keys = new int[3];
        switch (axis) {
            case X:
                keys[0] = 0;
                keys[1] = 1;
                keys[2] = 2;
                break;
            case Y:
                keys[0] = 1;
                keys[1] = 0;
                keys[2] = 2;
                break;
            case Z:
                keys[0] = 2;
                keys[1] = 0;
                keys[2] = 1;
        }
        return keys;
    }

    /**
     * @param size - the length of the rows and columns of the face
     * @param dim - the dimension that the face will be perpendicular to
     * @param isPositive - if positive construct face in positive grid space, otherwise negative grid space
     * @return the size-by-size face of the cube perpendicular to the axis of dim
     */
    private ArrayList<List<Tile>> generateDim(int size, Dimension dim, boolean isPositive) {
        int[] keys = getKeys(dim);
        float[] point = new float[3];
        float start = round(size/2f, 1);
        ArrayList<List<Tile>> result = new ArrayList<List<Tile>>();
        if(isPositive) {
            point[keys[0]] = start;
        } else {
            point[keys[0]] = -1*start;
        }
        point[keys[1]] = -1*start;
        point[keys[2]] = -1*start;
        for(int row=0;row<size;row++) {
            ArrayList<Tile> tiles = new ArrayList<Tile>();
            for(int col=0;col<size;col++) {
                Tile tile = new Tile(dim, new Point3f(point));
                group.addChild(tile);
                tiles.add(tile);
                point[keys[1]]++;
            }
            point[keys[1]] = -1*start;
            point[keys[2]]++;
            result.add(tiles);
        }
        return result;
    }


    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    public static void main(String[] args) {
        try {
            RubeCube theCube = new RubeCube(3);
            new MainFrame(theCube);
        } catch (NonPositiveSizeException e) {
            e.printStackTrace();
        }
    }
}
