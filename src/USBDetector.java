import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.filechooser.FileSystemView;

public class USBDetector {
	private OnPlugUSBListener m_OnPlugUSBListener;
	
	private Timer m_Timer;
	private TimerTask m_TimerTask;
	
	private Vector USBDriverLetters = new Vector();
	
	private final void findUSB() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		
		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i ++) {
			if(roots[i] == null) continue;
			if ((fsv.getSystemTypeDescription(roots[i]).contains("USB")
			    		|| fsv.getSystemTypeDescription(roots[i]).contains("Removable"))
					&& !(USBDriverLetters.contains(roots[i].getPath()))) {
				USBDriverLetters.addElement(roots[i].getPath());
				
				this.m_OnPlugUSBListener.onPlugUSB(roots[i]);
				
				// Now, I can detect only one USB drive. I will fix it later...
				break;
			}
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
	}
}
