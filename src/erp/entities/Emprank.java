package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the EMPRANK database table.
 * 
 */
@Entity
@NamedQuery(name="Emprank.findAll", query="SELECT e FROM Emprank e")
public class Emprank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EMPRANK_RANKID_GENERATOR", sequenceName="EMPRANK_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMPRANK_RANKID_GENERATOR")
	private long rankid;

	private String description;

	private String name;

	private BigDecimal ordered;

	//bi-directional many-to-one association to Staff
	@OneToMany(mappedBy="emprank")
	private List<Staff> staffs;

	public Emprank() {
	}

	public long getRankid() {
		return this.rankid;
	}

	public void setRankid(long rankid) {
		this.rankid = rankid;
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

	public BigDecimal getOrdered() {
		return this.ordered;
	}

	public void setOrdered(BigDecimal ordered) {
		this.ordered = ordered;
	}

	public List<Staff> getStaffs() {
		return this.staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public Staff addStaff(Staff staff) {
		getStaffs().add(staff);
		staff.setEmprank(this);

		return staff;
	}

	public Staff removeStaff(Staff staff) {
		getStaffs().remove(staff);
		staff.setEmprank(null);

		return staff;
	}

}