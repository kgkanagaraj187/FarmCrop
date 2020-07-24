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
	<%-- var detailAction = "<%=request.getParameter("url")%>?type=<%=request.getParameter("type")%>";
	var listAction = "<%=request.getParameter("url1")%>?type=<%=request.getParameter("type")%>";
	var populateAction ="<%=request.getParameter("url3")%>?type=<%=request.getParameter("type")%>";
	var subGridAction ="<%=request.getParameter("url2")%>?type=<%=request.getParameter("type")%>";

	 --%>var recordLimit='<s:property value="exportLimit"/>';
	var type="";
	jQuery(document).ready(function(){
		type='<%=request.getParameter("type")%>';
		jQuery(".well").hide();
		 onFilterData();
		 document.getElementById('senderWarehouse').selectedIndex=0;
		 document.getElementById('receiverWarehouse').selectedIndex=0;
		 document.getElementById('product').selectedIndex=0;
		 jQuery('#truckId').val('');
		 jQuery('#receiptNo').val('');
		  $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
			$('.applyBtn').click();
			//getAjaxRecord();
           <s:if test='branchId==null'>
		 document.getElementById('branchIdParma').selectedIndex=0
		 </s:if> 
		jQuery("#detail").jqGrid(
				{
				  	url:'distributionStockTransferReport_detail.action?type='+type,
				 	pager: '#pagerForDetail',
				   	datatype: "json",	
				    styleUI : 'Bootstrap',
				   	mtype: 'POST',		
				   	postData:{
				   	 "startDate" : function(){			  
							return  document.getElementById("hiddenStartDate").value;
						  },
						  "endDate" : function(){			  
							return document.getElementById("hiddenEndDate").value;
						  },
						
						  "senderWarehouse" : function(){			  
							return document.getElementById("senderWarehouse").value;
						  },
						 
						  "receiverWarehouse" : function(){			  
							  return document.getElementById("receiverWarehouse").value;
						  },
				   		
						   "product" : function(){			  
							  return document.getElementById("product").value;
						  },
						   
						  "receiptNo" : function(){			  
							  return document.getElementById("receiptNo").value;
						  },
						  
						  "truckId" : function(){			  
							  return document.getElementById("truckId").value;
						  },
						  "season" : function(){
							  return document.getElementById("season").value;
						  }
						  
						  <s:if test='branchId==null'>
		 	 			  "branchIdParma" : function(){			  
		 	 			      return document.getElementById("branchIdParma").value;
		 	 	 		  },
		 	 	 		  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						  "subBranchIdParam" : function(){
							  return document.getElementById("subBranchIdParam").value;
						  },
						  </s:if>
		 	 			  </s:if>
				   	},
				
				  colNames:[	
				        	<s:if test='branchId==null'>
							'<s:text name="%{getLocaleProperty('app.branch')}"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>

								'<s:text name="app.subBranch"/>',
							</s:if>
		                      '<s:text name="Date"/>',
		                      '<s:text name="season"/>',
		                      '<s:text name="receiptNo"/>',
							  '<s:text name="senderWarehouse"/>',
							  '<s:text name="receiverWarehouse"/>',
							  '<s:text name="truckId"/>',
							  '<s:text name="driverName"/>',
							  '<s:text name="totQuantity"/>',
							  <s:if test="type == 'transfer'">	
							  <sec:authorize ifAllGranted='report.distributionstocktransfer.delete'>
		                      '<s:text name="action"/>',
		                      </sec:authorize>
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
				   		
				   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>		
						{name:'Date',index:'Date',sortable:false,frozen:true},
						{name:'season',index:'season',sortable:false,frozen:true},
						{name:'receiptNo',index:'receiptNo',sortable:false,frozen:true},
						{name:'senderWarehouse',index:'senderWarehouse',sortable:false,frozen:true},
						{name:'receiverWarehouse',index:'receiverWarehouse',sortable:false},
						{name:'truckId',index:'truckId',sortable:false},
						{name:'driverName',index:'driverName',sortable:false},
						{name:'totQuantity',index:'totQuantity',sortable:false},
						 <s:if test="type == 'transfer'">	
						<sec:authorize ifAllGranted='report.distributionstocktransfer.delete'>
					       {name:'action',index:'action',sortable:false,search:false,align:'center'},
					        </sec:authorize>
					       </s:if>
					  	],
			     
			          
				   height: 380, 
				   width: $("#baseDiv").width(),
				   scrollOffset: 0,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   //autowidth:true,
				   shrinkToFit:true,	
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
						   //url:'distributionStockTransferReport_subGridDetail.action?id='+row_id,
						<%--   url:'<%=request.getParameter("url2")%>?id='+row_id, --%>
					    url:'distributionStockTransferReport_subGridDetail.action?type=reception&id='+row_id,
	  
						   	pager: pager_id,
						   	datatype: "json",	
						   	postData:{
						   	 "product" : function(){			  
								  return document.getElementById("product").value;
							  }
						   	},
						   	colNames:[	
										 <s:if test="type == 'transfer'">	
                                        '<s:text name="%{getLocaleProperty('category')}"/>',
										'<s:text name="product"/>',
										'<s:text name="%{getLocaleProperty('unit')}"/>',
										'<s:text name="%{getLocaleProperty('disQuantity')}"/>'
										</s:if>
										<s:else>
                                        '<s:text name="%{getLocaleProperty('category')}"/>',
                                        '<s:text name="product"/>',
										'<s:text name="%{getLocaleProperty('unit')}"/>',
										'<s:text name="%{getLocaleProperty('receivedQuantity')}"/>',
										'<s:text name="%{getLocaleProperty('damagedQuantity')}"/>',
										</s:else>
										],				 	      	 
							   	colModel:[
										<s:if test="type=='transfer'">
										{name:'category',index:'category',sortable:false},
										{name:'product',index:'product',sortable:false},
										{name:'unit',index:'unit',sortable:false},
										{name:'disQuantity',index:'disQuantity',sortable:false}
										</s:if>
										<s:else>
										{name:'category',index:'category',sortable:false},
										{name:'product',index:'product',sortable:false},
										{name:'unit',index:'unit',sortable:false},
										{name:'receivedQuantity',index:'receivedQuantity',sortable:false},
										{name:'damagedQuantity',index:'damagedQuantity',sortable:false},
										</s:else> 
							   	],
							scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    autowidth: true,
						    shrinkToFit:true, 
						    rowNum:10,
						    height:'100%',
						    rowList : [10,25,50],
						    viewrecords: true					
					   });
					  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
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
		jQuery("#detail").jqGrid("setFrozenColumns");
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	
	});
	<%-- function getAjaxRecord(){
		$.post("<%=request.getParameter("url3")%>?type=<%=request.getParameter("type")%>",function(result){
			columnNames = result;
			loadGrid();
		});
	} --%>
	function onFilterData(){
		callAjaxMethod("distributionStockTransferReport_populateSeasonList.action","season");
		callAjaxMethod("distributionStockTransferReport_populateWarehouseList.action","senderWarehouse");
		callAjaxMethod("distributionStockTransferReport_populateWarehouseList.action","receiverWarehouse");
		callAjaxMethod("distributionStockTransferReport_populateProductList.action?type="+type,"product");
        
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
	
	function deleteDistribution(distId){
		if (confirm('<s:text name="confirm.delete"/> ')) {
		$.ajax({
			 type: "POST",
		        async: false,
		        url: "distributionStockTransferReport_populateDelete.action",
			     data: {distId:distId},
			     success: function(result) {
			    	 clear();
			     }
		});
		}
		}
	function noDeleteDistribution(){
		alert("Distribution Stock Already Received");
		
	}
	
	function reloadGrid(){
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
			 document.getElementById("hiddenStartDate").value;
			 document.getElementById("hiddenEndDate").value;	
			 receiptNo =  document.getElementById("receiptNo").value;
			//selectedFieldStaff = document.getElementById("fieldStaff").value;
			
		jQuery("#detail").jqGrid('setGridParam',{url:'distributionStockTransferReport_detail.action?type='+type,page:1})
			.trigger('reloadGrid');				
		}	
		//populateFarmerMethod();
		//populateAgentMethod();
	}
	
  function clear(){
		   //resetReportFilter();
		   document.form.action = "distributionStockTransferReport_list.action?type="+type;
		   document.form.submit();
		  }  
