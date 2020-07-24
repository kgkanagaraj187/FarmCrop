package com.ese.service.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.StringUtil;

public class MysqlUtility {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/eses_kpf";

    // Database credentials
    static final String USER = "sourcetrace";
    static final String PASS = "Sourcetrace@123";

    static Connection conn = null;
    static Statement stmt = null;

    public static Integer getFarmerIdSeq() throws SQLException {
        Integer farmerId = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT WEB_SEQ FROM farmer_id_seq";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                farmerId = Integer.parseInt(rs.getString(1));
             }else{
                 farmerId = 0;
             }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++farmerId;
    }
public static Integer getFarmPrimaryKeyId(){

    Integer farmId=0;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        conn = null;
        stmt = null;
        
        // STEP 3: Open a connection
         
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // STEP 4: Execute a query
         
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT MAX(ID) from farm";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
        	try{
            farmId = Integer.parseInt(rs.getString(1));
        	}
        	catch (Exception e) {
				farmId=0;
			}
         }else{
             farmId = 0;
         }
        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ++farmId;

}
    public static Integer getFarmIdSeq() throws SQLException {
        Integer farmId=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT WEB_SEQ from farm_id_seq";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                farmId = Integer.parseInt(rs.getString(1));
             }else{
                 farmId = 0;
             }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++farmId;
    }
    
    public static Integer getFarmDetailedInfoIdSeq() throws SQLException {
        Integer farmDetailSeq=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT MAX(ID) FROM farm_detailed_info";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
            	try{
                farmDetailSeq = Integer.parseInt(rs.getString(1));
            	}catch (Exception e) {
            		 farmDetailSeq = 0;
				}
            }else{
                farmDetailSeq = 0;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++farmDetailSeq;
    }
    
    public static Integer getAccountSeq() throws SQLException {
        String accSeq="";
        Integer seq=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT MAX(ACC_NO) FROM ese_account WHERE ACC_TYPE='SB'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                accSeq = rs.getString(1);
            }else{
                accSeq = "0";
            }
            seq = Integer.parseInt(accSeq.substring(accSeq.length()-5));
            System.out.println("*********SEQ"+seq);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++seq;
    }
    

    public static void Insert(StringBuilder query){
        try {
            String[] myArray = query.toString().split("\\n");
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            
            for(int i=0 ; i<= myArray.length; i++){
                int rs;
                if(!StringUtil.isEmpty(myArray[i]))
                    rs =  stmt.executeUpdate(myArray[i].toString().trim());
            }
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void InsertString(String query){
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            
           int rs =  stmt.executeUpdate(query.trim());
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void update(String query){
        try {
//            String[] myArray = query.toString().split("\\n");
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            
            //for(int i=0 ; i<= myArray.length; i++){
                int rs;
                //if(!StringUtil.isEmpty(myArray[i]))
                    rs =  stmt.executeUpdate(query);
            //}
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Boolean isVillageExist(String village,String grampanchayat, String city){
        Boolean returnValue = Boolean.FALSE;
        String farmerId="";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "Select * from village v where v.NAME='"+village+"'and v.CITY_ID=(SELECT id from city where name='"+city+"') and v.GRAM_PANCHAYAT_ID=(SELECT id from gram_panchayat where name='"+grampanchayat+"');";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               returnValue = Boolean.TRUE;
            }else{
                farmerId = "0";
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    
    public static Boolean isCityExist(String city, String location){
        Boolean returnValue = Boolean.FALSE;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "Select * from city c where c.NAME='"+city+"'and c.LOCATION_ID=(SELECT id from location where name='"+location+"');";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               returnValue = Boolean.TRUE;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    
    public static Boolean isGramPanchayatExist(String gramPanchayat, String city){
        Boolean returnValue = Boolean.FALSE;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "Select * from gram_panchayat g where g.NAME='"+gramPanchayat+"'and g.CITY_ID=(SELECT id from city where name='"+city+"');";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               returnValue = Boolean.TRUE;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    
    public static String getOutPutPath(String filePath){
        return filePath.substring(0, filePath.lastIndexOf( '\\' ));
    }
    
    /*@Test
    public void sampleTest(){
        getOutPutPath("C:\\Users\\admin\\Desktop\\PCPC\\MasterData\\Format.xls");
    }*/
    
    public static String getFilePath(){
        Scanner sc = new Scanner(System.in);
        String filePath = sc.next();
        return filePath;
    }
    public static Integer getContractSeq() throws SQLException {
        String accSeq="";
        Integer seq=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT MAX(CONTRACT_NUMBER) FROM contract";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                accSeq = rs.getString(1);
            }else{
                accSeq = "0";
            }
            seq = Integer.parseInt(accSeq.substring(accSeq.length()-5));
            System.out.println("*********SEQ"+seq);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++seq;
    }
    
    public static Integer getCardSeq() throws SQLException {
        String accSeq="";
        Integer seq=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT SEQ_VAL FROM ese_seq WHERE SEQ_KEY='FARMER_CARD_ID_SEQ'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                accSeq = rs.getString(1);
            }else{
                accSeq = "0";
            }
            seq = Integer.parseInt(accSeq);
            System.out.println("*********SEQ"+seq);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++seq;
    }
    
    public static Integer getEseAccSeq() throws SQLException {
        String accSeq="";
        Integer seq=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT SEQ_VAL FROM ese_seq WHERE SEQ_KEY='FARMER_ACCOUNT_NO_SEQ'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                accSeq = rs.getString(1);
            }else{
                accSeq = "0";
            }
            seq = Integer.parseInt(accSeq);
            System.out.println("*********SEQ"+seq);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++seq;
    }
    
    public static String findVillageIdByName(String name,String city){
        Boolean returnValue = Boolean.FALSE;
        String villageId="";
        String villageQuery="INSERT INTO VILLAGE(NAME,CITY_ID,REVISION_NO,BRANCH_ID,CODE) VALUES(";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "Select * from village v where v.NAME='"+name.trim()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               returnValue = Boolean.TRUE;
               villageId=rs.getString(1);
            }else{
                villageId = "0";
            }
            if(!returnValue){
                StringBuilder sb=new StringBuilder();
                sb.append(villageQuery);
                sb.append("'"+name.trim()+"',");
                sb.append("(SELECT ID FROM CITY WHERE NAME='"+city.toLowerCase().trim()+"'),");
                sb.append("'"+DateUtil.getRevisionNumber()+"',");
                sb.append("'KPF2',");
                sb.append("'V00"+getVillageSeq()+"'");
                sb.append(")");
           //    System.out.println(sb);
                InsertString(sb.toString());
                StringBuilder update = new StringBuilder();
                update.append("UPDATE ESE_SEQ set SEQ_VAL='"+getVillageSeq()+"' where SEQ_KEY='VILLAGE_CODE_SEQ'");
                update(update.toString());
                //System.out.println(update);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return villageId;
    }
    
    public static Integer getVillageSeq() throws SQLException {
        String accSeq="";
        Integer seq=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            // STEP 3: Open a connection
             
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
             
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT SEQ_VAL FROM ese_seq WHERE SEQ_KEY='VILLAGE_CODE_SEQ'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                accSeq = rs.getString(1);
            }else{
                accSeq = "0";
            }
            seq = Integer.parseInt(accSeq);
            System.out.println("*********SEQ"+seq);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ++seq;
    }
    
    public static String getCityIdByVillage(String name){
        String villageId="";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "Select CITY_ID from village v where v.NAME='"+name.trim()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               villageId=rs.getString(1);
            }else{
                villageId = "0";
            }
          
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return villageId;
    }
    public static String findCatalougeValueCodeByName(String name) {
        String villageId="";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            stmt = null;
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "Select CODE from catalogue_value v where v.NAME='"+name.trim()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               villageId=rs.getString(1);
            }else{
                villageId = "0";
            }
          
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return villageId;
    }
    
}
