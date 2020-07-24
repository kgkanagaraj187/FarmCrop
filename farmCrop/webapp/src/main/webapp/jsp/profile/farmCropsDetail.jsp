<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>

<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style>
#map {
        height: 700px;
      }

      
</style>
</head>
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="farmCropsMaster.id" id="farmCropMasterId" />
	<s:hidden key="command" />
	
						<div class="flex-view-layout">
						<div class="fullwidth">
							<div class="flexWrapper">
								<div class="flexLeft appContentWrapper">
									<div class="error">
										<s:actionerror />
										<s:fielderror />
									</div>
									<div class="formContainerWrapper">

										<div class="aPanel">
											<div class="aTitle">
												<h2>
													<s:property value="%{getLocaleProperty('info.farmCropsMaster')}" />
													
												</h2>
											</div>
									</div>
									<div class="aContent dynamic-form-con">
									<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmCropsMaster.name" />
													</p>
													<p class="flexItem">
														<s:property value="farmCropsMaster.name" />
													</p>
												</div>
									
									</div>
									
									
									
									
								</div>
								<!-- 	<div class="mapWrapper">
			<div class="mapOverlay">
		<div id="map" class="smallmap"></div>			
		</div>
		</div>	 -->
					
					<div id="map" class="smallmap map" style="height:500px" ></div>			
								
								
								
							</div>
						</div>
					</div>
				</div>

<div class="flexItem flex-layout flexItemStyle">
											<div class="">
												<sec:authorize ifAllGranted="profile.farmCrops.update">
													<span id="update" class=""><span class="first-child">
															<button type="button" class="edit-btn btn btn-success">
																<FONT color="#FFFFFF"> <b><s:text
																			name="edit.button" /></b>
																</font>
															</button>
													</span></span>
												</sec:authorize>

												<sec:authorize ifAllGranted="profile.farmCrops.delete">
													<span id="delete" class=""><span class="first-child">
															<button type="button" class="delete-btn btn btn-warning">
																<FONT color="#FFFFFF"> <b><s:text
																			name="delete.button" /></b>
																</font>
															</button>
													</span></span>
												</sec:authorize>

												<span id="cancel" class=""><span class="first-child">
														<button type="button" class="back-btn btn btn-sts">
															<b><FONT color="#FFFFFF"><s:text
																		name="back.button" /> </font></b>
														</button>
												</span></span>
											</div>
										</div>	
									
	
	
</s:form>

<s:form name="updateform" action="farmCropsMaster_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="farmCropsMaster_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="farmCropsMaster_list.action">
	<s:hidden key="currentPage" />
</s:form>


<script type="text/javascript">  
/* YAHOO.util.Event.addListener(window, "load", function() {
	function onUpdate(p_oEvent){     
		 document.updateform.id.value = document.getElementById('farmCropMasterId').value;
		 document.updateform.currentPage.value = document.form.currentPage.value;
		document.updateform.submit();
    }
 var button = new YAHOO.widget.Button("update", { onclick: { fn: onUpdate } });
   
 function onDelete(p_oEvent){     
		 if(confirm( '<s:text name="confirm.delete"/> ')){
			 jQuery(p_oEvent.target).attr("disabled",true);
			 document.deleteform.id.value = document.getElementById('farmCropMasterId').value;
			 document.deleteform.currentPage.value = document.form.currentPage.value;
			 document.deleteform.submit();
		}
    }
 var button = new YAHOO.widget.Button("delete", { onclick: { fn: onDelete } });
  
}); */

$('#update').on(
		'click',
		function(e) {
			document.updateform.id.value = document.getElementById('farmCropMasterId').value;
			 document.updateform.currentPage.value = document.form.currentPage.value;
			document.updateform.submit();
		});
$('#delete')
.on(
		'click',
		function(e) {
			if(confirm( '<s:text name="confirm.delete"/> ')){
				
				 document.deleteform.id.value = document.getElementById('farmCropMasterId').value;
				 document.deleteform.currentPage.value = document.form.currentPage.value;
				 document.deleteform.submit();
			}
		});
		
function initMap() {
	var localFilePath = '<s:property value="kmlFileLocation"/>';	
  // alert("AAAA : "+localFilePath);
	var map = new google.maps.Map(document.getElementById('map'), {
      zoom: 7,
      center: {lat: 14.5995124, lng:120.9842195}
    });
 
    var ctaLayer = new google.maps.KmlLayer({
        url: localFilePath,
        map: map
      });

    
  }  
</script>
<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.29&callback=initMap"></script>