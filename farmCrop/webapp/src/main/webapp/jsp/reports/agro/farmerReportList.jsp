<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
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
var sDate='';
var eDate='';
var recordLimit='<s:property value="exportLimit"/>';
jQuery(document).ready(function(){
	

	$('.icsTypeHide').hide();
	var tenant='<s:property value="getCurrentTenantId()"/>';
	//alert(tenant);
	
	$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.updateView();
     $("#daterange").data().daterangepicker.updateCalendars();
	$('.applyBtn').click();
	if(tenant=='livelihood'){
		$('#daterange').hide();
		
	}
	if(tenant=='avt') {
		$('.icsNameClass').hide();
		$('.iccoaShow').hide();
	}
	
	if(tenant=='gar') {
		$('.icsNameClass').show();
	}
	else{
		$('.icsNameClass').hide();
	}
	/* if(tenant=='iccoa'){
		$('.iccoaHide').hide();
		$('.iccoaShow').show();
	}
	else{
		$('.iccoaHide').show();
		$('.iccoaShow').hide();
	} */
	if(tenant=="susagri"){		
		$('.susAgriHide').hide();
	}
	if(tenant=='blri')
	{
		$('#hideBlriExp').hide();
	}
	else
	{
		$('#hideBlriExp').show();
		
	}
	
	if('<s:property value="getCurrentTenantId()"/>'!='pratibha'){
		$("#fieldl option[value='8']").remove();
		$("#fieldl option[value='9']").remove();
		}
		
	loadGrid();
	
	jQuery(".well").hide();
  // document.getElementById('farmerCode').selectedIndex=0;
	/* document.getElementById('firstName').selectedIndex=0;
	 */
	//document.getElementById('lastName').selectedIndex=0;
	document.getElementById('villageName').selectedIndex=0;
	 <s:if test="currentTenantId!='pratibha'">
	document.getElementById('stateName').selectedIndex=0;
	<s:if test="currentTenantId!='efk'&& currentTenantId!='ocp' && currentTenantId!='symrise' && currentTenantId!='gsma' && currentTenantId!='ecoagri' && currentTenantId!='gar' && currentTenantId!='agro' && currentTenantId!='worldvision' && currentTenantId!='wilmar' && currentTenantId!='welspun' && currentTenantId!='awi' && currentTenantId!='avt'">
	document.getElementById('fpo').selectedIndex=0;
	</s:if>
	<s:if test="currentTenantId!='wilmar' && currentTenantId!='efk' && currentTenantId!='ocp'  && currentTenantId!='gsma' && currentTenantId!='olivado' && currentTenantId!='symrise' && currentTenantId!='sticky'  && currentTenantId!='ecoagri'  && currentTenantId!='awi' && currentTenantId!='iccoa'">
	document.getElementById('icsName').selectedIndex=0;
	</s:if></s:if>	 <s:if test="currentTenantId!='wilmar' && currentTenantId!='efk' && currentTenantId!='gsma' ">
	document.getElementById('gender').selectedIndex=0;</s:if>
	<s:if test="currentTenantId!='chetna' && currentTenantId!='ocp' && currentTenantId!='symrise'">
	document.getElementById('season').selectedIndex=0;
	</s:if>
	<s:if test="currentTenantId!='pratibha' && currentTenantId!='chetna' && currentTenantId!='gsma' && currentTenantId!='wilmar' && currentTenantId!='efk' && currentTenantId!='welspun'  && currentTenantId!='ecoagri'">
	document.getElementById('icsType').selectedIndex=-1;
	</s:if>
	/*   <s:if test='branchId==null'>
	document.getElementById('branchIdParam').selectedIndex=0;
	   </s:if> */
	   <s:if test="currentTenantId=='wilmar'">
	   document.getElementById('organicStatus').selectedIndex=-1;
	   </s:if>
	  
	   <s:if test="currentTenantId=='avt'|| currentTenantId=='susagri' ">
		document.getElementById('samithiName').selectedIndex=0;
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
		   	url:'farmerReport_data.action',
		   	pager: '#pagerForDetail',
		   	datatype: "json",
		   	mtype: 'POST',
			styleUI : 'Bootstrap',
			datatype: "json",
			postData:{	
		/* 		 "filter1.farm.farmer.farmerCode" : function(){			  
					return document.getElementById("farmerCode").value;
			     }, */
		          
			   	"startDate" : function(){
		              return document.getElementById("hiddenStartDate").value;           
		             },		
		       "endDate" : function(){
		          return document.getElementById("hiddenEndDate").value;
		           },
		           
			   /*  "filter1.farm.farmer.firstName" : function(){			  
					return document.getElementById("firstName").value;
	 			 }, */
	 			 
	 			 "villageName" : function(){			  
					  return document.getElementById("villageName").value;
	 			  }, 
	 			 /*  <s:if test="currentTenantId!='wilmar'&& currentTenantId!='efk' && currentTenantId!='olivado'">
	 			   <s:if test='branchId==null'>
	 			   "branchIdParma" : function(){
					  return document.getElementById("branchIdParam").value;
				  },
	 			   </s:if>  </s:if> */
				  
				  <s:if test="currentTenantId!='pratibha' && currentTenantId!='pgss'">
				  "stateName" : function(){
				  return  document.getElementById('stateName').value;
				  }, 
				  <s:if test="currentTenantId=='iccoa'">
				  "insYear" : function(){
					  return document.getElementById('insYear').value;
				  },</s:if>
				  <s:if test="currentTenantId=='avt'">
				  "createdUserName" : function(){
					  return document.getElementById('createdUser').value;
				  },</s:if>
				  <s:if test="currentTenantId!='efk' && currentTenantId!='ocp' && currentTenantId!='gar' && currentTenantId!='gsma' && currentTenantId!='agro'&& currentTenantId!='worldvision' && currentTenantId!='wilmar' && currentTenantId!='welspun'&& currentTenantId!='symrise' && currentTenantId!='awi' && currentTenantId!='avt'">
				  
				  "fpo" : function(){
					  return  document.getElementById('fpo').value;
					  },
					  </s:if>
<s:if test="currentTenantId!='wilmar' && currentTenantId!='efk' && currentTenantId!='olivado' && currentTenantId!='symrise' && currentTenantId!='gsma' && currentTenantId!='sticky' && currentTenantId!='ecoagri' && currentTenantId!='awi'&& currentTenantId!='ocp' && currentTenantId!='iccoa'">  
				"filter1.farm.farmer.icsName" : function(){
						  return  document.getElementById('icsName').value;
						  },
						  </s:if>
				  </s:if>
				  <s:if test="currentTenantId!='wilmar' && currentTenantId!='gsma' ">	  
				 "gender" : function(){
							  return  document.getElementById('gender').value;
							  }, </s:if>
							  <s:if test="currentTenantId!='chetna'&&currentTenantId!='ocp' && currentTenantId!='symrise'">
							
		
							  "season" : function(){
								  return  document.getElementById('season').value;
								  },
								</s:if> 
				
				
				<s:if test="currentTenantId!='pratibha' && currentTenantId!='chetna'&& currentTenantId!='pgss' && currentTenantId!='wilmar'&& currentTenantId!='efk' && currentTenantId!='welspun'">
				"icsType" : function(){
								return  document.getElementById('icsType').value;
								},
				 </s:if>
							  
				/*   <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				  "subBranchIdParma" : function(){
					  return document.getElementById("subBranchIdParam").value;
				  }, */
				  <s:if test="&& currentTenantId=='wilmar'">"
	 	 			"organicStatus" : function(){			  
	 				      return document.getElementById("organicStatus").value;
	 				          },
	 	 			  </s:if>
	 				<s:if test="currentTenantId=='avt' || currentTenantId=='susagri' ">"
	 		 	 			"samithiName" : function(){			  
	 		 				      return document.getElementById("samithiName").value;
	 		 				          },
	 		 	 			  </s:if>
				 /*  </s:if> */
				  
						 
			},
			
			colNames:[
			  <s:if test='branchId==null'>
					'<s:text name="%{getLocaleProperty('app.branch')}"/>',
			  </s:if>
			  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
			  </s:if>
					 <s:if test="currentTenantId =='ocp'">
					 '<s:property value="%{getLocaleProperty('farmerCode')}" />',
					 </s:if>
              <s:if test="farmerCodeEnabled==1">
			 '<s:property value="%{getLocaleProperty('farmerCode')}" />',
			 </s:if> 
			 
			 <s:if test="currentTenantId!='pratibha' && currentTenantId!='ocp'">
			 <s:if test="currentTenantId!='symrise'">
			 '<s:property value="%{getLocaleProperty('cSeasonCode')}" />',
			 </s:if>
			 </s:if>
			 <s:if test="currentTenantId=='avt'">
			 '<s:property value="%{getLocaleProperty('createdUsername')}" />',
			 </s:if>
			 //'<s:property value="%{getLocaleProperty('farmer.farmerCode')}"/>',
			
			 '<s:property value="%{getLocaleProperty('farmer.firstName')}"/>',
			// '<s:property value="%{getLocaleProperty('fatherName')}"/>',
			 <s:if test="currentTenantId!='gsma'">
			'<s:text name="gender"/>',
			</s:if>
			 <s:if test="currentTenantId!='wilmar' && currentTenantId!='gsma' ">
				 '<s:text name="age"/>',
				</s:if>
				 <s:if test="currentTenantId=='welspun'">
				 '<s:property value="%{getLocaleProperty('mobileNo')}"/>',
				 '<s:property value="%{getLocaleProperty('createdUsername')}"/>',
				 '<s:text name="%{getLocaleProperty('mappedAgentBySamithi')}" />',
				 </s:if>	
			  <s:if test="currentTenantId=='ocp'">
			  '<s:property value="%{getLocaleProperty('farmer.dateOfJoin')}"/>',
			  '<s:property value="%{getLocaleProperty('createdUsername')}"/>',
			  '<s:property value="%{getLocaleProperty('group')}"/>',
			  </s:if>	 
			  
			 <s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
				 '<s:text name="icsName"/>',
				 '<s:text name="icsCode"/>',
			 </s:if>
				 <s:if test="currentTenantId=='pratibha'">
					<s:if test="getBranchId()!='bci'">
						'<s:text name="%{getLocaleProperty('tracenetcode')}"/>',
					</s:if>
				</s:if>
				 
			 <s:if test="currentTenantId=='chetna'">
			 '<s:text name="icsName"/>',
			 '<s:property value="%{getLocaleProperty('farmer.cooperative')}" />',
		     </s:if>
			 <s:if test="currentTenantId=='susagri'">
			 '<s:property value="%{getLocaleProperty('group')}" />',
			</s:if>
			 '<s:property value="%{getLocaleProperty('village.name')}" />',
			 
				 '<s:property value="%{getLocaleProperty('city.name')}" />',
	             '<s:property value="%{getLocaleProperty('locality.name')}" />',
	             '<s:property value="%{getLocaleProperty('state.name')}" />',
             
	             /* <s:if test="currentTenantId!='chetna'">
             <s:if test="currentTenantId!='atma'">  
             '<s:text name="farm"/>', */
             /* <s:if test="currentTenantId!='chetna' && currentTenantId!='pratibha'">
             '<s:text name="farmCode"/>',
             </s:if> */
             
             /* '<s:property value="%{getLocaleProperty('totalLandHolding')}" /> (<s:property value="%{getAreaType().toUpperCase()}" />)',	          
			  '<s:property value="%{getLocaleProperty('proposedPlanting')}" /> (<s:property value="%{getAreaType().toUpperCase()}" />)', */
			  /* <s:if test="currentTenantId!='blri'"> */
				/* <s:if test="currentTenantId!='nei'">
			  '<s:property value="%{getLocaleProperty('farmOwned')}" />',
			  </s:if>
			  	  '<s:text name="cropCategory"/>',
				  '<s:text name="cropNames"/>', */
				 
				  
				/*   <s:if test='enableMultiProduct==0'>
				  '<s:property value="%{getLocaleProperty('expectedLintYield')}" />',
	              </s:if> */
	            	/*  <s:if test ="currentTenantId!='indev' && currentTenantId!='fincocoa' && currentTenantId=='chetna'">
				  '<s:property value="%{getLocaleProperty('estimatedYield')}" />'
				  
				  </s:if> */
				  /* </s:if> */
           /*   </s:if>
             </s:if> */
			 
			
			 /*  <s:if test="currentTenantId=='wilmar'">
			 '<s:property value="%{getLocaleProperty('farmer.status')}" />',
			 /* '<s:property value="%{getLocaleProperty('farm.totalLand')}" />',
			 '<s:property value="%{getLocaleProperty('organicStatus')}" />'
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
				   			<s:if test="currentTenantId =='ocp'">
				   			{name:'farmerId',index:'farmerId',sortable:false,search:true,width:225},  
				   			</s:if>  
				   			 <s:if test="farmerCodeEnabled==1">
							{name:'farmerCode',index:'farmerCode',sortable:false},
							</s:if> 
							
					 <s:if test="currentTenantId!='pratibha'&& currentTenantId!='ocp'">
					 <s:if test="currentTenantId!='symrise'">
						{name:'cSeasonCode',index:'cSeasonCode',sortable:false,frozen:true,search:false},
						</s:if>	
					</s:if>
					
				 <s:if test="currentTenantId=='avt'">
				 {name:'createdUser',index:'createdUser',sortable:false,search:false,width:225},  
				 </s:if>
				//{name:'farmerCode',index:'farmerCode',sortable:false,search:true,width:225},
				{name:'firstName',index:'firstName',sortable:false,search:true,width:225},   
				//{name:'lastName',index:'lastName',width:130,sortable:false},
				<s:if test="currentTenantId!='gsma'">
					{name:'gender',index:'gender',sortable:false,search:false},</s:if>
					 <s:if test="currentTenantId!='wilmar' && currentTenantId!='gsma' ">
				{name:'age',index:'age',sortable:false,search:false, align:'right'},
				</s:if> 
				 <s:if test="currentTenantId=='welspun'">
				 {name:'mobileNo',index:'mobileNo',width:125,sortable:false,search:false},
				 {name:'createdUsername',index:'createdUsername',width:125,sortable:false,search:false},
				 {name:'mappedAgentBySamithi',index:'mappedAgentBySamithi',width:125,sortable:true,search:false},				 
				 </s:if>	 
				  <s:if test="currentTenantId=='ocp'">
				  {name:'createdDate',index:'createdDate',width:125,sortable:false,search:false},
			   		{name:'createdUsername',index:'createdUsername',width:125,sortable:false,search:false},
					{name:'group',index:'group',sortable:false,search:false},
				  </s:if>	 
				<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci' ">
				{name:'icsName',index:'icsName',sortable:false,search:false},
				{name:'icsCode',index:'icsCode',sortable:false,search:false},
				</s:if>
				<s:if test="currentTenantId=='pratibha'">
				<s:if test="getBranchId()!='bci'">
					{name:'tracenetcode',index:'tracenetcode',sortable:false,search:false},
				</s:if>
			   </s:if>
			
				<s:if test="currentTenantId=='chetna'">
				{name:'icsName',index:'icsName',sortable:false,search:false},
				{name:'samithi',index:'samithi',sortable:false,search:false},
				/* {name:'latitude',index:'latitude',sortable:false},
				{name:'longitude',index:'longitude',sortable:false}, */
			     </s:if>
				 <s:if test="currentTenantId=='susagri'">
					{name:'group',index:'group',sortable:false,search:false},
				</s:if>
				{name:'village',index:'village',sortable:false,search:false},
			 
					{name:'city',index:'city',width:130,sortable:false,search:false},
					{name:'locality',index:'locality',width:130,sortable:false,search:false},
					{name:'state',index:'state',width:130,sortable:false,search:false},
				
					/* <s:if test="currentTenantId!='chetna'">
				 <s:if test="currentTenantId!='atma'">    	
				{name:'farmName',index:'farmName',sortable:false}, */
				/* <s:if test="currentTenantId!='chetna' && currentTenantId!='pratibha'">
				{name:'farmCode',index:'farmCode',sortable:false},
				</s:if> */
				
				/* {name:'totalLandHolding',index:'totalLandHolding',sortable:false},
				{name:'proposedPlantingAreas',index:'proposedPlantingAreas',sortable:false}, */
				/* <s:if test="currentTenantId!='blri'">
				
				{name:'farmOwned',index:'farmOwned',sortable:false},
			
				{name:'cropCategory',index:'cropCategory',sortable:false},
					{name:'cropNames',index:'cropNames',sortable:false},
					
					
					
					 <s:if test='enableMultiProduct==0'>
						{name:'expectedLintYield',index:'expectedLintYield',sortable:false},
					</s:if>
						<s:if test =" currentTenantId!='indev' && currentTenantId!='fincocoa' && currentTenantId=='chetna'">
					{name:'estimatedYield',index:'estimatedYield',sortable:false},
					</s:if>
					 </s:if>
				</s:if>
				</s:if> */
			 	 
				/* <s:if test="currentTenantId=='wilmar'">
				 {name:'status',index:'status',sortable:false,search:false},
			  {name:'totalLand',index:'totalLand',align:'right',sortable:false,search:false},
				 {name:'conventionalStatus',index:'conventionalStatus',align:'right',sortable:false,search:false} 
				 </s:if> */
			],
			  height: 380, 
			   width: $("#baseDiv").width(),
			   //autowidth:true,
			   shrinkToFit:true,			   
			   scrollOffset: 19,     
			   sortname:'id',	
			   sortorder: "asc",
			   rowNum:10,
			   rowList : [10,25,50],
			   viewrecords: true, 
			   <s:if test="currentTenantId=='wilmar'">
			   onSelectRow: function(id){ 
			    	
				   document.updateform.id.value  =id;
						  postDataSubmit();
			          document.updateform.submit();   
				},

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
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>"); //<div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid({
						   url:'farmerReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",
						   	mtype: 'POST',				   	
						   	postData:{	
						   		"organicStatus" : function(){			  
				 				      return document.getElementById("organicStatus").value;
				 				          },
						   	},
						   	colNames:[	
									'<s:text name="%{getLocaleProperty('farm.code')}"/>',
									'<s:text name="%{getLocaleProperty('farmName')}"/>',
									'<s:text name="%{getLocaleProperty('organicStatus')}"/>',
									'<s:text name="%{getLocaleProperty('dynamicCertification.icsType')}"/>',
									'<s:text name="%{getLocaleProperty('tot.landHold')}"/>',
									'<s:text name="%{getLocaleProperty('crops')}"/>'
                                    ],				 	      	 
						   	colModel:[
                                   
									{name:'frmcode',index:'frmcode',sortable:false},
									{name:'frmName',index:'frmName',sortable:false},
									{name:'orgStatus',index:'orgStatus',sortable:false},
									{name:'icsType',index:'icsType',sortable:false},
									{name:'totLandHold',index:'totLandHold',sortable:false},
									{name:'crops',index:'crops',sortable:false}
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
							shrinkToFit: true,
							autowidth: true,
						    viewrecords: true
					   });
					    jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				},
				</s:if>
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
			
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false});
			jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
}

