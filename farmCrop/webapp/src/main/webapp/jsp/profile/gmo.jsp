<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">
	function isDecimal(evt) {
		
		 evt = (evt) ? evt : window.event;
		    var charCode = (evt.which) ? evt.which : evt.keyCode;
		    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
		        return false;
		    }
		    return true;
	}
		function addGMO() {
			var hit = true;
			var samplesRaw = $("#noOfSamplesRaw").val();
			var positiveRaw = $("#noOfPositiveRaw").val();
			var samplesLeaf = $("#noOfSamplesLeaf").val();
			var positiveLeaf = $("#noOfPositiveLeaf").val();
			var samplesSeed = $("#noOfSamplesSeed").val();
			var positiveSeed = $("#noOfPositiveSeed").val();
			jQuery(".error").empty();
			if (isEmpty(samplesRaw) && isEmpty(samplesLeaf) && isEmpty(samplesSeed)) {
				jQuery(".error").append('<s:text name="empty.noOfSamples"/>');
				hit = false;
				return false;
			} else if (isEmpty(positiveRaw) && isEmpty(positiveLeaf) && isEmpty(positiveSeed) )
				{
				jQuery(".error").append('<s:text name="empty.noOfPositive"/>');
				hit = false;
				
			}

			if (hit) {
				alert("--")
				var rawPercent = $("#rawPercentage").text();
				var leafPercent = $("#leafPercentage").text();
				var seedPercent = $("#seedPercentage").text();
				var gmoDatas='';
				if(!isEmpty(samplesRaw) && !isEmpty(positiveRaw))
				{
					
					gmoDatas +=samplesRaw + "###" + positiveRaw + "###"+ rawPercent + "###" + $("#rawType").val()+"@@@";
				}
				if(!isEmpty(samplesLeaf) && !isEmpty(positiveLeaf))
				{
					gmoDatas +=samplesLeaf + "###" + positiveLeaf + "###"+ leafPercent + "###" + $("#leafType").val()+"@@@";
				}
				if(!isEmpty(samplesSeed) && !isEmpty(positiveSeed))
				{
					gmoDatas +=samplesSeed + "###" + positiveSeed + "###"+ seedPercent + "###" + $("#seedType").val()+"@@@";
				}
				
				$.post("gmo_populateSaveGMO.action", {
					gmoDatas : gmoDatas,
				}, function(data, result) {

					if (result == 'success') {
						document.getElementById("divMsg").innerHTML = result;
						document.getElementById("enableModal").click();
						//window.location.href="cropSaleEntryReport_list.action";
					}

				});
				/* 		dataArr.push({
				 noOfSamplesRaw : samplesRaw,
				 noOfPositiveRaw : positiveRaw,
				 rawPercentage : rawPercent,
				 rawType:$("#rawType").val();
				 noOfSamplesLeaf : samplesLeaf,
				 noOfPositiveLeaf : positiveLeaf,
				 leafPercentage : leafPercent,
				 leafType:$("#leafType").val();
				 noOfSamplesSeed : samplesSeed,
				 noOfPositiveSeed : positiveSeed,
				 seedPercentage : seedPercent,
				 seedType:$("#seedType").val();
				 });
				
				 var json = JSON.stringify(dataArr);
				 alert("==="+json)
				 $.ajax({
				 url:'gmo_populateSaveGMO.action',
				 type: 'post',
				 dataType: 'json',
				 data: json,
				 }); */
			}

		}

		function populatePercentage() {
			var samplesRaw = $("#noOfSamplesRaw").val();
			var positiveRaw = $("#noOfPositiveRaw").val();
			var samplesLeaf = $("#noOfSamplesLeaf").val();
			var positiveLeaf = $("#noOfPositiveLeaf").val();
			var samplesSeed = $("#noOfSamplesSeed").val();
			var positiveSeed = $("#noOfPositiveSeed").val();

			if (!isEmpty(samplesRaw) && !isEmpty(positiveRaw)) {
				var percentage = ((parseFloat(positiveRaw) / parseFloat(samplesRaw)) * 100);
				$("#rawPercentage").text(percentage.toFixed(2));
				$("#rawType").val(1);
			}
			if (!isEmpty(samplesLeaf) && !isEmpty(positiveLeaf)) {
				var percentage = (parseFloat(positiveLeaf) / parseFloat(samplesLeaf)) * 100;
				$("#leafPercentage").text(percentage.toFixed(2));
				$("#leafType").val(2);
			}
			if (!isEmpty(samplesSeed) && !isEmpty(positiveSeed)) {
				var percentage = (parseFloat(positiveSeed) / parseFloat(samplesSeed)) * 100;
				$("#seedPercentage").text(percentage.toFixed(2));
				$("#seedType").val(3);
			}
		}
	</script>
	<s:hidden id="seedType" name="seedType"/>
	<s:hidden id="leafType" name="leafType"/>
	<s:hidden id="rawType" name="rawType"/>
	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<h2>
				<s:property value="%{getLocaleProperty('info.gmo')}" />
			</h2>
		</div>
		<div class="error">
			<s:actionerror />
			<s:fielderror />
		</div>

		<div class="row">

			<div class="flex-layout filterControls col-md-4">
				<fieldset class="col-md-12">
					<legend> Raw Cotton </legend>
				</fieldset>
				<div class="flexItem col-md-2">
					<label for="txt"><s:property
							value="%{getLocaleProperty('noOfSamples')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<s:textfield name="gmo.noOfSamples" theme="simple" maxlength="35"
							onkeyup="populatePercentage()"
							onkeypress="return isDecimal(event)" cssClass="form-control "
							id="noOfSamplesRaw" />

					</div>
				</div>
				<div class="flexItem col-md-2">
					<label for="txt"><s:property
							value="%{getLocaleProperty('noOfPositive')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<s:textfield name="gmo.noOfPositive" theme="simple" maxlength="35"
							onkeyup="populatePercentage()"
							onkeypress="return isDecimal(event)" cssClass="form-control "
							id="noOfPositiveRaw" />

					</div>
				</div>
				<div class="flexItem">
					<label for="txt"><s:property
							value="%{getLocaleProperty('contaminPercentage')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<span id="rawPercentage"></span>

					</div>
				</div>
			</div>



			<div class="flex-layout filterControls col-md-4">
				<fieldset class="col-md-12">
					<legend> Lint Cotton </legend>
				</fieldset>
				<div class="flexItem col-md-2">
					<label for="txt"><s:property
							value="%{getLocaleProperty('noOfSamples')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<s:textfield name="gmo.noOfSamples" theme="simple" maxlength="35"
							onkeyup="populatePercentage()"
							onkeypress="return isDecimal(event)" cssClass="form-control "
							id="noOfSamplesLeaf" />

					</div>
				</div>
				<div class="flexItem col-md-2">
					<label for="txt"><s:property
							value="%{getLocaleProperty('noOfPositive')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<s:textfield name="gmo.noOfPositive" theme="simple" maxlength="35"
							onkeyup="populatePercentage()"
							onkeypress="return isDecimal(event)" cssClass="form-control "
							id="noOfPositiveLeaf" />

					</div>
				</div>
				<div class="flexItem">
					<label for="txt"><s:property
							value="%{getLocaleProperty('contaminPercentage')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<span id="leafPercentage"></span>

					</div>
				</div>

			</div>

			<div class="flex-layout filterControls col-md-4">
				<fieldset class="col-md-12">
					<legend> Seed Cotton </legend>
				</fieldset>
				<div class="flexItem col-md-2">
					<label for="txt"><s:property
							value="%{getLocaleProperty('noOfSamples')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<s:textfield name="gmo.noOfSamples" theme="simple" maxlength="35"
							onkeyup="populatePercentage()"
							onkeypress="return isDecimal(event)" cssClass="form-control "
							id="noOfSamplesSeed" />

					</div>
				</div>
				<div class="flexItem col-md-2">
					<label for="txt"><s:property
							value="%{getLocaleProperty('noOfPositive')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<s:textfield name="gmo.noOfPositive" theme="simple" maxlength="35"
							onkeyup="populatePercentage()"
							onkeypress="return isDecimal(event)" cssClass="form-control "
							id="noOfPositiveSeed" />

					</div>
				</div>
				<div class="flexItem">
					<label for="txt"><s:property
							value="%{getLocaleProperty('contaminPercentage')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element flexdisplay">
						<span id="seedPercentage"></span>

					</div>
				</div>


			</div>

		
		</div>
			<div class="flex-layout"
				style="margin-top: 1%">
				<div class="flexItem">
					<button type="button" class="btn btn-sts" id="add"
						onclick="addGMO()">
						<b><s:text name="save.button" /></b>
					</button>
				</div>
			</div>
	</div>
</body>
