package com.eltech.sh.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CSVService {
    private static final String NEW_LINE_SEPARATOR = "\n";

    public void save(Map<Integer, List<Integer>> map, String user) {
        createFile();
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            fileWriter = new FileWriter(getFilePath(user), true);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
                int userId = entry.getKey();
                for (Integer friendId : entry.getValue()) {
                    List<String> record = Arrays.asList(
                            String.valueOf(userId),
                            String.valueOf(friendId)
                    );
                    csvFilePrinter.printRecord(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteCSV(String user) {
        try {
            Files.deleteIfExists(new File(getFilePath(user)).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFile() {
        File folder = new File(System.getProperty("user.dir") +
                File.separator + "import");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public String getFilePath(String user) {
        return String.format("%s%2$simport%2$sdata-%s.csv", System.getProperty("user.dir"), File.separator, user);
    }
}
