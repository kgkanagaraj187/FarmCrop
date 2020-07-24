/*
 * CustomerProjectAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class CustomerProjectAction.
 */
@SuppressWarnings("serial")
public class CustomerProjectAction extends SwitchValidatorAction {
    private static final Logger logger = Logger.getLogger(CustomerProjectAction.class);

    protected static final String CREATE = "create";
    protected static final String DETAIL = "detail";
    protected static final String UPDATE = "update";
    protected static final String MAPPING = "mapping";
    protected static final String DELETE = "delete";
    protected static final String LIST = "list";
    protected static final String TITLE_PREFIX = "title.";
    protected static final String HEADING = "heading";
    protected static final int HOLDING_TYPE_DEFAULT_KEY = -1;
    protected static final int INSPECTION_TYPE_DEFAULT_KEY = -1;

    private IClientService clientService;
    private IUniqueIDGenerator idGenerator;
    private ICertificationService certificationService;

    private CustomerProject customerProject;
    private String id;
    private String customerId;
    private String customerUniqueId; /* This variable stores the Customer Table unique Id */
    private CustomerProject filter;
    private String selectedHoldingType;
    private String selectedInspectionType;
    private String selectedUnitCompoType;
    private String certificateCategoryId;
    private Map<String, String> holdingTypeList = new LinkedHashMap<String, String>();
    private Map<String, String> inspectionTypeList = new LinkedHashMap<String, String>();
    private Map<String, String> unitCompoTypeList = new LinkedHashMap<String, String>();
    private Map<String, String> icsList = new LinkedHashMap<String, String>();

    private String tabIndex = "#tabs-1";

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with

        CustomerProject filter = new CustomerProject();

        Customer customer = new Customer();
        customer.setCustomerId(request.getParameter("customerId"));
        filter.setCustomer(customer);

