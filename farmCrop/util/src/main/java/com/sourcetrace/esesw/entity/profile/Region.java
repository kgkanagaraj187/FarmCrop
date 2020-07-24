/*
 * Region.java
 * Copyright (c) 2008, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * The Class Region.
 * @author $Author: antronivan $
 * @version $Rev: 422 $, $Date: 2009-11-23 08:19:13 +0530 (Mon, 23 Nov 2009) $
 */
public class Region {

    private long id;
    private String name;
    private Set<LocationDetail> locations;
    
    
    /**
     * Gets the locations.
     * @return the locations
     */
    public Set<LocationDetail> getLocations() {
        return locations;
    }

    /**
     * Sets the locations.
     * @param locations the new locations
     */
    public void setLocations(Set<LocationDetail> locations) {
        this.locations = locations;
    }

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
    @Length(max = 30, message = "length.region")
    @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.region")
    @NotEmpty(message = "empty.region")
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
}
