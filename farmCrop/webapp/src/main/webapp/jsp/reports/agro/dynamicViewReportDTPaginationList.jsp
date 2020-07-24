<%@ include file="/jsp/common/datatable-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">

</head>
  
<body>
	<script src="js/dynamicComponents.js?v=18.14"></script>
	<script type="text/javascript">
		var branchId = '<s:property value="branchId"/>';
		var fetchType = "";
		var typez =<%out.print("'" + request.getParameter("id") + "'");%>;
			var table;
			var min = "";
			var max = "";
			var dataInd = "";
			var subBridObj ={};
			var columnsF ={};
		jQuery(document)
				.ready(
						function() {
							jQuery("#clsBtn").click(function() {
								hidePopup();
							});
							pdfMake.fonts = {
							  Roboto: {
								  normal: 'Roboto-Regular.ttf',
									bold: 'Roboto-Medium.ttf',
									italics: 'Roboto-Italic.ttf',
									bolditalics: 'Roboto-MediumItalic.ttf'
							  },
							  Roboto1: {
								    normal: 'Bangla.ttf',
								    bold: 'Bangla.ttf',
								    italics: 'Bangla.ttf',
								    bolditalics: 'Bangla.ttf'
								  }
							};

							subBridObj ={};
							
							fetchType = '<s:property value="fetchType"/>';
						
							var colNames = '<s:property value="mainGridCols"/>';
							
							loadGrid();
							 /*  var state = table.state.loaded();
						        if (state) {
						            table.columns().eq(0).each(function (colIdx) {
						                var colSearch = state.columns[colIdx].search;
						         var toww = $(".filterTd");
						                if (colSearch.search) {
						                	
						                	$("tr.filterTd th:nth-child("+colIdx+1+")").find(".filt").val(colSearch.search);
						                }
						            });
						 
						            table.draw();
						        }  */
							$("#reset").click(function() {
								var url = (window.location.href);
								window.location.href = url;
								$(".select2").val("");

							});
							 	
							 	$.fn.dataTable.ext.search.push(
								          function (settings, data, dataIndex) {
								     	  if(min!=null && max!=null && min!='' && max!='' &&  moment(data[dataInd])!=null){
								     			 var fDate,lDate,cDate;
									     	    cDate = moment(data[dataInd]);
									     	 if(min.isSame(max)){
									     		 if((min.isSame(cDate))) {
										     	        return true;
										     	    }
										     	    return false;
									     	   
									     	  }
									     	 else{
									     		
									     		fDate = min.toDate();
									     		cDate = cDate.toDate();
									     		lDate = max.toDate()
									     			if((cDate <= lDate && cDate >= fDate)) {
									     		
										     	        return true;
										     	    }
										     	    return false;
									     	  }
								     	  }else{
								     		 return true;
								     	  }
								    
								     
								    }
								    );

							$('#detail tbody').on(
									'click',
									'td.details-control',
									function() {
										var tr = $(this).closest('tr');
										var row = $('#detail').DataTable().row(
												tr);

										if (row.child.isShown()) {
											$(this).addClass(
													'glyphicon-plus-sign');
											$(this).removeClass(
													'glyphicon-minus-sign');
											row.child.hide();
											tr.removeClass('shown');
										} else {
											// Open this row
											row.child(format(row.data()))
													.show();
											$(this).removeClass(
													'glyphicon-plus-sign');
											$(this).addClass(
													'glyphicon-minus-sign');
											tr.addClass('shown');
										}
									});
							var flt =  '<s:property value="filterSize"/>';
							flt = flt.split(",");
							 $('#detail thead  tr:eq(0)').addClass("noBorderB");
							 $('#detail thead tr  th:eq(0)').removeClass("glyphicon glyphicon-plus-sign");
							 $('#detail tfoot tr  th:eq(0)').removeClass("glyphicon glyphicon-plus-sign");
							///$('#detail thead tr').clone(true).addClass("filterTd").appendTo( '#detail thead' );
						    $('#detail thead tr:eq(0) th').each( function (i) {
						    if($(this).attr("class").indexOf("details-control")<0){
						    	//var table =  $("<table/>").css("width","100%");
						    	//var tr = $("<tr/>").css("width","100%");
						        var title = $(this).text();
						        var fikter=containsAny(flt,$(this).attr("class").split(" "));
						         var tesst =$('<input type="text" placeholder="Search '+title+'" id="'+columnsF[i]+'" />' );
						       var closeBtn = $('<a title="Reset Search Value" style="padding-right: 0.3em;padding-left: 0.3em;color:white" class="clearsearchclass">x</a>');
						          if(fikter !='' && fikter != undefined){
						        	  tesst =$('#'+fikter);
						        	  if($(tesst).hasClass("4")){
						        		  $(closeBtn).addClass("4");
								        	$(tesst).daterangepicker({
								        		dateFormat : 'YYYY-MM-DD',
								        		format:'YYYY-MM-DD',
												startDate : document.getElementById("hiddenStartDate").value,
										        endDate : document.getElementById("hiddenEndDate").value,
										        placeholder:'Select '+title,
											      ranges: {
										           'Today': [moment(), moment()],
										           'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
										           'Last 7 Days': [moment().subtract(6, 'days'), moment()],
										           'Last 30 Days': [moment().subtract(29, 'days'), moment()],
										           'This Month': [moment().startOf('month'), moment().endOf('month')],
										           'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
										        }
										    },
							                  function (start, end) {
										    	if ( table.column(i).search() !== this.value ) {
										    		filterColumns();
										    	
										    	}
										    	
							                  });
								        }else{
								        	  $(closeBtn).addClass("2");
						        	  $('select[id='+fikter+'] > option:first-child')
						        	    .text('Select '+title).css("font-size","10px");
						        	  
						        	  $(tesst).unbind().bind('keyup change onchange', function() {
						        		  filterColumns();
						        		});
						        		
						        	
						  	       
								        }
						         }else{
						        $(tesst).on( 'keyup change onchange', function () {
						        
						            if ( table.column($(this).closest("th").index()).search() !== this.value ) {
						            	filterColumns();
						            }
						        } );
						        }
						        //$(this).html('');
						         $(closeBtn).on( 'click', function () {
						        	 filterColumns();
						           
						        } );
						         //$(this).html('');
						     	var tdd = $("<td/>");
						     	var tds = $("<td/>");
						     	$(tesst).attr("id",columnsF[i]);
						     	$(tesst).addClass("filt");
						        $(tdd).css("width","90%").addClass("filterTd").append(tesst);
						      
						        $(tds).css("width","10%").addClass("filterTd").append(closeBtn);
						   
						        $(this).append(tdd).append(tds);
						     //   $(table).append(tr);
						      //$(this).append(tr);
						        $('.select2').select2();
						       
						    

						    }
						    } );  
					
						});
		
		 
		jQuery("#generate").click(function() {
			reloadGrid();
		});

		jQuery("#clear").click(function() {
			clear();
		});
		var data = new Array();

		function format(d) {
			var subGridColumnNames = new Array();
			var subGridColumnModels = new Array();
			var subColNames = '<s:property value="subGridCols"/>';

			var subColumnModels = '<s:property value="subGridColNames"/>';

			var returTRs = "";
			if(subBridObj.hasOwnProperty(d.id)){
				returTRs = subBridObj[d.id];
			}else{
			$
					.ajax({
						type : "POST",
						async : false,
						url : "dynamicViewReportDT_subGridDetail.action?id="
								+d.id,
						success : function(result) {
						returTRs = '<table class=" table table-bordered" cellpadding="5" cellspacing="0" border="0" width="75%">';
							returTRs = returTRs + '<thead>';
							$(subColNames.split("%")).each(
									function(k, val) {
										if (val != null && val!='') {
											var cols = val.split("#");
											returTRs = returTRs + '<th>'
													+ cols[0] + '</th>';
										}
									});
							returTRs = returTRs + '</thead>';
							$(JSON.parse(result).data).each(
									function(k, val) {

										returTRs = returTRs + '<tr>';
										$(val).each(
												function(j, val1) {

													returTRs = returTRs
															+ '<td>' + val1
															+ "</td>";
												});
										returTRs = returTRs + '</tr>';
									});
							returTRs = returTRs + '</table>';

						}
					});
			subBridObj[d.id] = returTRs;
			}
			return $(returTRs);
		}

	function filterColumns(){
		var filts ={};
		   $('.filt').each( function (i) {
			if($(this).val()!=''){
				filts[$(this).attr("id")] = $(this).val();
			}
		});
		   table.ajax.url('dynamicViewReportDT_detail.action?id='+typez+'&filterList=' +JSON.stringify(filts)).load();
	}

		function loadGrid() {

			var gridColumnNames = new Array();
			var gridColumnModels = new Array();

			var colNames = '<s:property value="mainGridCols"/>';
			var colModels = '<s:property value="mainGridColNames"/>';
			var footCol = '<s:property value="footerSumCols"/>';
			var footTot = '<s:property value="footerTotCol"/>';
			var oibj = {};
			var foot = "<tfoot class='colFoot'><tr>";
			if ('<s:property value="subGridCols"/>' != null
					&& '<s:property value="subGridCols"/>' != '') {
				oibj['className'] = 'details-control glyphicon glyphicon-plus-sign filterTdR';
				oibj['orderable'] = false;
				oibj['data'] = null;
				oibj['defaultContent'] = '';
				gridColumnNames.push(oibj);
				foot = foot + "<th>";
			}
			
			$(colNames.split("%")).each(function(k, val) {

				if (!isEmpty(val)) {
					var cols = val.split("#");
					oibj = {};
					var classN=cols[3]+" filterTdR ";
					if(cols[2]=='right'){
						classN = classN+" numeric ";
						
						foot = foot + "<th  class='colFoot' align='right' id='foot"+(k)+"'>";
					}else{
						foot = foot + "<th   class='colFoot' id='foot"+(k)+"'>";
					}
					columnsF[k+1] =cols[3];
					oibj['sClass'] = classN;
					oibj['id'] = classN;
					oibj['sWidth'] = cols[1];
					oibj['sWidth'] = cols[1];
					oibj['title'] = cols[0];
					oibj['data'] =cols[4];
					oibj['defaultContent'] = '0';
					oibj['orderable'] = false;
					gridColumnNames.push(oibj);
				
					//gridColumnModels.push({name: cols[0],width:cols[1],sortable: false,frozen:true});
				}
			});

			$('#detail').append(foot + "</tr></tfoot>");
			
			 table = $('#detail')
					.DataTable(
							{
								// data: dataSet,
								"bPaginate": true,
     							"order": [ 0, 'asc' ],
    							"bInfo": true,
     							"iDisplayStart":0,
     							"bProcessing" : true,
    							"bServerSide" : true,
    							"processing": true,
    					        "serverSide": true,
								 "ajax" : {
									"url" : "dynamicViewReportDT_detail.action?id="
											+ typez,
									"type" : "POST",
									"dataSrc": function(json){
										return json.data;
									   
									},
								},
								//"paging":true,
								//"searching": true,
								//"bAutoWidth" : false,
								// "autoWidth": false,
								 //"responsive": true,
								//"colReorder" : true,
							
						
								"pageLength": 25,
					            
								language : {
									sLoadingRecords : '<span style="width:100%;" ><img align="center" src="img/ajax-loader.gif"></span>'
								},
								columns : gridColumnNames,
						
								bAutoWidth: false , 
								dom : 'Bfrtip',
								 lengthMenu: [
								              [ 10, 25, 50, -1 ],
								              [ '10 rows', '25 rows', '50 rows', 'Show all' ]
								          ],
							});
			 $('.dataTables_empty').css("align","center");
			 $("#detail").addClass("dataTableTheme");
			/*  
			For Blue Theme
			$("#detail").addClass("dataTableTheme");
			$('.dt-button').css("background","#2a3f54");
			$('.dt-button').css("color","#fff");
			*/
			 $(".dataTable").attr("id","detail");
			 $("#detail").removeClass("dataTable");
			$('.dt-button').css("background","lightgrey");
			$('.dt-button').css("color","black");
			
			$("#detail").addClass(" table table-bordered"); 
		
			if(typez=='17'){
				$('.pdfBtn').addClass('hide' );
			   
			}

		}
		function decode_utf8(s) {
			  return decodeURIComponent(escape(s));
			}
		function containsAny(source,target)
		{
		    var result = source.filter(function(item){ return target.indexOf(item) > -1});   
		    return result[0];  
		}   
		function getSorted(arr, sortArr) {
			  var result = [];
			  for (var i = 0; i < arr.length; i++) {
			    console.log(sortArr[i], arr[i]);
			    result[i] = arr[sortArr[i]];
			  }
			  return result;
			}
	
	</script>


	<style>
