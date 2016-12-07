package RubixCube.module;

import RubixCube.module.exceptions.ColumnOutOfBoundsException;
import RubixCube.module.exceptions.NonPositiveSizeException;
import RubixCube.module.exceptions.RowOutOfBoundsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam5binny on 2016-10-07.
 * An unfolded cube would have these orientations for each face with respect to face 1
 * CAUTION - In the code all faces are referred to by their zero-based indices
 *                ___
 *               | F4|
 *               |___|
 *               |ƐℲ |  (F3 is upside down)
 *               |___|
 *               | F2|
 *            ___|___|___
 *           | F5| F1| F6|
 *           |___|___|___|
 */
public class RubeCube {
    private List<RubeFace> faces = new ArrayList<RubeFace>();
    private final int size;

    public RubeCube(int size) throws NonPositiveSizeException {
        this.size = size;
        faces.add(new RubeFace(0, size));
        faces.add(new RubeFace(1, size));
        faces.add(new RubeFace(2, size));
        faces.add(new RubeFace(3, size));
        faces.add(new RubeFace(4, size));
        faces.add(new RubeFace(5, size));
    }

    public List<RubeFace> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<RubeFace> faces) {
        this.faces = faces;
    }

    // by default all movements operate from the perspective of face1


    /**
     *   Move the column at colIndex by moveBy rotations upwards
     **/
    public void moveColBy(int colIndex, int moveBy) throws ColumnOutOfBoundsException {
        moveBy %= 4;
        if(colIndex >= size || colIndex < 0) throw new ColumnOutOfBoundsException();
        moveCol(moveBy, colIndex);
        if(colIndex == 0) {
            faces.get(4).rotateClockwise(3*moveBy%4);
        } else if (colIndex == size-1) {
            faces.get(5).rotateClockwise(moveBy);
        }
    }

    private void moveCol(int moveBy, int colIndex) {
        RubeFace faceResult = null;
        try {
            faceResult = new RubeFace(0, size);
        } catch (NonPositiveSizeException e) {
            e.printStackTrace();  // impossible to reach
        }
        int[][] face;
        int[][] oldFaces = findColFaces(colIndex,moveBy);
        for(int i=0;i<4;i++) {
            face = faces.get(i).getTiles();
            for(int row=0;row<size;row++) {
                if(i == 2) {
                    face[row][findIndex(colIndex, size)] = oldFaces[i][row];
                } else {
                    face[row][colIndex] = oldFaces[i][row];
                }
            }
            faceResult.setTiles(face);
            faces.set(i, faceResult.copyFace());
        }
    }


    // EFFECTS: returns an array of int arrays representing the tile values in the column 'col' of each face
    // EFFECTS: returns the new column tiles on each face that will be received after a rotation of moveBy in col column of face1
    public int[][] findColFaces(int col, int moveBy) {
        int[][] tempTiles = new int[4][size];
        for(int face=0;face<4;face++){
            for(int tile=0;tile<size;tile++) {
                tempTiles[face][tile] = faces.get((face + 4 - moveBy)%4).getTiles()[tile][col%size];
                if((face + 4 - moveBy)%4 == 2) {
                    // if face2 moves to this face...
                    tempTiles[face][tile] = faces.get(2).getTiles()[findIndex(0, size)][findIndex(col, size)];
                }
                if((face + moveBy)%4 == 2) {
                    // if this face moves to face2...
                    tempTiles[2][tile] = faces.get(face).getTiles()[findIndex(tile, size)][col];
                }
            }
        }
        return tempTiles;
    }

    // EFFECTS: Given an index value, return the index equally far from the median index value in the opposite direction
    public static int findIndex(int i, int size) {
        return size - (i + 1);
    }


    /**
     *   Move the rowumn at rowIndex by moveBy rotations upwards
     **/
    public void moveRowBy(int rowIndex, int moveBy) throws RowOutOfBoundsException {
        moveBy %= 4;
        if(rowIndex >= size || rowIndex < 0) throw new RowOutOfBoundsException();
        moveRow(moveBy, rowIndex);
        if(rowIndex == 0) {
            faces.get(1).rotateClockwise(3*moveBy%4);
        } else if (rowIndex == size-1) {
            faces.get(3).rotateClockwise(moveBy);
        }
    }

    private void moveRow(int moveBy, int rowIndex) {
        RubeFace faceResult = null;
        try {
            faceResult = new RubeFace(0, size);
        } catch (NonPositiveSizeException e) {
            e.printStackTrace();  // impossible to reach
        }
        int[][] face;
        int[][] oldFaces = findRowFaces(rowIndex);
        for(int i=0;i<4;i++) {
            face = faces.get(convertForRow(i)).getTiles();
            for(int col=0;col<size;col++) {
                face[rowIndex][col] = oldFaces[(i+4-moveBy)%4][col];
            }
            faceResult.setTiles(face);
            faces.set(convertForRow(i), faceResult.copyFace());
        }
    }



    // EFFECTS: returns an array of int arrays representing the tile values in the row 'row' of each face
    private int[][] findRowFaces(int row) {
        int[][] tempTiles = new int[4][3];
        for(int face=0;face<4;face++){
            for(int tile=0;tile<3;tile++) {
                tempTiles[face][tile] = faces.get(convertForRow(face)).getTiles()[row%3][tile];
            }
        }
        return tempTiles;
    }

    // REQUIRES non-negative i
    // EFFECTS: converts index ints into ints representing appropriate faces for row transformations
    // 0 -> 0
    // 1 -> 5
    // 2 -> 2
    // 3 -> 4
    private int convertForRow(int i) {
        i = i % 4;
        switch (i) {
            case 0: return 0;
            case 1: return 5;
            case 2: return 2;
            case 3: return 4;
            default:
                System.out.println("CAUTION - negative integer passed to RubeCube.convertForRow(int)");
                return i;
        }
    }

    //  EFFECTS: Returns true if the two cubes are identical except for two faces which have swapped values
    public boolean twoTilesSwapped(RubeCube other) {
        List<RubeFace> unequalFaces = new ArrayList<RubeFace>();
        List<RubeFace> otherUnequalFaces = new ArrayList<RubeFace>();
        for(int i=0;i<6;i++) {
            if(!other.getFaces().contains(faces.get(i))) {
                unequalFaces.add(faces.get(i));
                otherUnequalFaces.add(other.getFaces().get(i));
                if(unequalFaces.size() > 2) return false;
            }
        }
        switch (unequalFaces.size()) {
            case 0: return false;
            case 1: return closeEnough(unequalFaces.get(0).getTiles(), otherUnequalFaces.get(0).getTiles(), 2, 2);
            case 2: return only2diff(unequalFaces, otherUnequalFaces);
            default: return false;
        }
    }

    // REQUIRES: both someFaces and someOtherFaces are not empty, have the same size, and have size 2
    // EFFECTS: returns true if and only if there are exactly 2 differences between the faces listed
    private boolean only2diff(List<RubeFace> someFaces, List<RubeFace> someOtherFaces) {
        if(closeEnough(someFaces.get(0).getTiles(), someOtherFaces.get(0).getTiles(), 1, 1)) {
            return closeEnough(someFaces.get(1).getTiles(), someOtherFaces.get(1).getTiles(), 1 ,1);
        }
        return false;
    }

    // EFFECTS: if the # of diff. elements between int[][]'s is within low and highTol, return true.  Otherwise false.
    private boolean closeEnough(int[][] array1, int[][] array2, int lowTol, int highTol) {
        int diffCount = 0;
        for(int row=0;row<3;row++) {
            for(int col=0;col<3;col++){
                if(array1[row][col] != array2[row][col]) diffCount++;
                if(diffCount > highTol) return false;
            }
        }
        return !(diffCount < lowTol);
    }

    @Override
    public String toString() {
        String result = "";
        for(int i=0;i<6;i++) {
            result = result + "Face " + i + "  ";
        }
        result = result + "\n";
        for(RubeFace face : faces) {
            for(int col=0;col<3;col++) {
                result = result + face.getTiles()[0][col] + " ";
            }
            result = result + "  ";
        }
        result = result + "\n";
        for(RubeFace face : faces) {
            for(int col=0;col<3;col++) {
                result = result + face.getTiles()[1][col] + " ";
            }
            result = result + "  ";
        }
        result = result + "\n";
        for(RubeFace face : faces) {
            for(int col=0;col<3;col++) {
                result = result + face.getTiles()[2][col] + " ";
            }
            result = result + "  ";
        }
        return result;
    }

    @Override
    public boolean equals(Object rc) {
        if(!(rc instanceof RubeCube)){
            System.out.println("CAUTION - RubeCub.equals(Object) called with a non-RubeCube parameter");
            return false;
        }
        RubeCube otherCube = (RubeCube) rc;
        boolean result = true;
        for(RubeFace face : faces) {
            if(!otherCube.getFaces().contains(face)) result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return faces.get(0).hashCode();
    }
}
