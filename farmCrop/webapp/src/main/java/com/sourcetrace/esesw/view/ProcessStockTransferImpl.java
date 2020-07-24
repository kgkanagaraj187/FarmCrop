/*
 * ProcessStockTransferImpl.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view;

import java.util.ArrayList;
import java.util.List;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.order.entity.txn.VillageWarehouse;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class ProcessStockTransferImpl {
    private IProductDistributionService productDistributionService;
    public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	private IProductService productService;
    public IProductService getProductService() {
		return productService;
	}

	private IAgentService agentService;

    public IAgentService getAgentService() {
		return agentService;
	}

	/**
     * Checks if is pending mtnt exists for agent.
     * 
     * @param agentId the agent id
     * 
     * @return true, if is pending mtnt exists for agent
     */
    public boolean isPendingMTNTExistsForAgent(String agentId) {

        return this.productDistributionService.isPendingMTNTExistsForAgent(agentId);
    }

    /**
     * Removes the pending mtnt stock.
     * 
     * @param agentId the agent id
     */
    public void removePendingMTNTStock(String agentId) {

        if (!StringUtil.isEmpty(agentId)) {
            productDistributionService.removePendingMTNTStockByAgentId(agentId);
        }
    }

    /**
     * Gets the agents from service point.
     * 
     * @param agentId the agent id
     * 
     * @return the agents from service point
     */
    public List<Agent> getAgentsFromServicePoint(String agentId) {

        List<Agent> agents = new ArrayList<Agent>();
        if (!StringUtil.isEmpty(agentId)) {
            Agent agent = agentService.findAgentByProfileId(agentId);
            agents = agentService.listAgentByServicePoint(agentId, agent.getServicePoint()
                    .getName());
        }
        return agents;
    }

    /**
     * Move stocks to another agent.
     * 
     * @param id the id
     * @param profileId the profile id
     * 
     * @return the string
     */
    public String moveStocksToAnotherAgent(String id, String profileId) {

        // Check whether agent is present.
        String responseString = "0";
        if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(profileId)
                && !ObjectUtil.isEmpty(agentService.findAgentByProfileId(profileId))) {
            // here there are two cases
            // when agent a and b has products 1 & 2. so we can do update process only
            // when agent a has product 1 & 2 and b has only 2 nd product. So we have to insert
            // product 1 and update 2nd product.
            List<VillageWarehouse> villageWarehouseForFromAgent = productDistributionService
                    .listVillageWarehouseForAgent(id);
            for (VillageWarehouse villageWarehouse : villageWarehouseForFromAgent) {
                VillageWarehouse villageWarehouseForToAgent = productDistributionService
                        .findVillageWarehouse(villageWarehouse.getVillage().getId(), 
                                villageWarehouse.getProcurementProduct().getId(), profileId);
                if (!ObjectUtil.isEmpty(villageWarehouseForToAgent)) {
                    // Update
                    villageWarehouseForToAgent.setNumberOfBags(villageWarehouseForToAgent
                            .getNumberOfBags()
                            + villageWarehouse.getNumberOfBags());
                    villageWarehouseForToAgent.setGrossWeight(villageWarehouseForToAgent
                            .getGrossWeight()
                            + villageWarehouse.getGrossWeight());
                    productService.updateVillageWarehouse(villageWarehouseForToAgent);
                } else {
                    // Insert
                    villageWarehouseForToAgent = new VillageWarehouse();
                    villageWarehouseForToAgent.setVillage(villageWarehouse.getVillage());
                    villageWarehouseForToAgent.setNumberOfBags(villageWarehouse.getNumberOfBags());
                    villageWarehouseForToAgent.setGrossWeight(villageWarehouse.getGrossWeight());
                    villageWarehouseForToAgent.setProcurementProduct(villageWarehouse
                            .getProcurementProduct());
                    villageWarehouseForToAgent.setAgentId(profileId);
                    productService.addVillageWarehouse(villageWarehouseForToAgent);
                }
            }
            productDistributionService.removePendingMTNTStockByAgentId(id);
            responseString = "1";
        }
        return responseString;
    }

    /**
     * Sets the product service.
     * 
     * @param productService the new product service
     */
    public void setProductService(IProductService productService) {

        this.productService = productService;
    }

    /**
     * Sets the product distribution service.
     * 
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Sets the agent service.
     * 
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

}
