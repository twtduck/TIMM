import java.io.File;
import java.util.ArrayList;

public class Player {
	public enum Status {
		Stopped, Paused, Playing;
	};
	public static Status status = Status.Stopped;
	public static Playlist dynamicList = new Playlist();
	private static double volume = 0.5;
	public static int currentTimeInt = 0;

	public static void play() {
		if(status == Status.Paused) {
			
		}
	}
	public static void pause() {
		if(status == Status.Playing) {
			dynamicList.getSongs().get(0).getPlayer().pauseSong();
			status = Status.Paused;
		}		
	}
	
	private static void resume() {
		dynamicList.getSongs().get(0).getPlayer().resumeSong();
		status = Status.Playing;
	}
	
	public static void next() {
		
	}
	
	public static void stop() {
		Player.status = Status.Stopped;
		
	}
	
	public static void endSong() {
		stop();
	}

	public static Double getVolume() {
		return volume; //EDIT: Make volume variable
	}

	public static void setVolume(double vol) {
		volume = vol;
	}
	
}
