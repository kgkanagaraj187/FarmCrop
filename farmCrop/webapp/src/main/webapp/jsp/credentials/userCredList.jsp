<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
	<META name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	jQuery("#detail").jqGrid(
	{
	   	url:'userCred_data.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	colNames:[	
	  		   	  '<s:text name="userCred.username"/>',
	  		      '<s:text name="userCred.role.id"/>',
	  		      '<s:text name="userCred.enabled"/>'
				 ],
	   	colModel:[
	   		{name:'username',index:'username',width:125,sortable:true},
	   		{name:'role.name',index:'role.name',width:125,sortable:true},
	   		{name:'enabled',index:'enabled',width:125,sortable:true}
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
		  document.updateform.id.value  =id;
          document.updateform.submit();      
		},		
        onSortCol: function (index, idxcol, sortorder) {
        	if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                   && this.p.colModel[this.p.lastsort].sortable !== false) {
               $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
           }
        }
    });	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}); // enabled refresh for reloading grid
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
<s:form name="updateform" action="userCred_detail">
    <s:hidden key="id"/>
      <s:hidden key="currentPage"/>
</s:form>
</body>