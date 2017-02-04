import java.awt.Dimension;

public class FilteredResultsPanel extends DynamicTable {

	public FilteredResultsPanel(String[] columns) {
		super(columns);
		// TODO Auto-generated constructor stub
		this.table.setPreferredSize(new Dimension(this.getPreferredSize().width,(int) (Library.filteredResults.size()*16)));

	}

}
