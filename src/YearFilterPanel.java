import java.util.ArrayList;

import javax.swing.ListSelectionModel;

public class YearFilterPanel extends FilterPanel {
	public static ArrayList<String> years;
	public YearFilterPanel() {
		super("Year");
		years = new ArrayList<String>();
		for(Song s : FilteredResultsTable.allMusic) {
			String year = s.getTags().getYearString();
			boolean hasYear = false;
			for(String testYear : years) {
				if(testYear.equals(year)) {
					hasYear = true;
					break;
				}
			}
			if(!hasYear) {
				years.add(year);
			}
		}
	}
	
	public void refreshYears() {
		years.clear();
		years = FilteredResultsTable.filteredResultsYearList;
		for(int i = 0; i < years.size(); i++) {
			String[] row = {years.get(i)};
			table.addRowToEndNoRefresh(row);
		}
		table.refreshTable();
		ListSelectionModel listSelectionModel = table.table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new Listener.FilterListener());
	}
	
}
