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
		addOption(document.getElementById("subcategory"), "Select", "");
		
		for (var i=0; i < arry.length;i++){
			if(arry[i]!="")
				addOption(document.getElementById("subcategory"), arry[i], arry[i]);
			}
		listProduct(document.getElementsByName('selectedSubCategory')[0]);
		}
	}
    var data = "selectedCategory="+call.value;	 
	var url='warehouseProduct_populateSubcategory';	   
	var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	   
   }

function listProduct(call){	
	var callback = {
    success: function (oResponse) 	{   
		var result=oResponse.responseText;
		var arry =populateValues(result);
		document.form.product.length = 0;
		addOption(document.getElementById("product"), "Select", "");
		
		for (var i=0; i < arry.length;i++){
			if(arry[i]!="")
				addOption(document.getElementById("product"), arry[i], arry[i]);
			}
		}
	}
    var data = "selectedSubCategory="+call.value;	 
	var url='warehouseProduct_populateProduct';	   
	var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	   
   }
</script>

<script type="text/javascript">
function check() {
	var hit = true;
	var stock = document.getElementById("stock").value;
	if(isNaN(stock)===true) {
		alert('<s:text name="inValid.stock"/>');
		hit = false;
	}
	if(hit) {
		document.form.submit();
	}	
}
</script>
<font color="red"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" />
</font>
<s:form name="form" cssClass="fillform" action="warehouseProduct_%{command}">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="warehouseProduct.id" />
	</s:if>
	<s:hidden key="command" />
	<s:hidden key="temp" id="temp"/>
	<table width="95%" cellspacing="0">
		<tr>
			<th colspan="2"><s:property value="%{getLocaleProperty('info.warehouseProduct')}" /></th>
		</tr>
		vghbc
		<tr class="odd">
 	 	<td><s:text name="warehouseProduct.warehouse"/></td>
			<td><s:select id="category" name="warehouseProduct.warehouse.name" headerKey="-1" headerValue="%{getText('txt.select')}"  list="listWarehouse" listKey="name" 
				listValue="name" theme="simple"/><sup style="color: red;">*</sup></td>
		</tr>
		
		
		<tr class="odd">
 	 	<td><s:text name="warehouseProduct.category"/></td>
			<td><s:select id="category" name="selectedCategory" headerKey="" headerValue="%{getText('txt.select')}"  list="listCategory" listKey="name" 
				listValue="name" theme="simple" onchange="listSubcategory(this)"/></td>
		</tr>
		<tr class="odd">
 	 	<td><s:text name="warehouseProduct.subCategory"/></td>
			<td><s:select id="subcategory" name="selectedSubCategory" headerKey="" headerValue="%{getText('txt.select')}"  list="subCategoryList" listKey="name" 
				listValue="name" theme="simple" onchange="listProduct(this)"/></td>
		</tr>
		
		<tr class="odd">
 	 	<td><s:text name="warehouseProduct.product"/></td>
			<td><s:select id="product" name="warehouseProduct.product.name" headerKey="" headerValue="%{getText('txt.select')}"  list="productList" listKey="name" 
				listValue="name" theme="simple"/><sup style="color: red;">*</sup></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="warehouseProduct.stock" /></td>
			<td width="65%"><s:textfield id="stock" name="warehouseProduct.stock" theme="simple" maxlength="6"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
	</table>
	
	<br />

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span class=""><span class="first-child">
		<button type="button" class="save-btn" onclick="check();"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
		
	</s:if> <s:else>
		<span class=""><span class="first-child">
		<button type="button" class="save-btn" onclick="check();"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span>
	</s:else>
	<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>
	</div>
</s:form>
<s:form name="cancelform" action="warehouseProduct_list.action">
    <s:hidden key="currentPage"/>
</s:form>



