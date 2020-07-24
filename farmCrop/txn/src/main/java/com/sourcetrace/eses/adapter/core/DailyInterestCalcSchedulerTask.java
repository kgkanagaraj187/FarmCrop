package com.sourcetrace.eses.adapter.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.service.IFarmerService;

@Component
public class DailyInterestCalcSchedulerTask implements Runnable {
    private static final Logger LOGGER = Logger
            .getLogger(DailyInterestCalcSchedulerTask.class.getName());
    @Autowired
    private IFarmerService farmerService;

    /*
     * (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {

        try {
            LOGGER.info("Daily Interest Calculation Scheduler Started");
            farmerService.processDailyInterestCalc();
            LOGGER.info("Daily Interest Calculation Scheduler Ended");
        } catch (Exception e) { // Catches all type of exception
            LOGGER.info("Caught Exception in Daily Interest Calculation Scheduler !!!");
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

}
