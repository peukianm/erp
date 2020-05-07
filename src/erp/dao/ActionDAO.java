package erp.dao;

import erp.entities.Action;
import java.util.List;
import java.util.function.Consumer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * A data access object (DAO) providing persistence and search support for Action entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually added to each of these methods for data to be persisted to the JPA datastore.
 *
 * @see erp.entities.Action
 * @author peukianm
 */
@Stateless
public class ActionDAO {

    private static final Logger logger = LogManager.getLogger(ActionDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;
    
    public Action get(long id) {
        return entityManager.find(Action.class, id);
    }

    public List<Action> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Action e");
        return query.getResultList();
    }

    public void save(Action auditing) {
        entityManager.persist(auditing);
    }

    public void update(Action action) {
        entityManager.merge(action);
    }

    public void delete(Action action) {
        entityManager.remove(action);
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
    public List<Action> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Action model where model." + propertyName + "= :propertyValue";            
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
    public List<Action> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Action model";
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