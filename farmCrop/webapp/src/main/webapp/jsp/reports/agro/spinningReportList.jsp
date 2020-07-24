
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
var detailAction = "spinningReport_detail?edit="+<%=request.getParameter("edit")%>;
jQuery(document).ready(function(){
	loadGrid();
	
});

function loadGrid(){
	var gridColumnNames = new Array();
	var gridColumnModels = new Array();
	var colNames='<s:property value="mainGridCols"/>';
	
	$(colNames.split("%")).each(function(k,val){
			if(!isEmpty(val)){
				var cols=val.split("#");
				console.log(cols)
				gridColumnNames.push(cols[0]);
				gridColumnModels.push({name: cols[0],sortable: false,frozen:true});
			}
	});
	
	jQuery("#detail").jqGrid(
			{
				url:'spinningReport_data.action?',
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
			   	subGrid: false,
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
	        beforeSelectRow: 
		        function(rowid, e) {
		    	var iCol = jQuery.jgrid.getCellIndex(e.target);
		    	var branchId = '<s:property value="getBranchId()"/>';
		            if(branchId==null||branchId==""){
		            	if (iCol >=13){return false; }
				            else{ return true; }
		            }else{
		            	 if (iCol >=12){return false; }
				            else{ return true; }
		            }
		           
		        },
			  onSelectRow: function(id){ 
				  document.updateform.id.value =id;
				  document.updateform.action = detailAction;
		          document.updateform.submit();      
		 } 
			});
			//jQuery("#detail").jqGrid('setFrozenColumns');
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
function exportXLS(){
	if(isDataAvailable("#detail")){
		data = new Array();
		$("#form select").each(function(){
			var value = $(this).val();
			var name=this.name;
			var label =$(this).parent().parent().find("label").html();
			if(!isEmpty(value)){
				data.push({name:name,value:value,label:label,fieldtxt:$(this).find('option:selected').text() });
			}
		});
		var json = JSON.stringify(data);
		var dataa={
				filterList:json
		};
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'spinningReport_populateXLS.action?',postData:dataa});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}	
function search(){
	data = new Array();
	$("#form select").each(function(){
		var value = $(this).val();
		var name=this.name;
		var label =$(this).parent().parent().find("label").html();
		if(!isEmpty(value)){
			data.push({name:name,value:value,label:label,fieldtxt:$(this).find('option:selected').text()});
		}
	});
	var json = JSON.stringify(data);
	var dataa={
			filterList:json
	};
	
	jQuery("#detail").jqGrid('setGridParam',{url:'spinningReport_data.action',page:1,postData:dataa}).trigger('reloadGrid');				
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
				mbody+='<img src="spinningReport_populateImage.action?imgId='+str_array[i]+'"/>';
				mbody+="</div>";
			}else{
				mbody="";
				mbody="<div class='item'>";
				mbody+='<img src="spinningReport_populateImage.action?imgId='+str_array[i]+'"/>';
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
<sec:authorize ifAllGranted="service.spinningTransfer.create"> 
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize> 
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
	<s:form name="createform" action="spinningTransfer_create?edit=1"></s:form>
	<s:form name="updateform">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
		<s:hidden name="type" id="type"></s:hidden>
	</s:form>
	
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
