package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the BRANCH database table.
 *
 */
@Entity
@Table(name = "BRANCH")
@NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch b")
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false)
    private long branchid;

    @Column(name = "ACTIVE", nullable = false)
    private BigDecimal active;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(length = 20)
    private String ordered;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "branch")
    private List<Staff> staffs;

    public Branch() {
    }

    public long getBranchid() {
        return this.branchid;
    }

    public void setBranchid(long branchid) {
        this.branchid = branchid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrdered() {
        return this.ordered;
    }

    public void setOrdered(String ordered) {
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
        staff.setBranch(this);
        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setBranch(null);
        return staff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Branch)) {
            return false;
        }

        Branch compare = (Branch) obj;
        return compare.branchid == (this.branchid);
    }

    @Override
    public int hashCode() {
        return branchid != 0 ? this.getClass().hashCode() + Long.hashCode(branchid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Branch{id=" + branchid + ", name=" + getName() + "}";
    }
}
