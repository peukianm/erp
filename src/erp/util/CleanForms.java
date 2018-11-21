package erp.util;

import java.util.Collection;
import java.util.Iterator;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class CleanForms implements ActionListener {

    public void processAction(ActionEvent event) throws AbortProcessingException {        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PartialViewContext ajaxContext = facesContext.getPartialViewContext();
        UIComponent root = facesContext.getViewRoot();
        Collection<String> renderIds = ajaxContext.getRenderIds();
        for (String renderId : renderIds) {
            UIComponent form = findComponent(root, renderId);
            if (form != null) {
                clearComponentHierarchy(form);
            }
        }
    }
    
    
    public void process() throws AbortProcessingException {        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PartialViewContext ajaxContext = facesContext.getPartialViewContext();
        UIComponent root = facesContext.getViewRoot();        
        Collection<String> renderIds = ajaxContext.getRenderIds();
        for (String renderId : renderIds) {            
            UIComponent form = findComponent(root, renderId);            
            if (form != null) {
                clearComponentHierarchy(form);
            }
        }
    }

    private void clearComponentHierarchy(UIComponent pComponent) {

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

    private static UIComponent findComponent(UIComponent base, String id) {
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
            result = findComponent(kid, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }
}
