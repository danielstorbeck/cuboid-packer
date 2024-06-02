package packergui.cubetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import box.OrderedDimensions;

public class TableDataListener implements TableModelListener {
    ContTableModel cmod;
    BoxTableModel bmod;
    JTable btab;
    BoxTableCellRenderer btcr;

    public void tableChanged(TableModelEvent e) {
        OrderedDimensions box;
        OrderedDimensions cont = new OrderedDimensions(cmod.getMeasures());
        int i = 0;
        List<Vector<Number>> avn = bmod.getDataRows();
        ArrayList<Integer> atb = new ArrayList<Integer>();
        for (Vector<Number> vn : avn) {
            box = new OrderedDimensions(new float[] { (Float) vn.get(0), (Float) vn.get(1), (Float) vn.get(2) });
            if (!box.fitsIn(cont)) {
                atb.add(new Integer(i));
            }
            i++;
        }
        int s = atb.size();
        if (s > 0) {
            int j = 0;
            int[] ai = new int[s];
            for (Integer idx : atb) {
                ai[j] = idx;
                j++;
            }
            btcr.setTooBigIndices(ai);
        } else {
            btcr.setTooBigIndices(new int[] {});
        }
        if (e.getSource().getClass() == BoxTableModel.class && e.getFirstRow() == e.getLastRow()) {
            final int start = e.getFirstRow();
            final int end = e.getLastRow();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    bmod.fireTableRowsUpdated(start, end);
                }
            });
        } else if (e.getSource().getClass() == ContTableModel.class) {
            final int start = 0;
            final int end = avn.size();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    bmod.fireTableRowsUpdated(start, end);
                }
            });
        }
    }

    public void setContModel(ContTableModel c) {
        cmod = c;
    }

    public void setBoxModel(BoxTableModel b) {
        bmod = b;
    }

    public void setBoxTable(JTable bt) {
        btab = bt;
    }

    public void setBoxTableCellRenderer(BoxTableCellRenderer r) {
        btcr = r;
    }
}
