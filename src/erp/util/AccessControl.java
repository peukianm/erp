/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.util;

import erp.entities.Usr;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author peukianm
 * @mode 0->none, 1->Page control, 2->Action control, 3-> Both Access Control
 */
public class AccessControl {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(AccessControl.class);

    private static String pageAccessRule;
    private static String actionAccessRule;

    public static boolean control(Usr user, String pageCode, String actionCode, int mode) {
        try {
            if (user == null || user.getRole() == null || (pageCode == null && mode == 1) || (actionCode == null && mode == 2)
                    || (actionCode == null && actionCode == null && mode == 3)) {
                redirectToNoAccessPage();
                return false;
            }
            switch (mode) {
                case 1:
                    if (!checkPageAccess(user.getRole().getRoleid(), pageCode)) {
                        redirectToNoAccessPage();
                        return false;                        
                    }
                case 2:
                    if (!checkActionAccess(user.getRole().getRoleid(), actionCode)) {
                        redirectToNoAccessPage();
                        return false;
                    }
                case 3:
                    if (!(checkPageAccess(user.getRole().getRoleid(), pageCode) && checkActionAccess(user.getRole().getRoleid(), actionCode))) {
                        redirectToNoAccessPage();
                        return false;
                    }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error on performin Access Control", ex);
            redirectToNoAccessPage();
            return false;
        }
    }

    //MODE=1
    private static boolean checkPageAccess(long roleID, String pageCode) throws Exception {
        System.out.println("Checking No Access Page fro roleid="+roleID+"  "+pageCode);
        pageAccessRule = SystemParameters.getInstance().getProperty("ROLE_PAGE_ACCESS_" + roleID);
        if (pageAccessRule.equals("ALL")) {
            return true;
        }
        String[] pages = pageAccessRule.split(",");
        return Arrays.stream(pages).anyMatch(pageCode::equals);
    }

    //MODE=2
    private static boolean checkActionAccess(long roleID, String accessCode) throws Exception {
        actionAccessRule = SystemParameters.getInstance().getProperty("ACTION_ROLE_ACCESS_" + roleID);
        if (actionAccessRule.equals("ALL")) {
            return true;
        }
        String[] pages = actionAccessRule.split(",");
        return Arrays.stream(pages).anyMatch(accessCode::equals);
    }

    private static void invalidateSession() throws Exception {
        FacesUtils.resetManagedBeanJSF2("sessionBean");
        FacesUtils.invalidateSession();
    }

    private static void redirectToNoAccessPage() {
        try {
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/common/access.jsf?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(AccessControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
