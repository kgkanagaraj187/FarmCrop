<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<s:form id="form" name="form">

<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper"̥>
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
				
					<h2><s:text name="info.cow" /></h2>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="cow.cowId" /></p>
						<p class="flexItem"><s:property value="costFarming.cow.cowId" /></p>
					</div>
					<s:if test='costFarming.elitType=="0"'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="farmerIdAndName" /></p>
						<p class="flexItem"><s:property value="costFarming.cow.farm.farmer.farmerId" />-<s:property
						value="costFarming.cow.farm.farmer.firstName" /></p>
					</div>
					</s:if>
					<s:else>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="cow.researchStationName" /></p>
						<p class="flexItem"><s:property value="costFarming.cow.researchStation.name" /></p>
					</div>
					</s:else>
				</div>
			</div>
		</div>
	</div>
</div>

<br/>
<div class="appContentWrapper marginBottom">	
	
		<table class="table table-bordered fillform"
			style="margin-bottom: 30px">
			<tr>
				<th colspan="8"><s:text name="info.cowDetail" /></th>
			</tr>
			<tr>

				<%-- <td>
							<s:text name="sNo"/>
						</td> --%>

				<th><s:text name="housingCost" /></th>
				<th><s:text name="feedCost" /></th>
				<th><s:text name="treatementCost" /></th>
				<th><s:text name="otherCost" /></th>
				<th><s:text name="totalExpence" /></th>
				<th><s:text name="incomeMilk" /></th>
				<th><s:text name="incomeOther" /></th>
				<th><s:text name="totalIncome" /></th>

			</tr>
			<s:iterator value="costFarmingList" status="status" var="bean">
				<tr>
					<%-- <td>
							<s:property value="#status.index"/>
						</td> --%>
					
					<td><s:property value="housingCost" /></td>
					<td><s:property value="feedCost" /></td>
					<td><s:property value="treatementCost" /></td>
					<td><s:property value="otherCost" /></td>
					<td><s:property value="totalExpence" /></td>
					<td><s:property value="incomeMilk" /></td>
					<td><s:property value="incomeOther" /></td>
					<td><s:property value="totalIncome" /></td>
				</tr>

			</s:iterator>
			
		</table>
	
	</div>
	<div class="yui-skin-sam">
			<span id="cancel" class="">
				<span class="first-child">
					<button type="button" class="back-btn btn btn-sts" onclick="getBack()">
						<b><FONT color="#FFFFFF"><s:text name="back.button" /> </font></b>
					</button>
				</span>
			</span>
		</div>
</s:form>
<s:form name="cancelform" action="costFarmingReport_list">
	<s:hidden key="currentPage" />
</s:form>
<script>
function getBack()
	{
		document.form.action="costFarmingReport_list.action";
		document.form.submit();
	}
</script>
</body>
</html>