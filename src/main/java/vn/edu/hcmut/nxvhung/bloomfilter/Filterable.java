package vn.edu.hcmut.nxvhung.bloomfilter;

public interface Filterable<T> {

  void add(T element);

  Filterable<T> merge(Filterable<T> bloomFilter);

  void delete(T element);

  boolean exists(T element);

}
