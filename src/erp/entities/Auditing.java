package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the AUDITING database table.
 * 
 */
@Entity
@NamedQuery(name="Auditing.findAll", query="SELECT a FROM Auditing a")
public class Auditing implements Serializable {
	private static final long serialVersionUID = 1L;

        public Auditing(Usr user, Company company, Action action, String comments, Timestamp actioDate,  Timestamp actionTime){
            this.usr = user;
            this.company = company;
            this.action = action;
            this.comments = comments;
            this.actiondate = actioDate; 
            this.actiontime = actionTime;
        }
        
        
	@Id
	@SequenceGenerator(name="AUDITING_AUDITINGID_GENERATOR", sequenceName="AUDITING_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUDITING_AUDITINGID_GENERATOR")
	private long auditingid;

	@Temporal(TemporalType.DATE)
	private Date actiondate;

	private Timestamp actiontime;

	private String comments;

	//bi-directional many-to-one association to Action
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ACTIONID")
	private Action action;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Usr
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USERID")
	private Usr usr;

	public Auditing() {
	}

	public long getAuditingid() {
		return this.auditingid;
	}

	public void setAuditingid(long auditingid) {
		this.auditingid = auditingid;
	}

	public Date getActiondate() {
		return this.actiondate;
	}

	public void setActiondate(Date actiondate) {
		this.actiondate = actiondate;
	}

	public Timestamp getActiontime() {
		return this.actiontime;
	}

	public void setActiontime(Timestamp actiontime) {
		this.actiontime = actiontime;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
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