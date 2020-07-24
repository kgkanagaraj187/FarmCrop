/*
 * 
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

// TODO: Auto-generated Javadoc
public class Vendor {

    private long id;
    private String vendorName;
    private String vendorId;
    private String vendorAddress;
    private String landLine;
    private String personName;
    private String mobileNumber;
    private String email;
    private long revisionNo;
    private String branchId;
    
    
    // transient variable
	private List<String> branchesList;

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
     * Gets the vendor name.
     * @return the vendor name
     */
    /*@Length(max = 90, message = "length.vendorName")
    @Pattern(regexp = "[^\\p{Punct}]+$",flags={Flag.CASE_INSENSITIVE})
    @NotEmpty(message = "empty.vendorName")*/
    public String getVendorName() {

        return vendorName;
    }

    /**
     * Sets the vendor name.
     * @param vendorName the new vendor name
     */
    public void setVendorName(String vendorName) {

        this.vendorName = vendorName;
    }

    /**
     * Gets the vendor id.
     * @return the vendor id
     */
    public String getVendorId() {

        return vendorId;
    }

    /**
     * Sets the vendor id.
     * @param vendorId the new vendor id
     */
    public void setVendorId(String vendorId) {

        this.vendorId = vendorId;
    }

    /**
     * Gets the vendor address.
     * @return the vendor address
     */
   /* @Length(max = 255, message = "length.vendorAddress")
    @Pattern(regexp = "[^@#!$%^&*_]*$", message = "pattern.vendorAddress")*/
    public String getVendorAddress() {

        return vendorAddress;
    }

    /**
     * Sets the vendor address.
     * @param vendorAddress the new vendor address
     */
    public void setVendorAddress(String vendorAddress) {

        this.vendorAddress = vendorAddress;
    }

    /**
     * Gets the land line.
     * @return the land line
     */
/*
    @Length(max = 15, message = "length.landLine")
    @Pattern(regexp = "[0-9]*", message = "pattern.landLine")*/
    public String getLandLine() {

        return landLine;
    }

    /**
     * Sets the land line.
     * @param landLine the new land line
     */
    public void setLandLine(String landLine) {

        this.landLine = landLine;
    }

    /**
     * Gets the person name.
     * @return the person name
     */
   /* @Length(max = 90, message = "length.personName")*/
    public String getPersonName() {

        return personName;
    }

    /**
     * Sets the person name.
     * @param personName the new person name
     */
    public void setPersonName(String personName) {

        this.personName = personName;
    }

    /**
     * Gets the mobile number.
     * @return the mobile number
     */
    
    
	@Length(max = 10, message = "length.mobileNumber")
	@Pattern(regexp = "[0-9]*", message = "pattern.mobileNumber")
    public String getMobileNumber() {

        return mobileNumber;
    }

    /**
     * Sets the mobile number.
     * @param mobileNumber the new mobile number
     */
    public void setMobileNumber(String mobileNumber) {

        this.mobileNumber = mobileNumber;
    }

    /**
     * Gets the email.
     * @return the email
     */
    /*@Length(max = 90, message = "length.email")
	@Email(message = "pattern.email")*/

    public String getEmail() {

        return email;
    }

    /**
     * Sets the email.
     * @param email the new email
     */
    public void setEmail(String email) {

        this.email = email;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
   
    
}
