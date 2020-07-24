<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
<!--<link href="css/main_table.css" rel="stylesheet" type="text/css" />-->
<s:head />
<style type="text/css">
.alignTopLeft {
	float: left;
	width: 6em;
}

.view {
	display: table-cell;
	background-color: #d2dae3;
}

.alignTopLeft {
	float: left;
	width: 6em;
}

select {
	width: 120px !important;
}

input[type="text"] {
	width: 120px;
}

.datepicker_clear {
	display: none;
}

.column-1 h3, .column-2 h3, .column-3 h3 {
	/*background: none repeat scroll 0 0 #6F9505 !important;*/
	border: 1px solid #fff !important;
	color: #fff !important;
	padding: 5px;
	margin: 0;
	text-align: left;
}

.alignCenter {
	text-align: center !important;
}

.alignLeft {
	text-align: left !important;
}

.alignRight {
	text-align: right !important;
}

.generalTable th {
	marign-top: 10px;
}

.column-2, .column-3 {
	margin-top: 25px;
}

.borNone {
	border-right: none !important;
	border-left: none !important;
	border-top: none !important;
	border-bottom: none !important;
}

.div.error {
	color: #FD0000;
	font-size: 12px;
	/*padding: 5px 10px 5px 0;*/
	text-align: left;
	width: auto;
	margin-bottom: 0px !important;
}
</style>

