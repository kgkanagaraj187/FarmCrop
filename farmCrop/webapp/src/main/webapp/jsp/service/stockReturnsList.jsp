<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
<!--<link href="css/main_table.css" rel="stylesheet" type="text/css" />-->
<style type="text/css">


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

.column-1 h3,.column-2 h3,.column-3 h3 {
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

.column-2,.column-3 {
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
	margin-bottom:0px !important;
}


</style>
</head>

<script>

jQuery(document).ready(function(){	
	 document.getElementById("startDate").value='<s:property value="currentDate" />';

		<s:if test='stockDescription!=null'>
		jQuery('<tr><td><s:property value="stockDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first")));
		jQuery("#restartAlert").css('height','180px');
	</s:if>

 hideDivBasedOnCashType(document.getElementById('cashId'));
 resetPrefixAndSuffix();
 showUnsoldType(document.getElementById('type1'));
	
});

function resetPrefixAndSuffix(){
	 jQuery("#costPriceRupees").val("0");
	 jQuery("#costPricePaise").val("00");	 
}

function cancel() {
		document.form.action = "stockReturns_list.action";
		document.listForm.submit();
}

function populateProducts()
{
		createGrid();
}


function createGrid()
{
  	try {
	  jQuery("#detail").jqGrid('GridUnload');          
    }
    catch(e){}
	globalGradeIdArray=new Array();
    
	var selType = $('input:radio[name="type1"]:checked').val();
	

    var vendorId=document.getElementById("vendor").value;
	var orderNo= jQuery("#orderNo option:selected").text();
	var warehouse=document.getElementById("warehouse").value

	var vendorIdSold=document.getElementById("vendorSold").value;
	var orderNoSold= jQuery("#orderNoSold option:selected").val();
	var warehouseSold=document.getElementById("warehouseSold").value
	if(selType=='damaged'){
		if(vendorId!="" && orderNo!="")
		{
			jQuery("#stockTransferInfo").hide();
			var agentIdx='<%=request.getParameter("agentId")%>';
			jQuery.post("stockReturns_populateProducts",{selectedVendor:vendorId,selectedOrderNo:orderNo,selectedWarehouse:warehouse},function(result){
			procurementProductJSON=$.parseJSON(result);
			globalGradeIdArray1=new Array();
				
			for(var count=0;count<procurementProductJSON.products.length;count++)
				{
			
					var gridColumnNames = new Array();
					var gridColumnModels = new Array();
					var footerRow = new Object();
					var rowData = new Array();
					
					var procurementGrades = procurementProductJSON.products[count].procurementGrades;
					for(var i=0;i<procurementGrades.length;i++){

						globalGradeIdArray[globalGradeIdArray.length] = procurementGrades[i].id;
						//globalGradeIdArray1=document.getElementsByName("returnDamagedStock[]");  
						var row = new Object();
						row['sNo']='<div>'+(i+1)+'</div>';
						row['subCategory']='<div>'+procurementGrades[i].subCategory+'</div>';
						row['product'] ='<div>'+procurementGrades[i].product+'</div>'+
						'<input id="productIds" type="hidden"  value='+procurementGrades[i].productId+' maxlength="10" name="productIds"/>';
						row['costPrice'] ='<input id="costPrice" type="hidden"  value='+procurementGrades[i].costPrice+' maxlength="10" name="costPrice[]"/>'
						+'<div>'+procurementGrades[i].costPrice+'</div>';
						row['currentDamagedStock'] ='<input id="currentQuantity" type="hidden"  value='+procurementGrades[i].currentDamagedStock+' maxlength="10" name="currentQuantity[]">'
						+'<div>'+procurementGrades[i].currentDamagedStock+'</div>';	
						row['returnDamagedStock'] = '<input id="returnDamagedStock" type="text" onkeyup="calculateTotalQuantity('+procurementGrades[i].returnDamagedStock+','+procurementGrades[i].productId+')" onkeypress="return isNumber(event)" style="text-align:right;padding-right:1px; width:60px!important;"  value='+procurementGrades[i].returnDamagedStock+' maxlength="10" name="returnDamagedStock">'
						  +'<sup style="color: red;">*</sup>';
						row['amount']='<div id="totalAmount'+(i)+'"></div>';
						rowData[rowData.length] = row;
					}
					
					globalGradeIdArray[globalGradeIdArray.length] = '';

					gridColumnNames  = ['<s:text name="sNo"/>','<s:text name="subCategory"/>','<s:text name="product"/>','<s:text name="costPrice" />','<s:text name="currentDamagedStock" />','<s:text name="returnDamagedStock" />','<s:text name="amount" />']
					    				gridColumnModels =
						    									
						    								 [{name:'sNo',index:'sNo',sortable:false, width: 60, align:'center',resizable: false},
							    							 {name:'subCategory',index:'subCategory',sortable:false, width: 60,resizable: false},
					    				    				{name:'product',index:'product',sortable:false, width: 60,resizable: false},
					    				    				{name:'costPrice',index:'costPrice',sortable:false, width: 60, align:'right',resizable: false},
					    				    				{name:'currentDamagedStock',index:'currentDamagedStock',sortable:false, width: 160, align:'right',resizable: false},
					    				    				{name:'returnDamagedStock',index:'returnDamagedStock',sortable:false, width: 160, align:'right',resizable: false},
					    				    				{name:'amount',index:'amount',sortable:false, width: 60, align:'right',resizable: false}
					    				    				]									    					

					jQuery("#detail").jqGrid(
					{		   		
					   	colNames:gridColumnNames,
					   	colModel:gridColumnModels,
					   	width: $("#baseDiv").width(),
					  
					   	height:'auto'
				    });

					for(var i=0;i<rowData.length;i++)
						jQuery("#detail").jqGrid('addRowData',(i+1),rowData[i]);
					
			    	jQuery("#detail").jqGrid('footerData' , 'set' , footerRow);

			    	calculateTotalQuantity();
			    	calculateTotalGrossWeight();
					break;
				}
		});

		
		
	}
}
	else if(selType=='unsold'){

		if(vendorIdSold!="" && orderNoSold!="")
		{
			jQuery("#stockTransferInfo").hide();
			var agentIdx='<%=request.getParameter("agentId")%>';	
			jQuery.post("stockReturns_populateProductsForUnSold",{selectedVendor:vendorIdSold,selectedOrderNo:orderNoSold,selectedWarehouse:warehouseSold},function(result){
			procurementProductJSON=$.parseJSON(result);
		    globalGradeIdArray1=new Array();
					
				for(var count=0;count<procurementProductJSON.products.length;count++)
					{
				
						var gridColumnNames = new Array();
						var gridColumnModels = new Array();
						var footerRow = new Object();
						var rowData = new Array();
						
						var procurementGrades = procurementProductJSON.products[count].procurementGrades;
						for(var i=0;i<procurementGrades.length;i++){

							globalGradeIdArray[globalGradeIdArray.length] = procurementGrades[i].id;
							//globalGradeIdArray1=document.getElementsByName("returnDamagedStock[]");  
							var row = new Object();
							row['sNo']='<div>'+(i+1)+'</div>';
							row['subCategory']='<div>'+procurementGrades[i].subCategory+'</div>';
							row['product'] ='<div>'+procurementGrades[i].product+'</div>'+
							'<input id="productIds" type="hidden"  value='+procurementGrades[i].productId+' maxlength="10" name="productIds"/>';
							row['costPrice'] ='<input id="costPrice" type="hidden"  value='+procurementGrades[i].costPrice+' maxlength="10" name="costPrice[]"/>'
							+'<div>'+procurementGrades[i].costPrice+'</div>';
							row['currentStock'] ='<input id="currentStokQuantity" type="hidden"  value='+procurementGrades[i].currentStock+' maxlength="10" name="currentStockQuantity[]">'
							+'<div>'+procurementGrades[i].currentStock+'</div>';	
							row['returnStock'] = '<input id="returnStock" type="text" onkeyup="calculateStockQuantity('+procurementGrades[i].returnStock+','+procurementGrades[i].productId+')" onkeypress="return isNumber(event)" style="text-align:right;padding-right:1px; width:60px!important;"  value='+procurementGrades[i].returnStock+' maxlength="10" name="returnStock">'
							  +'<sup style="color: red;">*</sup>';
							row['amount']='<div id="totalStockAmount'+(i)+'"></div>';
							rowData[rowData.length] = row;
						}
						
						globalGradeIdArray[globalGradeIdArray.length] = '';

						gridColumnNames  = ['<s:text name="sNo"/>','<s:text name="subCategory"/>','<s:text name="product"/>','<s:text name="costPrice" />','<s:text name="currentStock" />','<s:text name="returnStock" />','<s:text name="amount" />']
						    				gridColumnModels =
							    									
							    								 [{name:'sNo',index:'sNo',sortable:false, width: 60, align:'center',resizable: false},
								    							 {name:'subCategory',index:'subCategory',sortable:false, width: 60,resizable: false},
						    				    				{name:'product',index:'product',sortable:false, width: 60,resizable: false},
						    				    				{name:'costPrice',index:'costPrice',sortable:false, width: 60, align:'right',resizable: false},
						    				    				{name:'currentStock',index:'currentStock',sortable:false, width: 160, align:'right',resizable: false},
						    				    				{name:'returnStock',index:'returnStock',sortable:false, width: 160, align:'right',resizable: false},
						    				    				{name:'amount',index:'amount',sortable:false, width: 60, align:'right',resizable: false}
						    				    				]									    					

						jQuery("#detailUnsold").jqGrid(
						{		   		
						   	colNames:gridColumnNames,
						   	colModel:gridColumnModels,
						   	width: $("#baseDivUnsold").width(),
						  
						   	height:'auto'
					    });

						for(var i=0;i<rowData.length;i++)
							jQuery("#detailUnsold").jqGrid('addRowData',(i+1),rowData[i]);
						
				    	jQuery("#detailUnsold").jqGrid('footerData' , 'set' , footerRow);

				    	calculateStockQuantity();
				    	calculateTotalStockGrossWeight();
						break;
					}
			});

			
			
		}
	} 
}

function calculateStockQuantity(){
	   var totalQuantity=0;
		returnStockArray=new Array();
		returnStockArray=document.getElementsByName("returnStock");  
		
 for(var cnt=0;cnt<returnStockArray.length-1;cnt++)
     {
     var qty=returnStockArray[cnt].value;
     if(!isNaN(qty) && qty.trim()!='')
	   totalQuantity+=parseInt(qty);
 }
 document.getElementById("totaAmountFinal").innerHTML=totalQuantity;	
	
}


function calculateStockQuantity(result,prodId)
{
	var totalAmt=0;
	var totalQuantity=0;
	returnStockArray=new Array();
	productIds=new Array();
	costPrice=new Array();
    costPrice=document.getElementsByName("costPrice[]"); 
    returnStockArray=document.getElementsByName("returnStock");  
    productIds=document.getElementsByName("productIds");
   	var productTotalString='';
	
 	for(var cnt=0;cnt<returnStockArray.length;cnt++)
     {
	     if(returnStockArray[cnt].value!=0)
	     {
			productTotalString+=costPrice[cnt].value+"###"+returnStockArray[cnt].value+"###"+productIds[cnt].value+"@@@";
			document.getElementById("returnStockProducts").value=productTotalString;
	     }
	     
   	 	 var qty=returnStockArray[cnt].value;
    	 var price=costPrice[cnt].value;
    	 var amt=(price)*(qty);
    	 document.getElementById("totalStockAmount"+cnt).innerHTML=amt.toFixed(2);
    	if(!isNaN(qty) && qty.trim()!='')
	  	 totalQuantity+=parseInt(qty);
     	totalAmt+=parseFloat(amt);
       
    }
 document.getElementById("totaAmountFinal").innerHTML=totalAmt.toFixed(2);	
 document.getElementById("totalQtyFinal").innerHTML=totalQuantity;	
}

function calculateTotalQuantity()
{
	
	   var totalQuantity=0;
		returnDamagedArray=new Array();
		returnDamagedArray=document.getElementsByName("returnDamagedStock");  
		
    for(var cnt=0;cnt<returnDamagedArray.length-1;cnt++)
        {
        var qty=returnDamagedArray[cnt].value;
        if(!isNaN(qty) && qty.trim()!='')
 	   totalQuantity+=parseInt(qty);
    }
    document.getElementById("totaAmountFinal").innerHTML=totalQuantity;	
    
}

function calculateTotalQuantity(result,prodId)
{
	var totalAmt=0;
	var totalQuantity=0;
	returnDamagedArray=new Array();
	productIds=new Array();
	costPrice=new Array();
    costPrice=document.getElementsByName("costPrice[]"); 
    returnDamagedArray=document.getElementsByName("returnDamagedStock");  
    productIds=document.getElementsByName("productIds");
   	var productTotalString='';
	
 	for(var cnt=0;cnt<returnDamagedArray.length;cnt++)
     {

	     if(returnDamagedArray[cnt].value!=0)
	     {
			productTotalString+=costPrice[cnt].value+"###"+returnDamagedArray[cnt].value+"###"+productIds[cnt].value+"@@@";
			document.getElementById("returnDamagedProducts").value=productTotalString;
	     }
	     
   	 	 var qty=returnDamagedArray[cnt].value;
    	 var price=costPrice[cnt].value;
    	 var amt=(price)*(qty);
    	 document.getElementById("totalAmount"+cnt).innerHTML=amt.toFixed(2);
    	if(!isNaN(qty) && qty.trim()!='')
	  	 totalQuantity+=parseInt(qty);
     	totalAmt+=parseFloat(amt);
       
    }
 document.getElementById("totaAmountFinal").innerHTML=totalAmt.toFixed(2);	
 document.getElementById("totalQtyFinal").innerHTML=totalQuantity;	
}


function calculateTotalGrossWeight()
{
	var totalAmt=0;
	costPrice=new Array();
	returnDamagedArray=new Array();
	costPrice=document.getElementsByName("costPrice[]"); 
	returnDamagedArray=document.getElementsByName("returnDamagedStock");  
	for(var cnt=0;cnt<costPrice.length;cnt++)
	{
     var amt=(costPrice[cnt].value)*(returnDamagedArray[cnt].value);
     if(!isNaN(amt) && amt!='')
    	 totalAmt+=parseFloat(amt);
 	}
	document.getElementById("totaAmountFinal").innerHTML=totalAmt.toFixed(2);	
}

function calculateTotalStockGrossWeight()
{
	var totalAmt=0;
	costPrice=new Array();
	returnDamagedArray=new Array();
	costPrice=document.getElementsByName("costPrice[]"); 
	returnStockArray=document.getElementsByName("returnStock");  
	for(var cnt=0;cnt<costPrice.length;cnt++)
	{
     var amt=(costPrice[cnt].value)*(returnStockArray[cnt].value);
     if(!isNaN(amt) && amt!='')
    	 totalAmt+=parseFloat(amt);
 	}
	document.getElementById("totaAmountFinal").innerHTML=totalAmt.toFixed(2);	
}


function resetGradeDetails()
{
	jQuery("#mtntDetails").html('');
	for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++)
	{
		jQuery("#returnDamagedStock"+globalGradeIdArray[cnt]).val('0');
		   
	 }
    jQuery("#totalQtyFinal").html('0');
	jQuery("#totaAmountFinal").html('0.000');
}


function isNumber(evt)
{
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
     {
        return false;
 	 }
    return true;
}

function isDecimal(evt)
{
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46)
		{
	        return false;
	    }
	    return true;
}



