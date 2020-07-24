<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="swithlayout">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" href="bootstrap.min.css">
<script src="jquery.min.js"></script>
<script src="bootstrap.min.js"></script>
<script src="js/bootpopup.js"></script>
<script src="js/bootpopup.min.js"></script>

</head>

<body>

<script type="text/javascript">
	$(document).ready(function() {
		$("#dsc_id").val(<s:property value="dsc.id"/>);
		$("#dsc_sectionCode").val("<s:property value="dsc.sectionCode"/>");
		$("#dsc_id_delete").val(<s:property value="dsc.id"/>);
		$("#dsc_sectionCode_delete").val("<s:property value="dsc.sectionCode"/>");
		
	});		
	
	function cancelFormSubmit() {
		$("#cancelForm").submit();
	}
	
	function deleteFormSubmit(){
		$("#deleteForm").submit();
	}

	
	
	//section related
	
	function sectionFormSubmit() {
		var sectionName=$("#sectionName").val()
		if (sectionName !="" ) {
			$("#sectionForm").submit();
			//alert(sectionName +" saved successfully");
		} else {
			alert("Please enter the section name");
		}
	}
	
	//field related
	
</script>			

<!--section grid  -->
<s:form name="updateform" action="creationTool_detail">
	 <s:hidden name="id" /> 
	 <s:hidden  name="currentPage" /> 
</s:form>

<s:form id="deleteForm" action="creationTool_deleteSection">
	<s:hidden id="dsc_id_delete" name="dsc.id"  />
	<s:hidden id="dsc_sectionCode_delete" name="dsc.sectionCode"  />
</s:form>

<!-- common -->
<s:form id="cancelForm" action="creationTool_list"></s:form>


<div class="appContentWrapper marginBottom" >
 <ul class="nav nav-pills">
    <li class="active"><a data-toggle="pill" href="#sectionTab">Section</a></li>
   
    <!-- <li><a data-toggle="pill" href="#menu2">Menu 2</a></li> -->
  </ul>
 </div>

	<div class="tab-content">
		
		<div id="sectionTab" class="tab-pane fade in active">
			
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>Edit Section</h2>
				</div>
				
				<s:form id="sectionForm" action="creationTool_updateSection" >
					<div class="flexform">
						
							<div class="flexform-item ">
								<label for="txt">section Name<sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="sectionName" name="dsc.sectionName"
										cssClass="form-control" maxlength="45" />
								</div>
							</div>
		
							<div class="flexform-item"  >
								<div class="form-element">
	
									<button type="button" class="save-btn btn btn-success"
										id="sectionSaveButton" onclick="sectionFormSubmit();">
										<font color="#FFFFFF"> <b><s:text name="Update" /></b></font>
									</button>
	
									<button type="button" class="btnSrch btn btn-warning"
										id="cancelButton_section" onclick="cancelFormSubmit();">
										<font color="#FFFFFF"> <b><s:text
													name="cancel.button" /></b></font>
									</button>

								<button type="button" class="btnSrch btn btn-danger"
									id="cancelButton" onclick="deleteFormSubmit();">
									<font color="#FFFFFF"> <b><s:text name = "delete.button" /></b> </font>
								</button>

							</div>
							</div>

					</div>
					
					<s:hidden id="dsc_id" name="dsc.id"  />
					<s:hidden id="dsc_sectionCode" name="dsc.sectionCode"  />
					
				</s:form>
				
			</div>
			
			
			
		</div>
		
		
		
	</div>


</body>
</html>