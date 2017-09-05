import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

class ImageFolderTree extends JPanel {
	private OnFolderSelectedListener m_OnFolderSelectedListener = null;
	private JTree m_tree;
	private File m_dir;
	
	/*Constructor*/
	public ImageFolderTree() {
		this.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(m_tree);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public ImageFolderTree(File dir) {
		this.m_dir = new File(dir.getPath());
		this.setLayout(new BorderLayout());
		
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
		// Lastly, put the JTree into a JScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(m_tree);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setDir(File dir) {
		this.m_dir = new File(dir.getPath());
		this.removeAll();
		
		// Make a tree list with all the nodes, and make it a JTree
		m_tree = new JTree(addNodes(null, dir));
		
		// Add a listener
		m_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (node.getChildCount() > 0) {
					String path = ImageFinder.tempDrivePath + node.toString().substring(2);
					m_OnFolderSelectedListener.onFolderSelected(new File(path));
				}
			}
		});
		// Lastly, put the JTree into a JScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(m_tree);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void refresh() {
		this.setDir(this.m_dir);
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
	
	
	public static void main(String[] args) {
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
		});
		
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}