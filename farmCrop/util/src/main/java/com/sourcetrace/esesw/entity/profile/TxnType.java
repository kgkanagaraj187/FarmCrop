/*
 * TxnType.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
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

import com.sourcetrace.eses.order.entity.profile.Category;

/**
 * The Class TxnType.
 * @author $Author: moorthy $
 * @version $Rev: 1233 $ $Date: 2010-05-25 12:25:42 +0530 (Tue, 25 May 2010) $
 */
public class TxnType {

    public static final int CREDIT_NONE = 0;
    public static final int CREDIT_INCREASE = 1;
    public static final int CREDIT_DECREASE = 2;
    public static final int MAX_LENGTH_CODE=25;
    private Category category;
    private String code;
    private long id;
    private String name;
    private int credit;

    /**
     * Gets the credit.
     * @return the credit
     */
    public int getCredit() {

        return credit;
    }

    /**
     * Sets the credit.
     * @param credit the new credit
     */
    public void setCredit(int credit) {

        this.credit = credit;
    }

    /**
     * Gets the category.
     * @return the category
     */
    @NotNull(message = "empty.category")
    public Category getCategory() {

        return category;
    }

    /**
     * Gets the code.
     * @return the code
     */
    @Length(max =MAX_LENGTH_CODE, message = "length.code")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")  
    public String getCode() {

        return code;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the name.
     * @return the name
     */
    
    public String getName() {

        return name;
    }

    /**
     * Sets the category.
     * @param category the new category
     */
    public void setCategory(Category category) {

        this.category = category;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * To string.
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String str = (name == null || name.equals("")) ? code : name;
        return str;
    }
}
