package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.TimeWeightGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TimeWeightGlobalRepository
        extends JpaRepository<TimeWeightGlobal, Long> {

    Optional<TimeWeightGlobal> findByDate(LocalDate date);

}