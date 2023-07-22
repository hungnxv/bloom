package vn.edu.hcmut.nxvhung.bloomfilter.impl;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Key implements Comparable<Key> {

  private byte[] bytes;

  private String value;

  public Key() {}

  public Key(byte[] value) {
    this.bytes = value;
  }

  public Key(String value) {
    this.value = value;
    bytes = this.value.getBytes(StandardCharsets.UTF_8);
  }


  public static Key of(String key) {
    return new Key(key);
  }

  public void set(byte[] value) {
    if (value == null) {
      throw new IllegalArgumentException("value can not be null");
    }
    this.bytes = value;
  }

  public byte[] getBytes() {
    return this.bytes;
  }


  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Key)) {
      return false;
    }
    return this.compareTo((Key)o) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bytes);
  }

  // Comparable
  public int compareTo(Key other) {
    int result = this.bytes.length - other.getBytes().length;
    for (int i = 0; result == 0 && i < bytes.length; i++) {
      result = this.bytes[i] - other.bytes[i];
    }
    return result;
  }
}