function exportXLS(){
		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'distributionStockTransferReport_populateXLS.action?type='+type});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}

/* function exportPDF(){
		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){	
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'distributionStockTransferReport_populatePDF.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	} */
</script>

	<s:form name="form" action="distributionStockTransferReport_list">
		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('startingDate')}" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control  input-sm" />
						</div>
					</div>
						<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('season')}" /></label>
						<div class="form-element">
							<s:select name="season" id="season" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								theme="simple"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('receiptNo')}" /></label>
						<div class="form-element">
							<s:textfield name="receiptNo" theme="simple" id="receiptNo"
								class="form-control " />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('sender')}" /></label>
						<div class="form-element">
							<s:select name="senderWarehouse" id="senderWarehouse" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								listKey="name" listValue="name" theme="simple"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('receiver')}" /></label>
						<div class="form-element">
							<s:select name="receiverWarehouse" id="receiverWarehouse"
								list="{}" headerKey="" headerValue="%{getText('txt.select')}"
								listKey="name" listValue="name" theme="simple"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('product')}" /></label>
						<div class="form-element">
							<s:select name="product" id="product" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="name"
								listValue="name" theme="simple"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('truckId')}" /></label>
						<div class="form-element">
							<s:textfield name="truckId" theme="simple" id="truckId"
								class="form-control " />
						</div>
					</div>
					<%-- 		<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('product')}"/></label>
					<div class="form-element">
						<s:select name="product" id="product" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							listKey="name" listValue="name" theme="simple"
							cssClass="form-control input-sm select2" />
					</div>
				</div>  --%>

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
								<s:select name="branchIdParma" id="branchIdParma"
									list="branchesMap" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
					</s:if>
				</s:else>

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


	<!-- <div class="appContentWrapper marginBottom">
		
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div> -->
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">

			<div class="flexItem-2">
				<div class="yui-skin-sam">
					<div class="btn-group" style="float: right;">
						<a href="#" data-toggle="dropdown"
							class="btn btn-sts dropdown-toggle"> <s:text name="export" />
							<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" role="menu">
							<s:if test="currentTenantId!='iccoa'">
								<li role="presentation"><a href="#" onclick="exportXLS()"
									tabindex="-1" role="menuitem"> <s:text name="excel" />
								</a></li>
							<%-- 	<s:if test="currentTenantId!='chetna'">
									<li role="presentation"><a href="#" onclick="exportPDF();"
										tabindex="-1" role="menuitem"> <s:text name="pdf" />
									</a></li>
								</s:if> --%>
							</s:if>
						</ul>
					</div>
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




</body>
