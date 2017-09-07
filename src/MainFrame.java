import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.*;

class MainFrame extends JFrame {
	public static MainFrame sharedInstance;
	public ImageFolderTree imageFolderTree;
	public ImageGallery imageGallery;
	
	public static void main(String[] args) {
		sharedInstance = new MainFrame();
	}
	
	private ImageViewerPanel m_imageViewerPanel;
	
	public MainFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		
		this.setTitle("THEOCRATIC PICTURE VIEWER");
		
		this.setSize(width, height - 50);
		
		imageGallery = new ImageGallery();
		imageGallery.setOnImageBrokenListener(new ImageGallery.OnImageBrokenListener() {
			
			@Override
			public void onImageBroken(String imagePath) {
				m_imageViewerPanel.setImage(imagePath);
			}
		});

		imageFolderTree = new ImageFolderTree();
		imageFolderTree.setOnFolderSelectedListener(new ImageFolderTree.OnFolderSelectedListener() {
			
			@Override
			public void onFolderSelected(File folder) {
				imageGallery.setDir(folder);
			}
		});
		
		JSplitPane navigationSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imageFolderTree, imageGallery);
		
		navigationSplitPane.setDividerLocation(width / 6);
		Dimension minimumSize = new Dimension(100, 50);
		imageFolderTree.setMinimumSize(minimumSize);
		imageGallery.setMinimumSize(minimumSize);
		
		m_imageViewerPanel = new ImageViewerPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationSplitPane, m_imageViewerPanel);
		splitPane.setDividerLocation(width / 2);
		
		this.add(splitPane);
		
		USBDetector usbDetector = new USBDetector();
		usbDetector.setOnPlugUSBListener(new USBDetector.OnPlugUSBListener() {
			
			@Override
			public void onPlugUSB(File Drive) {
				ImageFinder.copyImagesToTemp(Drive, null);
				imageFolderTree.addDir(ImageFinder.tempDrive);
				imageFolderTree.setVisible(false);
				imageFolderTree.setVisible(true);
			}
		});
		
		this.setVisible(true);
	}
}