<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>

	<script type="text/javascript">
	var loadCountries;
	var loadstate;
	var loadDistricts;
	var loadCities;
	var gramPanchayatEnable = "<s:property value='gramPanchayatEnable'/>";
	//var loadPanchayat;
	
	$(document).ready(function(){	
		loadLocationValues();
		loadVillage();	
		loadCity();
		loadDistrict();
		loadStates();
		loadCountry();
		if(gramPanchayatEnable=='1'){
			loadGramPanchayat();
		}
		
		 $('select').select2();
	});

	 function resetLocationValues(){
		 var c = $.ajax({
				url: 'village_populateCountryList.action',
				async: false, 
				type: 'post',
				success: function(data, result) {
					if (!result) 
						alert('Failure to retrieve');
					}
			}).responseText;
			loadCountries=c.replace("{","").replace("}","");
			
			var state = $.ajax({
							url: 'village_populateStateList.action',
							async: false, 
							type: 'post',
							success: function(data, result) {
								if (!result) 
									alert('Failure to retrieve.');
								}
						}).responseText;
			loadstate=state.replace("{","").replace("}","");
			
			var district = $.ajax({
				url: 'village_populateLocalityList.action',
				async: false, 
				type: 'post',
				success: function(data, result) {
					if (!result) 
						alert('Failure to retrieve.');
					}
			}).responseText;
			loadDistricts=district.replace("{","").replace("}","");
			
			var city = $.ajax({
				url: 'village_populateCityList.action',
				async: false, 
				type: 'post',
				success: function(data, result) {
					if (!result) 
						alert('Failure to retrieve.');
					}
			}).responseText;
			loadCities=city.replace("{","").replace("}","");
			
			var grampanchayat = $.ajax({
				url: 'village_populateGrampanchayatList.action',
				async: false, 
				type: 'post',
				success: function(data, result) {
					if (!result) 
						alert('Failure to retrieve.');
					}
			}).responseText;
			loadPanchayat=city.replace("{","").replace("}","");
	} 

function loadLocationValues(){

	var c = $.ajax({
		url: 'village_populateCountryList.action',
		async: false, 
		type: 'post',
		success: function(data, result) {
			if (!result) 
				alert('Failure to retrieve');
			}
	}).responseText;
	loadCountries=c.replace("{","").replace("}","");
	
	var state = $.ajax({
					url: 'village_populateStateList.action',
					async: false, 
					type: 'post',
					success: function(data, result) {
						if (!result) 
							alert('Failure to retrieve.');
						}
				}).responseText;
	loadstate=state.replace("{","").replace("}","");
	
	var district = $.ajax({
		url: 'village_populateLocalityList.action',
		async: false, 
		type: 'post',
		success: function(data, result) {
			if (!result) 
				alert('Failure to retrieve.');
			}
	}).responseText;
	loadDistricts=district.replace("{","").replace("}","");
	
	var city = $.ajax({
		url: 'village_populateCityList.action',
		async: false, 
		type: 'post',
		success: function(data, result) {
			if (!result) 
				alert('Failure to retrieve.');
			}
	}).responseText;
	loadCities=city.replace("{","").replace("}","");
	
	var grampanchayat = $.ajax({
		url: 'village_populateGrampanchayatList.action',
		async: false, 
		type: 'post',
		success: function(data, result) {
			if (!result) 
				alert('Failure to retrieve.');
			}
	}).responseText;
	loadPanchayat=city.replace("{","").replace("}","");
}

