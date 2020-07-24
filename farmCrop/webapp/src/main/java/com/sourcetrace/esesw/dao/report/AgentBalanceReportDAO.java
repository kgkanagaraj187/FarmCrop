package com.sourcetrace.esesw.dao.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.sourcetrace.eses.dao.ReportDAO;
import com.sourcetrace.eses.order.entity.txn.AgentBalanceReport;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;

public class AgentBalanceReportDAO extends ReportDAO {


	private SimpleDateFormat DataBaseFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	protected void addExampleFiltering(Criteria criteria, Map params) {

		// check for filter entity
		AgentBalanceReport entity = (AgentBalanceReport) params
				.get(FILTER);

		if (entity != null) {
			Date from = (Date) params.get(FROM_DATE);
			// to date
			Date to = (Date) params.get(TO_DATE);
			if(!ObjectUtil.isEmpty(from)&&!ObjectUtil.isEmpty(to)){
				criteria.add(Restrictions.between("txnTime", from, to));
			}

			 //Criteria for TxnType
            if(entity.getBalanceType() == 1)
                criteria.add(Restrictions.disjunction().add(Restrictions.eq("txnType", "316"))
                        .add(Restrictions.eq("txnType", "334P")).add(Restrictions.eq("txnType", "300P")));
            else
                criteria.add(Restrictions.disjunction().add(Restrictions.eq("txnType", "314"))
                        .add(Restrictions.eq("txnType", "344")).add(Restrictions.eq("txnType", "334D")).add(Restrictions.eq("txnType", "300D")));
            
			if (ObjectUtil.isEmpty(entity.getProfileId())) {
				// from date
				if (entity.getAgentId() != null) {
					String selectedAgent = entity.getAgentId();
					if (selectedAgent != null && !"".equals(selectedAgent)) {
						criteria.add(Restrictions.like("agentId",
								selectedAgent, MatchMode.ANYWHERE));
					}
				}

				if (entity.getAgentName() != null
						&& !"".equals(entity.getAgentName())) {
					criteria.add(Restrictions.like("agentName", entity
							.getAgentName(), MatchMode.ANYWHERE));
				}

				if (entity.getAccountNumber() != null
						&& !"".equals(entity.getAccountNumber())) {
					criteria.add(Restrictions.like("accountNumber", entity
							.getAccountNumber(), MatchMode.ANYWHERE));
				}
				if (entity.getAccountType() != null
						&& !"".equals(entity.getAccountType())) {
					criteria.add(Restrictions.like("accountType", entity
							.getAccountType(), MatchMode.ANYWHERE));
				}
			} else {
				if (entity.getProfileId() != null
						&& !"".equals(entity.getProfileId())) {
					criteria.add(
							Restrictions.like("agentId", entity.getProfileId(),
									MatchMode.EXACT)).addOrder(
							Order.desc("txnTime"));
				}
			}

			// sort a column in the given direction ascending/descending
			String dir = (String) params.get(DIR);
			String sort = (String) params.get(SORT_COLUMN);
			if (dir != null && sort != null) {
				if (dir.equals(DESCENDING)) {
					criteria.addOrder(Order.desc(sort));
				} else {
					criteria.addOrder(Order.asc(sort));
				}
			}
		}

	}

	@Override
	protected void addProjections(Criteria criteria, Map params) {
		AgentBalanceReport entity = (AgentBalanceReport) params
				.get(FILTER);
		if (ObjectUtil.isEmpty(entity.getProfileId())) { // projections only for
			// parent grid

			Date from = (Date) params.get(FROM_DATE);
			// to date
			Date to = (Date) params.get(TO_DATE);

			Date dayStartDateTime = DateUtil.getDateWithoutTime(from);
			Date dayEndDateTime = DateUtil.getDateWithLastMinuteofDay(to);

			String dayStartDate = DataBaseFormat.format(dayStartDateTime);
			String dayEndDate = DataBaseFormat.format(dayEndDateTime);

			//TxnType 
            String txnType;
            if(entity.getBalanceType() == 1)
                txnType = "'316' OR trxn_agro.TXN_TYPE = '334P' OR trxn_agro.TXN_TYPE = '300P'";
            else
                txnType = "'314' OR trxn_agro.TXN_TYPE = '344' OR trxn_agro.TXN_TYPE = '334D' OR trxn_agro.TXN_TYPE = '300D'";
            
			String finalBalanceSqlQuery = "(SELECT trxn_agro.BAL_AMT FROM trxn_agro WHERE "
					+ "trxn_agro.AGENT_ID=y0_ AND "
					+ "trxn_agro.PROF_TYPE !=  '02' AND "
					+ "trxn_agro.OPER_TYPE = 1 AND "
					+ "trxn_agro.TXN_TIME BETWEEN'"
					+ dayStartDate
					+ "' AND '"
					+ dayEndDate
					+ "' AND (trxn_agro.TXN_TYPE = " +txnType+ ") ORDER BY trxn_agro.ID DESC LIMIT 1) as finalBalance";

			criteria.setProjection(Projections.projectionList()

			.add(Projections.groupProperty("agentId")).add(
                    Projections.property("profType")).add( // group records according to agentId
					Projections.property("agentName")).add(
					Projections.property("servicePointName")).add(
					Projections.property("accountNumber")).add(
					Projections.sqlProjection(finalBalanceSqlQuery,
							new String[] { "finalBalance" },
							new Type[] { StandardBasicTypes.DOUBLE }))
			// if any change of the above property(even order)--- check with AgentBalanceSheetReportAction-->toJSON()
					);
		}
	}
}
