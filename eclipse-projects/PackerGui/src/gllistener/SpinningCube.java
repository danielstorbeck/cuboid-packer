package gllistener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class SpinningCube extends AbstractGLObject {
	float ang = 0;

	public void display(GLAutoDrawable gLDrawable) {
		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		// Move.
		gl.glTranslatef(0.0f, 0.0f, -10.0f);
		// Rotate.
		gl.glRotatef(ang, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ang, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(ang, 0.0f, 0.0f, 1.0f);
		// cube definition
		gl.glBegin(GL2.GL_QUADS);
		// faces
		// front
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		// rear
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glVertex3f(1.0f, 1.0f, -1.0f);
		gl.glVertex3f(1.0f, -1.0f, -1.0f);
		// left
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		// right
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(1.0f, -1.0f, -1.0f);
		gl.glVertex3f(1.0f, 1.0f, -1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		// top
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, -1.0f);
		gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		// bottom
		gl.glColor3f(1.0f, 0.0f, 1.0f);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glVertex3f(1.0f, -1.0f, -1.0f);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		// End
		gl.glEnd();
		// Increment rotation angle.
		ang += 0.2;
		ang = ang % 360.0f;
	}

    @Override
    public void dispose(GLAutoDrawable drawable) {}
}
