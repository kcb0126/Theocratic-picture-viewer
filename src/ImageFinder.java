
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.filechooser.FileSystemView;

public class ImageFinder {
	private static final String[] file_types = {"png", "gif", "jpg", "jpeg", "tif", "pdf", "bmp"};
	
	public static File tempDrive;
	
	public static String tempDrivePath;
	
	public static String tempDriveLetter;
	
	public static boolean isImage(File file) {
		if(!(file.isDirectory())) {
			try {
				String type = Files.probeContentType(file.toPath());
				if(type != null) {
					for(int i = 0; i < file_types.length; i ++) {
						if(type.toLowerCase().contains(file_types[i])) {
							return true;
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isPDF(File file) {
		if(isImage(file)) {
			try {
				String type = Files.probeContentType(file.toPath());
				if(type != null) {
					if(type.toLowerCase().contains("pdf")) {
						return true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean hasImage(File file) {
		File[] list = file.listFiles();
		if(list!=null) {
			for(File fil : list) {
				if(fil.isDirectory()) {
					if(hasImage(fil)) {
						return true;
					}
				}
				else {
					if( isImage(fil)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean hasImageAsSon(File file) {
		File[] list = file.listFiles();
		for(File fil : list) {
			if(!fil.isDirectory()) {
				if(isImage(fil)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void copyImagesToTemp(File dir, File tmpDir) {
		if(tmpDir == null) {
			tmpDir = new File(System.getProperty("java.io.tmpdir") + "\\USBDetector");
			tmpDir.mkdirs();
		}
		if(hasImage(dir)) {
			String dirName = dir.getName();
			if(dirName.equals("")) {
				dirName = dir.getPath().substring(0, 1);
				tempDrivePath = tmpDir.getPath() + "\\" + dirName;
				tempDrive = new File(tempDrivePath);
				tempDriveLetter = dirName;
			}
			File newTmpDir = new File(tmpDir.getPath() + "\\" + dirName);
			newTmpDir.mkdir();
			File[] list = dir.listFiles();
			for(File file : list) {
				if(file.isDirectory()) {
					copyImagesToTemp(file, newTmpDir);
				}
				else {
					if(isImage(file)) {
						try {
							Files.copy(file.toPath(), new File(newTmpDir.getPath() + "\\" + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
							if(isPDF(file)) {
								ConvertPDFPagesToImages.convert(new File(newTmpDir.getPath() + "\\" + file.getName()));
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public void notmain() {
		System.out.println("Hello, world!");
		
		Timer m_timer = new Timer();
		TimerTask m_task = new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("Morph");
			}
		};
		
//		m_timer.schedule(m_task, 1000, 2000);
		
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] roots = fsv.getRoots();
		
		for (int i = 0; i < roots.length; i ++) {
			System.out.println("Roots: " + roots[i]);
		}
		
		System.out.println("Home directory: " + fsv.getHomeDirectory());
		
		File[] f = File.listRoots();
		for (int i = 0; i < f.length; i ++) {
			System.out.println("Drive: " + f[i]);
			System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
			System.out.println("Is drive: " + fsv.isDrive(f[i]));
			System.out.println("Is floppy: " + fsv.isFloppyDrive(f[i]));
			System.out.println("Readable: " + f[i].canRead());
			System.out.println("Writable: " + f[i].canWrite());
			
			System.out.println("Is USB: " + fsv.getSystemTypeDescription(f[i]).contains("USB"));
			
			if (fsv.getSystemTypeDescription(f[i]).contains("USB")) {
				hasImage(f[i]);
			}
		}
	}
}
