import java.util.*;

class QuadProbing<K,V> {
  int keyCount, usedBucks, capacity, threshold;
  double loadFactor;

  K[] keysTable;
  V[] valuesTable;

  public static void main(String[] args) {
    Random rand = new Random();
    int limit = Integer.parseInt(args[0]);
    long start = System.nanoTime();
    QuadProbing<Integer, Integer> mp = new QuadProbing<>();
    for (int i=0; i<limit; i++) {
      if (rand.nextInt(3) > 0) {
        int k = rand.nextInt(1_000_000);
        int v = rand.nextInt(1_000_000);
        mp.put(k, v);
      } else {
        int k = rand.nextInt(1_000_000);
      }
    }
    System.out.println("time to run these queries: " + (System.nanoTime() - start)/1_000_000 + "ms");
  } 

  final K TOMBSTONE = (K) (new Object());

  public static final int DEFAULT_CAPACITY = 2;
  public static final double DEFAULT_LOAD_FACTOR = 0.45;

  public QuadProbing() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  public QuadProbing(int cap, double loadF) {
    if (cap < 0) throw new IllegalArgumentException("Illegal argument value for capacity: " + cap);
    if (loadF <= 0 || Double.isNaN(loadF) || Double.isInfinite(loadF)) {
      throw new IllegalArgumentException("Illegal argument value for loadFactor: " + loadF);
    }
    usedBucks = keyCount = 0;
    this.threshold = (int) (cap * loadF);
    this.capacity = cap;
    this.loadFactor = loadF;

    keysTable = (K[]) new Object[cap];
    valuesTable = (V[]) new Object[cap];
  }

  public int size() {
    return keyCount;
  }

  public void clear() {
    for (int i=0; i<this.capacity; i++) {
      keysTable[i] = null;
      valuesTable[i] = null;
    }
    keyCount = 0; usedBucks = 0;
  }

  // Quadratic probing function
  private int probe(int x) {
    return (x*x + x) >> 1;
  }

  private int trueIndex(int ind) {
    return (ind & 0x7FFFFFFF) % capacity;
  }

  public boolean hasKey(K key) {
    int x = 0, tombIndex = -1;
    int index = trueIndex(key.hashCode());
     while (true) {
      index = trueIndex(index + probe(x++));
      if (keysTable[index] == TOMBSTONE) {
        if (tombIndex == -1) tombIndex = index;
      } else if (keysTable[index] != null && keysTable[index].equals(key)) {
        if (tombIndex != -1) {
          keysTable[tombIndex] = keysTable[index];
          valuesTable[tombIndex] = valuesTable[index];
          keysTable[index] = TOMBSTONE;
          valuesTable[index] = null;
        }
        return true;
      } else {
        return false;
      }
    }
  }

  public V get(K key) {
    if (key == null) throw new IllegalArgumentException("Key is null");

    int x = 0, tombIndex = -1;                                                   
    int index = trueIndex(key.hashCode());                                       
     while (true) {                                                              
      index = trueIndex(index + probe(x++));                                     
      if (keysTable[index] == TOMBSTONE) {                                       
        if (tombIndex == -1) tombIndex = index;                                  
      } else if (keysTable[index] != null) {
        if (keysTable[index].equals(key)) {
          if (tombIndex != -1) {
            V val = valuesTable[index];
            keysTable[tombIndex] = keysTable[index];                               
            valuesTable[tombIndex] = valuesTable[index];                           
            keysTable[index] = TOMBSTONE;                                          
            valuesTable[index] = null;
            return val;
          }
          return valuesTable[index];
        }
        return null;
      } else {
        return null;
      }
    }
  }

  private void resizeTable() {
    capacity *= 2;

    threshold = (int) (capacity * loadFactor);

    K[] k_table = (K[]) new Object[capacity];
    V[] v_table = (V[]) new Object[capacity];

    K[] tmp = keysTable;
    keysTable = k_table;
    k_table = tmp;

    V[] tmpV = valuesTable;
    valuesTable = v_table;
    v_table = tmpV;

    keyCount = usedBucks = 0;
    for (int i=0; i<k_table.length; i++) {
      if (k_table[i] != null && k_table[i] != TOMBSTONE) {
        put(k_table[i], v_table[i]);
      }
    }
  }

  public V put(K key, V val) {
    if (key == null) throw new IllegalArgumentException("Key is null");

    if (usedBucks >= threshold) resizeTable();


    int x = 0, tombIndex = -1;
    int index = trueIndex(key.hashCode());
     while (true) {
      index = trueIndex(index + probe(x++));
      if (keysTable[index] == TOMBSTONE) {
        if (tombIndex == -1) tombIndex = index;
      } else if (keysTable[index] != null) {
        // updating value
        if (keysTable[index].equals(key)) {
          if (tombIndex != -1) {
            V oldVal = valuesTable[index];
            keysTable[tombIndex] = keysTable[index];
            valuesTable[tombIndex] = val;
            keysTable[index] = TOMBSTONE;
            valuesTable[index] = null;
            return oldVal;
          } else {
            usedBucks += 1;
            keyCount += 1;
            valuesTable[index] = val;
            return null;
          }
        }
      } else {
        usedBucks += 1;
        keyCount += 1;
        keysTable[index] = key;
        valuesTable[index] = val;
        return null;
      }
    }
  }

  public V remove(K key) {
    if (key == null) throw new IllegalArgumentException("Key is null"); 

    int x = 0, tombIndex = -1;
    int index = trueIndex(key.hashCode());
    while (true) {
       index = trueIndex(index + probe(x++));
       if (keysTable[index] == TOMBSTONE) {
         if (tombIndex == -1) tombIndex = index;
       } else if (keysTable[index] != null) {
         if (keysTable[index].equals(key)) {
           keyCount -= 1;
           V val = valuesTable[index];
           keysTable[index] = TOMBSTONE;
           valuesTable[index] = null;
           return val;
         } else {
           return null;
         }
       } else {
         return null;
       }
      }
  }
 
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i=0; i<capacity; i++) {
      if (keysTable[i] != null && keysTable[i] != TOMBSTONE) {
        sb.append(keysTable[i] + "=" + valuesTable[i] + "\n");
      }
    }
    return sb.toString();
  }
}
