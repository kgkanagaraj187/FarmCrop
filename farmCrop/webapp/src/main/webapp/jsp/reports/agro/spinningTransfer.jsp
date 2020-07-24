<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
<s:form name="createForm" cssClass="fillform" enctype="multipart/form-data"
		action="spinningReport_create?edit=1">
		<s:hidden key="selectedBales" id="selectedBales" name="selectedBales" />
<div class="appContentWrapper marginBottom ">
	<div class="formContainerWrapper">
		<div class="row">
			<div class="container-fluid">
				<div class="notificationBar">
					<div class="error">
						<p class="notification">
							<span class="manadatory">*</span>
							<s:text name="reqd.field" />
							<div id="validateError" style="text-align: center;"></div>
							<div id="validatePriceError" style="text-align: center;"></div>
						</p>
					</div>
				</div>
			</div>
		</div>
		<h2><s:property value="%{getLocaleProperty('transfer.spinning')}" /></h2>
		<div class="flexiWrapper filterControls">
			<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('date')}" /><sup style="color: red;">*</sup></label>
				<div class="form-element">
							<s:textfield name="processDate" id="processDate" readonly="true"
								theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years"
								cssClass="date-picker col-sm-3 form-control" size="20" />
				</div>
			</div>
			<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('ginning')}" /><sup
							style="color: red;">*</sup></label>
				<div class="form-element">
							<s:select list="{}" headerKey="" name="selectedGining"
								headerValue="%{getText('txt.select')}" id="ginningList"
								onchange="loadHeap(this.value)"
								cssClass="form-control input-sm select2" />
				</div>
			</div>
			<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('spinning')}" /><sup
							style="color: red;">*</sup></label>
				<div class="form-element">
							<s:select list="{}" headerKey="" name="selectedSpinning"
								headerValue="%{getText('txt.select')}" id="spinningList"
								cssClass="form-control input-sm select2" />
				</div>
			</div>
			<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('heap')}" /><sup
							style="color: red;">*</sup></label>
				<div class="form-element">
							<s:select list="{}" headerKey="" name="selectedHeap"
								headerValue="%{getText('txt.select')}" id="heapList"
								multiple="true" onchange="loadBaleDetails();" cssClass="form-control input-sm select2" />
				</div>
			</div>
			<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('invoice')}" /><sup
							style="color: red;">*</sup></label>
				<div class="form-element">
					<s:textfield id="invoiceNo" name="invoiceNo" maxLength="10"
								cssClass="col-sm-3 form-control" onkeypress="return isAlpahaNumeric(event)"/>		
				</div>
			</div>
				<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('truckNo')}" /><sup
							style="color: red;">*</sup></label>
				<div class="form-element">
					<s:textfield id="truckNo" name="truckNo" maxLength="10"
								cssClass="col-sm-3 form-control" onkeypress="return isAlpahaNumeric(event)"/>		
				</div>
			</div>
			<div class="flexi flexi10">
				<label for="txt"><s:property
								value="%{getLocaleProperty('type')}" /><sup
							style="color: red;">*</sup></label>
				<div class="form-element">
					<s:select list="{}" headerKey="" name="selectedtype"
								headerValue="%{getText('txt.select')}" id="typeList"
								cssClass="form-control input-sm select2" />	
				</div>
			</div> 
			
			<div class="flexi flexi10">
						<label for="txt"><s:text name="rate" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield cssClass="form-control"
								id="price" name="price"
								maxlength="45" />
						</div>
					</div>
		</div>
		
			
		
	</div>
</div>
<div class="appContentWrapper marginBottom">
	<div class="formContainerWrapper dynamic-form-con">
		<h2><s:property value="%{getLocaleProperty('info.baleGeneration')}" /></h2>
		<table class="table table-bordered aspect-detail mainTable" id="baleTable">
			<thead>
				<tr class="odd">
					<th><s:text name="serialNo" /></th>
					<th><s:text name="heapName" /></th>
					<th><s:text name="lotNo" /></th>
					<th><s:text name="prNo" /></th>
					<th><s:text name="baleWeight" />(<s:property value="%{getLocaleProperty('kg')}" />)</th>
					<th><s:text name="status" /></th>
				</tr>
			</thead>
			<tbody id="tBodyTemplate1">
				  <tr>
					<td colspan="8" style="text-align: center;"><s:text	name="noGinningRecordFound" /></td>
				</tr>  
				</tbody>
				<!-- <tfoot>
					<tr>
						<td colspan="3" width="30%" style="border: none !important;">&nbsp;</td>
						<td style="border: none !important;">Total Bale Weight</td>
						<td style="padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important; text-align: right;">0.00</td>
						<td style="border: none !important;">&nbsp;</td>
					</tr>
				</tfoot> -->
		</table>
	</div>
