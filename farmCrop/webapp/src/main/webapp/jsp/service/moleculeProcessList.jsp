<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>


<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
<!--<link href="css/main_table.css" rel="stylesheet" type="text/css" />-->
<s:head />
<style type="text/css">
.alignTopLeft {
	float: left;
	width: 6em;
}

.view {
	display: table-cell;
	background-color: #d2dae3;
}

.alignTopLeft {
	float: left;
	width: 6em;
}

select {
	width: 120px !important;
}

input[type="text"] {
	width: 120px;
}

.datepicker_clear {
	display: none;
}

.column-1 h3, .column-2 h3, .column-3 h3 {
	/*background: none repeat scroll 0 0 #6F9505 !important;*/
	border: 1px solid #fff !important;
	color: #fff !important;
	padding: 5px;
	margin: 0;
	text-align: left;
}

.alignCenter {
	text-align: center !important;
}

.alignLeft {
	text-align: left !important;
}

.alignRight {
	text-align: right !important;
}

.generalTable th {
	marign-top: 10px;
}

.column-2, .column-3 {
	margin-top: 25px;
}

.borNone {
	border-right: none !important;
	border-left: none !important;
	border-top: none !important;
	border-bottom: none !important;
}

.div.error {
	color: #FD0000;
	font-size: 12px;
	/*padding: 5px 10px 5px 0;*/
	text-align: left;
	width: auto;
	margin-bottom: 0px !important;
}
</style>
</head>
<body>
	<script>
var typezz='';
jQuery(document).ready(function(){
	
	typezz=<%out.print("'" + request.getParameter("type") + "'");%>
	$('.dLabel').hide();
	if(typezz=="report"){
		$('.CompareButton').hide();
		$('.dLabel').show();
	}
	if(typezz=='service'){
		$(".breadCrumbNavigation").html('');
		$(".breadCrumbNavigation").append("<li><a href='#'>Service</a></li>");
		$(".breadCrumbNavigation").append("<li><a href='moleculeProcess_list.action?type=service'>Molecule</a></li>");
		$("#typez").val('service');
	}else if (typezz=='report'){
		$(".breadCrumbNavigation").html('');
		$(".breadCrumbNavigation").append("<li><a href='#'>Report</a></li>");
		$(".breadCrumbNavigation").append("<li><a href='moleculeProcess_list.action?type=report'>Molecule Report</a></li>");
		$("#typez").val('report');		
	}else{
		$(".breadCrumbNavigation").html('');
		$(".breadCrumbNavigation").append("<li><a href='#'>Service</a></li>");
		$(".breadCrumbNavigation").append("<li><a href='moleculeProcess_list.action?type=service'>Molecule</a></li>");
		$("#typez").val('service');
	}
	
	
	jQuery("#detail").jqGrid(
			{
			   	url:'moleculeProcess_data.action',
			   	pager: '#pagerForDetail',	  
			   	datatype: "json",	
			   	mtype: 'POST',
			    styleUI : 'Bootstrap',
			    postData:{
					  "landHolding" : function(){
							  return "<%=request.getParameter("landHolding")%>";
					  }
				},
			   	colNames:[
				          <s:if test='branchId==null'>
							'<s:text name="app.branch"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							'<s:text name="app.subBranch"/>',
							</s:if>
							'<s:text name="Date"/>',
			  		   	  '<s:text name="Lot No"/>',
			  		   	'<s:property value="%{getLocaleProperty('moleculeTypeName')}" />',
			  		    '<s:property value="%{getLocaleProperty('Status')}" />',
			  		  <s:if test='typez=="report"'>
			  		  '<s:property value="%{getLocaleProperty('File')}" />'
			  		</s:if>
			  		 //'<s:property value="%{getLocaleProperty('Status')}" />'
			  		 /*  <s:if test="isKpfBased==1">
			  		  <s:if test="currentTenantId!='gsma''">
			  		  '<s:property value="%{getLocaleProperty('samithi.type')}" />'
			  		</s:if>
			  		    </s:if> */
			      	 	 ],
			   	colModel:[
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
				   			        }
				   			    ]
				   			
				   			}},	   				   		
				   		</s:if>
				   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>
				   	{name:'cropYieldDate',index:'cropYieldDate',width:125,sortable:true,search:false},
			   		{name:'landHolding',index:'landHolding',width:125,sortable:true},
			   		{name:'moleculeTypeName',index:'moleculeTypeName',width:125,sortable:true,search:false},
			   		{name:'status',index:'status',width:125,sortable:true,search:false},
			   	 <s:if test='typez=="report"'>
			   		{name:'image',index:'image',width:125,sortable:false,search:false}
			   		</s:if>
			   		//{name:'status',index:'status',width:125,sortable:true,search:false}
			   	 /* <s:if test="isKpfBased==1">
			   	 <s:if test="currentTenantId!='gsma''">
			   
			   		{name:'groupType',index:'groupType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="allanswer"/>;4:<s:text name="masterType4"/>;11:<s:text name="masterType11"/>' }}
			   		</s:if>	</s:if> */
			   			 ],
			   	height: 301, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			  
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			    <s:if test="typez=='report'">
				beforeSelectRow: function(rowid, e) {
		            var iCol = jQuery.jgrid.getCellIndex(e.target);
		            if (iCol >2){return false; }
		            else{ return true; }
		        },
			     onSelectRow: function(id){ 			    	 
				  document.updateform.id.value  =id;
			      document.updateform.submit();      
				},
				
			   </s:if>
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        }
			});
	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

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

});


