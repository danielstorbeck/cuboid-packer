package packergui;

import java.awt.BorderLayout;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		// Turn off bold fonts.
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule Gui on event thread.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	static void createAndShowGUI() {
		// main window
		JFrame frame = new JFrame("Cuboid Packer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// main panel
		MainPanel mp = new MainPanel();
		frame.add(mp, BorderLayout.CENTER);
		// display
		frame.pack();
		frame.setVisible(true);
		HelpTicker.switchToListTab();
	}
}
