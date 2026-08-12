package com.streamforge.streamforge.model;

import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Video
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String fileName;      // actual stored filename on disk (e.g. uuid.mp4)

    @Column(nullable = false)
    private String contentType;   // e.g. video/mp4

    @Column(nullable = false)
    private Long fileSize;        // bytes — you'll need this for range calculations later

    @Column(nullable = false)
    private LocalDateTime uploadDate;
}