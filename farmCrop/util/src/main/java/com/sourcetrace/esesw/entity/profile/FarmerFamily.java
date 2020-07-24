/*
 * FarmerFamily.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
public class FarmerFamily {

    private long id;
    private Farmer farmer;
    private String name;
    private String relation;
    private String gender;
    private int age;
    private String education;
    private String headOfFamily;
    private String activity;
    private boolean wageEarner;
    private Integer profession;
    private String otherProfession;
   
    public static final String DISABLED_YES = "1";
	public static final String DISABLED_NO = "0";
    
    private int disability;
    private String disableDetail;
    private String maritalStatus;
	private String educationStatus;
	
	private String filterStatusDis;
	
	
	

	/**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the name.
     * @return the name
     */
     public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the age.
     * @return the age
     */

    public int getAge() {

        return age;
    }

    /**
     * Sets the age.
     * @param age the new age
     */
    public void setAge(int age) {

        this.age = age;
    }

    /**
     * Checks if is head of family.
     * @return true, if is head of family
     */
    public String isHeadOfFamily() {

        return headOfFamily;
    }

    /**
     * Sets the head of family.
     * @param headOfFamily the new head of family
     */
    public void setHeadOfFamily(String headOfFamily) {

        this.headOfFamily = headOfFamily;
    }

    /**
     * Gets the activity.
     * @return the activity
     */
     public String getActivity() {

        return activity;
    }

    /**
     * Sets the activity.
     * @param activity the new activity
     */
    public void setActivity(String activity) {

        this.activity = activity;
    }

    /**
     * Sets the farmer.
     * @param farmer the new farmer
     */
    public void setFarmer(Farmer farmer) {

        this.farmer = farmer;
    }

    /**
     * Gets the farmer.
     * @return the farmer
     */
    public Farmer getFarmer() {

        return farmer;
    }

    /**
     * Gets the gender.
     * @return the gender
     */
    public String getGender() {

        return gender;
    }

    /**
     * Sets the gender.
     * @param gender the new gender
     */
    public void setGender(String gender) {

        this.gender = gender;
    }

    /**
     * Checks if is wage earner.
     * @return true, if is wage earner
     */
    public boolean isWageEarner() {

        return wageEarner;
    }

    /**
     * Sets the wage earner.
     * @param wageEarner the new wage earner
     */
    public void setWageEarner(boolean wageEarner) {

        this.wageEarner = wageEarner;
    }

    /**
     * Gets the relation.
     * @return the relation
     */
  
    public String getRelation() {

        return relation;
    }

    /**
     * Sets the relation.
     * @param relation the new relation
     */
    public void setRelation(String relation) {

        this.relation = relation;
    }

    /**
     * Gets the education.
     * @return the education
     */
    public String getEducation() {

        return education;
    }

    /**
     * Sets the education.
     * @param education the new education
     */
    public void setEducation(String education) {

        this.education = education;
    }

    /**
     * Gets the profession.
     * @return the profession
     */
    public Integer getProfession() {

        return profession;
    }

    /**
     * Sets the profession.
     * @param profession the new profession
     */
    public void setProfession(Integer profession) {

        this.profession = profession;
    }

    /**
     * Gets the other profession.
     * @return the other profession
     */
    public String getOtherProfession() {

        return otherProfession;
    }

    /**
     * Sets the other profession.
     * @param otherProfession the new other profession
     */
    public void setOtherProfession(String otherProfession) {

        this.otherProfession = otherProfession;
    }
    
    public int getDisability() {
  		return disability;
  	}

  	public void setDisability(int disability) {
  		this.disability = disability;
  	}

  	public String getMaritalStatus() {
  		return maritalStatus;
  	}

  	public void setMaritalStatus(String maritalStatus) {
  		this.maritalStatus = maritalStatus;
  	}

  	public String getEducationStatus() {
  		return educationStatus;
  	}

  	public void setEducationStatus(String educationStatus) {
  		this.educationStatus = educationStatus;
  	}

	public String getFilterStatusDis() {
		return filterStatusDis;
	}

	public void setFilterStatusDis(String filterStatusDis) {
		this.filterStatusDis = filterStatusDis;
	}

	public String getDisableDetail() {
		return disableDetail;
	}

	public void setDisableDetail(String disableDetail) {
		this.disableDetail = disableDetail;
	}

}
