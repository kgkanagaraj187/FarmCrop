package com.sourcetrace.ws.txn.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;

public class FarmerEnrollmentTest {

    private static Logger LOGGER = Logger.getLogger(FarmerEnrollmentTest.class);

    private static final String LOCALHOST = "http://localhost:8090/tserv/rs";
    private static final String format = "application/json";

    /**
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

        WebClient client = WebClient.create(LOCALHOST);
        client.path("processTxnRequest").accept(format).type(format);
        Request request = new Request();
        Head head = new Head();
        head.setVersionNo("1.00");
        head.setSerialNo("8c40dac151c1731f6377fe5b51d043c6");
        head.setAgentId("111222");
        head.setAgentToken("25D08C028FE308B6E2EF598F18AC28D3");
        head.setServPointId("SP001");
        head.setTxnTime(sdf.format(new Date()));
        head.setTxnType(TransactionTypeProperties.FARMER_ENROLLMENT);
        head.setOperType(TransactionProperties.REGULAR_TXN);
        head.setMode(TransactionProperties.ONLINE_MODE);
        head.setResentCount("0");
        head.setMsgNo("201");
        head.setTenantId("awi");

        Map dataMap = new HashMap();

        //dataMap.put(TxnEnrollmentProperties.FARMER_ID, String.valueOf(System.currentTimeMillis()));
        dataMap.put(TxnEnrollmentProperties.FARMER_ID, "555");
        dataMap.put(TxnEnrollmentProperties.FARMER_CODE, "12345");
        dataMap.put(TxnEnrollmentProperties.DOJ, "20130916");
        dataMap.put(TxnEnrollmentProperties.ENROLLMENT_PLACE,"5");
        dataMap.put(TxnEnrollmentProperties.ENROLLMENT_PLACE_OTHER, "other");        
        dataMap.put(TxnEnrollmentProperties.FIRST_NAME,"Test");
        dataMap.put(TxnEnrollmentProperties.GENDER, "male");
        dataMap.put(TxnEnrollmentProperties.LAST_NAME, "Kumar");
        
        dataMap.put(TxnEnrollmentProperties.DOB, "20150828");
        dataMap.put(TxnEnrollmentProperties.MARITAL_STATUS, "2");
        dataMap.put(TxnEnrollmentProperties.ADULT_COUNT_MALE, "3");
        dataMap.put(TxnEnrollmentProperties.CHILD_COUNT_MALE,"4");
        dataMap.put(TxnEnrollmentProperties.ADULT_COUNT_FEMALE, "3");
        dataMap.put(TxnEnrollmentProperties.CHILD_COUNT_FEMALE,"4");
        dataMap.put(TxnEnrollmentProperties.FARMER_ANNUAL_INCOME, "30000");
        dataMap.put(TxnEnrollmentProperties.EDUCATION, "1");
        dataMap.put(TxnEnrollmentProperties.ADDRESS, "Address1");
        dataMap.put(TxnEnrollmentProperties.VILLAGE_CODE, "V00009");
        dataMap.put(TxnEnrollmentProperties.SAMITHI_CODE, "G00003");
        dataMap.put(TxnEnrollmentProperties.PHONE_NO, "0422344441");
        dataMap.put(TxnEnrollmentProperties.MOBILE_NO, "9123456782");
        dataMap.put(TxnEnrollmentProperties.EMAIL, "sts@gmail.com");
        dataMap.put(TxnEnrollmentProperties.LATITUDE, "11.001432");
        dataMap.put(TxnEnrollmentProperties.LONGITUDE, "76.973538");
        dataMap.put(TxnEnrollmentProperties.FARMER_CARD_ID, "001");
        dataMap.put(TxnEnrollmentProperties.FARMER_ACCOUNT_NO, "5868767867687");
        dataMap.put(TxnEnrollmentProperties.FARMER_CERTIFIED,"1"); 
        dataMap.put(TxnEnrollmentProperties.FARMER_CERTIFICATION_TYPE, "1");        
        dataMap.put(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME, sdf.format(new Date()));
        dataMap.put(TxnEnrollmentProperties.FARMER_HOUSE_OWNERSHIP,"1");
        dataMap.put(TxnEnrollmentProperties.FARMER_HOUSE_TYPE,"5");
        dataMap.put(TxnEnrollmentProperties.FARMER_HOUSE_TYPE_OTHER,"7");
        dataMap.put(TxnEnrollmentProperties.CURRENT_SEASON_CODE, "HS00001");
        // Farmer Family
        Collection familyCollection = new Collection();
        List<Object> familyObjectList = new ArrayList<Object>();

        Data familyName = new Data();
        familyName.setKey(TxnEnrollmentProperties.FAMILY_NAME);
        familyName.setValue("B");
        
        Data age = new Data();
        age.setKey(TxnEnrollmentProperties.FAMILY_AGE);
        age.setValue("22");
        
        Data familyGender = new Data();
        familyGender.setKey(TxnEnrollmentProperties.FAMILY_GENDER);
        familyGender.setValue("MALE");

        Data relation = new Data();
        relation.setKey(TxnEnrollmentProperties.FAMILY_REALTION);
        relation.setValue("1");
        
        Data education = new Data();
        education.setKey(TxnEnrollmentProperties.FAMILY_EDUCATION);
        education.setValue("2");

        Data profession = new Data();
        profession.setKey(TxnEnrollmentProperties.PROFESSION);
        profession.setValue("1");

        Data oterProfession = new Data();
        oterProfession.setKey(TxnEnrollmentProperties.OTHER_PROFESSION);
        oterProfession.setValue("Other Profession");

       
        List<Data> listOfFamilyData = new ArrayList<Data>();
        listOfFamilyData.add(familyName);
        listOfFamilyData.add(age);
        listOfFamilyData.add(familyGender);
        listOfFamilyData.add(relation);
        listOfFamilyData.add(education);
        listOfFamilyData.add(profession);
        listOfFamilyData.add(oterProfession);
      
        Object familyObj = new Object();
        familyObj.setData(listOfFamilyData);

        familyObjectList.add(familyObj);

        familyCollection.setObject(familyObjectList);
        dataMap.put(TxnEnrollmentProperties.FAMILY_MEMBERS, familyCollection);
        
        List<Data> dataList = CollectionUtil.mapToList(dataMap);
        
        
        Data farmerPhoto = new Data();
        FileDataSource dataSource = new FileDataSource(new File("C:\\Users\\admin\\Desktop\\STS Doc\\images\\1.jpg"));
        DataHandler dataHandler = new DataHandler(dataSource);
        farmerPhoto.setKey(TxnEnrollmentProperties.PHOTO);
        farmerPhoto.setBinaryValue(dataHandler);
        dataList.add(farmerPhoto);
        
        // FARM
        Collection farmCollection = new Collection();
        List<Object> farmObjectList = new ArrayList<Object>();

        Data code = new Data();
        code.setKey(TxnEnrollmentProperties.FARM_CODE);
        code.setValue(String.valueOf(System.currentTimeMillis()));

        Data name = new Data();
        name.setKey(TxnEnrollmentProperties.FARM_NAME);
        name.setValue("Farm Name New");

        Data surveyNo = new Data();
        surveyNo.setKey(TxnEnrollmentProperties.SURVEY_NUMBER);
        surveyNo.setValue("1000");
        
        Data isSameAddress = new Data();
        isSameAddress.setKey(TxnEnrollmentProperties.IS_SAME_AS_FARMER_ADDRESS);
        isSameAddress.setValue("1");
        
        Data totalLandHoldingArea = new Data();
        totalLandHoldingArea.setKey(TxnEnrollmentProperties.TOTAL_LAND_HOLDING_AREA);
        totalLandHoldingArea.setValue("9");
        
        Data proposedPlantnigArea = new Data();
        proposedPlantnigArea.setKey(TxnEnrollmentProperties.PROPOSED_PLANTING_AREA);
        proposedPlantnigArea.setValue("9");
        
        Data farmOwned = new Data();
        farmOwned.setKey(TxnEnrollmentProperties.FARM_OWNED);
        farmOwned.setValue("2");

        Data landGradient = new Data();
        landGradient.setKey(TxnEnrollmentProperties.LAND_GRADIENT);
        landGradient.setValue("1");

        Data approachRoad = new Data();
        approachRoad.setKey(TxnEnrollmentProperties.APPROACH_ROAD);
        approachRoad.setValue("1");
        
        Data regYear = new Data();
        regYear.setKey(TxnEnrollmentProperties.REGISTRATION_YEAR);
        regYear.setValue("2015-2016");
        
        Data farmPhoto = new Data();
        FileDataSource _dataSource = new FileDataSource(new File("C:\\Users\\admin\\Desktop\\STS Doc\\images\\1.jpg"));
        DataHandler _dataHandler = new DataHandler(_dataSource);
        farmPhoto.setKey(TxnEnrollmentProperties.FARM_PHOTO);
        farmPhoto.setBinaryValue(_dataHandler);

        Data farmPhotoCapturingDate = new Data();
        farmPhotoCapturingDate.setKey(TxnEnrollmentProperties.FARM_PHOTO_CAPTURING_TIME);
        farmPhotoCapturingDate.setValue("2013-10-23 01:54:10");
        
        Data farmLatitudee = new Data();
        farmLatitudee.setKey(TxnEnrollmentProperties.FARM_LATITUDE);
        farmLatitudee.setValue("11.001432");

        Data farmLongitude = new Data();
        farmLongitude.setKey(TxnEnrollmentProperties.FARM_LONGITUDE);
        farmLongitude.setValue("76.973538");
        
        Collection landAreaCollection = new Collection();
        List<Object> landAreaObjectList = new ArrayList<Object>();
        
       
        Data farmLatitude = new Data();
        farmLatitude.setKey(TxnEnrollmentProperties.FARM_LAND_AREA_LATITUDE);
        farmLatitude.setValue("11.001432");
        dataList.add(farmLatitude);
        farmLatitude = new Data();
        farmLatitude.setKey(TxnEnrollmentProperties.FARM_LAND_AREA_LONGITUDE);
        farmLatitude.setValue("77.001432");
        dataList.add(farmLatitude);
        Object landObj = new Object();
        landObj.setData(dataList);
        landAreaObjectList.add(landObj);
        
        dataList = new ArrayList<Data>();
        farmLatitude = new Data();
        farmLatitude.setKey(TxnEnrollmentProperties.FARM_LAND_AREA_LATITUDE);
        farmLatitude.setValue("12.001432");
        dataList.add(farmLatitude);
        farmLatitude = new Data();
        farmLatitude.setKey(TxnEnrollmentProperties.FARM_LAND_AREA_LONGITUDE);
        farmLatitude.setValue("76.001432");
        dataList.add(farmLatitude);
        landObj = new Object();
        landObj.setData(dataList);
        landAreaObjectList.add(landObj);
        
        landAreaCollection.setObject(landAreaObjectList);
        Data landAreaCollectionData = new Data();
        landAreaCollectionData.setKey(TxnEnrollmentProperties.FARM_LAND_AREA_GPS);
        landAreaCollectionData.setCollectionValue(landAreaCollection);
        
        Data soilType = new Data();
        soilType.setKey(TxnEnrollmentProperties.SOIL_TYPE);
        soilType.setValue("1");

        Data soilTexture = new Data();
        soilTexture.setKey(TxnEnrollmentProperties.SOIL_TEXTURE);
        soilTexture.setValue("2");

        Data fertilityStatus = new Data();
        fertilityStatus.setKey(TxnEnrollmentProperties.FERTILITY_STATUS);
        fertilityStatus.setValue("2");

        Data farmIrrigarion = new Data();
        farmIrrigarion.setKey(TxnEnrollmentProperties.IRRIGATION_SOURCEZ);
        farmIrrigarion.setValue("2");
        
        Data irrigationType = new Data();
        irrigationType.setKey(TxnEnrollmentProperties.IRRIGATION_SOURCE_TYPES);
        irrigationType.setValue("2");
        
        Data irrigationTypeOther = new Data();
        irrigationType.setKey(TxnEnrollmentProperties.IRRIGATION_SOURCE_TYPES_OTHER);
        irrigationType.setValue("2");
        
        // Certification Related Fields
        // Farm Labour    
        Data fullTimeWorkersCount = new Data();
        fullTimeWorkersCount.setKey(TxnEnrollmentProperties.FULL_TIME_WORKERS_COUNT);
        fullTimeWorkersCount.setValue("1000");

        Data partTimeWorkersCount = new Data();
        partTimeWorkersCount.setKey(TxnEnrollmentProperties.PART_TIME_WORKERS_COUNT);
        partTimeWorkersCount.setValue("1000");

        Data seasonalWorkersCount = new Data();
        seasonalWorkersCount.setKey(TxnEnrollmentProperties.SEASONAL_WORKERS_COUNT);
        seasonalWorkersCount.setValue("1000");

        // Coversion Information
        Data chemicalAppLastDate = new Data();
        chemicalAppLastDate.setKey(TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE);
        chemicalAppLastDate.setValue("20140506");
        
        Data conservationArea = new Data();
        conservationArea.setKey(TxnEnrollmentProperties.FARM_CONSERVATION_AREA);
        conservationArea.setValue("1000");

        Data pastureLand = new Data();
        pastureLand.setKey(TxnEnrollmentProperties.PASTURE_LAND);
        pastureLand.setValue("10");

        Data conventionalLand = new Data();
        conventionalLand.setKey(TxnEnrollmentProperties.CONVENTIONAL_LAND);
        conventionalLand.setValue("10");

        Data conventionalCrop = new Data();
        conventionalCrop.setKey(TxnEnrollmentProperties.CONVENTIONAL_CROP);
        conventionalCrop.setValue("10");

        Data conventionalEstimated = new Data();
        conventionalEstimated.setKey(TxnEnrollmentProperties.CONVENTIONAL_ESTIMATED_YIELD);
        conventionalEstimated.setValue("10");
        
        // Farm Inventory
        Collection inventoryCollection = new Collection();
        List<Object> inventoryObjectList = new ArrayList<Object>();

        Data familyInvId = new Data();
        familyInvId.setKey(TxnEnrollmentProperties.FARM_INVENTORY_ITEM);
        familyInvId.setValue("2");

        Data familyInvIdOther = new Data();
        familyInvIdOther.setKey(TxnEnrollmentProperties.FARM_INVENTORY_ITEM_OTHER);
        familyInvIdOther.setValue("FARM_INVENTORY_ITEM_OTHER");
        
        Data familyInvItemCount = new Data();
        familyInvItemCount.setKey(TxnEnrollmentProperties.FARM_INVENTORY_ITEM_COUNT);
        familyInvItemCount.setValue("2");

        List<Data> listOfFamilyInvData = new ArrayList<Data>();
        listOfFamilyInvData.add(familyInvId);
        listOfFamilyInvData.add(familyInvIdOther);
        listOfFamilyInvData.add(familyInvItemCount);

        Object familyInvObj = new Object();
        familyInvObj.setData(listOfFamilyInvData);

        inventoryObjectList.add(familyInvObj);

        inventoryCollection.setObject(inventoryObjectList);

        Data inventoryData = new Data();
        inventoryData.setKey(TxnEnrollmentProperties.FARM_INVENTORIES);
        inventoryData.setCollectionValue(inventoryCollection);

        // Farm Animals
        Collection animalCollection = new Collection();
        List<Object> animalObjectList = new ArrayList<Object>();

        Data farmAnimal = new Data();
        farmAnimal.setKey(TxnEnrollmentProperties.FARM_ANIMAL);
        farmAnimal.setValue("2");
        
        Data farmAnimalOther = new Data();
        farmAnimal.setKey(TxnEnrollmentProperties.FARM_ANIMAL_OTHER);
        farmAnimal.setValue("FARM_ANIMAL_OTHER");

        Data animalCount = new Data();
        animalCount.setKey(TxnEnrollmentProperties.FARM_ANIMAL_COUNT);
        animalCount.setValue("2");
        
        Data animalHousing = new Data();
        animalHousing.setKey(TxnEnrollmentProperties.FARM_ANIMAL_HOUSE);
        animalHousing.setValue("2");
        
        Data animalHousingOther = new Data();
        animalHousingOther.setKey(TxnEnrollmentProperties.FARM_ANIMAL_HOUSE_OTHER);
        animalHousingOther.setValue("FARM_ANIMAL_HOUSE_OTHER");
        
        Data fodder = new Data();
        fodder.setKey(TxnEnrollmentProperties.FODDER);
        fodder.setValue("2");
        
        Data fodderOther = new Data();
        fodderOther.setKey(TxnEnrollmentProperties.FODDER_OTHER);
        fodderOther.setValue("FODDER_OTHER");

        Data revenue = new Data();
        revenue.setKey(TxnEnrollmentProperties.REVENUE);
        revenue.setValue("2");
        
        Data revenueOther = new Data();
        revenueOther.setKey(TxnEnrollmentProperties.REVENUE_OTHER);
        revenueOther.setValue("REVENUE_OTHER");

        /*     
        Data feedingUsed = new Data();
        feedingUsed.setKey(TxnEnrollmentProperties.FARM_ANIMAL_FEED_USED);
        feedingUsed.setValue("2");
        
        Data animalProduction = new Data();
        animalProduction.setKey(TxnEnrollmentProperties.FARM_ANIMAL_PRODUCTION);
        animalProduction.setValue("Production");

        Data economicOutput = new Data();
        economicOutput.setKey(TxnEnrollmentProperties.FARM_ANIMAL_ESTIMATED_OUTPUT);
        economicOutput.setValue("Output");
        */
        
