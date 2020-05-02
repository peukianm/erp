package erp.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the EQUIPMENTDEPARTMENT database table.
 * 
 */
@Entity
@NamedQuery(name="Equipmentdepartment.findAll", query="SELECT e FROM Equipmentdepartment e")
public class Equipmentdepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EQUIPMENTDEPARTMENT_ID_GENERATOR", sequenceName="EQUIPMENTDEPARTMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EQUIPMENTDEPARTMENT_ID_GENERATOR")
	private BigDecimal id;

	
        @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
	private Timestamp createdTimestamp;

	 @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
	private Timestamp modifiedTimestamp;

	//bi-directional many-to-one association to Department
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENTID")
	private Department department;

	//bi-directional many-to-one association to Equipment
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EQUIPMENTID")
	private Equipment equipment;

	public Equipmentdepartment() {
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Timestamp getCreatedTimestamp() {
		return this.createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Timestamp getModifiedTimestamp() {
		return this.modifiedTimestamp;
	}

	public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Equipment getEquipment() {
		return this.equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
        
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Equipmentdepartment)) {
            return false;
        }

        Equipmentdepartment compare = (Equipmentdepartment) obj;
        return compare.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id != null ? this.getClass().hashCode() + id.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Equipment Department{id=" + id + "}";
    }

}