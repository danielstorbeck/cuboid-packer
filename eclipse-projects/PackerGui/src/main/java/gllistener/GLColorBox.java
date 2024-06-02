package gllistener;

import com.jogamp.opengl.GL2;

import store.SituatedBox;

public class GLColorBox extends AbstractGLDrawBox {
    static float[][] colors = new float[][] { { 1.0f, 0.0f, 0.0f }, { 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 1.0f },
            { 1.0f, 1.0f, 0.0f }, { 1.0f, 0.0f, 1.0f }, { 0.0f, 1.0f, 1.0f } };
    float[] c; // color

    public GLColorBox(SituatedBox sb, float[] cd) {
        super(sb, cd);
        // Pick color.
        int i = (int) Math.floor(5.99 * Math.random());
        c = colors[i];
    }

    @Override
    public void draw(GL2 gl) {
        // color
        gl.glColor3f(c[0], c[1], c[2]);
        // Draw faces.
        // 1
        gl.glVertex3f(o[0], o[1], o[2]);
        gl.glVertex3f(o[0] + d[0], o[1], o[2]);
        gl.glVertex3f(o[0] + d[0], o[1] + d[1], o[2]);
        gl.glVertex3f(o[0], o[1] + d[1], o[2]);
        // 2
        gl.glVertex3f(o[0] + d[0], o[1] + d[1], o[2] + d[2]);
        gl.glVertex3f(o[0] + d[0], o[1] + d[1], o[2]);
        gl.glVertex3f(o[0] + d[0], o[1], o[2]);
        gl.glVertex3f(o[0] + d[0], o[1], o[2] + d[2]);
        // 3
        gl.glVertex3f(o[0] + d[0], o[1] + d[1], o[2] + d[2]);
        gl.glVertex3f(o[0] + d[0], o[1], o[2] + d[2]);
        gl.glVertex3f(o[0], o[1], o[2] + d[2]);
        gl.glVertex3f(o[0], o[1] + d[1], o[2] + d[2]);
        // 4
        gl.glVertex3f(o[0], o[1], o[2]);
        gl.glVertex3f(o[0], o[1] + d[1], o[2]);
        gl.glVertex3f(o[0], o[1] + d[1], o[2] + d[2]);
        gl.glVertex3f(o[0], o[1], o[2] + d[2]);
        // 5
        gl.glVertex3f(o[0] + d[0], o[1] + d[1], o[2] + d[2]);
        gl.glVertex3f(o[0], o[1] + d[1], o[2] + d[2]);
        gl.glVertex3f(o[0], o[1] + d[1], o[2]);
        gl.glVertex3f(o[0] + d[0], o[1] + d[1], o[2]);
        // 6
        gl.glVertex3f(o[0], o[1], o[2]);
        gl.glVertex3f(o[0], o[1], o[2] + d[2]);
        gl.glVertex3f(o[0] + d[0], o[1], o[2] + d[2]);
        gl.glVertex3f(o[0] + d[0], o[1], o[2]);
    }
}
