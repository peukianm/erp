/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.action;

import erp.bean.AttendanceBean;
import erp.bean.DashboardAttendance;
import erp.bean.ErrorBean;
import erp.bean.SessionBean;
import erp.dao.AttendanceDAO;
import erp.dao.AuditingDAO;
import erp.dao.StaffDAO;
import erp.entities.Attendance;
import erp.util.FacesUtils;
import erp.util.FormatUtils;
import erp.util.MessageBundleLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
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

    @EJB
    AuditingDAO auditingDAO;

    @EJB
    AttendanceDAO attendanceDAO;

    @Inject
    DashboardAttendance dbAttendance;

    public void fetchAttendances() {
        try {
            List<AttendanceBean> retValue = new ArrayList<>(0);
            if (!dbAttendance.getSelectedSectors().isEmpty()) {
                dbAttendance.getSelectedSectors().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbAttendance.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbAttendance.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, temp, null);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        }
                        if (temp1.getExit() != null) {
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbAttendance.getSelectedDepartments().isEmpty()) {
                dbAttendance.getSelectedDepartments().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbAttendance.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbAttendance.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, null, temp);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        }
                        if (temp1.getExit() != null) {
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbAttendance.getSelectedStaff().isEmpty()) {
                dbAttendance.getSelectedStaff().forEach((temp) -> {
                    List<Attendance> attendances = attendanceDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbAttendance.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbAttendance.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), temp, null, null);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        }
                        if (temp1.getExit() != null) {
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
            goError(e);
        }
    }

    public void goError(Exception ex) {
        try {
            logger.error("-----------AN ERROR HAPPENED !!!! -------------------- : " + ex.toString());
            if (sessionBean.getUsers() != null) {
                logger.error("User=" + sessionBean.getUsers().getUsername());
            }
            logger.error("Cause=" + ex.getCause());
            logger.error("Class=" + ex.getClass());
            logger.error("Message=" + ex.getLocalizedMessage());
            logger.error(ex, ex);
            logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");

            ErrorBean errorBean = (ErrorBean) FacesUtils.getManagedBean("errorBean");
            errorBean.reset();
            errorBean.setErrorMSG(MessageBundleLoader.getMessage(sessionBean.getErrorMsgKey()));
            //FacesUtils.redirectAJAX("./templates/error.jsf?faces-redirect=true");
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/error.jsf?faces-redirect=true");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
