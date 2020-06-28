package erp.converter;

import erp.dao.StaffDAO;
import erp.entities.Department;
import erp.util.EJBUtil;
import erp.util.PersistenceHelper;
import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import javax.inject.Inject;

@FacesConverter(value = "DepartmentConverter", managed = true)
public class DepartmentConverter implements Converter {

    private PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.trim().isEmpty()) {
            return null;
        } else {
            try {
                Long number = new Long(submittedValue);
                Department department = persistenceHelper.getEntityManager().find(Department.class, number); //staffDAO.getStatff(number.longValue());
                return department;

            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Department"));
            }
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        try {            
            if (value == null || value.equals("")) {
                return "";
            } else {
                return String.valueOf(((Department)value).getDepartmentid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
