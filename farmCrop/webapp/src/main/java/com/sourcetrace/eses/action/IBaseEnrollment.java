/*
 * IBaseEnrollment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.action;

public interface IBaseEnrollment {

    /**
     * Creates the.
     * @return the string
     */
    public String create();

    /**
     * Save.
     * @return the string
     */
    public String save();

    /**
     * Update.
     * @return the string
     */

    public String update();

    /**
     * Edits the.
     * @return the string
     */
    public String edit();

    /**
     * Detail.
     * @return the string
     */
    public String detail();

    /**
     * Delete.
     * @return the string
     */
    public String delete();

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     */
    public void data() throws Exception;
}
