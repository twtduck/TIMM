import java.util.ArrayList;

import javax.swing.ListSelectionModel;

public class ArtistFilterPanel extends FilterPanel {
	public static ArrayList<String> artists;
	public ArtistFilterPanel() {
		super("Artist");
		artists = new ArrayList<String>();
		for(Song s : FilteredResultsTable.allMusic) {
			String artist = s.getTags().getArtist();
			boolean hasArtist = false;
			for(String testArtist : artists) {
				if(testArtist.equals(artist)) {
					hasArtist = true;
					break;
				}
			}
			if(!hasArtist) {
				artists.add(artist);
			}
		}
	}
	
	@Override
	public void refreshFilterPanel() {
		artists.clear();
		artists.addAll(FilteredResultsTable.filteredResultsArtistList);
		table.clearNoRefresh();
		String[] wildcard = {"All artists"};
		table.addRowToEndNoRefresh(wildcard);
		for(int i = 0; i < artists.size(); i++) {
			String[] row = {artists.get(i)};
			table.addRowToEndNoRefresh(row);
		}
		table.refreshTable();
		ListSelectionModel listSelectionModel = table.table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new Listener.FilterListener());
		if(!FilterPanel.filterPanels.contains(this))
			FilterPanel.filterPanels.add(this);
		this.setVisible(false);
		this.repaint();
		this.setVisible(true);
	}
	
}
