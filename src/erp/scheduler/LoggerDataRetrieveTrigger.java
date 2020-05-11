/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.scheduler;

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
public class LoggerDataRetrieveTrigger {
    @EJB
    private LoggerDataRetrieveTask retrieverTask;
 
    @Lock(LockType.READ)
    @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
    public void atSchedule() throws InterruptedException {
        //System.out.println("START!!!!!!!!!!!!!!!!!");
        retrieverTask.doSchedulerWork();
    }
    
}
