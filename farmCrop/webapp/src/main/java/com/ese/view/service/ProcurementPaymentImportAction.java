package com.ese.view.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sourcetrace.eses.dao.IAccountDAO;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.service.ExcelImportDetail;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
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
public class ProcurementPaymentImportAction extends SwitchValidatorAction {
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
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IProductService productService;
	@Autowired
	private IAccountDAO accountDAO;

	private static final int CELL_TYPE_BLANK = 3;
	private static final String STREAM = "stream";
	private ESEAccount account;
	private double intBalance;
	private double txnAmount;
	private double balAmount;
	public String fileUpload() {

		request.setAttribute(HEADING, getText("create.page"));
		return INPUT;
	}

	@Override
	public Object getData() {

		return null;
	}

	 public List<String> formFarmCropXlsHeaders()
	  {
	    List<String> farmCropHeader = new LinkedList();
	    farmCropHeader.add("CROP_FARM_CODE");
	    farmCropHeader.add("CROP_CATEGORY");
	    farmCropHeader.add("SEASON");
	    farmCropHeader.add("CULTIVATION_TYPE");
	    farmCropHeader.add("CROP_NAME");
	    farmCropHeader.add("VARIETY");
	    farmCropHeader.add("SOWING_DATE");
	    farmCropHeader.add("TYPE");
	    farmCropHeader.add("SEED_SOURCE");
	    farmCropHeader.add("STAPLE_LEN");
	    farmCropHeader.add("SEED_QUANTITY_USED");
	    farmCropHeader.add("SEED_QUANTITY_COST");
	    farmCropHeader.add("ESTIMATED_YIELD_CROPS");
	    return farmCropHeader;
	  }
	  
