/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import erp.entities.Staff;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author peukianm
 */
public class AttendanceBean implements Serializable {

    private String name;
    private LocalDate date;
    private LocalTime entrance;
    private LocalTime exit;
    private String duration;
    private Staff staff;
    private long secondsDuration;
    private long staffID;
    private long departmentID;
    private long sectorID;

    public long getSecondsDuration() {
        return secondsDuration;
    }

    public void setSecondsDuration(long secondsDuration) {
        this.secondsDuration = secondsDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEntrance() {
        return entrance;
    }

    public void setEntrance(LocalTime entrance) {
        this.entrance = entrance;
    }

    public LocalTime getExit() {
        return exit;
    }

    public void setExit(LocalTime exit) {
        this.exit = exit;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public long getStaffID() {
        return staffID;
    }

    public void setStaffID(long staffID) {
        this.staffID = staffID;
    }

    public long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    public long getSectorID() {
        return sectorID;
    }

    public void setSectorID(long sectorID) {
        this.sectorID = sectorID;
    }
    
    
    @Override
    public String toString() {
        return "AttendanceBeam{name=" + name + ", department=" + staff.getDepartment().getName() + "}";
    }

}
