<jsp:include page="/jsp/reports/agro/pmtReportList.jsp">
    <jsp:param name="type" value="pmtnt" />
    <jsp:param value="pmtntReport_detail.action" name="url"/>
      <jsp:param value="pmtntReport_list.action" name="url1"/>
        <jsp:param value="pmtntReport_subList.action" name="url2"/>
          <jsp:param value="pmtntReport_loadColumnLimit.action" name="url3"/>
          <jsp:param value="pmtntReport_populateXLS.action" name="url4"/>
    </jsp:include>