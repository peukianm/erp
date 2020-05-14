/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.entities.Attendance;
import erp.entities.Usr;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author peukianm
 */
@Named("dashboardView")
@ViewScoped
public class DashboardView implements Serializable {

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private StaffDAO staffDao

    private Attendance dayAttendance;

    public Attendance getDayAttendance() {
        Usr user = sessionBean.getUsers();
        
        return dayAttendance;
    }

    public void setDayAttendance(Attendance dayAttendance) {
        this.dayAttendance = dayAttendance;
    }

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void reset() {

    }

}
