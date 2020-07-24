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

	jQuery(document).ready(function(){
		jQuery(".well").hide();
		onFilterData();
		loadFields();
		//document.getElementById('fieldl').selectedIndex=0;
		  $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
			$('.applyBtn').click();
			
		 $('.applyBtn').click();
		
		document.getElementById("warehouseId").selectedIndex=0;
		document.getElementById("orderNo").selectedIndex=0;
		document.getElementById("vendorId").selectedIndex=0;
		document.getElementById("receiptNo").selectedIndex=0;
		 <s:if test='branchId==null'>
		document.getElementById("branchIdParma").selectedIndex=0;
		</s:if>
		
		jQuery("#detail").jqGrid(
				{
					url:'warehouseStockEntryReport_detail.action?',
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
	                 "warehouse":function(){
		               return document.getElementById("warehouseId").value;
	                 },
	                 "order":function(){
			           return document.getElementById("orderNo").value;
		             },
		             "selectedProduct" : function(){			  
	 					  return document.getElementById("productId").value;
	 	 			  }, 
	                 "vendor":function(){
			               return document.getElementById("vendorId").value;
		              },
		             "receipt":function(){
				           return document.getElementById("receiptNo").value;
			         },
			         "seasonCode":function(){
	 	 			    	return document.getElementById("seasonId").value;
	 	 			      },
			         <s:if test='branchId==null'>
		              "branchIdParma":function(){
				           return document.getElementById("branchIdParma").value;
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
                             '<s:text name="transactionDate"/>',
                             '<s:text name="seasonCode"/>',
                             '<s:text name="orderNo"/>',
                             '<s:property value="%{getLocaleProperty('warehouseName')}" />',
	                         '<s:text name="receiptNo"/>',
	                         '<s:text name="%{getLocaleProperty('vendorCodeAndName')}"/>',
	                        /*  <s:if test="currentTenantId!='sagi' && currentTenantId!='movcd'">
	                         '<s:text name="paymentMode"/>',
	                         '<s:text name="amounts"/>',
	                         </s:if> */
	                         '<s:text name="%{getLocaleProperty('totalQty')}"/>',
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
		 					    {name:'date',index:'date',width:70,sortable:false,search:false},
		 					   	{name:'seasonCode',index:'seasonCode',align:'left',sortable:false},
		 					    {name:'orderNo',index:'orderNo',align:'right',sortable:false},
		 					    {name:'code',index:'code',sortable:false},
		 					    {name:'receiptNofdsd',index:'receiptNo',align:'right',sortable:false},
		 					   	{name:'vendorIddfdf',index:'vendorId',sortable:false},
		 					  /*  <s:if test="currentTenantId!='sagi' && currentTenantId!='movcd'">
		 					    {name:'paymentMode',index:'paymentMode',width:250,sortable:false},
		 					    {name:'amount',index:'amount',align:'right',width:250,sortable:false},
		 					    </s:if> */
		 						{name:'totalQty',index:'totalQty',align:'right',sortable:false,search:false}
		 					   	],
				   height: 301, 
				  width: $("#baseDiv").width(),
				   shrinkToFit: true,
				   sortname:'id',
				   sortorder: "desc",
				   scrollOffset: 20,
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
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
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'warehouseStockEntryReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
							postData:{	
						   		"startDate" : function(){
				                     return document.getElementById("hiddenStartDate").value;           
				                    },		
				                    "endDate" : function(){
						               return document.getElementById("hiddenEndDate").value;
					                 },
			                 "warehouse":function(){
				               return document.getElementById("warehouseId").value;
			                 },
			                 "order":function(){
					           return document.getElementById("orderNo").value;
				             },
				             "selectedProduct" : function(){			  
			 					  return document.getElementById("productId").value;
			 	 			  }, 
			                 "vendor":function(){
					               return document.getElementById("vendorId").value;
				              },
				             "receipt":function(){
						           return document.getElementById("receiptNo").value;
					         },
					         "seasonCode":function(){
			 	 			    	return document.getElementById("seasonId").value;
			 	 			      },
					         <s:if test='branchId==null'>
				              "branchIdParma":function(){
						           return document.getElementById("branchIdParma").value;
					         },
					         </s:if>
					         <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							  "subBranchIdParma" : function(){
								  return document.getElementById("subBranchIdParam").value;
							  },
							  </s:if>
						 },
						
						   	colNames:[	

'<s:text name="categoryCodeAndName"/>',

'<s:text name="%{getLocaleProperty('productCodeAndName')}"/>',

'<s:text name="unit"/>',

<s:if test="enableBatchNo ==1">
'<s:text name="%{getLocaleProperty('warehouseProduct.batchNo')}"/>',
</s:if>
/*    '<s:text name="expDate"/>', */

/* '<s:text name="%{getLocaleProperty('costPrice')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)', */

'<s:text name="goodQty"/>',

'<s:text name="damQty"/>',

'<s:text name="totalQty"/>'
                                     ],				 	      	 
						   	colModel:[
                                    
									{name:'categoryCode',index:'categoryCode',sortable:false,width:80},
								
									{name:'productCode',index:'productCode',sortable:false,width:80},
								
									{name:'unit',index:'unit',sortable:false,width:80},
									
									<s:if test="enableBatchNo ==1">
									{name:'batchNo',index:'batchNo',sortable:false,width:80},
									</s:if>
									
								/* 	{name:'expDate',index:'expDate',sortable:false}, */
								 
									/* {name:'costPrice',index:'costPrice',align:'right',sortable:false}, */
									
									
									{name:'goodQty',index:'goodQty',align:'right',sortable:false,width:80},
								
									{name:'damQty',index:'damQty',align:'right',sortable:false,width:80},
									
									{name:'totalQty',index:'totalQty',align:'right',sortable:false,width:80}
										
									
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
							shrinkToFit: true,
						    viewrecords: true				
					   });
					 //  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
					   	jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
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
		//jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	

		function onFilterData(){
			callAjaxMethod("warehouseStockEntryReport_populateWarehouseList.action","warehouseId");
			callAjaxMethod("warehouseStockEntryReport_populateProductList.action","productId");
			callAjaxMethod("warehouseStockEntryReport_populateVendorList.action","vendorId");
			callAjaxMethod("warehouseStockEntryReport_populateSeasonList.action","seasonId");
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
		   jQuery("#detail").jqGrid('setGridParam',{url:"warehouseStockEntryReport_detail.action?",page:1}).trigger('reloadGrid');
		   }
			loadFields();
		}	
		function clear(){
			resetReportFilter();
			document.form.submit();
		}
	});
	
	function exportXLS() {
	    if (isDataAvailable("#detail")) {
	    	var totalQty=$("#totalQty").text();
	        jQuery("#detail").setGridParam({postData: {rows: 0,totalQty:totalQty}});
	        jQuery("#detail").jqGrid('excelExport', {url: 'warehouseStockEntryReport_populateXLS.action?'});
	}     else {
	        alert('<s:text name="export.nodata"/>');
	    }
	}
	
	function loadFields(){
		var warehouse=document.getElementById("warehouseId").value;
		var order=document.getElementById("orderNo").value;
		var selectedProduct=document.getElementById("productId").value;
		var vendor=document.getElementById("vendorId").value;
		var receipt=document.getElementById("receiptNo").value;
		var seasonCode=document.getElementById("seasonId").value;
		var branch='<s:property value="getBranchId()"/>';
		var startDate=document.getElementById("hiddenStartDate").value;  
		var endDate=document.getElementById("hiddenEndDate").value;
		var branchIdParma="";
		if(branch==null)
		{
			branchIdParma=document.getElementById("branchIdParma").value;
		}
	 	$.post("warehouseStockEntryReport_populateLoadFields",{warehouse:warehouse,order:order,selectedProduct:selectedProduct,vendor:vendor,receipt:receipt,seasonCode:seasonCode,branchIdParma:branchIdParma,startDate:startDate,endDate:endDate},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalQty").html(json[0].totalQty);
	 			
	 	});
	}
	
