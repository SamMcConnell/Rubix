package RubixCube.model3d;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import java.util.ArrayList;

import static RubixCube.model3d.Dimension.*;


/**
 * Created by sam5binny on 2016-12-08.
 */
public class Tile extends Shape3D {

    Tile(Point3f a, Point3f b, Point3f c, Point3f d){
        this.setGeometry(createGeometry(a, b, c, d));
        this.setAppearance(createAppearance());
    }

    Tile(Dimension axis, Point3f A) {
        ArrayList<Point3f> tileCoordinates = getTileCoordinates(axis, A);
        this.setGeometry(createGeometry(tileCoordinates.get(0),
                                        tileCoordinates.get(1),
                                        tileCoordinates.get(2),
                                        tileCoordinates.get(3)));
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
        Color3f col = new Color3f(0.0f, 1.0f, 1.0f);
        ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);

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
            case X: x++;
                break;
            case Y: y++;
                break;
            case Z: z++;
                break;
        }
        return new Point3f(x,y,z);
    }

    @Override
    public String toString() {
        String result = "A B C D\n";
        for(int i=0; i<4; i++){
            result = result + "X: " + ((QuadArray) getGeometry()).getCoordRef3f()[i].getX() + "    ";
        }
        result = result + "\n";
        for(int i=0; i<4; i++){
            result = result + "Y: " + ((QuadArray) getGeometry()).getCoordRef3f()[i].getY() + "    ";
        }
        result = result + "\n";
        for(int i=0; i<4; i++){
            result = result + "Z: " + ((QuadArray) getGeometry()).getCoordRef3f()[i].getZ() + "    ";
        }
        return result;
    }
}
