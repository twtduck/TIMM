import java.util.ArrayList;

public class Playlist {
	
	private ArrayList<Song> songs;
	public Playlist() {
		this.songs = new ArrayList<Song>();
	}
	
	public void clear() {
		this.songs.clear();
	}
	
	public ArrayList<Song> getSongs() {
		return this.songs;
	}
}
