package com.sourcetrace.eses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IFarmCropsDAO;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendarDetail;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.OfflineFarmCropsEnrollment;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;

@Service
@Transactional
public class FarmCropsService implements IFarmCropsService {

	@Autowired
	private IFarmCropsDAO farmCropsDAO;

	/**
	 * Sets the farm crops dao.
	 * 
	 * @param farmCropsDAO
	 *            the new farm crops dao
	 */
	public void setFarmCropsDAO(IFarmCropsDAO farmCropsDAO) {

		this.farmCropsDAO = farmCropsDAO;
	}

	/**
	 * Gets the farm crops dao.
	 * 
	 * @return the farm crops dao
	 */
	public IFarmCropsDAO getFarmCropsDAO() {

		return farmCropsDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmCropsService#addFarmCrops
	 * (com.ese.entity.profile.FarmCrops)
	 */
	public void addFarmCrops(FarmCrops farmCrops) {

		if (!ObjectUtil.isEmpty(farmCrops.getFarm())) {
			if (!ObjectUtil.isEmpty(farmCrops.getFarm().getFarmer())) {
				farmCrops.getFarm().getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
			}
			farmCrops.setStatus(Farmer.Status.ACTIVE.ordinal());
		}
		farmCropsDAO.save(farmCrops);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * addFarmCropsMaster(com.ese.entity.profile.FarmCropsMaster)
	 */
	public void addFarmCropsMaster(FarmCropsMaster farmCropsMaster) {

		farmCropsMaster.setRevisionNo(DateUtil.getRevisionNumber());
		farmCropsDAO.save(farmCropsMaster);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * editFarmCrops (com.ese.entity.profile.FarmCrops)
	 */
	public void editFarmCrops(FarmCrops farmCrops) {

		if (!ObjectUtil.isEmpty(farmCrops.getFarm())) {
			if (!ObjectUtil.isEmpty(farmCrops.getFarm().getFarmer())) {
				farmCrops.getFarm().getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
			}
		}
		farmCropsDAO.update(farmCrops);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * editFarmCropsMaster(com.ese.entity.profile.FarmCropsMaster)
	 */
	public void editFarmCropsMaster(FarmCropsMaster farmCropsMaster) {

		farmCropsMaster.setRevisionNo(DateUtil.getRevisionNumber());
		farmCropsDAO.update(farmCropsMaster);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * findFarmCropsByCode(java.lang.String)
	 */
	public FarmCrops findFarmCropsByCode(String code) {

		return farmCropsDAO.findFarmCropsByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * findFarmCropsById(long)
	 */
	public FarmCrops findFarmCropsById(long id) {

		return farmCropsDAO.findFarmCropsById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * findFarmCropsMasterByCode(java.lang.String)
	 */
	public FarmCropsMaster findFarmCropsMasterByCode(String code) {

		return farmCropsDAO.findFarmCropsMasterByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * findFarmCropsMasterById(long)
	 */
	public FarmCropsMaster findFarmCropsMasterById(long id) {

		return farmCropsDAO.findFarmCropsMasterById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * listFarmCrops ()
	 */
	public List<FarmCrops> listFarmCrops() {

		return farmCropsDAO.listFarmCrops();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * listFarmCropsMaster()
	 */
	public List<FarmCropsMaster> listFarmCropsMaster() {

		return farmCropsDAO.listFarmCropsMaster();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * removeFarmCrops (com.ese.entity.profile.FarmCrops)
	 */
	public void removeFarmCrops(FarmCrops farmCrops) {

		farmCropsDAO.delete(farmCrops);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * removeFarmCropsMaster(com.ese.entity.profile.FarmCropsMaster)
	 */
	public void removeFarmCropsMaster(FarmCropsMaster farmCropsMaster) {

		farmCropsDAO.delete(farmCropsMaster);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * listFarmCropsByFarmId(long)
	 */
	public List<FarmCrops> listFarmCropsByFarmId(long id) {

		return farmCropsDAO.listFarmCropsByFarmId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * findFarmCropbyFarmCropMasterIdFarmIdAndFarmerId(long, long, long)
	 */
	public FarmCrops findFarmCropbyFarmCropMasterIdFarmIdAndFarmerId(long farmCropMasterId, long farmId,
			long farmerId) {

		return farmCropsDAO.findFarmCropbyFarmCropMasterIdFarmIdAndFarmerId(farmCropMasterId, farmId, farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * listOfflineFarmCrops(java.lang.String, long)
	 */
	public List<OfflineFarmCropsEnrollment> listOfflineFarmCrops(String farmCode, long farmerId) {

		return farmCropsDAO.listOfflineFarmCrops(farmCode, farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * listFarmCropsFromFarmByFarmCode (java.lang.String)
	 */
	public List<FarmCrops> listFarmCropsFromFarmByFarmCode(String farmCode) {

		return farmCropsDAO.listFarmCropsFromFarmByFarmCode(farmCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * listFarmCropsMasterByRevisionNo (long)
	 */
	public List<FarmCropsMaster> listFarmCropsMasterByRevisionNo(long revisionNo) {

		return farmCropsDAO.listFarmCropsMasterByRevisionNo(revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmCropsService#
	 * findProcurementVarietyByCode (java.lang.String)
	 */
	public ProcurementVariety findProcurementVarietyByCode(String code) {

		return farmCropsDAO.findProcurementVarietyByCode(code);
	}

	public FarmCrops findFarmCropbyProcurementVarietyIdFarmIdAndFarmerId(long procurementVarietyId, long farmId,
			long farmerId) {

		return farmCropsDAO.findFarmCropbyProcurementVarietyIdFarmIdAndFarmerId(procurementVarietyId, farmId, farmerId);
	}

	public void removeFarmCropsById(long farmerCropsId) {

		farmCropsDAO.removeFarmCropsById(farmerCropsId);

	}

	@Override
	public FarmCrops findByFarmIdandVarietyId(long farmId, Long varietyId, int cropCategory) {
		// TODO Auto-generated method stub

		return farmCropsDAO.findByFarmIdandVarietyId(farmId, varietyId, cropCategory);
	}

	@Override
	public List<Object[]> listFarmCropsFarmInfo() {
		return farmCropsDAO.listFarmCropsFarmInfo();
	}

	@Override
	public FarmCropCalendarDetail findFarmCropCalendarDetailByFarmId(long id) {
		// TODO Auto-generated method stub
		return farmCropsDAO.findFarmCropCalendarDetailByFarmId(id);
	}
	
	@Override
	public FarmCrops findByFarmIdandVarietyIdAndSeason(long farmId, Long varietyId, int cropCategory ,long season) {
		// TODO Auto-generated method stub

		return farmCropsDAO.findByFarmIdandVarietyIdAndSeason(farmId, varietyId, cropCategory,season);
	}
}
