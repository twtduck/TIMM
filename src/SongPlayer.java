import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.MainLoop;

public class SongPlayer extends Thread {
	public TimeKeeper timer;
	private Song song;
	private Element songPlayer;
	private MainLoop ml;
	
	public SongPlayer(Song s) {
		this.song = s;
		timer = new TimeKeeper((long) s.getTags().getDuration());
	}
	
	public void playSong() {
		start();
	}
	
	public void resumeSong() {
		songPlayer.setState(org.freedesktop.gstreamer.State.PLAYING);
	}
	
	public void pauseSong() {
		songPlayer.setState(org.freedesktop.gstreamer.State.PAUSED);
	}
	
	public void stopSong() {
		pauseSong();
		//TODO: Figure out how to properly stop GStreamer
	}
	
	@Override
	public void run() {
		Gst.init();
		ml = new MainLoop();
		songPlayer = ElementFactory.make("playbin", "player");
    	songPlayer.set("uri", song.getFile().toURI().toString());
    	songPlayer.set("volume", Player.getVolume());
    	songPlayer.setState(org.freedesktop.gstreamer.State.PLAYING);
    	timer.start();
    	ml.run();
	}
	
	public void setVolume(double vol) {
		songPlayer.set("volume", Player.getVolume());
	}
}
