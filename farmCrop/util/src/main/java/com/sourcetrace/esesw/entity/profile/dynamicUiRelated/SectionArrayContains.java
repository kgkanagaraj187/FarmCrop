package com.sourcetrace.esesw.entity.profile.dynamicUiRelated;

import java.util.ArrayList;

public class SectionArrayContains {
	private SectionDetails sectionData;

	private  ArrayList<FieldDetails> fieldsArray = new ArrayList<FieldDetails>();
	
	public SectionDetails getSectionData() {
		return sectionData;
	}
	public void setSectionData(SectionDetails sectionData) {
		this.sectionData = sectionData;
	}
	public ArrayList<FieldDetails> getFieldsArray() {
		return fieldsArray;
	}
	public void setFieldsArray(ArrayList<FieldDetails> fieldsArray) {
		this.fieldsArray = fieldsArray;
	}
}
