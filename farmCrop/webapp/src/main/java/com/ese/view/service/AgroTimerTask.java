/*
 * AgroTimerTask.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.util.TimerTask;

import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxn;

public class AgroTimerTask extends TimerTask {

    private int times = 0;
    private boolean cancelled = false;
    private String agentId = null;
    private IAgentService agentService;
    public int TIMER_COUNT = 0;
    private IPreferencesService preferncesService;

    /**
     * Gets the times.
     * @return the times
     */
    public int getTimes() {

        return this.times;
    }

    /**
     * Checks if is cancelled.
     * @return true, if is cancelled
     */
    public boolean isCancelled() {

        return this.cancelled;
    }

    /**
     * Sets the times.
     * @param times the new times
     */
    public void setTimes(int times) {

        this.times -= times;
    }

    /**
     * Instantiates a new agro timer task.
     */
    public AgroTimerTask() {

    }

    /**
     * Instantiates a new agro timer task.
     * @param agentId the agent id
     * @param agentService the agent service
     * @param preferncesService the prefernces service
     */
    public AgroTimerTask(String agentId, IAgentService agentService,
            IPreferencesService preferncesService) {

        this.agentId = agentId;
        this.agentService = agentService;
        this.preferncesService = preferncesService;
        String timerValue = preferncesService.findAgentTimerValue();
        TIMER_COUNT = (!StringUtil.isEmpty(timerValue) ? Integer.parseInt(timerValue) : 0);
        
    }

    /**
     * @see java.util.TimerTask#run()
     */
    public void run() {

        times++;
        if (times > TIMER_COUNT) {
            cancelTimer(true);
        }

    }

    /**
     * Cancel timer.
     * @param isEod the is eod
     */
    public void cancelTimer(boolean isEod) {

        if (isEod)
            agentService.updateAgentBODStatus(agentId, ESETxn.EOD);
        this.cancel();
        cancelled = true;
    }

}
