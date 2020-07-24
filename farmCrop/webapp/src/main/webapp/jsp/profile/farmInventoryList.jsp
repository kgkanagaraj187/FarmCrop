

<script type="text/javascript">
$(document).ready(function(){	
	$("#flex1").flexigrid
			(
			{
			url: 'farmInventory_data.action?farmId='+document.updateform.farmId.value+'&currentPage='+document.updateform.currentPage.value,
			dataType: 'json',
			sortname: 'id',
			sortorder: 'asc',
			resizable: false,
			usepager: true,
			striped: true,
			qtype:'code',
			providerp: true,
		    nowrap: true, //		
			title: '',
			showTableToggleBtn: true,	
			multiSelect: false	,	
			
		  colModel : [			            		
         	 	{display:  '<b><s:text name="farmInventory.inventoryItem"/>', name : 'inventoryItem', sortable: true, width : 420, align: 'left'},
         	 	{display:  '<b><s:text name="farmInventory.itemCount"/>', name : 'itemCount', sortable: true, width : 420, align: 'left'}
				],
				onSubmit    : edit(), 			
				
			buttons : [ <sec:authorize ifAllGranted="profile.farm.create">
				{name: '<s:text name="add.button"/>', bclass: 'add', onpress : add}	</sec:authorize>		
				],				
			searchitems : [			
			               {display: '<b><s:text name="farmInventory.inventoryItem"/>', name : 'inventoryItem'},
			               {display: '<b><s:text name="farmInventory.itemCount"/>', name : 'itemCount'}
          ]					
			}			
			); 	 
	
       });

function add(com, grid) {
	   document.createform.submit();
	   }

function edit()
{   
    $('#flex1').click(function(event){
    	$('.trSelected', this).each( function(){
    	   var v=$("#flex1").flexGetPageNumber();        	            		    	     
    	   document.updateform.id.value  =$(this).attr('id').substr(3);
    	   document.updateform.currentPage.value =v;
	       document.updateform.submit();      
   		 });
	});
  }

</script>
<table id="flex1" style="display: table; position: absolute;"></table>
<s:form name="createform" action="farmInventory_create">
   <s:hidden name="farmId"  />
	<s:hidden key="currentPage"/>
</s:form>

<s:form name="updateform" action="farmInventory_detail">
	<s:hidden name="farmId" />
	<s:hidden key="id"/>
	<s:hidden key="currentPage"/>
</s:form>