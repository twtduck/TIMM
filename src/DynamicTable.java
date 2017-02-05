import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class DynamicTable extends JPanel {
	public JTable table;
	public  ArrayList<String[]> rows;
	private String[] columns;
	private GridBagConstraints c;
	private String[] selectedRow;
	private boolean hasBeenRefreshed;
	public DynamicTable(String[] columns, String firstRow) {
		super();
		rows = new ArrayList<String[]>();
		this.columns = columns;
		this.setLayout(new GridBagLayout());
		this.c = new GridBagConstraints();
		String[] row = {firstRow};
		this.hasBeenRefreshed = false;
		addRowToEndNoRefresh(row);
	}
	
	public DynamicTable(String[] columns) {
		super();
		rows = new ArrayList<String[]>();
		this.columns = columns;
		this.setLayout(new GridBagLayout());
		this.c = new GridBagConstraints();
	}
	
	public void addRowToEnd(String[] row) {
		rows.add(row);
		refreshTable();
	}
	
	public void addRowToEndNoRefresh(String[] row) {
		rows.add(row);
	}
	
	public void addRowToBeginning(String[] row) {
		rows.add(0, row);
		refreshTable();
	}
	
	public void removeFirstRow() {
		rows.remove(0);
		refreshTable();
	}
	
	public void removeLastRow() {
		removeLastRowNoRefresh();
		refreshTable();
	}
	
	public void removeLastRowNoRefresh() {
		rows.remove(rows.size() - 1);
	}
	
	public void clearNoRefresh() {
		while(rows.size() > 0) {
			removeLastRowNoRefresh();
		}
	}
	
	public void clear() {
		clearNoRefresh();
		refreshTable();
	}
	
	public String[] getColumns() {
		return this.columns;
	}
	
	public void refreshTable() {
		
		this.c.fill = GridBagConstraints.BOTH;
		this.c.anchor = GridBagConstraints.CENTER;
		//this.setBorder(BorderFactory.createLineBorder(Color.green, 4));
		String[][] tableContents = new String[rows.size()][columns.length];
		for(int row = 0; row < rows.size(); row++) {
			tableContents[row] = rows.get(row);
		}
		TableModel model = new DefaultTableModel(tableContents, columns) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		if(this.hasBeenRefreshed)
			this.selectedRow = rows.get(this.table.getSelectionModel().getMinSelectionIndex());
		this.table = new JTable(model);
		this.table.setFillsViewportHeight(true);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(15);
		if(this.table.getColumnCount() > 1) {
			this.table.getColumnModel().getColumn(5).setPreferredWidth(30);
		}
		JScrollPane tableScroller = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		c.weightx = 1;
		c.weighty = 1;
		tableScroller.setPreferredSize(new Dimension(this.getPreferredSize().width - 20, this.getPreferredSize().height - 20));
		this.removeAll();
		this.add(tableScroller,c);
		if(hasBeenRefreshed) {
			for(int rowNum = 0; rowNum < rows.size(); rowNum++) {
				boolean matches = true;
				for(int i = 0; i < rows.get(rowNum).length; i++) {
					if(!rows.get(rowNum)[i].equals(this.selectedRow[i])) {
						matches = false;
						break;
					}
				}
				if(matches) {
					table.setRowSelectionInterval(rowNum, rowNum);
					break;
				}
			}
		} else {
			table.setRowSelectionInterval(0, 0);

		}
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.hasBeenRefreshed = true;
	}
}
