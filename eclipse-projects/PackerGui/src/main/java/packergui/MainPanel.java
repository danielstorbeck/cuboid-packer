package packergui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import packergui.cubedisplay.CubeDisplay;
import packergui.cubegenerator.CubeGenerator;
import packergui.cubetable.CubeTable;
import packergui.helpticker.HelpTicker;

public class MainPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public MainPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // content panels on the tabbed pane
        CubeTable ct = new CubeTable();
        CubeGenerator cg = new CubeGenerator();
        // The box generator adds boxes to the box table
        // and therefore needs to access its model.
        cg.setBTM(ct.getBTM());
        CubeDisplay cd = new CubeDisplay();
        cd.setBTM(ct.getBTM());
        cd.setCTM(ct.getCTM());
        // tabbed pane
        JTabbedPane tp = new JTabbedPane();
        tp.addTab("List of boxes", ct);
        tp.addTab("Box generator", cg);
        tp.addTab("Box display", cd);
        tp.addChangeListener(new TabChangeListener());
        add(tp);
        // help ticker
        add(HelpTicker.getInstance());
    }
}

class TabChangeListener implements ChangeListener {
    int tab = -1;

    public void stateChanged(ChangeEvent e) {
        JTabbedPane p = (JTabbedPane) e.getSource();
        int i = p.getSelectedIndex();
        if (i != tab) {
            tab = i;
            switch (i) {
            case 0:
                HelpTicker.switchToListTab();
                break;
            case 1:
                HelpTicker.switchToGeneratorTab();
                break;
            case 2:
                HelpTicker.switchToDisplayTab();
                break;
            }
        }
    }
}
