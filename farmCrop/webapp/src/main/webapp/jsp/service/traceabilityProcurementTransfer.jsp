<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<style>
.fullwidth {
	width: 100%;
}
</style>

	<div class="appContentWrapper marginBottom ">
		<div class="formContainerWrapper">
			<h2>
				<s:property
					value="%{getLocaleProperty('info.traceabilityProcurementTransfer')}" />
			</h2>
			<div class="flexform">
				<div class="flexform-item">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('date')}" /><sup style="color: red;">*</sup>
					</label>
					<div class="form-element">
						<s:textfield name="date" id="date" readonly="true" theme="simple"
							data-date-format="%{getGeneralDateFormat().toLowerCase()}"
							cssClass="date-picker form-control calender" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('from')}" /><sup style="color: red;">*</sup>
					</label>
					<div class="form-element">
						<s:select name="selectedFrom" id="procurementCenterList"
							list="procurementCenterList" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="select2 form-control" onchange="loadTransferRecords()" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('to')}" /><sup style="color: red;">*</sup>
					</label>
					<div class="form-element">
						<s:select name="selectedTo" list="ginnerCenterList" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="select2 form-control" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('truckId')}" /><sup style="color: red;">*</sup>
					</label>
					<div class="form-element">
						<s:textfield id="truckId" name="truckId" theme="simple"
							maxlength="25" cssClass="form-control" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('driver')}" /><sup style="color: red;">*</sup>
					</label>
					<div class="form-element">
						<s:textfield id="driver" name="driver" theme="simple"
							maxlength="25" cssClass="form-control" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('transporter')}" /><sup style="color: red;">*</sup>
					</label>
					<div class="form-element">
						<s:textfield id="transporter" name="transporter" theme="simple"
							maxlength="25" cssClass="form-control" />
					</div>
				</div>
				
			</div>

			<div class="flexform">
				<div class="flexform-item fullwidth">
					<label for="txt" style="width: 14.3%"> <s:property
							value="%{getLocaleProperty('farmer.icsName')}" />
					</label>
					<div class="form-element" style="width: 85%">
						<s:select name="selectedVillage" id="icsList" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="select2 form-control" onchange="loadTransferRecords()"
							multiple="true" />
					</div>
				</div>
			</div>

			<div class="flexform margin-top2">
				<div class="col-md-12">
				<div class="error" style="color:red"></div>
					<table id="example" class="table table-striped table-bordered "
						width="100%" cellspacing="0">
						<thead>
							<tr>
								<th><input type='checkbox' class="form-control"
									onchange="checkAll(this)"></th>
								<th>SNo</th>
								<th><s:property
										value="%{getLocaleProperty('farmer.firstName')}" /></th>
								<th><s:property
										value="%{getLocaleProperty('farmer.farmerId')}" /></th>
										<th><s:property
										value="%{getLocaleProperty('city.name')}" /></th>
											<th><s:property
										value="%{getLocaleProperty('village.name')}" /></th>
										<th><s:property
										value="%{getLocaleProperty('profile.samithi')}" /></th>
										<th><s:property
										value="%{getLocaleProperty('fpoGroup')}" /></th>
											<th><s:property
										value="%{getLocaleProperty('product')}" /></th>
										<th><s:property
										value="%{getLocaleProperty('variety')}" /></th>
											<th><s:property
										value="%{getLocaleProperty('grade')}" /></th>
								<th><s:property value="%{getLocaleProperty('noOfBags')}" /></th>
								<th><s:property value="%{getLocaleProperty('grossWeight')}" /></th>
								<th><s:property
										value="%{getLocaleProperty('farmer.icsStatus')}" /></th>
							</tr>
						</thead>
						<tbody id="transferDetailsData">
						</tbody>
						<%-- <tfoot>
							<tr>
								<th></th>
								<th>SNo</th>
								<th><s:property
										value="%{getLocaleProperty('farmer.firstName')}" /></th>
								<th><s:property
										value="%{getLocaleProperty('farmer.farmerId')}" /></th>
								<th><s:property value="%{getLocaleProperty('noOfBags')}" /></th>
								<th><s:property value="%{getLocaleProperty('grossWeight')}" /></th>
								<th><s:property
										value="%{getLocaleProperty('farmer.icsStatus')}" /></th>
							</tr>
						</tfoot> --%>
					</table>
				</div>
			</div>

			<div class="flexItem flex-layout flexItemStyle">
				<div class="button-group-container">
					<button type="button" class='btn btn-sts'
						onclick="populateTransfer()">
						<s:property value="%{getLocaleProperty('transfer')}" />
					</button>
					<button type="button" onclick="onCancel();"  class='btn btn-warning'>
						<s:property value="%{getLocaleProperty('Cancel')}" />
					</button>
				</div>
			</div>

		</div>
	</div>
