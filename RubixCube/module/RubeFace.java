package RubixCube.module;

import java.util.Arrays;

/**
 * Created by sam5binny on 2016-10-07.
 */
public class RubeFace {

    private int[][] tiles = new int[3][3];

    public RubeFace(int c) {
        int d = c % 6;
        for(int row=0;row<3;row++) {
            for(int col=0;col<3;col++) {
                tiles[row][col] = d;
            }
        }
    }

    public int[][] getTiles() {
        return tiles;
    }

    public void setTiles(int[][] tiles) {
        this.tiles = tiles;
    }

    // REQUIRES: i is a non-negative integer
    // MODIFIES: this
    // EFFECTS: rotates this face 90 degrees clockwise
    public void rotateClockwise(int i) {
        i %= 4;
        RubeFace tempFace = this.copyFace();
        for(int row=0;row<3;row++) {
            for(int col=0;col<3;col++) {
                switch (i) {
                    case 1: tiles[row][col] = tempFace.getTiles()[RubeCube.findIndex(col)][row];
                        break;
                    case 2: tiles[row][col] = tempFace.getTiles()[RubeCube.findIndex(row)][RubeCube.findIndex(col)];
                        break;
                    case 3: tiles[row][col] = tempFace.getTiles()[col][RubeCube.findIndex(row)];
                        break;
                    default: System.out.println("CAUTION - 0 value entered in RubeFace.rotateClockwise(int)");
                        break;
                }
            }
        }
    }

    // EFFECTS: creates a copy of this face
    public RubeFace copyFace() {
        int[][] tilesCopy = new int[tiles.length][];
        RubeFace result = new RubeFace(0);

        for (int i = 0; i < tiles.length; i++) {
            tilesCopy[i] = new int[tiles[i].length];
            System.arraycopy(tiles[i], 0, tilesCopy[i], 0, tiles.length);
        }

        result.setTiles(tilesCopy);
        return result;
    }

    @Override
    public boolean equals(Object f) {
        if(!(f instanceof RubeFace)){
            System.out.println("CAUTION - RubeFace.equals(Object) called with a non-RubeFace parameter");
            return false;
        }
        RubeFace face = (RubeFace) f;
        return Arrays.deepEquals(tiles, face.getTiles());
    }

    @Override
    public int hashCode() {
        int row1 = tiles[0][0]<<6|tiles[0][1]<<3|tiles[0][2];
        int row2 = tiles[1][0]<<6|tiles[1][1]<<3|tiles[1][2];
        int row3 = tiles[2][0]<<6|tiles[2][1]<<3|tiles[2][2];
        return row1<<18|row2<<9|row3;
    }

    @Override
    public String toString() {
        String result = String.format("%d %d %d\n%d %d %d\n%d %d %d", tiles[0][0], tiles[0][1], tiles[0][2], tiles[1][0], tiles[1][1], tiles[1][2], tiles[2][0], tiles[2][1], tiles[2][2]);
        return result;
    }
}
