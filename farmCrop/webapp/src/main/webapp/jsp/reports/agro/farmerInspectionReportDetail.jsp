<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

<style>			
			#overlay {
				position:fixed; 
				top:0;
				left:0;
				width:100%;
				height:100%;
				background:#000;
				opacity:0.5;
				filter:alpha(opacity=50);
			}

			#modal {
				position:absolute;
				background:#565656 0 0 repeat;
				background:rgba(0,0,0,0.2);
				border-radius:5px;
				padding:8px;
			}

			#content {
				border-radius:8px;
				background:#fff;
				padding:20px;
				height:auto;
				width:250px;
				height:200px;
				font-family: Arial;
				font-size: 12px;				
			}
			
			#content img{
				width:250px;
				height:200px;	
			}
			
			button {
				border:solid 1px #565656;
				margin:10px 0 0 0px;
				cursor:pointer;
				font-size:12px;
				padding:5px;
			}
			.thClass{
				width:9.2%;
			}
		</style>

</head>
<s:hidden key="farmerInspectionReport.id" id="farmerInspectionReportId" />
<font color="red"> <s:actionerror /></font>

<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="farmerInspectionReport.id" />
	<s:hidden key="id" />
	<s:hidden key="command" />
	<s:hidden name="farmerNameAndId"/>
	<s:hidden name="farmNameAndId"/>
		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<h2><s:text name="info.farmerInspectionReport" /></h2>
							<div class="dynamic-flexItem">
								<p class="flexItem"><s:text name="farmerCropProdAnswers.farmer" /></p>
								<p class="flexItem"><s:if test="farmerCodeEnabled==1"><s:property value="farmerNameAndId" /></s:if>
													<s:else><s:property value="farmerName" /></s:else></p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem"><s:text name="farmerCropProdAnswers.farm" /></p>
								<p class="flexItem"><s:property value="farmNameAndId" /></p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem"><s:text name="farmerCropProdAnswers.answeredDate" /></p>
								<p class="flexItem"><s:property value="answeredDate" /></p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	<br />
