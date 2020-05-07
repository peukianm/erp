/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Staff;
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
 * @author user
 */
@Stateless
public class staffDAO {
     private static final Logger logger = LogManager.getLogger(staffDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;
    
    public Staff get(long id) {
        return entityManager.find(Staff.class, id);
    }

    public List<Staff> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Staff e");
        return query.getResultList();
    }

    public void save(Staff staff) {
        entityManager.persist(staff);
    }

    public void update(Staff staff) {
        entityManager.merge(staff);
    }

    public void delete(Staff staff) {
        entityManager.remove(staff);
    }

    private void executeInsideTransaction(Consumer<EntityManager> staff) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            staff.accept(entityManager);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Staff> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Staff model where model." + propertyName + "= :propertyValue";            
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


    @SuppressWarnings("unchecked")
    public List<Staff> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Staff model";
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
