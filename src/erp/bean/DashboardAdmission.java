/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.dao.ProadmissionDAO;
import erp.dao.UsrDAO;
import erp.entities.Department;
import erp.entities.Patient;
import erp.entities.Proadmission;
import erp.entities.Usr;
import erp.exception.ERPCustomException;
import erp.util.AccessControl;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
@Named("dbAdmission")
@ViewScoped
public class DashboardAdmission implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardUsers.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private ProadmissionDAO proadmissionDAO;

    private List<Patient> availablepatients;
    private List<Patient> searchPatients = new ArrayList<>(0);
    private Patient searchPatient;
    private List<Department> selectedDepartments = new ArrayList<>(0);
    private List<Department> departments;
    private boolean admin = false;
    private boolean release = false;
    private List<Proadmission> searchAdmissions = new ArrayList<>(0);
    private Date fromAdmissionDate;
    private Date toAdmissionDate;
    private Date fromReleaseDate;
    private Date toReleaseDate;
    private Proadmission viewAdmission;
    private Proadmission admissionForUpdate;

    public void preRenderView() {            
        if (sessionBean.getUsers().getNosStatus() == null) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_ADMISSION_ADMIN"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ADMISSION_ADMIN"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("admissionPage"));
    }

    @PostConstruct
    public void init() {
        fromAdmissionDate = new java.util.Date();
        toAdmissionDate = new java.util.Date();
        if (sessionBean.getUsers().getNosStatus() != null && sessionBean.getUsers().getNosStatus().equals("nosAdmin")) {
            departments = applicationBean.getClinics();
            admin = true;
        } else if (sessionBean.getUsers().getNosStatus() != null && sessionBean.getUsers().getNosStatus().equals("nos")) {
            departments = sessionBean.getUsers().getDepartments();
            selectedDepartments.add(sessionBean.getUsers().getDepartment());
            admin = false;
        }
    }

    @PreDestroy
    public void reset() {
    }

    public void resetSearchAdmissionForm() {
        searchPatients = new ArrayList<>(0);
        searchPatient = null;
        release = false;
        fromAdmissionDate = new java.util.Date();
        toAdmissionDate = new java.util.Date();
        fromReleaseDate = null;
        toReleaseDate = null;
        searchAdmissions = new ArrayList<>(0);
        if (admin) {
            selectedDepartments = new ArrayList<>(0);
        } else {
            selectedDepartments = new ArrayList<>(0);
            selectedDepartments.add(sessionBean.getUsers().getDepartment());
        }
    }

    public List<Patient> completePatientSurname(String surname) throws ERPCustomException {
        try {
            if (surname != null && !surname.trim().isEmpty() && surname.trim().length() >= 1) {
                surname = surname.trim();
                availablepatients = proadmissionDAO.fetchPatientAutoCompleteSurname(surname, true);
                return availablepatients;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Autocomplete Patient Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public List<Patient> completePatientAmka(String amka) throws ERPCustomException {
        try {
            if (amka != null && !amka.trim().isEmpty() && amka.trim().length() >= 1) {
                amka = amka.trim();
                availablepatients = proadmissionDAO.fetchPatientAutoCompleteAmka(amka, true);
                return availablepatients;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Autocomplete Patient Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void autocompleteSurnameSelectPatient(SelectEvent event) {
        if (!searchPatients.contains(searchPatient)) {
            searchPatients.add(searchPatient);
        }
        searchPatient = null;
    }

    public void removePatient(int index) {
        if (searchPatients != null && searchPatients.size() > 0 && searchPatients.size() > index) {
            searchPatients.remove(index);
        }
    }

    public List<Department> getSelectedDepartments() {
        return selectedDepartments;
    }

    public void setSelectedDepartments(List<Department> selectedDepartments) {
        this.selectedDepartments = selectedDepartments;
    }

    public Proadmission getViewAdmission() {
        return viewAdmission;
    }

    public void setViewAdmission(Proadmission viewAdmission) {
        this.viewAdmission = viewAdmission;
    }

    public Date getFromReleaseDate() {
        return fromReleaseDate;
    }

    public void setFromReleaseDate(Date fromReleaseDate) {
        this.fromReleaseDate = fromReleaseDate;
    }

    public Date getToReleaseDate() {
        return toReleaseDate;
    }

    public void setToReleaseDate(Date toReleaseDate) {
        this.toReleaseDate = toReleaseDate;
    }

    public boolean isRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public Date getFromAdmissionDate() {
        return fromAdmissionDate;
    }

    public void setFromAdmissionDate(Date fromAdmissionDate) {
        this.fromAdmissionDate = fromAdmissionDate;
    }

    public Date getToAdmissionDate() {
        return toAdmissionDate;
    }

    public void setToAdmissionDate(Date toAdmissionDate) {
        this.toAdmissionDate = toAdmissionDate;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Proadmission> getSearchAdmissions() {
        return searchAdmissions;
    }

    public void setSearchAdmissions(List<Proadmission> searchAdmissions) {
        this.searchAdmissions = searchAdmissions;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Patient> getAvailablepatients() {
        return availablepatients;
    }

    public void setAvailablepatients(List<Patient> availablepatients) {
        this.availablepatients = availablepatients;
    }

    public List<Patient> getSearchPatients() {
        return searchPatients;
    }

    public void setSearchPatients(List<Patient> searchPatients) {
        this.searchPatients = searchPatients;
    }

    public Patient getSearchPatient() {
        return searchPatient;
    }

    public void setSearchPatient(Patient searchPatient) {
        this.searchPatient = searchPatient;
    }

    public Proadmission getAdmissionForUpdate() {
        return admissionForUpdate;
    }

    public void setAdmissionForUpdate(Proadmission admissionForUpdate) {
        this.admissionForUpdate = admissionForUpdate;
    }

}
