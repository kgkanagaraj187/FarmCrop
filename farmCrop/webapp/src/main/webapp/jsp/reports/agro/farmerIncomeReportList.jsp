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
//var sDate='';
//var eDate='';
var farmerName='';
var lastName='';
var farmerCode='';
var villageId='';
var recordLimit='<s:property value="exportLimit"/>';
	jQuery(document).ready(function(){
		jQuery(".well").hide();
		//jQuery(".candaHide").hide();
		//document.getElementById('branchIdParma').selectedIndex=0;
		//alert("uariaufaf");
			
		//document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
		//document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
		//sDate = document.getElementById("startDate").value;
		//eDate = document.getElementById("endDate").value;
		document.getElementById('farmerName').selectedIndex=0;
		document.getElementById('seasonId').selectedIndex=0;
		document.getElementById('villageCode').selectedIndex=0;
		<s:if test="currentTenantId=='chetna'">
		document.getElementById('stateName').selectedIndex=0;
		document.getElementById('fpo').selectedIndex=0;
		document.getElementById('icsName').selectedIndex=0;
		</s:if>
		
		jQuery("#detail").jqGrid(
				{
					url:'farmerIncomeReport_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				   	styleUI : 'Bootstrap',
				   	postData:{	
					//"startDate" : function(){			  
					//return sDate;
				//  },
				  //"endDate" : function(){			  
				//	return eDate;
				 /// },
					 "farmerName" : function(){
					  return document.getElementById("farmerName").value;
				  },
				  /* "lastName" : function(){
					  return document.getElementById("lastName").value;
				  }, */
				 /*  "farmerCode" : function(){
					  return document.getElementById("farmerCode").value;
				  }, */
				  "seasonCode" : function(){			  
	 					  return document.getElementById("seasonId").value;
	 	 			      },
				  "villageCode": function(){
					  return document.getElementById('villageCode').value;
				  },
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
				  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				  "subBranchIdParma" : function(){
					  return document.getElementById("subBranchIdParam").value;
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
							'<s:property value="%{getLocaleProperty('season')}" />',
	                        /*  <s:if test="farmerCodeEnabled==1">
                           '<s:property value="%{getLocaleProperty('farmerCode')}" />',
                           </s:if> */
                           
                           '<s:property value="%{getLocaleProperty('farmer')}" />',
                          // '<s:property value="%{getLocaleProperty('fatherName')}"/>',
	                         '<s:text name="farm"/>',
	                         <s:if test="currentTenantId=='pratibha' && getBranchId()=='organic'">
	                         '<s:text name="icsName"/>',
	                         </s:if>
	                         //'<s:text name="joiningYearIcs"/>',
	                         '<s:text name="village"/>',
	                        
	                         <s:if test="currentTenantId!='pratibha'">
	                           '<s:property value="%{getLocaleProperty('primaryIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           /* 
	                           '<s:property value="%{getLocaleProperty('cottonIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                            */
	                           '<s:property value="%{getLocaleProperty('interCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           </s:if>
	                           <s:if test="currentTenantId=='pratibha'">
	                           '<s:property value="%{getLocaleProperty('primaryIncome')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           '<s:property value="%{getLocaleProperty('interCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           '<s:property value="%{getLocaleProperty('borderCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           '<s:property value="%{getLocaleProperty('coverCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           '<s:property value="%{getLocaleProperty('plantonBundCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           '<s:property value="%{getLocaleProperty('trapCropIncomeCrop')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           
	                           </s:if>
	                           /* 
	                         '<s:property value="%{getLocaleProperty('agriGross')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                          */
	                         '<s:text name="incomeOther"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                        
	                         '<s:property value="%{getLocaleProperty('agriGross')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                         
	                         <s:if test="currentTenantId!='pratibha'">
	                         '<s:property value="%{getLocaleProperty('loanTakenLastYear')}" />',
	                         </s:if>
	                         /* <s:if test="currentTenantId!='pratibha'">
	                         <s:if test="isInsured==1">
	                         '<s:text name="lifeInsurance"/>',
	                         '<s:text name="healthInsurance"/>',
	                         '<s:text name="cropInsuranece"/>'
	                         </s:if>
	                         </s:if> */
	                         
				  		      
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
			   		
			   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
			   		</s:if>
			 					{name:'cSeasonCode',index:'cSeasonCode',sortable:false},
			 				/* 	<s:if test="farmerCodeEnabled==1">
		 					    {name:'farmerCode',index:'farmerCode',sortable:false},
		 					    </s:if> */
		 					 
		 					    {name:'farmerName',index:'farmerName',sortable:false},
		 					//   {name:'lastName',index:'lastName',width:150,sortable:false},
		 					    {name:'farmName',index:'farmName',sortable:false},	
		 					   <s:if test="currentTenantId=='pratibha' && getBranchId()=='organic'">
		 					    {name:'icsName',index:'icsName',sortable:false},
		 					    </s:if>
		 					   //{name:'joiningYearIcs',index:'joiningYearIcs',width:200,sortable:false},
		 					   	{name:'village',index:'village',sortable:false},
		 					  /*  <s:if test="currentTenantId!='pratibha'">
		 					   {name:'taluka',index:'batch',width:150,sortable:false},
		 					   {name:'district',index:'district',width:150,sortable:false},
		 					   {name:'state',index:'state',align:'right',width:150,sortable:false},
		 					   </s:if> */
		 					  <s:if test="currentTenantId!='pratibha'">
		 					   {name:'agriIncome',index:'agriIncome',align:'right',sortable:false},
		 					/*  
		 					  {name:'cottonIncome',index:'cottonIncome',align:'right',width:220,sortable:false},
		 					 */
		 					    {name:'interCropIncome',index:'interCropIncome',align:'right',sortable:false},
		 					    </s:if>
		 					   <s:if test="currentTenantId=='pratibha'">
		 					  {name:'agriIncome',index:'agriIncome',align:'right',sortable:false},
		 					 {name:'interCropIncome',index:'interCropIncome',align:'right',sortable:false},
		 					{name:'borderCropIncome',index:'interCropIncome',align:'right',sortable:false},
		 					{name:'coverCropIncome',index:'interCropIncome',align:'right',sortable:false},
		 					{name:'plantonBundCropIncome',index:'interCropIncome',align:'right',sortable:false},
		 					{name:'trapCropIncome',index:'interCropIncome',align:'right',sortable:false},
			 					
		 					   </s:if>
		 					  /* 
		 					   {name:'agriGross',index:'agriGross',align:'right',sortable:false},
		 					    */
		 					   {name:'otherIncome',index:'otherIncome',align:'right',sortable:false},
		 					  
		 					   {name:'agriGross',index:'agriGross',align:'right',sortable:false},
		 					   
		                      <s:if test="currentTenantId!='pratibha'">
		 					  {name:'loanTakenLastYear',index:'loanTakenLastYear',align:'left',sortable:false}
		 					  </s:if>
		 					/*  <s:if test="currentTenantId!='pratibha'">
		 					  <s:if test="isInsured==1">
		 					  {name:'lifeInsurance',index:'lifeInsurance',align:'right',sortable:false},
		 					  {name:'healthInsurance',index:'lifeInsurance',align:'right',sortable:false},
		 					 {name:'cropInsuranece',index:'cropInsuranece',align:'right',sortable:false}
		 					  </s:if>
		 					  </s:if> */
		 			       
		 						
		 					   	],
				   height: 380, 
				   width: $("#baseDiv").width(),
				   //autowidth:true,
				   shrinkToFit:true,	
				   scrollOffset: 19,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
				   subGrid: false,
				   subGridOptions: {
				   "plusicon" : "ui-icon-triangle-1-e",
				   "minusicon" : "ui-icon-triangle-1-s",
				   "openicon" : "ui-icon-arrowreturn-1-e",
				   },
				 /*  subGridRowExpanded: function(subgrid_id, row_id){
					   var subgrid_table_id, pager_id; 
					   subgrid_table_id = subgrid_id+"_t"; 
					   pager_id = "p_"+subgrid_table_id; 
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>"); //<div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'cropSaleReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
									'<s:text name="cropName"/>',
									'<s:text name="variety"/>',
                                    '<s:text name="grade"/>',
                                    '<s:text name="prices"/>',
                                    '<s:text name="quantity"/>',
                                    '<s:text name="totalPrice"/>'
                                     ],				 	      	 
						   	colModel:[
									{name:'cropName',index:'cropName',width:150,sortable:false},
									{name:'variety',index:'variety',width:150,sortable:false},
									{name:'grade',index:'grade',width:150,sortable:false},
									{name:'prices',index:'prices',align:'right',width:150,sortable:false},
									{name:'quantity',index:'quantity',align:'right',width:150,sortable:false},
									{name:'totalPrice',index:'totalPrice',align:'right',width:150,sortable:false}					
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    autowidth: true,
						    viewrecords: true				
					   });
					 //  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
					   jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				},*/				
				
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
			    //jQuery("#detail").jqGrid('setFrozenColumns');
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	

		function reloadGrid(){
			//alert("lod");
			//var startDate=new Date(document.getElementById("startDate").value);
			//var endDate=new Date(document.getElementById("endDate").value);
			//if (startDate>endDate){
				//alert('<s:text name="date.range"/>');
			//}else{
				//sDate = document.getElementById("startDate").value;
				//eDate = document.getElementById("endDate").value;	
				farmerName = document.getElementById("farmerName").value;
				//farmerName = document.getElementById("lastName").value;
				//alert("farmerName :"+farmerName);
				//farmerCode = document.getElementById("farmerCode").value;
		   jQuery("#detail").jqGrid('setGridParam',{url:"farmerIncomeReport_data.action?",page:1}).trigger('reloadGrid');
		   //}
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
			jQuery("#detail").jqGrid('excelExport', {url: 'farmerIncomeReport_populateXLS.action'});
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
			jQuery("#detail").jqGrid('excelExport', {url: 'farmerIncomeReport_populatePDF.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
</script>

		<s:form name="form" action="farmerIncomeReport_list">
		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
			<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('cSeasonCode')}" /></label>
					<div class="form-element">
						<s:select name="filter.seasonCode" id="seasonId" list="seasonList"
						headerKey="" headerValue="%{getText('txt.select')}"
						cssClass="form-control input-sm select2"  />
					</div>
				</div>
				
				<div class="filterEle">
					<label for="txt"><s:property
						value="%{getLocaleProperty('farmerName')}" /></label>
					<div class="form-element">
							<s:select name="filter.farmer.id" id="farmerName"
						list="farmerNameList" headerKey=""
						headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2" />
					</div>
				</div>
				<%-- <div class="reportFilterItem">
					<label for="txt"><s:property value="%{getLocaleProperty('lastName')}" /></label>
					<div class="form-element">
					<s:select name="filter.farmer.id" id="lastName"
						list="fatherNameList" headerKey=""
						headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2" />
					</div>
				</div> --%>
				<div class="filterEle">
					<label for="txt"><s:text name="village" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.village.code" id="villageCode"
						list="villageMap" headerKey=""
						headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2"  />
					</div>
				</div>
				<%-- <div class="filterEle candaHide">
					<label for="txt"><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParma" id="branchIdParma"
						list="branchesMap" headerKey=""
						headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2"/>
					</div>
				</div> --%>
		<%-- 		<div class="filterEle">
					<label for="txt"><s:property
							value="%{getLocaleProperty('farmerCode')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.Code" id="farmerCode"
						list="farmerCodList" headerKey=""
						headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2" />
					</div>
				</div> --%>
				<s:if
					test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
						<div class="filterEle">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element">
								<s:select name="branchIdParam" id="branchIdParam"
							list="parentBranches" headerKey="" headerValue="Select"
							cssClass="input-sm form-control select2"
							onchange="populateChildBranch(this.value)" />
							</div>
						</div>
					</s:if>
						<label for="txt"><s:text name="app.subBranch" /></label>
						<s:select name="subBranchIdParam" id="subBranchIdParam"
							list="subBranchesMap" headerKey="" headerValue="Select"
							cssClass="input-sm form-control select2" />
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
						value="%{getLocaleProperty('fpo')}" /></label>
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
					 
					<li role="presentation"><a href="#" onclick="exportPDF();"
						tabindex="-1" role="menuitem"> <s:text name="pdf" />
					</a></li>
					
				</ul>
			</div>
		</div>
	</div>
	<div style="width:99%;" id="baseDiv">
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
