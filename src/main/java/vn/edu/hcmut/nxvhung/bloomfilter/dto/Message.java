package vn.edu.hcmut.nxvhung.bloomfilter.dto;

import java.io.Serializable;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1232L;
  private Integer timestamp;

  private String companyName;

  private Filterable blacklist;

  public Message() {

  }

  public Message(Integer timestamp, Filterable blacklist) {
    this.timestamp = timestamp;
    this.blacklist = blacklist;
  }

  public Integer getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Integer timestamp) {
    this.timestamp = timestamp;
  }

  public Filterable getBlacklist() {
    return blacklist;
  }

  public void setBlacklist(Filterable blacklist) {
    this.blacklist = blacklist;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  @Override
  public String toString() {
    return "Message{" +
        "timestamp=" + timestamp +
        ", companyName='" + companyName ;
  }
}
