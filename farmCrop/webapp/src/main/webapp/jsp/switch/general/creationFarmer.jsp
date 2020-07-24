<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<body>
<s:form name="form" cssClass="fillform" action="creationFarmer_create" 	method="post" enctype="multipart/form-data" id="target">
<s:hidden id="componentDatas" name="componentDatas"/>
	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<h2>
				<s:property value="%{getLocaleProperty('info.creation')}" />
			</h2>
			<div class="flexiWrapper">
				<div class="flexform-item">
					<label for="txt">Section<sup style="color: red;">*</sup></label>
					<div class="form-element">
						<select id="section" class="form-control">
						</select>
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt">Component Type<sup style="color: red;">*</sup></label>
					<div class="form-element">
						<select id="compoType" class="form-control"
							onchange="showDependentFields(this.value)">
							<option value="">Select</option>
							<option value="1">Text Box</option>
							<option value="2">Radio</option>
							<option value="3">Date picker</option>
							<option value="4">Drop Down</option>
							<option value="5">List</option>
						</select>
					</div>
				</div>

				<div class="flexform-item ">
					<label for="txt">Component Name<sup style="color: red;">*</sup></label>
					<div class="form-element">
						<input type="text" class="form-control" id="compoName">
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt">Is Required?</label>
					<div class="form-element">
						<input type="radio" value="1" class="form-control" name="isReq">Yes
						<input type="radio" value="0" class="form-control" name="isReq"
							checked>No
					</div>
				</div>

				<div class="flexform-item textbox dependentFields">
					<label for="txt">Max Len</label>
					<div class="form-element">
						<input type="text" class="form-control" id="maxLen">
					</div>
				</div>

				<div class="flexform-item textbox dropDown dependentFields">
					<label for="txt">Default Value</label>
					<div class="form-element">
						<input type="text" class="form-control" id="defValue">
					</div>
				</div>

				<div class="flexform-item dropDown dependentFields">
					<label for="txt">Catalogue</label>
					<div class="form-element">
						<select id="catalogue" class="form-control">
						</select>
					</div>
				</div>

				<div class="flexform-item datePicker dependentFields">
					<label for="txt">Date Format</label>
					<div class="form-element">
						<input type="text" class="form-control" id="dateFormat">
					</div>
				</div>

			</div>

			<div class="row margin-top-10">
				<div class="col-md-2">
					<button type="button" class="btn btn-success form-control"
						onclick="addTable()">Add</button>
				</div>
			</div>

		</div>
	</div>

	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<h2>Components List</h2>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-bordered">
						<thead>
							<tr>
								<td style="width: 30%">Section</td>
								<td style="width: 20%">Component Type</td>
								<td style="width: 30%">Component Name</td>
								<td style="width: 20%">Action</td>
							</tr>
						</thead>
						<tbody id="fList">
						</tbody>
					</table>
				</div>
			</div>

			<div class="row margin-top-10">
				<div class="col-md-2">
					<button type="button" class="btn btn-success form-control"
						onclick="save()">Save</button>
				</div>
			</div>

		</div>
	</div>

	<script>
		jQuery(document).ready(
				function() {
					hideDependentFields();
					getAjaxValue("creationFarmer_populateSection", "",
							"section", "0");
					getAjaxValue("creationFarmer_populateCatalogue", "",
							"catalogue", "1");
		});

		function hideDependentFields() {
			$('.dependentFields').addClass("hide");
		}

		function showDependentFields(val) {
			$('.dependentFields').addClass("hide");
			if (val == '1') {
				$(".textbox").removeClass("hide");
			} else if (val == '2') {
				$(".radio").removeClass("hide");
			} else if (val == '3') {
				$(".datePicker").removeClass("hide");
			} else if (val == '4') {
				$(".dropDown").removeClass("hide");
			} else {
				$(".textbox").addClass("hide");
			}
		}
		function addTable() {
			var sectionId = getElementValueById("section");
			var compoType = getElementValueById("compoType");
			var compoName = getElementValueById("compoName");
			var isReq = getRadioValueByName("isReq");
			var maxLen = getElementValueById("maxLen");
			var defValue = getElementValueById("defValue");
			var dateFormat = getElementValueById("dateFormat");
			var catalogue = getElementValueById("catalogue");

			var sectionText = getElementValueByText("section");
			var compoText = getElementValueByText("compoType");
			var catalogueText = getElementValueByText("catalogue");

			var tr = $("<tr/>");
			tr.append(td("sectionText", sectionText));
			tr.append(td("compoText", compoText));
			tr.append(td("compoName", compoName));

			tr.append(td("sectionId hide", sectionId));
			tr.append(td("compoType hide", compoType));
			tr.append(td("isReq hide", isReq));
			tr.append(td("maxLen hide", maxLen));
			tr.append(td("defValue hide", defValue));
			tr.append(td("dateFormat hide", dateFormat));
			tr.append(td("catalogue hide", catalogue));

			var action = "<td><button class='btn btn-danger' title='Delete' onclick='deleteRow(this)'><i class='fa fa-trash' aria-hidden='true'/></button>&nbsp;&nbsp;<button class='btn btn-primary' title='Duplicate' onclick='editRow(this)'><i class='fa fa-pencil' aria-hidden='true'/></button>&nbsp;&nbsp;<button class='btn btn-info' title='Duplicate' onclick='duplicateRow(this)'><i class='fa fa-files-o' aria-hidden='true'/></button></td>";
			tr.append(action);
			$("#fList").append(tr);
		}

		function td(classz, value) {
			var td = $("<td/>");
			td.addClass(classz);
			td.html(value);
			return td;
		}

		function deleteRow(obj) {
			$(obj).parent().parent().remove();
		}

		function duplicateRow(obj) {
			var trow = $(obj).parent().parent();

			var tr = $("<tr/>");
			tr.append(td("sectionText", trow.find(".sectionText").html()));
			tr.append(td("compoText", trow.find(".compoText").html()));
			tr.append(td("compoName", ""));

			tr.append(td("sectionId hide", trow.find(".sectionId").html()));
			tr.append(td("compoType hide", trow.find(".compoType").html()));
			tr.append(td("isReq hide", trow.find(".isReq").html()));
			tr.append(td("maxLen hide", trow.find(".maxLen").html()));
			tr.append(td("defValue hide", trow.find(".defValue").html()));
			tr.append(td("dateFormat hide", trow.find(".dateFormat").html()));
			tr.append(td("catalogue hide", trow.find(".catalogue").html()));

			var action = "<td><button class='btn btn-danger' title='Delete' onclick='deleteRow(this)'><i class='fa fa-trash' aria-hidden='true'/></button>&nbsp;&nbsp;<button class='btn btn-primary' title='Duplicate' onclick='editRow(this)'><i class='fa fa-pencil' aria-hidden='true'/></button>&nbsp;&nbsp;<button class='btn btn-info' title='Duplicate' onclick='duplicateRow(this)'><i class='fa fa-files-o' aria-hidden='true'/></button></td>";
			tr.append(action);
			$("#fList").append(tr);
		}

		function editRow(obj) {
			var trow = $(obj).parent().parent();
			$("#compoName").val("");
			var componentType = trow.find(".compoType").html();
			$("#compoType").val(componentType).trigger("change");
		}

		function save() {
			var dataArr = new Array();
			$("#fList").find("tr").each(function(i, v) {
				var trow = $(this);
				var sectionId = trow.find(".sectionId").html();
				var compoType = trow.find(".compoType").html();
				var compoName = trow.find(".compoName").html();
				var isReq = trow.find(".isReq").html();
				var maxLen = trow.find(".maxLen").html();
				var defValue = trow.find(".defValue").html();
				var dataFormat = trow.find(".dateFormat").html();
				var catalogue = trow.find(".catalogue").html();

				dataArr.push({
					sectionCode : sectionId,
					name : compoName,
					compontType : compoType,
					isRequired : isReq,
					maxLength : maxLen,
					defaultValue : defValue,
					dataFormat : dataFormat,
					catalogue : catalogue
				});
				
				console.log(dataArr);
				
				
			});
			
			
			var json = JSON.stringify(dataArr);
			$("#componentDatas").val(json);
			$("#target").submit();
			 
		}

		function getAjaxValue(url, dataa, id, needSelect) {
			var resp = $.ajax({
				url : url,
				data : dataa,
				type : 'post',
				async : false,
				success : function(data, result) {
					if (!result)
						alert('Failure to load data');
				}
			}).responseText;

			insertJQOptions(id, JSON.parse(resp), needSelect);
		}

		function insertJQOptions(ctrlName, jsonArr, needSelect) {
			document.getElementById(ctrlName).length = 0;
			if (needSelect == '1') {
				addOption(document.getElementById(ctrlName), "Select", "");
			}
			for (var i = 0; i < jsonArr.length; i++) {
				addOption(document.getElementById(ctrlName), jsonArr[i].name,
						jsonArr[i].id);
			}
			var id = "#" + ctrlName;
			jQuery(id).select2();
		}
	</script>
</s:form>
</body>
</html>