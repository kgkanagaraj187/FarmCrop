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
    var validationForCertifiedFarmer = false;
    var cerf = '<s:property value="farmerId" />';
    var hit=true;
    var enableSoil = '<s:property value="enableSoliTesting"/>';
    var tenant='<s:property value="getCurrentTenantId()"/>';
    var isGramPanchayatEnable='<s:property value="gramPanchayatEnable"/>';
   
    function isAlpahaNumeric(e){
    	var regex = new RegExp("^[a-zA-Z0-9\b]+$");
	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	    if (regex.test(str)) {
	        return true;
	    }

	    e.preventDefault();
	    return false;
    }


  
    
    function millestcultivation(evt){
    	var val=$(evt).val();
		if(val==1){
			$('.millCrop').show();
			$('.milletCrop').show();
			$('.millCount').show();
			$('.milletCount').show();
		}else{
			$('.millCrop').hide();
			$('.milletCrop').hide();
			$('.millCount').hide();
			$('.milletCount').hide();
		}
    }
    
    $(document).ready(function () {
    	
    	 var isEdit = '<s:property value="command" />';
    		
    		if(isEdit=='update'){
    			
    		//	showFarmOther($('#farmOwned').val());
    			//var soilType = $('#soilTypeName').val();
    			
    			 //irrigationType($('#methodIrrigation').val());
    			//$("#methodIrrigation").val();
    			calculateLandHect();
    			calculatePlantHect();
    			
    		}
    	
    });
  
   
   
    function validateImage() {

        var file = document.getElementById('farmImage').files[0];
        var filename = document.getElementById('farmImage').value;
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
                document.getElementById('farmImageExist').value = "1";
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
        var file = document.getElementById('farmImage').files[0];

        if (file) {

            img = new Image();
            img.onload = function () {
                imgHeight = this.height;
                imgWidth = this.width;
            };
            img.src = _URL.createObjectURL(file);
        }
    }
    function deleteFile(id) {
    	if(document.getElementById('farmImage').value==''){
    		return false;
    	}
    	
        if (confirm('<s:text name="confirmToDelete"/>')) {

            if (id != undefined) {

                $.post("farm_deletefile.action", {
                    fileid: id
                }, function (data, status) {
                    if (status === '<s:text name="success"/>') {
                        $('#image').attr("src", 'img/no-image.png');
                        $('#farmImageFileName').hide();
                        $("#farmImage").css({
                            "display": "block",
                            "float": "left"
                        });
                        $(".delBtnCon").addClass('noFile');
                        document.getElementById('farmImage').value = "";
                        document.getElementById('farmImageExist').value = "0";
                        alert('<s:text name="imgDelSuccessfully"/>');
                        $('#remImg').hide();
                    }
                });
            } else if (id == undefined) {

                $('#image').attr("src", 'img/no-image.png');
                $('#farmImageFileName').hide();
                $("#farmImage").css({
                    "display": "block",
                    "float": "left"
                });
                $(".delBtnCon").addClass('noFile');
                document.getElementById('farmImage').value = "";
                document.getElementById('farmImageExist').value = "0";
                alert('<s:text name="imgDelSuccessfully"/>');
               // $('#remImg').hide();
            }
        }
    }

    function checkImgHeightAndWidth(value) {
        $("#remImg").show();
        $(".farmImage").removeClass('farmImage');
        }

  

