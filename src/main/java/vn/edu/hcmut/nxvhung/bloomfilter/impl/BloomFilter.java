
package vn.edu.hcmut.nxvhung.bloomfilter.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.BitSet;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;

public class BloomFilter extends Filter {
  private static final byte[] bitvalues = new byte[] {
    (byte)0x01,
    (byte)0x02,
    (byte)0x04,
    (byte)0x08,
    (byte)0x10,
    (byte)0x20,
    (byte)0x40,
    (byte)0x80
  };

  /** The bit vector. */
  BitSet bits;

  /** Default constructor - use with readFields */
  public BloomFilter() {
    super();
  }

  public BloomFilter(int vectorSize, int nbHash, int hashType) {
    super(vectorSize, nbHash, hashType);

    bits = new BitSet(this.vectorSize);
  }

  @Override
  public void add(Key key) {
    if(key == null) {
      throw new NullPointerException("key cannot be null");
    }

    int[] h = hash.hash(key);

    for(int i = 0; i < numberOfHashes; i++) {
      bits.set(h[i]);
    }
  }

  @Override
  public Filterable<Key> merge(Filterable<Key> filter) {
    if(filter == null
        || !(filter instanceof BloomFilter)
        || ((BloomFilter) filter).vectorSize != this.vectorSize
        || ((BloomFilter) filter).numberOfHashes != this.numberOfHashes) {
      throw new IllegalArgumentException("filters cannot be and-ed");
    }

    this.bits.and(((BloomFilter) filter).bits);

    BitSet mergedBitset = new BitSet(this.vectorSize);
    mergedBitset.and(bits);
    mergedBitset.and(((BloomFilter) filter).bits);
    BloomFilter result = new BloomFilter();
    result.bits = mergedBitset;
    return result;
  }

  @Override
  public void delete(Key element) {
    throw new UnsupportedOperationException("Bloom Filter does not support delete");
  }

  @Override
  public boolean mayExists(Key key) {
    if(key == null) {
      throw new NullPointerException("key cannot be null");
    }

    int[] h = hash.hash(key);
    for(int i = 0; i < numberOfHashes; i++) {
      if(!bits.get(h[i])) {
        return false;
      }
    }
    return true;
  }


  @Override
  public String toString() {
    return bits.toString();
  }

  public int getVectorSize() {
    return this.vectorSize;
  }


  @Override
  public void write(DataOutput out) throws IOException {
    super.write(out);
    byte[] bytes = new byte[getNBytes()];
    for(int i = 0, byteIndex = 0, bitIndex = 0; i < vectorSize; i++, bitIndex++) {
      if (bitIndex == 8) {
        bitIndex = 0;
        byteIndex++;
      }
      if (bitIndex == 0) {
        bytes[byteIndex] = 0;
      }
      if (bits.get(i)) {
        bytes[byteIndex] |= bitvalues[bitIndex];
      }
    }
    out.write(bytes);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    super.readFields(in);
    bits = new BitSet(this.vectorSize);
    byte[] bytes = new byte[getNBytes()];
    in.readFully(bytes);
    for(int i = 0, byteIndex = 0, bitIndex = 0; i < vectorSize; i++, bitIndex++) {
      if (bitIndex == 8) {
        bitIndex = 0;
        byteIndex++;
      }
      if ((bytes[byteIndex] & bitvalues[bitIndex]) != 0) {
        bits.set(i);
      }
    }
  }

  /* @return number of bytes needed to hold bit vector */
  private int getNBytes() {
    return (vectorSize + 7) / 8;
  }
}//end class
