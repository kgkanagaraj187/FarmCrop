<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script src="js/dynamicComponents.js?v=1.36"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>

<body>
	<s:hidden key="farmerDynamicData.id" id="farmerDynamicDataId"
		class="uId" />
	<s:hidden key="currentPage" />
		
<!--  <div style="float:right">
	<button id="downloadFile"  value="Download PDF" type="button">Download
		PDF</button>
		</div> -->
			<div style="clear:both"></div>
	<div id="pdf">
		<div class="appContentWrapper marginBottom">

			<div class="formContainerWrapper">
				<div class="aPanel">
					<div class="aTitle">
						<h2>
							<%-- <s:property value="%{getLocaleProperty('info.dynamicCertification')}" /> --%>
							<s:property value="infoName" />
						</h2>

					</div>
					<div class="aContent dynamic-form-con">
						<div class="dynamic-flexItem2 dateDiv">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('date')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmerDynamicData.txnDate" />
							</p>
						</div>
						<div class="dynamic-flexItem2 farmerDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('village')}" />
							</p>
							<p class="flexItem">
								<s:property value="selectedVillage" />
							</p>
						</div>
						<div class="dynamic-flexItem2 farmerDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.farmer')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmer" />
							</p>
						</div>
						<div class="dynamic-flexItem2 farmDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.farm')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmList" />
							</p>
						</div>

						<div class="dynamic-flexItem2 groupDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.group')}" />
							</p>
							<p class="flexItem">
								<s:property value="group" />
							</p>
						</div>

						<div class="dynamic-flexItem2 seasonClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.season')}" />
							</p>
							<p class="flexItem">
								<s:property value="season" />
							</p>
						</div>
						
						 <div class="dynamic-flexItem2 seasonClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('farmer.address')}" />
							</p>
							<p class="flexItem">
								<s:property value="address" />
							</p>
						</div>
						
						
						<div class="dynamic-flexItem2 seasonClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('farm.area')}" />
							</p>
							<p class="flexItem">
								<s:property value="area" />
							</p>
						</div> 
						
						<div class="dynamic-flexItem2 seasonClass">
							<p class="flexItem">
							<s:if test="entityType==4">
								<s:property
									value="%{getLocaleProperty('digitalSignature')}" />
									</s:if>
									<s:else>
									<s:property
									value="%{getLocaleProperty('digitalSignatures')}" />
									</s:else>
							</p>
							
							<p class="flexItem">
								<s:if test='digitalSignatureByteString!=null && digitalSignatureByteString!=""'>
													<img border="0" height="50px"
														src="data:image/png;base64,<s:property value="digitalSignatureByteString"/>">
												</s:if>
												
							</p>
						</div>
						
							<div class="dynamic-flexItem2 seasonClass">
							<p class="flexItem">
							<s:if test="entityType==4">
								<s:property
									value="%{getLocaleProperty('agentSignature')}" /></s:if>
									<s:else>
									<s:property
									value="%{getLocaleProperty('agentSignatures')}" />
									</s:else>
									
							</p>
							<p class="flexItem">
								<s:if test='agentSignatureByteString!=null && agentSignatureByteString!=""'>
								<img border="0" height="50px" src="data:image/png;base64,<s:property value="agentSignatureByteString"/>">
								</s:if>			
												
							</p>
						</div>
						<%-- <div class="dynamic-flexItem2 icsDetailDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.farmer')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmer" />
							</p>
						</div>
						
						
						<div class="dynamic-flexItem2 icsDetailDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.farm')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmList" />
							</p>
						</div> --%>
						
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.insDate')}" />
							</p>
							<p class="flexItem">
								<s:property value="insDate" />
							</p>
						</div>
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.inspectorName')}" />
							</p>
							<p class="flexItem">
								<s:property value="inspectorName" />
							</p>
						</div>
						
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.inspectorMobile')}" />
							</p>
							<p class="flexItem">
								<s:property value="inspectorMobile" />
							</p>
						</div>
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.insType')}" />
							</p>
							<p class="flexItem">
								<s:property value="insType" />
							</p>
						</div>
						
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.scope')}" />
							</p>
							<p class="flexItem">
								<s:property value="scope" />
							</p>
						</div>
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.totLand')}" />
							</p>
							<p class="flexItem">
								<s:property value="totLand" />
							</p>
						</div>
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.orgLand')}" />
							</p>
							<p class="flexItem">
								<s:property value="orgLand" />
							</p>
						</div>
						
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.totSite')}" />
							</p>
							<p class="flexItem">
								<s:property value="totSite" />
							</p>
						</div>
						<div class="dynamic-flexItem2 inspectClass">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('dynamicCertification.inspectionStatus')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmerDynamicData.inspectionStatus" />
							</p>
						</div>

						<s:if test="farmerDynamicData.conversionStatus==1">
							<div class="dynamic-flexItem2 icsDetailDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.icsType')}" />
								</p>
								<p class="flexItem">
									<s:property value="icsType" />
								</p>

							</div>

						</s:if>
						<s:else>

							<div class="dynamic-flexItem2 icsDetailDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.correctiveActionPlan')}" />
								</p>
								<p class="flexItem">
									<s:property value="correctiveActionPlan" />
								</p>

							</div>
						</s:else>
					</div>
				</div>
			</div>
		</div>
			<div class="appContentWrapper marginBottom">
			<div class="dynamicFieldsRender"></div>

		</div>
	</div>

	<%-- <button type="submit" class='btn btn-sts' onclick="edit()"><s:text name='Edit'/></button> --%>

	<%-- <sec:authorize ifAllGranted="service.dynamicCertification.update">
		<span id="update" class="hide"><span class="first-child">
				<button type="button"  onclick="edit()" class="edit-btn btn btn-success">
					<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
					</font>
				</button>
		</span></span>
	</sec:authorize>
 --%>
 	<button type="button"  onclick="edit()" class="edit-btn btn btn-success">
					<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
					</font>
				</button>
	<button type="submit" class='btn btn-sts' onclick="redirect()">
		<s:text name='Back' />
	</button>

	<s:form id="form">
	</s:form>
	<s:form id="audioFileDownload" action="farmer_populateVideoPlay">
		<s:hidden id="loadId" name="imgId" />
	</s:form>
	<s:form name="cancelform" action="farmer_detail.action">
	<s:hidden name="farmerId" />
	<s:hidden name="id" value="%{farmerUniqueId}" />
	<s:hidden name="tabIndex" value="%{tabIndexFarmer}" />
	<s:hidden name="currentPage" />