<div class="appContentWrapper marginBottom">
	<table class="table table-bordered aspect-detail mainTable">

		<tr class="odd">
			<th width="2%"><s:text name="" /></th>
			<th width="9%"><s:text name="serialNo" /></th>
			<th colspan="5"><s:text name="sections" /></th>
		</tr>

		
			<s:iterator var="sectionAnswers" status="headerStatus"
				value="farmerCropProdAnswers.farmersSectionAnswers">
				
				<tr>
					<th style="padding-left:0;padding-right:0;">
						<a id="add<s:property value="#headerStatus.index"/>" href="javascript:header('<s:property value="#headerStatus.index"/>')" class="glyphicon glyphicon-plus">							
						</a>
					</th>
				
					<th>
						<s:property value="#sectionAnswers.serialNo" />
					</th>
					<th colspan="5">
						<s:property value="#sectionAnswers.sectionName" />
					</th>	
				</tr>
				
					<tr id="tr<s:property value="#headerStatus.index"/>" style="display:none;" class="trClass odd">
					
						<td width="95%" colspan="7" style="padding:0px;border-bottom: none !important;">
						
							<table class="table table-bordered aspect-detail" id="tab" width="100%" cellspacing="0" style="font-weight:normal;table-layout: fixed;">
								
								<tr id="headerId<s:property value="#headerStatus.index"/>">
									<th width="2%"><s:text name="" /></th>
					 				<th class="thClass"><s:text name="serialNo" /></th>
									<th width="27%"><s:text name="questions" /></th>
									<th width="8%"><s:text name="answers" /></th>
									<th width="32%"><s:text name="commentsOrExplanation" /></th>
									<th width="12%" class="needFollowUp"><s:text name="needFollowUp" /></th>
									<th width="7%"><s:text name="image" /></th>
								</tr>						
																				
								<s:iterator var="questionAnswers"
									value="#sectionAnswers.farmersQuestionAnswers">
							
									<s:if test="#questionAnswers.questionType != null">	
									
									<script>
									//hide the main header based on inner header
									$("#headerId"+<s:property value="#headerStatus.index"/>).hide();								
									</script>
							
										<tr class="odd">
											<td width="2%" class="break-word"></td>
											<td colspan="4">
												<table width="95%" cellspacing="0" style="table-layout: fixed;" class="baseTable">
												
													<s:if test="#questionAnswers.questionType eq 'ST0001_03_1'">
														<tr class="odd">
															<th width="30%"><s:text name="Corrective action agreed" /></th>
															<th width="50%"><s:text name="Implementation" /></th>
															<s:if test="#questionAnswers.image != null">
																<th width="9%"><s:text name="needFollowUp" /></th>
																<th width="6%">
																	<div class="yui-skin-sam"><span class="">
																	<span class="first-child">
																	<button
																		onclick="getImage(<s:property value="#questionAnswers.id"/>)"
																		type="button" class="fa fa-picture-o"><%-- <FONT color="#FFFFFF"><s:text
																		name="image.button" /> </font> --%></button>
																	</span> </span></div>	
																</th>
															</s:if>
															<s:else>
															<th width="15%"><s:text name="needFollowUp" /></th>	
															</s:else>
														</tr>
													</s:if>											
													<s:elseif test="#questionAnswers.questionType eq 'ST0001_06_1'">
														<tr class="odd">
															<th rowspan="2" width="30%"><s:text name="Crop" /></th>
															<th colspan="3" width="25%"><s:text name="Production last season (indicate units!)" /></th>
															<th colspan="2" width="25%"><s:text name="Production this season (expected)" /></th>
															<s:if test="#questionAnswers.image != null">
															<th rowspan="2" width="9%"><s:text name="needFollowUp" /></th>
																<th rowspan="2" width="6%">
																	<div class="yui-skin-sam"><span class="">
																	<span class="first-child">
																	<button
																		onclick="getImage(<s:property value="#questionAnswers.id"/>)"
																		type="button" class="fa fa-picture-o"><%-- <FONT color="#FFFFFF"><s:text
																		name="image.button" /> </font> --%></button>
																	</span> </span></div>	
																</th>
															</s:if>
															<s:else>
															<th rowspan="2" width="15%"><s:text name="needFollowUp" /></th>
															</s:else>
														</tr>
														<tr class="odd">
															<th width="10%"><s:text name="Total" /></th>
															<th width="10%"><s:text name="Per hectare or per tree" /></th>
															<th width="5%"><s:text name="Really Sold" /></th>
															<th width="15%"><s:text name="Total" /></th>
															<th width="10%"><s:text name="Per hectare or per tree" /></th>
														</tr>
													</s:elseif>	
																		
														<s:iterator var="answers" value="#questionAnswers.answers">
															
															<tr class="odd">
																<td  class="break-word"><s:property value="#answers.answer" /></td>
																<td  class="break-word"><s:property value="#answers.answer1" /></td>	
																														
																<s:if test="#questionAnswers.questionType eq 'ST0001_06_1'">
																<td  class="break-word"><s:property value="#answers.answer2" /></td>
																<td  class="break-word"><s:property value="#answers.answer3" /></td>
																<td  class="break-word"><s:property value="#answers.answer4" /></td>
																<td  class="break-word"><s:property value="#answers.answer5" /></td>
																</s:if>												
																<td class="break-word needFollowUp">
																	<s:property
																		value="getText('val'+#answers.followUp)" />
																		
																</td>													
															</tr>
														</s:iterator>
												</table>
											</td>
										</tr>						
									</s:if>
								<s:else>
								
									<tr>
										<td width="2%" class="break-word">		
										</td>
									
										<td width="9%" class="break-word">
											<s:property value="#questionAnswers.serialNo" />
										</td >
										
										<td width="27%" class="break-word">
											<s:property value="#questionAnswers.questionName" />
										</td>
			
										
										<s:iterator var="answers"
											value="#questionAnswers.answers">
											<td width="8%" class="break-word">
											<s:property value="getText('ans'+#answers.answer)" />
											<br /></td>
											<td width="32%" class="break-word">
											<s:if test="#answers.answer1 != null">
												<s:property value="getText(#answers.answer1)" />
											</s:if>
											<br /></td>
										</s:iterator>
			
										<td width="12%" class="break-word needFollowUp">
											<s:if test="#questionAnswers.followUp != null">
												<s:property
													value="getText('val'+#questionAnswers.followUp)" />
											</s:if>	
										</td>
											
										<td width="7%">
											<s:if test="#questionAnswers.image != null">
												<div class="yui-skin-sam"><span class="">
												<span class="first-child">
												<button
													onclick="getImage(<s:property value="#questionAnswers.id"/>)"
													type="button" class="fa fa-picture-o"><%-- <FONT color="#FFFFFF"><s:text
													name="image.button" /> </font> --%></button>
												</span> </span></div>	
											</s:if>
										</td>
										
								<!-- If Question has Sub Questions this part will work -->
										<s:if
											test="#questionAnswers.subQuestions != null">
			
											<s:iterator var="subQuestionAnswers"
												value="#questionAnswers.subQuestions">
			
												<tr>
													<td width="2%" class="break-word">		
													</td>
													
													<td width="9%" class="break-word">
														<s:property value="#subQuestionAnswers.serialNo" />
													</td>
														
													<td width="27%" class="break-word">
														<s:property value="#subQuestionAnswers.questionName" />
													</td>
			
												
													<s:iterator var="answers"
														value="#subQuestionAnswers.answers">
														<td width="8%" class="break-word">
														<s:property value="getText('ans'+#answers.answer)" />
														</td>
														<br />
														<td width="32%" class="break-word">
														<s:if test="#answers.answer1 != null">
															<s:property value="getText(#answers.answer1)" />
														</s:if></td>
														<br />
													</s:iterator>
													
									<%-- <s:iterator var="answers"
										value="#questionAnswers.answers">
										<td width="8%" class="break-word">
											<s:property value="getText('ans'+#answers.answer)" />
										</td>
										<td width="32%" class="break-word">										
											<s:property value="#answers.answer1" />										
										</td>
									</s:iterator> --%>	
			
													<td width="12%" class="break-word needFollowUp">
														<s:if test="#subQuestionAnswers.followUp != null">
															<s:property
																value="getText('val'+#subQuestionAnswers.followUp)" />
														</s:if>
													</td>
			
													<td width="7%">
														<s:if test="#subQuestionAnswers.image != null">
															<div class="yui-skin-sam"><span class="">
															<span class="first-child">
															<button
																onclick="getImage(<s:property value="#subQuestionAnswers.id"/>)"
																type="button" class="fa fa-picture-o"><%-- <FONT
																color="#FFFFFF"><s:text name="image.button" /> </font> --%></button>
															</span> </span></div>
														</s:if>
													</td>
												</tr>		
											</s:iterator>		
										</s:if>
									</tr>
								</s:else>
							</s:iterator>
						</table>
					</td>
				</tr>	
			</s:iterator>
		</table>
		</div>
	<br />


	<div class="yui-skin-sam"><span id="fair" class=""><span
		class="first-child">
	<button type="button" class="back-btn btn btn-sts" onclick="clickBack()"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>
	
		<div class="yui-skin-sam"><span id="rspo" class=""><span
		class="first-child">
	<button type="button" class="back-btn btn btn-sts" onclick="clickBack1()"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>
	
	<s:hidden name="type"/>

