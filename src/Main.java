import java.util.Calendar;

public class Main {
	public static Song playingSong;
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
