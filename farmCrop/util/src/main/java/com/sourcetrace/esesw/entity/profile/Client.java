/*
 * Client.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.entity.Profile;

/**
 * The Class ClientProfile.
 * 
 * @author $Author: antronivan $
 * @version $Rev: 150 $, $Date: 2009-09-03 10:31:04 +0530 (Thu, 03 Sep 2009) $
 */
public class Client extends Profile {

	private ClientType clientType;

	/**
	 * Gets the client type.
	 * 
	 * @return the client type
	 */
	public ClientType getClientType() {
		return clientType;
	}

	/**
	 * Sets the client type.
	 * 
	 * @param clientType
	 *            the new client type
	 */
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

}
