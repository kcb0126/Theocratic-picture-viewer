
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImageViewerPanel extends JPanel {
	
	JLabel imageLabel;

	public ImageViewerPanel() {
		imageLabel = new JLabel();
		this.add(imageLabel);
	}
	
	public ImageViewerPanel(String imagePath) {
		imageLabel = new JLabel();
		this.add(imageLabel);
		ImageIcon image = new ImageIcon(imagePath);
		int originalWidth = image.getIconWidth();
		int originalHeight = image.getIconHeight();
		int ratio = Math.min(this.getWidth() / originalWidth, this.getHeight() / originalHeight);
		imageLabel.setIcon(new ImageIcon(image.getImage().getScaledInstance(originalWidth * ratio, originalHeight * ratio, Image.SCALE_DEFAULT)));
	}
	
	public void setImage(String imagePath) {
		JFrame topFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
		if(imagePath == null) {
			imageLabel.setIcon(null);
			topFrame.setVisible(false);
		}
		else {
			topFrame.setVisible(true);
			if(ImageFinder.isPDF(new File(imagePath))) {
				imagePath = imagePath + ".png_";
			}
			ImageIcon image = new ImageIcon(imagePath);
			
			int originalWidth = image.getIconWidth();
			int originalHeight = image.getIconHeight();
			if(originalWidth > originalHeight) {
				imageLabel.setIcon(new ImageIcon(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT)));
			}
			else {
				double ratio = Math.min((double)(this.getWidth()) / originalWidth, (double)(this.getHeight()) / originalHeight);
				long newWidth = Math.round(originalWidth * ratio);
				long newHeight = Math.round(originalHeight * ratio);
				imageLabel.setIcon(new ImageIcon(image.getImage().getScaledInstance((int)newWidth, (int)newHeight, Image.SCALE_DEFAULT)));
			}
		}
		this.setVisible(false);
		this.setVisible(true);
	}
}
