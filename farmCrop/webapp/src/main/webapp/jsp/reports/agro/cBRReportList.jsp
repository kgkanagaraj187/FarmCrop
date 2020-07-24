<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">
var sDate='';
var eDate='';
var recordLimit='<s:property value="exportLimit"/>';
jQuery(document).ready(function(){
	jQuery(".well").hide();
	
   /*  document.getElementById('farmerCode').selectedIndex=0; */
	document.getElementById('firstName').selectedIndex=0;
	document.getElementById('villageName').selectedIndex=0;
	<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
	document.getElementById('icsName').selectedIndex=0;
	</s:if>
	document.getElementById('samithiName').selectedIndex=0;
	document.getElementById('farmName').selectedIndex=0;
	//document.getElementById('fatherName').selectedIndex=0;
	document.getElementById('seasonId').selectedIndex=0;
	 <s:if test="currentTenantId=='chetna'">
		document.getElementById('stateName').selectedIndex=0;
		document.getElementById('fpo').selectedIndex=0;
		document.getElementById('icsName').selectedIndex=0;
		</s:if>
	<s:if test='branchId==null'>
    document.getElementById('branchIdParam').selectedIndex=0;
    </s:if>
 
	 //sDate = document.getElementById("startDate").value;
	 //eDate = document.getElementById("endDate").value;
	jQuery("#detail").jqGrid(
			{ 
				url:'cBRReport_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			   	mtype: 'POST',
			    styleUI : 'Bootstrap',
			   	postData:{	
				//"startDate" : function(){			  
				//return sDate;
			 // },
			  //"endDate" : function(){			  
				//return eDate;
			  //},
				 /* "farmerCode" : function(){			  
				      return document.getElementById("farmerCode").value;
 			          }, */
 			      "firstName" : function(){			  
 					  return document.getElementById("firstName").value;
 	 			      }, 
 	 			    "villageName" : function(){			  
 	 					  return document.getElementById("villageName").value;
 	 	 			  },
 	 	 			 <s:if test='branchId==null'>
 	 	 			  "branchIdParam" : function(){
	 					  return document.getElementById("branchIdParam").value;
	 				  }, 
	 				 </s:if>
	 				 <s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
 	 	 			"icsName" : function(){			  
	 					  return document.getElementById("icsName").value;
	 	 			  },
	 	 			  </s:if>
	 	 			"samithiName" : function(){			  
	 					  return document.getElementById("samithiName").value;
	 	 			  },
	 	 			/* "fatherName" : function(){			  
 	 					  return document.getElementById("fatherName").value;
 	 	 			      },*/
 	 	 			   "seasonCode" : function(){			  
   	 					  return document.getElementById("seasonId").value;
   	 	 			      },
	 	 			"farmName" : function(){			  
	 					  return document.getElementById("farmName").value;
	 	 			  },
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
			  	        <s:if test='branchId==null'>
'<s:text name="%{getLocaleProperty('app.branch')}"/>',
</s:if>
<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>

'<s:text name="app.subBranch"/>',
</s:if>
'<s:property value="%{getLocaleProperty('cSeasonCode')}" />',


					 	
			  		   	  '<s:property value="%{getLocaleProperty('farmer')}" />',
			  		   	// '<s:property value="%{getLocaleProperty('fatherName')}"/>',
			  		   	 '<s:text name="farm"/>',
			  		   <s:if test="currentTenantId=='pratibha'">
							<s:if test="getBranchId()!='bci'">
			  		     '<s:text name="icsName"/>',
			  		     </s:if>
			  		     </s:if>
			  		   '<s:property value="%{getLocaleProperty('profile.samithi')}" />',
			  		   	 //'<s:text name="yearOfJoining"/>',
			  		   	  '<s:text name="village"/>',
			  		   /* <s:if test="currentTenantId!='pratibha'">
			  		      '<s:text name="taluk"/>',
			  		      '<s:text name="district"/>',
			  		      
			  		    '<s:property value="%{getLocaleProperty('profile.location.state')}" />',
			  		      </s:if>*/
			  		    <s:if test="currentTenantId!='pratibha'">
			  		    '<s:property value="%{getLocaleProperty('proposedPlantingArea')}" /> (<s:property value="%{getAreaType()}" />)',
			  		    </s:if>
			  		  <s:if test="currentTenantId!='pratibha'">
			  		   	 '<s:property value="%{getLocaleProperty('cottonAreaIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		
			  		    '<s:text name="%{getLocaleProperty('interCropIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		  <s:if test="currentTenantId=='pratibha'">
			  		'<s:property value="%{getLocaleProperty('primaryIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                    '<s:property value="%{getLocaleProperty('interCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                    '<s:property value="%{getLocaleProperty('borderCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                    '<s:property value="%{getLocaleProperty('coverCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                    '<s:property value="%{getLocaleProperty('plantonBundCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                    '<s:property value="%{getLocaleProperty('trapCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                    '<s:property value="%{getLocaleProperty('otherIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		</s:if>
			  		   	'<s:text name="%{getLocaleProperty('grossIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   	'<s:text name="%{getLocaleProperty('cultivationExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />) ',
			  		    '<s:text name="%{getLocaleProperty('totalProfit')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		 /*  <s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
				  		    '<s:text name="%{getLocaleProperty('profitPerAcre')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'
				  			</s:if>		 */	  		   	 
			  		   	       ],
			    datatype: "json",
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
				   			        }]
				   			
				   			}},	   				   		
				   		</s:if>
				   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   		
				   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>  
					{name:'cSeasonCode',index:'cSeasonCode',sortable:false,frozen:false},
					
					
				
					{name:'farmerName',index:'farmerName',sortable:false,frozen:false},
					//{name:'fatherName',index:'fatherName',width:150,sortable:false},
					{name:'farmName',index:'farmName',sortable:false},
					<s:if test="currentTenantId=='pratibha'">
					<s:if test="getBranchId()!='bci'">
					{name:'icsName',index:'icsName',sortable:false},
					</s:if>
					</s:if>
					{name:'samithiName',index:'samithiName',sortable:false},
					//{name:'yearOfJoining',index:'yearOfJoining',width:150,sortable:false},
					{name:'village',index:'village',sortable:false},
					/*<s:if test="currentTenantId!='pratibha'">
				    {name:'taluk',index:'taluk',width:150,sortable:false},
					{name:'district',index:'district',align:'right',width:150,sortable:false},
				    {name:'state',index:'state',width:150,sortable:false},
				    </s:if>*/
				    <s:if test='enableMultiProduct==1'>
				   	 /* {name:'crop',index:'crop',width:200,sortable:false}, */
				   	 </s:if>
				   	 <s:if test="currentTenantId!='pratibha'">
				    {name:'proposedPlantingArea',index:'proposedPlantingArea',sortable:false,align:'right'},
				    </s:if>
				    <s:if test="currentTenantId!='pratibha'">
					{name:'cottonAreaIncome',index:'cottonAreaIncome',sortable:false,align:'right'},
				    {name:'interCropIncome',index:'interCropIncome',sortable:false,align:'right'},
				    </s:if>
				    <s:if test="currentTenantId=='pratibha'">
				    {name:'agriIncome',index:'agriIncome',align:'right',sortable:false},
					 {name:'interCropIncome',index:'interCropIncome',align:'right',sortable:false},
					{name:'borderCropIncome',index:'interCropIncome',align:'right',sortable:false},
					{name:'coverCropIncome',index:'interCropIncome',align:'right',sortable:false},
					{name:'plantonBundCropIncome',index:'interCropIncome',align:'right',sortable:false},
					{name:'trapCropIncome',index:'interCropIncome',align:'right',sortable:false},
					{name:'otherIncome',index:'otherIncome',align:'right',sortable:false},
				    </s:if>
				    {name:'grossIncome',index:'grossIncome',sortable:false,align:'right'},
					{name:'cultivationExpenses',index:'cultivationExpenses',align:'right',sortable:false},
				    {name:'totalProfit',index:'totalProfit',sortable:false,align:'right'},
				   
				   /*  <s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
					{name:'profitPerAcre',index:'profitPerAcre',sortable:false,align:'right'}
		  			</s:if> */
				    
			        	],
			   height: 280, 
			   width: $("#baseDiv").width(),
			   //autowidth:true,
			   shrinkToFit:true,	
			   scrollOffset: 19,     
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
			
		  $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
	 jQuery("#detail").jqGrid('setFrozenColumns');
	jQuery("#generate").click( function() {
		reloadGrid();	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	

	function reloadGrid(){
	jQuery("#detail").jqGrid('setGridParam',{url:"cBRReport_data.action?",page:1}).trigger('reloadGrid');

	/* var startDate=new Date(document.getElementById("startDate").value);
	var endDate=new Date(document.getElementById("endDate").value);
	if (startDate>endDate){
		alert('<s:text name="date.range"/>');
	}else{
		sDate = document.getElementById("startDate").value;
		eDate = document.getElementById("endDate").value;	
			
	} */
	}
	function clear(){	
		resetReportFilter();
		document.form.submit();
	}
	
});

