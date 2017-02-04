import javafx.util.Duration;

public class Time {
	
	private int hour;
	private int min;
	private int sec;
	
	public Time(int hour, int min, int sec) {
		this.hour = hour;
		this.min = min;
		this.sec = sec;
	}
	public Time(Duration duration) {
		int secs = (int) duration.toSeconds();
	}
	public Time() {
		this.hour = 0;
		this.min = 0;
		this.sec = 0;
	}
	
	@Override
	public String toString() {
		String hourString = Integer.toString(hour);
		if(hourString.length() < 2)
			hourString = "0" + hourString;
		String minString = Integer.toString(min);
		if(minString.length() < 2)
			minString = "0" + minString;
		String secString = Integer.toString(sec);
		if(secString.length() < 2)
			secString = "0" + secString;
		String rtn = hourString + ":" + minString + ":" + secString;
		return rtn;
	}
	
	public void addSecond() {
		sec += 1;
		if(sec >= 60) {
			sec -= 60;
			min += 1;
			if(min >= 60) {
				min -= 60;
				hour += 1;
			}
		}
	}
}
