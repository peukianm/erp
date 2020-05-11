/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Attendance;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Staff;
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

    public List<Scheduletaskdetail> getAllScheduletaskdetail() {
        Query query = entityManager.createQuery("SELECT e FROM Schesuletaskdetail e");
        return query.getResultList();
    }
    
     public List<Staff> getAllStaff(boolean onlyActive) {
        String sql = "SELECT e FROM Staff e "
        + (onlyActive ? " where e.active = 1 " : " ");
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }
    

    @SuppressWarnings("unchecked")
    public Companytask findCtask(Company company, Scheduletask task) {

        try {
            final String queryString = "select model from Companytask model where "
                    + " model.active = 1 and "
                    + " model.company = :company and "
                    + " model.scheduletask = :task ";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("company", company);
            query.setParameter("task", task);
            return (Companytask) query.getResultList().get(0);
        } catch (RuntimeException re) {
            logger.error("Error on getting findCtask entity", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public Staff findStaffFromLoggerCode(String loggerCode) {

        try {
            final String queryString = "select model from Staff model where "
                    + " model.loggercode = '" + loggerCode +"'";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList().size() == 0 ? null : (Staff) query.getResultList().get(0);
        } catch (RuntimeException re) {
            logger.error("Error on getting Staff from Code ", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Attendance> findAttendance(Staff staff, String previousDate, String currentDate) {

        try {
            final String queryString = "select model from Attendance model where "
                    + " model.entrance BETWEEN '" + previousDate + " 00:00:00' AND '" + currentDate + " 23:59:59' and "
                    //+ " model.ended = 0 and "
                    + " model.staff = :staff "
                    + " order by model.entrance   ";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("staff", staff);            
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Staff from Code ", re);
            throw re;
        }
    }

    public void saveAttendance(Attendance attendance) {
        entityManager.persist(attendance);
    }

    public void updateAttendance(Attendance attendance) {
        entityManager.merge(attendance);
    }

    public void updateCtask(Companytask companyTask) {
        entityManager.merge(companyTask);
    }

    public void saveTaskDetails(Scheduletaskdetail scheduletaskdetail) {
        entityManager.persist(scheduletaskdetail);
    }

    public void updateTaskDetails(Scheduletaskdetail scheduletaskdetail) {
        entityManager.merge(scheduletaskdetail);
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
     * @param rowStartIdxAndCount Optional int varargs . rowStartIdxAndCount [0]
     * specifies the the row index in the query result -set to begin collecting
     * the results.rowStartIdxAndCount [1] specifies the the maximum count of
     * results to return.
     * @
     * return List<Auditing> all Auditing entities
     *
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
}
