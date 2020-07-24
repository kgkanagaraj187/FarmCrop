<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<style>
.accrodianTxt {
	color: black;
	font-style: bold;
}

.accrodianTxt:after {
	/* symbol for "opening" panels */
	font-family: 'Glyphicons Halflings';
	/* essential for enabling glyphicon */
	content: "\2212"; /* adjust as needed, taken from bootstrap.css */
	float: right; /* adjust as needed */
	color: black; /* adjust as needed */
}

.collapsed:after {
	/* symbol for "collapsed" panels */
	content: "\2b"; /* adjust as needed, taken from bootstrap.css */
}

.aspect-detail TD:nth-child(2) {
	font-weight: normal;
}
</style>
<script>
   var hit=true;
   var tenant='<s:property value="getCurrentTenantId()"/>';
   $(document).ready(function ()
	 {
      });
   
   function isAlpahaNumeric(e){
   	var regex = new RegExp("^[a-zA-Z0-9\b]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
        return true;
    }
   
    e.preventDefault();
    return false;
   }
   
   
   
   
   
   
   
   
</script>
<body>
	<div class="error">
		<s:actionerror />
		<s:fielderror />
		<div style="color: #ff0000">
		<sup>*</sup>
		<s:text name="reqd.field" />
	</div> <span id="validateError"></span>
	</div>
	<s:form name="form" cssClass="fillform" action="cow_%{command}"
		role="form" method="post" enctype="multipart/form-data" >
		<s:hidden key="command" id="command" />
		<s:hidden id="calfProdData" name="calfProdData" />
		<s:hidden id="researchStationId" name="researchStationId"/>
		<s:hidden id="farmId" name="farmId"/>
		<s:hidden id="cow.id" name="cow.id"/>
		<s:hidden name="tabIndexz" value="%{tabIndexz}" />
		<s:hidden name="tabIndex" value="%{tabIndexz}" />
		<s:hidden id="hiddenCalfId" name="hiddenCalfId" />
		
		<table id="info" class="table table-bordered aspect-detail">
			<tr>
				<th colspan="8"><a data-toggle="collapse"
					data-parent="#accordion" href="#cowAccordian" class="accrodianTxt">
						<s:text name="info.cow" />
				</a></th>
			</tr>
			<tbody id="farmerAccordian" class="panel-collapse collapse in">
				<tr>
					<td style="width: 17%;"><s:text name="cow.cowId" /> <sup
						style="color: red;">*</sup>
						</div></td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="cow.cowId" theme="simple" id="cowId"
								maxlength="35" cssClass="form-control input-sm checktexts" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="cow.lactationNo" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="cow.lactationNo" id="lactationNo"
								cssClass="form-control input-sm" theme="simple" maxlength="20" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="cow.tagNo" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="cow.tagNo" theme="simple" maxlength="20"
								id="tagNo" cssClass="form-control input-sm checktexts" />
						</div>
					</td>
					
				</tr>
				<tr>
				
				<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="cow.dob" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">

							<s:textfield name="dateOfBirth" id="dateOfBirth" readonly="true"
								theme="simple" data-date-format="mm/dd/yyyy"
								data-date-viewmode="years"
								cssClass="date-picker form-control input-sm" />

						</div>
				
				
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="cow.sireId" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:select name="cow.sireId" id="sireId" listKey="key" listValue="value" list="sireIdList"
								 headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm select2" theme="simple"  />
						</div>
					</td>
					
					
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="cow.photo" />
						</div></td>
						
					<td style="width: 17%;" colspan="2"><div class="col-xs-16">
							<s:file name="cow.cowImage" id="cowImage"
						onchange="checkImgHeightAndWidth(this)" />
						<s:if test='cowImageByteString!=null && cowImageByteString!=" "'>
						<img width="50" height="50" border="0" id="image"
						src="data:image/png;base64,<s:property value="cowImageByteString"/>">
					</s:if>
						</div></td>
				
				</tr>
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="latitude" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:textfield name="cow.latitude" maxlength="20"
								cssClass="form-control input-sm" />
						</div></td>

					
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="longitude" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:textfield name="longitude" maxlength="20"
								cssClass="form-control input-sm" />
						</div></td>				
					<td>
						<div class="col-xs-16">
							<s:text name="cow.damId" />
						</div>
					</td>
					<td>
						<div class="inputCon">
							<s:select name="cow.damId" id="damId" listKey="key" listValue="value" list="damIdList"
								 headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm select2" theme="simple" />
						</div>
					</td>
					
					
				</tr>
				<tr>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="cow.genoType" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:select name="cow.genoType" id="genoType" listKey="key" listValue="value" list="genoTypeList"
							 headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" theme="simple"  />
						</div>
					</td>
				
				
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="cow.milkFirstHundPerDay" />
						</div>
					</td>
				
				<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="cow.milkFirstHundPerDay" id="milkPerDay"
								cssClass="form-control input-sm" theme="simple" maxlength="12"
								onkeyup="calculatePlantHect();"
								onkeypress="return isDecimal(event)" />
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<table id="info" class="table table-bordered aspect-detail fixedTable">
			<tr>
				<th colspan="8"><a data-toggle="collapse"
					data-parent="#accordion" href="#calfAccordian"
					class="accrodianTxt collapsed"> <s:text name="info.calf" />
				</a></th>
			</tr>
			<tbody id="calfAccordian" class="panel-collapse collapse">
				<tr>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.calfId" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="calf.calfId" theme="simple" maxlength="35"
								id="calfId" cssClass="form-control input-sm checktexts" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.bullId" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:select name="calf.bullId" theme="simple" maxlength="35" listKey="key" listValue="value"  list="bullIdList"
							 headerKey="" headerValue="%{getText('txt.select')}"
								id="bullId" cssClass="form-control input-sm checktexts " />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.serviceDate" />
						</div>
					</td>

					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="serviceDate" id="serviceDate" readonly="true"
								data-date-format="mm/dd/yyyy"
								cssClass="date-picker form-control input-sm" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.lastDateCalving" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">

							<s:textfield name="lastDateCalving" id="lastDateCalving"
								readonly="true" theme="simple" data-date-format="mm/dd/yyyy"
								data-date-viewmode="years"
								cssClass="date-picker form-control input-sm" size="20" />
						</div>
					</td>
				</tr>
				<tr>

					<td style="width: 17%;"><div>
							<s:text name="calf.gender" />
						</div></td>
					<td style="width: 17%;"><div>
							<s:radio list="genderType" listKey="key" listValue="value"  headerKey="" headerValue="%{getText('txt.select')}"
								id="gender" onchange="getGenderVal(this.value)"
								name="calf.gender" theme="simple" />
						</div></td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.birthWeight" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="calf.birthWeight" id="birthWeight"
								theme="simple" maxlength="35" cssClass="form-control input-sm" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.calvingIntvalDays" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="calf.calvingIntvalDays" id="calvingIntvalDays"
								 headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" theme="simple" maxlength="12" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="calf.deliveryProcess" />
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:select name="calf.deliveryProcess" id="deliveryProcess" listKey="key" listValue="value" list="deliveryProcessList"
								 headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm " theme="simple"  />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="8">
						<button type="button" class="btn btn-primary" onclick="addCalf()">Add</button>
					</td>
				</tr>
				
				
				
				
				

				<tr>
					<td style="width: 100%" colspan="8">
						<table class="table table-bordered aspect-detail">
							<thead>
								<tr class="odd">
									<th><s:text name="calf.calfId" /></th>
									<th><s:text name="calf.bullId" /></th>
									<th><s:text name="calf.serviceDate" /></th>
									<th><s:text name="calf.lastDateCalving" /></th>
									<th><s:text name="calf.gender" /></th>
									<th><s:text name="calf.birthWeight" /></th>
									<th><s:text name="calf.calvingIntvalDays" /></th>
									<th><s:text name="calf.deliveryProcess" /></th>
									<th width="14%" colspan="3">Action</th>
								</tr>
							</thead>
							<tbody id="calfInfoTbl">
							
								<s:if test='"update".equalsIgnoreCase(command)'>
									<s:if test="calfList.size()>0">
										<s:iterator value="calfList" status="status" var="bean">
											<tr id='<s:property value="%{#status.count-1}" />'>
											
												<s:hidden id="calfUniqId" value="%{id}" />
												<td align="center"><s:property value="calfId" /></td>
												<td align="center"><s:property value="bullIdVal" /></td>
												<td align="center"><s:date name="serviceDate" format="MM/dd/yyyy"/></td>
												<td align="center"><s:date name="lastDateCalving" format="MM/dd/yyyy"/></td>
												<td align="center"><s:property value="gender" /></td>
												<td align="center"><s:property value="birthWeight" /></td>
												<td align="center"><s:property value="calvingIntvalDays" /></td>
												<td align="center"><s:property value="deliveryProcessVal" /></td>
													<td style="text-align:center;">
													<button type="button" class="btn btn-sm btn-success" aria-hidden="true" onclick="editRow('<s:property value="%{#status.count-1}" />')"><i class=" fa fa-pencil-square-o"></i></button>
													<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="removeRow('<s:property value="%{#status.count-1}" />')"><i class="fa fa-trash" aria-hidden="true"></i></button>
												</td>	
												<td class="hide"><s:property value="bullId" /></td>
												<td class="hide"><s:property value="deliveryProcess" /></td>
												<td class="hide"><s:property value="id" /></td>
											</tr>
										</s:iterator>
									</s:if>
								</s:if>	
							</tbody>
						</table>
					</td>
				</tr>

			</tbody>

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
	</s:form>
	
	<s:if test='farmId!="" && farmId!=null '>
	<s:form name="listForm" action="farm_detail.action%{tabIndexz}">
			<s:hidden name="id" value="%{farmId}"/>
			<s:hidden name="tabIndexz" value="%{tabIndexz}" />
			<s:hidden name="tabIndex" value="%{tabIndexz}" />
			<s:hidden key="currentPage" />
		</s:form>
	
	</s:if>

	<s:if test='researchStationId!="" && researchStationId!=null'>
		<s:form name="listForm" action="researchStation_detail.action%{tabIndexz}">
			<s:hidden name="id"  value="%{researchStationId}"/>
			<s:hidden name="tabIndexz" value="%{tabIndexz}" />
			<s:hidden name="tabIndex" value="%{tabIndexz}" />
			<s:hidden key="currentPage" />
		</s:form>
	</s:if>
	
	
	<s:hidden id="genderVal" name="genderVal" />
	<script type="text/javascript">
	var addRowCheck=0;
	
  	 var calfSno=0;
      function onCancel() {
          document.listForm.submit();
      }
      function td(val){
    	  return "<td>"+val+"</td>";
      }
      
      function tr(val,id){
    	  return "<tr id='"+id+"'>"+val+"</tr>";
      }
      
      function addCalf()
      {
    	  var bullVal = $("#bullId option:selected").text();
    	  var deliveryProcVal = $("#deliveryProcess option:selected").text();
    	  var calfId=$("#calfId").val();
    	  var bullId=$("#bullId").val();
    	  var serviceDate=$("#serviceDate").val();
    	  var lastDateCalving=$("#lastDateCalving").val();
    	  var gender=$("#genderVal").val();
    	  var birthWeight=$("#birthWeight").val();
    	  var calvingIntvalDays=$("#calvingIntvalDays").val();
    	  var deliveryProcess=$("#deliveryProcess").val();
    	  var calfUniqId=$("#hiddenCalfId").val();
    	  var rowCount = $('#calfInfoTbl tr').length;
    	  rowCount++;
    	  if(calfId==null || calfId=="")
    	  {
    		  document.getElementById("validateError").innerHTML='<s:text name="empty.calfId"/>';
    		  return false;
    		  
    		  
    	  }
    	  else if(bullId==null || bullId=="")
    	  {
    		  
    		  document.getElementById("validateError").innerHTML='<s:text name="empty.bullId"/>';
    		  return false;
    	  }
    	  else
    		{
    		   	var tbody="";
    		  	tbody+=td(calfId);
    		  	tbody+=td(bullVal);
    		  	tbody+=td(serviceDate);
    		  	tbody+=td(lastDateCalving);
    		  	tbody+=td(gender);
    		  	tbody+=td(birthWeight);
    		  	tbody+=td(calvingIntvalDays);
    		  	tbody+=td(deliveryProcVal);
    		  	tbody+="<td class='hide'>"+calfUniqId+"</td>";//td(getFormattedValue(calfUniqId));
    		  	
    		  	tbody+="<td class='hide'>"+bullId+"</td>";//td(bullId);
    		  	tbody+="<td class='hide'>"+deliveryProcess+"</td>";//td(deliveryProcess);
    		  	
    		  	tbody+='<td style="text-align:center;"><button type="button" class="btn btn-sm btn-success" aria-hidden="true" onclick="editRow('+rowCount+')" title="<s:text name="Edit" />"><i class="fa fa-pencil-square-o"></i></button>&nbsp;'+
				'<button type="button" class="btn btn-sm btn-danger" onclick="removeRow('+rowCount+')" title="<s:text name="Delete" />"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'

    		  	$('#calfInfoTbl').append(tr(tbody,rowCount));
    		
    		  
    	 	}
    	  addRowCheck=1;
    	  resetProducts();
      }
      
     
      
     function removeRow(indx){
    	 addRowCheck=1;
    	  $("#calfInfoTbl tr:eq("+indx+")").remove();
   	 }
     
     function editRow(index){
    	 var calfDatas="";
   		 var tableBody = jQuery("#calfInfoTbl");
    	 jQuery("#"+index).each(function (el) {
		 var $tds = $(this).find('td'),

    		 calfId = $tds.eq(0).text(),
        	 bullId = $tds.eq(1).text(),
        	 serviceDate = $tds.eq(2).text(),
        	 clavingDate = $tds.eq(3).text(),
        	 gender = $tds.eq(4).text(),
        	 birthWeg = $tds.eq(5).text(),
        	 clavingDays = $tds.eq(6).text(),
        	 deliverProc = $tds.eq(7).text(),
			 calfUniqId = $tds.eq(8).text(),
			 bullKey = $tds.eq(9).text(),
			 deliverKey = $tds.eq(10).text();
		
			 $("#deliveryProcess").val(deliverKey.trim());
			 $("#bullId").val(bullKey.trim());
	  		$("#calfId").val(calfId);
	       $("#serviceDate").val(serviceDate);
	      	$("#lastDateCalving").val(clavingDate);
	      	$("#genderVal").val(gender);
	      	$("#birthWeight").val(birthWeg);
	      	$("#calvingIntvalDays").val(clavingDays);
	      	$("#calfUniqId").val(calfUniqId);
	      	if(calfId!="" && calfId!=null || bullId!="" && bullId!=null)
	      	{
	      		calfDatas+=getFormattedValue(calfId)+"###"+getFormattedValue(bullId)+"###"+getFormattedValue(serviceDate)+"###"+getFormattedValue(clavingDate)+"###"+
	   			getFormattedValue(gender)+"###"+getFormattedValue(birthWeg)+"###"+getFormattedValue(clavingDays)+"###"+getFormattedValue(deliverProc)+"###"+getFormattedValue(calfUniqId)+"@@@";
	      	}	
   			
		});


			
    	    jQuery("#"+index).empty();
    	    addRowCheck=1;
     }
     
     
     function resetProducts()
     {
    	 var idsArray=['bullId','deliveryProcess'];
    	resetSelect2(idsArray);
	   	 $("#calfId").val("");
	     $("#bullId").val("");
	     $("#serviceDate").val("");
	     $("#lastDateCalving").val("");
      	$("#genderVal").val("");
      	$("#birthWeight").val("");
      	$("#calvingIntvalDays").val("");
      	$("#deliveryProcess").val("");
	      //	$("#calfUniqId").val(calfUniqId);
	
     }
    	
    function getGenderVal(gender)
    {
    	$("#genderVal").val(gender);
    }
    
    function onSubmit()
    {
    	validateImage();
    	validateData();
    	 var tableBody = jQuery("#calfInfoTbl");
    	 var calfDatas="";
		if(addRowCheck==1)
		{
    		 tableBody.find('tr').each(function (el) {
			 var $tds = $(this).find('td'),
        	 calfId = $tds.eq(0).text(),
        	 bullId = $tds.eq(1).text(),
        	 serviceDate = $tds.eq(2).text(),
        	 clavingDate = $tds.eq(3).text(),
        	 gender = $tds.eq(4).text(),
        	 birthWeg = $tds.eq(5).text(),
        	 clavingDays = $tds.eq(6).text(),
        	 deliverProc = $tds.eq(7).text(),
			 calfUniqId = $tds.eq(8).text(),
				bullKey = $tds.eq(9).text(),
			 delivKey = $tds.eq(10).text();
        
			 if(calfId!="" && calfId!=null || bullId!="" && bullId!=null)
		      	{
				 
				 	calfDatas+=getFormattedValue(calfId)+"###"+getFormattedValue(bullKey)+"###"+getFormattedValue(serviceDate)+"###"+getFormattedValue(clavingDate)+"###"+
	        		getFormattedValue(gender)+"###"+getFormattedValue(birthWeg)+"###"+getFormattedValue(clavingDays)+"###"+getFormattedValue(delivKey)+"###"+getFormattedValue(calfUniqId)+"@@@";
		      	}
	    	});
    		  
    		 var calfProdData = calfDatas.replace("null", ""); 
			 document.getElementById("calfProdData").value=calfProdData;
		}	 
			 if(validateData())
			 {
					 document.form.action='cow_<s:property value="command" />.action';
					 document.form.submit();
	    	  }
    	}
    		
   			

    function validateData()
    {
    	var hit=true;
    	 var cowId=$("#cowId").val();
    	 var lactationNo=$("#lactationNo").val();
    	 var tagNo=$("#tagNo").val();
    	  var calfId=$("#calfId").val();
    	  var bullId=$("#bullId").val();
    	  var tableBody = jQuery("#calfInfoTbl");
   			var rowCount = $('#calfInfoTbl tr').length;
    	 if(cowId==null || cowId=="")
   	 	 {
   		  document.getElementById("validateError").innerHTML='<s:text name="empty.cowId"/>';
   		  return false;
   		  hit="false";
   		  
   	 	 }
    	 else if(lactationNo==null || lactationNo=="")
    	 {
    		 document.getElementById("validateError").innerHTML='<s:text name="empty.lactationNo"/>';
      		  return false;
      		  hit="false";
    	 }
    	 
    	 else if(tagNo==null || tagNo=="")
    	 {
    		 document.getElementById("validateError").innerHTML='<s:text name="empty.tagNo"/>';
      		  return false;
      		  hit="false";
    	 }
    	 
    	 
    	 else if(calfId!='' && calfId!=null || bullId!=''&& bullId!=null && parseInt(rowCount)==0)
    	 {
    		 document.getElementById("validateError").innerHTML='<s:text name="empty.calfData"/>';
      		  return false;
      		  hit="false";
    	 }
    	 
    	 return hit;
    	
    }
    
    function getFormattedValue(val){
    	   if(val==null||val==undefined||val.toString().trim()==""){
    	    return " ";
    	   }
    	   return val;
    	 }
    
    function validateImage() {

        var file = document.getElementById('cowImage').files[0];
        var filename = document.getElementById('cowImage').value;
        var fileExt = filename.split('.').pop();

        if (file != undefined) {

            if (fileExt == 'jpg' || fileExt == 'jpeg' || fileExt == 'png'
                    || fileExt == 'JPG' || fileExt == 'JPEG'
                    || fileExt == 'PNG') {
                if (file.size > 51200) {
                    alert('<s:text name="fileSizeExceeds"/>');
                    file.focus();
                    return false;
                }//else if(imgWidth >260){
                //alert('<s:text name="imageWidthMsg"/>');
                //file.focus();
                //return false;	
                //}else if(imgHeight> 70){
                //alert('<s:text name="imageHeightMsg"/>');
                //file.focus();
                //return false;	
                //}
            //    document.getElementById('cowImageExist').value = "1";
            } else {
                alert('<s:text name="invalidFileExtension"/>')
                file.focus();
                return false;
            }
        }

        return true;
    }
    
    function checkImgHeightAndWidth(val) {
        var _URL = window.URL || window.webkitURL;
        var img;
        var file = document.getElementById('cowImage').files[0];
        if (file) {

            img = new Image();
            img.onload = function () {
                imgHeight = this.height;
                imgWidth = this.width;
            };
            img.src = _URL.createObjectURL(file);
        }
    }
    
     </script>

</body>

