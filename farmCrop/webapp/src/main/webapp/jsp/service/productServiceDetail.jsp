<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="product.id" id="productId" />
<font color="red"><s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="product.id" id="productID"/>
	<s:hidden name="subCategoryId" value="%{product.subcategory.id}" />
	<s:hidden key="command" />
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:text name="info.product" /></th> 
		</tr>
		<tr class="odd" style="display: none;">
			<td width="35%"><s:text name="product.category" /></td>
			<td width="65%"><s:property
				value="product.subcategory.category.name" />&nbsp;</td>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="product.subCategory" /></td>
			<td width="65%"><s:property value="product.subcategory.name" />&nbsp;</td>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="product.code" /></td>
			<td width="65%"><s:property value="product.code" />&nbsp;</td>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="product.name" /></td>
			<td width="65%"><s:property value="product.name" />&nbsp;</td>
		</tr>
		
		
		<tr class="odd">
			<td width="35%"><s:text name="product.unit" /></td>
			<td width="65%"><%-- <s:property value="product.unit"/>- --%><s:property value="product.type.name"/>&nbsp;</td>
		</tr>
		 

		<tr class="odd">
			<td width="35%"><s:text name="product.price" /></td>
			<td width="65%"><s:property value="product.price" />&nbsp;</td>
		</tr>

	</table>
	<br />
	<div class="yui-skin-sam"><sec:authorize
		ifAllGranted="profile.procurementProduct.update">
		<span id="update" class=""><span class="first-child">
		<button type="button" class="edit-btn btn btn-success"><FONT color="#FFFFFF">
		<b><s:text name="edit.button" /></b> </font></button>
		</span></span>
	</sec:authorize> 
	 <sec:authorize ifAllGranted="profile.procurementProduct.delete">
		<span id="delete" class=""><span class="first-child">
		<button type="button" class="delete-btn btn btn-warning"><FONT
			color="#FFFFFF"> <b><s:text name="delete.button" /></b> </font></button>
		</span></span>
	</sec:authorize>
	 <span id="cancel" class=""><span class="first-child">
	<button type="button" class="back-btn btn btn-sts"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>
</s:form>

<s:form name="updateform" action="productService_update.action">
  	    <s:hidden name="tabIndexsubCategoryService " value="%{tabIndexsubCategoryService}"/>
  	    <s:hidden key="id" value="%{product.id}" />
  	    <s:hidden name="subCategoryId" value="%{product.subcategory.id}" /> 
  	    <s:hidden key="currentPage" />
 
</s:form>
<s:form name="deleteform" action="productService_delete.action">
        <s:hidden name="tabIndexsubCategoryService " value="%{tabIndexsubCategoryService}"/>
        <s:hidden name="subCategoryId" value="%{product.subcategory.id}" /> 
        <s:hidden key="id" value="%{product.id}"/>
	    <s:hidden key="currentPage" />
</s:form>

<s:form name="cancelform" action="subCategoryService_detail.action">
        <s:hidden name="tabIndexsubCategoryService " value="%{tabIndexsubCategoryService}"/>
       <s:hidden key="id" value="%{product.subcategory.id}" id="product.subcategory.id" />	
       <s:hidden name="tabIndex" value="%{tabIndexsubCategoryService}"/>
        <s:hidden key="currentPage" />
</s:form>

<script type="text/javascript">  
/*
YAHOO.util.Event.addListener(window, "load", function() {
	function onUpdate(p_oEvent){     
		 document.updateform.currentPage.value = document.form.currentPage.value;
		 document.updateform.submit();
    }
 var button = new YAHOO.widget.Button("update", { onclick: { fn: onUpdate } });
   
});
YAHOO.util.Event.addListener(window, "load", function() {
	function onDelete(p_oEvent){     
		 if(confirm( '<s:text name="confirm.delete"/> ')){
			 document.deleteform.currentPage.value = document.form.currentPage.value;
				document.deleteform.submit();
		}
    }
 var button = new YAHOO.widget.Button("delete", { onclick: { fn: onDelete } });
   
});*/



$(document).ready(function () {
    $('#update').on('click', function (e) {
        document.updateform.id.value = document.getElementById('productID').value;
        document.updateform.currentPage.value = document.form.currentPage.value;
        document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
        if (confirm('<s:text name="confirm.delete"/> ')) {
            document.deleteform.id.value = document.getElementById('productID').value;
            document.deleteform.currentPage.value = document.form.currentPage.value;
            document.deleteform.submit();
        }
    });
});


</script>