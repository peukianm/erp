/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.scheduler;

import erp.dao.CompanyDAO;
import erp.dao.SchedulerDAO;
import erp.dao.StaffDAO;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Staff;
import erp.entities.Taskstatus;
import erp.util.ErpUtil;
import erp.util.FormatUtils;
import erp.util.SystemParameters;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
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
    StaffDAO staffDAO;

    @EJB
    CompanyDAO companyDAO;

    Taskstatus taskStatus;

    final String DATEPATTERN = "yyyy-MM-dd";
    final String TIMEPATTERN = "HH:mm:ss";
    final String FULLDATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Lock(LockType.READ)
    public int doSchedulerWork(Boolean force) throws InterruptedException {
        Company company = companyDAO.getCompany(Long.parseLong(SystemParameters.getInstance().getProperty("DEFAULT_COMPANY_ID")));   //(Company) persistenceHelper.find(Company.class, Long.parseLong(SystemParameters.getInstance().getProperty("DEFAULT_COMPANY_ID")));
        Scheduletask task = schedulerDAO.getScheduleTask(Long.parseLong(SystemParameters.getInstance().getProperty("SCHEDULE_TASK_UPDATE_STAFF")));
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
        System.out.println("START Update Staff Task");
        Connection conn = null;
        try {
            conn = ErpUtil.getOracleConnection(SystemParameters.getInstance().getProperty("cteamDB_host"),
                    SystemParameters.getInstance().getProperty("cteamDB_port"), SystemParameters.getInstance().getProperty("cteamDB_SID"),
                    SystemParameters.getInstance().getProperty("cteamDB_username"), SystemParameters.getInstance().getProperty("cteamDB_password"));

            String sqlSelect = " SELECT amy, first_name as name, last_name as surname, EM.FATHER_NAME as fathername, rolos_id as rankid, "
                    + " section_id as departmentid,  hoursperday as shiftid, EM.MOBILE,"
                    + " branch_id as branchid, adt,specialty_id as specialityid, afm, street as address,  "
                    + " work_phone as phone1,  home_phone as phone2,  "
                    + " birth_date as birthdate,employee_id as cteamid, active as enable,EM.AMKA as amka,"
                    + " studytype_id as studytypeid,familystatus_id as familystatusid,service_id as sectorid,"
                    + " bigsection_id as companyid, MISTHACTIVE "
                    + " from SYSPROS.EMP_EMPLOYEES em "
                    + " where em.active=1 AND TODATE >= current_date AND EM.MISTHACTIVE=1 "
                    + " order by last_name";

            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sqlSelect);

            Staff updateStaff;
            List<Staff> staffs;
            int index = 0;
            int update = 0;
            int insert = 0;
            List<String> amies = new ArrayList<>(0);
            while (rset.next()) {                
                index++;
                amies.add(rset.getString(1));
                
                staffs = staffDAO.findByProperty("amy", rset.getString(1));
                if (staffs.size() > 0) {
                    update++;
                    updateStaff = staffs.get(0);

                    updateStaff.setName(rset.getString("name"));
                    updateStaff.setSurname(rset.getString("surname"));
                    updateStaff.setFathername(rset.getString("fathername"));
                    updateStaff.setBirthdate(rset.getDate("birthdate"));
                    updateStaff.setAmka(rset.getString("amka"));
                    updateStaff.setAfm(rset.getString("afm"));
                    updateStaff.setAdt(rset.getString("adt"));
                    updateStaff.setPhone1(rset.getString("phone1"));
                    updateStaff.setPhone2(rset.getString("phone2"));
                    updateStaff.setMobile(rset.getString("mobile"));
                    updateStaff.setAddress(rset.getString("address"));
                    updateStaff.setCteamid(rset.getString("cteamid"));
                    updateStaff.setActive(BigDecimal.ONE);

                    if (rset.getString("departmentid") != null && !rset.getString("departmentid").equals("")) {
                        updateStaff.setDepartment(staffDAO.getDepartment(Long.parseLong(rset.getString("departmentid"))));
                    }

                    if (rset.getString("sectorid") != null && !rset.getString("sectorid").equals("")) {
                        updateStaff.setSector(staffDAO.getSector(Long.parseLong(rset.getString("sectorid"))));
                    }

                    if (rset.getString("specialityid") != null && !rset.getString("specialityid").equals("")) {
                        updateStaff.setSpeciality(staffDAO.getSpeciality(Long.parseLong(rset.getString("specialityid"))));
                    }

                    if (rset.getString("studytypeid") != null && !rset.getString("studytypeid").equals("")) {
                        updateStaff.setStudytype(staffDAO.getStudytype(Long.parseLong(rset.getString("studytypeid"))));
                    }

                    if (rset.getString("familystatusid") != null && !rset.getString("familystatusid").equals("")) {
                        updateStaff.setFamilystatus(staffDAO.getFamilyStatus(Long.parseLong(rset.getString("familystatusid"))));
                    }

                    if (rset.getString("branchid") != null && !rset.getString("branchid").equals("")) {
                        updateStaff.setBranch(staffDAO.getBranch(Long.parseLong(rset.getString("branchid"))));
                    }

                    updateStaff.setCompany(staffDAO.getCompany(1));

                    if (rset.getString("rankid") != null && !rset.getString("rankid").equals("")) {
                        updateStaff.setEmprank(staffDAO.getRank(Long.parseLong(rset.getString("rankid"))));
                    }

                    if (rset.getString("shiftid") != null && !rset.getString("shiftid").equals("")) {
                        updateStaff.setWorkshift(staffDAO.getShift(Long.parseLong(rset.getString("shiftid"))));
                    }
                    staffDAO.update(updateStaff);

                } else {
                    insert++;
                    Staff newStaff = new Staff();
                    newStaff.setName(rset.getString("name"));
                    newStaff.setSurname(rset.getString("surname"));
                    newStaff.setFathername(rset.getString("fathername"));
                    newStaff.setBirthdate(rset.getDate("birthdate"));
                    newStaff.setAmka(rset.getString("amka"));
                    newStaff.setAfm(rset.getString("afm"));
                    newStaff.setAdt(rset.getString("adt"));
                    newStaff.setPhone1(rset.getString("phone1"));
                    newStaff.setPhone2(rset.getString("phone2"));
                    newStaff.setMobile(rset.getString("mobile"));
                    newStaff.setAddress(rset.getString("address"));
                    newStaff.setCteamid(rset.getString("cteamid"));
                    newStaff.setAmy(rset.getString("amy"));
                    newStaff.setActive(BigDecimal.ONE);

                    if (rset.getString("departmentid") != null && !rset.getString("departmentid").equals("")) {
                        newStaff.setDepartment(staffDAO.getDepartment(Long.parseLong(rset.getString("departmentid"))));
                    }

                    if (rset.getString("sectorid") != null && !rset.getString("sectorid").equals("")) {
                        newStaff.setSector(staffDAO.getSector(Long.parseLong(rset.getString("sectorid"))));
                    }

                    if (rset.getString("specialityid") != null && !rset.getString("specialityid").equals("")) {
                        newStaff.setSpeciality(staffDAO.getSpeciality(Long.parseLong(rset.getString("specialityid"))));
                    }

                    if (rset.getString("studytypeid") != null && !rset.getString("studytypeid").equals("")) {
                        newStaff.setStudytype(staffDAO.getStudytype(Long.parseLong(rset.getString("studytypeid"))));
                    }

                    if (rset.getString("familystatusid") != null && !rset.getString("familystatusid").equals("")) {
                        newStaff.setFamilystatus(staffDAO.getFamilyStatus(Long.parseLong(rset.getString("familystatusid"))));
                    }

                    if (rset.getString("branchid") != null && !rset.getString("branchid").equals("")) {
                        newStaff.setBranch(staffDAO.getBranch(Long.parseLong(rset.getString("branchid"))));
                    }

                    newStaff.setCompany(staffDAO.getCompany(1));

                    if (rset.getString("rankid") != null && !rset.getString("rankid").equals("")) {
                        newStaff.setEmprank(staffDAO.getRank(Long.parseLong(rset.getString("rankid"))));
                    }

                    if (rset.getString("shiftid") != null && !rset.getString("shiftid").equals("")) {
                        newStaff.setWorkshift(staffDAO.getShift(Long.parseLong(rset.getString("shiftid"))));
                    }

                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!INSERTING staff=" + newStaff.getSurname() + " " + newStaff.getAmy());
                    staffDAO.save(newStaff);
                }
            }
            System.out.println("index=" + index + " insert=" + insert + " updata=" + update);
            
            List<Staff> allStaff = staffDAO.getAllStaff(true);                     
            int disabled=0;
            for (int i = 0; i < allStaff.size(); i++) {
                Staff staff = allStaff.get(i);                                        
                if (!amies.contains(staff.getAmy())) {
                    disabled++;
                    System.out.println("DISABLING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!="+staff.getSurname());
                    staff.setActive(BigDecimal.ZERO);
                    staffDAO.update(staff);
                }                
            }
            System.out.println("Update Staff: All entries="+index+" updated="+update+" inserted="+insert+" disabled="+disabled);
        } catch (Exception e) {
            goError(e);
            throw e;
        } finally {
            System.out.println("Closing connection!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            conn.close();
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

//                String sqlUpdate = "update staff  set "
//                        + " rankid = " + rset.getString("rankid")
//                        + " , departmentid =" + rset.getString("departmentid")
//                        + " , shiftid=" + rset.getString("shiftid")
//                        + " , branchid=" + rset.getString("branchid")
//                        + " , specialityid=" + rset.getString("specialityid")
//                        + " , studytypeid=" + rset.getString("studytypeid")
//                        + " , familystatusid=" + rset.getString("familystatusid")
//                        + " , sectorid=" + rset.getString("sectorid")
//                        + " , mobile='" + rset.getString("mobile") + "' "
//                        + " , address='" + rset.getString("address") + "' "
//                        + " , phone1='" + rset.getString("phone1") + "' "
//                        + " , phone2='" + rset.getString("phone2") + "' "
//                        + " , amka='" + rset.getString("amka") + "' "
//                        + " , adt='" + rset.getString("adt") + "' "
//                        + " , afm='" + rset.getString("afm") + "' "
//                        + " , fathername='" + rset.getString("fathername") + "' "
//                        + " , birthdate=" + rset.getString("birthdate")
//                        + " , name='" + rset.getString("name") + "' "
//                        + " , surname='" + rset.getString("surname") + "' "
//                        + " where amy='" + rset.getString("amy") + "'";
//                int code = stmt.executeUpdate(sqlUpdate);
//                if (code == 0) {
//                    Staff newStaff = new Staff();
//                    newStaff.setName(rset.getString("name"));
//                    newStaff.setSurname(rset.getString("surname"));
//                    newStaff.setFathername(rset.getString("fathername"));
//                    newStaff.setBirthdate(rset.getDate("birthdate"));
//                    newStaff.setAmka(rset.getString("amka"));
//                    newStaff.setAfm(rset.getString("afm"));
//                    newStaff.setAdt(rset.getString("adt"));
//                    newStaff.setPhone1(rset.getString("phone1"));
//                    newStaff.setPhone2(rset.getString("phone2"));
//                    newStaff.setMobile(rset.getString("mobile"));
//                    newStaff.setAddress(rset.getString("address"));
//                    newStaff.setCteamid(rset.getString("cteamid"));
//                    newStaff.setAmy(rset.getString("amy"));
//                    newStaff.setActive(BigDecimal.ONE);
//                    
//                    newStaff.setDepartment(staffDAO.getDepartment(Long.parseLong(rset.getString("departmentid"))));
//                    newStaff.setSector(staffDAO.getSector(Long.parseLong(rset.getString("sectorid"))));
//                    newStaff.setSpeciality(staffDAO.getSpeciality(Long.parseLong(rset.getString("specialityid"))));
//                    newStaff.setStudytype(staffDAO.getStudytype(Long.parseLong(rset.getString("studytypeid"))));
//                    newStaff.setFamilystatus(staffDAO.getFamilyStatus(Long.parseLong(rset.getString("familystatusid"))));
//                    newStaff.setBranch(staffDAO.getBranch(Long.parseLong(rset.getString("branchid"))));
//                    newStaff.setCompany(staffDAO.getCompany(1));
//                    newStaff.setEmprank(staffDAO.getRank(Long.parseLong(rset.getString("rankid"))));
//                    newStaff.setWorkshift(staffDAO.getShift(Long.parseLong(rset.getString("shiftid"))));
//                }