</s:form>
<s:form name="cancelform" action="farmerInspectionReport_list">
	<s:hidden key="currentPage" />
	<s:hidden name="filterMapReport" id="filterMapReport" />
	<s:hidden name="postdataReport" id="postdataReport" />
</s:form>
<s:form name="cancelFairtradeform" action="farmerInspectionReport_list.action?type=FAIR_TRADE">
	<s:hidden key="currentPage" />
	<s:hidden name="filterMapReport" id="filterMapReport" />
	<s:hidden name="postdataReport" id="postdataReport" />
</s:form>
<s:form name="cancelrspoform" action="farmerInspectionReport_list.action?type=RSPO">
	<s:hidden key="currentPage" />
	<s:hidden name="filterMapReport" id="filterMapReport" />
	<s:hidden name="postdataReport" id="postdataReport" />
</s:form>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document" style="width: 300px;">
		<div class="modal-content">
			<img src="" id="img" class="img-rounded center-block"
				style="width: 100%;" height=250 />
		</div>
		<div class="modal-footer">
			<center>
				<button type="button" class="btn btn-sts" data-dismiss="modal">OK</button>
			</center>
		</div>
	</div>
</div>


<script type="text/javascript">


function header(status){
	/*$('.trClass').hide();	
	if($("#add"+status).attr('class')=='plus'){
		$("#tr"+status).show();
		$('.minus').attr('class','plus');
		$("#add"+status).attr('class','minus');
	}else{
		$("#tr"+status).hide();
		$("#add"+status).attr('class','plus');
	}*/

	if($("#add"+status).attr('class')=='glyphicon glyphicon-plus'){
		$("#tr"+status).show();
		//$('.minus').attr('class','plus');
		$("#add"+status).attr('class','glyphicon glyphicon-minus');
	}else{
		$("#tr"+status).hide();
		$("#add"+status).attr('class','glyphicon glyphicon-plus');
	}

}

