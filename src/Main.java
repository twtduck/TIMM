import java.io.File;

import javafx.scene.media.MediaView;

public class Main {
	public static Song playingSong;
	public static MediaView mediaView;
	public static void main(String[] args) {
		System.out.println((int) 'a');
		System.out.println((int) 'z');
		System.out.println((int) 'A');
		System.out.println((int) 'Z');
		System.out.println((int) ' ');
		
		
		try {
			Library.loadLibrary();
			//System.out.println(System.getProperty("user.home"));
			GUI.createMainWindowGUI();
		} catch (IllegalStateException e) {
			// Do nothing
		}
		
	}
}
