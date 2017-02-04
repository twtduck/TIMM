import java.util.Calendar;

public class TimeKeeper extends Thread {
	// the loop's delay in milliseconds. 
	// If it's set to 10, the loop runs 100 times per second.
	// If it's set to 16, it updates just over 60 times per second, which is the refresh rate of most displays
	private final int UPDATE_DELAY = 8;
	private long duration;
	
	public TimeKeeper(long songDuration) {
		this.duration = songDuration;
	}
	
	public long getTime() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	@Override
	public void run() {
		long startTime = getTime();
		long pausedTime = 0;
		long songTime = getTime() - (startTime + pausedTime);
		long pauseStartTime = 0;
		long pauseEndTime = 0;
		int secondsInCheck = 0;
		boolean wasPaused = false;
		while(songTime < duration) {
			if(Player.status == Player.Status.Paused) {
				if(!wasPaused) {
					pauseStartTime = getTime();
					wasPaused = true;
				}
			} else {
				if(wasPaused) {
					pauseEndTime = getTime();
					pausedTime += (pauseEndTime - pauseStartTime);
				}
				int secondsIn = (int) (songTime / 1000);
				if(secondsIn != secondsInCheck) {
					secondsInCheck = secondsIn;
					Player.currentTimeInt = secondsIn;
					GUI.updateTime();
				}
			}
			try {
				Thread.sleep(UPDATE_DELAY);
			} catch (InterruptedException e) {
				// This should never happen
				e.printStackTrace();
				System.exit(-1);
			}
			songTime = getTime() - (startTime + pausedTime);
		}
	}
	
}