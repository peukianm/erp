package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

/**
 * The persistent class for the PROADMISSION database table.
 *
 */
@Entity
@NamedQuery(name = "Proadmission.findAll", query = "SELECT p FROM Proadmission p")
public class Proadmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PROADMISSION_ADMISSIONID_GENERATOR", sequenceName = "PROADMISSION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROADMISSION_ADMISSIONID_GENERATOR")
    private long admissionid;

    private BigDecimal active;

    @Temporal(TemporalType.DATE)
    private Date admissiondate;

    @Column(name = "CREATED_TIMESTAMP")
    private Timestamp createdTimestamp;

    private String diagnosis;

    private String icd10;

    @Column(name = "MODIFIED_TIMESTAMP")
    private String modifiedTimestamp;

    private BigDecimal processed;

    private BigDecimal released;

    @Temporal(TemporalType.DATE)
    private Date releasedate;

    //bi-directional many-to-one association to Patient
    @ManyToOne
    @JoinColumn(name = "PATIENTID")
    private Patient patient;

    //bi-directional many-to-one association to Patient
    @ManyToOne
    @JoinColumn(name = "DEPARTMENTID")
    private Department department;

    public Proadmission() {
    }

    public long getAdmissionid() {
        return this.admissionid;
    }

    public void setAdmissionid(long admissionid) {
        this.admissionid = admissionid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    public Date getAdmissiondate() {
        return this.admissiondate;
    }

    public void setAdmissiondate(Date admissiondate) {
        this.admissiondate = admissiondate;
    }

    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getIcd10() {
        return this.icd10;
    }

    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }

    public String getModifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    public void setModifiedTimestamp(String modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public BigDecimal getProcessed() {
        return this.processed;
    }

    public void setProcessed(BigDecimal processed) {
        this.processed = processed;
    }

    public BigDecimal getReleased() {
        return this.released;
    }

    public void setReleased(BigDecimal released) {
        this.released = released;
    }

    public Date getReleasedate() {
        return this.releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Proadmission)) {
            return false;
        }

        Proadmission compare = (Proadmission) obj;
        return compare.admissionid == (this.admissionid);
    }

    @Override
    public int hashCode() {
        return admissionid != 0 ? this.getClass().hashCode() + Long.hashCode(admissionid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Proadmission{id=" + admissionid + ", surname=" + patient.getSurname() + " admissiondate="+admissiondate+" }";
    }

}
