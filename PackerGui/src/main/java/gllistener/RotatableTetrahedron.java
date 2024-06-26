package gllistener;

import java.awt.Point;

import misc.ForDragLineAskable;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class RotatableTetrahedron extends AbstractGLObject {
    ForDragLineAskable fdla;

    public void setGLPanel(ForDragLineAskable a) {
        fdla = a;
    }

    public void display(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -5.0f);
        // Fetch rotation vector.
        int xDiff = 0;
        int yDiff = 0;
        Point[] pts = fdla.getDragLine();
        if (pts[0] != null && pts[1] != null) {
            xDiff = pts[1].x - pts[0].x;
            yDiff = pts[1].y - pts[0].y;
        }
        gl.glRotatef(yDiff, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(xDiff, 0.0f, 1.0f, 0.0f);
        // Tetrahedron
        gl.glBegin(GL2.GL_TRIANGLES);
        // Front
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        // Right Side Facing Front
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, -1.0f, -1.0f);
        // Left Side Facing Front
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, -1.0f, -1.0f);
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        // Bottom
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glColor3f(0.1f, 0.1f, 0.1f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glVertex3f(0.0f, -1.0f, -1.0f);
        // End
        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}
