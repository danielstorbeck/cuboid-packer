package gllistener;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import misc.ForDragLineAskable;
import store.SituatedBox;
import box.OrderedDimensions;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class RotatableBoxes extends AbstractGLObject {
    ForDragLineAskable fdla;
    OrderedDimensions cnt;

    ArrayList<GLDrawableBox> ldb = new ArrayList<GLDrawableBox>();

    public RotatableBoxes(OrderedDimensions c, List<SituatedBox> l) {
        cnt = c;
        for (SituatedBox sb : l)
            ldb.add(new GLColorBox(sb, c.getOrigDimsCopy()));
    }

    public void setDragLineProvider(ForDragLineAskable a) {
        fdla = a;
    }

    public void display(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        // Push container backwards depending on its size.
        gl.glTranslatef(0.0f, 0.0f, -(cnt.getSortedDimsCopy()[2]) * 2);
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
        // Draw container.
        gl.glBegin(GL2.GL_LINES);
        (new GLContainerGrid(cnt)).draw(gl);
        gl.glEnd();
        // Draw boxes.
        gl.glBegin(GL2.GL_QUADS);
        for (GLDrawableBox db : ldb)
            db.draw(gl);
        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}