function submitStockReturns()
{

	var vendor=$('#vendor').val();
    var orderNo=$('#orderNo').val();
    var warehouse=$('#warehouse').val();
    var totAmt=document.getElementById("totaAmountFinal").innerHTML;
    var totDamaged=document.getElementById("totalQtyFinal").innerHTML;
    var vendorCreditAmt=document.getElementById("vendorCreditAmt").innerHTML;
    var remarks = $("#remarks").val();
	var date = jQuery.trim(jQuery("#startDate").val());
	var paymentMode = $('input[name=amtType]:checked').val();
	var returnProduts=document.getElementById("returnDamagedProducts").value;
	var payAmtPrefix = $("#vendorCashAmtRupees").val();
	var payAmtSuffix = $("#vendorCashAmtPaise").val();
	var paymentAmount = payAmtPrefix+"."+payAmtSuffix;
	var selType = $('input:radio[name="type1"]:checked').val();
	var vendorsoldVar = jQuery("#vendorSold").val();
	var orderSoldVar = jQuery("#orderNoSold").val();
	var warehouseVar = jQuery("#warehouseSold").val();
	var returnUnSoldProduts=document.getElementById("returnStockProducts").value;
	var hit=validateData();
	if(hit)
	{
		if(selType=='damaged')
		{
		jQuery.post("stockReturns_populateStockProducts",{selectedVendor:vendor,selectedOrderNo:orderNo,selectedWarehouse:warehouse,startDate:date,
			totaAmountFinal:totAmt,totalQtyFinal:totDamaged,vendorCreditAmt:vendorCreditAmt,paymentAmount:paymentAmount,remarks:remarks,paymentMode:paymentMode,
			returnDamagedProducts:returnProduts,selectedType:selType}
		,function(data){		
			if(data == null || data == ""){				
			}
		else{ 	
			
			document.getElementById("divMsg").innerHTML=data;
			document.getElementById("enableModal").click();
			
		}
			//alert("-end--");
		});
	 }else if(selType=='unsold'){

		 jQuery.post("stockReturns_populateUnSoldStockProducts",{selectedVendor:vendorsoldVar,selectedOrderNo:orderSoldVar,selectedWarehouse:warehouseVar,startDate:date,
				totaAmountFinal:totAmt,totalQtyFinal:totDamaged,vendorCreditAmt:vendorCreditAmt,paymentAmount:paymentAmount,remarks:remarks,paymentMode:paymentMode,
				returnDamagedProducts:returnUnSoldProduts,selectedType:selType}
			,function(data){			
				if(data == null || data == ""){				
				}
			else{ 	 			
				document.getElementById("divMsg").innerHTML=data;
				document.getElementById("enableModal").click();
			}
				//alert("-end--");
			});
	 }				
	}
 }


