<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<%@ include file="/jsp/common/grid-assets.jsp"%>
	<%@ include file="/jsp/common/form-assets.jsp"%>
	<script type="text/javascript">
	var survey='';
	$(document).ready(function(){
		$(".otrCatTypes").hide();
    	$("#rmvCatTypeBtn").hide();
    	var tenant='<s:property value="getCurrentTenantId()"/>';
    	
    	
    	
    	 
		jQuery("#detail").jqGrid(
		{
		   	url:'catalogue_data.action',
		   	editurl:'catalogue_populateUpdate.action',
		   	pager: '#pagerForDetail',	  
		   	datatype: "json",	
		   	mtype: 'POST',
		   	styleUI : 'Bootstrap',
		   	postData:{
				  "survey" : function(){
						  return "<%=request.getParameter("survey")%>";
				  }
			},
		   	colNames:[
					/* <s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					'<s:text name="app.subBranch"/>',
					</s:if> */
		  		      '<s:text name="catalogue.code"/>',
		  		      '<s:text name="catalogue.typez"/>',
		  		      '<s:text name="catalogue.name"/>',
		  		   
		  		    '<s:text name="status"/>',
		  			 <s:if test="survey=='' || survey!='1'"> 
		  		  '<s:text name="Actions"/>'
		  		  </s:if>
		      	 	 ],
		   	colModel:[	
				/* <s:if test='branchId==null'>
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
			   		</s:if> */
		   		{name:'code',index:'code',width:125,sortable:true},
		   		{name:'typez',index:'typez',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="typezFilterText"/>' },editable: true,edittype: "select", editrules:{required: true}},
		   		{name:'name',index:'name',width:125,sortable:true,editable:true},
		  
		   		{name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="%{getLocaleProperty('status1')}"/>;0:<s:text name="%{getLocaleProperty('status0')}"/>' },editable: true,edittype: "checkbox", editrules:{required: true},editoptions: {value:'<s:property value="statusFilter"/>'}},
		   	 <s:if test="survey=='' || survey!='1'"> 
		   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                    formatoptions: {
                        keys: true, 
                        delbutton:false,
                        afterSave: function (id, response, options) {
                            var json = JSON.parse(response.responseText);
							jQuery("#detail").jqGrid('setGridParam',{url:"catalogue_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
							showPopup(json.msg,json.title);
                        }
                    }
		   	  </s:if>
		   	 }
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
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
	        onSelectRow: function(id){ 
	        	 if(survey!='' && survey=="1"){
	  		  document.updateform.id.value  =id;
	            document.updateform.submit();    
	        	 }
	  		},	
	        loadComplete: function() {
	        	
	      		$('#detail').setColProp('typez', { editoptions: { value: getCatalogues()} });
	      		
	      		$(".ui-inline-save span").removeClass("glyphicon").removeClass("glyphicon-save");
	            $(".ui-inline-save span").addClass("fa").addClass("fa-save").addClass("inline-gridSave");
	            $(".ui-inline-cancel span").removeClass("glyphicon").removeClass("glyphicon-remove-circle");
	            $(".ui-inline-cancel span").addClass("fa").addClass("fa-close").addClass("inline-gridSave");
	        	 if(survey!='' && survey=="1"){
	           		 jQuery('td[aria-describedby*="detail_act"]').remove()
	           		 jQuery('#gsh_detail_act').remove();
	           		$('.jqgfirstrow').find('td:last-child').remove();
	           		 jQuery('#detail_act').remove();
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
	    
	    survey="<%=request.getParameter("survey")%>";
   	 if(survey!='' && survey=="1"){
   				 $(".breadCrumbNavigation").find('li:not(:first)').remove();
				
				$(".breadCrumbNavigation").append("<li><a href='catalogue_list.action?survey=1'> <s:text name='catalogue.survey'/> </a></li> ");
					
			$('#addCat').attr("href", "#");
   			$("#addCat").click(function(){
   			    createFormSubmit();
   			});
   			
   		 }
	}); 
	
	function getCatalogues(){
		//populateCatalougeMaster
		var cat = $.ajax({
			url: 'catalogue_populateCatalougeMaster.action',
			async: false, 
			type: 'post',
			success: function(data, result) {
			}
		}).responseText;
		return cat.replace("{","").replace("}","");
	}
	
	function getStatus(){
		var cat = $.ajax({
			url: 'catalogue_populateStatus.action',
			async: false, 
			type: 'post',
			success: function(data, result) {
			}
		}).responseText;
		return cat.replace("{","").replace("}","");
	}
	function createFormSubmit(){
		document.createform.survey = survey;
		document.createform.submit();
	}
	
	 function otrFarmCatTyz(val){
 		var val= $("#typez").val();
 			$("#typez").val(-1);
 	 		$(".otrCatTypes").show();
 	 		$("#addCatTypeBtn").hide();
 	 		$("#rmvCatTypeBtn").show();
 	 	
 	 }
     function removeOtrFarmCatTyz(){
     	$(".otrCatTypes").hide();
     	$("#typez").val(-1);
     	$("#rmvCatTypeBtn").hide();
     	$("#addCatTypeBtn").show();
     }
     
     $('#saveCatalogueNameBtn').click(function(e){
			//alert("inside save");
			
			
		 });
     function saveCatTypz() {
     	saveCatalogueType();
     }
     
     function onSubmit(){
     	document.form.submit();
     }
     
     
     
     function buttonCancelCatalogueName(){
 		//refreshPopup();
 		
 		document.getElementById("model-close-edu-btn").click();	
      }

 	function closeButton(){
 		refreshPopup();
 		refreshEduPopup();
 	}
 	
 	function addCatalogue(){
 		$(".cerror").empty();
 		var typez = getElementValueById("typez");
 		var catName = getElementValueById("catName");
 		var statusZ =$('input:radio[name="farmCatalogueStatus"]:checked').val();
 		if(isEmpty(typez)){
 			$(".cerror").text('<s:text name="empty.type"/>');
 			return "false";
 		}else if(isEmpty(catName)){
 			$(".cerror").text('<s:text name="empty.CatalogueName"/>');
 			return "false";
 		}else if(isEmpty(statusZ)){
 			$(".cerror").text('<s:text name="empty.status"/>');
 			return "false";
 		}
 		/* else if(!isEmpty(catName)){
 			 var alphabetsRegx = /^[a-zA-Z0-9 ]*$/;
 			 var result = alphabetsRegx.test(catName);
 			 if(!result){
 				$(".cerror").text('<s:text name="pattern.name"/>');
 				return "false";
 			 }		 
 		} */
 		
 		var dataa = {
 				'farmCatalogue.name':catName,
 				'farmCatalogue.typez':typez,
 				'farmCatalogue.status':statusZ
 				
		}
 	
 		$.ajax({
			 url:'catalogue_populateCreate.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 resetData("catForm");
		    	 showPopup(result.msg,result.title);
		    	 jQuery("#detail").jqGrid('setGridParam',{url:"catalogue_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		     },
		     error: function(result) {
		    	 showPopup("Catalogue Name already Exists","Error");
		     	 //window.location.href="catalogue_list.action";
		     }
		});
 		
 		
 	}
 	
 	function resetData(id){
 		$("#"+id)[0].reset();
 		$("#"+id).trigger("reset");
 		 $('.select2').select2();
 	}
</script>

	
	<div class="appContentWrapper marginBottom">
		<sec:authorize ifAllGranted="profile.catalogue.create">
			<a data-toggle="collapse" data-parent="#accordion"
				href="#catalogueAccordian" id="addCat" class="btn btn-sts marginBottom"> <s:text
					name="create.button" />
			</a>
			<div id="catalogueAccordian" class="panel-collapse collapse">
				<div class="formContainerWrapper">
					<h2>
						<s:property value="%{getLocaleProperty('info.catalogue')}" />
					</h2>
				</div>
				<div class="error cerror"></div>
				<form name="catForm" id="catForm">
					<div class="flex-layout filterControls">
						<div class="flexItem">
							<label for="txt"><s:text name="catalogue.typez" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="typez" name="farmCatalogue.typez" list="typezList"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
								<!-- <button type="button" id="rmvCatTypeBtn"
							onclick="removeOtrFarmCatTyz(this.value)">x</button> -->
							</div>
						</div>

						<div class="flexItem otrCatTypes">
							<label for="txt"><s:text name="otherCatalogue.typez" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="farmCatalogue.otherCatalogueType"
									id="othrTxtval" theme="simple" cssClass="form-control input-sm"
									maxlength="35" />
							</div>
						</div>

						<div class="flexItem">
							<label for="txt"><s:text name="catalogue.name" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="farmCatalogue.name" theme="simple"
									cssClass="form-control input-sm" maxlength="250" id="catName" />
							</div>
						</div>
						
						
						<div class="flexItem">
							<label for="txt"><s:text name="status" /><sup
								style="color: red;">*</sup></label>
							<div class="">
								<label class="radio-inline"> <input type="radio"
									value="1" name="farmCatalogueStatus">
								<s:text name="%{getLocaleProperty('status1')}" />
								</label> <label class="radio-inline"> <input type="radio"
									value="0" name="farmCatalogueStatus">
								<s:text name="%{getLocaleProperty('status0')}" />
								</label>

								<button type="button" class="btn btn-sts" id="add"
									onclick="addCatalogue()">
									<b><s:text name="save.button" /></b>
								</button>
							</div>
						</div>

					</div>
				</form>
			</div>
		</sec:authorize>

	</div>

	<div class="appContentWrapper marginBottom">
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>

	<s:form name="createform" action="catalogue_create">
	<s:hidden name="survey" id="survey"/>
	</s:form>

	<s:form name="updateform" action="catalogue_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
</body>