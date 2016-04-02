package gllistener;

import javax.media.opengl.GL;

import store.SituBox;

public abstract class AbstractGLDrawBox implements GLDrawableBox {
	float[] d; // space diagonal
	float[] o; // origin

	public AbstractGLDrawBox(SituBox sb, float[] cd) {
		d = sb.getDims();
		float[] orig = sb.getOrigin();
		// Translate the origin of the container
		// to the center of the container.
		// Must be applied to each box.
		o = new float[] { orig[0] - (cd[0] * 0.5f), orig[1] - (cd[1] * 0.5f),
				orig[2] - (cd[2] * 0.5f) };
	}

	public abstract void draw(GL gl);
}