function validateData()
{
	var selType = $('input:radio[name="type1"]:checked').val();
	var vendor=jQuery("#vendor").val();
	var orderNo=jQuery("#orderNo").val();
	var startDate=jQuery("#startDate").val();
	var warehouse=jQuery("#warehouse").val();
	//var returnDamagedStock=jQuery("#returnDamagedStock").val();
	//alert("Dam:"+returnDamagedStock);
	var paymentMode = $('input[name=amtType]:checked').val();
	var payAmtPrefix = $("#vendorCashAmtRupees").val();
	var payAmtSuffix = $("#vendorCashAmtPaise").val();
	var paymentAmount = payAmtPrefix+"."+payAmtSuffix;
	var returnStock = jQuery("#returnStock").val();
	var vendorsoldVar = jQuery("#vendorSold").val();
	var orderSoldVar = jQuery("#orderNoSold").val();
	var warehouseVar = jQuery("#warehouseSold").val();
	var finalQty = document.getElementById("totalQtyFinal").innerHTML;
    //alert("Total:"+finalQty);
	
	if(selType==undefined){
		jQuery("#validateError").text('<s:text name="empty.Stocktype"/>');
		return false;
	}

	if(selType=='damaged'){
		if(vendor=="")
		{
			jQuery("#validateError").text('<s:text name="empty.vendor"/>');
			return false;
		}
		if((orderNo=="") || (orderNo=='0'))
    	{
			jQuery("#validateError").text('<s:text name="empty.orderNo"/>');
			return false;
		}
		if((warehouse=="-1")||(warehouse=="0"))
		{
			jQuery("#validateError").text('<s:text name="stockreturnempty.warehouse"/>');
			return false;
		}
		if(startDate=="")
		{
			jQuery("#validateError").text('<s:text name="empty.startDate"/>');
			return false;
		}
		if((finalQty=="")||(finalQty=="0"))
		{
			jQuery("#validateError").text('<s:text name="empty.returnDamagedStock"/>');
			return false;
		}
	    gradeInputStr='';
	    currentQuantity=new Array();
	    returnQuantity=new Array();
	    currentQuantity=document.getElementsByName("currentQuantity[]");
		returnQuantity=document.getElementsByName("returnDamagedStock")
		for(var cnt=0;cnt<currentQuantity.length;cnt++)
		{
		   var gradeId=jQuery("#gradeIdHeader"+globalGradeIdArray[cnt]).html();
		   var grade=jQuery("#gradeNameHeader"+globalGradeIdArray[cnt]).html();		   
		   if(parseInt(returnQuantity[cnt].value)>parseInt(currentQuantity[cnt].value))
			{
					jQuery("#validateError").text('<s:text name="inValid.returnDamagedStock"/>');
					return false;
			}
		  
		}
	}else if(selType=='unsold'){
		if(vendorsoldVar=="")
		{
			jQuery("#validateError").text('<s:text name="empty.vendor"/>');
			return false;
		}
		if((orderSoldVar=="") || (orderSoldVar=='0'))
	    {
			jQuery("#validateError").text('<s:text name="empty.orderNo"/>');
			return false;
		}

		if((warehouseVar=="-1")||(warehouseVar=="0"))
		{
			jQuery("#validateError").text('<s:text name="stockreturnempty.warehouse"/>');
			return false;
		}
		
		if((finalQty=="")||(finalQty=="0"))
		{
			jQuery("#validateError").text('<s:text name="empty.returnDamagedStock"/>');
			return false;
		}
		    gradeInputStr='';
		    currentQuantity=new Array();
		    returnQuantity=new Array();
		    currentQuantity=document.getElementsByName("currentStockQuantity[]");
			returnQuantity=document.getElementsByName("returnStock")
			for(var cnt=0;cnt<currentQuantity.length;cnt++)
			{
			   var gradeId=jQuery("#gradeIdHeader"+globalGradeIdArray[cnt]).html();
			   var grade=jQuery("#gradeNameHeader"+globalGradeIdArray[cnt]).html();		   
			   if(parseInt(returnQuantity[cnt].value)>parseInt(currentQuantity[cnt].value))
				{
						jQuery("#validateError").text('<s:text name="inValid.returnDamagedStock"/>');
						return false;
				}
			  
			}
		
	}
		
	if(paymentMode == "" || paymentMode == null || paymentMode == undefined) {
		jQuery("#validateError").text('<s:text name="empty.cashType"/>');
		return false;
	}

	if(paymentMode == 0 && payAmtPrefix == 0 && payAmtSuffix== 00) {
		jQuery("#validateError").text('<s:text name="empty.amount"/>');
		return false;
	}
	if(paymentMode == 0 && payAmtPrefix <= 0 ) {
		jQuery("#validateError").text('<s:text name="validAmunt"/>');
		return false;
	}

	return true;
}


