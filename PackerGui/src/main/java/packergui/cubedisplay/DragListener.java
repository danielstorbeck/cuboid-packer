package packergui.cubedisplay;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

public class DragListener extends MouseInputAdapter {
    private final GLPanel glPanel;
    DragListener(GLPanel glPanel)
    {
        super();
        this.glPanel = glPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        glPanel.setSPoint(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        glPanel.setEPoint(e.getPoint());
        glPanel.display();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        glPanel.resetSPoint();
        glPanel.resetEPoint();
        glPanel.display();
    }
}

