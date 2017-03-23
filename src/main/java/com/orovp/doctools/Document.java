package com.orovp.doctools;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Document {

	private HashMap<Integer, BufferedImage> diff = new HashMap<Integer, BufferedImage>();
	private String fileName;
		
	public Document(HashMap<Integer, BufferedImage> diff) {
		super();
		this.diff = diff;
	}
	
	public Document(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	public Document(HashMap<Integer, BufferedImage> diff, String fileName) {
		super();
		this.diff = diff;
		this.fileName = fileName;
	}
	
	public HashMap<Integer, BufferedImage> getDiff() {
		return diff;
	}
	public void setDiff(HashMap<Integer, BufferedImage> diff) {
		this.diff = diff;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
