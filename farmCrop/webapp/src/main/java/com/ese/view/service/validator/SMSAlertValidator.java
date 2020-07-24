/*
 * SMSAlertValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.ese.entity.sms.SMSHistory;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.validator.AbstractValidator;

@Component
public class SMSAlertValidator extends AbstractValidator {

    @Override
    protected Map<String, String> validate(Object object) {

        Map<String, String> errors = new HashMap<String, String>();
        if (!ObjectUtil.isEmpty(object) && object instanceof SMSHistory) {
            SMSHistory entity = (SMSHistory) object;

            addErrors(validateProperty(entity, "receiverMobNo"), errors, "smsHistory.receiverMobNo");

            if (StringUtil.isEmpty(errors.get("smsHistory.receiverMobNo"))) {
                String[] mobileNos = entity.getReceiverMobNo().split(",");
                Pattern pattern = Pattern.compile(SMSHistory.MOBILE_NO_PATTERN);
                if (!ObjectUtil.isEmpty(mobileNos) && mobileNos.length > 0) {
                    for (String mobileNo : mobileNos) {
                        if (!StringUtil.isEmpty(mobileNo)) {
                            Matcher matcher = pattern.matcher(mobileNo.trim());
                            if (!matcher.matches()) {
                                errors.put("smsHistory.receiverMobNo",
                                        "smsHistory.invalid.receiverMobNo");
                                break;
                            } else if (mobileNo.trim().length() != SMSHistory.MOBILE_NO_LENGTH) {
                                errors.put("smsHistory.receiverMobNo",
                                        "smsHistory.length.receiverMobNo");
                                break;
                            } else if (!(mobileNo.trim().startsWith("7")
                                    || mobileNo.trim().startsWith("8") || mobileNo.trim()
                                    .startsWith("9"))) {
                                errors.put("smsHistory.receiverMobNo",
                                        "smsHistory.invalid.receiverMobNo.startWith");
                                break;
                            }
                        } else {
                            errors.put("smsHistory.receiverMobNo", "smsHistory.empty.receiverMobNo");
                        }
                    }
                } else {
                    errors.put("smsHistory.receiverMobNo", "smsHistory.empty.receiverMobNo");
                }
            }

            addErrors(validateProperty(entity, "message"), errors, "smsHistory.message");
        }
        return errors;
    }

}
