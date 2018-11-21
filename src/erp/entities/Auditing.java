package erp.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Auditing entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUDITING", schema = "SKELETON")
public class Auditing implements java.io.Serializable {

	// Fields

	private BigDecimal auditingid;
	private Action action;
	private Company company;
	private Users users;
	private String comments;
	private Timestamp actiondate;
	private Timestamp actiontime;

	// Constructors

	/** default constructor */
	public Auditing() {
	}

	/** minimal constructor */
	public Auditing(BigDecimal auditingid, Action action, Users users, Timestamp actiondate) {
		this.auditingid = auditingid;
		this.action = action;
		this.users = users;
		this.actiondate = actiondate;
	}

	/** full constructor */
	public Auditing(BigDecimal auditingid, Action action, Company company, Users users,
			String comments, Timestamp actiondate, Timestamp actiontime) {
		this.auditingid = auditingid;
		this.action = action;
		this.company = company;
		this.users = users;
		this.comments = comments;
		this.actiondate = actiondate;
		this.actiontime = actiontime;
	}

	// Property accessors
	@Id
	@Column(name = "AUDITINGID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getAuditingid() {
		return this.auditingid;
	}

	public void setAuditingid(BigDecimal auditingid) {
		this.auditingid = auditingid;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIONID", nullable = false)
	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANYID")
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "COMMENTS", length = 400)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	//@Temporal(TemporalType.DATE)
	@Column(name = "ACTIONDATE", nullable = false, length = 7)
	public Timestamp getActiondate() {
		return this.actiondate;
	}

	public void setActiondate(Timestamp actiondate) {
		this.actiondate = actiondate;
	}

	@Column(name = "ACTIONTIME", length = 11)
	public Timestamp getActiontime() {
		return this.actiontime;
	}

	public void setActiontime(Timestamp actiontime) {
		this.actiontime = actiontime;
	}

}