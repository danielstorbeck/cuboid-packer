package packergui.cubetable;

import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import box.OrderedDimensions;
import misc.Event;
import misc.Observable;
import misc.Observer;

public class ContTableModel extends AbstractTableModel implements Observable {
    private static final long serialVersionUID = 1L;
    String[] contColNames = { "Width", "Height", "Depth" };
    float[] contData = { 2.0f, 2.0f, 2.0f }; // default dimensions
    Set<Observer> so = new HashSet<Observer>();

    public class ContentChangedEvent extends Event {
    }

    public void addObserver(Observer o) {
        so.add(o);
    }

    void notifyObservers() {
        for (Observer o : so)
            o.notifyAbout(new ContentChangedEvent());
    }

    public int getColumnCount() {
        return contColNames.length;
    }

    public int getRowCount() {
        return 1; // only one row, always
    }

    @Override
    public String getColumnName(int col) {
        return contColNames[col];
    }

    public Object getValueAt(int row, int col) {
        return contData[col];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return Float.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (row == 0)
            return true;
        else
            return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (contData[col] != (Float) value) {
            contData[col] = (Float) value;
            notifyObservers();
            fireTableCellUpdated(row, col);
        }
    }

    protected float[] getMeasures() {
        return contData;
    }

    public OrderedDimensions getContainerDims() {
        return new OrderedDimensions(contData);
    }
}
