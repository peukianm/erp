package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the FAMILYSTATUS database table.
 *
 */
@Entity
@Table(name = "FAMILYSTATUS")
@NamedQuery(name = "Familystatus.findAll", query = "SELECT f FROM Familystatus f")
public class Familystatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false)
    private long familystatusid;

    @Column(length = 80)
    private String description;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false)
    private BigDecimal ordered;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "familystatus")
    private List<Staff> staffs;

    public Familystatus() {
    }

    public long getFamilystatusid() {
        return this.familystatusid;
    }

    public void setFamilystatusid(long familystatusid) {
        this.familystatusid = familystatusid;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<Staff> getStaffs() {
        return this.staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }

    public Staff addStaff(Staff staff) {
        getStaffs().add(staff);
        staff.setFamilystatus(this);

        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setFamilystatus(null);

        return staff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Familystatus)) {
            return false;
        }

        Familystatus compare = (Familystatus) obj;
        return compare.familystatusid == (this.familystatusid);
    }

    @Override
    public int hashCode() {
        return familystatusid != 0 ? this.getClass().hashCode() + Long.hashCode(familystatusid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Familystatus{id=" + familystatusid + ", name=" + getName() + "}";
    }

}
