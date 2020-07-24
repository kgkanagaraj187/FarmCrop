<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
</head>
<body>
	<script type="text/javascript">

	var villageCode='';
	var status=-1;	

	jQuery(document).ready(function(){
		jQuery(".well").hide();
		document.getElementById('status').selectedIndex=0;
		//document.getElementById('lastName').selectedIndex=0;	
		document.getElementById('villageName').value='';			
		onFilterData();
		$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.updateView();
	     $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		jQuery("#detail").jqGrid(
				{					
					url:'offlineFarmerEnrollmentReport_detail.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	styleUI : 'Bootstrap',
				   	mtype: 'POST',
				   	postData:{				 
						  "filter.village.name" : function(){
							  return document.getElementById("villageName").value;
						  },
						  "filter.statusCode" : function(){
							  return status;
						  },
						 /*  "filter.lastName":function(){
							  return document.getElementById("lastName").value;
						  },  */
						 
						  <s:if test='branchId==null'>
						  "branchIdParam" : function(){
	 						  return document.getElementById("branchIdParam").value;
	 					  }
						  </s:if>
					},	
				   	colNames:[
						      <s:if test='branchId==null'>
						 	  '<s:text name="app.branch"/>',
							  </s:if>
				  		   	  /* '<s:text name="farmerId"/>', //Note: Removed on bug id 664.*/
				  		   	  <s:if test="farmerCodeEnabled==1">
				  		   	  '<s:property value="%{getLocaleProperty('farmerCode')}"/>',
				  		   	  </s:if>
				  		    '<s:property value="%{getLocaleProperty('farmer')}"/>',
				  		   	 // '<s:property value="%{getLocaleProperty('fatherName')}"/>',
				  		   	  '<s:text name="villageName"/>',
				  		      '<s:text name="statusMsg"/>'
		 	      	 ],
				   	colModel:[
						<s:if test='branchId==null'>
						{name:'branchId',index:'branchId',sortable: false},	   				   		
						</s:if>		      
						/* {name:'farmerId',index:'farmerId',width:250,sortable:false}, //Note: removed on bug id 664.*/
						<s:if test="farmerCodeEnabled==1">
						{name:'farmerCode',index:'farmerCode',sortable:false},
						</s:if>
				   		{name:'firstName',index:'firstName',sortable:false},
				   		//{name:'lastName',index:'lastName',sortable:false},
				   		{name:'villageName',index:'villageName',sortable:false},
				   		{name:'statusMsg',index:'statusMsg',sortable:false}
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   rowNum:10,
				   rowList : [10,25,50],
				   scrollOffset: 19,	    
				   sortname:'id',
				   shrinkToFit:true,	
				   sortorder: "desc",
				   viewrecords: true,  	
				   subGrid: false,
				   subGridOptions: {
					"plusicon" : "glyphicon-plus-sign",
					"minusicon" : "glyphicon-minus-sign",
					"openicon" : "glyphicon-hand-right",
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
		function onFilterData(){
			callAjaxMethod("offlineFarmerEnrollmentReport_populateVillageList.action","villageName");
		}
		function callAjaxMethod(url,name){
			var cat = $.ajax({
				url: url,
				async: true, 
				type: 'post',
				success: function(result) {
					insertOptions(name,JSON.parse(result));
				}        	

			})
			
		}
		
		function reloadGrid(){
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");

			var value1= d2[0];
			
			var value2= d2[1];
			
		document.getElementById("hiddenStartDate").value=value1;

		document.getElementById("hiddenEndDate").value=value2;

			var startDate=new Date(document.getElementById("hiddenStartDate").value);
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}
			
				villageCode = document.getElementById("villageName").value;
				status = document.getElementById("status").value;				
				jQuery("#detail").jqGrid('setGridParam',{url:"offlineFarmerEnrollmentReport_detail.action",page:1,mtype: 'POST',postData:{	
					"startDate" : function(){
			              return document.getElementById("hiddenStartDate").value;           
		             },		
		            "endDate" : function(){
		                return document.getElementById("hiddenEndDate").value;           
		             }
		             }}).trigger('reloadGrid');
				
		}

		function clear(){
			document.getElementById("status").value = "-1";
			document.form.submit();
		}

	});

	function exportXLS(){
		if(isDataAvailable("#detail")){
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'offlineFarmerEnrollmentReport_populateXLS.action?'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
	
</script>

	<s:form name="form" action="offlineFarmerEnrollmentReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				
				<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('startingDate')}" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm " />
						</div>
					</div>
								
				<div class="filterEle">
					<label for="status"><s:text name="status" /></label>
					<div class="form-element">
						<s:select name="filter.statusCode" id="status" list="statusesList"
					listKey="key" listValue="value" headerKey=" "
					headerValue="%{getText('txt.select')}"
					cssClass="form-control select2" />
					</div>
				</div>
				<%-- <div class="filterEle">
					<label for=""><s:property value="%{getLocaleProperty('fatherName')}" /></label>
					<div class="form-element">
						<s:select name="lastNames" id="lastName" list="lastNameList"
					listKey="key" listValue="value" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div> --%>
				<div class="filterEle">
					<label for="village"><s:text name="villageName" /></label>
					<div class="form-element">
						<s:select name="filter.villageName" id="villageName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2"/>
					</div>
				</div>
				<s:if test='branchId==null'>
				<div class="filterEle">
					<label for=""><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParam" id="branchIdParam"
						list="branchesMap" headerKey=""
						headerValue="%{getText('txt.select')}"
						cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				<div class="filterEle" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm" aria-hidden="true"
						 id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
					</div>
			</section>
		</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	</s:form>

	<div class="appContentWrapper marginBottom">
		<%--<div class="flex-layout reportData">
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container">
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Farmers&nbsp;<i
							class="fa fa-user"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Cultivation Cost&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Others&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Total Cost&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
				</div>
			</div>

			<%-- <div class="flexItem text-right flex-right">
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
						<li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li>
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div> 
		</div>--%>

<div class="flexItem text-right flex-right">
       <s:if test="currentTenantId!='chetna'">
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
				</s:if>
			</div>
			
		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>    
</body>
