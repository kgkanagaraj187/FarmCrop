<%@ include file="/jsp/common/grid-assets.jsp"%>
<link rel="stylesheet" href="plugins/select2/select2.min.css">
<script src="plugins/select2/select2.min.js"></script>
<head>
<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
var loadVariety='';
var loadUnit='';
	var loadCrop='';
var tenant='<s:property value="getCurrentTenantId()"/>';
if(tenant=='chetna')
{
	$("#mspDiv").show();	
}
else
{
	$("#mspDiv").hide();	
}
$(document).ready(function(){
	loadCropGrid();
	loadVarietyGrid();
	loadGradeGrid();
	$(".select2").select2();
	$('.ui-jqgrid .ui-jqgrid-bdiv').css('overflow-x', 'hidden');
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

function loadCropGrid()
{
	var unit = $.ajax({url: 'procurementProductEnroll_populateUnit.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
	loadUnit=unit.replace("{","").replace("}","");
	
	var cropCategory = $.ajax({url: 'procurementProductEnroll_populateCropCategory.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
	loadCropCategory=cropCategory.replace("{","").replace("}","");
	
	//alert(loadUnit);
	jQuery("#detail").jqGrid(
			{
			   	url:'procurementProductEnroll_data.action',
			   	pager: '#pagerForDetail',	
			   	styleUI : 'Bootstrap',
			   	datatype: "json",	
			   	mtype: 'POST',
				editurl:'procurementProductEnroll_update.action',
			   	colNames:[
						<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
						</s:if>
			  		   	  '<s:text name="product.code"/>',
			  		      '<s:text name="product.name"/>',
			  		    '<s:text name="product.unit"/>',
			  		 
			  		  <s:if test="currentTenantId=='kpf'||currentTenantId=='simfed' ">
			  		'<s:text name="product.category"/>',
			  		</s:if>
			  	  <s:if test="currentTenantId=='chetna'">
			  	'<s:text name="mspRate"/>',
			  	'<s:text name="mspPercentage"/>',
			  	</s:if>
			  		    '<s:text name="Actions"/>'
			      	 	 ],
			   	colModel:[	
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
			   		{name:'code',index:'code',width:125,sortable:true},
			   		{name:'cropName',index:'cropName',width:125,sortable:true,editable:true},
			   		{name:'unit',index:'unit',sortable:false, width:40,
						 edittype: "select", editrules: { required: true }},
						  
					<s:if test="currentTenantId=='kpf' ||currentTenantId=='simfed' ">
					{name:'cropCategory',index:'cropCategory',width:125,sortable:true,editable:true},
					</s:if>
					<s:if test="currentTenantId=='chetna'">
					{name:'mspRate',index:'mspRate',width:125,sortable:true,editable:true},
					{name:'mspPercentage',index:'mspPercentage',width:125,sortable:true,editable:true},
					</s:if>
			   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                        formatoptions: {
                            keys: true, 
                            
                            delOptions: { 
                            	url: 'procurementProductEnroll_populateDelete.action',
                            	afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#detail").closest('div.ui-jqgrid')
                            	    });
                            	},
                            	
                              afterSubmit: function(data) 
                                {
                                	var json = JSON.parse(data.responseText);
									//document.getElementById("validateErrorCate").innerHTML=json.msg;
									jQuery("#detail").jqGrid('setGridParam',{url:"procurementProductEnroll_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery.post("procurementVariety_populateCrop.action",function(result){
										insertOptions("selectedCrop",JSON.parse(result));
										insertOptions("procurementProductId",JSON.parse(result));
										jQuery('.ui-jqdialog-titlebar-close').click();
									});
                                }
                            },
                            afterSave: function (id, response, options) {
                                var json = JSON.parse(response.responseText);
								jQuery("#detail").jqGrid('setGridParam',{url:"procurementProductEnroll_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								
								showPopup(json.msg,json.title);
								jQuery.post("procurementVariety_populateCrop.action",function(result){
									insertOptions("selectedCrop",JSON.parse(result));
									insertOptions("procurementProductId",JSON.parse(result));
								jQuery('.ui-jqdialog-titlebar-close').click();
								});
                            }
                        }
			    	 }
			   			 ],
			   	height: 301, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			  
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
		        },
 			loadComplete: function() {
 					 $('#detail').setColProp('unit', { editoptions: { value: loadUnit} });
 					 $('#detail').setColProp('cropCategory', { editoptions: { value: loadCropCategory} });
		       		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		       		$(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		       		$(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		       		$(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		       		jQuery("#vareityDetail").jqGrid('setGridParam',{url:"procurementVariety_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		       		jQuery("#gradeDetail").jqGrid('setGridParam',{url:"procurementGrade_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
						//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
		       		
		        }
			});
			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			
			jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
		/* 	 $('#detail').jqGrid('setGridHeight',(windowHeight));  */
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
		   
}


function loadVarietyGrid(){
	var crop = $.ajax({url: 'procurementVariety_populateProcurementProduct.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Crops.');}}).responseText;
	loadCrop=crop.replace("{","").replace("}","");
	
		jQuery("#vareityDetail").jqGrid(
		{
		   	url:'procurementVariety_data.action',
		   	pager: '#pagerForDetailVariety',	  
		   	datatype: "json",
		   	styleUI : 'Bootstrap',
		   	editurl:'procurementVariety_update.action',
		   	mtype: 'POST',
		   	colNames:[
		  		   	  '<s:text name="procurementVariety.code"/>',
		  		    '<s:text name="crop"/>',
		  		      '<s:text name="procurementVariety.name"/>',
		  		    <s:if test="currentTenantId!='olivado'">	
		  		    '<s:text name="procurementVariety.noDaysToGrow"/>',
		  		  '<s:text name="%{getLocaleProperty('procurementVariety.yield')}"/>',
		  		 '<s:text name="%{getLocaleProperty('procurementVariety.harvDays')}"/>',
		  		 </s:if>
		  		    '<s:text name="Actions"/>'
		      	 	 ],
		   	colModel:[			
		   		{name:'code',index:'code',width:60,sortable:true},
		   		{name:'procurementProductId',index:'procurementProductId',sortable:false, width:100,
					 edittype: "select", editrules: { required: true } 	
				
				
				},
		   		{name:'varietyName',index:'varietyName',width:125,sortable:true,editable:true},
		   		 <s:if test="currentTenantId!='olivado'">	
		   		{name:'noDaysToGrow',index:'noDaysToGrow',width:125,sortable:true,editable:true,align:"right"},
		   		{name:'varietyYield',index:'varietyYield',width:125,sortable:true,editable:true,align:"right"},
		   		{name:'harvestDays',index:'harvestDays',width:100,sortable:true,editable:true,align:"right"},
		   	 </s:if>
		   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                        formatoptions: {
                            keys: true, 
                          
                            delOptions: { 
                            	url: 'procurementVariety_populateDelete.action',
                            	afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#vareityDetail").closest('div.ui-jqgrid')
                            	    });
                            	},
                            	
                            	
                              afterSubmit: function(data) 
                                {
                                	var json = JSON.parse(data.responseText);
									//document.getElementById("validateErrorCate").innerHTML=json.msg;
									jQuery("#vareityDetail").jqGrid('setGridParam',{url:"procurementVariety_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery.post("procurementGrade_populateVariety.action",function(result){
										insertOptions("selectedVariety",JSON.parse(result));
										insertOptions("procurementVarietyId",JSON.parse(result));
										
									});
									jQuery('.ui-jqdialog-titlebar-close').click();
                                }
                            },
                            
                     
                            
                            afterSave: function (id, response, options) {
                                var json = JSON.parse(response.responseText);
								jQuery("#vareityDetail").jqGrid('setGridParam',{url:"procurementVariety_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery.post("procurementGrade_populateVariety.action",function(result){
									insertOptions("selectedVariety",JSON.parse(result));
									insertOptions("procurementVarietyId",JSON.parse(result));
									jQuery('.ui-jqdialog-titlebar-close').click();
								});
                            }
                            
                        }
			    	 }
		   			 ],
		   	height: 301, 
		    width: $("#baseDiv").width(), // assign parent div width
		    scrollOffset: 0,
		   	rowNum:10,
		   	rowList : [10,25,50],
		    sortname:'id',			  
		    sortorder: "desc",
		    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
		   /*  onSelectRow: function(id){ 
			  document.detailform.id.value  =id;
		      document.detailform.submit();      
			},		 */
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
	        
	        loadComplete: function() {
	      		$('#vareityDetail').setColProp('procurementProductId', { editoptions: { value: loadCrop} });
	       		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
	       		$(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
	       		$(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
	       		$(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
	       		jQuery("#gradeDetail").jqGrid('setGridParam',{url:"procurementGrade_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
					//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
	       		
	        }
		});
		
		jQuery("#vareityDetail").jqGrid('navGrid','#pagerForDetailVariety',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
		jQuery("#vareityDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

		colModel = jQuery("#vareityDetail").jqGrid('getGridParam', 'colModel');
	    $('#gbox_' + $.jgrid.jqID(jQuery("#vareityDetail")[0].id) +
	        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
	        var cmi = colModel[i], colName = cmi.name;

	        if (cmi.sortable !== false) {
	            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
	        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
	            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
	        }
	    });
	
}

function loadGradeGrid(){
	
	var variety = $.ajax({url: 'procurementGrade_populateProcurementVariety.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
	 loadVariety=variety.replace("{","").replace("}","");
	
	var unit = $.ajax({url: 'procurementGrade_populateUnit.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
	 loadUnit=unit.replace("{","").replace("}","");
	
	var crop = $.ajax({url: 'procurementVariety_populateProcurementProduct.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
	 loadCrop=crop.replace("{","").replace("}","");
	
		jQuery("#gradeDetail").jqGrid(
		{
		   	url:'procurementGrade_data.action',
		   	pager: '#pagerForDetailGrade',	  
		   	datatype: "json",	
		   	styleUI : 'Bootstrap',
		   	editurl:'procurementGrade_update.action',
		   	mtype: 'POST',
		   	colNames:[
		  		   	  '<s:text name="procurementGrade.code"/>',
		  		   	'<s:text name="crop"/>',
		  		    '<s:text name="%{getLocaleProperty('variety')}"/>',
		  		      '<s:text name="procurementGrade.name"/>',
		  		   // '<s:text name="unit"/>',
		  		    
		  		      '<s:text name="procurementGrade.price"/>',
		  		    '<s:text name="Actions"/>'
		      	 	 ],
		   	colModel:[			
		   		{name:'code',index:'code',width:125,sortable:true},
		   		
		   		
		   		
		   		{name:'procurementProductId',index:'procurementProductId',sortable:false, width:40,editable: true,
					 edittype: "select", editrules:{required: true},
					 
					 editoptions: {
	                		dataEvents: [ 
	   			   			          {
	   			   			            type: "change",
	   			   			            fn: function () {
	   			   			            	var id=$(this).closest('tr').attr('id')+"_procurementVarietyId";
	   			   			         		populateVarietyEdit($(this).val(),id);
	   			   			            }
	   			   			        }]
	                        }	
				
	                
				},
				
				
				{name:'procurementVarietyId',index:'procurementVarietyId',sortable:false, width:40,editable: true,
					 edittype: "select", editrules:{required: true},
	                 
				
				},
				
		   		/* {name:'procurementVarietyId',index:'procurementVarietyId',sortable:false, width:40,editable: true,
					 edittype: "select", editrules: { required: true } 	
				
				
				}, */
		   		{name:'gradeName',index:'gradeName',width:125,sortable:true,editable: true},
		   		/*{name:'selectedUnit',index:'selectedUnit',sortable:false, width:40,editable: true,
					 edittype: "select", editrules: { required: true } 	
				
				
				},*/
		   		{name:'gradePrice',index:'gradePrice',width:125,sortable:true,align:'right',editable: true,},
		   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
		   		
                    formatoptions: {
                        keys: true, 
                        onEdit : function(val){
                        	var dataa = {
                        			procurementProductId:jQuery('#' + val + '_procurementProductId').val(),
			   				}
                        	getAjaxValue("procurementGrade_populateVariety",dataa,val,"_procurementVarietyId"); 
                        	
                        }, 
                    
                        delOptions: { 
                        	url: 'procurementGrade_populateDelete.action',
                        	afterShowForm:function ($form) {
                        	    $form.closest('div.ui-jqdialog').position({
                        	        my: "center",
                        	        of: $("#gradeDetail").closest('div.ui-jqgrid')
                        	    });
                        	},
                        	
                          afterSubmit: function(data) 
                            {
                            	var json = JSON.parse(data.responseText);
								//document.getElementById("validateErrorCate").innerHTML=json.msg;
								jQuery("#gradeDetail").jqGrid('setGridParam',{url:"procurementGrade_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
                            }
		   		
                            
                            
                        },
                        
                 
                        
                        afterSave: function (id, response, options) {
                            var json = JSON.parse(response.responseText);
							jQuery("#gradeDetail").jqGrid('setGridParam',{url:"procurementGrade_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
							showPopup(json.msg,json.title);
							jQuery('.ui-jqdialog-titlebar-close').click();
                        }
                        
                        
                           
                       
                    }
		    	 }
		   			 ],
		   	height: 301, 
		    width: jQuery("#baseDiv").width(), // assign parent div width
		    scrollOffset: 0,
		   	rowNum:10,
		   	rowList : [10,25,50],
		    sortname:'id',			  
		    sortorder: "desc",
		    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
		   /*  onSelectRow: function(id){ 
			  document.detailform.id.value  =id;
		      document.detailform.submit();      
			},		 */
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
	        
	        loadComplete: function() {
	        	$('#gradeDetail').setColProp('procurementProductId', { editoptions: { value: loadCrop} });
	      		$('#gradeDetail').setColProp('procurementVarietyId', { editoptions: { value: loadVariety} });
	      		$('#gradeDetail').setColProp('selectedUnit', { editoptions: { value: loadUnit} });
	       		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
	       		$(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
	       		$(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
	       		$(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
					//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
	       		
	        }
		});
		
		jQuery("#gradeDetail").jqGrid('navGrid','#pagerForDetailGrade',{edit:false,add:false,del:false,search:false,refresh:true}); // enabled refresh for reloading grid
		jQuery("#gradeDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

		colModel = jQuery("#gradeDetail").jqGrid('getGridParam', 'colModel');
	    $('#gbox_' + $.jgrid.jqID(jQuery("#gradeDetail")[0].id) +
	        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
	        var cmi = colModel[i], colName = cmi.name;

	        if (cmi.sortable !== false) {
	            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
	        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
	            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
	        }
	    });
	
}

function resetData()
{
	// $('#selectedCrop').prop('selectedIndex',-1);
	// $('#selectedCrop').val('');
	$("#cropName").val('');
	$("#unit").val('-1');
	$("#cropCat").val('');
	$("#varietyName").val('');
	$("#crop").val();
	$("#noDaysToGrow").val('');
	$("#varietyYield").val('');
	$("#harvestDays").val('');
	document.getElementById("validateErrorCrop").innerHTML='';
	document.getElementById("validateErrorVariety").innerHTML='';
	
}

function addCropCategory()     
{
	var crop=$("#cropName").val();
	var unit=$("#unit").val();
	//var cropCategory =$('input:radio[name="cropCategory"]:checked').val();
	var cropCategory=$("#cropCat").val();
	
	if(crop=="" || crop==null)
	{
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.name"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	else if(unit=="-1" || unit==null)
	{
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.unit"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	
	else if(cropCategory=="-1" || cropCategory==null)
	{
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.category"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	else
	{
		$.post("procurementProductEnroll_create",{cropName:crop,unit:unit,cropCategory:cropCategory},function(result){
			resetData("cropForm");
			jQuery("#detail").jqGrid('setGridParam',{url:"procurementProductEnroll_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			var validateMsg=JSON.parse(result);
			showPopup(validateMsg.msg,validateMsg.title);
			jQuery.post("procurementVariety_populateCrop.action",function(result){
				insertOptions("selectedCrop",JSON.parse(result));
				insertOptions("procurementProductId",JSON.parse(result));
			});
			
			
		});
		document.getElementById("validateErrorCrop").innerHTML='';
	}
}


function addCrop()     
{
	var crop=$("#cropName").val();
	var unit=$("#unit").val();
	var mspRate=$("#mspRate").val();
	var mspPercentage=$("#mspPercentage").val();
	if(crop=="" || crop==null)
	{
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.name"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	else if(unit=="-1" || unit==null)
	{
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.unit"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	
	else if(tenant=="chetna" && mspRate=="" )
	{
		
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.mspRate"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	
	else if(tenant=='chetna' && mspPercentage=="" )
	{
		document.getElementById("validateErrorCrop").innerHTML='<s:text name="empty.mspPerctage"/>';
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	
	else
	{
		$.post("procurementProductEnroll_create",{cropName:crop,unit:unit,mspRate:mspRate,mspPercentage:mspPercentage},function(result){

			resetData("cropForm");
			jQuery("#detail").jqGrid('setGridParam',{url:"procurementProductEnroll_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			showPopup(result.msg,result.title);
			 /* var validateMsg=JSON.parse(result);
			showPopup(validateMsg.msg,validateMsg.title); */
			
			
			jQuery.post("procurementVariety_populateCrop.action",function(result){
				insertOptions("selectedCrop",JSON.parse(result));
				insertOptions("procurementProductId",JSON.parse(result));
			});
			
			
		});
		document.getElementById("validateErrorCrop").innerHTML='';
	}
	
	
}


function addVariety()
{
	var variety=$("#varietyName").val();
	var selectedCrop=$("#selectedCrop").val();
	var noDaysToGrow=$("#noDaysToGrow").val();
	var varietyYield=$("#varietyYield").val();
	var harvestDays=$("#harvestDays").val();
	if(!selectedCrop.trim()|| selectedCrop==null)
	{
		document.getElementById("validateErrorVariety").innerHTML='<s:text name="empty.crop"/>';
	}
	else if(variety=="" || variety==null)
	{
		document.getElementById("validateErrorVariety").innerHTML='<s:text name="empty.name"/>';
	}
	
	else if(tenant=='kpf'||tenant=='wub' && varietyYield=="" )
	{
		document.getElementById("validateErrorVariety").innerHTML='<s:text name="empty.yield"/>';
		
	}
	else if(tenant=='kpf'||tenant=='wub'  && noDaysToGrow=="" )
	{
		document.getElementById("validateErrorVariety").innerHTML='<s:text name="empty.noDaysToGrow"/>';
	}
	else if((tenant!='pratibha') && (tenant!='olivado') && (tenant!='welspun') && (harvestDays=="" || harvestDays==null) && (tenant!='livelihood'))
	{
		document.getElementById("validateErrorVariety").innerHTML='<s:text name="empty.harvestDays"/>';
	}
	else
	{
		$.post("procurementVariety_create",{procurementProductId:selectedCrop,varietyName:variety,noDaysToGrow:noDaysToGrow,varietyYield:varietyYield,harvestDays:harvestDays},function(result){
			resetData("varietyForm");
			jQuery("#vareityDetail").jqGrid('setGridParam',{url:"procurementVariety_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			//var validateMsg=JSON.parse(result);
			
			
		
			var variety = $.ajax({url: 'procurementGrade_populateProcurementVariety.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
			 loadVariety=variety.replace("{","").replace("}","");
			
			var crop = $.ajax({url: 'procurementVariety_populateProcurementProduct.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
			 loadCrop=crop.replace("{","").replace("}","");
			
			/* showPopup(validateMsg.msg,validateMsg.title); */
			 showPopup(result.msg,result.title);
			
	});
		document.getElementById("validateErrorVariety").innerHTML='';
	}
	
}



function addGrade()
{
	
	var selectedCrop=$("#procurementProductId").val();
	var selectedVariety=$("#selectedVariety").val();
	var gradeName=$("#gradeName").val();
	var unit=$("#unit").val();
	var gradePrice=$("#gradePrice").val();
	//$('#procurementProductId').val(null).trigger('change');
	//$('#selectedVariety').val(null).trigger('change');
	//$('#gradeName').val(null);
	$('#unit').val(null);
	$('#gradePrice').val(null);
	
	if(selectedCrop=="" || selectedCrop==null)
	{
		
		document.getElementById("validateErrorGrade").innerHTML='<s:text name="empty.crop"/>';
	}
	
	 else if(selectedVariety=="" || selectedVariety==null || isEmpty(selectedVariety))
	{
		 document.getElementById("validateErrorGrade").innerHTML='<s:property value="%{getLocaleProperty('empty.variety')}" />';
	}
	
	else if(gradeName=="" || gradeName==null)
	{
		document.getElementById("validateErrorGrade").innerHTML='<s:text name="empty.name"/>';
	}
	/*else if(unit=="" || unit==null)
	{
		document.getElementById("validateErrorGrade").innerHTML='<s:text name="empty.unit"/>';
	}*/
	else if(gradePrice=="" || gradePrice==null)
	{
		document.getElementById("validateErrorGrade").innerHTML='<s:text name="empty.price"/>';
	}
	
	else
	{
		$.post("procurementGrade_create",{gradeName:gradeName,procurementVarietyId:selectedVariety,selectedUnit:unit,gradePrice:gradePrice},function(result){
			resetData("gradeForm");
			jQuery("#gradeDetail").jqGrid('setGridParam',{url:"procurementGrade_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			showPopup(result.msg,result.title);
			var validateMsg=JSON.parse(result);
			alert("validateMsg:"+validateMsg);
			
			//showPopup(validateMsg.msg,validateMsg.title);
			//resetData("gradeForm");
			
		});
		document.getElementById("validateErrorGrade").innerHTML='';
		loadCropGrid();
		loadVarietyGrid();
		loadGradeGrid();
	}
	
}

function populateVariety(obj)
{
	jQuery.post("procurementGrade_populateVariety.action",{id:obj.value,dt:new Date(),procurementProductId:obj.value},function(result){
		insertOptions("selectedVariety",JSON.parse(result));
	});
	
}

function populateVarietyEdit(val,id)
{
	jQuery.post("procurementGrade_populateVariety.action",{id:val,dt:new Date(),procurementProductId:val},function(result){
		insertOptions(id,JSON.parse(result));
		//listGramPanchayat(document.getElementById("vcities"));
	});
	
	


}
function resetData(id){
	$("#"+id)[0].reset();
	$("#"+id).trigger("reset");
	$('.select2').trigger("change");
	 $('.select2').select2();
}

		 
function getAjaxValue(url,dataa,selRow,idSuffix){
	var resp = $.ajax({
		url: url,
		data:dataa,
		type: 'post',
		async: false, 
		success: function(data, result) {
			if (!result) 
				alert('Failure to load data');
			}
	}).responseText;
	
		var id="#"+selRow+idSuffix;
		var selectedValue = jQuery(id).val();
		insertJQOptions(selRow+idSuffix,JSON.parse(resp));
		jQuery(id).val(selectedValue);
		
		
}		 

function insertJQOptions(ctrlName, jsonArr) {
	document.getElementById(ctrlName).length = 0;
	/* addOption(document.getElementById(ctrlName), "Select", ""); */
	for (var i = 0; i < jsonArr.length; i++) {
		addOption(document.getElementById(ctrlName), jsonArr[i].name,
				jsonArr[i].id);
	}
	
}

$("#variety").click(function() {
	  alert( "Handler for .click() called." );
	});
		 
</script>

<div id="baseDiv" style="width: 99%"></div>
<div class="appContentWrapper marginBottom">
	<ul class="nav nav-pills">
		<li class="active"><a data-toggle="pill" href="#crop"><s:property
					value="%{getLocaleProperty('crop')}" /></a></li>

		<li><a data-toggle="pill" href="#variety"><s:property
					value="%{getLocaleProperty('variety')}" /></a></li>

		<li><a data-toggle="pill" href="#grade"><s:property
					value="%{getLocaleProperty('grade')}" /></a></li>

	</ul>
</div>




<div class="tab-content">
	<div id="crop" class="tab-pane fade in active">
		<s:form name="cropForm" id="cropForm">
			<sec:authorize ifAllGranted="profile.procurementProduct.create">
				<a data-toggle="collapse" data-parent="#cropAccordian"
					href="#cropAccordian" class="btn btn-sts margin-bottom10px"> <s:text
						name="create.button" />
				</a>
			</sec:authorize>
			<div id="cropAccordian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorCrop" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text name="product.name" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="cropName" theme="simple" maxlength="35"
									id="cropName" />

							</div>
						</div>

						<div class="flexItem">
							<label for="txt"><s:text name="product.unit" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="unit" name="procurementProduct.types.code"
									list="listUom" headerKey="-1" listKey="code" listValue="name"
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" />

							</div>
						</div>

											
                         <s:if test="currentTenantId =='chetna'">
						<div class="flexItem mspDiv ">
							<label for="txt"><s:text name="mspRate" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="procurementProduct.mspRate" id="mspRate" theme="simple"
									maxlength="35" onkeypress="return isDecimal(event)" />
							</div>
						</div>
						<div class="flexItem mspDiv ">
							<label for="txt"><s:text name="mspPercentage" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="procurementProduct.mspPercentage" id="mspPercentage"
									theme="simple" maxlength="35"
									onkeypress="return isDecimal(event)" />
							</div>
						</div>
						</s:if>
							<div class="flexItem" style="margin-top: 20px;">
								<button type="button" class="btn btn-sts" data-toggle="modal"
									onclick="addCrop()">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
							</div>
						

					</div>
				</div>
			</div>
		</s:form>
		<div style="width: 100%;" id="cropBaseDiv">
			<div class="appContentWrapper ">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
			</div>
		</div>
	</div>


	<div id="variety" class="tab-pane fade">
		<s:form name="varietyForm" id="varietyForm">
			<sec:authorize ifAllGranted="profile.procurementProduct.create">
				<a data-toggle="collapse" data-parent="#varietyAccordian"
					href="#varietyAccordian" class="btn btn-sts margin-bottom10px">
					<s:text name="create.button" />
				</a>
			</sec:authorize>
			<div id="varietyAccordian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorVariety" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text name="crop" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedCrop" theme="simple" listKey="id"
									listValue="name" list="procurementProductList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" id="selectedCrop" />
							</div>
						</div>


						<div class="flexItem">
							<label for="txt"><s:property
									value="%{getLocaleProperty('procurementVariety.name')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="varietyName" theme="simple" maxlength="45"
									id="varietyName" />

							</div>
						</div>
						<s:if test="currentTenantId!='olivado'">	
						<div class="flexItem">
							<label for="txt"><s:text
									name="procurementVariety.noDaysToGrow" /> <s:if
									test="currentTenantId=='kpf'||currentTenantId=='wub'">
									<sup style="color: red;">*</sup>
								</s:if></label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="noDaysToGrow" id="noDaysToGrow" theme="simple"
									onkeypress="return isNumber(event)" maxlength="12" />
							</div>
						</div>

						<div class="flexItem">
							<label for="txt"><s:property
									value="%{getLocaleProperty('procurementVariety.yield')}" /> <s:if
									test="currentTenantId=='kpf'||currentTenantId=='wub'">
									<sup style="color: red;">*</sup>
								</s:if></label>
							<div class="form-element">
								<s:textfield id="varietyYield" name="varietyYield"
									maxlength="12" onkeypress="return isDecimal(event)" />
							</div>
						</div>

						<div class="flexItem">
							<label for="txt"><s:text
									name="procurementVariety.harvDays" /> 
								<s:if test="currentTenantId!='welspun'&&currentTenantId!='livelihood'">		
									<sup
								style="color: red;">*</sup></s:if>
								</label>
							<div class="form-element flexdisplay">
								<s:textfield cssClass="form-control input-sm" name="harvestDays"
									id="harvestDays" theme="simple"
									onkeypress="return isNumber(event)" maxlength="12" />

							</div>
						</div>
				</s:if>		
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts " data-toggle="modal"
								onclick="addVariety()">
								<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
								</font>
							</button>

						</div>
					</div>
				</div>
			</div>
		</s:form>
		<div style="width: 100%;" id="varietyBaseDiv">
			<div class="appContentWrapper ">
				<table id="vareityDetail"></table>
				<div id="pagerForDetailVariety"></div>
			</div>
		</div>
	</div>


	<div id="grade" class="tab-pane fade">
		<s:form name="gradeForm" id="gradeForm">
			<sec:authorize ifAllGranted="profile.procurementProduct.create">
				<a data-toggle="collapse" data-parent="#gradeAccordian"
					href="#gradeAccordian" class="btn btn-sts margin-bottom10px"> <s:text
						name="create.button" />
				</a>
			</sec:authorize>
			<div id="gradeAccordian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorGrade" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text name="crop" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="procurementProductId" theme="simple"
									listKey="id" listValue="name" list="procurementProductList"
									headerKey="" headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" id="procurementProductId"
									onchange="populateVariety(this)" />
							</div>
						</div>
						<div class="flexItem">
							<label for="txt"><s:property
								value="%{getLocaleProperty('variety')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedVariety" id="selectedVariety"
									theme="simple" listKey="id" listValue="name"
									list="procurementVarietyList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" />
							</div>
						</div>

						<div class="flexItem">
							<label for="txt"><s:text name="procurementGrade.name" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="gradeName" theme="simple" maxlength="45"
									id="gradeName" />
							</div>
						</div>
						<%-- <div class="flexItem">
							<label for="txt"><s:text name="procurementGrade.unit" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="unit" name="selectedUnit" list="listUom"
									headerKey="" listKey="code" listValue="name"
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />

							</div>
						</div>--%>
						<div class="flexItem">
							<label for="txt"><s:text name="procurementGrade.price" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element flexdisplay">
								<s:textfield cssClass="form-control input-sm" name="gradePrice"
									id="gradePrice" theme="simple" maxlength="8"
									onkeypress="return isDecimal(event)" />

							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts margin-bottom10px"
								data-toggle="modal" onclick="addGrade()">
								<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
								</font>
							</button>
						</div>
					</div>
				</div>
			</div>
		</s:form>

		<div style="width: 100%;" id="gradeBaseDiv">
			<div class="appContentWrapper ">
				<table id="gradeDetail"></table>
				<div id="pagerForDetailGrade"></div>
			</div>
		</div>
	</div>


</div>



<s:form name="createform" action="procurementProductEnroll_create">
</s:form>
<s:form name="updateform" action="procurementProductEnroll_detail">
	<s:hidden key="id" />
	<s:hidden name="ppid" value="%{id}" />
	<s:hidden key="currentPage" />
</s:form>