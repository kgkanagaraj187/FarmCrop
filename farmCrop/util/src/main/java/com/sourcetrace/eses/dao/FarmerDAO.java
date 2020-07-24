/*
\ * FarmerDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewFarmerActivity;
import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.FarmerLocationMapField;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportJoinMap;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.SamithiIcs;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.Contract;
import com.sourcetrace.eses.order.entity.txn.CostFarming;
import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.CultivationDetail;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.EventCalendar;
import com.sourcetrace.eses.order.entity.txn.Forecast;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisory;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisoryDetails;
import com.sourcetrace.eses.order.entity.txn.LoanApplication;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionData;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionSymptom;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.WeatherForeCast;
import com.sourcetrace.eses.service.FarmerService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.umgmt.entity.Entitlement;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.CottonPrice;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.DocumentUpload;
import com.sourcetrace.esesw.entity.profile.DynamicConstants;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.DynamicReportFieldsConfig;
import com.sourcetrace.esesw.entity.profile.DynamicReportTableConfig;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmElement;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.FarmInventory;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.FarmerFamily;
import com.sourcetrace.esesw.entity.profile.FarmerIncomeDetails;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.FarmerSourceIncome;
import com.sourcetrace.esesw.entity.profile.GMO;
import com.sourcetrace.esesw.entity.profile.GinnerQuantitySold;
import com.sourcetrace.esesw.entity.profile.HarvestData;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.InterestCalcHistory;
import com.sourcetrace.esesw.entity.profile.OfflineFarmerEnrollment;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.entity.Agent;

/**
 * The Class FarmerDAO.
 */
@SuppressWarnings("unchecked")
@Repository
@Transactional
public class FarmerDAO extends ESEDAO implements IFarmerDAO {

	protected static String SELECT = "-1";
	protected static String formula = "";
	@Autowired
	ILocationDAO locationDAO;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	public FarmerDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	public Farmer findFarmerByFarmerId(String farmerId) {

		Object[] values = { farmerId, ESETxnStatus.SUCCESS.ordinal() };
		Farmer farmer = (Farmer) find("FROM Farmer fr left join fetch fr.farms WHERE  fr.farmerId = ? AND fr.statusCode = ?", values);
		return farmer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmerById(java
	 * .lang.Long)
	 */
	public Farmer findFarmerById(Long id) {

		return (Farmer) find("FROM Farmer fr WHERE fr.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmById(java.lang
	 * .Long)
	 */
	public Farm findFarmById(Long id) {

		return (Farm) find("FROM Farm fm  WHERE fm.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmByFarmCode(
	 * java.lang.String)
	 */
	public Farm findFarmByFarmCode(String farmCode) {

		return (Farm) find("FROM Farm fm WHERE fm.farmCode = ?", farmCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmByFarmName(
	 * java.lang.String)
	 */
	public Farm findFarmByFarmName(String farmName) {

		return (Farm) find("FROM Farm fm WHERE fm.farmName = ?", farmName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#removeFarmBasedOnFarmer
	 * (long)
	 */
	public void removeFarmByFarmerId(long id) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE Farm fm WHERE fm.farmer.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmByCode(java
	 * .lang.String)
	 */
	public Farm findFarmByCode(String code) {

		return (Farm) find("FROM Farm fm WHERE fm.farmCode  = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#listFarmByFarmerId(
	 * long)
	 */
	public List<Farm> listFarmByFarmerId(long farmerId) {

		return list("FROM Farm fm where fm.farmer.id = ? and fm.status=1 ", farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#isCityMappingexist(
	 * long)
	 */
	public boolean isCityMappingexist(long id) {

		Object[] values = { id, ESETxnStatus.SUCCESS.ordinal() };
		List<Farmer> farmerList = list("FROM Farmer sd WHERE sd.city.id = ? AND sd.statusCode = ?", values);
		if (!ObjectUtil.isListEmpty(farmerList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listFarmerByRevNoAndCity (java.lang.Long, long)
	 */
	public List<Farmer> listFarmerByRevNoAndCity(Long revisionNo, long id) {

		Object[] values = { revisionNo, id, ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"FROM Farmer fr WHERE fr.revisionNo > ? AND fr.village.city.id = ? AND fr.statusCode = ? order by fr.revisionNo DESC",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#isVillageMappingExist
	 * (long)
	 */
	public boolean isVillageMappingExist(long id) {

		Object[] values = { id, ESETxnStatus.SUCCESS.ordinal() };
		List<Farmer> farmerList = list("FROM Farmer farmer WHERE farmer.village.id = ? AND farmer.statusCode = ?",
				values);
		if (!ObjectUtil.isListEmpty(farmerList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmerByFarmerName
	 * (java.lang.String)
	 */
	public Farmer findFarmerByFarmerName(String selectedFarmer) {

		Object[] values = { selectedFarmer, ESETxnStatus.SUCCESS.ordinal() };
		return (Farmer) find("FROM Farmer fr WHERE fr.firstName = ? AND fr.statusCode = ?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmerByVillageCode
	 * (java.lang.String)
	 */
	public List<Farmer> findFarmerByVillageCode(String villageCode) {

		Object[] values = { villageCode, ESETxnStatus.SUCCESS.ordinal() };
		return list("FROM Farmer fr WHERE fr.village.code = ? AND fr.statusCode = ? ORDER BY fr.firstName ", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#listFarmer()
	 */
	public List<Farmer> listFarmer() {

		return list("FROM Farmer f WHERE f.statusCode = ? ORDER BY f.firstName ASC", ESETxnStatus.SUCCESS.ordinal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listOfflineFarmerEnrollmentByStatusCode (int)
	 */
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusCode(int statusCode) {

		return list("From OfflineFarmerEnrollment ofe WHERE ofe.statusCode=?", statusCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listOfflineFarmerEnrollmentByStatusEnrollmentType(int, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusEnrollmentType(int statusCode,
			String txnType) {

		Object[] bindValues = { statusCode, txnType };
		return list("From OfflineFarmerEnrollment ofe WHERE ofe.statusCode=? AND ofe.transactionType=?", bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmPhotoById(java
	 * .lang.Long)
	 */
	public byte[] findFarmPhotoById(Long id) {

		return (byte[]) find("SELECT f.photo FROM Farm f WHERE f.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#listSeasons()
	 */
	@SuppressWarnings("unchecked")
	public List<Season> listSeasons() {

		return list("FROM Season");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findSeasonById(java
	 * .lang.Long)
	 */
	public Season findSeasonById(Long id) {

		return (Season) find("FROM Season s Where s.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#listFarmerByCityId(
	 * long)
	 */
	public List<Farmer> listFarmerByCityId(long id) {

		Object[] values = { id, ESETxnStatus.SUCCESS.ordinal() };
		return (List<Farmer>) list("FROM Farmer f WHERE f.city.id=? AND f.statusCode = ?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#listFarmerByVillageId
	 * (long)
	 */
	public List<Farmer> listFarmerByVillageId(long id) {

		Object[] values = { id, ESETxnStatus.SUCCESS.ordinal() };
		return (List<Farmer>) list("FROM Farmer f WHERE f.village.id=? AND f.statusCode = ?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findFarmerAccountStatuById (java.lang.String)
	 */
	public String findFarmerAccountStatuById(String farmerId) {

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery("SELECT STATUS FROM ese_account WHERE PROFILE_ID='" + farmerId + "'");
		List list = query.list();
		String value = list.get(0).toString();
		session.flush();
		session.close();
		return value;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findFarmerAccountStatuById (java.lang.String)
	 */
	public List<Farmer> listActiveFarmersByVillageCode(String villageCode) {

		Object[] bindValues = { villageCode, Farmer.Status.ACTIVE.ordinal() };
		return list("FROM Farmer f WHERE f.village.code=? AND f.status=?)", bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveFarmerByCityCode (java.lang.String )
	 */
	public List<Farmer> listActiveFarmerByCityCode(String cityCode) {

		Object[] bindValues = { cityCode, Farmer.Status.ACTIVE.ordinal() };
		return list("FROM Farmer f WHERE f.village.city.code=? AND f.status=?)", bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findContractByFarmerIdSeasonCodeProcurementProduct(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public Contract findContractByFarmerIdSeasonCodeProcurementProduct(String farmerId, String seasonCode,
			String procurementProductCode) {

		Object[] bindValues = { farmerId, seasonCode, procurementProductCode };
		return (Contract) find(
				"FROM Contract c WHERE c.farmer.farmerId=? AND c.season.code=? AND c.procurementProduct.code=?",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersByVillageCodeSeasonId (java.lang.String,
	 * java.lang.Long)
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCode(String villageCode, String seasonCode) {

		Object[] bindValues = { villageCode, seasonCode, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(),
				ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"SELECT c.farmer FROM Contract c WHERE c.farmer.village.code=? AND c.season.code=? AND c.account.status=? AND c.farmer.status=? AND c.farmer.statusCode = ? ORDER BY c.farmer.firstName,c.farmer.lastName",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findAccountBySeassonProcurmentProductFarmer (long, long, long)
	 */
	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long seasonId, long procurementProductId,
			long farmerId) {

		// HQL Query procurementProductId based constraints removed for fetching
		// single farmer
		// account
		return (ESEAccount) find("SELECT c.account FROM Contract c WHERE c.farmer.id=?", new Object[] { farmerId });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findContractBySeassonProcurmentProductFarmer (long, long, long)
	 */
	public Contract findContractBySeassonProcurmentProductFarmer(long seasonId, long procurementProductId,
			long farmerId) {

		// Commented HQL Query to fetch records with out procurement product
		// constraints
		// Because Farmer having only one account for single contract (Should
		// not more than on
		// contract)
		/*
		 * return (Contract) find(
		 * "FROM Contract c WHERE c.season.id=? AND c.procurementProduct.id=? AND c.farmer.id=?"
		 * , new Object[] { seasonId, procurementProductId, farmerId });
		 */
		return (Contract) find("FROM Contract c WHERE c.season.id=? AND c.farmer.id=?",
				new Object[] { seasonId, farmerId });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findContractByAccountNo
	 * (java.lang.String)
	 */
	public Contract findContractByAccountNo(String accountNo) {

		return (Contract) find("SELECT c FROM Contract c WHERE c.account.accountNo=?", accountNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * isFarmerContractMappingExist(java.lang. String)
	 */
	public boolean isFarmerContractMappingExist(String farmerId) {

		Long count = (Long) find("SELECT  Count(c) From Contract c WHERE c.farmer.farmerId=?", farmerId);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersBySeasonRevNoAndCity (java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonRevNoAndCity(Long cityId, String seasonCode, Long revisionNo) {

		Object[] values = { cityId, seasonCode, revisionNo, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(),
				ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"SELECT c.farmer FROM Contract c WHERE c.farmer.village.city.id=? AND c.season.code=? AND  c.farmer.revisionNo > ? AND c.account.status=? AND c.farmer.status=?  AND c.farmer.statusCode = ?order by c.farmer.revisionNo DESC",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersBySeasonProcurementProduct(long, long)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProduct(long seasonId, long procurementProductId) {

		return (List<Farmer>) list(
				"SELECT c.farmer FROM Contract c WHERE c.season.id=? AND c.procurementProduct.id=? AND c.account.status=? AND c.farmer.status=? AND c.farmer.statusCode = ? ORDER BY c.farmer.firstName,c.farmer.lastName",
				new Object[] { seasonId, procurementProductId, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(),
						ESETxnStatus.SUCCESS.ordinal() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersBySeasonProcurementProductVillage
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProductVillage(String seasoncode,
			String productCode, String selectedVillage) {

		return (List<Farmer>) list(
				"SELECT c.farmer FROM Contract c WHERE c.season.code=? AND c.procurementProduct.code=? AND c.farmer.village.code=? AND c.account.status=? AND c.farmer.status=? AND c.farmer.statusCode = ? ORDER BY c.farmer.firstName,c.farmer.lastName",
				new Object[] { seasoncode, productCode, selectedVillage, ESEAccount.ACTIVE,
						Farmer.Status.ACTIVE.ordinal(), ESETxnStatus.SUCCESS.ordinal() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public Contract findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode(String farmerId,
			String procurementProductCode, String seasonCode) {

		return (Contract) find(
				"From Contract c Where c.farmer.farmerId=?  AND c.procurementProduct.code=? AND c.season.code!=? AND c.account.balance!=? AND c.account.status=?",
				new Object[] { farmerId, procurementProductCode, seasonCode, 0d, ESEAccount.ACTIVE });

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findActiveContractByFarmerIdNotInSeasonCode (java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public Contract findActiveContractByFarmerIdNotInSeasonCode(String farmerId, String procurementProductCode,
			String seasonCode) {

		return (Contract) find(
				"From Contract c Where c.farmer.farmerId=?  AND c.procurementProduct.code=? AND c.season.code!=? AND c.account.status=? ",
				new Object[] { farmerId, procurementProductCode, seasonCode, ESEAccount.ACTIVE });

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findFarmerCurrentContractStatusById(java.lang.Long, java.lang.Long)
	 */
	public boolean findFarmerCurrentContractStatusById(Long id, Long seasonId) {

		Object[] values = { id, seasonId, ESEAccount.ACTIVE, Contract.Status.ACTIVE.ordinal() };
		Contract contract = (Contract) find(
				"FROM Contract c WHERE c.farmer.id=? AND c.season.id=? AND c.account.status=? AND c.status=?", values);
		return !ObjectUtil.isEmpty(contract);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findSeasonBySeasonCode
	 * (java.lang.String)
	 */
	public Long findSeasonBySeasonCode(String seasonCode) {

		return (Long) find("SELECT s.id FROM Season s WHERE s.code=?", seasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#listFarm()
	 */
	public List<Farm> listFarm() {

		return list(
				"SELECT farm FROM Farmer f INNER JOIN f.farms farm WHERE f.id = farm.farmer.id ORDER BY farm.farmName ASC");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#updateFarmerModifyTime
	 * (long, java.util.Date)
	 */
	public void updateFarmerModifyTime(long id, Date tempDate) {

		String queryString = "UPDATE ese_account SET ese_account.MOD_TIME = '" + tempDate + "' WHERE ese_account.ID = "
				+ id;
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		@SuppressWarnings("unused")
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersBySeasonRevNoAndSamithi(long, java.lang.String,
	 * java.lang.Long)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonRevNoAndSamithi(long id, String seasonCode, Long revisionNo) {

		Object[] values = { id, revisionNo, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(),
				ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"SELECT c.farmer FROM Contract c WHERE c.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=?) AND  c.farmer.revisionNo > ? AND c.account.status=? AND c.farmer.status=?  AND c.farmer.statusCode = ? order by c.farmer.revisionNo DESC",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * removeContractByFarmerId (long)
	 */
	public void removeContractByFarmerId(long id) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE Contract c WHERE c.farmer.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersByVillageCodeSeasonCodeSamithiId
	 * (java.lang.String, java.lang.String, long)
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCodeSamithiId(String villageCode, String seasonCode,
			long samithiId) {

		Object[] bindValues = { villageCode, seasonCode, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(), samithiId,
				ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"SELECT c.farmer FROM Contract c WHERE c.farmer.village.code=? AND c.season.code=? AND c.account.status=? AND c.farmer.status=? AND c.farmer.samithi.id=? AND c.farmer.statusCode = ? ORDER BY c.farmer.firstName,c.farmer.lastName",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode(String villageCode,
			String seasonCode, String samithiCode) {

		Object[] bindValues = { villageCode, seasonCode, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(), samithiCode,
				ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"SELECT c.farmer FROM Contract c WHERE c.farmer.village.code=? AND c.season.code=? AND c.account.status=? AND c.farmer.status=? AND c.farmer.samithi.code=? AND c.farmer.statusCode = ? ORDER BY c.farmer.firstName,c.farmer.lastName",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listFarmerByCityAndVillageId(long, long)
	 */
	public List<Farmer> listFarmerByCityAndVillageId(long cityId, long villageId) {

		Object[] values = { cityId, villageId, ESETxnStatus.SUCCESS.ordinal() };
		List<Farmer> farmer = list(
				"FROM Farmer fr WHERE fr.village.gramPanchayat.city.id = ? AND fr.village.id = ? AND fr.statusCode = ?",
				values);
		return farmer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#listFarmerWithAccount()
	 */
	public List<Object[]> listFarmerWithAccount() {

		Object[] values = { ESEAccount.CONTRACT_ACCOUNT, ESETxnStatus.SUCCESS.ordinal() };
		List<Object[]> farmerList = list(
				"SELECT fr.farmerId, fr.firstName, fr.lastName, ac.balance, ac.distributionBalance FROM Farmer fr,ESEAccount ac WHERE ac.profileId = fr.farmerId AND ac.type = ? AND fr.statusCode = ?",
				values);
		return farmerList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listFarmerByCooperativeCertificateStandardVillage(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public List<Object[]> listFarmerByCooperativeCertificateStandardVillage(String selectedCooperative,
			String selectedCertificateStandard, String selectedVillage) {

		String query = "select f.FARMER_ID,f.FIRST_NAME,v.`NAME` VNAME,f.LATITUDE,f.LONGITUDE,IFNULL(GROUP_CONCAT(DISTINCT cs.`NAME`),'') CS_STD_NAME,IFNULL(co.`NAME`,'') CNAME from farmer f inner join village v on f.VILLAGE_ID=v.ID left join certificate_standard cs on cs.ID = f.CERTIFICATE_STANDARD_ID left join warehouse co on co.ID=(SELECT sm.REF_WAREHOUSE_ID from warehouse sm WHERE sm.ID = f.SAMITHI_ID GROUP BY sm.ID) where IFNULL(co.code,'') like :selectedCooperative AND IFNULL(cs.code,'') like :selectedCertificateStandard AND v.code like :selectedVillage AND f.STATUS_CODE like :statusCode group by f.ID";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("FARMER_ID", StringType.INSTANCE)
				.addScalar("FIRST_NAME", StringType.INSTANCE).addScalar("VNAME", StringType.INSTANCE)
				.addScalar("LATITUDE", StringType.INSTANCE).addScalar("LONGITUDE", StringType.INSTANCE)
				.addScalar("CS_STD_NAME", StringType.INSTANCE).addScalar("CNAME", StringType.INSTANCE)
				.setString("selectedCooperative", !StringUtil.isEmpty(selectedCooperative) ? selectedCooperative : "%%")
				.setString("selectedCertificateStandard",
						!StringUtil.isEmpty(selectedCertificateStandard) ? selectedCertificateStandard : "%%")
				.setString("selectedVillage", !StringUtil.isEmpty(selectedVillage) ? selectedVillage : "%%")
				.setInteger("statusCode", ESETxnStatus.SUCCESS.ordinal()).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findHeadOfFamilyById
	 * (long)
	 */
	public FarmerFamily findHeadOfFamilyById(long id) {

		FarmerFamily farmerFamily = (FarmerFamily) find(
				"FROM FarmerFamily fe WHERE fe.headOfFamily = 1 and fe.farmer.id = ?", id);

		return farmerFamily;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmerFamilyById
	 * (long)
	 */
	public FarmerFamily findFarmerFamilyById(long id) {

		return (FarmerFamily) find("FROM FarmerFamily f WHERE f.id = ?", id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmerFamilyByName
	 * (java.lang.String, long)
	 */
	public FarmerFamily findFarmerFamilyByName(String name, long farmerId) {

		Object[] bind = { name, farmerId };
		FarmerFamily farmerFamily = (FarmerFamily) find("FROM FarmerFamily f WHERE f.name = ? and f.farmer.id=?", bind);
		return farmerFamily;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * removeFarmerEconomyMappingSQL(com.ese.entity .profile.FarmerEconomy)
	 */
	public void removeFarmerEconomyMappingSQL(FarmerEconomy farmerEconomy) {

		String farmerUnmappingQueryString = "UPDATE Farmer SET FARMER_ECONOMY_ID=NULL WHERE ID=:farmerId";
		String farmerEconomyDeletQueryString = "DELETE FROM FARMER_ECONOMY WHERE ID=:farmerEconomyId";
		Query unmapQuery = getSessionFactory().getCurrentSession().createSQLQuery(farmerUnmappingQueryString)
				.setParameter("farmerId", farmerEconomy.getFarmer().getId());
		unmapQuery.executeUpdate();
		Query deleteQuery = getSessionFactory().getCurrentSession().createSQLQuery(farmerEconomyDeletQueryString)
				.setParameter("farmerEconomyId", farmerEconomy.getId());
		deleteQuery.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findHarvestSeasonByCode
	 * (java.lang.String)
	 */
	public HarvestSeason findHarvestSeasonByCode(String code) {

		return (HarvestSeason) find("From HarvestSeason hs where hs.code=?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findHarvestSeasonByName
	 * (java.lang.String)
	 */
	public HarvestSeason findHarvestSeasonByName(String name) {

		return (HarvestSeason) find("From HarvestSeason hs where hs.name=?", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findHarvestSeasonById
	 * (java.lang.Long)
	 */
	public HarvestSeason findHarvestSeasonById(Long id) {

		return (HarvestSeason) find("From HarvestSeason hs where hs.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findHarvestDataById
	 * (java.lang.Long)
	 */
	public HarvestData findHarvestDataById(Long id) {

		return (HarvestData) find("From HarvestData hd where hd.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmInventoryById
	 * (java.lang.String)
	 */
	public FarmInventory findFarmInventoryById(String id) {

		FarmInventory farmInventory = (FarmInventory) find("FROM FarmInventory fg WHERE fg.id = ?", id.toString());
		return farmInventory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmInventoryItem
	 * (long, int)
	 */
	public FarmInventory findFarmInventoryItem(long farmId, int inventoryItem) {

		Object[] bind = { farmId, inventoryItem };
		return (FarmInventory) find("FROM FarmInventory fi WHERE fi.farmer.id=? AND fi.inventoryItem=?", bind);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findAnimalHusbandaryById (java.lang.String)
	 */
	public AnimalHusbandary findAnimalHusbandaryById(String id) {

		AnimalHusbandary animalHusbandary = (AnimalHusbandary) find("FROM AnimalHusbandary ah WHERE ah.id = ?",
				id.toString());
		return animalHusbandary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#isFarmMappedFarmCrops
	 * (long)
	 */
	public boolean isFarmMappedFarmCrops(long id) {

		List<FarmCrops> farmCropsList = list("FROM FarmCrops fc WHERE fc.farm.id = ? and fc.status=1", id);
		if (!ObjectUtil.isListEmpty(farmCropsList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * isFarmMappingAnimalHusbandry(long)
	 */
	public boolean isFarmMappingAnimalHusbandry(long id) {

		List<AnimalHusbandary> animalHusbandaryList = list("FROM AnimalHusbandary ah WHERE ah.farmer.id = ?", id);
		if (!ObjectUtil.isListEmpty(animalHusbandaryList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * isFarmMappingFarmInventory (long)
	 */
	public boolean isFarmMappingFarmInventory(long id) {

		List<FarmInventory> farmInventoryList = list("FROM FarmInventory fi WHERE fi.farmer.id = ?", id);
		if (!ObjectUtil.isListEmpty(farmInventoryList)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#listOfCertifiedFarmers
	 * ()
	 */
	public List<Farmer> listOfCertifiedFarmers() {

		Object bindValues[] = { ESETxnStatus.SUCCESS.ordinal(), Farmer.CERTIFICATION_TYPE_NONE };

		return list("FROM Farmer f WHERE f.statusCode = ? AND f.certificationType!=? ORDER BY f.firstName ASC",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listFarmerWithAccountByRevisionDate(java .util.Date)
	 */
	public List<Object[]> listFarmerWithAccountByRevisionDate(Date revisionDate) {

		String query = "SELECT " + "farmer.FARMER_ID AS FARMER_ID, "
				+ "farmer.FIRST_NAME AS FIRST_NAME, farmer.LAST_NAME AS LAST_NAME, "
				+ "eseaccount.BALANCE AS BALANCE, eseaccount.DIST_BALANCE AS DIST_BALANCE, "
				+ "eseaccount.MOD_TIME AS MOD_TIME FROM FARMER farmer, "
				+ "ESE_ACCOUNT eseaccount WHERE eseaccount.PROFILE_ID = farmer.FARMER_ID " + "AND eseaccount.TYPEE = '"
				+ ESEAccount.CONTRACT_ACCOUNT + "' " + "AND farmer.STATUS_CODE = " + ESETxnStatus.SUCCESS.ordinal()
				+ " ";
		if (!ObjectUtil.isEmpty(revisionDate)) {
			try {
				query += "AND eseaccount.MOD_TIME>'"
						+ DateUtil.convertDateToString(revisionDate, DateUtil.DATABASE_DATE_TIME) + "' ";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		query += "ORDER BY eseaccount.MOD_TIME DESC";
		Session session = getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		List list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findActiveContractFarmersLatestRevisionNoBySeasonAndSamithi(long,
	 * java.lang.String)
	 */
	public Long findActiveContractFarmersLatestRevisionNoByAgentAndSeason(long agentId, String seasonCode) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT Max(COALESCE(f.revisionNo,0)) FROM Farmer f WHERE f.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id)   AND f.statusCode in  (:status1) ");
		query.setParameter("id", agentId);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List list = query.list();
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0))) {
			return (Long) list.get(0);
		}
		return 0l;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listActiveContractFarmersAccountByAgentAndSeason(long, java.lang.String,
	 * java.util.Date)
	 */
	public List<Object[]> listActiveContractFarmersAccountByAgentAndSeason(long agentId, String seasonCode,
			Date revisionDate) {

		Object[] values = new Object[] { agentId, seasonCode, ESEAccount.CONTRACT_ACCOUNT, ESEAccount.ACTIVE,
				Farmer.Status.ACTIVE.ordinal(), ESETxnStatus.SUCCESS.ordinal() };

		String revisionQuery = " ";

		if (!ObjectUtil.isEmpty(revisionDate)) {
			values = new Object[] { agentId, seasonCode, ESEAccount.CONTRACT_ACCOUNT, ESEAccount.ACTIVE,
					Farmer.Status.ACTIVE.ordinal(), ESETxnStatus.SUCCESS.ordinal(), revisionDate };
			revisionQuery = "AND c.account.updateTime>? ";
		}
		List<Object[]> list = list(
				"SELECT c.farmer.farmerId,c.farmer.firstName,c.farmer.lastName,c.account.creditBalance,c.account.updateTime,c.account.cashBalance FROM Contract c WHERE c.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=?) AND c.season.code=?  AND c.account.type=? AND c.account.status=? AND c.farmer.status=?  AND c.farmer.statusCode = ?  "
						// "SELECT
						// c.farmer.farmerId,c.farmer.firstName,c.farmer.lastName,c.account.balance,c.account.distributionBalance,c.account.updateTime
						// FROM Contract c WHERE c.farmer.samithi.id in (SELECT
						// s.id FROM Agent a
						// INNER JOIN a.wareHouses s WHERE a.id=?) AND
						// c.season.code=? AND
						// c.account.type=? AND c.account.status=? AND
						// c.farmer.status=? AND
						// c.farmer.statusCode = ? "
						+ revisionQuery + " order by c.account.updateTime DESC ",
				values);
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findInterestCalcConsolidatedByfarmerProfileId (java.lang.String)
	 */
	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileId(String farmerProfileId) {

		// TODO Auto-generated method stub
		return (InterestCalcConsolidated) find("FROM InterestCalcConsolidated icc WHERE icc.farmerProfileId = ?  ",
				farmerProfileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * updateInterestCalcConsolidated(com.ese.
	 * entity.txn.mfi.InterestCalcConsolidated)
	 */
	public void updateInterestCalcConsolidated(InterestCalcConsolidated interestCalcConsolidated) {

		// TODO Auto-generated method stub
		update(interestCalcConsolidated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * listInterestCalcHistoryByDate(java.lang .String, java.util.Date)
	 */
	public List<InterestCalcHistory> listInterestCalcHistoryByDate(String formerId, Date date) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(InterestCalcHistory.class);
		criteria.add(Restrictions.eq("farmerProfileId", formerId));
		Date minDate = DateUtil.getDateWithoutTime(date);
		Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
		criteria.add(Restrictions.ge("calcDate", minDate));
		criteria.add(Restrictions.lt("calcDate", maxDate));
		List list = criteria.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#updateFarm(com.ese.
	 * entity.profile.Farm)
	 */
	public void updateFarm(Farm farm) {

		// TODO Auto-generated method stub
		System.out.println("farm " + farm.getId());
		Session session = getSessionFactory().openSession();

		session.update(farm);

		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#updateFarmer(com.ese.
	 * entity.profile.Farmer)
	 */
	public void updateFarmer(Farmer farmer) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		System.out.println("farmer " + farmer.getId());
		Transaction tr = session.beginTransaction();
		session.update(farmer);
		tr.commit();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmByfarmId(java.
	 * lang.String)
	 */
	public Farm findFarmByfarmId(String farmId) {

		Farm farm = (Farm) find("FROM Farm f WHERE  f.farmCode=?", farmId);
		return farm;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * removeUnmappedFarmCropObject()
	 */
	public void removeUnmappedFarmCropObject() {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE FarmCrops f WHERE f.farm is null");
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#updateFarmerRevisionNo(
	 * java.lang.Long, java.lang.Long)
	 */
	public void updateFarmerRevisionNo(Long farmerId, Long revisionNo) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE Farmer fa SET fa.revisionNo=:FARMER_REV_NO WHERE fa.id=:ID");
		query.setParameter("FARMER_REV_NO", revisionNo);
		query.setParameter("ID", farmerId);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#
	 * findFarmByFarmNameAndFarmerId(java.lang .String, java.lang.Long)
	 */
	public Farm findFarmByFarmNameAndFarmerId(String farmName, Long id) {

		Object[] values = { farmName, id };
		return (Farm) find("FROM Farm fm WHERE fm.farmName = ? AND fm.farmer.id=? AND fm.status<>2", values);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#listOfFarmersCount()
	 */
	public long listOfFarmersCount() {

		long totalFarmers = 0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM FARMER WHERE SAMITHI_ID IS NOT NULL AND VILLAGE_ID IS NOT NULL AND STATUS_CODE = '"
				+ ESETxnStatus.SUCCESS.ordinal() + "'";
		Query query = sessions.createSQLQuery(queryString);

		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalFarmers = count;
		}
		sessions.flush();
		sessions.close();
		return totalFarmers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmerDAO#deleteelemetbyId(java.
	 * lang.Long)
	 */
	public void deleteelemetbyId(Long fileid) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE Farm fr SET fr.photo=NULL,fr.farmImageFileName=NULL WHERE fr.id=:id");

		query.setParameter("id", fileid);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmerDAO#findFarmerList()
	 */
	public List<Farmer> findFarmerList() {

		return list("FROM Farmer");
	}

	public byte[] findfarmerVerificationFarmerVoiceById(Long id) {

		return (byte[]) find("SELECT f.audio FROM Farm f where f.id=?", id);
	}

	public List<HarvestSeason> listHarvestSeasons() {

		return list("FROM HarvestSeason");
	}

	public Farm findFarmByfarmId(Long farmId) {

		Farm farm = (Farm) find("FROM Farm f WHERE  f.id=?", farmId);
		return farm;

	}

	public List<CropHarvest> findCropHarvestByFarmCode(String farmCode) {

		return list("FROM CropHarvest ch WHERE ch.farmCode=?", farmCode);
	}

	public List<CropSupply> findCropSupplyByFarmCode(String farmCode) {

		return list("FROM CropSupply cs INNER JOIN FETCH cs.cropSupplyDetails csd WHERE cs.farmCode=?", farmCode);
	}

	public List<FarmCatalogue> listFarmAnimalBasedOnType() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cat WHERE cat.typez='2' AND cat.status=?", values);
	}

	public FarmCrops findFarmCropsByFarmCode(Long farmId) {

		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.farm.id = ?", farmId);
	}

	public FarmDetailedInfo findTotalLandHoldingById(Long id) {

		FarmDetailedInfo farmDetailedInfo = (FarmDetailedInfo) find("FROM FarmDetailedInfo fdi WHERE fdi.id = ?", id);
		return farmDetailedInfo;
	}

	public List<String[]> listOfFarmerCode() {

		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT farmer_id from cultivation group by farmer_id";
		Query query = sessions.createSQLQuery(queryString);

		List<String[]> array = query.list();

		sessions.flush();
		sessions.close();
		return array;
	}

	public List<String[]> listOfFarmerName() {

		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT farmer_Name from cultivation group by farmer_Name ";
		Query query = sessions.createSQLQuery(queryString);

		List<String[]> array = query.list();

		sessions.flush();
		sessions.close();
		return array;
	}

	public FarmCrops findCurrentSeasonCrop(Long farmId) {

		FarmCrops farmCrop = (FarmCrops) find("FROM FarmCrops fc WHERE  fc.farm.id=? AND fc.cropSeason.currentSeason=1",
				farmId);
		return farmCrop;
	}

	public List<Cultivation> findCostOfCultivationsByFarmerCode(String farmerId, String farmCode) {

		Object[] values = { farmerId, farmCode };
		// TODO Auto-generated method stub
		return list("FROM Cultivation c WHERE farmerId =? and farmId=? ORDER BY c.farmId  ASC", values);
	}

	public List<CultivationDetail> findCultivationDetailsByCultivationId(long id) {

		// TODO Auto-generated method stub
		return list("FROM CultivationDetail cd WHERE cd.cultivation.id=?", id);
	}

	public List<Object[]> findCultivationCost(String farmerId, String farmCode) {

		// TODO Auto-generated method stub
		Object[] values = { farmerId, farmCode };
		return list(
				"SELECT sum(c.totalCoc) as TotalCoc,sum(c.interCropIncome) as InterIncome,sum(c.agriIncome) as agriIncome,"
						+ "sum(c.otherSourcesIncome) as otherSourcesIncome,sum(c.cottonQty) as CottonQty,sum(c.unitSalePrice) as UnitSalePrice,sum(c.saleCottonIncome) as SaleCottonIncome FROM  Cultivation c where c.farmerId=? and c.farmId=?",
				values);
	}

	public Object findCottonIncomeByFarmerCode(String farmerId, String farmCode) {

		// TODO Auto-generated method stub
		Object[] values = { farmerId, farmCode };
		return find(
				"SELECT sum(cd.subTotal) from CropHarvestDetails cd Inner join cd.cropHarvest ch WHERE ch.farmerId=? and ch.farmCode=?",
				values);
	}

	public Farmer findFarmerByFarmerCode(String farmerCode) {

		Object[] values = { farmerCode, ESETxnStatus.SUCCESS.ordinal() };
		Farmer farmer = (Farmer) find("FROM Farmer fr WHERE  fr.farmerId = ? AND fr.statusCode = ?", values);
		return farmer;
	}

	public List<String[]> listFarmerName() {

		// TODO Auto-generated method stub
		return list("SELECT firstName from Farmer ORDER BY firstName ASC");

	}

	public List<String[]> listFatherName() {

		// TODO Auto-generated method stub
		return list("SELECT lastName from Farmer where lastName!='' and lastName!=null order by lastName asc");

	}

	public List<String[]> listFarmerCode() {

		// TODO Auto-generated method stub
		return list("SELECT farmerCode from Farmer where farmerCode!='' ORDER BY farmerCode ASC");
	}

	public double findCottonIncomeByFarmerCodeAndFarmCode(String farmerId, String farmCode) {

		// TODO Auto-generated method stub
		double array = 0.0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT (SELECT SUM(SUB_TOTAL) FROM crop_harvest_details WHERE CROP_HARVEST_ID = ch.id)  FROM crop_harvest ch where FARMER_ID="
				+ farmerId + " and FARM_CODE=" + farmCode + "";
		Query query = sessions.createSQLQuery(queryString);
		if (!StringUtil.isEmpty(query.list())) {
			array = (Double) query.list().get(0);
		}
		sessions.flush();
		sessions.close();
		return array;
	}

	public List<AnimalHusbandary> listAnimalHusbandaryList(Long farmId) {

		return list("FROM AnimalHusbandary ah WHERE ah.farmer.id=?", farmId);
	}

	public void updateAnimalInventory(AnimalHusbandary animalHusbandary) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"UPDATE AnimalHusbandary ah SET ah.animalCount=:ANIMALHUSBANDARY_ANIMAL_COUNT WHERE ah.id=:ID");
		query.setParameter("ANIMALHUSBANDARY_ANIMAL_COUNT", animalHusbandary.getAnimalCount());
		query.setParameter("ID", animalHusbandary.getId());
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	public List<FarmCatalogue> listFarmEquipmentBasedOnType() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cat WHERE cat.typez='1' AND cat.status=?", values);
	}

	public void updateFarmInventory(FarmInventory fm) {

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("UPDATE FarmInventory ah SET ah.itemCount=:ANIMALHUSBANDARY_ANIMAL_COUNT WHERE ah.id=:ID");
		query.setParameter("ANIMALHUSBANDARY_ANIMAL_COUNT", fm.getItemCount());
		query.setParameter("ID", fm.getId());
		int result = query.executeUpdate();

		session.flush();
		session.close();
	}

	public FarmInventory findFarmInventoryItem(long farmId, String inventoryItem) {

		Object[] bind = { farmId, inventoryItem };
		return (FarmInventory) find("FROM FarmInventory fi WHERE fi.farmer.id=? AND fi.inventoryItem.code=?", bind);
	}

	public List<FarmInventory> listFarmInventryList(Long farmerId) {

		return list("FROM FarmInventory fi WHERE fi.farmer.id=?", farmerId);
	}

	public Object findAnimalHusbandaryByFarmAndOtherItems(long farmId, long animalStr, String revenueStr,
			long housStr) {

		Object[] values = { farmId, animalStr, revenueStr, housStr };
		return find(
				"from AnimalHusbandary ah where ah.farmer.id=? AND ah.farmAnimal.id = ? AND ah.revenue=? AND ah.animalHousing.id=?",
				values);
	}

	public List<FarmCatalogue> listFodderBasedOnType() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cat WHERE cat.typez='3' AND cat.status=?", values);
	}

	public List<FarmCatalogue> listAnimalHousingBasedOnType() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cat WHERE cat.typez='4'  AND cat.status=?", values);
	}

	public List<FarmCatalogue> listRevenueBasedOnType() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cat WHERE cat.typez='5'  AND cat.status=?", values);
	}

	@Override
	public CropSupply findcropSupplyId(String id) {

		// TODO Auto-generated method stub
		return (CropSupply) find("FROM CropSupply  cs WHERE cs.id = ?", Long.valueOf(id));
	}

	@Override
	public List<CropSupplyDetails> findCropSupplyDetailId(long id) {

		// TODO Auto-generated method stub
		return list("FROM CropSupplyDetails csd WHERE csd.cropSupply.id=?", id);
	}

	@Override
	public List<Farm> listFarmerFarmByFarmerId(String farmerId) {

		// TODO Auto-generated method stub
		return list("FROM Farm f where f.farmer.farmerId =? and f.status=1 ", farmerId);
	}

	@Override
	public List<Farm> listfarmbyVillageId(Long id) {

		// TODO Auto-generated method stub
		return list(
				"SELECT farm From Farmer f INNER JOIN f.farms farm WHERE f.id = farm.farmer.id AND farm.farmer.village.id=?",
				id);
	}

	@Override
	public List<FarmCrops> listOfCrops(long id) {

		// TODO Auto-generated method stub
		return list("FROM FarmCrops cd WHERE cd.farm.id = ?  and cd.status=1", id);
	}

	public List<Object[]> listPeriodicInspectionFarmer() {

		String queryString = "SELECT distinct farmer.FARMER_ID, farmer.FIRST_NAME FROM periodic_inspection pi INNER JOIN farm farm ON pi.FARM_ID = farm.FARM_CODE INNER JOIN farmer farmer on farm.FARMER_ID=farmer.ID";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> listPeriodicInspectionFarm() {

		// String queryString="SELECT farm.FARM_CODE, farm.FARM_NAME FROM
		// periodic_inspection pi INNER JOIN farm farm ON pi.FARM_ID =
		// farm.FARM_CODE";
		String hqlString = "SELECT fm.farmCode, fm.farmName FROM Farm fm WHERE fm.farmCode IN (SELECT pi.farmId FROM PeriodicInspection pi)";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hqlString);
		List<Object[]> result = query.list();
		return result;
	}

	@Override
	public List<Farmer> listOfFarmers(String selectedVillage) {

		// TODO Auto-generated method stub
		return list("From Farmer f WHERE f.village.id=?  And f.status=1 ", Long.valueOf(selectedVillage));
	}

	@Override
	public List<FarmCrops> listOfCropNames(String selectedCropType, long farmId) {

		// TODO Auto-generated method stub
		Object values[] = { Integer.valueOf(selectedCropType), farmId };
		return list("FROM FarmCrops fc WHERE fc.cropCategory = ? and fc.farm.id=?  and fc.status=1", values);

	}

	@Override
	public List<FarmCrops> listOfVariety(String selectedCropName) {

		// TODO Auto-generated method stub
		return list("FROM ProcurementVariety pv WHERE pv.procurementProduct.id = ?", Long.valueOf(selectedCropName));
	}

	@Override
	public List<ProcurementGrade> listOfGrade(String selectedVariety) {

		// TODO Auto-generated method stub
		return list("FROM ProcurementGrade pg WHERE pg.procurementVariety.id = ?", Long.valueOf(selectedVariety));
	}

	@Override
	public Double findGradePrice(String selectedGrade) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select cs.price from ProcurementGrade cs WHERE cs.id=:ID");
		query.setParameter("ID", Long.valueOf(selectedGrade));
		List list = query.list();
		Double value = (Double) list.get(0);

		session.flush();
		session.close();
		return value;
	}

	public List<String[]> listIcsName() {

		// TODO Auto-generated method stub
		return list(
				"SELECT distinct f.icsName,f.icsCode from Farmer f where f.icsName!=null and f.icsName!='' and f.icsName!='-1' ORDER BY f.icsName ASC");

	}

	/*
	 * public List<Warehouse> listSamithiName() { // TODO Auto-generated method
	 * stub Session sessions = getSessionFactory().openSession(); String
	 * queryString =
	 * "select w.name from farmer f inner join  warehouse w on  w.id=f.SAMITHI_ID "
	 * ; Query query = sessions.createSQLQuery(queryString); List<Warehouse>
	 * array = query.list(); sessions.flush(); sessions.close(); return array; }
	 */

	public List<Warehouse> listSamithiName() {

		return list("FROM Warehouse w ORDER BY w.name ASC");
	}

	public List<Farm> listFarmName(String branchId) {

		// TODO Auto-generated method stub
		// return list("SELECT farmName from Farm");
		// return list("FROM Farm fa ORDER BY fa.farmName ASC");
		return list("FROM Farm fa WHERE fa.farmer.branchId=? and fa.status=1   ORDER BY fa.farmName ASC", branchId);
	}

	/*
	 * public boolean isVillageMappingExist(long id) { Object[] values = {id,
	 * ESETxnStatus.SUCCESS.ordinal()}; List<Farmer> farmerList =
	 * list("FROM Farmer farmer WHERE farmer.village.id = ? AND farmer.statusCode = ?"
	 * , values); if (!ObjectUtil.isListEmpty(farmerList)) { return true; }
	 * return false; }
	 */

	@Override
	public List<BankInformation> findFarmerBankinfo(long id) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("SELECT bi from Farmer fr INNER JOIN fr.bankInfo bi WHERE fr.id=:ID");
		query.setParameter("ID", id);
		List list = query.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public long listOfFarmersCountBasedOnBranch(String branchIdValue) {

		long totalFarmers = 0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM FARMER WHERE SAMITHI_ID IS NOT NULL AND VILLAGE_ID IS NOT NULL AND BRANCH_ID= :BRANCH AND STATUS_CODE = '"
				+ ESETxnStatus.SUCCESS.ordinal() + "'";
		Query query = sessions.createSQLQuery(queryString);
		query.setParameter("BRANCH", branchIdValue);
		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalFarmers = count;
		}
		sessions.flush();
		sessions.close();
		return totalFarmers;
	}

	@Override
	public boolean findCurrentSeason() {

		boolean flag;
		// TODO Auto-generated method stub
		HarvestSeason harvestSeason = (HarvestSeason) find("FROM HarvestSeason hs where hs.currentSeason=?", 1);
		if (ObjectUtil.isEmpty(harvestSeason)) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;

	}

	public List<Object[]> listFertilizerAppliedAndPestAppliedWithQty(String farmCode, String cropCode) {

		Session sessions = getSessionFactory().openSession();
		// String queryString = "SELECT IFNULL(SUM(pl.`QUANTITY`),0) AS QTY,CASE
		// pl.`CATALOUGE_VALUE` WHEN NULL THEN pl.OTHER_CATALOGUE_VALUE_NAME
		// ELSE
		// pl.`CATALOUGE_VALUE` END AS CATALOGUE_VALUE,pl.I_TYPE AS TYPE FROM
		// `periodic_inspection`
		// pins JOIN `periodic_inspection_data_list` pl ON pins.ID =
		// pl.`PERIODIC_INSPECTION_ID`
		// WHERE pl.I_TYPE IN ('FRTATYP','PEST') AND pins.FARM_ID =
		// '"+farmCode+"' GROUP BY
		// CATALOGUE_VALUE,pl.I_TYPE";
		// SQLQuery query =
		// sessions.createSQLQuery(queryString).addScalar("QTY",
		// Hibernate.DOUBLE).addScalar("CATALOGUE_VALUE",
		// Hibernate.STRING).addScalar("TYPE",
		// Hibernate.STRING);

		String queryString = "SELECT CASE WHEN pid.quantityValue IS NULL THEN '0' "
				+ "ELSE SUM(pid.quantityValue) END,CASE WHEN pid.catalogueValue IS NULL THEN pid.otherCatalogueValueName ELSE pid.catalogueValue END,pid.type,pid.cocDone "
				+ "FROM PeriodicInspectionData pid WHERE pid.type IN (:type1 ,:type2 ,:type3) AND pid.periodicInspection.farmId =:farmCode AND pid.periodicInspection.cropCode =:cropCode AND pid.cocDone =:coc GROUP BY pid.catalogueValue,pid.periodicInspection.inspectionType";

		Query query = sessions.createQuery(queryString);
		query.setParameter("type1", "FRTATYP");
		query.setParameter("type2", "PESTREC");
		query.setParameter("type3", "MATYP");
		query.setParameter("cropCode", cropCode);
		query.setParameter("farmCode", farmCode);
		query.setParameter("coc", "N");

		List<Object[]> array = query.list();

		sessions.flush();
		sessions.close();
		return array;
	}

	@Override
	public Cultivation findByCultivationId(String id) {

		// TODO Auto-generated method stub
		return (Cultivation) find("FROM Cultivation where id=?", Long.valueOf(id));
	}

	@Override
	public List<Object[]> listCropSupplyChartDetails() {

		return list("SELECT sum(cs.totalSaleValu),cs.farmerName FROM CropSupply cs GROUP BY cs.farmerId");
	}

	public List<Object[]> listCropSupplyQtyChartDetails() {

		return list(
				"SELECT sum(csd.qty),cs.farmerName FROM CropSupply cs INNER JOIN cs.cropSupplyDetails csd GROUP BY cs.farmerId");
	}

	public List<Object[]> listCropHarvestQtyChartDetails() {

		return list(
				"SELECT sum(ch.totalQty),concat(ch.farmerName,ch.farmName) FROM CropHarvest ch GROUP BY ch.farmCode");
	}

	@Override
	public List<Object[]> listCropHarvestBuyerChartDetails() {

		return list(
				"SELECT sum(csd.qty),buyer.customerName FROM CropSupply cs INNER JOIN cs.cropSupplyDetails csd INNER JOIN cs.buyerInfo buyer GROUP BY cs.buyerInfo");
	}

	@Override
	public List<FarmCatalogue> listCatelogueType(String type) {

		// TODO Auto-generated method stub
		Object[] values = { Integer.valueOf(type), FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue where typez=? AND status=?", values);
	}

	@Override
	public List<String[]> listFarmerId() {

		// TODO Auto-generated method stub
		return list("SELECT farmerId from Farmer where farmerId!='' ORDER BY farmerId ASC");
	}

	@Override
	public Integer findFarmerCountByMonth(Date sDate, Date eDate) {

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("select count(*) from Farmer where createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public List<Object> listFarmerCountByGroup() {

		return list("select count(*) as count,f.samithi.name as group from Farmer f GROUP BY samithi");
	}

	@Override
	public List<FarmCatalogue> listMachinary() {

		return list("FROM FarmCatalogue cat WHERE cat.typez='5'");
	}

	@Override
	public List<FarmCatalogue> listPolyHouse() {

		return list("FROM FarmCatalogue cat WHERE cat.typez='6'");
	}

	@Override
	public FarmElement findFarmElementById(Long templateId) {

		FarmElement farmElement = (FarmElement) find("FROM FarmElement fe WHERE fe.id = ?", templateId);
		return farmElement;
	}

	@Override
	public List<FarmElement> listFarmElementList(Long farmId) {

		return list("FROM FarmElement fe WHERE fe.farm.id=?", farmId);
	}

	@Override
	public FarmElement findFarmElementItem(Long farmId, String machStr) {

		Object[] values = { farmId, machStr };
		return (FarmElement) find("from FarmElement fe where fe.farm.id=? AND fe.catalogueId.code=?", values);
	}

	@Override
	public void updateFarmElement(FarmElement element) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE FarmElement fe SET fe.count=:COUNT WHERE fe.id=:ID");
		query.setParameter("COUNT", element.getCount());
		query.setParameter("ID", element.getId());
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<FarmElement> listMachinaryList(Long farmId, String machType) {

		Object[] val = { farmId, machType };
		return list("FROM FarmElement fe WHERE fe.farm.id=? AND fe.catalogueType=?", val);
	}

	@Override
	public MasterData findMasterDataIdByCode(String farmerId) {

		// TODO Auto-generated method stub
		return (MasterData) find("FROM MasterData md WHERE md.code = ?", farmerId);
	}

	@Override
	public Integer findFarmersCount() {

		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery("select count(*) from Farmer").uniqueResult()).intValue();
	}

	@Override
	public Integer findFarmersCountByStatus() {

		/*
		 * Session session = getSessionFactory().openSession(); Query query =
		 * session.createQuery("select count(*) from Farmer where status!=2");
		 * 
		 * query.setParameter("statusCode", ESETxnStatus.SUCCESS.ordinal());
		 * 
		 * Integer val = ((Long) query.uniqueResult()).intValue();
		 * session.flush(); session.close(); return val;
		 */

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("select count(*) from Farmer where status in (1) and status_code=:statusCode");

		query.setParameter("statusCode", Integer.valueOf(ESETxnStatus.SUCCESS.ordinal()));

		Integer val = Integer.valueOf(((Long) query.uniqueResult()).intValue());
		session.flush();
		session.close();
		return val;
	}

	@Override
	public FarmCrops findByFarmCropsId(Long cropId) {

		// TODO Auto-generated method stub
		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.id = ?", cropId);
	}

	public Farmer findFarmerByFarmerId(String farmerId, String tenantId) {

		/*
		 * Object[] values = {farmerId, ESETxnStatus.SUCCESS.ordinal()}; Farmer
		 * farmer = (Farmer)
		 * find("FROM Farmer fr WH\ERE  fr.farmerId = ? AND fr.statusCode = ?",
		 * values); return farmer;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM Farmer fr left join fetch fr.village v left join fetch fr.samithi s WHERE  fr.farmerId = :farmerId AND fr.statusCode = :statusCode");
		query.setParameter("farmerId", farmerId);
		query.setParameter("statusCode", ESETxnStatus.SUCCESS.ordinal());

		List<Farmer> farmerList = query.list();

		Farmer farmer = null;
		if (farmerList.size() > 0) {
			farmer = (Farmer) farmerList.get(0);
		}

		session.flush();
		session.close();
		return farmer;
	}

	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long seasonId, long procurementProductId,
			long farmerId, String tenantId) {

		// HQL Query procurementProductId based constraints removed for fetching
		// single farmer
		// account
		/*
		 * return (ESEAccount)
		 * find("SELECT c.account FROM Contract c WHERE c.season.id=?  AND c.farmer.id=?"
		 * , new Object[]{seasonId, farmerId});
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session
				.createQuery("SELECT c.account FROM Contract c WHERE c.season.id=:seasonId AND c.farmer.id=:farmerId");
		query.setParameter("seasonId", seasonId);
		query.setParameter("farmerId", farmerId);

		/*
		 * List<ESEAccount> eseAccountList = query.list(); ESEAccount eseAccount
		 * = (ESEAccount) eseAccountList.get(0);
		 */

		List<ESEAccount> eseAccountList = query.list();

		ESEAccount eseAccount = null;
		if (eseAccountList.size() > 0) {
			eseAccount = (ESEAccount) eseAccountList.get(0);
		}

		session.flush();
		session.close();
		return eseAccount;
	}

	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileId(String farmerProfileId,
			String tenantId) {
		// TODO Auto-generated method stub
		// return (InterestCalcConsolidated) find("FROM InterestCalcConsolidated
		// icc WHERE
		// icc.farmerProfileId = ? ", farmerProfileId);

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session
				.createQuery("FROM InterestCalcConsolidated icc WHERE icc.farmerProfileId = :farmerProfileId");
		query.setParameter("farmerProfileId", farmerProfileId);

		/*
		 * List<InterestCalcConsolidated> intCalConsolidatedList = query.list();
		 * InterestCalcConsolidated intCalConsolidated =
		 * (InterestCalcConsolidated) intCalConsolidatedList.get(0);
		 */

		List<InterestCalcConsolidated> intCalConsolidatedList = query.list();

		InterestCalcConsolidated intCalConsolidated = null;
		if (intCalConsolidatedList.size() > 0) {
			intCalConsolidated = (InterestCalcConsolidated) intCalConsolidatedList.get(0);
		}

		session.flush();
		session.close();
		return intCalConsolidated;
	}

	@Override
	public void updateInterestCalcConsolidated(InterestCalcConsolidated intCalConsolidated, String tenantId) {

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(intCalConsolidated);
		sessions.flush();
		sessions.close();
	}

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, String branchId) {

		String query = "SELECT IFNULL(GROUP_CONCAT(DISTINCT V.CODE),'') VILLAGE_CODE, V.NAME VILLAGE_NAME, "
				+ " SUM(FC.EST_YIELD) TOTAL_AREA, SUM(FDI.PROPOSED_PLANTING_AREA) TOTAL_PRODUCTION "
				+ " FROM village AS V INNER JOIN farmer AS FAR ON FAR.VILLAGE_ID = V.ID "
				+ " INNER JOIN farm AS FAM ON FAM.FARMER_ID = FAR.ID "
				+ " INNER JOIN farm_crops AS FC ON FAM.ID = FC.FARM_ID "
				+ " INNER JOIN procurement_variety AS PVAR ON FC.PROCUREMENT_CROPS_VARIETY_ID = PVAR.ID "
				+ " INNER JOIN procurement_product AS PPRO ON PVAR.PROCUREMENT_PRODUCT_ID = PPRO.ID  "
				+ " INNER JOIN farm_detailed_info AS FDI ON FAM.FARM_DETAILED_INFO_ID = FDI.ID "
				+ " WHERE  V.CODE LIKE :selectedVillage AND v.BRANCH_ID='" + branchId
				+ "'   GROUP BY V.ID ORDER BY V.CODE";
		System.out.println("query" + query);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("VILLAGE_CODE", StringType.INSTANCE)
				.addScalar("VILLAGE_NAME", StringType.INSTANCE).addScalar("TOTAL_AREA", DoubleType.INSTANCE)
				.addScalar("TOTAL_PRODUCTION", DoubleType.INSTANCE)
				.setString("selectedVillage", !StringUtil.isEmpty(selectedVillage) ? selectedVillage : "%%").list();

	}

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, int startIndex, int limit, String branchId) {

		String query = "SELECT IFNULL(GROUP_CONCAT(DISTINCT V.CODE),'') VILLAGE_CODE, V.NAME VILLAGE_NAME, "
				+ " SUM(FC.EST_YIELD) TOTAL_AREA, SUM(FDI.PROPOSED_PLANTING_AREA) TOTAL_PRODUCTION "
				+ " FROM village AS V INNER JOIN farmer AS FAR ON FAR.VILLAGE_ID = V.ID "
				+ " INNER JOIN farm AS FAM ON FAM.FARMER_ID = FAR.ID "
				+ " INNER JOIN farm_crops AS FC ON FAM.ID = FC.FARM_ID "
				+ " INNER JOIN procurement_variety AS PVAR ON FC.PROCUREMENT_CROPS_VARIETY_ID = PVAR.ID "
				+ " INNER JOIN procurement_product AS PPRO ON PVAR.PROCUREMENT_PRODUCT_ID = PPRO.ID  "
				+ " INNER JOIN farm_detailed_info AS FDI ON FAM.FARM_DETAILED_INFO_ID = FDI.ID "
				+ " WHERE  fc.status=1 and  V.CODE LIKE :selectedVillage and v.BRANCH_ID='" + branchId
				+ "' GROUP BY V.ID ORDER BY V.CODE" + " LIMIT " + startIndex + "," + limit + ";";
		System.out.println("query" + query);
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("VILLAGE_CODE", StringType.INSTANCE)
				.addScalar("VILLAGE_NAME", StringType.INSTANCE).addScalar("TOTAL_AREA", DoubleType.INSTANCE)
				.addScalar("TOTAL_PRODUCTION", DoubleType.INSTANCE)
				.setString("selectedVillage", !StringUtil.isEmpty(selectedVillage) ? selectedVillage : "%%").list();
	}

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, String branchId) {

		String query = "SELECT PPRO.CODE CROP_CODE, PPRO.NAME CROP_NAME, "
				+ " SUM(FC.EST_YIELD) AREA, SUM(FDI.PROPOSED_PLANTING_AREA) PRODUCTION_PER_YEAR "
				+ " FROM village AS V INNER JOIN farmer AS FAR ON FAR.VILLAGE_ID = V.ID "
				+ " INNER JOIN farm AS FAM ON FAM.FARMER_ID = FAR.ID "
				+ " INNER JOIN farm_crops AS FC ON FAM.ID = FC.FARM_ID "
				+ " INNER JOIN procurement_variety AS PVAR ON FC.PROCUREMENT_CROPS_VARIETY_ID = PVAR.ID "
				+ " INNER JOIN procurement_product AS PPRO ON PVAR.PROCUREMENT_PRODUCT_ID = PPRO.ID  "
				+ " INNER JOIN farm_detailed_info AS FDI ON FAM.FARM_DETAILED_INFO_ID = FDI.ID "
				+ " WHERE   fc.status=1 and V.CODE LIKE :selectedVillage AND v.BRANCH_ID='" + branchId
				+ "' GROUP BY PPRO.CODE";

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("CROP_CODE", StringType.INSTANCE)
				.addScalar("CROP_NAME", StringType.INSTANCE).addScalar("AREA", DoubleType.INSTANCE)
				.addScalar("PRODUCTION_PER_YEAR", DoubleType.INSTANCE)
				.setString("selectedVillage", !StringUtil.isEmpty(selectedVillage) ? selectedVillage : "%%").list();

	}

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, int startIndex, int limit,
			String branchId) {

		String query = "SELECT PPRO.CODE CROP_CODE, PPRO.NAME CROP_NAME, "
				+ " SUM(FC.EST_YIELD) AREA, SUM(FDI.PROPOSED_PLANTING_AREA) PRODUCTION_PER_YEAR "
				+ " FROM village AS V INNER JOIN farmer AS FAR ON FAR.VILLAGE_ID = V.ID "
				+ " INNER JOIN farm AS FAM ON FAM.FARMER_ID = FAR.ID "
				+ " INNER JOIN farm_crops AS FC ON FAM.ID = FC.FARM_ID "
				+ " INNER JOIN procurement_variety AS PVAR ON FC.PROCUREMENT_CROPS_VARIETY_ID = PVAR.ID "
				+ " INNER JOIN procurement_product AS PPRO ON PVAR.PROCUREMENT_PRODUCT_ID = PPRO.ID  "
				+ " INNER JOIN farm_detailed_info AS FDI ON FAM.FARM_DETAILED_INFO_ID = FDI.ID "
				+ " WHERE   fc.status=1 and V.CODE LIKE :selectedVillage AND v.BRANCH_ID='" + branchId
				+ "' GROUP BY PPRO.CODE" + " LIMIT " + startIndex + "," + limit + ";";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("CROP_CODE", StringType.INSTANCE)
				.addScalar("CROP_NAME", StringType.INSTANCE).addScalar("AREA", DoubleType.INSTANCE)
				.addScalar("PRODUCTION_PER_YEAR", DoubleType.INSTANCE)
				.setString("selectedVillage", !StringUtil.isEmpty(selectedVillage) ? selectedVillage : "%%").list();
	}

	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, String branchId) {

		if (StringUtil.isEmpty(selectedCropCode)) {
			selectedCropCode = "%%";
		}

		String query = "SELECT IFNULL(GROUP_CONCAT(DISTINCT PP.CODE),'') CROP_CODE, PP.NAME CROP_NAME, SUM(FC.EST_YIELD) TOTAL_AREA,SUM(FDI.PROPOSED_PLANTING_AREA) TOTAL_YIELD FROM "
				+ "VILLAGE AS V INNER JOIN FARMER AS FR ON FR.VILLAGE_ID = V.ID INNER JOIN"
				+ " FARM AS FM ON FM.FARMER_ID = FR.ID INNER JOIN FARM_CROPS AS FC ON FM.ID = FC.FARM_ID INNER JOIN"
				+ " PROCUREMENT_VARIETY AS PV ON FC.PROCUREMENT_CROPS_VARIETY_ID = PV.ID INNER JOIN PROCUREMENT_PRODUCT AS PP ON PV.PROCUREMENT_PRODUCT_ID = PP.ID INNER JOIN "
				+ "FARM_DETAILED_INFO AS FDI ON FM.FARM_DETAILED_INFO_ID = FDI.ID WHERE   fc.status=1 and  PP.CODE LIKE :selectedCropCode AND V.BRANCH_ID='"
				+ branchId + "' GROUP BY PP.CODE ORDER BY PP.NAME";

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("CROP_CODE", StringType.INSTANCE)
				.addScalar("CROP_NAME", StringType.INSTANCE).addScalar("TOTAL_AREA", DoubleType.INSTANCE)
				.addScalar("TOTAL_YIELD", DoubleType.INSTANCE)
				.setString("selectedCropCode", !StringUtil.isEmpty(selectedCropCode) ? selectedCropCode : "%%").list();
	}

	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, int startIndex, int limit, String branchId) {

		if (StringUtil.isEmpty(selectedCropCode)) {
			selectedCropCode = "%%";
		}
		String query = "SELECT IFNULL(GROUP_CONCAT(DISTINCT PP.CODE),'') CROP_CODE, PP.NAME CROP_NAME, SUM(FC.EST_YIELD) TOTAL_AREA,SUM(FDI.PROPOSED_PLANTING_AREA) TOTAL_YIELD FROM "
				+ "VILLAGE AS V INNER JOIN FARMER AS FR ON FR.VILLAGE_ID = V.ID INNER JOIN"
				+ " FARM AS FM ON FM.FARMER_ID = FR.ID INNER JOIN FARM_CROPS AS FC ON FM.ID = FC.FARM_ID INNER JOIN"
				+ " PROCUREMENT_VARIETY AS PV ON FC.PROCUREMENT_CROPS_VARIETY_ID = PV.ID INNER JOIN PROCUREMENT_PRODUCT AS PP ON PV.PROCUREMENT_PRODUCT_ID = PP.ID INNER JOIN "
				+ "FARM_DETAILED_INFO AS FDI ON FM.FARM_DETAILED_INFO_ID = FDI.ID WHERE   fc.status=1 and PP.CODE LIKE :selectedCropCode AND V.BRANCH_ID='"
				+ branchId + "' GROUP BY PP.CODE ORDER BY PP.NAME" + " LIMIT " + startIndex + "," + limit + ";";

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("CROP_CODE", StringType.INSTANCE)
				.addScalar("CROP_NAME", StringType.INSTANCE).addScalar("TOTAL_AREA", DoubleType.INSTANCE)
				.addScalar("TOTAL_YIELD", DoubleType.INSTANCE)
				.setString("selectedCropCode", !StringUtil.isEmpty(selectedCropCode) ? selectedCropCode : "%%").list();
	}

	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, String branchId) {

		if (StringUtil.isEmpty(selectedCropCode)) {
			selectedCropCode = "%%";
		}
		String query = "SELECT V.CODE VILLAGE_CODE , V.NAME VILLAGE_NAME, SUM(FC.EST_YIELD) "
				+ "TOTAL_AREA, SUM(FDI.PROPOSED_PLANTING_AREA) TOTAL_YIELD FROM VILLAGE AS V INNER JOIN FARMER AS FR ON FR.VILLAGE_ID = V.ID "
				+ "INNER JOIN FARM AS FM ON FM.FARMER_ID = FR.ID INNER JOIN FARM_CROPS AS FC ON FM.ID = FC.FARM_ID "
				+ "INNER JOIN PROCUREMENT_VARIETY AS PV ON FC.PROCUREMENT_CROPS_VARIETY_ID = PV.ID "
				+ "INNER JOIN PROCUREMENT_PRODUCT AS PP ON PV.PROCUREMENT_PRODUCT_ID = PP.ID "
				+ "INNER JOIN FARM_DETAILED_INFO AS FDI ON FM.FARM_DETAILED_INFO_ID = FDI.ID WHERE   fc.status=1 and PP.CODE LIKE :selectedCropCode AND V.BRANCH_ID='"
				+ branchId + "' " + "GROUP BY V.CODE ORDER BY V.NAME";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("VILLAGE_CODE", StringType.INSTANCE)
				.addScalar("VILLAGE_NAME", StringType.INSTANCE).addScalar("TOTAL_AREA", DoubleType.INSTANCE)
				.addScalar("TOTAL_YIELD", DoubleType.INSTANCE)
				.setString("selectedCropCode", !StringUtil.isEmpty(selectedCropCode) ? selectedCropCode : "%%").list();
	}

	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, int startIndex, int limit,
			String branchId) {

		if (StringUtil.isEmpty(selectedCropCode)) {
			selectedCropCode = "%%";
		}
		String query = "SELECT V.CODE VILLAGE_CODE , V.NAME VILLAGE_NAME, SUM(FC.EST_YIELD) TOTAL_AREA, SUM(FDI.PROPOSED_PLANTING_AREA) "
				+ "TOTAL_YIELD FROM VILLAGE AS V INNER JOIN FARMER AS FR ON FR.VILLAGE_ID = V.ID INNER JOIN FARM AS FM ON FM.FARMER_ID = FR.ID "
				+ "INNER JOIN FARM_CROPS AS FC ON FM.ID = FC.FARM_ID INNER JOIN PROCUREMENT_VARIETY AS PV ON FC.PROCUREMENT_CROPS_VARIETY_ID = PV.ID "
				+ "INNER JOIN PROCUREMENT_PRODUCT AS PP ON PV.PROCUREMENT_PRODUCT_ID = PP.ID "
				+ "INNER JOIN FARM_DETAILED_INFO AS FDI ON FM.FARM_DETAILED_INFO_ID = FDI.ID WHERE   fc.status=1 and PP.CODE LIKE :selectedCropCode AND V.BRANCH_ID='"
				+ branchId + "' " + "GROUP BY V.CODE ORDER BY V.NAME" + " LIMIT " + startIndex + "," + limit + ";";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(query).addScalar("VILLAGE_CODE", StringType.INSTANCE)
				.addScalar("VILLAGE_NAME", StringType.INSTANCE).addScalar("TOTAL_AREA", DoubleType.INSTANCE)
				.addScalar("TOTAl_YIELD", DoubleType.INSTANCE)
				.setString("selectedCropCode", !StringUtil.isEmpty(selectedCropCode) ? selectedCropCode : "%%").list();
	}

	@Override
	public List<FarmCatalogue> listSeedTreatmentDetailsBasedOnType() {

		return list("FROM FarmCatalogue cat WHERE cat.typez='7'");
	}

	@Override
	public FarmCatalogue findfarmcatalogueById(String farmCatalogueId) {

		Object[] values = { farmCatalogueId, 7 };
		// TODO Auto-generated method stub
		return (FarmCatalogue) find("FROM FarmCatalogue fc WHERE fc.code=? AND fc.typez=?", values);
	}

	@Override
	public void updateFarmerImageInfo(long id, long imageInfoId) {

		Session session = getSessionFactory().openSession();
		SQLQuery query = session
				.createSQLQuery("update farmer set IMAGE_INFO_ID = " + imageInfoId + " where id=" + id + "");
		int list = query.executeUpdate();

		session.flush();
		session.close();

	}

	@Override
	public List<Object> listPeriodicInsoectionFatherName() {

		// TODO Auto-generated method stub
		return list(
				"SELECT distinct fr.lastName FROM PeriodicInspection p INNER JOIN p.farm f INNER JOIN f.farmer fr where fr.lastName!=null AND fr.lastName!=''");
	}

	@Override
	public List<String[]> listByFatherNameList() {

		// TODO Auto-generated method stub
		return list("SELECT lastName from Farmer where lastName!='' and lastName!=null ORDER BY lastName ASC");
	}

	@Override
	public List<Object[]> listFarmerCodeIdNameByVillageCode(String villageCode) {

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(
				"SELECT f.farmerId,f.farmerCode,f.firstName,f.lastName,f.id,f.surName FROM Farmer f where f.village.code=:villageCode AND f.status=:status AND f.statusCode=:statusCode");
		query.setParameter("villageCode", villageCode);
		query.setParameter("status", Farmer.Status.ACTIVE.ordinal());
		query.setParameter("statusCode", ESETxnStatus.SUCCESS.ordinal());
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object[]> listHarvestSeasonFromToPeriod() {

		// TODO Auto-generated method stub
		return list("SELECT fromPeriod, toPeriod, id FROM HarvestSeason");
	}

	@Override
	public List<Object[]> listSeasonCodeAndName() {

		return list("SELECT s.code,s.name FROM HarvestSeason s ORDER BY s.name ASC");
	}

	@Override
	public HarvestSeason findSeasonNameByCode(String seasonCode) {

		return (HarvestSeason) find("FROM HarvestSeason hs WHERE hs.code=? ORDER BY hs.name ASC", seasonCode);
	}

	@Override
	public List<Object[]> listFarmerInfo() {
		// 0=Id,1=Farmer Id,2=Farmer Code,3=First Name,4=Last
		// name,5=surName,6=village name,7=Group name,8=Is certified
		// Farmer,9=Status,10=BranchId
		return list(
				"SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,f.surName,f.village.name,f.samithi.name,f.isCertifiedFarmer,f.status,f.branchId,f.village.code,f.city.locality.code,f.city.locality.name,COALESCE(f.fpo,''),f.city.name from Farmer f where f.refId is null AND  f.statusCode='"
						+ ESETxnStatus.SUCCESS.ordinal() + "'");
	}

	@Override
	public List<FarmerSoilTesting> listFarmerSoilTestingByFarmId(String farmId) {

		return list("FROM FarmerSoilTesting fst WHERE fst.farmId=?", Long.parseLong(farmId));
	}

	public List<FarmerLandDetails> listfarmerlanddetailsList(Long farmId) {

		return list("FROM FarmerLandDetails ah WHERE ah.farm.id=?", farmId);
	}

	public List<Object[]> listfarmingseasonlist() {

		return list("SELECT hs.code,hs.name FROM HarvestSeason hs");
	}

	public String findHarvestSeasonBycodeusingname(String name) {

		return (String) find("SELECT  hs.code From HarvestSeason hs where hs.name=?", name);

	}

	@Override
	public List<FarmerLandDetails> listFarmingSystemByFarmId(long id) {

		// TODO Auto-generated method stub
		return list("FROM FarmerLandDetails fd WHERE fd.farmId.id=?", id);

	}

	@Override
	public FarmerLandDetails findFarmerLandDetailsById(long id) {

		// TODO Auto-generated method stub
		return (FarmerLandDetails) find("FROM FarmerLandDetails fd WHERE fd.id=?", id);
	}

	@Override
	public void deleteFarmerLandDetailById(long farmerLandId) {

		Session session = getSessionFactory().openSession();
		// TODO Auto-generated method stub
		String hql = "DELETE FROM FarmerLandDetails " + "WHERE id = :farmerLandId";
		Query query = session.createQuery(hql);
		query.setParameter("farmerLandId", farmerLandId);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	public String findSanghamTypeFromWarehouseByWarehouseCode(String warehouseCode) {

		return (String) find("SELECT wh.sanghamType From Warehouse wh where wh.code = ? ", warehouseCode);
	}

	@Override
	public List<FarmerSourceIncome> listFarmSourceIncomeByFarmerId(String farmerId) {

		return list("FROM FarmerSourceIncome fsi where fsi.farmerId=?", Long.parseLong(farmerId));

	}

	public List<FarmerIncomeDetails> listFarmerIncomeDetailsBySourceIncomeId(String farmerSourceId) {

		return list("FROM FarmerIncomeDetails fid where fid.farmerSourceId=?", Long.parseLong(farmerSourceId));

	}

	public void deleteFarmerIncomeDetails(String tableName, String columnName) {

		// removeBlindChilds(tableName, columnName);
	}

	public void updateFarmerIdInFarmerSourceIncome(String farmerSourceId, String farmerId) {

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(
				"UPDATE farmer_source_income SET farmer_id='" + farmerId + "' Where id='" + farmerSourceId + "'");
		int list = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public List<Expenditure> listExpentitureListByFarmId(Long id) {
		// TODO Auto-generated method stub

		return list("FROM Expenditure e where e.farm.id=?", id);
	}

	@Override
	public HarvestSeason findHarvestSeasosnByName(String name) {

		return (HarvestSeason) find("From HarvestSeason hs where hs.name=?", name);
	}

	@Override
	public Warehouse findWareHouseByFarmerId(String farmerId) {

		Warehouse wareHouse = (Warehouse) find("FROM Warehouse wh WHERE wh.code=? ORDER BY wh.name ASC", farmerId);
		return wareHouse;
	}

	@Override
	public DocumentUpload findDocumentById(Long id) {

		return (DocumentUpload) find("FROM DocumentUpload doc WHERE doc.id =?", id);
	}

	@Override
	public String findBySeasonCode(String seasonCode) {

		// TODO Auto-generated method stub
		return (String) find("SELECT  hs.name From HarvestSeason hs where hs.code=?", seasonCode);
	}

	@Override
	public List<FarmCatalogue> listCatelogueTypeWithOther(String type) {

		// TODO Auto-generated method stub
		return list("FROM FarmCatalogue where typez in (?,?) and status = ?",
				new Object[] { Integer.valueOf(type), FarmCatalogue.OTHER, FarmCatalogue.ACTIVE });
	}

	@Override
	public List<FarmerCropProdAnswers> listFarmerwithCategoryCode(String categoryCode) {

		return list("FROM FarmerCropProdAnswers fcp where fcp.categoryCode=?", categoryCode);

	}

	@Override
	public List<Object> listFarmerCountByBranch() {

		return list("select count(f.id) as count,f.branchId as name from Farmer f GROUP BY f.branchId");
	}

	@Override
	public List<Object> listTotalFarmAcreByBranch(String branchId) {

		if (StringUtil.isEmpty(branchId)) {
			return list(
					"SELECT SUM(fd.totalLandHolding) as Total,SUM(fd.proposedPlantingArea) as Proposed,f.branchId FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f where  fa.status=1  GROUP BY f.branchId");
		} else {
			return list(
					"SELECT SUM(fd.totalLandHolding) as Total,SUM(fd.proposedPlantingArea) as Proposed,f.branchId FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f WHERE f.branchId=?  and  fa.status=1  ",
					branchId);
		}
	}

	@Override
	public List<Object> listSeedTypeCount() {

		String select = "-1";
		return list(
				"select count(f.id) as count,f.type as name from FarmCrops f where type!=?  and f.status=1 GROUP BY f.type",
				select);
	}

	@Override
	public List<Object> listSeedSourceCount() {

		return list(
				"select count(f.id) as count,f.seedSource as name from FarmCrops f where seedSource!=? and f.status=1 GROUP BY f.seedSource",
				SELECT);
	}

	@Override
	public List<Object> listSeedSourceCountBySource(String selectedSeedSource) {

		Object[] values = { selectedSeedSource, SELECT };
		return list(
				"select count(f.id) as count,f.seedSource as name from FarmCrops f where branchId=? and seedSource!=? and f.status=1 GROUP BY f.seedSource",
				values);
	}

	@Override
	public List<FarmersQuestionAnswers> listQuestionBySection(long id) {

		return list("FROM FarmersQuestionAnswers fqa where fqa.farmersSectionAnswers.id=? ORDER BY fqa.serialNo ASC",
				id);
	}

	@Override
	public List<Object[]> listFarmInfo() {

		return list("SELECT f.id,f.farmCode,f.farmName from Farm f  where  f.status=1 ");
	}

	@Override
	public Object[] findFarmerAndFatherNameByFarmerId(String farmerId) {

		return (Object[]) find(
				"SELECT f.farmerId,f.farmerCode,f.firstName,f.lastName,f.fatherHusbandName,f.village,f.samithi from Farmer f where f.farmerId=?",
				farmerId);
	}

	@Override
	public List<Farmer> listFarmerByFarmerId(long id) {

		Object[] values = { id, ESETxnStatus.SUCCESS.ordinal() };
		return (List<Farmer>) list("FROM Farmer f WHERE f.id=? AND f.statusCode = ?", values);
	}

	@Override
	public List<Object> listFarmerCountByGroupAndBranch(String branchId, Integer selectedYear) {
		Object[] values = { branchId, ESETxnStatus.SUCCESS.ordinal(), selectedYear, (selectedYear - 1) };
		Object[] valuesWithoutBranch = { ESETxnStatus.SUCCESS.ordinal(), selectedYear, (selectedYear - 1) };
		if (StringUtil.isEmpty(branchId)) {
			return list(
					"select count(*) as count,f.samithi.name as group from Farmer f where f.statusCode = ? AND  Year(f.createdDate)=? OR Year(f.createdDate)=? GROUP BY f.samithi",
					valuesWithoutBranch);
		} else {
			return list(
					"select count(*) as count,f.samithi.name as group from Farmer f where f.branchId=? AND f.statusCode = ? AND  Year(f.createdDate)=? OR Year(f.createdDate)=? GROUP BY f.samithi",
					values);
		}
	}

	public Integer findFarmersCountByBranch(String branchId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery("select count(*) from Farmer where statusCode=:statusCode AND branchId=:branchId");

		query.setParameter("statusCode", ESETxnStatus.SUCCESS.ordinal());
		query.setParameter("branchId", branchId);

		Integer val = ((Long) query.uniqueResult()).intValue();

		return val;
	}

	@Override
	public List<Object> findTotalFarmAcreByBranch(String selectedBranch) {

		Object[] values = { selectedBranch };
		if (StringUtil.isEmpty(selectedBranch)) {
			return list(
					"SELECT SUM(fd.totalLandHolding) as Total,SUM(fd.proposedPlantingArea) as Proposed FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f  where  fa.status=1  ");
		} else {
			return list(
					"SELECT SUM(fd.totalLandHolding) as Total,SUM(fd.proposedPlantingArea) as Proposed FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f WHERE f.branchId=?  and  fa.status=1  ",
					values);
		}
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldByBranch(String selectedBranch, Integer selectedYear) {

		Object[] values = { selectedBranch, selectedYear, (selectedYear - 1) };
		Object[] yearValues = { selectedYear, (selectedYear - 1) };
		if (StringUtil.isEmpty(selectedBranch)) {
			return list(
					"SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE Year(f.createdDate)=? OR Year(f.createdDate)=? and fc.status=1 and fc.farm.status=1",
					yearValues);

		} else {
			return list(
					"SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE fc.farm.farmer.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?)  and fc.status=1 and fc.farm.status=1",
					values);
		}

	}

	@Override
	public Long findInputCostByBranch(String branchId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery("select cast(sum(totalCoc) as long) from Cultivation where branchId=:branchId");

		query.setParameter("branchId", branchId);

		Long val = 0L;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).longValue();
		}
		return val;
	}

	@Override
	public Long findInputCost() {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select cast(sum(totalCoc) as long) from Cultivation");
		Long val = 0L;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).longValue();
		}
		return val;
	}

	@Override
	public Integer findTotalCultivationProdLandByBranch(String selectedBranch, Integer selectedYear) {
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedBranch)) {
			/*
			 * return ((Long) session.createQuery(
			 * "SELECT cast(sum(f.farmDetailedInfo.proposedPlantingArea) as long) from Farm f where f.farmCode in (SELECT c.farmCode from CropHarvest c WHERE Year(c.harvestDate)=:year)"
			 * ).setParameter("year", selectedYear).uniqueResult()).intValue();
			 */

			Query query = session.createQuery(
					"SELECT cast(sum(f.farmDetailedInfo.proposedPlantingArea) as int) from Farm f where f.farmCode in (SELECT c.farmCode from CropHarvest c WHERE Year(c.harvestDate)=:year OR Year(c.harvestDate)=:prevYear)  and  f.status=1  ");
			query.setParameter("year", selectedYear).setParameter("prevYear", (selectedYear - 1));
			Integer val = 0;
			Object obj = query.uniqueResult();
			if (obj != null && ObjectUtil.isInteger(obj)) {
				val = Integer.valueOf(obj.toString());
			}
			return val;
		} else {
			Query query = session.createQuery(
					"SELECT cast(sum(f.farmDetailedInfo.proposedPlantingArea) as int) from Farm f where f.farmCode in (SELECT c.farmCode from CropHarvest c WHERE Year(c.harvestDate)=:year OR Year(c.harvestDate)=:prevYear) and f.farmer.branchId=:branchId  and  f.status=1  ");
			query.setParameter("year", selectedYear);
			query.setParameter("branchId", selectedBranch).setParameter("prevYear", (selectedYear - 1));
			Integer val = 0;
			Object obj = query.uniqueResult();
			if (obj != null && ObjectUtil.isInteger(obj)) {
				val = Integer.valueOf(obj.toString());
			}
			return val;

		}
	}

	/*
	 * @Override public Long findTotalCultivationProdLand() {
	 * 
	 * Session session = getSessionFactory().getCurrentSession(); Query query =
	 * session.createQuery(
	 * "SELECT cast(sum(f.farmDetailedInfo.proposedPlantingArea) as long) from Farm f where   FARM_CODE in (SELECT c.farmCode from CropHarvest c)"
	 * ); Long val = 0L; if (!StringUtil.isEmpty(query.uniqueResult())) { val =
	 * ((Long) query.uniqueResult()).longValue(); } return val; }
	 */
	@Override
	public Long findTotalIncomeFromCottonByBranch(String selectedBranch, Integer selectedYear) {

		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedBranch)) {
			Query query = session.createQuery(
					"SELECT cast(sum(cd.subTotal) as long) from CropHarvestDetails cd Inner join cd.cropHarvest ch WHERE Year(ch.harvestDate)=:year OR Year(ch.harvestDate)=:prevYear");
			// SELECT cast(sum(cd.subTotal) as int) from CropHarvestDetails cd
			// Inner join cd.cropHarvest ch where ch.branchId=:branchId");
			query.setParameter("year", selectedYear).setParameter("prevYear", (selectedYear - 1));
			Long val = 0L;
			Object obj = query.uniqueResult();
			if (obj != null && ObjectUtil.isLong(obj)) {
				val = ((Long) obj).longValue();
			}
			return val;

		} else {
			Query query = session.createQuery(
					"SELECT cast(sum(cd.subTotal) as long) from CropHarvestDetails cd Inner join cd.cropHarvest ch WHERE (Year(ch.harvestDate)=:year OR Year(ch.harvestDate)=:prevYear) and ch.branchId=:branchId");
			query.setParameter("year", selectedYear).setParameter("prevYear", (selectedYear - 1));
			query.setParameter("branchId", selectedBranch);
			Long val = 0L;
			Object obj = query.uniqueResult();
			if (obj != null && ObjectUtil.isLong(obj)) {
				val = ((Long) obj).longValue();
			}
			return val;
		}
	}

	/*
	 * @Override public Long findTotalIncomeFromCotton() {
	 * 
	 * Session session = getSessionFactory().getCurrentSession(); Query query =
	 * session.createQuery(
	 * "SELECT cast(sum(cd.subTotal) as long) from CropHarvestDetails cd Inner join cd.cropHarvest ch"
	 * ); Long val = 0L; if (!StringUtil.isEmpty(query.uniqueResult())) { val =
	 * ((Long) query.uniqueResult()).longValue(); } return val; }
	 */

	@Override
	public Long findCultivatedFarmersCount() {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT distinct(farmerId) from CropHarvest");
		Long val = 0L;
		List<Long> list = query.list();
		val = Long.valueOf(list.size());
		return val;
	}

	@Override
	public Long findCultivatedFarmersCountByBranch(String branchId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT distinct(farmerId) from CropHarvest where branchId=:branchId");
		query.setParameter("branchId", branchId);
		Long val = 0L;
		List<Long> list = query.list();
		val = Long.valueOf(list.size());
		return val;
	}

	@Override
	public List<PeriodicInspectionData> findPeriodicDataByType(String value, long id) {

		Object[] bindValues = { value, id };

		return list("FROM PeriodicInspectionData pid where pid.type=? AND pid.periodicInspection.id=?", bindValues);
	}

	@Override
	public List<PeriodicInspectionSymptom> findPeriodicSymptomsByType(String value, long id) {

		Object[] bindValues = { value, id };

		return list("FROM PeriodicInspectionSymptom sym where sym.type=? AND sym.periodicInspection.id=?", bindValues);
	}

	@Override
	public List<PeriodicInspectionData> listPestRecomentedById(Long inspId) {

		return list("FROM PeriodicInspectionData pid where pid.periodicInspection.id=? AND pid.type='PESTREC'", inspId);
	}

	@Override
	public List<PeriodicInspectionData> listFungicideById(Long inspId) {

		return list("FROM PeriodicInspectionData pid where pid.periodicInspection.id=? AND pid.type='FUNGISREC'",
				inspId);
	}

	@Override
	public List<PeriodicInspectionData> listManureById(Long inspId) {

		return list("FROM PeriodicInspectionData pid where pid.periodicInspection.id=? AND pid.type='MATYP'", inspId);
	}

	@Override
	public List<PeriodicInspectionData> listFertilizerById(Long inspId) {

		return list("FROM PeriodicInspectionData pid where pid.periodicInspection.id=? AND pid.type='FRTATYP'", inspId);
	}

	@Override
	public List<Object[]> listFarmerFarmInfo() {
		// 0. f Id 1.farmerId 2.farmerCode 3.farmer name 4.last/Father name
		// 5.farm name 6.farmCode
		// 7.latitude 8.longitude 9.landmark 10.total holding
		// 11.proposedplanting 12.Village 13.Samithi
		return list(
				"SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,farm.farmName,farm.farmCode,farm.latitude,farm.longitude,farm.landmark,fdi.totalLandHolding,fdi.proposedPlantingArea,f.village.name,f.samithi.name from Farmer f INNER JOIN f.farms farm INNER JOIN farm.farmDetailedInfo fdi"
						+ " where f.statusCode='" + ESETxnStatus.SUCCESS.ordinal() + "'");
	}

	Map<Long, String> iscList = new HashMap<>();

	@Override
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus,
			String selectedFarmer) {

		ProjectionList pList = Projections.projectionList();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Farm.class);
		criteria.createAlias("farmer", "f");

		pList.add(Projections.property("f.id"));
		pList.add(Projections.property("latitude"));
		pList.add(Projections.property("longitude"));
		pList.add(Projections.property("farmCode"));
		pList.add(Projections.property("f.firstName"));
		pList.add(Projections.property("farmName"));
		pList.add(Projections.property("id"));
		pList.add(Projections.property("f.isCertifiedFarmer"));
		// pList.add(Projections.property("fic.organicStatus"));
		iscList = new HashMap<>();

		if (!StringUtil.isEmpty(farm.getCropCode())) {
			criteria.createAlias("farmCrops", "fc");

			criteria.createAlias("fc.procurementVariety", "pv");
			criteria.createAlias("pv.procurementProduct", "pp");
			criteria.add(Restrictions.eq("pp.id", Long.valueOf(farm.getCropCode())));

		}

		if (!ObjectUtil.isEmpty(farm.getFarmer())) {

			if (!ObjectUtil.isEmpty(farm.getFarmer().getVillage())) {
				criteria.createAlias("f.village", "v");
				criteria.add(Restrictions.eq("v.id", farm.getFarmer().getVillage().getId()));
			}

			if (!ObjectUtil.isEmpty(farm.getFarmer().getCity())
					&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality())) {
				criteria.createAlias("f.city", "c");
				criteria.createAlias("c.locality", "l");
				criteria.createAlias("l.state", "s");

				if (!ObjectUtil.isEmpty(farm.getFarmer().getStateId())) {
					criteria.add(Restrictions.eq("s.id", farm.getFarmer().getCity().getLocality().getState().getId()));
				}

				if (farm.getFarmer().getCity().getLocality().getId() > 0) {
					criteria.add(Restrictions.eq("l.id", farm.getFarmer().getCity().getLocality().getId()));
				}
			}
			if (!ObjectUtil.isEmpty(farm.getFarmer().getIsCertifiedFarmer())
					&& !(farm.getFarmer().getIsCertifiedFarmer() < 0)) {
				if (selectedOrganicStatus.equalsIgnoreCase("Conventional")) {
					criteria.add(Restrictions.eq("f.isCertifiedFarmer", 0));
				} else {
					criteria.add(Restrictions.eq("f.isCertifiedFarmer", farm.getFarmer().getIsCertifiedFarmer()));
				}
			}
			if (!StringUtil.isEmpty(farm.getFarmer().getSeasonCode())) {
				criteria.add(Restrictions.eq("f.seasonCode", farm.getFarmer().getSeasonCode()));
			}
			if (farm.getFarmer().getBranchId() != null && !StringUtil.isEmpty(farm.getFarmer().getBranchId())) {
				criteria.add(Restrictions.eq("f.branchId", farm.getFarmer().getBranchId()));
			}
			/*
			 * querying with left join ics conversion takes time so when
			 * filtering used join when no ics filter used takes seperate list
			 * for farm and organic status map and set to the object
			 */
			if (!ObjectUtil.isEmpty(farm.getFarmICSConversion())) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					Stream<Object[]> icsMap = list(
							"select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { farm.getFarmICSConversion().iterator().next().getOrganicStatus() }).stream();
					iscList = icsMap.collect(Collectors.toMap(u -> (Long) u[0], u -> u[1].toString()));
					criteria.add(Restrictions.in("id", iscList.keySet()));
				} else {
					Stream<Object[]> icsMap = list(
							"select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0 ,1 ,2)")
									.stream();
					iscList = icsMap.collect(Collectors.toMap(u -> (Long) u[0], u -> u[1].toString()));
					criteria.add(Restrictions.in("id", iscList.keySet()));
				}
			} else {

				Stream<Object[]> icsMap = list(
						"select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where fi.farm is not null and isActive=1")
								.stream();
				iscList = icsMap.collect(Collectors.toMap(u -> (Long) u[0], u -> u[1].toString()));

				/*
				 * String queryString =
				 * "select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where fi.farm is not null and isActive=1"
				 * ; Session ses = getSessionFactory().openSession(); Query
				 * query = ses.createQuery(queryString); List<Long> results =
				 * ((List<Object[]>) query.list()).stream().map(result ->(Long)
				 * result[0]).collect(Collectors.toList());
				 * //criteria.add(Restrictions.in("id", results));
				 */ }
		}
		if (!selectedFarmer.equalsIgnoreCase("") && !StringUtil.isEmpty(selectedFarmer)) {
			if (!ObjectUtil.isEmpty(farm.getFarmer().getId())) {
				criteria.add(Restrictions.eq("f.id", Long.valueOf(selectedFarmer)));
			}
		}
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.isNotNull("longitude"));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "")));
		criteria.add(Restrictions.eq("f.status", 1));
		criteria.add(Restrictions.eq("status", 1));
		criteria.setProjection(pList);
		List<Object[]> list = criteria.list();
		List<Object[]> output = list.stream().map(a -> {
			a = Arrays.copyOf(a, a.length + 1);

			a[a.length - 1] = iscList.containsKey(Long.valueOf(a[6].toString()))
					? iscList.get(Long.valueOf(a[6].toString())) : "0";
			return a;

		}).collect(Collectors.toList());

		list.clear();
		return output;
	}

	@Override
	public void updateFarmerUtzStatus(String farmId, String certificationStatus) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createSQLQuery("UPDATE farmer fa SET fa.UTZ_STATUS=:CERTIFICATION_STATUS WHERE fa.FARMER_ID=:ID");
		query.setParameter("CERTIFICATION_STATUS", certificationStatus);
		query.setParameter("ID", farmId);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public void updateSamithiUtzStatus(String farmerId, String certificationStatus) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createSQLQuery("UPDATE warehouse wh SET wh.UTZ_STATUS=:CERTIFICATION_STATUS WHERE wh.`CODE`=:CODE");
		query.setParameter("CERTIFICATION_STATUS", certificationStatus);
		query.setParameter("CODE", farmerId);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public void editFarmerStatus(long id, int statusCode, String statusMsg) {
		Session session = getSessionFactory().openSession();
		Query query = session
				.createSQLQuery("UPDATE farmer f set f.STATUS_CODE = :code,f.STATUS_MSG=:msg where f.id=:id");
		query.setParameter("code", statusCode);
		query.setParameter("msg", statusMsg);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public List<Object[]> listPeriodicInspectionVillage() {
		String queryString = "SELECT DISTINCT village.CODE,village.NAME FROM periodic_inspection pi INNER JOIN farm farm ON pi.FARM_ID = farm.FARM_CODE INNER JOIN farmer farmer ON farm.FARMER_ID = farmer.ID INNER JOIN village village on farmer.VILLAGE_ID=village.ID";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public int findLandPreparationDetailsByFarmCode(String farmCode, String seasonCode) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"SELECT count(*) FROM PeriodicInspection pi WHERE pi.farmId=:ID and pi.landpreparationCompleted='1' and pi.seasonCode=:SEASON");
		query.setParameter("ID", farmCode);
		query.setParameter("SEASON", seasonCode);

		// query=session.createSQLQuery(queryString);

		int yesCount = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return yesCount;

	}

	@Override
	public PeriodicInspection findperiodicInspectionByBranchIdAndFarmId(String branchId, String farmId) {

		Object[] values = { branchId, farmId };
		PeriodicInspection periodicInspection = (PeriodicInspection) find(
				"FROM PeriodicInspection pi WHERE  pi.branchId = ? AND pi.farmId = ?", values);
		return periodicInspection;
	}

	@Override
	public int findWeedingStatusByCode(String branchId, String Code, String farmCode, String seasonCode) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select count(*) from PeriodicInspection pi where pi.branchId =:branchId and pi.weeding=:code and pi.farmId=:farmCode and pi.seasonCode=:seasonCode");
		query.setParameter("branchId", branchId).setParameter("code", Code).setParameter("farmCode", farmCode)
				.setParameter("seasonCode", seasonCode);

		int occurence = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return occurence;
	}

	@Override
	public int findUsageCountforFertilizerFromCultivationDetails(String branchId, String Code, String farmCode,
			Long type, String seasonCode) {

		/*
		 * Long count = (Long)
		 * find("select count(*) from Cultivation c inner join c.cultivationDetails  cd where c.branchId=? and cd.type=? and cd.usageLevel=? and c.farmId=?"
		 * , new Object[]{branchId,type,Code,farmCode});
		 */

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select count(*) from CultivationDetail  cd where cd.type=:type and cd.usageLevel=:code and cd.cultivation.farmId=:farmCode and cd.cultivation.currentSeasonCode=:seasonCode and cd.cultivation.branchId=:branchId");
		query.setParameter("code", Code).setParameter("farmCode", farmCode).setParameter("type", type)
				.setParameter("seasonCode", seasonCode).setParameter("branchId", branchId);
		int occurence = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return occurence;
	}

	@Override
	public void updateFarmerRevisionNoAndBasicInfo(long farmerId, Long revisionNo, int basicInfo) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"UPDATE Farmer fa SET fa.revisionNo=:FARMER_REV_NO,fa.basicInfo=:BASIC_INFO WHERE fa.id=:ID");
		query.setParameter("ID", farmerId);
		query.setParameter("FARMER_REV_NO", revisionNo);
		query.setParameter("BASIC_INFO", basicInfo);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public int findPickingStatusByCode(String branchId, String Code, String farmCode, String seasonCode) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select count(*) from PeriodicInspection pi where pi.branchId =:branchId and pi.picking=:code and pi.farmId=:farmCode and pi.seasonCode=:seasonCode");
		query.setParameter("branchId", branchId).setParameter("code", Code).setParameter("farmCode", farmCode)
				.setParameter("seasonCode", seasonCode);

		int occurence = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return occurence;
	}

	public Farm findFarmByFarmerId(long farmerId) {

		return (Farm) find("FROM Farm fm WHERE fm.farmer.id = ?", farmerId);
	}

	@Override
	public List<Object[]> listFarmByFarmerrId(String farmerId) {

		Object[] values = { farmerId };
		List<Object[]> farmList = list("Farm fm where fm.farmer.id = ? ", values);
		return farmList;
	}

	@Override
	public List<Object[]> listFarmerInfoByCultivation(String seasonCode) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT f.FIRST_NAME,f.last_name,v.name,f.farmer_id,v.code FROM `farmer` f  join cultivation cp on cp.FARMER_ID =f.FARMER_id  and f.SEASON_CODE=:SEASON and cp.TXN_TYPE='2' join village v on v.id = f.village_id";
		Query query = sessions.createSQLQuery(queryString);
		query.setParameter("SEASON", seasonCode);
		List<Object[]> result = query.list();
		sessions.flush();
		sessions.close();
		return result;

	}

	@Override
	public List<Object[]> listFarmerInformationByCultivation() {
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT f.FIRST_NAME,f.last_name,v.name,f.farmer_id,v.code FROM `farmer` f  join cultivation cp on cp.FARMER_ID =f.FARMER_id join village v on v.id = f.village_id";
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		sessions.flush();
		sessions.close();
		return result;

	}

	@Override
	public List<Object[]> listFarmerInfoByDistribution() {
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT f.farmer_id,f.FIRST_NAME,f.last_name,v.name FROM `farmer` f  join Distribution cp on cp.FARMER_ID =f.FARMER_id join village v on v.id = f.village_id";
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		sessions.flush();
		sessions.close();
		return result;

	}

	@Override
	public List<Object[]> listFarmerInfoByProcurement() {

		List<Object[]> result = list(
				"SELECT DISTINCT f.farmerId,f.firstName FROM  Farmer f where f.farmerId in (select t.farmerId from AgroTransaction t)");

		return result;
	}

	@Override
	public List<Object[]> listFarmerInfoByCropHarvest() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT ch.farmerId,ch.firstName,ch.lastName FROM  Farmer ch where ch.farmerId in (select f.farmerId from CropHarvest f)");
		return result;
	}

	@Override
	public List<Object[]> listFarmerInfoByCropSale() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT ch.farmerId,ch.firstName,ch.lastName FROM  Farmer ch where ch.farmerId in (select f.farmerId from CropSupply f)");
		return result;
	}

	@Override
	public List<ResearchStation> listResearchStationByRevNo(Long revNo) {
		Object[] values = { revNo, Farmer.Status.ACTIVE.ordinal() };
		return list("FROM ResearchStation rs WHERE rs.revisionNo > ? AND rs.status = ? order by rs.revisionNo DESC",
				values);
	}

	public ResearchStation findResearchStation(long id) {

		ResearchStation researchStation = (ResearchStation) find("FROM ResearchStation c WHERE c.id = ?", id);

		return researchStation;
	}

	@Override
	public List<Object[]> listFarmInfoByCropHarvest() {
		// TODO Auto-generated method stub
		List<Object[]> result = list("SELECT DISTINCT ch.farmCode,ch.farmName FROM  CropHarvest ch");
		return result;
	}

	@Override
	public List<CultivationDetail> findCultivationDetailsByCultivationIdAndSession(long id) {
		return list("FROM CultivationDetail cd WHERE cd.cultivation.id=?", id);
	}

	@Override
	public List<Cultivation> findCostOfCultivationsByFarmerCodeAndSeason(String farmerId, String farmCode,
			String seasonCode) {
		Object[] values = { farmerId, farmCode, seasonCode };
		// TODO Auto-generated method stub
		return list(
				"FROM Cultivation c WHERE farmerId =? and farmId=? and c.currentSeasonCode=? ORDER BY c.farmId  ASC",
				values);
	}

	@Override
	public List<Object[]> listOfVillageByCultivation() {
		List<Object[]> result = list(
				"SELECT DISTINCT f.village.code,f.village.name FROM  Farmer f where f.farmerId in (select c.farmerId from Cultivation c)");
		return result;
	}

	@Override
	public Integer findFarmsCountByBranch(String branchId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select f.farms.size from Farmer f  where f.farmer.branchId=:branchId and f.status=1");

		query.setParameter("branchId", branchId);

		Integer val = ((Long) query.uniqueResult()).intValue();

		return val;
	}

	@Override
	public Integer findFarmCountByMonth(Date sDate, Date eDate) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select count(*) from Farm f where f.farmer.createdDate BETWEEN :startDate AND :endDate and    f.status=1  ");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public Integer findFarmsCount() {

		Session session = getSessionFactory().getCurrentSession();
		Long count = ((Long) session.createQuery("select COALESCE(SUM(f.farms.size),0) from Farmer f WHERE f.status=1").uniqueResult());
		return count == null ? 0 : count.intValue();
	}

	@Override
	public Integer findFarmsCropCountByBranch(String branchId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select count(*) from FarmCrops where status=1 and branchId=:branchId");

		query.setParameter("branchId", branchId);

		Integer val = ((Long) query.uniqueResult()).intValue();

		return val;
	}

	@Override
	public Integer findFarmCropCountByMonth(Date sDate, Date eDate) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select count(*) from FarmCrops fc where fc.status=1 and fc.farm.farmer.createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public Integer findFarmCropCount() {

		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session
				.createQuery("select COUNT(DISTINCT fc.procurementVariety.id) from FarmCrops fc where fc.status=1")
				.uniqueResult()).intValue();
	}

	public String findFarmTotalLandAreaCount() {

		Session session = getSessionFactory().getCurrentSession();
		return ((String) session
				.createQuery(
						"select COALESCE(sum(fm.farmDetailedInfo.totalLandHolding),'0') from Farmer f join f.farms fm where fm.status=1 and f.statusCode = 0")
				.uniqueResult());
	}

	@Override
	public Cow findCowByCowId(long id) {
		return (Cow) find("FROM Cow cw WHERE cw.id = ?", id);
	}

	@Override
	public List<Cow> findByCows(String farmCode) {
		// TODO Auto-generated method stub
		List<Cow> cowList = list("FROM Cow c where c.farm.farmCode= ? ", farmCode);
		return cowList;
	}

	@Override
	public ResearchStation findResearchStationId(String researchStationId) {
		// TODO Auto-generated method stub
		ResearchStation researchStation = (ResearchStation) find("FROM ResearchStation c WHERE c.researchStationId = ?",
				researchStationId);

		return researchStation;
	}

	@Override
	public Cow findCowId(long id) {
		// TODO Auto-generated method stub
		return (Cow) find("FROM Cow c WHERE c.id = ?", id);
	}

	@Override
	public List<Calf> listOfCalfs(long cowId) {
		// TODO Auto-generated method stub
		List<Calf> calfList = list("FROM Calf ca where ca.cow.id= ? ", cowId);
		return calfList;
	}

	@Override
	public HousingInfo findByHousingInfo(long farmId) {
		// TODO Auto-generated method stub
		return (HousingInfo) find("FROM HousingInfo hi where hi.farm.id= ? ", farmId);
	}

	@Override
	public HousingInfo findByHousingId(Long housingId) {
		// TODO Auto-generated method stub
		return (HousingInfo) find("FROM HousingInfo hi WHERE hi.id= ?", housingId);
	}

	@Override
	public Cow findByCowId(String cowId) {
		// TODO Auto-generated method stub
		return (Cow) find("FROM Cow c WHERE c.cowId = ?", cowId);
	}

	@Override
	public Date findByLastInspDate(String cowId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT lastInspDate from CowInspection where cowId=:cowId");
		query.setParameter("cowId", cowId);
		if (query.list().size() > 0) {
			Date val = (Date) query.list().get(0);
			return val;
		}
		return null;
	}

	@Override
	public List<Object[]> findByCowInspFarmer() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT  f.farmerId,f.firstName FROM  Farmer f where f.farmerId in (select ci.farmerId from CowInspection ci where ci.farmerId IS NOT NULL) group by farmerId");
		return result;
	}

	@Override
	public List<Object[]> findByCowInspFarm() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT fa.id,fa.farmName FROM  Farm fa where fa.id in (select ci.farm.id from CowInspection ci where ci.farm.id IS NOT NULL) group by fa.id");
		return result;
	}

	@Override
	public List<Object[]> findByResearchStation() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT  rs.id,rs.name FROM  ResearchStation rs where rs.id in (select ci.researchStation.id from CowInspection ci where ci.researchStation.id IS NOT NULL) group by rs.id");
		return result;
	}

	@Override
	public List<String> findByCowList() {
		try {
			List<String> result = list("SELECT ci.cowId FROM CowInspection ci group by ci.cowId");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CowInspection findCowInspectionById(Long id) {
		return (CowInspection) find("FROM CowInspection c WHERE c.id = ?", id);
	}

	@Override
	public byte[] findCowInspectionCowVoiceById(Long id) {
		// TODO Auto-generated method stub
		return (byte[]) find("SELECT ci.audio FROM CowInspection ci WHERE ci.id=?", id);
	}

	@Override
	public Integer findCowCount() {
		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery("select count(*) from Cow ").uniqueResult()).intValue();
	}

	@Override
	public Integer findCowCountByMonth(Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select count(*) from Cow WHERE createdDate BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public void updateResearchStatRevisionNo(long id, long revisionNumber) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE ResearchStation rs SET rs.revisionNo=:RS_REV_NO WHERE rs.id=:ID");
		query.setParameter("RS_REV_NO", revisionNumber);
		query.setParameter("ID", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<Object[]> findByResearchStationList() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT  rs.researchStationId,rs.name FROM  ResearchStation rs where rs.researchStationId in (select cf.researchStationId from CostFarming cf where cf.researchStationId IS NOT NULL) group by rs.id");
		return result;
	}

	@Override
	public List<CostFarming> findByCowList(long id) {
		// TODO Auto-generated method stub
		return list("FROM CostFarming cf where cf.cow.id=?", id);
	}

	@Override
	public List<Object[]> findFarmInfoByFarmerId(Long id) {
		List<Object[]> result = list(
				"SELECT f.id,f.farmCode,f.farmId,f.farmName FROM Farm f WHERE f.farmer.id=?  and  f.status=1   ORDER BY f.farmName ASC",
				id);
		return result;
	}

	@Override
	public List<CostFarming> listCostFarming() {

		return list("FROM CostFarming cf group by cf.farmerId,cf.cow.id,cf.researchStationId ");
	}

	@Override
	public List<Object> findMilkingCountByCow(String isMilking) {
		// TODO Auto-generated method stub
		return list("select count(*) as count from CowInspection ci where ci.isMilkingCow=? GROUP BY ci.cowId",
				isMilking);
	}

	@Override
	public List<Object> listCowCountByVillage() {
		// TODO Auto-generated method stub
		return list(
				"select count(*) as count,c.farm.farmer.village.name as village from Cow c GROUP BY c.farm.farmer.village.name");
	}

	@Override
	public List<Object> listCowCountByRS() {
		// TODO Auto-generated method stub
		return list(
				"select count(*) as count,c.researchStation.name as rsName from Cow c GROUP BY c.researchStation.name");
	}

	@Override
	public List<Object> listCowCountByDiseaseName() {
		// TODO Auto-generated method stub
		return list(
				"select count(*) as count,ci.diseaseName as diseaseName from CowInspection ci GROUP BY ci.diseaseName");
	}

	@Override
	public List<Object> listTotalFarmAcreByVillage() {

		return list(
				"SELECT SUM(fd.totalLandHolding) as Total,SUM(fd.proposedPlantingArea) as Proposed,f.village.name FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f  where  fa.status=1   GROUP BY f.village.name");
	}

	@Override
	public List<Object> findByCowCost() {
		// TODO Auto-generated method stub
		return list(
				"SELECT SUM(cf.housingCost) as HousingCost,SUM(cf.feedCost) as FeedCost,SUM(cf.treatementCost) as TreatCost,SUM(cf.otherCost) as OtherCost"
						+ "  FROM CostFarming cf");

	}

	@Override
	public List<CowInspection> findByCowList(String id) {
		// TODO Auto-generated method stub
		return list("FROM CowInspection ci where ci.cowId=?", id);
	}

	public Farm findFarmByFarmId(String farmId) {

		return (Farm) find("FROM Farm fm WHERE fm.farmId  = ?", farmId);
	}

	@Override
	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo) {

		Object[] values = { id, revisionNo, ESEAccount.ACTIVE, Farmer.Status.ACTIVE.ordinal(),
				ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"SELECT c.farmer.id,COALESCE(c.farmer.farmerId,''), COALESCE(c.farmer.farmerCode,''),COALESCE(c.farmer.firstName,''),COALESCE(c.farmer.lastName,''),COALESCE(c.farmer.village.code,''),COALESCE(c.farmer.samithi.code,''),COALESCE(c.farmer.isCertifiedFarmer,''),COALESCE(c.farmer.certificationType,''),COALESCE(c.farmer.farmersCodeTracenet,''),COALESCE(c.farmer.fpo,''),COALESCE(c.farmer.utzStatus,''),c.farmer.revisionNo,c.farmer.farms.size,c.farmer.seasonCode FROM Contract c WHERE c.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=?) AND  c.farmer.revisionNo > ? AND c.account.status=? AND c.farmer.status=?  AND c.farmer.statusCode = ? order by c.farmer.revisionNo DESC",
				values);
	}

	@Override
	public List<Object[]> listFarmFieldsByFarmerId(List<Long> farmerId) {

		Session session = getSessionFactory().getCurrentSession();
		/*
		 * Query query = session .createQuery(
		 * "SELECT f.id,COALESCE(f.farmCode,''),COALESCE(f.farmName,''),COALESCE(f.farmId,''),COALESCE(f.farmDetailedInfo.lastDateOfChemicalApplication,''),COALESCE(f.isVerified,'0'),f.farmer.farmerId,f.cows.size,f.farmICSConversion.size,f.farmer.id from Farm f where f.farmer.id in (:farmerId)  and  f.status=1  "
		 * )
		 */
		Query query = session.createQuery(
				"SELECT f.id,COALESCE(f.farmCode,''),COALESCE(f.farmName,''),COALESCE(f.farmId,''),COALESCE(f.farmDetailedInfo.lastDateOfChemicalApplication,''),COALESCE(f.isVerified,'0'),f.farmer.farmerId,f.farmICSConversion.size,f.farmer.id from Farm f where f.farmer.id in (:farmerId)  and  f.status=1  ")

				.setParameterList("farmerId", farmerId);
		List result = query.list();

		return result;

	}

	@Override
	public List<Object[]> listFarmCropsFieldsByFarmerId(Long farmId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery(
						"SELECT f.id,COALESCE(f.procurementVariety.code,''),COALESCE(f.procurementVariety.procurementProduct.code,''),COALESCE(f.seedSource,''),COALESCE(f.seedQtyCost,''),COALESCE(f.estimatedYield,''),COALESCE(f.cropSeason.code,''),COALESCE(f.cropCategory,''),COALESCE(f.farm.farmCode,''),COALESCE(f.farm.farmer.farmerId,''),COALESCE(f.riskAssesment,''),COALESCE(f.seedTreatmentDetails,''),COALESCE(f.otherSeedTreatmentDetails,''),COALESCE(f.stapleLength,''),COALESCE(f.seedQtyUsed,''),COALESCE(f.type,''),COALESCE(f.cropCategoryList,''),COALESCE(f.cultiArea,''),COALESCE(f.sowingDate,''),COALESCE(f.estimatedHarvestDate,''),COALESCE(f.interType,''),COALESCE(f.interAcre,''),COALESCE(f.totalCropHarv,''),COALESCE(f.grossIncome,'') from FarmCrops f where f.farm.id = :farmerId  and  f.status=1 ")
				.setParameter("farmerId", farmId);
		List result = query.list();

		return result;

	}

	@Override
	public List<Cow> listCowFieldsByFarmId(Long farmId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM Cow cw WHERE cw.farm.id =   :farmerId").setParameter("farmerId",
				farmId);
		List result = query.list();
		return result;
	}

	@Override
	public List<Object[]> listFarmIcsByFarmId() {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"SELECT ics.farm.id,ics.icsType FROM FarmIcsConversion ics WHERE ics.farm.id  IS NOT NULL and  ics.farm.status=1 and ics.isActive=1 ");
		List result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileIdOpt(String farmerProfileId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM InterestCalcConsolidated icc WHERE icc.farmerProfileId = :farmerId")
				.setParameter("farmerId", farmerProfileId);
		InterestCalcConsolidated result = (InterestCalcConsolidated) query.uniqueResult();

		return result;

	}

	public Integer findMinVarietyIntervalDays() {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select min(harvestDays) from ProcurementVariety");

		Integer val = (Long.valueOf(query.uniqueResult().toString())).intValue();
		session.flush();
		session.close();
		return val;
	}

	@Override
	public List<Object[]> findFarmCropProductList() {
		// TODO Auto-generated method stub
		return list(
				"SELECT fc.procurementVariety.code,fc.procurementVariety.procurementProduct.name,fc.procurementVariety.name FROM FarmCrops fc where  fc.status=1 group by fc.procurementVariety.id");
	}

	@Override
	public Object[] findFarmFarmerAndYieldCount(String varietyCode) {
		// TODO Auto-generated method stub
		Object[] obj = null;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(fc.farm.farmer.id),SUM(fc.cultiArea),SUM(fc.procurementVariety.yield) FROM FarmCrops fc where fc.procurementVariety.code='"
				+ varietyCode + "' and fc.status=1 group by fc.procurementVariety.id";
		Query query = sessions.createQuery(queryString);
		if (query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);

		}
		sessions.flush();
		sessions.close();
		return obj;
	}

	@Override
	public List<Object[]> findSowingDateAndInterval(String vCode) {

		return list(
				"SELECT fc.procurementVariety.harvestDays,fc.sowingDate,fc.procurementVariety.noDaysToGrow,fc.id FROM FarmCrops fc where fc.procurementVariety.code=? and fc.status=1",
				vCode);
	}

	@Override
	public List<Cultivation> findCOCByFarmerIdFarmIdCropCodeSeason(String farmerId, String farmId, String cropCode,
			String seasonCode) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"FROM  Cultivation c WHERE c.farmerId=:farmerId AND c.farmId=:farmId AND c.cropCode=:cropCode ORDER BY c.id ASC");
		query.setParameter("farmerId", farmerId);
		query.setParameter("farmId", farmId);
		query.setParameter("cropCode", cropCode);
		// query.setParameter("currentSeasonCode", seasonCode);

		List result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public long listOfFarmerCountBySamithiId(long id) {

		long totalFarmers = 0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM FARMER WHERE SAMITHI_ID ='" + id
				+ "' AND VILLAGE_ID IS NOT NULL AND STATUS_CODE = '" + ESETxnStatus.SUCCESS.ordinal() + "' ";
		Query query = sessions.createSQLQuery(queryString);

		int count = Integer.valueOf(query.list().get(0).toString());

		if (count > 0) {
			totalFarmers = count;
		}
		sessions.flush();
		sessions.close();
		return totalFarmers;
	}

	@Override
	public List<Object[]> findCropProductBySeason(String seasonCode, String varietyCode) {
		// TODO Auto-generated method stub
		if (!StringUtil.isEmpty(varietyCode)) {
			Object[] values = { seasonCode, varietyCode };
			return list(
					"SELECT fc.procurementVariety.harvestDays,fc.sowingDate,fc.procurementVariety.noDaysToGrow,fc.id FROM FarmCrops fc where fc.cropSeason.code=? and fc.procurementVariety.code=? and fc.status=1",
					values);
		} else {
			return list(
					"SELECT fc.procurementVariety.harvestDays,fc.sowingDate,fc.procurementVariety.noDaysToGrow,fc.id FROM FarmCrops fc where fc.cropSeason.code=? and fc.status=1",
					seasonCode);

		}
	}

	@Override
	public Object[] findFarmFarmerAndYieldCountBySeason(String seasonCode) {
		// TODO Auto-generated method stub
		Object[] obj = null;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(fc.farm.farmer.id),SUM(fc.cultiArea),SUM(fc.procurementVariety.yield) FROM FarmCrops fc where fc.cropSeason.code='"
				+ seasonCode + "' and fc.status=1 group by fc.cropSeason.code";
		Query query = sessions.createQuery(queryString);
		if (query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);

		}
		sessions.flush();
		sessions.close();
		return obj;
	}

	@Override
	public List<FarmCrops> listOfVarietyByCropTypeFarmCodeAndCrop(String selectedCropType, String selectedFarm,
			String selectedCropName) {
		Object values[] = { Integer.valueOf(selectedCropType), selectedFarm, Long.valueOf(selectedCropName) };
		return list(
				"FROM FarmCrops fc WHERE fc.cropCategory = ? AND fc.farm.farmCode=? and fc.status=1  AND fc.procurementVariety.procurementProduct.id=?",
				values);

	}

	@Override
	public String findFarmTotalLandAreaCount(String BranchId) {

		Session session = getSessionFactory().getCurrentSession();
		return ((String) session
				.createQuery(
						"select COALESCE(sum(fdi.farmDetailedInfo.totalLandHolding),'0') from Farm  fdi where fdi.farmer.branchId =:branch  and  fdi.status=1  ")
				.setParameter("branch", BranchId).uniqueResult());
	}

	@Override
	public List<PeriodicInspectionData> listDiseaseById(Long inspId) {

		return list("FROM PeriodicInspectionData pid where pid.periodicInspection.id=? AND pid.type='DISEASE'", inspId);
	}

	@Override
	public List<Cultivation> listCultivationExpenses() {
		return list("FROM Cultivation cul");
	}

	@Override
	public List<Object[]> findFarmerCountWithPhotos(String branchId) {
		if (!StringUtil.isEmpty(branchId)) {
			return list("SELECT  f.createdDate From Farmer f WHERE f.imageInfo is not null and f.status=1 and f.statusCode=0 and f.branchId=?", branchId);
		} else {
			return list("SELECT  f.createdDate From Farmer f WHERE f.imageInfo is not null and f.status=1 and f.statusCode=0");
		}
	}

	@Override
	public List<Object[]> findFarmCountWithPhotos(String branchId) {
		if (!StringUtil.isEmpty(branchId)) {
			return list(
					"SELECT  f.farmer.createdDate From Farm f WHERE f.photo is not null and f.farmer.branchId=?  and  f.status=1  and f.farmer.status=1 and f.farmer.statusCode=0",
					branchId);
		} else {
			return list("SELECT  f.farmer.createdDate From Farm f WHERE f.photo is not null  and  f.status=1  and f.farmer.status=1 and f.farmer.statusCode=0");
		}
	}

	@Override
	public List<Object[]> findFarmCountWithGPSTag(String branchId) {
		List<Object[]> farmCountWithGPSTags;
		if (!StringUtil.isEmpty(branchId)) {
			farmCountWithGPSTags = list(
					"SELECT  f.farmer.createdDate From Farm f WHERE exists elements(f.coordinatesMap) and f.farmer.branchId=?  and  f.status=1  and f.farmer.status=1 and f.farmer.statusCode=0",
					branchId);
		} else {
			farmCountWithGPSTags = list(
					"SELECT  f.farmer.createdDate From Farm f WHERE exists elements(f.coordinatesMap)  and  f.status=1  and f.farmer.status=1 and f.farmer.statusCode=0");
		}
		return farmCountWithGPSTags;
	}

	public long findFarmerCropProdAnswerByFarmerIdAndCategoryCode(String farmerId, String code) {

		Object[] values = { farmerId, code };
		Long id = (Long) find(
				"select max(fcp.id) from FarmerCropProdAnswers fcp WHERE fcp.farmerId=? and categoryCode=? ", values);
		return id != null ? id : 0;
	}

	@Override
	public List<Object> findFarmerCountByICconversion(String selectedBranch, Integer selectedYear,
			String selectedSeason, String selectedGender, String selectedState) {
		/*
		 * Object[] values = { selectedBranch, selectedYear, (selectedYear - 1)
		 * }; Object[] yearValues = { selectedYear, (selectedYear - 1) }; if
		 * (StringUtil.isEmpty(selectedBranch)) { return list(
		 * "select fc.icsType,count(f.id) FROM FarmIcsConversion fc INNER JOIN fc.farm fm INNER JOIN fm.farmer f WHERE Year(f.createdDate)=? OR Year(f.createdDate)=? GROUP BY fc.icsType"
		 * , yearValues); } else { return list(
		 * "select fc.icsType,count(f.id) FROM FarmIcsConversion fc INNER JOIN fc.farm fm INNER JOIN fm.farmer f WHERE f.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?) GROUP BY fc.icsType"
		 * , values); }
		 */

		String groupBy = " GROUP BY fic.icsType";
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select fic.icsType,count(f.id) FROM FarmCrops fc INNER JOIN  fc.farm fm INNER JOIN"
				+ " fm.farmICSConversion fic INNER JOIN  fm.farmer f WHERE f.status=1 and fm.status=1 and fc.status=1 and "
				+ " fic.icsType!=-1 AND fc.preHarvestProd is not null";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode=:year";
			params.put("year", selectedSeason);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		hqlQuery += groupBy;
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();

	}

	@Override
	public Integer findTotalFarmerCount(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedGender, String selectedState) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(object3) ? "" : object3.toString();

		Session session = getSessionFactory().getCurrentSession();
		int count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "";
		if (!StringUtil.isEmpty(selectedStaple)) {
			hqlQuery = "select count(*) FROM Farmer f INNER JOIN f.farms fa INNER JOIN fa.farmCrops fc WHERE f.statusCode=0 AND f.status=1";
			hqlQuery += " AND fc.stapleLength=:selectedStaple";
			params.put("selectedStaple", selectedStaple);
		} else {
			hqlQuery = "select count(*) FROM Farmer f WHERE f.statusCode=0 AND f.status=1";
		}
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			if (StringUtil.isEmpty(currentBranch)) {
				hqlQuery += " AND f.village.city.locality.state.name =:selectedState";
				params.put("selectedState", selectedState);
			} else {
				hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
				params.put("selectedState", Long.valueOf(selectedState));
			}
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			count = Integer.valueOf(qry);
		}
		return count;

	}

	@Override
	public Integer findTotalFarmCount(String selectedBranch, Integer selectedYear) {
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedBranch)) {
			return ((Long) session
					.createQuery(
							"select count(*) FROM Farm f WHERE Year(f.farmer.createdDate)=:year OR Year(f.farmer.createdDate)=:prevYear  and  f.status=1 ")
					.setParameter("year", selectedYear).setParameter("prevYear", (selectedYear - 1)).uniqueResult())
							.intValue();
		} else {
			return ((Long) session
					.createQuery(
							"select count(*) FROM Farm f WHERE f.farmer.branchId =:branch AND (Year(f.farmer.createdDate)=:year OR Year(f.farmer.createdDate)=:prevYear)  and  f.status=1  ")
					.setParameter("branch", selectedBranch).setParameter("year", selectedYear)
					.setParameter("prevYear", (selectedYear - 1)).uniqueResult()).intValue();
		}
	}

	/*
	 * @Override public List<CropHarvest> findCropHarvestByYearAndBranch(String
	 * selectedBranch, Integer selectedYear) { Object[] values = {
	 * selectedBranch, selectedYear,(selectedYear-1)}; Object[] yearValues =
	 * {selectedYear,(selectedYear-1)}; if (StringUtil.isEmpty(selectedBranch))
	 * { return
	 * list("From CropHarvest ch WHERE (Year(ch.harvestDate)=? OR Year(ch.harvestDate)=?)"
	 * , yearValues); } else { return
	 * list("From CropHarvest ch WHERE ch.branchId=? AND (Year(ch.harvestDate)=? OR Year(ch.harvestDate)=?)"
	 * , values); } }
	 * 
	 * @Override public List<CropSupply> findCropSupplyByYearAndBranch(String
	 * selectedBranch, Integer selectedYear) { Object[] values = {
	 * selectedBranch, selectedYear,(selectedYear-1) }; Object[] yearValues = {
	 * selectedYear,(selectedYear-1) }; if (StringUtil.isEmpty(selectedBranch))
	 * { return
	 * list("From CropSupply cs WHERE Year(cs.dateOfSale)=? OR Year(cs.dateOfSale)=?"
	 * , yearValues); } else { return
	 * list("From CropSupply cs WHERE cs.branchId=? AND (Year(cs.dateOfSale)=? OR Year(cs.dateOfSale)=?)"
	 * , values); } }
	 */

	@Override
	public List<FarmerField> listRemoveFarmerFields() {
		Object[] values = { FarmerField.INACTIVE };
		return list("From FarmerField ff WHERE ff.status=? )", values);
	}

	@Override
	public List<FarmField> listRemoveFarmFields() {
		Object[] values = { FarmerField.ACTIVE };
		return list("From FarmField ff WHERE ff.status=?", values);
	}

	@Override
	public List<Object[]> listFarmInfoByFarmerId(long id) {
		return list("SELECT f.id,f.farmCode,f.farmName from Farm f WHERE f.farmer.id=?  and  f.status=1 ", id);
	}

	@Override
	public List<Object[]> findCountOfFarmerEnrollment(List<String> agent) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT ald.txnType,sum(ald.txnCount) FROM AgentAccessLog al INNER JOIN al.agentAccessLogDetails ald WHERE al.profileId in(:profileId) AND ald.txnType =:txnType");
		query.setParameterList("profileId", agent);
		query.setParameter("txnType", "308");
		List list = query.list();
		/*
		 * session.flush(); session.close();
		 */
		return list;
	}

	@Override
	public List<FarmerField> findFarmerFieldByType(String type) {
		return list("FROM FarmerField ff WHERE ff.type = ?", type);
	}

	@Override
	public List<FarmerField> listFarmerFields() {
		return list("FROM FarmerField ff");
	}

	@Override
	public Object[] findFarmerCodeNameVillageSamithibyFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return (Object[]) find(
				"SELECT fr.farmerCode,fr.firstName,v.name,s.name from Farmer fr INNER JOIN fr.village v INNER JOIN fr.samithi s WHERE fr.farmerId=?",
				farmerId);
	}

	@Override
	public List<Object[]> listFarmerInfoByProductReturn() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT f.farmerId,f.firstName,f.lastName,f.village.name FROM  Farmer f where f.farmerId in (select pr.farmerId from ProductReturn pr)");

		return result;
	}

	@Override
	public Warehouse findSamithiByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return (Warehouse) find("SELECT s FROM Farmer f INNER JOIN f.samithi s WHERE f.farmerId=?", farmerId);
	}

	@Override
	public List<Object> listTotalFarmAcreByFpo() {

		// return list("SELECT SUM(fd.totalLandHolding) as
		// Total,SUM(fd.proposedPlantingArea) as Proposed FROM Farm fa INNER
		// JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f GROUP BY f.fpo");
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"SELECT SUM(fdi.TOTAL_LAND_HOLDING),SUM(fdi.PROPOSED_PLANTING_AREA),cv.NAME FROM farm fm LEFT JOIN farm_detailed_info fdi ON fm.FARM_DETAILED_INFO_ID = fdi.ID LEFT JOIN farmer f ON  fm.FARMER_ID=f.ID LEFT JOIN catalogue_value cv ON cv.code=f.fpo WHERE f.fpo <> '' AND fdi.TOTAL_LAND_HOLDING <> '' AND fdi.PROPOSED_PLANTING_AREA <> ''  and  fdi.status=1  GROUP BY f.FPO  LIMIT 5");
		List list = query.list();
		/*
		 * session.flush(); session.close();
		 */
		return list;
	}

	@Override
	public List<PMT> lisReceiptNumberList() {

		return list("FROM PMT");
	}

	@Override
	public Cultivation findCultivationByCultivationId(long cultiId) {
		// TODO Auto-generated method stub
		return (Cultivation) find("FROM Cultivation c where c.id=?", cultiId);
	}

	@Override
	public ProcurementProduct findCropByCropCode(String cropCode) {
		// TODO Auto-generated method stub
		return (ProcurementProduct) find("FROM ProcurementProduct p where p.code=?", cropCode);
	}

	@Override
	public List<Object[]> listFarmerIDAndNameByFarmerID(String farmerId) {
		return list("SELECT DISTINCT f.farmerId,f.firstName from Farmer f where f.farmerId=?", farmerId);
	}

	@Override
	public List<FarmCrops> findFarmCropsByFarmerIdAndProcId(long id, long proId) {

		/*
		 * Object values[]={id,proId}; return (FarmCrops)
		 * find("FROM FarmCrops fc INNER JOIN fc.procurementVariety pv  INNER JOIN fc.farm farm INNER JOIN farm.farmer f where f.id=? and pv.procurementProduct.id=? "
		 * ,values);
		 */
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT fc FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.id=:farmerId AND fc.status=1 and  fc.procurementVariety.procurementProduct.id=:productId");
		query.setParameter("farmerId", id);
		query.setParameter("productId", proId);
		List list = query.list();
		return list;

	}

	public void updatePeriodicInspectionData(String farmId, String cropCode, String category, String type) {

		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery(
				"UPDATE periodic_inspection_data_list pid INNER JOIN periodic_inspection p on pid.PERIODIC_INSPECTION_ID=p.id "
						+ "SET pid.COC_DONE='Y' WHERE p.CROP_CODE=:cropCode AND p.FARM_ID=:farmCode AND pid.I_TYPE=:type AND pid.CATALOUGE_VALUE=:category AND pid.COC_DONE=:cocDone");
		query.setParameter("cropCode", cropCode);
		query.setParameter("category", category);
		query.setParameter("type", type);
		query.setParameter("cocDone", "N");
		query.setParameter("farmCode", farmId);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo, List<String> branch) {

		Session session = getSessionFactory().getCurrentSession();
		/*
		 * Query query = session.createQuery(
		 * "SELECT c.farmer.id,COALESCE(c.farmer.farmerId,''), COALESCE(c.farmer.farmerCode,''),COALESCE(c.farmer.firstName,''),COALESCE(c.farmer.lastName,''),COALESCE(c.farmer.village.code,''),COALESCE(c.farmer.samithi.code,''),COALESCE(c.farmer.isCertifiedFarmer,''),COALESCE(c.farmer.certificationType,''),COALESCE(c.farmer.farmersCodeTracenet,''),COALESCE(c.farmer.fpo,''),COALESCE(c.farmer.utzStatus,''),c.farmer.revisionNo,c.farmer.farms.size,COALESCE(c.farmer.fingerPrint,''),COALESCE(c.farmer.traceId,'') FROM Contract c WHERE c.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  c.farmer.revisionNo > :revisionNo  AND c.account.status=:accSttaus AND c.farmer.status=:status  AND c.farmer.statusCode = :status1 and c.farmer.branchId in (:branches) order by c.farmer.revisionNo DESC"
		 * );
		 */
		Query query = session.createQuery(
				"SELECT f.id,COALESCE(f.farmerId,''), COALESCE(f.farmerCode,''),COALESCE(f.firstName,''),COALESCE(f.lastName,''),COALESCE(f.village.code,''),COALESCE(f.samithi.code,''),COALESCE(f.isCertifiedFarmer,''),COALESCE(f.certificationType,''),COALESCE(f.farmersCodeTracenet,''),COALESCE(f.fpo,''),COALESCE(f.utzStatus,''),f.revisionNo,f.farms.size,COALESCE(f.traceId,''),COALESCE(f.isDisable,''),COALESCE(f.typez,''),f.seasonCode,f.status,f.icsName,f.proofNo,f.mobileNumber,COALESCE(f.city.code,''),COALESCE(f.city.locality.code,''),COALESCE(f.city.locality.state.code,'') FROM Farmer f WHERE f.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  f.revisionNo > :revisionNo   AND f.statusCode in  (:status1)  order by f.revisionNo DESC");
		query.setParameter("id", id);
		query.setParameter("revisionNo", revisionNo);
		// query.setParameterList("branches", branch);
		// query.setParameter("accSttaus", ESEAccount.ACTIVE);
		// query.setParameter("status", Farmer.Status.ACTIVE.ordinal());
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List result = query.list();

		return result;

	}

	@Override
	public Integer findTotalFarmerCountByStapleLength(String selectedBranch, int selectedYear,
			List<String> selectedStapleLen) {
		// TODO Auto-generated method stub
		/*
		 * Session session = getSessionFactory().getCurrentSession(); if
		 * (StringUtil.isEmpty(selectedBranch)) { return ((Long) session
		 * .createQuery(
		 * "select count(*) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f  WHERE f.status=1 AND fc.stapleLength in:stapleLen AND (Year(f.createdDate)=:prevYear OR Year(f.createdDate)=:year)"
		 * ) .setParameter("year", selectedYear).setParameterList("stapleLen",
		 * selectedStapleLen) .setParameter("prevYear", (selectedYear -
		 * 1)).uniqueResult()).intValue(); } else { return ((Long) session
		 * .createQuery(
		 * "select count(*) FROM FarmCrops fc INNER JOIN fc.farm fa  INNER JOIN fa.farmer f WHERE f.status=1 AND fc.stapleLength in:stapleLen AND f.branchId =:branch AND (Year(f.createdDate)=:prevYear OR Year(f.createdDate)=:year)"
		 * ) .setParameterList("stapleLen",
		 * selectedStapleLen).setParameter("branch", selectedBranch)
		 * .setParameter("year", selectedYear).setParameter("prevYear",
		 * (selectedYear - 1)).uniqueResult()) .intValue(); }
		 */
		Session session = getSessionFactory().getCurrentSession();
		int count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(*) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f  WHERE f.status=1 and fc.status=1";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedYear) && selectedYear != 0) {
			hqlQuery += "  AND (Year(f.createdDate)=:year";
			params.put("year", selectedYear);

			hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			params.put("prevYear", (selectedYear - 1));
		}

		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND fc.stapleLength in:stapleLen ";
			params.put("stapleLen", selectedStapleLen);
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			count = Integer.valueOf(qry);
		}
		return count;
	}

	@Override
	public List<String> findFarmerIdsByfarmCode(List<String> farmCodes) {
		List<String> list = null;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT f.farmer.farmerId from Farm f where f.farmCode in :fCode and f.farmer.status=1 and f.farmer.statusCode=0");
		query.setParameterList("fCode", farmCodes);
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			list = query.list();
		}
		return list;

	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldAndHarvstByBranch(String selectedBranch, int selectedYear) {
		Object[] values = { selectedBranch, selectedYear, (selectedYear - 1) };
		Object[] yearValues = { selectedYear, (selectedYear - 1) };
		if (StringUtil.isEmpty(selectedBranch)) {
			return list(
					"SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed,fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE (Year(f.createdDate)=? OR Year(f.createdDate)=?) and fc.status=1",
					yearValues);

		} else {
			return list(
					"SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed,fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE fc.farm.farmer.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?) and fc.status=1",
					values);
		}

	}

	@Override
	public String findTotalQtyByFarm(List<String> farmCode) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT sum(ch.totalQty) from CropHarvest ch  WHERE ch.farmCode in :fCode");
		query.setParameterList("fCode", farmCode);
		String val = "0";
		if (!StringUtil.isEmpty(query) && query.uniqueResult() != null) {
			val = String.valueOf(query.uniqueResult());
		}
		return val;
	}

	@Override
	public Double findCottonIncByFarmerCode(List<String> farmerIds, List<String> farmCodes) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT sum(csd.subTotal) from CropSupplyDetails csd"
				+ " Inner join csd.cropSupply cs WHERE cs.farmerId in:fId  and cs.farmCode in:faCode");
		query.setParameterList("fId", farmerIds);
		query.setParameterList("faCode", farmCodes);
		Double val = 0.0;
		if (!StringUtil.isEmpty(query) && query.uniqueResult() != null) {
			val = ((Double) query.uniqueResult());
		}
		return val;

	}

	@Override
	public String findTotalLandByFarmCode(List<String> farmerIds, List<String> farmCodes) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT sum(f.farmDetailedInfo.proposedPlantingArea) from "
				+ "Farm f  WHERE f.farmer.farmerId in:fId  and f.farmCode in:faCode  and  f.status=1 ");
		query.setParameterList("fId", farmerIds);
		query.setParameterList("faCode", farmCodes);
		String val = "0.0";
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = String.valueOf(obj);
		}
		return val;
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldAndStapleByBranch(String selectedBranch, int selectedYear,
			List<String> selectedStapleLen) {

		/*
		 * Session session = getSessionFactory().getCurrentSession();
		 * 
		 * Query query = null;
		 * 
		 * if (StringUtil.isEmpty(selectedBranch)) {
		 * 
		 * query = session.createQuery(
		 * "SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed,fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm fa inner join fa.farmer f WHERE (Year(f.createdDate)=:pveYear OR Year(f.createdDate)=:year) AND fc.stapleLength in:staple"
		 * );
		 * 
		 * } else { query = session.createQuery(
		 * "SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed,fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm fa inner join fa.farmer f WHERE f.branchId=:branch AND (Year(f.createdDate)=:pveYear OR Year(f.createdDate)=:year) AND fc.stapleLength in:staple"
		 * ); query.setParameter("branch", selectedBranch); }
		 * 
		 * query.setParameterList("staple", selectedStapleLen);
		 * 
		 * query.setParameter("pveYear", selectedYear - 1);
		 * query.setParameter("year", selectedYear);
		 * 
		 * return query.list();
		 */
		Session session = getSessionFactory().getCurrentSession();
		int count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed,fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm fa inner join fa.farmer f WHERE f.status=1 and fc.status=1";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedYear) && selectedYear != 0) {
			hqlQuery += "  AND (Year(f.createdDate)=:year";
			params.put("year", selectedYear);

			hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			params.put("prevYear", (selectedYear - 1));
		}

		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND fc.stapleLength in:staple ";
			params.put("staple", selectedStapleLen);
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();
	}

	@Override
	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, String selectedSeason) {
		/*
		 * Object[] values = { selectedBranch, selectedYear, (selectedYear - 1)
		 * }; Object[] yearValues = { selectedYear, (selectedYear - 1) }; if
		 * (StringUtil.isEmpty(selectedBranch)) { return list(
		 * "SELECT fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , yearValues);
		 * 
		 * } else { return list(
		 * "SELECT fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE fc.farm.farmer.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , values); }
		 */

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT fc.farm.farmCode FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE f.status=1 and fc.status=1";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode =:year";
			params.put("year", selectedSeason);

			/*
			 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			 * params.put("prevYear", (selectedYear - 1));
			 */
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}

	@Override
	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear,
			List<String> selectedStapleLen) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().getCurrentSession();

		Query query = null;

		if (StringUtil.isEmpty(selectedBranch)) {

			query = session.createQuery(
					"SELECT fa.farmCode FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE (Year(f.createdDate)=:pveYear OR Year(f.createdDate)=:year) AND fc.stapleLength in:staple and fc.status=1");

		} else {
			query = session
					.createQuery("SELECT fa.farmCode FROM FarmCrops fc INNER JOIN fc.farm fa inner join fa.farmer f "
							+ "WHERE fc.farm.farmer.branchId=:branch AND (Year(f.createdDate)=:pveYear OR Year(f.createdDate)=:year) AND fc.stapleLength  in:staple and fc.status=1");
			query.setParameter("branch", selectedBranch);
		}

		query.setParameterList("staple", selectedStapleLen);

		query.setParameter("pveYear", selectedYear - 1);
		query.setParameter("year", selectedYear);

		return query.list();
	}

	@Override
	public String findTotalLandAcre(String selectedBranch, int selectedYear, String selectedSeason) {
		/*
		 * Object[] values = { selectedBranch, selectedYear, (selectedYear - 1)
		 * }; Object[] yearValues = { selectedYear, (selectedYear - 1) }; if
		 * (StringUtil.isEmpty(selectedBranch)) { return (String) find(
		 * "SELECT SUM(fdf.proposedPlantingArea) as Proposed FROM Farm fa  INNER JOIN fa.farmDetailedInfo fdf INNER JOIN fa.farmer f WHERE (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , yearValues);
		 * 
		 * } else { return (String) find(
		 * "SELECT SUM(fdf.proposedPlantingArea) as Proposed FROM Farm fa INNER JOIN fa.farmDetailedInfo fdf INNER JOIN fa.farmer f WHERE f.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , values); }
		 */
		Session session = getSessionFactory().getCurrentSession();
		String land = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT SUM(fdf.proposedPlantingArea) as Proposed FROM Farm fa  INNER JOIN fa.farmDetailedInfo fdf INNER JOIN fa.farmer f WHERE f.status=1  and  fa.status=1 ";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode=:year";
			params.put("year", selectedSeason);

			/*
			 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			 * params.put("prevYear", (selectedYear - 1));
			 */
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			land = qry;
		}
		return land;

	}

	public List<String> findFarmCodesByCultivation(String selectedBranch, String selectedSeason, String selectedYear) {
		/*
		 * Object[] values = { selectedBranch, selectedYear, (selectedYear - 1)
		 * }; Object[] yearValues = { selectedYear, (selectedYear - 1) }; if
		 * (StringUtil.isEmpty(selectedBranch)) { return list(
		 * "SELECT cu.farmId FROM Cultivation cu  WHERE (Year(cu.expenseDate)=? OR Year(cu.expenseDate)=?)"
		 * , yearValues);
		 * 
		 * } else { return list(
		 * "SELECT cu.farmId FROM Cultivation cu  WHERE  cu.branchId=? AND (Year(cu.expenseDate)=? OR Year(cu.expenseDate)=?)"
		 * , values); }
		 */

		Session session = getSessionFactory().openSession();
		String sqlString;
		Map<String, Object> params = new HashMap<String, Object>();
		sqlString = "SELECT cu.farm_Id FROM Cultivation cu inner join farm fa on fa.farm_Code=cu.farm_ID inner join farmer f on fa.farmer_ID=f.id where cu.id is not null AND fa.status=1 and f.status=1 and f.status_Code=0";
		if (!StringUtil.isEmpty(selectedBranch)) {
			sqlString += " AND cu.branch_Id =:branch ";
			params.put("branch", selectedBranch);
		}
		/*
		 * if (!StringUtil.isEmpty(sDate) && !StringUtil.isEmpty(eDate)) {
		 * hqlQuery += "  AND (Year(cu.expenseDate)=:year";
		 * params.put("year",Integer.valueOf(sDate));
		 * 
		 * hqlQuery += "  OR Year(cu.expenseDate)=:prevYear)";
		 * params.put("prevYear", Integer.valueOf(eDate));
		 * 
		 * }
		 */
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			sqlString += "  AND cu.current_Season_Code=:year";
			params.put("year", selectedSeason);
		}
		if (!StringUtil.isEmpty(selectedYear)) {
			sqlString += " AND (Year(cu.expense_Date)=:prevYear)";
			params.put("prevYear", Integer.valueOf(selectedYear));
		}

		SQLQuery query = session.createSQLQuery(sqlString);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		List<String> result = query.list();
		session.flush();
		session.close();
		return result;

	}

	@Override
	public String findByFarmCode(String farmCode) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT ch.totalQty from CropHarvest ch  WHERE ch.farmCode=:fCode");
		query.setParameter("fCode", farmCode);
		String val = "0";
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = String.valueOf(obj);
		}
		return val;
	}

	@Override
	public double findTotalCottonAreaCount() {

		double totalCottonArea = 0;
		String qry = "";
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT IFNULL(SUM(fc.CULTIVATION_AREA),0) FROM FARM_CROPS fc "
				+ "LEFT OUTER JOIN FARM f ON fc.FARM_ID = f.id " + "LEFT OUTER JOIN FARMER fm ON f.FARMER_ID = fm.id "
				+ "INNER JOIN VILLAGE v ON fm.VILLAGE_ID = v.id " + "INNER JOIN WAREHOUSE w ON fm.SAMITHI_ID = w.id "
				+ "LEFT OUTER JOIN PROCUREMENT_VARIETY pv ON fc.PROCUREMENT_CROPS_VARIETY_ID = pv.id "
				+ "INNER JOIN PROCUREMENT_PRODUCT pp ON pv.PROCUREMENT_PRODUCT_ID = pp.id "
				+ "INNER JOIN HARVEST_SEASON hs ON fc.CROP_SEASON = hs.id WHERE pp.NAME LIKE '%cotton%'  AND fm.STATUS_CODE = '"
				+ ESETxnStatus.SUCCESS.ordinal() + "'";

		Query query = sessions.createSQLQuery(queryString);
		qry = String.valueOf(query.list().get(0));
		double count = Double.valueOf(qry.toString());

		if (count > 0) {
			totalCottonArea = count;
		}
		sessions.flush();
		sessions.close();
		return (double) totalCottonArea;
	}

	@Override
	public double findTotalCottonAreaCountByMonth(Date sDate, Date eDate) {

		double totalCottonArea = 0;
		String qry = "";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery("SELECT  IFNULL(SUM(fc.CULTIVATION_AREA),0) FROM FARM_CROPS fc "
				+ "LEFT OUTER JOIN FARM f ON fc.FARM_ID = f.id " + "LEFT OUTER JOIN FARMER fm ON f.FARMER_ID = fm.id "
				+ "INNER JOIN VILLAGE v ON fm.VILLAGE_ID = v.id " + "INNER JOIN WAREHOUSE w ON fm.SAMITHI_ID = w.id "
				+ "LEFT OUTER JOIN PROCUREMENT_VARIETY pv ON fc.PROCUREMENT_CROPS_VARIETY_ID = pv.id "
				+ "INNER JOIN PROCUREMENT_PRODUCT pp ON pv.PROCUREMENT_PRODUCT_ID = pp.id "
				+ "INNER JOIN HARVEST_SEASON hs ON fc.CROP_SEASON = hs.id WHERE pp.NAME LIKE '%cotton%'  AND fm.STATUS_CODE = '"
				+ ESETxnStatus.SUCCESS.ordinal() + "' AND fm.CREATED_DATE BETWEEN :startDate AND :endDate");
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);

		qry = String.valueOf(query.list().get(0));
		double count = Double.valueOf(qry.toString());

		if (count > 0) {
			totalCottonArea = count;
		}
		sessions.flush();
		sessions.close();
		return (double) totalCottonArea;
	}

	public void updateFarmerStatus(long id) {

		Session session = getSessionFactory().openSession();

		Query query = session.createQuery(
				"UPDATE Farmer fm SET fm.statusCode='1',fm.status='0',fm.statusMsg='FARMER DELETED',fm.revisionNo=:revNo WHERE fm.id = :id");
		query.setParameter("id", id);
		query.setParameter("revNo", DateUtil.getRevisionNumber());
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT SUM(fc.estimatedYield) as Total,cast(SUM(fc.cultiArea) as int) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 and fa.status=1 and fc.status=1 and f.statusCode = 0";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != 0) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", selectedState);
		}
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();

	}
	
	
	
	
	@Override
	public List<Object> findFarmerDetailsByStateAndCropbyFarmerStatus(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender,String selectedStatus) {

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT SUM(fc.estimatedYield) as Total,cast(SUM(fc.cultiArea) as int) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE  fa.status=1 and fc.status=1 and f.statusCode = 0";
		
		if (!StringUtil.isEmpty(selectedStatus)) {
			hqlQuery += " AND f.status =:status ";
			params.put("status",Integer.valueOf(selectedStatus));
		}else{
			hqlQuery += " AND f.status =1 ";
		}
		
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != 0) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", selectedState);
		}
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();

	}

	@Override
	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		
	Session session = getSessionFactory().getCurrentSession();
	Map<String, Object> params = new HashMap<String, Object>();
	String hqlQuery = null;
	if (!StringUtil.isEmpty(selectedCrop)) {
		hqlQuery = "select count(*) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 AND fa.status=1 AND f.statusCode=0 AND fc.procurementVariety.procurementProduct.code=:selectedCrop and fc.status=1";
		params.put("selectedCrop", selectedCrop);
	} else {
		// hqlQuery = "select count(*) FROM Farmer f WHERE f.status=1";

		hqlQuery = "select count(*) from Farmer f where f.status='1' and status_code=:statusCode";

		hqlQuery = "select count(*) from Farmer f where status=1 and status_code=:statusCode";

		params.put("statusCode", Integer.valueOf(ESETxnStatus.SUCCESS.ordinal()));

	}

	if (!StringUtil.isEmpty(selectedBranch)) {
		hqlQuery += " AND f.branchId =:branch ";
		params.put("branch", selectedBranch);
	}
	if (!StringUtil.isEmpty(selectedState) && selectedState != 0) {
		hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
		params.put("selectedState", Long.valueOf(selectedState));
	}

	if (!StringUtil.isEmpty(selectedCooperative)) {
		hqlQuery += " AND f.fpo=:selectedCooperative";
		params.put("selectedCooperative", selectedCooperative);
	}

	if (!StringUtil.isEmpty(selectedGender)) {
		hqlQuery += " AND f.gender=:selectedGender";
		params.put("selectedGender", selectedGender);
	}
	Query query = session.createQuery(hqlQuery);
	for (String str : query.getNamedParameters()) {
		query.setParameter(str, params.get(str));
	}

	int val = 0;
	Object obj = query.uniqueResult();
	if (obj != null && ObjectUtil.isInteger(obj)) {
		val = Integer.valueOf(obj.toString());
	}
	return val;
	}
	
	@Override
	public Integer findFarmerCountByStateAndCropbyFarmerStatus(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender,String selectedStatus) {
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery = "select count(*) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 AND fa.status=1 AND f.statusCode=0 AND fc.procurementVariety.procurementProduct.code=:selectedCrop and fc.status=1";
			params.put("selectedCrop", selectedCrop);
		} else {
			// hqlQuery = "select count(*) FROM Farmer f WHERE f.status=1";
			hqlQuery = "select count(*) from Farmer f where status=1 and status_code=:statusCode";
			params.put("statusCode", Integer.valueOf(ESETxnStatus.SUCCESS.ordinal()));

		}

		if (!StringUtil.isEmpty(selectedStatus )) {
			hqlQuery += " AND f.status =:status ";
			params.put("status", Integer.valueOf(selectedStatus));
		}else{
			hqlQuery += " AND f.status =1 ";
		}
		
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != 0) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		int val = 0;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isInteger(obj)) {
			val = Integer.valueOf(obj.toString());
		}
		return val;
	}

	@Override
	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedGender, String selectedCooperative, String selectedCrop, String typez,
			String selectedVillage) {
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();

		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery = "select count(f.id) as count,f.samithi.name as group,f.village.name as village FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 AND fa.status=1 AND f.statusCode=0 AND fc.procurementVariety.procurementProduct.code=:selectedCrop and fc.status=1";
			params.put("selectedCrop", selectedCrop);
		} else {
			hqlQuery = "select count(*) as count,f.samithi.name as group,f.village.name as village from Farmer f where f.status =1 and f.statusCode=0";
		}
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}
		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		if (!StringUtil.isEmpty(typez)) {
			hqlQuery += " AND f.typez=:typez";
			params.put("typez", typez);
		}
		if (!StringUtil.isEmpty(selectedVillage)) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));
		}

		Query query = session.createQuery(hqlQuery + "  GROUP BY f.samithi order by count(*) desc");
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}

	@Override
	public FarmCatalogue findfarmcatalogueByCode(String code) {

		Object[] values = { code, "1" };
		// TODO Auto-generated method stub
		return (FarmCatalogue) find("FROM FarmCatalogue fc WHERE fc.code=? AND fc.status=?", values);
	}

	public Object[] listObjectCultivationExpenses(String selectedBranch, String selectedSeason, String selectedYear) {

		Session session = getSessionFactory().openSession();
		String sqlString;
		Map<String, Object> params = new HashMap<String, Object>();
		sqlString = "SELECT cu.FARM_ID,cu.FARMER_ID,month(cu.EXPENSE_DATE),year (cu.EXPENSE_DATE ),sum( cu.SALE_CT_INCOME),"
				+ "sum(cu.LAND_TOTAL ),sum(cu.TOTAL_WEED ),sum(cu.TOTAL_IRRIGATION ),sum(cu.TOTAL_FERTILIZER ),sum(cu.TOTAL_PESTICIDE ),"
				+ "sum(cu.TOTAL_EXPENSES ),sum(cu.TOTAL_MANURE ),sum(cu.TOTAL_SOWING ),sum(cu.LABOUR_COST ),sum(cu.TOTAL_CULTURE ),"
				+ "cu.CURRENT_SEASON_CODE,sum(cu.TOTAL_GAP),sum(cu.TOTAL_HARVEST) FROM cultivation cu  inner join "
				+ "farm fa on fa.FARM_CODE=cu.FARM_ID inner join farmer f on f.id= fa.farmer_id WHERE cu.ID IS NOT NULL "
				+ "AND fa.status=1 AND f.status=1 AND f.status_code=0";
		
		if (!StringUtil.isEmpty(selectedBranch)) {
			sqlString += " AND cu.BRANCH_ID =:branch ";
			params.put("branch", selectedBranch);
		}

		/*
		 * if (!StringUtil.isEmpty(sDate) && !StringUtil.isEmpty(eDate)) {
		 * hqlQuery += "  AND (Year(cu.expenseDate)=:year";
		 * params.put("year",Integer.valueOf(sDate));
		 * 
		 * hqlQuery += "  OR Year(cu.expenseDate)=:prevYear)";
		 * params.put("prevYear", Integer.valueOf(eDate)); }
		 */

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			sqlString += "  AND cu.current_Season_Code=:year";
			params.put("year", selectedSeason);
		}
		if (!StringUtil.isEmpty(selectedYear)) {
			sqlString += " AND (Year(cu.expense_Date)=:prevYear)";
			params.put("prevYear", Integer.valueOf(selectedYear));
		}
		// hqlQuery += " GROUP BY YEAR(cu.expenseDate)";
		SQLQuery query = session.createSQLQuery(sqlString);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		Object[] result = null;
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			result = (Object[]) query.list().get(0);
		}
		session.flush();
		session.close();
		return result;

	}

	@Override
	public Double findTotalYieldByBranch(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedState) {

		/*
		 * Object[] values = { selectedBranch, selectedYear, (selectedYear - 1)
		 * }; Object[] yearValues = { selectedYear, (selectedYear - 1) }; Double
		 * result = 0.0;
		 * 
		 * if (StringUtil.isEmpty(selectedBranch)) {
		 * 
		 * result = (Double) find(
		 * "SELECT  SUM(fc.estimatedYield) as Total FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , yearValues);
		 * 
		 * } else { result = (Double) find(
		 * "SELECT SUM(fc.estimatedYield) as Total FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , values); } return !StringUtil.isEmpty(result) ? result : 0.0;
		 */

		Double result = 0.0;
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT  SUM(fc.estimatedYield) as Total FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 and fa.status=1 and fc.status=1";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode =:year";
			params.put("year", selectedSeason);

		}

		if (!StringUtil.isEmpty(selectedStaple) && selectedStaple != null) {
			hqlQuery += "  AND fc.stapleLength=:selectedStaple";
			params.put("selectedStaple", selectedStaple);

		}

		if (!StringUtil.isEmpty(selectedState)) {
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
			if (StringUtil.isEmpty(currentBranch)) {
				hqlQuery += " AND f.village.city.locality.state.name =:selectedState";
				params.put("selectedState", selectedState);
			} else {
				hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
				params.put("selectedState", Long.valueOf(selectedState));
			}
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			if (qry != "null") {
				result = Double.valueOf(qry);
			}
		}
		return !StringUtil.isEmpty(result) ? result : 0.0;

	}

	@Override
	public List<Object> listFarmerCountByFpoGroup() {

		// return list("select count(*), fc.name from Farmer f INNER JOIN
		// FarmCatalogue fc WHERE fc.code = f.fpo AND f.fpo <> '' GROUP BY
		// f.fpo");
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"SELECT COUNT(*),cv.name AS FPO FROM farmer INNER JOIN catalogue_value cv ON cv.code=farmer.fpo WHERE farmer.FPO <>'' GROUP BY farmer.FPO ORDER BY COUNT(*) DESC LIMIT 10");
		List list = query.list();
		/*
		 * session.flush(); session.close();
		 */
		return list;
	}

	@Override
	public Double findCocCostByCropBranchCooperativeAndGender(String branchId, String selectedCooperative,
			String selectedCrop, String selectedGender, String selectedYear, String selectedState) {

		Session session = getSessionFactory().getCurrentSession();

		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(TOTAL_COC) from cultivation as coc INNER JOIN farm as fa on fa.farm_code=coc.farm_id "
				+ "INNER JOIN farm_crops as fc on fc.farm_id=fa.id INNER JOIN farmer AS f on f.id=fa.farmer_id INNER JOIN procurement_variety"
				+ "  AS pv on pv.id=fc.PROCUREMENT_CROPS_VARIETY_ID "
				+ "INNER JOIN procurement_product  AS pp on pp.id=pv.procurement_product_id INNER JOIN city AS c ON f.CITY_ID = c.ID "
				+ "INNER JOIN location_detail AS locDeta ON c.LOCATION_ID = locDeta.ID "
				+ "where coc.branch_Id=:branchId";

		if (!StringUtil.isEmpty(branchId)) {
			params.put("branchId", branchId);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND locDeta.state_id=:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (!StringUtil.isEmpty(selectedYear)) {
			hqlQuery += " AND (Year(coc.EXPENSE_DATE)=:prevYear OR Year(coc.EXPENSE_DATE)=:year)";
			params.put("prevYear", Integer.valueOf(selectedYear) - 1);
			params.put("year", selectedYear);
		}
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += " AND pp.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createSQLQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Double val = 0D;
		if (!StringUtil.isEmpty(query.list())) {
			val = (Double) query.list().get(0);
		}

		return val;
	}

	@Override
	public Integer findFarmerCountByState(String selectedBranch, Integer selectedState) {
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedBranch)) {
			return ((Long) session
					.createQuery(
							"select count(*) FROM Farmer f WHERE f.status=1 AND f.village.city.locality.state.id =:selectedState")
					.setParameter("selectedState", Long.valueOf(selectedState)).uniqueResult()).intValue();
		} else {
			return ((Long) session
					.createQuery(
							"select count(*) FROM Farmer f WHERE f.status=1 AND f.branchId =:branch AND f.village.city.locality.state.id =:selectedState")
					.setParameter("branch", selectedBranch).setParameter("selectedState", Long.valueOf(selectedState))
					.uniqueResult()).intValue();
		}
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldByState(String selectedBranch, Long selectedState) {
		Object[] values = { selectedBranch, selectedState };
		Object[] stateValues = { selectedState };
		if (StringUtil.isEmpty(selectedBranch)) {
			return list(
					"SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE f.village.city.locality.state.id =?  and fc.status=1",
					stateValues);

		} else {
			return list(
					"SELECT SUM(fc.estimatedYield) as Total,SUM(fc.farm.farmDetailedInfo.proposedPlantingArea) as Proposed FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE fc.farm.farmer.branchId=? AND f.village.city.locality.state.id =? and fc.status=1)",
					values);
		}

	}

	@Override
	public Long findTotalIncomeFromCottonByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT sum(cd.SUB_TOTAL) FROM crop_harvest_details cd INNER JOIN crop_harvest as ch on cd.CROP_HARVEST_ID=ch.ID INNER JOIN farmer AS f ON ch.FARMER_ID= f.FARMER_ID "
				+ "INNER JOIN farm  AS fm on fm.farmer_id=f.id  INNER JOIN farm_crops  AS fc on fc.farm_id=fm.id "
				+ "INNER JOIN procurement_variety  AS pv on pv.id=fc.PROCUREMENT_CROPS_VARIETY_ID "
				+ "INNER JOIN procurement_product  AS pp on pp.id=pv.procurement_product_id INNER JOIN city AS c ON f.CITY_ID = c.ID "
				+ "INNER JOIN location_detail AS ld ON c.LOCATION_ID = ld.ID WHERE ch.BRANCH_ID =:selectedBranch";

		if (!StringUtil.isEmpty(selectedBranch)) {
			params.put("selectedBranch", selectedBranch);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND ld.STATE_ID =:selectedState ";
			params.put("selectedState", Long.valueOf(selectedState));
		}
		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender =:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += "  AND pp.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}

		Query query = session.createSQLQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Double d = 0.0;
		long value = 0L;
		if (!StringUtil.isEmpty(query.list())) {
			d = (Double) query.list().get(0);
		}
		if (d != null) {
			value = d.longValue();
		}
		return value;

	}

	@Override
	public Integer findTotalCultivationProdLandByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();

		String hqlQuery = "SELECT cast(sum(fc.cultiArea) as int) from FarmCrops fc INNER JOIN fc.farm  fm inner join fm.farmer f where  fc.status=1 and fm.farmCode in (SELECT c.farmCode from CropHarvest c WHERE  f.branchId =:selectedBranch";

		if (!StringUtil.isEmpty(selectedBranch)) {
			params.put("selectedBranch", selectedBranch);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState ";
			params.put("selectedState", Long.valueOf(selectedState));
		}
		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender =:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += "  AND fc.procurementVariety.procurementProduct.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}

		Query query = session.createQuery(hqlQuery + ")");
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Integer val = 0;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isInteger(obj)) {
			val = Integer.valueOf(obj.toString());
		}
		return val;
	}

	@Override
	public List<Object[]> listOfCropNamesByCropTypeAndFarm(String selectedCropType, String selectedFarm) {
		Object values[] = { Integer.valueOf(selectedCropType), selectedFarm };
		return list(
				"select distinct fc.procurementVariety.procurementProduct.id,fc.procurementVariety.procurementProduct.name FROM FarmCrops fc WHERE fc.cropCategory = ? and fc.farm.farmCode=? and fc.status=1",
				values);
	}

	@Override
	public Double findCropSupplyByYearAndBranch(String selectedBranch, Integer selectedYear) {
		Object[] values = { selectedBranch, selectedYear, (selectedYear - 1) };
		Object[] yearValues = { selectedYear, (selectedYear - 1) };
		if (StringUtil.isEmpty(selectedBranch)) {
			return (Double) find(
					"SELECT sum(csd.qty) as totQty FROM CropSupply cs inner join cs.cropSupplyDetails csd WHERE Year(cs.dateOfSale)=? OR Year(cs.dateOfSale)=?",
					yearValues);
		} else {
			return (Double) find(
					"SELECT sum(csd.qty) as totQty FROM CropSupply cs inner join cs.cropSupplyDetails csd WHERE cs.branchId=? AND (Year(cs.dateOfSale)=? OR Year(cs.dateOfSale)=?)",
					values);
		}
	}

	@Override
	public List<Object> listSeedSourceCountBySource(String selectedBranch, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedVariety, String selectedState) {

		/*
		 * Object[] values = { selectedSeedSource, SELECT }; return list(
		 * "select count(f.id) as count,f.seedSource as name from FarmCrops f where branchId=? and seedSource!=? GROUP BY f.seedSource"
		 * , values);
		 */
		String groupBy = "  GROUP BY fc.seedSource";

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(fc.id) as count,fc.seedSource as name from FarmCrops fc where fc.seedSource!=:type  and fc.status=1";

		params.put("type", SELECT);

		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND fc.branchId=:selectedBranch";
			params.put("selectedBranch", selectedBranch);
		}

		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND fc.farm.farmer.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND fc.farm.farmer.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		if (!StringUtil.isEmpty(selectedVariety)) {
			hqlQuery += " AND fc.procurementVariety.code=:selectedVariety";
			params.put("selectedVariety", selectedVariety);
		}
		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND fc.farm.farmer.village.city.locality.state.id=:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}
		Query query = session.createQuery(hqlQuery + groupBy);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}

	@Override
	public List<Object> listTotalFarmAcreByBranch() {

		return list(
				"SELECT SUM(fd.totalLandHolding) as Total,SUM(fd.proposedPlantingArea) as Proposed,f.branchId FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f  where  fa.status=1  GROUP BY f.branchId");
	}

	@Override
	public String findCropHarvestByYearAndBranch(String selectedBranch, Integer selectedYear) {
		Object[] values = { selectedBranch, selectedYear, (selectedYear - 1) };
		Object[] yearValues = { selectedYear, (selectedYear - 1) };
		if (StringUtil.isEmpty(selectedBranch)) {
			return (String) find(
					"SELECT sum(ch.totalQty) as totQty FROM CropHarvest ch WHERE (Year(ch.harvestDate)=? OR Year(ch.harvestDate)=?)",
					yearValues);
		} else {
			return (String) find(
					"SELECT sum(ch.totalQty) as totQty FROM CropHarvest ch WHERE ch.branchId=? AND (Year(ch.harvestDate)=? OR Year(ch.harvestDate)=?)",
					values);
		}
	}

	@Override
	public List<Object> listSeedTypeCountByBranch(String selectedBranch, String selectedCrop,
			String selectedCooperative, String selectedGender) {

		/*
		 * Object[] values = { selectedBranch, SELECT }; return list(
		 * "select count(f.id) as count,f.type as name from FarmCrops f where f.branchId=? and type!=? GROUP BY f.type"
		 * , values);
		 */
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(fc.id) as count,fc.type as name from FarmCrops fc where fc.type!=:type and fc.type!='' and fc.status=1";
		String groupBy = " GROUP BY fc.type";
		params.put("type", SELECT);

		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND fc.branchId=:selectedBranch";
			params.put("selectedBranch", selectedBranch);
		}

		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND fc.farm.farmer.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND fc.farm.farmer.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createQuery(hqlQuery + groupBy);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}

	@Override
	public List<Object[]> listFarmerFarmInfoByVillageIdImg(Long cropId, Long farmerId, Long farmId) {
		// TODO Auto-generated method stub
		Object[] values = { cropId, farmId };
		if (!StringUtil.isEmpty(cropId) && cropId != 0) {
			return list("SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,farm.farmName,farm.farmCode,"
					+ "farm.latitude,farm.longitude,farm.landmark,fdi.totalLandHolding,fdi.proposedPlantingArea,"
					+ "f.village.name,img.image,f.seasonCode,f.branchId,f.createdDate,"
					+ "fc.procurementVariety.procurementProduct.name,fc.cultiArea,fc.estimatedHarvestDate,fc.estimatedYield from Farmer f "
					+ " LEFT JOIN f.imageInfo.photo img INNER JOIN f.farms farm INNER JOIN farm.farmDetailedInfo fdi INNER JOIN farm.farmCrops fc"
					+ " where f.statusCode='" + ESETxnStatus.SUCCESS.ordinal()
					+ "' and fc.procurementVariety.procurementProduct.id=? and farm.id=?", values);
		} else {
			return list("SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,farm.farmName,farm.farmCode,"
					+ "farm.latitude,farm.longitude,farm.landmark,fdi.totalLandHolding,fdi.proposedPlantingArea,"
					+ "f.village.name,img.image,f.seasonCode," + "f.branchId,f.createdDate from Farmer f "
					+ " LEFT JOIN f.imageInfo.photo img INNER JOIN f.farms farm INNER JOIN farm.farmDetailedInfo fdi"
					+ " where f.statusCode='" + ESETxnStatus.SUCCESS.ordinal() + "' and farm.id=?", farmId);

		}

	}

	@Override
	public List<Object[]> findFarmerByVillageCodeAndProcurement(String villageCode) {
		List<Object[]> result = list(
				"SELECT DISTINCT p.farmer.farmerId,p.farmer.firstName,p.farmer.lastName FROM Procurement p  where p.farmer.village.code = ?",
				villageCode);

		return result;
	}

	@Override
	public List<Object[]> findFarmerByFpo(String fpo) {
		Object[] values = { ESETxnStatus.SUCCESS.ordinal(), fpo };
		return list(
				"SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName FROM Farmer f where f.statusCode=? AND f.fpo=?",
				values);
	}

	@Override
	public String findComponentNameByDynamicField(String compName) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT componentName from DynamicFieldConfig dfc  WHERE dfc.componentName=:componentName");
		query.setParameter("componentName", compName);
		String val = "";
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = String.valueOf(obj);
		}
		return val;
	}

	@Override
	public List<DynamicSectionConfig> findDynamicFieldsBySectionId(String sectionId) {
		// TODO Auto-generated method stub
		return list("FROM DynamicSectionConfig dsc where dsc.tableId=? order by dsc.id asc", sectionId);
	}

	public Object[] findDynamicValueByFarmerId(String componentName, long farmerId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT fdfv.id,fdfv.fieldValue from FarmerDynamicFieldsValue fdfv  WHERE fdfv.farmer.id=:farmerId AND fdfv.fieldName=:componentName");
		query.setParameter("farmerId", farmerId);
		query.setParameter("componentName", componentName);
		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = (Object[]) obj;
		}
		return val;
	}

	@Override
	public List<DynamicFieldConfig> listDynamicFields() {
		return list("FROM DynamicFieldConfig dsc order by dsc.id asc ");
	}

	@Override
	public List<DynamicSectionConfig> listDynamicSections() {
		return list("FROM DynamicSectionConfig dsc order by dsc.id asc ");
	}

	@Override
	public List<FarmerFeedbackEntity> findFamerFromFarmerFeedback() {
		// TODO Auto-generated method stub
		return list("FROM FarmerFeedbackEntity ffe");
	}

	@Override
	public List<Object[]> listFarmerBySamithi(Long samithiIds) {
		Object[] values = { samithiIds, ESETxnStatus.SUCCESS.ordinal(), Farmer.CERTIFIED_YES };
		return (List<Object[]>) list(
				"select f.farmerId,f.firstName,f.lastName,f.id FROM Farmer f WHERE f.samithi.id=? AND f.statusCode = ? AND f.isCertifiedFarmer = ? ",
				values);
	}

	@Override
	public AnimalHusbandary findAnimalHusbandaryByFarmerIdAndId(Long farmerId, Long animalId) {
		// TODO Auto-generated method stub
		Object[] value = { farmerId, animalId };
		return (AnimalHusbandary) find("From AnimalHusbandary ah where ah.farmer.id=? AND ah.farmAnimal.id = ?", value);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmId(Long farmId) {
		// TODO Auto-generated method stub
		return (FarmIcsConversion) find("From FarmIcsConversion fc where fc.farm.id=? AND fc.isActive=1", farmId);
	}

	@Override
	public FarmCrops findFarmCropsByFarmCodeAndCategory(long farmId) {

		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.farm.id = ? AND fc.cropCategory='0' and fc.status=1",
				farmId);
	}

	@Override
	public List<Object[]> listCropsByFarmIdBasedOnCategory(Long farmId) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery(
						"SELECT f.id,COALESCE(f.procurementVariety.code,''),COALESCE(f.procurementVariety.procurementProduct.code,''),COALESCE(f.seedSource,''),COALESCE(f.seedQtyCost,''),COALESCE(f.estimatedYield,''),COALESCE(f.cropSeason.code,''),COALESCE(f.cropCategory,''),COALESCE(f.farm.farmCode,''),COALESCE(f.farm.farmer.farmerId,''),COALESCE(f.riskAssesment,''),COALESCE(f.seedTreatmentDetails,''),COALESCE(f.otherSeedTreatmentDetails,''),COALESCE(f.stapleLength,''),COALESCE(f.seedQtyUsed,''),COALESCE(f.type,''),COALESCE(f.cropCategoryList,''),COALESCE(f.cultiArea,''),COALESCE(f.sowingDate,''),COALESCE(f.estimatedHarvestDate,'') from FarmCrops f where f.farm.id = :farmerId AND f.cropCategory =:cat and f.status=1")
				.setParameter("farmerId", farmId).setParameter("cat", 0);
		List result = query.list();

		return result;
	}

	@Override
	public Object[] findTotalQtyByCropHarvest(String selectedBranch, String selectedSeason, String selectedStaple,
			String selectedState) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();

		Session session = getSessionFactory().getCurrentSession();
		String hqlQuery = "SELECT  SUM(fc.postHarvestProd),sum(fc.seedCotton) from FarmCrops fc "
				+ "WHERE fc.farm.farmer.status=1 and fc.farm.status=1 and  fc.status=1 AND fc.postHarvestProd is not null "
				+ "AND fc.postHarvestProd!='' AND fc.seedCotton is not null AND  fc.seedCotton!=''"
				+ " AND fc.seedCotton!=0 AND fc.seedCotton!=0.0 ";

		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND fc.farm.farmer.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND fc.farm.farmer.seasonCode=:year";
			params.put("year", selectedSeason);

		}

		if (!StringUtil.isEmpty(selectedStaple) && selectedStaple != null) {
			hqlQuery += "  AND fc.stapleLength=:selectedStaple";
			params.put("selectedStaple", selectedStaple);

		}

		if (!StringUtil.isEmpty(selectedState)) {
			HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
			Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
			String currentBranch = ObjectUtil.isEmpty(object3) ? "" : object3.toString();
			if (StringUtil.isEmpty(currentBranch)) {
				hqlQuery += " AND fc.farm.farmer.village.city.locality.state.name =:selectedState";
				params.put("selectedState", selectedState);
			} else {
				hqlQuery += " AND fc.farm.farmer.village.city.locality.state.id =:selectedState";
				params.put("selectedState", Long.valueOf(selectedState));
			}
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = (Object[]) obj;
		}
		return val;
	}

	@Override
	public Integer findCropCount() {

		Session session = getSessionFactory().getCurrentSession();
		return ((Long) session.createQuery("select count(*) from ProcurementProduct").uniqueResult()).intValue();
	}

	@Override
	public Double findSaleCottonByCoc(List<String> farmerIds, List<String> farmCodes, String selectedSeason) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT cast(sum(coc.saleCottonIncome) as long) as SaleCotton  from Cultivation coc WHERE coc.farmerId in:fId  and coc.farmId in:faCode and coc.currentSeasonCode=:season");
		query.setParameterList("fId", farmerIds);
		query.setParameterList("faCode", farmCodes);
		query.setParameter("season", selectedSeason);
		long val = 0L;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).longValue();
		}
		return StringUtil.isLong(val) ? Double.valueOf(val) : 0.0;
	}

	@Override
	public String findTotalLandByPostHarvest(String selectedBranch, int selectedYear, String selectedSeason) {
		// TODO Auto-generated method stub
		/*
		 * Object[] values = { selectedBranch, selectedYear, (selectedYear - 1)
		 * }; Object[] yearValues = { selectedYear, (selectedYear - 1) }; if
		 * (StringUtil.isEmpty(selectedBranch)) { return (String) find(
		 * "SELECT SUM(fc.postHarvestProd) as Proposed FROM FarmCrops fc  INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , yearValues);
		 * 
		 * } else { return (String) find(
		 * "SELECT SUM(fc.postHarvestProd) as Proposed FROM FarmCrops fc  INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.branchId=? AND (Year(f.createdDate)=? OR Year(f.createdDate)=?)"
		 * , values); }
		 */

		Session session = getSessionFactory().getCurrentSession();
		String land = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT SUM(fc.cultiArea) as Proposed FROM FarmCrops fc  INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 and fc.status=1 ";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode=:year";
			params.put("year", selectedSeason);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			land = qry;
		}
		return land;
	}

	@Override
	public List<Object[]> listFarmerIdAndName() {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(
				"SELECT	distinct farmer_id,FIRST_NAME,LAST_NAME FROM	farmer WHERE	 FIND_IN_SET(farmer_id,(SELECT group_concat(fcpa.farmer_id SEPARATOR  ',') FROM survey_farmer_crop_prod_answers fcpa))");

		List list = query.list();
		return list;
	}

	@Override
	public Long findFarmCountByFarmerId(Long farmerId) {
		// TODO Auto-generated method stub
		return (Long) find("SELECT count(*) FROM Farm fm WHERE fm.farmer.id=?  and  fm.status=1 ", farmerId);

	}

	public List<Cultivation> listCultivation() {
		return list("FROM Cultivation");
	}

	@Override
	public List<CropHarvest> listCropHarvest() {
		return list("FROM CropHarvest");
	}

	@Override
	public FarmerField findFarmerFieldByFieldName(String entityColumn) {
		return (FarmerField) find("FROM FarmerField ff WHERE ff.field = ?", entityColumn);
	}

	@Override
	public List<Object[]> listFarmerByCooperativeCode(List<String> samithiIds) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select f.farmerId,f.firstName,f.lastName FROM Farmer f WHERE f.samithi.code in (:codes) AND f.statusCode = :statCode");
		query.setParameterList("codes", samithiIds);
		query.setParameter("statCode", ESETxnStatus.SUCCESS.ordinal());

		return query.list();

	}

	@Override
	public List<Object[]> findCocCostByFarmAndFarmer(String farmerId, String farmCode) {
		// TODO Auto-generated method stub
		Object[] values = { farmerId, farmCode };
		return list(
				"SELECT c.totalCoc as TotalCoc,c.interCropIncome as InterIncome,c.agriIncome as agriIncome,"
						+ "c.otherSourcesIncome as otherSourcesIncome,c.cottonQty as CottonQty,c.unitSalePrice as UnitSalePrice,c.saleCottonIncome as SaleCottonIncome,c.id FROM  Cultivation c where c.farmerId=? and c.farmId=? and c.txnType='1'",
				values);
	}

	@Override
	public Object findCottonIncomeByFarmerAndFarm(String farmerId, String farmCode) {
		// TODO Auto-generated method stub
		Object[] values = { farmerId, farmCode };
		return find(
				"SELECT cd.subTotal from CropHarvestDetails cd Inner join cd.cropHarvest ch WHERE ch.farmerId=? and ch.farmCode=?",
				values);
	}

	@Override
	public List<Object[]> findFarmerByGroup(long fpo) {
		Object[] values = { ESETxnStatus.SUCCESS.ordinal(), fpo };
		return list(
				"SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,f.surName FROM Farmer f where f.statusCode=? AND f.samithi.id=?",
				values);
	}

	@Override
	public MasterData findMasterDataIdById(long farmerId) {

		// TODO Auto-generated method stub
		return (MasterData) find("FROM MasterData md WHERE md.id = ?", farmerId);
	}

	@Override
	public MasterData findMasterDataIdByCode(String farmerId, String tenantId) {

		// TODO Auto-generated method stub
		// return (MasterData) find("FROM MasterData md WHERE md.code = ?",
		// farmerId);
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM MasterData md WHERE md.code =:farmerId");
		query.setParameter("farmerId", farmerId);

		List<MasterData> masterDataList = query.list();

		MasterData masterData = null;
		if (masterDataList.size() > 0) {
			masterData = (MasterData) masterDataList.get(0);
		}

		session.flush();
		session.close();
		return masterData;
	}

	@Override
	public Farm findFarmByCode(String code, String tenantId) {

		// return (Farm) find("FROM Farm fm WHERE fm.farmCode = ?", code);

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Farm fm left join fetch fm.farmer fr WHERE fm.farmCode=:code");
		query.setParameter("code", code);

		List<Farm> farmList = query.list();

		Farm farm = null;
		if (farmList.size() > 0) {
			farm = (Farm) farmList.get(0);
		}

		session.flush();
		session.close();
		return farm;
	}

	@Override
	public Farmer findFarmerbyFarmerIdAndSeason(String farmerId, String season) {
		Object[] values = { farmerId, ESETxnStatus.SUCCESS.ordinal(), season };
		Farmer farmer = (Farmer) find("FROM Farmer fr WHERE  fr.farmerId = ? AND fr.statusCode = ? and fr.seasonCode=?",
				values);
		return farmer;
	}

	@Override
	public List<Cultivation> listCultivationBySeason(String currentSeasonsCode) {
		return list("FROM Cultivation where currentSeasonCode = ?", currentSeasonsCode);
	}

	@Override
	public List<Object[]> findCultivationCostBySeason(String farmerId, String farmCode, String season) {
		Object[] values = { farmerId, farmCode, season };
		return list(
				"SELECT sum(c.totalCoc) as TotalCoc,sum(c.interCropIncome) as InterIncome,sum(c.agriIncome) as agriIncome,"
						+ "sum(c.otherSourcesIncome) as otherSourcesIncome,sum(c.cottonQty) as CottonQty,sum(c.unitSalePrice) as UnitSalePrice,sum(c.saleCottonIncome) as SaleCottonIncome FROM  Cultivation c where c.farmerId=? and c.farmId=? and c.currentSeasonCode=?",
				values);
	}

	@Override
	public Farm findFarmByfarmIdAndSeason(String farmId, String cSeasonCode) {
		return (Farm) find("FROM Farm fm WHERE fm.farmCode  = ? and fm.farmer.seasonCode=?",
				new Object[] { farmId, cSeasonCode });
	}

	@Override
	public Farm findFarmByfarmCodeAndSeason(String farmId, String cSeasonCode) {
		return (Farm) find("FROM Farm fm WHERE fm.farmCode  = ? and fm.farmer.seasonCode=?",
				new Object[] { farmId, cSeasonCode });
	}

	@Override
	public List<Object> listFarmerCountByGroupByType() {
		Object[] values = { Farmer.IRP };
		return list("select count(*) as count,f.samithi.name as group from Farmer f WHERE f.typez= GROUP BY samithi",
				values);
	}

	@Override
	public Integer findFarmersCountByStatusByTypez() {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("select count(*) from Farmer where status=1 and typez=:typez");
		/*
		 * query.setParameter("status", ESETxnStatus.SUCCESS.ordinal());
		 */ query.setParameter("typez", Farmer.FARMER);

		Integer val = ((Long) query.uniqueResult()).intValue();
		session.flush();
		session.close();
		return val;

	}

	@Override
	public Farmer findFarmerById(Long id, String branchId) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		Query q = session.createQuery("FROM Farmer f WHERE f.id=:farmerId AND f.typez=:typez AND f.branchId=:branchId");
		q.setParameter("farmerId", id);
		q.setParameter("typez", Farmer.IRP);
		q.setParameter("branchId", branchId);
		Farmer farmer = (Farmer) q.uniqueResult();
		session.flush();
		session.close();
		return farmer;
	}

	public List<Object[]> listQtyByUomAndType(String id, String type) {

		String queryString = "Select COALESCE(SUM(cd.QUANTITY),0) AS QTY,cv.NAME from cultivation_detail cd INNER JOIN catalogue_value cv ON cv.CODE=cd.UOM WHERE cd.CULTIVATION_ID=:id AND cd.type=:type GROUP BY cd.UOM";
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery(queryString).setParameter("id", id).setParameter("type", type);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public List<Object[]> listUomType(String id) {

		// TODO Auto-generated method stub

		String queryString = "SELECT type from cultivation_detail WHERE CULTIVATION_ID=:id GROUP BY TYPE";
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery(queryString).setParameter("id", id);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<PeriodicInspection> listPeriodicInspection() {
		return list("FROM PeriodicInspection");
	}

	@Override
	public List<CultivationDetail> listCultivationDetail() {
		return list("FROM CultivationDetail");
	}

	public Farmer findFarmerByTraceId(String traceId) {

		Farmer farmer = (Farmer) find("From Farmer fm WHERE fm.farmersCodeTracenet = ?", traceId);
		return farmer;
	}

	@Override
	public List<Cultivation> findCOCByFarmerIdFarmIdSeason(String farmerId, String farmId, String seasonCode) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM  Cultivation c WHERE c.farmerId=:farmerId AND c.farmId=:farmId ORDER BY c.id ASC");
		query.setParameter("farmerId", farmerId);
		query.setParameter("farmId", farmId);
		// query.setParameter("currentSeasonCode", seasonCode);
		List result = query.list();
		session.flush();
		session.close();
		return result;

	}

	@Override
	public List<ViewFarmerActivity> listActivityReport(String sord, String sidx, int startIndex, int limit,
			Date getsDate, Date geteDate, ViewFarmerActivity entity, int page, Object object) {

		Session session = getSessionFactory().openSession();
		Query q = session.getNamedQuery("activityReport");
		String query = q.getQueryString(); // the sql statement
		// query += " and findStuff like :likeWhat"; // add your clause
		int i = 0;
		if (!StringUtil.isEmpty(entity.getFid())) {
			query += " and  f.id='" + entity.getFid() + "'";

			i = 1;
		}

		if (!StringUtil.isEmpty(entity.getBranchesList())) {
			query += " and BRANCH_ID in(:branchesList) ";
			i = 1;
		}

		query += " having DIST_COUNT>0 or PROC_COUNT>0 or PERIODIC_COUNT>0 or TRAIN_COUNT>0 or DYNAMIC_COUNT is not null ";

		query += " order by " + sidx + " " + sord;
		q = session.createSQLQuery(query);
		q.setParameter("startDate", getsDate);
		q.setParameter("endDate", geteDate);
		if (!StringUtil.isEmpty(entity.getBranchesList())) {
			q.setParameterList("branchesList", entity.getBranchesList());
		}

		if (limit != 0) {
			// set starting record index
			q.setFirstResult(startIndex);
			// set the page records limit
			q.setMaxResults(limit);
		}

		List<ViewFarmerActivity> list = (List<ViewFarmerActivity>) q.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public List<ViewFarmerActivity> listActivityReport(String startDate, String endDate, String farmer) {

		Session session = getSessionFactory().openSession();
		Query q = session.getNamedQuery("activityReport");
		String query = q.getQueryString(); // the sql statement
		q = session.createSQLQuery(query);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		List<ViewFarmerActivity> list = (List<ViewFarmerActivity>) q.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public List<Object> findICSFarmerCountByGroup(String selectedBranch, int selectedYear, String selectedSeason,
			int icsTyp) {
		String groupBy = " GROUP BY fic.icsType,f.samithi.name";
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select fic.icsType,count(f.id),f.samithi.name FROM FarmCrops fc INNER JOIN  fc.farm fm INNER JOIN fm.farmICSConversion fic INNER JOIN  fm.farmer f WHERE f.status=1 and fm.status=1 and fc.status=1 AND fc.preHarvestProd is not null";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedYear) && selectedYear != 0) {
			hqlQuery += "  AND (Year(f.createdDate)=:year";
			params.put("year", selectedYear);

			hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			params.put("prevYear", (selectedYear - 1));
		}

		if (!StringUtil.isEmpty(icsTyp)) {
			hqlQuery += " AND fic.icsType =:icsTyp ";
			params.put("icsTyp", String.valueOf(icsTyp));
		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode=:season";
			params.put("season", selectedSeason);
		}
		hqlQuery += groupBy;
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();

	}

	@Override
	public String findCropNameByCropCode(String cropCode) {
		// TODO Auto-generated method stub
		return (String) find("select p.name from ProcurementProduct p where p.code=?", cropCode);
	}

	@Override
	public List<Object[]> listFcpawithCategoryCode(String categoryCode) {

		return list(
				"select fcp.farmId,fcp.farmCode,fcp.farmName,fcp.farmerId,fcp.farmerName FROM FarmerCropProdAnswers fcp where fcp.categoryCode=?",
				categoryCode);

	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByFarmerId(Long farmerId, String txnType) {
		Object[] values = { farmerId, txnType };
		return list(
				"select fdfv FROM FarmerDynamicFieldsValue fdfv LEFT JOIN FETCH fdfv.dymamicImageData di where fdfv.farmer.id=? AND fdfv.txnType=?",
				values);
	}

	@Override
	public List<Object[]> ListOfFarmerHealthAssessmentByFarmerId() {
		// TODO Auto-generated method stub
		return list(
				"SELECT fh.id,fh.farmer.id,diabilityType,origin,remark,consultationStatus,consulatationDetail from FarmerHealthAsses fh");
	}

	@Override
	public List<Object[]> ListOfFarmerSelfAssesmentByFarmerId() {
		// TODO Auto-generated method stub
		return list("SELECT fs.id,fs.farmer.id,fs.activity,fs.value,fs.remark from FarmerSelfAsses fs");
	}

	@Override
	public List<FarmerDynamicFieldsValue> listDynmaicFieldsInfo() {
		// TODO Auto-generated method stub
		return list("select fdfv FROM FarmerDynamicFieldsValue fdfv");
	}

	@Override
	public List<Object[]> listFarmCropsFieldsByFarmerId(List<Long> farmIds) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery(
						"SELECT f.id,COALESCE(f.procurementVariety.code,''),COALESCE(f.procurementVariety.procurementProduct.code,''),COALESCE(f.seedSource,''),COALESCE(f.seedQtyCost,''),COALESCE(f.estimatedYield,''),COALESCE(f.cropSeason.code,''),COALESCE(f.cropCategory,''),COALESCE(f.farm.farmCode,''),COALESCE(f.farm.farmer.farmerId,''),COALESCE(f.riskAssesment,''),COALESCE(f.seedTreatmentDetails,''),COALESCE(f.otherSeedTreatmentDetails,''),COALESCE(f.stapleLength,''),COALESCE(f.seedQtyUsed,''),COALESCE(f.type,''),COALESCE(f.cropCategoryList,''),COALESCE(f.cultiArea,''),COALESCE(f.sowingDate,''),COALESCE(f.estimatedHarvestDate,''),COALESCE(f.interType,''),COALESCE(f.interAcre,''),COALESCE(f.totalCropHarv,''),COALESCE(f.grossIncome,''),f.farm.id from FarmCrops f where f.farm.id in (:farmerId) and f.status=1 ")
				.setParameterList("farmerId", farmIds);
		List result = query.list();

		return result;
	}

	@Override
	public Integer findFarmerSowingCount(String selectedBranch, int selectedYear, String selectedSeason) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(f.id) FROM FarmCrops fc INNER JOIN fc.farm fm INNER JOIN fm.farmer f WHERE f.status=1 and fc.status=1  AND fc.preSowingProd is not null";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		/*
		 * if (!StringUtil.isEmpty(selectedYear) && selectedYear != 0) {
		 * hqlQuery += "  AND (Year(f.createdDate)=:year"; params.put("year",
		 * selectedYear);
		 * 
		 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
		 * params.put("prevYear", (selectedYear - 1)); }
		 */

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode=:season";
			params.put("season", selectedSeason);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		int count = 0;
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			count = Integer.valueOf(qry);
		}
		return count;
	}

	@Override
	public Integer findFarmerPreHarvestCount(String selectedBranch, int selectedYear, String selectedSeason,
			String selectedGender, String selectedState) {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(object3) ? "" : object3.toString();

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(f.id) FROM FarmCrops fc INNER JOIN fc.farm fm INNER JOIN fm.farmer f"
				+ " WHERE fm.status=1 AND f.status=1 AND fc.preHarvestProd is not null and fc.preHarvestProd!=''  and fc.status=1";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		/*
		 * if (!StringUtil.isEmpty(selectedYear) && selectedYear != 0) {
		 * hqlQuery += "  AND (Year(f.createdDate)=:year"; params.put("year",
		 * selectedYear);
		 * 
		 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
		 * params.put("prevYear", (selectedYear - 1)); }
		 */

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			if (StringUtil.isEmpty(currentBranch)) {
				hqlQuery += " AND f.village.city.locality.state.name =:selectedState";
				params.put("selectedState", selectedState);
			} else {
				hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
				params.put("selectedState", Long.valueOf(selectedState));
			}
		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode=:season";
			params.put("season", selectedSeason);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		int count = 0;
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			count = Integer.valueOf(qry);
		}
		return count;
	}

	@Override
	public Integer findFarmerPostHarvestCount(String selectedBranch, int selectedYear, String selectedSeason,
			String selectedGender, String selectedState) {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		Object object3 = httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
		String currentBranch = ObjectUtil.isEmpty(object3) ? "" : object3.toString();

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(f.id) FROM FarmCrops fc INNER JOIN fc.farm fm INNER JOIN fm.farmer f "
				+ "WHERE fm.status=1 AND f.status=1 and fc.status=1 AND fc.postHarvestProd is not null and fc.postHarvestProd!='' ";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		/*
		 * if (!StringUtil.isEmpty(selectedYear) && selectedYear != 0) {
		 * hqlQuery += "  AND (Year(f.createdDate)=:year"; params.put("year",
		 * selectedYear);
		 * 
		 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
		 * params.put("prevYear", (selectedYear - 1)); }
		 */

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND f.seasonCode=:season";
			params.put("season", selectedSeason);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			if (StringUtil.isEmpty(currentBranch)) {
				hqlQuery += " AND f.village.city.locality.state.name =:selectedState";
				params.put("selectedState", selectedState);
			} else {
				hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
				params.put("selectedState", Long.valueOf(selectedState));
			}
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		int count = 0;
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			count = Integer.valueOf(qry);
		}
		return count;
	}

	@Override
	public List<Object[]> findComponentListVal(String compntTyp) {
		// TODO Auto-generated method stub
		return list("select dfc.id,dfc.componentName from DynamicFieldConfig dfc where dfc.componentType=?", compntTyp);
	}

	@Override
	public int findMaxOrderNo() {
		// TODO Auto-generated method stub
		int maxOrderNo = 0;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select max(orderSet) from DynamicFieldConfig");
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			if (!StringUtil.isEmpty(qry)) {
				maxOrderNo = Integer.valueOf(qry);
			}
		}
		return maxOrderNo;
	}

	@Override
	public String findSectionCodeByReferenceId(String dynamicFieldId) {
		String sectionCode = "";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select dfc.dynamicSectionConfig.sectionCode from DynamicFieldConfig dfc where  dfc.id=:dynamicFieldId");
		query.setParameter("dynamicFieldId", Long.valueOf(dynamicFieldId));
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			if (!StringUtil.isEmpty(qry)) {
				sectionCode = String.valueOf(qry);
			}
		}
		return sectionCode;
	}

	@Override
	public void updateFarmerStatusByFarmerId(String farmerId) {

		Session session = getSessionFactory().openSession();

		Query query = session.createSQLQuery(
				"UPDATE farmer fm  LEFT JOIN farm f on f.FARMER_ID = fm.ID LEFT JOIN farm_crops fc on fc.FARM_ID = f.ID  SET  fm.STATUS=:status,fm.STATUS_MSG='FARMER DELETED',fm.REVISION_NO=:revNo,fm.STATUS_CODE=:statCode,f.STATUS=:status,f.REVISION_NO=:revNo,fc.REVISION_NO=:revNo,fc.STATUS=:status WHERE fm.FARMER_ID = :farmerId");
		query.setParameter("farmerId", farmerId);
		query.setParameter("status", Farmer.Status.DELETED.ordinal());
		query.setParameter("statCode", ESETxnStatus.DELETED.ordinal());
		query.setParameter("revNo", DateUtil.getRevisionNumber());
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public Integer findFarmersCountByStatusAndSeason(
			String currentSeason) {/*
									 * // TODO Auto-generated method stub
									 * 
									 * Session session =
									 * getSessionFactory().openSession(); Query
									 * query = session
									 * .createQuery("select count(*) from Farmer where status=1 and seasonCode=:seasonCode and statusCode=0"
									 * );
									 * 
									 * query.setParameter("status",
									 * ESETxnStatus.SUCCESS.ordinal());
									 * query.setParameter("seasonCode",
									 * currentSeason);
									 * 
									 * Integer val = ((Long)
									 * query.uniqueResult()).intValue();
									 * session.flush(); session.close(); return
									 * val;
									 */

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select count(*) from Farmer where status in (0,1) and statusCode=:statusCode and seasonCode=:seasonCode");

		query.setParameter("seasonCode", currentSeason);
		query.setParameter("statusCode", Integer.valueOf(ESETxnStatus.SUCCESS.ordinal()));

		Integer val = Integer.valueOf(((Long) query.uniqueResult()).intValue());
		session.flush();
		session.close();
		return val;
	}

	@Override
	public Object[] findTotalQtyAndAmt(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma,
			String selectedFieldStaff) {
		// TODO Auto-generated method stub
		/*
		 * Object[] obj=null; Session session =
		 * getSessionFactory().getCurrentSession(); Query query = session.
		 * createQuery("select sum(dd.quantity),sum(d.totalAmount),sum(d.paymentAmount) from DistributionDetail dd inner join dd.distribution d"
		 * ); if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) { obj =
		 * (Object[]) query.list().get(0); } return obj;
		 */

		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(dd.quantity),sum(d.totalAmount),sum(d.paymentAmount) from DistributionDetail dd inner join dd.distribution d where d.id is not null";
		if (!StringUtil.isEmpty(location)) {
			hqlQuery += " AND d.servicePointName =:location ";
			params.put("location", location);
		}

		if (!StringUtil.isEmpty(selectedFieldStaff) && selectedFieldStaff != null) {
			hqlQuery += " AND d.agentId =:selectedFieldStaff";
			params.put("selectedFieldStaff", selectedFieldStaff);

		}

		if (!StringUtil.isEmpty(seasonCode) && seasonCode != null) {
			hqlQuery += " AND d.seasonCode =:season";
			params.put("season", seasonCode);

		}

		if (!StringUtil.isEmpty(stateName) && stateName != null) {
			hqlQuery += " AND d.stateName =:stateName";
			params.put("stateName", stateName);

		}

		if (!StringUtil.isEmpty(fpo) && fpo != null) {
			hqlQuery += " AND d.fpo =:fpo";
			params.put("fpo", fpo);

		}

		if (!StringUtil.isEmpty(icsName) && icsName != null) {
			hqlQuery += " AND d.icsName =:icsName";
			params.put("icsName", icsName);

		}

		if (!StringUtil.isEmpty(farmerId) && farmerId != null) {
			hqlQuery += " AND d.farmerName =:farmerId";
			params.put("farmerId", farmerId);

		}

		if (!StringUtil.isEmpty(product) && product != null) {
			hqlQuery += " AND d.productId =:product";
			params.put("product", product);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public void updateDynamicFarmerFieldComponentType() {
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery(
				"update farmer_dynamic_field_value set COMPONENT_TYPE=(SELECT dfc.COMPONENT_TYPE from dynamic_fields_config dfc where dfc.component_code=FIELD_NAME);");
		int result = query.executeUpdate();

		query = session.createSQLQuery(
				"DELETE FROM farmer_dynamic_field_value where farmer_id is null AND REFERENCE_ID IS NULL");
		result = query.executeUpdate();

		session.flush();
		session.close();
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByRefId(String refId, String txnType) {
		Object[] values = { refId, txnType };
		return list(
				"select fdfv FROM FarmerDynamicFieldsValue fdfv LEFT JOIN FETCH fdfv.dymamicImageData di LEFT JOIN FETCH  fdfv.followUps where fdfv.referenceId=? AND fdfv.txnType=?",
				values);

	}

	@Override
	public Object[] findTotalQtyAndAmt(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma,
			String selectedFieldStaff, Date startDate, Date endDate, String branch) {
		// TODO Auto-generated method stub
		/*
		 * Object[] obj=null; Session session =
		 * getSessionFactory().getCurrentSession(); Query query = session.
		 * createQuery("select sum(dd.quantity),sum(d.totalAmount),sum(d.paymentAmount) from DistributionDetail dd inner join dd.distribution d"
		 * ); if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) { obj =
		 * (Object[]) query.list().get(0); } return obj;
		 */

		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(dd.quantity),sum(d.finalAmount),sum(d.paymentAmount) from DistributionDetail dd"
				+ " inner join dd.distribution d where d.farmerId is not null";
		hqlQuery += " AND d.txnType in (" + Distribution.PRODUCT_DISTRIBUTION_TO_FARMER + ","
				+ Distribution.PRODUCT_RETURN_FROM_FARMER + "," + Distribution.PRODUCT_DISTRIBUTION_FARMER_BALANCE
				+ ")";
		if (!StringUtil.isEmpty(branch) && branch != null) {
			hqlQuery += " AND d.branchId =:branch";
			params.put("branch", branch);

		}
		if (!StringUtil.isEmpty(location)) {
			hqlQuery += " AND d.servicePointName =:location ";
			params.put("location", location);
		}

		if (!StringUtil.isEmpty(selectedFieldStaff) && selectedFieldStaff != null) {
			hqlQuery += " AND d.agentName =:selectedFieldStaff";
			params.put("selectedFieldStaff", selectedFieldStaff);

		}

		if (!StringUtil.isEmpty(seasonCode) && seasonCode != null) {
			hqlQuery += " AND d.seasonCode =:season";
			params.put("season", seasonCode);

		}

		if (!StringUtil.isEmpty(stateName) && stateName != null) {
			hqlQuery += " AND d.stateName =:stateName";
			params.put("stateName", stateName);

		}

		if (!StringUtil.isEmpty(fpo) && fpo != null) {
			hqlQuery += " AND d.fpo =:fpo";
			params.put("fpo", fpo);

		}

		if (!StringUtil.isEmpty(icsName) && icsName != null) {
			hqlQuery += " AND d.icsName =:icsName";
			params.put("icsName", icsName);

		}

		if (!StringUtil.isEmpty(farmerId) && farmerId != null) {
			hqlQuery += " AND d.farmerName like '%" + farmerId + "%' ";
			// params.put("farmerId", farmerId);

		}

		if (!StringUtil.isEmpty(product) && product != null) {
			hqlQuery += " AND d.productId =:product";
			params.put("product", product);

		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND d.branchId =:branchId";
			params.put("branchId", branchIdParma);

		}

		if (!StringUtil.isEmpty(startDate) && endDate != null) {
			hqlQuery += " AND d.txnTime BETWEEN :startDate AND :endDate";
			params.put("startDate", startDate);
			params.put("endDate", endDate);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public Object[] findTotalQty(String warehouse, String order, String selectedProduct, String vendor, String receipt,
			String seasonCode, String branchIdParma, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(wpd.stock),sum(wp.totalAmount),sum(wpd.damagedStock) from WarehousePaymentDetails wpd inner join wpd.warehousePayment wp where wpd.stock is not null and wpd.damagedStock is not null";
		if (!StringUtil.isEmpty(warehouse)) {
			hqlQuery += " AND wp.warehouse.code =:warehouse ";
			params.put("warehouse", warehouse);
		}

		if (!StringUtil.isEmpty(order) && order != null) {
			hqlQuery += " AND wp.orderNo =:orderNo";
			params.put("orderNo", order);

		}

		if (!StringUtil.isEmpty(selectedProduct) && selectedProduct != null) {
			hqlQuery += " AND wpd.product.id =:selectedProduct";
			params.put("selectedProduct", Long.valueOf(selectedProduct));

		}

		if (!StringUtil.isEmpty(vendor) && vendor != null) {
			hqlQuery += " AND wp.vendor.vendorId =:vendor";
			params.put("vendor", vendor);

		}

		if (!StringUtil.isEmpty(receipt) && receipt != null) {
			hqlQuery += " AND wp.receiptNo =:receiptNo";
			params.put("receiptNo", receipt);

		}

		if (!StringUtil.isEmpty(seasonCode) && seasonCode != null) {
			hqlQuery += " AND wp.seasonCode =:seasonCode";
			params.put("seasonCode", seasonCode);

		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND wp.branchId =:branchId";
			params.put("branchId", branchIdParma);

		}

		if (!StringUtil.isEmpty(startDate) && endDate != null) {
			hqlQuery += " AND wp.trxnDate BETWEEN :startDate AND :endDate";
			params.put("startDate", startDate);
			params.put("endDate", endDate);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public Object[] findTotalAmtAndweightByProcurement(String hqlQueryAppnd, Map<String, Object> params) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		String hqlQuery = "select sum(pd.NetWeight),sum(p.totalProVal),sum(p.paymentAmount) from ProcurementDetail pd inner join pd.procurement p inner join p.farmer f where p.farmer.id is not null";

		if (!StringUtil.isEmpty(hqlQueryAppnd) && !StringUtil.isEmpty(params)) {
			hqlQuery += hqlQueryAppnd;

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public Object[] findTotalNoBagsAndNetWeg(String farmerId, String selectedCoOperative, String branchIdParma,
			String branch, String productId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(cw.numberOfBags),sum(cw.grossWeight) from CityWarehouse cw where cw.coOperative.id is not null ";
		if (!StringUtil.isEmpty(branch)) {
			hqlQuery += " AND cw.branchId =:branch";
			params.put("branch", branch);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			hqlQuery += " AND cw.farmer.id =:farmerId ";
			params.put("farmerId", farmerId);
		}

		if (!StringUtil.isEmpty(selectedCoOperative) && selectedCoOperative != null) {
			hqlQuery += " AND cw.coOperative.id =:selectedCoOperative";
			params.put("selectedCoOperative", Long.valueOf(selectedCoOperative));

		}

		if (!StringUtil.isEmpty(productId) && productId != null) {
			hqlQuery += " AND cw.procurementProduct.id =:productId";
			params.put("productId", Long.valueOf(productId));
		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND cw.branchId =:branchId";
			params.put("branchId", branchIdParma);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public List<Object[]> listIcNameByFarmer(String selectedFarmer) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery("select f.ICS_NAME from farmer f where f.ID='" + selectedFarmer + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<Object[]> listIcsStatusByFarmer(String selectedFarmer) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select fic.ICS_TYPE from farm fm INNER JOIN farm_ics_conversion fic where fic.FARM_ID = fm.ID  and  fm.status=1  and fm.FARMER_ID='"
						+ selectedFarmer + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<Object[]> listVillageByGroup(long id) {
		return list("SELECT DISTINCT f.village.id,f.village.name FROM Farmer f WHERE f.samithi.id=?", id);
	}

	public Object[] findTotalQtyInAgent(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma, String agentId,
			Date frmDate, Date toDate, String branch) {
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(dd.quantity),sum(dd.existingQuantity),sum(dd.currentQuantity) from DistributionDetail dd inner join dd.distribution d where d.agentId is not null";
		hqlQuery += " AND d.txnType in (" + Distribution.PRODUCT_DISTRIBUTION_TO_FIELDSTAFF + ","
				+ Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF + ")";
		if (!StringUtil.isEmpty(branch) && branch != null) {
			hqlQuery += " AND d.branchId =:branch";
			params.put("branch", branch);

		}
		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND d.branchId =:branchId";
			params.put("branchId", branchIdParma);

		}
		if (!StringUtil.isEmpty(location)) {
			hqlQuery += " AND d.servicePointName =:location ";
			params.put("location", location);
		}
		if (!StringUtil.isEmpty(seasonCode) && seasonCode != null) {
			hqlQuery += " AND d.seasonCode =:season";
			params.put("season", seasonCode);

		}
		if (!StringUtil.isEmpty(farmerId) && farmerId != null) {
			hqlQuery += " AND d.agentName like '%" + farmerId + "%' ";
			// params.put("farmerId", farmerId);

		}

		if (!StringUtil.isEmpty(stateName) && stateName != null) {
			hqlQuery += " AND d.stateName =:stateName";
			params.put("stateName", stateName);

		}

		if (!StringUtil.isEmpty(fpo) && fpo != null) {
			hqlQuery += " AND d.fpo =:fpo";
			params.put("fpo", fpo);

		}

		if (!StringUtil.isEmpty(icsName) && icsName != null) {
			hqlQuery += " AND d.icsName =:icsName";
			params.put("icsName", icsName);

		}

		if (!StringUtil.isEmpty(product) && product != null) {
			hqlQuery += " AND dd.product.id =:product";
			params.put("product", Long.valueOf(product));

		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND d.branchId =:branchId";
			params.put("branchId", branchIdParma);

		}

		if (!StringUtil.isEmpty(frmDate) && toDate != null) {
			hqlQuery += " AND d.txnTime BETWEEN :startDate AND :endDate";
			params.put("startDate", frmDate);
			params.put("endDate", toDate);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	public List<Object> findCropHavestQuantity(String farmerId, String seasonCode, String stateName, String farmCode,
			String crop, String icsName, String branchIdParma, Date startDate, Date endDate, String agentId) {
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(chd.qty) from CropHarvest ch inner join ch.cropHarvestDetails chd where chd.cropHarvest.id is not null";
		if (!StringUtil.isEmpty(startDate) && endDate != null) {
			hqlQuery += " AND ch.harvestDate BETWEEN :startDate AND :endDate";
			params.put("startDate", startDate);
			params.put("endDate", endDate);

		}
		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND ch.branchId =:branchId";
			params.put("branchId", branchIdParma);

		}
		if (!StringUtil.isEmpty(seasonCode) && seasonCode != null) {
			hqlQuery += " AND ch.seasonCode =:seasonCode";
			params.put("seasonCode", seasonCode);

		}
		if (!StringUtil.isEmpty(farmerId) && farmerId != null) {
			hqlQuery += " AND ch.farmerId =:farmerId";
			params.put("farmerId", farmerId);

		}
		if (!StringUtil.isEmpty(farmCode) && farmCode != null) {
			hqlQuery += " AND ch.farmCode =:farmCode";
			params.put("farmCode", farmCode);

		}
		if (!StringUtil.isEmpty(crop) && crop != null) {
			hqlQuery += " AND chd.crop.name =:crop";
			params.put("crop", crop);

		}
		if (!StringUtil.isEmpty(agentId) && agentId != null) {
			hqlQuery += " AND ch.agentId =:agentId";
			params.put("agentId", agentId);

		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();
	}

	public Object[] findCropSaleQtyAmt(String farmerId, String seasonCode, String buyerInfo, String stateName,
			String fpo, String icsName, String branchIdParma, Date convertStringToDate, Date convertStringToDate2,
			String agentId, String subBranchIdParam) {
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(csd.qty),sum(csd.subTotal),sum(cs.paymentInfo) from CropSupply cs inner join cs.cropSupplyDetails csd where csd.cropSupply.id is not null";
		if (!StringUtil.isEmpty(convertStringToDate) && convertStringToDate2 != null) {
			hqlQuery += " AND cs.dateOfSale BETWEEN :startDate AND :endDate";
			params.put("startDate", convertStringToDate);
			params.put("endDate", convertStringToDate2);

		}
		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {

			if (!StringUtil.isEmpty(subBranchIdParam) && subBranchIdParam != null) {

				hqlQuery += " AND cs.branchId =:branchId";
				params.put("branchId", subBranchIdParam);
			} else {

				hqlQuery += " AND cs.branchId =:branchId";
				params.put("branchId", branchIdParma);
			}

		}
		if (!StringUtil.isEmpty(seasonCode) && seasonCode != null) {
			hqlQuery += " AND cs.currentSeasonCode =:seasonCode";
			params.put("seasonCode", seasonCode);

		}
		if (!StringUtil.isEmpty(buyerInfo) && buyerInfo != null) {
			hqlQuery += " AND cs.buyerInfo.id =:buyerInfo";
			params.put("buyerInfo", Long.valueOf(buyerInfo));

		}
		if (!StringUtil.isEmpty(farmerId) && farmerId != null) {
			hqlQuery += " AND cs.farmerId =:farmerId";
			params.put("farmerId", farmerId);

		}
		if (!StringUtil.isEmpty(agentId) && agentId != null) {
			hqlQuery += " AND cs.agentId =:agentId";
			params.put("agentId", agentId);

		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public List<Object[]> listICSByGroup(long id) {
		return list("SELECT DISTINCT f.icsName,f.icsCode FROM Farmer f WHERE f.samithi.id=? AND f.icsName IS NOT NULL",
				id);
	}

	@Override
	public List<Object[]> findDynamicSectionFieldsByTxnType(String txnType) {
		// TODO Auto-generated method stub
		return list(
				"SELECT dfc.id,dfc.code,dfc.componentName,dfc.dynamicSectionConfig.id FROM DynamicSectionConfig dsc INNER JOIN dsc.dynamicFieldConfigs dfc WHERE dfc.isReportAvail='1' ORDER BY dfc.orderSet");
	}

	@Override
	public List<Object[]> findDynamicSectionFields() {

		try {
			List<Object[]> result = list("SELECT dsc.sectionCode,dsc.sectionName FROM DynamicSectionConfig dsc ");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<DynamicSectionConfig> listDynamicSectionByTxnType(String txnType) {
		// TODO Auto-generated method stub

		return list("FROM DynamicSectionConfig dsc WHERE dsc.mTxnType=?", txnType);
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByTxnId(String selectedObject) {

		return list(
				"select fdfv FROM FarmerDynamicFieldsValue fdfv LEFT JOIN FETCH fdfv.dymamicImageData LEFT JOIN FETCH  fdfv.followUps  where fdfv.farmerDynamicData.id=?",
				Long.valueOf(selectedObject));
	}

	@Override
	public FarmerDynamicData findFarmerDynamicData(String id) {

		return (FarmerDynamicData) find("FROM FarmerDynamicData fdfv where fdfv.id=?", Long.valueOf(id));
	}

	@Override
	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery = "select count(distinct f.id) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 and f.statusCode=0 and fc.status=1 AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));
		} else {
			hqlQuery = "select count(*) FROM Farmer f WHERE f.status=1 and f.statusCode=0";
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}

		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}
		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id=:selectedFarmer";
			params.put("selectedFarmer", Long.valueOf(selectedFarmer));

		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);

		}

		if (!selectedOrganicStatus.equalsIgnoreCase("conventional")) {
			if (!StringUtil.isEmpty(selectedCrop)) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0,1,2)");
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				}
			} else {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					hqlQuery += " AND f.id in (:fList)";

					params.put("fList", icsMap);
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0 ,1 ,2)");
					hqlQuery += " AND f.id in (:fList)";

					params.put("fList", icsMap);
				} else {
					hqlQuery += " AND f.isCertifiedFarmer in (0,1)";
				}
			}
		}

		else {
			hqlQuery += " AND f.isCertifiedFarmer=0";
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			if (str.equalsIgnoreCase("fList")) {
				List<Long> result = (List<Long>) params.get(str);
				query.setParameterList(str, result);

			} else {
				query.setParameter(str, params.get(str));
			}
		}

		Integer val = 0;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).intValue();
		}
		return val;

	}

	@Override
	public Object[] findTotalAcreAndEstYield(String selectedCrop, String selectedTaluk, String selectedVillage,
			String selectedSeason) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT  SUM(fc.cultiArea),sum(fc.actualSeedYield),sum(fc.estimatedYield) FROM FarmCrops fc INNER JOIN fc.farm.farmer f WHERE  fc.status=1 and f.status=1";
		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}

		if (!StringUtil.isEmpty(selectedCrop) && selectedCrop != null) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));

		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {

			val = (Object[]) obj;
		}
		return val;
	}

	@Override
	public Object[] findPeriodicInsDateByFarmCode(String farmCode) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		String hql = "select pi.lastUpdatedDT,pi.lastUpdatedUserName from PeriodicInspection pi where pi.farmId=:farmId group by pi.farm.farmCode";
		Query query = session.createQuery(hql);
		query.setParameter("farmId", farmCode);

		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}
		return obj;
	}

	@Override
	public Farmer findFarmerByMsgNo(String msgNo) {
		// TODO Auto-generated method stub
		return (Farmer) find("FROM Farmer f WHERE f.msgNo=?", msgNo);
	}

	@Override
	public List<Object[]> listFarmFieldsByFarmerIdByAgentIdAndSeason(long id, Long revisionNo) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().getCurrentSession();
		/*
		 * Query query = session.createQuery(
		 * "SELECT fm.id,COALESCE(fm.farmCode,''),COALESCE(fm.farmName,''),COALESCE(fm.farmId,''),COALESCE(fm.farmDetailedInfo.lastDateOfChemicalApplication,''),COALESCE(fm.isVerified,'0'),fm.farmer.farmerId,fm.cows.size,fm.farmICSConversion.size,fm.farmer.id,fm.status,fm.revisionNo FROM Farm fm WHERE fm.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  fm.revisionNo > :revisionNo    and  fm.status=1  AND fm.farmer.statusCode in  (:status1)  order by fm.revisionNo DESC"
		 * );
		 */
		Query query = session.createQuery(
				"SELECT fm.id,COALESCE(fm.farmCode,''),COALESCE(fm.farmName,''),COALESCE(fm.farmId,''),COALESCE(fm.farmDetailedInfo.lastDateOfChemicalApplication,''),COALESCE(fm.isVerified,'0'),fm.farmer.farmerId,fm.farmICSConversion.size,fm.farmer.id,fm.status,fm.plottingStatus,fm.latitude,fm.longitude,fm.farmDetailedInfo.proposedPlantingArea,fm.farmDetailedInfo.totalLandHolding,fm.revisionNo FROM Farm fm WHERE fm.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  fm.revisionNo > :revisionNo    and  fm.status in (0,1)  AND fm.farmer.statusCode in  (:status1)  order by fm.revisionNo DESC");
		query.setParameter("id", id);
		query.setParameter("revisionNo", revisionNo);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List result = query.list();

		return result;

	}

	@Override
	public List<Object[]> listFarmCropsFieldsByFarmerIdByAgentIdAndSeason(long id, Long revisionNo) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT fcp.id,COALESCE(fcp.procurementVariety.code,''),COALESCE(fcp.procurementVariety.procurementProduct.code,''),COALESCE(fcp.seedSource,''),COALESCE(fcp.seedQtyCost,''),COALESCE(fcp.estimatedYield,''),COALESCE(fcp.cropSeason.code,''),COALESCE(fcp.cropCategory,''),COALESCE(fcp.farm.farmCode,''),COALESCE(fcp.farm.farmer.farmerId,''),COALESCE(fcp.riskAssesment,''),COALESCE(fcp.seedTreatmentDetails,''),COALESCE(fcp.otherSeedTreatmentDetails,''),COALESCE(fcp.stapleLength,''),COALESCE(fcp.seedQtyUsed,''),COALESCE(fcp.type,''),COALESCE(fcp.cropCategoryList,''),COALESCE(fcp.cultiArea,''),COALESCE(fcp.sowingDate,''),COALESCE(fcp.estimatedHarvestDate,''),COALESCE(fcp.interType,''),COALESCE(fcp.interAcre,''),COALESCE(fcp.totalCropHarv,''),COALESCE(fcp.grossIncome,''),fcp.farm.id,fcp.farm.farmer.id,fcp.status,fcp.revisionNo,fcp.cropEditStatus  FROM FarmCrops fcp WHERE fcp.farm.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  fcp.revisionNo > :revisionNo   and  fcp.status=1  and  fcp.farm.status=1  AND  fcp.farm.farmer.statusCode in  (:status1)  order by fcp.revisionNo DESC");
		query.setParameter("id", id);
		query.setParameter("revisionNo", revisionNo);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List result = query.list();

		return result;

	}

	@Override
	public Long listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(long id) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT  MAX(COALESCE(fm.revisionNo,0)) FROM Farm fm WHERE fm.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) and  fm.status in (0,1)  AND fm.farmer.statusCode in  (:status1)  order by fm.revisionNo DESC");
		query.setParameter("id", id);
		// query.setParameter("revisionNo", revisionNo);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List list = query.list();
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0))) {
			return (Long) list.get(0);
		}
		return 0l;
	}

	@Override
	public Long listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(long id) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT   MAX(COALESCE(fcp.revisionNo,0))  FROM FarmCrops fcp WHERE fcp.farm.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id)   and  fcp.status=1  and  fcp.farm.status=1  AND  fcp.farm.farmer.statusCode in  (:status1)  order by fcp.revisionNo DESC");
		query.setParameter("id", id);
		// query.setParameter("revisionNo", revisionNo);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List list = query.list();
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0))) {
			return (Long) list.get(0);
		}
		return 0l;
	}

	@Override
	public List<Object[]> findTotalAreaProdByOrg(String selectedSeason, String selectedGender, String selectedState,
			String branchId) {
		Session session = getSessionFactory().getCurrentSession();

		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT fc.farm.farmer.branchId,sum(cultiArea) from FarmCrops fc where fc.farm.farmer.status=1 and fc.status=1";
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND fc.farm.farmer.seasonCode=:selectedSeason";
			params.put("selectedSeason", selectedSeason);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND fc.farm.farmer.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND fc.farm.farmer.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (StringUtil.isEmpty(branchId)) {
			String groupBy = " group by fc.farm.farmer.branchId";
			hqlQuery += groupBy;
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();
	}

	@Override
	public List<Object[]> findTotalAreaProdByIcs(String selectedSeason) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		String hqlQuery = "SELECT fic.icsType,sum(fc.cultiArea) from FarmCrops fc"
				+ " inner join fc.farm fa inner join fa.farmer f inner join fa.farmICSConversion fic"
				+ "  where fic.icsType!=-1 and fc.status=1 AND f.status=1 AND f.seasonCode =:season group by fic.icsType";
		Query query = session.createQuery(hqlQuery);
		query.setParameter("season", selectedSeason);
		return query.list();
	}

	public List<MasterData> listMasterType() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='1'");
	}

	@Override
	public GMO findGMOById(Long gmoId) {
		// TODO Auto-generated method stub
		return (GMO) find("FROM GMO gmo WHERE gmo.id= ? ", gmoId);
	}

	@Override
	public int findMaxTypeId() {
		// TODO Auto-generated method stub
		int maxId = 0;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select max(typeId) FROM GMO group by typeId");
		List list = query.list();
		if (!ObjectUtil.isListEmpty(list) && !ObjectUtil.isEmpty(list.get(0))) {
			maxId = (Integer) list.get(0);
		}
		return maxId;
	}

	@Override
	public GinnerQuantitySold findGinnerQtySoldById(Long id) {
		// TODO Auto-generated method stub
		return (GinnerQuantitySold) find("FROM GinnerQuantitySold gq WHERE gq.id= ? ", id);
	}

	@Override
	public List<Object[]> findTotalGinnerQty(String selectedSeason, String selectedBranch) {
		// TODO Auto-generated method stub
		// String groupBy = " GROUP BY gs.name";
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select gs.name,gs.quantity,gs.address,gs.branchId FROM GinnerQuantitySold gs where gs.name is not null ";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND gs.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND gs.seasonCode=:selectedSeason";
			params.put("selectedSeason", selectedSeason);

			/*
			 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			 * params.put("prevYear", (selectedYear - 1));
			 */
		}
		// hqlQuery += groupBy;
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();
	}

	@Override
	public List<Object[]> findGmoPercentage(String selectedSeason, String selectedBranch) {
		// TODO Auto-generated method stub
		String groupBy = " GROUP BY gm.type";
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select gm.type,sum(gm.contaminationPercentage),sum(gm.noOfSamples),sum(gm.noOfPositive) FROM GMO gm where gm.type is not null ";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND gm.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += "  AND gm.seasonCode=:selectedSeason";
			params.put("selectedSeason", selectedSeason);

			/*
			 * hqlQuery += "  OR Year(f.createdDate)=:prevYear)";
			 * params.put("prevYear", (selectedYear - 1));
			 */
		}

		hqlQuery += groupBy;
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();
	}

	@Override
	public void updateDynmaicImage(FarmerDynamicFieldsValue fdfv, String parentId) {
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery("UPDATE farmer_dynamic_field_value SET FARMER_DYNAMIC_DATA_ID=" + parentId
				+ " WHERE ID=" + fdfv.getId() + "");
		query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public DynamicImageData findDynamicImageDataById(Long id) {
		// return (DynamicImageData) find("FROM DynamicImageData dd WHERE
		// dd.id=?",id);
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM DynamicImageData dd JOIN FETCH dd.farmerDynamicFieldsValue WHERE dd.id=:id");
		query.setParameter("id", id);
		List<DynamicImageData> list = query.list();
		session.flush();
		session.close();
		if (list.size() > 0) {
			DynamicImageData dynamicImageData = list.get(0);
			return dynamicImageData;
		} else {
			return null;
		}
	}

	@Override
	public List<Object[]> listProcurementTraceabilityCity() {
		return list("SELECT DISTINCT pt.farmer.city.code,pt.farmer.city.name from ProcurementTraceability pt");
	}

	@Override
	public List<Object[]> listProcurementTraceabilityVillage() {
		return list("SELECT DISTINCT pt.farmer.village.code,pt.farmer.village.name from ProcurementTraceability pt");
	}

	@Override
	public List<Object> listFarmerFpo() {

		String query = "select DISTINCT(f.FPO) FROM procurement_traceability pt INNER JOIN farmer f where f.ID=pt.FARMER_ID";
		Session session = getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		List<Object> list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<Object> listOfICS() {

		return list("SELECT DISTINCT f.icsName from Farmer f)");
	}

	@Override
	public List<Object[]> listcooperativeByFarmer(String selectedFarmer) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery("select fm.fpo from farmer fm where fm.ID='" + selectedFarmer + "'");
		List result = query.list();
		return result;
	}

	@Override
	public List<Object[]> findfpoByFarmerID(long id) {
		return list("select f.fpo,f.city FROM Farmer f WHERE f.id = ? ", id);
	}

	@Override
	public double findTotalStockInHeap(String gining, String procurementProduct, String ics, String heapDataName,
			String branchIdParma, String season) {

		Session session = getSessionFactory().getCurrentSession();
		double obj = 0.0;
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select sum(hd.totalStock) from HeapData hd WHERE hd.ics!=null";

		if (!StringUtil.isEmpty(gining)) {
			hqlQuery += " AND hd.ginning.id =:giningId";
			params.put("giningId", Long.valueOf(gining));
		}

		if (!StringUtil.isEmpty(procurementProduct) && procurementProduct != null) {
			hqlQuery += " AND hd.procurementProduct.id =:procurementProductId";
			params.put("procurementProductId", Long.valueOf(procurementProduct));
		}

		if (!StringUtil.isEmpty(ics) && ics != null) {
			hqlQuery += " AND hd.ics =:ics";
			params.put("ics", ics);
		}

		if (!StringUtil.isEmpty(heapDataName) && heapDataName != null) {
			hqlQuery += " AND hd.name =:heapDataName";
			params.put("heapDataName", heapDataName);
		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND hd.branchId =:branchId";
			params.put("branchId", branchIdParma);
		}
		hqlQuery += " AND hd.season=:season";
		params.put("season", season);

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			if (query.list().get(0) != null) {
				obj = (double) query.list().get(0);
			}
		}
		return obj;
	}

	@Override
	public List<Object[]> findFarmerDatasByFarmerID(long id) {
		// TODO Auto-generated method stub
		return list(
				" select f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,f.city.name,f.village.name,f.samithi.name,f.icsName,f.fpo FROM Farmer f where f.id=? ",
				id);
	}

	@Override
	public List<DynamicFeildMenuConfig> listDynamicMenus() {
		return list("FROM DynamicFeildMenuConfig dsc order by dsc.order asc ");
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldsValuePhotoByRefTxnType(String refId, String txnType) {
		Object[] values = { refId, txnType };
		return (List<FarmerDynamicFieldsValue>) list(
				"FROM FarmerDynamicFieldsValue fdfv INNER JOIN FETCH fdfv.dymamicImageData dImage LEFT JOIN FETCH  fdfv.followUps  WHERE fdfv.referenceId=? AND fdfv.txnType=?",
				values);
	}

	public List<DynamicFieldConfig> getListComponent(String sectionCode) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"FROM DynamicFieldConfig dfc WHERE dfc.componentType = :type AND dfc.dynamicSectionConfig.sectionCode = :section_code");
		query.setParameter("type", "8");
		query.setParameter("section_code", sectionCode);
		List<DynamicFieldConfig> listComponent = query.list();
		session.flush();
		session.close();
		return listComponent;
	}

	@Override
	public DynamicFieldConfig findDynamicFieldConfigById(Long id) {

		return (DynamicFieldConfig) find("FROM DynamicFieldConfig dfc  WHERE dfc.id=?", id);

	}

	@Override
	public CottonPrice findProdPriceById(Long id) {
		// TODO Auto-generated method stub
		return (CottonPrice) find("FROM CottonPrice prodPrice WHERE prodPrice.id= ? ", id);
	}

	@Override
	public String findMspByStapleLen(String selectedSeason, String selectedStapleLen) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		String obj = "";
		Session session = getSessionFactory().getCurrentSession();
		String hqlQuery = "SELECT cp.msp FROM CottonPrice cp where cp.msp is not null ";
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND cp.seasonCode =:selectedSeason";
			params.put("selectedSeason", selectedSeason);
		}

		if (!StringUtil.isEmpty(selectedStapleLen) && selectedStapleLen != null) {
			hqlQuery += " AND cp.stapleLength =:selectedStapleLen";
			params.put("selectedStapleLen", selectedStapleLen);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		List list = query.list();
		if (list.size() > 0) {
			obj = (String) list.get(0);
		}
		return obj;
	}

	@Override
	public GMO findGMOType(String type, String season) {
		// TODO Auto-generated method stub
		Object[] values = { type, season };
		return (GMO) find("FROM GMO where type=? AND seasonCode=?", values);
	}

	@Override
	public CottonPrice findStapleByCottonPrice(String staple, String seasonCode) {
		// TODO Auto-generated method stub
		Object[] values = { staple, seasonCode };
		return (CottonPrice) find("FROM CottonPrice cp  WHERE cp.stapleLength= ? AND cp.seasonCode=?", values);
	}

	@Override
	public List<Object[]> listFarmerIDAndName() {
		return list("SELECT DISTINCT f.id,f.farmerId,f.firstName,f.lastName from Farmer f where f.status=1");
	}

	@Override
	public List<Object[]> listFarmIDAndName() {
		return list("SELECT DISTINCT f.id,f.farmCode,f.farmName from Farm f");
	}

	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String type) {

		// return list("FROM DynamicFeildMenuConfig dsc WHERE dsc.txnType = ?
		// ORDER BY dsc.order ASC", type);
		return list(
				"select distinct dsc FROM DynamicFeildMenuConfig dsc join fetch dsc.dynamicSectionConfigs dss left join fetch dss.section ds left join fetch ds.languagePreferences join fetch dsc.dynamicFieldConfigs  dc  join fetch dc.field ff   left join fetch ff.languagePreferences join fetch ff.dynamicSectionConfig sec join fetch dc.field WHERE dsc.txnType = ? ORDER BY dsc.order ASC",
				type);
	}

	@Override
	public List<Object> listFarmerFpoFromTraceabilityStock() {
		List<Object> result = list("SELECT DISTINCT ps.farmer.fpo FROM ProcurementTraceabilityStockDetails ps");
		return result;
	}

	@Override
	public Object[] listFarmInfoByCode(String code) {
		// 0=Id, 1=code, 2=Name
		return (Object[]) find("SELECT f.id,f.farmCode,f.farmName FROM Farm f WHERE f.farmCode=? AND f.status=1", code);
	}

	@Override
	public List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntityAndTxn(String entity, String txnType) {
		Object[] values = { entity, txnType };
		return (List<DynamicFieldReportJoinMap>) list("FROM DynamicFieldReportJoinMap WHERE entityType=? AND txnType=?",
				values);
	}

	@Override
	public List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntityAndTxn(String entity, String txnType) {
		Object[] values = { entity, txnType };
		return (List<DynamicFieldReportConfig>) list(
				"FROM DynamicFieldReportConfig dfrg WHERE dfrg.entityType=? AND dfrg.txnType=? ORDER BY dfrg.orderz ASC",
				values);
	}

	@Override
	public void updateFarmICSStatusByFarmId(long farmId, String icsType) {

		Session session = getSessionFactory().openSession();
		Query query;
		if (icsType.equalsIgnoreCase("3")) {
			query = session.createQuery(
					"UPDATE FarmIcsConversion fic SET  fic.icsType=:icsType,fic.organicStatus=:organicStatus WHERE fic.farm.id = :farmId");
			query.setParameter("organicStatus", "3");

		} else {
			query = session
					.createQuery("UPDATE FarmIcsConversion fic SET  fic.icsType=:icsType WHERE fic.farm.id = :farmId");
		}
		query.setParameter("farmId", farmId);
		query.setParameter("icsType", icsType);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@SuppressWarnings("unchecked")
	public List<CropSupplyImages> listCropSupplyImages(long id) {

		return list("FROM CropSupplyImages ci WHERE ci.cropSupply.id=?", id);
	}

	public CropSupplyImages loadCropSupplyImages(Long id) {

		return (CropSupplyImages) find("FROM CropSupplyImages ci WHERE ci.id=?", id);
	}

	@Override
	public List<Object[]> findDynamicSectionByMenuId(Long menuId) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT dsf.id,dsf.section_Code,dsf.section_Name from dynamic_section_config dsf LEFT JOIN dynamic_menu_section_map dmsm ON dmsm.SECTION_ID = dsf.id WHERE dmsm.MENU_ID="
				+ menuId;

		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public List<Object[]> findDynamicFieldsByMenuId(Long menuId) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT dfc.id,dfc.component_Code,dfc.component_Name from dynamic_fields_config dfc LEFT JOIN dynamic_menu_field_map dmfm ON dmfm.FIELD_ID = dfc.id WHERE dmfm.MENU_ID="
				+ menuId;

		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public DynamicSectionConfig findDynamicSectionByName(String sectionName) {
		// TODO Auto-generated method stub
		return (DynamicSectionConfig) find("FROM DynamicSectionConfig ds WHERE ds.sectionName = ? ", sectionName);
	}

	@Override
	public List<String> listOfDynamicSections() {
		// TODO Auto-generated method stub
		return list("SELECT dsc.sectionName FROM DynamicSectionConfig dsc order by dsc.id asc ");
	}

	@Override
	public List<Object[]> listOfDynamicFields(List<String> selectedSections) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		String queryString = " SELECT dfc.id AS FDC_ID,dfc.component_Code as FDC_CODE,dfc.component_Name,pdfc.id,pdfc.component_Code from dynamic_fields_config dfc left join dynamic_fields_config pdfc on dfc.reference_id = pdfc.id where dfc.section_id in:selectedSections ";
		Query query = session.createSQLQuery(queryString);
		query.setParameterList("selectedSections", selectedSections);
		List list = query.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public List<String> listOfDynamicFields() {
		// TODO Auto-generated method stub
		return list("SELECT dsc.componentName FROM DynamicFieldConfig dsc order by dsc.id asc ");
	}

	@Override
	public List<DynamicFieldConfig> listDynamicFieldsBySectionId(List<String> selectedSection) {
		// TODO Auto-generated method stub
		List<DynamicFieldConfig> list = null;
		Session session = getSessionFactory().getCurrentSession();
		/*
		 * Query query = session.createQuery(
		 * "FROM DynamicFieldConfig df where df.componentType!='10' AND df.dynamicSectionConfig.sectionCode in :selectedSection"
		 * ); query.setParameterList("selectedSection", selectedSection);
		 */
		Query query = session.createQuery(
				"FROM DynamicFieldConfig df where df.dynamicSectionConfig.sectionCode in :selectedSection");
		query.setParameterList("selectedSection", selectedSection);
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			list = query.list();
		}
		return list;
	}

	@Override
	public List<DynamicSectionConfig> listDynamicSectionsBySectionId(List<String> selectedSection) {
		// TODO Auto-generated method stub
		List<DynamicSectionConfig> list = null;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM DynamicSectionConfig df where df.sectionCode in :selectedSection");
		query.setParameterList("selectedSection", selectedSection);
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			list = query.list();
		}
		return list;
	}

	@Override
	public List<DynamicFieldConfig> listDynamicFieldsByCodes(List<String> selectedSection) {
		// TODO Auto-generated method stub
		List<DynamicFieldConfig> list = null;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM DynamicFieldConfig df where df.code in :selectedSection");
		query.setParameterList("selectedSection", selectedSection);
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			list = query.list();
		}
		return list;
	}

	@Override
	public DynamicFeildMenuConfig findDynamicMenuConfigById(Long id) {
		// TODO Auto-generated method stub
		return (DynamicFeildMenuConfig) find("FROM DynamicFeildMenuConfig dfc WHERE dfc.id = ?", id);
	}

	@Override
	public List<Object[]> findSalePriceByFarmCodes(List<String> farmCodes) {
		// TODO Auto-generated method stub
		String sqlQuery = "SELECT sum(coc.SALE_PRICE),coc.branch_id FROM cultivation coc where coc.FARM_ID in:farmCodes group by coc.branch_Id";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sqlQuery);
		query.setParameterList("farmCodes", farmCodes);
		List list = query.list();
		return list;
	}

	@Override
	public List<String> findFarmCodsByStapleLen(String selectedSeason, String selectedStapleLen, String branch) {
		// TODO Auto-generated method stub
		Object[] params = { selectedStapleLen, selectedSeason, branch };
		return list(
				"SELECT fc.farm.farmCode FROM FarmCrops fc where fc.stapleLength=? AND fc.cropSeason.code=? AND fc.branchId=?",
				params);
	}

	public Farm findFarmByID(Long id) {

		return (Farm) find("FROM Farm fm WHERE fm.id  = ?", id);
	}

	public List<Object[]> getParentMenus() {
		// TODO Auto-generated method stub
		return list("SELECT m.id,m.label FROM Menu m where m.parentId = NULL");
	}

	public List<Object[]> getAction() {
		return list("SELECT action.id,action.name FROM Action action");
	}

	public List<Object[]> getRole() {
		return list("SELECT r.id,r.name FROM Role r");
	}

	public List<Object[]> findSubMenusByParentMenuId(String parentMenuId) {
		// TODO Auto-generated method stub
		return list("SELECT m.id,m.description,m.label FROM Menu m where m.parentId = ? order by m.order ASC",
				Long.valueOf(parentMenuId));
	}

	public void delete_subMenu(Long id, String name) {

		Session session = getSessionFactory().openSession();

		String query1 = "DELETE from ese_role_menu where MENU_ID =" + id;
		SQLQuery sqlQuery1 = session.createSQLQuery(query1);
		sqlQuery1.executeUpdate();

		name = name.concat("%");
		String query2 = "select id from ese_ent where `NAME` LIKE " + "'" + name + "'";
		SQLQuery sqlQuery2 = session.createSQLQuery(query2);
		List<BigInteger> ese_ent_ID = sqlQuery2.list();

		for (BigInteger long1 : ese_ent_ID) {
			System.out.println(long1);

			String query4 = "DELETE from ese_role_ent WHERE ENT_ID =" + long1;
			SQLQuery sqlQuery4 = session.createSQLQuery(query4);
			sqlQuery4.executeUpdate();

		}
		String query3 = "DELETE from ese_ent where `NAME` LIKE " + "'" + name + "'";
		SQLQuery sqlQuery3 = session.createSQLQuery(query3);
		sqlQuery3.executeUpdate();

		String query5 = "DELETE  from ese_menu_action where MENU_ID =" + id;
		SQLQuery sqlQuery5 = session.createSQLQuery(query5);
		sqlQuery5.executeUpdate();

		String query6 = "DELETE  from ese_menu where id =" + id;
		SQLQuery sqlQuery6 = session.createSQLQuery(query6);
		sqlQuery6.executeUpdate();

		session.flush();
		session.close();
	}

	public void save_subMenu(String parentId1, String menuName, String menuDes, String menuUrl, String menuOrder1,
			String ese_ent_name, String ese_action_actionId1, String role_id1) {

		Long parentId = Long.valueOf(parentId1);
		int menuOrder = Integer.parseInt(menuOrder1);
		String ese_actionId = String.valueOf(ese_action_actionId1);

		String[] val = ese_action_actionId1.split(",");

		Long role_id = Long.valueOf(role_id1);

		Session session = getSessionFactory().openSession();

		String query1 = "INSERT INTO `ese_menu` VALUES (NULL ," + "'" + ese_ent_name + "'" + "," + "'" + menuDes + "'"
				+ ", " + "'" + menuUrl + "'" + ", " + "'" + menuOrder + "'" + "," + "'" + parentId + "'"
				+ ",'0','0','0','0','NULL')";
		SQLQuery sqlQuery1 = session.createSQLQuery(query1);
		sqlQuery1.executeUpdate();

		if (ese_action_actionId1.contains(",")) {
			Arrays.asList(ese_action_actionId1.split(",")).stream().forEach(u -> {
				String query2 = "INSERT INTO `ese_menu_action` VALUES ((SELECT id FROM ese_menu WHERE URL=" + "'"
						+ menuUrl + "'" + "), " + "'" + u.split("~")[1].trim() + "'" + ")";
				SQLQuery sqlQuery2 = session.createSQLQuery(query2);
				sqlQuery2.executeUpdate();
				Entitlement ent = (Entitlement) find("FROM Entitlement e WHERE e.authority = ?",
						ese_ent_name + "." + u.split("~")[0].trim());

				if (ent == null) {

					String query3 = "INSERT INTO `ese_ent` VALUES (NULL, " + "'" + ese_ent_name + "."
							+ u.split("~")[0].trim() + "'" + ")";
					SQLQuery sqlQuery3 = session.createSQLQuery(query3);
					sqlQuery3.executeUpdate();
				}
				String query4 = "INSERT INTO `ese_role_ent` VALUES (" + "'" + role_id + "'"
						+ ", (SELECT id FROM ese_ent WHERE NAME=" + "'" + ese_ent_name + "." + u.split("~")[0].trim()
						+ "' limit 1" + "))";
				SQLQuery sqlQuery4 = session.createSQLQuery(query4);
				sqlQuery4.executeUpdate();

			});

		} else {

			String query2 = "INSERT INTO `ese_menu_action` VALUES ((SELECT id FROM ese_menu WHERE URL=" + "'" + menuUrl
					+ "'" + "), " + "'" + ese_actionId + "'" + ")";
			SQLQuery sqlQuery2 = session.createSQLQuery(query2);
			sqlQuery2.executeUpdate();
			Entitlement ent = (Entitlement) find("FROM Entitlement e WHERE e.authority = ?", ese_ent_name);

			if (ent == null) {

				String query3 = "INSERT INTO `ese_ent` VALUES (NULL, " + "'" + ese_ent_name + "'" + ")";
				SQLQuery sqlQuery3 = session.createSQLQuery(query3);
				sqlQuery3.executeUpdate();
			}
			String query4 = "INSERT INTO `ese_role_ent` VALUES (" + "'" + role_id + "'"
					+ ", (SELECT id FROM ese_ent WHERE NAME=" + "'" + ese_ent_name + "' limit 1" + "))";
			SQLQuery sqlQuery4 = session.createSQLQuery(query4);
			sqlQuery4.executeUpdate();

		}

		String query5 = "INSERT INTO `ese_role_menu` VALUES (NULL, (SELECT MAX(id) FROM ese_menu), " + "'" + role_id
				+ "'" + ")";
		SQLQuery sqlQuery5 = session.createSQLQuery(query5);
		sqlQuery5.executeUpdate();

		session.flush();
		session.close();

	}

	@Override
	public List<FarmCatalogue> listHeapData() {
		Object[] values = { FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue cat WHERE cat.typez='117' AND cat.status=?", values);
	}

	@Override
	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String typez) {
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();

		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery = "select count(f.id) as count,f.samithi.name as group,f.village.name as village from Farmer f WHERE f.status=1 AND f.samithi.code=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		} else {
			hqlQuery = "select count(*) as count,f.samithi.name as group,f.village.name as village from Farmer f where f.status =1 ";
		}
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}

		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		if (!StringUtil.isEmpty(typez)) {
			hqlQuery += " AND f.typez=:typez";
			params.put("typez", typez);
		}

		Query query = session.createQuery(hqlQuery + "  GROUP BY f.samithi order by count(*) desc");
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}

	@Override
	public Long findCropIdByFarmCode(String farmCode) {
		// TODO Auto-generated method stub
		Long value = 0L;
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select fc.procurementVariety.procurementProduct.id from FarmCrops fc WHERE fc.farm.farmCode=:farmCode");
		query.setParameter("farmCode", farmCode);
		List list = query.list();
		if (list.size() > 0) {
			value = (Long) list.get(0);
		}

		session.flush();
		session.close();
		return value;
	}

	@Override
	public void removeFarmCoordinates(Long id) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE Coordinates c WHERE c.farm.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public Farm listOfFarmCoordinateByFarmId(long id) {

		return (Farm) find("FROM Farm fr WHERE fr.id = ?", id);
	}

	@Override
	public List<FarmCrops> listFarmCropsByFarmId(long farmId) {
		// TODO Auto-generated method stub
		return list("FROM FarmCrops fc where fc.farm.id = ? AND fc.status='1'", farmId);
	}

	@Override
	public List<Object[]> listFarmerBySamithiId(long id) {
		// TODO Auto-generated method stub
		return (List<Object[]>) list(
				"select f.farmerId,f.firstName,f.lastName,f.id FROM Farmer f WHERE f.samithi.id=? ", id);
	}

	@Override
	public void removeFarmCropCoordinates(Long id) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE FarmCropsCoordinates fc WHERE fc.farmCrops.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<FarmerLocationMapField> listRemoveFarmerLocMapFields() {
		// TODO Auto-generated method stub
		Object[] values = { FarmerLocationMapField.ACTIVE };
		return list("From FarmerLocationMapField fm WHERE fm.status=?", values);
	}

	public void deleteListFields(Long id) {
		Session session = getSessionFactory().openSession();
		String hql = "DELETE FROM DynamicFieldConfig " + "WHERE referenceId = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	public DynamicSectionConfig findDynamicSectionConfigById(Long id) {
		return (DynamicSectionConfig) find("FROM DynamicSectionConfig dsc  WHERE dsc.id=?", id);
	}

	@Override
	public List<Object[]> listOfFarmInfo() {
		// TODO Auto-generated method stub
		return list("SELECT f.id,f.farmName from Farm f");
	}

	@Override
	public List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntity(String reportType) {
		// TODO Auto-generated method stub
		return (List<DynamicFieldReportConfig>) list(
				"FROM DynamicFieldReportConfig dfrg WHERE dfrg.entityType=? ORDER BY dfrg.orderz ASC", reportType);
	}

	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByEntityType(String reportType) {
		// TODO Auto-generated method stub
		return list("FROM DynamicFeildMenuConfig dsc WHERE dsc.entity = ? ORDER BY dsc.order ASC", reportType);
	}

	@Override
	public List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntity(String reportType) {
		// TODO Auto-generated method stub
		return (List<DynamicFieldReportJoinMap>) list("FROM DynamicFieldReportJoinMap WHERE entityType=? ", reportType);
	}

	@Override
	public FarmerDynamicData findFarmerDynamicData(String txnType, String referenceId) {

		return (FarmerDynamicData) find(
				"FROM FarmerDynamicData fdfv  join fetch  fdfv.farmerDynamicFieldsValues  where fdfv.txnType=? and fdfv.referenceId=?",
				new Object[] { txnType, referenceId });
	}

	int iCount = 0;

	@Override
	public List<FarmerDynamicFieldsValue> processFormula(FarmerDynamicData farmerDynamicData,
			Map<String, List<FarmerDynamicFieldsValue>> fdMap, LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap) {

		List<FarmerDynamicFieldsValue> resultSet = new ArrayList<>();
		
		LinkedList<DynamicFieldConfig> fmap = fieldConfigMap.values().stream()
				.filter(p -> (Arrays.asList("0", "2").contains(p.getIsMobileAvail()) && p.getFormula() != null
						&& !StringUtil.isEmpty(p.getFormula())) || (p.getIsMobileAvail().equals("5"))).sorted(Comparator.comparing(DynamicFieldConfig::getfOrder))
				.collect(Collectors.toCollection(LinkedList::new));
		
          
		List<DynamicConstants> consts = listDynamicConstants();
		Map<String, String> cts = consts.stream().collect(Collectors.toMap(u -> u.getCode(), u -> u.getFieldName()));
		if (fmap != null && !ObjectUtil.isListEmpty(fmap)) {
			// fmap.sort((p1, p2) -> p1.getCode().compareTo(p2.getCode()));
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			fmap.stream().forEach(u -> {
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				if (u.getIsMobileAvail().equals("5")) {
					if (farmerDynamicData.getScoreValue() != null && !farmerDynamicData.getScoreValue().isEmpty()
							&& farmerDynamicData.getScoreValue().containsKey(u.getCode())) {
						Map<String, String> smMap = farmerDynamicData.getScoreValue().get(u.getCode());

						smMap.entrySet().forEach(score -> {
							if (score.getKey().contains("|")) {
								/*
								 * format of the score /*CT001|f1,f2,f3~-1~3-2.0
								 */
								if (farmerDynamicFieldsValue.getScore() == null) {
									String[] cond = score.getKey().split("\\|");
									String catCode = cond[0].toString();
									String[] fields = cond[1].toString().split("~")[0].split(",");
									String noOf = cond[1].toString().split("~")[1].toString();
									String scPer = cond[1].toString().split("~")[2].toString();
									List<Integer> ii = new ArrayList<>();

									Arrays.asList(fields).stream().forEach(field -> {
										if (field.contains("$")) {
											iCount = 0;
											Arrays.asList(field.split("\\$")).stream().forEach(ff -> {
												if (fdMap.containsKey(ff.trim())) {
													if (fdMap.get(ff.trim()).get(0).getScore() != null
															&& fdMap.get(ff.trim()).get(0).getScore() > iCount) {
														iCount = fdMap.get(ff.trim()).get(0).getScore();
													}
												}
											});
											ii.add(iCount);
										} else {
											if (fdMap.containsKey(field.trim())) {

												if (fdMap.get(field.trim()).get(0).getScore() != null) {
													ii.add(fdMap.get(field.trim()).get(0).getScore());
												}

											} else {
												ii.add(0);
											}
										}
									});

									if (noOf != null && noOf.equals("-1")) {
										if (ii.stream().filter(per -> per == 3).count() == fields.length) {
											Integer sc = Integer.valueOf(scPer.split("-")[0].toString());
											Double per = Double.valueOf(scPer.split("-")[1].toString());
											farmerDynamicFieldsValue.setScore(sc);
											farmerDynamicFieldsValue.setPercentage(per);
											farmerDynamicFieldsValue.setFieldValue(catCode);
										}

									} else if (noOf != null && ii.stream()
											.filter(per -> Arrays.asList(noOf.split(",")).contains(String.valueOf(per)))
											.count() == fields.length) {
										Integer sc = Integer.valueOf(scPer.split("-")[0].toString());
										Double per = Double.valueOf(scPer.split("-")[1].toString());
										farmerDynamicFieldsValue.setScore(sc);
										farmerDynamicFieldsValue.setPercentage(per);
										farmerDynamicFieldsValue.setFieldValue(catCode);

									}

								}
							}

						});
						farmerDynamicData.setTotalScore(
								farmerDynamicData.getTotalScore() + (farmerDynamicFieldsValue.getPercentage() == null
										? 0 : farmerDynamicFieldsValue.getPercentage()));
					}
				} else {
					String ans = "";
					List<String> fieldLiust = new ArrayList<>();
					formula = u.getFormula();
					Pattern p = Pattern.compile("\\{([^}]*)\\}");
					Matcher m = p.matcher(formula);
					while (m.find())
						fieldLiust.add(m.group(1));

					fieldLiust.stream().filter(ff -> fieldConfigMap.containsKey(ff)).forEach(g -> {
						if (fieldConfigMap.get(g).getValidation() != null
								&& fieldConfigMap.get(g).getValidation().equals("4")) {
							Double String = fdMap.get(g) != null ? fdMap.get(g).stream()
									.map(e -> Double.valueOf(e.getFieldValue())).reduce(0.00, (a, b) -> a + b) : 0.0;
							formula = formula.replaceAll("\\{" + g + "\\}",
									fdMap.containsKey(g) ? String.toString() : "0");
						} else if (fieldConfigMap.get(g).getValidation() != null
								&& fieldConfigMap.get(g).getValidation().equals("2")) {
							Integer String = fdMap.get(g) != null ? fdMap.get(g).stream()
									.map(e -> Integer.valueOf(e.getFieldValue())).reduce(0, (a, b) -> a + b) : 0;
							formula = formula.replaceAll("\\{" + g + "\\}",
									fdMap.containsKey(g) ? String.toString() : "0");
						} else {
							formula = formula.replaceAll("\\{" + g + "\\}", fdMap.containsKey(g) ?'"'+fdMap.get(g).get(0).getFieldValue()+'"' : "0");
						}

					});
					cts.entrySet().forEach(consta -> {
						if (formula.contains(consta.getKey())) {
							Pattern cp = Pattern.compile("\\##([^##]*)\\##");
							Matcher cm = cp.matcher(formula);
							while (cm.find()) {

								String vlue = getFieldValueByContant(farmerDynamicData.getEntityId(),
										farmerDynamicData.getReferenceId(), cts.get(cm.group(1)));

								if (ObjectUtil.isEmpty(vlue) || StringUtil.isEmpty(vlue)) {
									vlue = "0";
								}

								formula = formula.replaceAll(DynamicFieldConfig.FORMULA_CONSTANT_VALUE + consta.getKey()
										+ DynamicFieldConfig.FORMULA_CONSTANT_VALUE, vlue);

							}
						}
					});
					try {
						ans = String.valueOf(engine.eval(formula));
						if (ans.equals("NaN") || ans.contains("Infinity")) {
							ans = "0";
						} 
					} catch (ScriptException e) {
						ans = "0";
					}
					farmerDynamicFieldsValue.setFieldValue(ans);
				}
				farmerDynamicFieldsValue.setFieldName(u.getCode());

				farmerDynamicFieldsValue.setComponentType(u.getComponentType());
				farmerDynamicFieldsValue.setTxnType(farmerDynamicData.getTxnType());
				farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
				farmerDynamicFieldsValue.setCreatedDate(farmerDynamicData.getCreatedDate());
				farmerDynamicFieldsValue.setCreatedUser(farmerDynamicData.getCreatedUser());
				farmerDynamicFieldsValue.setTxnUniqueId(farmerDynamicData.getTxnUniqueId());
				farmerDynamicFieldsValue.setIsMobileAvail(u.getIsMobileAvail());
				farmerDynamicFieldsValue.setValidationType(u.getValidation());
				farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
				farmerDynamicFieldsValue
						.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");

				farmerDynamicFieldsValue
						.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);

				farmerDynamicFieldsValue.setListMethod(
						fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
								.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");
				farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
				fdMap.put(farmerDynamicFieldsValue.getFieldName(), new ArrayList<FarmerDynamicFieldsValue>() {
					{
						add(farmerDynamicFieldsValue);

					}
				});
				resultSet.add(farmerDynamicFieldsValue);

			});
		}
		return resultSet;

	}

	@Override
	public void deleteChildObjects(String txnType) {
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(
				"delete from farmer_dynamic_field_value where FARMER_DYNAMIC_DATA_ID IS NULL and txn_type='" + txnType
						+ "';");
		query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public FarmerDynamicData findFarmerDynamicDataBySeason(String txnType, String id, String season) {
		return (FarmerDynamicData) find(
				"FROM FarmerDynamicData fdfv join fetch  fdfv.farmerDynamicFieldsValues where fdfv.txnType=? and fdfv.referenceId=? and fdfv.season=?",
				new Object[] { txnType, id, season });
	}

	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByMType(String txnType) {
		// return list("FROM DynamicFeildMenuConfig dsc WHERE dsc.mTxnType = ?
		// ORDER BY dsc.order ASC", txnType);
		return list(
				"select distinct dsc FROM DynamicFeildMenuConfig dsc join fetch dsc.dynamicSectionConfigs ds  join fetch dsc.dynamicFieldConfigs  dc join fetch dc.field ff join fetch ff.dynamicSectionConfig sec join fetch dc.field left join fetch dc.dynamicFieldScoreMap WHERE dsc.mTxnType = ? ORDER BY dsc.order ASC",
				txnType);
	}

	@Override
	public void addWeatherForeCast(WeatherForeCast foreCast) {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(foreCast);
		sessions.flush();
		sessions.close();
	}

	@Override
	public List<Object[]> findLatAndLongByFarm() {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = sessions
				.createQuery("SELECT fa.id,fa.latitude,fa.longitude FROM Farm fa where fa.latitude is not null AND "
						+ "fa.longitude is not null AND fa.latitude!='' AND fa.longitude!='' AND fa.latitude!=0 AND fa.longitude!=0 GROUP BY fa.latitude,fa.longitude");
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public String findLatAndLong(String lat, String lon) {
		// TODO Auto-generated method stub
		String result = null;
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = sessions.createQuery(
				"SELECT wfc.latitude FROM WeatherForeCast wfc where wfc.latitude=:lat AND " + "wfc.longitude=:lon");
		query.setParameter("lat", lat);
		query.setParameter("lon", lon);
		if (!ObjectUtil.isListEmpty(query.list())) {
			result = (String) query.list().get(0);
		}
		sessions.flush();
		sessions.close();
		return result;
	}

	@Override
	public List<WeatherForeCast> findForecastByCity(String selectedCity, List<String> daysList) {
		// TODO Auto-generated method stub
		/*
		 * return
		 * list("FROM WeatherForeCast wfc INNER JOIN  wfc.weatherForeCastDetails wfcd where wfc.city=? GROUP BY wfc.city"
		 * , selectedCity);
		 */ Object dasy[] = { "M", "A" };
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("SELECT wfc from WeatherForeCast wfc INNER JOIN wfc.weatherForeCastDetails"
				+ " wfcd WHERE wfc.city=:city AND wfcd.daysOfSegment in:daysSeq GROUP BY wfc.city");
		query.setParameter("city", selectedCity);
		query.setParameterList("daysSeq", dasy);
		List list = query.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public void removeForecastData() {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query queryMast = sessions.createQuery("DELETE FROM WeatherForeCast");
		Query querySub = sessions.createQuery("DELETE FROM WeatherForeCastDetails");
		queryMast.executeUpdate();
		querySub.executeUpdate();
		sessions.flush();
		sessions.close();
	}

	@Override
	public String findFarmIcsTypByFarmId(Long farmId) {
		// TODO Auto-generated method stub
		String icsType = null;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("SELECT fic.icsType FROM FarmIcsConversion fic where fic.farm.id=:farmId");
		query.setParameter("farmId", farmId);
		if (!ObjectUtil.isListEmpty(query.list())) {
			icsType = (String) query.list().get(0);
		}
		return icsType;
	}

	@Override
	public String findCropNameByCropId(Long farmId) {
		// TODO Auto-generated method stub
		String cropName = null;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT fc.procurementVariety.procurementProduct.name FROM FarmCrops fc where fc.farm.id=:farmId");
		query.setParameter("farmId", farmId);
		if (!ObjectUtil.isListEmpty(query.list())) {
			cropName = (String) query.list().get(0);
		}
		return cropName;
	}

	@Override
	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithiWithGramp(long id,
			String currentSeasonCode, Long revisionNo, List<String> branch) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select f.id,COALESCE(f.farmerId,''), COALESCE(f.farmerCode,''),COALESCE(f.firstName,''),COALESCE(f.lastName,''),COALESCE(f.village.code,''),COALESCE(f.samithi.code,''),COALESCE(f.isCertifiedFarmer,''),COALESCE(f.certificationType,''),COALESCE(f.farmersCodeTracenet,''),COALESCE(f.fpo,''),COALESCE(f.utzStatus,''),f.revisionNo,f.farms.size,COALESCE(f.traceId,''),COALESCE(f.isDisable,''),COALESCE(f.typez,''),f.seasonCode,f.status,f.icsName,f.proofNo,f.mobileNumber,COALESCE(gm.code,''),COALESCE(f.city.code,''),COALESCE(f.city.locality.code,''),COALESCE(f.city.locality.state.code,''),COALESCE(f.surName,''), (case when LENGTH(ipho.image) <= 0 or ipho.image=null then '0' else '1' END),(case when LENGTH(f.idProofImg) >  0 then '1' else '0' END),f.certificateStandardLevel,f.maritalSatus,f.phoneNumber,f.casteName,f.masterData,f.address FROM Farmer f LEFT JOIN f.village v LEFT JOIN v.gramPanchayat gm left join f.imageInfo im left join im.photo ipho WHERE f.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  f.revisionNo > :revisionNo  AND f.statusCode in  (:status1)  order by f.revisionNo DESC");

		query.setParameter("id", id);
		query.setParameter("revisionNo", revisionNo);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List result = query.list();

		return result;

	}

	@Override
	public Object[] findFarmerInfoById(Long id) {
		// TODO Auto-generated method stub
		return (Object[]) find("select fr.farmerId,fr.firstName,fr.lastName FROM Farmer fr WHERE fr.id = ?", id);
	}

	@Override
	public Object[] findFarmInfoById(Long id) {
		return (Object[]) find("select fr.farmCode,fr.farmName FROM Farm fr WHERE fr.id = ?", id);
	}

	public List<DynamicFieldConfig> getLitsComponentsBySectionCodeAndListId(String sectionCode, String id) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"FROM DynamicFieldConfig dfc WHERE dfc.dynamicSectionConfig.sectionCode = :sectionCode AND dfc.referenceId = :refid order by dfc.orderSet ");
		query.setParameter("sectionCode", sectionCode);
		query.setParameter("refid", Long.parseLong(id));
		List<DynamicFieldConfig> listComponent = query.list();
		session.flush();
		session.close();
		return listComponent;
	}

	public void updateListComponentsOrder(Long id) {
		int maxOrder;
		Session session = getSessionFactory().openSession();

		Query query1 = session.createQuery("select max(df.orderSet) from DynamicFieldConfig df");
		if (query1.uniqueResult() != null) {
			maxOrder = (int) query1.uniqueResult();
		} else {
			maxOrder = 0;
		}

		Query query = session.createQuery("UPDATE DynamicFieldConfig dfc SET  dfc.orderSet=:order WHERE dfc.id = :id");
		query.setParameter("id", id);
		query.setParameter("order", maxOrder + 1);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	public void updateOrderForSubMenus(Long id, int order) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE Menu m SET  m.order=:menu_order WHERE m.id = :id");
		query.setParameter("id", id);
		query.setParameter("menu_order", order);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	public List<Object[]> isAlreadyAvailableMenuByParentId(Long parentMenuId, String menuName) {
		Object[] values = { parentMenuId, menuName };
		return list("SELECT m.id,m.label FROM Menu m where m.parentId = ? and m.label = ?", values);
	}

	@Override
	public List<Object[]> listFarmListByFarmerId(Long farmerId) {
		// TODO Auto-generated method stub
		return list(
				"SELECT f.farmName,f.farmDetailedInfo.totalLandHolding FROM Farm f where f.farmer.id = ? AND f.status<>2",
				farmerId);
	}

	public boolean isPlottingExist(Long id) {
		Long count = (Long) find("SELECT count(*) from Coordinates c WHERE c.farm.farmer.id=?", id);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Object[]> listFarmerAddressByFarmerId(String farmerId) {

		return list("SELECT fr.address,fr.city.name,fr.village.name FROM Farmer fr WHERE fr.farmerId = ?", farmerId);
	}

	public List<DynamicFieldConfig> getFieldsListForFormula() {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM DynamicFieldConfig dfc WHERE dfc.componentType in ( :type)");
		query.setParameterList("type", new Object[] { String.valueOf(1), String.valueOf(7) });
		List<DynamicFieldConfig> listComponent = query.list();
		session.flush();
		session.close();
		return listComponent;
	}

	public ForecastAdvisoryDetails findforeCastAdvisoryDetailsById(Long id) {

		return (ForecastAdvisoryDetails) find("From ForecastAdvisoryDetails fd where fd.id=?", id);
	}

	public ForecastAdvisory findforeCastAdvisoryById(Long id) {
		return (ForecastAdvisory) find("From ForecastAdvisory f where f.id=?", id);
	}

	@Override
	public List<Object[]> findAdvisoryList() {
		// TODO Auto-generated method stub
		return list(
				"SELECT fca.procurementProduct.id,fcad.minimumTemp,fcad.maximumTemp,fcad.minimumWind,fcad.maximumWind,fcad.minimumHumi,fcad.maximumHumi,"
						+ "fca.description,fcad.minimumRain,fcad.maximumRain FROM  ForecastAdvisory fca INNER JOIN fca.forecastAdvisoryDetails fcad");
	}

	@Override
	public List<Object[]> findForeCastList(Long cropId) {
		// TODO Auto-generated method stub
		return list(
				"SELECT fc.farmId,fcd.lowTemp,fcd.highTemp,fcd.windSpeed,fcd.humidity,fcd.rainFall,fc.id FROM Forecast fc INNER JOIN fc.forecastDetails fcd where fc.farmCropId=?",
				cropId);
	}

	@Override
	public void addForeCast(Forecast foreCast) {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(foreCast);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void removeForecast() {
		// TODO Auto-generated method stub
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query queryMast = sessions.createQuery("DELETE FROM Forecast");
		Query querySub = sessions.createQuery("DELETE FROM ForecastDetails");
		queryMast.executeUpdate();
		querySub.executeUpdate();
		sessions.flush();
		sessions.close();
	}

	public void updateDependencyKeyForDependentFieldsOfFormula(String dependentFieldCode, String formula_label_code) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"UPDATE DynamicFieldConfig dfc SET dfc.dependencyKey=:formula_label_code WHERE dfc.code=:code");
		query.setParameter("formula_label_code", formula_label_code);
		query.setParameter("code", dependentFieldCode);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	public List<DynamicFieldConfig> availableFieldsForList() {
		return list(
				"From DynamicFieldConfig dfc where dfc.componentType != 8 AND dfc.componentType != 10 AND dfc.referenceId = null ");
	}

	public void updateFieldsReferenceId(Long listId, List<Long> fieldsIdList) {
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("UPDATE DynamicFieldConfig dfc SET dfc.referenceId = :listId WHERE dfc.id in(:fieldsId)");
		query.setParameter("listId", listId);
		query.setParameterList("fieldsId", fieldsIdList);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	public void updateListButtonOrder(Long referenceId) {
		int maxOrder;
		Session session = getSessionFactory().openSession();

		Query query2 = session.createQuery("select max(df.orderSet) from DynamicFieldConfig df");
		if (query2.uniqueResult() != null) {
			maxOrder = (int) query2.uniqueResult();
		} else {
			maxOrder = 0;
		}

		Query query = session.createQuery(
				"UPDATE DynamicFieldConfig dfc SET dfc.orderSet = :order WHERE dfc.referenceId = :refId AND dfc.orderSet = NULL");
		query.setParameter("refId", referenceId);
		query.setParameter("order", maxOrder + 1);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<Object[]> listFarmFieldsByFarmerId(long id) {

		return (List<Object[]>) list(
				"select fm.id,fm.farmCode,fm.farmName,fm.farmId,fm.latitude,fm.longitude FROM Farm fm Where fm.farmer.id = ? and fm.status=1",
				id);
	}

	@Override
	public List<Object[]> listCropNamesWithFarm() {
		String cropName = null;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery("SELECT fc.id,fc.procurementVariety.procurementProduct.name FROM FarmCrops fc ");
		return query.list();
	}

	@Override
	public String findTotalAcreAndEstYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedSeason) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();

		String hqlQuery = "SELECT SUM(fd.totalLandHolding) FROM Farm fa INNER JOIN fa.farmDetailedInfo fd INNER JOIN fa.farmer f where fa.status=1";

		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}

		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}

		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}

		/*
		 * if (!StringUtil.isEmpty(selectedCrop) && selectedCrop != null) {
		 * hqlQuery +=
		 * " AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
		 * params.put("selectedCrop", Long.valueOf(selectedCrop)); }
		 */

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		String val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = ((String) obj);
		}
		return val;

	}

	public List<Farmer> findExistingFarmerByFarmerId(String farmerId) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery("SELECT fr.id FROM Farmer fr WHERE  fr.farmerId = :id AND fr.statusCode = :status");
		query.setParameter("id", farmerId);
		query.setParameter("status", ESETxnStatus.SUCCESS.ordinal());
		List<Farmer> listComponent = query.list();
		return listComponent;
	}

	public void updateExistingFarmerFlagById(List<Farmer> existing_farmer) {
		int flagValue = 0;
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE Farmer f SET f.existingFarmerFlag = :flag WHERE f.id in(:idList)");
		query.setParameter("flag", flagValue);
		query.setParameterList("idList", existing_farmer);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public Farmer findFarmerByID(Long id, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query q = sessions.createQuery("FROM Farmer f WHERE f.id=:farmerId");
		q.setParameter("farmerId", id);
		Farmer farmer = (Farmer) q.uniqueResult();
		sessions.flush();
		sessions.close();
		return farmer;
	}

	@Override
	public List<Object[]> findFarmerTraceDetailsByFarmCode(String farmCode, String tenantId) {
		// TODO Auto-generated method stub

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = sessions.createQuery(
				"SELECT fm.farmName,fm.farmer.id,fm.farmer.firstName,fm.farmer.village.name,fm.farmer.village.city.name,"
						+ "fm.farmer.village.city.locality.name,fm.farmer.village.city.locality.state.name,fm.farmer.village.city.locality.state.country.name,"
						+ "fm.farmDetailedInfo.surveyNumber,fm.latitude,fm.longitude,pp.name,DATE(fc.sowingDate),fm.farmer.isCertifiedFarmer,"
						+ "fm.farmer.farmerId,fic.organicStatus,fm.farmer.lastName,fm.id,fm.farmer.farmerCode FROM Farm fm LEFT JOIN fm.farmCrops fc LEFT JOIN fc.procurementVariety.procurementProduct pp LEFT JOIN fm.farmICSConversion fic where fm.farmCode =:farmCode");
		query.setParameter("farmCode", farmCode);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object[]> listDistributionBalanceDownload(long id, String strevisionNo) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select f.farmerId,db.product.subcategory.code,db.product.code,db.stock,db.revisionNo,f.proofNo from DistributionBalance db inner join db.farmer f WHERE f.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  db.revisionNo > :revisionNo   AND f.statusCode in  (:status1)  order by db.revisionNo DESC");
		query.setParameter("id", id);
		query.setParameter("revisionNo", Long.valueOf(strevisionNo));
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		List<Object[]> result = query.list();

		return result;

	}

	@Override
	public DistributionBalance findDistributionBalanceItem(Long id, String procurementProduct) {
		// TODO Auto-generated method stub
		Object[] values = { id, procurementProduct };
		return (DistributionBalance) find(
				"FROM DistributionBalance d WHERE d.farmer.id=? AND d.procurementProduct.name=?", values);
	}

	@Override
	public void updateDistributionBalance(DistributionBalance db) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("UPDATE DistributionBalance db SET db.stock=:COUNT WHERE db.id=:ID");
		query.setParameter("COUNT", db.getStock());
		query.setParameter("ID", db.getId());
		int result = query.executeUpdate();

		session.flush();
		session.close();
	}

	@Override
	public List<DistributionBalance> listDistributionBalanceList(Long id) {
		// TODO Auto-generated method stub
		return list("FROM DistributionBalance db WHERE db.farmer.id=?", id);
	}

	@Override
	public DistributionBalance findDistributionBalanceById(long templateId) {
		// TODO Auto-generated method stub
		DistributionBalance distributionBalance = (DistributionBalance) find(
				"FROM DistributionBalance db WHERE db.id = ?", templateId);
		return distributionBalance;
	}

	@Override
	public List<FarmerDynamicData> listFarmerDynamicDataByTxnId(String txnId) {
		return list("FROM FarmerDynamicData fd where fd.txnType=?", txnId);
	}

	@Override
	public void updateFarmIcsConversionByFarmId(long farmId) {
		Session session = getSessionFactory().openSession();

		Query query = session
				.createQuery("UPDATE FarmIcsConversion fic SET  fic.isActive=:val WHERE fic.farm.id = :farmId");
		query.setParameter("farmId", farmId);
		query.setParameter("val", 0);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmIdWithActive(Long farmId) {
		return (FarmIcsConversion) find("From FarmIcsConversion fc where fc.farm.id=? AND fc.isActive='1' ", farmId);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(
			long selectedFarm, String insType, String scope, int inspectionDateYear) {
		Object[] values = { selectedFarm, insType, scope, inspectionDateYear };
		return (FarmIcsConversion) find(
				"FROM FarmIcsConversion fc WHERE fc.farm.id=? AND fc.insType=? AND fc.scope=? AND Year(fc.inspectionDate)=?",
				values);
	}

	@Override
	public void updateFarmICSStatusByFarmIdInsTypeAndIcsType(Long farmId, String insType, String icsType) {
		Session session = getSessionFactory().openSession();

		Query query = session.createQuery(
				"UPDATE FarmIcsConversion fic SET  fic.insType=:insType,fic.icsType=:icsType  WHERE fic.farm.id = :farmId");
		query.setParameter("farmId", farmId);
		query.setParameter("insType", insType);
		query.setParameter("icsType", icsType);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public FarmIcsConversion findFarmIcsConversionById(Long id) {
		return (FarmIcsConversion) find("From FarmIcsConversion fc where fc.id=?", id);
	}

	public List<Object[]> listCultivationByFarmerIncome(String branchId) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT cp.FARMER_NAME,v.name,v.id,cp.FARMER_ID FROM cultivation cp  join village v on v.id = cp.VILLAGE_ID where cp.TXN_TYPE='1'";
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		sessions.flush();
		sessions.close();
		return result;

	}

	@Override
	public List<Farmer> listFarmerByFarmerIdByIdList(List<String> id) {

		Session sessions = getSessionFactory().openSession();
		String queryString = "FROM Farmer f WHERE f.farmerId IN (:idList) AND f.statusCode = :status";
		Query query = sessions.createQuery(queryString);
		query.setParameterList("idList", id);
		query.setParameter("status", ESETxnStatus.SUCCESS.ordinal());
		List<Farmer> result = query.list();
		sessions.flush();
		sessions.close();
		return result;
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmIcsConvId(Long id) {
		// TODO Auto-generated method stub
		return (FarmIcsConversion) find("From FarmIcsConversion fc where fc.id=? ", id);
	}

	@Override
	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT  SUM(fc.cultiArea),sum(fc.actualSeedYield),sum(fc.estimatedYield) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fc.farm.farmer f  WHERE  fc.status=1 and f.status=1";

		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}
		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}
		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id=:selectedFarmer";
			params.put("selectedFarmer", Long.valueOf(selectedFarmer));

		}

		if (!StringUtil.isEmpty(selectedCrop) && selectedCrop != null) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));

		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);

		}
		if (!selectedOrganicStatus.equalsIgnoreCase("conventional")) {
			if (!StringUtil.isEmpty(selectedCrop)) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0,1,2)");
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				}

			} else {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					hqlQuery += " AND f.id in (:fList)";

					params.put("fList", icsMap);
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0 ,1 ,2) ");
					hqlQuery += " AND f.id in (:fList)";

					params.put("fList", icsMap);
				} else {
					hqlQuery += " AND f.isCertifiedFarmer in (1,0)";
				}
			}

		} else {
			hqlQuery += " AND f.isCertifiedFarmer=0";
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			if (str.equalsIgnoreCase("fList")) {
				List<Long> result = (List<Long>) params.get(str);
				query.setParameterList(str, result);

			} else {
				query.setParameter(str, params.get(str));
			}
		}

		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {

			val = (Object[]) obj;
		}
		return val;

	}

	@Override
	public void insertDistBalance(String stringCellValue, String string, Double valueOf) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery("insert into distribution_balance values (null,'" + stringCellValue + "','"
				+ string + "','" + valueOf + "','20180221124344');");

		int result = query.executeUpdate();

		session.flush();
		session.close();

	}

	@Override
	public Object[] findFarmerCountByStateName(String stateName) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT count(f.id),count(fa.id),sum(fc.postHarvestProd) FROM Farmer f INNER JOIN f.farms fa "
						+ "INNER JOIN fa.farmCrops fc INNER JOIN f.city c INNER JOIN c.locality l INNER JOIN l.state s  WHERE s.name LIKE '%"
						+ stateName + "%'");
		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = (Object[]) obj;
		}
		return val;

	}

	public List<Object[]> findCocDatasByMonth(String sDate, String eDate, String branch) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT year(cul.expenseDate),month(cul.expenseDate),"
				+ "sum(cul.landTotal),sum(cul.totalWeed),sum(cul.totalIrrigation),"
				+ "sum(cul.totalFertilizer),sum(cul.totalPesticide),sum(cul.totalExpense),"
				+ "sum(cul.totalManure),sum(cul.totalSowing),sum(cul.labourCost),sum(cul.totalCulture)"
				+ " FROM Cultivation cul where cul.id is not null";
		if (!StringUtil.isEmpty(branch)) {
			hqlQuery += " AND cul.branchId =:branch ";
			params.put("branch", branch);
		}

		/*
		 * if (!StringUtil.isEmpty(sDate) && !StringUtil.isEmpty(eDate)) {
		 * hqlQuery +=
		 * " AND cul.expenseDate BETWEEN :startDate AND :endDate cul.expenseDate=:startDate"
		 * ;
		 * 
		 * params.put("startDate", sDate); params.put("endDate", eDate); }
		 */

		if (!StringUtil.isEmpty(sDate) && !StringUtil.isEmpty(eDate)) {
			hqlQuery += " AND YEAR(cul.expenseDate)=:startDate OR YEAR(cul.expenseDate)=:endDate";

			params.put("startDate", Integer.valueOf(sDate));
			params.put("endDate", Integer.valueOf(eDate));
		}

		hqlQuery += " GROUP BY MONTH(cul.expenseDate)";
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}

	public List<Object[]> populateStateAndFarmerCountList(String selectedBranch) {
		String queryString = null;
		if (!StringUtil.isEmpty(selectedBranch)) {
			queryString = "select count(f.id),s.name,s.code from state s inner JOIN location_detail ld on ld.STATE_ID = s.id"
					+ " inner JOIN city c on ld.id = c.LOCATION_ID inner JOIN farmer f on f.CITY_ID = c.ID where s.branch_id='"
					+ selectedBranch + "' group by s.name";

		} else {

			queryString = "select count(f.id),s.name,s.code from state s inner JOIN location_detail ld on ld.STATE_ID = s.id inner JOIN city c on ld.id = c.LOCATION_ID inner JOIN farmer f on f.CITY_ID = c.ID group by s.name";

		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> populateDistrictAndFarmerCountList(String selectedBranch) {
		String queryString = null;
		if (!StringUtil.isEmpty(selectedBranch)) {
			queryString = "select count(f.id),ld.name,ld.code as districtCode,s.code as stateCode from location_detail ld "
					+ " inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id "
					+ "inner join farmer f on f.CITY_ID = c.id where s.branch_id='" + selectedBranch
					+ "'    group by ld.name";
		} else {
			queryString = "select count(f.id),ld.name,ld.code as districtCode,s.code as stateCode from location_detail ld "
					+ " inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id  inner join farmer f on f.CITY_ID = c.id   group by ld.name";

		}
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> populateDistrictAndactiveFarmers(String selectedBranch) {
		String queryString = null;
		if (!StringUtil.isEmpty(selectedBranch)) {
			queryString = "select count(f.id),ld.name,ld.code as districtCode,s.code as stateCode from location_detail ld  inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id  inner join farmer f on f.CITY_ID = c.id "
					+ " where f.`STATUS` = 1 AND s.branch_id='" + selectedBranch + "'  group by ld.name";
		} else {
			queryString = "select count(f.id),ld.name,ld.code as districtCode,s.code as stateCode from location_detail ld  inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id  inner join farmer f on f.CITY_ID = c.id  where f.`STATUS` = 1  group by ld.name";
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> getCertifiedFarmerCount() {
		String queryString = "select count(f.id),ld.name,ld.code as districtCode,s.code as stateCode from location_detail ld  inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id  inner join farmer f on f.CITY_ID = c.id  where f.IS_FARMER_CERTIFIED = 1  group by ld.name";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> getNonCertifiedFarmerCount() {
		String queryString = "select count(f.id),ld.name,ld.code as districtCode,s.code as stateCode from location_detail ld  inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id  inner join farmer f on f.CITY_ID = c.id  where f.IS_FARMER_CERTIFIED = 0  group by ld.name";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> getFarmDetailsAndProposedPlantingArea() {
		String queryString = "select count(fam.id) as FarmCount,sum(fdi.PROPOSED_PLANTING_AREA) as Area,ld.name as District,s.name as State,ld.code as districtCode,s.code as stateCode from location_detail ld  inner join state s on s.id = ld.STATE_ID inner join city c  on c.LOCATION_ID = ld.id  inner join farmer f on f.CITY_ID = c.id inner join farm fam on fam.FARMER_ID = f.ID inner join farm_detailed_info fdi on fdi.ID = fam.FARM_DETAILED_INFO_ID group by ld.name";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public BigInteger[] getPramidChartDetails() {
		BigInteger result[] = new BigInteger[7];

		Session session = getSessionFactory().openSession();

		String sqlQuery1 = "select count(f.id) from farmer f";
		SQLQuery query1 = session.createSQLQuery(sqlQuery1);
		BigInteger totalFarmers = (BigInteger) query1.uniqueResult();

		String sqlQuery2 = "select count(f.id) from farmer f where f.`STATUS` = 1";
		SQLQuery query2 = session.createSQLQuery(sqlQuery2);
		BigInteger activeFarmers = (BigInteger) query2.uniqueResult();

		String sqlQuery3 = "select count(f.id) from farmer f where f.`STATUS` = 0";
		SQLQuery query3 = session.createSQLQuery(sqlQuery3);
		BigInteger inActiveFarmers = (BigInteger) query3.uniqueResult();

		String sqlQuery4 = "select count(f.id) from farmer f where f.GENDER = 'MALE'";
		SQLQuery query4 = session.createSQLQuery(sqlQuery4);
		BigInteger maleFarmers = (BigInteger) query4.uniqueResult();

		String sqlQuery5 = "select count(f.id) from farmer f where f.GENDER = 'FEMALE'";
		SQLQuery query5 = session.createSQLQuery(sqlQuery5);
		BigInteger femaleFarmers = (BigInteger) query5.uniqueResult();

		String sqlQuery6 = "select count(f.id) from farmer f where f.IS_FARMER_CERTIFIED = 1";
		SQLQuery query6 = session.createSQLQuery(sqlQuery6);
		BigInteger certifiedFarmers = (BigInteger) query6.uniqueResult();

		String sqlQuery7 = "select count(f.id) from farmer f where f.IS_FARMER_CERTIFIED = 0";
		SQLQuery query7 = session.createSQLQuery(sqlQuery7);
		BigInteger nonCertifiedFarmers = (BigInteger) query7.uniqueResult();

		result[0] = totalFarmers;
		result[1] = activeFarmers;
		result[2] = inActiveFarmers;
		result[3] = maleFarmers;
		result[4] = femaleFarmers;
		result[5] = certifiedFarmers;
		result[6] = nonCertifiedFarmers;

		session.flush();
		session.close();
		return result;

	}

	public List<DynamicFieldConfig> listDynamicFieldsBySectionCode(String selectedSection) {

		// TODO Auto-generated method stub
		List<DynamicFieldConfig> list = null;
		Session session = getSessionFactory().getCurrentSession();
		/*
		 * Query query = session.createQuery(
		 * "FROM DynamicFieldConfig df where df.componentType!='10' AND df.dynamicSectionConfig.sectionCode in :selectedSection"
		 * ); query.setParameterList("selectedSection", selectedSection);
		 */
		Query query = session.createQuery(
				"FROM DynamicFieldConfig df where  df.dynamicSectionConfig.sectionCode in (:selectedSection)");

		if (selectedSection.contains(",")) {
			query.setParameterList("selectedSection",
					Arrays.asList(selectedSection.split(",")).stream().map(x -> x.trim()).collect(Collectors.toList()));

		} else {
			query.setParameterList("selectedSection", new ArrayList<String>() {
				{
					add(selectedSection);
				}
			});
		}

		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			list = query.list();
		}
		return list;
	}

	public void addNewComponentIntoList(Long component_id, Long list_id) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"UPDATE DynamicFieldConfig dfc SET  dfc.referenceId=:list_id WHERE dfc.id = :component_id");
		query.setParameter("component_id", component_id);
		query.setParameter("list_id", list_id);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo) {
		// TODO Auto-generated method stub
		return list(
				"select distinct dsc FROM DynamicFeildMenuConfig dsc  join fetch dsc.dynamicSectionConfigs dsm  join fetch dsc.dynamicFieldConfigs dfm  join fetch dsm.section ss  join fetch dfm.field ff  left join fetch ff.languagePreferences left join fetch ss.languagePreferences left join fetch dsc.languagePreferences left join fetch ff.dynamicSectionConfig ffs left join fetch dfm.dynamicFieldScoreMap  where dsc.revisionNo > ? order by dsc.revisionNo desc ",
				Long.valueOf(revisionNo));
	}

	@Override
	public List<Object[]> findSeasonCodeList(String selectedBranch) {
		// TODO Auto-generated method stub
		if (!StringUtil.isEmpty(selectedBranch)) {
			return list("SELECT hs.code,hs.name FROM HarvestSeason hs where hs.branchId=? ORDER BY hs.name",
					selectedBranch);
		} else {
			return list("SELECT hs.code,hs.name FROM HarvestSeason hs ORDER BY hs.name");
		}
	}

	@Override
	public List<Object[]> findTotalWeightAmtNameByProcurement(String hqlQueryAppnd, Map<String, Object> params) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		List<Object[]> obj = null;
		String hqlQuery = "select pd.procurementProduct.name,sum(pd.NetWeight),sum(p.totalProVal),sum(p.paymentAmount) from ProcurementDetail pd inner join pd.procurement p inner join p.farmer f where p.farmer.id is not null";

		if (!StringUtil.isEmpty(hqlQueryAppnd) && !StringUtil.isEmpty(params)) {
			hqlQuery += hqlQueryAppnd;
		}

		hqlQuery += " group by pd.procurementProduct.name order by pd.id desc";

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (List<Object[]>) query.list();
		}
		return obj;
	}

	public List<Object[]> findProductsByWarehouse(String warehouse, String chartType) {
		if (!StringUtil.isEmpty(chartType)) {
			chartType = "and d." + chartType + " is not null "; // for display
																// either farmer
																// or agent
																// based
																// distribution
		} else {
			chartType = ""; // for display both farmer and agent based
							// distribution
		}

		Session session = getSessionFactory().openSession();
		String queryString1 = "select d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,p.NAME,dd.EXISTING_QUANTITY,dd.QUANTITY,dd.CURRENT_QUANTITY,d.TOTAL_AMOUNT,d.FINAL_AMOUNT,d.PAYMENT_AMT from distribution_detail dd inner join distribution d on d.ID = dd.DISTRIBUTION_ID inner join product p on p.ID = dd.PRODUCT_ID where d.SERVICE_POINT_ID = '"
				+ warehouse + "' " + chartType + " ";
		SQLQuery query1 = session.createSQLQuery(queryString1);
		List<Object[]> warehouseAndProductsDetails = query1.list();
		session.flush();
		session.close();
		return warehouseAndProductsDetails;
	}

	public List<Object[]> listDistributionWarehouse(String branch, String season, String chartType) {
		Session session = getSessionFactory().openSession();
		String queryString1;
		if (!StringUtil.isEmpty(chartType)) {
			chartType = "and d." + chartType + " is not null "; // for display
																// either farmer
																// or agent
																// based
																// distribution
		} else {
			chartType = ""; // for display both farmer and agent based
							// distribution
		}

		if (!StringUtil.isEmpty(branch) && StringUtil.isEmpty(season)) {
			queryString1 = "select d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,sum(dd.QUANTITY) from distribution_detail dd inner join distribution d on d.ID = dd.DISTRIBUTION_ID inner join product p on p.ID = dd.PRODUCT_ID where d.SERVICE_POINT_ID !='' and d.SERVICE_POINT_ID is not null "
					+ chartType + " and d.BRANCH_ID = '" + branch + "' GROUP BY d.SERVICE_POINT_ID";
		} else if (!StringUtil.isEmpty(season) && StringUtil.isEmpty(branch)) {
			queryString1 = "select d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,sum(dd.QUANTITY) from distribution_detail dd inner join distribution d on d.ID = dd.DISTRIBUTION_ID inner join product p on p.ID = dd.PRODUCT_ID where d.SERVICE_POINT_ID !='' and d.SERVICE_POINT_ID is not null  "
					+ chartType + " and d.SEASON_CODE = '" + season + "' GROUP BY d.SERVICE_POINT_ID";
		} else if (!StringUtil.isEmpty(season) && !StringUtil.isEmpty(branch)) {
			queryString1 = "select d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,sum(dd.QUANTITY) from distribution_detail dd inner join distribution d on d.ID = dd.DISTRIBUTION_ID inner join product p on p.ID = dd.PRODUCT_ID where d.SERVICE_POINT_ID !='' and d.SERVICE_POINT_ID is not null  "
					+ chartType + "  and d.SEASON_CODE = '" + season + "' and d.BRANCH_ID = '" + branch
					+ "' GROUP BY d.SERVICE_POINT_ID";
		} else {
			queryString1 = "select d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,sum(dd.QUANTITY) from distribution_detail dd inner join distribution d on d.ID = dd.DISTRIBUTION_ID inner join product p on p.ID = dd.PRODUCT_ID where d.SERVICE_POINT_ID !='' and d.SERVICE_POINT_ID is not null  "
					+ chartType + "  GROUP BY d.SERVICE_POINT_ID";
		}
		SQLQuery query1 = session.createSQLQuery(queryString1);
		List<Object[]> DistributionWarehouseDetails = query1.list();
		session.flush();
		session.close();
		return DistributionWarehouseDetails;
	}

	@Override
	public List<Object[]> findAmtAndQtyByProcurment(String selectedBranch, String selectedState, String selectedSeason,
			Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		Session session = getSessionFactory().getCurrentSession();
		String hqlQuery = "select pd.procurementProduct.name,sum(pd.NetWeight),sum(pd.procurement.totalProVal),sum(pd.procurement.paymentAmount) from ProcurementDetail pd "
				+ "inner join pd.procurement p inner join p.farmer f "
				+ "WHERE pd.procurementProduct.name !=null and p.farmer.status=1 and p.farmer.statusCode=0";

		if (!StringUtil.isEmpty(sDate) && eDate != null) {
			hqlQuery += " AND pd.procurement.agroTransaction.txnTime BETWEEN :startDate AND :endDate";
			params.put("startDate", sDate);
			params.put("endDate", eDate);
		}

		if (!StringUtil.isEmpty(selectedBranch)) {

			hqlQuery += " AND pd.procurement.branchId=:branchId";
			params.put("branchId", selectedBranch);
		}

		if (!StringUtil.isEmpty(selectedSeason)) {

			hqlQuery += " AND pd.procurement.seasonCode=:season";
			params.put("season", selectedSeason);
		}

		if (!StringUtil.isEmpty(selectedState)) {

			hqlQuery += " AND pd.procurement.village.city.locality.state.id=:state";
			params.put("state", Long.valueOf(selectedState));
		}
		hqlQuery += " GROUP BY pd.procurementProduct.name";
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		return query.list();
	}

	@Override
	public Object[] findSaleIncomeByFarmer(List<String> farmerIds, List<String> farmCodes) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT cu.farmId,cu.farmerId,sum(cu.agriIncome),sum(cu.interCropIncome),sum(cu.otherSourcesIncome),cu.currentSeasonCode FROM Cultivation cu WHERE cu.farmerId in:fId  and cu.farmId in:faCode");
		query.setParameterList("fId", farmerIds);
		query.setParameterList("faCode", farmCodes);

		Object[] result = null;
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			result = (Object[]) query.list().get(0);
		}

		return result;

	}

	@Override
	public List<Object[]> listOfFarmersByFarmCrops() {
		return list(
				"SELECT DISTINCT f.farmerId,f.firstName from FarmCrops fcs inner join fcs.farm fa inner join fa.farmer f where f.refId is null AND  f.statusCode='"
						+ ESETxnStatus.SUCCESS.ordinal() + "'");
	}

	@Override
	public Object[] findTotalAmtAndweightByProcurementWithDate(String hqlQueryAppnd, Map<String, Object> params,
			Date convertStringToDate, Date convertStringToDate2, String branch) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = new Object[3];
		String hqlQuery = "select distinct p from Procurement p inner join p.farmer f where p.farmer.id is not null";

		//
		if (!StringUtil.isEmpty(hqlQueryAppnd) && !StringUtil.isEmpty(params)) {
			hqlQuery += hqlQueryAppnd;

		}

		if (!StringUtil.isEmpty(convertStringToDate) && convertStringToDate2 != null) {
			hqlQuery += " AND p.createdDate BETWEEN :startDate AND :endDate";
			params.put("startDate", convertStringToDate);
			params.put("endDate", convertStringToDate2);

		}

		if (!StringUtil.isEmpty(branch)) {
			hqlQuery += " AND p.branchId =:branch";
			params.put("branch", branch);
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		List<Procurement> pr = query.list();
		if (!pr.isEmpty()) {
			List<ProcurementDetail> prDet = new ArrayList<>();
			pr.stream().map(u -> u.getProcurementDetails()).collect(Collectors.toList()).forEach(u -> {
				prDet.addAll(u);
			});
			obj[0] = prDet.stream().mapToDouble(i -> i.getNetWeight()).sum();
			obj[1] = pr.stream().mapToDouble(i -> i.getPaymentAmount()).sum();
			obj[2] = pr.stream().mapToDouble(i -> i.getTotalProVal()).sum();
		} else {
			obj[0] = 0.0;
			obj[1] = 0.0;
			obj[2] = 0.0;
		}
		return obj;
	}

	@Override
	public double findStockAdded(Long distId, Long warehouseId) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"select wp.stock from WarehouseProduct wp where  wp.product.id=:ID  AND wp.warehouse.id=:WarehouseId");
		query.setParameter("ID", distId);
		query.setParameter("WarehouseId", warehouseId);
		List list = query.list();
		Double value = (Double) list.get(0);

		session.flush();
		session.close();
		return value;
	}

	public List<Object[]> farmersByBranch() {
		Session session = getSessionFactory().openSession();
		String queryString = "select count(f.ID),bm.`NAME`,bm.BRANCH_ID from farmer f inner join branch_master bm on bm.BRANCH_ID = f.BRANCH_ID where f.STATUS_CODE = 0 and f.status=1 group by bm.BRANCH_ID ORDER BY COUNT(f.ID) DESC";
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> farmersByCountry(String selectedBranch) {
		
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),ctry.name,ctry.code,ctry.branchId from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1";
			if(!StringUtil.isEmpty(tenantId) && tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))
			hqlString += " AND f.status=1";
			hqlString += " group by ctry.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
			
			
		} else {
			String hqlString = "SELECT count(f.id),ctry.name,ctry.code,ctry.branchId from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.statusCode = 0 and f.status=1 ";
			if(!StringUtil.isEmpty(tenantId) && tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))
				hqlString += " AND f.status=1";
				hqlString += " group by ctry.code";
				hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmersByState(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),s.name,s.code,ctry.code,s.id from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1   group by s.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id),s.name,s.code,ctry.code,s.id from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where  f.statusCode = 0 and f.status=1 group by s.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmersByLocality(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),ld.name,ld.code,s.code from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s  where f.branchId = :branch and f.statusCode = 0 and f.status=1  group by ld.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id),ld.name,ld.code,s.code from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s where f.statusCode = 0 and f.status=1 group by ld.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmersByMunicipality(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),c.name,c.code,ld.code from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld  where f.branchId = :branch and f.statusCode = 0 and f.status=1  group by c.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id),c.name,c.code,ld.code from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld where f.statusCode = 0 and f.status=1 group by c.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmersByGramPanchayat(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),gp.name,gp.code,c.code from Farmer f inner join f.village v  inner join v.gramPanchayat gp inner join gp.city c  where f.branchId = :branch and f.statusCode = 0 and f.status=1  group by gp.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id),gp.name,gp.code,c.code from Farmer f inner join f.village v  inner join v.gramPanchayat gp inner join gp.city c where f.statusCode = 0 and f.status=1  group by gp.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmersByVillageWithGramPanchayat(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),v.name,v.code,gp.code from Farmer f inner join f.village v  inner join v.gramPanchayat gp where f.branchId = :branch and f.statusCode = 0 and f.status=1  group by v.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id),v.name,v.code,gp.code from Farmer f inner join f.village v  inner join v.gramPanchayat gp where f.statusCode = 0 and f.status=1 group by v.code";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmersByVillageWithOutGramPanchayat(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id) ,v.name,v.code,c.code from Farmer f inner join f.village v  inner join v.city c where f.branchId = :branch and f.statusCode = 0 and f.status=1  group by v.code ";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id) ,v.name,v.code,c.code from Farmer f inner join f.village v  inner join v.city c where f.statusCode = 0 and f.status=1 group by v.code ";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> farmerDetailsByVillage(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id) as totalFarmers, (select count(f.id) from Farmer f where f.status = 1 and f.village.id = v.id) as active,(select count(f.id) from Farmer f where f.status = 0 and f.village.id = v.id) as inActive,(select count(f.id) from Farmer f where f.isCertifiedFarmer = 1 and f.status = 1 and f.village.id = v.id) as certified,(select count(f.id) from Farmer f where f.isCertifiedFarmer = 0 and f.status = 1 and f.village.id = v.id) as nonCertified,(select v1.code from Village v1 where f.village.id = v1.id) as villageCode  from Farmer f inner join f.village v where f.branchId = :branch and f.statusCode = 0 and f.status=1  group by f.village.id";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id) as totalFarmers, (select count(f.id) from Farmer f where f.status = 1 and f.village.id = v.id) as active,(select count(f.id) from Farmer f where f.status = 0 and f.village.id = v.id) as inActive,(select count(f.id) from Farmer f where f.isCertifiedFarmer = 1 and f.status = 1 and f.village.id = v.id) as certified,(select count(f.id) from Farmer f where f.isCertifiedFarmer = 0 and f.status = 1 and f.village.id = v.id) as nonCertified,(select v1.code from Village v1 where f.village.id = v1.id) as villageCode  from Farmer f inner join f.village v where f.statusCode = 0 and f.status=1 group by f.village.id";
			hqlString += " ORDER BY COUNT(f.id) DESC";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> getFarmDetailsAndProposedPlantingArea(String locationLevel1, String selectedBranch,
			String gramPanchayatEnable) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		String locationLevel = String.valueOf(locationLevel1.charAt(0));
		String flag = "enable";
		if (!locationLevel.equals("B")) {
			Session session = getSessionFactory().getCurrentSession();
			Query query;
			String hqlString = null;
			// if (!StringUtil.isEmpty(selectedBranch)) {

			switch (locationLevel) {
			case "C":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,s.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and ctry.code = :locationCode GROUP BY s.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,s.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and ctry.code = :locationCode GROUP BY s.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "S":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,ld.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s where f.branchId = :branch and f.statusCode = 0 and f.status=1  and fa.status=1 and s.code = :locationCode GROUP BY ld.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,ld.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s where f.branchId = :branch and f.statusCode = 0 and f.status=1  and fa.status=1 and s.code = :locationCode GROUP BY ld.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "D":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,c.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and ld.code = :locationCode GROUP BY c.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,c.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.city c inner join c.locality ld where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and ld.code = :locationCode GROUP BY c.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "M":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,gp.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.gramPanchayat gp inner join gp.city c where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and c.code = :locationCode GROUP BY gp.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,v.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.city c where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and c.code = :locationCode GROUP BY v.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "G":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,v.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.gramPanchayat gp where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and gp.code = :locationCode GROUP BY v.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "V":
				hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,f.firstName from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v  where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 and v.code = :locationCode GROUP BY f.id ORDER BY TOTAL_LAND_HOLDING desc";
				break;
			default:
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,ctry.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1 GROUP BY ctry.name ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fdi.totalLandHolding) as TOTAL_LAND_HOLDING,ctry.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fa.status=1";
					if(!StringUtil.isEmpty(tenantId) && tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))
						hqlString += " AND f.status=1";
						hqlString += " GROUP BY ctry.name ORDER BY TOTAL_LAND_HOLDING desc";
					
				}
				flag = "disable";
				break;
			}

			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
			if (!locationLevel1.equalsIgnoreCase("first_level_Branch_Login") && flag.equalsIgnoreCase("enable")) {
				query.setParameter("locationCode", locationLevel1);
			}

			List<Object[]> result = query.list();
			return result;
		} else {
			Session session = getSessionFactory().openSession();
			String queryString = "select count(fa.id),sum(fdi.TOTAL_LAND_HOLDING) as TOTAL_LAND_HOLDING,bm.name,bm.BRANCH_ID from farm fa inner join farmer f on f.ID = fa.FARMER_ID inner join farm_detailed_info fdi on fdi.ID = fa.FARM_DETAILED_INFO_ID inner join branch_master bm on bm.BRANCH_ID = f.BRANCH_ID where f.STATUS_CODE = 0 and fa.status=1 and f.status=1 GROUP BY bm.`NAME` ORDER BY TOTAL_LAND_HOLDING desc";
			SQLQuery query = session.createSQLQuery(queryString);
			List<Object[]> result = query.list();
			session.flush();
			session.close();
			return result;
		}

	}

	public List<Object[]> populateFarmerLocationCropChart(String codeForCropChart) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		char code = 0;
		if (!StringUtil.isEmpty(codeForCropChart)) {
			code = codeForCropChart.charAt(0);
		}
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		Query query;

		switch (code) {
		case 'C':
			hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where ctry.code = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			break;
		case 'S':
			hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where s.code = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			break;
		case 'D':
			hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where ld.code = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			break;
		case 'M':
			hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where c.code = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			break;
		case 'G':
			hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where gp.code = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			break;
		case 'V':
			hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where v.code = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			break;
		default:
			if (!StringUtil.isEmpty(codeForCropChart)) {
				hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.branchId = :branchId and f.statusCode = 0 and fa.status=1 and f.status=1 group by pp.code ORDER BY CULTIVATION_AREA desc";
			} else {
				hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code  from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 and fa.status=1 and f.status=1";
				if(!StringUtil.isEmpty(tenantId) && tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))
					hqlString += " AND f.status=1";
					hqlString += " group by pp.code ORDER BY CULTIVATION_AREA desc";
			}
			break;
		}

		query = session.createQuery(hqlString);
		if (!StringUtil.isEmpty(codeForCropChart)) {
			query.setParameter("branchId", codeForCropChart);
		}

		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> estimatedAndActualYield(String locationCode) {
		char code = 0;
		String queryString;
		if (!StringUtil.isEmpty(locationCode)) {
			code = locationCode.charAt(0);
		}

		switch (code) {
		case 'C':
			queryString = "select pp.name,sum(fc.EST_YIELD) as estimated,sum(chd.QTY) as actual from farm fa inner join crop_harvest ch on ch.FARM_CODE = fa.FARM_CODE inner join crop_harvest_details chd on chd.CROP_HARVEST_ID = ch.ID inner join procurement_product pp on pp.ID = chd.CROP inner join farm_crops fc on fc.FARM_ID = fa.ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID inner join state s on s.ID = ld.STATE_ID inner join country ctry on ctry.ID = s.COUNTRY_ID where ctry.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 group by pp.code  ORDER BY  estimated,actual desc";
			break;
		case 'S':
			queryString = "select pp.name,sum(fc.EST_YIELD) as estimated,sum(chd.QTY) as actual from farm fa inner join crop_harvest ch on ch.FARM_CODE = fa.FARM_CODE inner join crop_harvest_details chd on chd.CROP_HARVEST_ID = ch.ID inner join procurement_product pp on pp.ID = chd.CROP inner join farm_crops fc on fc.FARM_ID = fa.ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID inner join state s on s.ID = ld.STATE_ID where s.`CODE` = '"
					+ locationCode + "' and  f.STATUS_CODE = 0 group by pp.code  ORDER BY  estimated,actual desc";
			break;
		case 'D':
			queryString = "select pp.name,sum(fc.EST_YIELD) as estimated,sum(chd.QTY) as actual from farm fa inner join crop_harvest ch on ch.FARM_CODE = fa.FARM_CODE inner join crop_harvest_details chd on chd.CROP_HARVEST_ID = ch.ID inner join procurement_product pp on pp.ID = chd.CROP inner join farm_crops fc on fc.FARM_ID = fa.ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID where ld.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 group by pp.code  ORDER BY  estimated,actual desc";
			break;
		case 'M':
			queryString = "select pp.name,sum(fc.EST_YIELD) as estimated,sum(chd.QTY) as actual from farm fa inner join crop_harvest ch on ch.FARM_CODE = fa.FARM_CODE inner join crop_harvest_details chd on chd.CROP_HARVEST_ID = ch.ID inner join procurement_product pp on pp.ID = chd.CROP inner join farm_crops fc on fc.FARM_ID = fa.ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID where c.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 group by pp.code  ORDER BY  estimated,actual desc";
			break;
		case 'V':
			queryString = "select pp.name,sum(fc.EST_YIELD) as estimated,sum(chd.QTY) as actual from farm fa inner join crop_harvest ch on ch.FARM_CODE = fa.FARM_CODE inner join crop_harvest_details chd on chd.CROP_HARVEST_ID = ch.ID inner join procurement_product pp on pp.ID = chd.CROP inner join farm_crops fc on fc.FARM_ID = fa.ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID where v.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 group by pp.code  ORDER BY  estimated,actual desc";
			break;
		default:
			if (!StringUtil.isEmpty(locationCode)) {
				queryString = " SELECT pp.NAME,sum( fc.EST_YIELD ) as estimated,sum( chd.QTY ) as actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID where fc.BRANCH_ID = '"
						+ locationCode + "' GROUP BY pp.CODE  ORDER BY  estimated,actual desc";

			} else {
				queryString = "SELECT pp.NAME,sum( fc.EST_YIELD ) as estimated,sum( chd.QTY ) as actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID GROUP BY pp.CODE  ORDER BY  estimated,actual desc";
			}
			break;
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;

	}

	public List<Object[]> listfarmerwithoutorganic() {
		// return list("SELECT DISTINCT f.id,f.firstName,f.lastName from Farmer
		// f where f.status=1 AND f.id in (select f.id from farmer f where
		// f.IS_FARMER_CERTIFIED=0) or f.id in (SELECT fics.farmer.id from
		// FarmIcsConversion fics where fics.icsType!=3 AND fics.isActive=1)");
		Session session = getSessionFactory().openSession();
		String query = "SELECT DISTINCT f.id ,f.FIRST_NAME,f.LAST_NAME,f.SUR_NAME from Farmer f where f.status=1 AND ( f.TYPEZ IS NULL OR f.TYPEZ != '2' )  AND f.id in (select f.id from farmer f where f.IS_FARMER_CERTIFIED=0) or f.id in (SELECT farmicscon1_.FARMER_ID FROM FARM_ICS_CONVERSION farmicscon1_ WHERE farmicscon1_.ICS_TYPE <> 3 AND farmicscon1_.IS_ACTIVE = 1)";
		// Session session = getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		List list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<Object[]> listFarmFieldsByFarmerIdAndNonOrganic(long id) {
		Session session = getSessionFactory().openSession();
		String query = "SELECT f.id,fics.ORGANIC_STATUS,f.FARM_NAME FROM farm f LEFT JOIN farm_ics_conversion fics ON fics.FARM_ID=f.id  and  fics.IS_ACTIVE = 1 WHERE f.FARMER_ID ='"
				+ id + "' ";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		List list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
		/*
		 * return (List<Object[]>) list(
		 * "select fm.id,fm.farmCode,fm.farmName,fm.farmId,fm.latitude,fm.longitude FROM Farm fm left join fm.farmer fr WHERE fr.id=?  and fm.status=1 AND fm.id in (select fic.farm.id from FarmIcsConversion fic where fic.icsType <>3 and fic.isActive=1) "
		 * , id);
		 */
	}

	public List<Object[]> listFarmFieldsByFarmerIdNonCertified(long id) {
		return (List<Object[]>) list(
				"select fm.id,fm.farmCode,fm.farmName,fm.farmId,fm.latitude,fm.longitude FROM Farm fm left join fm.farmer fr WHERE fr.id=?  and fm.status=1  ",
				id);
	}

	@Override
	public List<DynamicImageData> listDynamicImageByIds(List<Long> isList) {
		return list(
				" from DynamicImageData dy inner join dy.farmerDynamicFieldsValue df inner join df.farmerDynamicData fdd where fdd.id in (?)",
				isList);
	}

	@Override
	public void updateDistributionStatus(int deleteStatus, long distId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("update Distribution d set d.status=:deleteStatus where id=:distId");
		query.setParameter("deleteStatus", deleteStatus);
		query.setParameter("distId", distId);
		int result = query.executeUpdate();

	}

	public List<Object[]> estimatedYield(String locationCode) {
		char code = 0;
		String queryString;
		if (!StringUtil.isEmpty(locationCode)) {
			code = locationCode.charAt(0);
		}

		switch (code) {
		case 'C':
			queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID inner join state s on s.ID = ld.STATE_ID inner join country ctry on ctry.ID = s.COUNTRY_ID where ctry.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		case 'S':
			queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID inner join state s on s.ID = ld.STATE_ID where s.`CODE` = '"
					+ locationCode + "' and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1";
			break;
		case 'D':
			queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID where ld.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		case 'M':
			queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID where c.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		case 'V':
			queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID where v.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		default:
			if (!StringUtil.isEmpty(locationCode)) {
				queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID where fc.BRANCH_ID = '"
						+ locationCode + "'";

			} else {
				queryString = "SELECT pp.name,pp.code,fc.EST_YIELD AS estimated FROM farm fa INNER JOIN farm_crops fc ON fc.FARM_ID = fa.ID inner join procurement_variety pv on pv.ID = fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID = pv.PROCUREMENT_PRODUCT_ID ";
			}
			break;
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;

	}

	public List<Object[]> actualYield(String locationCode) {
		char code = 0;
		String queryString;
		if (!StringUtil.isEmpty(locationCode)) {
			code = locationCode.charAt(0);
		}

		switch (code) {
		case 'C':
			queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID inner join state s on s.ID = ld.STATE_ID inner join country ctry on ctry.ID = s.COUNTRY_ID where ctry.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		case 'S':
			queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID inner join state s on s.ID = ld.STATE_ID where s.`CODE` = '"
					+ locationCode + "' and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1";
			break;
		case 'D':
			queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID inner join location_detail ld on ld.ID = c.LOCATION_ID where ld.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		case 'M':
			queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID inner join city c on c.id = v.CITY_ID where c.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		case 'G':
			queryString = "SELECT pp.NAME,pp.CODE,chd.QTY AS actual FROM farm  fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN farmer f ON f.ID = fa.FARMER_ID INNER JOIN village v ON v.ID = f.VILLAGE_ID INNER JOIN gram_panchayat gp ON gp.ID = v.GRAM_PANCHAYAT_ID INNER JOIN city c ON c.id = gp.CITY_ID WHERE gp.`CODE` = '"
					+ locationCode + "' AND f.STATUS_CODE = 0 and fa.status=1 and f.status=1";
			break;
		case 'V':
			queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP inner join farmer f on f.ID = fa.FARMER_ID inner join village v on v.ID = f.VILLAGE_ID where v.`CODE` = '"
					+ locationCode + "'  and  f.STATUS_CODE = 0 and fa.status=1 and f.status=1 ";
			break;
		default:
			if (!StringUtil.isEmpty(locationCode)) {
				queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP where ch.BRANCH_ID = '"
						+ locationCode + "'";

			} else {
				queryString = "SELECT pp.NAME,pp.CODE,chd.QTY  AS actual FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP";
			}
			break;
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;

	}

	@Override
	public List<Farmer> listFarmerByIds(List<String> ids) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM Farmer fr WHERE fr.id in (:ids)");
		query.setParameterList("ids", ObjectUtil.stringListToLongList(ids));
		List<Farmer> result = (List<Farmer>) query.list();
		session.flush();
		session.close();
		return result;
	}

	public String getFieldValueByContant(String entityId, String referenceId, String group) {
		String value = "0";
		Object f = null;
		if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()))) {
			f = findFarmerById(Long.valueOf(referenceId));
		} else if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()))
				|| entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()))) {
			f = findFarmByID(Long.valueOf(referenceId));

		} else if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()))) {
			f = locationDAO.findSamithiById(Long.valueOf(referenceId));

		}

		if (group.contains(".") && f != null) {
			value = String.valueOf(ReflectUtil.getObjectField(f, group));
		} else if (f != null) {
			value = String.valueOf(ReflectUtil.getFieldValue(f, group));
		}
		return value;
	}

	@Override
	public List<DynamicConstants> listDynamicConstants() {
		return list("FROM DynamicConstants dsc ORDER BY dsc.code ASC");
	}

	public List<Object[]> warehouseToMobileUserChart(String branch) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch)) {
			hqlString = "SELECT d.servicePointId,d.servicePointName,sum((dd.quantity * p.price)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.branchId = :branch group by d.servicePointId ORDER BY amount desc";
		} else {
			hqlString = "SELECT d.servicePointId,d.servicePointName,sum((dd.quantity * p.price)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' group by d.servicePointId ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> warehouseToMobileUser_AgentChart(String branch, String warehouse) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch) && !StringUtil.isEmpty(warehouse)) {
			hqlString = "SELECT d.agentId,d.agentName,sum((dd.quantity * p.price)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.branchId = :branch and d.servicePointId = :warehouse group by d.agentId ORDER BY amount desc";
		} else {
			hqlString = "SELECT d.agentId,d.agentName,sum((dd.quantity * p.price)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.servicePointId = :warehouse group by d.agentId ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);

		}
		if (!StringUtil.isEmpty(warehouse)) {
			query.setParameter("warehouse", warehouse);
		}

		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> warehouseToMobileUser_ProductChart(String branch, String warehouse, String agentId) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch) && !StringUtil.isEmpty(warehouse) && !StringUtil.isEmpty(agentId)) {
			hqlString = "SELECT p.name,sum((dd.quantity * p.price)) as amount,dd.quantity,p.unit  From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.branchId = :branch and d.servicePointId = :warehouse and d.agentId = :agent group by p.code ORDER BY amount desc";
		} else {
			hqlString = "SELECT p.name,sum((dd.quantity * p.price)) as amount,dd.quantity,p.unit  From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.servicePointId = :warehouse and d.agentId = :agent group by p.code ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(warehouse)) {
			query.setParameter("warehouse", warehouse);
		}
		if (!StringUtil.isEmpty(agentId)) {
			query.setParameter("agent", agentId);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> populateMobileUserToFarmer_AgentChart(String branch, String season) {
		String hqlString = null;
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(season)) {
			if (!StringUtil.isEmpty(branch)) {
				hqlString = "SELECT d.agentId,d.agentName,sum((dd.quantity * p.price)) as amount,d.warehouseCode From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and (d.servicePointId = null or d.servicePointId='') and d.agentId != null and d.farmerId != null group by d.agentId ORDER BY amount desc";
			} else {
				hqlString = "SELECT d.agentId,d.agentName,sum((dd.quantity * p.price)) as amount,d.warehouseCode From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and (d.servicePointId = null or d.servicePointId='') and d.agentId != null and d.farmerId != null group by d.agentId ORDER BY amount desc";
			}
		} else {
			if (!StringUtil.isEmpty(branch)) {
				hqlString = "SELECT d.agentId,d.agentName,sum((dd.quantity * p.price)) as amount,d.warehouseCode From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.seasonCode=:season and (d.servicePointId = null or d.servicePointId='') and d.agentId != null and d.farmerId != null group by d.agentId ORDER BY amount desc";
			} else {
				hqlString = "SELECT d.agentId,d.agentName,sum((dd.quantity * p.price)) as amount,d.warehouseCode From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.seasonCode=:season and (d.servicePointId = null or d.servicePointId='') and d.agentId != null and d.farmerId != null group by d.agentId ORDER BY amount desc";
			}
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}

		if (!StringUtil.isEmpty(season)) {
			query.setParameter("season", season);
		}

		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> populateMobileUserToFarmer_FarmerChart(String branch, String agent) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch)) {
			hqlString = "SELECT d.farmerId,d.farmerName,sum((dd.quantity * dd.costPrice)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.servicePointId = null and d.agentId != null and d.farmerId != null and d.agentId = :agent group by d.farmerId ORDER BY amount desc";
		} else {
			hqlString = "SELECT d.farmerId,d.farmerName,sum((dd.quantity * dd.costPrice)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and d.servicePointId = null and d.agentId != null and d.farmerId != null and d.agentId = :agent group by d.farmerId ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		query.setParameter("agent", agent);
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> populateMobileUserToFarmer_ProductChart(String branch, String agent, String farmerId) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch)) {
			hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.servicePointId = null  and d.farmerId = :farmer and d.agentId = :agent group by p.code ORDER BY amount desc";
		} else {
			hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and d.servicePointId = null and   d.farmerId = :farmer and d.agentId = :agent group by p.code ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		query.setParameter("agent", agent);
		query.setParameter("farmer", farmerId);
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> populateWarehouseToFarmer_WarehouseChart(String branch) {
		Session session = getSessionFactory().openSession();
		String sqlString;
		if (!StringUtil.isEmpty(branch)) {
			sqlString = "SELECT d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,sum((dd.quantity * p.price)) as amount From Distribution d inner join distribution_detail dd on d.id=dd.DISTRIBUTION_ID inner join product p on p.id=dd.PRODUCT_ID inner join farmer f on f.FARMER_ID=d.FARMER_ID "
					+ "where d.txn_Type = '314' and d.branch_Id =:branch and d.SERVICE_POINT_ID is not null and d.SERVICE_POINT_ID <> '' and d.farmer_Id is not null and f.STATUS_CODE=0 and f.STATUS=1 group by d.SERVICE_POINT_ID ORDER BY amount desc";
		} else {
			sqlString = "SELECT d.SERVICE_POINT_ID,d.SERVICE_POINT_NAME,sum((dd.quantity * p.price)) as amount From Distribution d inner join distribution_detail dd on d.id=dd.DISTRIBUTION_ID inner join product p on p.id=dd.PRODUCT_ID inner join farmer f on f.FARMER_ID=d.FARMER_ID"
					+ "where d.txn_Type = '314' and d.SERVICE_POINT_ID is not null and d.SERVICE_POINT_ID <> '' and d.farmer_Id is not null and f.STATUS_CODE=0 and f.STATUS=1 group by d.SERVICE_POINT_ID ORDER BY amount desc";
		}
		SQLQuery query = session.createSQLQuery(sqlString);
		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> populateWarehouseToFarmer_FarmerChart(String branch, String warehouse) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();

		if (!StringUtil.isEmpty(branch)) {
			hqlString = "SELECT d.farmerId,d.farmerName,sum((dd.quantity * p.price)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.servicePointId != null  and d.farmerId != null and d.servicePointId = :warehouse group by d.farmerId ORDER BY amount desc";
		} else {
			hqlString = "SELECT d.farmerId,d.farmerName,sum((dd.quantity * p.price)) as amount From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and d.servicePointId != null and   d.farmerId != null and d.servicePointId = :warehouse group by d.farmerId ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		query.setParameter("warehouse", warehouse);
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> populateWarehouseToFarmer_ProductChart(String branch, String warehouse, String farmerId) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();

		if (!StringUtil.isEmpty(branch)) {
			hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.servicePointId = :warehouse  and d.farmerId = :farmerId  group by p.code ORDER BY amount desc";
		} else {
			hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and d.servicePointId = :warehouse and   d.farmerId = :farmerId  group by p.code ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(warehouse)) {
			query.setParameter("warehouse", warehouse);
		}
		if (!StringUtil.isEmpty(farmerId)) {
			query.setParameter("farmerId", farmerId);
		}

		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> productChartByWarehouseToMobileUser(String branch, String warehouse) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch) && !StringUtil.isEmpty(warehouse)) {
			hqlString = "SELECT p.name,sum((dd.quantity * p.price)) as amount,dd.quantity,p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.branchId = :branch and d.servicePointId = :warehouse  group by p.code ORDER BY amount desc";
		} else {
			hqlString = "SELECT p.name,sum((dd.quantity * p.price)) as amount,dd.quantity,p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '514' and d.servicePointId = :warehouse group by p.code ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(warehouse)) {
			query.setParameter("warehouse", warehouse);
		}

		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> productChartByMobileUserToFarmer(String branch, String agent, String selectedSeason) {
		String hqlString = null;
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedSeason)) {
			if (!StringUtil.isEmpty(branch)) {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and (d.servicePointId = null or d.servicePointId ='') and d.agentId = :agent group by p.code ORDER BY amount desc";
			} else {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and (d.servicePointId = null or d.servicePointId ='') and  d.agentId = :agent group by p.code ORDER BY amount desc";
			}
		} else {
			if (!StringUtil.isEmpty(branch)) {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.seasonCode=:season and (d.servicePointId = null or d.servicePointId ='') and d.agentId = :agent group by p.code ORDER BY amount desc";
			} else {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.seasonCode=:season and (d.servicePointId = null or d.servicePointId ='') and  d.agentId = :agent group by p.code ORDER BY amount desc";
			}
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(selectedSeason)) {
			query.setParameter("season", selectedSeason);
		}
		query.setParameter("agent", agent);

		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> productChartByWarehouseToFarmer(String branch, String warehouse) {
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();

		if (!StringUtil.isEmpty(branch)) {
			hqlString = "SELECT p.name,p.code,sum((dd.quantity * p.price)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.branchId = :branch and d.servicePointId = :warehouse    group by p.code ORDER BY amount desc";
		} else {
			hqlString = "SELECT p.name,p.code,sum((dd.quantity * p.price)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314'  and d.servicePointId = :warehouse  group by p.code ORDER BY amount desc";
		}
		Query query = session.createQuery(hqlString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(warehouse)) {
			query.setParameter("warehouse", warehouse);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> listParentFields() {
		return list("select dfc.id,dfc.componentName from DynamicFieldConfig dfc where dfc.componentType in (2,4,6,9)");
	}

	@Override
	public List<Object[]> ListFarmerDynamicDataAgentByTxnType(String txnType, String branchId) {
		Session session = getSessionFactory().openSession();
		String qry = "SELECT DISTINCT fd.CREATED_USER, IFNULL( CONCAT( pi.FIRST_NAME, ' ', pi.LAST_NAME ), CONCAT( epi.FIRST_NAME, ' ', epi.LAST_NAME ) ) FROM  `farmer_dynamic_data` fd LEFT JOIN prof ap ON ap.PROF_ID = fd.CREATED_USER   AND ap.BRANCH_ID = fd.BRANCH_ID   LEFT JOIN pers_info pi ON pi.id = ap.PERS_INFO_ID  LEFT JOIN ese_user es ON es.USER_NAME = fd.CREATED_USER AND es.BRANCH_ID = fd.BRANCH_ID   LEFT JOIN pers_info epi ON epi.id = es.PERS_INFO_ID where    fd.txn_type =:txnType GROUP BY fd.CREATED_USER";
		if (branchId != null && !StringUtil.isEmpty(branchId)) {
			//qry = "SELECT DISTINCT fd.CREATED_USER, IFNULL( CONCAT( pi.FIRST_NAME, ' ', pi.LAST_NAME ), CONCAT( epi.FIRST_NAME, ' ', epi.LAST_NAME ) ) FROM  `farmer_dynamic_data` fd LEFT JOIN prof ap ON ap.PROF_ID = fd.CREATED_USER   AND ap.BRANCH_ID = fd.BRANCH_ID   LEFT JOIN pers_info pi ON pi.id = ap.PERS_INFO_ID  LEFT JOIN ese_user es ON es.USER_NAME = fd.CREATED_USER AND es.BRANCH_ID = fd.BRANCH_ID   LEFT JOIN pers_info epi ON epi.id = es.PERS_INFO_ID where    fd.txn_type =:txnType  AND fd.branch_id =:branchId GROUP BY fd.CREATED_USER";
			qry = "SELECT DISTINCT fd.CREATED_USER, IFNULL( CONCAT( pi.FIRST_NAME, ' ', pi.LAST_NAME ), CONCAT( epi.FIRST_NAME, ' ', epi.LAST_NAME ) ) FROM  `farmer_dynamic_data` fd LEFT JOIN prof ap ON ap.PROF_ID = fd.CREATED_USER   AND ap.BRANCH_ID = fd.BRANCH_ID   LEFT JOIN pers_info pi ON pi.id = ap.PERS_INFO_ID  LEFT JOIN ese_user es ON es.USER_NAME = fd.CREATED_USER AND es.BRANCH_ID = fd.BRANCH_ID   LEFT JOIN pers_info epi ON epi.id = es.PERS_INFO_ID where    fd.txn_type =:txnType  AND FIND_IN_SET(fd.branch_id,(:branchId))GROUP BY fd.CREATED_USER";
		}
		SQLQuery query = session.createSQLQuery(qry);
		query.setParameter("txnType", txnType);
		if (branchId != null && !StringUtil.isEmpty(branchId))
			query.setParameter("branchId", branchId);
		List list = query.list();

		session.flush();
		session.close();
		return list;
	}

	public List<Object[]> populateAvailableColumns() {
		String queryString = "SHOW COLUMNS FROM dynamic_report";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> getGridData(String queryString) {
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<FarmerDynamicData> ListFarmerDynamicDatas(long l) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(FarmerDynamicData.class);
		criteria.add(Restrictions.sqlRestriction("find_in_set(" + l + ",this_.REFERENCE_ID)"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List list = criteria.list();
		session.flush();
		session.close();
		return list;

	}

	@Override
	public FarmerDynamicData findFarmerDynamicDataByTxnUniquId(String txnUniquId) {
		// TODO Auto-generated method stub
		return (FarmerDynamicData) find("from FarmerDynamicData where txnUniqueId = ? ", Long.valueOf(txnUniquId));
	}

	public List<Object[]> cropHarvestByFarmerId(String farmerId, String startDate, String endDate, String seasonCode) {
		String queryString = null;
		if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT ch.HARVEST_DATE,ch.FARM_NAME, pp.NAME as product, pp.UNIT as unit, pv.`NAME` as variety, pg.`NAME` as grade, chd.QTY AS actual,ch.SEASON_CODE,chd.PRICE,(select concat (pinfo.FIRST_NAME,pinfo.LAST_NAME) from pers_info pinfo where pinfo.ID = (select pro.PERS_INFO_ID from prof pro where pro.PROF_ID = ch.AGENT_ID AND pro.BRANCH_ID=ch.BRANCH_ID)) as agentName FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN procurement_grade pg on pg.ID = chd.GRADE INNER JOIN procurement_variety pv on pv.ID = pg.PROCUREMENT_VARIETY_ID where fa.FARMER_ID = '"
					+ farmerId + "'";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT ch.HARVEST_DATE,ch.FARM_NAME, pp.NAME as product, pp.UNIT as unit, pv.`NAME` as variety, pg.`NAME` as grade, chd.QTY AS actual,ch.SEASON_CODE,chd.PRICE,(select concat (pinfo.FIRST_NAME,pinfo.LAST_NAME) from pers_info pinfo where pinfo.ID = (select pro.PERS_INFO_ID from prof pro where pro.PROF_ID = ch.AGENT_ID AND pro.BRANCH_ID=ch.BRANCH_ID)) as agentName FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN procurement_grade pg on pg.ID = chd.GRADE INNER JOIN procurement_variety pv on pv.ID = pg.PROCUREMENT_VARIETY_ID where fa.FARMER_ID = '"
					+ farmerId + "' and ch.SEASON_CODE = '" + seasonCode + "' and ch.HARVEST_DATE BETWEEN '" + startDate
					+ "' and '" + endDate + "'";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT ch.HARVEST_DATE,ch.FARM_NAME, pp.NAME as product, pp.UNIT as unit, pv.`NAME` as variety, pg.`NAME` as grade, chd.QTY AS actual,ch.SEASON_CODE,chd.PRICE,(select concat (pinfo.FIRST_NAME,pinfo.LAST_NAME) from pers_info pinfo where pinfo.ID = (select pro.PERS_INFO_ID from prof pro where pro.PROF_ID = ch.AGENT_ID AND pro.BRANCH_ID=ch.BRANCH_ID)) as agentName FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN procurement_grade pg on pg.ID = chd.GRADE INNER JOIN procurement_variety pv on pv.ID = pg.PROCUREMENT_VARIETY_ID where fa.FARMER_ID = '"
					+ farmerId + "' and ch.HARVEST_DATE BETWEEN '" + startDate + "' and '" + endDate + "'";
		} else if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT ch.HARVEST_DATE,ch.FARM_NAME, pp.NAME as product, pp.UNIT as unit, pv.`NAME` as variety, pg.`NAME` as grade, chd.QTY AS actual,ch.SEASON_CODE,chd.PRICE,(select concat (pinfo.FIRST_NAME,pinfo.LAST_NAME) from pers_info pinfo where pinfo.ID = (select pro.PERS_INFO_ID from prof pro where pro.PROF_ID = ch.AGENT_ID AND pro.BRANCH_ID=ch.BRANCH_ID)) as agentName FROM farm fa INNER JOIN crop_harvest ch ON ch.FARM_CODE = fa.FARM_CODE INNER JOIN crop_harvest_details chd ON chd.CROP_HARVEST_ID = ch.ID INNER JOIN procurement_product pp ON pp.ID = chd.CROP INNER JOIN procurement_grade pg on pg.ID = chd.GRADE INNER JOIN procurement_variety pv on pv.ID = pg.PROCUREMENT_VARIETY_ID where fa.FARMER_ID = '"
					+ farmerId + "' and ch.SEASON_CODE = '" + seasonCode + "'";
		}
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> distributionToFarmerByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode) {
		String queryString = null;
		if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT d.TXN_TIME,d.SERVICE_POINT_NAME,d.AGENT_NAME,p.name,d.IS_FREE_DISTRIBUTION,dd.QUANTITY,d.TOTAL_AMOUNT,d.FINAL_AMOUNT,d.PAYMENT_AMT,d.season_code,p.unit,p.price,d.TAX,sc.NAME as categoryName FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN farmer f on f.FARMER_ID = d.FARMER_ID INNER JOIN sub_category sc on sc.ID = p.SUB_CATEGORY_ID WHERE d.TXN_TYPE = '314' AND ( d.FARMER_ID IS NOT NULL ) AND f.ID = '"
					+ farmerId + "'";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT d.TXN_TIME,d.SERVICE_POINT_NAME,d.AGENT_NAME,p.name,d.IS_FREE_DISTRIBUTION,dd.QUANTITY,d.TOTAL_AMOUNT,d.FINAL_AMOUNT,d.PAYMENT_AMT,d.season_code,p.unit,p.price,d.TAX,sc.NAME as categoryName FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN farmer f on f.FARMER_ID = d.FARMER_ID INNER JOIN sub_category sc on sc.ID = p.SUB_CATEGORY_ID WHERE d.TXN_TYPE = '314' AND ( d.FARMER_ID IS NOT NULL ) AND f.ID = '"
					+ farmerId + "'and d.season_code = '" + seasonCode + "'  and d.TXN_TIME BETWEEN '" + startDate
					+ "' and '" + endDate + "'";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT d.TXN_TIME,d.SERVICE_POINT_NAME,d.AGENT_NAME,p.name,d.IS_FREE_DISTRIBUTION,dd.QUANTITY,d.TOTAL_AMOUNT,d.FINAL_AMOUNT,d.PAYMENT_AMT,d.season_code,p.unit,p.price,d.TAX,sc.NAME as categoryName FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN farmer f on f.FARMER_ID = d.FARMER_ID INNER JOIN sub_category sc on sc.ID = p.SUB_CATEGORY_ID WHERE d.TXN_TYPE = '314' AND ( d.FARMER_ID IS NOT NULL ) AND f.ID = '"
					+ farmerId + "' and d.TXN_TIME BETWEEN '" + startDate + "' and '" + endDate + "'";
		} else if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "SELECT d.TXN_TIME,d.SERVICE_POINT_NAME,d.AGENT_NAME,p.name,d.IS_FREE_DISTRIBUTION,dd.QUANTITY,d.TOTAL_AMOUNT,d.FINAL_AMOUNT,d.PAYMENT_AMT,d.season_code,p.unit,p.price,d.TAX,sc.NAME as categoryName FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN farmer f on f.FARMER_ID = d.FARMER_ID INNER JOIN sub_category sc on sc.ID = p.SUB_CATEGORY_ID WHERE d.TXN_TYPE = '314' AND ( d.FARMER_ID IS NOT NULL ) AND f.ID = '"
					+ farmerId + "'and d.season_code = '" + seasonCode + "'";
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> productReturnByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode) {
		// String queryString = "select
		// pr.TXN_TIME,pr.STOCK_TYPE,pr.SERVICE_POINT_NAME,pr.AGENT_NAME,p.`NAME`,prd.UNIT,prd.COST_PRICE,prd.SELLING_PRICE,prd.SUB_TOTAL
		// from product_return pr INNER JOIN product_return_detail prd on
		// prd.PRODUCT_RETURN_ID = pr.ID INNER JOIN product p on p.ID =
		// prd.PRODUCT_ID inner join farmer f on f.FARMER_ID = pr.FARMER_ID
		// where f.ID = '"+farmerId+"' GROUP BY pr.ID";

		String queryString = null;
		if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "select pr.TXN_TIME,pr.STOCK_TYPE,pr.SERVICE_POINT_NAME,pr.AGENT_NAME,p.`NAME`,prd.UNIT,prd.COST_PRICE,prd.SELLING_PRICE,prd.SUB_TOTAL,prd.EXISTING_QUANTITY,prd.QUANTITY,pr.season_code,p.PRICE from product_return pr INNER JOIN product_return_detail prd on prd.PRODUCT_RETURN_ID = pr.ID INNER JOIN product p on p.ID = prd.PRODUCT_ID inner join farmer f on f.FARMER_ID = pr.FARMER_ID where f.ID = '"
					+ farmerId + "' ";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "select pr.TXN_TIME,pr.STOCK_TYPE,pr.SERVICE_POINT_NAME,pr.AGENT_NAME,p.`NAME`,prd.UNIT,prd.COST_PRICE,prd.SELLING_PRICE,prd.SUB_TOTAL,prd.EXISTING_QUANTITY,prd.QUANTITY,pr.season_code,p.PRICE from product_return pr INNER JOIN product_return_detail prd on prd.PRODUCT_RETURN_ID = pr.ID INNER JOIN product p on p.ID = prd.PRODUCT_ID inner join farmer f on f.FARMER_ID = pr.FARMER_ID where f.ID = '"
					+ farmerId + "' and pr.season_code = '" + seasonCode + "'  and pr.TXN_TIME BETWEEN '" + startDate
					+ "' and '" + endDate + "' ";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "select pr.TXN_TIME,pr.STOCK_TYPE,pr.SERVICE_POINT_NAME,pr.AGENT_NAME,p.`NAME`,prd.UNIT,prd.COST_PRICE,prd.SELLING_PRICE,prd.SUB_TOTAL,prd.EXISTING_QUANTITY,prd.QUANTITY,pr.season_code,p.PRICE from product_return pr INNER JOIN product_return_detail prd on prd.PRODUCT_RETURN_ID = pr.ID INNER JOIN product p on p.ID = prd.PRODUCT_ID inner join farmer f on f.FARMER_ID = pr.FARMER_ID where f.ID = '"
					+ farmerId + "' and pr.TXN_TIME BETWEEN '" + startDate + "' and '" + endDate + "' ";
		} else if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "select pr.TXN_TIME,pr.STOCK_TYPE,pr.SERVICE_POINT_NAME,pr.AGENT_NAME,p.`NAME`,prd.UNIT,prd.COST_PRICE,prd.SELLING_PRICE,prd.SUB_TOTAL,prd.EXISTING_QUANTITY,prd.QUANTITY,pr.season_code,p.PRICE from product_return pr INNER JOIN product_return_detail prd on prd.PRODUCT_RETURN_ID = pr.ID INNER JOIN product p on p.ID = prd.PRODUCT_ID inner join farmer f on f.FARMER_ID = pr.FARMER_ID where f.ID = '"
					+ farmerId + "' and pr.season_code = '" + seasonCode + "' ";
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> trainingStatusReportByFarmerId(String farmerId, String startDate, String endDate) {
		// String queryString = "select
		// ts.TRAINING_DATE,ts.TRAINING_CODE,ts.TRAINING_ASSISTANT_NAME,ts.TIME_TAKEN_FOR_TRAINING,ts.FARMER_ATTENED,ts.REMARKS
		// from training_status ts inner join farmer f on f.FARMER_ID in(
		// ts.FARMER_ID ) where f.ID = '"+farmerId+"' GROUP BY ts.ID";

		String queryString;
		if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
			queryString = "select ts.TRAINING_DATE,ts.TRAINING_CODE,ts.TRAINING_ASSISTANT_NAME,ts.TIME_TAKEN_FOR_TRAINING,ts.FARMER_ATTENED,ts.REMARKS,(select ti.AGENT_NAME from transfer_info ti where ti.ID = ts.TRANSFER_INFO_ID) from training_status ts inner join farmer f on f.FARMER_ID in( ts.FARMER_ID ) where f.ID = '"
					+ farmerId + "' GROUP BY ts.ID";
		} else {
			queryString = "select ts.TRAINING_DATE,ts.TRAINING_CODE,ts.TRAINING_ASSISTANT_NAME,ts.TIME_TAKEN_FOR_TRAINING,ts.FARMER_ATTENED,ts.REMARKS,(select ti.AGENT_NAME from transfer_info ti where ti.ID = ts.TRANSFER_INFO_ID) from training_status ts inner join farmer f on f.FARMER_ID in( ts.FARMER_ID ) where f.ID = '"
					+ farmerId + "'  and ts.TRAINING_DATE BETWEEN '" + startDate + "' and '" + endDate
					+ "' GROUP BY ts.ID";
			// queryString = "select
			// pr.TXN_TIME,pr.STOCK_TYPE,pr.SERVICE_POINT_NAME,pr.AGENT_NAME,p.`NAME`,prd.UNIT,prd.COST_PRICE,prd.SELLING_PRICE,prd.SUB_TOTAL
			// from product_return pr INNER JOIN product_return_detail prd on
			// prd.PRODUCT_RETURN_ID = pr.ID INNER JOIN product p on p.ID =
			// prd.PRODUCT_ID inner join farmer f on f.FARMER_ID = pr.FARMER_ID
			// where f.ID = '"+farmerId+"' and pr.TXN_TIME BETWEEN
			// '"+startDate+"' and '"+endDate+"' GROUP BY pr.ID";
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> farmerBalanceReportByFarmerId(String farmerId, String startDate, String endDate) {
		String queryString;
		if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
			queryString = "select trxn.TXN_TIME,trxn.TXN_DESC,trxn.RECEIPT_NO,trxn.INT_BAL,trxn.TXN_AMT,trxn.BAL_AMT from trxn_agro trxn inner join farmer f on f.FARMER_ID  =  trxn.FARMER_ID where f.ID = '"
					+ farmerId + "'";
		} else {
			queryString = "select trxn.TXN_TIME,trxn.TXN_DESC,trxn.RECEIPT_NO,trxn.INT_BAL,trxn.TXN_AMT,trxn.BAL_AMT from trxn_agro trxn inner join farmer f on f.FARMER_ID  =  trxn.FARMER_ID where f.ID = '"
					+ farmerId + "' and trxn.TXN_TIME BETWEEN '" + startDate + "' and '" + endDate + "'";
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> procurementTransactionsByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode) {
		String queryString = null;
		if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "select p.CREATED_DATE,p.SEASON_CODE,pp.`NAME`,pp.UNIT,pd.NUMBER_OF_BAGS,pd.NET_WEIGHT,p.TOTAL_AMOUNT,p.PAYMENT_AMT, (select ta.AGENT_NAME from trxn_agro ta where ta.ID = p.TRXN_AGRO_ID) from procurement p inner join procurement_detail pd on pd.PROCUREMENT_ID = p.ID inner join procurement_product pp on pp.ID = pd.PROCUREMENT_PRODUCT_ID where p.FARMER_ID = '"
					+ farmerId + "'";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "select p.CREATED_DATE,p.SEASON_CODE,pp.`NAME`,pp.UNIT,pd.NUMBER_OF_BAGS,pd.NET_WEIGHT,p.TOTAL_AMOUNT,p.PAYMENT_AMT, (select ta.AGENT_NAME from trxn_agro ta where ta.ID = p.TRXN_AGRO_ID) from procurement p inner join procurement_detail pd on pd.PROCUREMENT_ID = p.ID inner join procurement_product pp on pp.ID = pd.PROCUREMENT_PRODUCT_ID where p.FARMER_ID = '"
					+ farmerId + "' and p.SEASON_CODE = '" + seasonCode + "' and p.CREATED_DATE BETWEEN '" + startDate
					+ "' and '" + endDate + "'";
		} else if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && StringUtil.isEmpty(seasonCode)) {
			queryString = "select p.CREATED_DATE,p.SEASON_CODE,pp.`NAME`,pp.UNIT,pd.NUMBER_OF_BAGS,pd.NET_WEIGHT,p.TOTAL_AMOUNT,p.PAYMENT_AMT, (select ta.AGENT_NAME from trxn_agro ta where ta.ID = p.TRXN_AGRO_ID) from procurement p inner join procurement_detail pd on pd.PROCUREMENT_ID = p.ID inner join procurement_product pp on pp.ID = pd.PROCUREMENT_PRODUCT_ID where p.FARMER_ID = '"
					+ farmerId + "' and p.CREATED_DATE BETWEEN '" + startDate + "' and '" + endDate + "'";
		} else if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate) && !StringUtil.isEmpty(seasonCode)) {
			queryString = "select p.CREATED_DATE,p.SEASON_CODE,pp.`NAME`,pp.UNIT,pd.NUMBER_OF_BAGS,pd.NET_WEIGHT,p.TOTAL_AMOUNT,p.PAYMENT_AMT, (select ta.AGENT_NAME from trxn_agro ta where ta.ID = p.TRXN_AGRO_ID) from procurement p inner join procurement_detail pd on pd.PROCUREMENT_ID = p.ID inner join procurement_product pp on pp.ID = pd.PROCUREMENT_PRODUCT_ID where p.FARMER_ID = '"
					+ farmerId + "' and p.SEASON_CODE = '" + seasonCode + "'";
		}

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public void saveDynamicReport(String title, String des, String query, String header_fields, String filter_data,
			String entity, String fields, String groupByField, String id) {
		Session session = getSessionFactory().openSession();
		query = query.replaceAll("\"", "\\\\\"");
		String query1 = "insert into `saved_dynamic_report` VALUES (NULL ," + "'" + title + "'" + "," + "'" + des + "'"
				+ ", " + "'" + query + "'" + ", " + "'" + header_fields + "'" + "," + "'" + filter_data + "'" + ", "
				+ "'" + entity + "'" + "," + "'" + fields + "'," + "'" + groupByField + "'" + ");";

		if (id != null && !StringUtil.isEmpty(id) && StringUtil.isLong(id)) {
			query1 = "update `saved_dynamic_report` set " + "  query=" + "'" + query + "'" + ",  header_fields= " + "'"
					+ header_fields + "'" + ", filter_data=" + "'" + filter_data + "'" + ",  entity= " + "'" + entity
					+ "'" + ", FIELDS_SELECTED = " + "'" + fields + "',  GROUP_BY_FIELD = " + "'" + groupByField + "'"
					+ " where id = " + id + ";";
		}

		SQLQuery sqlQuery1 = session.createSQLQuery(query1);
		sqlQuery1.executeUpdate();
		session.flush();
		session.close();
	}

	public void deleteDynamicReportById(String id) {
		Session session = getSessionFactory().openSession();
		String query1 = "delete  from saved_dynamic_report where id = " + id;
		SQLQuery sqlQuery1 = session.createSQLQuery(query1);
		sqlQuery1.executeUpdate();
		session.flush();
		session.close();
	}

	public void updateDynamicReportById(String id, String title, String des, String query, String header_fields,
			String filter_data, String entity, String fields, String groupByField) {

		Session session = getSessionFactory().openSession();
		String query1 = "update saved_dynamic_report set ";

		if (!StringUtil.isEmpty(title)) {
			query1 = query1 + " title = " + "'" + title + "' , ";
		}

		if (!StringUtil.isEmpty(des)) {
			query1 = query1 + " des = " + "'" + des + "'";
		}

		if (!StringUtil.isEmpty(des) && !StringUtil.isEmpty(query)) {
			query1 = query1 + ",";
		}

		if (!StringUtil.isEmpty(query)) {
			query1 = query1 + " query = " + "'" + query + "' , ";
		}

		if (!StringUtil.isEmpty(header_fields)) {
			query1 = query1 + " header_fields = " + "'" + header_fields + "' , ";
		}

		if (!StringUtil.isEmpty(filter_data)) {
			query1 = query1 + " filter_data = " + "'" + filter_data + "'";
		}

		if (!StringUtil.isEmpty(fields)) {
			query1 = query1 + " FIELDS_SELECTED = " + "'" + fields + "'";
		}

		if (!StringUtil.isEmpty(groupByField)) {
			query1 = query1 + " GROUP_BY_FIELD = " + "'" + groupByField + "'";
		}

		query1 = query1 + " where id = " + id;

		SQLQuery sqlQuery1 = session.createSQLQuery(query1);
		sqlQuery1.executeUpdate();
		session.flush();
		session.close();
	}

	public Farmer findFarmerByFirstNameLastNameAndSurName(String firstName, String lastName, String surName) {

		Object[] values = { firstName, lastName, surName, ESETxnStatus.SUCCESS.ordinal() };
		return (Farmer) find(
				"FROM Farmer fr WHERE fr.firstName = ? AND fr.lastName = ? AND fr.surName = ? AND fr.statusCode = ?",
				values);
	}

	public Farmer findFarmerByFirstNameAndSurName(String firstName, String surName) {

		Object[] values = { firstName, surName, ESETxnStatus.SUCCESS.ordinal() };
		return (Farmer) find("FROM Farmer fr WHERE fr.firstName = ? AND fr.surName = ? AND fr.statusCode = ?", values);
	}

	@Override
	public List<TreeDetail> findTreeDetailByFarmId(long id) {
		Session session = getSessionFactory().openSession();
		// Query query = session.createQuery("SELECT fs from FarmCrops fr INNER
		// JOIN fr.seedTreatments fs WHERE fr.id=:ID");
		Query query = session.createQuery("SELECT td from Farm fm INNER JOIN fm.treeDetails td WHERE fm.id=:ID");
		query.setParameter("ID", id);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public List<Object[]> periodicInspectionsByFarmerId(String farmCode, String startDate, String endDate,
			String seasonCode) {
		String queryString = null;
		queryString = "select p.INSPECTION_DATE,p.SEASON_CODE,p.FARM_ID,p.CROP_CODE,p.CREATE_USER_NAME,p.CURRENT_STATUS_OF_GROWTH,p.NAME_OF_INTERCROP from periodic_Inspection p where p.FARM_ID= '"
				+ farmCode + "' and p.INSPECTION_TYPE='1' ";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> periodicNeedBasedInspectionsByFarmerId(String farmCode, String startDate, String endDate,
			String seasonCode) {
		String queryString = null;
		queryString = "select p.INSPECTION_DATE,p.SEASON_CODE,p.FARM_ID,p.CROP_CODE,p.CREATE_USER_NAME,p.NAME_OF_INTERCROP from periodic_Inspection p where p.FARM_ID= '"
				+ farmCode + "' and p.INSPECTION_TYPE='2' ";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object> ListFarmerDynamicDatasByIds(List<Long> mainIds) {
		List<Object> finalLis = new ArrayList<>();
		Session session = getSessionFactory().openSession();
		// Query query = session.createQuery("SELECT fs from FarmCrops fr INNER
		// JOIN fr.seedTreatments fs WHERE fr.id=:ID");
		Query query = session.createSQLQuery(
				"select  fdv.FIELD_NAME,count(distinct typez) from farmer_dynamic_data fd join farmer_dynamic_field_value fdv on fd.id = fdv.FARMER_DYNAMIC_DATA_ID and fdv.TYPEZ is not null  and fd.id in (:ids)  group by fdv.FIELD_NAME ");
		query.setParameterList("ids", mainIds);
		List list = query.list();
		query = session.createSQLQuery(
				"CALL `export_fn`('" + mainIds.stream().map(Object::toString).collect(Collectors.joining(",")) + "')");
		List list1 = query.list();
		finalLis.add(0, list);
		finalLis.add(1, list1);
		session.flush();
		session.close();
		return finalLis;
	}

	@Override
	public List<DynamicReportFieldsConfig> populateDynamicReportFields() {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(DynamicReportFieldsConfig.class);
		List list = criteria.list();
		session.flush();
		session.close();
		return list;

	}

	public List<Object[]> listFarmerHavingPlots() {

		Object[] values = { ESETxnStatus.SUCCESS.ordinal() };
		return list(
				"select distinct fr.id,fr.firstName,fr.lastName FROM Farmer fr join fr.farms fm join fm.coordinates c WHERE  fr.statusCode = ? ",
				values);
	}

	public List<Object[]> listFarmByFarmerIds(long id) {

		Object[] values = { id, 1 };
		return list(
				"select distinct fm.id,fm.farmName,fm.revisionNo FROM Farm  fm join fm.coordinates c WHERE fm.farmer.id =? AND fm.status = ? ",
				values);
	}

	public List<FarmCropsField> listFarmCropsFields() {
		Object[] values = { FarmCropsField.ACTIVE };
		return list("From FarmCropsField fcf WHERE fcf.status=?", values);
	}

	public FarmerDynamicData findFarmerDynamicDataByReferenceId(long referenceId) {

		return (FarmerDynamicData) find("FROM FarmerDynamicData fdfv where fdfv.referenceId=? order by fdfv.id desc",
				String.valueOf(referenceId));
	}

	@Override
	public int findDynamicMenuMaxOrderNo() {
		// TODO Auto-generated method stub
		int maxOrderNo = 0;
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select max(d.order) from DynamicFeildMenuConfig d");
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			String qry = String.valueOf(query.list().get(0));
			if ((!StringUtil.isEmpty(qry)) && (qry != null) && (!qry.equalsIgnoreCase("null"))) {
				maxOrderNo = Integer.valueOf(qry);
			}
		}
		return maxOrderNo;
	}

	@Override
	public List<Object[]> listColdStorageNameDynamic() {

		// TODO Auto-generated method stub
		Object[] values = { FarmCatalogue.ACTIVE, Integer.valueOf("126") };
		return list(
				"Select fc.code,fc.name FROM FarmCatalogue fc where fc.status=? and fc.typez=? order by fc.name asc",
				values);
	}

	@Override
	public DynamicReportTableConfig findDyamicReportTableConfigById(Long enity) {
		// TODO Auto-generated method stub
		return (DynamicReportTableConfig) find("from DynamicReportTableConfig where id=?", enity);
	}

	@Override
	public FarmCrops findFarmCropById(Long caseId) {
		// TODO Auto-generated method stub
		return (FarmCrops) find("FROM FarmCrops fcp join fetch fcp.farm  WHERE fcp.id = ?", caseId);
	}

	@Override
	public boolean isFarmMappingFarm(long id) {
		// TODO Auto-generated method stub
		List<Farm> farmList = list("FROM Farm fm WHERE fm.status!=2 AND fm.farmer.id = ?", id);
		if (!ObjectUtil.isListEmpty(farmList)) {
			return true;
		}
		return false;
	}

	public WarehouseStorageMap findWarehouseStorageMapByWarehouseId(Long id) {
		return (WarehouseStorageMap) find("FROM WarehouseStorageMap w  WHERE w.id = ?", id);
	}

	@Override
	public List<WarehouseStorageMap> listOfWarehouseStorageMap(long id) {
		// TODO Auto-generated method stub
		List<WarehouseStorageMap> warehouseStorageMapList = list("FROM WarehouseStorageMap w where w.warehouse.id= ? ",
				id);
		return warehouseStorageMapList;
	}

	public List<Object[]> listFarmCropsFieldsByFarmerIdAgentIdAndSeason(long agentId, Long revisionNo,
			String seasonCode) {
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT fcp.id,COALESCE(fcp.procurementVariety.code,''),COALESCE(fcp.procurementVariety.procurementProduct.code,''),COALESCE(fcp.seedSource,''),COALESCE(fcp.seedQtyCost,''),COALESCE(fcp.estimatedYield,''),COALESCE(fcp.cropSeason.code,''),COALESCE(fcp.cropCategory,''),COALESCE(fcp.farm.farmCode,''),COALESCE(fcp.farm.farmer.farmerId,''),COALESCE(fcp.riskAssesment,''),COALESCE(fcp.seedTreatmentDetails,''),COALESCE(fcp.otherSeedTreatmentDetails,''),COALESCE(fcp.stapleLength,''),COALESCE(fcp.seedQtyUsed,''),COALESCE(fcp.type,''),COALESCE(fcp.cropCategoryList,''),COALESCE(fcp.cultiArea,''),COALESCE(fcp.sowingDate,''),COALESCE(fcp.estimatedHarvestDate,''),COALESCE(fcp.interType,''),COALESCE(fcp.interAcre,''),COALESCE(fcp.totalCropHarv,''),COALESCE(fcp.grossIncome,''),fcp.farm.id,fcp.farm.farmer.id,fcp.status,fcp.revisionNo,fcp.cropEditStatus  FROM FarmCrops fcp WHERE fcp.farm.farmer.samithi.id in (SELECT s.id FROM Agent a INNER JOIN a.wareHouses s WHERE a.id=:id) AND  fcp.revisionNo > :revisionNo   and  fcp.status=1  and  fcp.farm.status=1  AND fcp.cropSeason.code=:season AND fcp.farm.farmer.statusCode in  (:status1)  order by fcp.revisionNo DESC");
		query.setParameter("id", (Object) agentId);
		query.setParameter("revisionNo", (Object) revisionNo);
		query.setParameter("season", (Object) seasonCode);
		query.setParameterList("status1",
				new Object[] { ESETxnStatus.SUCCESS.ordinal(), ESETxnStatus.DELETED.ordinal() });
		query.setParameter("season", (Object) seasonCode);
		List result = query.list();
		return result;
	}

	@Override
	public void processCustomisedFormula(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap) {

		List<FarmerDynamicFieldsValue> resultSet = new ArrayList<>();
		List<DynamicFieldConfig> fmap = fieldConfigMap.values().stream()
				.filter(p -> (Arrays.asList("4").contains(p.getIsMobileAvail()) && p.getFormula() != null
						&& !StringUtil.isEmpty(p.getFormula())))
				.collect(Collectors.toList());
		if (fmap != null && !ObjectUtil.isListEmpty(fmap)) {
			Session session = this.getSessionFactory().openSession();
			fmap.sort((p1, p2) -> p1.getCode().compareTo(p2.getCode()));
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			fmap.stream().forEach(u -> {
				if(u.getReferenceId()!=null && u.getReferenceId()>0){
					u.setFormula(u.getFormula().replace("##REFID##", String.valueOf(farmerDynamicData.getId())));
					Query query = session.createSQLQuery(u.getFormula());
					List<Object[]> result = (List<Object[]>) query.list();
					if(result!=null ){
						result.stream().forEach(ff ->{
							FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
							farmerDynamicFieldsValue.setFieldName(u.getCode());
							farmerDynamicFieldsValue.setFieldValue(!ObjectUtil.isEmpty(ff[0].toString())?ff[0].toString():"");
							farmerDynamicFieldsValue.setTypez(!ObjectUtil.isEmpty(ff[1].toString())?Integer.parseInt(ff[1].toString()):0);
							farmerDynamicFieldsValue.setComponentType(u.getComponentType());
							farmerDynamicFieldsValue.setTxnType(farmerDynamicData.getTxnType());
							farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
							farmerDynamicFieldsValue.setCreatedDate(farmerDynamicData.getCreatedDate());
							farmerDynamicFieldsValue.setCreatedUser(farmerDynamicData.getCreatedUser());
							farmerDynamicFieldsValue.setTxnUniqueId(farmerDynamicData.getTxnUniqueId());
							farmerDynamicFieldsValue.setIsMobileAvail(u.getIsMobileAvail());
							farmerDynamicFieldsValue.setValidationType(u.getValidation());
							farmerDynamicFieldsValue
									.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
											? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");

							farmerDynamicFieldsValue
									.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
											? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);

							farmerDynamicFieldsValue.setListMethod(
									fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
											.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
													? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
													: "");
							farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
											? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
							farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
							session.saveOrUpdate(farmerDynamicFieldsValue);
						});
					}
					
				}else{
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				u.setFormula(u.getFormula().replace("##REFID##", String.valueOf(farmerDynamicData.getId())));
				Query query = session.createSQLQuery(u.getFormula());
				String result = (String) query.uniqueResult();
				farmerDynamicFieldsValue.setFieldName(u.getCode());
				farmerDynamicFieldsValue.setFieldValue(result);
				farmerDynamicFieldsValue.setComponentType(u.getComponentType());
				farmerDynamicFieldsValue.setTxnType(farmerDynamicData.getTxnType());
				farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
				farmerDynamicFieldsValue.setCreatedDate(farmerDynamicData.getCreatedDate());
				farmerDynamicFieldsValue.setCreatedUser(farmerDynamicData.getCreatedUser());
				farmerDynamicFieldsValue.setTxnUniqueId(farmerDynamicData.getTxnUniqueId());
				farmerDynamicFieldsValue.setIsMobileAvail(u.getIsMobileAvail());
				farmerDynamicFieldsValue.setValidationType(u.getValidation());
				farmerDynamicFieldsValue
						.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");

				farmerDynamicFieldsValue
						.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);

				farmerDynamicFieldsValue.setListMethod(
						fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
								.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");
				farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
				farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
				session.saveOrUpdate(farmerDynamicFieldsValue);
				}

			});

			session.flush();
			session.clear();
			session.close();
		}

	}

	@Override
	public List<Object[]> getFarmDetailsAndCultivationArea(String locationLevel1, String selectedBranch,
			String gramPanchayatEnable) {
		// TODO Auto-generated method stub
		String locationLevel = String.valueOf(locationLevel1.charAt(0));
		String flag = "enable";
		if (!locationLevel.equals("B")) {
			Session session = getSessionFactory().getCurrentSession();
			Query query;
			String hqlString = null;
			// if (!StringUtil.isEmpty(selectedBranch)) {

			switch (locationLevel) {
			case "C":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,s.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and ctry.code = :locationCode GROUP BY s.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,s.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and ctry.code = :locationCode GROUP BY s.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "S":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,ld.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and s.code = :locationCode GROUP BY ld.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,ld.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and s.code = :locationCode GROUP BY ld.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "D":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,c.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and ld.code = :locationCode GROUP BY c.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,c.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f inner join f.village v inner join v.city c inner join c.locality ld where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and ld.code = :locationCode GROUP BY c.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "M":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,gp.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f inner join f.village v inner join v.gramPanchayat gp inner join gp.city c where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and c.code = :locationCode GROUP BY gp.code ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,v.name AS NAME from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v inner join v.city c where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and c.code = :locationCode GROUP BY v.code ORDER BY NAME";
				}
				break;
			case "G":
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,v.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v inner join v.gramPanchayat gp where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and gp.code = :locationCode GROUP BY v.code ORDER BY TOTAL_LAND_HOLDING desc";
				}
				break;
			case "V":
				hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,f.firstName from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v  where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 and v.code = :locationCode GROUP BY f.id ORDER BY TOTAL_LAND_HOLDING desc";
				break;
			default:
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,ctry.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v inner join v.gramPanchayat gp inner join gp.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 GROUP BY ctry.name ORDER BY TOTAL_LAND_HOLDING desc";
				} else {
					hqlString = "select count(fa.id),sum(fc.cultiArea) as TOTAL_LAND_HOLDING,ctry.name from FarmCrops fc inner join fc.farm fa inner join fa.farmer f  inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.statusCode = 0 and f.status=1 and fc.status=1 and fa.status=1 GROUP BY ctry.name ORDER BY TOTAL_LAND_HOLDING desc";
				}
				flag = "disable";
				break;
			}

			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
			if (!locationLevel1.equalsIgnoreCase("first_level_Branch_Login") && flag.equalsIgnoreCase("enable")) {
				query.setParameter("locationCode", locationLevel1);
			}

			List<Object[]> result = query.list();
			return result;
		} else {
			Session session = getSessionFactory().openSession();
			String queryString = "select count(fa.id),sum(fc.CULTIVATION_AREA) as TOTAL_LAND_HOLDING,bm.name,bm.BRANCH_ID from farm_crops fc inner join farm fa  on fa.ID = fc.farm_id  inner join farmer f on f.ID = fa.FARMER_ID  inner join branch_master bm on bm.BRANCH_ID = f.BRANCH_ID where f.STATUS_CODE = 0 and f.status=1 and fa.status=1 and fc.status=1 GROUP BY bm.`NAME` ORDER BY TOTAL_LAND_HOLDING desc";
			SQLQuery query = session.createSQLQuery(queryString);
			List<Object[]> result = query.list();
			session.flush();
			session.close();
			return result;
		}
	}

	@Override
	public FarmCrops findFarmCropByCropIdAndFarmId(long farmId, long varietyId) {
		// TODO Auto-generated method stub
		Object[] values = { farmId, varietyId };
		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.status=1 and fc.farm.id = ? and fc.procurementVariety.id=?",
				values);
	}
	/*
	 * @Override public List<Object[]> listValuesbyQuery(String methodName) { //
	 * TODO Auto-generated method stub String queryString = null; queryString =
	 * methodName; Session session = getSessionFactory().openSession(); SQLQuery
	 * query = session.createSQLQuery(queryString); List<Object[]> result =
	 * query.list(); session.flush(); session.close(); return result;
	 * 
	 * }
	 */

	public List<Object[]> plottingAreaByBranch() {
		Session session = getSessionFactory().openSession();
		String queryString = "select ROUND(sum(CULTIVATION_AREA),3),BRANCH_ID,count(*) From vw_sowing_report v Where Crop_Category_No=0  GROUP BY BRANCH_ID ";
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> plottingByWarehouse(String selectedBranch) {
		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT ROUND(sum( CULTIVATION_AREA ),3),WAREHOUSE_NAME,WAREHOUSE_CODE,BRANCH_ID,count( * ) FROM vw_sowing_report v WHERE v.BRANCH_ID =:branch GROUP BY WAREHOUSE_CODE";
			query = session.createSQLQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT ROUND(sum( CULTIVATION_AREA ),3),WAREHOUSE_NAME,WAREHOUSE_CODE,BRANCH_ID,count( * ) FROM vw_sowing_report v GROUP BY WAREHOUSE_CODE";
			query = session.createSQLQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	public List<Object[]> plottingAreaByBranch(String selectedSeason) {
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT ROUND(sum(CULTIVATION_AREA),3), BRANCH_ID from vw_sowing_report where CROP_SEASON_CODE=:season AND Crop_Category_No=0 GROUP BY CROP_SEASON_CODE";
		SQLQuery query = session.createSQLQuery(queryString);
		query.setParameter("season", selectedSeason);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> plottingByMobileUser(String selectedBranch) {
		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "select ROUND(sum(CULTIVATION_AREA),3),AGENT_NAME,AGENT_ID,BRANCH_ID,count(*),WAREHOUSE_CODE From vw_sowing_report v WHERE v.BRANCH_ID =:branch AND Crop_Category_No=0 GROUP BY AGENT_ID";
			query = session.createSQLQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "select ROUND(sum(CULTIVATION_AREA),3),AGENT_NAME,AGENT_ID,BRANCH_ID,count(*),WAREHOUSE_CODE From vw_sowing_report v WHERE Crop_Category_No=0 GROUP BY AGENT_ID";
			query = session.createSQLQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}

	@Override
	public List<Object[]> trainingsByBranch() {
		/*
		 * Session session = getSessionFactory().openSession(); String
		 * queryString =
		 * "SELECT SUM(tt.count),bm.`NAME`,tt.BRANCH_ID,GROUP_concat(tt.count,'~',tt.wh,'@',tt.wareId) from (SELECT count( ts.id ) as count,w.name as wh,w.ID as wareId,ts.BRANCH_ID as BRANCH_ID FROM training_status ts INNER JOIN transfer_info ti ON ts.TRANSFER_INFO_ID = ti.id INNER JOIN prof p ON ti.AGENT_ID = p.PROF_ID INNER JOIN warehouse w ON p.WAREHOUSE_ID = w.ID GROUP BY ts.BRANCH_ID ,p.WAREHOUSE_ID ) tt inner join branch_master bm on bm.BRANCH_ID=tt.BRANCH_ID GROUP BY tt.BRANCH_ID"
		 * ; SQLQuery query = session.createSQLQuery(queryString);
		 * List<Object[]> result = query.list(); session.flush();
		 * session.close(); return result;
		 */

		Session session = getSessionFactory().openSession();
		String exeQuery = "SET  group_concat_max_len = 10000000000000000";
		String queryString = "SELECT GROUP_concat( tt.TRAINING_CODE), bm.`NAME`,tt.BRANCH_ID,GROUP_concat( tt.TRAINING_CODE, '~', tt.wh, '~', tt.wareId SEPARATOR '$') FROM	(SELECT	GROUP_CONCAT( ts.TRAINING_CODE ) AS TRAINING_CODE,	w.NAME AS wh,	w.ID AS wareId,	ts.BRANCH_ID AS BRANCH_ID FROM	training_status ts	INNER JOIN transfer_info ti ON ts.TRANSFER_INFO_ID = ti.id	INNER JOIN prof p ON ti.AGENT_ID = p.PROF_ID	INNER JOIN warehouse w ON p.WAREHOUSE_ID = w.ID GROUP BY	ts.BRANCH_ID,	p.WAREHOUSE_ID 	) tt	INNER JOIN branch_master bm ON bm.BRANCH_ID = tt.BRANCH_ID GROUP BY	tt.BRANCH_ID";
		SQLQuery exeQry = session.createSQLQuery(exeQuery);
		SQLQuery query = session.createSQLQuery(queryString);
		int i = exeQry.executeUpdate();
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;

	}

	@Override
	public List<Object[]> findAgentTrainingData(long warehouseId,String selectedFinYear) {
		String stDate = "2014-04-01";
		String enDate = DateUtil.getCurrentYear() + "-03-31";
		if (selectedFinYear != null && !StringUtil.isEmpty(selectedFinYear)) {
			stDate = selectedFinYear + "-04-01";
			int year = Integer.valueOf(selectedFinYear) + 1;
			enDate = year + "-03-31";
		}
		Date startDate = DateUtil.convertStringToDate(stDate, DateUtil.DATE);
		Date endDate = DateUtil.convertStringToDate(enDate, DateUtil.DATE);
		Session session = getSessionFactory().openSession();
		String exeQuery = "SET  group_concat_max_len = 10000000000000000";
		String queryString = "SELECT	p.PROF_ID,p.PROF_TYPE,CONCAT( pi.FIRST_NAME, ' ', pi.LAST_NAME ),count( ts.id ),CONCAT(p.PROF_ID,'#',GROUP_CONCAT(ts.ID,'~',ts.TRAINING_CODE,'~',ts.FARMER_ID,'~',(SELECT GROUP_CONCAT(tt.`CODE`,'-',tt.NAME SEPARATOR '@') FROM training_topic tt WHERE FIND_IN_SET(tt.id, ( SELECT GROUP_CONCAT( t.TRAINING_TOPIC_ID ) FROM farmer_training t WHERE FIND_IN_SET( t.CODE, ts.TRAINING_CODE ) ) ) ) SEPARATOR '$' ) ) FROM	training_status ts	INNER JOIN transfer_info ti ON ti.id = ts.TRANSFER_INFO_ID	INNER JOIN prof p ON p.PROF_ID = ti.AGENT_ID INNER JOIN pers_info pi ON pi.id = p.PERS_INFO_ID	INNER JOIN warehouse w ON w.id = p.WAREHOUSE_ID WHERE p.WAREHOUSE_ID = :warehouseId and CAST(ts.TRAINING_DATE as date) BETWEEN :startDate AND :endDate  GROUP BY ti.AGENT_ID";
		SQLQuery exeQry = session.createSQLQuery(exeQuery);
		SQLQuery query = session.createSQLQuery(queryString);
		query.setParameter("warehouseId", warehouseId);
		query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		int i = exeQry.executeUpdate();
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object[]> populateWarehouseMobileUserToFarmer_AgentChart(String branch, String season,String selectedFinYear) {
		String queryString = null;
		String sDate="2014-04-01";
		String eDate=DateUtil.getCurrentYear()+"-03-31";
		if(selectedFinYear!=null && !StringUtil.isEmpty(selectedFinYear)){
		 sDate=selectedFinYear+"-04-01";
		 int year=Integer.valueOf(selectedFinYear)+1;
		 eDate=year+"-03-31";
		}
		//criteria = criteria + "where " + dateProperty + " BETWEEN " + from + " AND " + to;
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(season)) {
			if (!StringUtil.isEmpty(branch)) {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE ) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.WAREHOUSE_CODE = w.`CODE` inner join farmer f on f.FARMER_ID=d.FARMER_ID  WHERE d.TXN_TYPE = '314' AND ( d.SERVICE_POINT_ID IS NULL OR d.SERVICE_POINT_ID = '') AND ( d.AGENT_ID IS NOT NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND d.branch_id=:branch AND CAST(d.TXN_TIME as date) BETWEEN :startDate AND :endDate and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			} else {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE ) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.WAREHOUSE_CODE = w.`CODE` inner join farmer f on f.FARMER_ID=d.FARMER_ID  WHERE d.TXN_TYPE = '314' AND ( d.SERVICE_POINT_ID IS NULL OR d.SERVICE_POINT_ID = '') AND ( d.AGENT_ID IS NOT NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND CAST(d.TXN_TIME as date) BETWEEN :startDate AND :endDate and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			}
		} else {
			if (!StringUtil.isEmpty(branch)) {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE ) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.WAREHOUSE_CODE = w.`CODE` inner join farmer f on f.FARMER_ID=d.FARMER_ID  WHERE d.TXN_TYPE = '314' AND ( d.SERVICE_POINT_ID IS NULL OR d.SERVICE_POINT_ID = '') AND ( d.AGENT_ID IS NOT NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND d.branch_id=:branch AND d.season_code =:season AND CAST(d.TXN_TIME as date) BETWEEN :startDate AND :endDate and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			} else {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.WAREHOUSE_CODE = w.`CODE` inner join farmer f on f.FARMER_ID=d.FARMER_ID  WHERE d.TXN_TYPE = '314' AND ( d.SERVICE_POINT_ID IS NULL OR d.SERVICE_POINT_ID = '') AND ( d.AGENT_ID IS NOT NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND d.season_code =:season AND CAST(d.TXN_TIME as date) BETWEEN :startDate AND :endDate and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			}
		}
		Query query = session.createSQLQuery(queryString);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(season)) {
			query.setParameter("season", season);
		}
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);
		List<Object[]> result = query.list();
		return result;
	}

	@Override
	public EventCalendar findEventByMessageNumber(String eventId) {
		// TODO Auto-generated method stub
		return (EventCalendar) find("FROM EventCalendar e  WHERE e.msgNo = ?", eventId);
	}

	@Override
	public List<EventCalendar> listEventByRevisionNo(Long revisionNo, String agentId) {
		// TODO Auto-generated method stub
		Object[] values = { revisionNo, agentId };
		return list(
				"FROM EventCalendar ec WHERE ec.revisionNo>? and ec.agentId=? and ec.status='0' ORDER BY ec.revisionNo DESC",
				values);
	}

	int i = 1;
	String methodNameQ = "";

	@Override
	public String getValueByQuery(String methodName, Object[] parameter, String branchId) {
		methodNameQ = methodName;
		Session session = getSessionFactory().openSession();
		if (branchId == null) {
			methodNameQ = methodName.replaceAll("<.*?>", "");

		} else {
			methodNameQ = methodNameQ.replaceAll("<", "").replaceAll(">", "").replaceAll("branchId",
					"'" + branchId + "'");
		}
		Query query = session.createSQLQuery(methodNameQ);
		if (parameter != null && parameter.length > 0) {
			i = 1;
			Arrays.asList(parameter).stream().forEach(u -> {
				if (methodNameQ.contains("param" + i)) {
					if (u != null && u.toString().contains(",")) {
						query.setParameterList("param" + i, Arrays.asList(u.toString().split(",")));
					} else {
						query.setParameter("param" + i, u);
					}
					i++;
				}
			});
		}
		List ls = query.list();
		session.flush();
		session.close();
		if (ls != null && ls.size() > 0) {
			return String.valueOf(ls.get(0));
		} else {
			return "";
		}

	}

	@Override
	public List<Object[]> listValuesbyQuery(String methodName, Object[] parameter, String branchId) {
		// TODO Auto-generated method stub
		String queryString = null;
		queryString = methodName;
		Session session = getSessionFactory().openSession();
		if (branchId == null) {
			queryString = queryString.replaceAll("<.*?>", "");

		} else {
			queryString = queryString.replaceAll("<", "").replaceAll(">", "").replaceAll("branchId",
					"'" + branchId + "'");
		}
		SQLQuery query = session.createSQLQuery(queryString);

		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;

	}

	@Override
	public DynamicFieldScoreMap findDynamicFieldScoreByFieldIdAndComponentCode(String id, String code) {
		Object[] values = { id, code };
		return (DynamicFieldScoreMap) find(
				"FROM DynamicFieldScoreMap dfsm  WHERE dfsm.dynamicMenuFieldMap.field.code=? AND dfsm.catalogueCode=? ",
				values);

	}

	@Override
	public List<Object[]> getValueListByQuery(String methodName, Object[] parameter, String branchId) {
		methodNameQ = methodName;
		Session session = getSessionFactory().openSession();
		if (branchId == null) {
			methodNameQ = methodName.replaceAll("<.*?>", "");

		} else {
			methodNameQ = methodNameQ.replaceAll("<", "").replaceAll(">", "").replaceAll("branchId",
					"'" + branchId + "'");
		}
		Query query = session.createSQLQuery(methodNameQ);
		if (parameter != null && parameter.length > 0) {
			i = 1;
			Arrays.asList(parameter).stream().forEach(u -> {
				if (methodNameQ.contains("param" + i)) {
					if (u != null && u.toString().contains(",")) {
						query.setParameterList("param" + i, Arrays.asList(u.toString().split(",")));
					}else {
						query.setParameter("param" + i, u);
					}
					i++;
				}
			});
		}
		List ls = query.list();
		session.flush();
		session.close();
		return ls;

	}

	@Override
	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(FarmCrops crops) {

		ProjectionList pList = Projections.projectionList();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FarmCrops.class);

		criteria.createAlias("farm", "fm");
		criteria.createAlias("fm.farmer", "f");
		// criteria.createAlias("fm.farmCrops", "fc");

		if (!StringUtil.isEmpty(crops.getCropCode())) {
			criteria.createAlias("procurementVariety", "pv");
			criteria.createAlias("pv.procurementProduct", "pp");
			criteria.add(Restrictions.eq("pp.id", Long.valueOf(crops.getCropCode())));

		}

		if (!ObjectUtil.isEmpty(crops.getFarm()) && !ObjectUtil.isEmpty(crops.getFarm().getFarmer())) {

			if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getVillage())) {
				criteria.createAlias("f.village", "v");
				criteria.add(Restrictions.eq("v.id", crops.getFarm().getFarmer().getVillage().getId()));
			}

			if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getCity())
					&& !ObjectUtil.isEmpty(crops.getFarm().getFarmer().getCity().getLocality())) {
				criteria.createAlias("f.city", "c");
				criteria.createAlias("c.locality", "l");
				criteria.createAlias("l.state", "s");

				if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getStateId())) {
					criteria.add(Restrictions.eq("s.id",
							crops.getFarm().getFarmer().getCity().getLocality().getState().getId()));
				}

				if (crops.getFarm().getFarmer().getCity().getLocality().getId() > 0) {
					criteria.add(Restrictions.eq("l.id", crops.getFarm().getFarmer().getCity().getLocality().getId()));
				}
			}
			if (crops.getFarm().getFarmer().getId() > 0) {
				criteria.add(Restrictions.eq("f.id", crops.getFarm().getFarmer().getId()));
			}

			if (crops.getFarm().getFarmer().getBranchId() != null
					&& !StringUtil.isEmpty(crops.getFarm().getFarmer().getBranchId())) {
				criteria.add(Restrictions.eq("f.branchId", crops.getFarm().getFarmer().getBranchId()));
			}

		}

		/*criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.isNotNull("longitude"));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "")));*/
		criteria.add(Restrictions.eq("f.status", 1));

		pList.add(Projections.property("id"));
		pList.add(Projections.property("latitude"));
		pList.add(Projections.property("longitude"));
		pList.add(Projections.property("fm.farmCode"));
		pList.add(Projections.property("f.firstName"));
		pList.add(Projections.property("fm.farmName"));
		pList.add(Projections.property("fm.id"));
		pList.add(Projections.property("f.id"));

		criteria.setProjection(pList);
		List<Object[]> list = criteria.list();
		return list;
	}

	public List<Object[]> listOfFarmersByVillageAndFarmCrops(long villageId,String plottingType) {
		String queryString =null;
		if (plottingType.equalsIgnoreCase("1")) {
		if (!StringUtil.isEmpty(villageId)) {
			queryString = "select fr.FARMER_ID,fr.FARMER_CODE,fr.FIRST_NAME,fr.LAST_NAME,fr.ID,fr.SUR_NAME from farm fa left Join farmer fr on fr.ID=fa.FARMER_ID  where fr.REF_ID is NULL and fr.STATUS_CODE='" + ESETxnStatus.SUCCESS.ordinal()+ "' and fr.VILLAGE_ID='"+ villageId + "' ";
		}else{
			queryString = "select fr.FARMER_ID,fr.FARMER_CODE,fr.FIRST_NAME,fr.LAST_NAME,fr.ID,fr.SUR_NAME from farm fa left Join farmer fr on fr.ID=fa.FARMER_ID  where fr.REF_ID is NULL and fr.STATUS_CODE='" + ESETxnStatus.SUCCESS.ordinal()+ "'";
		}
	   }else if(plottingType.equalsIgnoreCase("2")) {
			if (!StringUtil.isEmpty(villageId)) {
		    queryString = "select fr.FARMER_ID,fr.FARMER_CODE,fr.FIRST_NAME,fr.LAST_NAME,fr.ID,fr.SUR_NAME from farm_crops fcs left join farm fa on fa.ID=fcs.FARM_ID left Join farmer fr on fr.ID=fa.FARMER_ID  where fr.REF_ID is NULL and fr.STATUS_CODE='0' and fr.VILLAGE_ID='"+ villageId + "' ";
			}else{
			queryString = "select fr.FARMER_ID,fr.FARMER_CODE,fr.FIRST_NAME,fr.LAST_NAME,fr.ID,fr.SUR_NAME from farm_crops fcs left join farm fa on fa.ID=fcs.FARM_ID left Join farmer fr on fr.ID=fa.FARMER_ID  where fr.REF_ID is NULL and fr.STATUS_CODE='0'";
		}}else if(plottingType.equalsIgnoreCase("3")) {
			if (!StringUtil.isEmpty(villageId)) {
			queryString ="SELECT farmerId,farmerCode,firstName,lastName,id,surname FROM	(SELECT	fr.FARMER_ID AS farmerId,fr.FARMER_CODE AS farmerCode,	fr.FIRST_NAME AS firstName,fr.LAST_NAME AS lastName,fr.ID AS id,fr.SUR_NAME AS surname FROM	farm_crops fcs	LEFT JOIN farm fa ON fa.ID = fcs.FARM_ID	LEFT JOIN farmer fr ON fr.ID = fa.FARMER_ID WHERE fr.REF_ID IS NULL AND fr.STATUS_CODE = '0' AND fr.VILLAGE_ID = '"+ villageId + "' UNION SELECT	fr.FARMER_ID as farmerId,fr.FARMER_CODE as farmerCode,fr.FIRST_NAME as firstName,fr.LAST_NAME as lastName,fr.ID as id,fr.SUR_NAME as surname FROM farm fa	LEFT JOIN farmer fr ON fr.ID = fa.FARMER_ID WHERE fr.REF_ID IS NULL AND fr.STATUS_CODE = '0' AND fr.VILLAGE_ID ='"+ villageId + "') tt ";
		}else{
			queryString ="SELECT farmerId,farmerCode,firstName,lastName,id,surname FROM	(SELECT	fr.FARMER_ID AS farmerId,fr.FARMER_CODE AS farmerCode,	fr.FIRST_NAME AS firstName,fr.LAST_NAME AS lastName,fr.ID AS id,fr.SUR_NAME AS surname FROM	farm_crops fcs	LEFT JOIN farm fa ON fa.ID = fcs.FARM_ID	LEFT JOIN farmer fr ON fr.ID = fa.FARMER_ID WHERE fr.REF_ID IS NULL AND fr.STATUS_CODE = '0'  UNION SELECT fr.FARMER_ID as farmerId,fr.FARMER_CODE as farmerCode,fr.FIRST_NAME as firstName,fr.LAST_NAME as lastName,fr.ID as id,fr.SUR_NAME as surname FROM	farm fa	LEFT JOIN farmer fr ON fr.ID = fa.FARMER_ID WHERE fr.REF_ID IS NULL AND fr.STATUS_CODE = '0') tt ";
		}
		}
		
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			if (!StringUtil.isEmpty(selectedFarmer)) {
				hqlQuery = "select count(*) FROM Farmer f WHERE f.status=1 and f.statusCode=0";
			} else {

				hqlQuery = "select count(f.id) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f left join fa.farmICSConversion fics WHERE f.status=1 and f.statusCode=0 and fc.status=1 AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
				params.put("selectedCrop", Long.valueOf(selectedCrop));
			}

		} else {
			hqlQuery = "select count(*) FROM Farmer f WHERE f.status=1 and f.statusCode=0";
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}

		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}

		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id =:farmer";
			params.put("farmer", Long.valueOf(selectedFarmer));

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Integer val = 0;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).intValue();
		}
		return val;
	}

	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT  SUM(fc.cultiArea),sum(fc.actualSeedYield),sum(fc.estimatedYield) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fc.farm.farmer f left join fa.farmICSConversion fics WHERE  fc.status=1 and f.status=1";

		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}
		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}

		if (!StringUtil.isEmpty(selectedCrop) && selectedCrop != null) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));

		}

		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id =:farmer";
			params.put("farmer", Long.valueOf(selectedFarmer));

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = (Object[]) obj;
		}
		return val;

	}

	@Override
	public List<Object[]> listFarmerFarmCropInfoByVillageIdImg(String fType, Long farmCropId, Long farmId) {
		// TODO Auto-generated method stub
		if (!StringUtil.isEmpty(fType) && fType.equalsIgnoreCase("2")) {
			return list("SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,farm.farmName,farm.farmCode,"
					+ "farm.latitude,farm.longitude,farm.landmark,fc.cultiArea,fdi.proposedPlantingArea,"
					+ "f.village.name,img.image,f.seasonCode,f.branchId,f.createdDate,"
					+ "fc.procurementVariety.procurementProduct.name,fc.cultiArea,fc.estimatedHarvestDate,fc.sowingDate from Farmer f "
					+ " LEFT JOIN f.imageInfo.photo img INNER JOIN f.farms farm INNER JOIN farm.farmDetailedInfo fdi INNER JOIN farm.farmCrops fc"
					+ " where f.statusCode='" + ESETxnStatus.SUCCESS.ordinal()+"' and farm.id='" + farmId+"' and fc.id='" + farmCropId+"'");
		} else {
			return list("SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,farm.farmName,farm.farmCode,"
					+ "farm.latitude,farm.longitude,farm.landmark,fdi.totalLandHolding,fdi.proposedPlantingArea,"
					+ "f.village.name,img.image,f.seasonCode," + "f.branchId,f.createdDate from Farmer f "
					+ " LEFT JOIN f.imageInfo.photo img INNER JOIN f.farms farm INNER JOIN farm.farmDetailedInfo fdi"
					+ " where f.statusCode='" + ESETxnStatus.SUCCESS.ordinal() + "' and farm.id=?", farmId);

		}

	}

	int countSection = 0;

	@Override
	public void saveSectionBatch(List<DynamicSectionConfig> dscList) {

		Session session = getSessionFactory().openSession();
		dscList.stream().forEach(u -> {
			session.save(u);
			countSection++;
			if (countSection % 20 == 0) { // Same as the JDBC batch size
				// flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}

		});
		session.flush();
		session.clear();
		session.close();
	}

	public long findDynamicFieldsCountByCode(String code) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(DynamicFieldConfig.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.setProjection(Projections.rowCount());
		long count = (long) criteria.uniqueResult();
		session.flush();
		session.close();
		return count;
	}

	public long findDynamicSectionCountByCode(String code) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(DynamicSectionConfig.class);
		criteria.add(Restrictions.eq("sectionCode", code));
		criteria.setProjection(Projections.rowCount());
		long count = (long) criteria.uniqueResult();
		session.flush();
		session.close();
		return count;
	}

	public long findDynamicMenuCountByCode(String code) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(DynamicFeildMenuConfig.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.setProjection(Projections.rowCount());
		long count = (long) criteria.uniqueResult();
		session.flush();
		session.close();
		return count;
	}

	int countField = 0;

	public void saveFieldsBatch(List<DynamicFieldConfig> dfcList) {

		Session session = getSessionFactory().openSession();
		dfcList.stream().forEach(u -> {
			session.save(u);
			countField++;
			if (countField % 20 == 0) { // Same as the JDBC batch size
				// flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}

		});
		session.flush();
		session.clear();
		session.close();
	}

	public List<Object[]> listOfFarmersByVillageAndFarm(long villageId) {
		return list(
				"SELECT DISTINCT f.farmerId,f.farmerCode,f.firstName,f.lastName,f.id,f.surName from Farm fm  inner join fm.farmer f where f.village.id='"
						+ villageId + "' AND f.refId is null AND  f.statusCode='" + ESETxnStatus.SUCCESS.ordinal()
						+ "'");
	}

	@Override
	public List<DynamicReportFieldsConfig> listDynamicColumnsByEntity(String enities) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(DynamicReportFieldsConfig.class);
		criteria.add(Restrictions.in("id", StringUtil.convertStringList(Arrays.asList(enities.split(",")))));
		List list = criteria.list();
		session.flush();
		session.close();
		return list;
	}

	public List<Object[]> updateLotStatus(String selectedBales) {
		String queryString = "SELECT LOT_NO,PR_No,sum(BALE_WEIGHT ),count(Id) FROM bale_generation WHERE id IN ("
				+ selectedBales + ") GROUP BY BRANCH";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public String findQrCodeParameterByKey(String keyCode, String branchId) {
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery("Select qr.parameter from qr_code_link qr Where qr.key='" + keyCode
				+ "' AND qr.branch_id='" + branchId + "'");
		List list = query.list();
		String value = list.get(0).toString();
		session.flush();
		session.close();
		return value;
	}

	@Override
	public FarmerTraceability findTraceabilityDataById(Long id) {
		return (FarmerTraceability) find("FROM FarmerTraceability ft where ft.id=?", id);
	}

	@Override
	public Farm findFarmByID(Long id, String tenantId) {
		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query q = sessions.createQuery("FROM Farm fm left join fetch fm.farmer fr WHERE fm.id=:id");
		q.setParameter("id", id);
		Farm Farm = (Farm) q.uniqueResult();
		sessions.flush();
		sessions.close();
		return Farm;
	}

	@Override
	public List<Object[]> listFarmDetailsInfo() {

		return list("SELECT f.id,f.farmId,f.farmName from Farm f  where  f.status=1 ");
	}

	@Override
	public List<DataLevel> listDataLevel() {
		// TODO Auto-generated method stub
		return list("FROM DataLevel");
	}

	@Override
	public CropYield findCropYieldById(Long id) {
		// TODO Auto-generated method stub
		return (CropYield) find("FROM CropYield cy WHERE cy.id = ?", id);
	}

	@Override
	public List<CropYieldDetail> findCropYieldDetailById(Long id) {
		// TODO Auto-generated method stub
		return list("FROM CropYieldDetail cyd where cyd.cropYield.id=?", id);
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldByFdIds(List<Long> ids) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
	
		/*Query query = session.createQuery(
				"from FarmerDynamicFieldsValue fdv join fetch fdv.farmerDynamicData  left join fetch fdv.followUps fw where fdv.farmerDynamicData.id in (:ids) or fw.farmerDynamicData.id in (:ids)");*/
		
		Query query = session.createQuery(
				"from FarmerDynamicFieldsValue fdv join fetch fdv.farmerDynamicData  where fdv.farmerDynamicData.id in (:ids) ");
		query.setParameterList("ids", ids);
		List<FarmerDynamicFieldsValue> list = query.list();

		session.flush();
		session.close();

		return list;

	}

	@Override
	public void updateFDVS(List<FarmerDynamicFieldsValue> fdvs) {
		Session session = getSessionFactory().openSession();
		fdvs.stream().forEach(u -> {
			session.saveOrUpdate(u);
		});

		session.flush();
		session.close();
	}

	List<Object[]> result = new ArrayList<>();

	@Override
	public List<Object[]> listFDVSForFolloUp(String agentId,String revsionNo) {
		result = new ArrayList<>();
		Session session = getSessionFactory().openSession();
		SQLQuery query = session
				.createSQLQuery("select txn_type,entity from dynamic_menu_config dm where dm.is_score in (2,3)");
		List<Object[]> list = query.list();
		if (list != null && !ObjectUtil.isEmpty(list)) {
			list.stream().forEach(u -> {
				if (u[1].toString().equals("1")) {
					SQLQuery sql = session.createSQLQuery(
							"SELECT distinct fd.txn_Type,fd.id,fd.DATE,fd.CREATED_USER,fdv.FIELD_NAME,fdv.FIELD_VALUE,fdv.ACCESS_TYPE,fdv.CATALOGUE_TYPE,concat(f.first_name,' ',f.last_name),f.farmer_id,act.field_value  as act_value ,dead.field_value as dead,fdv.id AS DEALINE,fd.REVISION_NO,f.farmer_code AS code,w.name AS grp,v.name AS village    FROM `prof` p join agent_warehouse_map amp on p.id = amp.AGENT_ID  and p.PROF_ID='"+agentId+"' join farmer f on f.SAMITHI_ID = amp.WAREHOUSE_ID join warehouse w on w.id=f.samithi_id join village v on v.id = f.village_id join farmer_dynamic_data fd on fd.REFERENCE_ID = f.id AND (fd.ACT_STATUS=1) and fd.REVISION_NO >'"+ revsionNo+"' and TXN_TYPE ='"
									+ u[0].toString().trim()
									+ "' join farmer_dynamic_field_value fdv on fdv.FARMER_DYNAMIC_DATA_ID = fd.id   join farmer_dynamic_field_value act on fdv.action_plan = act.id  join farmer_dynamic_field_value dead on fdv.deadline = dead.id  order by fd.TXN_UNIQUE_ID DESC ");
					result.addAll(sql.list());
				} else if (u[1].toString().equals("2") || u[1].toString().equals("4")) {
					SQLQuery sql = session.createSQLQuery(
							"SELECT distinct fd.txn_Type,fd.id,fd.DATE,fd.CREATED_USER,fdv.FIELD_NAME,fdv.FIELD_VALUE,fdv.ACCESS_TYPE,fdv.CATALOGUE_TYPE,concat(f.first_name,'-',fm.farm_Name),fm.farm_Code,act.field_value  as act_value ,dead.field_value as dead,fdv.id AS DEALINE,fd.REVISION_NO,'',w.name AS grp,v.name AS village    FROM `prof` p join agent_warehouse_map amp on p.id = amp.AGENT_ID  and p.PROF_ID='"+agentId+"' join farmer f on f.SAMITHI_ID = amp.WAREHOUSE_ID join warehouse w on w.id=f.samithi_id join village v on v.id = f.village_id join farm fm on fm.FARMER_ID = f.id  join farmer_dynamic_data fd on fd.REFERENCE_ID = fm.id AND (fd.ACT_STATUS=1) and fd.REVISION_NO >'"+ revsionNo+"'  and TXN_TYPE ='"
									+ u[0].toString().trim()
									+ "' join farmer_dynamic_field_value fdv on fdv.FARMER_DYNAMIC_DATA_ID = fd.id   join farmer_dynamic_field_value act on fdv.action_plan = act.id  join farmer_dynamic_field_value dead on fdv.deadline = dead.id order by fd.TXN_UNIQUE_ID DESC ");
					result.addAll(sql.list());
				} 
			});
		}
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object[]> findFarmerByListOfFarmerId(List<String> farmerList) {
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("select f.farmerId,f.firstName,f.lastName from Farmer f where f.farmerId in (:ids)");
		query.setParameterList("ids", farmerList);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public String findComponentNameByDynamicFieldCode(String fieldCode) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT componentName from DynamicFieldConfig dfc  WHERE dfc.code=:componenCode");
		query.setParameter("componenCode", fieldCode);
		String val = "";
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = String.valueOf(obj);
		}
		return val;
	}
	
	
	public List<Object[]> listFarmerDynamicFieldsValuesByFarmIdList(List<String> farmIdList,List<String> selectedDynamicFieldCodeList) {

		//0 - fdfv.FIELD_NAME
		//1 - fdfv.FIELD_VALUE
		//2 - fdfv.COMPONENT_TYPE
		//3 - fdfv.TYPEZ
		//4 - fdfv.REFERENCE_ID
		//5 - fdfv.TXN_TYPE
		//6 - fdfv.FARMER_DYNAMIC_DATA_ID
		//7 - fdfv.ACCESS_TYPE
		//8 - fdfv.PARENT_ID
		//9 - fic.INSPECTION_DATE
		//10 - fic.INSPECTOR_NAME
		//11 - fic.INSPECTION_TYPE
		//12 - fic.TOTAL_LANDHOLD
		//13 - fic.LAND_ORGANIC
		
		/*String str = farmIdList.toString();
		String csv = str.substring(1, str.length() - 1).replace(", ", ",");
		
		String query = "SELECT ";
		query += " fdfv.FIELD_NAME,fdfv.FIELD_VALUE,fdfv.COMPONENT_TYPE,fdfv.TYPEZ,fdfv.REFERENCE_ID,fdfv.TXN_TYPE,fdfv.FARMER_DYNAMIC_DATA_ID,fdfv.ACCESS_TYPE,fdfv.PARENT_ID,fic.INSPECTION_DATE,fic.INSPECTOR_NAME,fic.INSPECTION_TYPE,fic.TOTAL_LANDHOLD,fic.LAND_ORGANIC ";
		query += " FROM farmer_dynamic_field_value fdfv ";
		query += " inner join farmer_dynamic_data fdd on fdd.ID = fdfv.FARMER_DYNAMIC_DATA_ID ";
		query += " inner join farm_ics_conversion fic on fic.ID = fdd.FARM_ICS_CONV_ID ";
		query += " WHERE ";
		if(selectedDynamicFieldCodeList != null && !selectedDynamicFieldCodeList.isEmpty()){
			query += "  fdfv.FIELD_NAME in ( :selectedDynFields )  and ";
		}
		query += " fdfv.REFERENCE_ID IN ( :referenceId ) ";
		query += " AND fdfv.FARMER_DYNAMIC_DATA_ID IN ( SELECT Max( fdfv2.FARMER_DYNAMIC_DATA_ID ) FROM farmer_dynamic_field_value fdfv2 WHERE fdfv2.REFERENCE_ID IN ( :referenceId ) GROUP BY fdfv2.REFERENCE_ID ORDER BY instr( :referenceIdStr ,fdfv2.REFERENCE_ID) ) ";
		
		query += " ORDER BY instr( :referenceIdStr ,fdfv.REFERENCE_ID) ";
		
		if(selectedDynamicFieldCodeList != null && !selectedDynamicFieldCodeList.isEmpty()){
		query += " , instr( :selectedDynFieldsStr ,fdfv.FIELD_NAME) ";
		}
		sqlQuery.setParameterList("referenceId", farmIdList);
		sqlQuery.setParameter("referenceIdStr", csv);
		
		*/
		
		String query = "SELECT ";
		query +=  " fdfv.FIELD_NAME,fdfv.FIELD_VALUE,fdfv.COMPONENT_TYPE,fdfv.TYPEZ,fdfv.REFERENCE_ID,fdfv.TXN_TYPE,fdfv.FARMER_DYNAMIC_DATA_ID,fdfv.ACCESS_TYPE,fdfv.PARENT_ID,fic.INSPECTION_DATE,fic.INSPECTOR_NAME,fic.INSPECTION_TYPE,fic.TOTAL_LANDHOLD,fic.LAND_ORGANIC  ";
		query +=  " FROM farmer_dynamic_field_value fdfv ";
		query +=  " INNER JOIN farmer_dynamic_data fdd ON fdd.ID = fdfv.FARMER_DYNAMIC_DATA_ID ";
		query +=  " AND fdfv.FIELD_NAME IN ( :selectedDynFields ) ";
		query +=  " AND fdd.ENTITY_ID = 4 ";
		query +=  " INNER JOIN farm_ics_conversion fic ON fic.ID = fdd.FARM_ICS_CONV_ID ";
		query +=  " JOIN ( SELECT df.REFERENCE_ID,max( FARMER_DYNAMIC_DATA_ID ) AS ffdis  ";
		query +=  " FROM farmer_dynamic_field_value df";
		query +=  " JOIN farmer_dynamic_data fdd ON fdd.id = df.FARMER_DYNAMIC_DATA_ID ";
		query +=  " AND fdd.ENTITY_ID = 4";
		query +=  " GROUP BY df.REFERENCE_ID";
		query +=  " ) x ON x.REFERENCE_ID = fdfv.REFERENCE_ID";
		query +=  " AND fdd.id = ffdis";
		query +=  " ORDER BY instr( :selectedDynFieldsStr , fdfv.FIELD_NAME )";
				
		
		
		Session session = getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		
		if(selectedDynamicFieldCodeList != null && !selectedDynamicFieldCodeList.isEmpty()){
			sqlQuery.setParameterList("selectedDynFields", selectedDynamicFieldCodeList);
			String selectedFields_str = selectedDynamicFieldCodeList.toString();
			String selectedFields_csv = selectedFields_str.substring(1, selectedFields_str.length() - 1).replace(", ", ",");
			sqlQuery.setParameter("selectedDynFieldsStr", selectedFields_csv);
		}
		
		List<Object[]> list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
	}
	
	@Override
	public Farmer findFarmerByMSISDN(String msisdn) {
		Object[] values = { msisdn, ESETxnStatus.SUCCESS.ordinal() };
		Farmer farmer = (Farmer) find("FROM Farmer fr WHERE  fr.mobileNumber = ? AND fr.statusCode = ?", values);
		return farmer;
	}

	public List<Object[]> listDynamicFieldsCodeAndName() {

		String query = " select dfc.COMPONENT_CODE,dfc.COMPONENT_NAME from dynamic_fields_config dfc ";
		Session session = getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		List<Object[]> list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType(int i) {
		return list(
				"select distinct dfm FROM DynamicMenuFieldMap dfm join fetch dfm.field ff  left join fetch ff.languagePreferences   left join fetch ff.dynamicSectionConfig ffs  where dfm.menu.isScore =? and ff.followUp in (3,4) ",
				i);
	}
	
	@Override
	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType() {
		return list("select distinct dfm FROM DynamicMenuFieldMap dfm join fetch dfm.field ff  left join fetch ff.languagePreferences   left join fetch ff.dynamicSectionConfig ffs  where dfm.menu.isScore in (2,3) and ff.followUp in (3,4) order by dfm.menu.mTxnType,dfm.order ");
	}
	
	@Override
	public List<Object[]> listFarmerCodeIdNameByStateCode(String stateCode) {

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(
				"SELECT f.farmerId,f.farmerCode,f.firstName,f.lastName,f.id,f.surName FROM Farmer f where f.city.locality.state.code=:stateCode AND f.status=:status AND f.statusCode=:statusCode");
		query.setParameter("stateCode", stateCode);
		query.setParameter("status", Farmer.Status.ACTIVE.ordinal());
		query.setParameter("statusCode", ESETxnStatus.SUCCESS.ordinal());
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo, String branchId,String tenantId) {
		
		//Session session = getHibernateTemplate().getSessionFactory().openSession();
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		//Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select distinct dsc FROM DynamicFeildMenuConfig dsc  join fetch dsc.dynamicSectionConfigs dsm  join fetch dsc.dynamicFieldConfigs dfm  join fetch dsm.section ss  join fetch dfm.field ff  left join fetch ff.languagePreferences left join fetch ss.languagePreferences  left join fetch ff.dynamicSectionConfig ffs left join fetch dfm.dynamicFieldScoreMap  where dsc.revisionNo > :revNo  and (dsc.branchId is null or dsc.branchId =''  or dsc.branchId like :branchId) and  (dfm.branchId is null or dfm.branchId ='' or dfm.branchId like :branchId) and  (dsm.branchId is null or dsm.branchId ='' or dsm.branchId like :branchId)  order by dsc.revisionNo desc ");
		query.setParameter("branchId", "%"+branchId+"%");
		query.setParameter("revNo", Long.valueOf(revisionNo));
	
		List<DynamicFeildMenuConfig> result = query.list();
		session.flush();
		session.close();
		return result;
		
		
	}
	
	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String txnTypez, String branchId){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery("select distinct dsc FROM DynamicFeildMenuConfig dsc join fetch dsc.dynamicSectionConfigs dss left join fetch dss.section ds left join fetch ds.languagePreferences join fetch dsc.dynamicFieldConfigs  dc  join fetch dc.field ff   left join fetch ff.languagePreferences join fetch ff.dynamicSectionConfig sec join fetch dc.field WHERE dsc.txnType = :txnType and (dsc.branchId is null or dsc.branchId =''  or dsc.branchId like :branchId) and  (dc.branchId is null or dc.branchId ='' or dc.branchId like :branchId) and  (dss.branchId is null or dss.branchId ='' or dss.branchId like :branchId) ORDER BY dsc.order ASC");
		query.setParameter("branchId", "%"+branchId+"%");
		query.setParameter("txnType",txnTypez);
	
		List<DynamicFeildMenuConfig> result = query.list();
		session.flush();
		session.close();
		return result;	
	}
	
	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByTypeForOCP(String txnTypez, String branchId) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String queryString1 = "select distinct dsc FROM DynamicFeildMenuConfig dsc join fetch dsc.dynamicSectionConfigs dss left join fetch dss.section ds left join fetch ds.languagePreferences join fetch dsc.dynamicFieldConfigs  dc  join fetch dc.field ff   left join fetch ff.languagePreferences join fetch ff.dynamicSectionConfig sec join fetch dc.field WHERE dsc.txnType = :txnType and (dsc.branchId is null or dsc.branchId =''  or dsc.branchId like :branchId) and  (dc.branchId is null or dc.branchId ='' or dc.branchId like :branchId) and  (dss.branchId is null or dss.branchId ='' or dss.branchId like :branchId) ORDER BY dsc.order ASC";
		String queryString2 = "select distinct dsc FROM DynamicFeildMenuConfig dsc join fetch dsc.dynamicSectionConfigs dss left join fetch dss.section ds left join fetch ds.languagePreferences join fetch dsc.dynamicFieldConfigs  dc  join fetch dc.field ff   left join fetch ff.languagePreferences join fetch ff.dynamicSectionConfig sec join fetch dc.field WHERE dsc.txnType = :txnType ORDER BY dsc.order ASC";
		Query query = null;

		if (!StringUtil.isEmpty(branchId)) {
			query = session.createQuery(queryString1);
			query.setParameter("branchId", "%" + branchId + "%");
		} else {
			query = session.createQuery(queryString2);
		}
		query.setParameter("txnType", txnTypez);

		List<DynamicFeildMenuConfig> result = query.list();
		session.flush();
		session.close();
		return result;
	}
	
	public List<Object[]> listValueByFieldName(String field,String branchId) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(FarmerDynamicFieldsValue.class);
		criteria.createAlias("farmerDynamicData", "fd",JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("fd.branch", branchId));
		criteria.add(Restrictions.eq("fieldName", field));
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("id"));
		pList.add(Projections.property("fieldValue"));
		pList.add(Projections.property("farmerDynamicData.id"));
		criteria.setProjection(pList);
		List<Object[]> list = criteria.list();
		session.flush();
		session.close();
		return list;
	}
	
	public FarmCrops findFarmCropFullObjectById(String id) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(FarmCrops.class);
		criteria.add(Restrictions.eq("id", Long.valueOf(id)));
		criteria.createAlias("farm", "fa",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fa.farmer", "f",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("f.samithi", "w",JoinType.LEFT_OUTER_JOIN);
		FarmCrops fc = (FarmCrops) criteria.uniqueResult();
		session.flush();
		session.close();
		return fc;
	}
	
	public FarmerDynamicFieldsValue findFarmerDynamicFieldsValueById(String id) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(FarmerDynamicFieldsValue.class);
		criteria.add(Restrictions.eq("id", Long.valueOf(id)));
		FarmerDynamicFieldsValue fdfv = (FarmerDynamicFieldsValue) criteria.uniqueResult();
		session.flush();
		session.close();
		return fdfv;
	}

	@Override
	public FarmerDynamicData processCustomisedFormula(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap, Map<String, List<FarmerDynamicFieldsValue>> fdMap) {

		List<FarmerDynamicFieldsValue> resultSet = new ArrayList<>();
		List<DynamicFieldConfig> fmap = fieldConfigMap.values().stream()
				.filter(p -> (Arrays.asList("4").contains(p.getIsMobileAvail()) && p.getFormula() != null
						&& !StringUtil.isEmpty(p.getFormula())))
				.collect(Collectors.toList());
		if (fmap != null && !ObjectUtil.isListEmpty(fmap)) {
			Session session = this.getSessionFactory().openSession();
			fmap.sort((p1, p2) -> p1.getCode().compareTo(p2.getCode()));
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			
			
			fmap.stream().forEach(u -> {
				if(u.getReferenceId()!=null && u.getReferenceId()>0){
					u.setFormula(u.getFormula().replace("##REFID##", String.valueOf(farmerDynamicData.getId())));
					u.setFormula(u.getFormula().replace("##REFERID##", String.valueOf(farmerDynamicData.getReferenceId())));
					u.setFormula(u.getFormula().replace("##SEASON##", String.valueOf(farmerDynamicData.getSeason())));		
					
					List<String> fieldLiust = new ArrayList<>();
					Pattern p = Pattern.compile("\\{([^}]*)\\}");
					Matcher m = p.matcher(u.getFormula());
					while (m.find())
						fieldLiust.add(m.group(1));
					
					fieldLiust.stream().filter(ff -> fieldConfigMap.containsKey(ff)).forEach(g -> {
						if (fieldConfigMap.get(g).getValidation() != null
								&& fieldConfigMap.get(g).getValidation().equals("4")) {
							Double String = fdMap.get(g) != null ? fdMap.get(g).stream()
									.map(e -> Double.valueOf(e.getFieldValue())).reduce(0.00, (a, b) -> a + b) : 0.0;
									u.setFormula(u.getFormula().replaceAll("\\{" + g + "\\}",
									fdMap.containsKey(g) ? String.toString() : "0"));
						} else if (fieldConfigMap.get(g).getValidation() != null
								&& fieldConfigMap.get(g).getValidation().equals("2")) {
							Integer String = fdMap.get(g) != null ? fdMap.get(g).stream()
									.map(e -> Integer.valueOf(e.getFieldValue())).reduce(0, (a, b) -> a + b) : 0;
									u.setFormula(u.getFormula().replaceAll("\\{" + g + "\\}",
									fdMap.containsKey(g) ? String.toString() : "0"));
						} else {
							u.setFormula(u.getFormula().replaceAll("\\{" + g + "\\}", fdMap.containsKey(g) ?'"'+fdMap.get(g).get(0).getFieldValue()+'"' : "0"));
						}

					});
					
					Query query = session.createSQLQuery(u.getFormula());
					List<Object[]> result = (List<Object[]>) query.list();
					if(result!=null ){
						result.stream().forEach(ff ->{
							FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
							farmerDynamicFieldsValue.setFieldName(u.getCode());
							farmerDynamicFieldsValue.setFieldValue(!ObjectUtil.isEmpty(ff[0].toString())?ff[0].toString():"");
							farmerDynamicFieldsValue.setTypez(!ObjectUtil.isEmpty(ff[1].toString())?Integer.parseInt(ff[1].toString()):0);
							farmerDynamicFieldsValue.setComponentType(u.getComponentType());
							farmerDynamicFieldsValue.setTxnType(farmerDynamicData.getTxnType());
							farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
							farmerDynamicFieldsValue.setCreatedDate(farmerDynamicData.getCreatedDate());
							farmerDynamicFieldsValue.setCreatedUser(farmerDynamicData.getCreatedUser());
							farmerDynamicFieldsValue.setTxnUniqueId(farmerDynamicData.getTxnUniqueId());
							farmerDynamicFieldsValue.setIsMobileAvail(u.getIsMobileAvail());
							farmerDynamicFieldsValue.setValidationType(u.getValidation());
							farmerDynamicFieldsValue
									.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
											? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");

							farmerDynamicFieldsValue
									.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
											? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);

							farmerDynamicFieldsValue.setListMethod(
									fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
											.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
													? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
													: "");
							farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
									&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
											? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
							farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
							session.saveOrUpdate(farmerDynamicFieldsValue);
						});
					}
					
				}else{
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				u.setFormula(u.getFormula().replace("##REFID##", String.valueOf(farmerDynamicData.getId())));
				u.setFormula(u.getFormula().replace("##REFERID##", String.valueOf(farmerDynamicData.getReferenceId())));
				u.setFormula(u.getFormula().replace("##SEASON##", String.valueOf(farmerDynamicData.getSeason())));	
				u.setFormula(u.getFormula().replace("##BRANCH##", String.valueOf(farmerDynamicData.getBranch())));	
				List<String> fieldLiust = new ArrayList<>();
				Pattern p = Pattern.compile("\\{([^}]*)\\}");
				Matcher m = p.matcher(u.getFormula());
				while (m.find())
					fieldLiust.add(m.group(1));
				
				fieldLiust.stream().filter(ff -> fieldConfigMap.containsKey(ff)).forEach(g -> {
					if (fieldConfigMap.get(g).getValidation() != null
							&& fieldConfigMap.get(g).getValidation().equals("4")) {
						Double String = fdMap.get(g) != null ? fdMap.get(g).stream()
								.map(e -> Double.valueOf(e.getFieldValue())).reduce(0.00, (a, b) -> a + b) : 0.0;
								u.setFormula(u.getFormula().replaceAll("\\{" + g + "\\}",
								fdMap.containsKey(g) ? String.toString() : "0"));
					} else if (fieldConfigMap.get(g).getValidation() != null
							&& fieldConfigMap.get(g).getValidation().equals("2")) {
						Integer String = fdMap.get(g) != null ? fdMap.get(g).stream()
								.map(e -> Integer.valueOf(e.getFieldValue())).reduce(0, (a, b) -> a + b) : 0;
								u.setFormula(u.getFormula().replaceAll("\\{" + g + "\\}",
								fdMap.containsKey(g) ? String.toString() : "0"));
					} else {
						u.setFormula(u.getFormula().replaceAll("\\{" + g + "\\}", fdMap.containsKey(g) ?'"'+fdMap.get(g).get(0).getFieldValue()+'"' : "0"));
					}

				});
				
				Query query = session.createSQLQuery(u.getFormula());
				String result = (String) query.uniqueResult();
				farmerDynamicFieldsValue.setFieldName(u.getCode());
					farmerDynamicFieldsValue.setFieldValue(result);	
				farmerDynamicFieldsValue.setComponentType(u.getComponentType());
				farmerDynamicFieldsValue.setTxnType(farmerDynamicData.getTxnType());
				farmerDynamicFieldsValue.setReferenceId(farmerDynamicData.getReferenceId());
				farmerDynamicFieldsValue.setCreatedDate(farmerDynamicData.getCreatedDate());
				farmerDynamicFieldsValue.setCreatedUser(farmerDynamicData.getCreatedUser());
				farmerDynamicFieldsValue.setTxnUniqueId(farmerDynamicData.getTxnUniqueId());
				farmerDynamicFieldsValue.setIsMobileAvail(u.getIsMobileAvail());
				farmerDynamicFieldsValue.setValidationType(u.getValidation());
				farmerDynamicFieldsValue
						.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");

				farmerDynamicFieldsValue
						.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);

				farmerDynamicFieldsValue.setListMethod(
						fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null && fieldConfigMap
								.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
										: "");
				farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
				farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
				
				farmerDynamicFieldsValue.setIsUpdateProfile(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsUpdateProfile()
						: "0");
				LinkedHashMap<String, DynamicFieldConfig> newMap = new LinkedHashMap<> (fieldConfigMap);
				Map<String, Object> profileUpdateFields = new HashMap<>();
				profileUpdateFields = new HashMap<>();
				profileUpdateFields.put(farmerDynamicFieldsValue.getFieldName(),
						farmerDynamicFieldsValue.getFieldValue());
				farmerDynamicFieldsValue.getFarmerDynamicData().setProfileUpdateFields(profileUpdateFields);
			/*	if (farmerDynamicData.getProfileUpdateFields() != null && !ObjectUtil.isEmpty(farmerDynamicData.getProfileUpdateFields())) {
					processProfileUpdates(farmerDynamicData.getProfileUpdateFields(), fieldConfigMap, farmerDynamicData);
				}*/
				session.saveOrUpdate(farmerDynamicFieldsValue);
				farmerService.processProfileUpdates(farmerDynamicFieldsValue.getFarmerDynamicData().getProfileUpdateFields(), fieldConfigMap, farmerDynamicData);
				farmerDynamicData.getFarmerDynamicFieldsValues().add(farmerDynamicFieldsValue);
				
				//farmerService.saveOrUpdate(farmerDynamicData,fdMap,newMap);
				}

			});
			session.flush();
			session.clear();
			session.close();
			
		}
		return farmerDynamicData;

	}
	
	public ColdStorage findColdStorageById(String id) {

		return (ColdStorage) find("FROM ColdStorage cs LEFT JOIN FETCH cs.farmer WHERE cs.id  = ?", Long.valueOf(id));
	}

	@Override
	public ColdStorageDetail findColdStorageDetailById(Long valueOf) {
		// TODO Auto-generated method stub
		return (ColdStorageDetail) find("FROM ColdStorageDetail cs LEFT JOIN FETCH cs.coldStorage c LEFT JOIN FETCH c.farmer WHERE cs.id  = ?", Long.valueOf(valueOf));
	}
	
	@Override
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm,String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers) {

		ProjectionList pList = Projections.projectionList();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Farm.class);
		criteria.createAlias("farmer", "f");

		pList.add(Projections.property("f.id"));
		pList.add(Projections.property("latitude"));
		pList.add(Projections.property("longitude"));
		pList.add(Projections.property("farmCode"));
		pList.add(Projections.property("f.firstName"));
		pList.add(Projections.property("farmName"));
		pList.add(Projections.property("id"));
		pList.add(Projections.property("f.isCertifiedFarmer"));
		//pList.add(Projections.property("fic.organicStatus"));
		 iscList = new HashMap<>();
	
		if (!StringUtil.isEmpty(farm.getCropCode())) {
			criteria.createAlias("farmCrops", "fc");

			criteria.createAlias("fc.procurementVariety", "pv");
			criteria.createAlias("pv.procurementProduct", "pp");
			criteria.add(Restrictions.eq("pp.id", Long.valueOf(farm.getCropCode())));

		}

		if (!ObjectUtil.isEmpty(farm.getFarmer())) {

			if (!ObjectUtil.isEmpty(farm.getFarmer().getVillage())) {
				criteria.createAlias("f.village", "v");
				criteria.add(Restrictions.eq("v.id", farm.getFarmer().getVillage().getId()));
			}
			
			

			if (!ObjectUtil.isEmpty(farm.getFarmer().getCity())
					&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality())) {
				criteria.createAlias("f.city", "c");
				criteria.createAlias("c.locality", "l");
				criteria.createAlias("l.state", "s");

				if (!ObjectUtil.isEmpty(farm.getFarmer().getStateId())) {
					criteria.add(Restrictions.eq("s.id", farm.getFarmer().getCity().getLocality().getState().getId()));
				}

				if (farm.getFarmer().getCity().getLocality().getId() > 0) {
					criteria.add(Restrictions.eq("l.id", farm.getFarmer().getCity().getLocality().getId()));
				}
			}
			if (!ObjectUtil.isEmpty(farm.getFarmer().getIsCertifiedFarmer())
					&& !(farm.getFarmer().getIsCertifiedFarmer() < 0)) {
				if (selectedOrganicStatus.equalsIgnoreCase("Conventional")) {
					criteria.add(Restrictions.eq("f.isCertifiedFarmer", 0));
				}else{
				criteria.add(Restrictions.eq("f.isCertifiedFarmer", farm.getFarmer().getIsCertifiedFarmer()));
				}
			}
			if (!StringUtil.isEmpty(farm.getFarmer().getSeasonCode())) {
				criteria.add(Restrictions.eq("f.seasonCode", farm.getFarmer().getSeasonCode()));
			}
			if (farm.getFarmer().getBranchId() != null && !StringUtil.isEmpty(farm.getFarmer().getBranchId())) {
				criteria.add(Restrictions.eq("f.branchId", farm.getFarmer().getBranchId()));
			}
			/* querying with left join ics conversion takes time so when filtering used join when no ics filter used takes seperate list for farm and organic status map and set to the object*/
			if (!ObjectUtil.isEmpty(farm.getFarmICSConversion())) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
				Stream<Object[]>  icsMap= list("select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",new Object[]{farm.getFarmICSConversion().iterator().next().getOrganicStatus()}).stream();
		     	 iscList = icsMap.collect(Collectors.toMap(u ->(Long) u[0], u -> u[1].toString()));
		     	criteria.add(Restrictions.in("id", iscList.keySet()));
				}
				else{
					Stream<Object[]>  icsMap= list("select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0 ,1 ,2)").stream();
			     	 iscList = icsMap.collect(Collectors.toMap(u ->(Long) u[0], u -> u[1].toString()));
			     	criteria.add(Restrictions.in("id", iscList.keySet()));
				}
			}else{
				
					Stream<Object[]>  icsMap= list("select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where fi.farm is not null and isActive=1").stream();
					 iscList = icsMap.collect(Collectors.toMap(u ->(Long) u[0], u -> u[1].toString()));
				
			   /*  
				String queryString = "select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where fi.farm is not null and isActive=1";
				Session ses = getSessionFactory().openSession();
				Query query = ses.createQuery(queryString);
				List<Long> results = ((List<Object[]>) query.list()).stream().map(result ->(Long) result[0]).collect(Collectors.toList());
		     	//criteria.add(Restrictions.in("id", results));
*/			}
		}
		if (!selectedFarmer.equalsIgnoreCase("") && !StringUtil.isEmpty(selectedFarmer)){
		if (!ObjectUtil.isEmpty(farm.getFarmer().getId())) {
			criteria.add(Restrictions.eq("f.id",  Long.valueOf(selectedFarmer)));
		}
}	
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.isNotNull("longitude"));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "")));
		criteria.add(Restrictions.eq("f.status", 1));
		criteria.add(Restrictions.eq("status", 1));
		
		if(yieldEstimationDoneFarmers.size() >= 1){
			criteria.add(Restrictions.in("id", yieldEstimationDoneFarmers));
		}
		
     	criteria.setProjection(pList);
		List<Object[]> list = criteria.list();
		List<Object[]> output = list.stream().map(a-> {
			 a  = Arrays.copyOf(a, a.length + 1);
			 
			    a[a.length - 1] = iscList.containsKey(Long.valueOf(a[6].toString())) ? iscList.get(Long.valueOf(a[6].toString())) : "0";
			    return a;
			   
         }).collect(Collectors.toList());
		
		list.clear();
		return output;
	}
	
	public FarmerDynamicData findFarmerDynamicDataByReferenceIdAndTxnType(long referenceId) {

		return (FarmerDynamicData) find("FROM FarmerDynamicData fdfv where fdfv.referenceId=? and fdfv.txnType=2001 order by fdfv.id desc",
				String.valueOf(referenceId));
	}
	
	public List<Object[]> listfarmerDynamicData(List<Long> fidLi) {
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery("SELECT fd.REFERENCE_ID,DATE(max( fd.DATE )),(SELECT fddv.field_value FROM farmer_dynamic_field_value fdv JOIN farmer_dynamic_field_value fddv ON fdv.deadline = fddv.id  where fdv.FARMER_DYNAMIC_DATA_ID = max( fd.id ) LIMIT 1) FROM farmer_dynamic_data fd  WHERE fd.REFERENCE_ID IN ( :ids)  AND fd.TXN_TYPE = 2001 GROUP BY fd.REFERENCE_ID");
		
		//SQLQuery sqlQuery = session.createSQLQuery(query);
		query.setParameterList("ids", fidLi);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}
	
	
	@Override
	public FarmCrops findFarmCropByCropIdAndFarmIdAndSeason(long farmId, long varietyId, long seasonId) {
		// TODO Auto-generated method stub
		Object[] values = { farmId, varietyId, seasonId };
		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.status=1 and fc.farm.id = ? and fc.procurementVariety.id=? and fc.cropSeason.id=?",
				values);
	}
	
	
	@Override
	public List<Object> listFarmerCountByGroupTraderAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedGender, String selectedCooperative, String selectedCrop, String typez,
			String selectedVillage) {
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();

		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery = "select count(f.id) as count,f.masterData as group,f.village.name as village FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1 AND fc.procurementVariety.procurementProduct.code=:selectedCrop and fc.status=1 and f.masterData!=null and f.masterData!='' ";
			params.put("selectedCrop", selectedCrop);
		} else {
			hqlQuery = "select count(*) as count,f.masterData as group,f.village.name as village from Farmer f where f.status =1 and f.masterData!=null and f.masterData!=''";
		}
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState)) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}
		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		if (!StringUtil.isEmpty(typez)) {
			hqlQuery += " AND f.typez=:typez";
			params.put("typez", typez);
		}
		if (!StringUtil.isEmpty(selectedVillage)) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));
		}

		Query query = session.createQuery(hqlQuery + "  GROUP BY f.masterData order by count(*) desc");
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();
	}
	
	@Override
	public List<Object> listFarmerCountByFarmInspection(Date sDate, Date eDate){
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		hqlQuery = "select count(p.farmId)as count,p.createdUserName as group From PeriodicInspection p WHERE p.inspectionDate between :sDate AND :eDate  ";
		Query query = session.createQuery(hqlQuery + "  GROUP BY p.createdUserName order by count(*) desc");
		query.setParameter("sDate", sDate).setParameter("eDate", eDate);
		return query.list();
	}
	
	public FarmerDynamicFieldsValue findLotCodeFromFarmerDynamicFieldsValue(Long id,String fieldName){
		Object[] values = { id, fieldName };
		return (FarmerDynamicFieldsValue) find("FROM FarmerDynamicFieldsValue fdfv WHERE fdfv.farmerDynamicData.id  = ? AND  fdfv.fieldName= ?", values);
	}
	
	@Override
	public List<Object[]> populateTrainingChart(String branch, String selectedFinYear) {
		// TODO Auto-generated method stub
		String queryString = null;
		String sDate = "2014-04-01 00:00:01";
		String eDate = DateUtil.getCurrentYear() + "-03-31 23:59:59";
		if (selectedFinYear != null && !StringUtil.isEmpty(selectedFinYear)) {
			sDate = selectedFinYear + "-04-01 00:00:01";
			int year = Integer.valueOf(selectedFinYear) + 1;
			eDate = year + "-03-31 23:59:59";
		}
		
		Session session = getSessionFactory().getCurrentSession();
		if (!StringUtil.isEmpty(branch)) {
			queryString= "SELECT GROUP_concat( tt.TRAINING_CODE), bm.`NAME`,tt.BRANCH_ID,GROUP_concat( tt.TRAINING_CODE, '~', tt.wh, '~', tt.wareId SEPARATOR '$') FROM	(SELECT	GROUP_CONCAT( ts.TRAINING_CODE ) AS TRAINING_CODE,	w.NAME AS wh,	w.ID AS wareId,	ts.BRANCH_ID AS BRANCH_ID FROM	training_status ts	INNER JOIN transfer_info ti ON ts.TRANSFER_INFO_ID = ti.id	INNER JOIN prof p ON ti.AGENT_ID = p.PROF_ID	INNER JOIN warehouse w ON p.WAREHOUSE_ID = w.ID  WHERE CAST(ts.TRAINING_DATE as date) BETWEEN :startDate AND :endDate AND ts.branch_id=:branch  GROUP BY	ts.BRANCH_ID,	p.WAREHOUSE_ID 	) tt	INNER JOIN branch_master bm ON bm.BRANCH_ID = tt.BRANCH_ID GROUP BY	tt.BRANCH_ID";
		} else {
			queryString= "SELECT GROUP_concat( tt.TRAINING_CODE), bm.`NAME`,tt.BRANCH_ID,GROUP_concat( tt.TRAINING_CODE, '~', tt.wh, '~', tt.wareId SEPARATOR '$') FROM	(SELECT	GROUP_CONCAT( ts.TRAINING_CODE ) AS TRAINING_CODE,	w.NAME AS wh,	w.ID AS wareId,	ts.BRANCH_ID AS BRANCH_ID FROM	training_status ts	INNER JOIN transfer_info ti ON ts.TRANSFER_INFO_ID = ti.id	INNER JOIN prof p ON ti.AGENT_ID = p.PROF_ID	INNER JOIN warehouse w ON p.WAREHOUSE_ID = w.ID  WHERE CAST(ts.TRAINING_DATE as date) BETWEEN :startDate AND :endDate GROUP BY	ts.BRANCH_ID,	p.WAREHOUSE_ID 	) tt	INNER JOIN branch_master bm ON bm.BRANCH_ID = tt.BRANCH_ID GROUP BY	tt.BRANCH_ID";
		}
		Query query = session.createSQLQuery(queryString);
		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		query.setParameter("startDate", sDate).setParameter("endDate", eDate);
		List<Object[]> result = query.list();
		return result;
	}

	@Override
	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear) {
		// TODO Auto-generated method stub
		String stDate = "2014-04-01";
		String enDate = DateUtil.getCurrentYear() + "-03-31";
		if (selectedFinYear != null && !StringUtil.isEmpty(selectedFinYear)) {
			stDate = selectedFinYear + "-04-01";
			int year = Integer.valueOf(selectedFinYear) + 1;
			enDate = year + "-03-31";
		}
		Date startDate = DateUtil.convertStringToDate(stDate, DateUtil.DATE);
		Date endDate = DateUtil.convertStringToDate(enDate, DateUtil.DATE);

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery = "select count(*) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.f.status=1 AND fc.procurementVariety.procurementProduct.code=:selectedCrop and f.enrollDate BETWEEN :sDate AND :eDate and fc.status=1";
			params.put("selectedCrop", selectedCrop);
		} else {
			// hqlQuery = "select count(*) FROM Farmer f WHERE f.status=1 and
			// CAST(f.enrollDate as date) BETWEEN :startDate AND :endDate";
			hqlQuery = "select count(*) from Farmer f where status in (0,1) and f.enrollDate BETWEEN :sDate AND :eDate and status_code=:statusCode";
			params.put("statusCode", Integer.valueOf(ESETxnStatus.SUCCESS.ordinal()));
		}
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != 0) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));
		}

		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		query.setParameter("sDate", startDate).setParameter("eDate", endDate);
		Integer val = ((Long) query.uniqueResult()).intValue();
		/*Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isInteger(obj)) {
			val = Integer.valueOf(obj.toString());
		}*/
		return val;
	}

	@Override
	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear) {
		// TODO Auto-generated method stub
		String stDate = "2014-04-01";
		String enDate = DateUtil.getCurrentYear() + "-03-31";
		if (selectedFinYear != null && !StringUtil.isEmpty(selectedFinYear)) {
			stDate = selectedFinYear + "-04-01";
			int year = Integer.valueOf(selectedFinYear) + 1;
			enDate = year + "-03-31";
		}
		Date startDate = DateUtil.convertStringToDate(stDate, DateUtil.DATE);
		Date endDate = DateUtil.convertStringToDate(enDate, DateUtil.DATE);

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT SUM(fc.estimatedYield) as Total,cast(SUM(fc.cultiArea) as int) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE  f.enrollDate BETWEEN :startDate AND :endDate  and f.status=1 and fa.status=1 and fc.status=1 and f.statusCode = 0";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedState) && selectedState != 0) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", selectedState);
		}
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.code=:selectedCrop";
			params.put("selectedCrop", selectedCrop);
		}
		if (!StringUtil.isEmpty(selectedCooperative)) {
			hqlQuery += " AND f.fpo=:selectedCooperative";
			params.put("selectedCooperative", selectedCooperative);
		}

		if (!StringUtil.isEmpty(selectedGender)) {
			hqlQuery += " AND f.gender=:selectedGender";
			params.put("selectedGender", selectedGender);
		}
		Query query = session.createQuery(hqlQuery);
		query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		query.setParameter("startDate", startDate).setParameter("endDate", endDate);

		return query.list();
	}

	@Override
	public List<Object[]> productChartByMobileUserToFarmer(String branch, String agent, String selectedSeason,
			String selectedFinYear) {
		// TODO Auto-generated method stub
		String hqlString = null;
		String stDate = "2014-04-01";
		String enDate = DateUtil.getCurrentYear() + "-03-31";
		if (selectedFinYear != null && !StringUtil.isEmpty(selectedFinYear)) {
			stDate = selectedFinYear + "-04-01";
			int year = Integer.valueOf(selectedFinYear) + 1;
			enDate = year + "-03-31";
		}
		Date startDate = DateUtil.convertStringToDate(stDate, DateUtil.DATE);
		Date endDate = DateUtil.convertStringToDate(enDate, DateUtil.DATE);

		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedSeason)) {
			if (!StringUtil.isEmpty(branch)) {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.agroTransaction.txnTime BETWEEN :startDate AND :endDate  and  d.branchId = :branch and (d.servicePointId = null or d.servicePointId ='') and d.agentId = :agent group by p.code ORDER BY amount desc";
			} else {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.agroTransaction.txnTime BETWEEN :startDate AND :endDate  and (d.servicePointId = null or d.servicePointId ='') and  d.agentId = :agent group by p.code ORDER BY amount desc";
			}
		} else {
			if (!StringUtil.isEmpty(branch)) {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.agroTransaction.txnTime BETWEEN :startDate AND :endDate  and  d.branchId = :branch and d.seasonCode=:season and (d.servicePointId = null or d.servicePointId ='') and d.agentId = :agent group by p.code ORDER BY amount desc";
			} else {
				hqlString = "SELECT p.name,p.code,sum((dd.quantity * dd.costPrice)) as amount,sum(dd.quantity),p.unit From Distribution d inner join d.distributionDetails dd inner join dd.product p where d.txnType = '314' and d.agroTransaction.txnTime BETWEEN :startDate AND :endDate  and  d.seasonCode=:season and (d.servicePointId = null or d.servicePointId ='') and  d.agentId = :agent group by p.code ORDER BY amount desc";
			}
		}
		Query query = session.createQuery(hqlString);
		query.setParameter("startDate", startDate).setParameter("endDate", endDate);

		if (!StringUtil.isEmpty(branch)) {
			query.setParameter("branch", branch);
		}
		if (!StringUtil.isEmpty(selectedSeason)) {
			query.setParameter("season", selectedSeason);
		}
		query.setParameter("agent", agent);

		List<Object[]> result = query.list();
		return result;
	}
	
	
	@Override
	public List<FarmCropsField> listRemoveFarmCropFields() {
		Object[] values = { FarmerField.ACTIVE };
		return list("From FarmCropsField ff WHERE ff.status=?", values);
	}

	@Override
	public List<Object[]> listFarmsLastInspectionDate() {
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery("SELECT max(DATE(pi.INSPECTION_DATE)),pi.FARM_ID from periodic_inspection pi group by pi.FARM_ID");
		List list = query.list();
		return list;
	}
	
	public CropYield findMoleculeDateByLotCode(String lotCode){
		return (CropYield) find("FROM CropYield cy WHERE cy.landHolding=?",lotCode);
	}
	@Override
	public Farmer findOlivadoFarmerByFarmerCode(String farmerCode) {
		Object[] values = { farmerCode, ESETxnStatus.SUCCESS.ordinal() };
		Farmer farmer = (Farmer) find("FROM Farmer fr WHERE  fr.farmerCode = ? AND fr.statusCode = ?", values);
		return farmer;
	}
	@Override
	public List<Object[]> listFarmerAddressById(Long farmerId) {

		return list("SELECT fr.address,fr.city.name,fr.village.name FROM Farmer fr WHERE fr.id = ?", farmerId);
	}

	@Override
	public List<Object[]> populateWarehouseMobileUserToFarmer_AgentChart(String selectedBranch, String selectedSeason) {
		// TODO Auto-generated method stub
		String queryString = null;
		Session session = getSessionFactory().getCurrentSession();
		if (StringUtil.isEmpty(selectedSeason)) {
			if (!StringUtil.isEmpty(selectedBranch)) {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE ) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.SERVICE_POINT_ID = w.`CODE`  inner join farmer f on f.FARMER_ID=d.FARMER_ID WHERE d.TXN_TYPE = '314' AND ( d.AGENT_ID IS NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND d.branch_id=:branch and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			} else {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE ) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.SERVICE_POINT_ID = w.`CODE`  inner join farmer f on f.FARMER_ID=d.FARMER_ID WHERE d.TXN_TYPE = '314' AND ( d.AGENT_ID IS NULL ) AND ( d.FARMER_ID IS NOT NULL ) and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			}
		} else {
			if (!StringUtil.isEmpty(selectedBranch)) {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE ) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.SERVICE_POINT_ID = w.`CODE`  inner join farmer f on f.FARMER_ID=d.FARMER_ID WHERE d.TXN_TYPE = '314' AND ( d.AGENT_ID IS NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND d.branch_id=:branch AND d.season_code =:season and f.STATUS=1 and f.STATUS_CODE=0 GROUP BY w.ID ORDER BY w.NAME DESC";
			} else {
				queryString = "SELECT w.ID ,w.NAME ,w.code,sum( dd.QUANTITY * p.PRICE) FROM DISTRIBUTION d INNER JOIN DISTRIBUTION_DETAIL dd ON d.id = dd.DISTRIBUTION_ID INNER JOIN PRODUCT p ON dd.PRODUCT_ID = p.ID INNER JOIN warehouse w ON d.SERVICE_POINT_ID = w.`CODE`  inner join farmer f on f.FARMER_ID=d.FARMER_ID WHERE d.TXN_TYPE = '314' AND ( d.AGENT_ID IS NULL ) AND ( d.FARMER_ID IS NOT NULL ) AND d.season_code =:season and f.STATUS=1 and f.STATUS_CODE=0  GROUP BY w.ID ORDER BY w.NAME DESC";
			}
		}
		Query query = session.createSQLQuery(queryString);

		if (!StringUtil.isEmpty(selectedBranch)) {
			query.setParameter("branch", selectedBranch);
		}
		if (!StringUtil.isEmpty(selectedSeason)) {
			query.setParameter("season", selectedSeason);
		}
		List<Object[]> result = query.list();
		return result;
	}
	
	@Override
	public List<Object[]> findDyamicReportTableConfigParentIdsById(String entity) {

		String queryString = "SELECT parent FROM dynamic_report_table_config fr WHERE fr.id in ("+entity+")";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
		
	
	}
	
	@Override
	public List<String> listFarmerByCreatedUser() {
		String queryString = "SELECT DISTINCT f.CREATED_USERNAME from farmer f GROUP BY f.CREATED_USERNAME;";
		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(queryString);
		List<String> result = query.list();
		session.flush();
		session.close();
		return result;
		//return list("SELECT DISTINCT f.createdUsername from farmer f GROUP BY f.createdUsername");
	}
	

	@Override
	public List<Object[]> listFarmersWithFarmParcelIds() {
		List<Object[]> farmerList = list(
				"SELECT DISTINCT frm.farmer.id,CONCAT(frm.farmer.firstName,' ', frm.farmer.lastName) FROM Farm frm where (frm.gcParcelID IS NOT NULL AND frm.gcParcelID <> '')");
		return farmerList;
	}
	
	@Override
	public List<Object[]> listFarmersWithFarmCropParcelIds() {
		List<Object[]> farmerList = list(
				"SELECT DISTINCT frmCrp.farm.farmer.id,CONCAT(frmCrp.farm.farmer.firstName,' ', frmCrp.farm.farmer.lastName) FROM FarmCrops frmCrp where ((frmCrp.gcParcelID IS NOT NULL AND frmCrp.gcParcelID <> ''))");
		return farmerList;
	}
	
	@Override
	public List<Object[]> listFarmParcelIdsByFarmer(String farmerId) {
		List<Object[]> farmerList = list(
				"SELECT DISTINCT frmCrp.farm.id,frmCrp.farm.farmName,frmCrp.farm.gcParcelID FROM FarmCrops frmCrp where frmCrp.farm.farmer.id = '"
						+ farmerId
						+ "' AND ((frmCrp.farm.gcParcelID IS NOT NULL AND frmCrp.farm.gcParcelID <> '') OR (frmCrp.gcParcelID IS NOT NULL AND frmCrp.gcParcelID <> ''))");
		return farmerList;
	}
	
	@Override
	public List<Object[]> listFarmCropsWithGcParcelIdsByFarm(String farmId) {
		List<Object[]> farmerList = list(
				"SELECT frmCrp.id,frmCrp.procurementVariety.procurementProduct.name,frmCrp.gcParcelID FROM FarmCrops frmCrp where ((frmCrp.gcParcelID IS NOT NULL AND frmCrp.gcParcelID <> '') OR (frmCrp.farm.gcParcelID IS NOT NULL AND frmCrp.farm.gcParcelID <> '')) AND frmCrp.farm.id='"
						+ farmId + "'");
		return farmerList;
	}
	
	@Override
	public List<Object[]> listFarmWithoutCropParcelIdsByFarmer(String farmerId) {
		List<Object[]> farmerList = list(
				"SELECT DISTINCT frm.id,frm.farmName,frm.gcParcelID FROM Farm frm where frm.farmer.id = '" + farmerId
						+ "' AND (frm.gcParcelID IS NOT NULL AND frm.gcParcelID <> '')");
		return farmerList;
	}
	
	@Override
	public List<Object[]> listFarmersWithIdAndCode() {
		List<Object[]> farmerIdCodeList = list(
				"SELECT fm.farmer.id,concat(fm.farmer.firstName,' ',fm.farmer.lastName,' - ',fm.farmer.farmerId) FROM Farm fm WHERE fm.farmer.farmerId IS NOT NULL ORDER BY fm.farmer.firstName");
		return farmerIdCodeList;
	}
	
	@Override
	public List<Object[]> listFarmsOfFarmerId(long farmerId) {

		List<Object[]> farmList = list(
				"SELECT fm.id ,concat(fm.farmName,'-',fm.farmCode) FROM Farm fm where fm.farmer.id = ? and fm.status=1",
				farmerId);
		return farmList;
	}
	
	@Override
	public boolean findIfFarmHasParcelId(String farmId) {
		boolean hasGCParcelId = false;
		Farm farm = (Farm) find(
				"FROM Farm frm WHERE frm.farmId = ? AND frm.gcParcelID IS NOT NULL AND frm.gcParcelID <> ''", farmId);
		if (!ObjectUtil.isEmpty(farm)) {
			hasGCParcelId = true;
		}
		return hasGCParcelId;
	}
	
	@Override
	public List<Object[]> listFarmCropsByFarm(String farmId) {
		List<Object[]> farmerList = list(
				"SELECT frmCrp.id,frmCrp.procurementVariety.procurementProduct.name,frmCrp.gcParcelID FROM FarmCrops frmCrp where frmCrp.farm.id='"
						+ farmId + "'");
		return farmerList;
	}
	
	@Override
	public List<Object[]> listFarmersWithFarmCoordinates() {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String sqlQuery = "SELECT DISTINCT	farm0_.FARMER_ID AS col_0_0_,concat( farmer3_.FIRST_NAME, ' ', farmer3_.LAST_NAME, ' - ', farmer3_.FARMER_ID ) AS col_1_0_ FROM	FARM farm0_	JOIN FARMER farmer3_ ON farmer3_.ID = farm0_.farmer_ID	JOIN COORDINATES_MAP coordinate1_ ON coordinate1_.`ID` = farm0_.`ACTIVE_COORDINATES`	JOIN COORDINATES coordinate2_ ON coordinate2_.COORDINATES_MAP_ID = coordinate1_.ID WHERE	farm0_.FARMER_ID = farmer3_.id 	AND ( farmer3_.FARMER_ID IS NOT NULL ) 	AND ( coordinate1_.ID IS NOT NULL ) 	AND ( coordinate2_.ID IS NOT NULL ) ORDER BY	farmer3_.FIRST_NAME";
		List<Object[]> farmerIdCodeList;
		Query query = session.createSQLQuery(sqlQuery);
		farmerIdCodeList = query.list();
		session.flush();
		session.close();
		return farmerIdCodeList;
	}

	@Override
	public List<Object[]> listFarmersWithFarmCropCoordinates() {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		String sqlQuery = "SELECT DISTINCT	farm0_.FARMER_ID AS col_0_0_,concat( farmer3_.FIRST_NAME, ' ', farmer3_.LAST_NAME, ' - ', farmer3_.FARMER_ID ) AS col_1_0_ FROM	FARM farm0_	JOIN FARMER farmer3_ ON farmer3_.ID = farm0_.farmer_ID	JOIN COORDINATES_MAP coordinate1_ ON coordinate1_.`ID` = farm0_.`ACTIVE_COORDINATES`	JOIN COORDINATES coordinate2_ ON coordinate2_.COORDINATES_MAP_ID = coordinate1_.ID WHERE	farm0_.FARMER_ID = farmer3_.id 	AND ( farmer3_.FARMER_ID IS NOT NULL ) 	AND ( coordinate1_.ID IS NOT NULL ) 	AND ( coordinate2_.ID IS NOT NULL ) ORDER BY	farmer3_.FIRST_NAME";
		List<Object[]> farmerIdCodeList;
		Query query = session.createSQLQuery(sqlQuery);
		farmerIdCodeList = query.list();
		session.flush();
		session.close();
		return farmerIdCodeList;
	}
	
	@Override
	public List<Object[]> listFarmCropByFarmId(String farmId) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Object[]> farmList = new ArrayList<Object[]>();
		String sqlQuery = "SELECT DISTINCT frm.id,frm.farm_name,frm.gc_parcel_id FROM Farm frm JOIN COORDINATES_MAP coord1 ON coord1.ID = frm.ACTIVE_COORDINATES JOIN COORDINATES coord2 ON coord2.COORDINATES_MAP_ID = coord1.ID where frm.id = '"
				+ farmId + "'";
		Query query = session.createSQLQuery(sqlQuery);
		farmList = query.list();

		session.flush();
		session.close();
		return farmList;
	}

	
	@Override
	public List<Object[]> listFarmWithCropParcelIdsByFarmer(String farmerId) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		List<Object[]> farmList = new ArrayList<Object[]>();
		String sqlQuery = "SELECT DISTINCT frm.id,frm.farm_name,frm.gc_parcel_id FROM Farm frm JOIN COORDINATES_MAP coord1 ON coord1.ID = frm.ACTIVE_COORDINATES JOIN COORDINATES coord2 ON coord2.COORDINATES_MAP_ID = coord1.ID where frm.farmer_id = '"
				+ farmerId + "'";
		Query query = session.createSQLQuery(sqlQuery);
		farmList = query.list();

		session.flush();
		session.close();
		return farmList;
	}
	
	@Override
	public List<Object[]> listFarmCropsWithFarmId(long farmId) {
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"SELECT DISTINCT frmCrp.id,frmCrp.procurementVariety.procurementProduct.name,frmCrp.gcParcelID FROM FarmCrops frmCrp WHERE frmCrp.farm.id =:farmId AND SIZE(frmCrp.activeCoordinates.farmCropsCoordinates)>1");
		List<Object[]> result = new ArrayList<Object[]>();
		query.setParameter("farmId", farmId);
		result = query.list();
		session.flush();
		session.close();
		return result;
	}

	public List<Object[]> listValueByFieldName(String field) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(FarmerDynamicFieldsValue.class);
		criteria.createAlias("farmerDynamicData", "fd",JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("fieldName", field));
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("id"));
		pList.add(Projections.property("fieldValue"));
		pList.add(Projections.property("farmerDynamicData.id"));
		criteria.setProjection(pList);
		List<Object[]> list = criteria.list();
		session.flush();
		session.close();
		return list;
	}
	
	@Override
	public List<Object[]> listOfProofNo() {
		// TODO Auto-generated method stub	
		Object[] values = { Farmer.Status.ACTIVE.ordinal()};
		return list(
				"SELECT fr.farmerId,fr.firstName,fr.id,fr.idproofNo FROM Farmer fr  WHERE fr.idproofNo !=null and fr.status=? order by fr.farmerId ASC",
				values);
	}

	@Override
	public List<Object[]> listVendorByLoanStatusAndFarmerAndVendor(Long farmerId,String vendorId) {
		
		Object[] values = { farmerId,vendorId};
		return list(
				"SELECT db.vendor.vendorId,db.vendor.vendorName,db.vendor.id FROM DistributionBalance db  WHERE db.farmer.id =? AND db.vendor.vendorId=? order by db.vendor.vendorId ASC",
				values);
	}

	public List<Object[]> listFarmerCodeIdNameByFarmerTypezAndLoanApplication(String branchId) {

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		/*Query query = session.createSQLQuery(
				"SELECT f.farmer_Id,f.farmer_Code,f.first_Name,f.last_Name,f.id,f.sur_Name FROM Farmer f INNER JOIN loan_application l ON l.farmer_Id = f.id  where f.VILLAGE_Id=:villageCode AND"
				+ " f.status=:status AND f.status_Code=:statusCode AND f.typez=:typez AND l.LOAN_STATUS =:loanStaus group by f.ID");
		*/
		Query query = session.createSQLQuery(
				"SELECT f.farmer_Id,f.farmer_Code,f.first_Name,f.last_Name,f.id,f.sur_Name,f.proof_No FROM Farmer f WHERE f.status=1 and f.branch_id=:branchId ");
		query.setParameter("branchId", branchId);

		//query.setMaxResults(1);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object[]> listActiveContractFarmersByLoanStatusAndSamithiAndVendor(Long id,String vendorId) {
		
		Object[] values = { id,vendorId,Farmer.Status.ACTIVE.ordinal()};
		return list(
				"SELECT db.farmer.farmerId,db.farmer.firstName,db.farmer.id,db.farmer.idproofNo FROM DistributionBalance db  WHERE db.farmer.samithi.id =? AND db.vendor.vendorId=? AND db.farmer.status=? order by db.farmer.farmerId ASC",
				values);
	}
	
	
	@Override
	public LoanDistribution findFarmerLatestLoanYear(Long farmerId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM LoanDistribution ld WHERE ld.farmer.id=:fId ORDER BY ld.id DESC");
		query.setParameter("fId", farmerId);
		query.setMaxResults(1);
		LoanDistribution loanDistribution = (LoanDistribution) query.uniqueResult();

		return loanDistribution;

	}
	
	public LoanDistribution findGroupLatestLoanYear(Long farmerId) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM LoanDistribution ld WHERE ld.group.id=:fId ORDER BY ld.id DESC");
		query.setParameter("fId", farmerId);
		query.setMaxResults(1);
		LoanDistribution loanDistribution = (LoanDistribution) query.uniqueResult();

		return loanDistribution;
	}
	
	@Override
	public LoanApplication findFarmerLatestLoanApplication(Long farmerId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM LoanApplication l WHERE l.farmer.id=:fId ORDER BY l.id DESC");
		query.setParameter("fId", farmerId);
		query.setMaxResults(1);
		LoanApplication loanApplication = (LoanApplication) query.uniqueResult();

		return loanApplication;

	}
	@Override
	public LoanApplication findGroupLatestLoanApplication(Long farmerId) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM LoanApplication l WHERE l.farmer.id=:fId ORDER BY l.id DESC");
		query.setParameter("fId", farmerId);
		query.setMaxResults(1);
		LoanApplication loanApplication = (LoanApplication) query.uniqueResult();

		return loanApplication;

	}
	
	public List<Farmer> listGroup() {
		Object[] values = { Farmer.IRP, ESETxnStatus.SUCCESS.ordinal() };
		return list("FROM Farmer f WHERE f.typez= ? AND f.statusCode = ? ORDER BY f.firstName ASC", values);
	}
	
	public Farmer findGroupById(Long id) {
		Object[] values = { id, Farmer.IRP };
		return (Farmer) find("FROM Farmer fr WHERE fr.id = ? AND fr.typez=?", values);
	}

	@Override
	public List<Object[]> listFarmscountInfo() {

		return list("SELECT f.id,f.farms.size from Farmer f  where f.statusCode=0 and f.status=1 ");
	}
	
	@Override
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm,String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers,String selectedStatus ) {

		ProjectionList pList = Projections.projectionList();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Farm.class);
		criteria.createAlias("farmer", "f");

		pList.add(Projections.property("f.id"));
		pList.add(Projections.property("latitude"));
		pList.add(Projections.property("longitude"));
		pList.add(Projections.property("farmCode"));
		pList.add(Projections.property("f.firstName"));
		pList.add(Projections.property("farmName"));
		pList.add(Projections.property("id"));
		pList.add(Projections.property("f.isCertifiedFarmer"));
		//pList.add(Projections.property("fic.organicStatus"));
		 iscList = new HashMap<>();
	
		if (!StringUtil.isEmpty(farm.getCropCode())) {
			criteria.createAlias("farmCrops", "fc");

			criteria.createAlias("fc.procurementVariety", "pv");
			criteria.createAlias("pv.procurementProduct", "pp");
			criteria.add(Restrictions.eq("pp.id", Long.valueOf(farm.getCropCode())));

		}

		if (!ObjectUtil.isEmpty(farm.getFarmer())) {

			if (!ObjectUtil.isEmpty(farm.getFarmer().getVillage())) {
				criteria.createAlias("f.village", "v");
				criteria.add(Restrictions.eq("v.id", farm.getFarmer().getVillage().getId()));
			}
			
			

			if (!ObjectUtil.isEmpty(farm.getFarmer().getCity()) 
					&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getId()) 
					&& farm.getFarmer().getCity().getId() > 0) {
				criteria.createAlias("f.city", "c");				
				criteria.add(Restrictions.eq("c.id", farm.getFarmer().getCity().getId()));
				
				if (!ObjectUtil.isEmpty(farm.getFarmer().getCity()) 
						&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality()) 
						&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality().getId()) 
						&& farm.getFarmer().getCity().getLocality().getId() > 0) {
					criteria.createAlias("c.locality", "l");
					criteria.add(Restrictions.eq("l.id", farm.getFarmer().getCity().getLocality().getId()));
				}
				
				if (!ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality()) 
						&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality().getState()) 
						&& !ObjectUtil.isEmpty(farm.getFarmer().getCity().getLocality().getState().getId())
						&& farm.getFarmer().getCity().getLocality().getState().getId() > 0) {
					criteria.createAlias("l.state", "s");
					criteria.add(Restrictions.eq("s.id", farm.getFarmer().getCity().getLocality().getState().getId()));
				}

				
			}
			if (!ObjectUtil.isEmpty(farm.getFarmer().getIsCertifiedFarmer())
					&& !(farm.getFarmer().getIsCertifiedFarmer() < 0)) {
				if (selectedOrganicStatus.equalsIgnoreCase("Conventional")) {
					criteria.add(Restrictions.eq("f.isCertifiedFarmer", 0));
				}/*else if(selectedOrganicStatus.equalsIgnoreCase("3")){
					criteria.add(Restrictions.eq("f.isCertifiedFarmer", 0));
				}*/else{
				criteria.add(Restrictions.eq("f.isCertifiedFarmer", farm.getFarmer().getIsCertifiedFarmer()));
				}
			}
			if (!StringUtil.isEmpty(farm.getFarmer().getSeasonCode())) {
				criteria.add(Restrictions.eq("f.seasonCode", farm.getFarmer().getSeasonCode()));
			}
			if (farm.getFarmer().getBranchId() != null && !StringUtil.isEmpty(farm.getFarmer().getBranchId())) {
				criteria.add(Restrictions.eq("f.branchId", farm.getFarmer().getBranchId()));
			}
			/* querying with left join ics conversion takes time so when filtering used join when no ics filter used takes seperate list for farm and organic status map and set to the object*/
			if (!ObjectUtil.isEmpty(farm.getFarmICSConversion())) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
				Stream<Object[]>  icsMap= list("select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where  fi.farm is not null and  fi.isActive=1 and fi.organicStatus=?",new Object[]{farm.getFarmICSConversion().iterator().next().getOrganicStatus()}).stream();
		     	 iscList = icsMap.collect(Collectors.toMap(u ->(Long) u[0], u -> u[1].toString()));
		     	if(!iscList.isEmpty()){
		     		criteria.add(Restrictions.in("id", iscList.keySet()));
		     	}
		     	 
				}
				else{
					Stream<Object[]>  icsMap= list("select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where  fi.farm is not null and  fi.isActive=1 and fi.icsType in (0 ,1 ,2) GROUP BY fi.farm.id").stream();
			     	 iscList = icsMap.collect(Collectors.toMap(u ->(Long) u[0], u -> u[1].toString()));
			     	criteria.add(Restrictions.in("id", iscList.keySet()));
				}
			}else{
				
					Stream<Object[]>  icsMap= list("select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where fi.farm is not null and fi.isActive=1 GROUP BY fi.farm.id").stream();
					 iscList = icsMap.collect(Collectors.toMap(u ->(Long) u[0], u -> u[1].toString()));
				
			   /*  
				String queryString = "select fi.farm.id,COALESCE(fi.organicStatus,'0') from  FarmIcsConversion fi  where fi.farm is not null and isActive=1";
				Session ses = getSessionFactory().openSession();
				Query query = ses.createQuery(queryString);
				List<Long> results = ((List<Object[]>) query.list()).stream().map(result ->(Long) result[0]).collect(Collectors.toList());
		     	//criteria.add(Restrictions.in("id", results));
*/			}
		}
		if (!selectedFarmer.equalsIgnoreCase("") && !StringUtil.isEmpty(selectedFarmer)){
		if (!ObjectUtil.isEmpty(farm.getFarmer().getId())) {
			criteria.add(Restrictions.eq("f.id",  Long.valueOf(selectedFarmer)));
		}
}	
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.isNotNull("longitude"));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "0")));
		criteria.add(Restrictions.not(Restrictions.eq("latitude", "")));
		criteria.add(Restrictions.not(Restrictions.eq("longitude", "")));
		
		
		if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
			if(!selectedStatus.equalsIgnoreCase("2")){
				criteria.add(Restrictions.eq("f.status", Integer.valueOf(selectedStatus)));
			}else{
				criteria.add(Restrictions.isNotNull("f.status"));
			}
		}else{
			criteria.add(Restrictions.eq("f.status",1));
		}

		criteria.add(Restrictions.eq("status", 1));
		
		if(yieldEstimationDoneFarmers.size() >= 1){
			criteria.add(Restrictions.in("id", yieldEstimationDoneFarmers));
		}
		
     	criteria.setProjection(pList);
		List<Object[]> list = criteria.list();
		List<Object[]> output = list.stream().map(a-> {
			 a  = Arrays.copyOf(a, a.length + 1);
			 
			    a[a.length - 1] = iscList.containsKey(Long.valueOf(a[6].toString())) ? iscList.get(Long.valueOf(a[6].toString())) : "0";
			    return a;
			   
         }).collect(Collectors.toList());
		
		list.clear();
		return output;
	}
	
	
	
	@Override
	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			hqlQuery = "select count(distinct f.id) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.statusCode=0 and fc.status=1 AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));
		} else {
			hqlQuery = "select count(*) FROM Farmer f WHERE  f.statusCode=0";
		}
		
		if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
			if(!selectedStatus.equalsIgnoreCase("2")){
				hqlQuery += " and f.status=:selectedStatus";
			params.put("selectedStatus", Integer.valueOf(selectedStatus));
			}else{
				hqlQuery += " and f.status is not null";
			}
		}else{
			hqlQuery += " and f.status=1";
		}
		
		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}

		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}
		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id=:selectedFarmer";
			params.put("selectedFarmer", Long.valueOf(selectedFarmer));

		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);

		}

		if (!selectedOrganicStatus.equalsIgnoreCase("conventional")) {
			if (!StringUtil.isEmpty(selectedCrop)) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  fi.isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  fi.isActive=1 and fi.organicStatus in (0,1,2)");
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				}
			} else {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  fi.isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					
						if(!ObjectUtil.isListEmpty(icsMap)){
							hqlQuery += " AND f.id in (:fList)";
							params.put("fList", icsMap);
						}else{
							hqlQuery += " AND f.isCertifiedFarmer=0";
						}
										
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  fi.isActive=1 and fi.organicStatus in (0 ,1 ,2)");
					hqlQuery += " AND f.id in (:fList)";

					params.put("fList", icsMap);
				} else {
					hqlQuery += " AND f.isCertifiedFarmer in (0,1)";
				}
			}
		}

		else {
			hqlQuery += " AND f.isCertifiedFarmer=0";
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			if (str.equalsIgnoreCase("fList")) {
				List<Long> result = (List<Long>) params.get(str);
				if(!ObjectUtil.isListEmpty(result)){
					query.setParameterList(str, result);
				}
				

			} else {
				query.setParameter(str, params.get(str));
			}
		}

		Integer val = 0;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).intValue();
		}
		return val;

	}
	
	
	
	@Override
	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT  SUM(fc.cultiArea),sum(fc.actualSeedYield),sum(fc.estimatedYield) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fc.farm.farmer f  WHERE  fc.status=1 ";
		
		
		if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
			if(!selectedStatus.equalsIgnoreCase("2")){
				hqlQuery += " and f.status=:selectedStatus";
			params.put("selectedStatus", Integer.valueOf(selectedStatus));
			}else{
				hqlQuery += " and f.status is not null";
			}
		}else{
			hqlQuery += " and f.status=1";
		}
			
		
		
		
		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}
		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}
		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id=:selectedFarmer";
			params.put("selectedFarmer", Long.valueOf(selectedFarmer));

		}

		if (!StringUtil.isEmpty(selectedCrop) && selectedCrop != null) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));

		}

		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND f.seasonCode =:season";
			params.put("season", selectedSeason);

		}
		if (!selectedOrganicStatus.equalsIgnoreCase("conventional")) {
			if (!StringUtil.isEmpty(selectedCrop)) {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0,1,2)");
					hqlQuery += " AND fa.id in (:fList) AND f.isCertifiedFarmer=1";
					params.put("fList", icsMap);
				}

			} else {
				if (selectedOrganicStatus.equalsIgnoreCase("3")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus=?",
							new Object[] { selectedOrganicStatus });
					if(!ObjectUtil.isListEmpty(icsMap)){
						hqlQuery += " AND f.id in (:fList)";
						params.put("fList", icsMap);
					}else{
						hqlQuery += " AND f.isCertifiedFarmer = 0";
					}
				} else if (selectedOrganicStatus.equalsIgnoreCase("0")) {
					List<Long> icsMap = list(
							"select fi.farm.farmer.id from  FarmIcsConversion fi  where  fi.farm is not null and  isActive=1 and fi.organicStatus in (0 ,1 ,2) ");
					hqlQuery += " AND f.id in (:fList)";

					params.put("fList", icsMap);
				} else {
					hqlQuery += " AND f.isCertifiedFarmer in (1,0)";
				}
			}

		} else {
			hqlQuery += " AND f.isCertifiedFarmer=0";
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			if (str.equalsIgnoreCase("fList")) {
				List<Long> result = (List<Long>) params.get(str);
				query.setParameterList(str, result);

			} else {
				query.setParameter(str, params.get(str));
			}
		}

		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {

			val = (Object[]) obj;
		}
		return val;

	}

	
	public List<Object[]> farmersByGroup(String selectedBranch) {

		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),s.name,s.code,f.branchId,s.id from Farmer f inner join f.farms fm inner join fm.farmICSConversion fic  inner join f.samithi s where f.branchId = :branch and f.statusCode = 0 group by f.samithi.id";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
		} else {
			String hqlString = "SELECT count(f.id),s.name,s.code,f.branchId,s.id from Farmer f inner join f.samithi s where f.statusCode = 0 group by f.samithi.id";
			query = session.createQuery(hqlString);
		}
		List<Object[]> result = query.list();
		return result;
	}
	
	public List<Object[]> getFarmDetailsAndProposedPlantingAreaByGroup(String locationLevel1, String selectedBranch,
			String gramPanchayatEnable) {
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		String locationLevel = String.valueOf(locationLevel1.charAt(0));
		String flag = "enable";
	
			Session session = getSessionFactory().getCurrentSession();
			Query query;
			String hqlString = null;
			 if (!StringUtil.isEmpty(locationLevel) && locationLevel.equals("f")) {
				 hqlString = "select count(fa.id),COALESCE(sum(fdi.proposedPlantingArea),0) as TOTAL_LAND_HOLDING,s.name from Farm fa inner join fa.farmer f inner join fa.farmDetailedInfo fdi inner join f.samithi s where f.branchId = :branch and f.statusCode = 0 GROUP BY s.id ";
					flag = "disable";
					query = session.createQuery(hqlString);
					query.setParameter("branch", selectedBranch);
					List<Object[]> result = query.list();
					return result;
			 }else{
				 hqlString = "select count(fm.id),COALESCE(sum(fdi.proposedPlantingArea),0) as TOTAL_LAND_HOLDING,CASE WHEN fic.icsType = 0 THEN 'IC-1' WHEN fic.icsType = 1 THEN 'IC-2' WHEN fic.icsType = 2 THEN 'IC-3' WHEN fic.icsType = 3 THEN 'Organic' ELSE 'Non-Certified' END,f.samithi.name,f.samithi.code FROM Farm fm inner join fm.farmDetailedInfo fdi INNER JOIN fm.farmICSConversion fic INNER JOIN  fm.farmer f  inner join f.samithi s WHERE f.status=1 AND f.branchId = :branch AND s.code= :locationCode GROUP BY fic.icsType";
					flag = "disable";
					query = session.createQuery(hqlString);
					query.setParameter("branch", selectedBranch);
					query.setParameter("locationCode", locationLevel1);
					List<Object[]> result = query.list();
					return result;
			 }
			
			
		

	}
	
	@Override
	public List<Object[]> findFarmerCountByGroupICS(String selectedBranch) {
		String groupBy = " GROUP BY fic.icsType,f.samithi.name";
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "select count(f.id),CASE WHEN fic.icsType = 0 THEN 'IC-1' WHEN fic.icsType = 1 THEN 'IC-2' WHEN fic.icsType = 2 THEN 'IC-3' WHEN fic.icsType = 3 THEN 'Organic' ELSE 'Non-Certified' END,f.samithi.name,f.samithi.code,fic.icsType,f.samithi.id FROM Farm fm INNER JOIN fm.farmICSConversion fic INNER JOIN  fm.farmer f WHERE f.status=1";
		if (!StringUtil.isEmpty(selectedBranch)) {
			hqlQuery += " AND f.branchId =:branch ";
			params.put("branch", selectedBranch);
		}		
		hqlQuery += groupBy;
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		List<Object[]> result = query.list();
		return result;

	}
	
	public List<Object[]> populateFarmerLocationCropChartByGroup(String codeForCropChart,String selectedBranch,String seasonCode) {
		
		
		String hqlString;
		Session session = getSessionFactory().getCurrentSession();
		Query query = null;

		if (StringUtil.isEmpty(seasonCode)) {
			if (!StringUtil.isEmpty(codeForCropChart)) {
				if(StringUtil.isEmpty(selectedBranch)){
					hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code,sum(famcrps.estimatedYield) as yield from Farmer f inner join f.samithi s inner join f.farms fa  inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 AND s.code= :samCode group by pp.code";
					
					query = session.createQuery(hqlString);
					query.setParameter("samCode", codeForCropChart);
					List<Object[]> result = query.list();
					return result;
				}else{
				hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code,sum(famcrps.estimatedYield) as yield from Farmer f inner join f.samithi s inner join f.farms fa  inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.branchId = :branchId and f.statusCode = 0 AND s.code= :samCode group by pp.code";
				
				query = session.createQuery(hqlString);
				query.setParameter("samCode", codeForCropChart);
				query.setParameter("branchId", selectedBranch);
				List<Object[]> result = query.list();
				return result;
				}
			} else {
				hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code,sum(famcrps.estimatedYield) as yield  from Farmer f inner join f.samithi s inner join f.farms fa  inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 AND f.status=1 group by pp.code ";
				query = session.createQuery(hqlString);
				List<Object[]> result = query.list();
				return result;
			}
		}
		else
		{
			if (!StringUtil.isEmpty(codeForCropChart)) {
				hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code,sum(famcrps.estimatedYield) as yield from Farmer f inner join f.samithi s inner join f.farms fa inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.branchId = :branchId and f.statusCode = 0 AND s.code= :samCode and famcrps.cropSeason.code = :season group by pp.code";
				
				query = session.createQuery(hqlString);
				query.setParameter("samCode", codeForCropChart);
				query.setParameter("branchId", selectedBranch);
				query.setParameter("season", seasonCode);
				List<Object[]> result = query.list();
				return result;
				
			} else {
				hqlString = "SELECT count(fa.id),sum(famcrps.cultiArea) as CULTIVATION_AREA,pp.name,pp.code,sum(famcrps.estimatedYield) as yield  from Farmer f inner join f.samithi s inner join f.farms fa  inner join fa.farmDetailedInfo fdi inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 AND f.status=1 and famcrps.cropSeason.code = :season group by pp.code ";
				query = session.createQuery(hqlString);
				query.setParameter("season", seasonCode);
				List<Object[]> result = query.list();
				return result;
			}
		}
			

		
	}


	
	/*@Override
	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(Object obj, String selectedStatus,
			String plottingType) {

		ProjectionList pList = Projections.projectionList();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = null;
		List<Object[]> list = null;
		if (obj instanceof FarmCrops && plottingType.equalsIgnoreCase("2")) {
			criteria = session.createCriteria(FarmCrops.class);
			FarmCrops crops = (FarmCrops) obj;
			criteria.createAlias("farm", "fm");
			criteria.createAlias("fm.farmer", "f");
			if (!StringUtil.isEmpty(crops.getCropCode())) {
				criteria.createAlias("procurementVariety", "pv");
				criteria.createAlias("pv.procurementProduct", "pp");
				criteria.add(Restrictions.eq("pp.id", Long.valueOf(crops.getCropCode())));
			}

			if (!ObjectUtil.isEmpty(crops.getFarm()) && !ObjectUtil.isEmpty(crops.getFarm().getFarmer())) {

				if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getVillage())) {
					criteria.createAlias("f.village", "v");
					criteria.add(Restrictions.eq("v.id", crops.getFarm().getFarmer().getVillage().getId()));
				}

				if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getCity())
						&& !ObjectUtil.isEmpty(crops.getFarm().getFarmer().getCity().getLocality())) {
					criteria.createAlias("f.city", "c");
					criteria.createAlias("c.locality", "l");
					criteria.createAlias("l.state", "s");

					if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getStateId())) {
						criteria.add(Restrictions.eq("s.id",
								crops.getFarm().getFarmer().getCity().getLocality().getState().getId()));
					}

					if (crops.getFarm().getFarmer().getCity().getLocality().getId() > 0) {
						criteria.add(
								Restrictions.eq("l.id", crops.getFarm().getFarmer().getCity().getLocality().getId()));
					}
				}
				if (crops.getFarm().getFarmer().getId() > 0) {
					criteria.add(Restrictions.eq("f.id", crops.getFarm().getFarmer().getId()));
				}

				if (crops.getFarm().getFarmer().getBranchId() != null
						&& !StringUtil.isEmpty(crops.getFarm().getFarmer().getBranchId())) {
					criteria.add(Restrictions.eq("f.branchId", crops.getFarm().getFarmer().getBranchId()));
				}

			}
			if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
				if (!selectedStatus.equalsIgnoreCase("2")) {
					criteria.add(Restrictions.eq("f.status", Integer.valueOf(selectedStatus)));
				} else {
					criteria.add(Restrictions.isNotNull("f.status"));
				}
			} else {
				criteria.add(Restrictions.eq("f.status", 1));
			}

			pList.add(Projections.property("id"));
			pList.add(Projections.property("latitude"));
			pList.add(Projections.property("longitude"));
			pList.add(Projections.property("fm.farmCode"));
			pList.add(Projections.property("f.firstName"));
			pList.add(Projections.property("fm.farmName"));
			pList.add(Projections.property("fm.id"));
			pList.add(Projections.property("f.id"));			
			criteria.setProjection(pList);
			 list = criteria.list();
		} else if (obj instanceof Farm && plottingType.equalsIgnoreCase("1")) {
				criteria = session.createCriteria(Farm.class);
				Farm crops = (Farm) obj;
				criteria.createAlias("farmer", "f");				

				if (!ObjectUtil.isEmpty(crops) && !ObjectUtil.isEmpty(crops.getFarmer())) {

					if (!ObjectUtil.isEmpty(crops.getFarmer().getVillage())) {
						criteria.createAlias("f.village", "v");
						criteria.add(Restrictions.eq("v.id", crops.getFarmer().getVillage().getId()));
					}

					if (!ObjectUtil.isEmpty(crops.getFarmer().getCity())
							&& !ObjectUtil.isEmpty(crops.getFarmer().getCity().getLocality())) {
						criteria.createAlias("f.city", "c");
						criteria.createAlias("c.locality", "l");
						criteria.createAlias("l.state", "s");

						if (!ObjectUtil.isEmpty(crops.getFarmer().getStateId())) {
							criteria.add(Restrictions.eq("s.id",
									crops.getFarmer().getCity().getLocality().getState().getId()));
						}

						if (crops.getFarmer().getCity().getLocality().getId() > 0) {
							criteria.add(Restrictions.eq("l.id",
									crops.getFarmer().getCity().getLocality().getId()));
						}
					}
					if (crops.getFarmer().getId() > 0) {
						criteria.add(Restrictions.eq("f.id", crops.getFarmer().getId()));
					}

					if (crops.getFarmer().getBranchId() != null
							&& !StringUtil.isEmpty(crops.getFarmer().getBranchId())) {
						criteria.add(Restrictions.eq("f.branchId", crops.getFarmer().getBranchId()));
					}

				}
				if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
					if (!selectedStatus.equalsIgnoreCase("2")) {
						criteria.add(Restrictions.eq("f.status", Integer.valueOf(selectedStatus)));
					} else {
						criteria.add(Restrictions.isNotNull("f.status"));
					}
				} else {
					criteria.add(Restrictions.eq("f.status", 1));
				}
				pList.add(Projections.property("id"));
				pList.add(Projections.property("latitude"));
				pList.add(Projections.property("longitude"));
				pList.add(Projections.property("farmCode"));
				pList.add(Projections.property("f.firstName"));
				pList.add(Projections.property("farmName"));
				pList.add(Projections.property("id"));
				pList.add(Projections.property("f.id"));				
				criteria.setProjection(pList);
				 list = criteria.list();
			}else if (obj instanceof Farm && plottingType.equalsIgnoreCase("3")) {
				Session sessions = getSessionFactory().openSession();
				Query query = session.createSQLQuery("SELECT fmid,latitude,longitude,farmCode,firstName,farmName,farmrId,farmerId,ftype FROM (SELECT fmc.id AS fmid,fmc.LATITUDE AS latitude,fmc.LONGITUDE AS longitude,fm.FARM_CODE AS farmCode,fr.FIRST_NAME AS firstName,fm.FARM_NAME AS farmName,fmc.id AS farmrId,fr.id AS farmerId,	'2' AS ftype FROM	FARM_CROPS fmc	LEFT JOIN FARM fm ON fmc.FARM_ID = fm.id LEFT JOIN FARMER fr ON fm.FARMER_ID = fr.id WHERE	fr.STATUS = '1' UNION SELECT	frm.id AS fmid,	frm.LATITUDE AS latitude,	frm.LONGITUDE AS longitude,	frm.FARM_CODE AS farmCode,far.FIRST_NAME AS firstName,	frm.FARM_NAME AS farmName,	 frm.id AS farmrId,far.id AS farmerId,	'1' AS ftype FROM FARM frm INNER JOIN FARMER far ON frm.FARMER_ID = far.id WHERE far.STATUS = '1') tt");
				list = (List<Object[]>) query.list();
				sessions.flush();
				sessions.close();	
			}
		return list;
	}*/

	
	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = null;
		if (!StringUtil.isEmpty(selectedCrop)) {
			if (!StringUtil.isEmpty(selectedFarmer)) {
				hqlQuery = "select count(*) FROM Farmer f WHERE f.statusCode=0";
			} else {

				hqlQuery = "select count(f.id) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f left join fa.farmICSConversion fics WHERE f.statusCode=0 and fc.status=1 AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
				params.put("selectedCrop", Long.valueOf(selectedCrop));
			}

		} else {
			hqlQuery = "select count(*) FROM Farmer f WHERE f.statusCode=0";
		}
		
		
		if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
			if(!selectedStatus.equalsIgnoreCase("2")){
				hqlQuery += " and f.status=:selectedStatus";
			params.put("selectedStatus", Integer.valueOf(selectedStatus));
			}else{
				hqlQuery += " and f.status is not null";
			}
		}else{
			hqlQuery += " and f.status=1";
		}
			
		
		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}

		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}

		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id =:farmer";
			params.put("farmer", Long.valueOf(selectedFarmer));

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Integer val = 0;
		Object obj = query.uniqueResult();
		if (obj != null && ObjectUtil.isLong(obj)) {
			val = ((Long) obj).intValue();
		}
		return val;
	}
	
	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus,String selectedFarm) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = "SELECT  SUM(fc.cultiArea),sum(fc.actualSeedYield),sum(fc.estimatedYield) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fc.farm.farmer f left join fa.farmICSConversion fics WHERE  fc.status=1";

		
		if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
			if(!selectedStatus.equalsIgnoreCase("2")){
				hqlQuery += " and f.status=:selectedStatus";
			params.put("selectedStatus", Integer.valueOf(selectedStatus));
			}else{
				hqlQuery += " and f.status is not null";
			}
		}else{
			hqlQuery += " and f.status=1";
		}
		
		if (!StringUtil.isEmpty(selectedState) && selectedState != null) {
			hqlQuery += " AND f.village.city.locality.state.id =:selectedState";
			params.put("selectedState", Long.valueOf(selectedState));

		}
		if (!StringUtil.isEmpty(selectedLocality) && selectedLocality != null) {
			hqlQuery += " AND f.city.locality.id =:selectedLocality";
			params.put("selectedLocality", Long.valueOf(selectedLocality));

		}
		if (!StringUtil.isEmpty(selectedTaluk) && selectedTaluk != null) {
			hqlQuery += " AND f.city.id =:selectedTaluk";
			params.put("selectedTaluk", Long.valueOf(selectedTaluk));

		}
		if (!StringUtil.isEmpty(selectedVillage) && selectedVillage != null) {
			hqlQuery += " AND f.village.id =:selectedVillage";
			params.put("selectedVillage", Long.valueOf(selectedVillage));

		}

		if (!StringUtil.isEmpty(selectedCrop) && selectedCrop != null) {
			hqlQuery += " AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(selectedCrop));

		}

		if (!StringUtil.isEmpty(selectedFarmer) && selectedFarmer != null) {
			hqlQuery += " AND f.id =:farmer";
			params.put("farmer", Long.valueOf(selectedFarmer));

		}
		
		if (!StringUtil.isEmpty(selectedFarm) && selectedFarm != null) {
			hqlQuery += " AND fa.id =:farm";
			params.put("farm", Long.valueOf(selectedFarm));

		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}

		Object[] val = null;
		Object obj = query.uniqueResult();
		if (obj != null) {
			val = (Object[]) obj;
		}
		return val;

	}
	
	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenus() {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery("select distinct dsc FROM DynamicFeildMenuConfig dsc join fetch dsc.dynamicSectionConfigs dss left join fetch dss.section ds left join fetch ds.languagePreferences join fetch dsc.dynamicFieldConfigs  dc  join fetch dc.field ff   left join fetch ff.languagePreferences join fetch ff.dynamicSectionConfig sec join fetch dc.field ORDER BY dsc.order ASC");

		List<DynamicFeildMenuConfig> result = query.list();
		session.flush();
		session.close();
		return result;
	}
	public List<Object[]> farmersByBranchandSeason(String season) {
		Session session = getSessionFactory().openSession();
		String queryString = "select count(f.ID),bm.`NAME`,bm.BRANCH_ID from farmer f inner join branch_master bm on bm.BRANCH_ID = f.BRANCH_ID where f.STATUS_CODE = 0 and f.SEASON_CODE = '"+season+"' group by bm.BRANCH_ID";
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}
	public List<Object[]> farmersByBranchandSeason(String branch,String season) {
		Session session = getSessionFactory().openSession();
		String queryString = "select count(f.ID),bm.`NAME`,bm.BRANCH_ID from farmer f inner join branch_master bm on bm.BRANCH_ID = f.BRANCH_ID where f.STATUS_CODE = 0 and f.BRANCH_ID = '"+branch+"'and f.SEASON_CODE = '"+season+"' group by bm.BRANCH_ID";
		SQLQuery query = session.createSQLQuery(queryString);
		List<Object[]> result = query.list();
		session.flush();
		session.close();
		return result;
	}
public List<Object[]> farmersByCountryAndSeason(String selectedBranch,String seasonCode) {
		
		HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}
		Session session = getSessionFactory().getCurrentSession();
		Query query;
		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),ctry.name,ctry.code,ctry.branchId from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.branchId = :branch and f.seasonCode = :season and f.statusCode = 0";
			if(!StringUtil.isEmpty(tenantId) && tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))
			hqlString += " AND f.status=1";
			hqlString += " group by ctry.code";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
			query.setParameter("season", seasonCode);
			
		} else {
			String hqlString = "SELECT count(f.id),ctry.name,ctry.code,ctry.branchId from Farmer f inner join f.village v  inner join v.city c inner join c.locality ld inner join ld.state s inner join s.country ctry where f.statusCode = 0 and f.seasonCode = :season";
			if(!StringUtil.isEmpty(tenantId) && tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID))
				hqlString += " AND f.status=1";
				hqlString += " group by ctry.code";
			query = session.createQuery(hqlString);
			query.setParameter("season", seasonCode);
		}
		List<Object[]> result = query.list();
		return result;
	}

public List<Object[]> farmersByGroupAndSeason(String selectedBranch,String seasonCode) {

	Session session = getSessionFactory().getCurrentSession();
	Query query;

		if (!StringUtil.isEmpty(selectedBranch)) {
			String hqlString = "SELECT count(f.id),s.name,s.code,f.branchId,s.id from Farmer f inner join f.samithi s where f.branchId = :branch and f.statusCode = 0 and f.seasonCode = :season group by s.id";
			query = session.createQuery(hqlString);
			query.setParameter("branch", selectedBranch);
			query.setParameter("season", seasonCode);
		} else {
			String hqlString = "SELECT count(f.id),s.name,s.code,f.branchId,s.id from Farmer f inner join f.samithi s where f.statusCode = 0 and f.seasonCode = :season group by s.id";
			query = session.createQuery(hqlString);
			query.setParameter("season", seasonCode);
		}
	
	List<Object[]> result = query.list();
	return result;
}

@Override
public List<Object[]> farmersByBranch(String branch) {
	Session session = getSessionFactory().openSession();
	String queryString = "select count(f.ID),bm.`NAME`,bm.BRANCH_ID from farmer f inner join branch_master bm on bm.BRANCH_ID = f.BRANCH_ID where f.BRANCH_ID= "+branch+" and f.STATUS_CODE = 0 group by bm.BRANCH_ID";
	SQLQuery query = session.createSQLQuery(queryString);
	List<Object[]> result = query.list();
	session.flush();
	session.close();
	return result;
}

@Override
public List<Object[]> findFarmerCountByGroupICS(String selectedBranch, String season) {
	String groupBy = " GROUP BY fic.icsType,f.samithi.name";
	Session session = getSessionFactory().getCurrentSession();
	Map<String, Object> params = new HashMap<String, Object>();
	String hqlQuery = "select count(f.id),CASE WHEN fic.icsType = 0 THEN 'IC-1' WHEN fic.icsType = 1 THEN 'IC-2' WHEN fic.icsType = 2 THEN 'IC-3' WHEN fic.icsType = 3 THEN 'Organic' ELSE 'Non-Certified' END,f.samithi.name,f.samithi.code FROM Farm fm INNER JOIN fm.farmICSConversion fic INNER JOIN  fm.farmer f WHERE f.status=1 and f.seasonCode = :season";
	if (!StringUtil.isEmpty(selectedBranch)) {
		hqlQuery += " AND f.branchId =:branch ";
		params.put("branch", selectedBranch);
		hqlQuery += " AND f.branchId =:branch ";
		params.put("season", season);
	}		
	hqlQuery += groupBy;
	Query query = session.createQuery(hqlQuery);
	for (String str : query.getNamedParameters()) {
		query.setParameter(str, params.get(str));
	}

	List<Object[]> result = query.list();
	return result;

}

public List<Object[]> populateFarmerCropCountChartByGroup(String seasonCode,String selectedics,String selectedBranch) {
	String hqlString;
	Session session = getSessionFactory().getCurrentSession();
	Query query = null;

	if (StringUtil.isEmpty(seasonCode)) {
		
			if(StringUtil.isEmpty(selectedics)){
				hqlString = "SELECT Count( f.id ),pp.name,pp.code from Farmer f inner join f.farms fa inner join f.samithi s inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0  group by pp.code ";
				
				query = session.createQuery(hqlString);
				List<Object[]> result = query.list();
				return result;
			}else{
			hqlString = "SELECT Count( f.id ),pp.name,pp.code FROM Farmer f inner join f.farms fa inner join f.samithi s inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 AND s.code= :ics  group by pp.code ";
			
			query = session.createQuery(hqlString);
			query.setParameter("ics", selectedics);
			List<Object[]> result = query.list();
			return result;
			}
	}
	else
	{
		if(!StringUtil.isEmpty(selectedics)){
			hqlString = "SELECT Count( f.id ),pp.name,pp.code FROM Farmer f inner join f.farms fa inner join f.samithi s inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 AND s.code= :ics and famcrps.cropSeason.code= :season  group by pp.code ";
			
			query = session.createQuery(hqlString);
			query.setParameter("ics", selectedics);
			query.setParameter("season", seasonCode);
			List<Object[]> result = query.list();
			return result;
			
		} else {
			hqlString = "SELECT Count( f.id ),pp.name,pp.code FROM Farmer f inner join f.farms fa inner join f.samithi s inner join fa.farmCrops famcrps inner join famcrps.procurementVariety pv inner join pv.procurementProduct pp where f.statusCode = 0 and famcrps.cropSeason.code= :season  group by pp.code ";
			query = session.createQuery(hqlString);
			query.setParameter("season", seasonCode);
			List<Object[]> result = query.list();
			return result;
		}
	}
		

	
}

@Override
public FarmIcsConversion findFarmIcsConversionByFarmSeasonScopeAndInspectionType(long selectedFarm, String season,String scope,String insType) {
	Object[] values = { selectedFarm, season, scope, insType };
	return (FarmIcsConversion) find(
			"FROM FarmIcsConversion fc WHERE fc.farm.id=? AND fc.season=? AND fc.scope=? AND fc.insType=?",values);
}

@Override
public List<Object[]> ListFarmerCropDetails(List<String> mainIds) {
	List<Object> finalLis = new ArrayList<>();
	Session session = getSessionFactory().openSession();
	// Query query = session.createQuery("SELECT fs from FarmCrops fr INNER
	// JOIN fr.seedTreatments fs WHERE fr.id=:ID");FROM Farmer fr WHERE fr.id in (:ids)
	Query query = session.createSQLQuery(
			"SELECT f.ID,fc.PROCUREMENT_CROPS_VARIETY_ID,IFNULL(fc.EST_YIELD/1000,0) AS Yield ,pp.`NAME`,fm.CREATED_USERNAME,sum(fd.TOTAL_LAND_HOLDING) FROM farm_crops fc inner JOIN farm fm ON fc.FARM_ID = fm.ID inner JOIN farmer f ON f.ID = fm.FARMER_ID  inner join procurement_variety pv on pv.ID=fc.PROCUREMENT_CROPS_VARIETY_ID inner join procurement_product pp on pp.ID=pv.PROCUREMENT_PRODUCT_ID inner join farm_detailed_info fd on fd.ID=fm.FARM_DETAILED_INFO_ID WHERE fc.CROP_CATEGORY=0 AND fm.STATUS=1 AND f.ID IN (:ids) GROUP BY f.ID");
	query.setParameterList("ids", mainIds);
	List list = query.list();
	List<Object[]> result = (List<Object[]>) query.list();
	session.flush();
	session.close();
	return result;
}

@Override
public List<Object[]> ListFarmerCropDetailsByFarmer(List<String> mainIds) {
	List<Object> finalLis = new ArrayList<>();
	Session session = getSessionFactory().openSession();
	// Query query = session.createQuery("SELECT fs from FarmCrops fr INNER
	// JOIN fr.seedTreatments fs WHERE fr.id=:ID");FROM Farmer fr WHERE fr.id in (:ids)
	Query query = session.createSQLQuery(
			"SELECT f.ID,f.FARMER_CODE,f.FIRST_NAME,f.LAST_NAME,fm.CREATED_USERNAME,sum( fd.TOTAL_LAND_HOLDING ) FROM farmer f	left JOIN farm fm ON fm.FARMER_ID = f.ID left JOIN farm_detailed_info fd ON fd.ID = fm.FARM_DETAILED_INFO_ID inner join village v on v.ID=f.VILLAGE_ID inner join warehouse w on w.ID=f.SAMITHI_ID WHERE fm.STATUS=1  AND  f.ID IN (:ids ) GROUP BY f.ID");
	query.setParameterList("ids", mainIds);
	List list = query.list();
	List<Object[]> result = (List<Object[]>) query.list();
	session.flush();
	session.close();
	return result;
}

@Override
public List<Object[]> listMaxTypez(List<Long> collect,String txnType) {
	Session session = getSessionFactory().openSession();
	SQLQuery query = session.createSQLQuery("select t.PARENT_ID,max(t.types) from (select fdv.PARENT_ID,fdv.FARMER_DYNAMIC_DATA_ID,count(distinct typez) as types from farmer_dynamic_field_value fdv join dynamic_fields_config df on df.COMPONENT_CODE =fdv.FIELD_NAME and TYPEZ is not  null and fdv.txn_type=:txnType and fdv.FARMER_DYNAMIC_DATA_ID in (:ids) group by fdv.PARENT_ID,fdv.FARMER_DYNAMIC_DATA_ID ) t group by t.PARENT_ID");
	query.setParameterList("ids", collect);
	query.setParameter("txnType", txnType);
	List<Object[]> list = query.list();
	session.flush();
	session.close();
	return list;

}

@Override
public List<FarmCrops> listOfCropsByFarmIdAndSeason(List<Long> id,long season) {
	Session session = getSessionFactory().openSession();
	Query query = session.createQuery(
			"FROM FarmCrops cd WHERE cd.farm.id in(:id) and cd.cropSeason.id=:season");
	query.setParameter("season", season);
	query.setParameterList("id", id);
	List result = query.list();
	session.flush();
	session.close();
	return result;
}

@Override
public List<Object[]> listOfFarmsByFarmer(Long farmerId) {
	// TODO Auto-generated method stub
	return list(
			"SELECT DISTINCT fm.farmer.farmerId,fm.farmer.firstName,fm.farmer.farmerCode,fm.id,fm.farmCode,fm.farmName from Farm fm  where fm.farmer.id='"
					+ farmerId + "' AND fm.farmer.refId is null AND fm.farmer.statusCode='" + ESETxnStatus.SUCCESS.ordinal()
					+ "'");
}


@Override
public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByRefId(String refId, String txnType,Long farmerDynamicDataId) {
	Object[] values = { refId, txnType ,farmerDynamicDataId};
	return list(
			"select fdfv FROM FarmerDynamicFieldsValue fdfv LEFT JOIN FETCH fdfv.dymamicImageData di LEFT JOIN FETCH  fdfv.followUps where fdfv.referenceId=? AND fdfv.txnType=? AND fdfv.farmerDynamicData.id=?",
			values);

}

@Override
public List<Object[]> findAmtAndQtyByProcurmentTraceability(String selectedBranch, String selectedState, String selectedSeason,
		Date sDate, Date eDate) {
	// TODO Auto-generated method stub
	Map<String, Object> params = new HashMap<String, Object>();
	Session session = getSessionFactory().getCurrentSession();
	String hqlQuery = "select pd.procurementProduct.name,sum(pd.netWeight),sum(pd.totalPricepremium),sum(pd.procurementTraceability.paymentAmount) from ProcurementTraceabilityDetails pd "
			+ "inner join pd.procurementTraceability p inner join p.farmer f "
			+ "WHERE pd.procurementProduct.name !=null and p.farmer.status=1 and p.farmer.statusCode=0";

	if (!StringUtil.isEmpty(sDate) && eDate != null) {
		hqlQuery += " AND pd.procurementTraceability.procurementDate BETWEEN :startDate AND :endDate";
		params.put("startDate", sDate);
		params.put("endDate", eDate);
	}

	if (!StringUtil.isEmpty(selectedBranch)) {

		hqlQuery += " AND pd.procurementTraceability.branchId=:branchId";
		params.put("branchId", selectedBranch);
	}

	if (!StringUtil.isEmpty(selectedSeason)) {

		hqlQuery += " AND pd.procurementTraceability.season=:season";
		params.put("season", selectedSeason);
	}

	if (!StringUtil.isEmpty(selectedState)) {

		hqlQuery += " AND f.village.city.locality.state.id=:state";
		params.put("state", Long.valueOf(selectedState));
	}
	hqlQuery += " GROUP BY pd.procurementProduct.name";
	Query query = session.createQuery(hqlQuery);
	for (String str : query.getNamedParameters()) {
		query.setParameter(str, params.get(str));
	}

	return query.list();
}

public List<DataLevel> listDataLevelByType(String type){
	return list("FROM DataLevel d where d.idendifier=?", type);
}

@Override
public SamithiIcs findSamithiIcsById(Long id) {
	// TODO Auto-generated method stub
	return (SamithiIcs) find("FROM SamithiIcs s WHERE s.id = ?", id);
}

@Override
public List<Object[]> listFarmInfoLatLon() {

	return list("SELECT f.id,f.farmCode,f.farmName,f.latitude,f.longitude from Farm f  where  f.status=1 ");
}


public List<Object[]> findCountOfDynamicDataByFarmerId(List<Long> fidLi,String season) {
	Session session = getSessionFactory().openSession();
	Query query = session.createSQLQuery("SELECT FARMER_ID,group_concat( concat( `name`, '-', `value` ) SEPARATOR ',' ) AS `ColumnName` FROM(SELECT fm.FARMER_ID,(CASE WHEN fdd.TXN_TYPE = 2001 THEN 'Land Preparation ' WHEN fdd.TXN_TYPE = 2002 THEN 'After Sowing ' WHEN fdd.TXN_TYPE = 2003 THEN 'Vegetative Growth phase I ' WHEN fdd.TXN_TYPE = 2004 THEN 'Vegetative Growth phase II ' WHEN fdd.TXN_TYPE = 2005 THEN 'Flowering and boll formation ' WHEN fdd.TXN_TYPE = 2006 THEN 'Harvesting ' ELSE '' END ) AS NAME,count( fdd.ID ) AS VALUE FROM farm_crops fc INNER JOIN farm fm ON fc.FARM_ID = fm.ID INNER JOIN farmer_dynamic_data fdd ON fdd.REFERENCE_ID = fc.ID  WHERE fm.FARMER_ID IN ( :ids ) AND fdd.TXN_TYPE in (2001,2002,2003,2004,2005,2006) AND fdd.SEASON =:season GROUP BY fm.FARMER_ID,fdd.TXN_TYPE ) tbl GROUP BY FARMER_ID");
	query.setParameter("season", season);
	query.setParameterList("ids", fidLi);
	List<Object[]> list = query.list();
	session.flush();
	session.close();
	return list;
}

public ESEAccount findEseAccountByFarmerId(String farmerId) {

	// HQL Query procurementProductId based constraints removed for fetching
	// single farmer
	// account
	return (ESEAccount) find("FROM ESEAccount ea WHERE ea.profileId=?", new Object[] { farmerId });
}

public List<Object[]> listLoanLedgerByEseAccountId(String accountId) {

	String query = "SELECT LL.TXN_TIME TXN_TIME,LL.RECEIPT_NO RECEIPT_NO,LL.LOAN_DESC LOAN_DESC,LL.ACTUAL_AMT ACTUAL_AMT FROM ESE_ACCOUNT EA "
			+ "INNER JOIN LOAN_LEDGER LL ON LL.FARMER_ID=EA.PROFILE_ID " + "WHERE LL.TXN_TYPE!='316' AND EA.PROFILE_ID='" + accountId
			+ "' ORDER BY LL.ID DESC;";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	return session.createSQLQuery(query).addScalar("TXN_TIME", StringType.INSTANCE)
			.addScalar("RECEIPT_NO", StringType.INSTANCE).addScalar("LOAN_DESC", StringType.INSTANCE)
			.addScalar("ACTUAL_AMT", DoubleType.INSTANCE).list();
}

public List<Object[]> listLoanLedgerByEseAccountId(String accountId, int startIndex, int limit) {

	String query = "SELECT LL.TXN_TIME TXN_TIME,LL.RECEIPT_NO RECEIPT_NO,LL.LOAN_DESC LOAN_DESC,LL.ACTUAL_AMT ACTUAL_AMT FROM ESE_ACCOUNT EA "
			+ "INNER JOIN LOAN_LEDGER LL ON LL.FARMER_ID=EA.PROFILE_ID " + "WHERE LL.TXN_TYPE!='316' AND EA.PROFILE_ID='" + accountId
			+ "' ORDER BY LL.ID DESC" + " LIMIT " + startIndex + "," + limit + ";";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	return session.createSQLQuery(query).addScalar("TXN_TIME", StringType.INSTANCE)
			.addScalar("RECEIPT_NO", StringType.INSTANCE).addScalar("LOAN_DESC", StringType.INSTANCE)
			.addScalar("ACTUAL_AMT", DoubleType.INSTANCE).list();
}

public ESEAccount findEseAccountById(Long id) {
	Object[] values = { id, ESEAccount.ACTIVE };
	return (ESEAccount) find("FROM ESEAccount fr WHERE fr.id = ? AND fr.status=?", values);
}

@Override
public List<Object[]> listFarmerInfoByProcurementWithTypez() {

	List<Object[]> result = list(
			"SELECT f.farmerId,f.firstName,f.farmerCode,f.lastName FROM  Farmer f where f.farmerId in (select DISTINCT(e.profileId) from LoanLedger ll INNER JOIN ll.account e)");

	return result;
}
@Override
public List<Object[]> listGroupInfoByProcurementWithTypez() {

	List<Object[]> result = list(
			"SELECT DISTINCT f.farmerId,f.firstName,f.farmerCode FROM  Farmer f where f.typez=2 AND f.farmerId in (select t.farmerId from AgroTransaction t)");

	return result;
}

public List<Object[]> listFarmerStatementByEseAccountId(String accountId) {

	String query = "SELECT LL.TXN_TIME TXN_TIME,LL.RECEIPT_NO RECEIPT_NO,LL.LOAN_DESC LOAN_DESC,LL.ACTUAL_AMT ACTUAL_AMT FROM ESE_ACCOUNT EA "
			+ "INNER JOIN LOAN_LEDGER LL ON LL.FARMER_ID=EA.PROFILE_ID " + "WHERE LL.TXN_TYPE!='701' AND EA.PROFILE_ID='" + accountId
			+ "' ORDER BY LL.ID DESC;";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	return session.createSQLQuery(query).addScalar("TXN_TIME", StringType.INSTANCE)
			.addScalar("RECEIPT_NO", StringType.INSTANCE).addScalar("LOAN_DESC", StringType.INSTANCE)
			.addScalar("ACTUAL_AMT", DoubleType.INSTANCE).list();
}

public List<Object[]> listFarmerStatementByEseAccountId(String accountId, int startIndex, int limit) {

	String query = "SELECT LL.TXN_TIME TXN_TIME,LL.RECEIPT_NO RECEIPT_NO,LL.LOAN_DESC LOAN_DESC,LL.ACTUAL_AMT ACTUAL_AMT FROM ESE_ACCOUNT EA "
			+ "INNER JOIN LOAN_LEDGER LL ON LL.FARMER_ID=EA.PROFILE_ID " + "WHERE LL.TXN_TYPE!='701' AND EA.PROFILE_ID='" + accountId
			+ "' ORDER BY LL.ID DESC" + " LIMIT " + startIndex + "," + limit + ";";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	return session.createSQLQuery(query).addScalar("TXN_TIME", StringType.INSTANCE)
			.addScalar("RECEIPT_NO", StringType.INSTANCE).addScalar("LOAN_DESC", StringType.INSTANCE)
			.addScalar("ACTUAL_AMT", DoubleType.INSTANCE).list();
}

@Override
public List<Object[]> listFarmerFilterWithLoanLedger() {
	// 0=Id,1=Farmer Id,2=Farmer Code,3=First Name,4=Last
	// name,5=surName,6=village name,7=Group name,8=Is certified
	// Farmer,9=Status,10=BranchId
	return list(
			"SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,f.surName from Farmer f where f.refId is null AND f.typez=1 AND f.farmerId In (select DISTINCT(e.profileId) from LoanLedger ll INNER JOIN ll.account e where ll.txnType <> 701) AND f.status='"
					+ Farm.Status.ACTIVE.ordinal() + "'");
}

@Override
public List<Object[]> listGroupFilterWithLoanLedger() {
	// 0=Id,1=Farmer Id,2=Farmer Code,3=First Name,4=Last
	// name,5=surName,6=village name,7=Group name,8=Is certified
	// Farmer,9=Status,10=BranchId
	return list(
			"SELECT f.id,f.farmerId,f.farmerCode,f.firstName,f.lastName,f.surName from Farmer f where f.refId is null AND f.typez=2 AND f.farmerId In (select DISTINCT(e.profileId) from LoanLedger ll INNER JOIN ll.account e where ll.txnType <> 701) AND f.status='"
					+ Farm.Status.ACTIVE.ordinal() + "'");
}

public List<Object[]> listFFBpurchaseAndFFBRepaymentAmt(String accountId) {

	String query = "SELECT (SELECT IFNULL(CAST(SUM(ll.ACTUAL_AMT) AS DECIMAL(30,2)),'0') FROM LOAN_LEDGER LL where LL.FARMER_ID='"+accountId+"' and TXN_TYPE='316') TOTAL_FRUIT_PURCHASE,"
			+ "(SELECT IFNULL(CAST(SUM(ll.ACTUAL_AMT) AS DECIMAL(30,2)),'0') FROM LOAN_LEDGER LL where LL.FARMER_ID='"+accountId+"' and TXN_TYPE='702') TOTAL_REPAYMENT from LOAN_LEDGER LL "
					+ "where LL.TXN_TYPE!='701' AND LL.FARMER_ID='"+accountId+"' GROUP BY LL.FARMER_ID;";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	return session.createSQLQuery(query).addScalar("TOTAL_FRUIT_PURCHASE", StringType.INSTANCE)
			.addScalar("TOTAL_REPAYMENT", StringType.INSTANCE).list();
}

public Farmer findFarmerInfoByFarmerId(String farmerId) {

	Object[] values = { farmerId };
	Farmer farmer = (Farmer) find("FROM Farmer fr WHERE  fr.farmerId = ?", values);
	return farmer;
}

public List<Object[]> findDateOfLoanLedger(Date startDate, Date endDate) {
	String query = "SELECT DISTINCT(ll.TXN_TIME) from loan_ledger ll Where ll.TXN_TIME BETWEEN :startDate AND :endDate GROUP BY date(TXN_TIME) ORDER BY ll.ID DESC";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	Query sqlQuery = session.createSQLQuery(query).setParameter("startDate", startDate).setParameter("endDate",
			endDate);
	List list = sqlQuery.list();
	return list;
}

public List<Object[]> findDateOfLoanLedger(Date startDate, Date endDate, int startIndex, int limit) {
	String query = "SELECT DISTINCT(ll.TXN_TIME) from loan_ledger ll Where ll.TXN_TIME BETWEEN :startDate AND :endDate GROUP BY date(TXN_TIME) ORDER BY ll.ID DESC"
			+ " LIMIT " + startIndex + "," + limit + ";";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	Query sqlQuery = session.createSQLQuery(query).setParameter("startDate", startDate).setParameter("endDate",
			endDate);
	List list = sqlQuery.list();
	return list;
}

public List<Object[]> listLoanLedgerByDate(String date,String branchId) {
	String query = "SELECT (SELECT IFNULL(CAST(SUM(ll.ACTUAL_AMT) AS DECIMAL(30,2)),'0') from loan_ledger ll where ll.TXN_TYPE='701' AND ll.BRANCH='"+branchId+"' AND ll.TXN_TIME='"
			+ date
			+ "' )AS LOAN_DIST,(SELECT IFNULL(CAST(SUM(ll.ACTUAL_AMT) AS DECIMAL(30,2)),'0') from loan_ledger ll where ll.TXN_TYPE IN ('702') AND ll.BRANCH='"+branchId+"' AND ll.TXN_TIME='"
			+ date + "' )AS LOAN_REPAY from loan_ledger ll GROUP BY ll.TXN_TYPE AND ll.TXN_TIME";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	SQLQuery sqlQuery = session.createSQLQuery(query);
	List list = sqlQuery.list();
	return list;
}

public List<Object[]> listLoanLedgerByDate(String date, int startIndex, int limit) {
	String query = "SELECT (SELECT IFNULL(CAST(SUM(ll.ACTUAL_AMT) AS DECIMAL(30,2)),'0') from loan_ledger ll where ll.TXN_TYPE='701' AND ll.TXN_TIME='"
			+ date
			+ "' )AS LOAN_DIST,(SELECT IFNULL(CAST(SUM(ll.ACTUAL_AMT) AS DECIMAL(30,2)),'0') from loan_ledger ll where ll.TXN_TYPE IN ('702') AND ll.TXN_TIME='"
			+ date + "' )AS LOAN_REPAY from loan_ledger ll GROUP BY ll.TXN_TYPE AND ll.TXN_TIME" + " LIMIT "
			+ startIndex + "," + limit + ";";
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	SQLQuery sqlQuery = session.createSQLQuery(query);
	List list = sqlQuery.list();
	return list;
}

public ESEAccount findAccountByFarmerLoanProduct(long farmerId) {

	// HQL Query procurementProductId based constraints removed for fetching
	// single farmer
	// account
	return (ESEAccount) find("SELECT c.account FROM Contract c WHERE c.farmer.id=?", new Object[] { farmerId });
}

@Override
public LoanInterest findLoanPercent(Long amt) {
	// TODO Auto-generated method stub
	Object[] values = { amt };
	return (LoanInterest) find("From LoanInterest li where ? between li.minRange and li.maxRange", values);
}

@Override
public LoanInterest findLoanRangeById(Long id) {
	// TODO Auto-generated method stub
	LoanInterest loanInterest = (LoanInterest) find("FROM LoanInterest li WHERE li.id = ?", id);
	return loanInterest;

}

public List<Farmer> listFarmerWithOutstandLoanBal(String villageCode) {
	
	Object[] values = { villageCode, ESETxnStatus.SUCCESS.ordinal() };
	
	return list(
			"FROM Farmer f WHERE f.village.code = ? AND f.statusCode = ? AND f.farmerId In (select e.profileId from ESEAccount e where e.outstandingLoanAmount != 0) ORDER BY f.firstName ASC",
			values);
}

@Override
public List<Object[]> listFarmerByLoanDistribution() {
	return list("SELECT DISTINCT ld.farmer.id,ld.farmer.farmerId,ld.farmer.farmerCode,ld.farmer.firstName,ld.farmer.lastName from LoanDistribution ld");
}

@Override
public List<byte[]> getImageByQuery(String methodName, Object[] parameter, String branchId) {
	List<byte[]> a=null;
	methodNameQ = methodName;
	Session session = getSessionFactory().openSession();
	if (branchId == null) {
		methodNameQ = methodName.replaceAll("<.*?>", "");

	} else {
		methodNameQ = methodNameQ.replaceAll("<", "").replaceAll(">", "").replaceAll("branchId",
				"'" + branchId + "'");
	}
	Query query = session.createSQLQuery(methodNameQ);
	if (parameter != null && parameter.length > 0) {
		i = 1;
		Arrays.asList(parameter).stream().forEach(u -> {
			if (methodNameQ.contains("param" + i)) {
				if (u != null && u.toString().contains(",")) {
					query.setParameterList("param" + i, Arrays.asList(u.toString().split(",")));
				} else {
					query.setParameter("param" + i, u);
				}
				i++;
			}
		});
	}
	List ls = query.list();
	session.flush();
	session.close();
	if (ls != null && ls.size() > 0) {
		return (List<byte[]>) (ls);
	} else {
		return a;
	}

}
public List<Long> listFarmerPrimaryId(Farmer f) {
	Criteria c = getSessionFactory().getCurrentSession().createCriteria(Farmer.class);
	c.createAlias("city", "c");
	c.createAlias("village", "v");
	//c.createAlias("samithi", "w");
	 c.createAlias("c.locality", "l");
     c.createAlias("l.state", "s");
     c.createAlias("farms", "fa",c.LEFT_JOIN);
     c.createAlias("fa.farmCrops", "fc",c.LEFT_JOIN);
     c.createAlias("fc.procurementVariety", "pv",c.LEFT_JOIN);
     c.createAlias("pv.procurementProduct", "pp",c.LEFT_JOIN);
	c.add(Restrictions.isNotNull("mobileNumber"));
	c.add(Restrictions.ne("mobileNumber", ""));
	if (!ObjectUtil.isEmpty(f)) {
		if (!StringUtil.isEmpty(f.getFirstName())) {
			c.add(Restrictions.like("firstName", f.getFirstName().trim(), MatchMode.ANYWHERE));
		}

		if (!StringUtil.isEmpty(f.getLastName())) {
			c.add(Restrictions.like("lastName", f.getLastName().trim(), MatchMode.ANYWHERE));
		}

		if (!StringUtil.isEmpty(f.getMobileNumber())) {
			c.add(Restrictions.like("mobileNumber", f.getMobileNumber().trim(), MatchMode.START));
		}

		if (!StringUtil.isEmpty(f.getVillageName())) {

			c.add(Restrictions.like("v.name", f.getVillageName().trim(), MatchMode.ANYWHERE));
		}

		if (!StringUtil.isEmpty(f.getCityName())) {

			c.add(Restrictions.like("c.name", f.getCityName().trim(), MatchMode.ANYWHERE));
		}

		if (!StringUtil.isEmpty(f.getFarmer_status())) {
			c.add(Restrictions.eq("status", Integer.valueOf(f.getFarmer_status())));
		}

		if (!StringUtil.isEmpty(f.getBranchId())) {
			c.add(Restrictions.eq("branchId", f.getBranchId()));
		}
	    if (f.getCity() != null && f.getCity().getLocality()  != null
                && f.getCity().getLocality().getName()!= null) {
            c.add(Restrictions.like("l.name", f.getCity().getLocality().getName().trim(), MatchMode.ANYWHERE));
        }
        if (f.getCity() != null && f.getCity().getLocality()  != null && f.getCity().getLocality().getState()!= null
                && f.getCity().getLocality().getState().getName()!= null) {
            c.add(Restrictions.like("s.name", f.getCity().getLocality().getState().getName().trim(), MatchMode.ANYWHERE));
        }

		if (!StringUtil.isEmpty(f.getFarmer_fpo())) {

			c.add(Restrictions.eq("w.id", Long.valueOf(f.getFarmer_fpo().trim())));
		}
		 if (!StringUtil.isEmpty(f.getCropNames())) {
             c.add(Restrictions.like("pp.name", f.getCropNames(), MatchMode.ANYWHERE));
         }

	}
	// c.add(Restrictions.eq("statusCode", ESETxnStatus.SUCCESS.ordinal()));

	return c.setProjection(Projections.projectionList().add(Projections.groupProperty("id"))).list();
}

@Override
public List<Long> listProfilePrimaryId(Agent agent) {
	Criteria c = getSessionFactory().getCurrentSession().createCriteria(Agent.class);
	if (!ObjectUtil.isEmpty(agent)) {
		if (!StringUtil.isEmpty(agent.getProfileId())) {
			c.add(Restrictions.like("profileId", agent.getProfileId()));
		}

		if (agent.getPersonalInfo() != null && (!StringUtil.isEmpty(agent.getPersonalInfo().getFirstName())
				|| !StringUtil.isEmpty(agent.getPersonalInfo().getLastName()))) {
			c.createAlias("personalInfo", "pi");
			if (agent.getPersonalInfo() != null && !StringUtil.isEmpty(agent.getPersonalInfo().getFirstName())) {
				c.add(Restrictions.like("pi.firstName", agent.getPersonalInfo().getFirstName()));
			}

			if (agent.getPersonalInfo() != null && !StringUtil.isEmpty(agent.getPersonalInfo().getLastName())) {
				c.add(Restrictions.like("pi.lastName", agent.getPersonalInfo().getLastName()));
			}
		}

		if (!StringUtil.isEmpty(agent.getMobileno())) {
			c.createAlias("contactInfo", "ci");
			c.add(Restrictions.like("ci.mobileNumber", agent.getMobileno(), MatchMode.START));
		}

		if (!StringUtil.isEmpty(agent.getFs_status())) {
			c.add(Restrictions.eq("status", Integer.valueOf(agent.getFs_status())));
		}

		if (!StringUtil.isEmpty(agent.getBranchId())) {
			c.add(Restrictions.eq("branchId", agent.getBranchId()));
		}

	}
	return c.setProjection(Projections.projectionList().add(Projections.property("id"))).list();
}

@Override
public List<Long> listWebUsersPrimaryId(User user) {
	Criteria c = getSessionFactory().getCurrentSession().createCriteria(User.class);
	if (!ObjectUtil.isEmpty(user)) {
		if (!StringUtil.isEmpty(user.getUsername())) {
			c.add(Restrictions.like("username", user.getUsername()));
		}

		if (user.getPersonalInfo() != null && (!StringUtil.isEmpty(user.getPersonalInfo().getFirstName())
				|| !StringUtil.isEmpty(user.getPersonalInfo().getLastName()))) {
			c.createAlias("personalInfo", "pi");
			if (user.getPersonalInfo() != null && !StringUtil.isEmpty(user.getPersonalInfo().getFirstName())) {
				c.add(Restrictions.like("pi.firstName", user.getPersonalInfo().getFirstName()));
			}

			if (user.getPersonalInfo() != null && !StringUtil.isEmpty(user.getPersonalInfo().getLastName())) {
				c.add(Restrictions.like("pi.lastName", user.getPersonalInfo().getLastName()));
			}
		}

		if (!StringUtil.isEmpty(user.getMobileno())) {
			c.createAlias("contactInfo", "ci");
			c.add(Restrictions.like("ci.mobileNumber", user.getMobileno(), MatchMode.START));
		}

		if (!StringUtil.isEmpty(user.getWu_status())) {
			c.add(Restrictions.eq("status", Integer.valueOf(user.getWu_status())));
		}

		if (!StringUtil.isEmpty(user.getBranchId())) {
			c.add(Restrictions.eq("branchId", user.getBranchId()));
		}
	}
	return c.setProjection(Projections.projectionList().add(Projections.property("id"))).list();
}


@Override
public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(Object obj, String selectedStatus,
		String plottingType) {

	ProjectionList pList = Projections.projectionList();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = null;
		List<Object[]> list = null;
		if (obj instanceof FarmCrops && plottingType.equalsIgnoreCase("2")) {
			criteria = session.createCriteria(FarmCrops.class);
			FarmCrops crops = (FarmCrops) obj;
			criteria.createAlias("farm", "fm");
			criteria.createAlias("fm.farmer", "f");
			criteria.createAlias("procurementVariety", "pv");
			criteria.createAlias("pv.procurementProduct", "pp");
			criteria.createAlias("f.village", "v");
			criteria.createAlias("f.city", "c");
			criteria.createAlias("c.locality", "l");
			criteria.createAlias("l.state", "s");
			
			if (!StringUtil.isEmpty(crops.getCropCode())) {				
				criteria.add(Restrictions.eq("pp.id", Long.valueOf(crops.getCropCode())));
			}

			if (!ObjectUtil.isEmpty(crops.getFarm()) && !ObjectUtil.isEmpty(crops.getFarm().getFarmer())) {

				if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getVillage())) {					
					criteria.add(Restrictions.eq("v.id", crops.getFarm().getFarmer().getVillage().getId()));
				}

				if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getCity())
						&& !ObjectUtil.isEmpty(crops.getFarm().getFarmer().getCity().getLocality())) {
					
					if (!ObjectUtil.isEmpty(crops.getFarm().getFarmer().getStateId())) {
						criteria.add(Restrictions.eq("s.id",
								crops.getFarm().getFarmer().getCity().getLocality().getState().getId()));
					}

					if (crops.getFarm().getFarmer().getCity().getLocality().getId() > 0) {
						criteria.add(
								Restrictions.eq("l.id", crops.getFarm().getFarmer().getCity().getLocality().getId()));
					}
				}
				if (crops.getFarm().getFarmer().getId() > 0) {
					criteria.add(Restrictions.eq("f.id", crops.getFarm().getFarmer().getId()));
				}

				if (crops.getFarm().getFarmer().getBranchId() != null
						&& !StringUtil.isEmpty(crops.getFarm().getFarmer().getBranchId())) {
					criteria.add(Restrictions.eq("f.branchId", crops.getFarm().getFarmer().getBranchId()));
				}

			}
			if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
				if (!selectedStatus.equalsIgnoreCase("2")) {
					criteria.add(Restrictions.eq("f.status", Integer.valueOf(selectedStatus)));
				} else {
					criteria.add(Restrictions.isNotNull("f.status"));
				}
			} else {
				criteria.add(Restrictions.eq("f.status", 1));
			}

			pList.add(Projections.property("id"));
			pList.add(Projections.property("latitude"));
			pList.add(Projections.property("longitude"));
			pList.add(Projections.property("fm.farmCode"));
			pList.add(Projections.property("f.firstName"));
			pList.add(Projections.property("fm.farmName"));
			pList.add(Projections.property("fm.id"));
			pList.add(Projections.property("f.id"));
			pList.add(Projections.property("s.id"));
			pList.add(Projections.property("l.id"));
			pList.add(Projections.property("c.id"));
			pList.add(Projections.property("v.id"));
			pList.add(Projections.property("pp.id"));			
			criteria.setProjection(pList);
			 list = criteria.list();
		} else if (obj instanceof Farm && plottingType.equalsIgnoreCase("1")) {
				criteria = session.createCriteria(Farm.class);					
				Farm crops = (Farm) obj;
				criteria.createAlias("farmer", "f");
				criteria.createAlias("f.village", "v");
				criteria.createAlias("f.city", "c");
				criteria.createAlias("c.locality", "l");
				criteria.createAlias("l.state", "s");
				//criteria.createAlias("procurementVariety", "pv");
				//criteria.createAlias("pv.procurementProduct", "pp");

				if (!ObjectUtil.isEmpty(crops) && !ObjectUtil.isEmpty(crops.getFarmer())) {

					if (!ObjectUtil.isEmpty(crops.getFarmer().getVillage())) {						
						criteria.add(Restrictions.eq("v.id", crops.getFarmer().getVillage().getId()));
					}

					if (!ObjectUtil.isEmpty(crops.getFarmer().getCity())
							&& !ObjectUtil.isEmpty(crops.getFarmer().getCity().getLocality())) {
						

						if (!ObjectUtil.isEmpty(crops.getFarmer().getStateId())) {
							criteria.add(Restrictions.eq("s.id",
									crops.getFarmer().getCity().getLocality().getState().getId()));
						}

						if (crops.getFarmer().getCity().getLocality().getId() > 0) {
							criteria.add(Restrictions.eq("l.id",
									crops.getFarmer().getCity().getLocality().getId()));
						}
					}
					if (crops.getFarmer().getId() > 0) {
						criteria.add(Restrictions.eq("f.id", crops.getFarmer().getId()));
					}

					if (crops.getFarmer().getBranchId() != null
							&& !StringUtil.isEmpty(crops.getFarmer().getBranchId())) {
						criteria.add(Restrictions.eq("f.branchId", crops.getFarmer().getBranchId()));
					}

				}
				if (!StringUtil.isEmpty(selectedStatus) && selectedStatus != null) {
					if (!selectedStatus.equalsIgnoreCase("2")) {
						criteria.add(Restrictions.eq("f.status", Integer.valueOf(selectedStatus)));
					} else {
						criteria.add(Restrictions.isNotNull("f.status"));
					}
				} else {
					criteria.add(Restrictions.eq("f.status", 1));
				}
				pList.add(Projections.property("id"));
				pList.add(Projections.property("latitude"));
				pList.add(Projections.property("longitude"));
				pList.add(Projections.property("farmCode"));
				pList.add(Projections.property("f.firstName"));
				pList.add(Projections.property("farmName"));
				pList.add(Projections.property("id"));
				pList.add(Projections.property("f.id"));
				pList.add(Projections.property("s.id"));
				pList.add(Projections.property("l.id"));
				pList.add(Projections.property("c.id"));
				pList.add(Projections.property("v.id"));
				//pList.add(Projections.property("p.id"));
				criteria.setProjection(pList);
				 list = criteria.list();
			}else if (obj instanceof Farm && plottingType.equalsIgnoreCase("3")) {
				Session sessions = getSessionFactory().openSession();
				Query query = session.createSQLQuery("SELECT fmid,latitude,longitude,farmCode,firstName,farmName,farmrId,farmerId,stateId,districtId,talukId,villageId,cropId,ftype FROM( "
						+ " SELECT fmc.id AS fmid,fmc.LATITUDE AS latitude,fmc.LONGITUDE AS longitude,fm.FARM_CODE AS farmCode,fr.FIRST_NAME AS firstName,fm.FARM_NAME AS farmName,fmc.id AS farmrId,fr.id AS farmerId,s.id as stateId,ld.id as districtId,c.id as talukId,fr.VILLAGE_ID as villageId,pp.id as cropId,'2' AS ftype "
						+ " FROM FARM_CROPS fmc LEFT JOIN FARM fm ON fmc.FARM_ID = fm.id LEFT JOIN FARMER fr ON fm.FARMER_ID = fr.id left join city c on fr.CITY_ID=c.id left join location_detail ld on c.LOCATION_ID=ld.id left join state s on ld.STATE_ID=s.id left join procurement_variety pv on fmc.PROCUREMENT_CROPS_VARIETY_ID=pv.id left join procurement_product pp on pv.PROCUREMENT_PRODUCT_ID=pp.id "
						+ "WHERE fr.STATUS = '1' UNION SELECT frm.id AS fmid,frm.LATITUDE AS latitude,frm.LONGITUDE AS longitude,frm.FARM_CODE AS farmCode, far.FIRST_NAME AS firstName,frm.FARM_NAME AS farmName,frm.id AS farmrId,far.id AS farmerId,s.id as stateId,ld.id as districtId,c.id as talukId,far.VILLAGE_ID as villageId,pp.id as cropId,'1' AS ftype  FROM FARM_CROPS fmcs left JOIN FARM frm ON fmcs.FARM_ID = frm.id left JOIN FARMER far ON frm.FARMER_ID = far.id "
						+ "left join city c on far.CITY_ID=c.id left join location_detail ld on c.LOCATION_ID=ld.id left join state s on ld.STATE_ID=s.id left join procurement_variety pv on fmcs.PROCUREMENT_CROPS_VARIETY_ID=pv.id left join procurement_product pp on pv.PROCUREMENT_PRODUCT_ID=pp.id WHERE far.STATUS = '1' ) tt");
				list = (List<Object[]>) query.list();
				sessions.flush();
				sessions.close();	
			}
		return list;
	}


@Override
public Object[] findFarmersCountFromLotTraceByLotNo(String selectedLotNo) {
	if(selectedLotNo!=null && !StringUtil.isEmpty(selectedLotNo)){
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	List query = session.createSQLQuery("select count(distinct ft.farmer_id),count(DISTINCT f.VILLAGE_ID) from farmer_traceability_data ft inner join farmer f on f.id=ft.FARMER_ID where ft.LOT_NO in("+selectedLotNo+")").list();
	return (Object[]) query.get(0);
	}else return null;
	

}

@Override
public List<Object[]> listFarmerFarmInfoByLotNoFromFarmerTraceabilityData(String selectedLotNo, String branch) {
	if(selectedLotNo!=null && !StringUtil.isEmpty(selectedLotNo)){
		Session session = getSessionFactory().openSession();
		
		String queryStr="SELECT f.id as farmer_id,fa.latitude,fa.LONGITUDE,fa.FARM_CODE,f.FIRST_NAME,fa.FARM_NAME,fa.id as farm_id,f.IS_FARMER_CERTIFIED FROM 	farmer_traceability_data ft INNER JOIN farmer f ON f.id = ft.FARMER_ID INNER JOIN farm fa ON fa.FARMER_ID = f.id WHERE fa.LONGITUDE is not null and fa.LONGITUDE<>'0' and fa.LONGITUDE<>'' and fa.LATITUDE is not null and fa.LATITUDE<>'0' and fa.LATITUDE<>'' and f.`STATUS`=1 and fa.`STATUS`=1 and	ft.LOT_NO IN ( :LOTNOS ) ";
		if(branch!=null && !StringUtil.isEmpty(branch)){
			queryStr+=" and ft.BRANCH_ID= :BRANCHID";
		}
		SQLQuery query = session.createSQLQuery(queryStr);
		query.setParameterList("LOTNOS", selectedLotNo.split(","));
		if(branch!=null && !StringUtil.isEmpty(branch)){
		query.setParameter("BRANCHID", branch); 	
		}
		List list = query.list();
		session.flush();
		session.close();
		return list;
		}else return null;
}




}

