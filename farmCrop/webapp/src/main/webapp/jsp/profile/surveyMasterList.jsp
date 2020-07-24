<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
	<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
var role='<s:property value="loggedInRole" />';

	$(document).ready(function(){
		jQuery("#detail").jqGrid(
		{
		   	url:'surveyMaster_data.action',
		   	pager: '#pagerForDetail',	  
		   	datatype: "json",	
		   	mtype: 'POST',
		   	colNames:[
		  		   	  '<s:text name="surveyMaster.code"/>',
		  		   	  '<s:text name="surveyMaster.name"/>',
		  		      '<s:text name="surveyMaster.description"/>',
		  		      '<s:text name="surveyMaster.surveyType"/>',
		  			      '<s:text name="surveyMaster.section"/>',
		  		   	  '<s:text name="surveyMaster.section.dataLevel"/>',
		  			 <s:if test="'Admin'.equalsIgnoreCase(loggedInRole)">
		  		      '<s:text name="surveyMaster.action"/>'
		  		   	</s:if>
		  		      
		  		    ],
		   	colModel:[			
		   		{name:'code',index:'code',width:125,sortable:true},
		   		{name:'name',index:'name',width:125,sortable:true},
		   		{name:'description',index:'description',width:125,sortable:true},
		   		{name:'surveyType',index:'surveyType',width:125,sortable:true},
		   	  		{name:'entity',index:'entity',width:125,sortable:true},
		   		{name:'profile',index:'profile',width:125,sortable:true},
		  
		   	 <s:if test="'Admin'.equalsIgnoreCase(loggedInRole)">
		   		{name:'action',index:'action',align:"center",width:125,sortable:true,search:false,
		    			formatter: function (cellValue, option) {
		   		        return '<input value="'+'<s:text name="clone"/>'+'" id="but_' + option.rowId +'" type="button" onclick="clone(' + option.rowId +')" class="btn btn-sts" />';
		   		    }
		   		}
		   		</s:if>
		        ],
        
		   	height: 301, 
		    width: $("#baseDiv").width(), // assign parent div width
		    scrollOffset: 0,
		   	rowNum:10,
		   	rowList : [10,25,50],
		    sortname:'id',			  
		    sortorder: "desc",
		    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			onCellSelect: function( rowId, iCol, content, event) {
                document.updateform.id.value=rowId;
        		  document.updateform.submit();      
        
            
			},	
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

	function processTargetValue(surveyId,targetValue){
		if(targetValue>=0) {
    		 $.post("surveyMaster_createTarget.action",{surveyId:surveyId,targetValue:targetValue},function(data,status){
			            	if(status=='success'){
			            		alert('<s:text name="save.success" />');
			            		$('#detail').trigger('reloadGrid');
			            	}else{
			            		alert('<s:text name="save.error" />');
			            	}
			     });
		}else{
			alert('Please Enter Valid Number!');
		}
	}
	
	function clone(obj){

		$('#type').val('');
		$('#name').val('');
		$('#idSent').val(obj);
		$('#restartAlert2').show();
		
		$('body').css('overflow','hidden');
		$('#popupBackground').css('width','100%');
		$('#popupBackground').css('height',$('#popupBackground').height());
		$('#popupBackground').css('top','0');
		$('#popupBackground').css('left','0');
		$('#popupBackground').show();
	//	$('#sectLangPrefSurvey').append($('#sectLangPref'));
		var i = 0;
		 $("#langList option").each(function(i){
		       var trss = '';
		       trss+='<tr class="odd"><td width="20%">'+$(this).val()+'<sup style="color: red;">*</sup></td>';
		       trss+='<td width="40%"><s:textfield value="" name="surveyMaster.languagePreferenceList['+i+'].name" id="langPrefName" theme="simple" cssClass="sectionName"/></td>'
		       trss+='<td width="40%"><s:textfield value="" name="surveyMaster.languagePreferenceList['+i+'].info"	theme="simple" cssClass="sectionInfo"/>';
		       trss+=' <s:hidden name="surveyMaster.languagePreferenceList['+i+'].id"/>';
		       trss+=' <s:hidden name="surveyMaster.languagePreferenceList['+i+'].code"/>';
		       trss+=' <s:hidden name="surveyMaster.languagePreferenceList['+i+'].shortName" cssClass="shortName"/>	';
		       trss+=' <s:hidden name="surveyMaster.languagePreferenceList['+i+'].lang" value="'+$(this).val()+'" cssClass="lang"/> ';
		       trss+=' <s:hidden name="surveyMaster.languagePreferenceList['+i+'].type" value="4" cssClass="type"/></td></tr>'
		       i++;
		       $('#sectLangPrefSurvey').append($(trss));
		       
		    });
		window.location.hash="#restartAlert2";
		
	
	}

	function disableKMLValuesAlert(){
		$('#pendingExtendAlertErrMsg').html('');
		$('#popupBackground').hide();
		$('#extendAlert').hide();
		$('#restartAlert2').hide();
		$('body').css('overflow','');
		
	}
	
	function populateClone(){
		var val = $('#name').val();
		var type = $('#type').val();
		var dl = $('#dataLevel').val();
		var hit=true;
		if(val==''){
			alert('<s:text name="empty.name"/>');
			hit=false;
		}
		
		if(type==''){
			alert('<s:text name="empty.surveyType"/>');
			hit=false;
		}
		
		if(dl==''){
			alert('<s:text name="empty.dataLevel"/>');
			hit=false;
		}
		
		var tableBody = jQuery("#sectLangPrefSurvey tr");
		var dataArr = new Array();
		
		
		
		$.each(tableBody, function(index, value) {
			var sectionName = jQuery(this).find(".sectionName").val();
			var sectionInfo = jQuery(this).find(".sectionInfo").val();
			var shortName = jQuery(this).find(".shortName").val();
			var lang=jQuery(this).find(".lang").val();
			var type=jQuery(this).find(".type").val();
			dataArr.push({name:sectionName,info:sectionInfo,shortName:shortName,lang:lang,type:type});
		});
		var preferenceJSON = JSON.stringify(dataArr);
		console.log(preferenceJSON);
		if(hit){
			   $.getJSON('surveyMaster_populateClone.action?id='+$('#idSent').val()+'&surveyName='+val+'&surveyType='+type+'&preferenceJSON='+preferenceJSON+'&dataLevelId='+dl,function(data){
          			if(data.maps=='success'){
          				alert('<s:text name="save.success" />');
	            		$('#detail').trigger('reloadGrid');
	            		disableKMLValuesAlert();
          			}else {
          				alert(data.errorMsg);
	            		//disableKMLValuesAlert();
          			}
           			 
           		});
		}
		
	}
	
</script>
	<div class="appContentWrapper marginBottom">
<sec:authorize ifAllGranted="profile.surveyMaster.create">
	<input type="BUTTON" id="add" value="<s:text name="create.button"/>" onclick="document.createform.submit()" class="btn btn-sts"/>
</sec:authorize>
<div id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
<s:select class="hide" list="languageList" id="langList"/>
</div>
<s:form name="updateform" action="surveyMaster_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
</s:form>

<s:form name="createform" action="surveyMaster_create">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
</s:form>

<s:form id="cloneForm">
	<div id="restartAlert2" class="modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">

					<button type="button" id="model-close-btn"
						onclick="disableKMLValuesAlert();" class="close"
						data-dismiss="modal">&times;</button>
					<h4 style="border-bottom: solid 1px #567304;">
							<s:text name="clone" />
						</h4>
				</div>
				<s:hidden id="idSent" />
				<div class="modal-body">
					<div class="modal-form">
						<div class="modal-form-item surveyMasterName">
							<label for="txt"><s:text name="surveyMaster.name" /></label>


							<s:textfield name="surveyMaster.name" id="name"
								class="form-control" maxlength="150" />
							<sup style="color: red;">*</sup>

						</div>
						
						<div class="modal-form-item dataLevel">
							<label for="txt"><s:text name="surveyMaster.section.dataLevel" /></label>


							<s:select listKey="key" listValue="value" list="dataLevelMap"
								name="surveyMaster.dataLevel.code" cssClass="form-control" headerKey="-1"
								headerValue="%{getText('txt.select')}" id="dataLevel"	/>
							<sup style="color: red;">*</sup>

						</div>
						
			
						<div class="modal-form-item surveyMasterSurveyType">

							<label for="txt"><s:text name="surveyMaster.surveyType" /></label>

								<s:select listKey="key" listValue="value" id="type"
									list="surveyTypes" name="surveyMaster.surveyType.id"
									headerKey="-1"
									class="form-control" headerValue="%{getText('selectName')}" />
								<sup style="color: red;">*</sup><label
									style="color: red; size: 20px;" id="surveyTypeWarning"
									class="hide"><s:text name="surveyTypeWarningText" /></label>							
						</div>
						<div class="modal-form-item surveyMasterSurveyType">
						<table class="table" >
						<tr>
						<th><s:text name="lang.code" /></th>
						<th><s:text name="lang.name" /></th>
						<th>	<s:property value="surveyMaster.languagePreferenceList"/></th>
					</tr>
				<tbody id="sectLangPrefSurvey"></tbody>
					</table>
</div>
						<div class="modal-form-item">

							<input type="button" id="restart" class="btn btn-sts"
								value="<s:text name="clone"/>" onclick="populateClone()" />

						</div>

					</div>
				</div>
			</div>

		</div>
	</div>
</s:form>