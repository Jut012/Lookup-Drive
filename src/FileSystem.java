import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystem {

    MyHashMap<String, ArrayList<FileData>> nameMap;
    MyHashMap<String, ArrayList<FileData>> dateMap;

    public FileSystem() {
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
    }

    public FileSystem(String inputFile) {
        this();
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                add(data[0], data[1], data[2]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public boolean add(String fileName, String directory, String modifiedDate) {
        FileData newFile = new FileData(fileName, directory, modifiedDate);

        ArrayList<FileData> listByName = nameMap.get(fileName);
        if (listByName == null) {
            listByName = new ArrayList<>();
        }
        for (FileData fd : listByName) {
            if (fd.dir.equals(directory)) return false;  
        }
        listByName.add(newFile);
        nameMap.put(fileName, listByName);
    
        ArrayList<FileData> listByDate = dateMap.get(modifiedDate);
        if (listByDate == null) {
            listByDate = new ArrayList<>();
        }
        listByDate.add(newFile);
        dateMap.put(modifiedDate, listByDate);
        return true;
    }

    public FileData findFile(String name, String directory) {
        ArrayList<FileData> listByName = nameMap.get(name);
        if (listByName != null) {
            for (FileData fd : listByName) {
                if (fd.dir.equals(directory)) return fd;
            }
        }
        return null;
    }

    public ArrayList<String> findAllFilesName() {
        return new ArrayList<>(nameMap.keys());
    }

    public ArrayList<FileData> findFilesByName(String name) {
        ArrayList<FileData> files = nameMap.get(name);
        return files != null ? files : new ArrayList<>();
    }

    public ArrayList<FileData> findFilesByDate(String modifiedDate) {
        ArrayList<FileData> files = dateMap.get(modifiedDate);
        return files != null ? files : new ArrayList<>();
    }

    public ArrayList<FileData> findFilesInMultDir(String modifiedDate) {
    ArrayList<FileData> filesByDate = dateMap.get(modifiedDate);
    HashMap<String, List<FileData>> nameDirectoryMap = new HashMap<>();
    ArrayList<FileData> result = new ArrayList<>();

    if (filesByDate != null) {
        for (FileData file : filesByDate) {
            nameDirectoryMap.computeIfAbsent(file.name, k -> new ArrayList<>()).add(file);
        }
        for (List<FileData> fileList : nameDirectoryMap.values()) {
            if (fileList.size() > 1) {  // Check if more than one file with the same name exists in different directories
                result.addAll(fileList);
            }
        }
    }
    return result;
}

public boolean removeByName(String name) {
    ArrayList<FileData> filesToRemove = nameMap.get(name);
    if (filesToRemove == null) {
        return false;
    }

    boolean removed = false;
    for (FileData file : filesToRemove) {
        System.out.println("Removing file: " + file);
        ArrayList<FileData> filesByDate = dateMap.get(file.lastModifiedDate);
        if (filesByDate != null) {
            filesByDate.removeIf(f -> f.name.equals(name) && f.dir.equals(file.dir));
            if (filesByDate.isEmpty()) {
                dateMap.remove(file.lastModifiedDate);
                System.out.println("Removed date key: " + file.lastModifiedDate);
            }
            removed = true;
        }
    }
    nameMap.remove(name);
    System.out.println("Files removed for name: " + name);
    printMapContents();
    return removed;
}

private void printMapContents() {
    System.out.println("Current nameMap: ");
    for (String key : nameMap.keys()) {
        System.out.println("Key: " + key + ", Value: " + nameMap.get(key));
    }

    System.out.println("Current dateMap: ");
    for (String key : dateMap.keys()) {
        System.out.println("Key: " + key + ", Value: " + dateMap.get(key));
    }
}


    public boolean removeFile(String name, String directory) {
        ArrayList<FileData> filesByName = nameMap.get(name);
        if (filesByName != null) {
            boolean removed = filesByName.removeIf(f -> f.dir.equals(directory));
            if (removed) {
                if (filesByName.isEmpty()) {
                    nameMap.remove(name);
                }
                List<String> datesToRemove = new ArrayList<>();
                for (String date : new ArrayList<>(dateMap.keys())) {
                    ArrayList<FileData> filesByDate = dateMap.get(date);
                    boolean dateModified = filesByDate.removeIf(f -> f.name.equals(name) && f.dir.equals(directory));

                    if (dateModified && filesByDate.isEmpty()) {
                        datesToRemove.add(date);
                    }
                }
                for (String date : datesToRemove) {
                    dateMap.remove(date);
                }
                return true;
            }
        }
        return false;
    }

    private void printMaps() {
        System.out.println("nameMap: " + nameMap);
        System.out.println("dateMap: " + dateMap);
    }
}
