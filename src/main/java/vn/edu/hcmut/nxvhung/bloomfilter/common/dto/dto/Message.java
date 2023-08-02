package vn.edu.hcmut.nxvhung.bloomfilter.common.dto.dto;

import java.io.Serializable;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class Message implements Serializable {

  private static final long serialVersionUID = -3287941672064071940L;

  private Integer logicalTimestamp;
  private Filterable blacklist;

  public Message(Integer logicalTimestamp, Filterable blacklist) {
    this.logicalTimestamp = logicalTimestamp;
    this.blacklist = blacklist;
  }

  public Integer getLogicalTimestamp() {
    return logicalTimestamp;
  }

  public void setLogicalTimestamp(Integer logicalTimestamp) {
    this.logicalTimestamp = logicalTimestamp;
  }

  public Filterable getBlacklist() {
    return blacklist;
  }

  public void setBlacklist(Filterable blacklist) {
    this.blacklist = blacklist;
  }
}
