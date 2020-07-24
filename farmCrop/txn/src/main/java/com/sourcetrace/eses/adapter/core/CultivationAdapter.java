package com.sourcetrace.eses.adapter.core;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.CultivationDetail;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class CultivationAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(CultivationAdapter.class.getName());
	@Autowired
	private IFarmerService farmerService;

	Set<CultivationDetail> cultivationDetailsSet = new HashSet<CultivationDetail>();

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();
		// mode = Integer.parseInt(head.getMode());

		LOGGER.info("----- HEADER VALUES -------");
		LOGGER.info("Serial Number : " + serialNo);
		LOGGER.info("Agent id : " + agentId);

		/** GET REQUEST DATA **/

		String receiptNo = (String) reqData.get(TxnEnrollmentProperties.CULTIVATION_RECEIPT_NO);
		String farmId = (String) reqData.get(TxnEnrollmentProperties.CULTIVATION_FARM_ID);
		String cropCode = (String) reqData.get(TxnEnrollmentProperties.CROP_CODE);
		String txnType = (String) reqData.get(TxnEnrollmentProperties.TXN_TYPE);
		String cocDate = (String) reqData.get(TxnEnrollmentProperties.COC_DATE);
		String cSeasonCode = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE)))
				? (String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE) : "";

		//Income Releated Keys
		
		String agriIncome = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.AGRI_INCOME)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.AGRI_INCOME)) : "";

		String interCropIncome = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.INTERCROP_INCOME)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.INTERCROP_INCOME)) : "";

		String otherSourceIncome = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.OTHERSOURCES_INCOME)))
						? CurrencyUtil.formatByUSDcomma(
								(String) reqData.get(TxnEnrollmentProperties.OTHERSOURCES_INCOME))
						: "";
		//Expense Releated Keys
				
		String soilPre =(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.SOIL_PREPARE)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.SOIL_PREPARE)): "";
		String LandPreLabour=(!StringUtil.isEmpty((String)reqData.get(TxnEnrollmentProperties.LAND_LABOUR_COST)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.LAND_LABOUR_COST)): "";
				
		String totalSowing = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_SOWING)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_SOWING)) : "";
		String sowLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.SOWING_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.SOWING_LABOUR_COST)) : "";
						
		String totalGap = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_GAP)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_GAP)) : "";
		String gapLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.GAP_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.GAP_LABOUR_COST)) : "";
						
		String totalWeed = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_WEED)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_WEED)) : "";
		String weedLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.WEED_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.WEED_LABOUR_COST)) : "";

		String totalCulture = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_CULTURE)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_CULTURE)) : "";
		String cultureLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.CULTURE_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.CULTURE_LABOUR_COST)) : "";

		String totalIrrig = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_IRRIG)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_IRRIG)) : "";
		String irriLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.IRRI_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.IRRI_LABOUR_COST)) : "";

		String totalFerti = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_FERTI)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_FERTI)) : "";
		String fertiLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.FERTI_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.FERTI_LABOUR_COST)) : "";
						
		String totalPesti = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_PESTI)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_PESTI)) : "";
		String pestLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.PEST_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.PEST_LABOUR_COST)) : "";
		
	   String totalManure = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_MANURE)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_MANURE)) : "";
	   String manureLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.MANURE_LABOUR_COST)))
									? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.MANURE_LABOUR_COST)) : "";

		String totalHarv = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_HARV)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_HARV)) : "";
		String harvestLabour = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.HARVEST_LABOUR_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.HARVEST_LABOUR_COST)) : "";
				
		String labourCost =(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.LABOUR_HIRE)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.LABOUR_HIRE)) : "";
		
		String fertilizerCost = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.COST_OF_FERTILIZER)))
				?(String) reqData.get(TxnEnrollmentProperties.COST_OF_FERTILIZER): "";

		String pesticideCost = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.PESTICIDE_COST)))
				?(String) reqData.get(TxnEnrollmentProperties.PESTICIDE_COST) : "";
			
		String manureCost = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.MANURE_COST)))
						? (String) reqData.get(TxnEnrollmentProperties.MANURE_COST) : "";
						
			   String manureUse = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.MANURE_USE)))
								? (String) reqData.get(TxnEnrollmentProperties.MANURE_USE) : "";
				String fertUse = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.FERT_USE)))
										? (String) reqData.get(TxnEnrollmentProperties.FERT_USE) : "";
				String pestUse = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.PEST_USE)))
												? (String) reqData.get(TxnEnrollmentProperties.PEST_USE) : "";
		/*
		 * String totalCoc=(String) reqData
		 * .get(TxnEnrollmentProperties.TOTAL_EXP_AMT);
		 */

		/*
		 * Double tempTotal = Double.valueOf((String) reqData
		 * .get(TxnEnrollmentProperties.TOTAL_EXP_AMT)) ;
		 */

		String totalCoc = (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_EXP_AMT)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_EXP_AMT)) : "";

		//String totalOtherExp; 
		String otherExp= (!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TOTAL_OTR_EXP)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TOTAL_OTR_EXP)) : "0";
		String transExp=(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TRANSPORST_COST)))
				? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.TRANSPORST_COST)) : "0";
		String fuelExp=(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.FUEL_COST)))
						? CurrencyUtil.formatByUSDcomma((String) reqData.get(TxnEnrollmentProperties.FUEL_COST)) : "0";
		
		
		String cotQty = (String) reqData.get(TxnEnrollmentProperties.COTTON_QTY);
		String salePrice = (String) reqData.get(TxnEnrollmentProperties.COTTON_SALE_PR);
		String saleCtIncome = (String) reqData.get(TxnEnrollmentProperties.INCOME_CT_SALE);		
		String latitude=(String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		String longitude=(String) reqData.get(TxnEnrollmentProperties.LONGITUDE);

		Date cocDate1 = null;
		if (StringUtil.isEmpty(cocDate)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_COC_DATE);

		} else {
			try {
				cocDate1 = DateUtil.convertStringToDate(cocDate, DateUtil.TXN_DATE_TIME);
			} catch (Exception e) {
				throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
			}
		}

		Farm farm = new Farm();
		Cultivation cultivation = new Cultivation();

		if (StringUtil.isEmpty(farmId)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_FARM);
		}
			farm = farmerService.findFarmByfarmId(farmId);
		if (ObjectUtil.isEmpty(farm))
			throw new SwitchException(SwitchErrorCodes.FARM_NOT_EXIST);

		if (StringUtil.isEmpty(receiptNo)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_RECEIPT_NO);
		}

		if (StringUtil.isEmpty(txnType)) {
			throw new SwitchException(SwitchErrorCodes.TXN_TYPE_EMPTY);
		}

		cultivation.setExpenseDate(cocDate1);
		cultivation.setFarmId(farmId);

		cultivation.setCropCode(cropCode);
		cultivation.setFarmName(farm.getFarmName());
		cultivation.setFarmerName(farm.getFarmer().getName());
		cultivation.setSamCode(String.valueOf(farm.getFarmer().getSamithi().getId()));
		cultivation.setReceiptNo(receiptNo);
		/*
		 * cultivation.setExpenseType((StringUtil.isEmpty(expenseType)?"" :
		 * expenseType));
		 */
		cultivation.setLandTotal(soilPre);
		cultivation.setLandLabourCost(LandPreLabour);
		cultivation.setTotalSowing(totalSowing);
		cultivation.setSowingLabourCost(sowLabour);
		cultivation.setTotalGap(totalGap);
		cultivation.setGapLabourCost(gapLabour);
		cultivation.setTotalCulture(totalCulture);
		cultivation.setCultureLabourCost(cultureLabour);
		cultivation.setTotalWeed(totalWeed);
		cultivation.setWeedLabourCost(weedLabour);
		cultivation.setTotalIrrigation(totalIrrig);
		cultivation.setIrriLabourCost(irriLabour);
		cultivation.setTotalFertilizer(totalFerti);
		cultivation.setFertLabourcost(fertiLabour);
		cultivation.setTotalPesticide(totalPesti);
		cultivation.setPestLabourCost(pestLabour);
		cultivation.setTotalManure(totalManure);
		cultivation.setManureLabourCost(manureLabour);
		cultivation.setTotalHarvest(totalHarv);
		cultivation.setHarvestLabourCost(harvestLabour);
		cultivation.setCurrentSeasonCode(cSeasonCode);
		cultivation.setTxnType(txnType);
		cultivation.setAgriIncome(agriIncome.trim());
		cultivation.setInterCropIncome(interCropIncome.trim());
		cultivation.setOtherSourcesIncome(otherSourceIncome.trim());
		cultivation.setBranchId(branchId);
		// cultivation.setFertilizerCost(Double.valueOf(fertilizerCost));
		cultivation.setFarmerId(farm.getFarmer().getFarmerId());
		cultivation.setVillageId(String.valueOf(farm.getFarmer().getVillage().getId()));
		cultivation.setTotalCoc(totalCoc);
		cultivation.setTransportCost(transExp);
		cultivation.setFuelCost(fuelExp);
		cultivation.setOtherCost(otherExp);
		cultivation.setTotalExpense(String.valueOf(Double.parseDouble(fuelExp)+Double.parseDouble(transExp)+Double.parseDouble(otherExp)));
		
		cultivation.setLabourCost(labourCost);
		cultivation.setSoilPreparation(soilPre);
		cultivation.setCottonQty(cotQty);
		cultivation.setUnitSalePrice(salePrice);
		cultivation.setSaleCottonIncome(saleCtIncome);
		cultivation.setAgentId(agentId);
		cultivation.setLatitude(latitude);
		cultivation.setLongitude(longitude);
		/*
		 * double total = cultivation.getPloughing() +
		 * cultivation.getRidgeFurrow() + cultivation.getSeedCost() +
		 * cultivation.getSowingTreat() + cultivation.getMenCost() +
		 * cultivation.getWomenCost() + cultivation.getPackingMaterial() +
		 * cultivation.getTransport() + cultivation.getMiscellaneous() +
		 * cultivation.getFertilizerCost(); cultivation.setTotalCost(total);
		 */
			cultivationDetailsSet.clear();
			if (!StringUtil.isEmpty(totalFerti) || !StringUtil.isEmpty(totalPesti)
					|| !StringUtil.isEmpty(totalManure)) {
				if (!StringUtil.isEmpty(totalPesti)) {
					long expenseValue = 2;
					cultivation.setCultivationDetails(getCultivationDetailsData(totalPesti, expenseValue,pestUse));
				}
				if (!StringUtil.isEmpty(totalFerti)) {
					long expenseValue = 1;
					cultivation.setCultivationDetails(getCultivationDetailsData(totalFerti, expenseValue,fertUse));
				}
				if (!StringUtil.isEmpty(totalManure)) {
					long expenseValue = 3;
					cultivation.setCultivationDetails(getCultivationDetailsData(totalManure, expenseValue,manureUse));
				}
			}
		
		try {
			farmerService.saveCultivation(cultivation);
			if (!ObjectUtil.isEmpty(cultivation.getFarmerId())) {
					Farmer farmer = farmerService.findFarmerByFarmerId(cultivation.getFarmerId());
					if (!ObjectUtil.isEmpty(farmer)) {
						farmerService.updateFarmerRevisionNo(farmer.getId(), DateUtil.getRevisionNumber());
					}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** FORM RESPONSE DATA **/
		Map resp = new HashMap();
		return resp;
	}

	private void processPeriodicInspection(Object object) {

		Cultivation cultivation = (Cultivation) object;
		String type = "";
		if (!ObjectUtil.isListEmpty(cultivation.getCultivationDetails())) {
			for (CultivationDetail cultiDetail : cultivation.getCultivationDetails())
				if (!ObjectUtil.isEmpty(cultivation) && !ObjectUtil.isEmpty(cultiDetail)) {
					if (cultiDetail.getType() == 1) {
						type = "FRTATYP";
					} else if (cultiDetail.getType() == 2) {
						type = "PESTREC";
					} else {
						type = "MATYP";
					}
					farmerService.updatePeriodicInspectionData(cultivation.getFarmId(), cultivation.getCropCode(),
							cultiDetail.getFertilizerType(), type);
				}
		}
	}

	private Set<CultivationDetail> getCultivationDetailsData(String fertilizerDetails, long expenseValue,String UsageDetail ) {
		// Set<CultivationDetail> cultivationDetailsSet = new
		// HashSet<CultivationDetail>();
		/*if (fertilizerDetails.contains(",")) {
			List<String> costSeperator = Arrays.asList(fertilizerDetails.split("\\,"));
			for (String seperatedString : costSeperator) {
				CultivationDetail cultivationDetail = new CultivationDetail();
				String[] values = seperatedString.split(":");
				cultivationDetail.setType(expenseValue);
				cultivationDetail.setFertilizerType(values[0]);
				cultivationDetail.setCost(Double.valueOf(values[2]));
				cultivationDetail.setQty(Double.valueOf(values[1]));
				cultivationDetail.setCompleted(String.valueOf(values[4]));
				if (values.length == 5) {
					cultivationDetail.setUsageLevel(values[3]);
				}
				cultivationDetailsSet.add(cultivationDetail);
			}
		} else {
			CultivationDetail cultivationDetail = new CultivationDetail();
			String[] values = fertilizerDetails.split(":");
			cultivationDetail.setType(expenseValue);
			cultivationDetail.setFertilizerType(values[0]);
			cultivationDetail.setCost(Double.valueOf(values[2]));
			cultivationDetail.setQty(Double.valueOf(values[1]));
			cultivationDetail.setCompleted(String.valueOf(values[4]));
			if (values.length == 5) {
				cultivationDetail.setUsageLevel(values[3]);
			}
			cultivationDetailsSet.add(cultivationDetail);

		}*/
		CultivationDetail cultivationDetail = new CultivationDetail();
		cultivationDetail.setType(expenseValue);
		cultivationDetail.setFertilizerType("-1");
		cultivationDetail.setCost(Double.valueOf(fertilizerDetails));
		cultivationDetail.setQty(0.00);
		cultivationDetail.setUsageLevel(UsageDetail);
		cultivationDetail.setCompleted("");
		cultivationDetailsSet.add(cultivationDetail);
		
		return cultivationDetailsSet;
	}
	
	private Set<CultivationDetail> getCultivationDetail(String fertilizerDetails, long expenseValue) {
		// Set<CultivationDetail> cultivationDetailsSet = new
		// HashSet<CultivationDetail>();
		if (fertilizerDetails.contains(",")) {
			List<String> costSeperator = Arrays.asList(fertilizerDetails.split("\\,"));
			for (String seperatedString : costSeperator) {
				CultivationDetail cultivationDetail = new CultivationDetail();
				String[] values = seperatedString.split(":");
				cultivationDetail.setType(expenseValue);
				cultivationDetail.setFertilizerType(values[0]);
				cultivationDetail.setCost(0.00);
				cultivationDetail.setQty(Double.valueOf(values[1]));
				cultivationDetail.setUom(values[2]);
			
				cultivationDetailsSet.add(cultivationDetail);
			}
		} else {
			CultivationDetail cultivationDetail = new CultivationDetail();
			String[] values = fertilizerDetails.split(":");
			cultivationDetail.setType(expenseValue);
			cultivationDetail.setFertilizerType(values[0]);
			cultivationDetail.setCost(0.00);
			cultivationDetail.setQty(Double.valueOf(values[1]));
			cultivationDetail.setUom(values[2]);
			cultivationDetailsSet.add(cultivationDetail);

		}
		return cultivationDetailsSet;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}
