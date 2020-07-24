<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/openlayers/OpenLayers.js"></script>
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
	<link rel="stylesheet" href="plugins/image_slider/css/responsive-carousel.css" media="screen">
	<link rel="stylesheet" href="plugins/image_slider/css/demostyles.css">
	
	<script src="plugins/image_slider/js/responsive-carousel.js"></script>
	<script src="plugins/image_slider/js/responsive-carousel.autoinit.js"></script>
	<script src="plugins/image_slider/js/globalenhance.js"></script>

	<script type="text/javascript">

	var distributorId='';
	var receiptNo='';
	var selectedFieldStaff='';
	var sDate='';
	var eDate='';
	var recordLimit='<s:property value="exportLimit"/>';
	var distributionImgAvil='<s:property value="distImgAvil"/>';
	var tenant = '<s:property value="getCurrentTenantId()"/>';
	
	var detailAction = "<%=request.getParameter("url")%>?type=<%=request.getParameter("type")%>";
	var listAction = "<%=request.getParameter("url1")%>?type=<%=request.getParameter("type")%>";
	var populateAction ="<%=request.getParameter("url4")%>?type=<%=request.getParameter("type")%>";
	var populatePDFAction ="farmerDistributionReport_populatePDF.action?type=<%=request.getParameter("type")%>";
	
	
	var $overlay;
	var $modal;
	var $slider;
	
	
  	jQuery(document).ready(function(){
  		var tenant='<s:property value="getCurrentTenantId()"/>';
  		jQuery(".well").hide();
  		onFilterData();
		var columnNames ;
		document.getElementById('receiptNo').value='';
		 document.getElementById('location').selectedIndex=0;
		 document.getElementById('fieldStaff').selectedIndex=0;
		 <s:if test="currentTenantId=='chetna'">
			document.getElementById('stateName').selectedIndex=0;
			//document.getElementById('fpo').selectedIndex=0;
			document.getElementById('icsName').selectedIndex=0;
			</s:if>
			 <s:if test="currentTenantId=='susagri'">
			 document.getElementById('samithi').selectedIndex=0;
			 </s:if>
		   $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
			$('.applyBtn').click();
			var d1=	jQuery('#daterange').val();
			
		 <s:if test="type == 'farmer'">	
		 document.getElementById('farmerId').selectedIndex=0;
		// document.getElementById('fatherName').selectedIndex=0;
		// document.getElementById('productId').selectedIndex=0;
		//	populateFarmerMethod();
		 </s:if>
 <s:if test="type == 'agent'">
		 
		// populateAgentMethod();
		 </s:if>
		 <s:if test='branchId==null'>
		 document.getElementById('branchIdParma').selectedIndex=0
		 </s:if>	
		getAjaxRecord();
	
		function loadGrid(){
			
		jQuery("#detail").jqGrid(
		{
		   	url:detailAction,
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
				
				  "location" : function(){			  
					return document.getElementById("location").value;
				  },
				 
				  "selectedFieldStaff" : function(){			  
						return selectedFieldStaff;
				  },
				  <s:if test="type == 'farmer'">	
				  "farmerId" : function(){
					  return document.getElementById("farmerId").value;
				  },
				  </s:if>
				/*   "fatherName" : function(){
					  return document.getElementById("fatherName").value;
				  }, */
				  <s:if test="type != 'farmer'">
				   "product" : function(){			  
 					  return document.getElementById("productId").value;
 	 			  }, 
				  </s:if>
 	 			  "seasonCode":function(){
	 			      return document.getElementById("seasonId").value;
	 			  },
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
 	 			<s:if test="currentTenantId=='chetna'">
				  "stateName" : function(){
				  return  document.getElementById('stateName').value;
				  },
				 /*  "fpo" : function(){
					  return  document.getElementById('fpo').value;
					  }, */
					  
				"icsName" : function(){
						  return  document.getElementById('icsName').value;
						  },
				  </s:if>
						  <s:if test="currentTenantId=='susagri'">
						  "samithi" : function(){
							  return  document.getElementById('samithi').value;
							  },
						  </s:if>
				 
			}, 
			 <s:if test="type == 'farmer'">	
		   	    colNames:[	
					<s:if test='branchId==null'>
					'<s:text name="%{getLocaleProperty('app.branch')}"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>

						'<s:text name="app.subBranch"/>',
					</s:if>
						'<s:text name="%{getLocaleProperty('txnDate')}"/>',
					  '<s:text name="stockType"/>',
					  '<s:property value="%{getLocaleProperty('warehouse')}"/>	',
					  '<s:property value="%{getLocaleProperty('profile.agent')}"/>	',
					  '<s:property value="%{getLocaleProperty('farmerType')}"/>	',  
					  '<s:property value="%{getLocaleProperty('farmer')}"/>	', 
					//  '<s:property value="%{getLocaleProperty('fatherName')}"/>',
					  '<s:text name="farmer.mobileNumber"/>',
					  '<s:property value="%{getLocaleProperty('village.name')}" />',
					 /*  '<s:property value="%{getLocaleProperty('samithi')}" />', */
					  '<s:property value="%{getLocaleProperty('freeDist')}" />',
					  '<s:text name="totalQty"/>',
					  '<s:text name="grossAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
					  '<s:text name="tax"/>',
					  '<s:text name="finalAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
					  '<s:text name="txnAmt"/>  (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
					 
					  
					 // '<s:text name="cash"/>',
					  //'<s:text name="credit"/>',
					  '<s:text name="seasonCode"/>',
					  <s:if test="distImgAvil==1">
					  '<s:text name="dist.image"/>'
					 </s:if>
				      // '<s:text name="txnType"/>', 
					 // '<s:text name='%{"comId"+type}'/>',
		  		      //'<s:text name='%{"comName"+type}'/>',
					 // '<s:text name='%{"distributorId"+type}'/>',
		  		     // '<s:text name='%{"distributorName"+type}'/>',
		  		     // '<s:text name="fAgentId"/>',
		  		     // '<s:text name="fAgentName"/>',
		  		     // '<s:text name='%{"location"+type}'/>',
		  		      //'<s:text name="agentType"/>'
		  		      // '<s:text name="pdf"/>',
		  		   
		  		      
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
				{name:'txnDate',index:'a1.txnTime',sortable:false,frozen:true},
				{name:'type1',index:'type1',sortable:false,frozen:true},
				{name:'locationagent',index:'locationagent',sortable:false,frozen:true},
				{name:'distributiorIdagent',index:'distributiorIdagent',sortable:false,frozen:true},
				{name:'farmerType',index:'farmerType',sortable:false,frozen:true},
				{name:'farmer',index:'farmer',sortable:false,frozen:true},
				{name:'farmer.mobileNumber',index:'farmer.mobileNumber',sortable:false},
				//{name:'lastName',index:'lastName',width:150,sortable:false},
				{name:'village',index:'village',sortable:false},
				/* {name:'samithi',index:'samithi',sortable:false}, */
				{name:'freeDist',index:'freeDist',sortable:false},
				{name:'totalQty',index:'totalQty',align:'right',sortable:false},
				{name:'grossAmt',index:'grossAmt',align:'right',sortable:false},
				{name:'tax',index:'tax',align:'right',sortable:false},
				{name:'finalAmt',index:'finalAmt',align:'right',sortable:false},
				{name:'txnAmt',index:'txnAmt',align:'right',sortable:false},
				
				//{name:'cash',index:'cash',width:170,align:'right',sortable:false},
				//{name:'credit',index:'credit',width:170,align:'right',sortable:false},
				{name:'seasonCode',index:'seasonCode',align:'left',sortable:false},
				 <s:if test="distImgAvil==1">
				{name:'distImage',index:'distImage',align:'right',sortable:false}
				</s:if>
			
			  	],
			  	
		   	</s:if>
              <s:else>
			colNames:[		
					<s:if test='branchId==null'>
                     '<s:text name="%{getLocaleProperty('app.branch')}"/>',
                   </s:if>
                  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>

                   '<s:text name="app.subBranch"/>',
                  </s:if>
                   '<s:text name="%{getLocaleProperty('txnDate')}"/>',
		  		      '<s:property value="%{getLocaleProperty('warehouse')}"/>',
		  		      '<s:property value="%{getLocaleProperty('profile.agent')}"/>',
		  		      '<s:text name="category"/>',
		  		      '<s:text name="product"/>',
		  		    '<s:text name="unit"/>',
		  		    <s:if test="enableBatchNo ==1">
						'<s:text name="warehouseProduct.batchNo"/>',
                      </s:if>
		  		      //'<s:text name="existingQty"/>',
		  		      '<s:text name="%{getLocaleProperty('distQty')}"/>',
		  		     //'<s:text name="currentQty"/>',	
		  		      '<s:text name="seasonCode"/>',	
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
				{name:'txnDate',index:'a1.txnTime',width:120,sortable:false,frozen:true},
		   		{name:'location',index:'location',sortable:false,frozen:true},
		   		{name:'agent',index:'agent',sortable:false,frozen:true},
		   		{name:'category',index:'category',sortable:false},
		   		{name:'product',index:'product',sortable:false},
		   		{name:'unit',index:'unit',sortable:false},
		   	   <s:if test="enableBatchNo ==1">
		   		{name:'batchNo',index:'batchNo',sortable:false},
		   		</s:if>
		   		//{name:'existingQty',index:'existingQty',align:'right',sortable:false},
		   		{name:'distQty',index:'distQty',align:'right',sortable:false},
		   		//{name:'currentQty',index:'currentQty',align:'right',sortable:false},
		   		{name:'seasonCode',index:'seasonCode',align:'left',sortable:false}
			 	],
              </s:else>
		     
		   
		   height: 301, 
		   width: $("#baseDiv").width(),
		   scrollOffset: 19,
		   rowNum:10,
		   rowList : [10,25,50],
		   autowidth:true,
		   shrinkToFit:true,	
		   viewrecords: true,  

		   beforeSelectRow: function(rowid, e) {
	            var iCol = jQuery.jgrid.getCellIndex(e.target);
	            if (iCol >11){return false; }
	            else{ return true; }
	        },
	        loadComplete: function(data){//in subgrid 
	        	 <s:if test="type == 'agent'">	
	        	jQuery("#detail").jqGrid('hideCol', "subgrid");
	        	</s:if>
	      $("#totalQty").text(data.qty);
	       
	      $("#totalAmt").text(data.finalAmt);
	        
	        $("#totalPayAmt").text(data.payMent);
	        $("#distQty").text(data.agentQty);
	        
			$("#existQty").text(data.agentExistQty);
	        
	        $("#currentQty").text(data.agentCurQty);
	       
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
							url:'<%=request.getParameter("url2")%>?id='+row_id,
						   	pager: pager_id,
						    styleUI : 'Bootstrap',
						   	datatype: "json",	
						   	colNames:[		
									     '<s:text name="category"/>',	
						  		      '<s:text name="product"/>',
						  		    '<s:text name="unit"/>',
						  		    <s:if test="enableBatchNo ==1">
										'<s:text name="warehouseProduct.batchNo"/>',
				                      </s:if>
						  		      '<s:text name="existingQty"/>',
						  		      '<s:text name="cstPrice"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
						  		     /*  '<s:text name="sellingPrice"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)', */
						  		      '<s:text name="amount"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
						  		      '<s:text name="distQty"/>',
						  		      '<s:text name="avlQty"/>', 
						  		     
						  		      
				 	      	 ],
						   	colModel:[		
									
		   							{name:'subCategoryName',index:'subCategoryName',sortable:false},
		   							{name:'productName',index:'productName',sortable:false},
		   							{name:'unit',index:'unit',sortable:false},
		   						    <s:if test="enableBatchNo ==1">
		   					   		  {name:'batchNo',index:'batchNo',sortable:false},
		   					   		</s:if>
		   							{name:'existingQty',index:'existingQty',sortable:false,align:'right'},
		   							{name:'cstPrice',index:'cstPrice',align:'right',sortable:false,},
		   							/* {name:'sellingPrice',index:'sellingPrifieldsetce',align:'right',sortable:false}, */
		   							{name:'amount',index:'amount',align:'right',sortable:false},
		   							{name:'distQty',index:'distQty',align:'right',sortable:false},
		   							{name:'avlQty',index:'avlQty',align:'right',sortable:false},
		   							
		   							
		   		
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    autowidth: true,
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
		
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',
				{edit:false,add:false,del:false,search:false,refresh:false});
		jQuery("#detail").jqGrid('hideCol',columnNames.split(",")).trigger('reloadGrid');
		
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
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();

		});	
		jQuery("#detail").jqGrid('setFrozenColumns');
		
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
				selectedFieldStaff = document.getElementById("fieldStaff").value;
				
			jQuery("#detail").jqGrid('setGridParam',{url:detailAction,page:1})
				.trigger('reloadGrid');				
			}	
		//	populateFarmerMethod();
			//populateAgentMethod();
		}
		
		function clear(){
			document.getElementById("receiptNo").value ="";
			document.form.action = listAction;
			resetReportFilter();
			document.form.submit();
			
		}

		function getAjaxRecord(){
			$.post("<%=request.getParameter("url3")%>?type=<%=request.getParameter("type")%>",function(result){
				columnNames = result;
				loadGrid();
			});
		}
		
	});
 