function getImage(id){
	//alert(id);
	$('#myModal').modal('show');
	 $('#myModal img').attr('src',"farmerInspectionReport_getImage.action?id="+id+"&dt="+new Date());
	//modal.open({src: "farmerInspectionReport_getImage.action?id="+id+"&dt="+new Date()});
	 // $('#img').attr('src',"farmerInspectionReport_getImage.action?id="+id+"&dt="+new Date());
	 // $('#popup').show();
	 //alert(id);
 }

	 
 var modal = (function(){
		var 
		method = {},
		$overlay,
		$modal,
		$content,
		$src,
		$close;

		// Center the modal in the viewport
		method.center = function () {
			var top, left;

			top = Math.max($(window).height() - $modal.outerHeight(), 0) / 2;
			left = Math.max($(window).width() - $modal.outerWidth(), 0) / 2;

			$modal.css({
				top:top + $(window).scrollTop(), 
				left:left + $(window).scrollLeft()
			});
		};

		// Open the modal
		method.open = function (settings) {
			//$content.empty().append(settings.content);
			$src = settings.src;
			$modal.css({
				width: settings.width || 'auto', 
				height: settings.height || 'auto'
			});

			method.center();
			$(window).bind('resize.modal', method.center);
			$modal.show();
			$overlay.show();
			$('#img').attr('src',$src);
		};

		// Close the modal
		method.close = function () {
			$modal.hide();
			$overlay.hide();
			//$content.empty();
			$(window).unbind('resize.modal');
		};

		// Generate the HTML and add it to the document
		$overlay = $('<div id="overlay"></div>');
		$modal = $('<div id="modal"></div>');
		$content = $('<div id="content"><img id="img" src="" /></div>');
		$close = $('<a id="close" href="#">close</a>');
		$btn = $('<div style="width:100%; text-align:center;"><button id="clk">OK</button></div>');

		$modal.hide();
		$overlay.hide();
		$modal.append($content, $btn);

		$(document).ready(function(){
			var filterMapReport='<s:property value="filterMapReport"/>';
			var postdataReport= '<s:property value="postdataReport"/>';
			$('body').append($overlay, $modal);
			var type="<s:property value='type'/>";
			if(type=='FAIR_TRADE'){
				$(".breadCrumbNavigation").html('');
				$(".breadCrumbNavigation").append("<li><a href='#'>Report</a></li>");
				$(".breadCrumbNavigation").append("<li><a href='#'>Farmer Inspection Report</a></li>");
				$(".breadCrumbNavigation").append("<li><a href='farmerInspectionReport_list.action?type=FAIR_TRADE'>FAIR_TRADE</a></li>");
				$("#type").val('FAIR_TRADE');
				$("#fair").show();
				$("#rspo").hide();
				$('.needFollowUp').hide();
			}
			
			if(type=='RSPO'){
				$(".breadCrumbNavigation").html('');
				$(".breadCrumbNavigation").append("<li><a href='#'>Report</a></li>");
				$(".breadCrumbNavigation").append("<li><a href='#'>Farmer Inspection Report</a></li>");
				$(".breadCrumbNavigation").append("<li><a href='farmerInspectionReport_list.action?type=RSPO'>RSPO</a></li>");
				$("#type").val('RSPO');
				$("#rspo").show();
				$("#fair").hide();
				$('.needFollowUp').hide();
			}
		});

		$btn.click(function(e){
			e.preventDefault();
			method.close();
		});

		return method;
	}());

	// Wait until the DOM has loaded before querying the document
	$(document).ready(function(){
		/*$('a#click').click(function(e){
			modal.open({src: "http://vanimg.s3.amazonaws.com/good-logos-1.jpg"});
			e.preventDefault();
		});	*/	
	});
	
	function clickBack(){
		document.cancelFairtradeform.submit();
			}
		
	function clickBack1(){
		document.cancelrspoform.submit();
		
		}
	
</script>

