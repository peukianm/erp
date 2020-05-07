/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.entities.Userrole;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author peukianm
 */


@Named(value = "roleSelectionBean")
@ViewScoped
public class RoleSelectionBean  implements Serializable {
    
    private List<Userrole> userroles;
    private Userrole selectedRole;
    
    
    @PreDestroy
    public void reset() {
        userroles = null;
        selectedRole = null;                
    }

    public List<Userrole> getUserroles() {
        return userroles;
    }

    public void setUserroles(List<Userrole> userroles) {
        this.userroles = userroles;
    }

    public Userrole getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Userrole selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    
    
}
