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
						<p class="flexItem"><s:property value="cowInspection.cowId" /></p>
					</div>
					
					<s:if test='costFarming.elitType=="0"'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="farmerIdAndName" /></p>
						<p class="flexItem"><s:property value="cowInspection.farm.farmer.farmerId" />-<s:property
						value="cowInspection.cow.farm.farmer.firstName" /></p>
					</div>
					</s:if>
					<s:else>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="cow.researchStationName" /></p>
						<p class="flexItem"><s:property value="cowInspection.researchStation.name" /></p>
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
				<th colspan="8"><s:text name="info.cowMilkDetail" /></th>
			</tr>
			<tr>

				<td>
							<s:text name="sNo"/>
						</td>
			
				<th><s:text name="currentInspDate" /></th>
				<th><s:text name="lastInspDate" /></th>
				<th><s:text name="intervalDays" /></th>
				<th><s:text name="litreMilkMrng" /></th>
				<th><s:text name="litreMilkEvn" /></th>
				<th><s:text name="totMilkPerDay" /></th>
				<th><s:text name="totMilkPeriod" /></th>

			</tr>
			<s:iterator value="cowProductionList" status="status" var="bean">
				<tr>
					<td>
							<s:property value="#status.index"/>
						</td>
					
               		 <td> <s:date name="currentInspDate" format="dd/MM/yyyy" /></td>
					<td> <s:date name="lastInspDate" format="dd/MM/yyyy" /></td>
					<td><s:property value="intervalDays" /></td>
					<td><s:property value="milkMorngPerday" /></td>
					<td><s:property value="milkEvngPerday" /></td>
					<td><s:property value="totalMilkPerDay" /></td>
					<td><s:property value="totalMilkPerPeriod" /></td>
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
<s:form name="cancelform" action="ilkProductionReport_list">
	<s:hidden key="currentPage" />
</s:form>
<script>
function getBack()
	{
		document.form.action="milkProductionReport_list.action";
		document.form.submit();
	}
</script>
</body>
</html>