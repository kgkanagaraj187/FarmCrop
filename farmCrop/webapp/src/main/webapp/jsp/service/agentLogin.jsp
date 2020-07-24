<%@ include file="/jsp/common/grid-assets.jsp"%>
<%--<%@ include file="/jsp/common/button-assets.jsp"%>
<%--<%@ include file="/switch/common/detailPage-assets.jsp"%>--%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style>
.loginFormContainer {
	width: 368px;
	margin-right: 800px;
}

.loginFormContainer {
	width: 400px\\9;
	position: relative\\9;
	left: 30px\\9;
}

.loginFormContainer {
	width: 400px\\0;
	position: relative\\9;
	left: 30px\\0;
}

.loginFormContainer .label {
	color: #23447e;
	float: left;
	font-size: 14px;
	font-weight: bold;
	margin-top: 6px;
	text-align: left;
	width: 100% !important;
	padding: 0 0 5px 33px;
}

.loginFormContainer .inputTxt {
	width: 200px;
	height: 20px;
	line-height: 10px\\0; *
	line-height: 10px;
	border: solid 1px #b9c285;
	background: #fff url(../images/agro-theme/inputBg.png) repeat-x;
	margin: 0 0 5px 33px;
	padding-left: 5px;
	border-radius: 0px;
	color: #3E431C;
}

.errorLogin {
	color: #ff0000;
	text-align: left;
	padding-top: 4px;
	margin-left: 80px;
}

.errorLogin li {
	list-style: none;
}
</style>


</head>
<body>
<div class="errorLogin" id="actionError" style="min-height: inherit;">
<s:actionerror theme="ese" /></div>
<div id="emptyCredential" class="errorLogin"></div>
<s:form name="loginform" method="post">

	<div>
	<table>
		<tr>
			<td>
			<div><s:if
				test="type=='procurement' || type=='distribution' || type=='payment'">
				<s:text name="comOrAgent.user.name" />
			</s:if><s:else>
				<s:text name="user.name" />
			</s:else> <sup style="color: red;">*</sup></div>
			</td>
			<td><s:textfield id="userName" theme="simple" size="20"
				name="userName" cssClass="inputTxt"
				onkeypress="if (event.keyCode==13){ onClick();}" /></td>
		</tr>

		<tr>
			<td>
			<div><s:text name="password" /><sup
				style="color: red;">*</sup></div>
			</td>
			<td><s:password id="password" theme="simple" size="20"
				name="password" showPassword="true" cssClass="inputTxt"
				onkeypress="if (event.keyCode==13){ onClick();}" /></td>
		</tr>
		<tr>
			<td></td>
			<td>
			<button id="login.button" name="login" class="btn btn-sts"
				style="top: 0px !important; margin: 0 0 5px 33px;" type="button"
				onclick="onClick();"><s:text name="login.button" /></button>
			</td>
		</tr>
	</table>
	</div>
	
	<script type="text/javascript">

window.onload = function () {

	$("#userName").focus();
    if (typeof history.pushState === "function") {
        history.pushState("sateObj", null, null);
        window.onpopstate = function () {
            history.pushState('newSateObj', null, null);
        };
    }
}



	var type="<%=request.getParameter("type")%>";
	if(type == "distribution")
		var loginAction = "<%=request.getParameter("url")%>?type=<%=request.getParameter("type")%>&fId=<%=request.getParameter("fId")%>";
	else
		var loginAction = "<%=request.getParameter("url")%>?type=<%=request.getParameter("type")%>";
		
   function onClick() {	   
		  	var userName=document.getElementById('userName').value;
		   	var password=document.getElementById('password').value;
		   	var type='<%=request.getParameter("type")%>';
		   	document.getElementById("actionError").innerHTML=" ";		   	
		   	var isFsTxn=type=='procurement' || type=='distribution' || type=='payment';
			   	if((userName=="" || userName==null) && (password=="" || password==null)){
				   	    if(isFsTxn)
				   	    	document.getElementById("emptyCredential").innerHTML='<s:text name="comOrAgent.userName.password.reqd"/>';
				   	    else
			   			    document.getElementById("emptyCredential").innerHTML='<s:text name="userName.password.reqd"/>';
			   	}else if(userName=="" || userName==null){
			   	         if(isFsTxn)
			   	        	document.getElementById("emptyCredential").innerHTML='<s:text name="comOrAgent.username.reqd"/>';
			   	         else
			   			    document.getElementById("emptyCredential").innerHTML='<s:text name="username.reqd"/>';
			   	}else if(password=="" || password==null){
			   			document.getElementById("emptyCredential").innerHTML='<s:text name="password.reqd"/>';
			   	}else{				   		
			   		document.loginform.action=loginAction;
			   	   	document.loginform.submit();
				}
   }
   
</script>

</s:form>