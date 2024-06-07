package packergui.cubetable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RowCreationListener extends MouseAdapter {
    BoxTableModel tableModel;

    public RowCreationListener(BoxTableModel btm) {
        tableModel = btm;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
            tableModel.addNewRow();
    }
}
