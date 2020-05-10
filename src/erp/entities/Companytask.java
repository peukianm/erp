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

	private Timestamp lastexecutiontime;

	private Timestamp modifiedtimestamp;

	private BigDecimal ordered;

	private String taskdata1;

	private String taskdata2;

	private String taskdata3;

	private String taskdata4;

	private String taskdata5;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Scheduletask
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TASKID")
	private Scheduletask scheduletask;

	//bi-directional many-to-one association to Scheduletaskdetail
	@OneToMany(mappedBy="companytask", fetch=FetchType.EAGER)
	private List<Scheduletaskdetail> scheduletaskdetails;

	//bi-directional many-to-one association to Taskstatus
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="LASTEXECUTIONSTATUTUS")
	private Taskstatus taskstatus;

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

	public Timestamp getLastexecutiontime() {
		return this.lastexecutiontime;
	}

	public void setLastexecutiontime(Timestamp lastexecutiontime) {
		this.lastexecutiontime = lastexecutiontime;
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

	public String getTaskdata1() {
		return this.taskdata1;
	}

	public void setTaskdata1(String taskdata1) {
		this.taskdata1 = taskdata1;
	}

	public String getTaskdata2() {
		return this.taskdata2;
	}

	public void setTaskdata2(String taskdata2) {
		this.taskdata2 = taskdata2;
	}

	public String getTaskdata3() {
		return this.taskdata3;
	}

	public void setTaskdata3(String taskdata3) {
		this.taskdata3 = taskdata3;
	}

	public String getTaskdata4() {
		return this.taskdata4;
	}

	public void setTaskdata4(String taskdata4) {
		this.taskdata4 = taskdata4;
	}

	public String getTaskdata5() {
		return this.taskdata5;
	}

	public void setTaskdata5(String taskdata5) {
		this.taskdata5 = taskdata5;
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

	public Taskstatus getTaskstatus() {
		return this.taskstatus;
	}

	public void setTaskstatus(Taskstatus taskstatus) {
		this.taskstatus = taskstatus;
	}

}