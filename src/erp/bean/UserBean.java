/**
 *
 */
package erp.bean;

import erp.entities.Company;
import erp.entities.Role;
import erp.entities.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author peukianm
 *
 */
@Named(value = "userBean")
@ViewScoped
public class UserBean implements Serializable{

    @Inject
    private SessionBean sessionBean;
    private java.lang.String username;
    private java.lang.String password;
    private Users user ;
    private List<Users> users = new ArrayList<Users>(0);
    private String searchByUsername;
    private String searchBySurname;
    private Role searchByRole;
    private Company searchByCompany ;
    private String repassword;
    private List<Role> roles = new ArrayList<Role>(0);
    private boolean active;

        
    public UserBean() {}

    @PostConstruct
    public void init() {}

    @PreDestroy
    public void reset() {        
        username = null;
        password = null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

        
    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
    
    

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public String getSearchByUsername() {
        return searchByUsername;
    }

    public void setSearchByUsername(String searchByUsername) {
        this.searchByUsername = searchByUsername;
    }

    public String getSearchBySurname() {
        return searchBySurname;
    }

    public void setSearchBySurname(String searchBySurname) {
        this.searchBySurname = searchBySurname;
    }

    public Role getSearchByRole() {
        return searchByRole;
    }

    public void setSearchByRole(Role searchByRole) {
        this.searchByRole = searchByRole;
    }

    public Company getSearchByCompany() {
        return searchByCompany;
    }

    public void setSearchByCompany(Company searchByCompany) {
        this.searchByCompany = searchByCompany;
    }

    
  
    
    public java.lang.String getUsername() {
        return username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }
}
