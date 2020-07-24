<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>



<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
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

	
			var colNames='<s:property value="mainGridCols"/>';
			loadGrid();
		
		
			$("#reset").click(function(){
			var url = (window.location.href);
			window.location.href=url;
			$(".select2").val("");
			
		})	
		
		/* jQuery("#generate").click( function() {
			reloadGrid("");	
		}); */
		
		/* jQuery("#clear").click( function() {
			clear();
		});	 */
		
		
	});
	
	function reloadGrid(){
		jQuery("#detail").jqGrid('setGridParam',{url:"icsFarmerDairyReport_data.action",page:1}).trigger('reloadGrid');	
	}
	
	function clear(){
		resetReportFilter();
		document.form.submit();
		
	}
	
	function search(){
		reloadGrid("");
	}
	
	function loadGrid(){

		var gridColumnNames = new Array();
		var gridColumnModels = new Array();
		
		var subGridColumnNames = new Array();
		var subGridColumnModels = new Array();
		
		var colNames='<s:property value="mainGridCols"/>';
		
		$(colNames.split("#")).each(function(k,val){
				if(!isEmpty(val)){
					var cols=val.split("#");
					gridColumnNames.push(cols[0]);
					gridColumnModels.push({name: cols[0],sortable: false,frozen:true});
				}
		});
		
		
		
		jQuery("#detail").jqGrid({
					url:'icsFarmerDairyReport_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',			
			    	postData:{	
				   	 /*  "farmerId" : function(){
						  return document.getElementById("farmerId").value;
					  }, */
					 /*  "farmId" : function(){
						  return document.getElementById("farmId").value;
					  }, */
				   	}, 
				   	colNames:gridColumnNames,
				   	colModel:gridColumnModels,
				   	height: 301, 
				   	width: $("#baseDiv").width(),
				   	scrollOffset: 19,     
				   	sortname:'id',
				   	shrinkToFit:true,
				   	sortorder: "desc",
				   	rowNum:10,
				   	rowList : [10,25,50],
				   	viewrecords: true,  	
				   	subGrid: false,
				 	onSelectRow: function(id){ 
						  document.updateform.id.value  =id;
						//  document.updateform.typez.value  =id;
				          document.updateform.submit();      
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
	
	</script>


	<%-- <s:form id="form">
	
	<div class="appContentWrapper marginBottom">
	<section class='reportWrap row'>
	<div class="gridly">
	
					 <div class="filterEle">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="filter.farmerId" id="farmerId" list="farmersList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div> 
					
					<div class="filterEle">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="farmId" id="farmId" list="farmList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					
					
					<div class='filterEle' style="margin-top: 2%;margin-right: 0%;">
						<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true" onclick="search()">
						<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="reset" onclick="clear()">
						<b class="fa fa-close"></b>
						</button>
					</div>
					
	</div>
	</section>
	</div>
	
	</s:form> --%>
	
	<div class="appContentWrapper marginBottom">
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div>


<s:form name="updateform" action="icsFarmerDairyReport_detail.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
</body>

