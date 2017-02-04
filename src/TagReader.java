import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.tika.parser.mp3.Mp3Parser;

public class TagReader {
	private Song song;
	private String title;
	private String artist;
	private String album;
	private int year;
	private int trackNum;
	private String genre;
	private String comment;
	private String albumArtist;
	private String composer;
	private int discNum;
	private double duration;

	public TagReader(Song song) {
		this.song = song;
	}

	public void load() {
		InputStream input = null;
		try {
			input = new FileInputStream(song.getFile());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		DefaultHandler handler = new DefaultHandler();
		Metadata metadata = new Metadata();
		Parser parser = new Mp3Parser();
		ParseContext parseCtx = new ParseContext();
		try {
			parser.parse(input, handler, metadata, parseCtx);
			input.close();
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		}

		this.title = metadata.get("title");

		if ((this.title == null) || (this.title.equals(""))) {
			this.title = "Unknown Title";
		}

		this.artist = metadata.get("xmpDM:artist");
		if ((this.artist == null) || (this.artist.equals(""))) {
			this.artist = "Unknown Artist";
		}

		this.album = metadata.get("xmpDM:album");
		if ((this.album == null) || (this.album.equals(""))) {
			this.album = "Unknown Album";
		}
		String yearString = metadata.get("xmpDM:releaseDate");
		try {
			this.year = Integer.parseInt(yearString);
		} catch (Exception e) {
			this.year = Integer.MIN_VALUE;
		}
		String trackNumString = metadata.get("xmpDM:trackNumber");
		try {
			this.trackNum = Integer.parseInt(trackNumString);
		} catch (Exception e) {
			this.trackNum = Integer.MIN_VALUE;
		}
		this.genre = metadata.get("xmpDM:genre");
		if (this.genre == null) {
			this.genre = "Unknown Genre";
		}

		this.comment = metadata.get("xmpDM:comment");
		if (this.comment == null) {
			this.comment = "";
		}
		this.comment = this.comment.replaceAll("/n", "");

		this.albumArtist = metadata.get("xmpDM:albumArtist");
		if (this.albumArtist == null) {
			this.albumArtist = "";
		}

		this.composer = metadata.get("xmpDM:composer");
		if (this.composer == null) {
			if (this.artist.equals("Unknown Artist")) {
				this.composer = "Unknown Composer";
			} else {
				this.composer = this.artist;
			}
		}

		String discNumString = metadata.get("xmpDM:discNumber");
		try {
			this.discNum = Integer.parseInt(discNumString);
		} catch (Exception e) {
			this.discNum = 1;
		}

		this.duration = Double.parseDouble(metadata.get("xmpDM:duration"));
	}

	public String getTitle() {
		return this.title;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getAlbum() {
		return this.album;
	}

	public int getYearInt() {
		return this.year;
	}

	public int getTrackNumInt() {
		return this.trackNum;
	}

	public String getGenre() {
		return this.genre;
	}

	public String getComment() {
		return this.comment;
	}

	public String getAlbumArtist() {
		return this.albumArtist;
	}

	public String getComposer() {
		return this.composer;
	}

	public int getDiscNumInt() {
		return this.discNum;
	}

	public double getDuration() {
		return this.duration;
	}

	public void importTags(String tagString) {
		title = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		artist = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		album = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		year = Integer.parseInt(tagString.substring(0, tagString.indexOf("::")));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		trackNum = Integer.parseInt(tagString.substring(0, tagString.indexOf("::")));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		genre = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		comment = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		albumArtist = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		composer = tagString.substring(0, tagString.indexOf("::"));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		discNum = Integer.parseInt(tagString.substring(0, tagString.indexOf("::")));
		tagString = tagString.substring(tagString.indexOf("::") + 2);
		duration = Double.parseDouble(tagString);
	}

	public String exportTags() {
		return title + "::" + artist + "::" + album + "::" + year + "::" + trackNum + "::" + genre + "::" + comment
				+ "::" + albumArtist + "::" + composer + "::" + discNum + "::" + duration;
	}
	
	public String getYearString() {
		if(year == Integer.MIN_VALUE)
			return "Unknown Year";
		return Integer.toString(year);
	}
	
	public String getTrackNumString() {
		if(trackNum == Integer.MIN_VALUE)
			return "";
		return Integer.toString(trackNum);
	}
	
	public String getDiscNumString() {
		return "Disc " + Integer.toString(discNum);
	}
	
	public String getDurationString() {
		int secs = (int) (duration / 1000) % 60; 
		int mins = (int) (duration / (1000 * 60) % 60);
		int hours = (int) (duration / (1000 * 60 * 60) % 60);
		return is(hours) + ":" + is(mins) + ":" + is(secs);
	}
	
	private static String is(int i) {
		String temp = Integer.toString(i);
		if (i < 10) {
			temp = "0" + temp;
		}
		if(i == Integer.MIN_VALUE)
			return "";
		return temp;
	}

}
