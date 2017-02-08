package com.java.utils.searchFile;

import java.io.File;

public class FileDTO {
	
	private File file;

	private static final long serialVersionUID = 1L;

	private int commentLines;
	
	private int maxBlockLines;

	public FileDTO(String filePath) {
		this.file = new File(filePath);
	}
	
	public FileDTO(File file) {
		this.file=file;
	}
	public int getCommentLines() {
		return commentLines;
	}

	public void setCommentLines(int comments) {
		this.commentLines = comments;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public int getMaxBlockLines() {
		return maxBlockLines;
	}

	public void setMaxBlockLines(int maxBlockLines) {
		this.maxBlockLines = maxBlockLines;
	}
}
