package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class CropSaleReportAction.
 */
public class CropSaleReportAction extends BaseReportAction implements IExporter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(CropSaleReportAction.class);

	/** The crops. */
	private Map<String, String> crops = new HashMap<String, String>();

	/** The filter. */
	private CropSupply filter;

	/** The sdf. */
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	/** The date of sale. */
	private Date dateOfSale;

	/** The farmer name. */
	private String farmerName;

	/** The farm name. */
	private String farmName;

	/** The crop type. */
	private String cropType;

	/** The qty. */
	private double qty;

	/** The price. */
	private double price;

	/** The id. */
	private String id;

	/** The location service. */
	private ILocationService locationService;

	/** The farmer service. */
	private IFarmerService farmerService;

	/** The farmer id. */
	private String farmerId;

	private String fatherName;

	/** The farm id. */
	private String farmId;
	/** The village. */
	private String village;

	/** The xls file name. */
	private String xlsFileName;
	private String buyerInfo;
	private String  batchLotNo;
	private String poNumber;
	private Customer customer;
	private Map<Long, String> buyersList;

	private IClientService clientService;
	/** The file input stream. */
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private IProductService productService;
	private IPreferencesService preferncesService;
	@Autowired
	private IAgentService agentService;
	private IProductDistributionService productDistributionService;
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private String branchIdParma;
	private String daterange;
	private String seasonCode;
	private String exportLimit;
	private String icsName;
	private String icsNameId;
	private String testDate;
	private String stateName;
	private String fpo;
	private String headerFields;
	private String selectedFieldStaff;
	private String villageId;
	private String villageName;
	@Autowired
	private ICatalogueService catalogueService;
	Map<String, String> coordinates = new LinkedHashMap<String, String>();
	Map<String, String> villageMap = new LinkedHashMap<>();
	/**
	 * List.
	 * 
	 * @return the string
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		// DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		ESESystem preferencess = preferncesService.findPrefernceById("1");
		/*
		 * if(!ObjectUtil.isEmpty(preferencess)) {
		 * //preferencess.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		 * DateFormat dff = new
		 * SimpleDateFormat(preferencess.getPreferences().get(ESESystem.
		 * GENERAL_DATE_FORMAT)); super.startDate = dff.format(cal.getTime());
		 * super.endDate = dff.format(currentDate.getTime());
		 * 
		 * }else{
		 */ DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		// }

		// super.startDate = df.format(cal.getTime());
		// super.endDate = df.format(currentDate.getTime());
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		daterange = super.startDate + " - " + super.endDate;
		crops.put("2", getText("dateOfSale"));
		crops.put("3", getText("cSeasonCode"));
		crops.put("4", getText("farmerName"));
		/* crops.put("5", getLocaleProperty("fatherName")); */
		crops.put("6", getText("buyerName"));
		/* crops.put("7",getText("cropType")); */
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { crops.put("9",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { crops.put("7", getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				crops.put("9", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				crops.put("9", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			crops.put("7", getText("app.branch"));
		}

		crops.put("8", getText("icsName"));
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			crops.put("10", getLocaleProperty("state.name"));

			crops.put("11", getLocaleProperty("cooperative"));

		}
		filter = new CropSupply();
		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		this.filter = new CropSupply();

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setCurrentSeasonCode(seasonCode);
		} /*
			 * else {
			 * filter.setCurrentSeasonCode(clientService.findCurrentSeasonCode()
			 * ); }
			 */

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);

		}
		
		/*if (!StringUtil.isEmpty(villageId)) {
			filter.setFarmerId(villageId);

		}*/
		
		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByCode(villageName.trim()));
		}
		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}
		if (!StringUtil.isEmpty(buyerInfo)) {
			Customer customer = new Customer();
			customer.setId(Long.valueOf(buyerInfo));
			filter.setBuyerInfo(customer);
		}
		
		if (!StringUtil.isEmpty(batchLotNo)) {
        	filter.setBatchLotNo(batchLotNo);
		}
		if (!StringUtil.isEmpty(poNumber)) {
        	filter.setPoNumber(poNumber);
		}
		
		if (!StringUtil.isEmpty(cropType)) {
			filter.setCropType(Integer.parseInt(cropType));
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
		}
	/*	if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}*/
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				if (StringUtil.isEmpty(getBranchId()) || getBranchId().equalsIgnoreCase("organic")) {
			if (!StringUtil.isEmpty(icsName)) {
				/*
				 * Farmer farmer= farmerService.findFarmerByIcsName(icsName);
				 * if(!ObjectUtil.isEmpty(farmer)){
				 * filter.setFarmerId(farmer.getFarmerId());
				 */
				filter.setIcsName(icsName);
				// }
			}}
		}

		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			if (!StringUtil.isEmpty(icsNameId)) {

				filter.setIcsName(icsNameId);
			}
		}
		super.filter = this.filter;
		if (getCurrentTenantId().equalsIgnoreCase("welspun")) {		
			getCoordinates();
		}
		Map data = readData("cropSupply");
		
		return sendJSONResponse(data);
	}

	/**
	 * Sub grid detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String subGridDetail() throws Exception {

		CropSupplyDetails cropSupplyDetails = new CropSupplyDetails();
		if (ObjectUtil.isEmpty(this.filter))
			cropSupplyDetails.setCropSupply(new CropSupply());
		cropSupplyDetails.getCropSupply().setId(Long.valueOf(id));
		super.filter = cropSupplyDetails;
		Map data = readData();
		return sendJSONResponse(data);

	}

	public String detailImage() {

		try {
			setId(id);
			CropSupplyImages imageDetail = farmerService.loadCropSupplyImages(Long.valueOf(id));
			byte[] image = imageDetail.getPhoto();
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.00");
		JSONObject jsonObject = new JSONObject();
		if (obj instanceof CropSupplyDetails) {
			CropSupplyDetails cropSupplyDetails = (CropSupplyDetails) obj;
			JSONArray rows = new JSONArray();
			rows.add(getText("ct" + cropSupplyDetails.getCropType()));
			rows.add(cropSupplyDetails.getCrop().getName());
			rows.add(cropSupplyDetails.getVariety().getName());
			rows.add(cropSupplyDetails.getGrade().getName());
			rows.add(cropSupplyDetails.getCrop().getUnit());
			rows.add(cropSupplyDetails.getBatchLotNo());
			rows.add(formatter.format(cropSupplyDetails.getPrice()));
			if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
				double metricTon = 0.00;
				rows.add(CurrencyUtil.getDecimalFormat(cropSupplyDetails.getQty(), "##.000") + "("
						+ cropSupplyDetails.getCrop().getUnit() + ")");

				if (cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("kg")
						|| cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("kgs")) {
					metricTon = cropSupplyDetails.getQty() / 1000;
				}
				if (cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("quintal")
						|| cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("quintals")) {
					metricTon = cropSupplyDetails.getQty() / 10;
				}
				rows.add(qtyFormat.format(metricTon));
			} else {
				rows.add(qtyFormat.format(cropSupplyDetails.getQty()));
			}
			rows.add(formatter.format(cropSupplyDetails.getSubTotal()));
			jsonObject.put("id", cropSupplyDetails.getId());
			jsonObject.put("cell", rows);
		} else {
			Object[] datas = (Object[]) obj;
			JSONArray rows = new JSONArray();
			Farmer farmer = farmerService.findFarmerByFarmerId(datas[2].toString());
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
							? getBranchesMap().get(getParentBranchMap().get(datas[6]))
							: getBranchesMap().get(datas[6]));
				}
				rows.add(getBranchesMap().get(datas[6]));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					rows.add(branchesMap.get(datas[6]));
				}
			}

			if (!ObjectUtil.isEmpty(datas[1])) {
				String date = "";
				if (!ObjectUtil.isEmpty(datas[1])) {
					ESESystem preferences = preferncesService.findPrefernceById("1");
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(genDate.format(datas[1]));
					}
				}

				// rows.add(date.toString()); // Sale Date
				if (!StringUtil.isEmpty(datas[7])) {
					HarvestSeason season = farmerService.findSeasonNameByCode(datas[7].toString());
					// rows.add(season.getCode() + "-" + season.getName()); //
					// Season
					rows.add(season == null ? "" : season.getName()); // Code.
				} else {
					rows.add("NA");
				}
				if (!StringUtil.isEmpty(datas[13])) {
					 Agent agent = agentService.findAgentByAgentId(datas[13].toString());
					rows.add(agent == null ? "" : agent.getProfileIdWithName());
				} else {
					rows.add("NA");
				}
				// rows.add(!ObjectUtil.isEmpty(datas[20])?datas[20].toString():"");
				// // Season Code
				// rows.add(datas[4].toString() + "-" + datas[5].toString());
				/*rows.add(datas[3].toString());// farmer
*/				  String firstName =  String.valueOf(datas[3]);
                  String farmerId =String.valueOf(datas[2]);
                  Farmer f=farmerService.findFarmerByFarmerId(farmerId);
                  if(!ObjectUtil.isEmpty(f) && f!=null){
  					String linkField = "<a href=farmer_detail.action?id="+f.getId()+" target=_blank>"+firstName+"</a>";
  					rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");
  					}else{
  						rows.add(firstName);
  					}
                 rows.add(!ObjectUtil.isEmpty(f.getFarmerCode()) ? f.getFarmerCode() : "");
	// farmer
						//  farmer name

				/*
				 * if (datas[3].toString() != "") {
				 * 
				 * if (!ObjectUtil.isEmpty(farmer)) {
				 * rows.add(farmer.getLastName()); } else { rows.add(""); } }
				 */

				// rows.add(datas[5].toString() + "-" + datas[6].toString());//
				// farm
				// name
				rows.add(datas[5].toString());
				if (datas[2].toString() != "") {
					// Farmer farmer =
					// farmerService.findFarmerByFarmerId(datas[2].toString());
					if (!ObjectUtil.isEmpty(farmer)) {
						rows.add(farmer.getVillage().getName());
					} else {
						rows.add("");
					}

				}
				CropSupply cropSupply = farmerService.findcropSupplyId(datas[0].toString());
				// rows.add(cropSupply.getBuyerInfo().getCustomerName());//
				// buyer Name
				rows.add(!ObjectUtil.isEmpty(cropSupply) ? cropSupply.getBuyerInfo().getCustomerName() : "");
				
				
				
				/*
				 * rows.add(getText("ct" + datas[7].toString()));// crop type
				 */ /*
					 * rows.add(datas[10]);// crop name
					 */ if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
					if (!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getIcsName())) {
						FarmCatalogue cat = catalogueService.findCatalogueByCode(farmer.getIcsName());
						rows.add(!ObjectUtil.isEmpty(cat) ? cat.getName() : "");// ICS
																				// Name
					} else {

						rows.add("");
					}

				}
				// rows.add(!ObjectUtil.isEmpty(datas[11]) ?
				// datas[11].toString() : "");// batch/lot
				// no
					 
			if (getCurrentTenantId().equalsIgnoreCase("welspun")) {					
				List<CropSupplyDetails> cropSupplyDetails = farmerService
				.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
				rows.add(!ObjectUtil.isListEmpty(cropSupplyDetails) ? cropSupplyDetails.get(0).getBatchLotNo():"N/A");
			}	
			
			
			if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
				rows.add(!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : "N/A");	
			}
								
				rows.add(!ObjectUtil.isEmpty(datas[8]) ? datas[8].toString() : "");// recipt
																		// no
				rows.add(!ObjectUtil.isEmpty(datas[17]) ? formatter.format(Double.valueOf(datas[17].toString())) : "");// price
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					rows.add(!ObjectUtil.isEmpty(datas[15]) ? qtyFormat.format(Double.valueOf(datas[15].toString()))
							: "");// qty
				}
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					double metricTon = 0.00;
					List<CropSupplyDetails> cropSupplyDetails = farmerService
							.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
					if (!ObjectUtil.isListEmpty(cropSupplyDetails)) {
						for (CropSupplyDetails crps : cropSupplyDetails) {
							if (crps.getCrop().getUnit().equalsIgnoreCase("kg")
									|| crps.getCrop().getUnit().equalsIgnoreCase("kgs")) {
								metricTon += (crps.getQty() / 1000);
							}
							if (crps.getCrop().getUnit().equalsIgnoreCase("quintal")
									|| crps.getCrop().getUnit().equalsIgnoreCase("quintals")) {
								metricTon += (crps.getQty() / 10);
							}
						}
					}
					rows.add(qtyFormat.format(metricTon));
				}

				rows.add(!ObjectUtil.isEmpty(datas[16]) ? formatter.format(Double.valueOf(datas[16].toString())) : "");// subTotal
				/*
				 * if (datas[20] == "null") {
				 * rows.add(!StringUtil.isEmpty(datas[20].toString()) ?
				 * CurrencyUtil.getDecimalFormat(Double.valueOf(datas[20].
				 * toString()), "##.00") : "");// total // price }
				 */
				// rows.add(!ObjectUtil.isEmpty(datas[19])?formatter.format(Double.valueOf(datas[19].toString())):"");//
				// total price
				rows.add(!ObjectUtil.isEmpty(datas[9]) ? datas[9].toString() : "");// transporter
																					// name
				rows.add(!ObjectUtil.isEmpty(datas[10]) ? datas[10].toString() : "");
				/*
				 * if(!ObjectUtil.isEmpty(datas[12]) &&
				 * datas[12].toString().equalsIgnoreCase("Cs")){
				 * rows.add(formatter.format(Double.parseDouble(datas[11].
				 * toString()))); } else{ rows.add(formatter.format(0)); }
				 */
				rows.add((!ObjectUtil.isEmpty(datas[11]) && datas[11] != null)
						? formatter.format(Double.parseDouble(datas[11].toString())) : "");

				
				// vechile No
				// rows.add(!ObjectUtil.isEmpty(datas[18])?datas[18].toString():"");//Branch
				// Id
				/*
				 * rows.add(CurrencyUtil.getDecimalFormat((Double) datas[9],
				 * "##.000"));// total qty
				 * rows.add(CurrencyUtil.getDecimalFormat((Double) datas[10],
				 * "##.000"));// total price
				 */
				
				if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
					
				if (!ObjectUtil.isEmpty(datas[4].toString())) {
					String farmCoord = coordinates.get(datas[4].toString());	
					rows.add(!StringUtil.isEmpty(farmCoord)
							? farmCoord : "0,0");
					}
				}
				String[] locVal = new String[0];
				String cropSupplyImgId = "";
				List<CropSupplyImages> cropSupplyImagesList = farmerService.listCropSupplyImages(cropSupply.getId());
				if (!ObjectUtil.isListEmpty(cropSupplyImagesList)) {
					for (CropSupplyImages cropSupplyImages : cropSupplyImagesList) {
						if (cropSupplyImages.getPhoto() != null && cropSupplyImages.getPhoto().length > 0) {
							cropSupplyImgId += String.valueOf(cropSupplyImages.getId()) + ",";
						}

					}

					if (!StringUtil.isEmpty(cropSupplyImgId)) {
						String tempVal = cropSupplyImgId.substring(0, cropSupplyImgId.length() - 1);
						rows.add("<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow("
								+ cropSupply.getId() + ",'" + tempVal + "')\"></button>");
					} else {
						rows.add("<button class='no-imgIcn'></button>");
					}
					
					
					/*
					 * if (!StringUtil.isEmpty(cropSupplyImgId)) { String
					 * tempVal = cropSupplyImgId.substring(0,
					 * cropSupplyImgId.length() - 1); String[] locArray =
					 * tempVal.split(","); String loc = locArray[0]; locVal =
					 * loc.split("/");
					 * 
					 * if ((!StringUtil.isEmpty(locVal[2]) &&
					 * !StringUtil.isEmpty(locVal[3]))) {
					 * rows.add("<button class='faMap' title='" +
					 * getText("farm.map.available.title") +
					 * "' onclick='showFarmMap(\"" +
					 * (!StringUtil.isEmpty(locVal[2]) ? locVal[2] : "0") +
					 * "\",\"" + (!StringUtil.isEmpty(locVal[3]) ? locVal[3] :
					 * "0") + "\")'></button>"); } else { // No Latlon
					 * rows.add("<button class='no-latLonIcn' title='" +
					 * getText("farm.map.unavailable.title") + "'></button>"); }
					 * 
					 * }
					 */

				} else
					rows.add("<button class='no-imgIcn'></button>");
				jsonObject.put("id", datas[0]);
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */

	public CropSupply getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(CropSupply filter) {

		this.filter = filter;
	}

	/**
	 * Sets the crops.
	 * 
	 * @param crops
	 *            the new crops
	 */

	/**
	 * Gets the date of sale.
	 * 
	 * @return the date of sale
	 */
	public Date getDateOfSale() {

		return dateOfSale;
	}

	/**
	 * Sets the date of sale.
	 * 
	 * @param dateOfSale
	 *            the new date of sale
	 */
	public void setDateOfSale(Date dateOfSale) {

		this.dateOfSale = dateOfSale;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Gets the farmer name.
	 * 
	 * @return the farmer name
	 */
	public String getFarmerName() {

		return farmerName;
	}

	/**
	 * Sets the farmer name.
	 * 
	 * @param farmerName
	 *            the new farmer name
	 */
	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	/**
	 * Gets the farm name.
	 * 
	 * @return the farm name
	 */
	public String getFarmName() {

		return farmName;
	}

	/**
	 * Sets the farm name.
	 * 
	 * @param farmName
	 *            the new farm name
	 */
	public void setFarmName(String farmName) {

		this.farmName = farmName;
	}

	/**
	 * Gets the crop type.
	 * 
	 * @return the crop type
	 */
	public String getCropType() {

		return cropType;
	}

	/**
	 * Sets the crop type.
	 * 
	 * @param cropType
	 *            the new crop type
	 */
	public void setCropType(String cropType) {

		this.cropType = cropType;
	}

	/**
	 * Gets the qty.
	 * 
	 * @return the qty
	 */
	public double getQty() {

		return qty;
	}

	/**
	 * Sets the qty.
	 * 
	 * @param qty
	 *            the new qty
	 */
	public void setQty(double qty) {

		this.qty = qty;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public double getPrice() {

		return price;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(double price) {

		this.price = price;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
	 */
	public String getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id
	 * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
	 */
	public void setId(String id) {

		this.id = id;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	/**
	 * Gets the farmers list.
	 * 
	 * @return the farmers list
	 */
	/*
	 * public Map<String, String> getFarmersList() {
	 * 
	 * Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
	 * 
	 * 
	 * List<Farmer> farmersList = farmerService.listFarmer();
	 * 
	 * for (Farmer obj : farmersList) { farmerListMap.put(obj.getFarmerId(),
	 * obj.getFirstName() + " " + obj.getLastName() + " - " +
	 * obj.getFarmerId()); }
	 * 
	 * 
	 * List<Object[]> farmersList = farmerService.listFarmerInfo();
	 * farmerListMap = farmersList.stream().collect(Collectors.toMap(obj ->
	 * String.valueOf(obj[1]), obj -> (String.valueOf(obj[3]) + "-" +
	 * String.valueOf(obj[1])))); farmerListMap =
	 * farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmerListMap;
	 * 
	 * }
	 */

	/*
	 * public Map<String, String> getFarmersList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * farmerService.listFarmerInfoByCropSale(); if
	 * (!ObjectUtil.isEmpty(farmerList)) {
	 * 
	 * farmerMap = farmerList.stream() .collect(Collectors.toMap(obj ->
	 * (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1]))));
	 * 
	 * } return farmerMap; }
	 */
	/*
	 * public List<String[]> getFathersNameList() {
	 * 
	 * 
	 * Map<String, String> fatherNameListMap = new LinkedHashMap<String,
	 * String>();
	 * 
	 * List<String[]> farmersList = farmerService.listByFatherNameList(); for
	 * (String[] obj : farmersList) { if(!StringUtil.isEmpty(obj[0])) {
	 * fatherNameListMap.put(obj[0], obj[0]); } } return fatherNameListMap;
	 * 
	 * 
	 * List<String[]> fatherNameList = farmerService.listByFatherNameList();
	 * 
	 * return fatherNameList; }
	 */

	public Map<String, String> getFathersNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByCropSale();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			testList = farmerList.stream().filter(obj -> !StringUtil.isEmpty(obj[2].toString()))
					.map(obj -> String.valueOf(obj[2])).distinct().collect(Collectors.toList());
			farmerMap = testList.stream().collect(Collectors.toMap(String::toString, String::toString));

		}
		return farmerMap;
	}

	/**
	 * Gets the farm list.
	 * 
	 * @return the farm list
	 */
	public Map<String, String> getFarmList() {

		Map<String, String> farmListMap = new LinkedHashMap<String, String>();

		List<Farm> listFarms = farmerService.listFarm();
		for (Farm obj : listFarms) {
			farmListMap.put(obj.getFarmCode(), obj.getFarmName() + " - " + obj.getFarmCode());
		}
		return farmListMap;

	}

	/**
	 * Gets the crop type list.
	 * 
	 * @return the crop type list
	 */
	public Map<Integer, String> getCropTypeList() {

		Map<Integer, String> cropListMap = new LinkedHashMap<Integer, String>();
		cropListMap.put(0, getText("ct0"));
		cropListMap.put(1, getText("ct1"));
		return cropListMap;

	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the farm id.
	 * 
	 * @return the farm id
	 */
	public String getFarmId() {

		return farmId;
	}

	/**
	 * Sets the farm id.
	 * 
	 * @param farmId
	 *            the new farm id
	 */
	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	/*
	 * public Map<String, String> getBuyersList() {
	 * 
	 * // Map<String, String> buyerListMap = new HashMap<String, String>();
	 * 
	 * buyersList = clientService.listOfCustomers();
	 * 
	 * for (Customer buyer : buyersList) {
	 * buyerListMap.put(buyer.getCustomerId(), buyer.getCustomerId() + " - " +
	 * buyer.getCustomerName()); }
	 * 
	 * 
	 * return buyersList; }
	 */

	/**
	 * Gets the village.
	 * 
	 * @return the village
	 */
	public String getVillage() {

		return village;
	}

	/**
	 * Sets the village.
	 * 
	 * @param village
	 *            the new village
	 */
	public void setVillage(String village) {

		this.village = village;
	}

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("CropSaleReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("cropSaleReport"), fileMap, ".xls"));
		return "xls";
	}

	/**
	 * Gets the excel export input stream.
	 * 
	 * @param manual
	 *            the manual
	 * @return the excel export input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */

	public InputStream getExportDataStream(String exportType) throws IOException {
		if (getCurrentTenantId().equalsIgnoreCase("welspun")) {		
		getCoordinates();
		}
		Long serialNumber = 0L;

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("CropSaleExportProcurementTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
		HSSFCellStyle style6 = wb.createCellStyle();
		
		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null,
				filterRow6, filterRow7 = null, filterRow8 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;
		int count = 0;
		int rowNum = 2;
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("CropSaleExportProcurementTitle")));
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

			if (!ObjectUtil.isEmpty(geteDate())) {
				cell = filterRow2.createCell(2);
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
			if (!StringUtil.isEmpty(farmerId)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
				cell.setCellValue(new HSSFRichTextString(farmer.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(villageName)) {

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(villageName)
						? locationService.findVillageByCode(villageName).getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}
			if (!StringUtil.isEmpty(batchLotNo)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("Batchno")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(batchLotNo));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(poNumber)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("ponumber")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(poNumber));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			
			/*
			 * if (!StringUtil.isEmpty(fatherName)) {
			 * 
			 * cell=filterRow1.createCell(1); cell.setCellValue(new
			 * HSSFRichTextString(getLocaleProperty("farmerName")));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * cell = filterRow1.createCell(2); cell.setCellValue(new
			 * HSSFRichTextString(fatherName)); filterFont.setBoldweight((short)
			 * 12); filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * }
			 */
			if (!StringUtil.isEmpty(buyerInfo)) {
				filterRow1 = sheet.createRow(rowNum++);
				Customer c = clientService.findCustomer(Long.valueOf(buyerInfo));
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("buyerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(c.getCustomerName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}
			if (getCurrentTenantId().equals("chetna")) {

				if (!StringUtil.isEmpty(stateName)) {
					filterRow6 = sheet.createRow(rowNum++);
					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					State s = locationService.findStateByCode(stateName);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(s) ? s.getName() : "")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (!StringUtil.isEmpty(fpo)) {
					filterRow7 = sheet.createRow(rowNum++);
					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					FarmCatalogue fc = farmerService.findfarmcatalogueByCode(fpo);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (!StringUtil.isEmpty(icsNameId)) {
					filterRow8 = sheet.createRow(rowNum++);
					cell = filterRow8.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow8.createCell(2);
					FarmCatalogue fc = farmerService.findfarmcatalogueByCode(icsNameId);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

			}
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if (!StringUtil.isEmpty(icsName)) {
					filterRow8 = sheet.createRow(rowNum++);
					cell = filterRow8.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow8.createCell(2);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(icsName) ? icsName : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
			}
			if (!StringUtil.isEmpty(selectedFieldStaff)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("agentId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				 Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);
				cell.setCellValue(new HSSFRichTextString(agent.getProfileIdWithName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow4.createCell(2);
					// cell.setCellValue(new
					// HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
					cell.setCellValue(new HSSFRichTextString(
							getBranchesMap().get(getParentBranchMap().get(getBranchId()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				} else {
					if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {

						cell = filterRow5.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow5.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				}

			} else {
				if (!StringUtil.isEmpty(branchIdParma)) {
					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getText("organization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "")));
					// cell.setCellValue(new HSSFRichTextString(branchIdParma));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}
				HSSFRow totalGridRowHead = sheet.createRow(rowNum++);
				if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
				String[] headerFieldsArr = headerFields.split("###");
				for (int i = 0; i < headerFieldsArr.length; i++) {
					cell = totalGridRowHead.createCell(count);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(headerFieldsArr[i])));
					style4.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
					style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style4.setFont(font2);
					sheet.setColumnWidth(count, (15 * 550));
					count++;

				}
				}
			}
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeaderIndevCropSale");
			} else {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeaderIndevCropSale");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderBranchCropSale");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganiCropSaleExportHeaderagent");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCICropSaleExportHeaderagent");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderBranchCropSale");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderCropSale");
			}
		}
		/*	 if
		 * (getCurrentTenantId().equals(ESESystem.CRSDEMO_TENANT_ID)) {
		 * mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeaderCrsdemoCropSale"); }
		 */
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				sheet.setColumnWidth(mainGridIterator, (15 * 550));
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase("Organization")) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					//style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
					style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style2);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
					mainGridIterator++;
				}
			}

		}
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new CropSupply();

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setCurrentSeasonCode(seasonCode);

		}

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}
		
		if (!StringUtil.isEmpty(villageName)) {
			filter.setVillage(locationService.findVillageByCode(villageName.trim()));
		}
		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}
		
		if (!StringUtil.isEmpty(batchLotNo)) {
        	filter.setBatchLotNo(batchLotNo);
		}
		if (!StringUtil.isEmpty(poNumber)) {	
        	filter.setPoNumber(poNumber);
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			String agentIds[] = selectedFieldStaff.split("-");
			String agentId = agentIds[0].trim();
			filter.setAgentId(agentId);
		}

		if (!StringUtil.isEmpty(buyerInfo)) {
			Customer customer = new Customer();
			customer.setId(Long.valueOf(buyerInfo));
			filter.setBuyerInfo(customer);
		}

		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}
			if(!StringUtil.isEmpty(icsNameId)){
				filter.setIcsName(icsNameId);
			}

		}
		
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (!StringUtil.isEmpty(icsName)) {

				filter.setIcsName(icsName);
			}
		}

		super.filter = this.filter;

		Map data = readData("cropSupply");

		List<Object[]> dfata = (ArrayList) data.get(ROWS);
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.00");
		if (!ObjectUtil.isEmpty(dfata)) {
			for (Object[] datas : dfata) {
				row = sheet.createRow(rowNum++);
				colNum = 0;
				Farmer farmer = farmerService.findFarmerByFarmerId(datas[2].toString());

				serialNumber++;
				cell = row.createCell(colNum++);
				/*cell.setCellValue(new HSSFRichTextString(
						String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));*/
				style6.setAlignment(CellStyle.ALIGN_CENTER);
				cell.setCellStyle(style6);
				cell.setCellValue(serialNumber);

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
										? getBranchesMap().get(getParentBranchMap().get(datas[6]))
										: getBranchesMap().get(datas[6])));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(datas[6])));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(branchesMap.get(datas[6])));
					}
				}

				if (!ObjectUtil.isEmpty(datas[1])) {
					String date = "";
					if (!ObjectUtil.isEmpty(datas[1])) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						if (!StringUtil.isEmpty(preferences)) {
							DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(genDate.format(datas[1])));
						}
					}
					if (!StringUtil.isEmpty(datas[7])) {
						HarvestSeason season = farmerService.findSeasonNameByCode(datas[7].toString());
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(season == null ? "" : season.getName()));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					}
					if (!StringUtil.isEmpty(datas[13])) {
						Agent agent = agentService.findAgentByAgentId(datas[13].toString());
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(agent == null ? "" : agent.getProfileIdWithName()));
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString("NA"));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(datas[3].toString()));// farmer
																					// name
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(farmer.getFarmerCode()));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(datas[5].toString()));
					if (datas[2].toString() != "") {
						if (!ObjectUtil.isEmpty(farmer)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(farmer.getVillage().getName()));
						} else {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}

					}
					CropSupply cropSupply = farmerService.findcropSupplyId(datas[0].toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(cropSupply.getBuyerInfo().getCustomerName()));// buyer
					
					
					if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
						if (!StringUtil.isEmpty(farmer.getIcsName())) {
							FarmCatalogue cat = catalogueService.findCatalogueByCode(farmer.getIcsName());
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(cat) ? cat.getName() : ""));// ICS
																														// Name
						} else {

							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(""));
						}

					}
					if (getCurrentTenantId().equalsIgnoreCase("welspun")) {					
						List<CropSupplyDetails> cropSupplyDetails = farmerService
						.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isListEmpty(cropSupplyDetails) ? cropSupplyDetails.get(0).getBatchLotNo():"N/A"));
					}	
					
					if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : "N/A"));	
	              	}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[8]) ? datas[8].toString() : ""));// recipt
					// no
					style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style5);
					cell = row.createCell(colNum++);
					//cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[16])
					//		? formatter.format(Double.valueOf(datas[16].toString())) : ""));// price
					
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(datas[17].toString()));
					style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style5);
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[14])
						//		? qtyFormat.format(Double.valueOf(datas[14].toString())) : ""));// qty
						
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[15])
										? qtyFormat.format(Double.valueOf(datas[15].toString())) : ""));
						style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style5);
					}
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						double metricTon = 0.00;
						List<CropSupplyDetails> cropSupplyDetails = farmerService
								.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
						if (!ObjectUtil.isListEmpty(cropSupplyDetails)) {
							for (CropSupplyDetails crps : cropSupplyDetails) {
								if (!StringUtil.isEmpty(crps.getCrop())
										&& !StringUtil.isEmpty(crps.getCrop().getUnit())) {
									if (crps.getCrop().getUnit().equalsIgnoreCase("kg")
											|| crps.getCrop().getUnit().equalsIgnoreCase("kgs")) {
										metricTon += (crps.getQty() / 1000);
									}
									if (crps.getCrop().getUnit().equalsIgnoreCase("quintal")
											|| crps.getCrop().getUnit().equalsIgnoreCase("quintals")) {
										metricTon += (crps.getQty() / 10);
									}
								}
							}
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(qtyFormat.format(metricTon)));
						
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(metricTon));
					}

					/*if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
						if (!ObjectUtil.isEmpty(datas[4].toString())) {
							String farmCoord = getCoordinates().get(datas[4].toString());	
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(farmCoord)
									? farmCoord : "0,0"));
							}
						}*/
					cell = row.createCell(colNum++);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.parseDouble(datas[16].toString()));

					//cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[15])
					//		? formatter.format(Double.valueOf(datas[15].toString())) : ""));// subTotal
					
					style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style5);
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(datas[9]) ? datas[9].toString() : ""));// transporter
					// name
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(!ObjectUtil.isEmpty(datas[10]) ? datas[10].toString() : ""));
					

					
					
					/*
					 * if(!ObjectUtil.isEmpty(datas[12]) &&
					 * datas[12].toString().equalsIgnoreCase("Cs")){ cell =
					 * row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(formatter.format(Double.parseDouble(
					 * datas[11].toString())))); } else{ cell =
					 * row.createCell(colNum++); cell.setCellValue(new
					 * HSSFRichTextString(formatter.format(0))); }
					 */

					if (!StringUtil.isEmpty((datas[11]))) {

						cell = row.createCell(colNum++);
						style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style5);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				//		cell.setCellValue(
					//			new HSSFRichTextString(formatter.format(Double.parseDouble(datas[11].toString()))));
						cell.setCellValue(Double.parseDouble(datas[11].toString()));

					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(""));
					}
					
					if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
						if (!ObjectUtil.isEmpty(datas[4].toString())) {
							String farmCoord = coordinates.get(datas[4].toString());	
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(farmCoord)
									? farmCoord : "0,0"));
							}
						}	

				}

				HSSFRow subGridRowHead = sheet.createRow(rowNum++);
				String subGridColumnHeaders = getLocaleProperty("exportSubgridHeadingsCropSale");

				int subGridIterator = 1;

				for (String cellHeader : subGridColumnHeaders.split("\\,")) {

					if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
					} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
						cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
					}

					cell = subGridRowHead.createCell(subGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style4.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
					style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style4);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style4.setFont(font2);
					sheet.setColumnWidth(subGridIterator, (15 * 550));
					subGridIterator++;
				}

				List<CropSupplyDetails> cropSupplyDetails = farmerService
						.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
				for (CropSupplyDetails csd : cropSupplyDetails) {
					row = sheet.createRow(rowNum++);
					colNum = 1;

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(getText("ct" + csd.getCropType())));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(csd.getCrop().getName()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(csd.getVariety().getName()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(csd.getGrade().getName()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(csd.getCrop().getUnit()));
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(csd.getBatchLotNo()));
					/*cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(csd.getBatchLotNo()));
					style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style5);*/
					cell = row.createCell(colNum++);
					//cell.setCellValue(new HSSFRichTextString(formatter.format(csd.getPrice())));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(csd.getPrice()));
					style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style5);
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						double metricTon = 0.00;
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(csd.getQty(), "##.000")
						//		+ "(" + csd.getCrop().getUnit() + ")"));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(csd.getQty()+"("+ csd.getCrop().getUnit() + ")"));
						
						if (!StringUtil.isEmpty(csd.getCrop()) && !StringUtil.isEmpty(csd.getCrop().getUnit())) {
							if (csd.getCrop().getUnit().equalsIgnoreCase("kg")
									|| csd.getCrop().getUnit().equalsIgnoreCase("kgs")) {
								metricTon = csd.getQty() / 1000;
							}
							if (csd.getCrop().getUnit().equalsIgnoreCase("quintal")
									|| csd.getCrop().getUnit().equalsIgnoreCase("quintals")) {
								metricTon = csd.getQty() / 10;
							}
						}
						cell = row.createCell(colNum++);
						//cell.setCellValue(new HSSFRichTextString(qtyFormat.format(metricTon)));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(metricTon));
						
					} else {
						cell = row.createCell(colNum++);
						style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						cell.setCellStyle(style5);
					//	cell.setCellValue(new HSSFRichTextString(qtyFormat.format(csd.getQty())));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(csd.getQty()));
					}
					cell = row.createCell(colNum++);
					style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style5);
					//cell.setCellValue(new HSSFRichTextString(formatter.format(csd.getSubTotal())));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(csd.getSubTotal()));
				}
				row = sheet.createRow(rowNum++);
			}
		}
		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("exportProcurementTitle") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

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

	public IProductService getProductService() {

		return productService;
	}

	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	public static SimpleDateFormat getFilenamedateformat() {

		return fileNameDateFormat;
	}

	/**
	 * Gets the pic index.
	 * 
	 * @param wb
	 *            the wb
	 * @return the pic index
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String getBuyerInfo() {

		return buyerInfo;
	}

	public void setBuyerInfo(String buyerInfo) {

		this.buyerInfo = buyerInfo;
	}

	
	
	public String getBatchLotNo() {
		return batchLotNo;
	}

	public void setBatchLotNo(String batchLotNo) {
		this.batchLotNo = batchLotNo;
	}

	public SimpleDateFormat getDateFormat() {

		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {

		this.dateFormat = dateFormat;
	}

	public IClientService getClientService() {

		return clientService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	public Customer getCustomer() {

		return customer;
	}

	public void setCustomer(Customer customer) {

		this.customer = customer;
	}

	/*
	 * public Map<Long, String> getBuyersList() { Map<Long, String> listOfBuyers
	 * = new HashMap<Long, String>(); List<Customer> customers =
	 * clientService.listOfCustomers(); for (Customer customer : customers) {
	 * listOfBuyers.put(customer.getId(), customer.getCustomerName()); } return
	 * listOfBuyers; }
	 */

	public void setBuyersList(Map<Long, String> buyersList) {

		this.buyersList = buyersList;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	@Override
	public String populatePDF() throws Exception {
		InputStream is = getPDFExportDataStream();
		setPdfFileName(getText("cropSupplyReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("cropSupplyReportList"), fileMap, ".pdf"));
		return "pdf";

	}

	public InputStream getPDFExportDataStream()
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {
		if (getCurrentTenantId().equalsIgnoreCase("welspun")) {		
		getCoordinates();
		}
		//BaseFont bf =BaseFont.createFont("c:/windows/fonts/arial.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');		
		String arialFontFileLocation = serverFilePath+"/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//","/");
		BaseFont bf =BaseFont.createFont(arialFontFileLocation,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
								// file.

		Long serialNo = 0L;

		int mainGridColWidth = 0;
		int subGridColWidth = 0;

		List<Object[]> entityFieldsList = new ArrayList<Object[]>(); // to hold
																		// properties
																		// of
																		// entity
																		// object
																		// passed
																		// as
																		// list.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();
		setMailExport(true);
		int cols;

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		document.open();

		PdfPTable table = null;
		PdfPCell cell = null;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getText("cropSupplyReportList"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
				? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));// blank
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		if (isMailExport()) {
			document.add(new Paragraph(new Phrase(getLocaleProperty("filter"),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));

			document.add(new Paragraph(
					new Phrase(getLocaleProperty("StartingDate") + " : " + filterDateFormat.format(getsDate()),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(getLocaleProperty("EndingDate") + " : " + filterDateFormat.format(geteDate()),
							new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));

			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
		if (!StringUtil.isEmpty(headerFields)) {
			String[] headerFieldsArr = headerFields.split("###");

			document.add(new Paragraph(new Phrase(
					headerFieldsArr[0] + " : " + headerFieldsArr[1] + "  " + headerFieldsArr[2] + " : "
							+ headerFieldsArr[3] + "  " + headerFieldsArr[4] + " : " + headerFieldsArr[5],
					new Font(bf, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		}
		boolean flag = true;
		DecimalFormat df = new DecimalFormat("0.00");
		String mainGridColumnHeaders = ""; // line
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { mainGridColumnHeaders =
		 * getLocaleProperty("ExportColumnHeaderIndevCropSale"); } else {
		 * mainGridColumnHeaders =
		 * getLocaleProperty("ExportColumnHeaderIndevCropSale"); } } else{ if
		 * (StringUtil.isEmpty(branchIdValue)) { mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeaderBranchCropSale"); } else {
		 * mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeaderCropSale"); } }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeaderIndevCropSale");
			} else {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnHeaderIndevCropSale");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderBranchCropSale");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganiCropSaleExportHeaderagent");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCICropSaleExportHeaderagent");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderBranchCropSale");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderCropSale");
			}
		}

		/*
		 if
		 * (getCurrentTenantId().equals(ESESystem.CRSDEMO_TENANT_ID)) {
		 * mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeaderCrsdemoCropSale"); }
		 */
		int mainGridIterator = 0;
		table = new PdfPTable(mainGridColumnHeaders.split("\\,").length);
		cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK);
		mainGridColWidth = mainGridColumnHeaders.split("\\,").length;
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new CropSupply();

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setCurrentSeasonCode(seasonCode);
			HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
			document.add(new Paragraph(new Phrase(getLocaleProperty("cSeasonCode") + ": " + season.getName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
			Farmer frm = farmerService.findFarmerByFarmerId(farmerId);
			document.add(new Paragraph(new Phrase(getLocaleProperty("farmerName") + ": " + frm.getName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		
		if (!StringUtil.isEmpty(batchLotNo)) {
			document.add(new Paragraph(new Phrase(getLocaleProperty("Batchno") + ": " + batchLotNo,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
        	filter.setBatchLotNo(batchLotNo);
        	
		}
		if (!StringUtil.isEmpty(poNumber)) {
			document.add(new Paragraph(new Phrase(getLocaleProperty("ponumber") + ": " + poNumber,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
        	filter.setPoNumber(poNumber);
		}
		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
			document.add(new Paragraph(new Phrase(getLocaleProperty("fatherName") + ": " + fatherName,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(villageName)) {
			document.add(new Paragraph(new Phrase(
					getLocaleProperty("villageName") + " : "
							+ (!ObjectUtil.isEmpty(villageName)
									? locationService.findVillageByCode(villageName).getName() : getText("NA")),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
		}
		
		
		if (!StringUtil.isEmpty(buyerInfo)) {
			Customer customer = new Customer();
			customer.setId(Long.valueOf(buyerInfo));
			filter.setBuyerInfo(customer);
			Customer c = clientService.findCustomer(Long.valueOf(buyerInfo));
			document.add(new Paragraph(new Phrase(getLocaleProperty("buyerName") + ": " + c.getCustomerName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(selectedFieldStaff)) {
			filter.setAgentId(selectedFieldStaff);
			Agent agent = agentService.findAgentByAgentId(selectedFieldStaff);
			document.add(new Paragraph(new Phrase(getLocaleProperty("agentName") + ": " + agent.getProfileIdWithName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (!StringUtil.isEmpty(branchIdParma)) {

				document.add(new Paragraph(
						new Phrase(getLocaleProperty("Organization") + " : " + getBranchesMap().get(getBranchIdParma()),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				document.add(new Paragraph(new Phrase(
						getLocaleProperty("Sub Organization") + " : " + getBranchesMap().get(getSubBranchIdParma()),
						new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
			} else {
				if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
					document.add(new Paragraph(new Phrase(
							getLocaleProperty("Sub Organization") + " : " + getBranchesMap().get(getSubBranchIdParma()),
							new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				}

			}
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
				State s = locationService.findStateByCode(stateName);
				document.add(new Paragraph(new Phrase(getLocaleProperty("stateName") + ": " + s.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(fpo);
				document.add(new Paragraph(new Phrase(getLocaleProperty("fpo") + ": " + fc.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
			}
			
			if (!StringUtil.isEmpty(icsNameId)) {

				filter.setIcsName(icsNameId);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(icsNameId);
				document.add(new Paragraph(new Phrase(getLocaleProperty("icsName") + ": " + fc.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));

			}
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
		if (!StringUtil.isEmpty(icsName)) {

			filter.setIcsName(icsName);
			document.add(new Paragraph(new Phrase(getLocaleProperty("icsName") + ": " + icsName,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));

		}
		}
		super.filter = this.filter;
		Map data = readData("cropSupply");
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.00");

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}
			cell = new PdfPCell(new Phrase(cellHeader, cellFont));
			cell.setBackgroundColor(new BaseColor(144,238,144));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setNoWrap(false); // To set wrapping of text in cell.
			// cell.setColspan(3); // To add column span.
			table.addCell(cell);
		}
		cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
		List<Object[]> mainGridRows = (ArrayList) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		else {
			for (Object[] datas : mainGridRows) {

				serialNo++;

				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				Farmer farmer = farmerService.findFarmerByFarmerId(datas[2].toString());
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(
								new Phrase(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[6])))
										? getBranchesMap().get(getParentBranchMap().get(datas[6]))
										: getBranchesMap().get(datas[6]), cellFont));
						table.addCell(cell);
					}
					cell = new PdfPCell(new Phrase(getBranchesMap().get(datas[6]), cellFont));
					table.addCell(cell);

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Paragraph(new Phrase(branchesMap.get(datas[6]), cellFont)));
						table.addCell(cell);
					}
				}

				if (!ObjectUtil.isEmpty(datas[1])) {
					String date = "";
					if (!ObjectUtil.isEmpty(datas[1])) {
						ESESystem preferences = preferncesService.findPrefernceById("1");
						if (!StringUtil.isEmpty(preferences)) {
							DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
							cell = new PdfPCell(new Phrase(genDate.format(datas[1]), cellFont));
							table.addCell(cell);
						}
					}

					if (!StringUtil.isEmpty(datas[7])) {
						HarvestSeason season = farmerService.findSeasonNameByCode(datas[7].toString());
						cell = new PdfPCell(new Phrase(season == null ? "" : season.getName(), cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("NA", cellFont));
						table.addCell(cell);
					}
					
					if (!StringUtil.isEmpty(datas[13])) {
						Agent agent = agentService.findAgentByAgentId(datas[13].toString());
						cell = new PdfPCell(new Phrase(agent == null ? "" : agent.getProfileIdWithName(), cellFont));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("NA", cellFont));
						table.addCell(cell);
					}
					
					cell = new PdfPCell(new Phrase(datas[3].toString(), cellFont));// farmer
																					// name
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(farmer.getFarmerCode(), cellFont));// farmer
					// name
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(datas[5].toString(), cellFont));
					table.addCell(cell);
					if (datas[2].toString() != "") {
						if (!ObjectUtil.isEmpty(farmer)) {
							cell = new PdfPCell(new Phrase(farmer.getVillage().getName(), cellFont));
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase((""), cellFont));
							table.addCell(cell);
						}

					}
					CropSupply cropSupply = farmerService.findcropSupplyId(datas[0].toString());
					cell = new PdfPCell(new Phrase(cropSupply.getBuyerInfo().getCustomerName(), cellFont));// buyer
																											// Name
					
					
					table.addCell(cell);

					if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
						if (!StringUtil.isEmpty(farmer.getIcsName())) {
							FarmCatalogue cat = catalogueService.findCatalogueByCode(farmer.getIcsName());
							cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(cat) ? cat.getName() : "", cellFont));// ICS
																														// Name
							table.addCell(cell);
						} else {

							cell = new PdfPCell(new Phrase((""), cellFont));
							table.addCell(cell);
						}

					}
					
					
					if (getCurrentTenantId().equalsIgnoreCase("welspun")) {					
						List<CropSupplyDetails> cropSupplyDetails = farmerService
						.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
						cell = new PdfPCell(new Phrase(!ObjectUtil.isListEmpty(cropSupplyDetails) ? cropSupplyDetails.get(0).getBatchLotNo():"N/A"));
						table.addCell(cell);
					}	
					
					if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
						cell = new PdfPCell(new Phrase((!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : "N/A")));
						table.addCell(cell);
					}
					
					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[8]) ? datas[8].toString() : "", cellFont));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[17])
							? formatter.format(Double.valueOf(datas[17].toString())) : "", cellFont));// price
					table.addCell(cell);
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[15])
								? qtyFormat.format(Double.valueOf(datas[15].toString())) : "", cellFont));// qty
						table.addCell(cell);
					}
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						double metricTon = 0.00;
						List<CropSupplyDetails> cropSupplyDetails = farmerService
								.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
						if (!ObjectUtil.isListEmpty(cropSupplyDetails)) {
							for (CropSupplyDetails crps : cropSupplyDetails) {
								if (!StringUtil.isEmpty(crps.getCrop())
										&& !StringUtil.isEmpty(crps.getCrop().getUnit())) {
									if (crps.getCrop().getUnit().equalsIgnoreCase("kg")
											|| crps.getCrop().getUnit().equalsIgnoreCase("kgs")) {
										metricTon += (crps.getQty() / 1000);
									}
									if (crps.getCrop().getUnit().equalsIgnoreCase("quintal")
											|| crps.getCrop().getUnit().equalsIgnoreCase("quintals")) {
										metricTon += (crps.getQty() / 10);
									}
								}
							}
						}
						cell = new PdfPCell(new Phrase(qtyFormat.format(metricTon), cellFont));
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[16])
							? formatter.format(Double.valueOf(datas[16].toString())) : "", cellFont));// subTotal
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[9]) ? datas[9].toString() : "", cellFont));// transporter
					table.addCell(cell);
					// name
				
					
					cell = new PdfPCell(
							new Phrase(!ObjectUtil.isEmpty(datas[10]) ? datas[10].toString() : "", cellFont));
					table.addCell(cell);
					
					
			
					/*
					 * if(!ObjectUtil.isEmpty(datas[12]) &&
					 * datas[12].toString().equalsIgnoreCase("Cs")){ cell=new
					 * PdfPCell(new
					 * Phrase(formatter.format(Double.parseDouble(datas[11].
					 * toString())),cellFont)); table.addCell(cell); } else{
					 * cell=new PdfPCell(new
					 * Phrase(formatter.format(0),cellFont));
					 * table.addCell(cell); }
					 */

					if (!StringUtil.isEmpty((datas[11]))) {

						cell = new PdfPCell(
								new Phrase(formatter.format(Double.parseDouble(datas[11].toString())), cellFont));
						table.addCell(cell);

					} else {

						cell = new PdfPCell();
						table.addCell("");

					}
					if (getCurrentTenantId().equalsIgnoreCase("welspun")) {	
						if (!ObjectUtil.isEmpty(datas[4].toString())) {
							String farmCoord = coordinates.get(datas[4].toString());	
							cell = new PdfPCell(
									new Phrase(!StringUtil.isEmpty(farmCoord)
									? farmCoord : "0,0"));
							table.addCell(cell);
							}
						}
					
					if (serialNo >= 1) {
						cell = new PdfPCell();
						table.addCell(cell);

					}
					
					
					/*	}
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						cell = new PdfPCell(new Phrase(!ObjectUtil.isEmpty(datas[15])
								? qtyFormat.format(Double.valueOf(datas[15].toString())) : "", cellFont));// qty
						table.addCell(cell);
					}*/
					String subGridHeaders = getLocaleProperty("exportSubgridHeadingsCropSale");
					int subGridIterator = 1;

					cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK);
					subGridColWidth = subGridHeaders.split("\\,").length;
					for (String cellHeader : subGridHeaders.split("\\,")) {
						if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
							cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
						} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
							cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
						}
						cell = new PdfPCell(new Phrase(cellHeader, cellFont));
						cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setNoWrap(false); // To set wrapping of text in
												// cell.
						// cell.setColspan(3); // To add column span.
						table.addCell(cell);
					}

					for (int i = subGridColWidth + 1; i < table.getNumberOfColumns(); i++) {
						cell = new PdfPCell(new Phrase("", cellFont));
						table.addCell(cell);
					}

					cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
					List<CropSupplyDetails> cropSupplyDetails = farmerService
							.findCropSupplyDetailId(Long.parseLong(datas[0].toString()));
					for (CropSupplyDetails csd : cropSupplyDetails) {

						if (serialNo >= 1) {
							cell = new PdfPCell();
							table.addCell(cell);

						}

						cell = new PdfPCell(new Phrase(getText("ct" + csd.getCropType()), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(csd.getCrop().getName(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(csd.getVariety().getName(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(csd.getGrade().getName(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(csd.getCrop().getUnit(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(csd.getBatchLotNo(), cellFont));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(formatter.format(csd.getPrice()), cellFont));
						table.addCell(cell);
						if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
							double metricTon = 0.00;
							cell = new PdfPCell(new Phrase(CurrencyUtil.getDecimalFormat(csd.getQty(), "##.000") + "("
									+ csd.getCrop().getUnit() + ")", cellFont));
							table.addCell(cell);
							if (!StringUtil.isEmpty(csd.getCrop()) && !StringUtil.isEmpty(csd.getCrop().getUnit())) {
								if (csd.getCrop().getUnit().equalsIgnoreCase("kg")
										|| csd.getCrop().getUnit().equalsIgnoreCase("kgs")) {
									metricTon = csd.getQty() / 1000;
								}
								if (csd.getCrop().getUnit().equalsIgnoreCase("quintal")
										|| csd.getCrop().getUnit().equalsIgnoreCase("quintals")) {
									metricTon = csd.getQty() / 10;
								}
							}
							cell = new PdfPCell(new Phrase(qtyFormat.format(metricTon), cellFont));
							table.addCell(cell);
						} else {
							cell = new PdfPCell(new Phrase(qtyFormat.format(csd.getQty()), cellFont));
							table.addCell(cell);
						}
						cell = new PdfPCell(new Phrase(formatter.format(csd.getSubTotal()), cellFont));
						table.addCell(cell);
						for (int i = subGridColWidth + 1; i < table.getNumberOfColumns(); i++) {
							cell = new PdfPCell(new Phrase("", cellFont));
							table.addCell(cell);
						}

					}

				}
			}
		}
		document.add(table); // Add table to document.

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}
	

	/**
	 * 
	 * Gets Season List
	 * 
	 * @return
	 */
	/*
	 * public Map<String, String> getSeasonList() {
	 * 
	 * Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	 * 
	 * List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
	 * 
	 * for (Object[] obj : seasonList) {
	 * 
	 * // seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
	 * seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1])); } return
	 * seasonMap;
	 * 
	 * }
	 */
	/*
	 * public Map<String, String> getIcsList() {
	 * 
	 * Map<String, String> icsList = new LinkedHashMap<String, String>();
	 * 
	 * List<Object[]> ics = catalogueService.loadICSName(); for (Object[] obj :
	 * ics) { icsList.put(String.valueOf(obj[0]), String.valueOf(obj[1])); }
	 * return icsList;
	 * 
	 * }
	 */
	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public Map<String, String> getCrops() {
		return crops;
	}

	public void setCrops(Map<String, String> crops) {
		this.crops = crops;
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public String getIcsName() {

		return icsName;
	}

	public void setIcsName(String icsName) {

		this.icsName = icsName;
	}

	public String getTestDate() {

		ESESystem preferences = preferncesService.findPrefernceById("1");
		String dateFormat = "";
		if (preferences != null && preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT) != null) {
			dateFormat = preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);

		} else {
			dateFormat = DateUtil.DATE_FORMAT_1;

		}
		return dateFormat;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getVillageId() {
		return villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
	}


	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	/*
	 * public Map<String, String> getStateList() {
	 * 
	 * Map<String, String> stateMap = new LinkedHashMap<String, String>();
	 * 
	 * List<State> stateList = locationService.listOfStates(); for (State obj :
	 * stateList) { stateMap.put(obj.getCode(), obj.getCode() + " - " +
	 * obj.getName());
	 * 
	 * } return stateMap;
	 * 
	 * }
	 * 
	 * public Map<String, String> getWarehouseList() { AtomicInteger i = new
	 * AtomicInteger(0); Map<String, String> warehouseMap = new
	 * LinkedHashMap<>(); FarmCatalogueMaster farmCatalougeMaster =
	 * catalogueService
	 * .findFarmCatalogueMasterByName(getLocaleProperty("cooperative")); if
	 * (!ObjectUtil.isEmpty(farmCatalougeMaster)) { Double d = new
	 * Double(farmCatalougeMaster.getId()); List<FarmCatalogue>
	 * farmCatalougeList =
	 * catalogueService.findFarmCatalougeByAlpha(d.intValue());
	 * 
	 * for (FarmCatalogue catalogue : farmCatalougeList) { if
	 * (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
	 * warehouseMap.put(catalogue.getCode(), catalogue.getName()); } }
	 * 
	 * } warehouseMap.put("99", "Others");
	 * 
	 * return warehouseMap;
	 * 
	 * }
	 */
	public void populateSeasonList() {
		JSONArray seasonArr = new JSONArray();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		if (!ObjectUtil.isEmpty(seasonList)) {
			seasonList.forEach(obj -> {
				seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(seasonArr);
	}

	public void populateFarmerList() {
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmerList = farmerService.listFarmerInfoByCropSale();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.forEach(obj -> {
				farmerArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}

	public void populateBuyerInfoList() {
		JSONArray customerArr = new JSONArray();
		List<Customer> customers = clientService.listOfCustomers();
		if (!ObjectUtil.isEmpty(customers)) {
			customers.forEach(obj -> {
				customerArr.add(getJSONObject(obj.getId(), obj.getCustomerName()));
			});
		}
		sendAjaxResponse(customerArr);
	}

	public void populateICSList() {
		JSONArray icsArr = new JSONArray();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("icsName"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());
			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					icsArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
				}
			}
		}
		icsArr.add(getJSONObject("99", "Others"));
		sendAjaxResponse(icsArr);
	}

	/*
	 * public void populateICSNameList(){ JSONArray icsArr = new JSONArray();
	 * List<Object[]> ics = catalogueService.loadICSName(); if
	 * (!ObjectUtil.isEmpty(ics)) { ics.forEach(obj -> {
	 * icsArr.add(getJSONObject(obj[0].toString(), obj[1].toString())); }); }
	 * sendAjaxResponse(icsArr); }
	 */
	public void populateStateList() {
		JSONArray stateArr = new JSONArray();
		List<State> stateList = locationService.listOfStates();
		if (!ObjectUtil.isEmpty(stateList)) {
			stateList.forEach(obj -> {
				stateArr.add(getJSONObject(obj.getCode(), obj.getCode() + "-" + obj.getName()));
			});
		}
		sendAjaxResponse(stateArr);
	}

	public void populateWarehouseList() {
		JSONArray warehouseArr = new JSONArray();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("cooperative"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());

			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					warehouseArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
				}
			}

		}
		warehouseArr.add(getJSONObject("99", "Others"));

	}

	public void populateQtyAmt() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Object[] data = farmerService.findCropSaleQtyAmt(farmerId, seasonCode, buyerInfo, stateName, fpo, icsName,
				branchIdParma, DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),
				DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT),selectedFieldStaff,subBranchIdParma);
		jsonObject.put("totalQty", !StringUtil.isEmpty(data[0]) ? CurrencyUtil.getDecimalFormat(Double.valueOf(String.valueOf(data[0])),"##.00" ): "0.0");
		jsonObject.put("totalAmt", !StringUtil.isEmpty(data[1]) ? CurrencyUtil.getDecimalFormat(Double.valueOf(String.valueOf(data[1])),"##.00" ) : "0.0");
		jsonObject.put("totalPayAmt", !StringUtil.isEmpty(data[2]) ? CurrencyUtil.getDecimalFormat(Double.valueOf(String.valueOf(data[2])),"##.00" ) : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}

	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}

	public String getIcsNameId() {
		return icsNameId;
	}

	public void setIcsNameId(String icsNameId) {
		this.icsNameId = icsNameId;
	}

	public String getSelectedFieldStaff() {
		return selectedFieldStaff;
	}

	public void setSelectedFieldStaff(String selectedFieldStaff) {
		this.selectedFieldStaff = selectedFieldStaff;
	}
	public void populateAgentList() {
		JSONArray agentArr = new JSONArray();
		List<Object[]> agentList = agentService.listAgentIdName();
		if (!ObjectUtil.isEmpty(agentList)) {
			agentList.forEach(obj -> {
				agentArr.add(getJSONObject(obj[0].toString(), obj[1].toString()+" - "+obj[0].toString()));
			});
		}
		sendAjaxResponse(agentArr);
		
	}
	 
		/*
		 * public Map<String, String> getSeasonList() {
		 * 
		 * Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		 * 
		 * List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		 * 
		 * for (Object[] obj : seasonList) {
		 * 
		 * // seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
		 * seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1])); } return
		 * seasonMap;
		 * 
		 * }
		 */
		
	public Map<String, String> getCoordinates() {
		List<Object[]> listFarms = farmerService.listFarmInfoLatLon();
		for (Object[] obj : listFarms) {
			
			coordinates.put(obj[1].toString(), !ObjectUtil.isEmpty(obj[3]) && !ObjectUtil.isEmpty(obj[4]) ? obj[3].toString() + "," + obj[4].toString() : "0,0");
		}
		return coordinates;
	}
	
	
	public void setCoordinates(Map<String, String> coordinates) {
		this.coordinates = coordinates;
	}

	public Map<String, String> getVillageMap() {
		locationService.listVillageIdAndName().stream().forEach(u -> {
			villageMap.put(u[1].toString(), u[2].toString());
		});
		return villageMap;
	}

	public void setVillageMap(Map<String, String> villageMap) {
		this.villageMap = villageMap;
	}

}