function hideDivBasedOnCashType(evt){
	var cashVal = $(evt).val();
	if(cashVal==''){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		document.getElementById("vendorCreditAmt").innerHTML="";
	}
	else if(cashVal==1){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").show();
		jQuery(".creditTypeDiv").show();
		jQuery("#vendorCashAmtRupees").val("0");
		jQuery("#vendorCashAmtPaise").val("00");
		document.getElementById("vendorCreditAmt").innerHTML=document.getElementById("totaAmountFinal").innerHTML;	
	}
	else if(cashVal==undefined){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		document.getElementById("vendorCreditAmt").innerHTML="";
	}
	else {
		jQuery(".cashTypeDivLabel").show();
		jQuery(".cashTypeDiv").show();
		jQuery("#vendorCashAmtRupees").val("0");
		jQuery("#vendorCashAmtPaise").val("00");
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		document.getElementById("vendorCreditAmt").innerHTML="";
	}
}


function showUnsoldType(evt){
	var radioType = $(evt).val();
	
	if(radioType==undefined){
		jQuery("#vendorLabel1").show();
		jQuery("#vendorDiv1").show();
		jQuery("#orderLabel1").show();
		jQuery("#orderDiv1").show();
		jQuery("#warehouseLabel1").show();
		jQuery("#warehouseDiv1").show();
		jQuery("#orderLabel2").hide();
		jQuery("#orderDiv2").hide();
		jQuery("#warehouseLabel2").hide();
		jQuery("#warehouseDiv2").hide();
		jQuery("#vendorLabel2").hide();
		jQuery("#vendorDiv2").hide();
		//document.getElementById("vendorCreditAmt").innerHTML="";
	}
	if(radioType=='damaged')
	{
		
		resetAllData();
		jQuery("#vendorLabel1").show();
		jQuery("#vendorDiv1").show();
		jQuery("#orderLabel1").show();
		jQuery("#orderDiv1").show();
		jQuery("#warehouseLabel1").show();
		jQuery("#warehouseDiv1").show();
		jQuery("#orderLabel2").hide();
		jQuery("#orderDiv2").hide();
		jQuery("#warehouseLabel2").hide();
		jQuery("#warehouseDiv2").hide();
		jQuery("#vendorLabel2").hide();
		jQuery("#vendorDiv2").hide();
		jQuery("#detailUnsold").empty();
		jQuery("#baseDivUnsold").hide();
		jQuery("#baseDiv").show();
		jQuery("#detail").empty();
		jQuery("#stockTransferInfo").show();
		 jQuery("#detail").jqGrid('GridUnload');   
		createGrid();
		//document.getElementById("vendorCreditAmt").innerHTML=document.getElementById("totaAmountFinal").innerHTML;	
	}
	if(radioType=='unsold')
	{
		resetAllData();
		//alert("unsold");
		jQuery("#vendorLabel2").show();
		jQuery("#vendorDiv2").show();
		jQuery("#orderLabel2").show();
		jQuery("#orderDiv2").show();
		jQuery("#warehouseLabel2").show();
		jQuery("#warehouseDiv2").show();
		jQuery("#orderLabel1").hide();
		jQuery("#orderDiv1").hide();
		jQuery("#warehouseLabel1").hide();
		jQuery("#warehouseDiv1").hide();
		jQuery("#vendorLabel1").hide();
		jQuery("#vendorDiv1").hide();
		
		jQuery("#detail").empty();
		jQuery("#baseDiv").hide();
		jQuery("#detailUnsold").empty();
		jQuery("#baseDivUnsold").show();
		jQuery("#stockTransferInfo").show();
		 jQuery("#detailUnsold").jqGrid('GridUnload');   
	}
	
}



