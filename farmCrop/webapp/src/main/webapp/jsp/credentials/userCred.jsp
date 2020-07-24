<%@ include file="/jsp/common/form-assets.jsp" %>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
<style type="text/css">
    .alignTopLeft {
        float: left;
        width: 13em; /* Adjust for the width of your labels */
        text-align: left;
        vertical-align: top;
        font-weight:normal;
    }
    .fillform TD:first-child {border:1px solid #D2D695 !important;}
</style></head>

<div class="error" style="margin-bottom:0px;">
    <s:fielderror/>
    <s:actionerror/>
    <sup>*</sup><s:text name="reqd.field" />
</div>
<div id="warn" class="error">
</div>
<s:form name="form" action="userCred_%{command}">
<s:hidden key="currentPage"/>
<s:hidden key="id" id="id"/>
<s:hidden key="userCred.id" />
<s:hidden key="command" />
<s:hidden key="userCred.dataId" id="dataId"/>
<s:hidden key="userCred.username" />
<s:hidden key="temp" id="temp"/>
<table width="85%" cellspacing="0" class="fillform" class="fillform">


        <tr>
            <th>
                <s:text name="info.userCred"/>
            </th>
        </tr>
        <tr>
            <td class="odd">
                
                    <label class="alignTopLeft">
                        <s:text name="userCred.username" />
                    </label>
                <s:property value="userCred.username" />
            </td>
        </tr>
        <tr class="odd">
            <td id='passwordOptionDiv'>
               
                    <label class="alignTopLeft">
                        <s:text name="userCred.changePassword" />
                    </label>
                <s:checkbox key="userCred.changePassword" theme="simple" onclick="javascript:showPass(this.name);"/>
                
               
            </td>
        </tr>
        <tr class="odd" id='pwd'>
            <td align="left" class="odd">
                <div>
                    <table cellspacing="0" style="margin-left: -11px;">
                        <tr>
                            <td style="width: 156px;">
                                
                                    <label>
                                        <s:text name="userCred.password" />
                                       
                                    </label>
                                
                            </td>
                            <td>
                                <s:password id="p" size="23" name="userCred.password" theme="simple" maxlength="16" /> <sup> *</sup>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 156px;">
                                
                                    <label>
                                        <s:text name="userCred.confirmpassword" />
                                    
                                    </label>
                                </strong>
                            </td>
                            <td>
                                <s:password size="23" id="pC" name="userCred.confirmPassword" maxlength="16" theme="simple" /><sup> *</sup>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
       
	 	<tr class="odd">
            <td >
             
                 <label class="alignTopLeft">
               <s:text name="user.select.role" />
                 </label>
                
                
          <div id="dynamicstate">
          <s:select name="userCred.role.id" list="roles" listKey="id" listValue="name" theme="simple" cssStyle="width:170px;margin:5px 0 5px 0;"/><sup>*</sup></div> 
            </td>
		 </tr>
		 
        
        <tr>
            <td class="odd">
                
                    <label class="alignTopLeft">
                        <s:text name="userCred.enabled" />
                     
                    </label>
                
                <s:checkbox name="userCred.enabled" theme="simple" />
            </td>
        </tr>
    </table>
</s:form>
<br/>
<div class="yui-skin-sam" align="left">
    <sec:authorize ifAllGranted="profile.user.update">
        <span id="button" class=""><span class="first-child">
                <button type="button" class="save-btn">
                    <FONT color="#FFFFFF">
                        <b>
                        <s:text name="update.button" />
                        </b>
                    </font>
                </button>
            </span></span>
    </sec:authorize><span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span></div>
<s:form name="cancelform" action="userCred_list.action">
    <s:hidden key="currentPage"/>
</s:form>

<script type="text/javascript">



<s:if test="userCred.password==null">
	document.getElementById('pwd').className = "even";
    </s:if>
    <s:elseif test="userCred.password==''">
    document.getElementById('pwd').className = "even";
    </s:elseif>
    <s:elseif test="userCred.changePassword==true">
    document.getElementById('pwd').className = "even";
    </s:elseif>
    <s:else>
    document.getElementById('pwd').className = "hide";
    </s:else>
    
    function showPass(element)
    {
    var group=document.getElementsByName(element);
    			if (group[0].checked)
    			{
    			document.getElementById('pwd').className="even";
    			}
    			else
    			{
    			 document.getElementById('pwd').className="hide";
    			}
    }
    
    YAHOO.util.Event.addListener(window, "load", function() {
    	
    
   		function onCreate(p_oEvent) {      	
   	   		
        	 			document.form.action="userCred_update.action";
        	 		    document.getElementById('temp').value = "1";
    	       			document.form.submit();
    	}
        var button = new YAHOO.widget.Button("button", { onclick: { fn: onCreate } });
    });


 
</script>
