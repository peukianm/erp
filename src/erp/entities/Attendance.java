package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the ATTENDANCE database table.
 * 
 */
@Entity
@NamedQuery(name="Attendance.findAll", query="SELECT a FROM Attendance a")
public class Attendance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ATTENDANCE_ID_GENERATOR", sequenceName="ATTENDANCE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ATTENDANCE_ID_GENERATOR")
	private long id;

	private BigDecimal ended;

	private Timestamp entrance;

	@Column(name="\"EXIT\"")
	private Timestamp exit;

	@Column(name="\"HOUR\"")
	private BigDecimal hour;

	@Column(name="\"MINUTE\"")
	private BigDecimal minute;

	//bi-directional many-to-one association to Company
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMPANYID")
	private Company company;

	//bi-directional many-to-one association to Department
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENTID")
	private Department department;

	//bi-directional many-to-one association to Sector
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SECTORID")
	private Sector sector;

	//bi-directional many-to-one association to Staff
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STAFFID")
	private Staff staff;

	public Attendance() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getEnded() {
		return this.ended;
	}

	public void setEnded(BigDecimal ended) {
		this.ended = ended;
	}

	public Timestamp getEntrance() {
		return this.entrance;
	}

	public void setEntrance(Timestamp entrance) {
		this.entrance = entrance;
	}

	public Timestamp getExit() {
		return this.exit;
	}

	public void setExit(Timestamp exit) {
		this.exit = exit;
	}

	public BigDecimal getHour() {
		return this.hour;
	}

	public void setHour(BigDecimal hour) {
		this.hour = hour;
	}

	public BigDecimal getMinute() {
		return this.minute;
	}

	public void setMinute(BigDecimal minute) {
		this.minute = minute;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public Staff getStaff() {
		return this.staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

}