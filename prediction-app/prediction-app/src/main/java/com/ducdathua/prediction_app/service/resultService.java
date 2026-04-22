package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.ResultRequest;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class resultService {
    private final ResultRepository repo;


    public resultService(ResultRepository repo) {
        this.repo = repo;
    }

    public List<Result> getAll() {
        return repo.findAll();
    }

    public List<Result> getByDate(String date) {
        return repo.findByDate(date);
    }

    public Result create(Result r) {
        return repo.save(r);
    }


}


