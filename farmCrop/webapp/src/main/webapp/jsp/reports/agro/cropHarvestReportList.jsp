<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>



<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
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
var selectedFieldStaff='';
	jQuery(document).ready(function(){
		onFilterData();
		var enableSubgrid='<s:property value="enableSubgrid"/>';
		
		
		//if('<s:property value="getCurrentTenantId()"/>'=='pratibha'  && '<s:property value="getBranchId()"/>' =='bci'){
			  $("#fieldl option[value='10']").remove();
			  $("#fieldl option[value='6']").remove();
			  //}
		
		jQuery(".well").hide();
		
		
		 $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.updateView();
	     $("#daterange").data().daterangepicker.updateCalendars();
		 $('.applyBtn').click();
		document.getElementById('farmerId').selectedIndex=0;	
		document.getElementById('fieldStaff').selectedIndex=0;
		document.getElementById('farmCode').selectedIndex=0;
		//document.getElementById('cropType').selectedIndex=0;	
		document.getElementById('crop').selectedIndex=0;
		<s:if test="currentTenantId=='chetna' ||currentTenantId=='pratibha' && getBranchId()!='bci'">
		document.getElementById('icsName').selectedIndex=0;
		</s:if>
		//document.getElementById('fpo').selectedIndex=0;
		 <s:if test="currentTenantId=='chetna'">
		document.getElementById('stateName').selectedIndex=0;
		</s:if>
		document.getElementById('seasonId').selectedIndex=0;
		<s:if test='branchId==null'>
		document.getElementById('branchIdParma').selectedIndex=0;
		</s:if>
		populateQuantity();
		jQuery("#detail").jqGrid(
				{
					url:'cropHarvestReport_data.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				    styleUI : 'Bootstrap',
				   	mtype: 'POST',					
					postData:{
						"startDate" : function(){
		                     return document.getElementById("hiddenStartDate").value;           
		                    },		
		                    "endDate" : function(){
				               return document.getElementById("hiddenEndDate").value;
			                 },
			                 "seasonCode" : function(){
					               return document.getElementById("seasonId").value;
				                 },
				  "farmerId" : function(){		
					 
					return document.getElementById("farmerId").value;	
				  },
				  "selectedFieldStaff" : function(){			  
						return  document.getElementById('fieldStaff').value;
					  },
				  "farmCode" : function(){
					 
				  	  return document.getElementById("farmCode").value;
				  },
				  /* "filter.cropType": function(){
					  return document.getElementById('cropType').value;
				  }, */
				  "crop" : function(){
					
				  	  return document.getElementById("crop").value;
				  },
				  <s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
				  "icsName" : function(){
						
				  	  return document.getElementById("icsName").value;
				  },
				  </s:if>
				 /*  "fpo" : function(){
						
				  	  return document.getElementById("fpo").value;
				  }, */
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
				  		   	  '<s:text name="date"/>',
				  		      '<s:text name="seasonCode"/>',
				  		    '<s:property value="%{getLocaleProperty('agentName')}"/>',
				  			  '<s:property value="%{getLocaleProperty('farmerId')}"/>',
				  		   	 /*  '<s:property value="%{getLocaleProperty('fatherName')}"/>', */
				  		   	  '<s:text name="farm"/>',
				  		   	  '<s:property value="%{getLocaleProperty('village.name')}" />',
				  		    <s:if test="currentTenantId=='chetna'">
				  		 /*    '<s:property value="%{getLocaleProperty('warehouseName')}" />', */
				  		    
				  		  	'<s:text name="icsName"/>',
								   </s:if> 
				  		  <s:elseif test="currentTenantId=='pratibha' && getBranchId()!='bci'">
				  		'<s:text name="icsName"/>',
				  		</s:elseif>
				  		  
				  		   //	  '<s:text name="cropType"/>',
				  		   <s:if test="currentTenantId!='pratibha'">
				  		   	 /* '<s:text name="cropName"/>', */
				  		   '<s:text name="%{getLocaleProperty('totalYieldQuantity')}"/>',	
				  		   </s:if>
				  		   	 
				  		   <s:if test="currentTenantId=='pratibha'">
				  		   	 '<s:text name="%{getLocaleProperty('totalQtyMT')}"/>'
				  		   	</s:if>
				  		   	  /* '<s:text name="%{getLocaleProperty('totalYieldPrice')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'	 */			  		   
		 	      	 ],
				   	colModel:[
				   	          
						<s:if test='branchId==null'>
						{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>'}},	
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						
							{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
						</s:if>
						{name:'harvestDate',index:'harvestDate',width:70,sortable:false},
						{name:'seasonCode',index:'seasonCode',sortable:false},
						{name:'agentId',index:'agentId',sortable:false},
						{name:'farmerId',index:'farmerId',sortable:false},
						/* {name:'lastName',index:'lastName',width:125,sortable:false}, */
						{name:'farmCode',index:'farmCode',sortable:false},
						{name:'village',index:'village',sortable:false},
						<s:if test="currentTenantId=='chetna'">
					 /*    {name:'warehouseName',index:'warehouseName',width:250,sortable:false}, */
					    {name:'icsName',index:'icsName',sortable:false},
						   </s:if> 
					    <s:elseif test="currentTenantId=='pratibha' && getBranchId()!='bci'">
					    {name:'icsName',index:'icsName',sortable:false},
					    </s:elseif>
						//{name:'cropType',index:'cropType',width:250,sortable:false},
						<s:if test="currentTenantId!='pratibha'">
						/* {name:'cropName',index:'cropName',width:250,sortable:false}, */
						{name:'totalYieldQuantity',index:'totalYieldQuantity',sortable:false,align:'right'},
						</s:if>
						
						 <s:if test="currentTenantId=='pratibha'">
							{name:'totalQtyMT',index:'totalQtyMT',sortable:false,align:'right'}
			  		   	</s:if>
				   		/* {name:'totalYieldPrice',index:'totalYieldPrice',sortable:false,align:'right'} */
				   		
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   scrollOffset: 19,		    
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   //autowidth:true,
				   shrinkToFit:true,	
				   rowList : [10,25,50],
				   viewrecords: true,  	
				   onSelectRow: function(id){ 
						  document.updateform.id.value  =id;
				          document.updateform.submit();      
						},
						
						<s:if test='enableSubgrid=="0"'>
						   				   		
						</s:if>
						<s:else>
					 subGrid: true,
				   subGridOptions: {
					   "plusicon" : "glyphicon-plus-sign",
					   "minusicon" : "glyphicon-minus-sign",
					   "openicon" : "glyphicon-hand-right",
				   },
				   subGridRowExpanded: function(subgrid_id, row_id){
					  var subgrid_table_id, pager_id; 
					 subgrid_table_id = subgrid_id+"_t"; 
					   pager_id = "p_"+subgrid_table_id; 
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'cropHarvestReport_subGridDetail.action?id='+row_id,
						  	pager: pager_id,
						   	datatype: "json",	
						   	mtype:'POST',						   
						  	postData:{
					   		},
						   	colNames:[	
									  '<s:text name="cropType"/>',
									  '<s:text name="crop"/>',
						  		   	  '<s:text name="variety"/>',
						  		   	  '<s:text name="grade"/>',		
						  		    '<s:text name="unit"/>',
						  		   	  /* '<s:text name="%{getLocaleProperty('price/kg')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)', */
						  		      '<s:text name="%{getLocaleProperty('quantity')}"/>',
						  		      <s:if test="currentTenantId=='pratibha'">
	                                   // '<s:text name="quantity"/>&nbsp;(MT)'
	                                     '<s:property value="%{getLocaleProperty('quantityMT')}"/>',
	                                  </s:if>
						  		      /* '<s:text name="%{getLocaleProperty('totalPriceAmt')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)' */
				 	      	 ],
						   	colModel:[	
						   	        {name:'cropType',index:'cropType',sortable:false},
									{name:'crop',index:'crop',sortable:false},
		   							{name:'variety',index:'variety',sortable:false},
		   							{name:'grade',index:'grade',sortable:false},
		   							{name:'unit',index:'unit',sortable:false},
		   							/* {name:'price/kg',index:'price/kg',sortable:false,align:'right'}, */
		   							{name:'quantity',index:'quantity',sortable:false,align:'right'},
		   						    <s:if test="currentTenantId=='pratibha'">
									  {name:'quantityMT',index:'quantityMT',align:'right',sortable:false}
									</s:if>
		   							/* {name:'totalPrice',index:'totalPrice',sortable:false,align:'right'} */
						   	],
						   	scrollOffset: 0,
						    sortname:'id',
						    sortorder: "desc",
						   //autowidth: true,
						    shrinkToFit:true,
						    rowNum:10,
						    height:'100%',
						    rowList : [10,25,50],
						   viewrecords: true				
					   });
					  // jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
					   jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				},
					</s:else>
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
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	
        function populateQuantity(){
        	var stateName="";
        	var icsName="";
        	var branchIdParma="";
        	var branch='<s:property value="getBranchId()"/>';
        	var farmerId=document.getElementById("farmerId").value;
        	var farmCode=document.getElementById("farmCode").value;
        	var crop=document.getElementById("crop").value;
        	var selectedFieldStaff=document.getElementById("fieldStaff").value;
    		<s:if test="currentTenantId=='chetna' ||currentTenantId=='pratibha' && getBranchId()!='bci'">
    		var icsName=document.getElementById("icsName").value;
    		</s:if>
    		 <s:if test="currentTenantId=='chetna'">
    		var stateName=document.getElementById("stateName").value;
    		</s:if>
    		var seasonCode=document.getElementById("seasonId").value;
    		var startDate=document.getElementById("hiddenStartDate").value;  
    		var endDate=document.getElementById("hiddenEndDate").value;
    		if(branch=="")
    		{
    			branchIdParma=document.getElementById("branchIdParma").value
    		}
    		jQuery.post("cropHarvestReport_populateQuantity.action", {farmerId:farmerId,seasonCode:seasonCode,stateName:stateName,farmCode:farmCode,crop:crop,icsName:icsName,branchIdParma:branchIdParma,startDate:startDate,endDate:endDate,selectedFieldStaff:selectedFieldStaff},
    				function(result) {
    					var json = JSON.parse(result);
    			 		
    			 		$("#totalYieldQuantity").html(json[0].totalYieldQuantity);
    				});
        }
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
				selectedFieldStaff = document.getElementById("fieldStaff").value;
			jQuery("#detail").jqGrid('setGridParam',{url:"cropHarvestReport_data.action",page:1}).trigger('reloadGrid');	
		}
			populateQuantity();
		}

		function clear(){
			resetReportFilter();
			<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
			 document.getElementById("icsName").value = "";
			 </s:if>
			document.form.submit();
		}
		
		function resetDate(){
			jQuery("#startDate").val(jQuery("#hiddenStartDate").val());
			jQuery("#endDate").val(jQuery("#hiddenEndDate").val());
		}
		
		jQuery("#minus").click( function() {
			clear();
		});
		jQuery("#plus").click( function() {
			showFields();
		});

		
	});


	function addOption(selectbox,text,value ){
		var optn = document.createElement("OPTION");
		optn.text = text;
		optn.value = value;
		selectbox.options.add(optn);
	}		


	function exportXls(){
		 document.form.action="cropHarvestReport_populateXLS.action";
		 document.form.submit();
		 document.form.target="";
	}

	
	function exportXLS(){
		var headerData =null;
		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){
			var totalYieldQuantity=$("#totalYieldQuantity").text();
			headerData='<s:text name="totalYieldQuantity"/>'+"###"+totalYieldQuantity;
		jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			jQuery("#detail").jqGrid('excelExport', {url: 'cropHarvestReport_populateXLS.action?'});
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
			var totalYieldQuantity=$("#totalYieldQuantity").text();
			headerData='<s:text name="totalYieldQuantity"/>'+"###"+totalYieldQuantity;
		jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			jQuery("#detail").jqGrid('excelExport', {url: 'cropHarvestReport_populatePDF.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
	function onFilterData(){
		callAjaxMethod("cropHarvestReport_populateFarmerList.action","farmerId");
		callAjaxMethod("cropHarvestReport_populateFarmCodeList.action","farmCode");
		callAjaxMethod("cropHarvestReport_populateSeasonList.action","seasonId");
		callAjaxMethod("cropHarvestReport_populateCropList.action","crop");
		callAjaxMethod("cropHarvestReport_populateStateList.action","stateName");
		callAjaxMethod("cropHarvestReport_populateWarehouseList.action","fpo");
		callAjaxMethod("cropHarvestReport_populateIcsNameList.action","icsName");
		callAjaxMethod("cropHarvestReport_populateAgentList.action","fieldStaff");
		
	}
	function callAjaxMethod(url,name){
		var cat = $.ajax({
			url: url,
			async: true, 
			type: 'post',
			success: function(result) {
				insertOptions(name,JSON.parse(result));
			}        	

		});
		
	}

</script>

	<s:form name="form" action="cropHarvestReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class='filterEle'>
						<label for="txt"><s:text name="date" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm"  />
						</div>
					</div>

					<div class='filterEle'>
						<label for="txt"><s:text name="season" /></label>
						<div class="form-element">
							<s:select name="seasonCode" id="seasonId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2"  />
						</div>
					</div>

					<div class='filterEle'>
						<label for="txt"><s:property value="%{getLocaleProperty('farmer')}"/></label>
						<div class="form-element">
							<s:select name="farmerId" id="farmerId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2"  />
						</div>
					</div>

					<%-- <div class='filterEle'>
					<label for="txt"><s:property value="%{getLocaleProperty('fatherName')}" /></label>
					<div class="form-element">
						<s:select name="filter.cropHarvest.fatherName" id="fatherName"
					list="fathersNameList" headerKey=""
					headerValue="%{getText('txt.select')}"
					class="form-control input-sm select2"  />
					</div>
				</div> --%>

					<div class='filterEle'>
						<label for="txt"><s:text name="farm" /></label>
						<div class="form-element">
							<s:select name="farmCode" id="farmCode" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2"  />
						</div>
					</div>

					<div class='filterEle'>
						<label for="txt"><s:text name="cropName" /></label>
						<div class="form-element">
							<s:select name="filter.cropHarvestDetails.crop.code" id="crop"
								list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2"  />
						</div>
					</div>

					<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
						<div class='filterEle'>
							<label for="txt"><s:text name="farmer.icsName" /></label>
							<div class="form-element">
								<s:textfield name="icsName" id="icsName"
									class="upercls form-control" list="{}"
									headerKey=""
									listKey="key" listValue="value"/>
							</div>
						</div>
					</s:if>
					<s:if test="currentTenantId=='chetna'">
				<div class="filterEle">
					<label for="txt"><s:property
						value="%{getLocaleProperty('stateName')}" /></label>
					<div class="form-element">
					<s:select name="stateName" id="stateName" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="filterEle hide">
					<label for="txt"><s:property
						value="%{getLocaleProperty('cooperatives')}" /></label>
					<div class="form-element">
						<s:select name="fpo" id="fpo" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="icsName" /></label>
					<div class="form-element">
					<s:select name="icsName" id="icsName" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>

					<%-- <div class='filterEle'>
					<label for="txt"><s:property value="%{getLocaleProperty('warehouseName')}" /></label>
					<div class="form-element">
					<s:select name="warehouse" id="fpo" list="warehouseList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"  />
					</div>
				</div> --%>
					<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('profile.agent')}"/></label>
					<div class="form-element">
						<s:select name="selectedFieldStaff" id="fieldStaff"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>

					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class='filterEle'>
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParam" id="branchIdParam"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="input-sm form-control select2"
										
										onchange="populateChildBranch(this.value)" />
								</div>
							</div>
						</s:if>
						<div class='filterEle'>
							<label for="txt"><s:text name="app.subBranch" /></label>
							<div class="form-element">
								<s:select name="subBranchIdParam" id="subBranchIdParam"
									list="subBranchesMap" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2"  />
							</div>
						</div>
					</s:if>
					<s:else>
						<s:if test='branchId==null'>
							<div class='filterEle'>
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

					<div class='filterEle' style="margin-top: 2%;margin-right: 0%;">
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
		<s:if test="currentTenantId!='pratibha'">
			 <div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container">
					<div class="report-summaryBlockItem">
					<span><span class="strong" id="totalYieldQuantity"></span> <s:text name="%{getLocaleProperty('totalYieldQuantityKgs')}"/> (Kgs)&nbsp;</span>
					</div>
				
				</div>
			</div> 
</s:if>
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
							data-toggle="tab" aria-controls="dropdown1" aria-expanded="false"><i
								class="fa fa-file-pdf-o"></i> <s:text name="pdf" /></a></li>
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
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>