package RubixCube.model3d;

import RubixCube.module.RubeCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static RubixCube.model3d.Dimension.*;

/**
 *  The class that contains the 3D model of the cube
 */
public class MainFrame {
    private RubeCube cube;
    private List<List<List<QuadArray>>> cube3D = new ArrayList<List<List<QuadArray>>>();

    public MainFrame(RubeCube cube) {
        this.cube = cube;
        generateCube();

        SimpleUniverse universe = new SimpleUniverse();
        // Get rotatable cube on screen
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

    private ArrayList<List<QuadArray>> generateDim(int size, Dimension dim, boolean isPositive) {
        int[] keys = getKeys(dim);
        float[] point = new float[3];
        float start = round(size/2, 1);
        ArrayList<List<QuadArray>> result = new ArrayList<List<QuadArray>>();
        if(isPositive) {
            point[keys[0]] = start;
        } else {
            point[keys[0]] = -1*start;
        }
        point[keys[1]] = -1*start;
        point[keys[2]] = -1*start;
        for(int row=0;row<size;row++) {
            ArrayList<QuadArray> tiles = new ArrayList<QuadArray>();
            for(int col=0;col<size;col++) {
                tiles.add(generatePlane(dim, point));
                point[keys[1]]++;
            }
            point[keys[2]]++;
            result.add(tiles);
        }
        return result;
    }

    /**
     * Produce a square,
     *
     * @param axis - the axis that the plane is orthogonal to
     * @param point - an array of 3 coordinates describing the x, y, and z values of a point
     * @return square, unit-length plane along the axis provided using point as the vector with the lowest coordinates.
     */
    private QuadArray generatePlane(Dimension axis, float[] point) {
        QuadArray result = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        float[] coordinate = point.clone();
        int nextIndex = 0;

        result.setCoordinate(nextIndex++, coordinate.clone());
        if(axis == Y || axis == Z) {
            coordinate[0]++;
            result.setCoordinate(nextIndex++, coordinate.clone());
            coordinate[0]--;
        }
        if(axis == X || axis == Z) {
            coordinate[1]++;
            result.setCoordinate(nextIndex++, coordinate.clone());
            coordinate[1]--;
        }
        if(axis == X || axis == Y) {
            coordinate[2]++;
            result.setCoordinate(nextIndex++, coordinate.clone());
            coordinate[2]--;
        }
        if(axis == X) {
            coordinate[1]++;
            coordinate[2]++;
            result.setCoordinate(nextIndex, coordinate.clone());
        }
        if(axis == Y) {
            coordinate[0]++;
            coordinate[2]++;
            result.setCoordinate(nextIndex, coordinate.clone());
        }
        if(axis == Z) {
            coordinate[0]++;
            coordinate[1]++;
            result.setCoordinate(nextIndex, coordinate.clone());
        }
        return result;
    }

    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    public static void main(String[] args) {
    }
}
