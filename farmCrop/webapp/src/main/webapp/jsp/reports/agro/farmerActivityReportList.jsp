<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>

<meta name="decorator" content="swithlayout">
</head>
<body>

	<script>
	var sDate='';
	var eDate='';
$(document).ready(function(){
	getFilterData();
	<s:if test='branchId==null'>
    document.getElementById('branchIdParam').selectedIndex=0;
    </s:if>
    document.getElementById('farmerId').selectedIndex=0;
    
    $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.updateView();
    $("#daterange").data().daterangepicker.updateCalendars();
	$('.applyBtn').click(); 
    
	loadGrid();
	
		
	jQuery("#generate").click( function() {
		reloadGrid();
		//jQuery("#detail").jqGrid('setGridParam',{url:"farmerActivityReport_data.action?",page:1}).trigger('reloadGrid');
	});
	
	jQuery("#clear").click( function() {
		clear();
	});
	
	 function reloadGrid(){
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			var value1= d2[0];
			var value2= d2[1];
			document.getElementById("hiddenStartDate").value=value1;
			document.getElementById("hiddenEndDate").value=value2;
			var startDate=new Date(document.getElementById("hiddenStartDate").value);
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
	   jQuery("#detail").jqGrid('setGridParam',{url:"farmerActivityReport_data.action",page:1}).trigger('reloadGrid');  
	  } 
	 }
	 
	
});

function loadGrid(){
	
	var colNames='<s:property value="mainGridCols"/>';
	var gridColumnNames = new Array();
	var gridColumnModels = new Array();
	
	$(colNames.split("|")).each(function(k,val){
			if(!isEmpty(val)){
				gridColumnNames.push(val);
				gridColumnModels.push({name: val,sortable: false});
			}
	});
	
	

	jQuery("#detail").jqGrid(
	{
	   	url:'farmerActivityReport_data.action',
	 	pager: '#pagerForDetail',
	   	datatype: "json",	
	    styleUI : 'Bootstrap',
	   	mtype: 'POST',		
	   	postData:{
	   	 	<s:if test='branchId==null'>
			  	"branchIdParma" : function(){
			  	return document.getElementById("branchIdParam").value;
		 	 }, 
		 	</s:if>
		  	<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
		  		"subBranchIdParma" : function(){
				return document.getElementById("subBranchIdParam").value;
		  	},
		  	</s:if>
		  		"startDate" : function(){
            	return document.getElementById("hiddenStartDate").value;           
            },		
       		"endDate" : function(){
        	 	return document.getElementById("hiddenEndDate").value;
        	}, 
	   		"farmerId" : function(){		  
				return document.getElementById("farmerId").value;
		    },
		    "postdata" :  function(){	
	 			return  decodeURI(postdataReport);
	 		},
		    
	   	}, 
	 	colNames:gridColumnNames,
	   	colModel:gridColumnModels,
		   height: 301, 
	   width: $("#baseDiv").width(),
	   scrollOffset: 19,
	   rowNum:10,
	   shrinkToFit:true,
	   rowList : [10,25,50],
	  // autowidth:true,
	   viewrecords: true,  

	   beforeSelectRow: function(rowid, e) {
            var iCol = jQuery.jgrid.getCellIndex(e.target);
            if (iCol >11){return false; }
            else{ return true; }
        },
        loadComplete: function(){//in subgrid 
        	 <s:if test="type == 'agent'">	
        	jQuery("#detail").jqGrid('hideCol', "subgrid");
        	</s:if>
    },
        

	   sortname:'id',
	   sortorder: "desc",
	   subGrid: false,
	   subGridOptions: {
	   "plusicon" : "glyphicon-plus-sign",
	   "minusicon" : "glyphicon-minus-sign",
	   "openicon" : "glyphicon-hand-right",
	   },
	    
	
	onSelectRow: function(id){ 
		  document.detailForm.id.value  =id;
		  getSelectData();
		  var postdataReport =  JSON.stringify($('#detail').getGridParam('postData'));
		  document.detailForm.postdataReport.value =postdataReport;
        document.detailForm.submit();
		},
		
    onSortCol: function (index, idxcol, sortorder) {
        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                && this.p.colModel[this.p.lastsort].sortable !== false) {
            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
        }
    }
	});	
	

	$('#detail').jqGrid('setGridHeight',(reportWindowHeight));	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false});
	
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
    jQuery("#detail").setGridWidth($("#baseDiv").width());
	}
	
function clear(){
	resetReportFilter();
	document.form.submit();
	
}

function exportXLS(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerActivityReport_populateXLS.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}	

</script>

	<s:form name="form" id="filter" action="farmerActivityReport_list">
		<div class="appContentWrapper marginBottom">
				<section class='reportWrap row'>
				<div class="gridly">
			
				
				
					<s:if
					test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
						<div class="filterEle">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element ">
								<s:select name="branchIdParam" id="branchIdParam"
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
								<s:select name="branchIdParam" id="branchIdParam"
									list="branchesMap" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
					</s:if>
				</s:else>
				
				
				<div class="filterEle">
						<label for="txt"><s:text name="StartingDate" /></label>
						<div class="form-element">
					<input id="daterange" name="daterange" id="daterangepicker"
						class="form-control input-sm " />
						</div>
				</div>
				
					<div class="filterEle">
						<label for="daterange"><s:property value="%{getLocaleProperty('farmer')}" /></label>
						<div class="form-element">
							<s:select name="farmerId" id="farmerId" list="farmerList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control select2" />
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
		</div>

	</s:form>

	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">


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
		</div>
		<!-- <div id="temp"></div> -->
		<div style="width: 98%;" id="baseDiv">
			<table id="detail"></table>

			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	<div style="width: 99%;" id="baseDiv">
		<table id="detail"></table>
		<div id="pagerForDetail"></div>
	</div>

	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
	<s:form name="detailForm" action="farmerActivityReport_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
		<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
		<s:hidden name="filterMapReport" id="filterMapReport" />
		<s:hidden name="postdataReport" id="postdataReport" />
	</s:form>
	
</body>