import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class DynamicTable extends JPanel {
	public JTable table;
	private ArrayList<String[]> rows;
	private String[] columns;
	private GridBagConstraints c;
	public DynamicTable(String[] columns, String firstRow) {
		super();
		rows = new ArrayList<String[]>();
		this.columns = columns;
		this.setLayout(new GridBagLayout());
		this.c = new GridBagConstraints();
		String[] row = {firstRow};
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
		rows.remove(rows.size() - 1);
		refreshTable();
	}
	
	public void clear() {
		while(rows.size() > 0) {
			removeLastRow();
		}
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
		this.table = new JTable(model);
		this.table.setFillsViewportHeight(true);
		/*
		this.table.getColumnModel().getColumn(0).setPreferredWidth(15);
		if(this.table.getColumnCount() > 1) {
			this.table.getColumnModel().getColumn(5).setPreferredWidth(30);
		}
		*/
		JScrollPane tableScroller = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		c.weightx = 1;
		c.weighty = 1;
		tableScroller.setPreferredSize(new Dimension(this.getPreferredSize().width - 20, this.getPreferredSize().height - 20));
		this.removeAll();
		this.add(tableScroller,c);
	}
}
