package com.ese.view.profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.FarmerField;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.esesw.view.SwitchAction;

@SuppressWarnings("serial")
public class FarmerFieldAction extends SwitchAction {
	private List<FarmerField> farmerFieldList;
	private static final String NAME="1";
	private static final String ID="2";
	private static final String CLASS="3";
	private static final String PARENT="4";
	@Autowired
	private IFarmerService farmerService;
	private String headers;
	private String fields;
	
	public String list() {
		farmerFieldList = farmerService.listFarmerFields();
		farmerFieldList.stream().forEach(config->{
			headers=config.getEntityFields();
			fields = config.getEntityFields();
		});
		return INPUT;
	}

	public List<FarmerField> getFarmerFieldList() {
		return farmerFieldList;
	}

	public void setFarmerFieldList(List<FarmerField> farmerFieldList) {
		this.farmerFieldList = farmerFieldList;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}
	
	

}
