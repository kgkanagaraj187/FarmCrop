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
			   	url:'customer_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",	
                styleUI : 'Bootstrap',
                postData:{
        			  "postdata" :  function(){	
        	   			return  decodeURI(postdata);
        			  } 
        	   	},
			   	colNames:[
						/* <s:if test='branchId==null'>
							'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							'<s:text name="app.subBranch"/>',
						</s:if> */
						'<s:text name="customer.customerId"/>',
						'<s:text name="customer.customerName"/>',
						'<s:text name="customer.personName"/>',
						'<s:text name="customer.mobileNumber"/>',
						'<s:text name="customer.email"/>'
				   	 ],
			   	colModel:[
/* 					<s:if test='branchId==null'>
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
			   		</s:if> */
			   		{name:'customerId',index:'customerId',width:125,sortable:true},
			   		{name:'customerName',index:'customerName',width:125,sortable:true},	
			   		{name:'personName',index:'personName',width:125,sortable:true},	
			   		{name:'mobileNumber',index:'mobileNumber',width:125,align:'right',sortable:false},	   		
			   		{name:'email',index:'email',width:125,sortable:true}
			   		   
			   	],
			   	height: 400, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [20,45,60],
			    sortname:'id',			  
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			    onSelectRow: function(id){ 
				  document.updateform.id.value  =id;
				  postDataSubmit();
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
			
			retainFields();
			postdata ='';
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
		    
			$('.ui-jqgrid .ui-jqgrid-bdiv').css('overflow-x', 'hidden');    
		});	           
	
		
</script>
	<div class="appContentWrapper marginBottom">
		<sec:authorize ifAllGranted="profile.customer.create">
			<input type="BUTTON" id="add" class="btn btn-sts"
				value="<s:text name="create.button"/>"
				onclick="document.createform.submit()" />
		</sec:authorize>

		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>
	<s:form name="createform" action="customer_create">
	</s:form>
	<s:form name="updateform" action="customer_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
		<s:hidden name="postdata" id="postdata" />
	</s:form>
</body>
