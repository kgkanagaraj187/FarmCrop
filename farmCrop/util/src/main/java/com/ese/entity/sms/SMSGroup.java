package com.ese.entity.sms;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.eses.entity.Profile;

// TODO: Auto-generated Javadoc
public class SMSGroup extends Profile{

    public enum ProfileType {
        OTHERS, USER, FIELD_STAFF, FARMER
    }

    private long id;
    private String name;
    private char isActive;
    private Date createDT;
    private String createUser;
    private Date lastUpdateDT;
    private String lastUpdateUser;
    private Set<SmsGroupDetail> smsGroupDetails;

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
     * Gets the is active.
     * @return the is active
     */
    public char getIsActive() {

        return isActive;
    }

    /**
     * Sets the is active.
     * @param isActive the new is active
     */
    public void setIsActive(char isActive) {

        this.isActive = isActive;
    }

    /**
     * Gets the create dt.
     * @return the create dt
     */
    public Date getCreateDT() {

        return createDT;
    }

    /**
     * Sets the create dt.
     * @param createDT the new create dt
     */
    public void setCreateDT(Date createDT) {

        this.createDT = createDT;
    }

    /**
     * Gets the create user.
     * @return the create user
     */
    public String getCreateUser() {

        return createUser;
    }

    /**
     * Sets the create user.
     * @param createUser the new create user
     */
    public void setCreateUser(String createUser) {

        this.createUser = createUser;
    }

    /**
     * Gets the last update dt.
     * @return the last update dt
     */
    public Date getLastUpdateDT() {

        return lastUpdateDT;
    }

    /**
     * Sets the last update dt.
     * @param lastUpdateDT the new last update dt
     */
    public void setLastUpdateDT(Date lastUpdateDT) {

        this.lastUpdateDT = lastUpdateDT;
    }

    /**
     * Gets the last update user.
     * @return the last update user
     */
    public String getLastUpdateUser() {

        return lastUpdateUser;
    }

    /**
     * Sets the last update user.
     * @param lastUpdateUser the new last update user
     */
    public void setLastUpdateUser(String lastUpdateUser) {

        this.lastUpdateUser = lastUpdateUser;
    }

    /**
     * Gets the sms group details.
     * @return the sms group details
     */
    public Set<SmsGroupDetail> getSmsGroupDetails() {

        return smsGroupDetails;
    }

    /**
     * Sets the sms group details.
     * @param smsGroupDetails the new sms group details
     */
    public void setSmsGroupDetails(Set<SmsGroupDetail> smsGroupDetails) {

        this.smsGroupDetails = smsGroupDetails;
    }

}
