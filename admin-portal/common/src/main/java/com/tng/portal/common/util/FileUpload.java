package com.tng.portal.common.util;

import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUpload {	
	
	private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);
	
	private FileUpload(){}

	/**
	 * upload a file to file server
	 * 
	 * @param ufile
	 *            file to be uploaded
	 * @param fpath
	 *            server path
	 * @param newName
	 *            The file name to be stored. Null will be random name
	 * @param replace
	 *            true - replace existing file
	 * @return new file name
	 * @throws Exception
	 */
	public static String upload(MultipartFile ufile, String fpath, String newName, boolean replace) 
			throws Exception {

		if (ufile == null || ufile.isEmpty()) {
			return "{\"warning\":\"File is empty!\"}";
		}

		// create remote directory
		File dir = new File(fpath);
		if (!dir.exists()) {
			boolean isCreated = dir.mkdirs();
			if (!isCreated) {
				return "{\"warning\":\"Can't create folder!\"}";
			}
		}
		
		// create destination file
		File destFile = null;		
		if ((newName == null ? "" : newName).length() == 0) {
			String orinalName = ufile.getOriginalFilename();			
			destFile = createNewFile(dir, orinalName);			
		} else {
			destFile = new File(dir, newName);
			
			if (destFile.exists() && !replace) {
				throw new BusinessException("file already exists: " + destFile.getAbsolutePath());
			}
			
			replaceFile(destFile);			
		}

		// copy the file
		try (OutputStream outputStream = new FileOutputStream(destFile);InputStream inputStream = ufile.getInputStream()){
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} 
		
		return destFile.getName();
	}
	
	private static void replaceFile(File destFile) throws IOException, InterruptedException {
		// replace the file.
		if (destFile.exists()) {
			logger.info(""+destFile.delete());
		}
		
		//retry to create new file
		boolean createDone = false;
		int retryCount = 10;
		do {				
			createDone = destFile.createNewFile();
			retryCount--;
			if (!createDone) {							
				Thread.sleep(100);
			}
		} while (!createDone && retryCount > 0);
		
		if (!createDone) {
			throw new BusinessException("file can't be replaced: " + destFile.getAbsolutePath());
		}
		
	}
	
	/**
	 * create new file with random name
	 * @param dir
	 * @param orinalName
	 * @return
	 * @throws Exception
	 */
	private static File createNewFile(File dir, String orinalName) {
		String suffix = orinalName.substring(orinalName.lastIndexOf('.'));
		
		File destFile = null;		
		do {
			String destFileName = UUID.randomUUID().toString().replace("-", "_") + suffix;
			destFile = new File(dir, destFileName);
		} while (destFile.exists());
		
		return destFile;
	}
	
	public static void deleteFile(String filename, String folder)
	{
		File file = new File(folder+"/"+filename);
		
		File thumbnailFile = new File(folder+ "/" + filename);
		if (file.exists()){
			logger.info(""+file.delete());
		}
		if (thumbnailFile.exists()){
			logger.info(""+thumbnailFile.delete());
		}
	}
	
	/**
	 *  Create date format directory 
	 * 
	 * @param parent
	 *             Create under this directory 
	 * @return  Returns the created path and returns the empty string by mistake. 
	 */
	public static String createDateDir(String parent) {
		if (StringUtils.isBlank(parent)) {
			logger.error("StringUtils.isBlank(parent) parent:{}", parent);
			return "";
		}
		DateFormat dateDirFormat = new SimpleDateFormat(DateCode.dateFormatMd);
		String date = dateDirFormat.format(new Date());
		String absolutePath = new File(parent, date).getAbsolutePath();
		return createDir(absolutePath) ? absolutePath : "";
	}

	/**
	 *  Create a directory 
	 * 
	 * @param parent
	 *             Create under this directory 
	 * @return  Create success or directory return true Create failure return false
	 */
	public static boolean createDir(String parent) {
		if (StringUtils.isBlank(parent)) {
			logger.error("StringUtils.isBlank(parent) parent:{}", parent);
			return false;
		}
		File dir = new File(parent);
		if (!dir.exists()) {
			boolean mkdirs = dir.mkdirs();
			if (!mkdirs) {
				logger.error("dir.mkdirs() error");
				return false;
			}
		} else {
			if (!dir.isDirectory()) {
				logger.error("!dir.isDirectory() dir:{}", dir.getAbsolutePath());
				return false;
			}
		}
		return true;
	}
	
}
