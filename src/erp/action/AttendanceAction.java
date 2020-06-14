package erp.action;

import erp.bean.AttendanceBean;
import erp.bean.DashboardAttendance;
import erp.bean.DashboardStatistics;
import erp.bean.SessionBean;
import erp.dao.AttendanceDAO;
import erp.dao.AuditingDAO;
import erp.dao.CompanyDAO;
import erp.dao.StaffDAO;
import erp.entities.Attendance;
import erp.entities.Staff;
import erp.exception.ERPCustomException;
import erp.util.FormatUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
public class AttendanceAction {

    private static final Logger logger = LogManager.getLogger(AttendanceAction.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    AuditingDAO auditingDAO;

    @Inject
    AttendanceDAO attendanceDAO;

    @Inject
    StaffDAO staffDAO;

    @Inject
    CompanyDAO companyDAO;

    @Inject
    DashboardAttendance dbAttendance;

    @Inject
    DashboardStatistics dbStat;

    public void fetchAttendances() throws ERPCustomException {
        try {
            List<AttendanceBean> retValue = new ArrayList<>(0);
            if (!dbAttendance.getSelectedSectors().isEmpty()) {
                dbAttendance.getSelectedSectors().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbAttendance.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbAttendance.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, temp, null, false);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbAttendance.getSelectedDepartments().isEmpty()) {
                dbAttendance.getSelectedDepartments().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbAttendance.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbAttendance.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, null, temp, false);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbAttendance.getSelectedStaff().isEmpty()) {
                dbAttendance.getSelectedStaff().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbAttendance.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbAttendance.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), temp, null, null, false);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });
            }
            dbAttendance.setAttendances(retValue);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Fetch Addetances Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void fetchAttendancesStatistics() throws ERPCustomException {
        try {
            List<AttendanceBean> retValue = new ArrayList<>(0);
            if (!dbStat.getSelectedSectors().isEmpty()) {
                dbStat.getSelectedSectors().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbStat.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbStat.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, temp, null, true);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setStaff(temp1.getStaff());
                        bean.setStaffID(temp1.getStaff().getStaffid());
                        bean.setDepartmentID(temp1.getStaff().getDepartment().getDepartmentid());
                        bean.setSectorID(temp1.getStaff().getSector().getSectorid());
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                            long secs = FormatUtils.getDateDiff(temp1.getEntrance(), temp1.getExit(), TimeUnit.SECONDS);
                            bean.setSecondsDuration(secs);
                            bean.setDuration(FormatUtils.splitSecondsToTime(secs));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbStat.getSelectedDepartments().isEmpty()) {
                dbStat.getSelectedDepartments().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbStat.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbStat.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, null, temp, true);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setStaff(temp1.getStaff());
                        bean.setStaffID(temp1.getStaff().getStaffid());
                        bean.setDepartmentID(temp1.getStaff().getDepartment().getDepartmentid());
                        bean.setSectorID(temp1.getStaff().getSector().getSectorid());
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                            long secs = FormatUtils.getDateDiff(temp1.getEntrance(), temp1.getExit(), TimeUnit.SECONDS);
                            bean.setSecondsDuration(secs);
                            bean.setDuration(FormatUtils.splitSecondsToTime(secs));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbStat.getSelectedStaff().isEmpty()) {
                dbStat.getSelectedStaff().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbStat.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbStat.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), temp, null, null, true);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setStaff(temp1.getStaff());
                        bean.setStaffID(temp1.getStaff().getStaffid());
                        bean.setDepartmentID(temp1.getStaff().getDepartment().getDepartmentid());
                        bean.setSectorID(temp1.getStaff().getSector().getSectorid());
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                            long secs = FormatUtils.getDateDiff(temp1.getEntrance(), temp1.getExit(), TimeUnit.SECONDS);
                            bean.setSecondsDuration(secs);
                            bean.setDuration(FormatUtils.splitSecondsToTime(secs));
                        }
                        retValue.add(bean);
                    });
                });
            } else if (dbStat.isAll()) {

                List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbStat.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                        FormatUtils.formatDate(dbStat.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, null, null, true);
                attendances.forEach((temp1) -> {
                    AttendanceBean bean = new AttendanceBean();
                    bean.setStaff(temp1.getStaff());
                    bean.setStaffID(temp1.getStaff().getStaffid());
                    bean.setDepartmentID(temp1.getStaff().getDepartment().getDepartmentid());
                    bean.setSectorID(temp1.getStaff().getSector().getSectorid());
                    bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                    bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                    bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                    if (temp1.getExit() != null) {
                        bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        long secs = FormatUtils.getDateDiff(temp1.getEntrance(), temp1.getExit(), TimeUnit.SECONDS);
                        bean.setSecondsDuration(secs);
                        bean.setDuration(FormatUtils.splitSecondsToTime(secs));
                    }
                    retValue.add(bean);
                });

            }
            dbStat.setAttendances(retValue);
            dbStat.setStatData(new ArrayList<>(0));
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Fetch Addetance Statistics Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void statPerStaff() throws ERPCustomException {
        try {
            List<AttendanceBean> attendances = dbStat.getAttendances();
            attendances.sort((m1, m2) -> {
                return m1.getName().compareTo(m2.getName());
            });

            Map<Long, List<AttendanceBean>> dateMap = attendances.stream().collect(Collectors.groupingBy(AttendanceBean::getStaffID));
            LongSummaryStatistics statistics;
            List<AttendanceBean> retValue = new ArrayList<>(0);
            for (Map.Entry<Long, List<AttendanceBean>> entry : dateMap.entrySet()) {
                statistics = entry.getValue().stream().collect(Collectors.summarizingLong(AttendanceBean::getSecondsDuration));
                AttendanceBean ab = new AttendanceBean();
                Staff staff = staffDAO.getStaff(entry.getKey());
                //ab.setStaff(staff);
                ab.setName(staff.getSurname() + " " + staff.getName());
                ab.setDepartment(staff.getDepartment());
                ab.setSector(staff.getSector());
                ab.setMax(FormatUtils.splitSecondsToTime(statistics.getMax()));
                ab.setMin(FormatUtils.splitSecondsToTime(statistics.getMin()));
                ab.setCount(String.valueOf(statistics.getCount()));
                ab.setAverage(FormatUtils.splitSecondsToTime((long) statistics.getAverage()));
                retValue.add(ab);
            }
            dbStat.setStatData(retValue); 
            dbStat.setShowName(true);
            dbStat.setShowDate(false);

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Statistics Per Employee Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void statTotal() throws ERPCustomException {
        try {
            List<AttendanceBean> attendances = dbStat.getAttendances();
            LongSummaryStatistics statistics = attendances.stream().collect(Collectors.summarizingLong(AttendanceBean::getSecondsDuration));

            List<AttendanceBean> retValue = new ArrayList<>(0);
            AttendanceBean ab = new AttendanceBean();
            ab.setMax(FormatUtils.splitSecondsToTime(statistics.getMax()));
            ab.setMin(FormatUtils.splitSecondsToTime(statistics.getMin()));
            ab.setCount(String.valueOf(statistics.getCount()));
            ab.setAverage(FormatUtils.splitSecondsToTime((long) statistics.getAverage()));
            retValue.add(ab);

            dbStat.setStatData(retValue);
            dbStat.setShowName(false);
            dbStat.setShowDate(false);

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Total Statistics Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void statPerDate() throws ERPCustomException {
        try {
            List<AttendanceBean> attendances = dbStat.getAttendances();
            attendances.sort((m1, m2) -> {
                return m1.getDate().compareTo(m2.getDate());
            });

            Map<LocalDate, List<AttendanceBean>> dateMap = attendances.stream().collect(Collectors.groupingBy(AttendanceBean::getDate));
            LongSummaryStatistics statistics;
            List<AttendanceBean> retValue = new ArrayList<>(0);
            for (Map.Entry<LocalDate, List<AttendanceBean>> entry : dateMap.entrySet()) {
                statistics = entry.getValue().stream().collect(Collectors.summarizingLong(AttendanceBean::getSecondsDuration));
                AttendanceBean ab = new AttendanceBean();
                ab.setDate(entry.getKey());
                ab.setMax(FormatUtils.splitSecondsToTime(statistics.getMax()));
                ab.setMin(FormatUtils.splitSecondsToTime(statistics.getMin()));
                ab.setCount(String.valueOf(statistics.getCount()));
                ab.setAverage(FormatUtils.splitSecondsToTime((long) statistics.getAverage()));
                retValue.add(ab);
            }
            dbStat.setStatData(retValue);
            dbStat.setShowName(false);
            dbStat.setShowDate(true);

//            Map<Long, List<AttendanceBean>> staffMap = attendances.stream().collect(Collectors.groupingBy(AttendanceBean::getStaffID));
//            Map<Long, List<AttendanceBean>> departmentMap = attendances.stream().collect(Collectors.groupingBy(AttendanceBean::getDepartmentID));
//            Map<Long, List<AttendanceBean>> sectorMap = attendances.stream().collect(Collectors.groupingBy(AttendanceBean::getSectorID));
//            Map<Long, Map<Long, List<AttendanceBean>>> sectorDepartmentMap = attendances.stream().collect(Collectors.groupingBy(AttendanceBean::getSectorID, Collectors.groupingBy(AttendanceBean::getDepartmentID)));
//            Map<LocalDate, Map<Long, Map<Long, List<AttendanceBean>>>> dateSectionDepartmentMap = attendances.stream().collect(
//                    Collectors.groupingBy(AttendanceBean::getDate, Collectors.groupingBy(AttendanceBean::getSectorID, Collectors.groupingBy(AttendanceBean::getDepartmentID))));
//            for (Map.Entry<Long, List<AttendanceBean>> entry : staffMapOfAttendance.entrySet()) {
//                System.out.println(entry.getKey() + "/" + entry.getValue());
//                statistics = attendances.stream().collect(Collectors.summarizingInt(AttendanceBean::getSecondsDuration));
//                System.out.println("count=" + statistics.getCount());
//
//            }
//            attendances.sort(Comparator.comparing(AttendanceBean::getStaff::surname));
//            attendances.sort((m1, m2) -> {
//                return m1.getStaff().getSurname().compareTo(m2.getStaff().getSurname());
//            });
            //           collectorMapOfLists.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Statistics Per Date", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void statPerDateSection() throws ERPCustomException {
        try {
            List<AttendanceBean> attendances = dbStat.getAttendances();
            attendances.sort((m1, m2) -> {
                return m1.getDate().compareTo(m2.getDate());
            });

            Map<LocalDate, Map<Long, List<AttendanceBean>>> dateMap = attendances.stream().collect(
                    Collectors.groupingBy(AttendanceBean::getDate, Collectors.groupingBy(AttendanceBean::getSectorID)));

            LongSummaryStatistics statistics;
            List<AttendanceBean> retValue = new ArrayList<>(0);
            for (Map.Entry<LocalDate, Map<Long, List<AttendanceBean>>> entry : dateMap.entrySet()) {
                Map<Long, List<AttendanceBean>> tempMap = entry.getValue();
                for (Map.Entry<Long, List<AttendanceBean>> inEntry : tempMap.entrySet()) {
                    statistics = inEntry.getValue().stream().collect(Collectors.summarizingLong(AttendanceBean::getSecondsDuration));
                    AttendanceBean ab = new AttendanceBean();
                    ab.setSector(companyDAO.getSector(inEntry.getKey()));
                    ab.setDate(entry.getKey());
                    ab.setMax(FormatUtils.splitSecondsToTime(statistics.getMax()));
                    ab.setMin(FormatUtils.splitSecondsToTime(statistics.getMin()));
                    ab.setCount(String.valueOf(statistics.getCount()));
                    ab.setAverage(FormatUtils.splitSecondsToTime((long) statistics.getAverage()));
                    retValue.add(ab);
                }
            }
            dbStat.setStatData(retValue);
            dbStat.setShowName(false);
            dbStat.setShowDate(true);

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Statistics Per Date/Section Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void statPerDateSectionDepartment() throws ERPCustomException {
        try {
            List<AttendanceBean> attendances = dbStat.getAttendances();
            attendances.sort((m1, m2) -> {
                return m1.getDate().compareTo(m2.getDate());
            });

            Map<LocalDate, Map<Long, Map<Long, List<AttendanceBean>>>> dateMap = attendances.stream().collect(
                    Collectors.groupingBy(AttendanceBean::getDate, Collectors.groupingBy(AttendanceBean::getSectorID, Collectors.groupingBy(AttendanceBean::getDepartmentID))));

            LongSummaryStatistics statistics;
            List<AttendanceBean> retValue = new ArrayList<>(0);

            for (Map.Entry<LocalDate, Map<Long, Map<Long, List<AttendanceBean>>>> outEntry : dateMap.entrySet()) {
                Map<Long, Map<Long, List<AttendanceBean>>> mp = outEntry.getValue();
                for (Map.Entry<Long, Map<Long, List<AttendanceBean>>> entry : mp.entrySet()) {
                    Map<Long, List<AttendanceBean>> tempMap = entry.getValue();
                    for (Map.Entry<Long, List<AttendanceBean>> inEntry : tempMap.entrySet()) {
                        statistics = inEntry.getValue().stream().collect(Collectors.summarizingLong(AttendanceBean::getSecondsDuration));
                        AttendanceBean ab = new AttendanceBean();
                        ab.setSector(companyDAO.getSector(entry.getKey()));
                        ab.setDepartment(companyDAO.getDepartment(inEntry.getKey()));
                        ab.setDate(outEntry.getKey());
                        ab.setMax(FormatUtils.splitSecondsToTime(statistics.getMax()));
                        ab.setMin(FormatUtils.splitSecondsToTime(statistics.getMin()));
                        ab.setCount(String.valueOf(statistics.getCount()));
                        ab.setAverage(FormatUtils.splitSecondsToTime((long) statistics.getAverage()));
                        retValue.add(ab);
                    }
                }
            }
            dbStat.setStatData(retValue);
            dbStat.setShowName(false);
            dbStat.setShowDate(true);

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Statistics Per Date/Section/Department Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

//    public void goError(Exception ex) {
//        try {
//            logger.error("-----------AN ERROR HAPPENED !!!! -------------------- : " + ex.toString());
//            if (sessionBean.getUsers() != null) {
//                logger.error("User=" + sessionBean.getUsers().getUsername());
//            }
//            logger.error("Cause=" + ex.getCause());
//            logger.error("Class=" + ex.getClass());
//            logger.error("Message=" + ex.getLocalizedMessage());
//            logger.error(ex, ex);
//            logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");
//
//            ErrorBean errorBean = (ErrorBean) FacesUtils.getManagedBean("errorBean");
//            errorBean.reset();
//            errorBean.setErrorMSG(MessageBundleLoader.getMessage(sessionBean.getErrorMsgKey()));
//            //FacesUtils.redirectAJAX("./templates/error.jsf?faces-redirect=true");
//            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/common/error.jsf?faces-redirect=true");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
