package com.streamforge.streamforge.controller;

import com.streamforge.streamforge.model.Video;
import com.streamforge.streamforge.repository.VideoRepository;
import com.streamforge.streamforge.service.FileStorageService;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/videos")
public class VideoStreamController {

    private final VideoRepository videoRepository;
    private final FileStorageService fileStorageService;

    public VideoStreamController(VideoRepository videoRepository, FileStorageService fileStorageService) {
        this.videoRepository = videoRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> stream(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader HttpHeaders headers) throws IOException {

        Optional<Video> videoOpt = videoRepository.findById(id);
        if (videoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Video video = videoOpt.get();
        Path filePath = fileStorageService.load(video.getFileName());
        UrlResource resource = new UrlResource(filePath.toUri());

        long contentLength = resource.contentLength();
        List<HttpRange> ranges = headers.getRange();

        ResourceRegion region;
        if (ranges.isEmpty()) {
            // no Range header — return full file as one region
            region = new ResourceRegion(resource, 0, contentLength);
        } else {
            // take the first requested range (browsers send one at a time)
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(1024 * 1024, end - start + 1); // cap chunk size, optional
            region = new ResourceRegion(resource, start, rangeLength);
        }

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType(video.getContentType()))
                .body(region);
    }
}