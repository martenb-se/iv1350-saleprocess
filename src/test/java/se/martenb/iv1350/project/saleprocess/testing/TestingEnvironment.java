package se.martenb.iv1350.project.saleprocess.testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * An object creator for tests, helping with the creation of objects used
 * in various tests. 
 * 
 * An environment creator for tests, helping with repeated setup and cleaning.
 */
public class TestingEnvironment {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private static final String LOG_FILE_NAME = "error_log.txt";
    private static final String LOG_FILE_BACKUP_NAME = "error_log.txt-backup";
    private boolean IS_LOG_FILE_EXISTING = false;
    
    public void redirectSystemOut() {
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }
    
    public void restoreSystemOut() {
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }
    
    public String getRedirectedSystemOut() {
        return printoutBuffer.toString();
    }
    
    private boolean fileExists(String fileToLookFor) {
        Path fileToLookForPath = Paths.get(fileToLookFor);
        boolean doesFileExist = Files.exists(fileToLookForPath);
        return doesFileExist;
    }
    
    private void copyFile(String sourceFile, String destinationFile) 
            throws IOException {
        Path source = Paths.get(sourceFile);
        Path destination = Paths.get(destinationFile);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }
    
    private void deleteFile(String fileToDelete) throws IOException {
        Path fileToDeletePath = Paths.get(fileToDelete);
        Files.delete(fileToDeletePath);
    }
    
    public void backupErrorLog() throws IOException {
        if(fileExists(LOG_FILE_NAME)) {
            IS_LOG_FILE_EXISTING = true;
            copyFile(LOG_FILE_NAME, LOG_FILE_BACKUP_NAME);
        }
    }
    
    public void restoreErrorLog() throws IOException {
        if (IS_LOG_FILE_EXISTING) {
            copyFile(LOG_FILE_BACKUP_NAME, LOG_FILE_NAME);
            deleteFile(LOG_FILE_BACKUP_NAME);
        } else {
            deleteFile(LOG_FILE_NAME);
        }
    }
    
    public boolean findTextInErrorLog(String textSearch) throws IOException {
        Path fileToSearchPath = Paths.get(LOG_FILE_NAME);
        Scanner scannerSearch = new Scanner(fileToSearchPath);
        boolean haveFoundText = false;
        while (scannerSearch.hasNextLine()) {
            String currentLine = scannerSearch.nextLine();
            if(currentLine.contains(textSearch)) {
                haveFoundText = true;
                break;
            }
        }
        return haveFoundText;
    }
    
}
