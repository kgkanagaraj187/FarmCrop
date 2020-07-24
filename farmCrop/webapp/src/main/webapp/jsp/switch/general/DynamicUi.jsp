<%@ include file="/jsp/common/form-assets.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="swithlayout">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" href="bootstrap.min.css">
<script src="jquery.min.js"></script>
<script src="bootstrap.min.js"></script>
<script src="js/bootpopup.js"></script>
<script src="js/bootpopup.min.js"></script>





</head>

<body>
	<script src="js/dynamicUi_Related/dynamicUI.js?v=25"></script>
	<script src="js/dynamicUi_Related/DynamicUi_validationRelated.js?v=20"></script>
	<script src="js/dynamicUi_Related/readDynamicUI.js?v=20"></script>
	<script src="js/dynamicComponents.js?v=20.20"></script>

	<!-- jquery-confirmRelatedPlugins -->

	<link rel="stylesheet"
		href="plugins/jquery-confirmRelatedPlugins/jquery-confirm.min.css">
	<script
		src="plugins/jquery-confirmRelatedPlugins/jquery-confirm.min.js"></script>

	<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.0/jquery-confirm.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.0/jquery-confirm.min.js"></script> -->

	<!-- jquery-confirmRelatedPlugins -->


	<style>
.left {
	width: 30%;
}

.right {
	width: 70%;
	/* background-color: #2a3f54; */
}

#sourceList {
	padding: 10px;
}

#sourceList>.column>button {
	padding: 10px;
	margin-top: 5px;
	width: 15%;
	background-color: #1abc9c;
	text-align: left;
}

#sourceList>.sectionColumn>button {
	width: 30%;
	background-color: #1abc9c;
}

#targetContainer {
	width: 100%;
	height: 481px;
	max-height: 481px;
	border: 1px solid black;
	overflow: scroll;
}

/* nav-tab related styles  */
.active {
	background-color: white;
	/* color: #254441; */
}
/*  */
.hilightField {
	border: 2px solid yellow !important;
	outline-style: solid !important;
	outline-color: #ff9900 !important;
}

.hilightlist {
	border: 2px solid red !important;
	outline-style: solid !important;
	outline-color: #ff9900 !important;
}
/* Catalogue Values table inside popup */
.table thead tr th {
	background-color: black;
	color: white;
}

/* Catalogue Values table inside popup  */
</style>







	<script type="text/javascript">

	$(document).ready(function() {
		 $(".select2").select2(); // If we use select2 older dropdown hided then new dropdown will select, so we cant get older drop down backernd-data and onclick functionality
		 $("#sortableSection").sortable();
		  
	});
		
	
	
</script>


	<div class="appContentWrapper marginBottom">
		<div id="errorDiv" class="error">
			<sup>*</sup>Mandatory Field
		</div>

		<div class="formContainerWrapper">
			<h2>Dynamic Question Configuration</h2>

			<div class="flexform">


				<div class="column left">

					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#addQuestion">Add</a></li>
						<li><a data-toggle="tab" href="#Edit">Edit</a></li>
						<li><a data-toggle="tab" href="#existingQuestion">Existing
								Question</a></li>

					</ul>

					<div class="tab-content">
						<div id="addQuestion" class="tab-pane fade in active">

							<div class="container" id="sourceList">

								<div class="sectionColumn">

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="section">
										<span class="glyphicon "> Section</span>
									</button>

								</div>

								<div class="column">

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="textBox">
										<span class="glyphicon "> Text Box</span>
									</button>

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="textarea">
										<span class="glyphicon "> Text Area</span>
									</button>

								</div>

								<div class="column">

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="drop-down">
										<span class="glyphicon "> Drop-Down</span>
									</button>

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="multiSelect">
										<span class="glyphicon "> Multi Select</span>
									</button>

								</div>

								<div class="column">

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="radio-group">
										<span class="glyphicon "> Radio-group</span>
									</button>

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="checkbox-group">
										<span class="glyphicon "> Checkbox-group </span>
									</button>

								</div>

								<div class="column">

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="date-picker">
										<span class="glyphicon "> Date-picker</span>
									</button>

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="label">
										<span class="glyphicon "> Label</span>
									</button>

								</div>

								<div class="column">

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="photo">
										<span class="glyphicon "> Photo</span> Section
									</button>

									<button type="button" class="btn btn-success" draggable="true"
										ondragstart="drag(event)" data-type="list">
										<span class="glyphicon "> List</span>
									</button>

								</div>



								<button type="button" class="btn btn-warning"
									style="width: 30%; margin-top: 10px;" onclick="saveMenu();">
									<span class="glyphicon "> Save menu</span>
								</button>


							</div>

						</div>

						<div id="Edit" class="tab-pane fade"></div>

						<div id="existingQuestion" class="tab-pane fade"></div>




					</div>
				</div>


				<div class="column right">

					<div class="col-sm-9" ondrop="drop(event)"
						ondragover="allowDrop(event)" id="targetContainer">
						<ul id="sortableSection"></ul>
					</div>

				</div>


			</div>



		</div>
	</div>



</body>
</html>