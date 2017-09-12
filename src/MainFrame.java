import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
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
		
		imageGallery = new ImageGallery();
		imageGallery.setOnImageSelectedListener(new ImageGallery.OnImageSelectedListener() {
			
			@Override
			public void onImageSelected(String imagePath) {
				m_imageViewerPanel.setImage(imagePath);
			}

			@Override
			public void onBroken() {
				m_imageViewerPanel.setImage(null);
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
		
		this.add(navigationSplitPane);
		
		JFrame imageViewFrame = new JFrame();
		
		m_imageViewerPanel = new ImageViewerPanel();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		
		imageViewFrame.setLayout(new GridLayout(1, 1));
		imageViewFrame.add(m_imageViewerPanel);
		if(gs.length == 1) {
			this.setSize(width / 2, height - 50);
			imageViewFrame.setSize(width / 2, height - 50);
			imageViewFrame.setLocation(width / 2, 0);
		}
		else {
			gs[0].setFullScreenWindow(this);
			gs[1].setFullScreenWindow(imageViewFrame);
		}
		
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationSplitPane, m_imageViewerPanel);
//		splitPane.setDividerLocation(width / 2);
		
//		this.add(splitPane);
		
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
//		imageViewFrame.setVisible(true);
	}
}