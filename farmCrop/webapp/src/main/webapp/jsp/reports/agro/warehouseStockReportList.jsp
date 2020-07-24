<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
</head>
<style>
.ui-jqgrid .ui-jqgrid-htable th div {
	height: auto;
	overflow: hidden;
	padding-right: 4px;
	position: relative;
	white-space: normal !important;
	word-break:break-word;
}

th.ui-th-column div {
	white-space: normal !important;
	height: auto !important;
	word-break:break-word;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	word-break:break-word;
	/*height: auto !important;*/
}
</style>
<body>
	<script type="text/javascript">
	var tenantId="<s:property value='tenantId'/>";
jQuery(document).ready(function(){
	jQuery(".well").hide();
	onFilterData();
	//document.getElementById('fieldl').selectedIndex=0;
    document.getElementById('warehouseId').selectedIndex=0;
	document.getElementById('productId').selectedIndex=0;
	<s:if test='branchId==null'>
    document.getElementById('branchIdParma').selectedIndex=0;
    </s:if>
    var enableBatch="<s:property value='getEnableBatchNo()'/>";
    
    // if(enableBatch=='1'){
    	loadGrid();
   //}else{ 
    	//loadGridWithoutBatch();
   // }
    
	jQuery("#generate").click( function() {
		reloadGrid();	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	

	function reloadGrid(){
	jQuery("#detail").jqGrid('setGridParam',{url:"warehouseStockReport_detail.action?",page:1}).trigger('reloadGrid');
	}

	function clear(){	
		resetReportFilter();
		document.form.submit();
	}
});


function loadGrid(){
	jQuery("#detail").jqGrid(
			{
				url:'warehouseStockReport_detail.action',
				editurl:'warehouseStockReport_populateWarehouseStockUpdate.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			   	mtype: 'POST',
			    styleUI : 'Bootstrap',
			   	postData:{	
				  "warehouse" : function(){			  
				      return document.getElementById("warehouseId").value;
 			          },
 			      "product" : function(){			  
 					  return document.getElementById("productId").value;
 	 			      }, 
 	 			   
 	 			  "seasonCode":function(){
 	 			    	return document.getElementById("seasonId").value;
 	 			      },
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
				  
			    },
			  	colNames:[
			  	          
			  	        <s:if test='branchId==null'>
						'<s:text name="%{getLocaleProperty('app.branch')}"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					
						'<s:text name="app.subBranch"/>',
					</s:if>
						'<s:text name="warehouse"/>',
			  		    <s:if test="currentTenantId!='sagi'">
			  		   	  '<s:text name="subCategory"/>',
			  		   	  </s:if>
			  		   	  '<s:text name="product"/>',
			  		   	'<s:text name="unit"/>',
			  		   /*  <s:if test="currentTenantId=='chetna'">
					      '<s:text name="unit"/>',
						   </s:if> */
						   <s:if test="enableBatchNo==1 && currentTenantId!='sagi'" >
							'<s:text name="%{getLocaleProperty('batchNo')}"/>',
							</s:if>
						  '<s:text name="stock"/>',
						  <s:if test="currentTenantId!='lalteer'">
						  '<s:text name="seasonCode"/>',
							/*  <s:if test="currentTenantId!='pratibha' && currentTenantId!='wilmar'&& currentTenantId!='chetna'">
						  '<s:text name="Actions"/>'
						  </s:if> */
						  </s:if>
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
					{name:'w.name',index:'w.name',sortable:false,resizable:false,search:false},
					 <s:if test="currentTenantId!='sagi'">
				    {name:'subCategory',index:'subCategory',sortable:false,search:false},
				    </s:if>
				    {name:'productName',index:'productName',sortable:false,search:false},
				    {name:'unit',index:'unit',sortable:false,search:false,width:60},
				  /*   <s:if test="currentTenantId=='chetna'">
				    {name:'unit',index:'unit',width:30,sortable:false},
					   </s:if> */
					   <s:if test="enableBatchNo ==1 && currentTenantId!='sagi'">
						{name:'batchno',index:'batchno',sortable:false,width:100},
						</s:if>
					{name:'stock',index:'stock',align:'right',sortable:false,search:false,width:120,editable: true,editoptions:{maxlength:"7"}},
					 <s:if test="currentTenantId!='lalteer'">
					{name:'seasonCode',index:'seasonCode',sortable:false,search:false,width:100},
					 </s:if>
					// <s:if test="currentTenantId!='pratibha' && currentTenantId!='chetna' && currentTenantId!='wilmar'">
					//	{name:'act',index:'act',width:60,search:false,sortable:false,formatter: "actions",formatoptions: {keys: true,
					  // 		delOptions: { url: 'warehouseStockReport_delete.action' ,
					   	//		afterShowForm:function ($form) {
	                     //   	    $form.closest('div.ui-jqdialog').position({
	                    //    	        my: "center",
	                    //    	        of: $("#detail").closest('div.ui-jqgrid')
	                    //    	    });
	                    //    	},	
	                        	
	                    //    	afterSubmit: function(data) 
	                      //      {
	                       // 	  var json = JSON.parse(data.responseText);
									//document.getElementById("validateErrorCate").innerHTML=json.msg;
							//		jQuery("#detail").jqGrid('setGridParam',{url:"warehouseStockReport_detail.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
						//			showPopup(json.msg,json.title);
									/* jQuery.post("state_countryList.action",function(result){
									 	insertOptions("scountry",JSON.parse(result)); 
									 }), */
							//		jQuery('.ui-jqdialog-titlebar-close').click();
									
	                       //     }
			   		
					   	//	} ,
					   		
					   	// afterSave: function (id, response, options) {
	                     //    var json = JSON.parse(response.responseText);
						//		jQuery("#detail").jqGrid('setGridParam',{url:"warehouseStockReport_detail.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

							//	showPopup(json.msg,json.title);
								/* jQuery.post("state_countryList.action",function(result){
								 	insertOptions("scountry",JSON.parse(result)); 
								}); */
							//	jQuery('.ui-jqdialog-titlebar-close').click();
								
	                    // }
				   	//	}}
					//	</s:if> 
			         	],
			   height: 301, 
			   width: $("#baseDiv").width(),
			   scrollOffset: 20,
			   shrinkToFit: true,
			   //autoWidth:true,
			   sortname:'id',
			   sortorder: "asc",
			   rowNum:10,
			   rowList : [10,25,50],
			   viewrecords: true,
			   <s:if test="currentTenantId=='sagi'">
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
							url:'warehouseStockReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
                                  /*  '<s:text name="batchno"/>' */
                                
									'<s:text name="%{getLocaleProperty('sagiCode')}"/>',
									'<s:text name="%{getLocaleProperty('lotNo')}"/>',
                                   '<s:text name="stock"/>'
                                    ],				 	      	 
						   	colModel:[
									{name:'batchno',index:'batchno',sortable:false},
									{name:'lotNo',index:'lotNo',sortable:false},
									{name:'stock',index:'stock',sortable:false}
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
				
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
	//jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
}
function exportXLS() {
    if (isDataAvailable("#detail")) {
        jQuery("#detail").setGridParam({postData: {rows: 0}});
        jQuery("#detail").jqGrid('excelExport', {url: 'warehouseStockReport_populateXLS.action?'});
}     else {
        alert('<s:text name="export.nodata"/>');
    }
}

function onFilterData(){
	callAjaxMethod("warehouseStockReport_populateWarehouseList.action","warehouseId");
	callAjaxMethod("warehouseStockReport_populateProductList.action","productId");
	callAjaxMethod("warehouseStockReport_populateSeasonList.action","seasonId");
	
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
			<s:form name="form" action="warehouseStockReport_list">
				<div class="appContentWrapper marginBottom">
					<section class="reportWrap row">
					<div class="gridly">
						<div class="filterEle">
							<label for="txt"><s:property value="%{getLocaleProperty('Warehouse')}"/></label>
							<div class="form-element">
								<s:select name="warehouse" id="warehouseId" list="{}"
									headerKey="" headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>

						<div class="filterEle">
							<label for="txt"><s:text name="product" /></label>
							<div class="form-element">
								<s:select name="product" id="productId" list="{}"
									headerKey="" headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>

						<div class="filterEle">
							<label for="txt"><s:text name="name.season" /></label>
							<div class="form-element">
								<s:select name="filter.seasonCode" id="seasonId"
									list="{}" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
						<s:if
							test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							<s:if test='branchId==null'>
								<div class="filterEle">
									<label for="txt"><s:text name="app.branch" /></label>
									<div class="form-element ">
										<s:select name="branchIdParma" id="branchIdParma"
											list="parentBranches" headerKey="" headerValue="Select"
											cssClass="select2" onchange="populateChildBranch(this.value)" />

									</div>
								</div>
							</s:if>

							<div class="filterEle">
								<label for="txt"><s:text name="app.subBranch" /></label>
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
										<s:select name="branchIdParma" id="branchIdParma"
											list="branchesMap" headerKey=""
											headerValue="%{getText('txt.select')}"
											cssClass="form-control input-sm select2" />
									</div>
								</div>
							</s:if>
						</s:else>

						<div class="filterEle" style="margin-top: 24px;">
							<button type="button" class="btn btn-success btn-sm"
								id="generate" aria-hidden="true">
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
									data-toggle="tab" aria-controls="dropdown2"
									aria-expanded="true"><i class="fa fa-table"></i> <s:text
											name="excel" /></a></li>
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
</body>