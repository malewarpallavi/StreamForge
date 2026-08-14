package com.streamforge.streamforge.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "watch_progress",
       uniqueConstraints = @UniqueConstraint(columnNames = {"userIdentifier", "video_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WatchProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userIdentifier;

    @Column(nullable = false)
    private Integer positionSeconds;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;
}
