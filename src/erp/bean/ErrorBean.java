package erp.bean;

import erp.util.MessageBundleLoader;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;


import javax.inject.Named;

@Named(value = "errorBean")
@RequestScoped
public class ErrorBean implements Serializable {

    String errorMSG = null;
    Boolean showDisconnectButton = true;

    @PostConstruct
    public void init(){
        errorMSG = MessageBundleLoader.getMessage("errMsg_GeneralError");                                        
    }
    
    @PreDestroy
    public void reset() {
        errorMSG = null;
        showDisconnectButton = true;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public void setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
    }

    public Boolean getShowDisconnectButton() {
        return showDisconnectButton;
    }

    public void setShowDisconnectButton(Boolean showDisconnectButton) {
        this.showDisconnectButton = showDisconnectButton;
    }
}
