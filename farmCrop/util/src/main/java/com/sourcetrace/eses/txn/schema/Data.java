//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.07 at 04:18:38 PM IST 
//

package com.sourcetrace.eses.txn.schema;

import java.io.Serializable;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * Java class for data complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="data">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice>
 *           &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="intValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="longValue" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *           &lt;element name="doubleValue" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *           &lt;element name="booleanValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="dateValue" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="timeValue" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *           &lt;element name="dateTimeValue" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *           &lt;element name="objectValue" type="{http://www.sourcetrace.com/ese/schemas/ese-switch-txn}Object"/>
 *           &lt;element name="collectionValue" type="{http://www.sourcetrace.com/ese/schemas/ese-switch-txn}Collection"/>
 *           &lt;element name="binaryValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "data", propOrder = { "key", "value", "intValue", "longValue", "doubleValue", "booleanValue",
		"dateValue", "timeValue", "dateTimeValue", "objectValue", "collectionValue", "binaryValue" })
public class Data implements Serializable {

	@XmlElement(required = true)
	protected String key;
	protected String value;
	protected Integer intValue;
	protected Long longValue;
	protected Double doubleValue;
	protected Boolean booleanValue;
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dateValue;
	@XmlSchemaType(name = "time")
	protected XMLGregorianCalendar timeValue;
	protected XMLGregorianCalendar dateTimeValue;
	@ApiModelProperty(dataType = "com.sourcetrace.eses.txn.schema.Object",name="objectValue")
	protected Object objectValue;
	@ApiModelProperty(dataType = "com.sourcetrace.eses.txn.schema.Collection",name="collectionValue")
	protected Collection collectionValue;
	@XmlMimeType("application/octet-stream")
	protected DataHandler binaryValue;

	/**
	 * Gets the value of the key property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the value of the key property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setKey(String value) {
		this.key = value;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the intValue property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getIntValue() {
		return intValue;
	}

	/**
	 * Sets the value of the intValue property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setIntValue(Integer value) {
		this.intValue = value;
	}

	/**
	 * Gets the value of the longValue property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getLongValue() {
		return longValue;
	}

	/**
	 * Sets the value of the longValue property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setLongValue(Long value) {
		this.longValue = value;
	}

	/**
	 * Gets the value of the doubleValue property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getDoubleValue() {
		return doubleValue;
	}

	/**
	 * Sets the value of the doubleValue property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setDoubleValue(Double value) {
		this.doubleValue = value;
	}

	/**
	 * Gets the value of the booleanValue property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isBooleanValue() {
		return booleanValue;
	}

	/**
	 * Sets the value of the booleanValue property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setBooleanValue(Boolean value) {
		this.booleanValue = value;
	}

	/**
	 * Gets the value of the dateValue property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDateValue() {
		return dateValue;
	}

	/**
	 * Sets the value of the dateValue property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDateValue(XMLGregorianCalendar value) {
		this.dateValue = value;
	}

	/**
	 * Gets the value of the timeValue property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getTimeValue() {
		return timeValue;
	}

	/**
	 * Sets the value of the timeValue property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setTimeValue(XMLGregorianCalendar value) {
		this.timeValue = value;
	}

	/**
	 * Gets the value of the dateTimeValue property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDateTimeValue() {
		return dateTimeValue;
	}

	/**
	 * Sets the value of the dateTimeValue property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDateTimeValue(XMLGregorianCalendar value) {
		this.dateTimeValue = value;
	}

	/**
	 * Gets the value of the objectValue property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public Object getObjectValue() {
		return objectValue;
	}

	/**
	 * Sets the value of the objectValue property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setObjectValue(Object value) {
		this.objectValue = value;
	}

	/**
	 * Gets the value of the collectionValue property.
	 * 
	 * @return possible object is {@link Collection }
	 * 
	 */
	public Collection getCollectionValue() {
		return collectionValue;
	}

	/**
	 * Sets the value of the collectionValue property.
	 * 
	 * @param value
	 *            allowed object is {@link Collection }
	 * 
	 */
	public void setCollectionValue(Collection value) {
		this.collectionValue = value;
	}

	/**
	 * Gets the value of the binaryValue property.
	 * 
	 * @return possible object is {@link DataHandler }
	 * 
	 */
	public DataHandler getBinaryValue() {
		return binaryValue;
	}

	/**
	 * Sets the value of the binaryValue property.
	 * 
	 * @param value
	 *            allowed object is {@link DataHandler }
	 * 
	 */
	public void setBinaryValue(DataHandler value) {
		this.binaryValue = value;
	}

}
