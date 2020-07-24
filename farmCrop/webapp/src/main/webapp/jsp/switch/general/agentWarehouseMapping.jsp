<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
$(function () { 
 $("#dynOpt input[value='Remove']").addClass("ic-remove");
 $("#dynOpt input[value='Add']").addClass("ic-add");
 $("#dynOpt input[value='Remove All']").addClass("ic-removeAll");
 $("#dynOpt input[value='Add All']").addClass("ic-addAll");
})
</script>
<br/>
<div id="dynOpt">
	<s:text name="availableWarehouse" var="availableTitle" />
<s:text name="selectedWarehouse" var="selectedTitle" />  
<s:optiontransferselect id="optrnsfr" buttonCssClass="optTrasel"
	allowSelectAll="false" buttonCssStyle="font-weight:bold!important;" allowUpDownOnLeft="false" labelposition="top"
	allowUpDownOnRight="false" name="availableName"
	list="availableWarehouse" 
	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle}"
	 headerKey="headerKey" doubleName="selectedName"
	doubleId="select" doubleList="selectedWarehouse"
	doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="Remove All"
	addAllToRightLabel="Add All" addToLeftLabel="Remove"
	addToRightLabel="Add" />
	</div>