</div> 

	<div>
						<div class="appContentWrapper marginBottom">
							<table id="gmo"
								class="table table-bordered aspect-detail mainTable"
								width="600px">

								<tr class="odd">
									<th bgcolor="#a8e3d6"><s:property value="%{getLocaleProperty('gmoFile')}" /></th>
									<th bgcolor="#a8e3d6">
										<button type="button" onclick="addGmoDetail()"
											cssclass="btn btn-sts" data-keyboard="false">
											<i class="fa fa-plus" aria-hidden="true"></i>
										</button>
									</td>
								</tr>
								
							<%-- 	<tr id="tr%{#stat.index}">
								<td colspan="2"><div class="form-element">
													<s:file name="imgSet[%{#stat.index}].imageFile"
														id="file%{#stat.index}"
														onchange="setFileName(%{#stat.index})"
														cssClass="form-control" />
													<s:hidden id="fileName%{#stat.index}"
														name="imgSet[%{#stat.index}].fileName" />
													<s:hidden id="fileType%{#stat.index}"
														name="imgSet[%{#stat.index}].fileType" /></td>
								</tr> --%>
							
							<%-- 	<s:iterator value="imgSet" status="stat" var="img">
									<tr id="tr%{#stat.index}">

										<s:if test='imageByteString!=null && imageByteString!=""'>
											<td><s:property value="#img.fileName" /> <s:hidden
													name="imgSet[%{#stat.index}].fileName"
													value="%{#img.fileName}" /> <s:hidden
													name="imgSet[%{#stat.index}].fileType"
													value="%{#img.fileType}" /> <s:hidden
													name="imgSet[%{#stat.index}].imageByteString"
													value="%{#img.imageByteString}" /></td>
											<td>
												<s:hidden  name="imgSet[%{#stat.index}].fileType"value="#fileName" />
												<button type="button" onclick="removeTr(this)"
													class="btn btn-sts" data-keyboard="false">
													<i class="fa fa-remove" aria-hidden="true"></i>
												</button>
											</td>
										</s:if>

										<s:else>
											<td colspan="2"><div class="form-element">
													<s:file name="imgSet[%{#stat.index}].imageFile"
														id="file%{#stat.index}"
														onchange="setFileName(%{#stat.index})"
														cssClass="form-control" />
													<s:hidden id="fileName%{#stat.index}"
														name="imgSet[%{#stat.index}].fileName" />
													<s:hidden id="fileType%{#stat.index}"
														name="imgSet[%{#stat.index}].fileType" /></td>
										</s:else>


									</tr>

								</s:iterator> --%>
							</table>
						</div>


					</div>
					<div class="yui-skin-sam " style="display: block">
		<span id="save" class=""><span class="first-child">
				<button id="saveButton" type="button" onclick="submitTransferSpinning()"
					class="save-btn btn btn-success">
					<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
					</font>
				</button>
		</span></span> <span id="cancel" class=""><span
			class="first-child"><button type="button" id="cancelBut"
					class="back-btn btn btn-sts">
					<b><FONT color="#FFFFFF"><s:text name="back.button" />
					</font></b>
				</button></span></span>
	</div>
</s:form>
<s:form name="cancelform">
	<s:hidden key="currentPage" />
</s:form>

