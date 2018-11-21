package erp.servlets;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class ServletRequestListenerEntityManager implements ServletRequestListener {

    public void requestDestroyed(ServletRequestEvent arg0) {
//              EntityManagerHelper.closeEntityManager();
    }

    public void requestInitialized(ServletRequestEvent arg0) {
    }
}