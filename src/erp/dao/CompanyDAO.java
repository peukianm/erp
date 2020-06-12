package erp.dao;

import erp.entities.Company;
import erp.entities.Department;
import erp.entities.Emprank;
import erp.entities.Sector;
import erp.entities.Workshift;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A data access object (DAO) providing persistence and search support for
 * Company entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 *
 * @see erp.entities.Company
 * @author peukianm
 */
@Stateless
public class CompanyDAO {

    private static final Logger logger = LogManager.getLogger(CompanyDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;

    public Company getCompany(long id) {
        try {
            return entityManager.find(Company.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Company entity", re);
            throw re;
        }
    }

    public Department getDepartment(long id) {
        try {
            return entityManager.find(Department.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Department entity", re);
            throw re;
        }
    }

    public Sector getSector(long id) {
        try {
            return entityManager.find(Sector.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Sector entity", re);
            throw re;
        }
    }

    public List<Company> getAllStaff(boolean onlyActive) {
        try {
            String sql = "SELECT e FROM Company e "
                    + (onlyActive ? " where e.active = 1 " : " ");
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all staff entity", re);
            throw re;
        }
    }

    public List<Department> getAllDepartment(boolean onlyActive) {
        try {
            String sql = "SELECT e FROM Department e "
                    + (onlyActive ? " where e.active = 1 " : " ")
                    + " order by e.name ";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all departments", re);
            throw re;
        }
    }

    public List<Workshift> getAllWorkShifts(boolean onlyActive) {
        try {
            String sql = "SELECT e FROM Workshift e "
                    + (onlyActive ? " where e.active = 1 " : " ")
                    + " order by e.name ";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all work shifts", re);
            throw re;
        }
    }

    public List<Emprank> getAllEmpRanks() {
        try {
            String sql = "SELECT e FROM Emprank e "
                    + " order by e.name ";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all employee ranks", re);
            throw re;
        }
    }

    public List<Company> getAllCompanies(boolean onlyActive) {
        try {
            String sql = "SELECT e FROM Company e "
                    + (onlyActive ? " where e.active = 1 " : " ")
                    + " order by e.name ";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all companies", re);
            throw re;
        }

    }

    public List<Sector> getAllSector() {
        try {
            String sql = "SELECT e FROM Sector e "
                    + " order by e.name ";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all sectors", re);
            throw re;
        }

    }

    /**
     * Perform an initial save of a previously unsaved Company entity. All
     * subsequent persist actions of this entity should use the #update()
     * method. This operation must be performed within the a database
     * transaction context for the entity's data to be permanently saved to the
     * persistence store, i.e., database. This method uses the null null null
     * null null     {@link javax.persistence.EntityManager#persist(Object)
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
            entityManager.persist(entity);

        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving company", re);
            throw re;
        }
    }

    /**
     * Delete a persistent Company entity. This operation must be performed
     * within the a database transaction context for the entity's data to be
     * permanently deleted from the persistence store, i.e., database. This
     * method uses the {@link javax.persistence.EntityManager#remove(Object)
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
            entity = entityManager.getReference(Company.class, entity.getCompanyid());
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on deleting company", re);
            throw re;
        }
    }

    /**
     * Persist a previously saved Company entity and return it or a copy of it
     * to the sender. A copy of the Company entity parameter is returned when
     * the JPA persistence mechanism has not previously been tracking the
     * updated entity. This operation must be performed within the a database
     * transaction context for the entity's data to be permanently saved to the
     * persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
     * operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = CompanyDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Company entity to update
     * @return Company the persisted Company entity instance, may not be the
     * same
     * @throws RuntimeException if the operation fails
     */
    public Company update(Company entity) {
        try {
            Company result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating company", re);
            throw re;
        }
    }

    public Company findById(long id) {
        try {
            Company instance = entityManager.find(Company.class, id);
            return instance;
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on finding company", re);
            throw re;
        }
    }

    /**
     * Find all Company entities with a specific property value.
     *
     * @param propertyName the name of the Company property to query
     * @param value the property value to match
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0]
     * specifies the the row index in the query result-set to begin collecting
     * the results. rowStartIdxAndCount[1] specifies the the maximum number of
     * results to return.
     * @return List<Company> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Company> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Company model where model." + propertyName + "= :propertyValue";
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
            re.printStackTrace();
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    /**
     * Find all Company entities.
     *
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0]
     * specifies the the row index in the query result-set to begin collecting
     * the results. rowStartIdxAndCount[1] specifies the the maximum count of
     * results to return.
     * @return List<Company> all Company entities
     */
    @SuppressWarnings("unchecked")
    public List<Company> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Company model";
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
            re.printStackTrace();
            logger.error("Error on finding all entities", re);
            throw re;
        }
    }

}
