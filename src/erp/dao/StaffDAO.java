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
import erp.entities.Sector;
import erp.entities.Staff;
import erp.util.FormatUtils;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

    @Inject
    SchedulerDAO schedulerDAO;

    public Staff getStatff(long id) {
        try {
            return entityManager.find(Staff.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Staff entity", re);
            throw re;
        }
    }

    public List<Staff> getAllStaff(boolean onlyActive) {
        try {
            String sql = "SELECT e FROM Staff e "
                    + (onlyActive ? " where e.active = 1 " : " ");
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting All Staff entity", re);
            throw re;
        }
    }

    public List<Department> getSectorDepartments(Company company, Sector sector) {
        try {
            String sql = "SELECT model.department FROM Sectordepartment model where "
                    + " model.company = :company "
                    + " and model.active = 1 "
                    + (sector != null ? " and model.sector = :sector " : " ");

            Query query = entityManager.createQuery(sql);
            query.setParameter("company", company);
            if (sector != null) {
                query.setParameter("sector", sector);
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Sector Departments entity", re);
            throw re;
        }
    }

    public String getTaskLastExecutionTime(Company company, Long taskID) {
        try {
            Scheduletask task = (Scheduletask) entityManager.find(Scheduletask.class, taskID);
            Companytask cTask = schedulerDAO.findCtask(company, task);
            return FormatUtils.formatTimeStamp(cTask.getLastexecutiontime(), FormatUtils.FULLDATEPATTERN);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Last Execution Time entity", re);
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
            query.setHint("javax.persistence.retrieveMode", "BYPASS");
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
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
    public List<Attendance> staffApperence(Company company, String startDate, String endDate, Staff staff, Sector sector, Department department) {
        try {
            final String queryString = "select model from Attendance model where "
                    + " model.company = :company  "
                    + " and model.entrance BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'  "
                    + (staff != null ? " and model.staff = :staff  " : " ")
                    + (department != null ? " and model.department = :department  " : " ")
                    + (sector != null ? " and model.sector = :sector  " : " ")
                    + " order by model.staff.surname ";

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

    public List<Staff> fetchStaffAutoCompleteSurname(String surname, Sector sector, Department department) {
        try {
            surname = surname.trim();
            String queryString = "Select staff from Staff staff  "
                    + " where (LOWER(staff.surname) like '" + ((String) surname).toLowerCase() + "%'"
                    + " OR UPPER(staff.surname)  like '" + ((String) surname).toUpperCase() + "%') "
                    + (department != null ? " and staff.department = :department  " : " ")
                    + (sector != null ? " and staff.sector = :sector  " : " ")
                    + " order by staff.surname";

            Query query = entityManager.createQuery(queryString);
            if (department != null) {
                query.setParameter("department", department);
            }
            if (sector != null) {
                query.setParameter("sector", sector);
            }

            query.setMaxResults(20);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

}