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
import erp.entities.Staff;
import java.sql.Timestamp;
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
            
            if (startDate!=null)  query.setParameter("entrance", startDate);
            if (endDate!=null)  query.setParameter("exit", endDate);
            if (staff!=null)  query.setParameter("staff", staff);
            if (department!=null)  query.setParameter("department", department);
            
            List<Attendance> attendances = query.getResultList();
            if (attendances.size() == 0)
                return null;
            foreach (int i = 0; i < attendances.size; i++) {
                Object object = arr[i];
                
            }
            entityManager.refresh(ctask);
            return ctask;
        } catch (RuntimeException re) {
            logger.error("Error on getting findCtask entity", re);
            throw re;
        }
    }

}
