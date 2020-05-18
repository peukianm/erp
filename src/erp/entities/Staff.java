package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the STAFF database table.
 *
 */
@Entity
@NamedQuery(name = "Staff.findAll", query = "SELECT s FROM Staff s")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "STAFF_STAFFID_GENERATOR", sequenceName = "STAFF_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STAFF_STAFFID_GENERATOR")
    private long staffid;

    private BigDecimal active;

    private String address;

    private String adt;

    private String afm;

    private String amka;

    private String amy;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    private String city;

    @Column(name = "CREATED_TIMESTAMP")
    private Timestamp createdTimestamp;

    private String cteamid;

    private String fathername;

    private String loggercode;

    @Column(name = "MODIFIED_TIMESTAMP")
    private Timestamp modifiedTimestamp;

    private String name;

    private String phone1;

    private String phone2;

    private String surname;

    //bi-directional many-to-one association to Company
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMPANYID")
    private Company company;

    //bi-directional many-to-one association to Department
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPARTMENTID")
    private Department department;

    //bi-directional many-to-one association to Emprank
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RANKID")
    private Emprank emprank;

    //bi-directional many-to-one association to Sector
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SECTORID")
    private Sector sector;

    //bi-directional many-to-one association to Workshift
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SHIFTID")
    private Workshift workshift;

    //bi-directional many-to-one association to Attendance
    @OneToMany(mappedBy = "staff")
    private List<Attendance> attendances;

    public Staff() {
    }

    public long getStaffid() {
        return this.staffid;
    }

    public void setStaffid(long staffid) {
        this.staffid = staffid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdt() {
        return this.adt;
    }

    public void setAdt(String adt) {
        this.adt = adt;
    }

    public String getAfm() {
        return this.afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getAmka() {
        return this.amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getAmy() {
        return this.amy;
    }

    public void setAmy(String amy) {
        this.amy = amy;
    }

    public Date getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCteamid() {
        return this.cteamid;
    }

    public void setCteamid(String cteamid) {
        this.cteamid = cteamid;
    }

    public String getFathername() {
        return this.fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getLoggercode() {
        return this.loggercode;
    }

    public void setLoggercode(String loggercode) {
        this.loggercode = loggercode;
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

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public Emprank getEmprank() {
        return this.emprank;
    }

    public void setEmprank(Emprank emprank) {
        this.emprank = emprank;
    }

    public Sector getSector() {
        return this.sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Workshift getWorkshift() {
        return this.workshift;
    }

    public void setWorkshift(Workshift workshift) {
        this.workshift = workshift;
    }

    public List<Attendance> getAttendances() {
        return this.attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendance addAttendance(Attendance attendance) {
        getAttendances().add(attendance);
        attendance.setStaff(this);

        return attendance;
    }

    public Attendance removeAttendance(Attendance attendance) {
        getAttendances().remove(attendance);
        attendance.setStaff(null);
        return attendance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Staff)) {
            return false;
        }

        Staff compare = (Staff) obj;
        return compare.staffid == (this.staffid);
    }

    @Override
    public int hashCode() {
        return staffid != 0 ? this.getClass().hashCode() + Long.hashCode(staffid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Staff{id=" + staffid + ", surname=" + getSurname() + "}";
    }

}
