package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the EQUIPMENTCATEGORY database table.
 *
 */
@Entity
@NamedQuery(name = "Equipmentcategory.findAll", query = "SELECT e FROM Equipmentcategory e")
public class Equipmentcategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "EQUIPMENTCATEGORY_CATEGORYID_GENERATOR", sequenceName = "EQUIPMENTCATEGORY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EQUIPMENTCATEGORY_CATEGORYID_GENERATOR")
    private BigDecimal categoryid;

    private BigDecimal active;

    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp createdTimestamp;

    private String description;

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    private Timestamp modifiedTimestamp;

    private String name;

    //bi-directional many-to-one association to Equipmenttype
    @OneToMany(mappedBy = "equipmentcategory")
    private List<Equipmenttype> equipmenttypes;

    public Equipmentcategory() {
    }

    public BigDecimal getCategoryid() {
        return this.categoryid;
    }

    public void setCategoryid(BigDecimal categoryid) {
        this.categoryid = categoryid;
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

    public List<Equipmenttype> getEquipmenttypes() {
        return this.equipmenttypes;
    }

    public void setEquipmenttypes(List<Equipmenttype> equipmenttypes) {
        this.equipmenttypes = equipmenttypes;
    }

    public Equipmenttype addEquipmenttype(Equipmenttype equipmenttype) {
        getEquipmenttypes().add(equipmenttype);
        equipmenttype.setEquipmentcategory(this);

        return equipmenttype;
    }

    public Equipmenttype removeEquipmenttype(Equipmenttype equipmenttype) {
        getEquipmenttypes().remove(equipmenttype);
        equipmenttype.setEquipmentcategory(null);

        return equipmenttype;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Equipmentcategory)) {
            return false;
        }

        Equipmentcategory compare = (Equipmentcategory) obj;
        return compare.categoryid.equals(this.categoryid);
    }

    @Override
    public int hashCode() {
        return categoryid != null ? this.getClass().hashCode() + categoryid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "EquiomentCategory{id=" + categoryid + ", name=" + getName() + "}";
    }

}
