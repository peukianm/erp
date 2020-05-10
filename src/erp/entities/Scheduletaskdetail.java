package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the SCHEDULETASKDETAILS database table.
 * 
 */
@Entity
@Table(name="SCHEDULETASKDETAILS")
@NamedQuery(name="Scheduletaskdetail.findAll", query="SELECT s FROM Scheduletaskdetail s")
public class Scheduletaskdetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SCHEDULETASKDETAILS_DETAILSID_GENERATOR", sequenceName="SCHEDULETASKDETAILS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SCHEDULETASKDETAILS_DETAILSID_GENERATOR")
	private long detailsid;

	private String data1;

	private String data2;

	private Timestamp executiontime;

	//bi-directional many-to-one association to Companytask
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CTASKID")
	private Companytask companytask;

	//bi-directional many-to-one association to Taskstatus
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="EXECUTIONSTATUS")
	private Taskstatus taskstatus;

	public Scheduletaskdetail() {
	}

	public long getDetailsid() {
		return this.detailsid;
	}

	public void setDetailsid(long detailsid) {
		this.detailsid = detailsid;
	}

	public String getData1() {
		return this.data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return this.data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public Timestamp getExecutiontime() {
		return this.executiontime;
	}

	public void setExecutiontime(Timestamp executiontime) {
		this.executiontime = executiontime;
	}

	public Companytask getCompanytask() {
		return this.companytask;
	}

	public void setCompanytask(Companytask companytask) {
		this.companytask = companytask;
	}

	public Taskstatus getTaskstatus() {
		return this.taskstatus;
	}

	public void setTaskstatus(Taskstatus taskstatus) {
		this.taskstatus = taskstatus;
	}

}