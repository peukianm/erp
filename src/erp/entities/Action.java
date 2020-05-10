package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the "ACTION" database table.
 * 
 */
@Entity
@Table(name="ACTION")
@NamedQuery(name="Action.findAll", query="SELECT a FROM Action a")
public class Action implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ACTION_ACTIONID_GENERATOR", sequenceName="ACTION_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACTION_ACTIONID_GENERATOR")
	private long actionid;

	private String name;

	//bi-directional many-to-one association to Actionscategory
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CATEGORYID")
	private Actionscategory actionscategory;

	//bi-directional many-to-one association to Auditing
	@OneToMany(mappedBy="action")
	private List<Auditing> auditings;

	public Action() {
	}

	public long getActionid() {
		return this.actionid;
	}

	public void setActionid(long actionid) {
		this.actionid = actionid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Actionscategory getActionscategory() {
		return this.actionscategory;
	}

	public void setActionscategory(Actionscategory actionscategory) {
		this.actionscategory = actionscategory;
	}

	public List<Auditing> getAuditings() {
		return this.auditings;
	}

	public void setAuditings(List<Auditing> auditings) {
		this.auditings = auditings;
	}

	public Auditing addAuditing(Auditing auditing) {
		getAuditings().add(auditing);
		auditing.setAction(this);

		return auditing;
	}

	public Auditing removeAuditing(Auditing auditing) {
		getAuditings().remove(auditing);
		auditing.setAction(null);

		return auditing;
	}

}