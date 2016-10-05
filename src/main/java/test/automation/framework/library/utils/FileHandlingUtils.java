package test.automation.framework.library.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileHandlingUtils {
	public static void createFile(String path, String filename) throws IOException {
		// Remove the end / if present
		if (path.charAt(path.length() - 1) == '\\') {
			path = path.substring(0, path.length() - 1);
		}

		// Initialize file
		File file = new File(path + "\\" + filename);

		// Create Parent dir if not exists
		FileUtils.forceMkdirParent(file);

		// Create file if not exists
		if (!file.exists())
			file.createNewFile();
	}

	public static void createFile(File file) throws IOException {
		// Create Parent dir if not exists
		FileUtils.forceMkdirParent(file);
		
		//Check if it is a directory, if yes throw exception
		if(file.isDirectory())
			throw new IOException("File handle specified in argument, is a directory, please check - "+file.getAbsolutePath());

		// Create file if not exists
		if (!file.exists())
			file.createNewFile();
	}
	
	public static void zipDir(String dirPath) throws IOException {
		File dirPath_f = new File(dirPath);
		
		if(!dirPath_f.exists())
			throw new IOException("Directory specified by path - "+dirPath+" does not exist.");
		
		if(!dirPath_f.isDirectory())
			throw new IOException("Path specifed is not a directory, please recheck. Path - "+dirPath);
	//TODO
	}
}