<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

</head>
<body>
	<script type="text/javascript">
	var incomeDatas="";
	var error =true;
jQuery(document).ready(function(){
	
	jQuery(".back-btn").click( function() {
	document.cancelform.submit();
	
	
});
	 /* jQuery("#update").click( function() {
		document.updateform.submit();
	}); */
	
});

function calcFarmerIncome(obj)
{
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
		jQuery("#validateError").text("");
		error= true;
		var val1=parseFloat(jQuery(this).find(".agriIncome").val());
		//var val2=parseFloat(jQuery(this).find(".cottonIncome").val());
		var val2=parseFloat(jQuery(this).find(".otherIncome").val());
		var val3=parseFloat(jQuery(this).find(".interCropIncome").val());
		//var val4=parseFloat(jQuery(this).find(".otherIncome").val());
		
		var cultiId=jQuery(this).find(".cultivationId").val();
		var totAmt = 0.00;
		  if((val1=="" || val1==null|| isNaN(val1))
				  &&(val2=="" || val2==null|| isNaN(val2))
				  && (val3=="" || val3==null||isNaN(val3))){
			  jQuery("#validateError").text("");
			  jQuery("#validateError").text('<s:text name="empty.income"/>');
			  error=false;
		}/* 
		 if(val3=="" || val3==null||isNaN(val3)){
			  jQuery("#validateError").text("");
			  jQuery("#validateError").text('<s:text name="empty.interValue"/>');
			  error=false;
		}
		if(val2=="" || val2==null|| isNaN(val2)){
			  jQuery("#validateError").text("");
			  jQuery("#validateError").text('<s:text name="otherInc"/>');
			  error=false;
		}  */
		 if(!isNaN(val1)&& !isNaN(val2) && !isNaN(val3)){
			totAmt = val1+val2+val3;
			}else if(!isNaN(val1) && isNaN(val2) && isNaN(val3)){
				totAmt = val1;	
			}else if(isNaN(val1) && !isNaN(val2) && isNaN(val3)){
				totAmt = val2;	
			}
			else if(isNaN(val1) && !isNaN(val2) && !isNaN(val3)){
				totAmt = val2+val3;	
			}
			else if(!isNaN(val1) && isNaN(val2) && !isNaN(val3)){
				totAmt = val1+val3;	
			}
			else if(!isNaN(val1) && !isNaN(val2) && isNaN(val3)){
				totAmt = val1+val2;	
			}
			else if(isNaN(val1) && isNaN(val2) && !isNaN(val3)){
				totAmt = val3;	
			}
			else{
				totAmt = 0.00;	
			}  
		 
		jQuery(this).find(".grossAgri").text(totAmt);
		incomeDatas+=getFormattedValue(val1)+"###"+getFormattedValue(val2)+"###"+getFormattedValue(val3)+"###"+cultiId+"@@@";

	});
	jQuery('#farmerIncomeDatas').val(incomeDatas);	
}

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}

function getFormattedValue(val){
	   if(val==null||val==undefined||val.toString().trim()=="" || isNaN(val)){
	    return " ";
	   }
	   return val;
	 }
	 
 

function onSubmit(){
	if(!error){
		$("#update").prop('disabled', false);
	}else{
		document.updateform.submit();
	}
   } 

</script>
	<font color="red"> <s:actionerror /></font>
	<s:form name="form" cssClass="fillform">

		<s:hidden key="command" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper margin-bottom">
						<div class="formContainerWrapper dynamic-form-con">
						 <div class="row">
						<div class="container-fluid">
							<div class="notificationBar">
								<div class="error">
									<p class="notification">
										<span class="manadatory">*</span>
										<s:text name="reqd.field" />
									<div id="validateError" style="text-align: center;"></div>
									</p>
								</div>

							</div>
						</div>
					</div> 

							<h2>
								<s:text name="info.farmer" />
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property value="%{getBranchName(filter.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
								<s:property value="%{getLocaleProperty('farmer.firstName')}" />	
								</p>
								<p class="flexItem">
									<s:property value="filter.farmer.firstName" />
									-
									<s:property value="filter.farmer.farmerId" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farm.farmName" />
								</p>
								<p class="flexItem">

									<s:property value="filter.farmName" />
									-
									<s:property value="filter.farmCode" />

								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="village" />
								</p>
								<p class="flexItem">
									<s:property value="filter.farmer.village.name" />
								</p>
							</div>

						</div>

					</div>
					<s:if test="farmerIncomeList.size()>0">
						<div class="appContentWrapper" style="width: 100%">
							<div class="formContainerWrapper dynamic-form-con">

								<h2>
									<s:text name="info.farmerIncome" />
								</h2>
								<table class="table table-bordered aspect-detail">
									<tr>
										<th><s:property
												value="%{getLocaleProperty('primaryIncome')}" /></th>
										<%-- <th><s:property
												value="%{getLocaleProperty('cottonIncome')}" /></th> --%>
										<th><s:property
												value="%{getLocaleProperty('interCropIncomeCrop')}" /></th>
										<th><s:text name="incomeOther"/></th>
										<th><s:property value="%{getLocaleProperty('agriGross')}" />
										</th>
									</tr>
									<s:iterator value="farmerIncomeList" id="bean">
										<tr class="farmerIncomeTR">

											<td><s:textfield value="%{#bean.agriIncome}" maxlength="8"
													class="agriIncome" onkeypress="return isDecimal(event)"
													id="agriIncome" onkeyup="calcFarmerIncome(this);" /></td>
											<%-- <td><s:property value="cottonIncome" /></td> --%>
											<%-- <td class="hide"><s:textfield id="cottonIncome" maxlength="8"
													value="%{#bean.cottonIncome}" class="cottonIncome" /></td> --%>
											<td><s:textfield value="%{#bean.interCropIncome}" maxlength="8"
													class="interCropIncome" onkeyup="calcFarmerIncome(this);"
													id="interCropIncome" onkeypress="return isDecimal(event)" /></td>
											<td><s:textfield value="%{#bean.otherIncome}" maxlength="8"
													class="otherIncome" onkeyup="calcFarmerIncome(this);"
													id="otherIncome" onkeypress="return isDecimal(event)" /></td>
											<td><s:label class="grossAgri"
													value="%{#bean.grossAgriIncome}" /></td>
											<td class="hide"><s:textfield class="cultivationId" maxlength="8"
													value="%{#bean.cultivationId}" /></td>

										</tr>
									</s:iterator>
								</table>
							</div>
							<div class="yui-skin-sam">
								<sec:authorize ifAllGranted="service.farmerIncome.update">
									<span id="update" class=""><span class="first-child">
											<button type="button" class="edit-btn btn btn-success" onclick="onSubmit();">
												<FONT color="#FFFFFF"> <b><s:text
															name="update.button" /></b>
												</font>
											</button>
									</span></span>
								</sec:authorize>


								<span id="cancel" class=""> <span class="first-child">
										<button type="button" class="back-btn btn btn-sts">
											<b><FONT color="#FFFFFF"><s:text
														name="back.button" /> </font></b>
										</button>
								</span>
								</span>
							</div>
						</div>
					</s:if>

				</div>
			</div>
		</div>



	</s:form>

	<s:form name="cancelform"
		action="farmerIncome_list.action?type=service">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="updateform"
		action="farmerIncome_update.action?type=service">
		<s:hidden key="currentPage" />
		<s:hidden id="farmerIncomeDatas" name="farmerIncomeDatas" />

	</s:form>
</body>
