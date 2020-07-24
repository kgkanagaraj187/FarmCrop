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
var farmerId='';
var fatherName='';
var buyerInfo='';
var cropType='';
var icsName='';
var branchIdParma='';
var selectedFieldStaff='';
var recordLimit='<s:property value="exportLimit"/>';

	jQuery(document).ready(function(){
		 jQuery(".well").hide();
		 onFilterData();
		//document.getElementById('fieldl').selectedIndex=0;	
		  $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
			$('.applyBtn').click();
		document.getElementById('farmerId').selectedIndex=0;
		<s:if test="currentTenantId=='welspun'">
		document.getElementById('villageName').selectedIndex=0;
		</s:if>
		//document.getElementById('fatherName').selectedIndex=0;
		document.getElementById('buyerInfo').selectedIndex=0;
//	document.getElementById('cropType').selectedIndex=0;
		document.getElementById('seasonId').selectedIndex=0;
		document.getElementById('fieldStaff').selectedIndex=0;
		<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
		document.getElementById('icsName').selectedIndex=0;
		</s:if>
		 <s:if test="currentTenantId=='chetna'">
			document.getElementById('stateName').selectedIndex=0;
			document.getElementById('fpo').selectedIndex=0;
			document.getElementById('icsNameId').selectedIndex=0;
			</s:if>
		<s:if test='branchId==null'>
		document.getElementById('branchIdParma').selectedIndex=0;
		</s:if>
		/* if('<s:property value="getCurrentTenantId()"/>'=='pratibha'  && '<s:property value="getBranchId()"/>' =='bci'){
			$("#fieldl option[value='8']").remove();
			} */
			populateQtyAmt();
		jQuery("#detail").jqGrid(
				{
					url:'cropSaleReport_detail.action?',
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
				  "farmerId" : function(){	
				  	  return document.getElementById("farmerId").value;
				  },
				  "selectedFieldStaff" : function(){			  
						return  document.getElementById('fieldStaff').value;
					  },
				  "buyerInfo" : function(){
					  return document.getElementById("buyerInfo").value;
				  },
				  "seasonCode" : function(){			  
	 					  return document.getElementById("seasonId").value;
	 	 			      },
				  /* "cropType": function(){
					  return document.getElementById('cropType').value;
				  }, */
				  <s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
				   "icsName": function(){
					  return document.getElementById('icsName').value;
				  }, 
				  </s:if>
				  <s:if test="currentTenantId=='chetna'">
				  "stateName" : function(){
				  return  document.getElementById('stateName').value;
				  },
				  "fpo" : function(){
					  return  document.getElementById('fpo').value;
					  },
					  "icsNameId": function(){
						  return document.getElementById('icsNameId').value;
					  },
					 
				  </s:if>
					  <s:if test="currentTenantId=='welspun'">
					"batchLotNo" : function(){						
					 return  document.getElementById("batchLotNo").value;
					 },  
					 "poNumber" : function(){						
						 return  document.getElementById("poNumber").value;
						 },  
						 
						 "villageName" : function(){						
							 return  document.getElementById("villageName").value;
							 }, 
					 </s:if>	
					
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
                            '<s:text name="date"/>',    
  					 	    '<s:text name="cSeasonCode"/>',
  					 	 '<s:property value="%{getLocaleProperty('agentId')}" />',
                           '<s:property value="%{getLocaleProperty('farmer')}" />',
                           '<s:property value="%{getLocaleProperty('farmerCode')}" />',
                           //'<s:text name="fatherName"/>',
	                         '<s:text name="farm"/>',
	                         '<s:property value="%{getLocaleProperty('village.name')}" />',
	                         '<s:text name="buyerName"/>',
	                        /* '<s:text name="cropType"/>',
	                         '<s:text name="cropName"/>',*/
	                         <s:if test="currentTenantId=='chetna'">
					  		 
					  		  	'<s:text name="icsName"/>',
									   </s:if> 
					  		  <s:if test="currentTenantId=='welspun'">
					  		  '<s:property value="%{getLocaleProperty('Batchno')}" />',
					  		  '<s:property value="%{getLocaleProperty('ponumber')}" />',
					  		 </s:if>
					  		  '<s:text name="receipt"/>',
	                           '<s:text name="%{getLocaleProperty('salesPrice')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                           <s:if test="currentTenantId!='pratibha'"> 
	                           '<s:text name="%{getLocaleProperty('totalSaleQty')}"/>',
	                           </s:if>
	                           <s:if test="currentTenantId=='pratibha'">
					  		   	 '<s:text name="%{getLocaleProperty('totalQtyMT')}"/>',
					  		   	</s:if>
	                         '<s:text name="%{getLocaleProperty('totalSale')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                         '<s:text name="%{getLocaleProperty('transporterName')}"/>',
	                         '<s:text name="vechileNo"/>',
	                         '<s:text name="%{getLocaleProperty('paymentInfo')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
	                         <s:if test="currentTenantId=='welspun'"> 
	                         '<s:text name="%{getLocaleProperty('farmcoordinates')}"/>',
	                     	 </s:if>
	                         '<s:text name="image"/>'
		 	      	 ],
		 	      	colModel:[
		 	      	          
		 	      	   	<s:if test='branchId==null'>
				   		{name:'branchId',index:'branchId',sortable: false,search:true,frozen:true,stype: 'select',searchoptions: {
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
				   		
				   			{name:'subBranchId',index:'subBranchId',sortable: false,frozen:true,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>
		 					    {name:'dateOfSale',index:'dateOfSale',sortable:false,frozen:true},	
		 						{name:'cSeasonCode',index:'cSeasonCode',sortable:false,frozen:true},
		 						{name:'agentId',index:'agentId',sortable:false,frozen:true},
		 					    {name:'farmerId',index:'farmerId',sortable:false,frozen:true},
		 					   {name:'farmerCode',index:'farmerCode',sortable:false,frozen:true},
		 					 //  {name:'lastName',index:'lastName',width:125,sortable:false},
		 					    {name:'farmId',index:'farmId',sortable:false},	
		 					    {name:'village',index:'village',sortable:false},
		 					   {name:'buyerName',index:'buyerName',align:'left',sortable:false},
		 					   	/*{name:'ct.cropType',index:'ct.cropType',width:150,sortable:false},
		 					   {name:'ct.cropName',index:'batch',align:'ct.cropName',width:150,sortable:false},*/
		 					  <s:if test="currentTenantId=='chetna'">							  
							    {name:'icsName',index:'icsName',sortable:false},
								   </s:if> 
							    <s:if test="currentTenantId=='welspun'">
							    {name:'batch',index:'batch',align:'right',sortable:false},
							    {name:'ponumber',index:'ponumber',align:'right',sortable:false},
							    </s:if> 
							    {name:'receipt',index:'receipt',align:'right',sortable:false},
			 					   {name:'salesPrice',index:'salesPrice',align:'right',sortable:false},
			 					  <s:if test="currentTenantId!='pratibha'"> 
			 					   {name:'qty',index:'qty',align:'right',sortable:false},
			 					   </s:if>
			 					   <s:if test="currentTenantId=='pratibha'">
									{name:'totalQtyMT',index:'totalQtyMT',sortable:false,align:'right'},
					  		   	</s:if>
			 					    {name:'totalSale',index:'totalSale',align:'right',sortable:false},
			 					   {name:'transporterName',index:'transporterName',align:'right',sortable:false},
			 					   {name:'vechileNo',index:'price',align:'right',sortable:false},
			 					  	{name:'paymentInfo',index:'paymentInfo',align:'right',sortable:false},
			 					   <s:if test="currentTenantId=='welspun'">
			 					  	{name:'farmcoordinates',index:'farmcoordinates',align:"center",sortable:false},
			 					   </s:if>
			 					  	{name:'image',index:'image',align:"center",sortable:false},
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
							url:'cropSaleReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
						   	      
                                    '<s:text name="cropType"/>',
									'<s:text name="cropName"/>',
									'<s:text name="variety"/>',
                                    '<s:text name="grade"/>',
                                    '<s:text name="unit"/>',
                                    '<s:property value="%{getLocaleProperty('Batchno')}" />',
                                    '<s:text name="pricess"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                                    //'<s:text name="%{getLocaleProperty('prices')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
                                    '<s:text name="quantity"/>',
                                    <s:if test="currentTenantId=='pratibha'">
                                    '<s:text name="%{getLocaleProperty('quantityMT')}"/>',
                                    </s:if>
                                    '<s:text name="saleValue"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'
                                     ],				 	      	 
						   	colModel:[
									{name:'cropType',index:'cropType',sortable:false},
									{name:'cropName',index:'cropName',sortable:false},
									{name:'variety',index:'variety',sortable:false},
									{name:'grade',index:'grade',sortable:false},
									{name:'unit',index:'unit',sortable:false},
									{name:'batch',index:'batch',align:'right',sortable:false},
									{name:'prices',index:'prices',align:'right',sortable:false},
									{name:'quantity',index:'quantity',align:'right',sortable:false},
									 <s:if test="currentTenantId=='pratibha'">
									{name:'quantityMT',index:'quantityMT',align:'right',sortable:false},
									</s:if>
									{name:'totalPrice',index:'totalPrice',align:'right',sortable:false}	
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
	
	function populateQtyAmt(){
		var icsName="";
		 var stateName="";
		 var fpo="";
		var farmerId=document.getElementById("farmerId").value;
		var selectedFieldStaff=document.getElementById("fieldStaff").value;
		var branch='<s:property value="getBranchId()"/>';
		var subBranchIdParmas='<s:property value="getBranchId()"/>';
		var startDate=document.getElementById("hiddenStartDate").value;  
		var endDate=document.getElementById("hiddenEndDate").value;
		var buyerInfo=document.getElementById("buyerInfo").value;
		var seasonCode=document.getElementById("seasonId").value;
		<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
		var icsName=document.getElementById("icsName").value;
		</s:if>
		 <s:if test="currentTenantId=='chetna'">
		 var stateName=document.getElementById("stateName").value;
		 var fpo=document.getElementById("fpo").value;
		 
			</s:if>
			if(branch=="")
			{
				branchIdParma=document.getElementById("branchIdParma").value;
				
			}
			if(subBranchIdParmas=="")
			{
				subBranchIdParmas=document.getElementById("subBranchIdParam").value;
			}
			jQuery.post("cropSaleReport_populateQtyAmt.action", {farmerId:farmerId,seasonCode:seasonCode,buyerInfo:buyerInfo,stateName:stateName,fpo:fpo,icsName:icsName,branchIdParma:branchIdParma,startDate:startDate,endDate:endDate,selectedFieldStaff:selectedFieldStaff,subBranchIdParma:subBranchIdParmas},
					function(result) {
						var json = JSON.parse(result);
				 		 $("#totalQty").html(json[0].totalQty);
				 		$("#totalAmt").html(json[0].totalAmt);
				 		$("#totalPayAmt").html(json[0].totalPayAmt); 
					});	
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
				farmerId = document.getElementById("farmerId").value;
				selectedFieldStaff = document.getElementById("fieldStaff").value;
				farmId = document.getElementById("buyerInfo").value;
		   jQuery("#detail").jqGrid('setGridParam',{url:"cropSaleReport_detail.action?",page:1}).trigger('reloadGrid');
		   }
			populateQtyAmt();
		}	
		function clear(){
			resetReportFilter();
			<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
			document.getElementById("icsName").value = "";
			 </s:if>
			 <s:if test="currentTenantId=='welspun'">
			 document.getElementById("batchLotNo").value ="";
			 document.getElementById("poNumber").value ="";
			 </s:if>
			document.form.submit();
		}
	

	function exportXLS(){
		var headerData =null;
		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){
			var totalQty=$("#totalQty").text();
			var totalAmt=$("#totalAmt").text();
			var totalPayAmt=$("#totalPayAmt").text();
			headerData='<s:text name="totalQty"/>(kgs)'+"###"+totalQty+"###"+'<s:text name="totalAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalAmt+"###"+'<s:text name="txnAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalPayAmt;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			jQuery("#detail").jqGrid('excelExport', {url: 'cropSaleReport_populateXLS.action?'});
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
			var totalQty=$("#totalQty").text();
			var totalAmt=$("#totalAmt").text();
			var totalPayAmt=$("#totalPayAmt").text();
			headerData='<s:text name="totalQty"/>(kgs)'+"###"+totalQty+"###"+'<s:text name="totalAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalAmt+"###"+'<s:text name="txnAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalPayAmt;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			jQuery("#detail").jqGrid('excelExport', {url: 'cropSaleReport_populatePDF.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
	function onFilterData(){
		callAjaxMethod("cropSaleReport_populateSeasonList.action","seasonId");
		callAjaxMethod("cropSaleReport_populateFarmerList.action","farmerId");
		callAjaxMethod("cropSaleReport_populateBuyerInfoList.action","buyerInfo");
		callAjaxMethod("cropSaleReport_populateICSList.action","icsNameId");
		callAjaxMethod("cropSaleReport_populateStateList.action","stateName");
		callAjaxMethod("cropSaleReport_populateWarehouseList.action","fpo");
		callAjaxMethod("cropSaleReport_populateAgentList.action","fieldStaff");
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
	
	
	 var imgID = "";
		function popupWindow(id, imageId) {
			try{
				var str_array = imageId.split(',');
				
				$("#mbody").empty();
				
				var mbody="";
				
				for(var i = 0; i < str_array.length; i++){
					var str_imageCode = str_array[i].split('/');
					
					if(i==0){
						mbody="";
						mbody="<div class='item active'>";
						mbody+='<img src="cropSaleReport_detailImage.action?id='+str_imageCode[0]+'"/>';
						mbody+="</div>";
					}else{
						mbody="";
						mbody="<div class='item'>";
						mbody+='<img src="cropSaleReport_detailImage.action?id='+str_imageCode[0]+'"/>';
						mbody+="</div>";
					}
					$("#mbody").append(mbody);
				}
				
				
				document.getElementById("enableModal").click();	
			}
			catch(e){
				
				alert(e);
				}
			
		}
		
		function buttonEdcCancel(){
			//refreshPopup();
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
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						 <div class="carousel-inner" role="listbox" id="mbody">
						 	
						 </div>
						 
						 <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
						      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						      <span class="sr-only">Previous</span>
   						 </a>
					    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
					      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					      <span class="sr-only">Next</span>
					    </a>
					    
					</div>
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
	
	<s:form name="form" action="cropSaleReport_list">
	<div class="appContentWrapper marginBottom">
			
			<section class='reportWrap row'>
				<div class="gridly">
                 
				<div class="filterEle">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control input-sm" />
					</div>
				</div>
				
			
				<div class="filterEle">
					<label for="txt"><s:text name="cSeasonCode" /></label>
					<div class="form-element">
						<s:select name="filter.seasonCode" id="seasonId" list="{}"
				headerKey="" headerValue="%{getText('txt.select')}"
				cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('farmer')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmerId" id="farmerId" list="{}"
				cssClass="form-control input-sm select2" headerKey=""
				headerValue="%{getText('txt.select')}"  />
					</div>
				</div>
				
				 <s:if test="currentTenantId=='welspun'">
					<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('village.name')}" /></label>
					<div class="form-element">
						<s:select name="village.name" id="villageName" list="villageMap"
				cssClass="form-control input-sm select2" headerKey=""
				headerValue="%{getText('txt.select')}"  />
					</div>
				</div>
				</s:if>
				
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
					<label for="txt"><s:property value="%{getLocaleProperty('fatherName')}" /></label>
					<div class="form-element">
						<s:select name="filter.fatherName" id="fatherName"
				list="fathersNameList" headerKey=""
				headerValue="%{getText('txt.select')}" class="form-control input-sm select2"  />
					</div>
				</div> --%>
				
				<div class="filterEle">
					<label for="txt"><s:text name="buyerName" /></label>
					<div class="form-element">
					<s:select name="filter.buyerInfo.customerId" id="buyerInfo"
				list="{}" cssClass="form-control input-sm select2" headerKey=""
				headerValue="%{getText('txt.select')}"  />
					</div>
				</div>
				
				 <s:if test="currentTenantId=='welspun'">
				<div class="filterEle">
			<label for="txt"><s:property value="%{getLocaleProperty('Batchno')}"/> </label>
			<div class="form-element">
			<s:textfield name="batchLotNo" id="batchLotNo" maxlength="35"
				cssClass="form-control" />
				</div>
			</div>
				<div class="filterEle">
			<label for="txt"><s:property value="%{getLocaleProperty('ponumber')}"/> </label>
			<div class="form-element">
			<s:textfield name="poNumber" id="poNumber" maxlength="35"
				cssClass="form-control" />
				</div>
			</div>
			</s:if>
				<s:if test="currentTenantId=='pratibha' && getBranchId()!='bci'">
				<div class="filterEle">
					<label for="txt"><s:text name="farmer.icsName" /></label>
					<div class="form-element">
					<s:textfield name="icsName" id="icsName" class="upercls form-control" list="{}"
									headerKey="" headerValue="%{getText('txt.select')}"
									listKey="key" listValue="value"/>
					</div>
				</div>
				</s:if>
			 <s:if test="currentTenantId=='chetna'">
				<div class="filterEle">
					<label for="txt"><s:text name="icsName" /></label>
					<div class="form-element">
					<%-- <s:select name="icsNameId" id="icsNameId" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" /> --%>
					
					<s:select name="icsNameId" id="icsNameId" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					
					</div>
				</div>
				</s:if> 
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
				<div class="filterEle hide">
					<label for="txt"><s:property
						value="%{getLocaleProperty('cooperative')}" /></label>
					<div class="form-element">
						<s:select name="fpo" id="fpo" list="{}"
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
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm" aria-hidden="true"
						 id="clear">
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
					   <span><span class="strong" id="totalQty"></span> <s:text name="totalQty"/></span>&nbsp;(kgs)</span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="totalAmt"></span> <s:text name="finalAmt"/>&nbsp;<i>(<s:property value="%{getCurrencyType().toUpperCase()}" />)</i></span>
					</div>
					<div class="report-summaryBlockItem">
					<span><span class="strong" id="totalPayAmt"></span> <s:text name="txnAmt"/>&nbsp;<i>(<s:property value="%{getCurrencyType().toUpperCase()}" /></i>)</span>
					</div>
				
				</div>
			</div>
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
	

	<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
		
	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
