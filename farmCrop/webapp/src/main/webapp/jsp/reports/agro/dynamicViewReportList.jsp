<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<script src="plugins/openlayers/OpenLayers.js"></script>
<style><%@include file="/css/dynamicDt.css"%></style>
<head>
<meta name="decorator" content="swithlayout">

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
	
	
</head>

<body>

	<script type="text/javascript">
	var branchId='<s:property value="branchId"/>';
	var isSubGrid='<s:property value="isSubGrid"/>';
	var $overlay;
	var $modal;
	var $slider;
	var fetchType='';
	var map;
	var grparr=new Array();
	 var typez  = <%out.print("'" + request.getParameter("id") + "'");%>
	 var tenant='<s:property value="getCurrentTenantId()"/>';
	 var deFil='';
	 var action='';
	jQuery(document).ready(function(){
		
		 fetchType = '<s:property value="fetchType"/>';
		 deFil	= '<s:property value="defFilters"/>';
		 action='<s:property value="detailMethod"/>';
		$(".dateFilterApp").each(function(){
			$(this).daterangepicker(
					 {
					     
						 format: 'YYYY-MM-DD',
					    startDate:document.getElementById("hiddenStartDate").value,
					     endDate:document.getElementById("hiddenEndDate").value
					 });
			
			
			});
		
			 $('.applyBtn').click();
			 loadCustomPopup();
			 $("#dialog").hide();
		var colNames='<s:property value="mainGridCols"/>';
		var subColNames='<s:property value="subGridCols"/>';
		//onLoadValues();
		loadGrid();
		
		$("#reset").click(function(){
			var url = (window.location.href);
			window.location.href=url;
			$(".select2").val("");
			
		})	
		
	});
	
	jQuery("#generate").click( function() {
		reloadGrid();	
	});
	
	jQuery("#clear").click( function() {
		clear();
	});	
	var data = new Array();
	
	function reloadGrid(){	
		$(".dateFilterApp").each(function(){
			var d1=	jQuery(this).val();
			var d2= d1.split("-");
			//	alert(d1);
			var value1= d2[0];
			//alert(value1);
			var value2= d2[1];
			//alert(value2);
		document.getElementById("hiddenStartDate").value=value1;
		
		document.getElementById("hiddenEndDate").value=value2;
		});
		
	
		var startDate=new Date(document.getElementById("hiddenStartDate").value);
	//	alert(startDate);
		var endDate=new Date(document.getElementById("hiddenEndDate").value);
	//	alert(endDate);
		if (startDate>endDate){
			alert('<s:text name="date.range"/>');
		}else{
		jQuery("#detail").jqGrid('setGridParam',{url:"dynamicViewReport_detail.action",page:1}).trigger('reloadGrid');	
	}
		populateQuantity();
	}
	try {
		var fProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
		var tProjection = new OpenLayers.Projection("EPSG:900913");
		var url = window.location.href;
		var temp = url;
		for (var i = 0; i < 1; i++) {
			temp = temp.substring(0, temp.lastIndexOf('/'));
		}
		var href = temp;
		var coordinateImg = "red_placemarker.png";
		var iconImage = temp + '/img/' + coordinateImg;
	} catch (err) {

	}
	
	function loadGrid(){
		var gridColumnNames = new Array();
		var gridColumnModels = new Array();
		
		
		var subGridColumnNames = new Array();
		var subGridColumnModels = new Array();
		
		var colNames='<s:property value="mainGridCols"/>';
		var colModels='<s:property value="mainGridColNames"/>';
		var footCol='<s:property value="footerSumCols"/>';
		var footTot='<s:property value="footerTotCol"/>';
		
		var subColNames='<s:property value="subGridCols"/>';
		
		$(colNames.split("%")).each(function(k,val){
				if(!isEmpty(val)){
					var cols=val.split("#");
					gridColumnNames.push(cols[0]);
					//gridColumnModels.push({name: cols[0],width:cols[1],sortable: false,frozen:true});
				}
		});
		
		
		$(subColNames.split("%")).each(function(k,val){
			if(!isEmpty(val)){	
				var cols=val.split("#");
				subGridColumnNames.push(cols[0]);
				subGridColumnModels.push({name: cols[0],width:cols[2],align:cols[3],sortable: false,frozen:true});
			}
		});
		
		$(colModels.split("%")).each(function(k,val){
			if(!isEmpty(val)){
				var cols=val.split("#");
				gridColumnModels.push({name: cols[0],width:cols[2],align:cols[3],sortable: false,frozen:true});
			}
	});
		
		jQuery("#detail").jqGrid(
				{
					url:'dynamicViewReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	styleUI : 'Bootstrap',
				   	mtype: 'POST',
				   	postData:{				 
						  "filterList" : function(){
							  return getPostdata();
						  },
						  "id" :  function(){
							  return typez;
						  },
						  <s:if test='branchId==null'>
			              "branchIdParma":function(){
			            		return $("#branchIdParma").val();
				         },
				         </s:if>
				         <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						  "subBranchIdParma" : function(){
							  return document.getElementById("subBranchIdParam").value;
						  },
						  </s:if>
						},
				   	colNames:gridColumnNames,
				   	colModel:gridColumnModels,
				   	height: 301, 
				   	width: $("#baseDiv").width(),
				   	scrollOffset: 0,     
				   	sortname:'id',
				   	sortorder: "desc",
				   	rowNum:10,
				   	rowList : [10,25,50],
				   	viewrecords: true,
				   	
				   	<s:if test='isSubGrid==1'>
				   	subGrid: true,
					   	subGridOptions: {
						   "plusicon" : "glyphicon-plus-sign",
						   "minusicon" : "glyphicon-minus-sign",
						   "openicon" : "glyphicon-hand-right",
					   	},
					   	subGridRowExpanded: function(subgrid_id, row_id){
						   var subgrid_table_id, pager_id; 
						   subgrid_table_id = subgrid_id+"_t"; 
						   pager_id = "p_"+subgrid_table_id; 
						   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>"); //<div id='"+pager_id+"' class='scroll'></div>");
						   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
						   jQuery("#"+subgrid_table_id).jqGrid({
							   url:'dynamicViewReport_subGridDetail.action?id='+row_id,
							   pager: pager_id,
							   datatype: "json",	
							   colNames:subGridColumnNames,
							   colModel:subGridColumnModels,
							    sortname:'id',
							    height: '100%', 
							    sortorder: "desc",
							    scrollOffset: 0,
							    shrinkToFit:true,
							   // autowidth: true,
							    viewrecords: true
						   });
						   	jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
						    jQuery("#"+subgrid_id).parent().css("width","100%");
						    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
						    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
						    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
						    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
					   },	
				   	
					   </s:if>
				   	
				   	
				   	
				   	
				   	
				   	
				   	
				   	
				   	onSelectRow: function(id){ 
				   /* 	 if(tenant=="griffith"){
				    	document.updateform.id.value=id;
				    	var myGrid = $('#detail');
				    	var selectedRowData = myGrid.getRowData(id);
				    	//alert(JSON.stringify(selectedRowData));
				    	$("#batchNo").val(selectedRowData.batchNo);
				    	//$("#plotCode").val(selectedRowData.plotCode);
				    	$("#farmerId").val(selectedRowData.farmerCode);
				    	$("#season").val(selectedRowData.season);
						//alert(JSON.stringify());	 
				    	postDataSubmit();
				          document.updateform.submit(); 
				   	 } */
				   	 if(action!=null && action!="" ){	
				   		detailData(id);
				   	 }
					},	
				   	footerrow: true,
				    loadComplete: function(data){
						 var columnNames = footCol.split("#");
						 var options = {};
						 options[footTot] = "Total";
						 if (data.footersum != '' && data.footersum != undefined && !isEmpty(data.footersum) && JSON.stringify(data.footersum)!='{}') {
							 var footerss = data.footersum;
						  var $self = $(this); 
					      for (var i = 0; i < columnNames.length-1; i++) {
					            totArry= footerss[columnNames[i]];
					            if(fetchType=='2'){
					            	options[columnNames[i]] = parseFloat(totArry).toFixed(2);
					            }else{
								options[columnNames[i]] = totArry;
					      }
								$self.footerData('set', options);
					        }  
						 }else{
							  var $self = $(this); 
							  totArry="0";
						      for (var i = 0; i < columnNames.length-1; i++) {
						            options[columnNames[i]] = parseFloat(totArry).toFixed(2);
						            }
									$self.footerData('set', options);
						 }
					      
					 },
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        }
				});
				
				//jQuery("#detail").jqGrid('setFrozenColumns');
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
		
			    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
				jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
				
				var gh='<s:property value="groupHeader"/>';
				$(gh.split("#")).each(function(k,val){  
					if(!isEmpty(val)){
						var cols=val.split("~");
						grparr.push({
						startColumnName:cols[0],
						numberOfColumns:cols[1],
						titleText:cols[2]
						});
					}
				});
		 		jQuery("#detail").jqGrid('setGroupHeaders', {
 			    	useColSpanStyle: true,  			    	
					  groupHeaders:grparr				
				});		 
        	      
	}
	function getPostdata(){
		var postData = {};
		deFil='<s:property value="getDefaultFilter()"/>';
		
		if(deFil!="1"&&deFil.includes(",")){
			//alert("inn")
			var fields = deFil.split(',');
			deFil=1;
			fields.forEach(function (item, index) {
			  console.log(item, index);
			  //var f=item.split(':');
			 //jQuery("#form_"+f[0].replace('.','_')).val(f[1]); 
			 if(item.split(':')[2]==0){
			postData[item.split(':')[0].split('~')[0]] = "1~"+item.split(":")[1]+"~"+item.split(':')[0].split('~')[1];
			}else{
				jQuery("#form_"+item.split(':')[0].replace('.','_')).val(item.split(":")[1]).change(); 
			}
			 
			});
			deFil=1;
		}
		if(deFil!="1"){
			//postData[deFil.split(':')[0]] = " = '"+deFil.split(':')[1]+"'";
			//$('select[name^='+deFil.split(":")[0]+'] option[value='+deFil.split(":")[1]+']').attr("selected","selected");
			if(deFil.split(':')[2]==0){
			postData[deFil.split(':')[0].split('~')[0]] = "1~"+deFil.split(":")[1]+"~"+deFil.split(':')[0].split('~')[1];
			}else{
				jQuery("#form_"+deFil.split(':')[0].replace('.','_')).val(deFil.split(":")[1]).change(); 
			}
	
		deFil=1;
		}
		if(fetchType=='3'){
		$(".filterClass").each(function(){
			var name = $(this).attr('name');
			var value = $(this).val();
				if(value!=''){
				if($(this).hasClass("3")){
					postData[name] = " = '"+value+"'";
				}else if($(this).hasClass("4")){
					var dateVal  =value.split(" - ");
					postData[name] = " between  '"+ dateVal[0].trim() +"' and '"+ dateVal[1].trim()+" 23:59:59"+"'";
				}else if($(this).hasClass("1")){
					postData[name] = $(this).parent().find('#cond').val()+ " '"+value+"'";
				}else if($(this).hasClass("6")){
					postData[name] = " LIKE '%"+value+"%'";
				}else if($(this).hasClass("7")){
					postData[name] = " = FIND_IN_SET('"+value+"',"+name+")";
				}
				
			}
		});
		postData['branch_id'] = ' is not null';
		}else{
			$(".filterClass").each(function(){
				var name = $(this).attr('name');
				var value = $(this).val();
				if(value!=''){
					if($(this).hasClass("3")){
						postData[name.split('~')[0]] = "1~"+value+"~"+name.split('~')[1];
					}else if($(this).hasClass("4")){
						var dateVal  =value.split(" - ");
						postData[name] = "2~"+ dateVal[0].trim()+" 00:00:00" +"|"+ dateVal[1].trim()+" 23:59:59";
					}else if($(this).hasClass("6")){
						postData[name] = "8~"+value;
					}else if($(this).hasClass("10") && value!=null){
						postData[name.split('~')[0]] = "10~"+value;
					}
					
				}
			});
			postData['branchId'] = '9~null';
		}
		return JSON.stringify(postData);
	}

		function exportRecPDF(exportParams) {
			
			var dataa = {
				filtersList : exportParams
			};

			jQuery("#detail").jqGrid('excelExport', {
				url : 'dynamicViewReport_populateSingleRecordPDF.action?filtersList='+exportParams,
				page : 1,
				postData : dataa
			});

		}

		function exportXLS() {
			if (isDataAvailable("#detail")) {

				$("#form select").each(function() {
					var value = $(this).val();
					var name = this.name;
					if (!isEmpty(value)) {
						data.push({
							name : name,
							value : value
						});
					}
				});
				var json = JSON.stringify(data);
				var dataa = {
					filterList : json
				};

				jQuery("#detail").setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#detail").jqGrid('excelExport', {
					url : 'dynamicViewReport_populateXLS.action?',
					postData : dataa
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}
		
		function exportCSV() {
			if (isDataAvailable("#detail")) {

				$("#form select").each(function() {
					var value = $(this).val();
					var name = this.name;
					if (!isEmpty(value)) {
						data.push({
							name : name,
							value : value
						});
					}
				});
				var json = JSON.stringify(data);
				var dataa = {
					filterList : json
				};

				jQuery("#detail").setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#detail").jqGrid('excelExport', {
					url : 'dynamicViewReport_populateCSV.action?',
					postData : dataa
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}


		function search() {
		/* 	var dataa = {
				filterList : getPostdata()
			}; */
			jQuery("#detail").jqGrid('setGridParam', {
				url : 'dynamicViewReport_detail.action',
				page : 1,
				postData:{				 
					  "filterList" : function(){
						  return getPostdata();
					  },
				},
			}).trigger('reloadGrid');
		}

		function sumarValores(fcols) {
			var sumaHa = 0;
			var columnNames = fcols.split("#");
			$('#detail').jqGrid('footerData', 'set', {
				Items : 'TOTAL:'
			});
			var $self = $('#detail');
			for (var z = 0; z < columnNames.length - 1; z++) {
				var colN = columnNames[z];
				var x = '"' + colN + '"';
				a = $('#detail').jqGrid("getCol", x, false, "sum");
				$('#detail').jqGrid("footerData", "set", {
					invdate : "Total:",
					colN : a
				});
				// $self.jqGrid("footerData", "set", {invdate: "Total:", currentQty: sum9});
			}

		}

		function exportPDF() {
			if (isDataAvailable("#detail")) {

				$("#form select").each(function() {
					var value = $(this).val();
					var name = this.name;
					if (!isEmpty(value)) {
						data.push({
							name : name,
							value : value
						});
					}
				});
				var json = JSON.stringify(data);
				var dataa = {
					filterList : json
				};

				jQuery("#detail").setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#detail").jqGrid('excelExport', {
					url : 'dynamicViewReport_populatePDF.action?',
					postData : dataa
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}

		function enablePopup(head, cont) {

			$(window).scrollTop(0);
			$('body').css('overflow', 'hidden');
			$(".bjqs").empty();
			var heading = '';
			var contentWidth = '100%';
			if (head != '') {
				heading += '<div style="height:10%;"><p class="bjqs-caption">'
						+ head + '</p></div>';
				contentWidth = '92%';
			}
			var content = "<div style='width:100%;height:" + contentWidth
					+ "'>" + cont + "</div>";
			$(".bjqs").append('<li>' + heading + content + '</li>')
			$(".bjqs-controls").css({
				'display' : 'block'
			});
			$('#modalWin').css('margin-top', '-260px');
			$modal.show();
			$overlay.show();
			$('#banner-fade').bjqs({
				height : 450,
				width : 600,
				showmarkers : false,
				usecaptions : true,
				automatic : true,
				nexttext : '',
				prevtext : '',
				hoverpause : false

			});

		}

		function loadCustomPopup() {
			$overlay = $('<div id="modOverlay"></div>');
			$modal = $('<div id="modalWin" class="ui-body-c"></div>');
			$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
			$close = $('<button id="clsBtn" class="btnCls">X</button>');

			$modal.append($slider, $close);
			$('body').append($overlay);
			$('body').append($modal);
			$modal.hide();
			$overlay.hide();

			jQuery("#clsBtn").click(function() {
				$('#modalWin').css('margin-top', '-230px');
				$modal.hide();
				$overlay.hide();
				$('body').css('overflow', 'visible');
			});
		}

		function detailPopupDt(img) {
			jQuery("#html").val(img);
			$("#printHTML").submit();
			
/* 
			
			//window.location.href="cropSaleEntryReport_list.action";

			alert(img)
			var ids = img;
			$("#detailDataTitle").empty();
			$("#detailDataHead").empty();

			$("#detailDataBody").empty();
			if (ids != '') {
				var val = ids;
				var arg = val.split("~")[0].trim();
				var query = val.split("~")[2].trim();
				var queryHead= query.split("#")[0].trim();
				var queryDt= query.split("#")[1].trim();
				var headerDiv = val.split("~")[1].trim();
				var div = $('<div/>').attr({
					id : "jQueryGridTable",
				});
			
			}
			try {
				$.ajax({
					type : "POST",
					async : true,
					url : "dynamicViewReport_methodQuery.action",
					data : {
						query : queryHead,
						param : arg,
						id : typez
					},
					success : function(result) {
						var jsonValue = $.parseJSON(result);
						$("#detailDataBody").append(headerDiv);
						//document.getElementById("enableDetailPopup").click();
							  $.each(jsonValue, function(k, value) {
								
									$.each(value, function(a, val) {
										 $("#val"+a).html(val);
										// $("#cal"+a).html(val);
										//$(lcTr).find("td#val"+a).html(val);
									});
									
									  //$("#detailDT").append(lcTr);
								}); 
								 if($('#detailDT').length>0){
							 $.ajax({
									type : "POST",
									async : true,
									url : "dynamicViewReport_methodQuery.action",
									data : {
										query : queryDt,
										param : arg,
										id : typez
									},
									success : function(result) {
										var jsonValue = $.parseJSON(result);
										//$("#detailDataBody").append(headerDiv);
										var hit=false;
										var trr;
											 hit=true;
											  trr= $('#detailDT').find('tr:eq(1)').clone();
											  $('#detailDT tr:eq(1)').remove();
										
										 $.each(jsonValue, function(q, valuee) {
											var lcTr ;
											 if(hit){
												 lcTr = $(trr).clone();
												 $(lcTr).find("td").removeClass("footer");
											} 
											$.each(valuee, function(w, vall) {
												// $("#val"+a).html(val);
												$(lcTr).find("td#cal"+w).html(vall);
											});
											//$(lcTr).find("td").attr("id",'');
											  $("#detailDT").append(lcTr);
										}); 
										// $(trr).find("td").attr("id",'');
										 var tfoot =$('<tfoot/>');
										 tfoot.append(trr);
										  $("#detailDT").append(tfoot);
										var tables = $('#detailDT').DataTable({
												"searching": false,
												"paging": false,
												 "footerCallback" : function() {
													var api = this.api();
														$('.footer').each(
																function(val) {
																		$(this)
																			.html(api.column($(this).index(),{ search:'applied' }, {
																				page : 'all'
																			}).data().sum().toFixed(2));
																}); 
													}  
										});
										 $('#detailDT').addClass("dataTableTheme");
										console.log($('#tbDt').html());
										//jQuery("#html").val($('#tbDt').html());
										//jQuery("#printHTML").submit();
										/*  $('#detailDT').addClass("dataTableTheme");
										console.log($('#tbDt').html()); */ 
										/* var css_link = $("<link/>", {
									        rel: "stylesheet",
									        type: "text/css",
									        href: "css/dynamicDt.css"
									    }); */
										// var wnd = window.open("about:blank","_blank");
										//$(wnd.document.head).append('<style type="text/css">.dataTableTheme {border-collapse: separate;border: solid black 1px;border-radius: 6px;-moz-border-radius: 6px;table-layout: fixed;overflow: scroll;}.dataTableTheme>thead>tr>th {background: #2a3f54;/* background: lightgrey; */color: #fff;/* 	color:black; */margin: 2px;}.dataTableTheme>thead {height: 50px;}.dataTableTheme>tbody>tr:nth-child(odd), .dataTableTheme>tfoot>tr:nth-child(odd),.dataTableTheme>thead>tr:nth-child(odd) {/* background-color: lightgrey; */border: 1px solid #111;background-color: rgba(168, 227, 214, 0.2);}table.dataTable thead th, table.dataTable thead td, table.dataTable tr th,table.dataTable tr td {padding: 5px;border: 1px solid #111;margin-left: 2px;}#custom td, #custom tr {border: 1px solid #ddd;padding: 8px;font-weight: bold;}#calc td, #custom tr {border: 1px solid #ddd;padding: 8px;font-weight: bold;}}</style>');
										//$(wnd.document.body).html($('#tbDt').html()); 
										
									/*     jQuery.get('css/dynamicDt.css', function (data) {
									    	alert(data)
									    	console.log(data)
									    	$(wnd.document.head).append('<style type="text/css">'+data+'</style>');
									    	$(wnd.document.body).html($('#tbDt').html()); 
									        //wnd.close();
									    });
										 

									}
								});

					}
					}
				});
				
				document.getElementById("enableDetailPopup").click();
			} catch (e) {
				alert(e);
			} 
 
			*/		
		}
		
		function detailPopup(img) {
			var ids = img;
			$("#detailDataTitle").empty();
			$("#detailDataHead").empty();

			$("#detailDataBody").empty();
			if (ids != '') {
				var val = ids;
				var arg = val.split("~")[0].trim();
				var query = val.split("~")[2].trim();
				var head = val.split("~")[1].trim();
				var headArr = head.split('~');
				$.each(headArr, function(k, value) {
					var tr = $("<tr/>");
					var headArrs = value.split(',');
					$.each(headArrs, function(a, val) {
						
						var td = $("<td/>");
						td.text(val);
						tr.append(td);

					});
					$("#detailDataHead").append(tr);
				});
				var title = val.split("~")[3].trim();
				$("#detailDataTitle").append(title);
			}
			try {
				$.ajax({
					type : "POST",
					async : true,
					url : "dynamicViewReport_methodQuery.action",
					data : {
						query : query,
						param : arg,
						id : typez
					},
					success : function(result) {
						var jsonValue = $.parseJSON(result);
						$.each(jsonValue, function(k, value) {
							var tr = $("<tr/>");
							$.each(value, function(a, val) {
								var td = $("<td/>");
								td.text(val);
								tr.append(td);
							});
							$("#detailDataBody").append(tr);
						});
					}
				});
				document.getElementById("enableDetailPopup").click();
			} catch (e) {
				alert(e);
			}

		}

		/* function popupWindow(img) {

			var id1 = img;

			var carousel = $("<div>").addClass("carousel");
			try {

				$("#mbody").empty();

				var div = $("<div>");
				var image = '<img src="data:image/png;base64,'+id1+'" height="220" width="260"/>';
				div.append(image);
				carousel.append(div);

				$("#mbody").append(carousel);
				$(".carousel").carousel();
				document.getElementById("enableModal").click();
			} catch (e) {
				alert(e);
			}
		} */
		
		
		
		
		
		 var imgID = "";
			function popupImages(ids) {
				
				try{
					var str_array = ids.split(',~#');
					$("#mImagebody").empty();
					
					var mbody="";
					
					for(var i = 1; i < str_array.length; i++){
						//<img class="slidesjs-slide" src="sensitizingReport_populateImage.action?id='+str_array[i]+'" slidesjs-index="0"/>');
						
						if(i==1){
							mbody="";
							mbody="<div class='item active'>";
							mbody+='<img src="data:image/png;base64,'+str_array[i]+'"/>';
							mbody+="</div>";
						}else{
							mbody="";
							mbody="<div class='item'>";
							mbody+='<img src="data:image/png;base64,'+str_array[i]+'"/>';
							mbody+="</div>";
						}
						$("#mImagebody").append(mbody);
					 }
					
					//$("#mbody").first().addClass( "active" );
					
					document.getElementById("enableImageModal").click();	
				}
				catch(e){
					alert(e);
					}
				
			}
		
		
		
		
		
		
		
		
		
		
		
		
		
		function downloadFile(img) {
/* alert(img);
			var id1 = img;

			var blob = id1;
		    console.log(blob.size);
		    var link=document.createElement('a');
		    //link.href=window.URL.createObjectURL(blob);
		    var binaryData = [];
binaryData.push(blob);
link.href = window.URL.createObjectURL(new Blob(binaryData, {type: "application/pdf"}))
		    
		    
		    link.download="Dossier_" + new Date() + ".pdf";
		    link.click(); */
			document.getElementById("fileId").value=img;
			$('#audioFileDownload').submit();
		}

		function showFarmMap(compCode) {
			var content = "<div id='map' class='smallmap'></div>";
			var landarry = new Array();
			if (compCode != '') {
				var val = compCode;
				var area;
				var coord;
				var hd; 
				
				if(val.includes("~") && val.includes("$") &&  val.includes("#")){
					 area = val.split("~")[0].trim().split("$")[1].trim();
					 coord = val.split("~")[1].trim();
					 hd = val.split("~")[0].trim().split("$")[0].trim(); 
					 enablePopup(hd, content);
						$('#clsBtn').attr('onclick', "hidePopup('" + compCode + "')");
						initMap();

						coord = coord.substring(0, coord.length - 1);
						$(coord.split('#'))
								.each(
										function(key, value) {
											var latLong = {};
											latLong.lat = parseFloat(
													value.split(',')[0].trim())
													.toFixed(5);
											latLong.lon = parseFloat(
													value.split(',')[1].trim())
													.toFixed(5);

											landarry.push(latLong);
										});

						loadMap(new Array(), landarry, area);
				}else{
					//only 2 coordinates allowed to create marker
					var cordinates = val.split(",");
					val = "<b> </b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>  </b>$~"+cordinates[0]+","+cordinates[1];
					 area = val.split("~")[0].trim().split("$")[1].trim();
					 coord = val.split("~")[1].trim();
					 hd = val.split("~")[0].trim().split("$")[0].trim(); 
					 enablePopup(hd, content);
						$('#clsBtn').attr('onclick', "hidePopup('" + compCode + "')");
						initMap();

						coord = coord.substring(0, coord.length - 1);
						$(coord.split('#'))
								.each(
										function(key, value) {
											var latLong = {};
											latLong.lat = parseFloat(
													value.split(',')[0].trim())
													.toFixed(5);
											latLong.lon = parseFloat(
													value.split(',')[1].trim())
													.toFixed(5);

											landarry.push(latLong);
										});

						loadMap(new Array(), landarry, area);
					 
					  var myLatLng = {lat: Number(cordinates[0]), lng: Number(cordinates[1])};

				        var map = new google.maps.Map(document.getElementById('map'), {
				          zoom: 7,
				          center: myLatLng
				        });

				        var marker = new google.maps.Marker({
				          position: myLatLng,
				          map: map,
				          title: 'Hello World!'
				        }); 
				}
				
				
				
			}

		}

		function loadMap(dataArr, landArea, area) {

			var url = window.location.href;
			var temp = url;
			coorArr = new Array();
			for (var i = 0; i < 1; i++) {
				temp = temp.substring(0, temp.lastIndexOf('/'));
			}
			var intermediateImg = "red_placemarker.png";
			var intermediatePointIcon = temp + '/img/' + intermediateImg;

			var infowindow = new google.maps.InfoWindow();

			var marker, i;
			$(dataArr).each(
					function(k, v) {
						marker = new google.maps.Marker({
							position : new google.maps.LatLng(v.latitude,
									v.longitude),
							icon : intermediatePointIcon,
							map : map
						});
						markers.push(marker);
						google.maps.event.addListener(marker, 'click',
								(function(marker, i) {
									return function() {
										infowindow.setContent(buildData(v));
										infowindow.open(map, marker);
									}
								})(marker, i));
						map.setCenter({
							lat : v.latitude,
							lng : v.longitude
						});
						map.setZoom(17);
					});
			var cords = new Array();
			var bounds = new google.maps.LatLngBounds();
			if (landArea.length > 0) {

				$(landArea).each(
						function(k, v) {
							cords.push({
								lat : parseFloat(v.lat),
								lng : parseFloat(v.lon)
							});
							var coordinatesLatLng = new google.maps.LatLng(
									parseFloat(v.lat), parseFloat(v.lon));
							coorArr.push(v.lat + "," + v.lon);
							bounds.extend(coordinatesLatLng);

						});
				var plotting = new google.maps.Polygon({
					paths : cords,
					strokeColor : '#50ff50',
					strokeOpacity : 0.8,
					strokeWeight : 2,
					fillColor : '#50ff50',
					fillOpacity : 0.45,
					editable : false,
					draggable : false
				});
				plotting.setMap(map);
				//  plotCOunt = plotCOunt + 1;
				var overlay = {
					overlay : plotting,
					type : google.maps.drawing.OverlayType.POLYGON
				};

				map.fitBounds(bounds);
				var arType = ' Ha';
				//alert("areaType:"+arType);
				var textTpe = "";

				textTpe = area + arType;
				//  $('#area').val(area);
				mapLabel2 = new MapLabel({

					text : textTpe,
					position : bounds.getCenter(),
					map : map,
					fontSize : 14,
					align : 'left'
				});
				mapLabel2.set('position', bounds.getCenter());

			} else {

			}

		}


		function initMap() {
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 11.0168,
					lng : 76.9558
				},
				zoom : 5,
				mapTypeId : google.maps.MapTypeId.HYBRID,
			});

		}
		function buttonEdcCancel() {
			document.getElementById("model-close-edu-btn").click();
		}
		function buttonImgCancel() {
			document.getElementById("model-close-edu-btn-img").click();
		}
		function buttonDataCancel() {
			document.getElementById("detail-close-data-btn").click();
		}

		function showMap(latitude, longitude) {
			//String[] a = latLon.split(",");

			try {
				var heading = '<s:text name="coordinates"/>:' + latitude + ","
						+ longitude;
				var content = "<div id='map' class='smallmap'></div>";

				enablePopup(heading, content);

				loadLatLonMap('map', latitude, longitude, "");
			} catch (e) {
				console.log(e);
			}
		}

		function loadLatLonMap(mapDiv, latitude, longitude, landArea) {
			var fProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
			var tProjection = new OpenLayers.Projection("EPSG:900913");
			try {
				jQuery("#map").css("height", "400px");
				jQuery("#map").css("width",
						(jQuery(".rightColumnContainer").width()));
				jQuery("#" + mapDiv).html("");
				var mapObject = new OpenLayers.Map(mapDiv, {
					controls : []
				});
				var googleLayer2 = new OpenLayers.Layer.OSM();
				mapObject.addLayer(googleLayer2);
				mapObject.addControls([ new OpenLayers.Control.Navigation(),
						new OpenLayers.Control.Attribution(),
						new OpenLayers.Control.PanZoomBar(), ]);
				// new OpenLayers.Control.LayerSwitcher()]);
				mapObject.zoomToMaxExtent();
				var setCenter = true;
				// alert(latitude+"===="+longitude);
				if (latitude != null && latitude != '' && longitude != null
						&& longitude != '' && longitude != 0 && latitude != 0
						&& landArea.length <= 0) {

					mapObject.setCenter(new OpenLayers.LonLat(longitude,
							latitude).transform(fProjection, tProjection), 17,
							false, false);
					setCenter = false;
					var markers = new OpenLayers.Layer.Markers("Markers");
					var size = new OpenLayers.Size(21, 25);
					var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
					var icon = new OpenLayers.Icon(iconImage, size, offset);
					var mark1 = new OpenLayers.Marker(new OpenLayers.LonLat(
							longitude, latitude).transform(fProjection,
							tProjection), icon);
					markers.addMarker(mark1);
					mapObject.addLayer(markers);
				}

				if (landArea.length > 0) {

					var polygonList = [], multuPolygonGeometry, multiPolygonFeature;
					var vector = new OpenLayers.Layer.Vector('multiPolygon');
					var pointList = [];
					for (var i = 0; i < landArea.length; i++) {
						var landObj = landArea[i];
						//alert(landObj.lat+"===="+landObj.lon);			
						if (landObj.lat != null && landObj.lat != ''
								&& landObj.lon != null && landObj.lon != ''
								&& landObj.lon != 0 && landObj.lat != 0) {
							var point = new OpenLayers.Geometry.Point(
									landObj.lon, landObj.lat).transform(
									fProjection, tProjection);
							pointList.push(point);
							if (setCenter) {
								mapObject.setCenter(new OpenLayers.LonLat(
										landObj.lon, landObj.lat).transform(
										fProjection, tProjection), 18, false,
										false);
								setCenter = false;
							}
						}
					}
					var linearRing = new OpenLayers.Geometry.LinearRing(
							pointList);
					var polygon = new OpenLayers.Geometry.Polygon(
							[ linearRing ]);
					polygonList.push(polygon);
					multuPolygonGeometry = new OpenLayers.Geometry.MultiPolygon(
							polygonList);
					multiPolygonFeature = new OpenLayers.Feature.Vector(
							multuPolygonGeometry);
					vector.addFeatures(multiPolygonFeature);
					mapObject.addLayer(vector);
				}

				//alert(jQuery("#lftCol").height()+"----"+jQuery("#rgtCol").height());
				//jQuery("#map").css("height",(jQuery("#lftCol").height()-68))

				//jQuery("#map").css("height","400px");
				//jQuery("#map").css("width",(jQuery(".rightColumnContainer").width()));

				//alert(jQuery("#map").height());
			} catch (err) {
				console.log(err);
			}
		}
		
		function detailData(id){
			document.updateform.action = action+id;
			postDataSubmit();
			document.updateform.submit(); 
			
		}
		
		 function redirectToCalendarView(farmId,seasonCode){
				var farm=farmId;	 
				var season =seasonCode;
				var title ='<s:property value="%{getLocaleProperty('sowingActivity')}" />';
				 var header ='<s:property value="%{getLocaleProperty('caldetailHeader')}" />';
				
				$("#detailDataTitle").empty();
				$("#detailDataHead").empty();
				$("#detailDataBody").empty();
				var tr = $("<tr/>");
				var headArrs = header.split(',');
						$.each(headArrs, function(a, val) {
							var td = $("<td/>");
							td.text(val);
							tr.append(td);

						});
					$("#detailDataHead").append(tr);
					$("#detailDataTitle").append(title);
				try {
					
					$.post("dynamicViewReport_populateCalendarValues", {
						selectedFarm : farm,
						selectedSeason : season
					}, function(data) {
						var jsonValue = $.parseJSON(data);
						$.each(jsonValue, function(k, value) {
								var tr2 = $("<tr/>");
								tr2.append($("<td/>").append(value.productName));
								tr2.append($("<td/>").append(value.varietyName));	
								tr2.append($("<td/>").append(value.activityName));		
								tr2.append($("<td/>").append(value.status));		
								tr2.append($("<td/>").append(value.date));		
								tr2.append($("<td/>").append(value.remarks));	
								$("#detailDataBody").append(tr2);
							});
							
						});
					document.getElementById("enableDetailPopup").click();
				} catch (e) {
					alert(e);
				}
			}
	</script>
	<script
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.28&libraries=geometry,drawing,places"></script>
	<script src="js/maplabel-compiled.js?k=2.16"></script>


	<style>
