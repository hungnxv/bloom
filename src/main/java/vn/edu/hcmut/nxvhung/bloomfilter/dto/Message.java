package vn.edu.hcmut.nxvhung.bloomfilter.dto;

import java.io.Serializable;
import java.util.Map;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1232L;

  private static final String TO_STRING_TEMPLATE = "Message{companyName= %s, timestamp = %s, Vector clock [%s]}";
  private Integer timestamp;

  private String companyName;

  private Filterable blacklist;

  private Map<String, Integer> timestampVector;

  public Message() {

  }

  public Message(Integer timestamp, Filterable blacklist) {
    this.timestamp = timestamp;
    this.blacklist = blacklist;
  }

  public Message(Integer timestamp, Filterable blacklist, Map<String, Integer> timestampVector) {
    this.timestamp = timestamp;
    this.blacklist = blacklist;
    this.timestampVector = timestampVector;
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
    return String.format(TO_STRING_TEMPLATE, companyName, timestamp, timestampVector) ;
  }


}
