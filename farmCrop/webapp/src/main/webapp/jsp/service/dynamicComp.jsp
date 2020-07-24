			<div id="audioDiv" class="hide">
				<div style="width: 45%;">
					<div id="jquery_jplayer_1" class="jp-jplayer"></div>
					<div id="jp_container_1" align="center" class="jp-video " role="application"
						aria-label="media player">
						<div class="jp-type-single">
							<div id="jquery_jplayer_1" class="jp-jplayer"></div>
							<div class="jp-gui">
								<div class="jp-video-play">
									<button class="jp-video-play-icon" role="button" tabindex="0">play</button>
								</div>
								<div class="jp-interface">
									<div class="jp-progress">
										<div class="jp-seek-bar">
											<div class="jp-play-bar"></div>
										</div>
									</div>
									<div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>
									<div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>
									<div class="jp-details">
										<div class="jp-title" aria-label="title">&nbsp;</div>
									</div>
									<div class="jp-controls-holder">
										<div class="jp-volume-controls">
											<button class="jp-mute" role="button" tabindex="0">mute</button>
											<button class="jp-volume-max" role="button" tabindex="0">max
												volume</button>
											<div class="jp-volume-bar">
												<div class="jp-volume-bar-value"></div>
											</div>
											
										</div>
										<div style="left: 456px; position: absolute; top: 0px;">
												<button type="button" class="fa fa-times"
													onclick="audioControlHide();" />
											</div>
										<div class="jp-controls">
											<button class="jp-play" role="button" tabindex="0">play</button>
											<button class="jp-stop" role="button" tabindex="0">stop</button>
										</div>
										<div class="jp-toggles">
											<button class="jp-repeat" role="button" tabindex="0">repeat</button>
											<button class="jp-full-screen" role="button" tabindex="0">full
												screen</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					</div>
					</div>
					
	<s:form id="audioFileDownload" action="farmer_populateVideoPlay">
	<s:hidden id="loadId" name="imgId" />
</s:form>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>
<script>
		jQuery(document).ready(function() {
			audioControlHide();
		});
		
		function audioControlHide(){
			$("#jquery_jplayer_1").jPlayer("stop");
			jQuery("#audioDiv").hide();
			jQuery("#audioDataDiv").hide();
		}

	</script>




