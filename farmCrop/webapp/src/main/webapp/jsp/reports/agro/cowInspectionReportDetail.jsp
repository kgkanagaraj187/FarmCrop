<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">
	jQuery(document).ready(function(){
			$(".item").first().addClass( "active" );
			var isMilkingCow='<s:property value="cowInspection.isMilkingCow"/>';
			if(isMilkingCow=='NO')
			{
				jQuery('.isMilkCow').hide();
			}
			else
			{
				jQuery('.isMilkCow').show();
				
			}
			
		});
	</script>
<s:form id="form" name="form">


<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper"̥>
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:text name="info.inspection" /></h2>
				
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="inspectionDate" /></p>
						<p class="flexItem"><s:date name="cowInspection.currentInspDate" format="dd/MM/yyyy" /></p>
					</div>
					
				<s:if test='cowInspection.elitType=="0"'>
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="farmerIdAndName" /></p>
						<p class="flexItem"><s:property value="cowInspection.farm.farmer.farmerId" />-<s:property
						value="cowInspection.farm.farmer.firstName" /></p>
					</div>
				</s:if>
				<s:else>
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="cow.researchStationName" /></p>
						<p class="flexItem"><s:property value="cowInspection.researchStation.name" /></p>
					</div>
				</s:else>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="cowId" /></p>
						<p class="flexItem"><s:property value="cowInspection.cowId" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="inspectionNo" /></p>
						<p class="flexItem"><s:property value="cowInspection.inspectionNo" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="lastInspDate" /></p>
						<p class="flexItem"><s:date name="cowInspection.lastInspDate"
						format="dd/MM/yyyy" /></div>
					
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="isMilkingCow" /></p>
						<p class="flexItem"><s:property value="cowInspection.isMilkingCow" /></p>
						</div>
					
					
					
			<div class="dynamic-flexItem isMilkCow">
			<s:if test='cowInspection.isMilkingCow=="YES"'>
			
						<p class="flexItem"><s:text name="litreMilkMrng" /></p>
						<p class="flexItem"><s:property value="cowInspection.milkMorngPerday" /></p>
			</s:if>
					<s:else>
						<div></div>
						<div></div>
					</s:else>
			</div>
				<div class="dynamic-flexItem">	
					<p class="flexItem"><s:text name="litreMilkEvn" /></p>
					<p class="flexItem"><s:property value="cowInspection.milkEvngPerday" /></p>
				</div>
			
				<div class="dynamic-flexItem isMilkCow">	
					<p class="flexItem"><s:text name="totMilkPerDay" /></p>
					<p class="flexItem"><s:property value="cowInspection.totalMilkPerDay" /></p>
				</div>
				
				<div class="dynamic-flexItem isMilkCow">	
					<p class="flexItem"><s:text name="totMilkPeriod" /></p>
					<p class="flexItem"><s:property value="cowInspection.totalMilkPerPeriod" /></p>
					
				</div>
				
				</div>
			</div>
		</div>
	</div>
</div>

<br/>

<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper"̥>
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:text name="info.health" /></h2>
					
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="infestationPara" /></p>
							<p class="flexItem"><s:property value="cowInspection.infestationPara" /></p>
							</div>
				
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="deworwingPlace" /></p>
							<p class="flexItem"><s:property value="cowInspection.deworwingPlace" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="medicineName" /></p>
							<p class="flexItem"><s:property value="cowInspection.medicineName" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="diseaseNoticed" /></p>
							<p class="flexItem"><s:property value="cowInspection.diseaseNoticed" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="diseaseNamez" /></p>
							<p class="flexItem"><s:property value="cowInspection.diseaseName" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="diseaseService" /></p>
							<p class="flexItem"><s:property value="cowInspection.diseaseServices" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="diseaseMedici" /></p>
							<p class="flexItem"><s:property value="cowInspection.diseaseMedicines" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="healthProblem" /></p>
							<p class="flexItem"><s:property value="cowInspection.healthProblem" /></p>
							</div>
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="corrective.measure" /></p>
							<p class="flexItem"><s:property value="cowInspection.correctivMeasure" /></p>
							</div>
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="healthService" /></p>
							<p class="flexItem"><s:property value="cowInspection.healthServices" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="healthMedici" /></p>
							<p class="flexItem"><s:property value="cowInspection.healthMedicines" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="vaccinationPlace" /></p>
							<p class="flexItem"><s:property value="cowInspection.vaccinationPlace" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="vaccinationDate" /></p>
							<p class="flexItem"><s:date name="cowInspection.vaccinationDate"
						format="dd/MM/yyyy hh:mm:ss" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="vaccinaName" /></p>
							<p class="flexItem"><s:property value="cowInspection.vaccinationName" /></p>
							</div>
							
							<div class="dynamic-flexItem">	
							<p class="flexItem"><s:text name="comments" /></p>
							<p class="flexItem"><s:property value="cowInspection.comments" /></p>
							</div>
				
				</div>
			</div>
		</div>
	</div>
