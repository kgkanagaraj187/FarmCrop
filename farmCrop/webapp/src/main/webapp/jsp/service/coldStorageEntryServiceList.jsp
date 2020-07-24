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

	
	var sDate='';
	var eDate='';


	jQuery(document).ready(function(){
		$("#bondNoDiv").hide();

		$('.applyBtn').click();
			
		jQuery("#detail").jqGrid(
				{
					url:'coldStorageEntry_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',				   	
					postData:{				 
						 
						},	
				   	colNames:[
							<s:if test='branchId==null'>
							'<s:text name="app.branch"/>',
							</s:if>
				   	       '<s:property value="%{getLocaleProperty('date')}" />',
				   	       '<s:property value="%{getLocaleProperty('farmer')}" />',
				   	       '<s:property value="%{getLocaleProperty('warehouse')}" />',
				   	   	   '<s:property value="%{getLocaleProperty('coldStorageName')}" />',
				   	   	   '<s:property value="%{getLocaleProperty('batchNo')}" />',
				   	    <s:if test="currentTenantId=='griffith'">
				   	   	'<s:property value="%{getLocaleProperty('bond.status')}" />',
					   	   </s:if>
				   	       '<s:property value="%{getLocaleProperty('bondNo')}" />',
				   	       '<s:property value="%{getLocaleProperty('totalNoOfBags')}" />',
				   	       '<s:property value="%{getLocaleProperty('totalQty')}" />',
				   	    <s:if test="currentTenantId=='griffith'">
				   	    '<s:property value="%{getLocaleProperty('editBtn')}" />',
				   	   </s:if>
				  		    
		 	      	 ],
				   	colModel:[
						<s:if test='branchId==null'>
						{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
						</s:if>		
						{name:'procurementDate',index:'procurementDate',width:250,sortable:false},
						{name:'farmer',index:'farmer',width:250,sortable:false},	
						{name:'warehouse',index:'warehouse',width:250,sortable:false},						
				   		{name:'coldStorageName',index:'coldStorageName',width:250,sortable:false},
				   		{name:'batchNo',index:'batchNo',width:250,sortable:false},
				   	 <s:if test="currentTenantId=='griffith'">
				   		{name:'bondStatus',index:'bondStatus',width:250,sortable:false},
				   		</s:if>	
				   		{name:'bondNo',index:'bondNo',width:250,sortable:false},				   	
				   		{name:'totalNoOfBags',index:'totalNoOfBags',width:250,sortable:false},
				   		{name:'totalQty',index:'totalQty',width:250,sortable:false},
				        <s:if test="currentTenantId=='griffith'">
				   		{name:'editBtn',index:'editBtn',width:250,sortable:false},
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
						 // document.detailform.id.value  =id;
					     // document.detailform.submit();      
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

	function populateModalWindow(obj){
		
		var array = $(obj).data("backend-values").split(",");
		
			
			$("#updateBtn").remove();
			var add_btn = $('<button/>',{
				id	:	"updateBtn",
				class	:	"btn btn-success",
				text	:	"Update",
				style	:	"border-radius: 7px;margin-top: 10px;",//background-color: #2a3f54;
				//"data-dismiss":"modal"
				onclick : 	"updateColdStorage("+array[1]+");"
			});
			$( ".btn.btn-secondary" ).before( $(add_btn) );
			
			/* $("#myModal").modal({
	            backdrop: 'static',
	            keyboard: false
	        }); */

		
	}
	
	function updateColdStorage(id){
		
		var bondStatus = $("#bondStatus").val();
		var bondNo = $("#bondNo").val();
		
		//alert(bondStatus);
		if(!isEmpty(bondStatus)){
			if(bondStatus == 2 && isEmpty(bondNo)){
				alert("Bond no should not be empty");
			//	alert("AAAA");
				return false;
				

			}else{
//alert("CCCC");
				var dataa = {};
				dataa["coldStorageId"] = id;
				dataa["bondStatus"] = bondStatus;
				dataa["bondNo"] = bondNo;
				
				$.ajax({
				    url: "coldStorageEntry_editColdStorageBondNo.action",
				    type: 'POST',
				    async: false,
				    data:dataa,
				    success: function (result) {
				    	$('#detail').trigger( 'reloadGrid' );
				    	$('#myModal').modal('hide');
				    	showPopup("Update Successfully",result.title);
				    	$("#bondNo").val("");
				    	$('#bondStatus').val("").trigger('change');
						
				    	
				    }
				}); 
			}
		}
		
			
		
		//alert("BBBB");
		
		
	}
	 function enableBondNo(val){
		
			 if(val == "2"){
				 $("#bondNoDiv").show();
			 }else{
				 $("#bondNoDiv").hide();
			 }
			
		
	 }
	function refreshPopup(){
		$("#bondStatus").val('').trigger('change');
		$("#bondNo").val("");
		
	}
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

	<sec:authorize ifAllGranted="service.coldStorageEntryService.create">
		<input type="BUTTON" class="btn btn-sts" id="add"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
	</sec:authorize>
	<s:form name="createform" action="coldStorageEntry_create">
	</s:form>
	<s:form name="detailform" action="coldStorageEntry_detail">
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



	<div class="container">

		<!-- Button to Open the Modal -->
		<!-- <button type="button" class="btn btn-primary" data-toggle="modal"
			data-target="#myModal">Open modal</button> -->

		<!-- The Modal -->
		<div class="modal fade" id="myModal">
			<div class="modal-dialog modal-dialog-centered">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title"><s:property value="%{getLocaleProperty('modal-header-cold-storage-entry-list')}" /></h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body">



						<form class="form-horizontal">
							<div class="form-group">
								<label class="control-label col-sm-2" for="txt"><s:property
										value="%{getLocaleProperty('bond.status')}" /></label>
								<div class="col-sm-4">
									<s:select name="bondStatus" list="listBondStatus" headerKey=""
										headerValue="%{getText('txt.select')}" listKey="key"
										listValue="value" onchange="enableBondNo(this.value);"
										id="bondStatus" cssClass="form-control input-sm select2" />
								</div>
							</div>
							<div class="form-group" id="bondNoDiv">
								<label class="control-label col-sm-2" for="txt"><s:property
										value="%{getLocaleProperty('bondNo')}" /> <sup style="color: red;">*</sup></label>
								<div class="col-sm-2">
									<s:textfield name="selectedBondNo" id="bondNo" maxlength="25" />
								</div>
							</div>

						</form>


					</div>

					<!-- Modal footer -->
					<div class="modal-footer">
						<button style="border-radius: 7px;margin-top: 10px;" type="button" class="btn btn-secondary btn-warning" data-dismiss="modal" onclick="refreshPopup()">Close</button>
					</div>

				</div>
			</div>
		</div>

	</div>

</body>
