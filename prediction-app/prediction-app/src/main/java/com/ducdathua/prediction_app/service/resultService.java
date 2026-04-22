package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.ResultRequest;
import com.ducdathua.prediction_app.exception.DuplicateDateException;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

@Service
public class resultService {
    private final ResultRepository repo;


    public resultService(ResultRepository repo) {
        this.repo = repo;
    }

    public List<Result> getAll() {
        return repo.findAllByOrderByDateDesc();
    }

    public Optional<Result> getByDate(LocalDate date) {
        return repo.findByDate(date);
    }

    public Result create(ResultRequest req) {
        boolean exists = repo.existsByDate(req.getDate());
        if (exists && !req.isForce()) {
            throw new DuplicateDateException();
        }
        Result result = repo.findByDate(req.getDate()).orElse(new Result());

        result.setDate(req.getDate());
        result.setNumbers(req.getNumbers());
        result.setSingleNumber(req.getSingleNumber());

        return repo.save(result);
    }


}


