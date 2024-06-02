package packergui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

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

class GeneratorSlider extends JPanel implements ChangeListener {
    private static final long serialVersionUID = 1L;
    MyLabel l;
    JSlider s;
    float factor; // Apply to int value returned by slider.

    class MyLabel extends JPanel {
        private static final long serialVersionUID = 1L;
        JLabel titleL, valueL;

        MyLabel(String txt) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            // slider title on the left
            titleL = new JLabel(txt + ": ");
            titleL.setAlignmentX(LEFT_ALIGNMENT);
            add(titleL);
            // spacer
            JLabel dummy = new JLabel();
            dummy.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));
            add(dummy);
            // slider value on the right
            valueL = new JLabel();
            valueL.setAlignmentX(RIGHT_ALIGNMENT);
            add(valueL);
        }

        // The slider only returns int values.
        // So treat the int value as the multiple of something,
        // the multiple being determined by a factor.
        String makeNiceFactoredNumberFrom(int val) {
            float f = (float) val * factor;
            int i = (int) f; // Truncate value.
            if ((float) (f - i) > 0.0f) {
                // Get the power to which the factor rises the int value.
                int j = Math.abs((int) Math.round(Math.log10((double) factor)));
                // If the power is -2 only display two post decimal positions
                // etc.
                String fs = "%." + new Integer(j).toString() + "f";
                return String.format(fs, new Float(f));
            } else
                return new Integer((int) (val * factor)).toString();
        }

        void setValue(int v) {
            valueL.setText(makeNiceFactoredNumberFrom(v));
        }
    }

    public GeneratorSlider(String title, // slider title
            int min, int max, // minimum and maximum slider values
            int value, // initial value
            int ts, // tick spacing
            float fac // factor applied to int values returned by slider
    ) {
        super();
        factor = fac;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        setOpaque(true);
        // label with title and current value
        l = new MyLabel(title);
        l.setValue(value);
        l.setOpaque(false);
        add(l);
        // slider
        // map of labels
        Hashtable<Integer, JLabel> mol = new Hashtable<Integer, JLabel>();
        for (int i = min; i <= max; i = i + ts) {
            String txt = l.makeNiceFactoredNumberFrom(i);
            mol.put(new Integer(i), new JLabel(txt));
        }
        // the slider itself
        s = new JSlider(SwingConstants.HORIZONTAL, min, max, value);
        s.setLabelTable(mol);
        s.setMajorTickSpacing(ts);
        s.setPaintTicks(true);
        s.setPaintLabels(true);
        s.addChangeListener(this);
        s.setOpaque(false);
        add(s);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider sl = (JSlider) e.getSource();
        l.setValue(sl.getValue());
    }

    public float getSliderValue() {
        return s.getValue() * factor;
    }
}