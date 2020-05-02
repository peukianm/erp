package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The persistent class for the USERS database table.
 *
 */
@Entity
@Table(name = "USERS")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM Users u")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "USERS_USERID_GENERATOR", sequenceName = "USERS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_USERID_GENERATOR")
    private BigDecimal userid;

    private BigDecimal active;

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp createdTimestamp;

    private String description;

    private String email;

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp modifiedTimestamp;

    private String name;

    private String password;

    private String phone;

    private String surname;

    private String username;

    //bi-directional many-to-one association to Auditing
    @OneToMany(mappedBy = "users")
    private List<Auditing> auditings;

    //bi-directional many-to-one association to Userrole
    @OneToMany(mappedBy = "users")
    private List<Userroles> userroleses;

    //bi-directional many-to-one association to Company
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANYID")
    private Company company;

    //bi-directional many-to-one association to Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENTID")
    private Department department;

    //bi-directional many-to-one association to Role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLEID")
    private Role role;

    public Users() {
    }

    public BigDecimal getUserid() {
        return this.userid;
    }

    public void setUserid(BigDecimal userid) {
        this.userid = userid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getModifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Auditing> getAuditings() {
        return this.auditings;
    }

    public void setAuditings(List<Auditing> auditings) {
        this.auditings = auditings;
    }

    public Auditing addAuditing(Auditing auditing) {
        getAuditings().add(auditing);
        auditing.setUsers(this);

        return auditing;
    }

    public Auditing removeAuditing(Auditing auditing) {
        getAuditings().remove(auditing);
        auditing.setUsers(null);

        return auditing;
    }

    public List<Userroles> getUserroleses() {
        return this.userroleses;
    }

    public void setUserroleses(List<Userroles> userroleses) {
        this.userroleses = userroleses;
    }

    public Userroles addUserrole(Userroles userrole) {
        getUserroleses().add(userrole);
        userrole.setUsers(this);
        return userrole;
    }

    public Userroles removeUserrole(Userroles userrole) {
        getUserroleses().remove(userrole);
        userrole.setUsers(null);

        return userrole;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    private List<Role> userRoles = new ArrayList<Role>(0);
    
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "USERROLES",
    joinColumns = {
        @JoinColumn(name = "USERID")
    },
    inverseJoinColumns = {
        @JoinColumn(name = "ROLEID")
    })
    public List<Role> getUserRoles() {
        if (userRoles!=null) {
//           Collections.sort(userRoles, new Comparator<Productspecification>() {
//                public int compare(Productspecification one, Productspecification other) {
//                    if (one.getOrdered()!=null && other.getOrdered()!=null)
//                        return one.getOrdered().compareTo(other.getOrdered());
//                    else if (one.getSpecification().getOrdered()!= null && other.getSpecification().getOrdered()!=null)
//                        return one.getSpecification().getOrdered().compareTo(other.getSpecification().getOrdered());
//                    else
//                        return one.getSpecification().getName().compareTo(other.getSpecification().getName()); 
//                }
//            });
        }
        return userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Users)) {
            return false;
        }

        Users compare = (Users) obj;
        return compare.userid.equals(this.userid);
    }

    @Override
    public int hashCode() {
        return userid != null ? this.getClass().hashCode() + userid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Users{id=" + userid + ", name=" + getName() + "}";
    }

}
