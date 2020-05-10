package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SCHEDULETASK database table.
 * 
 */
@Entity
@NamedQuery(name="Scheduletask.findAll", query="SELECT s FROM Scheduletask s")
public class Scheduletask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SCHEDULETASK_TASKID_GENERATOR", sequenceName="SCHEDULETASK_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SCHEDULETASK_TASKID_GENERATOR")
	private long taskid;

	private String description;

	private String name;

	//bi-directional many-to-many association to Company
	@ManyToMany(mappedBy="scheduletasks")
	private List<Company> companies;

	//bi-directional many-to-one association to Companytask
	@OneToMany(mappedBy="scheduletask")
	private List<Companytask> companytasks;

	public Scheduletask() {
	}

	public long getTaskid() {
		return this.taskid;
	}

	public void setTaskid(long taskid) {
		this.taskid = taskid;
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

	public List<Company> getCompanies() {
		return this.companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public List<Companytask> getCompanytasks() {
		return this.companytasks;
	}

	public void setCompanytasks(List<Companytask> companytasks) {
		this.companytasks = companytasks;
	}

	public Companytask addCompanytask(Companytask companytask) {
		getCompanytasks().add(companytask);
		companytask.setScheduletask(this);

		return companytask;
	}

	public Companytask removeCompanytask(Companytask companytask) {
		getCompanytasks().remove(companytask);
		companytask.setScheduletask(null);

		return companytask;
	}

}