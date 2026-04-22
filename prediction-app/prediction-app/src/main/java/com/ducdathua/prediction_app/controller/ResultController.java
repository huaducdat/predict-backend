package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.ResultRequest;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.resultService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "http://localhost:5173")
public class ResultController {
    private final resultService service;

    public ResultController(resultService service) {
        this.service = service;
    }

    @GetMapping
    public List<Result> getAll() {
        return service.getAll();
    }

    @GetMapping("/date/{date}")
    public List<Result> getByDate(@PathVariable String date) {
        return service.getByDate(date);
    }

    @PostMapping
    public Result create(@Valid @RequestBody ResultRequest req) {
        Result r = new Result();
        r.setDate(req.getDate());
        r.setSingleNumber(req.getSingleNumber());
        r.setNumbers(req.getNumbers());
        return service.create(r);
    }
}
