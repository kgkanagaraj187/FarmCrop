<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<div class="appContentWrapper marginBottom">
	<div class="error"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" /></div>
	<s:form name="form" cssClass="fillform" enctype="multipart/form-data"
		action="periodicInspectionServiceReport_%{command}?type=service">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="periodicInspection.id" id="inspectionId" />
		</s:if>
		<s:hidden key="command" />
		<div class="formContainerWrapper">
		<div class="flexiWrapper">
		<table class="table table-bordered aspect-detail">
			<tr>
				<th colspan="2"><s:text name="info.inspection" /></th>
			</tr>

			<s:if test='"update".equalsIgnoreCase(command)'>

				<tr class="odd">
					<td><s:text name="inspectionDate" /></td>
					<td><s:date name="periodicInspection.inspectionDate"
							format="dd/MM/yyyy hh:mm:ss" /></td>

				</tr>
				<tr class="odd">
					<td><s:text name="farmerIdNAme" /></td>
					<td width="65%"><s:property
							value="periodicInspection.farm.farmer.farmerIdAndName" />&nbsp;</td>
				</tr>

				<tr class="odd">
					<td><s:text name="farmIdName" /></td>
					<td width="65%"><s:property
							value="periodicInspection.farm.farmIdAndName" />&nbsp;</td>
				</tr>
				
				<tr class="odd">
			        <td width="30%"><s:text name="cropName" /></td>
			        <td width="40%"><s:property value="periodicInspection.cropCode" /></td>

		        </tr>

				<tr class="odd">
					<td><s:text name="fieldstaffIdName" /></td>
					<td width="40%"><s:property
							value="periodicInspection.createdUserName" />-<s:property
							value="%{mobileUserName}" /></td>
				</tr>

				<tr class="odd">
					<td width="35%"><s:text name="purpose" /><sup
						style="color: red;">*</sup></td>
					<td width="65%"><s:textfield id="purposeId"
							name="periodicInspection.purpose" theme="simple" maxlength="35" /></td>
				</tr>

				<tr class="odd">
					<td><s:text name="fertilizerApp" /></td>
					<td width="40%"><s:radio id="fertilizer" list="radioValueList"
							name="periodicInspection.isFertilizerApplied" listKey="key"
							listValue="value" theme="simple"
							onchange="enableSafetyDisposal(this.value);" /></td>
				</tr>

				<tr class="odd">
					<td class="disposalLabel"><s:text name="safetyDisposal" /></td>
					<td class="disValue" width="40%"><s:radio id="disposal"
							list="radioValueList"
							name="periodicInspection.isFieldSafetyProposal" listKey="key"
							listValue="value" theme="simple" /></td>
				</tr>

				<tr class="odd">
					<td width="35%"><s:text name="remarks" /></td>
					<td width="65%"><s:textfield name="periodicInspection.remarks"
							theme="simple" maxlength="35" /></td>

				</tr>

				<%-- <tr class="odd">
					<td width="35%"><s:text name="inspectionImage" /></td>
					<td width="25%">
					
						<div class="yui-skin-sam" style="text-align: center;">
						<s:iterator value="periodicInspectionImageList" var="img" status="rowStatus">
						    <s:file title="Add Image" id='imageFile%{#rowStatus.index}' type="file"
								name="periodicInspectionImageList[%{#rowStatus.index}].imageFile" onchange="validateImageDetail(this.id);" />
								<s:hidden value="%{imageByteString}" name="periodicInspectionImageList[%{#rowStatus.index}].imageByteString"/>
						</s:iterator>
							
						</div>

					</td>

				</tr> --%>


				<tr class="odd">
					<td width="35%"><s:text name="inspectionAudio" /></td>
					<td width="65%"><s:file name="inspectionAudio"
							id="inspectAudio" type="file" onchange="validateAudio()"/>
						<div style="font-size: 10px">
							<s:text name="inspection.audioTypes" />
							<%-- <font color="red"> <s:text name="imgSizeMsg" /></font> --%>
						</div></td>
				</tr>

			</s:if>
		</table>
		<br />
		<div class="yui-skin-sam">

			<s:if test="command =='update'">
				<span id="update" class=""><span class="first-child">
						<button type="submit" id="buttonUpdate" onclick="onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><div>
										<s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if>
			<span id="cancel" class=""><span class="first-child"><button
						type="button" class="cancel-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
						</font></b>
					</button></span></span>
		</div>
		</div>
		</div>
		
	</s:form>
</div>
	<s:form name="updateform"
		action="periodicInspectionServiceReport_updateNeed.action?type=service">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform"
		action="periodicInspectionServiceReport_list.action?type=service">
		<s:hidden key="currentPage" />
	</s:form>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('#buttonUpdate')
									.on(
											'click',
											function(e) {
												/* var getPurpose = $('#purposeId')
														.val(); */
												//if (getPurpose) {
													document.updateform.id.value = document
															.getElementById('inspectionId').value;
													document.updateform.currentPage.value = document.form.currentPage.value;
													document.updateform
															.submit();
												/* }else {
													//alert('<s:text name="empty.purpose"/>');
													e.preventDefault();
													return false;
												} */

											});

							/* var ferVal = $("input[name='periodicInspection.isFertilizerApplied']:checked").val();
							if (ferVal == 1) {
								$('.disposalLabel').show();
								$('.disValue').show();
							} else {
								$('.disposalLabel').hide();
								$('.disValue').hide();
							} */
							
							enableSafetyDisposal('<s:property value="periodicInspection.isFertilizerApplied"/>');

						});

		function enableSafetyDisposal(val) {
			if (val == 'Y') {
				$('.disposalLabel').show();
				$('.disValue').show();
			} else {
				$('.disValue').val('');
				$('.disposalLabel').hide();
				$('.disValue').hide();
			}
		}

		function checkImgHeightAndWidth(val) {

			var _URL = window.URL || window.webkitURL;
			var img;
			var file = document.getElementById('inspectImage').files[0];

			if (file) {

				img = new Image();
				img.onload = function() {
					imgHeight = this.height;
					imgWidth = this.width;
				};
				img.src = _URL.createObjectURL(file);
			}
		}
		
		function validateAudio(){
			var isError = true;
			var file = document.getElementById('inspectAudio').files[0];
		        var filename = document.getElementById('inspectAudio').value;
		        var size = document.getElementById('inspectAudio').files[0].size;
		        var fileExt = filename.split('.').pop();
		        if (file != undefined) {
				
		            if (fileExt !='mp3') {
		            	alert('<s:text name="invalidVideoFileExtension"/>');
		            	$('#buttonUpdate').prop('disabled', true);
		            	file.focus();
		            
		            	
		              }
		            else{
		            	if(parseInt(size)>2097152){
		            		alert('<s:text name="invalidVideoFileSize"/>');
		            		$('#buttonUpdate').prop('disabled', true);
		                	file.focus();
		            	}
		            	else
			        	   $('#buttonUpdate').prop('disabled', false);
			        }
		            }
		        
		       
			return true;
		}
		
		function onSubmit(){
			
			
			document.form.action="periodicInspectionServiceReport_updateNeed.action?type=service";
			
		     
			
		}
	</script>
</body>