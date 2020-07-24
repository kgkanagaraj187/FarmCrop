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
	<div class="row" style="margin-left: 0.5%">

		<s:form name="form" action="fieldStaffStockReport_list">
			<s:hidden name="enableBatchNo" />
			<div class="appContentWrapper marginBottom">

				<section class='reportWrap row'>
					<div class="gridly">
						<div class="filterEle">
							<label for="txt"><s:property
									value="%{getLocaleProperty('profile.agent')}" /> </label>
							<div class="form-element">
								<s:select name="fstaff" id="fstaff" list="{}"
									headerKey="" headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>


						<div class="filterEle">
							<label for="txt"><s:text name="name.product" /></label>
							<div class="form-element">
								<s:select name="product" id="productId" list="{}"
									headerKey="" headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>

						<div class="filterEle">
							<label for="firstName"><s:text name="name.season" /></label>
							<div class="form-element">
								<s:select name="filter.seasonCode" id="seasonId"
									list="{}" headerKey=""
									headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>
						<!-- 	organization hide -->
						<div class="filterEle hide">
							<label for="firstName"><s:text name="app.branch" /></label>
							<div class="form-element">
								<s:select name="branchIdParma" id="branchIdParma"
									list="branchesMap" headerKey=""
									headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>

						<div class="filterEle" style="margin-top: 2%; margin-right: 0%;">

							<button onclick="loadGrid()" type="button"
								class="btn btn-success btn-sm" id="generate" aria-hidden="true">
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


				<!--<div class="flexItem text-right flex-right">
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
	</div>


	<script type="text/javascript">
