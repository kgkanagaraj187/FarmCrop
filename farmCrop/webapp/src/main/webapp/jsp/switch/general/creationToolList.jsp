<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="swithlayout">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" href="bootstrap.min.css">
<script src="jquery.min.js"></script>
<script src="bootstrap.min.js"></script>
<script src="js/bootpopup.js"></script>
<script src="js/bootpopup.min.js"></script>

</head>

<body>
<style>

/* td, th {
	padding: 20px
} */
</style>
<script type="text/javascript">
var optionTransfer_List_div_flag;
var sectionCode_forList_op;
var dependencyKeyArray = new Array();
	$(document).ready(function() {
		
		$("#getList_fieldTab").hide();
		//section grid 
		
	jQuery("#detail").jqGrid(
	{
	   	url:'creationTool_dataForSectionGrid.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   	editurl:'creationTool_updateSection.action',
	   	colNames:[
					
	  		   	 
	  		      '<s:text name="Section code"/>',
	  			  '<s:text name="Section Name"/>',
	  			  '<s:text name="Actions"/>'
	  			
	      	 ],
	   	colModel:[						


			{name:'sectionCode',index:'sectionCode',width:125,sortable:true,search:false},
	   		
	   		{name:'dsc.sectionName',index:'dsc.sectionName',width:125,sortable:true,search:true,editable:true},
	   		{name:'act',index:'act',width:40,sortable:false,formatter: "actions",search:false,
                formatoptions: {
                    keys: true, 
                    
                    delOptions: { 
                    	url: 'creationTool_deleteSection.action',
                    	afterShowForm:function ($form) {
                    		 
                    	    $form.closest('div.ui-jqdialog').position({
                    	        my: "center",
                    	        of: $("#detail").closest('div.ui-jqgrid')
                    	    });
                    	}

       					 
            
                    },
                   
                }
	    	 }
	   		
	   	//	{name:'bodStatus',index:'bodStatus',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: '-1:<s:text name="filter.allStatus"/>;1:<s:text name="bodStatus1"/>;0:<s:text name="bodStatus0"/>' }}
	   		
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
		  
		}, */		
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

 	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
	
	jQuery("#detail").setGridWidth($("#baseDiv").width());
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
		
		
		//Field related
		populateSectionDropDown_fieldTab();	
		populateCatalogueTypezDropDown_fieldTab();
		$("#componentTypesDiv").hide();
		hideFields_fieldTab();
		document.getElementById("secondRadioBtn_enableParent").checked=true;
		
	});		
	

	function cancelFormSubmit() {
		$("#cancelForm").submit(); 	//this also working
		
	}
	
	function cancelFormSubmit_section() {
		$("#cancelForm_section").submit();		//this also working
		
	}
	
	
	//section related
	
	function sectionFormSubmit() {
		var sectionName2=$("#sectionName").val()
		if (sectionName2 !="" ) {
			//$("#sectionForm").submit();
			$.post("creationTool_saveSection",{sectionName:sectionName2},function(result){
				jQuery("#detail").jqGrid('setGridParam',{url:"creationTool_dataForSectionGrid.action?",page:1,mtype: 'POST'}).trigger('reloadGrid');
				$("#sectionName").val("");
				populateSectionDropDown_fieldTab();
			});
		} else {
			alert("Please enter the section name");
		}
	}
	
	//field related
	
	function populateSectionDropDown_fieldTab(){
		jQuery.post("creationTool_populateSection.action",{},function(result){
			insertOptions("sectionDropDown_fieldTab",jQuery.parseJSON(result));
		});
	}
	
	function populateCatalogueTypezDropDown_fieldTab(){
		jQuery.post("creationTool_getCatalogueTypezFromFarmCatalogueMaster.action",{},function(result){
			insertOptionsWithNoDefaultValue("catalogueTypezDropDown_fieldTab",jQuery.parseJSON(result));
			insertOptionsWithNoDefaultValue("catalogueValuesDropDown_fieldTab",jQuery.parseJSON(result));
		});
		
	}
	
	function showComponentTypesDiv(){
		$("#componentTypesDiv").show();
	}
	
	function hideFields_fieldTab(){
		$("#componentTypesDiv").hide();
		$("#getComponentName_fieldTab").hide();
		$("#getComponentMaxLen_fieldTab").hide();
		$("#getComponentDataFormat_fieldTab").hide();
		$("#getIsMobileAvl_fieldTab").hide();
		$("#getIsReportAvl_fieldTab").hide();
		$("#getMandatory_fieldTab").hide();
		$("#getCatalogueType_fieldTab").hide();
		$("#getCatalogueTypeDropDown_fieldTab").hide();
		$("#getCatalogueValuesDropDown_fieldTab").hide();
		$("#optionTransfer_fieldTab").hide();
		$("#optionTransfer_forList_div").hide();
		
		$("#buttonDiv1_fieldTab").hide();
		$("#getComponentValidation_fieldTab").hide();
		$("#getParentFieldValues_fieldTab").hide();
		$("#getEnableParent_fieldTab").hide();
		
		//formula
		$("#getFieldsListDropDown_fieldTab").hide();
		$("#getExpressionDropDown_fieldTab").hide();
		$("#getControlsOfFormula_fieldTab").hide();
		$("#getFormulaLabel_fieldTab").hide();
		//list
		
		$("#listTypesDiv").hide();
		
	}
	
	function showComponentMandatoryFields(componentType){
		$("#selectComponentTypeBtn").show();
		$("#saveButton").show();
		switch (Number(componentType)) {
		
		case 1:
				hideFields_fieldTab();
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "Text Box";
				$("#getComponentName_fieldTab").show();
				$("#getComponentMaxLen_fieldTab").show();
				$("#getComponentValidation_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("1");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				
				//$("#getComponentDataFormat_fieldTab").show();
				//$("#getIsReportAvl_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show();
				break;
		    case 2:
		    	hideFields_fieldTab();
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "Radio Button";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				defaultCatalogueTypeDropDown_fieldTab();				//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("2");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show(); */
		        break;
		    case 3:
		    	hideFields_fieldTab();
			    document.getElementById('selectedComponentType_fieldTab').innerHTML = "Date Picker";
			    $("#getComponentName_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("3");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show();
				$("#getCatalogueType_fieldTab").show(); */
		        break;
		    case 4:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Drop-Down";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				defaultCatalogueTypeDropDown_fieldTab();		//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("4");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show(); */
				break;
		    case 5:
		    	hideFields_fieldTab();
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "Text Area";
				$("#getComponentName_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show(); */
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#getComponentValidation_fieldTab").show();
				$("#componentType_fieldTab").val("5");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show();
				$("#getCatalogueType_fieldTab").show(); */
		        break;
		    case  6:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Check Box";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				defaultCatalogueTypeDropDown_fieldTab();		//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("6");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show(); */
				break;
		    case  7:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Label (Formula)";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#componentType_fieldTab").val("7");
				populateFieldsListDropDown_fieldTab();
				$("#getFieldsListDropDown_fieldTab").show();
				$("#getExpressionDropDown_fieldTab").show();
				$("#getControlsOfFormula_fieldTab").show();
				$("#getFormulaLabel_fieldTab").show();
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				break;
		    case  8: 
		    	hideFields_fieldTab();
		    	populateCheckAvailableFieldsForList();
		    	optionTransfer_List_div_flag = "List";
		    	
		    	break;
		    case  9:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Drop-Down Multiple";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				defaultCatalogueTypeDropDown_fieldTab();		//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("9");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				//$("#getComponentMaxLen_fieldTab").show();
				//$("#getComponentDataFormat_fieldTab").show();
				//$("#getIsReportAvl_fieldTab").show();
				break;
		    case  10:
		    	hideFields_fieldTab();
		    	$("#getList_fieldTab").show();
		    	$("#list_btn_components").hide();
		    	$("#selectComponentTypeBtn").hide();
		    	$("#buttonDiv1_fieldTab").show();
		    	$("#saveButton").hide();
				break;
		    case 11:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Audio";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("11");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				break;
		    case 12:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Photo";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("12");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				break;
		    case 13:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Video";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("13");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				break;
		    case 14:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Label";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("14");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
			break;
		    default:
		    	hideFields_fieldTab();
		    }
		}
	
	function showComponentsDivForList(){
		$("#selectComponentTypeBtn").show();
		
	}
	
	function isMobileAvl_fieldTab(val) {
		
		if (val.value== "yes") {
			$("#mobileAvl_fieldTab").val("1");
		}else{
			$("#mobileAvl_fieldTab").val("0");
		}
		
	}
	
	function isReportAvl_fieldTab(val) {
		if (val.value== "yes") {
			$("#reportAvl_fieldTab").val("1");
		}else{
			$("#reportAvl_fieldTab").val("0");
		}
	}
	
	function isMandatory_fieldTab(val) {
		if (val.value== "yes") {
			$("#mandatoryOrNot_fieldTab").val("1");
		}else{
			$("#mandatoryOrNot_fieldTab").val("0");
		}
		
	}
	
	/* function chooseCatalogueTypeDropDown_fieldTab(val) {			//use this function whenever  you need both catalogue types and values
		if (val.value=="type") {
			$("#getCatalogueValuesDropDown_fieldTab").hide();
			$("#optionTransfer_fieldTab").hide();
			document.getElementById("optionTransfer2_fieldTab").options.length=0;
			$("#getCatalogueTypeDropDown_fieldTab").show();
			$("#buttonDiv1_fieldTab").show();
		}
		if (val.value=="value") {
			$("#getCatalogueTypeDropDown_fieldTab").hide();
			document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
			$("#getCatalogueValuesDropDown_fieldTab").show();
		}
		
	} */ 
	
	function defaultCatalogueTypeDropDown_fieldTab(){	// alternate function for choosing catalogue types or values by radio button
		$("#getCatalogueValuesDropDown_fieldTab").hide();
		$("#optionTransfer_fieldTab").hide();
		document.getElementById("optionTransfer2_fieldTab").options.length=0;
		$("#getCatalogueTypeDropDown_fieldTab").show();
		$("#buttonDiv1_fieldTab").show();
	}
	
	function populateCatalogueValuesList() {
		var types=""+$("#catalogueValuesDropDown_fieldTab").val();
	
		 $.post("creationTool_populateCatalogueValuesByType",{selectedType:types},function(result){
			 insertOptionsWithNoDefaultValue("optionTransfer1_fieldTab",jQuery.parseJSON(result));
		}); 
		
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="RemoveAll"/>']")
				.addClass("btn btn-warning");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="Add"/>']").addClass(
				"btn btn-small btn-success fa fa-step-forward");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="Remove"/>']")
				.addClass("btn btn-danger");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="AddAll"/>']")
				.addClass("btn btn-sts");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="v"/>']")
				.addClass("btn btn-warning");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="^"/>']")
				.addClass("btn btn-success");
		$("#buttonDiv1_fieldTab").hide();
		$("#optionTransfer_fieldTab").show();
	}

	function insertOptionsWithNoDefaultValue(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		 addOption(document.getElementById(ctrlName), "Select", "");
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}

	function componentFormSubmit() {
	
		
		if (document.getElementById('optionTransfer2_fieldTab') != null) {
			document.getElementById('optionTransfer2_fieldTab').multiple = true; //to enable all option to be selected
			for (var x = 0; x < document.getElementById('optionTransfer2_fieldTab').options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById('optionTransfer2_fieldTab').options[x].selected = true;
				$("#accessType_fieldTab").val("2");
			}//select all option when u click save button
		}
		
		
		var catalogueType = $("#catalogueTypezDropDown_fieldTab").val();
		
		if(catalogueType != null && catalogueType != ""){
			$("#accessType_fieldTab").val("1");
		}
	
		var componentType = $("#componentType_fieldTab").val();
		
		var falgForParentDependentField = true;
		var parent_dependency_id = $("#parentFieldId").val();
		var parent_dependency_key = $("#parentFieldValues_fieldTab").val();
		
		if(!isEmpty(parent_dependency_id)){
			if(!isEmpty(parent_dependency_key)){
				falgForParentDependentField = true;
				$("#parentDependencyKey_fieldTab").val(parent_dependency_key);
				$("#parentDependentId_fieldTab").val(parent_dependency_id);
			}else{
				alert("Please select the parent dependency key");
				falgForParentDependentField = false;
			}
		}else{
			falgForParentDependentField = true;
		}
		
		if(falgForParentDependentField){
			switch (Number(componentType)) {
			
			case 1:
					var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var maxLen = $("#componentMaxLen").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var validation = $("#validationDropDown").val();
					if((section != " ") && (componentName != "") && (maxLen != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && validation != ""){
						$("#componentForm").submit();
						 
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
			    case 2:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					//if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){
						if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#componentForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#componentForm").submit();
						}
							
						
					}else{
						alert("Please enter all the mandatory fields")
					}
					
			        break;
			    case 3:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var data_format = $("#componentDataFormat").val();
					if((section != " ") && (componentName != "") && (data_format != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true)){
						$("#componentForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
			        break;
			    case 4:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var maxLen = $("#componentMaxLen").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					//if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != "")) ){
						if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != "")) ){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#componentForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#componentForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
			    case 5:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					/* var maxLen = $("#componentMaxLen").val(); */
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var validation = $("#validationDropDown").val();
					
					if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && validation != ""){
						$("#componentForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
			        break;
			    case  6:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					//if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){
					 if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){	
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#componentForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#componentForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
				case 7:
					var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var formula = $("#formulaLabel").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					$("#validationDropDown").val('4');
					var temp = formula;
					for(i=0;i<temp.length;i++){
						if(temp.includes("{") || temp.includes("}") ){
							var start = temp.indexOf("{");
							if(temp.indexOf("}") != -1){
								var end  =  temp.indexOf("}");
							}
							end = (end+1) - start;
							temp = temp.replace(temp.substr(start,end), getRandomInt(1,1000));
						}
					}
					try {
						eval(temp);
						if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (formula != "")){
							$("#componentForm").submit(); 
						}else{
							 alert("Please enter all the mandatory fields");
						 }
					}
					catch(err) {
					    alert("Please Enter Valid Formula");
					}
					
					
					break;
			    case 8: 
			    	var section = $("#sectionDropDown_fieldTab").val();
			    	var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					/*var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked'); */
					
					if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) ){
						$("#componentForm").submit();
						
					}else{
						alert("Please enter all the mandatory fields")
					}
			    	break;
			    	
			    case  9:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					//if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){
					if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#componentForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#componentForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
					
				case 11:
						
						var section = $("#sectionDropDown_fieldTab").val();
						var componentName = $("#componentName").val();
						var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
						var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
						var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
						var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
						if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) ){
							$("#componentForm").submit();
							 
						}else{
							alert("Please enter all the mandatory fields")
						}
						break;
					
				case 12:
						
					var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) ){
						$("#componentForm").submit();
						 
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;		
			
				case 13:
					
					var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) ){
						$("#componentForm").submit();
						 
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
					
				case 14:
					
					var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mobile').prop('checked');
					var second_mobile = $('#secondRadioBtn_mobile').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) ){
						$("#componentForm").submit();
						 
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;		
			
			    default:
			    	hideFields_fieldTab();
			    }
		}
		
	
		
	}
	
	// list
	
	function showlistTypesDiv(){
		hideFields_fieldTab();
		$("#listTypesDiv").show();
	}
	
	function populateListDropDown(obj){
		
		/* jQuery.post("creationTool_getListComponents.action",{sectionCode:obj.value},function(result){
			insertOptions("listDropDown_listTab",jQuery.parseJSON(result));
		}); */
		display_optionTransfer_forList_div(obj.value);
	}
	
	function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
	
	function populateFieldsListDropDown_fieldTab(){
		jQuery.post("creationTool_getFieldsListForFormula.action",{},function(result){
			insertOptions("fieldsListDropDown_fieldTab",jQuery.parseJSON(result));
		});
	}
	
	function generateFormula(){
		var componentCode = document.getElementById('fieldsListDropDown_fieldTab').value;
		if(!isEmpty(componentCode)){
			componentCode = " {"+componentCode+"}";
		}
		
		var expression = " "+document.getElementById('expressionDropDown_fieldTab').value;
		var dependencyKey = "";
		
		if(!isEmpty(componentCode)){
			var temp = componentCode.replace(/{/g , "");
			temp = temp.replace(/}/g , "");
			dependencyKeyArray.push(temp);
		}
		
		for (var i = 0; i < dependencyKeyArray.length; i++) {
			if(i == dependencyKeyArray.length-1){
        	   dependencyKey = dependencyKey+dependencyKeyArray[i];
           }else{
        	   dependencyKey = dependencyKey+dependencyKeyArray[i]+",";
           }
        }
		$("#dependencyKey_fieldTab").val(dependencyKey); 
		
		/* var dependencyKey = $("#dependencyKey_fieldTab").val();
		$("#dependencyKey_fieldTab").val(dependencyKey+componentCode+);  */
		
		var formula = $("#formulaLabel").val(); 
		formula = formula.trim()+componentCode+expression;
		
		var idsArray=['fieldsListDropDown_fieldTab','expressionDropDown_fieldTab'];
		resetSelect2(idsArray);
		/* document.getElementById('fieldsListDropDown_fieldTab').getElementsByTagName('option')[0].selected = 'selected';
		document.getElementById('expressionDropDown_fieldTab').getElementsByTagName('option')[0].selected = 'selected'; */
		if(!isEmpty(formula)){
		$("#formulaLabel").val(formula);
		}
	}
	
	function clearFormulaLabel(){
		$("#formulaLabel").val("");
		dependencyKeyArray.length = 0;
	}
	
	function populateCheckAvailableFieldsForList(){
		if(sectionCode_forList_op != undefined){
		jQuery.post("creationTool_listDynamicFieldsBySectionCode.action",{sectionCode:sectionCode_forList_op},function(result){
			var availableFields = jQuery.parseJSON(result);
			if(!isEmpty(availableFields)){
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "List";
		    	$("#getComponentName_fieldTab").show();
				//$("#getComponentMaxLen_fieldTab").show();
				//$("#getComponentDataFormat_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				//$("#getIsReportAvl_fieldTab").show();
				//$("#getMandatory_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("8");
				$("#buttonDiv1_fieldTab").hide();
				
				if(sectionCode_forList_op != undefined){
					display_optionTransfer_forList_div(sectionCode_forList_op);
				}else{
					alert("Please select the Section")
				}
				
			}else{
				alert("Please add atleast one field in selected section before create list")
			}
		});
		}else{
			alert("Please select the Section")
		}
	}
	
	function getRandomInt(min, max) {
		  return Math.floor(Math.random() * (max - min + 1)) + min;
		}
	/* for list  */
	function styleOfOptionTransferButtons(){
		$("#optionTransfer_forList_ot").find(
				"input[type='button'][value='<s:text name="RemoveAll"/>']")
				.addClass("btn btn-warning");
		$("#optionTransfer_forList_ot").find(
				"input[type='button'][value='<s:text name="Add"/>']").addClass(
				"btn btn-small btn-success fa fa-step-forward");
		$("#optionTransfer_forList_ot").find(
				"input[type='button'][value='<s:text name="Remove"/>']")
				.addClass("btn btn-danger");
		$("#optionTransfer_forList_ot").find(
				"input[type='button'][value='<s:text name="AddAll"/>']")
				.addClass("btn btn-sts");
		$("#optionTransfer_forList_ot").find(
				"input[type='button'][value='<s:text name="v"/>']")
				.addClass("btn btn-warning");
		$("#optionTransfer_forList_ot").find(
				"input[type='button'][value='<s:text name="^"/>']")
				.addClass("btn btn-success");
	}
	function availableFieldsForList(section_code){
		
	
		jQuery.post("creationTool_listDynamicFieldsBySectionCode.action", {sectionCode:section_code},function(result){
			var availableFields = jQuery.parseJSON(result);
			if(!isEmpty(availableFields)){
			insertOptionsWithNoDefaultValue_forList("optionTransfer_forList_ot",jQuery.parseJSON(result));
			$("#optionTransfer_forList_div").show();
			}else{
				alert("Please add atleast one field before create list");
				$("#optionTransfer_forList_div").hide();
			}
		});
	}
	
	function insertOptionsWithNoDefaultValue_forList(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}
	
	function updateFieldsReferenceId() {
		
		setOptionValues('optionTransfer2__forList_ot');
		
		componentFormSubmit(); 
		if (document.getElementById('optionTransfer2__forList_ot') != null) {
			document.getElementById('optionTransfer2__forList_ot').multiple = true; //to enable all option to be selected
			for (var x = 0; x < document.getElementById('optionTransfer2__forList_ot').options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById('optionTransfer2__forList_ot').options[x].selected = true;
			}//select all option when u click save button
		}
		
		/* 
		var selected_Fields = $("#optionTransfer2__forList_ot").val();
		selected_Fields = selected_Fields.toString();
		
		$.post("creationTool_updateFieldsReferenceId.action",{selList:selected_Fields},function(){
			$("#cancelForm").submit(); 
		}); */
	}
	

	function setOptionValues(id) {
	
		if (document.getElementById(id) != null) {
			document.getElementById(id).multiple = true; //to enable all option to be selected
			for (var x = 0; x < document.getElementById(id).options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById(id).options[x].selected = true;
			}//select all option when u click save button
		}
	}
	
	function display_optionTransfer_forList_div(section_code){
		sectionCode_forList_op = section_code;
		if(optionTransfer_List_div_flag == "List"){
			styleOfOptionTransferButtons();
			availableFieldsForList(section_code);
			$("#optionTransfer_forList_div").show();
		}
	}
	
	function populateCatalogueValuesOfSelectedParentField(parentFieldId){
		if(!isEmpty(parentFieldId)){
			jQuery.post("creationTool_populateCatalogueValuesOfSelectedParentField.action",{parentFieldId:parentFieldId},function(result){
				insertOptions("parentFieldValues_fieldTab",jQuery.parseJSON(result));
			});
			$("#getParentFieldValues_fieldTab").show();
		}else{
			$("#getParentFieldValues_fieldTab").hide();
		}
	}
	
	function populateParentRelatedFields_fieldTab(val){
		
		if(val.value == "enable"){
			
			var elementExists = document.getElementById("parentFieldId");
			if(elementExists == null){
				createDropDown();
			}
			
		}else{
			jQuery('#getParentFields_fieldTab').html('');
			jQuery('#getParentFields_fieldTab').removeClass("flexform-item");
			$("#getParentFieldValues_fieldTab").hide();
		
		}
	}
	
	function createDropDown() {
		$("#getParentFields_fieldTab").addClass("flexform-item");
		var className = "select2";
		
		var select = $('<select/>').attr({
			class : className,
			id : 'parentFieldId',
			name : 'dfc.parentDepen.id',
			onchange : 'populateCatalogueValuesOfSelectedParentField(this.value)'
		});

		
		var option = "<option value=''>Select </option>";
		$(select).append(option);
		
		jQuery.post("creationTool_getParentFieldsList_ForEditDropDown.action",{},function(result){
		var values =	jQuery.parseJSON(result);
			$.each(values, function(index, value) {
					var option = "<option value='" + value.id + "'>"+ value.name + "</option>";
					$(select).append(option);
			});
		});
		
		var label = $("<label>").text('Parent');
		$("#getParentFields_fieldTab").append(label);
		var form_element = $("<div/>").addClass("form-element");
		form_element.append(select);
		$("#getParentFields_fieldTab").append(form_element);
	}
	
	
	
	/*for list  */
