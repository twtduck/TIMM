import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class FilterPanel extends JPanel {
	public DynamicTable table;
	public String columnName;
	public GridBagConstraints constraints;
	public FilterPanel(String columnName) {
		super();
		String spacedColumnName = "  " + columnName;
		this.constraints = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		String[] col = {columnName};
		this.table = new DynamicTable(col);
		String[] firstRow = {"All " + columnName.toLowerCase() + "s (" + Library.filteredResults.size() + ")"};
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
		this.add(table, this.constraints);
	}
}
