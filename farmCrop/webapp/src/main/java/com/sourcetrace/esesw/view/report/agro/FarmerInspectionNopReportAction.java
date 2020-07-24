/*
 * TrainingCompletionReportAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.txn.agrocert.Answers;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersSectionAnswers;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;
/**
 * The Class FarmerInspectionReportAction.
 */
public class FarmerInspectionNopReportAction extends BaseReportAction  {
    private static final long serialVersionUID = 1L;
    private static final String CATEGORY_CODE = "CC005";

    private String DETAIL = "detail";
    private String UPDATE = "update";
    private String NO_RECORD = "No_Records_Present";
    private String command;
    private String id;
    private String answeredDate;
    private String farmerId;
    private String farmId;
    private String farmerNameAndId;
    private String farmNameAndId;
    private double acresOwned;
    private double acresFarmed;
	private double acresOrganic;
	private double acresTransition;
	private double acresEligibleNext;
	private double acresReqIns;
    private InputStream fileInputStream;
    PdfPCell cell;
    private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat(
    "yyyyMMddHHmmss");    
    private Set<FarmersSectionAnswers> sections = new HashSet<FarmersSectionAnswers>();
    private Set<FarmersSectionAnswers> sectionAttributesSet = new LinkedHashSet<FarmersSectionAnswers>();
	private String xlsFileName;
	private IPreferencesService preferncesService;
	private String farmerCodeEnabled;
    public Double getAcresFarmed() {
		return acresFarmed;
	}

	public void setAcresFarmed(Double acresFarmed) {
		this.acresFarmed = acresFarmed;
	}

	DateFormat df = new SimpleDateFormat(getESEDateFormat());
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    private ICertificationService certificationService;
    private IFarmerService farmerService;

    private FarmerCropProdAnswers filter;
    private FarmerCropProdAnswers farmerCropProdAnswers;
    private FarmersQuestionAnswers farmersQuestionAnswers;

    protected List<String> fields = new ArrayList<String>();
    List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
    private String branchIdParma;
    private String daterange;

  

	/**
     * @see com.sourcetrace.esesw.view.BaseReportAction#list()
     */
    public String list() throws Exception {

        Calendar currentDate = Calendar.getInstance();
        Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH,  currentDate.get(Calendar.MONTH)-1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df
				.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		
		daterange = super.startDate+" - "+super.endDate ;
        request.setAttribute(HEADING, getText(LIST));
        fields.add(getText("answeredDate"));
        fields.add(getText("farmer"));
       // fields.add(getText("farm"));
        if(ObjectUtil.isEmpty(getBranchId())){
        	fields.add(getText("app.branch"));
            }
        setFilter(farmerCropProdAnswers);
        return LIST;
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        farmerId = filter.getFarmerId();
        farmId = filter.getFarmId();

        FarmerCropProdAnswers farmerCropProdAnswers = new FarmerCropProdAnswers();
        farmerCropProdAnswers.setCategoryCode(CATEGORY_CODE);
        this.filter = farmerCropProdAnswers;

        if (!StringUtil.isEmpty(branchIdParma)) {
        	 filter.setBranchId(branchIdParma);				
     		}
        if (!StringUtil.isEmpty(farmerId))
            this.filter.setFarmerId(farmerId);

       /* if (!StringUtil.isEmpty(farmId))
            this.filter.setFarmId(farmId);*/

        super.filter = this.filter;
        Map data = readData();
        return sendJSONResponse(data);
    }

