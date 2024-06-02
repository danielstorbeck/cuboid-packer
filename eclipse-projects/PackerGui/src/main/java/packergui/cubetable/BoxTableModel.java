package packergui.cubetable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import box.OrderedDimensions;
import misc.Event;
import misc.Observable;
import misc.Observer;

public class BoxTableModel extends AbstractTableModel implements Observable {
    private static final long serialVersionUID = 1L;
    String[] boxColNames = { "Width", "Height", "Depth", "Number" };
    List<Vector<Number>> boxData;
    JTable table;
    Set<Observer> so = new HashSet<Observer>();

    public class ContentChangedEvent extends Event {
    }

    public BoxTableModel() {
        super();
        boxData = new ArrayList<Vector<Number>>();
        // Add four rows of identical boxes.
        for (int i = 0; i < 4; i++) {
            Vector<Number> vn = new Vector<Number>();
            for (int j = 0; j < 4; j++) {
                if (j < 3)
                    vn.add(new Float(1.0));
                else
                    vn.add(new Integer(1));
            }
            boxData.add(vn);
        }
        // Add one row of four identical boxes.
        Vector<Number> vn = new Vector<Number>();
        vn.add(new Float(1.0));
        vn.add(new Float(1.0));
        vn.add(new Float(1.0));
        vn.add(new Integer(4));
        boxData.add(vn);
    }

    public void setTable(JTable t) {
        table = t;
    }

    public void addObserver(Observer o) {
        so.add(o);
    }

    void notifyObservers() {
        for (Observer o : so)
            o.notifyAbout(new ContentChangedEvent());
    }

    public int getColumnCount() {
        return boxColNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return boxColNames[col];
    }

    public int getRowCount() {
        return boxData.size();
    }

    public Object getValueAt(int row, int col) {
        return boxData.get(row).get(col);
    }

    @Override
    public Class<?> getColumnClass(int c) {
        if (c < 3)
            return Float.class;
        else
            return Integer.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Vector<Number> vn = boxData.get(row);
        if (col < 3) {
            if (vn.get(col).floatValue() != ((Float) value).floatValue()) {
                vn.setElementAt((Float) value, col);
                notifyObservers();
                fireTableCellUpdated(row, col);
            }
        } else {
            if (vn.get(col).intValue() != ((Integer) value).intValue()) {
                vn.setElementAt((Integer) value, col);
                notifyObservers();
                fireTableCellUpdated(row, col);
            }
        }
    }

    public void addNewRow() {
        Vector<Number> vn = new Vector<Number>();
        vn.add(new Float(0.0));
        vn.add(new Float(0.0));
        vn.add(new Float(0.0));
        vn.add(new Integer(0));
        boxData.add(vn);
        fireTableDataChanged();
    }

    public void addNewRows(List<Vector<Number>> lvn) {
        for (Vector<Number> vn : lvn) {
            boxData.add(vn);
        }
        fireTableDataChanged();
        notifyObservers();
    }

    public void deleteSelectedRows() {
        int[] rows = table.getSelectedRows();
        ArrayList<Vector<Number>> avn = new ArrayList<Vector<Number>>();
        // First collect the references.
        for (int i : rows) {
            avn.add(boxData.get(i));
        }
        // Then remove by reference, not by index!
        // Removing by index changes subsequent indices.
        for (Vector<Number> vn : avn) {
            boxData.remove(vn);
        }
        fireTableDataChanged();
        notifyObservers();
    }

    protected void fireRowChanged(int i) {
        fireTableRowsUpdated(i, i);
    }

    protected List<Vector<Number>> getDataRows() {
        return boxData;
    }

    public List<OrderedDimensions> getBoxDims() {
        List<OrderedDimensions> ab = new ArrayList<OrderedDimensions>();
        OrderedDimensions b;
        int n = 0;
        for (Vector<Number> vn : boxData) {
            float[] ff = new float[] { (Float) vn.get(0), (Float) vn.get(1), (Float) vn.get(2) };
            for (int i = (Integer) vn.get(3); i > 0; i--) {
                b = new OrderedDimensions(ff);
                b.setID(n);
                ab.add(b);
            }
            n++;
        }
        return ab;
    }
}
