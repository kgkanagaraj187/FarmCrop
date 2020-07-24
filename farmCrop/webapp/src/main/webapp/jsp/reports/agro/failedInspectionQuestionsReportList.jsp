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
	jQuery(document).ready(function(){
		jQuery(".well").hide();
		
		
		//document.getElementById('fieldl').selectedIndex=0;
		//  document.getElementById('categoryId').selectedIndex=0;
		
		
		   //$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     //$("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     //$("#daterange").data().daterangepicker.updateView();
		      //$("#daterange").data().daterangepicker.updateCalendars();
		//$('.applyBtn').click();
		
		jQuery("#detail").jqGrid(
				{
				   	url:'failedInspectionQuestionsReport_data.action?',
				   	pager: '#pagerForDetail',
				    styleUI : 'Bootstrap',
				    mtype: 'POST',
				   	datatype: "json",
				   	postData:{				 
				   		/* "startDate" : function(){
		                     return document.getElementById("hiddenStartDate").value;           
		                    },		
		                    "endDate" : function(){
				               return document.getElementById("hiddenEndDate").value;
			                 }, */
			                 <s:if test='branchId==null'>
		 	 	 			   "branchIdParma" : function(){
		 						  return document.getElementById("branchIdParma").value;
		 					  }
		 	 	 			   </s:if>
						 
						},	
				   	colNames:[		
					            <s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
					         	</s:if>
						        <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
					         	</s:if>
						   	    '<s:text name="serialNo"/>',
						  		'<s:text name="questionName"/>',
						  		'<s:text name="failedCount"/>'			  		   	  
			 	      		 ],
			 	     
				   	colModel:[
								<s:if test='branchId==null'>
						   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
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
						   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
						   		</s:if>
								{name:'serialNo',index:'serialNo',sortable:false,align:'center'},				
								{name:'questionName',index:'questionName',sortable:false,width:400},
								{name:'failedCount',index:'failedCount',sortable:false,align:'center'}			   		
				   			 ],
				   			
				   	height: 380,
			 		width: $("#baseDiv").width(),
			 		scrollOffset: 19,
			 		rowNum:10,
			 		rowList : [10,25,50],
			 		viewrecords: true,
			 		sortname:'failedCount',
			 		shrinkToFit:true,
			 		sortorder: "desc",					   
				   });

			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
				
			jQuery("#generate").click( function() {
			reloadGrid();	
				});
			
/* 		jQuery("#clear").click(function() {
			clear();
		});	 */

		jQuery("#clear").click( function() {
			window.location.href="failedInspectionQuestionsReport_list.action";
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
		//	alert(startDate);
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
		//	alert(endDate);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
				
		jQuery("#detail").jqGrid('setGridParam',{url:"failedInspectionQuestionsReport_data.action?",page:1}).trigger('reloadGrid');
		}
		}
		
		
		function clear(){
			document.form.submit();
		}
		
		function reloadGrid(){
			jQuery("#detail").jqGrid('setGridParam',{url:"failedInspectionQuestionsReport_data.action?",page:1})
				.trigger('reloadGrid');	
		}

	
		
			
	});
	
	function exportXLS() {
	    if (isDataAvailable("#detail")) {
	        jQuery("#detail").setGridParam({postData: {rows: 0}});
	        jQuery("#detail").jqGrid('excelExport', {url: 'failedInspectionQuestionsReport_populateXLS.action?'});
	}     else {
	        alert('<s:text name="export.nodata"/>');
	    }
	}

	
</script>

       <s:form name="form" action="failedInspectionQuestionsReport_list">
	<div class="appContentWrapper marginBottom">
				<section class='reportWrap row'>
				<div class="gridly">
				<%-- <div class="filterEle">
					<label for="txt"><s:text name="answeredDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control input-sm" />
					</div>
				</div> --%>
				<s:if test='branchId==null'>
				<div class="filterEle">
					<label for="txt"><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParma" id="branchIdParma"
						         list="branchesMap" headerKey="" headerValue="%{getText('txt.select')}"
						          cssClass="form-control input-sm" />
					</div>
				</div>
				
				<div class="filterEle" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm" aria-hidden="true"
						 id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
				</s:if>
				
							
				
			</div>
				</div>
      </s:form>
<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<!-- <div class="flexItem-2">
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
			</div> -->

			<%-- <div class="flexItem text-right flex-right">
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
						<li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li>
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div> --%>
		</div>
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

	<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
		
	</div>
<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>