function exportXLS(){
	
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerReport_populateXLS.action'});
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
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerReport_populatePDF.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}
function exportAFL(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerReport_populateAFLExport.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}
function reloadGrid(flag){
	

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
	}
 
	jQuery("#detail").jqGrid('setGridParam',{url:"farmerReport_data.action?",page:1,mtype: 'POST',
	   	postData:{	
				 /* "farmerCode" : function(){			  
			      return document.getElementById("farmerCode").value;
			          }, */
			          
	   	  "startDate" : function(){
              return document.getElementById("hiddenStartDate").value;           
             },		
       "endDate" : function(){
          return document.getElementById("hiddenEndDate").value;
           }, 

			       /* "firstName" : function(){			  
					  return document.getElementById("firstName").value;
	 			      }, */
	 			    /*  "lastName" : function(){			  
						  return document.getElementById("lastName").value;
		 			      },  */
	 			    "villageName" : function(){			  
	 					  return document.getElementById("villageName").value;
	 	 			      },
	 	 			<s:if test="currentTenantId!='pratibha'&& currentTenantId!='wilmar'&&currentTenantId!='efk'">
	 	 			    <s:if test="currentTenantId!='chetna' && currentTenantId!='welspun'">
	 	 			 "icsType" : function(){
							return  document.getElementById('icsType').value;
							},
							</s:if>
							<s:if test="currentTenantId=='gar'">
					  "icsName" : function(){
							return  document.getElementById('icsName').value;
							},
							</s:if>
							  <s:if test="currentTenantId!='gsma'">
					  "gender" : function(){
				      return  document.getElementById('gender').value;
						    },
						    </s:if>
					</s:if>
						    <s:if test="&& currentTenantId!='chetna' && &&currentTenantId!='ocp'">"
						"season" : function(){
						return  document.getElementById('season').value;
						},
						</s:if>
						<s:if test="&& currentTenantId=='efk'">"
						"season" : function(){
						return  document.getElementById('season').value;
						},
						</s:if>
	 	 			   /*  <s:if test='branchId==null'>  
	 			     "branchIdParma" : function(){			  
	 				      return document.getElementById("branchIdParam").value;
	 				          },
	 	 			    </s:if> */
	 	 			  <s:if test=" currentTenantId=='wilmar'">
	 	 			"organicStatus" : function(){			  
	 				      return document.getElementById("organicStatus").value;
	 				          },
	 	 			  </s:if>
	 				         <s:if test=" currentTenantId=='avt' || currentTenantId=='susagri' ">
	 		 	 			"samithiName" : function(){			  
	 		 				      return document.getElementById("samithiName").value;
	 		 				          },
	 		 	 			  </s:if>
	 			      }}).trigger('reloadGrid');
	
	}
