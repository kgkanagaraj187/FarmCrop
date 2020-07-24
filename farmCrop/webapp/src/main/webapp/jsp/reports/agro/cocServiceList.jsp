<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
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
	<script type="text/javascript">

$(document).ready(function(){	
	jQuery("#detail").jqGrid(
			{
				url:'cultivationReport_populateData.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			    styleUI : 'Bootstrap',
				mtype: 'POST',
				colNames:[
			  	          //'<s:text name="expenseDate"/>',
			  	           <s:if test='branchId==null'>
							'<s:text name="%{getLocaleProperty('app.branch')}"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						
							'<s:text name="app.subBranch"/>',
						</s:if>
							'<s:property value="%{getLocaleProperty('cSeasonCode')}" />',
					 	<s:if test="farmerCodeEnabled==1">
							<s:if test="branchId!='organic' ">
					'<s:property value="%{getLocaleProperty('farmerCode')}" />',
					 	</s:if>
					 	</s:if>
					 	 
			  		   	  '<s:property value="%{getLocaleProperty('farmerName')}" />',
			  		  // 	'<s:property value="%{getLocaleProperty('fatherName')}"/>',
			  		   	 '<s:text name="farm"/>',
			  		   	  '<s:text name="village"/>',
			  		   /*  <s:if test="currentTenantId!='pratibha'">
			  		   	'<s:property value="%{getLocaleProperty('taluk')}"/>',
			  			'<s:property value="%{getLocaleProperty('district')}"/>',
			  		      // '<s:text name="taluk"/>', 
			  		      //'<s:text name="district"/>',
			  		      '<s:text name="state"/>',
			  		      </s:if> */
			  		   
			  		  '<s:property value="%{getLocaleProperty('landPreparation')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',	
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('landLabourCost')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		    '<s:property value="%{getLocaleProperty('sowing')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('sowingLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		  '<s:property value="%{getLocaleProperty('gapFilling')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		'<s:property value="%{getLocaleProperty('gapLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		</s:if>
			  		   	  '<s:property value="%{getLocaleProperty('weed')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		    <s:if test="currentTenantId=='pratibha'">
			  		   	'<s:property value="%{getLocaleProperty('weedLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   	</s:if>
			  		   	'<s:property value="%{getLocaleProperty('interCultural')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('interCulturalLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		      '<s:property value="%{getLocaleProperty('irrigation')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		    <s:if test="currentTenantId=='pratibha'">
			  		    '<s:property value="%{getLocaleProperty('irrigationLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		    </s:if>
			  		    
			  		   	'<s:property value="%{getLocaleProperty('fertilizer')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('fertilizerLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  			'<s:property value="%{getLocaleProperty('pesticide')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  			 <s:if test="currentTenantId=='pratibha'">
			  			'<s:property value="%{getLocaleProperty('pesticideLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  			</s:if>
			  		   	'<s:text name="fym"/>',
			  		  <s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('fymLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  </s:if>
			  		   	  '<s:text name="harvesting"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   	<s:if test="currentTenantId=='pratibha'">
			  		  '<s:property value="%{getLocaleProperty('harvestLabour')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  '<s:property value="%{getLocaleProperty('transExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		'<s:property value="%{getLocaleProperty('fuelExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		'<s:property value="%{getLocaleProperty('otherExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		</s:if>
			  		   	  '<s:property value="%{getLocaleProperty('totalExpenses')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		  	 /*  '<s:text name="agriIncome"/>',
			  		   	 '<s:text name="interCropIncome"/>',
			  		   	 '<s:text name="otherExpenseIncome"/>', */
			  		   
			  		   '<s:text name="%{getLocaleProperty('labourCost')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		   
			  		    '<s:text name="%{getLocaleProperty('totalCost')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
			  		 
			             ],
			    datatype: "json",
			    colModel:[
		                   // {name:'expenseDate',index:'expenseDate',width:40,sortable:false},
		                     <s:if test='branchId==null'>
						   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
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
						   		
						   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
						   		</s:if>
							{name:'cSeasonCode',index:'cSeasonCode',sortable:false},
							 <s:if test="farmerCodeEnabled==1">
							<s:if test="branchId!='organic'">
							{name:'farmerId',index:'farmerId',sortable:false},
							</s:if>
							</s:if>
							
							{name:'farmerName',index:'farmerName',sortable:false},
							//{name:'fatherName',index:'fatherName',width:120,sortable:false},
							
							{name:'farmName',index:'farmName',sortable:false},
		     				 {name:'village',index:'village',sortable:false},
		     				   /* <s:if test="currentTenantId!='pratibha'">
						    {name:'taluk',index:'taluk',width:150,sortable:false},
							{name:'district',index:'district',align:'right',width:150,sortable:false},
						    {name:'state',index:'state',width:150,sortable:false},
						    </s:if> */
						   
							{name:'landPreparation',index:'landPreparation',sortable:false,align:'right'},
							 <s:if test="currentTenantId=='pratibha'">
							{name:'landLabour',index:'landLabour',sortable:false,align:'right'},
							</s:if>
						    {name:'sowing',index:'sowing',sortable:false,align:'right'},
						    <s:if test="currentTenantId=='pratibha'">
						    {name:'sowingLabour',index:'sowingLabour',sortable:false,align:'right'},
						    </s:if>
						    {name:'gapFilling',index:'gapFilling',sortable:false,align:'right'},
						    <s:if test="currentTenantId=='pratibha'">
						    {name:'gapLabour',index:'gapLabour',sortable:false,align:'right'},
						    </s:if>
							{name:'weeding',index:'weeding',align:'right',sortable:false,align:'right'},
							 <s:if test="currentTenantId=='pratibha'">
							{name:'weedLabour',index:'weedLabour',align:'right',sortable:false,align:'right'},
							</s:if>
						    {name:'interCultural',index:'interCultural',sortable:false,align:'right'},
						    <s:if test="currentTenantId=='pratibha'">
						    {name:'interCulturalLabour',index:'interCulturalLabour',sortable:false,align:'right'},
						    </s:if>
							{name:'irrigation',index:'irrigation',sortable:false,align:'right'},
							 <s:if test="currentTenantId=='pratibha'">
							{name:'irrigationLabour',index:'irrigationLabour',sortable:false,align:'right'},
							</s:if>
						    {name:'fertilizer',index:'fertilizer',sortable:false,align:'right'},
						    <s:if test="currentTenantId=='pratibha'">
						    {name:'fertilizerLabour',index:'fertilizerLabour',sortable:false,align:'right'},
						    </s:if>
						    {name:'pesticide',index:'pesticide',sortable:false,align:'right'},
						    <s:if test="currentTenantId=='pratibha'">
						    {name:'pesticideLabour',index:'pesticideLabour',sortable:false,align:'right'},
						    </s:if>
						    {name:'fym',index:'fym',sortable:false,align:'right'},
						    <s:if test="currentTenantId=='pratibha'">
						    {name:'fymLabour',index:'fymLabour',sortable:false,align:'right'},
						    </s:if>
							{name:'harvesting',index:'harvesting',align:'right',sortable:false,align:'right'},
							 <s:if test="currentTenantId=='pratibha'">
							{name:'harvestLabour',index:'harvestLabour',align:'right',sortable:false,align:'right'},
							{name:'transExpenses',index:'transExpenses',align:'right',sortable:false,align:'right'},
							{name:'fuelExpenses',index:'fuelExpenses',align:'right',sortable:false,align:'right'},
							{name:'otherExpeness',index:'otherExpeness',align:'right',sortable:false,align:'right'},
							</s:if>
							{name:'totOtherExpenses',index:'totOtherExpenses',align:'right',sortable:false,align:'right'},
							/* {name:'agriIncome',index:'agriIncome',align:'right',width:40,sortable:false},
							{name:'interCropIncome',index:'interCropIncome',align:'right',width:40,sortable:false},
							{name:'otherExpenseIncome',index:'otherExpenseIncome',align:'right',width:40,sortable:false}, */
							
							{name:'labour',index:'labour',align:'right',sortable:false},
							
							{name:'total',index:'total',align:'right',sortable:false},
							 
					         	],
			         	 height: 300, 
						   width: $("#baseDiv").width(),
						   autowidth:true,
						   shrinkToFit:true,		
						   scrollOffset: 0,     
						   sortname:'id',	
						   sortorder: "asc",
						   rowNum:10,
						   rowList : [10,25,50],
						   viewrecords: true,  
						   /* caption: "Filter",  */

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
					jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});
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
					    
					jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
		    
		     	 $('#detail').jqGrid('setGridHeight',(windowHeight));
		     
		});	           
	
		
</script>
	<div class="appContentWrapper marginBottom">
		<%-- <sec:authorize ifAllGranted="profile.customer.create">
			<input type="BUTTON" id="add" class="btn btn-sts"
				value="<s:text name="create.button"/>"
				onclick="document.createform.submit()" />
		</sec:authorize> --%>

		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>
	
	<s:form name="updateform" action="cultivation_detail?type=service">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
</body>
