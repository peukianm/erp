/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */
package erp.util;

import erp.bean.ErrorBean;
import erp.bean.SessionBean;
import erp.entities.Usr;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
//import org.primefaces.context.RequestContext;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 * JSF utilities.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class FacesUtils {

    public static final String INSIDE = "1";
    public static final String OUTSIDE = "0";
    public static final double NOCLOSEDDRUG = 1;
    public static final double CLOSEDDRUGS = 0;
    private static Properties buildProperties = null;
    ;

    /**
     * Get servlet context.
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getExternalContext();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ServletRequest getRequest() {
        return (ServletRequest) getExternalContext().getRequest();
    }

    public static ServletResponse getResponse() {
        return (ServletResponse) getExternalContext().getResponse();
    }

    public static HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().
                getExternalContext().getSession(create);
    }

    public static String getContextPath() {
        return ((HttpServletRequest) getRequest()).getContextPath();
    }

    public static void invalidateSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public static ValueExpression createValueExpression(String expressionString) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        ValueExpression valueExpression = context.getApplication().getExpressionFactory().
                createValueExpression(elContext, expressionString, Object.class);
        return valueExpression;
    }

    public static MethodExpression createMethodExpression(String expressionString) {
        return createMethodExpression(expressionString, null);
    }

    public static MethodExpression createMethodExpression(String expressionString, Class<?>[] paramTypes) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        MethodExpression methodExpression = context.getApplication().getExpressionFactory().
                createMethodExpression(elContext, expressionString, null,
                paramTypes == null ? new Class[0] : paramTypes);
        return methodExpression;
    }

    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(elc, getJsfEl(beanName), Object.class);
        return ve.getValue(elc);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getManagedBeanJSF2(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
    }

    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */
    public static void resetManagedBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
        ef.createValueExpression(elc, getJsfEl(beanName),
                Object.class).setValue(elc, null);
    }

    public static void resetManagedBeanJSF2(String beanName) {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(beanName);
    }

    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
    }

    /**
     * Store the managed bean inside the request scope.
     *
     * @param beanName the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInRequest(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(beanName, managedBean);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Get named request map object value from request map.
     *
     * @param name the name of the key in map
     * @return the key value if any, null otherwise.
     */
    public static Object getRequestMapValue(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
    }

    /**
     * Gets the action attribute value from the specified event for the given name. Action attributes are specified by &lt;f:attribute /&gt;.
     *
     * @param event
     * @param name
     * @return
     */
    public static String getActionAttribute(ActionEvent event, String name) {
        return (String) event.getComponent().getAttributes().get(name);
    }

    public static String getBuildAttribute(String name) {
        if (buildProperties != null) {
            return buildProperties.getProperty(name, "unknown");
        }
        InputStream is = null;
        try {
            is = getServletContext().getResourceAsStream("/WEB-INF/buildversion.properties");
            buildProperties = new java.util.Properties();
            buildProperties.load(is);
        } catch (Throwable e) {
            is = null;
            buildProperties = null;
            return "unknown";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable t) {
                }
            }
        }
        return buildProperties.getProperty(name, "unknown");
    }

    /**
     * Gest parameter value from the the session scope.
     *
     * @param name name of the parameter
     * @return the parameter value if any.
     */
    public static String getSessionParameter(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession mySession = myRequest.getSession();
        return myRequest.getParameter(name);
    }

    public static Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    public static Map<String, Object> getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    public static Map<String, Object> getViewMap() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
    }

    public static Cookie[] getCookies() {
        return ((HttpServletRequest) getRequest()).getCookies();
    }

    public static void addCookie(Cookie cookie) {
        ((HttpServletResponse) getResponse()).addCookie(cookie);
    }

    /**
     * Get parameter value from the web.xml file
     *
     * @param parameter name to look up
     * @return the value of the parameter
     */
    public static String getFacesParameter(String parameter) {
        // Get the servlet context based on the faces context
        ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        // Return the value read from the parameter
        return sc.getInitParameter(parameter);
    }

    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg the information message
     */
    public static void addWarnMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }

    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    /**
     * Finds component with the given id
     *
     * @param c component check children of.
     * @param id id of component to search for.
     * @return found component if any.
     */
    public static UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public static UIComponent findComponent(String id) {
        try {
            return FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * Finds all component with the given id. Component id's are formed from the concatination of parent component ids. This search will find all componet in
     * the component tree with the specified id as it is possible to have the same id used more then once in the component tree
     *
     * @param root component check children of.
     * @param id id of component to search for.
     * @param foundComponents list of found component with the specified id.
     * @return found component if any.
     */
    public static void findAllComponents(UIComponent root, String id,
            List<UIComponent> foundComponents) {
        if (id.equals(root.getId())) {
            foundComponents.add(root);
        }
        Iterator<UIComponent> kids = root.getFacetsAndChildren();
        while (kids.hasNext()) {
            findAllComponents(kids.next(), id, foundComponents);
        }
    }

    /**
     * Add error message to a specific client.
     *
     * @param clientId the client id
     * @param msg the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

    /**
     * Creates an array of
     * <code>SelectItem</code>s from collection of
     * <code>String</code>s.
     *
     * @param values an array of <code>SelectItem</code> values.
     * @return array of JSF objects representing items.
     */
    public static SelectItem[] createSelectItems(Collection<String> values) {
        SelectItem[] items = new SelectItem[values.size()];
        int index = 0;
        for (String value : values) {
            items[index++] = new SelectItem(value);
        }
        return items;
    }

    /**
     * Creates an array of
     * <code>SelectItem</code>s from array of
     * <code>String</code>s.
     *
     * @param values an array of <code>SelectItem</code> values.
     * @return array of JSF objects representing items.
     */
    public static SelectItem[] createSelectItems(String[] values) {
        SelectItem[] items = new SelectItem[values.length];
        for (int i = 0; i < items.length; ++i) {
            items[i] = new SelectItem(values[i]);
        }
        return items;
    }

    /**
     * Creates an array of
     * <code>SelectItem</code>.
     *
     * @param values an array of values.
     * @param labels an array of labels.
     * @return array of JSF objects representing items.
     */
    public static SelectItem[] createSelectItems(String[] values, String[] labels) {
        SelectItem[] items = new SelectItem[values.length];
        for (int i = 0; i < items.length; ++i) {
            items[i] = new SelectItem(values[i], labels[i]);
        }
        return items;
    }

    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }

    public static void redirect(String page) throws IOException {
        HttpServletResponse response = (HttpServletResponse) getResponse();
        response.sendRedirect(page);
    }

    public static void redirectAJAX(String page) throws IOException {
        getExternalContext().redirect(page);
    }

    public static void redirectWithNavigationID(String navigationID) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().getNavigationHandler().handleNavigation(context, null, navigationID);

    }

    public static void redirectWithViewRoot(String page) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot =
                fc.getApplication().getViewHandler().createView(fc, page);
        fc.setViewRoot(viewRoot);
        fc.renderResponse();

    }

    public static void refreshView() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
    }

    public static void partialViewContextComponentReset() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PartialViewContext partialViewContext = facesContext.getPartialViewContext();
        Collection<String> renderIds = partialViewContext.getRenderIds();
        UIViewRoot viewRoot = facesContext.getViewRoot();

