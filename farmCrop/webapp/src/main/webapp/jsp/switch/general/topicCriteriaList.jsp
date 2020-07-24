<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/ese-tags" prefix="e"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
<style>
	select{
		width : 100px !important;
	}
</style>
</head>
<div id='warn' class="error">
    <s:actionerror/>
</div>

<script type="text/javascript">
$(document).ready(function(){	

	jQuery("#detail").jqGrid(
			{
			url:'topicCriteria_data.action',
			pager: '#pagerForDetail',
			mtype: 'POST',
			datatype: "json",
			styleUI : 'Bootstrap',
			colNames:[	
					<s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
					</s:if>
		  		   	  '<s:text name="topic.Criteriacode" />',
		  		      '<s:text name="topic.Criteriaprinciple" />',
		  		      '<s:text name="topic.topicCategory.name" />'
		      	 ],
		   colModel : [	
		   			<s:if test='branchId==null'>
			   		{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
			   			value: '<s:property value="parentBranchFilterText"/>',
			   			dataEvents: [ 
			   			          {
			   			            type: "change",
			   			            fn: function () {
			   			            	console.log($(this).val());
			   			             	getSubBranchValues($(this).val())
			   			            }
			   			        }
			   			    ]
			   			
			   			}},	   				   		
			   		</s:if>
			   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
			   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
			   		</s:if>
		      	      {name:'code',index:'code',sortable:true, width:125},
		      	      {name:'principle',index:'principle', sortable:true, width:125},
		      	      {name:'topicCategory.name',index:'topicCategory.name',sortable:true, width:125, search:true, stype: 'select', searchoptions: { value: '<s:property value="topicCategoryFilterComboData"/>' }}
		      	 ],			
		    height: 301, 
			width: $("#baseDiv").width(),
			scrollOffset: 0,
			rowNum:10,
			rowList : [10,25,50],
			sortname: 'id',
			sortorder: 'desc',
			viewrecords: true,
		    onSelectRow: function(id){ 
			  document.detailform.id.value =id;
	          document.detailform.submit();      
			},		
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        }
	   });
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});			

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

</script>

<sec:authorize ifAllGranted="profile.trainingMaster.topicCriteria.create">
	<input type="BUTTON" class="btn btn-sts id="add"  value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
</sec:authorize>
	
<div style="width: 99%;" id="baseDiv">

<table id="detail"></table>

<div id="pagerForDetail"></div>
</div>

<s:form id="createform" name="createform" action="topicCriteria_create">
	<s:hidden id="deviceType" name="deviceType" />
	<s:hidden name="command"/>
</s:form>

<s:form name="detailform" id="detailform"
	action="topicCriteria_detail">
	<s:hidden id="id" name="id" />
	<s:hidden id="currentPage" name="currentPage"/>
	<s:hidden name="command"/>
</s:form>

