<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
<style>
.ui-jqgrid .ui-jqgrid-htable th div {
	height: auto;
	overflow: hidden;
	padding-right: 4px;
	position: relative;
	white-space: normal !important;
}

th.ui-th-column div {
	white-space: normal !important;
	height: auto !important;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	/*height: auto !important;*/
}
</style>
</head>
<body>
	<script type="text/javascript">
	var temp=0;
    jQuery(document).ready(function(){
    	jQuery(".well").hide();
    	onFilterData();
	<s:if test='branchId==null'>
	
	</s:if>
	$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.updateView();
     $("#daterange").data().daterangepicker.updateCalendars();
	$('.applyBtn').click();
	
	jQuery("#detail").jqGrid(
			{
				url:'fieldStaffManagementReport_detail.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			    styleUI : 'Bootstrap',
			   	mtype: 'POST',
			   	postData:{				 
					  "startDate" : function(){
						  return  document.getElementById("hiddenStartDate").value;
					  },
					  "endDate" : function(){
						  return document.getElementById("hiddenEndDate").value;
					  },
					  <s:if test='branchId==null'>
					  "branchIdParma" : function(){
						  return document.getElementById("branchIdParma").value;
					  },
					  </s:if>
					  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					  "subBranchIdParma" : function(){
						  return document.getElementById("subBranchIdParam").value;
					  },
					  </s:if>
					  "filter.agentId" : function(){
						return document.getElementById("agentId").value;
					  }
				},	
			   	colNames:[
			   	       <s:if test='branchId==null'>
						  '<s:text name="app.branch"/>',
						  </s:if>
						  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							
								'<s:text name="app.subBranch"/>',
							</s:if>
			  		   	  '<s:text name="txnTime"/> ',
			  		   	  '<s:property value="%{getLocaleProperty('agentId')}"/>',
			  		 	  '<s:property value="%{getLocaleProperty('agentName')}"/>',
			  		 	<s:if test="currentTenantId=='wilmar'"> 
			  		 	'<s:property value="%{getLocaleProperty('startAddress')}"/>',
			  		 	  '<s:property value="%{getLocaleProperty('endAddress')}"/>',
			  		 	</s:if>
			  		   	 /*  '<s:text name="agentId"/>',
						  '<s:text name="agentName"/>', */
						 
						 // '<s:property value="%{getLocaleProperty('totDist')}"/>',
			  		   	  '<s:text name="view"/>',
			  		   	  
	 	      	 ],
			   	colModel:[
					<s:if test='branchId==null'>
					{name:'branchId',index:'branchId',sortable: false},	   				   		
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					
						{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					</s:if>
					{name:'txnTime',index:'txnTime',sortable:false},
					{name:'agentId',index:'agentId',sortable:false},
					{name:'agentName',index:'agentName',sortable:false},
					<s:if test="currentTenantId=='wilmar'"> 
					{name:'startAddress',index:'startAddress',sortable:false},
					{name:'endAddress',index:'endAddress',sortable:false},
					</s:if>
					//{name:'totDist',index:'totDist',sortable:false},
					{name:'button',index:'button',sortable:false,align:'center'},
					
			   	],
			   height: 380, 
			   width: $("#baseDiv").width(),
			   scrollOffset: 19,     
			   sortname:'txnTime',
			   sortorder: "desc",
			   shrinkToFit:true,
			   rowNum:10,
			   rowList : [10,25,50],
			   viewrecords: true,  
			/*    beforeProcessing: function(data){
				   $(data.rows).each(function(k,v){
					  var $self = $(this);
					  var address = "";
					  var coords =  v.cell[5].split("|");
					  var latlng = {lat: parseFloat(coords[0]), lng: parseFloat(coords[1])};		
					  	geocoder.geocode({'location': latlng}, function(results, status) {
					  		if (status === 'OK') {
								 address = results[1].formatted_address;
								 console.log(address);
							 }
						});
				  });
			   }, */
	           onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	          }
			});

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
		    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
	
	jQuery("#generate").click( function() {
		reloadGrid();	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	

	function reloadGrid(){
		var d1=	jQuery('#daterange').val();
		var d2= d1.split("-");
		//	alert(d1);
		var value1= d2[0];
		//alert(value1);
		var value2= d2[1];
		//alert(value2);
	document.getElementById("hiddenStartDate").value=value1;
	
	document.getElementById("hiddenEndDate").value=value2;
	var startDate=new Date(document.getElementById("hiddenStartDate").value);
		//alert(startDate);
		var endDate=new Date(document.getElementById("hiddenEndDate").value);
		//alert(startDate);
		if (startDate>endDate){
			alert('<s:text name="date.range"/>');
		}else{ 
			jQuery("#detail").jqGrid('setGridParam',{url:"fieldStaffManagementReport_detail.action?",page:1}).trigger('reloadGrid');
		}	
	}

	function clear(){		
		/* document.getElementById('agentId').selectedIndex=0;
		document.getElementById('branchIdParma').selectedIndex=0; */
		resetReportFilter();
		document.form.submit();
	}
	
});
    
	function onFilterData(){
		callAjaxMethod("fieldStaffManagementReport_populateAgentList.action","agentId");
	}
	function callAjaxMethod(url,name){
		var cat = $.ajax({
			url: url,
			async: true, 
			type: 'post',
			success: function(result) {
				insertOptions(name,JSON.parse(result));
			}        	

		})
		
	}
	
    function redirectToMapView(agentId,txnTime,id){	
    	var dts = txnTime.split("-");
    	var dt = dts[1]+"/"+dts[2]+"/"+dts[0]
    	document.getElementById("mapViewAgentId").value=agentId;	
    	document.getElementById("mapViewStartDate").value=dt;
    	document.getElementById("mapViewEndDate").value=dt;
    	document.getElementById("id").value=id;
    	document.getElementById("fieldStaffMapView").submit();
    }


    function exportXLS() {
        if (isDataAvailable("#detail")) {
            jQuery("#detail").setGridParam({postData: {rows: 0}});
            jQuery("#detail").jqGrid('excelExport', {url: 'fieldStaffManagementReport_populateXLS.action?'});
    }     else {
            alert('<s:text name="export.nodata"/>');
        }
    }

</script>

	<s:form name="form" action="fieldStaffManagementReport_list">
		
		<div class="appContentWrapper marginBottom">
				<section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
					<label for="startingDate"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
					class="form-control input-sm " />
					</div>
				</div>
				<div class="filterEle">
					<label for="filter.agentId"><s:property
							value="%{getLocaleProperty('agentName')}" /></label>
					<div class="form-element">
						<s:select list="{}" name="filter.agentId"
					id="agentId" headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control select2" />
					</div>
				</div>
					<s:if
					test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
						<div class="filterEle">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element ">
								<s:select name="branchIdParma" id="branchIdParma"
									list="parentBranches" headerKey="" headerValue="Select"
									cssClass="select2" onchange="populateChildBranch(this.value)" />

							</div>
						</div>
					</s:if>
					
					<div class="filterEle">
						<label for="branchIdParam"><s:text name="app.subBranch" /></label>
						<div class="form-element">
							<s:select name="subBranchIdParam" id="subBranchIdParam"
								list="subBranchesMap" headerKey="" headerValue="Select"
								cssClass="input-sm form-control select2" />
						</div>
					</div>

				</s:if>
				<s:else>
				    <s:if test='branchId==null'>
					<div class="filterEle">
					<label for="txt"><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParma" id="branchIdParma"
							list="branchesMap" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				</s:else>
				
			
				<div class="filterEle" style="margin-top: 2%;margin-right: 0%;">
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm" aria-hidden="true"
						 id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
			</div>
			</section>
		</div>
	</s:form>
	
	<div class="appContentWrapper marginBottom">
		<!-- <div class="flex-layout reportData">
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container">
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Farmers&nbsp;<i
							class="fa fa-user"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Cultivation Cost&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Others&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Total Cost&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
				</div>
			</div>
		</div> -->
		
		<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
						type="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
					
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>
			
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div>
	


	<s:form id="fieldStaffMapView"
		action="fieldStaffManagementReport_mapView" target="_blank"
		method="post">
		<s:hidden id="mapViewAgentId" name="filter.agentId" />
		<s:hidden id="mapViewStartDate" name="startDate" />
		<s:hidden id="mapViewEndDate" name="endDate" />
		<s:hidden id="id" name="id" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>