        List<Data> listOfAnimalData = new ArrayList<Data>();
        listOfAnimalData.add(farmAnimal);
        listOfAnimalData.add(farmAnimalOther);
        listOfAnimalData.add(animalCount);        
        listOfAnimalData.add(animalHousing);
        listOfAnimalData.add(animalHousingOther);
        listOfAnimalData.add(fodder);
        listOfAnimalData.add(fodderOther);
        listOfAnimalData.add(revenue);
        listOfAnimalData.add(revenueOther);
        
        // listOfAnimalData.add(feedingUsed);
        // listOfAnimalData.add(animalProduction);
        // listOfAnimalData.add(economicOutput);

        Object animalObj = new Object();
        animalObj.setData(listOfAnimalData);

        animalObjectList.add(animalObj);

        animalCollection.setObject(animalObjectList);

        Data animalData = new Data();
        animalData.setKey(TxnEnrollmentProperties.FARM_ANIMALS);
        animalData.setCollectionValue(animalCollection);
        
        
        // ICS Details
        Collection icsCollection = new Collection();
        List<Object> icsObjectList = new ArrayList<Object>();

        Data icsType = new Data();
        icsType.setKey(TxnEnrollmentProperties.ICS_TYPE);
        icsType.setValue("1");

        Data icsLandDetails = new Data();
        icsLandDetails.setKey(TxnEnrollmentProperties.ICS_LAND_DETAILS);
        icsLandDetails.setValue("ICS_LAND_DETAILS");
        
