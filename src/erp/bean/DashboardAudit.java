package erp.bean;

import erp.dao.UsrDAO;
import erp.entities.Action;
import erp.entities.Actionscategory;
import erp.entities.Auditing;
import erp.entities.Company;
import erp.entities.Usr;
import erp.exception.ERPCustomException;
import erp.util.AccessControl;
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
import org.primefaces.event.SelectEvent;
import java.util.Date;

/**
 *
 * @author peukianm
 */
@Named("dbAudit")
@ViewScoped
public class DashboardAudit implements Serializable {

    private static final Logger logger = LogManager.getLogger(DashboardAudit.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private UsrDAO userDao;

    private Usr searchUser;
    private Action selectAction;
    private Actionscategory selectCategory;
    private Company selectedCompany;
    private List<Usr> availableUsers;
    private List<Auditing> searchAudit = new ArrayList<>(0);
    private Date fromAuditDate;
    private Date toAuditDate;
    private List<Usr> users;

    public void preRenderView() {
        if (sessionBean.getUsers().getDepartment() != null && sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("itID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_AUDIT_CONTROL"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_AUDIT_ADMIN"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("auditPage"));
    }

    @PostConstruct
    public void init() {
        System.out.println("INITIALIZE DB AUDIT BEAN");
        fromAuditDate = new java.util.Date();
        toAuditDate = new java.util.Date();
        users = userDao.getAll();
    }

    @PreDestroy
    public void reset() {

    }

    public void resetSearchAuditForm() {
        searchUser = null;
        selectAction = null;
        selectCategory = null;
        selectedCompany = null;
        fromAuditDate = new java.util.Date();
        toAuditDate = new java.util.Date();
        searchAudit = new ArrayList<>(0);
    }

    public List<Usr> completeUser(String username) throws ERPCustomException {
        try {
            if (username != null && !username.trim().isEmpty() && username.trim().length() >= 1) {
                username = username.trim();
                availableUsers = userDao.fetchUserAutoCompleteUsername(username);
                return availableUsers;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Autocomplete User Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public List<Usr> getUsers() {
        return users;
    }

    public void setUsers(List<Usr> users) {
        this.users = users;
    }

    public void autocompleteUsernameSelectUser(SelectEvent event) {
        searchUser = null;
    }

    public List<Usr> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(List<Usr> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public Company getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Company selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public Usr getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(Usr searchUser) {
        this.searchUser = searchUser;
    }

    public Action getSelectAction() {
        return selectAction;
    }

    public void setSelectAction(Action selectAction) {
        this.selectAction = selectAction;
    }

    public Actionscategory getSelectCategory() {
        return selectCategory;
    }

    public void setSelectCategory(Actionscategory selectCategory) {
        this.selectCategory = selectCategory;
    }

    public List<Auditing> getSearchAudit() {
        return searchAudit;
    }

    public void setSearchAudit(List<Auditing> searchAudit) {
        this.searchAudit = searchAudit;
    }

    public Date getFromAuditDate() {
        return fromAuditDate;
    }

    public void setFromAuditDate(Date fromAuditDate) {
        this.fromAuditDate = fromAuditDate;
    }

    public Date getToAuditDate() {
        return toAuditDate;
    }

    public void setToAuditDate(Date toAuditDate) {
        this.toAuditDate = toAuditDate;
    }

}
