/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Attendance;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Department;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
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
public class StaffDAO {

    private static final Logger logger = LogManager.getLogger(StaffDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;

    public Staff getStatff(long id) {
        return entityManager.find(Staff.class, id);
    }

    public List<Staff> getAllStaff(boolean onlyActive) {
        String sql = "SELECT e FROM Staff e "
                + (onlyActive ? " where e.active = 1 " : " ");
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }
    
     public List<Department> getSectorDepartments(Company company, Sector sector) {
         System.out.println(sector);
         String sql = "SELECT model.department FROM Sectordepartment model where "
                + " model.company = :company "
                + " and model.active = 1 "
                +(sector != null ? " and model.sector = :sector " : " ");
         
        
        Query query = entityManager.createQuery(sql);
        query.setParameter("company", company);
        if (sector != null) 
                query.setParameter("sector", sector); 
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public Attendance getDayAttendance(Staff staff, boolean onlyEnded) {
        try {
            final String queryString = "select model from Attendance model where "
                    + " model.entrance BETWEEN '"+LocalDate.now()+" 00:00:00' AND '"+LocalDate.now()+" 23:59:59'  "
                    + (onlyEnded ? " and model.ended = 1 " : " ")
                    + " and model.staff = :staff "
                    + " order by model.id DESC  ";
            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.retrieveMode", "BYPASS");
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("staff", staff);
            List<Attendance> ats = query.getResultList();
            if (ats.isEmpty())
                return null;
            else {
                return ats.get(0);
            }            
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getDayAttendance ", re);
            throw re;
        }
    }
    

    @SuppressWarnings("unchecked")
    public List<Attendance> StaffApperence(Company company, Timestamp startDate, Timestamp endDate, Staff staff, Department department) {
        try {
            final String queryString = "select model from Attendance model where "
                    + " model.company = :company  "
                    + (startDate != null ? " and model.entrance = :entrance  " : " ")
                    + (endDate != null ? " and model.exit = :exit  " : " ")
                    + (staff != null ? " and model.staff = :staff  " : " ")
                    + (department != null ? " and model.department = :department  " : " ")
                    + " order by model.staff.surname ";

            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("company", company);

            if (startDate != null) {
                query.setParameter("entrance", startDate);
            }
            if (endDate != null) {
                query.setParameter("exit", endDate);
            }
            if (staff != null) {
                query.setParameter("staff", staff);
            }
            if (department != null) {
                query.setParameter("department", department);
            }

            List<Attendance> attendances = query.getResultList();
            if (attendances.size() == 0) {
                return null;
            }

            attendances.forEach(a -> entityManager.refresh(a));

            return attendances;
        } catch (RuntimeException re) {
            logger.error("Error on getting findCtask entity", re);
            throw re;
        }
    }

}
