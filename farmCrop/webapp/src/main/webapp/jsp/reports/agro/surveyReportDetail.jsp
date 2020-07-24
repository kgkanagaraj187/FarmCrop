<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	jQuery(document).ready(function() {
		var surveyEdit="<%=request.getParameter("surveyEdit")%>";
		jQuery(".back-btn").click(function() {
			if(surveyEdit!='' && surveyEdit=="1"){
				document.cancelform.action = 'surveyReport_list.action?surveyEdit=1';
			}
			document.cancelform.submit();
		});
		//alert('<s:property value="getCurrentLanguage()"/>');
		 if(surveyEdit!='' && surveyEdit=="1"){
			 $(".breadCrumbNavigation").find('li:not(:first)').remove();
				
				$(".breadCrumbNavigation").append("<li><a href='surveyReport_list.action?surveyEdit=1'> <s:text name='service.editSurvey'/> </a></li> ");
			
		 }
		 
		 var  url = window.location.href;
	  	  var command=$("#command").val();
	  	if(url.indexOf('?id=') < 0){
	  		alert("22");
	  		if(surveyEdit!='' && surveyEdit=="1"){
	    	  url = url+'?surveyEdit=1&id='+'<s:property value="fcpa.id"/>';
	  		}else{
	  			 url = url+'?id='+'<s:property value="fcpa.id"/>';
	  		}
	     
	      $( ".lanMenu" ).each(function() {
		    	 var url1 = $(this).attr("href") +'&url='+ encodeURIComponent(url);
		    	  $( this ).attr( "href",url1);
		    	});
		     }


	    
		jQuery(".edit-btn").click(function() {
			document.editForm.id = '<s:property value="fcpa.id"/>';
			document.editForm.surveyEdit  =surveyEdit;
			document.editForm.pageAction  ='editFull';
			document.editForm.submit();
		});
		jQuery(".delete-btn").click(function() {
			document.removeForm.id = '<s:property value="fcpa.id"/>';
			document.removeForm.surveyEdit  =surveyEdit;
			document.removeForm.submit();
		});
		
		
	});
	
	
