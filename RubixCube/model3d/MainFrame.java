package RubixCube.model3d;

import RubixCube.module.RubeCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Created by sam5binny on 2016-12-03.
 */
public class MainFrame {
    private RubeCube cube;

    public MainFrame(RubeCube cube) {
        this.cube = cube;

        SimpleUniverse universe = new SimpleUniverse();
        // Get rotatable cube on screen

        Transform3D transform = new Transform3D();
        TransformGroup tg = new TransformGroup();
        Cone cone = new Cone(0.5f, 0.5f);
        Vector3f vector = new Vector3f(-.2f, .1f, -.4f);
        transform.setTranslation(vector);
    }

    /**
     * Generate QuadArrays (funky name for planes) required for each face centered at the origin
     */
    private void generateCube() {
        int size = cube.getSize();
        float location = round(size/2f, 1);
        generateZ(size);
        //generateY(size);
        //generateX(size);
    }

    private void generateZ(float size) {
        for(int row=0;row<size;row++) {
            // move to starting point for row
            for(int col=0;col<size;col++) {

            }
        }
    }

    /**
     * Produce a square,
     *
     * @param axis
     * @param point
     * @return square, unit-length plane along the axis provided using point as the vector with the lowest coordinates.
     */
    private QuadArray generatePlane(Dimension axis, float[] point) {
        QuadArray result = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        float[] coordinate = point.clone();
        int nextIndex = 0;

        result.setCoordinate(nextIndex++, coordinate.clone());
        if(axis == Dimension.Y || axis == Dimension.Z) {
            coordinate[0]++;
            result.setCoordinate(nextIndex++, coordinate.clone());
            coordinate[0]--;
        }
        if(axis == Dimension.X || axis == Dimension.Z) {
            coordinate[1]++;
            result.setCoordinate(nextIndex++, coordinate.clone());
            coordinate[1]--;
        }
        if(axis == Dimension.X || axis == Dimension.Y) {
            coordinate[2]++;
            result.setCoordinate(nextIndex++, coordinate.clone());
            coordinate[2]--;
        }
        if(axis == Dimension.X) {
            coordinate[1]++;
            coordinate[2]++;
            result.setCoordinate(nextIndex, coordinate.clone());
        }
        if(axis == Dimension.Y) {
            coordinate[0]++;
            coordinate[2]++;
            result.setCoordinate(nextIndex, coordinate.clone());
        }
        if(axis == Dimension.Z) {
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
        float[] coord = new float[3];
        coord[0] = 1;
        coord[1] = 2;
        coord[2] = 3;
        float[] coord1 = coord.clone();
        coord[2]++;
        float[] coord2 = coord;
        for(int i=0;i<3;i++){
            System.out.println(coord1[i] + "   " + coord2[i]);
        }
    }
}
