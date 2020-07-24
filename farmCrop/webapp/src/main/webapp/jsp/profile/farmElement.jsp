<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
jQuery(document).ready(function(){
	refreshTemplateList();
	refreshTemplateList1();
	addElement();
});

function addElement(){
	var table = document.getElementById("tblData3");
    var rows = table.getElementsByTagName("tr");
    $("#tblData3 tbody").append(
    		"<tr class='tableTr1'id='"+rows.length+1+"'>"+
    		"<td align='center'><select id='dp1' class='tableTd10 txtFilterSelect filSelect select2 form-control input-sm' >"+getQuestions2("inventoryItem6")+"</select></td>"+
    		"<td align='center'><input type='text' class='tableTd11 form-control input-sm' name='textVal' maxLength='4' onkeypress='return isNumber(event)'/></td>"+
    		"<td align='center'><select id='dp2' class='tableTd12 txtFilterSelect filSelect select2 form-control input-sm' >"+getQuestions2("inventoryItem5")+"</select></td>"+
    		"<td align='center'><input type='text' class='tableTd13 form-control input-sm' name='textVal' maxLength='4' onkeypress='return isNumber(event)'/></td>"+
    		"<td align='center' class='tableTd3'><a id='"+rows.length+1+"' class='fa fa-trash' onclick='Delete(this);' href='#'/></td>"+
    		"</tr>");
	
}

function getQuestions2(idOption){
	var optionContent='';
	$("#"+idOption+" option").each(function(){
		optionContent+='<option value="'+$(this).val()+'">'+$(this).text()+'</option>';
	});
	return optionContent;
}

function Delete(evt){
	var par = $(evt).parent().parent(); //tr
	par.remove();
}

