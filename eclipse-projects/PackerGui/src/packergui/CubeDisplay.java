package packergui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.media.opengl.GLJPanel;
import com.sun.opengl.util.Animator;

import misc.Observer;
import misc.Event;
import misc.ForDragLineAskable;
import box.OrdDims;
import gllistener.RotatableBoxes;
import gllistener.SpinningCube;
import store.SituBox;
import packer.Packer;

public class CubeDisplay extends JPanel implements Observer, ActionListener {
	private static final long serialVersionUID = 1L;
	BoxTableModel btm;
    ContTableModel ctm;
    JButton b;
    GLPanel glp;
    public CubeDisplay() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // OpenGL panel
        glp = new GLPanel();
        add(glp);
        // button
        b = new JButton("Display boxes");
        b.setAlignmentX(CENTER_ALIGNMENT);
        b.addActionListener(this);
        add(b);
    }
    public void setBTM(BoxTableModel m) {
        btm = m;
        btm.addObserver(this);
    }
    public void setCTM(ContTableModel m) {
        ctm = m;
        ctm.addObserver(this);
    }
    public void notifyAbout(Event e) {
        if (e.getClass() == packergui.BoxTableModel.ContentChangedEvent.class
             || e.getClass() == packergui.ContTableModel.ContentChangedEvent.class) {
            b.setEnabled(true);
        }
    }
    public void actionPerformed(ActionEvent e) {
        OrdDims cnt = ctm.getContainerDims();
        // Check container.
        if (!cnt.isNonZero()) {
            b.setEnabled(false);
            HelpTicker.putStillMessage("Container has no volume!");
            return;
        }
        List<OrdDims> lb = btm.getBoxDims();
        // Check boxes.
        ArrayList<OrdDims> ab = new ArrayList<OrdDims>();
        for (OrdDims od : lb) {
            if (od.isNonZero() && od.fitsIn(cnt))
                ab.add(od);
        }
        if (ab.size() < 1) {
            b.setEnabled(false);
            HelpTicker.putStillMessage("There are no valid boxes that fit into the container!");
            return;
        }
        // Container ok, boxes ok.
        final OrdDims c = cnt;
        final ArrayList<OrdDims> a = ab;
        Runnable r = new Runnable() {
            public void run() {
                // Situate boxes in another thread.
                Packer pck = new Packer(c, a);
                final List<SituBox> lsb = pck.getSituatedBoxes();
                final List<OrdDims> lib = pck.getIgnoredBoxes();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        // Display stuff back in event thread.
                        HelpTicker.putStillMessage("Situated: " + lsb.size() + ", ignored: " + lib.size());
                        // Display boxes.
                        glp.displayBoxes(c, lsb, lib);

                    }
                });
            }
        };
        new Thread(r).start();
        // This may take time, so display waiting sign.
        glp.displayWaitingSign();
        HelpTicker.displayWaitMessage();
        // Disable button.
        b.setEnabled(false);
    }
}

class GLPanel extends GLJPanel implements ForDragLineAskable {
	private static final long serialVersionUID = 1L;
	DragListener dl = new DragListener();
    RotatableBoxes rb;
    SpinningCube sc = new SpinningCube();
    boolean waitingCubeActive = false;
    boolean dragListenerActive = false;
    Animator an = new Animator(this);
    Point sPoint = null;
    Point ePoint = null;
    class DragListener extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            sPoint = e.getPoint();
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            ePoint = e.getPoint();
            display();
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            sPoint = null;
            ePoint = null;
            display();
        }
    }
    public GLPanel() {
        super();
        plugWaitingCube();
    }
    public Point[] getDragLine() {
        return new Point[] {sPoint, ePoint};
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
        if (dragListenerActive == true) unplugBoxes();
        if (waitingCubeActive == false) plugWaitingCube();
    }
    public void displayBoxes(OrdDims cnt, List<SituBox> lsb, List<OrdDims> lib) {
        rb = new RotatableBoxes(cnt, lsb);
        rb.setDragLineProvider(this);
        if (waitingCubeActive == true) unplugWaitingCube();
        addGLEventListener(rb);
        plugBoxes();
    }
}