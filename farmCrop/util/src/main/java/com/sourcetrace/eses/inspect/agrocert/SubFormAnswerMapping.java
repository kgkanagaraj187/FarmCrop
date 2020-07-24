package com.sourcetrace.eses.inspect.agrocert;

import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersQuestionAnswers;

public class SubFormAnswerMapping  implements Comparable<SubFormAnswerMapping> {

	private long id; 
	private SurveyFarmersQuestionAnswers subFormParentQuestion;// parent
	private SurveyFarmersQuestionAnswers subFormChildQuestion;// child
	private SurveyAnswers answer;
	private int answerOrder;
	
	
	public int compareTo(SubFormAnswerMapping obj) {
		int value = 0;
		SubFormAnswerMapping subFormMapping = (SubFormAnswerMapping) obj;

		if (this.answerOrder > subFormMapping.answerOrder)
			value = 1;
		else if (this.answerOrder < subFormMapping.answerOrder)
			value = -1;
		else if (this.answerOrder == subFormMapping.answerOrder)
			value = 0;
		return value;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

 


	public SurveyAnswers getAnswer() {
		return answer;
	}


	public void setAnswer(SurveyAnswers answer) {
		this.answer = answer;
	}


	public SurveyFarmersQuestionAnswers getSubFormParentQuestion() {
		return subFormParentQuestion;
	}


	public void setSubFormParentQuestion(SurveyFarmersQuestionAnswers subFormParentQuestion) {
		this.subFormParentQuestion = subFormParentQuestion;
	}


	public SurveyFarmersQuestionAnswers getSubFormChildQuestion() {
		return subFormChildQuestion;
	}


	public void setSubFormChildQuestion(SurveyFarmersQuestionAnswers subFormChildQuestion) {
		this.subFormChildQuestion = subFormChildQuestion;
	}


	public int getAnswerOrder() {
		return answerOrder;
	}


	public void setAnswerOrder(int answerOrder) {
		this.answerOrder = answerOrder;
	}

 
	
}