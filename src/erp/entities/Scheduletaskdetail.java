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

	private Timestamp createdtimestamp;

	private String data1;

	private String data2;

	private Timestamp lastexecutiontime;

	private Timestamp modifiedtimestamp;

	//bi-directional many-to-one association to Companytask
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CTASKID")
	private Companytask companytask;

	public Scheduletaskdetail() {
	}

	public long getDetailsid() {
		return this.detailsid;
	}

	public void setDetailsid(long detailsid) {
		this.detailsid = detailsid;
	}

	public Timestamp getCreatedtimestamp() {
		return this.createdtimestamp;
	}

	public void setCreatedtimestamp(Timestamp createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
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

	public Companytask getCompanytask() {
		return this.companytask;
	}

	public void setCompanytask(Companytask companytask) {
		this.companytask = companytask;
	}

}