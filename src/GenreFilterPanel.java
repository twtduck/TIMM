import java.util.ArrayList;

import javax.swing.ListSelectionModel;

public class GenreFilterPanel extends FilterPanel {
	public static ArrayList<String> genres;
	public GenreFilterPanel() {
		super("Genre");
		genres = new ArrayList<String>();
		for(Song s : FilteredResultsTable.allMusic) {
			String genre = s.getTags().getGenre();
			boolean hasGenre = false;
			for(String testGenre : genres) {
				if(testGenre.equals(genre)) {
					hasGenre = true;
					break;
				}
			}
			if(!hasGenre) {
				genres.add(genre);
			}
		}
	}
	
	public void refreshGenres() {
		genres.clear();
		genres = FilteredResultsTable.filteredResultsGenreList;
		for(int i = 0; i < genres.size(); i++) {
			String[] row = {genres.get(i)};
			table.addRowToEndNoRefresh(row);
		}
		table.refreshTable();
		ListSelectionModel listSelectionModel = table.table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new Listener.FilterListener());
	}
	

}
