/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.scheduler;

import erp.dao.CompanyDAO;
import erp.dao.SchedulerDAO;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Taskstatus;
import erp.util.FormatUtils;
import erp.util.SystemParameters;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Singleton
public class StaffUpdateTask {

    private static final Logger logger = LogManager.getLogger(StaffUpdateTask.class);
    private AtomicBoolean busy = new AtomicBoolean(false);

    @EJB
    SchedulerDAO schedulerDAO;

    @EJB
    CompanyDAO companyDAO;

    Taskstatus taskStatus;

    final String DATEPATTERN = "yyyy-MM-dd";
    final String TIMEPATTERN = "HH:mm:ss";
    final String FULLDATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Lock(LockType.READ)
    public int doSchedulerWork(Boolean force) throws InterruptedException {
        Company company = companyDAO.getCompany(Long.parseLong(SystemParameters.getInstance().getProperty("DEFAULT_COMPANY_ID")));   //(Company) persistenceHelper.find(Company.class, Long.parseLong(SystemParameters.getInstance().getProperty("DEFAULT_COMPANY_ID")));
        Scheduletask task = schedulerDAO.getScheduleTask(Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")));
        Companytask cTask = schedulerDAO.findCtask(company, task);
        Scheduletaskdetail taskDetails = null;
        if (!force) {
            if (busy.get() || cTask.getActive() == BigDecimal.ZERO
                    || cTask.getTaskstatus().getStatusid() != Long.parseLong(SystemParameters.getInstance().getProperty("TASK_IDLE"))) {
                return 0;
            }
        } else {
            if (busy.get()) {
                 return 0;
             }
        }

        try {
            busy.set(true);
            System.out.println("START TASK UPDATE STAFF !!!!!!!!!!!!!!!!");
            Timestamp startTaskTime = FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN);
            logger.info("Starting Schedule Task " + task.getName() + " for Company " + company.getAbbrev() + " at " + startTaskTime);
            cTask.setLastexecutiontime(startTaskTime);
            taskStatus = schedulerDAO.getTaskstatus(Long.parseLong(SystemParameters.getInstance().getProperty("TASK_ONPROGRESS")));
            cTask.setTaskstatus(taskStatus);
            schedulerDAO.updateCtask(cTask);

            taskDetails = new Scheduletaskdetail();
            taskDetails.setCompanytask(cTask);
            taskDetails.setStartExecutiontime(startTaskTime);

            taskMainBody(cTask);

            //SUCCESS!!!!!!!!!!!!!!!!!!
            Timestamp endTaskTime = FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN);
            long secs = FormatUtils.getDateDiff(taskDetails.getStartExecutiontime(), endTaskTime, TimeUnit.SECONDS);

            taskStatus = schedulerDAO.getTaskstatus(Long.parseLong(SystemParameters.getInstance().getProperty("TASK_IDLE")));
            cTask.setTaskstatus(taskStatus);

            taskDetails.setEndExecutiontime(endTaskTime);
            taskDetails.setExecutiontime(FormatUtils.splitSecondsToTime(secs));
            taskStatus = schedulerDAO.getTaskstatus(Long.parseLong(SystemParameters.getInstance().getProperty("TASK_SUCCESS")));
            taskDetails.setTaskstatus(taskStatus);

            schedulerDAO.updateCtask(cTask);
            schedulerDAO.saveTaskDetails(taskDetails);

            System.out.println("TASK UPDATE STAFF ENDED WITH SUCESSS in " + FormatUtils.splitSecondsToTime(secs) + " !!!!!!!!!!!!!!!!");
            logger.info("Starting Schedule Task " + task.getName() + " for Company " + company.getAbbrev() + " SUCCEDED at " + endTaskTime + " in " + FormatUtils.splitSecondsToTime(secs));
            return 1;
        } catch (Exception ex) {
            Timestamp endTaskTime = FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN);
            long secs = FormatUtils.getDateDiff(taskDetails.getStartExecutiontime(), endTaskTime, TimeUnit.SECONDS);

            taskStatus = schedulerDAO.getTaskstatus(Long.parseLong(SystemParameters.getInstance().getProperty("TASK_IDLE")));
            cTask.setTaskstatus(taskStatus);

            taskDetails.setEndExecutiontime(endTaskTime);
            taskDetails.setExecutiontime(FormatUtils.splitSecondsToTime(secs));
            taskStatus = schedulerDAO.getTaskstatus(Long.parseLong(SystemParameters.getInstance().getProperty("TASK_ERROR")));
            taskDetails.setTaskstatus(taskStatus);

            schedulerDAO.updateCtask(cTask);
            schedulerDAO.saveTaskDetails(taskDetails);
            System.out.println("TASK DATA UPDATE STAFF FAILED " + FormatUtils.splitSecondsToTime(secs) + " !!!!!!!!!!!!!!");
            logger.info("Starting Schedule Task " + task.getName() + " for Company " + company.getAbbrev() + " failed " + endTaskTime);
            ex.printStackTrace();
            return -1;

        } finally {
            busy.set(false);
        }
    }

    public void taskMainBody(Companytask cTask) throws Exception {

        try {

        } catch (Exception e) {
            goError(e);
            throw e;
        }
    }

    public void goError(Exception ex) {
        logger.error("-----------AN ERROR HAPPENED ON STAFF UPDATE DATA SCHEDULER !!!! -------------------- : " + ex.toString());
        logger.error("Cause=" + ex.getCause());
        logger.error("Class=" + ex.getClass());
        logger.error("Message=" + ex.getLocalizedMessage());
        logger.error(ex, ex);
        logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");
    }

}
