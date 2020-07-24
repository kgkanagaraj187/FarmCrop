<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
</head>

<body>
	<style>
.top-space {
	margin-top: 1.5%;
}

#myOverlay{
position:fixed;
top:0;
left:0;
height:100%;
width:100%;
}
#myOverlay{
background:black;
backdrop-filter:blur(2px);
opacity:.2;
z-index:2;
display:none;
}

/* loader start */

.loader {

  border: 16px solid #f3f3f3;
  border-radius: 50%;
  border-top: 16px solid #3498db;
  width: 120px;
  height: 120px;
  -webkit-animation: spin 2s linear infinite; /* Safari */
  animation: spin 2s linear infinite;
  position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	

  
}

/* Safari */
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* loader end */
</style>
	<s:hidden id="groupId" />
	<div id="baseDiv" style="width: 900px"></div>

	<div class="appContentWrapper marginBottom" id="load">
		<ul class="nav nav-pills">
			<li class="active"><a data-toggle="tab" href="#compose"><s:text
						name="compose" /></a></li>

			<li><a data-toggle="tab" href="#groups"><s:text
						name="groups" /></a></li>

			<li><a data-toggle="tab" href="#template"><s:text
						name="template" /></a></li>
		</ul>

		<div class="tab-content">
			<div id="compose" class="tab-pane fade in active">
				<div class="row top-space">
					<div class="col-md-2">
					 	<ul class="nav nav-pills nav-stacked">
							<li class="active"><a data-toggle="pill" href="#inputNumber"><s:text
										name="inputNumber" /></a></li>
							<li><a data-toggle="pill" href="#menu1"><s:text
										name="inputGroupSms" /></a></li>
							<%-- <li><a data-toggle="pill" href="#menu3"><s:text
										name="smsReport" /></a></li> --%>
						</ul> 
					</div>

					<div class="col-md-9">
						<div class="tab-content">
							<div id="inputNumber" class="tab-pane fade in active">
								<div>
									<h5>
										<b><s:text name="enterMobileNumbers" /></b><sup
											style="color: red;">*</sup>
									</h5>
									<s:textarea name="mobileNos" theme="simple" maxlength="255"
										style="height:120%;resize:vertical" cssClass="form-control input-sm"
										onkeypress="return isMobileNumber(event)" />
									<h5>
										<b><s:text name="selectAMessageTemplate" /></b><sup
											style="color: red;">*</sup>
									</h5>
									<s:select name="" list="smsTemplateList"
										cssClass="form-control input-sm" headerKey=""
										headerValue="%{getText('txt.select')}"
										onchange="fillSms(this.value,'1')" id="smsTemplate" />
									<h5>
										<b><s:text name="composeSms" /></b><sup style="color: red;">*</sup>
										<div class="col-md-3 pull-right">
											<span id="txtCount">0</span>
											<s:text name="characters" />
										</div>
									</h5>
									<s:textarea name="message" id="composeMsg" maxlength="255"
									style="height:120%;resize:vertical"	cssClass="form-control input-sm" onchange="calcTextLength()" />

									<div class="row top-space">
										<div class="col-md-2">
											<input type="button" tabindex="33"
												class="btnSrch btn btn-small btn-success form-control"
												value="Send" name="button.save" id="butSend"
												onclick="sendSms()">
										</div>
										<div class="col-md-2">
											<input type="button" tabindex="35"
												class="btnSrch btn btn-warning btnClr form-control"
												value="Cancel" name="button.cancel" id="butSmsCancel"
												onclick="resetSms();">
										</div>
									</div>
								</div>
							</div>
							<div id="menu1" class="tab-pane fade" style="margin-top: 2%">
								<div class="row">
									<div class="col-md-2">
										<label><b><s:text name="groups" /></b></label>
									</div>
									<div class="col-md-5">
										<s:select name="selectedGroups" list="groupList" headerKey=""
											headerValue="%{getText('txt.select')}" theme="simple"
											id="selectedGroups" cssClass="form-control input-sm"
											onchange="groupChange()" />
									</div>
									<div class="col-md-2">
										<b><s:text name="selectAMessageTemplate" /></b>
									</div>
									<div class="col-md-3">
										<s:select name="" list="smsTemplateList"
											cssClass="form-control input-sm" headerKey=""
											headerValue="%{getText('txt.select')}"
											onchange="fillGroupSms(this.value)" id="groupTemplate" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<b><label id="numberCount"> 0&nbsp;</label> <s:text
												name="SMSmobileNumberSelected" /></b>
									</div>
								</div>

								<div class="row top-space">
									<div class="col-md-2">
										<b><s:text name="composeSms" /></b>
									</div>
									<div class="col-md-10">
										<s:textarea name="message" id="composeGroupMsg"
											maxlength="255" style="height:120%;resize:vertical" cssClass="form-control input-sm"
											onkeypress="calcTextLength()" />
									</div>
								</div>

								<div class="row top-space">
									<div class="col-md-2"></div>
									<div class="col-md-2">
										<input type="button" style="width: 100%" tabindex="33"
											class="btnSrch btn btn-small btn-success" value="Send"
											name="button.save" id="butSend" onclick="sendGroupSms()">
									</div>
									<div class="col-md-2">
										<input type="button" style="width: 100%" tabindex="35"
											class="btnSrch btn btn-warning btnClr" value="Cancel"
											name="button.cancel" onclick="resetGroupSms()">
									</div>
								</div>

							</div>
							<!-- <div id="menu3" class="tab-pane fade hide">oiururyry</div> -->
						</div>
					</div>
				</div>
			</div>

			<div id="groups" class="tab-pane fade">
				<div id="groupCreate">
					<div class="pull-right" style="margin-bottom: 1%">
						<button type="button" id="popupTemplate"
							class="slide_open btn btn-sm btn-primary form-control filterBtn filterBtn" onclick="addGroup()">
							<i class="fa fa-plus"  filterBtnaria-hidden="true">&nbsp;&nbsp;&nbsp;<s:text
									name="createAGroup" /></i>
						</button>
					</div>
					<br />
					<table
						class="table table-bordered table-hover table-striped aspect-detail fixedTable fillform">
						<thead>
							<th align='center' class="col-md-8"><s:text name="groupName" /></th>
							<th align='center'><s:text name="Edit" /></th>
							<th align='center'><s:text name="delete" /></th>
						</thead>
						<tbody id="group_contents">
						</tbody>
					</table>
				</div>

				<div class="row top-space" id="groupList" style="display: none">
					<div class="col-md-9 pull-right" style="margin-top: 2%">
						<div class="col-md-3">
							<b><s:text name="enterGroupName" /></b>
						</div>
						<div class="col-md-5">
							<s:textfield name="groupName" id="groupName"
								cssClass="form-control input-md" />
						</div>
						<div class="col-md-4">
							<input type="button" name="save" value="Save"
								onclick="saveGroup()" class="btn btn-sm btn-success" /> <input
								type="button" name="save" value="Cancel" onclick="cancelGroup()"
								class="btn btn-sm btn-danger" />
						</div>
					</div>

					<div class="col-md-3">
						<ul class="nav nav-pills nav-stacked">
							<li class="active"><a data-toggle="pill"
								href="#selectAFarmer"><s:text name="selectAFarmer" /></a></li>
							<li><a data-toggle="pill" href="#selectAFieldStaff"><s:text
										name="selectAFieldStaff" /></a></li>
							<li><a data-toggle="pill" href="#selectAUser"><s:text
										name="selectAUser" /></a></li>
						</ul>
					</div>

					<div class="col-md-9" id="groupFilterTable">
						<div class="tab-content">
							<div id="selectAFarmer" class="tab-pane fade in active"
								style="background-color: white;">
								<div style="margin-top: 4%">
								<button type="button" class="btn btn-sm btn-info" onclick="selectAllFarmersFn();">Select All</button>
									<table id="detail"></table>
									<div id="pagerForDetail"></div>
								</div>
							</div>
							<div id="selectAFieldStaff" class="tab-pane fade"
								style="background-color: white;">
								<div style="margin-top: 4%">
								<button type="button" class="btn btn-sm btn-info" onclick="selectAllFsFn();">Select All</button>
									<table id="fsDetail"></table>
									<div id="fsPagerForDetail"></div>
								</div>
							</div>
							<div id="selectAUser" class="tab-pane fade"
								style="background-color: white;">
								<div style="margin-top: 4%">
								<button type="button" class="btn btn-sm btn-info" onclick="selectAllWebUsersFn();">Select All</button>
									<table id="userDetail"></table>
									<div id="userPagerForDetail"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div id="template" class="tab-pane fade">
				<div class="pull-right"
					style="margin-bottom: 1%; background-color: white;">
					<button type="button" id="popupTemplate"
						class="slide_open btn btn-sm btn-warning form-control filterBtn" data-toggle="modal"
						data-target="#templatePopup">
						<i class="fa fa-plus" aria-hidden="true">&nbsp;&nbsp;&nbsp;<s:text
								name="addNewTemplate" /></i>
					</button>
				</div>

				<table
					class="table table-bordered table-hover table-striped aspect-detail fixedTable fillform"
					style="background-color: white;">
					<thead>
						<th align='center' class="col-md-4"><s:text
								name="templateName" /></th>
						<th align='center' class="col-md-6"><s:text name="contents" /></th>
						<th align='center'><s:text name="Edit" /></th>
						<th align='center'><s:text name="delete" /></th>
					</thead>
					<tbody id="template_contents">
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div id="templatePopup" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content"  style="width:600px;">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close"
						data-dismiss="modal">&times;</button>
					<h4 >
						<s:text name="addNewTemplate" />
					</h4>
				</div>
				<div id="validateError"
					style="text-align: center; padding: 5px 0 0 0; color: red;"></div>

				<div class="modal-body">
					<p id="templateErrorMsg" style="color: red;"></p>
					<div class="row" style="margin-bottom:1%">
					<div class="container">
						<div class="col-sm-2">
							<button type="button" class="btn  btn-danger form-control" onclick="dynmaicTemplateContent('1')"><s:property value="%{getLocaleProperty('farmer.SMSfirstName')}"/></button>
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn  btn-danger form-control" onclick="dynmaicTemplateContent('2')">Village
								Name</button>
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn btn-danger form-control" onclick="dynmaicTemplateContent('3')">Group
								Name</button>
						</div>
					</div></div></br>
					<table id="economyTable"
						class="table table-bordered aspect-detail fixedTable">
						<tr>
							<td><s:text name="templateName" /> <sup style="color: red;">*</sup>
							</td>
							<td><input type="text" id="templateName" name="templateName"
								maxlength="45" Class="form-control input-sm"></td>
						</tr>
						<tr>
							<td><s:text name="contents" /></td>
							<td><s:textarea name="templateMsg" id="templateMsg"
									theme="simple" maxlength="255"  style="height:120%;resize:vertical" cssClass="form-control input-sm" /></td>
						</tr>
						<tr>
							<td colspan="2"><span class="" id="button_create"><span
									class="first-child">
										<button class="save-btn btn btn-success" id="saveTemplate"
											type="button" onclick="saveTemplateInformation()">
											<font color="#FFFFFF"> <s:text name="save.button" />
											</font>
										</button>
								</span></span> <span class=""><span class="first-child">
										<button class="cancel-btn btn btn-sts" id="buttonEdcCancel"
											onclick="buttonEdcCancel()" type="button">
											<font color="#FFFFFF"> <s:text name="cancel" />
											</font>
										</button>
								</span></span></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

	</div>


	<button type="button" id="popupEditTemplate" class="hide"
		data-toggle="modal" data-target="#templateEditPopup"></button>
	<div id="templateEditPopup" class="modal fade" role="dialog">
		<div></div>
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-editTemplate-btn"
						class="close" data-dismiss="modal">&times;</button>
					<h4 style="border-bottom: solid 1px #567304;">
						<s:text name="editTemplate" />
					</h4>
				</div>
				<div id="validateError"
					style="text-align: center; padding: 5px 0 0 0; color: red;"></div>

				<div class="modal-body">
					<input type="hidden" id="templateId" />
					<p id="templateEditErrorMsg"></p>
					<div class="row" style="margin-bottom:1%">
						<div class="col-md-4">
							<button type="button" class="btn btn-success btn-danger form-control" onclick="dynmaicEditTemplateContent('1')"><s:property value="%{getLocaleProperty('farmer.SMSfirstName')}"/></button>
						</div>
						<div class="col-md-4">
							<button type="button" class="btn btn-success btn-danger form-control" onclick="dynmaicEditTemplateContent('2')">Village
								Name</button>
						</div>
						<div class="col-md-4">
							<button type="button" class="btn btn-success btn-danger form-control" onclick="dynmaicEditTemplateContent('3')">Group
								Name</button>
						</div>
					</div>
					<table class="table table-bordered aspect-detail fixedTable">
						<tr>
							<td><s:text name="editTemplate" /> <sup style="color: red;">*</sup>
							</td>
							<td><input type="text" id="editTemplateName"
								name="editTemplateName" maxlength="45"
								Class="form-control input-sm"></td>
						</tr>
						<tr>
							<td><s:text name="contents" /></td>
							<td><s:textarea name="editTemplateMsg" id="editTemplateMsg"
									theme="simple" maxlength="255" style="height:120%;resize:vertical" cssClass="form-control input-sm" /></td>
						</tr>
						<tr>
							<td colspan="2"><span class="" id="button_create"><span
									class="first-child">
										<button class="save-btn btn btn-success" id="saveTemplate"
											type="button" onclick="editTemplateInformation()">
											<font color="#FFFFFF"> <s:text name="save.button" />
											</font>
										</button>
								</span></span> <span class=""><span class="first-child">
										<button class="cancel-btn btn btn-sts" id="buttonEdcCancel"
											onclick="buttonEditCancel()" type="button">
											<font color="#FFFFFF"> <s:text name="cancel" />
											</font>
										</button>
								</span></span></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

	</div>

	<script>
    var selectedFarmerIds = new Array();
    var selectedFieldStaffIds = new Array();
    var selectedUserIds = new Array();
    var selectAllFarmers = "No";
    var selectAllFs = "No";
    var selectAllWebUsers = "No";
    jQuery(document).ready(function() {
    
    	//$(".loader").hide();
    	selectizeInputs();
        try{
    	loadTemplate();
        loadGroup();
        loadFarmerGrid();
        loadFieldStaffGrid();
        loadUserGrid();
        loadReportGrid() ;
        }catch(e){
        	
        }
        $("#detail").find("tbody").on("click", "tr", function(e) {
            var id = $(this).attr('id');
            if (isValExist(selectedFarmerIds,parseInt(id))) {
                selectedFarmerIds = removeVal(selectedFarmerIds,parseInt(id));
            } else {
                if (Object.prototype.toString
                    .call(selectedFarmerIds) !== '[object Array]') {
                    selectedFarmerIds = new Array();
                }
                selectedFarmerIds[selectedFarmerIds.length] = id;
            }
        });

        $("#fsDetail").on("click", "tr", function(e) {
            var id = $(this).attr('id');
            if (isValExist(
                    selectedFieldStaffIds,
                    parseInt(id))) {
                selectedFieldStaffIds = removeVal(
                    selectedFieldStaffIds,
                    parseInt(id));
            } else {
                if (Object.prototype.toString
                    .call(selectedFieldStaffIds) !== '[object Array]') {
                    selectedFieldStaffIds = new Array();
                }
                selectedFieldStaffIds[selectedFieldStaffIds.length] = id;
            }
        });
        
        $("#userDetail").on("click", "tr", function(e) {
            var id = $(this).attr('id');
            if (isValExist(selectedUserIds,parseInt(id))) {
            	selectedUserIds = removeVal(selectedUserIds,parseInt(id));
            } else {
                if (Object.prototype.toString
                    .call(selectedUserIds) !== '[object Array]') {
                	selectedUserIds = new Array();
                }
                selectedUserIds[selectedUserIds.length] = id;
            }
        });
        
    });
	
    function selectizeInputs(){
    	/* jQuery("#mobileNos").selectize({
    	    delimiter: ',',
    	    persist: false,
    	    create: function(input) {
    	        return {
    	            value: input,
    	            text: input
    	        }
    	    } 
    	}); */
    }
    
    function saveGroup() {
        var groupId = $('#groupId').val();
        var groupName = $('#groupName').val();
        var selectedFarmers = '';
        var selectedFieldStaffs = '';
        var selectedUsers = '';

        if (Object.prototype.toString
            .call(selectedFarmerIds) === '[object Array]') {
            selectedFarmers = selectedFarmerIds
                .join(",");
        }
        
        if (Object.prototype.toString
                .call(selectedFieldStaffIds) === '[object Array]') {
                selectedFieldStaffs = selectedFieldStaffIds
                    .join(",");
        }
        
        if (Object.prototype.toString
                .call(selectedUserIds) === '[object Array]') {
            	selectedUsers = selectedUserIds
                    .join(",");
        }

        if (groupName == '') {
            alert('Group name could not be blank');
            return false;
        }

        if (/^[a-zA-Z0-9- ]*$/.test(groupName) == false) {
            alert('Group name contain special characters!');
            return false;
        }
        var farmer_branchid = $("select[name=branchId]").val();
        var farmer_fpo = $("select[name=fpo]").val();
        var farmer_cluster = $("input[name=cluster]").val();
        var farmer_first_name = $("input[name=SMSSMSfirstName]").val();
        var farmer_last_name = $("input[name=SMSlastName]").val();
        var farmer_mobile = $("input[name=SMSmobileNumber]").val();
        var farmer_tehsil = $("input[name=city]").val();
        var farmer_village = $("input[name=village]").val();
        var farmer_status = $("select[name=status]").val();
        var farmer_district = $("input[name=district]").val();
        var farmer_state = $("input[name=state]").val();
        var farmer_cropNames = $("input[name=crop]").val();
        
        var fs_branchId = $("select[name=fs_branchId]").val();;
        var fs_agentId = $("input[name=fs_agentId]").val();
        var fs_SMSfirstName = $("input[name=fs_SMSfirstName]").val();
        var fs_SMSlastName = $("input[name=fs_SMSlastName]").val();
        var fs_SMSmobileNumber = $("input[name=fs_SMSmobileNumber]").val();
        var fs_status =  $("select[name=fs_status]").val();
        
        var wu_branchId = $("select[name=wu_branchId]").val();;
        var wu_username = $("input[name=wu_username]").val();
        var wu_SMSfirstName = $("input[name=wu_SMSfirstName]").val();
        var wu_SMSlastName = $("input[name=wu_SMSlastName]").val();
        var wu_SMSmobileNumber = $("input[name=wu_SMSmobileNumber]").val();
        var wu_status =  $("select[name=wu_status]").val();
        $(".loader").show();
        $("#myOverlay").show();
        
        $.post("sMSAlert_saveGroup.action", {
                groupId: groupId,
                groupName: groupName,
                selectedFarmerIds: selectedFarmers,
                selectedFieldStaffIds: selectedFieldStaffs,
                selectedUserIds: selectedUsers,
                selectAllFarmers:selectAllFarmers,
                selectAllFs:selectAllFs,
                selectAllWebUsers:selectAllWebUsers,
                farmer_branchid:farmer_branchid,
                farmer_fpo:farmer_fpo,
                farmer_cluster:farmer_cluster,
                farmer_first_name:farmer_first_name,
                farmer_last_name:farmer_last_name,
                farmer_mobile:farmer_mobile,
                farmer_tehsil:farmer_tehsil,
                farmer_village:farmer_village,
                farmer_status:farmer_status,
                farmer_district:farmer_district,
                farmer_state:farmer_state,
                farmer_cropNames:farmer_cropNames,
                
                fs_branchId:fs_branchId,
                fs_agentId:fs_agentId,
                fs_SMSfirstName:fs_SMSfirstName,
                fs_SMSlastName:fs_SMSlastName,
                fs_SMSmobileNumber:fs_SMSmobileNumber,
                fs_status:fs_status,
                
                wu_branchId:wu_branchId,
                wu_username:wu_username,
                wu_SMSfirstName:wu_SMSfirstName,
                wu_SMSlastName:wu_SMSlastName,
                wu_SMSmobileNumber:wu_SMSmobileNumber,
                wu_status:wu_status,
            },
            function(data,status) {
            	$(".loader").hide();
            	$("#myOverlay").hide();
                var jd = $.parseJSON(data);
                if (jd.success == '1') {
                    alert(jd.message);
                    jQuery("#groupCreate").show();
                    jQuery("#groupList").hide();
                    loadGroup();
                    resetGroupFields();
                } else {
                    alert(jd.message);
                   
                }
                selectAllFarmers = "No";
                selectAllFs = "No";
                selectAllWebUsers = "No";
            });
        
    };

    function isValExist(array, item) {
        for (var i = array.length - 1; i >= 0; i--) {
            if (array[i] == item) {
                return true;
            }
        }
        return false;
    }
	
    function removeVal(array, item) {
		for (var i = array.length - 1; i >= 0; i--) {
			if (array[i] == item) {
				array.splice(i, 1);
			}
		}
		return array;
	}
    
    
    function loadReportGrid() {
		jQuery("#reportTable")
				.jqGrid(
						{
							url : 'sMSAlert_data.action',
							pager : '#reporttoolbar',
							datatype : "json",
							colNames : [ /* '<s:text name="receiver.name"/>', */
									'<s:text name="receiver.mobileNo"/>',
									/* '<s:text name="receiver.type"/>', */
									/* '<s:text name="receiver.group"/>', */
									'<s:text name="receiver.time"/>',
									'<s:text name="sender.user"/>',
									'<s:text name="status"/>',
									'<s:text name="sender.deliveryStatus"/>' ],
							colModel : [
									/* {
										name : 'receiverName',
										index : 'receiverName',
										width : 125,
										sortable : false
									}, */
									{
										name : 'receiverMobNo',
										index : 'receiverMobNo',
										width : 125,
										sortable : false
									},
									/* {
										name : 'receiverType',
										index : 'receiverType',
										sortable : false,
										width : 125,
										stype : 'select',
										searchoptions : {
											value : '<s:property value="receiverTypeOptionValues"/>'
										}
									}, *//*  {
										name : 'receiverGroupName',
										index : 'receiverGroupName',
										width : 125,
										sortable : false
									}, */ {
										name : 'time',
										index : 'time',
										width : 125,
										sortable : false,
										search : false
									}, {
										name : 'user',
										index : 'user',
										width : 125,
										sortable : false
									}, {
										name : 'status',
										index : 'status',
										width : 125,
										sortable : false
									}, {
										name : 'updateStatus',
										index : 'updateStatus',
										width : 125,
										search : false,
										sortable : false
									} ],
							width : 1032,
							height : 250,
							rowNum : 10,
							rowList : [ 10, 25, 50 ],
							sortname : 'id',
							sortorder : 'desc',
							viewrecords : true,
							onSelectRow : function(id) {
							},
							onSortCol : function(index, idxcol, sortorder) {
								if (this.p.lastsort >= 0
										&& this.p.lastsort !== idxcol
										&& this.p.colModel[this.p.lastsort].sortable !== false) {
									$(this.grid.headers[this.p.lastsort].el)
											.find(
													">div.ui-jqgrid-sortable>span.s-ico")
											.show();
								}
							},
							loadComplete : function() {
							},
							grouping : false
						});
		jQuery("#reportTable").jqGrid('navGrid', '#reporttoolbar', {
			edit : false,
			add : false,
			del : false,
			search : false,
			refresh : true
		})
		jQuery("#reportTable").jqGrid('filterToolbar', {
			stringResult : true,
			searchOnEnter : false
		});

		colModel = jQuery("#reportTable")
				.jqGrid('getGridParam', 'colModel');
		$(
				'#gbox_' + $.jgrid.jqID(jQuery("#reportTable")[0].id)
						+ ' tr.ui-jqgrid-labels th.ui-th-column').each(
				function(i) {
					var cmi = colModel[i], colName = cmi.name;

					if (cmi.sortable !== false) {
						$(this).find('>div.ui-jqgrid-sortable>span.s-ico')
								.show();
					} else if (!cmi.sortable && colName !== 'rn'
							&& colName !== 'cb' && colName !== 'subgrid') {
						$(this).find('>div.ui-jqgrid-sortable').css({
							cursor : 'default'
						});
					}
				});
	}
    
    function loadFarmerGrid() {
        jQuery("#detail").jqGrid({
            url: 'sMSAlert_populateFarmersList.action',
            pager: '#pagerForDetail',
            mtype: 'POST',
            datatype: "json",
            styleUI: 'Bootstrap',
            colNames: [
            
                <s:if test='branchId==null'>
			   	          	'<s:text name="app.branch"/>',
						  </s:if> 
                '<s:text name="farmer.SMSfirstName"/>',
                '<s:property value="%{getLocaleProperty('SMSlastName')}" />',
                '<s:text name="SMSmobileNumber"/>',
                '<s:property value="%{getLocaleProperty('stateName')}" />',
                '<s:property value="%{getLocaleProperty('locality.name')}" />',
                '<s:property value="%{getLocaleProperty('city.name')}" />',
                '<s:text name="village.name"/>',
                '<s:property value="%{getLocaleProperty('crop')}" />',
                '<s:text name="status"/>',
            ],
            colModel: [
                <s:if test='branchId==null'>
				   				{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
				</s:if> 
                {name: 'SMSfirstName',index: 'SMSSMSfirstName',width: 125,sortable: false}, 
                {name: 'SMSlastName',index: 'SMSlastName',width: 125,sortable: false},
                {name: 'SMSmobileNumber',index: 'SMSmobileNumber',width: 125,sortable: false,align: "right"},
                {name: 'state',index: 'state',width: 125,sortable: false}, 
                {name: 'district',index: 'district',width: 125,sortable: false}, 
                {name: 'city',index: 'city',width: 125,sortable: false}, 
                {name: 'village',index: 'village',width: 125,sortable: false},
                {name: 'crop',index: 'crop',width: 125,sortable: false},
                {name: 'status',index: 'status',width: 125,sortable: false,search: true,stype: 'select',searchoptions: {value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>'}}
            ],
            height: 301,
            width: $("#baseDiv").width(),
            scrollOffset: 0,
            rowNum: 10,
            rowList: [10, 25, 50],
            sortname: 'id',
            sortorder: "desc",
            multiselect: true,
            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
            /*  onSelectRow: function(id){ 
					  document.updateform.id.value  =id;
			          document.updateform.submit();      
					},	 */

            onSortCol: function(index, idxcol, sortorder) {
                if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol &&
                    this.p.colModel[this.p.lastsort].sortable !== false) {
                    $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
                }
            },
            loadComplete: function() {
                setSelectedRows($("#detail"),
                    selectedFarmerIds);
                $(".loader").hide();
            },
            grouping: false
        });

        jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {
                edit: false,
                add: false,
                del: false,
                search: false,
                refresh: true
            }) // enabled refresh for reloading grid
        jQuery("#detail").jqGrid('filterToolbar', {
            stringResult: true,
            searchOnEnter: false
        }); // enabling filters on top of the header.

        colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
        $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
            ' tr.ui-jqgrid-labels th.ui-th-column').each(function(i) {
            var cmi = colModel[i],
                colName = cmi.name;

            if (cmi.sortable !== false) {
                $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
            } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
                $(this).find('>div.ui-jqgrid-sortable').css({
                    cursor: 'default'
                });
            }
        });

        jQuery("#jqgh_detail_cb").hide();
    }

    function loadFieldStaffGrid() {
        jQuery("#fsDetail").jqGrid({
            url: 'sMSAlert_populateFieldStaffList.action',
            pager: '#fsPagerForDetail',
            mtype: 'POST',
            datatype: "json",
            styleUI: 'Bootstrap',
            colNames: [
                <s:if test='branchId==null'>
			   	 '<s:text name="app.branch"/>',
				</s:if>
                '<s:text name="id"/>',
                '<s:text name="SMSfirstName"/>',
                '<s:text name="SMSlastName"/>',
                '<s:text name="SMSmobileNumber"/>',
                '<s:text name="status"/>',
            ],
            colModel: [
                <s:if test='branchId==null'>
				 {name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
				</s:if>
                {
                    name: 'agentId',
                    index: 'agentId',
                    width: 125,
                    sortable: false
                }, {
                    name: 'SMSfirstName',
                    index: 'SMSfirstName',
                    width: 125,
                    sortable: false
                }, {
                    name: 'SMSlastName',
                    index: 'SMSlastName',
                    width: 125,
                    sortable: false
                }, {
                    name: 'SMSmobileNumber',
                    index: 'SMSmobileNumber',
                    width: 125,
                    sortable: false,
                    align: "right"
                }, {
                    name: 'status',
                    index: 'status',
                    width: 125,
                    sortable: false,
                    search: true,
                    stype: 'select',
                    searchoptions: {
                        value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>'
                    }
                }
            ],
            height: 301,
            width: $("#baseDiv").width(),
            scrollOffset: 0,
            rowNum: 10,
            rowList: [10, 25, 50],
            sortname: 'id',
            sortorder: "desc",
            multiselect: true,
            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
            /*  onSelectRow: function(id){ 
					  document.updateform.id.value  =id;
			          document.updateform.submit();      
					},	 */

            onSortCol: function(index, idxcol, sortorder) {
                if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol &&
                    this.p.colModel[this.p.lastsort].sortable !== false) {
                    $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
                }
            },
            loadComplete: function() {
                setSelectedRows($("#fsDetail"),
                    selectedFarmerIds);
                $(".loader").hide();
            },
            grouping: false
        });

        jQuery("#fsDetail").jqGrid('navGrid', '#fsPagerForDetail', {
                edit: false,
                add: false,
                del: false,
                search: false,
                refresh: true
            }) // enabled refresh for reloading grid
        jQuery("#fsDetail").jqGrid('filterToolbar', {
            stringResult: true,
            searchOnEnter: false
        }); // enabling filters on top of the header.

        colModel = jQuery("#fsDetail").jqGrid('getGridParam', 'colModel');
        $('#gbox_' + $.jgrid.jqID(jQuery("#fsDetail")[0].id) +
            ' tr.ui-jqgrid-labels th.ui-th-column').each(function(i) {
            var cmi = colModel[i],
                colName = cmi.name;

            if (cmi.sortable !== false) {
                $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
            } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
                $(this).find('>div.ui-jqgrid-sortable').css({
                    cursor: 'default'
                });
            }
        });

        jQuery("#jqgh_fsDetail_cb").hide();
    }

    function loadUserGrid() {
        jQuery("#userDetail").jqGrid({
            url: 'sMSAlert_populateUserList.action',
            pager: '#userPagerForDetail',
            mtype: 'POST',
            datatype: "json",
            styleUI: 'Bootstrap',
            colNames: [
             <s:if test='branchId==null'>
			   	          	'<s:text name="app.branch"/>',
			</s:if> 
                '<s:text name="user.username"/>',
                '<s:text name="SMSfirstName"/>',
                '<s:text name="SMSlastName"/>',
                '<s:text name="SMSmobileNumber"/>',
                '<s:text name="status"/>',
            ],
            colModel: [
              <s:if test='branchId==null'>
				{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
			  </s:if>
                {
                    name: 'username',
                    index: 'username',
                    width: 125,
                    sortable: false
                }, {
                    name: 'SMSfirstName',
                    index: 'SMSfirstName',
                    width: 125,
                    sortable: false
                }, {
                    name: 'SMSlastName',
                    index: 'SMSlastName',
                    width: 125,
                    sortable: false
                }, {
                    name: 'SMSmobileNumber',
                    index: 'SMSmobileNumber',
                    width: 125,
                    sortable: false,
                    align: "right"
                }, {
                    name: 'status',
                    index: 'status',
                    width: 125,
                    sortable: false,
                    search: true,
                    stype: 'select',
                    searchoptions: {
                        value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>'
                    }
                }
            ],
            height: 301,
            width: $("#baseDiv").width(),
            scrollOffset: 0,
            rowNum: 10,
            rowList: [10, 25, 50],
            sortname: 'id',
            sortorder: "desc",
            multiselect: true,
            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
            /*  onSelectRow: function(id){ 
					  document.updateform.id.value  =id;
			          document.updateform.submit();      
					},	 */

            onSortCol: function(index, idxcol, sortorder) {
                if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol &&
                    this.p.colModel[this.p.lastsort].sortable !== false) {
                    $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
                }
            },
            loadComplete: function() {
                setSelectedRows($("#userDetail"),
                    selectedFarmerIds);
                $(".loader").hide();
            },
            grouping: false
        });

        jQuery("#userDetail").jqGrid('navGrid', '#userPagerForDetail', {
                edit: false,
                add: false,
                del: false,
                search: false,
                refresh: true
            }) // enabled refresh for reloading grid
        jQuery("#userDetail").jqGrid('filterToolbar', {
            stringResult: true,
            searchOnEnter: false
        }); // enabling filters on top of the header.

        colModel = jQuery("#userDetail").jqGrid('getGridParam', 'colModel');
        $('#gbox_' + $.jgrid.jqID(jQuery("#userDetail")[0].id) +
            ' tr.ui-jqgrid-labels th.ui-th-column').each(function(i) {
            var cmi = colModel[i],
                colName = cmi.name;

            if (cmi.sortable !== false) {
                $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
            } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
                $(this).find('>div.ui-jqgrid-sortable').css({
                    cursor: 'default'
                });
            }
        });

        jQuery("#jqgh_userDetail_cb").hide();
    }


    function setSelectedRows($grid, selectedIdsarr) {
        for (i = 0; i < selectedIdsarr.length; i++) {
            if (selectedIdsarr[i] != '' && selectedIdsarr[i] != null) {
                var $chcBox = $grid.find('#' + selectedIdsarr[i] +
                    ' input[type=checkbox]');
                if (!$chcBox.is(":checked")) {
                    $grid.jqGrid('setSelection', selectedIdsarr[i]);
                }
            }
        }
    }

    function saveTemplateInformation() {
        var templateName = jQuery("#templateName").val();
        var templateMsg = jQuery("#templateMsg").val();
        if(templateName==''){
        	error='<s:property value="%{getLocaleProperty('empty.templateName')}" />';
        	jQuery("#validateError").text(error);
        }
        else{
        jQuery.post("sMSAlert_saveTemplate.action", {
            templateName: templateName,
            templateContent: templateMsg
        }, function(result) {
            var json = $.parseJSON(result);
            if (json.success != '1') {
                jQuery("#templateErrorMsg").text(json.message);
            } else {
                jQuery("#templateName").val('');
                jQuery("#templateMsg").val('');
                jQuery("#templateErrorMsg").text('');
                document.getElementById("model-close-edu-btn").click();
                loadTemplate();
                loadTemplateData();   
                
            }
        });
        resetTemplate();
    }
       
    }
    

    function loadTemplateData() {
        jQuery.post("sMSAlert_populateListTemplates.action", {
        },function(result) {
            var json = $.parseJSON(result);
            jQuery('#smsTemplate').html('');
            var option="<option value=''>Select</option>";
            $.each(json, function(index, value) {
   				option+="<option value='"+value.smsTemplateId+"'>"+value.smsTemplate+"</option>";
   			 });
            jQuery("#smsTemplate").append(option);
            jQuery("#composeMsg").val("");
        });
    }
    
    function editTemplateInformation() {
        var templateName = jQuery("#editTemplateName").val();
        var templateMsg = jQuery("#editTemplateMsg").val();
        var templateId = jQuery("#templateId").val();
        jQuery.post("sMSAlert_saveTemplate.action", {
            templateName: templateName,
            templateContent: templateMsg,
            templateId: templateId
        }, function(result) {
            var json = $.parseJSON(result);
            if (json.success != '1') {
                jQuery("#templateEditErrorMsg").text(json.message);
            } else {
                jQuery("#editTemplateName").val('');
                jQuery("#editTemplateMsg").val('');
                jQuery("#templateErrorMsg").text('');
                document.getElementById("model-close-editTemplate-btn")
                    .click();
                loadTemplate();
                loadTemplateData();
            }
        });
        resetTemplate();
    }
   

    function editTemplate(id) {
        var nameId = "#templateName" + id;
        var msgId = "#templateMessage" + id;
        jQuery("#editTemplateName").val(jQuery(nameId).text());
        jQuery("#editTemplateMsg").val(jQuery(msgId).text());
        jQuery("#templateId").val(id);
        document.getElementById("popupEditTemplate").click();
        resetTemplate();
    }

    function editGroup(id) {
        //var nameId = "#templateName" + id;
        jQuery("#groupCreate").hide();
        jQuery("#groupList").show();
    	setExistingValues(id);
    }

    function setExistingValues(groupId) {
       	resetGroupFields();
        if (groupId != '' && groupId != null) {
        	jQuery.post("sMSAlert_findGroup.action", {groupId:groupId}, function(result) {
        		 var json = $.parseJSON(result);
        		 
        		 $.each(json, function(index, value) {
        			 if(index==0){
        				 jQuery("#groupName").val(value.groupName);
        			 }
        			 if(typeof value.profileId!= 'undefined' && typeof value.profileType!= 'undefined'){
        				 var id=value.profileId;
        				 if(value.profileType== 'FARMER'){
        					 if (Object.prototype.toString.call(selectedFarmerIds) !== '[object Array]') {
        	                            selectedFarmerIds = new Array();
        	                  }
        					 selectedFarmerIds[selectedFarmerIds.length] = id;
        					 var $chcBox = $("#detail").find('#'+id+'input[type=checkbox]');
        					 if (!$chcBox.is(":checked")) {
        						 $("#detail").jqGrid('setSelection', id);
        					 }
        					 
        					jQuery('.nav-pills a[href="#selectAFarmer"]').tab('show');
        					
        				 }else if (value.profileType == 'FIELD_STAFF') {
                             if (Object.prototype.toString.call(selectedFieldStaffIds) !== '[object Array]') {
                                  selectedFieldStaffIds = new Array();
                             }
                             selectedFieldStaffIds[selectedFieldStaffIds.length] = id;
                             var $chcBox = $("#fsDetail").find('#'+id+'input[type=checkbox]');
                             if (!$chcBox.is(":checked")) {
                            	 $("#fsDetail").jqGrid(
                            		'setSelection', id);
                                 }
	        					 
                             jQuery('.nav-pills a[href="#selectAFieldStaff"]').tab('show');
                             
                             } else if (value.profileType == 'USER') {
                                 if (Object.prototype.toString
                                     .call(selectedUserIds) !== '[object Array]') {
                                     selectedUserIds = new Array();
                                 }
                                 selectedUserIds[selectedUserIds.length] = id;
                                 var $chcBox = $("#userDetail").find('#'+id+'input[type=checkbox]');
                                 if (!$chcBox.is(":checked")) {
                                     $("#userDetail").jqGrid(
                                         'setSelection', id);
                                 }
                              jQuery('.nav-pills a[href="#selectAUser"]').tab('show');
                             }
        			 }
        		 });
        	});
        }
        jQuery("#groupId").val(groupId);
    }

    function resetGroupFields() {
        $("#detail").jqGrid('resetSelection');
        $("#fsDetail").jqGrid('resetSelection');
        $("#userDetail").jqGrid('resetSelection');
        $('#groupName').val('');
        jQuery("#groupId").val('');
        selectedFarmerIds = '';
        selectedFieldStaffIds = '';
        selectedUserIds = '';
    }

    function addGroup() {
        jQuery("#groupCreate").hide();
        jQuery("#groupList").show();
        resetGroupFields();
    }

    function cancelGroup() {
        jQuery("#groupCreate").show();
        jQuery("#groupList").hide();
        resetGroupFields();
    }

    function deleteTemplate(id) {
        if (confirm('<s:text name="confirm.delete"/>')) {
            jQuery.post("sMSAlert_removeTemplate.action", {
                templateId: id
            }, function(result) {
                var json = $.parseJSON(result);
                if (json.success != '1') {
                    jQuery("#templateEditErrorMsg").text(json.message);
                } else {
                    loadTemplate();
                    loadTemplateData();
                }
            });
        }
    }
    
    function deleteGroup(id) {
        if (confirm('<s:text name="confirm.delete"/>')) {
            jQuery.post("sMSAlert_removeGroup.action", {
            	groupId: id
            }, function(result) {
                var json = $.parseJSON(result);
                if (json.success != '1') {
                } else {
                    loadGroup();
                }
            });
        }
    }

    function loadTemplate() {
        jQuery.post("sMSAlert_listTemplates.action", {}, function(result) {
            var json = $.parseJSON(result);
            jQuery("#template_contents").html("");
            var bodyContent = '';
            $.each(json,function(index, value) {
                        bodyContent += ("<tr>");
                        bodyContent += ("<td id='templateName" +
                            value.id +
                            "' style='width:40%'>" +
                            value.templateName + "</td>");
                        bodyContent += ("<td id='templateMessage" +
                            value.id +
                            "' style='width:40%'>" +
                            value.message + "</td>");
                        bodyContent += ("<td style='width:10%' align='center'><i class='fa fa-pencil-square-o text-primary pointer_cursor' aria-hidden='true' onclick='editTemplate(" + value.id + ")'></i></td>");
                        bodyContent += ("<td style='width:10%' align='center'><i class='fa fa-trash text-danger pointer_cursor' aria-hidden='true' onclick='deleteTemplate(" + value.id + ")'></i></td>");
                        bodyContent += ("</tr>");
                    });
            jQuery("#template_contents")
                .append(bodyContent);
        });
    }
    
    function resetTemplate(){
    	jQuery("#templateName").val("");
    	jQuery("#templateMsg").val("");
    	jQuery("#validateError").text('');
    }

    function loadGroup() {
        jQuery.post("sMSAlert_listGroup.action", {}, function(result) {
            var json = $.parseJSON(result);
            jQuery("#group_contents").html("");
            var bodyContent = '';
            $.each(json, function(index, value) {
                bodyContent += ("<tr>");
                bodyContent += ("<td id='groupName" + value.id + "' style='width: 156px;'>" + value.groupName + "</td>");
                bodyContent += ("<td style='width:10%' align='center'><i class='fa fa-pencil-square-o text-primary pointer_cursor' aria-hidden='true' onclick='editGroup(" + value.id + ")'></i></td>");
                bodyContent += ("<td style='width:10%' align='center'><i class='fa fa-trash text-danger pointer_cursor' aria-hidden='true' onclick='deleteGroup(" + value.id + ")'></i></td>");
                bodyContent += ("</tr>");
            });
            jQuery("#group_contents")
                .append(bodyContent);
        });
    }

    function buttonEdcCancel() {
        jQuery("#templateName").val('');
        jQuery("#templateMsg").val('');
        jQuery("#validateError").text('');
        document.getElementById("model-close-edu-btn").click();
    }

    function buttonEditCancel() {
        jQuery("#templateName").val('');
        jQuery("#templateMsg").val('');
        jQuery("#validateError").text('');
        document.getElementById("model-close-editTemplate-btn").click();
    }
    
    function fillSms(value) {
        if (value.trim() != "") {
        	$.getJSON('sMSAlert_populateTemplateMsg.action?selectedTemplate='+value,function(jd){
        			jQuery("#composeMsg").val(jd.message);
        			calcTextLength();
      	   });
            
        } else {
            jQuery("#composeMsg").val("");
            calcTextLength();
        }
    }
    
    function fillGroupSms(value){
    	 if (value.trim() != "") {             
    		 $.getJSON('sMSAlert_populateTemplateMsg.action?selectedTemplate='+value,function(jd){
     			jQuery("#composeGroupMsg").val(jd.message);
     			calcTextLength();
   	   		 });
         } else {
             jQuery("#composeGroupMsg").val("");
         }
    }

    function calcTextLength() {
        jQuery("#txtCount").text((jQuery("#composeMsg").val()).length);
    }

    function sendSms() {
         
        var number = jQuery("#mobileNos").val();
        var message = jQuery("#composeMsg").val();
        var smsTemplate = jQuery("#smsTemplate").val();
        if(number==''){
        	alert('<s:text name="SMSmobileNumber.empty"/>');
    		return false;        	
        }
        if(number.length<10){
        	alert('<s:text name="SMSmobileNumber.validation"/>');
    		return false;
        }
        if(smsTemplate==''){
        	alert('<s:text name="smsTemplate.empty"/>');
    		return false; 
        }
        if(message==''){
        	alert('<s:text name="smsmessage.empty"/>');
    		return false; 
        }
      	$("#myOverlay").show();
   	 $(".loader").show(); 
        jQuery.post("sMSAlert_sendSms.action", {
            mobileNos: number,
            message: message
        }, function(result) {
        	 $(".loader").hide();
        	 $("#myOverlay").hide();
        	 var data = jQuery.parseJSON(result);
        	 alert(data.message);
            jQuery("#mobileNos").val('');
            jQuery("#composeMsg").val('');
            jQuery("#smsTemplate").val('');
            location.reload();
            //var data = jQuery.parseJSON(result);
            //jQuery("#composeMsg").val(data.message);
            //calcTextLength();
        });
    }
    
    function groupChange(){
    	var selectedGroupIds=jQuery("#selectedGroups").val();
    	
    	$.getJSON('sMSAlert_populateMobileNumberCountByGroups.action?selectedGroups='+selectedGroupIds,function(jd){
 		   var count=jd.data;
 		   $('#numberCount').text(count);
 	   });
    	
    }
    
    
    
    function sendGroupSms(){
    	var selectedGroupIds=jQuery("#selectedGroups").val();
    	var message=jQuery("#composeGroupMsg").val();
    	if(selectedGroupIds===null||selectedGroupIds==''){
    		alert('<s:text name="message.noContacts"/>');
    		return false;
    	}
    	
    	if(message==''){
    		alert('<s:text name="smsmessage.empty"/>');
    		return false;
    	}
    	$(".loader").show();
    	$("#myOverlay").show();
        jQuery.post("sMSAlert_sendGroupSms.action", {
        	groupIds:selectedGroupIds.toString(),
        	message:message
        }, function(result) {
        	$(".loader").hide();
        	$("#myOverlay").hide();
        	 var data = jQuery.parseJSON(result);
        	 alert(data.message);
        	resetGroupSms();
         });
    }
    
    function resetGroupSms(){
    	jQuery("#selectedGroups").val('');
    	jQuery("#groupTemplate").val('');
    	jQuery("#composeGroupMsg").val('');
    	$("#numberCount").text('0');
    }
    
    function resetSms(){
    	jQuery("#mobileNos").val('');
    	jQuery("#smsTemplate").val('');
    	jQuery("#composeMsg").val('');
    }
    