    /**
     * Detail.
     * @return the string
     */
    public String detail() {

        String view = "";
        if (id != null && !id.equals("")) {
            farmerCropProdAnswers = certificationService.findFarmerCropProdAnswersById(Long
                    .valueOf(id));

        	if (!StringUtil.isEmpty(farmerCropProdAnswers.getFarmerId())) {
					farmerNameAndId = farmerCropProdAnswers.getFarmerName() + " - "
							+ farmerCropProdAnswers.getFarmerId();

				
			}
			if (!StringUtil.isEmpty(farmerCropProdAnswers.getFarmId())) {
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
					farmNameAndId = farmerCropProdAnswers.getFarmName() + " - " + farmerCropProdAnswers.getFarmCode();
				} else {
					farmNameAndId = farmerCropProdAnswers.getFarmName();
				}
			}

            setAnsweredDate(this.sdf.format(farmerCropProdAnswers.getAnsweredDate()));
            setAcresOwned(Double.valueOf(farmerCropProdAnswers.getAcresOwned()));
            setAcresFarmed(Double.valueOf(farmerCropProdAnswers.getAcresFarmed()));
            setAcresOrganic(Double.valueOf(farmerCropProdAnswers.getAcresOrganic()));
            setAcresTransition(Double.valueOf(farmerCropProdAnswers.getAcresTransition()));
            setAcresEligibleNext(Double.valueOf(farmerCropProdAnswers.getAcresEligibleNext()));
            setAcresReqIns(Double.valueOf(farmerCropProdAnswers.getAcresReqIns()));
            if (farmerCropProdAnswers == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }

            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText(DETAIL));
        } else {
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return view;

    }

    /**
     * Gets the image.
     * @return the image
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("deprecation")
    public String getImage() throws IOException {

        if (id != null && !id.equals("")) {

            farmersQuestionAnswers = certificationService.findFarmerQuestionAswersById(Long
                    .valueOf(id));

            if (!ObjectUtil.isEmpty(farmersQuestionAnswers)) {
                OutputStream out = response.getOutputStream();
                out.write(farmersQuestionAnswers.getImage());
                out.flush();
                out.close();
            } else {

                File file = new File(request.getRealPath("/")
                        + "/img/no-image.png");
                OutputStream out = response.getOutputStream();
                out.write(FileUtil.getBinaryFileContent(file));
                out.flush();
                out.close();
                System.out.print("No Image");
            }

        }

        return null;

    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        JSONObject jsonObject = new JSONObject();
        FarmerCropProdAnswers farmerCropProdAnsObj = (FarmerCropProdAnswers) obj;

        JSONArray rows = new JSONArray();
        if (StringUtil.isEmpty(branchIdValue)) {
  			rows.add(branchesMap.get(farmerCropProdAnsObj.getBranchId()));
  		}
        rows.add("<div onclick='edit(" + farmerCropProdAnsObj.getId() + ")'>"
                + farmerCropProdAnsObj.getFarmerId() + "</div>");

        String farmerName = "";
		if (!ObjectUtil.isEmpty(farmerCropProdAnsObj.getFarmerName())) {
			farmerName = farmerCropProdAnsObj.getFarmerName();

		}
		rows.add(!StringUtil.isEmpty(farmerName) ? farmerName : getText("NA"));
		
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			rows.add(!StringUtil.isEmpty(farmerCropProdAnsObj.getFarmCode()) ? farmerCropProdAnsObj.getFarmCode()
					: getText("NA"));
		}
		rows.add(!StringUtil.isEmpty(farmerCropProdAnsObj.getFarmName()) ? farmerCropProdAnsObj.getFarmName()
				: getText("NA"));

        rows.add(this.sdf.format(farmerCropProdAnsObj.getAnsweredDate()));

        jsonObject.put("id", farmerCropProdAnsObj.getId());

        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
     */
    public String getId() {

        return id;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the answered date.
     * @return the answered date
     */
    public String getAnsweredDate() {

        return answeredDate;
    }

    /**
     * Sets the answered date.
     * @param answeredDate the new answered date
     */
    public void setAnsweredDate(String answeredDate) {

        this.answeredDate = answeredDate;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Gets the certification service.
     * @return the certification service
     */
    public ICertificationService getCertificationService() {

        return certificationService;
    }

    /**
     * Sets the certification service.
     * @param certificationService the new certification service
     */
    public void setCertificationService(ICertificationService certificationService) {

        this.certificationService = certificationService;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public FarmerCropProdAnswers getFilter() {

        return filter;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(FarmerCropProdAnswers filter) {

        this.filter = filter;
    }

    /**
     * Gets the farmer crop prod answers.
     * @return the farmer crop prod answers
     */
    public FarmerCropProdAnswers getFarmerCropProdAnswers() {

        return farmerCropProdAnswers;
    }

    /**
     * Sets the farmer crop prod answers.
     * @param farmerCropProdAnswers the new farmer crop prod answers
     */
    public void setFarmerCropProdAnswers(FarmerCropProdAnswers farmerCropProdAnswers) {

        this.farmerCropProdAnswers = farmerCropProdAnswers;
    }

    /**
     * Gets the farmers question answers.
     * @return the farmers question answers
     */
    public FarmersQuestionAnswers getFarmersQuestionAnswers() {

        return farmersQuestionAnswers;
    }

    /**
     * Sets the farmers question answers.
     * @param farmersQuestionAnswers the new farmers question answers
     */
    public void setFarmersQuestionAnswers(FarmersQuestionAnswers farmersQuestionAnswers) {

        this.farmersQuestionAnswers = farmersQuestionAnswers;
    }

    /**
     * Gets the fields.
     * @return the fields
     */
    public List<String> getFields() {

        return fields;
    }

    /**
     * Sets the fields.
     * @param fields the new fields
     */
    public void setFields(List<String> fields) {

        this.fields = fields;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
	 * Gets the farmers list.
	 * 
	 * @return the farmers list
	 */
	public Map<String, String> getFarmersList() {

		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();

		/*
		 * List<Farmer> farmersList = farmerService.listFarmer(); for (Farmer
		 * obj : farmersList) { farmerListMap.put(obj.getFarmerId(),
		 * obj.getFirstName() + " " + obj.getLastName() + " - " +
		 * obj.getFarmerId()); }
		 */

		List<FarmerCropProdAnswers> fcpList = farmerService.listFarmerwithCategoryCode(CATEGORY_CODE);
		
		for (FarmerCropProdAnswers fcp : fcpList) {
			Farmer farmer = farmerService.findFarmerByFarmerId(fcp.getFarmerId());
			if(farmer!=null){
			farmerListMap.put(String.valueOf(farmer.getFarmerId()), farmer.getName());
			}
		}
		return farmerListMap;

	}

	/**
	 * Gets the farms list.
	 * 
	 * @return the farms list
	 */
	public Map<String, String> getFarmsList() {

		Map<String, String> farmListMap = new LinkedHashMap<String, String>();
		/*
		 * List<Farm> farmList = farmerService.listFarm(); for (Farm farm :
		 * farmList) { farmListMap.put(String.valueOf(farm.getId()),
		 * farm.getFarmName() + " - " + farm.getFarmCode()); }
		 */
		List<FarmerCropProdAnswers> fcpList = farmerService.listFarmerwithCategoryCode(CATEGORY_CODE);
		Farm farm;
		for (FarmerCropProdAnswers fcp : fcpList) {
			farm = farmerService.findFarmById(Long.valueOf(fcp.getFarmId()));
			farmListMap.put(String.valueOf(farm.getId()), farm.getFarmName() + "-" + farm.getFarmCode());
		}
		return farmListMap;

	}
    /**
     * Sets the farm id.
     * @param farmId the new farm id
     */
    public void setFarmId(String farmId) {

        this.farmId = farmId;
    }

    /**
     * Gets the farm id.
     * @return the farm id
     */
    public String getFarmId() {

        return farmId;
    }

    /**
     * Sets the farmer name and id.
     * @param farmerNameAndId the new farmer name and id
     */
    public void setFarmerNameAndId(String farmerNameAndId) {

        this.farmerNameAndId = farmerNameAndId;
    }

    /**
     * Gets the farmer name and id.
     * @return the farmer name and id
     */
    public String getFarmerNameAndId() {

        return farmerNameAndId;
    }

    /**
     * Sets the farm name and id.
     * @param farmNameAndId the new farm name and id
     */
    public void setFarmNameAndId(String farmNameAndId) {

        this.farmNameAndId = farmNameAndId;
    }

    /**
     * Gets the farm name and id.
     * @return the farm name and id
     */
    public String getFarmNameAndId() {

        return farmNameAndId;
    }

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	  public DateFormat getDf() {
			return df;
		}

		public void setDf(DateFormat df) {
			this.df = df;
		}

		public Double getAcresOwned() {
			return acresOwned;
		}

		public void setAcresOwned(Double acresOwned) {
			this.acresOwned = acresOwned;
		}

		public double getAcresOrganic() {
			return acresOrganic;
		}

		public void setAcresOrganic(double acresOrganic) {
			this.acresOrganic = acresOrganic;
		}

		public double getAcresTransition() {
			return acresTransition;
		}

		public void setAcresTransition(double acresTransition) {
			this.acresTransition = acresTransition;
		}

		public double getAcresEligibleNext() {
			return acresEligibleNext;
		}

		public void setAcresEligibleNext(double acresEligibleNext) {
			this.acresEligibleNext = acresEligibleNext;
		}

		public double getAcresReqIns() {
			return acresReqIns;
		}

		public void setAcresReqIns(double acresReqIns) {
			this.acresReqIns = acresReqIns;
		}

		public void setAcresOwned(double acresOwned) {
			this.acresOwned = acresOwned;
		}

		public void setAcresFarmed(double acresFarmed) {
			this.acresFarmed = acresFarmed;
		}


		
		 public InputStream populatePdf() throws DocumentException, IOException {
			 String newLine = "\n";
		        String dashLine = "___________________";
		        Document document = new Document();
		        response.setContentType("application/pdf");
		        response.setHeader("Content-Disposition", "attachment;filename="
		                + getText("export.fileName") + fileNameDateFormat.format(new Date()) + ".pdf");
		        PdfWriter.getInstance(document, response.getOutputStream());
		        farmerCropProdAnswers = certificationService.findFarmerCropProdAnswersById(Long
	                    .valueOf(id));
		        if (!ObjectUtil.isEmpty(farmerCropProdAnswers)) {

		        // title header	        	
		        Font titleFont = null; // font for title text.
		        Paragraph title = null; 
		        
		        	Phrase phraseHeader = new Phrase();
		            PdfPTable exportHeadingTable = new PdfPTable(2);
		            exportHeadingTable.setWidthPercentage(100);
		            exportHeadingTable.setWidths(new int[] {10,10});
		            formHeaderCell(exportHeadingTable,getText("ListFile"), Element.ALIGN_CENTER,
		                    Rectangle.BOX);
		            phraseHeader.add(exportHeadingTable);	

		            System.out.println(document.getPageNumber());
		            loadSectionsForDetail(farmerCropProdAnswers);

		            document.open();
		            
		            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
	                logo.scaleAbsolute(100, 50);
	                logo.setAlignment(Element.ALIGN_CENTER);
	                document.add(logo);

	                titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
	                title = new Paragraph(new Phrase(getText("ExportPDFTitle"), titleFont));
	                title.setAlignment(Element.ALIGN_CENTER);
	                document.add(title);
	                document.add(new Paragraph(new Phrase(" ",
	                        new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK))));
	                	                		            
		            if (!ObjectUtil.isEmpty(farmerCropProdAnswers)) {

		                PdfPTable detailHeaderTable = new PdfPTable(2);
		                detailHeaderTable.setWidths(new int[] { 5, 5 });
		                detailHeaderTable.setWidthPercentage(100);
		                
		             // Table Header
		               	                
		                Farmer farmer = farmerService.findFarmerByFarmerId(farmerCropProdAnswers.getFarmerId());
			            if (!ObjectUtil.isEmpty(farmer)) {
			                farmerNameAndId = farmer.getFirstName() + " - " + farmer.getFarmerId();
			                if (!StringUtil.isEmpty(farmer.getLastName()))
			                    farmerNameAndId = farmer.getFirstName() + " " + farmer.getLastName() + " - "
			                            + farmer.getFarmerId();
			            }
			            
			            // Farmer Name & code
			
			            Font cellFont1 = new Font(FontFamily.HELVETICA, 8, Font.BOLD, GrayColor.GRAYBLACK);
		                cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.farmerNameCode"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (farmerNameAndId), Element.ALIGN_LEFT,
			                    Rectangle.BOX);
			            
			            // Farm Name & Code
			            
			            Farm farm = farmerService.findFarmById(Long.valueOf(farmerCropProdAnswers.getFarmId()));
			            if (!ObjectUtil.isEmpty(farm)) {
			                farmNameAndId = farm.getFarmName() + " - " + farm.getFarmCode();
			            }

			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.farmNameCode"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (farmNameAndId), Element.ALIGN_LEFT,
			                    Rectangle.BOX);

			            //Answered Date
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.answeredDate"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (this.sdf.format(farmerCropProdAnswers.getAnsweredDate())), Element.ALIGN_LEFT, Rectangle.BOX);
			            
			            //Acres Owned
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.acresOwned"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (Double.valueOf(farmerCropProdAnswers.getAcresOwned())), Element.ALIGN_LEFT, Rectangle.BOX);
			            
			           //Acres Farmed
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.acresFarmed"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (Double.valueOf(farmerCropProdAnswers.getAcresFarmed())), Element.ALIGN_LEFT, Rectangle.BOX);
			            
			            //Acres Organic
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.acresOrganic"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (Double.valueOf(farmerCropProdAnswers.getAcresOrganic())), Element.ALIGN_LEFT, Rectangle.BOX);
			            
			            // Acres Transition
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.acresTransition"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (Double.valueOf(farmerCropProdAnswers.getAcresTransition())), Element.ALIGN_LEFT, Rectangle.BOX);
			            
			            //Acres Eligible For Next Certification
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.acresEligibForNextCertification"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (Double.valueOf(farmerCropProdAnswers.getAcresEligibleNext())), Element.ALIGN_LEFT, Rectangle.BOX);
			            
			            //Acres Requested For Organic Inspection
			            cell = new PdfPCell(new Phrase(getText("farmerInspectionNopReport.acresRequestedForOrganicInspection"),cellFont1));		                
		                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		                cell.setBorder(Rectangle.BOX);
		                cell.setNoWrap(false);
		                detailHeaderTable.addCell(cell);
			            formDataCell(detailHeaderTable, (Double.valueOf(farmerCropProdAnswers.getAcresReqIns())), Element.ALIGN_LEFT, Rectangle.BOX);

			            
			            detailHeaderTable.setSpacingAfter(10f);
		                document.add(detailHeaderTable);
		              
		                
		                // SECTION TABLE
			             if (!ObjectUtil.isListEmpty(sections)) {

			                PdfPTable sectionTable = new PdfPTable(6);
			                sectionTable.setWidths(new int[] { 1, 7, 2, 4, 2, 2 });
			                sectionTable.setWidthPercentage(100);

			                PdfPCell cell;
			                Font cellFont5 = new Font(FontFamily.HELVETICA, 8, Font.BOLD, GrayColor.GRAYBLACK);
			                cell = new PdfPCell(new Phrase(getText("sno"), cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);
			                sectionTable.addCell(cell);

			                cell = new PdfPCell(new Phrase(getText("questions"),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);
			                sectionTable.addCell(cell);

			                cell = new PdfPCell(new Phrase(getText("answersLabel"),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);
			                cell.setColspan(4);
			                sectionTable.addCell(cell);

			                cell = new PdfPCell(new Phrase(getText(""),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);
			                cell.setColspan(2);
			                sectionTable.addCell(cell);

			                
			                cell = new PdfPCell(new Phrase(getText("answers"),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);			      
			                sectionTable.addCell(cell);
			                
			                cell = new PdfPCell(new Phrase(getText("nonConformityAction"),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);			 
			                sectionTable.addCell(cell);
			                
			                cell = new PdfPCell(new Phrase(getText("icsPersonal"),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);
			                sectionTable.addCell(cell);
			                
			                cell = new PdfPCell(new Phrase(getText("expectedDeadLine"),cellFont5));
			                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			                cell.setBorder(Rectangle.BOX);
			                cell.setNoWrap(false);
			                sectionTable.addCell(cell);
			                
			               for (FarmersSectionAnswers section : sections) {
			                    if (!ObjectUtil.isEmpty(section)) {  
			                   
					                
					                cell = new PdfPCell(new Phrase(section.getSerialNo(),cellFont5));
					                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					                cell.setBorder(Rectangle.BOX);
					                cell.setNoWrap(false);
					                sectionTable.addCell(cell);
					                
					             	
			                        cell = new PdfPCell(new Phrase(section.getSectionName(),cellFont5));
					                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					                cell.setBorder(Rectangle.BOX);
					                cell.setNoWrap(false);
					                sectionTable.addCell(cell);

			                    	 formDataCell(sectionTable, (""),
		                                        Element.ALIGN_LEFT, Rectangle.BOX);
			                    	 formDataCell(sectionTable, (""),
		                                        Element.ALIGN_LEFT, Rectangle.BOX);
			                    	 formDataCell(sectionTable, (""),
		                                        Element.ALIGN_LEFT, Rectangle.BOX);
			                    	 formDataCell(sectionTable, (""),
		                                        Element.ALIGN_LEFT, Rectangle.BOX);
			       
			                        List<FarmersQuestionAnswers> farmerQuestionList = farmerService.listQuestionBySection(section.getId()); 
			                        for (FarmersQuestionAnswers question : farmerQuestionList) {
			                            if (!ObjectUtil.isEmpty(question)) {
			                            	 formDataCell(sectionTable, (question.getSerialNo()),
			                                        Element.ALIGN_CENTER, Rectangle.BOX);
			                                formDataCell(sectionTable, (question.getQuestionName()),
			                                        Element.ALIGN_LEFT, Rectangle.BOX);
			                            }
			                            if (!ObjectUtil.isListEmpty(question.getAnswers())) {
			                                for (Answers answer : question.getAnswers()) {
			                                    if (!ObjectUtil.isEmpty(answer)) {
			                                    	
			                                    	formDataCell(sectionTable, (getText(answer.getAnswer())),
				                                                Element.ALIGN_LEFT, Rectangle.BOX);
			                                        formDataCell(sectionTable, (answer.getAnswer3()),
			                                                Element.ALIGN_LEFT, Rectangle.BOX);
			                                        formDataCell(sectionTable, (answer.getAnswer4()),
			                                                Element.ALIGN_LEFT, Rectangle.BOX);
			                                        formDataCell(sectionTable, (answer.getAnswer5()),
			                                                Element.ALIGN_LEFT, Rectangle.BOX);
			                                       
			                                    }
			                                }
			                            }
			                        }
			                    } 
			                }
			                sectionTable.setSpacingAfter(10f);
			                document.add(sectionTable);
			                
			            }
		            
		            }
		 }
		        document.close();
				return fileInputStream; }

		private void loadSectionsForDetail(FarmerCropProdAnswers farmerCropProdAnswers) {
			if (!ObjectUtil.isEmpty(farmerCropProdAnswers)) {

	            List<FarmersSectionAnswers> sectionList = certificationService.listsectionByFarmerCropProd(farmerCropProdAnswers.getId());
	            Collections.sort(sectionList);
	            sections  = new TreeSet<FarmersSectionAnswers>(sectionList);
	          //  sections = new HashSet<FarmersSectionAnswers>(sectionList);
	            Set<FarmersSectionAnswers> tempSectionSet = new TreeSet<FarmersSectionAnswers>();
	            tempSectionSet = sections;
	            sections = new LinkedHashSet<FarmersSectionAnswers>();
	            for (FarmersSectionAnswers sectionAnswers : tempSectionSet) {
	                if (!ObjectUtil.isEmpty(sectionAnswers)
	                        && !StringUtil.isEmpty(sectionAnswers.getSectionCode())
	                      ) {
	                    sections.add(sectionAnswers);
	                    
	                } else {
	                    sectionAttributesSet.add(sectionAnswers);
	                }
	            }
	              }
					}

		public InputStream getFileInputStream() {
			return fileInputStream;
		}

		public void setFileInputStream(InputStream fileInputStream) {
			this.fileInputStream = fileInputStream;
		}	 
	    /**
	     * Form data cell.
	     * @param table the table
	     * @param content the content
	     * @param alignment the alignment
	     * @param border the border
	     */
	    private void formDataCell(PdfPTable table, String content, int alignment, int border) {
	        Font cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
			// Normal cell
	        PdfPCell cell = new PdfPCell(new Phrase(content,cellFont));
	        cell.setHorizontalAlignment(alignment);
	        cell.setBorder(border);
	        cell.setNoWrap(false);
	        table.addCell(cell);
	    }

	    /**
	     * Form header cell.
	     * @param table the table
	     * @param content the content
	     * @param alignment the alignment
	     * @param border the border
	     */
	    private void formHeaderCell(PdfPTable table, String content, int alignment, int border) {
	    	  Font cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
	        // PAGE HEADER
	        PdfPCell cell = new PdfPCell(new Phrase(content, cellFont));
	        cell.setHorizontalAlignment(alignment);
	        cell.setBorder(border);
	        cell.setNoWrap(false);
	        table.addCell(cell);
	    }
	    
	    private void formDataCell(PdfPTable table, Double content, int alignment, int border) {
	        Font cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
			// Normal cell
	        PdfPCell cell = new PdfPCell(new Phrase(Double.toString(content),cellFont));
	        cell.setHorizontalAlignment(alignment);
	        cell.setBorder(border);
	        cell.setNoWrap(false);
	        table.addCell(cell);
	    }
	    
	    public String populateXLS() throws Exception {
			InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
			setXlsFileName(getText("farmerInspectionReport") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("farmerInspectionReport"), fileMap, ".xls"));
			return "xls";
		}
		
		
		@SuppressWarnings("unchecked")
		private InputStream getExportDataStream(String exportType)throws IOException {
			setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
			boolean flag = true;
			DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
			DecimalFormat df = new DecimalFormat("0.000");
			DecimalFormat df1 = new DecimalFormat("0.00");

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(getLocaleProperty("farmerInspectionReport"));
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

			HSSFRow row, titleRow, filterRowTitle, filterRow1,filterRow2,filterRow3,filterRow4,filterRow5;
			HSSFCell cell;

			int imgRow1 = 0;
			int imgRow2 = 4;
			int imgCol1 = 0;
			int imgCol2 = 1;
			int titleRow1 = 2;
			int titleRow2 = 5;

			int rowNum = 2;
			int colNum = 0;

			branchIdValue = getBranchId(); // set value for branch id.
			buildBranchMap(); // build branch map to get branch name form branch id.

			sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

			sheet.setDefaultColumnWidth(13);

			titleRow = sheet.createRow(rowNum++);
			cell = titleRow.createCell(2);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerInspectionNopReportTitle")));
			cell.setCellStyle(style1);
			font1.setBoldweight((short) 22);
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style1.setFont(font1);
			
			
			farmerId = filter.getFarmerId();
			farmId = filter.getFarmId();
			
			FarmerCropProdAnswers farmerCropProdAnswers = new FarmerCropProdAnswers();
			farmerCropProdAnswers.setCategoryCode(CATEGORY_CODE);
			this.filter = farmerCropProdAnswers;
			
			
			super.filter = this.filter;
			
			
			if (!StringUtil.isEmpty(farmerId)) {
				this.filter.setFarmerId(farmerId);
			}
			if (!StringUtil.isEmpty(farmId)) {
				this.filter.setFarmId(farmId);
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
				
				
				 if (!StringUtil.isEmpty(farmerId)) {

		                filterRow3 = sheet.createRow(rowNum++);

		                cell = filterRow3.createCell(1);
		                cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerId")));
		                filterFont.setBoldweight((short) 12);
		                filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                filterStyle.setFont(filterFont);
		                cell.setCellStyle(filterStyle);

		                cell = filterRow3.createCell(2);
		                Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
		                cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farmer) ? farmer.getFirstName() : ""));
		                filterFont.setBoldweight((short) 12);
		                filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                filterStyle.setFont(filterFont);
		                cell.setCellStyle(filterStyle);
		            }
				 
				 if (!StringUtil.isEmpty(farmId)) {

		                filterRow3 = sheet.createRow(rowNum++);

		                cell = filterRow3.createCell(1);
		                cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmId")));
		                filterFont.setBoldweight((short) 12);
		                filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                filterStyle.setFont(filterFont);
		                cell.setCellStyle(filterStyle);

		                cell = filterRow3.createCell(2);
		                Farm farm = farmerService.findFarmById(Long.valueOf(farmId));
		                cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(farm) ? farm.getFarmName() : ""));
		                filterFont.setBoldweight((short) 12);
		                filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                filterStyle.setFont(filterFont);
		                cell.setCellStyle(filterStyle);
		            }
				 
				 if (!StringUtil.isEmpty(branchIdParma)) {

		                filterRow3 = sheet.createRow(rowNum++);

		                cell = filterRow3.createCell(1);
		                cell.setCellValue(new HSSFRichTextString(getLocaleProperty("branchId")));
		                filterFont.setBoldweight((short) 12);
		                filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                filterStyle.setFont(filterFont);
		                cell.setCellStyle(filterStyle);

		                cell = filterRow3.createCell(2);
		                BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
		                cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(branch) ? branch.getName() : ""));
		                filterFont.setBoldweight((short) 12);
		                filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		                filterStyle.setFont(filterFont);
		                cell.setCellStyle(filterStyle);
		            }
				
			}
			
			Map data = isMailExport() ? readData() : readExportData();
			
		
			
			rowNum++;
			
			ESESystem preferences = preferncesService.findPrefernceById("1");
			setFarmerCodeEnabled("0");
			if (!StringUtil.isEmpty(preferences)) {
				if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
					setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
				}
			}
			
			rowNum++;
			
			HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
			String mainGridColumnHeaders = "";
			if(farmerCodeEnabled.equalsIgnoreCase("1")){
				mainGridColumnHeaders = getLocaleProperty("farmerInspectionNopFarmerCode");
			}else{
				mainGridColumnHeaders = getLocaleProperty("farmerInspectionNop");
			}
			
			
			int mainGridIterator = 0;
			
			 for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

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
			 
			
			 List<FarmerCropProdAnswers> mainGridRows = (List<FarmerCropProdAnswers>) data.get(ROWS);
				if (ObjectUtil.isListEmpty(mainGridRows))
					return null;
				
				for(FarmerCropProdAnswers farmerCropProdAns : mainGridRows){
					row = sheet.createRow(rowNum++);
					colNum = 0;
					
					DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
					
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(farmerCropProdAns.getBranchId())));
					}
					
					if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(farmerCropProdAns.getFarmerId()));
					}
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(farmerCropProdAns.getFarmerName()) ? farmerCropProdAns.getFarmerName() : getText("NA")));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(farmerCropProdAns.getFarmCode()) ? farmerCropProdAns.getFarmCode() : getText("NA")));
					
			
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!StringUtil.isEmpty(farmerCropProdAns.getFarmName()) ? farmerCropProdAns.getFarmName() : getText("NA")));
					

					
					if (!ObjectUtil.isEmpty(preferences) && !ObjectUtil.isEmpty(farmerCropProdAns.getAnsweredDate())) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(genDate.format(farmerCropProdAns.getAnsweredDate())));
						
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
			String fileName = getLocaleProperty("farmerInspectionReportTitle") + fileNameDateFormat.format(new Date()) + ".xls";
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

		public String getXlsFileName() {
			return xlsFileName;
		}

		public void setXlsFileName(String xlsFileName) {
			this.xlsFileName = xlsFileName;
		}

		public IPreferencesService getPreferncesService() {
			return preferncesService;
		}

		public void setPreferncesService(IPreferencesService preferncesService) {
			this.preferncesService = preferncesService;
		}

		public String getFarmerCodeEnabled() {
			return farmerCodeEnabled;
		}

		public void setFarmerCodeEnabled(String farmerCodeEnabled) {
			this.farmerCodeEnabled = farmerCodeEnabled;
		}

}

