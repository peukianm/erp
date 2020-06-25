package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the SECTOR database table.
 *
 */
@Entity
@NamedQuery(name = "Sector.findAll", query = "SELECT s FROM Sector s")
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SECTOR_SECTORID_GENERATOR", sequenceName = "SECTOR_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECTOR_SECTORID_GENERATOR")
    private long sectorid;

    private String name;

    private BigDecimal ordered;

    //bi-directional many-to-many association to Company
    @ManyToMany(mappedBy = "sectors")
    private List<Company> companies;

    //bi-directional many-to-one association to Companysector
    @OneToMany(mappedBy = "sector" )
    private List<Companysector> companysectors;

    //bi-directional many-to-many association to Department
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "SECTORDEPARTMENT",
            joinColumns = {
                @JoinColumn(name = "SECTORID")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "DEPARTMENTID")
            }
    )
    private List<Department> departments;

    //bi-directional many-to-one association to Sectordepartment
    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<Sectordepartment> sectordepartments;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "sector")
    private List<Staff> staffs;

    //bi-directional many-to-one association to Attendance
    @OneToMany(mappedBy = "sector")
    private List<Attendance> attendances;

    //bi-directional many-to-one association to Usr
    @OneToMany(mappedBy = "sector")
    private List<Usr> usrs;

    public Sector() {
    }

    public long getSectorid() {
        return this.sectorid;
    }

    public void setSectorid(long sectorid) {
        this.sectorid = sectorid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOrdered() {
        return this.ordered;
    }

    public void setOrdered(BigDecimal ordered) {
        this.ordered = ordered;
    }

    public List<Company> getCompanies() {
        return this.companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<Companysector> getCompanysectors() {
        return this.companysectors;
    }

    public void setCompanysectors(List<Companysector> companysectors) {
        this.companysectors = companysectors;
    }

    public Companysector addCompanysector(Companysector companysector) {
        getCompanysectors().add(companysector);
        companysector.setSector(this);

        return companysector;
    }

    public Companysector removeCompanysector(Companysector companysector) {
        getCompanysectors().remove(companysector);
        companysector.setSector(null);

        return companysector;
    }

    public List<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Sectordepartment> getSectordepartments() {
        return this.sectordepartments;
    }

    public void setSectordepartments(List<Sectordepartment> sectordepartments) {
        this.sectordepartments = sectordepartments;
    }

    public Sectordepartment addSectordepartment(Sectordepartment sectordepartment) {
        getSectordepartments().add(sectordepartment);
        sectordepartment.setSector(this);

        return sectordepartment;
    }

    public Sectordepartment removeSectordepartment(Sectordepartment sectordepartment) {
        getSectordepartments().remove(sectordepartment);
        sectordepartment.setSector(null);

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
        staff.setSector(this);

        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setSector(null);

        return staff;
    }

    public List<Attendance> getAttendances() {
        return this.attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendance addAttendance(Attendance attendance) {
        getAttendances().add(attendance);
        attendance.setSector(this);
        return attendance;
    }

    public Attendance removeAttendance(Attendance attendance) {
        getAttendances().remove(attendance);
        attendance.setSector(null);
        return attendance;
    }

    public List<Usr> getUsrs() {
        return usrs;
    }

    public void setUsrs(List<Usr> usrs) {
        this.usrs = usrs;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Sector)) {
            return false;
        }

        Sector compare = (Sector) obj;
        return compare.sectorid == (this.sectorid);
    }

    @Override
    public int hashCode() {
        return sectorid != 0 ? this.getClass().hashCode() + Long.hashCode(sectorid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Sector{id=" + sectorid + ", name=" + getName() + "}";
    }

}
