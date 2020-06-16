package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the USERROLE database table.
 * 
 */
@Entity
@NamedQuery(name="Userrole.findAll", query="SELECT u FROM Userrole u")
public class Userrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USERROLE_ID_GENERATOR", sequenceName="USERROLE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USERROLE_ID_GENERATOR")
	private long id;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	@Column(name="MODIFIED_TIMESTAMP")
	private Timestamp modifiedTimestamp;

	@Column(name="\"PRIMARY\"")
	private BigDecimal primary;

	//bi-directional many-to-one association to Role
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ROLEID")
	private Role role;

	//bi-directional many-to-one association to Usr
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="USERID")
	private Usr usr;

	public Userrole() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreatedTimestamp() {
		return this.createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Timestamp getModifiedTimestamp() {
		return this.modifiedTimestamp;
	}

	public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public BigDecimal getPrimary() {
		return this.primary;
	}

	public void setPrimary(BigDecimal primary) {
		this.primary = primary;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Usr getUsr() {
		return this.usr;
	}

	public void setUsr(Usr usr) {
		this.usr = usr;
	}

}