package RubixCube.module;

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

    public RubeCube() {
        faces.add(new RubeFace(0));
        faces.add(new RubeFace(1));
        faces.add(new RubeFace(2));
        faces.add(new RubeFace(3));
        faces.add(new RubeFace(4));
        faces.add(new RubeFace(5));
    }

    public List<RubeFace> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<RubeFace> faces) {
        this.faces = faces;
    }

    // by default all movements operate from the perspective of face1

    // REQUIRES: moveBy is not negative
    // MODIFIES: this
    // EFFECTS: moves the left column upwards by moveBy from the perspective of face1
    public void moveLeftCol(int moveBy) {
        RubeFace faceResult = new RubeFace(0);
        int[][] face;
        int[][] oldFaces = findColFaces(0,moveBy);
        for(int i=0;i<4;i++) {
            face = faces.get(i).getTiles();
            for(int j=0;j<3;j++) {
                if(i == 2) {
                    face[j][2] = oldFaces[i][j];
                } else {
                    face[j][0] = oldFaces[i][j];
                }
            }
            faceResult.setTiles(face);
            faces.set(i, faceResult.copyFace()); // THIS IS THE PROBLEM
        }
        faces.get(4).rotateClockwise(3*moveBy%4);
    }


    // EFFECTS: returns an array of int arrays representing the tile values in the column 'col' of each face
    // EFFECTS: returns the new column tiles on each face that will be received after a rotation of moveBy in col column of face1
    public int[][] findColFaces(int col, int moveBy) {
        int[][] tempTiles = new int[4][3];
        for(int face=0;face<4;face++){
            for(int tile=0;tile<3;tile++) {
                tempTiles[face][tile] = faces.get((face + 4 - moveBy)%4).getTiles()[tile][col%3];
                if((face + 4 - moveBy)%4 == 2) {
                    // if face2 moves to this face...
                    tempTiles[face][tile] = faces.get(2).getTiles()[findIndex(0)][findIndex(col)];
                }
                if((face + moveBy)%4 == 2) {
                    // if this face moves to face2...
                    tempTiles[2][tile] = faces.get(face).getTiles()[findIndex(tile)][col];
                }
            }
        }
        return tempTiles;
    }

    // EFFECTS: 0 -> 2, 1 -> 1, 2 -> 0
    public static int findIndex(int i) {
        return (((i+1)%3)*2)%3;
    }



    // REQUIRES: moveBy is not negative
    // MODIFIES: this
    // EFFECTS: moves the right column up or down according to the value of moveDown and updates all faces
    public void moveRightCol(int moveBy) {
        RubeFace faceResult = new RubeFace(0);
        int[][] face;
        int[][] oldFaces = findColFaces(2,moveBy);
        for(int i=0;i<4;i++) {
            face = faces.get(i).getTiles();
            for(int j=0;j<3;j++) {
                if(i == 2) {
                    face[j][0] = oldFaces[i][j];
                } else {
                    face[j][2] = oldFaces[i][j];
                }
            }
            faceResult.setTiles(face);
            faces.set(i, faceResult.copyFace()); // THIS IS THE PROBLEM
        }
        faces.get(5).rotateClockwise(moveBy);
    }

    // REQUIRES: moveBy is not negative
    // MODIFIES: this
    // EFFECTS: moves the top row left or right according to the value of moveLeft and updates all faces
    public void moveTopRow(int moveBy) {
        RubeFace faceResult = new RubeFace(0);
        int[][] face;
        int[][] oldFaces = findRowFaces(0);
        for(int i=0;i<4;i++) {
            face = faces.get(convertForRow(i)).getTiles();
            for(int j=0;j<3;j++) {
                face[0][j] = oldFaces[(i+4-moveBy)%4][j];
            }
            faceResult.setTiles(face);
            faces.set(convertForRow(i), faceResult.copyFace());
        }
        faces.get(1).rotateClockwise(3*moveBy%4);
    }

    // REQUIRES: moveBy is not negative
    // MODIFIES: this
    // EFFECTS: moves the bottom row left or right according to the value of moveLeft and updates all faces
    public void moveBottomRow(int moveBy) {
        RubeFace faceResult = new RubeFace(0);
        int[][] face;
        int[][] oldFaces = findRowFaces(2);
        for(int i=0;i<4;i++) {
            face = faces.get(convertForRow(i)).getTiles();
            for(int j=0;j<3;j++) {
                face[2][j] = oldFaces[(i+4-moveBy)%4][j];
            }
            faceResult.setTiles(face);
            faces.set(convertForRow(i), faceResult.copyFace());
        }
        faces.get(3).rotateClockwise(moveBy);
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
