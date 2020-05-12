/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.scheduler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 *
 * @author peukianm
 */
@Singleton
@Startup
public class LoggerDataRetrieveTimer {

    @Resource
    private TimerService timerService;

    public void setTimerService(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostConstruct
    private void postConstruct() {
        timerService.createCalendarTimer(createSchedule());
    }

    @Timeout
    public void timerTimeout(Timer timer) {
        // Add your code here to be called when scheduling is reached...
        // in this example: 01h:30m every day ;-)
    }

    private ScheduleExpression createSchedule() {

        ScheduleExpression expression = new ScheduleExpression();        
        expression.dayOfWeek("Sun,Mon,Tue,Wed,Thu,Fri,Sat");
        expression.hour("01");
        expression.minute("30");

        return expression;
    }

}
