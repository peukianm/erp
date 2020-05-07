package erp.dao;

import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Company;
import erp.entities.Usr;
import erp.util.EJBUtil;
import erp.util.PersistenceHelper;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A data access object (DAO) providing persistence and search support for Auditing entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 *
 * @see erp.entities.Auditing
 * @author MyEclipse Persistence Tools
 */
public class AuditingDAO_OLD {
    // property constants

    public static final String COMMENTS = "comments";
    private static final Logger logger = LogManager.getLogger(AuditingDAO.class);

    private EntityManager getEntityManager() {
        PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();
        return persistenceHelper.getEntityManager();
    }

    /**
     * Perform an initial save of a previously unsaved Auditing entity. All subsequent persist actions of this entity should use the #update() method. This
     * operation must be performed within the a database transaction context for the entity's data to be permanently saved to the persistence store, i.e.,
     * database. This method uses the      {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * AuditingDAO.save(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Auditing entity to persist
     * @throws RuntimeException when the operation fails
     */
    public void save(Auditing entity) {
        try {
            getEntityManager().persist(entity);
        } catch (RuntimeException re) {
            logger.error("Error on saving entity", re);
            throw re;
        }
    }

    /**
     * Delete a persistent Auditing entity. This operation must be performed within the a database transaction context for the entity's data to be permanently
     * deleted from the persistence store, i.e., database. This method uses the {@link javax.persistence.EntityManager#remove(Object)
     * EntityManager#delete} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * AuditingDAO.delete(entity);
     * EntityManagerHelper.commit();
     * entity = null;
     * </pre>
     *
     * @param entity Auditing entity to delete
     * @throws RuntimeException when the operation fails
     */
    public void delete(Auditing entity) {
        try {
            entity = getEntityManager().getReference(Auditing.class, entity.getAuditingid());
            getEntityManager().remove(entity);
        } catch (RuntimeException re) {
            logger.error("Error on deleting entity", re);
            throw re;
        }
    }

    /**
     * Persist a previously saved Auditing entity and return it or a copy of it to the sender. A copy of the Auditing entity parameter is returned when the JPA
     * persistence mechanism has not previously been tracking the updated entity. This operation must be performed within the a database transaction context for
     * the entity's data to be permanently saved to the persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = AuditingDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Auditing entity to update
     * @return Auditing the persisted Auditing entity instance, may not be the same
     * @throws RuntimeException if the operation fails
     */
    public Auditing update(Auditing entity) {
        try {
            Auditing result = getEntityManager().merge(entity);
            return result;
        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    public Auditing findById(BigDecimal id) {
        try {
            Auditing instance = getEntityManager().find(Auditing.class, id);
            return instance;
        } catch (RuntimeException re) {
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    /**
     * Find all Auditing entities with a specific property value.
     *
     * @param propertyName the name of the Auditing property to query
     * @param value the property value to match
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum number of results to return.
     * @return List<Auditing> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Auditing> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Auditing model where model." + propertyName + "= :propertyValue";
            Query query = getEntityManager().createQuery(queryString);
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
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    public List<Auditing> findByComments(Object comments, int... rowStartIdxAndCount) {
        return findByProperty(COMMENTS, comments, rowStartIdxAndCount);
    }

    /**
     * Find all Auditing entities.
     *
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum count of results to return.
     * @return List<Auditing> all Auditing entities
     */
    @SuppressWarnings("unchecked")
    public List<Auditing> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Auditing model";
            Query query = getEntityManager().createQuery(queryString);
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
    
    
    public List<Auditing> searchAudit(Usr user, Action action, Timestamp from, Timestamp to,
			 Company company){
		try {
			
			String queryString = "Select a from Auditing a"
					+ " where a.auditingid IS NOT NULL "
					+ (company != null ? " and a.company=:hospital " : " ")
					+ (user != null ? " and a.users=:user " : " ")
					+ (action != null ? " and a.action=:action " : " ")
                                        + (from != null ? " and a.actiondate>=:from" : " ")
					+ (to != null ? " and a.actiondate<=:to" : " ")					
					+ " order by a.actiondate DESC";

			

			Query query = getEntityManager().createQuery(queryString);
			if (company != null)
				query.setParameter("company", company);
			if (user != null)
				query.setParameter("user", user);
			if (action != null)
				query.setParameter("action", action);                        
			if (from != null)
				query.setParameter("from", from);
			if (to != null)
				query.setParameter("to", to);

			return query.getResultList();
		} catch (RuntimeException re) {			
			throw re;
		}
	}
    
    
    
}