.reportWrap .filterEle {
	float: left;
	cursor: pointer;
	margin-right: 1%;
	margin-left: 1%;
	margin-bottom: 5px;
	box-sizing: border-box;
}

.reportWrap .filterEle {
	width: 14%;
	height: auto;
	box-sizing: border-box;
}

.reportWrap .filterEle label {
	font-weight: bold;
	color: #000;
}



</style>

 <button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
		<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content" id="DivIdToPrint">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body" id="mbody">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonEdcCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div> 
	
	<button type="button" id="enableImageModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideImageModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>

	<div id="slideImageModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn-img" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body">
					<div id="myImageCarousel" class="carousel slide" data-ride="carousel">
						 <div class="carousel-inner" role="listbox" id="mImagebody">
						 	
						 </div>
						 
						 <a class="left carousel-control" href="#myImageCarousel" role="button" data-slide="prev">
						      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						      <span class="sr-only">Previous</span>
   						 </a>
					    <a class="right carousel-control" href="#myImageCarousel" role="button" data-slide="next">
					      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					      <span class="sr-only">Next</span>
					    </a>
					    
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonImgCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
	<s:form action="dynamicViewReport_populateHTML" id="printHTML"
	method="POST" target="printWindow">
	 <s:hidden id="id" name="id"></s:hidden>
	<s:hidden id="html" name="htmlContent"></s:hidden>
 
