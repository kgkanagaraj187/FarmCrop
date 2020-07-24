<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="java.util.Date"%>
<%@ page
	import="java.text.SimpleDateFormat,java.util.Map,com.opensymphony.xwork2.ActionContext"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<html>
<head>

<!-- add thi	s meta information to select layout  -->
<META name="decorator" content="swithlayout">

<style>
#form td {
	vertical-align: middle !important;
}
/*#templaeTbl{}

#templaeTbl td{border:solid 1px #567304 !important; padding:3px}

#templaeTbl th{border:solid 1px #567304 !important; background:#D3DEB0!important; padding:3px}

.thColHeader{
	background:#ff0000!important;
	
}

.trColOdd{
	background:none!important;
	
}

.trColEven{
	background:#b6c17b !important;
	
}*/
.w {
	padding: 0 8px !important;
}
</style>


</head>



<body>
	<script>
		jQuery(document)
				.ready(
						function() {
							var defaultHeight = jQuery('#pr').height();
							var tbHeight = 0;
							jQuery("#myTable05").find('tr').each(function() {
								tbHeight += jQuery(this).height();
							});

							if (defaultHeight > tbHeight) {
								defaultHeight = tbHeight + 20;
							}

							jQuery('#pr').css("height", defaultHeight + "px");

							//jQuery('#myTable05').fixedHeaderTable({ altClass: 'odd', footer: false, cloneHeadToFoot: true, fixedColumns: 5 });
							jQuery(".a").attr("colspan", "5");
							jQuery(".a1").attr("colspan", "6");
							jQuery(".r").remove();
							jQuery("#myTable05").show();

							jQuery("#templateCombo").change(function() {
								doFilter();
							});

							jQuery("#trainingRefCombo").change(function() {
								doFilter();
							});

							<s:iterator value="farmerTrainingList" var="farmerTraining">
							<s:iterator value="planners" status="pStatus">
							<s:if test='templateYear==year'>
							document
									.getElementById('<s:property value="farmerTraining.id"/>_<s:property value="year"/>_<s:property value="month"/>_<s:property value="week"/>_chk').checked = true;
							document
									.getElementById('<s:property value="farmerTraining.id"/>_<s:property value="year"/>_<s:property value="month"/>_<s:property value="week"/>_chk').disabled = false;
							</s:if>
							</s:iterator>
							</s:iterator>

							<s:iterator value="hideYearList">
							jQuery("[id*='_<s:property />_']").attr('disabled',
									true);
							</s:iterator>

							<s:iterator value="hideYearMonthList">
							jQuery("[id*='_<s:property />_']").attr('disabled',
									true);
							</s:iterator>

							<s:iterator value="hideYearMonthWeekList">
							jQuery("[id*='_<s:property />_']").attr('disabled',
									true);
							</s:iterator>

							<s:iterator value="showYearList">
							jQuery("[id*='_<s:property />_']").attr('enabled',
									true);
							</s:iterator>

							<s:iterator value="showYearMonthList">
							jQuery("[id*='_<s:property />_']").attr('enabled',
									true);
							</s:iterator>

							<s:iterator value="showYearMonthWeekList">
							jQuery("[id*='_<s:property />_']").attr('enabled',
									true);
							</s:iterator>
						});

		function doFilter() {
			jQuery("#templateYear").val(jQuery("#templateCombo").val());
			jQuery("#selectedTrainingRefId").val(
					jQuery("#trainingRefCombo").val());
			jQuery("#yearChangeForm").submit();
		}

		function exportXLS() {
			//document.exportForm.action = "trainingPlanner_export.action";
			jQuery("#exportForm").submit();
		}
		var dataArr = new Array();
		var delPlannerArr = new Array();
		function getPlannerDatas(obj) {
			if ($(obj).prop('checked') == true) {
				dataArr.push(obj.value);
			} else {
				delPlannerArr.push(obj.value)
			}
			$("#selectedWeeks").val(dataArr);
			$("#deletedWeeks").val(delPlannerArr);
			//dynamicFieldVal+=getFormattedValue(checkBoxName)+"###"+getFormattedValue(checkBoxVal)+"@@@";

		}
		function onSubmit() {
			$("#form").submit();
		}
	</script>
	<!-- Trining Template Sample Starts-->
	<form id="yearChangeForm" action="trainingPlanner_list" method="post">
		<s:hidden id="templateYear" name="templateYear" />
		<s:hidden name="selectedTrainingRefId" id="selectedTrainingRefId" />
	</form>

	<form id="exportForm" action="trainingPlanner_export" method="post">
		<s:hidden id="templateYear" name="templateYear" />
		<s:hidden name="selectedTrainingRefId" id="selectedTrainingRefId" />
	</form>
	<form id="form" name="form" action="trainingPlanner_update"
		method="post">
		<s:hidden id="selectedWeeks" name="selectedWeeks" />
		<s:hidden id="deletedWeeks" name="deletedWeeks" />

		<div>
			<div class="appContentWrapper marginBottom ">
				<div class="formContainerWrapper">
					<div class="formContainerWrapper dynamic-form-con">
						<s:select id="templateCombo" name="templateYear" list="years"
							cssStyle="width:280px;margin:5px 20px 5px 0; float:left;"
							cssClass="form-control input-sm select2"></s:select>
						<!-- <button class="xls-icon btn btn-sts" style="float:right;" onclick="exportXLS();" title="Export XLS" >EXPORT</button> -->

						<div id="pr" style="width: 100%; margin: 10px auto; height: 100%"
							class="table-responsive">

							<table class="table table-bordered aspect-detail" id="myTable05"
								cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<s:if
											test='isMultiBranch=="1"&&(getIsParentBranch()==1||getBranchId()==null)'>
											<s:if test='getBranchId==null'>
												<th rowspan="2"><s:text name="app.branch" /></th>
											</s:if>
											<th rowspan="2"><s:text name="app.subBranch" /></th>
										</s:if>
										<s:elseif test='getBranchId()==null'>
											<th rowspan="2"><s:text name="app.branch" /></th>
										</s:elseif>
										<th rowspan="2"><s:property	value="%{getLocaleProperty('farmerTraining.code')}" /><br />
											<s:select name="selectedTrainingRefId" id="trainingRefCombo"
												list="farmerTrainings" headerKey=""
												headerValue="%{getLocaleProperty('txt.all')}" listKey="id"
												listValue="code" cssStyle="width:100px !important" /></th>
										<th rowspan="2"><s:property
												value="%{getLocaleProperty('farmerTraining.trainingTopic')}" />
											<%-- <s:text name="farmerTraining.trainingTopic.name"></s:text> --%>
										</th>
										<th rowspan="2"><s:property
												value="%{getLocaleProperty('farmerTraining.topic.activity')}" />
											<%-- <s:text name="farmerTraining.topic.activity"></s:text> --%>
										</th>
										<th rowspan="2" class="hide"><s:text name="farmerTraining.targetGroup"></s:text>
										</th>
										<th rowspan="2"><s:property value="%{getLocaleProperty('farmerTraininig.methods')}" />
										</th>

										<s:iterator value="monthMap">
											<th colspan="<s:property value="value"/>" align="center"
												valign="top"><s:property value="key" /></th>
										</s:iterator>
									</tr>

									<tr>
										<s:iterator value="monthMap">
											<s:iterator status="stat" value="(value).{ #this }">
												<th class="w"><s:property value="#stat.count" /></th>
											</s:iterator>
										</s:iterator>
									</tr>
								</thead>

								<tbody>
									<s:iterator value="farmerTrainingList" status="sts">

										<s:if test="#sts.odd">
											<tr class="trColOdd">
										</s:if>
										<s:else>
											<tr class="trColEven">
										</s:else>
										<s:if
											test='isMultiBranch=="1"&&(getIsParentBranch()==1||getBranchId()==null)'>
											<s:if test='getBranchId()==null'>
												<td><s:property
														value="getBranchesMap().get(getParentBranchMap().get(branchId))" /></td>
											</s:if>
											<td><s:property value="getBranchesMap().get(branchId)" /></td>
										</s:if>
										<s:elseif test='getBranchId()==null'>
											<td><s:property value="getBranchesMap().get(branchId)" /></td>
										</s:elseif>

										<td><s:property value="code" /></td>
										<td><s:property value="trainingTopic.name" /></td>
										<td><s:property value="trainingTopicActivitySet" /></td>
										<td class="hide"><s:property value="targetGroupSet" /></td>
										<td><s:property value="trainingMethodSet" /></td>
										<s:iterator value="monthMap" status="monthIterator">
											<s:iterator status="stat" value="(value).{ #this }">
												<td class="TDClass"><s:checkbox name="selectWeeks"
														id="%{id}_%{templateYear}_%{#monthIterator.count}_%{#stat.count}_chk"
														fieldValue="%{id}_%{templateYear}_%{#monthIterator.count}_%{#stat.count}"
														class="weeks" onclick="getPlannerDatas(this)"></s:checkbox>
												</td>
											</s:iterator>
										</s:iterator>
										</tr>
									</s:iterator>
								</tbody>



							</table>
						</div>
						<div class="yui-skin-sam">
							<span class=""><span class="first-child">
									<button type="submit" id="buttonAdd1"
										onclick="event.preventDefault();onSubmit();"
										class="save-btn btn btn-sts">
										<font color="#FFFFFF"> <b><div>
													<s:property value="%{getLocaleProperty('save.button')}" /></b>
										</font>
									</button>
							</span></span>

						</div>
					</div>
				</div>
			</div>
		</div>



	</form>

	<!-- Trining Template Sample Ends-->
</body>
</html>