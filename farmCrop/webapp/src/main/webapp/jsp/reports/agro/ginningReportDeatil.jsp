<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="id" id="ginningId"/>
<font color="red">
    <s:actionerror/>
</font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:hidden key="command" />
	<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('info.baleGeneration')}" /></h2>
					 <s:if test='branchId==null'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property value="%{getBranchName(baleGeneration.branchId)}" /></p>
					</div>
				</s:if>	
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="ginningProcess.name" /></p>
						<p class="flexItem"><s:property value="ginningName"/></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="baleGeneration.genDate" /></p>
						<p class="flexItem"><s:property value="ginningdate" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="baleGeneration.heap" /></p>
						<p class="flexItem"><s:property value="heapName" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="icsName" /></p>
						<p class="flexItem"><s:property value="icsName" /></p>
					</div>
					<%-- <div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="farmer" /></p>
						<p class="flexItem"><s:property value="farmerName" /></p>
					</div> --%>
				</div><div>
				
				<div class="appContentWrapper marginBottom">
					<table class="table table-bordered aspect-detail mainTable">
					<tr>
							<td colspan="4" align="left"><b>Farmer Information</b></td>
					</tr>
							<tr class="odd">
								<th><s:text name="serialNo" /></th>
								<th><s:text name="farmerName" /></th>
								<th><s:text name="village"/></th>
								<th><s:text name="icsName"/></th>
							</tr>
							<s:iterator var="ginningFarmer" status="headerStatus" value="farmerInfo">
								<tr>
									<td><s:property value="%{#headerStatus.index+1}"/></td>
									<td><s:property value="#ginningFarmer[0]"/></td>
									<td><s:property value="#ginningFarmer[1]"/></td>
									<td><s:property value="#ginningFarmer[2]"/></td>
								</tr>
							</s:iterator>
							<%-- <tr>
							<td colspan="3" align="right"><b>Total</b></td>
							<td><s:property value="totBaleWeight"/></td>
							</tr> --%>
						</table>
				</div>
				
				<div class="appContentWrapper marginBottom">
					<table class="table table-bordered aspect-detail mainTable">
							<tr class="odd">
								<th><s:text name="serialNo" /></th>
								<th><s:text name="lotNo" /></th>
								<th><s:text name="prNo"/></th>
								<th><s:text name="baleWeight"/>(<s:property value="%{getLocaleProperty('kg')}"/>)</th>
							</tr>
							<s:iterator var="balData" status="headerStatus" value="baleGeneration">
								<tr>
									<td><s:property value="%{#headerStatus.index+1}"/></td>
									<td><s:property value="#balData.lotNo"/></td>
									<td><s:property value="#balData.prNo"/></td>
									<td><s:property value="#balData.baleWeight"/></td>
								</tr>
							</s:iterator>
							<tr>
							<td colspan="3" align="right"><b>Total</b></td>
							<td><s:property value="totBaleWeight"/></td>
							</tr>
						</table>
				</div>

				</div>
				
				
				<div class="yui-skin-sam">
				   <span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn btn btn-sts" >
				               <b><FONT color="#FFFFFF"><s:text name="back.button"/> 
				                </font></b></button></span></span>
				</div>
		</div>
	</div>
</div>
</div>
				
</s:form>



<s:form name="cancelform" action="ginningReport_list.action">
    <s:hidden key="currentPage"/>
</s:form>

