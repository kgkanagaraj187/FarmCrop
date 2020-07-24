<jsp:include page="/jsp/reports/agro/distributionReportList.jsp">
    <jsp:param name="type" value="agent" />
    <jsp:param value="agentDistributionReport_detail.action" name="url"/>
      <jsp:param value="agentDistributionReport_list.action" name="url1"/>
        <jsp:param value="agentDistributionReport_subList.action" name="url2"/>
          <jsp:param value="agentDistributionReport_loadColumnLimit.action" name="url3"/>
          <jsp:param value="agentDistributionReport_populateXLS.action" name="url4"/>
          <jsp:param value="agentDistributionReport_populatePrintHTML.action" name="url6"/>
    </jsp:include>