<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<script src="plugins/openlayers/OpenLayers.js"></script>
<style><%@include file="/css/dynamicDt.css"%></style>
<head>
<meta name="decorator" content="swithlayout">
</head>
<style>
.test {
width:auto;
height:120px;
}
.imgWrap{
display:flex;
align-items:center;
}
</style>
<script type="text/javascript">

var htmlContent=<%out.print("\"" + request.getParameter("htmlContent") + "\"");%>
var typez  = <%out.print("'" + request.getParameter("id") + "'");%>
jQuery(document).ready(function(){
	
	detailPopupDt(htmlContent);

	});
	
function detailPopupDt(img) {

	var ids = img;
var footerOwn;
var footerHire;
var size;
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
				$("#detailDataBody").append($(headerDiv));
				//document.getElementById("enableDetailPopup").click();
					  $.each(jsonValue, function(k, value) {
						
							$.each(value, function(a, val) {
								if(a==4){
									size=val;
								}
								 $("#val"+a).html(val);
								 if(a==15){
									 footerOwn=val; 
								 }if(a==16){
									 footerHire=val;
								 }
								 
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
								var t=true;
								var trr;
									 hit=true;
									  trr= $('#detailDT').find('tr:eq(1)').clone();
									  $('#detailDT tr:eq(1)').remove();
									  var lcTrr ; 
								 $.each(jsonValue, function(q, valuee) {
									var lcTr ;
									 if(hit){
										 lcTr = $(trr).clone();
										 $(lcTr).find("td").removeClass("footer");
										 $(lcTr).find("td").removeClass("total");
										 $(lcTr).find("td").removeClass("footerOwn");
										 $(lcTr).find("td").removeClass("footerHire");
										
									} 
									 if(t){
											lcTrr = $(trr).clone();
									 }
									$.each(valuee, function(w, vall) {
										// $("#val"+a).html(val);
										$(lcTr).find("td#cal"+w).html(vall);
										if(t){
											//lcTrr = $(trr).clone();
											$(lcTrr).find("td#cal"+w).html(vall);
										}
									});
									if(!t){
										  $("#detailDT").append(lcTr);
										
										if($(lcTr).find("td#cal5").html()==$(lcTrr).find("td#cal5").html()&&$(lcTr).find("td#cal4").html()==$(lcTrr).find("td#cal4").html()){
											if($(lcTr).find("td#cal6").html()==$(lcTrr).find("td#cal6").html())
											$(lcTr).find("td#cal6").html("");
											if($(lcTr).find("td#cal7").html()==$(lcTrr).find("td#cal7").html())
											$(lcTr).find("td#cal7").html("");
										}
										lcTrr = $(trr).clone();
										$.each(valuee, function(y, valll) {
											$(lcTrr).find("td#cal"+y).html(valll);
											 $('#test tbody').append(lcTrr);
										});
									}
									if(t){
										  $('#test tbody').append(lcTrr);
										  $("#detailDT").append(lcTr);
										  t=false;
									}
									  
									 
								}); 
								 
								 console.log($('#test').html());
								// $(trr).find("td").attr("id",'');
								 var tfoot =$('<tfoot/>');
								 tfoot.append(trr);
								  $("#detailDT").append(tfoot);

								var tables = $('#detailDT').DataTable({
										"searching": false,
										"paging": false,
										 "columnDefs" : [{"targets":0, "type":"date-eu"}],										 "footerCallback" : function() {
											var api = this.api();
											$('.total').html("TOTAL");
											
											$('.footerOwn').html(footerOwn);
											$('.footerHire').html(footerHire);
											
												$('.footer').each(
														function(val) {
															
																$(this)
																	.html(api.column($(this).index(),{ search:'applied' }, {
																		page : 'all'
																	}).data().sum().toFixed(2));
															
														});
											}
											  
								});
								var hire = $("tfoot td:nth-child(4)").text();
								//alert("hire :"+hire)
								var cost11 = $("tfoot td:nth-child(8)").text();
								//alert("cost11 :"+cost11)
								var cropSup = $("tfoot td:nth-child(10)").text();
								//alert("cropSup :"+cropSup)
								//alert("size :"+size)
								var totP=(parseFloat(hire)+parseFloat(cost11));
								var tot1=(parseFloat(hire)+parseFloat(cost11))/parseFloat(size);
								//alert("tot1 :"+tot1)
								var profit="";
								var profitAc="";
								if(cropSup!="0.00"&&cropSup!="0.0"&&cropSup!="0"){
									 profit = parseFloat(cropSup)-totP;
									//alert("profit :"+profit)
									profitAc=profit/parseFloat(size);
								}
								
								//alert(tot1)
								//alert(size)
								//var totP=tot1/size;
								//alert(size)
								//alert(radnici)
								//alert(cost11)
								//alert((radnici*cost11)/size)
								$('#val12').html(tot1.toFixed(2));
								$('#val13').html(profit.toFixed(2));
								$('#val14').html(profitAc.toFixed(2));
								 $('#detailDT').addClass("dataTableTheme");
								console.log($('#tbDt').html());
							}
						});

			}
			}
		});
		//document.getElementById("enableDetailPopup").click();
	} catch (e) {
		alert(e);
	} 

			
}
 function printDiv() 
{
//window.print();
var printContents = document.getElementById('detailDataBody').innerHTML;
var originalContents = document.body.innerHTML;
$(document.head).append('<style>.dataTableTheme {border-collapse: separate;border: solid black 1px;border-radius: 6px;-moz-border-radius: 6px;table-layout: fixed;overflow: scroll;}.dataTableTheme>thead>tr>th {background: #2a3f54;/* background: lightgrey; */color: #fff;/* 	color:black; */margin: 2px;}.dataTableTheme>thead {height: 50px;}.dataTableTheme>tbody>tr:nth-child(odd), .dataTableTheme>tfoot>tr:nth-child(odd),.dataTableTheme>thead>tr:nth-child(odd) {/* background-color: lightgrey; */border: 1px solid #111;background-color: rgba(168, 227, 214, 0.2);}table.dataTable thead th, table.dataTable thead td, table.dataTable tr th,table.dataTable tr td {padding: 5px;border: 1px solid #111;margin-left: 2px;}#custom td, #custom tr {border: 1px solid #ddd;padding: 8px;font-weight: bold;}#calc td, #custom tr {border: 1px solid #ddd;padding: 8px;font-weight: bold;}</style>');
document.body.innerHTML = printContents;
window.print();
document.body.innerHTML = originalContents;

    //var contents = $("#detailDataBody").html();
    //window.open('data:application/vnd.ms-excel,' + encodeURIComponent(contents));
}
function ExportToExcel() {

	let file = new Blob([$('#detailDataBody').html()], {type:"application/vnd.ms-excel"});
	let url = URL.createObjectURL(file);
	let a = $("<a />", {
	  href: url,
	  download: "ProfitLoss.xls"}).appendTo("body").get(0).click();
} 
</script>
<body>

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
						 <%-- <li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li>  --%>
						<li><a href="#" onclick="ExportToExcel()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					<li><a href="#" onclick="printDiv()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="Print" /></a></li>
					</ul>
				</div>
			</div>
		</div>

 				<div id="detailDataBody">
 				</div>
 				</div>
 				<table id="test" class="hide" >
 				<tbody></tbody>
 				</table>
<script src="//cdn.datatables.net/plug-ins/1.10.11/sorting/date-eu.js" type="text/javascript"></script>
</body>
</html>