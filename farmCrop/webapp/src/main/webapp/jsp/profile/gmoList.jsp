<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<style type="text/css">
.glyphicon-trash::before {
	content: "";
}
</style>
<body>

	<script type="text/javascript">
	var size="";
$(document).ready(function(){	

	loadGmo();
	var branch='<s:property value="branchId"/>';
	if( branch!=''){
		
		$(".hideAddBtn").show();
	}
	else
		{
		$(".hideAddBtn").hide();
		
		}
	

});


function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}


function isNumber(evt) {
	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}



function loadGmo()
{
	


	jQuery("#gmoDetail").jqGrid(
			{
			   	url:'gmo_data.action',
			   	editurl:'gmo_populateGmoUpdate',
			   	pager: '#pagerForgmoDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
     			  		    '<s:property value="%{getLocaleProperty('seasonCode')}"/>',
			  		   	'<s:property value="%{getLocaleProperty('noOfSamples')}"/>',
    			  		 '<s:property value="%{getLocaleProperty('noOfPositive')}"/>',
			  		  '<s:property value="%{getLocaleProperty('contaminPercentage')}"/>',
			  		  '<s:property value="%{getLocaleProperty('type')}"/>',
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
			   			
			   			{name:'noOfSamples',index:'noOfSamples',width:90,sortable:true,editable: true,edittype: "text",search:false, editrules:{required: true,decimal:true},
			                editoptions: {
		                		dataEvents: [ 
		   			   			          {
		   			   			            type: "keyup",
		   			   			            fn: function () {
		   			   			          var tmp=0;
		   			   			            	//alert($(this).val());
		   			   			            	var id=$(this).closest('tr').attr('id')+"_noOfSamples";
		   			   			          		var cid=$(this).closest('tr').attr('id')+"_noOfPositive";
		   			   			         		var samples=$("#"+id).val();
		   			   			         		var positive=$("#"+cid).val();
		   			   			          if(parseFloat(samples)>0)
					   			   			{
	   			   			          		 tmp= (positive/samples)*100;

					   			   			}
		   			   			          		var targetId=$(this).closest('tr').attr('id')+"_contaminPercent";
		   			   			          		$("#"+targetId).attr("disabled","disabled");
		   			   			          		$("#"+targetId).val(tmp.toFixed(2));
		   			   			            }
		   			   			        }]
		                        }
			   			},
			   			{name:'noOfPositive',index:'noOfPositive',width:90,sortable:true,editable: true,search:false,edittype: "text",editrules:{required: true,decimal:true},
			                  editoptions: {
			                		dataEvents: [ 
			   			   			          {
			   			   			            type: "keyup",
			   			   			            fn: function () {
			   			   			            	//alert($(this).val());
			   			   			            	var tmp=0;
			   			   			            	var id=$(this).closest('tr').attr('id')+"_noOfSamples";
			   			   			          		var cid=$(this).closest('tr').attr('id')+"_noOfPositive";
			   			   			     		var samples=$("#"+id).val();
		   			   			         		var positive=$("#"+cid).val();
		   			   			          if(parseFloat(samples)>0)
					   			   			{
	   			   			          		 tmp= (positive/samples)*100;

					   			   			}
			   			   			          		var targetId=$(this).closest('tr').attr('id')+"_contaminPercent";
			   			   			          		$("#"+targetId).val(tmp.toFixed(2));
			   			   			          		
			   			   			            }
			   			   			        }]
			   			
			   	   	
			                        }
			                      
					   		}, 
				   		{name:'contaminPercent',index:'contaminPercent',width:125,sortable:true,search:false,editable: true,edittype: "text",edittype: "text", editoptions: { size: 10, readonly: 'readonly'}},
			   			{name:'type',index:'type',width:125,sortable:true,search:false,search:true,stype: 'select',searchoptions: { value: ':<s:text name="type"/>;1:<s:text name="gmoType1"/>;2:<s:text name="gmoType2"/>;3:<s:text name="gmoType3"/>' }},	

			   			{name:'act',index:'act',width:60,sortable:false,search:false,formatter: "actions",formatoptions: {keys: true,
				/* 		   	delOptions: { url: 'gmo_populateDelete.action' ,
						   			afterShowForm:function ($form) {
	                            	    $form.closest('div.ui-jqdialog').position({
	                            	        my: "center",
	                            	        of: $("#gmoDetail").closest('div.ui-jqgrid')
	                            	    });
	                            	},	
	                            	
	                            	afterSubmit: function(data) 
	                                {
	                            	  var json = JSON.parse(data.responseText);
										//document.getElementById("validateErrorCate").innerHTML=json.msg;
										jQuery("#gmoDetail").jqGrid('setGridParam',{url:"gmo_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
										showPopup(json.msg,json.title);
										
										jQuery('.ui-jqdialog-titlebar-close').click();
										
	                                }
				   		
						   		} , */
						   		
						   		
						   	 afterSave: function (id, response, options) {
	                             var json = JSON.parse(response.responseText);
									jQuery("#gmoDetail").jqGrid('setGridParam',{url:"gmo_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
									
	                         } ,
	                         
	              	                  
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
	
/* 			jQuery("#gmoDetail").jqGrid('setGroupHeaders', {
				  useColSpanStyle: false, 
				  groupHeaders:[
					{startColumnName: 'noOfPositive', numberOfColumns: 3, titleText: '<em>Raw Cotton</em>'},
					{startColumnName: 'closed', numberOfColumns: 2, titleText: 'Shiping'}
				  ]
				}); */
	
	
			jQuery("#gmoDetail").jqGrid('navGrid','#pagerForgmoDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#gmoDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#gmoDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#gmoDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#gmoDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#gmoDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#gmoDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;
		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		   /*  windowHeight=windowHeight-80; */
		    $('#gmoDetail').jqGrid('setGridHeight',(windowHeight));
	
}

function addGMO() {
	var hit = true;
	var samplesRaw = $("#noOfSamplesRaw").val();
	var positiveRaw = $("#noOfPositiveRaw").val();
	var samplesLeaf = $("#noOfSamplesLeaf").val();
	var positiveLeaf = $("#noOfPositiveLeaf").val();
	var samplesSeed = $("#noOfSamplesSeed").val();
	var positiveSeed = $("#noOfPositiveSeed").val();
	var season = $("#season").val();
	jQuery(".error").empty();
	if (isEmpty(samplesRaw)  && isEmpty(samplesRaw)  && isEmpty(samplesLeaf) && isEmpty(samplesSeed)) {
		jQuery(".error").append('<s:text name="empty.noOfSamples"/>');
		hit = false;
		return false;
	} else if (isEmpty(positiveRaw) && isEmpty(positiveLeaf) && isEmpty(positiveSeed) )
		{
		jQuery(".error").append('<s:text name="empty.noOfPositive"/>');
		hit = false;
		
	}
	
	else if (parseFloat(positiveRaw)>parseFloat(samplesRaw))
	{
		jQuery(".error").append('<s:text name="lessthan.rawCotton"/>');
		hit = false;
	
	}
	else if(parseFloat(positiveLeaf)>parseFloat(samplesLeaf))
	{
		jQuery(".error").append('<s:text name="lessthan.leafCotton"/>');
		hit = false;
	}
	else if(parseFloat(positiveSeed)>parseFloat(samplesSeed))
	{
	
		jQuery(".error").append('<s:text name="lessthan.seedCotton"/>');
		hit = false;
	}
	
	if (hit) {
		var rawPercent = $("#rawPercentage").text();
		var leafPercent = $("#leafPercentage").text();
		var seedPercent = $("#seedPercentage").text();
		var gmoDatas='';
		if(!isEmpty(samplesRaw) && !isEmpty(positiveRaw))
		{
			
			gmoDatas +=samplesRaw + "###" + positiveRaw + "###"+ rawPercent + "###" + $("#rawType").val()+"###"+season+"@@@";
		}
		if(!isEmpty(samplesLeaf) && !isEmpty(positiveLeaf))
		{
			gmoDatas +=samplesLeaf + "###" + positiveLeaf + "###"+ leafPercent + "###" + $("#leafType").val()+"###"+season+"@@@";
		}
		if(!isEmpty(samplesSeed) && !isEmpty(positiveSeed))
		{
			gmoDatas +=samplesSeed + "###" + positiveSeed + "###"+ seedPercent + "###" + $("#seedType").val()+"###"+season+"@@@";
		}
		
/* 		$.post("gmo_populateSaveGMO.action", {
			gmoDatas : gmoDatas,
		},  success: function(result) {
			jQuery("#gmoDetail").jqGrid('setGridParam',{url:"gmo_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
	    	 resetGMOData();
   	 showPopup(result.msg,result.title);
    }); */
		
		var dataa = {
				gmoDatas : gmoDatas
		}
		
		
		$.ajax({
			 url:'gmo_populateSaveGMO.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
					jQuery("#gmoDetail").jqGrid('setGridParam',{url:"gmo_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			    	 resetGMOData();
		    	 showPopup(result.msg,result.title);
		    	
		    	}
		   
		});
	
	}

}

function populatePercentage() {
	$("#rawPercentage").text('');
	$("#leafPercentage").text('');
	$("#seedPercentage").text('');
	var samplesRaw = $("#noOfSamplesRaw").val();
	var positiveRaw = $("#noOfPositiveRaw").val();
	var samplesLeaf = $("#noOfSamplesLeaf").val();
	var positiveLeaf = $("#noOfPositiveLeaf").val();
	var samplesSeed = $("#noOfSamplesSeed").val();
	var positiveSeed = $("#noOfPositiveSeed").val();
	var percentage=0;
	
	if (!isEmpty(samplesRaw) && !isEmpty(positiveRaw)) {
		 percentage = ((parseFloat(positiveRaw) / parseFloat(samplesRaw)) * 100);
		if(isNaN(percentage))
		{
			percentage=0;
		}
		$("#rawPercentage").text(percentage.toFixed(2));
		$("#rawType").val(3);
	}
	if (!isEmpty(samplesLeaf) && !isEmpty(positiveLeaf)) {
		 percentage = (parseFloat(positiveLeaf) / parseFloat(samplesLeaf)) * 100;
		 if(isNaN(percentage))
			{
				percentage=0;
			}
		$("#leafPercentage").text(percentage.toFixed(2));
		$("#leafType").val(2);
	}
	if (!isEmpty(samplesSeed) && !isEmpty(positiveSeed)) {
		 percentage = (parseFloat(positiveSeed) / parseFloat(samplesSeed)) * 100;
		 if(isNaN(percentage))
			{
				percentage=0;
			}
		$("#seedPercentage").text(percentage.toFixed(2));
		$("#seedType").val(1);
	}
}
function resetGMOData()
{
	$("#rawPercentage").text('');
	$("#leafPercentage").text('');
	$("#seedPercentage").text('');
	$("#noOfSamplesRaw").val('');
	$("#noOfPositiveRaw").val('');
	$("#noOfSamplesLeaf").val('');
	$("#noOfPositiveLeaf").val('');
	$("#noOfSamplesSeed").val('');
	$("#noOfPositiveSeed").val('');
}
</script>
	<div id="dialog" style="display: none"></div>

	<div id="gmo" class="hideAddBtn">
		<s:form action="gmo_populateCreate" name="gmoForm" id="gmoForm">
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
							<s:property value="%{getLocaleProperty('info.gmo')}" />
						</h2>
					</div>
					<div class="error">
						<s:actionerror />
						<s:fielderror />
					</div>

					<div class="row">
					
						<div class="flex-layout filterControls col-md-3">
						<fieldset class="col-md-12">
								<legend style="font-size: 16px; font-style: bold;">
							<s:property
									value="%{getLocaleProperty('season')}"/></legend>
							</fieldset>
							<div class=" flexItem">
						
							<div class="form-element flexdisplay">
								<s:select class="form-control input-sm select2 seasonName " 
									list="seasonList" headerKey="" name="selectedSeason" id="season"
									value="CurrentSeason"
									onchange="farmerProdDetailBarChart(this.value)" />


							</div>
						</div> 
						</div>

						<div class="flex-layout filterControls col-md-3">
						
							
							<fieldset class="col-md-12">
								<legend style="font-size: 16px; font-style: bold;">
									Seed </legend>
							</fieldset>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfSamples')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="gmo.noOfSamples" theme="simple"
										maxlength="35" onkeyup="populatePercentage()"
										onkeypress="return isDecimal(event)" cssClass="form-control "
										id="noOfSamplesSeed" />

								</div>
							</div>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfPositive')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="gmo.noOfPositive" theme="simple"
										maxlength="35" onkeyup="populatePercentage()"
										onkeypress="return isDecimal(event)" cssClass="form-control "
										id="noOfPositiveSeed" />

								</div>
							</div>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('contaminPercentage')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<span id="seedPercentage"></span>

								</div>
							</div>


						</div>


						<div class="flex-layout filterControls col-md-3">
							<fieldset class="col-md-12">
								<legend style="font-size: 16px; font-style: bold;">
									Leaf </legend>
							</fieldset>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfSamples')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="gmo.noOfSamples" theme="simple"
										maxlength="8" onkeyup="populatePercentage()"
										onkeypress="return isDecimal(event)" cssClass="form-control "
										id="noOfSamplesLeaf" />

								</div>
							</div>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfPositive')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="gmo.noOfPositive" theme="simple"
										maxlength="8" onkeyup="populatePercentage()"
										onkeypress="return isDecimal(event)" cssClass="form-control "
										id="noOfPositiveLeaf" />

								</div>
							</div>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('contaminPercentage')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<span id="leafPercentage"></span>

								</div>
							</div>

						</div>

						<div class="flex-layout filterControls col-md-3">
							<fieldset class="col-md-12">
								<legend style="font-size: 16px; font-style: bold;"> Raw
									Cotton </legend>
							</fieldset>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfSamples')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="gmo.noOfSamples" theme="simple"
										maxlength="35" onkeyup="populatePercentage()"
										onkeypress="return isDecimal(event)" cssClass="form-control "
										id="noOfSamplesRaw" />

								</div>
							</div>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfPositive')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="gmo.noOfPositive" theme="simple"
										maxlength="35" onkeyup="populatePercentage()"
										onkeypress="return isDecimal(event)" cssClass="form-control "
										id="noOfPositiveRaw" />

								</div>
							</div>
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('contaminPercentage')}" /><sup
									style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<span id="rawPercentage"></span>

								</div>
							</div>
						</div>





					</div>
					<div class="flex-layout" style="margin-top: 1%">
						<div class="flexItem">
							<button type="button" class="btn btn-sts" id="add"
								onclick="addGMO()">
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
			<table id="gmoDetail"></table>
			<div id="pagerForgmoDetail"></div>
		</div>
	</div>
	<s:hidden id="seedType" name="seedType" />
	<s:hidden id="leafType" name="leafType" />
	<s:hidden id="rawType" name="rawType" />
</body>