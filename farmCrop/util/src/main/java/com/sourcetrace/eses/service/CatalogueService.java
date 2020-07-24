package com.sourcetrace.eses.service;

import java.util.List;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.ICatalogueDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Service
@Transactional
public class CatalogueService implements ICatalogueService {

	@Autowired
	private ICatalogueDAO catalogueDAO;

	public ICatalogueDAO getCatalogueDAO() {
		return catalogueDAO;
	}

	public void setCatalogueDAO(ICatalogueDAO catalogueDAO) {
		this.catalogueDAO = catalogueDAO;
	}

	public void addCatalogue(FarmCatalogue farmcatalogue) {
		catalogueDAO.save(farmcatalogue);
		if(farmcatalogue.getLanguagePreferences()!=null && !ObjectUtil.isListEmpty(farmcatalogue.getLanguagePreferences())){
		for (LanguagePreferences preferences : farmcatalogue.getLanguagePreferences()) {
			preferences.setCode(farmcatalogue.getCode());
			if(preferences.getName().isEmpty()){
				preferences.setName(farmcatalogue.getName());
			}
		
			catalogueDAO.save(preferences);
		}
		}

	}

	public void editCatalogue(FarmCatalogue farmcatalogue) {
		catalogueDAO.update(farmcatalogue);
		/*for (LanguagePreferences preferences : farmcatalogue.getLanguagePreferences()) {
			if (!ObjectUtil.isEmpty(preferences)){
				if(preferences.getName()==null || StringUtil.isEmpty(preferences.getName())){
					preferences.setName(farmcatalogue.getName());
				}
				catalogueDAO.update(preferences);
			}
		}*/

	}

	public FarmCatalogue findCatalogueById(Long id) {
		return catalogueDAO.findCatalogueById(id);
	}

	public FarmCatalogue findCatalogueByName(String name) {
		return catalogueDAO.findCatalogueByName(name);
	}

	public List<FarmCatalogue> listCatalogues() {
		return catalogueDAO.listCatalogues();
	}

	public void removeCatalogue(String name) {
		catalogueDAO.delete(findCatalogueByName(name));
	}

	public FarmCatalogue findCatalogueByCode(String i) {
		return catalogueDAO.findCatalogueByCode(i);
	}

	public boolean isCatMappedWithAnimalHusbandary(long id) {

		return catalogueDAO.isCatMappedWithAnimalHusbandary(id);
	}

	public boolean isCatMappedWithFarmInventory(long id) {

		return catalogueDAO.isCatMappedWithFarmInventory(id);
	}
	

	@Override
	public FarmCatalogue findByNameAndType(String educationName, Integer typez) {
		// TODO Auto-generated method stub
		return catalogueDAO.findByNameAndType(educationName,typez);
		
	}

	@Override
	public List<FarmCatalogue> listCataloguesByUnit() {
		// TODO Auto-generated method stub
		return catalogueDAO.listCataloguesByUnit();
	}

	@Override
	public List<Object[]> listCataloguesByType(int type) {
		// TODO Auto-generated method stub
		return catalogueDAO.listCataloguesByType(type);
	}

	@Override
	public void addFarmCatalogueMaster(FarmCatalogueMaster catalogueMaster) {
		// TODO Auto-generated method stub
		catalogueDAO.save(catalogueMaster);
	}

	@Override
	public FarmCatalogueMaster findFarmCatalogueMasterByName(String name) {
		// TODO Auto-generated method stub
		return catalogueDAO.findFarmCatalogueMasterByName(name);
	}

	@Override
	public List<FarmCatalogueMaster> listFarmCatalogueMatsters() {
		// TODO Auto-generated method stub
		return catalogueDAO.listFarmCatalogueMatsters();
	}

	@Override
	public FarmCatalogueMaster findFarmCatalogueMasterById(Long id) {
		// TODO Auto-generated method stub
		return catalogueDAO.findFarmCatalogueMasterById(id);
	}

	@Override
	public List<FarmCatalogue> findFarmCatalougeByType(int type) {
		return catalogueDAO.findFarmCatalougeByType(type);
	}

    @Override
    public List<Object[]> findCatalogueCodeAndNameByType(int type) {

        return catalogueDAO.findCatalogueCodeAndNameByType(type);
    }

