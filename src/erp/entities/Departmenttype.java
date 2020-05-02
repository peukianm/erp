package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the DEPARTMENTTYPE database table.
 *
 */
@Entity
@NamedQuery(name = "Departmenttype.findAll", query = "SELECT d FROM Departmenttype d")
public class Departmenttype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DEPARTMENTTYPE_TYPEID_GENERATOR", sequenceName = "DEPARTMENTTYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENTTYPE_TYPEID_GENERATOR")
    private BigDecimal typeid;

    private BigDecimal active;

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp createdTimestamp;

    private String description;

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp modifiedTimestamp;

    private String name;

    //bi-directional many-to-one association to Department
    @OneToMany(mappedBy = "departmenttype")
    private List<Department> departments;

    public Departmenttype() {
    }

    public BigDecimal getTypeid() {
        return this.typeid;
    }

    public void setTypeid(BigDecimal typeid) {
        this.typeid = typeid;
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

    public List<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public Department addDepartment(Department department) {
        getDepartments().add(department);
        department.setDepartmenttype(this);

        return department;
    }

    public Department removeDepartment(Department department) {
        getDepartments().remove(department);
        department.setDepartmenttype(null);

        return department;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Departmenttype)) {
            return false;
        }

        Departmenttype compare = (Departmenttype) obj;
        return compare.typeid.equals(this.typeid);
    }

    @Override
    public int hashCode() {
        return typeid != null ? this.getClass().hashCode() + typeid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Department type{id=" + typeid + ", name=" + getName() + "}";
    }

}
