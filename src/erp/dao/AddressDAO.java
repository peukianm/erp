package erp.dao;

import erp.dao.ActionscategoryDAO;
import erp.entities.Address;
import erp.util.EJBUtil;
import erp.util.PersistenceHelper;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A data access object (DAO) providing persistence and search support for Address entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually added to each of these methods for data to be persisted to the JPA datastore.
 *
 * @see erp.entities.Address
 * @author MyEclipse Persistence Tools
 */
public class AddressDAO {
    // property constants

    public static final String ADDRESS = "address";
    public static final String POSTALCODE = "postalcode";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    private static final Logger logger = LogManager.getLogger(AddressDAO.class);

    private EntityManager getEntityManager() {
        PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();
        return persistenceHelper.getEntityManager();
    }

    /**
     * Perform an initial save of a previously unsaved Address entity. All subsequent persist actions of this entity should use the #update() method. This
     * operation must be performed within the a database transaction context for the entity's data to be permanently saved to the persistence store, i.e.,
     * database. This method uses the      {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * AddressDAO.save(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Address entity to persist
     * @throws RuntimeException when the operation fails
     */
    public void save(Address entity) {
        try {
            getEntityManager().persist(entity);
        } catch (RuntimeException re) {
            logger.error("Error on saving entity", re);
            throw re;
        }
    }

    /**
     * Delete a persistent Address entity. This operation must be performed within the a database transaction context for the entity's data to be permanently
     * deleted from the persistence store, i.e., database. This method uses the {@link javax.persistence.EntityManager#remove(Object)
     * EntityManager#delete} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * AddressDAO.delete(entity);
     * EntityManagerHelper.commit();
     * entity = null;
     * </pre>
     *
     * @param entity Address entity to delete
     * @throws RuntimeException when the operation fails
     */
    public void delete(Address entity) {
        try {
            entity = getEntityManager().getReference(Address.class, entity.getAddressid());
            getEntityManager().remove(entity);
        } catch (RuntimeException re) {
            logger.error("Error on deleting entity", re);
            throw re;
        }
    }

    /**
     * Persist a previously saved Address entity and return it or a copy of it to the sender. A copy of the Address entity parameter is returned when the JPA
     * persistence mechanism has not previously been tracking the updated entity. This operation must be performed within the a database transaction context for
     * the entity's data to be permanently saved to the persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = AddressDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Address entity to update
     * @return Address the persisted Address entity instance, may not be the same
     * @throws RuntimeException if the operation fails
     */
    public Address update(Address entity) {
        try {
            Address result = getEntityManager().merge(entity);
            return result;
        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    public Address findById(long id) {
        try {
            Address instance = getEntityManager().find(Address.class, id);
            return instance;
        } catch (RuntimeException re) {
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    /**
     * Find all Address entities with a specific property value.
     *
     * @param propertyName the name of the Address property to query
     * @param value the property value to match
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum number of results to return.
     * @return List<Address> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Address> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Address model where model." + propertyName + "= :propertyValue";
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

    public List<Address> findByAddress(Object address, int... rowStartIdxAndCount) {
        return findByProperty(ADDRESS, address, rowStartIdxAndCount);
    }

    public List<Address> findByPostalcode(Object postalcode, int... rowStartIdxAndCount) {
        return findByProperty(POSTALCODE, postalcode, rowStartIdxAndCount);
    }

    public List<Address> findByCity(Object city, int... rowStartIdxAndCount) {
        return findByProperty(CITY, city, rowStartIdxAndCount);
    }

    public List<Address> findByCountry(Object country, int... rowStartIdxAndCount) {
        return findByProperty(COUNTRY, country, rowStartIdxAndCount);
    }

    /**
     * Find all Address entities.
     *
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum count of results to return.
     * @return List<Address> all Address entities
     */
    @SuppressWarnings("unchecked")
    public List<Address> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Address model";
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
}