<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<style>
i {
	color: blue;
}
</style>
</head>
<body>
	<script type="text/javascript">
var sDate='';
var eDate='';
var recordLimit='<s:property value="exportLimit"/>';
jQuery(document).ready(function(){
	
	var tenant='<s:property value="getCurrentTenantId()"/>';
	
	loadGrid();
	
	jQuery(".well").hide();
  /*  document.getElementById('farmerCode').selectedIndex=0; */
	
	  <s:if test='branchId==null'>
	document.getElementById('branchIdParam').selectedIndex=0;
	   </s:if>
	  
	jQuery("#generate").click( function() {
		reloadGrid("");	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	

	function clear(){
		resetReportFilter();
		document.form.submit();
		
	}
	
});


function loadGrid(){
	 jQuery("#detail").jqGrid({
		   	url:'farmerFeedBackReport_data.action',
		   	pager: '#pagerForDetail',
		   	datatype: "json",
		   	mtype: 'POST',
			styleUI : 'Bootstrap',
			datatype: "json",
			postData:{	
				 "filter.farmerId" : function(){			  
					return document.getElementById("firstName").value;
			     },
			     "filter.village.code" : function(){			  
						return document.getElementById("villageName").value;
				     },
				     "filter.warehouse.code" : function(){			  
							return document.getElementById("fpo").value;
					     }
				  
			},
			
			colNames:[
					  
					  			 '<s:property value="%{getLocaleProperty('answerDate')}" />',
					  			 '<s:property value="%{getLocaleProperty('village')}"/>',
					  			'<s:property value="%{getLocaleProperty('group')}" />',
					  			'<s:property value="%{getLocaleProperty('farmerHindi')}" />',
					  			'<s:property value="%{getLocaleProperty('ques1')}" />',
					  			'<s:property value="%{getLocaleProperty('ques2')}" />',
					  			'<s:property value="%{getLocaleProperty('ques3')}" />',
					  			'<s:property value="%{getLocaleProperty('ques4')}" />'
					  		     
			],
			colModel:[
			         
	
	{name:'answeredDate',index:'answeredDate',width:200,sortable:false}, 
	{name:'v.id',index:'v.id',width:200,sortable:false},
	{name:'w.id',index:'w.id',width:200,sortable:false},
	{name:'farmerName',index:'farmerName',width:200,sortable:false},
	{name:'question1',index:'question1',width:200,sortable:false},
	{name:'question2',index:'question2',width:200,sortable:false},
	{name:'question3',index:'question3',width:200,sortable:false},
	{name:'question4',index:'question4',width:200,sortable:false}
     	
	
			],
			  height: 380, 
			   width: $("#baseDiv").width(),
			   autowidth:true,
			   shrinkToFit:false,			   
			   scrollOffset: 0,     
			   sortname:'id',	
			   sortorder: "asc",
			   rowNum:10,
			   rowList : [10,25,50],
			   viewrecords: true,  

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
			
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
}

function exportXLS(){
	
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	/* if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else */ if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerFeedBackReport_populateXLS.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}

function exportPDF(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else if(isDataAvailable("#detail")){	
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerFeedBackReport_populatePDF.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}

function reloadGrid(flag){

	jQuery("#detail").jqGrid('setGridParam',{url:"farmerFeedBackReport_data.action?",page:1,mtype: 'POST',
		postData:{	
			 "filter.farmerId" : function(){			  
				return document.getElementById("firstName").value;
		     },
		     "filter.village.code" : function(){			  
					return document.getElementById("villageName").value;
			     },
			     "filter.warehouse.code" : function(){			  
						return document.getElementById("fpo").value;
				     }
			 
		}}).trigger('reloadGrid');
	
	}
</script>

	<s:form name="form" action="farmerFeedBackReport_list">
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">

				<div class="reportFilterItem">
					<label for="txt"><s:text name="farmerName" /></label>
					<div class="form-element">
						 <s:select name="filter.farmerId" id="firstName" list="farmerNameList"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="txt"><s:text name="villageName" /></label>
					<div class="form-element">
						<s:select name="filter.village.code" id="villageName" list="villageList"
							headerKey="" headerValue="%{getText('txt.select')}" listKey="code" listValue="name"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				
					<div class="reportFilterItem">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('group')}" /></label>
						<div class="form-element">
							<s:select name="filter.warehouse.code" id="fpo" list="warehouseList" headerKey=""  listKey="code" listValue="name"
								headerValue="%{getText('txt.select')}"
								cssClass="input-sm form-control select2" />
						</div>
					</div>
					<div class="reportFilterItem" style="margin-top: 24px;">
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
			
			
		
		</div>
	</s:form>
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">

			<div class="yui-skin-sam">
				<div class="btn-group" style="float: right;">
					<a href="#" data-toggle="dropdown"
						class="btn btn-sts dropdown-toggle"> <s:text name="export" />
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu">
						<li role="presentation"><a href="#" onclick="exportXLS()"
							tabindex="-1" role="menuitem"> <s:text name="excel" />
						</a></li>
						
						<%-- <li role="presentation"><a href="#" onclick="exportPDF();"
							tabindex="-1" role="menuitem"> <s:text name="pdf" />
						</a></li> --%>
					</ul>
				</div>
			</div>
		</div>
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div>

	
</body>