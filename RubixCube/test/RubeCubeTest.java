package RubixCube.test;

import RubixCube.module.RubeCube;
import RubixCube.module.RubeFace;
import RubixCube.module.exceptions.ColumnOutOfBoundsException;
import RubixCube.module.exceptions.NonPositiveSizeException;
import RubixCube.module.exceptions.RowOutOfBoundsException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by sam5binny on 2016-10-07.
 */
public class RubeCubeTest {
    RubeCube cube;
    int[][] testFace;

    @Before
    public void runBefore() throws NonPositiveSizeException {
        cube = new RubeCube(3);
        testFace = new int[3][3];
    }

    @Test
    public void testMoveLeftCol() throws ColumnOutOfBoundsException {
        int[][] face4Tiles = new int[3][3];
        face4Tiles = setFace(face4Tiles, 4);
        face4Tiles = setCol(face4Tiles, 8, 0);
        cube.getFaces().get(4).setTiles(face4Tiles); // alter this face
        cube.moveColBy(0, 1);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        testFace = setCol(testFace, 3, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));

        testFace = setFace(testFace, 4);
        testFace = setRow(testFace, 8, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(4).getTiles(), testFace));

        testFace = setFace(testFace, 1);
        testFace = setCol(testFace, 0, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(1).getTiles(), testFace));

        testFace = setFace(testFace, 2);
        testFace = setCol(testFace, 1, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));

        testFace = setFace(testFace, 3);
        testFace = setCol(testFace, 2, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(3).getTiles(), testFace));

        cube.moveColBy(0, 3);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));
        testFace = setFace(testFace, 1);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(1).getTiles(), testFace));
        testFace = setFace(testFace, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));
        testFace = setFace(testFace, 3);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(3).getTiles(), testFace));
        testFace = setFace(testFace, 4);
        testFace = setCol(testFace, 8, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(4).getTiles(), testFace));
    }


    @Test
    public void testMoveRightCol() throws ColumnOutOfBoundsException {
        int[][] face5Tiles = new int[3][3];
        face5Tiles = setFace(face5Tiles, 5);
        face5Tiles = setCol(face5Tiles, 8, 0);
        cube.getFaces().get(5).setTiles(face5Tiles); // alter this face
        cube.moveColBy(2, 1);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        testFace = setCol(testFace, 3, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));
        testFace = setFace(testFace, 5);
        testFace = setRow(testFace, 8, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(5).getTiles(), testFace));

        testFace = setFace(testFace, 1);
        testFace = setCol(testFace, 0, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(1).getTiles(), testFace));

        testFace = setFace(testFace, 2);
        testFace = setCol(testFace, 1, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));

        testFace = setFace(testFace, 3);
        testFace = setCol(testFace, 2, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(3).getTiles(), testFace));

        cube.moveColBy(2, 3);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));
        testFace = setFace(testFace, 1);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(1).getTiles(), testFace));
        testFace = setFace(testFace, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));
        testFace = setFace(testFace, 3);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(3).getTiles(), testFace));
        testFace = setFace(testFace, 5);
        testFace = setCol(testFace, 8, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(5).getTiles(), testFace));
    }

    @Test
    public void testMoveTopRow() throws RowOutOfBoundsException {
        int[][] face1Tiles = new int[3][3];
        face1Tiles = setFace(face1Tiles, 1);
        face1Tiles = setCol(face1Tiles, 8, 0);
        cube.getFaces().get(1).setTiles(face1Tiles); // alter this face
        cube.moveRowBy(0, 1);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        testFace = setRow(testFace, 4, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));

        testFace = setFace(testFace, 1);
        testFace = setRow(testFace, 8, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(1).getTiles(), testFace));

        testFace = setFace(testFace, 5);
        testFace = setRow(testFace, 0, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(5).getTiles(), testFace));

        testFace = setFace(testFace, 2);
        testFace = setRow(testFace, 5, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));

        testFace = setFace(testFace, 4);
        testFace = setRow(testFace, 2, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(4).getTiles(), testFace));

        cube.moveRowBy(0, 3);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));
        testFace = setFace(testFace, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));
        testFace = setFace(testFace, 4);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(4).getTiles(), testFace));
        testFace = setFace(testFace, 5);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(5).getTiles(), testFace));
        testFace = setFace(testFace, 1);
        testFace = setCol(testFace, 8, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(1).getTiles(), testFace));
    }

    @Test
    public void testMoveBottomRow() throws RowOutOfBoundsException {
        int[][] face3Tiles = new int[3][3];
        face3Tiles = setFace(face3Tiles, 3);
        face3Tiles = setCol(face3Tiles, 8, 0);
        cube.getFaces().get(3).setTiles(face3Tiles); // alter this face
        cube.moveRowBy(2, 1);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        testFace = setRow(testFace, 4, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));

        testFace = setFace(testFace, 3);
        testFace = setRow(testFace, 8, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(3).getTiles(), testFace));

        testFace = setFace(testFace, 5);
        testFace = setRow(testFace, 0, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(5).getTiles(), testFace));

        testFace = setFace(testFace, 2);
        testFace = setRow(testFace, 5, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));

        testFace = setFace(testFace, 4);
        testFace = setRow(testFace, 2, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(4).getTiles(), testFace));

        cube.moveRowBy(2, 3);
        assertTrue(cube.getFaces().size() == 6);
        testFace = setFace(testFace, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(0).getTiles(), testFace));
        testFace = setFace(testFace, 2);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(2).getTiles(), testFace));
        testFace = setFace(testFace, 4);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(4).getTiles(), testFace));
        testFace = setFace(testFace, 5);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(5).getTiles(), testFace));
        testFace = setFace(testFace, 3);
        testFace = setCol(testFace, 8, 0);
        assertTrue(Arrays.deepEquals(cube.getFaces().get(3).getTiles(), testFace));
    }

    @Test
    public void testTwoTilesSwapped() throws NonPositiveSizeException, ColumnOutOfBoundsException {
        List<RubeFace> faces = cube.getFaces();
        RubeCube otherCube = new RubeCube(3);
        // Test same cubes
        assertFalse(cube.twoTilesSwapped(otherCube));
        assertFalse(otherCube.twoTilesSwapped(cube));

        // Test cubes with more than 2 swaps
        cube.moveColBy(0, 1);
        assertFalse(cube.twoTilesSwapped(otherCube));
        assertFalse(otherCube.twoTilesSwapped(cube));

        // Test tiles swapped on same face
        int[][] tempFace = faces.get(0).getTiles();
        int a = tempFace[0][0];
        int b = tempFace[2][2];
        tempFace[0][0] = b;
        tempFace[2][2] = a;
        faces.get(0).setTiles(tempFace);
        otherCube.moveColBy(0, 1);
        assertTrue(cube.twoTilesSwapped(otherCube));
        assertTrue(otherCube.twoTilesSwapped(cube));

        // Test tiles swapped on different faces
        cube = new RubeCube(3);
        otherCube = new RubeCube(3);
        faces = cube.getFaces();
        tempFace = faces.get(1).getTiles();
        int[][] tempFace2 = faces.get(4).getTiles();
        a = tempFace[1][0];
        b = tempFace2[1][2];
        faces.get(1).setTiles(tempFace);
        faces.get(4).setTiles(tempFace2);
        tempFace[1][0] = b;
        tempFace2[1][2] = a;
        assertTrue(cube.twoTilesSwapped(otherCube));
        assertTrue(otherCube.twoTilesSwapped(cube));
    }

    private int[][] setFace(int[][] testFace, int value) {
        for(int row=0;row<3;row++) {
            for(int col=0;col<3;col++) {
                testFace[row][col] = value;
            }
        }
        return testFace;
    }

    private int[][] setCol(int[][] testFace, int value, int col) {
        for(int row=0;row<3;row++){
            testFace[row][col] = value;
        }
        return testFace;
    }

    private int[][] setRow(int[][] testFace, int value, int row) {
        for(int col=0;col<3;col++){
            testFace[row][col] = value;
        }
        return testFace;
    }

    private void printTestFace(int[][] aFace) {
        String result = "";
        for(int[] row : aFace) {
            for(int tile : row) {
                result = result + tile + " ";
            }
            result = result + "\n";
        }
        System.out.println(result);
    }
}