</head>
<body>

	<s:form id="mtnrForm" action="distributionStockReception_create"
		method="post" cssClass="fillform">
		<font color="red"> <s:actionerror /></font>
		<div>
			<div class="appContentWrapper marginBottom ">
				<div class="formContainerWrapper">
					<div class="row">
						<div class="container-fluid">
							<div class="notificationBar">
								<div class="error">
									<p class="notification">
									<div id="validateError" style="text-align: center;"></div>
									<div id="validatePriceError" style="text-align: center;"></div>
									</p>
								</div>
							</div>
						</div>

					</div>
					<h2>
						<s:text name="info.general" />
					</h2>
					<div class="flexiWrapper filterControls">
								<div class="flexi flexi12">
							<label for="txt"><s:text name="season" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="season" list="seasonList" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" id="season"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
						
						
						<div class="flexi flexi12 receiverWarhouse" style="width: 130px">
							<label for="txt"><s:text
									name="%{getLocaleProperty('receiverWarhouse')}" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="receiverWarhouse" list="receiverWarehouseList"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" id="receiverWarhouse"
									onchange="populateReceiptNumber();"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
						<div class="flexi flexi12">
							<label for="txt"><s:text name="receiptNumber" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="receiptNo" list="{}" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" id="receiptNo"
									onchange="createGrid(this);loadTruck(this);"
									cssClass="form-control input-sm select2" />
							</div>
						</div>


						<div class="flexi flexi12" style="width: 130px">
							<label for="txt"><s:text name="distributionDate" /></label>
							<div class="form-element">
								<s:textfield name="startDate" id="startDate" readonly="true"
									theme="simple"
									data-date-format="%{getGeneralDateFormat().toLowerCase()}"
									data-date-viewmode="years"
									cssClass="date-picker form-control input-sm" size="20" />
							</div>
						</div>

						<div class="flexi flexi12 truckId" style="width: 130px">
							<label for="txt"><s:text name="truckId" /></label>
							<div class="form-element">
								<s:textfield id="truckId" name="truckId"
									cssClass="form-control input-sm" maxlength="20" />
							</div>
						</div>
						<div class="flexi flexi12 driverName" style="width: 130px">
							<label for="txt"><s:text name="driverName" /></label>
							<div class="form-element">
								<s:textfield id="driverName" name="driverName"
									cssClass="form-control input-sm" maxlength="25" />
							</div>
						</div>

					</div>
				</div>
			</div>

			<div class="service-content-wrapper">
				<div class="service-content-section">

					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<div class="formContainerWrapper prodRecTable">
								<h2>
									<s:text name="distributionStock.reception" />
								</h2>
								<div class="flexiWrapper filterControls">
									<div id="prodRecpInfo"
										style="text-align: center; height: 20px; width: 100%;">
									</div>

									<div style="width: 98%;" id="baseDiv">
										<table id="detail" style="width: auto !Important;"></table>
									</div>

								</div>
							</div>
							<div class="yui-skin-sam" id="savebutton">
								<sec:authorize
									ifAllGranted="service.distributionStockReception.create">
									<span id="save" class=""><span class="first-child">
											<button id="saveBtn" type="button" onclick="event.preventDefault();submitProdRecp()"
												class="save-btn btn btn-success">
												<font color="#FFFFFF"> <b><s:text
															name="save.button" /></b>
												</font>
											</button>
									</span></span>

								</sec:authorize>
								<span class="" style="cursor: pointer;"><span
									class="first-child"><a id="cancelBtnId"
										onclick="disablePopupAlert();" class="cancel-btn btn btn-sts"> <font
											color="#FFFFFF"> <s:text name="cancel.button" />
										</font>
									</a></span> </span>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
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
						<s:text name="distributionStockReception" />
					</h4>
				</div>
				<div class="modal-body">
					<div id="divMsg" align="center"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="disablePopupAlert()">Close</button>
				</div>
			</div>

		</div>
	</div>

	<s:form action="mtnr_populatePrintHTML" id="receiptForm" method="POST"
		target="printWindow">
		<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
	</s:form>

	<script type="text/javascript">
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
var globalGradeIdArray;
var gradeInputStr='';
jQuery(document).ready(function(){
	$('#startDate').datepicker('setDate', new Date());
	 $("#saveBtn").on('click', function (event) {  
         event.preventDefault();
         var el = $(this);
         el.prop('disabled', true);
         setTimeout(function(){el.prop('disabled', false); }, 1000);
   });
});
function populateReceiptNumber(){
	   $('#truckId').val('');
	   $('#driverName').val('');
	   var rWare=$("#receiverWarhouse option:selected").val();
	   createGrid(rWare);
	$.ajax({
		 type: "POST",
        async: false,
        url: "distributionStockReception_populateReceiptNo",
        data: {receiverWarehouse: rWare},
        success: function(result) {
       	 if(result.length==0){
       	 }else{
       		 insertOptions("receiptNo",JSON.parse(result));
       		 
       	 }
        }
	}); 
}
function createGrid(val){
	jQuery(".prodRecTable").show();
	try {
  		jQuery("#detail").empty();
	  jQuery("#detail").jqGrid('GridUnload');          
    }catch(e){}
    globalGradeIdArray=new Array();
	if(val!=""){
		jQuery("#prodRecpInfo").hide();
		jQuery.post("distributionStockReception_populateStock",{receiptNo:$("#receiptNo").val(),receiverWarehouse:$('#receiverWarhouse').val()},function(result){
			var detJSON=$.parseJSON(result);
			for(var count=0;count<detJSON.products.length;count++){
					var gridColumnNames = new Array();
					var gridColumnModels = new Array();
					var footerRow = new Object();
					var rowData = new Array();
					var prods = detJSON.products[count].distStockReceiption;
					for(var i=0;i<prods.length;i++){
						globalGradeIdArray[globalGradeIdArray.length] = prods[i].id;
						var row = new Object();
						row['categoryName'] =  prods[i].categoryName+'<center><div id="categoryIdHeader'+(prods[i].id)+'" style="display: none">'+prods[i].categoryId+'</div><div id="categoryNameHeader'+ prods[i].id+'" style="display: none">'+ prods[i].categoryName+'</div>';
						row['ProdName'] =  prods[i].productName+'<center><div id="prodIdHeader'+(prods[i].id)+'" style="display: none">'+prods[i].productId+'</div><div id="prodNameHeader'+ prods[i].id+'" style="display: none">'+ prods[i].productName+'</div>';
						row['unit'] =  prods[i].unit+'<center><div id="unitHeader'+(prods[i].id)+'" style="display: none">'+prods[i].id+'</div><div id="unitNameHeader'+ prods[i].id+'" style="display: none">'+ prods[i].unit+'</div>';
						row['qty'] =prods[i].qty+'<center><div id="qtyVal'+(prods[i].id)+'" style="display: none">'+prods[i].qty+'</div><div id="qtyVal1'+ prods[i].id+'" style="display: none">'+ prods[i].qty+'</div>';
						row['receptQty'] = '<input id="recQty'+(prods[i].id)+'" type="text" onkeyup="calculateTotalGrossWeight();" onkeypress="return isDecimal(event)" style="text-align:right;padding-right:1px;" value="0.000" maxlength="10" name="" class="col-sm-3 form-control">'
						rowData[rowData.length] = row;
					}
					globalGradeIdArray[globalGradeIdArray.length] = '';
					gridColumnNames  = ['<s:text name="category"/>','<s:text name="product"/>','<s:text name="unit"/>','<s:text name="transQty" />','<s:text name="receiveQty" /><sup style="color: red;">*</sup>']
					    				gridColumnModels = [{name:'categoryName',index:'categoryName',sortable:false, width: 100,resizable: false},
					    				                    {name:'ProdName',index:'ProdName',sortable:false, width: 100,resizable: false},
					    				    				{name:'unit',index:'unit',sortable:false, width: 100,resizable: false},
					    				    				{name:'qty',index:'qty',sortable:false, width: 100,resizable: false},
					    				    				{name:'receptQty',index:'receptQty',sortable:false, width: 150, align:'center',resizable: false}]
					footerRow = {qty:'<s:text name="total"/>',receptQty:'<center><div id="stockReceive">0.000</div></center>'};
					jQuery("#detail").jqGrid(
					{		   		
					   	colNames:gridColumnNames,
					   	colModel:gridColumnModels,
					   	width: $("#baseDiv").width(),
					    footerrow: true,
					   	height:'auto'
				    });
					for(var i=0;i<rowData.length;i++)
						jQuery("#detail").jqGrid('addRowData',(i+1),rowData[i]);
			    	jQuery("#detail").jqGrid('footerData' , 'set' , footerRow);
			    	/* calculateTotalQuantity();
			    	calculateTotalGrossWeight(); */
					break;
			}
		});
	}else{
		jQuery("#prodRecpInfo").show();
	}	
}

