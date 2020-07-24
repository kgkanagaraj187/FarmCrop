<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<style>
table thead tr th {
	text-align: center;
}

.select2-selection{
	width:150px;
}
</style>
<script type="text/javascript">
var fodder=1;
var currentTenantId ='<s:property value="getCurrentTenantId()"/>';
jQuery(document).ready(function(){
	  $('.select2').select2();
	
refreshAnimalTemplateList1();
addInventry1();

	
});	


$(document).ready(function () {
	callSelectize();
	
	//var val=$("#fodder").val();
	
    //called when key is pressed in textbox
  	$(".tableTd22").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#msg").html("Digits Only").show();
               return false;
     	}
   	});
    
  	$(".tableTd23").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#msg").html("Digits Only").show();
               return false;
     	}
   	});
  	
  	$(".tableTd24").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#msg").html("Digits Only").show();
               return false;
     	}
   	});
});

function callSelectize(){
	$('.select2Multi').select2();
	jQuery(".select2-search__field").hide();
}

function addInventry1(){
	/* var table = document.getElementById("tblData1");
    var rows = table.getElementsByTagName("tr"); */
	 var rows = document.getElementById("tblData1").getElementsByTagName("tr").length;
   // alert(rows);
	$("#tblData1").append(
		"<tr class='tableTr1' id='"+rows+1+"'>"+
		/* "<td align='center'><select class='tableTdFarm txtFilterSelect filSelect form-control input-sm' >"+getQuestions1("farmAnimals")+"</select></td>"+ */
		"<td align='center'><select class='tableTd11 txtFilterSelect filSelect form-control input-sm' >"+getQuestions1("inventoryItem1")+"</select></td>"+
		"<td align='center' style='width: 12%;'><input type='text' class='tableTd22 form-control input-sm' name='textVal'onkeypress='return isNumber(event)' maxlength='5' /><div id='msg'></div></td>"+
		<s:if test="currentTenantId!='olivado'">
		"<td align='center' style='width: 17%;'><select class='tableTd6 txtFilterSelect filSelect form-control input-sm ct' id='fodder'>"+getQuestions1("inventoryItem2")+"</select></td>"+
		"<td align='center'><select class='tableTd4 txtFilterSelect filSelect select2 form-control input-sm' >"+getQuestions1("inventoryItem3")+"</select></td>"+
		"<td align='center'  style='width: 12%;'><input type='text' class='tableTd5 form-control input-sm' name='textVal1' maxlength='5'/></td>"+
		"<td align='center'><input type='text' class='tableTd99 form-control input-sm' name='textVal2' maxlength='15'/></td>"+
		</s:if>
		<s:if test="currentTenantId=='chetna'">
		"<td align='center' style='width: 12%;'><input type='text' class='tableTd23 form-control input-sm' name='textVal' maxlength='5' /><div id='msg'></div></td>"+
		"<td align='center' style='width: 12%;'><input type='text' class='tableTd24 form-control input-sm' name='textVal' maxlength='5' /><div id='msg'></div></td>"+
		</s:if>
		//"<td align='center'><select class='tableTd5 txtFilterSelect filSelect select2' >"+getQuestions1("inventoryItem4")+"</select></td>"+
		"<td class='tableTd3'><button id='"+rows+1+"' class='fa fa-trash' onclick='animalDelete1(this);'/></td>"+
		"</tr>");
		
	callSelectize();
}

function getQuestions1(idOption){
	var optionContent='';
	$("#"+idOption+" option").each(function(){
		optionContent+='<option value="'+$(this).val()+'">'+$(this).text()+'</option>';
	});
	return optionContent;
}

function animalDelete1(evt){
	var par = $(evt).parent().parent(); //tr
	par.remove();
} 


