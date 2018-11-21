package erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@FacesConverter("com.hosp.converter.DateConverter")
public class DateConverter extends DateTimeConverter implements Converter {

    public DateConverter() {

        this.setTimeZone(TimeZone.getDefault());
    }
    
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        try {

            //DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EEST' yyyy",Locale.ENGLISH);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = df.parse(value);
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        try {
            Date date = (Date) value;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(date);
            //return date.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            return "";
        }
    }
}