.dataTables_empty {
	text-align: center;
}
.filterClass.select2 {
width: 80% !important
}
.glyphicon {
	position: relative;
	top: 1px;
	display: table-cell;
	font-family: 'Glyphicons Halflings';
	font-style: normal;
	font-weight: 400;
	cursor: pointer;
	line-height: 1;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale
}

.details-control {
	width: 20px;
}

.select2 {
	/* 	width: auto !important; */
	position: relative;
	margin-top: 5px;
}

#baseDiv {
	overflow: scroll;
}

.dataTableTheme {
	border-collapse: separate;
	border: solid black 1px;
	border-radius: 6px;
	-moz-border-radius: 6px;
	table-layout: fixed;
	overflow: scroll;
}

.dataTableTheme>thead>tr>th {
	background: #2a3f54;
	/* background: lightgrey; */
	color: #fff;
	/* 	color:black; */
	margin: 2px;
}

.dataTableTheme>thead {
	height: 50px;
}

.dataTableTheme>tbody>tr:nth-child(odd), .dataTableTheme>tfoot>tr:nth-child(odd),
	.dataTableTheme>thead>tr:nth-child(odd) {
	/* background-color: lightgrey; */
	border: 1px solid #111;
	background-color: rgba(168, 227, 214, 0.2);
}

