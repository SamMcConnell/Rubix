package RubixCube.model3d;

import RubixCube.model3d.exceptions.NullCubeFrameException;
import RubixCube.module.RubeCube;
import RubixCube.module.exceptions.NonPositiveSizeException;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.*;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static RubixCube.model3d.Dimension.*;
import static java.lang.Float.NaN;

/**
 *  The class that contains the 3D model of the cube
 */
public class CubeFrame extends Applet implements KeyListener {
    private RubeCube cube;
    private List<List<List<Tile>>> cube3D;
    private static CubeFrame cubeFrame;
    private BranchGroup worldGroup;
    private TransformGroup objTrans;
    private Transform3D trans = new Transform3D();
    private SimpleUniverse universe;
    private final float dAngle = 0.05f;

    public static final float WIDTH = 1.1f;
    private static float SCL;

    private CubeFrame(RubeCube cube) {
        SCL = WIDTH / cube.getSize();
        cube3D = new ArrayList<List<List<Tile>>>();
        this.cube = cube;


        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        add("Center", c);
        c.addKeyListener(this);

        //timer.start();
        Panel p = new Panel();
        add("North", p);

        // Create a simple scene and attach it to the virtual universe
        universe = new SimpleUniverse(c);
        BranchGroup scene = createSceneGraph();
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(scene);
// Get rotatable cube on screen
    }

    /**
     * @return the BranchGroup containing the new modeled cube and its transforms
     */
    private BranchGroup createSceneGraph() {

        //add the transform group to the root of the branch graph
        // to be honest I don't understand the reasoning of this
        worldGroup = new BranchGroup();
        objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        worldGroup.addChild(objTrans);

        objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D pos1 = new Transform3D();
        pos1.setRotation(new AxisAngle4f(0f, 1f, 0f, 0.02f));
        objTrans.setTransform(pos1);
        generateCube();
        worldGroup.addChild(objTrans);

        Color3f light1Color = new Color3f(1.0f, 0.5f, 1.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 300.0);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        worldGroup.addChild(light1);

        return worldGroup;
    }

    /**
     * @return the singleton instance if it exists
     * @throws NullCubeFrameException if the instance does not exist
     */
    public static CubeFrame getInstance() throws NullCubeFrameException {
        if (cubeFrame == null) {
            throw new NullCubeFrameException();
        }
        return cubeFrame;
    }

