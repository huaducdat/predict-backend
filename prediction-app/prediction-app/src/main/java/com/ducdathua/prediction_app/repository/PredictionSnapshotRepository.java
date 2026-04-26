package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.PredictionSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PredictionSnapshotRepository
        extends JpaRepository<PredictionSnapshot, Long> {

    Optional<PredictionSnapshot> findByDate(LocalDate date);
    Optional<PredictionSnapshot> findTopByOrderByDateDesc();
    List<PredictionSnapshot> findAllByOrderByDateDesc();
}