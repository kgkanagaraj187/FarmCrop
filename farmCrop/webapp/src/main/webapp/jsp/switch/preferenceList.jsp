<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">

 

</head>
<script src="https://cdn.ckeditor.com/4.10.1/standard/ckeditor.js"></script>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
%>

<s:if test='getUsername().equalsIgnoreCase(getUser())'>
<ul class="nav nav-pills">
    <li class="active"><a data-toggle="pill" href="#home">General</a></li>
    <li><a data-toggle="pill" href="#menu_Creation">Menu Creation</a></li>
   <s:if test="currentTenantId=='agro'">
    <li><a data-toggle="pill" href="#contract_form">Contract Form</a></li>
    </s:if>
</ul>
</s:if>
<s:else>
<ul class="nav nav-pills">
    <s:if test="currentTenantId=='agro'">
    <li class="active"><a data-toggle="pill" href="#home">General</a></li>
    <li><a data-toggle="pill" href="#contract_form">Contract Form</a></li>
    </s:if>
</ul>
</s:else>
 <div class="tab-content">
    <div id="home" class="tab-pane fade in active">

<s:form name="prefernceupdateform" cssClass="fillform" method="post"
	enctype="multipart/form-data">
	<!-- action="prefernce_%{command}" -->
	<s:hidden key="id" />
	<s:hidden key="temp" id="temp" />
	<s:hidden name="viewName" id="viewName" />
	<s:hidden name="reportName" id="reportName" />
	<s:hidden name="valStr" id="valStr" />

	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<div class="message">
				<s:actionmessage cssStyle="color: red;" />
			</div>
			<div class="error">
				<s:actionerror />
				<s:fielderror />
			</div>
			<h2>
				<s:text name="generalinformation" />
			</h2>
			<div class="flexform">
				<div class="flexform-item">
					<label for="txt"><s:text name="noOfInvalidAttempts" /><sup>&nbsp;*</sup></label>
					<div class="form-element">
						<s:textfield name="noOfInvalidAttempts" theme="simple"
							maxLength="2" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:text name="timeToAutoRelease" /><sup>&nbsp;*</sup></label>
					<div class="form-element">
						<s:textfield name="timeToAutoRelease" theme="simple"
							maxLength="10" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:text name="aeraCaptureMode" /><sup>&nbsp;</sup></label>
					<div class="form-element">
						<s:radio list="aeraCaptureModeType" listKey="key"
							listValue="value" name="aeraCaptureMode" theme="simple" />
					</div>
				</div>


				<div class="flexform-item">
					<label for="txt"><s:text name="geoFencingFlag" /></label>
					<div class="form-element">
						<s:checkbox id="geoFincingFlg" theme="simple" name="geoFincingFlg" />
					</div>
				</div>

				<div class="flexform-item" id="geofincingrad">
					<label for="txt"><s:text name="geoFencingRadius" /><sup>&nbsp;*</sup></label>
					<div class="form-element">
						<s:textfield id="geoFincingRadius" name="geoFincingRadius"
							theme="simple" maxLength="10" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('currentSeasonLbl')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<%-- <s:select id="currentSeasonId" name="currentSeasonId"
							list="currentSeasonList" listKey="key" listValue="value"
							cssClass="form-control input-sm select2" /> --%>
							 <s:select id="currentSeasonId" name="currentSeasonId"
							list="harvestseasonsLang" listKey="key" listValue="value"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<s:if test="currentTenantId!='welspun'&&currentTenantId!='livelihood'">
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('currencyLbl')}" /></label>
					<div class="form-element">
						<s:textfield name="currencyTypez" theme="simple" cssClass="form-control input-sm" />
					</div>
				</div>
				</s:if>
				<!-- QR CODE ADDREESS -->
			<s:if test="currentTenantId=='kpf'  || currentTenantId=='wub' || currentTenantId=='simfed'">

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine1')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine1" name="addressLine1" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine2')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine2" name="addressLine2" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine3')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine3" name="addressLine3" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine4')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine4" name="addressLine4" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine5')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine5" name="addressLine5" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine6')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine6" name="addressLine6" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine7')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine7" name="addressLine7" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine8')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine8" name="addressLine8" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine9')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine9" name="addressLine9" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine10')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine10" name="addressLine10" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine11')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine11" name="addressLine11" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine12')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine12" name="addressLine12" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine13')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine13" name="addressLine13" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine14')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine14" name="addressLine14" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine15')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine15" name="addressLine15" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine16')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine16" name="addressLine16" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('addressLine17')}" /></label>
					<div class="form-element">
						<s:textfield id="addressLine17" name="addressLine17" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('barcode')}" /></label>
					<div class="form-element">
						<s:textfield id="barcode" name="barcode" theme="simple"
							maxLength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				
