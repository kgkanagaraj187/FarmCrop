/*
 * License.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.util.entity;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sourcetrace.eses.util.DateUtil;

/**
 * The Class License.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class License {

    private Long id;
    private String key;
    private Date start;
    private Date end;
    private String owner;
    private String client;
    private String version;
    private LicenseType type;

    /**
     * Gets the id.
     * @return the id
     */
    public Long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * Gets the key.
     * @return the key
     */
    public String getKey() {

        return key;
    }

    /**
     * Sets the key.
     * @param key the new key
     */
    public void setKey(String key) {

        this.key = key;
    }

    /**
     * Gets the start.
     * @return the start
     */
    public Date getStart() {

        return start;
    }

    /**
     * Sets the start.
     * @param start the new start
     */
    public void setStart(Date start) {

        this.start = start;
    }

    /**
     * Gets the end.
     * @return the end
     */
    public Date getEnd() {

        return end;
    }

    /**
     * Sets the end.
     * @param end the new end
     */
    public void setEnd(Date end) {

        this.end = end;
    }

    /**
     * Gets the owner.
     * @return the owner
     */
    public String getOwner() {

        return owner;
    }

    /**
     * Sets the owner.
     * @param owner the new owner
     */
    public void setOwner(String owner) {

        this.owner = owner;
    }

    /**
     * Sets the client.
     * @param client the new client
     */
    public void setClient(String client) {

        this.client = client;
    }

    /**
     * Gets the client.
     * @return the client
     */
    public String getClient() {

        return client;
    }

    /**
     * Gets the version.
     * @return the version
     */
    public String getVersion() {

        return version;
    }

    /**
     * Sets the version.
     * @param version the new version
     */
    public void setVersion(String version) {

        this.version = version;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public LicenseType getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(LicenseType type) {

        this.type = type;
    }

    /**
     * Gets the text.
     * @return the text
     */
    public String getText() {

        StringBuilder sb = new StringBuilder();
        sb.append(getType()).append(":");
        sb.append(getVersion()).append(":");
        sb.append(getClient()).append(":");
        sb.append(DateUtil.convertDateToString(start, "MMddyyyy")).append(":");
        sb.append(DateUtil.convertDateToString(end, "MMddyyyy")).append(":");
        sb.append(getOwner());

        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {

        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        License rhs = (License) other;
        return new EqualsBuilder().append(id, rhs.id).append(key, rhs.key).append(start, rhs.start)
                .append(end, rhs.end).append(owner, rhs.owner).append(client, rhs.client)
                .append(version, rhs.version).append(type, rhs.type).isEquals();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return new HashCodeBuilder(43, 11).append(id).append(key).append(start).append(end)
                .append(owner).append(client).append(version).append(type).toHashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Key: ").append(getKey()).append(", ");
        sb.append("Start: ").append(getStart()).append(", ");
        sb.append("End: ").append(getEnd()).append(", ");
        sb.append("Owner: ").append(getOwner()).append(", ");
        sb.append("Client: ").append(getClient()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Type: ").append(getType());
        return sb.toString();
    }
}
