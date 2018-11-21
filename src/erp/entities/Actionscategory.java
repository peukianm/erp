package erp.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Actionscategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACTIONSCATEGORY", schema = "SKELETON")
public class Actionscategory implements java.io.Serializable {

	// Fields
	private BigDecimal categoryid;
	private String name;
	private Set<Action> actions = new HashSet<Action>(0);

	// Constructors

	/** default constructor */
	public Actionscategory() {
	}

	/** minimal constructor */
	public Actionscategory(BigDecimal categoryid) {
		this.categoryid = categoryid;
	}

	/** full constructor */
	public Actionscategory(BigDecimal categoryid, String name, Set<Action> actions) {
		this.categoryid = categoryid;
		this.name = name;
		this.actions = actions;
	}

	// Property accessors
	@Id
	@Column(name = "CATEGORYID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCategoryid() {
		return this.categoryid;
	}

	public void setCategoryid(BigDecimal categoryid) {
		this.categoryid = categoryid;
	}

	@Column(name = "NAME", length = 4000)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "actionscategory")
	public Set<Action> getActions() {
		return this.actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}
        
        
         @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Actionscategory)) {
            return false;
        }

        Actionscategory  compare = (Actionscategory) obj;
        return compare.categoryid.equals(this.categoryid);
    }

    @Override
    public int hashCode() {
        return categoryid != null ? this.getClass().hashCode() + categoryid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Actioncategory{id=" + categoryid + ", name=" + getName()  + "}";
    }

}