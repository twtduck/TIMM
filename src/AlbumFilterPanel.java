import java.util.ArrayList;

import javax.swing.ListSelectionModel;

public class AlbumFilterPanel extends FilterPanel {
	public static ArrayList<String> albums;
	public AlbumFilterPanel() {
		super("Album");
		albums = new ArrayList<String>();
		for(Song s : FilteredResultsTable.allMusic) {
			String album = s.getTags().getAlbum();
			boolean hasAlbum = false;
			for(String testAlbum : albums) {
				if(testAlbum.equals(album)) {
					hasAlbum = true;
					break;
				}
			}
			if(!hasAlbum) {
				albums.add(album);
			}
		}
	}
	
	public void refreshAlbums() {
		albums.clear();
		albums = FilteredResultsTable.filteredResultsAlbumList;
		for(int i = 0; i < albums.size(); i++) {
			String[] row = {albums.get(i)};
			table.addRowToEndNoRefresh(row);
		}
		table.refreshTable();
		ListSelectionModel listSelectionModel = table.table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new Listener.FilterListener());
	}
	
}