</s:if>



			</div>
			<s:if test="enableLoanModule==1">
				<div class="formContainerWrapper">
						<h2>
							<s:text name="loanInterestDetails" />
						</h2>
			          	<!-- <div id="validateLoanErr"
								style="text-align: center; padding: 5px 0 0 0"></div> -->
							<table id="tblData1" class="table table-bordered aspect-detail"
								style="margin-top: 2%">
								<thead>
									<tr>
										<th><s:text name="minRange"></s:text></th>
										<th><s:text name="maxRange" /></th>
										<th><s:text name="interest" /></th>
										<th><s:text name="action" /></th>
									</tr>
								</thead>
								<tbody >
								<tr class="tableTr1">
								<td>
									<s:textfield  class="tableTd28 form-control input-sm"  maxlength="25" onkeypress="return isNumber(event)"  />
								</td>
								<td>
									<s:textfield  class="tableTd29 form-control input-sm"  maxlength="25" onkeypress="return isNumber(event)"  />
								</td>
								<td>
									<s:textfield  class="tableTd30 form-control input-sm"  maxlength="25" onkeypress="return isDecimal(event)"  />
								</td>
								<td>
								
								<button type="button" id="addLoanDetail"
												class="addLoanInfo slide_open btn btn-sts"
												onclick="saveLoanDetails()">
												<i class="fa fa-plus" aria-hidden="true"></i>
											</button>
								</td>
								</tr>
								</tbody>
							</table>

							<%-- <div class="yui-skin-sam">
								<span id="button1" class=""><span class="first-child">
										<button type="button" onclick="saveLoanDetails();"
											class="save-button btn btn-success">
											<font color="#FFFFFF"> <b><s:text
														name="save.button" /></b>
											</font>
										</button>
								</span></span>

							</div> --%>

							<table id="tableTemplate"
								class="table table-bordered aspect-detail"
								style="margin-top: 2%">
								<thead id="tableHD">
									<tr class="border-less">
										<th class="hide">Id</th>
										<%-- <th><s:text name="title.farm"></s:text></th> --%>
										<th><s:text name="minRange"></s:text></th>
										<th><s:text name="maxRange"></s:text></th>
										<th><s:text name="interest"></s:text></th>
										<th><s:text name="delete"></s:text></th>
									</tr>
								</thead>
								<tbody id="tBodyTemplate">
								</tbody>
							</table>

						</div>
				</s:if>
			<s:if test='getUsername().equalsIgnoreCase(getUser())'>
				<h2>
					<s:text name="dashboradSettings" />
				</h2>
				<div class="flexform">
					<div class="flexform-item">
						<label for="txt"><s:text name="warehouseStockChart" /></label>
						<div class="form-element">
							<s:radio id="warehouseStockShart" name="warehouseStockShart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farmersByGroupPie" /></label>
						<div class="form-element">
							<s:radio id="farmersByGroupPie" name="farmersByGroupPie"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farmersByGroupBar" /></label>
						<div class="form-element">
							<s:radio id="farmersByGroupBar" name="farmersByGroupBar"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="devicesChart" /></label>
						<div class="form-element">
							<s:radio id="deviceChart" name="deviceChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farmersByOrg" /></label>
						<div class="form-element">
							<s:radio id="farmersByOrg" name="farmersByOrg"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="totalLandAcreChart" /></label>
						<div class="form-element">
							<s:radio id="totalLandAcreChart" name="totalLandAcreChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="totalProductionAcreChart" /></label>
						<div class="form-element">
							<s:radio id="totalAreaAroductionChart"
								name="totalAreaAroductionChart" list="enableBranchTypes"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="seedTypeBreakUp" /></label>
						<div class="form-element">
							<s:radio id="seedTypeChart" name="seedTypeChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="seedSourceBreakUp" /></label>
						<div class="form-element">
							<s:radio id="seedSourceChart" name="seedSourceChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farmersDetailChart" /></label>
						<div class="form-element">
							<s:radio id="farmerDetailsChart" name="farmerDetailsChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farmerCropDetailChart" /></label>
						<div class="form-element">
							<s:radio id="farmerCropDetailChart" name="farmerCropDetailChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="txnChart" /></label>
						<div class="form-element">
							<s:radio id="txnChart" name="txnChart" list="enableBranchTypes"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cowMilkByMonChart" /></label>
						<div class="form-element">
							<s:radio id="cowMilkByMonthChart" name="cowMilkByMonthChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cowMilkNonMilkChart" /></label>
						<div class="form-element">
							<s:radio id="cowMilkNonMilkChart" name="cowMilkNonMilkChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cowByVillageChart" /></label>
						<div class="form-element">
							<s:radio id="cowByVillageChart" name="cowByVillageChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cowByResearchStationChart" /></label>
						<div class="form-element">
							<s:radio id="cowByResearchStationChart"
								name="cowByResearchStationChart" list="enableBranchTypes"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cowDiseaseChart" /></label>
						<div class="form-element">
							<s:radio id="cowDiseaseChart" name="cowDiseaseChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cowCostBarChart" /></label>
						<div class="form-element">
							<s:radio id="cowCostBarChart" name="cowCostBarChart"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="totalLandAcreVillageChart" /></label>
						<div class="form-element">
							<s:radio id="totalLandAcreVillageChart"
								name="totalLandAcreVillageChart" list="enableBranchTypes"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farmerSowingHavst" /></label>
						<div class="form-element">
							<s:radio id="farmerSowingHarvest" name="farmerSowingHarvest"
								list="farmerSowingHarvestList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
						<div class="flexform-item">
						<label for="txt"><s:text name="areaUnderProdByOrg" /></label>
						<div class="form-element">
							<s:radio id="areaUnderProdByOrg" name="areaUnderProdByOrg"
								list="areaUnderProdByOrgList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
					
					<div class="flexform-item">
						<label for="txt"><s:text name="ginnerQuantitySold" /></label>
						<div class="form-element">
							<s:radio id="ginnerQuantitySold" name="ginnerQuantitySold"
								list="ginnerQuantityList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
					
					<div class="flexform-item">
						<label for="txt"><s:text name="gmoPercentage" /></label>
						<div class="form-element">
							<s:radio id="gmoPercentage" name="gmoPercentage"
								list="gmoList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

				</div>
				<h2>
					<s:text name="Logo" />
				</h2>
				<div class="flexform">
					<div class="flexform-item">
						<label for="txt"><s:text name="loginLogo" /></label>

						<div class="form-element">
							<s:if
								test='loginLogoImageByteString!=null && loginLogoImageByteString!=""'>
								<img width="140" height="45" border="0"
									src="data:image/png;base64,<s:property value="loginLogoImageByteString"/>">
							</s:if>
							<s:else>
								<img align="middle" width="150" height="100" border="0"
									src="img/no-image.png">
							</s:else>



						</div>
						<div>
							<s:file name="loginLogoImage" id="loginLogoImage" />
							<s:text name="loginLogo.imageTypes" />
						</div>

					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="favIcon" /><sup>&nbsp;</sup></label>
						<div class="form-element">
							<s:if
								test='favIconImageByteString!=null && favIconImageByteString!=""'>
								<img width="25" height="25" border="0"
									src="data:image/ico;base64,<s:property value="favIconImageByteString"/>">
							</s:if>
							<s:else>
								<img align="middle" width="150" height="100" border="0"
									src="img/no-image.png">
							</s:else>



						</div>
						<div>
							<s:file name="favIconImage" id="favIconImage" />
							<s:text name="favIcon.imageTypes" />
						</div>

					</div>
					<div class="flexform-item">
						<div class="form-element">
							<font color="red"> <s:text name="imgSizeMsg" /></font>
						</div>
					</div>

				</div>
				<h2>
					<s:text name="adminSettings" />
				</h2>
				<div class="flexform">
					<div class="flexform-item">
						<label for="txt"><s:text name="enableBranchLbl" /></label>
						<div class="form-element">
							<s:radio id="enableBranch" name="enableBranch"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" onchange="showSingleOrMultiBranch(this);" />
						</div>
					</div>

					<div class="flexform-item" id="singleBranchTr">
						<label for="txt"><s:text name="mainBranchNameLbl" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="mainBranchName" name="mainBranchName"
								theme="simple" maxLength="60" />
						</div>
					</div>

					<div class="flexform-item" id="multiBranchTr">
						<label for="txt"><s:text name="enableBranchLbl" /></label>
						<div class="form-element">
							<s:select id="branchTenantId" name="branchTenantId"
								list="branchList" listKey="key" listValue="value" headerKey="-1"
								headerValue="%{getText('txt.select')}" />

						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="enableMultiProductLbl" /></label>
						<div class="form-element">
							<s:radio id="enableMultiProduct" name="enableMultiProduct"
								list="enableMultiProductTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="enableGramPanjayatLbl" /></label>
						<div class="form-element">
							<s:radio id="enableGrampanjayat" name="enableGrampanjayat"
								list="enableGrampanjayatTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"><s:text name="enablefpofg" /></label>
						<div class="form-element">
							<s:radio id="enableFPOFG" name="enableFPOFG"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="enablefpofg" /></label>
						<div class="form-element">
							<s:radio id="enableFPOFG" name="enableFPOFG"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="enableFarmerCode" /></label>
						<div class="form-element">
							<s:radio id="enableFarmerCode" name="enableFarmerCode"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="enableBankInfo" /></label>
						<div class="form-element">
							<s:radio id="enableBankInfo" name="enableBankInfo"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="codeGenType" /></label>
						<div class="form-element">
							<s:radio id="codeType" name="codeType" list="codeTypeValues"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="want.procurement" /></label>
						<div class="form-element">
							<s:radio id="supplier" name="supplierType"
								list="enableBranchTypes" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="PrefericsName" /></label>
						<div class="form-element">
							<s:radio id="ics" name="icsName" list="icsNameList" listKey="key"
								listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="soilTest" /></label>
						<div class="form-element">
							<s:radio id="soil" name="soil" list="soilList" listKey="key"
								listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="idProofName" /></label>
						<div class="form-element">
							<s:radio id="idProf" name="idProof" list="idProofList"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="insuranceInform" /></label>
						<div class="form-element">
							<s:radio id="insuranceInfo" name="insuranceInformation"
								list="insuranceInfoList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="cropInform" /></label>
						<div class="form-element">
							<s:radio id="cropInfo" name="cropInformation" list="cropInfoList"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="harvestSeason" /></label>
						<div class="form-element">
							<s:radio id="harvestSeasoninfo" name="harvestSeasonInformation"
								list="harvestSeasonInfoList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="isCertifiedFarmerText" /></label>
						<div class="form-element">
							<s:radio id="isCertifiedFarmerinfo" name="isCertifiedFarmer"
								list="isCertifiedFarmerInfoList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="approveOption" /></label>
						<div class="form-element">
							<s:radio id="approveOption" name="approveOption"
								list="approveOptionList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="PreferbatchNo" /></label>
						<div class="form-element">
							<s:radio id="batchNo" name="batchNo" list="batchNoList"
								listKey="key" listValue="value" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="traceableBatchNo" /></label>
						<div class="form-element">
							<s:radio id="traceableBatchNo" name="traceableBatchNo"
								list="batchNoList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
					<div class="flexform-item">
						<label for="txt"><s:text name="kpfBased" /></label>
						<div class="form-element">
							<s:radio id="isKpfBased" name="isKpfBased"
								list="isKpfBasedList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
					<div class="flexform-item">
						<label for="txt"><s:text name="distImgEn" /></label>
						<div class="form-element">
							<s:radio id="distImage" name="distImage"
								list="isDistImageList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
					<div class="flexform-item">
						<label for="txt"><s:text name="prodReturnImgEn" /></label>
						<div class="form-element">
							<s:radio id="prodReturnImg" name="prodReturnImg"
								list="isProdReturnImageList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
					
					<div class="flexform-item">
						<label for="txt"><s:text name="isDigitalSignature" /></label>
						<div class="form-element">
							<s:radio id="isDigitalSignature" name="isDigitalSignature"
								list="isDigitalSignatureList" listKey="key" listValue="value"
								theme="simple" />
						</div>
					</div>
				

				</div>

			</s:if>
			<%-- <div class="yui-skin-sam" align="left">
					<sec:authorize ifAllGranted="system.prefernces.update">
						<span id="button" class=""><span class="first-child">
								<button type="button" class="save-btn btn btn-success">
									<FONT color="#FFFFFF"><b><s:text name="save.button" /></b></font>
								</button>
						</span></span>
					</sec:authorize>
				</div> --%>


			<div class="flexItem flex-layout flexItemStyle">
				<sec:authorize ifAllGranted="system.prefernces.update">
					<span id="button" class="yui-button"><span
						class="first-child">
							<button type="button" class="save-btn btn btn-success">
								<FONT color="#FFFFFF"><b><s:text name="save.button" /></b></font>
							</button>
					</span></span>
				</sec:authorize>
			</div>
		</div>
	</div>
