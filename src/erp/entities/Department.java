package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the DEPARTMENT database table.
 *
 */
@Entity
@NamedQuery(name = "Department.findAll", query = "SELECT d FROM Department d")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DEPARTMENT_DEPARTMENTID_GENERATOR", sequenceName = "DEPARTMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_DEPARTMENTID_GENERATOR")
    private long departmentid;

    private BigDecimal active;

    @Column(name = "CREATED_TIMESTAMP")
    private Timestamp createdTimestamp;

    private String description;

    @Column(name = "MODIFIED_TIMESTAMP")
    private Timestamp modifiedTimestamp;

    private String name;

    //bi-directional many-to-one association to Attendance
    @OneToMany(mappedBy = "department")
    private List<Attendance> attendances;

    //bi-directional many-to-one association to Departmenttype
    @ManyToOne
    @JoinColumn(name = "TYPEID")
    private Departmenttype departmenttype;

    //bi-directional many-to-one association to Equipmentdepartment
    @OneToMany(mappedBy = "department")
    private List<Equipmentdepartment> equipmentdepartments;

    //bi-directional many-to-many association to Sector
    @ManyToMany(mappedBy = "departments")
    private List<Sector> sectors;

    //bi-directional many-to-one association to Sectordepartment
    @OneToMany(mappedBy = "department")
    private List<Sectordepartment> sectordepartments;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "department")
    private List<Staff> staffs;

    //bi-directional many-to-one association to Userdepartment
    @OneToMany(mappedBy = "department")
    private List<Userdepartment> userdepartments;

    //bi-directional many-to-one association to Usr
    @OneToMany(mappedBy = "department")
    private List<Usr> primaryUsers;

    //bi-directional many-to-many association to Usr
    @ManyToMany(mappedBy = "departments")
    private List<Usr> usrs;

    //bi-directional many-to-one association to Proadmission
    @OneToMany(mappedBy = "department")
    private List<Proadmission> proadmissions;

    public Department() {
    }

    public long getDepartmentid() {
        return this.departmentid;
    }

    public void setDepartmentid(long departmentid) {
        this.departmentid = departmentid;
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

    public List<Attendance> getAttendances() {
        return this.attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Attendance addAttendance(Attendance attendance) {
        getAttendances().add(attendance);
        attendance.setDepartment(this);

        return attendance;
    }

    public Attendance removeAttendance(Attendance attendance) {
        getAttendances().remove(attendance);
        attendance.setDepartment(null);

        return attendance;
    }

    public Departmenttype getDepartmenttype() {
        return this.departmenttype;
    }

    public void setDepartmenttype(Departmenttype departmenttype) {
        this.departmenttype = departmenttype;
    }

    public List<Equipmentdepartment> getEquipmentdepartments() {
        return this.equipmentdepartments;
    }

    public void setEquipmentdepartments(List<Equipmentdepartment> equipmentdepartments) {
        this.equipmentdepartments = equipmentdepartments;
    }

    public Equipmentdepartment addEquipmentdepartment(Equipmentdepartment equipmentdepartment) {
        getEquipmentdepartments().add(equipmentdepartment);
        equipmentdepartment.setDepartment(this);

        return equipmentdepartment;
    }

    public Equipmentdepartment removeEquipmentdepartment(Equipmentdepartment equipmentdepartment) {
        getEquipmentdepartments().remove(equipmentdepartment);
        equipmentdepartment.setDepartment(null);

        return equipmentdepartment;
    }

    public List<Sector> getSectors() {
        return this.sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public List<Sectordepartment> getSectordepartments() {
        return this.sectordepartments;
    }

    public void setSectordepartments(List<Sectordepartment> sectordepartments) {
        this.sectordepartments = sectordepartments;
    }

    public Sectordepartment addSectordepartment(Sectordepartment sectordepartment) {
        getSectordepartments().add(sectordepartment);
        sectordepartment.setDepartment(this);

        return sectordepartment;
    }

    public Sectordepartment removeSectordepartment(Sectordepartment sectordepartment) {
        getSectordepartments().remove(sectordepartment);
        sectordepartment.setDepartment(null);

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
        staff.setDepartment(this);

        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setDepartment(null);

        return staff;
    }

    public List<Userdepartment> getUserdepartments() {
        return this.userdepartments;
    }

    public void setUserdepartments(List<Userdepartment> userdepartments) {
        this.userdepartments = userdepartments;
    }

    public Userdepartment addUserdepartment(Userdepartment userdepartment) {
        getUserdepartments().add(userdepartment);
        userdepartment.setDepartment(this);

        return userdepartment;
    }

    public Userdepartment removeUserdepartment(Userdepartment userdepartment) {
        getUserdepartments().remove(userdepartment);
        userdepartment.setDepartment(null);

        return userdepartment;
    }

    public List<Usr> getUsrs() {
        return this.usrs;
    }

    public void setUsrs(List<Usr> usrs) {
        this.usrs = usrs;
    }

    public List<Usr> getPrimaryUsers() {
        return primaryUsers;
    }

    public void setPrimaryUsers(List<Usr> primaryUsers) {
        this.primaryUsers = primaryUsers;
    }

    public List<Proadmission> getProadmissions() {
        return this.proadmissions;
    }

    public void setProadmissions(List<Proadmission> proadmissions) {
        this.proadmissions = proadmissions;
    }

    public Proadmission addProadmission(Proadmission proadmission) {
        getProadmissions().add(proadmission);
        proadmission.setDepartment(this);

        return proadmission;
    }

    public Proadmission removeProadmission(Proadmission proadmission) {
        getProadmissions().remove(proadmission);
        proadmission.setDepartment(null);
        return proadmission;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Department)) {
            return false;
        }

        Department compare = (Department) obj;
        return compare.departmentid == (this.departmentid);
    }

    @Override
    public int hashCode() {
        return departmentid != 0 ? this.getClass().hashCode() + Long.hashCode(departmentid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Department{id=" + departmentid + ", name=" + getName() + "}";
    }

}
