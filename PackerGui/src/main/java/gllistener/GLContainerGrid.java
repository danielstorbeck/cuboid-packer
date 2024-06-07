package gllistener;

import com.jogamp.opengl.GL2;

import box.OrderedDimensions;

public class GLContainerGrid implements GLDrawableBox {
    float[] max;
    float[] min;

    public GLContainerGrid(OrderedDimensions od) {
        float[] dims = od.getOrigDimsCopy();
        min = new float[] { -(dims[0] / 2.0f), -(dims[1] / 2.0f), -(dims[2] / 2.0f) };
        max = new float[] { dims[0] + min[0], dims[1] + min[1], dims[2] + min[2] };
    }

    public void draw(GL2 gl) {
        gl.glColor3f(1, 0, 0);
        // 1
        gl.glVertex3f(min[0], min[1], min[2]);
        gl.glVertex3f(max[0], min[1], min[2]);
        gl.glColor3f(1, 1, 1);
        // 2
        gl.glVertex3f(max[0], min[1], min[2]);
        gl.glVertex3f(max[0], min[1], max[2]);
        // 3
        gl.glVertex3f(max[0], min[1], max[2]);
        gl.glVertex3f(min[0], min[1], max[2]);
        gl.glColor3f(1, 0, 0);
        // 4
        gl.glVertex3f(min[0], min[1], max[2]);
        gl.glVertex3f(min[0], min[1], min[2]);
        // 5
        gl.glVertex3f(min[0], min[1], min[2]);
        gl.glVertex3f(min[0], max[1], min[2]);
        gl.glColor3f(1, 1, 1);
        // 6
        gl.glVertex3f(max[0], min[1], min[2]);
        gl.glVertex3f(max[0], max[1], min[2]);
        // 7
        gl.glVertex3f(max[0], min[1], max[2]);
        gl.glVertex3f(max[0], max[1], max[2]);
        // 8
        gl.glVertex3f(min[0], min[1], max[2]);
        gl.glVertex3f(min[0], max[1], max[2]);
        // 9
        gl.glVertex3f(min[0], max[1], min[2]);
        gl.glVertex3f(max[0], max[1], min[2]);
        // 10
        gl.glVertex3f(max[0], max[1], min[2]);
        gl.glVertex3f(max[0], max[1], max[2]);
        // 11
        gl.glVertex3f(max[0], max[1], max[2]);
        gl.glVertex3f(min[0], max[1], max[2]);
        // 12
        gl.glVertex3f(min[0], max[1], max[2]);
        gl.glVertex3f(min[0], max[1], min[2]);
    }
}
