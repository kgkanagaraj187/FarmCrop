package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentUpload {
    
    public static final List<String> DOC_TYPE_LIST = new ArrayList<String>(Arrays.asList(new String[] { "pdf", "doc", "docx", "xls", "xlsx" }));
    public static final int UPLOAD_DOC_SIZE = 1024;
    
    private long id;
    private Farm farm;
    private String name;
    private byte[] content;
    
    
    private File docFile;
    private String docFileFileName;
    private String docFileContentType;

    
    
    public long getId() {
    
        return id;
    }
    public void setId(long id) {
    
        this.id = id;
    }
    public Farm getFarm() {
    
        return farm;
    }
    public void setFarm(Farm farm) {
    
        this.farm = farm;
    }
    public String getName() {
    
        return name;
    }
    public void setName(String name) {
    
        this.name = name;
    }
    public byte[] getContent() {
    
        return content;
    }
    public void setContent(byte[] content) {
    
        this.content = content;
    }
    public File getDocFile() {
    
        return docFile;
    }
    public void setDocFile(File docFile) {
    
        this.docFile = docFile;
    }
    public String getDocFileFileName() {
    
        return docFileFileName;
    }
    public void setDocFileFileName(String docFileFileName) {
    
        this.docFileFileName = docFileFileName;
    }
    public String getDocFileContentType() {
    
        return docFileContentType;
    }
    public void setDocFileContentType(String docFileContentType) {
    
        this.docFileContentType = docFileContentType;
    }
   

}
