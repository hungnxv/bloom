package vn.edu.hcmut.nxvhung.bloomfilter.dto;

import java.io.Serializable;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1232L;
  private Integer localTimestamp;

  private Filterable blacklist;

  public Message() {

  }

  public Message(Integer localTimestamp, Filterable blacklist) {
    this.localTimestamp = localTimestamp;
    this.blacklist = blacklist;
  }

  public Integer getLocalTimestamp() {
    return localTimestamp;
  }

  public void setLocalTimestamp(Integer localTimestamp) {
    this.localTimestamp = localTimestamp;
  }

  public Filterable getBlacklist() {
    return blacklist;
  }

  public void setBlacklist(Filterable blacklist) {
    this.blacklist = blacklist;
  }
}
