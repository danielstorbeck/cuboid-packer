package packergui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import misc.Observer;
import misc.Event;
import box.OrderedDimensions;
import store.SituatedBox;
import packer.Packer;
import packergui.cubetable.BoxTableModel;
import packergui.cubetable.ContTableModel;

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
        if (e.getClass() == BoxTableModel.ContentChangedEvent.class
                || e.getClass() == ContTableModel.ContentChangedEvent.class) {
            b.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        OrderedDimensions cnt = ctm.getContainerDims();
        // Check container.
        if (!cnt.isNonZero()) {
            b.setEnabled(false);
            HelpTicker.putStillMessage("Container has no volume!");
            return;
        }
        List<OrderedDimensions> lb = btm.getBoxDims();
        // Check boxes.
        ArrayList<OrderedDimensions> ab = new ArrayList<OrderedDimensions>();
        for (OrderedDimensions od : lb) {
            if (od.isNonZero() && od.fitsIn(cnt))
                ab.add(od);
        }
        if (ab.size() < 1) {
            b.setEnabled(false);
            HelpTicker.putStillMessage("There are no valid boxes that fit into the container!");
            return;
        }
        // Container ok, boxes ok.
        final OrderedDimensions c = cnt;
        final ArrayList<OrderedDimensions> a = ab;
        Runnable r = new Runnable() {
            public void run() {
                // Situate boxes in another thread.
                Packer pck = new Packer(c, a);
                final List<SituatedBox> lsb = pck.getSituatedBoxes();
                final List<OrderedDimensions> lib = pck.getIgnoredBoxes();
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