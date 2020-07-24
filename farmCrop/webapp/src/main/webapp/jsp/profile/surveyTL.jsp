<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
<style>
select[name='cs.name'], select[name='status'] {
	width: 100px !important;
}

.pdf-icon {
	background: transparent url("../img/pdf_icon2.png") no-repeat scroll
		left top;
	background-size: contain; /* to specify dimensions explicitly */
	background-repeat: no-repeat;
	border: medium none !important;
	box-shadow: none;
	cursor: pointer;
	height: 26px;
	text-indent: -10000px;
	width: 26px !important;
	transition: transform .3s ease-in-out;
	/* to have transiton effect of the zoom in .3 seconds */
	z-index: 100;
}

.pdf-icon:hover {
	transform: scale(2);
	zindex: 101;
} /* to have transform effect of the zoom */
</style>
</head>
<body>
	<script type="text/javascript">
var recordLimit='<s:property value="exportLimit"/>';
//var filterda='';
$(document).ready(function(){	
	var tenant='<s:property value="getCurrentTenantId()"/>';
	var userName='<s:property value="getUsername()"/>';

	var type= '<%=request.getParameter("type")%>';
	
			$(".breadCrumbNavigation").html('');
			$(".breadCrumbNavigation").append("<li><a href='#'>Report</a></li>");
			//$(".breadCrumbNavigation").append("<li><a href='#'>IRP</a></li>");
			$(".breadCrumbNavigation").append("<li><a href='surveyTL_list.action'>Survey Report</a></li>");
			$(".type").val(type);
		 
	 
	 var mydata;
	 $.ajax({
		    url:  "https://api.txtlocal.com/get_survey_results/?apikey=" + encodeURIComponent("vcq07onAi0c-iOGEkpVs9yYKTI4v8xbrifWr9M7Nio")+"&survey_id="+encodeURIComponent("24244"),
		    type: "GET",
		    async:false,

		    success: function (response) {
		    	response.survey_results.forEach((item, i) => {
		    			//item={};
		    		  	item.id = i + 1;
		    		});
		    	mydata=response.survey_results;
		    	console.log(response.survey_results);
		    },
		    error: function(error){
		    	alert("ass")
		        console.log("Something went wrong", error);
		    }
		});
	
	 /* var mydata = [
	               { id: "1", invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
	               { id: "2", invdate: "2007-10-02", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
	               { id: "3", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
	               { id: "4", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
	               { id: "5", invdate: "2007-10-05", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
	               { id: "6", invdate: "2007-09-06", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
	               { id: "7", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
	               { id: "8", invdate: "2007-10-03", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
	               { id: "9", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" }
	        ]; */
	jQuery("#detail").jqGrid(
	{
		
            datatype: "local",
			data: mydata,
            height: 401,
            pager: '#pagerForDetail',
			width: $("#baseDiv").width(),
            colModel: [
                { label: 'Group', name: 'group', width: 75, key:true },
                { label: 'Date/Time', name: 'Date/Time', width: 75 },
                { label: 'FirstName', name: 'FirstName', width: 75 },
                { label: 'LastName', name: 'LastName', width: 75 },
                { label: 'Number', name: 'Number', width: 75 },
                { label: 'Farmer Name', name: 'Farmer Name', width: 75 },
                { label: 'Gender', name: 'Gender', width: 75 },
                { label: 'Other Source of Income', name: 'OtherSourceofIncome', width: 75 },
                { label: 'Complete', name: 'Complete', width: 75 }
               /*  { label: 'Number', name: 'Number', width: 90 },
                { label: 'FirstName', name: 'FirstName', width: 100 },
                { label: 'LastName', name: 'LastName', width: 80 },
                { label: 'Date/Time', name: 'Date/Time', width: 80 },
                { label: 'Device', name: 'Device', width: 80 },
                { label: 'Farmer Name', name: 'Farmer Name', width: 150 },
                { label: 'Gender', name: 'Gender', width: 150 },
                { label: 'OtherSourceofIncome', name: 'OtherSourceofIncome', width: 150 } */
            ],
            viewrecords: true, // show the current page, data rang and total records on the toolbar
            rowNum:25,
    	   	rowList : [25,50,75]
            //caption: "Load jqGrid through Javascript Array",
       
    });
	   jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
		jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
		
			retainFields();     //For retaining filter fields
	
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
    $('#detail').jqGrid('setGridHeight',(windowHeight));
     postdata =  '';
    
	
});	      













</script>

	<div class="appContentWrapper marginBottom">
		

		<div id="baseDiv" style="width: 99%">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>

</body>