</s:form>
	<%-- <s:form name="updateform" action="dynamicCertification_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
		<s:hidden id="txnType" name="txnType" />
		<s:hidden id="reportType" name="entityType" />
	</s:form> --%>
	<%-- <jsp:include page="dynamicComp.jsp"></jsp:include> --%>

	<script src="js/dynamicComponents.js?v=1.36"></script>
	<script>
		var recordName = '';
		$(document)
				.ready(
						function() {
							var txnType = '<s:property value="getTxnType()"/>';
							var entityType = '<s:property value="entityType"/>';
							var seasonType = '<s:property value="seasonType"/>';
							if (seasonType == '0') {
								$(".seasonClass").hide();
							}
							if (window.location.href.indexOf("?txnType=") < 0) {
								var id = '<s:property value="id"/>'

								var urll = window.location.href + "?txnType="
										+ txnType + "&id=" + id;
					
								window.location.replace(urll)
							}
							renderDynamicDetailFeildsByTxnType();

							/* var url="dynamicCertification_list?txnType="+txnType+"&entityType="+entityType;*/
							var url="farmer_list.action"
							$(".breadCrumbNavigation").html('');
							$(".breadCrumbNavigation").append("<li><a href='#'>Profile </a></li>");
							$(".breadCrumbNavigation").append("<li><a href="+url+"> Farmer</a></li>"); 
							$(".farmDiv").hide();
							$(".farmerDiv").hide();
							$(".icsDetailDiv").hide();
							$(".groupDiv").hide();
							if (entityType == '4') {
								$(".farmDiv").show();
								$(".farmerDiv").show();
								//$(".groupDiv").show();
								$(".icsDetailDiv").show();
								$(".inspectClass").show();
								recordName = '<s:property value="farmList" />';
							} else if (entityType == '1' || entityType == '5') {
								$(".farmerDiv").show();
								recordName = '<s:property value="farmer" />';
								$(".inspectClass").hide();
							} else if (entityType == '2') {
								$(".farmerDiv").show();
								$(".farmDiv").show();
								$(".inspectClass").hide();
								recordName = '<s:property value="farmList" />';

							} else if (entityType == '3') {
								$(".groupDiv").show();
								$(".inspectClass").hide();
								recordName = '<s:property value="group" />';
							}

							

							$('#update')
									.on(
											'click',
											function(e) {
												document.updateform.id.value = document
														.getElementById('farmerDynamicDataId').value;

												document.updateform.submit();
											});
							var url = window.location.href;
							var command = $("#command").val();

							if (url.indexOf('?txnType=') < 0) {

								url = url
										+ '?txnType='
										+ txnType
										+ '&id='
										+ '<s:property value="farmerDynamicData.id"/>';

								$(".lanMenu").each(
										function() {
											var url1 = $(this).attr("href")
													+ '&url='
													+ encodeURIComponent(url);
											$(this).attr("href", url1);
										});
							}

							if (url.indexOf('Report_') > 0) {
								///alert(url.indexOf('Report='));
								$('#update').hide();
								$('#update').addClass("hide");

							} else {
								$('#update').show();
								$('#update').removeClass("hide");
							}
							$('#downloadFile')
									.on(
											'click',
											function() {
												var vv = '<s:property value="farmerDynamicData.txnUniqueId" />';
												var source = window.document
														.getElementById("pdf");
												$('.photoPdfImg').css(
														"display", "block");

												$(':button').css("display",
														"none");

												var count = true;
												jQuery('.photoPdfImg')
														.each(
																function() {
																	if (this.src.length > 0) {
																		count = false;
																	}
																});

												if (count) {
													$('.photoPdfImg').remove();
													$('.photoPdf').trigger(
															"click");
												}
													
																				 var divHeight = $('#pdf').height();
																				 var divWidth = $('#pdf').width();
																				 var ratio = divHeight / divWidth;
																				 html2canvas(document.getElementById("pdf"), {
																				      height: divHeight,
																				      width: divWidth,
																				      onrendered: function(canvas) {
																				    	  var width = canvas.width;
																			                var height = canvas.height;
																			                var millimeters = {};
																			                millimeters.width = Math.floor(width * 0.264583);
																			                millimeters.height = Math.floor(height * 0.264583);

																			                var imgData = canvas.toDataURL(
																			                    'image/png');
																			                var doc = new jsPDF("l", "mm", "a4");
																			                doc.deletePage(1);
																			                doc.addPage(millimeters.width, millimeters.height);
																			                doc.addImage(imgData, 'PNG', 0, 0);
																			              //  doc.save(recordName + "_"+ vv + ".pdf"); 
																			                
																			                var blob = doc.output("blob");
																							window.open(URL.createObjectURL(blob));
/* 
																				        	var imgData = canvas.toDataURL('image/png');

																				        	var doc = new jsPDF('l', 'pt', 'letter');
																				      	  var imgWidth = doc.internal.pageSize.width;    
																				        	var pageHeight = doc.internal.pageSize.height;
																				        	var imgHeight = canvas.height * imgWidth / canvas.width;
																				        	var heightLeft = imgHeight;
																				        	var position = 10;

																				        	doc.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
																				        	heightLeft -= pageHeight;

																				        	while (heightLeft >= 15) {
																				        	  position = heightLeft - imgHeight;
																				        	  doc.addPage();
																				        	  doc.addImage(imgData, 'PNG', 10, position-10, imgWidth, imgHeight);
																				        	  heightLeft -= pageHeight;
																				        	  
																				        	}
																				        	doc.save(recordName + "_"+ vv + ".pdf"); */
																				      
																				      }
																				 });
												$(':button').css("display",
														"block");

												$('.photoPdfImg').css(
														"display", "none");

											});

							
						});

		function getTxnType() {
			var txnType = '<s:property value="getTxnType()"/>';
			return txnType;
		}
		
        
        function getBranchIdDyn(){
       		if('<s:property value="getBranchId()"/>'==null ||'<s:property value="getBranchId()"/>'=='' ){
       			return '<s:property value="farmerDynamicData.branchId"/>';
       		}else{
       			return '<s:property value="getBranchId()"/>';
       		}
       	}
		
		function getEntityType() {
			var entityType = '<s:property value="getEntityType()"/>';
			return entityType;
		}
		function getId() {
			var type = '<s:property value="id"/>';
			return type;
		}

		function redirect() {
			document.cancelform.submit();
		}
		
		function edit() {
			var txnType = '<s:property value="getTxnType()"/>';
			var id = '<s:property value="farmerDynamicData.id"/>';
			var url = "dynamicCertification_update?txnType=" + txnType
					+ "&id=" + id;
			$("#form").attr("action", url);
			$("#form").submit();
		}
		
	</script>
</body>