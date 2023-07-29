package vn.edu.hcmut.nxvhung.bloomfilter.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;
import vn.edu.hcmut.nxvhung.bloomfilter.hash.Hash;

public class MergeableCountingBloomFilter extends Filter {
  protected long[] buckets;


  private List<BitSet> bitSetList;
  private BitSet orBitSet;
  private int sizeOfBitset;
  private int numberOfBits;

  protected final static long BUCKET_MAX_VALUE = 15;

  public MergeableCountingBloomFilter(int vectorSize, int nbHash, int hashType, int numberOfBits) {
    super(vectorSize, nbHash, hashType);

    this.sizeOfBitset = (int) Math.pow(2, numberOfBits) - 1;
    bitSetList = new ArrayList<>(sizeOfBitset);
    for(int i = 0; i < sizeOfBitset- 1; i++) {
      bitSetList.add(new BitSet(vectorSize));
    }
    hashForCuckoo = Hash.getInstance(Hash.MURMUR_HASH, 1, )
    this.numberOfBits = numberOfBits;
    orBitSet = new BitSet(vectorSize);
  }



  @Override
  public void add(Key key) {
    if(key == null) {
      throw new NullPointerException("key can not be null");
    }

    int[] h = hash.hash(key);

    inc(h);
    recalculateOrbit();
  }

  private void recalculateOrbit() {
    for(BitSet bitSet:  bitSetList) {
      orBitSet.or(bitSet);
    }
  }


  @Override
  public Filterable<Key> merge(Filterable<Key> filter) {
    if (filter == null || !(filter instanceof MergeableCountingBloomFilter)) {
      throw new IllegalArgumentException("filters cannot be merged");
    }

    MergeableCountingBloomFilter other = (MergeableCountingBloomFilter) filter;
    if (
        other.vectorSize != this.vectorSize
            || other.numberOfHashes != this.numberOfHashes
            || other.sizeOfBitset != this.sizeOfBitset) {
      throw new IllegalArgumentException("filters cannot be merged");
    }
    MergeableCountingBloomFilter mergedCBF = new MergeableCountingBloomFilter(this.vectorSize, numberOfHashes, hashType, numberOfBits);

    for(int i =0; i< sizeOfBitset; i++) {
      BitSet mergedBitset = mergedCBF.bitSetList.get(i);
      mergedBitset.or(other.bitSetList.get(i));
      mergedBitset.or(this.bitSetList.get(i));
    }
    mergedCBF.recalculateOrbit();
    return mergedCBF;
  }

  protected void inc(int[] h) {

    for(int i = 0; i < numberOfHashes; i++) {
      // find the bucket
      int currentIndex = 0;
      if(bitSetList.get(currentIndex).get(h[i]) == true) {
        //find another bucket
        virtualCuckoo(i);
      } else {
        bitSetList.get(currentIndex).set(h[i]);

      }

    }
  }

  private int virtualCuckoo(int index) {
//    while(
    return hash.hashSingleKey(index) % numberOfBits;
)
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