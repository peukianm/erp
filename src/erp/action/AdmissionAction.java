package erp.action;

import erp.bean.DashboardAdmission;
import erp.bean.InsertProadmission;
import erp.bean.SessionBean;
import erp.bean.UpdateProadmission;
import erp.dao.AuditingDAO;
import erp.dao.ProadmissionDAO;
import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Proadmission;
import erp.exception.ERPCustomException;
import erp.util.FacesUtils;
import erp.util.FormatUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
public class AdmissionAction {

    private static final Logger logger = LogManager.getLogger(AdmissionAction.class);

    @Inject
    private SessionBean sessionBean;

    @EJB
    ProadmissionDAO proadmissionDAO;

    @EJB
    AuditingDAO auditingDAO;

    @Inject
    InsertProadmission insertProadmission;

    @Inject
    UpdateProadmission updateProadmission;

    @Inject
    DashboardAdmission dbAdmission;

    public String insertProadmission() throws ERPCustomException {
        try {
            insertProadmission.getProadmission().setProcessed(BigDecimal.ZERO);
            insertProadmission.getProadmission().setActive(BigDecimal.ONE);

            if (insertProadmission.isRelease()) {
                insertProadmission.getProadmission().setReleased(BigDecimal.ONE);
            } else {
                insertProadmission.getProadmission().setReleased(BigDecimal.ZERO);
            }

            proadmissionDAO.save(insertProadmission.getProadmission());

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_INSERTADMISSION")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Proadmission " + insertProadmission.getProadmission() + " inserted",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("newAdmissionInserted"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ADMISSION_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("admissionPage"));
            insertProadmission.resetInsertAdmissionForm();
            return "insertProadmission";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From insert Proadmission Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String insertPatient() throws ERPCustomException {
        try {            
            insertProadmission.getNewPatient().setActive(BigDecimal.ONE);
            insertProadmission.getNewPatient().setDead(BigDecimal.ZERO);
             insertProadmission.setPatientInserted("1");
            proadmissionDAO.save(insertProadmission.getNewPatient());

            insertProadmission.getProadmission().setPatient(insertProadmission.getNewPatient());

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_INSERTPATIENT")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "PATIENT " + insertProadmission.getNewPatient() + " inserted",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("newPatientInserted"));
            //sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ADMISSION_ADMIN"));
            //sessionBean.setPageName(MessageBundleLoader.getMessage("admissionPage"));
            //insertProadmission.resetInsertAdmissionForm();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From insert Proadmission Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void fetchAdmissions() throws ERPCustomException {
        try {
            List<Proadmission> retValue = new ArrayList<>(0);

            if (!dbAdmission.getSearchPatients().isEmpty()) {

                dbAdmission.getSearchPatients().forEach((patient) -> {
                    if (dbAdmission.getSelectedDepartments().isEmpty()) {
                        Integer release = null;
                        if (dbAdmission.isRelease()) {
                            release = 1;
                        }
                        List<Proadmission> admissions = proadmissionDAO.getProadmissions(null, FormatUtils.formatDateToTimestamp(dbAdmission.getFromAdmissionDate(), FormatUtils.DATEPATTERN),
                                FormatUtils.formatDateToTimestamp(dbAdmission.getToAdmissionDate(), FormatUtils.DATEPATTERN), patient, FormatUtils.formatDateToTimestamp(dbAdmission.getFromReleaseDate(),
                                FormatUtils.DATEPATTERN), FormatUtils.formatDateToTimestamp(dbAdmission.getToReleaseDate(), FormatUtils.DATEPATTERN), release, null, true);

                        retValue.addAll(admissions);
                    } else {
                        dbAdmission.getSelectedDepartments().forEach((department) -> {
                            Integer release = null;
                            if (dbAdmission.isRelease()) {
                                release = 1;
                            }
                            List<Proadmission> admissions = proadmissionDAO.getProadmissions(department, FormatUtils.formatDateToTimestamp(dbAdmission.getFromAdmissionDate(), FormatUtils.DATEPATTERN),
                                    FormatUtils.formatDateToTimestamp(dbAdmission.getToAdmissionDate(), FormatUtils.DATEPATTERN), patient, FormatUtils.formatDateToTimestamp(dbAdmission.getFromReleaseDate(),
                                    FormatUtils.DATEPATTERN), FormatUtils.formatDateToTimestamp(dbAdmission.getToReleaseDate(), FormatUtils.DATEPATTERN), release, null, true);
                            retValue.addAll(admissions);
                        });
                    }
                });

            } else {

                if (dbAdmission.getSelectedDepartments().isEmpty()) {
                    Integer release = null;
                    if (dbAdmission.isRelease()) {
                        release = 1;
                    }
                    List<Proadmission> admissions = proadmissionDAO.getProadmissions(null, FormatUtils.formatDateToTimestamp(dbAdmission.getFromAdmissionDate(), FormatUtils.DATEPATTERN),
                            FormatUtils.formatDateToTimestamp(dbAdmission.getToAdmissionDate(), FormatUtils.DATEPATTERN), null, FormatUtils.formatDateToTimestamp(dbAdmission.getFromReleaseDate(),
                            FormatUtils.DATEPATTERN), FormatUtils.formatDateToTimestamp(dbAdmission.getToReleaseDate(), FormatUtils.DATEPATTERN), release, null, true);

                    retValue.addAll(admissions);
                } else {
                    dbAdmission.getSelectedDepartments().forEach((department) -> {
                        Integer release = null;
                        if (dbAdmission.isRelease()) {
                            release = 1;
                        }
                        List<Proadmission> admissions = proadmissionDAO.getProadmissions(department, FormatUtils.formatDateToTimestamp(dbAdmission.getFromAdmissionDate(), FormatUtils.DATEPATTERN),
                                FormatUtils.formatDateToTimestamp(dbAdmission.getToAdmissionDate(), FormatUtils.DATEPATTERN), null, FormatUtils.formatDateToTimestamp(dbAdmission.getFromReleaseDate(),
                                FormatUtils.DATEPATTERN), FormatUtils.formatDateToTimestamp(dbAdmission.getToReleaseDate(), FormatUtils.DATEPATTERN), release, null, true);
                        retValue.addAll(admissions);
                    });
                }

            }

            dbAdmission.setSearchAdmissions(retValue);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From fetch users Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String goUpdateAdmission(long admissionID) {
        return "updateProadmission?faces-redirect=true&admissionID=" + admissionID;
    }

    public String deleteAdmission(long admissionID) throws ERPCustomException {
        try {
            Proadmission admission = dbAdmission.getAdmissionForUpdate();
            admission.setActive(BigDecimal.ZERO);
            proadmissionDAO.delete(admission);
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEADMISSION")), "Admission " + admission + " deleted");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("admissionDeleted"));
            fetchAdmissions();
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From deactivate admission ", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String updateAdmission() throws ERPCustomException {
        try {

            Proadmission updatedAdmission = updateProadmission.getProadmission();
            if (updateProadmission.isRelease()) {
                updatedAdmission.setReleased(BigDecimal.ONE);
            } else {
                updatedAdmission.setReleased(BigDecimal.ZERO);
            }

            proadmissionDAO.update(updatedAdmission);

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEADMISSION")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Admission " + updatedAdmission + " updated",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("admissionUpdated"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ADMISSION_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("admissionPage"));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Update Admission Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

}
