package com.orovp.doctools;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class DocumentReader implements ItemReader<Document>{

	private static final Logger LOG = LoggerFactory.getLogger(DocumentReader.class);
	
	private int documentIndex;
	HashMap<Integer, String> filesToBeCompared = new HashMap<Integer, String>();

	
	private String beforeDirPath;
	private String afterDirPath;
	
	DocumentReader(String beforeDirPath, String afterDirPath){
		this.beforeDirPath = beforeDirPath;
		this.afterDirPath = afterDirPath;
		initialize();
	}

	
	private void initialize(){
			
		Path beforeFolder = Paths.get(beforeDirPath);
		try (DirectoryStream<Path> streamBeforeFolder = Files.newDirectoryStream(beforeFolder, "*.pdf")) {

			int iKeyMap = 0;
			for (Path entry : streamBeforeFolder) {
				if (new File(afterDirPath + "/" + entry.getFileName()).exists()) {
					filesToBeCompared.put(iKeyMap, entry.getFileName().toString());
					iKeyMap++;
				}else{
					LOG.warn("Before file [" + entry.getFileName() + "] does not have a twin into after folder [" + afterDirPath +"].");
				}
			}
		} catch (IOException ex) {
			LOG.error("An I/O problem has ocurred");
		}
		
		documentIndex = 0;
	}
	
	@Override
	public Document read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		
		
		if (filesToBeCompared.containsKey(documentIndex)){
			documentIndex++;
			return new Document(filesToBeCompared.get(documentIndex-1));
		}else{
			return null;
		}

	}
	
	

}