function isMobileNumber(evt) {
 	    evt = (evt) ? evt : window.event;
 	    var charCode = (evt.which) ? evt.which : evt.keyCode;
 	    if(charCode == 44||charCode == 39||charCode == 93||charCode == 36||charCode == 35){
 	    	return true;
 	    }
 	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
 	        return false;
 	    }
 	    return true;
 	}
 	
function updateDelivery(smsHistoryId, smsHistoryDetailId) {

	jQuery.post("sMSAlert_getDeliveryStatus.action", {
		"id" : smsHistoryId,
		"smsHistoryDetailId" : smsHistoryDetailId,
		dt : new Date()
	}, function(result) {
		var jsonResult = jQuery.parseJSON(result);
		for (var i = 0; i < jsonResult.length; i++) {
			jQuery("#status_" + jsonResult[i].smsHistoryDetailId).html(
					jsonResult[i].status);
			// Change button disabled based on status 
			if ("SUBMITTED" == jsonResult[i].status
					|| "INVALID_JOB_ID" == jsonResult[i].status
					|| "System Error" == jsonResult[i].status) {
				jQuery(
						"#dlvryStatusBtn_"
								+ jsonResult[i].smsHistoryDetailId)
						.attr("disabled", false);
				jQuery(
						"#dlvryStatusBtn_"
								+ jsonResult[i].smsHistoryDetailId)
						.removeAttr("disabled");
			} else {
				jQuery(
						"#dlvryStatusBtn_"
								+ jsonResult[i].smsHistoryDetailId)
						.attr("disabled", true);
			}
		}
	});
}

