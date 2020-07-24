<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
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

	<script>
	
		$(document).ready(function() {
			
			
			loadGrid();
			document.getElementById("sDate").value='<s:property value="currentDate" />';
			document.getElementById("eDate").value='<s:property value="currentDate" />';
		});
		
function loadGrid(){
			
			jQuery("#detail").jqGrid(
					{
					   	url:'yieldEstimationReport_data.action',
					   	pager: '#pagerForDetail',	  
					   	datatype: "json",	
					   	mtype: 'POST',
					   	styleUI : 'Bootstrap',
						postData:{
							
							"startDate" : function(){
			                     return document.getElementById("sDate").value;           
			                    },		
			                    "endDate" : function(){
					               return  document.getElementById("eDate").value;
				                 },
				                 
				                 "filter.cropSeason.code":function(){
						               return document.getElementById("seasonId").value;
					                 },
				                
						
							"varietyCode" : function(){			  
								return  document.getElementById("cropProduct").value;
							  },
						},
						colNames:[
									<s:if test="farmerCodeEnabled==1">
									  '<s:text name="farmerCode"/>',
									 </s:if>
						  		   	  '<s:text name="farmerName"/>',
						  		    '<s:property value="%{getLocaleProperty('samithi')}" />',
						  		      '<s:text name="village"/>',
						  		    <s:if test="currentTenantId=='chetna'">
						 			 '<s:text name="icsName"/>',
						 			 </s:if>
						  		      '<s:text name="crop"/>',
						  		      '<s:text name="totalArea"/>',
						  		      '<s:text name="estimatedYield"/>',
						  		    '<s:text name="season"/>'
						  		  //'<s:text name="latitude"/>',
						  		 // '<s:text name="longitude"/>'
				 	      	 ],
						   	colModel:[
								<s:if test="farmerCodeEnabled==1">  
						   	        {name:'co.code',index:'co.code',width:133,sortable:false},
						   	    </s:if>
								    {name:'farmerName',index:'farmerName',width:133,sortable:false},
									{name:'samithi',index:'samithi',width:133,sortable:false},
									{name:'village',index:'village',width:133,sortable:false},
									<s:if test="currentTenantId=='chetna'">
									{name:'icsName',index:'icsName',width:200,sortable:false},
								     </s:if>
									{name:'cropId',index:'cropId',width:133,sortable:false},
									{name:'totalArea',index:'totalArea',width:133,sortable:false,align:'left'},
									{name:'estimatedYield',index:'estimatedYield',width:133,sortable:false,align:'left'},
									{name:'season',index:'season',width:133,sortable:false,align:'left'}
									//{name:'latitude',index:'latitude',width:133,sortable:false,align:'left'},
									//{name:'longitude',index:'longitude',width:133,sortable:false,align:'left'}
								  
						   	],
						   height: 301, 
						   width: $("#baseDiv").width(),
						   autowidth:true,
						   shrinkToFit:false,
						   scrollOffset: 0,     
						   sortname:'id',
						   sortorder: "desc",
						   rowNum:10,
						   rowList : [10,25,50],
						   viewrecords: true,  	
						   subGrid: true,
						   subGridOptions: {
						   "plusicon" : "glyphicon-plus-sign",
						   "minusicon" : "glyphicon-minus-sign",
						   "openicon" : "glyphicon-hand-right",
						   },
						   loadComplete: function() {
							   	 var count=jQuery("#detail").jqGrid('getGridParam', 'records');
							   	 if(parseInt(count)>0){
							   		jQuery("#divHide").show();
							   		var variety = $("#cropProduct").val();							   		
							   		var seasonCode = $("#seasonId").val();
							   		$.post("yieldEstimationReport_populateTotalValues",{varietyCode:variety,seasonCode:seasonCode},function(data){
							   			if(data!=null && data!=""){
							   				var valuesArr = $.parseJSON(data);
											$.each(valuesArr, function(index, value) {
												jQuery('#farmerCount').text(value.fCount);
												jQuery('#tArea').text(value.tArea);
												jQuery('#tYield').text(value.tYield);
											});
							   			}
							   		});
							   	
							   	 }else{
							   		jQuery("#divHide").hide();
							   	 }
							},
						   subGridRowExpanded: function(subgrid_id, row_id){
							   
							   var subgrid_table_id, pager_id; 
							   subgrid_table_id = subgrid_id+"_t"; 
							   pager_id = "p_"+subgrid_table_id; 
							   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
							   //var ret = jQuery("#detail").jqGrid('getRowData',row_id);
							   var arrayVal = row_id.split("_"); 
							   
							   var coOperativeId ='';
							   var procurementProductId = '';
							   var farmerId = '';

							   if(arrayVal.length>0){
								   coOperativeId = arrayVal[0];
								   procurementProductId = arrayVal[1];
								   farmerId = arrayVal[2];
							   }
							  
							   jQuery("#"+subgrid_table_id).jqGrid(
							   {
									url:'yieldEstimationReport_subGridDetail.action?id='+row_id,
								   	pager: pager_id,
								   	datatype: "json",
									mtype: 'POST',				   	
								   	postData:{				 
										  
									},	
								   	colNames:[	
								   	       	  '<s:text name="farmName"/>',	
								   	       	  '<s:text name="variety"/>',
								  		      '<s:text name="cultivationArea"/>',						  		      
								  		      '<s:text name="Date"/>'
						 	      	 ],
								   	colModel:[	
											{name:'farmName',index:'farmName',width:150,sortable:false},
											{name:'variety',index:'variety',width:150,sortable:false},
				   							{name:'cultivationArea',index:'cultivationArea',width:150,sortable:false,align:'right'},
				   							{name:'Date',index:'Date',width:150,sortable:false,align:'right'}
								   	],
								   	scrollOffset: 0, 
								    sortname:'id',
								    height: '100%', 
								    sortorder: "desc",
								    autowidth: true,
								    rowNum:10,
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
						},		 // for viewing noofrecords displaying string at the right side of the table
				        onSortCol: function (index, idxcol, sortorder) {
					        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				        }
					});
			
			jQuery("#generate").click( function()
					{
					
					if(document.getElementById("cropProduct").value=="" && document.getElementById("seasonId").value=="" )
					{
						document.getElementById("validateError").innerHTML='<s:text name="empty.records"/>';
					}
					else
					{
						document.getElementById("validateError").innerHTML="";
						 jQuery("#detail").jqGrid('setGridParam',{url:"yieldEstimationReport_data.action?",page:1}).trigger('reloadGrid');
						 
					}
			});
			
			jQuery("#clear").click( function() {
				clear();
			});
			
			function clear(){
				$("#seasonId").val("");
				$("#cropProduct").val("");
				document.form.submit();
				
			}
					
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
	
			
		}
		
	</script>
	<s:form name="form" action="pesticideUsageReport_list">
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
	           
	           <div class="reportFilterItem">
					<label for="daterange"><s:text name="startDate" /></label>
					<div class="form-element">
						<s:textfield id="sDate" data-provide="datepicker"
					data-date-start-date="0d" readonly="true" data-date-format="dd/mm/yyyy"
					cssClass="col-input-sm form-control"/>
					</div>
				</div>
				  <div class="reportFilterItem">
					<label for="daterange"><s:text name="endingDate" /></label> 
					<div class="form-element">
						<s:textfield id="eDate" data-provide="datepicker" data-date-format="dd/mm/yyyy"
					data-date-start-date="0d" readonly="true"
					cssClass="col-input-sm form-control" />
					</div>
				</div>
				
				<div class="reportFilterItem">
				<label for="cropProduct" style="width: 140px;"> <s:text name="cropProduct" /></label>
					<div class="form-element">
						<s:select name="cropProduct" id="cropProduct" list="cropProductList" listKey="key" listValue="value"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.season"><s:text name="season" /></label>
					<div class="form-element">
						<s:select name="filter.cropSeason.code" id="seasonId" list="seasonList"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2"/>
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
			<div class="jqGridwrapper baseDiv">
				<table id="grid"></table>
				<div id="pager"></div>
			</div>
			<div style="width: 100%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>
		</div>
	</s:form>
</body>