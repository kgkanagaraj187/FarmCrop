package com.sourcetrace.eses.dao;

import java.util.List;

import org.apache.log4j.Category;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;

public interface ICatalogueDAO extends IESEDAO{

	
	public FarmCatalogue findCatalogueByName(String name);

	public FarmCatalogue findCatalogueByCode(String code);

	public FarmCatalogue findCatalogueById(Long id);

	public List<FarmCatalogue> listCatalogues();

    public boolean isCatMappedWithAnimalHusbandary(long id);

    public boolean isCatMappedWithFarmInventory(long id);

	public FarmCatalogue findByNameAndType(String educationName, Integer typez);

	public List<FarmCatalogue> listCataloguesByUnit();

	public List<Object[]> listCataloguesByType(int type);

	public FarmCatalogueMaster findFarmCatalogueMasterByName(String name);

	public List<FarmCatalogueMaster> listFarmCatalogueMatsters();

	public FarmCatalogueMaster findFarmCatalogueMasterById(Long id);
	
	public List<FarmCatalogue> findFarmCatalougeByType(int type);


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

	public FarmCatalogue findCatalogueByNameAndType(String name, int typez);

    public List<FarmCatalogue> findSymptomsByCode(String selectedPestVal);
    
    public List<FarmCatalogue> findCatalogueByTypezAndBranch(String typez,String branchId);
    
    public FarmCatalogue findCatalogueByNameAndBranch(String name,String branchId);
    
    public List<String> findCatalogueCodeByNameAndBranch(String selectedStapleLen, String branchId);

	public List<FarmCatalogue> listCataloguesByType(String text);

	public List<Object[]> findCatalogueCodeAndNameByType(int type);

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
