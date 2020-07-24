<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>

<meta name="decorator" content="swithlayout">
</head>
<script src="plugins/form-builder.min.js"></script>
<script src="plugins/form-render.min.js"></script>
<script>
jQuery($ => {
	
	var options = {
			disableFields: ['autocomplete','access','header','hidden','paragraph','number'],
			actionButtons: ['save','clear'],
			controlPosition:'left',
			replaceFields: [
			    {
			      type: "select",
			      label: "Select",
			      className:"select2 form-control"
			     
			    }
			  ],
			fields:[{
				  label: 'Multiple Dropdown',
				  className:'select2 form-control',
						
				  attrs: {
				    type: 'mulitSelect'
				  }
				},{
					  label: 'List',
					  attrs: {
					    type: 'listF'
					  }
					}
				],
				templates:{
					mulitSelect: function(fieldData) {
						    return {
						      field: '<select  id="'+fieldData.name+'" multiple="true" class="form-control select2" />',
						      onRender: function() {
						        $(document.getElementById(fieldData.name)).rateYo({rating: 3.6});
						      }
						    };
						  },
						  listF: function(fieldData) {
							    return {
							      field: '<table class="table"><tr></tr></table>',
							      onRender: function() {
							        $(document.getElementById(fieldData.name)).rateYo({rating: 3.6});
							      }
							    };
							  }
						},
			dataType:'json',
			scrollToFieldOnAdd:true,
			stickyControls: {
				
				    enable:true,
				
				    offset: {
				
				      top: 5,
				
				      bottom:'auto',
				
				      right:'auto',
				
				    },
				
				  },

			/* Trigger befor adding the field */
			onAddField: function(fieldData,fieldId) {
				
			  },
			  /*Triggers after adding the field*/
			  typeUserEvents: {
	                select: {
	                    onadd: function(fld) {
	                      
	                    	 $(fld).select2();
	                    }
	                }
			  },
			i18n: {
		        override: {
		          'en-US': {
		        	  checkboxGroup:"Checkbox",
		        	  radioGroup:"Radio Button",
		        	  select:"Dropdown"
		          }}}

				};
	
	
	  $('.build-wrap').formBuilder(options);
	})
</script>
<body>
<div class="build-wrap"></div>

</body>