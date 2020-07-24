package com.ese.service.report;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;

public class ProductMasterQueryGenarator {
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String args[]) throws IOException, SQLException {
	        
	        System.out.println("Enter File Path");
	   
	        String fileName=MysqlUtility.getFilePath()+"\\Farmer.xls";
	        FileInputStream myInput = new FileInputStream(fileName);
	        FileOutputStream myOutput = new FileOutputStream(
	                "D:\\santhosh-Aditi-pro-doc\\New_folder\\New\\ProductQuery_"
	                + fileNameDateFormat.format(new Date()) + ".sql");
	      
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();

	        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
	        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
	        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

	        StringBuilder sb = new StringBuilder();
	        String initialQuery = "INSERT INTO product(ID,NAME,CODE,SUB_CATEGORY_ID,UNIT,PRICE,REVISION_NO) VALUES(NULL,";
	        int i = 1;  
	        int rowCount = mySheet.getLastRowNum();
	        System.out.println("*******ROW COUNT***********"+rowCount);
	         
	       
	        try {
	          
	            while (i <= rowCount) {

	                HSSFRow myRow = mySheet.getRow(i);
	                String name= null;
		               name =!ObjectUtil.isEmpty(myRow.getCell(1))?myRow.getCell(1).getStringCellValue().trim():"";
		               String code =!ObjectUtil.isEmpty(myRow.getCell(0))?myRow.getCell(0).getStringCellValue().trim():"";
		               String subQuery="(SELECT ID FROM sub_category WHERE CODE=";
		               String subCategoryCode=!ObjectUtil.isEmpty(myRow.getCell(2))?myRow.getCell(2).getStringCellValue().trim():null;
		               String price=!ObjectUtil.isEmpty(myRow.getCell(3))?myRow.getCell(3).getStringCellValue().trim():null;
		               long revisionNo=DateUtil.getRevisionNumber();
		               if(code!=""){
			            sb.append(initialQuery);
			            sb.append("'"+name+"',");
			            sb.append("'"+code+"',");
			            sb.append(subQuery);
			            sb.append("'"+subCategoryCode+"'),");
			            sb.append("'1-Unit',");
			            sb.append(price);
			            sb.append(",'"+revisionNo+"'");
			            sb.append(");");
	                	sb.append("\n");
		               }
	           i++;
	            
	           
	            }
	            baos.write(sb.toString().getBytes());
	            myOutput.write(baos.toByteArray());
	            System.out.println("------Product Master Generated Successfully-------");
	        } catch (Exception e) {
	            System.out.println("Exception:" + e);
	            System.out.println("----------ROW_NO:" + i + "-----------");
	            e.printStackTrace();
	        }

	    
	    }
}
