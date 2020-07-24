<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">

var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();
var productsInfoArray = new Array();
var productTotalString=' ';
var prodEditArray;
var prodString;
var isEdit;

jQuery(document).ready(function(){
	//$('.tableTemplate').hide();
	isEdit = '<s:property value="command" />';
	if(isEdit=='update'){
		$('#productInfoTbl').val("");
	    prodString = '<s:property value="prodEditArray" escape="false" />';
	    if(prodString!=""){
	    	var jsonObj=JSON.parse(prodString);
	    	for(var i=0;i<jsonObj.length;i++){
	    		 var temp=jsonObj[i];
		    		productArray = {
		    				 selectedBenefitFarmer :temp.selectedBenefitFarmer,
		    				 selectedkgs:temp.selectedkgs,
		    				 selectedScheme:temp.selectedScheme,
		    				 receivedDate:temp.receivedDate,
		    				 selectedAmt: temp.selectedAmt,
		    				 selectedcontribute : temp.selectedcontribute
		    				 
		    	         };
		    		
		    		 productsInfoArray[productsInfoArray.length] = productArray;
		    		 reloadTable();
		    	}
	
	    	 $('#tableTemplate').removeClass("hide");
	}
	}
});



jQuery(function ($) {

$( "#calenderBenefit" ).datepicker(
		{
		maxDate: '0',
		beforeShow : function()
		{
		jQuery( this ).datepicker({ maxDate: 0 });
		},
		changeMonth: true,
		changeYear: true
		}
		);
});	

function resetData(){
	document.getElementById('benFarmer').value = "-1";
	$("#kgs").val("");
	document.getElementById('schemeId').value = "-1";
	$("#amt").val("");
	$("#calenderBenefit").val("");
	$("#contribute").val("");
	$("#error").val("");
}

function addRowBenfit(){
	
	 var hit=false;
	var selectedBenefitFarmer =  $("#benFarmer option:selected").text();
	var selBenefitFarmer =document.getElementById("benFarmer").value;
	var selectedkgs = document.getElementById("kgs").value;
	var selectedScheme =$("#schemeId option:selected").text();
	var selselectedScheme =document.getElementById("schemeId").value;
	var receivedDate =  $("#calenderBenefit").val();
	var selectedAmt=  document.getElementById("amt").value;
	var selectedcontribute =document.getElementById("contribute").value;
	var productArray=null;
	var productExists=false;
	
	if(selBenefitFarmer=='-1'&&selectedkgs==""&&selselectedScheme=='-1'&&receivedDate==""&&selectedAmt==""&&selectedcontribute==""){
		document.getElementById("error").innerHTML='<s:text name="noDetailsFound"/>';
		hit=true;
		return false;
	} else if(selBenefitFarmer=='-1'){
		document.getElementById("error").innerHTML='<s:text name="emptyBen"/>';
		hit=true;
		return false;
	}else if(selectedkgs==""){
		document.getElementById("error").innerHTML='<s:text name="emptyKgs"/>';
		hit=true;
		return false;
	}else if(selselectedScheme=='-1'){
		document.getElementById("error").innerHTML='<s:text name="emptyScheme"/>';
		hit=true;
		return false;
	}else if(receivedDate==""){
		document.getElementById("error").innerHTML='<s:text name="emptyDate"/>';
		hit=true;
		return false;
	}else if(selectedAmt==""){
		document.getElementById("error").innerHTML='<s:text name="emptyAmt"/>';
		hit=true;
		return false;
	}else if(selectedcontribute==""){
		document.getElementById("error").innerHTML='<s:text name="emptyCont"/>';
		hit=true;
		return false;
	}  
	if(selectedBenefitFarmer=="Select"){
		selectedBenefitFarmer=" ";
	}
	if(selectedScheme=="Select"){
		selectedScheme=" ";
	}
	 $('#tableTemplate').removeClass("hide");	
	 productArray = {
			 selectedBenefitFarmer :selectedBenefitFarmer,
			 selectedkgs:selectedkgs,
			 selectedScheme:selectedScheme,
			 receivedDate:receivedDate,
			 selectedAmt: selectedAmt,
			 selectedcontribute : selectedcontribute
			 
         };
	 productsInfoArray[productsInfoArray.length] = productArray;
	
	
reloadTable();
resetData();

}

