package erp.validator;

/**
 *
 * @author peukianm
 */
import erp.util.MessageBundleLoader;
import java.util.Map;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
import javax.faces.application.FacesMessage;  
import javax.faces.component.UIComponent;  
import javax.faces.context.FacesContext;  
import javax.faces.validator.FacesValidator;  
import javax.faces.validator.Validator;  
import javax.faces.validator.ValidatorException;  
import org.primefaces.validate.ClientValidator;  
  
/** 
 * Custom JSF Validator for Email input 
 */  
@FacesValidator("emailValidator")  
public class EmailValidator implements Validator, ClientValidator {  
  
    private Pattern pattern;  
    private Matcher matcher;  
   
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"  
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  
   
    public EmailValidator() {  
        pattern = Pattern.compile(EMAIL_PATTERN);  
    }  
  
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {  
        if(value == null) {  
            return;  
        }  
          
        if(!pattern.matcher(value.toString()).matches()) {  
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, value+":"+MessageBundleLoader.getMessage("noValidemail"),   
                        value + " is not a valid email;"));  
        }  
    }  
      
    public Map<String, Object> getMetadata() {  
        return null;  
    }  
  
    public String getValidatorId() {  
        return "custom.emailValidator";  
    }  
}
