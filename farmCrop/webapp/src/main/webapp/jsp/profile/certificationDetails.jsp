<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
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
	padding: 2px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: auto !important;
	padding: 2px;
}
</style>
</head>
<body>
	<script type="text/javascript">
	
	 jQuery(document).ready(function(){
		 
		 jQuery(".well").hide();
		
		jQuery("#detail").jqGrid(
				{
					url:'certificationDetails_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				    styleUI : 'Bootstrap',
				   	postData:{
				   		
				   		
				   	},
				   	colNames:[
				   		'<s:property value="%{getLocaleProperty('Farm Name')}" />',
				   		'<s:property value="%{getLocaleProperty('Season')}" />',
				   		'<s:property value="%{getLocaleProperty('Inspection Type')}" />',
				   		'<s:property value="%{getLocaleProperty('Certification Type')}" />',
				   		'<s:property value="%{getLocaleProperty('IC Status')}" />',
				   		'<s:property value="%{getLocaleProperty('Inspector Name')}" />',
				   		'<s:property value="%{getLocaleProperty('Inspection Date')}" />',
                      	 ],
		 	      	colModel:[
		 	      		{name:'farmName',index:'farmName',sortable:false,frozen:false},
		 	      		{name:'season',index:'season',sortable:false,frozen:false},
		 	      		{name:'inspType',index:'inspType',sortable:false,frozen:false},
		 	      		{name:'certType',index:'certType',sortable:false,frozen:false},
		 	      		{name:'icStatus',index:'icStatus',sortable:false,frozen:false},
		 	      		{name:'inspName',index:'inspName',sortable:false,frozen:false},
		 	      		{name:'inspDate',index:'inspDate',sortable:false,frozen:false},
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
				   
				   
				   onSortCol: function (index, idxcol, sortorder) {
			            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
			                    && this.p.colModel[this.p.lastsort].sortable !== false) {
			            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
			            }
			            }
				});
		jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
			           // jQuery("#detail15").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

			    colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
			    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail")[0].id) +
			            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
			    var cmi = colModel[i], colName = cmi.name;
			    if (cmi.sortable !== false) {
			    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
			    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
			    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
			    }
			    });
		$('#detail').jqGrid('setGridHeight',(reportWindowHeight));
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	
		 
		function reloadGrid(){
		
		   jQuery("#detail").jqGrid('setGridParam',{url:"certificationDetails_detail.action?",page:1}).trigger('reloadGrid');
		 
		}	
		function clear(){
		//	resetReportFilter();
			document.form.submit();
		}


		
	});


</script>
	<s:form name="form" action="certificationDetails_list">	

		<div class="appContentWrapper marginBottom">
			
			<div style="width: 100%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<!-- <div id="pager_id"></div> -->
			</div>

		</div>
	</s:form>
		
	
</body>