function loadOrderNo(obj){
	var selectedVendor=jQuery("#vendor option:selected").val();
	jQuery.post("stockReturns_populateOrderNoByVendor.action",{id:obj.value,dt:new Date(),selectedVendor:selectedVendor},function(result){
		insertOptions("orderNo",jQuery.parseJSON(result));
		loadWarehouse(document.getElementById("orderNo"));
	});
}

function loadOrderNoforQty(obj){
	var selectedVendor1=$("#vendorSold option:selected").val();
	jQuery.post("stockReturns_populateUnsoldOrderNoByVendor.action",{id:obj.value,dt:new Date(),selectedVendor:selectedVendor1},function(result){
		insertOptions("orderNoSold",jQuery.parseJSON(result));
		loadUnSoldWarehouse(document.getElementById("orderNoSold"));
	});
	
}

function loadWarehouse(obj){
	var selectedVendor=jQuery("#vendor").val();

	var selectedOrderNo = jQuery("#orderNo").val();
	jQuery.post("stockReturns_populateWarehouse.action",{id:obj.value,dt:new Date(),selectedVendor:selectedVendor,selectedOrderNo:selectedOrderNo},function(result){
		insertOptions("warehouse",jQuery.parseJSON(result));
	});
	
}

function loadUnSoldWarehouse(obj){
	var selectedVendor=jQuery("#vendorSold option:selected").val();
	var selectedOrderNo = jQuery("#orderNoSold option:selected").text();
	jQuery.post("stockReturns_populateWarehouse.action",{id:obj.value,dt:new Date(),selectedVendor:selectedVendor,selectedOrderNo:selectedOrderNo},function(result){
		insertOptions("warehouseSold",jQuery.parseJSON(result));
	});
	
}