function saveAnimalHusbandary(){

	//var farmId = $("#farm").val();
	var columnNamesArray = new Array();
	var columnModelArray = new Array();
	var filterParams = new Array();
	var colCount=0;
	var flag=0;
	var paramObj = {};	
	jQuery(".tableTr1").each(function(){
		
		
		/* jQuery(this).find(".tableTdFarm").each(function(){
			var farmIdVal = jQuery.trim(jQuery(this).find("option:selected").val());
			paramObj['animalInventoryList['+colCount+'].farmId'] =farmIdVal;
			if(farmIdVal == '-1'){
				alert('<s:text name="Please Select the Farm"/>');
				return exit;
			}
			 
		}); */
	    
		jQuery(this).find(".tableTd11").each(function(){
			var columnName1 = jQuery.trim(jQuery(this).find("option:selected").val());
			
			
			paramObj['animalInventoryList['+colCount+'].farmAnimal.code'] =columnName1;
			if(columnName1 == '-1'){
				alert('<s:text name="Please Select the Farm Animal"/>');
				return exit;
			}
			
		
		});
		
		jQuery(this).find(".tableTd22").each(function(){
			var columnModelName1 = jQuery.trim($(this).val());
			paramObj['animalInventoryList['+colCount+'].animalCount'] =columnModelName1;
			if(columnModelName1 == ""){
				alert('<s:text name="Please Enter the Animal Count"/>');
				return exit;
			}
	
			
		});
		
		
	jQuery(this).find(".tableTd6").each(function(){
			
			var selectedFodder =jQuery(this).val();
		//	var columnName2 = jQuery.trim(jQuery(this).find("option:selected").val());
			paramObj['animalInventoryList['+colCount+'].fodder.code'] =selectedFodder;
			 if(selectedFodder == null){
				alert('<s:text name="Please Enter the Fodder"/>');
				return exit;
			} 
			 
		});
		
		
		jQuery(this).find(".tableTd4").each(function(){
			var columnName3 = jQuery.trim(jQuery(this).find("option:selected").val());
			paramObj['animalInventoryList['+colCount+'].animalHousing.code'] =columnName3;
			if(columnName3 == '-1'){
				alert('<s:text name="Please Select the Animal Housing"/>');
				return exit
			}
			 
		});
		
		

		jQuery(this).find(".tableTd5").each(function(){
			var columnModelNameNew = jQuery.trim($(this).val());
			paramObj['animalInventoryList['+colCount+'].revenue'] =columnModelNameNew;
			 if(columnModelNameNew == ''){
				alert('<s:text name="Please Enter the Revenue"/>');
				return exit;
			} 
			 
		});
		
		jQuery(this).find(".tableTd99").each(function(){
			var columnModelNameNew6 = jQuery.trim($(this).val());
			paramObj['animalInventoryList['+colCount+'].breed'] =columnModelNameNew6;
			 if(columnModelNameNew6 == ''){
					alert('<s:text name="Please Enter the Breed Name"/>');
					return exit;
				} 
			
		});
		
		jQuery(this).find(".tableTd23").each(function(){
			var columnModelName7 = jQuery.trim($(this).val());
			paramObj['animalInventoryList['+colCount+'].manureCollected'] =columnModelName7;
			if(columnModelName7 == ""){
				alert('<s:text name="Please Enter the manureCollected"/>');
				return exit;
			}
			
		});
		
		jQuery(this).find(".tableTd24").each(function(){
			var columnModelName8 = jQuery.trim($(this).val());
			paramObj['animalInventoryList['+colCount+'].urineCollected'] =columnModelName8;
			if(columnModelName8 == ""){
				alert('<s:text name="Please Enter the urineCollected"/>');
				return exit;
			}
			
		});
		
		
		/* jQuery(this).find(".tableTd5").each(function(){
			var columnName4 = jQuery.trim(jQuery(this).find("option:selected").val());
			paramObj['animalInventoryList['+colCount+'].revenue.code'] =columnName4;
			if(columnName4 == '-1'){
				flag=1;
			}			
		}); */
		
		
		colCount++;
		
	});
	
	
     var animalInventoryList=[];
    
	for(var i=0;i<colCount;i++){
		 var objfI1=new Object();
		if(!isEmpty(paramObj['animalInventoryList['+i+'].farmAnimal.code']) || !isEmpty(paramObj['animalInventoryList['+i+'].animalCount']) || 
				!isEmpty(paramObj['animalInventoryList['+i+'].fodder.code']) || !isEmpty(paramObj['animalInventoryList['+i+'].animalHousing.code']) ||
				!isEmpty(paramObj['animalInventoryList['+i+'].revenue']) || !isEmpty(paramObj['animalInventoryList['+i+'].manureCollected']) || !isEmpty(paramObj['animalInventoryList['+i+'].urineCollected'])){
			
		objfI1.animalStr=paramObj['animalInventoryList['+i+'].farmAnimal.code'];
		
		objfI1.animalCount=paramObj['animalInventoryList['+i+'].animalCount'];
		
		if(tenant!="olivado"){
		objfI1.fodderStr=paramObj['animalInventoryList['+i+'].fodder.code'].toString();
			//objfI1.fodderStr=jQuery(fodderId).val();
		
		objfI1.housStr=paramObj['animalInventoryList['+i+'].animalHousing.code'];
		
		objfI1.revenueStr=paramObj['animalInventoryList['+i+'].revenue'];
		
		/* objfI1.farmId=paramObj['animalInventoryList['+i+'].farmId']; */
		
		objfI1.breedStr=paramObj['animalInventoryList['+i+'].breed'];
		}
		
		if(tenant=="chetna"){
			objfI1.manureCollected=paramObj['animalInventoryList['+i+'].manureCollected'];
			objfI1.urineCollected=paramObj['animalInventoryList['+i+'].urineCollected'];
		}else{
			objfI1.manureCollected="";
				objfI1.urineCollected="";
		}
		animalInventoryList.push(objfI1);
		
		}
	
		
	}
	
	var postData=new Object();
	var farmerIdId = "<s:property value='farmer.farmerId'/>";
	var farmerId = "<s:property value='farmer.id'/>";
	postData.animalInvenJsonString=JSON.stringify(animalInventoryList);
	//postData.farmId=farmId;
	postData.farmerIdId=farmerIdId;
	postData.farmerId=farmerId;
	if(animalInventoryList.length>0){
		console.log('ANIMAL===='+JSON.stringify(animalInventoryList));
	console.log('here '+JSON.stringify(postData));
	
	//$.post("farmInventory_populateInventry",{paramObj},function(data){
	//});
	
	if(flag==0){
		
	 $.ajax({
         url: 'animalHusbandary_populateAnimalInventry.action',
         type: 'post',
         dataType: 'json',
         success: function (data) {
        	 $('#tableBody1').empty();
        	 addInventry1();
	  refreshAnimal();
	  refreshAnimalTemplateList1();
             
         },
         data: postData
     });
	}else
	{
		alert('<s:text name="entervalues"/>');
		}
	}else{
		alert('<s:text name="entervalues"/>');
	}
}

