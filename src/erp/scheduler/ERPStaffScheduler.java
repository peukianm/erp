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
public class ERPStaffScheduler {

    @EJB
    private StaffUpdateTask staffUpdateTask;

    @Lock(LockType.READ)
    @Schedule(second = "*", minute = "*", hour = "*/2", persistent = false)
    public void atScheduleStaff() throws InterruptedException {
        if (SystemParameters.getInstance().getProperty("SCHEDULER_ENABLE") != null && SystemParameters.getInstance().getProperty("SCHEDULER_ENABLE").equals("true")
                && SystemParameters.getInstance().getProperty("STAFF_UPDATE_TASK_ENABLE").equals("true")) {
            staffUpdateTask.doSchedulerWork(false);
        }
    }

}
