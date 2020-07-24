/*
\ * LocationService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.traceability.HeapDataDetail;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.dao.IUserDAO;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.LocationHistoryDetail;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.CultivationDetail;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.entity.Location;
import com.sourcetrace.eses.util.entity.LocationLevel;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.GMO;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ServiceLocation;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;

/**
 * The Class LocationService.
 * 
 * @author $Author: boopalan $
 * @version $Rev: 838 $ $Date: 2010-03-15 12:55:12 +0530 (Mon, 15 Mar 2010) $
 */
@Service
@Transactional
public class LocationService implements ILocationService {
	@Autowired
	private ILocationDAO locationDAO;
	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private IServicePointDAO servicePointDAO;
	@Autowired
	private IAgentDAO agentDAO;
	@Autowired
	private IClientDAO clientDAO;
	@Autowired
	private IShopDealerDAO shopDealerDAO;
	@Autowired
	private IFarmerDAO farmerDAO;
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IESESystemDAO systemDAO;

	/**
	 * Sets the location dao.
	 * 
	 * @param locationDAO
	 *            the new location dao
	 */
	public void setLocationDAO(ILocationDAO locationDAO) {

		this.locationDAO = locationDAO;
	}

	@Override
	public List<Location> listLocations() {
		// TODO Auto-generated method stub
		return locationDAO.listLocations();
	}

	@Override
	public void addLocation(Location location) {
		// TODO Auto-generated method stub
		locationDAO.save(location);
	}

	@Override
	public void editLocation(Location location) {
		// TODO Auto-generated method stub
		locationDAO.update(location);
	}

	@Override
	public void removeLocation(Location location) {
		// TODO Auto-generated method stub
		locationDAO.delete(location);
	}

	@Override
	public Location findLocationById(long id) {
		// TODO Auto-generated method stub
		return locationDAO.findLocationById(id);
	}

	@Override
	public Location findLocationByCode(String code) {
		// TODO Auto-generated method stub
		return locationDAO.findLocationByCode(code);
	}

	@Override
	public Location findLocationByName(String name) {
		// TODO Auto-generated method stub
		return locationDAO.findLocationByName(name);
	}

	@Override
	public LocationLevel findLevelByCode(String levelCode) {
		// TODO Auto-generated method stub
		return locationDAO.findLevelByCode(levelCode);
	}

	@Override
	public List<Object[]> listLocationsInfoByLevelCode(String levelCode) {
		// TODO Auto-generated method stub
		return locationDAO.listLocationsInfoByLevelCode(levelCode);
	}

	@Override
	public List<Object[]> listLocationsInfoByParentCode(String parentCode) {
		// TODO Auto-generated method stub
		return locationDAO.listLocationsInfoByParentCode(parentCode);
	}

	/**
	 * Sets the farmer dao.
	 * 
	 * @param farmerDAO
	 *            the new farmer dao
	 */
	public void setFarmerDAO(IFarmerDAO farmerDAO) {

		this.farmerDAO = farmerDAO;
	}

	/**
	 * Gets the farmer dao.
	 * 
	 * @return the farmer dao
	 */
	public IFarmerDAO getFarmerDAO() {

		return farmerDAO;
	}

	/**
	 * Gets the user dao.
	 * 
	 * @return the user dao
	 */
	public IUserDAO getUserDAO() {

		return userDAO;
	}

	/**
	 * Sets the user dao.
	 * 
	 * @param userDAO
	 *            the new user dao
	 */
	public void setUserDAO(IUserDAO userDAO) {

		this.userDAO = userDAO;
	}

	/**
	 * Gets the service point dao.
	 * 
	 * @return the service point dao
	 */
	public IServicePointDAO getServicePointDAO() {

		return servicePointDAO;
	}

	/**
	 * Sets the service point dao.
	 * 
	 * @param servicePointDAO
	 *            the new service point dao
	 */
	public void setServicePointDAO(IServicePointDAO servicePointDAO) {

		this.servicePointDAO = servicePointDAO;
	}

	/**
	 * Gets the agent dao.
	 * 
	 * @return the agent dao
	 */
	public IAgentDAO getAgentDAO() {

		return agentDAO;
	}

	/**
	 * Sets the agent dao.
	 * 
	 * @param agentDAO
	 *            the new agent dao
	 */
	public void setAgentDAO(IAgentDAO agentDAO) {

		this.agentDAO = agentDAO;
	}

	/**
	 * Gets the client dao.
	 * 
	 * @return the client dao
	 */
	public IClientDAO getClientDAO() {

		return clientDAO;
	}

	/**
	 * Sets the client dao.
	 * 
	 * @param clientDAO
	 *            the new client dao
	 */
	public void setClientDAO(IClientDAO clientDAO) {

		this.clientDAO = clientDAO;
	}

	/**
	 * Gets the shop dealer dao.
	 * 
	 * @return the shop dealer dao
	 */
	public IShopDealerDAO getShopDealerDAO() {

		return shopDealerDAO;
	}

	/**
	 * Sets the shop dealer dao.
	 * 
	 * @param shopDealerDAO
	 *            the new shop dealer dao
	 */
	public void setShopDealerDAO(IShopDealerDAO shopDealerDAO) {

		this.shopDealerDAO = shopDealerDAO;
	}

	/**
	 * Gets the location dao.
	 * 
	 * @return the location dao
	 */
	public ILocationDAO getLocationDAO() {

		return locationDAO;
	}

	/**
	 * Sets the system dao.
	 * 
	 * @param systemDAO
	 *            the new system dao
	 */
	public void setSystemDAO(IESESystemDAO systemDAO) {

		this.systemDAO = systemDAO;
	}

	/**
	 * Sets the product distribution dao.
	 * 
	 * @param productDistributionDAO
	 *            the new product distribution dao
	 */
	public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {

		this.productDistributionDAO = productDistributionDAO;
	}

