/**
 *
 */
package erp.action;

import erp.bean.*;
import erp.dao.*;
import erp.entities.*;
import erp.util.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.event.SelectEvent;

public class AdministrationAction implements Serializable {

    UsersDAO userDAO = new UsersDAO();
    private static final Logger logger = LogManager.getLogger(AdministrationAction.class);

    //private SessionBean sessionBean = (SessionBean) FacesUtils.getManagedBean("sessionBean");
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

    @Resource 
    UserTransaction usrTransaction; 
    
    public AdministrationAction() {
    }

    public String loginAction() {
        try {            
            Users temp = null;
            List<Users> users = userDAO.findByProperty("username", userBean.getUsername());
            if (users == null || users.size() > 0) {                
                if (ErpUtil.check(userBean.getPassword(), users.get(0).getPassword())) 
                    temp = users.get(0);
                else 
                    temp = null;                
            } else 
                temp = null;
            
            if (temp == null) {
                userBean.setPassword(null);
                sessionBean.setErrorMsgKey("errMsg_InvalidCredentials");
                FacesUtils.addErrorMessage(MessageBundleLoader.getMessage("errMsg_InvalidCredentials"));
                return "loginPage";
            }

            List<Userroles> userroles = userDAO.getUserRoles(temp);
            RoleSelectionBean roleSelectionBean = (RoleSelectionBean) FacesUtils.getManagedBean("roleSelectionBean");
            roleSelectionBean.setUserroles(userroles);
            temp.setUserroleses(userroles);
            sessionBean.setUsers(temp);

            if (userroles.size() > 1) {
                FacesUtils.callRequestContext("PF('selectRoleDialog').show()");
            } else if (userroles.size() == 1) {
                temp.setRole(userroles.get(0).getRole());


//                Server server = (Server)SessionManager.getManager().getSession("sdsdsddsd", HttpSession.class.getClassLoader());
//            Session session = (Session) server.acquireClientSession();
//                SessionFactory sessionFactory = new SessionFactory("default");
//                Session session = sessionFactory.getSharedSession();
//                UnitOfWork uow = session.acquireUnitOfWork();           
//                uow.commit();

               

//               usrTransaction.begin();
//                Company comp = new Company();
//                comp.setName("1111111111111111111");
//                comp.setActive(BigDecimal.ONE);
//                persistenceHelper.create(comp);
//                             
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
            }
            return "";
        } catch (Exception e) {
//            try {
//                usrTransaction.rollback();
//            } catch (IllegalStateException ex) {
//                java.util.logging.Logger.getLogger(AdministrationAction.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SecurityException ex) {
//                java.util.logging.Logger.getLogger(AdministrationAction.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SystemException ex) {
//                java.util.logging.Logger.getLogger(AdministrationAction.class.getName()).log(Level.SEVERE, null, ex);
//            }
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String selectRole() {
        //RoleSelectionBean roleSelectionBean = (RoleSelectionBean) FacesUtils.getManagedBean("roleSelectionBean");
        Userroles selectedRole = roleSelectionBean.getSelectedRole();
        Users temp = sessionBean.getUsers();
        temp.setRole(selectedRole.getRole());
        sessionBean.setUsers(temp);

        return mainPageForward(temp);

    }

    private String mainPageForward(Users temp) {
        try {
            if (temp.getRole().getRoleid().intValue() == 1 || temp.getRole().getRoleid().intValue() == 2 || temp.getRole().getRoleid().intValue() == 3) {
                persistenceUtil.audit(temp, new BigDecimal(SystemParameters.getInstance().getProperty("ACT_LOGINUSER")), null);
                sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_ERP_HOME"));
                sessionBean.setPageName(MessageBundleLoader.getMessage("homePage"));
                //return "backend/main?faces-redirect=true";
                return "dashboard?faces-redirect=true";
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

    public String getPropertyValue(String key) {
        String propertyValue = SystemParameters.getInstance().getProperty(key);
        return propertyValue;
    }

    public String auditControl() {
        try {
            //AuditBean auditBean = (AuditBean) FacesUtils.getManagedBean("auditBean");
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
            //UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
            userBean.reset();
            sessionBean.setPageCode(SystemParameters.getInstance().getProperty("PAGE_USER_ADMIN"));
            sessionBean.setPageName(MessageBundleLoader.getMessage("userAdmin"));
            return "userAdmin?faces-redirect=true ";
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public List<Users> completeUser(String username) {
        try {
            if (username != null && !username.trim().isEmpty() && username.trim().length() >= 1) {
                username = username.trim();
                UsersDAO userDAO = new UsersDAO();
                List<Users> users = userDAO.fetchUserAutoCompleteUsername(username);
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

    public void autocompleteUsernameSelectUser(SelectEvent event) {
        try {
            //AuditBean auditBean = (AuditBean) FacesUtils.getManagedBean("auditBean");
            Users user = auditBean.getSelectUser();
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
//            AuditBean auditBean = (AuditBean) FacesUtils.getManagedBean("auditBean");
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

    public void searchUser() {
        try {
            //UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
            UsersDAO dao = new UsersDAO();
            List<Users> users = dao.searchUser(userBean.getSearchByRole(), userBean.getSearchByCompany(), userBean.getSearchByUsername(), userBean.getSearchBySurname());
            userBean.setUsers(users);
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void goInsertUser() {
        try {
            //UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
            Users user = new Users();
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
            //UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
            FacesUtils.callRequestContext("resetPasswordDialogWidget.show()");
            FacesUtils.updateHTMLComponnetWIthJS("resetPasswordUserPanelID");
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public void goUpdateUser() {
        try {
//            UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
            List<Userroles> userroles = userBean.getUser().getUserroleses();
            userBean.setRoles(new ArrayList<Role>(0));
            for (int i = 0; i < userroles.size(); i++) {
                Userroles userrole = userroles.get(i);
                userBean.getRoles().add(userrole.getRole());
            }

            if (userBean.getUser().getActive().equals(BigDecimal.ONE)) {
                userBean.setActive(true);
            } else {
                userBean.setActive(false);
            }

            FacesUtils.callRequestContext("updateUserDialogWidget.show()");
            FacesUtils.updateHTMLComponnetWIthJS("updateUserPanelID");
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String insertUser() {
//        UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
        try {
            List<Role> roles = userBean.getRoles();
            Users user = userBean.getUser();

            List<Users> usrs = userDAO.findByProperty("username", userBean.getUser().getUsername().trim());
            if (usrs.size() == 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("generalAlertWidget.show()");
                return "";
            }

            usrs = userDAO.findByProperty("email", userBean.getUser().getEmail().trim());

            if (usrs.size() == 1) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("emailAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("generalAlertWidget.show()");
                return "";
            }

            List<Userroles> userroles = new ArrayList<Userroles>(0);
            for (int i = 0; i < roles.size(); i++) {
                Role role = roles.get(i);
                Userroles userrole = new Userroles();
                userrole.setUsers(user);
                userrole.setRole(role);
                userroles.add(userrole);
            }

            String hashedPassword = ErpUtil.getSaltedHash(userBean.getPassword());

            user.setPassword(hashedPassword);
            user.setUserroleses(userroles);
            persistenceHelper.create(user);
            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_INSERTUSER")), "User " + user.getUsername() + " inserted");

            FacesUtils.callRequestContext("createUserDialogWidget.hide()");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("newUserInserted"));
            return userAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public void deleteUser() {
//        UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");        
        try {
            Users user = userBean.getUser();
            user.setActive(BigDecimal.ZERO);
            user = persistenceHelper.editPersist(user);
            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_DELETEUSER")), "User " + user.getUsername() + " removed");

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userDeleted"));
        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

    public String updateUser() {
//        UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");
        try {
            Users user = userBean.getUser();
            List<Users> usrs = userDAO.findByProperty("username", userBean.getUser().getUsername().trim());
            if (usrs.size() == 1 && !usrs.get(0).equals(user)) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("useranameAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("generalAlertWidget.show()");
                return "";
            }

            usrs = userDAO.findByProperty("email", userBean.getUser().getEmail().trim());

            if (usrs.size() == 1 && !usrs.get(0).equals(user)) {
                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("emailAlreadyUsed"));
                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
                FacesUtils.callRequestContext("generalAlertWidget.show()");
                return "";
            }

            if (userBean.isActive()) {
                user.setActive(BigDecimal.ONE);
            } else {
                user.setActive(BigDecimal.ZERO);
            }

            //removing
            for (int i = 0; i < user.getUserroleses().size(); i++) {
                Userroles userrole = user.getUserroleses().get(i);
                persistenceHelper.remove(userrole);
            }
            user.setUserroleses(null);

            //adding
            List<Role> roles = userBean.getRoles();
            List<Userroles> userroles = new ArrayList<Userroles>(0);
            for (int i = 0; i < roles.size(); i++) {
                Role role = roles.get(i);
                Userroles userrole = new Userroles();
                userrole.setUsers(user);
                userrole.setRole(role);
                userroles.add(userrole);
            }

            user.setUserroleses(userroles);

            user = persistenceHelper.editPersist(user);
            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_UPDATEUSER")), "User " + user.getUsername() + " updated");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));
            FacesUtils.callRequestContext("updateUserDialogWidget.hide()");
            return userAdmin();

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String resetPassword() {
//        UserBean userBean = (UserBean) FacesUtils.getManagedBean("userBean");        
        try {
            Users user = userBean.getUser();
            String hashedPassword = ErpUtil.getSaltedHash(userBean.getPassword());
            user.setPassword(hashedPassword);

            user = persistenceHelper.editPersist(user);
            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_UPDATEPASSWORD")), "User Password " + user.getUsername() + " updated");

            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));
            FacesUtils.callRequestContext("updateUserDialogWidget.hide()");
            return userAdmin();

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
            return "";
        }
    }

    public String goResetPasswordEmail() {
        try {
            //ResetBean resetBean = (ResetBean)FacesUtils.getManagedBean("resetBean");                        
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
            List<Users> users = userDAO.findByProperty("email", email);
            if (users != null && users.size() == 1) {
                Users user = users.get(0);
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
            Users user = resetBean.getUser();
            String hashedPassword = ErpUtil.getSaltedHash(resetBean.getPassword());
            user.setPassword(hashedPassword);

            user = persistenceHelper.editPersist(user);
            persistenceUtil.audit(user, new BigDecimal(SystemParameters.getInstance().getProperty("ACT_UPDATEPASSWORD")), "User Password " + user.getUsername() + " updated");
            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("userUpdated"));

        } catch (Exception e) {
            e.printStackTrace();
            sessionBean.setErrorMsgKey("errMsg_GeneralError");
            goError(e);
        }
    }

//    public void updateItem() {
//        TemplateEditorBean templateEditorBean = (TemplateEditorBean) FacesUtils.getManagedBean("templateEditorBean");
//        UserTransaction userTransaction = null;
//        try {
//            Item item = templateEditorBean.getItem();
//            userTransaction = persistenceHelper.getUserTransaction();
//            userTransaction.begin();
//            for (int i = 0; i < item.getItemspecifications().size(); i++) {
//                Itemspecification is = item.getItemspecifications().get(i);
//                persistenceHelper.remove(is);
//            }
//
//            item.setItemspecifications(null);
//            List<Specification> specs = templateEditorBean.getSpecificationPickList().getTarget();
//            List<Itemspecification> ispecs = new ArrayList<Itemspecification>(0);
//            for (int i = 0; i < specs.size(); i++) {
//                Specification specification = specs.get(i);
//                Itemspecification itemSpecification = new Itemspecification();
//                itemSpecification.setSpecification(specification);
//                itemSpecification.setActive(BigDecimal.ONE);
//                itemSpecification.setItem(item);
//                itemSpecification.setOrdered(new BigDecimal(i));
//                ispecs.add(itemSpecification);
//            }
//            item.setItemspecifications(ispecs);
//            item = persistenceHelper.editPersist(item);
//            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_UPDATETEMPLATEPRODUCT")), "Protorype product " + item.getName() + " updated");
//            userTransaction.commit();
//
//            templateEditorBean.reset();
//            ApplicationBean applicationBean = (ApplicationBean) FacesUtils.getManagedBean("applicationBean");
//            applicationBean.setItems(null);
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("prototypeProductUpdated"));
//            FacesUtils.callRequestContext("updateItemDialogWidget.hide()");
//
//        } catch (Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public void updateSpecification() {
//        TemplateEditorBean templateEditorBean = (TemplateEditorBean) FacesUtils.getManagedBean("templateEditorBean");
//        UserTransaction userTransaction = null;
//        try {
//            Specification specification = templateEditorBean.getSpecification();
//            userTransaction = persistenceHelper.getUserTransaction();
//            userTransaction.begin();
//
//
//            if (templateEditorBean.isDimension()) {
//                specification.setDimension(BigDecimal.ONE);
//            } else {
//                specification.setDimension(BigDecimal.ZERO);
//            }
//
//            if (templateEditorBean.isColor()) {
//                specification.setColor(BigDecimal.ONE);
//            } else {
//                specification.setColor(BigDecimal.ZERO);
//            }
//
//            if (templateEditorBean.isFreetext()) {
//                specification.setFreetext(BigDecimal.ONE);
//            } else {
//                specification.setFreetext(BigDecimal.ZERO);
//            }
//
//            if (templateEditorBean.isMultiinsert()) {
//                specification.setMultipleinsert(BigDecimal.ONE);
//            } else {
//                specification.setMultipleinsert(BigDecimal.ZERO);
//            }
//
//            if (templateEditorBean.isMultivalue()) {
//                specification.setMultiplevalues(BigDecimal.ONE);
//            } else {
//                specification.setMultiplevalues(BigDecimal.ZERO);
//            }
//
//
//            List<Svalue> svalues = templateEditorBean.getSvaluePickList().getTarget();            
//            
//            if (specification.getFreetext().equals(BigDecimal.ZERO)) {                
//                if (svalues.size() == 0) {
//                    sessionBean.setAlertMessage(MessageBundleLoader.getMessage("noValuesSelected"));
//                    FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                    FacesUtils.callRequestContext("generalAlertWidget.show()");
//                    return;
//                }
//            }
// 
//            
//            if (specification.getFreetext().equals(BigDecimal.ONE)) {                
//                if (svalues.size() > 0) {
//                    sessionBean.setAlertMessage(MessageBundleLoader.getMessage("valuesSelected"));
//                    FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                    FacesUtils.callRequestContext("generalAlertWidget.show()");
//                    return;
//                }
//            }
//            
//             if (specification.getDimension().equals(BigDecimal.ONE)) { 
//                 if (svalues.size() > 0) {
//                    sessionBean.setAlertMessage(MessageBundleLoader.getMessage("dimensionSelected"));
//                    FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                    FacesUtils.callRequestContext("generalAlertWidget.show()");
//                    return;
//                }
//             }
//             
//             
//              if (specification.getDimension().equals(BigDecimal.ONE) && specification.getColor().equals(BigDecimal.ONE)) {                  
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("dimensionColor"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return;                
//             }
//              
//              
//               if (specification.getMultiplevalues().equals(BigDecimal.ONE) && specification.getFreetext().equals(BigDecimal.ONE)) {                  
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("multivaluesFreeText"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return;                
//             }
//              
//            
//            for (int i = 0; i < specification.getSpecificationvalues().size(); i++) {
//                Specificationvalue specificationValue = specification.getSpecificationvalues().get(i);                
//                persistenceHelper.remove(specificationValue);
//            }
//  
//            specification.setSpecificationvalues(null);
//            List<Specificationvalue> specificationvalues = new ArrayList<Specificationvalue>(0);
//            for (int i = 0; i < svalues.size(); i++) {
//                Svalue svalue = svalues.get(i);
//                Specificationvalue specificationvalue = new Specificationvalue();
//                specificationvalue.setActive(BigDecimal.ONE);
//                specificationvalue.setSpecification(specification);
//                specificationvalue.setSvalue(svalue);
//                specificationvalue.setOrdered(new BigDecimal(i));
//                specificationvalues.add(specificationvalue);
//            }
//            specification.setSpecificationvalues(specificationvalues);
//
//            specification = persistenceHelper.editPersist(specification);
//            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_UPDATETEMPLATEPRODUCTSPEC")), "Prototype product specification " + specification.getName() + " updated");
//            userTransaction.commit();
//             
//            templateEditorBean.reset();
//            ApplicationBean applicationBean = (ApplicationBean) FacesUtils.getManagedBean("applicationBean");
//            applicationBean.setSpecifications(null);
//            applicationBean.setItems(null);
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("specificationUpdated"));
//            FacesUtils.callRequestContext("updateSpecificationDialogWidget.hide()");
//
//        } catch (Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//    public void updateSvalue() {
//        TemplateEditorBean templateEditorBean = (TemplateEditorBean) FacesUtils.getManagedBean("templateEditorBean");
//        UserTransaction userTransaction = null;
//        try {
//            Svalue svalue = templateEditorBean.getSvalue();
//            userTransaction = persistenceHelper.getUserTransaction();
//            userTransaction.begin();
//            
//            
//            svalue = persistenceHelper.editPersist(svalue);
//            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_UPDATETEMPLATEPRODUCTSPECVALUE")), "Protorype product speccification value " + svalue.getName() + " updated");
//            userTransaction.commit();
//
//            templateEditorBean.reset();
//            ApplicationBean applicationBean = (ApplicationBean) FacesUtils.getManagedBean("applicationBean");
//            applicationBean.setSvalues(null);
//            applicationBean.setSpecifications(null);
//            applicationBean.setItems(null);
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("specificationValueUpdated"));
//            FacesUtils.callRequestContext("updateSvalueDialogWidget.hide()");
//
//        } catch (Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//    
//    
//    
//
//    public void removeItem() {
//        TemplateEditorBean templateEditorBean = (TemplateEditorBean) FacesUtils.getManagedBean("templateEditorBean");
//        UserTransaction userTransaction = null;
//        try {
//            Item item = templateEditorBean.getItem();
//            
//            ProductDAO dao = new ProductDAO();            
//            List<Product> products = dao.findByProperty("item", item);
//            if (products.size()>0) {
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("itemInserted"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return;
//            }
//            
//            userTransaction = persistenceHelper.getUserTransaction();
//            userTransaction.begin();
//            persistenceHelper.remove(item);
//            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_DELETETEMPLATEPRODUCT")), "Prototype Product " + item.getName() + " deleted");
//            userTransaction.commit();
//
//            templateEditorBean.reset();
//            ApplicationBean applicationBean = (ApplicationBean) FacesUtils.getManagedBean("applicationBean");
//            applicationBean.setItems(null);
//
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("prototypeProductDeleted"));
//            FacesUtils.updateHTMLComponnetWIthJS("");
//        } catch (Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//
//    public void removeSpecification() {
//        TemplateEditorBean templateEditorBean = (TemplateEditorBean) FacesUtils.getManagedBean("templateEditorBean");
//        UserTransaction userTransaction = null;
//        try {
//            Specification specification = templateEditorBean.getSpecification();
//            
//            ProductspecificationDAO dao = new ProductspecificationDAO();            
//            List<Productspecification> productspecifications = dao.findByProperty("specification", specification);
//            if (productspecifications.size()>0) {
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("specificationValInserted"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return;
//            }
//            
//            userTransaction = persistenceHelper.getUserTransaction();
//            userTransaction.begin();
//            persistenceHelper.remove(specification);
//            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_DELETETEMPLATEPRODUCTSPEC")), "Prototype Product Specification " + specification.getName() + " deleted");
//            userTransaction.commit();
//
//            templateEditorBean.reset();
//            ApplicationBean applicationBean = (ApplicationBean) FacesUtils.getManagedBean("applicationBean");
//            applicationBean.setSpecifications(null);
//            applicationBean.setItems(null);
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("specificationDeleted"));
//            FacesUtils.updateHTMLComponnetWIthJS("");
//        } catch (Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
//    
//    
//    
//    
//    public void removeSvalue() {
//        TemplateEditorBean templateEditorBean = (TemplateEditorBean) FacesUtils.getManagedBean("templateEditorBean");
//        UserTransaction userTransaction = null;
//        try {
//            Svalue svalue = templateEditorBean.getSvalue();
//            ProductvalueDAO dao = new ProductvalueDAO();            
//            List<Productvalue> productvalues = dao.findByProperty("svalue", svalue);
//            if (productvalues.size()>0) {
//                sessionBean.setAlertMessage(MessageBundleLoader.getMessage("svalueInserted"));
//                FacesUtils.updateHTMLComponnetWIthJS("alertPanel");
//                FacesUtils.callRequestContext("generalAlertWidget.show()");
//                return;
//            }
//            
//                    
//            userTransaction = persistenceHelper.getUserTransaction();
//            userTransaction.begin();
//            
//            
////            Iterator itr = svalue.getSpecificationvalues().iterator();
////            while (itr.hasNext()) {
////                Specificationvalue sv = (Specificationvalue)itr.next();
////                persistenceHelper.remove(sv);
////            }
//            
//            
//            persistenceHelper.remove(svalue);
//            persistenceUtil.audit(sessionBean.getUsers(), new BigDecimal(SystemParameters.getInstance().getProperty("ACT_DELETETEMPLATEPRODUCTSPECVALUE")), "Prototype Product specification value " + svalue.getName() + " deleted");
//            userTransaction.commit();
//
//            templateEditorBean.reset();
//            ApplicationBean applicationBean = (ApplicationBean) FacesUtils.getManagedBean("applicationBean");
//            applicationBean.setSvalues(null);
//            applicationBean.setSpecifications(null);
//            applicationBean.setItems(null);
//
//
//            FacesUtils.addInfoMessage(MessageBundleLoader.getMessage("specificationValueDeleted"));
//            FacesUtils.updateHTMLComponnetWIthJS("");
//        } catch (Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            sessionBean.setErrorMsgKey("errMsg_GeneralError");
//            goError(e);
//        }
//    }
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
            FacesUtils.redirectAJAX(FacesUtils.getContextPath() + "/common/error.jsf?faces-redirect=true");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String logoutAction() {
        try {
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
