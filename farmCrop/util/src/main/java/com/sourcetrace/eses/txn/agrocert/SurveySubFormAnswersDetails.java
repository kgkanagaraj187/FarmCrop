package com.sourcetrace.eses.txn.agrocert;

public class SurveySubFormAnswersDetails{

	private long id;
	private SurveyFarmerCropProdAnswers surveyAnswers;
	private String parentQuestionCode;
	private String childQuestionCode;
	private String answerOrder;
	private int answerType;
	// Main Answer Code (Eg - Text box answer , Drop down code ,Check box ,
	// Radio button Code )
	private String answer;
	// Main Answer Label ( Eg - Drop down Label , Check Box Label , Radio Button
	// Label )
	private String answer1;
	// Sub Answer Code ( Eg - Drop down with other , if yes ask other question )
	private String answer2;
	// Sub Answer Label (Eg - Sub Answer Drop down Label , Sub Answer Check Box
	// Label , Sub Answer
	// Radio Button Label
	private String answer3;
	// If Unit Exist unit Code
	private String answer4;
	// If Unit Exist unit Name
	private String answer5;
	// Unit Other value
	private String answer6;
	private String followUp;
	private Integer componentType;
	private Integer questionOrder;
	private long parentfqa;
	private long childfqa;

	
	//Transient
	private String questionName;
	private String info;
	
	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public SurveyFarmerCropProdAnswers getSurveyAnswers() {

		return surveyAnswers;
	}

	public void setSurveyAnswers(SurveyFarmerCropProdAnswers surveyAnswers) {

		this.surveyAnswers = surveyAnswers;
	}
	
	public String getParentQuestionCode() {
		return parentQuestionCode;
	}

	public void setParentQuestionCode(String parentQuestionCode) {
		this.parentQuestionCode = parentQuestionCode;
	}

	public String getChildQuestionCode() {
		return childQuestionCode;
	}

	public void setChildQuestionCode(String childQuestionCode) {
		this.childQuestionCode = childQuestionCode;
	}

	public int getAnswerType() {

		return answerType;
	}

	public void setAnswerType(int answerType) {

		this.answerType = answerType;
	}

	public String getAnswer() {

		return answer;
	}

	public void setAnswer(String answer) {

		this.answer = answer;
	}

	public String getAnswer1() {

		return answer1;
	}

	public void setAnswer1(String answer1) {

		this.answer1 = answer1;
	}

	public String getAnswer2() {

		return answer2;
	}

	public void setAnswer2(String answer2) {

		this.answer2 = answer2;
	}

	public String getAnswer3() {

		return answer3;
	}

	public void setAnswer3(String answer3) {

		this.answer3 = answer3;
	}

	public String getAnswer4() {

		return answer4;
	}

	public void setAnswer4(String answer4) {

		this.answer4 = answer4;
	}

	public String getAnswer5() {

		return answer5;
	}

	public void setAnswer5(String answer5) {

		this.answer5 = answer5;
	}

	public String getAnswer6() {

		return answer6;
	}

	public void setAnswer6(String answer6) {

		this.answer6 = answer6;
	}

	public String getFollowUp() {

		return followUp;
	}

	public void setFollowUp(String followUp) {

		this.followUp = followUp;
	}

	public Integer getComponentType() {
		return componentType;
	}

	public void setComponentType(Integer componentType) {
		this.componentType = componentType;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAnswerOrder() {
		return answerOrder;
	}

	public void setAnswerOrder(String answerOrder) {
		this.answerOrder = answerOrder;
	}

	public Integer getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(Integer questionOrder) {
		this.questionOrder = questionOrder;
	}

	public long getParentfqa() {
		return parentfqa;
	}

	public void setParentfqa(long parentfqa) {
		this.parentfqa = parentfqa;
	}

	public long getChildfqa() {
		return childfqa;
	}

	public void setChildfqa(long childfqa) {
		this.childfqa = childfqa;
	}

	
	

}
