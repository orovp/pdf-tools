package com.orovp.doctools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

public class DiffProcessor implements ItemProcessor<Document, Document> {

	private static final Logger LOG = LoggerFactory.getLogger(DiffProcessor.class);
	
	@Value("${default.input.dir.before}") String beforeDirPath;
	@Value("${default.input.dir.after}") String afterDirPath;
	
	@Override
	public Document process(final Document document) throws Exception {
		
		PDDocument docBefore = PDDocument.load(new File(beforeDirPath + "/" + document.getFileName()));
		PDDocument docAfter = PDDocument.load(new File(afterDirPath + "/" + document.getFileName()));
		
		PDFRenderer beforePDF = new PDFRenderer(docBefore);
		PDFRenderer afterPDF = new PDFRenderer(docAfter);
		
		HashMap<Integer, BufferedImage> diffMap = new HashMap<Integer, BufferedImage>();
		
		if(docBefore.getNumberOfPages() == docAfter.getNumberOfPages()) {
			for(int i=0; i<docBefore.getNumberOfPages(); i++){
				BufferedImage imgBefore = beforePDF.renderImageWithDPI(i, 300, ImageType.RGB);
				BufferedImage imgAfter = afterPDF.renderImageWithDPI(i, 300, ImageType.RGB);
				diff(imgBefore,imgAfter,i,diffMap);
			}
			docBefore.close();
			docAfter.close();
			final Document finalDocument = new Document(diffMap, document.getFileName());
			
			return finalDocument;
		}else{
			LOG.info("PDF with different number of pages.");
			docBefore.close();
			docAfter.close();
			return null;
		}
		
		
	}

	static void diff(final BufferedImage imgBefore, final BufferedImage imgAfter, int pageNumber, HashMap<Integer, BufferedImage> diffMap){
		
		final int w = imgBefore.getWidth();
		final int h = imgBefore.getHeight();
		final int[] pageBefore = imgBefore.getRGB(0, 0, w, h, null, 0, w);
		final int[] pageAfter = imgAfter.getRGB(0, 0, w, h, null, 0, w);
		
		if(!(java.util.Arrays.equals(pageBefore, pageAfter))){
			LOG.info("Image with diff on page number" + pageNumber);
			for(int i = 0; i<pageBefore.length; i++){
				if (pageBefore[i] != pageAfter[i]){
					pageBefore[i] = Color.GREEN.getRGB();
				}
			}
			final BufferedImage diff = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			diff.setRGB(0, 0, w, h, pageBefore, 0, w);
			diffMap.put(pageNumber, diff);
		}
		
	}
	
}
