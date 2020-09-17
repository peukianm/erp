/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.scheduler;

import erp.util.SystemParameters;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule; 
import javax.ejb.Singleton;

/**
 *   
 * @author peukianm
 */
@Singleton
public class ERPLoggerScheduler {

    @EJB
    private LoggerDataRetrieveTask retrieverTask;
  
    @Lock(LockType.READ)     
    @Schedule(second = "0", minute = "58", hour = "7,11,15,23", persistent = false)
    public void atScheduleLoggers() throws InterruptedException {
        if (SystemParameters.getInstance().getProperty("SCHEDULER_ENABLE") != null && SystemParameters.getInstance().getProperty("SCHEDULER_ENABLE").equals("true")
                && SystemParameters.getInstance().getProperty("LOGGER_RETRIEVER_TASK_ENABLE").equals("true")) {
            retrieverTask.doSchedulerWork(false);
        }
    }
}
