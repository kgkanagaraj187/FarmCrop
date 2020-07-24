<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
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

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
</head>

<body>
	<script type="text/javascript">

	var farmerId='';
	var villageId=0;
	var sDate='';
	var eDate='';
	var seasonId='';

	jQuery(document).ready(function(){


		$('.applyBtn').click();
			
		jQuery("#detail").jqGrid(
				{
					url:'procurementProduct_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',				   	
					postData:{				 
						 
						},	
				   	colNames:[
				  		   	  '<s:text name="date"/>',
				  		    '<s:text name="seasonCode"/>',
				  		  <s:if test='supplierEnabled=="1"'>
				  		   	  '<s:text name="supplierType"/>',
				  		   	  '<s:text name="supplierName"/>',
				  		   	</s:if>
				  		    '<s:property value="%{getLocaleProperty('farmerName')}" />',
				  		  <s:if test="currentTenantId!='lalteer' && currentTenantId!='awi'">
				  		   	  //'<s:text name="agentName"/>',
				  			</s:if>
				  		   	'<s:property value="%{getLocaleProperty('village.name')}" />',
				  		 	'<s:text name="warehouse"/>',
				  		  <s:if test="currentTenantId!='olivado' && currentTenantId!='pratibha'">
				  		 	'<s:property value="%{getLocaleProperty('totalNoOfBags')}" />',
				  		 	</s:if>
				  		 	<s:if test="currentTenantId=='awi'">
				  		 	'<s:property value="%{getLocaleProperty('totalNoOfFruitBags')}" />',
				  		 	</s:if>
				  		  	 // '<s:text name="totalGrossWt"/>',
				  		      //'<s:text name="totalTareWt"/>',
				  		    '<s:property value="%{getLocaleProperty('totalNetwt')}" />',
				  		    <s:if test="currentTenantId!='lalteer' && currentTenantId!='awi'">
				  		   	  '<s:text name="txnAmount"/> ( <s:if test="currentTenantId!='pratibha'"><s:property value="%{getCurrencyType().toUpperCase()}" /></s:if><s:else><s:property value="%{getLocaleProperty('priceUnit')}" /></s:else>)'
				  		  </s:if>
		 	      	 ],
				   	colModel:[
						{name:'procurementDate',index:'procurementDate',width:250,sortable:false},
						{name:'seasonCode',index:'seasonCode',width:250,sortable:false},
						 <s:if test='supplierEnabled=="1"'>
				   		{name:'farmerId',index:'farmerId',width:250,sortable:false},
				   		{name:'farmerName',index:'farmerName',width:250,sortable:false},
				   	 	</s:if>
				   		{name:'farmerMblNo',index:'farmerMblNo',width:250,sortable:false},
				   	  <s:if test="currentTenantId!='lalteer' && currentTenantId!='awi'">
				   		//{name:'agentName',index:'agentName',width:250,sortable:false},
				  	 	</s:if>
				   		{name:'v.name',index:'v.name',width:250,sortable:false},
				   		{name:'warehouse',index:'warehouse',width:250,sortable:false},
				   		<s:if test="currentTenantId!='olivado' && currentTenantId!='pratibha'">
				   		{name:'totalNoOfBags',index:'totalNoOfBags',width:250,sortable:false,align:'right'},
				   		</s:if>
				   		<s:if test="currentTenantId=='awi'">
				   		{name:'totalNoOfFruitBags',index:'totalNoOfFruitBags',width:250,sortable:false,align:'right'},
				   		</s:if>
				   		//{name:'totalGrossWt',index:'totalGrossWt',width:250,sortable:false,align:'right'},
				   		//{name:'totalTareWt',index:'totalTareWt',width:250,sortable:false,align:'right'},
				   		{name:'totalNetwt',index:'totalNetwt',width:250,sortable:false,align:'right'},	
				   	 <s:if test="currentTenantId!='lalteer' && currentTenantId!='awi'">
				   		{name:'totalAmount',index:'totalAmount',width:250,sortable:false,align:'right'}
				   		</s:if>
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   scrollOffset: 0,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
				    onSelectRow: function(id){ 
						  document.detailform.id.value  =id;
					      document.detailform.submit();      
						}, 
				  	 //  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
					 
				
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        }
				});

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
		
				
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		


		function reloadGrid(){
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			//	alert(d1);
			var value1= d2[0];
			//alert(value1);
			var value2= d2[1];
			//alert(value2);
		
		
	
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
				
				jQuery("#detail").jqGrid('setGridParam',{url:"procurementProduct_data.action?",page:1}).trigger('reloadGrid');
			}				
		}

		function clear(){
			document.form.submit();
		}


	});

</script>


	<s:form name="form" action="procurementProduct_list">


	</s:form>




	<div class="btn-group" style="float: right; display: none">
		<a href="#" data-toggle="dropdown"
			class="btn btn-primary dropdown-toggle"> <s:text name="export" />
			<span class="caret"></span>
		</a>
		<ul class="dropdown-menu" role="menu">
			<li role="presentation"><a href="#" onclick="exportXLS()"
				tabindex="-1" role="menuitem"> <s:text name="excel" />
			</a></li>
			<%--    <li role="presentation">
            <a href="#" onclick="exportPDF();" tabindex="-1" role="menuitem">
         <s:text name="pdf"/>
            </a>
           </li> --%>
		</ul>
	</div>

	<sec:authorize ifAllGranted="profile.procurementProduct.create">
		<input type="BUTTON" class="btn btn-sts" id="add"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
	</sec:authorize>
	<s:form name="createform" action="procurementProduct_create">
	</s:form>
	<s:form name="detailform" action="procurementProduct_detail">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<div style="width: 98%; position: relative;" id="baseDiv">
				<table id="detail"></table>

				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>
		</div>
	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>