function refreshAnimal(){
	//$(".tableTd11").val("");
	//$(".tableTd11").append('<option value="' + '-1' + '">' + ''+ '</option>');
	$(".tableTd22").val("");
	$(".tableTd6").val("");
	//$(".tableTd4").val("");
	//$(".tableTd4").append('<option value="' + '-1' + '">' + 'Select'+ '</option>');
	$(".tableTd5").val("");
	$(".tableTd99").val("");
	$(".tableTd23").val("");
	$(".tableTd24").val("");
}

function refreshAnimalTemplateList1(){
	
	 $('#tBodyTemplate1').empty();
	 $('#temp').empty();
	 //var farmId = $("#farm").val();
	
	 $('#temp').append("<option value=''>Select Template</option>");
	 
	 var farmerId = "<s:property value='farmer.id'/>";
	
	 $.getJSON('animalHusbandary_populateAnimalInventryList.action',{id:farmerId},function(jd){
		
		 var temps=jd.data;
		 var bodyContent='';
		 for(var i=0;i<temps.length;i++){
			 var temp=temps[i];
			 bodyContent+='<tr>';
			 bodyContent+='<td class="hide">'+temp.id+'</td>';
			/*  bodyContent+='<td align="center">'+temp.farmName+'</td>'; */
			 bodyContent+='<td align="center">'+temp.item1+'</td>';
			 bodyContent+='<td align="center">'+temp.cout+'</td>';
			 <s:if test="currentTenantId!='olivado'">
			 bodyContent+='<td align="center">'+temp.item2+'</td>';
			 bodyContent+='<td align="center">'+temp.item3+'</td>';
			 //bodyContent+='<td align="center">'+temp.cout+'</td>';
			bodyContent+='<td align="center">'+temp.item4+'</td>';
			bodyContent+='<td align="center">'+temp.item23+'</td>';
			</s:if>
			<s:if test="currentTenantId=='chetna'">
			bodyContent+='<td align="center">'+temp.manureCollected+'</td>';
			bodyContent+='<td align="center">'+temp.urineCollected+'</td>';
			</s:if>
			 bodyContent+='<td align="center"><button class="fa fa-trash" onclick="butAnimalDelete1('+temp.id+');"/></td>';
			 bodyContent+='</tr>';
			 $('#temp').append("<option value='"+temp.id+"'>"+temp.inventoryItem1+temp.inventoryItem2+temp.inventoryItem3+temp.inventoryItem4+"</option>");
		}
		 
		 $('#tBodyTemplate1').html(bodyContent);
		 $('#temp').val('');
	 });
}

function butAnimalDelete1(val){
	var templateId=val;
	if(confirm('<s:text name="confirm.delete"/>')){
	  $.post("animalHusbandary_deleteAnimalInventry.action",{templateId:templateId},
	        	function(data,status){
      		alert('<s:text name="msg.removed"/>');
      		refreshAnimalTemplateList1();
      	
	        	});
      }
}