table.dataTable thead th, table.dataTable thead td, table.dataTable tr th,
	table.dataTable tr td {
	padding: 5px;
	border-bottom: 1px solid #111;
	margin-left: 2px;
}

button.dt-button {
	/* background: #2a3f54; */
 background: lightgrey; 
	border-color: #005bbf;
/* 	color: #fff; */
	 color:black; 
	border-radius: 6px;
}

.dt-button:hover {
	background-color: #2a3f54
}

.dt-button:active {
	background-color: #2a3f54;
	/* 	background: lightgrey; */
	box-shadow: 0 5px #666;
	transform: translateY(4px);
}

table td.numeric, table tfoot th.numeric {
	text-align: right;
}

.numeric {
	align: right;
}

select.form-control {
	background-color: #fff;
	border: 1px solid #D5D5D5;
	border-radius: 2 2 2 2 !important;
	color: #858585;
	padding: 5px 6px;
}

.input-sm {
	height: 30px;
	line-height: 30px;
}

input[type="text"], input[type="password"], input[type="datetime"],
	input[type="datetime-local"], input[type="date"], input[type="month"],
	input[type="time"], input[type="week"], input[type="number"], input[type="email"],
	input[type="url"], input[type="search"], input[type="tel"], input[type="color"]
	{
	background-color: #fff;
	border: 1px solid #d5d5d5;
	border-radius: 8 8 8 8 !important;
	color: #858585;
	font-family: inherit;
	font-size: 10px;
	line-height: 0.4;
	padding: 5px 4px;
	height: 25px;
	width: 90%;
	transition-duration: 1s;
	box-shadow: none;
	margin-top: 3px;
}

