package packergui.helpticker;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class HelpTicker extends JPanel {
    private static final long serialVersionUID = 1L;
    static HelpTicker ht;
    JLabel infoLabel = new JLabel();
    Thread currentT = null;

    private HelpTicker() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        // tag on the left that says "info"
        JLabel l = new JLabel("Info: ");
        add(l);
        // text field that displays help messages
        infoLabel.setMaximumSize( // Make it as wide as possible.
                new Dimension(Short.MAX_VALUE, l.getPreferredSize().height));
        add(infoLabel);
    }

    // factory method to get singleton
    public static HelpTicker getInstance() {
        if (ht == null)
            ht = new HelpTicker();
        return ht;
    }

    public static void setInfoText(String s) {
        ht.infoLabel.setText(s);
    }

    public static void switchToListTab() {
        ht.putMessage(Messages.BOX_LIST_MESSAGE);
    }

    public static void switchToGeneratorTab() {
        ht.putMessage(Messages.BOX_GENERATOR_MESSAGE);
    }

    public static void switchToDisplayTab() {
        ht.putMessage(Messages.BOX_DISPLAY_MESSAGE);
    }

    public static void displayWaitMessage() {
        ht.putMessage(Messages.PLEASE_WAIT_MESSAGE);
    }

    void putMessage(String s) {
        if (currentT != null) {
            currentT.interrupt();
        }
        Ticker t = new Ticker(s, infoLabel);
        currentT = new Thread(t);
        currentT.start();
    }

    void stopTicker() {
        if (currentT != null) {
            currentT.interrupt();
            currentT = null;
        }
    }

    public static void putStillMessage(String s) {
        ht.stopTicker();
        HelpTicker.setInfoText(s);
    }
}
