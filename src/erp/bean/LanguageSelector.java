package erp.bean;


import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;


import javax.inject.Named;

@Named(value = "languageSelector")
@SessionScoped
public class LanguageSelector implements Serializable{
	private Locale currentLocale = new Locale("gr");	
}
