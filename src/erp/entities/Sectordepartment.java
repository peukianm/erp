package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the SECTORDEPARTMENT database table.
 * 
 */
@Entity
@NamedQuery(name="Sectordepartment.findAll", query="SELECT s FROM Sectordepartment s")
public class Sectordepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SECTORDEPARTMENT_ID_GENERATOR", sequenceName="SECTORDEPARTMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SECTORDEPARTMENT_ID_GENERATOR")
	private long id;

	private BigDecimal active;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	@Column(name="MODIFIED_TIMESTAMP")
	private Timestamp modifiedTimestamp;

	private BigDecimal ordered;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Department
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENTID")
	private Department department;

	//bi-directional many-to-one association to Sector
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SECTORID")
	private Sector sector;

	public Sectordepartment() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Timestamp getModifiedTimestamp() {
		return this.modifiedTimestamp;
	}

	public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public BigDecimal getOrdered() {
		return this.ordered;
	}

	public void setOrdered(BigDecimal ordered) {
		this.ordered = ordered;
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

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

}