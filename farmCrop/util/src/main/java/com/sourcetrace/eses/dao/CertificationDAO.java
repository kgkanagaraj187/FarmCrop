/*
 * CertificationDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.TextType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.Language;
import com.sourcetrace.eses.entity.FarmCatalogue;
//import com.sourcetrace.eses.inspect.agrocert.AgentSurveyMapping;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.inspect.agrocert.Section;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyPreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.inspect.agrocert.SurveySection;
import com.sourcetrace.eses.inspect.agrocert.SurveyType;
import com.sourcetrace.eses.order.entity.profile.Symptoms;
import com.sourcetrace.eses.txn.agrocert.Answers;
import com.sourcetrace.eses.txn.agrocert.DownloadTask;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersSectionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersSectionAnswers;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.Farmer;

@Repository
@Transactional
public class CertificationDAO extends ESEDAO implements ICertificationDAO {

	@Autowired
	public CertificationDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}
    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * findCertificateCategoryByCode(java.lang .String)
     */
    public CertificateCategory findCertificateCategoryByCode(String categoryCode) {

        CertificateCategory certificateCategory = (CertificateCategory) find(
                "FROM CertificateCategory cc WHERE cc.code = ? ", categoryCode);
        return certificateCategory;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listSectionByCategoryCode
     * (java.lang.String )
     */
    public List<Section> listSectionByCategoryCode(String categoryCode) {

        List<Section> sectionsList = list("FROM Section s WHERE s.certificateCategory.code = ?ORDER BY s.serialNo ASC",
                categoryCode);
        return sectionsList;
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * findFarmerCropProdAnswersById(java.lang.Long)
     */
    public FarmerCropProdAnswers findFarmerCropProdAnswersById(Long id) {

        FarmerCropProdAnswers farmerCropProdAnswers = (FarmerCropProdAnswers) find(
                "from FarmerCropProdAnswers fcpa INNER JOIN FETCH fcpa.farmersSectionAnswers fsa INNER JOIN FETCH  fsa.farmersQuestionAnswers fqa  where fcpa.id = ?",
                id);
        return farmerCropProdAnswers;
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * findFarmerQuestionAswersById(java.lang.Long)
     */
    public FarmersQuestionAnswers findFarmerQuestionAswersById(Long id) {

        FarmersQuestionAnswers farmersQuestionAnswers = (FarmersQuestionAnswers) find(
                "FROM FarmersQuestionAnswers fqa WHERE fqa.id = ?", id);

        return farmersQuestionAnswers;
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * listOfInspectionReportFarmers(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> listOfInspectionReportFarmers(String categoryCode) {

        List<String> farmersList = list(
                "SELECT DISTINCT fcp.farmerId FROM FarmerCropProdAnswers fcp WHERE fcp.categoryCode=? ORDER BY fcp.farmerId",
                categoryCode);
        return farmersList;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listCertificateCategory ()
     */
    public List<CertificateCategory> listCertificateCategory() {

        List<CertificateCategory> certificateCategoryList = list("FROM CertificateCategory c");
        return certificateCategoryList;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listOfFarmers()
     */
    public List<String> listOfFarmers() {

        List<String> farmersList = list("SELECT DISTINCT fcp.farmerId FROM FarmerCropProdAnswers fcp");

        return farmersList;
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * loadFarmerFailedQuestionProcedure(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void loadFarmerFailedQuestionProcedure(String categoryCode, String answerType,
            String answer, String userName, String farmerId, String startDate, String endDate) {

        Session session = getHibernateTemplate().getSessionFactory().openSession();

        if (!StringUtil.isEmpty(farmerId)) {
            farmerId = "'" + farmerId + "'";
        } else {
            farmerId = null;
        }

        if (!StringUtil.isEmpty(startDate)) {
            startDate = "'" + startDate + "'";
        } else {
            startDate = null;
        }

        if (!StringUtil.isEmpty(endDate)) {
            endDate = "'" + endDate + "'";
        } else {
            endDate = null;
        }

        String queryString = "CALL PROC_SAVE_FARMER_FAILED_QUESTIONS (" + "'" + categoryCode
                + "','" + answerType + "','" + answer + "','" + userName + "'," + farmerId + ","
                + startDate + "," + endDate + ")";

        Query query = session.createSQLQuery(queryString);
        query.executeUpdate();
        session.flush();
        session.close();

    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * callFailedQuestionProcedure(java.lang.String, java.lang.String, java.lang.Stringcmd
     * 
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public void callFailedQuestionProcedure(String catgCode, String answerType, String answer,
            String startDate, String endDate, String userName) {

        Session sessions = getSessionFactory().openSession();
        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            String queryStrings = "CALL PROC_SAVE_FAILED_QUESTIONS('" + catgCode + "',"
                    + answerType + "," + answer + ",'" + startDate + "','" + endDate + "','"
                    + userName + "')";
            Query querys = sessions.createSQLQuery(queryStrings);
            querys.executeUpdate();
        } else {
            String queryStrings = "CALL PROC_SAVE_FAILED_QUESTIONS('" + catgCode + "',"
                    + answerType + "," + answer + ",'" + startDate + "','" + endDate + "','"
                    + userName + "')";
            Query querys = sessions.createSQLQuery(queryStrings);
            querys.executeUpdate();
        }

        sessions.flush();
        sessions.close();

    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * listFailedQuestionsByFarmerChartData(java.lang.String)
     */
    public List<Object[]> listFailedQuestionsByFarmerChartData(String query) {

        Session session = getSessionFactory().openSession();
        Query sqlQuery = session.createSQLQuery(query);
        List<Object[]> list = sqlQuery.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * listFailedQuestionsChartData(java.lang.String)
     */
    public List<Object[]> listFailedQuestionsChartData(String query) {

        Session session = getSessionFactory().openSession();
        Query sqlQuery = session.createSQLQuery(query);
        List<Object[]> list = sqlQuery.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listFarmerInspectionChart
     * (java.lang.String, java.lang.String)
     */
    public List<Object[]> listFarmerInspectionChart(String startDate, String endDate) {

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        String queryString = "SELECT FCPA.CATEGORY_NAME,COUNT(*) FROM FARMER_CROP_PROD_ANSWERS FCPA  WHERE FCPA.ANSWERED_DATE BETWEEN "
                + "'" + startDate + "'" + " AND " + "'" + endDate + "'GROUP BY FCPA.CATEGORY_CODE;";
        Query query = session.createSQLQuery(queryString);
        List<Object[]> list = query.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listOfInspectionFarmers
     * (java.lang.String)
     */
    public List<Farmer> listOfInspectionFarmers(String selectedCategory) {

        List<Farmer> farmersList = list(
                "FROM Farmer f WHERE f.farmerId IN (SELECT fcp.farmerId FROM FarmerCropProdAnswers fcp WHERE fcp.categoryCode=? GROUP BY fcp.farmerId) ORDER BY f.farmerId",
                selectedCategory);
        return farmersList;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#findCertificateStandardById(long)
     */
    public CertificateStandard findCertificateStandardById(long id) {

        return (CertificateStandard) find("FROM CertificateStandard cs WHERE cs.id =?", id);
    }

    /*
     * (non-Javadoc)
     * @seecom.sourcetrace.eses.agrocert.dao.ICertificationDAO#
     * listCertificateStandardByCertificateCategoryId(long)
     */
    public List<CertificateStandard> listCertificateStandardByCertificateCategoryId(long id) {

        return (List<CertificateStandard>) list(
                "FROM CertificateStandard cs WHERE cs.category.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#findCertificateCategoryById(long)
     */
    public CertificateCategory findCertificateCategoryById(long id) {

        return (CertificateCategory) find("FROM CertificateCategory cc WHERE cc.id = ? ", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listCertificateStandard()
     */
    public List<CertificateStandard> listCertificateStandard() {

        return (List<CertificateStandard>) list("FROM CertificateStandard cs");
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listCertificationCategoryStateChartData
     * (int)
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> listCertificationCategoryStateChartData(int limitValue) {

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        String queryString = "SELECT  t1.state_name,t2.* from (SELECT s1.ID,s1.name state_name,count(*) total_farmer_count FROM FARMER f1 INNER JOIN village v1 on f1.VILLAGE_ID=v1.ID INNER JOIN city c1 on v1.CITY_ID=c1.ID INNER JOIN location l1 on c1.LOCATION_ID=l1.ID INNER JOIN state s1 on l1.STATE_ID=s1.ID  group by s1.id order by count(*) desc limit :limitValue) t1 "
                + " INNER JOIN "
                + "(SELECT count(*) farmer_count,cc.name categoty,s.ID state  from farmer f LEFT JOIN certificate_standard cs on f.CERTIFICATE_STANDARD_ID=cs.ID  LEFT JOIN certificate_category cc on cs.CERTIFICATE_CATEGORY_ID=cc.id INNER JOIN village v on f.VILLAGE_ID=v.ID INNER JOIN city c on v.CITY_ID=c.ID INNER JOIN location l on c.LOCATION_ID=l.ID INNER JOIN state s on l.STATE_ID=s.ID GROUP BY s.ID,cc.id ) t2  on t1.ID=t2.state ORDER BY t1.total_farmer_count DESC,t2.categoty ";
        Query query = session.createSQLQuery(queryString).setParameter("limitValue", limitValue);
        List<Object[]> list = query.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listCertificationStandardStateChartData
     * (java.lang.String, int)
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> listCertificationStandardStateChartData(String certificationCategoryCode,
            int limitValue) {

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        String queryString = "SELECT t1.state_name,t2.* from ( SELECT s1.ID,s1.name state_name,count(*) total_farmer_count FROM FARMER f1 INNER JOIN certificate_standard cs1 on f1.CERTIFICATE_STANDARD_ID=cs1.ID INNER JOIN certificate_category cc1 on cs1.CERTIFICATE_CATEGORY_ID=cc1.id  INNER JOIN village v1 on f1.VILLAGE_ID=v1.ID INNER JOIN city c1 on v1.CITY_ID=c1.ID INNER JOIN location l1 on c1.LOCATION_ID=l1.ID INNER JOIN state s1 on l1.STATE_ID=s1.ID  WHERE cc1.`CODE`=:certificationCategoryCode group by s1.id order by count(*) desc limit :limitValue) t1 "
                + "INNER JOIN "
                + "(SELECT count(*) farmer_count,cs.name cert_std,s.ID state FROM FARMER f INNER JOIN certificate_standard cs on f.CERTIFICATE_STANDARD_ID=cs.ID INNER JOIN certificate_category cc on cs.CERTIFICATE_CATEGORY_ID=cc.id  INNER JOIN village v on f.VILLAGE_ID=v.ID INNER JOIN city c on v.CITY_ID=c.ID  INNER JOIN location l on c.LOCATION_ID=l.ID INNER JOIN state s on l.STATE_ID=s.ID  WHERE cc.`CODE`=:certificationCategoryCode GROUP BY s.ID,cs.ID) t2 on t1.ID=t2.state ORDER BY t1.total_farmer_count DESC,t2.cert_std ";
        Query query = session.createSQLQuery(queryString).setParameter("limitValue", limitValue)
                .setParameter("certificationCategoryCode", certificationCategoryCode);
        List<Object[]> list = query.list();
        session.flush();
        session.close();
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listCertificateCategoryNames()
     */
    @SuppressWarnings("unchecked")
    public List<String> listCertificateCategoryNames() {

        return list("SELECT cc.name FROM CertificateCategory cc ");
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listCertificationStandardNames(java.lang
     * .String)
     */
    @SuppressWarnings("unchecked")
    public List<String> listCertificationStandardNames(String certificationCategoryCode) {

        return list("SELECT cs.name FROM CertificateStandard cs WHERE cs.category.code=?",
                certificationCategoryCode);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listFarmersByCertificationCategory(long)
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> listFarmersByCertificationCategory(long certificateId) {
        Session session = getSessionFactory().openSession();
		Query query = session.createQuery("SELECT count(*),fr.village.gramPanchayat.city.locality.state.name FROM Farmer fr WHERE fr.certificateStandard.category.id = :certificateId GROUP BY fr.village.gramPanchayat.city.locality.state.id ORDER BY count(*) DESC ");
		query.setParameter("certificateId", certificateId);
		query.setMaxResults(5);
		List<Object[]> farmerList = query.list();
		session.flush();
		session.close();
		return farmerList;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.agrocert.dao.ICertificationDAO#listTraningTimeLineData()
     */
    public List<Object[]> listTraningTimeLineData() {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT (SELECT TOPIC.CRITERIA_CODE FROM TOPIC WHERE TOPIC.ID = tps.TOPIC_ID), SUM(TRAINING_STATUS.FARMER_ATTENED)"
                + " FROM TOPIC_TRAINING_STATUS tps INNER JOIN TRAINING_STATUS ON"
                + " TRAINING_STATUS.ID = tps.TRAINING_STATUS_ID GROUP BY tps.TOPIC_ID";
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        return list;
    }

    public CertificateStandard findCertificateStandardByCode(String code) {

        CertificateStandard certificateStandard = (CertificateStandard) find(
                "FROM CertificateStandard cs WHERE cs.code = ? ", code);
        return certificateStandard;
    }

    public List<Symptoms> findSymptomsCodeByType(Integer type) {

        List<Symptoms> symptomsList = list("FROM Symptoms s WHERE s.typez=?",type);
        return symptomsList;
    }

    public byte[] findInspectionImageById(Long id) {

        return (byte[]) find("SELECT iI.photo FROM InspectionImage iI WHERE iI.id=?", id);
    }
	@Override
	public FarmerCropProdAnswers  findFarmerCropProdAnswersByFarmerId(String farmerId,String category) {
		// TODO Auto-generated method stub
		 List list =  list("FROM FarmerCropProdAnswers where farmerId = ? and categoryCode=?  order by id desc", new Object[]{farmerId,category});
		 return list.isEmpty() ? new FarmerCropProdAnswers() : (FarmerCropProdAnswers) list.get(0);
	}
	@Override
	public List<Answers> listAnswersByQuestionAnswers(long id) {
		
		return list("FROM Answers ans where ans.farmersQuestionAnswers.id=?",id);
	}
	@Override
	public List<Question> listQuestionByCategoryCode(String code) {
		 
	     
	    Session session = getSessionFactory().openSession();  
	    String queryString="select s.id from Section s where s.certificateCategory.code=:catCode";
		Query query1 = session.createQuery(queryString).setParameter("catCode",code);
		Query query2 = session.createQuery("FROM Question que where que.section.id in(:ids)").setParameterList("ids",query1.list());
		List<Question> allQuestions= query2.list();
		session.flush();
        session.close();
		return allQuestions;
	}
	@Override
	public List listAnswersBySubQuestionAnswers(long id) {
		// TODO Auto-generated method stub
		return list("FROM Answers ans where ans.farmersQuestionAnswers.parentQuestionAnswers.id=?",id);
	}
	
	@Override
	public List<FarmersSectionAnswers> listsectionByFarmerCropProd(long id) {
		// TODO Auto-generated method stub
		 return list("FROM FarmersSectionAnswers s WHERE s.farmerCropProdAnswers.id=? ORDER BY s.sectionCode ASC", id);
	}
	@Override
	public Section findSectionByCode(String string) {
		Section farmerCropProdAnswers = (Section) find(
	                "from Section fcpa where fcpa.code=?",
	                string);
	        return farmerCropProdAnswers;
	}
	
	@Override
	public Question  findQuestionByCode(String string) {
		Question farmerCropProdAnswers = (Question) find(
                "from Question fcpa where fcpa.code=?",
                string);
        return farmerCropProdAnswers;
	}
	
	@Override
	public boolean isQuestionMappedWithSurvey(long id) {

		boolean isQuestionMappedWithSurvey = false;

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM SURVEY_QUESTION_MAP WHERE SURVEY_ID IS NOT NULL AND QUESTION_ID = '"
				+ id + "'";
		Query query = session.createSQLQuery(queryString);
		Long count = (Long) ((BigInteger) query.uniqueResult()).longValue();
		if (count > 0) {
			isQuestionMappedWithSurvey = true;
		}
		session.flush();
		session.close();
		return isQuestionMappedWithSurvey;

	}
	@Override
	public Country findCountryByCode(String code) {

        Country country = (Country) find(
                "FROM Country c WHERE c.code = ?", code);
        return country;
    }
	
	@Override
	public SurveyQuestion findQuestionById(long id) {
		// TODO Auto-generated method stub
		return (SurveyQuestion) find("FROM SurveyQuestion q WHERE q.id=?", id);
	}
	@Override
	public List<Country> listCountryByCode(String code) {
		// TODO Auto-generated method stub
        return list ("FROM Country c WHERE c.code = ?", code);
    
	}
	@Override
	public List<Country> listCountries() {
		// TODO Auto-generated method stub
		return list("FROM Country c ORDER BY c.name ASC");
	}
	@Override
	public List<SurveySection> listAllSectionsForFOFarmerFarmByCountryId(long Cid) {
		// TODO Auto-generated method stub
		return list("FROM SurveySection sec where sec.country.id= ?",Cid);
	}
	@Override
	public List<SurveyQuestion> listParentQuestions(long sectionId, String questionId) {
		// TODO Auto-generated method stub
		return list(
				"FROM SurveyQuestion ques WHERE ques.section.id=? AND ques.dependencyKey IS NOT NULL AND ques.id <> ?",
				new Object[] { sectionId, Long.valueOf(questionId) });
	}
	@Override
	public List<Language> listLanguages() {
		// TODO Auto-generated method stub
		return list("FROM Language l WHERE l.webStatus=1 ORDER by l.code ASC");
	}
	@Override
	public List<LanguagePreferences> listLangPrefByCode(String code) {
		// TODO Auto-generated method stub
		return list("FROM LanguagePreferences lp WHERE lp.code=?", code);
	}
	@Override
	public boolean isParentQuestion(long id) {

		boolean isParentQuestion = false;
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM SURVEY_QUESTION WHERE PARENT_QUESTION_ID = '"
				+ id + "'";
		Query query = session.createSQLQuery(queryString);
		Long count = (Long) ((BigInteger) query.uniqueResult()).longValue();
		if (count > 0) {
			isParentQuestion = true;
		}
		session.flush();
		session.close();
		return isParentQuestion;
	}
	@Override
	public void removeQuestionSurveyMapping(SurveyQuestion question) {

		Session session = getSessionFactory().openSession();
		String sss = "DELETE FROM SURVEY_QUESTION_MAP WHERE QUESTION_ID="
				+ question.getId() + ";";
		Query query = session.createSQLQuery(sss);
		query.executeUpdate();
		session.flush();
		session.close();

	}
	@Override
	public List<SurveyQuestion> listQuestionsNumericBySectionId(long sectionId) {

		// TODO Auto-generated method stub
		return list("FROM SurveyQuestion ques WHERE ques.validationType IN ('"
				+ SurveyQuestion.validationTypeMaster.NUMBER.ordinal() + "','"
				+ SurveyQuestion.validationTypeMaster.DOUBLE.ordinal() + "','"
				+ SurveyQuestion.validationTypeMaster.PERCENTAGE.ordinal()
				+ "') AND ques.section.id=? AND ques.status=?", new Object[] {
				sectionId, true });
	}
	@Override
	public List<SurveyPreferences> listSurveyPreferencesByType(int type) {
		// TODO Auto-generated method stub
		return list("FROM SurveyPreferences sp WHERE sp.type=?", type);
	}
	@Override
	public DataLevel findDataLevelBySectionId(long sectionId) {
		// TODO Auto-generated method stub
		return (DataLevel) find(
				"SELECT sec.dataLevel FROM SurveySection sec WHERE sec.id=?",
				sectionId);
	}
	
	
	@Override
	public List<Object[]> listQuestionsByQuestionTypeAndComponentTypeAndSection(long sectionId) {
		// TODO Auto-generated method stub
		return list(
				"select q.code,q.name FROM SurveyQuestion q WHERE q.questionType NOT IN ('3','2')  AND q.componentType NOT IN (4,6,8,11,13) AND q.section.id=? ORDER BY q.code ASC",
				sectionId);
	}
	@Override
	public List<SurveyQuestion> listQuestionsByCodes(List<String> codes) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM SurveyQuestion qus WHERE qus.code IN (:codes)");
		query.setParameterList("codes", codes);
		List<SurveyQuestion> questions = query.list();
		session.flush();
		session.close();
		return questions;
	}
	@Override
	public List<FarmCatalogue> listCataloguesByTypeAndCountry(String type, long cid) {
		// TODO Auto-generated method stub
		return list("FROM FarmCatalogue cat WHERE cat.catelogueType=? and cat.country.id=?",new Object[]{type,cid});
	}
	@Override
	public List<FarmCatalogue> listCataloguesByType(int type) {
		// TODO Auto-generated method stub
		return list("FROM FarmCatalogue cat WHERE cat.typez=?", type);
	}
	@Override
	public List<FarmCatalogue> listCataloguesByCodes(List<String> codes) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM FarmCatalogue cat WHERE cat.code IN (:codes)");
		query.setParameterList("codes", codes);
		List<FarmCatalogue> catalogues = query.list();
		session.flush();
		session.close();
		return catalogues;
	}
	@Override
	public FarmCatalogue findCatalogueByCode(String code) {
		// TODO Auto-generated method stub
		return (FarmCatalogue) find("From FarmCatalogue c Where c.code=?", code);
	}
	@Override
	public void updateQuestion(String code, List<String> questionCodes) {

		Session session = getSessionFactory().openSession();

		for (String subChildQuestion : questionCodes) {
			List<Object[]> qus = findQuestionBySubFormCode(subChildQuestion,
					code);
			if (ObjectUtil.isListEmpty(qus) || qus.size() == 1) {
				String queryString = "UPDATE SURVEY_QUESTION SET SUB_FORM_KEY='"
						+ code + "' WHERE CODE='" + subChildQuestion + "'";
				Query query = session.createSQLQuery(queryString);
				query.executeUpdate();
			}
			if (!ObjectUtil.isListEmpty(qus) && qus.size() > 1) {
				List<Object[]> codes = qus;
				String questCodes = codes.toString();
				questCodes = questCodes.substring(1, questCodes.length() - 1);
				// String appendParentCode=qus.getSubFormKey();
				// appendParentCode=appendParentCode.concat(",").concat(code);
				String queryString = "UPDATE SURVEY_QUESTION SET SUB_FORM_KEY='"
						+ questCodes + "' WHERE CODE='" + subChildQuestion
						+ "'";
				Query query = session.createSQLQuery(queryString);
				query.executeUpdate();
			}
		}

		session.flush();
		session.close();

	}
	
	public List<Object[]> findQuestionBySubFormCode(String subChildQuestion,
			String code) {
		// long childQuestion;
		// childQuestion=Long.parseLong(subChildQuestion);

		Session session = getSessionFactory().openSession();
		Query sqlQuery = session
				.createQuery("SELECT sfq.parentQuestion.code FROM SubFormQuestionMapping sfq WHERE sfq.childQuestion.code='"
						+ subChildQuestion + "'");
		List<Object[]> list = sqlQuery.list();
		session.flush();
		session.close();
		return list;
	}
	@Override
	public Country findCountryById(Long id) {
		// TODO Auto-generated method stub
		 Country country = (Country) find("FROM Country c WHERE c.id = ?",id);
	        return country;
	}
	@Override
	public boolean isQuestionAndSubFormMappedWithSurvey(long id) {
		boolean isQuestionMappedWithSurvey = false;

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM SURVEY_QUESTION_MAP WHERE SURVEY_ID IS NOT NULL AND QUESTION_ID = '"
				+ id
				+ "' OR QUESTION_ID IN (select smq.PARENT_QUESTION_ID from SUB_FORM_QUESTION_MAP smq WHERE smq.CHILD_QUESTION_ID = '"
				+ id + "')";
		Query query = session.createSQLQuery(queryString);
		Long count = (Long) ((BigInteger) query.uniqueResult()).longValue();
		if (count > 0) {
			isQuestionMappedWithSurvey = true;
		}
		session.flush();
		session.close();
		return isQuestionMappedWithSurvey;
	}
	@Override
	public SurveySection findSectionById(long id) {
		// TODO Auto-generated method stub
		return (SurveySection) find("From SurveySection s Where s.id=?", id);
	}
	@Override
	public List<SurveyQuestion> listParentQuestions(long sectionId) {
		// TODO Auto-generated method stub
		return list(
				"FROM SurveyQuestion ques WHERE ques.section.id=? AND ques.dependencyKey IS NOT NULL",
				sectionId);
	}
	@Override
	public List<SurveySection> listSections() {
		// TODO Auto-generated method stub
		return list("FROM SurveySection sec order by sec.name ");
	}
	
	public SurveyPreferences findSurveyPreference(String fromUnit,
			String toUnit, int type, Country country) {

		Object[] args = { fromUnit, toUnit, type,
				!ObjectUtil.isEmpty(country) ? country.getId() : 0 };
		return (SurveyPreferences) find(
				"From SurveyPreferences sp Where sp.code=? and (sp.convertionCode=null  or sp.convertionCode=?) and sp.type=? and (sp.country=null or sp.country.id=? )",
				args);
	}
	@Override
	public SurveySection findSectionByName(String selectedSection) {
		// TODO Auto-generated method stub
		return (SurveySection) find("FROM SurveySection ss WHERE ss.name = ?", selectedSection);
	}
	@Override
	public List<Language> findIsSurveyStatusLanguage() {
		// TODO Auto-generated method stub
		return list("FROM Language l where l.surveyStatus=1 ORDER BY l.code ASC");
		
	}
	public long findSurveyTypeBySurveyCode(String surveyCode) {

		// TODO Auto-generated method stub
		return (Long) find(
				"select sm.surveyType.id from SurveyMaster sm where sm.code=?",
				surveyCode);
	}
	@Override
	public List<Object[]> listDataLevel() {
		// TODO Auto-generated method stub
		return list("Select d.code,d.name FROM DataLevel d ");
	}
	@Override
	public List<LanguagePreferences> listLanguagePreferencesByLanguageAndType(String lang, int type) {
		return list(
				"From LanguagePreferences lp WHERE lp.lang=? AND lp.type=?",
				new Object[] { lang, type });
	}
	@Override
	public List<Object[]> listSectionsByDataLevelCode(String code) {
		return list(
				"Select s.code,s.name From Section s Where s.dataLevel.code=?",
				code);
	}
	@Override
	public SurveyMaster findSurveyMasterBySurveyCode(String code) {

		return (SurveyMaster) find(
				"FROM SurveyMaster sm  INNER JOIN FETCH sm.surveyQuestionMapping WHERE sm.code=?",
				code);
	}
	@Override
	public Object findEntityByEntityNameAndProperty(String entityName,
			String propertyName, String filterValue) {

		String query = "From " + entityName + " ent Where ent." + propertyName
				+ "=?";
		return find(query, filterValue);
	}
	@Override
	public SurveyFarmerCropProdAnswers findSurveyFarmerCropProdAnswersById(Long id) {
		SurveyFarmerCropProdAnswers farmerCropProdAnswers = (SurveyFarmerCropProdAnswers) find(
				"from SurveyFarmerCropProdAnswers fcpa left join fetch fcpa.farmersSectionAnswers fsa left join fetch fsa.farmersQuestionAnswers fqa left join fetch fqa.answers a where fcpa.id = ?",
				id);
		return farmerCropProdAnswers;
	}
	@Override
	public List<Object[]> listSubFormQuestionsAnswerCatalogueBySurveyCode(String surveyCode) {
		return list(
				"Select c.code,c.name,c.typez From SurveyMaster sm inner join sm.surveyQuestionMapping sqm inner join sqm.question q inner join q.subFormQuestions sfm inner join sfm.childQuestion.answerKeys c where sm.code=?",
				surveyCode);
	}
	@Override
	public List<Object[]> listSubFormQuestionsUnitsCatalogueBySurveyCode(String surveyCode) {
		return list(
				"Select c.code,c.name,c.typez From SurveyMaster sm inner join sm.surveyQuestionMapping sqm inner join sqm.question q inner join q.subFormQuestions sfm inner join sfm.childQuestion.units c where sm.code=?",
				surveyCode);
	}
	@Override
	public List<Object[]> listAnswerCataLogueBySurveyCode(String surveyCode) {
		return list(
				"Select c.code,c.name,c.typez From SurveyMaster sm inner join sm.surveyQuestionMapping sqm inner join sqm.question.answerKeys c where sm.code=?",
				surveyCode);
	}
	@Override
	public List<Object[]> listUnitCataLogueBySurveyCode(String surveyCode) {
		return list(
				"Select c.code,c.name,c.typez From SurveyMaster sm inner join sm.surveyQuestionMapping sqm inner join sqm.question.units c where sm.code=?",
				surveyCode);
	}
	@Override
	public List<Object[]> listSurveyMasters() {

		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(SurveyMaster.class);
		criteria.createAlias("sections","sec", Criteria.LEFT_JOIN);
		criteria.createAlias("dataLevel","dt", Criteria.LEFT_JOIN);
		criteria.setProjection(Projections.projectionList().add(
				Projections.property("code")).add(
				Projections.property("name")).add(
				Projections.sqlProjection(
						"GROUP_CONCAT(DISTINCT sec1_.code) as sectionss",
						new String[] { "sectionss" },
						new Type[] {StringType.INSTANCE})).add(
								Projections.property("dt.code")).add(
				Projections.groupProperty("id")));
		criteria.addOrder(Order.desc("name"));
		List list = criteria.list();
		session.flush();
		session.close();
		return list;
	}
	@Override
	public void saveFarmerCropProdAnswers(
			SurveyFarmerCropProdAnswers farmerCropProdAnswers) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		session.clear();
		farmerCropProdAnswers = removeQuestions(farmerCropProdAnswers);
		session.save(farmerCropProdAnswers);
	}
	
	private SurveyFarmerCropProdAnswers removeQuestions(
			SurveyFarmerCropProdAnswers farmerCropProdAnswers) {

		// webconsole
		if (!ObjectUtil.isEmpty(farmerCropProdAnswers
				.getFarmersSectionAnswers())) {
			for (SurveyFarmersSectionAnswers farmersSectionAnswers : farmerCropProdAnswers
					.getFarmersSectionAnswers()) {
				if (!ObjectUtil.isEmpty(farmersSectionAnswers
						.getFarmersQuestionAnswers())) {
					for (SurveyFarmersQuestionAnswers farmersQuestionAnswers : farmersSectionAnswers
							.getFarmersQuestionAnswers()) {
						if (!ObjectUtil.isEmpty(farmersQuestionAnswers))
							farmersQuestionAnswers.setQuestion(null);
					}
				}
			}
		}
		if (!ObjectUtil.isEmpty(farmerCropProdAnswers
				.getFarmersSectionAnswersList())) {
			for (SurveyFarmersSectionAnswers farmersSectionAnswers : farmerCropProdAnswers
					.getFarmersSectionAnswersList()) {
				if (!ObjectUtil.isEmpty(farmersSectionAnswers
						.getFarmersQuestionAnswersList())) {
					for (SurveyFarmersQuestionAnswers farmersQuestionAnswers : farmersSectionAnswers
							.getFarmersQuestionAnswersList()) {
						if (!ObjectUtil.isEmpty(farmersQuestionAnswers))
							farmersQuestionAnswers.setQuestion(null);
					}
				}
			}
		}
		return farmerCropProdAnswers;
	}
	
	
	public void editFarmerCropProdAnsStatus(long id, int status,
			String username, String date) {

		Session session = getSessionFactory().openSession();
		Query sqlQuery = session
				.createSQLQuery("UPDATE FARMER_CROP_PROD_ANSWERS SET SAVE_STATUS="
						+ status
						+ ", UPDATE_USER_NAME='"
						+ username
						+ "', UPDATE_DT='" + date + "' WHERE ID='" + id + "'");
		int i = sqlQuery.executeUpdate();
		session.flush();
		session.close();
	}
	
	public List<Object[]> listPartiallySavedFarmerCropPAInfo() {

		// TODO Auto-generated method stub
		List<Object[]> result = new ArrayList<Object[]>();
		Session session = getSessionFactory().openSession();
		
			SQLQuery sqlQuery = session
					.createSQLQuery("SELECT fcp.ID,fcp.ANSWERED_DATE,fcp.SURVEY_CODE,fcp.SURVEY_NAME,f.FARMER_CODE,f.FIRST_NAME,f.LAST_NAME,fcp.DATA_COLLECTOR_ID,fcp.DATA_COLLECTOR_NAME FROM survey_farmer_crop_prod_answers fcp "
							+ "LEFT JOIN farmer f ON f.FARMER_ID = fcp.FARMER_ID WHERE fcp.SAVE_STATUS = '1'");
			result = sqlQuery.list();
		
		session.flush();
		session.close();
		return result;
	}

	
	public SurveyFarmersQuestionAnswers findSurveyFarmerQuestionAswersById(Long id) {

		SurveyFarmersQuestionAnswers farmersQuestionAnswers = (SurveyFarmersQuestionAnswers) find(
				"FROM SurveyFarmersQuestionAnswers fqa WHERE fqa.id = ?", id);

		return farmersQuestionAnswers;
	}

	
	public SurveyFarmersQuestionAnswers findVerificationPhototById(Long id) {

		return (SurveyFarmersQuestionAnswers) find(
				"FROM SurveyFarmersQuestionAnswers fqa WHERE fqa.id =?", id);


	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyQuestion> listQuestionsBySectionCode(String sectionCode) {

		Session session = getSessionFactory().openSession();
		session.clear();
		Criteria criteria = session.createCriteria(SurveyQuestion.class);
		criteria.createAlias("section", "sec");
		criteria.add(Restrictions.eq("sec.code", sectionCode));
		List<SurveyQuestion> list = criteria.list();
		session.flush();
		session.close();
		return list;
	}
	
	public SurveyQuestion findSurveyQuestionByCode(String code) {

		return (SurveyQuestion) find("From SurveyQuestion q Where q.code=?", code);
	}
	@Override
	public DataLevel findDataLevelBySectionCode(String sectionId) {

		// TODO Auto-generated method stub
		return (DataLevel) find(
				"SELECT sec.dataLevel FROM SurveySection sec WHERE sec.code=?",
				sectionId);
	}

	@Override
	public List<FarmerField> findFieldsByParentIdandStatsuId(String code) {
		// TODO Auto-generated method stub
		return list("FROM FarmerField ff WHERE ff.parent is not null and ff.status=1 and ff.dataLevel.code=? ", code);
		
	}

	@Override
	public List<Long> listSurveyMasterIdsByAgentId(String agentId) {

		return list("Select sm.id From SurveyScope ss inner join ss.surveyMasters sm where ss.agent.profileId=?",
				agentId);
	}

	@Override
	public void editSurveyMaster(SurveyMaster surveyMaster) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		session.update(surveyMaster);
		for (LanguagePreferences preferences : surveyMaster.getLanguagePreferencesList()) {
			session.update(preferences);
		}
	}

	public SurveyMaster findSurveyMasterById(Long id) {

		return (SurveyMaster) find("FROM SurveyMaster sm WHERE sm.id = ?", id);
	}

	public List<SurveySection> listSectionsByCodes(List<String> codes) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("FROM SurveySection sec WHERE sec.code IN (:codes)");
		query.setParameterList("codes", codes);
		List<SurveySection> sections = query.list();
		session.flush();
		session.close();
		return sections;
	}

	

	public List<SurveyType> listSurveyTypes() {

		// TODO Auto-generated method stub
		return list("FROM SurveyType st");
	}

	public void updateSurveyMasterVerifyQuestion(long surveyId, List<Long> questionIds) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery deleteQuery = session
				.createSQLQuery("DELETE FROM SURVEY_VERIFY_QUS_MAP WHERE SURVEY_ID='" + surveyId + "'");
		deleteQuery.executeUpdate();
		for (Long qusId : questionIds) {
			SQLQuery insertQuery = session
					.createSQLQuery("INSERT INTO SURVEY_VERIFY_QUS_MAP VALUES('" + surveyId + "','" + qusId + "')");
			insertQuery.executeUpdate();
		}

	}

	public String findDataLevelCodeByInfraTypeCode(String infraTypeCode) {

		Session session = getSessionFactory().openSession();
		String queryString = "select data_level_code from infrastructure_type_master where INFRA_TYPE_CODE = '"
				+ infraTypeCode + "'";
		Query query = session.createSQLQuery(queryString);

		String dataLevelCode = query.list().get(0).toString();

		session.flush();
		session.close();

		return dataLevelCode;
	}

	public List<Section> listAllSectionsForFOFarmerFarm() {

		// TODO Auto-generated method stub
		return list("FROM SurveySection sec where sec.dataLevel.id IN ('1', '2', '3', '4') order by sec.name");
	}

	public List<SurveySection> listSectionCodesByDataLevelId(long dataLvlId) {

		// TODO Auto-generated method stub
		return list("FROM SurveySection sec WHERE sec.dataLevel.id=? order by sec.name", dataLvlId);
	}

	public boolean isSurveyMasterMappedWithSurveyScope(long id) {

		boolean isAgentMappedWithSurveuScope = false;
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM survey_scope_map where SURVEY_MASTER_ID=" + id;
		/*
		 * String queryString =
		 * "SELECT COUNT(*) FROM `farmer_crop_prod_answers` fcp WHERE fcp.`SURVEY_CODE` = 'CS001' AND fcp.`VERIFY_REF_SURVEY_ID`  > 0"
		 * + id;
		 */
		Query query = session.createSQLQuery(queryString);
		int count = Integer.valueOf(query.list().get(0).toString());
		if (count > 0) {
			isAgentMappedWithSurveuScope = true;
		}
		session.flush();
		session.close();
		return isAgentMappedWithSurveuScope;

	}


	public SurveyType findSurveyTypeById(long id) {

		// TODO Auto-generated method stub
		return (SurveyType) find("FROM SurveyType st WHERE st.id=?", id);
	}

	public List<String> listQuestionsByIds(List<String> filteredIds) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("SELECT qus.code FROM SurveyQuestion qus WHERE qus.code IN (:codes)");
		query.setParameterList("codes", filteredIds);
		List<String> questions = query.list();
		session.flush();
		session.close();
		return questions;
	}

	public void removeSurveyMasterQuestionMapping(SurveyMaster surveyMaster) {

		Session session = getSessionFactory().openSession();
		String sss = "DELETE FROM SURVEY_QUESTION_MAP WHERE SURVEY_ID=" + surveyMaster.getId() + ";";
		Query query = session.createSQLQuery(sss);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	public void removeSurveyMasterTarget(SurveyMaster surveyMaster) {

		Session session = getSessionFactory().openSession();
		String sss = "DELETE FROM SURVEY_MASTER_TARGET WHERE SURVEY_ID=" + surveyMaster.getId() + ";";
		Query query = session.createSQLQuery(sss);
		query.executeUpdate();
		session.flush();
		session.close();

	}

	public int isSurveyMasterVerified(String code) {

		int isSurveyMasterVerified = 0;
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT COUNT(*) FROM `survey_farmer_crop_prod_answers` fcp WHERE fcp.`SURVEY_CODE` = '" + code
				+ "' AND fcp.`VERIFY_REF_SURVEY_ID`  > 0";
		Query query = session.createSQLQuery(queryString);
		int count = Integer.valueOf(query.list().get(0).toString());
		if (count > 0) {
			isSurveyMasterVerified = 1;
		}
		session.flush();
		session.close();
		return isSurveyMasterVerified;
	}

	public List<Object[]> listAllSectionsObjectArrayForFOFarmerFarm() {

		// TODO Auto-generated method stub
		// return
		// list("FROM Section sec where sec.dataLevel.id IN ('1', '2', '3', '4')
		// order by sec.name");

		Session session = getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(
				"SELECT code, name FROM `section` WHERE `DATA_LEVEL_ID` IN ('1', '2', '3', '4') ORDER BY `NAME`");
		List<Object[]> list = query.addScalar("code", StringType.INSTANCE).addScalar("name", TextType.INSTANCE).list();
		session.flush();
		session.close();
		return list;
	}

	public List<SurveyQuestion> listQuestionsBySectionCodes(List<String> sectionCodes) {

		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM SurveyQuestion qus WHERE qus.section.code IN (:codes) AND qus.status = :stat");
		query.setParameterList("codes", sectionCodes);
		query.setParameter("stat", true);
		List<SurveyQuestion> questions = query.list();
		session.flush();
		session.close();
		return questions;
	}

	@Override
	public List<SurveyQuestion> listQuestionsBySectionCodesAndQuestionType(List<String> sectionCodes, int questionTyp) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(
				"FROM SurveyQuestion qus WHERE qus.section.code IN (:codes) AND qus.status = :stat AND  qus.questionType <> :questionTyp");
		query.setParameterList("codes", sectionCodes);
		query.setParameter("questionTyp", String.valueOf(questionTyp));
		query.setParameter("stat", true);
		List<SurveyQuestion> questions = query.list();
		session.flush();
		session.close();
		return questions;
	}

	public LanguagePreferences findLanguagePreferenceByCodeAndLanguage(
			String string, String loggedInUserLanguage) {
		// TODO Auto-generated method stub
		return (LanguagePreferences) find(
				"from LanguagePreferences lp where lp.code=? AND lp.lang=?",
				new Object[] { string, loggedInUserLanguage });
	}
	
	public List<SurveyMaster> listSurveyMasterObjectByDvId(
			String dataCollectorId, long revisionNo) {

		Object[] bindValues = { dataCollectorId, revisionNo };
		return (List<SurveyMaster>) list(
				"Select sm From VerifierScope ss inner join ss.surveyMasters sm where ss.agent.profileId=? and sm.revisionNumber>=? ",
				bindValues);
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyMaster> listSurveyMasterObjectByAgentId(
			String dataCollectorId, long revisionNo) {
			
					Object[] bindValues = { dataCollectorId, revisionNo };
		return (List<SurveyMaster>) list(
				"Select DISTINCT sm From SurveyScope ss inner join ss.surveyMasters sm where ss.agent.profileId=? and sm.revisionNumber>=? and sm.surveyType.id='"
						+ SurveyType.GENERAL
						+ "' or sm.surveyType.id='"
						+ SurveyType.CERTIFICATION
						+ "' or sm.surveyType.id='"
						+ SurveyType.TRAINING
						+ "' or sm.surveyType.id='"
						+ SurveyType.INTERNAL_PROGRAMS
						+ "' or sm.surveyType.id='"
						+ SurveyType.YIELD_MEASUREMENT
						+ "' or sm.surveyType.id='"
						+ SurveyType.FARMER_FAMILY_MEMBER
						+ "' or sm.surveyType.id='"
						+ SurveyType.FAMILY_PROFILE
						+ "' or sm.surveyType.id='"
						+ SurveyType.SOIL_PROFILE
						+ "' or sm.surveyType.id='"
						+ SurveyType.DIAGNOSTIC
						+ "'", bindValues);
	}
	
	public long surveyMasterRevisionByAgentId(String agentId) {

		Object obj = (Object) find(
				"Select MIN(sm.revisionNumber) From SurveyScope ss inner join ss.surveyMasters sm where ss.agent.profileId=?",
				agentId);
		return ObjectUtil.isLong(obj) ? Long.valueOf(String.valueOf(obj)) : 0;
	}
	
	public DataLevel findDataLevelByCode(String code) {

		return (DataLevel) find("From DataLevel d WHERE d.code=?", code);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> listSectionBySurveyMaster(SurveyMaster sm) {

		Session session = getSessionFactory().openSession();
		String qry = "SELECT\n"
				+ " (SELECT dlvc.CODE FROM data_level dlvc WHERE dlvc.id=max(dlv.ID)),\n"
				+ "  (SELECT dlvc.`NAME` FROM data_level dlvc WHERE dlvc.id=max(dlv.ID)),\n"
				+ " GROUP_CONCAT(sec. CODE),\n" + " GROUP_CONCAT(sec. NAME)\n"
				+ "FROM\n" + " survey_section_map secmap\n"
				+ "INNER JOIN section sec ON sec.ID = secmap.SECTION_ID\n"
				+ "INNER JOIN data_level dlv ON dlv.ID = sec.DATA_LEVEL_ID\n"
				+ "WHERE\n" + " secmap.SURVEY_ID = '" + sm.getId() + "'";
		SQLQuery frmsqlQuery = session.createSQLQuery(qry);

		List<Object[]> results = frmsqlQuery.list();
		session.flush();
		session.close();
		return results;

	}
	
	public List<LanguagePreferences> findNameLanguageAndType(String lang,
			String codd) {

		List<String> stList = new ArrayList<String>();
		String[] ss = null;

		String[] tokens = codd.split(",", -1);
		for (String sst : tokens) {
			stList.add(sst);
		}
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM LanguagePreferences lp WHERE lp.code IN (:codes) and lp.lang='"
						+ lang + "'");
		query.setParameterList("codes", stList);
		List<LanguagePreferences> lpref = query.list();
		session.flush();
		session.close();
		return lpref;
	}
	
	public List<String> listFoCodesByCustomerProgram(List<String> codes) {

		Session session = getSessionFactory().openSession();
		List<String> foCodes = new ArrayList<String>();
		Query query = session
				.createSQLQuery("select fo.code from `farmer_organisation` `fo` where find_in_set(:code,`fo`.`FO_CARGILL_PROGRAM`)");
		for (String code : codes) {
			query.setParameter("code", code);
			List<String> codesList = (List<String>) query.list();
			foCodes.addAll(codesList);
		}
		session.flush();
		session.close();
		return foCodes;
	}
		

	public List<DownloadTask> listDownloadTaskByUser(String userName) {

		// TODO Auto-generated method stub
		return list("from DownloadTask dt where dt.userName=?", userName);
	}
	
	public void updateDownloadTask(DownloadTask downloadTask) {

		// TODO Auto-generated method stub
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		session.update(downloadTask);
		session.flush();
		session.close();
	}
	
	public DownloadTask findDownloadTaskById(long id) {

		// TODO Auto-generated method stub
		return (DownloadTask) find("from DownloadTask dt where dt.id=?", id);
	}
	
	@Override
	public List<Object[]> findFarmerQuestionAnswerByIdAndMultiAnswers(List<Long> id) {
		
				
		Session session = getSessionFactory().getCurrentSession();
		
		Criteria criteria = session.createCriteria(FarmersQuestionAnswers.class);
		criteria.createAlias("answers", "ans");
		criteria.add(Restrictions.in("id", id));
		criteria.setProjection(Projections.projectionList().add(
				Projections.sqlProjection(
						"GROUP_CONCAT(ans1_.ANSWER_1) as anss",
						new String[] { "anss" },
						new Type[] {StringType.INSTANCE})).add(Projections.groupProperty("id")));
	

		return criteria.setFetchMode("ans", FetchMode.JOIN).list();
	
	
		
	
	}
	
	
	@Override
	public List<String> findFQAAnswersByAnswerId(long id) {
		return (List<String>) list(
				"select ans.answer1 FROM FarmersQuestionAnswers  fqa join fqa.answers ans WHERE fqa.id=?",
				id);
	}
	@Override
	public List<Object[]> listSamithiByFcpa() {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(
				"SELECT	distinct code,name FROM	warehouse WHERE	 FIND_IN_SET(code,(SELECT group_concat(fcpa.coopeartive_code SEPARATOR  ',') FROM survey_farmer_crop_prod_answers fcpa))");
	
		List list = query.list();
		return list;
	}
	
	@Override
	public SurveyQuestion findQuestionByParentDepen(String str) {
		return (SurveyQuestion) find("From SurveyQuestion q Where q.parentDepen=?", str.trim());
	}
	
	@Override
	public String findDependancyKeyByQuestionId(Long parentId) {
		return (String) find("Select  q.dependencyKey From SurveyQuestion q Where q.id=?", parentId);
	}
	@Override
	public List<LanguagePreferences> listLanguagePreferences() {
		// TODO Auto-generated method stub
		return list("from LanguagePreferences");
	}
	public List<String> listFilteredQuestionsByIds(List<String> ids) {

		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("SELECT qus.code FROM SurveyQuestion qus WHERE qus.parentQuestion.code IN (:codes)");
		query.setParameterList("codes", ids);
		List<String> quest = query.list();
		session.flush();
		session.close();
		return quest;
	}
	@Override
	public List<FarmField> findFarmFieldsByParentIdandStatsuId(String sectionId2) {
		// TODO Auto-generated method stub
		return list("FROM FarmField ff WHERE ff.parent is not null and ff.status=1 ");
	
	}
	@Override
	public List<Object[]> findFarmCropsFieldsByParentIdandStatsuId(String sectionId2) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(
				"SELECT	TYPE_NAME,NAME,DATA_LEVEL FROM	once_qn_fields where  status='1'");
	
		List list = query.list();
		return list;
	}
	@Override
	public List<SurveyMaster> listSurveyBySurveyCodes(List<String> codes) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM SurveyMaster qus WHERE qus.code IN (:codes)");
		query.setParameterList("codes", codes);
		List<SurveyMaster> questions = query.list();
		session.flush();
		session.close();
		return questions;
	}
	@Override
	public List<SurveyMaster> listAgentSurveyMaster(String agentId,String revisionNo) {
		// TODO Auto-generated method stub
		return list("Select distinct f.surveyMaster  FROM Agent a left join fetch a.surveys f WHERE a.profileId = ? and  f.surveyMaster.revisionNumber=? order by  f.surveyMaster.revisionNumber desc", new Object[]{agentId,Long.valueOf(revisionNo)});
	}
	@Override
	public List<SurveyQuestion> listSurveyQuestion(String revisionNo) {
		return list("from SurveyQuestion where revisionNumber > ? order by revisionNumber desc ",Long.valueOf(revisionNo));
	}
	@Override
	public List<SurveySection> listSurveySections(String revisionNo) {
		return list("from SurveySection where revisionNumber > ? order by revisionNumber desc ",Long.valueOf(revisionNo));
	}
	
	@Override
	public List<LanguagePreferences> listLanguagePreferencesByLanguage(int type) {
		return list(
				"From LanguagePreferences lp WHERE lp.type=?",
				type);
	}
	
	@Override
	public List<LanguagePreferences> listLangPrefByCodes(List<String> codes) {
		// TODO Auto-generated method stub
		
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("FROM LanguagePreferences lp WHERE lp.code in (:codes)");
		query.setParameterList("codes", codes);
		List<LanguagePreferences> questions = query.list();
		session.flush();
		session.close();
		return questions;
		
	}

}