function dynmaicTemplateContent(type){
	var templateMsg = jQuery("#templateMsg").val();
	if(type=='1'){
		templateMsg+="#First Name#";
	}
	else if(type=='2'){
		templateMsg+="#Village Name#";
	}
	else if(type=='3'){
		templateMsg+="#Group Name#";
	}
	jQuery("#templateMsg").val(templateMsg);
}
function dynmaicEditTemplateContent(type){
	var editTemplateMsg = jQuery("#editTemplateMsg").val();
	if(type=='1'){
		editTemplateMsg+="#First Name#";
	}
	else if(type=='2'){
		editTemplateMsg+="#Village Name#";
	}
	else if(type=='3'){
		editTemplateMsg+="#Group Name#";
	}
	jQuery("#editTemplateMsg").val(editTemplateMsg);
}
function selectAllFarmersFn(){
	selectAllFarmers = "yes";
	selectAllFs = "No";
	selectAllWebUsers = "No";
	//checkAllCheckBoxes("detail");
	alert("All Selected");
	
}

function selectAllFsFn(){
	
	selectAllFarmers = "No";
	selectAllFs = "yes";
	selectAllWebUsers = "No";
	//checkAllCheckBoxes("fsDetail");
	alert("All Selected");
}

function selectAllWebUsersFn(){
	
	selectAllFs = "No";
	selectAllFarmers = "No";
	selectAllWebUsers = "yes";
	//checkAllCheckBoxes("userDetail");
	alert("All Selected");
}

</script>
<div class="loader">
</div>
<div id="myOverlay"></div>
</body>