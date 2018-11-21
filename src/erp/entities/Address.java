package erp.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Address entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ADDRESS", schema = "SKELETON")
@SequenceGenerator(name = "SEQ_ADDRESS", sequenceName = "ADDRESS_SEQ", allocationSize = 1)
public class Address implements java.io.Serializable {

    // Fields
    private BigDecimal addressid;
    private Company company;
    private String address;
    private String postalcode;
    private String city;
    private String country;

    // Constructors
    /**
     * default constructor
     */
    public Address() {
    }

    /**
     * minimal constructor
     */
    public Address(BigDecimal addressid, Company company, String address, String city) {
        this.addressid = addressid;
        this.company = company;
        this.address = address;
        this.city = city;
    }

    /**
     * full constructor
     */
    public Address(BigDecimal addressid, Company company, String address, String postalcode, String city, String country) {
        this.addressid = addressid;
        this.company = company;
        this.address = address;
        this.postalcode = postalcode;
        this.city = city;
        this.country = country;
    }

    // Property accessors
    @Id
    @Column(name = "ADDRESSID", unique = true, nullable = false, precision = 22, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADDRESS")
    public BigDecimal getAddressid() {
        return this.addressid;
    }

    public void setAddressid(BigDecimal addressid) {
        this.addressid = addressid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANYID", nullable = false)
    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Column(name = "ADDRESS", nullable = false, length = 60)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "POSTALCODE", length = 10)
    public String getPostalcode() {
        return this.postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    @Column(name = "CITY", nullable = false, length = 60)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "COUNTRY", length = 60)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Address)) {
            return false;
        }

        Address compare = (Address) obj;
        if (compare.addressid!=null)
            return compare.addressid.equals(this.addressid);
        else
            return compare.address.equals(this.address);
    }

    @Override
    public int hashCode() {
        return addressid != null ? this.getClass().hashCode() + addressid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Address{id=" + addressid + ", Company=" + getCompany() + ", Address=" + getAddress() + "}";
    }
}