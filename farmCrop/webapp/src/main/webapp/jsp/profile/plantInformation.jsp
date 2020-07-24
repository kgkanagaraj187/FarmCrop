<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
jQuery(document).ready(function(){
/* 
	 $("#farmerPlants").keypress(function(event){
	        var inputValue = event.which;
	        // allow letters and whitespaces only.
	        if(!(inputValue >= 65 && inputValue <= 120) && (inputValue != 32 && inputValue != 0)) { 
	            event.preventDefault(); 
	        }
	    }); */
	 
	// $('#plantTableTemplate').hide();
	 
	 isEdit = '<s:property value="command" />';
	 if(isEdit=='update'){
	  $('#plantDetailInfo').val("");
	     prodString = '<s:property value="getplantDetailsList" escape="false" />';
	     if(prodString!=""){
	      var jsonObj=JSON.parse(prodString);
	      for(var i=0;i<jsonObj.length;i++){
	        var temp=jsonObj[i];
	        productArrays = {
	        		 selectedfarmerPlants:temp.selectedfarmerPlants,
	        		 selectedplantedPlants:temp.selectedplantedPlants,
	        		 selectednoOfLivePlants:temp.selectednoOfLivePlants
	         };
	        
	         productsInfoArrays[productsInfoArrays.length] = productArrays;
	         reloadTable1();
	        
	       }
	      
	  
	      $('#plantTableTemplate').removeClass("hide");
	      
	  
	  
	 }
	 }
});

var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();
var productsInfoArrays = new Array();
var productTotalString=' ';



function resetData1(){
	$("#farmerPlants").val("");
	$("#plantedPlants").val("");
	$("#noOfLivePlants").val("");
	$("#validateError").val("");
}

function addPlants(){
	 var hit=false;
	 var selectedfarmerPlants = document.getElementById("farmerPlants").value;
	 var selectedplantedPlants = document.getElementById("plantedPlants").value;
	 var selectednoOfLivePlants=document.getElementById("noOfLivePlants").value;
	 var plantsArray=null;
	 
	 if(selectedfarmerPlants==" "&&selectedplantedPlants==" "&&selectednoOfLivePlants==""){
			document.getElementById("validateError").innerHTML='<s:text name="noDetailsFound"/>';
			hit=true;
			return false;
		} else if(selectedfarmerPlants==""){
			document.getElementById("validateError").innerHTML='<s:text name="emptyPlants"/>';
			hit=true;
			return false;
		}else if(selectedplantedPlants==""){
			document.getElementById("validateError").innerHTML='<s:text name="emptyPlantedPlants"/>';
			hit=true;
			return false;
		}else if(selectednoOfLivePlants==""){
			document.getElementById("validateError").innerHTML='<s:text name="emptyLivePlants"/>';
			hit=true;
			return false;
		}
		 
	 $('#plantTableTemplate').removeClass("hide");	
	 productArrays = {
			 selectedfarmerPlants:selectedfarmerPlants,
			 selectedplantedPlants:selectedplantedPlants,
			 selectednoOfLivePlants:selectednoOfLivePlants
		   };
	 productsInfoArrays[productsInfoArrays.length] = productArrays;
	var productArrays=null;
	var productExists=false;
	
reloadTable1();
resetData1();

}

function reloadTable1(){
	var tbodyRow = "";
	var tfootArray = new Array();	
	jQuery('#plantDetailInfo').html('');
	var rowCount=0;
	for(var cnt=0;cnt<productsInfoArrays.length;cnt++){
		if(!productsInfoArrays[cnt].isEdit){
		rowCount++;
		tbodyRow += '<tr><td style="text-align:left;">'+productsInfoArrays[cnt].selectedfarmerPlants+'</td>'+
					'<td style="text-align:left;">'+productsInfoArrays[cnt].selectedplantedPlants+'</td>'+
					'<td style="text-align:right;">'+productsInfoArrays[cnt].selectednoOfLivePlants+'</td>'+
					'<td style="text-align:center;"><a class="fa fa-trash" onclick="removeRecord('+cnt+')"/></td></tr>';
	  }
		
		jQuery('#plantDetailInfo').html(tbodyRow);
		productTotalStrings="";
		productTotalStrings+=productsInfoArrays[cnt].selectedfarmerPlants+"##"+productsInfoArrays[cnt].selectedplantedPlants+"##"+productsInfoArrays[cnt].selectednoOfLivePlants+"||";
		
	}
}


function removeRecord(indx){
	for(var i=0;i<productTotalArray.length;i++){
	   	productTotalArray[i].selectedfarmerPlants-=productsInfoArrays[indx].selectedfarmerPlants;
		productTotalArray[i].selectedplantedPlants-=productsInfoArrays[indx].selectedplantedPlants;
		productTotalArray[i].selectednoOfLivePlants-=productsInfoArrays[indx].selectednoOfLivePlants;
		break;
		}
	if(productsInfoArrays.length>0){				
		productsInfoArrays.splice(indx,1);		
	}
	resetData1();
	reloadTable1();
}

function isNumber(evt) {
    var iKeyCode = (evt.which) ? evt.which : evt.keyCode
    if (iKeyCode != 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57))
        return false;

    return true;
}    


</script>


<tr class="plantDetails">
	<td><s:text name="farm.farmerPlants" /></td>
	<td><s:text name="farm.plantedPlants" /></td>
	<td><s:text name="farm.livePlants" /></td>
	<td><s:text name="action" /></td>

</tr>
<tr>
	<td><s:textfield id="farmerPlants" name="selectedfarmerPlants"
			maxlength="25"/></td>
			
	<td><s:textfield id="plantedPlants" name="selectedplantedPlants"
			maxlength="10" onkeypress="return isNumber(event)"/></td>
			
	<td><s:textfield id="noOfLivePlants" name="selectednoOfLivePlants"
			maxlength="10" onkeypress="return isNumber(event)"/></td>

	<td class="textAlignCenter">
		<button onclick="addPlants()" aria-hidden="true"
			class="btn btn-sm btn-success" type="button">
			<i class="fa fa-check"></i>
		</button> <!-- 	<button onclick="resetData()" aria-hidden="true "
					class="btn btn-sm btn-danger" type="button">
					<i class="fa fa-trash"></i>
				</button> -->
	</td>

</tr>


<table id="plantDetailInfo" class="table table-bordered aspect-detail">
	<thead>
		<tr id="plantTableTemplate" class="hide">

			<th><s:text name="Sno" /></th>
			<th><s:text name="farm.farmerPlants" /></th>
			<th><s:text name="farm.plantedPlants" /></th>
			<th><s:text name="farm.livePlants" /></th>
			<th><s:text name="action" /></th>
		</tr>

	</thead>
	<tbody>
		
	</tbody>
</table>





