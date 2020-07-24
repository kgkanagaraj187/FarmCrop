<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script src="js/dynamicComponents.js?v=1.19"></script>
<script>
	$(document).ready(function () {
		renderDynamicDetailFeilds();
		
	});
	
	
	function getTxnType(){
		return "381";
	}

    
    function getBranchIdDyn(){
    	return '<s:property value="warehouse.branchId"/>';
   	}
    

    function popDownload(id){
    	document.getElementById("loadId").value=id;
    	$('#audioFileDownload').submit(); 
    	
    }

</script>
<div class="appContentWrapper marginBottom ">
		<ul class="nav nav-pills">
			<li class="active"><a data-toggle="pill" href="#tabs-1"><s:property value="%{getLocaleProperty('title.samithi')}" /></a></li>
			<s:if test="getCurrentTenantId()=='susagri'">
			<li><a data-toggle="pill" href="#tabs-2"><s:property value="%{getLocaleProperty('title.farmer')}" /></a></li>	
			 <li><a data-toggle="pill" href="#tabs-3"><s:property value="%{getLocaleProperty('Scope Detail')}" /></a></li> 
			</s:if>		
		</ul>
</div>
<div class="tab-content">
		<div id="tabs-1" class="tab-pane fade in active">
			<div class="flexbox-container">
			<s:hidden key="warehouse.id" id="coOperativeId" class="uId"/>
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="warehouse.id" />
	<s:hidden key="command" />


	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">

					<div class="formContainerWrapper">

						<div class="aPanel">
							<div class="aTitle">


								<s:if
									test="getCurrentTenantId()=='pratibha' && getBranchId()=='bci' || getBranchId()=='organic'">
									<h2>
										<s:property value="%{getLocaleProperty('info.samithi.bci')}" />
									</h2>
								</s:if>
								<s:else>
									<h2>
										<s:property value="%{getLocaleProperty('info.samithi')}" />
									</h2>
								</s:else>
								<!-- <div class="pull-right">
										<a class="aCollapse" href="#samithiDetailsAccordian"><i
											class="fa fa-chevron-right"></i></a>
									</div> -->

							</div>
							<div class="aContent dynamic-form-con"
								id="samithiDetailsAccordian">


								<s:if test='branchId==null'>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="app.branch" />
										</p>
										<p class="flexItem">
											<s:property value="%{getBranchName(warehouse.branchId)}" />
										</p>
									</div>
								</s:if>

								<s:if test="getCurrentTenantId()=='atma' ">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('sangham.name')}" />
										</p>
										<p class="flexItem">
											<s:property value="sangham" />
										</p>
									</div>
								</s:if>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('samithi.name')}" />
									</p>
									<p class="flexItem">
										<s:property value="warehouse.name" />
									</p>
								</div>

								<s:if
									test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='pratibha' ">
									<s:if
									test="getCurrentTenantId()=='symrise' ">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('dateOfFormation')}" />
										</p>
										<p class="flexItem">
											<s:date name="warehouse.groupFormationDate" format="dd/MM/YYYY" />
										</p>
									</div>
</s:if>
<s:else>
<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('dateOfFormation')}" />
										</p>
										<p class="flexItem">
											<s:date name="warehouse.formationDate" format="dd/MM/YYYY" />
										</p>
									</div>