</s:form>
	<%-- FILTER SECTION --%>
	<s:form id="form">
	
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParma" id="branchIdParma" list="parentBranches"
										headerKey="" headerValue="Select" cssClass="select2"
										onchange="populateChildBranch(this.value)" />

								</div>
							</div>
						</s:if>

						<div class="filterEle">
							<label for="branchIdParam"><s:text name="app.subBranch" /></label>
							<div class="form-element">
								<s:select name="subBranchIdParam" id="subBranchIdParam"
									list="subBranchesMap" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2" />
							</div>
						</div>

					</s:if>
					<s:else>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParma" id="branchIdParma" list="branchesMap"
										headerKey="" headerValue="%{getText('txt.select')}"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
					</s:else>
					<s:if test="reportConfigFilters.size()>0">
					<s:iterator value="reportConfigFilters" status="status">
						<div class='filterEle'>
							<label for="txt"><s:property
									value='%{getLocaleProperty(label)}' /></label>
							<div class="form-element">
								<s:if test="type==3 || type==5">
									<s:select name="%{field}" list="options" headerKey=""
										headerValue="%{getText('txt.select')}"
										class="form-control input-sm select2 filterClass 3" />
									<s:set var="personName" value="%{method}" />
								</s:if>
								<s:if test="type==6">
									<s:select name="%{field}" list="options" headerKey=""
										headerValue="%{getText('txt.select')}"
										class="form-control input-sm select2 filterClass 6" />
									<s:set var="personName" value="%{method}" />
								</s:if>
									<s:if test="type==7">
									<s:select name="%{field}" list="options" headerKey=""
										headerValue="%{getText('txt.select')}"
										class="form-control input-sm select2 filterClass 7" />
									<s:set var="personName" value="%{method}" />
								</s:if>
								<s:if test="type==1">
									<select id="cond" cssClass="form-control" name="condition">
										<option value=">">Greater Than</option>
										<option value=">=">greater than or Equal to</option>
										<option value="<=">less than or Equal to</option>
										<option value="<">Less than</option>
										<option value="=">Equals</option>
										<option value="like">contains</option>
									</select>&nbsp;
									<s:textfield theme="simple" name="%{field}" maxlength="25"
										cssClass="1 filterClass form-control" />


								</s:if>
								<s:if test="type==4">
									<s:if test="isDateFilter==1">
										<s:textfield id="%{#field}" name="%{field}"
											class="form-control input-sm filterClass dateFilterApp 4" />
									</s:if>
									<s:else>
										<s:textfield id="%{#field}" name="%{field}"
											class="form-control input-sm filterClass dateFilterApp 4" />
									</s:else>
								</s:if>
							</div>
						</div>
					</s:iterator>
					</s:if>
					<div class='filterEle' style="margin-top: 2%; margin-right: 0%;">
						<button type="button" class="btn btn-success btn-sm" id="generate"
							aria-hidden="true" onclick="search()">
							<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger btn-sm"
							aria-hidden="true" id="reset" onclick="reset()">
							<b class="fa fa-close"></b>
						</button>
					</div>
				</div>
			</section>
		</div>
		
	</s:form>
	
	<s:form id="audioFileDownload" action="dynamicViewReport_populateDownload">
	<s:hidden id="fileId" name="csId" />
	 <s:hidden id="id" name="id"></s:hidden>
