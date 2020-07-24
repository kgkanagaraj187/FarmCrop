<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
function loadTopicGrid()
{
	jQuery("#detail").jqGrid(
			{
			   	url:'trainingTopic_data.action',
			   	pager: '#pagerForDetail',	  
			   	datatype: "json",	
			   	mtype: 'POST',
			   	editurl:'trainingTopic_update.action',
			 	styleUI : 'Bootstrap',
			   	colNames:[
						 <s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
						</s:if>		    
					    '<s:text name="%{getLocaleProperty('trainingTopic.code')}"/>',
						'<s:text name="%{getLocaleProperty('trainingTopic.name')}"/>',
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
 			   		{name:'name',index:'name',width:125,sortable:true,editable:true},
 		   		//  {name:'topicNameId',index:'topicNameId',width:125,sortable:true,editable:true},  */
			   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                        formatoptions: {
                            keys: true, 
                          
                            delOptions: { 
                            	url: 'trainingTopic_delete.action',
                            	afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#detail").closest('div.ui-jqgrid')
                            	    });
                            	},
                            	
                              afterSubmit: function(data) 
                                {
                                	var json = JSON.parse(data.responseText);
									jQuery("#detail").jqGrid('setGridParam',{url:"trainingTopic_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
                                }
			   		
                                
                                
                            },
                            
                            afterSave: function (id, response, options) {
                                var json = JSON.parse(response.responseText);
								jQuery("#detail").jqGrid('setGridParam',{url:"trainingTopic_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
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
			    /* onSelectRow: function(id){ 
				  document.updateform.id.value  =id;
			      document.updateform.submit();      
				},		 */
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
						//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
		       		
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
}
function loadObserveGrid(){
	jQuery("#detailObs").jqGrid(
{
	url:'observations_data.action',
   	pager: '#pageForObserve',	  
   	datatype: "json",	
   	mtype: 'POST',
   	editurl:'observations_update.action',
   	styleUI : 'Bootstrap',
   	colNames:[
			<s:if test='branchId==null'>
			'<s:text name="app.branch"/>',
			</s:if>
			<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
			'<s:text name="app.subBranch"/>',
			</s:if>
			'<s:text name="%{getLocaleProperty('observations.code')}"/>',
			'<s:text name="%{getLocaleProperty('observations.name')}"/>',
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
   		//{name:'name',index:'name',width:125,sortable:true,editable:true},
   		 {name:'observationName',index:'observationName',width:125,sortable:true,editable:true},
   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
            formatoptions: {
                keys: true, 
              
                delOptions: { 
                	url: 'observations_delete.action',
                	afterShowForm:function ($form) {
                	    $form.closest('div.ui-jqdialog').position({
                	        my: "center",
                	        of: $("#detailObs").closest('div.ui-jqgrid')
                	    });
                	},
                	
                  afterSubmit: function(data) 
                    {
                    	var json = JSON.parse(data.responseText);
						jQuery("#detailObs").jqGrid('setGridParam',{url:"observations_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
						showPopup(json.msg,json.title);
						jQuery('.ui-jqdialog-titlebar-close').click();
                    }
   		
                    
                    
                },
                
                afterSave: function (id, response, options) {
                    var json = JSON.parse(response.responseText);
					jQuery("#detailObs").jqGrid('setGridParam',{url:"observations_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
					showPopup(json.msg,json.title);
					jQuery('.ui-jqdialog-titlebar-close').click();
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
			//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
   		
    }
});
jQuery("#detailObs").jqGrid('navGrid','#pageForObserve',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
jQuery("#detailObs").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

colModel = jQuery("#detailObs").jqGrid('getGridParam', 'colModel');
$('#gbox_' + $.jgrid.jqID(jQuery("#detailObs")[0].id) +
    ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
    var cmi = colModel[i], colName = cmi.name;

    if (cmi.sortable !== false) {
        $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
        $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
    }
});
}
function loadMaterialGrid()
{
	
	jQuery("#detailMat").jqGrid(
			{
				url:'trainingMaterial_data.action',
			   	pager: '#pagerForMaterial',	  
			   	datatype: "json",	
			   	mtype: 'POST',
			   	editurl:'trainingMaterial_update.action',
			   	styleUI : 'Bootstrap',
			   	colNames:[
						<s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
						</s:if>
						'<s:text name="%{getLocaleProperty('trainingMaterial.code')}"/>',
						'<s:text name="%{getLocaleProperty('trainingMaterial.name')}"/>',
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
			   		//{name:'name',index:'name',width:125,sortable:true,editable:true},
			   		 {name:'materialsName',index:'materialsName',width:125,sortable:true,editable:true}, 
			   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
			   		 formatoptions: {
                         keys: true, 
                       
                         delOptions: { 
                         	url: 'trainingMaterial_delete.action',
                         	afterShowForm:function ($form) {
                         	    $form.closest('div.ui-jqdialog').position({
                         	        my: "center",
                         	        of: $("#detailMat").closest('div.ui-jqgrid')
                         	    });
                         	},
                         	
                           afterSubmit: function(data) 
                             {
                             	var json = JSON.parse(data.responseText);
									jQuery("#detailMat").jqGrid('setGridParam',{url:"trainingMaterial_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
                             }
			   		
                             
                             
                         },
                         
                         afterSave: function (id, response, options) {
                             var json = JSON.parse(response.responseText);
								jQuery("#detailMat").jqGrid('setGridParam',{url:"trainingMaterial_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
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
						//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
		       		
		        }
			});
			
			jQuery("#detailMat").jqGrid('navGrid','#pagerForMaterial',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detailMat").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

			colModel = jQuery("#detailMat").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#detailMat")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		}); 
			}
function loadCriteriaCatGrid()
{
	jQuery("#detailCat").jqGrid(
			{
			   	url:'trainingCriteriaCategory_populatedata.action',
			   	pager: '#pagerForDetailCategory',	  
			   	datatype: "json",	
			   	mtype: 'POST',
				styleUI : 'Bootstrap',
				editurl:'trainingCriteriaCategory_update.action',
			   	colNames:[
					<s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
					</s:if>
					'<s:text name="%{getLocaleProperty('trainingCriteriaCategory.code')}"/>',
					'<s:text name="%{getLocaleProperty('trainingCriteriaCategory.name')}"/>',
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
					//{name:'name',index:'name',width:125,sortable:true,editable:true},
			   		 {name:'topicCategoryName',index:'topicCategoryName',width:125,sortable:true,editable:true},
			   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                        formatoptions: {
                            keys: true, 
                         
                            delOptions: { 
                            	url: 'trainingCriteriaCategory_populatedelete.action',
                            	afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#detailCat").closest('div.ui-jqgrid')
                            	    });
                            	},
                            	
                              afterSubmit: function(data) 
                                {
                                	var json = JSON.parse(data.responseText);
									jQuery ("#detailCat").jqGrid('setGridParam',{url:"trainingCriteriaCategory_populatedata.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery.post("topicCriteria_populateTopicCategoryList.action",function(result){
										insertOptions("topicCategoryId",JSON.parse(result));
										insertOptions("id",JSON.parse(result));
										
									});
									jQuery('.ui-jqdialog-titlebar-close').click();
                                }
			   		
                                
                                
                            },
                            
                            afterSave: function (id, response, options) {
                                var json = JSON.parse(response.responseText);
								jQuery("#detailCat").jqGrid('setGridParam',{url:"trainingCriteriaCategory_populatedata.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery.post("topicCriteria_populateTopicCategoryList.action",function(result){
									insertOptions("topicCategoryId",JSON.parse(result));
									insertOptions("id",JSON.parse(result));
									
								});
								jQuery('.ui-jqdialog-titlebar-close').click();
						
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
		      		
		       		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
		       		$(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
		       		$(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
		       		$(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
						//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
		       		
		        }
			});
			
			jQuery("#detailCat").jqGrid('navGrid','#pagerForDetailCategory',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detailCat").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#detailCat")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });
}

function loadCriteriaGrid()
{
	var category = $.ajax({url: 'topicCriteria_populateTopicCategory.action', async: false, success: function(data, result) {if (!result) alert('Failure to retrieve the Countries.');}}).responseText;
	var loadCategory=category.replace("{","").replace("}","");

	jQuery("#detailCriteria").jqGrid(
			{
			url:'topicCriteria_populatedata.action',
			pager: '#pagerForDetailCriteria',
			mtype: 'POST',
			datatype: "json",
			styleUI : 'Bootstrap',
			editurl:'topicCriteria_populateupdate.action',
			colNames:[	
					<s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
					</s:if>
					'<s:text name="%{getLocaleProperty('topic.Criteriacode')}"/>',
					'<s:text name="%{getLocaleProperty('topic.Criteriaprinciple')}"/>',
					'<s:text name="%{getLocaleProperty('topic.des')}"/>',
					'<s:text name="%{getLocaleProperty('topic.topicCategory.name')}"/>',
		  		    '<s:text name="Actions"/>'
		      	 ],
		   colModel : [	
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
		      	      {name:'code',index:'code',sortable:true, width:125},
		      	      {name:'principle',index:'principle', sortable:true, width:125,editable:true},
		      	    {name:'topicDes',index:'topicDes', sortable:true, width:125,editable:true},
		      	    {name:'topicCategoryId',index:'topicCategoryId',sortable:false, width:70,editable: true,
							 edittype: "select", editrules: { required: true }},
		      	    {name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
	                        formatoptions: {
	                            keys: true, 
	                         
	                            delOptions: { 
	                            	url: 'topicCriteria_populatedelete.action',
	                            	afterShowForm:function ($form) {
	                            	    $form.closest('div.ui-jqdialog').position({
	                            	        my: "center",
	                            	        of: $("#detailCriteria").closest('div.ui-jqgrid')
	                            	    });
	                            	},
	                            	
	                              afterSubmit: function(data) 
	                                {
	                                	var json = JSON.parse(data.responseText);
										jQuery("#detailCriteria").jqGrid('setGridParam',{url:"topicCriteria_populatedata.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
										showPopup(json.msg,json.title);
										jQuery('.ui-jqdialog-titlebar-close').click();
	                                }
				   		
	                                
	                                
	                            },
	                            
	                            afterSave: function (id, response, options) {
	                                var json = JSON.parse(response.responseText);
									jQuery("#detailCriteria").jqGrid('setGridParam',{url:"topicCriteria_populatedata.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
	                            }
	                        }
				   		}
		      	 ],			
		    height: 301, 
			width: $("#baseDiv").width(),
			scrollOffset: 0,
			rowNum:10,
			rowList : [10,25,50],
			sortname: 'id',
			sortorder: 'desc',
			viewrecords: true,
		  /*   onSelectRow: function(id){ 
			  document.detailform.id.value =id;
	          document.detailform.submit();      
			},		 */
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
            
				loadComplete: function() {
					$('#detailCriteria').setColProp('topicCategoryId', { editoptions: { value: loadCategory} });
	       		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
	       		$(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
	       		$(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
	       		$(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
					//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
	       		
	        }
	   });
	jQuery("#detailCriteria").jqGrid('navGrid','#pagerForDetailCriteria',{edit:false,add:false,del:false,search:false,refresh:true})
	jQuery("#detailCriteria").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});			

	colModel = jQuery("#detailCriteria").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + $.jgrid.jqID(jQuery("#detailCriteria")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;

        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        }
    });
	
	
}

function loadTargetGroupGrid()
{
	
	jQuery("#detailTargetGroup").jqGrid(
			{
			   	url:'targetGroup_data.action',
			   	pager: '#pagerForDetailGroup',	  
			   	datatype: "json",	
			   	mtype: 'POST',
			   	styleUI : 'Bootstrap',
			   	editurl:'targetGroup_update.action',
			   	colNames:[
						<s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
						</s:if>
						'<s:text name="%{getLocaleProperty('targetGroup.code')}"/>',
						'<s:text name="%{getLocaleProperty('targetGroup.name')}"/>',
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
			   		{name:'targetGroupName',index:'targetGroupName',width:125,sortable:true,editable:true},
			   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                        formatoptions: {
                            keys: true, 
                         
                            delOptions: { 
                            	url: 'targetGroup_delete.action',
                            	afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#detailTargetGroup").closest('div.ui-jqgrid')
                            	    });
                            	},
                            	
                              afterSubmit: function(data) 
                                {
                                	var json = JSON.parse(data.responseText);
									jQuery("#detailTargetGroup").jqGrid('setGridParam',{url:"targetGroup_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
                                }
			   		
                                
                                
                            },
                            
                            afterSave: function (id, response, options) {
                                var json = JSON.parse(response.responseText);
								jQuery("#detailTargetGroup").jqGrid('setGridParam',{url:"targetGroup_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
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
				},		 */
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
						//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
		       		
		        }
			});
			
			jQuery("#detailTargetGroup").jqGrid('navGrid','#pagerForDetailGroup',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detailTargetGroup").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#detailTargetGroup")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });
}


function loadMethodGrid()
{
	jQuery("#detailMethod").jqGrid(
			{
			   	url:'trainingMethod_data.action',
			   	pager: '#pagerForDetailMehod',	  
			   	datatype: "json",	
			   	mtype: 'POST',
			   	styleUI : 'Bootstrap',
				editurl:'trainingMethod_update.action',
			   	colNames:[
						<s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
						</s:if>
						'<s:text name="%{getLocaleProperty('trainingMethod.code')}"/>',
						'<s:text name="%{getLocaleProperty('trainingMethod.name')}"/>',
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
			   		//{name:'name',index:'name',width:125,sortable:true,editable:true},
			   		 {name:'methodName',index:'methodName',width:125,sortable:true,editable:true}, 
			   		
			   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                        formatoptions: {
                            keys: true, 
                         
                            delOptions: { 
                            	url: 'trainingMethod_delete.action',
                            	afterShowForm:function ($form) {
                            	    $form.closest('div.ui-jqdialog').position({
                            	        my: "center",
                            	        of: $("#detailMethod").closest('div.ui-jqgrid')
                            	    });
                            	},
                            	
                              afterSubmit: function(data) 
                                {
                                	var json = JSON.parse(data.responseText);
									jQuery("#detailMethod").jqGrid('setGridParam',{url:"trainingMethod_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
									showPopup(json.msg,json.title);
									jQuery('.ui-jqdialog-titlebar-close').click();
                                }
			   		
                                
                                
                            },
                            
                            afterSave: function (id, response, options) {
                                var json = JSON.parse(response.responseText);
								jQuery("#detailMethod").jqGrid('setGridParam',{url:"trainingMethod_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
								showPopup(json.msg,json.title);
								jQuery('.ui-jqdialog-titlebar-close').click();
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
			    /* onSelectRow: function(id){ 
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
						//$(".ui-inline-save span").html('<i class="fa fa-floppy-o fa-4 inline-gridSave" aria-hidden="true"></i>');
		       		
		        }
			});
			
			jQuery("#detailMethod").jqGrid('navGrid','#pagerForDetailMehod',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
			jQuery("#detailMethod").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

			colModel = jQuery("#detailMethod").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + $.jgrid.jqID(jQuery("#detailMethod")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		        }
		    });
		
}


	$(document).ready(function(){
		loadTopicGrid();
		loadObserveGrid();
		loadMaterialGrid();
		loadCriteriaCatGrid();
		loadCriteriaGrid();
		loadTargetGroupGrid();
		loadMethodGrid();
	}); 
	
	function addTopic()
	{
		var topicName=$("#name").val();
		$("#name").val('');
		if(topicName=="" || topicName==null)
		{
			document.getElementById("validateErrorTopic").innerHTML='<s:text name="empty.trainingTopics"/>';
		}
		else
		{
			
			$.post("trainingTopic_create",{name:topicName},function(result){
				resetTopic();
				jQuery("#detail").jqGrid('setGridParam',{url:"trainingTopic_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
				var strData=JSON.stringify(result);
				var validateMsg =jQuery.parseJSON(strData);
				showPopup(validateMsg.msg,validateMsg.title);
				
			});
		}
		
	}
	function addObserve(obj){
		var ObserveName=$("#observeName").val();
		$("#observeName").val('');
		if(ObserveName=="" || ObserveName==null)
		{
			document.getElementById("validateErrorObserve").innerHTML='<s:text name="empty.trainingObservation"/>';
		}
		else{
			   $.post("observations_populateObservationCreate",{observationName:ObserveName},function(result){
				jQuery("#detailObs").jqGrid('setGridParam',{url:"observations_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
				var validateMsg=JSON.parse(result);
				showPopup(validateMsg.msg,validateMsg.title);
				
			});
		}
		
	}
	function addMaterial(){
		var MaterialName=$("#materialName").val();
		$("#materialName").val('');
		if(MaterialName=="" || MaterialName==null)
		{
			document.getElementById("validateErrorMaterial").innerHTML='<s:text name="empty.materialName"/>';
		}
		else{
			   $.post("trainingMaterial_populateMaterialCreate",{materialsName:MaterialName},function(result){
				jQuery("#detailMat").jqGrid('setGridParam',{url:"trainingMaterial_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
				var validateMsg=JSON.parse(result);
				showPopup(validateMsg.msg,validateMsg.title);
				
			});
		}
	}
	
function resetTopic()
{
	$("#name").val('');
	document.getElementById("validateErrorTopic").innerHTML='';
}

function resetTopicCate()
{
	$("#topicCategory").val('');
	document.getElementById("validateErrorCritCat").innerHTML='';
}

function resetTargetGroup()
{
	
	$("#targetGroupName").val('');
	$("#methodName").val('');
	document.getElementById("validateErrorTarget").innerHTML='';
}

function resetCirteria()
{
	
	$("#principle").val('');
	$("#topicDes").val('');
	$("#topicCategoryId").val('');
	document.getElementById("validateErrorCritera").innerHTML='';
}


function addTopicCategory()
{
	var topicCategory=$("#topicCategory").val();
	$("#topicCategory").val('');
	if(topicCategory=="" || topicCategory==null)
	{
		document.getElementById("validateErrorCritCat").innerHTML='<s:text name="empty.topicCategoryName"/>';
	}
	else
	{
		
		$.post("trainingCriteriaCategory_populatecreate",{topicCategoryName:topicCategory},function(result){
			resetTopicCate();
			jQuery("#detailCat").jqGrid('setGridParam',{url:"trainingCriteriaCategory_populatedata.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			//var validateMsg=JSON.parse(result);
			//showPopup(validateMsg.msg,validateMsg.title);
			jQuery.post("topicCriteria_populateTopicCategoryList.action",function(result){
				insertOptions("topicCategoryId",JSON.parse(result));
				insertOptions("id",JSON.parse(result));
			});
			
		});
	}
}

function addCriteria()
{
	var principle=$("#principle").val();
	var topicDes=$("#topicDes").val();
	var topicCategoryId=$("#topicCategoryId").val();
	//$("#principle").val('');
	//$("#topicDes").val('');
	$("#topicCategoryId").val('');
	if(principle=="" || principle==null)
	{
		document.getElementById("validateErrorCritera").innerHTML='<s:text name="empty.principle"/>';
		
	}
	else if(topicDes=="" || topicDes==null)
	{
		document.getElementById("validateErrorCritera").innerHTML='<s:text name="empty.des"/>';
	}
	else if(topicCategoryId=="" || topicCategoryId==null)
	{
		document.getElementById("validateErrorCritera").innerHTML='<s:text name="empty.topicCategory"/>';
	}
	else
	{
		
		$.post("topicCriteria_populateCreate",{principle:principle,topicDes:topicDes,topicCategoryId:topicCategoryId},function(result){
			resetCirteria();
			jQuery("#detailCriteria").jqGrid('setGridParam',{url:"topicCriteria_populatedata.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			var validateMsg=JSON.parse(result);
			showPopup(validateMsg.msg,validateMsg.title);
			
		});
	}
	
}

function addTargetGroup()
{
	var targetGroup=$("#targetGroupName").val();
	$("#targetGroupName").val('');
	if(targetGroup=="" || targetGroup==null)
	{
		document.getElementById("validateErrorTarget").innerHTML='<s:text name="empty.methodName"/>';
	}
	else
	{
		
		$.post("targetGroup_create",{targetGroupName:targetGroup},function(result){
			resetTargetGroup();
			jQuery("#detailTargetGroup").jqGrid('setGridParam',{url:"targetGroup_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			var validateMsg=JSON.parse(result);
			showPopup(validateMsg.msg,validateMsg.title);
			
		});
	}
	
}

function addMethod()
{
	var methodName=$("#methodName").val();
	$("#methodName").val('');
	if(methodName=="" || methodName==null)
	{
		document.getElementById("validateErrorMethod").innerHTML='<s:text name="empty.methodName"/>';
	}
	else
	{
		
		$.post("trainingMethod_create",{methodName:methodName},function(result){
			resetTargetGroup();
			jQuery("#detailMethod").jqGrid('setGridParam',{url:"trainingMethod_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
			var validateMsg=JSON.parse(result);
			showPopup(validateMsg.msg,validateMsg.title);
			
		});
	}
	
}



</script>


<div id="baseDiv" style="width: 97%"></div>
<div class="appContentWrapper marginBottom">
	<ul class="nav nav-pills">
		<li class="active"><a data-toggle="pill" href="#topic"><s:property
					value="%{getLocaleProperty('trainingTopic')}" /></a></li>

		<li><a data-toggle="pill" href="#observation"><s:property
					value="%{getLocaleProperty('trainingObservation')}" /></a></li>
					
		<li><a data-toggle="pill" href="#material"><s:property
					value="%{getLocaleProperty('trainingMaterial')}" /></a></li>			

		<li><a data-toggle="pill" href="#criteriaCate"><s:property
					value="%{getLocaleProperty('topicCriteriaCategory')}" /></a></li>
					
		<li><a data-toggle="pill" href="#criteria"><s:property
					value="%{getLocaleProperty('topicCriteria')}" /></a></li>					
					

		<%-- <li><a data-toggle="pill" href="#targetGroup"><s:property
					value="%{getLocaleProperty('targetGroup')}" /></a></li> --%>

		<li><a data-toggle="pill" href="#method"><s:property
					value="%{getLocaleProperty('trainingMethod')}" /></a></li>
	</ul>
</div>


<div class="tab-content">
	<div id="topic" class="tab-pane fade in active">
		<s:form name="productForm" id="productForm">
			<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create">
				<a data-toggle="collapse" data-parent="#topicAccordian"
					href="#topicAccordian" class="btn btn-sts margin-bottom10px"> <s:property
					value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="topicAccordian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorTopic" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:property
					value="%{getLocaleProperty('trainingTopic.name')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="topicName" theme="simple" maxlength="45"
									id="name" />
								
							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
						<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addTopic()">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
							</div>	
					</div>
				</div>
			</div>
		</s:form>
		<div style="width: 99%;" id="baseDiv1">
			<div class="appContentWrapper ">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
			</div>
		</div>
	</div>
	
	<div id="observation" class="tab-pane fade ">
       <s:form name="observeForm" id="observeForm">
		<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create">
				<a data-toggle="collapse" data-parent="#observeAccordian"
					href="#observeAccordian" class="btn btn-sts margin-bottom10px">  <s:property
					value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="observeAccordian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorObserve" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text name="%{getLocaleProperty('trainingObserve.name')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="observeName" theme="simple" maxlength="45"
									id="observeName" />
								
							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
						<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addObserve(this)">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
							</div>	
					</div>
				</div>
			</div>
		</s:form>
		<div style="width: 99%;" id="baseDiv1">
			<div class="appContentWrapper ">
				<table id="detailObs"></table>
				<div id="pageForObserve"></div>
			</div>
		</div>
	</div>
	
	
	<div id="material" class="tab-pane fade ">
            <s:form name="materialForm" id="materialForm">
		<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create">
				<a data-toggle="collapse" data-parent="#materialAccordian"
					href="#materialAccordian" class="btn btn-sts margin-bottom10px">  <s:property
					value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="materialAccordian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorMaterial" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text name="%{getLocaleProperty('trainingMaterial.name')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="materialName" theme="simple" maxlength="45"
									id="materialName" />
								
							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
						<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addMaterial()">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
							</div>	
					</div>
				</div>
			</div>
		</s:form>
		<div style="width: 99%;" id="baseDiv1">
			<div class="appContentWrapper ">
				<table id="detailMat"></table>
				<div id="pagerForMaterial"></div>
			</div>
		</div>
	</div>
	
	<div id="criteriaCate" class="tab-pane fade ">
		<s:form name="criteriaCatForm" id="criteriaCatForm">
			<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create">
				<a data-toggle="collapse" data-parent="#criteriaCateAccdian"
					href="#criteriaCateAccdian" class="btn btn-sts margin-bottom10px">
					 <s:property value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="criteriaCateAccdian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorCritCat" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text
									name="%{getLocaleProperty('trainingCriteriaCategory.name')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="topicCategory" theme="simple" maxlength="45"
									id="topicCategory" />
								
							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addTopicCategory()">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
						</div>
					</div>
				</div>
			</div>
		</s:form>

		<div style="width: 99%;" id="baseDivCat">
			<div class="appContentWrapper ">
				<table id="detailCat"></table>
				<div id="pagerForDetailCategory"></div>
			</div>
		</div>
	</div>
	<div id="criteria" class="tab-pane fade">
		<s:form name="criteriaForm" id="criteriaForm">
			<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create"> 
				<a data-toggle="collapse" data-parent="#criteriaAccdian"
					href="#criteriaAccdian" class="btn btn-sts margin-bottom10px">
					 <s:property value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="criteriaAccdian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorCritera" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text
									name="%{getLocaleProperty('topiccriteriatopic.principle')}" /><sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="principle" theme="simple" maxlength="45"
									id="principle" />

							</div>
						</div>
						<div class="flexItem">
							<label for="txt"><s:text name="topic.des" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element ">
								<s:textarea name="topicDes" id="topicDes" theme="simple"
									maxlength="255" cols="15" rows="15" />
							</div>
						</div>
						<div class="flexItem">
							<label for="txt"><s:text name="%{getLocaleProperty('topic.topicCategory')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element flexdisplay">
								<s:select name="topicCategoryId" id="topicCategoryId"
									list="topicCategoryList" listKey="id" listValue="name"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
								
							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addCriteria()">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
						</div>
					</div>
				</div>
			</div>
		</s:form>

		<div style="width: 99%;" id="baseDivCriteria">
			<div class="appContentWrapper ">
				<table id="detailCriteria"></table>
			</div>
			<div id="pagerForDetailCriteria"></div>
		</div>
	</div>

	<div id="targetGroup" class="tab-pane fade">
		<s:form name="targetGroupForm" id="targetGroupForm">
			<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create">
				<a data-toggle="collapse" data-parent="#targetGroupAccdian"
					href="#targetGroupAccdian" class="btn btn-sts margin-bottom10px">
					 <s:property value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="targetGroupAccdian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorTarget" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text
									name="targetGroup.name" /><sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="targetGroupName" theme="simple" maxlength="45"
									id="targetGroupName" />
									

							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addTargetGroup()">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<div class="appContentWrapper marginBottom">
			<div style="width: 99%;" id="baseDivTarget">
				<table id="detailTargetGroup"></table>
				<div id="pagerForDetailGroup"></div>
			</div>
		</div>
	</div>
	<div id="method" class="tab-pane fade">
		<s:form name="methodForm" id="methodForm">
			<sec:authorize
				ifAllGranted="profile.trainingMaster.trainingTopic.create">
				<a data-toggle="collapse" data-parent="#methodAccdian"
					href="#methodAccdian" class="btn btn-sts margin-bottom10px">  <s:property
					value="%{getLocaleProperty('create.button')}" />
				</a>
			</sec:authorize>
			<div id="methodAccdian" class="panel-collapse collapse">
				<div class="appContentWrapper marginBottom">
					<span id="validateErrorMethod" style="color: red;"></span>
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text
									name="%{getLocaleProperty('trainingMethod.name')}" /><sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="methodName" theme="simple" maxlength="45"
									id="methodName" />
									

							</div>
						</div>
						<div class="flexItem" style="margin-top: 20px;">
							<button type="button" class="btn btn-sts "
									data-toggle="modal" onclick="addMethod()">
									<font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b>
									</font>
								</button>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<div class="appContentWrapper marginBottom">
			<div style="width: 99%;" id="baseDivMethod">
				<table id="detailMethod"></table>
				<div id="pagerForDetailMehod"></div>
			</div>
		</div>
	</div>
</div>

<s:form name="createform" action="trainingTopic_create">
</s:form>
<s:form name="updateform" action="trainingTopic_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>

