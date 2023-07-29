package vn.edu.hcmut.nxvhung.bloomfilter.impl;

import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class CountingBloomFilter extends Filter {
  protected long[] buckets;

  protected final static long BUCKET_MAX_VALUE = 15;


  public CountingBloomFilter(int vectorSize, int nbHash, int hashType) {
    super(vectorSize, nbHash, hashType);
    buckets = new long[buckets2words(vectorSize)];
  }

  /** returns the number of 64 bit words it would take to hold vectorSize buckets
   * @param vectorSize total number of buckets.
   * @return the number of 64 bit words it would take to hold vectorSize buckets.
   */
  static int buckets2words(int vectorSize) {
    return ((vectorSize - 1) >>> 4) + 1;
  }


  @Override
  public void add(Key key) {
    if(key == null) {
      throw new NullPointerException("key can not be null");
    }

    int[] h = hash.hash(key);
    inc(h);
  }

  @Override
  public Filterable<Key> merge(Filterable<Key> bloomFilter) {
    throw new UnsupportedOperationException("Counting Bloom filter does not support merge");
  }

  protected void inc(int[] h) {
    for(int i = 0; i < numberOfHashes; i++) {
      // find the bucket
      int wordNum = h[i] >> 4;          // div 16
      int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

      long bucketMask = 15L << bucketShift;
      long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

      // only increment if the count in the bucket is less than BUCKET_MAX_VALUE
      if(bucketValue < BUCKET_MAX_VALUE) {
        // increment by 1
        buckets[wordNum] = (buckets[wordNum] & ~bucketMask) | ((bucketValue + 1) << bucketShift);
      }
    }
  }

  public void delete(Key key) {
    if(key == null) {
      throw new NullPointerException("Key may not be null");
    }
    if(!this.mayExists(key)) {
      throw new IllegalArgumentException("Key is not a member");
    }

    int[] h = hash.hash(key);

    for(int i = 0; i < numberOfHashes; i++) {
      // find the bucket
      int wordNum = h[i] >> 4;          // div 16
      int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

      long bucketMask = 15L << bucketShift;
      long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

      // only decrement if the count in the bucket is between 0 and BUCKET_MAX_VALUE
      if(bucketValue >= 1 && bucketValue < BUCKET_MAX_VALUE) {
        // decrement by 1
        buckets[wordNum] = (buckets[wordNum] & ~bucketMask) | ((bucketValue - 1) << bucketShift);
      }
    }
  }


  @Override
  public boolean mayExists(Key key) {
    if(key == null) {
      throw new NullPointerException("Key may not be null");
    }

    int[] h = hash.hash(key);

    for(int i = 0; i < numberOfHashes; i++) {
      // find the bucket
      int wordNum = h[i] >> 4;          // div 16
      int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

      long bucketMask = 15L << bucketShift;

      if((buckets[wordNum] & bucketMask) == 0) {
        return false;
      }
    }

    return true;
  }

  public int approximateCount(Key key) {
    int res = Integer.MAX_VALUE;
    int[] h = hash.hash(key);
    for (int i = 0; i < numberOfHashes; i++) {
      // find the bucket
      int wordNum = h[i] >> 4;          // div 16
      int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

      long bucketMask = 15L << bucketShift;
      long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;
      if (bucketValue < res) res = (int)bucketValue;
    }
    if (res != Integer.MAX_VALUE) {
      return res;
    } else {
      return 0;
    }
  }



  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();

    for(int i = 0; i < vectorSize; i++) {
      if(i > 0) {
        res.append(" ");
      }

      int wordNum = i >> 4;          // div 16
      int bucketShift = (i & 0x0f) << 2;  // (mod 16) * 4

      long bucketMask = 15L << bucketShift;
      long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

      res.append(bucketValue);
    }

    return res.toString();
  }
}