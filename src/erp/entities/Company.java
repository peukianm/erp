package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the COMPANY database table.
 *
 */
@Entity
@NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "COMPANY_COMPANYID_GENERATOR", sequenceName = "COMPANY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_COMPANYID_GENERATOR")
    private BigDecimal companyid;

    private BigDecimal active;

    private String afm;

    private String contactperson;

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp createdTimestamp;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    private String description;

    private String email;

    
    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp modifiedTimestamp;

    private String name;
    
    private String abbrev;

    private String phone1;

    private String phone2;

    //bi-directional many-to-one association to Address
    @OneToMany(mappedBy = "company")
    private List<Address> addresses;

    //bi-directional many-to-one association to Auditing
    @OneToMany(mappedBy = "company")
    private List<Auditing> auditings;

    //bi-directional many-to-one association to User
    @OneToMany(mappedBy = "company")
    private List<Users> users;

    public Company() {
    }

    public BigDecimal getCompanyid() {
        return this.companyid;
    }

    public void setCompanyid(BigDecimal companyid) {
        this.companyid = companyid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    public String getAfm() {
        return this.afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }
    
    public String getAbbrev() {
        return this.abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getContactperson() {
        return this.contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getCreateddate() {
        return this.createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
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

    public String getPhone1() {
        return this.phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return this.phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public List<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Address addAddress(Address address) {
        getAddresses().add(address);
        address.setCompany(this);

        return address;
    }

    public Address removeAddress(Address address) {
        getAddresses().remove(address);
        address.setCompany(null);

        return address;
    }

    public List<Auditing> getAuditings() {
        return this.auditings;
    }

    public void setAuditings(List<Auditing> auditings) {
        this.auditings = auditings;
    }

    public Auditing addAuditing(Auditing auditing) {
        getAuditings().add(auditing);
        auditing.setCompany(this);

        return auditing;
    }

    public Auditing removeAuditing(Auditing auditing) {
        getAuditings().remove(auditing);
        auditing.setCompany(null);

        return auditing;
    }

    public List<Users> getUsers() {
        return this.users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public Users addUser(Users user) {
        getUsers().add(user);
        user.setCompany(this);

        return user;
    }

    public Users removeUser(Users user) {
        getUsers().remove(user);
        user.setCompany(null);

        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Company)) {
            return false;
        }

        Company compare = (Company) obj;
        return compare.companyid.equals(this.companyid);
    }

    @Override
    public int hashCode() {
        return companyid != null ? this.getClass().hashCode() + companyid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Company{id=" + companyid + ", name=" + getName() + "}";
    }

}
