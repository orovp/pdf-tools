package com.orovp.doctools;

import org.apache.pdfbox.rendering.PDFRenderer;

public class Comparison {

	private PDFRenderer before;
	private PDFRenderer after;
	
	
	public PDFRenderer getAfter() {
		return after;
	}
	public void setAfter(PDFRenderer after) {
		this.after = after;
	}
	public PDFRenderer getBefore() {
		return before;
	}
	public void setBefore(PDFRenderer before) {
		this.before = before;
	}
	
}
