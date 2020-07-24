package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Object;

@Component
public class SupplierProductsDownload implements ITxnAdapter {
	@Autowired
	private IProductDistributionService productDistributionService;

	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private ILocationService locationService;

	Collection productCollection = new Collection();
	List<Object> listOfProductObject = new ArrayList<Object>();

	Collection varietyCollection = new Collection();
	List<Object> listOfVarietyObject = new ArrayList<Object>();

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Map resp = new HashMap();
		productDistributionService.listSupplierProcurementProducts().stream()
				.forEach(supplierProcurementProductDetail -> {
					List<Data> procurementData = new LinkedList<Data>();

					Data productName = new Data();
					productName.setKey("productName");
					productName.setValue(supplierProcurementProductDetail.getProcurementProduct().getName());
					procurementData.add(productName);

					Data productCode = new Data();
					productCode.setKey("productCode");
					productCode.setValue(supplierProcurementProductDetail.getProcurementProduct().getCode());
					procurementData.add(productCode);

					Object procurementObject = new Object();
					procurementObject.setData(procurementData);

					listOfProductObject.add(procurementObject);
				});

		productDistributionService.listSupplierProcurementVariety().stream()
				.forEach(supplierProcurementProductDetail -> {
					List<Data> procurementData = new LinkedList<Data>();

					Data productName = new Data();
					productName.setKey("varietyName");
					productName.setValue(
							supplierProcurementProductDetail.getProcurementGrade().getProcurementVariety().getName());
					procurementData.add(productName);

					Data productCode = new Data();
					productCode.setKey("VarietyCode");
					productCode.setValue(
							supplierProcurementProductDetail.getProcurementGrade().getProcurementVariety().getCode());
					procurementData.add(productCode);

					Object procurementObject = new Object();
					procurementObject.setData(procurementData);

					listOfVarietyObject.add(procurementObject);
				});

		productCollection.setObject(listOfProductObject);
		varietyCollection.setObject(listOfVarietyObject);

		resp.put("products", productCollection);
		resp.put("varieties", varietyCollection);

		return resp;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

}
