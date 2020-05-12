package erp.scheduler;

import erp.bean.LoggerData;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    PersistenceHelper persistenceHelper;

    final String DATEPATTERN = "yyyy-MM-dd";
    final String TIMEPATTERN = "HH:mm:ss";
    final String FULLDATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Lock(LockType.READ)
    public void doSchedulerWork() throws InterruptedException {        
        Company company = (Company) persistenceHelper.find(Company.class, Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_READ_LOGGERS")));        
        Scheduletask task = (Scheduletask) persistenceHelper.find(Scheduletask.class, Long.parseLong(SystemParameters.getInstance().getProperty("DEFAULT_COMPANY_ID")));
        Companytask cTask = schedulerDAO.findCtask(company, task); 
        Scheduletaskdetail taskDetails = null;
        if (!busy.compareAndSet(false, true) || cTask.getActive() == BigDecimal.ZERO || cTask.getTaskstatus().getStatusid()!= Long.parseLong(SystemParameters.getInstance().getProperty("TASK_IDLE"))) {
            return;
        }
        try {
            System.out.println("START TASK EXECUTION EXECUTION!!!!!!!!!!!!!!!!");
            cTask.setLastexecutiontime(FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            schedulerDAO.setTaskStatus(cTask, Long.parseLong(SystemParameters.getInstance().getProperty("TASK_ONPROGRESS")));            
            taskDetails = new Scheduletaskdetail();
            taskDetails.setCompanytask(cTask);
            
            
            
            taskMainBody(cTask);
            
            //Thread.sleep(10000L);
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            schedulerDAO.setTaskStatus(cTask, Long.parseLong(SystemParameters.getInstance().getProperty("TASK_IDLE")));
            busy.set(false);
        }
    }

    public void taskMainBody(Companytask cTask) throws Exception {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            System.out.println(dateFormat.format(calendar.getTime()));

            File file1 = new File();//new File(SystemParameters.getInstance().getProperty("LOGGER1_PATH"));

            //CHANGE
            //File file2 = new File(SystemParameters.getInstance().getProperty("LOGGER2_PATH"));
            //File file3 = new File(SystemParameters.getInstance().getProperty("LOGGER3_PATH"));
            //File file4 = new File(SystemParameters.getInstance().getProperty("LOGGER4_PATH"));
            File file = null;
            List<LoggerData> logerDataList = new ArrayList<LoggerData>();
            for (int i = 1; i <= 1; i++) { //CHANGE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (i == 1) {
                    file = file1;
                }
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
                System.out.println(allStaff.size());
                while (iterator.hasNext()) {
                    currentRow = iterator.next();
                    String lc = String.valueOf((int)currentRow.getCell(0).getNumericCellValue());                  
                    staff = allStaff.stream().filter(stf -> lc
                            .equals(stf.getLoggercode()))
                            .findAny()
                            .orElse(null);

                    //System.out.println("staf=" + staff);
                    if (staff != null) {
                        LoggerData logerData = new LoggerData(currentRow.getCell(0).getNumericCellValue(),
                                currentRow.getCell(1).getDateCellValue(),
                                currentRow.getCell(2).getNumericCellValue(), staff);
                        logerDataList.add(logerData);
                    }
                }
                workbook.close();
            }

            System.out.println(logerDataList.size());
            Collections.sort(logerDataList);
            updateAttendance(logerDataList);
            calendar = Calendar.getInstance();
            System.out.println(dateFormat.format(calendar.getTime()));

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
        int counter = 0;
        for (LoggerData loggerData : loggerDataList) {
            //if (counter>100) break;
            currentDate = FormatUtils.formatDate(loggerData.getDateTime(),FormatUtils.TIMESTAMPDATEPATTERN);
            previousDate = FormatUtils.formatDate(FormatUtils.minusOneDay(loggerData.getDateTime()), FormatUtils.TIMESTAMPDATEPATTERN);
            
            System.out.println(currentDate+" "+previousDate);
           
            List<Attendance> temp = schedulerDAO.findAttendance(loggerData.getStaff(), Timestamp.valueOf(previousDate+" 00:00:00"),  Timestamp.valueOf(currentDate+" 23:59:59"));
            System.out.println(temp);
            if (temp.size()>0) {
                for (Attendance attendance : temp) {
                    if (attendance.getEnded().intValue() == 0) {
                        if (FormatUtils.getDateDiff(attendance.getEntrance(), loggerData.getDateTime(), TimeUnit.HOURS) > 16) {
                            //System.out.println("ADD NEW ATTENDANCE");
                            Attendance newAttendance = new Attendance();
                            newAttendance.setCompany(loggerData.getStaff().getCompany());
                            newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                            newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                            newAttendance.setEnded(BigDecimal.ZERO);
                            newAttendance.setStaff(loggerData.getStaff());
                            newAttendance.setSector(loggerData.getStaff().getSector());
                            schedulerDAO.saveAttendance(newAttendance);
                        } else {
                            System.out.println("UPDATE ATTENDANCE");
                            attendance.setEnded(BigDecimal.ONE);
                            attendance.setExit(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                            schedulerDAO.updateAttendance(attendance);
                        }
                    }
                }
            } else {
                //System.out.println("ADD NEW ATTENDANCE");
                Attendance newAttendance = new Attendance();
                newAttendance.setCompany(loggerData.getStaff().getCompany());
                newAttendance.setEntrance(FormatUtils.formatDateToTimestamp(loggerData.getDateTime(), FULLDATEPATTERN));
                newAttendance.setDepartment(loggerData.getStaff().getDepartment());
                newAttendance.setEnded(BigDecimal.ZERO);
                newAttendance.setStaff(loggerData.getStaff());
                newAttendance.setSector(loggerData.getStaff().getSector());
                schedulerDAO.saveAttendance(newAttendance);
            }
            counter++;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            System.out.println(dateFormat.format(calendar.getTime()));
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
