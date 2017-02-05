import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.border.Border;

public class GUI extends Thread {
	private final static boolean START_MAXIMIZED = false;
	public static JFrame frame;
	public static JPanel controlPanel;
	private static JLabel titleLabel;
	private static JLabel albumLabel;
	private static JLabel artistLabel;
	private static JSlider seekerSlider;
	public static JLabel seekerLabel;
	public static YearFilterPanel yearFilterPanel;
	public static GenreFilterPanel genreFilterPanel;
	public static ArtistFilterPanel artistFilterPanel;
	public static AlbumFilterPanel albumFilterPanel;
	private static JSlider volumeSlider;
	public static FilteredResultsTable filteredResultsTable;
	public static JPanel playlistPanel;
	public static ArrayList<FilterPanel> filterPanels = new ArrayList<FilterPanel>();
	private static JLabel volumeLabel;
	public static JPanel filterPanel;
	public static JPanel filteredResultsPanel;

	static Border genBorder() {
		return BorderFactory.createLineBorder(Color.DARK_GRAY, 3);
	}

	public static void libraryUpdateGUI() {
		JFrame frame = new JFrame("Updating library....");
		JProgressBar pb = new JProgressBar(0, 1);
		pb.setValue(0);
		pb.setPreferredSize(new Dimension(320, 60));
		frame.add(pb);
		frame.pack();
		frame.addWindowListener(new Listener.LibraryUpdateWindowListener());
		frame.setVisible(true);
		class DisplayThread extends Thread {
			@Override
			public void run() {
				while ((Library.IMPORT_TOTAL == 0) || (Library.IMPORT_INDEX + 1 != Library.IMPORT_TOTAL)) {
					pb.setMaximum(Integer.max(Library.IMPORT_TOTAL, 1));
					pb.setValue(Library.IMPORT_INDEX);
					pb.setStringPainted(true);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Finished importing");
				frame.setVisible(false);
				frame.dispose();
			}
		}
		DisplayThread thread = new DisplayThread();
		thread.start();
	}

	public static void createMainWindowGUI() {
		// Create file menu for importing a folder
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem manageLibraryLocationsMenuItem = new JMenuItem("Manage library locations...");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		manageLibraryLocationsMenuItem.addActionListener(new Listener.MenuItemListener());
		exitMenuItem.addActionListener(new Listener.MenuItemListener());
		frame = new JFrame("Window");
		menuBar.add(fileMenu);
		fileMenu.add(manageLibraryLocationsMenuItem);
		fileMenu.add(exitMenuItem);
		frame.setJMenuBar(menuBar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (START_MAXIMIZED)
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		// A few objects need to be created early to fulfill dependencies
		genreFilterPanel = new GenreFilterPanel();

		
		// Main window panel
		frame.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints mainWinConstraints = new GridBagConstraints();
		mainWinConstraints.anchor = GridBagConstraints.NORTHWEST;
		mainWinConstraints.fill = GridBagConstraints.BOTH;
		mainWinConstraints.insets = new Insets(10, 10, 5, 10);
		mainWinConstraints.gridheight = 1;
		mainWinConstraints.gridwidth = 2;
		mainWinConstraints.gridx = 0;
		mainWinConstraints.weightx = 1;
		mainWinConstraints.gridy = 0;

		createControlPanel();

		frame.getContentPane().add(controlPanel, mainWinConstraints);

		// Lower panel
		// Playlist panel
		createPlaylistPanel();
		mainWinConstraints.gridy = 1;
		mainWinConstraints.gridheight = 2;
		mainWinConstraints.weighty = 1;
		mainWinConstraints.weightx = 0;
		mainWinConstraints.gridwidth = 1;
		mainWinConstraints.insets = new Insets(5, 10, 10, 5);
		frame.getContentPane().add(playlistPanel, mainWinConstraints);

		// Filter panel
		createFilterPanel();
		mainWinConstraints.gridx = 1;
		mainWinConstraints.gridheight = 1;
		mainWinConstraints.weighty = 0.3;
		mainWinConstraints.insets = new Insets(5, 5, 5, 10);
		frame.getContentPane().add(filterPanel, mainWinConstraints);

		// Filtered results panel
		createFilteredResultsPanel();
		mainWinConstraints.gridy = 2;
		mainWinConstraints.weighty = 0.7;
		mainWinConstraints.insets = new Insets(5, 5, 10, 10);
		frame.getContentPane().add(filteredResultsPanel, mainWinConstraints);

		frame.pack();
		frame.setVisible(true);
	}

	public static void createControlPanel() {
		// Button panel
		JPanel buttonPanel = new JPanel();
		Dimension buttonSize = new Dimension(52, 48);
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setPreferredSize(new Dimension(237, 74));
		buttonPanel.setBorder(genBorder());
		GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
		buttonPanelConstraints.anchor = GridBagConstraints.CENTER;
		buttonPanelConstraints.fill = GridBagConstraints.BOTH;
		buttonPanelConstraints.insets = new Insets(10, 10, 10, 0);
		buttonPanelConstraints.gridheight = 1;
		buttonPanelConstraints.gridwidth = 1;
		buttonPanelConstraints.gridx = 0;
		buttonPanelConstraints.gridy = 0;
		JButton prevButton = new JButton();
		prevButton.setIcon(new ImageIcon("img/prev.png"));
		prevButton.setPreferredSize(buttonSize);
		prevButton.addActionListener(new Listener.ControlButtonListener());
		buttonPanel.add(prevButton, buttonPanelConstraints);
		buttonPanelConstraints.gridx = 1;
		buttonPanelConstraints.insets = new Insets(10, 0, 10, 0);
		JButton playPauseButton = new JButton();
		playPauseButton.setIcon(new ImageIcon("img/play.png"));
		playPauseButton.setPreferredSize(buttonSize);
		playPauseButton.addActionListener(new Listener.ControlButtonListener());
		buttonPanel.add(playPauseButton, buttonPanelConstraints);
		buttonPanelConstraints.gridx = 2;
		buttonPanelConstraints.insets = new Insets(10, 0, 10, 0);
		JButton stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("img/stop.png"));
		stopButton.setPreferredSize(buttonSize);
		stopButton.addActionListener(new Listener.ControlButtonListener());
		buttonPanel.add(stopButton, buttonPanelConstraints);
		buttonPanelConstraints.gridx = 3;
		buttonPanelConstraints.insets = new Insets(10, 0, 10, 10);
		JButton nextButton = new JButton();
		nextButton.setIcon(new ImageIcon("img/next.png"));
		nextButton.setPreferredSize(buttonSize);
		nextButton.addActionListener(new Listener.ControlButtonListener());
		buttonPanel.add(nextButton, buttonPanelConstraints);

		// Song info panel
		JPanel songInfoPanel = new JPanel();
		songInfoPanel.setLayout(new GridBagLayout());
		songInfoPanel.setPreferredSize(new Dimension(200, 74));
		songInfoPanel.setBorder(genBorder());
		GridBagConstraints songInfoPanelConstraints = new GridBagConstraints();
		songInfoPanelConstraints.anchor = GridBagConstraints.WEST;
		songInfoPanelConstraints.fill = GridBagConstraints.BOTH;
		songInfoPanelConstraints.gridheight = 1;
		songInfoPanelConstraints.gridwidth = 1;
		songInfoPanelConstraints.gridx = 0;
		songInfoPanelConstraints.gridy = 0;
		songInfoPanelConstraints.insets = new Insets(0, 10, 0, 0);
		titleLabel = new JLabel("Title");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		songInfoPanel.add(titleLabel, songInfoPanelConstraints);
		songInfoPanelConstraints.gridy = 1;
		albumLabel = new JLabel("Album");
		albumLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 13));
		songInfoPanel.add(albumLabel, songInfoPanelConstraints);
		songInfoPanelConstraints.gridy = 2;
		artistLabel = new JLabel("Artist");
		artistLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		songInfoPanel.add(artistLabel, songInfoPanelConstraints);
		songInfoPanelConstraints.gridx = 1;
		songInfoPanelConstraints.gridy = 0;
		songInfoPanelConstraints.gridheight = 3;
		songInfoPanelConstraints.weightx = 1;
		songInfoPanelConstraints.insets = new Insets(0, 0, 0, 0);
		JPanel spacePanel = new JPanel();
		songInfoPanel.add(spacePanel, songInfoPanelConstraints);

