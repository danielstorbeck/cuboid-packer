package packergui.cubegenerator;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SliderLabel extends JPanel {
    private static final long serialVersionUID = 1L;
    JLabel titleL, valueL;
    float factor; // Apply to int value returned by slider.

    SliderLabel(String txt, float factor) {
        super();
        this.factor = factor;
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

