package packergui.cubetable;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RowDeletionListener extends KeyAdapter {
    BoxTableModel tableModel;
    boolean ctrlDown = false;

    public RowDeletionListener(BoxTableModel btm) {
        tableModel = btm;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (ctrlDown && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            tableModel.deleteSelectedRows();
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL)
            ctrlDown = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL)
            ctrlDown = false;
    }
}

