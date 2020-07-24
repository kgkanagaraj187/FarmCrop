/*
 * LocationDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.FarmerService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;

@Component
public class LocationDownloadAdapter implements ITxnAdapter {

	private static Logger LOGGER = Logger.getLogger(LocationDownloadAdapter.class);
	@Autowired
	private ILocationService locationService;

	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ICertificationService certificationService;
	private long countryRevisionNo;
	private long stateRevisionNo;
	private long districtRevisionNo;
	private long cityRevisionNo;
	private long panchayatRevisionNo;
	private long villageRevisionNo;
	private boolean initialDownload;
	private Integer gmEnable = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
        
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        
        /** VALIDATING REQUEST DATA **/
        String tenantId= head.getTenantId();
        
        /** GET REQUEST DATA **/
        LOGGER.info("---------- LocationDownload Start ----------");

        initializeRevisionNumbers();
        ESESystem preferences = preferncesService.findPrefernceById("1");
		 gmEnable = 0;
		if (!StringUtil.isEmpty(preferences)) {
			gmEnable = Integer.valueOf(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		}

	
        String revisionNoString = (String) reqData.get(TxnEnrollmentProperties.LOCATION_DOWNLOAD_REVISION_NO);
        if (!StringUtil.isEmpty(revisionNoString) && revisionNoString.contains("|")) {
            String[] revisionNoArray = revisionNoString.split("\\|");
            if (revisionNoArray.length == 6) {
                try {
                    countryRevisionNo = Long.valueOf(revisionNoArray[0]);
                    stateRevisionNo = Long.valueOf(revisionNoArray[1]);
                    districtRevisionNo = Long.valueOf(revisionNoArray[2]);
                    cityRevisionNo = Long.valueOf(revisionNoArray[3]);
                    panchayatRevisionNo = Long.valueOf(revisionNoArray[4]);
                    villageRevisionNo = Long.valueOf(revisionNoArray[5]);
                } catch (Exception e) {
                    LOGGER.info("Revision No Conversion Error:" + e.getMessage());
                    initializeRevisionNumbers();
                }
            }
            if (countryRevisionNo + stateRevisionNo + districtRevisionNo + cityRevisionNo
                    + panchayatRevisionNo + villageRevisionNo > 0)
                initialDownload = false;
        }

        Collection countryCollection = new Collection();
        Collection stateCollection = new Collection();
        Collection districtCollection = new Collection();
        Collection cityCollection = new Collection();
        Collection panchayatCollection = new Collection();
        Collection villageCollection = new Collection();
        List<String>  codes=new ArrayList<>();
        Map<String,List<LanguagePreferences>> lpMap = new HashMap<>();
        if (initialDownload) {
            List<Country> countries = locationService.listCountriesWithAll();
            codes.addAll(countries.stream().map(u -> u.getCode()).collect(Collectors.toList()));
            codes.addAll(countries.stream().flatMap(u -> u.getStates().stream()).collect(Collectors.toList()).stream().map(pp -> pp.getCode()).collect(Collectors.toList()));
            codes.addAll(countries.stream().flatMap(u -> u.getStates().stream()).collect(Collectors.toList()).stream().flatMap(u -> u.getLocalities().stream()).collect(Collectors.toList()).stream().map(pp -> pp.getCode()).collect(Collectors.toList()));
            codes.addAll(countries.stream().flatMap(u -> u.getStates().stream()).collect(Collectors.toList()).stream().flatMap(u -> u.getLocalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getMunicipalities().stream()).map(pp -> pp.getCode()).collect(Collectors.toList()));
            codes.addAll(countries.stream().flatMap(u -> u.getStates().stream()).collect(Collectors.toList()).stream().flatMap(u -> u.getLocalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getMunicipalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getGramPanchayats().stream()).map(pp -> pp.getCode()).collect(Collectors.toList()));
            codes.addAll(countries.stream().flatMap(u -> u.getStates().stream()).collect(Collectors.toList()).stream().flatMap(u -> u.getLocalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getMunicipalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getVillages().stream()).map(pp -> pp.getCode()).collect(Collectors.toList()));
            codes.addAll(countries.stream().flatMap(u -> u.getStates().stream()).collect(Collectors.toList()).stream().flatMap(u -> u.getLocalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getMunicipalities().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getGramPanchayats().stream()).collect(Collectors.toList()).stream().flatMap(uu -> uu.getVillages().stream()).map(pp -> pp.getCode()).collect(Collectors.toList()));
            if(codes!=null && !codes.isEmpty()){
            List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
            lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
            }
            countryCollection = buildCountriesCollection(countries,tenantId,lpMap);
             } else {
            List<Country> countries = locationService.listCountriesByRevisionNo(countryRevisionNo);
            
            codes.addAll(countries.stream().map(u -> u.getCode()).collect(Collectors.toList()));
            if(codes!=null && !codes.isEmpty()){
            List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
            
            lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
            }
            countryCollection = buildCountriesCollection(countries,tenantId,lpMap);
            
              List<State> states = locationService.listStatesByRevisionNo(stateRevisionNo);
              codes=new ArrayList<>();
              codes.addAll(states.stream().map(u -> u.getCode()).collect(Collectors.toList()));
              if(codes!=null && !codes.isEmpty()){
                  List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
                  
                  lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
                  }
            stateCollection = buildSatatesCollection(states,tenantId,lpMap);

            List<Locality> districts = locationService
                    .listLocalitiesByRevisionNo(districtRevisionNo);
            
            codes=new ArrayList<>();
            codes.addAll(districts.stream().map(u -> u.getCode()).collect(Collectors.toList()));
            if(codes!=null && !codes.isEmpty()){
                List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
                
                lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
                }
             
             
            districtCollection = buildDistrictsCollection(districts,tenantId,lpMap);

            List<Municipality> cities = locationService
                    .listMunicipalitiesByRevisionNo(cityRevisionNo);
            
            codes=new ArrayList<>();
            codes.addAll(cities.stream().map(u -> u.getCode()).collect(Collectors.toList()));
            if(codes!=null && !codes.isEmpty()){
                List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
                
                lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
                }
             
             
            cityCollection = buildCitiesCollection(cities,tenantId,lpMap);
            
            List<GramPanchayat> panchayats = locationService
                    .listGramPanchayatsByRevisionNo(panchayatRevisionNo);
            
            codes=new ArrayList<>();
            codes.addAll(panchayats.stream().map(u -> u.getCode()).collect(Collectors.toList()));
            if(codes!=null && !codes.isEmpty()){
                List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
                
                lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
                }
            panchayatCollection = buildPanchayatsCollection(panchayats,tenantId,lpMap);

            List<Village> villages = locationService.listVillagesByRevisionNo(villageRevisionNo);
            codes=new ArrayList<>();
            codes.addAll(villages.stream().map(u -> u.getCode()).collect(Collectors.toList()));
            if(codes!=null && !codes.isEmpty()){
                List<LanguagePreferences> lpLis = certificationService.listLangPrefByCodes(codes);
                
                lpMap = lpLis.stream().collect(Collectors.groupingBy(u -> u.getCode()));
                }
            villageCollection = buildVillagesCollection(villages,lpMap);
        }

        revisionNoString = countryRevisionNo + "|" + stateRevisionNo + "|" + districtRevisionNo
                + "|" + cityRevisionNo + "|" + panchayatRevisionNo + "|" + villageRevisionNo;

        Map resp = new LinkedHashMap();
        resp.put(TransactionProperties.COUNTRY_LIST, countryCollection);
        if (!initialDownload) {
            resp.put(TransactionProperties.STATE_LIST, stateCollection);
            resp.put(TransactionProperties.DISTRICT_LIST, districtCollection);
            resp.put(TransactionProperties.CITY_LIST, cityCollection);
            resp.put(TransactionProperties.GRAM_PANCHAYAT_LIST, panchayatCollection);
            resp.put(TransactionProperties.VILLAGE_LIST, villageCollection);
        }
        resp.put(TxnEnrollmentProperties.LOCATION_DOWNLOAD_REVISION_NO, revisionNoString);

        LOGGER.info("----------LocationDownload End ----------");
        return resp;
    }

	/**
	 * Initialize revision numbers.
	 */
	private void initializeRevisionNumbers() {

		countryRevisionNo = 0;
		stateRevisionNo = 0;
		districtRevisionNo = 0;
		cityRevisionNo = 0;
		panchayatRevisionNo = 0;
		villageRevisionNo = 0;
		initialDownload = true;
	}

	/**
	 * Builds the countries collection.
	 * 
	 * @param countriesList
	 *            the countries list
	 * @param tenantId
	 * @param lpMap2
	 * @return the collection
	 */
	private Collection buildCountriesCollection(List<Country> countriesList, String tenantId,
			Map<String, List<LanguagePreferences>> lpMap) {

		Collection countryCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> countryObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
		if (!ObjectUtil.isListEmpty(countriesList)) {
			for (Country country : countriesList) {
				com.sourcetrace.eses.txn.schema.Object countryObj = new com.sourcetrace.eses.txn.schema.Object();
				List<Data> countryDataList = new ArrayList<Data>();

				Data countryCodeData = new Data();
				countryCodeData.setKey(TransactionProperties.COUNTRY_CODE);
				countryCodeData.setValue(country.getCode());
				countryDataList.add(countryCodeData);

				Data countryNameData = new Data();
				countryNameData.setKey(TransactionProperties.COUNTRY_NAME);
				countryNameData.setValue(country.getName());
				countryDataList.add(countryNameData);

				if (countriesList != null && !ObjectUtil.isEmpty(countriesList) && !ObjectUtil.isEmpty(lpMap)) {
					Data lang = new Data();
					lang.setKey(TransactionProperties.LANG_LIST);
					lang.setCollectionValue(getCollection(lpMap.get(country.getCode())));
					countryDataList.add(lang);
				}

				if (initialDownload) {
					Data statesData = new Data();
					statesData.setKey(TransactionProperties.STATE_LIST);
					statesData.setCollectionValue(buildSatatesCollection(!ObjectUtil.isListEmpty(country.getStates())
							? new LinkedList<State>(country.getStates()) : null, tenantId, lpMap));
					countryDataList.add(statesData);

					countryRevisionNo = Math.max(country.getRevisionNo(), countryRevisionNo);
				}

				countryObj.setData(countryDataList);
				countryObjectList.add(countryObj);
			}
			if (!initialDownload)
				countryRevisionNo = countriesList.get(0).getRevisionNo();
		}
		countryCollection.setObject(countryObjectList);
		return countryCollection;
	}

	/**
	 * Builds the satates collection.
	 * 
	 * @param statesList
	 *            the states list
	 * @param tenantId
	 * @return the collection
	 */
	private Collection buildSatatesCollection(List<State> statesList, String tenantId,
			Map<String, List<LanguagePreferences>> lpMap) {

		Collection stateCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> stateObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

		if (!ObjectUtil.isListEmpty(statesList)) {
			for (State state : statesList) {
				com.sourcetrace.eses.txn.schema.Object stateObj = new com.sourcetrace.eses.txn.schema.Object();
				List<Data> stateDataList = new ArrayList<Data>();

				Data stateCodeData = new Data();
				stateCodeData.setKey(TransactionProperties.STATE_CODE);
				stateCodeData.setValue(state.getCode());
				stateDataList.add(stateCodeData);

				Data stateNameData = new Data();
				stateNameData.setKey(TransactionProperties.STATE_NAME);
				stateNameData.setValue(state.getName());
				stateDataList.add(stateNameData);

				if (statesList != null && !ObjectUtil.isEmpty(statesList) && !ObjectUtil.isEmpty(lpMap)) {
					Data lang = new Data();
					lang.setKey(TransactionProperties.LANG_LIST);
					lang.setCollectionValue(getCollection(lpMap.get(state.getCode())));
					stateDataList.add(lang);
				}

				if (initialDownload) {
					Data districtsData = new Data();
					districtsData.setKey(TransactionProperties.DISTRICT_LIST);
					districtsData
							.setCollectionValue(buildDistrictsCollection(!ObjectUtil.isListEmpty(state.getLocalities())
									? new LinkedList<Locality>(state.getLocalities()) : null, tenantId, lpMap));
					stateDataList.add(districtsData);

					stateRevisionNo = Math.max(state.getRevisionNo(), stateRevisionNo);
				} else {
					Data stateCountryCodeData = new Data();
					stateCountryCodeData.setKey(TransactionProperties.COUNTRY_CODE);
					stateCountryCodeData.setValue(state.getCountry().getCode());
					stateDataList.add(stateCountryCodeData);
				}

				stateObj.setData(stateDataList);
				stateObjectList.add(stateObj);
			}
			if (!initialDownload)
				stateRevisionNo = statesList.get(0).getRevisionNo();
		}
		stateCollection.setObject(stateObjectList);
		return stateCollection;

	}

	/**
	 * Builds the districts collection.
	 * 
	 * @param districtsList
	 *            the districts list
	 * @param tenantId
	 * @return the collection
	 */
	private Collection buildDistrictsCollection(List<Locality> districtsList, String tenantId,
			Map<String, List<LanguagePreferences>> lpMap) {
		Collection districtCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> districtObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

		if (!ObjectUtil.isListEmpty(districtsList)) {
			for (Locality district : districtsList) {
				com.sourcetrace.eses.txn.schema.Object districtObj = new com.sourcetrace.eses.txn.schema.Object();
				List<Data> districtDataList = new ArrayList<Data>();

				Data districtCode = new Data();
				districtCode.setKey(TransactionProperties.DISTRICT_CODE);
				districtCode.setValue(district.getCode());
				districtDataList.add(districtCode);

				Data districtName = new Data();
				districtName.setKey(TransactionProperties.DISTRICT_NAME);
				districtName.setValue(district.getName());
				districtDataList.add(districtName);

				if (districtsList != null && !ObjectUtil.isEmpty(districtsList) && !ObjectUtil.isEmpty(lpMap)) {
					Data lang = new Data();
					lang.setKey(TransactionProperties.LANG_LIST);
					lang.setCollectionValue(getCollection(lpMap.get(district.getCode())));
					districtDataList.add(lang);
				}

				if (initialDownload) {
					Data cityData = new Data();
					cityData.setKey(TransactionProperties.CITY_LIST);
					cityData.setCollectionValue(buildCitiesCollection(
							!ObjectUtil.isListEmpty(district.getMunicipalities())
									? new LinkedList<Municipality>(district.getMunicipalities()) : null,
							tenantId, lpMap));
					districtDataList.add(cityData);

					districtRevisionNo = Math.max(district.getRevisionNo(), districtRevisionNo);
				} else {
					Data stateCode = new Data();
					stateCode.setKey(TransactionProperties.STATE_CODE);
					stateCode.setValue(district.getState().getCode());
					districtDataList.add(stateCode);
				}

				districtObj.setData(districtDataList);
				districtObjectList.add(districtObj);
			}
			if (!initialDownload)
				districtRevisionNo = districtsList.get(0).getRevisionNo();
		}
		districtCollection.setObject(districtObjectList);
		return districtCollection;
	}

	/**
	 * Builds the cities collection.
	 * 
	 * @param citiesList
	 *            the cities list
	 * @param tenantId
	 * @return the collection
	 */
	private Collection buildCitiesCollection(List<Municipality> citiesList, String tenantId,
			Map<String, List<LanguagePreferences>> lpMap) {
		Collection cityCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> cityObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
		if (!ObjectUtil.isListEmpty(citiesList)) {
			for (Municipality city : citiesList) {
				com.sourcetrace.eses.txn.schema.Object cityObj = new com.sourcetrace.eses.txn.schema.Object();
				List<Data> cityDataList = new ArrayList<Data>();

				Data cityCode = new Data();
				cityCode.setKey(TransactionProperties.CITY_CODE);
				cityCode.setValue(city.getCode());
				cityDataList.add(cityCode);

				Data cityName = new Data();
				cityName.setKey(TransactionProperties.CITY_NAME);
				cityName.setValue(city.getName());
				cityDataList.add(cityName);

				if (citiesList != null && !ObjectUtil.isEmpty(citiesList) && !ObjectUtil.isEmpty(lpMap)) {
					Data lang = new Data();
					lang.setKey(TransactionProperties.LANG_LIST);
					lang.setCollectionValue(getCollection(lpMap.get(city.getCode())));
					cityDataList.add(lang);
				}

				if (initialDownload) {

					if (gmEnable == 1) {
						Data gmData = new Data();
						gmData.setKey(TransactionProperties.GRAM_PANCHAYAT_LIST);
						gmData.setCollectionValue(buildPanchayatsCollection(
								!ObjectUtil.isListEmpty(city.getGramPanchayats())
										? new LinkedList<GramPanchayat>(city.getGramPanchayats()) : null,
								tenantId, lpMap));
						cityDataList.add(gmData);

					} else {
						Data villagesData = new Data();
						villagesData.setKey(TransactionProperties.VILLAGE_LIST);
						villagesData
								.setCollectionValue(buildVillagesCollection(!ObjectUtil.isListEmpty(city.getVillages())
										? new LinkedList<Village>(city.getVillages()) : null, lpMap));
						cityDataList.add(villagesData);
					}

					cityRevisionNo = Math.max(city.getRevisionNo(), cityRevisionNo);
				} else {
					Data districtCityCode = new Data();
					districtCityCode.setKey(TransactionProperties.DISTRICT_CODE);
					districtCityCode.setValue(city.getLocality().getCode());
					cityDataList.add(districtCityCode);
				}

				cityObj.setData(cityDataList);
				cityObjectList.add(cityObj);
			}
			if (!initialDownload)
				cityRevisionNo = citiesList.get(0).getRevisionNo();
		}
		cityCollection.setObject(cityObjectList);
		return cityCollection;
	}

	/**
	 * Builds the panchayats collection.
	 * 
	 * @param panchayatsList
	 *            the panchayats list
	 * @return the collection
	 */
	private Collection buildPanchayatsCollection(List<GramPanchayat> panchayatsList, String tenantId,
			Map<String, List<LanguagePreferences>> lpMap) {

		Collection panchayatCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> panchayatObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
		if (!ObjectUtil.isListEmpty(panchayatsList)) {
			for (GramPanchayat panchayat : panchayatsList) {
				com.sourcetrace.eses.txn.schema.Object panchayatObj = new com.sourcetrace.eses.txn.schema.Object();
				List<Data> panchayatDataList = new ArrayList<Data>();

				Data panchayatCode = new Data();
				panchayatCode.setKey(TransactionProperties.PANCHAYAT_CODE);
				panchayatCode.setValue(panchayat.getCode());
				panchayatDataList.add(panchayatCode);

				Data panchayatName = new Data();
				panchayatName.setKey(TransactionProperties.PANCHAYAT_NAME);
				panchayatName.setValue(panchayat.getName());
				panchayatDataList.add(panchayatName);

				if (panchayatsList != null && !ObjectUtil.isEmpty(panchayatsList) && !ObjectUtil.isEmpty(lpMap)) {
					Data lang = new Data();
					lang.setKey(TransactionProperties.LANG_LIST);
					lang.setCollectionValue(getCollection(lpMap.get(panchayat.getCode())));
					panchayatDataList.add(lang);
				}

				if (initialDownload) {
					Data villagesData = new Data();
					villagesData.setKey(TransactionProperties.VILLAGE_LIST);
					villagesData
							.setCollectionValue(buildVillagesCollection(!ObjectUtil.isListEmpty(panchayat.getVillages())
									? new LinkedList<Village>(panchayat.getVillages()) : null, lpMap));
					panchayatDataList.add(villagesData);

					panchayatRevisionNo = Math.max(panchayat.getRevisionNo(), panchayatRevisionNo);
				} else {
					Data cityRefCode = new Data();
					cityRefCode.setKey(TransactionProperties.CITY_CODE);
					cityRefCode.setValue(panchayat.getCity().getCode());
					panchayatDataList.add(cityRefCode);
				}

				panchayatObj.setData(panchayatDataList);
				panchayatObjectList.add(panchayatObj);
			}
			if (!initialDownload)
				panchayatRevisionNo = panchayatsList.get(0).getRevisionNo();
		}
		panchayatCollection.setObject(panchayatObjectList);
		return panchayatCollection;
	}

	/**
	 * Builds the villages collection.
	 * 
	 * @param villagesList
	 *            the villages list
	 * @param lpMap
	 * @return the collection
	 */
	private Collection buildVillagesCollection(List<Village> villagesList,
			Map<String, List<LanguagePreferences>> lpMap) {
		Collection villageCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> villageObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

		if (!ObjectUtil.isListEmpty(villagesList)) {
			for (Village village : villagesList) {
				com.sourcetrace.eses.txn.schema.Object villageObj = new com.sourcetrace.eses.txn.schema.Object();
				List<Data> villageDataList = new ArrayList<Data>();

				Data villageCode = new Data();
				villageCode.setKey(TransactionProperties.VILLAGE_CODE);
				villageCode.setValue(village.getCode());
				villageDataList.add(villageCode);

				Data villageName = new Data();
				villageName.setKey(TransactionProperties.VILLAGE_NAME);
				villageName.setValue(village.getName());
				villageDataList.add(villageName);

				if (villagesList != null && !ObjectUtil.isEmpty(villagesList) && !ObjectUtil.isEmpty(lpMap)) {
					Data lang = new Data();
					lang.setKey(TransactionProperties.LANG_LIST);
					lang.setCollectionValue(getCollection(lpMap.get(village.getCode())));
					villageDataList.add(lang);
				}

				if (initialDownload)
					villageRevisionNo = Math.max(village.getRevisionNo(), villageRevisionNo);
				else {
					Data villageCityCode = new Data();
					if (gmEnable == 1) {
						villageCityCode.setKey(TransactionProperties.PANCHAYAT_CODE);
						villageCityCode.setValue(village.getGramPanchayat().getCode());
					} else {
						villageCityCode.setKey(TransactionProperties.CITY_CODE);
						villageCityCode.setValue(village.getCity().getCode());
					}

					villageDataList.add(villageCityCode);
				}

				villageObj.setData(villageDataList);
				villageObjectList.add(villageObj);
			}
			if (!initialDownload)
				villageRevisionNo = villagesList.get(0).getRevisionNo();
		}
		villageCollection.setObject(villageObjectList);
		return villageCollection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	public Collection getCollection(List<LanguagePreferences> lpListObj) {
		Collection langColl = new Collection();
		List<Object> languages = new ArrayList<Object>();
		if (lpListObj != null && !lpListObj.isEmpty()) {
			for (LanguagePreferences lp : lpListObj) {
				Data langCode = new Data();
				langCode.setKey(TransactionProperties.LANGUAGE_CODE);
				langCode.setValue(lp.getLang());

				Data langName = new Data();
				langName.setKey(TransactionProperties.LANGUAGE_NAME);
				langName.setValue(lp.getName());

				List<Data> langDataList = new ArrayList<Data>();
				langDataList.add(langCode);
				langDataList.add(langName);

				Object langMasterObject = new Object();
				langMasterObject.setData(langDataList);
				languages.add(langMasterObject);
				langColl.setObject(languages);

			}
		}

		return langColl;
	}

}
