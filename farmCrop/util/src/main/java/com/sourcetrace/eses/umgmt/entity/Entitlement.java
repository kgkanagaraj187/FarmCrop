/*
 * Entitlement.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.umgmt.entity;


import org.springframework.security.core.GrantedAuthority;

import com.sourcetrace.eses.util.log.Auditable;

public class Entitlement implements GrantedAuthority, Auditable {

    private static final long serialVersionUID = 5173949203067260070L;

    public static final String DASHBOARD_ENTITLEMENT = "dashboard.dashboard.list";

    private long id;
    private String authority;

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

    /*
     * (non-Javadoc)
     * @see org.springframework.security.GrantedAuthority#getAuthority()
     */
    public String getAuthority() {

        return authority;
    }

    /**
     * Sets the authority.
     * @param authority the new authority
     */
    public void setAuthority(String authority) {

        this.authority = authority;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object object) {

        Entitlement entitlement = (Entitlement) object;
        return this.authority.compareTo(entitlement.getAuthority());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return authority;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        boolean equals = false;

        if (obj instanceof Entitlement) {
            Entitlement ent = (Entitlement) obj;
            equals = authority.equals(ent.getAuthority());
        }

        return equals;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        return authority.hashCode();
    }
}
