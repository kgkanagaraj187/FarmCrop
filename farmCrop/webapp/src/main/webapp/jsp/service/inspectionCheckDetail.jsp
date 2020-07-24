<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script src="js/dynamicComponents.js?v=1.36"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>
<style>
.process_msg {
		display: none; /* Hidden by default */
		position: fixed; /* Stay in place */
		z-index: 1; /* Sit on top */
		padding-top: 100px; /* Location of the box */
		left: 0;
		top: 0;
		width: 100%; /* Full width */
		height: 100%; /* Full height */
		overflow: auto; /* Enable scroll if needed */
		background-color: rgb(0, 0, 0); /* Fallback color */
		background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
	}
	
	/* Modal Content */
	.process_msg-content {
		background-color: #fefefe;
		margin: auto;
		padding: 20px;
		border: 1px solid #888;
		width: 30%;
	}
	
	.process_msg-content_loading_msg {
		background-color: FFF;
		margin-left: 43%;
		/* padding: 10px;
	    border: 1px solid #888; */
		align: center;
		margin-top: 15%;
		color: white;
	}
	
	.close {
		color: #aaaaaa;
		float: right;
		font-size: 28px;
		font-weight: bold;
	}
	
	.close:hover, .close:focus {
		color: #000;
		text-decoration: none;
		cursor: pointer;
	}
	
	.formatStyleForPdf{
		/* padding-left: 10px;
		padding-top: 5px; */
		 padding: 10px;
		 font-size: 17px;
	}
	
	
</style>
<body>
	<s:hidden key="farmerDynamicData.id" id="farmerDynamicDataId"
		class="uId" />
	<s:hidden key="currentPage" />
		
