package com.sourcetrace.esesw.entity.profile;

public class Asset {

	public static final String APP_LOGO = "app_logo";
	public static final String FAVICON = "favicon";
	public static final String DATAIMPORT = "DATAIMPORT";
	public static final String PRINTER_PRN = "PRINTER_PRN";
	public static final String QR_LOGO = "qr_logo";
	
	private long id;
	private String code;
	private String description;
	private String content;
	private byte[] file;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
}
