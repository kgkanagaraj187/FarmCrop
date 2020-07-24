<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
</head>

<body>
	<script type="text/javascript">
	var branchId='<s:property value="branchId"/>';
	jQuery(document).ready(function(){
		var colNames='<s:property value="mainGridCols"/>';
		
		loadGrid();
	});
	var data = new Array();
	
	function loadGrid(){
		var gridColumnNames = new Array();
		var gridColumnModels = new Array();
		
		var subGridColumnNames = new Array();
		var subGridColumnModels = new Array();
		
		var colNames='<s:property value="mainGridCols"/>';
		var subColNames='<s:property value="subGridCols"/>';
		
		$(colNames.split("%")).each(function(k,val){
				if(!isEmpty(val)){
					var cols=val.split("#");
					console.log(cols)
					gridColumnNames.push(cols[0]);
					gridColumnModels.push({name: cols[0],align: cols[2],sortable: false,frozen:true});
						
					
					
				}
		});
		
		$(subColNames.split("%")).each(function(k,val){
			if(!isEmpty(val)){	
				var cols=val.split("#");
				subGridColumnNames.push(cols[0]);
				subGridColumnModels.push({name: cols[0],align: cols[2],sortable: false,frozen:true});
			}
		});
		
		
		
		jQuery("#detail").jqGrid(
				{
					url:'farmerProductReturnReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',				   	
				   	colNames:gridColumnNames,
				   	colModel:gridColumnModels,
				   	height: 301, 
				   	width: $("#baseDiv").width(),
				   	scrollOffset: 19,     
				   	sortname:'id',
				   	sortorder: "desc",
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
					   jQuery("#"+subgrid_table_id).jqGrid({
						   url:'farmerProductReturnReport_subGridDetail.action?id='+row_id,
						   pager: pager_id,
						   datatype: "json",	
						   colNames:subGridColumnNames,
						   colModel:subGridColumnModels,
						   scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    //autowidth: true,
						    shrinkToFit:true,
						    viewrecords: true
					   });
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
				jQuery("#detail").jqGrid('setFrozenColumns');
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
	
	function buttonEdcCancel(){
		//refreshPopup();
		document.getElementById("model-close-edu-btn").click();	
	 }
	
	function exportXLS(){
		if(isDataAvailable("#detail")){
			
			$("#form select").each(function(){
				var value = $(this).val();
				var name=this.name;
				if(!isEmpty(value)){
					data.push({name:name,value:value});
				}
			});
			var json = JSON.stringify(data);
			var dataa={
					filterList:json
			};
			
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'farmerProductReturnReport_populateXLS.action?',postData:dataa});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}	
	
	
	function search(){
		data = new Array();
		$("#form select").each(function(){
			var value = $(this).val();
			var name=this.name;
			if(!isEmpty(value)){
				data.push({name:name,value:value});
			}
		});
		var json = JSON.stringify(data);
		var dataa={
				filterList:json
		};
		
		jQuery("#detail").jqGrid('setGridParam',{url:'farmerProductReturnReport_detail.action',page:1,postData:dataa}).trigger('reloadGrid');				
		}
	
	var imgID = "";
	function popupWindow(ids) {
		
		try{
			var str_array = ids.split(',');
			$("#mbody").empty();
			
			var mbody="";
			
			for(var i = 0; i < str_array.length; i++){
				
				if(i==0){
					mbody="";
					mbody="<div class='item active'>";
					mbody+='<img src="farmerProductReturnReport_populateImage.action?imgId='+str_array[i]+'"/>';
					mbody+="</div>";
				}else{
					mbody="";
					mbody="<div class='item'>";
					mbody+='<img src="farmerProductReturnReport_populateImage.action?imgId='+str_array[i]+'"/>';
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
	
	
</script>

 <style>
	.reportWrap .filterEle {
		float: left;
		cursor: pointer;
		margin-right: 1%;
		margin-left: 1%;
		margin-bottom: 5px;
		box-sizing: border-box;
	}
	.reportWrap .filterEle {
		width: 14%;
		height: auto;
		box-sizing: border-box;
	}
	
	.reportWrap .filterEle label{
		font-weight:bold;
		color:#000;
	}
	</style> 
		<%-- FILTER SECTION --%>
			
	
		<s:form id="form">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<s:iterator value="reportConfigFilters" status="status">
						<div class='filterEle'>
							<label for="txt"><s:property value='%{getLocaleProperty(label)}'/></label>
							<div class="form-element">
								<s:select name="%{field}" list="options"
									headerKey="" headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
									<s:set var="personName" value="%{method}"/>
							</div>
						</div>
					</s:iterator>
					<div class='filterEle' style="margin-top: 2%;margin-right: 0%;">
						<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true" onclick="search()">
						<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="reset" onclick="reset()">
						<b class="fa fa-close"></b>
						</button>
					</div>
				</div>
			</section>
			</div>
		</s:form>
	
			
			<%-- GRID SECTION --%>
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
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
						<%-- <li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li> --%>
						
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

</body>
