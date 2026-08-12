package com.streamforge.streamforge.controller;

import com.streamforge.streamforge.model.Video;
import com.streamforge.streamforge.repository.VideoRepository;
import com.streamforge.streamforge.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/videos")
public class VideoController
{

    private final VideoRepository videoRepository;
    private final FileStorageService fileStorageService;

    public VideoController(VideoRepository videoRepository, FileStorageService fileStorageService)
    {
        this.videoRepository = videoRepository;
        this.fileStorageService = fileStorageService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        Optional<Video> videoOpt = videoRepository.findById(id);
        if (videoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Video video = videoOpt.get();
        fileStorageService.delete(video.getFileName()); // deletes the file
        videoRepository.deleteById(id);                 // deletes the DB row

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Video>> listVideos(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Video> videos = videoRepository.findAll(pageable);
        return ResponseEntity.ok(videos);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Video> upload
            (
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description
            )
    {

        String storedFileName = fileStorageService.store(file);

        String contentType = file.getContentType();
        if (contentType == null || contentType.equals("application/octet-stream")) {
            contentType = "video/mp4"; // reasonable default for this project's scope
        }

        Video video = Video.builder()
                .title(title)
                .description(description)
                .fileName(storedFileName)
                .contentType(contentType)
                .fileSize(file.getSize())
                .uploadDate(LocalDateTime.now())
                .build();

        Video saved = videoRepository.save(video);
        return ResponseEntity.ok(saved);
    }
}