    @Override
    public List<Object[]> loadSchemeBasedOnType() {

        return catalogueDAO.loadSchemeBasedOnType();
    }

    @Override
    public List<Object[]> loadlandlessBenefitBasedOnType() {

        return catalogueDAO.loadlandlessBenefitBasedOnType();
    }
    
    @Override
    public String findCatalogueValueByCode(String code) {

        return catalogueDAO.findCatalogueValueByCode(code);
    }

	@Override
	public List<FarmCatalogue> findFarmCatalougeByAlpha(Integer type) {
		// TODO Auto-generated method stub
		return catalogueDAO.findFarmCatalougeByAlpha(type);
	}

	@Override
	public List<Object[]> listFramEquipments() {
		// TODO Auto-generated method stub
		return catalogueDAO.listFramEquipments();
	}
	
    @Override
    public List<Object[]> loadICSName() {

        return catalogueDAO.loadICSName();
    }

    @Override
    public List<Object[]> loadICSUnit() {

        return catalogueDAO.loadICSUnit();
    }

    @Override
    public List<Object[]> loadICSRegNo() {

        return catalogueDAO.loadICSRegNo();
    }
    
    @Override
    public FarmCatalogueMaster findFarmCatalogueMasterByCatalogueTypez(Integer valueOf){
        return catalogueDAO.findFarmCatalogueMasterByCatalogueTypez(valueOf);
    }

    @Override
    public List<Object[]> loadTopography() {

        return catalogueDAO.loadTopography();
    }

	@Override
	public List<FarmCatalogue> listFarmCatalogueWithOther(int type, int otherValue) {
		return catalogueDAO.listFarmCatalogueWithOther(type, otherValue);
	}

    @Override
    public List<FarmCatalogue> findSymptomsByCode(String selectedPestVal) {

        return catalogueDAO.findSymptomsByCode(selectedPestVal);
    }
    
    @Override
    public List<FarmCatalogue> findCatalogueByTypezAndBranch(String typez,String branchId){
    	return catalogueDAO.findCatalogueByTypezAndBranch(typez, branchId);
    }

	@Override
	public FarmCatalogue findCatalogueByNameAndBranch(String name, String branchId) {
		return catalogueDAO.findCatalogueByNameAndBranch(name, branchId);
	}
	
	@Override
	public List<String> findCatalogueCodeByNameAndBranch(String selectedStapleLen, String branchId) {
		// TODO Auto-generated method stub
		return catalogueDAO.findCatalogueCodeByNameAndBranch(selectedStapleLen,branchId);
	}

	@Override
	public List<FarmCatalogue> listCataloguesByType(String text) {
		return catalogueDAO.listCataloguesByType(text);
	}
	
	@Override
    public List<Object[]> loadBenefitFarmerByType() {

        return catalogueDAO.loadBenefitFarmerByType();
    }

	@Override
	public List<FarmCatalogueMaster> listFarmCatalogueMatstersByStatusSurvey() {
		 return catalogueDAO.listFarmCatalogueMatstersByStatusSurvey();
	}

	@Override
	public List<FarmCatalogue> listCataloguesByTypeArray(List<Integer> typeList) {
		
		 return catalogueDAO.listCataloguesByTypeArray(typeList);
	}
	
	public List<FarmCatalogue> findCatalogueTypeByCataloueCode(List<String> catalogueType) {
		 return catalogueDAO.findCatalogueTypeByCataloueCode(catalogueType);
	}

	

	@Override
	public List<Object[]> findCatalogueCodeAndDisNameByType(Integer type) {
		// TODO Auto-generated method stub
		 return catalogueDAO.findCatalogueCodeAndDisNameByType(type);
	}

	@Override
	public FarmCatalogue findCatalogueByCode(String heap, String tenantId) {
		return catalogueDAO.findCatalogueByCode(heap,tenantId);
	}

	@Override
	public List<Object[]> listOfViewByDBName(String dbName) {
		return catalogueDAO.listOfViewByDBName(dbName);
	}

	@Override
	public List<Object> listFieldsByViewNameAndDBName(String dbName, String viewName) {
		return catalogueDAO.listFieldsByViewNameAndDBName(dbName, viewName);
	}

	@Override
	public List<Object[]> listCataloguesByCodes(List<String> codes, String lan) {
		return catalogueDAO.listCataloguesByCodes(codes,lan);
	}
	
}
