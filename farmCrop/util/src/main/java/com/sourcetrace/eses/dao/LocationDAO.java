/*
 * LocationDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.traceability.HeapData;
import com.ese.entity.traceability.HeapDataDetail;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.LocationHistoryDetail;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.entity.Warehouse.WarehouseTypes;
import com.sourcetrace.eses.order.entity.txn.TripSheet;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Location;
import com.sourcetrace.eses.util.entity.LocationLevel;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.GMO;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

/**
 * The Class LocationDAO.
 */
@Repository
@Transactional
public class LocationDAO extends ESEDAO implements ILocationDAO {

	@Autowired
	public LocationDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	@Override
	public List<Location> listLocations() {
		// TODO Auto-generated method stub
		return list("FROM Location l ORDER BY l.name ASC");
	}

	@Override
	public Location findLocationById(long id) {
		// TODO Auto-generated method stub
		return (Location) find("from Location l where l.id = ?", new Object[] { id });
	}

	@Override
	public Location findLocationByCode(String code) {
		// TODO Auto-generated method stub
		return (Location) find("from Location l LEFT JOIN FETCH l.childs c where l.code = ?", code);
	}

	@Override
	public Location findLocationByName(String name) {
		// TODO Auto-generated method stub
		return (Location) find("from Location l where l.name = ?", name);
	}

	@Override
	public LocationLevel findLevelByCode(String levelCode) {
		// TODO Auto-generated method stub
		return (LocationLevel) find("FROM LocationLevel l WHERE l.code=?", levelCode);
	}

	@Override
	public List<Object[]> listLocationsInfoByLevelCode(String levelCode) {
		// TODO Auto-generated method stub
		return list("SELECT l.code,l.name FROM Location l WHERE l.level.code=?", levelCode);
	}

