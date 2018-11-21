package erp.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Userroles entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "USERROLES", schema = "SKELETON")
@SequenceGenerator(name = "SEQ_USERROLES", sequenceName = "USERROLES_SEQ", allocationSize = 1)
public class Userroles implements java.io.Serializable {

    // Fields
    private BigDecimal id;
    private Users users;
    private Role role;
    private BigDecimal primary;
    private Timestamp createdTimestamp;
    private Timestamp modifiedTimestamp;

    // Constructors
    /**
     * default constructor
     */
    public Userroles() {
    }

    /**
     * minimal constructor
     */
    public Userroles(BigDecimal id, Users users, Role role) {
        this.id = id;
        this.users = users;
        this.role = role;
    }

    /**
     * full constructor
     */
    public Userroles(BigDecimal id, Users users, Role role, BigDecimal primary, Timestamp createdTimestamp, Timestamp modifiedTimestamp) {
        this.id = id;
        this.users = users;
        this.role = role;
        this.primary = primary;
        this.createdTimestamp = createdTimestamp;
        this.modifiedTimestamp = modifiedTimestamp;
    }

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERROLES")
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID", nullable = false)
    public Users getUsers() {
        return this.users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLEID", nullable = false)
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(name = "PRIMARY", precision = 22, scale = 0)
    public BigDecimal getPrimary() {
        return this.primary;
    }

    public void setPrimary(BigDecimal primary) {
        this.primary = primary;
    }
    
    @Column(name = "CREATED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Column(name = "MODIFIED_TIMESTAMP", length = 11, insertable = false, updatable = true)
    public Timestamp getModifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Userroles)) {
            return false;
        }

        Userroles compare = (Userroles) obj;

        return compare.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id != null ? this.getClass().hashCode() + id.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "UserRole{" + "id=" + id + ", role=" + getRole().getName() + " user=" + getUsers().getName() + "}";
    }
}