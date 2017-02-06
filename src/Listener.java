import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class Listener {
	
	public static class FilterListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				GUI.filteredResultsTable.refresh();
			}
		}
	}
	
	public static class LibraryUpdateWindowListener implements WindowListener {

		@Override
		public void windowClosed(WindowEvent arg0) {
			Library.updateInterrupt = true;
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			Library.updateInterrupt = true;
		}
		
		// Necessary method implementations

		@Override
		public void windowDeactivated(WindowEvent arg0) {}

		@Override
		public void windowDeiconified(WindowEvent arg0) {}

		@Override
		public void windowIconified(WindowEvent arg0) {}

		@Override
		public void windowOpened(WindowEvent arg0) {}

		@Override
		public void windowActivated(WindowEvent arg0) {}
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
