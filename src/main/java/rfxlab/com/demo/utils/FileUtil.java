package rfxlab.com.demo.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	public static void listFilesForFolder(final File folder, List<String> files) {
		File[] fList = folder.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				listFilesForFolder(file, files);
			}
		}
	}
	
	public static String createDirectory(String directoryPath) {
	    File dir = new File(directoryPath);
	    if (!dir.isDirectory()) {
	        dir.mkdir();
        }
	    return dir.getAbsolutePath();
	}
	
}
