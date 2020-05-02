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
@Table(name = "\"ROLE\"")
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ROLE_ROLEID_GENERATOR", sequenceName = "ROLE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ROLEID_GENERATOR")
    private BigDecimal roleid;

    private String description;

    private String name;

    private BigDecimal ordered;

    //bi-directional many-to-one association to Userrole
    @OneToMany(mappedBy = "role")
    private List<Userroles> userroles;

    //bi-directional many-to-one association to User
    @OneToMany(mappedBy = "role")
    private List<Users> users;

    public Role() {
    }

    public BigDecimal getRoleid() {
        return this.roleid;
    }

    public void setRoleid(BigDecimal roleid) {
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

    public List<Userroles> getUserroles() {
        return this.userroles;
    }

    public void setUserroles(List<Userroles> userroles) {
        this.userroles = userroles;
    }

    public Userroles addUserrole(Userroles userrole) {
        getUserroles().add(userrole);
        userrole.setRole(this);

        return userrole;
    }

    public Userroles removeUserrole(Userroles userrole) {
        getUserroles().remove(userrole);
        userrole.setRole(null);

        return userrole;
    }

    public List<Users> getUsers() {
        return this.users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public Users addUser(Users user) {
        getUsers().add(user);
        user.setRole(this);

        return user;
    }

    public Users removeUser(Users user) {
        getUsers().remove(user);
        user.setRole(null);

        return user;
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
        return "Role{id=" + roleid + ", name=" + getName() + "}";
    }

}
