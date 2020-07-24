package com.sourcetrace.esesw.view.service.agro;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.ProductDistributionService;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.ESELogo;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;
import com.sourcetrace.esesw.view.SwitchAction;
import com.sourcetrace.esesw.view.SwitchValidatorAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("serial")
public class MoleculeAction extends SwitchAction implements IExporter  {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private CropYield molecule;
	private File farmerImage;
	protected String id;
	@Autowired
	private IFarmerService farmerService;
	private List<CropYieldDetail> cropYieldDetailList;
	private List<CropYieldDetail> cropYieldDetailListUs;
	private String typez;
	private String xlsFileName;
	private InputStream fileInputStream;
	protected static final String FILTERDATE = "MM/dd/yyyy";
	private String statusMsg;
	@Autowired
	private IProductDistributionService productDistributionService;

	Map<Integer, String> moleculeList = new LinkedHashMap<Integer, String>();
	public String list() {
		typez = request.getParameter("type");
		return LIST;
	}
	
	public String data() throws Exception {
		
		Map<String, String> searchRecord = getJQGridRequestParam();
		
		

		CropYield filter = new CropYield();
		
        if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
            if (!getIsMultiBranch().equalsIgnoreCase("1")) {
                List<String> branchList = new ArrayList<>();
                branchList.add(searchRecord.get("branchId").trim());
                filter.setBranchesList(branchList);
            } else {
                List<String> branchList = new ArrayList<>();
                List<BranchMaster> branches = clientService
                        .listChildBranchIds(searchRecord.get("branchId").trim());
                branchList.add(searchRecord.get("branchId").trim());
                branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
                    branchList.add(branch.getBranchId());
                });
                filter.setBranchesList(branchList);
            }
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
            filter.setBranchId(searchRecord.get("subBranchId").trim());
        }
		
		if (!StringUtil.isEmpty(searchRecord.get("landHolding"))) {
			filter.setLandHolding(searchRecord.get("landHolding").trim());
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}
	
	
	public String create() {
		if (molecule == null) {
			return INPUT;
		} else {
			//List<DataLevel> cp = farmerService.listDataLevel();
			
			List<DataLevel> cp = farmerService.listDataLevelByType(molecule.getType());

			Map<String, String> euMap = new HashMap<>();
			Map<String, String> usMap = new HashMap<>();
			cp.stream().forEach(d -> {
				euMap.put(d.getCode(), d.getEntityName() + "#" + d.getName());
				usMap.put(d.getCode(), d.getEntityAbsoluteName() + "#" + d.getName());
			});

			if (getFarmerImage() != null) {
				Set<CropYieldDetail> cropYieldDetailSet = new HashSet<>();
				String excelFilePath = getFarmerImage().toString();
				molecule.setImage(FileUtil.getBinaryFileContent(getFarmerImage()));
				molecule.setCropYieldDate(new Date());
				Workbook workbook = null;
				try {
					FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
					workbook = new XSSFWorkbook(inputStream);

					String text = "";

					Sheet firstSheet = workbook.getSheetAt(0);
					Iterator<Row> iterator = firstSheet.iterator();
					int mainFlag = 0;
					int Flag = 0;

					while (iterator.hasNext()) {
						Row nextRow = iterator.next();
						int rNo = nextRow.getRowNum();
						if (rNo == 3 || rNo == 4) {
							text = "";
							Flag = 0;
							CropYieldDetail cyd = new CropYieldDetail();
							Iterator<Cell> cellIterator = nextRow.cellIterator();

							while (cellIterator.hasNext()) {
								Cell cell = cellIterator.next();
								int cIndex = cell.getColumnIndex();
								if (cIndex != 0) {
									switch (cell.getCellType()) {
									case Cell.CELL_TYPE_STRING:
										System.out.print(cell.getStringCellValue());
										break;
									case Cell.CELL_TYPE_BOOLEAN:
										System.out.print(cell.getBooleanCellValue());
										break;
									case Cell.CELL_TYPE_NUMERIC:
										System.out.print(cell.getNumericCellValue());
										System.out.print("--------");
										System.out.print(cIndex);
										if (rNo == 3) {
											cyd.setType(0);
											String[] d = euMap.get(String.valueOf(cIndex)).split("#");
											if (Double.valueOf(d[0]) < cell.getNumericCellValue()) {
												text = text + "~" + euMap.get(String.valueOf(cIndex))+"#"+cell.getNumericCellValue();
												Flag = 1;
												mainFlag = 1;
											}

										} else if (rNo == 4) {
											cyd.setType(1);
											String[] d = usMap.get(String.valueOf(cIndex)).split("#");
											if (Double.valueOf(d[0]) < cell.getNumericCellValue()) {
												text = text + "~" + usMap.get(String.valueOf(cIndex))+"#"+cell.getNumericCellValue();
												Flag = 1;
												mainFlag = 1;

											}

										}
										break;
									}
								}

							}
							cyd.setStatus(Flag);
							cyd.setYield(text);
							cropYieldDetailSet.add(cyd);
						}

					}
					molecule.setStatus(mainFlag);
					molecule.setCropYieldDetails(cropYieldDetailSet);
					molecule.setBranchId(getBranchId());
					farmerService.save(molecule);

					inputStream.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return REDIRECT;
	}

	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		CropYield cropYield = (CropYield) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(!StringUtil.isEmpty(
                    getBranchesMap().get(getParentBranchMap().get(cropYield.getBranchId())))
                            ? getBranchesMap()
                                    .get(getParentBranchMap().get(cropYield.getBranchId()))
                            : getBranchesMap().get(cropYield.getBranchId()));
        }
        rows.add(getBranchesMap().get(cropYield.getBranchId()));

    } else {
        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(branchesMap.get(cropYield.getBranchId()));
        }
    }
		rows.add(cropYield.getCropYieldDate()!=null?cropYield.getCropYieldDate().toString():"");
		rows.add(cropYield.getLandHolding());
		rows.add(cropYield.getType().equalsIgnoreCase("1") ? "Pesticide Resuide" : "AFLATOXIN TEST");
		rows.add(cropYield.getStatus() == 0 ? "Success" : "Failed");
		rows.add(
				"<button class=\"fa fa-download\"\"aria-hidden=\"true\"\" onclick=\"popDownload('"
						+ cropYield.getId() + "')\"></button>");
		rows.add(
				"<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
						+ cropYield.getId() + "')\"></button>");
		jsonObject.put("id", cropYield.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}
	public File getFarmerImage() {
		return farmerImage;
	}

	public void setFarmerImage(File farmerImage) {
		this.farmerImage = farmerImage;
	}

	public CropYield getMolecule() {
		return molecule;
	}

	public void setMolecule(CropYield molecule) {
		this.molecule = molecule;
	}
	
	public String populateDownload() {

		try {
			CropYield cy = farmerService.findCropYieldById(Long.valueOf(id));
			String fileName=cy.getLandHolding().replaceAll("\\s+","");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + fileName + "." + "xlsx");
			response.getOutputStream().write(cy.getImage());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	public String populateSampleFileDownload() {

		try {
			long idd=1;
			byte[] cy = productDistributionService.findLogoImageById(idd);

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + "sampleFormat" + "." + "xlsx");
			response.getOutputStream().write(cy);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public String populateMoleculeData() {
		
		try {

			setId(id);
			// String[] splitId = id.split("#");
			// List<Object[]> pmtDatas =
			// productDistributionService.findpmtdetailByPmtId(Long.valueOf(splitId[1]),Long.valueOf(splitId[0]));

			List<CropYieldDetail> lDatas = farmerService.findCropYieldDetailById(Long.valueOf(id));
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			
			
			lDatas.stream().forEach(loanData -> {
				JSONObject jsonObject = new JSONObject();
				
				jsonObject.put("failedMolecule", loanData.getYield());
				jsonObject.put("status", loanData.getStatus());
				
				
				jsonObjects.add(jsonObject);

			});

			printAjaxResponse(jsonObjects, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	  public String detail() throws Exception {
		  typez = request.getParameter("type");
			List<CropYieldDetail> cropSupplyDetailTempList = new ArrayList<>();
			cropYieldDetailList = new ArrayList<>();
			cropYieldDetailListUs=new ArrayList<>();
	        String view = "";
	        String[] one;
	        String[] two;
	        if (id != null && !id.equals("")) {
	        	molecule = farmerService.findCropYieldById(Long.valueOf(id));
	        }
		if (molecule.getStatus() != 1) {
			setStatusMsg("success");
		} else {
			setStatusMsg("Failed");
		}
	        cropSupplyDetailTempList = farmerService.findCropYieldDetailById(Long.valueOf(id));
	        for (CropYieldDetail cropSupplyDetails : cropSupplyDetailTempList) {
	        	String[] parts = cropSupplyDetails.getYield().split("~");
	        	System.out.println(parts);
	        	for(int i=1;i<=parts.length-1;i++){
	        		one=parts[i].split("#");
	        		CropYieldDetail crop = new CropYieldDetail();
	        		crop.setMoleculeValue(one[2].toString());
	        		crop.setMoleculeName(one[1].toString());
	        		crop.setMoleculeExpectedValue(one[0].toString());
	        		if(cropSupplyDetails.getType()==0)
	        		cropYieldDetailList.add(crop);
	        		else
	        			cropYieldDetailListUs.add(crop);
	        	}
	        	
	        	
        		//System.out.println(one);
	         	
	        }
	        
	            view = DETAIL;
	            request.setAttribute(HEADING, getText("harvestSeasondetail"));
	        
	            request.setAttribute(HEADING, getText("harvestSeasonlist"));
	            //return LIST;
	        
	        return view;
	    }

	public List<CropYieldDetail> getCropYieldDetailList() {
		return cropYieldDetailList;
	}

	public void setCropYieldDetailList(List<CropYieldDetail> cropYieldDetailList) {
		this.cropYieldDetailList = cropYieldDetailList;
	}

	public List<CropYieldDetail> getCropYieldDetailListUs() {
		return cropYieldDetailListUs;
	}

	public void setCropYieldDetailListUs(List<CropYieldDetail> cropYieldDetailListUs) {
		this.cropYieldDetailListUs = cropYieldDetailListUs;
	}

	public String getTypez() {
		return typez;
	}

	public void setTypez(String typez) {
		this.typez = typez;
	}
	
	public String populateXLS() throws Exception {
		InputStream is;
			is = getFarmFileInputStream(IExporter.EXPORT_MANUAL);
		if (!ObjectUtil.isEmpty(is)) {
			setXlsFileName(getLocaleProperty("Molecule report") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerList").trim(), fileMap, ".xls"));
			return "xls";
		} else {
			return LIST;
		}
	}
	
	
	private InputStream getFarmFileInputStream(String exportType) throws IOException {
		


		Map<String, String> searchRecord = getJQGridRequestParam();
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportXLSFarmerTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null,
				filterRow6 = null, filterRow7 = null, filterRow8 = null, filterRow9 = null, filterRow10 = null,
				filterRow11 = null, filterRow12 = null;
		;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;


		branchIdValue = getBranchId();
		buildBranchMap();

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(3);
		cell.setCellValue(new HSSFRichTextString(getText("Molecule Report")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);



		++rowNum;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String columnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportMoleculeColumnHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportMoleculeColumnHeader");
			}
		} else if (StringUtil.isEmpty(branchIdValue)) {
			columnHeaders = getLocaleProperty("exportMoleculeColumnHeaderBranch" );
		} else {
			columnHeaders = getLocaleProperty("exportMoleculeColumnHeader");
		}

		int mainGridIterator = 0;

		for (String cellHeader : columnHeaders.split("\\,")) {

			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = mainGridRowHead.createCell(mainGridIterator);
				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				if (mainGridIterator != 7) {
					sheet.setColumnWidth(mainGridIterator, (15 * 450));
				}
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		
		
        CropYield filter = new CropYield();


        if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
            if (!getIsMultiBranch().equalsIgnoreCase("1")) {
                List<String> branchList = new ArrayList<>();
                branchList.add(searchRecord.get("branchId").trim());
                filter.setBranchesList(branchList);
            } else {
                List<String> branchList = new ArrayList<>();
                List<BranchMaster> branches = clientService
                        .listChildBranchIds(searchRecord.get("branchId").trim());
                branchList.add(searchRecord.get("branchId").trim());
                branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
                    branchList.add(branch.getBranchId());
                });
                filter.setBranchesList(branchList);
            }
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
            filter.setBranchId(searchRecord.get("subBranchId").trim());
        }        
        
		if (!StringUtil.isEmpty(searchRecord.get("landHolding"))) {
			filter.setLandHolding(searchRecord.get("landHolding").trim());
		}

		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		
		List<CropYield> cropYieldList = (List) data.get(ROWS);
		

		Long serialNumber = 0L;

		AtomicInteger snoCount = new AtomicInteger(0);
		for (CropYield obj : cropYieldList) {
			
			row = sheet.createRow(rowNum++);
			row.setHeight((short) 400);
			colNum = 0;
			cell = row.createCell(colNum++);
			cell.setCellValue(snoCount.incrementAndGet());
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil
							.isEmpty(getBranchesMap().get(getParentBranchMap().get(obj.getBranchId())))
									? getBranchesMap().get(getParentBranchMap().get(obj.getBranchId()))
									: getBranchesMap().get(obj.getBranchId())));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(getBranchesMap().get(obj.getBranchId()));
			}

			else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(obj.getBranchId())
							? (branchesMap.get(obj.getBranchId())) : "N/A"));
				}
			}
			
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(StringUtil.isEmpty(obj.getCropYieldDate().toString()) ? getText("NA") : obj.getCropYieldDate().toString()));
			
			
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(StringUtil.isEmpty(obj.getLandHolding()) ? getText("NA") : obj.getLandHolding()));

			cell = row.createCell(colNum++);
			if(!StringUtil.isEmpty(String.valueOf(obj.getStatus()))){
				cell.setCellValue(
						new HSSFRichTextString((obj.getStatus()==0) ? "Success" : "Failed"));
			}else{
				cell.setCellValue("NA");
			}
			
			
			
		}
	

	
		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("farmerList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
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

@Override
public InputStream getExportDataStream(String exportType) throws IOException {
	// TODO Auto-generated method stub
	return null;
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

public String getStatusMsg() {
	return statusMsg;
}

public void setStatusMsg(String statusMsg) {
	this.statusMsg = statusMsg;
}

public Map<String, String> getListLotCode() {
	return farmerService.listValueByFieldName(getLocaleProperty("lotCode.fieldName"),getBranchId()).parallelStream()
			.filter(fil -> fil != null && !ObjectUtil.isEmpty(fil) && fil[2] != null && fil[1] != null)
			.collect(Collectors.toMap(id -> String.valueOf(id[1]), val -> String.valueOf(val[1])));
}



public Map<Integer, String> getMoleculeTypeList() {
	moleculeList.put(1, getText("moleculeType1"));
	moleculeList.put(2, getText("moleculeType2"));

	return moleculeList;
}

}