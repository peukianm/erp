/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.entities.Company;
import erp.entities.Role;
import erp.entities.Staff;
import java.io.Serializable;
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
@Named("insertUser")
@ViewScoped
public class InsertUser implements Serializable {

    @Inject
    private ApplicationBean applicationBean;

    String name;
    String surname;
    String username;
    String password;
    String repassword;
    String phone;
    String email;
    List<Role> selectedRoles;
    Staff staff;
    Company company;

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void reset() {
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
