
import java.io.File;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.MainLoop;

public class TestPlayer extends Thread {
	public static Element p1;
	@Override
	public void run() {
		Gst.init();
    	MainLoop ml = new MainLoop();
    	p1 = ElementFactory.make("playbin", "player");
    	//List<String> list = p1.listPropertyNames();
    	//for(String s : list) {
    		//System.out.println(s);
    	//}
    	
    	
    	p1.set("uri", new File("/home/thomas/voodoo.mp3").toURI().toString());
    	p1.set("volume", 0.2);
    	
    	p1.setState(org.freedesktop.gstreamer.State.PLAYING);
    	ml.run();
	}
}
