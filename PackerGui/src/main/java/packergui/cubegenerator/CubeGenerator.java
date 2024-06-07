package packergui.cubegenerator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import packergui.cubetable.BoxTableModel;

public class CubeGenerator extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    GeneratorSlider noc, minel, maxel, cubn;
    BoxTableModel boxTableModel;

    public CubeGenerator() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel dummy; // spacer
        // slider, nr. of cubes
        noc = new GeneratorSlider("Number of boxes", 0, 100, 10, 20, 1.0f);
        add(noc);
        dummy = new JLabel();
        dummy.setMaximumSize(new Dimension(10, 20));
        add(dummy);
        // slider, minimal edge length
        minel = new GeneratorSlider("Minimal edge length", 0, 100, 20, 20, 0.1f);
        add(minel);
        dummy = new JLabel();
        dummy.setMaximumSize(new Dimension(10, 20));
        add(dummy);
        // slider, maximal edge length
        maxel = new GeneratorSlider("Maximal edge length", 0, 100, 80, 20, 0.1f);
        add(maxel);
        dummy = new JLabel();
        dummy.setMaximumSize(new Dimension(10, 20));
        add(dummy);
        // slider, cubeness
        cubn = new GeneratorSlider("Cubeness", 0, 100, 50, 20, 0.01f);
        add(cubn);
        dummy = new JLabel();
        dummy.setMaximumSize(new Dimension(10, 40));
        add(dummy);
        // generate button
        JButton gb = new JButton("Generate boxes");
        gb.addActionListener(this);
        gb.setAlignmentX(CENTER_ALIGNMENT);
        add(gb);
    }

    public void setBTM(BoxTableModel btm) {
        boxTableModel = btm;
    }

    public void actionPerformed(ActionEvent e) {
        // Collect slider values.
        int nr = (int) noc.getSliderValue();
        float elmin = minel.getSliderValue();
        float elmax = maxel.getSliderValue();
        float cbns = cubn.getSliderValue();
        // Check values.
        if (nr < 1 || elmin == 0.0f || elmax == 0.0f)
            // Should display warning message.
            // ...
            return;
        // Prepare values for generation.
        float eMin = Math.min(elmin, elmax);
        float eMax = Math.max(elmin, elmax);
        float eDif = eMax - eMin;
        float rel = 1.0f; // random edge length
        float[] dims = new float[] { 1.0f, 1.0f, 1.0f }; // dimensions
        List<Vector<Number>> lvn = new ArrayList<Vector<Number>>();
        // Generate.
        for (int i = 0; i < nr; i++) {
            // length somewhere between min and max
            rel = eMin + ((float) Math.random() * eDif);
            if (cbns == 1.0f) {
                dims[0] = dims[1] = dims[2] = rel;
            } else {
                float rand;
                for (int j = 0; j < 3; j++) {
                    rand = (1.0f - cbns) * (float) Math.random();
                    if (Math.random() < 0.5)
                        dims[j] = rel - ((rel - eMin) * rand);
                    else
                        dims[j] = rel + ((eMax - rel) * rand);
                }
            }
            Vector<Number> vn = new Vector<Number>();
            for (float dim : dims) {
                vn.add(new Float(dim));
            }
            vn.add(new Integer(1));
            lvn.add(vn);
        }
        // Add boxes to list.
        boxTableModel.addNewRows(lvn);
        // Change visible tab to show the boxes' table.
        ((JTabbedPane) getParent()).setSelectedIndex(0);
    }
}
