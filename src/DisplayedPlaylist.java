import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DisplayedPlaylist extends Playlist {
	private JPanel displayPanel;
	private JTable displayedList;
	public DisplayedPlaylist() {
		super();
		this.displayPanel = new JPanel();
		
		
	}
	
	@Override
	public void clear() {
		super.clear();
		
	}
}
