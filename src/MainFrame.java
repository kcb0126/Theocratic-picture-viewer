import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.nio.channels.FileLock;

import javax.swing.*;

class MainFrame extends JFrame {
	public static MainFrame sharedInstance;
	public ImageFolderTree imageFolderTree;
	public ImageGallery imageGallery;

	public static void main(String[] args) {

		File lockfile = new File(System.getProperty("java.io.tmpdir") + "\\USBDetector");
		lockfile.mkdir();

		if (lockInstance(System.getProperty("java.io.tmpdir") + "\\USBDetector\\app.lock")) {
			sharedInstance = new MainFrame();
		} else {
			JOptionPane.showMessageDialog(null, "The application is already running");
			System.exit(0);
		}
	}

	private JFrame imageViewFrame;
	private boolean galleryFirst = true;
	private ImageViewerPanel m_imageViewerPanel;

	public boolean manuallyIconified = false;
	
	public MainFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;

		this.setTitle("Theocratic Picture Viewer");

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

		imageViewFrame = new JFrame();

		m_imageViewerPanel = new ImageViewerPanel();

		imageViewFrame.setLayout(new GridLayout(1, 1));
		imageViewFrame.add(m_imageViewerPanel);
		imageViewFrame.setUndecorated(true);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
			
			@Override
			public void windowIconified(WindowEvent windowEvent) {
				if(MainFrame.sharedInstance.manuallyIconified) {
					MainFrame.sharedInstance.manuallyIconified = false;
				} else {
					imageViewFrame.setState(JFrame.ICONIFIED);
				}
			}
		});
		
		
		arrangeFrames();
		
		imageViewFrame.setVisible(false);
		// JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		// navigationSplitPane, m_imageViewerPanel);
		// splitPane.setDividerLocation(width / 2);

		// this.add(splitPane);

		USBDetector usbDetector = new USBDetector();
		usbDetector.setOnPlugUSBListener(new USBDetector.OnPlugUSBListener() {

			@Override
			public void onPlugUSB(File Drive) {
				ImageFinder.copyImagesToTemp(Drive, null);
				imageFolderTree.addDir(ImageFinder.tempDrive);
				imageFolderTree.setVisible(false);
				imageFolderTree.setVisible(true);
			}

			@Override
			public void onUnplugUSB() {
				imageFolderTree.unplug();
				imageGallery.unPlug();
				imageViewFrame.setVisible(false);
			}
		});

		this.setVisible(true);
		imageViewFrame.setVisible(true);
	}

	private void arrangeFrames() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		JFrame first, second;
		if(galleryFirst) {
			first = this; second = imageViewFrame;
		} else {
			first = imageViewFrame; second = this;
		}
		
		if (gs.length == 1) {
			first.setLocation(0, 0);
			first.setSize(width / 2, height - 50);
			second.setSize(width / 2, height - 50);
			second.setLocation(width / 2, 0);
		} else {
			gs[0].setFullScreenWindow(first);
			gs[1].setFullScreenWindow(second);
		}
	}
	
	public void switchFrames() {
		galleryFirst = !galleryFirst;
		arrangeFrames();
	}
	
	private static boolean lockInstance(final String lockFile) {
		try {
			final File file = new File(lockFile);
			final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			if (fileLock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							fileLock.release();
							randomAccessFile.close();
							file.delete();
						} catch (Exception e) {
							// log.error("Unable to remove lock file: " + lockFile, e);
						}
					}
				});
				return true;
			}
		} catch (Exception e) {
			// log.error("Unable to create and/or lock file: " + lockFile, e);
		}
		return false;
	}
}