package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.PredictionSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionSetRepository extends JpaRepository<PredictionSet, Long> {
}
