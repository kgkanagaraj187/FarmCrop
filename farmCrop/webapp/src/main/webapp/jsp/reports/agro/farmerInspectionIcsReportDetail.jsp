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
				/*background:#565656 0 0 repeat;*/
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
<s:hidden key="farmerInspectionIcsReport.id" id="farmerInspectionIcsReportId" />
<font color="red"> <s:actionerror /></font>

<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="farmerInspectionIcsReport.id" />
	<s:hidden key="id" />
	<s:hidden key="command" />
	<s:hidden name="farmerNameAndId"/>
	<s:hidden name="farmNameAndId"/>
	<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<h2><s:text name="info.farmerInspectionIcsReport" /></h2>
							<div class="dynamic-flexItem">
								<p class="flexItem"><s:text name="farmerCropProdAnswers.farmer" /></p>
								<p class="flexItem"><s:if test="farmerCodeEnabled==1">
									<s:property value="farmerNameAndId" /></s:if>
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
	<table class="table table-bordered aspect-detail mainTable" >
		<tr class="odd">
			<th width="2%"><s:text name="" /></th>
			<th width="9%"><s:text name="serialNo" /></th>
			<th colspan="5"><s:text name="sections" /></th>
		</tr>
			<s:iterator var="sectionAnswers"  status="headerStatus"
				value="farmerCropProdAnswers.farmersSectionAnswers">
				
				<tr class="odd">	
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
						
							<tr>
								<th width="2%"><s:text name="" /></th>
								<th class="thClass"><s:text name="serialNo" /></th>
								<th width="27%"><s:text name="questions" /></th>
								<th width="8%"><s:text name="answers" /></th>
								<th width="32%"><s:text name="commentsOrExplanation" /></th>
								<th width="12%"><s:text name="gpsDateAndTime" /></th>
								<th width="7%"><s:text name="image" /></th>
							</tr>		
						
							<s:iterator var="questionAnswers"
							value="#sectionAnswers.farmersQuestionAnswers">	
								<tr> 
								
									<td width="2%" class="break-word">		
									</td>
									
									<td width="9%" class="break-word">
										<s:property value="#questionAnswers.serialNo" />
									</td>
									
									<td width="27%" class="break-word">
									<s:if test="#questionAnswers.registeredQuestion!=0">
										<s:property value="#questionAnswers.questionName" />
										</s:if>
									    <s:else> 
									    <s:text name="OTHER (SPECIFY)" /><br>
									    <s:property value="#questionAnswers.questionName" />
									    </s:else>
									</td>
		
									<s:iterator var="answers"
										value="#questionAnswers.answers">
										<s:if test="#questionAnswers.serialNo!=5.6">
										<td width="8%" class="break-word">
											<%--  <s:property value="getText('icsans'+#answers.answer)" />  --%>
											 <s:property value="getText(#answers.answer)" /> 
										</td>
										<td width="32%" class="break-word">										
											<s:property value="#answers.answer1" />										
										</td>
										</s:if>
										<s:else>
										<td width="8%" class="break-word">										
											<s:property value="#answers.answer" />										
										</td>
										<td width="32%" class="break-word">										
										</td>
										</s:else>
									</s:iterator>	
									
									<td width="12%" class="break-word">
										<s:date name="#questionAnswers.GPSCaptureDateTime" format="dd-MM-yyyy HH:mm:ss"/>									
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
								</tr>							
							</s:iterator>
						</table>
					</td>
				</tr>
			</s:iterator>
	</table>
	</div>
	<br />
	<div class="yui-skin-sam"><span id="cancel" class=""><span
		class="first-child">
	<button type="button" class="back-btn btn btn-sts"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>

</s:form>
<s:form name="cancelform" action="farmerInspectionIcsReport_list">
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
	 $('#myModal img').attr('src',"farmerInspectionIcsReport_getImage.action?id="+id+"&dt="+new Date());
	//modal.open({src: "farmerInspectionIcsReport_getImage.action?id="+id+"&dt="+new Date()});
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
	 
</script>