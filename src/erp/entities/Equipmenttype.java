package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the EQUIPMENTTYPE database table.
 *
 */
@Entity
@NamedQuery(name = "Equipmenttype.findAll", query = "SELECT e FROM Equipmenttype e")
public class Equipmenttype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "EQUIPMENTTYPE_TYPEID_GENERATOR", sequenceName = "EQUIPMENTTYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EQUIPMENTTYPE_TYPEID_GENERATOR")
    private BigDecimal typeid;

    private BigDecimal active;

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp createdTimestamp;

    private String description;

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp modifiedTimestamp;

    private String name;

    //bi-directional many-to-one association to Equipment
    @OneToMany(mappedBy = "equipmenttype")
    private List<Equipment> equipments;

    //bi-directional many-to-one association to Equipmentcategory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORYID")
    private Equipmentcategory equipmentcategory;

    public Equipmenttype() {
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

    public List<Equipment> getEquipments() {
        return this.equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public Equipment addEquipment(Equipment equipment) {
        getEquipments().add(equipment);
        equipment.setEquipmenttype(this);

        return equipment;
    }

    public Equipment removeEquipment(Equipment equipment) {
        getEquipments().remove(equipment);
        equipment.setEquipmenttype(null);

        return equipment;
    }

    public Equipmentcategory getEquipmentcategory() {
        return this.equipmentcategory;
    }

    public void setEquipmentcategory(Equipmentcategory equipmentcategory) {
        this.equipmentcategory = equipmentcategory;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Equipmenttype)) {
            return false;
        }

        Equipmenttype compare = (Equipmenttype) obj;
        return compare.typeid.equals(this.typeid);
    }

    @Override
    public int hashCode() {
        return typeid != null ? this.getClass().hashCode() + typeid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "EquipmentType{id=" + typeid + ", name=" + getName() + "}";
    }

}