</s:form>

</div>

 <div id="menu_Creation" class="tab-pane fade">
 						
 						<br>
 						<button id="save_subMenu_btn" type="button" class="btn btn-success"
							 onclick="save_subMenu_show();">
							<font color="#FFFFFF"> <b>Create menu</b></font>
						</button>

						<button id="delete_subMenu_btn" type="button" class="btnSrch btn btn-warning"
							onclick="delete_subMenu_show();">
							<font color="#FFFFFF"> <b>Delete menu</b></font>
						</button>
 						
 	 <div class="appContentWrapper marginBottom" id="save_subMenu_div" >
 			<div class="formContainerWrapper">
				<h2>Save Sub Menu</h2>
			</div>
			
			 <div class="flexform">
			 
			 	<div class="flexform-item " >
					<label for="txt">Parent Menu<sup style="color: red;">*</sup></label>
						<div class="form-element">
							 <s:select id="parentMenuIdForSaveSubMenu" list="parentMenus" headerKey="0" headerValue="Select" ></s:select>
						</div>
				</div>
			 	
			 	<div class="flexform-item"  >
					<label for="txt">Name<sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="menuName" cssClass="form-control" maxlength="45" />
						</div>
				</div>
			 	
			 				 	
			 	<div class="flexform-item" >
					<label for="txt">Description<sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="menuDescription" cssClass="form-control" maxlength="45" />
						</div>
				</div>
			 	
			 				 	
			 	<div class="flexform-item" >
					<label for="txt">URL<sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="menuUrl" cssClass="form-control" maxlength="45" />
						</div>
				</div>
			 	
			 				 	
			 	<%-- <div class="flexform-item"  >
					<label for="txt">Order<sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="menuOrder" cssClass="form-control" maxlength="45" onkeypress="return isNumber(event)" />
						</div>
				</div> --%>
			 	
			 	<div class="flexform-item " >
					<label for="txt">Action_Id (ese_menu_action)<sup style="color: red;">*</sup></label>
						<div class="form-element">
							 <s:select id="action_id_dropDown" list="action" headerKey="0" headerValue="Select" onchange="getActionId(this);" ></s:select>
						</div>
				</div>
				
				<div class="flexform-item " >
					<label for="txt">Role_Id (ese_role_ent,ese_role_menu)<sup style="color: red;">*</sup></label>
						<div class="form-element">
							 <s:select id="role_id_dropDown" list="role" headerKey="0" headerValue="Select" onchange="getRoleId(this);" ></s:select>
						</div>
				</div>
			 	
			 	<div class="flexform-item"  >
					<label for="txt">ese_ent (NAME)<sup style="color: red;">*</sup></label>
					<div class="form-element">
							<input id="ese_ent_radio1" type="radio" name="ese_ent" value="list" onclick="getEse_ent_Name(this);"> list 
							<input id="ese_ent_radio2" type="radio" name="ese_ent" value="create" onclick="getEse_ent_Name(this);"> create 
							<input id="ese_ent_radio3" type="radio" name="ese_ent" value="update" onclick="getEse_ent_Name(this);"> update 
							<input id="ese_ent_radio4" type="radio" name="ese_ent" value="delete" onclick="getEse_ent_Name(this);"> delete 
					</div>
				</div>
			 	
			 	<div class="flexform-item " >
					<label for="txt">Is View as Entity?</label>
						<div class="form-element">
							<s:checkbox name="isView" id="isView"  onclick="listView();"/>
						</div>
				</div>
				
			 	<div class="flexform-item butCls">

					<div class="form-element">

						<button type="button" class=" btn btn-success"
							 onclick="saveSubMenu();">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b></font>
						</button>

						<button type="button" class="btnSrch btn btn-warning"
							id="cancelButton" onclick="cancelFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b></font>
						</button>

					</div>
					
				</div>


			</div>
			
 	 </div>


		<div class="appContentWrapper marginBottom" id="orderSubMenus">
			<div class="formContainerWrapper">
				<h2>order Sub Menus</h2>
			</div>
			
			<div class="formContainerWrapper" >
				<ul id="sortable">
				</ul>
			</div>
			
			<div class="formContainerWrapper">
				<button type="button" class=" btn btn-success"
					onclick="getValueFromSortable();">
					<font color="#FFFFFF"> <b><s:text name="Update order" /></b></font>
				</button>

				<button type="button" class="btnSrch btn btn-warning"
					id="cancelButton" onclick="cancelFormSubmit();">
					<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b></font>
				</button>

			</div>

		</div>


		<div class="appContentWrapper marginBottom" id="delete_subMenu_div" >
		<div class="formContainerWrapper">
			<h2>Delete Sub Menu</h2>
		</div>
		
		 <div class="flexform">
											
				<div class="flexform-item " >
					<label for="txt">Parent Menu<sup style="color: red;">*</sup></label>
						<div class="form-element">
							 <s:select id="parentMenus_dropDown_delete" list="parentMenus" headerKey="0" headerValue="Select" onchange="populateSubMenuDropDown(this.value);"></s:select>
						</div>
				</div>
				
				<div class="flexform-item " >
					<label for="txt">Sub Menu<sup style="color: red;">*</sup></label>
						<div class="form-element">
							  <select id="subMenuDropDown"></select> 
						</div>
				</div>

				<div class="flexform-item">

					<div class="form-element">

						<button type="button" class="btn btn-success"
							 onclick="deleteSubMenu();">
							<font color="#FFFFFF"> <b><s:text name="delete.button" /></b></font>
						</button>

						<button type="button" class="btnSrch btn btn-warning"
							id="cancelButton" onclick="cancelFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b></font>
						</button>

					</div>
					
				</div>

			</div>
			
		<s:form id="cancelForm" action="prefernce_list"></s:form>	
			
	 </div>
 </div>
 

  <div id="contract_form" class="tab-pane fade">
  
  	 <div class="appContentWrapper marginBottom"  >
 			<div class="formContainerWrapper">
				<h2>Contract Form</h2>
			</div>
			
			
			
			
			<div class="appContentWrapper marginBottom">
			
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:farmerName]')">
					<font color="#FFFFFF"> <b>Farmer Name</b></font>
				</button>
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:fatherName]')">
					<font color="#FFFFFF"> <b>Father Name</b></font>
				</button>
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:village]')">
					<font color="#FFFFFF"> <b>Village</b></font>
				</button>
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:farmerAge]')">
					<font color="#FFFFFF"> <b>Farmer Age</b></font>
				</button>
				
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:season]')">
					<font color="#FFFFFF"> <b>Season</b></font>
				</button>
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:currentDate]')">
					<font color="#FFFFFF"> <b>Current Date</b></font>
				</button>
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:currentYear]')">
					<font color="#FFFFFF"> <b>Current Year</b></font>
				</button>
				
				<button type="button" class="btn btn-success"
					onclick="addContentToHtmlEditor('[:digitalSignature]')">
					<font color="#FFFFFF"> <b>Digital Signature</b></font>
				</button>
			
			</div>
			

			<textarea name="htmleditor"></textarea>

			<div class="flexform">
				<button type="button" class="btn btn-success"
					onclick="saveHtmlContent()">
					<font color="#FFFFFF"> <b>Save</b></font>
				</button>
			</div>
			
			

		</div>
  
  </div>
 
 
	<div class="appContentWrapper marginBottom viewDiv">
		<div class="formContainerWrapper">
			<h2>View Configuration</h2>
		</div>
		
		<div class="formContainerWrapper" style="color: red;">
			Prerequisites for the view Configuration <br>
			1. View has to be created before itself.<br>
			2. There should be Unique ID  ad First Column and Branch Id has to be the Second column of the view even if its non branching application. The branch column show and hide is controlled in the code.<br>
			3. The default Entitlement for these menus will be report.dynamicReport.list and URL will set in action itself dynamically.<br><br>
		</div>
