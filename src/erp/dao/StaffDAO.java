/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import erp.entities.Branch;
import erp.entities.Company;
import erp.entities.Companytask;
import erp.entities.Department;
import erp.entities.Emprank;
import erp.entities.Familystatus;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Sector;
import erp.entities.Speciality;
import erp.entities.Staff;
import erp.entities.Studytype;
import erp.entities.Workshift;
import erp.util.FormatUtils;
import erp.util.SystemParameters;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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

    public void save(Staff staff) {
        try {
            entityManager.persist(staff);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving staff entity", re);
            throw re;
        }

    }

    public void saveGeneric(Object entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving  entity", re);
            throw re;
        }
    }

    public void persistGeneric(Object entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving  entity", re);
            throw re;
        }
    }

    public void refreshGeneric(Object entity) {
        try {
            entityManager.refresh(entity);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on refreshing  entity", re);
            throw re;
        }
    }



    public void update(Staff staff) {
        try {
            entityManager.merge(staff);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating staff", re);
            throw re;
        }
    }

    public void updateGeneric(Object bean) {
        try {
            entityManager.merge(bean);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on generic updating ", re);
            throw re;
        }
    }
    
     public Object mergeGeneric(Object bean) {
        try {
            return entityManager.merge(bean);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on generic updating ", re);
            throw re;
        }
    }

    public void delete(Staff staff) {
        try {
            entityManager.remove(staff);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on deleting Staff entity", re);
            throw re;
        }
    }

    public void deleteSectordepartment(Object entity) {
        try {
            entityManager.remove(entityManager.merge(entity));
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on deleting entity", re);
            throw re;
        }
    }

    public Staff getStaff(long id) {
        try {
            return entityManager.find(Staff.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Staff entity", re);
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

    public Speciality getSpeciality(long id) {
        try {
            return entityManager.find(Speciality.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Speciality entity", re);
            throw re;
        }
    }

    public Familystatus getFamilyStatus(long id) {
        try {
            return entityManager.find(Familystatus.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Family Status entity", re);
            throw re;
        }
    }

    public Emprank getRank(long id) {
        try {
            return entityManager.find(Emprank.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Rank entity", re);
            throw re;
        }
    }

    public Branch getBranch(long id) {
        try {
            return entityManager.find(Branch.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Branch entity", re);
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

    public Studytype getStudytype(long id) {
        try {
            return entityManager.find(Studytype.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Studytype entity", re);
            throw re;
        }
    }

    public Company getCompany(long id) {
        try {
            return entityManager.find(Company.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Company entity", re);
            throw re;
        }
    }

    public Workshift getShift(long id) {
        try {
            return entityManager.find(Workshift.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Workshift entity", re);
            throw re;
        }
    }

    public void updateStaff(Staff staff) {
        try {
            entityManager.merge(staff);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating staff", re);
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
            System.out.println("GETTING ALL DEPARTMENTS ON SECTOR CHANGE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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

    @SuppressWarnings("unchecked")
    public Staff findStaffFromLoggerCode(String loggerCode) {

        try {
            final String queryString = "select model from Staff model where "
                    + " model.loggercode = '" + loggerCode + "'";
            Query query = entityManager.createQuery(queryString);
            return query.getResultList().size() == 0 ? null : (Staff) query.getResultList().get(0);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Staff from Code ", re);
            throw re;
        }
    }

    public String getTaskLastExecutionTime(Company company, Long taskID) {
        try {
            Scheduletask task = (Scheduletask) entityManager.find(Scheduletask.class, taskID);
            Companytask cTask = schedulerDAO.findCtask(company, task);

            final String queryString = "select model from Scheduletaskdetail model where "
                    + " model.companytask = :ctask  "
                    + " and model.taskstatus.statusid = " + SystemParameters.getInstance().getProperty("TASK_SUCCESS")
                    + " order by model.startexecutiontime DESC  ";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("ctask", cTask);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setMaxResults(1);

            List<Scheduletaskdetail> std = query.getResultList();
            if (std.isEmpty()) {
                return "N/A";
            } else {
                return FormatUtils.formatTimeStamp(std.get(0).getStartExecutiontime(), FormatUtils.FULLDATEPATTERN);
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Last Execution Time entity", re);
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

    @SuppressWarnings("unchecked")
    public List<Staff> getStaff(Company company, Sector sector, Department department, boolean active, String loggerCode) {
        try {
            final String queryString = "select model from Staff model where "
                    + " model.company = :company  "
                    + (department != null ? " and model.department = :department  " : " ")
                    + (sector != null ? " and model.sector = :sector  " : " ")
                    + (active ? " and model.active = 1  " : "  and model.active = 0  ")
                    + (loggerCode!=null && !(loggerCode.trim()).equals("") ? " and model.loggercode  like '" + loggerCode + "%' " : " ")
                    + " order by model.surname";

            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("company", company);

            if (department != null) {
                query.setParameter("department", department);
            }
            if (sector != null) {
                query.setParameter("sector", sector);
            }

            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting staff ", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Staff> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Staff model where model." + propertyName + "= :propertyValue";
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

}
