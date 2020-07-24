package com.sourcetrace.eses.util.entity;

import java.util.List;
import java.util.Map;

public class SurveyJSONDataObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -5060065678100882463L;
	private String id;
	private List<List<String>> aaData;
	private String success;
	private String message;
	private Map<String, Object> mapObj;
	private Map<String, Object> checkBoxlist;
	private List<String> list;
	private List<Object> objects;
	private  Map<String, String> errorCodesSubForm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<List<String>> getAaData() {
		return aaData;
	}

	public void setAaData(List<List<String>> aaData) {
		this.aaData = aaData;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getMapObj() {
		return mapObj;
	}

	public void setMapObj(Map<String, Object> mapObj) {
		this.mapObj = mapObj;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

    public Map<String, String> getErrorCodesSubForm() {
    
        return errorCodesSubForm;
    }

    public void setErrorCodesSubForm(Map<String, String> errorCodesSubForm) {
    
        this.errorCodesSubForm = errorCodesSubForm;
    }

	public Map<String, Object> getCheckBoxlist() {
		return checkBoxlist;
	}

	public void setCheckBoxlist(Map<String, Object> checkBoxlist) {
		this.checkBoxlist = checkBoxlist;
	}

	
}
