<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
<style>
.ui-jqgrid .ui-jqgrid-htable th{
background: blue

</style>
</head>
<body>
	<script type="text/javascript">
var recordLimit='<s:property value="exportLimit"/>';
//var filterda='';
$(document).ready(function(){	
	var tenant='<s:property value="getCurrentTenantId()"/>';
	var userName='<s:property value="getUsername()"/>';
	populateMethod();
	var type= '<%=request.getParameter("type")%>';
	 if(type=='2'){
			$(".breadCrumbNavigation").html('');
			$(".breadCrumbNavigation").append("<li><a href='#'>Profile</a></li>");
			//$(".breadCrumbNavigation").append("<li><a href='#'>IRP</a></li>");
			$(".breadCrumbNavigation").append("<li><a href='farmer_list.action?type=2'>IRP</a></li>");
			$(".type").val(type);
		} 
	 
	jQuery("#detail").jqGrid(
	{
	   	url:'farmer_data.action',
	   	pager: '#pagerForDetail',
	    mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   	postData:{
	   		"type" : function(){	
	   			return  type;
			  } ,
			  "postdata" :  function(){	
	   			return  decodeURI(postdata);
			  } 
	   	},
		
	   	
	   	colNames:[	
					<s:if test='branchId==null'>
						 '<s:text name="app.branch"/>',
					</s:if>
						 
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
					</s:if>
					<s:if test="currentTenantId=='ocp'"> 
						'<s:text name="%{getLocaleProperty('farmerCode')}"/>',
					</s:if>
					<s:if test="farmerCodeEnabled==1">
						<s:if test="currentTenantId=='pratibha' && getBranchId()=='organic'">
							'<s:text name="%{getLocaleProperty('tracenet')}"/>',
						</s:if>
					 	<s:elseif test='type!="2"'>
					 		'<s:text name="%{getLocaleProperty('farmerCode')}"/>',
					 	</s:elseif>
					</s:if>
	  			/*  <s:if test="farmerCodeEnabled==1">
	  		   	  '<s:text name="%{getLocaleProperty('farmerCode')}"/>',
	  		   	  </s:if> */
	  		   	
	  		   		'<s:property value="%{getLocaleProperty('farmer.firstName')}" />',
	  		  	<s:if test="currentTenantId!='symrise' && currentTenantId!='gsma'  && currentTenantId!='ocp' && currentTenantId!='farmAgg' ">
	  		    '<s:property value="%{getLocaleProperty('farmer.lastName')}" />',
	  		    </s:if>
	  		   
	  		    <s:if test="currentTenantId=='kpf' || currentTenantId=='movcd'|| currentTenantId=='simfed'||currentTenantId=='wub'">
	  		    '<s:text name="farmer.surName"/>',
	  		      </s:if>
	  		  <s:if test="currentTenantId=='livelihood'"> 
             '<s:property value="%{getLocaleProperty('state.name')}" />',
             '<s:property value="%{getLocaleProperty('locality.name')}" />',
             '<s:property value="%{getLocaleProperty('city.name')}" />',
	  		  </s:if>
	  		  '<s:property value="%{getLocaleProperty('village.name')}" />',
	  		<s:if test="currentTenantId=='symrise'">
	  		//'<s:text name="farmer.certificateStandard.name"/>',
	  		'<s:text name="certification.level"/>',
	  		</s:if>
	  		 <s:if test="currentTenantId=='pratibha' && getBranchId()=='bci'">
	  		      '<s:property value="%{getLocaleProperty('profile.samithi.bci')}" />',
	  		      </s:if><s:else>
	  		    <s:if test="currentTenantId!='farmAgg'">
	  		    '<s:property value="%{getLocaleProperty('profile.samithi')}" />',
	  		  </s:if>
	  		      </s:else> 
	  		  
	  		    
	  			<s:if test="currentTenantId!='olivado' && currentTenantId!='ocp' && currentTenantId!='susagri' && currentTenantId!='livelihood' && currentTenantId!='kenyafpo' && currentTenantId!='farmAgg'"> 
	  			<s:if test="getIsCertifiedFarmerInfoEnabled()==1">
 		     	 '<s:text name="certified"/>',
 		      </s:if>  
	  			<s:if test="getFpoEnabled()==1">
		  		  '<s:property value="%{getLocaleProperty('fpoGroup')}" />',
	  		    </s:if>
		  		</s:if> 	 
	  		  <s:if test="currentTenantId=='avt'">
	  		'<s:property value="%{getLocaleProperty('createdUsername')}" />',
	  			</s:if>
	  		     	<s:if test="currentTenantId=='agro' || currentTenantId=='avt' ">
			   		'<s:property value="%{getLocaleProperty('farm.totalLand')}" />',
			   		</s:if>
				   	  <s:if test="currentTenantId=='wilmar'"> 
					   	'<s:property value="%{getLocaleProperty('selectedMasterData')}" />',
					  	'<s:property value="%{getLocaleProperty('dateOfJoining')}" />',
					   </s:if>
					  	 <s:if test="currentTenantId=='ocp'">
					  	'<s:property value="%{getLocaleProperty('farm.totalLandd')}" />',
					  	'<s:property value="%{getLocaleProperty('farmer.dateOfJoin')}" />',
			  		      '<s:text name="%{getLocaleProperty('createdUsername')}" />',
			  		    </s:if>
			     <s:if test="currentTenantId=='griffith' || currentTenantId=='olivado'  "> 
				 '<s:property value="%{getLocaleProperty('farmer.dateOfJoin')}" />',
				 '<s:text name="%{getLocaleProperty('createdUsername')}" />',
				 </s:if>
				 <s:if test="currentTenantId=='welspun'"> 
				 '<s:text name="%{getLocaleProperty('Enrollment Date')}" />',
				 '<s:text name="%{getLocaleProperty('createdUsername')}" />',
				 '<s:text name="%{getLocaleProperty('mappedAgentBySamithi')}" />',
				 '<s:text name="%{getLocaleProperty('noOfFarms')}" />',
				 '<s:text name="%{getLocaleProperty('farm.proposedPlantingAreas')}" />',
				 </s:if>
	  		    <s:if test="currentTenantId!='blri'"> 
	  		      '<s:text name="account.status"/>',
	  		      </s:if>
	  		    <s:if test="currentTenantId=='olivado'"> 
	  		      '<s:text name="totalAcreage"/>',
	  		      </s:if>
	  		    <s:if test="currentTenantId=='ocp'">
	  		      '<s:text name="%{getLocaleProperty('crops')}" />',
			      '<s:text name="%{getLocaleProperty('cropyield')}" />',	  		           
	  		    </s:if>
	  		    <s:if test="currentTenantId=='livelihood'"> 
	  		  '<s:property value="%{getLocaleProperty('farmer.dateOfJoin')}" />',
	  		'<s:text name="%{getLocaleProperty('createdUsername')}" />',
	  		'<s:text name="%{getLocaleProperty('Updated Date')}" />',
	  		'<s:text name="%{getLocaleProperty('Updated UserName')}" />',
	  			 '<s:text name="Print ID Card"/>'
	  			 </s:if>
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
	        }]
	
	}},	   				   		
</s:if>
<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
</s:if>
			   			<s:if test="currentTenantId=='ocp'"> 
			   			{name:'farmerId',index:'farmerId',width:125,sortable:true},
			   		</s:if>
			   		 <s:if test="farmerCodeEnabled==1">
						<s:if test="currentTenantId=='pratibha' && getBranchId()=='organic'">
						{name:'farmersCodeTracenet',index:'farmersCodeTracenet',width:125,sortable:true},
						</s:if>
						<s:elseif test='type!="2"'>
						{name:'farmerCode',index:'farmerCode',width:200,sortable:true},
						 </s:elseif>
						
					</s:if>
				
	       	/* <s:if test='farmerCodeEnabled==1'>
	   		{name:'farmerCode',index:'farmerCode',width:125,sortable:true},
	   		</s:if> */
	   	  
	   		{name:'firstName',index:'firstName',width:125,sortable:true},
	   	  <s:if test="currentTenantId!='symrise' && currentTenantId!='gsma'  && currentTenantId!='farmAgg'  && currentTenantId!='ocp'">
	   		{name:'lastName',index:'lastName',width:125,sortable:true},
	   		</s:if>
	   		
	   	  	<s:if test="currentTenantId=='kpf'|| currentTenantId=='movcd'|| currentTenantId=='simfed'||currentTenantId=='wub'">
	   		{name:'surName',index:'surName',width:125,sortable:true},
	   		</s:if>
	   	  <s:if test="currentTenantId=='livelihood'">
	   		{name:'state.name',index:'state.name',width:130,sortable:true},
	   		{name:'locality.name',index:'locality.name',width:130,sortable:true},
	   		{name:'city.name',index:'city.name',width:130,sortable:true},
			</s:if>
	   		{name:'v.name',index:'v.name',width:125,sortable:true},	   	
	   		<s:if test="currentTenantId=='symrise'">
	   		//{name:'cs.name',index:'cs.name',width:125,sortable:false, id:"certificateStandardCtrl", search:true, stype: 'select', searchoptions: { value: '<s:property value="certificateStandardFilterText"/>' }},
	   		{name:'certificationLevel',index:'certificationLevel',width:125,sortable:false, id:"certificationLevelCtrl", search:true, stype: 'select', searchoptions: { value: '<s:property value="certificationLevelsFilterText"/>' }},
	  		</s:if>
	    	 <s:if test="currentTenantId!='farmAgg'">
	   		{name:'s.name',index:'s.name',width:125,sortable:true},	   
	   		</s:if>
	   		<%--{name:'cs.name',index:'cs.name',width:125,sortable:false, id:"certificateStandardCtrl", search:true, stype: 'select', searchoptions: { value: '<s:property value="certificateStandardFilterText"/>' }},
	   		{name:'certificationType',index:'certificationType',width:125,sortable:false, id:"certificationTypeCtrl", search:true, stype: 'select', searchoptions: { value: '<s:property value="certificationTypesFilterText"/>' }},--%>
	   		 <s:if test="currentTenantId!='olivado'  && currentTenantId!='ocp' && currentTenantId!='susagri' && currentTenantId!='farmAgg' && currentTenantId!='livelihood' && currentTenantId!='kenyafpo'">
	   		
	   			<s:if test="getIsCertifiedFarmerInfoEnabled()==1">
		   		{name:'isCertifiedFarmer',index:'isCertifiedFarmer',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="certifiedFarmerFilterText"/>' }},
		   		</s:if>
		   		<s:if test="getFpoEnabled()==1">
	   			{name:'fpo',index:'fpo',width:125,sortable:false,search:true,stype: 'select',searchoptions: { value: '<s:property value="fpoList"/>' }},
		    </s:if> 
		   	</s:if>	
		    <s:if test="currentTenantId=='avt'">
		    {name:'createdUsername',index:'createdUsername',width:125,sortable:false,search:false},
		    </s:if>
		   		<s:if test="currentTenantId=='agro' || currentTenantId=='ocp' || currentTenantId=='avt'">
		   		{name:'farmSize',index:'farmSize',width:125,sortable:false,search:true,stype: 'select',align:'right',searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="greaterthanone"/>;2:<s:text name="lessthanone"/>;3:<s:text name="NA"/>' }},
		   		</s:if>
				  <s:if test="currentTenantId=='wilmar'">
			   		{name:'masterData',index:'masterData',width:125,sortable: false, width :125},	   
			   		{name:'createdDate',index:'createdDate',width:125,sortable:true,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
			   		</s:if>
			   		<s:if test="currentTenantId=='ocp'">
			   		{name:'createdDate',index:'createdDate',width:125,sortable:true,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
			   		{name:'createdUsername',index:'createdUsername',width:125,sortable:true},
			    </s:if>
			 <s:if test="currentTenantId=='griffith' || currentTenantId=='olivado'">
			 {name:'createdDate',index:'createdDate',width:125,sortable:true,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
		   		{name:'createdUsername',index:'createdUsername',width:125,sortable:true},
			</s:if>
		   		<s:if test="currentTenantId=='welspun'">
		   		{name:'createdDate',index:'createdDate',width:125,sortable:true,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
		   		{name:'createdUsername',index:'createdUsername',width:125,sortable:true},
		   		{name:'mappedAgentBySamithi',index:'mappedAgentBySamithi',width:125,sortable:true,search:false},
		   		{name:'noOfFarms',index:'noOfFarms',width:125,sortable:true,search:false},
		   		{name:'farmSize',index:'farmSize',width:125,sortable:false,search:true},
			</s:if>
	   		<s:if test="currentTenantId!='blri'&& currentTenantId!='olivado' && currentTenantId!='livelihood'">
	   		{name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>' }},	   		
	   		</s:if>
	   		<s:if test="currentTenantId=='livelihood'">
	   			{name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="approve"/>;0:<s:text name="notApprove"/>' }},
	   		</s:if>
	   		<s:if test="currentTenantId=='olivado'">
	   		{name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>;3:<s:text name="status3"/>;4:<s:text name="status4"/>;5:<s:text name="status5"/>'}},
	   		{name:'totalAcreage',index:'totalAcreage',width:125,sortable:true,align:'right'} ,
	   		</s:if>
	   		<s:if test="currentTenantId=='ocp'">
	   			{name:'crop',index:'crop',width:125,sortable: false, width :125, search:true},
	   			{name:'cropYield',index:'cropYield',width:125,sortable:false,search:false} ,
		    </s:if>
		    <s:if test="currentTenantId=='livelihood'">
		    {name:'createdDate',index:'createdDate',width:125,sortable:true,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
	   		{name:'createdUsername',index:'createdUsername',width:125,sortable:true},</s:if>
		    <s:if test="currentTenantId=='livelihood'">
	   		{name:'lastUpdatedDate',index:'lastUpdatedDate',width:125,sortable:true,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
	   		{name:'lastUpdatedUsername',index:'lastUpdatedUsername',width:125,sortable:true},
		    {name:'printQR',index:'printQR',width:125,sortable:true},
		    </s:if>
	   		],
	   	height: 401, 
	    width: $("#baseDiv").width(), // assign parent div width
	    scrollOffset: 0,
	   	rowNum:25,
	   	rowList : [25,50,75],
	   shrinkToFit:true,
	    sortname:'id',			  
	    sortorder: "desc",
	    beforeSelectRow:
            function(rowid, e) {
            var iCol = jQuery.jgrid.getCellIndex(e.target);
            if (iCol >= 9){return false; }
            else{ return true; }
            },
	    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	    onSelectRow: function(id){ 
	    	document.updateform.id.value  =id;
				  postDataSubmit();
	          document.updateform.submit();   
		},		
        onSortCol: function (index, idxcol, sortorder) {
	        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                    && this.p.colModel[this.p.lastsort].sortable !== false) {
                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
            }
        }
       
    });
	   jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
		jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
		
			retainFields();     //For retaining filter fields
	
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
    $('#detail').jqGrid('setGridHeight',(windowHeight));
     postdata =  '';
    
	
});	      
function exportKML(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	 if(count>recordLimit){
	   alert('<s:text name="export.limited"/>');
	 }
	 else if(isDataAvailable("#detail")){
	  jQuery("#detail").setGridParam ({postData: {rows : 0}});
	  jQuery("#detail").jqGrid('excelExport', {url: 'farmer_populateKML.action'});
	 }else{
	      alert('<s:text name="export.nodata"/>');
	 }
	 
		
}
function exportXLS(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'farmer_populateXLS.action'});
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
		jQuery("#detail").jqGrid('excelExport', {url: 'farmer_populatePDF.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}


function populateQrCode() {

	var count=jQuery("#detail").jqGrid('getGridParam', 'records');

	 if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'farmer_populateQrCode.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}

	
}
function printQR(idd){
	jQuery("#detail").setGridParam ({postData: {rows : 0}});
	jQuery("#detail").jqGrid('excelExport', {url: "farmer_populateQRPdf.action?id="+idd});
	
}
function populateMethod() {
	jQuery.post("farmer_populateMethod.action", {},
			function(result) {
				var valuesArr = $.parseJSON(result);
				$.each(valuesArr, function(index, value) {
	
					jQuery('#farmerCount').text(value.farmerCount);
					jQuery('#farmerPercentage').text(
							value.farmerCountPercentage
									+ '<s:text name="last.month"/>');
					jQuery('#farmerStatus').addClass(
							value.farmerCountstauts);
					jQuery('#farmerPercentage').addClass(
							value.farmerText);
					
					jQuery('#farmCount').text(value.farmCount);
					jQuery('#farmPercentage').text(
							value.farmCountPercentage
									+ '<s:text name="last.month"/>');
					jQuery('#farmStatus').addClass(
							value.farmCountstauts);
					jQuery('#farmPercentage').addClass(
							value.farmText);
				
					
					jQuery('#farmCropCount').text(value.farmCropCount);
					jQuery('#farmCropPercentage').text(
							value.farmCropCountPercentage
									+ '<s:text name="last.month"/>');
					jQuery('#farmCropStatus').addClass(
							value.farmCropCountstauts);
					jQuery('#farmCropPercentage').addClass(
							value.farmCropText);
					
					
					
					jQuery('#cowCount').text(value.cowCount);
					jQuery('#cowPercentage').text(
							value.farmCropCountPercentage
									+ '<s:text name="last.month"/>');
					jQuery('#cowStatus').addClass(
							value.cowCountstauts);
					jQuery('#cowPercentage').addClass(
							value.cowText);

					
					
					jQuery('#farmLandCount').text(value.farmLandCount);
					jQuery('#farmLandPercentage').text(
							value.farmLandCountPercentage
									+ '<s:text name="last.month"/>');
					jQuery('#farmLandStatus').addClass(
							value.farmLandCountstauts);
					jQuery('#farmLandPercentage').addClass(
							value.farmLandText);

				});
			});
}


datePick = function(elem)
{	
   jQuery(elem).datepicker({
		   changeYear: true,
	       changeMonth: true,       
	       dateFormat: 'dd-mm-yy',
		   onSelect:function(dateText, inst){	   	 
		   		jQuery("#detail")[0].triggerToolbar();
		   }
	   }
)};

function getBranchIdDyn(){
	return null;
}
</script>

      
	<div>
		
			<sec:authorize ifAllGranted="profile.farmer.create">
			<input type="BUTTON" id="add" value="Add Farmer" onclick="document.createform.submit()" class="btn btn-success mb-2 float-right" />
			</sec:authorize>

			<%-- <div class="dropdown">
				<s:if
					test="currentTenantId!='symrise' && currentTenantId!='gsma' && currentTenantId!='ocp'  ">
					<button id="dLabel" class="btn btn-primary btn-sts" type="button"
						onclick="exportKML()">
						<i class="fa fa-share"></i>
						<s:text name="exportKML" />
					</button>
				</s:if>
				<button id="dLabel" class="btn btn-primary btn-sts" type="button"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<i class="fa fa-share"></i>
					<s:text name="export" />
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right"
					aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
					<li role="presentation"><a href="#" onclick="exportXLS()"
						tabindex="-1" role="menuitem"> <i
								class="fa fa-table"></i> <s:text name="excel" />
					</a></li>
					<s:if test="currentTenantId!='ocp'  ">					
					<li role="presentation"><a href="#" tabindex="-1"
						role="menuitem" onClick="exportPDF()"> <i
								class="fa fa-file-pdf-o"></i><s:text name="pdf" />
					
					</a></li>
					</s:if>
				</ul>
			</div> --%>
	

</div>
<div>
	 <div class="table-responsive mt-3"  id="baseDiv">
			<table class="table table-centered datatable dt-responsive nowrap " style="border-collapse: collapse; border-spacing: 0; width: 100%;" id="detail"></table>
			<div id="pagerForDetail"></div>
		</div> 
	</div>
	<s:form name="createform" action="farmer_create">
		<s:hidden name="type" class="type" />
	</s:form>
	<s:form name="updateform" action="farmer_detail">
		<s:hidden name="id" />
		<s:hidden name="postdata" id="postdata" />
		<s:hidden name="type" class="type" />
		<s:hidden name="currentPage" />
	</s:form>
	<s:form name="exportform">
	</s:form>
</body>
