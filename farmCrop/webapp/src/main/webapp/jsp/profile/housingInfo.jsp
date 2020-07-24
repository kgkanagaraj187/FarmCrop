<%@ taglib prefix="s" uri="/struts-tags"%>
<div style="color: red;">
<span id="validateError" ></span>
</div>
	<s:hidden id="farm.id" name="farmId"/>
		<s:hidden id="calfProdData" name="calfProdData" />
		<s:hidden id="removeHousingIds" name="removeHousingIds"/>
		<table class="table table-bordered aspect-detail" style="width:100% ">
			<tr>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="noCowShad" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="housingInfo.noCowShad" theme="simple" maxlength="35" onkeypress="return isNumber(event)"
								id="noCowShad" cssClass="form-control input-sm checktexts" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="housingType" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:select name="housingInfo.housingShadType" theme="simple" list="housingShadTypeList" listKey="key" listValue="value"
								headerKey="" headerValue="%{getText('txt.select')}" id="housingShadType" cssClass="form-control input-sm checktexts" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="spacePerCow" />
						</div>
					</td>
					
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="housingInfo.spacePerCow" theme="simple" maxlength="35"
								id="spacePerCow" cssClass="form-control input-sm checktexts" />
						</div>
					</td>
					<td colspan="8">
						<button type="button" class="btn btn-primary" onclick="addHousing()">Add</button>
					</td>

				</tr>
				
				<tr>
					<td style="width: 100%" colspan="8">
						<table class="table table-bordered aspect-detail">
							<thead>
								<tr class="odd">
									<th><s:text name="noCowShad" /></th>
									<th><s:text name="housingType" /></th>
									<th><s:text name="spacePerCow" /></th>
									<th width="14%" colspan="3"><s:text name="action" /></th>
								</tr>
							</thead>
							<tbody id="housingInfoTbl">
									<s:if test="housingList.size()>0">
										<s:iterator value="housingList" status="status" var="bean">
											<tr id='<s:property value="%{#status.count}" />'>
											
												<td align="center"><s:property value="noCowShad" /></td>
												<td align="center"><s:property value="housingShadType" /></td>
												<td align="center"><s:property value="spacePerCow" /></td>
												<td align="center" style="display: none">
												<td style="text-align:center;">
													<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="removeRow('<s:property value="%{#status.count}" />',<s:property value="id" />)"><i class="fa fa-trash" aria-hidden="true"></i></button>
												</td>	
											</tr>
										</s:iterator>
									</s:if>
							
							</tbody>
						</table>
					</td>
				</tr>


		</table>
		<div class="yui-skin-sam">
				<span id="button1" class=""> <span class="first-child">
						<button type="button" onclick="onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b> <s:text name="save.button" />
							</b>
							</font>
						</button>
				</span>
				</span>
			
			<span id="cancel" class=""> <span class="first-child">
					<button type="button" onclick="onCancel();"
						class="cancel-btn btn btn-sts">
						<b> <FONT color="#FFFFFF"> <s:text name="cancel.button" />
						</FONT>
						</b>
					</button>
			</span>
			</span>
		</div>
		
		<script type="text/javascript">
var calfSno=0;
var checkAddRow=0;
function addHousing()
{
	var noCowShad=$("#noCowShad").val();
	 var housingShadType=$("#housingShadType option:selected").text();
	  var spacePerCow=$("#spacePerCow").val();
	if(validateData())
	{
		var tbody="";
	  	tbody+=td(noCowShad);
	  	tbody+=td(housingShadType);
	  	tbody+=td(spacePerCow);
	  
	  	tbody+='<td style="text-align:center;"><button type="button" class="btn btn-sm btn-danger" onclick="removeRow('+calfSno+')" title="<s:text name="Delete" />"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'

	  	$('#housingInfoTbl').append(tr(tbody,calfSno));
		  resetProducts();
		  checkAddRow=1;
	}
}


function resetProducts()
{
	 
	 $("#noCowShad").val("");
     	$("#housingShadType").val("");
      $("#spacePerCow").val("");
     	
}

function td(val){
	  return "<td>"+val+"</td>";
}

function tr(val,id){
	  return "<tr id='"+id+"'>"+val+"</tr>";
}



function getFormattedValue(val){
	   if(val==null||val==undefined||val.trim()==""){
	    return " ";
	   }
	   return val;
	 }


function removeRow(indx,removeId)
{
		 var inde=indx-1;
		 var removeIds="";
		 removeIds+=removeId
	  $("#housingInfoTbl tr:eq("+inde+")").remove();
	  document.getElementById("removeHousingIds").value=removeIds;
 }
 
function onSubmit()
{
		var calfProdData="";
		var calfDatas="";
		var tableBody = jQuery("#housingInfoTbl");
		var rowCount = $('#housingInfoTbl tr').length;
		var farmId=$("#farmId").val();
		var removeHousingIds=$("#removeHousingIds").val();
		if(rowCount>0)
		{
			if(checkAddRow==1)
			{
				
				 tableBody.find('tr').each(function (el) {
				var $tds = $(this).find('td'),
				noCowShad = $tds.eq(0).text(),
				housingShadType = $tds.eq(1).text(),
				spacePerCow = $tds.eq(2).text();
				calfDatas+=getFormattedValue(noCowShad)+"###"+getFormattedValue(housingShadType)+"###"+getFormattedValue(spacePerCow)+"###"+farmId+"###"+getFormattedValue(removeHousingIds)+"@@@";
				});
			    
		}
			else
			{
				
				calfDatas+=removeHousingIds+"###";
			}
			 var dataa={
				calfProdData:calfDatas,
			 }
			 
			

			 
			 $.ajax({
		         url: 'housingInfo_populateHousing.action',
		         type: 'post',
		         dataType: 'json',
		         data: dataa,
		         success: function (data) {
		        	 $('#housingInfoTbl').empty();
		        	 document.getElementById("validateError").innerHTML="";
		         },
		     });
			
		}
		else
			{
				validateData();
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


function validateData()
{
	var hit=true;
	 var noCowShad=$("#noCowShad").val();
	  var housingShadType=$("#housingShadType").val();
	  var spacePerCow=$("#spacePerCow").val();
	  if(noCowShad==null || noCowShad=="")
	  {
		  document.getElementById("validateError").innerHTML='<s:text name="empty.noCowShad"/>';
		  hit="false";
		  return false;
		  
		  
	 }
	 else if(housingShadType==null || housingShadType=="")
	  {
		  document.getElementById("validateError").innerHTML='<s:text name="empty.housingShadType"/>';
		  hit="false";
		  return false;
		  
	 }
	 else if(spacePerCow==null || spacePerCow=="")
	  {
		  document.getElementById("validateError").innerHTML='<s:text name="empty.spacePerCow"/>';
		  hit="false";
		  return false;
		  
	 }
	  
	  return hit;
}
		
</script>