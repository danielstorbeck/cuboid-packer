package packergui;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.Color;

import box.OrderedDimensions;
import misc.Observable;
import misc.Observer;
import misc.Event;

public class CubeTable extends JPanel {
    private static final long serialVersionUID = 1L;
    JTable contTable; // for container dimensions
    JTable boxTable; // for dimensions of boxes
    BoxTableModel btm;
    ContTableModel ctm;

    public CubeTable() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // title for container measures
        JLabel cl = new JLabel("Measures of the container");
        cl.setAlignmentX(CENTER_ALIGNMENT);
        add(cl);
        // listener for container and box tables
        TableDataListener tdl = new TableDataListener();
        // table for container measures
        ctm = new ContTableModel();
        tdl.setContModel(ctm);
        ctm.addTableModelListener(tdl);
        contTable = new JTable(ctm);
        // Add table's header to the lay.man. separately.
        JTableHeader contTableHeader = contTable.getTableHeader();
        add(contTableHeader);
        add(contTable);
        // title for table of boxes' measures
        JLabel bl = new JLabel("Measures of the boxes");
        bl.setAlignmentX(CENTER_ALIGNMENT);
        add(bl);
        // scrollable table for boxes' dimensions
        btm = new BoxTableModel();
        tdl.setBoxModel(btm);
        btm.addTableModelListener(tdl);
        boxTable = new JTable(btm);
        BoxTableCellRenderer bcr = new BoxTableCellRenderer();
        bcr.setBoxModel(btm);
        boxTable.setDefaultRenderer(Float.class, bcr);
        tdl.setBoxTableCellRenderer(bcr);
        tdl.setBoxTable(boxTable);
        btm.setTable(boxTable);
        boxTable.addMouseListener(new RowCreationListener(btm));
        boxTable.addKeyListener(new RowDeletionListener(btm));
        boxTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // The scroll pane displays the table header automatically.
        add(new JScrollPane(boxTable));
        addMouseListener(new RowCreationListener(btm));
    }

    protected BoxTableModel getBTM() {
        return btm;
    }

    protected ContTableModel getCTM() {
        return ctm;
    }
}

class ContTableModel extends AbstractTableModel implements Observable {
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

class BoxTableModel extends AbstractTableModel implements Observable {
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

class RowCreationListener extends MouseAdapter {
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

class RowDeletionListener extends KeyAdapter {
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

class TableDataListener implements TableModelListener {
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

class BoxTableCellRenderer extends DefaultTableCellRenderer {
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
