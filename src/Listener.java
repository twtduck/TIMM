import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JMenuItem;

public abstract class Listener {
	public static class LibraryUpdateWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			Library.updateInterrupt = true;
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			Library.updateInterrupt = true;
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public static class ControlButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public static class MenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(((JMenuItem) e.getSource()).getText().equals("Exit")) {
				System.exit(0);
			} else if(((JMenuItem) e.getSource()).getText().equals("Manage library locations...")) {
				GUI.manageLibraryLocations();
			}
			
		}
		
	}
}
