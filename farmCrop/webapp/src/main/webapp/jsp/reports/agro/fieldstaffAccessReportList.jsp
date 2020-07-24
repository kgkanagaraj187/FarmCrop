<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
</head>
<body>

	<script>
	 var recordLimit='<s:property value="exportLimit"/>';
	$(document).ready(function(){
		loadGrid();
		onFilterData();
		jQuery(".well").hide();
		//jQuery(".breadcrumb").append("<li><a href='#'>Report</a></li><li><a href='fieldStaffAccessReport_list.action'>FieldStaff Access Report</a></li>");
		 $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.updateView();
	     $("#daterange").data().daterangepicker.updateCalendars();
		 $('.applyBtn').click();
		 
			<s:if test='branchId==null'>
   	 		document.getElementById('branchIdParam').selectedIndex=0;
   			 </s:if>
		
		jQuery("#generate").click( function() {
			
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
				jQuery("#detail").jqGrid('setGridParam',{url:"fieldStaffAccessReport_detail.action?",page:1}).trigger('reloadGrid');
			}
		});
		
		
		function resetDate(){
			jQuery("#startDate").val(jQuery("#hiddenStartDate").val());
			jQuery("#endDate").val(jQuery("#hiddenEndDate").val());
		}
		
	jQuery("#clear").click( function() {
			clear();
		});
		
		jQuery("#minus").click( function() {
			clear();
		});
		jQuery("#plus").click( function() {
			showFields();
		});
	});
	
	function onFilterData(){
		callAjaxMethod("fieldStaffAccessReport_populateFieldStaffList.action","selectedFieldStafId");
	}
	function callAjaxMethod(url,name){
		var cat = $.ajax({
			url: url,
			async: true, 
			type: 'post',
			success: function(result) {
				insertOptions(name,JSON.parse(result));
			}        	

		})
		
	}
	
	
	function clear(){
		resetReportFilter();
		document.form.submit();
	}
		function loadGrid(){
			jQuery("#detail").jqGrid(
			{
			   	url:'fieldStaffAccessReport_detail.action',
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
			   		
			   		"selectedFieldStafId" : function(){			  
				      return document.getElementById("selectedFieldStafId").value;
 			     	},
	 			   /*  "selectedDevice" : function(){			  
					      return document.getElementById("selectedDevice").value;
	 			     }, */
		 			 <s:if test='branchId==null'>
	 	 			  "branchIdParam" : function(){
	 					  return document.getElementById("branchIdParam").value;
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
							'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						
							'<s:text name="app.subBranch"/>',    
						</s:if>
						'<s:text name="date"/>',  
						  //'<s:text name="deviceName"/>',
						  '<s:property value="%{getLocaleProperty('agentId')}"/>	',
						  '<s:property value="%{getLocaleProperty('agentName')}"/>	',
						  '<s:text name="%{getLocaleProperty('farmerEnroll')}"/>',
                        <s:if test="currentTenantId=='welspun'">
						  '<s:text name="%{getLocaleProperty('existingfarmerEnroll')}"/>',
						  '<s:text name="sowing"/>',
						  '<s:text name="%{getLocaleProperty('additionalFarmerInfo')}"/>',
						  </s:if>
						  <s:else>
						  '<s:text name="distribution"/>',
						
						  <s:if test="currentTenantId!='pratibha'">
						  '<s:text name="procurment"/>',
						  '<s:text name="payment"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
						  </s:if>
						  <s:else>
						  '<s:text name="Crop Harvest"/>',
						  '<s:text name="cropSale"/>',
						  </s:else>
						  '<s:text name="inspection"/>',
						  <s:if test="currentTenantId=='pratibha'">
						  '<s:text name="sowing"/>',
						  '<s:text name="geoTag"/>',
						  '<s:text name="TotLandArea"/>',
						  '<s:text name="%{getLocaleProperty('farmerFoto')}"/>',
						  </s:if>
						  </s:else>
						  <s:if test="currentTenantId=='griffith'">
						  '<s:text name="Crop Harvest"/>',
						  '<s:text name="cropSale"/>',
						  '<s:text name="%{getLocaleProperty('Land Preperation')}"/>',
						  '<s:text name="%{getLocaleProperty('Fertilizer Application')}"/>',
							'<s:text name="%{getLocaleProperty('Water Management')}"/>',
							'<s:text name="%{getLocaleProperty('Field Visits')}"/>',
							'<s:text name="%{getLocaleProperty('Intercultivation')}"/>',
							'<s:text name="%{getLocaleProperty('Weed Management')}"/>',
							'<s:text name="%{getLocaleProperty('Pesticide Management')}"/>',
							'<s:text name="%{getLocaleProperty('Training')}"/>',
							'<s:text name="%{getLocaleProperty('Yield Estimation')}"/>',
							'<s:text name="%{getLocaleProperty('Harvest')}"/>',
							'<s:text name="%{getLocaleProperty('Milestones')}"/>',
							'<s:text name="%{getLocaleProperty('Geotagging')}"/>',
							'<s:text name="%{getLocaleProperty('Physical Quality Check')}"/>',
							'<s:text name="%{getLocaleProperty('Land Holding Details')}"/>',
							'<s:text name="%{getLocaleProperty('Fertilizer Application')}"/>',
							'<s:text name="%{getLocaleProperty('Pesticide Management')}"/>',
							'<s:text name="%{getLocaleProperty('Harvest Details Turmeric')}"/>',
						  </s:if>
						  '<s:text name="status"/>',
						
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
			   		{name:'txnDate',index:'txnDate',width:100,sortable:false},
					//{name:'deviceName',index:'deviceName',width:250,sortable:false},
					{name:'agentId',index:'agentId',sortable:false},
					{name:'agentName',index:'agentName',sortable:false},
					{name:'farmerEnroll',index:'farmerEnroll',sortable:false,align:'right'},
					<s:if test="currentTenantId=='welspun'">
					{name:'existingfarmerEnroll',index:'existingfarmerEnroll',sortable:false},
					{name:'sowing',index:'sowing',sortable:false},
					{name:'additionalFarmerInfo',index:'additionalFarmerInfo',sortable:false},
					 
					  </s:if>
					  <s:else>
					{name:'distribution',index:'distribution',sortable:false,align:'right'},
					<s:if test="currentTenantId!='pratibha'">
					{name:'procurment',index:'procurment',sortable:false,align:'right'},
					{name:'payment',index:'payment',sortable:false,align:'right'},
					 </s:if>
					 <s:else>
					{name:'cropHarvest',index:'cropHarvest',sortable:false,align:'right'},
					{name:'cropSale',index:'cropSale',sortable:false,align:'right'},
					  </s:else>
					{name:'inspection',index:'inspection',sortable:false,align:'right'},
					  <s:if test="currentTenantId=='pratibha'">
					{name:'sowing',index:'sowing',sortable:false},
					{name:'geoTag',index:'geoTag',sortable:false},
					{name:'TotLandArea',index:'TotLandArea',sortable:false},
					{name:'farmerFoto',index:'farmerFoto',sortable:false},
					  </s:if>
					</s:else>
					<s:if test="currentTenantId=='griffith'">
					{name:'cropHarvest',index:'cropHarvest',sortable:false,align:'right'},
					{name:'cropSale',index:'cropSale',sortable:false,align:'right'},
					{name:'landPreparation',index:'landPreparation',sortable:false,align:'right'},
					{name:'fertiAppl',index:'fertiAppl',sortable:false,align:'right'},
					{name:'waterManagement',index:'waterManagement',sortable:false,align:'right'},
					{name:'fldVisits',index:'fldVisits',sortable:false,align:'right'},
					{name:'interCultivation',index:'interCultivation',sortable:false,align:'right'},
					{name:'weedManagement',index:'weedManagement',sortable:false,align:'right'},
					{name:'pesticideManagement',index:'pesticideManagement',sortable:false,align:'right'},
					{name:'training',index:'training',sortable:false,align:'right'},
					{name:'yieldEstimation',index:'yieldEstimation',sortable:false,align:'right'},
					{name:'harvest',index:'harvest',sortable:false,align:'right'},
					{name:'milestones',index:'milestones',sortable:false,align:'right'},
					{name:'geotagging',index:'geotagging',sortable:false,align:'right'},
					{name:'physicalQualityCheck',index:'physicalQualityCheck',sortable:false,align:'right'},
					{name:'landHoldingDetails',index:'landHoldingDetails',sortable:false,align:'right'},
					{name:'fertilizerApplication',index:'fertilizerApplication',sortable:false,align:'right'},
					{name:'pesticideManagement',index:'pesticideManagement',sortable:false,align:'right'},
					{name:'harvestDetailsTurmeric',index:'harvestDetailsTurmeric',sortable:false,align:'right'},
					 </s:if>
					{name:'status',index:'status',sortable:false},
				
				  	],
			     
			   
			   height: 301, 
			   width: $("#baseDiv").width(),
			   scrollOffset: 19,
			   rowNum:10,
			   shrinkToFit:true,
			   rowList : [10,25,50],
			  // autowidth:true,
			   viewrecords: true,  

			   beforeSelectRow: function(rowid, e) {
		            var iCol = jQuery.jgrid.getCellIndex(e.target);
		            if (iCol >11){return false; }
		            else{ return true; }
		        },
		        loadComplete: function(){//in subgrid 
		        	 <s:if test="type == 'agent'">	
		        	jQuery("#detail").jqGrid('hideCol', "subgrid");
		        	</s:if>
		    },
		        
			   //<s:if test="type == 'farmer'">		
			  // onSelectRow: function(id){ 

					 // document.detailform.id.value  =id;
					 // document.detailform.action = "farmerDistributionReport_detailTransaction.action?type=farmer";
			         // document.detailform.submit();      
					//},	
					  
					//</s:if>  
			   sortname:'id',
			   sortorder: "desc",
			   subGrid: false,
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
								url:'fieldStaffAccessReport_populateSubGridDetails.action?id='+row_id,
							   	pager: pager_id,
							    styleUI : 'Bootstrap',
							   	datatype: "json",	
							   	postData:{
							   		
							   	},
							   	colNames:[				   	       	  
	                                      '<s:text name="txnType"/>',	
							  		      '<s:text name="count"/>'
							  		      //'<s:text name="lastTransactionDate"/> (<s:property value="%{getGeneralDateFormat().toUpperCase()}" />)'
							  		      
					 	      	 ],
							   	colModel:[						
			   							{name:'txnType',index:'txnType',sortable:false},
			   							{name:'count',index:'count',sortable:false,align:'right'},
			   							//{name:'existingQty',index:'existingQty',sortable:false}
							   	],
							   	scrollOffset: 0, 
							    sortname:'id',
							    height: '100%', 
							    sortorder: "desc",
							    //autowidth: true,
							    rowNum:10,
							    shrinkToFit:true,
								rowList : [10,25,50],
							    viewrecords: true
			    });


				    jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
				   			//jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
						    jQuery("#"+subgrid_id).parent().css("width","100%");
						    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
						    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
						    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
						    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");
			},		
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        }
			});	
			

			$('#detail').jqGrid('setGridHeight',(reportWindowHeight));	
			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false});
			/* jQuery("#detail").jqGrid('hideCol',columnNames.split(",")).trigger('reloadGrid'); */
			
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
		    jQuery("#detail").setGridWidth($("#baseDiv").width());
			}
		function exportXLS(){
			var count=jQuery("#detail").jqGrid('getGridParam', 'records');
			/* if(count>recordLimit){
				 alert('<s:text name="export.limited"/>');
			}
			else */ if(isDataAvailable("#detail")){
				jQuery("#detail").setGridParam ({postData: {rows : 0}});
				jQuery("#detail").jqGrid('excelExport', {url: 'fieldStaffAccessReport_populateXLS.action'});
			}else{
			     alert('<s:text name="export.nodata"/>');
			}
		}	
	</script>


	<s:form name="form" action="fieldStaffAccessReport_list">
		<div class="appContentWrapper marginBottom">

			<section class='reportWrap row'>
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:text name="startingDate" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control" />
						</div>
					</div>
					<%-- <div class="reportFilterItem">
					<label for="farmerCode"><s:text name="deviceName" /></label>
					<div class="form-element">
						<s:select name="selectedDevice" id="selectedDevice" list="deviceList"
						headerKey="" headerValue="%{getText('txt.select')}"
						cssClass="form-control select2" style="auto"/>
					</div>
				</div> --%>
					<div class="filterEle">
						<label for="firstName"><s:text name="agentIdGrid" /></label>
						<div class="form-element">
							<s:select name="selectedFieldStafId" id="selectedFieldStafId"
								list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" style="auto" />
						</div>
					</div>
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

					<div class="filterEle" style="margin-top: 2%; margin-right: 0%;">
						<button onclick="loadGrid()" type="button"
							class="btn btn-success btn-sm" aria-hidden="true" id="generate"
							title='<s:text name="Search" />'>
							<b class="fa fa-search"></b>
						</button>

						<button type="button" class="btn btn-danger btn-sm"
							aria-hidden="true" id="clear" title='<s:text name="Clear" />'>
							<b class="fa fa-close"></b>
						</button>
					</div>

				</div>
			</section>
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
		</div>

		<div style="width: 98%;" id="baseDiv">
			<table id="detail"></table>

			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>

	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>