package gllistener;

import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import misc.ForDragLineAskable;
import box.OrdDims;
import store.SituBox;

public class RotatableBoxes extends AbstractGLObject {
	ForDragLineAskable fdla;
	OrdDims cnt;

	ArrayList<GLDrawableBox> ldb = new ArrayList<GLDrawableBox>();

	public RotatableBoxes(OrdDims c, List<SituBox> l) {
		cnt = c;
		for (SituBox sb : l)
			ldb.add(new GLColorBox(sb, c.getDims()));
	}

	public void setDragLineProvider(ForDragLineAskable a) {
		fdla = a;
	}

	public void display(GLAutoDrawable gLDrawable) {
		final GL gl = gLDrawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		// Push container backwards depending on its size.
		gl.glTranslatef(0.0f, 0.0f, -(cnt.getOrdDims()[2]) * 2);
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
		gl.glBegin(GL.GL_LINES);
		(new GLContainerGrid(cnt)).draw(gl);
		gl.glEnd();
		// Draw boxes.
		gl.glBegin(GL.GL_QUADS);
		for (GLDrawableBox db : ldb)
			db.draw(gl);
		gl.glEnd();
	}
}