</script>


	<s:form name="form" action="warehouseStockEntryReport_list">
		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('startingDate')}" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm " />
						</div>
					</div>
					
					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParma" id="branchIdParma" list="parentBranches"
										headerKey="" headerValue="Select" cssClass="select2"
										onchange="populateChildBranch(this.value)" />

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
									<s:select name="branchIdParma" id="branchIdParma" list="branchesMap"
										headerKey="" headerValue="%{getText('txt.select')}"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
					</s:else>

					<div class="filterEle">
						<label for="txt"><s:text name="name.season" /></label>
						<div class="form-element">
							<s:select name="filter.seasonCode" id="seasonId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="orderNo" /></label>
						<div class="form-element">
							<s:textfield name="orderNo" id="orderNo" theme="simple"
								cssClass="form-control input-sm "></s:textfield>
						</div>
					</div>
					
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse')}" /> </label>
						<div class="form-element">
							<s:select name="warehouse" id="warehouseId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="filterEle">
						<label for="txt"><s:text name="receiptNo" /></label>
						<div class="form-element">
							<s:textfield name="receiptNo" id="receiptNo" theme="simple"
								cssClass="form-control input-sm "></s:textfield>
						</div>
					</div>


					<div class="filterEle">
						<label for="txt"><s:text name="vendor" /></label>
						<div class="form-element">
							<s:select name="vendor" id="vendorId" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="product" /></label>
						<div class="form-element">
							<s:select name="product" id="productId" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>


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
		<s:if test="currentTenantId!='pratibha'">
		<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container">
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="totalQty"></span> <s:text name="%{getLocaleProperty('totalQty')}"/> &nbsp;<i
							class="fa fa-user"></i></span>
					</div>
					
				</div>
			</div>
</s:if>
			<%-- 			<div class="flexItem text-right flex-right">
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
			</div> --%>

			<div class="flexItem text-right flex-right">
				<s:if test="currentTenantId!='chetna'">
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
				</s:if>
			</div>

		</div>


		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>

	<div style="width: 99%; position: relative;" id="baseDiv">
		<table id="detail"></table>

		<div id="pagerForDetail"></div>
		<div id="pager_id"></div>
	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>
