import java.util.ArrayList;

public class SongList {
	public String yearFilter = "*";
	public String genreFilter = "*";
	public String artistFilter = "*";
	public String albumFilter = "*";
	
	private DynamicTable displayTable;
	
	public SongList() {
		String[] columns = {"Track Num", "Title", "Genre", "Artist", "Album", "Year", "Duration"};
		displayTable = new DynamicTable(columns);
		
	}
}
