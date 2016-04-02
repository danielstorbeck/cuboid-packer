package gllistener;

import javax.media.opengl.GL;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

public abstract class AbstractGLObject implements GLEventListener {
	GLU glu = new GLU();

	public abstract void display(GLAutoDrawable gLDrawable);

	public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	public void init(GLAutoDrawable gLDrawable) {
		GL gl = gLDrawable.getGL();
		gl.glShadeModel(GL.GL_FLAT);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width,
			int height) {
		GL gl = gLDrawable.getGL();
		if (height <= 0) {
			height = 1;
		}
		float h = (float) width / (float) height;
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(50.0f, h, 1.0, 1000.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
