/*
 * IESEAction.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view;

/**
 * IESEAction is an interface for ese actions.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public interface IESEAction {

    public static final String CREATE = "create";
    public static final String DETAIL = "detail";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String LIST = "list";
    public static final String TITLE_PREFIX = "title.";
    public static final String HEADING = "heading";
    public static final String EMPTY = "";
    
    public static final String DATE = "date";
    public static final String DATE_TIME = "dateTime";

    public static final String ROWS = "rows";
    public static final String RECORDS = "records";

    public static final String BY_REGION = "byProdReg";
    public static final String BY_BUSINESSPOINT = "byProdBPoint";
    public static final String BY_DAILY = "byProdDaily";
    public static final String BY_MONTH = "byProdMonth";
    public static final String CHECK_MONTH = "0";
    public static final String CHECK_TRIMESTER = "1";
    public static final String CHECK_SEMESTER = "2";
    public static final String BY_TRIMESTER = "byProdTrimester";
    public static final String BY_SEMESTER = "byProdSemester";
    public static final String BY_LOCATION = "byProdLocation";
    public static final String BY_BRANCH = "byProdBranch";
    public static final String REDIRECT = "redirect";
}