	/**
	 * Gets the product distribution dao.
	 * 
	 * @return the product distribution dao
	 */
	public IProductDistributionDAO getProductDistributionDAO() {

		return productDistributionDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addCountry(com
	 * .sourcetrace.esesw.entity.profile.Country)
	 */
	public void addCountry(Country country) {

		country.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.save(country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addLocality(com
	 * .sourcetrace.esesw.entity.profile.Locality)
	 */
	public void addLocality(Locality locality) {

		locality.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.save(locality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addMunicipality
	 * (com.sourcetrace.esesw.entity.profile.Municipality)
	 */
	public void addMunicipality(Municipality municipality) {

		municipality.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.save(municipality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#addState(com.
	 * sourcetrace.esesw.entity.profile.State)
	 */
	public void addState(State state) {

		state.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.save(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#removeCountry(
	 * java.lang.String)
	 */
	public void removeCountry(String name) {

		locationDAO.delete(findCountryByName(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#removeLocality
	 * (java.lang.String)
	 */
	public void removeLocality(String name) {

		locationDAO.delete(findLocalityByName(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#removeMunicipality
	 * (java.lang.String)
	 */
	public void removeMunicipality(String name) {

		locationDAO.delete(findMunicipalityByName(name));
	}

	/*
	 * * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#removeState(java
	 * .lang.String)
	 */
	public void removeState(String name) {

		locationDAO.delete(findStateByName(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editCountry(com
	 * .sourcetrace.esesw.entity.profile.Country)
	 */
	public void editCountry(Country country) {

		country.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.update(country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editLocality(com
	 * .sourcetrace.esesw.entity.profile.Locality)
	 */
	public void editLocality(Locality locality) {

		locality.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.update(locality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editMunicipality
	 * (com.sourcetrace.esesw.entity.profile.Municipality)
	 */
	public void editMunicipality(Municipality municipality) {

		municipality.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.update(municipality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editState(com.
	 * sourcetrace.esesw.entity.profile.State)
	 */
	public void editState(State state) {

		state.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.update(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findCountryByName
	 * (java.lang.String)
	 */
	public Country findCountryByName(String name) {

		return locationDAO.findCountryByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findLocalityByName
	 * (java.lang.String)
	 */
	public Locality findLocalityByName(String name) {

		return locationDAO.findLocalityByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findLocalityByCode
	 * (java.lang.String)
	 */
	public Locality findLocalityByCode(String code) {

		return locationDAO.findLocalityByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findMunicipalityByName (java.lang.String)
	 */
	public Municipality findMunicipalityByName(String name) {

		return locationDAO.findMunicipalityByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findMunicipality
	 * (java.lang.String, java.lang.String)
	 */
	public Municipality findMunicipality(String name, String postalCode) {

		return locationDAO.findMunicipality(name, postalCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * findMunicipalitiesByPostalCode(java.lang.String)
	 */
	public List<Municipality> findMunicipalitiesByPostalCode(String postalCode) {

		return locationDAO.findMunicipalitiesByPostalCode(postalCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findStateByName
	 * (java.lang.String)
	 */
	public State findStateByName(String name) {

		return locationDAO.findStateByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findCountryById
	 * (java.lang.Long)
	 */
	public Country findCountryById(Long id) {

		return locationDAO.findCountryById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findLocalityById
	 * (java.lang.Long)
	 */
	public Locality findLocalityById(Long id) {

		return locationDAO.findLocalityById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findMunicipalityById (java.lang.Long)
	 */
	public Municipality findMunicipalityById(Long id) {

		return locationDAO.findMunicipalityById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findStateById(
	 * java.lang.Long)
	 */
	public State findStateById(Long id) {

		return locationDAO.findStateById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listCountries()
	 */
	public List<Country> listCountries() {

		return locationDAO.listCountries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listLocalities
	 * (java.lang.String)
	 */
	public List<Locality> listLocalities(String state) {

		return locationDAO.listLocalities(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listMunicipalities
	 * (java.lang.String)
	 */
	public List<Municipality> listMunicipalities(String locality) {

		return locationDAO.listMunicipalities(locality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listStates(java
	 * .lang.String)
	 */
	public List<State> listStates(String country) {

		return locationDAO.listStates(country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addServiceLocation
	 * (com.sourcetrace.esesw.entity.profile.ServiceLocation)
	 */
	public void addServiceLocation(ServiceLocation serviceLocation) {

		locationDAO.save(serviceLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * editServiceLocation
	 * (com.sourcetrace.esesw.entity.profile.ServiceLocation)
	 */
	public void editServiceLocation(ServiceLocation temp) {

		locationDAO.update(temp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * removeServiceLocation
	 * (com.sourcetrace.esesw.entity.profile.ServiceLocation)
	 */
	public void removeServiceLocation(ServiceLocation serviceLocation) {

		locationDAO.delete(serviceLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findMunicipalityByCode (java.lang.String)
	 */
	public Municipality findMunicipalityByCode(String code) {

		return locationDAO.findMunicipalityByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#isCityMappingexist
	 * (long)
	 */
	public String isCityMappingexist(Municipality municipality) {

		boolean userMapped = userDAO.isCityMappingexist(municipality.getId());
		if (userMapped) {
			return "user.mapped";
		}
		// Commented - Adding Municipality will create Warehouse and Service
		// point
		/*
		 * boolean servicePointMapped = servicePointDAO.isCityMappingexist(id);
		 * if (servicePointMapped) { return "servicePoint.mapped"; }
		 */
		boolean agentMapped = isAgentMappedForServicePoint(municipality.getCode());
		if (agentMapped) {
			return "agent.mapped";
		}
		agentMapped = agentDAO.isCityMappingexist(municipality.getId());
		if (agentMapped) {
			return "agent.mapped";
		}
		boolean clientMapped = clientDAO.isCityMappingexist(municipality.getId());
		if (clientMapped) {
			return "client.mapped";
		}
		boolean shopDealerMapped = shopDealerDAO.isCityMappingexist(municipality.getId());
		if (shopDealerMapped) {
			return "shopDealer.mapped";
		}
		boolean farmerMapped = farmerDAO.isCityMappingexist(municipality.getId());
		if (farmerMapped) {
			return "farmer.mapped";
		}
		// Commented - Adding Municipality will create Warehouse and Service
		// point
		/*
		 * boolean warehouseMapped = locationDAO
		 * .isCityMappingExistsWithWarehouse(id); if (warehouseMapped) { return
		 * "warehouse.mapped"; }
		 */
		boolean stockExist = locationDAO.isWarehouseProductCityMappingExist(municipality.getCode());
		if (stockExist) {
			return "productStock.exist";
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findVillageByCode
	 * (java.lang.String)
	 */
	public Village findVillageByCode(String villageCode) {

		return locationDAO.findVillageByCode(villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findVillageById
	 * (long)
	 */
	public Village findVillageById(long id) {

		return locationDAO.findVillageById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findVillageByName
	 * (java.lang.String)
	 */
	public Village findVillageByName(String name) {

		return locationDAO.findVillageByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addVillage(com
	 * .sourcetrace.esesw.entity.profile.Village)
	 */
	public void addVillage(Village village) {

		// As City is not mapped directly with villages,these changes made
		// village.setCity(village.getCity());
		village.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.save(village);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editVillage(com
	 * .sourcetrace.esesw.entity.profile.Village)
	 */
	public void editVillage(Village existing) {

		// As City is not mapped directly with villages,these changes made
		// existing.setCity(existing.getCity());
		existing.setRevisionNo(DateUtil.getRevisionNumber());
		// editWarehouseRevNo(existing);
		locationDAO.update(existing);

	}

	private void editWarehouseRevNo(Village village) {

		if (!ObjectUtil.isEmpty(village)) {
			List<Warehouse> warehouses = listWarehouseByVillageId(village.getId());
			if (!ObjectUtil.isListEmpty(warehouses)) {
				for (Warehouse warehouse : warehouses) {
					editWarehouse(warehouse);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#removeVillage(
	 * java.lang.String)
	 */
	public void removeVillage(String name) {

		Village village = findVillageByName(name);
		if (!ObjectUtil.isEmpty(village)) {
			editWarehouseRevNo(village);
			locationDAO.delete(village);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * isVillageMappingExist (long)
	 */
	public String isVillageMappingExist(long id) {

		boolean farmerMapped = farmerDAO.isVillageMappingExist(id);
		if (farmerMapped) {
			return "farmer.mapped";
		} else {
			return productDistributionDAO.isVillageMappindExistForDistributionAndProcurement(id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listVillages(java
	 * .lang.String)
	 */
	public List<Village> listVillages(String selectedCity) {

		return locationDAO.listVillages(selectedCity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addWarehouse(com
	 * .ese.entity.profile.Warehouse)
	 */
	public void addWarehouse(Warehouse warehouse) {

		warehouse.setRevisionNo(DateUtil.getRevisionNumber());
		if (!ObjectUtil.isEmpty(warehouse.getRefCooperative())) {
			warehouse.getRefCooperative().setRevisionNo(DateUtil.getRevisionNumber());
		}
		locationDAO.save(warehouse);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editWarehouse(
	 * com.ese.entity.profile.Warehouse)
	 */
	public void editWarehouse(Warehouse existing) {

		existing.setRevisionNo(DateUtil.getRevisionNumber());
		if (!ObjectUtil.isEmpty(existing.getRefCooperative())) {
			existing.getRefCooperative().setRevisionNo(DateUtil.getRevisionNumber());
		}
		locationDAO.update(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findWarehouseByCode (java.lang.String)
	 */
	public Warehouse findWarehouseByCode(String warehouseCode) {

		return locationDAO.findWarehouseByCode(warehouseCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findWarehouseById
	 * (long)
	 */
	public Warehouse findWarehouseById(long id) {

		return locationDAO.findWarehouseById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findWarehouseByName (java.lang.String)
	 */
	public Warehouse findWarehouseByName(String name) {

		return locationDAO.findWarehouseByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#removeWarehouse
	 * (com.ese.entity.profile.Warehouse)
	 */
	public void removeWarehouse(Warehouse warehouse) {

		locationDAO.delete(warehouse);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#listCity()
	 */
	public List listCity() {

		return locationDAO.listCity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#listVillage()
	 */
	public List listVillage() {

		return locationDAO.listVillage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listWarehouseByCity (long)
	 */
	public List<Warehouse> listWarehouseByCity(long id) {

		return locationDAO.listWarehouseByCity(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listWarehouse()
	 */
	public List<Warehouse> listWarehouse() {

		return locationDAO.listWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listVillageByCityId (long)
	 */
	public List<Village> listVillageByCityId(long id) {

		return locationDAO.listVillageByCityId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listVillageByCity(
	 * long)
	 */
	public List<Village> listVillageByCity(long id) {

		return locationDAO.listVillageByCity(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * findMunicipalityByVillageCode(java .lang.String)
	 */
	public Municipality findMunicipalityByVillageCode(String villageCode) {

		return locationDAO.findMunicipalityByVillageCode(villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addLocationHistory
	 * (com.ese.entity. txn.LocationHistory)
	 */
	public void addLocationHistory(LocationHistory locationHistory) {

		ESESystem eseSystem = systemDAO.findPrefernceById("2");
		int thresholdValue = Integer
				.valueOf(eseSystem.getPreferences().get(ESESystem.LOCATION_HISTORY_THRESHOLD_VALUE));

		long id = locationDAO.findThresholdExceededLocationHistory(locationHistory.getSerialNumber(), thresholdValue);
		if (id > 0) {
			locationDAO.deleteLocationHistoryById(id);
		}
		locationDAO.save(locationHistory);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * listDeviceLocationHistory(java.lang .String, java.util.Date,
	 * java.util.Date)
	 */
	public List<LocationHistory> listDeviceLocationHistory(String serialNumber, Date startDate, Date endDate) {

		return locationDAO.listDeviceLocationHistory(serialNumber, startDate, endDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * listDeviceLocationHistoryByAgentId (java.lang.String, java.util.Date,
	 * java.util.Date)
	 */
	public List<LocationHistory> listDeviceLocationHistoryByAgentId(String agentId, Date startDate, Date endDate) {

		return locationDAO.listDeviceLocationHistoryByAgentId(agentId, startDate, endDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listVillagesByCityCode (java.lang.String )
	 */
	public List<Village> listVillagesByCityCode(String cityCode) {

		return locationDAO.listVillagesByCityCode(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listCityByFarmerCity ()
	 */
	public List<Municipality> listCityByFarmerCity() {

		return locationDAO.listCityByFarmerCity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * isAgentMappedForServicePoint(java. lang.String)
	 */
	public boolean isAgentMappedForServicePoint(String code) {

		return agentDAO.isAgentMappedForServicePoint(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listWarehouseForMTNTReceiptNo()
	 */
	public List<Warehouse> listWarehouseForMTNTReceiptNo() {

		return locationDAO.listWarehouseForMTNTReceiptNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listOfProductWarehouse()
	 */
	public List<WarehouseProduct> listOfProductWarehouse() {

		return locationDAO.listOfProductWarehouse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listWarehouseForProductExist()
	 */
	public List<Warehouse> listWarehouseForProductExist() {

		return locationDAO.listWarehouseForProductExist();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * listWarehouseByTripSheetChartNoCityWarehouseStock()
	 */
	public List<Warehouse> listWarehouseByTripSheetChartNoCityWarehouseStock() {

		return locationDAO.listWarehouseByTripSheetChartNoCityWarehouseStock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findWarehouseByCityId(long)
	 */
	public Warehouse findWarehouseByCityId(long cityId) {

		return locationDAO.findWarehouseByCityId(cityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findVillage(long)
	 */
	public Village findVillage(long id) {

		return locationDAO.findVillage(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listServicePoint()
	 */
	public List<ServicePoint> listServicePoint() {

		return locationDAO.listServicePoint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listActiveFarmersSamithiByAgentId( long)
	 */
	public List<Warehouse> listActiveFarmersSamithiByAgentId(long id) {

		return locationDAO.listActiveFarmersSamithiByAgentId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listActiveFarmersSamithiByCoOperativeId (long)
	 */
	public List<Warehouse> listActiveFarmersSamithiByCoOperativeId(long coOperativeId) {

		return locationDAO.listActiveFarmersSamithiByCoOperativeId(coOperativeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listActiveFarmersVillageBySamithiId (long)
	 */
	public List<Village> listActiveFarmersVillageBySamithiId(long samithiId) {

		return locationDAO.listActiveFarmersVillageBySamithiId(samithiId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#addGramPanchayat(
	 * com.sourcetrace.esesw .entity.profile.GramPanchayat)
	 */
	public void addGramPanchayat(GramPanchayat gramPanchayat) {

		gramPanchayat.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.save(gramPanchayat);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#editGramPanchayat(
	 * com.sourcetrace. esesw.entity.profile.GramPanchayat)
	 */
	public void editGramPanchayat(GramPanchayat existing) {

		existing.setRevisionNo(DateUtil.getRevisionNumber());
		locationDAO.update(existing);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findGramPanchayatById(long)
	 */
	public GramPanchayat findGramPanchayatById(long id) {

		return locationDAO.findGramPanchayatById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findGramPanchayatByName(java.lang. String)
	 */
	public GramPanchayat findGramPanchayatByName(String name) {

		return locationDAO.findGramPanchayatByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * removeGramPanchayat(java.lang.String)
	 */
	public void removeGramPanchayat(String name) {

		locationDAO.delete(findGramPanchayatByName(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listGramPanchayats
	 * (java.lang.String)
	 */
	public List<GramPanchayat> listGramPanchayats(String selectedCity) {

		return locationDAO.listGramPanchayats(selectedCity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listCoOperativeAndSamithi()
	 */
	public List<Warehouse> listCoOperativeAndSamithi() {

		return locationDAO.listCoOperativeAndSamithi();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findSamithiAndCoOperativeByVillage (long)
	 */
	public Warehouse findSamithiAndCoOperativeByVillage(long villageId) {

		return locationDAO.findSamithiAndCoOperativeByVillage(villageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listActiveFarmersVillageBySamithiCode (java.lang.String)
	 */
	public List<Village> listActiveFarmersVillageBySamithiCode(String samithiCode) {

		return locationDAO.listActiveFarmersVillageBySamithiCode(samithiCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listOfVillageCodeNameByCityCode(java .lang.String)
	 */
	public List<Object[]> listOfVillageCodeNameByCityCode(String cityCode) {

		return locationDAO.listOfVillageCodeNameByCityCode(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listOfVillagesByNames(java.util.List)
	 */
	public List<Village> listOfVillagesByNames(List<String> villageNames) {

		return locationDAO.listOfVillagesByNames(villageNames);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findVillageNameByCode(java.lang.String )
	 */
	public String findVillageNameByCode(String code) {

		return locationDAO.findVillageNameByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listOfVillagesByCodes(java.util.List)
	 */
	public List<Village> listOfVillagesByCodes(List<String> villageCodes) {

		return locationDAO.listOfVillagesByCodes(villageCodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findCityNameByCode
	 * (java.lang.String)
	 */
	public String findCityNameByCode(String code) {

		return locationDAO.findCityNameByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findSamithiByCode(
	 * java.lang.String)
	 */
	public Warehouse findSamithiByCode(String samithiCode) {

		return locationDAO.findSamithiByCode(samithiCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findCoOperativeByCode(java.lang.String )
	 */
	public Warehouse findCoOperativeByCode(String code) {

		return locationDAO.findCoOperativeByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findCooperativeMappedWithSamithi(java .lang.String)
	 */
	public boolean findCooperativeMappedWithSamithi(String code) {

		Warehouse warehouse = locationDAO.findCooperativeMappedWithSamithi(code);
		boolean flag = false;
		if (!ObjectUtil.isEmpty(warehouse))
			flag = true;

		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listOfCooperatives
	 * ()
	 */
	public List<Warehouse> listOfCooperatives() {

		return locationDAO.listOfCooperatives();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listOfSamithi()
	 */
	public List<Warehouse> listOfSamithi() {

		return locationDAO.listOfSamithi();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findFarmerMappedWithSamithi(long)
	 */
	public boolean findFarmerMappedWithSamithi(long id) {

		return locationDAO.findFarmerMappedWithSamithi(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findCooperativeByName(java.lang.String )
	 */
	public Warehouse findCooperativeByName(String cooperativeName) {

		return locationDAO.findCooperativeByName(cooperativeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * findCooperativeVillageMappedWtihSamithiVillage(long, java.lang.String)
	 */
	public String findCooperativeVillageMappedWtihSamithiVillage(long cooperId, String villageCode) {

		return locationDAO.findCooperativeVillageMappedWtihSamithiVillage(cooperId, villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findCoOperativeByName(java.lang.String )
	 */
	public Warehouse findCoOperativeByName(String name) {

		return locationDAO.findCoOperativeByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listFarmerMapppedSmithiVillages(java .lang.Long)
	 */
	public List<Village> listFarmerMapppedSmithiVillages(Long id) {

		return locationDAO.listFarmerMapppedSmithiVillages(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listSamithiByVillageId(long)
	 */
	public List<Warehouse> listSamithiByVillageId(long id) {

		return locationDAO.listSamithiByVillageId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findSamithiByName(
	 * java.lang.String)
	 */
	public Warehouse findSamithiByName(String name) {

		return locationDAO.findSamithiByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listVillagesByPanchayat(java.lang. String)
	 */
	public List<Village> listVillagesByPanchayat(String selectedPanchayat) {

		return locationDAO.listVillagesByPanchayat(selectedPanchayat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * isVillageMappedWithCooperative(long)
	 */
	public boolean isVillageMappedWithCooperative(long id) {

		return locationDAO.isVillageMappedWithCooperative(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#getVillageExistMap
	 * (java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getVillageMappedWithAgent(Set<Warehouse> samithis) {

		Map<String, String> villageMap = new HashMap<String, String>();
		Iterator itr = samithis.iterator();
		while (itr.hasNext()) {
			Warehouse samithi = (Warehouse) itr.next();
			Iterator itrVil = samithi.getVillages().iterator();
			while (itrVil.hasNext()) {
				Village village = (Village) itrVil.next();
				villageMap.put(village.getCode(), village.getName());
			}
		}
		return villageMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.esesw.service.profile.ILocationService#
	 * listCoOperativeManagerVillageByCoOperativeManagerId(long)
	 */
	public List<Village> listCoOperativeManagerVillageByCoOperativeManagerId(long id) {

		return locationDAO.listCoOperativeManagerVillageByCoOperativeManagerId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listFieldStaffVillageByFieldStaffId (long)
	 */
	public List<Village> listFieldStaffVillageByFieldStaffId(long id) {

		return locationDAO.listFieldStaffVillageByFieldStaffId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#listSamithies()
	 */
	public List<Warehouse> listSamithies() {

		return locationDAO.listSamithies();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listFarmerCountBySamithiAndGender( long)
	 */
	public List<Object[]> listFarmerCountBySamithiAndGender(long id) {

		return locationDAO.listFarmerCountBySamithiAndGender(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listFieldStaffIdAndNameBySamithiId (long)
	 */
	public String listFieldStaffIdAndNameBySamithiId(long id) {

		return locationDAO.listFieldStaffIdAndNameBySamithiId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listCountriesByRevisionNo(long)
	 */
	public List<Country> listCountriesByRevisionNo(long revisionNo) {

		return locationDAO.listCountriesByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listStatesByRevisionNo(long)
	 */
	public List<State> listStatesByRevisionNo(long revisionNo) {

		return locationDAO.listStatesByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listLocalitiesByRevisionNo(long)
	 */
	public List<Locality> listLocalitiesByRevisionNo(long revisionNo) {

		return locationDAO.listLocalitiesByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listMunicipalitiesByRevisionNo(long)
	 */
	public List<Municipality> listMunicipalitiesByRevisionNo(long revisionNo) {

		return locationDAO.listMunicipalitiesByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listGramPanchayatsByRevisionNo(long)
	 */
	public List<GramPanchayat> listGramPanchayatsByRevisionNo(long revisionNo) {

		return locationDAO.listGramPanchayatsByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listVillagesByRevisionNo(long)
	 */
	public List<Village> listVillagesByRevisionNo(long revisionNo) {

		return locationDAO.listVillagesByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listVillageByCoOperative(java.lang .String)
	 */
	public List<Village> listVillageByCoOperativeCode(String selectedCooperative) {

		return locationDAO.listVillageByCoOperativeCode(selectedCooperative);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listCoOperativeAndSamithiByRevisionNo (long, int)
	 */
	public List<Warehouse> listCoOperativeAndSamithiByRevisionNo(long revisionNo, int typez) {

		return locationDAO.listCoOperativeAndSamithiByRevisionNo(revisionNo, typez);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listWarehouseByVillageId(long)
	 */
	public List<Warehouse> listWarehouseByVillageId(long villageId) {

		return locationDAO.listWarehouseByVillageId(villageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listCoOperativeByAgent(java.lang.String )
	 */
	public List<Warehouse> listCoOperativeByAgent(String agentId) {

		return locationDAO.listCoOperativeByAgent(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listCoOperativeWithManagers()
	 */
	public List<Warehouse> listCoOperativeWithManagers() {

		return locationDAO.listCoOperativeWithManagers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listOfWareHouseProductCooperatives()
	 */
	public List<Warehouse> listOfWareHouseProductCooperatives() {

		return locationDAO.listOfWareHouseProductCooperatives();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findLocalityByStateId(long)
	 */
	public List<Locality> findLocalityByStateId(long selectedState) {

		return locationDAO.findLocalityByStateId(selectedState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listGramPanchayatsByCityId(long)
	 */
	public List<GramPanchayat> listGramPanchayatsByCityId(long selectedCity) {

		return locationDAO.listGramPanchayatsByCityId(selectedCity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listMunicipalitiesByLocalityId(long)
	 */
	public List<Municipality> listMunicipalitiesByLocalityId(long selectedLocality) {

		return locationDAO.listMunicipalitiesByLocalityId(selectedLocality);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listVillagesByPanchayatId(long)
	 */
	public List<Village> listVillagesByPanchayatId(long selectedPanchayat) {

		return locationDAO.listVillagesByPanchayatId(selectedPanchayat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * findVillageBySelectedVillageId(long)
	 */
	public Village findVillageBySelectedVillageId(long selectedVillage) {

		return locationDAO.findVillageBySelectedVillageId(selectedVillage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.service.profile.ILocationService#
	 * listOfWarehouseCount()
	 */
	public long listOfWarehouseCount() {

		return locationDAO.listOfWarehouseCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.service.profile.ILocationService#findSamithiById(
	 * long)
	 */
	public Warehouse findSamithiById(long id) {

		return locationDAO.findSamithiById(id);
	}

	public List<Village> listVillagesByCityId(long selectedCity) {

		return locationDAO.listVillagesByCityId(selectedCity);
	}

	// added panchayat
	public List<GramPanchayat> listPanchayatBycityId(long selectedCity) {

		return locationDAO.listPanchayatBycityId(selectedCity);
	}

	public boolean findCooperativeMappedWithWarhousePayment(long warhouseId) {
		// TODO Auto-generated method stub

		WarehousePayment warehouse = locationDAO.findCooperativeMappedWithWarhousePayment(warhouseId);
		boolean flag = false;
		if (!ObjectUtil.isEmpty(warehouse))
			flag = true;

		return flag;
	}

/*	@Override
	public Map<String, String> listVillageIdAndName() {
		// TODO Auto-generated method stub
		return locationDAO.listVillageIdAndName();
	}*/

	@Override
	public List<Municipality> listMunicipality() {
		// TODO Auto-generated method stub
		return locationDAO.listMunicipality();
	}

	@Override
	public List<State> listStatesByCountryID(Long countryID) {
		return locationDAO.listStatesByCountryID(countryID);
	}

	@Override
	public List<Locality> listLocalitiesByStateID(Long stateId) {
		return locationDAO.listLocalitiesByStateID(stateId);
	}

	@Override
	public List<Village> listVillagesByCityID(Long cityId) {
		return locationDAO.listVillagesByCityID(cityId);
	}

	@Override
	public long listOfWarehouseCountBasedOnBranch(String branchIdValue) {
		return locationDAO.listOfWarehouseCountBasedOnBranch(branchIdValue);
	}

	@Override
	public List<Municipality> listMunicipalityBasedOnBranch(String branchIdValue) {
		return locationDAO.listMunicipalityBasedOnBranch(branchIdValue);
	}

	@Override
	public List<Warehouse> listSamithiesBasedOnType() {
		// TODO Auto-generated method stub
		return locationDAO.listSamithiesBasedOnType();
	}

	@Override
	public Integer findWarehouseCount() {
		return locationDAO.findWarehouseCount();
	}

	@Override
	public Integer findWarehouseCountByMonth(Date sDate, Date eDate) {
		return locationDAO.findWarehouseCountByMonth(sDate, eDate);
	}

	@Override
	public List<Object> listWarehouseProductAndStock() {
		return locationDAO.listWarehouseProductAndStock();
	}

	public Village findVillageByCode(String villageCode, String tenantId) {

		return locationDAO.findVillageByCode(villageCode, tenantId);
	}

	public Warehouse findSamithiByCode(String samithiCode, String tenantId) {

		return locationDAO.findSamithiByCode(samithiCode, tenantId);
	}

	@Override
	public Warehouse findCoOperativeByCode(String servicePointId, String tenantId) {

		return locationDAO.findCoOperativeByCode(servicePointId, tenantId);
	}

	public List<Municipality> listOfCitysByIds(List<Long> municipality) {

		return locationDAO.listOfCitysByIds(municipality);
	}

	public List<Object> listWarehouseProductAndStockByWarehouseId(Long warehouseId) {
		return locationDAO.listWarehouseProductAndStockByWarehouseId(warehouseId);
	}

	@Override
	public List<Object[]> listCityCodeAndName() {
		return locationDAO.listCityCodeAndName();
	}

	@Override
	public List<Object[]> listVillageCodeAndNameByCityCode(String cityCode) {
		// TODO Auto-generated method stub
		return locationDAO.listVillageCodeAndNameByCityCode(cityCode);
	}

	@Override
	public List<Warehouse> listSamithiBySangham(String groupCode, String villageCode) {
		// TODO Auto-generated method stub
		return locationDAO.listSamithiBySangham(groupCode, villageCode);
	}

	@Override
	public List<Object[]> listLocalityIdCodeAndName() {
		return locationDAO.listLocalityIdCodeAndName();
	}

	@Override
	public List<Object[]> listCityCodeAndNameByDistrictId(Long id) {
		return locationDAO.listCityCodeAndNameByDistrictId(id);
	}

	@Override
	public List<Object[]> listGramPanchayatIdCodeName() {
		return locationDAO.listGramPanchayatIdCodeName();
	}

	@Override
	public List<Object[]> listGramPanchayatByCityId(Long id) {
		return locationDAO.listGramPanchayatByCityId(id);
	}

	@Override
	public List<Object[]> listVillageByPanchayatId(Long id) {

		return locationDAO.listVillageByPanchayatId(id);
	}

	@Override
	public Integer findFarmerCountBySamtihi(Long id) {
		return locationDAO.findFarmerCountBySamtihi(id);
	}

	@Override
	public List<Object[]> listOfCooperativesByDistribution() {
		// TODO Auto-generated method stub
		return locationDAO.listOfCooperativesByDistribution();
	}

	@Override
	public List<Object[]> listOfVillageInfoByProcurement() {
		// TODO Auto-generated method stub
		return locationDAO.listOfVillageInfoByProcurement();
	}

	@Override
	public List<Object[]> listOfCooperativeByProcurement() {
		// TODO Auto-generated method stub
		return locationDAO.listOfCooperativeByProcurement();
	}

	@Override
	public List<Object[]> listOfWarehouseByStock() {
		// TODO Auto-generated method stub
		return locationDAO.listOfWarehouseByStock();
	}

	@Override
	public List<Object[]> listOfWarehouseByStockEntry() {
		// TODO Auto-generated method stub
		return locationDAO.listOfWarehouseByStockEntry();
	}

	@Override
	public List<Object[]> listOfWarehousePMT() {
		// TODO Auto-generated method stub
		return locationDAO.listOfWarehousePMT();
	}

	@Override
	public GramPanchayat findGrampanchaythByCode(String code) {
		// TODO Auto-generated method stub
		return locationDAO.findGrampanchaythByCode(code);
	}

	@Override
	public List<GramPanchayat> listGramPanchayatsByCityCode(String selectedCity) {
		return locationDAO.listGramPanchayatsByCityCode(selectedCity);
	}

	@Override
	public List<Municipality> listMunicipalitiesByCode(String locality) {
		return locationDAO.listMunicipalitiesByCode(locality);
	}

	@Override
	public List<GramPanchayat> listGramPanchayatsByCode(String selectedCity) {
		return locationDAO.listGramPanchayatsByCode(selectedCity);
	}

	@Override
	public List<State> listStatesByCode(String country) {

		return locationDAO.listStatesByCode(country);
	}

	@Override
	public void removeVillage(Village village) {

		if (!ObjectUtil.isEmpty(village)) {
			editWarehouseRevNo(village);
			locationDAO.delete(village);
		}

	}

	@Override
	public void removeGramPanchayat(GramPanchayat gm) {
		locationDAO.delete(gm);

	}

	@Override
	public void removeCity(Municipality mun) {
		locationDAO.delete(mun);

	}

	@Override
	public List<Warehouse> listSamithiBySanghamType(String groupCode) {
		// TODO Auto-generated method stub
		return locationDAO.listSamithiBySanghamType(groupCode);
	}

	@Override
	public State findStateByCode(String code) {
		return locationDAO.findStateByCode(code);
	}

	@Override
	public List<State> listOfStates() {
		// TODO Auto-generated method stub
		return locationDAO.listOfStates();
	}

	public Country findCountryByCode(String code) {
		return locationDAO.findCountryByCode(code);
	}

	@Override
	public List<Object[]> listOfCooperativesByProductReturn() {
		// TODO Auto-generated method stub
		return locationDAO.listOfCooperativesByProductReturn();
	}

	@Override
	public List<Object[]> listWarehouses() {
		// TODO Auto-generated method stub
		return locationDAO.listWarehouses();
	}

	@Override
	public void editCultivation(Cultivation cultivation) {
		locationDAO.update(cultivation);
	}

	@Override
	public List<Object[]> listOfGroup() {
		return locationDAO.listOfGroup();
	}

	@Override
	public List<Object[]> listOfAgentsByAgroTxn() {
		// TODO Auto-generated method stub
		return locationDAO.listOfAgentsByAgroTxn();
	}

	@Override
	public List<Object[]> listOfFarmersByAgroTxn() {
		// TODO Auto-generated method stub
		return locationDAO.listOfFarmersByAgroTxn();
	}

	@Override
	public List<Municipality> listCityByProcurement() {
		// TODO Auto-generated method stub
		return locationDAO.listCityByProcurement();
	}

	@Override
	public List<Village> listVillageByProcurementAndCityId(String cityId) {
		// TODO Auto-generated method stub
		return locationDAO.listVillageByProcurementAndCityId(cityId);
	}

	@Override
	public Village findVillageByNameAndCity(String village, String city) {
		return locationDAO.findVillageByNameAndCity(village, city);
	}

	@Override
	public GramPanchayat findGramPanchayatByNameAndCity(String gp, String city) {
		return locationDAO.findGramPanchayatByNameAndCity(gp, city);
	}

	@Override
	public Municipality findMunicipalityByNameAndDistrict(String taluk, String districtz) {
		return locationDAO.findMunicipalityByNameAndDistrict(taluk, districtz);
	}

	@Override
	public DataLevel findDataLevelByName(String name) {
		// TODO Auto-generated method stub
		return locationDAO.findDataLevelByName(name);
	}

	@Override
	public DataLevel findDataLevelByCode(String code) {
		// TODO Auto-generated method stub
		return locationDAO.findDataLevelByCode(code);
	}

	@Override
	public List<LocationHistory> listDeviceLocationHistoryByAgentId(String agentId, Date startDate, Date endDate,
			String tenantId) {
		// TODO Auto-generated method stub
		return locationDAO.listDeviceLocationHistoryByAgentId(agentId, startDate, endDate, tenantId);
	}

	@Override
	public Map tableFields() {
		return locationDAO.tableFields();
	}

	@Override
	public Integer findGroupCount() {

		// TODO Auto-generated method stub
		return locationDAO.findGroupCount();
	}

	@Override
	public Integer findGroupCountByMonth(Date sDate, Date eDate) {

		// TODO Auto-generated method stub
		return locationDAO.findGroupCountByMonth(sDate, eDate);
	}

	@Override
	public Warehouse findWarehouseWithoutTypeById(Long id) {
		// TODO Auto-generated method stub
		return locationDAO.findWarehouseWithoutTypeById(id);
	}

	@Override
	public List<BankInformation> findWarehouseBankinfo(long id) {
		// TODO Auto-generated method stub
		return locationDAO.findWarehouseBankinfo(id);
	}

	@Override
	public List<Warehouse> listProcurementCenter() {
		// TODO Auto-generated method stub
		return locationDAO.listProcurementCenter();

	}

	@Override
	public List<Object[]> listOfWarehouses() {
		// TODO Auto-generated method stub
		return locationDAO.listOfWarehouses();
	}

	@Override
	public Warehouse findProcurementWarehouseById(long id) {
		return locationDAO.findProcurementWarehouseById(id);
	}

	@Override
	public List<Warehouse> listCoOperativeAndSamithiByRevisionNo(Long revNo, Integer[] warehousetypesarray) {
		// TODO Auto-generated method stub
		return locationDAO.listCoOperativeAndSamithiByRevisionNo(revNo, warehousetypesarray);
	}

	@Override
	public void updateCultivationDetailCost(CultivationDetail pestiCocDetails, String type) {
		locationDAO.deleteCultivationDetailByCultivationId(pestiCocDetails.getCultivation().getId(), type);
		locationDAO.save(pestiCocDetails);
	}

	@Override
	public void updateCultivationCost(long id, String cost, String type) {
		locationDAO.updateCultivationCost(id, cost, type);

	}

	@Override
	public List<GMO> listGMOBySeason(String seasonCode) {
		return locationDAO.listGMOBySeason(seasonCode);
	}
	@Override
	public HeapData isHeapSpaceAvailableByIcsCode(String ics) {
		return locationDAO.isHeapSpaceAvailableByIcsCode(ics);
	}

	@Override
	public HeapData findHeapDataByICS(String icsCode) {
		return locationDAO.findHeapDataByICS(icsCode);
	}

	@Override
	public void updateHeapData(HeapData hd) {
		locationDAO.update(hd);
		
	}

	@Override
	public boolean isStockMovedToHeap(long pmtDetId) {
		return locationDAO.isStockMovedToHeap(pmtDetId);
	}

	@Override
	public List<Object[]> listOfGiningFromHeap() {
		return locationDAO.listOfGiningFromHeap();
	}
	

	@Override
	public List<Object[]> listOfGinningFromHeap(String season) {
		return locationDAO.listOfGinningFromHeap(season);
	}

	@Override
	public List<Object[]> listOfProductFromHeapWithHeap(String selectedGinning, String selectedHeap) {
		return locationDAO.listOfProductFromHeapWithHeap(selectedGinning,selectedHeap);
	}

	@Override
	public List<Object[]> listOfICSFromHeapByGinningCodeAndProdut(String ginningCode,String selectedProduct){
		return locationDAO.listOfICSFromHeapByGinningCodeAndProdut(ginningCode,selectedProduct);
	}

	@Override
	public List<Object[]> listOfHeapsByGinningCodeProdutICS(String ginningCode,String season) {
		return locationDAO.listOfHeapsByGinningCodeProdutICS(ginningCode,season);
	}

	@Override
	public String findHeapQtyByHeapDataId(String selectedHeap) {
		return locationDAO.findHeapQtyByHeapDataId(selectedHeap);
	}

	@Override
	public HeapData findHeapDataById(String heapId) {
		return locationDAO.findHeapDataById(heapId);
	}

	@Override
	public void saveGinningProcess(GinningProcess ginningProcess) {
		locationDAO.save(ginningProcess);
	}

	@Override
	public Object[] findHeapQtyICSAndProductByHeapAndGinning(String selectedHeap, String selectedGinning, String selectedProduct,String season) {
		return locationDAO.findHeapQtyICSAndProductByHeapAndGinning(selectedHeap,selectedGinning,selectedProduct,season);
	}
	
	public List<Object[]> listOfCooperativesByGinningProcess(){
		return locationDAO.listOfCooperativesByGinningProcess();
	}
	
	public List<Object[]> listOfHeapNameByGinningProcess(){
		return locationDAO.listOfHeapNameByGinningProcess();
	}
	
	public Double  findTxnStockByGinningId(long id){
		return locationDAO.findTxnStockByGinningId(id);
	}
	
	@Override
	public List<State> listOfStatesByBranch(String selectedBranch) {
		// TODO Auto-generated method stub
		return locationDAO.listOfStatesByBranch(selectedBranch);
	}
	
	public List<Object[]> findStockByGinningId(long id){
		return locationDAO.findStockByGinningId(id);
	}

	@Override
	public Warehouse findWarehouseByCodeAndType(String code, int type) {
		return locationDAO.findWarehouseByCodeAndType(code, type);
	}
	
	@Override
	public List<Village> listVillageByBranch(String selectedBranch,Long selectedState) {
		return locationDAO.listVillageByBranch(selectedBranch,selectedState);
	}

	@Override
	public Integer findFacilitiesCount() {
		return locationDAO.findFacilitiesCount();
	}

	@Override
	public Warehouse findWarehouseByCodeAndType(String spinnerCode, int type, String tenantId) {
		return locationDAO.findWarehouseByCodeAndType(spinnerCode, type, tenantId);
	}

	@Override
	public List<Object[]> findCityNames() {
		// TODO Auto-generated method stub
		return locationDAO.findCityNames();
	}

	@Override
	public List<Object[]> listVillageIdAndName() {
		// TODO Auto-generated method stub
		return locationDAO.listVillageIdAndName();
	}

	
	@Override
	public Village findVillageAndCityByVillName(String otherVi,Long cityId) {
		// TODO Auto-generated method stub
		return locationDAO.findVillageAndCityByVillName(otherVi,cityId);
	}

	@Override
	public List<Object[]> listStates() {
		// TODO Auto-generated method stub
		 return locationDAO.listStates();
	}

	@Override
	public List<Locality> listOfLocalities() {
		// TODO Auto-generated method stub
		return locationDAO.listOfLocalities();
	}

	@Override
	public List<Object[]> listLocalities() {
		// TODO Auto-generated method stub
		return locationDAO.listLocalities();
	}
	
	public List<LocationHistory> listDeviceLocationHistoryByAgentIdWithAccuracy(String agentId, Date startDate, Date endDate, Long accuracy ) {

		return locationDAO.listDeviceLocationHistoryByAgentIdWithAccuracy(agentId, startDate, endDate, accuracy);
	}
	
	public List<Object[]> listWarehousesWithoutBranch(){
		return locationDAO.listWarehousesWithoutBranch();
	}
	@Override
	public Warehouse findWarehouseByIdWithoutBranch(long id){
		
		return locationDAO.findWarehouseByIdWithoutBranch(id);
	}

	@Override
	public List<Object[]> listOfMobileUserByDistribution() {
		// TODO Auto-generated method stub
		return locationDAO.listOfMobileUserByDistribution();
	}
	
	public LocationHistory findLocationHistoryByTxnTimeSerialNoBranch(Date date,String serialNo,String branch){
		return locationDAO.findLocationHistoryByTxnTimeSerialNoBranch(date,serialNo,branch);
	}
	
	@Override
	public List<LocationHistoryDetail> listDeviceLocationHistoryDetailByAgentId(String agentId, Date startDate, Date endDate,
			String tenantId){
		return locationDAO.listDeviceLocationHistoryDetailByAgentId(agentId,startDate,endDate,tenantId);
	}
	
	public List<LocationHistoryDetail> findLocationHistoryDetailById(long id){
		return locationDAO.findLocationHistoryDetailById(id);
	}

	@Override
	public List<WarehouseStorageMap> listWarehouseStorageMapByWarehouseID(Long warehouseId) {
		// TODO Auto-generated method stub
		return locationDAO.listWarehouseStorageMapByWarehouseID(warehouseId);
	}

	@Override
	public WarehouseStorageMap findMaxBinHoldByWarehouseIdAndColdStorageName(long id, String coldStorageName) {
		// TODO Auto-generated method stub
		return locationDAO.findMaxBinHoldByWarehouseIdAndColdStorageName(id, coldStorageName);
	}

	@Override
	public List<Object[]> listWarehouseIdAndName() {
		// TODO Auto-generated method stub
		return locationDAO.listWarehouseIdAndName();
	}

	@Override
	public Warehouse findWarehouseById(long id, String tenantId) {
		return locationDAO.findWarehouseById(id, tenantId);
	}

	@Override
	public Warehouse findGinnerByGinningId(Long ginningId) {
		return locationDAO.findGinnerByGinningId(ginningId);
	}

	@Override
	public List<Object[]> listOfGinningFromBaleGeneration(String seasonsCode) {
		return locationDAO.listOfGinningFromBaleGeneration(seasonsCode);
	}

	@Override
	public List<Object[]> listOfSpinning() {
		return locationDAO.listOfSpinning();
	}

	@Override
	public List<Object[]> listOfHeapByGinningFromBaleGeneration(String selectedGinning,String season) {
		return locationDAO.listOfHeapByGinningFromBaleGeneration(selectedGinning,season);
	}
	
   public Warehouse findGinningById(long id){
	   return locationDAO.findGinningById(id);
   }
	
	public Warehouse findSpinningById(long id){
		 return locationDAO.findSpinningById(id);
	}
	@Override
	public Warehouse findWarehouseByIdAndTenantId(long id, String tenantId){
		return locationDAO.findWarehouseByIdAndTenantId(id,tenantId);
	}

	@Override
	public Village findVillageByIdAndTenant(long id, String tenantId) {
		return locationDAO.findVillageByIdAndTenant(id, tenantId);
	}

	@Override
	public List<Warehouse> listWarehouseByWarehouseIds(String idsArr) {
		return locationDAO.listWarehouseByWarehouseIds(idsArr);
	}
	
	@Override
	public List<Country> listCountriesWithAll(){
		return locationDAO.listCountriesWithAll();
		
	}

	@Override
	public List<Object[]> findLocalityByBranch(String Branch) {
		// TODO Auto-generated method stub
		return locationDAO.findLocalityByBranch(Branch);
	}
	
	@Override
	public void updateLocHistory(Long object, String lat, String lon) {
		locationDAO.updateLocHistory( object,  lat,  lon);
	}
	
}
