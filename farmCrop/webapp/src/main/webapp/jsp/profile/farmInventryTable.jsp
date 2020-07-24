<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
jQuery(document).ready(function(){
	$('#tableTemplate').hide();
	refreshInventryTemplateList();
	addInventry();
	$("#otherInventoryItemRow").hide();
});
function addInventry(){
	var table = document.getElementById("tblData");
    var rows = table.getElementsByTagName("tr");
	$("#tblData tbody").append(
		"<tr class='tableTr'id='"+rows.length+1+"'>"+
		//"<td><div class='row'><div class='col-md-12'><select class='tableTd4 txtFilterSelect filSelect form-control input-sm' >"+getQuestions("farms")+"</select></div></div></td>"+
		"<td><div class='row'><div class='col-md-10'><select class='tableTd1 txtFilterSelect filSelect form-control input-sm' >"+getQuestions("inventoryItem")+"</select></div><div class='col-md-2' style='padding:0;'><button type='button' class='btn btn-sts' onclick='addOtherFarmEquipment();'>+</button></div></div></td>"+
		"<td align='center'><input type='text' class='tableTd2 form-control input-sm' name='textVal' maxLength='4' onkeypress='return isNumber(event)'/></td>"+
		"<td class='tableTd3'><a id='"+rows.length+1+"' class='fa fa-trash' onclick='DeleteInventry(this);' href='#'/></td>"+
		"</tr>");
	//addFramEquipement();
}

function addFramEquipement(){
	var otherInventoryItem=$("#otherInventoryItem").val();
	 document.getElementById("inventoryItem").length = 0;
     addOption(document.getElementById("inventoryItem"), "Select", "");
	$.post("farmInventory_populateFramCatalogue",{otherInventoryItem:otherInventoryItem},function(result){
		var data=jQuery.parseJSON(result);
	
		if(data[0].error!='' && data[0].error!=undefined){	
			
				alert(data[0].error);
	      
		}else{
		$(".tableTd1").html("");
		$(".tableTd1").append('<option value="' + '-1' + '">' + 'Select'+ '</option>');
		$.each(data, function(k, v) {
			$(".tableTd1").append('<option value="' + v.code + '">' + v.name + '</option>');
			$("#inventoryItem").append('<option value="' + v.code + '">' + v.name + '</option>');
        });
		$("#otherInventoryItemRow").hide();
	}
		
	});


	
}
function addOtherFarmEquipment(){
	document.getElementById("otherInventoryItem").value="";
	$("#otherInventoryItemRow").show();
}

function getQuestions(idOption){
	var optionContent='';
	$("#"+idOption+" option").each(function(){
		optionContent+='<option value="'+$(this).val()+'">'+$(this).text()+'</option>';
	});
	return optionContent;
}

function DeleteInventry(evt){
	var par = $(evt).parent().parent(); //tr
	par.remove();
} 

function saveFarmInventry(){
	//var farmId = $("#farm").val();
	
	var columnNamesArray = new Array();
	var columnModelArray = new Array();
	var filterParams = new Array();
	var colCount=0;
	var paramObj = {};	
	var hit=0;
	
	jQuery(".tableTr").each(function(){
		
		/* jQuery(this).find(".tableTd4").each(function(){
			var farmIdVal = jQuery.trim(jQuery(this).find("option:selected").val());	
				paramObj['farmInventoryList['+colCount+'].farmId'] =farmIdVal;
				if(farmIdVal == '-1'){
					alert('<s:text name="Please Select the Farm"/>');
					return exit;
				}
			
		}); */
		
		jQuery(this).find(".tableTd1").each(function(){
			var columnName = jQuery.trim(jQuery(this).find("option:selected").val());
				paramObj['farmInventoryList['+colCount+'].invItem'] =columnName;
				if(columnName == '-1'){
					alert('<s:text name="Please Select the Farm Equipment Item"/>');
					return exit;
				}
			
		});
		
		

		jQuery(this).find(".tableTd2").each(function(){
			var columnModelName = jQuery.trim($(this).val());
				paramObj['farmInventoryList['+colCount+'].itemCount'] =columnModelName;
				if(columnModelName == ''){
					alert('<s:text name="Please Enter the Farm Equipment Item Count"/>');		
					return exit;
				}
		});
		colCount++;
	});
     var farmInventoryList=[];
	for(var i=0;i<colCount;i++){
		var objfI=new Object();
		if(paramObj['farmInventoryList['+i+'].invItem']!=undefined){
			objfI.invItem=paramObj['farmInventoryList['+i+'].invItem'];
		}else{
			objfI.invItem = "";
		}
		if(paramObj['farmInventoryList['+i+'].itemCount']!=undefined){
			objfI.itemCount=paramObj['farmInventoryList['+i+'].itemCount'];
		}else{
			objfI.itemCount="";
		}
		
		/* if(paramObj['farmInventoryList['+i+'].farmId']!=undefined){
			objfI.farmId=paramObj['farmInventoryList['+i+'].farmId'];
		}else{
			objfI.farmId="";
		} */
		
		if(objfI.invItem!="" && objfI.itemCount!="" ){
			farmInventoryList.push(objfI);
		}
	}
	
	var postData=new Object();
	var farmerIdId = "<s:property value='farmer.farmerId'/>";
	var farmerId = "<s:property value='farmer.id'/>";
	postData.farmInvenJsonString=JSON.stringify(farmInventoryList);
	//postData.farmId=farmId;
	postData.farmerIdId=farmerIdId;
	postData.farmerId=farmerId;
	if(farmInventoryList.length>0){
	console.log('here '+JSON.stringify(postData));
	if(hit ==0){
	  $.ajax({
         url: 'farmInventory_populateInventry.action',
         type: 'post',
         dataType: 'json',
         success: function (data) {
		 $('#tableBody').empty();
		 refreshInventryTemplateList();
		 addInventry();
         },
         data: postData
     }); 
	}else{
		alert('<s:text name="entervalues"/>');
	}
	}else{
		alert('<s:text name="entervalues"/>');
		}
	 
}

