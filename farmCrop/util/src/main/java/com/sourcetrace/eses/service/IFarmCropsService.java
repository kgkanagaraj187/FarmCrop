package com.sourcetrace.eses.service;

import java.util.List;

import com.sourcetrace.eses.order.entity.txn.FarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;
import com.sourcetrace.esesw.entity.profile.OfflineFarmCropsEnrollment;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;


public interface IFarmCropsService {

    /**
     * List farm crops.
     * @return the list< farm crops>
     */
    public List<FarmCrops> listFarmCrops();

    /**
     * Adds the farm crops.
     * @param farmCrops the farm crops
     */
    public void addFarmCrops(FarmCrops farmCrops);

    /**
     * Edits the farm crops.
     * @param farmCrops the farm crops
     */
    public void editFarmCrops(FarmCrops farmCrops);

    /**
     * Removes the farm crops.
     * @param farmCrops the farm crops
     */
    public void removeFarmCrops(FarmCrops farmCrops);

    /**
     * Find farm crops by id.
     * @param id the id
     * @return the farm crops
     */
    public FarmCrops findFarmCropsById(long id);

    /**
     * Find farm crops by code.
     * @param code the code
     * @return the farm crops
     */
    public FarmCrops findFarmCropsByCode(String code);

    /**
     * List farm crops master.
     * @return the list< farm crops master>
     */
    public List<FarmCropsMaster> listFarmCropsMaster();

    /**
     * Adds the farm crops master.
     * @param farmCropsMaster the farm crops master
     */
    public void addFarmCropsMaster(FarmCropsMaster farmCropsMaster);

    /**
     * Edits the farm crops master.
     * @param farmCropsMaster the farm crops master
     */
    public void editFarmCropsMaster(FarmCropsMaster farmCropsMaster);

    /**
     * Removes the farm crops master.
     * @param farmCropsMaster the farm crops master
     */
    public void removeFarmCropsMaster(FarmCropsMaster farmCropsMaster);

    /**
     * Find farm crops master by id.
     * @param id the id
     * @return the farm crops master
     */
    public FarmCropsMaster findFarmCropsMasterById(long id);

    /**
     * Find farm crops master by code.
     * @param code the code
     * @return the farm crops master
     */
    public FarmCropsMaster findFarmCropsMasterByCode(String code);

    /**
     * List farm crops by farm id.
     * @param id the id
     * @return the list< farm crops>
     */
    public List<FarmCrops> listFarmCropsByFarmId(long farmId);

    /**
     * Find farm cropby farm crop master id farm id and farmer id.
     * @param farmCropMasterId the farm crop master id
     * @param farmId the farm id
     * @param farmerId the farmer id
     * @return the farm crops
     */
    public FarmCrops findFarmCropbyFarmCropMasterIdFarmIdAndFarmerId(long farmCropMasterId,
            long farmId, long farmerId);

    /**
     * List offline farm crops.
     * @param farmCode the farm code
     * @param farmerId the farmer id
     * @return the list< offline farm crops enrollment>
     */
    public List<OfflineFarmCropsEnrollment> listOfflineFarmCrops(String farmCode, long farmerId);

    /**
     * List farm crops from farm by farm code.
     * @param farmCode the farm code
     * @return the list< farm crops>
     */
    public List<FarmCrops> listFarmCropsFromFarmByFarmCode(String farmCode);

    /**
     * List farm crops master by revision no.
     * @param revisionNo the revision no
     * @return the list< farm crops master>
     */
    public List<FarmCropsMaster> listFarmCropsMasterByRevisionNo(long revisionNo);

    /**
     * Find procurement variety by code.
     * @param code
     * @return the procurement variety
     */
    public ProcurementVariety findProcurementVarietyByCode(String code);

    public FarmCrops findFarmCropbyProcurementVarietyIdFarmIdAndFarmerId(long procurementVarietyId,long farmId, long farmerId);

    public void removeFarmCropsById(long farmerCropsId);

	public FarmCrops findByFarmIdandVarietyId(long farmId, Long varietyId, int cropCategory);
	
	public List<Object[]> listFarmCropsFarmInfo();
	
	public FarmCropCalendarDetail findFarmCropCalendarDetailByFarmId(long id);
	
	public FarmCrops findByFarmIdandVarietyIdAndSeason(long farmId, Long varietyId, int cropCategory ,long season);
}