<div class="clear"></div>
		<div class="flexform">
			<div class="flexform-item">
				<label for="txt">View</label>
				<div class="form-element">
					<s:select id="allView" list="{}" headerKey="0" headerValue="Select"
						name="selectedView" onchange="getViewFields();" />
				</div>
			</div>
			<div class="flexform-item">
				<label for="txt">Fields</label>
				<div class="form-element">
					<s:select name="selectedFields" list="{}"
						listKey="key" listValue="value" theme="simple" id="fields" onchange="resetFieldProp()"
						cssClass="form-control input-sm select2" multiple="true" />
				</div>
			</div>
			
					<div class="flexform-item">

					<div class="form-element">

					<button type="button" class="btn btn-success"
							 onclick="loadFieldsProperties();">
							<font color="#FFFFFF"> <b><s:text name="Set Fields Properties" /></b></font>
						</button>
				</div></div>
			
			
		</div>
	</div>

	<div class="appContentWrapper marginBottom fieldsSetupDiv">
		<div class="formContainerWrapper">
			<h2>View Configuration</h2>
		</div>
		<div class="formContainerWrapper">
			<div class="panel panel-default">
				<table id="fieldsTable" Class="fillform table table-bordered aspect-detail">
					<thead>
						<tr>
							<th>Field</th>
							<th>Label Name</th>
							<th>Order</th>
							<th>Is Grid Availability</th>
							<th>Alignment</th>
							<th>Is Footer Total Availability</th>
							<th>Is Filter</th>
							<th>Type</th>
							<th>Method</th>
							<th>Filter Order</th>
								<th>Is Date Filter</th>
						</tr>
					</thead>
					<tbody>
					
					</tbody>
				</table>
			</div>
		</div>

					<div class="form-element">

					<button type="button" class="btn btn-success"
							 onclick="saveSubMenu();">
							<font color="#FFFFFF"> <b><s:text name="Make" /></b></font>
						</button>
				</div>
		
		
	</div>

