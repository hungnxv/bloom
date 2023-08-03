package vn.edu.hcmut.nxvhung.bloomfilter.dto;

import java.io.Serializable;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1232L;
  private Integer localTimestamp;

  private Filterable filterable;

  public Message() {

  }

  public Message(Integer localTimestamp, Filterable filterable) {
    this.localTimestamp = localTimestamp;
    this.filterable = filterable;
  }

  public Integer getLocalTimestamp() {
    return localTimestamp;
  }

  public void setLocalTimestamp(Integer localTimestamp) {
    this.localTimestamp = localTimestamp;
  }

  public Filterable getFilterable() {
    return filterable;
  }

  public void setFilterable(Filterable filterable) {
    this.filterable = filterable;
  }
}
