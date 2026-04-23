package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.PredictionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Long> {
    Optional<PredictionResult> findByDate(LocalDate date);
}