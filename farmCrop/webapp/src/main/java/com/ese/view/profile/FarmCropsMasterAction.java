/*
 * FarmCropsMasterAction.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class FarmCropsMasterAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;
    @Autowired
    private IUniqueIDGenerator idGenerator;   
    @Autowired
    private IFarmCropsService farmCropsService;
    private FarmCropsMaster farmCropsMaster;  
    private String id;
    private String kmlFileLocation;

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
        // value

        FarmCropsMaster filter = new FarmCropsMaster();

        if (!StringUtil.isEmpty(searchRecord.get("code"))) {
            filter.setCode(searchRecord.get("code").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("name"))) {
            filter.setName(searchRecord.get("name").trim());
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    public JSONObject toJSON(Object obj) {

        FarmCropsMaster farmCropsMaster = (FarmCropsMaster) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + farmCropsMaster.getCode()
                + "</font>");
        rows.add(farmCropsMaster.getName());
        jsonObject.put("id", farmCropsMaster.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {
    	byte [] kmlFileData;
        if (farmCropsMaster == null) {
            command = "create";
            request.setAttribute(HEADING, getText(CREATE));
            return INPUT;
        } else {
            farmCropsMaster.setCode(idGenerator.getFarmCropsMasterCodeSeq());
            if(!ObjectUtil.isEmpty(farmCropsMaster.getKmlFile())){
            	kmlFileData = getKmlFileData(farmCropsMaster.getKmlFile());
                if(!ObjectUtil.isEmpty(farmCropsMaster)){
                	farmCropsMaster.setKmlFileData(kmlFileData);
                }
            }
            farmCropsService.addFarmCropsMaster(farmCropsMaster);                       
            return REDIRECT;
        }
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {
    	
        String view = LIST;
        request.setAttribute(HEADING, getText(LIST));
        if (id != null && !id.equals("")) {
            farmCropsMaster = farmCropsService.findFarmCropsMasterById(Long.parseLong(id));
            if (farmCropsMaster == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            kmlFileLocation = getServerKmlFileLocation();
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText(DETAIL));
        }
        return view;
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {
    	byte[] kmlFileData;
        if (id != null && !id.equals("")) {
            farmCropsMaster = farmCropsService.findFarmCropsMasterById(Long.parseLong(id));
            if (farmCropsMaster == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
            if (farmCropsMaster != null) {
                FarmCropsMaster existing = farmCropsService.findFarmCropsMasterById(farmCropsMaster
                        .getId()); // existing
                // object
                setCurrentPage(getCurrentPage());
                if (existing == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                existing.setName(farmCropsMaster.getName());
                
                if(!ObjectUtil.isEmpty(farmCropsMaster.getKmlFile())){
                	kmlFileData = getKmlFileData(farmCropsMaster.getKmlFile());
                    if(!ObjectUtil.isEmpty(farmCropsMaster)){
                    	existing.setKmlFileData(kmlFileData);
                    }
                }
                
                farmCropsService.editFarmCropsMaster(existing);
                request.setAttribute(HEADING, getText(LIST));
                return LIST;
            }
        }
        return INPUT;
    }

    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception {

        if (this.getId() != null && !(this.getId().equals(EMPTY))) {
            farmCropsMaster = farmCropsService.findFarmCropsMasterById(Long.valueOf(id));
            if (farmCropsMaster == null) {
                addActionError(NO_RECORD);
                return list();
            }
           
            setCurrentPage(getCurrentPage());
            farmCropsService.removeFarmCropsMaster(farmCropsMaster);
        }
        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        return farmCropsMaster;
    }

    /**
     * Sets the farm crops master.
     * @param farmCropsMaster the new farm crops master
     */
    public void setFarmCropsMaster(FarmCropsMaster farmCropsMaster) {

        this.farmCropsMaster = farmCropsMaster;
    }

    /**
     * Gets the farm crops master.
     * @return the farm crops master
     */
    public FarmCropsMaster getFarmCropsMaster() {

        return farmCropsMaster;
    }

    /**
     * Sets the farm crops service.
     * @param farmCropsService the new farm crops service
     */
    public void setFarmCropsService(IFarmCropsService farmCropsService) {

        this.farmCropsService = farmCropsService;
    }

    /**
     * Gets the farm crops service.
     * @return the farm crops service
     */
    public IFarmCropsService getFarmCropsService() {

        return farmCropsService;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }

    public IUniqueIDGenerator getIdGenerator() {
    
        return idGenerator;
    }

    public void setIdGenerator(IUniqueIDGenerator idGenerator) {
    
        this.idGenerator = idGenerator;
    }
    
    public byte[] getKmlFileData(File kmlFile) throws IOException{
    	FileInputStream is = new FileInputStream(kmlFile);
        byte[] data = new byte[is.available()];
        is.read(data);
        is.close();
        return data;
    }

	public String getKmlFileLocation() {
		return kmlFileLocation;
	}

	public void setKmlFileLocation(String kmlFileLocation) {
		this.kmlFileLocation = kmlFileLocation;
	}

	public String getServerKmlFileLocation() throws IOException {
		File tmpFile;
		FileOutputStream fos;
		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		String serverContextPath = request.getContextPath();
		File tmpServerFile = null;
		String tmpFolder = "\\tmpKmlFiles\\";
		String serverUrl = request.getHeader("referer");
		serverUrl = serverUrl.substring(0, serverUrl.lastIndexOf("/"));
		serverUrl = serverUrl.substring(0, serverUrl.lastIndexOf("/"));
		String kmlFileLocation = "";
		if (!ObjectUtil.isEmpty(farmCropsMaster.getKmlFileData())) {
			tmpFile = File.createTempFile("tmpKmlFile", ".kml");
			fos = new FileOutputStream(tmpFile);
			fos.write(farmCropsMaster.getKmlFileData());
			serverFilePath = serverFilePath +tmpFolder+ tmpFile.getName();
			serverContextPath = serverContextPath+tmpFolder+ tmpFile.getName();
			serverFilePath = serverFilePath.replace("\\", "/");
			tmpServerFile = new File(serverFilePath);
			FileUtils.copyFile(tmpFile, tmpServerFile);
			kmlFileLocation = serverContextPath;
			kmlFileLocation = serverUrl+kmlFileLocation;
		}
		if (!StringUtil.isEmpty(kmlFileLocation)) {
			kmlFileLocation = kmlFileLocation.replace("\\", "/");			
		}
		return kmlFileLocation;
	}
	
}
