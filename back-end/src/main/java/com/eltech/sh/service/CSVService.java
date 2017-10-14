package com.eltech.sh.service;

import java.util.List;
import java.util.Map;

public interface CSVService {
    void saveToSeparateFile(Map<Integer, List<Integer>> map, String userID);

    void deleteCSV(String user);

    String getFilePath(String user);
}
