package erp.scheduler;

import erp.bean.LoggerData;
import erp.dao.CompanyDAO;
import erp.dao.SchedulerDAO;
import erp.entities.Attendance;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Staff;
import erp.entities.Taskstatus;
import erp.util.FormatUtils;
import erp.util.PersistenceHelper;
import erp.util.SystemParameters;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ejb.*;
import javax.ejb.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author peukianm
 */
@Singleton
public class LoggerDataRetrieveTask {

    private static final Logger logger = LogManager.getLogger(LoggerDataRetrieveTask.class);
    private AtomicBoolean busy = new AtomicBoolean(false);

    @EJB
    SchedulerDAO schedulerDAO;

    @EJB
    CompanyDAO companyDAO;

    final String DATEPATTERN = "yyyy-MM-dd";
    final String TIMEPATTERN = "HH:mm:ss";
    final String FULLDATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    Taskstatus taskStatus;

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
            System.out.println("START TASK RETRIEVE DATA FROM LOGGERS !!!!!!!!!!!!!!!!");
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

            System.out.println("TASK DATA RETRIEVE FORM LOGGERS ENDED WITH SUCESSS in " + FormatUtils.splitSecondsToTime(secs) + " !!!!!!!!!!!!!!!!");
            logger.info("Starting Schedule Task " + task.getName() + " for Company " + company.getAbbrev() + " SUCCEDED at " + endTaskTime + " in " + FormatUtils.splitSecondsToTime(secs));
            return 1;
        } catch (Exception ex) {
            //FAILURE
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
            System.out.println("TASK DATA RETRIEVE FORM LOGGERS FAILED " + FormatUtils.splitSecondsToTime(secs) + " !!!!!!!!!!!!!!");
            logger.info("Starting Schedule Task " + task.getName() + " for Company " + company.getAbbrev() + " failed " + endTaskTime);
            ex.printStackTrace();
            return -1;

        } finally {
            busy.set(false);
        }
    }

    public void taskMainBody(Companytask cTask) throws Exception {

        try {
            int loggerscount = Integer.parseInt(SystemParameters.getInstance().getProperty("COMPANYLOGGERS"));
            File file = null;
            List<LoggerData> logerDataList = new ArrayList<LoggerData>();
            for (int i = 1; i <= loggerscount; i++) {

                file = new File(SystemParameters.getInstance().getProperty("LOGGER" + i + "_PATH"));

                FileInputStream excelFile = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);

                int pointer = 0;
                switch (i) {  //CHANGE!!!!!!!!!!!!!!!!!
                    case 1:
                        pointer = cTask.getTaskdata1() == null ? 0 : Integer.parseInt(cTask.getTaskdata1());
                        break;
                    case 2:
                        pointer = cTask.getTaskdata2() == null ? 0 : Integer.parseInt(cTask.getTaskdata2());
                        break;
                    case 3:
                        pointer = cTask.getTaskdata3() == null ? 0 : Integer.parseInt(cTask.getTaskdata3());
                        break;
                    default:
                        pointer = cTask.getTaskdata4() == null ? 0 : Integer.parseInt(cTask.getTaskdata4());
                        break;
                }

                int counter = 0;
                Row currentRow = null;
                Iterator<Row> iterator = datatypeSheet.iterator();
                Staff staff = null;

                for (int j = 0; j < pointer; j++) {
                    iterator.next();
                }

                List<Staff> allStaff = schedulerDAO.getAllStaff(true);
                while (iterator.hasNext()) {
                    counter++;
                    currentRow = iterator.next();
                    String lc = String.valueOf((int) currentRow.getCell(0).getNumericCellValue());
                    staff = allStaff.stream().filter(stf -> lc
                            .equals(stf.getLoggercode()))
                            .findAny()
                            .orElse(null);

                    if (staff != null) {
                        LoggerData logerData = new LoggerData(currentRow.getCell(0).getNumericCellValue(),
                                currentRow.getCell(1).getDateCellValue(),
                                currentRow.getCell(2).getNumericCellValue(), staff);
                        logerDataList.add(logerData);
                    }
                }
                switch (i) {
                    case 1:
                        cTask.setTaskdata1(String.valueOf(counter + pointer));
                        System.out.println(counter + " new values from elogger data 1");
                        break;
                    case 2:
                        cTask.setTaskdata2(String.valueOf(counter + pointer));
                        System.out.println(counter + " new valued from elogger data 2");
                        break;
                    case 3:
                        cTask.setTaskdata3(String.valueOf(counter + pointer));
                        System.out.println(counter + " new valued from elogger data 3");
                        break;
                    default:
                        cTask.setTaskdata4(String.valueOf(counter + pointer));
                        System.out.println(counter + " new valued from elogger data 4");
                        break;
                }

                workbook.close();
            }

            System.out.println("Total entries from all loggers=" + logerDataList.size());
            Collections.sort(logerDataList);
            updateAttendance(logerDataList);
        } catch (FileNotFoundException e) {
            goError(e);
            throw e;
        } catch (IOException e) {
            goError(e);
            throw e;
        } catch (Exception e) {
            goError(e);
            throw e;
        }
    }

    private void updateAttendance(List<LoggerData> loggerDataList) throws Exception {
        String currentDate = "";
        String previousDate = "";
        for (LoggerData loggerData : loggerDataList) {
            currentDate = FormatUtils.formatDate(loggerData.getDateTime(), FormatUtils.TIMESTAMPDATEPATTERN);
            previousDate = FormatUtils.formatDate(FormatUtils.minusOneDay(loggerData.getDateTime()), FormatUtils.TIMESTAMPDATEPATTERN);

            List<Attendance> temp = schedulerDAO.findOpenAttendance(loggerData.getStaff(), Timestamp.valueOf(previousDate + " 00:00:00"), Timestamp.valueOf(currentDate + " 23:59:59"));
            if (temp.size() > 0) {
                Attendance attendance = temp.get(0);
                if (FormatUtils.getDateDiff(attendance.getEntrance(), loggerData.getDateTime(), TimeUnit.HOURS) > 16) {
                    Attendance newAttendance = new Attendance();
                    newAttendance.setCompany(loggerData.getStaff().getCompany());
                    newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                    newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                    newAttendance.setEnded(BigDecimal.ZERO);
                    newAttendance.setStaff(loggerData.getStaff());
                    newAttendance.setSector(loggerData.getStaff().getSector());
                    schedulerDAO.saveAttendance(newAttendance);
                } else {
                    attendance.setEnded(BigDecimal.ONE);
                    attendance.setExit(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                    schedulerDAO.updateAttendance(attendance);
                }
            } else {
                Attendance newAttendance = new Attendance();
                newAttendance.setCompany(loggerData.getStaff().getCompany());
                newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                newAttendance.setEnded(BigDecimal.ZERO);
                newAttendance.setStaff(loggerData.getStaff());
                newAttendance.setSector(loggerData.getStaff().getSector());
                schedulerDAO.saveAttendance(newAttendance);
            }
        }
    }

    public void goError(Exception ex) {
        logger.error("-----------AN ERROR HAPPENED ON RETRIEVE LOGGER DATA SCHEDULER !!!! -------------------- : " + ex.toString());
        logger.error("Cause=" + ex.getCause());
        logger.error("Class=" + ex.getClass());
        logger.error("Message=" + ex.getLocalizedMessage());
        logger.error(ex, ex);
        logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");
    }

}