<!--  <div style="float:right">
	<button id="downloadFile"  value="Download PDF" type="button">Download
		PDF</button>
		</div> -->
		
			<div id="loading_msg" class="process_msg">

		<!-- Modal content -->
		<div class="process_msg-content_loading_msg">
			<h3>Processing ...</h3>
		</div>

	</div>
		<div style="float:right">
			<button id="pdfDownloadBtn" onclick="exportPDFCa()" class="btn btn-warning">Download
			PDF</button>
			</div>
			<div style="clear:both"></div>
	<div id="pdf" class="pdff">
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
		function downloadPDF(){
			$('#pdfDownloadBtn').prop('disabled', true);
			//var modal = document.getElementById('loading_msg');
			//modal.style.display = "inline-block";
			$("#loading_msg").css("display","inline-block").promise().done(function(){ 
				console.log("blured");
				
			});
			
			
			var div = jQuery('<div/>', {
			id : "tempPDF"
			});
					 
		$("#pdf").before($(div)); 
			
			//$("#tempPDF").width("21cm");
 			//$("#tempPDF").height("29.7cm"); 
 			
 			
			var divCount = $("#pdf").find(".appContentWrapper.marginBottom").find(".formContainerWrapper").length;
			var farmer = '<s:property value="farmer" />';
			
			
		
				var count = 0;
			$("#pdf").find(".appContentWrapper.marginBottom").find(".formContainerWrapper").each(function(i, elm) {
				//var tempObj = $(elm).clone();
				$(elm).addClass("formatStyleForPdf");
				var title = $(elm).find(".aPanel").find(".titleHd").text();
				var id = parseInt(title);
				if(isEmpty(id) || isNaN(id)){
					id = 0;
				}
				html2canvas($(elm), {
					onrendered: function(canvas) {
						
						var whole_pdf = canvas.toDataURL("image/png");
						count = count+1;
						$('#tempPDF').append('<div class="appContentWrapper marginBottom"> <img id="pdfImages'+id+'" src="'+whole_pdf+'"  /> </div>');
							 
					}
					}).then(function (canvas) {
						if(count == divCount) {
							//alert("done --> "+count +"=="+ divCount)
							//console.log($('#pdfImages'+id).attr('src'));
							generatePDF();
						} 
						}); 
			}).promise().done( function(){ 
				console.log(" iteration done");
				
			});
			 
			
		}
		function generatePDF(){
			var breadCrumb = '<s:property value="infoName"/>';
		var divCount = $("#pdf").find(".appContentWrapper.marginBottom").find(".formContainerWrapper").length;
			var doc = new jsPDF("p", "cm", "a4");
			doc.deletePage(1);
			// doc.addPage("1300","2000");
			//doc.addPage("1250","1250");
			doc.addPage("1270", "1700");
			// doc.addPage(Math.floor($('#tempPDF').width()), Math.floor($('#tempPDF').height()));
			var space = 0;

			for (i = 0; i <= divCount - 1; i++) {
				var whole_pdf = $('#pdfImages' + i).attr('src');
				var tempSpace = space + $('#pdfImages' + i).height();
				if (tempSpace <= 1700) {
					if (i == 0) {
						doc.addImage(whole_pdf, 'PNG', 0, 0,
								$('#pdfImages' + i).width(),
								$('#pdfImages' + i).height());
						space = space + $('#pdfImages' + i).height();

					} else {

						doc.addImage(whole_pdf, 'PNG', 0, space, $(
								'#pdfImages' + i).width(), $('#pdfImages' + i)
								.height());
						space = space + $('#pdfImages' + i).height();

					}
				} else {
					doc.addPage("1270", "1700");
					doc.addImage(whole_pdf, 'PNG', 0, 0, $('#pdfImages' + i)
							.width(), $('#pdfImages' + i).height());
					space = 0;
					space = space + $('#pdfImages' + i).height();
				}
			}

			var modal = document.getElementById('loading_msg');
			modal.style.display = "none";
			doc.save(""+breadCrumb+".pdf");
			$("#tempPDF").remove();
			$(".formatStyleForPdf").removeClass("formatStyleForPdf");
			$('#pdfDownloadBtn').prop('disabled', false);

		}
		
		function edit() {
			var txnType = '<s:property value="getTxnType()"/>';
			var id = '<s:property value="farmerDynamicData.id"/>';
			var url = "dynamicCertification_update?txnType=" + txnType
					+ "&id=" + id;
			$("#form").attr("action", url);
			$("#form").submit();
		}
		
		function exportPDFCa(){
    		var printContents="";
    		/* $( ".pdff" ).each(function() {
    			printContents = printContents+$(this).html();
    			}); */
    		var originalContents = document.body.innerHTML;
    	
			var domele = $( ".pdff" );
			var imgtag;
			var div =  $('<div/>').attr({
					
			
				});
			$(domele).find(".imgTd").each(function() {

				$(this).find("button").each(function() {
	    			if($(this).find("i").hasClass("fa fa-picture-o")){
	    				console.log($(this).find("i").attr("class"))
	    				if($(this).attr("onclick").includes("enablePhotoModal")){
	    					var regExp = /\(([^)]+)\)/;
	    					var matches = regExp.exec($(this).attr("onclick"));
							var imgid = matches[1].substring(1,matches[1].length-1);
	    					
	    					$.ajax({
	    						 type: 'POST',
	    						  url: "farmer_populateDynamicImage.action",
	    						  async:false,
	    						  data: {
	    							  imgId : imgid
	    								},
	    								 success:  function(result) {
	    									 var imgdata = $.parseJSON(result);
	    									 imgtag = $('<img/>').attr({
				    										width : "80%",
				    										height : "350px",
				    										src : imgdata.img,
				    										"style":"max-width: 500px;"
													
														});
	    									 
	    									 $(div).append($(imgtag));
	    									
	    									}
	    									 
	    					});
	    				}
	    				
	    			}
	    			//$(this).parent().attr("style", "width: 100% !important");
	    			//$(this).parent().replaceWith($(imgtag));
	    			$(this).hide();
				});
			}).promise().done(function () { 
				 $(domele).append($(div).html());
				document.body.innerHTML = $(domele).html();
				window.print();
				document.body.innerHTML = originalContents;
			});
    		
    		 /*  var script = document.createElement('script');
         	  script.type = 'text/javascript';
         	  script.src = 'https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap';
         	
          
         	  document.body.appendChild(script); */
         	  
      		//console.log($(domele).html());
    		
    		
    	}
	</script>
</body>