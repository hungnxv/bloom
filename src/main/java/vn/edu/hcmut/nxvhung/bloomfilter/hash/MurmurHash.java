package vn.edu.hcmut.nxvhung.bloomfilter.hash;

import java.io.Serializable;

public class MurmurHash extends Hash implements Serializable {
  private static final long serialVersionUID = -2346766574523341240L;

  private static MurmurHash _instance = new MurmurHash();

  public static Hash getInstance() {
    return _instance;
  }

  @Override
  public int hash(byte[] data, int length, int seed) {
    int m = 0x5bd1e995;
    int r = 24;

    int h = seed ^ length;

    int len_4 = length >> 2;

    for (int i = 0; i < len_4; i++) {
      int i_4 = i << 2;
      int k = data[i_4 + 3];
      k = k << 8;
      k = k | (data[i_4 + 2] & 0xff);
      k = k << 8;
      k = k | (data[i_4 + 1] & 0xff);
      k = k << 8;
      k = k | (data[i_4 + 0] & 0xff);
      k *= m;
      k ^= k >>> r;
      k *= m;
      h *= m;
      h ^= k;
    }

    // avoid calculating modulo
    int len_m = len_4 << 2;
    int left = length - len_m;

    if (left != 0) {
      if (left >= 3) {
        h ^= (int) data[length - 3] << 16;
      }
      if (left >= 2) {
        h ^= (int) data[length - 2] << 8;
      }
      if (left >= 1) {
        h ^= (int) data[length - 1];
      }

      h *= m;
    }

    h ^= h >>> 13;
    h *= m;
    h ^= h >>> 15;

    return h;
  }
}
