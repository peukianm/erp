package erp.action;

import erp.bean.*;
import erp.dao.AuditingDAO;
import erp.dao.StaffDAO;
import erp.dao.UsrDAO;
import erp.entities.*;
import erp.exception.ERPCustomException;
import erp.util.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;

/**
 *
 * @author peukianm
 */
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
    DashboardAttendance dbAttendance;

    @Inject
    InsertUser insertUser;

    @Inject
    UpdateUser updateUser;

    @Inject
    UpdateAccount updateAccount;

    public AdministrationAction() {
    }

    public String loginAction() throws ERPCustomException {
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
                return mainPageForward(temp);
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Login Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String selectRole() throws ERPCustomException {
        Userrole selectedRole = roleSelectionBean.getSelectedRole();
        Usr temp = sessionBean.getUsers();
        temp.setRole(selectedRole.getRole());
        sessionBean.setUsers(temp);
        return mainPageForward(temp);

    }

    private String mainPageForward(Usr temp) throws ERPCustomException {
        try {
            if (temp.getRole().getRoleid() == 1 || temp.getRole().getRoleid() == 2 || temp.getRole().getRoleid() == 3) {
                Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_LOGINUSER")));
                Auditing audit = new Auditing(temp, temp.getCompany(), action, "User " + sessionBean.getUsers().getUsername() + " connected on the platform",
                        FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                        FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));

                auditingDAO.save(audit);
                sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
                sessionBean.setPageName(MessageBundleLoader.getMessage("usersPage"));

                return "dashboardAttendanceStatistics?faces-redirect=true";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Masin Page Forward Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public void invalidateSession() {
        FacesUtils.resetManagedBeanJSF2("sessionBean");
        FacesUtils.invalidateSession();
    }

    public void fetchUsers() throws ERPCustomException {
        try {
            List<Usr> users = userDAO.searchUser(dbUsers.getSelectedRole(), dbUsers.getSelectedCompany(), dbUsers.getSelectedDepartment(),
                    dbUsers.getSelectedSector(), dbUsers.getSurname(), dbUsers.getActive());
            dbUsers.setSearchUsers(users);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From fetch users Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String insertUser() throws ERPCustomException {
        try {
            List<Usr> usrs = userDAO.findByProperty("username", insertUser.getUsername().trim());
            if (!insertUser.getPassword().trim().equals(insertUser.getRepassword().trim())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("passwordDontMatch"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }
            if (usrs.size() == 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            usrs = userDAO.findByProperty("email", insertUser.getEmail().trim());

            if (usrs.size() == 1) {
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

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_INSERTUSER")));
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
            throw new ERPCustomException("Throw From insert User Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public List<Usr> completeUser(String username) throws ERPCustomException {
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
            throw new ERPCustomException("Throw From complete User (username) Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String updateUser() throws ERPCustomException {
        try {
            List<Usr> usrs = userDAO.findByProperty("username", updateUser.getUser().getUsername().trim());
            if (usrs.size() >= 1 && !usrs.get(0).equals(updateUser.getUser())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                PrimeFaces.current().ajax().update("alertPanel");
                return "";
            }

            usrs = userDAO.findByProperty("email", updateUser.getUser().getEmail().trim());

            if (usrs.size() >= 1 && !usrs.get(0).equals(updateUser.getUser())) {
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

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEUSER")));
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
            throw new ERPCustomException("Throw From Update User Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String updateAccount() throws ERPCustomException {
        try {
            List<Usr> usrs = userDAO.findByProperty("email", updateAccount.getUser().getEmail().trim());

            if (usrs.size() >= 1 && !usrs.get(0).equals(updateAccount.getUser())) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("emailAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("PF('generalAlertWidget').show();");
                return "";
            }

            userDAO.update(updateAccount.getUser());

            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_UPDATEACCOUNT")));
            Auditing audit = new Auditing(sessionBean.getUsers(), sessionBean.getUsers().getCompany(), action, "Account " + updateAccount.getUser().getUsername() + " updated",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));
            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("accountUpdated"));
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ATTENDANCE_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("attendancePage"));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Update Account Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String resetPassword() throws ERPCustomException {
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
            throw new ERPCustomException("Throw From Reset Password Action", e, sessionBean.getUsers(), "errMsg_GeneralError");
        }
    }

    public String goUpdateUser(long userID) {
        return "updateUser?faces-redirect=true&userID=" + userID;
    }

    public String goResetPasswordEmail() {
        sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_RESET_PASSWORD"));
        sessionBean.setPageName(MessageBundleLoader.getMessage("passwordReminder"));
        return "resetPassword?faces-redirect=true";
    }

    public String sendResetPasswordEmail() throws ERPCustomException {
        try {
            String email = resetBean.getEmail().trim();
            List<Usr> users = userDAO.findByProperty("email", email);
            if (users != null && users.size() == 1) {
                Usr user = users.get(0);
                String username = user.getUsername();

                String link = SystemParameters.getInstance().getProperty("resetPassword_link") + user.getPassword() + "&userid=" + user.getUserid();
                String host = SystemParameters.getInstance().getProperty("gmail_host");
                String port = SystemParameters.getInstance().getProperty("gmail_port");
                String account = SystemParameters.getInstance().getProperty("gmail_account");
                String password = SystemParameters.getInstance().getProperty("gmail_password");
                String subject = SystemParameters.getInstance().getProperty("email_subject") + username;
                String body = SystemParameters.getInstance().getProperty("email_body") + link;

                if (ErpUtil.sendFromGMail(host, port, account, password, user.getEmail(), subject, body)) {
                    FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("successUserEmail"));
                } else {
                    FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("errorSendingEmail"));
                }
                return "";
            } else {
                FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("falseUserEmail"));
                return "";
            }

            //return "loginPage?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Send Reset Password Email Action", e, sessionBean.getUsers(), "errMsg_GeneralError");

        }
    }

    public void resetPasswordEmail() throws ERPCustomException {
        try {
            Usr user = resetBean.getUser();
            String hashedPassword = ErpUtil.getSaltedHash(resetBean.getPassword());
            user.setPassword(hashedPassword);

            userDAO.update(user);
            Action action = auditingDAO.getAction(Long.parseLong(SystemParameters.getInstance().getProperty("ACT_RESETPASSWORD")));
            Auditing audit = new Auditing(user, user.getCompany(), action, "User " + user.getUsername() + " password was reseted using the email procedure",
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.DATEPATTERN),
                    FormatUtils.formatDateToTimestamp(new Date(), FormatUtils.FULLDATEPATTERN));

            auditingDAO.save(audit);

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Reset Password Email Action", e, sessionBean.getUsers(), "errMsg_GeneralError");

        }
    }

    public String logoutAction() throws ERPCustomException {
        try {
            FacesUtils.resetManagedBeanJSF2("sessionBean");
            FacesUtils.invalidateSession();
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/login.jsf?faces-redirect=true");
            return "./login?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Logout Action", e, sessionBean.getUsers(), "errMsg_GeneralError");

        }
    }

    public String logoutAction(int t) throws ERPCustomException {
        try {
            FacesUtils.resetManagedBeanJSF2("sessionBean");
            FacesUtils.invalidateSession();
            return "/login?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            throw new ERPCustomException("Throw From Logout(0) Action", e, sessionBean.getUsers(), "errMsg_GeneralError");

        }
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    public String getPropertyValue(String key) {
//        String propertyValue = SystemParameters.getInstance().getProperty(key);
//        return propertyValue;
//    }
//
//    public String auditControl() {
//        try {
//            auditBean.reset();
//            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_AUDIT_CONTROL"));
//            sessionBean.setPageName(MessageBundleLoader.getMessage("audit"));
//            return "auditControl?faces-redirect=true ";
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//            return "";
//        }
//    }
//
//    public String userAdmin() {
//        try {
//            userBean.reset();
//            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
//            sessionBean.setPageName(MessageBundleLoader.getMessage("userAdmin"));
//            return "userMgt?faces-redirect=true ";
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//            return "";
//        }
//    }
//
//    public void autocompleteUsernameSelectUser(SelectEvent event) {
//        try {
//
//            Usr user = auditBean.getSelectUser();
//            auditBean.setSearchUser(user);
//            auditBean.setSelectUser(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public String resetSearchAudit() {
//        try {
//            return auditControl();
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//            return "";
//        }
//    }
//
//    public void searchAudit() {
//        try {
//            Timestamp from = null;
//            if (auditBean.getSearchFromActionDate() != null) {
//                from = FormatUtils.formatDateToTimestamp(auditBean.getSearchFromActionDate());
//            }
//
//            Timestamp to = null;
//            if (auditBean.getSearchToActionDate() != null) {
//                to = FormatUtils.formatDateToTimestamp(auditBean.getSearchToActionDate());
//            }
//
//            AuditingDAO dao = new AuditingDAO();
//            List<Auditing> auditings = dao.searchAudit(auditBean.getSearchUser(), auditBean.getSearchAction(), from, to, auditBean.getSearchCompany());
//            auditBean.setAudits(auditings);
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public String resetUserAdmin() {
//        try {
//            return userAdmin();
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//            return "";
//        }
//    }
//
//    public void goInsertUser() {
//        try {
//            Usr user = new Usr();
//            user.setActive(BigDecimal.ONE);
//            userBean.setRoles(new ArrayList<Role>(0));
//
//            userBean.setUser(user);
//            FacesUtils.callRequestContext("createUserDialogWidget.show()");
//            FacesUtils.updateHTMLComponnetWIthJS("createUserPanelID");
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public void goResetPasword() {
//        try {
//            FacesUtils.callRequestContext("PF('resetPasswordDialogWidget').show()");
//            FacesUtils.updateHTMLComponnetWIthJS("resetPasswordUserPanelID");
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public void deleteUser() {
//        try {
//            Usr user = userBean.getUser();
//            user.setActive(BigDecimal.ZERO);
//            user = persistenceHelper.editPersist(user);
//            persistenceUtil.audit(sessionBean.getUsers(), Long.parseLong(SystemParameters.getInstance().getProperty("ACT_DELETEUSER")), "User " + user.getUsername() + " removed");
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userDeleted"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public void goErrorrrrrrrrrrrrrrrr(Exception ex) {
//        try {
//            logger.error("-----------AN ERROR HAPPENED !!!! -------------------- : " + ex.toString());
//            if (sessionBean.getUsers() != null) {
//                logger.error("User=" + sessionBean.getUsers().getUsername());
//            }
//            logger.error("Cause=" + ex.getCause());
//            logger.error("Class=" + ex.getClass());
//            logger.error("Message=" + ex.getLocalizedMessage());
//            logger.error(ex, ex);
//            logger.error("--------------------- END OF ERROR --------------------------------------------------------\n\n");
//
//            ErrorBean errorBean = (ErrorBean) FacesUtils.getManagedBean("errorBean");
//            errorBean.reset();
//            errorBean.setErrorMSG(MessageBundleLoader.getMessage(sessionBean.getErrorMsgKey()));
//            //FacesUtils.redirectAJAX("./templates/error.jsf?faces-redirect=true");
//            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/common/error.jsf?faces-redirect=true");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void closeAlertDlg() {
//        sessionBean.setShowGeneralDialog(false);
//        sessionBean.setShowMsgDialog(false);
//    }
//}
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