        Data icsSurveyNo = new Data();
        icsSurveyNo.setKey(TxnEnrollmentProperties.ICS_SURVEY_NO);
        icsSurveyNo.setValue("2");
        
        Data begOfConv = new Data();
        begOfConv.setKey(TxnEnrollmentProperties.ICS_BEGIN_CONV);
        begOfConv.setValue(sdf.format(new Date()));

        
        List<Data> listOfIcsData = new ArrayList<Data>();
        listOfIcsData.add(icsType);
        listOfIcsData.add(icsLandDetails);
        listOfIcsData.add(icsSurveyNo);
        listOfIcsData.add(begOfConv);

        Object icsObj = new Object();
        icsObj.setData(listOfIcsData);

        icsObjectList.add(icsObj);

        icsCollection.setObject(icsObjectList);

        Data icsData = new Data();
        icsData.setKey(TxnEnrollmentProperties.ICS_LIST);
        icsData.setCollectionValue(icsCollection);
        
        
        /*
         
        Data landMeasurement = new Data();
        landMeasurement.setKey(TxnEnrollmentProperties.LAND_MEASUREMENT);
        landMeasurement.setValue("1000");

        Data farmArea = new Data();
        farmArea.setKey(TxnEnrollmentProperties.FARM_AREA);
        farmArea.setValue("1000");

        Data productiveArea = new Data();
        productiveArea.setKey(TxnEnrollmentProperties.FARM_PRODUCTIVE_AREA);
        productiveArea.setValue("1000");

        Data waterBodiesCount = new Data();
        waterBodiesCount.setKey(TxnEnrollmentProperties.WATER_BODIES_COUNT);
        waterBodiesCount.setValue("1000");

        Data areaUnderIrrg = new Data();
        areaUnderIrrg.setKey(TxnEnrollmentProperties.AREA_UNDER_IRRIGATION);
        areaUnderIrrg.setValue("0");

        Data irrgSource = new Data();
        irrgSource.setKey(TxnEnrollmentProperties.IRRIGATION_SOURCE);
        irrgSource.setValue("0");

        Data irrgMethod = new Data();
        irrgMethod.setKey(TxnEnrollmentProperties.IRRIGATION_METHOD);
        irrgMethod.setValue("0");

        Data farmIrrg = new Data();
        farmIrrg.setKey(TxnEnrollmentProperties.FARM_IRRIGATION);
        farmIrrg.setValue("0");        

        Data beginOfConversion = new Data();
        beginOfConversion.setKey(TxnEnrollmentProperties.BEGIN_OF_CONVERSION);
        beginOfConversion.setValue("begin");

        Data intInspDate = new Data();
        intInspDate.setKey(TxnEnrollmentProperties.INTERNAL_INSPECTION_DATE);
        intInspDate.setValue("2013-10-23 01:54:10");

        Data intInspName = new Data();
        intInspName.setKey(TxnEnrollmentProperties.INTERNAL_INSPECTOR_NAME);
        intInspName.setValue("inspection");

        Data surveyNo = new Data();
        surveyNo.setKey(TxnEnrollmentProperties.SURVEY_NUMBER);
        surveyNo.setValue("2");

        Data landUnderICSStatus = new Data();
        landUnderICSStatus.setKey(TxnEnrollmentProperties.LAND_UNDER_ICS_STATUS);
        landUnderICSStatus.setValue("0");

        Data nonConfirmty = new Data();
        nonConfirmty.setKey(TxnEnrollmentProperties.NON_CONFIRMTY);
        nonConfirmty.setValue("10");
        
        */