function exportXLS(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'cBRReport_populateXLS.action'});
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
		jQuery("#detail").jqGrid('excelExport', {url: 'cBRReport_populatePDF.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}
</script>


	<s:form name="form" action="cBRReport_list">

		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
			<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('cSeasonCode')}" /></label>
					<div class="form-element">
						<s:select name="filter.seasonCode" id="seasonId" list="seasonList"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('farmerName')}" /></label>
					<div class="form-element">
						<s:select name="firstName" id="firstName" list="farmerNameList"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="villageName" /></label>
					<div class="form-element">
						<s:select name="villageName" id="villageName" list="villageMap"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
				<%-- <div class="reportFilterItem">
					<label for="txt"><s:property
					value="%{getLocaleProperty('fatherName')}" /></label>
					<div class="form-element">
						<s:select name="fatherName" id="fatherName" list="fatherNameList"
				headerKey="" headerValue="%{getText('txt.select')}"
				class="form-control input-sm select2"  />
					</div>
				</div>--%>
				<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
					<div class="filterEle">
						<label for="txt"><s:text name="icsName" /></label>
						<div class="form-element">
							<s:select name="icsName" id="icsName"
								class="form-control input-sm select2" list="icsList"
								headerKey="" headerValue="%{getText('txt.select')}"
								listKey="key" listValue="value"></s:select>
						</div>
					</div>
				</s:if>
				<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('samithiName')}" /></label>
					
					<div class="form-element">
						<s:select name="samithiName" id="samithiName"
							list="samithiNameList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>

				
				<div class="filterEle">
					<label for="txt"><s:text name="farmName" /></label>
					<div class="form-element">
						<s:select name="farmName" id="farmName" list="farmNameList"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
<%-- 

				<div class="filterEle">
					<label for="txt"><s:property
							value="%{getLocaleProperty('farmerCode')}" /></label>
					<s:select name="farmerCode" id="farmerCode" list="farmerCodeList"
						headerKey="" headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2" />
				</div> --%>
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
				<s:select name="fpo" id="fpo" list="warehouseList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control select2" />
               </div>
               <div class="filterEle">
               <label for="icsName"><s:text name="icsName" /></label>
				<s:select name="icsName" id="icsName" list="icsNameList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control select2"/>
               </div>
               </s:if>

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
							
							<li><a href="#" onclick="exportPDF();" role="tab"
								data-toggle="tab" aria-controls="dropdown1"
								aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
										name="pdf" /></a></li>
							<li><a href="#" onclick="exportXLS()" role="tab"
								data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
									class="fa fa-table"></i> <s:text name="excel" /></a></li>
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
	</s:form>






	<s:form name="detailForm" action="cultivationReport_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
		<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
	</s:form>

</body>