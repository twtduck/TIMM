import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class UI implements ActionListener, ChangeListener, MouseListener {
	private final static boolean SHOW_PANEL_BORDERS = true;
	private final static Color PANEL_BORDER_COLOR = Color.DARK_GRAY;
	private static JFrame frame;
	private static JButton playButton;
	private static JButton pauseButton;
	private static JButton stopButton;
	private static JButton nextButton;
	private static JButton prevButton;
	private static JSlider seekerSlider;
	private static JSlider volumeSlider;
	private static JLabel seekerLabel;
	private static JLabel titleLabel;
	private static JLabel albumLabel;
	private static JLabel artistLabel;
	private static JLabel volumeLabel;
	private static JPanel songInfoPanel;
	private static JPanel seekerPanel;
	private static JPanel buttonPanel;
	private static JPanel volumePanel;
	private static JPanel controlsPanel;
	private static JPanel filterPanel;
	private static JPanel listPanel;
	private static Song[] library;
	private static String totalDurationString;
	private static int currentDurationInt;
	private static String currentDurationString;

	public static void launch() {
		frame = new JFrame("WMM - " + Player.getStatus());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridwidth = 1;
		generateControlsPanel();
		frame.add(controlsPanel, c);
		c.gridy = 1;
		generateFilterPanel();
		frame.add(filterPanel, c);
		c.gridy = 2;
		generateListPanel();
		frame.add(listPanel, c);
		frame.pack();
		frame.setVisible(true);
		startSongInfoUpdater();
	}

	public static void importLibrary() {
		// TODO: Search more than a hard-coded music folder
		//File libraryDir = new File("/home/thomas/Music/Hans Zimmer/Gladiator");
		File libraryDir = new File("/home/thomas/Music/Hans Zimmer/Gladiator");
		
		ArrayList<File> mp3s = searchForMusic(libraryDir);
		library = new Song[mp3s.size()];
		for (int i = 0; i < library.length; i++) {
			System.out.println("(" + i + ") Adding to library: " + mp3s.get(i).getAbsolutePath());
			library[i] = new Song(mp3s.get(i));
		}
	}

	private static ArrayList<File> searchForMusic(File dir) {
		System.out.println("Entering directory " + dir.getAbsolutePath());
		ArrayList<File> rtn = new ArrayList<File>();
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				rtn.addAll(searchForMusic(f));
			} else {
				if (f.getName().endsWith(".mp3")) {
					rtn.add(f);
				}
			}
		}
		System.out.println("Leaving directory " + dir.getAbsolutePath());
		return rtn;
	}

	private static void generateListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		if (SHOW_PANEL_BORDERS) {
			panel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(10, 10, 10, 10);
		c.ipadx = 15;
		c.ipady = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		String[] columns = { "Track Number", "Title", "Genre", "Arist", "Album", "Time" };
		String[][] tableContents = new String[library.length][columns.length];
		for (int row = 0; row < library.length; row++) {
			String[] rowData = { 
					is(library[row].getTags().getTrackNum()), 
					library[row].getTags().getTitle(),
					library[row].getTags().getGenre(), 
					library[row].getTags().getArtist(),
					library[row].getTags().getAlbum(), 
					formatTime((int) library[row].getTags().getDuration() / 1000) };
			tableContents[row] = rowData;
		}
		
		TableModel model = new DefaultTableModel(tableContents, columns) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(1760,500));
		table.setFillsViewportHeight(true);
		table.addMouseListener(new UI());
		JScrollPane tableScroller = new JScrollPane(table);
		panel.add(tableScroller);
		listPanel = panel;

	}

	private static void generateFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		if (SHOW_PANEL_BORDERS) {
			panel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 15;
		c.ipady = 10;
		filterPanel = panel;
	}

	public static void startSongInfoUpdater() {
		class PlayerWatcher extends Thread {
			@Override
			public void run() {

				Song registeredSong = null;
				boolean registeredLoop = false;
				while (true) {
					try {
						if ((registeredSong != Player.nowPlaying)
								|| (registeredLoop != registeredSong.getMainLoop().isRunning())) {
							System.out.println("Change detected! Updating info");
							UI.updateSongInfo();
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						registeredSong = Player.nowPlaying;
						registeredLoop = registeredSong.getMainLoop().isRunning();

					} catch (NullPointerException e) {
						// This is expected when stopped
						if (!Player.getStatus().equals("Stopped"))
							System.out.println("Unexpected NullPointerException in the SongInfoUpdater");

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// e1.printStackTrace();
							System.out.println("Unexpected thread interruption in the info updater thread!");
						}
					}
				}
			}
		}
		PlayerWatcher pw = new PlayerWatcher();
		pw.start();
	}

	public static void updateSongInfo() {
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		artistLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		albumLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		if (Player.getStatus().equals("Stopped")) {
			titleLabel.setText("Not playing");
			albumLabel.setText("");
			artistLabel.setText("");
		} else {
			titleLabel.setText(Player.nowPlaying.getTags().getTitle());
			artistLabel.setText("by " + Player.nowPlaying.getTags().getArtist());
			albumLabel.setText("from " + Player.nowPlaying.getTags().getAlbum());
		}
		updateTime();
	}

	private static String formatTime(int seconds) {
		return "00:00:00";
	}

	private static void updateTime() {
		if (Player.getStatus().equals("Stopped")) {
			currentDurationString = "00:00:00";
			totalDurationString = "00:00:00";
		} else {
			int sec = currentDurationInt % 60;
			int min = (currentDurationInt % 3600) / 60;
			int hour = currentDurationInt / 3600; // 60 * 60
			currentDurationString = is(hour) + ":" + is(min) + ":" + is(sec);
			int total = (int) Player.nowPlaying.getTags().getDuration() / 1000;
			totalDurationString = is(total / 3600) + ":" + is((total % 3600) / 60) + ":" + is(total % 60);
		}
		try {
			seekerLabel.setText(currentDurationString + "/" + totalDurationString);
			seekerSlider.setValue(currentDurationInt);
			seekerSlider.setMaximum((int) Player.nowPlaying.getTags().getDuration() / 1000);
		} catch (NullPointerException e) {
			System.out.println("Can't yet edit seeker element because they don't exist");
		}
	}

	private static void generateControlsPanel() {
		controlsPanel = new JPanel();
		if (SHOW_PANEL_BORDERS) {
			controlsPanel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		controlsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		generateButtonPanel();
		controlsPanel.add(buttonPanel, c);
		c.gridx = 1;
		generateSongInfoPanel();
		controlsPanel.add(songInfoPanel, c);
		c.gridx = 2;
		generateSeekerPanel();
		controlsPanel.add(seekerPanel, c);
		c.gridx = 3;
		generateVolumePanel();
		controlsPanel.add(volumePanel, c);
	}

	private static void generateVolumePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		if (SHOW_PANEL_BORDERS) {
			panel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 15;
		c.ipady = 10;
		volumeSlider = new JSlider();
		volumeSlider.addChangeListener(new UI());
		volumeLabel = new JLabel("Volume: " + volumeSlider.getValue() + "%");
		panel.add(volumeSlider, c);
		c.gridy = 1;
		panel.add(volumeLabel, c);
		panel.setPreferredSize(new Dimension(250, 70));
		volumePanel = panel;
	}

	private static void generateSeekerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setPreferredSize(new Dimension(600, 70));
		if (SHOW_PANEL_BORDERS) {
			panel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 15;
		c.ipady = 10;
		seekerSlider = new JSlider(0, 1, 0);
		seekerSlider.setPreferredSize(new Dimension(550, 20));
		seekerSlider.setEnabled(false);
		panel.add(seekerSlider, c);
		updateTime();
		seekerLabel = new JLabel(currentDurationString + "/" + totalDurationString);
		seekerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		c.gridy = 1;
		panel.add(seekerLabel, c);

		seekerPanel = panel;
	}

	private static void generateSongInfoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setPreferredSize(new Dimension(400, 70));
		if (SHOW_PANEL_BORDERS) {
			panel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 15;
		c.ipady = 0;
		titleLabel = new JLabel();
		artistLabel = new JLabel();
		albumLabel = new JLabel();
		updateSongInfo();
		panel.add(titleLabel, c);
		c.gridy++;
		panel.add(albumLabel, c);
		c.gridy++;
		panel.add(artistLabel, c);
		songInfoPanel = panel;
	}

	private static void generateButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		if (SHOW_PANEL_BORDERS) {
			buttonPanel.setBorder(BorderFactory.createLineBorder(PANEL_BORDER_COLOR, 3, true));
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 1;
		c.ipadx = 10;
		c.ipady = 10;
		c.fill = GridBagConstraints.BOTH;
		System.out.println(System.getProperty("java.home"));
		prevButton = new JButton(/* "\u23EE" */);
		prevButton.setIcon(new ImageIcon("img/prev.png"));
		prevButton.addActionListener(new UI());
		pauseButton = new JButton(/* "\u23F8" */);
		pauseButton.setIcon(new ImageIcon("img/pause.png"));
		pauseButton.addActionListener(new UI());
		playButton = new JButton(/* "\u23F5" */);
		playButton.setIcon(new ImageIcon("img/play.png"));
		playButton.addActionListener(new UI());
		stopButton = new JButton(/* "\u23F9" */);
		stopButton.setIcon(new ImageIcon("img/stop.png"));
		stopButton.addActionListener(new UI());
		nextButton = new JButton(/* "\u23ED" */);
		nextButton.setIcon(new ImageIcon("img/next.png"));
		nextButton.addActionListener(new UI());

		buttonPanel.add(prevButton, c);
		c.gridx = 1;
		buttonPanel.add(pauseButton, c);
		c.gridx = 2;
		buttonPanel.add(playButton, c);
		c.gridx = 3;
		buttonPanel.add(stopButton, c);
		c.gridx = 4;
		buttonPanel.add(nextButton, c);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == playButton) {
			// System.out.println("Play button clicked");
			Player.play();
		} else if (event.getSource() == pauseButton) {
			Player.pause();
		} else if (event.getSource() == stopButton) {
			currentDurationInt = 0;
			Player.stop();
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource().equals(volumeSlider)) {
			volumeLabel.setText("Volume: " + volumeSlider.getValue() + "%");
			double vol = volumeSlider.getValue();
			vol /= 100;
			Player.setVolume(vol);
		}
	}

	public static void incSeconds() {
		++currentDurationInt;
		updateTime();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		JTable table =(JTable) me.getSource();
		Point p = me.getPoint();
		int row = table.rowAtPoint(p);
		if(me.getClickCount() == 2) {
			// Double click on the row at
			if(!Player.getStatus().equals("Stopped")) {
				Player.stop();
			}
			Player.playSong(library[row]);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static void setTime(int i) {
		UI.currentDurationInt = i;
	}
}
