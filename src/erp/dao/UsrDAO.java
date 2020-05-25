/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Action;
import erp.entities.Company;
import erp.entities.Role;
import erp.entities.Usr;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Stateless
public class UsrDAO implements Serializable {

    private static final Logger logger = LogManager.getLogger(UsrDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;

    public Usr get(long id) {
        try {
            return entityManager.find(Usr.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting User entity", re);
            throw re;
        }
    }

    public List<Usr> getAll() {
        try {
            Query query = entityManager.createQuery("SELECT e FROM Usr e");
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all users ", re);
            throw re;
        }
    }

    public List<Role> getAllRoles() {
        try {
            Query query = entityManager.createQuery("SELECT e FROM Role e");
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all roles ", re);
            throw re;
        }
    }

    public List<Action> getAllActions() {
        try {
            Query query = entityManager.createQuery("SELECT e FROM Action e");
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting all actions ", re);
            throw re;
        }
    }

    public Action getAction(long id) {
        try {
            return entityManager.find(Action.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Action ", re);
            throw re;
        }
    }

    public void save(Usr user) {
        try {
            entityManager.persist(user);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving user entity", re);
            throw re;
        }

    }

    public void update(Usr user) {
        try {
            entityManager.merge(user);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating user", re);
            throw re;
        }
    }

    public void delete(Usr user) {
        try {
            entityManager.remove(user);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on deleting User entity", re);
            throw re;
        }
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
    public List<Usr> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Usr model where model." + propertyName + "= :propertyValue";
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

    public Usr findUser(String username, String password) {
        try {
            String sql = "Select u from Usr u where "
                    + " u.username like '" + username + "'  "
                    + " AND u.password like '" + password + "' ";

            Query query = entityManager.createQuery(sql);
            return (Usr) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException nre) {
            return null;
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on finding user entity", re);
            throw re;

        }
    }

    public List<Usr> fetchUserAutoCompleteUsername(String name) {
        try {
            name = name.trim();

            String queryString = "Select user from Usr  user  "
                    + " where (LOWER(user.username) like '" + ((String) name).toLowerCase() + "%'"
                    + " OR UPPER(user.username)  like '" + ((String) name).toUpperCase() + "%') "
                    + " order by user.username";

            Query query = entityManager.createQuery(queryString);
            query.setMaxResults(20);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    public List<Usr> searchUser(Role role, Company company, String username, String surname) {
        try {

            String queryString = "Select u from Usr u "
                    + (role != null ? " JOIN u.userrole userrole " : " ")
                    + " where u.userid IS NOT NULL "
                    + (company != null ? " and u.company=:company " : " ")
                    + (role != null ? " and userrole.role=:role " : " ")
                    + (username != null ? " and (LOWER(u.username) like '" + ((String) username).toLowerCase() + "%'"
                            + " OR UPPER(u.username)  like '" + ((String) username).toUpperCase() + "%') " : " ")
                    + (surname != null ? " and (LOWER(u.surname) like '" + ((String) surname).toLowerCase() + "%'"
                            + " OR UPPER(u.surname)  like '" + ((String) surname).toUpperCase() + "%') " : " ")
                    + " order by u.username DESC";

            Query query = entityManager.createQuery(queryString);
            if (company != null) {
                query.setParameter("company", company);
            }
            if (role != null) {
                query.setParameter("role", role);
            }

            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on searching entity", re);
            throw re;
        }
    }

}
