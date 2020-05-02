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
    private BigDecimal departmentid;

    private BigDecimal active;

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp createdTimestamp;

    private String description;

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp modifiedTimestamp;

    private String name;

    //bi-directional many-to-one association to Departmenttype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPEID")
    private Departmenttype departmenttype;

    //bi-directional many-to-one association to Equipmentdepartment
    @OneToMany(mappedBy = "department")
    private List<Equipmentdepartment> equipmentdepartments;

    //bi-directional many-to-one association to User
    @OneToMany(mappedBy = "department")
    private List<Users> users;

    public Department() {
    }

    public BigDecimal getDepartmentid() {
        return this.departmentid;
    }

    public void setDepartmentid(BigDecimal departmentid) {
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

    public List<Users> getUsers() {
        return this.users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public Users addUser(Users user) {
        getUsers().add(user);
        user.setDepartment(this);

        return user;
    }

    public Users removeUser(Users user) {
        getUsers().remove(user);
        user.setDepartment(null);

        return user;
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
        return compare.departmentid.equals(this.departmentid);
    }

    @Override
    public int hashCode() {
        return departmentid != null ? this.getClass().hashCode() + departmentid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Department{id=" + departmentid + ", name=" + getName() + "}";
    }

}
