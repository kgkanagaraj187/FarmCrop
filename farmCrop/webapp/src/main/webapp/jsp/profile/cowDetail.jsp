<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<s:hidden key="cow.id" id="cowId"/>
<font color="red">
    <s:actionerror/></font>
<s:form name="form" cssClass="fillform">

		<table class="table table-bordered aspect-detail fillform">
						<tr>
							<th colspan="6" class="table table-bordered aspect-detail">
								<a data-toggle="collapse" data-parent="#accordion"
								href="#cowInfoAccordian" class="accrodianTxt"><s:text
										name="info.cow" /></a>
							</th>
						</tr>
						<tbody id="cowInfoAccordian" class="panel-collapse collapse in">
							<tr class="odd">
								<td style="width: 17%;"><s:text name="cow.cowId" /></td>
								<td style="width: 17%;"><s:property value="cow.cowId" /></td>
								<td rowspan="4" colspan="2" style="width: 34%;">
									<table class="table table-bordered aspect-detail fillform">
										<tr>
											<td colspan="4"><s:if
													test='cowImageByteString!=null && cowImageByteString!=""'>
													<img width="150" height="100" border="0"
														src="data:image/png;base64,<s:property value="cowImageByteString"/>">
												</s:if> <s:else>
													<img align="middle" width="150" height="100" border="0"
														src="img/no-image.png">

												</s:else></td>
										</tr>
										<tr>
											<td><s:text name="latitude" />:</td>
											<td><s:property value="cow.latitude" /></td>
											<td><s:text name="longitude" /></td>
											<td><s:property value="cow.longitude" /></td>
										</tr>
									</table>
								</td>
								
								<td style="width: 17%;"><s:text name="cow.lactationNo" /></td>
								<td style="width: 17%; font-weight: bold;"><s:property
										value="cow.lactationNo" />&nbsp;</td>
							</tr>

						
								<tr class="odd">
								
								<td style="width: 17%;"><s:text name="cow.sireId" /></td>
								<td style="width: 17%; font-weight: bold;"><s:property
										value="cow.sireId" />&nbsp;</td>
										
									<td><s:text name="cow.damId"/></td>
									<td style="font-weight: bold;"><s:property
											value="cow.damId" />&nbsp;</td>
									
								</tr>

							<tr class="odd">
							
							<td><s:text name="cow.genoType" /></td>
									<td style="font-weight: bold;"><s:property
											value="cow.genoType" /></td>
											
								<td><s:text name="cow.milkFirstHundPerDay" /></td>
									<td style="font-weight: bold;"><s:property
											value="cow.milkFirstHundPerDay" />&nbsp;</td>
								
								

							</tr>
							
							<tr>
								<td><s:text name="cow.tagNo" /></td>
								<td style="font-weight: bold;"><s:property
											value="cow.tagNo" />&nbsp;</td>
											
											
								<td><s:text name="cow.dob" /></td>
								<td style="font-weight: bold;"><s:date name="cow.dob" format="MM/dd/yyyy"/></td>
							</tr>
							<tr>
								
					<%-- 
								<td><s:text name="farmer.gender" /></td>
								<td style="font-weight: bold;"><s:text
										name='%{farmer.gender}' />&nbsp;</td> --%>
							</tr>
							
	</tbody>
	</table>
	
	
	<br />
		<s:if test="calfList.size>0">
	<table class="table table-bordered aspect-detail" >
		<tr>
			<th colspan="8"><s:text name="info.calf" /></th>
		</tr>
	
			<tr>
				<td width="20%">
					<b><s:text name="calf.calfId" /></b>
				</td>
				<td>
					<b><s:text name="calf.bullId" /></b>
				</td>
				<td>
					<b><s:text name="calf.serviceDate" /></b>
				</td>
				<td>
					<b><s:text name="calf.gender" /></b>
				</td>
				<td>
					<b><s:text name="calf.lastDateCalving" /></b>
				</td>
				<td>
					<b><s:text name="calf.birthWeight" /></b>
				</td>
				<td>
					<b><s:text name="calf.calvingIntvalDays" /></b>
				</td>
				<td>
					<b><s:text name="calf.deliveryProcess" /></b>
				</td>
			
			</tr>
			<s:iterator value="calfList" status="state" var="bean" >
			<tr class="odd">
				<td>
					<s:property value="calfId"/>
				</td>
				<td>
					<s:property value="bullId"/>
				</td>
				<td>
					<s:date name="serviceDate" format="MM/dd/yyyy"/>
				</td>
				<td>
					<s:property value="gender"/>
				</td>
				<td>
					<s:date name="lastDateCalving" format="MM/dd/yyyy"/>
				</td>
				<td>
					<s:property value="birthWeight"/>
				</td>
				<td>
					<s:property value="calvingIntvalDays"/>
				</td>
				<td>
					<s:property value="deliveryProcess"/>
				</td>
			</tr>
		</s:iterator>
	</table>
	</s:if>
	<br/>
