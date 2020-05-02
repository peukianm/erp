/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

/**
 *
 * @author peukianm
 */
@WebListener
public class ERPSessionListener implements HttpSessionListener {
  @Override
  public void sessionCreated(HttpSessionEvent se) {
      System.out.println("-- SESSION CREATED invoked --");
      HttpSession session = se.getSession();
      System.out.println("session id: " + session.getId());
      session.setMaxInactiveInterval(5);//in seconds
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
      System.out.println("-- SESSION DESTROYED invoked --");
  }
}
