/*
 * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.service.DynamicReportProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("unchecked")
public class ProcurementReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private static final String NO_OF_BAGS = "bags";
	private static final String GROSS_WEIGHT = "grossWt";
	private static final String NET_WEIGHT = "netWt";
	private static final String DRY_LOSS = "dryLoss";
	private static final String GRADING_LOSS = "gradingLoss";
	private static final String PRODUCT_ID = "productId";
	private static final String GRADE_ID = "gradeId";
	private String mainGridCols;
	private static String subGridCols;
	private static DynamicReportConfig dynamicReportConfig;
	private static DynamicReportConfig subDynamicReportConfig;
	private Object fValue;
	private Object mValue;
	private List<Object[]> seasonList;
	private List<Object[]> farmList;
	private List<Object[]> agentList;
	private List<Object[]> farmerList;
	private List<Object[]> productInfoList;
	private String gridIdentity;
	private Map<String, String> warehouseMap = new HashMap<>();
	private String filterList;
	private String seasonCode;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private Procurement filter;
	private Map<String, String> fpoMap = new HashMap<>();
	private Map<String, String> farmerMap = new HashMap<>();
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	protected List<String> fields = new ArrayList<String>();
	private String supplierEnabled;
	int serialNo = 1;
	private String daterange;
	private String tenantId;
	private String enableTraceability;
	private String villageName;
	private String farmerId;
	private String name;
	private String selectedProcId;
	private Map<String, GradeMaster> gradeMasterMap;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String headerFields;
	private String isKpfEnabled;
	private String branch;
	
	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		setEnableTraceability(preferncesService.findPrefernceByName(ESESystem.ENABLE_TRACEABILITY));
		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));
		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setIsKpfEnabled(preferences.getPreferences().get(ESESystem.IS_KPF_BASED));
		
		/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)) */
		if(isKpfEnabled=="1"){
			filter = new Procurement();
			setTenantId(getCurrentTenantId());
			setSupplierEnabled(preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER));

			return LIST;

		} else {
			formMainGridCols();
			if (!ObjectUtil.isEmpty(dynamicReportConfig)
					&& !ObjectUtil.isListEmpty(dynamicReportConfig.getDynmaicReportConfigFilters())) {
				setFilterSize(String.valueOf(dynamicReportConfig.getDynmaicReportConfigFilters().size()));

				dynamicReportConfig.getDynmaicReportConfigFilters().stream().forEach(reportConfigFilter -> {
					Map<String, String> optionMap = (Map<String, String>) getMethodValue(reportConfigFilter.getMethod(),
							null);
					reportConfigFilter.setOptions(optionMap);
					reportConfigFilters.add(reportConfigFilter);
				});
			}
			return LIST;
		}
	}

	private void formMainGridCols() {
		dynamicReportConfig = clientService.findReportByName(DynamicReportProperties.PROCUREMENT_REPORT);
		if (!ObjectUtil.isEmpty(dynamicReportConfig)) {
			mainGridCols = "";
			if (StringUtil.isEmpty(getBranchId())) {
				mainGridCols = getLocaleProperty("branchId") + "#" + "150%";
			}
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if (dynamicReportConfigDetail.getLabelName().contains("@")) {
							String label = dynamicReportConfigDetail.getLabelName();
							mainGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#" + dynamicReportConfigDetail.getAlignment()+ "%";
						} else {
							mainGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
									+ dynamicReportConfigDetail.getWidth() + "#" + dynamicReportConfigDetail.getAlignment()+ "%";
						}

					});
		}

		subDynamicReportConfig = clientService.findReportByName(DynamicReportProperties.PROCUREMENT_REPORT_SUB);
		if (!ObjectUtil.isEmpty(subDynamicReportConfig)) {
			subGridCols = "";
			subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsGridAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						if (dynamicReportConfigDetail.getLabelName().contains("@")) {
							String label = dynamicReportConfigDetail.getLabelName();
							subGridCols += getLocaleProperty(label) + " (" + getCurrencyType() + ")" + "#"
									+ dynamicReportConfigDetail.getWidth() + "#" + dynamicReportConfigDetail.getAlignment()+ "%";
						} else {
							subGridCols += getLocaleProperty(dynamicReportConfigDetail.getLabelName()) + "#"
									+ dynamicReportConfigDetail.getWidth() + "#" + dynamicReportConfigDetail.getAlignment()+ "%";
						}
					});
		}
	}

	public String subGridDetail() throws Exception {
		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setIsKpfEnabled(preferences.getPreferences().get(ESESystem.IS_KPF_BASED));
		
		
		/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))*/ 
		if(isKpfEnabled=="1"){
			ProcurementDetail procurementDetail = new ProcurementDetail();
			Procurement procurement = new Procurement();
			procurement.setId(Long.valueOf(id));
			procurementDetail.setProcurement(procurement);
			if (getPage() > 1) {
				serialNo = (getPage() - 1) * 10 + 1;
			}
			super.filter = procurementDetail;
			Map data = readData();
			return sendJSONResponse(data);
		} else {
			ProcurementDetail procurementDetail = new ProcurementDetail();
			Procurement procurement = new Procurement();
			procurement.setId(Long.valueOf(id));
			procurementDetail.setProcurement(procurement);
			super.filter = procurementDetail;
			Map data = null;
			if (!ObjectUtil.isEmpty(subDynamicReportConfig) && subDynamicReportConfig.getFetchType() == 2L) {
				data = readProjectionData(subDynamicReportConfig.getDynmaicReportConfigDetails());
			} else {
				data = readData();
			}
			setGridIdentity(IReportDAO.SUB_GRID);
			return sendJSONResponse(data);
		}
	}

	public String detail() throws Exception {
		Procurement procurement = new Procurement();
		/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))*/
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setIsKpfEnabled(preferences.getPreferences().get(ESESystem.IS_KPF_BASED));
		
		if(isKpfEnabled=="1"){
			setFilter(new Procurement());
			filter.setAgroTransaction(new AgroTransaction());
			if (!StringUtil.isEmpty(seasonCode)) {

				filter.setSeasonCode(seasonCode);
			}

			if (!StringUtil.isEmpty(farmerId)) {

				filter.getAgroTransaction().setFarmerId(farmerId);
			}
			if (!StringUtil.isEmpty(villageName)) {
				filter.setVillage(locationService.findVillageByName(villageName.trim()));
			}
			super.filter = this.filter;
			Map data = readData();
			return sendJSONResponse(data);
		} else {
			if (!StringUtil.isEmpty(filterList)) {
				try {
					Type listType1 = new TypeToken<List<FilterFieldData>>() {
					}.getType();
					List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
					procurement.setFilterData(filtersList.stream()
							.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.filter = procurement;

			Map data = null;
			if (!ObjectUtil.isEmpty(dynamicReportConfig) && dynamicReportConfig.getFetchType() == 2L) {
				data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
			} else {
				data = readData();
			}
			setGridIdentity(IReportDAO.MAIN_GRID);
			return sendJSONResponse(data);
		}
	}

	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID))*/
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setIsKpfEnabled(preferences.getPreferences().get(ESESystem.IS_KPF_BASED));

			if(isKpfEnabled=="1")
			{
			if (obj instanceof Procurement) {
				Procurement procurement = (Procurement) obj;
				JSONArray rows = new JSONArray();

				rows.add(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						? (!StringUtil.isEmpty(procurement.getAgroTransaction().getTxnTime())
								? DateUtil.getDateByDateTime(procurement.getAgroTransaction().getTxnTime()) : "")
						: "");

				rows.add(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						? (!StringUtil.isEmpty(procurement.getAgroTransaction().getTxnTime())
								? DateUtil.getMonthByDateTime(procurement.getAgroTransaction().getTxnTime()) : "")
						: "");

				rows.add(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						? (!StringUtil.isEmpty(procurement.getAgroTransaction().getTxnTime())
								? DateUtil.getYearByDateTime(procurement.getAgroTransaction().getTxnTime()) : "")
						: "");

				rows.add(!ObjectUtil.isEmpty(procurement.getInvoiceNo())
						? (!StringUtil.isEmpty(procurement.getInvoiceNo()) ? procurement.getInvoiceNo() : "") : "");

				if (!StringUtil.isEmpty(procurement.getWarehouseId())
						&& StringUtil.isLong(procurement.getWarehouseId())) {
					rows.add(getWarehouseMap().containsKey(procurement.getWarehouseId())
							? getWarehouseMap().get(procurement.getWarehouseId()) : "");

				} else {
					rows.add("");
				}

				if (!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						&& !StringUtil.isEmpty(procurement.getAgroTransaction().getAgentId())) {
					rows.add(procurement.getAgroTransaction().getAgentName());
				} else {
					rows.add("");
				}

				rows.add(!StringUtil.isEmpty(procurement.getCropType())
						? getLocaleProperty("cpType" + procurement.getCropType()) : "");

				if (preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER).equals("1")) {
					if (!StringUtil.isEmpty(procurement.getProcMasterType())) {
						if (procurement.getProcMasterType().equals("99")
								|| procurement.getProcMasterType().equals("11")) {
							if (procurement.getProcMasterType().equals("99")) {
								if (ObjectUtil.isEmpty(procurement.getFarmer())) {
									rows.add(getText("sup99"));
								} else {
									rows.add(
											!ObjectUtil.isEmpty(procurement.getFarmer())
													? (!ObjectUtil.isEmpty(procurement.getFarmer().getSamithi())
															? (procurement.getFarmer().getSamithi()
																	.getGroupType() != null
																			? getText(
																					"grpType" + procurement.getFarmer()
																							.getSamithi()
																							.getGroupType())
																			: "")
															: "")
													: "");
								}
							} else if (procurement.getProcMasterType().equals("11")) {

								rows.add(!ObjectUtil.isEmpty(procurement.getFarmer())
										? (!ObjectUtil.isEmpty(procurement.getFarmer().getSamithi())
												? (procurement.getFarmer().getSamithi().getGroupType() != null
														? getText("grpType"
																+ procurement.getFarmer().getSamithi().getGroupType())
														: "")
												: "")
										: "");
							} else {

								rows.add(getText("NA"));
							}
							/* rows.add("NA"); */
							if (!ObjectUtil.isEmpty(procurement.getFarmer())
									&& !ObjectUtil.isEmpty(procurement.getFarmer().getSamithi())) {
								rows.add(!StringUtil.isEmpty(procurement.getFarmer().getSamithi().getName())
										? procurement.getFarmer().getSamithi().getName() : "");
							} else {
								rows.add("NA");
							}
							rows.add(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
									? (!StringUtil.isEmpty(procurement.getAgroTransaction().getFarmerName())
											? procurement.getAgroTransaction().getFarmerName() : "")
									: "");
						} else {
							rows.add(getText("sup" + procurement.getProcMasterType()));
							if (!ObjectUtil.isEmpty(procurement.getFarmer())) {
								rows.add(getFpoMap().get(procurement.getProcMasterTypeId()));
								rows.add(procurement.getFarmer().getFirstName());
							} else {
								if (!StringUtil.isEmpty(procurement.getProcMasterTypeId())) {
									MasterData mData = farmerService
											.findMasterDataIdById(Long.valueOf(procurement.getProcMasterTypeId()));
									rows.add(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
									rows.add("NA");

								} else {
									rows.add("NA");
									rows.add("NA");
								}

							}
						}
					} else {
						rows.add("NA");
						rows.add("NA");
					}
				}

				rows.add(!ObjectUtil.isEmpty(procurement.getVillage()) ? procurement.getVillage().getName() : "");

				Map<String, Object> details = getTotalProductDetails(procurement);
				DecimalFormat df1 = new DecimalFormat("0.00");		
				rows.add(df1.format(details.get(NET_WEIGHT)));

				rows.add(!ObjectUtil.isEmpty(procurement.getTotalProVal())
						? CurrencyUtil.thousandSeparator(procurement.getTotalProVal()) : "0.00");

				if ((!StringUtil.isEmpty(procurement.getLatitude())
						&& !StringUtil.isEmpty(procurement.getLongitude()))) {
					rows.add("<button class='faMap' title='" + getText("farm.map.available.title")
							+ "' onclick='showFarmMap(\""
							+ (!StringUtil.isEmpty(procurement.getLatitude()) ? procurement.getLatitude() : "0")
							+ "\",\""
							+ (!StringUtil.isEmpty(procurement.getLongitude()) ? procurement.getLongitude() : "0")
							+ "\")'></button>");
				} else {
					// No Latlon
					rows.add("<button class='no-latLonIcn' title='" + getText("farm.map.unavailable.title")
							+ "'></button>");
				}

				jsonObject.put("id", procurement.getId());
				jsonObject.put("cell", rows);
			} else if (obj instanceof ProcurementDetail) {

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)) {
					ProcurementDetail procurementDetail = (ProcurementDetail) obj;

					JSONArray rows = new JSONArray();

					rows.add(serialNo);
					rows.add(!ObjectUtil.isEmpty(
							procurementDetail.getProcurementGrade().getProcurementVariety().getProcurementProduct())
									? procurementDetail.getProcurementGrade().getProcurementVariety()
											.getProcurementProduct().getName()
									: "");

					String quality = procurementDetail.getProcurementGrade().getName();
					if (getGradeMasterMap().containsKey(quality)) {
						quality = getGradeMasterMap().get(quality).getName();
					}

					rows.add(quality);
					if (!StringUtil.isEmpty(procurementDetail.getUnit())) {
						FarmCatalogue cat = catalogueService.findCatalogueByCode(procurementDetail.getUnit());
						rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");
					} else {
						rows.add("");
					}
					Double price = !ObjectUtil.isEmpty(procurementDetail.getRatePerUnit())
							? procurementDetail.getRatePerUnit() : 0D;

					rows.add(CurrencyUtil.getDecimalFormat(price, "##.000"));

					rows.add(CurrencyUtil.getDecimalFormat(procurementDetail.getGrossWeight(), "##.000"));

					Double total = price * procurementDetail.getGrossWeight();
					
			

					jsonObject.put("id", procurementDetail.getId());
					jsonObject.put("cell", rows);
					serialNo++;
				}

			}
		} else {
			JSONArray rows = new JSONArray();
			AtomicInteger runCount = new AtomicInteger(1);
			String id = "";
			if (obj instanceof Object[]) {
				Object[] arr = (Object[]) obj;
				id = String.valueOf(arr[0]);
				if (getGridIdentity().equalsIgnoreCase(IReportDAO.MAIN_GRID)) {
					if (!StringUtil.isEmpty(dynamicReportConfig)) {
						if (StringUtil.isEmpty(getBranchId())) {
							dynamicReportConfig.getDynmaicReportConfigDetails().stream()
									.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).limit(1)
									.forEach(dynamicReportConfigDetail -> {
										fValue = ReflectUtil.getObjectFieldValue(arr,
												String.valueOf(runCount.getAndIncrement()));

										rows.add(getBranchesMap().get(fValue));
									});
						} else {
							runCount.getAndIncrement();
						}
						
						dynamicReportConfig.getDynmaicReportConfigDetails().stream()
								.filter(config -> config.getIsGridAvailabiltiy())
								.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
								.forEach(dynamicReportConfigDetail -> {
									if (dynamicReportConfigDetail.getAccessType() == 1L) {
										fValue = ReflectUtil.getObjectFieldValue(arr,
												String.valueOf(runCount.getAndIncrement()));
										if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
											rows.add(fValue.toString());
										} else {
											rows.add("");
										}
									} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
										fValue = ReflectUtil.getObjectFieldValue(arr,
												String.valueOf(runCount.getAndIncrement()));
										if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
											mValue = getMethodValue(dynamicReportConfigDetail.getMethod(),
													fValue.toString());
											if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
												rows.add(mValue.toString());
											} else {
												rows.add("");
											}
										} else {
											rows.add("");
										}
									} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
										fValue = ReflectUtil.getObjectFieldValue(arr,
												dynamicReportConfigDetail.getParameters());
										if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
											mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue);
											if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
												rows.add(mValue.toString());
											} else {
												rows.add("");
											}
										} else {
											rows.add("");
										}
									}
								});
					}
				} else if (!StringUtil.isEmpty(dynamicReportConfig)) {
					subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
							.filter(config -> config.getIsGridAvailabiltiy())
							.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
							.forEach(dynamicReportConfigDetail -> {
								if (dynamicReportConfigDetail.getAccessType() == 1L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										rows.add(fValue.toString());
									} else {
										rows.add("");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										mValue = getMethodValue(dynamicReportConfigDetail.getMethod(),
												fValue.toString());
										if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
											rows.add(mValue.toString());
										} else {
											rows.add("");
										}
									} else {
										rows.add("");
									}
								} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										rows.add(fValue.toString());
									} else {
										rows.add("");
									}
								}
								else if (dynamicReportConfigDetail.getAccessType() == 14L) {
									fValue = ReflectUtil.getObjectFieldValue(arr,
											String.valueOf(runCount.getAndIncrement()));
									if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
										rows.add("<button class='btn btn-sts' onclick='enablePrinter(" + fValue.toString()
										+ ")'><i class='fa fa-print' aria-hidden='true'></i></button>");
										//rows.add(fValue.toString());
									} else {
										rows.add("");
									}
								}
							});

				}
				jsonObject.put("id", id);
				jsonObject.put("cell", rows);

			}

		}
		return jsonObject;
	}

	public void populatePrintz() {
		try {
			String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
					: request.getSession().getId();
			String makeDir = FileUtil.storeXls(request.getSession().getId());
			String fileName = getCurrentTenantId() + "-" + DateUtil.getRevisionNoDateTimeMilliSec() + ".pdf";

			File file = new File(makeDir + fileName);
			FileWriter outputFileReader = new FileWriter(file);
			PrintWriter outputStream = new PrintWriter(outputFileReader);
			outputStream.println();

			String inLine = null;
	/*		SupplierProcurementDetail supplierProcurementDetail = productDistributionService
					.findSupplierProcurementDetailById(Long.valueOf(getSelectedProcId()));*/
			
			ProcurementDetail procurementDetail= productDistributionService .findByProcurementDetailId(Long.valueOf(getSelectedProcId()));
					
					

			StringBuilder sb = new StringBuilder();

			sb = new StringBuilder();
			
			if (procurementDetail.getProcurement().getProcMasterType().equals("99")
					|| procurementDetail.getProcurement().getProcMasterType().equalsIgnoreCase("11") || procurementDetail.getProcurement().getProcMasterType().equalsIgnoreCase("-1")) {
				if (!ObjectUtil.isEmpty(procurementDetail.getProcurement().getFarmer())) {
				
					Farm farm = farmerService.findFarmByFarmerId(procurementDetail.getProcurement().getFarmer().getId());
					String url = request.getRequestURL().toString();
					 URL aURL = new URL(url);
					 String path=aURL.getPath();
					 String fullPath[]= path.split("/", 0);			
					 String urll=aURL.getProtocol()+"://"+aURL.getAuthority()+"/"+fullPath[1];		 
					 String tenant=getCurrentTenantId();					
					String message =urll+"/getTraceDetails/traceDetails.html"+"?traceDetails=%"+farm.getFarmCode();
					sb.append(message);
					/*sb.append(System.getProperty("line.separator"));
					sb.append("Trace your Produce");*/
				}
			} else {
				sb.append(procurementDetail.getProcurementGrade().getName() + " in ");
				sb.append(System.getProperty("line.separator"));
				sb.append(procurementDetail.getProcurementGrade().getProcurementVariety().getName());
				sb.append(System.getProperty("line.separator"));
				sb.append("is procured from ");
				sb.append(System.getProperty("line.separator"));
			//	if (procurementDetail.getProcurement().getIsRegSupplier() == 0) {

					if (!StringUtil.isEmpty(procurementDetail.getProcurement().getProcMasterTypeId())) {
						MasterData mData = farmerService.findMasterDataIdByCode(
								procurementDetail.getProcurement().getProcMasterTypeId());
						if (!ObjectUtil.isEmpty(mData)) {
							sb.append(getLocaleProperty(
									"sup" + procurementDetail.getProcurement().getProcMasterType()));
							sb.append(System.getProperty("line.separator"));
							sb.append(mData.getName());
							sb.append(System.getProperty("line.separator"));
							sb.append("Krishi Pragati Foundation Centre");
							sb.append(System.getProperty("line.separator"));
							sb.append("Otur, Pune, Maharashtra");
							sb.append(System.getProperty("line.separator"));
							sb.append("Standard as per NPOP");
							sb.append(System.getProperty("line.separator"));
							sb.append("Visit us : www.go4fresh.in");
						}

					}
				}

		//	}

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			
			com.itextpdf.text.Image logo =com.itextpdf.text.Image.getInstance(exportQRLogo());
			logo.scaleAbsolute(150,40); logo.setAlignment(Image.LEFT);
			document.add(logo);
			
			ESESystem preferences = null;
			if (!StringUtil.isEmpty(getBranchId())) {
				preferences = preferncesService.findPrefernceByOrganisationId(getBranchId());
			} else {
				preferences = preferncesService.findPrefernceById("1");
			}

			Font bold = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD);

			Paragraph addressLine1 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE1), bold);
			addressLine1.setAlignment(Element.ALIGN_LEFT);
			
			document.add(addressLine1);

			Paragraph addressLine2 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE2), bold);
			addressLine2.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine2);

			Paragraph addressLine3 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE3), bold);
			addressLine3.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine3);

			Paragraph addressLine4 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE4), bold);
			addressLine4.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine4);

			Paragraph addressLine5 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE5), bold);
			addressLine5.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine5);

			Paragraph addressLine6 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE6), bold);
			addressLine6.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine6);

			Paragraph addressLine7 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE7), bold);
			addressLine7.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine7);

			Paragraph addressLine8 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE8), bold);
			addressLine8.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine8);

			Paragraph addressLine9 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE9), bold);
			addressLine9.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine9);

			Paragraph addressLine10 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE10), bold);
			addressLine10.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine10);

			Paragraph addressLine11 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE11), bold);
			addressLine11.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine11);

			Paragraph addressLine12 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE12), bold);
			addressLine12.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine12);

			Paragraph addressLine13 = new Paragraph(preferences.getPreferences().get(ESESystem.ADDRESS_LINE13), bold);
			addressLine13.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(addressLine13);

			BarcodeQRCode barcodeQRCode = new BarcodeQRCode(sb.toString(), 1000, 1000, null);
			Image codeQrImage = barcodeQRCode.getImage();
			codeQrImage.setAlignment(Image.LEFT);
			codeQrImage.scaleAbsolute(150, 150);
			document.add(codeQrImage);
			document.add(addressLine1);
			document.close();
			

			response.setContentType("application/pdf");
			OutputStream out = response.getOutputStream();
			out.write(IOUtils.toByteArray(new FileInputStream(file)));
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private Object getMethodValue(String methodName, Object param) {
		Object field = null;
		try {
			@SuppressWarnings("rawtypes")
			Class cls = this.getClass();

			if (param != null) {
				if (param instanceof Object[]) {
					Method setNameMethod = this.getClass().getMethod(methodName, Object[].class);
					field = setNameMethod.invoke(this, param);
				} else {
					Method setNameMethod = this.getClass().getMethod(methodName, String.class);
					field = setNameMethod.invoke(this, param);
				}
			} else {
				Method setNameMethod = this.getClass().getMethod(methodName);
				field = setNameMethod.invoke(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}

	public Map<String, String> getSeasonsList() {
		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(seasonList)) {
			seasonList = farmerService.listSeasonCodeAndName();
		}
		for (Object[] obj : seasonList) {
			
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;
	}
	public Map<String, String> getProcurementSeasonsList() {
		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(seasonList)) {
			seasonList = farmerService.listSeasonCodeAndName();
		}
		for (Object[] obj : seasonList) {
			
			seasonMap.put(String.valueOf(obj[1]), String.valueOf(obj[1]));
		}
		return seasonMap;
	}
	public Map<String, String> getUserList() {

		Map<String, String> userMap = new LinkedHashMap<String, String>();
		List<Object[]> agentList = agentService.listMobileUser();
		for (Object[] inspection : agentList) {
				if(!ObjectUtil.isEmpty(inspection)){
			userMap.put(inspection[0].toString(),inspection[1].toString());
				}
				
		}

		return userMap;

	}
	public Map<String, String> getFarmersList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByProcurement();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));

		}
		return farmerMap;
	}

	public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}

	public Map<String, String> getFarmsList() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmList)) {
			farmList = farmerService.listFarmInfo();
		}
		farmList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[2]));
		});

		return farmMap;
	}
	
	public Map<String, String> getFarmsListByFarmId() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmList)) {
			farmList = farmerService.listFarmDetailsInfo();
		}
		farmList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[2]));
		});

		return farmMap;
	}

	public Map<String, String> getProcurementProductList() {
		Map<String, String> productMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			productMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[8]));
		});
		return productMap;
	}

	public Map<String, String> getProcurementProductUnitList() {
		Map<String, String> productMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			productMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[9]));
		});
		return productMap;
	}

	public Map<String, String> getProcurementVarietyList() {
		Map<String, String> varietyMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			varietyMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[5]));
		});
		return varietyMap;
	}

	public Map<String, String> getProcurementGradeList() {
		Map<String, String> gradeMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(productInfoList)) {
			productInfoList = productService.listProcurementGradeInfo();
		}
		productInfoList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			gradeMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[2]));
		});
		return gradeMap;
	}

	public Map<String, String> getAgentsList() {
		Map<String, String> agentMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(agentList)) {
			agentList = agentService.listAgentIdName();
		}
		agentList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			agentMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[1]));
		});
		return agentMap;
	}
	
	
	public Map<String, String> getCustomerList() {

		Map<String, String> customerMap = new LinkedHashMap<>();
		List<Customer> customerList = clientService.listOfCustomers();
		for (Customer obj : customerList) {
			if(!ObjectUtil.isEmpty(obj)){
				customerMap.put(String.valueOf(obj.getId()),obj.getCustomerName());
			}
		}
		return customerMap;
	}

	public String getBuyer(String customerId){
		String type = getCustomerList().get(customerId);
		return type;
	}

	public String getSeasonByCode(String code) {
		String season = getSeasonsList().get(code);
		return season;
	}
	public String getProcurementSeasonByCode(String code) {
		String season = getProcurementSeasonsList().get(code);
		return season;
	}
	public String getFarmerRegUnReg(Object[] code) {

		for (Object obj : code) {
			if (obj != null) {
				return obj.toString();
			}

		}
		return "";
	}

	public String getFarmByCode(String id) {
		String farm = getFarmsList().get(id);
		return farm;
	}
	public String getFarmByFarmId(String id) {
		String farm = getFarmsListByFarmId().get(id);
		return farm;
	}

	public String getFarmerById(String id) {
		String farmer = getFarmersList().get(id);
		return farmer;
	}

	public String getAgentByProfile(String profileId) {
		String agent = getAgentsList().get(profileId);
		return agent;
	}
	public String getStatus(String s){
		if(s.equals("1"))
			return "Approved";
		else
			return "Not yet Approved";
	}
	public String getCatlogueValueByCodeArray(String code) {
		if (ObjectUtil.isEmpty(getFarmCatalogueList()) && ObjectUtil.isListEmpty(getFarmCatalogueList())) {
			setFarmCatalogueList(new ArrayList<>());
			setFarmCatalogueList(catalogueService.listCatalogues());
		}
		name = "";
		if (code.contains(",")) {
			Arrays.asList(code.split(",")).stream().forEach(u -> {
				FarmCatalogue catValue = getFarmCatalogueList().stream()
						.filter(fc -> fc.getCode().equalsIgnoreCase(u.trim())).findAny().orElseGet(() -> {
							FarmCatalogue tmp = new FarmCatalogue();
							tmp.setName("");
							tmp.setDispName("");
							return tmp;
						});
				String tmName = getLanguagePref(getLoggedInUserLanguage(), catValue.getCode());
				name += tmName == null || StringUtil.isEmpty(tmName) ? catValue.getName()+"," : tmName;

			});
			name = StringUtil.removeLastComma(name);
		} else {
			FarmCatalogue catValue = getFarmCatalogueList().stream().filter(fc -> fc.getCode().equalsIgnoreCase(code))
					.findAny().orElseGet(() -> {
						FarmCatalogue tmp = new FarmCatalogue();
						tmp.setName("");
						tmp.setDispName("");
						return tmp;
					});

			String tmName = getLanguagePref(getLoggedInUserLanguage(), catValue.getCode());
			name = tmName == null || StringUtil.isEmpty(tmName) ? catValue.getName() : tmName;
			name = StringUtil.removeLastComma(name);
		}

		return name;
	}
	 
	public Map<String, String> getRegType() {
		Map<String, String> regType = new LinkedHashMap<>();
		regType.put("0", getText("REG"));
		regType.put("1", getText("UNREG"));
		
		return regType;
	}
	public String getFarmerType(String id){
		String type = getRegType().get(id);
		return type;
	}

	public String getProcurementGradeById(String id) {
		String grade = getProcurementGradeList().get(id);
		return grade;
	}

	public String getProcurementVarietyById(String id) {
		String variety = getProcurementVarietyList().get(id);
		return variety;
	}

	public String getProcurementProductById(String id) {
		String product = getProcurementProductList().get(id);
		return product;
	}

	public String getProcurementProductUnitById(String id) {
		String product = getProcurementProductUnitList().get(id);
		return product;
	}

	public String populateXLS() throws Exception {

		/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)) */
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setIsKpfEnabled(preferences.getPreferences().get(ESESystem.IS_KPF_BASED));

		if(isKpfEnabled=="1")
		{
			InputStream is = getKpfExportDataStream(IExporter.EXPORT_MANUAL);
			setXlsFileName(getText("ProcurementReportList") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(
					FileUtil.createFileInputStreamToZipFile(getText("procurementReportList"), fileMap, ".xls"));

		} else {

			InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
			setXlsFileName(getText("procurementReportList") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(
					FileUtil.createFileInputStreamToZipFile(getText("procurementReportList"), fileMap, ".xls"));

		}
		return "xls";
	}

	XSSFRow row, filterRowTitle, filterRow1, filterRow2, filterRow3,filterRow4;
	XSSFRow titleRow;
	int colCount, rowCount, titleRow1 = 4, titleRow2 = 6;
	Cell cell;
	Integer cellIndex;

	int serialNumber=1;
	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		InputStream fileInputStream;
		Procurement procurements = new Procurement();
		if (!StringUtil.isEmpty(filterList)) {
			try {
				Type listType1 = new TypeToken<List<FilterFieldData>>() {
				}.getType();
				List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
				procurements.setFilterData(filtersList.stream()
						.collect(Collectors.toMap(FilterFieldData::getName, obj -> obj.getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.filter = procurements;

		Map data = null;
		if (dynamicReportConfig.getFetchType() == 2L) {
			data = readProjectionData(dynamicReportConfig.getDynmaicReportConfigDetails());
		} else {
			data = readData();
		}

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(getLocaleProperty("ProcurementExportProcurementTitle"));

		/** Defining Styles */
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		//headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	/*	headerStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle filterStyle = workbook.createCellStyle();
		/*filterStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		filterStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		filterStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		filterStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		filterStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		filterStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		filterStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		filterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		filterStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		XSSFCellStyle headerLabelStyle = workbook.createCellStyle();
		/*headerLabelStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerLabelStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerLabelStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		headerLabelStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		headerLabelStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	//	headerLabelStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

		XSSFColor subGridColor = new XSSFColor(new Color(204,255,204));
		filterStyle.setFillForegroundColor(subGridColor);
		filterStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		XSSFCellStyle subGridHeader = workbook.createCellStyle();
		subGridHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		/*subGridHeader.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setBorderRight(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		subGridHeader.setBorderTop(XSSFCellStyle.BORDER_THIN);
		subGridHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		subGridHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		XSSFColor myColor = new XSSFColor(new Color(219, 218, 218));
		subGridHeader.setFillForegroundColor(myColor);

		XSSFCellStyle rows = workbook.createCellStyle();
		/*rows.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		rows.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		rows.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		rows.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderRight(XSSFCellStyle.BORDER_THIN);
		rows.setRightBorderColor(IndexedColors.BLACK.getIndex());
		rows.setBorderTop(XSSFCellStyle.BORDER_THIN);
		rows.setTopBorderColor(IndexedColors.BLACK.getIndex());*/

		/** Defining Fonts */
		XSSFFont font1 = workbook.createFont();
		font1.setFontHeightInPoints((short) 22);

		XSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 12);

		XSSFFont font3 = workbook.createFont();
		font3.setFontHeightInPoints((short) 10);
		
		XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();

		int imgRow1 = 0;
		int imgRow2 = 6;
		int imgCol1 = 0;
		int imgCol2 = 0;

		int titleRow1 = 2;
		int titleRow2 = 5;
		int count=0;
		rowCount = 2;
		colCount = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, titleRow1, titleRow2));
		sheet.setDefaultColumnWidth(13);
		
		titleRow = sheet.createRow(rowCount++);
		titleRow.setHeight((short) 500);

		cell = titleRow.createCell(2);
		cell.setCellValue(getLocaleProperty("ProcurementExportProcurementTitle"));
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font1);
		cell.setCellStyle(headerStyle);

		rowCount = 7 ;
		/** Setting Filer Fileds */

		if (!ObjectUtil.isEmpty(procurements.getFilterData()) && procurements.getFilterData().size() > 0) {
			// sheet.addMergedRegion(new CellRangeAddress(rowCount + 2, rowCount
			// + procurements.getFilterData().size() , titleRow1, titleRow2));
			rowCount++;
			if (!StringUtil.isEmpty(filterList)) {
				try {
					Type listType1 = new TypeToken<List<FilterFieldData>>() {
					}.getType();
					List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);

					row = sheet.createRow(rowCount++);
					cell = row.createCell(1);
					cell.setCellValue(getLocaleProperty("filter") + ":");
					cell.setCellStyle(filterStyle);

					filtersList.stream().forEach(filterFieldData -> {
						row = sheet.createRow(rowCount++);

						cell = row.createCell(1);
						cell.setCellValue(filterFieldData.getLabel());
						cell.setCellStyle(rows);

						cell = row.createCell(2);
						cell.setCellValue(filterFieldData.getValue());
						cell.setCellStyle(rows);

					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(ObjectUtil.isEmpty(procurements.getFilterData()) && procurements.getFilterData().size() <= 0){
			rowCount++;
		rowCount++;
		}
		filterRow4 = sheet.createRow(rowCount++);
		String[] headerFieldsArr=headerFields.split("###");
		  for(int i=0;i<headerFieldsArr.length;i++)
		  {
			  cell = filterRow4.createCell(count);
				cell.setCellValue(String.valueOf(headerFieldsArr[i]));
				cell.setCellStyle(headerLabelStyle);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				headerLabelStyle.setFont(font2);
				sheet.setColumnWidth(count, (15 * 550));
				count++;
			  
		  }
		

		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);

		colCount = 0;
		cell = row.createCell(colCount++);
		cell.setCellValue("S.No");
		cell.setCellStyle(filterStyle);
		font2.setBoldweight((short) 5);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		filterStyle.setFont(font2);
		cell.setCellStyle(filterStyle);
		sheet.setColumnWidth(colCount, (15 * 550));
		if (StringUtil.isEmpty(getBranchId())) {
			cell = row.createCell(colCount++);
			cell.setCellValue(getLocaleProperty("branchId"));
			cell.setCellStyle(filterStyle);
			font2.setBoldweight((short) 5);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(font2);
			cell.setCellStyle(filterStyle);
			sheet.setColumnWidth(colCount, (15 * 550));
		}
		
		
		
		//headings
		dynamicReportConfig.getDynmaicReportConfigDetails().stream().filter(config -> config.getIsExportAvailabiltiy())
				.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).forEach(dynamicReportConfigDetail -> {
					cell = row.createCell(colCount++);
					cell.setCellValue(getLocaleProperty(dynamicReportConfigDetail.getLabelName()));
					font2.setBoldweight((short) 5);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(font2);
					/* cell.setCellStyle(filterStyle); */
					cell.setCellStyle(filterStyle);
					sheet.setColumnWidth(colCount, (15 * 550));

				});

		/**/
		row = sheet.createRow(rowCount++);
		mainGridRows.stream().forEach(arr -> {
			row.setHeight((short) 600);
			AtomicInteger colCount = new AtomicInteger(0);
			cell = row.createCell(0);
			style1.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style1);
			cell.setCellValue(serialNumber++);
			
			Long procurmentId = (Long) arr[0];
			if (StringUtil.isEmpty(getBranchId())) {
				dynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder())).limit(1)
						.forEach(dynamicReportConfigDetail -> {
							cell = row.createCell(colCount.getAndIncrement());
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));

							cell.setCellValue(getBranchesMap().get(fValue));
						});
				// cell.setCellStyle(rows);
			} else {
				colCount.getAndIncrement();
				//colCount.getAndIncrement();
			}
			//Values
			dynamicReportConfig.getDynmaicReportConfigDetails().stream()
					.filter(config -> config.getIsExportAvailabiltiy())
					.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
					.forEach(dynamicReportConfigDetail -> {
						cell = row.createCell(colCount.getAndIncrement());
						if (dynamicReportConfigDetail.getAccessType() == 1L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								if (fValue instanceof Long) {
									cell.setCellValue((Long) fValue);
								}
								else if (fValue instanceof Double) {
									cell.setCellValue((Double) fValue);
								}else {
									cell.setCellValue(fValue.toString());
								}
							} else {
								cell.setCellValue("");
							}
						} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, String.valueOf(colCount));
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
								if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
									if (fValue instanceof Integer) {
										if(mValue instanceof String){
									           cell.setCellValue(mValue.toString());
									          }
										else{
									          cell.setCellValue((Integer) mValue);
									          }
									} else {
										cell.setCellValue(mValue.toString());
									}
								} else {
									cell.setCellValue("");
								}
							}
						} else if (dynamicReportConfigDetail.getAccessType() == 3L) {
							fValue = ReflectUtil.getObjectFieldValue(arr, dynamicReportConfigDetail.getParameters());
							if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
								mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue);
								if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
									cell.setCellValue((Integer) mValue);
								} else {
									cell.setCellValue(mValue.toString());
								}
							} else {
								cell.setCellValue("");
							}
						}
						// cell.setCellStyle(rows);
					});

			ProcurementDetail procurementDetail = new ProcurementDetail();
			Procurement procurement = new Procurement();
			procurement.setId(procurmentId);
			procurementDetail.setProcurement(procurement);
			super.filter = procurementDetail;
			Map subData = null;
			if (subDynamicReportConfig.getFetchType() == 2L) {
				subData = readProjectionData(subDynamicReportConfig.getDynmaicReportConfigDetails());
			} else {
				subData = readData();
			}

			List<Object[]> subGridRows = (List<Object[]>) subData.get(ROWS);

			if (!ObjectUtil.isListEmpty(subGridRows)) {
				row = sheet.createRow(rowCount++);
				row.setHeight((short) 300);
				AtomicInteger colCount1 = new AtomicInteger(1);
				subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.filter(config -> config.getIsGridAvailabiltiy())
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
						.forEach(dynamicReportConfigDetail -> {
							cell = row.createCell(colCount1.getAndIncrement());
							cell.setCellValue(getLocaleProperty(dynamicReportConfigDetail.getLabelName()));
							font2.setBoldweight((short) 5);
							font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
							subGridHeader.setFont(font2);
							/* cell.setCellStyle(filterStyle); */
							cell.setCellStyle(subGridHeader);
						});

				// sheet.addMergedRegion(new CellRangeAddress(rowCount,
				// rowCount+subGridRows.size(), 1, colCount1.get()));
			}

			subGridRows.stream().forEach(subArr -> {
				row = sheet.createRow(rowCount++);
				row.setHeight((short) 300);
				AtomicInteger colCount2 = new AtomicInteger(0);
				cellIndex = 0;
				cell = row.createCell(cellIndex++);
				cell.setCellValue("");

				subDynamicReportConfig.getDynmaicReportConfigDetails().stream()
						.filter(config -> config.getIsGridAvailabiltiy())
						.sorted((f1, f2) -> Long.compare(f1.getOrder(), f2.getOrder()))
						.forEach(dynamicReportConfigDetail -> {
							cell = row.createCell(cellIndex++);
							colCount2.getAndIncrement();
							if (dynamicReportConfigDetail.getAccessType() == 1L) {
								fValue = ReflectUtil.getObjectFieldValue(subArr, String.valueOf(colCount2));
								if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
									if (fValue instanceof Long) {
										cell.setCellValue((Long) fValue);
									}else if (fValue instanceof Double) {
										cell.setCellValue((Double) fValue);
									}else {
										cell.setCellValue(fValue.toString());
									}
								} else {
									cell.setCellValue("");
								}
							} else if (dynamicReportConfigDetail.getAccessType() == 2L) {
								fValue = ReflectUtil.getObjectFieldValue(subArr, String.valueOf(colCount2));
								if (!ObjectUtil.isEmpty(fValue) && !StringUtil.isEmpty(fValue)) {
									mValue = getMethodValue(dynamicReportConfigDetail.getMethod(), fValue.toString());
									if (!ObjectUtil.isEmpty(mValue) && !StringUtil.isEmpty(mValue)) {
										if (fValue instanceof Integer) {
											cell.setCellValue((Integer) mValue);
										}else if (fValue instanceof Double) {
											cell.setCellValue((Double) fValue);
										} else {
											cell.setCellValue(mValue.toString());
										}
									} else {
										cell.setCellValue("");
									}
								}
							}
							cell.setCellStyle(rows);
						});
			});

			row = sheet.createRow(rowCount++);
			row = sheet.createRow(rowCount++);
		});
