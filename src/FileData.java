public class FileData {

    public String name;
    public String dir;
    public String lastModifiedDate;

    public FileData(String name, String directory, String modifiedDate) {
        this.name = name;
        this.dir = directory;
        this.lastModifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "{Name: " + name + ", Directory: " + dir + ", Modified Date: " + lastModifiedDate + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        FileData fileData = (FileData) obj;
        if (name != null ? !name.equals(fileData.name) : fileData.name != null) return false;
        if (dir != null ? !dir.equals(fileData.dir) : fileData.dir != null) return false;
        return lastModifiedDate != null ? lastModifiedDate.equals(fileData.lastModifiedDate) : fileData.lastModifiedDate == null;
    }

    @Override
    public int hashCode() {
        int result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (dir != null ? dir.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        return result;
    }
}
