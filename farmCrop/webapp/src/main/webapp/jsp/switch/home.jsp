<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="java.util.Date"%>
<html>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
<div style="font-weight: bold;">
<s:text name="currentTime"/> <%=new Date()%>
</div>
</body>
</html>