package packergui.cubetable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class BoxTableCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    int[] tooBig = new int[] {};
    BoxTableModel bm;

    public void setBoxModel(BoxTableModel b) {
        bm = b;
    }

    public void setTooBigIndices(int[] ai) {
        tooBig = ai;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        setValue(value);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
            setBorder(border);
        } else {
            setBorder(noFocusBorder);
        }
        boolean tb = false;
        for (int r : tooBig) {
            if (r == row) {
                tb = true;
            }
        }
        if (tb) {
            if (isSelected) {
                setBackground(Color.PINK);
            } else {
                setBackground(Color.red);
            }
        }
        return this;
    }
}
