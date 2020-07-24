/*
 * IntegerUtil.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.util;

import java.util.Random;

import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class IntegerUtil.
 * @author $Author: aravind $
 * @version $Rev: 74 $, $Date: 2009-10-08 08:19:33 +0530 (Thu, 08 Oct 2009) $
 */
public class IntegerUtil extends ObjectUtil {

    /**
     * Checks if is zero.
     * @param value the value
     * @return true, if is zero
     */
    public static boolean isZero(int value) {

        return (value == 0);
    }

    /**
     * Checks if is zero.
     * @param value the value
     * @return true, if is zero
     */
    public static boolean isZero(String value) {

        return (StringUtil.isEmpty(value) || Integer.parseInt(value) == 0);
    }

    /**
     * Int value.
     * @param value the value
     * @return the int
     */
    public static int intValue(String value) {

        return isZero(value) ? 0 : Integer.parseInt(value);
    }

    /**
     * Int value.
     * @param value the value
     * @return the int
     */
    public static int intValue(Object value) {

        return Integer.parseInt(StringUtil.getString(value, "0"));
    }

    /**
     * Random gen.
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomGen(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
