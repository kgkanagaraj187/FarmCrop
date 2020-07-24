<jsp:include page="/jsp/reports/agro/pmtReportList.jsp">
    <jsp:param name="type" value="procTrans" />
    <jsp:param value="productTransferReport_detail.action" name="url"/>
      <jsp:param value="productTransferReport_list.action" name="url1"/>
        <jsp:param value="productTransferReport_subList.action" name="url2"/>
          <jsp:param value="productTransferReport_loadColumnLimit.action" name="url3"/>
            <jsp:param value="productTransferReport_populateXLS.action" name="url4"/>
    </jsp:include>