package vn.edu.hcmut.nxvhung.bloomfilter.temp;

import java.util.Arrays;

public class CuckooHash<K, V> {
  private static final int DEFAULT_CAPACITY = 10;
  private static final int MAX_REHASH_ATTEMPTS = 10;

  private int size;
  private int capacity;
  private int numTables;
  private int[] tableSizes;
  private Object[][] tables;
  private int[] hashValues;

  public CuckooHash() {
    this(DEFAULT_CAPACITY);
  }

  public CuckooHash(int capacity) {
    this.size = 0;
    this.capacity = capacity;
    this.numTables = 2;
    this.tableSizes = new int[numTables];
    this.tableSizes[0] = capacity;
    this.tableSizes[1] = capacity;
    this.tables = new Object[numTables][capacity];
    this.hashValues = new int[numTables];
  }

  private int hash(K key, int table) {
    int hash = key.hashCode();
    if (table == 1) {
      return hash;
    }
    return hash ^ hashValues[table - 1];
  }

  private void rehash() {
    int oldCapacity = capacity;
    int oldNumTables = numTables;
    int[] oldTableSizes = Arrays.copyOf(tableSizes, numTables);
    Object[][] oldTables = Arrays.copyOf(tables, numTables);
    int[] oldHashValues = Arrays.copyOf(hashValues, numTables);

    capacity *= 2;
    tableSizes = new int[numTables];
    tableSizes[0] = capacity;
    tableSizes[1] = capacity;
    tables = new Object[numTables][capacity];
    hashValues = new int[numTables];

    for (int i = 0; i < oldNumTables; i++) {
      for (int j = 0; j < oldTableSizes[i]; j++) {
        if (oldTables[i][j] != null) {
          insert((K) oldTables[i][j]);
        }
      }
    }
  }

  public void insert(K key) {
    if (size >= capacity) {
      rehash();
    }

    int hash1 = hash(key, 1) % tableSizes[0];
    int hash2 = hash(key, 2) % tableSizes[1];

    for (int i = 0; i < MAX_REHASH_ATTEMPTS; i++) {
      if (tables[0][hash1] == null) {
        tables[0][hash1] = key;
        size++;
        return;
      } else if (tables[1][hash2] == null) {
        tables[1][hash2] = key;
        size++;
        return;
      }

      K temp = (K) tables[0][hash1];
      tables[0][hash1] = key;
      key = temp;

      hash1 = hash(key, 1) % tableSizes[0];
      hash2 = hash(key, 2) % tableSizes[1];
    }

    rehash();
    insert(key);
  }

  public boolean contains(K key) {
    int hash1 = hash(key, 1) % tableSizes[0];
    int hash2 = hash(key, 2) % tableSizes[1];

    return tables[0][hash1] != null && tables[0][hash1].equals(key) ||
        tables[1][hash2] != null && tables[1][hash2].equals(key);
  }

  public void remove(K key) {
    int hash1 = hash(key, 1) % tableSizes[0];
    int hash2 = hash(key, 2) % tableSizes[1];

    if (tables[0][hash1] != null && tables[0][hash1].equals(key)) {
      tables[0][hash1] = null;
      size--;
    } else if (tables[1][hash2] != null && tables[1][hash2].equals(key)) {
      tables[1][hash2] = null;
      size--;
    }
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public static void main(String[] args) {
    CuckooHash<String, String> cuckooHash = new CuckooHash<>();
    cuckooHash.insert("a");
    cuckooHash.insert("b");
    cuckooHash.insert("c");
    cuckooHash.insert("3");
    cuckooHash.insert("6");
    cuckooHash.insert("8");
    cuckooHash.insert("9");
  }
}
