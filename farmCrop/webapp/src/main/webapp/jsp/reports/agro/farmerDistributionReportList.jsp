<jsp:include page="/jsp/reports/agro/distributionReportList.jsp">
    <jsp:param name="type" value="farmer" />   
    <jsp:param value="farmerDistributionReport_detail.action" name="url"/>
      <jsp:param value="farmerDistributionReport_list.action" name="url1"/>
        <jsp:param value="farmerDistributionReport_subList.action" name="url2"/>
          <jsp:param value="farmerDistributionReport_loadColumnLimit.action" name="url3"/>
          <jsp:param value="farmerDistributionReport_populateXLS.action" name="url4"/>
          <jsp:param value="farmerDistributionReport_populatePrintHTML.action" name="url5"/>
    </jsp:include>