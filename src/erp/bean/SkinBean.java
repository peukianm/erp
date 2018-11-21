package erp.bean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named(value = "skinBean")
@SessionScoped
public class SkinBean implements Serializable {

    private static final long serialVersionUID = 2744605279708632184L;
    private SelectItem[] skinSetItems = {
        new SelectItem("blueSky"),
        new SelectItem("classic"),
        new SelectItem("deepMarine"),
        new SelectItem("DEFAULT"),
        new SelectItem("emeraldTown"),
        new SelectItem("japanCherry"),
        new SelectItem("NULL"),
        new SelectItem("plain"),
        new SelectItem("ruby"),
        new SelectItem("wine")
    };
    private String skin = "classic";
    private boolean enableElementsSkinning = true;
    private boolean enableClassesSkinning = false;

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public boolean isEnableElementsSkinning() {
        return enableElementsSkinning;
    }

    public void setEnableElementsSkinning(boolean enableElementsSkinning) {
        this.enableElementsSkinning = enableElementsSkinning;
    }

    public boolean isEnableClassesSkinning() {
        return enableClassesSkinning;
    }

    public void setEnableClassesSkinning(boolean enableClassesSkinning) {
        this.enableClassesSkinning = enableClassesSkinning;
    }

    public SelectItem[] getSkinSetItems() {
        return skinSetItems;
    }
}
