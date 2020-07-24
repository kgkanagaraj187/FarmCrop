package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

@SuppressWarnings("serial")
public class MasterDataAction extends SwitchValidatorAction
{
	@SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(MasterDataAction.class);

    protected static final String CREATE = "create";
    protected static final String DETAIL = "detail";
    protected static final String UPDATE = "update";
    protected static final String MAPPING = "mapping";
    protected static final String DELETE = "delete";
    protected static final String LIST = "list";
    protected static final String TITLE_PREFIX = "title.";
    protected static final String HEADING = "heading";
   
	private String id;
	private String code;
    private String name;
    private String masterType;
	
	private MasterData masterData;
    private MasterData filter;
    
    private IClientService clientService;
    
    List<String> mTypeList = new ArrayList<String>();

	@Override
    public Object getData() {

        if (ObjectUtil.isEmpty(masterData)) {
            return null;
        } else {
        	masterData.setCode(masterData.getCode());
        	masterData.setName(masterData.getName());
        	masterData.setMasterType(masterData.getMasterType());
            return masterData;
        }
    }
    
    public String data() throws Exception 
    {
        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with

        MasterData filter = new MasterData();

        if (!StringUtil.isEmpty(searchRecord.get("code"))) {
            filter.setCode(searchRecord.get("code").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("name"))) {
            filter.setName(searchRecord.get("name").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("masterType"))) {
            filter.setMasterType(searchRecord.get("masterType").trim());
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) 
    {
        MasterData masterData = (MasterData) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        
        rows.add(masterData.getCode());
        rows.add(masterData.getName());
        String mastertype = "-";
        Boolean flag = false;
        
        for(int i=0; i<=MasterData.masterTypes.values().length;i++)
        {
        	if (Integer.parseInt(masterData.getMasterType()) == i) {
        		mastertype = getText("masterType"+i);
                rows.add(mastertype);
                flag = true;
            }
        }
        
        if(!flag)
        	rows.add(mastertype);
        
        jsonObject.put("id", masterData.getId());
        jsonObject.put("cell", rows);
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
    
    public Map<Integer, String> getMasterTypeList() {
    	/*//mTypeList.add(getText("masterType0"));
    	mTypeList.add(getText("masterType1"));
    	mTypeList.add(getText("masterType2"));
    	mTypeList.add(getText("masterType3"));
    	mTypeList.add(getText("masterType4"));
    	mTypeList.add(getText("masterType5"));
    	mTypeList.add(getText("masterType6"));
    	mTypeList.add(getText("masterType7"));
        return mTypeList;*/
    	//mTypeList.add(getText("masterType0"));
    	Map<Integer, String> mTypeList = new HashMap<Integer, String>();
    	if (mTypeList.size() == 0) {
    		mTypeList = getPropertyData("masterDataTypeList");
			return mTypeList;
		}
		return mTypeList;
   
    }
    
    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {

        if (masterData == null) {
            command = "create";
            request.setAttribute(HEADING, getText(CREATE));        
            return INPUT;
        } 
        else 
        {    
        	masterData.setName(masterData.getName());
        	
        	for(int i=0; i<=MasterData.masterTypes.values().length;i++)
            {
            	if (masterData.getMasterType().equalsIgnoreCase(getText("masterType"+i))) {
            		masterData.setMasterType(String.valueOf(i));
                }
            }
        	masterData.setContactPersonName(masterData.getContactPersonName());
        	masterData.setMobileNo(masterData.getMobileNo());
        	masterData.setLandlineNo(masterData.getLandlineNo());
        	masterData.setEmailAddress(masterData.getEmailAddress());
        	masterData.setAddress(masterData.getAddress());
        	masterData.setRevisionNo(DateUtil.getRevisionNumber());
        	
            clientService.addMasterData(masterData);     
            return REDIRECT;
        }
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception 
    {
        if (id != null && !id.equals("")) {
            masterData = clientService.findMasterDataById(Long.valueOf(id));
            if (masterData == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            
            for(int i=0; i<=MasterData.masterTypes.values().length;i++)
            {
            	if (Integer.parseInt(masterData.getMasterType()) == i) {
            		masterData.setMasterType(getText("masterType"+i));
            		break;
                }
            }
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
            if (masterData != null) { 
            	MasterData tempMasterData = clientService.findMasterDataById(Long.valueOf(masterData.getId()));
                if (tempMasterData == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
                tempMasterData.setName(masterData.getName());        
                tempMasterData.setContactPersonName(masterData.getContactPersonName());
            	tempMasterData.setMobileNo(masterData.getMobileNo());
            	tempMasterData.setLandlineNo(masterData.getLandlineNo());
            	tempMasterData.setEmailAddress(masterData.getEmailAddress());
            	tempMasterData.setAddress(masterData.getAddress());     
            	tempMasterData.setRevisionNo(DateUtil.getRevisionNumber());
            	
                clientService.editMasterData(tempMasterData);
            }
            //command = UPDATE;
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return super.execute();
    }
    
    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            masterData = clientService.findMasterDataById(Long.valueOf(id));
            if (masterData == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            for(int i=0; i<=MasterData.masterTypes.values().length;i++)
            {
            	if (Integer.parseInt(masterData.getMasterType()) == i) {
            		masterData.setMasterType(getText("masterType"+i));
            		break;
                }
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
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception 
    {
    	if (id != null && !id.equals("")) {
            masterData = clientService.findMasterDataById(Long.valueOf(id));
            /*for(int i=0; i<=MasterData.masterTypes.values().length;i++)
            {
            	if (Integer.parseInt(masterData.getMasterType()) == i) {
            		masterData.setMasterType(getText("masterType"+i));
            		break;
                }
            }*/
            if (masterData == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
    	}
    	
        if (!ObjectUtil.isEmpty(masterData))
        {
        	//clientService.removeMasterData(masterData);    // this line has been commented to disable deleting master data.
        }
        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }
    
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMasterType() {
		return masterType;
	}

	public void setMasterType(String masterType) {
		this.masterType = masterType;
	}

	public MasterData getMasterData() {
		return masterData;
	}

	public void setMasterData(MasterData masterData) {
		this.masterData = masterData;
	}

	public MasterData getFilter() {
		return filter;
	}

	public void setFilter(MasterData filter) {
		this.filter = filter;
	}
	
	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public List<String> getmTypeList() {
		return mTypeList;
	}

	public void setmTypeList(List<String> mTypeList) {
		this.mTypeList = mTypeList;
	}
}
