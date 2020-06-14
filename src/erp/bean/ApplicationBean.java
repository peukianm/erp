package erp.bean;

import erp.dao.AuditingDAO;
import erp.dao.CompanyDAO;
import erp.dao.UsrDAO;

import erp.entities.Action;
import erp.entities.Company;
import erp.entities.Department;
import erp.entities.Emprank;
import erp.entities.Role;
import erp.entities.Sector;
import erp.entities.Workshift;

import erp.util.SystemParameters;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author peukianm
 */
@Named(value = "applicationBean")
@ApplicationScoped
public class ApplicationBean implements Serializable {

    String propertyValue;
    List<Company> companies;
    List<Role> roles;
    List<Sector> sectors;
    List<Department> departments;
    List<Action> actions;
    List<Emprank> empranks;
    List<Workshift> workshifts;

    @Inject
    CompanyDAO companyDAO;

    @Inject
    UsrDAO userDAO;
    
    @Inject
    AuditingDAO auditingDAO;

    @PostConstruct
    public void init() {
    }

    public List<Emprank> getEmpranks() {
                if (empranks == null) {
            empranks = companyDAO.getAllEmpRanks();
        }        
       
        return empranks;
    }

    public void setEmpranks(List<Emprank> empranks) {
        this.empranks = empranks;
    }

    public List<Workshift> getWorkshifts() {
        if (workshifts == null) {
            workshifts = companyDAO.getAllWorkShifts(true);
        }        
        return workshifts;
    }

    public void setWorkshifts(List<Workshift> workshifts) {
        this.workshifts = workshifts;
    }

    
    
    public List<Department> getDepartments() {
        if (departments == null) {
            departments = companyDAO.getAllDepartment(true);
        }
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Sector> getSectors() {
        if (sectors == null) {
            sectors = companyDAO.getAllSector();
        }
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public String getPropertyValue(String key) {
        propertyValue = SystemParameters.getInstance().getProperty(key);
        return propertyValue;
    }

    public List<Company> getCompanies() {
        if (companies == null) {
            companies = companyDAO.getAllCompanies(true);
        }
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<Role> getRoles() {
        if (roles == null) {
            roles = userDAO.getAllRoles();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void resetCompanies() {
        this.companies = null;
    }

    public List<Action> getActions() {
        if (actions == null) {
            actions = auditingDAO.getAllActions();
        }
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

}
