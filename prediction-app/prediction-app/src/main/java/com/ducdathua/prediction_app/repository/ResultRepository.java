package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByOrderByDateDesc();
    Optional<Result> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);
}
