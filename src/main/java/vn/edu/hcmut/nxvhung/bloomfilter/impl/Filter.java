package vn.edu.hcmut.nxvhung.bloomfilter.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import vn.edu.hcmut.nxvhung.bloomfilter.Filterable;
import vn.edu.hcmut.nxvhung.bloomfilter.hash.Hash;

public abstract class Filter implements Filterable<Key>, Serializable {
  private static final long serialVersionUID = 2346766574523341240L;
  private static final int VERSION = -1;
  protected int vectorSize;

  protected HashFunction hash;

  protected int numberOfHashes;

  protected int hashType;

  protected Filter() {}

  protected Filter(int vectorSize, int numberOfHashes, int hashType) {
    this.vectorSize = vectorSize;
    this.numberOfHashes = numberOfHashes;
    this.hashType = hashType;
    this.hash = new HashFunction(this.vectorSize, this.numberOfHashes, this.hashType);
  }

  public abstract void add(Key key);

  public abstract boolean mayExists(Key key);


  public void add(Collection<Key> keys){
    if(keys == null) {
      throw new IllegalArgumentException("Collection<Key> may not be null");
    }
    for(Key key: keys) {
      add(key);
    }
  }

  public void write(DataOutput out) throws IOException {
    out.writeInt(VERSION);
    out.writeInt(this.numberOfHashes);
    out.writeByte(this.hashType);
    out.writeInt(this.vectorSize);
  }

  public void readFields(DataInput in) throws IOException {
    int ver = in.readInt();
    if (ver > 0) { // old unversioned format
      this.numberOfHashes = ver;
      this.hashType = Hash.JENKINS_HASH;
    } else if (ver == VERSION) {
      this.numberOfHashes = in.readInt();
      this.hashType = in.readByte();
    } else {
      throw new IOException("Unsupported version: " + ver);
    }
    this.vectorSize = in.readInt();
    this.hash = new HashFunction(this.vectorSize, this.numberOfHashes, this.hashType);
  }

  @Override
  public Object clone() throws CloneNotSupportedException{
    return  super.clone();
  }
}//end class