</s:form>

	<%-- <s:if test="id==10 && tenant==griffith">
		<s:form name="updateform" action="dynamicViewReport_cropDetail">
		<s:hidden name="id" />
		<s:hidden name="farmerId" id="farmerId"/>
		<s:hidden name="farmId" />
		 <s:hidden name="lotCode" id="batchNo"/>
		<s:hidden name="postdata" id="postdata" />
		<s:hidden name="type" class="type" />
		<s:hidden name="currentPage" />
		<s:hidden name="farmCropId" id="plotCode"/>
		<s:hidden name="season" id="season"/>
	</s:form>
	
</s:if> --%>

	<s:form  id="updateform"
	method="POST" target="printWindow">
	<s:hidden name="currentPage" />
	<s:hidden name="id" />
	<s:hidden name="postdata" id="postdata" />
	</s:form>

	<%-- GRID SECTION --%>

	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
						type="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
						  <li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li>  
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					<%-- <li><a href="#" onclick="exportCSV()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="csv" /></a></li> --%>
					</ul>
				</div>
			</div>
		</div>


		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	<!-- <div id="map" class="smallmap map" style="height: 500px"></div> -->
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
	
 	<button type="button" id="enableDetailPopup"
		class="hide slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideDetailModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button> 
	 <div id="slideDetailModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
				 	<button type="button" id="detail-close-data-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="detailDataTitle"></h4>
				</div>

				<table class="table table-bordered table-responsive">
					<div class="modal-body">
						
						<thead>
							 <tr>
								<%-- <td><s:property value="%{getLocaleProperty('farm')}" /></td>
								<td><s:property value="%{getLocaleProperty('farmer')}" /></td>
								<td><s:property value="%{getLocaleProperty('plottingtime')}" /></td>
								<td><s:property value="%{getLocaleProperty('plottingtime')}" /></td> --%>
								<tbody id="detailDataHead">
								
							</tr> 
						
						</thead>
						<tbody id="detailDataBody">
						</tbody>
						
					</div>
				</table>

				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonDataCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div> 
</body>
