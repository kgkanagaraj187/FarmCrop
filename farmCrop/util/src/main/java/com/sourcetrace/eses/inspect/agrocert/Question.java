/*
 * Question.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.Set;

import org.eclipse.jdt.internal.core.NameLookup.Answer;

import com.sourcetrace.eses.txn.agrocert.Answers;

/**
 * The Class Question.
 */
public class Question {

    private long id;
    private String code;
    private String serialNo;
    private String name;
    private String questionType;
    private Section section;
    private Question parentQuestion;
    private Set<Question> subQuestions;
    private long revisionNumber;
    private String componentType;
    
  //  Transient Variable
    private Answers answer;
    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the serial no.
     * @return the serial no
     */
    public String getSerialNo() {

        return serialNo;
    }

    /**
     * Sets the serial no.
     * @param serialNo the new serial no
     */
    public void setSerialNo(String serialNo) {

        this.serialNo = serialNo;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the question type.
     * @return the question type
     */
    public String getQuestionType() {

        return questionType;
    }

    /**
     * Sets the question type.
     * @param questionType the new question type
     */
    public void setQuestionType(String questionType) {

        this.questionType = questionType;
    }

    /**
     * Gets the section.
     * @return the section
     */
    public Section getSection() {

        return section;
    }

    /**
     * Sets the section.
     * @param section the new section
     */
    public void setSection(Section section) {

        this.section = section;
    }

    /**
     * Gets the parent question.
     * @return the parent question
     */
    public Question getParentQuestion() {

        return parentQuestion;
    }

    /**
     * Sets the parent question.
     * @param parentQuestion the new parent question
     */
    public void setParentQuestion(Question parentQuestion) {

        this.parentQuestion = parentQuestion;
    }

    /**
     * Gets the sub questions.
     * @return the sub questions
     */
    public Set<Question> getSubQuestions() {

        return subQuestions;
    }

    /**
     * Sets the sub questions.
     * @param subQuestions the new sub questions
     */
    public void setSubQuestions(Set<Question> subQuestions) {

        this.subQuestions = subQuestions;
    }

    /**
     * Gets the revision number.
     * @return the revision number
     */
    public long getRevisionNumber() {

        return revisionNumber;
    }

    /**
     * Sets the revision number.
     * @param revisionNumber the new revision number
     */
    public void setRevisionNumber(long revisionNumber) {

        this.revisionNumber = revisionNumber;
    }

	public Answers getAnswer() {
		return answer;
	}

	public void setAnswer(Answers answer) {
		this.answer = answer;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

}
