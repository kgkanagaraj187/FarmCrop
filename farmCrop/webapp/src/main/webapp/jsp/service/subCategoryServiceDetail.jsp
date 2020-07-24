<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<body>
  <div class="tab-content">
<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#tabs-1"><s:property value="%{getLocaleProperty('info.category')}" /></a></li>
	<li><a data-toggle="tab" href="#tabs-2"><s:property value="%{getLocaleProperty('info.products')}" /></a></li>	
</ul>

<div id="baseDiv1" style="width:98%"></div>
<div class="tab-content">
<div id="tabs-1"  class="tab-pane fade in active">
<s:hidden key="subCategory.id" id="subCategoryId" />
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="subCategory.id" />
	<s:hidden key="command" />
	<table class="table table-bordered aspect-detail">
		<s:if test='branchId==null'>
			<tr class="odd">
				<td><div class="col-xs-6"><s:text name="app.branch" /></div></td>
				<td><div class="col-xs-6"><s:property value="%{getBranchName(subCategory.branchId)}" />&nbsp;</div></td>
			</tr>
		</s:if>

		<tr class="odd">
			<td><div class="col-xs-6"><s:text name="category.code" /></div></td>
			<td><div class="col-xs-6"><s:property value="subCategory.code" />&nbsp;</div></td>
		</tr>

		<tr class="odd">
			<td><div class="col-xs-6"><s:text name="category.name" /></div></td>
			<td><div class="col-xs-6"><s:property value="subCategory.name" />&nbsp;</div></td>
		</tr>

	</table>
	<br />
	<div class="yui-skin-sam"><sec:authorize
		ifAllGranted="profile.products.update">
		<span id="update" class=""><span class="first-child">
		<button type="button" class="edit-btn btn btn-success"><FONT color="#FFFFFF">
		<b><s:text name="edit.button" /></b> </font></button>
		</span></span>
	</sec:authorize> <sec:authorize ifAllGranted="profile.products.delete">
		<span id="delete" class=""><span class="first-child">
		<button type="button" class="delete-btn btn btn-warning"><FONT
			color="#FFFFFF"> <b><s:text name="delete.button" /></b> </font></button>
		</span></span>
	</sec:authorize> <span id="cancel" class=""><span class="first-child">
	<button type="button" class="back-btn btn btn-sts"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>
</s:form>

<s:form name="updateform" action="subCategoryService_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="subCategoryService_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="subCategoryService_list.action">
	<s:hidden key="currentPage" />
</s:form>
</div>
<div id="tabs-2" class="tab-pane fade">
<s:hidden key="subCategoryId" id="subCategoryId" />
<sec:authorize ifAllGranted="profile.products.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" style="margin-bottom: 1.5%"/>
</sec:authorize>
<div style="width: 98%;" id="baseDiv1">
<table id="detail"></table>
<div id="pagerForDetail"></div>
</div>

<s:form name="createform" action="productService_create">
      <s:hidden key="subCategoryId" id="subCategoryId" value="%{subCategory.id}"/>
      <s:hidden key="currentPage" />
      <s:hidden name="subCategoryCode" value="%{subCategory.code}" />
      <s:hidden name="subCategoryName" value="%{subCategory.name}"  />
</s:form>

<s:form name="updateproductService" action="productService_update.action">
      <s:hidden key="subCategoryId" id="subCategoryId" value="%{subCategory.id}"/>
      <s:hidden name="subCategoryCode" value="%{subCategory.code}" />
      <s:hidden name="subCategoryName" value="%{subCategory.name}"  />
      <s:hidden key="currentPage"/>
</s:form>

<s:form name="detailform" action="productService_detail">
     <s:hidden key="id" value="%{subCategory.id}" id="subCategoryId" />
     <s:hidden key="subCategoryId" id="subCategoryId" value="%{subCategory.id}"/>
     <s:hidden key="currentPage" />
</s:form>

</div>
</div>
</div>

<script type="text/javascript">  

function loadProductServiceGrid(){


	jQuery("#detail").jqGrid(
			{
			   	url:'productService_data.action?subCategoryId='+document.getElementById('subCategoryId').value,
			   	pager: '#pagerForDetail',	  
			   	datatype: "json",	
			    styleUI : 'Bootstrap',
				mtype: 'POST',
			   	colNames:[	
						'<s:text name="product.code"/>',
		  				'<s:text name="product.name"/>',
		  				'<s:text name="product.subCategory"/>',
		  				//'<s:text name="product.unit"/>',  				
			  			'<s:text name="product.price"/>'
						 ],
			   	colModel:[
			   		{name:'code',index:'code',width:125,sortable:true},
			   		{name:'name',index:'name',width:125,sortable:true},
			   		{name:'sc.id',index:'sc.id',width:125,sortable:true},
			   		//{name:'unit',index:'unit',width:125,sortable:true},	   		
			   		{name:'price',index:'price', sortable:true, width:125, align:'left'}
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

}

$(document).ready(function () {
	loadProductServiceGrid();
	
	var tabIndex=<%if(request.getParameter("tabIndex")==null){out.print("'#tabs-1'");}else{out.print("'"+request.getParameter("tabIndex")+"'");}%>;
	var tabObj=$('a[href="'+tabIndex+'"]');
	$(tabObj).closest("li").addClass('active');
	$("div").removeClass("active in");
	$(tabIndex).addClass('active in');
	
    $('#update').on('click', function (e) {
        document.updateform.id.value = document.getElementById('subCategoryId').value;
        document.updateform.currentPage.value = document.form.currentPage.value;
        document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
        if (confirm('<s:text name="confirm.delete"/> ')) {
            document.deleteform.id.value = document.getElementById('subCategoryId').value;
            document.deleteform.currentPage.value = document.form.currentPage.value;
            document.deleteform.submit();
        }
    });
});

</script>
 </body>
