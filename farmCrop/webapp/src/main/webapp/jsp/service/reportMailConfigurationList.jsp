<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">

$(document).ready(function(){	
	jQuery("#detail").jqGrid(
			{
				url:'reportMailConfiguration_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",	
                styleUI : 'Bootstrap',
                mtype: 'POST',
                colNames:[				 
        	  		   	  '<s:text name="reportMailConfiguration.name"/>',
        	  		   	  '<s:text name="reportMailConfiguration.mailId"/>',
        	  		      '<s:text name="reportMailConfiguration.status"/>'
        	      	     ],
        	    colModel:[		
        	      		   {name:'name',index:'name',width:125,sortable:true},
        	      		   {name:'mailId',index:'mailId',width:125,sortable:true},
        	      		   {name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;0:<s:text name="status0"/>;1:<s:text name="status1"/>' }}
        	      		   ],
        	    height: 301, 
        	    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [20,45,60],
			    sortname:'id',			  
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			    onSelectRow: function(id){ 
					  document.updateform.id.value  = id;
			          document.updateform.submit();      
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
	<div class="appContentWrapper marginBottom">
		<sec:authorize ifAllGranted="service.reportMailConfig.create">
			<input type="BUTTON" id="add" class="btn btn-sts"
				value="<s:text name="create.button"/>"
				onclick="document.createform.submit()" />
		</sec:authorize>

 			<div style="width: 99%;" id="baseDiv">
				<table id="detail"></table>
			<div id="pagerForDetail"></div>
 			</div>
   </div>
<s:form name="createform" action="reportMailConfiguration_create">
</s:form>

<s:form name="updateform" action="reportMailConfiguration_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
</s:form>
</body>
