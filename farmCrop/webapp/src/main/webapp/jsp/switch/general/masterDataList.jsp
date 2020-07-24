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
			   	url:'masterData_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[				   	       	  
						'<s:text name="masterData.code" />',
						'<s:text name="masterData.name" />',
						'<s:text name="masterData.type" />'					
				],
			   	colModel:[						
						{name:'code',index:'code',sortable:true, width:125},
				 		{name:'name',index:'name', sortable:true, width:125},
				 		<s:if test="currentTenantId =='wilmar'">
				 		{name:'masterType',index:'masterType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="allanswer"/>;1:<s:property value='%{getLocaleProperty("masterType1")}' />;2:<s:property value='%{getLocaleProperty("masterType2")}'/>;3:<s:property value='%{getLocaleProperty("masterType3")}'/>;5:<s:property value='%{getLocaleProperty("masterType5")}'/>;6:<s:property value='%{getLocaleProperty("masterType6")}'/>;7:<s:property value='%{getLocaleProperty("masterType7")}'/>;9:<s:property value='%{getLocaleProperty("masterType9")}'/>;10:<s:property value='%{getLocaleProperty("masterType10")}'/>;12:<s:property value='%{getLocaleProperty("masterType12")}'/>;13:<s:property value='%{getLocaleProperty("masterType13")}'/> '}}
                         </s:if>
                         <s:else>
				 		//{name:'masterType',index:'masterType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="allanswer"/>;1:<s:property value='%{getLocaleProperty("masterType1")}' />;2:<s:property value='%{getLocaleProperty("masterType2")}'/>;3:<s:property value='%{getLocaleProperty("masterType3")}'/>;7:<s:property value='%{getLocaleProperty("masterType7")}'/>;9:<s:property value='%{getLocaleProperty("masterType9")}'/>;10:<s:property value='%{getLocaleProperty("masterType10")}'/>;12:<s:property value='%{getLocaleProperty("masterType12")}'/>;13:<s:property value='%{getLocaleProperty("masterType13")}'/> '}}
				 		{name:'masterType',index:'masterType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="allanswer"/>;1:<s:property value='%{getLocaleProperty("masterType1")}' />;2:<s:property value='%{getLocaleProperty("masterType2")}'/>;3:<s:property value='%{getLocaleProperty("masterType3")}'/>;7:<s:property value='%{getLocaleProperty("masterType7")}'/>;9:<s:property value='%{getLocaleProperty("masterType9")}'/>;10:<s:property value='%{getLocaleProperty("masterType10")}'/>;12:<s:property value='%{getLocaleProperty("masterType12")}'/>;13:<s:property value='%{getLocaleProperty("masterType13")}'/> '}}
				 		//{name:'masterType',index:'masterType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="allanswer"/>;1:<s:text name="masterType1"/>;2:<s:text name="masterType2"/>;3:<s:text name="masterType3"/>' }}
			   	         </s:else> 
				 		],
			   	height: 255, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [15,30,45],
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
			
			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#detail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","masterType","",{"text-align":"center"});
			
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
		    $('#detail').jqGrid('setGridHeight',(windowHeight));
		});	           
	
</script>

<div class="appContentWrapper marginBottom">
	<sec:authorize ifAllGranted="admin.masterData.create">
		<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
	</sec:authorize>
	<div style="width: 99%;" id="baseDiv">
		<table id="detail"></table>
		<div id="pagerForDetail"></div>
	</div>
</div>
<s:form name="createform" action="masterData_create">
</s:form>
<s:form name="updateform" action="masterData_detail">
	<s:hidden name="id" />
	<s:hidden name="currentPage" />
</s:form>
</body>
