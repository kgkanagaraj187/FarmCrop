<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/ese-tags" prefix="e"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<head>
<META name="decorator" content="swithlayout">

<style type="text/css">
body {
	font: normal normal normal 10px/1.5 Arial, Helvetica, sans-serif;
}

.ui-dialog-osx {
	-moz-border-radius: 0 0 8px 8px;
	-webkit-border-radius: 0 0 8px 8px;
	border-radius: 0 0 8px 8px;
	border-width: 0 8px 8px 8px;
}

.popupBackground {
	position: absolute;
	display: none;
	background-color: #000;
	opacity: 0.5;
	filter: alpha(opacity = 50);
	width: 100%;
	height: 100%;
	top: 0;
	left: 0;
}

.popupPanelContent {
	background: #fff;
	border: 5px solid #aad2ca;
	box-shadow: 1px 2px 15px #b8ebed;
}

.popClose {
	width: 20px;
	height: 20px;
	position: absolute;
	top: 10px;
	right: 10px;
	cursor: pointer;
}

.popClose:hover {
	cursor: pointer;
}

.popAlert {
	display: none;
	clear: both;
	position: absolute;
	width: 400px;
	margin-left: -200px;
	z-index: 1;
}
</style>
</head>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<div id='warn' class="error">
	<s:actionerror />
</div>

<script type="text/javascript">

$.fn.regexMask = function(mask) {
    $(this).keypress(function (event) {
        if (!event.charCode) return true;
        var part1 = this.value.substring(0, this.selectionStart);
        var part2 = this.value.substring(this.selectionEnd, this.value.length);
        if (!mask.test(part1 + String.fromCharCode(event.charCode) + part2))
            return false;
    });
};

$(document).ready(function(){
	//$( "#dialog-form" ).hide();
	
	latestVersion();
	
	var mask = new RegExp('^[a-zA-Z0-9\\-\\s]+$')
    $("#popName").regexMask(mask); 
    
	$( "#popUp" ).hide();
	//$( "#tabs" ).tabs();
	
	$("#detail").jqGrid(
			{
			url:'device_data.action',
			pager: '#pagerForDetail',
			mtype: 'POST',
			datatype: "json",
			styleUI : 'Bootstrap',
			colNames:[
					  <s:if test='branchId==null'>
						 '<s:text name="app.branch"/>',
					  </s:if>
						 
						 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							'<s:text name="app.subBranch"/>',
						</s:if>
		  		   	  '<s:text name="profile.device.code" />',
		  		      '<s:text name="profile.device.name" />',
		  		      '<s:text name="profile.device.serialNumber" />',
		  		   	  '<s:text name="profile.device.status" />',
		  		      '<s:text name="agentName" />',
		  		    '<s:text name="surveyMaster.version" />',
		  		    '<s:text name="lastLogin" />'
		      	 ],

		   colModel : [		
		<s:if test='branchId==null'>
			{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
				value: '<s:property value="parentBranchFilterText"/>',
				dataEvents: [ 
				          {
				            type: "change",
				            fn: function () {
				            	console.log($(this).val());
				             	getSubBranchValues($(this).val())
				            }
				        }]
				
				}},	   				   		
			</s:if>
	<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
		{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
	</s:if>
		      	      {name:'code',index:'code',sortable:true, width:125},
		      	      {name:'name',index:'name', sortable:true, width:125},
		      	      {name:'serialNumber',index:'serialNumber',sortable:true, width:125},
		      	      {name:'enabled',index:'enabled',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="deviceStatus1"/>;0:<s:text name="deviceStatus0"/>' }},
		      	      {name:'agentName',index:'agentName', sortable:true, width:125},
		      	    {name:'appversion',index:'appversion', sortable:true,search:false, width:125},
		      	    {name:'logintime',index:'logintime', sortable:true,search:false, width:125}
		      	 ],			
		    height: 301, 
			width: $("#baseDiv").width(),
			scrollOffset: 0,
			rowNum:10,
			rowList : [10,25,50],
			sortname: 'id',
			sortorder: 'desc',
			viewrecords: true,
		    onSelectRow: function(id){ 
			  document.deviceDetailForm.id.value  =id;
	          document.deviceDetailForm.submit();      
			},		
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        }
	   });
	
	jQuery("#detail").jqGrid("setLabel","branchId","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","code","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","name","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","serialNumber","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","enabled","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","agentName","",{"text-align":"center"});
	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});			

	colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;

        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        }
    });

    jQuery("#unRegDetail").jqGrid(
			{
			
				url:'device_dataUnReg.action',
			pager: '#unRegPagerForDetail',
			datatype: "json",
			styleUI : 'Bootstrap',
			colNames:[
			          '<s:text name="device.transactionDate" />',
		  		      '<s:text name="device.serialNo" />',
		  		      '<s:text name="device.fieldStaff" />',
		  		      '<s:text name="Action"/>'
		  			
		      	 ],

		   colModel : [
					 {name:'modifiedTime',index:'modifiedTime',sortable:false,search:false, width:125},
		      	     {name:'serialNo',index:'serialNo', sortable:true, width:125},
		      	     {name:'lastUpdatedUsername',index:'lastUpdatedUsername',sortable:true,width:125},
		      	   	 {name:'update',index:'update', sortable:false, width:125,search:false,align:'center'}
		      	 
		      	 ],			
		    height: 255, 
			width: $("#baseDiv").width(),
			rowNum:10,
			rowList : [10,25,50],
			sortname: 'id',
			sortorder: 'desc',
			viewrecords: true,
		    	
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        }
	   });
   
    jQuery("#unRegDetail").jqGrid("setLabel","modifiedTime","",{"text-align":"center"});
    jQuery("#unRegDetail").jqGrid("setLabel","serialNo","",{"text-align":"center"});
    jQuery("#unRegDetail").jqGrid("setLabel","lastUpdatedUsername","",{"text-align":"center"});
    jQuery("#unRegDetail").jqGrid("setLabel","update","",{"text-align":"center"});
    
	jQuery("#unRegDetail").jqGrid('navGrid','#unRegPagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
	jQuery("#unRegDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});			
	 
	colModel = jQuery("#unRegDetail").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + $.jgrid.jqID(jQuery("#unRegDetail")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;

        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        }
    });

});

