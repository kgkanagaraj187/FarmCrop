/**
 * AuditLog.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.eses.util.entity;


import java.util.Date;
import java.util.Map;


/**
 * The Class AuditLog.
 * @author $Author: antronivan $
 * @version $Rev: 422 $, $Date: 2009-11-23 08:19:13 +0530 (Mon, 23 Nov 2009) $
 */
public class AuditLog {

	public static final String ESE = "ESE";

    // severity
    public static final String FAILURE = "failure";
    public static final String WARN = "warning";
    public static final String PENDING = "pending";
    public static final String SUCCESS = "success";

    // category
    public static final String AGENT = "agent";
    public static final String CLIENT = "client";
    public static final String USER = "user";
    public static final String POS = "POS";
    public static final String KIOSK = "kiosk";
    public static final String ENROLL_STATION = "enrollmentStation";
    public static final String TELLER_POS = "tellerPOS";
    public static final String CARD = "card";
    public static final String MCC = "MCC";
    public static final String ONLINE_TXN = "onlineTransaction";
    public static final String ONLINE_REVERSAL_TXN = "onlineReversalTransaction";
    public static final String OFFLINE_TXN = "offlineTransaction";
    public static final String OFFLINE_REVERSAL_TXN = "offlineReversalTransaction";
    public static final String BATCH_JOB ="scheduledTask";

    // action
    public static final String COMM_PYMNT = "commissionPayment";
    public static final String ADD = "add";
    public static final String EDIT = "edit";
    public static final String DELETE = "delete";
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    public static final String SYNCH = "synch";
    public static final String UPDATE = "update";
    public static final String LOCKED = "locked";
    public static final String UNLOCKED = "unlocked";
    public static final String INVALID_LOGIN = "failedLogin";
    public static final String PROCESS_ONLINE = "processTransaction";
    public static final String PROCESS_OFFLINE = "processOfflineTransactions";
    public static final String PROCESS_COMM_FEE = "processCommissionPayments";

    public static final String ACCT_SYNCH = "accountSynch";
    public static final String CLIENT_SYNCH = "clientSynch";
    public static final String BALANCE_LOOKUP = "balanceLookup";
    public static final String REVERSE_BALANCE_LOOKUP = "reverseBalanceLookup";
    public static final String CREDIT_PYMNT = "creditPayment";
    public static final String LOAN_PYMNT = "loanPayment";
    public static final String DEPOSIT = "deposit";
    public static final String REVERSE_DEPOSIT = "reverseDeposit";
    public static final String WITHDRAWAL = "withdrawal";
    public static final String REVERSE_WITHDRAWAL = "reverseWithdrawal";
    public static final String SHOPPING = "shopping";
    public static final String REVERSE_SHOPPING = "reverseShopping";
    public static final String CREDIT_SHOPPING = "creditShopping";
    public static final String REVERSE_CREDIT_SHOPPING = "reverseCreditShopping";
    public static final String CREDIT_WITHDRAWAL = "creditWithdrawal";
    public static final String REVERSE_CREDIT_WITHDRAWAL = "reverseCreditWithdrawal";
    public static final String PING = "ping";
    public static final String BULK_COMM_PYMNT = "commissionPayment";
    public static final String BULK_OFFLINE = "offline";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";

    public static final String ALL = "all";
    private long id;
    private Date date;
    private String user;
    private String severity;
    private String category;
    private String action;
    private String event;
    private String reference;
    private Map<String, String> properties;


    public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

    /**
	 * Gets the date.
	 * @return the date
	 */
    public Date getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 * @param date the new date
	 */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
	 * Gets the user.
	 * @return the user
	 */
    public String getUser() {
        return user;
    }

    /**
	 * Sets the user.
	 * @param user the new user
	 */
    public void setUser(String user) {
        this.user = user;
    }

    /**
	 * Gets the severity.
	 * @return the severity
	 */
    public String getSeverity() {
        return severity;
    }

    /**
	 * Sets the severity.
	 * @param severity the new severity
	 */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
	 * Gets the category.
	 * @return the category
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * Sets the category.
	 * @param category the new category
	 */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
	 * Gets the action.
	 * @return the action
	 */
    public String getAction() {
        return action;
    }

    /**
	 * Sets the action.
	 * @param action the new action
	 */
    public void setAction(String action) {
        this.action = action;
    }

    /**
	 * Gets the event.
	 * @return the event
	 */
    public String getEvent() {
        return event;
    }

    /**
	 * Sets the event.
	 * @param event the new event
	 */
    public void setEvent(String event) {
        this.event = event;
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

	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(date + ", ");
		buff.append(user + ", ");
		buff.append(severity + ", ");
		buff.append(category + ", ");
		buff.append(action + ", ");
		buff.append(event + ", ");
		buff.append(properties);

		return buff.toString();
	}
}
