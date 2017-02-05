import java.util.ArrayList;

public class FilteredResultsTable extends DynamicTable {
	public static ArrayList<Song> allMusic = new ArrayList<Song>();
	public static ArrayList<Song> filteredResults = new ArrayList<Song>();
	public static ArrayList<String> filteredResultsYearList = new ArrayList<String>();
	public static ArrayList<String> filteredResultsGenreList = new ArrayList<String>();
	public static ArrayList<String> filteredResultsArtistList = new ArrayList<String>();
	public static ArrayList<String> filteredResultsAlbumList= new ArrayList<String>();

	public FilteredResultsTable(String[] columns) {
		super(columns);
	}

	public void refresh() {
		System.out.println("Refreshing filteredResultsTable.");
		String yearFilter = GUI.yearFilterPanel.getSelectedRow();
		String genreFilter = GUI.genreFilterPanel.getSelectedRow();
		System.out.println(genreFilter);
		String artistFilter = GUI.artistFilterPanel.getSelectedRow();
		String albumFilter = GUI.albumFilterPanel.getSelectedRow();
		System.out.println("AllMusic size:" + allMusic.size());
		clearNoRefresh();
		filteredResults.clear();
		filteredResultsYearList.clear();
		filteredResultsGenreList.clear();
		filteredResultsArtistList.clear();
		filteredResultsAlbumList.clear();
		
		for (Song s : allMusic) {
			boolean yearGood = yearFilter.equals(s.getTags().getYearString()) || yearFilter.equals("All years"); 
			boolean genreGood = genreFilter.equals(s.getTags().getGenre()) || genreFilter.equals("All genres");
			boolean artistGood = artistFilter.equals(s.getTags().getArtist()) || artistFilter.equals("All artists");
			boolean albumGood = albumFilter.equals(s.getTags().getAlbum()) || albumFilter.equals("All albums");
			
			//System.out.println(s.getTags().getTitle() + ": Year:" + yearGood + " Genre:" + genreGood + " Artist:" + artistGood + " Album:" + albumGood);
			
			if(genreGood && artistGood && albumGood) 
				addToListIfAbsent(s.getTags().getYearString(), filteredResultsYearList);
			if(yearGood && artistGood && albumGood) {
				addToListIfAbsent(s.getTags().getGenre(), filteredResultsGenreList);
			}
				
			if(yearGood && genreGood && albumGood) 
				addToListIfAbsent(s.getTags().getArtist(), filteredResultsArtistList);
			if(yearGood && genreGood && artistGood) 
				addToListIfAbsent(s.getTags().getAlbum(), filteredResultsAlbumList);
			if(yearGood && genreGood && artistGood && albumGood) {
				System.out.println("Added to filtered results: " + s.getTags().getTitle());
				filteredResults.add(s);
			}
				
			
			
		}
		for(Song track : filteredResults) {
			String[] row = { track.getTags().getTrackNumString(), track.getTags().getTitle(),
					track.getTags().getGenre(), track.getTags().getArtist(), track.getTags().getAlbum(),
					track.getTags().getDurationString() };
			addRowToEndNoRefresh(row);
		}
		refreshTable();
	}
	
	private void addToListIfAbsent(String s, ArrayList<String> list) {
		for(String test : list) {
			if(test.equals(s))
				return;
		}
		list.add(s);
	}
	
	public static void refreshFilters() {
		GUI.yearFilterPanel.refreshYears();
		GUI.genreFilterPanel.refreshGenres();
		GUI.artistFilterPanel.refreshArtists();
		GUI.albumFilterPanel.refreshAlbums();
	}

}
