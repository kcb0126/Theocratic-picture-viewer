import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

class ImageCellPanel extends JPanel {
	private String imagePath;
	
	private JButton btnBreak;
	private JCheckBox checkBox;
	
	private OnBreakListener m_OnBreakListener = null;
	
	private int m_index;
	
	private boolean m_isbroken = false;
	
	public ImageCellPanel(int index, String imagePath) {
		this.imagePath = imagePath;
		if(ImageFinder.isPDF(new File(imagePath))) {
			imagePath = imagePath + ".png_";
		}
		this.m_index = index;
		
		this.setLayout(new BorderLayout(10, 10));
		
		JLabel indexLabel = new JLabel(String.valueOf(index + 1));
		indexLabel.setForeground(Color.WHITE);
		this.add(indexLabel, BorderLayout.WEST);
		
		JPanel imageWithCheck = new JPanel();
		imageWithCheck.setBackground(Color.BLACK);
		this.add(imageWithCheck, BorderLayout.CENTER);
		imageWithCheck.setLayout(new BorderLayout(5, 5));
		checkBox = new JCheckBox();
		checkBox.setBackground(Color.BLACK);
		imageWithCheck.add(checkBox, BorderLayout.WEST);
		JLabel label = new JLabel();
		ImageIcon icon = new ImageIcon(imagePath);
		label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
		imageWithCheck.add(label, BorderLayout.CENTER);
		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(Color.BLACK);
		imageWithCheck.add(eastPanel, BorderLayout.EAST);
		
		btnBreak = new JButton("BREAK");
		btnBreak.addActionListener(new BreakButtonClickListener());
//		button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
		
		imageWithCheck.add(btnBreak, BorderLayout.NORTH);
		JLabel imageName = new JLabel(new File(imagePath).getName(), SwingConstants.CENTER);
		imageName.setForeground(Color.WHITE);
		Dimension imageNameDimension = new Dimension(imageWithCheck.getWidth(), 20);

		imageWithCheck.add(imageName, BorderLayout.SOUTH);
		
		imageName.setMinimumSize(imageNameDimension);
		imageName.setPreferredSize(imageNameDimension);
		imageName.setMaximumSize(imageNameDimension);
		this.setBackground(Color.BLACK);
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setOnBreakListener(OnBreakListener onBreakListener) {
		this.m_OnBreakListener = onBreakListener;
	}
	
	public void setBroken(boolean b) {
		if(b) {
			btnBreak.setBackground(Color.GREEN);
		}
		else {
			btnBreak.setBackground((new JButton()).getBackground());
		}
		this.m_isbroken = b;
	}
	
	public void unbreak() {
		this.m_OnBreakListener.onUnbreak(this.m_index);
	}
	
	public boolean isChecked() {
		return checkBox.isSelected();
	}
	
	public boolean isBroken() {
		return this.m_isbroken;
	}
	
	private class BreakButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(m_OnBreakListener != null) {
				m_OnBreakListener.onBreak(m_index);
			}
		}
		
	}
	
	public interface OnBreakListener {
		public void onBreak(int index);
		public void onUnbreak(int index);
	}
}