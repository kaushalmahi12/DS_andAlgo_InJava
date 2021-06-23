import java.util.*;

class Entry<K, V> {
  K key;
  V value;
  int hash;

  Entry(K k, V v) {
    this.key = k;
    this.value = v;
    this.hash = k.hashCode(); // this can be negative as well
  }

  public boolean equals(Entry<K, V> other) {
    if (this == other) return true;
    if (this.hash != other.hash) return false;
    return this.key.equals(other.key);
  }
   
  public String toString() {
    return key + "=>" + value;
  }
}

public class HashTable<K, V> {
  private LinkedList<Entry<K,V>> [] table;
  private int  capacity, size=0, threshold;
  private static final int  DEFAULT_CAP = 4;
  private static final double  DEFAULT_LOAD_FACTOR = 0.75;
  private double loadFactor;

  public HashTable() {
    this(DEFAULT_CAP, DEFAULT_LOAD_FACTOR);
  }

  public HashTable(int cap) {
    this(cap, DEFAULT_LOAD_FACTOR);
  }

  public HashTable(int cap, double factor) {
    if (cap < 0) throw new IllegalArgumentException("capacity can't be negative");

    if ( factor<=0 || Double.isNaN(factor) || Double.isInfinite(factor)) throw new IllegalArgumentException("Illegal factor");
    this.table = new LinkedList[cap];
    this.capacity = cap;
    this.loadFactor = factor;
    this.threshold = (int) (factor * cap); // this indicates when should we rehash the table
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  public void clear() {
    Arrays.fill(table, null);
    size = 0;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (LinkedList<Entry<K, V>> bucket: table) {
      if (bucket != null) {
        for (Entry<K, V> entry: bucket) {
          sb.append(entry.toString()+", ");
        }
      }
    }
    sb.append("}");
    return sb.toString();
  }
 

  public boolean containsKey(K key) {
    return hasKey(key);
  }

  // to make remove the sign and bound it
  private int trueIndex(int ind) {
    return (ind & 0x7FFFFFFF) % this.capacity;
  }

  private boolean hasKey(K key) {
    int index = key.hashCode();
    return findEntry(trueIndex(index), key) != null;
  }

  public V put(K key, V val) {
    if (key == null) throw new IllegalArgumentException("key can't be null");

    Entry<K,V> entry = new Entry<>(key, val);
    int ind = entry.hash;
    ind = trueIndex(ind);
    
    if (this.table[ind] == null) this.table[ind] = new LinkedList<>();
    
    Entry<K, V> existingEntry = findEntry(ind, key);
    
    if (existingEntry == null) {
      this.table[ind].add(entry);
      size += 1;
      if (size > threshold) resizeTable();
      return null;
    }
    V old = existingEntry.value;
    existingEntry.value = val;
    return old;
  }

  public V get(K key) {
    if (key == null) return null;
    int ind = trueIndex(key.hashCode());
    Entry<K, V> entry = findEntry(ind, key);
    if (entry != null ) return entry.value;
    return null;
  }

  public V removeKey(K key) {
    if (key == null) throw new IllegalArgumentException("key can't be null");

    int ind = trueIndex(key.hashCode());
    Entry<K, V> entry = findEntry(ind, key);

    if (entry != null) {
      V val = entry.value;
      this.table[ind].remove(entry);
      this.size -= 1;
      return val;
    }
    return null;
  }

  private void resizeTable() {
    this.capacity *= 2;
    this.threshold = (int) (this.capacity * this.loadFactor);
    LinkedList<Entry<K, V>>[] newTable = new LinkedList[this.capacity]; 
    
    for (int i=0; i<this.table.length; i++) {
      if (this.table[i] != null) {
        for (Entry<K,V> entry: this.table[i]) {
          int index = trueIndex(entry.hash);
          if (newTable[index] == null) newTable[index] = new LinkedList<>();
          newTable[index].add(entry);
        }
        // reduce the overhead on GC;
        table[i].clear();
        table[i] = null;
     }
   }
   this.table = newTable;
  }

  public Entry<K, V> findEntry(int bucketIndex, K key) {
    if (key == null) return null;

    LinkedList<Entry<K,V>> bucket = this.table[bucketIndex];

    if (bucket == null) return null;

    for (Entry<K, V> entry: bucket) {
      if (entry.key.equals(key)) {
        return entry;
      }
    }
    return null;
  }

  public List<K> keys() {
    List<K> keys = new ArrayList<>(size());
    for (LinkedList<Entry<K, V>> bucket: this.table) {
      if (bucket != null) {
        for (Entry<K, V> entry: bucket) {
          keys.add(entry.key);
        }
      }
    }
    return keys;
  }

  public List<V> values() {
    List<V> values = new ArrayList<>(size());
    for (LinkedList<Entry<K, V>> bucket: this.table) {                           
     if (bucket != null) {                                                      
       for (Entry<K, V> entry: bucket) {                                        
         values.add(entry.value);                                                   
       }                                                                        
     }                                                                          
    }
    return values;
  }
}
