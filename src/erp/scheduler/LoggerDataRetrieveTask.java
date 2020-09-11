package erp.scheduler;

import erp.bean.LoggerData;
import erp.dao.AttendanceDAO;
import erp.dao.CompanyDAO;
import erp.dao.SchedulerDAO;
import erp.dao.StaffDAO;
import erp.entities.Attendance;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Staff;
import erp.entities.Taskstatus;
import erp.util.FormatUtils;
import erp.util.SystemParameters;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ejb.*;
import javax.ejb.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    StaffDAO staffDAO;

    @EJB
    AttendanceDAO attendanceDAO;

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
        List<LoggerData> logerDataList = new ArrayList<LoggerData>();
        String currentDate = new SimpleDateFormat(FormatUtils.yyyyMMdd).format(new Date());

        int pointer = cTask.getTaskdata2() == null ? 0 : Integer.parseInt(cTask.getTaskdata2());
        String dbDate = cTask.getTaskdata1() == null ? currentDate : cTask.getTaskdata1();
        System.out.println("INITIAL POINTER=" + pointer + " and DATABASE DATE=" + dbDate+"\n");

        if (!dbDate.equals(currentDate)) {

            long days = FormatUtils.getDateDiff(FormatUtils.getDate(dbDate, FormatUtils.yyyyMMdd), new Date(), TimeUnit.DAYS);
            for (int i = 0; i <= days - 1; i++) {
                Date tempDate = FormatUtils.addDaysDate(dbDate, i, FormatUtils.yyyyMMdd);
                String tempDateString = FormatUtils.formatDate(tempDate, FormatUtils.yyyyMMdd);
                System.out.println("------------------------------------------------------------------------------------");
                System.out.println("Proccessing File " + tempDateString + ".txt");
                cTask.setTaskdata1(tempDateString);
                try (BufferedReader br = new BufferedReader(new FileReader(SystemParameters.getInstance().getProperty("LOGGER_PATH") + "\\" + tempDateString + ".txt"))) {
                    Staff staff = null;
                    List<Staff> allStaff = staffDAO.getAllStaff(true);

                    int counter = 0;
                    if (tempDateString.equals(dbDate)) {
                        for (int j = 0; j <= pointer - 1; ++j) {
                            counter++;
                            br.readLine();
                        }
                    }

                    for (String line; (line = br.readLine()) != null;) {
                        counter++;
                        String[] parts = line.split("\\t");
                        String afm = parts[0];
                        staff = allStaff.stream().filter(stf -> afm
                                .equals("1" + stf.getAfm()))
                                .findAny()
                                .orElse(null);

                        if (staff != null) {
                            LoggerData logerData = new LoggerData(parts[0],
                                    FormatUtils.getDate(parts[1], FormatUtils.LOGGERFULLDATEPATTERN),
                                    parts[2], staff);
                            logerDataList.add(logerData);
                        } else {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SOS!!! No staff found for AFM=" + afm);
                        }
                    }
                    cTask.setTaskdata1(tempDateString);
                    cTask.setTaskdata2(String.valueOf(counter));
                    
                    System.out.println("For Date=" + tempDateString + " counter=" + counter + " (File pointer)");
                    System.out.println("Until Date=" + tempDateString + " LoggerDataList size=" + logerDataList.size());
                    System.out.println("------------------------------------------------------------------------------------\n");
                } catch (Exception ex) {
                    System.out.println("FILE NOT FOUND=" + tempDateString + " date");
                }
            }
        }

        //ΦΟΡΤΩΣΗ ΚΑΤΑΓΡΑΦΕΩΝ ΣΗΜΕΡΙΝΗΣ ΗΜΕΡΟΜΗΝΙΑ
        try (BufferedReader br = new BufferedReader(new FileReader(SystemParameters.getInstance().getProperty("LOGGER_PATH") + "\\" + currentDate + ".txt"))) {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("/////////////Proccessing file for today date " + currentDate + ".txt //////////////////////");
            int counter = 0;
            Staff staff = null;
            List<Staff> allStaff = staffDAO.getAllStaff(true);
            System.out.println("Pointer inside today date=" + pointer);
            if (currentDate.equals(dbDate)) {
                for (int i = 0; i <= pointer - 1; ++i) {
                    counter++;
                    br.readLine();
                }
            }

            for (String line; (line = br.readLine()) != null;) {
                counter++;
                String[] parts = line.split("\\t");
                String afm = parts[0];
                staff = allStaff.stream().filter(stf -> afm
                        .equals("1" + stf.getAfm()))
                        .findAny()
                        .orElse(null);

                if (staff != null) {
                    LoggerData logerData = new LoggerData(parts[0],
                            FormatUtils.getDate(parts[1], FormatUtils.LOGGERFULLDATEPATTERN),
                            parts[2], staff);
                    logerDataList.add(logerData);
                } else {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SOS!!! No staff found for AFM=" + afm);
                }
            }

            cTask.setTaskdata2(String.valueOf(counter));
            cTask.setTaskdata1(currentDate);
            System.out.println("Pointer inside today date after parsing the file=" + counter);
            System.out.println("Total entries from all loggers (logerDataList.size())=" + logerDataList.size());
            System.out.println("------------------------------------------------------------------------------------\n");

        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND FOR CURRENT DATE=" + currentDate);
        } catch (IOException e) {
            goError(e);
            throw e;
        } catch (Exception e) {
            goError(e);
            throw e;
        }

        if (logerDataList.size() > 0) {
            try {
                Collections.sort(logerDataList);
                updateAttendance(logerDataList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateAttendance(List<LoggerData> loggerDataList) throws Exception {
        String currentDate = "";
        String previousDate = "";
        Timestamp minusFiveMins;
        Timestamp plusFiveMins;
        int removed = 0;
        int newEntry = 0;
        int closesdEntry = 0;
        long delay = 5 * 60 * 1000;
        for (LoggerData loggerData : loggerDataList) {
            minusFiveMins = new Timestamp(loggerData.getDateTime().getTime() - delay);
            plusFiveMins = new Timestamp(loggerData.getDateTime().getTime() + delay);

            if (attendanceDAO.findLoggerHitsFromUser(loggerData.getStaff(), minusFiveMins, plusFiveMins)) {
                removed++;
                //System.out.println("!!!!!!!!!!!!!!!!REMOVING ENTRY FROM FILE BECAUSE OF CONTINUE HIT!!!!!!!!!!!!!!!! " + loggerData.getStaff().getSurname() + " " + loggerData.getStaff().getName() + " DATE=" + loggerData.getDateTime());
                continue;
            }

            currentDate = FormatUtils.formatDate(loggerData.getDateTime(), FormatUtils.TIMESTAMPDATEPATTERN);
            previousDate = FormatUtils.formatDate(FormatUtils.minusOneDay(loggerData.getDateTime()), FormatUtils.TIMESTAMPDATEPATTERN);
            List<Attendance> temp = attendanceDAO.findOpenAttendance(loggerData.getStaff(), Timestamp.valueOf(previousDate + " 00:00:00"), Timestamp.valueOf(currentDate + " 23:59:59"));

            if (temp.size() > 0) {
                Attendance attendance = temp.get(0);
                if (FormatUtils.getDateDiff(attendance.getEntrance(), loggerData.getDateTime(), TimeUnit.HOURS) > 12) {
                    newEntry++;
                    //System.out.println("!!!!!!!!!!Attendanse " + attendance.getEntrance() + " for staff " + attendance.getStaff().getSurname() + " " + attendance.getStaff().getName() + " will remain open!!!!!!!!!!!!!!!!!!");
                    Attendance newAttendance = new Attendance();
                    newAttendance.setCompany(loggerData.getStaff().getCompany());
                    newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                    newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                    newAttendance.setEnded(BigDecimal.ZERO);
                    newAttendance.setStaff(loggerData.getStaff());
                    newAttendance.setSector(loggerData.getStaff().getSector());
                    attendanceDAO.saveAttendance(newAttendance);
                } else {
                    if (FormatUtils.getDateDiff(attendance.getEntrance(), loggerData.getDateTime(), TimeUnit.MINUTES) > 5) {
                        closesdEntry++;
                        attendance.setEnded(BigDecimal.ONE);
                        attendance.setExit(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                        attendanceDAO.updateAttendance(attendance);
                    } else {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!SOS ENTRY NOT INSERTED BECAUSE AN UNKNOWN REASON " + loggerData.getStaff().getSurname() + " " + loggerData.getStaff().getName() + " "
                                + " entry " + loggerData.getDateTime());
                    }
                }
            } else {
                newEntry++;
                Attendance newAttendance = new Attendance();
                newAttendance.setCompany(loggerData.getStaff().getCompany());
                newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                newAttendance.setEnded(BigDecimal.ZERO);
                newAttendance.setStaff(loggerData.getStaff());
                newAttendance.setSector(loggerData.getStaff().getSector());
                attendanceDAO.saveAttendance(newAttendance);
            }
        }
        System.out.println("----------------------------------------------------------");
        System.out.println("Update Attendance Summary:");
        System.out.println("LoggerDataList size=" + loggerDataList.size());
        System.out.println("New Entries=" + newEntry);
        System.out.println("Closed Entries=" + closesdEntry);
        System.out.println("Removed Entries=" + removed);
        System.out.println("------------------------------------------------------------");
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
