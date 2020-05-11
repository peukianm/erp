/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.util;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author peukianm
 */
@Stateless
public class PersistenceHelper  {
        
    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public <T extends Object> T findGeneric(Class<T> arg0, Object arg1) {
        return getEntityManager().find(arg0, arg1);
    }

    public void create(Object entity) {
        getEntityManager().persist(entity);
    }

    public void edit(Object entity) {
        getEntityManager().merge(entity);
    }

    public <T extends Object> T editPersist(T entity) {
        return getEntityManager().merge(entity);
    }

    public void remove(Object entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public Object find(Class entityClass, Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<Object> findAll(Class entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Object> findRange(Class entityClass, int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);

        return q.getResultList();
    }

    public int count(Object entity) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entity.getClass());
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return ((Long) q.getSingleResult()).intValue();
    }

}
