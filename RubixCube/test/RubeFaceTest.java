package RubixCube.test;

import RubixCube.module.RubeFace;
import RubixCube.module.exceptions.NonPositiveSizeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sam5binny on 2016-10-13.
 */
public class RubeFaceTest {
    RubeFace face;
    int[][] tiles = new int[3][3];

    @Before
    public void runBefore() throws NonPositiveSizeException {
        face = new RubeFace(1, 3);
        resetFace();
        System.out.println("Before\n-----\n" + face);
    }

    @Test
    public void testCopyFace() {
        System.out.println("Testing copying face...");
        System.out.println("-----\n" + face);
        assertEquals(face, face.copyFace());
    }

    @Test
    public void testRotateFace() throws NonPositiveSizeException {
        System.out.println("Testing rotating face...");
        int[][] result = new int[3][3];
        result[0][0] = 6;
        result[0][1] = 3;
        result[0][2] = 0;
        result[1][0] = 7;
        result[1][1] = 4;
        result[1][2] = 1;
        result[2][0] = 8;
        result[2][1] = 5;
        result[2][2] = 2;
        RubeFace resultFace = new RubeFace(0 , 3);
        resultFace.setTiles(result);
        face.rotateClockwise(1);
        System.out.println("-----\n" + face);
        assertEquals(face, resultFace);

        resetFace();
        result[0][0] = 8;
        result[0][1] = 7;
        result[0][2] = 6;
        result[1][0] = 5;
        result[1][1] = 4;
        result[1][2] = 3;
        result[2][0] = 2;
        result[2][1] = 1;
        result[2][2] = 0;
        resultFace.setTiles(result);
        face.rotateClockwise(2);
        assertEquals(face, resultFace);

        resetFace();
        result[0][0] = 2;
        result[0][1] = 5;
        result[0][2] = 8;
        result[1][0] = 1;
        result[1][1] = 4;
        result[1][2] = 7;
        result[2][0] = 0;
        result[2][1] = 3;
        result[2][2] = 6;
        resultFace.setTiles(result);
        face.rotateClockwise(3);
        assertEquals(face, resultFace);
    }

    private void resetFace() {
        int i = 0;
        for(int row=0;row<3;row++) {
            for(int col=0;col<3;col++) {
                tiles[row][col] = i++;
            }
        }
        face.setTiles(tiles);
    }
}
