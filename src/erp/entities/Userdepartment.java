package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the USERDEPARTMENT database table.
 * 
 */
@Entity
@NamedQuery(name="Userdepartment.findAll", query="SELECT u FROM Userdepartment u")
public class Userdepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USERDEPARTMENT_ID_GENERATOR", sequenceName="USERDEPARTMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USERDEPARTMENT_ID_GENERATOR")
	private long id;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	@Column(name="MODIFIED_TIMESTAMP")
	private Timestamp modifiedTimestamp;

	//bi-directional many-to-one association to Department
	@ManyToOne
	@JoinColumn(name="DEPARTMENTID")
	private Department department;

	//bi-directional many-to-one association to Usr
	@ManyToOne
	@JoinColumn(name="USERID")
	private Usr usr;

	public Userdepartment() {
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

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Usr getUsr() {
		return this.usr;
	}

	public void setUsr(Usr usr) {
		this.usr = usr;
	}

}