        // Farm Crops
        Collection farmCropsCollection = new Collection();
        List<Object> farmCropsObjectList = new ArrayList<Object>();

        Data farmCropCode = new Data();
        farmCropCode.setKey(TxnEnrollmentProperties.FARM_CROPS_CODE);
        farmCropCode.setValue("67");
        
        Data seedSource = new Data();
        seedSource.setKey(TxnEnrollmentProperties.SEED_SOURCE);
        seedSource.setValue("2");
        
        Data production = new Data();
        production.setKey(TxnEnrollmentProperties.PRODUCTION_YEAR);
        production.setValue("1000");

        Data cropSeason = new Data();
        cropSeason.setKey(TxnEnrollmentProperties.CROP_SEASON);
        cropSeason.setValue("0");

        Data cropCategory = new Data();
        cropCategory.setKey(TxnEnrollmentProperties.CROP_CATEGORY);
        cropCategory.setValue("0");
        
        /*
        Data cropArea = new Data();
        cropArea.setKey(TxnEnrollmentProperties.CROP_AREA);
        cropArea.setValue("800");
        */
        
        List<Data> listOfFarmCropsData = new ArrayList<Data>();
        listOfFarmCropsData.add(farmCropCode);
        // listOfFarmCropsData.add(cropArea);
        listOfFarmCropsData.add(seedSource);
        listOfFarmCropsData.add(production);
        listOfFarmCropsData.add(cropSeason);
        listOfFarmCropsData.add(cropCategory);