</div>





<script type="text/javascript">
function showSingleOrMultiBranch(evt)
{
	var val=$(evt).val();
	if(val==1){
		//alert("YES");
		$('#singleBranchTr').show();
		$('#multiBranchTr').hide();
	}
	else
	{
		//alert("NO");
		$('#multiBranchTr').show();
		$('#singleBranchTr').hide();
	}
}

function validateLoginLogoImage()
{
	var file=document.getElementById('loginLogoImage').files[0];
	var filename=document.getElementById('loginLogoImage').value;
	var fileExt=filename.split('.').pop();			

	if(file != undefined){
									   
		if(fileExt=='jpg' || fileExt=='jpeg' || fileExt=='png'||fileExt=='JPG'||fileExt=='JPEG'||fileExt=='PNG')
		{ 			
			if(file.size>51200){
				alert('<s:text name="fileSizeExceeds"/>');	
				file.focus();
				return false;			
			}//else if(imgWidth >260){
				//alert('<s:text name="imageWidthMsg"/>');
				//file.focus();
				//return false;	
			//}else if(imgHeight> 70){
				//alert('<s:text name="imageHeightMsg"/>');
				//file.focus();
				//return false;	
			//}
		}else{
			alert('<s:text name="invalidFileExtension"/>')	
			file.focus();
			return false;				
		}
	}
	return true;
}
function resetFieldProp(){
	jQuery('#fieldsTable > tbody').html('');
}
function validateFavIconImage()
{
	var file=document.getElementById('favIconImage').files[0];
	var filename=document.getElementById('favIconImage').value;
	var fileExt=filename.split('.').pop();			

	if(file != undefined){
									   
		if(fileExt=='ico' || fileExt=='ICO')
		{ 			
			if(file.size>51200){
				alert('<s:text name="fileSizeExceeds"/>');	
				file.focus();
				return false;			
			}//else if(imgWidth >260){
				//alert('<s:text name="imageWidthMsg"/>');
				//file.focus();
				//return false;	
			//}else if(imgHeight> 70){
				//alert('<s:text name="imageHeightMsg"/>');
				//file.focus();
				//return false;	
			//}
		}else{
			alert('<s:text name="invalidFileExtension"/>')	
			file.focus();
			return false;				
		}
	}
	return true;
}
var enableLoanModule = "<s:property value='enableLoanModule'/>";
$(document).ready(function()
{	
	$('.butCls').show();
	$('.viewDiv').hide();
	$('.fieldsSetupDiv').hide();
	populateSubMenuDropDown("0");
	var ese_ent_Name;
	var actionId;
	var roleId;
	$("#orderSubMenus").hide();
	$("#save_subMenu_div").hide();
	$("#delete_subMenu_div").hide();
	if(enableLoanModule=='1'){
		refreshLoanTemplateList();
		refreshLoanDetails();
	}
	<!--s:if test='getUsername().equalsIgnoreCase(getExecUser())'-->
	var username = '<s:property value="username"/>';
	var user = '<s:property value="user"/>';

	if(username.toUpperCase()===user.toUpperCase())
	{
		var radioValue = $("input[name='enableBranch']:checked").val();
		if(radioValue==1)
		{
			$('#singleBranchTr').show();
		}
		else
		{
			$('#multiBranchTr').show();
		}
	}
	
	$('.save-btn').on('click',function()
	{
		<!--s:if test='getUsername().equalsIgnoreCase(getExecUser())'-->
		if(username.toUpperCase()===user.toUpperCase())
		{
			validateLoginLogoImage();
			validateFavIconImage();
		}
		document.prefernceupdateform.action = "prefernce_update.action";
		document.getElementById('temp').value="yes";
		document.prefernceupdateform.submit();
	});

	$('#geoFincingFlg').change(function(evt) {
        if($(this). prop("checked") == true){        	
            $('#geofincingrad').show();
        } else {        	
        	$('#geofincingrad').hide();
        }            
    });
	
	var geoFlag = '<s:property value="geoFincingFlg" />';
	// alert(geoFlag);
	if(geoFlag == 'true') {		 
		 $('#geofincingrad').show();
	} else {		
		$('#geofincingrad').hide();
	}
	
	 
	 
	CKEDITOR.replace( 'htmleditor' );
	editContentToHtmlEditor();			
				
	
});

