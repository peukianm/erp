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
@Table(name="\"ROLE\"")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROLE_ROLEID_GENERATOR", sequenceName="ROLE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_ROLEID_GENERATOR")
	private long roleid;

	private String description;

	private String name;

	private BigDecimal ordered;

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="role")
	private List<Userrole> userroles;

	//bi-directional many-to-one association to Usr
	@OneToMany(mappedBy="role")
	private List<Usr> usrs1;

	//bi-directional many-to-many association to Usr
	@ManyToMany(mappedBy="roles")
	private List<Usr> usrs2;

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

	public List<Usr> getUsrs1() {
		return this.usrs1;
	}

	public void setUsrs1(List<Usr> usrs1) {
		this.usrs1 = usrs1;
	}

	public Usr addUsrs1(Usr usrs1) {
		getUsrs1().add(usrs1);
		usrs1.setRole(this);

		return usrs1;
	}

	public Usr removeUsrs1(Usr usrs1) {
		getUsrs1().remove(usrs1);
		usrs1.setRole(null);

		return usrs1;
	}

	public List<Usr> getUsrs2() {
		return this.usrs2;
	}

	public void setUsrs2(List<Usr> usrs2) {
		this.usrs2 = usrs2;
	}

}