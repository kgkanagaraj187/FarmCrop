<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>

<body>

	<script type="text/javascript">
$(document).ready(function(){	

	loadCottonPrice();
	var size='<s:property value="dataSize"/>';
	var branch='<s:property value="branchId"/>';
	if(size<1 && branch!=''){
		
		$(".hideAddBtn").show();
	}
	else
		{
		$(".hideAddBtn").hide();
		
		}
	

});


function isNumber(evt) {
	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}


function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}



function loadCottonPrice()
{
	


	jQuery("#priceDetail").jqGrid(
			{
			   	url:'cottonPrice_data.action',
			   	editurl:'cottonPrice_populateUpdate',
			   	pager: '#pagerForPriceDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
     			  		    '<s:property value="%{getLocaleProperty('seasonCode')}"/>',
			  		   	'<s:property value="%{getLocaleProperty('staple')}"/>',
/*     			  		 '<s:property value="%{getLocaleProperty('pricess')}"/>',
 */			  		  '<s:property value="%{getLocaleProperty('mspRate')}"/>',
			  		  	'<s:text name="Actions"/>'
						 ],
			   	colModel:[
						<s:if test='branchId==null'>
					   		{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
					   			value: '<s:property value="parentBranchFilterText"/>',
					   		/* 	dataEvents: [ 
					   			          {
					   			            type: "change",
					   			            fn: function () {
					   			            	console.log($(this).val());
					   			             	getSubBranchValues($(this).val())
					   			            }
					   			        }] */
					   			
					   			}},	   				   		
					   		</s:if>
					   		
			   			{name:'seasonCode',index:'seasonCode',width:125,sortable:true,search:false,search:true,stype: 'select',searchoptions: { value: '<s:property value="seasonFilter"/>' }},
			   			{name:'stapleLength',index:'stapleLength',width:125,sortable:true,search:false,search:true,stype: 'select',searchoptions: { value: '<s:property value="stapleLengthFilter"/>' }},

			   	/* 		{name:'price',index:'price',width:90,sortable:true,editable: true,edittype: "text",search:false, editrules:{required: true,number:true},
			   
			   			}, */
			   			{name:'msp',index:'msp',width:90,sortable:true,editable: true,search:false,edittype: "text",editrules:{required: true,number:true},
			                 
			                      
					   		}, 

			   			{name:'act',index:'act',width:60,sortable:false,search:false,formatter: "actions",formatoptions: {keys: true,
						   	delOptions: { url: 'cottonPrice_populateDelete.action' ,
						   			afterShowForm:function ($form) {
	                            	    $form.closest('div.ui-jqdialog').position({
	                            	        my: "center",
	                            	        of: $("#priceDetail").closest('div.ui-jqgrid')
	                            	    });
	                            	},	
	                            	
	                            	afterSubmit: function(data) 
	                                {
	                            	  var json = JSON.parse(data.responseText);
										//document.getElementById("validateErrorCate").innerHTML=json.msg;
										jQuery("#priceDetail").jqGrid('setGridParam',{url:"cottonPrice_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
										showPopup(json.msg,json.title);
										
										jQuery('.ui-jqdialog-titlebar-close').click();
										
	                                }
						   	},
						   		
						   		
						   	 afterSave: function (id, response, options) {
	                             var json = JSON.parse(response.responseText);
									jQuery("#priceDetail").jqGrid('setGridParam',{url:"cottonPrice_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
									
	                         } 
	                         
	             
	                         
	                  
					   		}}
			   		   
						 ],
			   	height: 301, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			  
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        },
		        loadComplete: function() {
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
	
/* 			jQuery("#priceDetail").jqGrid('setGroupHeaders', {
				  useColSpanStyle: false, 
				  groupHeaders:[
					{startColumnName: 'noOfPositive', numberOfColumns: 3, titleText: '<em>Raw Cotton</em>'},
					{startColumnName: 'closed', numberOfColumns: 2, titleText: 'Shiping'}
				  ]
				}); */
	
	
			jQuery("#priceDetail").jqGrid('navGrid','#pagerForPriceDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#priceDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#priceDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#priceDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#priceDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#priceDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#priceDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;
		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		   /*  windowHeight=windowHeight-80; */
		    $('#priceDetail').jqGrid('setGridHeight',(windowHeight));
	
}

function addCottonPrice() {
	
var hit = true;
	var staple = $("#stapleLength").val();
	var season = $("#season").val();
	var price = $("#price").val();
	var mspRate = $("#mspRate").val();
	jQuery(".error").empty();
	
	if (isEmpty(season))
	{
		jQuery(".error").append('<s:text name="empty.season"/>');
		hit = false;
	
	}
	else if (isEmpty(staple)) {
		jQuery(".error").append('<s:text name="empty.staple"/>');
		hit = false;
		return false;
	}
	else if (isEmpty(mspRate))
	{
		jQuery(".error").append('<s:text name="empty.mspRate"/>');
		hit = false;
	
	}
	
	
	
	if (hit) {
		var cottonPriceDatas='';
			var dataa = {
					msp:mspRate,
					stapleLength:staple,
					seasonCode:season
			}
			$.ajax({
				 url:'cottonPrice_populateSavePrice.action',
			     type: 'post',
			     dataType: 'json',
			     data: dataa,
			     success: function(result) {
						jQuery("#priceDetail").jqGrid('setGridParam',{url:"cottonPrice_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
				    	 resetCPriceData();
				 showPopup(result.msg,result.title);
			     }
			   
			});
		

	
	}

}

function resetCPriceData()
{
	
	$("#stapleLength").val('');
	$("#mspRate").val('');
	
}
</script>
	<div id="dialog" style="display: none"></div>

	<s:form action="cottonPrice_populateSavePrice" name="priceForm"
		id="priceForm">
		<sec:authorize ifAllGranted="profile.gmo.list">
			<a data-toggle="collapse" data-parent="#accordion"
				href="#gmoAccordian" class="btn btn-sts margin-bottom10px "> <s:text
					name="create.button" />
			</a>
		</sec:authorize>
		<div id="gmoAccordian" class="panel-collapse collapse">

			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>
						<s:property value="%{getLocaleProperty('info.prodPrice')}" />
					</h2>
				</div>
				<div class="error">
					<s:actionerror />
					<s:fielderror />
				</div>

				<div class="row">

					<div class="flex-layout filterControls">


						<div class="flexItem col-md-4">
							<label for="txt"><s:property
									value="%{getLocaleProperty('season')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element flexdisplay">
								<s:select class="form-control input-sm select2 " 
									list="seasonList" headerKey="" name="selectedSeason" id="season"
									value="CurrentSeason"
									/>


							</div>
						</div>

						<div class="flexItem col-md-4">
							<label for="txt"><s:property
									value="%{getLocaleProperty('staple')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element flexdisplay">
								<s:select list="stapleLengthList" id="stapleLength"
									class="form-control input-sm" name="stapleLength" headerKey=""
									headerValue="%{getText('txt.select')}" />

							</div>
						</div>
						<div class="flexItem col-md-4">
							<label for="txt"><s:property
									value="%{getLocaleProperty('mspRate')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element flexdisplay">
								<s:textfield name="mspRate" theme="simple" maxlength="35"
									onkeypress="return isDecimal(event)" cssClass="form-control "
									id="mspRate" />

							</div>
						</div>
					</div>


				</div>
				<div class="flex-layout" style="margin-top: 1%">
					<div class="flexItem">
						<button type="button" class="btn btn-sts" id="add"
							onclick="addCottonPrice()">
							<b><s:text name="save.button" /></b>
						</button>
					</div>
				</div>
			</div>
		</div>
	</s:form>



	<div class="appContentWrapper marginBottom">
		<div style="width: 99%;" id="baseDiv">
			<table id="priceDetail"></table>
			<div id="pagerForPriceDetail"></div>
		</div>
	</div>

</body>