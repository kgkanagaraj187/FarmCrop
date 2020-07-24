<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
</head>
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
<body>
	<script type="text/javascript">
	var recordLimit='<s:property value="exportLimit"/>';

jQuery(document).ready(function(){

	
	jQuery(".well").hide();
	getFilterData();
	if(filterDataReport==''){
	document.getElementById('farmerName').selectedIndex=0;
	document.getElementById('village').selectedIndex=0;
	
	<s:if test='branchId==null'>
	document.getElementById('branchIdParma').selectedIndex=0;
	document.getElementById('seasonId').selectedIndex=0;
	//document.getElementById('fatherName').selectedIndex=0;
	 
	 
	</s:if>
	 <s:if test="currentTenantId=='chetna'">
		document.getElementById('stateName').selectedIndex=0;
		document.getElementById('fpo').selectedIndex=0;
		document.getElementById('icsName').selectedIndex=0;
		</s:if>
	   $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "DD-MM-YYYY" );
	     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "DD-MM-YYYY" );
	     $("#daterange").data().daterangepicker.updateView();
	      $("#daterange").data().daterangepicker.updateCalendars(); 
	$('.applyBtn').click();
	}
	jQuery("#detail").jqGrid(
			{
				url:'cultivationReport_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			   	mtype: 'POST',
			    styleUI : 'Bootstrap',
			  	postData:{	
			   		"startDate" : function(){
	                     return document.getElementById("hiddenStartDate").value;           
	                    },		
	                    "endDate" : function(){
			               return document.getElementById("hiddenEndDate").value;
		                 },
				       "farmerName" : function(){			  
 					  return document.getElementById("farmerName").value;
 	 			      }, 
 	 			    "village" : function(){			  
 	 					  return document.getElementById("village").value;
 	 	 			      },
 	 	 			   /* "fatherName" : function(){			  
   	 					  return document.getElementById("fatherName").value;
   	 	 			      }, */
  	 	 			  "seasonCode" : function(){			  
  	 					  return document.getElementById("seasonId").value;
  	 	 			      },
  	 	 			  "postdata" :  function(){	
  	 		   			return  decodeURI(postdataReport);
  	 				  },
 	 	 			   <s:if test='branchId==null'>
 	 			    "branchIdParma" : function(){			  
	 					  return document.getElementById("branchIdParma").value;
	 	 			      } ,
 	 	 			      </s:if>
	 	 			    <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
	 					  "subBranchIdParma" : function(){
	 						  return document.getElementById("subBranchIdParam").value;
	 					  },
	 					  </s:if>
	 					 <s:if test="currentTenantId=='chetna'">
	 					  "stateName" : function(){
	 					  return  document.getElementById('stateName').value;
	 					  },
	 					  "fpo" : function(){
	 						  return  document.getElementById('fpo').value;
	 						  },
	 						  
	 					"icsName" : function(){
	 							  return  document.getElementById('icsName').value;
	 							  },
	 							 
	 					  </s:if>
	 					
			    }, 
			  	colNames:[
			  	          //'<s:text name="expenseDate"/>',
			  	           <s:if test='branchId==null'>
							'<s:text name="%{getLocaleProperty('app.branch')}"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						
							'<s:text name="app.subBranch"/>',
						</s:if>
							'<s:property value="%{getLocaleProperty('cSeasonCode')}" />',
					 	<s:if test="farmerCodeEnabled==1">
							<s:if test="branchId!='organic' ">
					'<s:property value="%{getLocaleProperty('farmerCode')}" />',
					 	</s:if>
					 	</s:if>
					 	 
			  		   	  '<s:property value="%{getLocaleProperty('farmerName')}" />',
			  		  // 	'<s:property value="%{getLocaleProperty('fatherName')}"/>',
			  		   	 '<s:text name="farm"/>',
			  		   	  '<s:text name="village"/>',
			  		   /*  <s:if test="currentTenantId!='pratibha'">
			  		   	'<s:property value="%{getLocaleProperty('taluk')}"/>',
			  			'<s:property value="%{getLocaleProperty('district')}"/>',
			  		      // '<s:text name="taluk"/>', 
			  		      //'<s:text name="district"/>',
			  		      '<s:text name="state"/>',
			  		      </s:if> */
			  		   
			  		  '<s:property value="%{getLocaleProperty('landPreparation')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',	
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('landLabourCost')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		    '<s:property value="%{getLocaleProperty('sowing')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('sowingLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		  '<s:property value="%{getLocaleProperty('gapFilling')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		'<s:property value="%{getLocaleProperty('gapLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		</s:if>
			  		   	  '<s:property value="%{getLocaleProperty('weed')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		    <s:if test="currentTenantId=='pratibha'">
			  		   	'<s:property value="%{getLocaleProperty('weedLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   	</s:if>
			  		   	'<s:property value="%{getLocaleProperty('interCultural')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('interCulturalLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		      '<s:property value="%{getLocaleProperty('irrigation')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		    <s:if test="currentTenantId=='pratibha'">
			  		    '<s:property value="%{getLocaleProperty('irrigationLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		    </s:if>
			  		    
			  		   	'<s:property value="%{getLocaleProperty('fertilizer')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('fertilizerLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  			'<s:property value="%{getLocaleProperty('pesticide')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  			 <s:if test="currentTenantId=='pratibha'">
			  			'<s:property value="%{getLocaleProperty('pesticideLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  			</s:if>
			  		   	'<s:text name="fym"/>',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('fymLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		   	  '<s:text name="harvesting"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   	<s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('harvestLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  '<s:property value="%{getLocaleProperty('transExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		'<s:property value="%{getLocaleProperty('fuelExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		'<s:property value="%{getLocaleProperty('otherExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		</s:if>
			  		   	  '<s:property value="%{getLocaleProperty('totalExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  	 /*  '<s:text name="agriIncome"/>',
			  		   	 '<s:text name="interCropIncome"/>',
			  		   	 '<s:text name="otherExpenseIncome"/>', */
			  		   
			  		   '<s:text name="%{getLocaleProperty('labourCost')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   
			  		    '<s:text name="%{getLocaleProperty('totalCost')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		 
			             ],
			    datatype: "json",
			   	colModel:[
                   // {name:'expenseDate',index:'expenseDate',width:40,sortable:false},
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
				   			        }]
				   			
				   			}},	   				   		
				   		</s:if>
				   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   		
				   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>
					{name:'cSeasonCode',index:'cSeasonCode',sortable:false},
					 <s:if test="farmerCodeEnabled==1">
					<s:if test="branchId!='organic'">
					{name:'farmerId',index:'farmerId',sortable:false},
					</s:if>
					</s:if>
					
					{name:'farmerName',index:'farmerName',sortable:false},
					//{name:'fatherName',index:'fatherName',width:120,sortable:false},
					
					{name:'farmName',index:'farmName',sortable:false},
     				 {name:'village',index:'village',sortable:false},
     				   /* <s:if test="currentTenantId!='pratibha'">
				    {name:'taluk',index:'taluk',width:150,sortable:false},
					{name:'district',index:'district',align:'right',width:150,sortable:false},
				    {name:'state',index:'state',width:150,sortable:false},
				    </s:if> */
				   
					{name:'landPreparation',index:'landPreparation',sortable:false,align:'right'},
					 <s:if test="currentTenantId=='pratibha'">
					{name:'landLabour',index:'landLabour',sortable:false,align:'right'},
					</s:if>
				    {name:'sowing',index:'sowing',sortable:false,align:'right'},
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'sowingLabour',index:'sowingLabour',sortable:false,align:'right'},
				    </s:if>
				    {name:'gapFilling',index:'gapFilling',sortable:false,align:'right'},
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'gapLabour',index:'gapLabour',sortable:false,align:'right'},
				    </s:if>
					{name:'weeding',index:'weeding',align:'right',sortable:false,align:'right'},
					 <s:if test="currentTenantId=='pratibha'">
					{name:'weedLabour',index:'weedLabour',align:'right',sortable:false,align:'right'},
					</s:if>
				    {name:'interCultural',index:'interCultural',sortable:false,align:'right'},
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'interCulturalLabour',index:'interCulturalLabour',sortable:false,align:'right'},
				    </s:if>
					{name:'irrigation',index:'irrigation',sortable:false,align:'right'},
					 <s:if test="currentTenantId=='pratibha'">
					{name:'irrigationLabour',index:'irrigationLabour',sortable:false,align:'right'},
					</s:if>
				    {name:'fertilizer',index:'fertilizer',sortable:false,align:'right'},
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'fertilizerLabour',index:'fertilizerLabour',sortable:false,align:'right'},
				    </s:if>
				    {name:'pesticide',index:'pesticide',sortable:false,align:'right'},
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'pesticideLabour',index:'pesticideLabour',sortable:false,align:'right'},
				    </s:if>
				    {name:'fym',index:'fym',sortable:false,align:'right'},
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'fymLabour',index:'fymLabour',sortable:false,align:'right'},
				    </s:if>
					{name:'harvesting',index:'harvesting',align:'right',sortable:false,align:'right'},
					 <s:if test="currentTenantId=='pratibha'">
					{name:'harvestLabour',index:'harvestLabour',align:'right',sortable:false,align:'right'},
					{name:'transExpenses',index:'transExpenses',align:'right',sortable:false,align:'right'},
					{name:'fuelExpenses',index:'fuelExpenses',align:'right',sortable:false,align:'right'},
					{name:'otherExpeness',index:'otherExpeness',align:'right',sortable:false,align:'right'},
					</s:if>
					{name:'totOtherExpenses',index:'totOtherExpenses',align:'right',sortable:false,align:'right'},
					/* {name:'agriIncome',index:'agriIncome',align:'right',width:40,sortable:false},
					{name:'interCropIncome',index:'interCropIncome',align:'right',width:40,sortable:false},
					{name:'otherExpenseIncome',index:'otherExpenseIncome',align:'right',width:40,sortable:false}, */
					
					{name:'labour',index:'labour',align:'right',sortable:false},
					
					{name:'total',index:'total',align:'right',sortable:false},
					 
			         	],
			   height: 300, 
			   width: $("#baseDiv").width(),
			  // autowidth:true,
			  // shrinkToFit:true,		
			   scrollOffset: 19,     
			   sortname:'id',	
			   sortorder: "asc",
			   rowNum:10,
			   rowList : [10,25,50],
			   viewrecords: true,  
			   /* caption: "Filter",  */

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
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
				jQuery("#detail").jqGrid('setGroupHeaders', {
			  useColSpanStyle: true, 
			 /*  groupHeaders:[
							{startColumnName: 'landPreparation', numberOfColumns: 2, titleText: 'Land Preparation'},
							{startColumnName: 'sowing', numberOfColumns: 2, titleText: 'Sowing'},
							{startColumnName: 'gapFilling', numberOfColumns: 2, titleText: 'Gap Filling & Thinning'},
							{startColumnName: 'weeding', numberOfColumns: 2, titleText: 'Weeding'},
							{startColumnName: 'interCultural', numberOfColumns: 2, titleText: 'Inter Cultural'},
							{startColumnName: 'irrigation', numberOfColumns: 2, titleText: 'Irrigation'},
							{startColumnName: 'fertilizer', numberOfColumns: 2, titleText: 'Fertilizer'},
							{startColumnName: 'pesticide', numberOfColumns: 2, titleText: 'Pesticide'},
							{startColumnName: 'fym', numberOfColumns: 2, titleText: 'Farm Yard Manure (FYM)'},
							{startColumnName: 'harvesting', numberOfColumns: 2, titleText: 'Harvesting'},
							{startColumnName: 'transExpenses', numberOfColumns: 4, titleText: 'Other Expenses'},
							 
			  ] */
 			});
	jQuery("#generate").click( function() {
		reloadGrid();	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	
	
	jQuery("#detail").jqGrid('setFrozenColumns');

/* 	
	var d1=	$('#daterange').val();
		alert(d1); */

		/* function resetOption(name,value) {
			$("select[name='"+name+"']").val(value).trigger("change");
		
		} */
	
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
			
	jQuery("#detail").jqGrid('setGridParam',{url:"cultivationReport_data.action?",page:1}).trigger('reloadGrid');
	}
	}

	function clear(){	
		resetReportFilter();
		document.form.submit();
	}
	
	/* $(".ui-jqgrid-titlebar").append($("#filterzz"));
	
	$(".ui-jqgrid-titlebar").css("background-color", "#F3F3F3");
	
	$(".ui-jqgrid-title").hide(); */
	postdataReport='';
});