</script>
<body>
	<div class="error">
		<s:actionerror />
		<s:fielderror />
		
		<sup>*</sup>
		<s:text name="reqd.field" />
		
		<div style="float: right; color: #000000">
			<s:hidden name="farmerUniqueId" id="farmerUniqueId" />
			<table class="table table-bordered aspect-detail">
				<tr>
					<td style="padding-right: 5px; text-align: right;"><b><s:text
								name="farmer.name" /></b> <s:if
							test='"update".equalsIgnoreCase(command)'>
							<s:property value="farm.farmer.name" />
						</s:if> <s:else>
							<s:property value="farmerName" />
						</s:else></td>
				</tr>
			</table>
		</div>
		<div>
			<span id="validateError"></span>
		</div>
	</div>

	
	<s:form name="form" cssClass="fillform" action="farm_%{command}"
		role="form" method="post" enctype="multipart/form-data">
		<s:hidden key="currentPage" />
		<s:hidden id="farmerId" name="farmerId" />
		<s:hidden id="farmerName" name="farmerName" />
		<s:hidden name="tabIndex" />
		<s:hidden id="dateOfInspection" name="dateOfInspection" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="farm.id" />
		</s:if>
		<s:hidden key="command" id="command" />

		<table id="info" class="table table-bordered aspect-detail fixedTable">
			<tr>
				<th colspan="6"><a data-toggle="collapse"
					data-parent="#accordion" href="#farmerAccordian"
					class="accrodianTxt"><s:text name="info.farm" /></a></th>
			</tr>

			<tbody id="farmerAccordian" class="panel-collapse collapse in">
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="farm.farmName" />
							<sup style="color: red;">*</sup>
						</div></td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="farm.farmName" theme="simple" maxlength="35" id="farmName"
								cssClass="form-control input-sm checktexts" />
						</div>
					</td>


					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="%{getLocaleProperty('farm.totalLand')}" />
						</div></td>
					<td style="width: 17%;"><div class="inputCon">
							<s:textfield name="farm.farmDetailedInfo.totalLandHolding"
								id="totalLand" cssClass="form-control input-sm" theme="simple"
								maxlength="12" onkeyup="calculateLandHect();"
								onkeypress="return isDecimal(event)" />
						</div></td>
						
					<%-- 	<td style="width: 17%;"><div class="col-xs-16">
					<s:property value="%{getLocaleProperty('farm.totalLandHectare')}" />
							
						</div></td>
					<td style="width: 17%;"><div class="inputCon">
							<s:label id="landHectValues" />
						</div></td> --%>
						
								<td><div class="col-xs-16">
							<%--  <s:text name="farm.proposedPlantingArea" /> --%>
							<s:property
								value="%{getLocaleProperty('farm.proposedPlantingArea')}" />
						</div></td>
					<td><div class="inputCon">
							<s:textfield name="farm.farmDetailedInfo.proposedPlantingArea"
								id="plantingArea" cssClass="form-control input-sm"
								theme="simple" maxlength="12" onkeyup="calculatePlantHect();"
								onkeypress="return isDecimal(event)" />
						</div></td>
				</tr>

				<tr>

		<%-- 			
			<td style="width: 17%;"><div class="col-xs-16">
							<s:text
								name="%{getLocaleProperty('farm.proposedPlantingHectare')}" />
						</div></td>
					<td style="width: 17%;"><div class="inputCon">
							<s:label id="plantHectValues" />
						</div></td>


				 --%>
					<td><div class="col-xs-16">
							<s:text name="farm.photo" />
						</div></td>
					<td class="delBtnCon">
						<div class="inputCon">
							<s:if
								test='farmImageByteString!=null && farmImageByteString!=" "'>
								<s:textfield name="farm.farmImageFileName"
									id="farmImageFileName" theme="simple" maxlength="35" />
								<s:file name="farm.farmImage" id="farmImage"
									cssStyle="display:none;"
									onchange="checkImgHeightAndWidth(this)" />
							</s:if>
							<s:else>
								<s:file name="farm.farmImage" id="farmImage"
									onchange="checkImgHeightAndWidth(this)" />
							</s:else>
							<button type="button" class="aButtonClsWbg" id="remImg"
								onclick='deleteFile(<s:property value="farm.id" />)'>
								<i class="fa fa-trash-o" aria-hidden="true"></i>

							</button>
							<div style="font-size: 10px">
								<s:text name="farm.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
							</div>
							<div></div>
							<script type="text/javascript">
                                <s:if test='farmImageByteString==null || farmImageByteString==" "'>
                            		//$('#remImg').hide();
                                </s:if>
                            </script>
					</td>
					<td>
						<table class="table table-bordered aspect-detail">
							<tr>
								<td
									style="border-right: solid 1px #d2d695 !important; border-top: solid 1px #d2d695 !important;">
									<s:if
										test='farmImageByteString!=null && farmImageByteString!=" "'>
										<img width="50" height="50" border="0" id="image"
											src="data:image/png;base64,<s:property value="farmImageByteString"/>">
									</s:if> <s:else>
										<img width="50" height="50" id="remImgs" border="0"
											src="img/no-image.png">
									</s:else> <s:hidden name="farm.farmImageExist" id="farmImageExist" />
								</td>
							</tr>
						</table>
						</div>
					</td>
					
				</tr>
				
			</tbody>
		</table>
		
				
			<table id="info" class="table table-bordered aspect-detail">
			<tr>
				<th colspan="6"><a data-toggle="collapse"
					data-parent="#accordion" href="#farmHousing"
					class="accrodianTxt collapsed"><s:text name="info.housingInfo" /></a></th>
			</tr>
			<tbody id=farmHousing class="panel-collapse collapse">
				<tr>

			
					<td style="width: 17%;">
						<div class="col-xs-16">
							<s:text name="noCowShad" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td style="width: 17%;">
						<div class="inputCon">
							<s:textfield name="housingInfo.noCowShad" theme="simple" maxlength="20" onkeypress="return isNumber(event)"
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
					
				</tr>
				
			</tbody>
				


		</table>
	
		
		<div class="yui-skin-sam">
			<s:if test="command =='create'">
				<span id="button1" class=""><span class="first-child">
						<button type="button" onclick="onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if>
			<s:else>
				<span id="button1" class=""><span class="first-child">
						<button type="button" onclick="onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:else>
			<span id="cancel" class=""> <span class="first-child">
					<button type="button" onclick="onCancel();"
						class="cancel-btn btn btn-sts">
						<b> <FONT color="#FFFFFF"> <s:text name="cancel.button" />
						</FONT>
						</b>
					</button>
			</span></span>
		</div>
	</s:form>

	<s:form name="listForm" id="listForm" action="farmer_details.action">
		<s:hidden name="farmerId" value="%{farmerId}" />
		<s:hidden name="id" value="%{farmerId}" />
		<s:hidden name="tabIndexFarmer" />
		<s:hidden name="tabIndex" value="%{tabIndexFarmerZ}" />
		<s:hidden name="currentPage" />
	</s:form>

	<script type="text/javascript">
	
        function onCancel() {
            document.listForm.submit();
        }

        function onSubmit() {
            //document.getElementById("dateOfInspection").value = document.getElementById("calendar").value;
            validateImage();
            var tenant='<s:property value="getCurrentTenantId()"/>';
            //alert(sanghamVal);
           var farmName=$("#farmName").val();
            var prospadLand=$("#plantingArea").val();
            var totLand=$("#totalLand").val();
            if(farmName=='' || farmName==null)
            {
            	document.getElementById("validateError").innerHTML='<s:text name="empty.farmName"/>';
        		return false;
            }
            
            else if(parseFloat(prospadLand)>parseFloat(totLand))
            {
            	document.getElementById("validateError").innerHTML='<s:text name="pattern.proposedPlantingArea"/>';
        		return false;
            	
            }
            else
           	{
            	 document.form.action="farm_<s:property value="command" />.action"
            	 document.form.submit();
            	
           	}
  
       		
           
        }
      
        
     function showFarmOther(val){
    	if(val==99){
    	 jQuery(".farmOtherDiv").show();
    	 }else{
    		 jQuery(".farmOtherDiv").hide();
    		 $("#farmOther").val('');
    	 }
     }
     
     function calculateLandHect(){
     	var getValue=$("#totalLand").val();
     	if(getValue==""){
     		$("#landHectValues").empty();
     	}
     	else{
     		var landHecVal = $("#totalLand").val()*0.40468564224;
     		
         	document.getElementById('landHectValues').innerHTML=landHecVal.toFixed(5);
     	}
     }
     
     
     function calculatePlantHect(){
     	var value=$("#plantingArea").val();
     	if(value=="")
     		{
     		$("#plantHectValues").empty();
     		}
     	else
     		{
     	var plantHect = $("#plantingArea").val()*0.40468564224;
     	
			
     	document.getElementById('plantHectValues').innerHTML=plantHect.toFixed(5);
     		}
     }
     
     function setQualified(val){


     	if(val=='2'){
     	 jQuery(".qualNo").removeClass('hide');
     	 }else{
     		jQuery(".qualNo").find("input").val("");
        	 jQuery(".qualNo").addClass('hide');
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
     
     
     function isDecimal(evt) {
    		
    	 evt = (evt) ? evt : window.event;
    	    var charCode = (evt.which) ? evt.which : evt.keyCode;
    	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
    	        return false;
    	    }
    	    return true;
    }

    </script>
	
<s:hidden id="gramPanchayatEnable" name="gramPanchayatEnable"></s:hidden>
</body>
