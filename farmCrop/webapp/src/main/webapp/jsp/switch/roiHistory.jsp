<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
	<META name="decorator" content="swithlayout">
	<style>		
		select[name='cs.name'], select[name='status']{
			width:100px !important;
		}
	</style>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){		
	jQuery("#detail").jqGrid(
	{
	   	url:'roi_historyData.action',
	   	pager: '#pagerForDetail',
	    mtype: 'POST',
	   	datatype: "json",	
	   	colNames:[				   	       	  
	  		   	  '<s:text name="rateOfInterest"/>',
	  		      '<s:text name="roiEffectiveFrom"/>',
	  		      '<s:text name="roiApplyExist"/>',
	  		      '<s:text name="roi.isActive"/>'
	      	 ],
	   	colModel:[						
			
	   		{name:'rateOfInterest',index:'rateOfInterest',width:125,sortable:true, search:false, align:"center"},
	   		{name:'roiEffectiveFrom',index:'roiEffectiveFrom',width:125,sortable:true, search:false, align:"center"},
	   		{name:'roiApplyExist',index:'roiApplyExist',width:125,sortable:true, search:false, align:"center"},
	   		{name:'roi.isActive',index:'roi.isActive',width:125,sortable:true, search:false, align:"center"},	   	
	   	],
	   	height: 301, 
	    width: $("#baseDiv").width(), // assign parent div width
	    scrollOffset: 0,
	   	rowNum:10,
	   	rowList : [10,25,50],
	    sortname:'id',			  
	    sortorder: "desc",
	    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	    onSelectRow: function(id){ 
	    
		},		
        onSortCol: function (index, idxcol, sortorder) {
	        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                    && this.p.colModel[this.p.lastsort].sortable !== false) {
                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
            }
        }
    });
	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

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
});	      
</script>
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
<s:form name="prefListForm" action="prefernce_list.action">
</s:form>
<input type="BUTTON" id="cancelAg" value="<s:text name="cancel.button"/>" onclick="document.prefListForm.submit()" />
</body>
