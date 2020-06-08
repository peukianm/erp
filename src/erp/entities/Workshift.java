package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the WORKSHIFT database table.
 *
 */
@Entity
@NamedQuery(name = "Workshift.findAll", query = "SELECT w FROM Workshift w")
public class Workshift implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "WORKSHIFT_SHIFTID_GENERATOR", sequenceName = "WORKSHIFT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKSHIFT_SHIFTID_GENERATOR")
    private long shiftid;

    private BigDecimal active;

    private String description;

    private String name;

    private BigDecimal ordered;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "workshift")
    private List<Staff> staffs;

    public Workshift() {
    }

    public long getShiftid() {
        return this.shiftid;
    }

    public void setShiftid(long shiftid) {
        this.shiftid = shiftid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
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
        staff.setWorkshift(this);
        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setWorkshift(null);
        return staff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Workshift)) {
            return false;
        }

        Workshift compare = (Workshift) obj;
        return compare.shiftid == (this.shiftid);
    }

    @Override
    public int hashCode() {
        return shiftid != 0 ? this.getClass().hashCode() + Long.hashCode(shiftid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Workshift{id=" + shiftid + ", name=" + getName() + "}";
    }

}
