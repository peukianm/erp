package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the "ROLE" database table.
 *
 */
@Entity
@Table(name = "ROLE")
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ROLE_ROLEID_GENERATOR", sequenceName = "ROLE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ROLEID_GENERATOR")
    private long roleid;

    private String description;

    private String name;

    private BigDecimal ordered;

    //bi-directional many-to-one association to Userrole
    @OneToMany(mappedBy = "role")
    private List<Userrole> userroles;

    //bi-directional many-to-many association to Usr
    @ManyToMany(mappedBy = "roles")
    private List<Usr> usrs;

    public Role() {
    }

    public long getRoleid() {
        return this.roleid;
    }

    public void setRoleid(long roleid) {
        this.roleid = roleid;
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

    public List<Userrole> getUserroles() {
        return this.userroles;
    }

    public void setUserroles(List<Userrole> userroles) {
        this.userroles = userroles;
    }

    public Userrole addUserrole(Userrole userrole) {
        getUserroles().add(userrole);
        userrole.setRole(this);

        return userrole;
    }

    public Userrole removeUserrole(Userrole userrole) {
        getUserroles().remove(userrole);
        userrole.setRole(null);

        return userrole;
    }

    public List<Usr> getUsrs() {
        return this.usrs;
    }

    public void setUsrs(List<Usr> usrs) {
        this.usrs = usrs;
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
        return compare.roleid == (this.roleid);
    }

    @Override
    public int hashCode() {
        return roleid != 0 ? this.getClass().hashCode() + Long.hashCode(roleid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Role{id=" + roleid + ", name=" + getName() + "}";
    }

}
