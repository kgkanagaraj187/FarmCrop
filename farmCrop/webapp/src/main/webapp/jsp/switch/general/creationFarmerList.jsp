<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
	<div class="appContentWrapper marginBottom">
		<div class="row" style="margin-bottom:1%">
			<div class="col-md-2">
				<sec:authorize ifAllGranted="admin.creation.farmer.create">
					<input type="BUTTON" id="add" class="btn btn-sts form-control"
						value="Add Field" onclick="document.createform.submit()" />
				</sec:authorize>
			</div>
			<div class="col-md-2">
				<sec:authorize ifAllGranted="admin.creation.farmer.create">
					<input type="BUTTON" id="add" class="btn btn-sts form-control"
						value="Add List" onclick="document.createListForm.submit()" />
				</sec:authorize>
			</div>
		</div>
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>
	
	<s:form name="createform" action="creationFarmer_input">
	</s:form>
	
	<s:form name="createListForm" action="creationFarmer_createList">
	</s:form>

	<script type="text/javascript">
		$(document).ready(function() {
			loadGrid();
		});

		function loadGrid() {
			jQuery("#detail")
					.jqGrid(
							{
								url : 'creationFarmer_data.action',
								pager : '#pagerForDetail',
								datatype : "json",
								styleUI : 'Bootstrap',
								colNames : [ '<s:text name="Section"/>',
										'<s:text name="Component Type"/>',
										'<s:text name="Component Name"/>',
										'<s:text name="createdDate"/>' ],
								colModel : [ {
									name : 'section',
									index : 'customerId',
									width : 125,
									sortable : true
								}, {
									name : 'componentType',
									index : 'customerName',
									width : 125,
									sortable : true
								}, {
									name : 'componentName',
									index : 'personName',
									width : 125,
									sortable : true
								}, {
									name : 'createdDate',
									index : 'createdDate',
									width : 125,
									sortable : false,
									search:false,
								},

								],
								height : 400,
								width : $("#baseDiv").width(), // assign parent div width
								scrollOffset : 0,
								rowNum : 10,
								rowList : [ 20, 45, 60 ],
								sortname : 'id',
								sortorder : "desc",
								viewrecords : true, // for viewing noofrecords displaying string at the right side of the table
								onSelectRow : function(id) {
									document.updateform.id.value = id;
									document.updateform.submit();
								},
								onSortCol : function(index, idxcol, sortorder) {
									if (this.p.lastsort >= 0
											&& this.p.lastsort !== idxcol
											&& this.p.colModel[this.p.lastsort].sortable !== false) {
										$(this.grid.headers[this.p.lastsort].el)
												.find(
														">div.ui-jqgrid-sortable>span.s-ico")
												.show();
									}
								}
							});

			jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {
				edit : false,
				add : false,
				del : false,
				search : false,
				refresh : true
			}) // enabled refresh for reloading grid
			jQuery("#detail").jqGrid('filterToolbar', {
				stringResult : true,
				searchOnEnter : false
			}); // enabling filters on top of the header.

			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
			$(
					'#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id)
							+ ' tr.ui-jqgrid-labels th.ui-th-column').each(
					function(i) {
						var cmi = colModel[i], colName = cmi.name;

						if (cmi.sortable !== false) {
							$(this).find('>div.ui-jqgrid-sortable>span.s-ico')
									.show();
						} else if (!cmi.sortable && colName !== 'rn'
								&& colName !== 'cb' && colName !== 'subgrid') {
							$(this).find('>div.ui-jqgrid-sortable').css({
								cursor : 'default'
							});
						}
					});
		}
	</script>

</body>