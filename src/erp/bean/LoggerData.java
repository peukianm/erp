/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;

import java.util.Date;

/**
 *
 * @author peukianm
 */
public class LoggerData implements Comparable<LoggerData> {

  private Date dateTime;
  private String code;
  private String status;
  

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date datetime) {
    this.dateTime = datetime;
  }

  @Override
  public int compareTo(LoggerData o) {
    return getDateTime().compareTo(o.getDateTime());
  }
}