function saveHtmlContent(){
	var objhtmleditor = CKEDITOR.instances["htmleditor"];
	var htmlContent = objhtmleditor.getData();
	
	$.ajax({
		 type: "POST",
         async: false,
         data:{htmlContent:htmlContent},
      	 url: "prefernce_saveContracteTemplate",
      	 success: function(result) {
      		showPopup(result.msg,result.title);
       }
	});
	
	/* var win = window.open("", "Title", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=780,height=200,top="+(screen.height-400)+",left="+(screen.width-840));
	win.document.body.innerHTML = objhtmleditor.getData();
	win.print(); */
}

function addContentToHtmlEditor(txt){
	var objHtmlEditor = CKEDITOR.instances["htmleditor"];
	var htmlContent = objHtmlEditor.getData();
	htmlContent = htmlContent+txt;
	CKEDITOR.instances['htmleditor'].setData(htmlContent);
}

function editContentToHtmlEditor(){
	 $.ajax({
		 type: "POST",
         async: false,
         data:{},
      	 url: "prefernce_getContracteTemplate",
      	 success: function(result) {
      		var json = jQuery.parseJSON(result);
      		var html_str = json.templateHtml;
      		addContentToHtmlEditor(html_str);
      	 	}
      	 });
}

	// Dynamic menu creation,delete
	function populateSubMenuDropDown(parentMenuId){
		jQuery.post("prefernce_populateSubMenus.action",{parentMenuId:parentMenuId},function(result){
			insertOptions("subMenuDropDown",jQuery.parseJSON(result));
			
		});
	}
	
	function deleteSubMenu(subMenuId){
		var sub_menu_id = $("#subMenuDropDown").val();
		jQuery.post("prefernce_deleteSubMenu.action",{subMenuId:sub_menu_id},function(result){
			showPopup(result.msg,result.title);
			
			$("#parentMenus_dropDown_delete").val("0")
			document.getElementById('subMenuDropDown').options.length = 0;
			populateSubMenuDropDown("0");
			
			$("#delete_subMenu_div").toggle(1000);
			$("#save_subMenu_btn").toggle(1000);
			$("#delete_subMenu_btn").toggle(1000);
		});
		
	}
	
	function saveSubMenu(){
		
		var valStr="";
		var view="";
		var reportName="";
		  $('#fieldsTable > tbody > tr').each(function() {
			  $(this).find('.viwFlt').each(function(){
				  var type = $(this).attr('type');
				  var value=0;
					if(type=="checkbox"){
						if ($(this).is(':checked')) {
							value=1;
						}
					}
					else{
					value = $(this).val();
					}
					if(valStr!="")
						valStr=valStr+"~"+value;
					else
						valStr=value;
			  });
			  valStr=valStr+"#";
		  });
		 // alert("valStr: "+valStr);
		if(valStr!=''){
			valStr = 	valStr.split("#~").join("#");
		  
		 view=document.getElementById("allView").value;
		   reportName=document.getElementById("menuDescription").value;
		
		}
	myFunction:{
		var parentId = $("#parentMenuIdForSaveSubMenu").val();
		var menuName = $("#menuName").val();
		var menuDescription = $("#menuDescription").val();
		var menuUrl = $("#menuUrl").val();
		//var menuOrder = $("#menuOrder").val(); 
		var menuOrder = "1";
		
		if(parentId == "0"){
			alert("Please select Parent Menu");
			break myFunction;
		}
		
		if(menuName != ""){
			
			 var isChecked = checkRadioButtonCheckedOrNot();
				if(isChecked == true){
					var ese_ent_NAME = menuName+"."+ese_ent_Name;
					
				} 
		}else{
			alert("Please enter Name")
			break myFunction;
		}
		
		var actionId_Value = $("#action_id_dropDown").val();
		if(actionId_Value != "0"){
			var ese_action_ACTIONID = actionId;
		}else{
			alert("Please select Action_Id");
			break myFunction;
		}
		
		var roleId_Value = $("#role_id_dropDown").val();
		if(roleId_Value != "0"){
			var role_ID = roleId;
		}else{
			alert("Please select Role_Id ");
			break myFunction;
		}
		if(valStr!=''){
			ese_ent_NAME = 'report.dynamicReport.list';
		}
		if(menuDescription==''){
			menuDescription =menuName; 
		}
		
		if(valStr=='' && menuUrl==''){
			alert("Please enter URL")
			break myFunction;
		}
		if(parentId != "0" && menuName != "" && menuDescription != "" && menuUrl != "" && menuOrder != "" && ese_ent_NAME != undefined && ese_action_ACTIONID != "" && role_ID != ""){
			
			jQuery.post("prefernce_saveSubMenu.action",{parentId:parentId,menuName:menuName,menuDescription:menuDescription,menuUrl:menuUrl,menuOrder:menuOrder,ese_ent_name:ese_ent_NAME,ese_action_actionId:ese_action_ACTIONID,role_id:role_ID,valStr:valStr,viewName:view,reportName:reportName},function(result){
				
				if(result.msg == "Menu name already exist"){
				showPopup(result.msg,result.title);
				}else{
				showPopup("created new menu successfully","success");
				$('#allView').val('').trigger('change');
				$('#fields').val('').trigger('change');
				$('.viewDiv').hide();
				insertOptions_sortable("sortable",JSON.parse(result));
				
				$("#save_subMenu_div").toggle(1000);
				$("#orderSubMenus").toggle(1000);
				}
				//$( "#sortable" ).append('<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s" title="<s:text name="tipsTitle" />"></span>'+$(this).text()+'</li>');
			});
		}/* else{
			
			alert("Please enter all the mandatory fields")
		} */
		
	}
		
}
	
	/* function checkRadioButtonCheckedOrNot(){
		var isChecked = false;
		 isChecked = $('#ese_ent_radio1').prop('checked');
		if(isChecked == false){
			 isChecked = $('#ese_ent_radio2').prop('checked');
			 if(isChecked == false){
				 isChecked = $('#ese_ent_radio3').prop('checked');
				 if(isChecked == false){
					 isChecked = $('#ese_ent_radio4').prop('checked');
				 }
			 }
		}
		
		return isChecked;
	} */
	
	
	function checkRadioButtonCheckedOrNot(){
		var isChecked = false;
			if(isChecked == false){
				for (i=1;i<=4;i++){
					var  id = "#ese_ent_radio"+i;
					isChecked = $(id).prop('checked');
					if(isChecked == true){
						 break;
					}
				}
			}
		return isChecked;
	}
	
	function unCheckRadioButton(){
		document.getElementById('ese_ent_radio1').checked = false;
		document.getElementById('ese_ent_radio2').checked = false;
		document.getElementById('ese_ent_radio3').checked = false;
		document.getElementById('ese_ent_radio4').checked = false;
			
	}
	
    function insertOptions_sortable(ctrlName, jsonArr) {
        for (var i = 0; i < jsonArr.length; i++) {
            addOption_sortable(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
        }
        var id="#"+ctrlName;
        
    }
	
	
	function addOption_sortable(selectbox, text, value)
    {
        $( "#sortable" ).append('<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s" title="<s:text name="tipsTitle" />"></span>'+value+'</li>');
     }
	
	function cancelFormSubmit() {
		$("#cancelForm").submit();
	}
	
	function getEse_ent_Name(obj){
		ese_ent_Name = obj.value;
		
	}
	
	function getActionId(obj){
		//alert(obj.value)
		actionId = obj.value;
	}
	
	function getRoleId(obj){
		//alert(obj.value)
		roleId = obj.value;
	}
	
	function save_subMenu_show(){
		$("#save_subMenu_div").toggle(1000);
		$("#delete_subMenu_div").hide();
		$("#save_subMenu_btn").hide();
		$("#delete_subMenu_btn").hide();
	}
	
	function delete_subMenu_show(){
		$("#delete_subMenu_div").toggle(1000);
		$("#save_subMenu_div").hide();
		$("#save_subMenu_btn").hide();
		$("#delete_subMenu_btn").hide();
	}
	
	$( function() {
	    $( "#sortable" ).sortable();
	    $( "#sortable" ).disableSelection();
	    
	  } );
	
	function getValueFromSortable(){
		
		var value = document.getElementById("sortable");	
		var id = "";
		$(value).find("li").each(function(i,v){
			var str = $(this).text();
			
				for(var i=0; i<str.length;i++) {
				    if (str[i] == "-"){
				    	id = id+",";
					    break;
				      }else{
				    	id = id+str[i];
				    }
				}
			
			
		});
		
		jQuery.post("prefernce_updateSubMenusOrders.action",{subMenusOrder:id},function(result){
			showPopup(result.msg,result.title);
			
			$("#menuName").val("");
			$("#menuDescription").val("");
			$("#menuUrl").val("");
			$("#parentMenuIdForSaveSubMenu").val("0");
			$("#action_id_dropDown").val("0");
			$("#role_id_dropDown").val("0");
			unCheckRadioButton();
			var sortable_element = document.getElementById("sortable");
			sortable_element.innerHTML = '';
			
			$("#orderSubMenus").toggle(1000);
			$("#save_subMenu_btn").toggle(1000);
			$("#delete_subMenu_btn").toggle(1000);
		});
		
	}
	
	function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}

	function listView(){
		if (document.getElementById('isView').checked) 
		  {
			$('.viewDiv').show();
			$('.butCls').hide();
			$('#menuUrl').parent().parent().hide();
			
			$("input[name='ese_ent']").parent().parent().hide();
			$.ajax({
				 type: "POST",
		        async: false,
		        url: "prefernce_populateViews",
		        success: function(result) {
		       	 if(result.length==0){
		       	 }else{
		       		 insertOptions("allView",JSON.parse(result));
		       		 
		       	 }
		        }
			}); 
		  }else{
			  $('#menuUrl').parent().parent().show();
				
				$("input[name='ese_ent']").parent().parent().show();
			
		  }
		  
	}
