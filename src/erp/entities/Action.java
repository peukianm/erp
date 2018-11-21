package erp.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Action entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACTION", schema = "SKELETON")
public class Action implements java.io.Serializable {

	// Fields

	private BigDecimal actionid;
	private Actionscategory actionscategory;
	private String name;

	// Constructors

	/** default constructor */
	public Action() {
	}

	/** minimal constructor */
	public Action(BigDecimal actionid, String name) {
		this.actionid = actionid;
		this.name = name;
	}

	/** full constructor */
	public Action(BigDecimal actionid, Actionscategory actionscategory, String name) {
		this.actionid = actionid;
		this.actionscategory = actionscategory;
		this.name = name;
	}

	// Property accessors
	@Id
	@Column(name = "ACTIONID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getActionid() {
		return this.actionid;
	}

	public void setActionid(BigDecimal actionid) {
		this.actionid = actionid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORYID")
	public Actionscategory getActionscategory() {
		return this.actionscategory;
	}

	public void setActionscategory(Actionscategory actionscategory) {
		this.actionscategory = actionscategory;
	}

	@Column(name = "NAME", nullable = false, length = 300)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
        
        
        @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Action)) {
            return false;
        }

        Action  compare = (Action) obj;
        return compare.actionid.equals(this.actionid);
    }

    @Override
    public int hashCode() {
        return actionid != null ? this.getClass().hashCode() + actionid.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Action{id=" + actionid + ", name=" + getName() + ", actioncategory=" + getActionscategory() + "}";
    }

}