.select2-container--default .select2-selection--single .select2-selection__rendered
	{
	line-height: 90% !important;
	word-wrap: break-word !important;
	text-overflow: ellipsis !important;
	white-space: normal !important;
}

.select2-selection__rendered {
	font-size: 10px;
}

.colFoot {
	background-color: grey;
}

tr.filterTd th {
  border-top: 0 !important;
  padding-top: 1px !important;
}

th.filterTdR {
  border-bottom: 0 !important;
}
tr.filterTd{
background-color: #ededed !important;
}

tr.noBorderB{
 padding-bottom: 1px !important;
}
.filterTdR  {
  word-wrap: break-word;
  
}

</style>


	<%-- FILTER SECTION --%>
	<s:form id="form">
		<s:if test="reportConfigFilters.size()>0">
			<div class="appContentWrapper marginBottom hide">
				<section class='reportWrap row'>
					<div class="gridly">
						<s:iterator value="reportConfigFilters" status="status">
							<div class='filterEle'>
								<label for="txt"><s:property
										value='%{getLocaleProperty(label)}' /></label>
								<div class="form-element">
									<s:if test="type==3 ">
										<s:select name="%{field}" list="options" headerKey=""
											headerValue="%{getText('txt.select')}" id="%{label}"
											class="form-control input-sm select2 filterClass 3" />
										<s:set var="personName" value="%{method}" />
									</s:if>
									<s:if test="type==5 ">
										<s:select name="%{field}" list="options" headerKey=""
											headerValue="%{getText('txt.select')}" id="%{label}"
											class="form-control input-sm select2 filterClass 5" />
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
									<s:textfield theme="simple" name="%{field}" id="%{label}"
											maxlength="25" cssClass="1 filterClass form-control" />


									</s:if>
									<s:if test="type==4">
										<s:textfield id="%{label}" name="%{field}"
											class="form-control input-sm filterClass dateFilterApp 4" />
									</s:if>
								</div>
							</div>
						</s:iterator>

					</div>
				</section>
			</div>
		</s:if>
	</s:form>

	<%-- GRID SECTION --%>

	<div class="appContentWrapper marginBottom">

		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>

		</div>

	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:form id="activityDetail" target="_blank">
	
	</s:form>
	
	<s:form id="pdf" action="dynamicViewReportDT_populatePDF.action">
	<s:hidden name="pdfData" id="pdfData"/>
	<s:hidden name="pdfFileName" id="pdfFileName"/>
	<s:hidden name="id" id="id"/>
	</s:form>
</body>
