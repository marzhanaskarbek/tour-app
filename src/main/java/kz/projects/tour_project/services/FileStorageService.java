package kz.projects.tour_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    @Qualifier("resourceLoaderBean")
    ResourceLoader resourceLoader;

    public String saveFile(MultipartFile file, String fileStoragePath) throws IOException {
        String fileName =  UUID.randomUUID() + "_" + file.getOriginalFilename();
        Resource staticResource = resourceLoader.getResource("classpath:static" + fileStoragePath);
        String filePath = staticResource.getFile().getAbsolutePath() + File.separator + fileName;
        File destFile = new File(filePath);
        file.transferTo(destFile);
        return fileStoragePath + fileName;
    }
}