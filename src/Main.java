import java.io.File;
import java.util.Calendar;

import javafx.scene.media.MediaView;

public class Main {
	public static Song playingSong;
	public static MediaView mediaView;
	public static long startTime;
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		startTime = c.getTimeInMillis();
		try {
			Library.loadLibrary();
			//System.out.println(System.getProperty("user.home"));
			GUI.createMainWindowGUI();
		} catch (IllegalStateException e) {
			// Do nothing
		}
		
	}
}
