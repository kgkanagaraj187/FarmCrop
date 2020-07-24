<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">

$(document).ready(function(){

	jQuery("#detail").jqGrid(
			{
			url:'forecastAdvisor_data.action',
			pager: '#pagerForDetail',
			datatype: "json",
			 styleUI : 'Bootstrap',
			colNames:[
			          <s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
					</s:if>
					 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
					</s:if>
		  		   	  '<s:text name="Crop" />',
		  		      '<s:text name="Description" /> ',
		  		    '<s:text name="Minimum" /> ',
		  		  '<s:text name="Maximum" /> ',
		  		  '<s:text name="Minimum" /> ',
		  		  '<s:text name="Maximum" /> ',
		  		'<s:text name="Minimum" /> ',
		  		  '<s:text name="Maximum" /> ',
		  		'<s:text name="Minimum" /> ',
		  		  '<s:text name="Maximum" /> ',
],
		   colModel : [
					 	 <s:if test='branchId==null'>
			   			{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
			   			value: '<s:property value="parentBranchFilterText"/>',
			   			dataEvents: [ 
			   			          {
			   			            type: "change",
			   			            fn: function () {
			   			            	console.log($(this).val());
			   			             	getSubBranchValues($(this).val())
			   			            }
			   			        }
			   			    ]
			   			
			   			}},	   				   		
			   		</s:if>
			   		 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
			   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
			   		</s:if>
		      	      {name:'crop',index:'crop',sortable:false, width:125},
		      	      {name:'description',index:'description',sortable:false},
		      	    {name:'miniRain',index:'miniRain', sortable:false},
		      	  {name:'maxiRain',index:'maxiRain', sortable:false},
		      	{name:'miniHumi',index:'miniHumi', sortable:false},
		      	  {name:'maxiHumi',index:'maxiHumi', sortable:false},
		      	{name:'miniWind',index:'miniWind', sortable:false},
		      	  {name:'maxiWind',index:'maxiWind', sortable:false},
		      	{name:'miniTemp',index:'miniTemp', sortable:false},
		      	  {name:'maxiTemp',index:'maxiTemp', sortable:false},
		      	 ],			
		    height: 255, 
			width: $("#baseDiv").width(),
			scrollOffset: 0,
			rowNum:10,
			rowList : [10,25,50],
			sortname: 'id',
			sortorder: 'desc',
			viewrecords: true,
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
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
	jQuery("#detail").jqGrid('setGroupHeaders', {
			  useColSpanStyle: true, 
			  groupHeaders:[
				{startColumnName: 'miniRain', numberOfColumns: 2, titleText: '<s:text name="Rainfall"/>'},
				{startColumnName: 'miniHumi', numberOfColumns: 2, titleText: '<s:text name="Humitity"/>'},
				{startColumnName: 'miniWind', numberOfColumns: 2, titleText: '<s:text name="windSpeed"/>'},
				{startColumnName: 'miniTemp', numberOfColumns: 2, titleText: '<s:text name="Temperature"/>'},
				
			  ]
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
});
var productsInfoArray = new Array();

function addRow(){
	//var editIndex = getEditIndex();
	var hit=true;
	var selectedCrop=$("#selectedCrop option:selected").text();
	var selectedforecast=$("#selectedforecast option:selected").text();
	var forecast=$("#selectedforecast").val();
	var minimum=$("#minimumval").val();
	var maximum=$("#maximumval").val();
	var productExists=false;	
	var productArray=null;
		productArray = {
				selectedCrop :selectedCrop,
				 selectedforecast:selectedforecast,
				 forecast:forecast,
				 minimum:minimum,
				 maximum:maximum
	     };
		alert(productArray);
		if(!productExists){
			 productsInfoArray[productsInfoArray.length] = productArray;
		}
	 reloadTable();
	 jQuery('#selectedCrop').prop("disabled",true);
		jQuery('#selectedforecast').prop("disabled",false);
		var idsArray=['selectedforecast'];
		resetSelect2(idsArray);
		document.getElementById('minimumval').innerHTML = "";
		document.getElementById('minimumval').value="";
		document.getElementById('maximumval').innerHTML = "";
		document.getElementById('maximumval').value="";
}
function getEditIndex(){
	
	for(var i=0; i< productsInfoArray.length; i++){
		if(productsInfoArray[i].isEdit)
			return i;
	}
	return -1;
}
function enableButton1(){

	jQuery("#addButton").prop('enabled',true);

}
function reloadTable(){
	var tbodyRow = "";
	jQuery('#productInfoTbl1 > tbody').html('');
	var rowCount=0;

	
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		rowCount++;
		alert(rowCount);	
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].selectedCrop+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].selectedforecast+'</td>'+
				
					'<td style="text-align:center;">'+productsInfoArray[cnt].maximum+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].minimum+'</td>'+
					
					'</tr>';	
	
	  }	
	 if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="11" style="text-align:center"><s:text name="noRecordFo"/></td>'+
					'</tr>';
	} 
	
		
	jQuery('#productInfoTbl1 > tbody').html(tbodyRow);
	
											

}



