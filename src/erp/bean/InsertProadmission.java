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
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named("insertProadmission")
@ViewScoped
public class InsertProadmission implements Serializable {

    private static final Logger logger = LogManager.getLogger(InsertProadmission.class);

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

    private boolean release = false;

    boolean active;

    public void preRenderView() {
        if (sessionBean.getUsers().getDepartment() != null
                && sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("itID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_INSERT_ADMISSION"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_INSERT_ADMISSION"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("insertAdmission"));
    }

    @PostConstruct
    public void init() {
        if (sessionBean.getUsers().getRole().getRoleid() < 3 || sessionBean.getUsers().getDepartment().getDepartmentid() == Integer.parseInt(SystemParameters.getInstance().getProperty("itID"))
                || sessionBean.getUsers().getDepartment().getDepartmentid() == Integer.parseInt(SystemParameters.getInstance().getProperty("kinisisID"))) {
            departments = applicationBean.getClinics();
        } else {
            departments = sessionBean.getUsers().getDepartments();
        }        
    }

    @PreDestroy
    public void reset() {
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
                availablePatient = proadmissionDAO.fetchPatientAutoCompleteSurname(amka, true);
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

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public void autocompleteSelectPatient(SelectEvent event) {
    }

    public void checkRelease() {
        System.out.println(release);
    }

    public void removeSelectedPatient() {
        searchPatient = null;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Patient> getAvailablePatient() {
        return availablePatient;
    }

    public void setAvailablePatient(List<Patient> availablePatient) {
        this.availablePatient = availablePatient;
    }

}
