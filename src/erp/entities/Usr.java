package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the USR database table.
 * 
 */
@Entity
@NamedQuery(name="Usr.findAll", query="SELECT u FROM Usr u")
public class Usr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USR_USERID_GENERATOR", sequenceName="USR_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USR_USERID_GENERATOR")
	private long userid;

	private BigDecimal active;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	private String description;

	private String email;

	@Column(name="MODIFIED_TIMESTAMP")
	private Timestamp modifiedTimestamp;

	private String name;

	private String password;

	private String phone;

	private String surname;

	private String username;

	//bi-directional many-to-one association to Auditing
	@OneToMany(mappedBy="usr")
	private List<Auditing> auditings;

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="usr")
	private List<Userrole> userroles;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Department
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENTID")
	private Department department;

	//bi-directional many-to-one association to Role
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ROLEID")
	private Role role;

	//bi-directional many-to-many association to Role
	@ManyToMany
	@JoinTable(
		name="USERROLE"
		, joinColumns={
			@JoinColumn(name="USERID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ROLEID")
			}
		)
	private List<Role> roles;

	public Usr() {
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public BigDecimal getActive() {
		return this.active;
	}

	public void setActive(BigDecimal active) {
		this.active = active;
	}

	public Timestamp getCreatedTimestamp() {
		return this.createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getModifiedTimestamp() {
		return this.modifiedTimestamp;
	}

	public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Auditing> getAuditings() {
		return this.auditings;
	}

	public void setAuditings(List<Auditing> auditings) {
		this.auditings = auditings;
	}

	public Auditing addAuditing(Auditing auditing) {
		getAuditings().add(auditing);
		auditing.setUsr(this);

		return auditing;
	}

	public Auditing removeAuditing(Auditing auditing) {
		getAuditings().remove(auditing);
		auditing.setUsr(null);

		return auditing;
	}

	public List<Userrole> getUserroles() {
		return this.userroles;
	}

	public void setUserroles(List<Userrole> userroles) {
		this.userroles = userroles;
	}

	public Userrole addUserrole(Userrole userrole) {
		getUserroles().add(userrole);
		userrole.setUsr(this);

		return userrole;
	}

	public Userrole removeUserrole(Userrole userrole) {
		getUserroles().remove(userrole);
		userrole.setUsr(null);

		return userrole;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}