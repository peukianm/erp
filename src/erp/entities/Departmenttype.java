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
@NamedQuery(name="Departmenttype.findAll", query="SELECT d FROM Departmenttype d")
public class Departmenttype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEPARTMENTTYPE_TYPEID_GENERATOR", sequenceName="DEPARTMENTTYPE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEPARTMENTTYPE_TYPEID_GENERATOR")
	private long typeid;

	private BigDecimal active;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	private String description;

	@Column(name="MODIFIED_TIMESTAMP")
	private Timestamp modifiedTimestamp;

	private String name;

	//bi-directional many-to-one association to Department
	@OneToMany(mappedBy="departmenttype")
	private List<Department> departments;

	public Departmenttype() {
	}

	public long getTypeid() {
		return this.typeid;
	}

	public void setTypeid(long typeid) {
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

}