        Object farmCropObj = new Object();
        farmCropObj.setData(listOfFarmCropsData);

        farmCropsObjectList.add(farmCropObj);
        farmCropsCollection.setObject(farmCropsObjectList);

        Data farmCropsData = new Data();
        farmCropsData.setKey(TxnEnrollmentProperties.FARM_CROPS_LIST);
        farmCropsData.setCollectionValue(farmCropsCollection);
        
        //BANK
        
        Collection bankCollection = new Collection();
        List<Object> bankObjectList = new ArrayList<Object>();
     
        
        Data bankName=new Data();
        bankName.setKey(TxnEnrollmentProperties.BANK_NAME);
        bankName.setValue("HSBC");
        
        Data accNo=new Data();
        accNo.setKey(TxnEnrollmentProperties.ACCOUNT_NO);
        accNo.setValue("1234567");
        
        Data branchDetail=new Data();
        branchDetail.setKey(TxnEnrollmentProperties.BRANCH_DETAILS);
        branchDetail.setValue("CTR1");
        
        Data sortCode=new Data();
        sortCode.setKey(TxnEnrollmentProperties.SORT_CODE);
        sortCode.setValue("12113");
        
        Data swiftCode=new Data();
        swiftCode.setKey(TxnEnrollmentProperties.SWIFT_CODE);
        swiftCode.setValue("23456");
        