/*
		for (int i = 0; i <= colCount; i++) {
			sheet.autoSizeColumn(i);
		}*/

		// alternateGreenAndWhiteRows(sheet);

		Drawing drawing = sheet.createDrawingPatriarch();
		int pictureIdx = getPicIndex(workbook);
		XSSFClientAnchor anchor = new XSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		//anchor.setAnchorType(1);
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();

		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("procurementReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		workbook.write(fileOut);
		/*InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();*/
		File file = new File(makeDir + fileName);
		fileInputStream = new FileInputStream(file);
		fileOut.close();


		return fileInputStream;
	}

	public void alternateGreenAndWhiteRows(XSSFSheet sheet) {

		SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
		ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("MOD(ROW(),2)");
		PatternFormatting fill1 = rule1.createPatternFormatting();
		fill1.setFillBackgroundColor(IndexedColors.TURQUOISE.index);
		fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

		CellRangeAddress[] regions = { CellRangeAddress.valueOf("A7:Z108") };

		sheetCF.addConditionalFormatting(regions, rule1);

	}

	@SuppressWarnings("rawtypes")
	public InputStream getKpfExportDataStream(String exportType) throws IOException {
		
		InputStream stream;
		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("ProcurementExportProcurementTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("ProcurementExportProcurementTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		if (isMailExport()) {
			rowNum++;
			rowNum++;
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow1 = sheet.createRow(rowNum++);

			cell = filterRow1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(getsDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow2 = sheet.createRow(rowNum++);

			cell = filterRow2.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow2.createCell(2);
			if (geteDate() != null) {
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(seasonCode)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cSeasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(season.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("ProcurementExportColumnHeader");
		int mainGridIterator = 0;

		String enableSupplier = preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER);
		if (enableSupplier.equals("1")) {
			mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderSupplierProcurement");
		}
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			cell = mainGridRowHead.createCell(mainGridIterator);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}
		
	
		
		setFilter(new Procurement());
		filter.setAgroTransaction(new AgroTransaction());
		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeasonCode(seasonCode);
		}

		if (!StringUtil.isEmpty(farmerId)) {

			filter.getAgroTransaction().setFarmerId(farmerId);
		}
		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByName(villageName.trim()));
		}
		super.filter = this.filter;
		Map data = readData();

		List<Procurement> mainGridRows = (List<Procurement>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		ESESystem preferences = preferncesService.findPrefernceById("1");
		DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
		enableSupplier = preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER);
		setIsKpfEnabled(preferences.getPreferences().get(ESESystem.IS_KPF_BASED));

		for (Procurement procurement : mainGridRows) {

			if (isMailExport() && flag) {

				if (!StringUtil.isEmpty(filter.getAgroTransaction().getFarmerId())) {

					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmer")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow4.createCell(2);
					cell.setCellValue(new HSSFRichTextString(procurement.getAgroTransaction().getFarmerName() + " - "
							+ procurement.getAgroTransaction().getFarmerId()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

				if (!ObjectUtil.isEmpty(filter.getVillage()) && !StringUtil.isEmpty(filter.getVillage().getName())) {

					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("village")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					cell.setCellValue(new HSSFRichTextString(
							procurement.getVillage().getName() + " - " + procurement.getVillage().getCode()));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					if (StringUtil.isEmpty(filter.getAgroTransaction().getFarmerId())) {
						sheet.shiftRows(filterRow4.getRowNum() + 1, filterRow5.getRowNum() + 1, -1);
					}
				}

				flag = false;
			}

			//if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KPF_TENANT_ID)) 

			if(isKpfEnabled=="1"){
				row = sheet.createRow(rowNum++);
				colNum = 0;

				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						? (!StringUtil.isEmpty(procurement.getAgroTransaction().getTxnTime())
								? DateUtil.getDateByDateTime(procurement.getAgroTransaction().getTxnTime()) : "")
						: "");

				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						? (!StringUtil.isEmpty(procurement.getAgroTransaction().getTxnTime())
								? DateUtil.getMonthByDateTime(procurement.getAgroTransaction().getTxnTime()) : "")
						: "");

				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						? (!StringUtil.isEmpty(procurement.getAgroTransaction().getTxnTime())
								? DateUtil.getYearByDateTime(procurement.getAgroTransaction().getTxnTime()) : "")
						: "");

				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(procurement.getInvoiceNo())
						? (!StringUtil.isEmpty(procurement.getInvoiceNo()) ? procurement.getInvoiceNo() : "") : "");

				cell = row.createCell(colNum++);
				if (!StringUtil.isEmpty(procurement.getWarehouseId())
						&& StringUtil.isLong(procurement.getWarehouseId())) {
					cell.setCellValue(getWarehouseMap().containsKey(procurement.getWarehouseId())
							? getWarehouseMap().get(procurement.getWarehouseId()) : "");
				} else {
					cell.setCellValue("");
				}

				cell = row.createCell(colNum++);
				if (!ObjectUtil.isEmpty(procurement.getAgroTransaction())
						&& !StringUtil.isEmpty(procurement.getAgroTransaction().getAgentId())) {
					cell.setCellValue(procurement.getAgroTransaction().getAgentName());
				} else {
					cell.setCellValue("");
				}

				cell = row.createCell(colNum++);
				cell.setCellValue(!StringUtil.isEmpty(procurement.getCropType())
						? getLocaleProperty("cpType" + procurement.getCropType()) : "");

				if (enableSupplier.equals("1")) {
					if (!StringUtil.isEmpty(procurement.getProcMasterType())) {
						if (procurement.getProcMasterType().equals("99")
								|| procurement.getProcMasterType().equals("11")) {
							if (procurement.getProcMasterType().equals("99")) {
								if (ObjectUtil.isEmpty(procurement.getFarmer())) {
									cell = row.createCell(colNum++);
									cell.setCellValue(getText("sup99"));
									cell = row.createCell(colNum++);
								} else {

									cell = row.createCell(colNum++);
									cell.setCellValue(
											!ObjectUtil.isEmpty(procurement.getFarmer())
													? (!ObjectUtil.isEmpty(procurement.getFarmer().getSamithi())
															? (procurement.getFarmer().getSamithi()
																	.getGroupType() != null
																			? getText(
																					"grpType" + procurement.getFarmer()
																							.getSamithi()
																							.getGroupType())
																			: "")
															: "")
													: "");
									cell = row.createCell(colNum++);

								}

							} /*
								 * else
								 * if(procurement.getProcMasterType().equals("4"
								 * )) { cell = row.createCell(colNum++);
								 * cell.setCellValue(getText("sup4")); cell =
								 * row.createCell(colNum++); }
								 */else if (procurement.getProcMasterType().equals("11")) {
								cell = row.createCell(colNum++);
								cell.setCellValue(!ObjectUtil.isEmpty(procurement.getFarmer())
										? (!ObjectUtil.isEmpty(procurement.getFarmer().getSamithi())
												? (procurement.getFarmer().getSamithi().getGroupType() != null
														? getText("sup"
																+ procurement.getFarmer().getSamithi().getGroupType())
														: "")
												: "")
										: "");
								cell = row.createCell(colNum++);
							} else {
								cell.setCellValue("NA");
							}
							if (!ObjectUtil.isEmpty(procurement.getFarmer())
									&& !ObjectUtil.isEmpty(procurement.getFarmer().getSamithi())) {

								cell.setCellValue(!StringUtil.isEmpty(procurement.getFarmer().getSamithi().getName())
										? procurement.getFarmer().getSamithi().getName() : "");
							} else {
								cell.setCellValue("NA");
							}
							cell = row.createCell(colNum++);
							cell.setCellValue(!ObjectUtil.isEmpty(procurement.getAgroTransaction())
									? (!StringUtil.isEmpty(procurement.getAgroTransaction().getFarmerName())
											? procurement.getAgroTransaction().getFarmerName() : "")
									: "");
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(getText("sup" + procurement.getProcMasterType()));
							if (!ObjectUtil.isEmpty(procurement.getFarmer())) {
								cell = row.createCell(colNum++);
								cell.setCellValue((getFpoMap().get(procurement.getProcMasterTypeId())));

								cell = row.createCell(colNum++);
								cell.setCellValue(procurement.getFarmer().getFirstName());

							} else {
								if (!ObjectUtil.isEmpty(procurement.getProcMasterTypeId())
										&& !procurement.getProcMasterTypeId().equalsIgnoreCase("")) {
									MasterData mData = farmerService
											.findMasterDataIdById(Long.valueOf(procurement.getProcMasterTypeId()));
									cell = row.createCell(colNum++);
									cell.setCellValue(!ObjectUtil.isEmpty(mData) ? mData.getName() : "");
									cell = row.createCell(colNum++);
									cell.setCellValue("NA");
								} else {
									cell = row.createCell(colNum++);
									cell.setCellValue("NA");
									cell = row.createCell(colNum++);
									cell.setCellValue("NA");
								}
							}
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue("NA");
						cell = row.createCell(colNum++);
						cell.setCellValue("NA");

					}
				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue("NA");
					cell = row.createCell(colNum++);
					cell.setCellValue("NA");

				}

				cell = row.createCell(colNum++);
				cell.setCellValue(
						!ObjectUtil.isEmpty(procurement.getVillage()) ? procurement.getVillage().getName() : "");

				Map<String, Object> details = getTotalProductDetails(procurement);

				cell = row.createCell(colNum++);
				cell.setCellValue(CurrencyUtil.getDecimalFormat((Double) details.get(NET_WEIGHT), "##.000"));

				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(procurement.getTotalProVal())
						? CurrencyUtil.thousandSeparator(procurement.getTotalProVal()) : "0.00");

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String sunGridcolumnHeaders = getLocaleProperty("exportColumnSubHeaderSupplierProcurement");
				int subGridIterator = 1;

				for (String cellHeader : sunGridcolumnHeaders.split("\\,")) {

					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
					}

					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(style3);
					font3.setBoldweight((short) 10);
					font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style3.setFont(font3);
					subGridIterator++;
				}

				for (ProcurementDetail procurementDetail : procurement.getProcurementDetails()) {
					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(!ObjectUtil.isEmpty(procurementDetail.getProcurementGrade())
									? procurementDetail.getProcurementGrade().getProcurementVariety()
											.getProcurementProduct().getName()
									: ""));

					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(!ObjectUtil.isEmpty(procurementDetail.getProcurementGrade())
									? procurementDetail.getProcurementGrade().getName() : ""));

					if (!StringUtil.isEmpty(procurementDetail.getUnit())) {
						FarmCatalogue cat = catalogueService.findCatalogueByCode(procurementDetail.getUnit());
						cell = row.createCell(colNum++);
						cell.setCellValue(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");
					} else {
						cell.setCellValue("NA");
					}

					Double price = !ObjectUtil.isEmpty(procurementDetail.getProcurementGrade())
							? procurementDetail.getProcurementGrade().getPrice() : 0D;

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(price, "##.000")));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							CurrencyUtil.getDecimalFormat(procurementDetail.getGrossWeight(), "##.000")));

					Double total = price * procurementDetail.getGrossWeight();
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(total, "##.000")));

				}

			}

		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("procurementReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		File file = new File(makeDir + fileName);
		stream = new FileInputStream(file);
		fileOut.close();

		return stream;

	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public int getPicIndex(XSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	public static DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public static void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		ProcurementReportAction.dynamicReportConfig = dynamicReportConfig;
	}

	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public static String getDateByDateTime(String date) {
		if (!StringUtil.isEmpty(date)) {
			Date startDate;
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			DateFormat df = new SimpleDateFormat(TXN_TIME_FORMAT);
			try {
				String d = date.substring(0, date.length() - 2);
				startDate = df.parse(d);
				calendar.setTime(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int day = calendar.get(Calendar.DAY_OF_MONTH);
			return String.valueOf(day);
		}
		return null;

	}

	public static String getMonthByDateTime(String date) {
		if (!ObjectUtil.isEmpty(date)) {
			Date startDate;
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			DateFormat df = new SimpleDateFormat(TXN_TIME_FORMAT);
			try {
				String d = date.substring(0, date.length() - 2);
				startDate = df.parse(d);
				calendar.setTime(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String month = new SimpleDateFormat("MMM").format(calendar.getTime());
			return String.valueOf(month);
		}
		return null;

	}

	public static String getYearByDateTime(String date) {
		if (!ObjectUtil.isEmpty(date)) {
			Date startDate;
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			DateFormat df = new SimpleDateFormat(TXN_TIME_FORMAT);
			try {
				String d = date.substring(0, date.length() - 2);
				startDate = df.parse(d);
				calendar.setTime(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int year = calendar.get(Calendar.YEAR);
			return String.valueOf(year);
		}
		return null;

	}

	public Map<String, String> getWarehouseMap(String id) {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> id, Warehouse::getName));
		}
		return warehouseMap;
	}

	public String getCropType(String prop) {

		return !StringUtil.isEmpty(prop) ? getLocaleProperty("cpType" + prop) : "";
	}

	public String getSupplier(String supplier) {
		if (preferncesService.findPrefernceByName(ESESystem.ENABLE_SUPPLIER).equals("1")) {
			if (!StringUtil.isEmpty(supplier)) {
				if (supplier.equals("99") || supplier.equals("11") || supplier.equals("4")) {
					if (supplier.equals("99")) {
						return getText("sup99");
					} else if (supplier.equals("4")) {
						return getText("sup4");
					} else {
						return getText("sup11");
					}

				} else {
					return getText("sup" + supplier);
				}
			}

		}
		return null;
	}

	public String getSupplierName(String fpo) {
		if (!StringUtil.isEmpty(fpo)) {
			FarmCatalogue cat = getCatlogueValueByCode(fpo);
			return !ObjectUtil.isEmpty(cat) ? cat.getName() : "";
		}
		return null;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Map<String, String> getWarehouseMap() {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		}
		return warehouseMap;
	}

	public Map<String, String> getFpoMap() {
		if (fpoMap.size() <= 0) {
			fpoMap = farmerService.listCatelogueType(getText("fpoType")).stream()
					.collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		}
		return fpoMap;
	}

	public Map<String, String> getFarmerMap() {
		if (farmerMap.size() <= 0) {
			farmerService.listFarmerInfo().stream().forEach(farmer -> {
				farmerMap.put(String.valueOf(farmer[1]), String.valueOf(farmer[6]));
			});
		}
		return farmerMap;
	}

	private Map<String, Object> getTotalProductDetails(Procurement procurement) {

		long noOfBags = 0;
		double grossWt = 0;
		/* double tareWt = 0; */
		double netWt = 0;
		double dryLoss = 0;
		double gradingLoss = 0;
		long productId = 0;
		long gradeId = 0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(procurement.getProcurementDetails())) {
			for (ProcurementDetail detail : procurement.getProcurementDetails()) {
				noOfBags = noOfBags + detail.getNumberOfBags();
				grossWt = grossWt + detail.getGrossWeight();
				/* tareWt = tareWt + detail.getTareWeight(); */
				netWt = netWt + detail.getNetWeight();
				if (detail.getDryLoss() != null) {
					dryLoss = detail.getDryLoss();
				} else {
					dryLoss = 0;
				}
				if (detail.getGradingLoss() != null) {
					gradingLoss = detail.getGradingLoss();
				} else {
					gradingLoss = 0;
				}
				if (detail.getProcurementProduct() != null) {
					productId = detail.getProcurementProduct().getId();
				}
				if (detail.getProcurementGrade() != null) {
					gradeId = detail.getProcurementGrade().getId();
				}

			}
		}
		details.put(NO_OF_BAGS, noOfBags);
		details.put(GROSS_WEIGHT, grossWt);
		/* details.put(TARE_WEIGHT, tareWt); */
		details.put(NET_WEIGHT, netWt);
		details.put(DRY_LOSS, dryLoss);
		details.put(GRADING_LOSS, gradingLoss);
		details.put(PRODUCT_ID, productId);
		details.put(GRADE_ID, gradeId);
		return details;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getEnableTraceability() {
		return enableTraceability;
	}

	public void setEnableTraceability(String enableTraceability) {
		this.enableTraceability = enableTraceability;
	}

	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setFpoMap(Map<String, String> fpoMap) {
		this.fpoMap = fpoMap;
	}

	public void setFarmerMap(Map<String, String> farmerMap) {
		this.farmerMap = farmerMap;
	}

	public String getSupplierEnabled() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE);
		if (!ObjectUtil.isEmpty(preference)) {
			supplierEnabled = (preference.getPreferences().get(ESESystem.ENABLE_SUPPLIER));

		}
		return supplierEnabled;
	}

	public void setSupplierEnabled(String supplierEnabled) {

		this.supplierEnabled = supplierEnabled;
	}

	public Map<String, String> getVillageList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfVillageInfoByProcurement();
		List<String> villagesList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			villagesList = farmerList.stream().map(obj -> String.valueOf(obj[1])).distinct()
					.collect(Collectors.toList());
			farmerMap = villagesList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
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

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public Procurement getFilter() {
		return filter;
	}

	public void setFilter(Procurement filter) {
		this.filter = filter;
	}
	
	

	
	public void populateHeaderFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Object[] datas=null;
		String hqlQuery=null;
		String hqlQueryTrim="";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(filterList)) {
			Type listType1 = new TypeToken<List<FilterFieldData>>() {
			}.getType();
			List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
			for(FilterFieldData filterFieldData:filtersList)
			{
				if(filterFieldData.getName().equalsIgnoreCase("seasonCode"))
				{
					hqlQuery+= " AND p."+filterFieldData.getName()+"=:"+filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
				else
				{
					hqlQuery+= " AND "+filterFieldData.getName()+"=:"+filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
			}
			
		}
		if(!StringUtil.isEmpty(hqlQuery))
		{
			hqlQueryTrim=hqlQuery.replace("null", "");
		}
		//datas = farmerService.findTotalAmtAndweightByProcurement(hqlQueryTrim,params);
		datas = farmerService.findTotalAmtAndweightByProcurementWithDate(hqlQueryTrim,params,DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),
			    DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT),branch);
		
		jsonObject.put("totalNetwt", !StringUtil.isEmpty(datas[0]) ? datas[0] : "0.0");
		jsonObject.put("txnAmount", !StringUtil.isEmpty(datas[2]) ? datas[2] : "0.0");
		jsonObject.put("totalPayAmt", !StringUtil.isEmpty(datas[1]) ? datas[1] : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}
	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}

	public String getIsKpfEnabled() {
		return isKpfEnabled;
	}

	public void setIsKpfEnabled(String isKpfEnabled) {
		this.isKpfEnabled = isKpfEnabled;
	}
	
	public void populateChartFieldData() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Object[] datas=null;
		String hqlQuery=null;
		String hqlQueryTrim="";
		String txnAmountLabel = "";
		String totalNetWeightLabel = "";
		String paymentAmountLabel = "";
		String procurementChartTitle = "";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(filterList)) {
			Type listType1 = new TypeToken<List<FilterFieldData>>() {
			}.getType();
			List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
			for(FilterFieldData filterFieldData:filtersList)
			{
				if(filterFieldData.getName().equalsIgnoreCase("seasonCode"))
				{
					hqlQuery+= " AND p."+filterFieldData.getName()+"=:"+filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
				else
				{
					hqlQuery+= " AND "+filterFieldData.getName()+"=:"+filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
			}
			
		}		
		totalNetWeightLabel = getLocaleProperty("totalNetWeightLabel");
		paymentAmountLabel = getLocaleProperty("paymentAmountLabel");
		txnAmountLabel = getLocaleProperty("txnAmountLabel");
		procurementChartTitle = getLocaleProperty("procurementChartTitle");
		if(!StringUtil.isEmpty(hqlQuery))
		{
			hqlQueryTrim=hqlQuery.replace("null", "");
		}
		datas = farmerService.findTotalAmtAndweightByProcurement(hqlQueryTrim,params);
		jsonObject.put("totalNetwt", !StringUtil.isEmpty(datas[0]) ? datas[0] : "0.0");
		jsonObject.put("txnAmount", !StringUtil.isEmpty(datas[1]) ? datas[1] : "0.0");
		jsonObject.put("totalPayAmt", !StringUtil.isEmpty(datas[2]) ? datas[2] : "0.0");
		jsonObject.put("txnAmountLabel", txnAmountLabel);
		jsonObject.put("totalNetWeightLabel", totalNetWeightLabel);
		jsonObject.put("paymentAmountLabel", paymentAmountLabel);
		jsonObject.put("procurementChartTitle", procurementChartTitle);
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}
	
	public void populateChartDrillDownFieldData() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		List<Object[]> datas = new ArrayList<Object[]>();
		String hqlQuery = null;
		String hqlQueryTrim = "";
		String procurementChartTitle = getLocaleProperty("procurementChartTitle");

		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(filterList)) {
			Type listType1 = new TypeToken<List<FilterFieldData>>() {
			}.getType();
			List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
			for (FilterFieldData filterFieldData : filtersList) {
				if (filterFieldData.getName().equalsIgnoreCase("seasonCode")) {
					hqlQuery += " AND p." + filterFieldData.getName() + "=:" + filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				} else {
					hqlQuery += " AND " + filterFieldData.getName() + "=:" + filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
			}

		}

		if (!StringUtil.isEmpty(hqlQuery)) {
			hqlQueryTrim = hqlQuery.replace("null", "");
		}
		datas = farmerService.findTotalWeightAmtNameByProcurement(hqlQueryTrim, params);
		for(Object[] data : datas){
			jsonObject = new JSONObject();
			jsonObject.put("prodName", !StringUtil.isEmpty(data[0]) ? data[0] : "0.0");
			jsonObject.put("procWeight", !StringUtil.isEmpty(data[1]) ? data[1] : "0.0");
			jsonObject.put("txnAmt", !StringUtil.isEmpty(data[2]) ? data[2] : "0.0");
			jsonObject.put("paymentAmt", !StringUtil.isEmpty(data[3]) ? data[3] : "0.0");
			jsonArray.add(jsonObject);
		}				
		
		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelectedProcId() {
		return selectedProcId;
	}

	public void setSelectedProcId(String selectedProcId) {
		this.selectedProcId = selectedProcId;
	}

	public byte[] exportQRLogo() {

		byte[] picData = clientService.findLogoByCode(Asset.QR_LOGO);
		if (!ObjectUtil.isEmpty(picData)) {
			// picData = Base64.decode(picData);
		}
		return picData;
	}
	
}