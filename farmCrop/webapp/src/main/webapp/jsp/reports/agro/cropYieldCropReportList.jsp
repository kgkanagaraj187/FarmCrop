<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
</head>
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
<body>
	<script type="text/javascript">

var farmCropCode='';
var listAction = "cropYieldCropReport_list.action";
var dataAction = "cropYieldCropReport_data.action";
var subListAction ="cropYieldCropReport_subList.action?selectedCropCode=";

	$(document).ready(function(){
		document.getElementById('fieldl').selectedIndex=0;
		document.getElementById('farmCropId').value='';
		
		jQuery("#detail").jqGrid(
		{
			url:dataAction,
   		   	pager: '#pagerForDetail',
   			mtype: 'POST',
   		   	datatype: "json",
   			postData:{
				  "selectedCropCode" : function(){			  
						return farmCropCode;
				  }
			}, 
   		   		
   		   	colNames:[	
   		   		 		'<s:text name="farmcrops.code"/>',
		  		      '<s:text name="farmcrops.name"/>',			   	       	  
   		  		   	  '<s:text name="totalCropArea"/>',
   		  		      '<s:text name="totalProductionPerYear"/>'
   			  		    
   		      	 ],
   		   	colModel:[	
				{name:'code',index:'fcm.code', width:125,sortable:false,search:false},
				{name:'name',index:'fcm.name', width:125,sortable:false,search:false},
   		   		{name:'cropArea',index:'cropArea', width:125,sortable:false,search:false},
   		   		{name:'productionPerYear',index:'productionPerYear', width:125,sortable:false,search:false}
   		   	    
   		   	],
   		   	height: 301, 
   		    width: $("#baseDiv").width(),
   		    scrollOffset: 0,
   		   	rowNum:10,
   		   	rowList : [10,25,50],
   		    sortname:'id',			  
   		    sortorder: "desc",
   		    viewrecords: true,
	   		 subGrid: true,
			   subGridOptions: {
				   "plusicon" : "glyphicon-plus-sign",
				   "minusicon" : "glyphicon-minus-sign",
				   "openicon" : "glyphicon-hand-right",
			   },


				subGridRowExpanded: function(subgrid_id, row_id){
				   	
				   var subgrid_table_id, pager_id; 
				   subgrid_table_id = subgrid_id+"_t"; 
				   pager_id = "p_"+subgrid_table_id; 
				 $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
				   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
				   			   	jQuery("#"+subgrid_table_id).jqGrid(
							{
								url:subListAction+row_id,
							   	pager: pager_id,
							   	datatype: "json",	
							   	colNames:[				   	       	  
										 
							   	      	   '<s:text name="villageCode"/>',
   		  		      						'<s:text name="villageName"/>',
   		  		      						'<s:text name="cropArea"/>',
					   		  		  	   '<s:text name="productionPerYear"/>'			  		     
							  		      
					 	      	 ],
							   	colModel:[						
							   		{name:'villageCode',index:'villageCode', width:125,sortable:false,search:false},
							   		{name:'villageName',index:'villageName', width:125,sortable:false,search:false},	
									{name:'cropArea',index:'cropArea', width:125,sortable:false,search:false},
									{name:'productionPerYear',index:'productionPerYear', width:125,sortable:false,search:false}	   						
			   		
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


				    jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
				   			//jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
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

	    jQuery("#generate").click( function() {			
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();

		});	

		function reloadGrid(){
			farmCropCode = document.getElementById("farmCropId").value;
			jQuery("#detail").jqGrid('setGridParam',{url:dataAction,page:1})
				.trigger('reloadGrid');		
		}

		function clear(){		
			document.form.action = listAction;
			document.form.submit();			
		}

						});
	</script>
	<div id="divs" align="right" class="hide">

		<%-- <s:text name="field" />  --%>

		<div class="row">

			<div class="col-md-3"></div>

			<div class="col-md-3"></div>

			<div class="col-md-6" style="width: 42%; float: right;">

				<div class="" style="width: 300px; float: left;">
					<s:select name="fieldl" id="fieldl" list="fields"
						class="form-control" headerKey=""
						headerValue="%{getText('txt.select')}" theme="simple" />
				</div>

				<div class="" style="width: 52px; float: left;">
					<button type="button" class="btn btn-sts" aria-hidden="true"
						id="plus">
						<b class="fa fa-plus"></b>
					</button>
				</div>

				<div class="" style="width: 52px; float: left;">
					<button type="button" class="btn btn-sts" aria-hidden="true"
						id="minus">
						<b class="fa fa-minus"></b>
					</button>
				</div>

			</div>

		</div>



	</div>


	<s:form name="form" action="cropYieldCropReport_list">

		<div class="well well-sm">
			<div class="form-inline">
				<div id="searchbtn">
					<label><s:text name="farmCrop" /></label>
					<s:select id="farmCropId" name="selectedCropCode" headerKey=""
						headerValue="%{getText('txt.select')}" list="farmCropsList"
						class="form-control input-sm select2" cssStyle="width:150px" />
				</div>
				<div class="left-space">
					<button type="button" class="btn btn-primary" aria-hidden="true"
						id="generate" title='<s:text name="Search" />'>
						<b class="fa fa-search"></b>
					</button>

					<button type="button" class="btn btn-warning" aria-hidden="true"
						id="clear" title='<s:text name="Clear" />'>
						<b class="fa fa-times"></b>
					</button>
				</div>
			</div>
		</div>
	</s:form>





	<%-- 	<div class="btn-group" style="float: right;">
		<a href="#" data-toggle="dropdown"
			class="btn btn-primary dropdown-toggle"> <s:text name="export" />
			<span class="caret"></span>
		</a>
		<ul class="dropdown-menu" role="menu">
			<li role="presentation"><a href="#" onclick="exportXLS()"
				tabindex="-1" role="menuitem"> <s:text name="excel" />
			</a></li>
			<li role="presentation"><a href="#" onclick="exportPDF();"
				tabindex="-1" role="menuitem"> <s:text name="pdf" />
			</a></li>
		</ul>
	</div> --%>
	<div class="clear"></div>
	<div style="width: 98%; padding-top: 20px" id="baseDiv">
		<table id="detail"></table>

		<div id="pagerForDetail"></div>
		<div id="pager_id"></div>
	</div>
	<s:form name="detailForm" action="cropYieldCropReport_data">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
</body>