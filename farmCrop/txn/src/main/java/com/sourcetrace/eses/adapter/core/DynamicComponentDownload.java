package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicMenuSectionMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class DynamicComponentDownload implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(DynamicComponentDownload.class.getName());
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ICertificationService certificationService;
	Map<String, Map<String, LinkedList<Object>>> folloFields = new HashMap<>();
	private Map<Long, String> fieldsList = new HashMap<>();
	private Map<String, List<Long>> rtefList = new HashMap();

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		LOGGER.info("--------------------Dynamic Component Download statrts--------------------------");
		String revisionNo = (String) reqData.get(TxnEnrollmentProperties.DYNAMIC_COMPONENT_DOWNLOAD_REVISION_NO);
		String folloRev = (String) reqData.get("followUpRevNo");

		if (StringUtil.isEmpty(revisionNo))
			revisionNo = "0";

		if (StringUtil.isEmpty(folloRev))
			folloRev = "0";
		fieldsList = new HashMap<>();
		rtefList = new HashMap<>();
		Map resp = new LinkedHashMap<>();
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		List<DynamicFeildMenuConfig> dynamicMenuList = farmerService.listDynamicMenusByRevNo(revisionNo,
				head.getBranchId(), head.getTenantId());
		folloFields = new HashMap<>();
		if (dynamicMenuList == null || dynamicMenuList.isEmpty()) {
			resp.put(TxnEnrollmentProperties.MENU_LIST, new Collection());
			resp.put(TxnEnrollmentProperties.DYNAMIC_COMPONENT_DOWNLOAD_REVISION_NO, "0");

		} else {

			Collection menuCollection = new Collection();

			menuCollection.setObject(dynamicMenuList.stream().map(menu -> {
				Object fieldObj = new Object();
				List<Data> menuData = new ArrayList<Data>();

				List<DynamicFieldConfig> menuFieldList = menu.getDynamicFieldConfigs().stream()
						.map(DynamicMenuFieldMap::getField).collect(Collectors.toList());
				rtefList = menuFieldList.stream().filter(u -> u.getReferenceId() != null)
						.collect(Collectors.groupingBy(u -> u.getDynamicSectionConfig().getSectionCode(),
								Collectors.mapping(DynamicFieldConfig::getReferenceId, Collectors.toList())));
				fieldsList = menuFieldList.stream().collect(Collectors.toMap(DynamicFieldConfig::getId,
						DynamicFieldConfig::getComponentName, (address1, address2) -> {

							return address1;
						}));

				Data menuId = new Data();
				menuId.setKey(TxnEnrollmentProperties.MENU_ID);
				menuId.setValue(!StringUtil.isEmpty(menu.getCode()) ? menu.getCode() : "");
				menuData.add(menuId);

				Data menuName = new Data();
				menuName.setKey(TxnEnrollmentProperties.MENU_NAME);
				menuName.setValue(!StringUtil.isEmpty(menu.getName()) ? menu.getName() : "");
				menuData.add(menuName);

				Data iconClass = new Data();
				iconClass.setKey(TxnEnrollmentProperties.MENU_ICON_CLASS);
				iconClass.setValue(!StringUtil.isEmpty(menu.getIconClass()) ? menu.getIconClass() : "");
				menuData.add(iconClass);

				Data entity = new Data();
				entity.setKey(TxnEnrollmentProperties.ENTITY);
				entity.setValue(!StringUtil.isEmpty(menu.getEntity()) ? menu.getEntity() : "");
				menuData.add(entity);

				Data menuOrder = new Data();
				menuOrder.setKey(TxnEnrollmentProperties.MENU_ORDER);
				menuOrder.setValue(
						!StringUtil.isEmpty(String.valueOf(menu.getOrder())) ? String.valueOf(menu.getOrder()) : "");
				menuData.add(menuOrder);

				Data isSeason = new Data();
				isSeason.setKey(TxnEnrollmentProperties.IS_SEASON);
				isSeason.setValue(!StringUtil.isEmpty(menu.getIsSeason()) ? String.valueOf(menu.getIsSeason()) : "");
				menuData.add(isSeason);

				Data txnType = new Data();
				txnType.setKey(TxnEnrollmentProperties.TXN_TYPE_ID);
				// txnType.setValue(!StringUtil.isEmpty(menu.getTxnType()) ?
				// menu.getTxnType() : "");
				txnType.setValue(!StringUtil.isEmpty(menu.getmTxnType()) ? menu.getmTxnType() : "");

				menuData.add(txnType);

				Data lang = new Data();
				lang.setKey(TransactionProperties.LANG_LIST);

				lang.setCollectionValue(
						getCollection(new ArrayList<LanguagePreferences>(menu.getLanguagePreferences())));

				menuData.add(lang);

				Data agentTy = new Data();
				agentTy.setKey(TxnEnrollmentProperties.AGENT_TYPE);
				agentTy.setValue(!StringUtil.isEmpty(menu.getAgentType()) ? menu.getAgentType() : "02");
				menuData.add(agentTy);
				Map sectMap = getSectionCollection(menu.getDynamicSectionConfigs(), folloFields);
				Data sectionList = new Data();
				sectionList.setKey(TxnEnrollmentProperties.SECTIONS);
				sectionList.setCollectionValue((Collection) sectMap.get("newAp"));
				menuData.add(sectionList);
				if (sectMap.containsKey("fw") && !((Map) sectMap.get("fw")).isEmpty()) {
					folloFields.putAll((Map) sectMap.get("fw"));
				}

				fieldObj.setData(menuData);
				return fieldObj;
			}).collect(Collectors.toList()));

			resp.put(TxnEnrollmentProperties.MENU_LIST, menuCollection);
			resp.put(TxnEnrollmentProperties.DYNAMIC_COMPONENT_DOWNLOAD_REVISION_NO,
					dynamicMenuList.get(0).getRevisionNo());

		}

		List<java.lang.Object[]> foolowUps = farmerService.listFDVSForFolloUp(agentId, folloRev);
		if (foolowUps == null || foolowUps.isEmpty()) {
			resp.put("followUpList", new Collection());
			resp.put("followUpRevNo", folloRev);
		} else {
			if (dynamicMenuList == null || dynamicMenuList.isEmpty()) {
				// List<DynamicMenuFieldMap> menuList =
				// farmerService.listDynamisMenubyscoreType(2);
				List<DynamicMenuFieldMap> menuList = farmerService.listDynamisMenubyscoreType();
				Map fieldss = getFieldCollection(new HashSet<>(menuList), folloFields);
				if (fieldss.containsKey("fw") && !((Map) fieldss.get("fw")).isEmpty()) {
					folloFields = (Map<String, Map<String, LinkedList<Object>>>) fieldss.get("fw");
				}
			}
			Map<String, List<java.lang.Object[]>> folloMap = foolowUps.stream()
					.collect(Collectors.groupingBy(uu -> uu[1].toString() + "_" + uu[2].toString() + "_"
							+ uu[9].toString() + "_" + uu[0].toString() + "_" + uu[8].toString() + "_" + uu[14] + "_"
							+ uu[15].toString() + "_" + uu[16].toString()));
			if (!foolowUps.isEmpty()) {
				folloRev = foolowUps.get(0)[13].toString();
			}
			Collection fwCOll = getFollowUpCollection(folloMap, folloFields);
			resp.put("followUpList", fwCOll);
			resp.put("followUpRevNo", folloRev);
		}

		return resp;
	}

	int i = 0;

	public Collection getFollowUpCollection(Map<String, List<java.lang.Object[]>> folloMap,
			Map<String, Map<String, LinkedList<Object>>> folloFields) {
		Collection menuCollection = new Collection();
		List<Object> listOffieldsObject = new ArrayList<Object>();
		folloMap.entrySet().stream().forEach(txn -> {
			List<Data> txnDatas = new ArrayList<Data>();
			String txnTypee = txn.getKey().split("_")[3].toString();
			Data isfollowup = new Data();
			isfollowup.setKey("isfollowup");
			isfollowup.setValue("1");
			txnDatas.add(isfollowup);

			Data txnId = new Data();
			txnId.setKey("fluptxnId");
			txnId.setValue(txn.getKey().split("_")[0].toString());
			txnDatas.add(txnId);

			Data txnType = new Data();
			txnType.setKey("txnTypeId");
			/*if (txn.getKey().split("_")[3].toString().equalsIgnoreCase("359")) {
				txnType.setValue("2010");
			} else {
				txnType.setValue(txn.getKey().split("_")[3].toString());
			}*/
			txnType.setValue(txn.getKey().split("_")[3].toString());
			txnDatas.add(txnType);

			Data txnDate = new Data();
			txnDate.setKey("txnDate");
			txnDate.setValue(txn.getKey().split("_")[1].toString());
			txnDatas.add(txnDate);

			Data refId = new Data();
			refId.setKey("refId");
			refId.setValue(txn.getKey().split("_")[2].toString());
			txnDatas.add(refId);

			String refnma = txn.getKey().split("_")[5] != null
					? txn.getKey().split("_")[5].toString() + "-" + txn.getKey().split("_")[4].toString()
					: txn.getKey().split("_")[4].toString();
			Data refName = new Data();
			refName.setKey("refName");
			refName.setValue(refnma);
			txnDatas.add(refName);

			Data village = new Data();
			village.setKey("village");
			village.setValue(txn.getKey().split("_")[7].toString());
			txnDatas.add(village);

			Data w = new Data();
			w.setKey("grp");
			w.setValue(txn.getKey().split("_")[6].toString());
			txnDatas.add(w);

			Collection sectionCollection = new Collection();
			List<Object> listOfsectiontObject = new ArrayList<Object>();

			Data sectionId = new Data();
			sectionId.setKey(TxnEnrollmentProperties.SECTION);
			sectionId.setValue("temp" + txn.getKey().split("_")[0].toString());

			Data sectionName = new Data();
			sectionName.setKey(TxnEnrollmentProperties.SECTION_NAME);
			sectionName.setValue("Follow Up Audit");

			List<Data> secData = new ArrayList<Data>();
			secData.add(isfollowup);
			secData.add(txnId);
			secData.add(sectionId);
			secData.add(sectionName);
			secData.add(txnType);

			Collection field = new Collection();
			List<Object> listFieldCol = new ArrayList<Object>();

			txn.getValue().forEach(feld -> {
				if (folloFields.containsKey(txnTypee) && folloFields.get(txnTypee).containsKey(feld[4].toString())) {
					LinkedList<Object> fieldObjlist = folloFields.get(txnTypee).get(feld[4].toString());
					LinkedList<Object> ObjectList = new LinkedList<>();
					i = 0;
					fieldObjlist.stream().forEach(datt -> {
						Object parentObj = new Object();
						List<Data> fieldDat = new ArrayList<Data>();
						Data isfollowupF = new Data();
						isfollowupF.setKey("isfollowup");
						isfollowupF.setValue("1");
						fieldDat.add(0, isfollowupF);

						Data txnIdF = new Data();
						txnIdF.setKey("fluptxnId");
						txnIdF.setValue(txn.getKey().split("_")[0].toString());
						fieldDat.add(1, txnIdF);
						String actionPlan = "";
						String deadlin = "";
						String fid = "";
						String answe = "";

						if (i == 0) {

							actionPlan = feld[10] != null ? feld[10].toString() : "'";
							fid = feld[12] != null ? feld[12].toString() : "'";
							answe = feld[5] != null ? feld[5].toString() : "'";
							if (feld[11] != null && feld[11].toString().contains("-")
									&& feld[11].toString().split("-").length == 2) {
								deadlin = DateUtil.monthNames[Integer
										.valueOf(feld[11].toString().split("-")[0].toString().trim())] + "-"
										+ feld[11].toString().split("-")[1].toString();
							} else if (feld[11] != null) {
								deadlin = feld[11].toString();
							}

						}

						Data fieldId = new Data();
						fieldId.setKey("actionPlan");
						fieldId.setValue(actionPlan);
						fieldDat.add(fieldId);

						Data fieldValue = new Data();
						fieldValue.setKey("deadline");
						fieldValue.setValue(deadlin);
						fieldDat.add(fieldValue);

						Data fieldAns = new Data();
						fieldAns.setKey("answer");
						fieldAns.setValue(answe);
						fieldDat.add(fieldAns);

						fieldDat.add(sectionId);
						fieldDat.add(sectionName);
						
						/*if (txn.getKey().split("_")[3].toString().equalsIgnoreCase("359")) {
							fieldDat.addAll(datt.getData().stream()
									.filter(datth -> (!datth.getKey().equals(TxnEnrollmentProperties.SECTION)
											&& !datth.getKey().equals(TxnEnrollmentProperties.SECTION_NAME)))
									.collect(Collectors.toList()));
							txnType.setValue("2010");
						} else {
							fieldDat.addAll(datt.getData().stream()
									.filter(datth -> (!datth.getKey().equals(TxnEnrollmentProperties.SECTION)
											&& !datth.getKey().equals(TxnEnrollmentProperties.SECTION_NAME)))
									.collect(Collectors.toList()));
						}*/
						fieldDat.addAll(datt.getData().stream()
								.filter(datth -> (!datth.getKey().equals(TxnEnrollmentProperties.SECTION)
										&& !datth.getKey().equals(TxnEnrollmentProperties.SECTION_NAME)))
								.collect(Collectors.toList()));

						
						Data fId = new Data();
						fId.setKey("fId");
						fId.setValue(fid);
						fieldDat.add(fId);
						i++;
						parentObj.setData(fieldDat);
						ObjectList.add(parentObj);
					});
					listFieldCol.addAll(ObjectList);
				}

			});
			field.setObject(listFieldCol);
			Data fieldss = new Data();
			fieldss.setKey(TxnEnrollmentProperties.FIELDS_LIST);
			fieldss.setCollectionValue(field);
			secData.add(fieldss);
			Object secObject = new Object();
			secObject.setData(secData);

			listOfsectiontObject.add(secObject);

			sectionCollection.setObject(listOfsectiontObject);
			Data secs = new Data();
			secs.setKey(TxnEnrollmentProperties.SECTIONS);
			secs.setCollectionValue(sectionCollection);
			txnDatas.add(secs);

			Object txnObjj = new Object();
			txnObjj.setData(txnDatas);
			listOffieldsObject.add(txnObjj);

		});

		menuCollection.setObject(listOffieldsObject);
		return menuCollection;
	}

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}

	public Map getFieldCollection(Set<DynamicMenuFieldMap> dynamicFieldMap,
			Map<String, Map<String, LinkedList<Object>>> folloFields) {
		Collection fieldCollection = new Collection();

		List<Object> listOffieldsObject = new ArrayList<Object>();
		SortedSet<DynamicMenuFieldMap> sortedMap = new TreeSet<>(Comparator.comparing((DynamicMenuFieldMap pp) -> pp.getMenu().getmTxnType()).thenComparing(DynamicMenuFieldMap::getOrder));
		sortedMap.addAll(dynamicFieldMap);
		sortedMap.forEach(u -> {
			DynamicFieldConfig dfields = u.getField();
			if (!ObjectUtil.isEmpty(dfields)
					&& (dfields.getIsMobileAvail() != null && (dfields.getFollowUp() != 1 && dfields.getFollowUp() != 2)
							&& (!dfields.getIsMobileAvail().equals("2") && !dfields.getIsMobileAvail().equals("6")
									&& !dfields.getIsMobileAvail().equals("4")))) {

				List<Data> fieldData = new ArrayList<Data>();

				Data compoType = new Data();
				compoType.setKey(TxnEnrollmentProperties.COMPONENT_TYPE);
				compoType.setValue(!StringUtil.isEmpty(dfields.getComponentType()) ? dfields.getComponentType() : "");
				fieldData.add(compoType);
				// defaultValue
				/*
				 * Data defaultValue = new Data();
				 * defaultValue.setKey(TxnEnrollmentProperties.DEFAULT_VALUE);
				 * defaultValue.setValue(!StringUtil.isEmpty(dfields.
				 * getDefaultValue()) ? dfields.getDefaultValue() : "");
				 * fieldData.add(defaultValue);
				 */

				Data compoLabel = new Data();
				compoLabel.setKey(TxnEnrollmentProperties.COMPONENT_LABEL);
				compoLabel.setValue(!StringUtil.isEmpty(dfields.getComponentName()) ? dfields.getComponentName() : "");
				fieldData.add(compoLabel);

				Data compoId = new Data();
				compoId.setKey(TxnEnrollmentProperties.COMPONENT_ID);
				compoId.setValue(!StringUtil.isEmpty(dfields.getCode()) ? dfields.getCode() : "");
				fieldData.add(compoId);

				Data tBoxType = new Data();
				tBoxType.setKey(TxnEnrollmentProperties.TEXTBOX_TYPE);
				tBoxType.setValue(!StringUtil.isEmpty(dfields.getTextBoxType()) ? dfields.getTextBoxType() : "");
				fieldData.add(tBoxType);

				/*
				 * Data beforeIns = new Data();
				 * beforeIns.setKey(TxnEnrollmentProperties.BEFORE_INSERT);
				 * beforeIns.setValue(!StringUtil.isEmpty(dfields.
				 * getBeforeInsert()) ? dfields.getBeforeInsert() : "");
				 * fieldData.add(beforeIns);
				 * 
				 * Data afterIns = new Data();
				 * afterIns.setKey(TxnEnrollmentProperties.AFTER_INSERT);
				 * afterIns.setValue(!StringUtil.isEmpty(dfields.getAfterInsert(
				 * )) ? dfields.getAfterInsert() : ""); fieldData.add(afterIns);
				 */

				/*
				 * Data beforeIns = new Data();
				 * beforeIns.setKey(TxnEnrollmentProperties.FIELD_BEFORE_INSERT)
				 * ; beforeIns.setValue(!StringUtil.isEmpty(dfields.
				 * getmBeforeInsert()) ? dfields.getmBeforeInsert() : "");
				 * fieldData.add(beforeIns);
				 * 
				 * Data afterIns = new Data();
				 * afterIns.setKey(TxnEnrollmentProperties.FIELD_AFTER_INSERT);
				 * afterIns.setValue(!StringUtil.isEmpty(dfields.getmAfterInsert
				 * ()) ? dfields.getmAfterInsert() : "");
				 * fieldData.add(afterIns);
				 */

				Data beforeIns = new Data();
				beforeIns.setKey(TxnEnrollmentProperties.FIELD_BEFORE_INSERT);
				beforeIns.setValue(!StringUtil.isEmpty(u.getmBeforeInsert()) ? u.getmBeforeInsert() : "");
				fieldData.add(beforeIns);

				Data afterIns = new Data();
				afterIns.setKey(TxnEnrollmentProperties.FIELD_AFTER_INSERT);
				afterIns.setValue(!StringUtil.isEmpty(u.getmAfterInsert()) ? u.getmAfterInsert() : "");
				fieldData.add(afterIns);

				Data dataFormat = new Data();
				dataFormat.setKey(TxnEnrollmentProperties.DATA_FORMAT);
				dataFormat.setValue(!StringUtil.isEmpty(dfields.getDataFormat()) ? dfields.getDataFormat() : "");
				fieldData.add(dataFormat);

				Data isMandatory = new Data();
				isMandatory.setKey(TxnEnrollmentProperties.IS_MANDATORY);
				isMandatory.setValue(!StringUtil.isEmpty(dfields.getIsRequired()) ? dfields.getIsRequired() : "");
				fieldData.add(isMandatory);

				Data validation = new Data();
				validation.setKey(TxnEnrollmentProperties.VALID_TYPE);
				validation.setValue(!StringUtil.isEmpty(dfields.getValidation()) ? dfields.getValidation() : "");
				fieldData.add(validation);

				Data maxLen = new Data();
				maxLen.setKey(TxnEnrollmentProperties.MAXLENGTH);
				maxLen.setValue(
						!StringUtil.isEmpty(dfields.getComponentMaxLength()) ? dfields.getComponentMaxLength() : "");
				fieldData.add(maxLen);

				Data minLen = new Data();
				minLen.setKey(TxnEnrollmentProperties.MIN_LENGTH);
				minLen.setValue(!StringUtil.isEmpty(dfields.getMinLen()) ? dfields.getMinLen() : "");
				fieldData.add(minLen);

				Data isDependency = new Data();
				isDependency.setKey(TxnEnrollmentProperties.IS_DEPENDENCY);
				isDependency.setValue(!StringUtil.isEmpty(dfields.getIsDependency()) ? dfields.getIsDependency() : "");
				fieldData.add(isDependency);

				Data dependentField = new Data();
				dependentField.setKey(TxnEnrollmentProperties.DEPENDENT_FIELD);
				dependentField
						.setValue(!StringUtil.isEmpty(dfields.getDependencyKey()) ? dfields.getDependencyKey() : "");
				fieldData.add(dependentField);

				Data catType = new Data();
				catType.setKey(TxnEnrollmentProperties.CATLOGUE_TYPE);
				catType.setValue(
						!StringUtil.isEmpty(dfields.getCatalogueType()) && !ObjectUtil.isEmpty(dfields.getAccessType())
								? dfields.getCatalogueType() + "|" + dfields.getAccessType() : "");
				fieldData.add(catType);

				if (dfields.getParentActKey() != null && !StringUtil.isEmpty(dfields.getParentActKey())
						&& (dfields.getFollowUp() == 1 || dfields.getFollowUp() == 2)) {
					dfields.setParentDependencyKey(dfields.getParentDependencyKey() != null
							&& !StringUtil.isEmpty(dfields.getParentDependencyKey())
									? dfields.getParentDependencyKey() + "," + dfields.getParentActKey()
									: dfields.getParentActKey());
					dfields.setParentDependencyKey(Arrays.asList(dfields.getParentDependencyKey().split(",")).stream()
							.distinct().collect(Collectors.joining(",")));
				}

				Data parentDependent = new Data();
				parentDependent.setKey(TxnEnrollmentProperties.PARENT_DEPEND);
				parentDependent.setValue(
						!StringUtil.isEmpty(dfields.getParentDependencyKey()) ? dfields.getParentDependencyKey() : "");
				fieldData.add(parentDependent);

				if (dfields.getParentActField() != null && !StringUtil.isEmpty(dfields.getParentActField())
						&& (dfields.getFollowUp() == 1 || dfields.getFollowUp() == 2)) {
					Data parentField = new Data();
					parentField.setKey(TxnEnrollmentProperties.PARENT_FIELD);
					parentField.setValue(dfields.getParentActField());
					fieldData.add(parentField);
				} else {
					Data parentField = new Data();
					parentField.setKey(TxnEnrollmentProperties.PARENT_FIELD);
					parentField.setValue(
							!ObjectUtil.isEmpty(dfields.getParentDepen()) ? dfields.getParentDepen().getCode() : "");
					fieldData.add(parentField);
				}

				Data isOth = new Data();
				isOth.setKey(TxnEnrollmentProperties.IS_OTHER);
				isOth.setValue(String.valueOf(dfields.getIsOther()));
				fieldData.add(isOth);

				if (dfields.getParentActKey() != null && !StringUtil.isEmpty(dfields.getParentActKey())
						&& (dfields.getFollowUp() == 3)) {
					dfields.setCatDependencyKey(
							dfields.getCatDependencyKey() != null && !StringUtil.isEmpty(dfields.getCatDependencyKey())
									? dfields.getCatDependencyKey() + "," + dfields.getParentActKey()
									: dfields.getParentActKey());
				}

				Data catDepKey = new Data();
				catDepKey.setKey(TxnEnrollmentProperties.CAT_DEP_KEY);
				catDepKey.setValue(
						!StringUtil.isEmpty(dfields.getCatDependencyKey()) ? dfields.getCatDependencyKey() : "");
				fieldData.add(catDepKey);

				Data sectionId = new Data();
				sectionId.setKey(TxnEnrollmentProperties.SECTION);
				sectionId.setValue(!StringUtil.isEmpty(dfields.getDynamicSectionConfig())
						? dfields.getDynamicSectionConfig().getSectionCode() : "");
				fieldData.add(sectionId);

				Data txnType = new Data();
				txnType.setKey(TxnEnrollmentProperties.TXN_TYPE_ID);
				txnType.setValue(u.getMenu().getmTxnType());
				fieldData.add(txnType);

				Data listId = new Data();
				listId.setKey(TxnEnrollmentProperties.LIST_ID);
				if (StringUtil.isInteger(dfields.getComponentType()) && Integer
						.valueOf(dfields.getComponentType()) == DynamicFieldConfig.COMPONENT_TYPES.LIST.ordinal()) {
					listId.setValue(String.valueOf(dfields.getId()));
				} else {
					listId.setValue(!StringUtil.isEmpty(dfields.getReferenceId())
							? String.valueOf(dfields.getReferenceId()) : "");
				}

				fieldData.add(listId);

				Data fieldOrder = new Data();
				fieldOrder.setKey(TxnEnrollmentProperties.ORDER);
				fieldOrder.setValue(String.valueOf(u.getOrder()));
				fieldData.add(fieldOrder);

				Data blockId = new Data();
				blockId.setKey(TxnEnrollmentProperties.BLOCK_ID);
				blockId.setValue("0");
				fieldData.add(blockId);

				Data listMethod = new Data();
				listMethod.setKey(TxnEnrollmentProperties.LIST_METHOD_NAME);
				listMethod.setValue("");
				fieldData.add(listMethod);

				Data formula = new Data();
				formula.setKey(TxnEnrollmentProperties.FORMULA_DEPENDENCY);
				formula.setValue(!StringUtil.isEmpty(dfields.getFormula()) ? dfields.getFormula() : "");
				fieldData.add(formula);

				Data lang = new Data();
				lang.setKey(TransactionProperties.LANG_LIST);
				lang.setCollectionValue(
						getCollection(new ArrayList<LanguagePreferences>(dfields.getLanguagePreferences())));

				fieldData.add(lang);
				
				
				Data valueDep = new Data();
				valueDep.setKey(TxnEnrollmentProperties.VALUE_DEPENDANCY);
				valueDep.setValue(String.valueOf(dfields.getValueDependency()));
				fieldData.add(valueDep);

				Object secObject = new Object();
				secObject.setData(fieldData);
				if (dfields.getFollowUp() == 4 || dfields.getFollowUp() == 3) {
					Map<String, LinkedList<Object>> objLis = new HashMap();
					if (folloFields.containsKey(u.getMenu().getmTxnType())) {
						objLis = folloFields.get(u.getMenu().getmTxnType());
					}
				
					LinkedList<Object> obbList = new LinkedList<>();
				
					if (dfields.getFollowUp() == 4) {
						if (dfields.getParentActField() != null && objLis.containsKey(dfields.getParentActField())) {
							obbList = objLis.get(dfields.getParentActField());
						}
						
						/*if( u.getMenu().getmTxnType().equals("359")){
					
							folObject.getData().removeIf(dtt -> dtt.getKey().equals(TxnEnrollmentProperties.TXN_TYPE_ID));
							Data tnType = new Data();
							tnType.setKey(TxnEnrollmentProperties.TXN_TYPE_ID);
							tnType.setValue("2010");
							folObject.getData().add(tnType);
						}*/
						
						obbList.add(secObject);
						objLis.put(dfields.getParentActField(), obbList);

					} else {
						if (objLis.containsKey(dfields.getCode())) {
							obbList = objLis.get(dfields.getCode());
						}
						/*if( u.getMenu().getmTxnType().equals("359")){
							
							folObject.getData().removeIf(dtt -> dtt.getKey().equals(TxnEnrollmentProperties.TXN_TYPE_ID));
							Data tnType = new Data();
							tnType.setKey(TxnEnrollmentProperties.TXN_TYPE_ID);
							tnType.setValue("2010");
							folObject.getData().add(tnType);
						}*/
						
						
						obbList.add(secObject);
						objLis.put(dfields.getCode(), obbList);

					}

					folloFields.put(u.getMenu().getmTxnType(), objLis);
					if (dfields.getFollowUp() == 3) {
						listOffieldsObject.add(secObject);
					}
				} else {
					listOffieldsObject.add(secObject);
				}
			}
		});

		fieldCollection.setObject(listOffieldsObject);
		Map newAp = new HashMap<>();
		newAp.put("fieldsColl", fieldCollection);
		newAp.put("fw", folloFields);
		return newAp;
	}

	public Map getSectionCollection(Set<DynamicMenuSectionMap> dynamicSectionMap,
			Map<String, Map<String, LinkedList<Object>>> folloFields) {
		Collection sectionCollection = new Collection();
		List<Object> listOfsectiontObject = new ArrayList<Object>();
		dynamicSectionMap.forEach(u -> {
			DynamicSectionConfig section = u.getSection();
			if (!ObjectUtil.isEmpty(section) && section.getMobileFieldsSize() > 0) {
				Data sectionId = new Data();
				sectionId.setKey(TxnEnrollmentProperties.SECTION);
				sectionId.setValue(section.getSectionCode());

				Data sectionName = new Data();
				sectionName.setKey(TxnEnrollmentProperties.SECTION_NAME);
				sectionName.setValue(section.getSectionName());

				StringBuilder sb = new StringBuilder();
				StringBuilder aft = new StringBuilder();
				StringBuilder bef = new StringBuilder();
				if (section.getSectionCode().equals("00S19")) {
					System.out.println("ddd");
				}
				Data txnType = new Data();
				txnType.setKey(TxnEnrollmentProperties.TXN_TYPE_ID);
				txnType.setValue(u.getMenu().getmTxnType());

				/*
				 * Data beforeIns = new Data();
				 * beforeIns.setKey(TxnEnrollmentProperties.BEFORE_INSERT);
				 * beforeIns.setValue(!StringUtil.isEmpty(u.getSection().
				 * getmBeforeInsert()) ? u.getSection().getmBeforeInsert() :
				 * "");
				 * 
				 * Data afterIns = new Data();
				 * afterIns.setKey(TxnEnrollmentProperties.AFTER_INSERT);
				 * afterIns.setValue(
				 * !StringUtil.isEmpty(u.getSection().getmAfterInsert()) ?
				 * u.getSection().getmAfterInsert() : "");
				 */

				Data beforeIns = new Data();
				beforeIns.setKey(TxnEnrollmentProperties.BEFORE_INSERT);
				beforeIns.setValue(!StringUtil.isEmpty(u.getmBeforeInsert()) ? u.getmBeforeInsert() : "");

				Data afterIns = new Data();
				afterIns.setKey(TxnEnrollmentProperties.AFTER_INSERT);
				afterIns.setValue(!StringUtil.isEmpty(u.getmAfterInsert()) ? u.getmAfterInsert() : "");
				
				Data fieldOrder = new Data();
				fieldOrder.setKey(TxnEnrollmentProperties.SECTION_ORDER);
				fieldOrder.setValue(String.valueOf(u.getOrder()));

				Data blockId = new Data();
				blockId.setKey(TxnEnrollmentProperties.BLOCK_ID);
				if (!ObjectUtil.isEmpty(section.getDynamicFieldMenuConfig())) {
					blockId.setValue(String.valueOf(u.getMenu().getId()));
				} else {
					blockId.setValue("0");
				}

				Data lists = new Data();
				lists.setKey(TxnEnrollmentProperties.LISTS);
				Collection listCollection = new Collection();
				List<Object> listObject = new ArrayList<Object>();
				if (rtefList.containsKey(u.getSection().getSectionCode())) {
					List<Long> lsu = rtefList.get(u.getSection().getSectionCode());
					lsu.stream().distinct()

							.forEach(fieldsConfig -> {
								Data listId = new Data();
								listId.setKey(TxnEnrollmentProperties.LIST_ID);
								listId.setValue((fieldsConfig).toString());

								if (fieldsList == null || fieldsList.isEmpty()) {

									System.out.println("ccc");
								}

								Data listName = new Data();
								listName.setKey(TxnEnrollmentProperties.LIST_NAME);
								listName.setValue(fieldsList.containsKey(Long.valueOf(fieldsConfig))
										? fieldsList.get(Long.valueOf(fieldsConfig)) : "");

								List<Data> listData = new ArrayList<Data>();
								listData.add(listId);
								listData.add(listName);

								Object litObject = new Object();
								litObject.setData(listData);

								listObject.add(litObject);
							});
				}

				listCollection.setObject(listObject);
				lists.setCollectionValue(listCollection);

				Data fieldList = new Data();
				fieldList.setKey(TxnEnrollmentProperties.FIELDS_LIST);
				Map fieldss = getFieldCollection(
						u.getMenu()
								.getDynamicFieldConfigs().stream().filter(mm -> mm.getField().getDynamicSectionConfig()
										.getSectionCode().equals(section.getSectionCode()))
								.collect(Collectors.toSet()),
						folloFields);
				if (fieldss.containsKey("fw") && !((Map) fieldss.get("fw")).isEmpty()) {
					folloFields.putAll((Map) fieldss.get("fw"));
				}
				fieldList.setCollectionValue((Collection) fieldss.get("fieldsColl"));

				Data lang = new Data();
				lang.setKey(TransactionProperties.LANG_LIST);
				lang.setCollectionValue(
						getCollection(new ArrayList<LanguagePreferences>(section.getLanguagePreferences())));

				List<Data> secData = new ArrayList<Data>();
				secData.add(sectionId);
				secData.add(sectionName);
				secData.add(txnType);
				secData.add(afterIns);
				secData.add(beforeIns);
				secData.add(fieldOrder);
				secData.add(blockId);
				secData.add(lists);
				secData.add(lang);
				secData.add(fieldList);

				Object secObject = new Object();
				secObject.setData(secData);

				listOfsectiontObject.add(secObject);
			}
		});

		sectionCollection.setObject(listOfsectiontObject);
		Map newAp = new HashMap<>();
		newAp.put("newAp", sectionCollection);
		newAp.put("fw", folloFields);
		return newAp;

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