	@Override
	public List<Object[]> listLocationsInfoByParentCode(String parentCode) {
		// TODO Auto-generated method stub
		return list("SELECT c.code,c.name FROM Location l INNER JOIN l.childs c where l.code = ?", parentCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCountryByName(java
	 * .lang.String)
	 */
	public Country findCountryByName(String name) {

		Country country = (Country) find("FROM Country c LEFT JOIN FETCH c.states st WHERE c.name = ?", name);
		return country;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCountryByCode(java
	 * .lang.String)
	 */
	public Country findCountryByCode(String code) {

		Country country = (Country) find("FROM Country c LEFT JOIN FETCH c.states WHERE c.code = ?", code);
		return country;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findLocalityByName(java
	 * .lang.String)
	 */
	public Locality findLocalityByName(String name) {

		Locality locality = (Locality) find("FROM Locality l LEFT JOIN FETCH l.municipalities WHERE l.name = ?", name);
		return locality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findLocalityByCode(java
	 * .lang.String)
	 */
	public Locality findLocalityByCode(String code) {

		Locality locality = (Locality) find("FROM Locality l LEFT JOIN FETCH l.municipalities WHERE l.code = ?", code);
		return locality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findMunicipalityByName
	 * (java.lang.String)
	 */
	public Municipality findMunicipalityByName(String name) {

		Municipality municipality = (Municipality) find("FROM Municipality m WHERE m.name = ?", name);
		return municipality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findMunicipality(java.
	 * lang.String, java.lang.String)
	 */
	public Municipality findMunicipality(String name, String postalCode) {

		Object[] bindValues = { name, postalCode };
		Municipality municipality = (Municipality) find("FROM Municipality m WHERE m.name = ? and m.postalCode=?",
				bindValues);
		return municipality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findMunicipalitiesByPostalCode (java.lang.String)
	 */
	public List<Municipality> findMunicipalitiesByPostalCode(String postalCode) {

		return list("FROM Municipality m WHERE m.postalCode = ?", postalCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findMunicipalityByCoordinates (java.lang.String, java.lang.String)
	 */
	public Municipality findMunicipalityByCoordinates(String latitude, String longitude) {

		Object[] bindValues = { latitude, longitude };
		Municipality municipality = (Municipality) find("FROM Municipality m WHERE m.latitude = ? and m.longitude = ?",
				bindValues);
		return municipality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findStateByName(java.lang
	 * .String)
	 */
	public State findStateByName(String name) {

		State state = (State) find("FROM State s LEFT JOIN FETCH s.localities WHERE s.name = ?", name);
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findStateByCode(java.lang
	 * .String)
	 */
	public State findStateByCode(String code) {

		State state = (State) find("FROM State s LEFT JOIN FETCH s.localities WHERE s.code = ?", code);
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCountryById(java.lang
	 * .Long)
	 */
	public Country findCountryById(Long id) {

		Country country = (Country) find("FROM Country c LEFT JOIN FETCH c.states WHERE c.id = ?", id);
		return country;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findLocalityById(java.
	 * lang.Long)
	 */
	public Locality findLocalityById(Long id) {

		Locality locality = (Locality) find("FROM Locality l WHERE l.id = ?", id);
		return locality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findMunicipalityById(java
	 * .lang.Long)
	 */
	public Municipality findMunicipalityById(Long id) {

		Municipality municipality = (Municipality) find("FROM Municipality m WHERE m.id = ?", id);
		return municipality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findStateById(java.lang
	 * .Long)
	 */
	public State findStateById(Long id) {

		State state = (State) find("FROM State s WHERE s.id = ?", id);
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listCountries()
	 */
	public List<Country> listCountries() {

		return list("FROM Country c ORDER BY c.name ASC");
	}
	
	public List<Object[]> listStates() {
		return list("select DISTINCT s.code,s.name,s.id FROM State s ORDER BY s.name ASC ");
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listLocalities(java.lang
	 * .String)
	 */
	public List<Locality> listLocalities(String state) {

		return list("FROM Locality l WHERE l.state.code = ? ORDER BY l.name ASC", state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listMunicipalities(java
	 * .lang.String)
	 */
	public List<Municipality> listMunicipalities(String locality) {

		return list("FROM Municipality m WHERE m.locality.code = ? ORDER BY m.name ASC", locality);
	}

	public List<Municipality> listMunicipalitiesByCode(String locality) {

		return list("FROM Municipality m WHERE m.locality.code = ? ORDER BY m.name ASC", locality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listStates(java.lang.
	 * String )
	 */
	public List<State> listStates(String country) {

		return list("FROM State s WHERE s.country.name = ? ORDER BY s.name ASC", country);
	}

	public List<State> listStatesByCode(String country) {

		return list("FROM State s WHERE s.country.code = ? ORDER BY s.name ASC", country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findMunicipalityByCode
	 * (java.lang.String)
	 */
	public Municipality findMunicipalityByCode(String code) {

		return (Municipality) find("FROM Municipality m WHERE m.code = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findVillageByCode(java
	 * .lang.String)
	 */
	public Village findVillageByCode(String villageCode) {

		return (Village) find("FROM Village v WHERE v.code = ?", villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#findVillageById(long)
	 */
	public Village findVillageById(long id) {

		return (Village) find("FROM Village v WHERE v.id= ? ", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findVillageByName(java
	 * .lang.String)
	 */
	public Village findVillageByName(String name) {

		return (Village) find("FROM Village v WHERE v.name= ? ", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillages(java.lang
	 * .String)
	 */
	public List<Village> listVillages(String selectedCity) {

		return list("FROM Village v WHERE v.city.name = ? ORDER BY v.name ASC", selectedCity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findWarehouseByCode(java
	 * .lang.String)
	 */
	public Warehouse findWarehouseByCode(String warehouseCode) {

		// Object[] values = { warehouseCode,
		// Warehouse.WarehouseTypes.COOPERATIVE.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.code = ?", warehouseCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findWarehouseById(long)
	 */
	public Warehouse findWarehouseById(long id) {

		Object[] values = { id, Warehouse.WarehouseTypes.COOPERATIVE.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findWarehouseByName(java
	 * .lang.String)
	 */
	public Warehouse findWarehouseByName(String name) {

		Object[] values = { name, Warehouse.WarehouseTypes.COOPERATIVE.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.name= ? AND wh.typez=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#getCityList()
	 */
	public List listCity() {

		return list("FROM Municipality m ORDER BY m.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#getVillageList()
	 */
	public List listVillage() {

		return list("FROM Village v ORDER BY v.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listWarehouseByCity(long)
	 */
	public List<Warehouse> listWarehouseByCity(long id) {

		return list("FROM Warehouse wh WHERE wh.city.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listWarehouse()
	 */
	public List<Warehouse> listWarehouse() {

		return list("FROM Warehouse w where w.typez=? ORDER BY w.name ASC", Warehouse.COOPERATIVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillageByCity(long)
	 */
	public List<Village> listVillageByCity(long id) {

		return list("FROM Village v WHERE v.city.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * isCityMappingExistsWithWarehouse(long)
	 */
	public boolean isCityMappingExistsWithWarehouse(long cityId) {

		List<Warehouse> warehouseList = list("FROM Warehouse w WHERE w.city.id = ?", cityId);
		if (!ObjectUtil.isListEmpty(warehouseList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillageByCityId(long)
	 */
	public List<Village> listVillageByCityId(long id) {

		String queryString = "SELECT * FROM village INNER JOIN farmer ON farmer.VILLAGE_ID = village.ID WHERE village.CITY_ID = '"
				+ id + "' GROUP BY village.ID ORDER BY village.name";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString).addEntity(Village.class);
		List<Village> list = query.list();
		sessions.flush();
		sessions.close();
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findMunicipalityByVillageCode (java.lang.String )
	 */
	public Municipality findMunicipalityByVillageCode(String villageCode) {

		return (Municipality) find("SELECT m FROM Municipality m LEFT JOIN m.villages v WHERE v.code=?", villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listDeviceLocationHistory
	 * (java.lang.String, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<LocationHistory> listDeviceLocationHistory(String serialNumber, Date startDate, Date endDate) {

		Object[] bindValues = { serialNumber, startDate, endDate };
		return list(
				"FROM LocationHistory lh WHERE lh.serialNumber=? AND lh.txnTime BETWEEN ? AND ? ORDER BY lh.txnTime",
				bindValues);
	}

	public long findThresholdExceededLocationHistory(String serialNumber, int thresholdValue) {

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session
				.createSQLQuery(
						"Select ID FROM LOC_HISTORY WHERE LOC_HISTORY.SERIAL_NO=:serialNumber ORDER BY LOC_HISTORY.TXN_TIME DESC,LOC_HISTORY.ID DESC LIMIT :thresholdValue,1")
				.setParameter("serialNumber", serialNumber).setParameter("thresholdValue", thresholdValue);
		List list = new LinkedList();
		list = query.list();
		session.flush();
		session.close();
		return (ObjectUtil.isListEmpty(list) ? 0 : (BigInteger) list.get(0)).longValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#deleteLocationHistoryById
	 * (long)
	 */
	public void deleteLocationHistoryById(long id) {

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery("DELETE FROM LOC_HISTORY WHERE ID=" + id);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listDeviceLocationHistoryByAgentId(java.lang .String, java.util.Date,
	 * java.util.Date)
	 */
	public List<LocationHistory> listDeviceLocationHistoryByAgentId(String agentId, Date startDate, Date endDate) {

		Object[] bindValues = { agentId, startDate, endDate };
		return list(
				"FROM LocationHistory lh WHERE lh.agentId=? AND lh.txnTime BETWEEN ? AND ? AND lh.longitude !=null AND lh.latitude !=null AND lh.latitude!='' AND lh.longitude!='' ORDER BY lh.txnTime",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillagesByCityCode
	 * (java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Village> listVillagesByCityCode(String cityCode) {

		return list("FROM Village v WHERE v.city.code = ? ORDER BY v.name ASC", cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listCityByFarmerCity()
	 */
	public List<Municipality> listCityByFarmerCity() {

		return list("SELECT c FROM Farmer f INNER JOIN f.city c GROUP BY c.id ORDER BY c.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listCityByFarmerCity()
	 */
	public boolean isWarehouseProductCityMappingExist(String code) {

		Long warehouseProductCount = (Long) find(
				"SELECT Count(wp) From WarehouseProduct wp WHERE wp.warehouse.code=? AND wp.stock>0", code);
		if (warehouseProductCount > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listWarehouseForMTNTReceiptNo ()
	 */
	public List<Warehouse> listWarehouseForMTNTReceiptNo() {

		String queryString = "select * from warehouse Inner join dmt ON warehouse.ID=dmt.RECEIVER_WAREHOUSE_ID where dmt.MTNT_RECEIPT_NO != 'NULL' and dmt.STATUS='1'GROUP BY warehouse.ID";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		sessions.flush();
		sessions.close();
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listOfProductWarehouse()
	 */
	@SuppressWarnings("unchecked")
	public List<WarehouseProduct> listOfProductWarehouse() {

		return list(
				"FROM WarehouseProduct wp WHERE wp.warehouse.code IN (SELECT pf.servicePoint.code FROM Profile pf WHERE pf.status=1) AND wp.agent.id=NULL AND wp.stock>0 GROUP BY wp.warehouse.id");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listWarehouseForProductExist ()
	 */
	public List<Warehouse> listWarehouseForProductExist() {

		String queryString = "select * from warehouse INNER JOIN warehouse_product ON warehouse_product.WAREHOUSE_ID = warehouse.ID AND warehouse_product.AGENT_ID IS NULL AND warehouse_product.STOCK > 0 GROUP BY warehouse.ID ORDER BY warehouse.name ASC";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		sessions.flush();
		sessions.close();
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listWarehouseByTripSheetChartNoCityWarehouseStock ()
	 */
	public List<Warehouse> listWarehouseByTripSheetChartNoCityWarehouseStock() {

		return list(
				"SELECT w FROM Warehouse w WHERE w.city IN (SELECT ts.city FROM TripSheet ts INNER JOIN ts.city c WHERE c.id IN (SELECT c.id FROM CityWarehouse cw INNER JOIN cw.city c WHERE cw.isDelete=0 AND (cw.numberOfBags>0 OR cw.grossWeight>0) GROUP BY c.id) AND ts.transitStatus=?)",
				TripSheet.TRANSIT_STATUS.NONE.ordinal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findWarehouseByCityId(
	 * long)
	 */
	public Warehouse findWarehouseByCityId(long cityId) {

		Warehouse warehouse = (Warehouse) find("FROM Warehouse wh WHERE wh.city.id = ?", cityId);
		return warehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#findVillage(long)
	 */
	public Village findVillage(long id) {

		return (Village) find(
				"From Village v LEFT JOIN FETCH v.city c LEFT JOIN FETCH c.locality l LEFT JOIN FETCH l.state s LEFT JOIN FETCH s.country c WHERE v.id=? ",
				id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listServicePoint()
	 */
	@SuppressWarnings("unchecked")
	public List<ServicePoint> listServicePoint() {

		return list("FROM ServicePoint sp ORDER BY sp.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listActiveFarmersSamithiByAgentId(long)
	 */
	public List<Warehouse> listActiveFarmersSamithiByAgentId(long id) {

		return (List<Warehouse>) list(
				"SELECT f.samithi FROM Farmer f WHERE f.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=? AND f.status=?) GROUP BY f.samithi",
				new Object[] { id, Farmer.Status.ACTIVE.ordinal() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listActiveFarmersSamithiByCoOperativeId(long)
	 */
	public List<Warehouse> listActiveFarmersSamithiByCoOperativeId(long coOperativeId) {

		return (List<Warehouse>) list(
				"SELECT f.samithi FROM Farmer f WHERE f.samithi.refCooperative.id = ? and f.status = ? GROUP BY f.samithi",
				new Object[] { coOperativeId, Farmer.Status.ACTIVE.ordinal() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listActiveFarmersVillageBySamithiId(long)
	 */
	public List<Village> listActiveFarmersVillageBySamithiId(long samithiId) {

		return (List<Village>) list(
				"SELECT f.village FROM Farmer f WHERE f.samithi.id=? AND f.status=? GROUP BY f.village",
				new Object[] { samithiId, Farmer.Status.ACTIVE.ordinal() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findGramPanchayatById(
	 * long)
	 */
	public GramPanchayat findGramPanchayatById(long id) {

		return (GramPanchayat) find("FROM GramPanchayat gp WHERE gp.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findGramPanchayatByName
	 * (java.lang.String)
	 */
	public GramPanchayat findGramPanchayatByName(String name) {

		return (GramPanchayat) find("FROM GramPanchayat gp WHERE gp.name= ? ", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findGramPanchayatByCode
	 * (java.lang.String)
	 */
	public GramPanchayat findGramPanchayatByCode(String code) {

		return (GramPanchayat) find("FROM GramPanchayat gp WHERE gp.code = ?", code);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listGramPanchayats(java
	 * .lang.String)
	 */
	public List<GramPanchayat> listGramPanchayats(String selectedCity) {

		return list("FROM GramPanchayat gp WHERE gp.city.name = ? ORDER BY gp.name ASC", selectedCity);
	}

	public List<GramPanchayat> listGramPanchayatsByCode(String selectedCity) {

		return list("FROM GramPanchayat gp WHERE gp.city.code = ? ORDER BY gp.name ASC", selectedCity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listActiveFarmersVillageBySamithiCode(java .lang.String)
	 */
	public List<Village> listActiveFarmersVillageBySamithiCode(String samithiCode) {

		return (List<Village>) list("SELECT Distinct f.village FROM Farmer f WHERE f.samithi.code=? AND f.status=? ",
				new Object[] { samithiCode, Farmer.Status.ACTIVE.ordinal() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listOfVillageCodeNameByCityCode(java.lang. String)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> listOfVillageCodeNameByCityCode(String cityCode) {

		return list("SELECT v.code, v.name FROM Village v WHERE v.gramPanchayat.city.code=?", cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listOfVillagesByNames(
	 * java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<Village> listOfVillagesByNames(List<String> villageNames) {

		Session sessions = getSessionFactory().getCurrentSession();
		Query query = sessions.createQuery("FROM Village v WHERE v.name IN(:villageNames)");
		query.setParameterList("villageNames", villageNames);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findVillageNameByCode(
	 * java.lang.String)
	 */
	public String findVillageNameByCode(String code) {

		return (String) find("SELECT v.name FROM Village v WHERE v.code=?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listOfVillagesByCodes(
	 * java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<Village> listOfVillagesByCodes(List<String> villageCodes) {

		Session sessions = getSessionFactory().getCurrentSession();
		Query query = sessions.createQuery("FROM Village v WHERE v.code IN(:villageCodes)");
		query.setParameterList("villageCodes", villageCodes);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCityNameByCode(java
	 * .lang.String)
	 */
	public String findCityNameByCode(String code) {

		return (String) find("SELECT m.name FROM Municipality m WHERE m.code=?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findSamithiByCode(java
	 * .lang.String)
	 */
	public Warehouse findSamithiByCode(String samithiCode) {

		Object[] values = { samithiCode, Warehouse.WarehouseTypes.SAMITHI.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.code = ? AND wh.typez=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCoOperativeByCode(
	 * java.lang.String)
	 */
	public Warehouse findCoOperativeByCode(String code) {

		return (Warehouse) find(
				"FROM Warehouse coOperative WHERE coOperative.refCooperative=NULL AND coOperative.code = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findCooperativeMappedWithSamithi(java.lang .String)
	 */
	public Warehouse findCooperativeMappedWithSamithi(String code) {

		return (Warehouse) find("FROM Warehouse w WHERE w.refCooperative.code = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findFarmerMappedWithSamithi (long)
	 */
	public boolean findFarmerMappedWithSamithi(long id) {

		boolean isFarmerMappedWithSamithi = false;
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "SELECT SAMITHI_ID FROM FARMER WHERE STATUS<>2 AND SAMITHI_ID = " + id;
		Query querys = sessions.createSQLQuery(queryStrings);
		List results = querys.list();
		if (results.size() > 0) {
			isFarmerMappedWithSamithi = true;
		}
		sessions.flush();
		sessions.close();
		return isFarmerMappedWithSamithi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listOfCooperatives()
	 */
	public List<Warehouse> listOfCooperatives() {

		return list("FROM Warehouse w  WHERE w.typez=0  ORDER BY w.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listOfSamithi()
	 */
	public List<Warehouse> listOfSamithi() {

		return list("FROM Warehouse samithi  WHERE samithi.typez=1  ORDER BY samithi.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findSamithiByName(java
	 * .lang.String)
	 */
	public Warehouse findSamithiByName(String samithiName) {

		Object[] values = { samithiName, Warehouse.WarehouseTypes.SAMITHI.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.name = ? AND wh.typez=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listCoOperativeAndSamithi
	 * ()
	 */
	public List<Warehouse> listCoOperativeAndSamithi() {

		return list("FROM Warehouse wh ORDER BY wh.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findSamithiAndCoOperativeByVillage(long)
	 */
	public Warehouse findSamithiAndCoOperativeByVillage(long villageId) {

		Warehouse warehouse = (Warehouse) find(
				"SELECT wh FROM Warehouse wh INNER JOIN wh.villages v WHERE wh.refCooperative != NULL AND v.id = ?",
				villageId);
		return warehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCooperativeByName(
	 * java.lang.String)
	 */
	public Warehouse findCooperativeByName(String cooperativeName) {

		return (Warehouse) find("FROM Warehouse w WHERE w.refCooperative is NULL AND w.name=?", cooperativeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findCooperativeVillageMappedWtihSamithiVillage (long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public String findCooperativeVillageMappedWtihSamithiVillage(long cooperId, String villageCode) {

		List list = null;
		try {
			Session session = getSessionFactory().openSession();

			Query query = session.createSQLQuery(
					"SELECT V.NAME FROM WAREHOUSE AS W INNER JOIN WAREHOUSE_VILLAGE_MAP AS WV ON(WV.WAREHOUSE_ID=W.ID) INNER JOIN VILLAGE AS V ON(V.ID=WV.VILLAGE_ID) WHERE V.`CODE`=:villageCode AND W.REF_WAREHOUSE_ID=:warehouseId");
			query.setParameter("villageCode", villageCode);
			query.setParameter("warehouseId", cooperId);
			list = query.list();
			session.flush();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!ObjectUtil.isListEmpty(list)) {
			return (String) list.get(0);
		} else {
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findCoOperativeByName(
	 * java.lang.String)
	 */
	public Warehouse findCoOperativeByName(String name) {

		return (Warehouse) find(
				"FROM Warehouse coOperative WHERE coOperative.refCooperative=NULL AND coOperative.name = ?", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listFarmerMapppedSmithiVillages(java.lang. Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Village> listFarmerMapppedSmithiVillages(Long id) {

		return list("select DISTINCT(f.village) FROM Farmer f where f.samithi.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listSamithiByVillageId
	 * (long)
	 */
	public List<Warehouse> listSamithiByVillageId(long id) {

		return list(
				"SELECT wh FROM Warehouse wh INNER JOIN wh.villages v WHERE wh.typez = 1 AND v.id = ? ORDER BY wh.name",
				id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillagesByPanchayat
	 * (java.lang.String)
	 */
	public List<Village> listVillagesByPanchayat(String selectedPanchayat) {

		return list("FROM Village v WHERE v.gramPanchayat.name = ? ORDER BY v.name ASC", selectedPanchayat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * isVillageMappedWithCooperative (long)
	 */
	public boolean isVillageMappedWithCooperative(long id) {

		boolean isVillageMappedWithCooperative = false;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM WAREHOUSE_VILLAGE_MAP WHERE VILLAGE_ID = " + id;
		Query query = sessions.createSQLQuery(queryString);

		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			isVillageMappedWithCooperative = true;
		}
		sessions.flush();
		sessions.close();
		return isVillageMappedWithCooperative;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listCoOperativeManagerVillageByCoOperativeManagerId(long)
	 */
	public List<Village> listCoOperativeManagerVillageByCoOperativeManagerId(long id) {

		return list(
				"SELECT v FROM Warehouse samithi INNER JOIN samithi.villages v WHERE samithi.refCooperative.id IN (SELECT co.id FROM Agent a INNER JOIN a.wareHouses co WHERE a.agentType.code = ? AND a.id=?) GROUP BY v",
				new Object[] { AgentType.COOPERATIVE_MANAGER, id });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listFieldStaffVillageByFieldStaffId(long)
	 */
	public List<Village> listFieldStaffVillageByFieldStaffId(long id) {

		return list(
				"SELECT v FROM Warehouse samithi INNER JOIN samithi.villages v WHERE samithi.id IN (SELECT sam.id FROM Agent a INNER JOIN a.wareHouses sam WHERE a.agentType.code=? AND a.id=?) GROUP BY v",
				new Object[] { AgentType.FIELD_STAFF, id });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#listSamithies()
	 */
	public List<Warehouse> listSamithies() {

		return list("FROM Warehouse w  WHERE w.refCooperative IS NOT NULL ORDER BY w.name ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listFarmerCountBySamithiAndGender(long)
	 */
	public List<Object[]> listFarmerCountBySamithiAndGender(long id) {

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*),F.GENDER FROM FARMER  F WHERE F.SAMITHI_ID=" + "'" + id + "'"
				+ " GROUP BY F.GENDER ORDER BY F.GENDER DESC";
		Query query = session.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listFieldStaffIdAndNameBySamithiId(long)
	 */
	public String listFieldStaffIdAndNameBySamithiId(long id) {

		List list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String queryString = "SELECT GROUP_CONCAT(' ',P.PROF_ID,'-',PI.FIRST_NAME) FROM PERS_INFO AS PI INNER JOIN PROF AS P ON P.PERS_INFO_ID=PI.ID INNER JOIN AGENT_WAREHOUSE_MAP AWP ON AWP.AGENT_ID=P.ID WHERE AWP.WAREHOUSE_ID="
				+ "'" + id + "'";
		Query query = session.createSQLQuery(queryString);
		list = query.list();
		session.flush();
		session.close();
		if (!ObjectUtil.isListEmpty(list)) {
			return (String) list.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listCountriesByRevisionNo
	 * (long)
	 */
	@SuppressWarnings("unchecked")
	public List<Country> listCountriesByRevisionNo(long revisionNo) {

		return list("From Country c left join fetch c.states WHERE c.revisionNo>? ORDER BY c.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listStatesByRevisionNo
	 * (long)
	 */
	@SuppressWarnings("unchecked")
	public List<State> listStatesByRevisionNo(long revisionNo) {

		return list("From State s left join fetch s.localities WHERE s.revisionNo>? ORDER BY s.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listLocalitiesByRevisionNo
	 * (long)
	 */
	@SuppressWarnings("unchecked")
	public List<Locality> listLocalitiesByRevisionNo(long revisionNo) {

		return list("From Locality l left join fetch l.municipalities WHERE l.revisionNo>? GROUP BY l.id ORDER BY l.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listMunicipalitiesByRevisionNo (long)
	 */
	@SuppressWarnings("unchecked")
	public List<Municipality> listMunicipalitiesByRevisionNo(long revisionNo) {

		return list("From Municipality m left join fetch m.gramPanchayats left join fetch m.villages  WHERE m.revisionNo>? ORDER BY m.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listGramPanchayatsByRevisionNo (long)
	 */
	@SuppressWarnings("unchecked")
	public List<GramPanchayat> listGramPanchayatsByRevisionNo(long revisionNo) {

		return list("From GramPanchayat gp left join fetch gp.villages WHERE gp.revisionNo>? ORDER BY gp.revisionNo DESC", revisionNo);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillagesByRevisionNo
	 * (long)
	 */
	@SuppressWarnings("unchecked")
	public List<Village> listVillagesByRevisionNo(long revisionNo) {

		return list("From Village v WHERE v.revisionNo>? ORDER BY v.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillageByCoOperative
	 * (java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Village> listVillageByCoOperativeCode(String selectedCooperative) {

		return list("SELECT wh.villages FROM Warehouse wh WHERE wh.code = ?", selectedCooperative);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listCoOperativeAndSamithiByRevisionNo(long, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Warehouse> listCoOperativeAndSamithiByRevisionNo(long revisionNo, int typez) {

		return list("FROM Warehouse wh WHERE wh.typez=? AND wh.revisionNo>? ORDER BY wh.revisionNo DESC",
				new Object[] { typez, revisionNo });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listWarehouseByVillageId
	 * (long)
	 */
	public List<Warehouse> listWarehouseByVillageId(long villageId) {

		return list("SELECT w FROM Warehouse w INNER JOIN w.villages v WHERE v.id=?", villageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listCoOperativeByAgent(
	 * java.lang.String)
	 */
	public List<Warehouse> listCoOperativeByAgent(String agentId) {

		// TODO Auto-generated method stub
		List<Warehouse> warehouses = new ArrayList<Warehouse>();
		warehouses.addAll(((Agent) find("from Agent ag where ag.profileId=?", agentId)).getWareHouses());
		return warehouses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listCoOperativeWithManagers()
	 */
	public List<Warehouse> listCoOperativeWithManagers() {

		// TODO Auto-generated method stub
		List<Warehouse> warehouses = new ArrayList<Warehouse>();
		List<Agent> agents = list("from Agent");
		for (Agent agent : agents) {
			Set<Warehouse> sets = agent.getWareHouses();
			for (Warehouse warehouse : sets) {
				if (ObjectUtil.isEmpty(warehouse.getRefCooperative())) {
					warehouses.add(warehouse);
				}
			}
		}
		return warehouses;
	}

	/*
	 * public List<Village> listActiveFarmersVillageId( long id) { return
	 * (List<Village>) list(
	 * "SELECT  FROM Farmer f WHERE f.village.id = ? and f.status = ?", new
	 * Object[] { id, Farmer.Status.ACTIVE.ordinal() }); }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listOfWareHouseProductCooperatives()
	 */
	public List<Warehouse> listOfWareHouseProductCooperatives() {

		return (List<Warehouse>) list("select wp.warehouse FROM WarehouseProduct wp where wp.warehouse.typez=0 ");
	}

	/*
	 * public WarehouseProduct findWarehouseProductbyCode(String warehousecode,
	 * String productCode) { Object[] values = { warehousecode, productCode };
	 * return (WarehouseProduct) find(
	 * "FROM WarehouseProduct wp WHERE wp.warehouse.code = ? AND wp.product.code = ?"
	 * , values); }
	 */

	/*
	 * public List<WarehouseProduct> listOfWareHouseProductCooperatives() { //
	 * TODO Auto-generated method stub return null; }
	 */

	/*
	 * public List<WarehouseProduct> listOfWareHouseProductCooperatives(String
	 * warehousecode) { // TODO Auto-generated method stub return null; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#findLocalityByStateId(
	 * long)
	 */
	public List<Locality> findLocalityByStateId(long selectedState) {

		return list("FROM Locality l WHERE l.state.id = ? ORDER BY l.name ASC", selectedState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listGramPanchayatsByCityId
	 * (long)
	 */
	public List<GramPanchayat> listGramPanchayatsByCityId(long selectedCity) {

		return list("FROM GramPanchayat gp WHERE gp.city.id = ? ORDER BY gp.name ASC", selectedCity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * listMunicipalitiesByLocalityId(long)
	 */
	public List<Municipality> listMunicipalitiesByLocalityId(long selectedLocality) {

		return list("FROM Municipality m WHERE m.locality.id = ? ORDER BY m.name ASC", selectedLocality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listVillagesByPanchayatId(
	 * long)
	 */
	public List<Village> listVillagesByPanchayatId(long selectedPanchayat) {

		return list("FROM Village v WHERE v.gramPanchayat.id = ? ORDER BY v.name ASC", selectedPanchayat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#
	 * findVillageBySelectedVillageId(long)
	 */
	public Village findVillageBySelectedVillageId(long selectedVillage) {

		return (Village) find("FROM Village v WHERE v.id= ? ", selectedVillage);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.ILocationDAO#listOfWarehouseCount()
	 */
	public long listOfWarehouseCount() {

		long totalWarehouse = 0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM WAREHOUSE WHERE TYPEZ = "
				+ Warehouse.WarehouseTypes.COOPERATIVE.ordinal();
		Query query = sessions.createSQLQuery(queryString);

		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalWarehouse = count;
		}
		sessions.flush();
		sessions.close();
		return totalWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.ILocationDAO#findSamithiById(long)
	 */
	public Warehouse findSamithiById(long id) {

		Object[] values = { id, Warehouse.WarehouseTypes.SAMITHI.ordinal() };

		return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez=?", values);
	}

	public List<Village> listVillagesByCityId(long selectedCity) {

		return list("FROM Village v WHERE v.city.id = ? ORDER BY v.name ASC", selectedCity);

	}

	// using selected city to get panchayat
	public List<GramPanchayat> listPanchayatBycityId(long selectedCity) {

		return list("FROM GramPanchayat v WHERE v.city.id = ? ORDER BY v.name ASC", selectedCity);

	}

	public WarehousePayment findCooperativeMappedWithWarhousePayment(long warhouseId) {
		// TODO Auto-generated method stub
		return (WarehousePayment) find("FROM WarehousePayment w WHERE w.warehouse.id = ?", warhouseId);
	}

	@Override
	public Warehouse findWarehouseByNameAndType(String name, int type) {
		// TODO Auto-generated method stub
		Object[] values = { name, type };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.name= ? AND wh.typez=?", values);
	}

	/*@Override
	public Map<String, String> listVillageIdAndName() {
		// TODO Auto-generated method stub
		Map<String, String> returnVillageMap = new HashMap<String, String>();
		List<Object> villages = list("SELECT v.id,v.name FROM Village v");
		if (!ObjectUtil.isEmpty(villages)) {
			returnVillageMap.put(String.valueOf(villages.get(0)), String.valueOf(villages.get(1)));
		}
		return returnVillageMap;
	}*/

	@Override
	public List<Municipality> listMunicipality() {
		// TODO Auto-generated method stub
		return list("FROM Municipality m ORDER BY m.name ASC");
	}

	@Override
	public List<State> listStatesByCountryID(Long countryID) {
		return list("FROM State s WHERE s.country.id = ? ORDER BY s.name ASC", countryID);
	}

	public List<Locality> listLocalitiesByStateID(Long stateId) {
		return list("FROM Locality l WHERE l.state.id = ? ORDER BY l.name ASC", stateId);
	}

	public List<Village> listVillagesByCityID(Long cityId) {
		return list("FROM Village v WHERE v.city.id = ? ORDER BY v.name ASC", cityId);
	}

	@Override
	public long listOfWarehouseCountBasedOnBranch(String branchIdValue) {

		long totalWarehouse = 0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM WAREHOUSE WHERE BRANCH_ID= :BRANCH AND TYPEZ = "
				+ Warehouse.WarehouseTypes.COOPERATIVE.ordinal();
		Query query = sessions.createSQLQuery(queryString);
		query.setParameter("BRANCH", branchIdValue);
		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalWarehouse = count;
		}
		sessions.flush();
		sessions.close();
		return totalWarehouse;
	}

	@Override
	public List<Municipality> listMunicipalityBasedOnBranch(String branchIdValue) {

		return list("FROM Municipality m WHERE m.branchId=? ORDER BY m.name ASC", branchIdValue);
	}

	@Override
	public List<Warehouse> listSamithiesBasedOnType() {
		return list("FROM Warehouse wr WHERE wr.typez='1' ORDER BY wr.name ASC");
	}

	@Override
	public Integer findWarehouseCount() {
		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery(
				"select count(*) from Warehouse WHERE typez='" + Warehouse.WarehouseTypes.COOPERATIVE.ordinal() + "'")
				.uniqueResult()).intValue();
	}

	@Override
	public Integer findWarehouseCountByMonth(Date sDate, Date eDate) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select count(*) from Warehouse WHERE typez='"
				+ Warehouse.WarehouseTypes.COOPERATIVE.ordinal() + "' AND createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public List<Object> listWarehouseProductAndStock() {
		return list("SELECT SUM(wp.stock),wp.product.name from WarehouseProduct wp where wp.warehouse.id is not NULL GROUP BY wp.product");
	}

	@SuppressWarnings("unchecked")
	public List<Municipality> listOfCitysByIds(List<Long> municipalityIds) {

		Session sessions = getSessionFactory().getCurrentSession();
		Query query = sessions.createQuery("FROM Municipality m WHERE m.id IN(:cityid)");
		query.setParameterList("cityid", municipalityIds);
		return query.list();
	}

	public Village findVillageByCode(String villageCode, String tenantId) {

		// return (Village) find("FROM Village v WHERE v.code = ?",
		// villageCode);

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Village v WHERE v.code = :villageCode");
		query.setParameter("villageCode", villageCode);

		List<Village> villageList = query.list();
		Village village = null;
		if (villageList.size() > 0) {
			village = (Village) villageList.get(0);
		}

		session.flush();
		session.close();
		return village;
	}

	public Warehouse findSamithiByCode(String samithiCode, String tenantId) {

		/*
		 * Object[] values = {samithiCode,
		 * Warehouse.WarehouseTypes.SAMITHI.ordinal()}; return (Warehouse)
		 * find("FROM Warehouse wh WHERE wh.code = ? AND wh.typez=?", values);
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Warehouse wh WHERE wh.code = :samithiCode AND wh.typez = :typez");
		query.setParameter("samithiCode", samithiCode);
		query.setParameter("typez", Warehouse.WarehouseTypes.SAMITHI.ordinal());

		List<Warehouse> warehouseList = query.list();
		Warehouse warehouse = null;
		if (warehouseList.size() > 0) {
			warehouse = (Warehouse) warehouseList.get(0);
		}

		session.flush();
		session.close();
		return warehouse;
	}

	@Override
	public Warehouse findCoOperativeByCode(String servicePointId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM Warehouse coOperative WHERE coOperative.refCooperative=NULL AND coOperative.code = :servicePointId");
		query.setParameter("servicePointId", servicePointId);

		List<Warehouse> warehouseList = query.list();
		Warehouse warehouse = null;
		if (warehouseList.size() > 0) {
			warehouse = (Warehouse) warehouseList.get(0);
		}

		session.flush();
		session.close();
		return warehouse;
	}

	@Override
	public List<Object> listWarehouseProductAndStockByWarehouseId(Long warehouseId) {
		Object[] values = { warehouseId };
		return list(
				"SELECT SUM(wp.stock),wp.product.name from WarehouseProduct wp  WHERE wp.warehouse.id=? GROUP BY wp.product order by wp.stock DESC",
				values);
	}

	@Override
	public List<Object[]> listCityCodeAndName() {

		return list("SELECT m.code,m.name FROM Municipality m ORDER BY m.name ASC");
	}

	@Override
	public List<Object[]> listVillageCodeAndNameByCityCode(String cityCode) {
		Object value = cityCode;
		return list("SELECT v.code,v.name FROM Village v WHERE v.city.code=?", cityCode);
	}

	@Override
	public List<Warehouse> listSamithiBySangham(String groupCode, String villageCode) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM Warehouse w WHERE sanghamType =:sanghamType AND w.typez=:type AND CODE like:vCode");
		query.setParameter("sanghamType", groupCode);
		query.setParameter("type", Warehouse.WarehouseTypes.SAMITHI.ordinal());
		query.setParameter("vCode", "%" + villageCode + "%");
		List<Warehouse> codeList = query.list();
		session.flush();
		session.close();
		return codeList;
	}

	@Override
	public List<Object[]> listLocalityIdCodeAndName() {
		return list("SELECT l.id,l.code,l.name FROM Locality l order by l.name asc");
	}

	@Override
	public List<Object[]> listCityCodeAndNameByDistrictId(Long id) {
		return list("SELECT m.id,m.code,m.name FROM Municipality m WHERE m.locality.id=? ORDER BY m.name ASC", id);
	}

	@Override
	public List<Object[]> listGramPanchayatIdCodeName() {
		return list("SELECT g.id,g.code,g.name FROM GramPanchayat g order by g.name asc");
	}

	@Override
	public List<Object[]> listGramPanchayatByCityId(Long id) {
		return list("SELECT g.id,g.code,g.name FROM GramPanchayat g WHERE g.city.id=? ORDER BY g.name ASC", id);
	}

	@Override
	public List<Object[]> listVillageByPanchayatId(Long id) {
		return list("SELECT v.id,v.code,v.name FROM Village v WHERE v.gramPanchayat.id=? ORDER BY v.name ASC", id);
	}

	public Integer findFarmerCountBySamtihi(Long id) {
		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery("select count(f.id) from Farmer f where f.samithi.id=:id")
				.setParameter("id", id).uniqueResult()).intValue();
	}

	@Override
	public List<Object[]> listOfCooperativesByDistribution() {
		// TODO Auto-generated method stub
		return list("SELECT DISTINCT d.servicePointId,d.servicePointName,d.agentId,d.agentName from Distribution d");
	}

	@Override
	public List<Object[]> listOfVillageInfoByProcurement() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT f.village.id,f.village.name from Farmer f where f.village.id in(select p.village.id from Procurement p)");

		return result;
	}

	@Override
	public List<Object[]> listOfCooperativeByProcurement() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT cw.coOperative.id,cw.coOperative.code,cw.coOperative.name from CityWarehouse cw)");

		return result;
	}

	@Override
	public List<Object[]> listOfWarehouseByStock() {
		// TODO Auto-generated method stub
		List<Object[]> result = list("SELECT DISTINCT wp.warehouse.code,wp.warehouse.name from WarehouseProduct wp)");

		return result;
	}

	@Override
	public List<Object[]> listOfWarehouseByStockEntry() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT wpd.warehousePayment.warehouse.code,wpd.warehousePayment.warehouse.name from WarehousePaymentDetails wpd)");

		return result;
	}

	@Override
	public List<Object[]> listOfWarehousePMT() {
		// TODO Auto-generated method stub
		List<Object[]> result = list("SELECT DISTINCT pm.coOperative.code,pm.coOperative.name from PMT pm)");

		return result;
	}

	@Override
	public GramPanchayat findGrampanchaythByCode(String code) {
		// TODO Auto-generated method stub
		return (GramPanchayat) find("FROM GramPanchayat gp WHERE gp.code=?", code);
	}

	@Override
	public List<GramPanchayat> listGramPanchayatsByCityCode(String selectedCity) {
		return list("FROM GramPanchayat gp WHERE gp.city.code = ? ORDER BY gp.name ASC", selectedCity);
	}

	public Village findDuplicateVillageName(long id, String name) {
		Object[] values = { id, name };
		Village villageList = (Village) find("FROM Village v WHERE v.gramPanchayat.id = ? and v.name=?", values);

		return villageList;
	}

	@Override
	public GramPanchayat findDuplicateGramPanchayatName(long id, String name) {
		Object[] values = { id, name };
		GramPanchayat gramPanchayatList = (GramPanchayat) find(
				"FROM GramPanchayat gp WHERE gp.city.id = ? and gp.name=?", values);

		return gramPanchayatList;
	}

	@Override
	public Municipality findDuplicateMunicipalityName(long id, String name) {
		Object[] values = { id, name };
		Municipality municipalityList = (Municipality) find("FROM Municipality m WHERE m.locality.id = ? and m.name=?",
				values);

		return municipalityList;
	}

	@Override
	public List<Warehouse> listSamithiBySanghamType(String groupCode) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM Warehouse w WHERE sanghamType =:sanghamType AND w.typez=:type");
		query.setParameter("sanghamType", groupCode);
		query.setParameter("type", Warehouse.WarehouseTypes.SAMITHI.ordinal());
		List<Warehouse> codeList = query.list();
		session.flush();
		session.close();
		return codeList;
	}

	@Override
	public List<State> listOfStates() {
		// TODO Auto-generated method stub
		return list("FROM State st ORDER BY st.name ASC");
	}

	@Override
	public List<Object[]> listOfCooperativesByProductReturn() {
		// TODO Auto-generated method stub
		return list(
				"SELECT DISTINCT pr.servicePointId,pr.servicePointName,pr.agentId,pr.agentName from ProductReturn pr");
	}

	@Override
	public List<Object[]> listWarehouses() {
		// TODO Auto-generated method stub
		return list("SELECT DISTINCT w.id,w.code,w.name,w.branchId from Warehouse w  where w.typez=? ORDER BY w.name ASC ",
				Warehouse.COOPERATIVE);

	}

	@Override
	public List<Object[]> listOfGroup() {
		return list("SELECT DISTINCT g.code,g.name,g.id from Warehouse g where g.typez=? ORDER BY g.name ASC",
				Warehouse.SAMITHI);
	}

	@Override
	public List<Object[]> listOfAgentsByAgroTxn() {
		// TODO Auto-generated method stub
		return list("SELECT DISTINCT at.agentId,at.agentName from AgroTransaction at");
	}

	@Override
	public List<Object[]> listOfFarmersByAgroTxn() {
		// TODO Auto-generated method stub
		return list("SELECT DISTINCT at.farmerId,at.farmerName from AgroTransaction at");
	}

	@Override
	public List<Municipality> listCityByProcurement() {
		// TODO Auto-generated method stub
		List<Municipality> result = list("SELECT DISTINCT p.farmer.village.city from Procurement p ");

		return result;
	}

	@Override
	public List<Village> listVillageByProcurementAndCityId(String cityId) {
		// TODO Auto-generated method stub
		List<Village> result = list(
				"SELECT DISTINCT p.farmer.village from Procurement p where  p.farmer.village.city.code =?", cityId);

		return result;
	}

	@Override
	public Village findVillageByNameAndCity(String village, String city) {
		Object[] values = { village, city };
		return (Village) find("FROM Village v WHERE v.name= ? AND v.city.name=?", values);
	}

	@Override
	public GramPanchayat findGramPanchayatByNameAndCity(String gp, String city) {
		Object[] values = { gp, city };
		return (GramPanchayat) find("FROM GramPanchayat gp WHERE gp.name= ? AND gp.city.name=?", values);
	}

	@Override
	public Municipality findMunicipalityByNameAndDistrict(String taluk, String districtz) {
		Object[] values = { taluk, districtz };
		Municipality municipality = (Municipality) find("FROM Municipality m WHERE m.name = ? AND m.locality.name=?",
				values);
		return municipality;
	}

	@Override
	public DataLevel findDataLevelByName(String name) {
		// TODO Auto-generated method stub
		return (DataLevel) find("FROM DataLevel d WHERE d.name=?", name);
	}

	@Override
	public DataLevel findDataLevelByCode(String code) {
		// TODO Auto-generated method stub
		return (DataLevel) find("FROM DataLevel d WHERE d.code=?", code);
	}

	@Override
	public List<LocationHistory> listDeviceLocationHistoryByAgentId(String agentId, Date startDate, Date endDate,
			String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Object[] bindValues = { agentId, startDate, endDate };
		Query query = session.createQuery(
				"FROM LocationHistory lh WHERE lh.agentId=:agentId AND lh.txnTime BETWEEN :startDate AND :endDate AND lh.longitude !=null AND lh.latitude !=null AND lh.latitude!='' AND lh.longitude!='' ORDER BY lh.txnTime");

		query.setParameter("agentId", agentId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		List<LocationHistory> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public Integer findGroupCount() {
		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery(
				"select count(*) from Warehouse WHERE typez='" + Warehouse.WarehouseTypes.SAMITHI.ordinal() + "'")
				.uniqueResult()).intValue();
	}

	@Override
	public Integer findGroupCountByMonth(Date sDate, Date eDate) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select count(*) from Warehouse WHERE typez='"
				+ Warehouse.WarehouseTypes.SAMITHI.ordinal() + "' AND createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public Map tableFields() {
		Map allEntityMap = getSessionFactory().getAllClassMetadata();
		/*
		 * SingleTableEntityPersister obj = (SingleTableEntityPersister) x
		 * .get("com.sourcetrace.esesw.entity.profile.Farmer"); List<String>
		 * farmer = new ArrayList<>(); for (int j = 0; j <
		 * obj.getPropertyNames().length; j++) { System.out.println(" " +
		 * obj.getPropertyNames()[j] + " -> " +
		 * (obj.getPropertyColumnNames(j).length > 0?
		 * obj.getPropertyColumnNames(j)[0]: "")); }
		 */
		return allEntityMap;
	}

	public Warehouse findWarehouseById(long id, String tenantId) {

		/*
		 * Object[] values = { id,
		 * Warehouse.WarehouseTypes.COOPERATIVE.ordinal() };
		 * System.out.println("values" + values[0] + values[1]); return
		 * (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez=?",
		 * values);
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Warehouse wh WHERE wh.id=:id");
		query.setParameter("id", id);

		List<Warehouse> warehouseList = query.list();
		Warehouse warehouse = null;
		if (warehouseList.size() > 0) {
			warehouse = (Warehouse) warehouseList.get(0);
		}

		session.flush();
		session.close();
		return warehouse;
	}

	@Override
	public Warehouse findWarehouseWithoutTypeById(Long id) {
		return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez!=null", id);
	}

	@Override
	public List<BankInformation> findWarehouseBankinfo(long id) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		// Query query = session.createQuery("SELECT bi from Farmer fr INNER
		// JOIN fr.bankInfo bi WHERE fr.id=:ID");
		Query query = session.createQuery("SELECT bi from Warehouse w INNER JOIN w.bankInfo bi WHERE w.id=:ID");
		query.setParameter("ID", id);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<Warehouse> listProcurementCenter() {
		return list("FROM Warehouse w where w.typez=? ORDER BY w.name ASC", Warehouse.PROCUREMENT_PLACE);
	}

	@Override
	public List<Object[]> listOfWarehouses() {
		// TODO Auto-generated method stub
		return list("SELECT DISTINCT pt.warehouse.code,pt.warehouse.name from ProcurementTraceability pt)");
	}

	@Override
	public Warehouse findProcurementWarehouseById(long id) {
		return (Warehouse) find("FROM Warehouse w where w.id=?", id);
	}

	@Override
	public List<Warehouse> listCoOperativeAndSamithiByRevisionNo(Long revNo, Integer[] warehousetypesarray) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"FROM Warehouse wh WHERE wh.typez in:type AND wh.revisionNo>:revNo ORDER BY wh.revisionNo DESC");
		query.setParameter("revNo", revNo);
		query.setParameterList("type", warehousetypesarray);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public void deleteCultivationDetailByCultivationId(long cultiId, String type) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery("delete from cultivation_detail where cultivation_id=:id and TYPE=:type");
		query.setParameter("id", cultiId);
		query.setParameter("type", type);
		query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public void updateCultivationCost(long id, String cost, String type) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String qry = null;
		if (type.equals("1"))
			qry = "UPDATE cultivation set TOTAL_FERTILIZER =:cost where ID=:id";
		if (type.equals("2"))
			qry = "UPDATE cultivation set TOTAL_PESTICIDE =:cost where ID=:id";

		if (type.equals("3"))
			qry = "UPDATE cultivation set TOTAL_MANURE =:cost where ID=:id";
		Query query = session.createSQLQuery(qry);
		query.setParameter("cost", cost);
		query.setParameter("id", id);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<GMO> listGMOBySeason(String seasonCode) {
		return list("FROM GMO where seasonCode=? and type in ('1','2','3')  ", seasonCode);
	}

	@Override
	public HeapData isHeapSpaceAvailableByIcsCode(String ics) {
		HeapData hd = (HeapData) find("from HeapData h where h.ics=?", ics);

		return hd;
	}

	@Override
	public HeapData findHeapDataByICS(String icsCode) {
		return (HeapData) find("FROM HeapData hd where hd.ics=?", icsCode);
	}

	@Override
	public boolean isStockMovedToHeap(long pmtDetId) {
		Object[] params = { pmtDetId, 1 };
		HeapDataDetail hdd = (HeapDataDetail) find("from HeapDataDetail hdd where hdd.pmtDetailId=? and hdd.status=?",
				params);
		if (!ObjectUtil.isEmpty(hdd))
			return true;
		else
			return false;
	}

	@Override
	public List<Object[]> listOfGiningFromHeap() {
		List<Object[]> result = list("SELECT DISTINCT ps.ginning.id, ps.ginning.name FROM HeapData ps");
		return result;
	}

	@Override
	public List<Object[]> listOfGinningFromHeap(String season) {
		List<Object[]> result = list("SELECT DISTINCT hd.ginning.id,hd.ginning.name FROM HeapData hd where hd.totalStock>0 and hd.season=?",season);
		return result;
	}

	@Override
	public List<Object[]> listOfProductFromHeapWithHeap(String selectedGinning, String selectedHeap) {
		List<Object[]> result = list(
				"SELECT DISTINCT hd.procurementProduct.code,hd.procurementProduct.name FROM HeapData hd where hd.ginning.id=? and hd.code=?",
				new Object[]{Long.parseLong(selectedGinning),selectedHeap});
		return result;
	}

	@Override
	public List<Object[]> listOfICSFromHeapByGinningCodeAndProdut(String ginningCode, String selectedProduct) {
		Object[] val = { ginningCode, selectedProduct };
		List<Object[]> result = list(
				"SELECT DISTINCT hd.ics,(SELECT cv.name from FarmCatalogue cv where cv.code=hd.ics) from HeapData hd where hd.ginning.code=? and hd.procurementProduct.code=?",
				val);
		return result;
	}

	@Override
	public List<Object[]> listOfHeapsByGinningCodeProdutICS(String ginningCode,String season) {
		List<Object[]> result = list("SELECT DISTINCT hd.code,hd.name FROM HeapData hd where hd.ginning.id=? and hd.totalStock>0 and hd.season=?",
				new Object[]{Long.parseLong(ginningCode),season});
		return result;
	}

	@Override
	public String findHeapQtyByHeapDataId(String selectedHeap) {
		Double result = (Double) find("SELECT hd.totalStock from HeapData hd where hd.id=?",
				Long.parseLong(selectedHeap));
		return String.valueOf(result);
	}

	@Override
	public HeapData findHeapDataById(String heapId) {
		return (HeapData) find("FROM HeapData hd where hd.id=?", Long.parseLong(heapId));
	}

	@Override
	public Object[] findHeapQtyICSAndProductByHeapAndGinning(String selectedHeap, String selectedGinning, String selectedProduct,String season) {
			 String queryString = "SELECT  (select GROUP_CONCAT(c.`NAME`) from catalogue_value c where FIND_IN_SET(c.`CODE`,hd.ICS) group by c.TYPEZ),"
			 		+ " hd.TOTAL_STOCK,(select GROUP_CONCAT(c.CODE) from catalogue_value c where FIND_IN_SET(c.`CODE`,hd.ICS) group by c.TYPEZ) "
			 		+ "FROM heap_data hd inner join procurement_product pp on pp.id=hd.PRODUCT WHERE hd.GINNING=:selectedGinning AND hd.`CODE`=:selectedHeap AND pp.code=:selectedProduct AND hd.SEASON=:season";
				Session sessions = getSessionFactory().openSession();
				Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
				 Query query = sessions.createSQLQuery(queryString);
				 query.setParameter("selectedHeap", selectedHeap);
				 query.setParameter("selectedGinning", selectedGinning);
				 query.setParameter("selectedProduct", selectedProduct);
				 query.setParameter("season", season);
				List<Object[]> list = query.list();
				sessions.flush();
				sessions.close();
				return list.get(0);			
		
	}

	@Override
	public List<State> listOfStatesByBranch(String selectedBranch) {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(selectedBranch)) {
			return list("FROM State st group by st.name ORDER BY st.name ASC");

		} else {
			return list("FROM State st where st.branchId in ? ORDER BY st.name ASC", selectedBranch);

		}

	}
	
	public List<Object[]> listOfCooperativesByGinningProcess(){
		return list("SELECT DISTINCT g.ginning.id,g.ginning.name from GinningProcess g");
	}
	
	public List<Object[]> listOfHeapNameByGinningProcess(){
		return list("SELECT DISTINCT g.heapCode,(SELECT DISTINCT fc.name from FarmCatalogue fc where fc.code=g.heapCode) from GinningProcess g");
	}
	
	public Double findTxnStockByGinningId(long id){
		
		return (Double)find ("SELECT hdd.heapData.totalStock FROM HeapDataDetail hdd WHERE hdd.pmtDetailId = ?",id);
	
	}
	public List<Object[]> findStockByGinningId(long id) {
		return list("SELECT hdd.heapData.totalStock,hdd.heapData.totLintCotton,hdd.heapData.totSeedCotton,hdd.heapData.totScrup FROM HeapDataDetail hdd WHERE hdd.pmtDetailId = ?", id);
	}

	@Override
	public Warehouse findWarehouseByCodeAndType(String code, int type) {
		return (Warehouse) find("FROM Warehouse wh WHERE wh.code = ? and wh.typez=?",new Object[]{code,type} );
	}

	@Override
	public List<Village> listVillageByBranch(String selectedBranch,Long selectedState) {
		Object[] values={selectedBranch,selectedState};
				if (StringUtil.isEmpty(selectedBranch)) {
					return list("FROM Village v where v.city.locality.state.id = ? group by v.name ORDER BY v.name ASC", selectedState);

				} else {
					return list("FROM Village v where v.branchId in ? AND v.city.locality.state.id = ? ORDER BY v.name ASC", values);

				}
	}

	@Override
	public Integer findFacilitiesCount() {
		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery(
				"select count(*) from Warehouse WHERE typez in (:type)").setParameterList("type", new Object[] { Warehouse.COOPERATIVE, Warehouse.GINNER,Warehouse.SPINNING,Warehouse.PROCUREMENT_PLACE})
				.uniqueResult()).intValue();
	}

	@Override
	public Warehouse findWarehouseByCodeAndType(String spinnerCode, int type, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM Warehouse wh WHERE wh.code=:code and wh.typez=:type");
		query.setParameter("code", spinnerCode);
		query.setParameter("type", type);

		List<Warehouse> warehouseList = query.list();
		Warehouse warehouse = null;
		if (warehouseList.size() > 0) {
			warehouse = (Warehouse) warehouseList.get(0);
		}

		session.flush();
		session.close();
		return warehouse;
	
	}

	@Override
	public List<Object[]> findCityNames() {
		// TODO Auto-generated method stub
		return list("SELECT wfc.city,wfc.state FROM WeatherForeCast wfc GROUP BY wfc.city");
	}

	@Override
	public List<Object[]> listVillageIdAndName() {
		// TODO Auto-generated method stub
		return list("SELECT v.id,v.code,v.name FROM Village v order by v.name asc");
	}

	
	@Override
	public Village findVillageAndCityByVillName(String otherVi,Long cityId) {
		// TODO Auto-generated method stub
		Object[] values={otherVi,cityId};
		return (Village) find("FROM Village v WHERE v.name= ? and v.city.id=?", values);
	}

	@Override
	public List<Locality> listOfLocalities() {
		// TODO Auto-generated method stub
		return list("FROM Locality l ORDER BY l.name ASC");

	}

	@Override
	public List<Object[]> listLocalities() {
		// TODO Auto-generated method stub
		return list("select DISTINCT l.code,l.name,l.id FROM Locality l ORDER BY l.name ASC ");
	}
	
	public List<LocationHistory> listDeviceLocationHistoryByAgentIdWithAccuracy(String agentId, Date startDate, Date endDate, Long accuracy ){
		Object[] bindValues = { agentId, startDate, endDate, accuracy };
		return list(
				"FROM LocationHistory lh WHERE lh.agentId=? AND lh.txnTime BETWEEN ? AND ? AND lh.longitude !=null AND lh.latitude !=null AND lh.latitude!='' AND lh.longitude!='' AND lh.accuracy <= ?  ORDER BY lh.txnTime",
				bindValues);
	}
	
	@Override
	public List<Object[]> listWarehousesWithoutBranch() {
		// TODO Auto-generated method stub
		/*return list("SELECT DISTINCT w.id,w.code,w.name,w.branchId from Warehouse w  where w.typez=? ORDER BY w.name ASC ",
				Warehouse.COOPERATIVE);
*/
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String queryString = "SELECT DISTINCT ID,CODE,NAME,BRANCH_ID from WAREHOUSE where TYPEZ=:TYPE AND BRANCH_ID is not null ORDER BY NAME ASC";
		Query query = session.createSQLQuery(queryString);
		query.setParameter("TYPE", Warehouse.WarehouseTypes.COOPERATIVE.ordinal());
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}
	@Override
	public Warehouse findWarehouseByIdWithoutBranch(long id) {

		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String queryString = "SELECT * from WAREHOUSE WHERE ID = '"
				+ id + "' AND TYPEZ='"
				+ Warehouse.WarehouseTypes.COOPERATIVE.ordinal() + "'";
		Query query = session.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@Override
	public List<Object[]> listOfMobileUserByDistribution() {
		// TODO Auto-generated method stub
		return list("SELECT DISTINCT d.agentId,d.agentName from Distribution d");
	}
	
	public LocationHistory findLocationHistoryByTxnTimeSerialNoBranch(Date date,String serialNo,String branch){
		Object[] values={date,serialNo,branch};
		return (LocationHistory) find("FROM LocationHistory lc WHERE lc.txnTime= ? and lc.agentId=? and lc.branchId=?", values);
	}
	
	@Override
	public List<LocationHistoryDetail> listDeviceLocationHistoryDetailByAgentId(String agentId, Date startDate, Date endDate,
			String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Object[] bindValues = { agentId, startDate, endDate };
		Query query = session.createQuery(
				"FROM LocationHistoryDetail lh WHERE lh.agentId=:agentId AND lh.txnTime BETWEEN :startDate AND :endDate AND lh.longitude !=null AND lh.latitude !=null AND lh.latitude!='' AND lh.longitude!='' ORDER BY lh.txnTime");

		query.setParameter("agentId", agentId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		List<LocationHistoryDetail> list = query.list();
		session.flush();
		session.close();
		return list;
	}
	
	public List<LocationHistoryDetail> findLocationHistoryDetailById(long id) {

		return list("FROM LocationHistoryDetail l WHERE l.locHistory.id = ? AND l.netStatus!=null AND l.gpsStatus!=null ORDER BY l.txnTime", id);
	}

	public List<WarehouseStorageMap> listWarehouseStorageMapByWarehouseID(Long warehouseId) {
		return list("FROM WarehouseStorageMap wsm WHERE wsm.warehouse.id = ? ORDER BY wsm.coldStorageName ASC", warehouseId);
	}
	
	@Override
	public WarehouseStorageMap findMaxBinHoldByWarehouseIdAndColdStorageName(long id,String coldStorageName) {
		Object[] value = { id, coldStorageName };
		return (WarehouseStorageMap) find("FROM WarehouseStorageMap cs WHERE cs.warehouse.id= ? and cs.coldStorageName=?", value);
	}
	@Override
	public List<Object[]> listWarehouseIdAndName() {
		// TODO Auto-generated method stub
		return list("SELECT w.id,w.code,w.name FROM Warehouse w where w.typez=? order by w.name asc",Warehouse.COOPERATIVE);
	}

	@Override
	public Warehouse findGinnerByGinningId(Long ginningId) {
		Object[] values = { ginningId, Warehouse.WarehouseTypes.GINNER.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez=?", values);
	}

	@Override
	public List<Object[]> listOfGinningFromBaleGeneration(String seasonsCode) {
		List<Object[]> result = list("SELECT DISTINCT bg.ginning.id,bg.ginning.name FROM BaleGeneration bg where bg.season=? and bg.status=0",seasonsCode);
		return result;
	}

	@Override
	public List<Object[]> listOfSpinning() {
		return list("SELECT DISTINCT sp.id,sp.name FROM Warehouse sp where sp.typez=?", WarehouseTypes.SPINNER.ordinal());
	}

	@Override
	public List<Object[]> listOfHeapByGinningFromBaleGeneration(String selectedGinning,String season) {
		return list("SELECT DISTINCT bg.heap,(SELECT DISTINCT fc.name from FarmCatalogue fc where fc.code=bg.heap) from BaleGeneration bg where bg.ginning.id=? and bg.season=?",new Object[]{Long.parseLong(selectedGinning),season});
	}
	
	public Warehouse findGinningById(long id) {

		Object[] values = { id, Warehouse.WarehouseTypes.GINNER.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez=?", values);
	}
	
	public Warehouse findSpinningById(long id) {

		Object[] values = { id, Warehouse.WarehouseTypes.SPINNER.ordinal() };
		return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.typez=?", values);
	}

	@Override
	public Warehouse findWarehouseByIdAndTenantId(long id, String tenantId) {
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Warehouse wh WHERE wh.id = :Id");
		query.setParameter("Id", id);

		List<Warehouse> wList = query.list();
		Warehouse warehouse = null;
		if (wList.size() > 0) {
			warehouse = (Warehouse) wList.get(0);
		}

		session.flush();
		session.close();
		return warehouse;
		
		//return (Warehouse) find("FROM Warehouse wh WHERE wh.id= ? AND wh.branchId=?", new Object[]{id,tenantId});
	}

	@Override
	public Village findVillageByIdAndTenant(long id, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Village v WHERE v.id = :Id");
		query.setParameter("Id", id);

		List<Village> vList = query.list();
		Village village = null;
		if (vList.size() > 0) {
			village = (Village) vList.get(0);
		}

		session.flush();
		session.close();
		return village;
	}

	@Override
	public List<Warehouse> listWarehouseByWarehouseIds(String idsArr) {
		String queryString = "SELECT * FROM warehouse w where w.id in ("+idsArr+")";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	public List<Country> listCountriesWithAll() {

		return list("select distinct c FROM Country c left join fetch c.states s left join fetch s.localities ld left join fetch ld.municipalities m left join fetch m.villages left join fetch m.gramPanchayats gm  left join fetch gm.villages ORDER BY c.name ASC");
	}
	
	public List<Object[]> findLocalityByBranch(String Branch) {

		// return list("FROM Locality l WHERE l.branchId = ?", Branch);
		return list("SELECT l.id,l.code,l.name FROM Locality l WHERE l.code = ? order by l.name asc", Branch);
	}
	
	@Override
	public void updateLocHistory(Long object, String lat, String lon) {
		String queryString = "update loc_history set address='"+lat.replaceAll("'", "''") +"' ,end_address='"+lon.replaceAll("'", "''")+"' where id="+object+";";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		query.executeUpdate();
		sessions.flush();
		sessions.close();
		
	}
	
}
