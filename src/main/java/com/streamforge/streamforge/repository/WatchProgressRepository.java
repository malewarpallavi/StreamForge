package com.streamforge.streamforge.repository;

import com.streamforge.streamforge.model.WatchProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WatchProgressRepository extends JpaRepository<WatchProgress, Long> {
    Optional<WatchProgress> findByUserIdentifierAndVideoId(String userIdentifier, Long videoId);
}
