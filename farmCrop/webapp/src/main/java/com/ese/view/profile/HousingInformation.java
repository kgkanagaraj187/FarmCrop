package com.ese.view.profile;

import java.net.URLEncoder;


import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class HousingInformation extends SwitchValidatorAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 801013554430491300L;
	private String calfProdData;
	private HousingInfo housingInfo;
	private IFarmerService farmerService;
	private ICatalogueService catalogueService;
	private String tabIndex;
    private String tabIndexZ;
	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}



	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}



	private String farmId;
	public String getFarmId() {
		return farmId;
	}



	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}



	public IFarmerService getFarmerService() {
		return farmerService;
	}



	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}



	public String getCalfProdData() {
		return calfProdData;
	}



	public void setCalfProdData(String calfProdData) {
		this.calfProdData = calfProdData;
	}



	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public String populateHousing()
	{
			if(!StringUtil.isEmpty(calfProdData)&&calfProdData.contains("@@@"))
			{
				String housingDatas[]=calfProdData.split("@@@");
				for(int i=0;i<housingDatas.length;i++)
				{
					HousingInfo housingInfo=new HousingInfo();
					String data[]=housingDatas[i].split("###");
					if(!StringUtil.isEmpty(data[0]))
					{
						housingInfo.setNoCowShad(String.valueOf(data[0]));
					}
					housingInfo.setHousingShadType(data[1]);
					housingInfo.setSpacePerCow(data[2]);
					if(!StringUtil.isEmpty(data[3]))
					{
					
						Farm farm=farmerService.findFarmById(Long.valueOf(data[3]));
						housingInfo.setFarm(farm);
					}
					if(!StringUtil.isEmpty(data[4]))
					{
						deleteHousingInfo(data[4]);
					}
					farmerService.addHousingInfo(housingInfo);
				}
				
			}
			else
			{
				if(!StringUtil.isEmpty(calfProdData))
				{
					String housingDatas[]=calfProdData.split("###");
					for(int i=0;i<housingDatas.length;i++)
					{
						deleteHousingInfo(housingDatas[i]);
					}
				}
								
			}
			return SUCCESS;
		}
	
	
	public void deleteHousingInfo(String housingIds)
	{
		
		if(!StringUtil.isEmpty(housingIds))
		{
			
			HousingInfo housingInfo=farmerService.findByHousingId(Long.valueOf(housingIds));
			farmerService.removeHousingInfo(housingInfo);
		}
		
	}
		
	
	public String getFarmDetailParams() {

        return "tabIndex=" + URLEncoder.encode(tabIndex) + "&id=" + getFarmId() + "&" + tabIndex;
    }



	public HousingInfo getHousingInfo() {
		return housingInfo;
	}



	public void setHousingInfo(HousingInfo housingInfo) {
		this.housingInfo = housingInfo;
	}



	public String getTabIndexZ() {
		return tabIndexZ;
	}



	public void setTabIndexZ(String tabIndexZ) {
		this.tabIndexZ = tabIndexZ;
	}
	

}
