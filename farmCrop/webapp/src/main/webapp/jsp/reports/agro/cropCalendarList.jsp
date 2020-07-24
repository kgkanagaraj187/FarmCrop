<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
<style type="text/css">
#modalWindow {
	position:absolute;	
	border-radius:0 0 5px 5px;
	padding:15px;
	z-index:1000;
	top:50%;
	left:50%;
	width:620px;
	height:460px;
	margin-left:-310px;
	margin-top:-230px;
}
</style>
</head>
<script type="text/javascript">

	$(document).ready(function(){		
    	loadCustomPopup();
		jQuery("#detail").jqGrid(
				{
				   	url:'cropCalendar_data.action',
				 	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',	
				   	postData:{				 
						 
					},	
				   	colNames:[
				   	      
				  		   	/* '<s:text name="harvestDate"/>',				  		  
				  		   	 '<s:text name="name"/>',  */
				  		   '<s:text name="cSeasonCode"/>',
				  		   	  '<s:text name="crop"/>',
				  		   	  '<s:text name="variety"/>',
				  		   	'<s:property value="%{getLocaleProperty('calendarView')}" />',
				  		    /*  '<s:text name="cSeasonCode"/>'  */
				  		  
		 	      	 ],
		 	      	colModel:[
		 	      	       
								/* {name:'harvestDate',index:'harvestDate',width:120,sortable:false},								
								{name:'name',index:'name',width:250,sortable:false},  */
								{name:'seasonCode',index:'seasonCode',width:150,sortable:false,search:true,stype:'select',searchoptions: { value: '<s:property value="harvestSeasonFilterList"/>' }},	
								{name:'crop',index:'crop',width:250,sortable:true,search:true},
								{name:'variety',index:'variety',width:250,sortable:true,search:true},
								{name:'button',index:'button',sortable:false,align:'center'},
							   /*  {name:'seasonCode',index:'seasonCode',width:150,sortable:false}  */
								
						   	],
				   	height: 301, 
				    width: $("#baseDiv").width(), // assign parent div width
				    scrollOffset: 0,
				   	rowNum:10,
				   	rowList : [10,25,50],
				    sortname:'id',	
				    autowidth:true,
					shrinkToFit:true,
				    sortorder: "desc",
				    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				   /*  onSelectRow: function(id){ 
					  document.updateform.id.value  =id;
				      document.updateform.submit();      
					},	 */	
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
	
	</script>
<script type="text/javascript">
var $overlay;
var $modal;
var $slider;
function redirectToCalendarView(varietyId,seasonCode){
		
		var heading ='<s:property value="%{getLocaleProperty('sowingActivity')}" />';
		var content="<div id='calendarAct' class='smallmap'></div>";
		enablePopup(heading,content);
		
		loadValues(varietyId,seasonCode);
	}
	
	function loadValues(variety,seasonCode){
		//alert("AAA");
		
		var varietyId=variety;
		var season =seasonCode;
		if (!isEmpty(varietyId) && !isEmpty(season)) {
			$.post("cropCalendar_populateCalendarValues", {				
				selectedVarietyId : varietyId,
				selectedSeason : season
			}, function(data) {
				var jsonData = $.parseJSON(data);
				var table = $("<table/>").addClass("table table-bordered").attr({
					id : "cropActivity"
		});
		var thead = $("<thead/>");
		var tr = $("<tr/>");
		tr.append($("<th/>").append("Activity"));
		tr.append($("<th/>").append("No of Days"));		
		table.append(thead);
		thead.append(tr);
		var tbody = $("<tbody/>");
			$.each(jsonData, function(index, value) {
				var tr2 = $("<tr/>");
				tr2.append($("<td/>").append(value.name));
				tr2.append($("<td/>").append(value.noOfDays));				
				tbody.append(tr2);
			});
		table.append(tbody);
		$('#calendarAct').append(table);
			});
		
		}
	}
function loadCustomPopup(){
	$overlay = $('<div id="modOverlay"></div>');
	$modal = $('<div id="modalWin" class="ui-body-c gmap3"></div>');
	$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
	$close = $('<button id="clsBtn" class="btnCls">X</button>');
	
	$modal.append($slider, $close);
	$('body').append($overlay);
	$('body').append($modal);
	$modal.hide();
	$overlay.hide();

	jQuery("#clsBtn").click(function () {
    	$('#modalWin').css('margin-top','-290px');	
		$modal.hide();
		$overlay.hide();			
		$('body').css('overflow','visible');
	});
}


function enablePopup(head, cont){
	$(window).scrollTop(0); 
	$('body').css('overflow','hidden');
	$(".bjqs").empty();		
	var heading='';
	var contentWidth='100%';
	if(head!=''){
		heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
		contentWidth='92%';
	}
	var content="<div style='width:100%;height:"+contentWidth+"'>"+cont+"</div>";	
	$(".bjqs").append('<li>'+heading+content+'</li>')
	$(".bjqs-controls").css({'display':'block'});
	$('#modalWin').css('margin-top','-290px');
	$modal.show();
	$overlay.show();
	$('#banner-fade').bjqs({
        height      : 482,
        width       : 600,
        showmarkers : false,
        usecaptions : true,
        automatic : true,
        nexttext :'',
        prevtext :'',
        hoverpause : false                                           

    });
}
</script>
<sec:authorize ifAllGranted="service.cropCalendar.create">
	<input type="BUTTON" class="btn btn-sts" id="add"
		value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">
		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>
<s:form name="createform" action="cropCalendar_create">
</s:form>
<s:form name="updateform" action="cropCalendar_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>