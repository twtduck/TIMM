import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class FilterPanel extends JPanel {
	public DynamicTable table;
	public String columnName;
	public GridBagConstraints constraints;

	public FilterPanel(String columnName) {
		super();
		this.constraints = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		String[] col = { columnName };
		this.table = new DynamicTable(col);
		String[] firstRow = { "All " + columnName.toLowerCase() + "s" };
		table.addRowToEndNoRefresh(firstRow);
		this.constraints.anchor = GridBagConstraints.CENTER;
		this.constraints.fill = GridBagConstraints.BOTH;
		this.constraints.insets = new java.awt.Insets(0, 0, 0, 0);
		this.constraints.gridheight = 1;
		this.constraints.gridwidth = 1;
		this.constraints.gridx = 0;
		this.constraints.gridy = 0;
		this.constraints.weightx = 1;
		this.constraints.weighty = 1;
		table.refreshTable();
		table.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(table, this.constraints);
	}

	public String getSelectedRow() {
		return table.rows.get(table.table.getSelectionModel().getMinSelectionIndex())[0];
	}
}
