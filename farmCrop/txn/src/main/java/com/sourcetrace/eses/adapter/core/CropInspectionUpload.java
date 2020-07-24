/*
 * CropInspectionUpload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.inspect.agrocert.Section;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.agrocert.Answers;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersSectionAnswers;
import com.sourcetrace.eses.txn.agrocert.InspectionStandard;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class CropInspectionUpload implements ITxnAdapter {

    public static final Logger LOGGER = Logger.getLogger(CropInspectionUpload.class.getName());
	@Autowired
    private ICertificationService certificationService;
	@Autowired
    private IProductDistributionService productDistributionService;
	@Autowired
    private IFarmerService farmerService;
	@Autowired
    private IFarmCropsService farmCropsService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	Head head = (Head) reqData.get(TransactionProperties.HEAD);
    	
        LOGGER.info("CropInspectionUpload Txn Begin");
        String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
        String farmId = (String) reqData.get(TxnEnrollmentProperties.FARM_ID);
        String seasonCode = (String) reqData.get(TxnEnrollmentProperties.SEASON_CODE);
        String answerDate = (String) reqData.get(TxnEnrollmentProperties.ANSWER_DATE);
        String categoryCode = (String) reqData.get(TxnEnrollmentProperties.CERTIFICATE_CATEGORY);
        Object standardCollectionObj = reqData.get(TxnEnrollmentProperties.STANDARDS);
        String acresOwned=(String)reqData.get(TxnEnrollmentProperties.ACRES_OWNED);
    	String acresFarmed=(String)reqData.get(TxnEnrollmentProperties.ACRES_FARMED);
    	String acresOrganic=(String)reqData.get(TxnEnrollmentProperties.ACRES_ORGANIC);
    	String acresTrans=(String) reqData.get(TxnEnrollmentProperties.ACRES_TRANSITION);
    	String acresEligible=(String)reqData.get(TxnEnrollmentProperties.ELIGIBLE_ACRES_NEXT_CERTIFICATION);
    	String acresReq=(String)reqData.get(TxnEnrollmentProperties.ACRES_REQ_ORGANIC_INSPECTION);
    	String certificationStatus=(String)reqData.get(TxnEnrollmentProperties.CERTIFICATION_STATUS);
    	String latitude=(String)reqData.get(TxnEnrollmentProperties.LATITUDE);
    	String longitude=(String)reqData.get(TxnEnrollmentProperties.CROP_INSPECTION_FARM_LONGITUDE);
        if (StringUtil.isEmpty(farmerId)) {
            throw new SwitchException(SwitchErrorCodes.EMPTY_FARMER_ID);
        }

        CropYield cropYield = buildCropYield(reqData);

        FarmerCropProdAnswers farmerCropProdAnswers = new FarmerCropProdAnswers();
        farmerCropProdAnswers.setBranchId(head.getBranchId());
        farmerCropProdAnswers.setFarmerId(farmerId);
        if(categoryCode.equals("CC006")||categoryCode.equals("CC007")||categoryCode.equals("CC008")||categoryCode.equals("CC009")){
        	farmerCropProdAnswers.setFarmId(farmId);
        }
        else{
        farmerCropProdAnswers.setFarmId(String.valueOf(cropYield.getFarm().getId()));
        }
        if (!StringUtil.isEmpty(seasonCode)) {
            Season season = productDistributionService.findSeasonBySeasonCode(seasonCode);
            if (!ObjectUtil.isEmpty(season))
                farmerCropProdAnswers.setSeason(season);
        }
        farmerCropProdAnswers.setLatitude(latitude);
        farmerCropProdAnswers.setLongitude(longitude);
        if(categoryCode.equals("CC005")){
        	if(!StringUtil.isEmpty(acresOwned) && acresOwned!=null){
        	farmerCropProdAnswers.setAcresOwned(Double.parseDouble(acresOwned));
        	}
        	if(!StringUtil.isEmpty(acresFarmed) && acresFarmed!=null){
        	farmerCropProdAnswers.setAcresFarmed(Double.parseDouble(acresFarmed));
        	}
        	if(!StringUtil.isEmpty(acresOrganic) && acresOrganic!=null){
        	farmerCropProdAnswers.setAcresOrganic(Double.parseDouble(acresOrganic));
        	}
        	if(!StringUtil.isEmpty(acresTrans) && acresTrans!=null){
        	farmerCropProdAnswers.setAcresTransition(Double.parseDouble(acresTrans));
        	}
        	if(!StringUtil.isEmpty(acresEligible) && acresEligible!=null){
        	farmerCropProdAnswers.setAcresEligibleNext(Double.parseDouble(acresEligible));
        	}
        	if(!StringUtil.isEmpty(acresReq) && acresReq!=null){
        	farmerCropProdAnswers.setAcresReqIns(Double.parseDouble(acresReq));
        	}
        }
        else{
        	    farmerCropProdAnswers.setAcresOwned(0);
            	
            	farmerCropProdAnswers.setAcresFarmed(0);
            	
            	farmerCropProdAnswers.setAcresOrganic(0);
            	
            	farmerCropProdAnswers.setAcresTransition(0);
            	
            	farmerCropProdAnswers.setAcresEligibleNext(0);
            	
            	farmerCropProdAnswers.setAcresReqIns(0);
            	
            	}
        	
         // setting season for Crop Yield from farmer crop prod answers
        if (!ObjectUtil.isEmpty(cropYield))
            cropYield.setSeason(farmerCropProdAnswers.getSeason());

        try {
            farmerCropProdAnswers.setAnsweredDate(DateUtil.convertStringToDate(answerDate,
                    DateUtil.TXN_DATE_TIME));
        } catch (Exception e) {
            LOGGER.info("Answer Date Conversion Error " + e.getMessage());
            throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
        }
        // Setting Category Code and Category Name
        setCertificateCategoryCodeAndName(categoryCode, farmerCropProdAnswers,
                standardCollectionObj);

        Object secObj = reqData.get(TxnEnrollmentProperties.SECTIONS);
        if (secObj instanceof Collection) {

            Collection sectionCollection = (Collection) secObj;
            if (CollectionUtil.isCollectionEmpty(sectionCollection)) {
                throw new SwitchException(SwitchErrorCodes.EMPTY_SECTIONS);
            }
            Map<String, Object> objMap = getSectionAndQuestionMap(categoryCode);
            farmerCropProdAnswers.setFarmersSectionAnswers(buildFarmerSectionAnswersObj(objMap,
                    sectionCollection));
            // Commented because both farmerCropProdAnswers & cropYield entity want to be saved in
            // single service layer call
            // certificationService.addFarmerCropProdAnswers(farmerCropProdAnswers);
            if(categoryCode.equals("CC007")){
            farmerCropProdAnswers.setCertificationStatus(Integer.parseInt(certificationStatus));
            }else{
            farmerCropProdAnswers.setCertificationStatus(0);	
            }
            certificationService.addFarmCropProdAnswersAndCropYield(farmerCropProdAnswers,
                    cropYield);
            if(certificationStatus!=null && categoryCode.equals("CC007")){
            Farmer farmer = farmerService.findFarmerByFarmerId(farmerCropProdAnswers.getFarmId());	
            if(farmer!=null){
            farmerService.updateFarmerUtzStatus(farmerCropProdAnswers.getFarmId(),certificationStatus);}
            else{
            Warehouse wareHouse = farmerService.findWareHouseByFarmerId(farmerCropProdAnswers.getFarmerId());	
            if(wareHouse!=null){
            farmerService.updateSamithiUtzStatus(farmerCropProdAnswers.getFarmerId(),certificationStatus); 	
            } 	
            }
            }
        }
        LOGGER.info("CropProdInspectionReportUpload Txn End !!!");
        Map resp = new HashMap();
        return resp;
    }

    /**
     * Builds the crop yield.
     * @param reqData the req data
     * @return the crop yield
     */
    private CropYield buildCropYield(Map<?, ?> reqData) {

        CropYield cropYield = new CropYield();

        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String farmId = (String) reqData.get(TxnEnrollmentProperties.FARM_ID);
        String latitude = (String) reqData
                .get(TxnEnrollmentProperties.CROP_INSPECTION_FARM_LATITUDE);
        String longitude = (String) reqData
                .get(TxnEnrollmentProperties.CROP_INSPECTION_FARM_LONGITUDE);
        String landHolding = (String) reqData.get(TxnEnrollmentProperties.LAND_HOLDING);
        Collection cropYieldListCollection = (Collection) reqData
                .get(TxnEnrollmentProperties.CROP_YIELD_LIST);
        String categoryCode = (String) reqData.get(TxnEnrollmentProperties.CERTIFICATE_CATEGORY);
        if(categoryCode.equals("CC006")||(categoryCode.equals("CC007"))){
        	cropYield.setFarm(null);
        	cropYield.setLatitude(null);
        	cropYield.setLongitude(null);
        	cropYield.setLandHolding(null);
        }else{
        if (StringUtil.isEmpty(farmId)) {
            throw new SwitchException(SwitchErrorCodes.EMPTY_FARM_ID);
        }
        if(!"CC006".equalsIgnoreCase(categoryCode)||!"CC007".equalsIgnoreCase(categoryCode)
        		||!"CC008".equalsIgnoreCase(categoryCode)||!"CC009".equalsIgnoreCase(categoryCode)){
        Farm farm = farmerService.findFarmByCode(farmId);
        if (ObjectUtil.isEmpty(farm)) {
            throw new SwitchException(SwitchErrorCodes.FARM_NOT_EXIST);
        }

        cropYield.setFarm(farm);
        if(!StringUtil.isEmpty(latitude)&&!StringUtil.isEmpty(longitude)){
	        double lat=Double.valueOf(latitude);
	        double lon=Double.valueOf(longitude);
	        if(lat!=0&&lon!=0){
	        farm.setLatitude(latitude);
	        farm.setLongitude(longitude);
	        }
        }
        cropYield.setLatitude(latitude);
        cropYield.setLongitude(longitude);
        cropYield.setLandHolding(landHolding);
        }}
        try {
            cropYield.setCropYieldDate(DateUtil.convertStringToDate(head.getTxnTime(),
                    DateUtil.TXN_DATE_TIME));
        } catch (Exception e) {
            LOGGER.info("TXN DATE - " + head.getTxnTime() + " -  Conversion Error "
                    + e.getMessage());
            cropYield.setCropYieldDate(new Date());
        }
        Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        paramsMap.put("collection", cropYieldListCollection);
        paramsMap.put("cropYield", cropYield);
        cropYield.setCropYieldDetails(buildCropYieldDetails(paramsMap));

        return cropYield;
    }

    private Set<CropYieldDetail> buildCropYieldDetails(Map<String, Object> paramsMap) {

        Collection cropYieldListCollection = (Collection) paramsMap.get("collection");
        CropYield cropYield = (CropYield) paramsMap.get("cropYield");
        Set<CropYieldDetail> cropYieldDetails = null;
        if (!ObjectUtil.isEmpty(cropYieldListCollection) && !ObjectUtil.isEmpty(cropYield)) {
            List<com.sourcetrace.eses.txn.schema.Object> cropYieldObject = cropYieldListCollection
                    .getObject();
            cropYieldDetails = new LinkedHashSet<CropYieldDetail>();
            for (com.sourcetrace.eses.txn.schema.Object object : cropYieldObject) {
                List<Data> cropYieldData = object.getData();
                CropYieldDetail cropYieldDetail = new CropYieldDetail();
                for (Data data : cropYieldData) {
                    String key = data.getKey();
                    String value = (String) data.getValue();
                    if (TxnEnrollmentProperties.FARM_CROPS_CODE.equalsIgnoreCase(key)) {
                        if (!StringUtil.isEmpty(value)) {
                           /* FarmCropsMaster farmCropsMaster = farmCropsService
                                    .findFarmCropsMasterByCode(value);*/
                            
                            ProcurementProduct procurementProduct = productDistributionService.findProcurementProductByCode(value);
                            
                            if (ObjectUtil.isEmpty(procurementProduct))
                                throw new SwitchException(
                                        SwitchErrorCodes.FARM_CROPS_DOES_NOT_EXIST);
                            cropYieldDetail.setProcurementProduct(procurementProduct);
                        }
                    }
                    if (TxnEnrollmentProperties.YIELD.equalsIgnoreCase(key)) {
                        cropYieldDetail.setYield(value);
                    }
                }
                cropYieldDetail.setCropYield(cropYield);
                cropYieldDetails.add(cropYieldDetail);
            }
        }
        return cropYieldDetails;
    }

    /**
     * Sets the certificate category code and name.
     * @param categoryCode the category code
     * @param farmerCropProdAnswers the farmer crop prod answers
     * @param standardCollectionObj the standard collection obj
     */
    private void setCertificateCategoryCodeAndName(String categoryCode,
            FarmerCropProdAnswers farmerCropProdAnswers, Object standardCollectionObj) {

        Map<String, Object> objMap = getCategoryAndStandardMap(categoryCode);
        CertificateCategory certificateCategory = (CertificateCategory) objMap.get(categoryCode);
        farmerCropProdAnswers.setCategoryCode(certificateCategory.getCode());
        farmerCropProdAnswers.setCategoryName(certificateCategory.getName());

        if (standardCollectionObj instanceof Collection) {
            Collection standardCollection = (Collection) standardCollectionObj;
            farmerCropProdAnswers.setInspectionStandards(buildInspectionStandard(
                    standardCollection, objMap));
        }
    }

    /**
     * Gets the category and standard map.
     * @param categoryCode the category code
     * @return the category and standard map
     */
    private Map<String, Object> getCategoryAndStandardMap(String categoryCode) {

        Map<String, Object> objMap = new HashMap<String, Object>();
        CertificateCategory certificateCategory = certificationService
                .findCertificateCategoryByCode(categoryCode);
        if (!ObjectUtil.isEmpty(certificateCategory)) {
            objMap.put(certificateCategory.getCode(), certificateCategory);
            for (CertificateStandard certificateStandard : certificateCategory.getStandards()) {
                objMap.put(certificateStandard.getCode(), certificateStandard);
            }
        }
        return objMap;
    }

    /**
     * Builds the inspection standard.
     * @param standardCollection the standard collection
     * @param objMap the obj map
     * @return the set< inspection standard>
     */
    private Set<InspectionStandard> buildInspectionStandard(Collection standardCollection,
            Map<String, Object> objMap) {

        Set<InspectionStandard> standardSet = new HashSet<InspectionStandard>();

        for (com.sourcetrace.eses.txn.schema.Object standardObject : standardCollection.getObject()) {

            InspectionStandard inspectionStandard = new InspectionStandard();
            for (com.sourcetrace.eses.txn.schema.Data data : standardObject.getData()) {

                if (data.getKey().equals(TxnEnrollmentProperties.STANDARD_CODE)) {
                    CertificateStandard certificateStandard = (CertificateStandard) objMap.get(data
                            .getValue());
                    if (ObjectUtil.isEmpty(certificateStandard))
                        throw new SwitchException(
                                SwitchErrorCodes.INVALID_CATEGORY_STANDARD_MAPPING);
                    inspectionStandard.setStandardCode(certificateStandard.getCode());
                    inspectionStandard.setStandardName(certificateStandard.getName());
                    standardSet.add(inspectionStandard);
                }
            }
        }
        return standardSet;
    }

    /**
     * Gets the section and question map.
     * @param categoryCode the category code
     * @return the section and question map
     */
    private Map<String, Object> getSectionAndQuestionMap(String categoryCode) {

        Map<String, Object> objMap = new HashMap<String, Object>();
        List<Section> sectionsList = certificationService.listSectionByCategoryCode(categoryCode);
        for (Section section : sectionsList) {
            objMap.put(section.getCode(), section);
            for (Question question : section.getQuestions()) {
                objMap.put(question.getCode(), question);
                for (Question subQuestion : question.getSubQuestions()) {
                    objMap.put(subQuestion.getCode(), subQuestion);
                }
            }
        }
        return objMap;
    }

    /**
     * Builds the farmer section answers obj.
     * @param objMap the obj map
     * @param sectionCollection the section collection
     * @return the sorted set< farmers section answers>
     */
    private SortedSet<FarmersSectionAnswers> buildFarmerSectionAnswersObj(
            Map<String, Object> objMap, Collection sectionCollection) {

        SortedSet<FarmersSectionAnswers> farmersSectionAnswersSet = new TreeSet<FarmersSectionAnswers>();
        for (com.sourcetrace.eses.txn.schema.Object secObject : sectionCollection.getObject()) {

            FarmersSectionAnswers farmerSectionAnswers = new FarmersSectionAnswers();
            for (com.sourcetrace.eses.txn.schema.Data data : secObject.getData()) {

                if (TxnEnrollmentProperties.SECTION_CODE.equals(data.getKey())) {
                    farmerSectionAnswers.setSectionCode(data.getValue());
                }

                if (TxnEnrollmentProperties.QUESTIONS.equals(data.getKey())) {
                    Collection questionsCollection = data.getCollectionValue();
                    if (CollectionUtil.isCollectionEmpty(questionsCollection)) {
                        throw new SwitchException(SwitchErrorCodes.EMPTY_QUESTIONS);
                    }
                    farmerSectionAnswers
                            .setFarmersQuestionAnswers(buildFarmerQuestionAnswersCollectionObj(
                                    objMap, questionsCollection));
                }
            }

            Section section = ((Section) (objMap.get(farmerSectionAnswers.getSectionCode())));
            if (!ObjectUtil.isEmpty(section)) {
                // Validate Section to Question Map, if Section is a registered Section
                validateSectionQuestionsMapping(section.getCode(), farmerSectionAnswers
                        .getFarmersQuestionAnswers(), objMap);
                farmerSectionAnswers.setSectionType(section.getSectionType());
                farmerSectionAnswers.setSectionName(section.getName());
                farmerSectionAnswers.setSerialNo(section.getSerialNo());
            } else {
                throw new SwitchException(SwitchErrorCodes.INVALID_SECTION_CODE);
            }

            farmersSectionAnswersSet.add(farmerSectionAnswers);
        }
        return farmersSectionAnswersSet;
    }

    /**
     * Builds the farmer question answers collection obj.
     * @param objMap the obj map
     * @param questionCollection the question collection
     * @return the sorted set< farmers question answers>
     */
    private SortedSet<FarmersQuestionAnswers> buildFarmerQuestionAnswersCollectionObj(
            Map<String, Object> objMap, Collection questionCollection) {

        SortedSet<FarmersQuestionAnswers> farmersQuestionAnswersSet = new TreeSet<FarmersQuestionAnswers>();

        for (com.sourcetrace.eses.txn.schema.Object questionObject : questionCollection.getObject()) {

            FarmersQuestionAnswers farmersQuestionAnswers = new FarmersQuestionAnswers();
            farmersQuestionAnswers.setRegisteredQuestion(ICertificationService.REGISTERED_QUESTION);
            for (com.sourcetrace.eses.txn.schema.Data data : questionObject.getData()) {
                if (TxnEnrollmentProperties.QUESTION_CODE.equals(data.getKey())) {
                    farmersQuestionAnswers.setQuestionCode(data.getValue());
                }
                else if (TxnEnrollmentProperties.IS_REGISTERED_QUESTION.equals(data.getKey())) {
                    farmersQuestionAnswers.setRegisteredQuestion(data.getValue());
                }
                else if (TxnEnrollmentProperties.QUESTION_NAME.equals(data.getKey())) {
                    farmersQuestionAnswers.setQuestionName(data.getValue());
                }
                else if (TxnEnrollmentProperties.PHOTO.equals(data.getKey())) {
                    DataHandler dataHandler = data.getBinaryValue();
                    try {
                        byte[] photoContent = IOUtils.toByteArray(dataHandler.getInputStream());
                        farmersQuestionAnswers.setImage(photoContent);
                    } catch (IOException e) {
                        LOGGER.info("Photo For Questions Conversion Error " + e.getMessage());
                        e.printStackTrace();
                    }

                }
                else if (TxnEnrollmentProperties.QUESTION_LATITUDE.equals(data.getKey())) {
                    farmersQuestionAnswers.setLatitude(data.getValue());
                }
                else if (TxnEnrollmentProperties.QUESTION_LONGITUDE.equals(data.getKey())) {
                    farmersQuestionAnswers.setLongtitude(data.getValue());
                }
                else if (TxnEnrollmentProperties.GPS_CAPTURE_DATE_TIME.equals(data.getKey())) {
                    if (!StringUtil.isEmpty(data.getValue())) {
                        try {
                            farmersQuestionAnswers.setGPSCaptureDateTime(DateUtil
                                    .convertStringToDate(data.getValue(), DateUtil.TXN_DATE_TIME));
                        } catch (Exception e) {
                            throw new SwitchException(SwitchErrorCodes.INVALID_DATE_FORMAT);
                        }
                    }
                }
                else if (TxnEnrollmentProperties.FOLLOW_UP.equals(data.getKey())) {
                    farmersQuestionAnswers.setFollowUp(data.getValue());
                }
                else if (TxnEnrollmentProperties.ANSWERS.equals(data.getKey())) {
                    Collection answersCollection = data.getCollectionValue();
                    if (!CollectionUtil.isCollectionEmpty(answersCollection)) {
                    	farmersQuestionAnswers.setAnswers(buildAnswersCollectionObj(answersCollection));
                    }
                }
                else if (TxnEnrollmentProperties.SUB_QUESTIONS.equals(data.getKey())) {
                    Collection subQuestionsCollection = data.getCollectionValue();
                    if (!CollectionUtil.isCollectionEmpty(subQuestionsCollection)) {
                    	farmersQuestionAnswers.setSubQuestions(buildFarmerQuestionAnswersCollectionObj(
                                objMap, subQuestionsCollection));
                    }
                    
                }
            }
            if (farmersQuestionAnswers.getRegisteredQuestion().equals(
                    ICertificationService.REGISTERED_QUESTION)) {

                Question question = ((Question) objMap
                        .get(farmersQuestionAnswers.getQuestionCode()));
                if(ObjectUtil.isEmpty(question)){
                	System.out.println(farmersQuestionAnswers.getQuestionCode());
                }
                if (!ObjectUtil.isEmpty(question)) {
                    // Validate Question to Sub Question Mapping, If Question type is not as 3.1 or
                    // 6.1 AND Farmer Sub Questions Answers
                    // available in Farmer Question Answers
                    if (ObjectUtil.isEmpty(question.getQuestionType())
                            && farmersQuestionAnswers.getSubQuestions() != null) {
                        validateQuestionSubQuestionsMapping(question.getCode(),
                                farmersQuestionAnswers.getSubQuestions(), objMap);
                    }
                    farmersQuestionAnswers.setQuestionType(question.getQuestionType());
                    farmersQuestionAnswers.setSerialNo(question.getSerialNo());
                    farmersQuestionAnswers.setQuestionName(question.getName());
                    } 
                  /*  else
                    {
                    	throw new SwitchException(SwitchErrorCodes.INVALID_QUESTION_CODE);
                    }*/
            }
            else{
            	 Question question = ((Question) objMap
                         .get(farmersQuestionAnswers.getQuestionCode()));
            	 if(!ObjectUtil.isEmpty(question)){
            		 farmersQuestionAnswers.setSerialNo(question.getSerialNo());
            	 }
            }
            farmersQuestionAnswersSet.add(farmersQuestionAnswers);
        }
        return farmersQuestionAnswersSet;
    }

    /**
     * Builds the answers collection obj.
     * @param answersCollection the answers collection
     * @return the sorted set< answers>
     */
    private SortedSet<Answers> buildAnswersCollectionObj(Collection answersCollection) {

        SortedSet<Answers> answersSet = new TreeSet<Answers>();

        for (com.sourcetrace.eses.txn.schema.Object answerObject : answersCollection.getObject()) {

            Answers answers = new Answers();
            for (com.sourcetrace.eses.txn.schema.Data data : answerObject.getData()) {

                if (TxnEnrollmentProperties.ANSWER_TYPE.equals(data.getKey())) {
                    answers.setAnswerType(Integer.valueOf(data.getValue()));
                }
                else if (TxnEnrollmentProperties.ANSWER.equals(data.getKey())) {
                    answers.setAnswer(data.getValue());
                }
                else if (TxnEnrollmentProperties.ANSWER_1.equals(data.getKey())) {
                    answers.setAnswer1(data.getValue());
                }
                else if (TxnEnrollmentProperties.ANSWER_2.equals(data.getKey())) {
                    answers.setAnswer2(data.getValue());
                }
                else if (TxnEnrollmentProperties.ANSWER_3.equals(data.getKey())) {
                    answers.setAnswer3(data.getValue());
                }
                else if (TxnEnrollmentProperties.ANSWER_4.equals(data.getKey())) {
                    answers.setAnswer4(data.getValue());
                }
                else if (TxnEnrollmentProperties.ANSWER_5.equals(data.getKey())) {
                    answers.setAnswer5(data.getValue());
                }
                else if (TxnEnrollmentProperties.FOLLOW_UP.equals(data.getKey())) {
                    answers.setFollowUp(data.getValue());
                }
               
            }
            answersSet.add(answers);
        }
        return answersSet;
    }

    

    
    /**
     * Validate question sub questions mapping.
     * @param questionCode the question code
     * @param farmersSubQuestions the farmers sub questions
     * @param objMap the obj map
     */
    private void validateQuestionSubQuestionsMapping(String questionCode,
            Set<FarmersQuestionAnswers> farmersSubQuestions, Map<String, Object> objMap) {

        for (FarmersQuestionAnswers mapObj : farmersSubQuestions) {

            if (ICertificationService.REGISTERED_QUESTION.equals(mapObj.getRegisteredQuestion())) {

                Question subQuestion = (Question) objMap.get(mapObj.getQuestionCode());
                if (ObjectUtil.isEmpty(subQuestion.getParentQuestion())
                        || !(subQuestion.getParentQuestion().getCode().equals(questionCode))) {
                    throw new SwitchException(
                            SwitchErrorCodes.INVALID_QUESTION_SUB_QUESTION_MAPPING);
                }
            }
        }
    }

    /**
     * Validate section questions mapping.
     * @param sectionCode the section code
     * @param farmersQuestionAnswers the farmers question answers
     * @param objMap the obj map
     */
    private void validateSectionQuestionsMapping(String sectionCode,
            Set<FarmersQuestionAnswers> farmersQuestionAnswers, Map<String, Object> objMap) {

        for (FarmersQuestionAnswers mapObj : farmersQuestionAnswers) {

            if (ICertificationService.REGISTERED_QUESTION.equals(mapObj.getRegisteredQuestion())) {
            	System.out.println(mapObj.getQuestionCode());
                Question question = (Question) objMap.get(mapObj.getQuestionCode());
                if(!ObjectUtil.isEmpty(question)){
                 if(!(question.getSection().getCode().equals(sectionCode))){
                	 throw new SwitchException(SwitchErrorCodes.INVALID_SECTION_QUESTION_MAPPING);
                 }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
     */
    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }

    /**
     * Sets the certification service.
     * @param certificationService the new certification service
     */
    public void setCertificationService(ICertificationService certificationService) {

        this.certificationService = certificationService;
    }

    /**
     * Gets the certification service.
     * @return the certification service
     */
    public ICertificationService getCertificationService() {

        return certificationService;
    }

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
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
     * Gets the farm crops service.
     * @return the farm crops service
     */
    public IFarmCropsService getFarmCropsService() {

        return farmCropsService;
    }

    /**
     * Sets the farm crops service.
     * @param farmCropsService the new farm crops service
     */
    public void setFarmCropsService(IFarmCropsService farmCropsService) {

        this.farmCropsService = farmCropsService;
    }

}
