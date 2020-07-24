<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

<style>
</style>
</head>
<body>
	<script src="js/dynamicComponents.js?v=18.20"></script>
	<script type="text/javascript" src="js/jquery.qrcode.min.js"></script>
	<script>
		jQuery(document).ready(function(){
			var tenant='<s:property value="getCurrentTenantId()"/>';
			var branchId='<s:property value="getBranchId()"/>';
			var type= '<%=request.getParameter("type")%>';
			var id= '<%=request.getParameter("id")%>';
			var gsmaTenantId = "GSMA";
			//alert(type);
		
			 if(type=='2'){
					$(".breadCrumbNavigation").html('');
					$(".breadCrumbNavigation").append("<li><a href='#'>Profile</a></li>");
					//$(".breadCrumbNavigation").append("<li><a href='#'>IRP</a></li>");
					$(".breadCrumbNavigation").append("<li><a href='farmer_list.action?type=2'>IRP</a></li>");
					$(".type").val(type);
					$(".nav").parent().addClass("hide");
					$("#cancelform").prop("action","farmer_list.action?type=2");
				} 
			 hideFields();
				jQuery.post("farmer_populateHideFn.action", {type:type}, function(result) {
					
					var farmerFieldsArray = jQuery.parseJSON(result);
					$.each(farmerFieldsArray, function(index, value) {
						if(index=='activeFields'){
							$(value).each(function(k,v){
								if(v.id=='1'){
									showByEleName(v.name);
								}if(v.id=='2'){
									showByEleId(v.name);
								}if(v.id=='3'){
									showByEleClass(v.name);
								}if(v.id=='4'){
									$("."+v.name).removeClass("hide");
									
								}
							});
						}
						else if(index=='destroyFileds'){
							$(value).each(function(k,v){
								$("."+v.name).remove();
							});
						}
					});
					$(".farmerDynamicField").removeClass("hide");
					
				});
				
			
			
			callGrids();
			populateInsertComponent();
			
		
	    
			$("ul > li").removeClass("active");
			var tabIndex =<%if (request.getParameter("tabIndex") == null) {
				out.print("'#tabs-1'");
			} else {
				out.print("'" + request.getParameter("tabIndex") + "'");
			}%>;
		    var tabObj = $('a[href="' + tabIndex + '"]');
		    $(tabObj).closest("li").addClass('active');
		    $("div").removeClass("active in");
		    $(tabIndex).addClass('active in');
			
		    //below code to set tab as 2 when bread crumb value is sent.    
		    var tabSelected = getUrlParameter('tabValue');
		    if (tabSelected == "tabs-2"){
			    $(tabIndex).removeClass("active in");
			    $('#tabs-2').addClass('active in');
			    $(tabObj).closest("li").removeClass('active');
			    tabObj = $('a[href="#tabs-2"]');
			    $(tabObj).closest("li").addClass('active');
		    }
		    renderDynamicDetailFeilds();
		    if(tenant.toUpperCase() === gsmaTenantId){
		    makeQrCode();
		    }
		    
		    
		    
if('<s:property value="getBranchId()"/>'=='Individual'){
	        	
	    		$(".individual").removeClass("hide");
	    		
	    		jQuery(".nameLab").text("<s:property value="%{getLocaleProperty('farmer.nameInv')}"/>");
	    		jQuery(".adarNo").text("<s:property value="%{getLocaleProperty('adhaarInv')}"/>");
	    		jQuery(".dateName").text("<s:property value="%{getLocaleProperty('farmer.dateOfBirth')}"/>");
	    		jQuery(".telephone").text("<s:property value="%{getLocaleProperty('farmer.mobileno')}"/>");
	    		jQuery(".phNumber").text("<s:property value="%{getLocaleProperty('farmer.phNum')}"/>");
	    		jQuery(".addInfo").text("<s:property value="%{getLocaleProperty('farmer.address')}"/>");
	    		jQuery(".cnName").text("<s:property value="%{getLocaleProperty('country.nameInv')}"/>");	
	    		jQuery(".stName").text("<s:property value="%{getLocaleProperty('stNameInv')}"/>");
	    		jQuery(".locName").text("<s:property value="%{getLocaleProperty('locNameInv')}"/>");
	    		jQuery(".ctName").text("<s:property value="%{getLocaleProperty('ctNameInv')}"/>");
	    		jQuery(".gpName").text("<s:property value="%{getLocaleProperty('gpNameInv')}"/>");
	    		jQuery(".villName").text("<s:property value="%{getLocaleProperty('villNameInv')}"/>");
	    	 	
	    		jQuery(".lifAmt").text("<s:property value="%{getLocaleProperty('farmer.phNum')}"/>");
	    		jQuery(".hltAmt").text("<s:property value="%{getLocaleProperty('farmer.phNum')}"/>");
	    	}
	        
	    if('<s:property value="getBranchId()"/>'=='Corporate'){
	        	
	    	    jQuery(".adarNo").text("<s:property value="%{getLocaleProperty('adhaarCorp')}"/>");
	    		jQuery(".nameLab").text("<s:property value="%{getLocaleProperty('farmer.nameCorp')}"/>");
	    		jQuery(".cnName").text("<s:property value="%{getLocaleProperty('country.nameCorp')}"/>");
	    		jQuery(".dateName").text("<s:property value="%{getLocaleProperty('farmer.dateofcorp')}"/>");
	    		jQuery(".telephone").text("<s:property value="%{getLocaleProperty('farmer.mobilenoCorp')}"/>");
	    		jQuery(".addInfo").text("<s:property value="%{getLocaleProperty('farmer.addressCorp')}"/>");
	    		 jQuery(".stName").text("<s:property value="%{getLocaleProperty('stNameCorp')}"/>");
	    		jQuery(".locName").text("<s:property value="%{getLocaleProperty('locNameCorp')}"/>");
	    		jQuery(".ctName").text("<s:property value="%{getLocaleProperty('ctNameCorp')}"/>");
	    		jQuery(".gpName").text("<s:property value="%{getLocaleProperty('gpNameCorp')}"/>");
	    		jQuery(".villName").text("<s:property value="%{getLocaleProperty('villNameCorp')}"/>");
	    		
	    		jQuery(".lifAmt").text("<s:property value="%{getLocaleProperty('farmer.acresCroped')}"/>");
	    		jQuery(".hltAmt").text("<s:property value="%{getLocaleProperty('farmer.acresCroped')}"/>");
	    		
	    	
	      } 
		    
		    
		    
		    
		});
		
		

		function makeQrCode () {      
		    var elText = '<s:property value="farmer.farmerId" />';		   
		    $('#qrcode').qrcode(elText);
		}
		
		function hideFields(){
			 var app = $(".aPanel");
			 $(app).addClass("hide");
			 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
			 	 $(this).addClass("hide");
			 });
			  
			}
		
		function getUrlParameter(sParam) {
	        var sPageURL = decodeURIComponent(window.location.search.substring(1));
	        var sURLVariables = sPageURL.split('&');
	        var sParameterName;
	        var i;
	        for (i = 0; i < sURLVariables.length; i++) {
	        sParameterName = sURLVariables[i].split('=');
	        if (sParameterName[0] === sParam) {
	        return sParameterName[1] === undefined ? true : sParameterName[1];
	        }
	        }
	        }
		
		function callGrids(){
			try{
				loadFarmGrid();
				loadFamilyDetailGrid();
				loadFarmCropsGrid();
				loadFarmHistoryGrid();
			//	loadInspectionCheckGrid();
			if(tenant=="susagri"){
				loadFarmCertificationDetailsGrid();
			}	
			
			}catch(e){
				console.log(e);
			}
		}
		
		function hideByEleName(name){
			$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).addClass( "hide" );
		}

		function showByEleName(name){
			$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).removeClass( "hide" );
		}

		function hideByEleClass(className){
			$("."+className).closest( ".dynamic-flexItem2" ).addClass( "hide" );
		}

		function showByEleClass(className){
			$("."+className).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
			$("."+className).parent().removeClass( "hide" );
		}

		function hideByEleId(id){
			$("#"+id).closest( ".dynamic-flexItem2" ).addClass( "hide" );
		}

		function showByEleId(id){
			$("#"+id).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
		}
		
		
		function loadFarmGrid(){

		    jQuery("#detail1").jqGrid(
		    {
		    url:'farm_data.action?farmerId=' + document.getElementById('farmerId').value,
		            pager: '#pagerForDetail1',
		            mtype: 'POST',
		            datatype: "json",
		                   colNames:[
									<s:if test="currentTenantId=='awi'">
									'<s:property value="%{getLocaleProperty('farm.farmCode')}" />',
									</s:if>
									<s:else>
									'<s:property value="%{getLocaleProperty('farm.farmCode')}" />',
		                            </s:else>
									'<s:property value="%{getLocaleProperty('farm.farmName')}" />',
		                            //  '<s:text name="farm.regYear"/>',
		                            <s:if test="currentTenantId!='blri' && currentTenantId!='efk' && currentTenantId!='wilmar' && currentTenantId!='olivado' && currentTenantId!='griffith' && currentTenantId!='cofBoard' && currentTenantId!='gsma' && currentTenantId!='ecoagri'  && currentTenantId!='livelihood'&& currentTenantId!='symrise'">
		                            '<s:property value="%{getLocaleProperty('farm.surveyNumber')}"/>',
		                            </s:if>
		                          
		                            <s:if test="currentTenantId=='wilmar'">
		                            '<s:property value="%{getLocaleProperty('nameOfInspector')}"/>',
		                            '<s:property value="%{getLocaleProperty('dateOfInspection')}"/>',
		                            '<s:property value="%{getLocaleProperty('totalCoconutArea')}"/>',
		                            '<s:property value="%{getLocaleProperty('organicStatus')}"/>'
		                            </s:if>
		                            <s:if test="currentTenantId!='efk' && branchId =='organic'">
		                            '<s:property value="%{getLocaleProperty('organicStatus')}"/>',
		                            </s:if>
		                           <s:if test="currentTenantId=='griffith'">
		                            '<s:property value="%{getLocaleProperty('farm.farmDetailedInfo.totalLandHolding')}"/>',
		                            </s:if> 
		                         
		                            <s:if test="currentTenantId=='symrise'">
		                            '<s:property value="%{getLocaleProperty('certificate.level')}"/>'
		                            </s:if>
		                            //'<s:text name="farm.landInProduction"/>',
		                            // '<s:text name="farm.landNotInProduction"/>',
		                            //'<s:text name="farm.hectares"/>',
		                            // '<s:text name="farm.photo"/>'
		                            //'<s:text name="farm.map"/>'<sec:authorize ifAllGranted="profile.farmer.delete">,
		                    // '<s:text name="Action"/>'</sec:authorize>
		                            '<s:property value="%{getLocaleProperty('farm.plottingStatus')}"/>'
		            ],
		            colModel:[
					<s:if test="currentTenantId=='awi'">
						{name:'farmCode', index:'farmCode', width:125, sortable:true},
					</s:if>
					<s:else>
			            {name:'farmCode', index:'farmCode', width:125, sortable:true},
			        </s:else>
			            {name:'farmName', index:'farmName', width:125, sortable:true},
		                    //{name:'fdi.regYear',index:'fdi.regYear',width:125,sortable:true},
		                    <s:if test="currentTenantId!='blri' && currentTenantId!='efk' && currentTenantId!='symrise' && currentTenantId!='wilmar' && currentTenantId!='olivado' && currentTenantId!='griffith' && currentTenantId!='cofBoard' && currentTenantId!='gsma' && currentTenantId!='ecoagri' && currentTenantId!='livelihood'">
		                    {name:'fdi.surveyNumber', index:'fdi.surveyNumber', width:125, sortable:true},
		                    </s:if>
		                   
		                    <s:if test="currentTenantId=='wilmar'">
			                {name:'nameOfInspector', index:'nameOfInspector', width:125, sortable:false, search:false},
			                {name:'dateOfInspection', index:'dateOfInspection', width:125, sortable:false, search:false},
			                {name:'totalCoconutArea', index:'totalCoconutArea', width:175, sortable:false, search:false},
			                {name:'organicStatus', index:'organicStatus', width:125, sortable:false, search:false}
                            </s:if>
		                <s:if test="currentTenantId!='efk' && branchId=='organic' ">
		                {name:'organicStatus', index:'organicStatus', width:125, sortable:false, search:false},
		                </s:if>
		                <s:if test="currentTenantId=='griffith'">
		                {name:'farm.farmDetailedInfo.totalLandHolding', index:'farm.farmDetailedInfo.totalLandHolding', width:125, sortable:true},
		                </s:if>  
		                <s:if test="currentTenantId=='symrise'">
		                {name:'farm.farmDetailedInfo.totalLandHolding', index:'farm.certificateStandardLevel', width:125, sortable:true},
                        </s:if>
		            //{name:'f.firstName',index:'f.firstName',width:125,sortable:true},
		            //{name:'landInProduction',index:'landInProduction',width:125,sortable:true},
		            //{name:'landNotInProduction',index:'landNotInProduction',width:125,sortable:true},
		            //{name:'hectares',index:'hectares',width:125,sortable:true},
		                    //{name:'photo',index:'photo',width:125,sortable:false,search:false,align:'center'}
		                    //{name:'map',index:'map',width:125,sortable:false,search:false,align:'center'}<sec:authorize ifAllGranted="profile.farmer.delete">,
		            //{name:'action',index:'action',width:125,sortable:false,search:false,align:'center'}</sec:authorize>
		                {name:'plottingStatus', index:'plottingStatus', width:125, sortable:false, search:false}
		            ],
		            height: 301,
		            width: $("#baseDiv").width(), // assign parent div width
		            scrollOffset: 0,
		            rowNum:10,
		            rowList : [10, 25, 50],
		            sortname:'id',
		            sortorder: "desc",
		            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
		            beforeSelectRow:
		            function(rowid, e) {
		            var iCol = jQuery.jgrid.getCellIndex(e.target);
		            if (iCol >= 4){return false; }
		            else{ return true; }
		            },
		            onSelectRow: function(id){
		            	 document.farmdetailform.id.value = id;
		 	            document.farmdetailform.tabIndex.value = "#tabs-1";
		 	            document.farmdetailform.submit();
		            
		            },
		            onSortCol: function (index, idxcol, sortorder) {
		            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		            }
		    });
		    jQuery("#detail1").jqGrid('navGrid', '#pagerForDetail1', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
		            jQuery("#detail1").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

		    colModel = jQuery("#detail1").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail1")[0].id) +
		            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		    var cmi = colModel[i], colName = cmi.name;
		    if (cmi.sortable !== false) {
		    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		    }
		    });
		    windowHeight = windowHeight-100;
		    $('#detail1').jqGrid('setGridHeight',(windowHeight));
		    }
	function loadFamilyDetailGrid(){
		 jQuery("#familyDetail").jqGrid({
      		url:'farmerFamily_data.action?id='+ document.getElementById('farmerId').value,
              pager: '#familyPagerForDetail',
              mtype: 'POST',
              datatype: "json",
              colNames:[
					'<s:text name="Name"/>',
					'<s:text name="Gender"/>',
					'<s:text name="Age"/>',
					'<s:text name="Relationship"/>',
					'<s:text name="education"/>',
					'<s:text name="Disability"/>',
					'<s:text name="Marital"/>',
					'<s:text name="EducationStatus"/>',
                        ],
	                     colModel:[
								{name:'famly.name', index:'famly.name', width:125, sortable:true},
								{name:'famly.gender', index:'famly.gender', width:125, sortable:true,search:true,stype: 'select',searchoptions: { value: '<s:property value="genderText"/>' }},
								{name:'famly.age', index:'famly.age', width:125, search:true,sortable:true},
								{name:'famly.rel', index:'famly.rel', width:125, search:true,sortable:true,stype: 'select',searchoptions: { value: '<s:property value="relationText"/>' }},
								{name:'famly.edu', index:'famly.edu', width:125, search:true,sortable:true,stype: 'select',searchoptions: { value: '<s:property value="educationText"/>' }},
								{name:'famly.abi', index:'famly.abi', width:125, search:true,sortable:true,stype: 'select',searchoptions: { value: '<s:property value="disText"/>' }},
								{name:'famly.marital', index:'famly.marital', width:125, search:true,sortable:true,stype: 'select',searchoptions: { value: '<s:property value="marStText"/>' }},
								{name:'famly.eduStatus', index:'famly.eduStatus', width:125, search:true, sortable:true,stype: 'select',searchoptions: { value: '<s:property value="educationStatusText"/>' }},
	                               
		 ],
		 height: 301,
         width: $("#baseDiv").width(), // assign parent div width
         scrollOffset: 0,
         rowNum:10,
         rowList : [10, 25, 50],
         sortname:'id',
         sortorder: "desc",
         viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
         beforeSelectRow:
         function(rowid, e) {
         var iCol = jQuery.jgrid.getCellIndex(e.target);
         if (iCol >= 8){return false; }
         else{ return true; }
         },
         onSelectRow: function(id){

        	 document.familydetailform.id.value = id;
	            document.familydetailform.tabIndex.value = "#tabs-6";
	            document.familydetailform.submit();
         },
         onSortCol: function (index, idxcol, sortorder) {
         if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                 && this.p.colModel[this.p.lastsort].sortable !== false) {
         $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
         }
         }
 });
 jQuery("#familyDetail").jqGrid('navGrid', '#familyPagerForDetail', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
         jQuery("#familyDetail").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

 colModel = jQuery("#familyDetail").jqGrid('getGridParam', 'colModel');
 $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail1")[0].id) +
         ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
 var cmi = colModel[i], colName = cmi.name;
 if (cmi.sortable !== false) {
 $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
 } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
 $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
 }
 });
 windowHeight = windowHeight-100;
 $('#familyDetail').jqGrid('setGridHeight',(windowHeight));
}
		
		 function loadFarmCropsGrid(){
			 jQuery("#cropDetail").jqGrid({
		             		url:'farmCrops_data.action?id='+ document.getElementById('farmerId').value,
		                     pager: '#cropPagerForDetail',
		                     mtype: 'POST',
		                     datatype: "json",
		                     colNames:[
		                             //'<s:text name="farmcrops.code"/>',
		                             //'<s:text name="farmcrops.name"/>',
		                             '<s:text name="cropName"/>',
		                             '<s:property value="%{getLocaleProperty('variety')}" />',
		                             <s:if test="currentTenantId!='griffith'">
		                             '<s:text name="farmcrops.cropCategory.prop"/>',
		                             </s:if>
		                             '<s:property value="%{getLocaleProperty('farm.farmName')}" />',
		                             <s:if test="currentTenantId=='wilmar'"> 
		                             '<s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" />',
		          		  		       '<s:property value="%{getLocaleProperty('cultiAreas')}" />',
		          		  		   </s:if>
		          		  		       '<s:property value="%{getLocaleProperty('farmcrops.cropSeason')}" />',
		          		  	 		  
		                             <s:if test="cropInfoEnabled==1 && currentTenantId!='griffith'">
		          		  		     '<s:property value="%{getLocaleProperty('farmcrops.estimatedYeild')}" />',
		                             </s:if>
		                             <%--<sec:authorize ifAllGranted="profile.farmCrop.delete">,
		'<s:text name="Action"/>'</sec:authorize>--%>
		
		                     ],
		                     colModel:[
		                             //{name:'fcm.code',index:'fcm.code', width:125,sortable:true},
		                                     //{name:'fcm.name',index:'fcm.name', width:125,sortable:true},
		                                     {name:'pc.name', index:'pc.name', width:125, sortable:true},
		                             {name:'pv.name', index:'pv.name', width:125, sortable:true},
		                             <s:if test="currentTenantId!='griffith'">
		                             {name:'type', index:'type', width:125, sortable:false, search:true, stype: 'select', searchoptions: { value: '<s:property value="cropTypeFilter"/>' }},
		                             </s:if>
		                             {name:'farmName', index:'farmName', width:125, sortable:true},
		                             <s:if test="currentTenantId=='wilmar'"> 
		                		   		{name:'farmfarmcrops.estimatedYeild.tonnes',index:'farmfarmcrops.estimatedYeild.tonnes', width:125,sortable:true,search:false},
		                		   		{name:'cultiArea',index:'cultiArea', width:200,sortable:true,search:false},
		                		   		</s:if>
		                		   	 <s:if test="currentTenantId=='wilmar'"> 
		                		      	{name:'farmcrops.cropSeason',index:'farmcrops.cropSeason', width:125,sortable:true,search:true, stype: 'select', searchoptions: { value: '<s:property value="seasonTypeFilter"/>' }},
		                		      	</s:if>
		                		      	<s:else>
		                		      	{name:'farmcrops.cropSeason',index:'farmcrops.cropSeason', width:125,sortable:true,search:true, stype: 'select', search:false},
		                		      	</s:else>
		                		      	
		                             <s:if test="cropInfoEnabled==1 && currentTenantId!='griffith'">
		                             {name:'productionPerYear', index:'productionPerYear', width:125, sortable:true, search:false}
		                             </s:if>
		                             <%--<sec:authorize ifAllGranted="profile.farmCrop.delete">,
		{name:'action',index:'action',width:125,sortable:false,search:false}</sec:authorize>--%>
		                             ],
		                                     height: 301,
		                                     width: $("#baseDiv").width(),
		                                     scrollOffset: 0,
		                                     rowNum:10,
		                                     rowList : [10, 25, 50],
		                                     sortname:'id',
		                                     sortorder: "desc",
		                                     viewrecords: true,
		                                     beforeSelectRow:
		                                     function(rowid, e) {
		                                     var iCol = jQuery.jgrid.getCellIndex(e.target);
		                                     if (iCol >= 7){return false; }
		                                     else{ return true; }
		                                     },
		                                     onSelectRow: function(id){
		                                     document.detailfarmCrops.id.value = id;
		                                     document.detailfarmCrops.submit();
		                                     document.detailfarmCrops.tabIndex.value = tabIndex;
		                                     },
		                                     onSortCol: function (index, idxcol, sortorder) {
		                                     if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                                             && this.p.colModel[this.p.lastsort].sortable !== false) {
		                                     $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		                                     }
		                                     }
		                             });
			  
				                     jQuery("#cropDetail").jqGrid('navGrid', '#cropPagerForDetail', {edit:false, add:false, del:false, search:false, refresh:true}); // enabled refresh for reloading grid
				                     jQuery("#cropDetail").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.
				                    
				                     colModel = jQuery("#cropPagerForDetail").jqGrid('getGridParam', 'colModel');
				                     $('#gbox_' + $.jgrid.jqID(jQuery("#cropDetail")[0].id) +
				                             ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				                    /*  var cmi = colModel[i], colName = cmi.name;
				                     if (cmi.sortable !== false) {
				                     $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				                     } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				                     $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				                     } */
				                     });
				                     $('#cropDetail').jqGrid('setGridHeight',(windowHeight));
				                   
		                     }

			function loadFarmHistoryGrid(){

			    jQuery("#farmHistoryDetail").jqGrid(
			    {
			    url:'farmHistory_data.action?id='+ document.getElementById('farmerId').value,
			            pager: '#farmHistoryPagerForDetail',
			            mtype: 'POST',
			            datatype: "json",
			                   colNames:[
										<s:if test="currentTenantId=='awi'">
											'<s:text name="year"/>',
										</s:if>
										<s:else>
			                            	'<s:text name="year"/>',
			                            </s:else>
										'<s:property value="%{getLocaleProperty('crops')}" />',
										
										'<s:property value="%{getLocaleProperty('farmerLandDetails.seedlings')}" />',
										'<s:property value="%{getLocaleProperty('estimatedAcreage')}" />',
										'<s:property value="%{getLocaleProperty('farmerLandDetails.noOfTrees')}" />',
										'<s:property value="%{getLocaleProperty('pestdiseases')}" />',
										'<s:property value="%{getLocaleProperty('pestdiseasesControl')}" />',
     
			            ],
			            colModel:[
						<s:if test="currentTenantId=='awi'">
							{name:'year', index:'year', width:125, sortable:true,search:true},
						</s:if>
						<s:else>
				            {name:'year', index:'year', width:125, sortable:true,search:true},
				        </s:else>
				            {name:'crops', index:'crops', width:125, sortable:true,search:true},
				            {name:'seedlings', index:'seedlings', width:125, sortable:true,search:true},
				            {name:'estimatedAcreage', index:'estimatedAcreage', width:125, sortable:true,search:true},
				            {name:'noOfTrees', index:'noOfTrees', width:125, sortable:true,search:true},
				            {name:'pestdiseases', index:'pestdiseases', width:125, sortable:true,search:true},
				            {name:'pestdiseasesControl', index:'pestdiseasesControl', width:125, sortable:true,search:true}
			                    //{name:'fdi.regYear',index:'fdi.regYear',width:125,sortable:true},
			            ],
			            height: 301,
			            width: $("#baseDiv").width(), // assign parent div width
			            scrollOffset: 0,
			            rowNum:10,
			            rowList : [10, 25, 50],
			            sortname:'id',
			            sortorder: "desc",
			            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			            beforeSelectRow:
			            function(rowid, e) {
			            var iCol = jQuery.jgrid.getCellIndex(e.target);
			            if (iCol >= 4){return false; }
			            else{ return true; }
			            },
			            onSelectRow: function(id){
			            	document.farmHistorydetailform.id.value = id;
			 	            document.farmHistorydetailform.tabIndex.value = "#tabs-12";
			 	            document.farmHistorydetailform.submit();
			            
			            },
			            onSortCol: function (index, idxcol, sortorder) {
			            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
			                    && this.p.colModel[this.p.lastsort].sortable !== false) {
			            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
			            }
			            }
			    });
			    jQuery("#farmHistoryDetail").jqGrid('navGrid', '#farmHistoryPagerForDetail', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
			            jQuery("#farmHistoryDetail").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

			    colModel = jQuery("#farmHistoryDetail").jqGrid('getGridParam', 'colModel');
			    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#farmHistoryDetail")[0].id) +
			            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
			    var cmi = colModel[i], colName = cmi.name;
			    if (cmi.sortable !== false) {
			    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
			    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
			    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
			    }
			    });
			    windowHeight = windowHeight-100;
			    $('#farmHistoryDetail').jqGrid('setGridHeight',(windowHeight));
			    }
			function loadFarmCertificationDetailsGrid(){

			    jQuery("#detail15").jqGrid(
			    {
			    url:'certificationDetails_detail.action?farmerId='+document.getElementById('farmerId').value,
			            pager: '#pagerForDetail15',
			            mtype: 'POST',
			            datatype: "json",
			        	colNames:[
					   		'<s:property value="%{getLocaleProperty('Farm Name')}" />',
					   		'<s:property value="%{getLocaleProperty('Season')}" />',
					   		'<s:property value="%{getLocaleProperty('Inspection (Inspection 1 / Inspection 2)')}" />',
					   		'<s:property value="%{getLocaleProperty('Certification Type')}" />',
					   		'<s:property value="%{getLocaleProperty('Certification Status')}" />',
					   		'<s:property value="%{getLocaleProperty('Inspector Name')}" />',
					   		'<s:property value="%{getLocaleProperty('Inspection Date')}" />',
					   		'<s:property value="%{getLocaleProperty('Nc Raised')}" />',
	                      	 ],
			 	      	colModel:[
			 	      		{name:'farmName',index:'farmName',sortable:false,frozen:false},
			 	      		{name:'season',index:'season',sortable:false,frozen:false},
			 	      		{name:'inspType',index:'inspType',sortable:false,frozen:false},
			 	      		{name:'certType',index:'certType',sortable:false,frozen:false},
			 	      		{name:'icStatus',index:'icStatus',sortable:false,frozen:false},
			 	      		{name:'inspName',index:'inspName',sortable:false,frozen:false},
			 	      		{name:'inspDate',index:'inspDate',sortable:false,frozen:false},
			 	      		{name:'ncRaised',index:'ncRaised',sortable:false,frozen:false},
										],
			            height: 301,
			            width: $("#baseDiv").width(), // assign parent div width
			            scrollOffset: 0,
			            rowNum:10,
			            rowList : [10, 25, 50],
			            sortname:'id',
			            sortorder: "desc",
			            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			            beforeSelectRow:
			            function(rowid, e) {
			            var iCol = jQuery.jgrid.getCellIndex(e.target);
			            if (iCol >= 4){return false; }
			            else{ return true; }
			            },
			            /* onSelectRow: function(id){
			            	 document.certificationDetailsform.id.value = id;
			 	            document.certificationDetailsform.tabIndex.value = "#tabs-15";
			 	            document.certificationDetailsform.submit();
			            
			            }, */
			            onSortCol: function (index, idxcol, sortorder) {
			            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
			                    && this.p.colModel[this.p.lastsort].sortable !== false) {
			            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
			            }
			            }
			    });
			    jQuery("#detail15").jqGrid('navGrid', '#pagerForDetail15', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
			            jQuery("#detail15").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

			    colModel = jQuery("#detail15").jqGrid('getGridParam', 'colModel');
			    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail15")[0].id) +
			            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
			    var cmi = colModel[i], colName = cmi.name;
			    if (cmi.sortable !== false) {
			    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
			    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
			    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
			    }
			    });
			  //  windowHeight = windowHeight-100;
			   // $('#detail15').jqGrid('setGridHeight',(windowHeight));
			    }
                     function addfarmCrop()
                     {
                     	document.cropCreateform.submit();
                     }
                     
                     function addfamilyDetail(){
                    	 document.familyDetailCreateform.submit();
                     }
                     function addfarmHistory(){
                    	 document.farmHistoryCreateform.submit();
                     }
                     
                     function populateInsertComponent()
                     {
                     	 jQuery.post("farmer_populateDynamicInsertField.action", {}, function(result) {
                     		 var jsonData = jQuery.parseJSON(result);
                     			
                     		 $(jsonData).each(function(k, v) {
                				 var id = "#"+(v.componentName);
                				 if(!isEmpty(v.afterInsert)){
                					 var insertEle = $('[name="'+v.afterInsert+'"]').parent();
                					 var compo =  jQuery(id).parent();
                					 console.log(compo);
                					 $(compo).insertAfter(insertEle);
                				 }
                				 if(!isEmpty(v.beforeInsert)){
                					 var insertEle = $('[name="'+v.beforeInsert+'"]').parent();
                					 console.log(insertEle);
                					 var compo =  jQuery(id).parent();
                					 console.log(compo);
                					 $(compo).insertBefore(insertEle);
                				 }
                			 });
                     		
                     	  });
                     }
                     
                     function getTxnType(){
                    		//return "308";
                    	 var type= '<%=request.getParameter("type")%>';
                    	 /* if(type=='2'){
                    		 return "3008A";
                    	 } */
                    	return "308";
                    	}
                     
                     function getBranchIdDyn(){
                    		return '<s:property value="farmer.branchId"/>';
                    	}

		
	</script>

	<!--begin::Nav Tabs-->
	<ul
		class="dashboard-tabs nav nav-pills nav-danger row row-paddingless m-0 p-0"
		role="tablist">
		<!--begin::Item-->
		<li
			class="nav-item d-flex col flex-grow-1 flex-shrink-0 mr-3 mb-3 mb-lg-0">
			<a
			class="nav-link active  border py-10 d-flex flex-grow-1 rounded flex-column align-items-center"
			data-toggle="pill"
			href="https://preview.keenthemes.com/metronic/demo3/features/widgets/nav-panels.html#tab_forms_widget_1">
				<span class="nav-icon py-2 w-auto"> <span
					class="svg-icon svg-icon-3x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo3/dist/assets/media/svg/icons/Home/Library.svg-->
						<svg xmlns="http://www.w3.org/2000/svg"
							xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
							height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
								fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<path
								d="M5,3 L6,3 C6.55228475,3 7,3.44771525 7,4 L7,20 C7,20.5522847 6.55228475,21 6,21 L5,21 C4.44771525,21 4,20.5522847 4,20 L4,4 C4,3.44771525 4.44771525,3 5,3 Z M10,3 L11,3 C11.5522847,3 12,3.44771525 12,4 L12,20 C12,20.5522847 11.5522847,21 11,21 L10,21 C9.44771525,21 9,20.5522847 9,20 L9,4 C9,3.44771525 9.44771525,3 10,3 Z"
								fill="#000000"></path>
																			<rect fill="#000000" opacity="0.3"
								transform="translate(17.825568, 11.945519) rotate(-19.000000) translate(-17.825568, -11.945519)"
								x="16.3255682" y="2.94551858" width="3" height="18" rx="1"></rect>
																		</g>
																	</svg> <!--end::Svg Icon-->
				</span>
			</span> <span
				class="nav-text font-size-lg py-2 font-weight-bold text-center">Farmer
					Details</span>
		</a>
		</li>
		<!--end::Item-->
		<!--begin::Item-->
		<li
			class="nav-item d-flex col flex-grow-1 flex-shrink-0 mr-3 mb-3 mb-lg-0">
			<a
			class="nav-link border py-10 d-flex flex-grow-1 rounded flex-column align-items-center"
			data-toggle="pill"
			href="https://preview.keenthemes.com/metronic/demo3/features/widgets/nav-panels.html#tab_forms_widget_2">
				<span class="nav-icon py-2 w-auto"> <span
					class="svg-icon svg-icon-3x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo3/dist/assets/media/svg/icons/Layout/Layout-4-blocks.svg-->
						<svg xmlns="http://www.w3.org/2000/svg"
							xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
							height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
								fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<rect fill="#000000" x="4" y="4" width="7" height="7"
								rx="1.5"></rect>
																			<path
								d="M5.5,13 L9.5,13 C10.3284271,13 11,13.6715729 11,14.5 L11,18.5 C11,19.3284271 10.3284271,20 9.5,20 L5.5,20 C4.67157288,20 4,19.3284271 4,18.5 L4,14.5 C4,13.6715729 4.67157288,13 5.5,13 Z M14.5,4 L18.5,4 C19.3284271,4 20,4.67157288 20,5.5 L20,9.5 C20,10.3284271 19.3284271,11 18.5,11 L14.5,11 C13.6715729,11 13,10.3284271 13,9.5 L13,5.5 C13,4.67157288 13.6715729,4 14.5,4 Z M14.5,13 L18.5,13 C19.3284271,13 20,13.6715729 20,14.5 L20,18.5 C20,19.3284271 19.3284271,20 18.5,20 L14.5,20 C13.6715729,20 13,19.3284271 13,18.5 L13,14.5 C13,13.6715729 13.6715729,13 14.5,13 Z"
								fill="#000000" opacity="0.3"></path>
																		</g>
																	</svg> <!--end::Svg Icon-->
				</span>
			</span> <span
				class="nav-text font-size-lg py-2 font-weight-bolder text-center">Farm
					Details</span>
		</a>
		</li>
		<!--end::Item-->
		<!--begin::Item-->
		<li
			class="nav-item d-flex col flex-grow-1 flex-shrink-0 mr-3 mb-3 mb-lg-0">
			<a
			class="nav-link border py-10 d-flex flex-grow-1 rounded flex-column align-items-center"
			data-toggle="pill"
			href="https://preview.keenthemes.com/metronic/demo3/features/widgets/nav-panels.html#tab_forms_widget_3">
				<span class="nav-icon py-2 w-auto"> <span
					class="svg-icon svg-icon-3x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo3/dist/assets/media/svg/icons/Media/Movie-Lane2.svg-->
						<svg xmlns="http://www.w3.org/2000/svg"
							xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
							height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
								fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<path
								d="M6,3 L18,3 C19.1045695,3 20,3.8954305 20,5 L20,19 C20,20.1045695 19.1045695,21 18,21 L6,21 C4.8954305,21 4,20.1045695 4,19 L4,5 C4,3.8954305 4.8954305,3 6,3 Z M5.5,5 C5.22385763,5 5,5.22385763 5,5.5 L5,6.5 C5,6.77614237 5.22385763,7 5.5,7 L6.5,7 C6.77614237,7 7,6.77614237 7,6.5 L7,5.5 C7,5.22385763 6.77614237,5 6.5,5 L5.5,5 Z M17.5,5 C17.2238576,5 17,5.22385763 17,5.5 L17,6.5 C17,6.77614237 17.2238576,7 17.5,7 L18.5,7 C18.7761424,7 19,6.77614237 19,6.5 L19,5.5 C19,5.22385763 18.7761424,5 18.5,5 L17.5,5 Z M5.5,9 C5.22385763,9 5,9.22385763 5,9.5 L5,10.5 C5,10.7761424 5.22385763,11 5.5,11 L6.5,11 C6.77614237,11 7,10.7761424 7,10.5 L7,9.5 C7,9.22385763 6.77614237,9 6.5,9 L5.5,9 Z M17.5,9 C17.2238576,9 17,9.22385763 17,9.5 L17,10.5 C17,10.7761424 17.2238576,11 17.5,11 L18.5,11 C18.7761424,11 19,10.7761424 19,10.5 L19,9.5 C19,9.22385763 18.7761424,9 18.5,9 L17.5,9 Z M5.5,13 C5.22385763,13 5,13.2238576 5,13.5 L5,14.5 C5,14.7761424 5.22385763,15 5.5,15 L6.5,15 C6.77614237,15 7,14.7761424 7,14.5 L7,13.5 C7,13.2238576 6.77614237,13 6.5,13 L5.5,13 Z M17.5,13 C17.2238576,13 17,13.2238576 17,13.5 L17,14.5 C17,14.7761424 17.2238576,15 17.5,15 L18.5,15 C18.7761424,15 19,14.7761424 19,14.5 L19,13.5 C19,13.2238576 18.7761424,13 18.5,13 L17.5,13 Z M17.5,17 C17.2238576,17 17,17.2238576 17,17.5 L17,18.5 C17,18.7761424 17.2238576,19 17.5,19 L18.5,19 C18.7761424,19 19,18.7761424 19,18.5 L19,17.5 C19,17.2238576 18.7761424,17 18.5,17 L17.5,17 Z M5.5,17 C5.22385763,17 5,17.2238576 5,17.5 L5,18.5 C5,18.7761424 5.22385763,19 5.5,19 L6.5,19 C6.77614237,19 7,18.7761424 7,18.5 L7,17.5 C7,17.2238576 6.77614237,17 6.5,17 L5.5,17 Z"
								fill="#000000" opacity="0.3"></path>
																			<path
								d="M11.3521577,14.5722612 L13.9568442,12.7918113 C14.1848159,12.6359797 14.2432972,12.3248456 14.0874656,12.0968739 C14.0526941,12.0460053 14.0088196,12.002002 13.9580532,11.9670814 L11.3533667,10.1754041 C11.1258528,10.0189048 10.8145486,10.0764735 10.6580493,10.3039875 C10.6007019,10.3873574 10.5699997,10.4861652 10.5699997,10.5873545 L10.5699997,14.1594818 C10.5699997,14.4356241 10.7938573,14.6594818 11.0699997,14.6594818 C11.1706891,14.6594818 11.2690327,14.6290818 11.3521577,14.5722612 Z"
								fill="#000000"></path>
																		</g>
																	</svg> <!--end::Svg Icon-->
				</span>
			</span> <span
				class="nav-text font-size-lg py-2 font-weight-bolder text-center">Sowing
					Details</span>
		</a>
		</li>
		<!--end::Item-->
		<!--begin::Item-->
		<li
			class="nav-item d-flex col flex-grow-1 flex-shrink-0 mr-3 mb-3 mb-lg-0">
			<a
			class="nav-link border py-10 d-flex flex-grow-1 rounded flex-column align-items-center"
			data-toggle="pill"
			href="https://preview.keenthemes.com/metronic/demo3/features/widgets/nav-panels.html#tab_forms_widget_4">
				<span class="nav-icon py-2 w-auto"> <span
					class="svg-icon svg-icon-3x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo3/dist/assets/media/svg/icons/Media/Equalizer.svg-->
						<svg xmlns="http://www.w3.org/2000/svg"
							xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
							height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
								fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<rect fill="#000000" opacity="0.3" x="13" y="4"
								width="3" height="16" rx="1.5"></rect>
																			<rect fill="#000000" x="8" y="9" width="3"
								height="11" rx="1.5"></rect>
																			<rect fill="#000000" x="18" y="11" width="3"
								height="9" rx="1.5"></rect>
																			<rect fill="#000000" x="3" y="13" width="3"
								height="7" rx="1.5"></rect>
																		</g>
																	</svg> <!--end::Svg Icon-->
				</span>
			</span> <span
				class="nav-text font-size-lg py-2 font-weight-bolder text-center">Animal
					Husbandry</span>
		</a>
		</li>
		<!--end::Item-->
		<!--begin::Item-->
		<li class="nav-item d-flex col flex-grow-1 flex-shrink-0 mr-3"><a
			class="nav-link border py-10 d-flex flex-grow-1 rounded flex-column align-items-center"
			data-toggle="pill"
			href="https://preview.keenthemes.com/metronic/demo3/features/widgets/nav-panels.html#tab_forms_widget_5">
				<span class="nav-icon py-2 w-auto"> <span
					class="svg-icon svg-icon-3x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo3/dist/assets/media/svg/icons/General/Shield-check.svg-->
						<svg xmlns="http://www.w3.org/2000/svg"
							xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
							height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
								fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<path
								d="M4,4 L11.6314229,2.5691082 C11.8750185,2.52343403 12.1249815,2.52343403 12.3685771,2.5691082 L20,4 L20,13.2830094 C20,16.2173861 18.4883464,18.9447835 16,20.5 L12.5299989,22.6687507 C12.2057287,22.8714196 11.7942713,22.8714196 11.4700011,22.6687507 L8,20.5 C5.51165358,18.9447835 4,16.2173861 4,13.2830094 L4,4 Z"
								fill="#000000" opacity="0.3"></path>
																			<path
								d="M11.1750002,14.75 C10.9354169,14.75 10.6958335,14.6541667 10.5041669,14.4625 L8.58750019,12.5458333 C8.20416686,12.1625 8.20416686,11.5875 8.58750019,11.2041667 C8.97083352,10.8208333 9.59375019,10.8208333 9.92916686,11.2041667 L11.1750002,12.45 L14.3375002,9.2875 C14.7208335,8.90416667 15.2958335,8.90416667 15.6791669,9.2875 C16.0625002,9.67083333 16.0625002,10.2458333 15.6791669,10.6291667 L11.8458335,14.4625 C11.6541669,14.6541667 11.4145835,14.75 11.1750002,14.75 Z"
								fill="#000000"></path>
																		</g>
																	</svg> <!--end::Svg Icon-->
				</span>
			</span> <span
				class="nav-text font-size-lg py-2 font-weight-bolder text-center">Farm
					Equipment</span>
		</a></li>
		<!--end::Item-->
		<!--begin::Item-->
		<li
			class="nav-item d-flex col flex-grow-1 flex-shrink-0 mr-0 mb-3 mb-lg-0">
			<a
			class="nav-link border py-10 d-flex flex-grow-1 rounded flex-column align-items-center"
			data-toggle="pill"
			href="https://preview.keenthemes.com/metronic/demo3/features/widgets/nav-panels.html#tab_forms_widget_5">
				<span class="nav-icon py-2 w-auto"> <span
					class="svg-icon svg-icon-3x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo3/dist/assets/media/svg/icons/Communication/Group.svg-->
						<svg xmlns="http://www.w3.org/2000/svg"
							xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
							height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
								fill-rule="evenodd">
																			<polygon points="0 0 24 0 24 24 0 24"></polygon>
																			<path
								d="M18,14 C16.3431458,14 15,12.6568542 15,11 C15,9.34314575 16.3431458,8 18,8 C19.6568542,8 21,9.34314575 21,11 C21,12.6568542 19.6568542,14 18,14 Z M9,11 C6.790861,11 5,9.209139 5,7 C5,4.790861 6.790861,3 9,3 C11.209139,3 13,4.790861 13,7 C13,9.209139 11.209139,11 9,11 Z"
								fill="#000000" fill-rule="nonzero" opacity="0.3"></path>
																			<path
								d="M17.6011961,15.0006174 C21.0077043,15.0378534 23.7891749,16.7601418 23.9984937,20.4 C24.0069246,20.5466056 23.9984937,21 23.4559499,21 L19.6,21 C19.6,18.7490654 18.8562935,16.6718327 17.6011961,15.0006174 Z M0.00065168429,20.1992055 C0.388258525,15.4265159 4.26191235,13 8.98334134,13 C13.7712164,13 17.7048837,15.2931929 17.9979143,20.2 C18.0095879,20.3954741 17.9979143,21 17.2466999,21 C13.541124,21 8.03472472,21 0.727502227,21 C0.476712155,21 -0.0204617505,20.45918 0.00065168429,20.1992055 Z"
								fill="#000000" fill-rule="nonzero"></path>
																		</g>
																	</svg> <!--end::Svg Icon-->
				</span>
			</span> <span
				class="nav-text font-size-lg py-2 font-weight-bolder text-center">Farmer
					Summary</span>
		</a>
		</li>
		<!--end::Item-->
	</ul>
	<!--end::Nav Tabs-->


	<script>
	var tenant='<s:property value="getCurrentTenantId()"/>';
		$(document).ready(function() {
		
							$('#update').on(
											'click',
											function(e) {
												document.updatfrm.id.value = document.getElementById('farmerId').value;
												document.updatfrm.currentPage.value = document.form.currentPage.value;
												document.updatfrm.submit();
											});
							$('#delete')
									.on(
											'click',
											function(e) {
												if (confirm('<s:text name="confirm.deleteFarmer"/> ')) {
													document.deleteform.id.value = document
															.getElementById('farmerId').value;
													document.deleteform.currentPage.value = document.form.currentPage.value;
													document.deleteform
															.submit();
												}
											});
							$('#approve')
							.on(
									'click',
									function(e) {
										if (confirm('<s:property value="%{getLocaleProperty('confirm.approveFarmer')}" />')) {
											document.approveform.id.value = document
													.getElementById('farmerId').value;
											document.approveform.currentPage.value = document.form.currentPage.value;
											document.approveform
													.submit();
										}
									});
							if(tenant!='olivado')
								{
								populateDistributionTransactionByFarmerId();
								}
							if(tenant=='olivado')
						    {
						    	jQuery(".ggn").addClass("hide");
						    	jQuery(".farmCode").addClass("hide");
						    	jQuery(".ggnRegNo").addClass("hide");
						    	jQuery(".contactName").removeClass("hide");
						    }
						    else
						    {
						    	jQuery(".farmCode").removeClass("hide");
						    	jQuery(".ggn").addClass("hide");
						    	jQuery(".ggnRegNo").addClass("hide");
						    	jQuery(".contactName").addClass("hide");
						    }
							
						});
		
		
		if(tenant!='pgss')
	     {
	    	 $(".hideDob").show();
	    	 $(".hideAge").hide();
	    	 $(".hideLoanTakenScheme").hide();
	    	 $(".hidePreferenceWrk").hide();
	    	 

	    }
		
		
	     else
	     {
	    	 $(".hideDob").hide();
	    	 $(".hideAge").show();
	    	 $(".hideLoanTakenScheme").show();
	    	 $(".hideLoanTakenScheme").removeClass("hide");
	    	 $(".hidePreferenceWrk").show();

	     }
		
		if(tenant=='chetna')
	     {
	    	 $(".familyDet").show();
	    	
	    	 

	    }
	
		
		


		function enableFarmerPhotoModal(idArr) {
			try {
				$("#mbody").empty();
				var mbody = "";
				mbody = "";
				mbody = "<div class='item active'>";
				mbody += '<img src="farmer_populateImage.action?id='
						+ idArr + '&type=2"/>';
				mbody += "</div>";
				$("#mbody").append(mbody);
				
				document.getElementById("enablePhotoModal").click();
			} catch (e) {
				alert(e);
			}
		}
		/* 	var isDisabled="<s:property value='farmer.isDisable'/>"
			if(isDisabled=="0"){
				$(".irpFormHide").hide();
				$(".S02").hide();
				resetAssesment();
			}else if(isDisabled=="1"){
				$(".irpFormHide").show();
				$(".S02").show();
			} */
	
	    

			
			function populateDistributionTransactionByFarmerId(){
				jQuery.post("farmer_distributionToFarmerByFarmerId.action", {id:document.getElementById('farmerId').value}, function(distribution) {
					var distributionData = $.parseJSON(distribution);
					var table = $("<table/>").addClass("table table-bordered ").attr({
								id : "distribution"
					});
					var thead = $("<thead/>");
					var tr = $("<tr/>");
					tr.append($("<th/>").append("Date"));
					tr.append($("<th/>").append("Warehouse"));
					tr.append($("<th/>").append("Mobile User"));
					tr.append($("<th/>").append("Category"));
					tr.append($("<th/>").append("Product"));
					tr.append($("<th/>").append("Season"));
					tr.append($("<th/>").append("Unit"));
					//tr.append($("<th/>").append("Is free distribution"));
					tr.append($("<th/>").append("Total Quantity"));
					tr.append($("<th/>").append("Gross Amount"));
					tr.append($("<th/>").append("Tax"));
					tr.append($("<th/>").append("Final Amount"));
					tr.append($("<th/>").append("Payment Amount"));
					
				    
					
				
					 
				 
					thead.append(tr);
					table.append(thead);
					
					var tbody = $("<tbody/>");
						$.each(distributionData, function(index, value) {
							var tr2 = $("<tr/>");
							tr2.append($("<td/>").append(value.TXN_TIME));
							tr2.append($("<td/>").append(value.SERVICE_POINT_NAME));
							tr2.append($("<td/>").append(value.AGENT_NAME));
							tr2.append($("<td/>").append(value.category));
							tr2.append($("<td/>").append(value.productName));
							tr2.append($("<td/>").append(value.season));
							tr2.append($("<td/>").append(value.unit));
							//tr2.append($("<td/>").append(value.IS_FREE_DISTRIBUTION));
							tr2.append($("<td/>").append(value.QUANTITY));
							tr2.append($("<td/>").append(value.TOTAL_AMOUNT));
							tr2.append($("<td/>").append(value.tax));
							tr2.append($("<td/>").append(value.FINAL_AMOUNT));
							tr2.append($("<td/>").append(value.PAYMENT_AMT));
							tbody.append(tr2);
						});
					table.append(tbody);
				
					
					var wrappedDiv = $('<div/>').attr({
						id : "distribution_mainDiv",
						class :"appContentWrapper marginBottom"
					});
					
					var headerDiv =  $("<div/>").addClass("formContainerWrapper");
					headerDiv.append("<h2>Distribution Transaction <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#distribution').parent().slideToggle(1000);$('#exportButtons_distribution').toggle(1000);$('#filterDiv_distribution').toggle(1000);></i></a></div> </h2>");
					headerDiv.append("<div id='exportButtons_distribution' style='float:right;'></div>");
					wrappedDiv.append(headerDiv);
					
					var startDateBox = $('<input/>').attr({
						type : 'text',
						class : 'input-sm',
						id : "startDateBox_distribution",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "Start date",
					});
					
					var endDateBox = $('<input/>').attr({
						type : 'text',
						class : 'input-sm',
						id : "endDateBox_distribution",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "End date",
						style : 'margin-left:1%'
					});
					
					var seasonBox = $('<select/>').attr({
						class : "seasonFilter select2",
						id : "season_distribution",
						//style : 'margin-left:1%'
						style : 'float:right'
					});
				
					var filterDiv = $("<div id='filterDiv_distribution'/>");
					filterDiv.append(startDateBox);
					filterDiv.append(endDateBox);
					filterDiv.append(seasonBox);
					filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_distribution" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
					filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_distribution" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
					wrappedDiv.append(filterDiv);
					wrappedDiv.append(table);
					$("#tabs-11").append(wrappedDiv);
					
					if(distributionData != ''){
						
						 var table = $('#distribution').DataTable();
					     var buttons = new $.fn.dataTable.Buttons(table, {
					    	  buttons: [
						                 {
						                     extend: 'excel',
						                     title: 'Distribution Report ( '+'<s:property value="farmer.firstName"/>'+' )',
						                     messageTop: '',
						                     messageBottom: '',
						                   
						                 },
						                 {
						                     extend: 'pdf',
						                     title: 'Distribution Report  ( '+'<s:property value="farmer.firstName"/>'+' )',
						                     messageTop: '',
						                     messageBottom: '',
						                     
						                 }
						             ]
					   }).container().appendTo($('#exportButtons_distribution'));
					     
					}else{
						$('#distribution').DataTable();
					} 
					     
					     
					     
					     $("#startDateBox_distribution" ).datepicker({
						 		dateFormat : "dd-mm-yy",
						 		changeMonth : true,
						 		changeYear : true
						 	}); 
						   
					     $("#endDateBox_distribution" ).datepicker({
						 		dateFormat : "dd-mm-yy",
						 		changeMonth : true,
						 		changeYear : true
						 	});
					     
						      
						    $("#submitBtn_distribution").attr("onclick", "datefilterForDataTable('distribution','distributionToFarmerByFarmerId')");
						    $("#clearBtn_distribution").attr("onclick", "clearFilters('distribution','distributionToFarmerByFarmerId')");  
						  
						 $("#distribution").addClass("dataTableTheme");
						    
						    
					populateCropHarvestTransactionByFarmerId();
					
				});
			}
			
		
			
	  function populateCropHarvestTransactionByFarmerId(){
				
				jQuery.post("farmer_cropHarvestByFarmerId.action", {id:document.getElementById('farmerId').value}, function(cropHarvest) {
					var cropHarvestData = $.parseJSON(cropHarvest);
					var table = $("<table/>").addClass("table table-bordered").attr({
								id : "cropHarvest"
					});
					var thead = $("<thead/>");
					var tr = $("<tr/>");
					tr.append($("<th/>").append("Date"));
					tr.append($("<th/>").append("Farm"));
					tr.append($("<th/>").append("Mobile user"));
					tr.append($("<th/>").append("Product"));
					tr.append($("<th/>").append("Season"));
					tr.append($("<th/>").append("Variety"));
					tr.append($("<th/>").append("Grade"));
					tr.append($("<th/>").append("Total Yield Quantity"));
					tr.append($("<th/>").append("Unit"));
					table.append(thead);
					thead.append(tr);
					var tbody = $("<tbody/>");
						$.each(cropHarvestData, function(index, value) {
							var tr2 = $("<tr/>");
							tr2.append($("<td/>").append(value.HARVEST_DATE));
							tr2.append($("<td/>").append(value.FARM_NAME));
							tr2.append($("<td/>").append(value.agentName));
							tr2.append($("<td/>").append(value.ProductName));
							tr2.append($("<td/>").append(value.season));
							tr2.append($("<td/>").append(value.Variety));
							tr2.append($("<td/>").append(value.Grade));
							tr2.append($("<td/>").append(value.Quantity));
							tr2.append($("<td/>").append(value.UNIT));
							tbody.append(tr2);
						});
					table.append(tbody);
					var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
					var headerDiv =  $("<div/>").addClass("formContainerWrapper");
					headerDiv.append("<h2>Crop Harvest Transaction  <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#cropHarvest').parent().slideToggle(1000);$('#exportButtons_cropHarvest').toggle(1000);$('#filterDiv_cropHarvest').toggle(1000);></i></a></div>  </h2>");
					headerDiv.append("<div id='exportButtons_cropHarvest' style='float:right;'></div>");
					wrappedDiv.append(headerDiv);
					
					var startDateBox = $('<input/>').attr({
						type : 'text',
						id : "startDateBox_cropHarvest",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "Start date",
					});
					
					var endDateBox = $('<input/>').attr({
						type : 'text',
						id : "endDateBox_cropHarvest",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "End date",
						style : 'margin-left:1%'
					});
					
					var seasonBox = $('<select/>').attr({
						class : "seasonFilter select2",
						id : "season_cropHarvest",
						style : 'margin-left:1%'
					});
					
					var filterDiv = $("<div id='filterDiv_cropHarvest'/>");
					filterDiv.append(startDateBox);
					filterDiv.append(endDateBox);
					filterDiv.append(seasonBox);
					filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_cropHarvest" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
					filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_cropHarvest" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
					wrappedDiv.append(filterDiv);
					
					wrappedDiv.append(table);
					$("#tabs-11").append(wrappedDiv);
					
					if(cropHarvestData != ''){
						var table = $('#cropHarvest').DataTable();
					    var buttons = new $.fn.dataTable.Buttons(table, {
					    	  buttons: [
						                 {
						                     extend: 'excel',
						                     title: 'Crop Harvest Report ( '+'<s:property value="farmer.firstName"/>'+' )',
						                     messageTop: '',
						                     messageBottom: '',
						                     
						                 },
						                 {
						                     extend: 'pdf',
						                     title: 'Crop Harvest Report ( '+'<s:property value="farmer.firstName"/>'+' )',
						                     messageTop: '',
						                     messageBottom: '',
						                    
						                 }
						             ]
					   }).container().appendTo($('#exportButtons_cropHarvest'));
					}else{
						$('#cropHarvest').DataTable();
					}
					
					 	
					   
					    $("#startDateBox_cropHarvest" ).datepicker({
					 		dateFormat : "dd-mm-yy",
					 		changeMonth : true,
					 		changeYear : true
					 	}); 
					   
				     $("#endDateBox_cropHarvest" ).datepicker({
					 		dateFormat : "dd-mm-yy",
					 		changeMonth : true,
					 		changeYear : true
					 	});
					     
				     $("#submitBtn_cropHarvest").attr("onclick", "datefilterForDataTable('cropHarvest','cropHarvestByFarmerId')");
					 $("#clearBtn_cropHarvest").attr("onclick", "clearFilters('cropHarvest','cropHarvestByFarmerId')"); 
					 
					 $("#cropHarvest").addClass("dataTableTheme");
					 populateProductReturnTransactionByFarmerId();
				});
			}
			
		function populateProductReturnTransactionByFarmerId(){
			jQuery.post("farmer_productReturnByFarmerId.action", {id:document.getElementById('farmerId').value}, function(productReturn) {
				var productReturnData = $.parseJSON(productReturn);
				var table = $("<table/>").addClass("table table-bordered").attr({
							id : "productReturn"
				});
				var thead = $("<thead/>");
				var tr = $("<tr/>");
				tr.append($("<th/>").append("Date"));
				tr.append($("<th/>").append("Stock Type"));
				tr.append($("<th/>").append("Warehouse"));
				tr.append($("<th/>").append("Mobile User"));
				tr.append($("<th/>").append("Product"));
				tr.append($("<th/>").append("Unit"));
				tr.append($("<th/>").append("season"));
				tr.append($("<th/>").append("Existing Quantity"));
				tr.append($("<th/>").append("Return Quantity"));
				tr.append($("<th/>").append("Cost price"));
				//tr.append($("<th/>").append("Selling price"));
				//tr.append($("<th/>").append("Sub total"));
				table.append(thead);
				thead.append(tr);
				var tbody = $("<tbody/>");
					$.each(productReturnData, function(index, value) {
						var tr2 = $("<tr/>");
						tr2.append($("<td/>").append(value.TXN_TIME));
						tr2.append($("<td/>").append(value.STOCK_TYPE));
						tr2.append($("<td/>").append(value.SERVICE_POINT_NAME));
						tr2.append($("<td/>").append(value.AGENT_NAME));
						tr2.append($("<td/>").append(value.productName));
						tr2.append($("<td/>").append(value.UNIT));
						tr2.append($("<td/>").append(value.season));
						
						tr2.append($("<td/>").append(value.EXISTING_QUANTITY));
						tr2.append($("<td/>").append(value.QUANTITY));
						
						tr2.append($("<td/>").append(value.productPrice));
						//tr2.append($("<td/>").append(value.SELLING_PRICE));
						//tr2.append($("<td/>").append(value.SUB_TOTAL));
						tbody.append(tr2);
					});
				table.append(tbody);
				var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
				var headerDiv =  $("<div/>").addClass("formContainerWrapper");
				headerDiv.append("<h2>Product return Transaction <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#productReturn').parent().slideToggle(1000);$('#exportButtons_productReturn').toggle(1000);$('#filterDiv_productReturn').toggle(1000);></i></a></div>  </h2>");
				headerDiv.append("<div id='exportButtons_productReturn' style='float:right;'></div>");
				wrappedDiv.append(headerDiv);
				
				
				/*  */
				var startDateBox = $('<input/>').attr({
						type : 'text',
						id : "startDateBox_productReturn",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "Start date",
					});
					
					var endDateBox = $('<input/>').attr({
						type : 'text',
						id : "endDateBox_productReturn",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "End date",
						style : 'margin-left:1%'
					});
					
					var seasonBox = $('<select/>').attr({
						class : "seasonFilter select2",
						id : "season_productReturn",
						style : 'margin-left:1%'
					});
					
					var filterDiv = $("<div id='filterDiv_productReturn'/>");
					filterDiv.append(startDateBox);
					filterDiv.append(endDateBox);
					filterDiv.append(seasonBox);
					filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_productReturn" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
					filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_productReturn" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
					wrappedDiv.append(filterDiv);
				
				/*  */
				/* 
				
				var dateBox = $('<input/>').attr({
					type : 'text',
					id : "dateSearchBox_productReturn",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yy",
					placeholder : "Select date",
					onchange : "datefilterForDataTable('productReturn',this.value)"
				});
				
				
				var filterDiv = $("<div id='filterDiv_productReturn'/>");
				filterDiv.append(dateBox);
				filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_productReturn" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
				wrappedDiv.append(filterDiv);
				
				 */
				
				wrappedDiv.append(table);
				$("#tabs-11").append(wrappedDiv);
				
				if(productReturnData != ''){
				
					 var table = $('#productReturn').DataTable();
				     var buttons = new $.fn.dataTable.Buttons(table, {
				    	  buttons: [
					                 {
					                     extend: 'excel',
					                     title: 'Product Return Report ( '+'<s:property value="farmer.firstName"/>'+' )',
					                     messageTop: '',
					                     messageBottom: '',
					                     
					                 },
					                 {
					                     extend: 'pdf',
					                     title: 'Product Return Report ( '+'<s:property value="farmer.firstName"/>'+' )',
					                     messageTop: '',
					                     messageBottom: '',
					                    
					                 }
					             ]
				   }).container().appendTo($('#exportButtons_productReturn'));
			   
				}else{
					$('#productReturn').DataTable();
				}
			  /*    $("#dateSearchBox_productReturn" ).datepicker({
				 		dateFormat : "dd-mm-yy",
				 		changeMonth : true,
				 		changeYear : true
				 	}); 
				     
				    $("#clearBtn_productReturn").attr("onclick", "clearFilters('productReturn','dateSearchBox_productReturn')");
			      */
			      
			      
			      $("#startDateBox_productReturn" ).datepicker({
				 		dateFormat : "dd-mm-yy",
				 		changeMonth : true,
				 		changeYear : true
				 	}); 
				   
			     $("#endDateBox_productReturn" ).datepicker({
				 		dateFormat : "dd-mm-yy",
				 		changeMonth : true,
				 		changeYear : true
				 	});
			     
				      
				    $("#submitBtn_productReturn").attr("onclick", "datefilterForDataTable('productReturn','productReturnByFarmerId')");
				    $("#clearBtn_productReturn").attr("onclick", "clearFilters('productReturn','productReturnByFarmerId')");  
				    $("#productReturn").addClass("dataTableTheme");
			     populateTrainingStatusReportByFarmerId();
			});
		}
		
		function populateTrainingStatusReportByFarmerId(){
			jQuery.post("farmer_trainingStatusReportByFarmerId.action", {id:document.getElementById('farmerId').value}, function(trainingStatus) {
				var trainingStatusData = $.parseJSON(trainingStatus);
				var table = $("<table/>").addClass("table table-bordered").attr({
							id : "trainingStatus"
				});
				var thead = $("<thead/>");
				var tr = $("<tr/>");
				tr.append($("<th/>").append("Date"));
				tr.append($("<th/>").append("Mobile user"));
				tr.append($("<th/>").append("Training code"));
				tr.append($("<th/>").append("Training assistant name"));
				tr.append($("<th/>").append("Time taken for training"));
			/* 	tr.append($("<th/>").append("Farmer attended")); */
				tr.append($("<th/>").append("Remarks"));
				table.append(thead);
				thead.append(tr);
				var tbody = $("<tbody/>");
					$.each(trainingStatusData, function(index, value) {
						var tr2 = $("<tr/>");
						tr2.append($("<td/>").append(value.TRAINING_DATE));
						tr2.append($("<td/>").append(value.agentName));
						tr2.append($("<td/>").append(value.TRAINING_CODE));
						tr2.append($("<td/>").append(value.TRAINING_ASSISTANT_NAME));
						tr2.append($("<td/>").append(value.TIME_TAKEN_FOR_TRAINING));
						/* tr2.append($("<td/>").append(value.FARMER_ATTENED)); */
						tr2.append($("<td/>").append(value.REMARKS));
						tbody.append(tr2);
					});
				table.append(tbody);
				var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
				var headerDiv =  $("<div/>").addClass("formContainerWrapper");
				headerDiv.append("<h2>Training Status <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#trainingStatus').parent().slideToggle(1000);$('#exportButtons_trainingStatus').toggle(1000);$('#filterDiv_trainingStatus').toggle(1000);></i></a></div>  </h2>");
				headerDiv.append("<div id='exportButtons_trainingStatus' style='float:right;'></div>");
				wrappedDiv.append(headerDiv);
				
				
				var startDateBox = $('<input/>').attr({
					type : 'text',
					id : "startDateBox_trainingStatus",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yyyy",
					placeholder : "Start date",
				});
				
				var endDateBox = $('<input/>').attr({
					type : 'text',
					id : "endDateBox_trainingStatus",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yyyy",
					placeholder : "End date",
					style : 'margin-left:1%'
				});
				
				
				
				var filterDiv = $("<div id='filterDiv_trainingStatus'/>");
				filterDiv.append(startDateBox);
				filterDiv.append(endDateBox);
				
				filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_trainingStatus" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
				filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_trainingStatus" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
				wrappedDiv.append(filterDiv);
				
				/* var dateBox = $('<input/>').attr({
					type : 'text',
					id : "dateSearchBox_trainingStatus",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yy",
					placeholder : "Select date",
					onchange : "datefilterForDataTable('trainingStatus',this.value)"
				});
				
				
				var filterDiv = $("<div id='filterDiv_trainingStatus'/>");
				filterDiv.append(dateBox);
				filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_trainingStatus" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
				wrappedDiv.append(filterDiv); */
				
				wrappedDiv.append(table);
				$("#tabs-11").append(wrappedDiv);
				
				if(trainingStatusData != ''){
					 var table = $('#trainingStatus').DataTable();
				     var buttons = new $.fn.dataTable.Buttons(table, {
				    	  buttons: [
					                 {
					                     extend: 'excel',
					                     title: 'Training Status Report ( '+'<s:property value="farmer.firstName"/>'+' )',
					                     messageTop: '',
					                     messageBottom: '',
					                     
					                 },
					                 {
					                     extend: 'pdf',
					                     title: 'Training Status Report ( '+'<s:property value="farmer.firstName"/>'+' )',
					                     messageTop: '',
					                     messageBottom: '',
					                    
					                 }
					             ]
				   }).container().appendTo($('#exportButtons_trainingStatus'));
				}else{
					 $('#trainingStatus').DataTable();
				}
				
				
			     
			     /* $("#dateSearchBox_trainingStatus" ).datepicker({
				 		dateFormat : "dd-mm-yy",
				 		changeMonth : true,
				 		changeYear : true
				 	}); 
				     
				    $("#clearBtn_trainingStatus").attr("onclick", "clearFilters('trainingStatus','dateSearchBox_trainingStatus')");
			      */
			      
			      
			      $("#startDateBox_trainingStatus" ).datepicker({
				 		dateFormat : "dd-mm-yy",
				 		changeMonth : true,
				 		changeYear : true
				 	}); 
				   
			     $("#endDateBox_trainingStatus" ).datepicker({
				 		dateFormat : "dd-mm-yy",
				 		changeMonth : true,
				 		changeYear : true
				 	});
			     
				      
				    $("#submitBtn_trainingStatus").attr("onclick", "datefilterForDataTable('trainingStatus','trainingStatusReportByFarmerId')");
				    $("#clearBtn_trainingStatus").attr("onclick", "clearFilters('trainingStatus','trainingStatusReportByFarmerId')"); 
				    $("#trainingStatus").addClass("dataTableTheme");
			     populateFarmerBalanceReportByFarmerId();
			});
		}
		
		function populateFarmerBalanceReportByFarmerId(){
			jQuery.post("farmer_farmerBalanceReportByFarmerId.action", {id:document.getElementById('farmerId').value}, function(farmerBalanceReport) {
				var farmerBalanceReportData = $.parseJSON(farmerBalanceReport);
				var table = $("<table/>").addClass("table table-bordered").attr({
							id : "farmerBalanceReport"
				});
				var thead = $("<thead/>");
				var tr = $("<tr/>");
				tr.append($("<th/>").append("Date"));
				tr.append($("<th/>").append("Transaction Type"));
				tr.append($("<th/>").append("Receipt Number"));
				tr.append($("<th/>").append("Initial Balance"));
				tr.append($("<th/>").append("Transaction Amount"));
				tr.append($("<th/>").append("Balance Amount"));
				table.append(thead);
				thead.append(tr);
				var tbody = $("<tbody/>");
					$.each(farmerBalanceReportData, function(index, value) {
						var tr2 = $("<tr/>");
						tr2.append($("<td/>").append(value.TXN_TIME));
						tr2.append($("<td/>").append(value.TXN_DESC));
						tr2.append($("<td/>").append(value.RECEIPT_NO));
						tr2.append($("<td/>").append(value.INT_BAL));
						tr2.append($("<td/>").append(value.TXN_AMT));
						tr2.append($("<td/>").append(value.BAL_AMT));
						tbody.append(tr2);
					});
				table.append(tbody);
				
				
				
				
				var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
				var headerDiv =  $("<div/>").addClass("formContainerWrapper");
				headerDiv.append("<h2>Farmer Balance Report <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#farmerBalanceReport').parent().slideToggle(1000);$('#exportButtons_farmerBalanceReport').toggle(1000);$('#filterDiv_farmerBalanceReport').toggle(1000);></i></a></div>  </h2>");
				headerDiv.append("<div id='exportButtons_farmerBalanceReport' style='float:right;'></div>");
				wrappedDiv.append(headerDiv);
				
				var startDateBox = $('<input/>').attr({
					type : 'text',
					id : "startDateBox_farmerBalanceReport",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yyyy",
					placeholder : "Start date",
				});
				
				var endDateBox = $('<input/>').attr({
					type : 'text',
					id : "endDateBox_farmerBalanceReport",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yyyy",
					placeholder : "End date",
					style : 'margin-left:1%'
				});
				
				
				
				var filterDiv = $("<div id='filterDiv_farmerBalanceReport'/>");
				filterDiv.append(startDateBox);
				filterDiv.append(endDateBox);
				
				filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_farmerBalanceReport" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
				filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_farmerBalanceReport" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
				wrappedDiv.append(filterDiv);
				
				/* var dateBox = $('<input/>').attr({
					type : 'text',
					id : "dateSearchBox_farmerBalanceReport",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yy",
					placeholder : "Select date",
					onchange : "datefilterForDataTable('farmerBalanceReport',this.value)"
				});
				
				var filterDiv = $("<div id='filterDiv_farmerBalanceReport'/>");
				filterDiv.append(dateBox);
				filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_farmerBalanceReport" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
				wrappedDiv.append(filterDiv); */
				
				wrappedDiv.append(table);
				$("#tabs-11").append(wrappedDiv);
				
				if(farmerBalanceReportData != ''){
					var table = $('#farmerBalanceReport').DataTable();
				     var buttons = new $.fn.dataTable.Buttons(table, {
				       buttons: [
				                 {
				                     extend: 'excel',
				                     title: 'Farmer Balance Report ( '+'<s:property value="farmer.firstName"/>'+' )',
				                     messageTop: '',
				                     messageBottom: '',
				                     
				                 },
				                 {
				                     extend: 'pdf',
				                     title: 'Farmer Balance Report ( '+'<s:property value="farmer.firstName"/>'+' )',
				                     messageTop: '',
				                     messageBottom: '',
				                    
				                 }
				             ]
				   }).container().appendTo($('#exportButtons_farmerBalanceReport')); 
				}else{
					 $('#farmerBalanceReport').DataTable();
				}
				
				  
			    

					
				  $("#startDateBox_farmerBalanceReport" ).datepicker({
					 		dateFormat : "dd-mm-yy",
					 		changeMonth : true,
					 		changeYear : true
					 	}); 
					   
				     $("#endDateBox_farmerBalanceReport" ).datepicker({
					 		dateFormat : "dd-mm-yy",
					 		changeMonth : true,
					 		changeYear : true
					 	});
				     
					      
					    $("#submitBtn_farmerBalanceReport").attr("onclick", "datefilterForDataTable('farmerBalanceReport','farmerBalanceReportByFarmerId')");
					    $("#clearBtn_farmerBalanceReport").attr("onclick", "clearFilters('farmerBalanceReport','farmerBalanceReportByFarmerId')");  
					    $("#farmerBalanceReport").addClass("dataTableTheme");
					    populateProcurementTransactionsByFarmerId();
			     
			  /*    
			    $("#dateSearchBox_farmerBalanceReport" ).datepicker({
			 		dateFormat : "dd-mm-yy",
			 		changeMonth : true,
			 		changeYear : true
			 	}); 
			     
			    $("#clearBtn_farmerBalanceReport").attr("onclick", "clearFilters('farmerBalanceReport','dateSearchBox_farmerBalanceReport')");
			    */  
			});
		}
		
		
		function populateProcurementTransactionsByFarmerId(){

			jQuery.post("farmer_procurementTransactionsByFarmerId.action", {id:document.getElementById('farmerId').value}, function(procurementTransaction) {
				var procurementTransactionData = $.parseJSON(procurementTransaction);
				var table = $("<table/>").addClass("table table-bordered").attr({
							id : "procurementTransactionReport"
				});
				var thead = $("<thead/>");
				var tr = $("<tr/>");
				tr.append($("<th/>").append("Date"));
				tr.append($("<th/>").append("Mobile user"));
				tr.append($("<th/>").append("Season"));
				tr.append($("<th/>").append("Product Name"));
				tr.append($("<th/>").append("Unit"));
				tr.append($("<th/>").append("Number Of Bags"));
				tr.append($("<th/>").append("Net Weight"));
				tr.append($("<th/>").append("Total Amount"));
				tr.append($("<th/>").append("payment Amount"));
				table.append(thead);
				thead.append(tr);
				var tbody = $("<tbody/>");
					$.each(procurementTransactionData, function(index, value) {
						var tr2 = $("<tr/>");
						tr2.append($("<td/>").append(value.CREATED_DATE));
						tr2.append($("<td/>").append(value.agentName));
						tr2.append($("<td/>").append(value.season));
						tr2.append($("<td/>").append(value.ProductName));
						tr2.append($("<td/>").append(value.Unit));
						tr2.append($("<td/>").append(value.NUMBER_OF_BAGS));
						tr2.append($("<td/>").append(value.NET_WEIGHT));
						tr2.append($("<td/>").append(value.TOTAL_AMOUNT));
						tr2.append($("<td/>").append(value.PAYMENT_AMT));
						tbody.append(tr2);
					});
				table.append(tbody);
				
				
				
				
				var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
				var headerDiv =  $("<div/>").addClass("formContainerWrapper");
				headerDiv.append("<h2>Procurement Transaction Report <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#procurementTransactionReport').parent().slideToggle(1000);$('#exportButtons_procurementTransactionReport').toggle(1000);$('#filterDiv_procurementTransactionReport').toggle(1000);></i></a></div>  </h2>");
				headerDiv.append("<div id='exportButtons_procurementTransactionReport' style='float:right;'></div>");
				wrappedDiv.append(headerDiv);
				
				var startDateBox = $('<input/>').attr({
					type : 'text',
					id : "startDateBox_procurementTransactionReport",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yyyy",
					placeholder : "Start date",
				});
				
				var endDateBox = $('<input/>').attr({
					type : 'text',
					id : "endDateBox_procurementTransactionReport",
					readonly : 'readonly',
					'data-date-format' : "dd-mm-yyyy",
					placeholder : "End date",
					style : 'margin-left:1%'
				});
				
				var seasonBox = $('<select/>').attr({
					class : "seasonFilter select2",
					id : "season_procurementTransactionReport",
					style : 'margin-left:1%'
				});
				
				var filterDiv = $("<div id='filterDiv_procurementTransactionReport'/>");
				filterDiv.append(startDateBox);
				filterDiv.append(endDateBox);
				filterDiv.append(seasonBox);
				filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_procurementTransactionReport" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
				filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_procurementTransactionReport" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
				wrappedDiv.append(filterDiv);
				
				
				wrappedDiv.append(table);
				$("#tabs-11").append(wrappedDiv);
				
				if(procurementTransactionData != ''){
					 var table = $('#procurementTransactionReport').DataTable();
				     var buttons = new $.fn.dataTable.Buttons(table, {
				       buttons: [
				                 {
				                     extend: 'excel',
				                     title: 'Procurement TransactionReport Report ( '+'<s:property value="farmer.firstName"/>'+' )',
				                     messageTop: '',
				                     messageBottom: '',
				                     
				                 },
				                 {
				                     extend: 'pdf',
				                     title: 'Procurement TransactionReport Report ( '+'<s:property value="farmer.firstName"/>'+' )',
				                     messageTop: '',
				                     messageBottom: '',
				                    
				                 }
				             ]
				   }).container().appendTo($('#exportButtons_procurementTransactionReport')); 
				}else{
					$('#procurementTransactionReport').DataTable();
				}
				
				
			    

					
				  $("#startDateBox_procurementTransactionReport" ).datepicker({
					 		dateFormat : "dd-mm-yy",
					 		changeMonth : true,
					 		changeYear : true
					 	}); 
					   
				     $("#endDateBox_procurementTransactionReport" ).datepicker({
					 		dateFormat : "dd-mm-yy",
					 		changeMonth : true,
					 		changeYear : true
					 	});
				     
					      
					    $("#submitBtn_procurementTransactionReport").attr("onclick", "datefilterForDataTable('procurementTransactionReport','procurementTransactionsByFarmerId')");
					    $("#clearBtn_procurementTransactionReport").attr("onclick", "clearFilters('procurementTransactionReport','procurementTransactionsByFarmerId')");  
					    $("#procurementTransactionReport").addClass("dataTableTheme");
					    loadSeasonFilter();  
			  
			});
		}
		 function populatePeriodicInspectionsByFarmerId(){
				jQuery.post("farmer_periodicInspectionsByFarmerId.action", {id:document.getElementById('farmerId').value}, function(periodicInspections) {
					var periodicInspectionsData = $.parseJSON(periodicInspections);
					
					var table = $("<table/>").addClass("table table-bordered").attr({
								id : "periodicInspectionsReport"
					});
					var thead = $("<thead/>");
					var tr = $("<tr/>");
					tr.append($("<th/>").append("Date"));
					tr.append($("<th/>").append("Season"));
					tr.append($("<th/>").append("Farm"));
					tr.append($("<th/>").append("Crop"));
					tr.append($("<th/>").append("Mobile User"));
					tr.append($("<th/>").append("Status Of Growth"));
					tr.append($("<th/>").append("Inter Crop"));
					//tr.append($("<th/>").append("payment Amount"));
					table.append(thead);
					thead.append(tr);
					var tbody = $("<tbody/>");
						$.each(periodicInspectionsData, function(index, value) {
							var tr2 = $("<tr/>");
							tr2.append($("<td/>").append(value.CREATED_DATE));
							tr2.append($("<td/>").append(value.season));
							tr2.append($("<td/>").append(value.farm));
							tr2.append($("<td/>").append(value.crop));
							tr2.append($("<td/>").append(value.mobile));
							tr2.append($("<td/>").append(value.catalogue));
							tr2.append($("<td/>").append(value.interCrop));
							tbody.append(tr2);
						});
					table.append(tbody);
					
					
					
					
					var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
					var headerDiv =  $("<div/>").addClass("formContainerWrapper");
					headerDiv.append("<h2>Regular Inspection Report <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#periodicInspectionsReport').parent().slideToggle(1000);$('#exportButtons_periodicInspectionsReport').toggle(1000);$('#filterDiv_periodicInspectionsReport').toggle(1000);></i></a></div>  </h2>");
					headerDiv.append("<div id='exportButtons_periodicInspectionsReport' style='float:right;'></div>");
					wrappedDiv.append(headerDiv);
					
					 var startDateBox = $('<input/>').attr({
						type : 'text',
						id : "startDateBox_periodicInspectionsReport",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "Start date",
					});
					
					var endDateBox = $('<input/>').attr({
						type : 'text',
						id : "endDateBox_periodicInspectionsReport",
						readonly : 'readonly',
						'data-date-format' : "dd-mm-yyyy",
						placeholder : "End date",
						style : 'margin-left:1%'
					}); 
					
				 	var seasonBox = $('<select/>').attr({
						class : "seasonFilter select2",
						id : "season_periodicInspectionsReport",
						style : 'margin-left:1%'
					}); 
					
					var filterDiv = $("<div id='filterDiv_periodicInspectionsReport'/>");
					filterDiv.append(startDateBox);
					filterDiv.append(endDateBox);
					filterDiv.append(seasonBox);
					filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_periodicInspectionsReport" class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
					filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_periodicInspectionsReport" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
					wrappedDiv.append(filterDiv);
					
					
					wrappedDiv.append(table);
					$("#tabs-11").append(wrappedDiv);
					
					if(periodicInspectionsData != ''){
						var table = $('#periodicInspectionsReport').DataTable();
					     var buttons = new $.fn.dataTable.Buttons(table, {
					       buttons: [
					                 {
					                     extend: 'excel',
					                     title: 'Farm Inspection Report ( '+'<s:property value="farmer.firstName"/>'+' )',
					                     messageTop: '',
					                     messageBottom: '',
					                     
					                 },
					                 {
					                     extend: 'pdf',
					                     title: 'Farm Inspection Report Report ( '+'<s:property value="farmer.firstName"/>'+' )',
					                     messageTop: '',
					                     messageBottom: '',
					                    
					                 }
					             ]
					   }).container().appendTo($('#exportButtons_periodicInspectionsReport')); 
					}else{
						$('#periodicInspectionsReport').DataTable();
					}
					
					 
				    

						
				  $("#startDateBox_periodicInspectionsReport" ).datepicker({
						 		dateFormat : "dd-mm-yy",
						 		changeMonth : true,
						 		changeYear : true
						 	}); 
						   
					     $("#endDateBox_periodicInspectionsReport" ).datepicker({
						 		dateFormat : "dd-mm-yy",
						 		changeMonth : true,
						 		changeYear : true
						 	});
					     
						       
						    $("#submitBtn_periodicInspectionsReport").attr("onclick", "datefilterForDataTable('periodicInspectionsReport','periodicInspectionsByFarmerId')");
						    $("#clearBtn_periodicInspectionsReport").attr("onclick", "clearFilters('periodicInspectionsReport','periodicInspectionsByFarmerId')");  
						    $("#periodicInspectionsReport").addClass("dataTableTheme");
						   // loadSeasonFilter(); 
						    populatePeriodicNeedBasedInspectionsByFarmerId();
				  
				});
			}
			 
			 function populatePeriodicNeedBasedInspectionsByFarmerId(){
					jQuery.post("farmer_periodicNeedBasedInspectionsByFarmerId.action", {id:document.getElementById('farmerId').value}, function(periodicNeedBasedInspection) {
						var periodicNeedBasedInspectionData = $.parseJSON(periodicNeedBasedInspection);
						
						var table = $("<table/>").addClass("table table-bordered").attr({
									id : "periodicNeedBasedInspectionReport"
						});
						var thead = $("<thead/>");
						var tr = $("<tr/>");
						tr.append($("<th/>").append("Date"));
						tr.append($("<th/>").append("Season"));
						tr.append($("<th/>").append("Farm"));
						tr.append($("<th/>").append("Crop"));
						tr.append($("<th/>").append("Mobile User"));
						tr.append($("<th/>").append("Inter Crop"));
						//tr.append($("<th/>").append("payment Amount"));
						table.append(thead);
						thead.append(tr);
						var tbody = $("<tbody/>");
							$.each(periodicNeedBasedInspectionData, function(index, value) {
								var tr2 = $("<tr/>");
								tr2.append($("<td/>").append(value.CREATED_DATE));
								tr2.append($("<td/>").append(value.season));
								tr2.append($("<td/>").append(value.farm));
								tr2.append($("<td/>").append(value.crop));
								tr2.append($("<td/>").append(value.mobile));
								tr2.append($("<td/>").append(value.interCrop));
								tbody.append(tr2);
							});
						table.append(tbody);
						
						
						
						
						var wrappedDiv = $("<div/>").addClass("appContentWrapper marginBottom");
						var headerDiv =  $("<div/>").addClass("formContainerWrapper");
						headerDiv.append("<h2>Need Based Inspection Report <div class='pull-right'><a class='aCollapse'><i class='fa fa-chevron-right' onclick=$('#periodicNeedBasedInspectionReport').parent().slideToggle(1000);$('#exportButtons_periodicNeedBasedInspectionReport').toggle(1000);$('#filterDiv_periodicInspectionsReport').toggle(1000);></i></a></div>  </h2>");
						headerDiv.append("<div id='exportButtons_periodicNeedBasedInspectionReport' style='float:right;'></div>");
						wrappedDiv.append(headerDiv);
						
						 var startDateBox = $('<input/>').attr({
							type : 'text',
							id : "startDateBox_periodicNeedBasedInspectionReport",
							readonly : 'readonly',
							'data-date-format' : "dd-mm-yyyy",
							placeholder : "Start date",
						});
						
						var endDateBox = $('<input/>').attr({
							type : 'text',
							id : "endDateBox_periodicNeedBasedInspectionReport",
							readonly : 'readonly',
							'data-date-format' : "dd-mm-yyyy",
							placeholder : "End date",
							style : 'margin-left:1%'
						}); 
						
					 	var seasonBox = $('<select/>').attr({
							class : "seasonFilter select2",
							id : "season_periodicNeedBasedInspectionReport",
							style : 'margin-left:1%'
						}); 
						
						var filterDiv = $("<div id='filterDiv_periodicNeedBasedInspectionReport'/>");
						filterDiv.append(startDateBox);
						filterDiv.append(endDateBox);
						filterDiv.append(seasonBox);
						filterDiv.append('<button style="margin-left:1%;" type="button" id="submitBtn_periodicNeedBasedInspectionReport class="btn btn-success btn-sm" >Search <i class="glyphicon glyphicon-search" aria-hidden="true"></i></button>');
						filterDiv.append('<button style="margin-left:1%;" type="button" id="clearBtn_periodicNeedBasedInspectionReport" class="btn btn-warning btn-sm" >Clear <i class="glyphicon glyphicon-remove" aria-hidden="true"></i></button>');
						wrappedDiv.append(filterDiv);
						
						
						wrappedDiv.append(table);
						$("#tabs-11").append(wrappedDiv);
						
						if(periodicNeedBasedInspectionData != ''){
							var table = $('#periodicNeedBasedInspectionReport').DataTable();
						     var buttons = new $.fn.dataTable.Buttons(table, {
						       buttons: [
						                 {
						                     extend: 'excel',
						                     title: 'Farm Inspection Report ( '+'<s:property value="farmer.firstName"/>'+' )',
						                     messageTop: '',
						                     messageBottom: '',
						                     
						                 },
						                 {
						                     extend: 'pdf',
						                     title: 'Farm Inspection Report Report ( '+'<s:property value="farmer.firstName"/>'+' )',
						                     messageTop: '',
						                     messageBottom: '',
						                    
						                 }
						             ]
						   }).container().appendTo($('#exportButtons_periodicNeedBasedInspectionReport')); 
						}else{
							$('#periodicNeedBasedInspectionReport').DataTable();
						}
						
						
					    

							
					  $("#startDateBox_periodicNeedBasedInspectionReport" ).datepicker({
							 		dateFormat : "dd-mm-yy",
							 		changeMonth : true,
							 		changeYear : true
							 	}); 
							   
						     $("#endDateBox_periodicNeedBasedInspectionReport" ).datepicker({
							 		dateFormat : "dd-mm-yy",
							 		changeMonth : true,
							 		changeYear : true
							 	});
						     
							       
							    $("#submitBtn_periodicNeedBasedInspectionReportt").attr("onclick", "datefilterForDataTable('periodicNeedBasedInspectionReport','periodicNeedBasedInspectionsByFarmerId')");
							    $("#clearBtn_periodicNeedBasedInspectionReport").attr("onclick", "clearFilters('periodicNeedBasedInspectionReport','periodicNeedBasedInspectionsByFarmerId')");  
							    $("#periodicNeedBasedInspectionReport").addClass("dataTableTheme");
							    loadSeasonFilter();  
					  
					});
			 }
			 
		
	function datefilterForDataTable(tableId,url){
		
		var start = $('#startDateBox_'+tableId).val();
		var end = 	$('#endDateBox_'+tableId).val();
		var season = $("#season_"+tableId).val();
		
		if(!isEmpty(start) && isEmpty(end) || isEmpty(start) && !isEmpty(end) ){
			alert("Please choose both dates")
		}else if(isEmpty(start) && isEmpty(end) && isEmpty(season) ){
			alert("Please choose filter")
		}else{
			$('#'+tableId).DataTable().destroy();	
			if(isEmpty(start)){
				start = "showAll";
			}
			
			if(isEmpty(end)){
				end = "showAll";
			}
			jQuery.post("farmer_"+url+".action", {id:document.getElementById('farmerId').value,startDate:start,endDate:end,seasonCode:season}, function(result) {
					var data = $.parseJSON(result);
					var dataFor_dataTable = [];
					
					for (var i = 0; i < data.length; i++){
					    var obj = data[i];
					    for (var key in obj){
					        var attrName = key;
					        var attrValue = obj[key];
					       
					        if(attrName == "data"){
					        	var result = attrValue.substring(1, attrValue.length-1);
					        	 
					        	 while(result.includes("]")){
					        		 var start =	result.indexOf("[");
							        	var end =	result.indexOf("]");
							            var res = result.substring(start+1, end);
							            var tempArr =	res.split(",");
							           	result = result.slice(start+1);
							        	result = result.slice(end);
							        	
							        	/* for (var i = 0; i < tempArr.length; i++){
							        		 if(tempArr[i].includes("#")){
							        			 tempArr[i] = tempArr[i].replace(/#/g, ",");
							        			}
							        	} */
							        	
							        	dataFor_dataTable.push(tempArr);
					        	 }
					        	
					        	if(dataFor_dataTable.length != 0){
					        		 var table  = $('#'+tableId).DataTable( {
							        	 data: dataFor_dataTable
								        });
						        	
						        	var buttons = new $.fn.dataTable.Buttons(table, {
								    	  buttons: [
									                 {
									                     extend: 'excel',
									                     title: tableId+' Report ( '+'<s:property value="farmer.firstName"/>'+' )',
									                     messageTop: '',
									                     messageBottom: '',
									                     
									                 },
									                 {
									                     extend: 'pdf',
									                     title: tableId+' Report  ( '+'<s:property value="farmer.firstName"/>'+' )',
									                     messageTop: '',
									                     messageBottom: '',
									                    
									                 }
									             ]
								   }).container().appendTo($('#exportButtons_'+tableId));
					        	}else{
					        		$('#'+tableId).DataTable( {
							        	 data: dataFor_dataTable
								        });
					        	}
					        	
					        	
					        	
					       }
					    }
					} 
				 }); 
		}
		
}
		
		function clearFilters(tableId,url){
			if(!isEmpty($("#startDateBox_"+tableId).val())  || !isEmpty($("#endDateBox_"+tableId).val()) || !isEmpty($("#season_"+tableId).val()) ){
				$("#startDateBox_"+tableId).val("");
				$("#endDateBox_"+tableId).val("");
				$("#season_"+tableId).val("");
				refreshDataTable(tableId,url);
			}
		}
		
		
		function refreshDataTable(tableId,url){

				 $('#startDateBox_'+tableId).val("");
				$('#endDateBox_'+tableId).val("");
				//$("#season_"+tableId).val("");
				
				var seasonFilter = document.getElementById("season_"+tableId);
			
				if(seasonFilter != null){
					$("#season_"+tableId).select2("val", "");
				} 
				
					
				$('#'+tableId).DataTable().destroy();	
				var start = "showAll";
				var end = "showAll";
						
						jQuery.post("farmer_"+url+".action", {id:document.getElementById('farmerId').value,startDate:start,endDate:end}, function(result) {
								var data = $.parseJSON(result);
								var dataFor_dataTable = [];
								
								for (var i = 0; i < data.length; i++){
								    var obj = data[i];
								    for (var key in obj){
								        var attrName = key;
								        var attrValue = obj[key];
								       
								        if(attrName == "data"){
								        	var result = attrValue.substring(1, attrValue.length-1);
								        	
								        	 while(result.includes("]")){
								        		 var start =	result.indexOf("[");
										        	var end =	result.indexOf("]");
										            var res = result.substring(start+1, end);
										           	var tempArr =	res.split(",");
										        	result = result.slice(start+1);
										        	result = result.slice(end);
										        	dataFor_dataTable.push(tempArr);
								        	 }
								        	 
								        if(dataFor_dataTable.length != 0){
								        	
								        	var table  = $('#'+tableId).DataTable( {
									        	 data: dataFor_dataTable
									        } );
								        	
								        	
								        	var buttons = new $.fn.dataTable.Buttons(table, {
										    	  buttons: [
											                 {
											                     extend: 'excel',
											                     title: tableId+' Report ( '+'<s:property value="farmer.firstName"/>'+' )',
											                     messageTop: '',
											                     messageBottom: '',
											                     
											                 },
											                 {
											                     extend: 'pdf',
											                     title: tableId+' Report  ( '+'<s:property value="farmer.firstName"/>'+' )',
											                     messageTop: '',
											                     messageBottom: '',
											                    
											                 }
											             ]
										   }).container().appendTo($('#exportButtons_'+tableId));
								        }else{
								        	 $('#'+tableId).DataTable( {
									        	 data: dataFor_dataTable
									        });
								        }
								        	
								       }
								    }
								} 
							 }); 
			
		}
		
		function loadSeasonFilter(){
			var url = "dashboard_populateSeasonList.action";
			var name = "seasonFilter";
			var cat = $.ajax({
				url: url,
				async: false, 
				type: 'post',
				success: function(result) {
					insertOptionsByClass(name,JSON.parse(result));
				}        	

			});
			$('.select2').select2();
		}
		
		 function insertOptionsByClass(ctrlName, jsonArr) {
			
			  $("."+ctrlName).append($('<option>',{
	            	 value: "",
	                 text: "Select season"
		 }));
			 
			 $.each(jsonArr, function(i, value) {
		            $("."+ctrlName).append($('<option>',{
		            	 value: value.id,
		                 text: value.name
			 }));
		});	
		 }
		
		 function getContracteTemplate(){
			 $.ajax({
				 type: "POST",
		         async: false,
		         data:{},
		      	 url: "prefernce_getContracteTemplate",
		      	 success: function(result) {
		      		var json = jQuery.parseJSON(result);
		      		var html_str = json.templateHtml;
		      		
		      		if(html_str.includes("[:farmerName]")){
		      			html_str = html_str.replace("[:farmerName]", '<s:property value="farmer.firstName"/>');
		      		}
		      		
		      		if(html_str.includes("[:fatherName]")){
		      			html_str = html_str.replace("[:fatherName]", '<s:property value="farmer.lastName" />');
		      		}
		      		
		      		if(html_str.includes("[:village]")){
		      			html_str = html_str.replace("[:village]", '<s:property value="farmer.village.name" />');
		      		}
		      		
		      		if(html_str.includes("[:season]")){
		      			html_str = html_str.replace("[:season]", '<s:property value="getCurrentSeason()"/>');
		      		}
		      		
		      		if(html_str.includes("[:currentYear]")){
		      			html_str = html_str.replace("[:currentYear]", (new Date()).getFullYear());
		      		}
		      		
		      		if(html_str.includes("[:currentDate]")){
		      			var d = new Date();
		      			var strDate = d.getFullYear() + "/" + (d.getMonth()+1) + "/" + d.getDate();
		      			html_str = html_str.replace("[:currentDate]", strDate);
		      		}
		      		
		      		if(html_str.includes("[:farmerAge]")){
		      			html_str = html_str.replace("[:farmerAge]", '<s:property value="farmer.age"/>');
		      		}
		      		
		      		if(html_str.includes("[:digitalSignature]")){
		      			
		      			if(!isEmpty('<s:property value="digitalSignatureByteString"/>') && '<s:property value="digitalSignatureByteString"/>' != null){
		      				html_str = html_str.replace("[:digitalSignature]", '<img border="0" width="150px" height="100px" src="data:image/png;base64,<s:property value="digitalSignatureByteString"/>">');
		      			}else{
		      				html_str = html_str.replace("[:digitalSignature]","");
		      			}
		      			
		      		}
		      		
		      		//var win = window.open("", "Title", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=780,height=200,top="+(screen.height-400)+",left="+(screen.width-840));
		      		var win = window.open();
		      		win.document.body.innerHTML = html_str;
		      		//win.print();
		      		
		       }
			});
		 }
		 
		 
		 
	</script>
	<div class="card-header card mb-1 shadow-none">
		<a href="#persInfo" class="text-dark" data-toggle="collapse"
			aria-expanded="true" aria-controls="collapseOne">
			<div class="card-header" id="headingOne">
				<h6 class="m-0">
					<div class="wizard-wrapper">
						<div class="wizard-icon">
							<span class="svg-icon svg-icon-2x"> <!--begin::Svg Icon | path:/metronic/theme/html/demo1/dist/assets/media/svg/icons/General/User.svg-->
								<svg xmlns="http://www.w3.org/2000/svg"
									xmlns:xlink="http://www.w3.org/1999/xlink" width="24px"
									height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none"
										fill-rule="evenodd">
																			<polygon points="0 0 24 0 24 24 0 24"></polygon>
																			<path
										d="M12,11 C9.790861,11 8,9.209139 8,7 C8,4.790861 9.790861,3 12,3 C14.209139,3 16,4.790861 16,7 C16,9.209139 14.209139,11 12,11 Z"
										fill="#000000" fill-rule="nonzero" opacity="0.3"></path>
																			<path
										d="M3.00065168,20.1992055 C3.38825852,15.4265159 7.26191235,13 11.9833413,13 C16.7712164,13 20.7048837,15.2931929 20.9979143,20.2 C21.0095879,20.3954741 20.9979143,21 20.2466999,21 C16.541124,21 11.0347247,21 3.72750223,21 C3.47671215,21 2.97953825,20.45918 3.00065168,20.1992055 Z"
										fill="#000000" fill-rule="nonzero"></path>
																		</g>
																	</svg> <!--end::Svg Icon-->
							</span>

						</div>
						<div class="wizard-label">

							<h3 class="wizard-title">
								<s:property value="%{getLocaleProperty('info.personal')}" />
							</h3>
							<div class="wizard-desc">Setup Farmer Basic Information
								Details</div>
						</div>
					</div>
					<i
						class="mdi mdi-minus float-right accor-plus-icon collapse-icon-custom"></i>
				</h6>
			</div>
		</a>

	</div>
	<div class="card-body">

		<div class="row">

			<%-- 	<div class="col-md-4">
				<div class="form-group firstName">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('farmer.firstName')}" /><sup
						style="color: red;">*</sup>
					</label>
						<p class="form-group" name="farmer.firstName">
														<s:property value="farmer.firstName" />
													</p>
					<div class="">
						<s:textfield id="firstName" name="farmer.firstName" maxlength="50"
							onkeypress="return isAlphabet(event)"
							cssClass="upercls form-control" />
					</div>
				</div>
			</div> --%>
			<div class="col-md-4">
				<div class="dynamic-flexItem2">
					<p class="form-group nameLab">
						<s:property value="%{getLocaleProperty('farmer.firstName')}" />
					</p>
					<p class="form-control" name="farmer.firstName">
						<s:property value="farmer.firstName" />
					</p>
				</div>

			</div>
			<div class="col-md-4">
				<p class="form-group">
					<s:property value="%{getLocaleProperty('farmer.lastName')}" />
				</p>
				<p class="form-control" name="farmer.lastName">
					<s:property value="farmer.lastName" />
				</p>
			</div>

			<div class="col-md-4">
				<p class="form-group">
					<s:property value="%{getLocaleProperty('farmer.surName')}" />
				</p>
				<p class="form-control" name="farmer.surName">
					<s:property value="farmer.surName" />
				</p>
			</div>

			<div class="col-md-4">
				<p class="form-group">
					<s:property value="%{getLocaleProperty('farmer.gender')}" />
				</p>
				<p class="form-control" name="farmer.gender">
					<s:text name='%{farmer.gender}' />
				</p>
			</div>
			<div class="col-md-4">
				<p class="form-group dateName">
					<s:text name="farmer.dateOfBirth" />
				</p>
				<p class="form-control font-weight-bold" name="calendar">
					<s:property value="dateOfBirth" />
				</p>
			</div>
			<div class="col-md-4">
				<p class="form-group">
					<s:property value="%{getLocaleProperty('farmer.age1')}" />
				</p>
				<p class="form-control" name="age">
					<s:property value="farmer.age" />
				</p>
			</div>
		</div>
	</div>
</body>

