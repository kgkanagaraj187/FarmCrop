<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<%@ include file="/jsp/common/grid-assets.jsp"%>
	<%@ include file="/jsp/common/form-assets.jsp"%>


<script type="text/javascript">
	var survey='';
	$(document).ready(function(){
		
    	
    	
    	 
		jQuery("#detail").jqGrid(
		{
		   	url:'distributionBalance_data.action',
		   	editurl:'distributionBalance_populateUpdate.action',
		   	pager: '#pagerForDetail',	  
		   	datatype: "json",	
		   	mtype: 'POST',
		   	styleUI : 'Bootstrap',
		
		   	colNames:[
		   	      	 '<s:text name="distributionBalance.farmer"/>',
		  		      '<s:text name="distributionBalance.product"/>',
		  		      '<s:text name="distributionBalance.stock"/>',
		  		  	  '<s:text name="Actions"/>'
		      	 	 ],
		   	colModel:[	
				{name:'farmer',index:'farmer',width:125,sortable:true},
		   		{name:'product',index:'product',width:125,sortable:true},
		   		{name:'stock',index:'stock',width:125,sortable: false,editable:true,search:false},
		   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                    formatoptions: {
                        keys: true, 
                        delbutton:false,
                        afterSave: function (id, response, options) {
                            var json = JSON.parse(response.responseText);
							jQuery("#detail").jqGrid('setGridParam',{url:"distributionBalance_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
							showPopup(json.msg,json.title);
                        }
                    }
		   	 }
		   		
		   		],
		   	height: 301, 
		    width: $("#baseDiv").width(), // assign parent div width
		//    scrollOffset: 0,
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
	        	
	      		//$('#detail').setColProp('typez', { editoptions: { value: getCatalogues()} });
	      		
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
	    

	}); 
	

     function onSubmit(){
     	document.form.submit();
     }
     
     
     
     function buttonCancelCatalogueName(){
 		
 		document.getElementById("model-close-edu-btn").click();	
      }

 	function closeButton(){
 		refreshPopup();
 		refreshEduPopup();
 	}
 	
 	function addDistributionBalance(){
 		$(".cerror").empty();
 		
 		var state =$("#state").val();
 		var localities =$("#localities").val();
 		var cities =$("#cities").val();
 		var farmer =$("#farmer").val();
 		var product =$("#product").val();
 		var stock =$("#stock").val();
 		
 	
 		if(isEmpty(state)){
 			$(".cerror").text('<s:text name="empty.state"/>');
 			return "false";
 		}else if(isEmpty(localities)){
 			$(".cerror").text('<s:property
					value="%{getLocaleProperty('empty.localities')}" />');
 			return "false";
 		}else if(isEmpty(cities)){
 			$(".cerror").text('	<s:property
 					value="%{getLocaleProperty('empty.cities')}" />');
 			return "false";
 		}else if(isEmpty(farmer)){
 			$(".cerror").text('<s:text name="empty.farmer"/>');
 			return "false";
 		}else if(isEmpty(product)){
 			$(".cerror").text('<s:text name="empty.product"/>');
 			return "false";
 		}else if(isNaN(stock)==true|| stock<=0){
 			$(".cerror").text('<s:text name="invalid.stock"/>');
 			return "false";
 		}else if(isEmpty(stock)){
 			$(".cerror").text('<s:text name="empty.stock"/>');
 			return "false";
 		}
 		
 		var dataa = {
 				'farmerIdId' : farmer,
 				'product':product,
 				'stock':stock
		}
 		
 		$.ajax({
			 url:'distributionBalance_populateDistributionBalance.action',
		     type: 'post',
		     dataType: 'json',
		     data: dataa,
		     success: function(result) {
		    	 resetData("catForm");
		    	 showPopup(result.msg,result.title);
		    	 jQuery("#detail").jqGrid('setGridParam',{url:"distributionBalance_data.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
		     },
		     error: function(result) {
		    	 showPopup("Error");
		     }
		});
 		resetFields();
 	}
 	
 	function resetData(id){
 		$("#"+id)[0].reset();
 		$("#"+id).trigger("reset");
 		 $('.select2').select2();
 	}
 	
 	function resetFields(){
 		
 		var idsArray=['state','localities','cities','farmer','product'];
 		resetSelect2(idsArray);
 		jQuery("#stock").val("");
 		
 	}
 function listLocality(){
	
		var selectedState = $('#state').val();
 		jQuery.post("distributionBalance_populateLocality.action",{selectedState:selectedState},function(result){
 			insertOptions('localities',JSON.parse(result));
 		});
 	} 
 	
 	function listMunicipality(){
 		var locality = $('#localities').val();
 		jQuery.post("distributionBalance_populateCity.action",{selectedDistrict:locality},function(result){
 			insertOptions('cities', $.parseJSON(result));
 		});
 	}
 	
 	function listFarmer(){
 		var city = $('#cities').val();
 		jQuery.post("distributionBalance_populateFarmer.action",{selectedCity:city},function(result){
 			insertOptions('farmer', $.parseJSON(result));
 		});
 	}
 	
 	
</script>

	
		<div class="appContentWrapper marginBottom">
	
			<a data-toggle="collapse" data-parent="#accordion"
				href="#catalogueAccordian" id="addDistributionBalance" class="btn btn-sts marginBottom"> <s:text
					name="create.button" />
			</a>
			<div id="catalogueAccordian" class="panel-collapse collapse">
				<div class="formContainerWrapper">
					<h2>
						<s:property value="%{getLocaleProperty('info.distributionBalance')}" />
					</h2>
				</div>
				<div class="error cerror"></div>
				 <form name="catForm" id="catForm"> 
					<div class="flex-layout filterControls">
					
				 <div class="flexItem">
						<label for="state"><s:property
								value="%{getLocaleProperty('state')}" /></label>
						<div class="form-element">
							<s:select id="state" name="selectedState" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" list="statesList" listKey="key"
								onchange="listLocality()" 
								listValue="value" theme="simple"  />
						</div>
					</div> 
					
					<div class="flexItem">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('locality.name')}" />
						</label>
						<div class="form-element">
							<s:select cssClass="form-control  select2"
								name="selectedDistrict" id="localities" list="listLocalities"
								headerKey="" headerValue="%{getText('txt.select')}" Key="id"
								Value="name" theme="simple" onchange="listMunicipality()"/>
						</div>
					</div>
					
					<div class="flexItem">
								<label for="txt"><s:property
										value="%{getLocaleProperty('city.name')}" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:select name="selectedCity" id="cities" list="cities"
										Key="code" Value="name" headerKey=""
										headerValue="%{getText('txt.select')}" theme="simple"
										cssClass="form-control  select2" onchange="listFarmer()" />
								</div>
							</div>
					
						<div class="flexItem">
							<label for="txt"><s:text name="farmer" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="farmer" name="farmerIdId" list="farmerList"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
						
						<div class="flexItem">
							<label for="txt"><s:text name="product" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="product" name="product" list="productList"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" />
							</div>
						</div>

						<div class="flexItem">
							<label for="txt"><s:text name="stock" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="stock" theme="simple"
									cssClass="form-control input-sm" maxlength="8" id="stock" />
							</div>
						</div>
						
						<button type="button" class="btn btn-sts" id="add"
									onclick="addDistributionBalance()">
									<b><s:text name="save.button" /></b>
								</button>

						

					</div>
				 </form> 
			</div>
		

	</div>
	

	<div class="appContentWrapper marginBottom">
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>
	
<s:hidden name="id" />
	
</body>