	 @SuppressWarnings("resource")
	public String processProcurement() throws IOException {
	    
	   
	    try {
	      String filePath = request.getSession().getServletContext().getRealPath("/");
	      File fileToCreate = new File(filePath, uploadFileFileName);
	      if (ObjectUtil.isEmpty(fileToCreate)) {
	        throw new FileNotFoundException();
	      }
	      FileUtils.copyFile(uploadFile, fileToCreate);
	      FileReader filereader = new FileReader(fileToCreate);       
	 // create csvParser object with 
			// custom seperator semi-colon 
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build(); 

			// create csvReader object with parameter 
			// filereader and parser 
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).withCSVParser(parser).build(); 
			
			// Read all data at once 
			List<String[]> allData = csvReader.readAll(); 

			csvReader.close();

			    for (int row =0; row<allData.size(); row++){
			    
			    		  try{
			    			  for (String[] rows : allData) { 
					  				String reciptNo = !StringUtil.isEmpty(rows[0]) ? String.valueOf(rows[0]) : "";
					  		        
					  		        String txnDate = !StringUtil.isEmpty(rows[1]) ? String.valueOf(rows[1]) : "";
					  		        
					  		        String msisdn = !StringUtil.isEmpty(rows[9]) ? String.valueOf(rows[9]) : "";
					  		        
					  		       // String farmerName = !StringUtil.isEmpty(row[10]) ? String.valueOf(row[10]) : "";
					  		        
					  		        String txnAmt = !StringUtil.isEmpty(rows[13]) ? String.valueOf(rows[13]) : "";
					  		        String[]tokens = msisdn.split(":|\\/");
					  			
					  		
					  		      List<AgroTransaction> existing = productDistributionService.findAgroTxnByReceiptNo(reciptNo);
					  		    if (!ObjectUtil.isListEmpty(existing)) {
					  		    	 addActionError("Receipt No : "+reciptNo+" Already Exist  | Error found on row " +csvReader.getLinesRead());
					  		    	return INPUT;
					  		    }else{
					  		    	 AgroTransaction txn = new AgroTransaction();
							  	       txn.setDeviceId("N/A");
							  	       txn.setDeviceName("N/A");
							  	       txn.setBranch_id(getBranchId());
							  	       if (!StringUtil.isEmpty(txnDate)) {
							  	         try {
							  	           txn.setTxnTime(DateUtil.convertStringToDate(String.valueOf(txnDate), getGeneralDateFormat()));
							  	         }
							  	         catch (Exception e) {
							  	           String date = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
							  	           txn.setTxnTime(DateUtil.convertStringToDate(date, getGeneralDateFormat()));
							  	         }
							  	       }
							  	       
							  	       txn.setTxnType("334P");
							  	       txn.setProfType("02");
							  	       txn.setOperType(1);
							  	       txn.setTxnDesc("PROCUREMENT PAYMENT");
							  	       txn.setSeasonCode(getCurrentSeasonsCode());
							  	       if(!StringUtil.isEmpty(tokens[1])){
							  	    	   Farmer farmer= farmerService.findFarmerByMSISDN(tokens[1]);
							  	    	   if (!ObjectUtil.isEmpty(farmer)) {
							  	  	         txn.setFarmerId(farmer.getFarmerId());
							  	  	         txn.setFarmerName(!StringUtil.isEmpty(farmer.getLastName()) ? farmer.getFirstName() + " " + farmer.getLastName() : farmer.getFirstName());
							  	  	         ESEAccount farmerAccount = productDistributionDAO.findESEAccountByProfileId(farmer.getFarmerId(), 3);
								  		        if(!ObjectUtil.isEmpty(farmerAccount)){
								  		        	 Double bal = Double.valueOf(farmerAccount.getCashBalance());
									  		         txn.setIntBalance(bal.doubleValue());
									  		         txn.setTxnAmount(StringUtil.isDouble(Double.valueOf(txnAmt)) ? Double.valueOf(txnAmt).doubleValue() : 0.0D);
									  		         txn.setBalAmount(bal.doubleValue() + Double.valueOf(txnAmt));
									  		         txn.setAccount(farmerAccount);
								  		        } else {
								  		        	 addActionError("No Farmer Account Found | Error found on row " +csvReader.getLinesRead());
										  		    	return INPUT;
										  	       }	      
							  	  	        
							  	    	   }else{
							  	    		 addActionError("No Farmer mapped with "+tokens[1]+" MSISDN Number | Error found on row " +csvReader.getLinesRead());
								  		    	return INPUT;
							  	    	   }
							  	    	   txn.setReceiptNo(reciptNo);	
								  	       txn.calculateBalance(txn.getAccount(), txn.getTxnAmount(), true,
								  					false);
								  	       productDistributionService.saveAgroTransaction(txn);
								  	       if (!StringUtil.isEmpty(txn.getBalAmount())) {
								  				accountDAO.updateESEAccountCashBalById(txn.getAccount().getId(),
								  						txn.getBalAmount());
								  			}
							  	    	   
							  	    	   }else{
							  	    		 addActionError("MSISDN Number :"+tokens[1]+" Not found  | Error found on row " +csvReader.getLinesRead());
								  		    	return INPUT;
								  	    	   }
							  	  
							  	       
					  		    }
					  	      
					  	      }
			    		  }catch (Exception e) {
			    			  addActionError("Error found on row " +csvReader.getLinesRead());				  		    
			    			  e.printStackTrace();
			    		    }
			    		  
			    		  
			    		  
			 
			    	  
			    }
			
	    } catch (Exception e) {
	     
	    	e.printStackTrace();
	    }
	    addActionMessage("Import Successfully");
	    return INPUT;
	  }
	 @SuppressWarnings("unchecked")
		protected JSONObject getJSONObject(Object code, Object name) {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", code);
			jsonObject.put("name", name);
			return jsonObject;
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
    public void calculateBalance(ESEAccount account, double txnAmount,
            boolean isProcurementBalance, boolean isCredit) {

        this.account = account;
        if (!ObjectUtil.isEmpty(this.account)) {
            if (isProcurementBalance) {
                calculateBalance(this.account.getCashBalance(), txnAmount, isCredit);
                this.account.setCashBalance(this.balAmount);
            } else {
                calculateBalance(this.account.getCashBalance(), txnAmount, isCredit);
                this.account.setCashBalance(this.balAmount);
            }
        } else {
            calculateBalance(this.intBalance, txnAmount, isCredit);
        }
    }

	public ESEAccount getAccount() {
		return account;
	}

	public void setAccount(ESEAccount account) {
		this.account = account;
	}
	public void calculateBalance(double initBalance, double txnAmount, boolean isCredit) {

        this.intBalance = initBalance;
        this.txnAmount = txnAmount;
        this.balAmount = isCredit ? (this.intBalance + this.txnAmount)
                : (this.intBalance - this.txnAmount);
    }
}
