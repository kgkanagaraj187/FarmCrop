package com.sourcetrace.eses.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Category;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmInventory;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class CatalogueDAO extends ESEDAO implements ICatalogueDAO {

	@Autowired
	public CatalogueDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	public FarmCatalogue findCatalogueByCode(String code) {
		return (FarmCatalogue) find("FROM FarmCatalogue cg WHERE cg.code = ?", code);
	}

	public FarmCatalogue findCatalogueById(Long id) {
		return (FarmCatalogue) find("FROM FarmCatalogue cg WHERE cg.id = ?", id);
	}

	public FarmCatalogue findCatalogueByName(String name) {
		return (FarmCatalogue) find("FROM FarmCatalogue cg WHERE cg.name = ?", name);
	}

	public List<FarmCatalogue> listCatalogues() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cg where cg.status=? ORDER BY cg.name ASC", values);
	}

	public boolean isCatMappedWithFarmInventory(long id) {

		List<FarmInventory> farmerList = list("FROM FarmInventory sd WHERE sd.inventoryItem.id = ? ", id);
		if (!ObjectUtil.isListEmpty(farmerList)) {
			return true;
		}
		return false;
	}

	public boolean isCatMappedWithAnimalHusbandary(long id) {
		String fid = String.valueOf(id);
		Object[] values = { id, id, fid };
		List<AnimalHusbandary> farmerList = list(
				"FROM AnimalHusbandary sd WHERE sd.farmAnimal.id = ? OR sd.animalHousing.id=? OR sd.fodder=?", values);
		if (!ObjectUtil.isListEmpty(farmerList)) {
			return true;
		}
		return false;
	}

	@Override
	public FarmCatalogue findByNameAndType(String educationName, Integer typez) {
		// TODO Auto-generated method stub
		Object[] values = { educationName, typez };
		return (FarmCatalogue) find("FROM FarmCatalogue cg WHERE cg.name = ? and typez=? ", values);
	}

	@Override
	public List<FarmCatalogue> listCataloguesByUnit() {
		// TODO Auto-generated method stub
		Object[] values = { Product.listUnitBasedOnUOM, FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cg WHERE cg.typez=? and cg.status=?", values);
	}

	@Override
	public List<Object[]> listCataloguesByType(int type) {
		// TODO Auto-generated method stub
		Object[] values = { type, ESEAccount.ACTIVE };
		return list("SELECT fc.id,fc.name FROM  FarmCatalogueMaster fc WHERE fc.typez=? and fc.status=?", values);
	}

	@Override
	public FarmCatalogueMaster findFarmCatalogueMasterByName(String name) {
		// TODO Auto-generated method stub
		Object[] values = { name, ESEAccount.ACTIVE };
		return (FarmCatalogueMaster) find("FROM FarmCatalogueMaster fcm WHERE fcm.name=? and fcm.status=?", values);
	}

	@Override
	public List<FarmCatalogueMaster> listFarmCatalogueMatsters() {
		// TODO Auto-generated method stub
		return list("FROM FarmCatalogueMaster fc where fc.status=? ORDER BY fc.name", ESEAccount.ACTIVE);
	}

	@Override
	public FarmCatalogueMaster findFarmCatalogueMasterById(Long id) {
		// TODO Auto-generated method stub
		return (FarmCatalogueMaster) find("FROM FarmCatalogueMaster fm WHERE fm.id=?", id);
	}

	@Override
	public List<FarmCatalogue> findFarmCatalougeByType(int type) {
		return list("FROM FarmCatalogue fc where fc.typez=? and fc.status='1'", type);
	}

	public List<Object[]> findCatalogueCodeAndNameByType(int type) {

		return list("SELECT fc.code,fc.name,fc.dispName FROM  FarmCatalogue fc WHERE fc.typez=? ORDER BY fc.name ASC",type);
	}

	@Override
	public List<Object[]> loadSchemeBasedOnType() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='20' and fc.status='1' ORDER BY fc.name ASC");
	}

	@Override
	public List<Object[]> loadlandlessBenefitBasedOnType() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='26' and fc.status='1' ORDER BY fc.name ASC");
	}

	@Override
	public String findCatalogueValueByCode(String code) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("SELECT fc.name FROM FarmCatalogue fc  WHERE fc.code=:code");
		query.setParameter("code", code);
		List<String> codeList = query.list();
		String codeVal = null;
		if (codeList.size() > 0) {
			codeVal = codeList.get(0);
		}
		session.flush();
		session.close();
		return codeVal;
	}
	
	@Override
	public List<FarmCatalogue> findFarmCatalougeByAlpha(Integer type) {
		// TODO Auto-generated method stub
		Object[] values = { type, FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue fc where fc.typez=? and fc.status=? ORDER BY fc.name ASC", values);
	}

	@Override
	public List<Object[]> listFramEquipments() {
		// TODO Auto-generated method stub
		return list("SELECT fc.code,fc.name FROM FarmCatalogue fc where fc.typez=? ORDER BY fc.name ASC",
				FarmCatalogue.FARM_EQUIPMENT);
	}

	@Override
	public List<Object[]> loadICSName() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='27'  ORDER BY fc.name ASC");
	}

	@Override
	public List<Object[]> loadICSUnit() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='28'  ORDER BY fc.name ASC");
	}

	@Override
	public List<Object[]> loadICSRegNo() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='29'  ORDER BY fc.name ASC");
	}

	@Override
	public FarmCatalogueMaster findFarmCatalogueMasterByCatalogueTypez(Integer valueOf) {
		List<Integer> intList =  new ArrayList<>();
		intList.add(ESEAccount.ACTIVE);
		intList.add(FarmCatalogueMaster.STATUS_SURVEY);
	
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM FarmCatalogueMaster fm WHERE fm.typez=:type and fm.status in (:vals) ");
		query.setParameter("type", valueOf);
		query.setParameterList("vals", intList);
		try{
			FarmCatalogueMaster codeList = (FarmCatalogueMaster) query.list().get(0);
			session.flush();
			session.close();
			return codeList;
		}catch (Exception e) {
			session.flush();
			session.close();
			return null;
		}
		
		
	

	}

	@Override
	public List<Object[]> loadTopography() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='39' and fc.status='1' ORDER BY fc.name ASC");
	}

	@Override
	public List<FarmCatalogue> listFarmCatalogueWithOther(int type, int otherValue) {
		return list("FROM FarmCatalogue fc where fc.typez in (?,?) and fc.status=?",
				new Object[] { type, FarmCatalogue.OTHER, FarmCatalogue.ACTIVE });
	}

	@Override
	public FarmCatalogue findCatalogueByNameAndType(String name, int typez) {
		return (FarmCatalogue) find("FROM FarmCatalogue cg WHERE cg.name = ? and cg.typez=?",
				new Object[] { name, typez });
	}

	@Override
	public List<FarmCatalogue> findSymptomsByCode(String selectedPestVal) {
		return list("FROM FarmCatalogue fc where fc.parentId=?", selectedPestVal);
	}

	@Override
	public List<FarmCatalogue> findCatalogueByTypezAndBranch(String typez, String branchId) {
		int type = Integer.parseInt(typez);
		return list("FROM FarmCatalogue fc where fc.typez=? ORDER BY fc.id ASC",
				new Object[] {  type });
	}

	@Override
	public FarmCatalogue findCatalogueByNameAndBranch(String name, String branchId) {
		return (FarmCatalogue) find("from FarmCatalogue fc where fc.name=? and fc.branchId=?",
				new Object[] { name, branchId });
	}

	@Override
	public List<String> findCatalogueCodeByNameAndBranch(String selectedStapleLen, String branchId) {
		// TODO Auto-generated method stub
		Object[] parmValues = { selectedStapleLen, branchId };
		if (!StringUtil.isEmpty(branchId)) {
			return list("SELECT fc.code FROM FarmCatalogue fc WHERE fc.name = ? and fc.branchId=? ", parmValues);
		} else {
			return list("SELECT fc.code FROM FarmCatalogue fc WHERE fc.name = ? ", selectedStapleLen);
		}
	}

	@Override
	public List<FarmCatalogue> listCataloguesByType(String text) {
		Object[] values = { FarmCatalogue.ACTIVE ,Integer.parseInt(text)};
		return list("FROM FarmCatalogue cg where cg.status=? and cg.typez = ? ORDER BY cg.code ASC", values);
	}
	
	@Override
	public List<FarmCatalogue> listCataloguesByTypeArray(List<Integer> typeList) {
		
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM FarmCatalogue fc WHERE fc.typez in (:type) and fc.status = :val");
		query.setParameterList("type", typeList);
		query.setParameter("val", FarmCatalogue.ACTIVE);
		 List<FarmCatalogue> fcList = query.list();
			session.flush();
		session.close();
		return  fcList;
	}
	
	
	
	@Override
	public List<Object[]> loadBenefitFarmerByType() {

		return list("SELECT fc.code,fc.name FROM  FarmCatalogue fc WHERE fc.typez='19' and fc.status='1' ORDER BY fc.name ASC");
	}

	@Override
	public List<FarmCatalogueMaster> listFarmCatalogueMatstersByStatusSurvey() {
		// TODO Auto-generated method stub
				return list("FROM FarmCatalogueMaster fc where fc.status=? ORDER BY fc.name",FarmCatalogueMaster.STATUS_SURVEY);
	}

	@Override
	public List<Object[]> findCatalogueCodeAndDisNameByType(Integer type) {
		// TODO Auto-generated method stub
		return list("SELECT fc.code,fc.name,fc.dispName FROM  FarmCatalogue fc WHERE fc.typez=? ORDER BY fc.dispName ASC",type);
	}

	@Override
	public FarmCatalogue findCatalogueByCode(String heap, String tenantId) {
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM FarmCatalogue cw WHERE cw.code=:code");
		query.setParameter("code", heap);

		List<FarmCatalogue> masterDataList = query.list();

		FarmCatalogue masterData = null;
		if (masterDataList.size() > 0) {
			masterData = (FarmCatalogue) masterDataList.get(0);
		}

		session.flush();
		session.close();
		
		
		return masterData;
	}
	
	public List<FarmCatalogue> findCatalogueTypeByCataloueCode(List<String> catalogueCode) {
		
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM FarmCatalogue cg WHERE cg.code  in (:code) and cg.status = :val");
		query.setParameterList("code", catalogueCode);
		query.setParameter("val", FarmCatalogue.ACTIVE);
		 List<FarmCatalogue> fcList = query.list();
			session.flush();
		session.close();
		return  fcList;
	}
	
	@Override
	public List<Object[]> listOfViewByDBName(String dbName) {
		Session session = getSessionFactory().openSession();
		String queryString = "SHOW FULL TABLES IN "+dbName+" WHERE TABLE_TYPE LIKE 'VIEW'";
		Query query = session.createSQLQuery(queryString);

		List <Object[]>list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<Object> listFieldsByViewNameAndDBName(String dbName, String viewName) {
		Session session = getSessionFactory().openSession();
		String queryString = "select column_name from information_schema.columns where table_name='"+viewName+"' and table_schema='"+dbName+"'";
		Query query = session.createSQLQuery(queryString);

		List <Object>list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<Object[]> listCataloguesByCodes(List<String> codes,String lan) {
		Session session = getSessionFactory().openSession();
		String queryString = "select  ct.code,IFNULL(lp.`NAME`,ct.name)  from catalogue_value ct left join language_pref  lp on lp.code = ct.code and ct.code is not null and ct.code in (:codes) and lp.LTYPE='0' and lp.LANG='"+lan+"';";
		Query query = session.createSQLQuery(queryString);
query.setParameterList("codes", codes);
		List <Object[]>list = query.list();
		session.flush();
		session.close();
		return list;
	}
	
	
}
