import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

class ImageFolderTree extends JPanel {
	private OnFolderSelectedListener m_OnFolderSelectedListener = null;
	private JTree m_tree = null;
	private File m_dir;
	private JLabel message;
	
	
	/*Constructor*/
	public ImageFolderTree() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
		message = new JLabel("<html>Please add your USB device and wait for the directory to appear below.  Then select a folder to show the picture files within the folder.</html>");
		message.setForeground(Color.WHITE);
		this.add(message, BorderLayout.NORTH);
	}
/*	
	public ImageFolderTree(File dir) {
		this.m_dir = new File(dir.getPath());
		this.setLayout(new BorderLayout());
		
//		setDir(dir);
		/*
		// Make a tree list with all the nodes, and make it a JTree
		m_tree = new JTree(addNodes(null, dir));
		
		// Add a listener
		m_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (node.getChildCount() > 0) {
					m_OnFolderSelectedListener.onFolderSelected(new File(node.toString()));
				}
			}
		});
		//**
		// Lastly, put the JTree into a JScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(m_tree);
		message = new JLabel("<html>Please add your USB device and wait for the directory to appear below.  Then select a folder to show the picture files within the folder.</html>");
		message.setForeground(Color.WHITE);
		this.add(message, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
	}
*/	
	public void setDir(File dir) {
		this.m_dir = new File(dir.getPath());
		this.removeAll();
		
		// Make a tree list with all the nodes, and make it a JTree
		m_tree = new JTree(new DefaultMutableTreeNode());
		m_tree.setRootVisible(false);
		m_tree.setBackground(Color.BLACK);
		m_tree.setForeground(Color.WHITE);
		m_tree.setCellRenderer(new MyCellRenderer());
		DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
//		root.add(addNodes(null, dir));
		Vector<File> imageDirs = findImageDirs(dir);
		for(int i = 0; i < imageDirs.size(); i ++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(imageDirs.get(i).getPath().replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
			root.add(node);
			for(File file : imageDirs.get(i).listFiles()) {
				if(!file.isDirectory()) {
					if(ImageFinder.isImage(file)) {
						DefaultMutableTreeNode imageNode = new DefaultMutableTreeNode(file.getPath().replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
						node.add(imageNode);
					}
				}
			}
		}
		
		
		model.reload(root);
//		m_tree = new JTree(addNodes(null, dir));
		
		// Add a listener
		String strTempDrivePath = ImageFinder.tempDrivePath;
		m_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (node.getChildCount() > 0) {
					String path = strTempDrivePath + node.toString().substring(2);
					m_OnFolderSelectedListener.onFolderSelected(new File(path));
					System.out.println("valueChanged " + path);
				}
			}
		});
		// Lastly, put the JTree into a JScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(m_tree);
		if(imageDirs.size() > 0) {
			message = new JLabel("<html>You can now remove the USB drive.  The pictures are loaded onto this machine and will auto-delete when you close this program.<br>Please select a folder below to show the picture files within the folder.</html>");
			message.setForeground(Color.WHITE);
			this.add(message, BorderLayout.NORTH);
		}
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addDir(File dir) {
		message.setText("<html>You can now remove the USB drive.  The pictures are loaded onto this machine and will auto-delete when you close this program.<br>Please select a folder below to show the picture files within the folder.</html>");
		this.add(message, BorderLayout.NORTH);
//		if (this.m_dir == null) {
		if (true) {
			this.setDir(dir);
		}
		else {
			this.m_dir = new File(dir.getPath());
			DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
//			root.add(addNodes(null, dir));
			Vector<File> imageDirs = findImageDirs(dir);
			for(int i = 0; i < imageDirs.size(); i ++) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(imageDirs.get(i).getPath().replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
				root.add(node);
				for(File file : imageDirs.get(i).listFiles()) {
					if(!file.isDirectory()) {
						if(ImageFinder.isImage(file)) {
							DefaultMutableTreeNode imageNode = new DefaultMutableTreeNode(file.getPath().replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
							node.add(imageNode);
						}
					}
				}
			}
			model.reload(root);
		}
	}
	
	private Vector<File> findImageDirs(File dir) {
		Vector<File> dirs = new Vector<File>();
		if(ImageFinder.hasImageAsSon(dir)) {
			dirs.add(dir);
		}
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i ++) {
			if (files[i].isDirectory()) {
				dirs.addAll(findImageDirs(files[i]));
			}
		}
		return dirs;
	}
	
	public void refresh() {
		this.setDir(this.m_dir);
	}
	
	public void unplug() {
		this.removeAll();
		
		// Make a tree list with all the nodes, and make it a JTree
		m_tree = new JTree(new DefaultMutableTreeNode());
		m_tree.setRootVisible(false);
		m_tree.setBackground(Color.BLACK);
		m_tree.setForeground(Color.WHITE);
		m_tree.setCellRenderer(new MyCellRenderer());
		this.add(m_tree, BorderLayout.CENTER);
		message = new JLabel("<html>Please add your USB device and wait for the directory to appear below.  Then select a folder to show the picture files within the folder.</html>");
		message.setForeground(Color.WHITE);
		this.add(message, BorderLayout.NORTH);
		this.setVisible(false);
		this.setVisible(true);
	}
	
	public void setOnFolderSelectedListener(OnFolderSelectedListener onFolderSelectedListener) {
		this.m_OnFolderSelectedListener = onFolderSelectedListener;
	}
	
	/**
	 * Add nodes from under "dir" into curTop. Highly recursive.
	 * */
	DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath.replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
		if (curTop != null) { // should only be null at root
			curTop.add(curDir);
		}
		Vector ol = new Vector();
		String[] tmp = dir.list();
		for(int i = 0; i < tmp.length; i ++) {
			ol.addElement(tmp[i]);
		}
		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		File f;
		Vector files = new Vector();
		// Make two passes, one for Dirs and one for Files. This is #1.
		for (int i = 0; i < ol.size(); i ++) {
			String thisObject = (String) ol.elementAt(i);
			String newPath;
			if (curPath.replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":").equals("")) {
				newPath = thisObject;
			}
			else {
				newPath = curPath + File.separator + thisObject;
			}
			
			f = new File(newPath);
			if (f.isDirectory()) {
				if (ImageFinder.hasImage(f))
					addNodes(curDir, f);
			}
			else {
				if (ImageFinder.isImage(f))
					files.addElement(thisObject);
			}
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++) {
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		}
		return curDir;
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(200, 400);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(200, 400);
	}
	
	public interface OnFolderSelectedListener {
		void onFolderSelected(File folder);
	}
	
/*	
	public static void notmain(String[] args) {
		JFrame frame = new JFrame("FileTree");
		frame.setBackground(Color.BLACK);
		frame.setBackground(Color.LIGHT_GRAY);
		Container cp = frame.getContentPane();
		
//		cp.add(new ImageFolderTree(new File(".")));
		
		USBDetector usbDetector = new USBDetector();
		usbDetector.setOnPlugUSBListener(new USBDetector.OnPlugUSBListener() {
			@Override
			public void onPlugUSB(File Drive) {
				cp.removeAll();
				cp.add(new ImageFolderTree(Drive));
				frame.pack();
			}

			@Override
			public void onUnplugUSB() {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
*/	
	public class MyCellRenderer extends DefaultTreeCellRenderer {
		
		@Override
		public Color getBackgroundNonSelectionColor() {
			return (null);
		}
		
	    @Override
	    public Color getBackgroundSelectionColor() {
	        return Color.GREEN;
	    }

	    @Override
	    public Color getBackground() {
	        return (null);
	    }

	    @Override
	    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
	        final Component ret = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

	        final DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
	        this.setText(value.toString());
	        setForeground(Color.WHITE);
	        return ret;
	    }	
    }
}