function resetDatas(){
	document.getElementById('orderNo').selectedIndex = "";
	document.getElementById("warehouse").selectedIndex="";
}


function resetAllData()
{
	document.getElementById("warehouse").selectedIndex="";
	document.getElementById("orderNo").selectedIndex="";
	document.getElementById("vendor").selectedIndex="";
	document.getElementById("vendor").selectedIndex="";
	document.getElementById("vendorSold").selectedIndex="";
	document.getElementById("warehouseSold").selectedIndex="";
	document.getElementById("orderNoSold").selectedIndex="";
	document.getElementById("startDate").value='<s:property value="currentDate" />';
	document.getElementById("validateError").innerHTML="";
	document.getElementById("remarks").value="";
	document.getElementById("vendorCashBal").innerHTML="";
	document.getElementById("vendorCreditBal").innerHTML="";
	document.getElementById("totaAmountFinal").innerHTML="";	
	document.getElementById("totalQtyFinal").innerHTML="";	
	document.getElementById("vendorCreditAmt").innerHTML="";	
	
}

function loadVendorAccBal(){
	
	var selectedVendor = $("#vendor option:selected").val();
	var vendorCashBalValue;
	var vendorCreditBalValue;
	$.post("stockReturns_populateVendorAccBalance",{selectedVendor:selectedVendor},function(data){
		if(data!=null && data!=""){
			var dataArr = data.split(",");
			vendorCashBalValue=parseFloat(dataArr[0]).toFixed(2);
			vendorCreditBalValue = parseFloat(dataArr[1]).toFixed(2);
			document.getElementById("vendorCashBal").innerHTML=vendorCashBalValue;
			document.getElementById("vendorCreditBal").innerHTML=vendorCreditBalValue;
			}
	});

}
function loadVendorAccBalForUnSold(){
	
	var selectedVendor = $("#vendorSold option:selected").val();
	var vendorCashBalValue;
	var vendorCreditBalValue;
	$.post("stockReturns_populateVendorAccBalance",{selectedVendor:selectedVendor},function(data){
		if(data!=null && data!=""){
			var dataArr = data.split(",");
			vendorCashBalValue=parseFloat(dataArr[0]).toFixed(2);
			vendorCreditBalValue = parseFloat(dataArr[1]).toFixed(2);
			document.getElementById("vendorCashBal").innerHTML=vendorCashBalValue;
			document.getElementById("vendorCreditBal").innerHTML=vendorCreditBalValue;
			}
	});

}

function enablePopupAlert(){
	$('body').css('overflow','hidden');
	$('#pendingRestartAlertErrMsg').html('');
	$('#popupBackground').addClass('loginPanelContent')
	$('#popupBackground').css('width','100%');
	$('#popupBackground').css('height',getWindowHeight());
	$('#popupBackground').css('top','0');
	$('#popupBackground').css('left','0');
	$('#popupBackground').show();
	$('#restartAlert').css({top:'50%',left:'50%',margin:'-'+($('#restartAlert').height() / 2)+'px 0 0 -'+($('#restartAlert').width() / 2)+'px'});
	$('#restartAlert').show();

	window.location.hash="#restartAlert";
}

function disablePopupAlert(){
	$('#pendingRestartAlertErrMsg').html('');
	$('#popupBackground').hide();
	$('#restartAlert').hide();
	$('body').css('overflow','');
	document.form.action = "stockReturns_list.action";
	document.listForm.submit();
	
}
function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}

var printWindowCnt=0;
var windowRef;
function printReceipt(receiptNo){
	jQuery("#receiptNo").val(receiptNo);	
	var targetName="printWindow"+printWindowCnt;
	windowRef = window.open('',targetName);
	try{
		windowRef.referenceWindow = windowRef;		
	}catch(e){
	}
	jQuery("#receiptForm").attr("target",targetName);	
	jQuery("#receiptForm").submit();
	++printWindowCnt;
}

