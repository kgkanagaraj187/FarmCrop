package com.ese.entity.sms;

import com.sourcetrace.eses.entity.Profile;

// TODO: Auto-generated Javadoc
public class SmsTemplate extends Profile {
    private String name;
    private String template;
    private String branchId;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getTemplate() {

        return template;
    }

    public void setTemplate(String template) {

        this.template = template;
    }

    public String getBranchId() {
    
        return branchId;
    }

    public void setBranchId(String branchId) {
    
        this.branchId = branchId;
    }
    
}
