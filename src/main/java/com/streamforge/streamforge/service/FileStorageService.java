package com.streamforge.streamforge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
        import java.util.UUID;

@Service
public class FileStorageService
{

    @Value("${video.storage.location}")
    private String storageLocation;

    public String store(MultipartFile file)
    {
        try
        {
            Path root = Paths.get(storageLocation);
            if (!Files.exists(root)) Files.createDirectories(root);

            String extension = getExtension(file.getOriginalFilename());
            String generatedName = UUID.randomUUID() + extension;

            Path target = root.resolve(generatedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return generatedName;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path load(String fileName)
    {
        return Paths.get(storageLocation).resolve(fileName);
    }

    private String getExtension(String filename)
    {
        int dot = filename.lastIndexOf('.');
        return dot == -1 ? "" : filename.substring(dot);
    }
}