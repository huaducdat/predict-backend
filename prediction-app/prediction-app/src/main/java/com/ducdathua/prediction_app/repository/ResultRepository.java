package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
List<Result> findByDate(String date);
}