</script>
</head>
<body>
<div style="color: red;">
<div class="div.error"><sup>*</sup><s:text name="reqd.field" /></div>
<div id="validateError" class="error"
	style="text-align: center; padding: 5px 0 0 0"></div>
<div id="actionError" class="error"
	style="text-align: center; padding: 5px 0 0 0"><s:actionerror />
<s:fielderror /></div>
</div>
<s:form name="form" id="stockTransferForm" action="stockTransfer_create" method="post"
	cssClass="fillform">
	<s:hidden name="agentId" />
	<s:hidden id="gradeInputString" name="gradeInputString" />
	
	<div class="column-1">
	<h3 style="padding: 10px; font-size: 16px; font-weight: bold;"><s:text name="info.general" /></h3>	
	</div>
	<table class="table table-bordered aspect-detail">
		<tr>
			<td>
				<s:text name="stockReturns.type" /><sup style="color: red;">*</sup>
			</td>
			<td>
				<s:radio list="#{'damaged':'Damaged','unsold':'Unsold'}" name="type1" id="type1" onchange="showUnsoldType(this);"></s:radio>
			</td>
		</tr>
	</table> 
	<table class="table table-bordered aspect-detail" id="gradeInfo">
	
	     <tr>
			<td id="vendorLabel1" ><s:text name="stockReturns.vendor" /><sup style="color: red;">*</sup></td></td>
			
			<td id="vendorDiv1"><s:select
				name="selectedVendor" list="selectVendorList" headerKey="" headerValue="%{getText('txt.select')}" listKey="key" cssClass="form-control input-sm select2"
				listValue="value" id="vendor" cssStyle="width:160px !important;" onchange="loadOrderNo(this);resetDatas();loadVendorAccBal();"/>				
			
			
			<td id="orderLabel1"><s:text name="stockReturns.orderNo" /><sup style="color: red;">*</sup></td></td>
			<td id="orderDiv1"><s:select
				name="selectedOrderNo" list="orderNoList" onchange="populateProducts();loadWarehouse(this);"  headerKey="" headerValue="%{getText('txt.select')}" listKey="id"
				listValue="name" id="orderNo" cssStyle="width:160px !important;" cssClass="form-control input-sm select2"/>				
			
			<td id="warehouseLabel1" ><s:text name="stockReturns.warehouse" /><sup style="color: red;">*</sup></td></td>
			<td id="warehouseDiv1" ><s:select
				name="selectedWarehouse" list="warehouseList" headerKey="-1" headerValue="%{getText('txt.select')}" listKey="id"
				listValue="name" id="warehouse" cssStyle="width:160px !important;" cssClass="form-control input-sm select2" />				
			
			
			<td id="vendorLabel2"><s:text name="stockReturns.vendor" /></td>
			<td id="vendorDiv2"><s:select
				name="selectedVendor" list="vendorList" headerKey="" headerValue="%{getText('txt.select')}" listKey="key" cssClass="form-control input-sm select2"
				listValue="value" id="vendorSold" cssStyle="width:160px !important;" onchange="loadOrderNoforQty(this);resetDatas();loadVendorAccBalForUnSold();"/>				
			<sup style="color: red;">*</sup></td>			
			<td id="orderLabel2"><s:text name="stockReturns.orderNo" /><sup style="color: red;">*</sup></td></td>
			<td id="orderDiv2"><s:select name="selectedOrderNo" list="allOrderNo" onchange="loadUnSoldWarehouse(this);" headerKey="" headerValue="%{getText('txt.select')}" listKey="id"
				listValue="name" id="orderNoSold" cssStyle="width:160px !important;" cssClass="form-control input-sm select2"/>				
			
			<td id="warehouseLabel2" ><s:text name="stockReturns.warehouse" /></td>
			<td id="warehouseDiv2" ><s:select name="selectedWarehouse" list="allWarehouse" onchange="populateProducts();" headerKey="-1" headerValue="%{getText('txt.select')}" listKey="id"
				listValue="name" id="warehouseSold" cssStyle="width:160px !important;" cssClass="form-control input-sm select2"/>		
			<sup style="color: red;">*</sup></td>
			
			
			<td><s:text name="stockReturns.dateOfReturn" /> <sup style="color: red;">*</sup></td>
			<td><s:textfield name="startDate" id="startDate" readonly="true" theme="simple"  cssStyle="width:160px !important;" /></td>
					
		</tr>
		
		
	</table>

	<br />
	
	<div class="column-1">
	<h3 style="padding: 10px; font-size: 16px; font-weight: bold;"><s:text name="info.stockReturns" /></h3>	
	</div>
		
	<div id="stockTransferInfo" style="border:1px solid #D2D695;text-align:center;height:20px">
		<s:text name="noRecordFound" />
	</div>	
		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>		
		</div>
	
	<div style="width: 100%;" id="baseDivUnsold">
		<table id="detailUnsold"></table>		
	</div>
		
	<br />
	<div>
	<table class="table table-bordered aspect-detail">

		<tfoot>
			<tr>
				<td   style="border: none !important;">&nbsp;</td>
				<td class="alignRight"
					style="border: none !important; padding-right: 10px !important; padding-top: 6px !important; padding-bottom: 6px !important;"><s:text
					name="total" /></td>
				<td class="alignRight"
					style="width:70px;padding-right: 20px !important; padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;"><div id="totalQtyFinal" style="font-weight: bold;font-size:14px;">0</div></td>
				
				<td class="alignRight"  style="width:100px;padding-right:0px !important; padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;"><div id="totaAmountFinal"  style="font-weight: bold;font-size:14px; ">0.00</div></td>
			</tr>
		</tfoot>
	
		</table>
		
	</div>	
	

	<div class="column-1">
	<h3 style="padding: 10px; font-size: 16px; font-weight: bold;"><s:text name="info.vendorPayment" /></h3>	
	</div>
	<table id="vendorInfoTbl" class="table table-bordered aspect-detail">
 
		<thead>
			<tr class="odd">
				<th width="25%"><s:text name="warehouseProduct.vendorAccBal" /></th>
				<th width="25%"><s:text name="warehouseProduct.type" /></th>
				<th width="25%" class="cashTypeDivLabel"><s:text name="warehouseProduct.enterAmt" /></th>
				<th width="25%" class="creditTypeDivLabel"><s:text name="warehouseProduct.creditText" /></th>
				<th width="25%"><s:text name="warehouseProduct.remarks" /></th>
			</tr>

			<tr>
			   <td>
			   			<table cellpadding=0; cellspacing="0" class="borNone" style="border: none !important;">
  						<tr>
    						<td><s:text name="cashType1"/> : </td>
    						<td id="vendorCashBal" style="font-weight: bold;"></td>
    						<td><s:text name="cashType2"/> : </td>
    						<td id="vendorCreditBal" style="font-weight: bold;"></td>
    				 	</tr>
    				 </table>
			 	</td>
				
				<td>
					<s:radio list="cashType"  listKey="key" listValue="value" name="amtType" theme="simple" onchange="hideDivBasedOnCashType(this);"/>
				</td>
		     	
		     	<td class="cashTypeDiv">
					<s:textfield id="vendorCashAmtRupees" name="cashAmtRupee"
								onkeypress="return isNumber(event)" cssStyle="text-align:right;padding-right:1px; width:50px!important;"/>
								 .<s:textfield id="vendorCashAmtPaise" name="cashAmtPaise"
								 onkeypress="return isNumber(event)" cssStyle="text-align:right;padding-right:1px; width:50px!important;"/>
				</td>
				
				<td class="creditTypeDiv">
					<div id="vendorCreditAmt"></div>
				</td>
				
				<td>
					<s:textarea id="remarks" name="remarks"/>
				</td>
			</tr>
			
		</thead>
		
		<thead>
			<tr class="odd">
				<th width="25%" style="display:none;"><s:text name="warehouseProduct.vendorFinalBal" /></th>
			</tr>
			
			<tr style="height:40px">
				<td class="alignRight" style="text-align: center;padding-right: 10px !important; display:none;">
					<div id="vendorFinalTaxAmt" style="font-weight: bold;"></div>
				</td>
				
			</tr>
		</thead>
		
	</table>

	
	<div>
	<div class="yui-skin-sam" id="loadList" style="display: block">
	<sec:authorize ifAllGranted="service.stockReturns.create">
		<span id="savebutton" class="">
			<span class="first-child">
			<button id="submitButton"  class="save-btn btn btn-success" type="button" onclick="submitStockReturns();">
				<font color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>
			</span>
		</span>
	</sec:authorize> 
		<span class="">
			<span class="first-child"><a id="cancelbutton" onclick="cancel();" class="cancel-btn btn btn-sts"> 
			<font color="#FFFFFF"> <s:text name="cancel.button" /> </font> </a></span></span></div>
	</div>
	
	<br />