		// Seeker panel
		JPanel seekerPanel = new JPanel();
		seekerPanel.setLayout(new GridBagLayout());
		seekerPanel.setPreferredSize(new Dimension(320, 74));
		seekerPanel.setBorder(genBorder());
		GridBagConstraints seekerPanelConstraints = new GridBagConstraints();
		seekerPanelConstraints.anchor = GridBagConstraints.CENTER;
		seekerPanelConstraints.fill = GridBagConstraints.BOTH;
		seekerPanelConstraints.insets = new Insets(10, 10, 5, 10);
		seekerPanelConstraints.gridheight = 1;
		seekerPanelConstraints.gridwidth = 3;
		seekerPanelConstraints.gridx = 0;
		seekerPanelConstraints.gridy = 0;
		seekerSlider = new JSlider(0, 1, 0);
		// seekerSlider.setBorder(genBorder());
		seekerSlider.setPreferredSize(new Dimension(280, seekerSlider.getPreferredSize().height));
		seekerPanel.add(seekerSlider, seekerPanelConstraints);
		seekerPanelConstraints.gridy = 1;
		seekerPanelConstraints.gridwidth = 1;
		seekerPanelConstraints.weightx = 1;
		seekerPanel.add(new JPanel(), seekerPanelConstraints);
		seekerPanelConstraints.gridx = 1;
		seekerPanelConstraints.weightx = 0;
		seekerPanelConstraints.insets = new Insets(5, 10, 10, 10);
		seekerLabel = new JLabel("00:00:00 / 00:00:00");
		seekerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		// seekerLabel.setBorder(genBorder());
		seekerPanel.add(seekerLabel, seekerPanelConstraints);
		seekerPanelConstraints.gridx = 2;
		seekerPanelConstraints.weightx = 1;
		seekerPanel.add(new JPanel(), seekerPanelConstraints);

