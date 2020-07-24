/*
 * SurveyValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.util.Base64Util;
import com.ese.view.validator.IValidator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetrace.eses.dao.ICertificationDAO;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.txn.agrocert.SurveyAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersSectionAnswers;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.SurveyJSONDataObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
@Component
public class SurveyValidator  implements IValidator {
@Autowired
	private ICertificationDAO certificationDAO;

	/**
	 * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
	 */
	@Override
	public Map<String, String> validate(Object object) {

		Map<String, String> errorCodes = new LinkedHashMap<String, String>();
		Map<String, String> errorCodesSubForm = new LinkedHashMap<String, String>();
		Integer numericAnswerVal = null;
		if (!ObjectUtil.isEmpty(object)) {
			SurveyFarmerCropProdAnswers farmerCropProdAnswers = (SurveyFarmerCropProdAnswers) object;
			if (!ObjectUtil.isEmpty(farmerCropProdAnswers)
					&& !ObjectUtil.isListEmpty(farmerCropProdAnswers.getFarmersSectionAnswersList())) {
				boolean partialSave = false;
				if (!ObjectUtil.isEmpty(farmerCropProdAnswers.getSaveStatus())
						&& farmerCropProdAnswers.getSaveStatus() == 1) {
					partialSave = true;
				}
				List<Object> objects = new ArrayList<Object>();
				List<Object> idkQusCodes = new ArrayList<Object>();
				List<Object> naCodes = new ArrayList<Object>();
				List<String> verifiedQusCodes = new ArrayList<String>();
				Map<String, Object> multiSelectCodesmapObj = new HashMap<String, Object>();
				Map<String, Object> ckechMapObj = new HashMap<String, Object>();
				for (SurveyFarmersSectionAnswers farmersSectionAnswers : farmerCropProdAnswers
						.getFarmersSectionAnswersList()) {
					if (!ObjectUtil.isEmpty(farmersSectionAnswers)
							&& !ObjectUtil.isListEmpty(farmersSectionAnswers.getFarmersQuestionAnswersList())) {
						Map<String, SurveyQuestion> questionMap = getQuestionMap(farmersSectionAnswers.getSectionCode());
						for (SurveyFarmersQuestionAnswers farmersQuestionAnswers : farmersSectionAnswers
								.getFarmersQuestionAnswersList()) {
							if (!ObjectUtil.isEmpty(farmersQuestionAnswers)
									&& !ObjectUtil.isListEmpty(farmersQuestionAnswers.getAnsList())) {
								if (farmersQuestionAnswers.getImageByteArray() != null) {
									farmersQuestionAnswers.setImage(
											FileUtil.getBinaryFileContent(farmersQuestionAnswers.getImageByteArray()));

								} else if (!StringUtil.isEmpty(farmersQuestionAnswers.getPhotoByteString())) {
									farmersQuestionAnswers
											.setImage(Base64.decode(farmersQuestionAnswers.getPhotoByteString()));
								}
								if (farmersQuestionAnswers.getImage() != null) {
									farmersQuestionAnswers.setImageByteArray(
											FileUtil.getConvertedPicture(farmersQuestionAnswers.getImage(),
													farmersQuestionAnswers.getQuestionCode()));
									farmersQuestionAnswers
											.setPhotoByteString(Base64Util.encoder(farmersQuestionAnswers.getImage()));
								}
								SurveyQuestion question = questionMap.get(farmersQuestionAnswers.getQuestionCode().trim());
								if (Integer.valueOf(question.getQuestionType()) == SurveyQuestion.questionTypes.SUB_FORM
										.ordinal()) {

									boolean proceed = true;
									
									for (SurveyAnswers answers : farmersQuestionAnswers.getAnsList()) {

										// Processing Main Answer Or Main Answer
										// Code
										if ((answers.getAnswer() != null
												&& (answers.getAnswer().equalsIgnoreCase("CT136")))
												|| (answers.getAnswer() == null && answers.isNonConfirm())) {

											proceed = false;
										}
									}
									if (proceed) {
										if (farmerCropProdAnswers.getSubFormJsonData() != null
												&& !StringUtil.isEmpty(farmerCropProdAnswers.getSubFormJsonData())) {

											JsonParser parser = new JsonParser();
											JsonObject json = parser.parse(farmerCropProdAnswers.getSubFormJsonData())
													.getAsJsonObject();
											if (question.getSubFormQuestions() != null
													&& !question.getSubFormQuestions().isEmpty()
													&& json.get(String.valueOf(question.getId())) != null) {

												JsonObject trs = json.get(String.valueOf(question.getId()))
														.getAsJsonObject();
												for (Entry<String, JsonElement> rows : trs.entrySet()) {

													String[] rowValues;
													String rowsStr = rows.getValue().toString().substring(1,
															rows.getValue().toString().length() - 1);
													SurveyQuestion childQuestion = certificationDAO
															.findQuestionById(Long.valueOf(rows.getKey()));
													if (rowsStr.contains(":")) {
														rowValues = rowsStr.split(":");
													} else {
														rowValues = new String[] { rowsStr };
													}

													for (String ans : rowValues) {
														String anser = ans.split("-", -1)[1];
														SurveyAnswers answer = new SurveyAnswers();
														String[] answers;
														if (anser.contains("~")) {
															answers = anser.split("~", -1);
														} else {
															answers = new String[] { anser };
														}
														answer.setAnswer(answers[0]);
														if (1 < answers.length) {
															answer.setAnswer1(answers[1]);

														}

														if (answer.getAnswer() == null
																|| answer.getAnswer().isEmpty()) {
															if (Integer.valueOf(childQuestion
																	.getComponentType()) == SurveyQuestion.componentTypeMaster.GPS_TEXT_BOX
																			.ordinal()) {
																if ((answer.getAnswer() == null
																		|| answer.getAnswer().isEmpty())
																		&& (answer.getAnswer1() == null
																				|| answer.getAnswer1().isEmpty())
																		&& !partialSave) {
																	errorCodes.put(question.getCode().trim(),
																			"answer.all.sub");
																} else {
																	errorCodesSubForm = validateAnswer(answer,
																			ans.split("-", -1)[0], childQuestion,
																			farmersQuestionAnswers.getQuestionCode(),
																			errorCodesSubForm, partialSave);
																	errorCodes.putAll(errorCodesSubForm);
																}
															} else {
																if (!partialSave) {
																	errorCodes.put(question.getCode().trim(),
																			"answer.all.sub");
																}
															}
														} else {
															if (childQuestion != null) {
																errorCodesSubForm = validateAnswer(answer,
																		ans.split("-", -1)[0], childQuestion,
																		farmersQuestionAnswers.getQuestionCode(),
																		errorCodesSubForm, partialSave);
																errorCodes.putAll(errorCodesSubForm);
															}

														}

													}
												}
											}

										}
									} else {
										farmersQuestionAnswers.setAnswers(
												new TreeSet<SurveyAnswers>(farmersQuestionAnswers.getAnsList()));
									}

								} else {
									if( farmersQuestionAnswers.getAnsList()!=null && !ObjectUtil.isListEmpty( farmersQuestionAnswers.getAnsList())){
									for (SurveyAnswers answers : farmersQuestionAnswers.getAnsList()) {
										if (!StringUtil.isEmpty(answers.getAnswer())
												|| !StringUtil.isEmpty(answers.getAnswer1())) {

											// Question question =
											// questionMap.get(farmersQuestionAnswers
											// .getQuestionCode().trim());
											if (question.getMandatory() == 0) {
												if (StringUtil.isEmpty(answers.getAnswer())) {
													errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
															"mandatory.question");
												}
											}
											if (!ObjectUtil.isEmpty(question) && question.getValidationType() > 0) {
												if (answers.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW)
														|| answers.isNonConfirm()) {

												} else if (SurveyQuestion.validationTypeMaster.NUMBER.ordinal() == question
														.getValidationType()) {
													if (!answers.getAnswer().trim().matches(NUMERIC_PATTERN))
														errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
																"valid.numeric");
													if (!StringUtil.isEmpty(answers.getAnswer())) {
														try {
															numericAnswerVal = Integer
																	.parseInt(answers.getAnswer().trim());
															if (!ObjectUtil.isEmpty(numericAnswerVal)) {
																if (numericAnswerVal < question.getMinRange()) {
																	errorCodes.put(farmersQuestionAnswers
																			.getQuestionCode().trim(),
																			"valid.minRange");
																} else if (numericAnswerVal > question.getMaxRange()) {
																	errorCodes.put(farmersQuestionAnswers
																			.getQuestionCode().trim(),
																			"valid.maxRange");
																}
															}
														} catch (Exception e) {
															System.err.println("ERROR:-" + e.getMessage());
														}
													}
												} else if ((SurveyQuestion.validationTypeMaster.DOUBLE.ordinal() == question
														.getValidationType()
														|| SurveyQuestion.validationTypeMaster.PERCENTAGE
																.ordinal() == question.getValidationType()
														|| SurveyQuestion.validationTypeMaster.HOURS.ordinal() == question
																.getValidationType())
														&& (!StringUtil.isEmpty(answers.getAnswer())
																|| !StringUtil.isEmpty(answers.getAnswer1()))) {
													try {
														Double value = Double.valueOf(
																answers.getAnswer() + "." + answers.getAnswer1());
														if (value < 0) {
															errorCodes.put(
																	farmersQuestionAnswers.getQuestionCode().trim(),
																	"positive.double");
														} else if (value > 100
																&& SurveyQuestion.validationTypeMaster.PERCENTAGE
																		.ordinal() == question.getValidationType()) {
															errorCodes.put(
																	farmersQuestionAnswers.getQuestionCode().trim(),
																	"invalid.percentage");
														} else if (SurveyQuestion.validationTypeMaster.HOURS
																.ordinal() == question.getValidationType()
																&& !StringUtil.isEmpty(answers.getAnswer1())) {
															Double minutes = Double.valueOf(answers.getAnswer1());
															if (minutes > 59) {
																errorCodes.put(
																		farmersQuestionAnswers.getQuestionCode().trim(),
																		"invalid.minutes");
															}
														}
													} catch (Exception e) {
														errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
																"valid.double");
													}
												} else if (SurveyQuestion.validationTypeMaster.DATE.ordinal() == question
														.getValidationType()) {
													try {
														Date date = DateUtil.convertStringToDate(
																answers.getAnswer().trim(), DateUtil.DATE_FORMAT);
														/*
														 * if (!DateUtil.
														 * isValidRange(date)) {
														 * errorCodes.put(
														 * farmersQuestionAnswers
														 * .getQuestionCode().
														 * trim(),
														 * "invalid.dateRange");
														 * }
														 */
													} catch (Exception e) {
														errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
																"invalid.dateFormat");
													}

												} else if (SurveyQuestion.validationTypeMaster.GPS_POINTS
														.ordinal() == question.getValidationType()
														&& (!StringUtil.isEmpty(answers.getAnswer())
																|| !StringUtil.isEmpty(answers.getAnswer1()))) {
													if (StringUtil.isEmpty(answers.getAnswer())) {
														if (!partialSave)
															errorCodes
																	.put(farmersQuestionAnswers.getQuestionCode().trim()
																			+ "_lat", "empty.latitude");
													} else {
														try {
															Double.valueOf(answers.getAnswer().trim());
														} catch (Exception e) {
															errorCodes
																	.put(farmersQuestionAnswers.getQuestionCode().trim()
																			+ "_lat", "invalid.latitude");
														}
													}
													if (StringUtil.isEmpty(answers.getAnswer1())) {
														if (!partialSave)
															errorCodes
																	.put(farmersQuestionAnswers.getQuestionCode().trim()
																			+ "_lon", "empty.longitude");
													} else {
														try {
															Double.valueOf(answers.getAnswer1().trim());
														} catch (Exception e) {
															errorCodes
																	.put(farmersQuestionAnswers.getQuestionCode().trim()
																			+ "_lon", "invalid.longitude");
														}
													}
												} else if (SurveyQuestion.validationTypeMaster.MOBILE_NO.ordinal() == question
														.getValidationType()) {
													if (!answers.getAnswer().trim().matches(NUMERIC_PATTERN)
															|| answers.getAnswer().length() > 10) {
														errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
																"valid.mobileNo");
													}
												}
											}
										}/*else{
										if (question.getMandatory() == 0) {
											
												errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
														"mandatory.question");
											
										}
									}*/
									}
									}/*else{
										if (question.getMandatory() == 0) {
											
												errorCodes.put(farmersQuestionAnswers.getQuestionCode().trim(),
														"mandatory.question");
											
										}
									}*/
								}
								if (ObjectUtil.isEmpty(farmersQuestionAnswers.getQuestion())) {
									// Question question =
									// questionMap.get(farmersQuestionAnswers
									// .getQuestionCode().trim());
									if (ObjectUtil.isEmpty(question)) {
										question = certificationDAO
												.findSurveyQuestionByCode(farmersQuestionAnswers.getQuestionCode());
									}
									farmersQuestionAnswers.setQuestion(question);
								}
								if (!ObjectUtil.isListEmpty(farmersQuestionAnswers.getAnsList())
										&& (!StringUtil
												.isEmpty(farmersQuestionAnswers.getAnsList().get(0).getAnswer())
												|| farmersQuestionAnswers.getAnsList().get(0).isNonConfirm())
										&& !StringUtil.isEmpty(farmersQuestionAnswers.getQuestionCode())) {
									if (farmersQuestionAnswers.getAnsList().get(0).isNonConfirm()) {
										naCodes.add(String.valueOf(farmersQuestionAnswers.getQuestion().getId()));
									} else if (farmersQuestionAnswers.getAnsList().get(0).getAnswer()
											.equals(SurveyQuestion.CAT_IDONTKNOW)) {
										idkQusCodes.add(String.valueOf(farmersQuestionAnswers.getQuestion().getId()));
									}

								}

								
								// Processing Mulit Select Drop Down Or Check
								// Box
								if ((!StringUtil.isEmpty(farmersQuestionAnswers.getQuestion().getListMethodName())
										||  !StringUtil.isEmpty(question.getAnswerCatType()))
										&& (farmersQuestionAnswers
												.getComponentType() == SurveyQuestion.componentTypeMaster.MULTI_SELECT_DROP_DOWN
														.ordinal()
												|| farmersQuestionAnswers
														.getComponentType() == SurveyQuestion.componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS
																.ordinal()
												)) {
									if (!ObjectUtil.isListEmpty(farmersQuestionAnswers.getAnsList())) {
										String ans = "";
										for (SurveyAnswers answers2 : farmersQuestionAnswers.getAnsList()) {
											ans += StringUtil.removeWhiteSpace(answers2.getAnswer()) + ",";
										}
										multiSelectCodesmapObj.put(farmersQuestionAnswers.getQuestionCode(),
												StringUtil.removeLastComma(ans));
									}

								}
								
								if ((!StringUtil.isEmpty(farmersQuestionAnswers.getQuestion().getListMethodName())
										||  !StringUtil.isEmpty(question.getAnswerCatType()))
										&& (farmersQuestionAnswers
														.getComponentType() == SurveyQuestion.componentTypeMaster.CHECK_BOX
																.ordinal()
												|| farmersQuestionAnswers
														.getComponentType() == SurveyQuestion.componentTypeMaster.CHECK_BOX_OTHERS
																.ordinal())) {
									if (!ObjectUtil.isListEmpty(farmersQuestionAnswers.getAnsList())) {
										String ans = "";
										for (SurveyAnswers answers2 : farmersQuestionAnswers.getAnsList()) {
											ans += StringUtil.removeWhiteSpace(answers2.getAnswer()) + ",";
										}
										ckechMapObj.put(farmersQuestionAnswers.getQuestionCode(),
												StringUtil.removeLastComma(ans));
									}

								}

							}

						}
					}
				}
				SurveyJSONDataObject dataObject = new SurveyJSONDataObject();
				dataObject.setMapObj(multiSelectCodesmapObj);
				dataObject.setCheckBoxlist(ckechMapObj);
				dataObject.setList(verifiedQusCodes);
				dataObject.setErrorCodesSubForm(errorCodesSubForm);
				objects.add(idkQusCodes);
				objects.add(naCodes);
				dataObject.setObjects(objects);
				farmerCropProdAnswers.setJsonData(new Gson().toJson(dataObject));
			}

		}

		return errorCodes;
	}

	/**
	 * Gets the question map.
	 * 
	 * @param sectionCode
	 *            the section code
	 * @return the question map
	 */
	private Map<String, SurveyQuestion> getQuestionMap(String sectionCode) {

		Map<String, SurveyQuestion> questionMap = new HashMap<String, SurveyQuestion>();
		List<SurveyQuestion> questionList = certificationDAO.listQuestionsBySectionCode(sectionCode);
		if (!ObjectUtil.isListEmpty(questionList)) {
			for (SurveyQuestion question : questionList)
				questionMap.put(question.getCode(), question);
		}
		return questionMap;
	}

	/**
	 * Sets the certification dao.
	 * 
	 * @param certificationDAO
	 *            the new certification dao
	 */
	public void setCertificationDAO(ICertificationDAO certificationDAO) {

		this.certificationDAO = certificationDAO;
	}

	/**
	 * Gets the certification dao.
	 * 
	 * @return the certification dao
	 */
	public ICertificationDAO getCertificationDAO() {

		return certificationDAO;
	}

	public Map<String, String> validateAnswer(SurveyAnswers answers, String order, SurveyQuestion childQuestion, String questionCode,
			Map<String, String> errorCodes, boolean partialSave) {
		Integer numericAnswerVal = null;
		if (SurveyQuestion.validationTypeMaster.NUMBER.ordinal() == childQuestion.getValidationType()) {
			if (!answers.getAnswer().trim().matches(NUMERIC_PATTERN))
				errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order, "valid.numeric");
			if (!StringUtil.isEmpty(answers.getAnswer())) {
				try {
					numericAnswerVal = Integer.parseInt(answers.getAnswer().trim());
					if (!ObjectUtil.isEmpty(numericAnswerVal)) {
						if (numericAnswerVal < childQuestion.getMinRange()) {
							errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
									"valid.minRange");
						} else if (numericAnswerVal > childQuestion.getMaxRange()) {
							errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
									"valid.maxRange");
						}
					}
				} catch (Exception e) {
					System.err.println("ERROR:-" + e.getMessage());
				}
			}
		} else if ((SurveyQuestion.validationTypeMaster.DOUBLE.ordinal() == childQuestion.getValidationType()
				|| SurveyQuestion.validationTypeMaster.PERCENTAGE.ordinal() == childQuestion.getValidationType()
				|| SurveyQuestion.validationTypeMaster.HOURS.ordinal() == childQuestion.getValidationType())
				&& (!StringUtil.isEmpty(answers.getAnswer()) || !StringUtil.isEmpty(answers.getAnswer1()))) {
			try {
				Double value = Double.valueOf(answers.getAnswer());
				if (value < 0) {
					errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
							"positive.double");
				} else if (value > 100
						&& SurveyQuestion.validationTypeMaster.PERCENTAGE.ordinal() == childQuestion.getValidationType()) {
					errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
							"invalid.percentage");
				} else if (SurveyQuestion.validationTypeMaster.HOURS.ordinal() == childQuestion.getValidationType()
						&& !StringUtil.isEmpty(answers.getAnswer1())) {
					Double minutes = Double.valueOf(answers.getAnswer1());
					if (minutes > 59) {
						errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
								"invalid.minutes");
					}
				}
			} catch (Exception e) {
				errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
						"valid.double");
			}
		} else if (SurveyQuestion.validationTypeMaster.DATE.ordinal() == childQuestion.getValidationType()) {
			try {
				Date date = DateUtil.convertStringToDate(answers.getAnswer().trim(), DateUtil.DATE_FORMAT);
				/*
				 * if (!DateUtil.isValidRange(date)) {
				 * errorCodes.put(questionCode.trim()+"_"+question.getCode()+"_"
				 * +order.trim(), getText("invalid.dateRange")); }
				 */
			} catch (Exception e) {
				errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
						"invalid.dateFormat");
			}

		} else if (SurveyQuestion.validationTypeMaster.GPS_POINTS.ordinal() == childQuestion.getValidationType()
				&& (!StringUtil.isEmpty(answers.getAnswer()) || !StringUtil.isEmpty(answers.getAnswer1()))) {
			String error = "";
			if (StringUtil.isEmpty(answers.getAnswer())) {
				if (!partialSave)
					error = error + "," + "empty.latitude";

			} else {
				try {
					Double.valueOf(answers.getAnswer().trim());
				} catch (Exception e) {
					error = error + "," +"invalid.latitude";

				}
			}
			if (StringUtil.isEmpty(answers.getAnswer1())) {
				if (!partialSave)
					error = error + "," +"empty.longitude";

			} else {
				try {
					Double.valueOf(answers.getAnswer1().trim());
				} catch (Exception e) {
					error = error + "," + "invalid.longitude";

				}
			}
			if (!StringUtil.isEmpty(error)) {
				error = error.substring(1);
				errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(), error);
			}
		} else if (SurveyQuestion.validationTypeMaster.MOBILE_NO.ordinal() == childQuestion.getValidationType()) {
			if (!answers.getAnswer().trim().matches(NUMERIC_PATTERN) || answers.getAnswer().length() > 10) {
				errorCodes.put(questionCode.trim() + "_" + childQuestion.getCode() + "_" + order.trim(),
						"valid.mobileNo");
			}
		}
		return errorCodes;
	}

}
