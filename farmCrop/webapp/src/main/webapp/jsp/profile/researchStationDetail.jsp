<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<div id="tabs">
<div id="baseDiv1" style="width:98%"></div>
<ul id="tabsUL" class="nav nav-tabs">
	<li><a data-toggle="tab" href="#tabs-1"><s:text name="info.researchStation" /></a></li>
	<li><a data-toggle="tab" href="#tabs-2"><s:text name="researchStation.cow" /></a></li>
</ul>

  <div class="tab-content">
  	<div id="tabs-1" class="tab-pane fade in active">
  	<s:form name="form" cssClass="fillform">
  	 <s:hidden key="currentPage"/>
    <s:hidden key="id" />
    <s:hidden key="researchStation.id"/>

    <s:hidden key="command" />
    <table class="table table-bordered aspect-detail">
       <s:if test='branchId==null'>
            <tr class="odd">
                <td width="35%"><s:text name="app.branch" /></td>
                <td width="65%"><s:property value="%{getBranchName(researchStation.branchId)}" />&nbsp;</td>
            </tr>
        </s:if>


        <tr class="odd">
            <td width="35%"><s:text name="researchStation.researchStationId" /></td>
            <td width="65%"><s:property value="researchStation.researchStationId" />&nbsp;</td>
        </tr>

        <tr class="odd">
            <td width="35%"><s:text name="researchStation.name" /></td>
            <td width="65%"><s:property value="researchStation.name" />&nbsp;</td>
        </tr>
		
		<tr class="odd">
            <td width="35%"><s:text name="country.name" /></td>
            <td width="65%"><s:property value="selectedCountry" />&nbsp;</td>
        </tr>
        <tr class="odd">
            <td width="35%"><s:property value="%{getLocaleProperty('state.name')}"/></td>
            <td width="65%"><s:property value="selectedState" />&nbsp;</td>
        </tr>
        
         <tr class="odd">
            <td width="35%"><s:text name="locality.name" /></td>
            <td width="65%"><s:property value="researchStation.city.name" />&nbsp;</td>
        </tr>
        <tr class="odd">
            <td width="35%"><s:property value="%{getLocaleProperty('city.name')}"/></td>
            <td width="65%"><s:property value="researchStation.municipality.name" />&nbsp;</td>
        </tr>
        
        <tr class="odd">
            <td width="35%"><s:text name="researchStation.pointPerson" /></td>
            <td width="65%"><s:property value="researchStation.pointPerson" />&nbsp;</td>
        </tr>
        
        <%-- <tr class="odd">
            <td width="35%"><s:text name="researchStation.division" /></td>
            <td width="65%">
                <div style="width:600px;word-wrap:break-word;">
                    <s:property value="researchStation.division" />&nbsp;
                </div>				
            </td>
        </tr>
 --%>
        <tr class="odd">
            <td width="35%"><s:text name="researchStation.designation" /></td>
            <td width="65%">
                <div style="width:600px;word-wrap:break-word;">
                    <s:property value="researchStation.designation" />&nbsp;
                </div>				
            </td>
        </tr>
        
         <tr class="odd">
            <td width="35%"><s:text name="researchStation.researchStationAddress" /></td>
            <td width="65%">
                <div style="width:600px;word-wrap:break-word;">
                    <s:property value="researchStation.researchStationAddress" />&nbsp;
                </div>				
            </td>
        </tr>



    </table>
    <br />
    <div class="yui-skin-sam">
        <sec:authorize
            ifAllGranted="profile.researchStation.update">
            <span id="update" class=""><span class="first-child">
                    <button type="button" onclick="onUpdate();" class="edit-btn btn btn-success"><FONT
                            color="#FFFFFF"> <b><s:text name="edit.button" /></b> </font></button>
                </span></span>
            </sec:authorize> <sec:authorize ifAllGranted="profile.researchStation.delete">
            <span id="delete" class=""><span class="first-child">
                    <button type="button" onclick="onDelete();" class="delete-btn btn btn-warning"><FONT
                            color="#FFFFFF"> <b><s:text name="delete.button" /></b> </font></button>
                </span></span>
        </sec:authorize> <span id="cancel" class=""><span class="first-child">
                <button type="button" onclick="onCancel();" class="back-btn btn btn-sts"><b><FONT
                            color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
            </span> </span>
    </div>

</s:form>

