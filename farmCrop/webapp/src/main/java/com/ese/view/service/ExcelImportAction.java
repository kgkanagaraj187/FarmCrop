package com.ese.view.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ExcelImportDetail;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

@SuppressWarnings("serial")
public class ExcelImportAction extends SwitchValidatorAction {
	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_1);
	private InputStream fileInputStream;
	private String formattedFileName;

	/** SERVICE INJECTION */
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IFarmCropsService farmCropsService;
	@Autowired
	private IClientService clientService;

	private static final int CELL_TYPE_BLANK = 3;
	private static final String STREAM = "stream";

	public String fileUpload() {

		request.setAttribute(HEADING, getText("create.page"));
		return INPUT;
	}

	@Override
	public Object getData() {

		return null;
	}

	public String populateXLS() throws IOException {

		Integer sheetRowCount = 0;
		int i = 1;
		if (!StringUtil.isEmpty(uploadFileFileName)) {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			File fileToCreate = new File(filePath, uploadFileFileName);
			if (ObjectUtil.isEmpty(fileToCreate)) {
				throw new FileNotFoundException();
			}
			FileUtils.copyFile(uploadFile, fileToCreate);
			try {
				HSSFWorkbook workBook = null;
				FileInputStream fstream = new FileInputStream(fileToCreate.toString());
				workBook = new HSSFWorkbook(fstream);
				HSSFSheet hssfsheet = workBook.getSheetAt(1);
				sheetRowCount = hssfsheet.getLastRowNum();

				/** SHEET COUNT CHECKING */
				if (sheetRowCount == 0) {
					addActionMessage(getText("sheet.empty"));
					return INPUT;
				}

				/** Farmer Section */
				List<String> headerValue = formFarmerXlsHeaders();
				int colSize = headerValue.size() - 1;

				Map<String, String> tempMap;
				while (i <= sheetRowCount) {
					int nullCount = 0;
					HSSFRow hssfRow = hssfsheet.getRow(i);
					tempMap = new LinkedHashMap<String, String>();
					for (int columnIndex = 0; columnIndex <= colSize; columnIndex++) {
						HSSFCell hssfCell = null;
						hssfCell = hssfRow.getCell(columnIndex);
						if ((hssfCell == null) || (hssfCell.getCellType() == CELL_TYPE_BLANK)) {
							nullCount++;
							String key = (String) headerValue.get(columnIndex);
							tempMap.put(key, null);
						} else {
							String key = (String) headerValue.get(columnIndex);
							String value = trimIncludingNonbreakingSpace(hssfCell.toString());
							tempMap.put(key, value);
						}
					}
					if (nullCount != headerValue.size()) {
						processData(tempMap);
					}
					i++;
				}
				HSSFSheet hssfsheet2 = workBook.getSheetAt(2);
				Integer rowCount = hssfsheet2.getLastRowNum();
				if (rowCount > 1) {
					populateFarmData();
				}

			} catch (Exception e) {
				addActionError("Error in Row" + i + "Cause:" + e.toString());
				e.printStackTrace();
			}
		}
		return INPUT;
	}

	public String populateFarmData() {

		/** Farm Section */
		Integer sheetRowCount = 0;
		int i = 1;
		try {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			File fileToCreate = new File(filePath, uploadFileFileName);
			if (ObjectUtil.isEmpty(fileToCreate)) {
				throw new FileNotFoundException();
			}
			FileUtils.copyFile(uploadFile, fileToCreate);

			List<String> headerValue = formFarmXlsHeaders();
			int colSize = headerValue.size() - 1;
			HSSFWorkbook workBook = null;
			FileInputStream fstream = new FileInputStream(fileToCreate.toString());
			workBook = new HSSFWorkbook(fstream);
			HSSFSheet hssfsheet = workBook.getSheetAt(2);
			sheetRowCount = hssfsheet.getLastRowNum();

			Map<String, String> tempMap;
			while (i <= sheetRowCount) {
				int nullCount = 0;
				HSSFRow hssfRow = hssfsheet.getRow(i);
				tempMap = new LinkedHashMap<String, String>();
				for (int columnIndex = 0; columnIndex <= colSize; columnIndex++) {
					HSSFCell hssfCell = null;
					hssfCell = hssfRow.getCell(columnIndex);
					if ((hssfCell == null) || (hssfCell.getCellType() == CELL_TYPE_BLANK)) {
						nullCount++;
						String key = (String) headerValue.get(columnIndex);
						tempMap.put(key, null);
					} else {
						String key = (String) headerValue.get(columnIndex);
						String value = trimIncludingNonbreakingSpace(hssfCell.toString());
						tempMap.put(key, value);
					}
				}
				if (nullCount != headerValue.size()) {
					processFarmData(tempMap);
				}
				i++;
			}
		} catch (Exception e) {
			addActionError("Error in Farm: Row No-" + i + "Cause:" + e.toString());
			e.printStackTrace();
		}
		return INPUT;
	}

	public String populateCropData() {

		int i = 1;
		Integer sheetRowCount = 0;
		try {
			try {
				String filePath = request.getSession().getServletContext().getRealPath("/");
				File fileToCreate = new File(filePath, uploadFileFileName);
				if (ObjectUtil.isEmpty(fileToCreate)) {
					throw new FileNotFoundException();
				}
				FileUtils.copyFile(uploadFile, fileToCreate);

				List<String> headerValue = formFarmCropXlsHeaders();
				int colSize = headerValue.size() - 1;
				HSSFWorkbook workBook = null;
				FileInputStream fstream = new FileInputStream(fileToCreate.toString());
				workBook = new HSSFWorkbook(fstream);
				HSSFSheet hssfsheet = workBook.getSheetAt(3);
				sheetRowCount = hssfsheet.getLastRowNum();

				Map<String, String> tempMap;
				while (i <= sheetRowCount) {
					int nullCount = 0;
					HSSFRow hssfRow = hssfsheet.getRow(i);
					tempMap = new LinkedHashMap<String, String>();
					for (int columnIndex = 0; columnIndex <= colSize; columnIndex++) {
						HSSFCell hssfCell = null;
						hssfCell = hssfRow.getCell(columnIndex);
						if ((hssfCell == null) || (hssfCell.getCellType() == CELL_TYPE_BLANK)) {
							nullCount++;
							String key = (String) headerValue.get(columnIndex);
							tempMap.put(key, null);
						} else {
							String key = (String) headerValue.get(columnIndex);
							String value = trimIncludingNonbreakingSpace(hssfCell.toString());
							tempMap.put(key, value);
						}
					}
					if (nullCount != headerValue.size()) {
						processFarmCropData(tempMap);
					}
					i++;
				}
			} catch (Exception e) {
				addActionError("Error in Farm: Row No-" + i + "Cause:" + e.toString());
				e.printStackTrace();
			}
		} catch (Exception e) {
			addActionError("Error in Farm Crop: Row No-" + i + "Cause:" + e.toString());
			e.printStackTrace();
		}
		return INPUT;
	}

	private void processFarmCropData(Map<String, String> tempMap) {

		String key, value;
		ExcelImportDetail excelImportDetail = new ExcelImportDetail();
		for (Entry<String, String> entry : tempMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			if (key.equalsIgnoreCase(ExcelImportProperties.CROP_FARM_CODE)) {
				excelImportDetail.setCropFarmCode(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CROP_CATEGORY)) {
				excelImportDetail.setCropCategory(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SEASON)) {
				excelImportDetail.setSeason(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CULTIVATION_TYPE)) {
				excelImportDetail.setCultivationType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CROP_NAME)) {
				excelImportDetail.setCropName(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.VARIETY)) {
				excelImportDetail.setVariety(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SOWING_DATE)) {
				excelImportDetail.setSowingDate(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.TYPE)) {
				excelImportDetail.setType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SEED_SOURCE)) {
				excelImportDetail.setSeedSource(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.STAPLE_LEN)) {
				excelImportDetail.setStapleLen(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SEED_QUANTITY_USED)) {
				excelImportDetail.setSeedQuantityUsed(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SEED_QUANTITY_COST)) {
				excelImportDetail.setSeedQuantityCost(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ESTIMATED_YIELD_CROPS)) {
				excelImportDetail.setEstimatedYieldCrop(value);
			}
		}
		validateAndFormFarmCropData(excelImportDetail);
	}

	private void processFarmData(Map<String, String> valuesMap) {

		String key, value;
		ExcelImportDetail excelImportDetail = new ExcelImportDetail();
		for (Entry<String, String> entry : valuesMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			if (key.equalsIgnoreCase(ExcelImportProperties.CODE)) {
				excelImportDetail.setFarmfarmerCode(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.FARM_NAME)) {
				excelImportDetail.setFarmName(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SURVEY_NUMBER)) {
				excelImportDetail.setSurveyNumber(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.TOTAL_LAND_HOLDING)) {
				excelImportDetail.setTotalLandHolding(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.PROPOSED_PLANTING)) {
				excelImportDetail.setProposedPlanting(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LAND_OWNERSHIP)) {
				excelImportDetail.setLandOwnership(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LAND_GRADIENT)) {
				excelImportDetail.setLandGradient(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.IS_SAME_ADDRESS)) {
				excelImportDetail.setIsSameAddress(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.FARM_ADDRESS)) {
				excelImportDetail.setFarmAddress(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.APPROACH_ROAD)) {
				excelImportDetail.setApproachRoad(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SOIL_TYPE)) {
				excelImportDetail.setSoilType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SOIL_TEXTURE)) {
				excelImportDetail.setSoilTexture(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.FERTILITY_STATUS)) {
				excelImportDetail.setFertilityStatus(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.IRRIGATION_SOURCE)) {
				excelImportDetail.setIrrigationSource(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.IRRIGATION_TYPE)) {
				excelImportDetail.setIrrigationType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.OTHERS)) {
				excelImportDetail.setOthers(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.FULL_TIME_WORKERS_COUNT)) {
				excelImportDetail.setFullTimeWorkersCount(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.PART_TIME_WORKERS_COUNT)) {
				excelImportDetail.setPartTimeWorkersCount(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SEASONAL_WORKERS_COUNT)) {
				excelImportDetail.setSeasonalWorkesCount(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LAST_DATE_CHEMICAL_APPLIED)) {
				excelImportDetail.setLastDateOfChemicalApp(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CONVENTIONAL_LAND)) {
				excelImportDetail.setConventionalLands(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.PASTURE_LAND)) {
				excelImportDetail.setPastureLand(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CONVENTIONAL_CROPS)) {
				excelImportDetail.setConventionalCrops(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ESTIMATED_YIELD)) {
				excelImportDetail.setEstimatedYield(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS1_LAND)) {
				excelImportDetail.setIcs1LandDetails(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS1_SURVEY_NUMBER)) {
				excelImportDetail.setIcs1SurveyNumber(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS1_BEGIN_CONVERSATION)) {
				excelImportDetail.setIcs1BeginOfConversation(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS2_LAND)) {
				excelImportDetail.setIcs2LandDetails(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS2_SURVEY_NUMBER)) {
				excelImportDetail.setIcs2SurveyNumber(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS2_BEGIN_CONVERSATION)) {
				excelImportDetail.setIcs2BeginOfConversation(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS3_LAND)) {
				excelImportDetail.setIcs3LandDetails(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS3_SURVEY_NUMBER)) {
				excelImportDetail.setIcs3SurveyNumber(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS3_BEGIN_CONVERSATION)) {
				excelImportDetail.setIcs3BeginOfConversation(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ORGANIC_LAND)) {
				excelImportDetail.setOrganicLandDetails(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ORGANIC_SURVEY_NUMBER)) {
				excelImportDetail.setOrganicSurveyNumber(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ORGANIC_BEGIN_CONVERSATION)) {
				excelImportDetail.setOrganicBeginOfConversation(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LATITUDE)) {
				excelImportDetail.setLatitude(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LONGITUDE)) {
				excelImportDetail.setLongitude(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LAND_MARK)) {
				excelImportDetail.setLandMark(value);
			}
			
			else if (key.equalsIgnoreCase(ExcelImportProperties.TOTAL_PROD_AREA)) {
				excelImportDetail.setTotalProdArea(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.TOTAL_ORGANIC_AREA)) {
				excelImportDetail.setTotalOrganicArea(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.TOTAL_NON_ORGANIC_AREA)) {
				excelImportDetail.setTotalNonOrganicArea(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.EST_YIELD_TO_CERTI)) {
				excelImportDetail.setEstYieldToCerti(value);
			}/*else if (key.equalsIgnoreCase(ExcelImportProperties.TOTAL_CULTI_AREA)) {
				excelImportDetail.setTotalCultiArea(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.DISTANCE_PROCESSING_UNIT)) {
				excelImportDetail.setDistanceProcessingUnit(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.PROCESS_ACTIVITY)) {
				excelImportDetail.setProcessingActivity(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.ICS_TYPE)) {
				excelImportDetail.setIcsType(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.INSPECTION_DATE)) {
				excelImportDetail.setInspectionDate(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.INSPECTOR_NAME)) {
				excelImportDetail.setInspectorName(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.QUALIFIED)) {
				excelImportDetail.setQualified(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.ORGANIC_STATUS)) {
				excelImportDetail.setOrganicStatus(value);
			}*/
			
		}
		validateAndFormFarmData(excelImportDetail);
	}

	private void processData(Map<String, String> valuesMap) {

		String key, value;
		ExcelImportDetail excelImportDetail = new ExcelImportDetail();
		for (Entry<String, String> entry : valuesMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			if (key.equalsIgnoreCase(ExcelImportProperties.SNO)) {
				excelImportDetail.setsNo(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.CODE)) {
				excelImportDetail.setFarmerCode(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.NAME)) {
				excelImportDetail.setFarmerName(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.FATHERNAME)) {
				excelImportDetail.setFatherName(value);
			}else if (key.equalsIgnoreCase(ExcelImportProperties.SURNAME)) {
				excelImportDetail.setSurName(value);
			}
			else if (key.equalsIgnoreCase(ExcelImportProperties.GENDER)) {
				excelImportDetail.setGender(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.DOB)) {
				excelImportDetail.setDob(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.AGE)) {
				excelImportDetail.setAge(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ACCBALANCE)) {
				excelImportDetail.setAccBalance(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ADDRESS)) {
				excelImportDetail.setAddress(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.MOBILE_NUMBER)) {
				excelImportDetail.setMobile(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.EMAIL)) {
				excelImportDetail.setEmail(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.COUNTRY)) {
				excelImportDetail.setCountry(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.STATE)) {
				excelImportDetail.setState(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.DISTRICT)) {
				excelImportDetail.setDistrict(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.TALUK)) {
				excelImportDetail.setCity(value);
			} /*else if (key.equalsIgnoreCase(ExcelImportProperties.GRAMPANCHAYAT)) {
				excelImportDetail.setGramPanchayat(value);
			}*/ else if (key.equalsIgnoreCase(ExcelImportProperties.VILLAGE)) {
				excelImportDetail.setVillage(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.GROUP)) {
				excelImportDetail.setWarehouse(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.PHONENUMBER)) {
				excelImportDetail.setPhoneNumber(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.STATUS)) {
				excelImportDetail.setStatus(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.NAME_OF_ICS)) {
				excelImportDetail.setNameOfICS(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ICS_CODE)) {
				excelImportDetail.setIcsCode(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CERT_TYPE)) {
				excelImportDetail.setCertType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.IS_CERTIFIED)) {
				excelImportDetail.setIsCertified(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.NO_OF_FAMILY_MEMBERS)) {
				excelImportDetail.setNoOfFamilyMem(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ADULT)) {
				excelImportDetail.setAdult(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CHILDREN)) {
				excelImportDetail.setChildren(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SCHOOL_GOING_CHILD)) {
				excelImportDetail.setSchoolGoingChild(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.MIGRATING_FOR_WRK)) {
				excelImportDetail.setMigratedMembers(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.MALE)) {
				excelImportDetail.setMale(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.FEMLAE)) {
				excelImportDetail.setFemale(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.AGRI)) {
				excelImportDetail.setAgriculture(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.OTHER_SOURCE)) {
				excelImportDetail.setOtherSource(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CONSUMER_ELECTRONICS)) {
				excelImportDetail.setConsumerElectronics(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.VEHICLE)) {
				excelImportDetail.setVehicle(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CELLPHONE)) {
				excelImportDetail.setCellPhone(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.HOUSING_OWNERSHIP)) {
				excelImportDetail.setHousingOwnerShip(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.HOUSING_OWNERSHIP_OTH)) {
				excelImportDetail.setHousingOwnerShipOth(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ELECTRIFIED_HOUSE)) {
				excelImportDetail.setElectrifiedHouse(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.HOUSING_TYPE)) {
				excelImportDetail.setHousingType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.HOUSING_TYPE_OTHERS)) {
				excelImportDetail.setHousingTypeOth(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.DRINKING_WATER_SOURCE)) {
				excelImportDetail.setDrinkingWaterSource(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.DRINKING_WATER_SOURCE_OTHER)) {
				excelImportDetail.setDrinkingWaterSourceOth(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LIFE_HEALTH_INS)) {
				excelImportDetail.setLifeHealthIns(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.CROP_INS)) {
				excelImportDetail.setCropIns(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LOAN_TAKEN_FROM)) {
				excelImportDetail.setLoanTaken(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.LOAN_TAKEN_LAST_YEAR)) {
				excelImportDetail.setLoanTakenFrom(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.BANK_ACC_TYPE)) {
				excelImportDetail.setBankAccType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.BANK_ACC_NO)) {
				excelImportDetail.setBankAccType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.BANK_NAME)) {
				excelImportDetail.setBankAccType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.BRANCH_DETIALS)) {
				excelImportDetail.setBankAccType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.IFSC_CODE)) {
				excelImportDetail.setBankAccType(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.AADHAR_CARD)) {
				excelImportDetail.setAadharNo(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.ID_PROOF)) {
				excelImportDetail.setIdProof(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.PROOF_NO)) {
				excelImportDetail.setProofNo(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.RELIGION)) {
				excelImportDetail.setReligion(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SOCIAL_CATAGEORY)) {
				excelImportDetail.setSocialCategory(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.PRIMARY_OCCUPATION)) {
				excelImportDetail.setPrimaryOccupation(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.SECONDARY_OCCUPATION)) {
				excelImportDetail.setSecondaryOccupation(value);
			} else if (key.equalsIgnoreCase(ExcelImportProperties.EDUCATION)) {
				excelImportDetail.setEducation(value);
			}

		}
		validateAndFormFarmerData(excelImportDetail);
	}

	private void validateAndFormFarmData(ExcelImportDetail excelImportDetail) {
		Farm farm = new Farm();
		FarmDetailedInfo farmDetailedInfo = new FarmDetailedInfo();
		if (!ObjectUtil.isEmpty(excelImportDetail)) {
			farm.setFarmCode(String.valueOf(DateUtil.getRevisionNoDateTimeMilliSec()));
			farm.setFarmName(excelImportDetail.getFarmName() + " Farm");
			farmDetailedInfo.setSurveyNumber(excelImportDetail.getSurveyNumber());
			farmDetailedInfo.setTotalLandHolding(excelImportDetail.getTotalLandHolding());
			farmDetailedInfo.setProposedPlantingArea(excelImportDetail.getProposedPlanting());
			
			//farm.setDistanceProcessingUnit(excelImportDetail.getDistanceProcessingUnit());
			//farmDetailedInfo.setProcessingActivity(Integer.parseInt(excelImportDetail.getProcessingActivity()));
			//farmDetailedInfo.setTotalLandHolding(excelImportDetail.getTotalProdArea());
			farmDetailedInfo.setProposedPlantingArea(excelImportDetail.getTotalOrganicArea());
			farmDetailedInfo.setConventionalLand(excelImportDetail.getTotalNonOrganicArea());
			farmDetailedInfo.setFallowOrPastureLand(excelImportDetail.getPastureLand());			
			farmDetailedInfo.setConventionalEstimatedYield(excelImportDetail.getEstYieldToCerti());
			if (!StringUtil.isEmpty(excelImportDetail.getLandGradient())) {
				String landGradient = "";
				if (excelImportDetail.getLandGradient().toLowerCase().startsWith("deep")) {
					landGradient = landGradient + "1,";
				} else if (excelImportDetail.getLandGradient().toLowerCase().startsWith("gentle")) {
					landGradient = landGradient + "2,";
				} else if (excelImportDetail.getLandGradient().toLowerCase().startsWith("plain")) {
					landGradient = landGradient + "3,";
				} else if (excelImportDetail.getLandGradient().toLowerCase().startsWith("undul")) {
					landGradient = landGradient + "4,";
				} else if (excelImportDetail.getLandGradient().toLowerCase().startsWith("oth")) {
					landGradient = landGradient + "99,";
				}
			}

			if (!StringUtil.isEmpty(excelImportDetail.getIsSameAddress())) {
				if (excelImportDetail.getIsSameAddress().trim().equalsIgnoreCase("y")
						|| excelImportDetail.getIsSameAddress().trim().equalsIgnoreCase("yes")) {
					farmDetailedInfo.setSameAddressofFarmer(true);
				} else if (excelImportDetail.getIsSameAddress().trim().equalsIgnoreCase("N")
						|| excelImportDetail.getIsSameAddress().trim().equalsIgnoreCase("NO")) {
					farmDetailedInfo.setSameAddressofFarmer(false);
				} else {
					farmDetailedInfo.setSameAddressofFarmer(false);
				}
			}

			farmDetailedInfo.setFarmAddress(excelImportDetail.getFarmAddress());
			if (!StringUtil.isEmpty(excelImportDetail.getApproachRoad())) {
				String approachRoad = "";
				if (excelImportDetail.getApproachRoad().toLowerCase().startsWith("Cart")) {
					approachRoad = approachRoad + "1,";
				} else if (excelImportDetail.getApproachRoad().toLowerCase().startsWith("Foot")) {
					approachRoad = approachRoad + "2,";
				} else if (excelImportDetail.getApproachRoad().toLowerCase().startsWith("Metal")) {
					approachRoad = approachRoad + "3,";
				} else if (excelImportDetail.getApproachRoad().toLowerCase().startsWith("Thar")) {
					approachRoad = approachRoad + "4,";
				}
				farmDetailedInfo.setApproachRoad(approachRoad);
			}
			farmDetailedInfo.setFullTimeWorkersCount(excelImportDetail.getFullTimeWorkersCount());
			farmDetailedInfo.setPartTimeWorkersCount(excelImportDetail.getPartTimeWorkersCount());
			farmDetailedInfo.setSeasonalWorkersCount(excelImportDetail.getSeasonalWorkesCount());

			farmDetailedInfo.setLastDateOfChemicalApplication(excelImportDetail.getLastDateOfChemicalApp());
			farmDetailedInfo.setConventionalLand(excelImportDetail.getConventionalLands());
			farmDetailedInfo.setFallowOrPastureLand(excelImportDetail.getPastureLand());
			farmDetailedInfo.setConventionalCrops(excelImportDetail.getConventionalCrops());
			farmDetailedInfo.setConventionalEstimatedYield(excelImportDetail.getEstimatedYield());

			farm.setFarmICS(formIcsDetails(excelImportDetail));
			farm.setFarmDetailedInfo(farmDetailedInfo);

			String lat = null, lon = null;
			if (!StringUtil.isEmpty(excelImportDetail.getLatitude())) {
				String dms = excelImportDetail.getLatitude().trim();
				String deg = dms.substring(0, 2);
				String mins = dms.substring(3, 5);
				String secs = dms.substring(6, 9);
				double d = 0, m = 0, s = 0;
				d = Double.valueOf(deg);
				m = Double.valueOf(mins);
				s = Double.valueOf(secs);

				lat = String.valueOf(Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0)));
			}

			if (!StringUtil.isEmpty(excelImportDetail.getLongitude())) {
				String dms = excelImportDetail.getLongitude().trim();
				String deg = dms.trim().substring(0, 2);
				String mins = dms.trim().substring(3, 5);
				String secs = dms.trim().substring(6, 9);
				double d = 0, m = 0, s = 0;

				d = Double.valueOf(deg);
				m = Double.valueOf(mins);
				s = Double.valueOf(secs);

				lon = String.valueOf(Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0)));
				}

			farm.setLatitude(lat);
			farm.setLongitude(excelImportDetail.getLongitude());
			farm.setIsVerified(0);
			farm.setFarmPlatNo(excelImportDetail.getPlatNo());
			if (!StringUtil.isEmpty(excelImportDetail.getFarmfarmerCode())) {

				/*
				 * Farmer farmer = farmerService.findFarmerByFarmerCode(
				 * StringUtil.getExact(excelImportDetail.getFarmfarmerCode().
				 * trim(), Farmer.FARMER_ID_LENGTH));
				 */

				Farmer farmer = farmerService
						.findFarmerById(Long.parseLong(excelImportDetail.getFarmfarmerCode().trim()));
				if (!StringUtil.isEmpty(farmer)) {
					farm.setFarmer(farmer);
					farmerService.addFarmDetailedInfo(farm.getFarmDetailedInfo());
					farm.setFarmDetailedInfo(farm.getFarmDetailedInfo());

					farmerService.addFarm(farm);
					//farmerService.updateFarmerRevisionNo(farmer.getId(), DateUtil.getRevisionNoDateTimeMilliSec());
				}
			}
		}
		}
	private void validateAndFormFarmCropData(ExcelImportDetail excelImportDetail) {

		FarmCrops farmCrops = new FarmCrops();
		if (!StringUtil.isEmpty(excelImportDetail.getCropCategory())) {
			if (excelImportDetail.getCropCategory().trim().toLowerCase().startsWith("m")) {
				farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
			} else if (excelImportDetail.getCropCategory().trim().toLowerCase().startsWith("i")) {
				farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
			}
		}

		if (!StringUtil.isEmpty(excelImportDetail.getSeason())) {
			HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(excelImportDetail.getSeason());
			if (!ObjectUtil.isEmpty(harvestSeason)) {
				farmCrops.setCropSeason(harvestSeason);
			}
		}

		if (!StringUtil.isEmpty(excelImportDetail.getCultivationType())) {
			if (excelImportDetail.getCultivationType().trim().toLowerCase().startsWith("fru")) {
				farmCrops.setCropCategoryList("0");
			} else if (excelImportDetail.getCultivationType().trim().toLowerCase().startsWith("veg")) {
				farmCrops.setCropCategoryList("1");
			}
		}

		if (!StringUtil.isEmpty(excelImportDetail.getCropName())) {
			ProcurementProduct procurementProduct = productDistributionService
					.findProcurementProductByName(excelImportDetail.getCropName().trim());
			if (!ObjectUtil.isEmpty(procurementProduct)) {
				if (!StringUtil.isEmpty(excelImportDetail.getVariety())) {
					ProcurementVariety variety = productDistributionService
							.findProcurementVariertyByName(excelImportDetail.getVariety().trim());
					if (!ObjectUtil.isEmpty(variety)) {
						farmCrops.setProcurementVariety(variety);
					} else {
						ProcurementVariety procurementVariteyNew = new ProcurementVariety();
						procurementVariteyNew.setName(excelImportDetail.getCropName());
						procurementVariteyNew.setYield("0");
						procurementVariteyNew.setCode(idGenerator.getProcurementVarietyIdSeq());
						procurementVariteyNew.setProcurementProduct(procurementProduct);
						productDistributionService.addProcurementVariety(procurementVariteyNew);
					}
				}
			} else {
				ProcurementProduct procurementProductNew = new ProcurementProduct();
				procurementProductNew.setName(excelImportDetail.getCropName());
				procurementProductNew.setCode(idGenerator.getProductEnrollIdSeq());
				procurementProductNew.setBranchId(getBranchId());
				productDistributionService.addProcurementProduct(procurementProduct);
				if (!StringUtil.isEmpty(excelImportDetail.getVariety())) {
					ProcurementVariety procurementVariteyNew = new ProcurementVariety();
					procurementVariteyNew.setName(excelImportDetail.getCropName());
					procurementVariteyNew.setYield("0");
					procurementVariteyNew.setCode(idGenerator.getProcurementVarietyIdSeq());
					procurementVariteyNew.setProcurementProduct(procurementProductNew);
					productDistributionService.addProcurementVariety(procurementVariteyNew);
					farmCrops.setProcurementVariety(procurementVariteyNew);
				}
			}
		}

		if (!StringUtil.isEmpty(excelImportDetail.getSowingDate())) {
			Date sowingDate;
			try {
				sowingDate = df.parse(excelImportDetail.getSowingDate().trim());
				farmCrops.setSowingDate(sowingDate);
			} catch (ParseException e) {
				farmCrops.setSowingDate(null);
			}

		}
		if (!StringUtil.isEmpty(excelImportDetail.getType())) {
			if (excelImportDetail.getType().trim().startsWith("Hybrid")) {
				farmCrops.setType("1");
			} else if (excelImportDetail.getType().trim().startsWith("BT")) {
				farmCrops.setType("2");
			} else if (excelImportDetail.getType().trim().startsWith("Desi")) {
				farmCrops.setType("3");
			}
		}

		if (!StringUtil.isEmpty(excelImportDetail.getSeedSource())) {
			if (excelImportDetail.getSeedSource().trim().startsWith("Own")) {
				farmCrops.setSeedSource(String.valueOf(1));
			} else if (excelImportDetail.getSeedSource().trim().startsWith("Dealer")) {
				farmCrops.setSeedSource(String.valueOf(2));
			}
		}

		farmCrops.setSeedQtyUsed(StringUtil.isDouble(excelImportDetail.getSeedQuantityUsed())
				? Double.parseDouble(excelImportDetail.getSeedQuantityUsed()) : 0D);
		farmCrops.setSeedQtyCost(StringUtil.isDouble(excelImportDetail.getSeedQuantityCost())
				? Double.parseDouble(excelImportDetail.getSeedQuantityCost()) : 0D);
		farmCrops.setEstimatedYield(StringUtil.isDouble(excelImportDetail.getEstimatedYieldCrop())
				? Double.parseDouble(excelImportDetail.getEstimatedYieldCrop()) : 0D);

		if (!StringUtil.isEmpty(excelImportDetail.getCropFarmCode())) {
			Farm farm = farmerService.findFarmByCode(excelImportDetail.getCropFarmCode());
			if (!StringUtil.isEmpty(farm)) {
				farmCrops.setFarm(farm);
				farmCropsService.addFarmCrops(farmCrops);
				farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
				farmerService.updateFarmerRevisionNo(farmCrops.getFarm().getFarmer().getId(),
						DateUtil.getRevisionNumber());
			}
		}
	}

	private Set<FarmICS> formIcsDetails(ExcelImportDetail excelImportDetail) {

		Set<FarmICS> farmIcsSet = new LinkedHashSet<>();
		boolean flag = false;

		if (!StringUtil.isEmpty(excelImportDetail.getIcs1LandDetails())
				|| !StringUtil.isEmpty(excelImportDetail.getIcs1SurveyNumber())
				|| !StringUtil.isEmpty(excelImportDetail.getIcs1BeginOfConversation())) {
			FarmICS farmIcs = new FarmICS();
			farmIcs.setLandIcsDetails(excelImportDetail.getIcs1LandDetails());
			farmIcs.setSurveyNo(excelImportDetail.getIcs1SurveyNumber());
			farmIcs.setIcsType(FarmICS.ICSTypes.ICS_1.ordinal());
			farmIcs.setStatus(FarmICS.ACTIVE);
			if (!StringUtil.isEmpty(excelImportDetail.getIcs1BeginOfConversation())) {
				try {
					Date beginOfCon = df.parse(excelImportDetail.getIcs1BeginOfConversation().trim());
					farmIcs.setBeginOfConversion(beginOfCon);
				} catch (Exception e) {
					farmIcs.setBeginOfConversion(null);
				}
			}
			flag = true;
			farmIcsSet.add(farmIcs);
		}
		if (!StringUtil.isEmpty(excelImportDetail.getIcs2LandDetails())
				|| !StringUtil.isEmpty(excelImportDetail.getIcs2SurveyNumber())
				|| !StringUtil.isEmpty(excelImportDetail.getIcs2BeginOfConversation())) {
			FarmICS farmIcs = new FarmICS();
			farmIcs.setLandIcsDetails(excelImportDetail.getIcs2LandDetails());
			farmIcs.setSurveyNo(excelImportDetail.getIcs2SurveyNumber());
			farmIcs.setIcsType(FarmICS.ICSTypes.ICS_2.ordinal());
			farmIcs.setStatus(FarmICS.ACTIVE);
			if (!StringUtil.isEmpty(excelImportDetail.getIcs2BeginOfConversation())) {
				try {
					Date beginOfCon = df.parse(excelImportDetail.getIcs2BeginOfConversation().trim());
					farmIcs.setBeginOfConversion(beginOfCon);
				} catch (Exception e) {
					farmIcs.setBeginOfConversion(null);
				}
			}
			flag = true;
			farmIcsSet.add(farmIcs);
		}
		if (!StringUtil.isEmpty(excelImportDetail.getIcs3LandDetails())
				|| !StringUtil.isEmpty(excelImportDetail.getIcs3SurveyNumber())
				|| !StringUtil.isEmpty(excelImportDetail.getIcs3BeginOfConversation())) {
			FarmICS farmIcs = new FarmICS();
			farmIcs.setLandIcsDetails(excelImportDetail.getIcs3LandDetails());
			farmIcs.setSurveyNo(excelImportDetail.getIcs3SurveyNumber());
			farmIcs.setIcsType(FarmICS.ICSTypes.ICS_3.ordinal());
			farmIcs.setStatus(FarmICS.ACTIVE);
			if (!StringUtil.isEmpty(excelImportDetail.getIcs3BeginOfConversation())) {
				try {
					Date beginOfCon = df.parse(excelImportDetail.getIcs3BeginOfConversation().trim());
					farmIcs.setBeginOfConversion(beginOfCon);
				} catch (Exception e) {
					farmIcs.setBeginOfConversion(null);
				}
			}
			flag = true;
			farmIcsSet.add(farmIcs);
		}
		if (!StringUtil.isEmpty(excelImportDetail.getOrganicLandDetails())
				|| !StringUtil.isEmpty(excelImportDetail.getOrganicSurveyNumber())
				|| !StringUtil.isEmpty(excelImportDetail.getOrganicBeginOfConversation())) {
			FarmICS farmIcs = new FarmICS();
			farmIcs.setLandIcsDetails(excelImportDetail.getOrganicLandDetails());
			farmIcs.setSurveyNo(excelImportDetail.getOrganicSurveyNumber());
			farmIcs.setIcsType(FarmICS.ICSTypes.ORGANIC.ordinal());
			farmIcs.setStatus(FarmICS.ACTIVE);
			if (!StringUtil.isEmpty(excelImportDetail.getOrganicBeginOfConversation())) {
				try {
					Date beginOfCon = df.parse(excelImportDetail.getOrganicBeginOfConversation().trim());
					farmIcs.setBeginOfConversion(beginOfCon);
				} catch (Exception e) {
					farmIcs.setBeginOfConversion(null);
				}
			}
			flag = true;
			farmIcsSet.add(farmIcs);
		}
		if (flag) {
			return farmIcsSet;
		} else {
			return null;
		}
	}

	private void validateAndFormFarmerData(ExcelImportDetail excelImportDetail) {

		Farmer farmer = new Farmer();
		String enableGp = preferncesService.findPrefernceByName(ESESystem.ENABLE_GRAMPANJAYAT);
		if (!ObjectUtil.isEmpty(excelImportDetail)) {

			if (!StringUtil.isEmpty(excelImportDetail.getFarmerCode())) {
				farmer.setFarmerCode(excelImportDetail.getFarmerCode());
			}

			if (!StringUtil.isEmpty(excelImportDetail.getFarmerName())) {
				farmer.setFirstName(excelImportDetail.getFarmerName());
			}

			if (!StringUtil.isEmpty(excelImportDetail.getFatherName())) {
				farmer.setLastName(excelImportDetail.getFatherName());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getSurName())) {
				farmer.setSurName(excelImportDetail.getSurName());
			}
			
			if (!StringUtil.isEmpty(excelImportDetail.getMobile())) {
				farmer.setMobileNumber(excelImportDetail.getMobile());
			}
			
			if (!StringUtil.isEmpty(excelImportDetail.getGender())) {
				if (excelImportDetail.getGender().equals("1")){
					farmer.setGender("MALE");
				} else if (excelImportDetail.getGender().equals("2")) {
					farmer.setGender("FEMALE");
				}
				//farmer.setGender(excelImportDetail.getGender());
			}

			/*if (!StringUtil.isEmpty(excelImportDetail.getDob())) {
				try {
					Date farmerDOB = df.parse(excelImportDetail.getDob().trim());
					farmer.setDateOfBirth(farmerDOB);
				} catch (Exception e) {
					farmer.setDateOfBirth(null);
				}
			}*/
			if (StringUtil.isInteger(excelImportDetail.getDob())) {
				farmer.setAge(Integer.parseInt(excelImportDetail.getDob()));
			}
			/*if (StringUtil.isInteger(excelImportDetail.getEmail())) {
				farmer.setEducation(excelImportDetail.getEmail());
			}
			farmer.setRefId(Long.valueOf(excelImportDetail.getAddress()));*/
			
			/*if (!StringUtil.isEmpty(excelImportDetail.getEducation())) {
				farmer.setEducation(excelImportDetail.getEducation());
			}*/
			/*if (!StringUtil.isEmpty(excelImportDetail.getReligion())) {
				farmer.setReligion(excelImportDetail.getReligion());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getSocialCategory())) {
				farmer.setSocialCategory(excelImportDetail.getSocialCategory());
			}
			farmer.setAddress(excelImportDetail.getAddress());*/

			/*if (!StringUtil.isEmpty(excelImportDetail.getMobile())) {
				///if (ValidationUtil.isPatternMaches(excelImportDetail.getMobile(), ValidationUtil.NUMBER_PATTERN)) {
					farmer.setMobileNumber(excelImportDetail.getMobile());
				//}
			}*/
			/*farmer.setEmail(excelImportDetail.getEmail());*/

			if (!StringUtil.isEmpty(excelImportDetail.getVillage())) {
				Village village = locationService.findVillageByName(excelImportDetail.getVillage());
				if (!ObjectUtil.isEmpty(village)) {
					farmer.setVillage(village);
					farmer.setCity(village.getCity());
				} else {

					Village villageNew = new Village();
					villageNew.setName(excelImportDetail.getVillage());
					villageNew.setBranchId(getBranchId());
					villageNew.setCode(idGenerator.getVillageIdSeq());
					villageNew.setRevisionNo(DateUtil.getRevisionNumber());
					GramPanchayat gramPanchayat = null;
					Municipality city = locationService.findMunicipalityByName(excelImportDetail.getCity());

					if (!ObjectUtil.isEmpty(gramPanchayat)) {
						if (enableGp.equals("1")) {
							villageNew.setGramPanchayat(gramPanchayat);
						}
						villageNew.setCity(city);
						//villageNew.setSeq("1");
						locationService.addVillage(villageNew);
					} else {
						if (enableGp.equals("1")) {

							
						}
						if (!ObjectUtil.isEmpty(city)) {
							if (enableGp.equals("1")) {
								
							}
							villageNew.setCity(city);
							//villageNew.setSeq("1");
							locationService.addVillage(villageNew);
						} else {
							Municipality cityNew = new Municipality();
							cityNew.setName(excelImportDetail.getCity());
							cityNew.setBranchId(getBranchId());
							cityNew.setCode(idGenerator.getMandalIdSeq());
							cityNew.setRevisionNo(DateUtil.getRevisionNumber());
							Locality district = locationService.findLocalityByName(excelImportDetail.getDistrict());
							if (!ObjectUtil.isEmpty(district)) {
								cityNew.setLocality(district);
								locationService.addMunicipality(cityNew);
								
								villageNew.setCity(cityNew);
								//villageNew.setSeq("1");
								locationService.addVillage(villageNew);
							} else {
								Locality localityNew = new Locality();
								localityNew.setName(excelImportDetail.getDistrict());
								localityNew.setBranchId(getBranchId());
								localityNew.setCode(idGenerator.getDistrictIdSeq());
								localityNew.setRevisionNo(DateUtil.getRevisionNumber());
								State state = locationService.findStateByName(excelImportDetail.getState());
								if (!ObjectUtil.isEmpty(state)) {
									localityNew.setState(state);
									locationService.addLocality(localityNew);
									cityNew.setLocality(localityNew);
									locationService.addMunicipality(cityNew);
									
									
									villageNew.setCity(cityNew);
									//villageNew.setSeq("1");
									locationService.addVillage(village);
								} else {
									State stateNew = new State();
									stateNew.setName(excelImportDetail.getState());
									stateNew.setBranchId(getBranchId());
									stateNew.setCode(idGenerator.getStateIdSeq());
									stateNew.setRevisionNo(DateUtil.getRevisionNumber());
									Country country = locationService.findCountryByName(excelImportDetail.getCountry());
									if (!ObjectUtil.isEmpty(country)) {
										stateNew.setCountry(country);
										locationService.addState(stateNew);
										localityNew.setState(stateNew);
										locationService.addLocality(localityNew);
										cityNew.setLocality(localityNew);
										locationService.addMunicipality(cityNew);
										villageNew.setCity(cityNew);
										//villageNew.setSeq("1");
										locationService.addVillage(villageNew);
									} else {
										Country countryNew = new Country();
										countryNew.setName(excelImportDetail.getCountry());
										countryNew.setCode(idGenerator.getCountryIdSeq());
										countryNew.setBranchId(getBranchId());
										countryNew.setRevisionNo(DateUtil.getRevisionNumber());
										locationService.addCountry(countryNew);
										stateNew.setCountry(countryNew);
										locationService.addState(stateNew);
										localityNew.setState(stateNew);
										locationService.addLocality(localityNew);
										cityNew.setLocality(localityNew);
										locationService.addMunicipality(cityNew);
										villageNew.setCity(cityNew);
										//villageNew.setSeq("1");
										locationService.addVillage(villageNew);
									}
								}
							}
						}
					}
					farmer.setVillage(villageNew);
					farmer.setCity(villageNew.getCity());
				}
			}

			if (!StringUtil.isEmpty(excelImportDetail.getWarehouse())) {
				Warehouse samithi = locationService.findSamithiByName(excelImportDetail.getWarehouse());
				if (!ObjectUtil.isEmpty(samithi)) {
					farmer.setSamithi(samithi);
				} else {
					Warehouse samithiNew = new Warehouse();
					samithiNew.setName(excelImportDetail.getWarehouse());
					samithiNew.setBranchId(getBranchId());
					samithiNew.setCode(idGenerator.getGroupIdSeq());
					samithiNew.setRevisionNo(DateUtil.getRevisionNumber());
					samithiNew.setTypez(Warehouse.SAMITHI);
					locationService.addWarehouse(samithiNew);
					farmer.setSamithi(samithiNew);
				}
			}
			farmer.setPhoneNumber(excelImportDetail.getPhoneNumber());
			farmer.setStatus(StringUtil.isEmpty(excelImportDetail.getStatus()) ? -1
					: Integer.parseInt(excelImportDetail.getStatus()));
			//farmer.setIcsName(excelImportDetail.getNameOfICS());
			//farmer.setIcsCode(excelImportDetail.getIcsCode());
			farmer.setBranchId(getBranchId());

			farmer.setStatus(Farmer.Status.ACTIVE.ordinal());

			if (!StringUtil.isEmpty(excelImportDetail.getCertType())) {
				farmer.setIsCertifiedFarmer(Farmer.CERTIFIED_YES);
			}
			farmer.setCertificationType(Farmer.CERTIFICATION_TYPE_GROUP);

			/*farmer.setNoOfFamilyMembers(StringUtil.isEmpty(excelImportDetail.getNoOfFamilyMem()) ? 0
					: Integer.parseInt(excelImportDetail.getNoOfFamilyMem()));*/
			/*
			 * farmer.setAdultCount(excelImportDetail.getAdult());
			 * farmer.setChildCount(excelImportDetail.getChildren());
			 * farmer.setNoOfSchoolChild(StringUtil.isEmpty(excelImportDetail.
			 * getSchoolGoingChild()) ? 0 :
			 * Integer.parseInt(excelImportDetail.getSchoolGoingChild()));
			 */
			/*farmer.setNoOfHouseHoldMem(StringUtil.isEmpty(excelImportDetail.getMigratedMembers()) ? 0
					: Integer.parseInt(excelImportDetail.getMigratedMembers()));
			farmer.setMaleCnt(StringUtil.isEmpty(excelImportDetail.getMale()) ? 0
					: Integer.parseInt(excelImportDetail.getMale()));
			farmer.setFemaleCnt(StringUtil.isEmpty(excelImportDetail.getFemale()) ? 0
					: Integer.parseInt(excelImportDetail.getFemale()));*/
			
			farmer.setAdultCountMale(StringUtil.isEmpty(excelImportDetail.getNoOfFamilyMem()) ? "0"
					: excelImportDetail.getNoOfFamilyMem());
			farmer.setAdultCountFemale(StringUtil.isEmpty(excelImportDetail.getAdult()) ? "0"
					: excelImportDetail.getAdult());
			farmer.setChildCountMale(StringUtil.isEmpty(excelImportDetail.getChildren()) ? "0"
					: excelImportDetail.getChildren());
			farmer.setChildCountFemale(StringUtil.isEmpty(excelImportDetail.getSchoolGoingChild()) ? "0"
					: excelImportDetail.getSchoolGoingChild());
			farmer.setNoOfSchoolChildMale(StringUtil.isEmpty(excelImportDetail.getMigratedMembers()) ? 0
					: Integer.parseInt(excelImportDetail.getMigratedMembers()));
			farmer.setNoOfSchoolChildFemale(StringUtil.isEmpty(excelImportDetail.getMale()) ? 0
					: Integer.parseInt(excelImportDetail.getMale()));
			farmer.setNoOfHouseHoldMem(StringUtil.isEmpty(excelImportDetail.getFemale()) ? 0
					: Integer.parseInt(excelImportDetail.getFemale()));
			farmer.setTotalHsldLabel(String.valueOf(Integer.valueOf(farmer.getAdultCountMale())
					+ Integer.valueOf(farmer.getAdultCountFemale()) + Integer.valueOf(farmer.getChildCountMale())
					+ Integer.valueOf(farmer.getChildCountFemale())));
			/*farmer.setAgriculture(excelImportDetail.getAgriculture());
			farmer.setOtherSource(excelImportDetail.getOtherSource());*/
			/*if (!StringUtil.isEmpty(excelImportDetail.getAgriculture())
					|| !StringUtil.isEmpty(excelImportDetail.getAgriculture())) {
				Double totalIncome = Double
						.valueOf(!StringUtil.isEmpty(excelImportDetail.getAgriculture())
								? excelImportDetail.getOtherSource() : "0")
						+ Double.valueOf(!StringUtil.isEmpty(excelImportDetail.getAgriculture())
								? excelImportDetail.getOtherSource() : "0");
				farmer.setTotalSourceIncome(totalIncome);
			}*/
			// farmer.setConsumerElectronics(excelImportDetail.getConsumerElectronics());
			// farmer.setVehicle(excelImportDetail.getVehicle());
			// farmer.setVehicleOther(excelImportDetail.get);
			if (!StringUtil.isEmpty(excelImportDetail.getAadharNo())) {
				farmer.setAdhaarNo(excelImportDetail.getAadharNo());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getIdProof())) {
				farmer.setIdProof(excelImportDetail.getIdProof());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getProofNo())) {
				farmer.setProofNo(excelImportDetail.getProofNo());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getPrimaryOccupation())) {
				farmer.setHouseOccupationPrimary(excelImportDetail.getPrimaryOccupation());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getSecondaryOccupation())) {
				farmer.setHouseOccupationSecondary(excelImportDetail.getSecondaryOccupation());
			}
			if (!StringUtil.isEmpty(excelImportDetail.getHousingOwnerShip())
					|| !StringUtil.isEmpty(excelImportDetail.getHousingOwnerShipOth())
					|| !StringUtil.isEmpty(excelImportDetail.getElectrifiedHouse())
					|| !StringUtil.isEmpty(excelImportDetail.getHousingType())
					|| !StringUtil.isEmpty(excelImportDetail.getHousingTypeOth())
					|| !StringUtil.isEmpty(excelImportDetail.getDrinkingWaterSource())
					|| !StringUtil.isEmpty(excelImportDetail.getLifeHealthIns())
					|| !StringUtil.isEmpty(excelImportDetail.getCropIns())) {

				FarmerEconomy farmerEconomy = new FarmerEconomy();
				Integer housingOwnerShip = -1;
				Integer electrifiedHouse = -1;
				Integer housingType = -1;
				String drinkingWaterSource = "";

				if (!StringUtil.isEmpty(excelImportDetail.getHousingOwnerShip())
						&& excelImportDetail.getHousingOwnerShip() != "-1") {

					if (excelImportDetail.getHousingOwnerShip().trim().equalsIgnoreCase("Leased")) {
						housingOwnerShip = FarmerEconomy.HOUSING_OWNERSHIP.LEASED.ordinal();
					} else if (excelImportDetail.getHousingOwnerShip().trim().equalsIgnoreCase("Own")) {
						housingOwnerShip = FarmerEconomy.HOUSING_OWNERSHIP.OWN.ordinal();
					} else if (excelImportDetail.getHousingOwnerShip().trim().equalsIgnoreCase("Rented")) {
						housingOwnerShip = FarmerEconomy.HOUSING_OWNERSHIP.RENTED.ordinal();
					} else if (excelImportDetail.getHousingOwnerShip().trim().equalsIgnoreCase("Others")) {
						housingOwnerShip = 99;
						farmerEconomy.setHousingOwnershipOther(excelImportDetail.getHousingOwnerShipOth());
					}
				}
				if (!StringUtil.isEmpty(excelImportDetail.getElectrifiedHouse())
						&& excelImportDetail.getElectrifiedHouse() != "-1") {
					if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("y")
							|| excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("yes")) {
						electrifiedHouse = 1;
					} else if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("N")
							|| excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("NO")) {
						electrifiedHouse = 2;
					}
				}

				if (!StringUtil.isEmpty(excelImportDetail.getHousingType())
						&& excelImportDetail.getHousingType() != "-1") {
					if (excelImportDetail.getHousingType().equalsIgnoreCase("Bungalow")) {
						housingType = 1;
					} else if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("Cottage")) {
						housingType = 2;
					} else if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("Farm House")) {
						housingType = 3;
					} else if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("Mansion")) {
						housingType = 4;
					} else if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("Villa")) {
						housingType = 5;
					} else if (excelImportDetail.getElectrifiedHouse().equalsIgnoreCase("Other")) {
						housingType = 99;
						farmerEconomy.setOtherHousingType(excelImportDetail.getHousingTypeOth());
					}
				}

				if (!StringUtil.isEmpty(excelImportDetail.getDrinkingWaterSource())) {
					if (excelImportDetail.getDrinkingWaterSource().contains("Borewell")) {
						drinkingWaterSource = drinkingWaterSource + "1,";
					}
					if (excelImportDetail.getDrinkingWaterSource().contains("River")) {
						drinkingWaterSource = drinkingWaterSource + "2,";
					}
					if (excelImportDetail.getDrinkingWaterSource().contains("Pond")) {
						drinkingWaterSource = drinkingWaterSource + "2,";
					}
					if (excelImportDetail.getDrinkingWaterSource().contains("Tap")) {
						drinkingWaterSource = drinkingWaterSource + "3,";
					}
					if (excelImportDetail.getDrinkingWaterSource().contains("Tubewell")) {
						drinkingWaterSource = drinkingWaterSource + "4,";
					}
					if (excelImportDetail.getDrinkingWaterSource().contains(",Well")) {
						drinkingWaterSource = drinkingWaterSource + "5,";
					}
					if (excelImportDetail.getDrinkingWaterSource().contains("other")) {
						drinkingWaterSource = drinkingWaterSource + "99,";
						farmerEconomy.setDrinkingWaterSourceOther(excelImportDetail.getDrinkingWaterSourceOth());
					}
				}
				if (!StringUtil.isEmpty(excelImportDetail.getLifeHealthIns())) {
					if (excelImportDetail.getLifeHealthIns().trim().equalsIgnoreCase("y")
							|| excelImportDetail.getLifeHealthIns().trim().equalsIgnoreCase("yes")) {
						farmerEconomy.setLifeOrHealthInsurance(1);
					} else if (excelImportDetail.getLifeHealthIns().trim().equalsIgnoreCase("n")
							|| excelImportDetail.getLifeHealthIns().trim().equalsIgnoreCase("NO")) {
						farmerEconomy.setLifeOrHealthInsurance(2);
					}
				}

				if (!StringUtil.isEmpty(excelImportDetail.getCropIns())) {
					if (excelImportDetail.getCropIns().trim().equalsIgnoreCase("y")
							|| excelImportDetail.getCropIns().trim().equalsIgnoreCase("yes")) {
						farmerEconomy.setCropInsurance(1);
					} else if (excelImportDetail.getCropIns().trim().equalsIgnoreCase("n")
							|| excelImportDetail.getCropIns().trim().equalsIgnoreCase("NO")) {
						farmerEconomy.setCropInsurance(2);
					}
				}

				farmerEconomy.setHousingOwnership(housingOwnerShip);
				farmerEconomy.setElectrifiedHouse(electrifiedHouse);
				farmerEconomy.setDrinkingWaterSource(drinkingWaterSource);
				farmerEconomy.setHousingType(String.valueOf(housingType));
				farmer.setFarmerEconomy(farmerEconomy);
			}

			if (!StringUtil.isEmpty(excelImportDetail.getBankAccType())
					|| !StringUtil.isEmpty(excelImportDetail.getBankAccNo())
					|| !StringUtil.isEmpty(excelImportDetail.getBankName())
					|| !StringUtil.isEmpty(excelImportDetail.getBranchDetails())) {
				Set<BankInformation> bankInfoSet = new LinkedHashSet<>();
				BankInformation bankInfo = new BankInformation();

				/*
				 * if (!StringUtil.isEmpty(excelImportDetail.getBankAccType())
				 * && excelImportDetail.getBankAccType().trim().startsWith(
				 * "Personal")) { bankInfo.setAccType(1); } else if
				 * (!StringUtil.isEmpty(excelImportDetail.getBankAccType()) &&
				 * excelImportDetail.getBankAccType().trim().startsWith("Joint")
				 * ) { bankInfo.setAccType(2); } else if
				 * (!StringUtil.isEmpty(excelImportDetail.getBankAccType()) &&
				 * excelImportDetail.getBankAccType().trim().startsWith("Other")
				 * ) { bankInfo.setAccType(99); }
				 */
				bankInfo.setAccNo(excelImportDetail.getBankAccNo());
				bankInfo.setBankName(excelImportDetail.getBankName());
				bankInfo.setBranchName(excelImportDetail.getBranchDetails());
				bankInfo.setSortCode(excelImportDetail.getIfscCode());
				bankInfoSet.add(bankInfo);
				farmer.setBankInfo(bankInfoSet);
			}

			if (!StringUtil.isEmpty(excelImportDetail.getLoanTaken())
					&& (excelImportDetail.getLoanTaken().equalsIgnoreCase("y")
							|| excelImportDetail.getLoanTaken().equalsIgnoreCase("yes"))) {
				farmer.setLoanTakenLastYear(1);
				if (!StringUtil.isEmpty(excelImportDetail.getLoanTakenFrom())) {
					if (excelImportDetail.getLoanTakenFrom().equalsIgnoreCase("Bank")) {
						farmer.setLoanTakenFrom("1");
					} else if (excelImportDetail.getLoanTakenFrom().equalsIgnoreCase("Money Lender")) {
						farmer.setLoanTakenFrom("2");
					} else if (excelImportDetail.getLoanTakenFrom().equalsIgnoreCase("Cooperative")) {
						farmer.setLoanTakenFrom("3");
					} else if (excelImportDetail.getLoanTakenFrom().equalsIgnoreCase("Group")) {
						farmer.setLoanTakenFrom("4");
					}
				}
			} else if (!StringUtil.isEmpty(excelImportDetail.getLoanTaken())
					&& (excelImportDetail.getLoanTaken().equalsIgnoreCase("n")
							|| excelImportDetail.getLoanTaken().equalsIgnoreCase("no"))) {
				farmer.setLoanTakenLastYear(2);
			}
			farmer.setCreatedDate(new Date());
			farmer.setFarmerId(excelImportDetail.getAge());
			//farmer.setSeasonCode();
			//farmer.setRefId(Long.valueOf(excelImportDetail.getAccBalance()));

			String farmerSeqLength = preferncesService.findPrefernceByName(ESESystem.FARMER_ID_LENGTH);
			String farmerMaxRanges = preferncesService.findPrefernceByName(ESESystem.FARMER_MAX_RANGE);

			if (farmerSeqLength == null || farmerSeqLength.isEmpty()) {
				farmer.setFarmerId(idGenerator.getFarmerWebIdSeq());

			} else {
				long farmerMaxRange = Farmer.FARMER_ID_MAX_RANGE;
				if (farmerMaxRanges != null && !farmerMaxRanges.isEmpty()) {

					farmerMaxRange = Long.valueOf(farmerMaxRanges);
				}
				farmer.setFarmerId(idGenerator.getFarmerWebIdSeq(Integer.valueOf(farmerSeqLength), farmerMaxRange));
			}

			farmerService.addContractForFarmer(farmer, request.getSession().getAttribute("user").toString());
		}
	}

	public static String trimIncludingNonbreakingSpace(String s) {

		return s.replaceFirst("^[\\x00-\\x200\\xA0]+", "").replaceFirst("[\\x00-\\x20\\xA0]+$", "");
	}

	public List<String> formFarmerXlsHeaders() {

		List<String> headerList = new LinkedList<>();
		headerList.add(ExcelImportProperties.SNO);
		headerList.add(ExcelImportProperties.CODE);
		headerList.add(ExcelImportProperties.NAME);
		headerList.add(ExcelImportProperties.FATHERNAME);
		headerList.add(ExcelImportProperties.SURNAME);
		headerList.add(ExcelImportProperties.GENDER);
		headerList.add(ExcelImportProperties.DOB);
		headerList.add(ExcelImportProperties.AGE);
		/*
		 * headerList.add(ExcelImportProperties.EDUCATION);
		 * headerList.add(ExcelImportProperties.MARITAL);
		 */
		headerList.add(ExcelImportProperties.ACCBALANCE);
		headerList.add(ExcelImportProperties.ADDRESS);
		headerList.add(ExcelImportProperties.MOBILE_NUMBER);
		headerList.add(ExcelImportProperties.EMAIL);
		headerList.add(ExcelImportProperties.COUNTRY);
		headerList.add(ExcelImportProperties.STATE);
		headerList.add(ExcelImportProperties.DISTRICT);
		headerList.add(ExcelImportProperties.TALUK);
		//headerList.add(ExcelImportProperties.GRAMPANCHAYAT);
		headerList.add(ExcelImportProperties.VILLAGE);
		headerList.add(ExcelImportProperties.GROUP);
		headerList.add(ExcelImportProperties.PHONENUMBER);
		headerList.add(ExcelImportProperties.STATUS);
		headerList.add(ExcelImportProperties.NAME_OF_ICS);
		headerList.add(ExcelImportProperties.ICS_CODE);
		headerList.add(ExcelImportProperties.IS_CERTIFIED);
		headerList.add(ExcelImportProperties.CERT_TYPE);
		headerList.add(ExcelImportProperties.NO_OF_FAMILY_MEMBERS);
		headerList.add(ExcelImportProperties.ADULT);
		headerList.add(ExcelImportProperties.CHILDREN);
		headerList.add(ExcelImportProperties.SCHOOL_GOING_CHILD);
		headerList.add(ExcelImportProperties.MIGRATING_FOR_WRK);
		headerList.add(ExcelImportProperties.MALE);
		headerList.add(ExcelImportProperties.FEMLAE);
		headerList.add(ExcelImportProperties.AGRI);
		headerList.add(ExcelImportProperties.OTHER_SOURCE);
		headerList.add(ExcelImportProperties.CONSUMER_ELECTRONICS);
		headerList.add(ExcelImportProperties.VEHICLE);
		headerList.add(ExcelImportProperties.CELLPHONE);
		headerList.add(ExcelImportProperties.HOUSING_OWNERSHIP);
		headerList.add(ExcelImportProperties.HOUSING_OWNERSHIP_OTH);
		headerList.add(ExcelImportProperties.ELECTRIFIED_HOUSE);
		headerList.add(ExcelImportProperties.HOUSING_TYPE);
		headerList.add(ExcelImportProperties.HOUSING_TYPE_OTHERS);
		headerList.add(ExcelImportProperties.DRINKING_WATER_SOURCE);
		headerList.add(ExcelImportProperties.DRINKING_WATER_SOURCE_OTHER);
		headerList.add(ExcelImportProperties.LIFE_HEALTH_INS);
		headerList.add(ExcelImportProperties.CROP_INS);
		headerList.add(ExcelImportProperties.LOAN_TAKEN_LAST_YEAR);
		headerList.add(ExcelImportProperties.LOAN_TAKEN_FROM);
		headerList.add(ExcelImportProperties.BANK_ACC_TYPE);
		headerList.add(ExcelImportProperties.BANK_ACC_NO);
		headerList.add(ExcelImportProperties.BANK_NAME);
		headerList.add(ExcelImportProperties.BRANCH_DETIALS);
		headerList.add(ExcelImportProperties.IFSC_CODE);
		headerList.add(ExcelImportProperties.AADHAR_CARD);
		headerList.add(ExcelImportProperties.ID_PROOF);
		headerList.add(ExcelImportProperties.PROOF_NO);
		headerList.add(ExcelImportProperties.RELIGION);
		headerList.add(ExcelImportProperties.SOCIAL_CATAGEORY);
		headerList.add(ExcelImportProperties.PRIMARY_OCCUPATION);
		headerList.add(ExcelImportProperties.SECONDARY_OCCUPATION);
		headerList.add(ExcelImportProperties.EDUCATION);
		return headerList;
	}

	public List<String> formFarmXlsHeaders() {

		List<String> farmHeaders = new LinkedList<>();
		farmHeaders.add(ExcelImportProperties.SNO);
		farmHeaders.add(ExcelImportProperties.CODE);
		farmHeaders.add(ExcelImportProperties.FARM_NAME);
		farmHeaders.add(ExcelImportProperties.SURVEY_NUMBER);
		farmHeaders.add(ExcelImportProperties.TOTAL_LAND_HOLDING);
		farmHeaders.add(ExcelImportProperties.PROPOSED_PLANTING);
		farmHeaders.add(ExcelImportProperties.LAND_OWNERSHIP);
		farmHeaders.add(ExcelImportProperties.LAND_GRADIENT);
		farmHeaders.add(ExcelImportProperties.IS_SAME_ADDRESS);
		farmHeaders.add(ExcelImportProperties.FARM_ADDRESS);
		farmHeaders.add(ExcelImportProperties.APPROACH_ROAD);
		/*
		 * farmHeaders.add(ExcelImportProperties.SOIL_TYPE);
		 * farmHeaders.add(ExcelImportProperties.SOIL_TEXTURE);
		 * farmHeaders.add(ExcelImportProperties.FERTILITY_STATUS);
		 * farmHeaders.add(ExcelImportProperties.IRRIGATION_SOURCE);
		 * farmHeaders.add(ExcelImportProperties.IRRIGATION_TYPE);
		 * farmHeaders.add(ExcelImportProperties.OTHERS);
		 */ farmHeaders.add(ExcelImportProperties.FULL_TIME_WORKERS_COUNT);
		farmHeaders.add(ExcelImportProperties.PART_TIME_WORKERS_COUNT);
		farmHeaders.add(ExcelImportProperties.SEASONAL_WORKERS_COUNT);
		farmHeaders.add(ExcelImportProperties.LAST_DATE_CHEMICAL_APPLIED);
		farmHeaders.add(ExcelImportProperties.CONVENTIONAL_LAND);
		farmHeaders.add(ExcelImportProperties.PASTURE_LAND);
		farmHeaders.add(ExcelImportProperties.CONVENTIONAL_CROPS);
		farmHeaders.add(ExcelImportProperties.ESTIMATED_YIELD);
		farmHeaders.add(ExcelImportProperties.ICS1_LAND);
		farmHeaders.add(ExcelImportProperties.ICS1_SURVEY_NUMBER);
		farmHeaders.add(ExcelImportProperties.ICS1_BEGIN_CONVERSATION);
		farmHeaders.add(ExcelImportProperties.ICS2_LAND);
		farmHeaders.add(ExcelImportProperties.ICS2_SURVEY_NUMBER);
		farmHeaders.add(ExcelImportProperties.ICS2_BEGIN_CONVERSATION);
		farmHeaders.add(ExcelImportProperties.ICS3_LAND);
		farmHeaders.add(ExcelImportProperties.ICS3_SURVEY_NUMBER);
		farmHeaders.add(ExcelImportProperties.ICS3_BEGIN_CONVERSATION);
		farmHeaders.add(ExcelImportProperties.ORGANIC_LAND);
		farmHeaders.add(ExcelImportProperties.ORGANIC_SURVEY_NUMBER);
		farmHeaders.add(ExcelImportProperties.ORGANIC_BEGIN_CONVERSATION);
		farmHeaders.add(ExcelImportProperties.LATITUDE);
		farmHeaders.add(ExcelImportProperties.LONGITUDE);
		farmHeaders.add(ExcelImportProperties.LAND_MARK);
		farmHeaders.add(ExcelImportProperties.TOTAL_PROD_AREA);
		farmHeaders.add(ExcelImportProperties.TOTAL_ORGANIC_AREA);
		farmHeaders.add(ExcelImportProperties.TOTAL_NON_ORGANIC_AREA);
		farmHeaders.add(ExcelImportProperties.EST_YIELD_TO_CERTI);
		/*farmHeaders.add(ExcelImportProperties.TOTAL_CULTI_AREA);		
		farmHeaders.add(ExcelImportProperties.DISTANCE_PROCESSING_UNIT);
		farmHeaders.add(ExcelImportProperties.PROCESS_ACTIVITY);
		farmHeaders.add(ExcelImportProperties.ICS_TYPE);
		farmHeaders.add(ExcelImportProperties.INSPECTION_DATE);		
		farmHeaders.add(ExcelImportProperties.INSPECTOR_NAME);
		farmHeaders.add(ExcelImportProperties.QUALIFIED);
		farmHeaders.add(ExcelImportProperties.ORGANIC_STATUS);*/


		return farmHeaders;
	}

	public List<String> formFarmCropXlsHeaders() {

		List<String> farmCropHeader = new LinkedList<>();
		farmCropHeader.add(ExcelImportProperties.CROP_FARM_CODE);
		farmCropHeader.add(ExcelImportProperties.CROP_CATEGORY);
		farmCropHeader.add(ExcelImportProperties.SEASON);
		farmCropHeader.add(ExcelImportProperties.CULTIVATION_TYPE);
		farmCropHeader.add(ExcelImportProperties.CROP_NAME);
		farmCropHeader.add(ExcelImportProperties.VARIETY);
		farmCropHeader.add(ExcelImportProperties.SOWING_DATE);
		farmCropHeader.add(ExcelImportProperties.TYPE);
		farmCropHeader.add(ExcelImportProperties.SEED_SOURCE);
		farmCropHeader.add(ExcelImportProperties.STAPLE_LEN);
		farmCropHeader.add(ExcelImportProperties.SEED_QUANTITY_USED);
		farmCropHeader.add(ExcelImportProperties.SEED_QUANTITY_COST);
		farmCropHeader.add(ExcelImportProperties.ESTIMATED_YIELD_CROPS);
		return farmCropHeader;
	}

	public File getUploadFile() {

		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {

		this.uploadFile = uploadFile;
	}

	public String getUploadFileContentType() {

		return uploadFileContentType;
	}

	public void setUploadFileContentType(String uploadFileContentType) {

		this.uploadFileContentType = uploadFileContentType;
	}

	public String getUploadFileFileName() {

		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {

		this.uploadFileFileName = uploadFileFileName;
	}

	public String populateDownloadXLS() throws IOException {

		byte[] xlData = null;
		xlData = clientService.findLogoByCode(Asset.DATAIMPORT);
		if (!ObjectUtil.isEmpty(xlData)) {
			fileInputStream = new ByteArrayInputStream(xlData);
			formattedFileName = "farmerTemplate.xls";
		}
		return STREAM;
	}

	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	public String getFormattedFileName() {

		return formattedFileName;
	}

	public void setFormattedFileName(String formattedFileName) {

		this.formattedFileName = formattedFileName;
	}

	/*
	 * public String downloadData() throws IOException { if
	 * (ObjectUtil.isLong(id)) { DownloadTask downloadTask =
	 * certificationService .findDownloadTaskById(Long.parseLong(id)); if
	 * (!ObjectUtil.isEmpty(downloadTask)) {
	 * response.setContentType("application/vnd.ms-excel");
	 * response.setHeader("Content-Disposition", "attachment;filename=" +
	 * downloadTask.getFileName());
	 * response.getOutputStream().write(downloadTask.getFileData());
	 * downloadTask.setStatus(DownloadTask.STATUS_DOWNLOADED);
	 * certificationService.updateDownloadTask(downloadTask); //
	 * certificationService.removeOldDownloads(getUsername()); } } return null;
	 * }
	 */
	public String populateNewCropCotton() {
		Integer sheetRowCount = 0;
		int i = 1;
		try {
			String filePath = request.getSession().getServletContext().getRealPath("/");
			File fileToCreate = new File(filePath, uploadFileFileName);
			if (ObjectUtil.isEmpty(fileToCreate)) {
				throw new FileNotFoundException();
			}
			FileUtils.copyFile(uploadFile, fileToCreate);

			List<String> headerValue = formFarmCropXlsHeaders();
			int colSize = headerValue.size() - 1;
			HSSFWorkbook workBook = null;
			FileInputStream fstream = new FileInputStream(fileToCreate.toString());
			workBook = new HSSFWorkbook(fstream);
			HSSFSheet hssfsheet = workBook.getSheetAt(4);
			sheetRowCount = hssfsheet.getLastRowNum();
			String nullString = null;
			Map<String, String> tempMap;
			while (i <= sheetRowCount) {
				int nullCount = 0;
				HSSFRow hssfRow = hssfsheet.getRow(i);
				String traceId = !StringUtil.isEmpty(hssfRow.getCell(0))
						? String.valueOf(hssfRow.getCell(0).getStringCellValue().trim()) : nullString;
				nullCount++;
				// String key = (String) headerValue.get(columnIndex);

				List<String> set1 = getFCSet(hssfRow, 1);
				processFarmCropCottonData(set1, traceId);

				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}
	
	private void processFarmCropCottonData(List<String> set, String traceId) {
		// java.util.Iterator<String> iterator = set.iterator();

		if (!StringUtil.isEmpty(set.get(1))) {
			String a = "JK-4";
			ProcurementVariety variety1 = productDistributionService.findProcurementVariertyByName(a);
			FarmCrops farmCrops = new FarmCrops();
			if (!StringUtil.isEmpty(set.get(8))) {
				if (set.get(8).trim().toLowerCase().startsWith("m")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
				} else if (set.get(8).trim().toLowerCase().startsWith("i")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
				}
			}
			if (!StringUtil.isEmpty(set.get(7))) {
				HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(set.get(7));
				if (!ObjectUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				}
			}
			if (!ObjectUtil.isEmpty(variety1)) {
				farmCrops.setProcurementVariety(variety1);
				farmCrops.setCultiArea(set.get(1));
			}
			farmCrops.setBranchId(getBranchId());
			farmCrops.setSeedSource(set.get(9));
			farmCrops.setSeedQtyUsed(Double.valueOf(set.get(10)));
			Farmer farmer = farmerService.findFarmerByTraceId(traceId);
			if (!ObjectUtil.isEmpty(farmer.getFarms().iterator().next())) {
				farmCrops.setFarm(farmer.getFarms().iterator().next());
				farmCropsService.addFarmCrops(farmCrops);
				// farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
			}
		}
		if (!StringUtil.isEmpty(set.get(2))) {
			String b = "JK-35";
			ProcurementVariety variety2 = productDistributionService.findProcurementVariertyByName(b);
			FarmCrops farmCrops = new FarmCrops();
			if (!StringUtil.isEmpty(set.get(8))) {
				if (set.get(8).trim().toLowerCase().startsWith("m")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
				} else if (set.get(8).trim().toLowerCase().startsWith("i")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
				}
			}
			if (!StringUtil.isEmpty(set.get(7))) {
				HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(set.get(7));
				if (!ObjectUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				}
			}
			if (!ObjectUtil.isEmpty(variety2)) {
				farmCrops.setProcurementVariety(variety2);
				farmCrops.setCultiArea(set.get(2));
			}
			farmCrops.setBranchId(getBranchId());
			farmCrops.setSeedSource(set.get(9));
			farmCrops.setSeedQtyUsed(Double.valueOf(set.get(10)));
			Farmer farmer = farmerService.findFarmerByTraceId(traceId);
			if (!ObjectUtil.isEmpty(farmer.getFarms().iterator().next())) {
				farmCrops.setFarm(farmer.getFarms().iterator().next());
				farmCropsService.addFarmCrops(farmCrops);
				// farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
			}
		}
		if (!StringUtil.isEmpty(set.get(3))) {
			String c = "Suraj";
			ProcurementVariety variety3 = productDistributionService.findProcurementVariertyByName(c);
			FarmCrops farmCrops = new FarmCrops();
			if (!StringUtil.isEmpty(set.get(8))) {
				if (set.get(8).trim().toLowerCase().startsWith("m")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
				} else if (set.get(8).trim().toLowerCase().startsWith("i")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
				}
			}
			if (!StringUtil.isEmpty(set.get(7))) {
				HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(set.get(7));
				if (!ObjectUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				}
			}
			if (!ObjectUtil.isEmpty(variety3)) {
				farmCrops.setProcurementVariety(variety3);
				farmCrops.setCultiArea(set.get(3));
			}
			farmCrops.setBranchId(getBranchId());
			farmCrops.setSeedSource(set.get(9));
			farmCrops.setSeedQtyUsed(Double.valueOf(set.get(10)));
			Farmer farmer = farmerService.findFarmerByTraceId(traceId);
			if (!ObjectUtil.isEmpty(farmer.getFarms().iterator().next())) {
				farmCrops.setFarm(farmer.getFarms().iterator().next());
				farmCropsService.addFarmCrops(farmCrops);
				// farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
			}
		}

		if (!StringUtil.isEmpty(set.get(4))) {
			String c = "Vasudha P-1";
			ProcurementVariety variety4 = productDistributionService.findProcurementVariertyByName(c);
			FarmCrops farmCrops = new FarmCrops();
			if (!StringUtil.isEmpty(set.get(8))) {
				if (set.get(8).trim().toLowerCase().startsWith("m")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
				} else if (set.get(8).trim().toLowerCase().startsWith("i")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
				}
			}
			if (!StringUtil.isEmpty(set.get(7))) {
				HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(set.get(7));
				if (!ObjectUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				}
			}
			if (!ObjectUtil.isEmpty(variety4)) {
				farmCrops.setProcurementVariety(variety4);
				farmCrops.setCultiArea(set.get(4));
			}
			farmCrops.setBranchId(getBranchId());
			farmCrops.setSeedSource(set.get(9));
			farmCrops.setSeedQtyUsed(Double.valueOf(set.get(10)));
			Farmer farmer = farmerService.findFarmerByTraceId(traceId);
			if (!ObjectUtil.isEmpty(farmer.getFarms().iterator().next())) {
				farmCrops.setFarm(farmer.getFarms().iterator().next());
				farmCropsService.addFarmCrops(farmCrops);
				// farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
			}
		}

		if (!StringUtil.isEmpty(set.get(5))) {
			String e = "Vasudha P-2";
			ProcurementVariety variety6 = productDistributionService.findProcurementVariertyByName(e);
			FarmCrops farmCrops = new FarmCrops();
			if (!StringUtil.isEmpty(set.get(8))) {
				if (set.get(8).trim().toLowerCase().startsWith("m")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
				} else if (set.get(8).trim().toLowerCase().startsWith("i")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
				}
			}
			if (!StringUtil.isEmpty(set.get(7))) {
				HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(set.get(7));
				if (!ObjectUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				}
			}
			if (!ObjectUtil.isEmpty(variety6)) {
				farmCrops.setProcurementVariety(variety6);
				farmCrops.setCultiArea(set.get(5));
			}
			farmCrops.setBranchId(getBranchId());
			farmCrops.setSeedSource(set.get(9));
			farmCrops.setSeedQtyUsed(Double.valueOf(set.get(10)));
			Farmer farmer = farmerService.findFarmerByTraceId(traceId);
			if (!ObjectUtil.isEmpty(farmer.getFarms().iterator().next())) {
				farmCrops.setFarm(farmer.getFarms().iterator().next());
				farmCropsService.addFarmCrops(farmCrops);
				// farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
			}
		}
		if (!StringUtil.isEmpty(set.get(6))) {
			String f = "NH-615";
			ProcurementVariety variety7 = productDistributionService.findProcurementVariertyByName(f);
			FarmCrops farmCrops = new FarmCrops();
			if (!StringUtil.isEmpty(set.get(8))) {
				if (set.get(8).trim().toLowerCase().startsWith("m")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.MAINCROP.ordinal());
				} else if (set.get(8).trim().toLowerCase().startsWith("i")) {
					farmCrops.setCropCategory(FarmCrops.CROPTYPE.INTERCROP.ordinal());
				}
			}
			if (!StringUtil.isEmpty(set.get(7))) {
				HarvestSeason harvestSeason = farmerService.findHarvestSeasosnByName(set.get(7));
				if (!ObjectUtil.isEmpty(harvestSeason)) {
					farmCrops.setCropSeason(harvestSeason);
				}
			}
			if (!ObjectUtil.isEmpty(variety7)) {
				farmCrops.setProcurementVariety(variety7);
				farmCrops.setCultiArea(set.get(6));
			}
			farmCrops.setBranchId(getBranchId());
			farmCrops.setSeedSource(set.get(9));
			farmCrops.setSeedQtyUsed(Double.valueOf(set.get(10)));
			Farmer farmer = farmerService.findFarmerByTraceId(traceId);
			if (!ObjectUtil.isEmpty(farmer.getFarms().iterator().next())) {
				farmCrops.setFarm(farmer.getFarms().iterator().next());
				farmCropsService.addFarmCrops(farmCrops);
				// farmCrops.setFarm(farmerService.findFarmByfarmId(farm.getId()));
			}
		}

		/*
		 * Set<Farm> farm=farmer.getFarms(); for(Farm f : farm){ set<Farm }
		 */
		/*
		 * Farm farm =farmer.getFarms().iterator().next(); Set<FarmCrops>
		 * frmCrops=new HashSet<>(); frmCrops.add(farmCrops);
		 * farm.setFarmCrops(frmCrops);
		 */

	}
	
	private List<String> getFCSet(HSSFRow hssfRow, int startCol) {
		List<String> fcList = new ArrayList<>();
		int endCol = startCol + 10;
		for (int i = startCol; i <= endCol; i++) {
			HSSFCell hssfCell = null;
			hssfCell = hssfRow.getCell(i);
			if (hssfCell == null) {
				fcList.add(null);
			} else {
				fcList.add(hssfCell.toString());
			}
		}
		return fcList;
	}
}
