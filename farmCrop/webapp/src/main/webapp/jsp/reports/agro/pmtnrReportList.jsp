<jsp:include page="/jsp/reports/agro/pmtReportList.jsp">
    <jsp:param name="type" value="pmtnr" />
    <jsp:param value="pmtnrReport_detail.action" name="url"/>
      <jsp:param value="pmtnrReport_list.action" name="url1"/>
        <jsp:param value="pmtnrReport_subList.action" name="url2"/>
          <jsp:param value="pmtnrReport_loadColumnLimit.action" name="url3"/>
            <jsp:param value="pmtnrReport_populateXLS.action" name="url4"/>
    </jsp:include>