</script>			

<!--section grid  -->
<s:form name="updateform" action="creationTool_detailForSectionGrid">
		 <s:hidden name="id" /> 
		 <s:hidden  name="currentPage" /> 
</s:form>
<s:form id="cancelForm_section" action="creationTool_grid"></s:form>

<!-- common -->
<s:form id="cancelForm" action="creationTool_list"></s:form>


<div class="appContentWrapper marginBottom" >
 <ul class="nav nav-pills">
    <li class="active"><a data-toggle="pill" href="#sectionTab">Add Section</a></li>
    <li><a data-toggle="pill" href="#fieldTab">Add Fields</a></li>
    <!-- <li><a data-toggle="pill" href="#menu2">Menu 2</a></li> -->
  </ul>
 </div>

	<div class="tab-content">
		
		<div id="sectionTab" class="tab-pane fade in active">
			
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>Add Section</h2>
				</div>
				
				<s:form id="sectionForm" action="creationTool_saveSection" >
					<div class="flexform">
						
							<div class="flexform-item ">
								<label for="txt">section Name<sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="sectionName" name="dsc.sectionName"
										cssClass="form-control" maxlength="255" />
								</div>
							</div>
		
							<div class="flexform-item"  >
								<div class="form-element">
	
									<button type="button" class="save-btn btn btn-success"
										id="sectionSaveButton" onclick="sectionFormSubmit();">
										<font color="#FFFFFF"> <b><s:text name="save.button" /></b></font>
									</button>
	
									<button type="button" class="btnSrch btn btn-warning"
										id="cancelButton_section" onclick="cancelFormSubmit_section();">
										<font color="#FFFFFF"> <b><s:text
													name="cancel.button" /></b></font>
									</button>
	
								</div>
							</div>

					</div>
				</s:form>
				
			</div>
			
			<div class="appContentWrapper ">
				<div style="width: 99%;" id="baseDiv">
					<table id="detail"></table>
					<div id="pagerForDetail"></div>
				</div>
			</div>	
			
			
		</div>
		
		<!-- Field Tab -->
		
		<div id="fieldTab" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>Add Fields</h2>
				</div>
				
				<s:form id="componentForm" action="creationTool_saveField" >
					<div class="flexform">

						<div class="flexform-item">
							<label for="txt">Section<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<!-- <select id="sectionDropDown_fieldTab" name="dfc.dynamicSectionConfig.sectionCode" onchange="populateListDropDown(this);"></select> -->
								<s:select id="sectionDropDown_fieldTab" name="dfc.dynamicSectionConfig.sectionCode" onchange="populateListDropDown(this);" headerValue="%{getText('txt.select')}"  headerKey=""  cssClass="form-control  select2" list="{}"/>
							</div>
						</div>
						
						
						<div class="flexform-item" id="getList_fieldTab">
							<label for="txt">Choose List<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<!-- <select id="listDropDown_listTab" name="dfc.referenceId" onchange="showComponentsDivForList();"></select> -->
								<s:select id="listDropDown_listTab" name="dfc.referenceId" onchange="showComponentsDivForList();" headerValue="%{getText('txt.select')}"  headerKey=""  cssClass="form-control  select2" list="{}"/>
							</div>
						</div>
						
						<div class="flexform-item" id="selectComponentTypeBtn" >
							<label for="txt">Component Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<button type="button" class="save-btn btn btn-success"
									cssClass="form-control" onclick="showComponentTypesDiv();">
									<font color="#FFFFFF"> <b><s:text name="Select" /></b></font>
								</button>
								<p id="selectedComponentType_fieldTab"></p>
							</div>
						</div>
						
						
						
						<div class="flexform-item" id="getComponentName_fieldTab">
							<label for="txt">Name<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="componentName" name="dfc.componentName" cssClass="form-control" maxlength="255" />
							</div>
						</div>  
						
						<div class="flexform-item" id="getComponentValidation_fieldTab">
							<label for="txt">Validation<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select headerKey="" headerValue="Select" id="validationDropDown" cssClass="form-control  select2"
									list="#{'1':'Alphabet', '2':'Number', '3':'Email', '4':'Decimal', '5':'AlphaNumeric'}"
									name="dfc.validation" value="0" />
							</div>
						</div>
						
						<div class="flexform-item" id="getComponentMaxLen_fieldTab">
							<label for="txt">Max Length<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="componentMaxLen" name="dfc.componentMaxLength"
									cssClass="form-control" maxlength="45" onkeypress="return isNumber(event)" />
							</div>
						</div>

						<div class="flexform-item" id="getComponentDataFormat_fieldTab">
							<label for="txt">Date Format<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="componentDataFormat" name="dfc.dataFormat"
									cssClass="form-control" maxlength="45" />
							</div>
						</div>

						<div class="flexform-item" id="getIsMobileAvl_fieldTab">
							<label for="txt">Is Mobile Available<sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_mobile" type="radio"  name="mobile" value="yes" onclick="isMobileAvl_fieldTab(this);"> Yes<br> 
								<input id="secondRadioBtn_mobile" type="radio" name="mobile" value="no" onclick="isMobileAvl_fieldTab(this); "> No<br>
							</div>
						</div>

						<div class="flexform-item" id="getIsReportAvl_fieldTab">
							<label for="txt">Is report Available<sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_report" type="radio" name="report" value="yes" onclick="isReportAvl_fieldTab(this);"> Yes<br>
								 <input id="secondRadioBtn_report" type="radio" name="report" value="no" onclick="isReportAvl_fieldTab(this); "> No<br>
							</div>
						</div>

						<div class="flexform-item" id="getMandatory_fieldTab">
							<label for="txt">Mandatory<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_mandatory" type="radio" name="mandatory" value="yes" onclick="isMandatory_fieldTab(this);"> Yes<br> 
								<input id="secondRadioBtn_mandatory" type="radio" name="mandatory" value="no" onclick="isMandatory_fieldTab(this); "> No<br>
							</div>
						</div>

						<div class="flexform-item" id="getCatalogueType_fieldTab">
							<label for="txt">Catalogue Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_catalogueType" type="radio" name="catalogueType" value="type" onclick="chooseCatalogueTypeDropDown_fieldTab(this);">Catalogue Types<br> 
								<input id="secondRadioBtn_catalogueType" type="radio" name="catalogueType" value="value" onclick="chooseCatalogueTypeDropDown_fieldTab(this); ">Catalogue Values<br>
							</div>
						</div>	

						<div class="flexform-item" id="getCatalogueTypeDropDown_fieldTab">
							<label for="txt">Catalogue Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<!-- <select id="catalogueTypezDropDown_fieldTab"  style="width: 100%;" name="dfc.catalogueType"></select> -->
								<s:select id="catalogueTypezDropDown_fieldTab" headerValue="%{getText('txt.select')}"  headerKey=""  name="dfc.catalogueType"
								 cssClass="form-control  select2" list="{}"/>
							</div>
						</div>

						<div class="flexform-item" id="getCatalogueValuesDropDown_fieldTab">
							<label for="txt">Catalogue Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<select id="catalogueValuesDropDown_fieldTab" multiple="true" onchange="populateCatalogueValuesList();"></select>
							</div>
						</div>
						
						<div class="flexform-item" id="getFieldsListDropDown_fieldTab">
							<label for="txt">Fields<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="fieldsListDropDown_fieldTab" headerValue="%{getText('txt.select')}"  headerKey=""
								 cssClass="form-control  select2" list="{}"/>
							</div>
						</div>
						
						
						
						<div class="flexform-item" id="getExpressionDropDown_fieldTab">
							<label for="txt">Expression<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select headerKey="" headerValue="Select" id="expressionDropDown_fieldTab" cssClass="form-control  select2"
									list="#{'+':'+', '-':'-', '*':'*', '/':'/', '(':'(', ')':')', '%':'%'}"  />
							</div>
						</div>
						
						<div class="flexform-item" id="getControlsOfFormula_fieldTab">
							<label for="txt">Generate Formula<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<button type="button" class="save-btn btn btn-success"
									onclick="generateFormula();">
									<font color="#FFFFFF"> <b><s:text name="Generate" /></b>
									</font>
								</button>
								
								<button type="button" class="save-btn btn btn-success"
									onclick="clearFormulaLabel();">
									<font color="#FFFFFF"> <b><s:text name="Clear" /></b>
									</font>
								</button>
							</div>
						</div>
						
						<div class="flexform-item" id="getFormulaLabel_fieldTab">
							<label for="txt">Formula<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="formulaLabel" name="dfc.formula" cssClass="form-control"   />
								
							</div>
						</div>
						
					
						<s:if test="parentFieldsList.size() > 0 ">
						
						<div class="flexform-item" id="getEnableParent_fieldTab">
							<label for="txt">Enable Parent<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_enableParent" type="radio" name="enableParent" value="enable" onclick="populateParentRelatedFields_fieldTab(this);"> Enable<br> 
								<input id="secondRadioBtn_enableParent" type="radio" name="enableParent" value="disable" onchange="populateParentRelatedFields_fieldTab(this); "> Disable<br>
							
							</div>
						</div>
						<div id="getParentFields_fieldTab" ></div>
						
						<div class="flexform-item" id="getParentFieldValues_fieldTab">
							<label for="txt">Parent field Values<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="parentFieldValues_fieldTab" headerValue="%{getText('txt.select')}"  headerKey=""
								 cssClass="form-control  select2" list="{}"  multiple="true" />
							</div>
						</div>
						
						<%-- <div class="flexform-item" id="getParentFields_fieldTab">
							<label for="txt">Parent field<sup style="color: red;"></sup></label>
							<div class="form-element">
								 <s:select id="parentFields_fieldTab" cssClass="form-control"
								 list="parentFieldsList" name="dfc.parentDepen.id"
								headerKey="" headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="populateCatalogueValuesOfSelectedParentField(this.value)" />
								 
							</div>
						</div>
						
						<div class="flexform-item" id="getParentFieldValues_fieldTab">
							<label for="txt">Parent field Values<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="parentFieldValues_fieldTab" headerValue="%{getText('txt.select')}"  headerKey=""
								 cssClass="form-control  select2" list="{}"  multiple="true" />
							</div>
						</div> --%>
						</s:if>
						
					</div>

					<br>
					<div id="buttonDiv1_fieldTab">

						<button type="button" class="save-btn btn btn-success" id="saveButton"
							onclick="componentFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>

						<button type="button" class="btnSrch btn btn-warning"
							id="cancelButton" onclick="cancelFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b>
							</font>
						</button>

					</div>


					<br>
					<div class="appContentWrapper marginBottom" id="optionTransfer_fieldTab">

						<s:text name="RemoveAll" var="rmvall" />
						<s:text name="Remove" var="rmv" />
						<s:text name="Add" var="add" />
						<s:text name="AddAll" var="addall" />
						<s:optiontransferselect id="optionTransfer1_fieldTab"
							cssClass="form-control "
							cssStyle="width:500px;height:450px;overflow-x:auto;"
							doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
							doubleCssClass="form-control" buttonCssClass="optTrasel"
							allowSelectAll="false"
							buttonCssStyle="font-weight:bold!important;"
							allowUpDownOnLeft="true" labelposition="top"
							allowUpDownOnRight="true" name="selectedCatalogueCode" list="{}"
							leftTitle="<b>List of Catalogue values<b>"
							rightTitle="<b>Selected Catalogue values<b>"
							headerKey="headerKey" doubleName="selectedCatalogueCode"
							doubleId="optionTransfer2_fieldTab" doubleList="{}"
							doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
							addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
							addToRightLabel="%{add}" />

						<br>
						<div id="button">
							<div class="form-element">

								<button type="button" class="save-btn btn btn-success"
									onclick="componentFormSubmit();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>

								<button type="button" class="btnSrch btn btn-warning"
									id="cancelButton" onclick="cancelFormSubmit();">
									<font color="#FFFFFF"> <b><s:text
												name="cancel.button" /></b>
									</font>
								</button>

							</div>
						</div>

					</div>
					
					<!--for list  -->
					<div class="appContentWrapper marginBottom" id="optionTransfer_forList_div">

						<s:text name="RemoveAll" var="rmvall" />
						<s:text name="Remove" var="rmv" />
						<s:text name="Add" var="add" />
						<s:text name="AddAll" var="addall" />
						<s:optiontransferselect id="optionTransfer_forList_ot"
							cssClass="form-control "
							cssStyle="width:500px;height:450px;overflow-x:auto;"
							doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
							doubleCssClass="form-control" buttonCssClass="optTrasel"
							allowSelectAll="false"
							buttonCssStyle="font-weight:bold!important;"
							allowUpDownOnLeft="true" labelposition="top"
							allowUpDownOnRight="true" name="selectedFieldNames" list="{}"
							leftTitle="<b>List of Available Fields<b>"
							rightTitle="<b>Selected Fields<b>"
							headerKey="headerKey" doubleName="selectedFieldNames" 
							doubleId="optionTransfer2__forList_ot" doubleList="{}"
							doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
							addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
							addToRightLabel="%{add}"  />

						<br>
						<div id="button">
							<div class="form-element">

								<button type="button" class="save-btn btn btn-success"
									onclick="updateFieldsReferenceId();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>

								<button type="button" class="btnSrch btn btn-warning"
									id="cancelButton" onclick="cancelFormSubmit();">
									<font color="#FFFFFF"> <b><s:text
												name="cancel.button" /></b>
									</font>
								</button>

								

							</div>
						</div>

					</div>
					<!--for list  -->
					
					<s:hidden id="componentType_fieldTab" name="dfc.componentType" />
					<s:hidden id="accessType_fieldTab" name="dfc.accessType" />
					<s:hidden id="mobileAvl_fieldTab" name="dfc.isMobileAvail" />
					<s:hidden id="reportAvl_fieldTab" name="dfc.isReportAvail" />
					<s:hidden id="mandatoryOrNot_fieldTab" name="dfc.isRequired" />
					<s:hidden id="dependencyKey_fieldTab" name="dfc.dependencyKey" />
					
					<s:hidden id="parentDependencyKey_fieldTab" name="parentDependencyKey" />
					<s:hidden id="parentDependentId_fieldTab" name="parentDependentId" />
					
				</s:form>
			</div>

			<%-- <div class="appContentWrapper marginBottom" id="optionTransfer_fieldTab">

				<s:text name="RemoveAll" var="rmvall" />
				<s:text name="Remove" var="rmv" />
				<s:text name="Add" var="add" />
				<s:text name="AddAll" var="addall" />
				<s:optiontransferselect id="optionTransfer1"
					cssClass="form-control "
					cssStyle="width:500px;height:450px;overflow-x:auto;"
					doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
					doubleCssClass="form-control" buttonCssClass="optTrasel"
					allowSelectAll="false" buttonCssStyle="font-weight:bold!important;"
					allowUpDownOnLeft="false" labelposition="top"
					allowUpDownOnRight="false" name="selectedCatalogueCode" list="{}"
					leftTitle="<b>List of Catalogue values<b>"
					rightTitle="<b>Selected Catalogue values<b>" headerKey="headerKey"
					doubleName="selectedCatalogueCode" doubleId="optionTransfer2"
					doubleList="{}" doubleHeaderKey="doubleHeaderKey"
					addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
					addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />


				<div id="button">
					<div class="form-element">

						<button type="button" class="save-btn btn btn-success"
							onclick="componentFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>

						<button type="button" class="btnSrch btn btn-warning"
							id="cancelButton" onclick="cancelFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b>
							</font>
						</button>

					</div>
				</div>

			</div> --%>
			
			<div class="appContentWrapper marginBottom" id="componentTypesDiv" >
				<button id="textBoxButton" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('1');">
					<font color="#FFFFFF"> <b><s:text name="Text Box" /></b></font>
				</button>

				<button id="radioButton" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('2');">
					<font color="#FFFFFF"> <b><s:text name="Radio button" /></b></font>
				</button>

				<button id="datePicker" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('3');">
					<font color="#FFFFFF"> <b><s:text name="Date Picker" /></b></font>
				</button>

				<button id="dropDown" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('4');">
					<font color="#FFFFFF"> <b><s:text name="Drop down" /></b></font>
				</button>

				<button id="textArea" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('5');">
					<font color="#FFFFFF"> <b><s:text name="Text Area" /></b></font>
				</button>

				<button id="checkBox" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('6');">
					<font color="#FFFFFF"> <b><s:text name="Check Box" /></b></font>
				</button>
				
				<button id="label" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('7');">
					<font color="#FFFFFF"> <b><s:text name="Label (Formula)" /></b></font>
				</button>
				
				<button id="dropDownMultiple" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('9');">
					<font color="#FFFFFF"> <b><s:text
								name="Drop-down Multiple" /></b></font>
				</button>

				<button id="audioComponent" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('11');">
					<font color="#FFFFFF"> <b><s:text name="Audio" /></b></font>
				</button>
				
				<button id="photoComponent" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('12');">
					<font color="#FFFFFF"> <b><s:text name="Photo" /></b></font>
				</button>

				<%-- <button id="addButton" type="button" class="save-btn btn btn-success" cssClass="form-control" onclick="populateComponent('10');">
													<font color="#FFFFFF"> <b><s:text name="Add Button" /></b></font>
												</button> --%>

				<button id="list_btn_components" type="button" class="save-btn btn btn-success hide" 
					cssClass="form-control" onclick="showlistTypesDiv();">
					<font color="#FFFFFF"> <b><s:text name="List" /></b></font>
				</button>
				
				<button id="videoComponent" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('13');">
					<font color="#FFFFFF"> <b><s:text name="Video" /></b></font>
				</button>
				
				
				
			
				
				<button id="weatherInfo" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('14');">
					<font color="#FFFFFF"> <b><s:text name="Weather Info" /></b></font>
				</button>
				
				<button id="list" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('8');">
					<font color="#FFFFFF"> <b><s:text name="Create List" /></b></font>
				</button>
				
				
				
				
			</div>
			
			<div class="appContentWrapper marginBottom" id="listTypesDiv" >
				<button id="list" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('8');">
					<font color="#FFFFFF"> <b><s:text name="Create List" /></b></font>
				</button>
				
				<button id="list" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('10');">
					<font color="#FFFFFF"> <b><s:text name="Add fields" /></b></font>
				</button>
			</div>
			
			
			
		</div>
		<!-- <div id="menu2" class="tab-pane fade">
			<h3>Menu 2</h3>

		</div> -->
		
		
	</div>


</body>
</html>