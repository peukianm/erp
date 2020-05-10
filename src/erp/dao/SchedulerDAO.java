/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Companytask;
import erp.entities.Scheduletaskdetail;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Stateless
public class SchedulerDAO implements Serializable {

    private static final Logger logger = LogManager.getLogger(SchedulerDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;

    public Scheduletaskdetail getScheduleTaskDetail(long id) {
        return entityManager.find(Scheduletaskdetail.class, id);
    }

    public List<Scheduletaskdetail> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Schesuletaskdetail e");
        return query.getResultList();
    }

    public void saveTaskDetails(Scheduletaskdetail scheduletaskdetail) {
        entityManager.persist(scheduletaskdetail);
    }

    public void updateTaskDetails(Scheduletaskdetail scheduletaskdetail) {
        entityManager.merge(scheduletaskdetail);
    }
    
    @SuppressWarnings("unchecked")
    public Companytask findCtask(long companyid, long taskid) {

        try {
            final String queryString = "select model from Scheduletaskdetail model where "
                    + " model.active = 1 and "
                    + " model.companyid = " + companyid + " and "
                    + " model.taskid = " + taskid ;
            Query query = entityManager.createQuery(queryString);            
            return (Companytask)query.getResultList().get(0);
        } catch (RuntimeException re) {
            logger.error("Error on getting findCtask entity", re);
            throw re;
        }
    }

    public void updateCtask(Companytask companyTask) {
        entityManager.merge(companyTask);
    }

  
    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Scheduletaskdetail> findByPropertyScheduletaskdetail(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Scheduletaskdetail model where model." + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }

                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    /**
     * 
     * @param
    rowStartIdxAndCount Optional int varargs
    . rowStartIdxAndCount 
    [0] specifies the the row index in the query result
    -set to begin collecting the
    * results.rowStartIdxAndCount 
    [1] specifies the the maximum count of results to return.
     * @
    return List<Auditing> all Auditing entities

    */
    @SuppressWarnings("unchecked")
    public List<Scheduletaskdetail> findAllScheduletaskdetail(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Scheduletaskdetail model";
            Query query = entityManager.createQuery(queryString);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }

                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            logger.error("Error on finding entity", re);
            throw re;
        }
    }
}
