package erp.bean;

import erp.dao.StaffDAO;
import erp.entities.Company;
import erp.entities.Department;
import erp.entities.Role;
import erp.entities.Sector;
import erp.entities.Staff;
import erp.exception.ERPCustomException;
import erp.util.AccessControl;
import erp.util.FacesUtils;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DualListModel;

/**
 *
 * @author peukianm
 */
@Named("insertUser")
@ViewScoped
public class InsertUser implements Serializable {

    private static final Logger logger = LogManager.getLogger(InsertUser.class);

    @Inject
    private SessionBean sessionBean;
    
     @Inject
    private ApplicationBean applicationBean;

    @Inject
    private StaffDAO staffDao;

    String name;
    String surname;
    String username;
    String password;
    String repassword;
    String phone;
    String email;
    List<Role> selectedRoles;
    List<Department> selectedDepartments;
    Staff staff;
    Company company;
    Sector sector;
    Department department;
    List<Staff> availableStaff;
    private DualListModel<Department> depsPickList;

    public void preRenderView() {
        if (sessionBean.getUsers().getDepartment() != null && sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("itID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_INSERT_USER"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_INSERT_USER"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("insertUser"));
    }

    @PostConstruct
    public void init() {
        List<Department> depsSource = new ArrayList<>(0);
        List<Department> depstarget = new ArrayList<>(0);
        Department emptyDep = new Department();
        depsSource.add(emptyDep);
        depsSource.addAll(applicationBean.getDepartments());        
        depsPickList = new DualListModel<Department>(depsSource, depstarget);
    }

    @PreDestroy
    public void reset() {
    }

    public List<Staff> completeStaff(String surname) throws ERPCustomException {
        try {
            if (surname != null && !surname.trim().isEmpty() && surname.trim().length() >= 1) {
                surname = surname.trim();
                availableStaff = staffDao.fetchStaffAutoCompleteSurname(surname, null, null);
                return availableStaff;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Autocomplete Staff Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }
    
    public void onDepartmentChange(){
        System.out.println("ON DEPARTMET CHANGE");
        FacesUtils.updateHTMLComponnetWIthJS("insertUserFormID");
        
    }

    public DualListModel<Department> getDepsPickList() {
        return depsPickList;
    }

    public void setDepsPickList(DualListModel<Department> depsPickList) {
        this.depsPickList = depsPickList;
    }

    
    
    public List<Department> getSelectedDepartments() {
        return selectedDepartments;
    }

    public void setSelectedDepartments(List<Department> selectedDepartments) {
        this.selectedDepartments = selectedDepartments;
    }    
    
    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public List<Staff> getAvailableStaff() {
        return availableStaff;
    }

    public void setAvailableStaff(List<Staff> availableStaff) {
        this.availableStaff = availableStaff;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