</s:else>
								</s:if>
								
								<s:if test="isKpfBased==1">
									<s:if test="getCurrentTenantId()!='gsma' && getCurrentTenantId()!='ecoagri'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('group.type')}" />
										</p>
										<p class="flexItem">
											<s:property value="mTypeList[warehouse.groupType]" />
										</p>
									</div>
								</s:if></s:if>
								<s:if test="getCurrentTenantId()=='kenyafpo'">
						<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('country.name')}" />
										</p>
										<p class="flexItem" name="selectedCountry">
											<s:property value="selectedCountry" />
										</p>
									</div>
						<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('state.name')}" />
										</p>
										<p class="flexItem" name="selectedState">
											<s:property value="selectedState" />
										</p>
									</div>
									
							<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('locality.name')}" />
										</p>
										<p class="flexItem" name="selectedLocality">
											<s:property value="selectedLocality" />
										</p>
									</div>
									
							<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('city.name')}" />
										</p>
										<p class="flexItem" name="selectedCity">
											<s:property value="selectedCity" />
										</p>
									</div>
									
						<s:if test="gramPanchayatEnable==1">
						<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('panchayat.name')}" />
										</p>
										<p class="flexItem" name="selectedPanchayat">
											<s:property value="selectedPanchayat" />
										</p>
									</div>
						</s:if>
						
							<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('village.name')}" />
										</p>
										<p class="flexItem" name="selectedVillage">
											<s:property value="selectedVillage" />
										</p>
									</div>
						
						</s:if>

								<s:if test="getCurrentTenantId()=='chetna'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.totalMembers')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.totalMembers" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.groupFormationDate')}" />
										</p>
										<p class="flexItem">
											<s:property value="groupFormationDate" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.noOfMale')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.noOfMale" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.noOfFemale')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.noOfFemale" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.presidentName')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.presidentName" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.presidentMobileNumber')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.presidentMobileNumber" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.secretaryName')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.secretaryName" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.secretaryMobileNumber')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.secretaryMobileNumber" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.treasurer')}" />
										</p>
										<p class="flexItem">
											<s:property value="warehouse.treasurer" />
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('warehouse.treasurerMobileNumber')}" />
										</p>
										<p class="flexItem" name="warehouse.treasurerMobileNumber">
											<s:property value="warehouse.treasurerMobileNumber" />
										</p>
									</div>
								</s:if>


							</div>

						</div>
				

						<s:if test="getCurrentTenantId()=='chetna'">
							<div class="aPanel">
								<div class="aTitle">
									<h2>
										<s:text name="info.bank" />
										<div class="pull-right">
											<a class="aCollapse" href="#bankingInfo"><i
												class="fa fa-chevron-down"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="bankingInfo">
									<table class="table table-bordered aspect-detail">


										<tr class="odd">
											<th colspan="4"><s:property
													value="%{getLocaleProperty('warehouse.bankInfo.AccountTypeProp')}" /></th>

											<th colspan="4"><s:property
													value="%{getLocaleProperty('warehouse.bankInfo.accNo')}" /></th>
											<th colspan="4"><s:property
													value="%{getLocaleProperty('warehouse.bankInfo.bankName')}" /></th>
											<th colspan="4"><s:property
													value="%{getLocaleProperty('warehouse.bankInfo.branchName')}" /></th>
											<th colspan="4"><s:property
													value="%{getLocaleProperty('warehouse.bankInfo.sortCode')}" /></th>

										</tr>

										<s:iterator value="warehouse.bankInfo" status="status">


											<tr>

												<td colspan="4"><s:property value="accType" /></td>
												<td colspan="4"><s:property value="accNo" /></td>
												<td colspan="4"><s:property value="bankName" /></td>
												<td colspan="4"><s:property value="branchName" /></td>
												<td colspan="4"><s:property value="sortCode" /></td>
											</tr>
										</s:iterator>


									</table>
								</div>
							</div>
						</s:if>

					</div>

