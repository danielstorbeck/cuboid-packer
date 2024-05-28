package packergui;

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

class Messages {
    static String BOX_LIST_MESSAGE = " To edit table cells double click on them, edit them, and then press return. "
            + " To add more lines to the table, right click on the present lines,"
            + " or on the table's title. "
            + " To generate lines with random box definitions, go to the box generator tab. "
            + " To select lines just point, click and drag. "
            + " To delete selected lines, press control backspace. "
            + " A line turns red when the box does not fit into the container. ";
    static String BOX_GENERATOR_MESSAGE = " Adjust the no. of boxes, min. and max. edge lengths,"
            + " cubeness, and press the generate button. "
            + " Cubeness is the tendency of all edges to have equal length. ";
    static String BOX_DISPLAY_MESSAGE = " To generate the three-dimensional representation of the boxes"
            + " press the display button. "
            + " When the result is displayed, point, click and drag on the image"
            + " to rotate the container. ";
    static String PLEASE_WAIT_MESSAGE = " The graphical representation is being calculated. "
            + " Please wait. ";
}

class Ticker implements Runnable {
    String mess;
    JLabel lab;
    String str;

    Ticker(String s, JLabel l) {
        mess = s;
        lab = l;
        str = "  " + mess.substring(0);
    }

    @Override
    public void run() {
        lab.setText(str);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return;
        }
        while (!Thread.interrupted()) {
            while (str.length() < 500) {
                str = str + " *** " + mess.substring(0);
            }
            str = str.substring(1);
            lab.setText(str);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
