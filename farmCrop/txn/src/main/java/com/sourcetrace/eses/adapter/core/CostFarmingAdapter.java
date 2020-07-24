package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.CostFarming;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
@Component
public class CostFarmingAdapter implements ITxnAdapter
{
	
	private static final String EMPTY = "empty";
    private static final String INVALID = "invalid";
	@Autowired
    private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	 private static final Logger LOGGER = Logger.getLogger(CostFarmingAdapter.class.getName());
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		
		 LOGGER.info("----- Cost of Farming Starts -----");
		 
		 Head head = (Head) reqData.get(TransactionProperties.HEAD);
	        Map<String, String> errorCodes = new HashMap<String, String>();
	        CostFarming   costFarming=new CostFarming();
	        
	        String elitType = (String) reqData.get(TxnEnrollmentProperties.ELITE_TYPE);
	        if(!StringUtil.isEmpty(elitType) && elitType.equalsIgnoreCase(Cow.ELITE_RESEARCH))
	        {
	        	String researchStationId = (String) reqData.get(TxnEnrollmentProperties.RESEARCH_STATION_ID);
	        	validateEmptyString(researchStationId, ITxnErrorCodes.EMPTY_RESEARCH_STATION_ID);
	        	 costFarming.setResearchStationId(researchStationId);       
	        }
	        else
	        {
	            String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
	  	        validateEmptyString(farmerId, ITxnErrorCodes.EMPTY_FARMER_ID);
	  	        costFarming.setFarmerId(farmerId);

	  	        String farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
	  	        validateEmptyString(farmCode, SwitchErrorCodes.EMPTY_FARM_CODE);
	  	        costFarming.setFarmCode(farmCode);
	        }
	        validateEmptyString(elitType, ITxnErrorCodes.ELITE_TYPE);
	        costFarming.setElitType(elitType);
	        String cowId = (String) reqData.get(TxnEnrollmentProperties.COW_ID);
	        validateEmptyString(cowId, ITxnErrorCodes.EMPTY_COW_ID);
	        Cow cow;
      	  try {
      		cow = farmerService.findByCowId(cowId);
	          } catch (Exception e) {
	              e.printStackTrace();
	              LOGGER.info(e.getMessage());
	              throw new SwitchException(ITxnErrorCodes.INVALID_COW);
	          }
	          validateEmptyObject(cow, ITxnErrorCodes.INVALID_COW);
	          costFarming.setCow(cow);
	        
	        String collDate = (String) reqData.get(TxnEnrollmentProperties.COLLECTION_DATE);
	        errorCodes.put(EMPTY, ITxnErrorCodes.EMPTY_COLLECTION_DATE);
	        errorCodes.put(INVALID, ITxnErrorCodes.INVALID_DATE_FORMAT);
	        costFarming.setCollectionDate(validateDate(collDate, DateUtil.TXN_TIME_FORMAT,true, errorCodes));
	        String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
	        validateEmptyString(receiptNo, ITxnErrorCodes.EMPTY_RECEIPT_NO);
	        costFarming.setReceiptNo(receiptNo);
	        
	        String housingCost = (String) reqData.get(TxnEnrollmentProperties.HOUSING_COST);
	        String feedCost = (String) reqData.get(TxnEnrollmentProperties.FEED_COST);
	        String treatCost = (String) reqData.get(TxnEnrollmentProperties.TREATEMENT_COST);
	        String otherCost = (String) reqData.get(TxnEnrollmentProperties.OTHER_COST);
	        String totExpence =(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_EXPENCE)))
					? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_EXPENCE)) : "";
	        String incomeMilk = (String) reqData.get(TxnEnrollmentProperties.INCOME_MILK);
	        String incomeOther = (String) reqData.get(TxnEnrollmentProperties.INCOME_OTHER);
	        String totalIncome = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_INCOME)))
					? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_INCOME)) : "";
			String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
			String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
			
	        costFarming.setHousingCost(!StringUtil.isEmpty(housingCost)?Double.valueOf(housingCost):0.0);
	        costFarming.setFeedCost(!StringUtil.isEmpty(feedCost)?Double.valueOf(feedCost):0.0);
	        
	        costFarming.setTreatementCost(!StringUtil.isEmpty(treatCost)?Double.valueOf(treatCost):0.0);
	        costFarming.setOtherCost(!StringUtil.isEmpty(otherCost)?Double.valueOf(otherCost):0.0);
	        costFarming.setTotalExpence(!StringUtil.isEmpty(totExpence)?Double.valueOf(totExpence):0.0);
	        costFarming.setIncomeMilk(!StringUtil.isEmpty(incomeMilk)?Double.valueOf(incomeMilk):0.0);
	        costFarming.setIncomeOther(!StringUtil.isEmpty(incomeOther)?Double.valueOf(incomeOther):0.0);
	        costFarming.setTotalIncome(!StringUtil.isEmpty(totalIncome)?Double.valueOf(totalIncome):0.0);
	        costFarming.setCreateDate(new Date());
	        costFarming.setBranchId(!StringUtil.isEmpty(head.getBranchId())?head.getBranchId():"");
	        costFarming.setLatitude(latitude);
	        costFarming.setLongitude(longitude);
	        Agent agent = agentService.findAgentByAgentId(head.getAgentId());
	        if (!ObjectUtil.isEmpty(agent)) {
	        	costFarming.setCreatedUserName(head.getAgentId());
	        	costFarming.setLastUpdatedUserName(head.getAgentId());
	        }
	        farmerService.addCostFarming(costFarming);
	        return null;
	}
	
	
	/**
	 * 
	 * Validate Empty String
	 * @param input
	 * @param errorCode
	 */
	 private void validateEmptyString(String input, String errorCode) {

	        if (StringUtil.isEmpty(input))
	            throw new SwitchException(errorCode);
	    }
	 
	 /**
	  * Validate Date
	  * @param dateString
	  * @param format
	  * @param isMandatory
	  * @param errorCodes
	  * @return
	  */
	    private Date validateDate(String dateString, String format, boolean isMandatory,
	            Map<String, String> errorCodes) {

	        if (isMandatory) {
	            if (StringUtil.isEmpty(dateString))
	                throw new SwitchException(errorCodes.get(EMPTY));
	        } else {
	            if (StringUtil.isEmpty(dateString))
	                return null;
	        }
	        try {
	            return DateUtil.convertStringToDate(dateString.trim(), format);
	        } catch (Exception e) {
	            e.printStackTrace();
	            LOGGER.info(e.getMessage());
	            throw new SwitchException(errorCodes.get(INVALID));
	        }
	    }


	    /**
	     * Validate empty object.
	     * @param input 
	     * @param errorCode 
	     */
	    private void validateEmptyObject(Object input, String errorCode) {

	        if (ObjectUtil.isEmpty(input))
	            throw new SwitchException(errorCode);
	    }

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

}
