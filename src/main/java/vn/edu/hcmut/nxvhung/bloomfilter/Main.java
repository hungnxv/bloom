package vn.edu.hcmut.nxvhung.bloomfilter;

import vn.edu.hcmut.nxvhung.bloomfilter.hash.Hash;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.BloomFilter;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.CountingBloomFilter;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.Key;
import vn.edu.hcmut.nxvhung.bloomfilter.impl.MergeableCountingBloomFilter;

public class Main {
  public static void main(String[] args) {

//    BloomFilter bf = new BloomFilter(10, 2, Hash.MURMUR_HASH);
//    Key key = Key.of("0979561151");
//    bf.add(key);
//    System.out.println("key present after insertion " +  bf.mayExists(key));
//
//
//    final CountingBloomFilter cbf = new CountingBloomFilter(100, 2, Hash.MURMUR_HASH);
//    key = Key.of("abc");
//    cbf.add(key);
//    System.out.println("key present after insertion " +  cbf.mayExists(key));
//
//    cbf.delete(key);
//    System.out.println("key gone after deletion: " +  cbf.mayExists(key));

    Key key = Key.of("0979561151");
    MergeableCountingBloomFilter mergeableCountingBloomFilter = new MergeableCountingBloomFilter(10, 2, Hash.MURMUR_HASH, 4);
    System.out.println("key present after insertion " +  mergeableCountingBloomFilter.mayExists(key));

    mergeableCountingBloomFilter.add(key);

    Key key1 = Key.of("0979561123");
    mergeableCountingBloomFilter.add(key1);

    Key key2 = Key.of("0989561123");
    mergeableCountingBloomFilter.add(key2);

    Key key3 = Key.of("0929561123");
    mergeableCountingBloomFilter.add(key3);

    Key key4 = Key.of("0979561123");
    mergeableCountingBloomFilter.add(key4);
    System.out.println("key present after insertion " +  mergeableCountingBloomFilter.mayExists(key));


  }

}
