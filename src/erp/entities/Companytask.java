package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the COMPANYTASK database table.
 * 
 */
@Entity
@NamedQuery(name="Companytask.findAll", query="SELECT c FROM Companytask c")
public class Companytask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COMPANYTASK_ID_GENERATOR", sequenceName="COMPANYTASK_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPANYTASK_ID_GENERATOR")
	private long id;

	private BigDecimal active;

	private Timestamp createdtimestamp;

	private Timestamp modifiedtimestamp;

	private BigDecimal ordered;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Scheduletask
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TASKID")
	private Scheduletask scheduletask;

	//bi-directional many-to-one association to Scheduletaskdetail
	@OneToMany(mappedBy="companytask")
	private List<Scheduletaskdetail> scheduletaskdetails;

	public Companytask() {
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

	public Timestamp getCreatedtimestamp() {
		return this.createdtimestamp;
	}

	public void setCreatedtimestamp(Timestamp createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public Timestamp getModifiedtimestamp() {
		return this.modifiedtimestamp;
	}

	public void setModifiedtimestamp(Timestamp modifiedtimestamp) {
		this.modifiedtimestamp = modifiedtimestamp;
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

	public Scheduletask getScheduletask() {
		return this.scheduletask;
	}

	public void setScheduletask(Scheduletask scheduletask) {
		this.scheduletask = scheduletask;
	}

	public List<Scheduletaskdetail> getScheduletaskdetails() {
		return this.scheduletaskdetails;
	}

	public void setScheduletaskdetails(List<Scheduletaskdetail> scheduletaskdetails) {
		this.scheduletaskdetails = scheduletaskdetails;
	}

	public Scheduletaskdetail addScheduletaskdetail(Scheduletaskdetail scheduletaskdetail) {
		getScheduletaskdetails().add(scheduletaskdetail);
		scheduletaskdetail.setCompanytask(this);

		return scheduletaskdetail;
	}

	public Scheduletaskdetail removeScheduletaskdetail(Scheduletaskdetail scheduletaskdetail) {
		getScheduletaskdetails().remove(scheduletaskdetail);
		scheduletaskdetail.setCompanytask(null);

		return scheduletaskdetail;
	}

}