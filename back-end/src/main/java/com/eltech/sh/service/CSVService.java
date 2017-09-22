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
    private static final String FILE_NAME = "C:/Users/Admin/Documents/Neo4j/default.graphdb/import/opa.csv";

    public void save(Map<Integer, List<Integer>> map) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            fileWriter = new FileWriter(FILE_NAME, true);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            for(Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
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

    public void deleteCSV(){
        try {
            Files.deleteIfExists(new File(FILE_NAME).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
