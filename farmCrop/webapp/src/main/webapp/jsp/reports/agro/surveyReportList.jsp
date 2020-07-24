<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>

<script src="plugins/openlayers/OpenLayers.js"></script>

<!-- <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwN5CwZmhwRU1b0qxHHMVAkx2xwOY9_kU"
type="text/javascript"></script> -->

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
	<script>
	var surveyEdit = '';
	var lang ='en';
	$(document).ready(function(){
		loadGrid();
		jQuery("#generate").click( function() {
			jQuery("#detail").jqGrid('setGridParam',{url:'surveyReport_data.action',page:1})
			.trigger('reloadGrid');		
		});
		
		 surveyEdit="<%=request.getParameter("surveyEdit")%>";
		jQuery("#clear").click( function() {
			if(surveyEdit!='' && surveyEdit=="1"){
			window.location.href="surveyReport_list.action?surveyEdit=1";
			}else{
				window.location.href="surveyReport_list.action";
			}
		});	
	
		 if(surveyEdit!='' && surveyEdit=="1"){
			 $(".breadCrumbNavigation").find('li:not(:first)').remove();
				
				$(".breadCrumbNavigation").append("<li><a href='surveyReport_list.action?surveyEdit=1'> <s:text name='service.editSurvey'/> </a></li> ");
				$(".navmenu>li.open").removeClass("open");
				$(".navmenu>li.active").removeClass("active");
				$('.service').addClass("active open");
				
			
		 }
	    $("#answeredDate").daterangepicker({
	        dateFormat: "dd-M-yy",
	        minDate: 0,
	        onSelect: function () {
	            var dt2 = $('#answeredDate');
	            var startDate = $(this).daterangepicker('getDate');
	            //add 30 days to selected date
	            startDate.setDate(startDate.getDate() + 30);
	            var minDate = $(this).daterangepicker('getDate');
	            //minDate of dt2 datepicker = dt1 selected day
	            dt2.daterangepicker('setDate', minDate);
	            //sets dt2 maxDate to the last day of 30 days window
	            dt2.daterangepicker('option', 'maxDate', startDate);
	            //first day which can be selected in dt2 is selected date in dt1
	            dt2.daterangepicker('option', 'minDate', minDate);
	            //same for dt1
	            $(this).daterangepicker('option', 'minDate', minDate);
	        }
	    });
	});
	
	function reloadGrid(flag){
		jQuery("#detail").jqGrid('setGridParam',{url:'surveyReport_data.action',page:1})
		.trigger('reloadGrid');		
	}
	
	var fProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
	var tProjection   = new OpenLayers.Projection("EPSG:900913");
	var url = window.location.href;
	var temp = url;
	for(var i = 0 ; i < 1 ; i++) {
	temp = temp.substring(0, temp.lastIndexOf('/'));
	}
	var href = temp;
	var coordinateImg = "red_placemarker.png";
	var iconImage = temp + '/img/' + coordinateImg;
	
		function loadGrid(){
			
			jQuery("#detail").jqGrid(
					{
					   	url:'surveyReport_data.action',
					   	pager: '#pagerForDetail',	  
					   	datatype: "json",	
					   	mtype: 'POST',
					   	styleUI : 'Bootstrap',
						postData:{
							  "branchIdParma" : function(){
								  return  document.getElementById("branchIdParma").value;
							  },
							  "answeredDate" : function(){			  
								return  document.getElementById("answeredDate").value;
							  },
							  "farmerId" : function(){			  
									return  document.getElementById("farmerId").value;
								  },
							"surveyCode" : function(){			  
										return  document.getElementById("surveyCode").value;
									  },
									  "selectedSamithi" : function(){			  
											return  document.getElementById("samithi").value;
										  },
						},
					   	colNames:[
					   		<s:if test='branchId==null'>
					   	 '<s:property value="%{getLocaleProperty('app.branch')}" />',
					   			
					   	     </s:if>
					   	     <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   	 	 '<s:property value="%{getLocaleProperty('app.subBranch')}" />',
							 </s:if>
					   	  '<s:property value="%{getLocaleProperty('fcpa.answeredDate')}" />',
					   	 '<s:property value="%{getLocaleProperty('fcpa.surveyCode')}" />',
					   	 '<s:property value="%{getLocaleProperty('fcpa.surveyName')}" />',
					   	 '<s:property value="%{getLocaleProperty('category.filter')}" />',
					   	 '<s:property value="%{getLocaleProperty('fcpa.fOName')}" />',
					   	 '<s:property value="%{getLocaleProperty('farm.farmerName')}" />',
					  		
					  		
					      	 	 ],
					   	colModel:[	
							<s:if test='branchId==null'>
						   		{name:'branchId',index:'branchId',sortable: false,width :125,search:false,stype: 'select',searchoptions: {
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
						   	{name:'answeredDate',index:'answeredDate',sortable:false,align:'center',search:false},
					   		{name:'surveyCode',index:'surveyCode',sortable:false,align:'center',search:false,},
					   		{name:'surveyName',index:'surveyName',sortable:false,align:'center',search:false},
					   		{name:'surveyName',index:'surveyName',sortable:false,align:'center',search:false},
					   		{name:'categoryName',index:'categoryName',sortable:false,align:'center',search:false},
					   		{name:'farmerId',index:'farmerId',sortable:false,align:'center',search:false},
					   			 ],
					   			 
					   	height: 301, 
					    width: $("#baseDiv").width(), // assign parent div width
					    scrollOffset: 19,
					    shrinkToFit:true,
					   	rowNum:10,
					   	rowList : [10,25,50],
					    sortname:'id',			  
					    sortorder: "desc",
					    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				        onSortCol: function (index, idxcol, sortorder) {
					        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				        },
				        onSelectRow: function(id){ 
							  document.detailForm.id.value  =id;
							  document.detailForm.lang.value  =lang;
							  if(surveyEdit!='' && surveyEdit=="1"){
							  document.detailForm.action='surveyReport_detail.action?surveyEdit=1';
							  }
					          document.detailForm.submit();      
							},
					});
					
					jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
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
			
		}
		
	
			function exportXLS(){
				var count=jQuery("#detail").jqGrid('getGridParam', 'records');
			if(isDataAvailable("#detail")){
					jQuery("#detail").setGridParam ({postData: {rows : 0}});
					jQuery("#detail").jqGrid('excelExport', {url: 'surveyReport_populateXLS.action'});
				}else{
				     alert('<s:text name="export.nodata"/>');
				}
			}		
			
			
	</script>

	<div class="appContentWrapper marginBottom">
		<div class="reportFilterWrapper filterControls">
			<div class="reportFilterItem">
				<label for="txt"><s:property
						value="%{getLocaleProperty('app.branch')}" /></label>
				<div class="form-element">
					<s:select name="branchIdParma" id="branchIdParma"
						list="parentBranches" headerKey="" headerValue="Select"
						cssClass="select2" />
				</div>
			</div>

			<div class="reportFilterItem">
				<label for="txt"><s:property
						value="%{getLocaleProperty('profile.samithi')}" /></label>
				<div class="form-element">
					<s:select name="samithiName" id="samithi" list="samithis"
						headerKey="" headerValue="%{getText('txt.select')}"
						cssClass="form-control select2" />

				</div>
			</div>

			<div class="reportFilterItem">
				<label for="txt"><s:property
						value="%{getLocaleProperty('answeredDate')}" /></label>
				<div class="form-element">
					<s:textfield name="answeredDate" id="answeredDate"
						style="border: 1px solid !important;" />

				</div>
			</div>
			<div class="reportFilterItem">
				<label for="txt"><s:property
						value="%{getLocaleProperty('farm.farmerName')}" /></label>
				<div class="form-element">
					<s:select name="farmerId" id="farmerId" list="farmerList"
						headerKey="" headerValue="%{getText('txt.select')}"
						cssClass="form-control select2" />

				</div>
			</div>
			<div class="reportFilterItem">
				<label for="txt">
					<s:property value="%{getLocaleProperty('survey')}" escape="false" /></label>
				<div class="form-element">
					<s:select name="surveyCode" id="surveyCode" list="surveyMasterList"
						headerKey="" headerValue="%{getText('txt.select')}"
						cssClass="form-control select2" />

				</div>
			</div>
			<div style="margin-top: 24px;">
				<button type="button" class="btn btn-success btn-sm" id="generate"
					aria-hidden="true">
					<b class="fa fa-search"></b>
				</button>
				<button type="button" class="btn btn-danger btn-sm"
					aria-hidden="true" id="clear">
					<b class="fa fa-close"></b>
				</button>
			</div>

		</div>
	</div>


	<div class="appContentWrapper marginBottom">
	<s:if test='surveyEdit=="" || surveyEdit==null'>
		<div class="flex-layout reportData">
		
			<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
						type="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">

						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>



		</div>
		</s:if>

		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>

	</div>

	<button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>

	<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead"></h4>
				</div>
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						<div class="carousel-inner" role="listbox" id="mbody"></div>

						<a class="left carousel-control" href="#myCarousel" role="button"
							data-slide="prev"> <span
							class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
							<span class="sr-only">Previous</span>
						</a> <a class="right carousel-control" href="#myCarousel"
							role="button" data-slide="next"> <span
							class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
							<span class="sr-only">Next</span>
						</a>

					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonEdcCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>

	<s:form name="detailForm" action="surveyReport_detail">
		<s:hidden name="id" />
		<s:hidden id="lang" name="lang" />
		
	</s:form>

</body>
