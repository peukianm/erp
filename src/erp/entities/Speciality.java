package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the SPECIALITY database table.
 *
 */
@Entity
@Table(name = "SPECIALITY")
@NamedQuery(name = "Speciality.findAll", query = "SELECT s FROM Speciality s")
public class Speciality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false)
    private long specialityid;

    private BigDecimal branchid;

    @Column(length = 20)
    private String code;

    @Column(name = "ACTIVE", nullable = false)
    private BigDecimal active;

    @Column(nullable = false, length = 60)
    private String name;

    private BigDecimal ordered;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "speciality")
    private List<Staff> staffs;

    public Speciality() {
    }

    public long getSpecialityid() {
        return this.specialityid;
    }

    public void setSpecialityid(long specialityid) {
        this.specialityid = specialityid;
    }

    public BigDecimal getBranchid() {
        return this.branchid;
    }

    public void setBranchid(BigDecimal branchid) {
        this.branchid = branchid;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal enable) {
        this.active = enable;
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
        staff.setSpeciality(this);

        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setSpeciality(null);

        return staff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Speciality)) {
            return false;
        }

        Speciality compare = (Speciality) obj;
        return compare.specialityid == (this.specialityid);
    }

    @Override
    public int hashCode() {
        return specialityid != 0 ? this.getClass().hashCode() + Long.hashCode(specialityid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Speciality{id=" + specialityid + ", name=" + getName() + "}";
    }

}
