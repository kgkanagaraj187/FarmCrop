/*
 * TxnFault.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.exception;

import java.io.IOException;
import java.util.Properties;

import javax.xml.ws.WebFault;

import org.apache.log4j.Logger;

@WebFault(name = "Response", targetNamespace = "http://service.eses.sourcetrace.com/")
public class TxnFault extends RuntimeException {

    private static final long serialVersionUID = 8085487621092356949L;
    private static final Logger LOGGER = Logger.getLogger(TxnFault.class.getName());
    private static Properties errors;
    private Throwable cause;
    private String code;
    private String message;
    private long txnLogId;

    private com.sourcetrace.eses.txn.schema.Response errorResponse;
    static {
        errors = new Properties();
        try {
            errors.load(TxnFault.class.getResourceAsStream("transactionError.properties"));
        } catch (IOException e) {
            LOGGER.error("Error reading error codes and messages, transactionError.properties");
        }
    }

    /**
     * Instantiates a new txn fault.
     * @param message the message
     */
    public TxnFault(String message) {

        setCode(message);
        setMessage(errors.getProperty(message, message));
    }

    /**
     * Instantiates a new txn fault.
     * @param code the code
     * @param message the message
     */
    public TxnFault(String code, String message) {

        setCode(code);
        setMessage(message);
    }

    /**
     * Instantiates a new txn fault.
     * @param message the message
     * @param cause the cause
     */
    public TxnFault(String message, Throwable cause) {

        setCode(message);
        setMessage(errors.getProperty(message, message));
        setCause(cause);
    }

    /**
     * Gets the fault info.
     * @return the fault info
     */
    public com.sourcetrace.eses.txn.schema.Response getFaultInfo() {

        if (errorResponse == null) {
            errorResponse = new com.sourcetrace.eses.txn.schema.Response();
            com.sourcetrace.eses.txn.schema.Status status = new com.sourcetrace.eses.txn.schema.Status();
            status.setCode(code);
            status.setMessage(message);
            errorResponse.setStatus(status);
            errorResponse.setTxnLogId(txnLogId);
        }

        return this.errorResponse;
    }

    /**
     * Gets the errors.
     * @return the errors
     */
    public static Properties getErrors() {

        return errors;
    }

    /**
     * Sets the errors.
     * @param errors the new errors
     */
    public static void setErrors(Properties errors) {

        TxnFault.errors = errors;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the message.
     * @return the message
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {

        return message;
    }

    /**
     * Sets the message.
     * @param message the new message
     */
    public void setMessage(String message) {

        this.message = message;
    }

    /**
     * Gets the error response.
     * @return the error response
     */
    public com.sourcetrace.eses.txn.schema.Response getErrorResponse() {

        return errorResponse;
    }

    /**
     * Sets the error response.
     * @param errorResponse the new error response
     */
    public void setErrorResponse(com.sourcetrace.eses.txn.schema.Response errorResponse) {

        this.errorResponse = errorResponse;
    }

    /**
     * Gets the cause.
     * @return the cause
     * @see java.lang.Throwable#getCause()
     */
    public Throwable getCause() {

        return cause;
    }

    /**
     * Sets the cause.
     * @param cause the new cause
     */
    public void setCause(Throwable cause) {

        this.cause = cause;
    }

    /**
     * Gets the txn log id.
     * @return the txn log id
     */
    public long getTxnLogId() {

        return txnLogId;
    }

    /**
     * Sets the txn log id.
     * @param txnLogId the new txn log id
     */
    public void setTxnLogId(long txnLogId) {

        this.txnLogId = txnLogId;
    }

}