function deleteUnregisteredDevice(deviceId){
	var devId=deviceId;
	if(confirm('<s:text name="confirm.delete"/>')){
	  $.post("device_deleteUnRegDevice.action",{devId:devId},
	        	function(data,status){
      	    alert('<s:text name="msg.removed"/>');
      	    location.reload();
	     });
      }
}

function openModel(serialNo,devId){
	$("#popSerialNoTxt").html(serialNo);
	$("#popSerialNo").html(serialNo);
	document.getElementById("popDeviceId").value=devId;
	//enablePopup();
}
function exportXLS(){
	 if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'device_populateXLS.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}

function saveModel(){
	var valid=true;
	var deviceId=jQuery("#popDeviceId").val();
    var deviceName = jQuery("#popName").val();
    var deviceSerialNo = jQuery("#popSerialNo").html();
    var deviceIsRegistered = '0';
    if(deviceName =="" ){
    	alert('<s:text name="empty.name"/>');
    
    	valid=false;
    	
    }else{								
    	//alert("Heerererre");			
		jQuery.post("device_updateUnReg.action",{devId:deviceId,deviceIsRegistered:deviceIsRegistered,deviceName:deviceName},function(data,status){
			cancelModel();
			$("#dialog-form").hide();
			location.reload();
		}); 
    }
}
 
function cancelModel(){
	document.getElementById("popDeviceId").innerHTML="";
	jQuery("#popName").val("");
	document.getElementById("popSerialNo").innerHTML="";
	$( "#popUp" ).hide();
}

function enablePopup(){		
	$('body').css('overflow','hidden');
	$('#popupBackground').css('width','100%');
	$('#popupBackground').css('height',getWindowHeight());
	$('#popupBackground').css('top','0');
	$('#popupBackground').css('left','0');
	$('#popupBackground').show();
	$('#popUp').css({top:'50%',left:'50%',margin:'-'+($('#popUp').height() / 2)+'px 0 0 -'+($('#popUp').width() / 2)+'px'});
	$('#popUp').show();
	//window.location.hash="#popUp";
}

function disableExtendAlert(){
	$('#pendingpopUpErrMsg').html('');
	$('#popupBackground').hide();
	$('#popUp').hide();
	$('#popupPanelContent').hide();
	$('body').css('overflow','hidden');
	
}

function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}