function butDelete(val){
	var templateId=val;
	if(confirm('<s:text name="confirm.delete"/>')){
	  $.post("farmTab_deleteMachinaryItem.action",{templateId:templateId},
	        	function(data,status){
      		alert('<s:text name="msg.removed"/>');
      		refreshTemplateList();
	        	});
      }
}
function butDelete1(val){
	var templateId=val;
	if(confirm('<s:text name="confirm.delete"/>')){
	  $.post("farmTab_deletePolyItem.action",{templateId:templateId},
	        	function(data,status){
      		alert('<s:text name="msg.removed"/>');
      		refreshTemplateList1();
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

function save1(){
	var farmId = $("#farm").val();
	var columnNamesArray = new Array();
	var columnModelArray = new Array();
	var filterParams = new Array();
	var count1=0;
	var paramObj = {};	
	var hit=0;
	
	jQuery(".tableTr1").each(function(){
		jQuery(this).find(".tableTd10").each(function(){
			var columnName1 = jQuery.trim(jQuery(this).find("option:selected").val());
			paramObj['farmElementList['+count1+'].catalogueIdList[0]'] =columnName1;
			if(columnName1 == '-1'){
				hit=1;
			}
		});

		jQuery(this).find(".tableTd11").each(function(){
			var columnModelName1 = jQuery.trim($(this).val());
				paramObj['farmElementList['+count1+'].countList[0]'] =columnModelName1;
				if(columnModelName1 == ''){
					hit=1;
				}
		});
		jQuery(this).find(".tableTd12").each(function(){
			var columnName2 = jQuery.trim(jQuery(this).find("option:selected").val());
			paramObj['farmElementList['+count1+'].catalogueIdList[1]'] =columnName2;
			if(columnName2 == '-1'){
				hit=1;
			}
			
		});
		
		jQuery(this).find(".tableTd13").each(function(){
			var columnName3 = jQuery.trim($(this).val());
				paramObj['farmElementList['+count1+'].countList[1]'] =columnName3;
				if(columnName3 == ''){
					hit=1;
				}
		});
		count1++;
	});
	//console.log("here : "+JSON.stringify(paramObj));
    var farmElementList=[];
	for(var k=1;k<count1;k++){
		var objfI2=new Object();
		if(paramObj['farmElementList['+k+'].catalogueIdList[0]']!=undefined){
			objfI2.machStr=paramObj['farmElementList['+k+'].catalogueIdList[0]'];
		}else{
			objfI2.machStr = "";
		}
		if(paramObj['farmElementList['+k+'].countList[0]']!=undefined){
			objfI2.machCount=paramObj['farmElementList['+k+'].countList[0]'];
		}else{
			objfI2.machCount= "";
		}
		if(paramObj['farmElementList['+k+'].catalogueIdList[1]']!=undefined){
			objfI2.houseStr=paramObj['farmElementList['+k+'].catalogueIdList[1]'];
		}else{
			objfI2.houseStr= "";
		}
		if(paramObj['farmElementList['+k+'].countList[1]']!=undefined){
			objfI2.houseCnt=paramObj['farmElementList['+k+'].countList[1]'];
		}else{
			objfI2.houseCnt= "";
		}
		
			farmElementList.push(objfI2);
	}
	
	var postData=new Object();
	postData.farmElementJsonString=JSON.stringify(farmElementList);
	postData.farmId=farmId;
	if(farmElementList.length>0){
	console.log('here '+JSON.stringify(postData));
	if(hit ==0){
	 $.ajax({
         url: 'farmTab_populateInventry.action',
         type: 'post',
         dataType: 'json',
         success: function (data) {
		 $('#tableBody').empty();
		 refresh1();
		 refreshTemplateList();
		 refreshTemplateList1();
		 
         },
         data: postData
     });
	}else{
		alert("Please Enter Values");
	}
	}else{
		alert("Please Enter Values");
		}
}

function refreshTemplateList(){
	$('#tableTemplate2').hide();
	 $('#tBodyTemplate2').empty();
	 $('#template').empty();
	 var farmId = $("#farm").val();
	 $('#template').append("<option value=''>Select Template</option>");
	
	 $.getJSON('farmTab_populateInventryList.action',{farmId:farmId },function(jd){
		 var templates=jd.data;
		 var bodyContent='';
		 for(var i=0;i<templates.length;i++){
			 var template=templates[i];
			 bodyContent+='<tr>';
			 bodyContent+='<td class="hide">'+template.id+'</td>';
			 bodyContent+='<td align="center">'+template.item+'</td>';
			 bodyContent+='<td align="center">'+template.ct+'</td>';
			 bodyContent+='<td align="center"><a href="#" class="fa fa-trash" onclick="butDelete('+template.id+');"/></td>';
			 bodyContent+='</tr>';
			 $('#template').append("<option value='"+template.id+"'>"+template.item+"</option>");
				$('#tableTemplate2').show();
		 }
		
		 $('#tBodyTemplate2').html(bodyContent);
		 $('#template').val('');
	 });
}

function refreshTemplateList1(){
	$('#tableTemplate3').hide();
	 $('#tBodyTemplate3').empty();
	 $('#template').empty();
	 var farmId = $("#farm").val();
	 $('#template').append("<option value=''>Select Template</option>");
	
	 $.getJSON('farmTab_populatePolyList.action',{farmId:farmId },function(jd){
		 var templates=jd.data;
		 var bodyContent='';
		 for(var i=0;i<templates.length;i++){
			 var template=templates[i];
			 bodyContent+='<tr>';
			 bodyContent+='<td class="hide">'+template.id1+'</td>';
			 bodyContent+='<td align="center">'+template.item1+'</td>';
			 bodyContent+='<td align="center">'+template.ct1+'</td>';
			 bodyContent+='<td align="center"><a href="#" class="fa fa-trash" onclick="butDelete1('+template.id1+');"/></td>';
			 bodyContent+='</tr>';
			 $('#template').append("<option value='"+template.id1+"'>"+template.item1+"</option>");
				$('#tableTemplate3').show();
		 }
		
		 $('#tBodyTemplate3').html(bodyContent);
		 $('#template').val('');
	 });
} 

function refresh1(){
	document.getElementById("dp1").value = "-1";
	$(".tableTd11").val("");
	document.getElementById("dp2").value = "-1";
	$(".tableTd13").val("");
}


</script>
<s:hidden name="farmId" value="%{farm.id}" />
<s:hidden name="farmName" value="%{farm.farmName}" />
<s:hidden name="farmerId" value="%{farm.farmer.farmerId}" />
<s:hidden name="farmerName" value="%{farm.farmer.name}" />

<s:select name="selectedMachinaryItem" cssClass="hide"
	list="machinaryList" headerKey="-1" headerValue="%{getText('txt.select')}" listKey="code"
	listValue="name" theme="simple" id="inventoryItem6" />
<s:select name="selectedPolyItem" cssClass="hide" list="polyList"
	headerKey="-1" headerValue="%{getText('txt.select')}" listKey="code" listValue="name"
	theme="simple" id="inventoryItem5" />
<input type="BUTTON" id="add" value="<s:text name="add.button"/>"
	onclick="addElement()" class="btn btn-sts" />
<table id="tblData3" class="table table-bordered aspect-detail"
	style="margin-top: 2%">

	<thead>

		<tr>
			<th><s:text name="farmelement.machitem"></s:text></th>
			<th> <s:text name="farmelement.machcpont"></s:text></th>
			<th><s:text name="farmelement.machihouse"></s:text></th>
			<th><s:text name="farmelement.machpoly"></s:text></th>
			<th><s:text name="farmelement.machdel"></s:text></th>
		</tr>
	</thead>
	<tbody id="tableBody">
	</tbody>
</table>


<table class="table table-bordered aspect-detail">
	<tr class="hide">
		<td width="35%"><s:text
				name="farmInventory.otsasaherInventoryItem" /></td>
		<td width="65%"><s:textfield
				name="farmInventory.otherInventoryItem" value="%{farm.id}" id="farm" /><sup
			style="color: red;">*</sup></td>
	</tr>
</table>
<div class="yui-skin-sam">
	<span id="button1" class=""><span class="first-child">
			<button type="button" onclick="save1();"
				class="save-btn btn btn-success">
				<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
				</font>
			</button>
	</span></span>
</div>

<table id="tableTemplate2" class="table table-bordered aspect-detail"
	style="margin-top: 2%">
	<thead id="tableHD">
		<tr class="border-less">
			<th class="hide">Id</th>
			<th><s:text name="farmelement.machitem"></s:text></th>
			<th> <s:text name="farmelement.machcpont"></s:text></th>
			<th><s:text name="farmelement.machdel"></s:text></th>
		</tr>
	</thead>
	<tbody id="tBodyTemplate2">
	</tbody>
</table>

<table id="tableTemplate3" class="table table-bordered aspect-detail"
	style="margin-top: 2%">
	<thead id="tableHD">
		<tr class="border-less">
			<th class="hide">Id</th>
			<th><s:text name="farmelement.machihouse"></s:text></th>
			<th><s:text name="farmelement.machpoly"></s:text></th>
			<th><s:text name="farmelement.machdel"></s:text></th>
		</tr>
	</thead>
	<tbody id="tBodyTemplate3">
	</tbody>
</table>




