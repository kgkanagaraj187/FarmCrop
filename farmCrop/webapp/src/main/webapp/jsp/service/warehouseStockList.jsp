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
		jQuery("#detail").jqGrid(
				{
					url:'warehouseProduct_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',	
				    styleUI : 'Bootstrap',
				   	
				
				   	colNames:[
					   	   	   <s:if test='branchId==null'>
							  '<s:text name="app.branch"/>',
							   </s:if>
                          //   '<s:text name="transactionDate"/>',
                             '<s:property value="%{getLocaleProperty('warehouseProduct.orderNo')}"/>',
                             '<s:property value="%{getLocaleProperty('info.coOperative')}"/>',
	                         '<s:text name="receiptNo"/>',
	                       
	                     	'<s:property value="%{getLocaleProperty('warehouseProduct.vendorId')}" />',
	                        // '<s:text name="warehouseProduct.amount"/>',
                           //  '<s:text name="totalQty"/>',
                             '<s:text name="seasonCode"/>' 
		 	      	 ],
		 	      	colModel:[
		 	      	          
								<s:if test='branchId==null'>
								{name:'branchId',index:'branchId',width:290,sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
								</s:if>		
		 					 //   {name:'date',index:'date',width:240,sortable:false},
		 					    {name:'orderNo',index:'orderNo',width:295,align:'right',sortable:false,search:true},
		 					    {name:'warehouse',index:'warehouse',width:295,sortable:false,search:true},
		 					    {name:'receiptNo',index:'receiptNo',width:290,align:'right',sortable:false,search:true},
		 					   	{name:'vendor',index:'vendor',width:300,sortable:false,search:true},
		 					  //   {name:'amount',index:'amount',align:'right',width:140,sortable:false},
		 						//{name:'totalQty',index:'totalQty',align:'right',width:140,sortable:false},
		 						{name:'seasonCode',index:'seasonCode',width:300,sortable:false,search:true}	   						   		
		 					   	
		 					   	],
				   height: 255, 
				   width: $("#baseDiv").width(),
				   autowidth:true,
				   shrinkToFit:true,
				   scrollOffset: 0,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
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
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>"); //<div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'warehouseStockEntryReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
									'<s:text name="categoryCodeAndName"/>',
									'<s:text name="productCodeAndName"/>',
									'<s:text name="unit"/>',
									<s:if test="enableBatchNo ==1">
									'<s:text name="warehouseProduct.batchNo"/>',
                                    </s:if>
                                 /*    '<s:text name="expDate"/>', */
                                    '<s:text name="costPrice"/>',
                                    '<s:text name="goodQty"/>',
                                    '<s:text name="damQty"/>',
                                    '<s:text name="totalQty"/>',
                                    '<s:text name="amt"/>'
                                  
                                     ],				 	      	 
						   	colModel:[
									{name:'categoryCode',index:'categoryCode',width:150,sortable:false},
									{name:'productCode',index:'productCode',width:150,sortable:false},
									{name:'unit',index:'unit',width:150,sortable:false},
									<s:if test="enableBatchNo ==1">
									{name:'batchNo',index:'batchNo',width:150,sortable:false},
									</s:if>
								/* 	{name:'expDate',index:'expDate',width:150,sortable:false}, */
									{name:'costPrice',index:'costPrice',align:'right',width:150,sortable:false},
									{name:'goodQty',index:'goodQty',align:'right',width:150,sortable:false},
									{name:'damQty',index:'damQty',align:'right',width:150,sortable:false},
									{name:'totalQty',index:'totalQty',align:'right',width:150,sortable:false},
									{name:'amt',index:'amt',align:'right',width:150,sortable:false}	
									
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
				},				
				onSelectRow: function(id){ 
					  document.detailForm.id.value  =id;
			          document.detailForm.submit();      
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
		
				
			    jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
				jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	

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
		}	
		function clear(){
			resetReportFilter();
			document.form.submit();
		}


		
	});
</script>

</script>
<s:form name="form" action="warehouseProduct_list" >

						
</s:form>
<sec:authorize ifAllGranted="service.warehouseProduct.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>
<s:form name="createform" action="warehouseProduct_create">

</s:form>
<s:form name="detailForm" action="warehouseProduct_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<div class="appContentWrapper marginBottom">
 <div class="flex-layout">
<div style="width: 98%; position: relative;" id="baseDiv">
<table id="detail"></table>

<div id="pagerForDetail"></div>
<div id="pager_id"></div>
</div>
</div>
</div>
<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>
