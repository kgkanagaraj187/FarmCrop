/*
 * Checksum.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.txn.engine.incpt;

import org.apache.log4j.Logger;

import com.sourcetrace.eses.txn.exception.ESEException;

/**
 * The Class Checksum.
 * @author $Author: aravind $
 * @version $Rev: 72 $, $Date: 2009-07-27 22:53:56 +0530 (Mon, 27 Jul 2009) $
 */
public class Checksum {

    private static final Logger LOGGER = Logger.getLogger(Checksum.class);
    private static final int CHECKSUM_CHARS_COUNT = 3;
    private static final int FIRST_CHECKSUM_FROM_LAST = 2;
    private static final int SECOND_CHECKSUM_FROM_LAST = 1;
    private static final byte[] CHECKSUM = {'u', 'J', 'b', 'X', '1', '^', 'T', 's', 'R', '6', 'h',
            'H', 'x', 'o', 'A', '4', 'q', 'E', 'a', '_', 'G', 'e', 'Z', 'p', 'V', 'L', '[', 'N',
            'I', 'P', 't', 'Y', '~', '3', 'r', 'n', 'W', '-', '5', 'c', '[', 'j', '2', 'F', 'K',
            '*', 'O', 'd', '8', 'f', 'g', 'Q', 'i', 'M', '|', 'l', 'm', 'D', '0', ')', 'C', '{',
            'w', '7', '(', 'v', 'B', '9', 'y', 'k', 'U', 'z', '}', 'S'};

    /**
     * Check.
     * @param data the data
     */
    public void check(byte[] data) {

        byte[] cheksumData = new byte[data.length - CHECKSUM_CHARS_COUNT];

        System.arraycopy(data, 0, cheksumData, 0, data.length - CHECKSUM_CHARS_COUNT);

        byte checkSum = getChecksum(cheksumData);
        byte checkSum2 = (byte) (~checkSum);

        checkSum = getChecksumValue(checkSum);
        checkSum2 = getChecksumValue(checkSum2);

        boolean invalidCheckSum = (checkSum != data[data.length - FIRST_CHECKSUM_FROM_LAST])
                || (checkSum2 != data[data.length - SECOND_CHECKSUM_FROM_LAST]);
        if (invalidCheckSum) {
            throw new ESEException(ESEException.INVALID_DATA);
        }
    }

    /**
     * Adds the.
     * @param data the data
     * @return the string
     */
    public String buildDataStringWithChecksum(byte[] data) {

        byte[] cheksumData = new byte[data.length + CHECKSUM_CHARS_COUNT];

        System.arraycopy(data, 0, cheksumData, 0, data.length);

        byte checkSum = getChecksum(cheksumData);
        byte checkSum2 = (byte) (~checkSum);

        checkSum = getChecksumValue(checkSum);
        checkSum2 = getChecksumValue(checkSum2);

        cheksumData[cheksumData.length - CHECKSUM_CHARS_COUNT] = ",".getBytes()[0];
        cheksumData[cheksumData.length - FIRST_CHECKSUM_FROM_LAST] = checkSum;
        cheksumData[cheksumData.length - SECOND_CHECKSUM_FROM_LAST] = checkSum2;

        return new String(cheksumData);
    }

    /**
     * Gets the checksum.
     * @param data the data
     * @return the checksum
     */
    private byte getChecksum(byte[] data) {

        byte checkSum = 0;

        for (int i = 0; i < (data.length); i++) {
            checkSum += data[i];
        }

        return checkSum;
    }

    /**
     * Gets the checksum value.
     * @param value the value
     * @return the checksum value
     */
    private byte getChecksumValue(int value) {

        value = Math.abs(value);

        if (value >= CHECKSUM.length) {
            return getChecksumValue(value - CHECKSUM.length);
        } else {
            return CHECKSUM[value];
        }
    }
}
