package erp.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the AUDITING database table.
 *
 */
@Entity
@NamedQuery(name = "Auditing.findAll", query = "SELECT a FROM Auditing a")
public class Auditing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "AUDITING_AUDITINGID_GENERATOR", sequenceName = "AUDITING_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDITING_AUDITINGID_GENERATOR")
    private BigDecimal auditingid;

    //@Temporal(TemporalType.DATE)
    @Column(name = "ACTIONDATE", nullable = false, length = 7)
    private Timestamp actiondate;

    private Timestamp actiontime;

    private String comments;

    //bi-directional many-to-one association to Action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIONID")
    private Action action;

    //bi-directional many-to-one association to Company
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANYID")
    private Company company;

    //bi-directional many-to-one association to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    private Users users;

    public Auditing() {
    }

    public BigDecimal getAuditingid() {
        return this.auditingid;
    }

    public void setAuditingid(BigDecimal auditingid) {
        this.auditingid = auditingid;
    }

    public Timestamp getActiondate() {
        return this.actiondate;
    }

    public void setActiondate(Timestamp actiondate) {
        this.actiondate = actiondate;
    }

    public Timestamp getActiontime() {
        return this.actiontime;
    }

    public void setActiontime(Timestamp actiontime) {
        this.actiontime = actiontime;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Users getUsers() {
        return this.users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Auditing)) {
            return false;
        }

        Auditing compare = (Auditing) obj;
        return compare.auditingid.equals(this.auditingid);
    }

    @Override
    public int hashCode() {
        return auditingid != null ? this.getClass().hashCode() + auditingid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Audit{id=" + auditingid + ", user name=" + getUsers().getSurname() + "}";
    }

}
