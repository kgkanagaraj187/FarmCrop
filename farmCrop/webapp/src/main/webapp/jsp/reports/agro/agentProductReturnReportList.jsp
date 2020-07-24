<jsp:include page="/jsp/reports/agro/productReturnReportList.jsp">
    <jsp:param name="type" value="agent" />
    <jsp:param value="agentProductReturnReport_detail.action" name="url"/>
      <jsp:param value="agentProductReturnReport_list.action" name="url1"/>
        <jsp:param value="agentProductReturnReport_subList.action" name="url2"/>
          <jsp:param value="agentProductReturnReport_loadColumnLimit.action" name="url3"/>
          <jsp:param value="agentProductReturnReport_populateXLS.action" name="url4"/>
          <jsp:param value="agentProductReturnReport_populatePrintHTML.action" name="url6"/>
    </jsp:include>