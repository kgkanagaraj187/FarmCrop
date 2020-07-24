<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">
$(document).ready(function(){	
	loadGinnerQty();
});

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}
function loadGinnerQty()
{
	jQuery("#ginnerQtyDetail").jqGrid(
			{
			   	url:'ginnerQuantitySold_data.action',
			   	editurl:'ginnerQuantitySold_populateUpdate',
			   	pager: '#pagerForDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
     			  		    '<s:property value="%{getLocaleProperty('seasonCode')}"/>',
			  		    '<s:property value="%{getLocaleProperty('ginnerName')}"/>',
    			  		 '<s:property value="%{getLocaleProperty('ginnerQty')}"/>',
    			  		 '<s:property value="%{getLocaleProperty('ginnerAddress')}"/>',
			  		  	'<s:text name="Actions"/>'
						 ],
			   	colModel:[
						<s:if test='branchId==null'>
					   		{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
					   			value: '<s:property value="parentBranchFilterText"/>',
					   		
					   			}},	   				   		
					   		</s:if>
					   		
			   			{name:'seasonCode',index:'seasonCode',width:125,sortable:true,search:false,search:true,stype: 'select',searchoptions: { value: '<s:property value="seasonFilter"/>' }},
			   			{name:'ginnerName',index:'ginnerName',width:90,sortable:true,editable: true,search:false,edittype: "text", editrules:{required: true}}, 
			   			{name:'ginnerQty',index:'ginnerQty',width:90,sortable:true,editable: true,edittype: "text",search:false, editrules:{required: true}},
			   			{name:'ginnerAddress',index:'ginnerAddress',width:90,sortable:true,search:false,editable: true,edittype: "text",search:false},
			   			{name:'act',index:'act',width:60,sortable:false,search:false,formatter: "actions",formatoptions: {keys: true,
						   	delOptions: { url: 'ginnerQuantitySold_populateDelete.action' ,
						   			afterShowForm:function ($form) {
	                            	    $form.closest('div.ui-jqdialog').position({
	                            	        my: "center",
	                            	        of: $("#ginnerQtyDetail").closest('div.ui-jqgrid')
	                            	    });
	                            	},	
	                            	
	                            	afterSubmit: function(data) 
	                                {
	                            	  var json = JSON.parse(data.responseText);
										//document.getElementById("validateErrorCate").innerHTML=json.msg;
										jQuery("#ginnerQtyDetail").jqGrid('setGridParam',{url:"ginnerQuantitySold_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
										showPopup(json.msg,json.title);
										
										jQuery('.ui-jqdialog-titlebar-close').click();
										
	                                }
				   		
						   		} ,
						   		
						   	 afterSave: function (id, response, options) {
	                             var json = JSON.parse(response.responseText);
									jQuery("#ginnerQtyDetail").jqGrid('setGridParam',{url:"ginnerQuantitySold_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

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
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
	
/* 			jQuery("#ginnerQtyDetail").jqGrid('setGroupHeaders', {
				  useColSpanStyle: false, 
				  groupHeaders:[
					{startColumnName: 'noOfPositive', numberOfColumns: 3, titleText: '<em>Raw Cotton</em>'},
					{startColumnName: 'closed', numberOfColumns: 2, titleText: 'Shiping'}
				  ]
				}); */
	
	
			jQuery("#ginnerQtyDetail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#ginnerQtyDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#ginnerQtyDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#ginnerQtyDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#ginnerQtyDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#ginnerQtyDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#ginnerQtyDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		   /*  windowHeight=windowHeight-80; */
		    $('#ginnerQtyDetail').jqGrid('setGridHeight',(windowHeight));
	
}

function addGinnerQty() {
	var hit = true;
	var name = $("#ginnerName").val();
	var qty = $("#ginnerQty").val();
	var address = $("#ginnerAddress").val();
	jQuery(".error").empty();
	if (isEmpty(name)) {
		jQuery(".error").append('<s:text name="empty.ginnerName"/>');
		hit = false;
	} else if(isEmpty(qty))
		{
		jQuery(".error").append('<s:text name="empty.ginnerQty"/>');
		hit = false;
		
	}

	if (hit) {
		var dataa = {
				ginnerName:name,
				ginnerQty:qty,
				ginnerAddress:address
		}
		
		$.ajax({
			 url:'ginnerQuantitySold_populateSave.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 jQuery("#ginnerQtyDetail").jqGrid('setGridParam',{url:"ginnerQuantitySold_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	 resetGinnerQtyData();
		    	 showPopup(result.msg,result.title);
		     }
		   
		});
	
	}

}


function resetGinnerQtyData()
{
	$("#ginnerName").val('');
	$("#ginnerAddress").val('');
	$("#ginnerQty").val('');
	
}
</script>
	<div id="dialog" style="display: none"></div>

	<div id="gmo">
		<s:form name="gmoForm" id="gmoForm">
			<sec:authorize ifAllGranted="profile.ginnerQuantitySold.list">
				<a data-toggle="collapse" data-parent="#accordion"
					href="#ginnerQtySoldAccordian"
					class="btn btn-sts margin-bottom10px"> <s:text
						name="create.button" />
				</a>
			</sec:authorize>

			<div id="ginnerQtySoldAccordian" class="panel-collapse collapse">

				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<h2>
							<s:property value="%{getLocaleProperty('info.ginner')}" />
						</h2>
					</div>
					<div class="error">
						<s:actionerror />
						<s:fielderror />
					</div>

					<div class="row">

						<div class="flex-layout filterControls">
							<div class="flexItem col-md-2">
								<label for="txt"><s:property
										value="%{getLocaleProperty('ginnerName')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="ginnerName" theme="simple"
										maxlength="35" 	cssClass="form-control "
										id="ginnerName" />

								</div>
							</div>
							
								<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('ginnerQty')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="ginnerQty" theme="simple"
										maxlength="10" onkeypress="return isDecimal(event)" 
										cssClass="form-control "
										id="ginnerQty" />

								</div>
							</div>
							
							
								<div class="flexItem col-md-6">
								<label for="txt"><s:property
										value="%{getLocaleProperty('ginnerAddress')}" /></label>
								<div class="form-element flexdisplay">
									<s:textarea name="address" theme="simple"  cssStyle="height:40px"
										maxlength="150"  cssClass="form-control "
										id="ginnerAddress" />

								</div>
							</div>

						</div>

					</div>
					<div class="flex-layout" style="margin-top: 1%">
						<div class="flexItem">
							<button type="button" class="btn btn-sts" id="add"
								onclick="addGinnerQty()">
								<b><s:text name="save.button" /></b>
							</button>
						</div>
					</div>
				</div>
			</div>
		</s:form>
	</div>



	<div class="appContentWrapper marginBottom">
		<div style="width: 99%;" id="baseDiv">
			<table id="ginnerQtyDetail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>

</body>