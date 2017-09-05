import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class TestPanel extends JPanel {

	public TestPanel() {
		// Generate panels
		JPanel panelA = new JPanel();
		JPanel panelB = new JPanel();
		
		// Change color of panels
		this.setBackground(Color.BLUE);
		panelA.setBackground(Color.CYAN);
		panelB.setBackground(Color.GREEN);
		
		// Generate label and add to panelA
		JLabel label = new JLabel("Welcome to Picture Importer");
		panelA.add(label);
		
		// Generate buttons
		JButton button1 = new JButton("First Button");
		JButton button2 = new JButton("Second Button");
		JButton button3 = new JButton("Third Button");
		
		// Add button1~3 to panelB
		panelB.add(button1);
		panelB.add(button2);
		panelB.add(button3);
		
		// Add panel A, B to panel
		this.add(panelA);
		this.add(panelB);
	}
}