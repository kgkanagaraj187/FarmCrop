<%@ taglib uri="/struts-tags" prefix="s"%>

<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript"><!--

$(document).ready(function(){	
	$("#flex1").flexigrid
			(
			{
			url: 'farmerFamily_data.action?farmerId='+document.getElementById("farmerid").value+'&currentPage='+document.detailForm.currentPage.value,
			dataType: 'json',
			sortname: 'id',
			sortorder: 'asc',
			resizable: false,
			usepager: true,
			striped: true,
			qtype:'providername',
		    useRp: true,
		    nowrap: true, //		 
			width: 850,	
			height: 270,					
			rp: 10,	
			page: 1, //current page
			total: 1, //total pages				
			blockOpacity: 0.5,
			title: '',
			showTableToggleBtn: true,	
			multiSelect: false	,
			
		  colModel : [			            		
		              {display: '<b><s:text name="farmerFamilyName" /></b>', name : 'name', sortable: true, width : 215, align: 'left'},
		              {display: '<b><s:text name="farmerFamilyAge" /></b>', name : 'age', sortable: true, width : 200, align: 'left'},
		              {display: '<b><s:text name="farmerFamilyRelation" /></b>', name : 'relation', sortable: true, width : 200, align: 'left'},
		              {display: '<b><s:text name="farmerFamilyEducation" /></b>', name : 'education', sortable: true, width : 200, align: 'left'},
			               
     
				],	
				onSubmit    : edit(), 
				buttons : [
						<sec:authorize ifAllGranted="profile.farmer.update">
							{name: '<s:text name="add.button"/>', bclass: 'add', onpress : doCommand}	
						</sec:authorize>		
							],			
			
				
            		searchitems : [			
            {display: '<b><s:text name="farmerFamilyName" /></b>', name : 'name'},
            {display: '<b><s:text name="farmerFamilyAge" /></b>', name : 'age'},   
            {display: '<b><s:text name="farmerFamilyRelation" /></b>', name : 'relation'},
            {display: '<b><s:text name="farmerFamilyEducation" /></b>', name : 'education'},  
                       
          		]					
			}			
			); 	 
	
       });
	
	function submitCreateForm(){		
		document.getElementById("createForm").submit();
	}

	function edit()
    {   
	    $('#flex1').click(function(event){
	    	$('.trSelected', this).each( function(){
	    	   var v=$("#flex1").flexGetPageNumber();        	            		    	     
	    	   document.detailForm.id.value =$(this).attr('id').substr(3);
	    	   document.detailForm.currentPage.value =v;
		       document.detailForm.submit();	      
	   		 });
		});
	  }
    
	function doCommand(com, grid) {	 	      		
	     if (com =='<s:text name="add.button"/>') {		   
	    	 submitCreateForm();	    
	     } 
	   }       
	
--></script>


<table id="flex1" style="display: table;"></table>
<s:submit value="Add" onclick="submitCreateForm();"
	cssStyle="display:none;"></s:submit>
<s:form id="createForm" action="farmerFamily_create">
	<s:hidden id="id" name="id" />
	<s:hidden id="currentPage" name="currentPage" />
	<s:hidden name="tabIndex" />
	<s:hidden name="command" />
</s:form>
<s:form name="detailForm" id="detailForm" action="farmerFamily_detail">
	<s:hidden key="id" />
	<s:hidden id="currentPage" name="currentPage" />
	<s:hidden name="tabIndex" />
	<s:hidden name="command" />
</s:form>
<s:hidden id="farmerid" name="farmerId" />

</body>
