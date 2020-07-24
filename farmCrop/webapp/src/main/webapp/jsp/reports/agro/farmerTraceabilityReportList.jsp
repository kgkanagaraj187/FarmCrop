<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
<style>
.trace th {
    background: #A8E3D6;
    border: solid 1px #fff;
        border-left-color: rgb(255, 255, 255);
        border-left-style: solid;
        border-left-width: 1px;
}
</style>
</head>
<body>
<script type="text/javascript">
jQuery(document).ready(function(){
	onFilterData();
	loadGrid();
});
function onFilterData() {
	callAjaxMethod("farmerTraceabiltiyReport_populateLotNoList.action","lotNo");
	callAjaxMethod("farmerTraceabiltiyReport_populateStateList.action","state");
	callAjaxMethod("farmerTraceabiltiyReport_populateICSList.action","icss");
	callAjaxMethod("farmerTraceabiltiyReport_populateTalukList.action","mandal");
	callAjaxMethod("farmerTraceabiltiyReport_populateVillageList.action","village");
	callAjaxMethod("farmerTraceabiltiyReport_populateSHGList.action","shg");
}
function callAjaxMethod(url, name) {
	var cat = $.ajax({
					url : url,
					async : true,
					type : 'post',
					success : function(result) {
						insertOptions(name, JSON.parse(result));
					}
			});
}
function loadGrid(){
	var gridColumnNames = new Array();
	var gridColumnModels = new Array();
	var colNames='<s:property value="gridColNames"/>';
	jQuery(colNames.split("#")).each(function(k,val){
			if(!isEmpty(val)){
				//var cols=val.split("#");
				//console.log(val)
				gridColumnNames.push(val);
				gridColumnModels.push({name: val,sortable: false});
			}
	});
	jQuery("#detail").jqGrid(
	{
		url:'farmerTraceabiltiyReport_data.action',
	 	pager: '#pagerForDetail',
	   	datatype: "json",	
	    styleUI : 'Bootstrap',
	   	mtype: 'POST',
		postData:{
			
		},
	        	colNames:gridColumnNames,
			    colModel:gridColumnModels,
				height: 301, 
			   	//width: $("#baseDiv").width(),
			   	autowidth:true,
			   	scrollOffset: 5,     
			   	sortname:'id',
			   	sortorder: "desc",
			   	rowNum:10,
			    shrinkToFit:false,
			   	rowList : [10,25,50],
			   	viewrecords: true,  	
			   	subGrid: false,
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
	});
	colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column trace').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;
        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        }
    });	

    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
 			jQuery("#detail").jqGrid('setGroupHeaders', {
			  useColSpanStyle: true, 
			  groupHeaders:[
							{startColumnName: 'Young_Age', numberOfColumns: 3, titleText: 'Age'},
							{startColumnName: 'Married', numberOfColumns: 4, titleText: 'Marital Status'},
							{startColumnName: 'General', numberOfColumns: 7, titleText: 'Government Caste Catagorization'},
							{startColumnName: 'FEMALE', numberOfColumns: 2, titleText: 'Gender'},
							{startColumnName: 'Kacha', numberOfColumns: 4, titleText: 'Housing Type'},
							{startColumnName: 'Borewell', numberOfColumns: 6, titleText: 'Drinking Water'},
							{startColumnName: 'Electrified_House', numberOfColumns: 2, titleText: 'Electricity'},
							{startColumnName: 'Toilet_Avilable', numberOfColumns: 2, titleText: 'Toilet'},
							{startColumnName: 'School', numberOfColumns: 6, titleText: 'Education'},
							{startColumnName: 'Gov_Schemes', numberOfColumns: 2, titleText: 'Gov Schemes'},
							{startColumnName: 'Firewood', numberOfColumns: 7, titleText: 'Cooking Fuel'},
							{startColumnName: 'Cellphone', numberOfColumns: 2, titleText: 'Cell Phone '},
							{startColumnName: 'Car', numberOfColumns: 5, titleText: 'Vehicle'} 
			  ]
 			});
	search();
}
function search(){
	var postData = {};
	data = new Array();
	$("#form select").each(function(){
		var value = $(this).val();
		var name=this.name;
		if(value!=null)
			postData[name] = " = '"+value+"'";
	});
	var prNo=$('#prNo').val();
	if(!isEmpty(prNo) && prNo!='' && prNo!=undefined){
		postData['flp.PR_NO'] = "'"+prNo+"'";
	}
	var json = JSON.stringify(postData);
	var dataa={
			filterList:json
	}; 
	jQuery("#detail").jqGrid('setGridParam',{url:'farmerTraceabiltiyReport_data.action',page:1,postData:dataa}).trigger('reloadGrid');		
}
function reset(){
	
}
function exportXLS(){
	if(isDataAvailable("#detail")){
		var postData = {};
		var filterData = {};
		$("#form select").each(function(){
			var value = $(this).val();
			var name=this.name;
			var id=this.id;
			var txt=$(this).find("option:selected").text();
			if(!isEmpty(value)){
				postData[name] = " = '"+value+"'";
				filterData[id]= txt;
			}
		});
		
		var json = JSON.stringify(postData);
		var filterJson=JSON.stringify(filterData);
		
		var dataa={
				filterList:json				
		};
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'farmerTraceabiltiyReport_populateXLS.action?filterDataStr='+filterJson,postData:dataa});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}
</script>
<s:form id="form">
	<div class="appContentWrapper marginBottom">
		<section class='reportWrap row'>
			<div class="gridly">
					 <div class="filterEle">
					<label for="txt"><s:text name="lotNo" /></label>
					<div class="form-element">
						<s:select name="flp.LOT_NO" id="lotNo"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" multiple="true"/>
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="prNo" /></label>
					<div class="form-element">
						<s:textfield id="prNo" name="flp.PR_NO" theme="simple" maxlength="12"
								cssClass="form-control " onkeypress="return isNumber(event)" />
					</div>
				</div> 
				<div class="filterEle">
					<label for="txt"><s:text name="state" /></label>
					<div class="form-element">
						<s:select name="s.ID" id="state"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="icss" /></label>
					<div class="form-element">
						<s:select name="f.ICS_NAME" id="icss"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="Taluk" /></label>
					<div class="form-element">
						<s:select name="f.CITY_ID" id="mandal"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="village" /></label>
					<div class="form-element">
						<s:select name="f.VILLAGE_ID" id="village"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="SHG" /></label>
					<div class="form-element">
						<s:select name="f.SAMITHI_ID" id="shg"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
		
				<div class='filterEle' style="margin-top: 1.5%;margin-right: 0%;">
						<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true" onclick="search()">
						<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="reset" onclick="reset()">
						<b class="fa fa-close"></b>
						</button>
				</div>
			</div>
			</section>
		</div>
	</s:form>
	<!-- <div class="formContainerWrapper">
	<h2>Age</h2>
	</div> -->
	<div class="appContentWrapper marginBottom">
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
						<%-- <li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li> --%>
						
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>
		</div>


		<div style="width: 99%;" id="baseDiv">
			<table id="detail" class="ttrace"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
</body>