package erp.converter;

import java.math.BigDecimal;
import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


import erp.entities.Userrole;
import erp.util.EJBUtil;
import erp.util.PersistenceHelper;

public class UserrolesConverter implements Converter {
    
    private PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().isEmpty()) {
            return null;
        } else {
            try {
                BigDecimal number = new BigDecimal(submittedValue);

                Userrole userrole = persistenceHelper.getEntityManager().find(Userrole.class, number);
                return userrole;


            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Userole"));
            }
        }

    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        try {
            if (value == null || value.equals("")) {
                return "";
            } else {

                return String.valueOf(((Userrole) value).getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
