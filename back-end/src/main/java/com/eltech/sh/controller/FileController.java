package com.eltech.sh.controller;


import com.eltech.sh.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FileController {

    private final CSVService csvService;

    @Autowired
    public FileController(CSVService csvService) {
        this.csvService = csvService;
    }

    @GetMapping("/csv/{user}")
    public void downloadCSV(HttpServletResponse response, @PathVariable String user) {
        Path path = new File(csvService.getFilePath(user)).toPath();
        if (Files.exists(path)) {
            response.addHeader("Content-Disposition", "attachment; filename=data.csv");
            try {
                Files.copy(path, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}