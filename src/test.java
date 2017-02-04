
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class test implements ActionListener {
	    public static void main(String[] args) throws Exception {
	    	System.out.println(System.getProperty("sun.arch.data.model"));
	    	
	    	TestPlayer tp = new TestPlayer();
	    	tp.start();
	    	/*
			Gst.init();
	    	MainLoop ml = new MainLoop();
	    	Element p1;
	    	p1 = ElementFactory.make("playbin", "player");
	    	List<String> list = p1.listPropertyNames();
	    	for(String s : list) {
	    		//System.out.println(s);
	    	}
	    	
	    	
	    	p1.set("uri", new File("/home/thomas/voodoo.mp3").toURI().toString());
	    	p1.set("volume", 0.2);
	    	
	    	p1.setState();
	    	ml.run();
	    	*/
	    	System.out.println("Cont");
	    	JFrame stopperFrame = new JFrame("Stop!");
	    	stopperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	JButton playButton = new JButton("Click to play");
	    	JButton stopButton = new JButton("Click to pause");
	    	stopperFrame.setLayout(new GridBagLayout());
	    	GridBagConstraints c = new GridBagConstraints();
	    	c.ipadx = 30;
	    	c.ipady = 30;
	    	c.insets = new Insets(20,20,20,20);
	    	stopperFrame.add(playButton, c);
	    	playButton.addActionListener(new test());
	    	stopButton.addActionListener(new test());
	    	c.gridy += 1;
	    	stopperFrame.add(stopButton, c);
	    	stopperFrame.pack();
	    	stopperFrame.setVisible(true);
	    	
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			if(((JButton) e.getSource()).getText().equals("Click to pause")) {
				System.out.println("Pause button clicked");
				TestPlayer.p1.setState(org.freedesktop.gstreamer.State.PAUSED); 
			} else {
				System.out.println("Play button clicked");
				TestPlayer.p1.setState(org.freedesktop.gstreamer.State.PLAYING);
			}
		}
	}