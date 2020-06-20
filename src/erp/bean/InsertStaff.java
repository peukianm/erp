package erp.bean;

import erp.dao.StaffDAO;
import erp.entities.Staff;
import erp.util.AccessControl;
import erp.util.MessageBundleLoader;
import erp.util.SystemParameters;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Named("insertStaff")
@ViewScoped
public class InsertStaff implements Serializable {

    private static final Logger logger = LogManager.getLogger(InsertStaff.class);

    @Inject
    private SessionBean sessionBean;

    @Inject
    private StaffDAO staffDao;

    Staff staff;
    boolean active;

    public void preRenderView() {
        if (sessionBean.getUsers().getDepartment() != null && sessionBean.getUsers().getDepartment().getDepartmentid() != Integer.parseInt(SystemParameters.getInstance().getProperty("hrID"))) {
            if (!AccessControl.control(sessionBean.getUsers(), SystemParameters.getInstance().getProperty("PAGE_INSERT_STAFF"), null, 1)) {
                return;
            }
        }
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_INSERT_STAFF"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("insertStaff"));
    }

    @PostConstruct
    public void init() {
        staff = new Staff();
        staff.setCompany(sessionBean.getUsers().getCompany());
        active = true;
    }

    @PreDestroy
    public void reset() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
