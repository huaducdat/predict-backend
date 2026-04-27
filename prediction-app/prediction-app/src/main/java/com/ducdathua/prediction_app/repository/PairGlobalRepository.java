package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.PairGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PairGlobalRepository extends JpaRepository<PairGlobal, Long> {

    Optional<PairGlobal> findByDate(LocalDate date);

    void deleteByDate(LocalDate date);
}