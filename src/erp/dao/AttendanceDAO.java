/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Attendance;
import erp.entities.Company;
import erp.entities.Department;
import erp.entities.Sector;
import erp.entities.Staff;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author peukianm
 */
@Stateless
public class AttendanceDAO {
    private static final Logger logger = LogManager.getLogger(AttendanceDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;
    
     public Attendance getAttendance(long id) {
        try {
            return entityManager.find(Attendance.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Attendance entity", re);
            throw re;
        }
    }
     
         public List<Attendance> getAllAttendances() {
        try {
            String sql = "SELECT e FROM Staff e ";                   
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting All Attendances ", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
    public Attendance getDayAttendance(Staff staff, boolean onlyEnded) {
        try {
            final String queryString = "select model from Attendance model where "
                    + " model.entrance BETWEEN '" + LocalDate.now() + " 00:00:00' AND '" + LocalDate.now() + " 23:59:59'  "
                    + (onlyEnded ? " and model.ended = 1 " : " ")
                    + " and model.staff = :staff "
                    + " order by model.id DESC  ";
            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");           
            query.setParameter("staff", staff);
            List<Attendance> ats = query.getResultList();
            if (ats.isEmpty()) {
                return null;
            } else {
                return ats.get(0);
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on get Day Attendance ", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Attendance> staffApperence(Company company, String startDate, String endDate, Staff staff, Sector sector, Department department, boolean onlyEnded) {
        try {
            final String queryString = "select model from Attendance model where "
                    + " model.company = :company  "
                    + " and model.entrance BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'  "
                    + (staff != null ? " and model.staff = :staff  " : " ")
                    + (department != null ? " and model.department = :department  " : " ")
                    + (sector != null ? " and model.sector = :sector  " : " ")
                    + (onlyEnded ? " and model.ended = 1  " : " ")
                    + " order by model.staff.surname, model.entrance ";

            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("company", company);

            if (staff != null) {
                query.setParameter("staff", staff);
            }
            if (department != null) {
                query.setParameter("department", department);
            }
            if (sector != null) {
                query.setParameter("sector", sector);
            }

            List<Attendance> attendances = query.getResultList();

//            if (attendances.isEmpty()) 
//                return null;
//            attendances.forEach(a -> entityManager.refresh(a));
            return attendances;
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting staff apperance entity", re);
            throw re;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public List<Attendance> findOpenAttendance(Staff staff, Timestamp previousDate, Timestamp currentDate) {

        try {
            final String queryString = "select model from Attendance model where "
                    + " model.entrance BETWEEN :previous AND :current and "
                    + " model.ended = 0 and "
                    + " model.staff = :staff "
                    + " order by model.entrance DESC  ";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("previous", previousDate);
            query.setParameter("current", currentDate);
            query.setParameter("staff", staff);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Staff from Code ", re);
            throw re;
        }
    }

    public void saveAttendance(Attendance attendance) {
        try {
            entityManager.persist(attendance);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving entity", re);
            throw re;
        }
    }

    public void updateAttendance(Attendance attendance) {

        try {
            entityManager.merge(attendance);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating entity", re);
            throw re;
        }
    }
    
}
