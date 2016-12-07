package RubixCube.test;

import RubixCube.module.RubeFace;
import RubixCube.module.exceptions.NonPositiveSizeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sam5binny on 2016-10-13.
 */
public class Rube5FaceTest {
    RubeFace face;
    int[][] tiles = new int[5][5];

    @Before
    public void runBefore() throws NonPositiveSizeException {
        face = new RubeFace(1, 5);
        resetFace();
    }

    @Test
    public void testCopyFace() {
        System.out.println("-----\n" + face);
        assertEquals(face, face.copyFace());
    }

    @Test
    public void testRotateFace() throws NonPositiveSizeException {
        int[][] result = new int[5][5];
        result[0][0] = 20;
        result[0][1] = 15;
        result[0][2] = 10;
        result[0][3] = 5;
        result[0][4] = 0;
        result[1][0] = 21;
        result[1][1] = 16;
        result[1][2] = 11;
        result[1][3] = 6;
        result[1][4] = 1;
        result[2][0] = 22;
        result[2][1] = 17;
        result[2][2] = 12;
        result[2][3] = 7;
        result[2][4] = 2;
        result[3][0] = 23;
        result[3][1] = 18;
        result[3][2] = 13;
        result[3][3] = 8;
        result[3][4] = 3;
        result[4][0] = 24;
        result[4][1] = 19;
        result[4][2] = 14;
        result[4][3] = 9;
        result[4][4] = 4;
        RubeFace resultFace = new RubeFace(0, 5);
        resultFace.setTiles(result);
        face.rotateClockwise(1);
        System.out.println("-----\n" + face);
        assertEquals(face, resultFace);

        resetFace();
        result[0][0] = 24;
        result[0][1] = 23;
        result[0][2] = 22;
        result[0][3] = 21;
        result[0][4] = 20;
        result[1][0] = 19;
        result[1][1] = 18;
        result[1][2] = 17;
        result[1][3] = 16;
        result[1][4] = 15;
        result[2][0] = 14;
        result[2][1] = 13;
        result[2][2] = 12;
        result[2][3] = 11;
        result[2][4] = 10;
        result[3][0] = 9;
        result[3][1] = 8;
        result[3][2] = 7;
        result[3][3] = 6;
        result[3][4] = 5;
        result[4][0] = 4;
        result[4][1] = 3;
        result[4][2] = 2;
        result[4][3] = 1;
        result[4][4] = 0;
        resultFace.setTiles(result);
        face.rotateClockwise(2);
        assertEquals(face, resultFace);

        resetFace();
        result[0][0] = 4;
        result[0][1] = 9;
        result[0][2] = 14;
        result[0][3] = 19;
        result[0][4] = 24;
        result[1][0] = 3;
        result[1][1] = 8;
        result[1][2] = 13;
        result[1][3] = 18;
        result[1][4] = 23;
        result[2][0] = 2;
        result[2][1] = 7;
        result[2][2] = 12;
        result[2][3] = 17;
        result[2][4] = 22;
        result[3][0] = 1;
        result[3][1] = 6;
        result[3][2] = 11;
        result[3][3] = 16;
        result[3][4] = 21;
        result[4][0] = 0;
        result[4][1] = 5;
        result[4][2] = 10;
        result[4][3] = 15;
        result[4][4] = 20;
        resultFace.setTiles(result);
        face.rotateClockwise(3);
        assertEquals(face, resultFace);
    }

    private void resetFace() {
        int i = 0;
        for(int row=0;row<5;row++) {
            for(int col=0;col<5;col++) {
                tiles[row][col] = i++;
            }
        }
        face.setTiles(tiles);
    }
}