function loadTruck(){
	var value=$("#receiptNo").val();
	 $.post("distributionStockReception_populateTruck",{receiptNo:value},function(result){
		 var strData=JSON.stringify(result);
			var data1 =jQuery.parseJSON(strData);
			$("#truckId").val(data1.truck);
			$("#driverName").val(data1.driver);
			
	}); 		
}
function calculateTotalGrossWeight(){
	var weight=0;
    for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
    	var st=$('#qtyVal'+globalGradeIdArray[cnt]).text();
    	var wt=jQuery("#recQty"+globalGradeIdArray[cnt]).val();
    	
        if(!isNaN(wt) && wt.trim()!=''){
        	if(parseFloat(st)<parseFloat(wt)){
        		jQuery("#stockReceive").text("0.000");
        		jQuery("#recQty"+globalGradeIdArray[cnt]).val('0.000');
        	}else{
        	weight+=parseFloat(wt);
        	}
        }
    }
    jQuery("#stockReceive").text(weight.toFixed(3));
}
function submitProdRecp(){
	var receiverWarhouse=$('#receiverWarhouse').val();
	var season=$('#season').val();
    var recNum=$('#receiptNo').val();
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverName").val();
	var date = jQuery.trim(jQuery("#startDate").val());

	var hit=validateData();
	if(hit){
		$("#saveBtn").prop("disabled",true);
		jQuery.post("distributionStockReception_populateReception.action",{season:season,receiverWarehouse:receiverWarhouse,startDate:date,truckId:truckId,driverName:driverId,distDetailsStr:gradeInputStr,receiptNo:recNum},function(data){			
			if(data == null || data == ""){		
			$("#saveBtn").prop("disabled",false);
			jQuery("#stockReceive").html("0");
			jQuery("#recQty").html("0.00");
			//jQuery(".prodRecTable").hide();
			}
		else{ 	 			
			document.getElementById("divMsg").innerHTML=data;
			document.getElementById("enableModal").click();
			$("#saveBtn").prop("disabled",false);
			jQuery("#stockReceive").html("0");
			jQuery("#recQty").html("0.00");
			
		}
		});
				
	  }
	
 }
