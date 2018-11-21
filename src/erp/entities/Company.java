package erp.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Company entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COMPANY", schema = "SKELETON")
@SequenceGenerator(name = "SEQ_COMPANY", sequenceName = "COMPANY_SEQ", allocationSize = 1)
public class Company implements java.io.Serializable {

    // Fields
    private BigDecimal companyid;
    private BigDecimal active;
    private String name;
    private String description;
    private String email;
    private String afm;
    private String contactperson;
    private Timestamp createdTimestamp;
    private Timestamp modifiedTimestamp;
    private String phone1;
    private String phone2;
    private Date createddate;
    private List<Address> addresses = new ArrayList<Address>(0);
    private Set<Auditing> auditings = new HashSet<Auditing>(0);
    private Set<Users> useres = new HashSet<Users>(0);


    
    
    // Constructors
    /**
     * default constructor
     */
    public Company() {
    }

    /**
     * minimal constructor
     */
    public Company(BigDecimal companyid, String name) {
        this.companyid = companyid;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Company(BigDecimal companyid, String name, String description, String email, String afm, String contactperson, Timestamp createdTimestamp,
            Timestamp modifiedTimestamp, String phone1, String phone2, List<Address> addresses, Set<Auditing> auditings,
            Set<Users> useres, BigDecimal active, Date createddate) {
        this.companyid = companyid;
        this.active = active;
        this.name = name;
        this.description = description;
        this.email = email;
        this.afm = afm;
        this.contactperson = contactperson;
        this.createdTimestamp = createdTimestamp;
        this.modifiedTimestamp = modifiedTimestamp;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.addresses = addresses;
        this.auditings = auditings;
        this.useres = useres;
        this.createddate = createddate;

    }

    // Property accessors
    @Id
    @Column(name = "COMPANYID", unique = true, nullable = false, precision = 22, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMPANY")
    public BigDecimal getCompanyid() {
        return this.companyid;
    }

    public void setCompanyid(BigDecimal companyid) {
        this.companyid = companyid;
    }

    @Column(name = "ACTIVE", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    @Column(name = "NAME", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION", length = 300)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "EMAIL", length = 40)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "AFM", length = 12)
    public String getAfm() {
        return this.afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    @Column(name = "CONTACTPERSON", length = 70)
    public String getContactperson() {
        return this.contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    public Timestamp getModifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    @Column(name = "PHONE1", length = 22)
    public String getPhone1() {
        return this.phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    @Column(name = "PHONE2", length = 22)
    public String getPhone2() {
        return this.phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    public Set<Users> getUseres() {
        return this.useres;
    }

    public void setUseres(Set<Users> useres) {
        this.useres = useres;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    public List<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    public Set<Auditing> getAuditings() {
        return this.auditings;
    }

    public void setAuditings(Set<Auditing> auditings) {
        this.auditings = auditings;
    }
  
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", length = 7)
    public Date getCreateddate() {
        return this.createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
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
        return "Company{id=" + companyid + ", name=" + getName() + ", description=" + getDescription() + "}";
    }
}