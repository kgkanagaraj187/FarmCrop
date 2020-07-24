package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

@Component
public class SupplierProcurementDownload implements ITxnAdapter {
	@Autowired
	private IProductDistributionService productDistributionService;

	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private ILocationService locationService;

	@Autowired
	private IPreferencesService preferncesService;

	private Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	private Map<String, String> warehouseMap = new LinkedHashMap<String, String>();
	private Map<String, String> samithiMap = new LinkedHashMap<String, String>();
	private Map<String, String> masterDataMap = new LinkedHashMap<String, String>();

	Collection collection = new Collection();
	List<Object> listOfProcurementObject = new ArrayList<Object>();

	Collection prefCollection = new Collection();
	List<Object> listOfPrefObject = new ArrayList<Object>();

	private Map<String, GradeMaster> gradeMasterMap;
	Map resp = new HashMap();

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		listOfProcurementObject = new ArrayList<Object>();
		listOfPrefObject = new ArrayList<Object>();

		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String ssDate = (String) reqData.get(TxnEnrollmentProperties.START_DATE);
        String eeDate = (String) reqData.get(TxnEnrollmentProperties.END_DATE);


		productDistributionService.listSupplierProcurementDetailProperties(ssDate,eeDate).stream()
				.forEach(supplierProcurementDetail -> {
					List<Data> procurementData = new LinkedList<Data>();

					Data id = new Data();
					id.setKey(TxnEnrollmentProperties.ID);
					id.setValue(String.valueOf(supplierProcurementDetail[1]));
					procurementData.add(id);

					Date d = null;
					if (!StringUtil.isEmpty(supplierProcurementDetail[2])) {
						String dateString = getFormattedValue(supplierProcurementDetail[2]).substring(0,getFormattedValue(supplierProcurementDetail[2]).length()-2);
						d = DateUtil.convertStringToDate(dateString,
								DateUtil.DATABASE_DATE_TIME);
					}

					Data procDateData = new Data();
					procDateData.setKey(TxnEnrollmentProperties.PROCUREMENT_DATE);
					procDateData.setValue(!StringUtil.isEmpty(d)
							? String.valueOf(DateUtil.convertDateToString(d, DateUtil.DATE_FORMAT_1)) : "");
					procurementData.add(procDateData);
					
					Data invoiceNo = new Data();
					invoiceNo.setKey(TxnEnrollmentProperties.RECEIPT_NO);
					invoiceNo.setValue(!StringUtil.isEmpty(supplierProcurementDetail[3])
							? getFormattedValue(supplierProcurementDetail[3]) : "");
					procurementData.add(invoiceNo);

					Data season = new Data();
					season.setKey(TxnEnrollmentProperties.SEASON);
					if (!StringUtil.isEmpty(supplierProcurementDetail[4])) {
						season.setValue(getSeasonMap().containsKey(getFormattedValue(supplierProcurementDetail[4]))
								? getSeasonMap().get(getFormattedValue(supplierProcurementDetail[4])) : "");
					} else {
						season.setValue("");
					}
					procurementData.add(season);

					Data warehouse = new Data();
					warehouse.setKey(TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE);
					if (!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[5]))
							&& StringUtil.isLong(getFormattedValue(supplierProcurementDetail[5]))) {
						warehouse
								.setValue(getWarehouseMap().containsKey(getFormattedValue(supplierProcurementDetail[5]))
										? getWarehouseMap().get(getFormattedValue(supplierProcurementDetail[5])) : "");
					} else {
						warehouse.setValue("");
					}
					procurementData.add(warehouse);

					Data supType = new Data();
					supType.setKey("supType");
					if (getFormattedValue(supplierProcurementDetail[6]).equals("1")) {
						supType.setValue("MANDI TRADER");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("2")) {
						supType.setValue("MANDI AGGREGATOR");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("3")) {
						supType.setValue("FARM AGGREGATOR");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("4")) {
						supType.setValue("FPO");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("5")) {
						supType.setValue("CIG");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("6")) {
						supType.setValue("FIG");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("7")) {
						supType.setValue("PRODUCE IMPORTER");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("11")) {
						supType.setValue("FPO/FG");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("12")) {
						supType.setValue("Customer");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("13")) {
						supType.setValue("Cash Purchase");
					} else if (getFormattedValue(supplierProcurementDetail[6]).equals("99")) {
						supType.setValue("FARMER");
					} else {
						System.out.println(getFormattedValue(supplierProcurementDetail[6]));
					}
					procurementData.add(supType);

