package erp.bean;

import erp.entities.Staff;
import java.util.Date;

/**
 * 
 * @author peukianm
 */
public class LoggerData implements Comparable<LoggerData> {

  private Date dateTime;
  private Staff staff;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }
    
  private double code;
  private double status;
  

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date datetime) {
    this.dateTime = datetime;
  }
  
  public LoggerData(double code, Date datetime, double status, Staff staff){
      this.code = code;
      this.status = status;
      this.dateTime = datetime;
      this.staff = staff;
  }

  @Override
  public int compareTo(LoggerData o) {
      int result = (int)(getCode() - o.getCode()) ;
      if (result==0)
          result = getDateTime().compareTo(o.getDateTime());
      return result;
  }
}