<br/>

<div class="yui-skin-sam">
   <sec:authorize ifAllGranted="profile.researchStation.update">
             <span id="update" class=""><span class="first-child">
                <button type="button"   onclick="onUpdate();"  class="edit-btn btn btn-success">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span></sec:authorize> 
            
            <sec:authorize ifAllGranted="profile.researchStation.delete">
             <span id="delete" class=""><span class="first-child">
                <button type="button" class="delete-btn btn btn-warning"  onclick="onDelete();">
                    <FONT color="#FFFFFF">
                    <b><s:text name="delete.button" /></b>
                    </font>
                </button>
            </span></span></sec:authorize>  

    <span id="cancel" class=""><span class="first-child"><button type="button" id="back" onclick="onCancel();" class="back-btn btn btn-sts">

               <b><FONT color="#FFFFFF"><s:text name="back.button"/>
                </font></b></button></span></span>

</div>
</s:form>

<s:form name="updateform" action="cow_update.action">
   <s:hidden id="id" name="id" value="%{cow.id}"   />
    <s:hidden key="currentPage"/>
   <s:hidden name="researchStationId" value="%{researchStationId}"   />
    <s:hidden name="farmId" value="%{farmId}"   />
    <s:hidden name="tabIndexz" value="%{tabIndexz}" />
	<s:hidden name="tabIndex" value="%{tabIndexz}" />
</s:form>
<s:form name="deleteform" action="cow_delete.action">
   <s:hidden name="id" value="%{cow.id}"  />
   <s:hidden key="currentPage"/>
   <s:hidden name="farmId" value="%{farmId}"   />
      <s:hidden name="researchStationId" value="%{researchStationId}"   />
    <s:hidden name="tabIndexz" value="%{tabIndexz}" />
	<s:hidden name="tabIndex" value="%{tabIndexz}" />
   
</s:form>

	
<s:if test='farmId!="" && farmId!=null '>
	<s:form name="cancelform" action="farm_detail.action%{tabIndexz}">
		<s:hidden name="id" value="%{farmId}" />
		<s:hidden name="tabIndexz" value="%{tabIndexz}" />
		<s:hidden name="tabIndex" value="%{tabIndexz}" />
		<s:hidden key="currentPage" />
	</s:form>
</s:if>
<s:if test='researchStationId!="" && researchStationId!=null'>
		<s:form name="cancelform" action="researchStation_detail.action%{tabIndexz}">
		 <s:hidden key="id" value="%{researchStationId}"  name="id"/>
 			<s:hidden name="tabIndexz" value="%{tabIndexz}" />
			<s:hidden name="tabIndex" value="%{tabIndexz}" />
			<s:hidden key="currentPage" />
		</s:form>
	</s:if>



<script type="text/javascript">  

function onUpdate(){
    document.updateform.submit();
   }

    function onDelete(){

    var val = confirm('<s:text name="confirm.delete"/>');
    if (val)
            document.deleteform.submit();
    }

    function onCancel(){
    document.cancelform.submit();
    }

</script>



</body>