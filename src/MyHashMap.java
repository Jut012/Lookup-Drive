import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Iterator;

public class MyHashMap<K, V> implements DefaultMap<K, V> {
	public static final double DEFAULT_LOAD_FACTOR = 0.75;
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	public static final String ILLEGAL_ARG_CAPACITY = "Initial Capacity must be non-negative";
	public static final String ILLEGAL_ARG_LOAD_FACTOR = "Load Factor must be positive";
	public static final String ILLEGAL_ARG_NULL_KEY = "Keys must be non-null";
	
	private double loadFactor;
	private int capacity;
	private int size;

	//for Separate Chaining conflict resolution
	private List<HashMapEntry<K, V>>[] buckets;  
	
	//for Linear Probing

	public MyHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	@SuppressWarnings("unchecked")
	public MyHashMap(int initialCapacity, double loadFactor) throws IllegalArgumentException {
		if (initialCapacity < 0) throw new IllegalArgumentException(ILLEGAL_ARG_CAPACITY);
		if (loadFactor <= 0 || Double.isNaN(loadFactor)) throw new IllegalArgumentException(ILLEGAL_ARG_LOAD_FACTOR);
	
		this.capacity = initialCapacity;
		this.loadFactor = loadFactor;
		this.size = 0;
		buckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];
		    for (int i = 0; i < capacity; i++) {
       		buckets[i] = new ArrayList<>();
    }
	}

	@Override
	public boolean put(K key, V value) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

		int index = Math.abs(key.hashCode()) % capacity;
		List<HashMapEntry<K, V>> bucket = buckets[index];
		for (HashMapEntry<K, V> entry : bucket) {
			if (entry.getKey().equals(key)) {
				entry.setValue(value);
				return false;
			}
	}
		bucket.add(new HashMapEntry<>(key, value));
		size++;
		if ((double) size / capacity > loadFactor) {
			resize();
	}
		return true;
}

	@Override
	public boolean replace(K key, V newValue) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

		int index = Math.abs(key.hashCode()) % capacity;
		List<HashMapEntry<K, V>> bucket = buckets[index];
	
		for (HashMapEntry<K, V> entry : bucket) {
			if (entry.getKey().equals(key)) {
				entry.setValue(newValue);
				return true;
			}
		}
		return false; 
	}

	@Override
	public boolean remove(K key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

		int index = Math.abs(key.hashCode()) % capacity;
		List<HashMapEntry<K, V>> bucket = buckets[index];
	
		Iterator<HashMapEntry<K, V>> it = bucket.iterator();
		while (it.hasNext()) {
			HashMapEntry<K, V> entry = it.next();
			if (entry.getKey().equals(key)) {
				it.remove();
				size--;
				return true;
			}
		}
		return false;
	}

	@Override
	public void set(K key, V value) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

		int index = Math.abs(key.hashCode()) % capacity;
		List<HashMapEntry<K, V>> bucket = buckets[index];
	
		for (HashMapEntry<K, V> entry : bucket) {
			if (entry.getKey().equals(key)) {
				entry.setValue(value);
				return; // Value replaced
			}
		}
		bucket.add(new HashMapEntry<>(key, value)); // Value added
		size++;
		if ((double) size / capacity > loadFactor) {
			resize();
		}
	}

	@Override
	public V get(K key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

		int index = Math.abs(key.hashCode()) % capacity;
		List<HashMapEntry<K, V>> bucket = buckets[index];
	
		for (HashMapEntry<K, V> entry : bucket) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(K key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);

		int index = Math.abs(key.hashCode()) % capacity;
		List<HashMapEntry<K, V>> bucket = buckets[index];
	
		for (HashMapEntry<K, V> entry : bucket) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<K> keys() {
		List<K> keyList = new ArrayList<>();
		for (List<HashMapEntry<K, V>> bucket : buckets) {
			for (HashMapEntry<K, V> entry : bucket) {
				keyList.add(entry.getKey());
			}
		}
		return keyList;
	}
	
	private static class HashMapEntry<K, V> implements DefaultMap.Entry<K, V> {
		
		K key;
		V value;
		
		private HashMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public void setValue(V value) {
			this.value = value;
		}
	}

	private void resize() {
		int newCapacity = capacity * 2;
		List<HashMapEntry<K, V>>[] newBuckets = (List<HashMapEntry<K, V>>[]) new List[newCapacity];
		for (int i = 0; i < newCapacity; i++) {
			newBuckets[i] = new ArrayList<>();
		}
	
		for (List<HashMapEntry<K, V>> bucket : buckets) {
			for (HashMapEntry<K, V> entry : bucket) {
				int index = Math.abs(entry.getKey().hashCode()) % newCapacity;
				newBuckets[index].add(entry);
			}
		}
		buckets = newBuckets;
		capacity = newCapacity;
	}

	public ArrayList<FileData> entrySet() {
		throw new UnsupportedOperationException("Unimplemented method 'entrySet'");
	}
	
}
