package erp.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Role entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ROLE", schema = "SKELETON")
public class Role implements java.io.Serializable {

    // Fields
    private BigDecimal roleid;
    private String name;
    private String description;
    private BigDecimal ordered;
    private Set<Userroles> userroleses = new HashSet<Userroles>(0);
    private Set<Users> userses = new HashSet<Users>(0);

    // Constructors
    /**
     * default constructor
     */
    public Role() {
    }

    /**
     * minimal constructor
     */
    public Role(BigDecimal roleid, String name) {
        this.roleid = roleid;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Role(BigDecimal roleid, String name, String description, BigDecimal ordered, Set<Userroles> userroleses, Set<Users> userses) {
        this.roleid = roleid;
        this.name = name;
        this.description = description;
        this.ordered = ordered;
        this.userroleses = userroleses;
        this.userses = userses;
    }

    // Property accessors
    @Id
    @Column(name = "ROLEID", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getRoleid() {
        return this.roleid;
    }

    public void setRoleid(BigDecimal roleid) {
        this.roleid = roleid;
    }

    @Column(name = "NAME", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION", length = 200)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDERED", precision = 22, scale = 0)
    public BigDecimal getOrdered() {
        return this.ordered;
    }

    public void setOrdered(BigDecimal ordered) {
        this.ordered = ordered;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
    public Set<Userroles> getUserroleses() {
        return this.userroleses;
    }

    public void setUserroleses(Set<Userroles> userroleses) {
        this.userroleses = userroleses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
    public Set<Users> getUserses() {
        return this.userses;
    }

    public void setUserses(Set<Users> userses) {
        this.userses = userses;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Role)) {
            return false;
        }

        Role compare = (Role) obj;

        return compare.roleid.equals(this.roleid);
    }

    @Override
    public int hashCode() {
        return roleid != null ? this.getClass().hashCode() + roleid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Role{" + "roleid=" + roleid + ", name=" + getName() + "}";
    }
}