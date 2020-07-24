<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<style type="text/css">
.alignTopLeft {
    float: left;
    width: 6em;
}
#serviceLocationDiv select{width:260px!important;}
#serviceLocationDiv input {width:100px;height:24px;/*background:#204190;border:1px solid #224395 !important;*/color:#fff;font-size:12px;text-align:left;padding-left:18px;}
#serviceLocationDiv td{border:none!important;}
#serviceLocationDiv > table TD:nth-child(2) {text-align:center!important;width:100px!important;}
#serviceLocationDiv > table TD:nth-child(1){width:140px!important; padding-left:0px!important;}
#serviceLocationDiv > table TD>label{font-weight:bold!important;}
</style>
<script type="text/javascript">
$(function () { 
	//alert(HI);
 $("#dynOpt input[value='Remove']").addClass("ic-remove");
 $("#dynOpt input[value='Add']").addClass("ic-add");
 $("#dynOpt input[value='Remove All']").addClass("ic-removeAll");
 $("#dynOpt input[value='Add All']").addClass("ic-addAll");
})
</script>

<br/>
<div id="dynOpt">
	<s:text name="availableLocation" var="availableTitle" />
<s:text name="selectedLocation" var="selectedTitle" />  
<s:optiontransferselect id="optrnsfr" buttonCssClass="optTrasel"
	allowSelectAll="false" buttonCssStyle="font-weight:bold!important;" allowUpDownOnLeft="false" labelposition="top"
	allowUpDownOnRight="false" name="availableName"
	list="availableServiceLocation" 
	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle}"
	 headerKey="headerKey" doubleName="selectedName"
	doubleId="select" doubleList="selectedServiceLocation"
	doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="Remove All"
	addAllToRightLabel="Add All" addToLeftLabel="Remove"
	addToRightLabel="Add" />
	</div>