function exportXLS(){
	var headerData =null;		
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name=".limited"/>');
	}
	else if(isDataAvailable("#detail")){
		<s:if test="type == 'farmer'">
		var totalQty=$("#totalQty").text();
		var totalAmt=$("#totalAmt").text();
		var totalPayAmt=$("#totalPayAmt").text();
		headerData='<s:text name="totalQty"/>(Kgs)'+"###"+totalQty+"###"+'<s:text name="finalAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalAmt+"###"+'<s:text name="txnAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalPayAmt;
		</s:if>
		<s:if test="type == 'agent'">	
		var existQty=$("#existQty").text();
		var distQty=$("#distQty").text();
		var currentQty=$("#currentQty").text();
		<s:if test="currentTenantId=='chetna'">
		headerData='<s:text name="distQty"/>(<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+distQty;
		</s:if>
		 <s:else>
		headerData='<s:text name="existQty"/>(Kgs)'+"###"+existQty+"###"+'<s:text name="distQty"/>(Kgs)'+"###"+distQty+"###"+'<s:text name="currentQty"/>(Kgs)'+"###"+currentQty;
		</s:else>
		
		
		
		</s:if>
		jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});

		 jQuery("#detail").jqGrid('excelExport', {url: populateAction});
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
	<s:if test="type == 'farmer'">
	var totalQty=$("#totalQty").text();
	var totalAmt=$("#totalAmt").text();
	var totalPayAmt=$("#totalPayAmt").text();
	headerData='<s:text name="totalQty"/>(Kgs)'+"###"+totalQty+"###"+'<s:text name="finalAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalAmt+"###"+'<s:text name="txnAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalPayAmt;
	</s:if>
	<s:if test="type == 'agent'">	
	var existQty=$("#existQty").text();
	var distQty=$("#distQty").text();
	var currentQty=$("#currentQty").text();
	<s:if test="currentTenantId=='chetna'">
	headerData='<s:text name="distQty"/>(<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+distQty;
	</s:if>
    <s:else>
	headerData='<s:text name="existQty"/>(Kgs)'+"###"+existQty+"###"+'<s:text name="distQty"/>(Kgs)'+"###"+distQty+"###"+'<s:text name="currentQty"/>(Kgs)'+"###"+currentQty;
	</s:else>	
	</s:if>
	jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
	jQuery("#detail").jqGrid('excelExport', {url: populatePDFAction});
}else{
    alert('<s:text name="export.nodata"/>');
}
}

