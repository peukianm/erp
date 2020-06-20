package erp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the ACTIONSCATEGORY database table.
 *
 */
@Entity
@NamedQuery(name = "Actionscategory.findAll", query = "SELECT a FROM Actionscategory a")
public class Actionscategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ACTIONSCATEGORY_CATEGORYID_GENERATOR", sequenceName = "ACTIONSCATEGORY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIONSCATEGORY_CATEGORYID_GENERATOR")
    private long categoryid;

    private String name;

    //bi-directional many-to-one association to Action
    @OneToMany(mappedBy = "actionscategory")
    private List<Action> actions;

    public Actionscategory() {
    }

    public long getCategoryid() {
        return this.categoryid;
    }

    public void setCategoryid(long categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Action addAction(Action action) {
        getActions().add(action);
        action.setActionscategory(this);
        return action;
    }

    public Action removeAction(Action action) {
        getActions().remove(action);
        action.setActionscategory(null);
        return action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Actionscategory)) {
            return false;
        }

        Actionscategory compare = (Actionscategory) obj;
        return compare.categoryid == (this.categoryid);
    }

    @Override
    public int hashCode() {
        return categoryid != 0 ? this.getClass().hashCode() + Long.hashCode(categoryid) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Actioncategory{id=" + categoryid + ", name=" + getName() + "}";
    }

}