        List<Data> listOfBankInfo=new ArrayList<Data>();
        listOfBankInfo.add(bankName);
        listOfBankInfo.add(accNo);
        listOfBankInfo.add(branchDetail);
        listOfBankInfo.add(sortCode);
        listOfBankInfo.add(swiftCode);
        
        Object bankObj = new Object();
        bankObj.setData(listOfBankInfo);

        bankObjectList.add(bankObj);

        bankCollection.setObject(bankObjectList);
        dataMap.put(TxnEnrollmentProperties.BANK_INFORMATION, bankCollection);
        
        dataList = CollectionUtil.mapToList(dataMap);
        // Farm Harvest Data
        Collection harvestCollection = new Collection();
        List<Object> harvestObjectList = new ArrayList<Object>();

        Data farmCropCodeRef = new Data();
        farmCropCodeRef.setKey(TxnEnrollmentProperties.HARVEST_FARM_CROP_CODE_REF);
        farmCropCodeRef.setValue("FC001");

        Data harvestDate = new Data();
        harvestDate.setKey(TxnEnrollmentProperties.HARVEST_DATE);
        harvestDate.setValue(sdf.format(new Date()));

        Data harvestQuantity = new Data();
        harvestQuantity.setKey(TxnEnrollmentProperties.HARVEST_QUANTITY);
        harvestQuantity.setValue("700");

