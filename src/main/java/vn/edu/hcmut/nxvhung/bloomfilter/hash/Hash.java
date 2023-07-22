package vn.edu.hcmut.nxvhung.bloomfilter.hash;
public abstract class Hash {
  public static final int INVALID_HASH = -1;
  public static final int JENKINS_HASH = 0;
  public static final int MURMUR_HASH  = 1;

  public static int parseHashType(String name) {
    if ("jenkins".equalsIgnoreCase(name)) {
      return JENKINS_HASH;
    } else if ("murmur".equalsIgnoreCase(name)) {
      return MURMUR_HASH;
    } else {
      return INVALID_HASH;
    }
  }

  public static Hash getInstance(int type) {
    switch(type) {
    case JENKINS_HASH:
      return JenkinsHash.getInstance();
    case MURMUR_HASH:
      return MurmurHash.getInstance();
    default:
      return null;
    }
  }

  public int hash(byte[] bytes) {
    return hash(bytes, bytes.length, -1);
  }

  public int hash(byte[] bytes, int seed) {
    return hash(bytes, bytes.length, seed);
  }

  public abstract int hash(byte[] bytes, int length, int seed);
}
