package RubixCube.model3d;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Created by sam5binny on 2016-12-03.
 */
public class MainFrame {

    public MainFrame() {

        SimpleUniverse universe = new SimpleUniverse();
        // Get rotatable cube on screen

        Transform3D transform = new Transform3D();
        TransformGroup tg = new TransformGroup();
        Cone cone = new Cone(0.5f, 0.5f);
        Vector3f vector = new Vector3f(-.2f, .1f, -.4f);
        transform.setTranslation(vector);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
