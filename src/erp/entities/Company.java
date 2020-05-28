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
    private long companyid;

    private String abbrev;

    private BigDecimal active;

    private String afm;

    private String contactperson;

    @Column(name = "CREATED_TIMESTAMP")
    private Timestamp createdTimestamp;

    @Temporal(TemporalType.DATE)
    private Date createddate;

    private String description;

    private String email;

    @Column(name = "MODIFIED_TIMESTAMP")
    private Timestamp modifiedTimestamp;

    private String name;

    private String phone1;

    private String phone2;

    //bi-directional many-to-one association to Address
    @OneToMany(mappedBy = "company")
    private List<Address> addresses;

    //bi-directional many-to-one association to Auditing
    @OneToMany(mappedBy = "company")
    private List<Auditing> auditings;

    //bi-directional many-to-many association to Scheduletask
    @ManyToMany
    @JoinTable(
            name = "COMPANYTASK",
             joinColumns = {
                @JoinColumn(name = "COMPANYID")
            },
             inverseJoinColumns = {
                @JoinColumn(name = "TASKID")
            }
    )
    private List<Scheduletask> scheduletasks;

    //bi-directional many-to-many association to Sector
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "COMPANYSECTOR",
             joinColumns = {
                @JoinColumn(name = "COMPANYID")
            },
             inverseJoinColumns = {
                @JoinColumn(name = "SECTORID")
            }
    )
    private List<Sector> sectors;

    //bi-directional many-to-one association to Companysector
    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<Companysector> companysectors;

    //bi-directional many-to-one association to Companytask
    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<Companytask> companytasks;

    //bi-directional many-to-one association to Equipmentdepartment
    @OneToMany(mappedBy = "company")
    private List<Equipmentdepartment> equipmentdepartments;

    //bi-directional many-to-one association to Loggerdata
    @OneToMany(mappedBy = "company")
    private List<Loggerdata> loggerdata;

    //bi-directional many-to-one association to Sectordepartment
    @OneToMany(mappedBy = "company")
    private List<Sectordepartment> sectordepartments;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "company")
    private List<Staff> staffs;

    //bi-directional many-to-one association to Usr
    @OneToMany(mappedBy = "company")
    private List<Usr> usrs;

    //bi-directional many-to-one association to Attendance
    @OneToMany(mappedBy = "company")
    private List<Attendance> attendances;

    public Company() {
    }

    public long getCompanyid() {
        return this.companyid;
    }

    public void setCompanyid(long companyid) {
        this.companyid = companyid;
    }

    public String getAbbrev() {
        return this.abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
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

    public List<Scheduletask> getScheduletasks() {
        return this.scheduletasks;
    }

    public void setScheduletasks(List<Scheduletask> scheduletasks) {
        this.scheduletasks = scheduletasks;
    }

    public List<Sector> getSectors() {
        return this.sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public List<Companysector> getCompanysectors() {
        return this.companysectors;
    }

    public void setCompanysectors(List<Companysector> companysectors) {
        this.companysectors = companysectors;
    }

    public Companysector addCompanysector(Companysector companysector) {
        getCompanysectors().add(companysector);
        companysector.setCompany(this);

        return companysector;
    }

    public Companysector removeCompanysector(Companysector companysector) {
        getCompanysectors().remove(companysector);
        companysector.setCompany(null);

        return companysector;
    }

    public List<Companytask> getCompanytasks() {
        return this.companytasks;
    }

    public void setCompanytasks(List<Companytask> companytasks) {
        this.companytasks = companytasks;
    }

    public Companytask addCompanytask(Companytask companytask) {
        getCompanytasks().add(companytask);
        companytask.setCompany(this);

        return companytask;
    }

    public Companytask removeCompanytask(Companytask companytask) {
        getCompanytasks().remove(companytask);
        companytask.setCompany(null);

        return companytask;
    }

    public List<Equipmentdepartment> getEquipmentdepartments() {
        return this.equipmentdepartments;
    }

    public void setEquipmentdepartments(List<Equipmentdepartment> equipmentdepartments) {
        this.equipmentdepartments = equipmentdepartments;
    }

    public Equipmentdepartment addEquipmentdepartment(Equipmentdepartment equipmentdepartment) {
        getEquipmentdepartments().add(equipmentdepartment);
        equipmentdepartment.setCompany(this);

        return equipmentdepartment;
    }

    public Equipmentdepartment removeEquipmentdepartment(Equipmentdepartment equipmentdepartment) {
        getEquipmentdepartments().remove(equipmentdepartment);
        equipmentdepartment.setCompany(null);

        return equipmentdepartment;
    }

    public List<Loggerdata> getLoggerdata() {
        return this.loggerdata;
    }

    public void setLoggerdata(List<Loggerdata> loggerdata) {
        this.loggerdata = loggerdata;
    }

    public Loggerdata addLoggerdata(Loggerdata loggerdata) {
        getLoggerdata().add(loggerdata);
        loggerdata.setCompany(this);

        return loggerdata;
    }

    public Loggerdata removeLoggerdata(Loggerdata loggerdata) {
        getLoggerdata().remove(loggerdata);
        loggerdata.setCompany(null);

        return loggerdata;
    }

    public List<Sectordepartment> getSectordepartments() {
        return this.sectordepartments;
    }

    public void setSectordepartments(List<Sectordepartment> sectordepartments) {
        this.sectordepartments = sectordepartments;
    }

    public Sectordepartment addSectordepartment(Sectordepartment sectordepartment) {
        getSectordepartments().add(sectordepartment);
        sectordepartment.setCompany(this);

        return sectordepartment;
    }

    public Sectordepartment removeSectordepartment(Sectordepartment sectordepartment) {
        getSectordepartments().remove(sectordepartment);
        sectordepartment.setCompany(null);

        return sectordepartment;
    }

    public List<Staff> getStaffs() {
        return this.staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }

    public Staff addStaff(Staff staff) {
        getStaffs().add(staff);
        staff.setCompany(this);

        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setCompany(null);

        return staff;
    }

    public List<Usr> getUsrs() {
        return this.usrs;
    }

    public void setUsrs(List<Usr> usrs) {
        this.usrs = usrs;
    }

    public Usr addUsr(Usr usr) {
        getUsrs().add(usr);
        usr.setCompany(this);

        return usr;
    }

    public Usr removeUsr(Usr usr) {
        getUsrs().remove(usr);
        usr.setCompany(null);

        return usr;
    }

    public List<Attendance> getAttendances() {
        return this.attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendance addAttendance(Attendance attendance) {
        getAttendances().add(attendance);
        attendance.setCompany(this);

        return attendance;
    }

    public Attendance removeAttendance(Attendance attendance) {
        getAttendances().remove(attendance);
        attendance.setCompany(null);

        return attendance;
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
        return compare.companyid == (this.companyid);
    }

    @Override
    public int hashCode() {
        return companyid != 0 ? this.getClass().hashCode() + Long.hashCode(companyid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Company{id=" + companyid + ", name=" + getName() + "}";
    }

}
