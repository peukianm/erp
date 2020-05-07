/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Company;
import erp.entities.Usr;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;


/**
 *
 * @author peukianm
 */
@Named(value = "auditBean")
@ViewScoped
public class AuditBean implements Serializable {
    
    @Inject
    private SessionBean sessionBean;
    
    private Date searchFromActionDate;
    private Date searchToActionDate;
    private Usr searchUser;
    private Usr selectUser;
    private Action searchAction;
    private Company searchCompany;

    private List<Auditing> audits = new ArrayList<>(0);

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void reset() {

        searchFromActionDate = null;
        searchToActionDate = null;
        searchUser = null;
        selectUser = null;

        searchAction = null;
        searchCompany = null;
        audits = new ArrayList<Auditing>(0);

    }

    public Company getSearchCompany() {
        return searchCompany;
    }

    public void setSearchCompany(Company searchCompany) {
        this.searchCompany = searchCompany;
    }

    public Usr getSelectUser() {
        return selectUser;
    }

    public void setSelectUser(Usr selectUser) {
        this.selectUser = selectUser;
    }

    public Date getSearchFromActionDate() {
        return searchFromActionDate;
    }

    public void setSearchFromActionDate(Date searchFromActionDate) {
        this.searchFromActionDate = searchFromActionDate;
    }

    public Date getSearchToActionDate() {
        return searchToActionDate;
    }

    public void setSearchToActionDate(Date searchToActionDate) {
        this.searchToActionDate = searchToActionDate;
    }

    public Usr getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(Usr searchUser) {
        this.searchUser = searchUser;
    }

    public Action getSearchAction() {
        return searchAction;
    }

    public void setSearchAction(Action searchAction) {
        this.searchAction = searchAction;
    }

    
    public List<Auditing> getAudits() {
        return audits;
    }

    public void setAudits(List<Auditing> audits) {
        this.audits = audits;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