function refreshInventryTemplateList(){
	$('#tableTemplate').hide();
	 $('#tBodyTemplate').empty();
	 $('#template').empty();
	// var farmId = $("#farm").val();
	 $('#template').append("<option value=''>Select Template</option>");
	 var farmerId = "<s:property value='farmer.id'/>";
	 $.getJSON('farmInventory_populateInventryList.action',{id:farmerId},function(jd){
		 var templates=jd.data;
		 var bodyContent='';
		 for(var i=0;i<templates.length;i++){
			 var template=templates[i];
			 bodyContent+='<tr>';
			 bodyContent+='<td class="hide">'+template.id+'</td>';
			 //bodyContent+='<td align="center">'+template.farmName+'</td>';
			 bodyContent+='<td align="center">'+template.item+'</td>';
			 bodyContent+='<td align="center">'+template.ct+'</td>';
			 bodyContent+='<td align="center"><a href="#" class="fa fa-trash" onclick="butInventryDelete('+template.id+');"/></td>';
			 bodyContent+='</tr>';
			 $('#template').append("<option value='"+template.id+"'>"+template.item+"</option>");
				$('#tableTemplate').show();
		 }
		
		 $('#tBodyTemplate').html(bodyContent);
		 $('#template').val('');
	 });
}

function butInventryDelete(val){
	var templateId=val;
	if(confirm('<s:text name="confirm.delete"/>')){
	  $.post("farmInventory_deleteInventry.action",{templateId:templateId},
	        	function(data,status){
      		alert('<s:text name="msg.removed"/>');
      		refreshInventryTemplateList();
      	
	        	});
      }
}


function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}


</script>
 <%-- <s:hidden name="farmId" value="%{farm.id}" />
	<s:hidden name="farmName" value="%{farm.farmName}" /> --%>
	<s:hidden name="farmerIdId" value="%{farmer.farmerId}"/>
	<s:hidden name="farmerId" />

<s:select name="selectedFarmInventoryItem" cssClass="hide" list="catalogueList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="inventoryItem" />
			
<s:select name="selectedFarm" cssClass="hide" list="farmListByFarmer"
			headerKey="-1" headerValue="%{getText('txt.select')}" 
			theme="simple" id="farms" />
			
			<input type="BUTTON" id="add" value="<s:text name="add.button"/>"
		onclick="addInventry()" class="btn btn-sts"/>
	<table id="tblData" class="table table-bordered aspect-detail" style="margin-top:2%">
	
		<thead>
		
			<tr>
				<%-- <td><s:text name="title.farm" /></td> --%>
				<td><s:text name="farmInventory.inventoryItem"></s:text></td>
				<td><s:text name="farmInventory.itemCount"></s:text></td>
				<td><s:text name="deleteAction"></s:text></td>
			</tr>
		</thead>
		<tbody id="tableBody">
		</tbody>
	</table>
	
	
	<table class="table table-bordered aspect-detail">
	<tr class="hide">
		<td width="35%"><s:text name="farmInventory.otsasaherInventoryItem" /></td>
		<td width="65%"><s:textfield name="farmInventory.otherInventoryItem" value="%{farm.id}" id="farm"/><sup style="color: red;">*</sup></td>
	</tr>
	<tr id="otherInventoryItemRow">
		<td width="33"><s:text name="farmInventory.otherFarmEquipemnt" /></td>
		<td width="19"><s:textfield name="farmInventory.inventoryItem.name"  id="otherInventoryItem"/></td>
		<td  width="35"><button type="button" class="save-btn btn btn-success" onclick="addFramEquipement()" ><s:text name="save.button" /></button></td>
	</tr>
	</table>
		<div class="yui-skin-sam">
		<span id="button1" class=""><span class="first-child">
		<button type="button" onclick="saveFarmInventry();" class="save-btn btn btn-success">
		<font color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>
		</span></span>

		</div>
		
<table  id="tableTemplate" class="table table-bordered aspect-detail" style="margin-top:2%">
	<thead id="tableHD">
		<tr class="border-less">
			<th class="hide">Id</th>
				<%-- <th><s:text name="title.farm"></s:text></th> --%>
				<th><s:text name="farmInventory.inventoryItem"></s:text></th>
				<th><s:text name="farmInventory.itemCount"></s:text></th>
				<th><s:text name="deleteAction"></s:text></th>
		</tr>
	</thead>
	<tbody id="tBodyTemplate">
	</tbody>
</table>




