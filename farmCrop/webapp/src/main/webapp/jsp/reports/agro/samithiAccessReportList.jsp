<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
</head>
<!-- <style>
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
</style> -->
<body>

<script>
	$(document).ready(function(){
		loadGrid();
		jQuery(".well").hide();
		//jQuery(".breadcrumb").append("<li><a href='#'>Report</a></li><li><a href='fieldStaffAccessReport_list.action'>FieldStaff Access Report</a></li>");
		
		onFilterData();
		
		jQuery("#generate").click( function() {
			jQuery("#detail").jqGrid('setGridParam',{url:"samithiAccessReport_detail.action?",page:1}).trigger('reloadGrid');
		});
		
		jQuery("#clear").click( function() {
			clear();
		});
	});
	
	function clear(){
		resetReportFilter();
		document.form.submit();
	}
		function loadGrid(){
			jQuery("#detail").jqGrid(
			{
			   	url:'samithiAccessReport_detail.action',
			 	pager: '#pagerForDetail',
			   	datatype: "json",	
			    styleUI : 'Bootstrap',
			   	mtype: 'POST',		
			   	postData:{
			   	  	"warehouse" : function(){			  
				      return document.getElementById("warehouseId").value;
 			     	}
			   	}, 
			   	    colNames:[		
						<s:if test='branchId==null'>
							'<s:text name="app.branch"/>',
						</s:if>
						'<s:property value="%{getLocaleProperty('samithiName')}" />',						
						 '<s:text name="totalFieldStaff"/>',
						  '<s:property value="%{getLocaleProperty('totalFarmer')}" />',
						  '<s:property value="%{getLocaleProperty('farmerEnroll')}" />',
						  '<s:text name="distribution"/>',
						  '<s:text name="procurment"/>',
						  '<s:text name="payment"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
						  '<s:text name="inspection"/>',
						 // '<s:text name="agentNameGrid"/>',
						 // '<s:text name="status"/>',
						 // '<s:text name="lastTransactionDate"/>',  
	 	      	 ],   
			   	colModel:[	
					<s:if test='branchId==null'>
						{name:'branchId',index:'branchId',sortable: false},	   				   		
					</s:if>	
					{name:'samithiName',index:'samithiName',sortable:false},
					{name:'totalFieldStaff',index:'totalFieldStaff',sortable:false,align:'right'},
					{name:'totalFarmer',index:'totalFarmer',sortable:false,align:'right'},
					{name:'farmerEnroll',index:'farmerEnroll',sortable:false,align:'right'},
					{name:'distribution',index:'distribution',sortable:false,align:'right'},
					{name:'procurment',index:'procurment',sortable:false,align:'right'},
					{name:'payment',index:'payment',sortable:false,align:'right'},
					{name:'inspection',index:'inspection',sortable:false,align:'right'}
					//{name:'status',index:'status',width:250,sortable:false},
					//{name:'lastTransactionDate',index:'lastTransactionDate',width:275,sortable:false},
				  	],
			     
			   
			   height: 301, 
			   width: $("#baseDiv").width(),
			   scrollOffset: 19,
			   rowNum:10,
			   rowList : [10,25,50],
			   /* autowidth:true, */
			   shrinkToFit:true,	
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
								url:'samithiAccessReport_populateSubGridDetails.action?id='+row_id,
							   	pager: pager_id,
							    styleUI : 'Bootstrap',
							   	datatype: "json",	
							   	postData:{
							   		
							   	},
							   	colNames:[				   	       	  
	                                      '<s:text name="txnType"/>',	
							  		      '<s:text name="count"/>',
							  		     // '<s:text name="lastTransactionDate"/>'
							  		      
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
							   /*  autowidth: true, */
							    shrinkToFit:true,
							    rowNum:10,
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
				jQuery("#detail").jqGrid('excelExport', {url: 'samithiAccessReport_populateXLS.action'});
			}else{
			     alert('<s:text name="export.nodata"/>');
			}
		}
		function onFilterData(){
			callAjaxMethod("samithiAccessReport_populateWarehouseList.action","warehouseId");
			
			
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

	<div class="appContentWrapper marginBottom">
		<s:form name="form" action="samithiAccessReport_list">
			
				<section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
					<label for="daterange"><s:text name="Group" /></label>
					<div class="form-element">
						<s:select name="warehouse" id="warehouseId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control select2" />
					</div>
				</div>
							
				<div  class="filterEle" style="margin-top: 2%;margin-right: 0%;">
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


			
		</s:form>
	</div>

	<div class="appContentWrapper marginBottom">
	
	
		<div class="flex-layout reportData">
			<!--<div class="flexItem-2">
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
			
		</div>-->
		
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
</body>