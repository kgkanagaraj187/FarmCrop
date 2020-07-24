<jsp:include page="/jsp/reports/agro/productReturnReportList.jsp">
    <jsp:param name="type" value="farmer" />   
    <jsp:param value="farmerProductReturnReport_detail.action" name="url"/>
      <jsp:param value="farmerProductReturnReport_list.action" name="url1"/>
        <jsp:param value="farmerProductReturnReport_subList.action" name="url2"/>
          <jsp:param value="farmerProductReturnReport_loadColumnLimit.action" name="url3"/>
          <jsp:param value="farmerProductReturnReport_populateXLS.action" name="url4"/>
          <jsp:param value="farmerProductReturnReport_populatePrintHTML.action" name="url5"/>
    </jsp:include>