function reloadTable(){
	
	var tbodyRow="";
	var tfootArray = new Array();	
	jQuery('#productInfoTbl').html('');
	var rowCount=0;
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].selectedBenefitFarmer+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].selectedkgs+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].selectedScheme+'</td>'+
					'<td style="text-align:right; width="5%" " >'+productsInfoArray[cnt].receivedDate+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].selectedAmt+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].selectedcontribute+'</td>'+
					'<td style="text-align:center;"><a class="fa fa-trash" onclick="removeRow('+cnt+')"/></td>'+
					'</tr>';
	  }
		jQuery('#productInfoTbl').html(tbodyRow);
	}
}


function removeRow(indx){

	for(var i=0;i<productTotalArray.length;i++){
	   
		productTotalArray[i].selectedBenefitFarmer-=productsInfoArray[indx].selectedBenefitFarmer;
		productTotalArray[i].selectedkgs-=productsInfoArray[indx].selectedkgs;
		productTotalArray[i].selectedScheme-=productsInfoArray[indx].selectedScheme;
		productTotalArray[i].receivedDate-=productsInfoArray[indx].receivedDate;
		productTotalArray[i].selectedAmt-=productsInfoArray[indx].selectedAmt;
		productTotalArray[i].selectedcontribute-=productsInfoArray[indx].selectedcontribute;
		break;
		
	}
	if(productsInfoArray.length>0){				
		productsInfoArray.splice(indx,1);		
	}
	resetData();
	reloadTable();
}

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}


</script>

<tr class="productDetails">
	<s:if test="sangham=='01'">
		<td><s:text name="benefitDetails" /></td>
	</s:if>
	<s:if test="sangham=='02'">
		<td><s:text name="benefitDetails" /></td>
	</s:if>
	<td><s:text name="kgs" /></td>
	<td><s:text name="scheme" /></td>
	<td><s:text name="datee" /></td>
	<td><s:text name="amt" /></td>
	<td><s:text name="contri" /></td>
	<td><s:text name="action" /></td>
</tr>

<tr>
	<s:if test="sangham=='01'">
		<td><s:select name="selectedBenefitFarmer" list="benefitFarmer"
				headerKey="-1" cssClass="form-control input-sm" headerValue="Select"
				id="benFarmer" /></td>
	</s:if>
	<s:if test="sangham=='02'">

		<td><s:select name="selectedBenefitLand" list="benefitLandless"
				headerKey="-1" cssClass="form-control input-sm" headerValue="Select"
				id="benFarmer" /></td>
	</s:if>

	<td><s:textfield id="kgs" name="selectedkgs" maxlength="10"
			onkeypress="return isDecimal(event)" size="16"/></td>

	<td><s:select name="selectedScheme" list="schemeList"
			headerKey="-1" cssClass="form-control input-sm" headerValue="Select"
			id="schemeId" /></td>

	<td><s:textfield name="receivedDate" id="calenderBenefit"
			readonly="true" theme="simple" cssClass="form-control input-sm" /></td>

	<td><s:textfield id="amt" name="selectedAmt" maxlength="8"
			onkeypress="return isDecimal(event)" size="18" /></td>

	<td><s:textfield id="contribute" name="selectedcontribute"
			maxlength="8" onkeypress="return isDecimal(event)" size="18" /></td>

	<td class="textAlignCenter">
		<button onclick="addRowBenfit()" aria-hidden="true"
			class="btn btn-sm btn-success" type="button">
			<i class="fa fa-check"></i>
		</button>
		<button onclick="resetData()" aria-hidden="true "
			class="btn btn-sm btn-danger" type="button">
			<i class="fa fa-trash"></i>
		</button>
	</td>

</tr>


<table id="productInfoTbl" class="table table-bordered aspect-detail">
	<thead>
		<tr id="tableTemplate" class="hide">
			<th><s:text name="Sno" /></th>
			<th><s:text name="Benefitary" /></th>
			<th><s:text name="Kgs" /></th>
			<th><s:text name="Scheme" /></th>
			<th><s:text name="datee" /></th>
			<th><s:text name="amt" /></th>
			<th><s:text name="contri" /></th>
			<th><s:text name="action" /></th>
		</tr>

	</thead>

</table>


