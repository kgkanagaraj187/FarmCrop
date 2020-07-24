<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<s:form name="form" action="userCred">
<s:hidden key="currentPage"/>
<table width="95%" cellspacing="0" class="fillform"  style="margin-top:10px;">
		<tr>
			<th colspan="2"><s:text name="info.userCred"/></th>
		</tr>
		<tr class="odd">
			<td width="35%"> <s:text name="userCred.username" /></td>
			<td width="65%"><s:property value="userCred.username"/>&nbsp;</td>
		</tr>
		<tr class="odd">
			<td width="35%"> <s:text name="userCred.role.id" /></td>
			<td width="65%"><s:property value="roleName"/>&nbsp;</td>
		</tr>
		<tr class="odd">

			<td width="35%"><s:text name="userCred.enabled" /></td>
			<td width="65%"><s:property value="status"/>&nbsp;</td>
		</tr>
		
	</table>    
    <br/>
<div class="yui-skin-sam" align="left">
    <sec:authorize ifAllGranted="profile.user.credential.update">
        <span id="button" class=""><span class="first-child">
                <button type="button" class="edit-btn">
                    <FONT color="#FFFFFF">
                        <b>
                        <s:text name="edit.button" />
                        </b>
                    </font>
                </button>
            </span></span>
    </sec:authorize> <span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn">
               <b><FONT color="#FFFFFF"><s:text name="back.button"/>
                </font></b></button></span></span>
</div>
</s:form>
<s:form name="usercredupdateform" action="userCred_update">
    <s:hidden key="id"/>
   <s:hidden key="currentPage"/>
</s:form>

<s:form name="cancelform" action="userCred_list.action">
    <s:hidden key="currentPage"/>
</s:form>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function(){
        function onCreate(p_oEvent){                    	
            document.usercredupdateform.action = "userCred_update.action";
            document.usercredupdateform.currentPage.value = document.form.currentPage.value;
            document.usercredupdateform.submit();
        }
        var button = new YAHOO.widget.Button("button", {	
            onclick: {
                fn: onCreate
            }
        });
    });
</script>
