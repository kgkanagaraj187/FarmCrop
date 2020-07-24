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
	  var procurementProductId = '';
var recordLimit='<s:property value="exportLimit"/>';
	jQuery(document).ready(function(){
		onFilterData();
		populateHeaderFields();
		document.getElementById('procurementProductId').selectedIndex=0;
		//$('branchIdParma').val('');
		jQuery(".well").hide();
		$('.showProp').hide();
		jQuery("#detail").jqGrid(
				{
					url:'heapStockReport_detail.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				    styleUI : 'Bootstrap',
				   	mtype: 'POST',				   	
				   	postData:{				 
			               "gining":function(){
				               return document.getElementById("gining").value;
			              },
			              "procurementProduct":function(){
				               return document.getElementById("procurementProductId").value;
			              }, 
			              "ics":function(){
				               return document.getElementById("ics").value;
			              },
			              "heapDataName":function(){
				               return document.getElementById("heapName").value;
			              },
			              "season":function(){
			            	  return document.getElementById("season").value;
			              }
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
                            '<s:property value="%{getLocaleProperty('season')}"/>',
				  			'<s:property value="%{getLocaleProperty('gining')}"/>',
				  			'<s:property value="%{getLocaleProperty('pp.name')}"/>',
				  			'<s:property value="%{getLocaleProperty('icsNames')}"/>',
				  		 	'<s:property value="%{getLocaleProperty('heapName')}"/>',
				  			'<s:property value="%{getLocaleProperty('stock')}"/>'
				  			
				  		   
		 	      	 ],
				   	colModel:[
						<s:if test='branchId==null'>
				   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
				   			value: '<s:property value="parentBranchFilterText"/>',
				   			dataEvents: [{
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
				   			{name:'season',index:'season',sortable:false},
				   			{name:'gining',index:'gining',sortable:false},
							{name:'pp.name',index:'pp.name',sortable:false},
							{name:'ics',index:'ics',sortable:false},
							{name:'heapName',index:'heapName',sortable:false},
							{name:'stock',index:'stock',sortable:false}
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   shrinkToFit:true,
				   scrollOffset: 19,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
				   subGrid: false,
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
			
			jQuery("#detail").jqGrid('setGridParam',{url:"heapStockReport_detail.action",page:1}).trigger('reloadGrid');
			populateHeaderFields();
		}
		function clear(){
			document.form.submit();
		}
	});
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
	function exportXLS(){

		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){
			
			var totalStock=$("#totalStock").text();
			headerData='<s:text name="Total Net weight(quintal)"/>'+"###"+totalStock;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});
			
			jQuery("#detail").jqGrid('excelExport', {url: 'heapStockReport_populateXLS.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
	function onFilterData(){
 		callAjaxMethod("heapStockReport_populateProductList.action","procurementProductId");
		callAjaxMethod("heapStockReport_populateGiningList.action","gining");
		callAjaxMethod("heapStockReport_populateICSList.action","ics");
		callAjaxMethod("heapStockReport_populateHeapNameList.action","heapName");
		callAjaxMethod("heapStockReport_populateSeasonList.action","season");
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
	function populateHeaderFields() {
		
 		var branchIdParma='<s:property value="getBranchId()"/>';
		var gining=document.getElementById("gining").value;
		var procurementProduct=document.getElementById("procurementProductId").value;
		var ics=document.getElementById("ics").value;
		var heapDataName=document.getElementById("heapName").value;
		var season=document.getElementById("season").value;
		$.post("heapStockReport_populateHeaderFields",{gining:gining,procurementProduct:procurementProduct,ics:ics,heapDataName:heapDataName,season:season,branchIdParma:branchIdParma},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalStock").html(json[0].totalStock);	 		
	 	}); 
	}
</script>
	<s:form name="form" action="heapStockReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('gining')}" /></label>
						<div class="form-element">
							<s:select name="gining" id="gining" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="product" /></label>
						<div class="form-element">
							<s:select name="procurementProductId" id="procurementProductId"
								list="{}" headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt"><s:text name="icss" /></label>
						<div class="form-element">
							<s:select name="ics" id="ics" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('heapName')}" /></label>
						<div class="form-element">
							<s:select name="heapName" id="heapName" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('season')}" /></label>
						<div class="form-element">
							<s:select name="season" id="season" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />

						</div>
					</div>

					<s:if test='branchId==null'>
						<div class="filterEle">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element">
								<s:select name="branchIdParma" id="branchIdParma"
									list="branchesMap" headerKey=""
									headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>
					</s:if>
					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
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
								cssClass="input-sm form-control select2" />
						</div>
					</s:if>
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
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
					<div class="report-summaryBlockItem">
						<span> <s:text name="totalStock" />:&nbsp;<span
							class="strong" id="totalStock"></span>
						</span>
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
	
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
