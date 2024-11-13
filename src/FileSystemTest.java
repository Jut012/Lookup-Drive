import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

public class FileSystemTest {
    private FileSystem fs;

    @Before
    public void setUp() {
        fs = new FileSystem();
        fs.add("report.doc", "/home/user3", "2022-10-03");
        fs.add("example.txt", "/home/user1", "2022-10-01");
        fs.add("example.txt", "/home/user2", "2022-10-02");
        fs.add("test.txt", "/home", "2023-01-01");
    }

    @Test
    public void testAddFile() {
        assertTrue(fs.add("readme.md", "/home", "2023-01-02"));
        assertNotNull(fs.findFile("readme.md", "/home"));
    }

    @Test
    public void testFindNonExistentFile() {
        assertNull(fs.findFile("nonexistent.txt", "/home"));
    }

    @Test
    public void testAddDuplicateFileSameDirectory() {
        assertFalse(fs.add("test.txt", "/home", "2023-01-01"));
    }

    @Test
    public void testRemoveFile() {
        assertTrue(fs.add("remove_me.txt", "/home", "2023-01-03"));
        assertTrue(fs.removeFile("remove_me.txt", "/home"));
        assertNull(fs.findFile("remove_me.txt", "/home"));
    }

    //Tests that I failed and want to see if I can fix
    @Test
    public void testFindFilesInMultiDir() {
        fs.add("shared.txt", "/home/user1", "2023-01-01");
        fs.add("shared.txt", "/home/user2", "2023-01-01");
        ArrayList<FileData> results = fs.findFilesInMultDir("2023-01-01");
        assertEquals(2, results.size());  // Expecting both entries to be returned
        assertTrue(results.stream().anyMatch(f -> f.dir.equals("/home/user1")));
        assertTrue(results.stream().anyMatch(f -> f.dir.equals("/home/user2")));
    }

    @Test
    public void testRemoveByNameSuccessFromDateMap() {
        fs.add("unique.txt", "/home/user1", "2023-01-02");
        fs.removeByName("unique.txt");
        ArrayList<FileData> dateResults = fs.findFilesByDate("2023-01-02");
        assertTrue(dateResults.isEmpty());  // Expect no entries under this date
    }

    @Test
    public void testRemoveFileSuccessFromDateMap() {
        fs.add("temporary.txt", "/home/temp", "2023-01-03");
        fs.removeFile("temporary.txt", "/home/temp");
        ArrayList<FileData> dateResults = fs.findFilesByDate("2023-01-03");
        assertTrue(dateResults.isEmpty());  // Expect no entries under this date after removal
    }

    @Test
    public void testRemoveFileSuccessFromDateMapDeleteEntry() {
        fs.add("lastFile.txt", "/home/last", "2023-01-04");
        fs.removeFile("lastFile.txt", "/home/last");
        assertFalse(fs.dateMap.containsKey("2023-01-04"));  // Expect the key to be removed entirely
    }

    @Test
    public void testRemoveByNameRemovesAllEntriesFromDateMap() {
        fs.removeByName("example.txt");

        assertTrue("Date map should have no entries for the first date", 
            fs.findFilesByDate("2022-10-01").isEmpty());
        assertTrue("Date map should have no entries for the second date", 
            fs.findFilesByDate("2022-10-02").isEmpty());
    }

    @Test
    public void testRemoveByNameSuccessFromDateMapPT2() {

        fs.removeByName("example.txt");

        assertTrue("Date map should have no entries for 2022-10-01 after removing example.txt", 
            fs.findFilesByDate("2022-10-01").isEmpty());
        assertTrue("Date map should have no entries for 2022-10-02 after removing example.txt", 
            fs.findFilesByDate("2022-10-02").isEmpty());
        assertFalse("Date map should still contain entries for dates not associated with example.txt",
            fs.findFilesByDate("2022-10-03").isEmpty());
}
   
    @Test
    public void testRemoveByName() {
        fs.add("duplicate.txt", "/dir1", "2023-01-01");
        fs.add("duplicate.txt", "/dir2", "2023-01-01");
        fs.add("test.txt", "/home", "2023-01-01"); 

        assertNotNull(fs.findFile("duplicate.txt", "/dir1"));
        assertNotNull(fs.findFile("duplicate.txt", "/dir2"));
        assertNotNull(fs.findFile("test.txt", "/home"));  

        assertTrue(fs.removeByName("duplicate.txt"));
        assertNull(fs.findFile("duplicate.txt", "/dir1"));
        assertNull(fs.findFile("duplicate.txt", "/dir2"));

        assertTrue(fs.findFilesByDate("2023-01-01").size() == 1);
        assertFalse(fs.dateMap.get("2023-01-01").stream().anyMatch(f -> f.name.equals("duplicate.txt")));
    }

    @Test
    public void testDateMapCleanupAfterRemoveByName() {
        fs.add("cleanup.txt", "/cleanup/one", "2023-01-01");
        fs.add("cleanup.txt", "/cleanup/two", "2023-01-01");
    
        fs.removeByName("cleanup.txt");
        assertFalse("Date map should not have the date after removal",
            fs.dateMap.containsKey("2023-01-01"));
        
        assertEquals("Name map should be empty after removal", 0, fs.nameMap.size());
        assertEquals("Date map should be empty after removal", 0, fs.dateMap.size());
    }    
}
