/*
 * ITxnAdapter.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.adapter;

import java.util.Map;
import java.util.Properties;

public interface ITxnAdapter {

    public static Properties fieldsList = new Properties();

    /**
     * Process.
     * @param reqData the req data
     * @return the map
     */
    public Map<?, ?> process(Map<?, ?> reqData);

    /**
     * Process void.
     * @param reqData the req data
     * @return the map
     */
    public Map<?, ?> processVoid(Map<?, ?> reqData);

}
