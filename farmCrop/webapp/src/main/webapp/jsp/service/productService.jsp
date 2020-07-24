<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script>

function listSubcategory(call){	
	var callback = {
    success: function (oResponse) 	{   
		var result=oResponse.responseText;
		var arry =populateValues(result);
		document.form.subcategory.length = 0;
		addOption(document.getElementById("subcategory"), "Vegetable", "1");
		
		for (var i=0; i < arry.length;i++){
			if(arry[i]!="")
				addOption(document.getElementById("subcategory"), arry[i], arry[i]);
			}
	}
	}
    var data = "selectedCategory="+call.value;	 
	var url='productService_populateSubcategory';	   
	var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	   
   }

function isNumber(evt) {
	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}


</script>
<div class="error">
<sup>*</sup> <s:text name="reqd.field" /></div>
<font color="red"> <s:actionerror /> <s:fielderror /></font>
<s:form name="form" cssClass="fillform" action="productService_%{command}">
	<s:hidden key="currentPage"/>
		
	<s:hidden name="selectedCategory" value="Vegetables"/>
	<s:if test='"update".equalsIgnoreCase(command)'> 

	</s:if>
	<s:hidden key="command" />
	 <s:hidden key="currentPage"/>
	 <s:hidden key ="id"/>
	 <s:hidden key="product.id" />
    <s:hidden name="tabIndexsubCategoryService" />
    <s:hidden name="subCategoryId" value="%{subCategoryId}" />
    <s:hidden name="subCategoryName" value="%{subCategoryName}" />
    <s:hidden name="subCategoryCode" value="%{subCategoryCode}" />
	<s:hidden key="temp" id="temp"/>
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:text name="info.product" /></th> 
		</tr>
		<%--<tr class="odd" style="display: none;">
 	 	<td><s:text name="product.category"/></td>
			<td><s:select id="category" name="selectedCategory" headerKey="-1" headerValue="%{getText('txt.select')}"  list="listCategory" listKey="name" 
				listValue="name" theme="simple" onchange="listSubcategory(this)"/></td>
		</tr>
		 --%>
		<tr class="odd">
 	 	<td><s:text name="product.subCategory"/></td>
			<td>
			
			  <!-- <s:select id="subcategory" value="%{product.subcategory.name}" name="product.subcategory.name" headerKey="-1" headerValue="%{getText('txt.select')}" list="listSubCategory" listKey="name" 
				listValue="name" theme="simple"/><sup style="color: red;">*</sup>	-->
				
				<s:if test='"update".equalsIgnoreCase(command)'>
					<s:property value="%{product.subcategory.Code}" />			
					<s:text name="-" />
					<s:property value="product.subcategory.Name" />
					<s:hidden name="product.subcategory.Code" />	
				</s:if> 
				<s:else>
					<s:property value="%{subCategoryCode}" />				
					<s:text name="-" />
					<s:property value="%{subCategoryName}" />
				</s:else>
		    </td>
		  </tr>
		<%-- <tr class="odd">
			<td width="35%"><s:text name="product.code" /></td>
			<td width="65%">
			<s:if test='"update".equalsIgnoreCase(command)'>
					<s:property value="product.code" />
						<s:hidden key="product.code" />
				</s:if>
			<s:else>
				<s:textfield name="product.code" theme="simple" maxlength="8"/><sup
				style="color: red;">*</sup></s:else>
			</td>
		</tr>
		 --%>
		<tr class="odd">
			<td width="35%"><s:text name="product.name" /></td>
			<td width="65%"><s:textfield name="product.name" theme="simple" maxlength="50"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
		 
		<tr class="odd">
			<td width="35%"><s:text name="product.unit" /></td>
			
		<td width="65%">
				<div style="margin-left:-15px" class="col-xs-3"><s:select id="type" name="product.type.code" 
				list="listUom" headerKey="-1" listKey="code" listValue="name" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm select2"/></div>
				<sup style="color: red;">*</sup></td>
			
							
		</tr>  
		
		<tr class="odd">
			<td width="35%"><s:text name="product.price" /></td>
			<td width="65%" style="width: 10px">
			<s:textfield name="product.pricePrefix" id="proPrefix" theme="simple" maxlength="5" cssStyle="width:70px;" onkeypress="return isNumber(event)"/>
			.<s:textfield name="product.priceSuffix" id="proSuffix" theme="simple" maxlength="2" cssStyle="width:70px;" onkeypress="return isNumber(event)"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
	</table>
	<br />

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
		
	</s:if> <s:else>
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span>
	</s:else>
	<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn btn btn-sts">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>
	</div>
</s:form>

<s:form name="cancelform" action="subCategoryService_detail.action%{tabIndexsubCategoryService}">
     <s:hidden name="tabIndexsubCategoryService " value="%{tabIndexsubCategoryService}"/>
     <s:hidden key="id" value="%{subCategoryId}" id="subCategoryId" />	
     <s:hidden name="tabIndex" value="%{tabIndexsubCategoryService}"/>
     <s:hidden key="currentPage"/>
</s:form>




