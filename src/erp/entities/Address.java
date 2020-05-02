package erp.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 * The persistent class for the ADDRESS database table.
 *
 */
@Entity
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ADDRESS_ADDRESSID_GENERATOR", sequenceName = "ADDRESS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_ADDRESSID_GENERATOR")
    private BigDecimal addressid;

    private String address;

    private String city;

    private String country;

    private String postalcode;

    //bi-directional many-to-one association to Company
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANYID")
    private Company company;

    public Address() {
    }

    public BigDecimal getAddressid() {
        return this.addressid;
    }

    public void setAddressid(BigDecimal addressid) {
        this.addressid = addressid;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return this.postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        return compare.addressid.equals(this.addressid);
    }

    @Override
    public int hashCode() {
        return addressid != null ? this.getClass().hashCode() + addressid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Address{id=" + addressid + ", name=" + getAddress()+ "}";
    }

}
