package vn.edu.hcmut.nxvhung.bloomfilter.temp;

public interface UniversalHashFunction<T> {
   HashFunction<T> randomHashFunction(int buckets);

}