function exportXLS(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		alert('<s:text name="export.limited"/>');
	}else if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'cultivationReport_populateXLS.action'});
	}
	else{
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
		jQuery("#detail").jqGrid('excelExport', {url: 'cultivationReport_populatePDF.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}

function populatefarmer(obj){
	if(filterDataReport==''){
	var tenant='<s:property value="currentTenantId"/>';
	if($(obj).val()!=''){
		$('#farmerId').empty();
		$('#farmerName').empty();
		$('#village').empty();
	$.getJSON('cultivationReport_populateFarmerData.action?seasonCode='+$(obj).val(),function(data){
		var village = data.village;
		var farmerNameList = data.farmerNameList;
		var farmerFarmerIdList = data.farmerFarmerIdList;
		 if(village!=null&&village!=''){
			 $("#village").append('<option value=""><s:text name="txt.select"/></option>');
			 $.each(village, function(k, v) {
				 $("#village").append("<option value='"+k+"'>"+v+"</option>");
		    });
         }
		 if(farmerNameList!=null&&farmerNameList!=''){
			 $("#farmerName").append('<option value=""><s:text name="txt.select"/></option>');
			 $.each(farmerNameList, function(k, v) {
				 $("#farmerName").append("<option value='"+k+"'>"+v+"</option>");
		    });
         }
		 if(farmerFarmerIdList!=null&&farmerFarmerIdList!=''){
			 $.each(farmerFarmerIdList, function(k, v) {
				 $("#farmerId").append("<option value='"+k+"'>"+v+"</option>");
		    });
         }
         
		 
	});
}
	}
}

</script>
	<s:form name="form" id="filter" action="cultivationReport_list">
		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
				<div class="gridly">
					<div class="filterEle hide">
						<label for="txt"><s:text name="startingDate" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('cSeasonCode')}" /></label>
						<div class="form-element">
							<s:select name="seasonCode" id="seasonId" list="seasonList" onchange="populatefarmer(this)"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control  select2" />
						</div>
					</div>
					
					
					
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer')}" /></label>
						<div class="form-element">
							<s:select name="farmerName" id="farmerName" list="farmerNameList"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control  select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="village" /></label>
						<div class="form-element">
							<s:select name="village" id="village" list="villageMap"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control  select2" />
						</div>
					</div>


					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParam" id="branchIdParma"
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
									cssClass="form-control select2" />
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
										cssClass="form-control select2" />
								</div>
							</div>
						</s:if>
					</s:else>
					
						<div class="filterEle hide">
							<label for="txt"><s:text name="crop" /></label>

							<s:select name="cropCode" id="cropId" list="cropNameList"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" />
						</div>
				
					<s:if test="currentTenantId=='chetna'">
						<div class="filterEle">
							<label for="stateName"><s:property
									value="%{getLocaleProperty('stateName')}" /></label>
							<s:select name="stateName" id="stateName" list="stateList"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" />
						</div>
						<div class="filterEle">
							<label for="cooperative"><s:property
									value="%{getLocaleProperty('fpoGroup')}" /></label>
							<s:select name="fpo" id="fpo" list="warehouseList" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" />
						</div>
						<div class="filterEle">
						   <label for="icsName"><s:text name="icsName" /></label>
						   <s:select name="icsName" id="icsName" list="icsNameList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control select2"/>
						</div>

					</s:if>

					<%-- <div class="reportFilterItem">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('fatherName')}" /></label>
						<div class="form-element">
							<s:select name="fatherName" id="fatherName" list="fatherNameList"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
						</div>
					</div> --%>

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


		<div class="appContentWrapper marginBottom">
			<div class="flex-layout reportData">
				<div class="flexItem-2">
					<div class="summaryBlocksWrapper flex-container hide">
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="farmerCount"></span>
								Farmers&nbsp;<i class="fa fa-user"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="tArea"></span> Total
								Area&nbsp;<i class="fa fa-dollar"></i></span>
						</div>

						<div class="report-summaryBlockItem">
							<span><span class="strong" id="totalCoc"></span> Total
								Cost&nbsp;<i class="fa fa-dollar"></i></span>
						</div>
					</div>
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
							<s:if test="currentTenantId!='movcd'">
								<%-- <li><a href="#" onclick="exportPDF();" role="tab"
								data-toggle="tab" aria-controls="dropdown1"
								aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
										name="pdf" /></a></li> --%>
							</s:if>
							<li><a href="#" onclick="exportXLS()" role="tab"
								data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
									class="fa fa-table"></i> <s:text name="excel" /></a></li>
						</ul>
					</div>
				</div>
			</div>

			<div class="jqGridwrapper baseDiv">
				<table id="grid"></table>
				<div id="pager"></div>
			</div>

			<div style="width: 99%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>

		</div>
	</s:form>
	<div class="clear"></div>
	<s:form name="detailForm" action="cultivationReport_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
		<s:hidden name="filterMapReport" id="filterMapReport" />
		<s:hidden name="postdataReport" id="postdataReport" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>