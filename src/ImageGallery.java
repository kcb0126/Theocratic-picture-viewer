import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class ImageGallery extends JPanel {
	
	private JPanel picturePanel;
	
	private ArrayList<ImageCellPanel> cells;
	
	private OnImageBrokenListener m_onImageBrokenListener = null;
	
	private File m_dir = null;
	
	private int m_brokenCellIndex = -1;
	
	public ImageGallery() {
		this.setLayout(new BorderLayout(10, 10));
		String strInstruction = "<html><center>Use arrow up/down keys to show the image on the secondary screen.<br>Supported file types PNG, GIF, JPG, JPEG, PDF and TIF</center></html>";
		this.add(new JLabel(strInstruction, SwingConstants.CENTER), BorderLayout.NORTH);
		picturePanel = new JPanel();
		picturePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		this.add(picturePanel, BorderLayout.CENTER);
		cells = new ArrayList<ImageCellPanel>();
		JButton removeButton = new JButton("Remove Selected");
		removeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < cells.size(); i ++) {
					if(cells.get(i).isChecked()) {
						File file = new File(cells.get(i).getImagePath());
						file.delete();
						File file1 = new File(cells.get(i).getImagePath().replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
						System.out.println(file1.getPath());
						file1.delete();
					}
				}
				setDir(m_dir);
				MainFrame.sharedInstance.imageFolderTree.refresh();
			}
		});
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < cells.size(); i ++) {
					File file = new File(cells.get(i).getImagePath());
					file.delete();
					File file1 = new File(cells.get(i).getImagePath().replace(ImageFinder.tempDrivePath, ImageFinder.tempDriveLetter + ":"));
					System.out.println(file1.getPath());
					file1.delete();
				}
				setDir(m_dir);
				MainFrame.sharedInstance.imageFolderTree.refresh();
			}
		});
		JButton upButton = new JButton("UP");
		upButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cells.size() == 0) {
					return;
				}
				
				int cellCountInRow = MainFrame.sharedInstance.imageGallery.getWidth() / cells.get(0).getWidth();
				
				int newBrokenCellIndex = Math.max(m_brokenCellIndex - cellCountInRow, 0);

				breakCellAt(newBrokenCellIndex);
			}
		});
		JButton downButton = new JButton("DOWN");
		JButton exitButton = new JButton("EXIT");
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		JPanel buttonGroup = new JPanel();
		buttonGroup.add(removeButton);
		buttonGroup.add(clearButton);
//		buttonGroup.add(upButton);
//		buttonGroup.add(downButton);
		buttonGroup.add(exitButton);
		this.add(buttonGroup, BorderLayout.SOUTH);
		this.setFocusable(true);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent arg0) {
				int newBrokenCellIndex, cellCountInRow;
				if(arg0.getID() == KeyEvent.KEY_PRESSED) {
					switch(arg0.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						newBrokenCellIndex = Math.max(m_brokenCellIndex - 1, 0);
						breakCellAt(newBrokenCellIndex);
						break;
					case KeyEvent.VK_UP:
						cellCountInRow = MainFrame.sharedInstance.imageGallery.getWidth() / cells.get(0).getWidth();
						newBrokenCellIndex = Math.max(m_brokenCellIndex - cellCountInRow, 0);
						breakCellAt(newBrokenCellIndex);
						break;
					case KeyEvent.VK_RIGHT:
						newBrokenCellIndex = Math.min(m_brokenCellIndex + 1, cells.size() - 1);
						breakCellAt(newBrokenCellIndex);
						break;
					case KeyEvent.VK_DOWN:
						cellCountInRow = MainFrame.sharedInstance.imageGallery.getWidth() / cells.get(0).getWidth();
						newBrokenCellIndex = Math.min(m_brokenCellIndex + cellCountInRow, cells.size() - 1);
						breakCellAt(newBrokenCellIndex);
						break;
					default:
						
					}
				}
				return false;
			}
		});
	}
	
/*	public ImageGallery(File dir) {
		this.m_dir = new File(dir.getPath());
		this.setLayout(new BorderLayout(10, 10));
		String strInstruction = "<html><center>Use arrow up/down keys to show the image on the secondary screen.<br>Supported file types PNG, GIF, JPG, JPEG, PDF and TIF</center></html>";
		this.add(new JLabel(strInstruction, SwingConstants.CENTER), BorderLayout.NORTH);
		picturePanel = new JPanel();
		//picturePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		this.add(picturePanel, BorderLayout.CENTER);
		File[] listFiles = dir.listFiles();
		cells = new ArrayList<ImageCellPanel>();
		int i = 0;
		for(File file : listFiles) {
			if(ImageFinder.isImage(file)) {
				i ++;
				ImageCellPanel cell = new ImageCellPanel(i, file.getPath());
				cell.setOnBreakListener(new ImageCellPanel.OnBreakListener() {
					
					@Override
					public void onBreak(int index) {
						breakCellAt(index);
					}
				});
				cells.add(cell);
				picturePanel.add(cell);
			}
		}
		JButton removeButton = new JButton("Remove Selected");
		JButton clearButton = new JButton("Clear");
		JButton upButton = new JButton("UP");
		JButton downButton = new JButton("DOWN");
		JButton exitButton = new JButton("EXIT");
		JPanel buttonGroup = new JPanel();
		buttonGroup.add(removeButton);
		buttonGroup.add(clearButton);
		buttonGroup.add(upButton);
		buttonGroup.add(downButton);
		buttonGroup.add(exitButton);
		this.add(buttonGroup, BorderLayout.SOUTH);
		picturePanel.setVisible(false);
		picturePanel.setVisible(true);
	}
*/	
	public void setDir(File dir) {
		this.m_dir = new File(dir.getPath());
		picturePanel.removeAll();
		File[] listFiles = dir.listFiles();
		cells.clear();
		int i = 0;
		for(File file : listFiles) {
			if(ImageFinder.isImage(file)) {
				ImageCellPanel cell = new ImageCellPanel(i, file.getPath());
				cell.setOnBreakListener(new ImageCellPanel.OnBreakListener() {
					
					@Override
					public void onBreak(int index) {
						breakCellAt(index);
					}
				});
				cells.add(cell);
				picturePanel.add(cell);
				i ++;
			}
		}
		this.m_brokenCellIndex = -1;
		picturePanel.repaint();
		picturePanel.setVisible(false);
		picturePanel.setVisible(true);
	}
	
	public void setOnImageBrokenListener(OnImageBrokenListener onImageBrokenListener) {
		this.m_onImageBrokenListener = onImageBrokenListener;
	}
	
	private void breakCellAt(int index) {
		if(this.m_onImageBrokenListener != null) {
			this.m_onImageBrokenListener.onImageBroken(cells.get(index).getImagePath());
		}
		
		this.m_brokenCellIndex = index;
		
		for(int i = 0; i < cells.size(); i ++) {
			if(i == index) {
				cells.get(i).setBroken(true);
			}
			else {
				cells.get(i).setBroken(false);
			}
		}
	}
	
	public interface OnImageBrokenListener {
		public void onImageBroken(String imagePath);
	}
}