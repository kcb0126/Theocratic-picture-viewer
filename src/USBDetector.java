import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.filechooser.FileSystemView;

public class USBDetector {
	private OnPlugUSBListener m_OnPlugUSBListener;
	
	private Timer m_Timer;
	private TimerTask m_TimerTask;
	
	private Vector<String> USBDriveLetters = new Vector<String>();
	
	private final void findUSB() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		
		try {
		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i ++) {
			if(roots[i] == null) continue;
			if ((fsv.getSystemTypeDescription(roots[i]).contains("USB")
					|| fsv.getSystemTypeDescription(roots[i]).contains("Removable"))
					&& !(USBDriveLetters.contains(roots[i].getPath()))) {
				USBDriveLetters.addElement(roots[i].getPath());
				
				this.m_OnPlugUSBListener.onPlugUSB(roots[i]);
				
				// Now, I can detect only one USB drive. I will fix it later...
//				break;
//				System.out.println(roots[i].getPath());
			}
		}
		
		USBDriveLetters_loop:
		for (int j = USBDriveLetters.size() - 1; j > -1; j --) {
			for (int k = 0; k < roots.length; k ++) {
				if ((fsv.getSystemTypeDescription(roots[k]).contains("USB")
					|| fsv.getSystemTypeDescription(roots[k]).contains("Removable"))
					&& USBDriveLetters.get(j).equals(roots[k].getPath())) {
					continue USBDriveLetters_loop;
				}
			}
			USBDriveLetters.removeElementAt(j);
			this.m_OnPlugUSBListener.onUnplugUSB();
		}
		}
		catch(Exception e) {
			this.m_OnPlugUSBListener.onUnplugUSB();
		}
	}
	
	public USBDetector() {
		this.m_OnPlugUSBListener = null;
	}
	
	public USBDetector(OnPlugUSBListener onPlugUSBListener) {
		this.setOnPlugUSBListener(onPlugUSBListener);
	}
	
	public void setOnPlugUSBListener(OnPlugUSBListener onPlugUSBListener) {
		this.m_OnPlugUSBListener = onPlugUSBListener;
		if (m_Timer != null) {
			m_Timer.cancel();
		}
		m_Timer = new Timer();
		TimerTask m_TimerTask = new TimerTask() {
			@Override
			public void run() {
				findUSB();
			}
		};
		
		m_Timer.scheduleAtFixedRate(m_TimerTask, 0, 1000);
	}
	
	public interface OnPlugUSBListener {
		void onPlugUSB(File Drive);
		void onUnplugUSB();
	}
}