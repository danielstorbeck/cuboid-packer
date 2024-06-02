package packergui.cubetable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

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

    public BoxTableModel getBTM() {
        return btm;
    }

    public ContTableModel getCTM() {
        return ctm;
    }
}