<s:form name="updateForm" id="updateForm" action="researchStation_update">		
    <s:hidden name="id" value="%{researchStation.id}" />  	  
    <s:hidden key="currentPage" />
</s:form>
<s:form name="deleteForm"  id="deleteForm" action="researchStation_delete">
    <s:hidden key="id" value="%{researchStation.id}"/>
    <s:hidden key="currentPage" />
</s:form>

<s:form name="listForm" id="listForm" action="researchStation_list">
    <s:hidden name="currentPage" />
</s:form>

</div>

	<div id="tabs-2" class="tab-pane fade">
		<s:hidden key="id" /> 
		<sec:authorize ifAllGranted="profile.researchStation.create">
			<input type="BUTTON" class="btn btn-sts" id="add" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
		</sec:authorize>
		<div id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
		<s:form name="createform" action="cow_create">
			  <s:hidden key="id" value="%{researchStation.id}"  name="researchStationId"/>
			<s:hidden key="currentPage"/>
			<s:hidden name="tabIndex"/>	
			<s:hidden name="tabIndexz" value="#tabs-2" />
			
		</s:form>
		<s:form name="detailform" action="cow_detail">
			<s:hidden name="tabIndex"/>	
			<s:hidden key="id"  name="id"/>
			 <s:hidden key="id" value="%{researchStation.id}"  name="researchStationId"/>
			<s:hidden key="currentPage"/>
			<s:hidden name="tabIndexz" value="#tabs-2" />
			
		</s:form>
	</div>
  </div>

</div>

<script>
$(document).ready(function () {
	
	 var tabIndex=<%if (request.getParameter("tabIndex") == null) {
			out.print("'#tabs-1'");
		} else {
			out.print("'" + request.getParameter("tabIndex") + "'");
		}%>;
		
	
	loadCowGrid();
	
	var tabIndex=<%if(request.getParameter("tabIndex")==null){out.print("'#tabs-1'");}else{out.print("'"+request.getParameter("tabIndex")+"'");}%>;
	var tabObj=$('a[href="'+tabIndex+'"]');
	$(tabObj).closest("li").addClass('active');
	$("div").removeClass("active in");
	$(tabIndex).addClass('active in');
	
	//Below code to set varity tab active if crumb value is redirected.
	 var tabSelected = getUrlParameter('tabValue'); 
	 if(tabSelected=="tabs-2"){
		 $(tabIndex).removeClass("active in");
		 $('#tabs-2').addClass('active in');
		 $(tabObj).closest("li").removeClass('active');
		 tabObj=$('a[href="#tabs-2"]');
		 $(tabObj).closest("li").addClass('active');
	 }
	
 	
});

function loadCowGrid(){
		$(document).ready(function(){
			jQuery("#detail").jqGrid(
			{
			   	url:'cow_data.action?researchStationId='+<s:property value="researchStation.id" />,
			   	pager: '#pagerForDetail',	  
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	mtype: 'POST',
			   	colNames:[
			  		   	  '<s:text name="cow.cowId"/>',
			  		      '<s:text name="cow.tagNo"/>',
			  		    '<s:text name="cow.lactationNo"/>'
			  		  
			      	 	 ],
			   	colModel:[			
			   		{name:'cowId',index:'cowId',width:125,sortable:true},
			   		{name:'tagNo',index:'tagNo',width:125,sortable:true},
			   		{name:'lactationNo',index:'lactationNo',width:125,sortable:true}
			   		
			   			 ],
			   	height: 301, 
			    width: $("#baseDiv1").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			  
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			    onSelectRow: function(id){ 
				  document.detailform.id.value  =id;
			      document.detailform.submit();      
				},		
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        }
			});
			
			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });
		}); 
	}
	
	
	

function onUpdate(){
document.updateForm.submit();

}

function onDelete(){

var val = confirm('<s:text name="confirm.delete"/>');
if (val)
        document.deleteForm.submit();
}

function onCancel(){
document.listForm.submit();
}

	
	//Function to get URL Parameter values.
	function getUrlParameter(sParam) {	   
	   var sPageURL = decodeURIComponent(window.location.search.substring(1));
	   var sURLVariables = sPageURL.split('&');
	   var  sParameterName;
	   var i;	   

	   for (i = 0; i < sURLVariables.length; i++) {		   
	       sParameterName = sURLVariables[i].split('=');        
	       if (sParameterName[0] === sParam) {  	    	   
	           return sParameterName[1] === undefined ? true : sParameterName[1];
	       }
	   }
	}
	</script>