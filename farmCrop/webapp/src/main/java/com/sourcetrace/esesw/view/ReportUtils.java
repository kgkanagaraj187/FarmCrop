package com.sourcetrace.esesw.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.Season;

public class ReportUtils {
	@Autowired
	private IClientService clientService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IProductDistributionService productDistributionService;

	private static List<Object[]> seasonList;

	public ReportUtils() {
		// seasonList = productDistributionService.listSeasons();
		System.out.println("Called");
	}


	public Map<String, String> getSeasonList() {
		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(seasonList)) {
			seasonList = farmerService.listSeasonCodeAndName();
		}
		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
		}
		return seasonMap;
	}

	public String getSeasonByCode(String code) {
		String season = getSeasonList().get(code);
		return season;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

}
