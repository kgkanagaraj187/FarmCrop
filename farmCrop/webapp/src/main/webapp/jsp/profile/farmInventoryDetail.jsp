<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="farmInventory.id" id="farmInventoryId"/>
<font color="red">
    <s:actionerror/></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:hidden key="farmInventory.id"/>
	<s:hidden key="farm.id"/>
	<s:hidden key="command" />
	<table width="95%" cellspacing="0">
		<tr>
			<th colspan="2"><s:text name="info.farmInventory" /></th>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="farmInventory.inventoryItem" /></td>
			<td width="65%"><s:property value="farmInventory.inventoryItem.propertyValue" />&nbsp;</td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="farmInventory.itemCount" /></td>
			<td width="65%"><s:property value="farmInventory.itemCount" />&nbsp;</td>
		</tr>
		
	</table>
	<br />
<div class="yui-skin-sam">
    <sec:authorize ifAllGranted="profile.farm.update">
        <span id="update" class=""><span class="first-child">
                <button type="button" class="ic-edit">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span>
    </sec:authorize> <sec:authorize ifAllGranted="profile.farm.delete">
             <span id="delete" class=""><span class="first-child">
                <button type="button" class="ic-delete">
                    <FONT color="#FFFFFF">
                    <b><s:text name="delete.button" /></b>
                    </font>
                </button>
            </span></span></sec:authorize>
  <span id="cancel" class=""><span class="first-child">
  			<button type="button" class="ic-back"><b><FONT color="#FFFFFF"><s:text name="back.button"/>
                </font></b></button></span></span>
</div>
</s:form>

<s:form name="updateform" action="farmInventory_update.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="farmInventory_delete.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="cancelform" action="farmInventory_list.action">
    <s:hidden key="currentPage"/>
</s:form>

<script type="text/javascript">  
YAHOO.util.Event.addListener(window, "load", function() {
	function onUpdate(p_oEvent){     
		 document.updateform.id.value = document.getElementById('farmInventoryId').value;
		 document.updateform.currentPage.value = document.form.currentPage.value;
		document.updateform.submit();
    }
 var button = new YAHOO.widget.Button("update", { onclick: { fn: onUpdate } });
   
 function onDelete(p_oEvent){     
		 if(confirm( '<s:text name="confirm.delete"/> ')){
			 document.deleteform.id.value = document.getElementById('farmInventoryId').value;
			 document.deleteform.currentPage.value = document.form.currentPage.value;
			document.deleteform.submit();
		}
    }
 var button = new YAHOO.widget.Button("delete", { onclick: { fn: onDelete } });
   
});</script>