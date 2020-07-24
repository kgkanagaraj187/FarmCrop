/*
 * SwitchTxnResponseProperty.java
 * Copyright (c) 2010-2011, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

/**
 * The Class SwitchTxnResponseProperty.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class SwitchTxnResponseProperty implements Comparable<SwitchTxnResponseProperty> {

    private long id;
    private ResponseParameter responseParameter;
    private String name;
    private int order;
    private String printspace;
    private boolean print;
    private TxnOutputType output;
    private String reference;

    // transient variable
    private String resParameterId;

    /**
     * Gets the printspace.
     * @return the printspace
     */
    public String getPrintspace() {

        return printspace;
    }

    /**
     * Sets the printspace.
     * @param printspace the new printspace
     */
    public void setPrintspace(String printspace) {

        this.printspace = printspace;
    }

    /**
     * Gets the output.
     * @return the output
     */
    public TxnOutputType getOutput() {

        return output;
    }

    /**
     * Sets the output.
     * @param output the new output
     */
    public void setOutput(TxnOutputType output) {

        this.output = output;
    }

    /**
     * Gets the res parameter id.
     * @return the res parameter id
     */
    public String getResParameterId() {

        return resParameterId;
    }

    /**
     * Sets the res parameter id.
     * @param resParameterId the new res parameter id
     */
    public void setResParameterId(String resParameterId) {

        this.resParameterId = resParameterId;
    }

   

    public String getReference() {
    
        return reference;
    }

    public void setReference(String reference) {
    
        this.reference = reference;
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
     * Gets the label.
     * @return the label
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the label.
     * @param name the name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the order.
     * @return the order
     */
    public int getOrder() {

        return order;
    }

    /**
     * Sets the order.
     * @param order the new order
     */
    public void setOrder(int order) {

        this.order = order;
    }

    /**
     * Gets the response parameter.
     * @return the response parameter
     */
    public ResponseParameter getResponseParameter() {

        return responseParameter;
    }

    /**
     * Sets the response parameter.
     * @param responseParameter the new response parameter
     */
    public void setResponseParameter(ResponseParameter responseParameter) {

        this.responseParameter = responseParameter;
    }

    /**
     * Checks if is prints the.
     * @return true, if is prints the
     */
    public boolean isPrint() {

        return print;
    }

    /**
     * Sets the prints the.
     * @param print the new prints the
     */
    public void setPrint(boolean print) {

        this.print = print;
    }

    /**
     * Compare to.
     * @param prop the prop
     * @return the int
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SwitchTxnResponseProperty prop) {

        int diff = 0;

        if (order != prop.getOrder()) {

            if (order > prop.getOrder()) {
                diff = 1;
            } else {
                diff = -1;
            }
        }

        return diff;
    }
}
