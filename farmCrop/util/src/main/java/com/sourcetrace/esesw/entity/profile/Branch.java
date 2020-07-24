/*
 * Branch.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


// TODO: Auto-generated Javadoc
/**
 * The Class Branch.
 *
 * @author $Author: antronivan $
 * @version $Rev: 489 $, $Date: 2009-12-09 21:05:43 +0530 (Wed, 09 Dec 2009) $
 */
public class Branch {

    /** The id. */
    private long id;

    /** The branch id. */
    private String branchId;

    /** The name. */
    private String name;

   

  

    /**
     * Gets the branch id.
     *
     * @return the branch id
     */
    @Pattern(regexp="[^\\p{Punct}]+$", message="pattern.branchId" )
    @Length(max=20, message="length.branchId")
    @NotEmpty(message="empty.branchId")
    public String getBranchId() {
        return branchId;
    }

    /**
     * Sets the branch id.
     *
     * @param branchId the new branch id
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    

   

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Pattern(regexp="[^\\p{Punct}]+$", message="pattern.name")
    @Length(max=30, message="length.name")
    @NotEmpty(message="empty.name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
    public String toString(){return branchId;}


}
