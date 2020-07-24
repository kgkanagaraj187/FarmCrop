package com.sourcetrace.eses.txn.agrocert;

import java.util.Date;

public class DownloadTask implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static int STATUS_START=0;
    public static int STATUS_END=1;
    public static int STATUS_ERROR=3;
    public static int STATUS_DOWNLOADED=4;
    
    public static final String FILE_TYPE_EXCEL = "EXCEL";
    public static final String FILE_TYPE_KML = "KML";
    
	private Long id;
	private String userName;
	private Date startTime;
	private int progressStatus;
	private String fileName;
	private String fileType;
	private String description;
	private byte[] fileData;
	private int status;
	private Date endTime;
	
	public DownloadTask() {
	}

	public DownloadTask(String userName, Date startTime, int progressStatus,
			String fileName, String fileType, String description,
			byte[] fileData, int status) {
		super();
		this.userName = userName;
		this.startTime = startTime;
		this.progressStatus = progressStatus;
		this.fileName = fileName;
		this.fileType = fileType;
		this.description = description;
		this.fileData = fileData;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getProgressStatus() {
		return progressStatus;
	}

	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	

}