<!-- <button type="button" data-toggle="modal" data-target="#myModal"
		style="display: none" id="enableModal" class="modal hide"
		data-backdrop="static" data-keyboard="false">Open Modal</button>
		 -->
		
		<button type="button" class="modal hide" id="enableModal" data-toggle="modal"
  data-target="#myModal">Open Modal</button>

	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog modal-sm">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						onclick="disablePopupAlert()">&times;</button>
					<h4 class="modal-title">
						<s:text name="mtntSuccess" />
					</h4>
				</div>
				<div class="modal-body">
					<div id="divMsg" align="center"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" id="closeBut"
						onclick="disablePopupAlert()">Close</button>
				</div>
			</div>

		</div>
	</div>
	<script>
		jQuery(document).ready(function() {
			jQuery.post("traceabilityProcurementTransfer_populateICSByGroup.action",{}, function(result) {
						insertOptions("icsList", JSON.parse(result));
			});
			$('#myModal').on('shown.bs.modal', function(e){
			    $('#closeBut').focus();
			});
		
			
		});

		function loadVillageByGroup(val) {
			jQuery
					.post(
							"traceabilityProcurementTransfer_populateICSByGroup.action",
							{
								selectedGroup : val
							}, function(result) {
								insertOptions("icsList", JSON.parse(result));
							});
		}

		function loadTransferRecords() {
			$('#example').DataTable().ajax.reload();
			$('.error').html('');
		}

		function td(data) {
			var td = $("<td/>").text(data);
			return td;
		}

		function checkAll(obj) {
			if ($(obj).prop('checked')) {
				$(".icheckbox").attr("checked");
				$('.icheckbox').prop('checked', true);
			} else {
				$('.icheckbox').prop('checked', false);
			}
		}
		

		function onCancel() {
	      window.location.href="traceabilityProcurementTransfer_create.action";	
	    }
		
		function populateTransfer() {
			var dataa=new Array();
			$('.error').html('');
			$(".icheckbox").each(function(){
				if ($(this).prop('checked')) {
					netWeigh = $('#'+"netWeigh"+$(this).val()).val();
					noBags = $('#'+"noBags"+$(this).val()).val();
					farmer=$('#'+"farmer"+$(this).val()).val();
					ics=$('#'+"ics"+$(this).val()).val();
					farmerName=$('#'+"farmerName"+$(this).val()).val();
					dataa.push({
						id:$(this).val(),
						netWeight:netWeigh,
						noOfBags:noBags,
						farmer:farmer,
						ics:ics,
						farmerName:farmerName
					});
				}
			});
			
			
			var jsonString = "";
			if($.isEmptyObject(dataa)){
				alert("Please Select Atleast One Farmer");
				return false;
			}
			else{
				//alert("Success");
				jsonString = JSON.stringify(dataa);
			}
		
			if(isEmpty($("#selectedTo").val())){
				alert("Please Select Ginning");
				return false;
			} 
			if(isEmpty($("#truckId").val())){
				alert("Please Enter Truck No");
				return false;
			}
			if(isEmpty($("#driver").val())){
				alert("Please Enter Driver Name");
				return false;
			}
			if(isEmpty($("#transporter").val())){
				alert("Please Enter Transporter Name");
				return false;
			}
			if(isEmpty($('#date').val())){
				alert("Please Select Date");
				return false;
			}
			else{
				//alert(jsonString);
			var data={
					jsonString:jsonString,
					selectedFrom:$("#procurementCenterList").val(),
					selectedTo:$("#selectedTo").val(),
					truckId:$("#truckId").val(),
					driver:$("#driver").val(),
					transporter:$("#transporter").val(),
					date:$('#date').val(),
				};
			 $.ajax({
				 type: "POST",
		         async: false,
		         url: "traceabilityProcurementTransfer_populateTransfer.action",
		         data:data,
		         success: function(data) {
		            	/* window.location.href="traceabilityProcurementTransfer_list.action"; */
		        	 if(data == null || data == ""){		
		     			$("#saveBtn").prop("disabled",false);
		     			}
		     		else{ 	 			
		     			var jsonData = JSON.parse(data);
		     			if(jsonData.id=="error"){
		     				alert(jsonData.name);
		     			}else{
		     				var ids=new Array();
		     				 ids.push("selectedTo");
		     				 ids.push("icsList");
		     				 ids.push("procurementCenterList");
		     				resetSelect2(ids);
		     				$("#saveBtn").prop("disabled",true);
		     				/* $('selectedTo').val('').select2();
		     				$('icsList').val('').select2();
		     				$('procurementCenterList').val('').select2().trigger('change'); */
		     				//$('#procurementCenterList').trigger('change'))
		     				document.getElementById("divMsg").innerHTML=jsonData.name;
	     					document.getElementById("enableModal").click();
			     		
		     			}
		     		}
		         }
			 });
			}
		}

		$('#example')
				.DataTable(
						{
							scrollY : '50vh',
							scrollCollapse : true,
							"destroy" : true,
							aLengthMenu:[10,25,50,100,200],
							ajax : {
								url : 'traceabilityProcurementTransfer_populateTransferStockDetails.action',
								"processing" : true,
								type : 'POST',
								"data" : function(reqData) {
									var dataa = "";
									$($("#icsList").val()).each(function(k, v) {
										dataa += v + ",";
									});
									reqData.selectedVillage = dataa;
									reqData.selectedFrom = $(
											"#procurementCenterList").val();
								}
							}
						});
		
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
		/* function validateBags(sNo,id,bags){
			var currBags = $('#noBags'+id).val();
			if(parseInt(currBags) > parseInt(bags)){
				$('.error').html('<s:text name ="greaterThanbags"></s:text>');
				$('.btn-sts').prop("disabled",true);
				
			}else{
				$('.error').html('');
				$('.btn-sts').prop("disabled",false);
			}
		}

	function validateWeight(sNo,id,wight){
		
		var currBags = $('#netWeigh'+id).val();
		if(parseFloat(currBags) > parseFloat(wight)){
			$('.error').html('<s:text name ="greaterThanAvailableWirght"></s:text>');
			$('.btn-sts').prop("disabled",true);
		}else if(currBags.trim()==''||currBags.trim()==0){
			$('.error').html('<s:text name ="netWeightGreaterThanZero"></s:text>');
			$('.btn-sts').prop("disabled",true);
		}
		else{
			$('.error').html('');
			$('.btn-sts').prop("disabled",false);
		} 
			
		}*/
		function disablePopupAlert(){
			$('#pendingRestartAlertErrMsg').html('');
			$('#popupBackground').hide();
			$('#restartAlert').hide();
			$('body').css('overflow','');
			reset();
		 	$.post("traceabilityProcurementTransfer_create.action",{procurementInfo:""},function(data){
		 		$("#saveBtn").prop("disabled",false);
		 	}); 
		 	
		}
		function reset(){
			window.location.href="traceabilityProcurementTransfer_list.action";    
		}
	</script>

</body>