function getViewFields(){
	var view=document.getElementById("allView").value;
	$.ajax({
		 type: "POST",
       async: false,
       data:{viewName:view},
       url: "prefernce_populateViewFields",
       success: function(result) {
      	 if(result.length==0){
      	 }else{
      		 insertOptions("fields",JSON.parse(result));
      	 }
       }
	});
}
function loadFieldsProperties(){
	 var selectedFields = ""+$("#fields").val()
	 var fls=selectedFields.split(",");
	$('.fieldsSetupDiv').show();
	var tbodyRow = "";
	  $.each(fls, function(i,e){
	tbodyRow += '<tr class="trCls">'+
	'<td><input type="hidden" class="viwFlt" value="'+e+'" name="entyNam_'+e+'"/>'+e+'</td>'+
	'<td><input type="text" class="viwFlt" name="lblNam_'+e+'"/></td>'+
    '<td><input type="text" class="viwFlt" size="3" maxlength="2" onkeypress="return isNumber(event)" name="ordr_'+e+'"/></td>'+
	'<td><input type="checkbox" class="viwFlt" name="grdAvil_'+e+'"/></td>'+
	'<td><select class="viwFlt" name="alin_'+e+'"><option value="">Select</option><option value="left">Left</option><option value="right">Right</option><option value="center">Center</option></select></td>'+
	
	
	'<td><select class="viwFlt" name="footTot_'+e+'"><option value="0">No</option><option value="2">Label On</option><option value="1">Yes</option></select></td>'+
	
	'<td><input class="viwFlt" type="checkbox" onkeypress="return isNumber(event)" name="isFiltr_'+e+'"/></td>'+
	
	'<td><select class="viwFlt" name="filterTyp_'+e+'"><option value="0">Select</option><option value="1">Text Box</option><option value="3">Drop Down</option><option value="4">Date</option></select></td>'+
	'<td><input class="viwFlt" type="text" name="filterMethod_'+e+'"/></td>'+
	'<td><input class="viwFlt" type="text" size="3" onkeypress="return isNumber(event)" maxlength="2" name="filterOrdr_'+e+'"/></td>'+
		'<td><input class="viwFlt" type="checkbox" name="datFilter_'+e+'"/></td>'+
	'</tr>';
	  });
	jQuery('#fieldsTable > tbody').html(tbodyRow);
}
function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
	