function isEmpty(val){
	  if(val==null||val==undefined||val.trim()==""){
	   return true;
	  }
	  return false;
	}


</script>
<s:select name="selectedAnimalInventoryItem1" cssClass="hide"
	list="catalogueList1" headerKey="-1" headerValue="%{getText('txt.select')}"
	listKey="key" listValue="value" theme="simple" id="inventoryItem1" />
<s:select name="selectedAnimalInventoryItem2" cssClass="hide"
	list="catalogueList2" headerKey="-1" 
	listKey="key" listValue="value" theme="simple" id="inventoryItem2" />
<s:select name="selectedAnimalInventoryItem3" cssClass="hide"
	list="catalogueList3" headerKey="-1" headerValue="%{getText('txt.select')}"
	listKey="key" listValue="value" theme="simple" id="inventoryItem3" />
<s:select name="selectedAnimalInventoryItem4" cssClass="hide"
	list="catalogueList4" headerKey="-1" headerValue="%{getText('txt.select')}"
	listKey="key" listValue="value" theme="simple" id="inventoryItem4" />

<s:select name="selectedFarm" cssClass="hide" list="farmListByFarmer"
			headerKey="-1" headerValue="%{getText('txt.select')}" 
			theme="simple" id="farmAnimals" />

<input type="BUTTON" id="add" value="<s:text name="add.button"/>"
	onclick="addInventry1()" class="btn btn-sts" />
<table id="tblData1" class="table table-bordered aspect-detail"
	style="margin-top: 2%">

	<thead>

		<tr>
			<%-- <th><s:text name="title.farm"></s:text></th> --%>
			<th><s:text name="animalHusbandary.farmAnimal"></s:text></th>
			<th><s:text name="animalHusbandary.animalCount"/></th>
			<s:if test="currentTenantId!='olivado'">
			<th><s:property value="%{getLocaleProperty('animalHusbandary.animalCount1')}" /></th>
			<th><s:property value="%{getLocaleProperty('animalHusbandary.animalHousing')}" /></th>
			<th><s:property value="%{getLocaleProperty('animalHusbandary.animrev')}" /></th>
			<%-- <th><s:text name="animalHusbandary.animrev"></s:text></th> --%>
			<th><s:text name="animalHusbandary.breed"></s:text></th>
			</s:if>
			<s:if test="currentTenantId=='chetna'">
			<th><s:text name="animalHusbandary.manureCollected"/></th>
			<th><s:text name="animalHusbandary.urineCollected"/></th>
			</s:if>
			<th>Action</th>
		</tr>
	</thead>
	<tbody id="tableBody1">
	</tbody>
</table>

<div class="yui-skin-sam">
	<span id="button1" class=""><span class="first-child">
			<button type="button" onclick="saveAnimalHusbandary();"
				class="save-btn btn btn-success">
				<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
				</font>
			</button>
	</span></span>

</div>
<%-- <table>
	<tr class="hide">
		<td width="35%"><s:text name="animalInventory.otherInventoryItem" /></td>
		<td width="65%"><s:textfield
				name="animalInventory.otherInventoryItem" value="%{farm.id}"
				id="farm" /><sup style="color: red;">*</sup></td>
	</tr>
</table> --%>

<table id="tableTemplate1" class="table table-bordered aspect-detail"
	style="margin-top: 2%">
	<thead>
		<tr class="border-less">
			<th class="hide">Id</th>
			<%-- <th><s:text name="title.farm"></s:text></th> --%>
			<th><s:text name="animalHusbandary.farmAnimal"></s:text></th>
			<th><s:text name="animalHusbandary.animalCount"/></th>
			<s:if test="currentTenantId!='olivado'">
			<th><s:text name="animalHusbandary.animalCount1"/></th>
			<th><s:text name="animalHusbandary.animalHousing"/></th>
			<%-- <th><s:text name="animalHusbandary.animrev"></s:text></th> --%>
			<th><s:property value="%{getLocaleProperty('animalHusbandary.animrev')}" /></th>
			<th><s:text name="animalHusbandary.breed"></s:text></th>
			</s:if>
			<s:if test="currentTenantId=='chetna'">
			<th><s:text name="animalHusbandary.manureCollected"/></th>
			<th><s:text name="animalHusbandary.urineCollected"/></th>
			</s:if>
			<th><s:text name="animalHusbandary.delete"></s:text></th>
		</tr>
	</thead> 
	<tbody id="tBodyTemplate1">
	</tbody>
</table>




