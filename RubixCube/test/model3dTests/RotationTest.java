package RubixCube.test.model3dTests;

import RubixCube.model3d.CubeFrame;
import RubixCube.model3d.exceptions.NullCubeFrameException;
import RubixCube.module.RubeCube;
import RubixCube.module.exceptions.NonPositiveSizeException;
import org.junit.Before;
import org.junit.Test;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;

import static org.junit.Assert.assertEquals;

/**
 * Created by sammc on 2016-12-29.
 */
public class RotationTest {
    private RubeCube rubeCube;
    private static final float DELTA = 0.00001f;

    @Before
    public void runBefore() {
        try {
            rubeCube = new RubeCube(3);
        } catch (NonPositiveSizeException e) {
            e.printStackTrace();
        }
        CubeFrame.getInstance(rubeCube);
    }


    @Test
    // Cases:
    //      1) Axis and angle of rotation are both zero
    //      2) the expected angle of rotation is NaN (ie. trace of the transform is not -1<trace<3)
    //      3) the angle of rotation is some non-negative real number
    public void testFindAngle() throws NullCubeFrameException {
        assertEquals(CubeFrame.getInstance().findAngle(), 0f, DELTA);
        Transform3D testTransform = new Transform3D();
        float[] testMatrix = new float[16];
        testMatrix[0] = 1;
        testMatrix[5] = 2;
        testMatrix[10] = 3;
        testTransform.set(testMatrix);
        assertEquals(CubeFrame.getInstance().findAngle(testTransform), Float.NaN, DELTA);
        CubeFrame.getInstance().updateRotation(new Point3f(1f,0f,0f), true, 1);
        assertEquals(CubeFrame.getInstance().findAngle(), 1, DELTA);
    }

    @Test
    public void testRoundTransform() {
    }

    @Test
    public void testUpdateRotation() {
    }

    @Test
    public void testRotateCube() {
    }
}
