<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">
var type="<%=request.getParameter("type")%>";
$(document).ready(function(){		
	jQuery("#detail").jqGrid(
	{
	   	url:'<%=request.getParameter("type")%>_data.action?type=<%=request.getParameter("type")%>',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   	colNames:[
					<s:if test='branchId==null'>
					 	'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
					</s:if>	
	  		   	  '<s:text name='%{"agentName"+type}'/>',
	  		      '<s:text name="personalInfo.firstName"/>',
	  		      '<s:text name="personalInfo.lastName"/>',
	  			  '<s:text name="contactInfo.mobileNumber"/>' ,
	  			 <s:if test="currentTenantId=='pratibha'">
	  			  '<s:text name="lastLogin"/>' ,
	  			  '<s:text name="version"/>' ,
	  			  '<s:text name="%{getLocaleProperty('fstLastTime')}"/>' ,
	  			  '<s:text name="%{getLocaleProperty('agentType')}"/>',
	  			  '<s:text name="%{getLocaleProperty('warehouse')}"/>',
	  			 </s:if>
	  		      '<s:text name="personalInfo.bodStatus" />'      
	      	 ],
	   	colModel:[						
<s:if test='branchId==null'>
	{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
		value: '<s:property value="parentBranchFilterText"/>',
		dataEvents: [ 
		          {
		            type: "change",
		            fn: function () {
		            	console.log($(this).val());
		             	getSubBranchValues($(this).val())
		            }
		        }]
		
		}},	   				   		
	</s:if>
	<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
		{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
	</s:if>
			
	   		{name:'profileId',index:'profileId',width:125,sortable:true},
	   		{name:'pI.firstName',index:'pI.firstName',width:125,sortable:true},
	   		{name:'pI.lastName',index:'pI.lastName',width:125,sortable:true},
	   		{name:'cI.mobileNumber',index:'cI.mobileNumber',width:125,sortable:true},
	   		<s:if test="currentTenantId=='pratibha'">
	   		{name:'lastLogin',index:'lastLogin',width:125,sortable:true,search:false},
	   		{name:'version',index:'version',width:125,sortable:true,search:false},
	   		{name:'fstLastTime',index:'fstLastTime',width:125,sortable:true,search:false},
	   		<s:if test="currentTenantId!='pratibha'">
	   		{name:'agentType',index:'agentType',width:125,sortable:true,search:true,stype:'select',searchoptions:{value: '-1:<s:text name="filter.allStatus"/>;01:<s:text name="agent01"/>;02:<s:text name="agent02"/>;03:<s:text name="agent03"/>'}},
	   		</s:if>
	   		<s:if test="currentTenantId=='pratibha'">
	   		{name:'agentType',index:'agentType',width:125,sortable:true,search:true,stype:'select',searchoptions:{value: '-1:<s:text name="filter.allStatus"/>;05:<s:text name="agent01"/>;02:<s:text name="agent02"/>;06:<s:text name="agent03"/>'}},
	   		</s:if>
	   		{name:'warehouse',index:'agentWarehouse',width:125,sortable:true,search:false},
	   		</s:if>
	   		{name:'bodStatus',index:'bodStatus',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: '-1:<s:text name="filter.allStatus"/>;1:<s:text name="bodStatus1"/>;0:<s:text name="bodStatus0"/>' }}
	   		
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
	
	jQuery("#detail").jqGrid("setLabel","branchId","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","profileId","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","pI.firstName","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","pI.lastName","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","cI.mobileNumber","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","bodStatus","",{"text-align":"center"});
	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
	if(type=="fieldStaff"){
		jQuery("#detail").jqGrid('hideCol',"w.name");
		
	}
	jQuery("#detail").setGridWidth($("#baseDiv").width());
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
	<sec:authorize ifAllGranted="profile.agent.create">
		
	</sec:authorize>
	<div class="appContentWrapper ">
	<input type="BUTTON" id="add" class="btn btn-sts"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>	
	<s:form name="createform" action="%{type}_create?type=%{type}">
	</s:form>
	<s:form name="updateform" action="%{type}_detail?type=%{type}">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
</body>
