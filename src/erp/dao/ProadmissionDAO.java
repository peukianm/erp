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
import erp.entities.Patient;
import erp.entities.Proadmission;
import erp.entities.Scheduletask;
import erp.entities.Scheduletaskdetail;
import erp.entities.Sector;
import erp.entities.Speciality;
import erp.entities.Staff;
import erp.entities.Studytype;
import erp.entities.Workshift;
import erp.util.FormatUtils;
import erp.util.SystemParameters;
import java.sql.Timestamp;
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
public class ProadmissionDAO {

    private static final Logger logger = LogManager.getLogger(ProadmissionDAO.class);

    @PersistenceContext(unitName = "erp")
    private EntityManager entityManager;

    public void save(Proadmission proadmission) {
        try {
            entityManager.persist(proadmission);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on saving proadmission entity", re);
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

    public void update(Proadmission proadmission) {
        try {
            entityManager.merge(proadmission);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating proadmission", re);
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

    public void delete(Proadmission proadmission) {
        try {
            entityManager.remove(proadmission);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on deleting proadmission entity", re);
            throw re;
        }
    }

    public Proadmission getProadmission(long id) {
        try {
            return entityManager.find(Proadmission.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting proadmission entity", re);
            throw re;
        }
    }

    public Patient getPatient(long id) {
        try {
            return entityManager.find(Patient.class, id);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Patient entity", re);
            throw re;
        }
    }

    public void updateProadmission(Proadmission proadmission) {
        try {
            entityManager.merge(proadmission);
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on updating proadmission", re);
            throw re;
        }
    }

    public List<Proadmission> getAllProadmissions(boolean onlyActive) {
        try {
            String sql = "SELECT e FROM Proadmission e "
                    + (onlyActive ? " where e.active = 1 " : " ");
            Query query = entityManager.createQuery(sql);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting All Proadmission entity", re);
            throw re;
        }
    }

    public List<Department> getProadmissions(Department department, Timestamp fromAdmission, Timestamp toAdmission, Patient patient,
            Timestamp fromRelease, Timestamp toRelease, Integer release, Integer processed, boolean active) {
        try {
            String sql = "SELECT model.proadmission FROM Proadmission model where "
                    + (active ? " model.active = 1 " : " model.active = 0 ")
                    + (processed != null ? " and model.processed = " + processed.intValue() : " ")
                    + (release != null ? " and model.released = " + release.intValue() : " ")
                    + (fromAdmission != null ? " and model.admissiondate>=:fromAdmission" : " ")
                    + (toAdmission != null ? " and model.admissiondate<=:toAdmission" : " ")
                    + (fromRelease != null ? " and model.releasedate>=:fromRelease" : " ")
                    + (toRelease != null ? " and model.releasedate<=:toRelease" : " ")
                    + (patient != null ? " and model.patient = :patient " : " ")
                    + (department != null ? " and model.department= :department" : " ")
                    + " order by model.admissiondate DESC, model.patient.surname DESC";

            Query query = entityManager.createQuery(sql);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            if (fromAdmission != null) {
                query.setParameter("fromAdmission", fromAdmission);
            }
            if (toAdmission != null) {
                query.setParameter("toAdmission", toAdmission);
            }
            if (fromRelease != null) {
                query.setParameter("fromRelease", fromRelease);
            }
            if (toRelease != null) {
                query.setParameter("toRelease", toRelease);
            }
            if (patient != null) {
                query.setParameter("patient", patient);
            }
            if (department != null) {
                query.setParameter("department", department);
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting Proadmissions entity", re);
            throw re;
        }
    }

    public List<Patient> fetchPatientAutoCompleteSurname(String surname, boolean active) {
        try {
            surname = surname.trim();
            String queryString = "Select patient from Patient patient  "
                    + " where (LOWER(patient.surname) like '" + ((String) surname).toLowerCase() + "%'"
                    + " OR UPPER(patient.surname)  like '" + ((String) surname).toUpperCase() + "%') "
                    + (active ? " and patient.active =1 " : " ")
                    + " order by patient.surname";

            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setMaxResults(40);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    public List<Patient> fetchPatientAutoCompleteAmka(String amka, boolean active) {
        try {
            amka = amka.trim();
            String queryString = "Select patient from Patient patient  "
                    + " where (LOWER(patient.amka) like '" + ((String) amka).toLowerCase() + "%'"
                    + " OR UPPER(patient.amka)  like '" + ((String) amka).toUpperCase() + "%') "
                    + (active ? " and patient.active =1 " : " ")
                    + " order by patient.surname ";

            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setMaxResults(40);
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on finding entity", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Staff> getPatients(boolean active, String surname, String name, String amka, String cteamID) {
        try {
            final String queryString = "select model from Patient model where "
                    + (active ? " model.active = 1  " : "  model.active = 0  ")
                    + (surname != null ? " and  (LOWER(patient.surname) like '" + ((String) surname).toLowerCase() + "%'"
                            + " OR UPPER(patient.surname)  like '" + ((String) surname).toUpperCase() + "%') " : " ")
                    + (name != null ? " and (LOWER(patient.name) like '" + ((String) name).toLowerCase() + "%'"
                            + " OR UPPER(patient.name)  like '" + ((String) name).toUpperCase() + "%') " : " ")
                    + (amka != null ? " and patient.amka = :amka" : " ")
                    + (cteamID != null ? " and patient.cteamid = :cteamID " : " ")
                    + " order by model.surname ";

            Query query = entityManager.createQuery(queryString);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            if (amka != null) {
                query.setParameter("amka", amka);
            }
            if (cteamID != null) {
                query.setParameter("cteamID", cteamID);
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            re.printStackTrace();
            logger.error("Error on getting patients ", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Proadmission> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {

        try {
            final String queryString = "select model from Proadmission model where model." + propertyName + "= :propertyValue";
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