var printWindowCnt=0;
var windowRef;
function printReceipt(receiptNo){	
	jQuery("#receiptNo").val(receiptNo);	
	var targetName="printWindow"+printWindowCnt;
	windowRef = window.open('',targetName);
	try{
		windowRef.referenceWindow = windowRef;		
	}catch(e){
	}
	jQuery("#receiptForm").attr("target",targetName);	
	jQuery("#receiptForm").submit();
	++printWindowCnt;
}

function onFilterData(){
	callAjaxMethod("farmerDistributionReport_populateFieldStaffList.action","fieldStaff");
	callAjaxMethod("farmerDistributionReport_populateLocationList.action","location");
	callAjaxMethod("farmerDistributionReport_populateFarmerList.action","farmerId");
	callAjaxMethod("farmerDistributionReport_populateProductList.action","productId");
	callAjaxMethod("farmerDistributionReport_populateSeasonList.action","seasonId");
	callAjaxMethod("farmerDistributionReport_populateStateList.action","stateName");
	//callAjaxMethod("farmerDistributionReport_populateWarehouseList.action","fpo");
	callAjaxMethod("farmerDistributionReport_populateIcsList.action","icsName");
	
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

function populateFarmerMethod() {
	var productId="";
	var farmerId="";
	var stateName="";
	var fpo="";
	var icsName="";
	var branchIdParma="";
	var subBranchIdParam="";
	var location=document.getElementById("location").value;
	var type='<%=request.getParameter("type")%>';
	var branch='<s:property value="getBranchId()"/>';
	var startDate=document.getElementById("hiddenStartDate").value;  
	var endDate=document.getElementById("hiddenEndDate").value;
	
	var farmerId= $("#farmerId").val();
	var seasonCode=document.getElementById("seasonId").value;
	if(tenant=='chetna')
	{
		stateName=document.getElementById('stateName').value;
		//fpo=document.getElementById('fpo').value;
		icsName=document.getElementById('icsName').value;
	}
	 
	var fieldStaff=document.getElementById('fieldStaff').value;
	if(branch=="")
	{
		branchIdParma=document.getElementById("branchIdParma").value
	}
/* 	var subBranchIdParam=document.getElementById("subBranchIdParam").value;
 */	jQuery.post("farmerDistributionReport_populateFarmerMethod.action", {location:location,selectedFieldStaff:fieldStaff,farmerId:farmerId,seasonCode:seasonCode,stateName:stateName,fpo:fpo,icsName:icsName,branchIdParma:branchIdParma,startDate:startDate,endDate:endDate,branch:branch},
			function(result) {
				//var valuesArr = $.parseJSON(result);
				var json = JSON.parse(result);
		 		$("#totalQty").html(json[0].totalQty);
		 		$("#totalAmt").html(json[0].totalAmt);
		 		$("#totalPayAmt").html(json[0].totalPayAmt);
			});
}

function populateAgentMethod(){
	var farmerId="";
	var stateName="";
	var fpo="";
	var icsName="";
	var productId="";
	var branchIdParma="";
	var location=document.getElementById("location").value;
	var type='<%=request.getParameter("type")%>';
	var branch='<s:property value="getBranchId()"/>';
	var startDate=document.getElementById("hiddenStartDate").value;  
	var endDate=document.getElementById("hiddenEndDate").value;
	var productId=document.getElementById("productId").value;
	var seasonCode=document.getElementById("seasonId").value;
	if(tenant=='chetna')
	{
		stateName=document.getElementById('stateName').value;
		//fpo=document.getElementById('fpo').value;
		icsName=document.getElementById('icsName').value;
	}
	 
	var fieldStaff=document.getElementById('fieldStaff').value;
	if(branch=="")
	{
		branchIdParma=document.getElementById("branchIdParma").value
	}
	jQuery.post("farmerDistributionReport_populateAgentMethod.action", {location:location,selectedFieldStaff:fieldStaff,productId:productId,seasonCode:seasonCode,stateName:stateName,fpo:fpo,icsName:icsName,branchIdParma:branchIdParma,startDate:startDate,endDate:endDate,branch:branch},
			function(result) {
				//var valuesArr = $.parseJSON(result);
				var json = JSON.parse(result);
		 		$("#existQty").html(json[0].existQty);
		 		$("#distQty").html(json[0].distQty);
		 		$("#currentQty").html(json[0].currentQty);
			});
	
}

function popupWindow(imgArr) {
	var id =imgArr;
	var carousel = $("<div>").addClass("carousel");
	try{
		var str_array = id.split(',');
		$("#mbody").empty();
		for(var i = 0; i < str_array.length; i++){
			var div = $("<div>");
		
			 var image = '<img src="farmerDistributionReport_populateDistributionImage.action?id='+str_array[i]+'"/>';
			div.append(image);
			carousel.append(div); 
			jQuery.post("farmerDistributionReport_populateLatAndLon.action", {id:str_array[i]},
					function(result) {
						//var valuesArr = $.parseJSON(result);
						var json = JSON.parse(result);
				 		$("#lat").html(json[0].lat);
				 		$("#lon").html(json[0].lon);
				 		//$("#currentQty").html(json[0].currentQty);
					});
		}
		
		$("#mbody").append(carousel);
		$( ".carousel" ).carousel(); 	 	
		document.getElementById("enableModal").click();	
	}catch(e){
		alert(e);
	}
}



var fProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var tProjection   = new OpenLayers.Projection("EPSG:900913");
var url = window.location.href;
var temp = url;
for(var i = 0 ; i < 1 ; i++) {
	temp = temp.substring(0, temp.lastIndexOf('/'));
	}
var href = temp;
var coordinateImg = "red_placemarker.png";
var iconImage = temp + '/img/' + coordinateImg;

// Variable relate to loading Custom Popup
var $overlays;
var $modals;
var $sliders;

function loadCustomPopup(){
	$overlays = $('<div id="modOverlay"></div>');
	$modals = $('<div id="modalWin" class="ui-body-c gmap3"></div>');
	$sliders = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
	$close = $('<button id="clsBtn" class="btnCls">X</button>');
	
	$modals.append($sliders, $close);
	$('body').append($overlays);
	$('body').append($modals);
	$modals.hide();
	$overlays.hide();

	jQuery("#clsBtn").click(function () {
    	$('#modalWin').css('margin-top','-230px');	
		$modals.hide();
		$overlays.hide();			
		$('body').css('overflow','visible');
	});
}


function enablePopup(head, cont){
	$(window).scrollTop(0); 
	$('body').css('overflow','hidden');
	$(".bjqs").empty();		
	var heading='';
	var contentWidth='100%';
	if(head!=''){
		heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
		contentWidth='92%';
	}
	var content="<div style='width:100%;height:"+contentWidth+"'>"+cont+"</div>";	
	$(".bjqs").append('<li>'+heading+content+'</li>')
	$(".bjqs-controls").css({'display':'block'});
	$('#modalWin').css('margin-top','-200px');
	$modals.show();
	$overlays.show();
	$('#banner-fade').bjqs({
        height      : 482,
        width       : 600,
        showmarkers : false,
        usecaptions : true,
        automatic : true,
        nexttext :'',
        prevtext :'',
        hoverpause : false                                           

    });
}






function buttonEdcCancel(){
	document.getElementById("model-close-edu-btn").click();	
 }
</script>
<button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
		<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body" id="mbody">
				</div>
		
			
				<div class="flexItem flex-layout flexItemStyle">
				<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
				<label for="txt" align="left" id="lat"></label><span>    </span>
</div>
<div class="flexItem flex-layout flexItemStyle">
				<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
				<label for="txt" align="right" id="lon"></label>
			</div> 
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonEdcCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
	<s:form name="form">

		<div class="appContentWrapper marginBottom">
			<section class="reportWrap row">
			<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('startingDate')}"/></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control  input-sm" />
					</div>
				</div>
				
			
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('profile.agent')}"/></label>
					<div class="form-element">
						<s:select name="selectedFieldStaff" id="fieldStaff"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div>
			
			
		 <%-- <div class="reportFilterItem">
					<label for="txt"><s:property value="%{getLocaleProperty('warehouse')}"/>	</label>
					<div class="form-element">
						<s:select name="location" id="location" list="prodCenterList"
							headerKey="" headerValue="%{getText('txt.select')}"
							 theme="simple" cssClass="form-control input-sm select2" />
					</div>
				</div>
				 --%>
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('warehouse')}"/></label>
					<div class="form-element">
						<s:select name="location" id="location" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							listKey="name" listValue="name" theme="simple"
							cssClass="form-control input-sm select2" />
					</div>
				</div> 
				
				<%-- <div class="reportFilterItem">
					<label for="txt"><s:property value="%{getLocaleProperty('warehouse')}"/></label>
					<div class="form-element">
						<s:select name="location" id="location" list="locationList"
							headerKey="" headerValue="%{getText('txt.select')}"
							listKey="name" listValue="name" theme="simple"
							cssClass="form-control input-sm select2" />
					</div>
				</div> --%>
				
				<s:if test="!(type.equals('agent'))">
					<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('farmer')}" /></label>
						<div class="form-element">
							<s:select name="filter.farmerId" id="farmerId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
				</s:if>
					<%-- <div class="reportFilterItem">
						<label for="txt"><s:property
								value="%{getLocaleProperty('fatherName')}" /></label>
						<div class="form-element">
							<s:select name="filter.fatherName" id="fatherName"
								list="fatherNameList" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2"  />
						</div>
					</div> --%>

					<s:if test="(type.equals('agent'))">
					 <div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('product')}" /></label>
						<div class="form-element">
							<s:select name="filter.product" id="productId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div> 
					</s:if>

				

				<div class="filterEle">
					<label for="txt"><s:text name="season" /></label>
					<div class="form-element">
						<s:select name="filter.seasonCode" id="seasonId"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
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
				<!-- Commented as per bug id : 2273 -->
				<%-- <div class="filterEle">
					<label for="txt"><s:property
						value="%{getLocaleProperty('cooperative')}" /></label>
					<div class="form-element">
						<s:select name="fpo" id="fpo" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div> --%>
				<div class="filterEle">
					<label for="txt"><s:text name="icsName" /></label>
					<div class="form-element">
					<s:select name="icsName" id="icsName" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				<s:if test="currentTenantId=='susagri'">
				<div class="filterEle">
					<label for="txt"><s:property
						value="%{getLocaleProperty('samithi')}" /></label>
					<div class="form-element">
						<s:select name="samithi" id="samithi" list="samithiList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>

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





	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
		<s:if test="currentTenantId!='pratibha' && currentTenantId!='wub'">
		<s:if test="type == 'farmer'">		
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="totalQty"></span> <s:text name="%{getLocaleProperty('totalQty')}"/>&nbsp;(kgs)</span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="totalAmt"></span> <s:text name="%{getLocaleProperty('finalAmt')}"/>&nbsp;<i>(<s:property value="%{getCurrencyType().toUpperCase()}" />)</i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="totalPayAmt"></span> <s:text name="%{getLocaleProperty('txnAmt')}"/>&nbsp;<i>(<s:property value="%{getCurrencyType().toUpperCase()}" />)</i></span>
					</div>
					
				</div>
			</div>
			</s:if>
			<s:if test="type == 'agent'">
			 
			 <div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
				<s:if test="currentTenantId!='chetna'">
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="existQty"></span> <s:text name="%{getLocaleProperty('existQty')}"/>&nbsp;(Kgs)</span>
					</div>
					</s:if>
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="distQty"></span> <s:text name="%{getLocaleProperty('distQty')}"/>&nbsp;(Kgs)</span>
					</div>
					<s:if test="currentTenantId!='chetna'">
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="currentQty"></span> <s:text name="%{getLocaleProperty('currentQty')}"/>&nbsp;(Kgs)</span>
					</div>
					</s:if>
				</div>
			</div>
			 </s:if>
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

			<div style="width: 99%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>

		</div>
	</div>



	
	
	
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>

	<s:form name="detailform">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
	<s:form action="farmerDistributionReport_populatePrintHTML"
		id="receiptForm" method="POST" target="printWindow">
		<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
	</s:form>
</body>

