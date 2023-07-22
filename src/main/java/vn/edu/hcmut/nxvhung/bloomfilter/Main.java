package vn.edu.hcmut.nxvhung.bloomfilter;

import vn.edu.hcmut.nxvhung.bloomfilter.hash.Hash;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.CountingBloomFilter;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.Key;

public class Main {
  public static void main(String[] args) {

    final CountingBloomFilter cbf = new CountingBloomFilter(100, 2, Hash.MURMUR_HASH);
    final Key key = Key.of("abc");
    cbf.add(key);
    System.out.println("key present after insertion " +  cbf.exists(key));

    cbf.delete(key);
    System.out.println("key gone after deletion: " +  cbf.exists(key));



  }

}
