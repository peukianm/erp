/**
 *
 */
package erp.action;

import erp.bean.*;
import erp.dao.AuditingDAO;
import erp.dao.StaffDAO;
import erp.dao.UsrDAO;
import erp.entities.*;
import erp.util.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

public class AdministrationAction implements Serializable {

    private static final Logger logger = LogManager.getLogger(AdministrationAction.class);

    @EJB
    UsrDAO userDAO;

    @EJB
    AuditingDAO auditingDAO;

    @EJB
    StaffDAO staffDAO;

    @Inject
    private SessionBean sessionBean;

    @EJB
    private PersistenceUtil persistenceUtil;

    @EJB
    private PersistenceHelper persistenceHelper;

    @Inject
    UserBean userBean;

    @Inject
    RoleSelectionBean roleSelectionBean;

    @Inject
    AuditBean auditBean;

    @Inject
    ResetBean resetBean;

    @Inject
    DashboardView dbView;

    @Inject
    DashboardUsers dbUsers;

    @Inject
    InsertUser insertUser;

    @Inject
    UpdateUser updateUser;

    public AdministrationAction() {
    }

    public String loginAction() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("LOGIN ACTION!!!!!!");
            }

            Usr temp = null;
            List<Usr> users = userDAO.findByProperty("username", userBean.getUsername());
            if (users.size() > 0 && ErpUtil.check(userBean.getPassword(), users.get(0).getPassword())) {
                temp = users.get(0);
            }

            if (temp == null) {
                userBean.setPassword(null);
                sessionBean.setErrorMsgKey("errMsg_InvalidCredentials");
                FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("errMsg_InvalidCredentials"));
                return "loginPage";
            }

            List<Userrole> userroles = temp.getUserroles();
            //roleSelectionBean.setUserroles(userroles);
            //temp.setUserroles(userroles);
            sessionBean.setUsers(temp);

            if (userroles.size() > 1) {
                FacesUtils.callRequestContext("PF('selectRoleDialog').show()");
            } else if (userroles.size() == 1) {
                temp.setRole(userroles.get(0).getRole());

//               usrTransaction.begin();
//                Company comp = new Company();
//                comp.setName("1111111111111111111");
//                comp.setActive(BigDecimal.ONE);
//                persistenceHelper.create(comp);
//                Users usr = new Users();
//                //usr.setActive(BigDecimal.ONE);
//                usr.setUsername("Username1");
//                usr.setPassword("Password1");
//                usr.setName("Name1");
//                usr.setSurname("Surname1");
//                persistenceHelper.create(usr);
//                usrTransaction.commit();
//               
                return mainPageForward(temp);
                //return userAdmin();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String selectRole() {
        //RoleSelectionBean roleSelectionBean = (RoleSelectionBean) FacesUtils.getManagedBean("roleSelectionBean");
        Userrole selectedRole = roleSelectionBean.getSelectedRole();
        Usr temp = sessionBean.getUsers();
        temp.setRole(selectedRole.getRole());
        sessionBean.setUsers(temp);

        return mainPageForward(temp);

    }

    private String mainPageForward(Usr temp) {
        try {
            if (temp.getRole().getRoleid() == 1 || temp.getRole().getRoleid() == 2 || temp.getRole().getRoleid() == 3) {
                Action action = userDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_LOGINUSER")));
                Auditing audit = new Auditing(temp, temp.getCompany(), action, null,
                        FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                        FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));

                auditingDAO.save(audit);
                sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
                sessionBean.setPageName(MessageBundleLoader.getMessage("usersPage"));
                return "dashboardUsers?faces-redirect=true";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void invalidateSession() {
        FacesUtils.resetManagedBeanJSF2("sessionBean");
        FacesUtils.invalidateSession();
    }

    public String gotoMain() {
        try {
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ERP_HOME"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("homePage"));
            return "main?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void fetchUsers() {
        try {
            List<Usr> users = userDAO.searchUser(dbUsers.getSelectedRole(), dbUsers.getSelectedCompany(), dbUsers.getSelectedDepartment(),
                    dbUsers.getSelectedSector(), dbUsers.getSurname(), dbUsers.getActive());
            dbUsers.setSearchUsers(users);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void fetchAttendances() {
        try {
            List<AttendanceBean> retValue = new ArrayList<>(0);
            if (!dbView.getSelectedSectors().isEmpty()) {
                dbView.getSelectedSectors().forEach((temp) -> {
                    List<Attendance> attendances = staffDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbView.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbView.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, temp, null);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        }
                        if (temp1.getExit() != null) {
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbView.getSelectedDepartments().isEmpty()) {
                dbView.getSelectedDepartments().forEach((temp) -> {
                    List<Attendance> attendances = staffDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbView.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbView.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), null, null, temp);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        }
                        if (temp1.getExit() != null) {
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });

            } else if (!dbView.getSelectedStaff().isEmpty()) {
                dbView.getSelectedStaff().forEach((temp) -> {
                    List<Attendance> attendances = staffDAO.staffApperence(sessionBean.getUsers().getCompany(), FormatUtils.formatDate(dbView.getFromAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN),
                            FormatUtils.formatDate(dbView.getToAttendanceDate(), FormatUtils.TIMESTAMPDATEPATTERN), temp, null, null);
                    attendances.forEach((temp1) -> {
                        AttendanceBean bean = new AttendanceBean();
                        bean.setName(temp1.getStaff().getSurname() + " " + temp1.getStaff().getName());
                        bean.setDate(temp1.getEntrance().toLocalDateTime().toLocalDate());
                        bean.setEntrance(temp1.getEntrance().toLocalDateTime().toLocalTime());
                        if (temp1.getExit() != null) {
                            bean.setExit(temp1.getExit().toLocalDateTime().toLocalTime());
                        }
                        if (temp1.getExit() != null) {
                            bean.setDuration(FormatUtils.splitSecondsToTime(FormatUtils.getDateDiff(temp1.getEntrance(),
                                    temp1.getExit(), TimeUnit.SECONDS)));
                        }
                        retValue.add(bean);
                    });
                });
            }
            dbView.setAttendances(retValue);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String insertUser() {
        try {
            List<Usr> usrs = userDAO.findByProperty("username", insertUser.getUsername().trim());
            if (!insertUser.getPassword().trim().equals(insertUser.getRepassword().trim())) {
                System.out.println("Not Same Password");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("passwordDontMatch"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }
            if (usrs.size() == 1) {
                System.out.println("Username already  exists");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            usrs = userDAO.findByProperty("email", insertUser.getEmail().trim());

            if (usrs.size() == 1) {
                System.out.println("EMAIL ALREADY EXISTS");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("emailAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            String hashedPassword = ErpUtil.getSaltedHash(insertUser.getPassword());

            Usr newUser = new Usr();
            newUser.setActive(BigDecimal.ONE);
            newUser.setName(insertUser.getName());
            newUser.setSurname(insertUser.getSurname());
            newUser.setUsername(insertUser.getUsername());
            newUser.setCompany(insertUser.getCompany());
            newUser.setDepartment(insertUser.getDepartment());
            newUser.setEmail(insertUser.getEmail());
            newUser.setPassword(hashedPassword);
            newUser.setPhone(insertUser.getPhone());
            newUser.setRoles(insertUser.getSelectedRoles());
            newUser.setStaff(insertUser.getStaff());

            userDAO.save(newUser);

            Action action = userDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_INSERTUSER")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "User " + newUser.getUsername() + " inserted",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("newUserInserted"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("usersPage"));
            return "dashboardUsers";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public List<Usr> completeUser(String username) {
        try {
            if (username != null && !username.trim().isEmpty() && username.trim().length() >= 1) {
                username = username.trim();
                List<Usr> users = userDAO.fetchUserAutoCompleteUsername(username);
                return users;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return null;
        }
    }

    public String updateUser() {
        try {
            List<Usr> usrs = userDAO.findByProperty("username", updateUser.getUser().getUsername().trim());
            if (usrs.size() == 1 && !usrs.get(0).getUsername().equals(updateUser.getUser().getUsername().trim())) {
                System.out.println("Username already  exists");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            usrs = userDAO.findByProperty("email", updateUser.getUser().getEmail().trim());

            if (usrs.size() == 1 && !usrs.get(0).getEmail().equals(updateUser.getUser().getEmail().trim())) {
                System.out.println("EMAIL ALREADY EXISTS");
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("emailAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            if (updateUser.isActive()) {
                updateUser.getUser().setActive(BigDecimal.ONE);
            } else {
                updateUser.getUser().setActive(BigDecimal.ZERO);
            }

            userDAO.update(updateUser.getUser());

            Action action = userDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEUSER")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "User " + updateUser.getUser().getUsername() + " updated",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("usersPage"));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }
    
        public String resetPassword() {
        try {
            Usr user = updateUser.getUser();
            String hashedPassword = ErpUtil.getSaltedHash(updateUser.getPassword());
            user.setPassword(hashedPassword);

            userDAO.update(user);
            
            auditingDAO.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEPASSWORD")), "User Password " + user.getUsername() + " updated");
            
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));
            FacesUtils.callRequestContext("PF('resetPasswordDialogWidget').hide()");
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String goUpdateUser(long userID) {
        try {
            return "updateUser?faces-redirect=true&userID=" + userID;
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String getPropertyValue(String key) {
        String propertyValue = SystemParameters.getInstance().getProperty(key);
        return propertyValue;
    }

    public String auditControl() {
        try {
            auditBean.reset();
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_AUDIT_CONTROL"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("audit"));
            return "auditControl?faces-redirect=true ";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String userAdmin() {
        try {
            userBean.reset();
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("userAdmin"));
            return "userMgt?faces-redirect=true ";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void autocompleteUsernameSelectUser(SelectEvent event) {
        try {

            Usr user = auditBean.getSelectUser();
            auditBean.setSearchUser(user);
            auditBean.setSelectUser(null);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String resetSearchAudit() {
        try {
            return auditControl();
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void searchAudit() {
        try {
            Timestamp from = null;
            if (auditBean.getSearchFromActionDate() != null) {
                from = FormatUtils.formatDateToTimestamp(auditBean.getSearchFromActionDate());
            }

            Timestamp to = null;
            if (auditBean.getSearchToActionDate() != null) {
                to = FormatUtils.formatDateToTimestamp(auditBean.getSearchToActionDate());
            }

            AuditingDAO dao = new AuditingDAO();
            List<Auditing> auditings = dao.searchAudit(auditBean.getSearchUser(), auditBean.getSearchAction(), from, to, auditBean.getSearchCompany());
            auditBean.setAudits(auditings);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String resetUserAdmin() {
        try {
            return userAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void goInsertUser() {
        try {
            Usr user = new Usr();
            user.setActive(BigDecimal.ONE);
            userBean.setRoles(new ArrayList<Role>(0));

            userBean.setUser(user);
            FacesUtils.callRequestContext("createUserDialogWidget.show()");
            FacesUtils.updateHTMLComponnetWIthJS("createUserPanelID");
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void goResetPasword() {
        try {
            FacesUtils.callRequestContext("PF('resetPasswordDialogWidget').show()");
            FacesUtils.updateHTMLComponnetWIthJS("resetPasswordUserPanelID");
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void deleteUser() {
        try {
            Usr user = userBean.getUser();
            user.setActive(BigDecimal.ZERO);
            user = persistenceHelper.editPersist(user);
            persistenceUtil.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_DELETEUSER")), "User " + user.getUsername() + " removed");

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userDeleted"));
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

//    public String updateUser() {
//        try {
//            Usr user = userBean.getUser();
//            List<Usr> usrs = userDAO.findByProperty("username", userBean.getUser().getUsername().trim());
//            if (usrs.size() == 1 && !usrs.get(0).equals(user)) {
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return "";
//            }
//
//            usrs = userDAO.findByProperty("email", userBean.getUser().getEmail().trim());
//
//            if (usrs.size() == 1 && !usrs.get(0).equals(user)) {
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("emailAlreadyUsed"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return "";
//            }
//
//            if (userBean.isActive()) {
//                user.setActive(BigDecimal.ONE);
//            } else {
//                user.setActive(BigDecimal.ZERO);
//            }
//
//            //removing
//            for (int i = 0; i < user.getUserroles().size(); i++) {
//                Userrole userrole = user.getUserroles().get(i);
//                persistenceHelper.remove(userrole);
//            }
//            user.setUserroles(null);
//
//            //adding
//            List<Role> roles = userBean.getRoles();
//            List<Userrole> userroles = new ArrayList<Userrole>(0);
//            for (int i = 0; i < roles.size(); i++) {
//                Role role = roles.get(i);
//                Userrole userrole = new Userrole();
//                userrole.setUsr(user);
//                userrole.setRole(role);
//                userroles.add(userrole);
//            }
//
//            user.setUserroles(userroles);
//
//            user = persistenceHelper.editPersist(user);
//            persistenceUtil.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEUSER")), "User " + user.getUsername() + " updated");
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));
//            FacesUtils.callRequestContext("updateUserDialogWidget.hide()");
//            return userAdmin();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//            return "";
//        }
//    }


    public String goResetPasswordEmail() {
        try {
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_RESET_PASSWORD"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("passwordReminder"));
            return "resetPassword?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String sendResetPasswordEmail() {
        try {
            ResetBean resetBean = (ResetBean) FacesUtils.getManagedBean("resetBean");
            String email = resetBean.getEmail().trim();
            List<Usr> users = userDAO.findByProperty("email", email);
            if (users != null && users.size() == 1) {
                Usr user = users.get(0);
                String username = user.getUsername();
                String[] emails = new String[1];
                emails[0] = user.getEmail();

                String link = SystemParameters.getInstance().getProperty("resetPassword_link") + user.getPassword() + "&userid=" + user.getUserid();
                String host = SystemParameters.getInstance().getProperty("gmail_host");
                String port = SystemParameters.getInstance().getProperty("gmail_port");
                String account = SystemParameters.getInstance().getProperty("gmail_account");
                String password = SystemParameters.getInstance().getProperty("gmail_password");
                String subject = SystemParameters.getInstance().getProperty("email_subject") + username;
                String body = SystemParameters.getInstance().getProperty("email_body") + link;

                ErpUtil.sendFromGMail(host, port, account, password, emails, subject, body);
                FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("successUserEmail"));
                return "";
            } else {
                FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("falseUserEmail"));
                return "";
            }

            //return "loginPage?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void resetPasswordEmail() {
        //ResetBean resetBean = (ResetBean) FacesUtils.getManagedBean("resetBean");        
        try {
            Usr user = resetBean.getUser();
            String hashedPassword = ErpUtil.getSaltedHash(resetBean.getPassword());
            user.setPassword(hashedPassword);

            user = persistenceHelper.editPersist(user);
            persistenceUtil.audit(user, Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEPASSWORD")), "User Password " + user.getUsername() + " updated");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void goError(Exception ex) {
        try {
            logger.error("-----------AN ERROR HAPPENED !!!! -------------------- : " + ex.toString());
            if (sessionBean.getUsers() != null) {
                logger.error("User=" + sessionBean.getUsers().getUsername());
            }
            logger.error("Cause=" + ex.getCause());
            logger.error("Class=" + ex.getClass());
            logger.error("Message=" + ex.getLocalizedMessage());
            logger.error(ex, ex);
            logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");

            ErrorBean errorBean = (ErrorBean) FacesUtils.getManagedBean("errorBean");
            errorBean.reset();
            errorBean.setErrorMSG(MessageBundleLoader.getMessage(sessionBean.getErrorMsgKey()));
            //FacesUtils.redirectAJAX("./templates/error.jsf?faces-redirect=true");
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/error.jsf?faces-redirect=true");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String logoutAction() {
        try {
            System.out.println("LOGING OUT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            FacesUtils.resetManagedBeanJSF2("sessionBean");
            FacesUtils.invalidateSession();
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/login.jsf?faces-redirect=true");
            return "./login?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String logoutAction(int t) {
        try {
            FacesUtils.resetManagedBeanJSF2("sessionBean");
            FacesUtils.invalidateSession();
            //FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/loginPage.jsf?faces-redirect=true");
            return "/login?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void closeAlertDlg() {
        sessionBean.setShowGeneralDialog(false);
        sessionBean.setShowMsgDialog(false);
    }
}
