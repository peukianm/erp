package org.primefaces.babylon.view;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    private String menuMode = "layout-static";

    private String theme = "blue-accent";

    private String layout = "blue";

    private boolean darkMenu = true;

    private String profileMode = "popup";

    private boolean groupedMenu = true;

    private boolean darkLogo;

    public String getLayout() {
        return this.layout;
    }

    public String getTheme() {
        return this.theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
        this.layout = this.theme.split("-")[0];
        this.darkLogo = this.layout.equals("lime") || this.layout.equals("yellow");
    }

    public void changeTheme(String theme, boolean dark) {
        this.setTheme(theme);
        this.darkMenu = dark;
    }

    public String getMenuMode() {
        return this.menuMode;
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;

        if (this.menuMode.equals("layout-horizontal")) {
            this.groupedMenu = true;
        }
    }

    public boolean isDarkMenu() {
        return this.darkMenu;
    }

    public void setDarkMenu(boolean darkMenu) {
        this.darkMenu = darkMenu;
    }

    public String getMenuColor() {
        return this.darkMenu ? "layout-menu-dark" : "layout-menu-light";
    }

    public String getProfileMode() {
        return this.profileMode;
    }

    public void setProfileMode(String profileMode) {
        this.profileMode = profileMode;
    }

    public boolean isGroupedMenu() {
        return this.groupedMenu;
    }

    public void setGroupedMenu(boolean value) {
        this.groupedMenu = value;
        this.menuMode = "layout-static";
    }

    public boolean isDarkLogo() {
        return this.darkLogo;
    }
}
