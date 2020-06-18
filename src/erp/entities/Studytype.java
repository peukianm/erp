package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the STUDYTYPE database table.
 *
 */
@Entity
@Table(name = "STUDYTYPE")
@NamedQuery(name = "Studytype.findAll", query = "SELECT s FROM Studytype s")
public class Studytype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "STUDYTYPE_STUDYTYPEID_GENERATOR", sequenceName = "STUDYTYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STUDYTYPE_STUDYTYPEID_GENERATOR")
    @Column(unique = true, nullable = false)
    private long studytypeid;

    @Column(length = 20)
    private String code;

    @Column(name = "ACTIVE", nullable = false)
    private BigDecimal active;

    @Column(nullable = false, length = 70)
    private String name;

    private BigDecimal ordered;

    //bi-directional many-to-one association to Staff
    @OneToMany(mappedBy = "studytype")
    private List<Staff> staffs;

    public Studytype() {
    }

    public long getStudytypeid() {
        return this.studytypeid;
    }

    public void setStudytypeid(long studytypeid) {
        this.studytypeid = studytypeid;
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
        this.active = active;
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
        staff.setStudytype(this);

        return staff;
    }

    public Staff removeStaff(Staff staff) {
        getStaffs().remove(staff);
        staff.setStudytype(null);

        return staff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Studytype)) {
            return false;
        }

        Studytype compare = (Studytype) obj;
        return compare.studytypeid == (this.studytypeid);
    }

    @Override
    public int hashCode() {
        return studytypeid != 0 ? this.getClass().hashCode() + Long.hashCode(studytypeid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Studytype{id=" + studytypeid + ", surname=" + getName() + "}";
    }

}
