<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">

</head>


<script type="text/javascript">
$(document).ready(function(){

	jQuery("#detail").jqGrid(
			{
			url:'harvestSeason_data.action?currentPage='+document.updateform.currentPage.value,
			pager: '#pagerForDetail',
			datatype: "json",
			 styleUI : 'Bootstrap',
			 postData:{
   			  "postdata" :  function(){	
   	   			return  decodeURI(postdata);
   			  } 
   	   	},
			colNames:[
			          <s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
					</s:if>
					 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
					</s:if>
		  		   	  '<s:text name="harvestSeason.code" />',
		  		      '<s:text name="%{getLocaleProperty('harvestSeason.name')}" /> ',
		  		   
		  		      '<s:text name="harvestSeason.fromPeriod" />',
		  		   	  '<s:text name="harvestSeason.toPeriod" />'
		  		   	  
		  		   	  /* '<s:text name="harvestSeason.currentSeason" />' */
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
		      	      {name:'name',index:'name', sortable:true, width:125},
		      	    
		      	   
		      	      {name:'fromPeriod',index:'fromPeriod',sortable:true, width:125,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
		      	      {name:'toPeriod',index:'toPeriod', sortable:true,width:125,searchoptions:{dataInit:datePick,attr:{readonly:true}}},
		      	    
		      	      /* {name:'currentSeason',index:'currentSeason',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>' }}*/
		      	 ],			
		    height: 255, 
			width: $("#baseDiv").width(),
			scrollOffset: 0,
			rowNum:10,
			rowList : [10,25,50],
			sortname: 'id',
			sortorder: 'desc',
			viewrecords: true,
		    onSelectRow: function(id){ 
			  document.updateform.id.value  =id;
			  postDataSubmit();
	          document.updateform.submit();      
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
	retainFields();

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
    postdata =  '';
});

datePick = function(elem)
{	
   jQuery(elem).datepicker({
		   changeYear: true,
	       changeMonth: true,       
	       dateFormat: 'dd-mm-yy',
		   onSelect:function(dateText, inst){	   	 
		   		jQuery("#detail")[0].triggerToolbar();
		   }
	   }
)};


</script>
<sec:authorize ifAllGranted="profile.season.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">
<div style="width: 99%;" id="baseDiv">
<table id="detail"></table>
<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="createform" action="harvestSeason_create">
</s:form>

<s:form name="updateform" action="harvestSeason_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
	<s:hidden name="postdata" id="postdata" />
</s:form>