<div class="dynamicFieldsRender"></div>
					<div class="yui-skin-sam">
					<sec:authorize  access="hasAnyRole('profile.samithi.update','profile.samithi.bci.update')">
							<span id="update" class=""><span class="first-child">
									<button type="button" class="edit-btn btn btn-success">
										<FONT color="#FFFFFF"> <b><s:text
													name="edit.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('profile.samithi.delete','profile.samithi.bci.delete')">
							<span id="delete" class=""><span class="first-child">
									<button type="button" class="delete-btn btn btn-warning">
										<FONT color="#FFFFFF"> <b><s:text
													name="delete.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize>
						<span id="cancel" class=""><span class="first-child"><button
									type="button" class="back-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button></span></span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%-- <div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper">
					
					
					<div>
						<s:if
							test="getCurrentTenantId()=='pratibha' && getBranchId()=='bci' || getBranchId()=='organic'">
							<h2>
								<s:property value="%{getLocaleProperty('info.samithi.bci')}" />
							</h2>
						</s:if>
						<s:else>
							<h2>
								<s:property value="%{getLocaleProperty('info.samithi')}" />
							</h2>
						</s:else>
						<s:if test='branchId==null'>
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="app.branch" />
								</p>
								<p class="flexItem">
									<s:property value="%{getBranchName(warehouse.branchId)}" />
								</p>
							</div>
						</s:if>
						<s:if test="getCurrentTenantId()=='atma' ">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('sangham.name')}" />
								</p>
								<p class="flexItem">
									<s:property value="sangham" />
								</p>
							</div>
						</s:if>
						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('samithi.name')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.name" />
							</p>
						</div>

						<s:if
							test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='pratibha' ">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('dateOfFormation')}" />
								</p>
								<p class="flexItem">
									<s:date name="warehouse.formationDate" format="dd/MM/YYYY" />
								</p>
							</div>

						</s:if>
						<s:if test="getCurrentTenantId()=='kpf'">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('group.type')}" />
								</p>
								<p class="flexItem">
									<s:property value="mTypeList[warehouse.groupType]" />
								</p>
							</div>
						</s:if>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('warehouse.totalMembers')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.totalMembers" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('warehouse.groupFormationDate')}" />
							</p>
							<p class="flexItem">
								<s:property value="groupFormationDate" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('warehouse.noOfMale')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.noOfMale" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('warehouse.noOfFemale')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.noOfFemale" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('warehouse.presidentName')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.presidentName" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('warehouse.presidentMobileNumber')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.presidentMobileNumber" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('warehouse.secretaryName')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.secretaryName" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('warehouse.secretaryMobileNumber')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.secretaryMobileNumber" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('warehouse.treasurer')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.treasurer" />
							</p>
						</div>

						<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('warehouse.treasurerMobileNumber')}" />
							</p>
							<p class="flexItem">
								<s:property value="warehouse.treasurerMobileNumber" />
							</p>
						</div>
						
						</div>
						
						<div class="aPanel">
							<div class="aTitle">
								<h2>
									<s:text name="info.bank" />
									<div class="pull-right">
										<a class="aCollapse" href="#bankingInfo"><i
											class="fa fa-chevron-down"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="bankingInfo">
								<table class="table table-bordered aspect-detail">


									<tr class="odd">
										<th colspan="4"><s:property
												value="%{getLocaleProperty('warehouse.bankInfo.AccountTypeProp')}" /></th>

										<th colspan="4"><s:property
												value="%{getLocaleProperty('warehouse.bankInfo.accNo')}" /></th>
										<th colspan="4"><s:property
												value="%{getLocaleProperty('warehouse.bankInfo.bankName')}" /></th>
										<th colspan="4"><s:property
												value="%{getLocaleProperty('warehouse.bankInfo.branchName')}" /></th>
										<th colspan="4"><s:property
												value="%{getLocaleProperty('warehouse.bankInfo.sortCode')}" /></th>

									</tr>
									
									<s:iterator value="warehouse.bankInfo" status="status">


										<tr>

											<td colspan="4"><s:property value="accType" /></td>
											<td colspan="4"><s:property value="accNo" /></td>
											<td colspan="4"><s:property value="bankName" /></td>
											<td colspan="4"><s:property value="branchName" /></td>
											<td colspan="4"><s:property value="sortCode" /></td>
										</tr>
									</s:iterator>


								</table>
							</div>
						</div>
						
						
					</div>
					<div class="yui-skin-sam">
						<sec:authorize
							access="hasAnyRole('profile.samithi.update','profile.samithi.bci.update')">
							<span id="update" class=""><span class="first-child">
									<button type="button" class="edit-btn btn btn-success">
										<FONT color="#FFFFFF"> <b><s:text
													name="edit.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize>
						<sec:authorize
							access="hasAnyRole('profile.samithi.delete','profile.samithi.bci.delete')">
							<span id="delete" class=""><span class="first-child">
									<button type="button" class="delete-btn btn btn-warning">
										<FONT color="#FFFFFF"> <b><s:text
													name="delete.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize>
						<span id="cancel" class=""><span class="first-child"><button
									type="button" class="back-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button></span></span>
					</div>
				</div>


			</div>

		</div>

	</div> --%>




