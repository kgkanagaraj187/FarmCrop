<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet"
	href="plugins/selectize/css/selectize.bootstrap3.css">
<script src="plugins/selectize/js/standalone/selectize.min.js"></script>
<link rel="stylesheet" href="plugins/select2/select2.min.css">
<script src="plugins/select2/select2.min.js"></script>
<style>
table thead tr th {
	text-align: center;
}

.select2-selection {
	width: 150px;
}
</style>

<script type="text/javascript">
jQuery(document).ready(function(){
var farmerId=document.getElementById('farmerId').value;
var farmList="";
var dataArr = new Array();
	//loadFarm(farmerId);
loadFieldHistoryGrid();   
		
	});	
	
/* function loadFarm(call) {
	dataArr = new Array();
	   var selectedFarmer = document.getElementById('farmerId').value;
	   if(selectedFarmer !=null || selectedFarmer !=""){
		   $.post("inspectionCheck_populateFarm", {farmerId: selectedFarmer}, function (data) {
			     alert("----"+data);
			 //  $('#farmList').attr("id");
			 //  var farmerId=document.getElementById('farmerId').value;
			     // insertOptions( $('#farmList').attr("id"),$.parseJSON(data));
			     
			      var temp=$.parseJSON(data);
	    	 $(temp).each(function(k, v){
	    		 alert(v.id);
	    		 dataArr.push({
	    			 farmList:v.id 
	    		 });
	    	 });
	    	alert(dataArr);
	   });
	   
	     
	  }
	   loadInspectionCheckGrid();   
}
 */


function loadFieldHistoryGrid(){
	jQuery("#fieldHistoryDetail").jqGrid({
		 url:'fieldHistory_data.action?id='+ document.getElementById('farmerId').value,
       pager: '#fieldHistoryPagerForDetail',
       mtype: 'POST',
       datatype: "json",
       mtype: 'POST',			
	   	
       colNames:[
               //'<s:text name="farmcrops.code"/>',
               //'<s:text name="farmcrops.name"/>',
               '<s:text name="date"/>',
               '<s:text name="createdUser"/>',
               '<s:text name="season"/>',
               '<s:text name="farm"/>',
               '<s:text name="farmer"/>',
               '<s:property value="%{getLocaleProperty('state')}" />',
               /* '<s:text name="group"/>' */
               <%--<sec:authorize ifAllGranted="profile.farmCrop.delete">,
'<s:text name="Action"/>'</sec:authorize>--%>

       ],
       colModel:[
               //{name:'fcm.code',index:'fcm.code', width:125,sortable:true},
                       //{name:'fcm.name',index:'fcm.name', width:125,sortable:true},
                       {name:'date', index:'date', width:125, sortable:true, search:false},
               {name:'createdUser', index:'createdUser', width:125, sortable:true, search:false},
               {name:'farmer', index:'farmer', width:125, sortable:false, search:false},
               {name:'farm', index:'farm', width:125, sortable:false, search:false},
               {name:'village', index:'village', width:125, sortable:true, search:false},
               {name:'group', index:'group', width:125, sortable:true, search:false},
               <%--<sec:authorize ifAllGranted="profile.farmCrop.delete">,
{name:'action',index:'action',width:125,sortable:false,search:false}</sec:authorize>--%>
               ],
                       height: 301,
                       width: $("#baseDiv").width(),
                       scrollOffset: 0,
                       rowNum:10,
                       rowList : [10, 25, 50],
                       sortname:'id',
                       sortorder: "desc",
                       viewrecords: true,
                       subGrid: false,
                       beforeSelectRow: 
       			        function(id, e) {
       			    	var iCol = jQuery.jgrid.getCellIndex(e.target);
       			    	var rowData = jQuery("#detail").getRowData(id);
       			        
       			        }, 
      			 	onSelectRow: function(id){ 
      			 		$('#updateform').prop('action','fieldHistory_detail.action');
      			 		var act =  $('#updateform').prop('action') ;
      			 		if(act.indexOf("?txnType=")){
      				act = act.split("?txnType=")[0]+"?txnType=2002&id="+id;
      			}
      			 			
      			 		document.getElementById("updateform").action =act;
      					//  document.updateform.typez.value  =id;
      					document.getElementById("updateform").submit();
      			        //  document.updateform.submit();       
      					},
      			
      			
      	        onSortCol: function (index, idxcol, sortorder) {
      		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
      	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
      	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
      	            }
      	        }
      			});
      			jQuery("#clear").click( function() {
      				clear();
      			});	

      			colModel = jQuery("#fieldHistoryDetail").jqGrid('getGridParam', 'colModel');
      		    $('#gbox_' + $.jgrid.jqID(jQuery("#fieldHistoryDetail")[0].id) +
      		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
      		        var cmi = colModel[i], colName = cmi.name;
      		
      		        if (cmi.sortable !== false) {
      		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
      		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
      		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
      		        }
      		    });	
      	
      		    $('#fieldHistoryDetail').jqGrid('setGridHeight',(reportWindowHeight));
                   }
                   



</script>


<div style="width: 99%;" id="baseDiv">
	<table id="fieldHistoryDetail"></table>
	<div id="fieldHistoryPagerForDetail"></div>
</div>

<s:form name="updateform" id="updateform"
	action="fieldHistory_detail.action">
	<%-- 	<s:hidden key="id" /> --%>
	<s:hidden key="currentPage" />
	<s:hidden id="txnType" name="txnType" />
	<s:hidden id="reportType" name="entityType" />
</s:form>

