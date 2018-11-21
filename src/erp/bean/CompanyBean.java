/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.entities.Address;
import erp.entities.Company;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author peukianm
 */
@Named(value = "companyBean")
@ViewScoped
public class CompanyBean implements Serializable {

    @Inject
    private SessionBean sessionBean;
    private List<Company> companies = new ArrayList<Company>(0);
    private Company company;
    private Boolean isInsert = false;
    private Address address;

    @PostConstruct
    public void init() {}

    @PreDestroy
    public void reset() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getIsInsert() {
        return isInsert;
    }

    public void setIsInsert(Boolean isInsert) {
        this.isInsert = isInsert;
    }

   
    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