        Data harvestAmt = new Data();
        harvestAmt.setKey(TxnEnrollmentProperties.HARVEST_AMOUNT);
        harvestAmt.setValue("1000");

        Data buyerName = new Data();
        buyerName.setKey(TxnEnrollmentProperties.BUYER_NAME);
        buyerName.setValue("FS001");

        Data farmCropPhoto = new Data();
        FileDataSource _dataSource1 = new FileDataSource(new File("C:\\Users\\admin\\Desktop\\STS Doc\\images\\1.jpg"));
        DataHandler _dataHandler1 = new DataHandler(_dataSource1);
        farmCropPhoto.setKey(TxnEnrollmentProperties.CROP_PHOTO);
        farmCropPhoto.setBinaryValue(_dataHandler1);

        List<Data> listOfFarmHarvestData = new ArrayList<Data>();
        listOfFarmHarvestData.add(farmCropCodeRef);
        listOfFarmHarvestData.add(harvestDate);
        listOfFarmHarvestData.add(harvestQuantity);
        listOfFarmHarvestData.add(harvestAmt);
        listOfFarmHarvestData.add(buyerName);
        listOfFarmHarvestData.add(farmCropPhoto);

        Object farmHarvestObj = new Object();
        farmHarvestObj.setData(listOfFarmHarvestData);