function disablePopupAlert(){

	document.redirectform.submit();
}
</script>
<%-- 	<div class="appContentWrapper marginBottom">
		
				<div class="formContainerWrapper">
					<h2>
						<s:property value="%{getLocaleProperty('info.forecastAdvisor')}" />
					</h2>
					<div class="flexiWrapper filterControls">
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('crop')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
								<s:select name="selectedCrop" theme="simple" listKey="id"
									listValue="name" list="procurementProductList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" id="selectedCrop" />
							</div>
						</div>
						
						
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('forecastadvisor')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
								<s:select name="selectedforecast" theme="simple" listKey="id"
									listValue="name" list="forecastDataList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" id="selectedforecast" />
							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('maximum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="maximum" theme="simple" maxlength="45"
									id="maximumval" />

							</div>
						</div>
						
							<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('minimum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="minimum" theme="simple" maxlength="45"
									id="minimumval" />

							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts " data-toggle="modal"
								onclick="addRow()">
								<font color="#FFFFFF"> <b><s:text name="add.button" /></b>
								</font>
							</button>

						</div>
						
						</div>
					</div>

	
	<table id="productInfoTbl1"
							class="table table-bordered aspect-detail">
							<thead>
								<tr class="odd">
								<th><s:text name="s.no" /></th>
									<th><s:text name="crop" /></th>
									<th><s:text name="forecastadvisor " /></th>
									<th><s:text name="maximum" /></th>
									<th><s:text name="minimum" /></th>
									
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="12" style="text-align: center;"><s:text
											name="noRecordFounds" /></td>
								</tr>
							</tbody>
						</table>
						<div class="flexi flexi10">
						<div class="flexItem">
							<label for="txt"><s:property
									value="%{getLocaleProperty('Description')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="descripe" theme="simple" maxlength="45"
									id="descripeval" />

							</div>
						</div>
							<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts " data-toggle="modal"
								onclick="saveForeCastAdvisor()">
								<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
								</font>
							</button>

						</div>
						</div>
						</div> --%>
						
						<div class="appContentWrapper marginBottom">
	<sec:authorize ifAllGranted="admin.forecastAdvisor.create">
		<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
	</sec:authorize>
<div style="width: 99%;" id="baseDiv">
<table id="detail"></table>
<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="createform" action="forecastAdvisor_create">
</s:form>
<s:form name="updateform" action="forecastAdvisor_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
   <button type="button" data-toggle="modal" data-target="#myModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					onclick="disablePopupAlert()">&times;</button>
				<h4 class="modal-title">
					<s:text name="foreCastAdvisor.success" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="divMsg" align="center"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="disablePopupAlert()">
					<s:text name="closeBtn" />
				</button>
			</div>
		</div>

	</div>
</div>
</body>
</html>