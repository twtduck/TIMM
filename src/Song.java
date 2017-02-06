import java.io.File;

public class Song extends Thread {
	private TagReader tags;
	private File mediaFile;
	private SongPlayer player;
	public int libraryIndex;
	public Song(File mediaFile) {
		this.mediaFile = mediaFile;
		this.tags = new TagReader(this);
		this.tags.load();
		this.player = new SongPlayer(this);
	}
	
	public Song(String dataString) {
		this.libraryIndex = Integer.parseInt(dataString.substring(0, dataString.indexOf("::")));
		dataString = dataString.substring(dataString.indexOf("::") + 2);
		this.mediaFile = new File(dataString.substring(0, dataString.indexOf("::")));
		this.tags = new TagReader(this);
		this.tags.importTags(dataString.substring(dataString.indexOf("::") + 2));
		this.player = new SongPlayer(this);
	}
	
	public TagReader getTags() {
		return this.tags;
	}
	
	public File getFile() {
		return this.mediaFile;
	}

	public String export() {
		return libraryIndex + "::" + this.mediaFile.getAbsolutePath() + "::" + this.tags.exportTags();
		
	}

	public SongPlayer getPlayer() {
		return this.player;
	}
}
