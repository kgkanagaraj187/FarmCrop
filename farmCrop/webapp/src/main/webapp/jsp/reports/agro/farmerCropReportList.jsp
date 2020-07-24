<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
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
	padding: 2px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: auto !important;
	padding: 2px;
}
</style>
</head>
<body>
	<script type="text/javascript">
	 var recordLimit='<s:property value="exportLimit"/>';
	 jQuery(document).ready(function(){
		 jQuery(".farmerCodeHide").hide();
		 jQuery(".well").hide();
		$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.updateView();
	     $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		document.getElementById("farmerNameId").selectedIndex=0;
		//document.getElementById("fatherNameId").selectedIndex=0;
		document.getElementById("farmerCodeId").selectedIndex=0;
		document.getElementById("villageId").selectedIndex=0;
		<s:if test="currentTenantId!='livelihood'">
		document.getElementById('seasonId').selectedIndex=0;</s:if>
		<s:if test="currentTenantId=='pratibha'">
		document.getElementById('samithiName').selectedIndex=0;
		</s:if>
		
		$('#daterange').val()
		<s:if test='branchId==null'>
		document.getElementById('branchIdParam').selectedIndex=0;
		</s:if>
		jQuery("#detail").jqGrid(
				{
					url:'farmerCropReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				    styleUI : 'Bootstrap',
				   	postData:{	
					"startDate" : function(){
                     return document.getElementById("hiddenStartDate").value;           
                    },		
                    "endDate" : function(){
		               return  document.getElementById("hiddenEndDate").value;
	                 },
	                 "farmerName":function(){
		               return document.getElementById("farmerNameId").value;
	                 },
	                 
	                 /* "fatherName":function(){
		               return document.getElementById("fatherNameId").value;
	                 }, */
	                 <s:if test="currentTenantId=='pratibha'">
	         		"samithiName" : function(){			  
	 					  return document.getElementById("samithiName").value;
	 	 			  },</s:if>
	                 "farmerCode":function(){
			           return document.getElementById("farmerCodeId").value;
		             },
		             "villageName":function(){
				           return document.getElementById("villageId").value;
			             },
			             <s:if test='branchId==null'>
		             "branchIdParma":function(){
			               return document.getElementById("branchIdParam").value;
		                 },
			             </s:if>
		                 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						  "subBranchIdParma" : function(){
							  return document.getElementById("subBranchIdParam").value;
						  },
						  </s:if>
							<s:if test="currentTenantId!='livelihood'">
						  "seasonCode" : function(){			  
	   	 					  return document.getElementById("seasonId").value;
	   	 	 			      },</s:if>
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
						</s:if><s:if test='currentTenantId!="livelihood"'>
							'<s:property value="%{getLocaleProperty('cSeasonCode')}" />',</s:if>
							<s:if test ="currentTenantId=='welspun'">
							'<s:property value="%{getLocaleProperty('sowingDate')}" />',
							</s:if>
							 <s:if test="farmerCodeEnabled==1">
                             '<s:property value="%{getLocaleProperty('farmerCode')}" />',
                             </s:if> 
                             
                             '<s:property value="%{getLocaleProperty('farmer')}" />',
                           //  '<s:property value="%{getLocaleProperty('fatherName')}"/>',
                             <s:if test="currentTenantId=='pratibha' && getBranchId()=='organic'">
                             '<s:text name="nameOfICS"/>',
                             </s:if>
                             //'<s:text name="yearOfJoiningICS"/>',
                             '<s:property value="%{getLocaleProperty('village.name')}" />',
                             /* <s:if test="currentTenantId!='pratibha'">
                             '<s:property value="%{getLocaleProperty('city.name')}" />',
                             '<s:property value="%{getLocaleProperty('locality.name')}" />',
	                         '<s:property value="%{getLocaleProperty('state.name')}" />',
	                         </s:if> */
	                         '<s:text name="farm"/>',
	                         <s:if test='currentTenantId=="livelihood"'>
	                         '<s:property value="%{getLocaleProperty('totalLandArea')}" />',
	                         '<s:property value="%{getLocaleProperty('cocoTypeLabel')}" />',
	                         </s:if>
                             <s:if test='enableMultiProduct=="1"'>
	                         '<s:property value="%{getLocaleProperty('cropTypeMain')}" />',
	                         </s:if>
	                         '<s:property value="%{getLocaleProperty('produnit')}" />',
	                         <s:if test ="currentTenantId=='welspun'">
	                         '<s:property value="%{getLocaleProperty('variety')}" />',
								</s:if>
	                         '<s:property value="%{getLocaleProperty('cottonArea')}" /> (<s:property value="%{getAreaType()}" />)',
	                         
	                       /*   <s:if test='currentTenantId =="pratibha"'>
	                         '<s:text name="cropName"/>',
	                         </s:if> */
	                         <s:if test='currentTenantId!="livelihood"'>
                             '<s:property value="%{getLocaleProperty('type')}"/>',
                             </s:if>
                             <s:if test='enableMultiProduct=="0"'>
                             '<s:text name="staple"/>',
	                         </s:if>
                             '<s:property value="%{getLocaleProperty('interCropDetails')}"/>',
                             <s:if test ="currentTenantId=='pratibha'">
                            	 '<s:text name="%{getLocaleProperty('borderCropDetails')}"/>',
                            	 '<s:text name="%{getLocaleProperty('coverCropDetails')}"/>',
                            	 '<s:text name="%{getLocaleProperty('plantOnbundCropDetails')}"/>',
                            	 '<s:text name="%{getLocaleProperty('trapCropDetails')}"/>',
                             </s:if>
                             <s:if test ="currentTenantId!='chetna' && currentTenantId!='welspun'">
                             '<s:property value="%{getLocaleProperty('estimatedCottonYield')}"/>',
                             </s:if>
                             <s:if test =" currentTenantId!='welspun' && currentTenantId!='gsma' && currentTenantId!='livelihood'">
                             '<s:property value="%{getLocaleProperty('seedCottonHarvested')}"/>',
                             </s:if>
                             <s:if test='enableMultiProduct=="0" && currentTenantId!="chetna" && currentTenantId!="gsma"'>
                             '<s:property value="%{getLocaleProperty('lintCottonHarvested')}"/>',
	                         </s:if>
                             <s:if test='currentTenantId=="livelihood"'>
                            	 '<s:property value="%{getLocaleProperty('prodTrees')}" />',
                            	 '<s:property value="%{getLocaleProperty('affTrees')}" />',
                            	 '<s:property value="%{getLocaleProperty('noOfTrees')}" />',
                            	 '<s:property value="%{getLocaleProperty('seedQtyUsed')}" />',
                             </s:if>
                             <s:if test ="currentTenantId!='iccoa' && currentTenantId!='welspun' && currentTenantId!='gsma' && currentTenantId!='livelihood' ">
                             '<s:property value="%{getLocaleProperty('yieldPerAcre')}"/>'
                             </s:if>
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
			   			        }]
			   			
			   			}},	   				   		
			   		</s:if>
			   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
			   		
			   			{name:'subBranchId',index:'subBranchId',sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
			   		</s:if><s:if test='currentTenantId!="livelihood"'>
								{name:'cSeasonCode',index:'cSeasonCode',sortable:false,frozen:false},</s:if>
								<s:if test ="currentTenantId=='welspun'">
								{name:'sowingDate',index:'sowingDate',sortable:false,frozen:false},
								</s:if>
								 <s:if test="farmerCodeEnabled==1">
		 					    {name:'farmerCode',index:'farmerCode',sortable:false,frozen:false},
		 					    </s:if> 
		 					  
		 					    {name:'farmerName',index:'farmerName',sortable:false,frozen:false},
		 					  //{name:'fatherName',index:'fatherName',width:150,sortable:false},
		 					 <s:if test="currentTenantId=='pratibha' && getBranchId()=='organic'">
		 					    {name:'nameOfICS',index:'nameOfICS',sortable:false},
		 					    </s:if>
		 					    //{name:'yearOfJoiningICS',index:'yearOfJoiningICS',width:150,sortable:false},
		 						{name:'village',index:'village',sortable:false},
		 						/* <s:if test="currentTenantId!='pratibha'">
		 					    {name:'taluk',index:'taluk',width:150,sortable:false},
		 					    {name:'district',index:'district',align:'right',width:150,sortable:false},
		 					    {name:'state',index:'state',align:'right',width:150,sortable:false},
		 					    </s:if> */
		 					   {name:'farmName',index:'farmName',sortable:false},
		 					   <s:if test='currentTenantId=="livelihood"'>
		 					   {name:'totalLandArea',index:'totalLandArea',sortable:false},
		 					   {name:'cocoType',index:'cocoType',sortable:false},
		 					   </s:if>
		 					    <s:if test='enableMultiProduct=="1" '>
								{name:'cropType',index:'cropNames',sortable:false},
								</s:if>
								  {name:'unit',index:'unit',sortable:false},
								<s:if test ="currentTenantId=='welspun'">
								{name:'variety',index:'variety',sortable:false,frozen:false},
								</s:if>
								{name:'cottonAreaInAcres',index:'cottonAreaInAcres',align:'right',sortable:false},
								
								/*  <s:if test='currentTenantId =="pratibha"'>
								{name:'cropName',index:'cropName',align:'right',sortable:false},
								</s:if> */
							    <s:if test='currentTenantId!="livelihood"'>
		 						{name:'cottonType',index:'cottonType',align:'left',sortable:false},
	                            </s:if>
		 						 <s:if test='enableMultiProduct=="0" '>
		 						{name:'staple',index:'staple',align:'right',sortable:false},
								</s:if>
		 						{name:'interCropDetails',index:'interCropDetails',align:'left',sortable:false},
		 						<s:if test ="currentTenantId=='pratibha'">
		 						{name:'borderCropDetails',index:'borderCropDetails',align:'left',sortable:false},
		 						{name:'coverCropDetails',index:'coverCropDetails',align:'left',sortable:false},
		 						{name:'plantOnbundCropDetails',index:'plantOnbundCropDetails',align:'left',sortable:false},
		 						{name:'trapCropDetails',index:'trapCropDetails',align:'left',sortable:false},
		 						</s:if>
		 						 <s:if test ="currentTenantId!='chetna' && currentTenantId!='welspun'">
		 						{name:'estimatedCottonYield',index:'estimatedCottonYield',align:'right',sortable:false},
		 						</s:if>
		 						<s:if test ="currentTenantId!='welspun' && currentTenantId!='gsma' && currentTenantId!='livelihood'">
		 						{name:'actualCottonSeedHarvested',index:'actualCottonSeedHarvested',align:'right',sortable:false},
		 						</s:if>
		 						<s:if test='enableMultiProduct=="0" && currentTenantId!="chetna" && currentTenantId!="gsma"'>
		 						{name:'actualLintCottonHarvested',index:'actualLintCottonHarvested',align:'right',sortable:false},
								</s:if>
		 						<s:if test='currentTenantId=="livelihood"'>
		 						{name:'prodTrees',index:'prodTrees',align:'right',sortable:false},
		 						{name:'affTrees',index:'affTrees',align:'right',sortable:false},
		 						{name:'noOfTrees',index:'noOfTrees',align:'right',sortable:false},
		 						{name:'seedQtyUsedPfx',index:'seedQtyUsedPfx',align:'right',sortable:false},
		 						</s:if>
		 						<s:if test='currentTenantId!="iccoa" && currentTenantId!="welspun" && currentTenantId!="gsma" && currentTenantId!="livelihood"'>
		 						{name:'pricePerAcre',index:'pricePerAcre',align:'right',sortable:false}
		 						</s:if>
									],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   //autowidth:true,
				   shrinkToFit:true,	
				   scrollOffset: 19,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true  	
				}).jqGrid('setFrozenColumns');

		$('#detail').jqGrid('setGridHeight',(reportWindowHeight));
		
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
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
		   jQuery("#detail").jqGrid('setGridParam',{url:"farmerCropReport_detail.action?",page:1}).trigger('reloadGrid');
		   }
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
			jQuery("#detail").jqGrid('excelExport', {url: 'farmerCropReport_populateXLS.action'});
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
			jQuery("#detail").jqGrid('excelExport', {url: 'farmerCropReport_populatePDF.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
</script>
	<s:form name="form" action="farmerCropReport_list">
		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
			<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control input-sm" />
					</div>
				</div>
					
				
				<div class="filterEle">
					<label for="txt"><s:property
							value="%{getLocaleProperty('farmer')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.id" id="farmerNameId"
							list="farmerNameList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
			
				<%-- <div class="reportFilterItem">
					<label for="txt"><s:property
							value="%{getLocaleProperty('fatherName')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.id" id="fatherNameId"
							list="fatherNameList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div> --%>
				<div class="filterEle">
					<label for="txt"><s:property
							value="%{getLocaleProperty('village')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.village.code" id="villageId"
							list="villageList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
				<s:if test="currentTenantId=='pratibha'">
			    <div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('samithiName')}" /></label>
					
					<div class="form-element">
						<s:select name="samithiName" id="samithiName"
							list="samithiNameList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				<div class="filterEle farmerCodeHide">
					<label for="txt"><s:property
							value="%{getLocaleProperty('farmerCode')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.Code" id="farmerCodeId"
							list="farmerCodList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2"/>
					</div>
				</div>
				<s:if
					test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
						<div class="filterEle">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element ">
								<s:select name="branchIdParma" id="branchIdParam"
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
						<s:select name="branchIdParma" id="branchIdParam"
							list="branchesMap" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				</s:else>
				<s:if test="currentTenantId!='livelihood'">
					<div class="filterEle">
						<label for="branchIdParam"><s:property
									value="%{getLocaleProperty('cSeasonCode')}" /></label>
						<div class="form-element ">
						<s:select name="filter.seasonCode" id="seasonId" list="seasonList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
						</div>
					</div></s:if>
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
							<span><span class="strong">0</span> Total Area(Acre)&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong">0</span> Total Estimated Yield&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
						
					</div>
				</div>
				
				 
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
					<s:if test="currentTenantId!='pratibha' && currentTenantId!='livelihood'">
					 <li role="presentation"><a href="#" onclick="exportPDF();"
						tabindex="-1" role="menuitem"> <s:text name="pdf" />
					</a></li> 
					</s:if>
				</ul>
			</div>
		</div>
			</div>
			<div style="width: 100%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>

		</div>
	</s:form>
		
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