		// Volume panel
		JPanel volumePanel = new JPanel();
		volumePanel.setLayout(new GridBagLayout());
		volumePanel.setPreferredSize(new Dimension(194, 74));
		volumePanel.setBorder(genBorder());
		GridBagConstraints volumePanelConstraints = new GridBagConstraints();
		volumePanelConstraints.anchor = GridBagConstraints.CENTER;
		volumePanelConstraints.fill = GridBagConstraints.BOTH;
		volumePanelConstraints.insets = new Insets(10, 10, 5, 10);
		volumePanelConstraints.gridheight = 1;
		volumePanelConstraints.gridwidth = 3;
		volumePanelConstraints.gridx = 0;
		volumePanelConstraints.gridy = 0;
		volumeSlider = new JSlider();
		// volumeSlider.setBorder(genBorder());
		volumeSlider.setPreferredSize(new Dimension(168, volumeSlider.getPreferredSize().height));
		volumePanel.add(volumeSlider, volumePanelConstraints);
		volumePanelConstraints.gridy = 1;
		volumePanelConstraints.gridwidth = 1;
		volumePanelConstraints.weightx = 1;
		volumePanel.add(new JPanel(), volumePanelConstraints);
		volumePanelConstraints.gridx = 1;
		volumePanelConstraints.weightx = 0;
		volumePanelConstraints.insets = new Insets(5, 10, 10, 10);
		volumeLabel = new JLabel("Volume: 50%");
		volumeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		// volumeLabel.setBorder(genBorder());
		volumePanel.add(volumeLabel, volumePanelConstraints);
		volumePanelConstraints.gridx = 2;
		volumePanelConstraints.weightx = 1;
		volumePanel.add(new JPanel(), volumePanelConstraints);