</div>
	
	<br/>
	
 <div class="col-md-6">
		<table class="table table-bordered fillform"
			style="margin-bottom: 30px">
			<tr>
				<th colspan="4"><s:text name="info.vaccination" /></th>
			</tr>
			<tr>

				<th><s:text name="vaccinaName" /></th>

				<th><s:text name="vaccinationDate" /></th>

			</tr>
			<s:iterator value="cowVaccinations" status="status">
				<tr>
					<td><s:property value="name" /></td>
					<td><s:property  value="date" /></td>
				</tr>

			</s:iterator>
		</table>
	</div>

	<div class="col-md-6">
		<table class="table table-bordered fillform"
			style="margin-bottom: 30px">
			<tr>
				<th colspan="4"><s:text name="info.feedType" /></th>
			</tr>
			<tr>

				<%-- <td>
							<s:text name="sNo"/>
						</td> --%>

				<th><s:text name="typeFeed" /></th>

				<th><s:text name="amtFeed" /></th>

			</tr>
			<s:iterator value="feedTypes" status="status">
				<tr>
					<%-- <td>
							<s:property value="#status.index"/>
						</td> --%>
					<td><s:property value="feedType" /></td>
					<td><s:property value="amount" /></td>
				</tr>

			</s:iterator>
			<tfoot>
				<tr>
					<td><s:text name="total"></s:text></td>
					<td><s:property value="feedTotalAmt" /></td>
				</tr>
			</tfoot>
		</table>
	</div>



	<%-- <div class="row">
		<ul class="bxslider"
			style="position: relative; width: auto !important;">
			<table>
			<s:iterator var="cowInspection.inspectionImages"
				status="headerstatus" value="cowInspection.inspectionImages">
				<li><s:hidden name="id" id="photoId" />
					<tr>
						<label><s:text name="date" /> : <s:date
								name="photoCaptureTime" format="dd/MM/yyyy hh:mm:ss " /></label>
					</tr>
					<tr>
						<label><s:text name="latitude" /> : <s:property
								value="latitude" /> <s:text name="longitude" /> : <s:property
								value="longitude" /></label>
					</tr>

					<tr>
						<img height="200" width="200"
							src="data:image/png;base64,<s:property value="imageByteString"/>">
					</tr></li>
			</s:iterator>
			</table>
		</ul>
	</div> --%>


	<div class="row">
		<div class="col-md-4">
			<div class="container">
				<div id="myCarousel" class="carousel slide" data-ride="carousel">
					<div id="stsEx" class="carousel-inner" role="listbox">
						<s:iterator var="cowInspection.inspectionImages"
							status="headerstatus" value="cowInspection.inspectionImages">
							<div class="item">
								<img width="300" height="200"
									src="data:image/png;base64,<s:property value="imageByteString"/>">
							</div>
						</s:iterator>
					</div>
					<a class="left carousel-control" href="#myCarousel" role="button"
						data-slide="prev"> <span
						class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						<span class="sr-only">Previous</span>
					</a> <a class="right carousel-control" href="#myCarousel" role="button"
						data-slide="next"> <span
						class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
						<span class="sr-only">Next</span>
					</a>
				</div>
			</div>
		</div>
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
	<s:form name="cancelform" action="cowInspectionReport_list">
	<s:hidden key="currentPage" />
</s:form>
<script>
	function getBack()
	{
		document.form.action="cowInspectionReport_list.action";
		document.form.submit();
	}
</script>
</body>