jQuery(document).ready(function(){
	onFilterData();
    document.getElementById('fstaff').selectedIndex=0;
	document.getElementById('productId').selectedIndex=0;
	<s:if test='branchId==null'>
    document.getElementById('branchIdParma').selectedIndex=0;
    </s:if>
	
  	var enableBatch="<s:property value='getEnableBatchNo()'/>";
  	var isMultiBranch ="<s:property value='isMultiBranch'/>";
  
  	if(isMultiBranch=='1'){
  		jQuery(".branchHide").hide();
  	}
  	
    if(enableBatch=='1'){
    	loadGrid();
    }else{
    	loadGridWithoutBatch();
    }
    
	jQuery("#generate").click( function() {
		reloadGrid();	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	

	function reloadGrid(){
	jQuery("#detail").jqGrid('setGridParam',{url:"fieldStaffStockReport_detail.action?",page:1}).trigger('reloadGrid');
	}

	function clear(){		
		document.form.submit();
	}
	
	jQuery("#clear").click( function() {
		window.location.href="fieldStaffStockReport_list.action";
	});
	
	
	jQuery("#minus").click( function() {
		if(document.getElementById(document.getElementById("fieldl").selectedIndex)!=null && (document.getElementById(document.getElementById("fieldl").selectedIndex).className=="" || document.getElementById(document.getElementById("fieldl").selectedIndex).className==" ")){
			removeFields();
			var divOption = document.getElementById("fieldl");
			divOption.selectedIndex=0;
			reloadGrid();
			}
	});

	jQuery("#plus").click( function() {
		showFields();
	});
	
	function onFilterData(){
		callAjaxMethod("fieldStaffStockReport_populateProductList.action","productId");
		callAjaxMethod("fieldStaffStockReport_populateSeasonList.action","seasonId");
		callAjaxMethod("fieldStaffStockReport_populateFieldStaffList.action","fstaff");
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

});


function loadGrid(){
	jQuery("#detail").jqGrid(
			{ 
				url:'fieldStaffStockReport_detail.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			   	mtype: 'POST',	
			    styleUI : 'Bootstrap',
			   	postData:{	
				 "fstaff" : function(){			  
				      return document.getElementById("fstaff").value;
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
 					  }
 					  </s:if>
			    },
			  	colNames:[
			  	          
						   <s:if test='branchId==null'>
					      '<s:text name="app.branch"/>',
						   </s:if>
					  	<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						
							'<s:text name="app.subBranch"/>',    
						</s:if>
						   '<s:property value="%{getLocaleProperty('fsCodes')}"/>	',
			  		   	  '<s:property value="%{getLocaleProperty('fsNames')}"/>	',
			  		   	 '<s:property value="%{getLocaleProperty('profile.products')}" />',
			  		   	  '<s:text name="productName"/>',
			  		      '<s:text name="stock"/>',
			  		    '<s:text name="finalStkAmt"/>',
			  		   	'<s:text name="seasonCode"/>'
			             ],
			    datatype: "json",
			   	colModel:[
			   	          
					 <s:if test='branchId==null'>
					 {name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
					 </s:if>
					 
					 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   		
				   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>
				   			
					{name:'fsCode',index:'fsCode',sortable:false},
					{name:'fsName',index:'fsName',sortable:false},
					{name:'subcategory',index:'subcategory',sortable:false},
				   
				    {name:'productName',index:'productName',sortable:false},
				  
					{name:'stock',index:'stock',align:'right',sortable:false},
					{name:'finalStkAmt',index:'finalStkAmt',align:'right',sortable:false},
					{name:'seasonCode',index:'seasonCode',sortable:false}
					
			         	],
			   height: 301, 
			   width: $("#baseDiv").width(),
			 
			   
			   scrollOffset: 19,     
			   sortname:'id',	
			   sortorder: "asc",
			   rowNum:10,
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
							url:'fieldStaffStockReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
						   	       <s:if test="enableBatchNo ==1">
						  		   	  '<s:text name="batchno"/>',
						  		   	  </s:if>
                                   '<s:text name="stock"/>'
                                    ],				 	      	 
						   	colModel:[
									  <s:if test="enableBatchNo ==1">
				   					 {name:'batchno',index:'batchno',sortable:false},
					 				 </s:if>
									{name:'stock',index:'stock',align:'stock',sortable:false}	
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    shrinkToFit:true,
						    //autowidth: true,
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
		    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
	
}

function loadGridWithoutBatch(){
	jQuery("#detail").jqGrid(
			{ 
				url:'fieldStaffStockReport_detail.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			   	mtype: 'POST',	
			    styleUI : 'Bootstrap',
			   	postData:{	
				 "fstaff" : function(){			  
				      return document.getElementById("fstaff").value;
 			          },
 			      "product" : function(){			  
 					  return document.getElementById("productId").value;
 	 			      }, 
 	 			    <s:if test="currentTenantId!='lalteer'">
 	 			      "seasonCode":function(){
 	 			    	return document.getElementById("seasonId").value;
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
 					  }
 					  </s:if>
			    },
			  	colNames:[
			  	          
						   <s:if test='branchId==null'>
					      '<s:text name="app.branch"/>',
						   </s:if>
					      <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							
								'<s:text name="app.subBranch"/>',    
							</s:if>
								 '<s:property value="%{getLocaleProperty('fsCodes')}"/>	',
					  		   	  '<s:property value="%{getLocaleProperty('fsNames')}"/>	',
			  		    '<s:property value="%{getLocaleProperty('profile.products')}" />',
			  		   	 // '<s:text name="productCode"/>',
			  		   	  '<s:text name="productName"/>',
			  		   	'<s:text name="Unit"/>',
			  		    <s:if test="enableBatchNo ==1">
			  		   	  '<s:text name="batchno"/>',
			  		   	  </s:if>
						  '<s:text name="stock"/>',
						  <s:if test="currentTenantId=='pratibha'">
						  '<s:text name="finalStkAmt"/>',
						  </s:if>
						  '<s:text name="seasonCode"/>'
			             ],
			    datatype: "json",
			   	colModel:[
			   	          
					 <s:if test='branchId==null'>
					 {name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
					 </s:if>
					 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   			{name:'subBranchId',index:'subBranchId',sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>
					{name:'fsCode',index:'fsCode',sortable:false},
					{name:'fsName',index:'fsName',sortable:false},
					{name:'subcategory',index:'subcategory',sortable:false},
				   // {name:'productCode',index:'productName',width:120,sortable:false},
				    {name:'productName',index:'productName',sortable:false},
				    {name:'unit',index:'unit',sortable:false},
				    <s:if test="enableBatchNo ==1">
				    {name:'batchno',index:'batchno',sortable:false},
					  </s:if>
					{name:'stock',index:'stock',align:'right',sortable:false},
					  <s:if test="currentTenantId=='pratibha'">
					{name:'finalStkAmt',index:'finalStkAmt',align:'right',sortable:false},
					</s:if>
					{name:'seasonCode',index:'seasonCode',align:'right',sortable:false}
			         	],
			   height: 301, 
			   width: $("#baseDiv").width(),
			 
			   
			   scrollOffset: 19,     
			   sortname:'id',	
			   sortorder: "asc",
			   rowNum:10,
			   shrinkToFit:true,
			   rowList : [10,25,50],
			   viewrecords: true,  
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
	
}


function exportXLS(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	/* if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else */ if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'fieldStaffStockReport_populateXLS.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}	

</script>
</body>