function validateData(){
	jQuery("#validateError").text('');
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverName").val();
	var receiverWarhouse=$('#receiverWarhouse').val();
	var recNo = $('#receiptNo').val();
	var season = $('#season').val();
    if(season=="-1" || season==""){
    	jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.season")}' />');
		return false;
    }
    if(receiverWarhouse=="-1" || receiverWarhouse==""){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.receiverWarhouse")}' />');
		return false;
	}else if(recNo=="-1"|| recNo==""){
		jQuery("#validateError").text('<s:text name="empty.RecpNo"/>');
		return false;
	}else if(globalGradeIdArray.length==0){
 		 jQuery("#validateError").text('<s:text name="grades.not.exists"/>');
         return false;
 	 }
	    gradeInputStr='';
	    var flag=0;
	   
		for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
			var catId=jQuery("#categoryIdHeader"+globalGradeIdArray[cnt]).html();
			var prodId=jQuery("#prodIdHeader"+globalGradeIdArray[cnt]).html();
			var unit=jQuery("#unitHeader"+globalGradeIdArray[cnt]).html();
		   var givenQty=jQuery("#qtyVal"+globalGradeIdArray[cnt]).val().trim();
		   var receiveQty=jQuery("#recQty"+globalGradeIdArray[cnt]).val().trim();
			
		   if(givenQty!='' && (isNaN(givenQty) || isNaN(parseFloat(givenQty)))){
				jQuery("#validateError").text('<s:text name="invalidQtyFor"/>');
				return false;
			}
			if(receiveQty !='' && (isNaN(receiveQty) || isNaN(parseFloat(receiveQty)))){
				jQuery("#validateError").text('<s:text name="invalidReceiveQty"/>');
				return false;
			}
			if(parseFloat(receiveQty)>parseFloat(givenQty)){
				jQuery("#validateError").text('<s:text name="greaterReceiveWeight"/>');
				return false;
			}
			if(parseFloat(givenQty)<0){
				jQuery("#validateError").text('<s:text name="noStockFound"/>');
				return false;
			}
			if( receiveQty.trim()==''){
				receiveQty=0;
			}
			if( receiveQty!=''){
				gradeInputStr+=prodId.trim()+'##'+parseFloat(receiveQty.trim())+'##'+parseInt(unit.trim())+'||';
				flag=1;
			}
			/* if( receiveQty=='0.000'){
				flag=0;
			}  */
					
		}
	//	gradeInputStr: 26##0##78||
		var mainStr=gradeInputStr.split("||");
			if(mainStr.length-1==1){
				var aStr=mainStr[0].split("##");
				if(aStr[1].trim()=='' || isNaN(parseFloat(aStr[1].trim())) || parseFloat(aStr[1].trim())<=0){
					jQuery("#validateError").text('<s:text name="noStockFound"/>');
					return false;
				}
			}
		if(flag==0){
			jQuery("#validateError").text('<s:text name="noDistributionStockReceptionfound"/>');
			return false;
		}
		return true; 
}
function isValidAlphaNumericWithSpaceValue(val){
	 var numberReg = /^[A-Za-z0-9 ]+$/;
	 var result = true;
	 var regExVal = val.match(numberReg);
	 if(regExVal=='' || regExVal==null || regExVal== undefined ){
	  result = false;
	 }
	 return result;
}
function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}
function disablePopupAlert(){
	document.redirectform.action="distributionStockReception_create.action";
	document.redirectform.submit();
}

</script>
<s:form name="redirectform" action="distributionStockReception_create.action"
	method="POST">
</s:form>
</body>