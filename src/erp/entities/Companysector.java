package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the COMPANYSECTOR database table.
 * 
 */
@Entity
@NamedQuery(name="Companysector.findAll", query="SELECT c FROM Companysector c")
public class Companysector implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COMPANYSECTOR_ID_GENERATOR", sequenceName="COMPANYSECTORDEPARTMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPANYSECTOR_ID_GENERATOR")
	private long id;

	private BigDecimal active;

	@Column(name="CREATED_TIMESTAMP")
	private Timestamp createdTimestamp;

	@Column(name="MODIFIED_TIMESTAMP")
	private String modifiedTimestamp;

	private BigDecimal ordered;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Sector
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SECTORID")
	private Sector sector;

	public Companysector() {
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

	public String getModifiedTimestamp() {
		return this.modifiedTimestamp;
	}

	public void setModifiedTimestamp(String modifiedTimestamp) {
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

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

}