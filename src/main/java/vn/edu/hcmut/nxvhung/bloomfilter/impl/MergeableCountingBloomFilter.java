package vn.edu.hcmut.nxvhung.bloomfilter.impl;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class MergeableCountingBloomFilter extends Filter implements Serializable {

  private static final long serialVersionUID = 2346766574523341240L;

  private List<BitSet> bitSetList;
  private BitSet orBitSet;
  private int sizeOfBitset;
  private int numberOfBits;

  public MergeableCountingBloomFilter(int vectorSize, int nbHash, int hashType, int numberOfBits) {
    super(vectorSize, nbHash, hashType);

    this.sizeOfBitset = (int) Math.pow(2, numberOfBits) - 1;
    bitSetList = new ArrayList<>(sizeOfBitset);
    for (int i = 0; i < sizeOfBitset; i++) {
      bitSetList.add(new BitSet(vectorSize));
    }
    this.numberOfBits = numberOfBits;
    orBitSet = new BitSet(vectorSize);
  }


  @Override
  public void add(Key key) {
    if (key == null) {
      throw new NullPointerException("key can not be null");
    }

    int[] h = hash.hash(key);

    inc(h);
    recalculateOrbit();
  }

  private void recalculateOrbit() {
    for (BitSet bitSet : bitSetList) {
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

    for (int i = 0; i < sizeOfBitset; i++) {
      BitSet mergedBitset = mergedCBF.bitSetList.get(i);
      mergedBitset.or(other.bitSetList.get(i));
      mergedBitset.or(this.bitSetList.get(i));
    }
    mergedCBF.recalculateOrbit();
    return mergedCBF;
  }

  protected void inc(int[] h) {
    int currentIndex = hash(Integer.valueOf(0), sizeOfBitset, 2);
    for (int i = 0; i < numberOfHashes; i++) {

      // find the bucket
      if (bitSetList.get(currentIndex).get(h[i])) {
        //find another bucket
        currentIndex = virtualCuckoo(currentIndex, h[i]);
        if(currentIndex < 0) {
          System.out.println("Skip ");
          return;
//          throw new RuntimeException("The Filter is full");
        }
        bitSetList.get(currentIndex).set(h[i]);
      } else {
        bitSetList.get(currentIndex).set(h[i]);
      }

    }
  }

  private int virtualCuckoo(int index, int bitPosition) {
    Integer current = index;
    int count = 0;
    while (count < sizeOfBitset) {
      current = hash(current, sizeOfBitset, 3);
      count++;
      if (!bitSetList.get(current).get(bitPosition)) {
        return current;
      }
    }
    return -1;
  }

  private static final Random random = new Random();

  public int hash(Object key, int limit, int rounds) {
    random.setSeed(key.hashCode());
    int h = random.nextInt(limit);
    for (int i = 1; i < rounds; i++) {
      h = random.nextInt(limit);
    }

    return h;
  }
  private int hash(Object object) {
    int h = object.hashCode();
    int hash = h ^= (h >>> 20) ^ (h >>> 12);
    return hash & sizeOfBitset;
  }

  public void delete(Key key) {
    if (key == null) {
      throw new NullPointerException("Key may not be null");
    }
    if (!this.mayExists(key)) {
      throw new IllegalArgumentException("Key is not a member");
    }

    int[] h = hash.hash(key);
    for (int i = 0; i < numberOfHashes; i++) {
      int currentIndex = 0;
      // find the bucket
      if (bitSetList.get(currentIndex).get(h[i])) {
        bitSetList.get(currentIndex).set(h[i], false);
      } else {
        //find another bucket
        currentIndex = virtualCuckoo(currentIndex, h[i]);
        bitSetList.get(currentIndex).set(h[i], false);
      }

    }

    recalculateOrbit();

  }


  @Override
  public boolean mayExists(Key key) {
    if (key == null) {
      throw new NullPointerException("Key may not be null");
    }

    int[] h = hash.hash(key);

    for (int index: h) {
      if(!orBitSet.get(index)) {
        return false;
      }
    }

    return true;
  }


  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();

    for (BitSet bitSet: bitSetList) {
      res.append(bitSet.toString());
      res.append("\n");
    }

    return res.toString();
  }


  @Override
  public Object clone() throws CloneNotSupportedException {
    MergeableCountingBloomFilter mergeableCountingBloomFilter = (MergeableCountingBloomFilter) super.clone();
    mergeableCountingBloomFilter.bitSetList = new ArrayList<>(bitSetList.size());
    bitSetList.forEach(bitSet -> mergeableCountingBloomFilter.bitSetList.add((BitSet) bitSet.clone()));
    mergeableCountingBloomFilter.orBitSet = (BitSet) this.orBitSet.clone();
    return mergeableCountingBloomFilter;
  }

}