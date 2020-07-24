<%@ include file="/jsp/common/grid-assets.jsp"%>



<head>
<meta name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript"><!--

	var receiptNo='';
	var farmerId='';
	var status=0;
	var sDate='';
	var eDate='';

	jQuery(document).ready(function(){

		document.getElementById('fieldl').selectedIndex=0;
		document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
		document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
		sDate = document.getElementById("startDate").value;
		eDate = document.getElementById("endDate").value;
		document.getElementById('receiptNo').value='';
		document.getElementById('farmerId').value='';
		document.getElementById('status').selectedIndex=0;		
		
		jQuery("#detail").jqGrid(
				{
					url:'offlineDistributionReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				   	postData:{				 
						  "startDate" : function(){
							  return sDate;
						  },
						  "endDate" : function(){
							  return eDate;
						  },
						  "filter.receiptNo" : function(){			  
							return receiptNo;
						  },
						  "filter.farmerId" : function(){
							  return farmerId;
							  
						  },
						  "status" : function(){
							  return status;							  
						  },
					},	
				   	colNames:[
				  		   	  '<s:text name="distributionDate"/>',
				  		   	  '<s:text name="receiptNo"/>',
				  		   	  '<s:text name="farmerId"/>',
				  		      '<s:text name="statusMsg"/>'
		 	      	 ],
				   	colModel:[
						{name:'distributionDate',index:'distributionDate',width:250,sortable:false},
						{name:'receiptNo',index:'receiptNo',width:250,sortable:false},
						{name:'farmerId',index:'farmerId',width:250,sortable:false},
				   		{name:'statusMsg',index:'statusMsg',width:250,sortable:false}
				   	],
				   onSelectRow: function(id){
					  var rowData = jQuery(this).getRowData(id); 
					   var statusMsg= rowData['statusMsg'];
					   if("PENDING"!= statusMsg.trim()){
						   popupWindowForReconcile(id);
					   }
				},
				   height: 301, 
				   width: $("#baseDiv").width(),
				   scrollOffset: 0,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
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
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>"); //<div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'offlineDistributionReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
									  '<s:text name="productCode"/>',
						  		   	  '<s:text name="quantity"/>',
						  		   	  '<s:text name="pricePerUnit"/>',			  		  	 
						  		   	  '<s:text name="subTotal"/>'
				 	      	 ],
						   	colModel:[	
									{name:'productCode',index:'productCode',width:150,sortable:false},
		   							{name:'quantity',index:'quantity',width:150,sortable:false},
		   							{name:'pricePerUnit',index:'pricePerUnit',width:150,sortable:false,align:'right'},
		   							{name:'subTotal',index:'subTotal',width:150,sortable:false,align:'right'}
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    autowidth: true,
						    viewrecords: true				
					   });
					 //  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
					   jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				},				
				
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
			var startDate=new Date(document.getElementById("startDate").value);
			var endDate=new Date(document.getElementById("endDate").value);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
				sDate = document.getElementById("startDate").value;
				eDate = document.getElementById("endDate").value;	
					
				receiptNo = document.getElementById("receiptNo").value;
				farmerId = document.getElementById("farmerId").value;
				status = document.getElementById("status").value;
				  
				jQuery("#detail").jqGrid('setGridParam',{url:"offlineDistributionReport_detail.action?",page:1}).trigger('reloadGrid');
			}	
		}

		function clear(){
			document.getElementById("status").value ="";
			document.form.submit();
		}

		

		jQuery("#minus").click( function() {
			if(document.getElementById(document.getElementById("fieldl").selectedIndex)!=null && (document.getElementById(document.getElementById("fieldl").selectedIndex).className=="" || document.getElementById(document.getElementById("fieldl").selectedIndex).className==" ")){
			removeFields();
			var divOption = document.getElementById("fieldl");
			if(divOption != null){
				if(divOption.selectedIndex == 1){
					document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
					document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
				}
			}
			divOption.selectedIndex=0;
			reloadGrid();
			}
		});

		jQuery("#plus").click( function() {
			showFields();
		});

		function popupWindowForReconcile(id) { 
			 if(confirm( '<s:text name="status.change"/>')){
				 $.post('offlineDistributionReport_reconcile.action', {'id':id},
				      function() {
				    	reloadGrid();
			});
			}
		}		
	
	});	

	function exportXLS(){
		if(isDataAvailable("#detail")){
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'offlineDistributionReport_populateXLS.action?'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
		
</script>

<div id="divs" align="right"><s:text name="field" /> <s:select
	name="fieldl" id="fieldl" list="fields" headerKey=""
	headerValue="%{getText('txt.select')}" theme="simple" /> &nbsp;
<button type="button" id="plus"><b><font color="green"><s:text
	name="+" /> </font></b></button>
&nbsp;
<button type="button" id="minus"><b><font color="green"><s:text
	name="-" /> </font></b></button>
</div>

<fieldset><legend><font color="#565656"> <!-- 291112 -->
<s:text name="filterSelection" /></font></legend> <s:form name="form" action="offlineDistributionReport_list"  style="width:600px; float:left;">

	<div id="1" class="hide">
	<table>
		<tr>
			<td style="width: 120px"><s:text name="startingDate" /></td>
			<td><s:textfield name="startDate" id="startDate" readonly="true"
				theme="simple" size="20" /></td>
			<td style="width: 120px;" align="center"><s:text name="endingDate" /></td>
			<td><s:textfield name="endDate" id="endDate" readonly="true"
				theme="simple" size="20" /></td>
		</tr>
	</table>
	<%--<table>
		<tr>
			<td style="width: 120px"><s:text name="EndingDate" /></td>
			<td><s:textfield name="endDate" id="endDate" readonly="true"
				theme="simple" size="20" /></td>
		</tr>
	</table>  --%> 
	</div>
	
	<div id="2" class="hide">
	<table>
		<tr>
			<td style="width: 120px"><s:text name="farmerId" /></td>
			<td><s:textfield name="filter.farmerId" id="farmerId" /></td>
		</tr>
	</table>
	</div>

	<div id="3" class="hide">
	<table>
		<tr>
			<td style="width: 120px"><s:text name="receiptNo" /></td>
			<td><s:textfield name="filter.receiptNo" id="receiptNo" /></td>
		</tr>
	</table>
	</div>

	<div id="4" class="hide">
	<table>
		<tr>
			<td style="width: 120px"><s:text name="status" /></td>
			<td><s:select name="status" theme="simple"
				list="statusList" headerKey="" headerValue="%{getText('txt.select')}"
				id="status" cssStyle="width:200px;margin:5px 0 5px 0;" />
			</td>
		</tr>
	</table>
	</div>

</s:form>

<div style="float:right;">
 	<div class="yui-skin-sam"><span id="button" class=""><span
		class="first-child">
	<button type="button" id="generate" class="search-btn"><b>
	<font color="#FFFFFF"><s:text name="generate.button" /> </font></b></button>
	</span></span> <span id="button" class=""><span class="first-child">
	<button type="button" id="clear" class="clear-btn"><b> <font
		color="#FFFFFF"><s:text name="clearDate" /> </font></b></button>
	</span></span>
	
	</div>
</div>
<div class="clear"></div>

</fieldset>
<br />

<br />

<div style="width: 98%; position:relative;" id="baseDiv">

<input type="BUTTON" class="xls-icon" style="margin-right:20px;position:absolute; right:-22px; top:-33px;display:none" onclick="exportXLS();" title="Export XLS" disabled="disabled"/>

<table id="detail"></table>

<div id="pagerForDetail"></div>
<div id="pager_id"></div>
</div>
<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>
