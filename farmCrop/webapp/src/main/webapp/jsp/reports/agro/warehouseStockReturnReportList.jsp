<%@ include file="/jsp/common/grid-assets.jsp"%>



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
</head>
<body>
<script type="text/javascript">

	jQuery(document).ready(function(){
		document.getElementById('fieldl').selectedIndex=0;
		document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
		document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
		document.getElementById("warehouseId").selectedIndex=0;
		document.getElementById("orderNoId").selectedIndex=0;
		document.getElementById("vendorId").selectedIndex=0;
		document.getElementById("returnType").selectedIndex=0;
		jQuery("#detail").jqGrid(
				{
					url:'warehouseStockReturnReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',		
				   	postData:{	
					"startDate" : function(){
                     return document.getElementById("startDate").value;
                    },		
                    "endDate" : function(){
		               return document.getElementById("endDate").value;
	                 },
	                 "warehouse":function(){
		               return document.getElementById("warehouseId").value;
	                 },
	                 "order":function(){
			           return document.getElementById("orderNoId").value;
		             },
	                 "vendor":function(){
			               return document.getElementById("vendorId").value;
		              },
		             "returnType":function(){
				           return document.getElementById("returnType").value;
			         }
				        
				 },
				
				 colNames:[
				  		   	  '<s:text name="returnType"/>',
				  		   	  '<s:text name="dateofReturn"/>',
				  		   	  '<s:text name="vendor"/>',
				  		   	  '<s:text name="warehouse"/>',
				  		      '<s:text name="orderNo"/>',
					  	   	  '<s:text name="category"/>',
				  		   	  '<s:text name="products"/>',
				  		   	  '<s:text name="returnQuantity"/>',
				  		   	  '<s:text name="amount"/>',
				  		      '<s:text name="totalAmt"/>',
				  		      '<s:text name="remarks"/>'
				             ],
				    datatype: "json",
				   	colModel:[
						{name:'returnType',index:'returnType',width:40,sortable:false},
						{name:'dateofReturn',index:'dateofReturn',width:40,sortable:false},
					    {name:'vendor',index:'vendor',width:40,sortable:false},
					    {name:'warehouse',index:'warehouse',width:40,sortable:false},
					    {name:'orderNo',index:'orderNo',width:40,sortable:false},
						{name:'category',index:'category',width:40,sortable:false},
						{name:'products',index:'products',width:40,sortable:false},
					    {name:'returnQuantity',index:'returnQuantity',width:40,sortable:false},
					    {name:'amount',index:'amount',width:40,sortable:false},
					    {name:'totalAmt',index:'totalAmt',width:40,sortable:false},
					    {name:'remarks',index:'remarks',width:40,sortable:false}
					    
					    
				         	],
				         	 height: 301, 
							   width: $("#baseDiv").width(),
							   scrollOffset: 0,     
							   sortname:'id',	
							   sortorder: "asc",
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
					
					jQuery("#clear").click( function() {
						clear();
					});	

					function reloadGrid(){
					jQuery("#detail").jqGrid('setGridParam',{url:"warehouseStockReturnReport_detail.action?",page:1}).trigger('reloadGrid');
					}

					function clear(){		
						document.form.submit();
					}
					
					jQuery("#minus").click( function() {
						if(document.getElementById(document.getElementById("fieldl").selectedIndex)!=null && (document.getElementById(document.getElementById("fieldl").selectedIndex).className=="" || document.getElementById(document.getElementById("fieldl").selectedIndex).className==" ")){
							removeFields();
							var divOption = document.getElementById("fieldl");
							divOption.selectedIndex=0;
							reloadGrid();
							}
					});

					jQuery("#plus").click( function() {
						showFields();
					});
				});
								 
</script>

<div id="divs" align="right"><s:text name="field" /> <s:select
	name="fieldl" id="fieldl" list="fields" headerKey=""
	headerValue="%{getText('txt.select')}" theme="simple" /> &nbsp;
<button type="button" id="plus"><b><font color="green"><s:text
	name="+" /> </font></b></button>
<button type="button" id="minus"><b><font color="green"><s:text
	name="-" /> </font></b></button>

</div>
<fieldset><legend><font color="#565656"> <!-- 291112 -->
<s:text name="filter" /></font></legend> <s:form name="form"
	action="warehouseStockReturnReport_list"
	style="width:600px; float:left;">
	<div id="1" class="hide">
	<table>
		<tr>
			<td style="width: 80px"><s:text name="startingDate" /></td>
			<td><s:textfield name="startDate" id="startDate" readonly="true"
				theme="simple" size="20" /></td>
			<td style="width: 80px;" align="center"><s:text
				name="endingDate" /></td>
			<td><s:textfield name="endDate" id="endDate" readonly="true"
				theme="simple" size="20" /></td>
		</tr>
	</table>
	</div>
	<div id="2" class="hide">
	<table>
		<tr>
			<td style="width: 80px"><s:text name="warehouse" /></td>
			<td><s:select name="warehouse" id="warehouseId"
				list="warehouseList" headerKey="" headerValue="%{getText('txt.select')}"
				cssStyle="width:200px;margin:5px 0 5px 0;" /></td>
		</tr>
	</table>
	</div>
	<div id="3" class="hide">
	<table>
		<tr>
			<td style="width: 100px"><s:text name="orderNo" /></td>
			<td>
			<s:select name="order" list="orderNoList" id="orderNoId"  listKey="orderNo" listValue="orderNo" headerKey="" headerValue="%{getText('txt.select')}" cssStyle="width:200px;margin:5px 0 5px 0;" />
			</td>
		</tr>
	</table>
	</div>
	<div id="4" class="hide">
	<table>
		<tr>
			<td style="width: 80px"><s:text name="vendor" /></td>
			<td><s:select name="vendor" id="vendorId"
				list="vendorList" headerKey="" headerValue="%{getText('txt.select')}"
				cssStyle="width:200px;margin:5px 0 5px 0;" /></td>
		</tr>
	</table>
	</div>
	<div id="5" class="hide">
	<table>
		<tr>
			<td style="width: 100px"><s:text name="returnType" /></td>
			<td><s:select name="returnType" id="returnType"
				list="stockReturnTypeList" headerKey="" headerValue="%{getText('txt.select')}" listKey="returnType" listValue="returnType"
				cssStyle="width:200px;margin:5px 0 5px 0;" /></td>
		</tr>
	</table>
	</div>
</s:form>
<div style="float: right;">
<div class="yui-skin-sam"><span id="button" class=""><span
	class="first-child">
<button type="button" id="generate" class="search-btn"><b>
<font color="#FFFFFF"><s:text name="generate.button" /> </font></b></button>
</span></span> <span id="button" class=""><span class="first-child">
<button type="button" id="clear" class="clear-btn"><b> <font
	color="#FFFFFF"><s:text name="clearDate" /> </font></b></button>
</span></span></div>

</div>
<div class="clear"></div>
</fieldset>

<br />
<br />

<div style="width:98%;" id="baseDiv">
<table id="detail"></table>

<div id="pagerForDetail"></div>
<div id="pager_id"></div>
</div>
<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>
