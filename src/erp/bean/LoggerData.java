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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
  private String code;
  private String status;
  

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date datetime) {
    this.dateTime = datetime;
  }
  
  public LoggerData(String code, Date datetime, String status, Staff staff){
      this.code = code;
      this.status = status;
      this.dateTime = datetime;
      this.staff = staff;
  }

  @Override
  public int compareTo(LoggerData o) {
      int result = getCode().compareTo(o.getCode());
      if (result==0)
          result = getDateTime().compareTo(o.getDateTime());
      return result;
  }
}