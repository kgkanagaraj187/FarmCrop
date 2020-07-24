package com.sourcetrace.eses.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;

public interface  ICatalogueService {

	public void addCatalogue(FarmCatalogue farmcatalogue);

	public void removeCatalogue(String name);

	public void editCatalogue(FarmCatalogue farmcatalogue);

	public FarmCatalogue findCatalogueByName(String name);

	public FarmCatalogue findCatalogueById(Long id);
	
	public List<FarmCatalogue> listCatalogues();

    public FarmCatalogue findCatalogueByCode(String id);

    public boolean isCatMappedWithFarmInventory(long id);

    public boolean isCatMappedWithAnimalHusbandary(long id);
    
	public FarmCatalogue findByNameAndType(String educationName, Integer typez);

	public List<FarmCatalogue> listCataloguesByUnit();
	
	public List<Object[]> listCataloguesByType(int type);

	public void addFarmCatalogueMaster(FarmCatalogueMaster catalogueMaster);

	public FarmCatalogueMaster findFarmCatalogueMasterByName(String trim);

	public List<FarmCatalogueMaster> listFarmCatalogueMatsters();

	public FarmCatalogueMaster findFarmCatalogueMasterById(Long valueOf);
	
	public List<FarmCatalogue> findFarmCatalougeByType(int type);

    public List<Object[]> findCatalogueCodeAndNameByType(int type);

    public List<Object[]> loadSchemeBasedOnType();

    public List<Object[]> loadlandlessBenefitBasedOnType();
    
    public String findCatalogueValueByCode(String code);

	public List<FarmCatalogue> findFarmCatalougeByAlpha(Integer type);

	public List<Object[]> listFramEquipments();
	
	public List<Object[]> loadICSName();

	public List<Object[]> loadICSUnit();

	public List<Object[]> loadICSRegNo();
	
	public FarmCatalogueMaster findFarmCatalogueMasterByCatalogueTypez(Integer valueOf);

    public List<Object[]> loadTopography();
    
    public List<FarmCatalogue> listFarmCatalogueWithOther(int type, int otherValue);

    public List<FarmCatalogue> findSymptomsByCode(String selectedPestVal);
    
    public List<FarmCatalogue> findCatalogueByTypezAndBranch(String typez,String branchId);
    
    public FarmCatalogue findCatalogueByNameAndBranch(String name,String branchId);
    public List<String> findCatalogueCodeByNameAndBranch(String selectedStapleLen, String branchId);

	public List<FarmCatalogue> listCataloguesByType(String text);
	
	public List<Object[]> loadBenefitFarmerByType();

	public List<FarmCatalogueMaster> listFarmCatalogueMatstersByStatusSurvey();
	
	public List<FarmCatalogue> listCataloguesByTypeArray(List<Integer> typeList);

	public List<Object[]> findCatalogueCodeAndDisNameByType(Integer type);

	public FarmCatalogue findCatalogueByCode(String heap, String tenantId);
	
	public List<FarmCatalogue> findCatalogueTypeByCataloueCode(List<String> catalogueType);
	
	public List<Object[]> listOfViewByDBName(String dbName);

	public List<Object> listFieldsByViewNameAndDBName(String dbName, String viewName);

	public List<Object[]> listCataloguesByCodes(List<String> codes, String lan);
	
	
}


