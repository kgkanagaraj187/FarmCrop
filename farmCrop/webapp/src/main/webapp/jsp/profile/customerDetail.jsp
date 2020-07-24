<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
    <!-- add this meta information to select layout  -->
    <meta name="decorator" content="swithlayout">
</head>
<s:hidden key="customer.id" id="customerId"/>
<font color="red">
<s:actionerror/></font>
<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('info.customer')}" /></h2>
					<%-- <div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property value="%{getBranchName(customer.branchId)}" /></p>
					</div> --%>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.customerId" /></p>
						<p class="flexItem"><s:property value="customer.customerId" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.customerName" /></p>
						<p class="flexItem"><s:property value="customer.customerName" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="%{getLocaleProperty('customer.customerType')}" /></p>
						<p class="flexItem"><s:property value="customer.customerType" /></p>
					</div>
					<h2>Location Information</h2>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="country.name" /></p>
						<p class="flexItem"><s:property value="selectedCountry" />&nbsp;-&nbsp;<s:property value="selectedCountryCode"/></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('state.name')}" /></p>
						<p class="flexItem"><s:property value="selectedState" />&nbsp;-&nbsp;<s:property value="selectedStateCode" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="locality.name" /></p>
						<p class="flexItem"><s:property value="customer.city.name" />&nbsp;-&nbsp;<s:property value="customer.city.code" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('city.name')}"/></p>
						<p class="flexItem"><s:property value="customer.municipality.name" />&nbsp;-&nbsp;<s:property value="customer.municipality.code" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.customerAddress" /></p>
						<p class="flexItem"><s:property value="customer.customerAddress" /></p>
					</div>
					
					
						<h2>Personal Information</h2>
							<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.personName" /></p>
						<p class="flexItem"><s:property value="customer.personName" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('customer.customerSegment')}" /></p>
						<p class="flexItem"><s:property value="customer.customerSegment" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.landLine" /></p>
						<p class="flexItem"><s:property value="customer.landLine" /></p>
					</div>
				
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.mobileNumber" /></p>
						<p class="flexItem"><s:property value="customer.mobileNumber" /></p>
					</div>
					
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="customer.email" /></p>
						<p class="flexItem"><s:property value="customer.email" /></p>
					</div>
					
			
				</div>
				<div class="flexItem flex-layout flexItemStyle">
					<div class="button-group-container">
						<a  onclick="onUpdate();" class="btn btn-success"><s:text name="edit.button" /></a>
						<a onclick="onDelete();" class="btn btn-danger"><s:text name="delete.button" /></a>
						<a onclick="onCancel();" class="btn btn-sts"><s:text name="back.button" /></a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
					
<s:form name="updateForm" id="updateForm" action="customer_update">		
    <s:hidden name="id" value="%{customer.id}" />  	  
    <s:hidden key="currentPage" />
</s:form>
<s:form name="deleteForm"  id="deleteForm" action="customer_delete">
    <s:hidden key="id" value="%{customer.id}"/>
    <s:hidden key="currentPage" />
</s:form>

<s:form name="listForm" id="listForm" action="customer_list">
    <s:hidden name="currentPage" />
    <s:hidden name="postdata" id="postdata"/>
</s:form>


<script type="text/javascript">

    var tabIndex = "<s:property value='tabIndex'/>";
    function setTabIndex(index){

    tabIndex = index;
	    if (tabIndex == "#tabs-2"){
	   	 loadCustomerProjectsGrid();
	    }
    }
    
    function loadCustomerProjectsGrid(){
    jQuery("#detail").jqGrid(
    {

    url:'customerProject_data.action?customerId=' + document.getElementById('customerId').value,
            pager: '#pagerForDetail',
            datatype: "json",
            colNames:[

                    '<s:text name="customerProject.customerProjectCode"/>',
                    '<s:text name="customerProject.nameOfProject"/>',
                    '<s:text name="customerProject.unitNo"/>',
                    '<s:text name="customerProject.nameOfUnit"/>',
                    '<s:text name="customerProject.locationOfUnit"/>',
    <sec:authorize ifAllGranted="profile.customer.delete">
            '<s:text name="Action"/>'
    </sec:authorize>
            ],
            colModel:[
            {name:'codeOfProject', index:'codeOfProject', width:125, sortable:true},
            {name:'nameOfProject', index:'nameOfProject', width:125, sortable:true},
            {name:'unitNo', index:'unitNo', width:125, sortable:true},
            {name:'nameofUnit', index:'nameOfUnit', width:125, sortable:true},
            {name:'locationOfUnit', index:'locationOfUnit', width:125, sortable:true},
    <sec:authorize ifAllGranted="profile.customer.delete">
            {name:'action', index:'action', width:125, sortable:false, search:false}
    </sec:authorize>

            ],
            height: 255,
            width: $("#baseDiv").width(), // assign parent div width
            scrollOffset: 0,
            rowNum:10,
            rowList : [10, 25, 50],
            sortname:'id',
            sortorder: "desc",
            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
            beforeSelectRow:
            function(rowid, e) {
            var iCol = jQuery.jgrid.getCellIndex(e.target);
            if (iCol >= 5){return false; }
            else{ return true; }
            },
            onSelectRow: function(id){

            document.customerProjectupdateform.id.value = id;
            document.customerProjectupdateform.tabIndex.value = tabIndex;
            document.customerProjectupdateform.submit();
            },
            onSortCol: function (index, idxcol, sortorder) {
            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                    && this.p.colModel[this.p.lastsort].sortable !== false) {
            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
            }
            }
    });
    jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
            jQuery("#detail").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

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
    }

    $(document).ready(function(){

//     jTab('#tabs').tabs({
//     activate: function (event, ui) {

//     var act = jTab("#tabs").tabs("option", "active");
//     var index = "#tabs-" + (act + 1);
//     setTabIndex(index);
//     },
//             create: function(event, ui) {

//             var act = jTab("#tabs").tabs("option", "active");
//             var index = "#tabs-" + (act + 1);
//             setTabIndex(index);
//             }
//     });
   
    });
    function deleteCustomerProject(id){

    if (confirm('<s:text name="customerProject.confirmDelete"/> ')){
    document.customerProjectdeleteform.id.value = id;
    document.customerProjectdeleteform.tabIndex.value = tabIndex;
    document.customerProjectdeleteform.submit();
    }
    }

//function addCustomerProject()
//{
    //document.createform.tabIndex.value = tabIndex; 
    // document.createform.submit();
//}


    function onUpdate(){
    document.updateForm.submit();
//    document.updateform.id.value = document.getElementById('id').value;
//    document.updateform.currentPage.value = document.form.currentPage.value;
//    document.updateform.submit();
    }

    function onDelete(){

    var val = confirm('<s:text name="confirm.delete"/>');
    if (val)
            document.deleteForm.submit();
    }

    function onCancel(){
    document.listForm.submit();
    }


</script>

