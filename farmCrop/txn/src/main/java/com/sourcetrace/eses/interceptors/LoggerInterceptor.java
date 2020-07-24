/*
 * LoggerInterceptor.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;

import com.sourcetrace.eses.util.log.TransactionLog;

public class LoggerInterceptor extends LoggingInInterceptor {

	private static final Logger LOG = LogUtils.getLogger(LoggerInterceptor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.cxf.interceptor.LoggingInInterceptor#handleMessage(org.apache.
	 * cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) throws Fault {

		LOG.info("---- LOGGER Limit : " + limit + " ---- ");

		if (message.containsKey(LoggingMessage.ID_KEY)) {
			return;
		}
		String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
		if (id == null) {
			id = LoggingMessage.nextId();
			message.getExchange().put(LoggingMessage.ID_KEY, id);
		}
		message.put(LoggingMessage.ID_KEY, id);
		final LoggingMessage buffer = new LoggingMessage("Inbound Message\n----------------------------", id);

		Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
		if (responseCode != null) {
			buffer.getResponseCode().append(responseCode);
		}

		String encoding = (String) message.get(Message.ENCODING);

		if (encoding != null) {
			buffer.getEncoding().append(encoding);
		}
		String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
		if (httpMethod != null) {
			buffer.getHttpMethod().append(httpMethod);
		}
		String ct = (String) message.get(Message.CONTENT_TYPE);
		if (ct != null) {
			buffer.getContentType().append(ct);
		}
		Object headers = message.get(Message.PROTOCOL_HEADERS);

		if (headers != null) {
			buffer.getHeader().append(headers);
		}
		String uri = (String) message.get(Message.REQUEST_URI);
		if (uri != null) {
			buffer.getAddress().append(uri);
		}

		InputStream is = message.getContent(InputStream.class);
		if (is != null) {
			CachedOutputStream bos = new CachedOutputStream();
			try {
				IOUtils.copy(is, bos);

				bos.flush();
				is.close();

				message.setContent(InputStream.class, bos.getInputStream());
				if (bos.getTempFile() != null) {
					// large thing on disk...
					buffer.getMessage().append("\nMessage (saved to tmp file):\n");
					buffer.getMessage().append("Filename: " + bos.getTempFile().getAbsolutePath() + "\n");
				}
				if (bos.size() > limit) {
					buffer.getMessage().append("(message truncated to " + limit + " bytes)\n");
				}
				writePayload(buffer.getPayload(), bos, encoding, ct);

				// Logging Transaction request string to object
				TransactionLog txnLog = new TransactionLog();
				txnLog.setRequestLogStringBuffer(new StringBuffer(buffer.getPayload().toString()));
				message.setContent(TransactionLog.class, txnLog);

				bos.close();
			} catch (Exception e) {
				throw new Fault(e);
			}
		}
		log(LOG, buffer.toString());
	}

	@Override
	protected Logger getLogger() {

		return LOG;
	}

}
