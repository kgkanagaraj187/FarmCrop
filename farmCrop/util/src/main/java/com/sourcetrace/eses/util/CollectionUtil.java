/*
 * CollectionUtil.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;

public class CollectionUtil {

	/**
	 * List to map.
	 * 
	 * @param body
	 *            the body
	 * @return the hash map
	 */
	public static HashMap<String, Object> listToMap(Body body) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (!ObjectUtil.isEmpty(body)) {
			List<Data> list = body.getData();

			for (Data data : list) {
				if (data.getValue() != null && !data.getValue().equals("")) {
					map.put(data.getKey(), data.getValue());
				} else if (data.getIntValue() != null && !data.getIntValue().equals("")) {
					map.put(data.getKey(), data.getIntValue());
				} else if (data.getDoubleValue() != null && !data.getDoubleValue().equals("")) {
					map.put(data.getKey(), data.getDoubleValue());
				} else if (data.getDateTimeValue() != null && !data.getDateTimeValue().equals("")) {
					map.put(data.getKey(), data.getDateTimeValue());
				} else if (data.getBinaryValue() != null) {
					map.put(data.getKey(), data.getBinaryValue());
				} else if (data.getCollectionValue() != null && !data.getCollectionValue().equals("")) {
					map.put(data.getKey(), data.getCollectionValue());
				}

			}
		}
		return map;
	}

	/**
	 * Map to list.
	 * 
	 * @param map
	 *            the map
	 * @return the list
	 */
	public static List<Data> mapToList(Map<String, String> map) {

		List<Data> list = new ArrayList<Data>();

		if (!ObjectUtil.isEmpty(map)) {
			Iterator<?> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Data data = new Data();
				if (!StringUtil.isEmpty(entry.getKey())) {
					data.setKey(String.valueOf(entry.getKey()));
				} else {
					data.setKey("");
				}
				if (!StringUtil.isEmpty(entry.getValue())) {
					if (entry.getValue() instanceof Collection) {
						Collection collection = (Collection) entry.getValue();
						data.setCollectionValue(collection);
					} else if (entry.getValue() instanceof DataHandler) {
						data.setBinaryValue((DataHandler) entry.getValue());
					} else {
						data.setValue(String.valueOf(entry.getValue()));
					}
				} else {
					data.setValue("");
				}

				list.add(data);
			}
		}
		return list;

	}

	public static boolean isCollectionEmpty(Collection collection) {

		return !(collection != null && !ObjectUtil.isListEmpty(collection.getObject()));
	}

}
