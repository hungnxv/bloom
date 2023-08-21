package vn.edu.hcmut.nxvhung.bloomfilter;

public interface Filterable<T> extends Cloneable{

  void add(T element);

  Filterable<T> merge(Filterable<T> bloomFilter);

  void delete(T element);

  boolean mayExists(T element);

  Object clone() throws CloneNotSupportedException;

}