//    	ViewHandler viewHandler = facesContext.getApplication().getViewHandler();    	
//    	UIViewRoot viewRoot = viewHandler.createView(facesContext, facesContext.getViewRoot().getViewId());

        for (String renderId : renderIds) {
            UIComponent component = viewRoot.findComponent(renderId);
            javax.faces.component.html.HtmlForm form;
            EditableValueHolder input = (EditableValueHolder) component;
            input.resetValue();
        }

    }

    public static void resetFormComponents() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PartialViewContext ajaxContext = facesContext.getPartialViewContext();
        UIComponent root = facesContext.getViewRoot();
        Collection<String> renderIds = ajaxContext.getRenderIds();
        for (String renderId : renderIds) {
            UIComponent form = findFormComponent(root, renderId);
            if (form != null) {
                clearComponentHierarchy(form);
            }
        }
    }

    public static void resetFormComponents(String formid) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent root = facesContext.getViewRoot();
        UIComponent form = findFormComponent(root, formid);
        if (form != null) {
            clearComponentHierarchy(form);
        }
    }

    public static UIComponent getUIComponent(String componentID) {
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        return viewRoot.findComponent(componentID);
    }

    public static void resetSpecificComponents(Collection<String> componentIDs) {
        for (String componentID : componentIDs) {
            UIComponent component = getUIComponent(componentID);
            if (component instanceof EditableValueHolder) {
                EditableValueHolder editableValueHolder = (EditableValueHolder) component;
                editableValueHolder.setSubmittedValue(null);
                editableValueHolder.setValue(null);
                editableValueHolder.setLocalValueSet(false);
                editableValueHolder.setValid(true);
            }

        }

    }

    private static void clearComponentHierarchy(UIComponent pComponent) {

        if (pComponent instanceof DataTable) {
            DataTable table = (DataTable) pComponent;
            table.setSelection(null);
        }
        if (pComponent.isRendered()) {            
            if (pComponent instanceof EditableValueHolder) {
                EditableValueHolder editableValueHolder = (EditableValueHolder) pComponent;
                editableValueHolder.setSubmittedValue(null);
                editableValueHolder.setValue(null);
                editableValueHolder.setLocalValueSet(false);
                editableValueHolder.setValid(true);
            }

            for (Iterator<UIComponent> iterator = pComponent.getFacetsAndChildren(); iterator.hasNext();) {
                clearComponentHierarchy(iterator.next());
            }

        }
    }

    private static UIComponent findFormComponent(UIComponent base, String id) {
        if (id.equals(base.getId())) {
            return base;
        }
        UIComponent kid = null;
        UIComponent result = null;
        Iterator kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = (UIComponent) kids.next();
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = findFormComponent(kid, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public static void callRequestContext(String command) {
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.execute(command);
        PrimeFaces.current().executeScript(command);
    }

    public static void updateHTMLComponnetWIthJS(String components) {
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update(components);
        PrimeFaces.current().ajax().update(components);
    }

    public static void addRequestContextCallBaclParam(String argName, Object argValue) {
        //context.addCallbackParam(argName, argValue);
         PrimeFaces.current().ajax().addCallbackParam(argName,argValue);
    }
    
    public static void addGrowlMessage(String msg1, String msg2) {
        FacesContext context = FacesContext.getCurrentInstance();            
        context.addMessage(null, new FacesMessage(msg1, msg2));  
    }
    
   
    
//    public static void partialViewUpdate(String componentID){
//    	RequestContext context = RequestContext.getCurrentInstance();
//		context.addPartialUpdateTarget(componentID); 	
//    }
    //basic parameter  
}