    /**
     * @param aCube the rubix cube that is being represented (assumed to be organized initially)
     * @return the 3D representation of aCube, creating it if necessary
     */
    public static CubeFrame getInstance(RubeCube aCube) {
        if (cubeFrame == null) {
            cubeFrame = new CubeFrame(aCube);
        }
        return cubeFrame;
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
     *
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
     * @param size       - the number of the rows/columns of the face
     * @param dim        - the dimension that the face will be perpendicular to
     * @param isPositive - if positive construct face in positive dim grid space, otherwise negative dim grid space
     * @return the size-by-size face of the cube perpendicular to the axis of dim
     */
    private ArrayList<List<Tile>> generateDim(int size, Dimension dim, boolean isPositive) {
        int[] keys = getKeys(dim);
        float[] point = new float[3];
        float start = WIDTH / 2f;
        ArrayList<List<Tile>> result = new ArrayList<List<Tile>>();
        if (isPositive) {
            point[keys[0]] = start;
        } else {
            point[keys[0]] = -1 * start;
        }
        point[keys[1]] = -1 * start;
        point[keys[2]] = -1 * start;
        for (int row = 0; row < size; row++) {
            ArrayList<Tile> tiles = new ArrayList<Tile>();
            for (int col = 0; col < size; col++) {
                Tile tile = new Tile(dim, new Point3f(point), size);
                objTrans.addChild(tile);
                tiles.add(tile);
                point[keys[1]] += SCL;
            }
            point[keys[1]] = -1 * start;
            point[keys[2]] += SCL;
            result.add(tiles);
        }
        return result;
    }


    private static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * updates the rotation transform (trans) so that global (rather than local) coordinates can be used as input
     *
     * @param rotationVector describes the desired axis of rotation in global coordinates
     * @param isPositive     determines whether the angle of rotation will be positive or negative
     */
    public void updateRotation(Point3f rotationVector, boolean isPositive, float angle) {
        int sign = 1;
        if (!isPositive) sign = -1;
        Point3f tempVector = new Point3f(rotationVector.getX(), rotationVector.getY(), rotationVector.getZ());
        Transform3D temp = new Transform3D();
        temp.invert(trans);
        temp.transform(tempVector);
        temp.setRotation(new AxisAngle4f(tempVector.getX(), tempVector.getY(), tempVector.getZ(), sign * angle));
        trans.mul(temp);
        objTrans.setTransform(trans);
    }

    /**
     * Used to reset the rotation transform (trans) of the cube
     */
    private void updateRotation() {
        trans = new Transform3D();
        objTrans.setTransform(trans);
    }

    /**
     * WARNING - may return NaN if the trace of the transform is not within -1 and 3
     *
     * @param transform is the rotation transform of interest
     * @return the angle of the rotation transform
     */
    public float findAngle(Transform3D transform) {
        float[] matrix = new float[16];
        transform.get(matrix);
        float trace = matrix[0] + matrix[5] + matrix[10];
        return (float) Math.acos((trace - 1) / 2);
    }

    public float findAngle(){
        return findAngle(trans);
    }

    public Transform3D roundTransform(Transform3D transform, int precision) {
        float[] matrix = new float[16];
        transform.get(matrix);
        for (int i=0;i<matrix.length;i++) {
            matrix[i] = round(matrix[i], precision);
        }
        Transform3D result = new Transform3D();
        result.set(matrix);
        return result;
    }


    public void rotateCube(Point3f rotationVector, boolean isPositive){
        float angle = 0;  // how far we've rotated in radians since the method was first called
        float threshold = (float) Math.PI / 2;  // how far we'd like to rotate in radians
        int precision = 7;  // how many decimal places do we keep after rounding
        long delay = 30;    // how slow we want to rotate
        while(threshold - angle > dAngle){
            updateRotation(rotationVector, isPositive, dAngle);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            angle += dAngle;
            printAxis(trans);
        }
        updateRotation(rotationVector, isPositive, threshold-angle);
        trans = roundTransform(trans, precision);
        objTrans.setTransform(trans);
        printAxis(trans);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Invoked when a key has been pressed.
        //need to find vector that is the local rotation axis rotated by the inverse of trans

        Point3f rotationXVector = new Point3f(0f,1f,0f);
        Point3f rotationYVector = new Point3f(1f,0f,0f);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                rotateCube(rotationXVector, false);
                break;
            case KeyEvent.VK_RIGHT:
                rotateCube(rotationXVector, true);
                break;
            case KeyEvent.VK_UP:
                rotateCube(rotationYVector, false);
                break;
            case KeyEvent.VK_DOWN:
                rotateCube(rotationYVector, true);
                break;
            case KeyEvent.VK_A:
                updateRotation(rotationXVector, false, dAngle);
                printAxis(trans);
                break;
            case KeyEvent.VK_D:
                updateRotation(rotationXVector, true, dAngle);
                printAxis(trans);
                break;
            case KeyEvent.VK_W:
                updateRotation(rotationYVector, false,dAngle);
                printAxis(trans);
                break;
            case KeyEvent.VK_S:
                updateRotation(rotationYVector, true, dAngle);
                printAxis(trans);
                break;
            case KeyEvent.VK_SPACE:
                updateRotation();
                printAxis(trans);
                break;
            case KeyEvent.VK_P:
                printAxis(trans);
                break;
            default: break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void printAxis(Transform3D transform) {
        float[] matrix = new float[16];
        transform.get(matrix);
        float[] result = new float[3];
        result[0] = matrix[9] - matrix[6];
        result[1] = matrix[2] - matrix[8];
        result[2] = matrix[4] - matrix[1];
        System.out.println("X: " + result[0] + "    Y: " + result[1] + "    Z: " + result[2] + "    Angle: " + findAngle(transform));
    }


    public static void main(String[] args) {
        try {
            RubeCube theCube = new RubeCube(3);
            getInstance(theCube);
            System.out.println("Program Started");
        } catch (NonPositiveSizeException e) {
            e.printStackTrace();
        }
        try {
            getInstance().addKeyListener(getInstance());
            MainFrame mf = new MainFrame(getInstance(), 1024, 1024);
        } catch (NullCubeFrameException e) {
            e.printStackTrace();
        }
    }
}
