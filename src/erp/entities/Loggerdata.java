package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the LOGGERDATA database table.
 * 
 */
@Entity
@NamedQuery(name="Loggerdata.findAll", query="SELECT l FROM Loggerdata l")
public class Loggerdata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOGGERDATA_LDATAID_GENERATOR", sequenceName="LOGGERDATA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOGGERDATA_LDATAID_GENERATOR")
	private long ldataid;

	private Timestamp entrydate;

	private BigDecimal entrytype;

	private String loggerid;

	private BigDecimal procceded;

	private String usercode;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Usr
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USERID")
	private Usr usr;

	public Loggerdata() {
	}

	public long getLdataid() {
		return this.ldataid;
	}

	public void setLdataid(long ldataid) {
		this.ldataid = ldataid;
	}

	public Timestamp getEntrydate() {
		return this.entrydate;
	}

	public void setEntrydate(Timestamp entrydate) {
		this.entrydate = entrydate;
	}

	public BigDecimal getEntrytype() {
		return this.entrytype;
	}

	public void setEntrytype(BigDecimal entrytype) {
		this.entrytype = entrytype;
	}

	public String getLoggerid() {
		return this.loggerid;
	}

	public void setLoggerid(String loggerid) {
		this.loggerid = loggerid;
	}

	public BigDecimal getProcceded() {
		return this.procceded;
	}

	public void setProcceded(BigDecimal procceded) {
		this.procceded = procceded;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Usr getUsr() {
		return this.usr;
	}

	public void setUsr(Usr usr) {
		this.usr = usr;
	}

}