package packergui.cubedisplay;

import java.awt.Point;
import java.util.List;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;

import box.OrderedDimensions;
import gllistener.RotatableBoxes;
import gllistener.SpinningCube;
import misc.ForDragLineAskable;
import store.SituatedBox;

public class GLPanel extends GLJPanel implements ForDragLineAskable {
    private static final long serialVersionUID = 1L;
    DragListener dl = new DragListener(this);
    RotatableBoxes rb;
    SpinningCube sc = new SpinningCube();
    boolean waitingCubeActive = false;
    boolean dragListenerActive = false;
    Animator an = new Animator(this);
    Point sPoint = null;
    Point ePoint = null;

    public GLPanel() {
        super();
        plugWaitingCube();
    }

    public Point[] getDragLine() {
        return new Point[] { sPoint, ePoint };
    }

    void plugWaitingCube() {
        addGLEventListener(sc);
        an.start();
        waitingCubeActive = true;
    }

    void unplugWaitingCube() {
        an.stop();
        removeGLEventListener(sc);
        waitingCubeActive = false;
    }

    void plugBoxes() {
        addMouseListener(dl);
        addMouseMotionListener(dl);
        dragListenerActive = true;
    }

    void unplugBoxes() {
        removeGLEventListener(rb);
        removeMouseListener(dl);
        removeMouseMotionListener(dl);
        dragListenerActive = false;
    }

    public void displayWaitingSign() {
        if (dragListenerActive == true)
            unplugBoxes();
        if (waitingCubeActive == false)
            plugWaitingCube();
    }

    public void displayBoxes(OrderedDimensions cnt, List<SituatedBox> lsb, List<OrderedDimensions> lib) {
        rb = new RotatableBoxes(cnt, lsb);
        rb.setDragLineProvider(this);
        if (waitingCubeActive == true)
            unplugWaitingCube();
        addGLEventListener(rb);
        plugBoxes();
    }

    void resetEPoint() {
        ePoint = null;
    }

    void resetSPoint() {
        sPoint = null;
    }

    void setEPoint(Point point) {
        ePoint = point;
    }

    void setSPoint(Point point) {
        sPoint = point;
    }
}