        harvestObjectList.add(farmHarvestObj);
        harvestCollection.setObject(harvestObjectList);

        Data harvestCollectionData = new Data();
        harvestCollectionData.setKey(TxnEnrollmentProperties.HARVEST_LIST);
        harvestCollectionData.setCollectionValue(harvestCollection);
        
        List<Data> listOfFarmData = new ArrayList<Data>();
        
        // Farm Related Information
        listOfFarmData.add(code);
        listOfFarmData.add(name);
        listOfFarmData.add(surveyNo);
        listOfFarmData.add(isSameAddress);
        listOfFarmData.add(totalLandHoldingArea);
        listOfFarmData.add(proposedPlantnigArea);        
        listOfFarmData.add(farmOwned);
        listOfFarmData.add(landGradient);
        listOfFarmData.add(approachRoad);
        listOfFarmData.add(regYear);
        
        listOfFarmData.add(farmPhoto);
        listOfFarmData.add(farmPhotoCapturingDate);
        listOfFarmData.add(farmLatitude);
        listOfFarmData.add(farmLongitude);
        
        listOfFarmData.add(landAreaCollectionData);
        
        listOfFarmData.add(soilType);
        listOfFarmData.add(soilTexture);
        listOfFarmData.add(fertilityStatus);
        listOfFarmData.add(farmIrrigarion);
        listOfFarmData.add(irrigationType);
        listOfFarmData.add(irrigationTypeOther);
        
        // Farm Crop Data
        listOfFarmData.add(farmCropsData);
        
        // Farm Certification Information 
        // Labour Info
        listOfFarmData.add(fullTimeWorkersCount);
        listOfFarmData.add(partTimeWorkersCount);
        listOfFarmData.add(seasonalWorkersCount);

        // Farm Animal
        listOfFarmData.add(animalData);
        
        // Farm Inventory
        listOfFarmData.add(inventoryData);
        
        // Conversion Information
        listOfFarmData.add(chemicalAppLastDate);        
        listOfFarmData.add(conservationArea);
        listOfFarmData.add(conventionalLand);
        listOfFarmData.add(conventionalCrop);
        listOfFarmData.add(conventionalEstimated);
        listOfFarmData.add(pastureLand);
        
        // Land ICS Details
        listOfFarmData.add(icsData);        

        Object farmObj = new Object();
        farmObj.setData(listOfFarmData);

        farmObjectList.add(farmObj);

        farmCollection.setObject(farmObjectList);
        dataMap.put(TxnEnrollmentProperties.FARM_LIST, farmCollection);
        dataList = CollectionUtil.mapToList(dataMap);

        Data data1 = new Data();
        FileDataSource dataSource1 = new FileDataSource(new File("C:\\Users\\admin\\Desktop\\STS Doc\\images\\1.jpg"));
        DataHandler dataHandler1 = new DataHandler(dataSource);
        data1.setKey(TxnEnrollmentProperties.PHOTO);
        data1.setBinaryValue(dataHandler1);
        dataList.add(data1);
       
     
        Body body = new Body();
        body.setData(dataList);
        request.setHead(head);
        request.setBody(body);

        Response response = client.post(request, Response.class);
        LOGGER.info(response.getStatus().getCode());
        LOGGER.info(response.getStatus().getMessage());
        System.out.println("Status Code : " + response.getStatus().getCode());
        System.out.println("Status Msg  : " + response.getStatus().getMessage());
    }

}
