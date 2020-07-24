/*
 * PeriodicInspectionAdapter.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.profile.InspectionImage;
import com.sourcetrace.eses.order.entity.profile.InspectionImageInfo;
import com.sourcetrace.eses.order.entity.profile.Symptoms;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionData;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionSymptom;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionAdapter.
 */
@Component
public class PeriodicInspectionAdapter implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(PeriodicInspectionAdapter.class.getName());
    private static final String EMPTY = "empty";
    private static final String INVALID = "invalid";
    private static final String MULTIPLE_SEPERATOR = ",";
    private static final String QUANTITY_SEPERATOR = ":";
    
    @Autowired
    private IFarmerService farmerService;
    @Autowired
    private ICertificationService certificationService;
    @Autowired
    private IAgentService agentService;

    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        LOGGER.info("----- Periodic Inspection Starts -----");
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String txnType = (String) reqData.get(TransactionProperties.TXN_TYPE);
        PeriodicInspection periodicInspection = new PeriodicInspection();

        // Inspection Type
        String inspectionType = (String) reqData.get(TxnEnrollmentProperties.PERIODIC_INSPECTION_TYPE);
        validateEmptyString(inspectionType, ITxnErrorCodes.EMPTY_INSPECTION_TYPE);
        periodicInspection.setInspectionType(inspectionType);
        periodicInspection.setTxnType(head.getTxnType());
        

        // Inspection Date
        String inputDate = (String) reqData.get(TxnEnrollmentProperties.INSPECTION_DATE);
        Map<String, String> errorCodes = new HashMap<String, String>();
        errorCodes.put(EMPTY, ITxnErrorCodes.EMPTY_INSPECTION_DATE);
        errorCodes.put(INVALID, ITxnErrorCodes.INVALID_INSPECTION_DATE);
        periodicInspection.setInspectionDate(validateDate(inputDate, DateUtil.TXN_TIME_FORMAT,true, errorCodes));

        // Farm Id
        String farmId = (String) reqData.get(TxnEnrollmentProperties.PR_FARM_ID);
        validateEmptyString(farmId, ITxnErrorCodes.EMPTY_PR_FARM_ID);
        Farm farm = null;
        try {
            farm = farmerService.findFarmByfarmId(farmId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            throw new SwitchException(ITxnErrorCodes.INVALID_FARM_ID);
        }
        validateEmptyObject(farm, ITxnErrorCodes.INVALID_FARM_ID);
        periodicInspection.setFarmId(farmId);

        // Purpose
        String purpose = (String) reqData.get(TxnEnrollmentProperties.PERIODIC_PURPOSE);
        if (!StringUtil.isEmpty(purpose))
            periodicInspection.setPurpose(purpose.trim());

        // Remarks
        String remarks = (String) reqData.get(TxnEnrollmentProperties.PERIODIC_REMARKS);
        if (!StringUtil.isEmpty(remarks))
            periodicInspection.setRemarks(remarks.trim());

        // Survival Percentage
        if("lalteer".equalsIgnoreCase(head.getTenantId())|| "lalteerqa".equalsIgnoreCase(head.getTenantId()) ){
            String survivalPercentage = (String) reqData.get(TxnEnrollmentProperties.SURVIVAL);
            if(!StringUtil.isEmpty(survivalPercentage)){
            periodicInspection.setSurvivalPercentage(survivalPercentage);
            }else{
                periodicInspection.setSurvivalPercentage("-1");
            }
            
        }else{
            String survivalPercentage = (String) reqData.get(TxnEnrollmentProperties.SURVIVAL);
            if (!StringUtil.isEmpty(survivalPercentage))
                periodicInspection.setSurvivalPercentage(survivalPercentage.trim());

        }
        
        // Average Height
        String averageHeight = (String) reqData.get(TxnEnrollmentProperties.AVERAGE_HEIGHT);
        if (!StringUtil.isEmpty(averageHeight))
            periodicInspection.setAverageHeight(averageHeight.trim());

        // Average Grith
        String averageGrith = (String) reqData.get(TxnEnrollmentProperties.AVERAGE_GIRTH);
        if (!StringUtil.isEmpty(averageGrith))
            periodicInspection.setAverageGirth(Double.valueOf(averageGrith.trim()));

        // Current Status of growth
        String currentStatus = (String) reqData.get(TxnEnrollmentProperties.CURRENT_STATUS_OF_GROWTH);
        if (!StringUtil.isEmpty(currentStatus))
            periodicInspection.setCurrentStatusOfGrowth(currentStatus);

        // Pest Problem Noticed
        String problemNoticed = (String) reqData.get(TxnEnrollmentProperties.PEST_PROBLEM_NOTICED);
        if (!StringUtil.isEmpty(problemNoticed))
            periodicInspection.setPestProblemNoticed(problemNoticed.trim().charAt(0));
        else
            periodicInspection.setPestProblemNoticed(PeriodicInspection.IN_ACTIVE);

        // Other Pest Symptom
        String otherValue = (String) reqData.get(TxnEnrollmentProperties.OTHER_PEST_SYMPTOM);
        if (!StringUtil.isEmpty(otherValue))
            periodicInspection.setPestSymptomOtherValue(otherValue.trim());

        // Whether the Pest Infestation above ETL
        String pestInfestationETL = (String) reqData.get(TxnEnrollmentProperties.PEST_INFESTATION_ABOUT_ETL);
        if (!StringUtil.isEmpty(pestInfestationETL))
            periodicInspection.setPestInfestationETL(pestInfestationETL.trim().charAt(0));
        else
            periodicInspection.setPestInfestationETL(PeriodicInspection.IN_ACTIVE);

        // Whether the Pest Problem Solved
        String pestProblemSolved = (String) reqData.get(TxnEnrollmentProperties.PEST_PROBLEM_SOLVED);
        if (!StringUtil.isEmpty(pestProblemSolved))
            periodicInspection.setPestProblemSolved(pestProblemSolved.trim().charAt(0));
        else
            periodicInspection.setPestProblemSolved(PeriodicInspection.IN_ACTIVE);

        // Disease Problem Noticed
        String diseaseProblemNoticed = (String) reqData.get(TxnEnrollmentProperties.DISEASE_PROBLEM_NOTICED);
        if (!StringUtil.isEmpty(diseaseProblemNoticed))
            periodicInspection.setDiseaseProblemNoticed(diseaseProblemNoticed.trim().charAt(0));
        else
            periodicInspection.setDiseaseProblemNoticed(PeriodicInspection.IN_ACTIVE);

        // Other Disease Symptom
        otherValue = (String) reqData.get(TxnEnrollmentProperties.OTHER_DISEASE_SYMPTOM);
        if (!StringUtil.isEmpty(otherValue))
            periodicInspection.setDiseaseSymptomOtherValue(otherValue.trim());

        // Whether the Disease Infestation above ETL
        String diseaseInfestationETL = (String) reqData.get(TxnEnrollmentProperties.DISEASE_INFESTATION_ABOUT_ETL);
        if (!StringUtil.isEmpty(diseaseInfestationETL))
            periodicInspection.setDiseaseInfestationETL(diseaseInfestationETL.trim().charAt(0));
        else
            periodicInspection.setDiseaseInfestationETL(PeriodicInspection.IN_ACTIVE);

        // Whether the Disease Problem Solved
        String diseaseProblemSolved = (String) reqData.get(TxnEnrollmentProperties.DISEASE_PROBLEM_SOLVED);
        if (!StringUtil.isEmpty(diseaseProblemSolved))
            periodicInspection.setDiseaseProblemSolved(diseaseProblemSolved.trim().charAt(0));
        else
            periodicInspection.setDiseaseProblemSolved(PeriodicInspection.IN_ACTIVE);

        // Farmers Opinion about the Service
        String farmerOpinion = (String) reqData.get(TxnEnrollmentProperties.FARMER_OPINION_ABOUT_SERVICE);
        if (!StringUtil.isEmpty(farmerOpinion))
            periodicInspection.setFarmerOpinion(farmerOpinion.trim());

        // Is Intercrop
        String interCrop = (String) reqData.get(TxnEnrollmentProperties.IS_INTER_CROP);
        if (!StringUtil.isEmpty(interCrop))
            periodicInspection.setInterCrop(interCrop.trim().charAt(0));
        else
            periodicInspection.setInterCrop(PeriodicInspection.IN_ACTIVE);

        // Name of the Crop
        String nameOfInterCrop = (String) reqData.get(TxnEnrollmentProperties.NAME_OF_CROP);
        if (!StringUtil.isEmpty(nameOfInterCrop))
            periodicInspection.setNameOfInterCrop(nameOfInterCrop.trim());

        // Yield Obtained in MT
        String yieldObtained = (String) reqData.get(TxnEnrollmentProperties.YIELD_OBTAINED_MT);
        if (!StringUtil.isEmpty(yieldObtained))
            periodicInspection.setYieldObtained(Double.valueOf(yieldObtained.trim()));

        // Expenditure Incured
        String expenditureIncurred = (String) reqData
                .get(TxnEnrollmentProperties.EXPENDITURE_INCURED);
        if (!StringUtil.isEmpty(expenditureIncurred))
            periodicInspection.setExpenditureIncurred(Double.valueOf(expenditureIncurred.trim()));

        // Income Generated
        String incomeGenerated = (String) reqData.get(TxnEnrollmentProperties.INCOME_GENERATED);
        if (!StringUtil.isEmpty(incomeGenerated))
            periodicInspection.setIncomeGenerated(Double.valueOf(incomeGenerated.trim()));

        // Net Profit / Loss
        String netProfitOrLoss = (String) reqData.get(TxnEnrollmentProperties.NET_PROFIT_OR_LOSS);
        if (!StringUtil.isEmpty(netProfitOrLoss))
            periodicInspection.setNetProfitOrLoss(Double.valueOf(netProfitOrLoss.trim()));

        // Is Fertilizer Applied
        String isFertilizerApplied = (String) reqData.get(TxnEnrollmentProperties.IS_FERTILIZER_APPLIED);
        periodicInspection.setIsFertilizerApplied(PeriodicInspection.NO);
        if (!StringUtil.isEmpty(isFertilizerApplied)) {
            periodicInspection.setIsFertilizerApplied(isFertilizerApplied.trim().charAt(0));
        }else
            periodicInspection.setIsFertilizerApplied(PeriodicInspection.IN_ACTIVE);

        // Is Field Safety Proposal
        String isFieldSaftyProposal = (String) reqData.get(TxnEnrollmentProperties.IS_SAFETY_DISPOSAL);
        periodicInspection.setIsFieldSafetyProposal(PeriodicInspection.NO);
        if (!StringUtil.isEmpty(isFieldSaftyProposal)) {
            periodicInspection.setIsFieldSafetyProposal(isFieldSaftyProposal.trim().charAt(0));
        }else
            periodicInspection.setIsFieldSafetyProposal(PeriodicInspection.IN_ACTIVE);

        // Chemical Name
        String chemicalName = (String) reqData.get(TxnEnrollmentProperties.CHEMICAL_NAME);
        if (!StringUtil.isEmpty(chemicalName)) {
            periodicInspection.setChemicalName(chemicalName);
        }
        // No of plants replanted
        String plantsReplanted = (String) reqData
                .get(TxnEnrollmentProperties.NO_OF_PLANTS_REPLANNED);
        if (!StringUtil.isEmpty(plantsReplanted))
            periodicInspection.setNoOfPlantsReplanned(plantsReplanted);

        // Date of Gap training
        inputDate = (String) reqData
                .get(TxnEnrollmentProperties.GAP_PLANTING_DATE);
        errorCodes = new HashMap<String, String>();
        errorCodes.put(INVALID, ITxnErrorCodes.INVALID_GAP_PLANTING_DATE);
        periodicInspection.setGapPlantingDate(validateDate(inputDate,
                DateUtil.DATABASE_DATE_FORMAT, false, errorCodes));

        // Month of Fertilizer Applied
        String monthOfFertilizerApplied = (String) reqData.get(TxnEnrollmentProperties.MONTH_OF_FERTILIZER_APPLIED);
        if (!StringUtil.isEmpty(monthOfFertilizerApplied)) {
            periodicInspection.setMonthOfFertilizerApplied(monthOfFertilizerApplied);
        }

        // Month of Pesticide Applied
        String monthOfPesticideApplied = (String) reqData.get(TxnEnrollmentProperties.MONTH_OF_PESTICIDE_APPLIECTION);
        if (!StringUtil.isEmpty(monthOfPesticideApplied)) {
            periodicInspection.setMonthOfPesticideApplication(monthOfPesticideApplied);
        }

        // Month of Fungicide Applied
        String monthOfFungicideApplied = (String) reqData.get(TxnEnrollmentProperties.MONTH_OF_FUNGICIDE_APPLICATION);
        if (!StringUtil.isEmpty(monthOfFungicideApplied)) {
            periodicInspection.setMonthOfFungicideApplication(monthOfFungicideApplied);
        }

        List<PeriodicInspectionData> periodicInspectionDataList = new ArrayList<PeriodicInspectionData>();
        List<PeriodicInspectionSymptom> periodicInspectionSymptomList = new ArrayList<PeriodicInspectionSymptom>();

        // Activities carried out after Previous Visit
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.ACTIVITIES_CARRIED_OUT))) {
            buildPeriodicInspectionData(reqData, TxnEnrollmentProperties.ACTIVITIES_CARRIED_OUT, null, null, null, PeriodicInspection.ACTIVITIES_CARRIED_OUT,periodicInspection, periodicInspectionDataList);
        }
        
        // Inter ploughing with
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.INTERPLOUGHING_WITH))){
            buildPeriodicInspectionData(reqData, TxnEnrollmentProperties.INTERPLOUGHING_WITH, null, null, null, PeriodicInspection.INTERPLOUGHING_WITH,periodicInspection, periodicInspectionDataList);
        }

        // Type of Manure Applied
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TYPE_OF_MANURE_APPLIED))){
        	buildPeriodicInspectionTypeData(reqData, TxnEnrollmentProperties.TYPE_OF_MANURE_APPLIED,
                TxnEnrollmentProperties.OTHER_TYPE_OF_MANURE_APPLIED, "4",
                TxnEnrollmentProperties.MANURE_QUANTITY_APPLIED, PeriodicInspection.MANURE_APPLIED,periodicInspection,
                periodicInspectionDataList);
        }

        // Type of Fertilizer Applied
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.TYPE_OF_FERTILIZER_APPLIED))){
        	buildPeriodicInspectionTypeData(reqData, TxnEnrollmentProperties.TYPE_OF_FERTILIZER_APPLIED,
                TxnEnrollmentProperties.OTHER_TYPE_OF_FERTILIZER_APPLIED, "7",
                TxnEnrollmentProperties.FERTILIZER_QUANTITY_APPLIED, PeriodicInspection.FERTILIZER_APPLIED,periodicInspection,
                periodicInspectionDataList);
        }
        // Name of the Pest*
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.NAME_OF_PEST))){
            buildPeriodicInspectionData(reqData, TxnEnrollmentProperties.NAME_OF_PEST,
                TxnEnrollmentProperties.OTHER_NAME_OF_PEST, "5", null, PeriodicInspection.PEST_NAME,periodicInspection,
                periodicInspectionDataList);
        }
        // Pest Symptoms
        if(head.getTenantId()=="kpf"|| head.getTenantId()=="wub" ||head.getTenantId()=="gar"){
            String pestName = (String) reqData.get(TxnEnrollmentProperties.PEST_SYMPTOM);
            if (!StringUtil.isEmpty(pestName))
                periodicInspection.setPestSymptomsName(pestName);
        }else{
            if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.PEST_SYMPTOM))){
                Map<String, Symptoms> pestStmptomsMap = getSymptomsMap(Symptoms.PEST);
                buildPeriodicInspectionSymptom(reqData, TxnEnrollmentProperties.PEST_SYMPTOM,
                    Symptoms.PEST, pestStmptomsMap, periodicInspection, periodicInspectionSymptomList);
        }
        }
            
       
        // Name of Pesticide Recomended
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.NAME_OF_PESTICIDE_RECOMMENDED))){
        	buildPeriodicInspectionTypeData(reqData,
                TxnEnrollmentProperties.NAME_OF_PESTICIDE_RECOMMENDED,
                TxnEnrollmentProperties.OTHER_NAME_OF_PESTICIDE_RECOMMENTED, "5",
                TxnEnrollmentProperties.PESTICIDE_QUALITY_APPLIED, PeriodicInspection.PESTICIDE_RECOMMENTED,periodicInspection,
                periodicInspectionDataList);
        }
        // Name of the Disease
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.NAME_OF_DISEASE))){
            buildPeriodicInspectionData(reqData, TxnEnrollmentProperties.NAME_OF_DISEASE,
                TxnEnrollmentProperties.OTHER_NAME_OF_DISEASE, "8", null, PeriodicInspection.DISEASE_NAME,periodicInspection,
                periodicInspectionDataList);
        }

        // Disease Symptoms
        if(head.getTenantId()=="kpf"||head.getTenantId()=="wub"||head.getTenantId()=="gar"){
        String diseaseName = (String) reqData.get(TxnEnrollmentProperties.DISEASE_SYMPTOM);
        if (!StringUtil.isEmpty(diseaseName))
            periodicInspection.setDiseaseSymptomsName(diseaseName);
        }else{
            if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.DISEASE_SYMPTOM))){
                Map<String, Symptoms> diseaseStmptomsMap = getSymptomsMap(Symptoms.DISEASE);
                buildPeriodicInspectionSymptom(reqData, TxnEnrollmentProperties.DISEASE_SYMPTOM,
                        Symptoms.DISEASE, diseaseStmptomsMap, periodicInspection,
                        periodicInspectionSymptomList);
            }
        }
        // Name of Fungiside Recomended
        if(!StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.NAME_OF_FUNGISIDE_RECOMMENDED))){
        	buildPeriodicInspectionTypeData(reqData,
                    TxnEnrollmentProperties.NAME_OF_FUNGISIDE_RECOMMENDED,
                    TxnEnrollmentProperties.OTHER_NAME_OF_FUNGICIDE_RECOMMENTED, "5",
                    TxnEnrollmentProperties.FUNGICIDE_QUANTITY_APPLIED, PeriodicInspection.FUNGISIDE_RECOMMENTED,periodicInspection,
                    periodicInspectionDataList);        
        }

        periodicInspection.setPeriodicInspectionData(new LinkedHashSet<PeriodicInspectionData>(periodicInspectionDataList));
        periodicInspection.setPeriodicInspectionSymptoms(new LinkedHashSet<PeriodicInspectionSymptom>(periodicInspectionSymptomList));

        // Audio File
        String voiceData = (String) reqData.get(TxnEnrollmentProperties.FARMER_OR_FS_VOICE);
        byte[] voiceDataContent = null;
        try {
            if (voiceData != null && voiceData.length() > 0) {
                voiceDataContent = Base64.decodeBase64(voiceData.getBytes());
                periodicInspection.setFarmerVoice(voiceDataContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            throw new SwitchException(ITxnErrorCodes.ERROR_WHILE_PROCESSING_FARMER_VOICE);
        }

        Agent agent = agentService.findAgentByAgentId(head.getAgentId());
        if (!ObjectUtil.isEmpty(agent)) {
            periodicInspection.setCreatedUserName(head.getAgentId());
            periodicInspection.setLastUpdatedUserName(head.getAgentId());
        }
        //Crop Code 
        String cropCode = (String) reqData.get(TxnEnrollmentProperties.PR_CROP_CODE);
        validateEmptyString(cropCode, ITxnErrorCodes.EMPTY_PR_CROP);
        ProcurementProduct crop = null;
        try {
        	crop = farmerService.findCropByCropCode(cropCode);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            throw new SwitchException(ITxnErrorCodes.INVALID_CROP_CODE);
        }
        validateEmptyObject(crop, ITxnErrorCodes.INVALID_CROP_CODE);
        periodicInspection.setCropCode(cropCode);
        
        
        //Crop Rotation
       // if("lalteer".equalsIgnoreCase(head.getTenantId()) ||  "pratibha".equalsIgnoreCase(head.getTenantId()) || "agro".equalsIgnoreCase(head.getTenantId()) || "chetna".equalsIgnoreCase(head.getTenantId()) ||  "crsdemo".equalsIgnoreCase(head.getTenantId())){
        String cropRot = (String) reqData.get(TxnEnrollmentProperties.CROP_ROTATION);
        periodicInspection.setCropRotation(StringUtil.isEmpty(cropRot)?"":cropRot);
        
        //Temp
        String temp = (String) reqData.get(TxnEnrollmentProperties.TEMP);
        periodicInspection.setTemperature(StringUtil.isEmpty(temp)?"":temp);
        
        //humidity
        String humidity = (String) reqData.get(TxnEnrollmentProperties.HUMIDITY);
        periodicInspection.setHumidity(StringUtil.isEmpty(humidity)?"":humidity);
        //rain
        String rain = (String) reqData.get(TxnEnrollmentProperties.RAIN);
        periodicInspection.setRain(StringUtil.isEmpty(rain)?"":rain);
        //wind
        String wind = (String) reqData.get(TxnEnrollmentProperties.WIND_SPEED);
        periodicInspection.setWindSpeed(StringUtil.isEmpty(wind)?"":wind);
       // }

        //seasonCode
        String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);
        periodicInspection.setSeasonCode(StringUtil.isEmpty(season)?"":season);
        
        //latitude
        String latitude=  (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
        
        periodicInspection.setLatitude(!StringUtil.isEmpty(latitude) ? latitude : "0");
        
        //longitude
        String longitude=  (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
        periodicInspection.setLongitude(!StringUtil.isEmpty(longitude) ? longitude : "0");
        
        
        String landPrep = (String) reqData.get(TxnEnrollmentProperties.LAND_PREP_COMPLETED);
        if (!StringUtil.isEmpty(landPrep))
            periodicInspection.setLandpreparationCompleted(landPrep.trim());
        else
            periodicInspection.setLandpreparationCompleted("");

        String chemSpray = (String) reqData.get(TxnEnrollmentProperties.CHEMICAL_SPRAY);
        if (!StringUtil.isEmpty(chemSpray))
            periodicInspection.setChemicalSpray(chemSpray.trim());
        else
            periodicInspection.setChemicalSpray("");

        String mono = (String) reqData.get(TxnEnrollmentProperties.MONO_IMIDA);
        if (!StringUtil.isEmpty(mono))
            periodicInspection.setMonoOrImida(mono.trim());
        else
            periodicInspection.setMonoOrImida("");

        String single = (String) reqData.get(TxnEnrollmentProperties.SINGLE_SPRAY_COCKTAIL);
        if (!StringUtil.isEmpty(single))
            periodicInspection.setSingleSprayOrCocktail(single.trim());
        else
            periodicInspection.setSingleSprayOrCocktail("");

        String repe = (String) reqData.get(TxnEnrollmentProperties.REPETITION_PEST);
        if (!StringUtil.isEmpty(repe))
            periodicInspection.setRepetitionOfPest(repe.trim());
        else
            periodicInspection.setRepetitionOfPest("");

        String nitro = (String) reqData.get(TxnEnrollmentProperties.NITROGENOUS_FERT);
        if (!StringUtil.isEmpty(nitro))
            periodicInspection.setNitrogenousFert(nitro.trim());
        else
            periodicInspection.setNitrogenousFert("");

        String last = (String) reqData.get(TxnEnrollmentProperties.CROP_SPACE_LAST_YEAR);

        periodicInspection.setCropSpacingLastYear(last);

        String curr = (String) reqData.get(TxnEnrollmentProperties.CROP_SPACE_CURRENT_YEAR);

        periodicInspection.setCropSpacingCurrentYear(curr);

        String weed = (String) reqData.get(TxnEnrollmentProperties.WEEDING);

        periodicInspection.setWeeding(weed);

        String pick = (String) reqData.get(TxnEnrollmentProperties.PICKING);

        periodicInspection.setPicking(pick);
        
        String cropProtectionPractice = (String) reqData.get(TxnEnrollmentProperties.CROP_PROTECTION_PRACTICE);

        periodicInspection.setCropProtectionPractice(cropProtectionPractice);
        
        periodicInspection.setCreatedDT(new Date());
        periodicInspection.setLastUpdatedDT(new Date());
        periodicInspection.setInspectionImageInfo(buildInspectionImageInfoObject(reqData));
        if(!ObjectUtil.isEmpty(periodicInspection)&&!ObjectUtil.isEmpty(periodicInspection.getInspectionImageInfo())
        		&&!ObjectUtil.isListEmpty(periodicInspection.getInspectionImageInfo().getInspectionImages())){
        	double lat=Double.valueOf(periodicInspection.getInspectionImageInfo().getInspectionImages().iterator().next().getLatitude());
        	double lon=Double.valueOf(periodicInspection.getInspectionImageInfo().getInspectionImages().iterator().next().getLongitude());
        	if(lat!=0&&lon!=0){
        		farm.setLatitude(periodicInspection.getInspectionImageInfo().getInspectionImages().iterator().next().getLatitude());
            	farm.setLongitude(periodicInspection.getInspectionImageInfo().getInspectionImages().iterator().next().getLongitude());
        	}
        	
        }
        periodicInspection.setTxnId((String) reqData.get(TransactionProperties.TXN_ID));
        periodicInspection.setIsDeleted("0");
        periodicInspection.setBranchId(head.getBranchId());
        
        farmerService.editFarm(farm);
        farmerService.editFarmer(farm.getFarmer());
        
        certificationService.addPeriodicInspection(periodicInspection);
        Map resp = new LinkedHashMap();
        LOGGER.info("---------- Periodic Inspection End ----------");
        return resp;
    }

    /**
     * Build periodic inspection data.
     * @param reqData 
     * @param reqKey 
     * @param reqOtherKey 
     * @param otherIndexPos 
     * @param reqQtyKey 
     * @param type 
     * @param periodicInspection 
     * @param periodicInspectionDataList 
     */
    private void buildPeriodicInspectionData(Map<?, ?> reqData, String reqKey, String reqOtherKey,String otherIndexPos, String reqQtyKey, String type,PeriodicInspection periodicInspection,List<PeriodicInspectionData> periodicInspectionDataList) {

        String data = (String) reqData.get(reqKey);

        if (!StringUtil.isEmpty(data)) {
            String[] dataArray = data.split("\\,");

            String otherData = (String) reqData.get(reqOtherKey);
            String qtyData = (String) reqData.get(reqQtyKey);
            Map<String, String> dataQtyMap = new HashMap<String, String>();
            Map<String, String> dataQtyCocMap = new HashMap<String, String>();
            Map<String, String> dataQtyUomMap = new HashMap<String, String>();
            
            if (!StringUtil.isEmpty(qtyData)) {
                dataQtyMap = getQuantityData(qtyData);
                //dataQtyCocMap = getCocData(qtyData);
                dataQtyUomMap = getUomData(qtyData);
            }
            for (String value : dataArray) {
                PeriodicInspectionData periodicInspectionData = new PeriodicInspectionData();
                periodicInspectionData.setCatalogueValue(value);
                periodicInspectionData.setPeriodicInspection(periodicInspection);
                periodicInspectionData.setType(type);

                if (dataQtyMap.containsKey(value)) {
                    periodicInspectionData.setQuantityValue(dataQtyMap.get(value));
                }
                if (value.equals(otherIndexPos)) {
                    periodicInspectionData.setOtherCatalogueValueName(otherData);
                }
                if (dataQtyCocMap.containsKey(value)) {
                    periodicInspectionData.setCocDone(null);
                }
                if (dataQtyUomMap.containsKey(value)) {
                    periodicInspectionData.setUom(dataQtyUomMap.get(value));
                }
                periodicInspectionDataList.add(periodicInspectionData);
            }
        }
    }

    /**`
     * Gets the quantity data.
     * @param qtyData 
     * @return the quantity data
     */
    private Map<String, String> getQuantityData(String qtyData) {

        Map<String, String> qtyMap = new HashMap<String, String>();
        if(!StringUtil.isEmpty(qtyData)){
            String[] qtyArraySet = qtyData.split("\\,");
        if (!ObjectUtil.isEmpty(qtyArraySet) && qtyArraySet.length > 0) {
            for (String dataValue : qtyArraySet) {
                String[] qtyDataArray = dataValue.split("\\:");
                if (!ObjectUtil.isEmpty(qtyDataArray) && qtyDataArray.length >=2 ) {
                    qtyMap.put(qtyDataArray[0], qtyDataArray[1]);
                }
            }
        }
        }
        return qtyMap;
    }
    
    private Map<String, String> getCocData(String qtyData) {

        Map<String, String> qtyMap = new HashMap<String, String>();
        if(!StringUtil.isEmpty(qtyData)){
            String[] qtyArraySet = qtyData.split("\\,");
        if (!ObjectUtil.isEmpty(qtyArraySet) && qtyArraySet.length > 0) {
            for (String dataValue : qtyArraySet) {
                String[] qtyDataArray = dataValue.split("\\:");
                if (!ObjectUtil.isEmpty(qtyDataArray)){
                	if(qtyDataArray.length >=4) 
                    qtyMap.put(qtyDataArray[0], qtyDataArray[2]);
                }
            }
        }
        }
        return qtyMap;
    }

    /**
     * Build periodic inspection symptom.
     * @param reqData 
     * @param reqKey 
     * @param type 
     * @param symptomsMap 
     * @param periodicInspection 
     * @param periodicInspectionSymptomList 
     */
    private void buildPeriodicInspectionSymptom(Map<?, ?> reqData, String reqKey, Integer type,Map<String, Symptoms> symptomsMap, PeriodicInspection periodicInspection,List<PeriodicInspectionSymptom> periodicInspectionSymptomList) {

        String data = (String) reqData.get(reqKey);
        if (!StringUtil.isEmpty(data)) {
            String[] dataArray = data.split("\\,");
            for (String dataValue : dataArray) {
                PeriodicInspectionSymptom periodicInspectionSymptom = new PeriodicInspectionSymptom();
                //if (symptomsMap.containsKey(dataValue)) {
                    periodicInspectionSymptom.setPeriodicInspection(periodicInspection);
                    periodicInspectionSymptom.setType(String.valueOf(type));
                    periodicInspectionSymptom.setSymCode(dataValue);
                //}
                periodicInspectionSymptomList.add(periodicInspectionSymptom);
            }
        }
    }

    /**
     * Gets the symptoms map.
     * @param type 
     * @return the symptoms map
     */
    private Map<String, Symptoms> getSymptomsMap(Integer type) {

        Map<String, Symptoms> symptomMap = new HashMap<String, Symptoms>();
        List<Symptoms> sympotomsList = certificationService.findSymptomsCodeByType(type);
        for (Symptoms symptoms : sympotomsList) {
            symptomMap.put(symptoms.getCode(), symptoms);
        }
        return symptomMap;
    }

    /**
     * Validate empty string.
     * @param input 
     * @param errorCode 
     */
    private void validateEmptyString(String input, String errorCode) {

        if (StringUtil.isEmpty(input))
            throw new SwitchException(errorCode);
    }

    /**
     * Validate empty object.
     * @param input 
     * @param errorCode 
     */
    private void validateEmptyObject(Object input, String errorCode) {

        if (ObjectUtil.isEmpty(input))
            throw new SwitchException(errorCode);
    }

    /**
     * Validate date.
     * @param dateString 
     * @param format 
     * @param isMandatory 
     * @param errorCodes 
     * @return the date
     */
    private Date validateDate(String dateString, String format, boolean isMandatory,
            Map<String, String> errorCodes) {

        if (isMandatory) {
            if (StringUtil.isEmpty(dateString))
                throw new SwitchException(errorCodes.get(EMPTY));
        } else {
            if (StringUtil.isEmpty(dateString))
                return null;
        }
        try {
            return DateUtil.convertStringToDate(dateString.trim(), format);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            throw new SwitchException(errorCodes.get(INVALID));
        }
    }

    /**
     * Build inspection image info object.
     * @param reqData 
     * @return the inspection image info
     */
    public InspectionImageInfo buildInspectionImageInfoObject(Map<?, ?> reqData) {

        Head head = (Head) reqData.get(TxnEnrollmentProperties.HEAD);
        InspectionImageInfo inspectionImageInfo = null;
        Collection photoCollection = (Collection) reqData
                .get(TxnEnrollmentProperties.PHOTO_LIST);
        if (!CollectionUtil.isCollectionEmpty(photoCollection)) {
            Set<InspectionImage> inspectionImageSet = new HashSet<InspectionImage>();
            List<com.sourcetrace.eses.txn.schema.Object> photoDataObject = photoCollection
                    .getObject();

            inspectionImageInfo = new InspectionImageInfo();
            for (com.sourcetrace.eses.txn.schema.Object object : photoDataObject) {
                List<Data> photoDatas = object.getData();
                InspectionImage inspectionImage = new InspectionImage();
                inspectionImage.setInspectionImageInfo(inspectionImageInfo);
                byte[] imageContent = null;

                for (Data data : photoDatas) {
                    String key = data.getKey();
                    String value = (String) data.getValue();

                    // Photo Content
                    if (TxnEnrollmentProperties.PHOTO.equalsIgnoreCase(key)) {
                        DataHandler photo = data.getBinaryValue();
                        try {
                            if (photo != null && photo.getInputStream().available() > 0) {
                                imageContent = IOUtils.toByteArray(photo.getInputStream());
                                inspectionImage.setPhoto(imageContent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new SwitchException(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
                        }
                    }

                    // Photo Capture Time
                    if (TxnEnrollmentProperties.PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
                        if (!StringUtil.isEmpty(value) && !value.equals("0")) {
                            try {
                                Date photoCaptureDate = DateUtil.convertStringToDate(value,
                                        DateUtil.TXN_TIME_FORMAT);
                                inspectionImage.setPhotoCaptureTime(photoCaptureDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new SwitchException(ITxnErrorCodes.DATE_CONVERSION_ERROR);
                            }
                        }
                    }

                    // Photo Capture Latitude
                    if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key))
                    	if(!StringUtil.isEmpty(value)){
                            inspectionImage.setLatitude(value); 
                            }else{
                            	inspectionImage.setLatitude("0");
                            }
                     

                    // Photo Capture Longitude
                    if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key))
                    	if(!StringUtil.isEmpty(value)){
                            inspectionImage.setLongitude(value);
                        	}else{
                        		inspectionImage.setLongitude("0");
                        	}
                       
                }
                inspectionImageSet.add(inspectionImage);
            }
            if (!ObjectUtil.isListEmpty(inspectionImageSet)) {
                inspectionImageInfo.setTxnType(head.getTxnType());
                inspectionImageInfo.setInspectionImages(inspectionImageSet);
            }
        }
        return inspectionImageInfo;
    }

    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        // TODO Auto-generated method stub
        return null;
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
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }
    
    private void buildPeriodicInspectionTypeData(Map<?, ?> reqData, String reqKey, String reqOtherKey,String otherIndexPos, String reqQtyKey, String type,PeriodicInspection periodicInspection,List<PeriodicInspectionData> periodicInspectionDataList) {

        String data = (String) reqData.get(reqKey);

        if (!StringUtil.isEmpty(data)) {
            String[] dataArray = data.split("\\,");

            String otherData = (String) reqData.get(reqOtherKey);
            String qtyData = (String) reqData.get(reqQtyKey);
            Map<String, String> dataQtyMap = new HashMap<String, String>();
            Map<String, String> dataQtyCocMap = new HashMap<String, String>();
            Map<String, String> dataQtyUomMap = new HashMap<String, String>();
            if (!StringUtil.isEmpty(qtyData)) {
                dataQtyMap = getQuantityData(qtyData);
                //dataQtyCocMap = getCocData(qtyData);
                dataQtyUomMap = getUomData(qtyData);
            }
            for (String value : dataArray) {
                PeriodicInspectionData periodicInspectionData = new PeriodicInspectionData();
                periodicInspectionData.setCatalogueValue(value);
                periodicInspectionData.setPeriodicInspection(periodicInspection);
                periodicInspectionData.setType(type);
               

                if (dataQtyMap.containsKey(value)) {
                    periodicInspectionData.setQuantityValue(dataQtyMap.get(value));
                }
                if (value.equals(otherIndexPos)) {
                    periodicInspectionData.setOtherCatalogueValueName(otherData);
                }
                if (dataQtyCocMap.containsKey(value)) {
                    periodicInspectionData.setCocDone(null);
                }
                if (dataQtyUomMap.containsKey(value)) {
                    periodicInspectionData.setUom(dataQtyUomMap.get(value));
                }
                periodicInspectionDataList.add(periodicInspectionData);
            }
        }
    }
    
    private Map<String, String> getUomData(String qtyData) {

        Map<String, String> qtyMap = new HashMap<String, String>();
        if(!StringUtil.isEmpty(qtyData)){
            String[] qtyArraySet = qtyData.split("\\,");
        if (!ObjectUtil.isEmpty(qtyArraySet) && qtyArraySet.length > 0) {
            for (String dataValue : qtyArraySet) {
                String[] qtyDataArray = dataValue.split("\\:");
					if (!ObjectUtil.isEmpty(qtyDataArray)&& qtyDataArray.length >= 2) {
						//if (qtyDataArray.length > 2)
							qtyMap.put(qtyDataArray[0], qtyDataArray[2]);
						/*else
							qtyMap.put(qtyDataArray[0], qtyDataArray[2]);*/
					}
            }
        }
        }
        return qtyMap;
    }
    
}
