/*
 * Currency.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The Class CurrencyType.
 * @author $Author: ganesh $
 * @version $Rev: 991 $ $Date: 2010-03-09 13:07:01 +0530 (Tue, 09 Mar 2010) $
 */
public class Currency {
   
    public static final int MAX_LENGTH_CODE=7;
    public static final int MAX_LENGTH_SYMBOL=10;
    private String code;
    private long id;
    private String name;
    private String symbol;
   

   
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
     * Gets the symbol.
     * @return the symbol
     */
    @Length(max =MAX_LENGTH_SYMBOL, message = "length.symbol")
    @NotEmpty(message = "empty.symbol")
    public String getSymbol() {

        return symbol;
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
     * Sets the symbol.
     * @param symbol the new symbol
     */
    public void setSymbol(String symbol) {

        this.symbol = symbol;
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

	public String getCurrencyCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
