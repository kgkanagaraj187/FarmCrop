<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
</head>


<style>			
			#overlay {
				position:fixed; 
				top:0;
				left:0;
				width:100%;
				height:100%;
				background:#000;
				opacity:0.5;
				filter:alpha(opacity=50);
			}

			#modal {
				position:absolute;
				/*background:#565656 0 0 repeat;*/
				background:rgba(0,0,0,0.2);
				border-radius:5px;
				padding:8px;
			}

			#content {
				border-radius:8px;
				background:#fff;
				padding:20px;
				height:auto;
				width:250px;
				height:200px;
				font-family: Arial;
				font-size: 12px;				
			}
			
			#content img{
				width:250px;
				height:200px;	
			}
			
			button {
				border:solid 1px #565656;
				margin:10px 0 0 0px;
				cursor:pointer;
				font-size:12px;
				padding:5px;
			}
			.thClass{
				width:9.2%;
			}
		</style>

</head>
<s:hidden key="icsFarmerDairy.id" id="icsFarmerDairyId" />
<font color="red"> <s:actionerror /></font>

<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="icsFarmerDairy.id" />
	<s:hidden key="id" />
	<s:hidden key="command" />
	<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<h2><s:text name="info.farmerInspectionIcsReport" /></h2>
							<div class="dynamicFieldsRender"></div>
											
						</div>
					</div>
				</div>
			</div>
		</div>
	
	<br />

	<br />
	<div class="yui-skin-sam"><span id="cancel" class=""><span
		class="first-child">
	<button type="button" class="back-btn btn btn-sts"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>

</s:form>
<s:form name="cancelform" action="icsFarmerDairyReport_list">
	<s:hidden key="currentPage" />
</s:form>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document" style="width: 300px;">
		<div class="modal-content">
			<img src="" id="img" class="img-rounded center-block"
				style="width: 100%;" height=250 />
		</div>
		<div class="modal-footer">
			<center>
				<button type="button" class="btn btn-sts" data-dismiss="modal">OK</button>
			</center>
		</div>
	</div>
</div>
	<script src="js/dynamicComponents.js?v=1.14"></script>
<script type="text/javascript">

$(document).ready(function(){			
	renderDynamicDetailFeildsByTxnType();
});

function getId(){
	var type= '<s:property value="id"/>';
	return type;
}

function getTxnType(){
	return "384";
}

function getBranchIdDyn(){
		if('<s:property value="getBranchId()"/>'==null ||'<s:property value="getBranchId()"/>'=='' ){
			return '<s:property value="icsFarmerDairy.branchId"/>';
		}else{
			return '<s:property value="getBranchId()"/>';
		}
	}
	
</script>