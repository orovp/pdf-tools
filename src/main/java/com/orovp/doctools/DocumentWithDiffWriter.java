package com.orovp.doctools;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

public class DocumentWithDiffWriter implements ItemWriter<Document>{

	private static final Logger LOG = LoggerFactory.getLogger(DocumentWithDiffWriter.class);
	
	@Value("${default.output.dir.diff}") String outputDir;
	
	@Override
	public void write(List<? extends Document> documents) throws Exception {
		LOG.info("Starting Diff Writer");
		for (Document doc : documents) {
			int pageNumber = 1;
			
			while (doc.getDiff().containsKey(pageNumber-1)){	
				File outputFolder = new File(outputDir+ "/" + doc.getFileName());
				LOG.info("Creating diff subfolder for document " + doc.getFileName());
				outputFolder.mkdirs();
				File outputFile = new File(outputDir + "/" + doc.getFileName() + "/page-" + pageNumber + ".png");
				LOG.info("Writing file for document " + doc.getFileName() + " and page number (" + pageNumber +")");
				ImageIO.write(doc.getDiff().get(pageNumber-1), "png", outputFile);
				pageNumber++;
			}
			
		}
		
	}
}