function popDownload(id){
	document.getElementById("loadId").value=id;
	$('#audioFileDownload').submit(); 
	
}

function popupWindow(id) {
	try{
	
		
	
		var finalStatus=""
		var jsonFarmerData = "";
		//var str_array = val.split(',');
		$("#mDatabody").empty();
		//alert(str_array);
		var mDatabody="";
		
			$.ajax({
				 type: "POST",
		        async: false,
		        url: "moleculeProcess_populateMoleculeData.action",
		        data: {id : id},
		        success: function(result) {
		        	var jsonData = $.parseJSON(result);
		        	console.log(jsonData);
		        	$.each(jsonData, function(index, value) {
		        		var tr = $("<tr/>");		
		        		var td = $("<td/>");
			       	 	td.text(value.failedMolecule);
			       		var td1 = $("<td/>");
			       	 	td1.text(value.status);
			       		/* var td2 = $("<td/>");
			       		td2.text(value.loanPurpose);
			       		var td3 = $("<td/>");
			       		td3.text(value.status);
			       		var td4 = $("<td class='hide'/>");
			       		td4.text(value.farmerId);
			       		var td5 = $("<td class='hide'/>");
			       		td5.text(value.loanReq);
			       		var td6 = $("<td class='hide'/>");
			       		td6.text(value.villageId);
			       		var td7 = $("<td class='hide'/>");
			       		td7.text(value.samithiId);
			       		finalStatus=value.finalStatus; */
			       		
			       		

			       		tr.append(td);
			       		tr.append(td1);
			       		/* tr.append(td2);
			       		tr.append(td3);
			       		tr.append(td4);
			       		tr.append(td5);
			       		tr.append(td6);
			       		tr.append(td7); */
			       		
			       		
			       		
			       	 $("#mDatabody").append(tr);
			     	
		        	});
		        	
		        	
		        }
		       
			});

	
		document.getElementById("enableDataModal").click();	
	}
	catch(e){
		alert(e);
		}

}

function buttonDataCancel(){
	document.getElementById("model-close-data-btn").click();
}


function exportXLS(){
	
	 if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		
		jQuery("#detail").jqGrid('excelExport', {url: 'moleculeProcess_populateXLS.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}


</script>

	<%-- <div class="appContentWrapper marginBottom">
		<section class='reportWrap row'>
			<div class="gridly">
				<div class="filterEle iccoaHide">
					<label for="txt"><s:property
							value="%{getLocaleProperty('villageName')}" /></label>
					<div class="form-element">
						<s:select name="filter1.farm.farmer.villageName" id="villageName"
							list="{}" headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<div class="filterEle" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
			</div>
		</section>
	</div> --%>

	<div class="appContentWrapper marginBottom">
	<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn dLabel"
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
	
		<input type="BUTTON" id="add" class="btn btn-sts CompareButton"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
			<input type="BUTTON" id="sampleFile" class="btn btn-sts CompareButton"
			value="<s:text name="Sample File"/>"
			onclick="document.sampleFile.submit()" />
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>
	<s:form name="createform" action="moleculeProcess_create?type=service">
	</s:form>
	<s:form name="sampleFile" action="moleculeProcess_populateSampleFileDownload">
	</s:form>
	<s:form id="audioFileDownload"
		action="moleculeProcess_populateDownload">
		<s:hidden id="loadId" name="id" />
	</s:form>

	<s:form id="updateform" action="moleculeProcess_detail">
		<s:hidden id="id" name="id" />
	</s:form>

	<button type="button" id="enableDataModal"
		class="hide slide_open btn btn-sm btn-success" data-toggle="modal"
		data-target="#slideDataModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>

	<div id="slideDataModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-data-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">Farmer Details</h4>
				</div>

				<table class="table table-bordered table-responsive">
					<div class="modal-body">
						<thead>
							<tr>
								<!-- <td>Sno</td> -->
								<td>Failed Molecule</td>
								<td>Status</td>
								<!-- <td>Loan Purpose</td>
								<td>Status</td>
								<td id="td1">Farmer Id</td>
								<td id="td2">Loan Req</td>
								<td id="td3">Village</td>
								<td id="td4">Samithi</td> -->

							</tr>
						</thead>
						<tbody id="mDatabody">
						</tbody>
					</div>
				</table>

				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonDataCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>


</body>