</script>
<s:hidden key="fcpa.id" id="id" />
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">

	<s:hidden key="command" />
	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2>						
						<s:property
										value="%{getLocaleProperty('survey.info')}" />
					</h2>
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="fcpa.survey"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="fcpa.surveyCode" /> - <s:property value="fcpa.surveyName" />
						</p>
					</div>
					
						<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="survey.dataCollector"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="fcpa.dataCollectorId" /> - <s:property value="fcpa.dataCollectorName" />
						</p>
					</div>
					
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="profile.samithi"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="fcpa.cooperativeCode" /> - <s:property value="fcpa.farmerOrgName" />
						</p>
					</div>
					
						<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="farm.farmerName"></s:text>
						</p>
						<p class="flexItem">
							 <s:property value="fcpa.farmerName" />
						</p>
					</div>
					
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="survey.Comment"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="fcpa.comment" /> 
						</p>
					</div>
					
					</div>
						</div>

			</div>
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper dynamic-form-con">
					<h2>						
						<s:property
										value="%{getLocaleProperty('survey.info.question')}" />
					</h2>
		<table class="table table-bordered aspect-detail">
		
  <s:set var="serialNoCount" value="0"/>
  <s:iterator value="farmerSections" >
    <s:if test='farmersQuestionAnswers.size()>0'>
      <s:if test="sectionCode=='S05' || sectionCode=='S06'">				     
        <tr>
          <th  colspan="50">            
              <s:property value="sectionName"/>            
          </th>
        </tr>				
        <tr style="font-weight: bold;">
          <td colspan="6" >
            <s:text name="s.no">
            </s:text>
          </td>
          <td colspan="6"  >
            <s:text name="question.code"/>
          </td>
          <td colspan="6"  >
            <s:text name="question.name"/>
          </td>
          <td colspan="6"  >
            <s:text name="estimated.hours"/>
          </td>
          <td colspan="6" >
            <s:text name="answers"/>
          </td>
          <td colspan="6" >
            <s:text name="nonConfirmed"/>
          </td>
           <td colspan="6" >
            <s:text name="actionPlan" />
          </td>
           <td colspan="6" >
            <s:text name="deadline" />
          </td>           
           <td class="fw hide" colspan="6">
            <s:text name="actionPlanStatu" />
          </td>
          <td colspan="6" >
            <s:text name="image"/>
          </td>
   
        </tr>
      </s:if>
      <s:else>
        <tr>
          <th id="thCol"  colspan="50">            
              <s:property value="sectionName"/>            
          </th>
        </tr>				
        <tr style="font-weight: bold;">
          <td colspan="6" >
            <s:text name="s.no">
            </s:text>
          </td>
          <td style="font-weight: bold;" colspan="6"  >
            <s:text name="question.code"/>
          </td>
          <td style="font-weight: bold;" colspan="6"  >
            <s:text name="question.name"/>
          </td>				
          <td width="30%" colspan="6" >
            <s:text name="answers"/>
          </td>
          <td width="25%" colspan="6" >
            <s:text name="nonConfirmed"/>
          </td>
           <td class="fw hide" width="35%" colspan="6" >
            <s:text name="actionPlan" />
          </td>
           <td class="fw hide" width="35%" colspan="6" >
            <s:text name="deadline" />
          </td>
            <td class="fw hide" width="35%" colspan="6" >
            <s:text name="actionPlanStatu" />
          </td>
          <td width="35%" colspan="6" >
            <s:text name="image" />
          </td>
            
        
        </tr>			
      </s:else>
      <s:iterator value="farmersQuestionAnswers" status="farmersQuestionAnswersCount" var="farmersQuestionAnswers" >
        <s:set var="serialNoCount" value="%{#serialNoCount+1}"/>
        <s:if test="question.questionType>2">
          <tr>
            <td  colspan="6" >
              <s:property value="%{#serialNoCount}" />
            </td>						
            <td  colspan="6" >
              <s:property value="questionCode"/>
            </td>				
            <td  colspan="6" >
              <s:property value="questionName"/>
            </td>				
            <s:if test="sectionCode=='S05' || sectionCode=='S06'">
              <td  colspan="6" >
              </td>
            </s:if>											
            <td  class="break-word" colspan="6">
            
             <s:if test='answersList.size()>0 && answersList[0].answer=="CT136"'>
                  <s:text name="idontKnow" /> 
                </s:if>
            </td>
            <td align="center" colspan="6" >
              <s:checkbox name="nonConfirmed" disabled="true"/>
            </td>
            <td colspan="6" > 
              <s:if test="image.length>0">
                <img border="0" style="width:150px;height:133px;" src="data:image/png;base64,<s:property value="photoByteString"/>">
              </s:if>
              <s:elseif test="image.length==0">
                <img style="width:150px;height:133px;" border="0" src="assets/client/demo/images/no-image.png">
              </s:elseif>
            </td>
          </tr>
          <s:if test="subQuestionAnswers.size()>0">
            <tr>
              <td colspan="50">
                <table class="table table-bordered aspect-detail" >
                  <tr>
                  <td class="break-word" colspan="15" >
                        <s:text name="s.no"/>
                      </td>	
                    <s:iterator value="subQuestionList" status="subQuestionListCount" var="subQuestionList"  >
                      <td class="break-word" colspan="15" >
                        <s:property value="questionName"/>
                      </td>		
                    </s:iterator>
                  </tr>
                  <s:iterator value="subQuestionAnswers" status="subQuestionAnswersCount" var="subQuestionAnswers"  >
                    <tr>
                      <td class="break-word" colspan="15" >
                           <s:property
								value="#subQuestionAnswersCount.index+1" />
                      </td>	
                      <s:iterator value="value" >
                        <td class="break-word" colspan="15" >
                          <s:if test='answersList.size()>0 && answersList[0].answer=="CT136"'>
                  <s:text name="idontKnow" /> 
                </s:if>
                          <s:elseif test='farmersQuestionAnswers.componentType=="1"'>
                            
                              <s:property value="answer"/>
                           &nbsp
                            <s:property value="answer5"/>
                            <s:if test='answer6!=null'>&nbsp;-&nbsp;
                              <s:property value="answer6"/>
                            </s:if>
                          </s:elseif>
                          <s:elseif test='farmersQuestionAnswers.componentType=="5"'>
                           <s:if test='answer1!=null'>
                              <s:property value="answer1"/>
                            </s:if>
                         
                          </s:elseif>
                          <s:elseif test='farmersQuestionAnswers.componentType=="3"'>
                              <s:if test='answer1!=null'>
                              <s:property value="answer1"/>
                            </s:if>
                           
                          </s:elseif> 
                          <s:elseif test='farmersQuestionAnswers.componentType=="7"'>
                              <s:if test='answer1!=null'>
                              <s:property value="answer1"/>
                            </s:if>
                            <s:else>
                              <s:property value="answer"/>
                            </s:else>
                           
                             
                          </s:elseif>
                          <s:elseif test='farmersQuestionAnswers.componentType=="9"'>
                           <s:if test='answer!=null'>
                              <s:property value="answer"/>
                          </s:if>
                          </s:elseif>
                          <s:elseif test='farmersQuestionAnswers.componentType=="10"'>
                            <s:property value="getFormatedDate(answer)"/>
                          </s:elseif>
                          <s:elseif test='farmersQuestionAnswers.componentType=="2"'>
                             <s:if test='answer!=null'>
                              <s:property value="answer"/>
                            </s:if>
                          </s:elseif>
                          <s:elseif test='farmersQuestionAnswers.componentType=="12"'>
                             <s:if test='answer1!=null'>
                              <s:property value="answer1"/>
                            </s:if>
                          
                          </s:elseif>
                          <s:else>
                          <s:if test='answer1!=null'>
                              <s:property value="answer1"/>
                            </s:if>
                            <s:else>
                              <s:property value="answer"/>
                            </s:else>&nbsp
                          </s:else>  
                        </td>
                      </s:iterator>
                    </tr>
                  </s:iterator>
                </table>
          </s:if>
          </s:if>
          <s:else>
            <tr>
              <td class="break-word" colspan="6" >
                <s:property value="%{#serialNoCount}" />
              </td>						
              <td class="break-word" colspan="6" >
                <s:property value="questionCode"/>
              </td>				
              <td class="break-word" colspan="6" >
                <s:property value="questionName"/>
              </td>				
              <s:if test="sectionCode=='S05' || sectionCode=='S06'">
                <td class="break-word" colspan="6" >
                  <s:iterator value="answers">
                    <s:property value="answer2"/>
                  </s:iterator>
                </td>
              </s:if>											
              <td  class="break-word" colspan="6" >
                <s:if test='answersList.size()>0 && answersList[0].answer=="CT136"'>
                  <s:text name="idontKnow" /> 
                </s:if>
                <s:elseif test='componentType=="0"'>
                  <s:iterator value="answers">
                    <s:property value="answer"/>&nbsp
                    <s:property value="answer5"/>
                    <s:if test='answer6!=null'>&nbsp;-&nbsp;
                      <s:property value="answer6"/>
                    </s:if>
                  </s:iterator>
                </s:elseif>
                <s:elseif test='componentType=="1"'>
                  <s:iterator value="answers">
                    <s:property value="answer"/>&nbsp
                    <s:property value="answer5"/>
                    <s:if test='answer6!=null'>&nbsp;-&nbsp;
                      <s:property value="answer6"/>
                    </s:if>
                  </s:iterator>
                </s:elseif>
                <s:elseif test='componentType=="2"'>
                  <s:iterator value="answers">
                    <s:property value="answer"/>
                  </s:iterator>
                </s:elseif>
                <s:elseif test='componentType=="3"'>
                  <s:iterator value="answers">
                    <s:property value="getMultiSelectAnswerString(#farmersQuestionAnswers)" />
                  </s:iterator>
                </s:elseif> 
                <s:elseif test='componentType=="8" || componentType=="4"'>
                  <s:property value="otherAnswer" />
                </s:elseif> 
                <s:elseif test='componentType=="7"'>
                  <s:if test="!nonConfirmed" >
                    <s:property value="getMultiSelectAnswerString(#farmersQuestionAnswers)" />
                  </s:if>
                </s:elseif>
                <s:elseif test='componentType=="9"'>
                  <s:iterator value="answers">
                    <s:property value="answer"/>&nbsp
                    <s:property value="answer5"/>
                    <s:if test='answer6!=null'>&nbsp;-&nbsp;
                      <s:property value="answer6"/>
                    </s:if>
                  </s:iterator>
                </s:elseif>
                <s:elseif test='componentType=="10"'>
                  <s:iterator value="answers">
                    <s:property value="answer"/>
                  </s:iterator>
                </s:elseif>
                <s:elseif test='componentType=="12" || componentType=="5" || componentType=="13" || componentType=="6"'>
                  <s:if test="!nonConfirmed" >
                    <s:property value="getMultiSelectAnswerString(#farmersQuestionAnswers)"/>
                  </s:if>
                </s:elseif>
                <s:else>
                  <s:iterator value="answers">
                    <s:property value="answer"/>
                  </s:iterator>
                </s:else>
              </td>
              <td align="center" colspan="6" >
                <s:checkbox name="nonConfirmed" disabled="true"/>
              </td>
             
               <td class="fw hide" align="center" colspan="6" >
                 <s:property value="actionPlan"/>
              </td>
               <td class="fw hide" align="center" colspan="6" >
                 <s:property value="deadLine"/>
              </td>
              <td class="fw hide" align="center" colspan="6" >
                 <s:property value="actionPlanStatuss"/>
              </td>
              <td colspan="6" > 
                <s:if test="image.length>0">
                  <img border="0" style="width:150px;height:133px;" src="data:image/png;base64,<s:property value="photoByteString"/>">
                </s:if>
                <s:elseif test="image.length==0">
                  <img style="width:150px;height:133px;" border="0" src="assets/client/demo/images/no-image.png">
                </s:elseif>
              </td>
            </tr>
          </s:else>
          </s:iterator>
        </s:if>
      </s:iterator>		
    </table>
</div>
</div>
	</div>
					<div class="yui-skin-sam">

						<span id="cancel" class=""> <span class="first-child">
								<button type="button" class="back-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button>
						</span>
						</span>
						<s:if test='surveyEdit.equalsIgnoreCase("1")'>
						<span id="edit" class=""> <span class="first-child">
								<button type="button" class="edit-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="edit.button" />
									</font></b>
								</button>
						</span>
						</span>
						
							<span id="edit" class=""> <span class="first-child">
								<button type="button" class="delete-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="delete.button" />
									</font></b>
								</button>
						</span>
						</span>
						</s:if>
					</div>
			

		</div>

	</div>

	<br />

</s:form>



<s:form name="cancelform" action="surveyReport_list.action?">
	<s:hidden key="id" />	
</s:form>

<s:form name="editForm" action="survey_createPartial.action">
	<s:hidden name="id" />	
	<s:hidden name="pageAction" />	
	<s:hidden name="surveyEdit" />
</s:form>

<s:form name="removeForm" action="surveyReport_removeSurvey.action">
	<s:hidden name="id" />
</s:form>

<script type="text/javascript">

</script>