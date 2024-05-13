package com.cqupt.software_10.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RequestMapping("/File")
@RestController
public class FileController {

    @Value("${file.path}")
    private String dirPath;

    @Value("softOperation.docx")
    private String optFileName;
    @Value("softInfo.pdf")
    private String introFile;


    @RequestMapping("/getOptFile")
    public String getOptFile() throws IOException {
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(optFileName)
                .toUriString();

        return fileDownloadUri;

    }
    @RequestMapping( "/getIntroFile")
    public String getIntroFile() throws IOException {
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(introFile)
                .toUriString();

        return fileDownloadUri;

    }

}
