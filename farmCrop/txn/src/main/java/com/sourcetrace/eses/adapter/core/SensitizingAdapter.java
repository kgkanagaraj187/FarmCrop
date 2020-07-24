package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.Sensitizing;
import com.sourcetrace.eses.order.entity.txn.SensitizingImg;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.StringUtil;

@SuppressWarnings({ "unused", "unchecked" })
@Component
public class SensitizingAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(SensitizingAdapter.class.getName());
	@Autowired
	private IFarmerService farmerService;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();

		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("TXN TYPE: " + TransactionTypeProperties.SENSITIZING_ADAPTER);
		LOGGER.info("TXN NAME: " + "SENSITIZING");

		String groupId = (String) reqData.get(TransactionProperties.GROUP_ID);
		String vcode= (String) reqData.get(TransactionProperties.VCODE);
		String farmerCount = (String) reqData.get(TransactionProperties.FARMER_CNT);
		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		String remarks = (String) reqData.get(TransactionProperties.REMARKS);
		String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);

		Sensitizing sensitizing = new Sensitizing();
		sensitizing.setGroupId(groupId);
		sensitizing.setVillage(vcode);
		sensitizing.setFarmerCount(farmerCount);
		sensitizing.setRemarks(remarks);
		sensitizing.setCreatedDt(new Date());
		sensitizing.setLatitude(latitude);
		sensitizing.setLongitude(longitude);
		sensitizing.setRevisionNo(DateUtil.getRevisionNumber());
		sensitizing.setBranchId(head.getBranchId());
		
		sensitizing.setSentizingImages(getSensitizingImages(reqData));

		farmerService.save(sensitizing);

		return null;
	}

	private Set<SensitizingImg> getSensitizingImages(Map reqData) {
		Set<SensitizingImg> sensitizingImageSet = new HashSet<SensitizingImg>();

		Collection photoCollection = (Collection) reqData.get(TxnEnrollmentProperties.PHOTO_LIST);

		if (!CollectionUtil.isCollectionEmpty(photoCollection)) {
			photoCollection.getObject().stream().forEach(object -> {
				List<Data> photoDatas = object.getData();
				SensitizingImg sensitizingImg = new SensitizingImg();

				object.getData().stream().forEach(data -> {

					String key = data.getKey();
					String value = (String) data.getValue();
					byte[] imageContent = null;

					if (TxnEnrollmentProperties.PHOTO.equalsIgnoreCase(key)) {
						DataHandler photo = data.getBinaryValue();
						try {
							if (photo != null && photo.getInputStream().available() > 0) {
								imageContent = IOUtils.toByteArray(photo.getInputStream());
								sensitizingImg.setPhoto(imageContent);
							}
						} catch (Exception e) {
							e.printStackTrace();
							throw new SwitchException(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
						}
					}

					if (TxnEnrollmentProperties.PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && !value.equals("0")) {
							try {
								Date photoCaptureDate = DateUtil.convertStringToDate(value, DateUtil.TXN_TIME_FORMAT);
								sensitizingImg.setCaptureTime(photoCaptureDate);
							} catch (Exception e) {
								e.printStackTrace();
								throw new SwitchException(ITxnErrorCodes.DATE_CONVERSION_ERROR);
							}
						}
					}

					if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key))
						if (!StringUtil.isEmpty(sensitizingImg.getLatitude())) {
							sensitizingImg.setLatitude(value);
						} else {
							sensitizingImg.setLatitude("0");
						}
					if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key))
						if (!StringUtil.isEmpty(sensitizingImg.getLongitude())) {
							sensitizingImg.setLongitude(value);
						} else {
							sensitizingImg.setLongitude("0");
						}

					sensitizingImageSet.add(sensitizingImg);

				});
			});

		}
		return sensitizingImageSet;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}
}
