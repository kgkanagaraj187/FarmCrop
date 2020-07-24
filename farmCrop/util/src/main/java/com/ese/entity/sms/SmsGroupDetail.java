package com.ese.entity.sms;

import javax.validation.GroupSequence;

import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;

@GroupSequence({ SmsGroupDetail.class, First.class, Second.class })
public class SmsGroupDetail extends Profile {
    public static final String PROFILE_TYPE_FARMER = "FARMER";
    public static final String PROFILE_TYPE_FIELD_STAFF = "FIELD_STAFF";
    public static final String PROFILE_TYPE_USER = "USER";
    public static final String PROFILE_TYPE_OTHER = "OTHER";
    public static final String PROFILE_TYPE_ALL = "ALL";

    private String profileType;
    private SmsGroupHeader smsGroupHeader;
    private SmsGroupHeader smsGroup;

    public SmsGroupHeader getSmsGroupHeader() {

        return smsGroupHeader;
    }

    public void setSmsGroupHeader(SmsGroupHeader smsGroupHeader) {

        this.smsGroupHeader = smsGroupHeader;
    }

    public String getProfileType() {

        return profileType;
    }

    public void setProfileType(String profileType) {

        this.profileType = profileType;
    }

    public static String getProfileTypeFarmer() {

        return PROFILE_TYPE_FARMER;
    }

    public static String getProfileTypeFieldStaff() {

        return PROFILE_TYPE_FIELD_STAFF;
    }

    public static String getProfileTypeUser() {

        return PROFILE_TYPE_USER;
    }

    public SmsGroupHeader getSmsGroup() {

        return smsGroup;
    }

    public void setSmsGroup(SmsGroupHeader smsGroup) {

        this.smsGroup = smsGroup;
    }

}
