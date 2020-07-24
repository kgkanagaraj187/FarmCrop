<jsp:include page="/jsp/reports/agro/pmtReportList.jsp">
    <jsp:param name="type" value="procRecp" />
    <jsp:param value="productReceiptionReport_detail.action" name="url"/>
      <jsp:param value="productReceiptionReport_list.action" name="url1"/>
        <jsp:param value="productReceiptionReport_subList.action" name="url2"/>
          <jsp:param value="productReceiptionReport_loadColumnLimit.action" name="url3"/>
            <jsp:param value="productReceiptionReport_populateXLS.action" name="url4"/>
    </jsp:include>