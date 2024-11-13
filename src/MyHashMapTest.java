import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.*;

public class MyHashMapTest {
	
	private DefaultMap<String, String> testMap; // use this for basic tests
	private DefaultMap<String, String> mapWithCap; // use for testing proper rehashing
	public static final String TEST_KEY = "Test Key";
	public static final String TEST_VAL = "Test Value";
	
	@Before
	public void setUp() {
		testMap = new MyHashMap<>();
		mapWithCap = new MyHashMap<>(4, MyHashMap.DEFAULT_LOAD_FACTOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPut_nullKey() {
		testMap.put(null, TEST_VAL);
	}

	@Test
	public void testKeys_nonEmptyMap() {
		List<String> expectedKeys = new ArrayList<>(5);
		for(int i = 0; i < 5; i++) {
			// key + i is used to differentiate keys since they must be unique
			testMap.put(TEST_KEY + i, TEST_VAL + i);
			expectedKeys.add(TEST_KEY + i);
		}
		List<String> resultKeys = testMap.keys();
		// we need to sort because hash map doesn't guarantee ordering
		Collections.sort(resultKeys);
		assertEquals(expectedKeys, resultKeys);
	}
	
    @Test
    public void testRemoveKey() {
        testMap.put("testKey", "testValue");
        assertTrue(testMap.containsKey("testKey"));
        testMap.remove("testKey");
        assertFalse(testMap.containsKey("testKey"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(testMap.isEmpty());
        testMap.put("testKey", "testValue");
        assertFalse(testMap.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, testMap.size());
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        assertEquals(2, testMap.size());
    }

    @Test
    public void testReplaceValue() {
        testMap.put("key", "initialValue");
        assertTrue(testMap.replace("key", "newValue"));
        assertEquals("newValue", testMap.get("key"));
    }
}