function latestVersion() {
	
	$.post("device_populateVersion.action",{},function(data){
		var jsonData = $.parseJSON(data);
		$.each(jsonData, function(index, value) {
		if(value.name!=null&&value.name!==undefined&&value.name!=''){
			$("#version").text(value.name);
		}
	}); 
 });
}
</script>
<body>
	<div id="baseDiv" style="width:97%"> 	</div>
	<div class="appContentWrapper marginBottom">
		<ul class="nav nav-pills">
			<li class="active" id="tab1"><a data-toggle="pill" href="#tabs-1"><s:text
						name="device.reg" /></a></li>
			<li id="tab2"><a data-toggle="pill" href="#tabs-2"><s:text
						name="device.unreg" /> </a></li>
		</ul>
	</div>

	
		<div class="tab-content">
			<div id="tabs-1" class="tab-pane fade in active">
				<s:if test="branchId!=null || currentTenantId=='pratibha'">
					<sec:authorize ifAllGranted="profile.device.create">
						<input class="btn btn-sts" type="BUTTON" id="add"
							value="<s:text name="create.button"/>"
							onclick="document.createform.submit()" />
					</sec:authorize>
				</s:if>
				
						
				<div class="dropdown" align="right">
				<span>
					<s:text name="Latest Version"/>: <span id="version" />	</span>				
					<button id="dLabel" class="btn btn-primary btn-sts" type="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
						<li role="presentation"><a href="#" onclick="exportXLS()"
							tabindex="-1" role="menuitem"> <s:text name="excel" />
						</a></li>
						
					</ul>
				</div>
				<div class="appContentWrapper ">
				<div style="width: 99%;" id="baseDiv">

					<table id="detail"></table>

					<div id="pagerForDetail"></div>
				</div>
				</div>
			</div>

			<div id="tabs-2" class="tab-pane fade">

				<div id="popUp" class="popAlert">
					<div class="popupPanelContent" style="padding: 25px;">
						<div class="popClose" onclick="disableExtendAlert();"></div>

					</div>
				</div>
			<div class="appContentWrapper ">
				<div style="width: 100%;" id="baseDiv2">
					<table id="unRegDetail"></table>
					<div id="unRegPagerForDetail"></div>
				</div>
				</div>
			</div>

		</div>

		<s:form id="deviceForm" name="createform" action="device_create">
			<s:hidden id="deviceType" name="deviceType" />
			<s:hidden name="command" />
		</s:form>
		<s:form name="deviceDetailForm" id="deviceDetailForm"
			action="device_detail">
			<s:hidden id="id" name="id" />
			<s:hidden id="currentPage" name="currentPage" />
			<s:hidden name="command" />
		</s:form>

		<s:form id="deleteForm" action="device_delete">
			<s:hidden name="currentPage" />
			<s:hidden id="deleteId" name="id" />
			<s:hidden name="tabIndex" value="#tab2" />
			<s:hidden name="device.id" />
			<s:hidden name="device.code" />
			<s:hidden name="device.serialNumber" />
			<s:hidden name="device.name" />
		</s:form>


	<div id="slide" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-btn" class="close"
						data-dismiss="modal">&times;</button>
					<h4>
						<s:text name="device.unreg" />
					</h4>
				</div>

				<div class="modal-body">
					<table id="unRegTable" class="table table-bordered aspect-detail">

						<s:hidden id="popDeviceId" name="device.id" />
						<s:hidden id="unreg" name="device.isRegistered" value="1" />
						<s:hidden name="device.serialNo" id="popSerialNoTxt" />


						<tr class="odd">
							<td><s:text name="device.serialNo" /><sup
								style="color: red;">*</sup></td>
							<td><div id="popSerialNo"></div></td>
						</tr>

						<tr class="odd">
							<td><s:text name="device.name" /><sup style="color: red;">*</sup></td>
							<td><s:textfield id="popName" name="device.name"
									maxlength="20" cssClass="form-control" /></td>
						</tr>

						<tr class="odd">
							<td colspan="2">
								<button type="button" Class="btnSrch btn btn-success"
									onclick="saveModel();" id="save">
									<s:text name="save" />
								</button>
								<button type="button" Class="btnClr btn btn-warning"
									onclick="cancelModel();" id="cancel" data-dismiss="modal">
									<s:text name="Cancel" />
								</button>
							</td>
						</tr>



					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
