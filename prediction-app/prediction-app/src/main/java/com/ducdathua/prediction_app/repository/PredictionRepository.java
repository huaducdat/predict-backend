package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

}
