package com.sourcetrace.esesw.view.report.agro;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public class CustomImageBytesResult implements Result{
	@Override
    public void execute(ActionInvocation invocation) throws Exception {
		TrainingCompletionReportAction action = (TrainingCompletionReportAction) invocation.getAction();
        HttpServletResponse response = ServletActionContext.getResponse();

        response.setContentType(action.getCustomContentType());
    //    response.getOutputStream().write(action.getCustomImageInBytes());
        response.getOutputStream().flush();
    }
}
