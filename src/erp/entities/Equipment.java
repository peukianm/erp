package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the EQUIPMENT database table.
 * 
 */
@Entity
@NamedQuery(name="Equipment.findAll", query="SELECT e FROM Equipment e")
public class Equipment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EQUIPMENT_EQUIPMENTID_GENERATOR", sequenceName="EQUIPMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EQUIPMENT_EQUIPMENTID_GENERATOR")
	private long equipmentid;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	private String description;

	@Column(name="MODIFIED_TIMESTAMP")
	private Timestamp modifiedTimestamp;

	private String name;

	//bi-directional many-to-many association to Department
	@ManyToMany
	@JoinTable(
		name="EQUIPMENTDEPARTMENT"
		, joinColumns={
			@JoinColumn(name="EQUIPMENTID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="DEPARTMENTID")
			}
		)
	private List<Department> departments;

	//bi-directional many-to-one association to Equipmenttype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TYPEID")
	private Equipmenttype equipmenttype;

	//bi-directional many-to-one association to Equipmentdepartment
	@OneToMany(mappedBy="equipment")
	private List<Equipmentdepartment> equipmentdepartments;

	public Equipment() {
	}

	public long getEquipmentid() {
		return this.equipmentid;
	}

	public void setEquipmentid(long equipmentid) {
		this.equipmentid = equipmentid;
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

	public Equipmenttype getEquipmenttype() {
		return this.equipmenttype;
	}

	public void setEquipmenttype(Equipmenttype equipmenttype) {
		this.equipmenttype = equipmenttype;
	}

	public List<Equipmentdepartment> getEquipmentdepartments() {
		return this.equipmentdepartments;
	}

	public void setEquipmentdepartments(List<Equipmentdepartment> equipmentdepartments) {
		this.equipmentdepartments = equipmentdepartments;
	}

	public Equipmentdepartment addEquipmentdepartment(Equipmentdepartment equipmentdepartment) {
		getEquipmentdepartments().add(equipmentdepartment);
		equipmentdepartment.setEquipment(this);

		return equipmentdepartment;
	}

	public Equipmentdepartment removeEquipmentdepartment(Equipmentdepartment equipmentdepartment) {
		getEquipmentdepartments().remove(equipmentdepartment);
		equipmentdepartment.setEquipment(null);

		return equipmentdepartment;
	}

}