<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
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
</head>

<body>
	<script type="text/javascript">
	var farmerId='';
	var productId='';
var recordLimit='<s:property value="exportLimit"/>';
	jQuery(document).ready(function(){
		onFilterData();
		loadFields();
		//document.getElementById('productId').selectedIndex=0;
		jQuery(".well").hide();
		/* if('<s:property value="currentTenantId"/>'=='lalteer' || '<s:property value="currentTenantId"/>'=='kpf'){
		$('.showProp').show();
		}
		else{
			$('.showProp').hide();
		} */	
		$('.showProp').show();
		if('<s:property value="currentTenantId"/>'!='lalteer'){
		$("#fieldl option[value='Farmer']").remove();
		}
		jQuery("#detail").jqGrid(
				{
					url:'procurementStockReport_detail.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				    styleUI : 'Bootstrap',
				   	mtype: 'POST',				   	
				   	postData:{				 
						  "filter.coOperative.id" : function(){
							  return $('#coOperative').val();
						  },
						  
						 /*  "filter.farmer.farmerId" : function(){
							  return document.getElementById("farmerId").value;
							 //return $('#farmerId').val();
						  }, */
						 
						  "filter.procurementProduct.id" : function(){
							  return document.getElementById("productId").value;
						  },
						 
						  <s:if test='branchId==null'>
						    "branchIdParma" : function(){			  
	 	 					  return document.getElementById("branchIdParma").value;
	 	 	 			      } 
						  </s:if>
						  
					},
				   	colNames:[
				   	           <s:if test='branchId==null'>
                                      '<s:text name="%{getLocaleProperty('app.branch')}"/>',
                               </s:if>
                               <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>

                               '<s:text name="app.subBranch"/>',
                                </s:if>
				  		 //  	  '<s:text name="co.code"/>',
				  		  '<s:property value="%{getLocaleProperty('co.name')}"/>	',
				  		   	 
				  		  //  <s:if test="currentTenantId!='lalteer'">
				  		  // 	  '<s:text name="pp.code"/>',
				  		 //   </s:if>
				  		   	  '<s:text name="pp.name"/>',
				  		    <s:if test="currentTenantId=='lalteer'">
				  		   	'<s:text name="f.name"/>',
				  		   	  </s:if>
				  		  '<s:text name="unit"/>',
				  		  <s:if test="currentTenantId!='pratibha'">
				  		   	'<s:property value="%{getLocaleProperty('noofBags')}"/>	',
				  		   	</s:if>
				  		   	 // '<s:text name="noOfBags"/>',
				  		 	'<s:property value="%{getLocaleProperty('netWeight')}"/>'
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
						 <s:if test="currentTenantId=='lalteer'">
					//	{name:'co.code',index:'co.code',sortable:false},
						{name:'co.name',index:'co.name',sortable:false},
						//{name:'pp.code',index:'pp.code',width:200,sortable:false},
						
						{name:'f.name',index:'f.name',sortable:false},
						
						{name:'pp.name',index:'pp.name',sortable:false},
						 <s:if test="currentTenantId!='pratibha'">
						{name:'noOfBags',index:'noOfBags',sortable:false,align:'left'},
						</s:if>
						{name:'netWeight',index:'netWeight',sortable:false,align:'left'}	
						  </s:if>
						  <s:else>
						//  {name:'co.code',index:'co.code',sortable:false},
							{name:'co.name',index:'co.name',sortable:false},
						//	{name:'pp.code',index:'pp.code',sortable:false},
							{name:'pp.name',index:'pp.name',sortable:false},
							{name:'unit',index:'unit',sortable:false},
							 <s:if test="currentTenantId!='pratibha'">
							{name:'noOfBags',index:'noOfBags',sortable:false,align:'right'},
							</s:if>
							{name:'netWeight',index:'netWeight',sortable:false,align:'right'}	
						  </s:else>
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   //autowidth:true,
				   shrinkToFit:true,
				   scrollOffset: 19,     
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
							url:'procurementStockReport_populateSubGridDetails.action',
						   	pager: pager_id,
						   	datatype: "json",
							mtype: 'POST',				   	
				 		   	postData:{				 
								  "filter.coOperative.id" : function(){
									  return coOperativeId;
								  }, 
								 "filter.procurementProduct.id" : function(){
									  return procurementProductId;
								  }, 
								   "filter.farmer.id" : function(){
									  if(farmerId=='undefined'){
									  	return farmerId;
									  }else{
										  return "";
									  }
								  } 
							},	 
						   	colNames:[	
						   	       	  '<s:text name="variety"/>',	
						   	       <s:if test="currentTenantId!='lalteer'">
						   	          '<s:text name="grade"/>',
						   	          </s:if>
						   	       <s:if test="currentTenantId!='pratibha'">
						   	       '<s:property value="%{getLocaleProperty('noofBags')}"/>	',
						   	       </s:if>
						  		      //'<s:text name="noOfBags"/>',						  		      
						   	    '<s:property value="%{getLocaleProperty('netWeight')}"/>'				  			 
				 	      	 ],
						   	colModel:[	
									{name:'variety',index:'variety',sortable:false},
									 <s:if test="currentTenantId!='lalteer'">
		   							{name:'grade',index:'grade',sortable:false},
		   							</s:if>
		   						 <s:if test="currentTenantId!='pratibha'">
		   							{name:'noOfBags',index:'noOfBags',sortable:false,align:'right'},
		   							</s:if>
		   							{name:'netWeight',index:'netWeight',sortable:false,align:'right'}		   		
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    autowidth: true,
						    shrinkToFit:true,
						    rowNum:10,
							rowList : [10,25,50],
						    viewrecords: true				
					   });
					  //jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
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
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	
		
		function reloadGrid(){
			jQuery("#detail").jqGrid('setGridParam',{url:"procurementStockReport_detail.action?",page:1}).trigger('reloadGrid');
			
			productId = document.getElementById("productId").value;
			
			loadFields();
		}

		function clear(){
			document.form.action="procurementStockReport_list.action";
			document.getElementById('coOperative').selectedIndex=0;
			document.getElementById('productId').selectedIndex=0;
			document.form.submit();
		}

		

		
	});
	
	
	function loadCentralWarehouse(){
		$.post("procurementStockReport_populateCentralWarehouse",{dt:new Date()},function(data){
			if(data == null || data == ""){
				alert('<s:text name="noRecordFoundForCentralWarehouse"/>')
			}else{
			enableRestartAlert(data);
			}
		});

	}	
	function enableRestartAlert(data){	
		
		$('body').css('overflow','hidden');
		$('#pendingRestartAlertErrMsg').html('');
		$('#popupBackground').css('width','100%');
		$('#popupBackground').css('height',getWindowHeight());
		$('#popupBackground').css('top','0');
		$('#popupBackground').css('left','0');
		$('#popupBackground').show();
		$('#restartAlert').css({top:'50%',left:'50%',margin:'-'+($('#restartAlert').height() / 2)+'px 0 0 -'+($('#restartAlert').width() / 2)+'px'});
		$('#restartAlert').show();
		
		var newRow="<thead><tr><th colspan='3'><s:text name='centralWarehouse' /></th></tr><tr bgcolor='#E8FAE6'><th><s:text name='qualitty' /></th><th><s:text name='noOfBags' /></th><th><s:text name='netWeight' /></th></tr></thead><tbody>"+data+"</tbody>";
		
		$('#tableData').append(newRow); 
		jQuery("#tableData").css('height','170px');
		window.location.hash="#restartAlert";
		
	}
	function getWindowHeight(){
		var height = document.documentElement.scrollHeight;
		if(height<$(document).height())
			height = $(document).height();
		return height;
	}

	function disableExtendAlert(){
		$('#pendingRestartAlertErrMsg').html('');
		$('#popupBackground').hide();
		$('#restartAlert').hide();
		$('body').css('overflow','');
		jQuery('#tableData > thead').html('');
		jQuery('#tableData > tbody').html('');
		clear();
		
	}
	
	function exportXLS(){
		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){
			
			var totalNoBags=$("#totalNoBags").text();
			var totalNetWegt=$("#totalNetWegt").text();
			headerData='<s:property value="%{getLocaleProperty('noOfBags')}"/>	'+"###"+totalNoBags+"###"+'<s:text name="netWeight"/>(kgs)'+"###"+totalNetWegt;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			
			jQuery("#detail").jqGrid('excelExport', {url: 'procurementStockReport_populateXLS.action'});
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
			var totalNoBags=$("#totalNoBags").text();
			var totalNetWegt=$("#totalNetWegt").text();
			headerData='<s:property value="%{getLocaleProperty('noOfBags')}"/>	'+"###"+totalNoBags+"###"+'<s:text name="netWeight"/>(kgs)'+"###"+totalNetWegt;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			jQuery("#detail").jqGrid('excelExport', {url: 'procurementStockReport_populatePDF.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
	function onFilterData(){
		callAjaxMethod("procurementStockReport_populateCooperativeList.action","coOperative");
		callAjaxMethod("procurementStockReport_populateProductList.action","productId");
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
	
	
	function loadFields(){
		var selectedCoOperative=document.getElementById("coOperative").value;
		var productId=$("#productId").val();
		
		var branchIdParma="";
		var branch='<s:property value="getBranchId()"/>';
		if(branch=="")
		{
			branchIdParma=document.getElementById("branchIdParma").value
		}
	 	$.post("procurementStockReport_populateHeaderFields",{selectedCoOperative:selectedCoOperative,productId:productId,branchIdParma:branchIdParma,branch:branch},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalNoBags").html(json[0].totalNoBags);
	 		$("#totalNetWegt").html(json[0].totalNetWegt);
	 			
	 	});
	}
	

</script>
	<s:form name="form" action="procurementStockReport_list">
	
	<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
						<label for="txt"><s:property
						value="%{getLocaleProperty('cooperative')}" /></label>
						<div class="form-element">
							<s:select name="coOperative" id="coOperative" list="{}"
					 headerKey="" headerValue="%{getText('txt.select')}" class="form-control input-sm select2" />
						</div>
				</div>
				<%-- 
				<div class="filterEle showProp">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
								<s:select name="farmerId" id="farmerId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
				</div>
				 --%>
				
				<div class="filterEle showProp">
						<label for="txt"><s:text name="Product" /></label>
						<div class="form-element">
								<s:select name="Productname" id="productId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
				</div>
				
				<s:if test='branchId==null'>
				<div class="filterEle">
						<label for="txt"><s:text name="app.branch" /></label>
						<div class="form-element">
							<s:select name="branchIdParma" id="branchIdParma"
						list="branchesMap" headerKey="" headerValue="%{getText('txt.select')}"
						class="form-control input-sm select2" />
						</div>
				</div>
				</s:if>
				
				<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
					<div class="filterEle">
						<label for="txt"><s:text name="app.branch" /></label>
						<s:select name="branchIdParam" id="branchIdParam"
							list="parentBranches" headerKey="" headerValue="Select"
							cssClass="input-sm form-control select2" 
							onchange="populateChildBranch(this.value)" />
							</div>
					</s:if>
					<div class="filterEle">
					<label for="txt"><s:text name="app.subBranch" /></label>
					<s:select name="subBranchIdParam" id="subBranchIdParam"
						list="subBranchesMap" headerKey="" headerValue="Select"
						cssClass="input-sm form-control select2"  />
						</div>
				</s:if>
				
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
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
					<s:if test="currentTenantId!='pratibha'">
					<div class="report-summaryBlockItem">
						<span> <s:property value="%{getLocaleProperty('noofBags')}" />&nbsp;<span class="strong" id="totalNoBags"></span>&nbsp;</span>
					</div>
					</s:if>
					<div class="report-summaryBlockItem">
						<span><s:property value="%{getLocaleProperty('netWeight')}" />&nbsp;<span class="strong" id="totalNetWegt"></span><s:if test="currentTenantId!='pratibha'">&nbsp;kgs</s:if></span>
					</div>
					
					
				</div>
			</div>

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
	

	<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
		
	</div>
	<div id="restartAlert" class="popStockAlert">
		<div class="popClose" onclick="disableExtendAlert()"></div>
		<div id="pendingRestartAlertErrMsg" class="popPendingMTNTAlertErrMsg"
			align="center"></div>
		<div id="defaultRestartAlert" style="display: block;">

			<table align="center" id="tableData" cellspacing="0" border="1"
				style="text-align: center">
			</table>
		</div>
	</div>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
