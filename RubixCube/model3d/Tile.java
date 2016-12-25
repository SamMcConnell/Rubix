package RubixCube.model3d;


import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import java.util.ArrayList;

import static RubixCube.model3d.Dimension.*;


/**
 * A class which creates planes
 */
public class Tile extends Shape3D {
    private static int colour;
    private static float scl;
    private static final float WIDTH = 1.1f;

    Tile(Point3f a, Point3f b, Point3f c, Point3f d, int size) {
        scl = WIDTH/size;
        this.setGeometry(createGeometry(a, b, c, d));
        this.setAppearance(createAppearance());
    }

    // No matter the size of the cube, always display it so that it spans between

    Tile(Dimension axis, Point3f A, int size) {
        scl = WIDTH/size;
        ArrayList<Point3f> tileCoordinates = getTileCoordinates(axis, A);
        this.setGeometry(createGeometry(tileCoordinates.get(0),
                                        tileCoordinates.get(1),
                                        tileCoordinates.get(2),
                                        tileCoordinates.get(3)));
        colour += 50;
        this.setAppearance(createAppearance());
    }

    private Geometry createGeometry(Point3f A, Point3f B, Point3f C, Point3f D) {
        QuadArray plane = new QuadArray(4, QuadArray.COORDINATES);
        plane.setCoordinate(0, A);
        plane.setCoordinate(1, B);
        plane.setCoordinate(2, C);
        plane.setCoordinate(3, D);

        return plane;
    }

    private Appearance createAppearance() {
        Appearance appear = new Appearance();
        Color3f col = new Color3f((colour>>8)/15f, ((colour>>4)&15)/15f, (colour&15)/15f);
        ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        appear.setPolygonAttributes(pa);
        appear.setColoringAttributes(ca);
        return appear;
    }

    /**
     * @param axis - the axis that the plane is orthogonal to
     * @param point - an array of 3 coordinates describing the x, y, and z values of a point
     * @return square, unit-length plane along the axis provided using point as the vector with the lowest coordinates.
     */

    private ArrayList<Point3f> getTileCoordinates(Dimension axis, Point3f point) {
        ArrayList<Point3f> result = new ArrayList<Point3f>();

        result.add(point);
        if(axis == Y || axis == Z) result.add(incrementDim(X, point));
        if(axis == Z) result.add(incrementDim(X, incrementDim(Y, point)));
        if(axis == X || axis == Z) result.add(incrementDim(Y, point));
        if(axis == X) result.add(incrementDim(Y, incrementDim(Z, point)));
        if(axis == Y) result.add(incrementDim(X, incrementDim(Z, point)));
        if(axis == X || axis ==  Y) result.add(incrementDim(Z, point));
        return result;
    }

    private Point3f incrementDim(Dimension dim, Point3f point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();
        switch (dim) {
            case X: x += scl;
                break;
            case Y: y += scl;
                break;
            case Z: z += scl;
                break;
        }
        return new Point3f(x,y,z);
    }
}
