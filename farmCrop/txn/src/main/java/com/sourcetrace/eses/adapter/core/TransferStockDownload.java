package com.sourcetrace.eses.adapter.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.traceability.HeapData;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.txn.schema.Object;

@Component
public class TransferStockDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(TransferStockDownload.class.getName());
	@Autowired

	private IProductDistributionService productDistributionService;
	  @Autowired
	    private IAgentService agentService;

	  @Autowired
	    private IClientService clientService;
	  
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		LOGGER.info("---------------TRANSFER STOCK DOWNLOAD-------------------");
		/** HEADER VALUES **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		Map resp = new HashedMap();
        Agent agent = agentService.findAgentByProfileId(head.getAgentId());
        Collection collection = new Collection();
        Collection heapCollection=new Collection();
        String currentBranch = ObjectUtil.isEmpty(head) ? null : head.getBranchId();
        String seasonCode= clientService.findCurrentSeasonCodeByBranchId(currentBranch);
        if(ObjectUtil.isEmpty(agent.getProcurementCenter())){
        	resp.put(TransactionProperties.STOCK_LIST, collection);
    		resp.put(TransactionProperties.STOCK_REV, "0");
    		resp.put(TransactionProperties.HEAP_LIST, heapCollection);
    		resp.put(TransactionProperties.HEAP_REV, "0");
    		return resp;
        }
		List<PMT> stockList = productDistributionService.findTransferStockByGinner(Warehouse.GINNER,agent.getProcurementCenter().getId(),seasonCode);
		List<Object> catalogues = new ArrayList<Object>();
		if (!ObjectUtil.isEmpty(stockList)) {
			for (PMT pmt : stockList) {
				Data senderWarhouse = new Data();
				senderWarhouse.setKey(TransactionProperties.SENDER_QWH);
				senderWarhouse.setValue(pmt.getCoOperative().getCode());

				/*
				 * Data rec = new Data();
				 * rec.setKey(TransactionProperties.RECEIV_QWH);
				 * rec.setValue(cat.getPmtDetails().iterator().next().
				 * getCoOperative().getCode());
				 */
				Data recNo = new Data();
				recNo.setKey(TransactionProperties.TRANSFER_RECEIPT_NO);
				recNo.setValue(pmt.getMtntReceiptNumber());
				
				
				
				Data type=new Data();
				type.setKey(TransactionProperties.TRACE_STKDOWNLOAD_TYPE);
				type.setValue(String.valueOf(pmt.getStatusCode()));

				Collection pmtDetails = new Collection();
				List<Object> pmtDetai = new ArrayList<Object>();
				for (PMTDetail pmd : pmt.getPmtDetails()) {
					Data prodCode = new Data();
					prodCode.setKey(TransactionProperties.PRODUCT_CODE);
					prodCode.setValue(pmd.getProcurementProduct().getCode());

					Data prodName = new Data();
					prodName.setKey(TxnEnrollmentProperties.PRODUCT_NAME);
					prodName.setValue(pmd.getProcurementProduct().getName());

					Data bags = new Data();
					bags.setKey(TransactionProperties.BAGS);
					bags.setValue(String.valueOf(pmd.getMtntNumberOfBags()));

					Data icsCode=new Data();
					icsCode.setKey(TransactionProperties.ICS_CODE);
					icsCode.setValue(pmd.getIcs());
					
					Data weg = new Data();
					weg.setKey(TransactionProperties.WEIGHT);
					weg.setValue(String.valueOf(pmd.getMtntQuintalWeight()));
					
					List<Data> catalogueDataList = new ArrayList<Data>();
					catalogueDataList.add(prodCode);
					catalogueDataList.add(prodName);
					catalogueDataList.add(bags);
					catalogueDataList.add(icsCode);
					catalogueDataList.add(weg);
					Object catalogueMasterObject = new Object();
					catalogueMasterObject.setData(catalogueDataList);
					pmtDetai.add(catalogueMasterObject);
				}
				pmtDetails.setObject(pmtDetai);

				Data proList = new Data();
				proList.setKey(TransactionProperties.PRODUCT_LIST);
				proList.setCollectionValue(pmtDetails);
				List<Data> catalogueDataList = new ArrayList<Data>();
				catalogueDataList.add(senderWarhouse);
				// catalogueDataList.add(rec);
				catalogueDataList.add(recNo);
				catalogueDataList.add(type);
				catalogueDataList.add(proList);
				// catalogueDataList.add(catalogueYear);
				Object catalogueMasterObject = new Object();
				catalogueMasterObject.setData(catalogueDataList);
				catalogues.add(catalogueMasterObject);
			}
			collection.setObject(catalogues);
		}
		List<Object> heapObjList=new ArrayList<>();
	//	Collection tempCollection=new Collection();
		List<HeapData> heapData=productDistributionService.findHeapDataByGinner(Warehouse.GINNER,agent.getProcurementCenter().getId(),seasonCode);
		if(!ObjectUtil.isListEmpty(heapData)){
			for(HeapData hd:heapData){
				/*Data ginner=new Data();
				ginner.setKey(TransactionProperties.GINNER_CODE);
				ginner.setValue(hd.getGinning().getCode());*/
				Data prod=new Data();
				prod.setKey(TransactionProperties.PRD_CODE);
				prod.setValue(hd.getProcurementProduct().getCode());
				
				/*Data ics=new Data();
				ics.setKey(TransactionProperties.ICS);
				ics.setValue(hd.getIcs());
				*/
				Data heap=new Data();
				heap.setKey(TransactionProperties.HEAP_CODE);
				heap.setValue(hd.getCode());
				
				Data totStk=new Data();
				totStk.setKey(TransactionProperties.TOTAL_STOCK);
				totStk.setValue(String.valueOf(hd.getTotalStock()));
				
				Data lintStk=new Data();
				lintStk.setKey(TransactionProperties.LINT_STOCK);
				lintStk.setValue(String.valueOf(hd.getTotLintCotton()));
				
				Calendar currentDate = Calendar.getInstance();
				Calendar cal = (Calendar) currentDate.clone();
				cal.set(Calendar.YEAR, currentDate.get(Calendar.YEAR) - 3);
				DateFormat df = new SimpleDateFormat(DateUtil.DATE);
				String startDate = df.format(cal.getTime());
				String endDate = df.format(currentDate.getTime());
				List<java.lang.Object[]> ginningProcessList=productDistributionService.listGinningProcessByHeapProductAndGinning(hd.getCode(),hd.getProcurementProduct().getId(),agent.getProcurementCenter().getId(),startDate,endDate,seasonCode);
				Collection ginColl = new Collection();
				List<Object> ginningProcesses = new ArrayList<Object>();
				for(java.lang.Object[] gp:ginningProcessList){
					Data processDate=new Data();
					processDate.setKey(TransactionProperties.GINNING_DATE);
					processDate.setValue(String.valueOf(gp[0]));
					
					Data processQty=new Data();
					processQty.setKey(TransactionProperties.GINNING_STOCK);
					processQty.setValue(String.valueOf(gp[1]));

						List<Data> ginDataList = new ArrayList<Data>();
						ginDataList.add(processDate);
						ginDataList.add(processQty);

						Object langMasterObject = new Object();
						langMasterObject.setData(ginDataList);
						ginningProcesses.add(langMasterObject);
						ginColl.setObject(ginningProcesses);

					
				
				}
				
				Data heapGinn=new Data();
				heapGinn.setKey(TransactionProperties.GINNING_LIST);
				heapGinn.setCollectionValue(ginColl);
				
				List<Data> heapStockList=new ArrayList<>();
				//heapStockList.add(ginner);
				heapStockList.add(prod);
				//heapStockList.add(ics);
				heapStockList.add(heap);
				heapStockList.add(totStk);
				heapStockList.add(lintStk);
				heapStockList.add(heapGinn);
				
				Object heapObject=new Object();
			
				heapObject.setData(heapStockList);
				
				
				heapObjList.add(heapObject);
				
			}
			heapCollection.setObject(heapObjList);
			/*Data heapList=new Data();
			heapList.setKey(TransactionProperties.HEAP_LIST);
			heapList.setCollectionValue(heapCollection);*/
		}

		/** RESPONSE DATA **/
		resp.put(TransactionProperties.STOCK_LIST, collection);
		resp.put(TransactionProperties.STOCK_REV, "0");
		resp.put(TransactionProperties.HEAP_LIST, heapCollection);
		resp.put(TransactionProperties.HEAP_REV, "0");
		return resp;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

}
