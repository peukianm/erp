/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.dao.ProadmissionDAO;
import erp.entities.Department;
import erp.entities.Patient;
import erp.entities.Proadmission;
import erp.exception.ERPCustomException;
import erp.util.AccessControl;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author peukianm
 */
@Named("updateProadmission")
@ViewScoped
public class UpdateProadmission implements Serializable {

    private static final Logger logger = LogManager.getLogger(UpdateProadmission.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ApplicationBean applicationBean;

    @EJB
    ProadmissionDAO proadmissionDAO;

    private Proadmission proadmission;
    private Patient searchPatient;
    private List<Patient> availablePatient;
    private List<Department> departments;
    private String patientInserted;
    private boolean release = false;

    String admissionID;

    public void preRenderView() {

    }

    public void init() {
        if (sessionBean.getUsers().getNosStatus() == null) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_UPDATE_ADMISSION"), null, 1)) {
                return;
            }
        }

        if (admissionID == null || admissionID.equals("")) {
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("noAdmissionSelected"));
            FacesUtils.redirectWithNavigationID("dashboardAdmission");
        }
        proadmission = proadmissionDAO.getProadmission(Long.parseLong(admissionID));

        if (proadmission == null) {
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("invalidAdmissionSelected"));
            FacesUtils.redirectWithNavigationID("dashboardAdmission");
        }

        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_UPDATE_ADMISSION"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("updateAdmission"));

        if (sessionBean.getUsers().getNosStatus() != null && sessionBean.getUsers().getNosStatus().equals("nosAdmin")) {
            departments = applicationBean.getClinics();
        } else if (sessionBean.getUsers().getNosStatus() != null && sessionBean.getUsers().getNosStatus().equals("nos")) {
            departments = sessionBean.getUsers().getDepartments();
        }

        if (proadmission.getReleased().equals(BigDecimal.ONE)) {
            release = true;
        } else {
            release = false;
        }
        patientInserted = "1";

    }

    @PreDestroy
    public void reset() {
        patientInserted = null;
    }

    public List<Patient> completePatientSurname(String surname) throws ERPCustomException {
        try {
            if (surname != null && !surname.trim().isEmpty() && surname.trim().length() >= 1) {
                surname = surname.trim();
                availablePatient = proadmissionDAO.fetchPatientAutoCompleteSurname(surname, true);
                return availablePatient;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Autocomplete Patient Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public List<Patient> completePatientAMKA(String amka) throws ERPCustomException {
        try {
            if (amka != null && !amka.trim().isEmpty() && amka.trim().length() >= 1) {
                amka = amka.trim();
                availablePatient = proadmissionDAO.fetchPatientAutoCompleteAmka(amka, true);
                return availablePatient;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Autocomplete Patient Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String getPatientInserted() {
        return patientInserted;
    }

    public void setPatientInserted(String patientInserted) {
        this.patientInserted = patientInserted;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public void autocompleteSelectPatient(SelectEvent event) {
        proadmission.setPatient(searchPatient);
        patientInserted = "1";
        searchPatient = null;
    }

    public void checkRelease() {
        if (!release){
            proadmission.setReleasedate(null);
        }            
    }

    public void removeSelectedPatient() {
        proadmission.setPatient(null);
        patientInserted = null;
    }

    public boolean isRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public Proadmission getProadmission() {
        return proadmission;
    }

    public void setProadmission(Proadmission proadmission) {
        this.proadmission = proadmission;
    }

    public Patient getSearchPatient() {
        return searchPatient;
    }

    public void setSearchPatient(Patient searchPatient) {
        this.searchPatient = searchPatient;
    }

   
    public List<Patient> getAvailablePatient() {
        return availablePatient;
    }

    public void setAvailablePatient(List<Patient> availablePatient) {
        this.availablePatient = availablePatient;
    }

    public String getAdmissionID() {
        return admissionID;
    }

    public void setAdmissionID(String admissionID) {
        this.admissionID = admissionID;
    }

}