        if (!StringUtil.isEmpty(searchRecord.get("codeOfProject"))) {
            filter.setCodeOfProject(searchRecord.get("codeOfProject").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("nameOfProject"))) {
            filter.setNameOfProject(searchRecord.get("nameOfProject").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("unitNo"))) {
            filter.setUnitNo(searchRecord.get("unitNo").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("nameOfUnit"))) {
            filter.setNameOfUnit(searchRecord.get("nameOfUnit").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("locationOfUnit"))) {
            filter.setLocationOfUnit(searchRecord.get("locationOfUnit").trim());
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
    public String list() throws Exception {

        if (getCurrentPage() != null) {
            setCurrentPage(getCurrentPage());
        }

        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            customerProject = clientService.findCustomerProjectById(Long.valueOf(id));
            if (customerProject == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            command = UPDATE;
            view = DETAIL;
            request.setAttribute(HEADING, getText(DETAIL));
        } else {
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return view;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {

        if (customerProject == null) {
            command = "create";
            request.setAttribute(HEADING, getText(CREATE));
            setSelectedHoldingType(CustomerProject.INDIVIDUAL_FARMER);
            setSelectedUnitCompoType(CustomerProject.UNIT_COMPOSITION_TYPE_IC1);
            return INPUT;
        } else {

            Customer customer = clientService.findCustomerById(customerId);

            if (!ObjectUtil.isEmpty(customer)) {
                customerProject.setCustomer(customer);
            }

            CertificateStandard certificateStandard = null;
            if (!ObjectUtil.isEmpty(customerProject.getCertificateStandard()))
                certificateStandard = certificationService
                        .findCertificateStandardById(customerProject.getCertificateStandard()
                                .getId());
            customerProject.setCertificateStandard(certificateStandard);

            // customerProject.setTypeOfHolding(getIntValueFromString(selectedHoldingType,
            // HOLDING_TYPE_DEFAULT_KEY));
            customerProject.setInspection(getIntValueFromString(selectedInspectionType,
                    INSPECTION_TYPE_DEFAULT_KEY));
            // customerProject.setUnitCompFS(selectedUnitCompoType);

            String customerProjectCodeSeq = idGenerator.createCustomerProjectCode(customerProject
                    .getCustomer().getCustomerId());
            customerProject.setCodeOfProject(customerProjectCodeSeq);

            idGenerator.saveCustomerProjectSequence(customerProject.getCustomer().getCustomerId(),
                    customerProjectCodeSeq);

            clientService.addCustomerProject(customerProject);

            return "customerDetail";
        }
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        if (id != null && !id.equals("")) {
            customerProject = clientService.findCustomerProjectById(Long.valueOf(id));
            if (customerProject == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            // setSelectedHoldingType(String.valueOf(customerProject.getTypeOfHolding()));
            // setSelectedInspectionType(getText(customerProject.getInspection()));
            setSelectedInspectionType(String.valueOf(customerProject.getInspection()));
            // setSelectedUnitCompoType(customerProject.getUnitCompFS());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
            if (customerProject != null) {
                CustomerProject tempCustomerProject = clientService.findCustomerProjectById(Long
                        .valueOf(customerProject.getId()));
                if (tempCustomerProject == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
                tempCustomerProject.setNameOfProject(customerProject.getNameOfProject());
                tempCustomerProject.setNumberOfProjects(customerProject.getNumberOfProjects());
                tempCustomerProject.setReportNo(customerProject.getReportNo());
                tempCustomerProject.setUnitNo(customerProject.getUnitNo());
                tempCustomerProject.setNameOfUnit(customerProject.getNameOfUnit());
                tempCustomerProject.setLocationOfUnit(customerProject.getLocationOfUnit());
                // tempCustomerProject.setTypeOfHolding(getIntValueFromString(selectedHoldingType,
                // HOLDING_TYPE_DEFAULT_KEY));

                CertificateStandard certificateStandard = null;
                if (!ObjectUtil.isEmpty(customerProject.getCertificateStandard()))
                    certificateStandard = certificationService
                            .findCertificateStandardById(customerProject.getCertificateStandard()
                                    .getId());
                tempCustomerProject.setCertificateStandard(certificateStandard);

                tempCustomerProject.setInspection(getIntValueFromString(selectedInspectionType,
                        INSPECTION_TYPE_DEFAULT_KEY));
                tempCustomerProject.setIcsStatus(customerProject.getIcsStatus());
                // tempCustomerProject.setUnitCompFS(selectedUnitCompoType);
                tempCustomerProject.setNumberOfFarmers(customerProject.getNumberOfFarmers());
                tempCustomerProject.setArea(customerProject.getArea());
                clientService.editCustomerProject(tempCustomerProject);
            }
            request.setAttribute(HEADING, getText(LIST));
            return "customerDetail";
        }
        return super.execute();
    }

    /**
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception {

        if (this.getId() != null && !(this.getId().equals(EMPTY))) {
            customerProject = clientService.findCustomerProjectById(Long.valueOf(getId()));
            if (customerProject == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            if (!ObjectUtil.isEmpty(customerProject)) {

                boolean isProjectMappedWithFS = false;/*
                                                       * clientService
                                                       * .isProjectWithMappedFieldStaff
                                                       * (customerProject.getId());
                                                       */

                boolean isProjectMappedWithFarmer = clientService
                        .isProjectWithMappedFarmer(customerProject.getId());

                if (isProjectMappedWithFS) {
                    request.getSession().setAttribute("projectFieldStaffExist",
                            getText("projectFS.exist"));
                } else if (isProjectMappedWithFarmer) {
                    request.getSession().setAttribute("projectFarmerExist",
                            getText("projectFarmer.exist"));
                } else {
                    clientService.removeCustomerProject(customerProject);
                }
            }
        }

        request.setAttribute(HEADING, getText(LIST));
        return "customerDetail";

    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object record) {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        if (record instanceof CustomerProject) {
            CustomerProject customerProject = (CustomerProject) record;
            JSONArray rows = new JSONArray();
            rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">"
                    + customerProject.getCodeOfProject() + "</font>");
            rows.add(customerProject.getNameOfProject());
            rows.add(customerProject.getUnitNo());
            rows.add(customerProject.getNameOfUnit());
            rows.add(customerProject.getLocationOfUnit());
            rows.add("<button class='ic-del' onclick='deleteCustomerProject("
                    + customerProject.getId() + ")'>Delete</button");
            jsonObject.put("id", customerProject.getId());
            jsonObject.put("cell", rows);
            return jsonObject;

        }
        return jsonObject;
    }

    /**
     * Populate state.
     * @param populateResponce the populate responce
     * @return the string
     * @throws Exception the exception
     */

    protected String sendResponse(List<?> populateResponce) throws Exception {

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(populateResponce);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void reloadInspectionType() {

        try {
            inspectionTypeList.clear();
            String[] iTypes = getText("it").split(",");
            for (int i = 0; i < iTypes.length; i++) {
                inspectionTypeList.put(String.valueOf(i), iTypes[i]);
            }
        } catch (Exception e) {
            logger.info("Exception on reloadInspectionType : " + e.getMessage());
        }
    }

    private void reloadHoldingType() {

        try {
            holdingTypeList.clear();
            String[] hTypes = getText("th").split(",");
            for (int i = 0; i < hTypes.length; i++) {
                holdingTypeList.put(String.valueOf(i), hTypes[i]);
            }
        } catch (Exception e) {
            logger.info("Exception on reloadHoldingType : " + e.getMessage());
        }
    }

    private void reloadICSList() {

        try {
            icsList.clear();
            String[] icsStatusArray = getText("ics").split(",");
            for (int i = 0; i < icsStatusArray.length; i++) {
                icsList.put(String.valueOf(i), icsStatusArray[i]);
            }
        } catch (Exception e) {
            logger.info("Exception on reloadICSStatus : " + e.getMessage());
        }
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        /*
         * holdingTypeList.put(CustomerProject.INDIVIDUAL_FARMER,
         * getText(CustomerProject.INDIVIDUAL_FARMER));
         * holdingTypeList.put(CustomerProject.SMALL_HOLDER_GROUP,
         * getText(CustomerProject.SMALL_HOLDER_GROUP));
         */
        reloadHoldingType();

        /*
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_FIRST,
         * getText(CustomerProject.INSPECTION_TYPE_FIRST));
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_REPEAT,
         * getText(CustomerProject.INSPECTION_TYPE_REPEAT));
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_SUPPLEMENTRY,
         * getText(CustomerProject.INSPECTION_TYPE_SUPPLEMENTRY));
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_NEW_OR_PRODUCTION,
         * getText(CustomerProject.INSPECTION_TYPE_NEW_OR_PRODUCTION));
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_CHANGED,
         * getText(CustomerProject.INSPECTION_TYPE_CHANGED));
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_NEW_FIELD,
         * getText(CustomerProject.INSPECTION_TYPE_NEW_FIELD));
         * inspectionTypeList.put(CustomerProject.INSPECTION_TYPE_OTHERS,
         * getText(CustomerProject.INSPECTION_TYPE_OTHERS));
         */

        reloadInspectionType();

        /*
         * unitCompoTypeList.put(CustomerProject.UNIT_COMPOSITION_TYPE_IC1,
         * getText(CustomerProject.UNIT_COMPOSITION_TYPE_IC1));
         * unitCompoTypeList.put(CustomerProject.UNIT_COMPOSITION_TYPE_IC2,
         * getText(CustomerProject.UNIT_COMPOSITION_TYPE_IC2));
         * unitCompoTypeList.put(CustomerProject.UNIT_COMPOSITION_TYPE_ORGANIC,
         * getText(CustomerProject.UNIT_COMPOSITION_TYPE_ORGANIC));
         */

        reloadICSList();

        if (ObjectUtil.isEmpty(customerProject)) {
            return null;
        } else {
            customerProject.setInspection(getIntValueFromString(getSelectedInspectionType(),
                    INSPECTION_TYPE_DEFAULT_KEY));
            return customerProject;
        }
    }

    private int getIntValueFromString(String value, int defaultValue) {

        int intValue = defaultValue;
        try {
            if (StringUtil.isEmpty(value)) {
                intValue = defaultValue;
                value = String.valueOf(defaultValue);
            } else {
                intValue = Integer.valueOf(value);
            }
        } catch (Exception e) {
            intValue = defaultValue;
        }
        return intValue;
    }

    /**
     * Gets the certificate categories.
     * @return the certificate categories
     */
    public List<CertificateCategory> getCertificateCategories() {

        return certificationService.listCertificateCategory();
    }

    /**
     * Populate certificate standards.
     */
    @SuppressWarnings("unchecked")
    public void populateCertificateStandards() {

        JSONArray certificateStandardJSONArray = new JSONArray();
        if (!StringUtil.isEmpty(this.certificateCategoryId)) {
            List<CertificateStandard> certificateStandards = certificationService
                    .listCertificateStandardByCertificateCategoryId(Long
                            .valueOf(this.certificateCategoryId));
            for (CertificateStandard certificateStandard : certificateStandards) {
                JSONObject certificateStandardJSONObject = new JSONObject();
                certificateStandardJSONObject.put("csId", certificateStandard.getId());
                certificateStandardJSONObject.put("csName", certificateStandard.getName());
                certificateStandardJSONArray.add(certificateStandardJSONObject);
            }
        }
        printAjaxResponse(certificateStandardJSONArray, "text/html");
    }

    /**
     * Gets the certificate standards.
     * @return the certificate standards
     */
    public List<CertificateStandard> getCertificateStandards() {

        List<CertificateStandard> certificateStandards = new ArrayList<CertificateStandard>();
        if (!ObjectUtil.isEmpty(customerProject)
                && !ObjectUtil.isEmpty(customerProject.getCertificateStandard())
                && !ObjectUtil.isEmpty(customerProject.getCertificateStandard().getCategory())
                && customerProject.getCertificateStandard().getCategory().getId() > 0) {
            certificateStandards = certificationService
                    .listCertificateStandardByCertificateCategoryId(customerProject
                            .getCertificateStandard().getCategory().getId());
        }
        return certificateStandards;
    }

    /**
     * Gets the client service.
     * @return the client service
     */
    public IClientService getClientService() {

        return clientService;
    }

    /**
     * Sets the client service.
     * @param clientService the new client service
     */
    public void setClientService(IClientService clientService) {

        this.clientService = clientService;
    }

    /**
     * Gets the id generator.
     * @return the id generator
     */
    public IUniqueIDGenerator getIdGenerator() {

        return idGenerator;
    }

    /**
     * Sets the id generator.
     * @param idGenerator the new id generator
     */
    public void setIdGenerator(IUniqueIDGenerator idGenerator) {

        this.idGenerator = idGenerator;
    }

    /**
     * Gets the customer project.
     * @return the customer project
     */
    public CustomerProject getCustomerProject() {

        return customerProject;
    }

    /**
     * Sets the customer project.
     * @param customerProject the new customer project
     */
    public void setCustomerProject(CustomerProject customerProject) {

        this.customerProject = customerProject;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public CustomerProject getFilter() {

        return filter;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(CustomerProject filter) {

        this.filter = filter;
    }

    /**
     * Gets the selected holding type.
     * @return the selected holding type
     */
    public String getSelectedHoldingType() {

        return selectedHoldingType;
    }

    /**
     * Sets the selected holding type.
     * @param selectedHoldingType the new selected holding type
     */
    public void setSelectedHoldingType(String selectedHoldingType) {

        this.selectedHoldingType = selectedHoldingType;
    }

    /**
     * Gets the holding type list.
     * @return the holding type list
     */
    public Map<String, String> getHoldingTypeList() {

        return holdingTypeList;
    }

    /**
     * Sets the holding type list.
     * @param holdingTypeList the holding type list
     */
    public void setHoldingTypeList(Map<String, String> holdingTypeList) {

        this.holdingTypeList = holdingTypeList;
    }

    /**
     * Gets the customer id.
     * @return the customer id
     */
    public String getCustomerId() {

        return customerId;
    }

    /**
     * Sets the customer id.
     * @param customerId the new customer id
     */
    public void setCustomerId(String customerId) {

        this.customerId = customerId;
    }

    /**
     * Gets the customer detail params.
     * @return the customer detail params
     */
    public String getCustomerDetailParams() {

        return "tabIndex=" + URLEncoder.encode(tabIndex) + "&id=" + getCustomerUniqueId() + "&"
                + tabIndex;
    }

    /**
     * Gets the tab index.
     * @return the tab index
     */
    public String getTabIndex() {

        return tabIndex;
    }

    /**
     * Sets the tab index.
     * @param tabIndex the new tab index
     */
    public void setTabIndex(String tabIndex) {

        this.tabIndex = tabIndex;
    }

    /**
     * Gets the inspection type list.
     * @return the inspection type list
     */
    public Map<String, String> getInspectionTypeList() {

        return inspectionTypeList;
    }

    /**
     * Sets the inspection type list.
     * @param inspectionTypeList the inspection type list
     */
    public void setInspectionTypeList(Map<String, String> inspectionTypeList) {

        this.inspectionTypeList = inspectionTypeList;
    }

    /**
     * Gets the selected inspection type.
     * @return the selected inspection type
     */
    public String getSelectedInspectionType() {

        return selectedInspectionType;
    }

    /**
     * Sets the selected inspection type.
     * @param selectedInspectionType the new selected inspection type
     */
    public void setSelectedInspectionType(String selectedInspectionType) {

        this.selectedInspectionType = selectedInspectionType;
    }

    /**
     * Gets the unit compo type list.
     * @return the unit compo type list
     */
    public Map<String, String> getUnitCompoTypeList() {

        return unitCompoTypeList;
    }

    /**
     * Sets the unit compo type list.
     * @param unitCompoTypeList the unit compo type list
     */
    public void setUnitCompoTypeList(Map<String, String> unitCompoTypeList) {

        this.unitCompoTypeList = unitCompoTypeList;
    }

    /**
     * Gets the selected unit compo type.
     * @return the selected unit compo type
     */
    public String getSelectedUnitCompoType() {

        return selectedUnitCompoType;
    }

    /**
     * Sets the selected unit compo type.
     * @param selectedUnitCompoType the new selected unit compo type
     */
    public void setSelectedUnitCompoType(String selectedUnitCompoType) {

        this.selectedUnitCompoType = selectedUnitCompoType;
    }

    /**
     * Gets the customer unique id.
     * @return the customer unique id
     */
    public String getCustomerUniqueId() {

        return customerUniqueId;
    }

    /**
     * Sets the customer unique id.
     * @param customerUniqueId the new customer unique id
     */
    public void setCustomerUniqueId(String customerUniqueId) {

        this.customerUniqueId = customerUniqueId;
    }

    /**
     * Gets the certification service.
     * @return the certification service
     */
    public ICertificationService getCertificationService() {

        return certificationService;
    }

    /**
     * Sets the certification service.
     * @param certificationService the new certification service
     */
    public void setCertificationService(ICertificationService certificationService) {

        this.certificationService = certificationService;
    }

    /**
     * Gets the certificate category id.
     * @return the certificate category id
     */
    public String getCertificateCategoryId() {

        return certificateCategoryId;
    }

    /**
     * Sets the certificate category id.
     * @param certificateCategoryId the new certificate category id
     */
    public void setCertificateCategoryId(String certificateCategoryId) {

        this.certificateCategoryId = certificateCategoryId;
    }

    /**
     * Gets the ics list.
     * @return the ics list
     */
    public Map<String, String> getIcsList() {

        return icsList;
    }

    /**
     * Sets the ics list.
     * @param icsList the ics list
     */
    public void setIcsList(Map<String, String> icsList) {

        this.icsList = icsList;
    }

}