</s:form>

<s:form name="updateform" action="samithi_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="samithi_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="samithi_list.action">
	<s:hidden key="currentPage" />
</s:form>
			</div>
		</div>
		<s:if test="getCurrentTenantId()=='susagri'">
		<div id="tabs-2" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">
		<div id="baseDiv1">
					<table id="detail2"></table>
				
					<div id="pagerForDetail2"></div>
				</div> 
			</div>
		</div>
		
				<div id="tabs-3" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">
	<input class="btn btn-sts" type="BUTTON" id="add"
						value="<s:text name="create.button"/>"
						onclick="document.createform.submit();"
						style="margin-bottom: 1.5%" />
		<div id="baseDiv1">
					<table id="detail3"></table>
				
					<div id="pagerForDetail3"></div>
				</div> 
					 	<s:form name="createform" action="samithiIcs_create">
				
					<s:hidden name="tabIndex" />
					<s:hidden key="currentPage" />
				</s:form>  
			</div>
		</div>
		</s:if>
		
	
</div>

	<s:form id="audioFileDownload"
		action="samithiIcs_populateDownload">
		<s:hidden id="loadId" name="id" />
	</s:form>
<script type="text/javascript">
    $(document).ready(function () {
    	if('<s:property value="getCurrentTenantId()"/>'== 'susagri'){
    		loadFarmerDetailsGrid();
    		loadIcsDetailsGrid();
    	}
    	
    	if('<s:property value="getCurrentTenantId()"/>' == 'pratibha' && '<s:property value="getBranchId()"/>' == 'bci'){
    		
			$(".breadcrumb").find('li:not(:first)').remove();
			
			
		$(".breadcrumb").append('<li><a href="samithi_list.action">/ <s:property value="%{getLocaleProperty('profile.samithi.bci')}" /></a></li>');
		}
	
        $('#update').on('click', function (e) {
            document.updateform.id.value = document.getElementById('coOperativeId').value;
            document.updateform.currentPage.value = document.form.currentPage.value;
            document.updateform.submit();
        });

        $('#delete').on('click', function (e) {
            if (confirm('<s:text name="confirm.delete"/> ')) {
                document.deleteform.id.value = document.getElementById('coOperativeId').value;
                document.deleteform.currentPage.value = document.form.currentPage.value;
                document.deleteform.submit();
            }
        });
       
    });
    function loadFarmerDetailsGrid(){

	    jQuery("#detail2").jqGrid(
	    {
	    url:'samithiFarmerDetails_data.action?samithiId='+document.getElementById('coOperativeId').value,
	            pager: '#pagerForDetail2',
	            mtype: 'POST',
	            datatype: "json",
	        	colNames:[
			   		'<s:property value="%{getLocaleProperty('Farmer Code By Tracenet')}" />',
			   		'<s:property value="%{getLocaleProperty('Farmer Name')}" />',
			   		'<s:property value="%{getLocaleProperty('City')}" />',
			   		'<s:property value="%{getLocaleProperty('Village')}" />',
			   		'<s:property value="%{getLocaleProperty('Farm Name')}" />',
			   		'<s:property value="%{getLocaleProperty('Inspection Type')}" />',
			   		'<s:property value="%{getLocaleProperty('IC Status')}" />', 
                  	 ],
	 	      	colModel:[
	 	      		{name:'farmersCodeTracenet',index:'FarmersCodeTracenet',sortable:false,frozen:false},
	 	      		{name:'farmerName',index:'farmerName',sortable:false,frozen:false},
	 	      		{name:'city',index:'city',sortable:false,frozen:false},
	 	      		{name:'village',index:'village',sortable:false,frozen:false},
	 	      		{name:'farmName',index:'farmName',sortable:false,frozen:false},
	 	      		{name:'inspType',index:'inspType',sortable:false,frozen:false},
	 	      		{name:'icType',index:'icType',sortable:false,frozen:false}, 
								],
	            height: 301,
	            width: $("#baseDiv").width(), // assign parent div width
	            scrollOffset: 0,
	            rowNum:10,
	            rowList : [10, 25, 50],
	            sortname:'id',
	            sortorder: "desc",
	            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	            beforeSelectRow:
	            function(rowid, e) {
	            var iCol = jQuery.jgrid.getCellIndex(e.target);
	            if (iCol >= 4){return false; }
	            else{ return true; }
	            },
	    /*         onSelectRow: function(id){
	            	 document.certificationDetailsform.id.value = id;
	 	            document.certificationDetailsform.tabIndex.value = "#tabs-2";
	 	            document.certificationDetailsform.submit();
	            
	            }, */
	            onSortCol: function (index, idxcol, sortorder) {
	            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	            }
	    });
	    jQuery("#detail2").jqGrid('navGrid', '#pagerForDetail2', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
	            jQuery("#detail2").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

	    colModel = jQuery("#detail2").jqGrid('getGridParam', 'colModel');
	    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail2")[0].id) +
	            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
	    var cmi = colModel[i], colName = cmi.name;
	    if (cmi.sortable !== false) {
	    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
	    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
	    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
	    }
	    });
	    windowHeight = windowHeight-100;
	    $('#detail2').jqGrid('setGridHeight',(windowHeight));
	    }
    
    function loadIcsDetailsGrid(){

	    jQuery("#detail3").jqGrid(
	    {
	    url:'samithiIcs_data.action?samithiId='+document.getElementById('coOperativeId').value,
	            pager: '#pagerForDetail3',
	            mtype: 'POST',
	            datatype: "json",
	        	colNames:[
			   		'<s:property value="%{getLocaleProperty('fieldStatus')}" />',
			   		'<s:property value="%{getLocaleProperty('Season')}" />',
			   		'<s:property value="%{getLocaleProperty('Scope Validity')}" />',
			   		'<s:property value="%{getLocaleProperty('Scope Certificate')}" />',
			   		
                  	 ],
	 	      	colModel:[
	 	      		{name:'icsType',index:'icsType',sortable:false,frozen:false,search:false},
	 	      		{name:'Season',index:'Season',sortable:false,frozen:false,search:false},
	 	      		{name:'date',index:'date',sortable:false,frozen:false,search:false},
	 	      		{name:'download',index:'download',sortable:false,frozen:false,search:false},
	 	      		
								],
	            height: 301,
	            width: $("#baseDiv").width(), // assign parent div width
	            scrollOffset: 0,
	            rowNum:10,
	            rowList : [10, 25, 50],
	            sortname:'id',
	            sortorder: "desc",
	            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	            beforeSelectRow:
	            function(rowid, e) {
	            var iCol = jQuery.jgrid.getCellIndex(e.target);
	            if (iCol >= 4){return false; }
	            else{ return true; }
	            },
	      /*       onSelectRow: function(id){
	            	 document.certificationDetailsform.id.value = id;
	 	            document.certificationDetailsform.tabIndex.value = "#tabs-3";
	 	            document.certificationDetailsform.submit();
	            
	            }, */
	            onSortCol: function (index, idxcol, sortorder) {
	            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	            }
	    });
	    jQuery("#detail3").jqGrid('navGrid', '#pagerForDetail3', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
	            jQuery("#detail3").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

	    colModel = jQuery("#detail3").jqGrid('getGridParam', 'colModel');
	    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail3")[0].id) +
	            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
	    var cmi = colModel[i], colName = cmi.name;
	    if (cmi.sortable !== false) {
	    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
	    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
	    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
	    }
	    });
	    windowHeight = windowHeight-100;
	    $('#detail3').jqGrid('setGridHeight',(windowHeight));
	    }

</script>