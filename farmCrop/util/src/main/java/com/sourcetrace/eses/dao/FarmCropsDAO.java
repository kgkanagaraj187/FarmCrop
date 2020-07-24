package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.order.entity.txn.FarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendarDetail;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.OfflineFarmCropsEnrollment;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;

@Repository
@Transactional
public class FarmCropsDAO extends ESEDAO implements IFarmCropsDAO {
	
	@Autowired
	public FarmCropsDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#findFarmCropsByCode
	 * (java.lang.String)
	 */
	public FarmCrops findFarmCropsByCode(String code) {

		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.cropCode = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#findFarmCropsById
	 * (long)
	 */
	public FarmCrops findFarmCropsById(long id) {

		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * findFarmCropsMasterByCode(java.lang.String)
	 */
	public FarmCropsMaster findFarmCropsMasterByCode(String code) {

		return (FarmCropsMaster) find("FROM FarmCropsMaster fcm WHERE fcm.code = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * findFarmCropsMasterById (long)
	 */
	public FarmCropsMaster findFarmCropsMasterById(long id) {

		return (FarmCropsMaster) find("FROM FarmCropsMaster fcm WHERE fcm.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#listFarmCrops()
	 */
	public List<FarmCrops> listFarmCrops() {

		return list("FROM FarmCrops where status=1");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#listFarmCropsMaster
	 * ()
	 */
	public List<FarmCropsMaster> listFarmCropsMaster() {

		return list("FROM FarmCropsMaster");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * listFarmCropsByFarmId (long)
	 */
	public List<FarmCrops> listFarmCropsByFarmId(long id) {

		return list("FROM FarmCrops fc WHERE fc.farm.id = ? and fc.status=1", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * listFarmCropsFromFarmByFarmCode(java .lang.String)
	 */
	public List<FarmCrops> listFarmCropsFromFarmByFarmCode(String farmCode) {

		return list("FROM FarmCrops f WHERE f.farm.farmCode = ? and f.status=1", farmCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * findFarmCropbyFarmCropMasterIdFarmIdAndFarmerId(long, long, long)
	 */
	public FarmCrops findFarmCropbyFarmCropMasterIdFarmIdAndFarmerId(long farmCropMasterId, long farmId,
			long farmerId) {

		Object[] bind = { farmCropMasterId, farmId, farmerId };
		return (FarmCrops) find(
				"FROM FarmCrops fc WHERE fc.farmCropsMaster.id = ? AND fc.farm.id = ? AND fc.farm.farmer.id = ? ",
				bind);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#listOfflineFarmCrops
	 * (java.lang.String, long)
	 */
	public List<OfflineFarmCropsEnrollment> listOfflineFarmCrops(String farmCode, long farmerId) {

		Object[] values = { farmCode, farmerId };
		return list("FROM OfflineFarmCropsEnrollment ofce WHERE ofce.farmCode = ? AND ofce.farmer.id = ?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * listFarmCropsMasterByRevisionNo(long)
	 */
	public List<FarmCropsMaster> listFarmCropsMasterByRevisionNo(long revisionNo) {

		return list("FROM FarmCropsMaster fcm WHERE fcm.revisionNo>? ORDER BY fcm.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IFarmCropsDAO#
	 * findProcurementVarietyByCode(java.lang.String)
	 */
	public ProcurementVariety findProcurementVarietyByCode(String code) {

		return (ProcurementVariety) find("FROM ProcurementVariety pv WHERE pv.code=? ", code);
	}

	public FarmCrops findFarmCropbyProcurementVarietyIdFarmIdAndFarmerId(long procurementVarietyId, long farmId,
			long farmerId) {

		Object[] bind = { procurementVarietyId, farmId, farmerId };
		return (FarmCrops) find(
				"FROM FarmCrops fc WHERE fc.procurementVariety.id = ? AND fc.farm.id = ? AND fc.farm.farmer.id = ? ",
				bind);
	}
	
	public void removeFarmCropsById(long farmerCropsId)
	{

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("update FarmCrops fc set fc.status=:status, fc.revisionNo=:revNo WHERE fc.id = :id");
		query.setParameter("id", farmerCropsId);
		query.setParameter("revNo", DateUtil.getRevisionNumber());
		query.setParameter("status", Farmer.Status.DELETED.ordinal());
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public FarmCrops findByFarmIdandVarietyId(long farmId, Long varietyId, int cropCategory) {
		// TODO Auto-generated method stub
		Object[] values = {varietyId, farmId, cropCategory };
		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.procurementVariety.id = ? AND fc.farm.id = ? AND fc.cropCategory=? AND fc.status<>2", values);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listFarmCropsFarmInfo() {
		//0=FarmCrops id, 1=Farm Id, 2=Farm Code, 3=culti Area, 4= procurement variety code,5=Procurement Product Code,6=Procurement Product Name
		return list("SELECT fc.id,fa.id,fa.farmCode,fc.cultiArea,pv.code,pp.code,pp.name FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fc.procurementVariety pv INNER JOIN pv.procurementProduct pp where fc.status=1");
	}

	@Override
	public FarmCropCalendarDetail findFarmCropCalendarDetailByFarmId(long id) {
		// TODO Auto-generated method stub
		return (FarmCropCalendarDetail) find("FROM FarmCropCalendarDetail fccd WHERE fcc.farmCropCalendar.farm.id = ?", id);
	}
	
	@Override
	public FarmCrops findByFarmIdandVarietyIdAndSeason(long farmId, Long varietyId, int cropCategory,long season) {
		// TODO Auto-generated method stub
		Object[] values = {varietyId, farmId, cropCategory,season };
		return (FarmCrops) find("FROM FarmCrops fc WHERE fc.procurementVariety.id = ? AND fc.farm.id = ? AND fc.cropCategory=? And fc.cropSeason.id=? AND fc.status<>2", values);
	}

}
