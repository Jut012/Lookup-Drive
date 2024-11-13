import static org.junit.Assert.*;

import org.junit.*;

public class FileDataTest {
    @Test
    public void testConstructorAndAttributes() {
        FileData fileData = new FileData("example.txt", "/home/user", "2021-04-01");
        assertEquals("example.txt", fileData.name);
        assertEquals("/home/user", fileData.dir);
        assertEquals("2021-04-01", fileData.lastModifiedDate);
    }

    @Test
    public void testToStringMethod() {
        FileData fileData = new FileData("example.txt", "/home/user", "2021-04-01");
        String expected = "{Name: example.txt, Directory: /home/user, Modified Date: 2021-04-01}";
        assertEquals(expected, fileData.toString());
    }
    @Test
    public void testInitializationWithNullValues() {
        FileData fileData = new FileData(null, null, null);
        assertNull(fileData.name);
        assertNull(fileData.dir);
        assertNull(fileData.lastModifiedDate);
    }

    @Test
    public void testAttributeModification() {
        FileData fileData = new FileData("initial.txt", "/initial", "2000-01-01");
        fileData.name = "modified.txt";
        fileData.dir = "/modified";
        fileData.lastModifiedDate = "2001-01-01";
        assertEquals("modified.txt", fileData.name);
        assertEquals("/modified", fileData.dir);
        assertEquals("2001-01-01", fileData.lastModifiedDate);
    }

    @Test
    public void testEquality() {
        FileData fileData1 = new FileData("same.txt", "/same", "2022-01-01");
        FileData fileData2 = new FileData("same.txt", "/same", "2022-01-01");
        assertTrue(fileData1.equals(fileData2));
        assertEquals(fileData1.hashCode(), fileData2.hashCode());
    }
}