</script>

	<s:form name="form" action="farmerReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">

					
					
					
				<%-- 	
					<div class='filterEle'>
						<label for="txt"><s:text name="farmerName" /></label>
						<div class="form-element">
							<s:select name="filter1.farm.farmer.firstName" id="firstName"
								list="farmerNameList" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					 --%>
					
					
					<%-- <div class="reportFilterItem">
					<label for="txt"><s:property
						value="%{getLocaleProperty('fatherName')}" /></label>
					<div class="form-element">
						<s:select name="lastName" id="lastName" list="fatherNameList"
					headerKey="" headerValue="%{getText('txt.select')}"
					class="form-control input-sm select2" />
					</div>
				</div> --%>
					 <%-- <s:if test="currentTenantId =='ocp' "> 
					<div class="filterEle">
					<label for="txt"><s:property
									value="%{getLocaleProperty('farmer.dateOfJoin')}" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
					class="form-control input-sm " />
					</div>
				</div>
					 </s:if> --%>
					 <s:if test="currentTenantId=='livelihood'">
					 <div class="filterEle hide">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.dateOfJoin')}" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm " />
						</div>
					</div> 
					</s:if>
					<s:else>
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.dateOfJoin')}" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm " />
						</div>
					</div>
					</s:else> 
					
					
					<div class="filterEle iccoaHide">
						<label for="txt"><s:property
									value="%{getLocaleProperty('villageName')}" /></label>
						<div class="form-element">
							<s:select name="filter1.farm.farmer.villageName" id="villageName"
								list="villageMap" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="input-sm form-control select2" />
						</div>
					</div>
					<s:if test="currentTenantId!='pratibha' && getBranchId()!='bci' && currentTenantId!='efk' && currentTenantId!='gar' && currentTenantId!='agro' && && currentTenantId!='symrise' ">
						<%-- <s:if
							test="currentTenantId!='pgss'  && currentTenantId!='wilmar' && currentTenantId!='olivado'">
							<div class="filterEle iccoaHide">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParam" id="branchIdParam"
										list="branchesMap" headerKey="" headerValue="Select"
										cssClass="input-sm form-control select2" />
								</div>
							</div>
						</s:if> --%>
					</s:if>

					<s:if test="currentTenantId!='pratibha'">
						<div class="filterEle iccoaHide">
							<label for="txt"><s:property
									value="%{getLocaleProperty('stateName')}" /></label>
							<div class="form-element">
								<s:select name="stateName" id="stateName" list="stateList"
									headerKey="" headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" />
							</div>
						</div>
						
						<s:if test="currentTenantId!='pgss'  && currentTenantId!='wilmar'&& currentTenantId!='efk' && currentTenantId!='gar' && 
						 currentTenantId!='symrise' && currentTenantId!='olivado' && currentTenantId!='ecoagri'&& currentTenantId!='ocp'">
							<div class="filterEle icsNameClass">
								<label for="txt"><s:text name="icsName" /></label>
								<div class="form-element">
									<s:select name="filter1.farm.farmer.icsName" id="icsName"
										list="icsNameList" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>
							</div>
								
				<s:if test="currentTenantId!='welspun'">
							<div class="filterEle iccoaShow">
								<label for="txt"><s:text name="insYear" /></label>
								<div class="form-element">
									<s:select list="insYearList" name="insYear" id="insYear"
										headerKey="" headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>

							</div>
							</s:if>
						</s:if>
						<s:if test="currentTenantId=='iccoa'">
							<div class="filterEle iccoaShow">
								<label for="txt"><s:text name="insYear" /></label>
								<div class="form-element">
									<s:select list="insYearList" name="insYear" id="insYear"
										headerKey="" headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>

							</div>
							</s:if>
						
						<s:if test="currentTenantId=='gar'">
						    <div class="filterEle">
						         <label for="txt"><s:text name="icsName" /></label>
						      <div class="form-element">
							       <s:textfield id="icsName" name="farmer.icsName" theme="simple"
								cssClass="upercls form-control" list="{}"
									headerKey=""    listKey="key" listValue="value" />
						     </div>	
						   </div>
						</s:if>
						
					 <%-- 	<s:if
							test="currentTenantId!='chetna' && currentTenantId!='pgss' && currentTenantId!='wilmar'&& currentTenantId!='efk' && currentTenantId!='welspun'"> --%>
							<div class="filterEle icsTypeHide">
								<label for="txt"><s:text name="icsType" /></label>
								<div class="form-element">
									<s:select name="filter1.farm.farmer.icsType" id="icsType"
										list="icsTypeList" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>
							</div>
						<%-- </s:if>  --%>

					</s:if>



					<%-- <div class="filterEle">
					<label for="txt"><s:text name="farmerCode" /></label>
					<div class="form-element">
						<s:select name="filter1.farm.farmer.farmerCode" id="farmerCode" list="farmerCodeList"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div> --%>
