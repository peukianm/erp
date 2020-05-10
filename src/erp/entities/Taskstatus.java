package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TASKSTATUS database table.
 * 
 */
@Entity
@NamedQuery(name="Taskstatus.findAll", query="SELECT t FROM Taskstatus t")
public class Taskstatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TASKSTATUS_STATUSID_GENERATOR", sequenceName="TASKSTATUS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TASKSTATUS_STATUSID_GENERATOR")
	private long statusid;

	private String description;

	private String name;

	//bi-directional many-to-one association to Companytask
	@OneToMany(mappedBy="taskstatus")
	private List<Companytask> companytasks;

	//bi-directional many-to-one association to Scheduletaskdetail
	@OneToMany(mappedBy="taskstatus")
	private List<Scheduletaskdetail> scheduletaskdetails;

	public Taskstatus() {
	}

	public long getStatusid() {
		return this.statusid;
	}

	public void setStatusid(long statusid) {
		this.statusid = statusid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Companytask> getCompanytasks() {
		return this.companytasks;
	}

	public void setCompanytasks(List<Companytask> companytasks) {
		this.companytasks = companytasks;
	}

	public Companytask addCompanytask(Companytask companytask) {
		getCompanytasks().add(companytask);
		companytask.setTaskstatus(this);

		return companytask;
	}

	public Companytask removeCompanytask(Companytask companytask) {
		getCompanytasks().remove(companytask);
		companytask.setTaskstatus(null);

		return companytask;
	}

	public List<Scheduletaskdetail> getScheduletaskdetails() {
		return this.scheduletaskdetails;
	}

	public void setScheduletaskdetails(List<Scheduletaskdetail> scheduletaskdetails) {
		this.scheduletaskdetails = scheduletaskdetails;
	}

	public Scheduletaskdetail addScheduletaskdetail(Scheduletaskdetail scheduletaskdetail) {
		getScheduletaskdetails().add(scheduletaskdetail);
		scheduletaskdetail.setTaskstatus(this);

		return scheduletaskdetail;
	}

	public Scheduletaskdetail removeScheduletaskdetail(Scheduletaskdetail scheduletaskdetail) {
		getScheduletaskdetails().remove(scheduletaskdetail);
		scheduletaskdetail.setTaskstatus(null);

		return scheduletaskdetail;
	}

}