import java.util.List;
public interface DefaultMap<K, V> {
	
	interface Entry<K, V> {
		K getKey();
		V getValue();
		
		void setValue(V value);
	}

	boolean put(K key, V value) throws IllegalArgumentException;
	

	boolean replace(K key, V newValue) throws IllegalArgumentException;
	

	boolean remove(K key) throws IllegalArgumentException;
	

	void set(K key, V value) throws IllegalArgumentException; 
	

	V get(K key) throws IllegalArgumentException;
	

	int size();
	

	boolean isEmpty();
	

	boolean containsKey(K key) throws IllegalArgumentException;
	
	
	List<K> keys();
}
