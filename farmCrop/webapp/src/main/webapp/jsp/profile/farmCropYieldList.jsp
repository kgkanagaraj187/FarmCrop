<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
function loadCropYieldDetails(){	
	jQuery("#cropYieldDetail").jqGrid(
	{
	   	url:'cropYield_data.action?id=<s:property value="farm.id"/>',
	   	pager: '#pagerForCropYieldDetail',
	    mtype: 'POST',
	   	datatype: "json",	
	   	colNames:[				   	       	  
	  		   	  '<s:text name="cropYield.cropYieldDate"/>',
	  		      '<s:text name="cropYield.season.name"/>',
	  		      '<s:text name="cropYield.landHolding"/>',
	  		      '<s:text name="cropYield.latitude"/>',
	  		      '<s:text name="cropYield.longitude"/>',
	      	 ],
	   	colModel:[						
			
	   		{name:'cropYieldDate',index:'cropYieldDate',width:125,sortable:true,search:false},
	   		{name:'s.name',index:'s.name',width:125,sortable:true},
	   		{name:'landHolding',index:'landHolding',width:125,sortable:true},
	   		{name:'latitude',index:'latitude',width:125,sortable:true},	   	
	   		{name:'longitude',index:'longitude',width:125,sortable:true},	   	
	   	],
	   	height: 301, 
	    width: $("#baseDiv_cropYieldDetail").width(), // assign parent div width
	    scrollOffset: 0,
	   	rowNum:10,
	   	rowList : [10,25,50],
	    sortname:'id',			  
	    sortorder: "desc",
	    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table	    		
        onSortCol: function (index, idxcol, sortorder) {
	        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                    && this.p.colModel[this.p.lastsort].sortable !== false) {
                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
            }
        },
        subGrid: true,
	    subGridOptions: {
		   "plusicon" : "ui-icon-triangle-1-e",
		   "minusicon" : "ui-icon-triangle-1-s",
		   "openicon" : "ui-icon-arrowreturn-1-e",
		},
		subGridRowExpanded: function(subgrid_id, row_id){
			 	var subgrid_table_id, pager_id; 
			  	subgrid_table_id = subgrid_id+"_t"; 
			   	pager_id = "p_"+subgrid_table_id; 
			   	$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");			   	
			   	jQuery("#"+subgrid_table_id).jqGrid(
			   	{
					url:'cropYield_subGridDetail.action?',
				   	pager: pager_id,
				   	datatype: "json",
					mtype: 'POST',				   	
				   	postData:{				 
						  "id" : function(){
							  return row_id;
						  }
					},	
				   	colNames:[	
				   	       	  '<s:text name="cropYieldDetail.farmCropsMaster.code"/>',
				   	       	  '<s:text name="cropYieldDetail.farmCropsMaster.name"/>',			  		  	 
				  		      '<s:text name="cropYieldDetail.yield"/>'
		 	      	 ],
				   	colModel:[	
							{name:'fcm.code',index:'fcm.code',width:150,sortable:false},
   							{name:'fcm.name',index:'fcm.name',width:150,sortable:false},
   							{name:'yield',index:'yield',width:150,sortable:false,align:'right'},   									   		
				   	],
				   	scrollOffset: 0, 
				    sortname:'id',
				    height: '100%', 
				    sortorder: "desc",
				    autowidth: true,
				    rowNum:10,
					rowList : [10,25,50],
				    viewrecords: true				
			   	});
			  	jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
			    jQuery("#"+subgrid_id).parent().css("width","100%");
			    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
			    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
			    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
			    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");
			    jQuery("#"+subgrid_table_id).setGridWidth($("#baseDiv_cropYieldDetail").width()-55);
		}
    });
	
	jQuery("#cropYieldDetail").jqGrid('navGrid','#pagerForCropYieldDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#cropYieldDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

	colModel = jQuery("#cropYieldDetail").jqGrid('getGridParam', 'colModel');
	jQuery('#gbox_' + jQuery.jgrid.jqID(jQuery("#cropYieldDetail")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;

        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        }
    });
}
</script>
<div style="width: 99%;" id="baseDiv_cropYieldDetail">
	<table id="cropYieldDetail"></table>
	<div id="pagerForCropYieldDetail"></div>
</div>