function saveLoanDetails(){
	var flag=0;
	var paramObj = {};	
	var minRange='';
	var maxRange='';
	var interest='';
	jQuery(".tableTr1").each(function(){
		
		jQuery(this).find(".tableTd28").each(function(){
			//columnModelName1 = jQuery.trim($(this).val());
			minRange= jQuery.trim($(this).val());
			if(minRange == "" || minRange == '0'){
				alert('<s:text name="Please Enter the Min Range Greater Than Zero"/>');
				 flag=1;
				return exit;
			}
			
		});
		
		jQuery(this).find(".tableTd29").each(function(){
			//columnModelName2 = jQuery.trim($(this).val());
			maxRange =jQuery.trim($(this).val());
			 if(maxRange == ''|| maxRange == '0'){
				alert('<s:text name="Please Enter the Max Range Greater Than Zero"/>');
				flag=1;
				return exit;
			} 
			 
		});
		
		
		
		jQuery(this).find(".tableTd30").each(function(){
			//var columnModelName3 = jQuery.trim($(this).val());
			interest =jQuery.trim($(this).val());
			 if(interest == ''){
				alert('<s:text name="Please Enter the Interest"/>');
				flag=1;
				return exit;
			} 
			 
		});

		if(parseInt(minRange)>=parseInt(maxRange)){
			alert('<s:text name="Please Enter Min Range Less Than Max Range "/>');
			flag=1;
			return exit;
		}
  
	});
	
	var loanInterestList=[];
	
		 var objfI1=new Object();
		 if(!isEmpty(minRange) || !isEmpty(maxRange) || !isEmpty(interest)){

		objfI1.minRange=minRange;
		
		objfI1.maxRange=maxRange;
		
		objfI1.interest=interest;
		
		loanInterestList.push(objfI1);
		
		}
	//alert(JSON.stringify(loanInterestList));
	var postData=new Object();

	postData.loanDetailJsonString=JSON.stringify(loanInterestList);

	if(loanInterestList.length>0){
		console.log('INTEREST===='+JSON.stringify(loanInterestList));
	console.log('here '+JSON.stringify(postData));

	if(flag==0){
		
	 $.ajax({
         url: 'prefernce_populateLoanInfo.action',
         async: false,
         type: 'post',
         data: postData,
         dataType: 'json',
         success: function (data) {
        	// alert(JSON.stringify(data));
        	 var dat = jQuery.parseJSON(JSON.stringify(data));
        	// alert(dat);
        	 if(dat.msg=='success'){
        	 $('#tableBody1').empty();
        	// refreshLoanDetails();
        	 refreshLoanTemplateList();
        	 //addLoan();
        	 }else{	 
        		 alert(dat.msg);
        	 }
         },
         
     });
	}else
	{
		alert('<s:text name="entervalues"/>');
		}
	}else{
		alert('<s:text name="entervalues"/>');
	}
	
	// refreshLoanDetails();
	 refreshLoanTemplateList();
	 refreshLoanDetails();
}
function refreshLoanDetails(){
	$(".tableTd28").val("");
	$(".tableTd29").val("");
	$(".tableTd30").val("");
}
function refreshLoanTemplateList(){
	$('#tableTemplate').hide();
	 $('#tBodyTemplate').empty();
	 $('#template').empty();
	 $('#template').append("<option value=''>Select Template</option>");
	 $.getJSON('prefernce_populateLoanList.action',function(jd){
		 var templates=jd.data;
		 var bodyContent='';
		 for(var i=0;i<templates.length;i++){
			 var template=templates[i];
			 bodyContent+='<tr>';
			 bodyContent+='<td class="hide">'+template.id+'</td>';
			 //bodyContent+='<td align="center">'+template.farmName+'</td>';
			 bodyContent+='<td align="center">'+template.min+'</td>';
			 bodyContent+='<td align="center">'+template.max+'</td>';
			 bodyContent+='<td align="center">'+template.interest+'</td>';
			 bodyContent+='<td align="center"><a href="#" class="fa fa-trash" onclick="butLoanDelete('+template.id+');"/></td>';
			 bodyContent+='</tr>';
			 $('#template').append("<option value='"+template.id+"'>"+template.min+""+template.max+""+template.interest+"</option>");
				$('#tableTemplate').show();
		 }
		
		 $('#tBodyTemplate').html(bodyContent);
		 $('#template').val('');
	 });
}


function butLoanDelete(val){
	var templateId=val;
	if(confirm('<s:text name="confirm.delete"/>')){
	  $.post("prefernce_deleteLoanRange.action",{templateId:templateId},
	        	function(data,status){
      		alert('<s:text name="msg.removed"/>');
      		refreshLoanTemplateList();
      	
	        	});
      }
}
function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}

</script>


<style>
 #sortable {
 list-style-type: none;
 margin: 0;
 padding: 0;
 width: 100%;
}

#sortable li {
 margin: 0 3px 3px 3px;
 padding: 0.4em;
 padding-left: 1.5em; /*font-size: 1.4em;*/
 font-size: 12px;
 height: 60px;
 
}

#sortable li span {
 position: absolute;
 margin-left: -1.3em;
 cursor: pointer;
}
  </style>