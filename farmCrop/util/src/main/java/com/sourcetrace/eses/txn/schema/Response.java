/*
 * Response.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.txn.schema;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://www.sourcetrace.com/ese/schemas/ese-switch-txn}Status"/>
 *         &lt;element name="body" type="{http://www.sourcetrace.com/ese/schemas/ese-switch-txn}body"/>
 *         &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="binaryType" type="{http://www.sourcetrace.com/ese/schemas/ese-switch-txn}binaryType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "status", "body", "sessionId", "binaryType", "txnLogId" })
@XmlRootElement(name = "Response")
@ApiModel( value = "Response", description = "Person resource representation" )
public class Response implements Serializable {

    @XmlElement(required = true)
    @ApiModelProperty( value = "Response status", dataType = "com.sourcetrace.eses.txn.schema.Status") 
    protected Status status;
    @XmlElement(required = true)
    @ApiModelProperty( value = "Response Body", dataType = "com.sourcetrace.eses.txn.schema.Body" ) 
    protected Body body;
    @XmlElement(required = true)
    @ApiModelProperty( value = "Response sessionId", dataType = "java.lang.String") 
    protected String sessionId;
    @XmlElement(required = true)
    @ApiModelProperty( value = "Response binaryType", dataType = "com.sourcetrace.eses.txn.schema.BinaryType" ) 
    protected BinaryType binaryType;
    @ApiModelProperty( value = "Response txnLogId", dataType = "java.lang.Long") 
    private long txnLogId;

    /**
     * Gets the value of the status property.
     * @return the status possible object is {@link Status }
     */
    public Status getStatus() {

        return status;
    }

    /**
     * Sets the value of the status property.
     * @param value allowed object is {@link Status }
     */
    public void setStatus(Status value) {

        this.status = value;
    }

    /**
     * Gets the value of the body property.
     * @return the body possible object is {@link Body }
     */
    public Body getBody() {

        return body;
    }

    /**
     * Sets the value of the body property.
     * @param value allowed object is {@link Body }
     */
    public void setBody(Body value) {

        this.body = value;
    }

    /**
     * Gets the value of the sessionId property.
     * @return the session id possible object is {@link String }
     */
    public String getSessionId() {

        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * @param value allowed object is {@link String }
     */
    public void setSessionId(String value) {

        this.sessionId = value;
    }

    /**
     * Gets the value of the binaryType property.
     * @return the binary type possible object is {@link BinaryType }
     */
    public BinaryType getBinaryType() {

        return binaryType;
    }

    /**
     * Sets the value of the binaryType property.
     * @param value allowed object is {@link BinaryType }
     */
    public void setBinaryType(BinaryType value) {

        this.binaryType = value;
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
