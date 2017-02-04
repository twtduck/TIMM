	import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Library {
	public static int IMPORT_INDEX = 0;
	public static int IMPORT_TOTAL = 0;
	public static boolean updateInterrupt = false;
	public static boolean libraryIsUpdating = false;

	final static File LIB_FILE = new File(System.getProperty("user.home") + "/.local/share/wmm-library.db");
	public static ArrayList<File> locations = new ArrayList<File>();
	public static ArrayList<Artist> music = new ArrayList<Artist>();
	public static ArrayList<Song> filteredResults = new ArrayList<Song>();
	public static class Disc {
		public int discNum;
		public ArrayList<Song> tracks;
		public Disc(int discNum) {
			this.discNum = discNum;
			this.tracks = new ArrayList<Song>();
		}
		
		public int compareTo(Disc o) {
			return(discNum - o.discNum);
		}
	}
	
	public static class Album {
		public String name;
		public String simpleName;
		public ArrayList<Disc> discs;
		
		public Album(String name) {
			this.name = name;
			this.simpleName = simplefy(name);
			this.discs = new ArrayList<Disc>();
		}
		
		public int compareTo(Album o) {
			return simpleName.compareTo(o.simpleName);
		}
	}
	
	public static class Artist {
		public String name;
		public String simpleName;
		public ArrayList<Album> albums;
		
		public Artist(String name) {
			this.name = name;
			this.simpleName = simplefy(name);
			this.albums = new ArrayList<Album>();
		}
		
		public int compareTo(Artist o) {
			return simpleName.compareTo(o.simpleName);
		}
	}
	
	public static void loadLibrary() {
		if(!LIB_FILE.exists()) {
			genDefaultLibraryFileAndLoadLibrary();
		} else {
			loadLibraryFromFile();
		}
		
	}
	
	public static void addSong(Song s) {
		String artistName = s.getTags().getArtist();
		boolean newArtist = true;
		for(Artist artist : music) {
			if(artist.name.equals(artistName)) {
				String albumName = s.getTags().getAlbum();
				boolean newAlbum = true;
				for(Album album : artist.albums) {
					if(album.name.equals(albumName)) {
						int discNum = s.getTags().getDiscNumInt();
						boolean newDisc = true;
						for(Disc disc : album.discs) {
							if(disc.discNum == discNum) {
								disc.tracks.add(s);
								newDisc = false;
								break;
							}
						}
						if(newDisc) {
							Disc disc = new Disc(discNum);
							disc.tracks.add(s);
							album.discs.add(disc);
						}
						newAlbum = false;
						break;
					}
					
				}
				if(newAlbum) {
					Album album = new Album(albumName);
					Disc disc = new Disc(s.getTags().getDiscNumInt());
					disc.tracks.add(s);
					album.discs.add(disc);
					artist.albums.add(album);
				}
				newArtist = false;
				break;
			}
		}
		if(newArtist) {
			Artist artist = new Artist(artistName);
			Album album = new Album(s.getTags().getAlbum());
			Disc disc = new Disc(s.getTags().getDiscNumInt());
			disc.tracks.add(s);
			album.discs.add(disc);
			artist.albums.add(album);
			music.add(artist);
		}
	}

	public void sort() {
		for(Artist artist : music) {
			for(Album album : artist.albums) {
				for(Disc disc : album.discs) {
					Collections.sort(disc.tracks, new Comparator<Song>() {
						@Override
						public int compare(Song song1, Song song2) {
							return 0;
						}
					});
				}
			}
		}
	}
	private static void loadLibraryFromFile() {
		try {
			Scanner libScanner = new Scanner(LIB_FILE);
			while(libScanner.hasNextLine()) {
				addSong(new Song(libScanner.nextLine()));
			}
			libScanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not find library file");
			System.exit(-1);
		}
	}

	private static void genDefaultLibraryFileAndLoadLibrary() {
		System.out.println("Generating default library file...");
		//locations.add(new File("/home/thomas/Music/Hans Zimmer/Gladiator/"));
		locations.add(new File(System.getProperty("user.home") + "/Music/"));
		updateLibrary();
	}
	
	public static void updateLibrary() {
		if(LIB_FILE.exists())
			LIB_FILE.delete();
		System.out.println("Call to update library registered");
		GUI.libraryUpdateGUI();
		libraryIsUpdating = true;
		try {
			class UpdateThread extends Thread {
				@Override
				public void run() {
					PrintWriter libFileWriter;
					try {
						libFileWriter = new PrintWriter(LIB_FILE);
						for(File f : locations) {
							ArrayList<File> mp3s = searchDirectory(f);
							IMPORT_TOTAL = mp3s.size();
							for(int i = 0; i < mp3s.size(); i++) {
								if(updateInterrupt) {
									break;
								}
								System.out.println("Getting song data for file " + (i + 1) + " of " + (mp3s.size() + 1));
								Song song = new Song(mp3s.get(i));
								song.getTags().load();
								libFileWriter.println(song.export());
								addSong(song);
								IMPORT_INDEX = i;
								System.out.println("Added song " + song.getFile().getAbsolutePath() + " to library");
							}
							if(updateInterrupt) {
								libFileWriter.close();
								LIB_FILE.delete();
								System.exit(0);
							} else {
								libraryIsUpdating = false;
							}
						}
						libFileWriter.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			UpdateThread up = new UpdateThread();
			up.start();
			
			while(libraryIsUpdating) {
				Thread.sleep(10);
			}
		} catch (Exception e) {
			// This should never happen
		}
	}
	
	public static String simplefy(String s) {
		String simplestring = "";
		for(char c : s.toLowerCase().toCharArray()) {
			// Check if it's in the alphabet or a space
			if((c == ' ') || (((int) c < 123) && ((int) c > 96))) {
				simplestring += c;
			}
		}
		return simplestring;
	}
	
	public static ArrayList<File> searchDirectory(File dir) {
		System.out.println("Searching for mp3 files in " + dir.getAbsolutePath() + "...");
		ArrayList<File> rtn = new ArrayList<File>();
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				rtn.addAll(searchDirectory(f));
			} else {
				if(f.getName().endsWith(".mp3")) {
					rtn.add(f);
				}
			}
		}
		System.out.println("Finished searching for mp3 files in " + dir.getAbsolutePath());
		return rtn;
	}
}
