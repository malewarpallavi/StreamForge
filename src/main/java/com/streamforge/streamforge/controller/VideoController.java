package com.streamforge.streamforge.controller;

import com.streamforge.streamforge.model.Video;
import com.streamforge.streamforge.repository.VideoRepository;
import com.streamforge.streamforge.service.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

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
            contentType = "video/mp4";
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        Optional<Video> videoOpt = videoRepository.findById(id);
        if (videoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Video video = videoOpt.get();
        fileStorageService.delete(video.getFileName());
        videoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam(value = "description", required = false) String description) {
        Optional<Video> videoOpt = videoRepository.findById(id);
        if (videoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Video video = videoOpt.get();
        video.setTitle(title);
        video.setDescription(description);
        return ResponseEntity.ok(videoRepository.save(video));
    }
}