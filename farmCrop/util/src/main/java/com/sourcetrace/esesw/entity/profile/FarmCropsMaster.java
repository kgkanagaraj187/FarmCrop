/*
 * FarmCropsMaster.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class FarmCropsMaster {

	 private long id;
	    private String code;
	    private String name;
	    private long revisionNo;
	    private Set<FarmCrops> farmCrops;
	    private File kmlFile;
	    private byte[] kmlFileData;
	    private String kmlFileContentType;
	    private String kmlFileFileName;
	    
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
	
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public long getRevisionNo() {
			return revisionNo;
		}
		public void setRevisionNo(long revisionNo) {
			this.revisionNo = revisionNo;
		}
		public Set<FarmCrops> getFarmCrops() {
			return farmCrops;
		}
		public void setFarmCrops(Set<FarmCrops> farmCrops) {
			this.farmCrops = farmCrops;
		}
		public File getKmlFile() {
			return kmlFile;
		}
		public void setKmlFile(File kmlFile) {
			this.kmlFile = kmlFile;
		}
		public byte[] getKmlFileData() {
			return kmlFileData;
		}
		public void setKmlFileData(byte[] kmlFileData) {
			this.kmlFileData = kmlFileData;
		}
		public String getKmlFileContentType() {
			return kmlFileContentType;
		}
		public void setKmlFileContentType(String kmlFileContentType) {
			this.kmlFileContentType = kmlFileContentType;
		}
		public String getKmlFileFileName() {
			return kmlFileFileName;
		}
		public void setKmlFileFileName(String kmlFileFileName) {
			this.kmlFileFileName = kmlFileFileName;
		}
    


}
