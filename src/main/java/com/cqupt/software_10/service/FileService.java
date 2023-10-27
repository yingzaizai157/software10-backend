package com.cqupt.software_10.service;

import com.cqupt.software_10.common.UploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {


    public UploadResult fileUpload(MultipartFile file, String newName, String disease, String createName) throws IOException;

    public UploadResult creatUpTable(MultipartFile file, String newName, String disease, String createName) throws IOException;
}