function loadVillage(){
	jQuery("#detail").jqGrid({
			   	url:'village_data.action',
			   	editurl:'village_populateVillageUpdate.action',
			   	pager: '#pagerForDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
						    <s:if test='branchId==null'>
							'<s:text name="app.branch"/>',
						   </s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							'<s:text name="app.subBranch"/>',
						</s:if>
							<s:if test="currentTenantId!='livelihood'">
			  		   	  '<s:text name="village.code"/>',
			  		   	  </s:if>
			  		   	'<s:property value="%{getLocaleProperty('village.name')}"/>',
			  		    <s:if test='gramPanchayatEnable==1'>
			  		  
				  		  '<s:property value="%{getLocaleProperty('gramPanchayat.name')}"/>',
				  		  </s:if>
				  		 '<s:property value="%{getLocaleProperty('city.name')}" />',
				  		  '<s:property value="%{getLocaleProperty('locality.name')}"/>',
				  		 '<s:property value="%{getLocaleProperty('state.name')}"/>',
				  		'<s:property value="%{getLocaleProperty('country.name')}"/>',
				  		'<s:text name="Actions"/>'
						 ],
			   	colModel:[
						<s:if test='branchId==null'>
							{name:'branchId',index:'branchId',width:100,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
						</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>  
						<s:if test="currentTenantId=='olivado'">
			   				{name:'code',index:'code',width:50,sortable:true,editable: true},
			   			</s:if>
			   			<s:elseif test="currentTenantId!='livelihood'">
			   				{name:'code',index:'code',width:50,sortable:true},
				   		</s:elseif>
			   			{name:'name',index:'name',width:90,sortable:true,editable: true},	   		
			   			 <s:if test='gramPanchayatEnable==1'>
			   				//{name:'gp.name',index:'gp.name',width:90,sortable:true,editable: true},
			   				{name:'selectedGramPanchayat',index:'selectedGramPanchayat',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true}},
			   			</s:if>
			   			{name:'selectedCity',index:'selectedCity',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true},
			   				
			   			 editoptions: {
		                		dataEvents: [ 
		   			   			          {
		   			   			            type: "change",
		   			   			            fn: function () {
		   			   			            	//alert($(this).val());
		   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedGramPanchayat";
		   			   			         		 listGramPanchayatz($(this).val(),id);
		   			   			            }
		   			   			        }]
		                        }
			   				
			   				
			   			},
			   			{name:'selectedDistrict',index:'selectedDistrict',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true},
			                  editoptions: {
			                		dataEvents: [ 
			   			   			          {
			   			   			            type: "change",
			   			   			            fn: function () {
			   			   			            	//alert($(this).val());
			   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedCity";
			   			   			        		
			   			   			          		listMunicipalityz($(this).val(),id);
			   			   			          var gid=$(this).closest('tr').attr('id')+"_selectedGramPanchayat";
			   			   			         		 listGramPanchayatz("",gid);
			   			   			            }
			   			   			        }]
			                        }
					   		},
			   			 {name:'selectedState',index:'selectedState',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true},
		                  editoptions: {
		                		dataEvents: [ 
		   			   			          {
		   			   			            type: "change",
		   			   			            fn: function () {
		   			   			            	//alert($(this).val());
		   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedDistrict";
		   			   			          		var cid=$(this).closest('tr').attr('id')+"_selectedCity";
		   			   			          		listMunicipalityz("",cid);
		   			   			          		listLocalitz($(this).val(),id);
		   			   			          var gid=$(this).closest('tr').attr('id')+"_selectedGramPanchayat";
	   			   			         		 listGramPanchayatz("",gid);
		   			   			            }
		   			   			        }]
		                        }
				   		}, 
				   		{name:'selectedCountry',index:'selectedCountry',width:50,sortable:true,editable: true,edittype: "select", editrules:{required: true},
				   			editoptions: {
		                		dataEvents: [ 
		   			   			          {
		   			   			            type: "change",
		   			   			            fn: function () {
		   			   			            	//alert($(this).val());
		   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedState";
		   			   			          		var lid=$(this).closest('tr').attr('id')+"_selectedDistrict";
		   			   			         		 var cid=$(this).closest('tr').attr('id')+"_selectedCity";
	   			   			          		
		   			   			          		listState($(this).val(),id);
		   			   			          		listLocalitz("",lid);
		   			   			        	  listMunicipalityz("",cid);
		   			   			        var gid=$(this).closest('tr').attr('id')+"_selectedGramPanchayat";
  			   			         		 listGramPanchayatz("",gid);
		   			   			            }
		   			   			        }]
		                        }
				   		},
			   			{name:'act',index:'act',width:60,sortable:false,formatter: "actions",formatoptions: {keys: true,
			   			onEdit : function(val){
			   				$('#detail').setColProp('selectedCountry', { editoptions: { value: loadCountries} });
			   				var dataa = {
			   						selectedCountry:jQuery('#' + val + '_selectedCountry').val(),
			   				}
			   				getAjaxValue("village_populateState.action",dataa,val,"_selectedState");
			   				
			   				
			   				/***/
			   				dataa = {
			   						selectedState:jQuery('#' + val + '_selectedState').val(),
			   				}
			   				getAjaxValue("village_populateLocality.action",dataa,val,"_selectedDistrict");
			   				
			   				dataa = {
			   						selectedDistrict:jQuery('#' + val + '_selectedDistrict').val(),
				   			}
			   				getAjaxValue("village_populateCity.action",dataa,val,"_selectedCity");
			   				
			   				dataa = {
			   						selectedCity:jQuery('#' + val + '_selectedCity').val(),
				   			}
			   				getAjaxValue("village_populateGramPanchayat.action",dataa,val,"_selectedGramPanchayat");
			   				
			   			},
			   			delOptions: { url: 'village_delete.action' ,
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
								jQuery("#detail").jqGrid('setGridParam',{url:"village_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
                            }
		   		
				   		} ,
				   		
				   		
				   		afterSave: function (id, response, options) {
                            var json = JSON.parse(response.responseText);
								jQuery("#detail").jqGrid('setGridParam',{url:"village_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
                        }
			   		}}	
				],
			   	height: 301, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 20,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			  
			    sortorder: "desc",
			    /* caption:'<s:text name="list"/>', */
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			 	onSelectRow: function(id){ 
				 	var myGrid = jQuery('#detail'),
			        selRowId = myGrid.jqGrid ('getGridParam', 'selrow'),
			        celValue = myGrid.jqGrid ('getCell', selRowId, 'id');
				},	
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        },
		        loadComplete: function() {
		        	//alert('qq1'+loadstate);
		        	$('#detail').setColProp('selectedCountry', { editoptions: { value: loadCountries} });
		      		$('#detail').setColProp('selectedState', { editoptions: { value: loadstate} });
		      		$('#detail').setColProp('selectedDistrict', { editoptions: { value: loadDistricts} });
		      		$('#detail').setColProp('selectedCity', { editoptions: { value: loadCities} });
		      		$('#detail').setColProp('selectedGramPanchayat', { editoptions: { value: loadPanchayat} });
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });
	
			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:true,save:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#detail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","gp.name","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","c.name","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","s.name","",{"text-align":"center"});
			
			
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
		    windowHeight=windowHeight-80;
		    $('#detail').jqGrid('setGridHeight',(windowHeight));
}


