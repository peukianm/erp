package erp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import erp.util.MessageBundleLoader;

public class IntegerConverter extends DateTimeConverter implements Converter {

    public IntegerConverter() {
    }

    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        try {
            Object integer = "";
            if (value == null || value.isEmpty()) {
                return null;
            }

            integer = new Integer(value);

            return integer;
        } catch (Exception e) {
            context.addMessage(component.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageBundleLoader.getMessage("integerValidation"), MessageBundleLoader.getMessage("integerValidation")));
            return value;
        }
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        try {
            return value.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