</s:form>
<s:form name="listForm" id="listForm" action="stockReturns_list">
	<s:hidden name="currentPage" />
</s:form>
<%-- <div id="restartAlert" class="popPendingMTNTAlert">
<div id="pendingRestartAlertErrMsg" class="popPendingMTNTAlertErrMsg"
	align="center"></div>
<div id="defaultRestartAlert">	
	<div id="divMsg" align="center"></div>
	<table align="center">
	<tr>
	<td id="closePopup" style="text-align: center"><input id="ok"
			type="button" class="popBtn" value="<s:text name="ok"/>"
			onclick="disablePopupAlert();" /></td>
	</tr>
</table>
</div>
</div> --%>

<button type="button" data-toggle="modal" data-target="#myModal" style="display:none" id="enableModal">Open Modal</button>

  <!-- Modal -->
 <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick="disablePopupAlert()">&times;</button>
          <h4 class="modal-title">Warehouse Stock Returns</h4>
        </div>
        <div class="modal-body">
          <div id="divMsg" align="center"></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal" onclick="disablePopupAlert()">Close</button>
        </div>
      </div>
      
    </div>
  </div>

<s:textfield id="returnDamagedProducts" name="returnDamagedProducts" cssStyle="display:none;"></s:textfield>
<s:textfield id="returnStockProducts" name="returnStockProducts" cssStyle="display:none;"></s:textfield>

<!--<s:form action="stockTransfer_populatePrintHTML" id="receiptForm" method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
</s:form>--%>
<!-- Include web_transaction-assets.jsp for getting popups and timer related logics -->
<%--<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>--%>

<s:form action="stockReturns_populatePrintHTML" id="receiptForm"
	method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNo"></s:hidden>
</s:form>
</body>