function loadGramPanchayat(){
	jQuery("#panchayatDetail").jqGrid(
			{
			   	url:'gramPanchayat_data.action',
			   	editurl:'gramPanchayat_populatePanchayatUpdate.action',
			   	pager: '#pagerForPanchayatDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
			   	          
			   	       <s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
							</s:if>
			  		   	  '<s:text name="grampanchayat.code"/>',
			  		   	  '<s:property value="%{getLocaleProperty('panchayat.name')}" />',
				  		  '<s:property value="%{getLocaleProperty('city.name')}" />',
				  		  '<s:property value="%{getLocaleProperty('locality.name')}"/>',
				  		  '<s:property value="%{getLocaleProperty('state.name')}"/>',
				  		  '<s:property value="%{getLocaleProperty('country.name')}"/>',
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
					   			        }]
					   			
					   			}},	   				   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if>  
			   			{name:'code',index:'code',width:125,sortable:true},
			   			{name:'name',index:'name',width:125,sortable:true,editable: true},
			   			{name:'selectedCity',index:'selectedCity',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true}},
			   			{name:'selectedDistrict',index:'selectedDistrict',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true},
			                  editoptions: {
			                		dataEvents: [ 
			   			   			          {
			   			   			            type: "change",
			   			   			            fn: function () {
			   			   			            	//alert($(this).val());
			   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedCity";
			   			   			          		listMunicipalityz($(this).val(),id);
			   			   			            }
			   			   			        }]
			                        }
					   		},
					   	 {name:'selectedState',index:'selectedState',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true},
				                  editoptions: {
				                		dataEvents: [ 
				   			   			          {
				   			   			            type: "change",
				   			   			            fn: function () {
				   			   			            	//alert($(this).val());
				   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedDistrict";
				   			   			          		var cid=$(this).closest('tr').attr('id')+"_selectedCity";
				   			   			          		listMunicipalityz("",cid);
				   			   			          		listLocalitz($(this).val(),id);
				   			   			            }
				   			   			        }]
				                        }
						   		}, 
						   {name:'selectedCountry',index:'selectedCountry',width:50,sortable:true},
						   {name:'act',index:'act',width:60,sortable:false,formatter: "actions",formatoptions: {keys: true,
					   			onEdit : function(val){
					   				var dataa = {
					   						selectedState:jQuery('#' + val + '_selectedState').val(),
					   				}
					   				getAjaxValue("village_populateLocality.action",dataa,val,"_selectedDistrict"); 
					   				dataa = {
					   						selectedDistrict:jQuery('#' + val + '_selectedDistrict').val(),
						   			}
					   				getAjaxValue("village_populateCity.action",dataa,val,"_selectedCity");
					   			},
					   			delOptions: { url: 'gramPanchayat_delete.action' ,
						   			afterShowForm:function ($form) {
		                        	    $form.closest('div.ui-jqdialog').position({
		                        	        my: "center",
		                        	        of: $("#panchayatDetail").closest('div.ui-jqgrid')
		                        	    });
		                        	},	
		                        	
		                        	afterSubmit: function(data) 
		                            {
		                        	  var json = JSON.parse(data.responseText);
										//document.getElementById("validateErrorCate").innerHTML=json.msg;
										jQuery("#panchayatDetail").jqGrid('setGridParam',{url:"gramPanchayat_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
										showPopup(json.msg,json.title);
										jQuery('.ui-jqdialog-titlebar-close').click();
		                            }
				   		
						   		} ,
						   		
						   		
						   		afterSave: function (id, response, options) {
		                            var json = JSON.parse(response.responseText);
										jQuery("#panchayatDetail").jqGrid('setGridParam',{url:"gramPanchayat_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

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
		        	$('#panchayatDetail').setColProp('selectedState', { editoptions: { value: loadstate} });
		      		$('#panchayatDetail').setColProp('selectedDistrict', { editoptions: { value: loadDistricts} });
		      		$('#panchayatDetail').setColProp('selectedCity', { editoptions: { value: loadCities} });
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
			jQuery("#panchayatDetail").jqGrid('navGrid','#pagerForPanchayatDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#panchayatDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#panchayatDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#panchayatDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#panchayatDetail").jqGrid("setLabel","c.name","",{"text-align":"center"});

			colModel = jQuery("#panchayatDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#panchayatDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		    
		  /*   windowHeight=windowHeight-80; */
		    $('#panchayatDetail').jqGrid('setGridHeight',(windowHeight));
}

function loadCity(){
	jQuery("#cityDetail").jqGrid(
			{
			   	url:'municipality_data.action',
			   	editurl:'municipality_populateCityUpdate',
			   	pager: '#pagerForcityDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
							</s:if>
								<s:if test="currentTenantId!='livelihood'">
			  		   	  '<s:text name="village.code"/>',
			  		   	  </s:if>
			  		      '<s:text name="Villagevillage.name"/>',
			  		    '<s:property value="%{getLocaleProperty('locality.name')}"/>',
			  		  '<s:property value="%{getLocaleProperty('state.name')}"/>',
			  		 '<s:property value="%{getLocaleProperty('country.name')}"/>',
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
					   			        }]
					   			
					   			}},	   				   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if>
					   			<s:if test="currentTenantId=='olivado'">
					   				{name:'code',index:'code',width:125,sortable:true,editable: true},
					   			</s:if>
					   			<s:elseif test="currentTenantId!='livelihood'">
						   			{name:'code',index:'code',width:125,sortable:true},
						   		</s:elseif>
			   			{name:'name',index:'name',width:125,sortable:true,editable: true},
			   			{name:'selectedDistrict',index:'selectedDistrict',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true}},
			   			{name:'selectedState',index:'selectedState',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true},
			                  editoptions: {
			                		dataEvents: [ 
			   			   			          {
			   			   			            type: "change",
			   			   			            fn: function () {
			   			   			            	//alert($(this).val());
			   			   			            	var id=$(this).closest('tr').attr('id')+"_selectedDistrict";
			   			   			          		var cid=$(this).closest('tr').attr('id')+"_selectedCity";
			   			   			          		listMunicipalityz("",cid);
			   			   			          		listLocalitz($(this).val(),id);
			   			   			            }
			   			   			        }]
			                        }
					   		}, 
					   {name:'selectedCountry',index:'selectedCountry',width:125,sortable:true},
			   			 {name:'act',index:'act',width:60,sortable:false,formatter: "actions",formatoptions: {keys: true,
					   		delOptions: { url: 'municipality_delete.action' ,
					   			afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#cityDetail").closest('div.ui-jqgrid')
                            	    });
                            	},	
                            	
                            	afterSubmit: function(data) 
                                {
                            	  var json = JSON.parse(data.responseText);
									//document.getElementById("validateErrorCate").innerHTML=json.msg;
									jQuery("#cityDetail").jqGrid('setGridParam',{url:"municipality_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
                                }
			   		
					   		} ,
					   		
					   		afterSave: function (id, response, options) {
	                             var json = JSON.parse(response.responseText);
									jQuery("#cityDetail").jqGrid('setGridParam',{url:"municipality_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

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
		        	$('#cityDetail').setColProp('selectedState', { editoptions: { value: loadstate} });
		      		$('#cityDetail').setColProp('selectedDistrict', { editoptions: { value: loadDistricts} });
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
			jQuery("#cityDetail").jqGrid('navGrid','#pagerForcityDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#cityDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#cityDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#cityDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#cityDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#cityDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#cityDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		   /*  windowHeight=windowHeight-80; */
		    $('#cityDetail').jqGrid('setGridHeight',(windowHeight));
}



function loadDistrict(){
	jQuery("#districtDetail").jqGrid(
			{
			   	url:'locality_data.action',
			   	editurl:'locality_populateLocalityUpdate.action',
			   	pager: '#pagerForDistrictDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
							</s:if>
								<s:if test="currentTenantId!='livelihood'">
			  		   	  '<s:text name="village.code"/>',
			  		   	  </s:if>
			  		      '<s:text name="Villagevillage.name"/>',
			  		      '<s:property value="%{getLocaleProperty('state.name')}"/>',
			  		    '<s:property value="%{getLocaleProperty('country.name')}"/>',
			  		  	  '<s:text name="Actions"/>'
			  		     
				  		  /* '<s:text name="municipality.latitude"/>',
				  		  '<s:text name="municipality.longitude"/>' */
				  		
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
					   			        }]
					   			
					   			}},	   				   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if>
					   			<s:if test="currentTenantId=='olivado'">
					   			{name:'code',index:'code',width:125,sortable:true,editable: true},
					   			</s:if>
					   			<s:elseif test="currentTenantId!='livelihood'">
					   			{name:'code',index:'code',width:125,sortable:true},
					   			</s:elseif>
						   		{name:'name',index:'name',width:125,sortable:true,editable: true},
						   		{name:'selectedState',index:'selectedState',width:90,sortable:true,editable: true,edittype: "select", editrules:{required: true}},
						   		{name:'selectedCountry',index:'selectedCountry',width:125,sortable:true},
						   		{name:'act',index:'act',width:60,sortable:false,formatter: "actions",formatoptions: {keys: true,
							   		delOptions: { url: 'locality_delete.action',
							   			afterShowForm:function ($form) {
	                            	    $form.closest('div.ui-jqdialog').position({
	                            	        my: "center",
	                            	        of: $("#districtDetail").closest('div.ui-jqgrid')
	                            	    });
	                            	},	
	                            	
	                            	afterSubmit: function(data) 
	                                {
	                            	  var json = JSON.parse(data.responseText);
										//document.getElementById("validateErrorCate").innerHTML=json.msg;
										jQuery("#districtDetail").jqGrid('setGridParam',{url:"locality_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
										showPopup(json.msg,json.title);
										jQuery('.ui-jqdialog-titlebar-close').click();
	                                }
				   		
						   		} ,
							   		
							   		afterSave: function (id, response, options) {
			                             var json = JSON.parse(response.responseText);
											jQuery("#districtDetail").jqGrid('setGridParam',{url:"locality_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');

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
		      		$('#districtDetail').setColProp('selectedState', { editoptions: { value: loadstate} });
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
			jQuery("#districtDetail").jqGrid('navGrid','#pagerForDistrictDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#districtDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#districtDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#districtDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#districtDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#districtDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#districtDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		    $('#districtDetail').jqGrid('setGridHeight',(windowHeight));
}

function loadStates(){
	jQuery("#stateDetail").jqGrid(
			{
			   	url:'state_data.action',
			   	editurl:'state_populateStateUpdate.action',
			   	pager: '#pagerForStateDetail',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
							</s:if>
								<s:if test="currentTenantId!='livelihood'">
			  		   	  '<s:text name="village.code"/>',
			  		   	  </s:if>
			  		      '<s:text name="Villagevillage.name"/>',
			  		    '<s:property value="%{getLocaleProperty('country.name')}"/>',
			  		  '<s:text name="Actions"/>'
			  		     
				  		  /* '<s:text name="municipality.latitude"/>',
				  		  '<s:text name="municipality.longitude"/>' */
				  		
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
					   			        }]
					   			
					   			}},	   				   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if>
					   			<s:if test="currentTenantId=='olivado'">
					   			{name:'code',index:'code',width:125,sortable:true,editable: true},
					   			</s:if>
					   			<s:elseif test="currentTenantId!='livelihood'">
					   			{name:'code',index:'code',width:125,sortable:true},
					   			</s:elseif>
						   		{name:'name',index:'name',width:125,sortable:true,editable: true},
						   		{name:'selectedCountry',index:'selectedCountry',width:90,sortable:true},
						   		{name:'act',index:'act',width:60,sortable:false,formatter: "actions",formatoptions: {keys: true,
							   		delOptions: { url: 'state_delete.action' ,
							   			afterShowForm:function ($form) {
		                            	    $form.closest('div.ui-jqdialog').position({
		                            	        my: "center",
		                            	        of: $("#stateDetail").closest('div.ui-jqgrid')
		                            	    });
		                            	},	
		                            	
		                            	afterSubmit: function(data) 
		                                {
		                            	  var json = JSON.parse(data.responseText);
											//document.getElementById("validateErrorCate").innerHTML=json.msg;
											jQuery("#stateDetail").jqGrid('setGridParam',{url:"state_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
											showPopup(json.msg,json.title);
											jQuery('.ui-jqdialog-titlebar-close').click();
		                                }
					   		
							   		} ,
							   	 afterSave: function (id, response, options) {
		                             var json = JSON.parse(response.responseText);
		                             jQuery("#stateDetail").jqGrid('setGridParam',{url:"state_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		                             showPopup(json.msg,json.title);
										jQuery('.ui-jqdialog-titlebar-close').click();
		                         }
						   		}}	
			   			/* {name:'latitude',index:'latitude',width:125,sortable:true},
			   			{name:'longitude',index:'longitude',width:125,sortable:true} */
			   		   
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
		      		$('#stateDetail').setColProp('selectedCountry', { editoptions: { value: loadCountries} });
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
			jQuery("#stateDetail").jqGrid('navGrid','#pagerForStateDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#stateDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#stateDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#stateDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#stateDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#stateDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#stateDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		    $('#stateDetail').jqGrid('setGridHeight',(windowHeight));
}

function loadCountry(){
	jQuery("#countryDetail").jqGrid(
			{
			   	url:'country_data.action',
			   	pager: '#pagerForCountryDetail',
			   	editurl:'country_populateCountryUpdate.action',
			   	mtype: 'POST',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[	
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
							</s:if>
								<s:if test="currentTenantId!='livelihood'">
			  		   	  '<s:text name="village.code"/>',
			  		   	  </s:if>
			  		      '<s:text name="Villagevillage.name"/>',
			  		    '<s:text name="Actions"/>'
			  		     
				  		  /* '<s:text name="municipality.latitude"/>',
				  		  '<s:text name="municipality.longitude"/>' */
				  		
						 ],
			   	colModel:[
						<s:if test='branchId==null'>
					   		{name:'branchId',index:'branchId',width:125,sortable: false,width :100,search:true,stype: 'select',searchoptions: {
					   			value: '<s:property value="parentBranchFilterText"/>',
					   			dataEvents: [ 
					   			          {
					   			            type: "change",
					   			            fn: function () {
					   			            	console.log($(this).val());
					   			             	getSubBranchValues($(this).val())
					   			            }
					   			        }]
					   			
					   			}},	   				   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if>
					   			<s:if test="currentTenantId!='livelihood'">
			   			{name:'code',index:'code',width:80,sortable:true},
			   			</s:if>
			   			{name:'name',index:'name',width:125,sortable:true,editable: true},
			   			{name:'act',index:'act',width:60,sortable:false,formatter: "actions",formatoptions: {keys: true,
					   		delOptions: { url: 'country_delete.action' ,
					   			afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#countryDetail").closest('div.ui-jqgrid')
                            	    });
                            	},	
                            	
                            	afterSubmit: function(data) 
                                {
                            	  var json = JSON.parse(data.responseText);
									//document.getElementById("validateErrorCate").innerHTML=json.msg;
									jQuery("#countryDetail").jqGrid('setGridParam',{url:"country_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery.post("state_countryList.action",function(result){
									 	insertOptions("scountry",JSON.parse(result)); 
									 }),
									jQuery('.ui-jqdialog-titlebar-close').click();
									
                                }
			   		
					   		} ,
					   		
					   	 afterSave: function (id, response, options) {
                             var json = JSON.parse(response.responseText);
								jQuery("#countryDetail").jqGrid('setGridParam',{url:"country_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								if(json.status=="1"){
								showPopup(json.msg,json.title);
								jQuery.post("state_countryList.action",function(result){
								 	insertOptions("scountry",JSON.parse(result)); 
								});
								}
								else{
						    		 showPopup(json.msg,json.title);
						    	}
								jQuery('.ui-jqdialog-titlebar-close').click();
                         }
				   		}}
			   			/* {name:'latitude',index:'latitude',width:125,sortable:true},
			   			{name:'longitude',index:'longitude',width:125,sortable:true} */
			   		   
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
		      		
		      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
		        }
		    });	
			jQuery("#countryDetail").jqGrid('navGrid','#pagerForCountryDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#countryDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			jQuery("#countryDetail").jqGrid("setLabel","code","",{"text-align":"center"});
			jQuery("#countryDetail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#countryDetail").jqGrid("setLabel","l.name","",{"text-align":"center"});
			

			colModel = jQuery("#countryDetail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#countryDetail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });	
		    $('#countryDetail').jqGrid('setGridHeight',(windowHeight));
}



function listState(val,id){
	var selectedCountry = $('#country').val();
	jQuery.post("village_populateState.action",{id:val,dt:new Date(),selectedCountry:val},function(result){
		insertOptions(id,JSON.parse(result));
		//listLocality(document.getElementById(id));
	});
}
   


function listLocality(val,id){
	jQuery.post("village_populateLocality.action",{id:val,dt:new Date(),selectedState:val},function(result){
		insertOptions(id,JSON.parse(result));
		//listMunicipality(document.getElementById(id));
	});
}

function listLocalitz(val,id){
	var selectedState = val;
	jQuery.post("village_populateLocality.action",{id:selectedState,dt:new Date(),selectedState:selectedState},function(result){
		insertOptions(id,JSON.parse(result));
		//listMunicipalityz("",id);
	});
}

function listMunicipality(val,id){
	jQuery.post("village_populateCity.action",{id:val,dt:new Date(),selectedDistrict:val},function(result){
		insertOptions(id,JSON.parse(result));
		//listGramPanchayat(document.getElementById(id));
	});
}


function listMunicipalityz(val,id){
	jQuery.post("village_populateCity.action",{id:val,dt:new Date(),selectedDistrict:val},function(result){
		insertOptions(id,JSON.parse(result));
		//listGramPanchayat(document.getElementById("vcities"));
	});
}

function listGramPanchayatz(val,id){
	jQuery.post("village_populateGramPanchayat.action",{id:val,dt:new Date(),selectedCity:val},function(result){
		insertOptions(id,JSON.parse(result));
		//listGramPanchayat(document.getElementById("vcities"));
	});
}

function listGramPanchayat(obj){
	if(gramPanchayatEnable=="1"){
	var selectedLocality = $('#cities').val();
	jQuery.post("village_populateGramPanchayat.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
		insertOptions("vgramPanchayats",JSON.parse(result));
		//listGramPanchayat(document.getElementById("gramPanchayats"));
	});
	}
}

function addVillage(){
	var country = getElementValueById("vcountry");
	var state = getElementValueById("vstate");
	var localites = getElementValueById("vlocalites");
	var cities = getElementValueById("vcities");
	var gramPanchayats= getElementValueById("vgramPanchayats");
	var village=getElementValueById("villageName");
	var code=getElementValueById("villageCode");
	var hit=true;
	
	jQuery(".verror").empty();
	
	if(isEmpty(country)){
		jQuery(".verror").append('<s:text name="empty.country"/>');
		hit=false;
	}else if(isEmpty(state)){
		jQuery(".verror").append('<s:property value="%{getLocaleProperty('empty.state')}" />');
		hit=false;
	}else if(isEmpty(localites)){
		jQuery(".verror").append('<s:property value="%{getLocaleProperty('empty.district')}" />');
		hit=false;
	}else if(isEmpty(cities)){
		jQuery(".verror").append('<s:property value="%{getLocaleProperty('empty.city')}" />');
		hit=false;
	}
	else if((gramPanchayatEnable=='1')&& isEmpty(gramPanchayats)){
		jQuery(".verror").append('<s:property value="%{getLocaleProperty('empty.gramPanchayat')}" />');
		hit=false;
	}else if(isEmpty(village)){
		jQuery(".verror").append('<s:property value="%{getLocaleProperty('empty.villname')}" />');
		hit=false;
	}	
		
	
	if(hit){
		var dataa = {
				selectedCountry:country,
				selectedState:state,
				selectedDistrict:localites,
				selectedCity:cities,
				selectedGramPanchayat:gramPanchayats,
				name:village,
				code:code
		}
		
		$.ajax({
			 url:'village_populateSaveVillage.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 jQuery("#detail").jqGrid('setGridParam',{url:"village_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	 resetData("villageForm");
		    	 showPopup(result.msg,result.title);
		    	 resetLocationValues();
		     },
		     error: function(data) {
		     	alert("Some Error Occured , Please Try again");
		     	window.location.href="village_list.action";
		     }
		});
	}
}

function addPanchayat(){
	var country = getElementValueById("pcountry");
	var state = getElementValueById("pstate");
	var localites = getElementValueById("plocalites");
	var city=getElementValueById("pcities");
	var panchayat=getElementValueById("panchayatName");
	var hit=true;
	
	jQuery(".perror").empty();
	
	if(isEmpty(country)){
		jQuery(".perror").append('<s:text name="empty.country"/>');
		hit=false;
	}else if(isEmpty(state)){
		jQuery(".perror").append('<s:text name="empty.state"/>');
		hit=false;
	}else if(isEmpty(localites)){
		jQuery(".perror").append('<s:text name="empty.district"/>');
		hit=false;
	}else if(isEmpty(city)){
		jQuery(".perror").append('<s:text name="empty.city"/>');
		hit=false;
	}else if(isEmpty(panchayat)){
		jQuery(".perror").append('<s:text name="empty.name"/>');
		hit=false;
	}
	
	if(hit){
		var dataa = {
				selectedCountry:country,
				selectedState:state,
				selectedDistrict:localites,
				selectedCity:city,
				name:panchayat
		}
		
		$.ajax({
			 url:'gramPanchayat_populateSavePanchayat.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 jQuery("#panchayatDetail").jqGrid('setGridParam',{url:"gramPanchayat_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	 resetData("panchayatForm");
		    	 
		    	 showPopup(result.msg,result.title);
		    	 resetLocationValues();
		     },
		     error: function(data) {
		     	alert("Some Error Occured , Please Try again");
		     	window.location.href="village_list.action";
		     }
		});
		
		/* jQuery.post("village_create.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
			
		}); */
	}
}

function addCity(){
	var country = getElementValueById("ccountry");
	var state = getElementValueById("cstate");
	var localites = getElementValueById("clocalites");
	var city=getElementValueById("cityName");
	var hit=true;
	
	jQuery(".cerror").empty();
	
	if(isEmpty(country)){
		jQuery(".cerror").append('<s:text name="empty.country"/>');
		hit=false;
	}else if(isEmpty(state)){
		jQuery(".cerror").append('<s:property value="%{getLocaleProperty('empty.state')}" />');
		hit=false;
	}else if(isEmpty(localites)){
		jQuery(".cerror").append('<s:property value="%{getLocaleProperty('empty.district')}" />');
		hit=false;
	}else if(isEmpty(city)){
		jQuery(".cerror").append('<s:property value="%{getLocaleProperty('empty.citname')}" />');
		hit=false;
	}
	
	if(hit){
		var dataa = {
				selectedCountry:country,
				selectedState:state,
				selectedDistrict:localites,
				name:city
		}
		
		$.ajax({
			 url:'municipality_populateSaveCity.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 jQuery("#cityDetail").jqGrid('setGridParam',{url:"municipality_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	 resetData("cityForm");
		    	 showPopup(result.msg,result.title);
		    	 resetLocationValues();
		     },
		     error: function(data) {
		     	alert("Some Error Occured , Please Try again");
		     	window.location.href="village_list.action";
		     }
		});
		
		/* jQuery.post("village_create.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
			
		}); */
	}
}

function addLocality(){
	var country = getElementValueById("dcountry");
	var state = getElementValueById("dstate");
	var district=getElementValueById("districtName");
	var hit=true;
	
	jQuery(".lerror").empty();
	
	if(isEmpty(country)){
		jQuery(".lerror").append('<s:text name="empty.country"/>');
		hit=false;
	}else if(isEmpty(state)){
		jQuery(".lerror").append('<s:property value="%{getLocaleProperty('empty.state')}" />');
		hit=false;
	}else if(isEmpty(district)){
		jQuery(".lerror").append('<s:property value="%{getLocaleProperty('empty.disname')}" />');
		hit=false;
	}
	
	if(hit){
		var dataa = {
				selectedCountry:country,
				selectedState:state,
				name:district
		}
		
		$.ajax({
			 url:'locality_populateSaveLocality.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 jQuery("#districtDetail").jqGrid('setGridParam',{url:"locality_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	 resetData("districtForm");
		    	 showPopup(result.msg,result.title);
		    	 resetLocationValues();
		     },
		     error: function(data) {
		     	alert("Some Error Occured , Please Try again");
		     	window.location.href="village_list.action";
		     }
		});
	}
}

function addState(){
	
	var country = getElementValueById("scountry");
	var state = getElementValueById("stateName");
	var hit=true;
	
	jQuery(".serror").empty();
	
	if(isEmpty(country)){
		jQuery(".serror").append('<s:text name="empty.country"/>');
		hit=false;
	}else if(isEmpty(state)){
		jQuery(".serror").append('<s:property value="%{getLocaleProperty('empty.statename')}" />');
		hit=false;
	}
	
	if(hit){
		var dataa = {
				selectedCountry:country,
				name:state
		}
		
		$.ajax({
			 url:'state_populateStateCreate.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 jQuery("#stateDetail").jqGrid('setGridParam',{url:"state_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	 if(result.status=="1"){
		    	 resetData("stateForm");
		    	 showPopup(result.msg,result.title);
		    	resetLocationValues();
		    	 }
		    	 else{
		    		 showPopup(result.msg,result.title);
		    	 }
		     },
		     error: function(data) {
		     	alert("Some Error Occured , Please Try again");
		     	window.location.href="village_list.action";
		     }
		});
	}
}

function addCountry(){
	var country = getElementValueById("countryName");

	var hit=true;
	
	jQuery(".coerror").empty();
	
	if(isEmpty(country)){
		jQuery(".coerror").append('<s:text name="empty.countryName"/>');
		hit=false;
	}
	if(hit){
		var dataa = {
				name:country
		}
		
		$.ajax({
			 url:'country_populateCountryCreate.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(data) {
		    	 jQuery("#countryDetail").jqGrid('setGridParam',{url:"country_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		    	if(data.status=="1"){
		    	 resetData("countryForm");
		    	 showPopup(data.msg,data.title);
		    	 window.location.href="village_list.action";
		    	 resetLocationValues();
		    	}
		    	else{
		    		 showPopup(data.msg,data.title);
		    	}
		     },
		     error: function(data) {
		     	alert("Some Error Occured , Please Try again");
		     	window.location.href="village_list.action";
		     }
		});
	}
	jQuery.post("state_countryList.action",function(result){
	 	insertOptions("scountry",JSON.parse(result));  
 	});
}

//@PARAM(URL,POST DATA,SELECTED ROW ID,ID SUFFIX)
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

	
	console.log("ctrlName:"+ctrlName);
	document.getElementById(ctrlName).length = 0;
	/* addOption(document.getElementById(ctrlName), "Select", ""); */
	for (var i = 0; i < jsonArr.length; i++) {
		addOption(document.getElementById(ctrlName), jsonArr[i].name,
				jsonArr[i].id);
	}

	var id = "#" + ctrlName;
	jQuery(id).select2();
}

function resetData(id){
	$("#"+id)[0].reset();
	$("#"+id).trigger("reset");
	$('.select2').trigger("change");
	 $('.select2').select2();
}
	

</script>
	<div id="dialog" style="display: none"></div>
	<div class="appContentWrapper marginBottom">
		<ul class="nav nav-pills">
			<li class="active"><a data-toggle="pill" href="#countries"><s:property
						value="%{getLocaleProperty('country.name')}" /></a></li>

			<li><a data-toggle="pill" href="#states"><s:property
						value="%{getLocaleProperty('state.name')}" /></a></li>

			<li><a data-toggle="pill" href="#district"><s:property
						value="%{getLocaleProperty('locality.name')}" /></a></li>

			<li><a data-toggle="pill" href="#city"><s:property
						value="%{getLocaleProperty('city.name')}" /></a></li>

			<s:if test="gramPanchayatEnable==1">
				<li><a data-toggle="pill" href="#panchayat"><s:property
							value="%{getLocaleProperty('panchayat.name')}" /></a></li>
			</s:if>

			<li><a data-toggle="pill" href="#village"><s:property
						value="%{getLocaleProperty('village.name')}" /></a></li>
		</ul>
	</div>

	<div class="tab-content">
		<div id="village" class="tab-pane fade">
			<s:form action="village_create" name="villageForm" id="villageForm">
				<sec:authorize ifAllGranted="profile.location.village.create">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#villageAccordian" class="btn btn-sts margin-bottom10px">
						<s:text name="create.button" />
					</a>
				</sec:authorize>

				<div id="villageAccordian" class="panel-collapse collapse">

					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.village')}" />
							</h2>
						</div>
						<div class="error verror">
							<s:actionerror />
							<s:fielderror />
						</div>
						<div class="flex-layout filterControls">

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('country.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCountry" list="countriesList"
										headerKey="" headerValue="%{getText('txt.select')}" Key="name"
										Value="name" theme="simple" id="vcountry"
										onchange="listState(this.value,'vstate')"
										cssClass="form-control select2" />
								</div>
							</div>


							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('state.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedState" list="states" Key="code"
										Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										id="vstate" onchange="listLocality(this.value,'vlocalites')"
										cssClass="form-control  select2" />
								</div>
							</div>


							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('locality.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedDistrict" id="vlocalites"
										list="localities" Key="code" Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										onchange="listMunicipality(this.value,'vcities')"
										cssClass="form-control  select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('city.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCity" id="vcities" list="cities"
										Key="code" Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										onchange="listGramPanchayat(this)"
										cssClass="form-control  select2" />
								</div>
							</div>

							<s:if test="gramPanchayatEnable==1">
								<div class="flexItem">
									<label for="txt"><s:property
											value="%{getLocaleProperty('gramPanchayat.name')}" /><sup style="color: red;">*</sup></label>
									<div class="form-element">
										<s:select name="village.gramPanchayat.code"
											id="vgramPanchayats" list="gramPanchayats" Key="code"
											Value="name" headerKey=""
											headerValue="%{getText('txt.select')}" theme="simple"
											cssClass="form-control  select2" />
									</div>
								</div>
							</s:if>
							<s:if
					test="currentTenantId=='symrise' || currentTenantId=='avt'">
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('village.code')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="village.code" theme="simple" maxlength="35"
										cssClass="form-control " id="villageCode" />

								</div>
							</div>
							</s:if>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('village.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="village.name" theme="simple" maxlength="35"
										cssClass="form-control " id="villageName" />

								</div>
							</div>
							<div class="flexItem" style="margin-top: 24px;">
								<button type="button" class="btn btn-sts" id="add"
									onclick="addVillage()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>
						</div>

					</div>

				</div>
			</s:form>

			<div class="appContentWrapper marginBottom">
				<div style="width: 100%;" id="baseDiv1">
					<table id="detail"></table>
					<div id="pagerForDetail"></div>
				</div>
			</div>
		</div>

		<div id="panchayat" class="tab-pane fade">
			<s:form name="panchayatForm" id="panchayatForm">
				<sec:authorize ifAllGranted="profile.location.village.create">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#pachayatAccordian" class="btn btn-sts margin-bottom10px">
						<s:text name="create.button" />
					</a>
				</sec:authorize>

				<div id="pachayatAccordian" class="panel-collapse collapse">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.grampanchayat')}" />
							</h2>
						</div>
						<div class="error perror">
							<s:actionerror />
							<s:fielderror />
						</div>

						<div class="flex-layout filterControls">
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('country.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCountry" list="countriesList"
										headerKey="" headerValue="%{getText('txt.select')}" Key="name"
										Value="name" theme="simple" id="pcountry"
										onchange="listState(this.value,'pstate')"
										cssClass="form-control select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('state.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedState" list="states" Key="code"
										Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										id="pstate" onchange="listLocality(this.value,'plocalites')"
										cssClass="form-control  select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('locality.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedDistrict" id="plocalites"
										list="localities" Key="code" Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										onchange="listMunicipality(this.value,'pcities')"
										cssClass="form-control  select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('city.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCity" id="pcities" list="cities"
										Key="code" Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										cssClass="form-control  select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('panchayat.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="panchayt.name" theme="simple" maxlength="35"
										cssClass="form-control " id="panchayatName" />

								</div>
							</div>
							<div class="flexItem" style="margin-top: 24px;">
								<button type="button" class="btn btn-sts" id="add"
									onclick="addPanchayat()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>
						</div>
					</div>
				</div>
			</s:form>

			<div class="appContentWrapper marginBottom">
				<table id="panchayatDetail"></table>
				<div id="pagerForPanchayatDetail"></div>
			</div>
		</div>


		<div id="city" class="tab-pane fade">
			<s:form name="cityForm" id="cityForm">
				<sec:authorize ifAllGranted="profile.location.village.create">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#cityAccordian" class="btn btn-sts margin-bottom10px"> <s:text
							name="create.button" />
					</a>
				</sec:authorize>

				<div id="cityAccordian" class="panel-collapse collapse">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.municipality')}" />
							</h2>
						</div>
						<div class="error cerror">
							<s:actionerror />
							<s:fielderror />
						</div>

						<div class="flex-layout filterControls">
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('country.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCountry" list="countriesList"
										headerKey="" headerValue="%{getText('txt.select')}" Key="name"
										Value="name" theme="simple" id="ccountry"
										onchange="listState(this.value,'cstate')"
										cssClass="form-control select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('state.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedState" list="states" Key="code"
										Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										id="cstate" onchange="listLocality(this.value,'clocalites')"
										cssClass="form-control  select2" />
								</div>
							</div>


							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('locality.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedDistrict" id="clocalites"
										list="localities" Key="code" Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										cssClass="form-control  select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('city.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="city.name" theme="simple" maxlength="35"
										cssClass="form-control " id="cityName" />

								</div>
							</div>
							<div class="flexItem" style="margin-top: 24px;">
								<button type="button" class="btn btn-sts" id="add"
									onclick="addCity()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>

						</div>
					</div>
				</div>
			</s:form>

			<div class="appContentWrapper marginBottom">
				<table id="cityDetail"></table>
				<div id="pagerForcityDetail"></div>
			</div>
		</div>
		<div id="district" class="tab-pane fade">
			<s:form name="districtForm" id="districtForm">
				<sec:authorize ifAllGranted="profile.location.village.create">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#DistrictAccordian" class="btn btn-sts margin-bottom10px">
						<s:text name="create.button" />
					</a>
				</sec:authorize>

				<div id="DistrictAccordian" class="panel-collapse collapse">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.locality')}" />
							</h2>
						</div>
						<div class="error lerror">
							<s:actionerror />
							<s:fielderror />
						</div>

						<div class="flex-layout filterControls">
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('country.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCountry" list="countriesList"
										headerKey="" headerValue="%{getText('txt.select')}" Key="name"
										Value="name" theme="simple" id="dcountry"
										onchange="listState(this.value,'dstate')"
										cssClass="form-control select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('state.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedState" list="states" Key="code"
										Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										id="dstate" cssClass="form-control  select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('locality.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="city.name" theme="simple" maxlength="35"
										cssClass="form-control " id="districtName" />

								</div>
							</div>
							<div class="flexItem" style="margin-top: 24px;">
								<button type="button" class="btn btn-sts" id="add"
									onclick="addLocality()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>
						</div>
					</div>
				</div>
			</s:form>
			<div class="appContentWrapper marginBottom">
				<table id="districtDetail"></table>
				<div id="pagerForDistrictDetail"></div>
			</div>
		</div>

		<div id="states" class="tab-pane fade">
			<s:form name="stateForm" id="stateForm">
				<sec:authorize ifAllGranted="profile.location.village.create">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#stateAccordian" class="btn btn-sts margin-bottom10px">
						<s:text name="create.button" />
					</a>
				</sec:authorize>

				<div id="stateAccordian" class="panel-collapse collapse">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.state')}" />
							</h2>
						</div>
						<div class="error serror">
							<s:actionerror />
							<s:fielderror />
						</div>

						<div class="flex-layout filterControls">
							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('country.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCountry" list="countriesList"
										headerKey="" headerValue="%{getText('txt.select')}" Key="name"
										Value="name" theme="simple" id="scountry"
										cssClass="form-control select2" />
								</div>
							</div>

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('state.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="city.name" theme="simple" maxlength="35"
										cssClass="form-control " id="stateName" />

								</div>
							</div>
							<div class="flexItem" style="margin-top: 24px;">
								<button type="button" class="btn btn-sts" id="add"
									onclick="addState()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>
						</div>
					</div>
				</div>
			</s:form>

			<div class="appContentWrapper marginBottom">
				<table id="stateDetail"></table>
				<div id="pagerForStateDetail"></div>
			</div>
		</div>

		<div id="countries" class="tab-pane fade in active">


			<s:form name="countryForm" id="countryForm">
				<sec:authorize ifAllGranted="profile.location.village.create">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#countryAccordian" class="btn btn-sts margin-bottom10px">
						<s:text name="create.button" />
					</a>
				</sec:authorize>

				<div id="countryAccordian" class="panel-collapse collapse">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.country')}" />
							</h2>
						</div>
						<div class="error coerror">
							<s:actionerror />
							<s:fielderror />
						</div>

						<div class="flex-layout filterControls">

							<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('country.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element flexdisplay">
									<s:textfield name="countryName" theme="simple" maxlength="35"
										cssClass="form-control " id="countryName" />

								</div>
							</div>
							<div class="flexItem" style="margin-top: 24px;">
								<button type="button" class="btn btn-sts" id="add"
									onclick="addCountry()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>
						</div>
					</div>
				</div>

			</s:form>

			<div class="appContentWrapper marginBottom">
				<div style="width: 99%;" id="baseDiv">
					<table id="countryDetail"></table>
					<div id="pagerForCountryDetail"></div>
				</div>
			</div>
		</div>

	</div>