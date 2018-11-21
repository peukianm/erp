package erp.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import erp.entities.*;
import erp.util.*;
import java.sql.Timestamp;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * A data access object (DAO) providing persistence and search support for Users entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually added to each of these methods for data to be persisted to the JPA datastore.
 *
 * @see com.hosp.dao.Users
 * @author MyEclipse Persistence Tools
 */
public class UsersDAO {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    private static final Logger logger = LogManager.getLogger(UsersDAO.class);

    private EntityManager getEntityManager() {
        PersistenceHelper persistenceHelper = EJBUtil.lookupPersistenceHelperBean();
        return persistenceHelper.getEntityManager();
    }

    /**
     * Perform an initial save of a previously unsaved Users entity. All subsequent persist actions of this entity should use the #update() method. This
     * operation must be performed within the a database transaction context for the entity's data to be permanently saved to the persistence store, i.e.,
     * database. This method uses the null null null     {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * UsersDAO.save(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Users entity to persist
     * @throws RuntimeException when the operation fails
     */
    public void save(Users entity) {

        try {
            getEntityManager().persist(entity);

        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    /**
     * Delete a persistent Users entity. This operation must be performed within the a database transaction context for the entity's data to be permanently
     * deleted from the persistence store, i.e., database. This method uses the {@link javax.persistence.EntityManager#remove(Object)
     * EntityManager#delete} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * UsersDAO.delete(entity);
     * EntityManagerHelper.commit();
     * entity = null;
     * </pre>
     *
     * @param entity Users entity to delete
     * @throws RuntimeException when the operation fails
     */
    public void delete(Users entity) {

        try {
            entity = getEntityManager().getReference(Users.class, entity.getUserid());
            getEntityManager().remove(entity);

        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    /**
     * Persist a previously saved Users entity and return it or a copy of it to the sender. A copy of the Users entity parameter is returned when the JPA
     * persistence mechanism has not previously been tracking the updated entity. This operation must be performed within the a database transaction context for
     * the entity's data to be permanently saved to the persistence store, i.e., database. This method uses the
     * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge} operation.
     *
     * <pre>
     * EntityManagerHelper.beginTransaction();
     * entity = UsersDAO.update(entity);
     * EntityManagerHelper.commit();
     * </pre>
     *
     * @param entity Users entity to update
     * @return Users the persisted Users entity instance, may not be the same
     * @throws RuntimeException if the operation fails
     */
    public Users update(Users entity) {

        try {
            Users result = getEntityManager().merge(entity);

            return result;
        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    public Users findById(BigDecimal id) {

        try {
            Users instance = getEntityManager().find(Users.class, id);
            return instance;
        } catch (RuntimeException re) {
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    /**
     * Find all Users entities with a specific property value.
     *
     * @param propertyName the name of the Users property to query
     * @param value the property value to match
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum number of results to return.
     * @return List<Users> found by query
     */
    @SuppressWarnings("unchecked")
    public List<Users> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Users model where model." + propertyName + "= :propertyValue";
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
            logger.error("Error on updating entity", re);
            throw re;
        }
    }

    public List<Users> findByUsername(Object username, int... rowStartIdxAndCount) {
        return findByProperty(USERNAME, username, rowStartIdxAndCount);
    }

    public List<Users> findByPassword(Object password, int... rowStartIdxAndCount) {
        return findByProperty(PASSWORD, password, rowStartIdxAndCount);
    }

    public List<Users> findByDescription(Object description, int... rowStartIdxAndCount) {
        return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
    }

    public List<Users> findByName(Object name, int... rowStartIdxAndCount) {
        return findByProperty(NAME, name, rowStartIdxAndCount);
    }

    public List<Users> findBySurname(Object surname, int... rowStartIdxAndCount) {
        return findByProperty(SURNAME, surname, rowStartIdxAndCount);
    }

    /**
     * Find all Users entities.
     *
     * @param rowStartIdxAndCount Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the
     * results. rowStartIdxAndCount[1] specifies the the maximum count of results to return.
     * @return List<Users> all Users entities
     */
    @SuppressWarnings("unchecked")
    public List<Users> findAll(final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Users model";
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
            logger.error("Error on updating entity", re);


            throw re;
        }
    }

    public Users findUserExoterika(String username, String password) {
        try {
            //EntityManager em = EntityManagerHelper.getEntityManager();
//			String sql = "Select NEW com.hosp.bean.UserBean(u.username,u.password,u.department)" +
//			" from Users u where u.username like '" + username + "'  AND u.password like '"+password+"'";

            String sql = "Select u from Users u where "
                    + " u.username like '" + username + "'  "
                    + " AND u.password like '" + password + "' "
                    + " AND u.role.roleid IN (6,7,8)";

            Query query = getEntityManager().createQuery(sql);
            return (Users) query.getSingleResult();

        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        } catch (RuntimeException re) {
            throw re;

        }
    }

    public Users findUser(String username, String password) {
        try {
            //EntityManager em = EntityManagerHelper.getEntityManager();
//			String sql = "Select NEW com.hosp.bean.UserBean(u.username,u.password,u.department)" +
//			" from Users u where u.username like '" + username + "'  AND u.password like '"+password+"'";

            String sql = "Select u from Users u where "
                    + " u.username like '" + username + "'  "
                    + " AND u.password like '" + password + "' ";

            Query query = getEntityManager().createQuery(sql);
            return (Users) query.getSingleResult();

        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        } catch (RuntimeException re) {
            throw re;

        }
    }

    public List<Userroles> getUserRoles(Users user) {
        try {
            EntityManager em = getEntityManager();
            String queryString = "Select ur from Userroles ur"
                    + " where ur.users=:user "
                    + " order by ur.role.ordered ASC ";

            Query query = em.createQuery(queryString);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    private PersistenceHelper lookupPersistenceHelperBean() {
        try {
            Context c = new InitialContext();
            //return (PersistenceHelper) c.lookup("java:global/HERP/PersistenceHelper!com.hosp.util.PersistenceHelper");
            return (PersistenceHelper) c.lookup("java:module/PersistenceHelper");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
    public List<Users> fetchUserAutoCompleteUsername(String name) {
        try {
            name = name.trim();

            String queryString = "Select user from Users  user  "
                    + " where (LOWER(user.username) like '" + ((String) name).toLowerCase() + "%'"
                    + " OR UPPER(user.username)  like '" + ((String) name).toUpperCase() + "%') "                    
                    + " order by user.username";

            Query query = getEntityManager().createQuery(queryString);
            
            query.setMaxResults(20);
            return query.getResultList();
        } catch (RuntimeException re) {
            logger.error("Error on finding entity", re);
            throw re;
        }
    }
    
    public List<Users> searchUser(Role role, Company company, String username, String surname){
		try {
			
			String queryString = "Select u from Users u "
                                + (role != null ? " JOIN u.userroleses userrole " : " " )
                                + " where u.userid IS NOT NULL "
                                + (company != null ? " and u.company=:company " : " ")
                                + (role != null ? " and userrole.role=:role " : " ")
                                
                                + (username!=null ? " and (LOWER(u.username) like '" + ((String) username).toLowerCase() + "%'"
                                + " OR UPPER(u.username)  like '" + ((String) username).toUpperCase() + "%') "     : " ")
                                
                                + (surname!=null ? " and (LOWER(u.surname) like '" + ((String) surname).toLowerCase() + "%'"
                                + " OR UPPER(u.surname)  like '" + ((String) surname).toUpperCase() + "%') "     : " ")
                                
                                + " order by u.username DESC";

			

			Query query = getEntityManager().createQuery(queryString);
			if (company != null)
				query.setParameter("company", company);
			if (role != null)
				query.setParameter("role", role);
			
			return query.getResultList();
		} catch (RuntimeException re) {			
			throw re;
		}
	}
    
    
}