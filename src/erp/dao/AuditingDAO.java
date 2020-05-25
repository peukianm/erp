/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Action;
import erp.entities.Auditing;
import erp.entities.Company;
import erp.entities.Usr;
import java.io.Serializable;
import java.sql.Timestamp;
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
public class AuditingDAO implements Serializable {

    private static final Logger logger = LogManager.getLogger(AuditingDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;

    public Auditing get(long id) {
        try {
            return entityManager.find(Auditing.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Audit entity", re);
            throw re;
        }
    }

    public List<Auditing> getAll() {
        try {
            Query query = entityManager.createQuery("SELECT e FROM Auditing e");
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all audits entity", re);
            throw re;
        }
    }

    public void save(Auditing auditing) {
        try {
            entityManager.persist(auditing);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving Audit entity", re);
            throw re;
        }
        //executeInsideTransaction(entityManager -> entityManager.persist(auditing));
    }

    public void update(Auditing auditing, String[] params) {
        try {
            entityManager.merge(auditing);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on update Audit entity", re);
            throw re;
        }
        //executeInsideTransaction(entityManager -> entityManager.merge(auditing));
    }

    public void update(Auditing auditing) {
        try {
            entityManager.merge(auditing);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on update Audit entity", re);
            throw re;
        }
        //executeInsideTransaction(entityManager -> entityManager.merge(auditing));
    }

    public void delete(Auditing auditing) {
        try {
            entityManager.remove(auditing);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on deleting audit entity", re);
            throw re;
        }
        //executeInsideTransaction(entityManager -> entityManager.remove(auditing));
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
    public List<Auditing> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Auditing model where model." + propertyName + "= :propertyValue";
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
     * Find all Auditing entities .
     *
     * @param rowStartIdxAndCount Optional int varargs . rowStartIdxAndCount [0]
     * specifies the the row index in the query result -set to begin collecting
     * the results.rowStartIdxAndCount [1] specifies the the maximum count of
     * results to return.
     * @
     * return List<Auditing> all Auditing entities
     *
     */
    @SuppressWarnings("unchecked")
    public List<Auditing> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from Auditing model";
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
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    public List<Auditing> searchAudit(Usr user, Action action, Timestamp from, Timestamp to,
            Company company) {
        try {

            String queryString = "Select a from Auditing a"
                    + " where a.auditingid IS NOT NULL "
                    + (company != null ? " and a.company=:hospital " : " ")
                    + (user != null ? " and a.users=:user " : " ")
                    + (action != null ? " and a.action=:action " : " ")
                    + (from != null ? " and a.actiondate>=:from" : " ")
                    + (to != null ? " and a.actiondate<=:to" : " ")
                    + " order by a.actiondate DESC";

            Query query = entityManager.createQuery(queryString);
            if (company != null) {
                query.setParameter("company", company);
            }
            if (user != null) {
                query.setParameter("user", user);
            }
            if (action != null) {
                query.setParameter("action", action);
            }
            if (from != null) {
                query.setParameter("from", from);
            }
            if (to != null) {
                query.setParameter("to", to);
            }

            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on searching audit ", re);
            throw re;
        }
    }
}