					Data masterData = new Data();
					masterData.setKey("mData");
					if (!getFormattedValue(supplierProcurementDetail[6]).equalsIgnoreCase("99")
							&& !getFormattedValue(supplierProcurementDetail[6]).equalsIgnoreCase("11")) {
						if (getFormattedValue(supplierProcurementDetail[17]).equals("0")) {
							if (!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[7]))) {
								MasterData mData = farmerService
										.findMasterDataIdByCode(supplierProcurementDetail[7].toString());
								
									masterData
									.setValue(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
									
								/*
								
								
								
								
								masterData
										.setValue(getMasterMap().get(getFormattedValue(supplierProcurementDetail[7])));*/
							} else {
								masterData.setValue("NA");
							}
						} else {
							masterData.setValue(!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[7]))
									? getFormattedValue(supplierProcurementDetail[7]) : "");
						}
					} else if (getFormattedValue(supplierProcurementDetail[6]).equalsIgnoreCase("11")) {
						if (!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[7]))) {
							if (getSamithi().containsKey(getFormattedValue(supplierProcurementDetail[7]))) {
								masterData
										.setValue((getSamithi().get(getFormattedValue(supplierProcurementDetail[7]))));
							} else {
								masterData.setValue("NA");
							}

						} else {
							masterData.setValue("NA");
						}
					} else {
						masterData.setValue("NA");
					}
					procurementData.add(masterData);

					Data farmerData = new Data();
					farmerData.setKey("farmerData");

					Data villageData = new Data();
					villageData.setKey("villageData");

					Data cityData = new Data();
					cityData.setKey("cityData");

					Data groupData = new Data();
					groupData.setKey("groupData");
					
					Data farmData = new Data();
					farmData.setKey("farmData");
					
					Farm farm = new Farm();
					if (getFormattedValue(supplierProcurementDetail[6]).equals("99")
							|| getFormattedValue(supplierProcurementDetail[6]).equals("11")) {
						if (StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[7]))) {

							if (!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[8]))) {
								farmerData.setValue(!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[8]))
										? getFormattedValue(supplierProcurementDetail[8]) : "NA");
								villageData
										.setValue(!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[10]))
												? getFormattedValue(supplierProcurementDetail[10]) : "NA");

								cityData.setValue(!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[11]))
										? getFormattedValue(supplierProcurementDetail[11]) : "NA");

								groupData.setValue(!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[9]))
										? getFormattedValue(supplierProcurementDetail[9]) : "NA");
								if(!ObjectUtil.isEmpty(supplierProcurementDetail[18]))
									farm = farmerService.findFarmByFarmerId(Long.valueOf(supplierProcurementDetail[19].toString()));
								
								farmData.setValue(!ObjectUtil.isEmpty(farm)&& !ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) ? farm.getFarmDetailedInfo().getSurveyNumber() : "NA");
								
							}
						} else {

							farmerData.setValue(!StringUtil.isEmpty(getFormattedValue(supplierProcurementDetail[18]))
									? getFormattedValue(supplierProcurementDetail[8]) : "N/A");
							villageData.setValue("N/A");
							cityData.setValue("N/A");
							groupData.setValue("N/A");
							farmData.setValue("N/A");
						}

					} else {
						farmerData.setValue("N/A");
						villageData.setValue("N/A");
						cityData.setValue("N/A");
						groupData.setValue("N/A");
						farmData.setValue("N/A");
					}
					procurementData.add(farmerData);
					procurementData.add(villageData);
					procurementData.add(cityData);
					procurementData.add(groupData);
					procurementData.add(farmData);
					
					Data invoiceValue = new Data();
					invoiceValue.setKey("invoiceValue");
					Double invoiceVal=Double.valueOf(getFormattedValue(supplierProcurementDetail[20]));
					invoiceValue.setValue(CurrencyUtil.getDecimalFormat(invoiceVal, "##.00"));
					procurementData.add(invoiceValue);
					
					Data procProduct = new Data();
					procProduct.setKey("procProduct");
					procProduct.setValue(getFormattedValue(supplierProcurementDetail[12]));
					procurementData.add(procProduct);

					Data procgrade = new Data();
					procgrade.setKey("procGrade");
					procgrade.setValue(getFormattedValue(supplierProcurementDetail[13]));
					procurementData.add(procgrade);

					Double price = !StringUtil.isEmpty(supplierProcurementDetail[15])? 
							Double.parseDouble(supplierProcurementDetail[15].toString()) : 0.00;

					Data nBags = new Data();
					nBags.setKey("nBags");
					nBags.setValue(getFormattedValue(supplierProcurementDetail[14]));
					procurementData.add(nBags);
					/*
					 * 1=Id,2=Txn
					 * time,3=invoice,4=season,5=warehouseId,6=procMasterType,7=
					 * procMasterTypeId,8=Farmer Name,9=Group,10=Village
					 * 11=City,12=product name,13=grade
					 * name,14=numberOfBags,15=ratePerUnit,16=grossWeight
					 */
					Data pricez = new Data();
					pricez.setKey("price");
					pricez.setValue(CurrencyUtil.getDecimalFormat(price, "##.00"));
					procurementData.add(pricez);

					Data netWeight = new Data();
					netWeight.setKey("netWeight");
					netWeight.setValue(CurrencyUtil.getDecimalFormat(
							Double.valueOf(getFormattedValue(supplierProcurementDetail[16])), "##.00"));
					procurementData.add(netWeight);

					Data totalz = new Data();
					totalz.setKey("totalz");
					Double total = price * Double.valueOf(getFormattedValue(supplierProcurementDetail[16]));
					totalz.setValue(CurrencyUtil.getDecimalFormat(total, "##.00"));
					procurementData.add(totalz);
					
					Object procurementObject = new Object();
					procurementObject.setData(procurementData);

					listOfProcurementObject.add(procurementObject);

				});

		List<Data> prefData = new LinkedList<Data>();
		ESESystem mainese = preferncesService.findPrefernceByOrganisationId(head.getBranchId());

		Data adl1 = new Data();
		adl1.setKey("adl1");
		adl1.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE1)));
		prefData.add(adl1);

		Data adl2 = new Data();
		adl2.setKey("adl2");
		adl2.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE2)));
		prefData.add(adl2);

		Data adl3 = new Data();
		adl3.setKey("adl3");
		adl3.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE3)));
		prefData.add(adl3);

		Data adl4 = new Data();
		adl4.setKey("adl4");
		adl4.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE4)));
		prefData.add(adl4);

		Data adl5 = new Data();
		adl5.setKey("adl5");
		adl5.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE5)));
		prefData.add(adl5);

		Data adl6 = new Data();
		adl6.setKey("adl6");
		adl6.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE6)));
		prefData.add(adl6);

		Data adl7 = new Data();
		adl7.setKey("adl7");
		adl7.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE7)));
		prefData.add(adl7);

		Data adl8 = new Data();
		adl8.setKey("adl8");
		adl8.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE8)));
		prefData.add(adl8);

		Data adl9 = new Data();
		adl9.setKey("adl9");
		adl9.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE9)));
		prefData.add(adl9);

		Data adl10 = new Data();
		adl10.setKey("adl10");
		adl10.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE10)));
		prefData.add(adl10);

		Data adl11 = new Data();
		adl11.setKey("adl11");
		adl11.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE11)));
		prefData.add(adl11);

		Data adl12 = new Data();
		adl12.setKey("adl12");
		adl12.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE12)));
		prefData.add(adl12);

		Data adl13 = new Data();
		adl13.setKey("adl13");
		adl13.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE13)));
		prefData.add(adl13);

		Data adl14 = new Data();
		adl14.setKey("adl14");
		adl14.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE14)));
		prefData.add(adl14);

		Data adl15 = new Data();
		adl15.setKey("adl15");
		adl15.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE15)));
		prefData.add(adl15);

		Data adl16 = new Data();
		adl16.setKey("adl16");
		adl16.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE16)));
		prefData.add(adl16);
		
		Data adl17 = new Data();
		adl17.setKey("adl17");
		adl17.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.ADDRESS_LINE17)));
		prefData.add(adl17);
		
		Data barcode = new Data();
		barcode.setKey("barcode");
		barcode.setValue(getFormattedValue(mainese.getPreferences().get(ESESystem.BARCODE)));
		prefData.add(barcode);

		Object prefObj = new Object();
		prefObj.setData(prefData);

		listOfPrefObject.add(prefObj);

		collection.setObject(listOfProcurementObject);
		resp.put(TxnEnrollmentProperties.PROCUREMENT_LIST, collection);

		prefCollection.setObject(listOfPrefObject);
		resp.put(TxnEnrollmentProperties.PREF_LIST, prefCollection);

		return resp;
	}

	public Map<String, GradeMaster> getGradeMasterMap() {

		if (ObjectUtil.isEmpty(gradeMasterMap)) {
			gradeMasterMap = new LinkedHashMap<String, GradeMaster>();
			List<GradeMaster> gradeMasterList = productDistributionService.listGradeMaster();
			for (GradeMaster gradeMaster : gradeMasterList)
				gradeMasterMap.put(gradeMaster.getCode(), gradeMaster);
		}
		return gradeMasterMap;

	}

	private Map<String, String> getSeasonMap() {
		if (seasonMap.size() <= 0) {
			seasonMap = farmerService.listHarvestSeasons().stream()
					.collect(Collectors.toMap(HarvestSeason::getCode, HarvestSeason::getName));
		}
		return seasonMap;
	}

	private Map<String, String> getWarehouseMap() {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouses().stream()
					.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[2])));
		}
		return warehouseMap;
	}

	private Map<String, String> getSamithi() {
		if (samithiMap.size() <= 0) {
			samithiMap = locationService.listOfGroup().stream()
					.collect(Collectors.toMap(obj -> String.valueOf(obj[2]), obj -> String.valueOf(obj[1])));
		}
		return samithiMap;
	}

	private Map<String, String> getMasterMap() {
		if (masterDataMap.size() <= 0) {
			masterDataMap = farmerService.listMasterType().stream()
					.collect(Collectors.toMap(MasterData::getCode, MasterData::getName));
		}
		return masterDataMap;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getFormattedValue(String val) {
		if (!StringUtil.isEmpty(val)) {
			return val;
		}
		return "";
	}

	public String getFormattedValue(java.lang.Object val) {
		if (!StringUtil.isEmpty(val)) {
			return String.valueOf(val);
		}
		return "";
	}

}
