import java.util.ArrayList;

public class GenreFilterPanel extends FilterPanel {
	public static ArrayList<String> genres;
	public GenreFilterPanel() {
		super("Genre");
		genres = new ArrayList<String>();
		for(Song s : Library.filteredResults) {
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
		System.out.println("Number of genres: " + genres.size());
	}
	
	public void refreshGenres() {
		genres.clear();
		for(Song s : Library.filteredResults) {
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
		System.out.println("Number of genres: " + genres.size());
		for(int i = 0; i < genres.size(); i++) {
			String[] row = {genres.get(i)};
			table.addRowToEnd(row);
		}
	}

}