<script>
	$(document).ready(function() {
		
		$("#cancelBut").click(function() {
		
			document.cancelform.action="spinningReport_list?edit=1";
			document.cancelform.submit();
			});
		$('#processDate').val('<s:property value="currentDate" />');
		onFilterData();
	});
	
	function onFilterData() {
		callAjaxMethod("spinningReport_populateGinningList.action","ginningList");
		callAjaxMethod("spinningReport_populateSpinningList.action","spinningList");
		callAjaxMethod("spinningReport_populateTypeList.action","typeList");
	}
	function callAjaxMethod(url, name) {
		var cat = $.ajax({
						url : url,
						async : true,
						type : 'post',
						success : function(result) {
							insertOptions(name, JSON.parse(result));
						}
				});
	}
	function setFileName(idd){
		var file = document.getElementById('file'+idd).files[0];
		var filename =  document.getElementById('file'+idd).value;
		filename=filename.replace(/\s/g,'');
		filename = filename.substring(filename.lastIndexOf("\\") + 1);
		//filename = filename.split('\\').pop();
		var fileExt = filename.split('.').pop();
		$('#fileName'+idd).val(filename);
		$('#fileType'+idd).val(fileExt);
	
	}
	function submitTransferSpinning(){
		 var balIds = '';
		$('.baleMove:checked').each(function() {
			
				if (!isEmpty(this.id))
				balIds = balIds + this.id.split("_")[1] + ",";
		});
		
		$("#selectedBales").val(balIds); 
		var processDate = document.getElementById("processDate").value;
		var invoiceNo = document.getElementById("invoiceNo").value;
		var truckNo = document.getElementById("truckNo").value;
		var selectedtype = document.getElementById("typeList").value;
		var selectedSpinning = document.getElementById("spinningList").value;
		var selectedGining = document.getElementById("ginningList").value;
		var selectedHeap = document.getElementById("heapList").value;
		var price = document.getElementById("price").value;
		 if(processDate=="" &&  processDate==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.Date')}" />';
				enableButton();
				hit=false;
				return false;
			}
			
			 if(invoiceNo=="" || invoiceNo==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.invoiceNo')}" />';
				enableButton();
				hit=false;
				return false;
			}
			 if(truckNo=="" || truckNo==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty,truck')}" />';
				enableButton();
				hit=false;
				return false;
			}
			 if(selectedtype=="" || selectedtype==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.type')}" />';
				enableButton();
				hit=false;
			}
			 if(selectedSpinning=="" || selectedSpinning==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.spinning')}" />';
				enableButton();
				hit=false;
				return false;
			}
			 if(selectedGining=="" || selectedGining==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.ginning')}" />';
				enableButton();
				hit=false;
				return false;
			}
			 if(selectedHeap=="" || selectedHeap==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.batch')}" />';
				enableButton();
				hit=false;
				return false;
			}
			 if(balIds=="" || balIds==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.selectedBales')}" />';
				enableButton();
				hit=false;
				return false;
			}
			if(price=="" || price==0){
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.price')}" />';
				enableButton();
				hit=false;
				return false;
			}
		document.createForm.submit();	
	}
	
	function addGmoDetail() {

		var rowCount = $('#gmo tr').length;
		$("#gmo").append($('<tr>').append($('<td>').append('<s:file id="file'+ (rowCount - 1)+'" onchange="setFileName('+ (rowCount - 1)+')" name="imgSet['
																+ (rowCount - 1)
																+ '].imageFile" 	cssClass="form-control" />	<s:hidden id="fileName'+ (rowCount - 1)+'" name="imgSet['+ (rowCount - 1)+'].fileName"/>'
																+ '<s:hidden id="fileType'+ (rowCount - 1)+'" name="imgSet['+ (rowCount - 1)+'].fileType"/>')));

	}
	function removeTr(id){
		$(id).closest('tr').remove();
	}
	function validateImage() {
		var file = document.getElementById('gmoFile').files[0];
		var filename = document.getElementById('gmoFile').value;
		var fileExt = filename.split('.').pop();
		hit = true;
		if (file != undefined) {
			if (fileExt == 'jpg' || fileExt == 'jpeg' || fileExt == 'png'
					|| fileExt == 'JPG' || fileExt == 'JPEG'
					|| fileExt == 'PNG' || fileExt == 'xlsx'
					|| fileExt == 'xls' || fileExt == 'docx'
					|| fileExt == 'doc') {
				hit = true;
			} else {
				hit = false;
			}
		}
		return hit;
	}
	function isAlpahaNumeric(e){
    	var regex = new RegExp("^[a-zA-Z0-9\b]+$");
	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	    if (regex.test(str)) {
	        return true;
	    }
	    e.preventDefault();
	    return false;
	}
	function loadHeap(ginning){
	  var resl = $.ajax({
					url : 'spinningReport_populateHeapList.action',
					async : true,
					data: {selectedGinning:ginning},
					type : 'post',
					success : function(result) {
						insertOptions('heapList', JSON.parse(result));
					}
				}); 
	}
	function loadBaleDetails(){
		var selectedGinning=$('#ginningList').val();
		var selectedHeaps="";
		$($("#heapList").val()).each(function(k, v) {
			selectedHeaps +="'"+v+"'" + ",";
		});
		
		 $.getJSON('spinningReport_populateBaleData.action',{selectedHeaps : selectedHeaps,selectedGinning : selectedGinning},function(jd){
				
			 var temps=jd.data;
			 var bodyContent='';
			 for(var i=0;i<temps.length;i++){
				 var temp=temps[i];
				 var balId="myCheck_"+temp.id;
				 bodyContent+='<tr>';
				 bodyContent+='<td class="hide" >'+temp.id+'</td>';
			 bodyContent+='<td align="center">'+temp.sNo+'</td>'; 
				 bodyContent+='<td align="center">'+temp.heap+'</td>';
				 bodyContent+='<td align="center">'+temp.lot+'</td>';
		
				 bodyContent+='<td align="center">'+temp.pr+'</td>';
				 bodyContent+='<td align="center" >'+temp.bale+'</td>'; 
				 bodyContent+='<td align="center"><input type="checkbox"  id="'+balId+'" class="baleMove"></td>';
			//	bodyContent+='<td align="center">'+temp.ststus+'</td>';
			
				 bodyContent+='</tr>';
			}
			 
			 $('#tBodyTemplate1').html(bodyContent);
			 $('#temp').val('');
		 });
	}	 

		   
</script>
</body>




 
    