<s:if test="currentTenantId!='wilmar' && currentTenantId!='gsma'">
					<div class="filterEle iccoaHide">
						<label for="txt"><s:text name="gender" /></label>
						<div class="form-element">
							<s:select name="filter1.farm.farmer.gender" id="gender"
								list="genderList" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="input-sm form-control select2" />
						</div>
					</div></s:if>
					<s:if test="currentTenantId!='chetna' && currentTenantId!='ocp' && currentTenantId!='symrise'">
						<div class="filterEle iccoaHide susAgriHide">
							<label for="txt"><s:property
									value="%{getLocaleProperty('cSeasonCode')}" /></label>
							<div class="form-element">
								<s:select name="filter1.cropSeason.code" id="season"
									list="seasonList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="input-sm form-control select2" />
							</div>
						</div>
					</s:if>
					<s:if test="currentTenantId=='wilmar'">
					<div class="filterEle">
						         <label for="txt"><s:text name="%{getLocaleProperty('organicStatus')}" /></label>
						      <div class="form-element">
							      <s:select name="selectedOrganicStatus" id="organicStatus"
										list="organicStatusList" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
						     </div>	
						   </div>
					</s:if>

					<s:if test="currentTenantId=='livelihood'">
						<div class="filterEle iccoaHide susAgriHide hide">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farmer.cooperative')}" /></label>
							<div class="form-element">
								<s:select name="fpo" id="fpo" list="warehouseList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="input-sm form-control select2" />
							</div>
						</div>
					</s:if>
					<s:else>
					<s:if test="currentTenantId!='pgss'&& currentTenantId!='gsma' && currentTenantId!='pratibha'&& currentTenantId!='efk' && currentTenantId!='gar' && currentTenantId!='agro'
					 && currentTenantId!='worldvision' && currentTenantId!='wilmar' && currentTenantId!='ocp' && currentTenantId!='welspun'&& currentTenantId!='symrise' && currentTenantId!='awi'
					  && currentTenantId!='avt' && currentTenantId!='kenyafpo'">
							<div class="filterEle iccoaHide susAgriHide">
								<label for="txt"><s:property
										value="%{getLocaleProperty('farmer.cooperative')}" /></label>
								<div class="form-element">
									<s:select name="fpo" id="fpo" list="warehouseList" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>
							</div>
						</s:if>
						</s:else>						   
						
						<s:if test="currentTenantId=='avt' || currentTenantId=='susagri' ">
						
							<div class="filterEle">
								<label for="txt"><s:property
										value="%{getLocaleProperty('group')}" /></label>
								<div class="form-element">
									<s:select name="filter1.farm.farmer.samithiName" id="samithiName"
										list="samithiList" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>
							</div>
				
							<div class="filterEle susAgriHide">
								<label for="txt"><s:text name="createdUser" /></label>
								<div class="form-element">
									<s:select list="createdUserList" name="createdUser" id="createdUser"
										headerKey="" headerValue="%{getText('txt.select')}"
										cssClass="input-sm form-control select2" />
								</div>

							</div> 
						</s:if>
					
				<%-- 	<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle iccoaHide">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParam" id="branchIdParam"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="select2" onchange="populateChildBranch(this.value)" />

								</div>
							</div>
						</s:if>
						<div class="filterEle iccoaHide">
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
							<div class="filterEle iccoaHide">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParam" id="branchIdParam"
										list="branchesMap" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
					</s:else> --%>

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
						<span><span class="strong">0</span> Total Estimated
							Yield&nbsp;<i class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><i class='fa fa-pagelines'><s:text name='ct0' /></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span> <i class='fa fa fa-leaf' aria-hidden='true'><s:text
									name='ct1' /></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><i class='fa fa-asterisk' aria-hidden='true'><s:text
									name='ct2' /></i></span>
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
					<s:if test="currentTenantId!='iccoa' && currentTenantId!='susagri'">
						<li role="presentation"><a href="#" onclick="exportXLS()"
							tabindex="-1" role="menuitem"> <s:text name="excel" />
						</a></li>
						<s:if test="currentTenantId!='chetna' && currentTenantId!='ocp'">
							<li role="presentation"><a href="#" onclick="exportPDF();"
								tabindex="-1" role="menuitem"> <s:text name="pdf" />
							</a></li>
						</s:if>
						</s:if>
<s:else>
	<li role="presentation"><a href="#" onclick="exportAFL();"
		tabindex="-1" role="menuitem"> <s:text name="afl" />
	</a></li>
</s:else>
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

	<s:form name="detailForm" action="cultivationReport_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
		<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
	</s:form>
	
	
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
	
	<s:form name="updateform" action="farmerReport_populateFarmerProfileExport">
		<s:hidden name="id" />
		<s:hidden name="postdata" id="postdata" />
		  <s:hidden name="type" class="type"/>  
		<s:hidden name="currentPage" />
	</s:form>
	
	
</body>