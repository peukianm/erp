package erp.converter;

import erp.dao.StaffDAO;
import erp.entities.Staff;
import erp.util.EJBUtil;
import erp.util.PersistenceHelper;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@FacesConverter(value = "StaffConverter", managed = true)
public class StaffConverter implements Converter {

    @Inject
    private StaffDAO staffDAO;
    
    private PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.trim().isEmpty()) {
            return null;
        } else {
            try {
                System.out.println("submittedValue=" + submittedValue);
                System.out.println("staffDAO=" + staffDAO);
                System.out.println("em=" + persistenceHelper);
                
                Long number = new Long(submittedValue);
                Staff staff =  persistenceHelper.getEntityManager().find(Staff.class, number); //staffDAO.getStatff(number.longValue());
                return staff;

            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Staff"));
            }
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        try {
            if (value == null || value.equals("")) {
                return "";
            } else {
                return String.valueOf(((Staff) value).getStaffid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
