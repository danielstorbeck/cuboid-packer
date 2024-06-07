package packergui.cubegenerator;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GeneratorSlider extends JPanel implements ChangeListener {
    private static final long serialVersionUID = 1L;
    SliderLabel l;
    JSlider s;
    float factor; // Apply to int value returned by slider.

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
        l = new SliderLabel(title, factor);
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