package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the PATIENT database table.
 *
 */
@Entity
@NamedQuery(name = "Patient.findAll", query = "SELECT p FROM Patient p")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PATIENT_PATIENTID_GENERATOR", sequenceName = "PATIENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PATIENT_PATIENTID_GENERATOR")
    private long patientid;

    private BigDecimal active;

    private String address;

    private String adt;

    private String afm;

    private String amka;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    private String children;

    private BigDecimal cityid;

    private BigDecimal countryid;

    @Column(name = "CREATED_TIMESTAMP")
    private Timestamp createdTimestamp;

    private String cteamid;

    private BigDecimal dead;

    @Temporal(TemporalType.DATE)
    private Date deathdate;

    private String fathername;

    private String fullname;

    @Column(name = "MODIFIED_TIMESTAMP")
    private Timestamp modifiedTimestamp;

    private String mothername;

    private String name;

    private BigDecimal nationalityid;

    private String phone1;

    private String phone2;

    private String phone3;

    private String postalcode;

    private BigDecimal prefectureid;

    private BigDecimal professionid;

    private BigDecimal sex;

    private String spousename;

    private String surname;

    //bi-directional many-to-one association to Familystatus
    @ManyToOne
    @JoinColumn(name = "FAMILYSTATUSID")
    private Familystatus familystatus;

    //bi-directional many-to-one association to Proadmission
    @OneToMany(mappedBy = "patient")
    private List<Proadmission> proadmissions;

    public Patient() {
    }

    public long getPatientid() {
        return this.patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    public BigDecimal getActive() {
        return this.active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdt() {
        return this.adt;
    }

    public void setAdt(String adt) {
        this.adt = adt;
    }

    public String getAfm() {
        return this.afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getAmka() {
        return this.amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public Date getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getChildren() {
        return this.children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public BigDecimal getCityid() {
        return this.cityid;
    }

    public void setCityid(BigDecimal cityid) {
        this.cityid = cityid;
    }

    public BigDecimal getCountryid() {
        return this.countryid;
    }

    public void setCountryid(BigDecimal countryid) {
        this.countryid = countryid;
    }

    public Timestamp getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCteamid() {
        return this.cteamid;
    }

    public void setCteamid(String cteamid) {
        this.cteamid = cteamid;
    }

    public BigDecimal getDead() {
        return this.dead;
    }

    public void setDead(BigDecimal dead) {
        this.dead = dead;
    }

    public Date getDeathdate() {
        return this.deathdate;
    }

    public void setDeathdate(Date deathdate) {
        this.deathdate = deathdate;
    }

    public String getFathername() {
        return this.fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Timestamp getModifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public String getMothername() {
        return this.mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getNationalityid() {
        return this.nationalityid;
    }

    public void setNationalityid(BigDecimal nationalityid) {
        this.nationalityid = nationalityid;
    }

    public String getPhone1() {
        return this.phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return this.phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return this.phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getPostalcode() {
        return this.postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public BigDecimal getPrefectureid() {
        return this.prefectureid;
    }

    public void setPrefectureid(BigDecimal prefectureid) {
        this.prefectureid = prefectureid;
    }

    public BigDecimal getProfessionid() {
        return this.professionid;
    }

    public void setProfessionid(BigDecimal professionid) {
        this.professionid = professionid;
    }

    public BigDecimal getSex() {
        return this.sex;
    }

    public void setSex(BigDecimal sex) {
        this.sex = sex;
    }

    public String getSpousename() {
        return this.spousename;
    }

    public void setSpousename(String spousename) {
        this.spousename = spousename;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Familystatus getFamilystatus() {
        return this.familystatus;
    }

    public void setFamilystatus(Familystatus familystatus) {
        this.familystatus = familystatus;
    }

    public List<Proadmission> getProadmissions() {
        return this.proadmissions;
    }

    public void setProadmissions(List<Proadmission> proadmissions) {
        this.proadmissions = proadmissions;
    }

    public Proadmission addProadmission(Proadmission proadmission) {
        getProadmissions().add(proadmission);
        proadmission.setPatient(this);

        return proadmission;
    }

    public Proadmission removeProadmission(Proadmission proadmission) {
        getProadmissions().remove(proadmission);
        proadmission.setPatient(null);

        return proadmission;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Patient)) {
            return false;
        }

        Patient compare = (Patient) obj;
        return compare.patientid == (this.patientid);
    }

    @Override
    public int hashCode() {
        return patientid != 0 ? this.getClass().hashCode() + Long.hashCode(patientid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Patient{id=" + patientid + ", surname=" + getSurname() + "}";
    }

}