		// Control panel
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		controlPanel.setPreferredSize(new Dimension(1150, 100));
		controlPanel.setBorder(genBorder());
		GridBagConstraints controlPanelConstraints = new GridBagConstraints();
		controlPanelConstraints.anchor = GridBagConstraints.CENTER;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		controlPanelConstraints.insets = new Insets(10, 10, 10, 5);
		controlPanelConstraints.gridheight = 1;
		controlPanelConstraints.gridwidth = 1;
		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 0;
		controlPanel.add(buttonPanel, controlPanelConstraints);
		controlPanelConstraints.gridx = 1;
		controlPanelConstraints.weightx = 0.3;
		controlPanelConstraints.insets = new Insets(10, 5, 10, 5);
		controlPanel.add(songInfoPanel, controlPanelConstraints);
		controlPanelConstraints.gridx = 2;
		controlPanelConstraints.weightx = 0.7;
		controlPanelConstraints.insets = new Insets(10, 5, 10, 5);
		controlPanel.add(seekerPanel, controlPanelConstraints);
		controlPanelConstraints.gridx = 3;
		controlPanelConstraints.weightx = 0;
		controlPanelConstraints.insets = new Insets(10, 5, 10, 10);
		controlPanel.add(volumePanel, controlPanelConstraints);
	}

	public static void createPlaylistPanel() {
		// TODO: Write playlist tabs, playlist display area, and playlist button
		// panel

		playlistPanel = new JPanel();
		playlistPanel.setLayout(new GridBagLayout());
		playlistPanel.setPreferredSize(new Dimension(250, 670));
		playlistPanel.setBorder(genBorder());
		GridBagConstraints playlistPanelConstraints = new GridBagConstraints();
		playlistPanelConstraints.anchor = GridBagConstraints.CENTER;
		playlistPanelConstraints.fill = GridBagConstraints.BOTH;
		playlistPanelConstraints.insets = new Insets(10, 10, 10, 10);
		playlistPanelConstraints.gridheight = 1;
		playlistPanelConstraints.gridwidth = 1;
		playlistPanelConstraints.gridx = 0;
		playlistPanelConstraints.gridy = 0;
	}

	public static void createFilterPanel() {
		filterPanel = new JPanel();
		filterPanel.setLayout(new GridBagLayout());
		filterPanel.setPreferredSize(new Dimension(890, 250));
		filterPanel.setBorder(genBorder());
		GridBagConstraints filterPanelConstraints = new GridBagConstraints();
		filterPanelConstraints.anchor = GridBagConstraints.CENTER;
		filterPanelConstraints.fill = GridBagConstraints.BOTH;
		filterPanelConstraints.insets = new Insets(10, 10, 10, 5);
		filterPanelConstraints.gridheight = 1;
		filterPanelConstraints.gridwidth = 1;
		filterPanelConstraints.gridx = 0;
		filterPanelConstraints.weightx = 1;
		filterPanelConstraints.weighty = 1;
		filterPanelConstraints.gridy = 0;
		Dimension filterPanelSize = new Dimension(208, 230);
		yearFilterPanel = new YearFilterPanel();
		yearFilterPanel.setBorder(genBorder());
		yearFilterPanel.setPreferredSize(filterPanelSize);
		filterPanel.add(yearFilterPanel, filterPanelConstraints);
		filterPanels.add(yearFilterPanel);
		genreFilterPanel.setBorder(genBorder());
		genreFilterPanel.setPreferredSize(filterPanelSize);
		filterPanelConstraints.insets = new Insets(10, 5, 10, 5);
		filterPanelConstraints.gridx = 1;
		filterPanel.add(genreFilterPanel, filterPanelConstraints);
		filterPanels.add(genreFilterPanel);
		artistFilterPanel = new ArtistFilterPanel();
		artistFilterPanel.setBorder(genBorder());
		artistFilterPanel.setPreferredSize(filterPanelSize);
		filterPanelConstraints.gridx = 2;
		filterPanel.add(artistFilterPanel, filterPanelConstraints);
		filterPanels.add(artistFilterPanel);
		albumFilterPanel = new AlbumFilterPanel();
		albumFilterPanel.setBorder(genBorder());
		albumFilterPanel.setPreferredSize(filterPanelSize);
		filterPanelConstraints.gridx = 3;
		filterPanelConstraints.insets = new Insets(10, 5, 10, 10);
		filterPanel.add(albumFilterPanel, filterPanelConstraints);
		filterPanels.add(albumFilterPanel);
	}

	public static void createFilteredResultsPanel() {
		filteredResultsPanel = new JPanel();
		filteredResultsPanel.setLayout(new GridBagLayout());
		filteredResultsPanel.setPreferredSize(new Dimension(890, 390));
		filteredResultsPanel.setBorder(genBorder());
		GridBagConstraints filteredResultsPanelConstraints = new GridBagConstraints();
		filteredResultsPanelConstraints.anchor = GridBagConstraints.CENTER;
		filteredResultsPanelConstraints.fill = GridBagConstraints.BOTH;
		filteredResultsPanelConstraints.insets = new Insets(10, 10, 10, 10);
		filteredResultsPanelConstraints.gridheight = 1;
		filteredResultsPanelConstraints.gridwidth = 1;
		filteredResultsPanelConstraints.gridx = 0;
		filteredResultsPanelConstraints.gridy = 0;
		filteredResultsPanelConstraints.weightx = 1;
		filteredResultsPanelConstraints.weighty = 1;
		
		String[] columns = { "Track #", "Title", "Genre", "Artist", "Album", "Time" };
		filteredResultsTable = new FilteredResultsTable(columns);

		for (Library.Artist artist : Library.music) {
			for (Library.Album album : artist.albums) {
				for (Library.Disc disc : album.discs) {
					Collections.sort(disc.tracks, new Comparator<Song>() {

						@Override
						public int compare(Song o1, Song o2) {

							return o1.getTags().getTrackNumInt() - o2.getTags().getTrackNumInt();
						}

					});
				}
				Collections.sort(album.discs, new Comparator<Library.Disc>() {

					@Override
					public int compare(Library.Disc o1, Library.Disc o2) {
						return o1.discNum - o2.discNum;
					}

				});
			}

			Collections.sort(artist.albums, new Comparator<Library.Album>() {

				@Override
				public int compare(Library.Album o1, Library.Album o2) {
					return o1.simpleName.compareTo(o2.simpleName);
				}

			});
		}
		Collections.sort(Library.music, new Comparator<Library.Artist>() {

			@Override
			public int compare(Library.Artist o1, Library.Artist o2) {
				return o1.simpleName.compareTo(o2.simpleName);
			}

		});
		for (Library.Artist artist : Library.music) {
			for (Library.Album album : artist.albums) {
				for (Library.Disc disc : album.discs) {
					for (Song track : disc.tracks) {
						FilteredResultsTable.allMusic.add(track);
						String[] row = { track.getTags().getTrackNumString(), track.getTags().getTitle(),
								track.getTags().getGenre(), track.getTags().getArtist(), track.getTags().getAlbum(),
								track.getTags().getDurationString() };
						filteredResultsTable.addRowToEndNoRefresh(row);
					}
				}
			}
		}
		filteredResultsTable.setPreferredSize(new Dimension(864, 378));
		filteredResultsTable.refresh();
		filteredResultsTable.setBorder(genBorder());
		filteredResultsPanel.add(filteredResultsTable, filteredResultsPanelConstraints);
		
	}

	public static void manageLibraryLocations() {
		// TODO
	}

	public static void updateTime() {
		// TODO Auto-generated method stub

	}

	public static void updateSongInfo() {
		// TODO: Write this method
	}
}
