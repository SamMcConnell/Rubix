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

/**
 *  The class that contains the 3D model of the cube
 */
public class CubeFrame extends Applet implements KeyListener{
    private RubeCube cube;
    private List<List<List<Tile>>> cube3D;
    private static CubeFrame cubeFrame;
    private BranchGroup worldGroup;
    private TransformGroup objTrans;
    private Transform3D trans = new Transform3D();
    private SimpleUniverse universe;
    private float xAngle = 0f;
    private final float dAngle = 0.05f;
    private float yAngle = 0f;

    public static final float WIDTH = 1.1f;
    private static float SCL;

    private CubeFrame(RubeCube cube) {
        SCL = WIDTH/cube.getSize();
        cube3D = new ArrayList<List<List<Tile>>>();
        this.cube = cube;


        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        add("Center", c);
        c.addKeyListener(this);

        //timer.start();
        Panel p =new Panel();
        add("North",p);

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
        pos1.setRotation(new AxisAngle4f(0f,1f,0f, 0.02f));
        objTrans.setTransform(pos1);
        generateCube();
        worldGroup.addChild(objTrans);

        Color3f light1Color = new Color3f(1.0f, 0.5f, 1.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 300.0);
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
     * @param size - the number of the rows/columns of the face
     * @param dim - the dimension that the face will be perpendicular to
     * @param isPositive - if positive construct face in positive dim grid space, otherwise negative dim grid space
     * @return the size-by-size face of the cube perpendicular to the axis of dim
     */
    private ArrayList<List<Tile>> generateDim(int size, Dimension dim, boolean isPositive) {
        int[] keys = getKeys(dim);
        float[] point = new float[3];
        float start = WIDTH/2f;
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
                Tile tile = new Tile(dim, new Point3f(point), size);
                objTrans.addChild(tile);
                tiles.add(tile);
                point[keys[1]] += SCL;
            }
            point[keys[1]] = -1*start;
            point[keys[2]] += SCL;
            result.add(tiles);
        }
        return result;
    }


    private static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
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
            MainFrame mf = new MainFrame(getInstance(), 256, 256);
        } catch (NullCubeFrameException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void updateRotation() {
        Transform3D xTrans = new Transform3D();
        Transform3D yTrans = new Transform3D();
        xTrans.setRotation(new AxisAngle4f(0f,1f,0f, xAngle));
        float[] yRot = yRotAxis(xAngle);
        // set the axis of rotation of Y to accommodate for rotation from x
        yTrans.setRotation(new AxisAngle4f(yRot[0],yRot[1],0f, yAngle));
        trans.mul(yTrans, xTrans);
        objTrans.setTransform(trans);
    }

    private float[] yRotAxis(float angle) {
        float[] result = new float[2];
        float sin = (float) Math.sin(-angle);
        float cos = (float) Math.cos(-angle);
        result[0] = cos-sin;
        result[1] = cos+sin;
        return result;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Invoked when a key has been pressed.
        if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            xAngle -= dAngle;
            updateRotation();
        }
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
            xAngle += dAngle;
            updateRotation();
        }
        if (e.getKeyCode()==KeyEvent.VK_UP) {
            yAngle -= dAngle;
            updateRotation();
        }
        if (e.getKeyCode()==KeyEvent.VK_DOWN) {
            yAngle += dAngle;
            updateRotation();
        }
        if (e.getKeyCode()==KeyEvent.VK_SPACE) {
            yAngle = 0;
            xAngle = 0;
            updateRotation();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
