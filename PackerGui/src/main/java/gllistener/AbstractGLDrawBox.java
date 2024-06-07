package gllistener;

import store.SituatedBox;

import com.jogamp.opengl.GL2;

public abstract class AbstractGLDrawBox implements GLDrawableBox {
    float[] d; // space diagonal
    float[] o; // origin

    public AbstractGLDrawBox(SituatedBox sb, float[] cd) {
        d = sb.getOrigDimsCopy();
        float[] orig = sb.getOrigin();
        // Translate the origin of the container
        // to the center of the container.
        // Must be applied to each box.
        o = new float[] { orig[0] - (cd[0] * 0.5f), orig[1] - (cd[1] * 0.5f), orig[2] - (cd[2] * 0.5f) };
    }

    public abstract void draw(GL2 gl);
}
