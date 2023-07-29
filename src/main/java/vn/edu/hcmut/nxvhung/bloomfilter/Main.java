package vn.edu.hcmut.nxvhung.bloomfilter;

import vn.edu.hcmut.nxvhung.bloomfilter.hash.Hash;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.BloomFilter;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.CountingBloomFilter;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.Key;

public class Main {
  public static void main(String[] args) {

    BloomFilter bf = new BloomFilter(10, 2, Hash.MURMUR_HASH);
    Key key = Key.of("0979561151");
    bf.add(key);
    System.out.println("key present after insertion " +  bf.mayExists(key));


    final CountingBloomFilter cbf = new CountingBloomFilter(100, 2, Hash.MURMUR_HASH);
    key = Key.of("abc");
    cbf.add(key);
    System.out.println("key present after insertion " +  cbf.mayExists(key));

    cbf.delete(key);
    System.out.println("key gone after deletion: " +  cbf.mayExists(key));



  }

}
