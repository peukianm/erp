package erp.dao;


import erp.entities.Company;

import erp.util.EJBUtil;
import erp.util.PersistenceHelper;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set; 
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;


/**
 * A data access object (DAO) providing persistence and search support for Company entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually added to each of these methods for data to be persisted to the JPA datastore.
 *
 * @see erp.entities.Company
 * @author MyEclipse Persistence Tools
 */
public class CompanyDAO {
    // property constants
 
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String EMAIL = "email";
    public static final String AFM = "afm";
    public static final String CONTACTPERSON = "contactperson";
    public static final String PHONE1 = "phone1";
    public static final String PHONE2 = "phone2";
    private static final Logger logger = LogManager.getLogger(CompanyDAO.class);

    private EntityManager getEntityManager() {
        PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();
        return persistenceHelper.getEntityManager();
    }

    /**
     * Perform an initial save of a previously unsaved Company entity. All subsequent persist actions of this entity should use the #update() method. This
     * operation must be performed within the a database transaction context for the entity's data to be permanently saved to the persistence store, i.e.,
     * database. This method uses the      {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * CompanyDAO.save(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Company entity to persist
     * @throws RuntimeException when the operation fails
     */
    public void save(Company entity) {
        try {
            getEntityManager().persist(entity);

        } catch (RuntimeException re) {
            logger.error("Error on saving entity", re);
            throw re;
        }
    }

    /**
     * Delete a persistent Company entity. This operation must be performed within the a database transaction context for the entity's data to be permanently
     * deleted from the persistence store, i.e., database. This method uses the {@link javax.persistence.EntityManager#remove(Object)
     * EntityManager#delete} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * CompanyDAO.delete(entity);
     * EntityManagerHelper.commit();
     * entity = null;
     * </pre>
     *
     * @param entity Company entity to delete
     * @throws RuntimeException when the operation fails
     */
    public void delete(Company entity) {
        try {
            entity = getEntityManager().getReference(Company.class, entity.getCompanyid());
            getEntityManager().remove(entity);
        } catch (RuntimeException re) {
            logger.error("Error on deleting entity", re);
            throw re;
        }
    }

    /**
     * Persist a previously saved Company entity and return it or a copy of it to the sender. A copy of the Company entity parameter is returned when the JPA
     * persistence mechanism has not previously been tracking the updated entity. This operation must be performed within the a database transaction context for
     * the entity's data to be permanently saved to the persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = CompanyDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Company entity to update
     * @return Company the persisted Company entity instance, may not be the same
     * @throws RuntimeException if the operation fails
     */
    public Company update(Company entity) {
        try {
            Company result = getEntityManager().merge(entity);
            return result;
        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    public Company findById(BigDecimal id) {
        try {
            Company instance = getEntityManager().find(Company.class, id);
            return instance;
        } catch (RuntimeException re) {
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    /**
     * Find all Company entities with a specific property value.
     *
     * @param propertyName the name of the Company property to query
     * @param value the property value to match
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum number of results to return.
     * @return List<Company> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Company> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Company model where model." + propertyName + "= :propertyValue";
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
            //return query.setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache).getResultList();
            return query.getResultList();
        } catch (RuntimeException re) {
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    public List<Company> findByName(Object name, int... rowStartIdxAndCount) {
        return findByProperty(NAME, name, rowStartIdxAndCount);
    }

    public List<Company> findByDescription(Object description, int... rowStartIdxAndCount) {
        return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
    }

    public List<Company> findByEmail(Object email, int... rowStartIdxAndCount) {
        return findByProperty(EMAIL, email, rowStartIdxAndCount);
    }

    public List<Company> findByAfm(Object afm, int... rowStartIdxAndCount) {
        return findByProperty(AFM, afm, rowStartIdxAndCount);
    }

    public List<Company> findByContactperson(Object contactperson, int... rowStartIdxAndCount) {
        return findByProperty(CONTACTPERSON, contactperson, rowStartIdxAndCount);
    }

    public List<Company> findByPhone1(Object phone1, int... rowStartIdxAndCount) {
        return findByProperty(PHONE1, phone1, rowStartIdxAndCount);
    }

    public List<Company> findByPhone2(Object phone2, int... rowStartIdxAndCount) {
        return findByProperty(PHONE2, phone2, rowStartIdxAndCount);
    }

    /**
     * Find all Company entities.
     *
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum count of results to return.
     * @return List<Company> all Company entities
     */
    @SuppressWarnings("unchecked")
    public List<Company> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Company model";
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
    
    
    @SuppressWarnings("unchecked")
    public List<Company> getAllCompanies(boolean showOnlyEnable) {
        try {
            Query query = getEntityManager().createQuery("Select c from Company c "                    
                     + (showOnlyEnable ? " where c.active= 1  " : " "));            
            
            List<Company> companies = query.getResultList();                        
            return companies;
        } catch (RuntimeException re) {
            throw re;
        }
    }   
    
}